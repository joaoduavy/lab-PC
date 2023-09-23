#!/bin/bash

directory="$1"

outputFilePath=$(mktemp)
echo "Arquivo temp criado em: $outputFilePath"

open_dir() {
	local root
	root=$(ls -1 "$1")
	
	n_procs=0
	pids=()
	for item in $root; do
		if [ -d "$directory/$item" ]; then
			python3 ./word_count.py "$directory/$item" "$outputFilePath" &
			pids[${n_procs}]=$!
			((n_procs++))
		else
			echo "$item is not a dir"
		fi
	done

	for ((i = 0; i < n_procs; i++)); do
		wait ${pids[${i}]}
	done

	total=0
	while IFS= read -r line; do
		total=$((total + line))
	done < "$outputFilePath"

	echo "Some total do valores no arquivo: $total"

	rm "$outputFilePath"
}

open_dir "$directory"
