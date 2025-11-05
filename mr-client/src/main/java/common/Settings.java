package common;

/**Класс содержащий основные настройки программ и дефолтные параметры подключения к БД*/
@SuppressWarnings("unused")
public class Settings {
    /**Рабочая директория приложения */
    public static final String WORK_DIR = System.getProperty("user.home") + "/.MyReports/";
    /** Имя файла конфигурации программы*/
    public static final String CONFIG_FILE_NAME = "configxml.properties";
    /** Директория логов*/
    public static final String LOG_DIR = WORK_DIR + "/log";
    /** Директория отчетов*/
    public static final String REPORT_DIR = WORK_DIR + "/Report";
    /**Директория для выгрузки данных*/
    public static final String OUTDATA_DIR = WORK_DIR + "/log";
    /**Директория настроек пользователя*/
    public static final String USER_PROP = WORK_DIR + "/User properties";


    public final static String JDBC_MSSQL_DRIVER = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
    public final static String JDBC_MSSQL_PREFIX = "jdbc:sqlserver://";
    public final static String JDBC_MSSQL_URL = "10.10.10.101";
    public final static String JDBC_MSSQL_PORT = "";

    public final static String JDBC_POSTGRESQL_DRIVER = "org.postgresql.Driver";
    public final static String JDBC_POSTGRESQL_PREFIX = "jdbc:postgresql://";
    public final static String JDBC_POSTGRESQL_URL = "localhost";
    public final static String JDBC_POSTGRESQL_PORT = "5432";

    public final static String POSTGRESQL_USERNAME = "postgres";
    public final static String POSTGRESQL_USERPASS = "postgres";

    public final static String MSSQL_USERNAME = "sa";
    public final static String MSSQL_USERPASS = "123";

    public Settings() {
        //marchMSDBDefault =

    }
}
