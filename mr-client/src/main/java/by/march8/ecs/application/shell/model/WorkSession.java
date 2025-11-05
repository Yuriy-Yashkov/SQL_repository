package by.march8.ecs.application.shell.model;

import by.gomel.freedev.ucframework.ucdao.DaoFactory;
import by.gomel.freedev.ucframework.ucdao.interfaces.ICommonDao;
import by.gomel.freedev.ucframework.ucdao.interfaces.IGenericDao;
import by.gomel.freedev.ucframework.ucdao.interfaces.INativeDao;
import by.gomel.freedev.ucframework.ucdao.model.QueryProperty;
import by.march8.ecs.MainController;
import by.march8.ecs.framework.common.BackgroundTask;
import by.march8.entities.admin.AdmLog;
import by.march8.entities.admin.FunctionalRole;
import by.march8.entities.admin.UserInformation;
import by.march8.entities.admin.UserProperty;
import by.march8.entities.unknowns.UserCompatibility;
import common.User;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.List;

/**
 * Модель рабочего сеанса пользователя, TimeStamp входа и выхода, информация о
 * пользователе, его права и настройки.
 *
 * @author andy-windows
 * @see by.march8.entities.admin.UserInformation
 */

@SuppressWarnings("all")
public class WorkSession {

    private UserInformation account = null;


    /**
     * IP-адресс компьютера, с которого произведен вход в программу.
     */
    private String accountIp;

    private ArrayList<FunctionalRole> accoutRole;
    private String runPath;

    private AdmLog entityLog;

    private String currentProfileDir;
    private UserCompatibility userCompatibility;

    /**
     * Instantiates a new work session.
     *
     * @param accountItem the user
     */
    public WorkSession(final UserInformation accountItem) {
        this.setAccount(accountItem);
        this.accountIp = getUserIpAdress();
    }

    public String getAccountIp() {
        return accountIp;
    }

    public void setAccountIp(final String accountIp) {
        this.accountIp = accountIp;
    }

    public ArrayList<FunctionalRole> getAccoutRole() {
        return accoutRole;
    }

    public void setAccoutRole(final ArrayList<FunctionalRole> accoutRole) {
        this.accoutRole = accoutRole;
    }

    /**
     * Gets the user.
     *
     * @return the user
     */
    public UserInformation getUser() {
        return account;
    }

    /**
     * Log out.
     */
    public void logOut() {
        class Task extends BackgroundTask {
            public Task(final String messageText) {
                super(messageText);
            }

            @Override
            protected Boolean doInBackground() throws Exception {
                entityLog.setSessionEnd(getCurrentTime().getTime());
                DaoFactory factory = DaoFactory.getInstance();
                ICommonDao dao = factory.getCommonDao();
                try {
                    dao.updateEntity(entityLog);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                return true;
            }
        }
        try {
            Task task = new Task("Завершение сеанса для " + account.getUserLogin());
            task.executeTask();
        } catch (Exception e) {
            MainController.exception(e, "Ошибка завершения сеанса для" + account.getUserLogin());
        }
    }

    public void logIn() {
        class Task extends BackgroundTask {
            public Task(final String messageText) {
                super(messageText);
            }

            @Override
            protected Boolean doInBackground() throws Exception {
                entityLog = new AdmLog();
                entityLog.setUser(account.getId());
                entityLog.setUserIp(getAccountIp());
                entityLog.setSessionStart(getCurrentTime().getTime());
                entityLog.setNote("");

                DaoFactory factory = DaoFactory.getInstance();
                ICommonDao dao = factory.getCommonDao();
                AdmLog logger = null;
                try {
                    logger = (AdmLog) dao.saveEntity(entityLog);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                entityLog.setId(logger.getId());
                return true;
            }
        }
        try {
            Task task = new Task("Начало сеанса для " + account.getUserLogin());
            task.executeTask();
        } catch (Exception e) {
            MainController.exception(e, "Начало завершения сеанса для" + account.getUserLogin());
        }
    }

    @Override
    public String toString() {
        return "Начало сессии :[" + entityLog.getSessionStart() + "]\nПользователь :[" + this.getUser().toString() + "]";
    }

    /**
     * Метод возвращает текущий TimeStamp.
     *
     * @return the current time
     */
    private Calendar getCurrentTime() {
        try {
            final Calendar c = Calendar.getInstance();
            return c;
        } catch (final Exception e) {
            System.out.println("Ошибка получения текущего TimeStamp");
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Gets the user ip adress.
     *
     * @return the user ip adress
     */
    private String getUserIpAdress() {
        try {
            @SuppressWarnings("rawtypes") final Enumeration networkInterfaces = NetworkInterface
                    .getNetworkInterfaces();
            while (networkInterfaces.hasMoreElements()) {
                final NetworkInterface networkInterface = (NetworkInterface) networkInterfaces
                        .nextElement();
                if (networkInterface.getName().equals("eth0")) {
                    @SuppressWarnings("rawtypes") final Enumeration inetAddresses = networkInterface
                            .getInetAddresses();
                    if (inetAddresses.hasMoreElements()) {
                        inetAddresses.nextElement();
                        String s = inetAddresses.nextElement().toString();
                        int ipprefix = s.indexOf('/');
                        if (ipprefix < 0) {
                            return s;
                        } else {
                            return s.substring(ipprefix + 1, s.length());
                        }
                    }
                }
            }
            final InetAddress localHost = InetAddress.getLocalHost();
            String s = localHost.toString();
            int ipprefix = s.indexOf('/');
            if (ipprefix < 0) {
                return s;
            } else {
                return s.substring(ipprefix + 1, s.length());
            }
        } catch (final Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    public String getRunPath() {
        return runPath;
    }

    public void setRunPath(final String runPath) {
        this.runPath = runPath;
    }

    public AdmLog getEntityLog() {
        return entityLog;
    }

    public void setEntityLog(final AdmLog entityLog) {
        this.entityLog = entityLog;
    }

    public UserInformation getAccount() {
        return account;
    }

    /**
     * Sets the user.
     *
     * @param accountItem the new user
     */
    public void setAccount(final UserInformation accountItem) {
        this.account = accountItem;
    }

    public String getCurrentProfileDir() {
        return currentProfileDir;
    }

    public void setCurrentProfileDir(final String currentProfileDir) {
        this.currentProfileDir = currentProfileDir;
    }

    public void updateCompatiblityUserInfo() {
        // для сохранения совместимости версий заполним синглтон user
        // данными из PostgreSQL базы
        userCompatibility = new UserCompatibility();
        DaoFactory factory = DaoFactory.getInstance();
        INativeDao dao = factory.getNativeDao();
        dao.processing(connection -> {
            String query = "Select users.id as user_Id,users.\"login\",users.\"password\", " +
                    "employees.id as employee_id,fio," +
                    "dept.\"id\" as department_id, department from employees, users, dept where login = ? " +
                    "and  id_users = users.id and id_dept = dept.id";
            try {
                PreparedStatement ps = connection.prepareStatement(query);
                ps.setString(1, account.getUserLogin());
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    userCompatibility.setUserId(rs.getInt(1));
                    userCompatibility.setUserLogin(rs.getString(2));
                    userCompatibility.setUserPassword(rs.getString(3));

                    userCompatibility.setEmployeeId(rs.getInt(4));
                    userCompatibility.setEmployeeName(rs.getString(5));

                    userCompatibility.setDepartmentId(rs.getInt(6));
                    userCompatibility.setDepartmentName(rs.getString(7));
                }
            } catch (Exception ex) {
                MainController.exception(ex, "Ошибка получени данных для активного пользователя в режиме совместимости");
            }
            return true;
        }, UserCompatibility.class);

        User user = User.getInstance();
        user.setName(userCompatibility.getUserLogin());
        user.setPassword(userCompatibility.getUserPassword());
        user.setIdEmployee(String.valueOf(userCompatibility.getEmployeeId()));
        user.setFio(userCompatibility.getEmployeeName());
        user.setDepartment(userCompatibility.getDepartmentName());
    }

    public UserCompatibility getUserCompatibility() {
        return userCompatibility;
    }

    public void setUserCompatibility(final UserCompatibility userCompatibility) {
        this.userCompatibility = userCompatibility;
    }

    public String getUserProperty(String key) {
        //Получаем все проперти по пользователю
        DaoFactory<UserProperty> factory = DaoFactory.getInstance();
        IGenericDao<UserProperty> dao = factory.getGenericDao();
        List<QueryProperty> criteria = new ArrayList<>();
        criteria.add(new QueryProperty("userId", getUser().getId()));
        List<UserProperty> list = null;
        try {
            list = dao.getEntityListByNamedQuery(UserProperty.class, "UserProperty.findAllByUserId", criteria);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (list != null) {
            for (UserProperty property : list) {
                System.out.println(property.getKey());
                if (property.getKey().trim().equals(key)) {
                    if (property != null) {
                        return property.getValue().trim();
                    } else {
                        return null;
                    }
                }
            }
        }
        return null;
    }
}
