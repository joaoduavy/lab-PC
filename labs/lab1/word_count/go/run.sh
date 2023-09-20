#!/bin/bash

directory="$1"
number_words=0

# Crie o arquivo outputfile.txt como temporário
outputFilePath=$(mktemp)
echo "Arquivo temporário criado em: $outputFilePath"

open_dir() {
    local root
    root=$(ls -1 "$1")

    n_procs=0
    pids=() # Array para armazenar os PIDs dos processos

    for item in $root; do
        if [ -d "$directory/$item" ]; then
            echo "about to go run world_count $directory/$item"
            go run ./word_count.go "$directory/$item" "$outputFilePath" &
            pids[${n_procs}]=$! # Armazena o PID no índice correspondente de pids
            ((n_procs++))
        else
            echo "$item"
            echo "do nothing"
        fi
    done

    # Espera por todos os PIDs
    for ((i = 0; i < n_procs; i++)); do
        wait ${pids[${i}]}
    done

    echo "Todos os processos concluídos."

    # Após todos os subprocessos concluírem, leia e some os valores no arquivo
    total=0
    while IFS= read -r line; do
        total=$((total + line))
    done < "$outputFilePath"

    echo "Soma total dos valores no arquivo: $total"

    # Remova o arquivo temporário
    rm "$outputFilePath"
    echo "Arquivo temporário removido."
}

open_dir "$directory"

