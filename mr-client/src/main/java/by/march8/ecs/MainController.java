package by.march8.ecs;

import by.Version;
import by.gomel.freedev.ucframework.uccore.enums.RightEnum;
import by.gomel.freedev.ucframework.uccore.interfaces.ApplicationController;
import by.gomel.freedev.ucframework.uccore.interfaces.Module;
import by.gomel.freedev.ucframework.uccore.utils.SystemUtils;
import by.gomel.freedev.ucframework.ucdao.DaoFactory;
import by.gomel.freedev.ucframework.ucdao.interfaces.IAdministrationDao;
import by.gomel.freedev.ucframework.ucdao.interfaces.ICommonDao;
import by.gomel.freedev.ucframework.ucdao.interfaces.IGenericDao;
import by.gomel.freedev.ucframework.ucdao.model.CriteriaItem;
import by.gomel.freedev.ucframework.ucdao.model.QueryBuilder;
import by.gomel.freedev.ucframework.ucswing.IFrameButton;
import by.gomel.freedev.ucframework.ucswing.dialog.VersionUpdatedDialog;
import by.gomel.freedev.ucframework.ucswing.iframe.BaseInternalFrame;
import by.march8.ecs.application.modules.MainModuleController;
import by.march8.ecs.application.shell.general.events.EventInternalFrame;
import by.march8.ecs.application.shell.general.events.HintActivateMouseListener;
import by.march8.ecs.application.shell.general.events.IFrameButtonActionListener;
import by.march8.ecs.application.shell.general.uicontrol.IFrameListPane;
import by.march8.ecs.application.shell.general.uicontrol.LoginForm;
import by.march8.ecs.application.shell.general.uicontrol.MainForm;
import by.march8.ecs.application.shell.general.uicontrol.StatusBar;
import by.march8.ecs.application.shell.model.SectionMenu;
import by.march8.ecs.application.shell.model.WorkSession;
import by.march8.ecs.framework.common.Configuration;
import by.march8.ecs.framework.common.Personalization;
import by.march8.ecs.framework.common.Settings;
import by.march8.ecs.framework.common.exception.ExceptionHandler;
import by.march8.ecs.framework.helpers.AccountUtils;
import by.march8.entities.admin.FunctionMode;
import by.march8.entities.admin.FunctionalRole;
import by.march8.entities.admin.UserFormRights;
import by.march8.entities.admin.UserInformation;
import by.march8.entities.admin.UserRight;
import by.march8.entities.admin.UserRole;
import by.march8.entities.admin.VersionInfo;
import common.User;
import dept.MyReportsMenuBar;
import dept.MyReportsModule;
import lombok.Getter;
import lombok.Setter;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TimerTask;

/**
 * Главный контроллер программы, отвечает за инициализацию программы,
 * взаимодействие UI компонентов и подключаемых модулей с исполнительной логикой
 * программы.
 *
 * @author andy-windows
 * @since 1.0.1
 */
public class MainController implements ApplicationController {
    @Getter
    private static ExceptionHandler exceptionHandler;

    @Getter
    @Setter
    private static String currentDialogPath;

    @Getter
    public static JFrame parentFrame = null;

    /**
     * Класс конфигурации программы.
     */
    @Getter
    private static Configuration config;

    @Getter
    @Setter
    private static String runPath = "";

    @Getter
    private static Long applicationSize = 0L;

    @Getter
    public static boolean isLoad = false;

    /**
     * Коллекция доступных приложению меню. Заполняется подключаемыми модулями
     *
     * @see Module
     * @see by.gomel.freedev.ucframework.uccore.enums.MarchSection
     */
    private final List<SectionMenu> menuList = new ArrayList<>();

    /**
     * Коллекция ролей текущего пользователя
     */
    private final List<UserRole> userRole = new ArrayList<>();

    private final Set<FunctionalRole> userFunctionalRole = new HashSet<>();

    @Getter
    private final Bootstrap bootstrap;

    /**
     * Главный фрейм программы.
     */
    private MainForm mainForm;

    @Getter
    private StatusBar statusBar;

    @Getter
    private WorkSession workSession = null;

    @Getter
    private Personalization personalization;

    /**
     * События для внутренних фреймов, в момент закрытия фрейма вызывается метод
     * {@link MainController#closeInternalFrame(JInternalFrame)}.
     */
    private EventInternalFrame eventInternalFrame;
    private IFrameButtonActionListener iFrameButtonActionListener;
    private HintActivateMouseListener hintActivateMouseListener;

    @Getter
    private IFrameListPane iFrameListPane;
    private boolean isDebug = false;
    private MyReportsModule myReportsModule = null;

    /**
     * Конструктор контроллера приложения.
     *
     * @param bootstrap первичный загрузчик
     */
    public MainController(Bootstrap bootstrap) {
        runPath = bootstrap.getRunPath();
        this.bootstrap = bootstrap;

        // Контроллер инициализирован для работы в активаном режиме
        preInit();

        // Смотрим, есть ли в аргументах запуска пара uName/upass,
        // и устанавливаем флаг
        boolean isAutoLogin = false;

        // Если найден параметр имени пользователя
        String uName = null;
        // Если найден параметр пароля
        String uPassword = null;

        for (String s : bootstrap.getArguments()) {
            if (s.startsWith("uname")) {
                uName = s;
            }
            if (s.startsWith("upass")) {
                uPassword = s;
            }
        }

        // Если найден параметр пользователя а пароля нет,
        // принимаем пустой пароль
        if (uName != null) {
            String[] user = uName.split("=");
            if (user.length > 1) {
                uName = user[1];
            }
        }

        if (uPassword != null) {
            String[] password = uPassword.split("=");

            if (password.length > 1) {
                uPassword = password[1];
            } else {
                uPassword = "";
            }
        } else {
            if (uName != null) {
                uPassword = "";
            }
        }

        if (uName != null && uPassword != null) {
            isAutoLogin = true;
            System.out.println("Попытка выполнения автоматического входа в программу...[" + uName + "]");
        }

        // Если есть пара автологина и автологин прошел успешно
        if (isAutoLogin && autoLogin(uName, uPassword)) {
            System.out.println("Выполнен автоматический вход в программу...[" + uName + "]");
            initialisation();
        } else {
            if (loginProcessing()) { // Здесь именно ввод логина и пароля в окошко при старте!
                initialisation();
                //postInit();
            } else {
                System.exit(0);
            }
        }

    }

    /**
     * Метод показывает окно исключения пользователю
     *
     * @param e             исключение
     * @param customMessage краткое содержание сообщения
     */
    public static void exception(final Throwable e, String customMessage) {
        if (runPath == null) {
            System.err.println(customMessage);
            e.printStackTrace();
        } else {
            exceptionHandler.showMessage(e, customMessage);
        }
    }

    /**
     * Возвращает текущую конфигурацию програмым.
     *
     * @return активная конфигурация программы
     */
    public static Configuration getConfiguration() {

        return config;
    }

    private boolean autoLogin(String user, String password) {
        DaoFactory factory = DaoFactory.getInstance();
        IAdministrationDao accountDao = factory.getAdministrationDao();
        final UserInformation requestUserInfo = accountDao
                .getUserInformationThread(user.toLowerCase()
                        .trim());

        if (requestUserInfo == null) {
            return false;
        } else {
            if (!AccountUtils.passCheck(password.toCharArray(),
                    requestUserInfo.getUserPassword().toCharArray())) {
                return false;
            } else {
                //loginUser = requestUserInfo;
                workSession = new WorkSession(requestUserInfo);
                if (!isDebug) {
                    config.setLastLoginUser(workSession.getUser().getUserLogin());
                    workSession.setRunPath(config.getProperty(Configuration.RUN_PATH));
                    workSession.logIn();
                }
                return true;
            }
        }
    }

    private void initialisation() {
        myReportsModule = new MyReportsModule(this);
        MyReportsModule.UserName = workSession.getUser().getUserLogin();

        eventInternalFrame = new EventInternalFrame(this);
        //eventWindowListMenuItem = new EventWindowListMenuItem(this);
        hintActivateMouseListener = new HintActivateMouseListener(this);
        iFrameButtonActionListener = new IFrameButtonActionListener(this);
        iFrameListPane = new IFrameListPane(this);
        statusBar = new StatusBar(this);

        mainForm = new MainForm(this);
        parentFrame = mainForm;
        MainModuleController moduleController = new MainModuleController(this);
        System.out.println(moduleController.getModuleAmount());
        MyReportsMenuBar menuBar = new MyReportsMenuBar(this);
//        JMenuBar menuBar = new GeneralMenuBar(this);
        mainForm.setGeneralMenuBar(menuBar);

        workSession.updateCompatiblityUserInfo();
        statusBar.setSession(workSession);
        statusBar.getLableSession().setForeground(Color.green.darker());

        checkUpdateInitializer();

        myReportsModule.createMenuOld(menuBar);
        treeMenuForming();
        personalization = new Personalization(workSession);
        createMenu();
        checkUpdateMessage();
        isLoad = true;
    }

    /**
     * Метод проверяет было ли обновление, и показывалось ли обновление пользователю
     * Если в базе нет сведений о обновлении - сообщение пользователю не будет показано
     */
    private void checkUpdateMessage() {
        String configKey = "current_version";
        String newVersion = Version.VERSION;
        String currentVersion = config.getProperty(configKey);

        if (!currentVersion.equals(newVersion)) {
            DaoFactory<VersionInfo> factoryDocument = DaoFactory.getInstance();
            IGenericDao<VersionInfo> dao = factoryDocument.getGenericDao();

            QueryBuilder queryBuilder = new QueryBuilder(VersionInfo.class);
            queryBuilder.addCriteria(new CriteriaItem(newVersion, "versionName", "LIKE"));

            VersionInfo version = null;
            try {
                version = dao.getEntityByQuery(VersionInfo.class, queryBuilder.getQuery());
            } catch (SQLException e) {
                e.printStackTrace();
            }

            if (version != null) {
                String detail = version.getVersionDetail();

                if (detail == null || detail.trim().isEmpty()) {
                    detail = "Нет данных, вероятнее всего программист много чего наворотил\n" +
                            "и ему лень писать много текста\n " +
                            "или наоборот, изменения в программе не значительные.\nВсе равно этот текст никто не читает (.Y.)";
                }

                VersionUpdatedDialog dialog = new VersionUpdatedDialog(this);
                dialog.setInformation(newVersion, detail);
                dialog.setVisible(true);
            }
            config.setProperty(configKey, newVersion);
        }
    }

    /**
     * Метод "Смена пользователя" (reLogIn)
     */
    public void changeUser() {
        closeAllInternalFrames();
        if (workSession != null && !isDebug) {
                workSession.logOut();
            }


        // Скрываем менюхи нового движка
        for (SectionMenu object : menuList) {
            JMenuItem item = object.getMenu();
            item.setVisible(false);
        }

        MyReportsMenuBar.MENU_INNOVATIONS.setVisible(false);

        // Скрываем менюхи старого движка
        MyReportsMenuBar oldMenu = (MyReportsMenuBar) getMainForm().getJMenuBar();
        oldMenu.hideMenu();

        statusBar.setSession(null);

        // восстановление сеанса после успешного входа
        if (loginProcessing()) {
            workSession.updateCompatiblityUserInfo();
            myReportsModule.createMenuOld(oldMenu);
            createMenu();
            statusBar.setSession(workSession);
        }
    }

    /**
     * Закрывает активную форму и удаляет пункт меню "Список окон"
     *
     * @param internalFrame ссылка на закрываемую формук
     */
    public void closeInternalFrame(final JInternalFrame internalFrame) {
        iFrameListPane.deleteButton(internalFrame);
        mainForm.setTitle(Settings.PROGRAM_NAME);
    }

    /**
     * Метод возвращает ссылку на главную форму программы.
     *
     * @return главная форма программы
     */
    public JFrame getMainForm() {
        return mainForm;
    }

    /**
     * Метод добавляет в контейнер главной формы приложения дочерний фрейм, и
     * делает его активным.
     *
     * @param iFrame the iframe
     */
    public void openInternalFrame(final JInternalFrame iFrame) {
        iFrame.addInternalFrameListener(eventInternalFrame);
        //activeInternalFrame = iFrame;
        IFrameButton button = new IFrameButton(iFrame);
        button.addActionListener(iFrameButtonActionListener);
        mainForm.addWindow(iFrame);
        iFrameListPane.addWindow(button);
        iFrameListPane.resize();

        ((BaseInternalFrame) iFrame).resizeEvent(mainForm.getFramePanelSize()
                .getSize());
    }

    public JDesktopPane getDesktop() {
        return mainForm.getDesktopPane();
    }

    /**
     * Делает активной выбранную из меню "Список окон" форму
     *
     * @param frame ссылка на закрываемую формук
     */
    public void setActiveInternalFrame(final JInternalFrame frame) {
        //activeInternalFrame = frame;
        mainForm.setActiveFrame(frame);
        iFrameListPane.activateButton(frame);
    }

    /**
     * Метод закрывает все открытые окна. Часть алгоритма "Смена пользователя"
     * {@link MainController#changeUser()}
     */
    private void closeAllInternalFrames() {
        iFrameListPane.deleteAllButtons();
        mainForm.setTitle(Settings.PROGRAM_NAME);
    }

    /**
     * Метод загрузки конфигурации программы.
     */
    private void loadConfig() {
        config = bootstrap.getConfiguration();
        //environmentConfig = config;
        applicationSize = SystemUtils.getApplicationFileSize();
        String homeDir = config.getProperty("programm.path");
        if (homeDir != null) {
            // Парсим путь к домашней папке приложения и если в пути указана ссылка на старый MyReports
            // то изменяем на MyReports20(чтоб не менять вручную каждому юзеру)
            int i = homeDir.indexOf("MyReports20");
            // Путь корректный, ничего не правим
            if (i < 0) {
                System.out.println(homeDir);
                // Ищем индекс вхождения MyReports старого образца
                i = homeDir.indexOf("MyReports");

                homeDir = homeDir.substring(0, i + 9) + "20" + homeDir.substring(i + 9);

                System.out.println("Путь к домашнему каталогу скорректирован: " + homeDir);

                File f = new File(homeDir);

                if (f.exists()) {
                    config.setProperty("programm.path", homeDir);
                }
            }

        }

    }

    /**
     * Метод инициализирует форму входа в программу, в случае успешного входа в
     * программу возвращает <code>true</code>.
     *
     * @return true успешный вход в программу
     */
    private boolean loginProcessing() {
        final LoginForm login = new LoginForm(config.getLastLogInUser());
        if (login.activateLoginForm()) {
            isDebug = login.getActiveUser().getUserLogin().equals("debug");
            workSession = new WorkSession(login.getActiveUser());
            if (!isDebug) {
                config.setLastLoginUser(workSession.getUser().getUserLogin());
                workSession.setRunPath(config.getProperty(Configuration.RUN_PATH));
                workSession.logIn();
                User user = User.getInstance();
                MyReportsModule.UserName = user.getName();
            }
            // Персонализатор
            personalization = new Personalization(workSession);
            login.dispose();
            System.out.println("Вход в программу выполнен успешно");
            return true;
        } else {
            System.out.println("Отмена входа в программу");
            return false;
        }
    }

    /**
     * Формирование главного меню после успешного входа в программу/ или смены пользователя
     */
    private void createMenu() {
        // Скрываем все пункты
        for (SectionMenu object : menuList) {
            object.getMenu().setVisible(false);
        }

        userRole.clear();
        userFunctionalRole.clear();
        if (isDebug) {
            userRole.addAll(getAvailableRoles());
        } else {
            userRole.addAll(workSession.getUser().getEmployee().getRoles());
        }
        userRole.parallelStream()
                .filter(Objects::nonNull)
                .flatMap(item -> item.getFunctionalRole().stream()
                        .filter(Objects::nonNull))
                .forEach(functional -> {
                    userFunctionalRole.add(functional);
                    if (functional.getFunctionMode() != null) {
                        enableMenu(functional.getFunctionMode().getNameEng().trim());
                    }
                });
        treeMenuForming();
    }

    private ArrayList<UserRole> getAvailableRoles() {
        ArrayList<UserRole> result = new ArrayList<>();
        UserRole debugRole = new UserRole();
        debugRole.setName("Отладка");
        debugRole.setNote("Супрепользователь");
        DaoFactory factory = DaoFactory.getInstance();
        ICommonDao dao = factory.getCommonDao();

        ArrayList<Object> aModes = null;
        try {
            aModes = dao.getAllEntity(FunctionMode.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (aModes != null) {
            aModes.stream().filter(Objects::nonNull).forEach(item -> {
                FunctionalRole role = new FunctionalRole();
                role.setFunctionMode((FunctionMode) item);
                UserRight right = new UserRight();
                right.setName("ПОЛНЫЙ ДОСТУП");
                role.setRight(right);
                debugRole.getFunctionalRole().add(role);
            });
        }

        result.add(debugRole);
        return result;
    }

    /**
     * Активация пункта меню переданного параметром.
     */
    private void enableMenu(String marchSection) {
        if (marchSection == null || marchSection.isEmpty()) {
            return;
        }

        for (SectionMenu aMenuList : menuList) {
            if (aMenuList != null) {
                try {
                    if (marchSection.equals(aMenuList.getSection().name())) {
                        String tempMenuName = "";
                        String[] menuNames = marchSection.split("_");
                        for (int i = 0; i < menuNames.length; i++) {
                            if ((i < (menuNames.length)) & (i > 0)) {
                                tempMenuName += "_";
                            }
                            tempMenuName += menuNames[i];
                            JMenuItem menu = getMenuItem(tempMenuName);
                            if (menu != null) {
                                menu.setToolTipText(menu.getText());
                                menu.addMouseListener(hintActivateMouseListener);
                                menu.setVisible(true);
                                //menu.getParent().setVisible(true);
                                // Временное решение
                                //MyReportsMenuBar.MENU_INNOVATIONS.setVisible(true);
                            }
                        }
                    }
                } catch (Exception e) {
                    exception(e, "");
                }
            }
        }
    }

    /**
     * Метод компоновки пунктов меню согласно SectionMenu
     */
    private void treeMenuForming() {
        // Сортируем пункты меню по возврастанию ответвлений
        menuList.sort((o1, o2) -> {
            if (o1.getBranchCount() < o2.getBranchCount()) {
                return -1;
            } else if (o1.getBranchCount() > o2.getBranchCount()) {
                return 1;
            }
            return 0;
        });

        menuList.stream()
                .filter(item -> item.getBranchCount() > 1)
                .forEach(item -> createMenuBranch(item.getSection().name()));
    }

    /**
     * Формирование ветвлений меню
     */
    private void createMenuBranch(String section) {
        String[] tmpSplit = section.split("_");
        String[] result = {"", ""};
        StringBuilder tmpString = new StringBuilder();
        for (int i = 0; i < tmpSplit.length - 1; i++) {
            if (i > 0) {
                tmpString.append("_");
            }
            tmpString.append(tmpSplit[i]);
        }
        result[0] = tmpString.toString();
        result[1] = tmpSplit[tmpSplit.length - 1];

        JMenuItem source = getMenuItem(section);
        JMenuItem destination = getMenuItem(result[0]);
        if (source != null) {
            if (destination != null) {
                destination.add(source);
            }
        }
    }

    /**
     * Метод возвращает ссылку на пункт меню по его имени в коллекции menuList
     */
    private JMenuItem getMenuItem(String menuName) {
        for (SectionMenu aMenuList : menuList) {
            if (menuName.equals(aMenuList.getSection().name())) {
                return aMenuList.getMenu();
            }
        }
        return null;
    }

    /**
     * Первичная инициализация программы. Загрузка конфигурации, инициализация
     * обработчика исключений.
     */
    private void preInit() {
        exceptionHandler = new ExceptionHandler(this);
        loadConfig();
    }

    private void checkUpdateInitializer() {
        String timeoutCheckUpdateKey = "timeout_check_update";
        int timeoutCheckUpdate = 300000;

        try {
            timeoutCheckUpdate = Integer.parseInt(config.getProperties().getProperty(timeoutCheckUpdateKey, String.valueOf(timeoutCheckUpdate)));
            config.setProperty(timeoutCheckUpdateKey, String.valueOf(timeoutCheckUpdate));
        } catch (Exception e) {
            exception(e, "Ошибка получения значения задержки при проверке  обновлений.");
        }

        java.util.Timer timer = new java.util.Timer();
        timer.schedule(new TimerTask() {
            public void run() {
                if (!ExceptionHandler.notificationOutOfDate) {
                    if (SystemUtils.applicationIsOutdated()) {
                        timer.cancel();
                        // System.out.println("Проверка версии");
                        //exceptionHandler.showApplicationOutOfDate();
                        JLabel lbl = statusBar.getLableSession();
                        lbl.setForeground(Color.blue);
                        lbl.setText("Доступна новая версия ");
                        lbl.setToolTipText("Перезапустите программу");
                    }
                }
            }
        }, timeoutCheckUpdate, timeoutCheckUpdate);
    }

    /**
     * Метод добавляет элемент структуры SectionMenu в коллекцию меню
     */
    public void addModuleMenu(SectionMenu sectionMenu) {
        menuList.add(sectionMenu);
    }

    public void applicationTerminate() {
        if (!isDebug) {
            if (getWorkSession() != null) {
                getWorkSession().logOut();
            }
        }
        System.exit(0);
    }

    public void showHint(final String toolTipText) {
        statusBar.setHint(toolTipText);
    }

    public HintActivateMouseListener getHintListener() {
        return hintActivateMouseListener;
    }

    /**
     * Метод возвращает права на модуль/справочник, за которым закреплен пункт меню
     */
    public RightEnum getRight(String string) {
        if (isDebug) {
            return RightEnum.WRITE;
        } else {
            for (SectionMenu aMenuList : menuList) {
                if (aMenuList.getMenu() instanceof JMenuItem) {
                    String textMenu = aMenuList.getMenu().getText();
                    if (textMenu.equals(string)) {
                        // Найден пункт меню, вытаскиваем наименование Секции
                        String section = aMenuList.getSection().name();
                        for (FunctionalRole functional : userFunctionalRole) {
                            if (functional != null) {
                                FunctionMode fMode = functional.getFunctionMode();
                                if (fMode != null) {
                                    String mode = fMode.getNameEng();
                                    if (mode.equals(section)) {
                                        String right = functional.getRight().getName();
                                        if (right.equals("ТОЛЬКО ЧТЕНИЕ")) {
                                            return RightEnum.READ;
                                        } else if (right.equals("ПОЛНЫЙ ДОСТУП")) {
                                            return RightEnum.WRITE;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            return RightEnum.READ;
        }
    }

    /**
     * Метод генерирует конечное меню активному пользователю согласно учетной политики
     * приложения для указанной формы
     *
     * @param formName имя класса формы
     * @param menuMap  мапа пунктов меню
     */
    public void menuFormInitialisation(String formName, Map<String, JMenuItem> menuMap) {
        String right = null;
        try {
            UserInformation userInformation = getWorkSession().getUser();
            UserFormRights userRight = userInformation.getFormRightsByFormClass(formName);
            if (userRight != null) {
                right = userRight.getRightsValue();
            }

            if (right != null) {
                String[] aRights = right.split(",");
                for (final String aRight : aRights) {
                    JMenuItem mi = menuMap.get(aRight);
                    if (mi != null) {
                        mi.setVisible(true);
                    }
                }
            }
        } catch (Exception e) {
            MainController.exception(e, "Ошибка инициализации меню");
        }
    }
}
