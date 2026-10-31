Write-Host "===================================================" -ForegroundColor Cyan
Write-Host "            Starting TripNest Platform            " -ForegroundColor Cyan
Write-Host "===================================================" -ForegroundColor Cyan
Write-Host ""

# 1. PostgreSQL check
Write-Host "[1/3] Checking PostgreSQL Service..." -ForegroundColor Yellow
$pgService = Get-Service -Name "*postgres*" -ErrorAction SilentlyContinue | Where-Object { $_.Status -eq 'Running' }
if ($pgService) {
    Write-Host "[OK] PostgreSQL Service is RUNNING ($($pgService.Name))." -ForegroundColor Green
} else {
    Write-Host "[!] Attempting to start PostgreSQL service..." -ForegroundColor Yellow
    Start-Service -Name "postgresql-x64-18" -ErrorAction SilentlyContinue
}

# 2. Launch Backend
Write-Host "[2/3] Starting Spring Boot Backend (Port 8081)..." -ForegroundColor Yellow
$backendPath = Join-Path $PSScriptRoot "tripnest-backend"
Start-Process -FilePath "cmd.exe" -ArgumentList "/k", "mvnw.cmd spring-boot:run" -WorkingDirectory $backendPath

Start-Sleep -Seconds 5

# 3. Launch Frontend
Write-Host "[3/3] Starting Vite React Frontend (Port 5173)..." -ForegroundColor Yellow
$frontendPath = Join-Path $PSScriptRoot "tripnest-frontend"
Start-Process -FilePath "cmd.exe" -ArgumentList "/k", "npm.cmd run dev" -WorkingDirectory $frontendPath

Start-Sleep -Seconds 4

# 4. Open Browser
Write-Host "`n===================================================" -ForegroundColor Green
Write-Host "  TripNest is launching! Opening browser...       " -ForegroundColor Green
Write-Host "===================================================" -ForegroundColor Green
Start-Process "http://localhost:5173/"

Write-Host "`nBoth Backend and Frontend windows are open. Keep them open while working!`n" -ForegroundColor Cyan