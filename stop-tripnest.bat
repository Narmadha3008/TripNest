@echo off
title Stop TripNest Servers
echo ===================================================
echo             Stopping TripNest Servers
echo ===================================================
echo.

echo Stopping processes on port 8081 (Backend)...
for /f "tokens=5" %%a in ('netstat -aon ^| find ":8081" ^| find "LISTENING"') do (
    taskkill /F /PID %%a 2>nul
)

echo Stopping processes on port 5173 (Frontend)...
for /f "tokens=5" %%a in ('netstat -aon ^| find ":5173" ^| find "LISTENING"') do (
    taskkill /F /PID %%a 2>nul
)

echo.
echo [OK] TripNest servers stopped cleanly.
pause
