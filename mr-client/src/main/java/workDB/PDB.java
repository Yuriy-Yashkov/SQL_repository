package workDB;

import by.gomel.freedev.ucframework.ucdao.jdbc.AbstractPostgreSQLServerJDBC;
import by.march8.ecs.framework.common.LogCrutch;
import by.march8.entities.unknowns.UserCompatibility;
import dept.MyReportsModule;
import dept.marketing.Image_;
import dept.tools.imgmanager.ItemModel;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Vector;

//import org.apache.log4j.Logger;

public class PDB extends AbstractPostgreSQLServerJDBC {

    //private static final Logger log = new Log().getLoger(PDB.class);
    private static final LogCrutch log = new LogCrutch();
    Connection conn;
    Statement stmt;
    PreparedStatement ps;
    CallableStatement cs;
    ResultSet rs;
    String dbUrl;
    String dbUserName;
    String dbUserPass;
    Properties prop = new Properties();
    private boolean fConn;

    public PDB() {
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
                conn = DriverManager.getConnection("jdbc:postgresql://" + dbUrl + "/march:5432", dbUserName, dbUserPass);
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

    private void setAutoCommit(boolean f) {
        try {
            conn.setAutoCommit(f);
        } catch (Exception ex) {
            log.error("Ошибка  функции setAutoCommit ", ex);
            System.err.println("Ошибка  функции setAutoCommit " + ex.getMessage());
        }
    }

    private void commit() {
        try {
            conn.commit();
        } catch (Exception ex) {
            log.error("Ошибка  функции сommit ", ex);
            System.err.println("Ошибка  функции сommit " + ex.getMessage());
        }
    }

    private void rollBack() {
        try {
            conn.rollback();
        } catch (Exception ex) {
            log.error("Ошибка  функции rollback() ", ex);
            System.err.println("Ошибка  rollback() " + ex.getMessage());
        }
    }

    /**
     * Преобразует строку в формате "дд.мм.гггг" в миллисекунды тип long
     * @return long количество миллисекунд
     */
    public long convertDateStrToLong(String date) {
        java.util.Date d = new java.util.Date();
        DateFormat formatter = new java.text.SimpleDateFormat("dd.MM.yyyy");

        try {
            d = formatter.parse(date);
        } catch (Exception e) {
            System.out.println("Error in convertDateStrToLong function: " + e);
        }
        return d.getTime();
    }

/* *****************************************************************************
-----------------------Главная форма--------------------------------------------
******************************************************************************/

    public String enter(String login, String password) {
        String query = "select id from users where login = ? and password = ?";
        try {
            ps = conn.prepareStatement(query);
            ps.setString(1, login);
            ps.setString(2, password);
            rs = ps.executeQuery();

            if (rs.next()) {
                return getMenuRights(rs.getInt(1), 1);
            } else return null;
        } catch (Exception e) {
            log.error("Ошибка при входе в систему: " + e);
            System.err.print("Ошибка при входе в систему: " + e.getMessage());
        }
        return null;
    }

    public String getMenuRights(int id_user, int id_form) {
        String query = "select rights from menu_right where id_user = ? and id_form = ?";
        try {
            ps = conn.prepareStatement(query);
            ps.setInt(1, id_user);
            ps.setInt(2, id_form);
            rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getString(1);
            } else return null;
        } catch (Exception e) {
            log.error("Ошибка при получении прав на меню: " + e);
            System.err.print("[PDB.getMenuRights(int id_user, int id_form)]Ошибка при получении прав на меню: " + e.getMessage());
        }
        return null;
    }

    /**
     * Возвращает права пользователя на форму
     * @param login
     * @param password
     * @param form полное название формы, включая названия пакетов
     * @return права пользователя
     * @throws Exception
     */
    public String getMenuRights(String login, String password, String form) throws Exception {
        String right = null;
        String sql = "Select rights "
                + "     From menu_right, forms, users "
                + "     Where forms.id = menu_right.id_form and "
                + "     	users.id = menu_right.id_user and "
                + "     	name_form = ? and "
                + "             login = ? and "
                + "             password = ?  ";
        try {
            ps = conn.prepareStatement(sql);
            ps.setString(1, form);
            ps.setString(2, login);
            ps.setString(3, password);
            rs = ps.executeQuery();

            if (rs.next()) {
                right = rs.getString("rights");
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new Exception("Не удалось прочитать права пользователя! ", e);
        }
        return right;
    }

    /* *****************************************************************************
    ---------------------------------Логирование----------------------------------
    ******************************************************************************/
    public Vector getUserLogin(String user, String sDate, String eDate) {
        Vector items = new Vector();
        long sd;
        long ed;
        int id = -1;
        sd = convertDateStrToLong(sDate);
        ed = convertDateStrToLong(eDate);
        String query;

        try {
            setAutoCommit(false);
            query = "select id from users where login = ?";
            ps = conn.prepareStatement(query);
            ps.setString(1, user);
            rs = ps.executeQuery();
            if (rs.next()) {
                id = rs.getInt(1);
            }
            if (user.equals("Все...")) {
                query = "select users.login, ip, date, loglogin.login from loglogin, users where userid = users.id and date >= ? and date < ? order by users.login, ip, date, loglogin.login";
                ps = conn.prepareStatement(query);
            } else {
                query = "select users.login, ip, date, loglogin.login from loglogin, users where  userid = users.id and date >= ? and date < ? and userid = ? order by users.login, ip, date, loglogin.login";
                ps = conn.prepareStatement(query);
                ps.setInt(3, id);
            }
            ps.setDate(1, new java.sql.Date(sd));
            ps.setDate(2, new java.sql.Date(ed + DAY));
            rs = ps.executeQuery();
            while (rs.next()) {
                Vector v = new Vector();
                v.add(rs.getString(2));
                v.add(rs.getString(1));
                v.add(rs.getString(3));
                if (rs.getBoolean(4)) {
                    v.add("Вход");
                } else v.add("Выход");
                items.add(v);
            }
            commit();
        } catch (Exception e) {
            rollBack();
            log.error("ошибка при добавлении записи в loglogin " + e);
            System.err.println("ошибка при добавлении записи в loglogin " + e.getMessage());

        } finally {
            setAutoCommit(true);
        }
        return items;
    }

    public void delUserLogin(String user, String sDate, String eDate) {
        long sd;
        long ed;
        int id = -1;
        sd = convertDateStrToLong(sDate);
        ed = convertDateStrToLong(eDate);
        String query;

        try {
            query = "select id from users where login = ?";
            ps = conn.prepareStatement(query);
            ps.setString(1, user);
            rs = ps.executeQuery();
            if (rs.next()) {
                id = rs.getInt(1);
            }
            if (user.equals("Все...")) {
                query = "delete from loglogin where date >= ? and date < ?";
                ps = conn.prepareStatement(query);
            } else {
                query = "delete from loglogin where date >= ? and date < ? and userid = ?";
                ps = conn.prepareStatement(query);
                ps.setInt(3, id);
            }
            ps.setDate(1, new java.sql.Date(sd));
            ps.setDate(2, new java.sql.Date(ed + DAY));
            ps.executeUpdate();
        } catch (Exception e) {
            log.error("ошибка delUserLogin " + e);
            System.err.println("ошибка delUserLogin " + e.getMessage());
        }
        JOptionPane.showMessageDialog(null, "Данные пользователя удалены", "", javax.swing.JOptionPane.PLAIN_MESSAGE);
    }

    public void addLogLogin(String login, String pas, String ip, boolean b) {
        int userID = -1;
        String query = "SELECT id FROM users where login = ? and password = ?";
        try {
            ps = conn.prepareStatement(query);
            ps.setString(1, login);
            ps.setString(2, pas);
            rs = ps.executeQuery();
            while (rs.next()) {
                userID = rs.getInt(1);
            }
            if (userID != -1) {
                query = "insert into loglogin (userId, login, ip) values(?, ?, ?)";
                ps = conn.prepareStatement(query);
                ps.setInt(1, userID);
                ps.setBoolean(2, b);
                ps.setString(3, ip);
                ps.executeUpdate();
            }
        } catch (Exception e) {
            log.error("ошибка getUserLogin " + e);
            System.err.println("ошибка getUserLogin " + e.getMessage());
        }
    }

    /* *****************************************************************************
    -----------------------Форма администрирования----------------------------------
    ******************************************************************************/
    public boolean addUser(String login, String password, String about) throws Exception {
        String query = "select id from users where login = ?";
        try {
            ps = conn.prepareStatement(query);
            ps.setString(1, login);
            rs = ps.executeQuery();
            if (rs.next()) {
                //JOptionPane.showMessageDialog(null, "Пользователь с таким логином уже существует", "Ошибка!!!", JOptionPane.ERROR_MESSAGE);
                return false;
            }

        } catch (Exception e) {
            log.error("Ошибка добавлении нового пользователя: " + e);
            System.err.print("Ошибка добавлении нового пользователя: " + e.getMessage());
            throw e;
        }

        query = "insert into users (login, password, about) values(?, ?, ?)";
        try {
            ps = conn.prepareStatement(query);
            ps.setString(1, login);
            ps.setString(2, password);
            ps.setString(3, about);
            ps.executeUpdate();

        } catch (Exception e) {
            log.error("Ошибка добавлении нового пользователя: " + e);
            System.err.print("Ошибка добавлении нового пользователя: " + e.getMessage());
            throw e;
        }
        return true;
    }


    /**
     * Получениея списка имён всех пользователей
     * @return ArrayList с элементами типа String
     * @throws java.lang.Exception
     */
    public ArrayList getUsers() throws Exception {
        ArrayList<String> users = new ArrayList<>();
        String query = "select login from users order by login asc";

        try {
            ps = conn.prepareStatement(query);
            rs = ps.executeQuery();
            while (rs.next()) {
                users.add(rs.getString(1));
            }
        } catch (Exception e) {
            log.error("Ошибка формирования списка пользователей: " + e);
            e.printStackTrace();
            throw e;
        }
        return users;
    }

    public String getUserInfo(String user, String param) throws Exception {
        String query = null;

        if (param.equals("password")) {
            query = "select password from users where login = ?";
        } else if (param.equals("rights")) {
            query = "select rights from users where login = ?";
        } else if (param.equals("about")) {
            query = "select about from users where login = ?";
        }
        try {
            ps = conn.prepareStatement(query);
            ps.setString(1, user);
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString(1);
            }
        } catch (Exception e) {
            log.error("Ошибка формирования списка пользователей: " + e);
            throw e;
        }
        return "";
    }

    public String setUserInfo(String user, String param) throws Exception {
        String query = "update users set rights = ? where login = ?";

        try {
            ps = conn.prepareStatement(query);
            ps.setString(1, param);
            ps.setString(2, user);
            ps.executeUpdate();
        } catch (Exception e) {
            log.error("Ошибка при назначении прав пользователю " + user + " : " + e);
            throw e;
        }
        return "";
    }

    public void changeUser(String user, String login, String password, String about) throws Exception {
        String query = "update users set login = ?, password = ?, about = ? where login = ?";

        try {
            ps = conn.prepareStatement(query);
            ps.setString(1, login);
            ps.setString(2, password);
            ps.setString(3, about);
            ps.setString(4, user);
            ps.executeUpdate();
        } catch (Exception e) {
            log.error("Ошибка при изменении данных пользователя: " + e);
            throw e;
        }
    }

    public void delUser(String user) throws Exception {
        String query = "delete from users where login = ?";

        try {
            ps = conn.prepareStatement(query);
            ps.setString(1, user);
            ps.executeUpdate();
        } catch (Exception e) {
            log.error("Ошибка при удалении пользователя: " + e);
            throw e;
        }
    }

/* ****************************************************************************
-----------------------Сбыт отправка накладных---------------------------------
******************************************************************************/

    /**
     * Создаёт список электронных адресов клиента по его idDept
     * @rezalt idDept ID клиента
     * @return Список эхлектронных адресов клиента
     */
    public String[] getEmailList(int id) {
        String[] emails = new String[300];
        emails[0] = "Адрес отсутствует";
        String query = "select email from client where id_client = ? order by email";
        int i = 0;

        try {
            ps = conn.prepareStatement(query);
            ps.setLong(1, id);
            rs = ps.executeQuery();
            while (rs.next()) {
                emails[i++] = rs.getString(1);
            }
        } catch (Exception e) {
            log.error("Ошибка создания листа электронных адресов ", e);
            System.err.println("Ошибка создания листа электронных адресов " + e.getMessage());
        }
        emails[i] = "Добавить...";
        return emails;
    }

    /**
     * Заменяет электронный адрес клиента на новый или просто добовляет его если старый не задан
     * @rezalt idDept идентификатор клиента
     * @rezalt oldMail старое значение адреса
     * @rezalt newMail новый адрес
     */
    public boolean setEmail(long id, String oldMail, String newMail) {
        String query = null;

        try {
            if (oldMail.equals("")) {
                query = "insert into client (id_client, email) values(?, ?)";
                ps = conn.prepareStatement(query);
                ps.setLong(1, id);
                ps.setString(2, newMail);
            } else {
                query = "update client set email = ? where id_client = ? and email = ?";
                ps = conn.prepareStatement(query);
                ps.setString(1, newMail);
                ps.setLong(2, id);
                ps.setString(3, oldMail);
            }
            ps.executeUpdate();
            return true;
        } catch (Exception e) {
            log.error("Ошибка изменения электронного адреса ", e);
            System.err.println("Ошибка изменения электронного адреса " + e.getMessage());
            return false;
        }
    }

    /**
     * Удаляет электронный адрес клиента
     * @param id идентификатор клиента
     * @param mail адрес который надо удалить
     * @return true || false
     */
    public boolean delEmail(long id, String mail) {
        String query;

        try {
            query = "delete from client where email = ? and id_client = ? ";
            ps = conn.prepareStatement(query);
            ps.setString(1, mail);
            ps.setLong(2, id);
            ps.executeUpdate();
            return true;
        } catch (Exception e) {
            log.error("Ошибка удаления электронного адреса ", e);
            System.err.println("Ошибка удаления электронного адреса " + e.getMessage());
            return false;
        }
    }

    /* ****************************************************************************
     * ******************************NSI*******************************************
     *****************************************************************************/
    public void clearTrudoZat() throws Exception {
        try {
            String querty = "delete from trudozatrati;";
            ps = conn.prepareStatement(querty);
            ps.executeUpdate();
        } catch (Exception e) {
            log.error("Ошибка при очистке таблицы трудозатрат ", e);
            System.err.println("Ошибка очистке таблицы трудозатрат " + e.getMessage());
            throw new Exception("Ошибка при очистке таблицы трудозатрат");
        }
    }

    public void insertTrudoZat(Integer art, Integer fas, Float zatr) throws Exception {
        try {
            String querty = "INSERT INTO trudozatrati(art, fas, zatr) values( ?, ?, ?);";
            ps = conn.prepareStatement(querty);
            ps.setLong(1, art);
            ps.setInt(2, fas);
            ps.setFloat(3, zatr);
            ps.executeUpdate();
        } catch (Exception e) {
            log.error("Ошибка при добавлении трудозатрат ", e);
            System.err.println("Ошибка при добавлении трудозатрат " + e.getMessage());
            throw new Exception("Ошибка при добавлении трудозатрат");
        }
    }

    public float getTrudoZat(Integer art, Integer fas, Integer kol) throws Exception {
        float zatr = 0;
        try {
            String querty = "SELECT zatr from trudozatrati where art/10000 = ? and fas = ?;";
            ps = conn.prepareStatement(querty);
            ps.setLong(1, art / 10000);
            ps.setInt(2, fas);
            rs = ps.executeQuery();
            if (rs.next()) {
                zatr += rs.getFloat(1) * kol;
            }
        } catch (Exception e) {
            log.error("Ошибка при получении трудозатрат ", e);
            System.err.println("Ошибка при получении трудозатрат " + e.getMessage());
            throw new Exception("Ошибка при получении трудозатрат");
        }
        return zatr;
    }


    public void insertPlanSstoimst(ArrayList rows) throws Exception {
        try {
            setAutoCommit(false);

            //создали временную таблицу
            String query = "CREATE temp TABLE tmp_plan_sstoimost(begin_date date NOT NULL, cex integer NOT NULL, art character varying(9) NOT NULL, razm integer NOT NULL, syrje real NOT NULL, vsp_mat real NOT NULL, toplivo real NOT NULL, zarplata real NOT NULL, soc_strax real NOT NULL, amort real NOT NULL, dor_fond real NOT NULL, fndzan_ozd real NOT NULL, chrez_nal real NOT NULL, transport real NOT NULL, prochie real NOT NULL, inov_fond real NOT NULL, det_fond real NOT NULL, dt_korr date NOT NULL, kr integer NOT NULL, prc real NOT NULL);";
            ps = conn.prepareStatement(query);
            ps.executeUpdate();

            //добавляем в справочник новые данные
            int count = rows.size();
            Object row[];
            for (int i = 0; i < count; i++) {
                row = (Object[]) rows.get(i);

                String querty = "INSERT INTO nsi_plan_sstoimost(begin_date, cex, art, razm, syrje, vsp_mat, toplivo, zarplata, soc_strax, amort, dor_fond, fndzan_ozd, chrez_nal, transport, prochie, inov_fond, det_fond, dt_korr, kr, prc)" +
                        " values(?, ?,?, ?, ?,?, ?, ?,?, ?, ?,?, ?, ?,?, ?, ?,?, ?, ?);";
                ps = conn.prepareStatement(querty);

                java.util.Date d = new java.util.Date(System.currentTimeMillis());
                int m = d.getMonth();
                int y = 0;
                if (m < (Integer.parseInt((String) row[0]) - 1)) y = 1;
                d.setMonth(Integer.parseInt((String) row[0]) - 1);
                d.setDate(Integer.parseInt((String) row[1]));

                ps.setDate(1, new java.sql.Date(d.getYear() - y, d.getMonth(), d.getDate()));
                ps.setInt(2, Integer.parseInt((String) row[2]));
                ps.setString(3, (String) row[3]);
                ps.setInt(4, Integer.parseInt((String) row[4]));
                ps.setDouble(5, (Double) row[5]);
                ps.setDouble(6, (Double) row[6]);
                ps.setDouble(7, (Double) row[7]);
                ps.setDouble(8, (Double) row[8]);
                ps.setDouble(9, (Double) row[9]);
                ps.setDouble(10, (Double) row[10]);
                ps.setDouble(11, (Double) row[11]);
                ps.setDouble(12, (Double) row[12]);
                ps.setDouble(13, (Double) row[13]);
                ps.setDouble(14, (Double) row[14]);
                ps.setDouble(15, (Double) row[15]);
                ps.setDouble(16, (Double) row[16]);
                ps.setDouble(17, (Double) row[17]);
                ps.setDate(18, new java.sql.Date(((java.util.Date) row[18]).getTime()));
                ps.setLong(19, (Long) row[19]);
                ps.setDouble(20, (Double) row[20]);

                ps.executeUpdate();
            }

            //заполняем временную таблицу уникальными запясями справочника
            query = "insert into tmp_plan_sstoimost  select distinct begin_date, cex, art, razm, syrje, vsp_mat, toplivo, zarplata, soc_strax, amort, dor_fond, fndzan_ozd, chrez_nal, transport, prochie,  " +
                    " inov_fond, det_fond, dt_korr, kr, prc from nsi_plan_sstoimost;";
            ps = conn.prepareStatement(query);
            ps.executeUpdate();

            //очищаем справочник
            query = "delete from nsi_plan_sstoimost;";
            ps = conn.prepareStatement(query);
            ps.executeUpdate();

            //копируем записи из временной таблицы в справочник
            query = "insert into nsi_plan_sstoimost (begin_date, cex, art, razm, syrje, vsp_mat, toplivo, zarplata, soc_strax, amort, dor_fond, fndzan_ozd, chrez_nal, transport, prochie,  " +
                    " inov_fond, det_fond, dt_korr, kr, prc) select * from tmp_plan_sstoimost;";
            ps = conn.prepareStatement(query);
            ps.executeUpdate();
            //удаляем временную таблицу
            query = "drop TABLE tmp_plan_sstoimost;";
            ps = conn.prepareStatement(query);
            ps.executeUpdate();
            commit();
        } catch (Exception e) {
            rollBack();
            log.error("Ошибка при добавлении плановой себестоимости ", e);
            System.err.println("Ошибка при добавлении плановой себестоимости " + e.getMessage());
            throw new Exception("Ошибка при добавлении плановой себестоимости");
        } finally {
            setAutoCommit(true);
        }
    }

    public float getSstoimost(Long sar, Long size) {
        float zatr = 0;
        try {
            String querty = "select  (syrje + toplivo + vsp_mat + amort + dor_fond + soc_strax + fndzan_ozd + chrez_nal + prochie) from nsi_plan_sstoimost where art = ? and razm <= ? order by begin_date desc, dt_korr desc, razm desc, id desc limit 1 ";
            ps = conn.prepareStatement(querty);
            ps.setString(1, sar.toString());
            ps.setLong(2, size);
            rs = ps.executeQuery();
            if (rs.next()) {
                zatr = rs.getFloat(1);
            } else {
                querty = "select  (syrje + toplivo + vsp_mat + amort + dor_fond + soc_strax + fndzan_ozd + chrez_nal + prochie) from  nsi_plan_sstoimost where art = ? and razm = 0  order by begin_date desc, dt_korr desc, razm desc, id desc limit 1 ";
                ps = conn.prepareStatement(querty);
                ps.setString(1, sar.toString());
                rs = ps.executeQuery();
                if (rs.next()) {
                    zatr = rs.getFloat(1);
                }
            }
        } catch (Exception e) {
            log.error("Ошибка при получении трудозатрат ", e);
            System.err.println("Ошибка при получении трудозатрат " + e.getMessage());
        }
        return zatr;
    }

    public boolean insertOtgruz(String dao, ArrayList list) {
        int i;
        String sql;
        Object[] object;
        Calendar cal = Calendar.getInstance();
        try {
            setAutoCommit(false);

            sql = "Delete from nsi_otgruz where text(dao) like ? ";
            ps = conn.prepareStatement(sql);
            ps.setString(1, dao.substring(3) + "-" + dao.substring(0, 2) + "%");
            ps.execute();

            for (Iterator it = list.iterator(); it.hasNext(); ) {
                i = 1;
                object = (Object[]) it.next();
                cal.set(Integer.parseInt(dao.substring(3)),
                        Integer.parseInt(dao.substring(0, 2)) - 1,
                        Integer.parseInt(object[0].toString().substring(0, object[0].toString().length() - 2)));

                sql = "Insert into nsi_otgruz ( dao, kpl, sk, ttn, nak, sar, sot, fas, rst, rzm,"
                        + " srt, pach, kol, cno, cnr, kcv, npt, vcn, crr, ndb, ndr, psk)"
                        + " values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ;";
                ps = conn.prepareStatement(sql);
                ps.setDate(1, new java.sql.Date(cal.getTimeInMillis()));
                ps.setInt(2, Integer.parseInt(object[i++].toString()));
                ps.setInt(3, Integer.parseInt(object[i++].toString()));
                ps.setInt(4, Integer.parseInt(object[i++].toString()));
                ps.setInt(5, Integer.parseInt(object[i++].toString()));
                ps.setInt(6, Integer.parseInt(object[i++].toString()));
                ps.setInt(7, Integer.parseInt(object[i++].toString()));
                ps.setInt(8, Integer.parseInt(object[i++].toString()));
                ps.setInt(9, Integer.parseInt(object[i++].toString()));
                ps.setInt(10, Integer.parseInt(object[i++].toString()));
                ps.setInt(11, Integer.parseInt(object[i++].toString()));
                ps.setInt(12, Integer.parseInt(object[i++].toString()));
                ps.setDouble(13, Double.parseDouble(object[i++].toString()));
                ps.setDouble(14, Double.parseDouble(object[i++].toString()));
                ps.setDouble(15, Double.parseDouble(object[i++].toString()));
                ps.setInt(16, Integer.parseInt(object[i++].toString()));
                ps.setInt(17, Integer.parseInt(object[i++].toString()));
                ps.setString(18, object[i++].toString());
                ps.setDouble(19, Double.parseDouble(object[i++].toString()));
                ps.setDouble(20, Double.parseDouble(object[i++].toString()));
                ps.setDouble(21, Double.parseDouble(object[i++].toString()));
                ps.setDouble(22, Double.parseDouble(object[i++].toString()));
                ps.execute();
            }

            commit();
            return true;
        } catch (Exception e) {
            rollBack();
            log.error("Ошибка insertOtgruz() ", e);
            System.err.println("Ошибка insertOtgruz() " + e.getMessage());
            return false;
        } finally {
            setAutoCommit(true);
        }
    }
     
/* ****************************************************************************
---------------------------------Остатки---------------------------------------
******************************************************************************/


//упаковка

    /**
     * возвращает остатки на упаковке на начало месяца
     * @return набор данных в виде: код изделия -- цвет -- кол-во
     */
    public ArrayList monthOstUpack(int month) {
        ArrayList items = new ArrayList();
        try {
            String query = "select kod_izd, color, month" + month + " from ost_upack  where month" + month + " <> 0 order by kod_izd, color";
            ps = conn.prepareStatement(query);
            rs = ps.executeQuery();
            while (rs.next()) {
                Vector v = new Vector();
                v.add(rs.getInt(1));
                v.add(rs.getString(2));
                v.add(rs.getInt(3));
                items.add(v);
            }
        } catch (Exception e) {
            System.err.println("Ошибка monthOstUpack " + e.getMessage());
        }
        return items;
    }

    /**
     * возвращает остатки на упаклвке на текущую дату
     * @param eDate -- дата
     * @return набор данных в виде: код изделия -- цвет -- кол-во
     */
    public List dateOstUpack(String eDate) {
        String sDate;
        int m = Integer.parseInt(eDate.substring(3, 5));
        if (m < 10) sDate = "01." + "0" + m + eDate.substring(5);
        else sDate = "01." + m + eDate.substring(5);

        String month = "month" + Integer.parseInt(eDate.substring(3, 5));
        List items = new ArrayList();
        long sd;
        long ed;
        DB db = new DB();
        sd = convertDateStrToLong(sDate);
        ed = convertDateStrToLong(eDate) + DAY;
        items = db.sdanoNaUpack(sd, ed);

        Iterator it = items.iterator();
        String query;
        int kod_izd;
        String color;
        int count;

        try {
            setAutoCommit(false);
            query = " drop table if exists tmp_ost_upack";
            ps = conn.prepareStatement(query);
            ps.executeUpdate();

            query = " create temp table tmp_ost_upack(kod_izd integer NOT NULL, color character(15) NOT NULL, count integer NOT NULL DEFAULT 0);";
            ps = conn.prepareStatement(query);
            ps.executeUpdate();

            //заполняем временную таблицу принятыми изделиями
            query = "INSERT INTO tmp_ost_upack VALUES(?, ?, ?);";
            ps = conn.prepareStatement(query);
            while (it.hasNext()) {
                kod_izd = Integer.parseInt(it.next().toString());
                color = it.next().toString();
                count = Integer.parseInt(it.next().toString());
                ps.setInt(1, kod_izd);
                ps.setString(2, color);
                ps.setInt(3, count);
                ps.executeUpdate();
            }

            //добавляем изделия на начало месяца
            query = "UPDATE tmp_ost_upack set count = count + " + month + " from (select " + month + ", kod_izd, color from ost_upack)as t1 where t1.kod_izd = tmp_ost_upack.kod_izd and t1.color = tmp_ost_upack.color;";
            ps = conn.prepareStatement(query);
            ps.executeUpdate();

            query = "insert into tmp_ost_upack (kod_izd, color, count) " +
                    " select os.kod_izd, os.color, os." + month + " from (select kod_izd, color, " + month + " from ost_upack where " + month + " <> 0) os " +
                    " left join tmp_ost_upack as ot on os.kod_izd = ot.kod_izd and os.color = ot.color where ot.kod_izd is null;";
            ps = conn.prepareStatement(query);
            ps.executeUpdate();

            //вычитаем изделия которые сдали
            ArrayList newItems = new ArrayList();
            items = db.sdanoUpackSklad(sd, ed);
            it = items.iterator();
            query = "UPDATE tmp_ost_upack set count = count - ? where kod_izd = ? and color = ?;";
            ps = conn.prepareStatement(query);
            while (it.hasNext()) {
                kod_izd = Integer.parseInt(it.next().toString());
                color = it.next().toString();
                count = Integer.parseInt(it.next().toString());
                ps.setInt(1, count);
                ps.setInt(2, kod_izd);
                ps.setString(3, color);
                if (ps.executeUpdate() == 0) {
                    newItems.add(kod_izd);
                    newItems.add(color);
                    newItems.add(count);
                }

            }

            it = newItems.iterator();
            query = "insert into tmp_ost_upack (kod_izd, color, count) values(?, ?, ?);";
            ps = conn.prepareStatement(query);
            while (it.hasNext()) {
                kod_izd = Integer.parseInt(it.next().toString());
                color = it.next().toString();
                count = Integer.parseInt(it.next().toString());
                ps.setInt(1, kod_izd);
                ps.setString(2, color);
                ps.setInt(3, -1 * count);
                ps.executeUpdate();
            }

            items = new ArrayList();
            query = "select kod_izd, color, count from tmp_ost_upack;";
            ps = conn.prepareStatement(query);
            ps.executeQuery();
            rs = ps.executeQuery();
            while (rs.next()) {
                Vector v = new Vector();
                v.add(rs.getInt(1));//код изделия
                v.add(rs.getString(2));//цвет изделия
                v.add(rs.getInt(3));//кол-во
                items.add(v);
            }
            commit();
            return items;
        } catch (Exception e) {
            rollBack();
            System.err.println("Ошибка dateOstUpack " + e.getMessage());
            return null;
        } finally {
            setAutoCommit(true);
        }
    }

    /**
     * подсчёт остатков на начало месяца
     * @param eDate
     */
    public void ostNewMonthUpack(String eDate) {

        List items;
        long sd;
        long ed;
        DB db = new DB();

        String sDate;
        int m = Integer.parseInt(eDate.substring(3, 5));
        m--;
        if (m == 0) {
            sDate = "01.12." + (Integer.parseInt(eDate.substring(6)) - 1);
            m = 12;
        } else {
            if (m < 10) sDate = "01." + "0" + m + eDate.substring(5);
            else sDate = "01." + m + eDate.substring(5);
        }
        String month = "month" + m;
        sd = convertDateStrToLong(sDate);
        ed = convertDateStrToLong(eDate);
        items = db.sdanoNaUpack(sd, ed);

        Iterator it = items.iterator();
        String query;
        int kod_izd;
        String color;
        int count;

        try {
            setAutoCommit(false);
            //удаляем временную таблицу с остатками
            query = " drop table if exists tmp_ost_upack";
            ps = conn.prepareStatement(query);
            ps.executeUpdate();

            //создаём временную таблицу с остатками
            query = " create temp table tmp_ost_upack(kod_izd integer NOT NULL, color character(15) NOT NULL, count integer NOT NULL DEFAULT 0);";
            ps = conn.prepareStatement(query);
            ps.executeUpdate();

            //заполняем временную таблицу пришедшими изделиями
            query = "INSERT INTO tmp_ost_upack VALUES(?, ?, ?);";
            ps = conn.prepareStatement(query);
            while (it.hasNext()) {
                kod_izd = Integer.parseInt(it.next().toString());
                color = it.next().toString();
                count = Integer.parseInt(it.next().toString());
                ps.setInt(1, kod_izd);
                ps.setString(2, color);
                ps.setInt(3, count);
                ps.executeUpdate();
            }

            //добовляем к временным остаткам остатки на начало прошлого месяца
            query = "UPDATE tmp_ost_upack set count = count + " + month + " from (select " + month + ", kod_izd, color from ost_upack)as t1 where t1.kod_izd = tmp_ost_upack.kod_izd and t1.color = tmp_ost_upack.color;";
            ps = conn.prepareStatement(query);
            ps.executeUpdate();

            query = "insert into tmp_ost_upack (kod_izd, color, count) " +
                    " select os.kod_izd, os.color, os." + month + " from (select kod_izd, color, " + month + " from ost_upack where " + month + " <> 0) os " +
                    " left join tmp_ost_upack as ot on os.kod_izd = ot.kod_izd and os.color = ot.color where ot.kod_izd is null;";
            ps = conn.prepareStatement(query);
            ps.executeUpdate();

            //обнуляем остатки за прошлый год в текущем месяце
            if (m == 12) m = 1;
            else m++;
            query = "update ost_upack set month" + m + " = 0";
            ps = conn.prepareStatement(query);
            ps.executeUpdate();

            // копируем остатки из временной таблицы в постоянную с остатками на начало месяца

            query = "UPDATE ost_upack set month" + m + " = count from (select count, kod_izd, color from tmp_ost_upack)as t1 where t1.kod_izd = ost_upack.kod_izd and t1.color = ost_upack.color;";
            ps = conn.prepareStatement(query);
            ps.executeUpdate();

            query = "insert into ost_upack (kod_izd, color, month" + m + ") " +
                    " select ot.kod_izd, ot.color, ot.count from (select kod_izd, color, count from tmp_ost_upack) ot " +
                    " left join ost_upack as os on ot.kod_izd = os.kod_izd and ot.color = os.color where os.kod_izd is null;";
            ps = conn.prepareStatement(query);
            ps.executeUpdate();

            //очищаем временую таблицу
            query = "delete from tmp_ost_upack";
            ps = conn.prepareStatement(query);
            ps.executeUpdate();

            //заполняем временную таблицу отгруженными изделиями

            items = db.sdanoUpackSklad(sd, ed);
            it = items.iterator();
            query = "INSERT INTO tmp_ost_upack VALUES(?, ?, ?);";
            ps = conn.prepareStatement(query);
            while (it.hasNext()) {
                kod_izd = Integer.parseInt(it.next().toString());
                color = it.next().toString();
                count = Integer.parseInt(it.next().toString());
                ps.setInt(1, kod_izd);
                ps.setString(2, color);
                ps.setInt(3, count);
                ps.executeUpdate();
            }

            //вычитаем отгрузку из остатков
            query = "UPDATE ost_upack set month" + m + " = month" + m + " - count from (select count, kod_izd, color from tmp_ost_upack)as t1 where t1.kod_izd = ost_upack.kod_izd and t1.color = ost_upack.color;";
            ps = conn.prepareStatement(query);
            ps.executeUpdate();

            commit();
        } catch (Exception e) {
            rollBack();
            System.err.println("Откат базы назад");
            System.err.println("Ошибка ostNewMonth " + e);
        } finally {
            setAutoCommit(true);
        }
    }

//склад

    /**
     * возвращает остатки на складе на начало месяца
     * @param  month номер месяца
     * @return набор данных в виде: код изделия -- цвет -- кол-во
     */
    public ArrayList monthOstSklad(int month) {
        ArrayList items = new ArrayList();
        try {
            String query = "select kod_izd, color, month" + month + " from ost_sklad  where month" + month + " <> 0 order by kod_izd, color";
            ps = conn.prepareStatement(query);
            rs = ps.executeQuery();
            while (rs.next()) {
                Vector v = new Vector();
                v.add(rs.getInt(1));
                v.add(rs.getString(2));
                v.add(rs.getInt(3));
                items.add(v);
            }
        } catch (Exception e) {
            System.err.println("Ошибка printOstTemp " + e.getMessage());
        }
        return items;
    }

    /**
     * возвращает остатки на складе на текущую дату
     * @param  eDate -- дата
     * @return набор данных в виде: код изделия -- цвет -- кол-во
     */
    public List dateOstSklad(String eDate) {
        String sDate;
        int c = 0;
        int m = Integer.parseInt(eDate.substring(3, 5));
        if (m < 10) sDate = "01." + "0" + m + eDate.substring(5);
        else sDate = "01." + m + eDate.substring(5);

        String month = "month" + Integer.parseInt(eDate.substring(3, 5));
        List items;
        long sd;
        long ed;
        DB db = new DB();
        sd = convertDateStrToLong(sDate);
        ed = convertDateStrToLong(eDate) + DAY;
        items = db.zdanoNaSklad(sd, ed);

        Iterator it = items.iterator();
        String query;
        int kod_izd;
        String color;
        int count;
        ArrayList newItems;

        try {
            //создаём временную таблицу
            setAutoCommit(false);
            query = " drop table if exists ost_temp";
            ps = conn.prepareStatement(query);
            ps.executeUpdate();

            query = " create temp table ost_temp(kod_izd integer NOT NULL, color character(15) NOT NULL, count integer NOT NULL DEFAULT 0);";
            ps = conn.prepareStatement(query);
            ps.executeUpdate();

            //заполняем временную таблицу изделиями принятыми скалдом

            query = "UPDATE ost_temp set count = count + ? where kod_izd = ? and color = ?;";
            ps = conn.prepareStatement(query);
            String query2 = "insert into ost_temp (kod_izd, color, count) values(?, ?, ?);";
            PreparedStatement ps2 = conn.prepareStatement(query2);
            while (it.hasNext()) {
                kod_izd = Integer.parseInt(it.next().toString());
                color = it.next().toString();
                count = Integer.parseInt(it.next().toString());
                ps.setInt(1, count);
                ps.setInt(2, kod_izd);
                ps.setString(3, color);
                if (ps.executeUpdate() == 0) {
                    ps2.setInt(1, kod_izd);
                    ps2.setString(2, color);
                    ps2.setInt(3, count);
                    ps2.executeUpdate();
                }

            }
            if (ps2 != null) ps2.close();

            //добавляем изделия на начало месяца
            query = "UPDATE ost_temp set count = count + " + month + " from (select " + month + ", kod_izd, color from ost_sklad)as t1 where t1.kod_izd = ost_temp.kod_izd and t1.color = ost_temp.color;";
            ps = conn.prepareStatement(query);
            ps.executeUpdate();

            query = "insert into ost_temp (kod_izd, color, count) " +
                    " select os.kod_izd, os.color, os." + month + " from (select kod_izd, color, " + month + " from ost_sklad where " + month + " <> 0) os " +
                    " left join ost_temp as ot on os.kod_izd = ot.kod_izd and os.color = ot.color where ot.kod_izd is null;";
            ps = conn.prepareStatement(query);
            ps.executeUpdate();

            //вычитаем отгрузку
            newItems = new ArrayList();
            items = db.otgruzkaSklad(sd, ed);
            it = items.iterator();
            query = "UPDATE ost_temp set count = (count - ?) where kod_izd = ? and color = ?;";
            ps = conn.prepareStatement(query);
            while (it.hasNext()) {
                kod_izd = Integer.parseInt(it.next().toString());
                color = it.next().toString().trim();
                count = Integer.parseInt(it.next().toString());
                ps.setLong(1, count);
                ps.setLong(2, kod_izd);
                ps.setString(3, color);
                int r = ps.executeUpdate();
                if (r == 0) {
                    newItems.add(kod_izd);
                    newItems.add(color);
                    newItems.add(count);
                }
            }

            it = newItems.iterator();
            query = "insert into ost_temp (kod_izd, color, count) values(?, ?, ?);";
            ps = conn.prepareStatement(query);
            while (it.hasNext()) {
                kod_izd = Integer.parseInt(it.next().toString());
                color = it.next().toString();
                count = Integer.parseInt(it.next().toString());
                ps.setInt(1, kod_izd);
                ps.setString(2, color);
                ps.setInt(3, (-1 * count));
                ps.executeUpdate();
            }

            //создаём список остатков
            items = new ArrayList();
            query = "select kod_izd, color, count from ost_temp;";
            ps = conn.prepareStatement(query);
            ps.executeQuery();
            rs = ps.executeQuery();
            while (rs.next()) {
                Vector v = new Vector();
                v.add(rs.getInt(1));//код изделия
                v.add(rs.getString(2));//цвет изделия
                c = rs.getInt(3);
                v.add(c);//кол-во
                if (c != 0) items.add(v);
            }
            commit();
            return items;
        } catch (Exception e) {
            rollBack();
            System.err.println("Ошибка dateOstSklad " + e.getMessage());
            return null;
        } finally {
            setAutoCommit(true);
        }
    }

    /**
     * подсчёт остатков на начало месяца
     * @rezalt eDate
     */
    public void ostNewMonthSklad(String eDate) {

        List items;
        long sd;
        long ed;
        DB db = new DB();

        String sDate;
        int m = Integer.parseInt(eDate.substring(3, 5));
        m--;
        if (m == 0) {
            sDate = "01.12." + (Integer.parseInt(eDate.substring(6)) - 1);
            m = 12;
        } else {
            if (m < 10) sDate = "01." + "0" + m + eDate.substring(5);
            else sDate = "01." + m + eDate.substring(5);
        }
        String month = "month" + m;
        sd = convertDateStrToLong(sDate);
        ed = convertDateStrToLong(eDate);
        items = db.zdanoNaSklad(sd, ed);

        Iterator it = items.iterator();
        String query;
        int kod_izd;
        String color;
        int count;

        try {
            setAutoCommit(false);
            //удаляем временную таблицу с остатками
            query = " drop table if exists ost_temp";
            ps = conn.prepareStatement(query);
            ps.executeUpdate();

            //создаём временную таблицу с остатками
            query = " create temp table ost_temp(kod_izd integer NOT NULL, color character(15) NOT NULL, count integer NOT NULL DEFAULT 0);";
            ps = conn.prepareStatement(query);
            ps.executeUpdate();

            //заполняем временную таблицу изделиями пришедшими на склад
            query = "UPDATE ost_temp set count = count + ? where kod_izd = ? and color = ?;";
            ps = conn.prepareStatement(query);
            String query2 = "insert into ost_temp (kod_izd, color, count) values(?, ?, ?);";
            PreparedStatement ps2 = conn.prepareStatement(query2);
            while (it.hasNext()) {
                kod_izd = Integer.parseInt(it.next().toString());
                color = it.next().toString();
                count = Integer.parseInt(it.next().toString());
                ps.setInt(1, count);
                ps.setInt(2, kod_izd);
                ps.setString(3, color);
                if (ps.executeUpdate() == 0) {
                    ps2.setInt(1, kod_izd);
                    ps2.setString(2, color);
                    ps2.setInt(3, count);
                    ps2.executeUpdate();
                }

            }
            if (ps2 != null) ps2.close();

            //добовляем к временным остаткам остатки на начало прошлого месяца
            query = "UPDATE ost_temp set count = count + " + month + " from (select " + month + ", kod_izd, color from ost_sklad)as t1 where t1.kod_izd = ost_temp.kod_izd and t1.color = ost_temp.color;";
            ps = conn.prepareStatement(query);
            ps.executeUpdate();

            query = "insert into ost_temp (kod_izd, color, count) " +
                    " select os.kod_izd, os.color, os." + month + " from (select kod_izd, color, " + month + " from ost_sklad where " + month + " <> 0) os " +
                    " left join ost_temp as ot on os.kod_izd = ot.kod_izd and os.color = ot.color where ot.kod_izd is null;";
            ps = conn.prepareStatement(query);
            ps.executeUpdate();

            //обнуляем остатки за прошлый год в текущем месяце
            if (m == 12) m = 1;
            else m++;
            query = "update ost_sklad set month" + m + " = 0";
            ps = conn.prepareStatement(query);
            ps.executeUpdate();

            // копируем остатки из временной таблицы в постоянную с остатками на начало месяца

            query = "UPDATE ost_sklad set month" + m + " = count from (select count, kod_izd, color from ost_temp)as t1 where t1.kod_izd = ost_sklad.kod_izd and t1.color = ost_sklad.color;";
            ps = conn.prepareStatement(query);
            ps.executeUpdate();

            query = "insert into ost_sklad (kod_izd, color, month" + m + ") " +
                    " select ot.kod_izd, ot.color, ot.count from (select kod_izd, color, count from ost_temp) ot " +
                    " left join ost_sklad as os on ot.kod_izd = os.kod_izd and ot.color = os.color where os.kod_izd is null;";
            ps = conn.prepareStatement(query);
            ps.executeUpdate();

            //очищаем временую таблицу
            query = "delete from ost_temp";
            ps = conn.prepareStatement(query);
            ps.executeUpdate();

            //заполняем временную таблицу отгруженными изделиями

            items = db.otgruzkaSklad(sd, ed);
            it = items.iterator();
            query = "INSERT INTO ost_temp VALUES(?, ?, ?);";
            ps = conn.prepareStatement(query);
            while (it.hasNext()) {
                kod_izd = Integer.parseInt(it.next().toString());
                color = it.next().toString();
                count = Integer.parseInt(it.next().toString());
                ps.setInt(1, kod_izd);
                ps.setString(2, color);
                ps.setInt(3, count);
                ps.executeUpdate();
            }

            //вычитаем отгрузку из остатков
            query = "UPDATE ost_sklad set month" + m + " = month" + m + " - count from (select count, kod_izd, color from ost_temp)as t1 where t1.kod_izd = ost_sklad.kod_izd and t1.color = ost_sklad.color;";
            ps = conn.prepareStatement(query);
            ps.executeUpdate();

            commit();
        } catch (Exception e) {
            rollBack();
            System.err.println("Откат базы назад");
            System.err.println("Ошибка ostNewMonth " + e.getMessage());
        } finally {
            setAutoCommit(true);
        }
    }

    public void setOst(int kod, String color, int kol) {
        try {
            ps = conn.prepareStatement("insert into ost_sklad (kod_izd , color, month11) values (?,?,?)");
            ps.setInt(1, kod);
            ps.setString(2, color);
            ps.setInt(3, kol);
            ps.executeUpdate();
        } catch (Exception e) {
            log.error("Ошибка setOst(): ", e);
            JOptionPane.showMessageDialog(null, "Ошибка setOst(): " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

/* ****************************************************************************
---------------------------Маркетинг-------------------------------------------
******************************************************************************/

    /**
     * cохраняет изображения в БД (миниатюра + исходное)
     * @param file
     * @param sar
     * @param smallImage
     * @return true/false
     */
    public boolean addImage(File file, int sar, byte[] smallImage) {
        try {
            ps = conn.prepareStatement("INSERT INTO izd_images (sar, image, image_thumb) VALUES( ?, ?, ?)");
            ps.setInt(1, sar);
            ps.setBytes(2, smallImage);
            ps.setBinaryStream(3, new FileInputStream(file), (int) file.length());
            ps.executeUpdate();
        } catch (Exception e) {
            log.error("Ошибка addImage(): ", e);
            JOptionPane.showMessageDialog(null, "Ошибка addImage(): " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    /**
     * удаляет изображения из БД (миниатюра + исходное)
     * @param selectid
     * @param sar
     * @return true/false
     */
    public boolean deleteImage(int selectid, int sar) {
        String query = "delete from izd_images where id=? and sar=?";
        try {
            ps = conn.prepareStatement(query);
            ps.setInt(1, selectid);
            ps.setInt(2, sar);
            ps.executeUpdate();
        } catch (Exception e) {
            log.error("Ошибка deleteImage(): ", e);
            JOptionPane.showMessageDialog(null, "Ошибка deleteImage(): " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    /**
     * возвращает все миниатюры изображений
     * @param sar
     * @return
     */
    public Image_[] getSmallImages(int sar) {
        ArrayList<Image_> pictures = new ArrayList<Image_>();
        Image_[] images = null;
        try {
            ps = conn.prepareStatement("Select * from izd_images where sar=" + sar + "Order by id");
            rs = ps.executeQuery();
            while (rs.next()) {
                pictures.add(new Image_(
                        rs.getInt("id"),
                        rs.getInt("sar"),
                        rs.getBytes("image_thumb")));
            }

            if (pictures.isEmpty()) {
                ps = conn.prepareStatement("Select * from izd_images where sar= 1");
                rs = ps.executeQuery();
                while (rs.next()) {
                    pictures.add(new Image_(
                            rs.getInt("id"),
                            rs.getInt("sar"),
                            rs.getBytes("image_thumb")));
                }
            }
            images = new Image_[pictures.size()];
            pictures.toArray(images);
        } catch (Exception e) {
            log.error("Ошибка getSmallImages(): ", e);
            JOptionPane.showMessageDialog(null, "Ошибка getSmallImages(): " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
        return images;
    }

    /**
     * возвращает исходное изорражение
     * @param idPicture
     * @return
     */
    public byte[] getBigImages(int idPicture) {
        byte[] byteImage = null;
        try {
            ps = conn.prepareStatement("Select image_thumb from izd_images where id=" + idPicture);
            rs = ps.executeQuery();
            while (rs.next()) {
                byteImage = rs.getBytes("image_thumb");
            }
        } catch (Exception e) {
            log.error("Ошибка getBigImages(): ", e);
            JOptionPane.showMessageDialog(null, "Ошибка getBigImages(): " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
        return byteImage;
    }

    /**
     * добавляет элемент в корзину
     * @param sar
     * @param nar
     * @param ngpr
     * @param fason
     * @param rost
     * @param razmer
     * @param color
     * @param kol_vo
     */
    public void addInCart(int sar, String nar, String ngpr, int fason, int sort, int rost, int razmer, String color, int kol_vo, String note, boolean flagsar) {
        int count = 0;
        try {
            ps = conn.prepareStatement("Select max(idtmp) from tmpcarts;");
            rs = ps.executeQuery();
            while (rs.next()) count = rs.getInt(1);

            ps = conn.prepareStatement("INSERT INTO tmpcarts VALUES( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
            ps.setInt(1, count + 1);                                   //#
            ps.setInt(2, sar);                                       //шифр
            ps.setString(3, nar);                                    //артикул
            ps.setInt(4, fason);                                     //фасон
            ps.setInt(5, sort);                                      //сорт
            ps.setString(6, ngpr);                                   //изделие
            ps.setInt(7, rost);                                      //рост
            ps.setInt(8, razmer);                                    //размер
            ps.setString(9, color);                                  //цвет
            ps.setInt(10, kol_vo);                                   //ко-во
            ps.setString(11, note);                                  //примечание
            ps.setBoolean(12, false);                                //edit
            ps.setBoolean(13, flagsar);                              //флаг артикул
            ps.executeUpdate();
        } catch (Exception e) {
            log.error("Ошибка addInCart(): ", e);
            JOptionPane.showMessageDialog(null, "Ошибка addInCart(): " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * возвращает содержимое корзины
     * @param idvaluta
     * @param kurs
     * @param nds
     * @return
     */
    public Object[][] returnDataCart(int idvaluta, int kurs, int nds, boolean sorting) {
        Object[][] cart = null;
        int i = 0, j = 0, count = 0;
        DB db = new DB();
        try {
            ps = conn.prepareStatement("SELECT count(*) FROM tmpcarts;");
            rs = ps.executeQuery();
            while (rs.next()) count = rs.getInt(1);

            cart = new Object[count][17];

            if (sorting)
                ps = conn.prepareStatement("Select idtmp,sh_artikul,name_artikul,fason,sort,name_izdelie,rost,razmer,color,kol_vo,note,checkbox,artikul" +
                        " From tmpcarts ORDER BY sh_artikul,name_artikul,artikul,fason,sort,name_izdelie,color,note,rost,razmer");
            else
                ps = conn.prepareStatement("Select idtmp,sh_artikul,name_artikul,fason,sort,name_izdelie,rost,razmer,color,kol_vo,note,checkbox,artikul" +
                        " From tmpcarts ORDER BY idtmp");
            rs = ps.executeQuery();
            while (rs.next()) {
                double[] cena = db.getCena(rs.getInt("sh_artikul"), rs.getString("name_artikul"), rs.getInt("fason"), rs.getInt("sort"), rs.getInt("rost"), rs.getInt("razmer"), idvaluta, kurs);
                if (nds >= 0) cena[1] = nds;
                Object[] data = {rs.getInt("idtmp"),               // ID
                        rs.getInt("sh_artikul"),           // sar
                        rs.getString("name_artikul"),                   // nar
                        rs.getInt("fason"),                // fas
                        rs.getInt("sort"),                 // srt
                        rs.getString("name_izdelie"),                   // ngpr
                        rs.getInt("rost"),                 // rst
                        rs.getInt("razmer"),               // rzm
                        rs.getString("color"),                          // ncw
                        rs.getInt("kol_vo"),               // кол-во
                        cena[0],                                        // cena
                        cena[0] * rs.getLong("kol_vo"),
                        new DecimalFormat("###,###.###").format(((cena[0] * cena[1]) / 100) * rs.getLong("kol_vo")) + "(" + new DecimalFormat("###,###.###").format(cena[1]) + "%)",
                        (cena[0] * rs.getLong("kol_vo")) + (((cena[0] * cena[1]) / 100) * rs.getLong("kol_vo")),
                        rs.getString("note"),                           // примечание
                        rs.getBoolean("checkbox"),                      // edit
                        rs.getBoolean("artikul")};                      // флаг артикул
                for (j = 0; j < 17; j++) {
                    cart[i][j] = data[j];
                }
                ++i;
            }
        } catch (Exception e) {
            log.error("Ошибка returnDataCart(): ", e);
            JOptionPane.showMessageDialog(null, "Ошибка returnDataCart(): " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
        return cart;
    }

    /**
     * очищает корзину
     */
    public void updateCart() {
        try {
            ps = conn.prepareStatement("Drop table if exists tmpcarts;");
            ps.executeUpdate();
            ps = conn.prepareStatement(" Create TEMPORARY table tmpcarts(" +
                    "  idtmp integer NOT NULL," +
                    "  sh_artikul integer," +
                    "  name_artikul character varying(50)," +
                    "  fason integer," +
                    "  sort integer," +
                    "  name_izdelie character varying(150)," +
                    "  rost integer," +
                    "  razmer integer," +
                    "  color character varying(50)," +
                    "  kol_vo integer," +
                    "  note character varying(150), " +
                    "  checkbox boolean ," +
                    "  artikul boolean ," +
                    "  CONSTRAINT tmpcarts_pkey PRIMARY KEY (idtmp)" +
                    ");");
            ps.executeUpdate();
        } catch (Exception e) {
            log.error("Ошибка updateCart(): ", e);
            JOptionPane.showMessageDialog(null, "Ошибка updateCart()! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * редактирует кол-во выбранный продукции в корзине
     * @param idselect
     * @param kolvo
     */
    public void editKolvo(int idselect, int kolvo) {
        try {
            ps = conn.prepareStatement("Update tmpcarts SET kol_vo = ? where idtmp =" + idselect + ";");
            ps.setInt(1, kolvo);
            ps.executeUpdate();
        } catch (Exception e) {
            log.error("Ошибка editKolvo(): ", e);
            JOptionPane.showMessageDialog(null, "Ошибка editKolvo(): " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * редактирует поле в корзине с checkbox-сам для выбранной продукции
     * @param idselect
     * @param value
     */
    public void editChekBox(int idselect, boolean value) {
        try {
            ps = conn.prepareStatement("Update tmpcarts SET checkbox = ? where idtmp =" + idselect + ";");
            ps.setBoolean(1, value);
            ps.executeUpdate();
        } catch (Exception e) {
            log.error("Ошибка editChekBox(): ", e);
            JOptionPane.showMessageDialog(null, "Ошибка editChekBox(): " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    public void editFlagArtikul(int idselect, boolean value) {
        try {
            ps = conn.prepareStatement("Update tmpcarts SET artikul = ? where idtmp =" + idselect + ";");
            ps.setBoolean(1, value);
            ps.executeUpdate();
        } catch (Exception e) {
            log.error("Ошибка editChekBox(): ", e);
            JOptionPane.showMessageDialog(null, "Ошибка editChekBox(): " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    public void editNote(int idselect, Object value) {
        try {
            ps = conn.prepareStatement("Update tmpcarts SET note = ? where idtmp =" + idselect + ";");
            ps.setObject(1, value);
            ps.executeUpdate();
        } catch (Exception e) {
            log.error("Ошибка editNote(): ", e);
            JOptionPane.showMessageDialog(null, "Ошибка editNote(): " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * редактирует все поля с checkbox-сами
     */
    public void selectAll(boolean flag) {
        try {
            ps = conn.prepareStatement("Select checkbox from tmpcarts;");
            rs = ps.executeQuery();
            while (rs.next()) {
                ps = conn.prepareStatement("UPDATE tmpcarts SET checkbox = '" + flag + "';");
                ps.executeUpdate();
            }
        } catch (Exception e) {
            log.error("Ошибка selectAll(): ", e);
            JOptionPane.showMessageDialog(null, "Ошибка selectAll(): " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * удаляет выбранные поля в корзине
     */
    public void deleteChekBox() {
        try {
            ps = conn.prepareStatement("DELETE FROM tmpcarts where checkbox = 'true';");
            ps.executeUpdate();
        } catch (Exception e) {
            log.error("Oшибка deleteChekBox(): ", e);
            JOptionPane.showMessageDialog(null, "Oшибка deleteChekBox(): " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    public void editNameIzdelie(Object value, int row) {
        try {
            ps = conn.prepareStatement("UPDATE tmpcarts SET name_izdelie = '" + value + "' where idtmp = '" + row + "';");
            ps.executeUpdate();
        } catch (Exception e) {
            log.error("Ошибка editNameIzdelie(): ", e);
            JOptionPane.showMessageDialog(null, "Ошибка editNameIzdelie(): " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    public boolean saveCart(int idor, int idcl, int state, int madeorder, String dateorder, int idvaluta, int kurs, int nds, String note) {
        Object[][] cart = returnDataCart(idvaluta, kurs, nds, false);
        int id = 0;
        boolean rezalt = false;
        try {
            setAutoCommit(false);

            if (idor > 0) {
                ps = conn.prepareStatement("UPDATE list_orders SET kod_klient = " + idcl + ", state = " + state + ", dateorder = '" + new Timestamp(new SimpleDateFormat("dd.MM.yyyy").parse(dateorder).getTime()) + "', editorder = " + madeorder + ", dateedit = now() , edit = true where id_order = " + idor + "");
                ps.executeUpdate();

                ps = conn.prepareStatement("DELETE FROM orders where id_order = " + idor + "");
                ps.executeUpdate();

                id = idor;
            } else {
                ps = conn.prepareStatement("INSERT INTO list_orders (kod_klient, state, madeorder, dateorder) VALUES( ?, ?, ?, ?) RETURNING id_order;");
                ps.setInt(1, idcl);
                ps.setInt(2, state);
                ps.setInt(3, madeorder);
                ps.setTimestamp(4, new Timestamp(new SimpleDateFormat("dd.MM.yyyy").parse(dateorder).getTime()));
                rs = ps.executeQuery();
                if (rs.next())
                    id = rs.getInt(1);
            }

            ps = conn.prepareStatement("UPDATE list_orders SET note = '" + note + "' where id_order = " + id + "");
            ps.executeUpdate();

            for (int i = 0; i < cart.length; i++) {
                ps = conn.prepareStatement("INSERT INTO orders VALUES( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? )");
                ps.setInt(1, id);
                ps.setInt(2, Integer.parseInt(cart[i][1].toString()));          //шифр артикул
                ps.setString(3, cart[i][2].toString());                         //артикул
                ps.setInt(4, Integer.parseInt(cart[i][3].toString()));          //фасон
                ps.setInt(5, Integer.parseInt(cart[i][4].toString()));          //сорт
                ps.setString(6, cart[i][5].toString());                         //изделие
                ps.setInt(7, Integer.parseInt(cart[i][6].toString()));          //рост
                ps.setInt(8, Integer.parseInt(cart[i][7].toString()));          //размер
                ps.setString(9, cart[i][8].toString());                         //цвет
                ps.setInt(10, Integer.parseInt(cart[i][9].toString()));         //ко-во
                ps.setString(11, cart[i][14].toString());                       //примечание
                ps.setBoolean(12, Boolean.parseBoolean(cart[i][16].toString()));//флаг артикул
                ps.executeUpdate();
            }
            rezalt = true;
            commit();
        } catch (Exception e) {
            rollBack();
            rezalt = false;
            log.error("Ошибка сохранения заказа: ", e);
            JOptionPane.showMessageDialog(null, "Ошибка сохранения заказа! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        } finally {
            setAutoCommit(true);
        }
        if (state == 0 && rezalt)
            JOptionPane.showMessageDialog(null, "Заказ успешно сохранен под №" + (id) + " ", "Сохранение заказа", javax.swing.JOptionPane.INFORMATION_MESSAGE);
        return rezalt;
    }

    /**
     * возвращает заказы из БД
     * @param param
     * @param sdate
     * @param edate
     * @return набор данных
     */
    public Vector getOrders(int param, String sdate, String edate) {
        Vector tmp = new Vector();
        Vector orders = new Vector();
        Vector ndocs = new Vector();
        String ndoc;
        DB db = new DB();
        try {
            sdate = new SimpleDateFormat("yyyy-MM-dd").format(new SimpleDateFormat("dd.MM.yyyy").parse(sdate));
            edate = new SimpleDateFormat("yyyy-MM-dd").format(new SimpleDateFormat("dd.MM.yyyy").parse(edate));

            timerNewOrder();

            if (param == 3) {
                ps = conn.prepareStatement("SELECT id_order, kod_klient, dateorder, fio, datemade, gatherorder, dategather, state, edit FROM list_orders, employees " +
                        "                     where state = 3 and datemade between '" + sdate + "' and '" + edate + " 23:59:59' and employees.id = madeorder" +
                        "                  Order by dategather");
            } else {
                ps = conn.prepareStatement("SELECT id_order, kod_klient, dateorder, fio, datemade, gatherorder, state, edit FROM list_orders, employees " +
                        " where (state = 1 or state = 2 or state = 4) and datemade between'" + sdate + "' and '" + edate + " 23:59:59' and employees.id = madeorder" +
                        " Order by datemade");
            }
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                tmp = new Vector();
                tmp.add(rs.getInt("state"));
                tmp.add(rs.getBoolean("edit"));
                tmp.add(rs.getInt("id_order"));
                tmp.add(db.getNameClient(rs.getInt("kod_klient")));
                tmp.add(rs.getInt("kod_klient"));
                tmp.add(new SimpleDateFormat("dd-MM-yyyy").format(rs.getDate("dateorder")));
                tmp.add(rs.getString("fio"));
                tmp.add(new SimpleDateFormat("dd-MM-yyyy").format(rs.getDate("datemade")) + " " + rs.getTime("datemade"));

                ndoc = "";
                ndocs = getNDocs(rs.getInt("id_order"));
                for (int i = 0; i < ndocs.size(); i++)
                    ndoc = ndoc + ndocs.get(i) + "; ";
                tmp.add(ndoc);

                ps = conn.prepareStatement("Select fio FROM employees where employees.id = " + rs.getInt("gatherorder") + " ");
                ResultSet rs_ = ps.executeQuery();
                if (rs_.next()) tmp.add(rs_.getString("fio"));
                else tmp.add("-");
                rs_.close();

                orders.add(tmp);
            }
        } catch (Exception e) {
            log.error("Ошибка getOrders(): ", e);
            JOptionPane.showMessageDialog(null, "Ошибка getOrders(): " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
        return orders;
    }

    public Vector getOrder(int idOrder, int param) {
        Vector order = new Vector();
        try {
            if (param == 2) {
                ps = conn.prepareStatement("UPDATE list_orders SET state = 2, edit = 'false' where state < 3 and id_order = " + idOrder + "");
                ps.executeUpdate();
            }

            ps = conn.prepareStatement("Select fason, name_artikul, name_izdelie, rost, razmer, sort, color, kol_vo, orders.note, sh_artikul" +
                    "  from list_orders, orders where list_orders.id_order=orders.id_order and list_orders.id_order = " + idOrder + "" +
                    "  Order by  name_izdelie, fason, color, sort, rost, razmer");
            rs = ps.executeQuery();
            while (rs.next()) {
                Vector tmp = new Vector();
                tmp.add(rs.getObject(1));
                tmp.add(rs.getObject(2));
                tmp.add(rs.getObject(3));
                tmp.add(rs.getObject(4));
                tmp.add(rs.getObject(5));
                tmp.add(rs.getObject(6));
                tmp.add(rs.getObject(7));
                tmp.add(rs.getObject(8));
                order.add(tmp);
            }

        } catch (Exception e) {
            log.error("Ошибка getOrder(): ", e);
            JOptionPane.showMessageDialog(null, "Ошибка getOrder(): " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
        return order;
    }

    public Vector getSavedOrders(int idcl, int madeorder) {
        Vector savedorder = new Vector();
        try {
            ps = conn.prepareStatement("Select id_order, dateorder, fio, department, datemade, list_orders.note FROM list_orders, employees, dept" +
                    "     where kod_klient = " + idcl + " and state = 0 and madeorder = " + madeorder + " and madeorder=employees.id and id_dept = dept.id" +
                    "     Order by  datemade");
            rs = ps.executeQuery();
            while (rs.next()) {
                Vector tmpsavedor = new Vector();
                tmpsavedor.add(rs.getInt("id_order"));
                tmpsavedor.add(new SimpleDateFormat("dd-MM-yyyy").format(rs.getDate("dateorder")));
                tmpsavedor.add(rs.getString("fio"));
                tmpsavedor.add(rs.getString("department"));
                tmpsavedor.add(new SimpleDateFormat("dd-MM-yyyy").format(rs.getDate("datemade")) + " " + rs.getTime("datemade"));
                tmpsavedor.add(rs.getString(6));
                savedorder.add(tmpsavedor);
            }
        } catch (Exception e) {
            log.error("Ошибка getSavedOrders(): ", e);
            JOptionPane.showMessageDialog(null, "Ошибка getSavedOrders(): " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
        return savedorder;
    }

    public void deleteOrder(int idOrder, int idUser) {
        try {
            setAutoCommit(false);

            ps = conn.prepareStatement("UPDATE list_orders SET state = -1, editorder = " + idUser + ", dateedit = now(), timer = true where id_order = " + idOrder + "");
            ps.executeUpdate();

            ps = conn.prepareStatement("DELETE FROM otgruz where id_order = " + idOrder + "");
            ps.executeUpdate();

            commit();
        } catch (Exception e) {
            rollBack();
            log.error("Oшибка deleteOrder(): ", e);
            JOptionPane.showMessageDialog(null, "Oшибка deleteOrder(): " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        } finally {
            setAutoCommit(true);
        }
    }

    public Vector getSkladOrders(String date) {
        Vector orders = new Vector();
        ResultSet rs_ = null;
        DB db = new DB();
        try {
            date = new SimpleDateFormat("yyyy-MM-dd").format(new SimpleDateFormat("dd.MM.yyyy").parse(date));
            ps = conn.prepareStatement("Select id_order, kod_klient, dateorder, fio, department, datemade, state, gatherorder, editorder, dateedit FROM list_orders, employees, dept" +
                    "                                  where state > 0 and madeorder=employees.id and id_dept = dept.id and datemade > '" + date + "'" +
                    "                   Order by id_order");
            rs = ps.executeQuery();
            while (rs.next()) {
                Vector tmpsavedor = new Vector();
                tmpsavedor.add(rs.getInt("id_order"));
                tmpsavedor.add(rs.getInt("kod_klient"));
                tmpsavedor.add(db.getNameClient(rs.getInt("kod_klient")));
                tmpsavedor.add(new SimpleDateFormat("dd-MM-yyyy").format(rs.getDate("dateorder")));
                tmpsavedor.add(rs.getString("fio"));
                tmpsavedor.add(rs.getString("department"));
                tmpsavedor.add(new SimpleDateFormat("dd-MM-yyyy").format(rs.getDate("datemade")) + " " + rs.getTime("datemade"));

                ps = conn.prepareStatement("Select fio FROM employees where employees.id = " + rs.getInt("editorder") + " ");
                rs_ = ps.executeQuery();
                if (rs_.next()) {
                    tmpsavedor.add(rs_.getString("fio"));
                    tmpsavedor.add(new SimpleDateFormat("dd-MM-yyyy").format(rs.getDate("dateedit")) + " " + rs.getTime("dateedit"));
                } else {
                    tmpsavedor.add("-");
                    tmpsavedor.add("");
                }

                ps = conn.prepareStatement("Select fio FROM employees where employees.id = " + rs.getInt("gatherorder") + " ");
                rs_ = ps.executeQuery();
                if (rs_.next())
                    tmpsavedor.add(rs_.getString("fio"));
                else
                    tmpsavedor.add("-");

                rs_.close();

                switch (rs.getInt("state")) {
                    case 1:
                        tmpsavedor.add("не открыта");
                        break;
                    case 2:
                        tmpsavedor.add("формируется");
                        break;
                    case 3:
                        tmpsavedor.add("выполнена");
                        break;
                    case 4:
                        tmpsavedor.add("срок вышел");
                        break;
                    default:
                        tmpsavedor.add("-");
                        break;
                }
                tmpsavedor.add(rs.getInt("state"));

                orders.add(tmpsavedor);
            }
        } catch (Exception e) {
            log.error("Ошибка getSkladOrders: ", e);
            JOptionPane.showMessageDialog(null, "Ошибка getSkladOrders: " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
        return orders;
    }

    public Vector returnDataCartFromSklad(int idClient, int idOrder) {
        Vector cartSklad = new Vector();
        Vector dataOtgruz;
        Vector row;
        Vector tmp;
        HashMap<String, String> map = new HashMap<String, String>();
        int num = 0, idKey = 0;
        DB db = new DB();
        try {
            Vector ndocs = getNDocs(idOrder);

            dataOtgruz = db.getDataOtgruz(idClient, ndocs);

            ps = conn.prepareStatement("Select sh_artikul, name_artikul, fason, sort, name_izdelie, rost, razmer, color, kol_vo " +
                    "from orders where id_order = " + idOrder + "");
            rs = ps.executeQuery();
            while (rs.next()) {
                Vector otgruz = db.getDataOtgruzDetails(idClient, ndocs, rs.getInt("sh_artikul"), rs.getString("name_artikul"),
                        rs.getInt("fason"), rs.getInt("sort"), rs.getString("name_izdelie"), rs.getInt("rost"), rs.getInt("razmer"), rs.getString("color"));
                tmp = new Vector();
                tmp.add(otgruz.get(0));                                         // наличие в заявке(проценты)
                tmp.add(++num);                                                 // №
                tmp.add(rs.getInt("sh_artikul"));                               // шифр
                tmp.add(rs.getString("name_artikul"));                          // артикул
                tmp.add(rs.getInt("fason"));                                    // модель
                tmp.add(rs.getInt("sort"));                                     // сорт
                tmp.add(rs.getString("name_izdelie"));                          // название изд.
                tmp.add(rs.getInt("rost"));                                     // рост
                tmp.add(rs.getInt("razmer"));                                   // размер
                tmp.add(rs.getString("color"));                                 // цвет
                tmp.add(rs.getInt("kol_vo"));                                   // кол-во
                tmp.add(otgruz.get(2));                                         // шифр*
                tmp.add(otgruz.get(3));                                         // артикул*
                tmp.add(otgruz.get(4));                                         // модель*
                tmp.add(otgruz.get(5));                                         // сорт*
                tmp.add(otgruz.get(6));                                         // название изд.*
                tmp.add(otgruz.get(7));                                         // рост*
                tmp.add(otgruz.get(8));                                         // размер*
                tmp.add(otgruz.get(9));                                         // цвет*
                tmp.add(otgruz.get(10));                                        // кол-во*
                map.put(String.valueOf(otgruz.get(1)), String.valueOf(idKey++));
                cartSklad.add(tmp);
            }

            for (int i = 0; i < dataOtgruz.size(); i++) {
                row = (Vector) dataOtgruz.elementAt(i);
                if (map.get(String.valueOf(row.get(0))) != null) {
                    dataOtgruz.removeElementAt(i);
                    i--;
                } else {
                    tmp = new Vector();
                    tmp.add(0);                                                 // наличие в заявке(проценты)
                    tmp.add(++num);                                             // №
                    tmp.add("-");                                               // шифр
                    tmp.add("-");                                               // артикул
                    tmp.add("-");                                               // модель
                    tmp.add("-");                                               // сорт
                    tmp.add("-");                                               // название изд.
                    tmp.add("-");                                               // рост
                    tmp.add("-");                                               // размер
                    tmp.add("-");                                               // цвет
                    tmp.add("-");                                               // кол-во
                    tmp.add(row.get(1));                                        // шифр*
                    tmp.add(row.get(2));                                        // артикул*
                    tmp.add(row.get(3));                                        // модель*
                    tmp.add(row.get(4));                                        // сорт*
                    tmp.add(row.get(5));                                        // название изд.*
                    tmp.add(row.get(6));                                        // рост*
                    tmp.add(row.get(7));                                        // размер*
                    tmp.add(row.get(8));                                        // цвет*
                    tmp.add(row.get(9));                                        // кол-во*
                    cartSklad.add(tmp);
                }
            }
        } catch (Exception e) {
            log.error("Ошибка returnDataCartFromSklad(): ", e);
            JOptionPane.showMessageDialog(null, "Ошибка returnDataCartFromSklad(): " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
        return cartSklad;
    }

    public boolean downloadSavedOrder(int idOrder, int param) {
        int count = 0;
        try {
            setAutoCommit(false);

            if (param == 2) {
                ps = conn.prepareStatement("UPDATE list_orders SET state = 2, edit = 'false' where state < 3 and id_order = " + idOrder + "");
                ps.executeUpdate();
            }

            updateCart();

            ps = conn.prepareStatement("SELECT * FROM orders where id_order= '" + idOrder + "'");
            rs = ps.executeQuery();
            while (rs.next()) {
                ps = conn.prepareStatement("INSERT INTO tmpcarts VALUES( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
                ps.setInt(1, count + 1);                                           //#
                ps.setInt(2, rs.getInt("sh_artikul"));                           //шифр
                ps.setString(3, rs.getString("name_artikul"));                   //артикул
                ps.setInt(4, rs.getInt("fason"));                               //фасон
                ps.setInt(5, rs.getInt("sort"));                                 //сорт
                ps.setString(6, rs.getString("name_izdelie"));                   //изделие
                ps.setInt(7, rs.getInt("rost"));                                 //рост
                ps.setInt(8, rs.getInt("razmer"));                               //размер
                ps.setString(9, rs.getString("color"));                          //цвет
                ps.setInt(10, rs.getInt("kol_vo"));                             //ко-во
                ps.setString(11, rs.getString("note"));                         //примечание
                ps.setBoolean(12, false);                                        //edit
                ps.setBoolean(13, rs.getBoolean("artikul"));                    //флаг артикул
                ps.executeUpdate();
                count++;
            }

            if (param == 1) {
                ps = conn.prepareStatement("Delete FROM list_orders where id_order= '" + idOrder + "'");
                ps.executeUpdate();
            }
            commit();
            return true;
        } catch (Exception e) {
            rollBack();
            log.error("Ошибка downloadSavedOrder(): ", e);
            JOptionPane.showMessageDialog(null, "Ошибка downloadSavedOrder(): " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
            return false;
        } finally {
            setAutoCommit(true);
        }
    }

    public String downloadNoteSavedOrder(int idOrder) {
        String note = "";
        try {
            ps = conn.prepareStatement("SELECT note FROM list_orders where id_order= '" + idOrder + "'");
            rs = ps.executeQuery();
            if (rs.next())
                note = rs.getString("note");
        } catch (Exception e) {
            log.error("Ошибка downloadNoteSavedOrder(): ", e);
            JOptionPane.showMessageDialog(null, "Ошибка downloadNoteSavedOrder(): " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
        return note;
    }

    public Vector getNDocs(int idOrder) {
        Vector ndocs = new Vector();
        try {
            ps = conn.prepareStatement("Select ndoc from otgruz where id_order = " + idOrder + " Order by ndoc desc");
            rs = ps.executeQuery();
            while (rs.next()) {
                ndocs.add(rs.getString("ndoc").trim());
            }
        } catch (Exception e) {
            log.error("Ошибка getNDoc(): ", e);
            JOptionPane.showMessageDialog(null, "Ошибка getNDoc(): " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
        return ndocs;
    }

    public boolean editNDocOrder(Vector ndocs, int idOrder, Object gatherorder) {
        try {
            setAutoCommit(false);

            ps = conn.prepareStatement("Delete FROM otgruz where id_order = " + idOrder + "");
            ps.executeUpdate();

            for (int i = 0; i < ndocs.size(); i++) {
                ps = conn.prepareStatement("INSERT INTO otgruz VALUES( ?, ?, now() )");
                ps.setString(1, ndocs.elementAt(i).toString().trim());
                ps.setInt(2, idOrder);
                ps.executeUpdate();
            }

            ps = conn.prepareStatement("UPDATE list_orders SET gatherorder=" + gatherorder + " where id_order = " + idOrder + " ");
            ps.executeUpdate();

            commit();
            return true;
        } catch (Exception e) {
            rollBack();
            log.error("Ошибка addNDoc(): ", e);
            JOptionPane.showMessageDialog(null, "Ошибка addNDoc(): " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
            return false;
        } finally {
            setAutoCommit(true);
        }
    }

    public void deleteNDoc(Object ndoc) {
        try {
            ps = conn.prepareStatement("Delete FROM otgruz where ndoc = '" + ndoc + "'");
            ps.executeUpdate();
        } catch (Exception e) {
            log.error("Ошибка deleteNDoc(): ", e);
            JOptionPane.showMessageDialog(null, "Ошибка deleteNDoc(): " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    public int getNDocOtgruz(String ndoc) {
        int idOrderUsed = 0;
        try {
            ps = conn.prepareStatement("Select id_order from otgruz where ndoc = '" + ndoc + "';");
            rs = ps.executeQuery();
            if (rs.next()) {
                idOrderUsed = rs.getInt(1);
            }
        } catch (Exception e) {
            log.error("Ошибка getNDocOtgruz(): ", e);
            JOptionPane.showMessageDialog(null, "Ошибка getNDocOtgruz(): " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
        return idOrderUsed;
    }

    public Vector getNDocOstatokPostponed() {
        Vector ndocs = new Vector();
        try {
            ps = conn.prepareStatement("Select ndoc, otgruz.id_order, dateorder from otgruz, list_orders where  dateorder >='today' and state = 3 and list_orders.id_order=otgruz.id_order");
            rs = ps.executeQuery();
            while (rs.next()) {
                Vector tmp = new Vector();
                tmp.add(rs.getString(1).trim());
                tmp.add(rs.getInt(2));
                tmp.add(new SimpleDateFormat("dd.MM.yyyy").format(new SimpleDateFormat("yyyy-MM-dd").parse(rs.getString(3))));
                ndocs.add(tmp);
            }
        } catch (Exception e) {
            log.error("Ошибка getNDocOstatokPostponed(): ", e);
            JOptionPane.showMessageDialog(null, "Ошибка getNDocOstatokPostponed(): " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
        return ndocs;
    }

    public boolean timerNewOrder() {
        boolean rezalt = false;
        try {
            ps = conn.prepareStatement("Select id_order from list_orders where timer = 'false' and state = 1");
            rs = ps.executeQuery();
            if (rs.next()) {
                ps = conn.prepareStatement("UPDATE list_orders SET timer = 'true' where timer = 'false' and state = 1");
                ps.executeUpdate();
                rezalt = true;
            }
        } catch (Exception e) {
            System.out.println("Ошибка " + e);
            log.error("Ошибка timerNewOrder(): ", e);
            JOptionPane.showMessageDialog(null, "Ошибка timerNewOrder(): " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
        return rezalt;
    }

    public void setOrderProcessSklad(int idorder, int state) {
        try {
            if (state == 3) {
                ps = conn.prepareStatement("UPDATE list_orders SET state = 3, dategather = now(), edit = false where id_order = " + idorder + " and (state = 2  or state = 4)");
                ps.executeUpdate();
            } else if (state == 2) {
                ps = conn.prepareStatement("UPDATE list_orders SET state = 2, dategather = null  where id_order = " + idorder + " and state = 3 ");
                ps.executeUpdate();
            }
        } catch (Exception e) {
            log.error("Ошибка setOrderProcessSklad(): ", e);
            JOptionPane.showMessageDialog(null, "Ошибка setOrderProcessSklad(): " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    public int getOstatokPostponed(Object sar, Object nar, Object fason, Object sort, Object rost, Object razmer, Object color) {
        int rezult = 0;
        try {
            ps = conn.prepareStatement("Select sum(kol_vo) from list_orders, orders " +
                    "       where sh_artikul= " + sar + "" +
                    "            	 and name_artikul like '" + nar + "%'" +
                    "              	 and fason= " + fason + "" +
                    "              	 and sort= " + sort + "" +
                    "             	 and rost = " + rost + "" +
                    "              	 and razmer = " + razmer + "" +
                    "              	 and color like '" + color + "%'  " +
                    "                    and (state= 1 or state= 2 or state= 4) " +
                    "             	 and list_orders.id_order=orders.id_order");
            rs = ps.executeQuery();
            if (rs.next())
                rezult = rs.getInt(1);
        } catch (Exception e) {
            log.error("Ошибка getOstatokPostponed(): ", e);
            JOptionPane.showMessageDialog(null, "Ошибка getOstatokPostponed(): " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
        return rezult;
    }

    public Vector<String> getClientsPostponed(Object sar, Object nar, Object fason, Object sort, Object color, Object rost, Object razmer) {
        Vector<String> clients = new Vector<String>();
        DB db = new DB();
        try {
            ps = conn.prepareStatement("Select list_orders.id_order, sum(kol_vo), list_orders.kod_klient, dateorder from list_orders, orders " +
                    "       where sh_artikul= " + sar + "" +
                    "       	 and name_artikul like '" + nar + "%'" +
                    "             	 and fason = " + fason + "" +
                    "             	 and sort = " + sort + "" +
                    "             	 and rost = " + rost + "" +
                    "              	 and razmer = " + razmer + "" +
                    "              	 and color like '" + color + "%'  " +
                    "                and (state = 1 or state = 2 or state = 4) " +
                    "             	 and list_orders.id_order=orders.id_order" +
                    "        Group by list_orders.id_order, list_orders.kod_klient, dateorder" +
                    "        Order by list_orders.id_order");
            rs = ps.executeQuery();
            while (rs.next()) {
                clients.add("заказ № " + rs.getString(1) + " - " + rs.getInt(2) + " шт. " + db.getNameClient(rs.getInt(3)).trim() + " на " + new SimpleDateFormat("dd.MM.yyyy").format(new SimpleDateFormat("yyyy-MM-dd").parse(rs.getString(4))));
            }
        } catch (Exception e) {
            log.error("Ошибка getClientsPostponed: ", e);
            JOptionPane.showMessageDialog(null, "Ошибка getClientsPostponed: " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);

        }
        return clients;
    }

    public Vector getNameEmploye(String login, String pas) {
        Vector employee = new Vector();
        try {
            ps = conn.prepareStatement("Select employees.id, fio, department from employees, users, dept where login = '" + login + "' " +
                    "  and password = '" + pas + "' and  id_users = users.id and id_dept = dept.id;");
            rs = ps.executeQuery();
            if (rs.next()) {
                employee.add(rs.getInt(1));
                employee.add(rs.getString(2));
                employee.add(rs.getString(3));
            }
        } catch (Exception e) {
            log.error("Ошибка getNameEmploye(): ", e);
            JOptionPane.showMessageDialog(null, "Ошибка getNameEmploye(): " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
        return employee;
    }

    public Vector getEmployeSklad() {
        Vector people = new Vector();
        try {
            ps = conn.prepareStatement("Select employees.id, fio from employees,dept where dept.id = id_dept and id_dept = 4");
            rs = ps.executeQuery();
            while (rs.next()) {
                Vector man = new Vector();
                man.add(rs.getInt(1));
                man.add(rs.getString(2));
                people.add(man);
            }
        } catch (Exception e) {
            log.error("Ошибка getEmployeSklad(): ", e);
            JOptionPane.showMessageDialog(null, "Ошибка getEmployeSklad(): " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
        return people;
    }

    public void updateListOrdersPDB() {
        try {
            ps = conn.prepareStatement("UPDATE list_orders SET state = 4 where dateorder < 'today' and (state = 2 or state = 1)");
            ps.executeUpdate();
        } catch (Exception e) {
            log.error("Ошибка updateListOrdersPDB(): ", e);
            JOptionPane.showMessageDialog(null, "Ошибка updateListOrdersPDB(): " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    public void addImageModel(ItemModel item, int sar, File file) {
        try {
            String query = "insert into izd_images " +
                    "(model, sar, image_thumb) " +
                    "values(?,?,?)";
            ps = conn.prepareStatement(query);
            ps.setString(1, item.getName());
            ps.setInt(2, sar);
            ps.setBinaryStream(3, new FileInputStream(file), (int) file.length());
            ps.executeUpdate();
        } catch (Exception e) {
            log.error("Ошибка метода addImageModel() для модели :[" + item.getName() + "]", e);
            System.err.println("Ошибка метода addImageModel() для модели :[" + item.getName() + "]" + e.getMessage());
        }
        System.out.println("[" + item.getFullName() + "]: Добавлена в модели");
    }

    public void addImageDetail(ItemModel item, String path) {
        try {
            String query = "insert into izd_subimages " +
                    "(name, fullname, imgnumber, submodel, sketch, imgpath) " +
                    "values(?, ?, ?, ?, ?, ?)";
            ps = conn.prepareStatement(query);
            ps.setString(1, item.getName());
            ps.setString(2, item.getFullName());
            ps.setInt(3, item.getImageNumber());
            ps.setInt(4, item.getSubmodel());
            ps.setInt(5, item.getSketch());
            ps.setString(6, path);
            ps.executeUpdate();
            System.out.println("[" + item.getFullName() + "]: Добавлена в изображения");
        } catch (Exception e) {
            log.error("Ошибка метода addImageDetail() для модели :[" + item.getName() + "]", e);
            System.err.println("Ошибка метода addImageDetail() для модели :[" + item.getName() + "]" + e.getMessage());
        }
    }


    /** Метод проверяет наличие данных о модели*/
    public int isImageDetailModelExist(ItemModel item) {
        try {
            // ПРоверим есть ли запись с полным именем модели (с атрибутами)
            String query = "select count(*) from izd_subimages where fullname=?";
            ps = conn.prepareStatement(query);
            ps.setString(1, item.getFullName());
            rs = ps.executeQuery();
            System.out.println("[" + item.getFullName() + "]: Поиск в базе");
            while (rs.next()) {
                if (rs.getInt(1) > 0) {
                    System.out.println("[" + item.getFullName() + "]: Запись существует");
                    return 0;
                } else {
                    // Если записи нету проверяем, есть ли в главной таблице запись о этой модели
                    query = "select count(*) from izd_images where model=?";
                    ps = conn.prepareStatement(query);
                    ps.setString(1, item.getName());
                    rs = ps.executeQuery();
                    while (rs.next()) {
                        if (rs.getInt(1) > 0) {
                            // Если в главной таблице есть инфа о модели
                            return 1;
                        } else {
                            // Если в главной таблице нет инфы о модели
                            return 2;
                        }
                    }
                }
            }
        } catch (Exception e) {
            log.error("Ошибка метода isImageDetailModelExist() для модели :[" + item.getName() + "]", e);
            System.err.println("Ошибка метода isImageDetailModelExist() для модели :[" + item.getName() + "]" + e.getMessage());
            return -1;
        }
        return -1;
    }

    /**Метод обновляет шифр артикула в izd_images до самого последнего из nsi_kld*/
    public void updateSARIzdImage(String model, int sar) {
        try {
            ps = conn.prepareStatement("UPDATE izd_images SET sar = ? where model = ?");
            ps.setInt(1, sar);
            ps.setString(2, model);
            ps.executeUpdate();
            System.out.println("[" + model + "]: Шифр артикула обновлен");
        } catch (Exception e) {
            log.error("Ошибка метода updateSARIzdImage() для модели :[" + model + "]", e);
            System.err.println("Ошибка метода updateSARIzdImage() для модели :[" + model + "]" + e.getMessage());
        }

    }

    /**
     * Метод возвращает BufferedImage соответствующий переданному аргументом модели
     * */

    public BufferedImage getThumbImageByModel(String model) {
        BufferedImage image = null;
        byte[] byteImage = null;

        try {
            ps = conn.prepareStatement("Select image_thumb from izd_images where model=?");
            ps.setString(1, model);
            rs = ps.executeQuery();
            while (rs.next()) {
                image = ImageIO.read(rs.getBinaryStream(1));
                return image;
            }
        } catch (Exception e) {
            log.error("Ошибка метода getThumbImageByModel() для модели :[" + model + "]", e);
            System.err.println("Ошибка метода getThumbImageByModel() для модели :[" + model + "]" + e.getMessage());
        }
        return image;
    }

    /**
     * Ментод для поддержания совместимости справочник старого и нового ядра
     * @param login имя пользователя активной сессии
     * @return объект информации пользователя для совместимости
     */
    public UserCompatibility getCompatibilityUserInformation(String login) {
        UserCompatibility userInformation = new UserCompatibility();
        try {
            ps = conn.prepareStatement("Select users.id as user_Id,users.\"login\",users.\"password\", " +
                    "employees.id as employee_id,fio," +
                    "dept.\"id\" as department_id, department from employees, users, dept where login = ?" +
                    "and  id_users = users.id and id_dept = dept.id");
            ps.setString(1, login);
            rs = ps.executeQuery();
            if (rs.next()) {
                userInformation.setUserId(rs.getInt(1));
                userInformation.setUserLogin(rs.getString(2));
                userInformation.setUserPassword(rs.getString(3));

                userInformation.setEmployeeId(rs.getInt(4));
                userInformation.setEmployeeName(rs.getString(5));

                userInformation.setDepartmentId(rs.getInt(6));
                userInformation.setDepartmentName(rs.getString(7));
            }
        } catch (Exception e) {
            log.error("Ошибка getCompatibilityUserInformation(): ", e);
            JOptionPane.showMessageDialog(null, "Ошибка getCompatibilityUserInformation(): " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
        return userInformation;
    }
}