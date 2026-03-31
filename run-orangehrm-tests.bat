@echo off
setlocal

cd /d "%~dp0"

echo Running OrangeHRM test suite...
call mvn clean test

echo.
if %ERRORLEVEL% EQU 0 (
    echo OrangeHRM test execution completed successfully.
) else (
    echo OrangeHRM test execution failed. Check the Maven output above.
)

echo.
pause
