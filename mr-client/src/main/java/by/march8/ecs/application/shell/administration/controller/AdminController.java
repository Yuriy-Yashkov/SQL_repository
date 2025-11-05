package by.march8.ecs.application.shell.administration.controller;

import by.gomel.freedev.ucframework.uccore.enums.MarchReferencesType;
import by.gomel.freedev.ucframework.uccore.enums.MarchWindowType;
import by.gomel.freedev.ucframework.uccore.enums.RecordOperationType;
import by.gomel.freedev.ucframework.ucdao.DaoFactory;
import by.gomel.freedev.ucframework.ucdao.interfaces.ICommonDao;
import by.gomel.freedev.ucframework.ucdao.interfaces.IGenericDao;
import by.gomel.freedev.ucframework.ucdao.interfaces.IGenericDaoGUI;
import by.gomel.freedev.ucframework.ucdao.model.QueryProperty;
import by.gomel.freedev.ucframework.ucswing.dialog.BaseEditorDialog;
import by.gomel.freedev.ucframework.ucswing.dialog.Dialogs;
import by.march8.ecs.MainController;
import by.march8.ecs.application.shell.administration.uicontrol.AdminControlPanelForm;
import by.march8.ecs.application.shell.administration.uicontrol.UserAccountEditor;
import by.march8.ecs.framework.sdk.reference.Reference;
import by.march8.entities.admin.UserInformation;
import by.march8.entities.admin.UserRole;
import by.march8.entities.admin.VUserInformation;
import by.march8.entities.admin.VUserRole;
import by.march8.entities.company.Employee;
import dept.tools.FormsForm;
import workDB.PDB;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Контроллер панели управления администратора
 */
public class AdminController {
    /**
     * ССылка на главный контроллер приложения
     */
    private final MainController controller;
    /**
     * Коллекция аккаунтов
     */
    private ArrayList<Object> aUserAccount;
    /**
     * Модель таблицы Аккаунтов
     */
    //private final GeneralTableModel tmUserAccount;
    /**
     * Модель таблицы Ролей по аккаунту
     */
    //private final GeneralTableModel tmUserRole;
    /**
     * Коллекция ролей по аккаунту
     */
    private ArrayList<Object> aUserRole;
    /**
     * Форма контрольной панели администратора
     */
    private AdminControlPanelForm adminForm;
    /**
     * Панель редактирования аккаунта
     */
    private UserAccountEditor userAccountEditPane;
    /**
     * Выбранная из таблицы аккаунтовзапись
     */
    private Employee currentEmployee;

    /**
     * Конструктор
     */
    public AdminController(final MainController controller) {
        // Инициализация
        this.controller = controller;
        openAdminControlPanel();
    }

    /**
     * Метод добавляет новый аккаунт
     */
    public void addUserAccount() {
        // Пустая форма-диалог
        final BaseEditorDialog editor = new BaseEditorDialog(controller,
                RecordOperationType.NEW);
        userAccountEditPane.setSourceEntity(null);
        // Для формы устанавливаем панель редактирования
        editor.setEditorPane(userAccountEditPane);

        // Модально показываем форму редактирования
        if (editor.showModal()) {
            //форма закрыта с результатом true
            try {
                // Получаем слой DAO
                DaoFactory factory = DaoFactory.getInstance();
                // Интерфейс CommonDAO для работы с БД
                ICommonDao dao = factory.getCommonDao();
                //Сохраняем запись в БД
                dao.addEntity(userAccountEditPane.getSourceEntity());
            } catch (final SQLException e) {
                e.printStackTrace();
            }
            // Обновляем грид
            updateUserAccountContent();

            UserInformation user = (UserInformation) userAccountEditPane.getSourceEntity();

            PDB pdb = null;
            try {
                pdb = new PDB();
                pdb.addUser(user.getUserLogin(), user.getUserPassword(), user.getNote());
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Ошибка добавлении нового пользователя", "Ошибка!!!", JOptionPane.ERROR_MESSAGE);
            } finally {
                if (pdb != null) {
                    pdb.disConn();
                }
            }
        }
        //Диспозим форму редактирования
        editor.dispose();
    }

    /**
     * Метод редактирует выбранный аккаунт
     */
    public void editUserAccount() {
        VUserInformation selectItem = (VUserInformation) adminForm.getUserAccountViewPort().getSelectedItem();
        if (selectItem == null) {
            return;
        }
        // Пустая форма-диалог
        final BaseEditorDialog editor = new BaseEditorDialog(controller,
                RecordOperationType.NEW);
        // передаем на панель редактирования выбранную из грида запись
        // Получаем запись из базы по ID из вьюхи

        UserInformation item = getUserAccountById(selectItem.getId());
        userAccountEditPane.setSourceEntity(item);
        // Для формц устанавливаем панель редактирования
        editor.setEditorPane(userAccountEditPane);
        // Модально показываем форму редактирования
        String tempName = selectItem.getUserLogin();
        if (editor.showModal()) {
            UserInformation user = (UserInformation) userAccountEditPane.getSourceEntity();

            PDB pdb = null;
            try {
                pdb = new PDB();
                pdb.changeUser(tempName, user.getUserLogin(), user.getUserPassword(), user.getNote());
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Ошибка редактирования учетной записи", "Ошибка!!!", JOptionPane.ERROR_MESSAGE);
            } finally {
                if (pdb != null) {
                    pdb.disConn();
                }
            }
            try {
                // Получаем слой DAO
                DaoFactory factory = DaoFactory.getInstance();
                // Интерфейс CommonDAO для работы с БД
                ICommonDao dao = factory.getCommonDao();
                //Сохраняем запись в БД
                dao.updateEntity(userAccountEditPane.getSourceEntity());
            } catch (final SQLException e) {
                e.printStackTrace();
            }

            // Обновляем грид
            updateUserAccountContent();
        }
        //Диспозим форму редактирования
        editor.dispose();
    }

    /**
     * Метод удаляет аккаунт из БД
     */
    public void deleteUserAccount() {
        VUserInformation selectItem = (VUserInformation) adminForm.getUserAccountViewPort().getSelectedItem();
        if (selectItem == null) {
            return;
        }
        String tempName = selectItem.getUserLogin();

        final int answer = Dialogs.showDeleteDialog(MarchReferencesType.ADM_ACCOUNTS, selectItem);
        if (answer == 0) {
            // Юзер нажал ДА
            try {
                // Получаем слой DAO
                DaoFactory factory = DaoFactory.getInstance();
                // Интерфейс CommonDAO для работы с БД
                ICommonDao dao = factory.getCommonDao();
                // Удаляем запись
                dao.deleteEntity(UserInformation.class, selectItem.getId());

                PDB pdb = null;
                try {
                    pdb = new PDB();
                    pdb.delUser(tempName);
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "Ошибка удаления учетной записи", "Ошибка!!!", JOptionPane.ERROR_MESSAGE);
                } finally {
                    if (pdb != null) {
                        pdb.disConn();
                    }
                }

            } catch (final SQLException e) {
                e.printStackTrace();
            }
            //Обновляем грид
            updateUserAccountContent();
        }
    }

    /**
     * Метод обновляет грид аккаунтов
     */
    public void updateUserAccountContent() {
/*        // Получаем слой DAO
        final DaoFactory factory = DaoFactory.getInstance();
        // Получаем интерфейс DAO
        final ICommonDao dao = factory.getCommonDao();
        //Чистим коллекцию аккаунтов
        aUserAccount.clear();

        try {
            // заполняем коллекцию аккаунтов из DAO
            aUserAccount.addAll(dao.getAllEntity(UserInformation.class));
        } catch (SQLException e) {
            e.printStackTrace();
        }*/


        DaoFactory<VUserInformation> factory = DaoFactory.getInstance();
        IGenericDao<VUserInformation> dao = factory.getGenericDao();
        List<VUserInformation> list;
        aUserAccount.clear();
        try {
            list = dao.getEntityListByNamedQuery(VUserInformation.class, "VUserInformation.findAll", null);
            aUserAccount.addAll(list);
        } catch (SQLException e) {
            MainController.exception(e, "Ошибка получения списка учетных записей");
        }

        // Обновляем грид
        adminForm.getUserAccountViewPort().updateViewPort();
    }

    /**
     * Возвращает ссылку на главный контроллер приложения
     */
    public MainController getMainController() {
        return controller;
    }

    /**
     * Метод инициализирует и отображает формы панели управления администратора
     */
    public void openAdminControlPanel() {
        // Инициализация
        adminForm = new AdminControlPanelForm(this);
        userAccountEditPane = new UserAccountEditor(controller);

        aUserAccount = adminForm.getUserAccountViewPort().getDataModel();
        aUserRole = adminForm.getUserRolesViewPort().getDataModel();

        // Обновление гридов
        updateUserAccountContent();
        // updateUserRoleContent((UserInformation) aUserAccount.get(0));

        JPopupMenu popupMenu = adminForm.getTbUserRole().initPopupMenu();

        popupMenu.add(new JMenuItem(new AbstractAction("Новая роль") {
            public void actionPerformed(ActionEvent e) {
                addUserRoleAsNewRecord();
            }
        }));

        popupMenu.add(new JMenuItem(new AbstractAction("Роль из справочника") {
            public void actionPerformed(ActionEvent e) {
                addUserRole();
            }
        }));

        // Открываем форму
        controller.openInternalFrame(adminForm);

    }


    /**
     * Обновляем грид ролей по аккаунту
     */
    public void updateUserRoleContent() {
        System.out.println("Запрос");
        VUserInformation item = (VUserInformation) adminForm.getUserAccountViewPort().getSelectedItem();
        if (item != null) {

            DaoFactory<VUserRole> factory = DaoFactory.getInstance();
            IGenericDao<VUserRole> dao = factory.getGenericDao();
            List<VUserRole> list;
            aUserRole.clear();

            List<QueryProperty> criteria = new ArrayList<>();
            criteria.add(new QueryProperty("employee", item.getEmployeeId()));

            try {

                list = dao.getEntityListByNamedQuery(VUserRole.class, "VUserRole.findByEmployeeId", criteria);
                aUserRole.addAll(list);
            } catch (SQLException e) {
                MainController.exception(e, "Ошибка получения списка ролей для учетной записи");
            }
            adminForm.getUserRolesViewPort().updateViewPort();

            currentEmployee = getEmployeeFullDataById(item.getEmployeeId());
        }
    }

    public void addUserRole() {

        final Reference ref = new Reference(controller,
                MarchReferencesType.ADM_ROLE,
                MarchWindowType.PICKFRAME);

        // Приведение к нужному нам типу
        final UserRole item = (UserRole) ref.showPickFrame();
        // Если не null - получаем информацию по объекту

        if (item != null) {
            if (!roleIsExist(item)) {
                item.getEmployees().add(currentEmployee);
                currentEmployee.getRoles().add(item);
                final DaoFactory factory = DaoFactory.getInstance();
                // ПОлучаем интерфейс для работы с БД
                final ICommonDao dao = factory.getCommonDao();
                try {
                    // Обновляем запись в БД
                    dao.updateEntity(currentEmployee);
                    System.out.println();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                updateUserRoleContent();
            }
        }
    }

    public void addUserRoleAsNewRecord() {

        final Reference ref = new Reference(controller,
                MarchReferencesType.ADM_ROLE,
                MarchWindowType.PICKFRAME);

        ref.addRecord();

        if (ref.isCanceled()) {
            return;
        }

        // Приведение к нужному нам типу
        final UserRole item = (UserRole) ref.getActiveRecord();
        // Если не null - получаем информацию по объекту
        if (item != null) {
            if (!roleIsExist(item)) {
                item.getEmployees().add(currentEmployee);
                currentEmployee.getRoles().add(item);
                final DaoFactory factory = DaoFactory.getInstance();
                // ПОлучаем интерфейс для работы с БД
                final ICommonDao dao = factory.getCommonDao();
                try {
                    // Обновляем запись в БД
                    dao.updateEntity(currentEmployee);
                    System.out.println();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                updateUserRoleContent();
            }
        }
    }


    public void editUserRole() {
        VUserRole selectItem = (VUserRole) adminForm.getUserRolesViewPort().getSelectedItem();
        if (selectItem == null) {
            return;
        }

        final Reference ref = new Reference(controller,
                MarchReferencesType.ADM_ROLE,
                MarchWindowType.PICKFRAME);

        UserRole oldRole = getUserRoleById(selectItem.getRoleId());
        if (oldRole == null) {
            return;
        }
        // Приведение к нужному нам типу
        final UserRole item = (UserRole) ref.doEditActiveRecord(oldRole);
        // Если не null - получаем информацию по объекту
        if (item != null) {
            for (UserRole role : currentEmployee.getRoles()) {
                if (role.getId() == oldRole.getId()) {
                    currentEmployee.getRoles().remove(oldRole);
                    item.getEmployees().add(currentEmployee);
                    currentEmployee.getRoles().add(item);
                    break;
                }
            }

            final DaoFactory factory = DaoFactory.getInstance();
            // ПОлучаем интерфейс для работы с БД
            final ICommonDao dao = factory.getCommonDao();
            try {
                // Обновляем запись в БД
                dao.updateEntity(currentEmployee);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        updateUserRoleContent();
    }

    public void deleteUserRole() {
        VUserRole selectItem = (VUserRole) adminForm.getUserRolesViewPort().getSelectedItem();
        if (selectItem == null) {
            return;
        }

        final int answer = Dialogs.showDeleteDialog(MarchReferencesType.ADM_ROLE, selectItem);
        if (answer == 0) {

            UserRole oldRole = getUserRoleById(selectItem.getRoleId());
            if (oldRole == null) {
                return;
            }

            for (UserRole role : currentEmployee.getRoles()) {
                if (role.getId() == oldRole.getId()) {
                    System.err.println("Удаление роли");
                    currentEmployee.getRoles().remove(role);
                    break;
                }
            }

            final DaoFactory factory = DaoFactory.getInstance();
            // ПОлучаем интерфейс для работы с БД
            final ICommonDao dao = factory.getCommonDao();
            try {
                dao.updateEntity(currentEmployee);
            } catch (Exception e) {
                e.printStackTrace();
            }
            updateUserRoleContent();
        }
    }

    private boolean roleIsExist(UserRole role) {
        for (Object obj : aUserRole) {
            VUserRole item = (VUserRole) obj;
            if (item.getRoleId() == role.getId()) {
                return true;
            }
        }
        return false;
    }


    public void formRightsControl(int id) {
        UserInformation userInformation = getUserAccountById(id);
        if (userInformation != null) {
            FormsForm dialog = new FormsForm(controller, userInformation);
            dialog.showModal();
        }
    }


    private UserInformation getUserAccountById(int id) {
        DaoFactory<UserInformation> factory = DaoFactory.getInstance();
        IGenericDaoGUI<UserInformation> dao = factory.getGenericDao();
        UserInformation item = null;
        try {
            item = dao.getEntityByIdGUI(UserInformation.class, id);
        } catch (SQLException e) {
            MainController.exception(e, "Ошибка получения учетной записи для идентификатора [" + id + "]");
        }
        return item;
    }

    private UserRole getUserRoleById(int id) {
        DaoFactory<UserRole> factory = DaoFactory.getInstance();
        IGenericDaoGUI<UserRole> dao = factory.getGenericDao();
        UserRole item = null;
        try {
            item = dao.getEntityByIdGUI(UserRole.class, id);
        } catch (SQLException e) {
            MainController.exception(e, "Ошибка получения учетной записи для идентификатора [" + id + "]");
        }
        return item;
    }

    private Employee getEmployeeFullDataById(int id) {
        DaoFactory<Employee> factory = DaoFactory.getInstance();
        IGenericDao<Employee> dao = factory.getGenericDao();
        Employee result = null;
        List<QueryProperty> criteria = new ArrayList<>();
        criteria.add(new QueryProperty("employee", id));
        try {
            result = dao.getEntityByNamedQuery(Employee.class, "Employee.findByEmployeeId", criteria);
        } catch (SQLException e) {
            MainController.exception(e, "Ошибка получения списка ролей для учетной записи");
        }
        return result;
    }
}
