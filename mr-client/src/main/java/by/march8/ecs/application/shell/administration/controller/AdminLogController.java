package by.march8.ecs.application.shell.administration.controller;

import by.gomel.freedev.ucframework.ucdao.DaoFactory;
import by.gomel.freedev.ucframework.ucdao.interfaces.ICommonDaoThread;
import by.gomel.freedev.ucframework.ucdao.model.CriteriaItem;
import by.gomel.freedev.ucframework.ucdao.model.QueryBuilder;
import by.gomel.freedev.ucframework.ucswing.uicontrols.UCDatePeriodPicker;
import by.gomel.freedev.ucframework.ucswing.uicontrols.UCToolBar;
import by.march8.ecs.MainController;
import by.march8.ecs.application.shell.administration.uicontrol.AdminLogForm;
import by.march8.entities.admin.VAdmLog;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.sql.SQLException;
import java.util.ArrayList;

public class AdminLogController {

    private final MainController controller;
    private ArrayList<Object> data = new ArrayList<>();
    private UCToolBar toolBar;
    private QueryBuilder queryBuilder;
    private AdminLogForm monitorForm;


    public AdminLogController(final MainController controller) {
        this.controller = controller;
        initFrame();
        initEvents();
    }

    private void initFrame() {
        monitorForm = new AdminLogForm(controller, data);
        toolBar = monitorForm.getToolBar();
        controller.openInternalFrame(monitorForm);
        toolBar.getBtnViewItem().setVisible(true);
        UCDatePeriodPicker datePeriod = new UCDatePeriodPicker();
        datePeriod.setActive(true);
        toolBar.getBtnReport().setVisible(false);

        queryBuilder = new QueryBuilder(VAdmLog.class);

        queryBuilder.addCriteria(new CriteriaItem(datePeriod, "logDate", ""));
        toolBar.clearToolBar();

        toolBar.add(new JLabel("Период :"));
        toolBar.add(datePeriod);
        updateContent();

        datePeriod.addOnChangeAction(e -> updateContent());
    }


    private void updateContent() {
        DaoFactory factory = DaoFactory.getInstance();
        ICommonDaoThread dao = factory.getCommonDaoThread();
        data.clear();
        try {
            data.addAll(dao.getAllEntityByStringQueryThread(VAdmLog.class, queryBuilder.getQuery()));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        ((AbstractTableModel) monitorForm.getTView().getModel()).fireTableDataChanged();

        System.out.println(queryBuilder.getQuery());
    }

    private void initEvents() {
        toolBar.getBtnViewItem().addActionListener(e -> updateContent());
    }
}
