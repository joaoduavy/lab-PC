import os
import sys

def wc(content):
    return len(content.split())

def wc_file(filename):
    try:
        with open(filename, 'r', encoding='latin-1') as f:
            file_content = f.read()
        return wc(file_content)
    except FileNotFoundError:
        return 0

def wc_dir(dir_path, output_file):
    count = 0
    with open(output_file, 'a') as out_file:
        for filename in os.listdir(dir_path):
            filepath = os.path.join(dir_path, filename)
            if os.path.isfile(filepath):
                count += wc_file(filepath)
        out_file.write(str(count) + '\n')

def main():
    if len(sys.argv) != 3:
        print("Usage: python", sys.argv[0], "directory_path", "outputfile_path")
        return
    dir_path = os.path.abspath(sys.argv[1])
    output_file = os.path.abspath(sys.argv[2])
    wc_dir(dir_path, output_file)

if __name__ == "__main__":
    main()
