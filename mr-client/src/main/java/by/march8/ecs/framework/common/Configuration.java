package by.march8.ecs.framework.common;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Класс работы с конфигурационным файлом настрорек программы.
 *
 * @author andy-linux
 */
public class Configuration {
    // *************************************************************
    // Константные ключи параметров конфигурации
    // *************************************************************
    /**
     * The Constant KEY_JDBC_CLASSCONNECTOR.
     */
    public final static String KEY_JDBC_CLASSCONNECTOR = "jdbc.classconnector";

    /**
     * The Constant KEY_JDBC_DRIVERCONNECTOR.
     */
    public final static String KEY_JDBC_DRIVERCONNECTOR = "jdbc.driverconnector";

    /**
     * The Constant KEY_JDBC_SERVERURL.
     */
    public final static String KEY_JDBC_SERVERURL = "server.url";

    /**
     * The Constant KEY_JDBC_SERVERPORT.
     */
    public final static String KEY_JDBC_SERVERPORT = "server.port";

    /**
     * The Constant KEY_JDBC_DATABASENAME.
     */
    public final static String KEY_JDBC_DATABASENAME = "database.name";

    /**
     * The Constant KEY_JDBC_USERNAME.
     */
    public final static String KEY_JDBC_USERNAME = "database.username";

    /**
     * The Constant KEY_JDBC_USERPASS.
     */
    public final static String KEY_JDBC_USERPASS = "database.userpass";

    /**
     * The Constant KEY_LOGIN_USERNAME.
     */
    public final static String KEY_LOGIN_USERNAME = "login.username";
    public final static String RUN_PATH = "run.path";

    public static final String TEMPLATE_JASPER = "template.jasper";
    public static final String TEMPLATE_MICROSOFT = "template.microsoft";
    public static final String TEMPLATE_DBF = "template.dbf";


    /**
     * The properties.
     */
    private Properties properties;

    /**
     * The file name.
     */
    private String fileName = "";

    /**
     * Instantiates a new configuration.
     *
     * @param configFile the config file
     */
    public Configuration(String configFile) {
        prepareDirs();
        properties = new Properties();
        fileName = configFile;

        File config = new File(fileName);
        if (config.exists()) {
            loadConfiguration();
        } else {
            defaultConfiguration();
            saveConfiguration();
            loadConfiguration();
        }
    }

    /**
     * Prepare dirs.
     */
    private void prepareDirs() {
        File f = new File(Settings.HOME_DIR);
        if (!f.exists()) {
            f.mkdir();
        }
    }

    /**
     * Load configuration.
     */
    private void loadConfiguration() {
        try {
            properties.load(new FileInputStream(fileName));
        } catch (IOException e) {
            System.out.println("Ошибка загрузки файла конфигурации :"
                    + fileName + " :" + e.toString());
        }
    }

    /**
     * Возвращает параметр согласно ключа <code>key</code> из файла
     * конфигурации.
     * <p/>
     * Если по ключу настройки не найдено возвращает null
     *
     * @param key the key
     * @return the property
     */
    public String getProperty(String key) {
        loadConfiguration();
        String result = properties.getProperty(key);
        if (result == null) {
            setProperty(key, "");
            result = "";
        }
        return result;
    }

    public String getProperty(String key, String defaultValue) {
        loadConfiguration();
        return properties.getProperty(key, defaultValue);
    }

    public void setProperty(String key, String value) {
        properties.setProperty(key, value);
        System.out.println("Сохранение конфигурации для [" + key + "] с значением [" + value + "]");
        saveConfiguration();
    }

    /**
     * Метод загружает из конфигурационного файла данные о подключении.
     *
     * @param connectKey константная строка из {@link DatabaseConnnectInfo }
     * @return the connect info
     */

    /**
     * Save configuration.
     */
    private void saveConfiguration() {
        try {
            properties.store(new FileOutputStream(fileName), null);
        } catch (Exception e) {
            System.out.println("Ошибка сохранения файла конфигурации :"
                    + fileName + " :" + e.toString());
        }
    }

    /**
     * Default configuration.
     */
    private void defaultConfiguration() {
        properties.setProperty("programm.path", "/nfs/Programs/MyReports20");
        properties.setProperty("programm.login", "");
        properties.setProperty("db.conn.url", "mssql-server");
        properties.setProperty("db.conn.user", "March8");
        properties.setProperty("db.conn.password", "");
        properties.setProperty("dbpostgres.conn.url", "postgres-server");
        properties.setProperty("dbpostgres.conn.user", "client");
        properties.setProperty("dbpostgres.conn.password", "march8");
        properties.setProperty("dbf.plan", "/nfs/ser01_D/PLAN/");
        properties.setProperty("dbf.DVI", "/nfs/ser01_D/DVI/");
        saveConfiguration();
    }

    /**
     * Метод возвращает из конфигурационного файла имя пользователя,
     * выполнявшего вход в программу.
     *
     * @return имя пользователя
     */
    public String getLastLogInUser() {
        return properties.getProperty(KEY_LOGIN_USERNAME, "");
    }

    /**
     * Метод сохраняет в конфигурационом файле имя пользователя.
     *
     * @param userName имя пользователя
     */
    public void setLastLoginUser(String userName) {
        properties.setProperty(KEY_LOGIN_USERNAME, userName);
        saveConfiguration();
    }

    public Properties getProperties() {
        return properties;
    }

    public void setProperties(final Properties properties) {
        this.properties = properties;
    }
}
