package dept;

import by.march8.ecs.Bootstrap;
import by.march8.ecs.MainController;
import by.march8.ecs.framework.common.Configuration;
import by.march8.ecs.framework.common.LogCrutch;
import by.march8.entities.admin.UserInformation;
import common.SingleCopy;

import javax.swing.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;
import java.util.logging.Level;

public class MyReportsModule {

    private static final LogCrutch log = new LogCrutch();
    public static String sDate;
    public static String eDate;
    public static int sYear;
    public static int eYear;
    public static String progPath;
    public static String confPath;
    public static String logPath;
    public static String dbfPlanPath;
    public static String dbfDVIPath;
    public static String UserName;
    public static File confDesignPath;
    Properties prop;
    Configuration configuration;
    private MainController controller;
    private Bootstrap bootStrap;

    public MyReportsModule(MainController mainController) {
        this.bootStrap = mainController.getBootstrap();
        controller = mainController;
        prop = controller.getConfiguration().getProperties();
        configuration = controller.getConfiguration();
        String path = System.getProperty("user.home");
        path += "/.MyReports";
        // проверка на запуск одной копии программы
        SingleCopy sc = new SingleCopy(path);
        sc.start();
        try {
            Thread.sleep(100);
        } catch (InterruptedException ex) {
            java.util.logging.Logger.getLogger(MyReportsModule.class.getName()).log(
                    Level.SEVERE, null, ex);
        }
        if (SingleCopy.f) {
            JOptionPane.showMessageDialog(null,
                    "У вас запущено 3 копии программы, поищите в панеле задач",
                    "Ошибка!!!", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Calendar c = Calendar.getInstance();
        DateFormat df = new java.text.SimpleDateFormat("dd.MM.yyyy");
        eDate = df.format(c.getTime());
        int i = c.get(Calendar.MONTH) + 1;
        String month = new String();
        if (i < 10) {
            month = "0" + i;
        } else {
            month = Integer.toString(i);
        }

        sDate = new String("01." + month + "." + c.get(Calendar.YEAR));
        sYear = Integer.valueOf(df.format(c.getTime()).substring(9));
        eYear = sYear > 0 ? sYear - 1 : 9;


        // -------------Создаём файл настроек-------------------
        try {
            confPath = path + "/";
            File fC = new File(path);
            if (!fC.exists()) {
                new File(path).mkdir();
            }
            File configfile = new File(confPath + "Conf.properties");
            if (!configfile.exists()) {
                prop.setProperty("programm.path", "/nfs/Programs/MyReports20");
                prop.setProperty("programm.login", "");
                prop.setProperty("db.conn.url", "mssql-server");
                prop.setProperty("db.conn.user", "March8");
                prop.setProperty("db.conn.password", "");
                prop.setProperty("dbpostgres.conn.url", "postgres-server");
                prop.setProperty("dbpostgres.conn.user", "client");
                prop.setProperty("dbpostgres.conn.password", "march8");
                prop.setProperty("dbf.plan", "/nfs/ser01_D/PLAN/");
                prop.setProperty("dbf.DVI", "/nfs/ser01_D/DVI/");
                prop.store(new FileOutputStream(configfile), null);
            }
            path += "/Log";
            fC = new File(path);
            if (!fC.exists()) {
                new File(path).mkdir();
            }
            logPath = new String(path + "/MyReports.log");
        } catch (Exception e) {
            //logger.error("Ошибка при попытке создать файл с конфигурациями", e);
            JOptionPane.showMessageDialog(null,
                    "Ошибка при попытке создать файл с конфигурациями.",
                    "Ошибка!!!", JOptionPane.ERROR_MESSAGE);
        }
        // ----------извлекаем путь к программе из конфигурационного
        // файла---------------
        try {
            File configfile = new File(confPath + "Conf.properties");
            prop.load(new FileInputStream(configfile));
            progPath = new String(prop.getProperty("programm.path"));
            dbfPlanPath = new String(prop.getProperty("dbf.plan"));
            try {
                dbfDVIPath = new String(prop.getProperty("dbf.DVI"));
            } catch (Exception ex) {
                configfile = new File(confPath + "Conf.properties");
                prop.setProperty("dbf.plan", "/nfs/ser01_D/PLAN/");
                prop.setProperty("dbf.DVI", "/nfs/ser01_D/DVI/");
                try {
                    prop.store(new FileOutputStream(configfile), null);
                } catch (Exception e) {
                    java.util.logging.Logger
                            .getLogger(MyReportsModule.class.getName()).log(
                                    Level.SEVERE, null, ex);
                }
            }
        } catch (Exception e) {

            System.out.println(e.getMessage());
            //logger.error("Ошибка при попытке загрузить файл с конфигурациями",                              e);
            JOptionPane.showMessageDialog(
                    null,
                    "Ошибка при попытке загрузить файл с конфигурациями. Если программа \n  была запущена первый раз, попробуйте зайти в настройки и указать полный путь к её месторасположению",
                    "Ошибка!!!", JOptionPane.ERROR_MESSAGE);
        }
    }

    public Map<String, JMenuItem> getMenu() {
        return ((MyReportsMenuBar) controller.getMainForm().getJMenuBar()).getTreeMenu();
    }

    public void createMenuOld(MyReportsMenuBar menu) {
        menu.hideMenu();

        UserInformation user = controller.getWorkSession().getUser();
        controller.menuFormInitialisation("dept.MainForm", menu.getTreeMenu());

        try {
            Properties propDesign = new Properties();
            File catalogDesign = new File(confPath + "UserProperties/");
            confDesignPath = new File(confPath + "UserProperties/"
                    + user.getUserLogin() + "DesignForms.properties");

            if (!catalogDesign.isDirectory()) {
                if (catalogDesign.isFile()) {
                    catalogDesign.renameTo(new File(confPath
                            + "UserProperties" + "1"));
                }

                catalogDesign.mkdirs();
            }
            if (!confDesignPath.exists()) {
                propDesign.store(new FileOutputStream(confDesignPath), null);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Ошибка файла настроек пользователя!", "Ошибка",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
    public void createMenu(JMenuBar menu) {

        UserInformation user = controller.getWorkSession().getUser();

        try {
            Properties propDesign = new Properties();
            File catalogDesign = new File(confPath + "UserProperties/");
            confDesignPath = new File(confPath + "UserProperties/"
                    + user.getUserLogin() + "DesignForms.properties");

            if (!catalogDesign.isDirectory()) {
                if (catalogDesign.isFile()) {
                    catalogDesign.renameTo(new File(confPath
                            + "UserProperties" + "1"));
                }

                catalogDesign.mkdirs();
            }
            if (!confDesignPath.exists()) {
                propDesign.store(new FileOutputStream(confDesignPath), null);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Ошибка файла настроек пользователя!", "Ошибка",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}
