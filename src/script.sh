#!/bin/bash

# Define o nome do diretório
dir="Teste"

# Verifica se o diretório já existe
if [ -d "$dir" ]; then
   # Se o diretório existir, ele será removido
   rm -r "$dir"
fi

# Cria o diretório novamente
mkdir "$dir"

# Compila todos os arquivos .java no diretório atual e no diretório "system"
# e coloca os arquivos .class no diretório "Teste"
javac -d "$dir" $(find . -name "*.java")
