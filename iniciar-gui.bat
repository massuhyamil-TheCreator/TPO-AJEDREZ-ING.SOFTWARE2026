@echo off
setlocal
cd /d "%~dp0"
call "%~dp0mvnw.cmd" javafx:run
if errorlevel 1 (
    echo.
    echo No se pudo iniciar la interfaz. Revisa el mensaje anterior.
    echo Necesitas JDK 17 o superior y conexion a internet la primera vez.
    pause
    exit /b 1
)
