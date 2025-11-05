package by.march8.ecs.application.shell.administration.controlpane;

import by.gomel.freedev.ucframework.uccore.modes.interfaces.IEditableModule;
import by.gomel.freedev.ucframework.uccore.modes.interfaces.IReference;
import by.gomel.freedev.ucframework.uccore.viewport.frameviewport.FrameViewPort;
import by.gomel.freedev.ucframework.uccore.viewport.frameviewport.abstracts.ControlPane;
import by.gomel.freedev.ucframework.ucdao.DaoFactory;
import by.gomel.freedev.ucframework.ucdao.interfaces.ICommonDao;
import by.gomel.freedev.ucframework.ucswing.uicontrols.UCToolBar;
import by.march8.ecs.MainController;
import by.march8.entities.admin.FunctionalRole;
import by.march8.entities.admin.UserRole;
import by.march8.entities.company.Employee;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.Set;

public class AdmRoleControl extends ControlPane {
    private IReference references;
    private FrameViewPort form;


    @Override
    public void beforeEmbedding(final FrameViewPort frameViewPort) {
        references = frameViewPort.getReference();
        form = frameViewPort;

        UCToolBar toolBar = form.getFrameRegion().getToolBar();
        final JButton btnCopyRole = new JButton();
        btnCopyRole.setIcon(new ImageIcon(MainController.getRunPath() + "/Img/copy24.png", "Копировать"));
        btnCopyRole.setToolTipText("Новая роль на основании выбранной");
        btnCopyRole.addMouseListener(form.getController().getHintListener());

        JPanel searchPanel = form.getFrameRegion().getToolBar().getSearchPanel();
        toolBar.remove(searchPanel);
        toolBar.add(btnCopyRole);
        toolBar.addSeparator();
        toolBar.add(searchPanel);

        btnCopyRole.addActionListener(e -> createRoleBySelectRole());
    }

    @Override
    public void afterEmbedding() {
        //Удалим обработчик события удаления записи
        JButton btnDeleteItem = form.getFrameRegion().getToolBar().getBtnDeleteItem();
        for (ActionListener act : btnDeleteItem.getActionListeners()) {
            btnDeleteItem.removeActionListener(act);
        }
        btnDeleteItem.addActionListener(e -> {
            // Запрос для юзера на удаление записи
            final int answer = JOptionPane.showConfirmDialog(null,
                    "Удалить запись ?", "Удаление записи",
                    JOptionPane.YES_NO_OPTION);
            if (answer == 0) {
                deleteRole();
            }

        });
    }


    private UserRole getSelectedRole() {
        return (UserRole) form.getGridViewPort().getSelectedItem();
    }

    private void deleteRole() {
        UserRole selectedRole = getSelectedRole();
        if (selectedRole == null) {
            return;
        }

        Set<Employee> employees = selectedRole.getEmployees();

        DaoFactory factory = DaoFactory.getInstance();
        ICommonDao dao = factory.getCommonDao();


        for (Employee e : employees) {
            e.getRoles().remove(selectedRole);
            try {
                dao.updateEntity(e);
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        }
        try {
            form.getGridViewPort().setDeletedObject(selectedRole);
            dao.deleteEntity(UserRole.class, selectedRole.getId());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        IEditableModule module = references.getEditableModule();
        module.updateContent();
    }

    private void createRoleBySelectRole() {
        UserRole selectedRole = getSelectedRole();
        if (selectedRole == null) {
            return;
        }
        String s = (String) JOptionPane.showInputDialog(
                null,
                "Создается новая роль на основании роли [" + selectedRole.getName() + "]\nВведите имя новой роли.",
                "Новая роль",
                JOptionPane.PLAIN_MESSAGE,
                null,
                null,
                "Копия " + selectedRole.getName());


        if ((s != null) && (s.trim().length() > 0)) {

            UserRole newRole = new UserRole();
            newRole.setName(s);
            newRole.setNote(selectedRole.getNote());
            Set<FunctionalRole> functionalRoles = selectedRole.getFunctionalRole();

            for (FunctionalRole fRole : functionalRoles) {
                FunctionalRole newFunctionalRole = new FunctionalRole(fRole);
                newFunctionalRole.setRole(newRole);
                newRole.getFunctionalRole().add(newFunctionalRole);
            }

            DaoFactory factory = DaoFactory.getInstance();
            ICommonDao dao = factory.getCommonDao();
            try {
                Object o = dao.saveEntity(newRole);
                form.getGridViewPort().setUpdatedObject(o);
            } catch (SQLException e) {
                e.printStackTrace();
            }

            IEditableModule module = references.getEditableModule();
            module.updateContent();
        }
    }


}
