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
public abstract class AbstractPostgreSQLServerJDBC implements BaseDB {

    private static final Logger log = Logger.getLogger(AbstractPostgreSQLServerJDBC.class.getName());

    private static String dbUrl;
    private static String dbUserName;
    private static String dbUserPass;
    private static Properties PROP = new Properties();

    static {
        try {
            PROP.load(new FileInputStream(MyReportsModule.confPath + "Conf.properties"));
            dbUrl = PROP.getProperty("dbpostgres.conn.url");
            dbUserName = PROP.getProperty("dbpostgres.conn.user");
            dbUserPass = PROP.getProperty("dbpostgres.conn.password");
        } catch (IOException e) {
            log.severe(e.getMessage());
        }
    }

    /**
     * Method connect to SQL Server
     */
    public Connection getConnection() throws SQLException, ClassNotFoundException {

        Class.forName("org.postgresql.Driver");
        return DriverManager.getConnection("jdbc:postgresql://" + dbUrl + "/march:5432", dbUserName, dbUserPass);
    }
}
