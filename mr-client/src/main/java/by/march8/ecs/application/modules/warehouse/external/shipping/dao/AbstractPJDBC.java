package by.march8.ecs.application.modules.warehouse.external.shipping.dao;

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
import java.sql.Statement;
import java.util.Date;
import java.util.HashMap;
import java.util.Properties;
import java.util.Set;

public class AbstractPJDBC {
    // private static final Logger log = new Log().getLoger(PDB_new.class);
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

    public AbstractPJDBC() {
        conn = null;
        stmt = null;
        rs = null;
        ps = null;
        cs = null;
        fConn = false;

        File configfile = new File(MyReportsModule.confPath + "Conf.properties");
        try {
            prop.load(new FileInputStream(configfile));
            dbUrl = prop.getProperty("dbpostgres.conn.url");
            dbUserName = prop.getProperty("dbpostgres.conn.user");
            dbUserPass = prop.getProperty("dbpostgres.conn.password");
        } catch (Exception e) {
            log.error("Ошибка чтения настроек из Conf.properties для ПостГрес: ", e);
            System.out.println(e.getMessage());
            JOptionPane.showMessageDialog(null, "Ошибка чтения настроек из Conf.properties для ПостГрес: " + e.toString(), "Ошибка!!!", JOptionPane.ERROR_MESSAGE);
            return;
        }
        conn();
    }

    /**
     * Method connect to SQL Server
     */
    public void conn() {
        if (!fConn) {
            try {
                Class.forName("org.postgresql.Driver");
                conn = DriverManager.getConnection("jdbc:postgresql://" + dbUrl + "/march", dbUserName, dbUserPass);
                // conn = DriverManager.getConnection("jdbc:postgresql://"+dbUrl+"/march:5432?user="+dbUserName+"&password="+dbUserPass);
                //datamatrix?user=fred&password=secret&ssl=true
                stmt = conn.createStatement();
            } catch (Exception e) {
                System.err.println("Cannot connect to database server " + e.toString());
                log.error("Ошибка подключения к серверу баз данных ПостГрес", e);
                JOptionPane.showMessageDialog(null, "Ошибка подключения к серверу баз данных ПостГрес", "Ошибка!!!", JOptionPane.ERROR_MESSAGE);
                return;
            }
            fConn = true;
        }
    }
    //jdbc:postgresql://localhost/datamatrix?user=fred&password=secret&ssl=true

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
            System.err.println("Cannot disconnect from database server");
            JOptionPane.showMessageDialog(null, "Ошибка отключения от сервера баз данных ПостГрес", "Ошибка!!!", JOptionPane.ERROR_MESSAGE);
            log.error("Ошибка при дисконекте от сервера бд ПостГрес", e);
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
        } catch (Exception ex) {
            log.error("Ошибка  функции setAutoCommit ", ex);
            System.err.println("Ошибка  функции setAutoCommit " + ex.getMessage());
        }
    }

    public void commit() {
        try {
            conn.commit();
        } catch (Exception ex) {
            log.error("Ошибка  функции сommit ", ex);
            System.err.println("Ошибка  функции сommit " + ex.getMessage());
        }
    }

    public void rollBack() {
        try {
            conn.rollback();
        } catch (Exception ex) {
            log.error("Ошибка  функции rollback() ", ex);
            System.err.println("Ошибка  rollback() " + ex.getMessage());
        }
    }

    public HashMap<Date, Double> getCurrencyRateList(int currencyId, Set<java.util.Date> dateSet) {

        // 15 USD
        HashMap<Date, Double> items = new HashMap<>();
        try {

            String d = dateSet.toString().replace("[", "'").replace("]", "'").replace(", ", "','");


            String query = "select date_start, kurs from valuta_kurs WHERE  id_valuta2 = ? and  CAST(date_start AS VARCHAR) in (" + d + ")";
            System.out.println(query);

            ps = conn.prepareStatement(query);
            ps.setInt(1, currencyId);

            //Array array = conn.createArrayOf("date", new Set[]{dateSet});
            //ps.setArray(2, array);

            rs = ps.executeQuery();
            while (rs.next()) {
                items.put(rs.getDate("date_start"), rs.getDouble("kurs"));
                System.out.println(rs.getDate("date_start") + " - " + rs.getDouble("kurs"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return items;
    }
}
