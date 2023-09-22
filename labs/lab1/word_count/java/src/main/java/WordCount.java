import java.io.*;
import java.nio.file.*;
import java.util.*;

public class WordCount {

    public static void main(String[] args) {
        if (args.length != 2) {
            System.err.println("Usage: java WordCount <directory_path> <output_file_path>");
            System.exit(1);
        }

        String rootPath = args[0];
        String outputFilePath = args[1];
        wcDir(rootPath, outputFilePath);
    }

    public static int wc(String fileContent) {
        String[] words = fileContent.split("\\s+");
        return words.length;
    }

    public static int wcFile(String filename) {
        try {
            String fileContent = new String(Files.readAllBytes(Paths.get(filename)), "UTF-8");
            return wc(fileContent);
        } catch (IOException e) {
            return 0;
        }
    }

    public static void wcDir(String dirPath, String outputFilePath) {
        File output = new File(outputFilePath);
        int count = 0;

        try (FileWriter writer = new FileWriter(output, true)) {
            File dir = new File(dirPath);
            File[] files = dir.listFiles();

            if (files != null) {
                for (File file : files) {
                    if (file.isFile()) {
                        count += wcFile(file.getAbsolutePath());
                    }
                }
                writer.write(count + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
