package by.march8.ecs.application.shell.administration.uicontrol;

import by.gomel.freedev.ucframework.uccore.viewport.gridviewport.GridViewPort;
import by.gomel.freedev.ucframework.uccore.viewport.gridviewport.abstracts.TableEventAdapter;
import by.gomel.freedev.ucframework.ucswing.iframe.BaseInternalFrame;
import by.gomel.freedev.ucframework.ucswing.uicontrols.UCToolBar;
import by.march8.ecs.MainController;
import by.march8.ecs.application.shell.administration.controller.AdminController;
import by.march8.entities.admin.VUserInformation;
import by.march8.entities.admin.VUserRole;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Andy on 10.09.2014
 * Форма панели управления администратора
 */
public class AdminControlPanelForm extends BaseInternalFrame {

    private final AdminController controller;
    private final JLabel clEmployee = new JLabel("Сотрудник");
    private final JLabel clDepartment = new JLabel("Подразделения");
    private final JLabel clEmployeePost = new JLabel("Должность");

    private GridViewPort gvUserAccount;
    private GridViewPort gvUserRoles;


    private JLabel lEmployee;
    private JLabel lDepartment;
    private JLabel lEmployeePost;

    private UCToolBar tbUserRole = null;

    public AdminControlPanelForm(final AdminController controller) {
        super(controller.getMainController());
        this.controller = controller;
        setTitle("Панель управления администратора");
        toolBar.setVisibleSearchControls(false);
        this.initFrame();
        this.initEvents();
    }

    /**
     * События на форме, передает индекс выбранной в гриде строки в необходимые методы
     * контроллера панели админитсратора (в основном это редактирование и удаление)
     */
    private void initEvents() {
        // Добавить аккаунт
        toolBar.getBtnNewItem().addActionListener(e -> controller.addUserAccount());


        // Редактировать аккаунт
        toolBar.getBtnEditItem().addActionListener(e ->
                controller.editUserAccount()
        );

        // Удалить аккаунт
        toolBar.getBtnDeleteItem().addActionListener(e ->
                controller.deleteUserAccount()
        );

        // Редактировать аккаунт
        tbUserRole.getBtnEditItem().addActionListener(e ->
                controller.editUserRole()
        );
        // Удалить аккаунт
        tbUserRole.getBtnDeleteItem().addActionListener(e ->
                controller.deleteUserRole()
        );
    }

    public UCToolBar getTbUserRole() {
        return tbUserRole;
    }

    /**
     * Инициализация контролов формы
     */
    private void initFrame() {

        tbUserRole = new UCToolBar();
        tbUserRole.setVisibleSearchControls(false);
        tbUserRole.getBtnViewItem().setVisible(false);
        toolBar.getBtnViewItem().setVisible(false);

        toolBar.getBtnReport().setVisible(false);

        final JButton btnForms = new JButton();
        btnForms.setIcon(new ImageIcon(MainController.getRunPath() + "/Img/cascade.png", "Forms"));
        btnForms.setToolTipText("Управление доступом к формам");
        toolBar.add(btnForms);

        tbUserRole.getBtnReport().setVisible(false);

        panelTop.remove(toolBar);

        panelCenter.setLayout(new BorderLayout());

        Font normalFont = panelContent.getFont();
        normalFont.deriveFont(Font.PLAIN);

        clEmployee.setFont(normalFont);
        clDepartment.setFont(normalFont);
        clEmployeePost.setFont(normalFont);

        //final Dimension minimumSize = new Dimension(300, 0);

        gvUserAccount = new GridViewPort(VUserInformation.class, false);
        gvUserRoles = new GridViewPort(VUserRole.class, false);

/*        tableUserAccount = new JTable();
        new TableFilterHeader(tableUserAccount, AutoChoices.ENABLED);

        tableUserRole = new NiceJTable();

        final JScrollPane spUserInformation = new JScrollPane(tableUserAccount);
        spUserInformation.setMinimumSize(minimumSize);

        final JScrollPane spUserRole = new JScrollPane(tableUserRole);
        spUserRole.setMinimumSize(minimumSize);*/

        lEmployee = new JLabel();
        lDepartment = new JLabel();
        lEmployeePost = new JLabel();

        final JPanel pEmployeeInfo = new JPanel(new MigLayout());

        pEmployeeInfo.add(clEmployee, "wrap");
        pEmployeeInfo.add(lEmployee, "wrap,wrap");
        pEmployeeInfo.add(clDepartment, "wrap");
        pEmployeeInfo.add(lDepartment, "wrap,wrap");
        pEmployeeInfo.add(clEmployeePost, "wrap");
        pEmployeeInfo.add(lEmployeePost);

        final JPanel pUserAccount = new JPanel(new BorderLayout());
        pUserAccount.add(toolBar, BorderLayout.NORTH);
        pUserAccount.add(gvUserAccount, BorderLayout.CENTER);
        pUserAccount.add(pEmployeeInfo, BorderLayout.SOUTH);

        final JPanel pUserRole = new JPanel(new BorderLayout());

        pUserRole.add(tbUserRole, BorderLayout.NORTH);
        pUserRole.add(gvUserRoles, BorderLayout.CENTER);

        final JSplitPane pSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                pUserAccount, pUserRole);
        pSplit.setDividerLocation(300);

        panelCenter.add(pSplit, BorderLayout.CENTER);

        gvUserAccount.setTableEventHandler(new TableEventAdapter() {
            @Override
            public void onSelectChanged(final int rowIndex, final Object object) {

                VUserInformation item = (VUserInformation) gvUserAccount.getSelectedItem();
                if (item != null) {
                    lEmployee.setText(item.getFullEmployeeName());
                    lDepartment.setText(item.getDepartmentName());
                    lEmployeePost.setText(item.getPositionName());

                    controller.updateUserRoleContent();
                }
            }
        });

        btnForms.addActionListener(e -> {
            VUserInformation item = (VUserInformation) gvUserAccount.getSelectedItem();
            if (item != null) {
                controller.formRightsControl(item.getId());
            }
        });

        /*
        tableUserAccount.getSelectionModel().addListSelectionListener(
                event -> {
                    final int viewRow = tableUserAccount.getSelectedRow();
                    if (viewRow >= 0) {
                        tAccountModelRow = tableUserAccount.convertRowIndexToModel(viewRow);
                    }
                });

        tableUserRole.getSelectionModel().addListSelectionListener(
                event -> {
                    final int viewRow = tableUserRole.getSelectedRow();
                    if (viewRow >= 0) {
                        tRoleModelRow = tableUserRole.convertRowIndexToModel(viewRow);
                    }
                });


        tableUserAccount.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(final MouseEvent e) {

            }

            @Override
            public void mousePressed(final MouseEvent e) {
                // Отследили одиночный клик
                if (e.getClickCount() == 2) {
                    //передали в контроллер админа индекс выбранной строки грида
                    controller.editUserAccount(tAccountModelRow);
                }
            }

            @Override
            public void mouseReleased(final MouseEvent e) {

            }

            @Override
            public void mouseEntered(final MouseEvent e) {

            }

            @Override
            public void mouseExited(final MouseEvent e) {

            }
        });

        tableUserRole.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(final MouseEvent e) {

            }

            @Override
            public void mousePressed(final MouseEvent e) {
                // Отследили одиночный клик
                if (e.getClickCount() == 2) {
                    //передали в контроллер админа индекс выбранной строки грида
                    controller.editUserRole(tRoleModelRow);
                }
            }

            @Override
            public void mouseReleased(final MouseEvent e) {

            }

            @Override
            public void mouseEntered(final MouseEvent e) {

            }

            @Override
            public void mouseExited(final MouseEvent e) {

            }
        });


        btnForms.addActionListener(e -> {
            final int viewRow = tableUserAccount.getSelectedRow();
            if (viewRow >= 0) {
                controller.formRightsControl(viewRow);
            }

        });
        */
    }

    public GridViewPort getUserAccountViewPort() {
        return gvUserAccount;
    }


    public GridViewPort getUserRolesViewPort() {
        return gvUserRoles;
    }

}
