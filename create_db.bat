@echo off
set PGPASSWORD=root
psql -U postgres -h localhost -p 5432 -f create_db.sql
echo Done.
pause
