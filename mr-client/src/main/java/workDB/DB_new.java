package workDB;

import by.gomel.freedev.ucframework.ucdao.jdbc.AbstractMSSQLServerJDBC;
import by.march8.ecs.framework.common.LogCrutch;
import dept.MyReportsModule;

import javax.swing.*;
import java.io.File;
import java.io.FileInputStream;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.logging.Logger;

/**
 *
 * @author vova
 */
public class DB_new extends AbstractMSSQLServerJDBC {

    //private static final Logger log = new Log().getLoger(DB.class);
    private static final LogCrutch log = new LogCrutch();

    public Connection conn;
    public Statement stmt;
    public PreparedStatement ps;
    public CallableStatement cs;
    public ResultSet rs;
    String dbUrl;
    String dbUserName;
    String dbUserPass;
    Properties prop = new Properties();
    private boolean fConn;

    public DB_new() {
        conn = null;
        stmt = null;
        rs = null;
        ps = null;
        cs = null;
        fConn = false;

        File configfile = new File(MyReportsModule.confPath + "Conf.properties");
        try {
//		 Load props from the file
            prop.load(new FileInputStream(configfile));
            dbUrl = new String(prop.getProperty("db.conn.url"));
            dbUserName = new String(prop.getProperty("db.conn.user"));
            dbUserPass = new String(prop.getProperty("db.conn.password"));
        } catch (Exception e) {
            log.error("Ошибка чтения настроек из Conf.properties: ", e);
            JOptionPane.showMessageDialog(null, "Ошибка чтения настроек из Conf.properties: " + e.toString(), "Ошибка!!!", JOptionPane.ERROR_MESSAGE);
        }
        conn();
    }

    /**
     * Method connect to SQL Server
     */
    public void conn() {
        if (!fConn) {
            try {
                Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
                conn = DriverManager.getConnection("jdbc:sqlserver://" + dbUrl, dbUserName, dbUserPass);
                stmt = conn.createStatement();
            } catch (Exception e) {
                System.err.println("Cannot connect to database server " + e.toString());
                log.error("Ошибка подключения к серверу баз данных", e);
                JOptionPane.showMessageDialog(null, "Ошибка подключения к серверу баз данных", "Ошибка!!!", JOptionPane.ERROR_MESSAGE);
            }
            fConn = true;
        }
    }

    /**
     * Method disconnect from SQL Server
     */
    public void disConn() {
        try {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
            if (ps != null) ps.close();
            if (cs != null) cs.close();
            if (conn != null) conn.close();
        } catch (Exception e) {
            System.err.println("Cannot connect to database server");
            JOptionPane.showMessageDialog(null, "Ошибка отключения от сервера баз данных: ", "Ошибка!!!", JOptionPane.ERROR_MESSAGE);
            log.error("Ошибка при дисконекте от сервера бд", e);
            e.printStackTrace();
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (Exception e) {
                    System.out.println("Error close statment" + e.getMessage());
                }
            }
            if (ps != null) {
                try {
                    ps.close();
                } catch (Exception e) {
                    System.out.println("Error close preparedstatment" + e.getMessage());
                }
            }
            if (cs != null) {
                try {
                    cs.close();
                } catch (Exception e) {
                    System.out.println("Error close preparedstatment" + e.getMessage());
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (Exception e) {
                    System.out.println("Error close connection" + e.getMessage());
                }
            }
        }
    }

    public void setAutoCommit(boolean f) {
        try {
            conn.setAutoCommit(f);
        } catch (SQLException ex) {
            log.error("Ошибка  функции setAutoCommit ", ex);
            System.err.println("Ошибка  функции setAutoCommit " + ex.getMessage());
        }
    }

    public void commit() {
        try {
            conn.commit();
        } catch (SQLException ex) {
            log.error("Ошибка  функции сommit ", ex);
            System.err.println("Ошибка  функции сommit " + ex.getMessage());
        }
    }

    public void rollback() {
        try {
            conn.rollback();
        } catch (SQLException ex) {
            log.error("Ошибка  функции rollback() ", ex);
            System.err.println("Ошибка  rollback() " + ex.getMessage());
        }
    }
}