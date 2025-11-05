package by.gomel.freedev.ucframework.uccore.utils;

import by.march8.ecs.MainController;
import by.march8.ecs.framework.common.Settings;

import java.io.File;
import java.net.MalformedURLException;
import java.util.Date;
import java.util.Objects;

/**
 * Created by Andy on 19.03.2015.
 */
@SuppressWarnings("all")
public class SystemUtils {
    public static Date dateApplication;

    public static boolean isWindows() {
        String os = System.getProperty("os.name").toLowerCase();
        if (os.indexOf("win") >= 0) {
            return true;
        } else {
            return false;
        }
    }

    public static java.net.URL convertPathToUrl(String path) {
        try {
            return new File(path).toURI().toURL();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String convertPathToSimpleUrl(String path) {
        if (isWindows()) {
            return "file:///" + path;
        } else {
            return "file://" + path;
        }
    }

    /**
     * Возвращает ссылку на исполнимый файл приложения
     *
     * @param controller
     * @return исполнимый файл приложения
     */
    public static long getApplicationFileSize() {
        String sourcePath = "";
        if (SystemUtils.isWindows()) {
            sourcePath = Settings.WINDOWS_SOURCE_PATH;
        } else {
            sourcePath = Settings.UNIX_SOURCE_PATH;
        }

        File file = new File(sourcePath + "/MyReports.jar");

        if (file.exists()) {
            return file.length();
        } else {
            System.out.println("Application file is not found for path [" + sourcePath + "MyReports.jar]");
            return -1L;
        }
    }

    public static boolean applicationIsOutdated() {
        Long sourceFileSize = SystemUtils.getApplicationFileSize();
        Long runFileSize = SystemUtils.getSelfApplicationFileSize();
        //Long file
        if (!Objects.equals(sourceFileSize, runFileSize)) {
            System.out.println("Executable files is not identical" + "[" + runFileSize + "][" + sourceFileSize + "]");
            return true;
        } else {
            System.out.println("Executable files is identical " + "[" + runFileSize + "][" + sourceFileSize + "]");
            return false;
        }
    }

    public static String convertPathToFileName(String path) {
        return new File(path).getName();
    }

    public static String convertPathToShortFileName(String path) {
        String tmp = convertPathToFileName(path);
        return tmp.substring(0, tmp.lastIndexOf('.'));
    }

    public static Long getSelfApplicationFileSize() {
        File file = new File(MainController.getRunPath() + "/MyReports.jar");
        if (file.exists()) {
            return file.length();
        } else {
            System.out.println("Application file is not found for path [" + MainController.getRunPath() + "/MyReports.jar]");
            return -1L;
        }
    }
}
