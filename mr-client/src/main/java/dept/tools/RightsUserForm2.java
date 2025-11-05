/*
 * RightsUserForm2.java
 *
 * Created on 13.08.2012, 13:42:11
 */
package dept.tools;

import by.gomel.freedev.ucframework.ucdao.DaoFactory;
import by.gomel.freedev.ucframework.ucdao.interfaces.ICommonDao;
import by.gomel.freedev.ucframework.ucswing.dialog.BaseDialog;
import by.march8.ecs.MainController;
import by.march8.ecs.framework.common.LogCrutch;
import by.march8.entities.admin.ApplicationForm;
import by.march8.entities.admin.UserFormRights;
import by.march8.entities.admin.UserInformation;
import common.FormMenu;
import dept.MyReportsMenuBar;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;
import java.util.Set;

//import org.apache.log4j.Logger;

/**
 * @author vova
 */
public class RightsUserForm2 extends BaseDialog {
    // private static final Logger log = new Log().getLoger(RightsUserForm2.class);
    private static final LogCrutch log = new LogCrutch();

    JTable table;
    JPanel mainPanel;
    JScrollPane tableScroll;
    DefaultTableModel dtm;
    Map<String, JMenuItem> menu;
    String user;
    String className;

    int mpx = 5;
    int mpy = 10;
    int idForm;

    private MainController controller;
    private UserInformation currentUser;
    private ApplicationForm currentForm;
    private UserFormRights currentRights;

    public RightsUserForm2(final MainController
                                   mainController, UserInformation user, ApplicationForm applicationForm) {
        super(mainController, new Dimension(420, 500));
        controller = mainController;
        setTitle("Настройка прав доступа для " + user.getUserLogin());
        currentForm = applicationForm;
        currentUser = user;

        this.user = user.getUserLogin();
        this.idForm = applicationForm.getId();

        currentRights = user.getFormRightsByFormId(applicationForm.getId());
        if (currentRights == null) {
            System.out.println("Пустая форма");
            currentRights = new UserFormRights();
        }
        initPanel();
        pContent.add(mainPanel);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(controller.getMainForm());
    }

    @SuppressWarnings("unchecked")
    private void initPanel() {
//------------- Создаём таблицу-------------------------------
        Object colNames[] = {"Доступ", "Меню", "Код"};
        Object[][] data = {};
        String rights;

        dtm = new DefaultTableModel(data, colNames);
        table = new JTable(dtm);
        className = currentForm.getName();
        rights = currentRights.getRightsValue();

        try {

            Object o;
            if (className.equals("dept.MainForm")) {
                menu = ((MyReportsMenuBar) controller.getMainForm().getJMenuBar()).getTreeMenu();
            } else {
                o = Class.forName(className).newInstance();
                ((FormMenu) o).setFormVisible();
                menu = ((FormMenu) o).getMenuMap();
                ((FormMenu) o).disposeForm();
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Error in class RightsUserForm2", e);
        }

        String tab;
        Set<Map.Entry<String, JMenuItem>> set = menu.entrySet();
        for (Map.Entry<String, JMenuItem> me : set) {
            int i = 0;
            StringBuilder str = new StringBuilder(me.getKey());
            tab = "";
            while (true) {
                i = str.indexOf("-", i + 1);
                if (i < 0) break;
                tab += "-|";
            }
            dtm.addRow(new Object[]{Boolean.FALSE, tab + me.getValue().getText(), me.getKey()});
        }

//--------------ставим галочки на доступных пунктах меню---
        String[] aRights;
        aRights = rights.split(",");
        for (final String aRight : aRights) {
            for (int j = 0; j < dtm.getRowCount(); j++) {
                if (dtm.getValueAt(j, 2).toString().equals(aRight)) {
                    dtm.setValueAt(true, j, 0);
                    break;
                }
            }
        }

        TableColumn tc = table.getColumnModel().getColumn(0);
        tc.setCellEditor(table.getDefaultEditor(Boolean.class));
        tc.setCellRenderer(table.getDefaultRenderer(Boolean.class));
        table.getColumnModel().getColumn(0).setPreferredWidth(30);
        table.getColumnModel().getColumn(1).setPreferredWidth(270);
        table.getColumnModel().getColumn(2).setPreferredWidth(0);
        table.getColumnModel().getColumn(2).setMinWidth(0);
        table.getColumnModel().getColumn(2).setMaxWidth(0);

        table.setAutoscrolls(true);
        tableScroll = new JScrollPane(table);

        tableScroll.setBounds(mpx, mpy + 30, 400, 380);

        mainPanel = new JPanel();
        mainPanel.add(tableScroll);

        btnSave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                saveRights();

            }
        });

        btnCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                modalResult = false;
                setVisible(false);
            }
        });
    }

    @SuppressWarnings("all")
    private void saveRights() {
        StringBuilder rightsBuffer = new StringBuilder();

        for (int i = 0; i < dtm.getRowCount(); i++) {
            if (dtm.getValueAt(i, 0).equals(true)) {
                rightsBuffer.append(dtm.getValueAt(i, 2).toString()).append(",");
            }
        }


        currentRights.setForm(currentForm);
        currentRights.setUser(currentUser);
        currentRights.setRightsValue(rightsBuffer.toString());
        currentUser.getFormRights().add(currentRights);

        DaoFactory factory = DaoFactory.getInstance();
        ICommonDao dao = factory.getCommonDao();
        try {
            if (currentRights.getId() == 0) {
                dao.addEntity(currentUser);
            } else {
                dao.updateEntity(currentUser);
            }
            modalResult = true;
            JOptionPane.showMessageDialog(null, "Права пользователя успешно изменены", "Изменено", JOptionPane.INFORMATION_MESSAGE);

            ToolsPDB pdb = null;
            try {
                pdb = new ToolsPDB();
                pdb.setRights(currentUser.getUserLogin(), currentForm.getId(), currentRights.getRightsValue());
            } catch (Exception e) {
                System.out.println("Ошибка сохранения прав в постгрес");
            }

            pdb.disConn();
        } catch (Exception e) {
            modalResult = false;
            System.out.println("Ошибка");
        }
        setVisible(false);
    }
}
