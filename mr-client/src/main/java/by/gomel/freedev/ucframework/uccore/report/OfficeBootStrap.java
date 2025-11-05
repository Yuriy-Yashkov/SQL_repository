package by.gomel.freedev.ucframework.uccore.report;

import by.gomel.freedev.ucframework.uccore.utils.SystemUtils;
import by.march8.ecs.MainController;
import by.march8.ecs.framework.common.Settings;

import java.io.File;
import java.net.URLDecoder;

/**
 * Загрузчик Open Office. Чисто формальный класс, определяет расположение исполнимого файла
 * soffice.exe в системе. Синглтон
 * <p>Если в конфигурационном файле отсутствует параметр <code>openoffice.path</code> или он равен пустому
 * значению загрузчик возвращает путь по старой схеме (ничего не менялось, со времен старой версии MyReports)</p>
 *
 * @author Andy 04.04.2016.
 */
public class OfficeBootStrap {

    public static OfficeBootStrap instance = new OfficeBootStrap();

    public static OfficeBootStrap getInstance() {
        return OfficeBootStrap.instance;
    }

    /**
     * Метод возвращает путь к исполнимому файлу soffice.exe по требованию пользователя
     * <p>
     * Для совместимости с рабочими станциями, на которых установлены различные версии
     * Libre Office (с ОС Windows), оставлены всевозможные варианты поиска каталогов.
     *
     * @return путь к исполнимому файлу soffice.exe
     */
    public String getBootPath() {
        String pathOfficeConfig = MainController.getConfiguration().getProperty(Settings.PROPERTY_OPEN_OFFICE);
        String oooExeFolder = "";
        if (pathOfficeConfig != null) {
            if (!pathOfficeConfig.trim().equals("")) {
                System.out.println("Параметры загрузчика OpenOffice найдены в конфигурации... ");
                try {
                    String decodedPath = URLDecoder.decode(pathOfficeConfig, "UTF-8");
                    File executeFile = new File(decodedPath);

                    oooExeFolder = executeFile.getPath();
                    if (oooExeFolder.contains("\\")) {
                        oooExeFolder = oooExeFolder.replace("\\", "/");
                    }
                } catch (Exception e) {
                    MainController.exception(e, "Ошибка преобразования пути к исполнимому файлу soffice");
                }
            } else {
                oooExeFolder = getNativeOpenOfficePath();
            }
        } else {
            oooExeFolder = getNativeOpenOfficePath();
        }
        if (oooExeFolder.equals("")) {
            System.out.println("Запуск soffice не выполнен, файл soffice не найден..." + oooExeFolder);
        } else {
            System.out.println("Запускаем soffice..." + oooExeFolder);
        }
        return oooExeFolder;
    }

    /**
     * Возвращает путь к исполнимому фалй soffice "дедовским" способом
     *
     * @return путь к исполнимому файлу soffice
     */
    private String getNativeOpenOfficePath() {
        System.out.println("Параметры загрузчика OpenOffice не найдены в конфигурации... ");

        String result = "";
        File file;

        if (!SystemUtils.isWindows()) {
            result = "/usr/bin/soffice";
        } else {
            file = new java.io.File("c:\\Program Files\\LibreOffice 3.5\\program\\soffice.exe");
            if (file.exists())
                result = "c://Program Files//LibreOffice 3.5//program//soffice.exe";
            else {
                file = new java.io.File("c:\\Program Files\\OpenOffice.org 3\\program\\soffice.exe");
                if (file.exists()) {
                    result = "c://Program Files//OpenOffice.org 3//program//soffice.exe";
                } else {
                    file = new java.io.File("c:\\Program Files (x86)\\LibreOffice 3.6\\program\\soffice.exe");
                    if (file.exists()) {
                        result = "c://Program Files (x86)//LibreOffice 3.6//program//soffice.exe";
                    }
                }
            }
        }
        return result;
    }
}
