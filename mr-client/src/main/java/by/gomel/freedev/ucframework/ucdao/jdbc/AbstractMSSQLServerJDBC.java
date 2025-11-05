package by.gomel.freedev.ucframework.ucdao.jdbc;

import dept.MyReportsModule;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Logger;

/**
 * @author Andy 12.07.2016.
 */
public abstract class AbstractMSSQLServerJDBC implements BaseDB {

    private static final Logger log = Logger.getLogger(AbstractMSSQLServerJDBC.class.getName());

    private static String dbUrl;
    private static String dbUserName;
    private static String dbUserPass;
    private static Properties PROP = new Properties();

    static {
        try {
            PROP.load(new FileInputStream(MyReportsModule.confPath + "Conf.properties"));
            dbUrl = PROP.getProperty("db.conn.url");
            dbUserName = PROP.getProperty("db.conn.user");
            dbUserPass = PROP.getProperty("db.conn.password");
        } catch (IOException e) {
            log.severe(e.getMessage());
        }
    }

    /**
     * Method connect to SQL Server
     */
    public Connection getConnection() throws SQLException, ClassNotFoundException {
        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        Properties props = new Properties();
        props.put("user", dbUserName);
        props.put("password", dbUserPass);
        props.put("Integrated Security", "False");
        props.put("Connect Timeout", "15");
        props.put("Encrypt", "False");
        props.put("TrustServerCertificate", "False");
        return DriverManager.getConnection("jdbc:sqlserver://" + dbUrl, props);
    }
}
