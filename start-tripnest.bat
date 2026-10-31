@echo off
title TripNest Full-Stack Launcher
echo ===================================================
echo             Starting TripNest Platform
echo ===================================================
echo.

:: 1. Verify PostgreSQL Service
echo [1/3] Checking PostgreSQL Service...
sc query postgresql-x64-18 2>nul | findstr /i "RUNNING" >nul
if %ERRORLEVEL% equ 0 (
    echo [OK] PostgreSQL Service is RUNNING.
) else (
    echo [!] Attempting to start PostgreSQL service...
    net start postgresql-x64-18 >nul 2>&1
)
echo.

:: 2. Launch Spring Boot Backend in a new window
echo [2/3] Starting Spring Boot Backend on Port 8081...
start "TripNest Backend - Spring Boot" /D "%~dp0tripnest-backend" cmd /k "mvnw.cmd spring-boot:run"

:: Wait 5 seconds
ping 127.0.0.1 -n 6 >nul

:: 3. Launch React Frontend in a new window
echo [3/3] Starting React Frontend on Port 5173...
start "TripNest Frontend - Vite React" /D "%~dp0tripnest-frontend" cmd /k "npm.cmd run dev"

:: Wait 4 seconds
ping 127.0.0.1 -n 5 >nul

:: 4. Open in Default Browser
echo.
echo ===================================================
echo     TripNest is launching! Opening your browser...
echo ===================================================
start http://localhost:5173/

echo.
echo Both Frontend (http://localhost:5173) and Backend (http://localhost:8081) have been started!
echo Keep those terminal windows open while using TripNest.
echo.
pause