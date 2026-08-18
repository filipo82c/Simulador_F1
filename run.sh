#!/bin/bash
echo "Compilando Simulador F1..."
mkdir -p bin
javac -d bin src/model/*.java src/database/*.java src/gui/*.java src/Main.java
if [ $? -eq 0 ]; then
    echo "Iniciando..."
    java -cp bin Main
else
    echo "Error de compilacion."
fi
