import os
import sys
from threading import Thread, Semaphore

count = 0
sem = Semaphore(1)
threads = []

def wc(content):
    return len(content.split())

def wc_file(filename):
    try:
        with open(filename, 'r', encoding='latin-1') as f:
            file_content = f.read()
            n = wc(file_content)
            sem.acquire()
            global count  # Indica que estamos usando a variÃ¡vel global 'count'
            count += n
            sem.release()
    except FileNotFoundError:
        return 0

def wc_dir(dir_path, output_file):
	with open(output_file, 'a') as out_file:
		for filename in os.listdir(dir_path):
			filepath = os.path.join(dir_path, filename)
			if os.path.isfile(filepath):
				wc_file_thread = Thread(target=wc_file, args=[filepath])
				threads.append(wc_file_thread)
				wc_file_thread.start()
			elif os.path.isdir(filepath):
				wc_dir(filepath)
		for thread in threads:
			wc_file_thread.join()
		out_file.write(str(count) + '\n')

def main():
    if len(sys.argv) != 3:
        print("Usage: python", sys.argv[0], "root_directory_path output_file_path")
        return
    root_path = os.path.abspath(sys.argv[1])
    output_file = os.path.abspath(sys.argv[2])
    wc_dir(root_path, output_file)

if __name__ == "__main__":
    main()
