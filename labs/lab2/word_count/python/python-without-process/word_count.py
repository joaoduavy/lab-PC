import os
import sys
import threading

def wc(content):
    return len(content.split())

def wc_file(filename):
    try:
        with open(filename, 'r', encoding='latin-1') as f:
            file_content = f.read()
        return wc(file_content)
    except FileNotFoundError:
        return 0

def wc_subdir(dir_path):
    x = 0
    for filename in os.listdir(dir_path):
        filepath = os.path.join(dir_path, filename)
        if os.path.isfile(filepath):
            x += wc_file(filepath)
    return x

def wc_dir(dir_path):
    mutex = threading.Lock()
    barrier = threading.Barrier(len(os.listdir(dir_path)) + 1)
    
    for filename in os.listdir(dir_path):
        filepath = os.path.join(dir_path, filename)
        if os.path.isdir(filepath):
            thread = threading.Thread(target=run, args=(mutex, barrier, filepath))
            thread.start()
    
    barrier.wait()

    return count

def run(mutex, barrier, filePath):
    global count
    result = wc_subdir(filePath)
    mutex.acquire()
    count = count + result
    mutex.release()

    barrier.wait()
    


def main():
    if len(sys.argv) != 2:
        print("Usage: python", sys.argv[0], "root_directory_path")
        return
    root_path = os.path.abspath(sys.argv[1])
    global count
    wc_dir(root_path)
    print(count)

count = 0
if __name__ == "__main__":
    main()
