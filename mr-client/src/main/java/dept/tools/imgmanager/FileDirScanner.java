package dept.tools.imgmanager;

import javax.swing.*;
import java.io.File;

/**
 * Класс сканирует директорию [rootDirectoryPath] на наличие файлов и папок (и
 * их вложения) все найденое добавляет в [outputField]
 *
 * @author Andy
 * @version 1.0
 */
public class FileDirScanner implements Runnable {
    public File rootDirectory;
    private DefaultListModel<String> outputField;

    public FileDirScanner(String rootDirectoryPath,
                          DefaultListModel<String> outputField) {
        this.outputField = outputField;
        File file = new File(rootDirectoryPath);
        if (file.exists() && file.isDirectory()) {
            rootDirectory = file;
        }
    }

    @Override
    public void run() {
        scanDirectory(rootDirectory);
    }


    private void scanDirectory(File directory) {
        File[] files = directory.listFiles();
        if (files != null) {
            for (File f : files) {
                final String path = f.getAbsolutePath();
                if (SwingUtilities.isEventDispatchThread()) {
                    outputField.addElement(path);
                } else {
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            outputField.addElement(path);
                            System.out.println(path);
                        }
                    });
                }
                if (f.isDirectory() && !f.isHidden()) {
                    scanDirectory(f);
                }
            }
        }
    }
}
