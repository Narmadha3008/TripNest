$mysqlPath = "C:\Program Files\MySQL\MySQL Server 8.0\bin\mysql.exe"
$user = "root"
$pass = "Saki@123"
$db = "tripnest"

$sqlLines = @()
$sqlLines += "-- ==========================================================="
$sqlLines += "-- TripNest: MySQL to PostgreSQL Migration Script"
$sqlLines += "-- Generated automatically on $(Get-Date -Format 'yyyy-MM-dd HH:mm:ss')"
$sqlLines += "-- ==========================================================="
$sqlLines += ""

# Tables in dependency order
$tables = @('users', 'destination', 'trips', 'trip_collaborators', 'itinerary', 'expense', 'documents', 'chat_messages', 'notification')

function Escape-Sql([string]$val) {
    if ($null -eq $val) { return "NULL" }
    $escaped = $val.Replace("'", "''")
    return "'$escaped'"
}

foreach ($tbl in $tables) {
    # Get columns
    $colQuery = "SELECT COLUMN_NAME, DATA_TYPE FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA='$db' AND TABLE_NAME='$tbl' ORDER BY ORDINAL_POSITION;"
    $colData = & $mysqlPath -u $user "-p$pass" -N -e $colQuery
    
    $cols = @()
    $colTypes = @{}
    foreach ($line in ($colData -split "`r?`n")) {
        if (-not [string]::IsNullOrWhiteSpace($line)) {
            $parts = $line -split "`t"
            $colName = $parts[0].Trim()
            $colType = $parts[1].Trim()
            $cols += $colName
            $colTypes[$colName] = $colType
        }
    }
    
    if ($cols.Count -eq 0) { continue }
    
    $colList = ($cols | ForEach-Object { "`"$_`"" }) -join ", "
    
    # Query all rows in TSV
    $selectCols = ($cols | ForEach-Object { "IFNULL($_, '___NULL___')" }) -join ", "
    $dataQuery = "SELECT $selectCols FROM $db.$tbl;"
    $rows = & $mysqlPath -u $user "-p$pass" -N -e $dataQuery
    
    $sqlLines += "-- -----------------------------------------------------------"
    $sqlLines += "-- Table: $tbl"
    $sqlLines += "-- -----------------------------------------------------------"
    
    $rowCount = 0
    foreach ($row in ($rows -split "`r?`n")) {
        if ([string]::IsNullOrWhiteSpace($row)) { continue }
        $vals = $row -split "`t"
        if ($vals.Count -ne $cols.Count) { continue }
        
        $valSql = @()
        for ($i = 0; $i -lt $cols.Count; $i++) {
            $cName = $cols[$i]
            $cType = $colTypes[$cName]
            $v = $vals[$i]
            
            if ($v -eq '___NULL___') {
                $valSql += "NULL"
            } elseif ($cType -eq 'tinyint' -and ($cName -like "is_*" -or $cName -like "has_*")) {
                if ($v -eq '1') { $valSql += "TRUE" } else { $valSql += "FALSE" }
            } elseif ($cType -in @('int', 'bigint', 'smallint', 'tinyint', 'double', 'float', 'decimal')) {
                $valSql += $v
            } else {
                $valSql += (Escape-Sql $v)
            }
        }
        
        $sqlLines += "INSERT INTO $tbl ($colList) VALUES ($($valSql -join ', '));"
        $rowCount++
    }
    
    # Reset sequence if table has id column
    if ($cols -contains 'id') {
        $sqlLines += "SELECT setval(pg_get_serial_sequence('$tbl', 'id'), COALESCE(MAX(id), 1)) FROM $tbl;"
    }
    $sqlLines += ""
    Write-Host "Processed ${tbl} - $rowCount rows exported"
}

$targetFile = "C:\Users\Sakanasri S\OneDrive\Desktop\tripnest final\migrate_to_postgres.sql"
$sqlLines | Out-File -FilePath $targetFile -Encoding utf8
Write-Host "Migration SQL written to: $targetFile"

