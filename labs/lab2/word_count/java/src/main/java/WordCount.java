import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.CyclicBarrier;

public class WordCount {

    public static int count;
    public static ReentrantLock mutex;
    public static CyclicBarrier barreira;
    
    // Calculate the number of words in the files stored under the directory name
    // available at argv[1].
    //
    // Assume a depth 3 hierarchy:
    //   - Level 1: root
    //   - Level 2: subdirectories
    //   - Level 3: files
    //
    // root
    // ├── subdir 1
    // │     ├── file
    // │     ├── ...
    // │     └── file
    // ├── subdir 2
    // │     ├── file
    // │     ├── ...
    // │     └── file
    // ├── ...
    // └── subdir N
    // │     ├── file
    // │     ├── ...
    // │     └── file
    public static void main(String[] args) {
        if (args.length != 1) {
            System.err.println("Usage: java WordCount <root_directory>");
            System.exit(1);
        }

        //MyThread t0 = new MyThread();
        //t0.start();
        // ------------------------------------------
        //MinhaRunnable minhaRunnable = new MinhaRunnable("Olá, mundo!");
        //Thread thread = new Thread(minhaRunnable);
        //thread.start();
        
        String rootPath = args[0];
        File rootDir = new File(rootPath);
        File[] subdirs = rootDir.listFiles();

        mutex =  new ReentrantLock();
        barreira = new CyclicBarrier(subdirs.length + 1);
        count = 0;

        if (subdirs != null) {
            for (File subdir : subdirs) {
                if (subdir.isDirectory()) {
                    String dirPath = rootPath + "/" + subdir.getName();
                    MyOwnRunnable run = new MyOwnRunnable(dirPath);
                    Thread thread = new Thread(run);
                    thread.start();
                }
            }
        }

        try {
            barreira.await();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(count);
    }

    public static int wc(String fileContent) {
        String[] words = fileContent.split("\\s+");
        return words.length;
    }

    public static int wcFile(String filePath) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filePath));
            StringBuilder fileContent = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                fileContent.append(line).append("\n");
            }

            reader.close();
            return wc(fileContent.toString());

        } catch (IOException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public static int wcDir(String dirPath) {
        File dir = new File(dirPath);
        File[] files = dir.listFiles();
        int count = 0;

        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    count += wcFile(file.getAbsolutePath());
                }
            }
            return count;
        }
        return count;
    }

    // n requer uma instancia da outra classe para ser instanciada
     static class MyOwnRunnable implements Runnable {
    
        String dirPath;

        public MyOwnRunnable(String dirPath) {
            this.dirPath = dirPath;
            
        }

        @Override
        public void run() {
            int words =  wcDir(dirPath);
            mutex.lock();
            try {
                count += words;
                System.out.println("Thread está na seção crítica. " + count);
            } finally {
                mutex.unlock();
            }

            try {
                barreira.await();
            } catch (Exception e) {
                e.printStackTrace();
            }
            
        }
    }

}








