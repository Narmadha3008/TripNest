@echo off
set PGPASSWORD=root
echo === Testing postgres user (password: root) ===
psql -U postgres -h localhost -p 5432 -c "SELECT usename FROM pg_user;" 2>&1
echo.
echo === Checking if tripnest DB exists ===
psql -U postgres -h localhost -p 5432 -c "SELECT datname FROM pg_database WHERE datname='tripnest';" 2>&1
echo.
echo === If above failed, trying without password (trust auth) ===
psql -U postgres -p 5432 -c "SELECT usename FROM pg_user;" 2>&1
