@echo off
echo Compilando Simulador F1...
javac -d bin src/model/*.java src/database/*.java src/gui/*.java src/Main.java
if %errorlevel% neq 0 (
    echo Error de compilacion.
    pause
    exit /b %errorlevel%
)
echo Iniciando...
java -cp bin Main
