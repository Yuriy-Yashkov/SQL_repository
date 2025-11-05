/**
 * пакет
 */
package by.march8.ecs.framework.common;


import by.Version;
import by.gomel.freedev.ucframework.uccore.utils.SystemUtils;

import java.awt.*;

/**
 * Класс констант и параметров приложения.
 *
 * @author Andy
 * @since 1.0.1
 */
public class Settings {

    /**
     * Название программы без идентификатора версии.
     */
    public static final String PROGRAM_NAME_SHORT = "MyReports";

    /**
     * Верси программы.
     */
    public static final String PROGRAM_VERSION = "2.3.1";

    /**
     * Полное имя программы.
     */
    public static final String PROGRAM_NAME = PROGRAM_NAME_SHORT + " v" + PROGRAM_VERSION + " build " + Version.VERSION;
    /**
     * Рабочая директория приложения.
     */
    public static final String HOME_DIR = System.getProperty("user.home") + "/.MyReports/";

    /**Директория для сохранения профилей*/
    public static final String PROFILES_DIR = HOME_DIR + "/UserProperties/";

    /**Директория для сохранения шаблонов*/
    public static final String TEMPLATE_DIR = HOME_DIR + "/templates/";

    /**Директория для сохранения временных файлов*/
    public static final String TEMPORARY_DIR = HOME_DIR + "/temp/";


    /**
     * Имя файла конфигурации программы.
     */
    public static final String CONFIG_FILE_NAME = HOME_DIR + "Conf.properties";

    /**  Директория логов. */
    //public static final String LOG_DIR = HOME_DIR+"/log";
    /**  Директория отчетов. */
    public static final String REPORT_DIR = HOME_DIR + "/report";
    public static final String ISSUANCE_DIR = HOME_DIR + "/issuance/";
    public static final String FACTORING_DIR = HOME_DIR + "/factoring/";
    public static final Dimension BUTTON_HALF_BIG_SIZE_PLUS = new Dimension(270, 26);
    /**
     * Размеры кнопок ОБЫЧНОЙ ШИРИНЫ.
     */
    public final static Dimension BUTTON_NORMAL_SIZE = new Dimension(120, 26);

    /** Директория для выгрузки данных. */
    //public static final String OUTDATA_DIR =HOME_DIR+"/export";
    /** Директория настроек пользователя. */
    //public static final String USER_PROP = HOME_DIR+"/settings";
    /** Размеры кнопок ДВОЙНОЙ ШИРИНЫ. */
    public final static Dimension BUTTON_BIG_SIZE = new Dimension(250, 26);
    /**Размеры кнопок ПОЛУТОРНОЙ ШИРИНЫ*/
    public final static Dimension BUTTON_HALF_BIG_SIZE = new Dimension(170, 26);
    /**Боковой отступ панели контента от края формы */
    public final static Dimension SIDE_BORDER = new Dimension(5, 5);
    public static final Color COLOR_WRONG_VALUE = new Color(239, 179, 171);
    public static final Color COLOR_RECALCULATE = new Color(207, 239, 212);
    public static final Color COLOR_DISABLED = new Color(255, 200, 200);
    public static final Color COLOR_ENABLED = new Color(222, 255, 226);
    /**
     * Параметр в конфиге для OPenOffice исполнимого файла
     */
    public static final String PROPERTY_OPEN_OFFICE = "openoffice.path";
    public static final String PROPERTY_OFFICE_BOOTSTRAP = "openoffice.bootstrap";
    public static final String PROPERTY_CATALOG_PATH = "catalog.path";
    public static final String WINDOWS_SOURCE_PATH = "//file-server4/Programs/MyReports20/";
    public static final String UNIX_SOURCE_PATH = "//nfs/Programs/MyReports20/";
    public static String LAST_DIALOG_DIR = HOME_DIR;
    public static String UNIX_CATALOG_PATH = "//nfs/Programs/catalog/resize/";
    public static String WINDOWS_CATALOG_PATH = "\\\\file-server4\\Programs\\catalog\\resize\\";

    public static String UNIX_EXCHANGE_PATH = "//nfs/Programs/MyReports/dbf/";
    public static String WINDOWS_EXCHANGE_PATH = "\\\\file-server\\dbf\\";


    public static String CATALOG_TREE_FILE = "tree.list";

    public static boolean isNoGUI = false;

    public static String getExchangeCatalog() {
        if (SystemUtils.isWindows()) {
            return WINDOWS_EXCHANGE_PATH;
        } else {
            return UNIX_EXCHANGE_PATH;
        }
    }
}
