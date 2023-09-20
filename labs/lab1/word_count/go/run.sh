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
    pids=() 

    for item in $root; do
        if [ -d "$directory/$item" ]; then
            echo "about to go run world_count $directory/$item"
            go run ./word_count.go "$directory/$item" "$outputFilePath" &
            pids[${n_procs}]=$! 
            ((n_procs++))
        else
            echo "$item"
            echo "do nothing"
        fi
    done

    
    for ((i = 0; i < n_procs; i++)); do
        wait ${pids[${i}]}
    done

    echo "Todos os processos concluídos."

    
    total=0
    while IFS= read -r line; do
        total=$((total + line))
    done < "$outputFilePath"

    echo "Soma total dos valores no arquivo: $total"

    
    rm "$outputFilePath"
    echo "Arquivo temporário removido."
}

open_dir "$directory"

