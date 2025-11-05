package by;

import by.march8.ecs.Bootstrap;
import by.march8.ecs.framework.common.Settings;

import javax.swing.*;
import java.io.File;

/**
 * Руннер приложения
 * Created by Andy on 31.07.2015.
 */
public class Runner {
    static String splitter = File.separator;

    /**
     * Main метод раннера, получаем класспаз приложения, сомтрим наличие папки ресурсов
     * и в случае успехапринимаем каталог как корневой
     *
     * @param args аргументы запуска
     */
    public static void main(String[] args) {
        String runPath = "";
        try {
            String tmpRunPath = (new File("")).getAbsolutePath();
            if (getResourcesDirectory(tmpRunPath)) {
                runPath = tmpRunPath;
            } else {
                tmpRunPath = getResourcesDirectory();
                if (!tmpRunPath.equals("")) {
                    runPath = tmpRunPath;
                } else {
                    runPath = "";
                }
            }
        } catch (Exception e) {
            System.err.println("Невозможно получить доступ к папке запуска MyReports20 ");
            e.printStackTrace();
        }

        if (!runPath.equals("")) {
            System.out.println("RUN PATH FOR APPLICATION ..." + runPath);
            System.out.println("START APPLICATION ..." + Settings.PROGRAM_NAME);
        } else {
            System.out.println("RUN PATH IS NOT IDENTIFIED...");
        }

        final String finalRunPath = runPath;
        SwingUtilities.invokeLater(() -> new Bootstrap(finalRunPath, args));
    }

    private static String getResourcesDirectory() {
        File fileList = new File(System.getProperty("java.class.path"));
        File dir = fileList.getParentFile();
        String[] classpathEntries = dir.toString().split(File.pathSeparator);
        for (String s : classpathEntries) {
            if (getResourcesDirectory(s)) {
                return s;
            }
        }
        return "";
    }

    private static boolean getResourcesDirectory(String path) {
        String file = path + splitter + "Img" + splitter;
        File fileRes = new File(file);
        return (fileRes.exists()) && (fileRes.isDirectory());
    }
}
