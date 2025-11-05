package by.gomel.freedev.ucframework.uccore.utils;

import by.march8.ecs.framework.common.Settings;

import java.io.File;

/**
 * @author Andy 15.10.2014.
 */
public class FileUtils {

    public static String extractPath(String path) {
        File file = new File(path);
        String absolutePath = file.getAbsolutePath();
        return absolutePath.
                substring(0, absolutePath.lastIndexOf(File.separator));
    }

    private static void prepareDirs() {
        File workDir = new File(Settings.HOME_DIR);
        if (!workDir.exists()) {
            workDir.mkdir();
        }
        workDir = new File(Settings.TEMPLATE_DIR);
        if (!workDir.exists()) {
            workDir.mkdir();
        }

        workDir = new File(Settings.TEMPORARY_DIR);
        if (!workDir.exists()) {
            workDir.mkdir();
        }
    }

    /**
     * Метод возвращает каталог для временных файлов программы
     *
     * @return путь к каталогу временных файлов
     */
    public static String getTemporaryDir() {
        prepareDirs();
        return Settings.TEMPORARY_DIR;
    }

    /**
     * Метод возвращает каталог для хранения шаблоногв документов
     *
     * @return путь к каталогу шаблонов
     */
    public static String getTemplateDir() {
        prepareDirs();
        return Settings.TEMPLATE_DIR;
    }


    public static void purgeDirectory(File dir) {
        for (File file : dir.listFiles()) {
            if (file.isDirectory()) {
                purgeDirectory(file);
            }
            file.delete();
        }
    }
}
