package by.gomel.freedev.ucframework.common;

import javax.swing.*;
import java.io.File;
import java.io.FileWriter;
import java.util.List;

/**
 * Сканирует указанную дирректорию рекурсивно и помещает в список
 * @author Andy 20.12.2017.
 */
public class DirectoryScanner implements Runnable {

    public File rootDirectory;
    private List<String> outputField;

    public DirectoryScanner(String baseDir,
                            List<String> outputField) {
        this.outputField = outputField;
        File file = new File(baseDir);
        if (file.exists() && file.isDirectory()) {
            rootDirectory = file;
        }
    }

    @Override
    public void run() {
        scanDirectory(rootDirectory);
    }

    public void start() {
        scanDirectory(rootDirectory);
    }

    private void scanDirectory(File directory) {
        File[] files = directory.listFiles();
        if (files != null) {
            for (File f : files) {
                final String path = f.getAbsolutePath();
                if (SwingUtilities.isEventDispatchThread()) {
                    outputField.add(path);
                } else {
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            outputField.add(path);
                        }
                    });
                }
                if (f.isDirectory() && !f.isHidden()) {
                    scanDirectory(f);
                }
            }
        }
    }

    /**
     * Сохраняет результат сканирования в файл
     *
     * @param fileName имя файла
     */
    public void saveToFile(String fileName) {
        try {
            FileWriter writer = new FileWriter(fileName);
            for (String str : outputField) {
                writer.write(str);
            }
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * Возвращает результат сканирования
     *
     * @return список результата сканирования
     */
    public List<String> getScanResult() {
        return outputField;
    }

}
