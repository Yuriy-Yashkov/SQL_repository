package by.march8.ecs.application.modules.production.mode;

import by.gomel.freedev.ucframework.uccore.enums.MarchWindowType;
import by.gomel.freedev.ucframework.uccore.enums.RecordOperationType;
import by.gomel.freedev.ucframework.uccore.enums.RightEnum;
import by.gomel.freedev.ucframework.uccore.modes.abstracts.AbstractFunctionalMode;
import by.gomel.freedev.ucframework.uccore.viewport.frameviewport.FrameViewPort;
import by.gomel.freedev.ucframework.uccore.viewport.frameviewport.interfaces.IActiveFrameRegion;
import by.gomel.freedev.ucframework.uccore.viewport.gridviewport.GridViewPort;
import by.gomel.freedev.ucframework.ucdao.DaoFactory;
import by.gomel.freedev.ucframework.ucdao.interfaces.ICommonDao;
import by.gomel.freedev.ucframework.ucdao.interfaces.IGenericDaoGUI;
import by.gomel.freedev.ucframework.ucdao.model.QueryProperty;
import by.gomel.freedev.ucframework.ucswing.dialog.BaseEditorDialog;
import by.gomel.freedev.ucframework.ucswing.dialog.Dialogs;
import by.gomel.freedev.ucframework.ucswing.uicontrols.EditingPane;
import by.gomel.freedev.ucframework.ucswing.uicontrols.UCDatePeriodPicker;
import by.gomel.freedev.ucframework.ucswing.uicontrols.UCToolBar;
import by.march8.api.utils.DateUtils;
import by.march8.ecs.MainController;
import by.march8.ecs.application.modules.production.editor.RouteSheetEditor;
import by.march8.ecs.application.modules.production.model.RouteSheetReportData;
import by.march8.ecs.application.modules.production.report.RouteSheetOO;
import by.march8.entities.production.RouteSheetItem;
import by.march8.entities.production.RouteSheetSelected;
import by.march8.entities.production.RouteSheetSelectedView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * Режим просмотра отчета по маршрутным листам за период
 * Created by lidashka on 15.10.2018.
 */
public class RouteSheetViewMode extends AbstractFunctionalMode {
    private final JButton btnDocumentTools = new JButton();
    private RightEnum right;
    private UCDatePeriodPicker datePeriodPicker;
    private ArrayList<Object> list;
    private ArrayList<Object> listItems = new ArrayList<>();
    private EditingPane editingPane;
    private JButton btnSearch = new JButton();
    private JButton btnPrint = new JButton();
    private JLabel footer = new JLabel();
    private JButton btnCalculate;
    private JButton btnUpdate;
    private JButton btnImport;
    private JButton btnInsert;
    private GridViewPort gridViewPortItems;
    private double sumKol;
    private RouteSheetItem footerRouteSheetItem = new RouteSheetItem();
    private JPopupMenu popupMenuTools;
    private JMenuItem miChangeDocumentDate;

    public RouteSheetViewMode(MainController mainController) {
        controller = mainController;
        modeName = "Маршрутные листы";
        frameViewPort = new FrameViewPort(mainController, MarchWindowType.INTERNALFRAME);
        frameViewPort.getFrameControl().setTitleFrame(modeName);

        right = RightEnum.WRITE;

        final UCToolBar toolBar = frameViewPort.getFrameRegion().getToolBar();
        toolBar.registerEvents(this);
        toolBar.setRight(right);
        toolBar.getBtnEditItem().setVisible(false);

        // Селектор периода просмотра документов
        datePeriodPicker = new UCDatePeriodPicker();
        datePeriodPicker.setName("datePeriodPicker");
        datePeriodPicker.setDatePickerBegin(DateUtils.getFirstDay(datePeriodPicker.getDatePickerEnd()));
        datePeriodPicker.setEditable(true);

        btnSearch.setIcon(new ImageIcon(MainController.getRunPath() + "/Img/update_data.png", "Обновить данные"));
        btnSearch.setToolTipText("Обновить данные");
        btnSearch.addActionListener(a -> {
            updateContent();
            updateContentItems();
        });

        JRadioButton jrReport1 = new JRadioButton();
        jrReport1.setFont(new java.awt.Font("Dialog", 0, 12));
        jrReport1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jrReport1.setText("по моделям;");
        jrReport1.setActionCommand("RouteSheetModelsRoport.ots");
        jrReport1.setSelected(true);

        JRadioButton jrReport2 = new JRadioButton();
        jrReport2.setFont(new java.awt.Font("Dialog", 0, 12));
        jrReport2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jrReport2.setText("по маршрутным листам;");
        jrReport2.setActionCommand("RouteSheetItemsRoport.ots");

        ButtonGroup buttonGroupReport = new ButtonGroup();
        buttonGroupReport.add(jrReport1);
        buttonGroupReport.add(jrReport2);

        JPanel pPrintPanel = new JPanel(new GridLayout(2, 0, 5, 5));
        pPrintPanel.add(jrReport1);
        pPrintPanel.add(jrReport2);


        btnDocumentTools.setIcon(new ImageIcon(MainController.getRunPath() + "/Img/admin_22.png", "Изменение атрибутов документа"));
        btnDocumentTools.setToolTipText("Изменение атрибутов документа");
        toolBar.add(btnDocumentTools);


        btnPrint.setIcon(new ImageIcon(MainController.getRunPath() + "/Img/printer.png", "Печать документа"));
        btnPrint.setToolTipText("Печать отчета");
        btnPrint.addActionListener(a -> {
            if (listItems.size() > 0) {
                final int answer = JOptionPane
                        .showOptionDialog(null,
                                pPrintPanel,
                                "Сформировать отчет?",
                                JOptionPane.YES_NO_OPTION,
                                JOptionPane.QUESTION_MESSAGE,
                                null,
                                new Object[]{"Продолжить", "Отмена"},
                                "Продолжить");
                if (answer == 0) {
                    printContentItems(buttonGroupReport.getSelection().getActionCommand());
                }
            }
        });

        toolBar.add(new JLabel("     "));
        toolBar.add(btnPrint);
        toolBar.add(new JLabel("     "));
        toolBar.add(datePeriodPicker);
        toolBar.add(btnSearch);
        toolBar.add(new Box(BoxLayout.X_AXIS));
        toolBar.setLayout(new FlowLayout(FlowLayout.LEFT));

        controller.getPersonalization().getPersonalSettings(this, datePeriodPicker);
        datePeriodPicker.setDatePickerEnd(new Date());

        gridViewPort = new GridViewPort(RouteSheetSelectedView.class, false);
        list = gridViewPort.getDataModel();

        gridViewPortItems = new GridViewPort(RouteSheetItem.class, false);
        listItems = gridViewPortItems.getDataModel();

        gridViewPortItems.getTable().getRowSorter().addRowSorterListener(e -> {
            try {
                sumKol = 0;

                for (int index = 0; index < gridViewPortItems.getTable().getRowCount(); index++) {
                    int modelIndex = gridViewPortItems.getTable().convertRowIndexToModel(index);
                    RouteSheetItem item = (RouteSheetItem) listItems.get(modelIndex);
                    sumKol += item.getKol();
                }

                footer.setText("ВСЕГО по размерам - " + sumKol + ";         ");
                //footerRouteSheetItem.setKol(sumKol);
            } catch (Exception e1) {
                JOptionPane.showMessageDialog(null, e1.getMessage(), "Ошибка ", JOptionPane.ERROR_MESSAGE);
            }
        });
        //gridViewPortItems.initializeFooterBar(footerRouteSheetItem);


        popupMenuTools = new JPopupMenu();

        miChangeDocumentDate = new JMenuItem("Изменить цех назначения документа");
        popupMenuTools.add(miChangeDocumentDate);


        btnDocumentTools.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                if (btnDocumentTools.isEnabled()) {
                    popupMenuTools.show(e.getComponent(), 0, e.getComponent().getHeight());
                }
            }
        });

        miChangeDocumentDate.addActionListener(a -> {
            changeDepartmentDocument();
        });

        footer.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);

        JPanel pLeftPanel = new JPanel(new BorderLayout());
        pLeftPanel.setPreferredSize(new Dimension(210, 30));
        pLeftPanel.add(gridViewPort, BorderLayout.CENTER);

        JPanel pRightPanel = new JPanel(new BorderLayout());
        pRightPanel.add(gridViewPortItems, BorderLayout.CENTER);
        pRightPanel.add(footer, BorderLayout.SOUTH);

        final JSplitPane splitPanel = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, pLeftPanel, pRightPanel);
        splitPanel.setResizeWeight(0.1);
        splitPanel.setOneTouchExpandable(true);
        splitPanel.setContinuousLayout(true);

        IActiveFrameRegion frameRegion = frameViewPort.getFrameRegion();
        frameRegion.getCenterContentPanel().add(splitPanel, BorderLayout.CENTER);

        updateContent();

        gridViewPort.primaryInitialization();
        frameViewPort.getFrameControl().showFrame();
    }

    @Override
    public void updateContent() {
        DaoFactory<RouteSheetSelectedView> factory = DaoFactory.getInstance();
        IGenericDaoGUI<RouteSheetSelectedView> dao = factory.getGenericDao();
        List<QueryProperty> criteria = new ArrayList<>();

        criteria.add(new QueryProperty("date_start", datePeriodPicker.getDatePickerBegin()));
        criteria.add(new QueryProperty("date_end", datePeriodPicker.getDatePickerEnd()));

        try {
            list.clear();
            list.addAll(dao.getEntityListByNamedQueryGUI(RouteSheetSelectedView.class, "RouteSheetSelectedView.findByDate", criteria));

        } catch (final Exception e) {
            e.printStackTrace();
        }
        frameViewPort.updateContent();
        gridViewPort.updateViewPort();
    }

    public void updateContentItems() {
        DaoFactory<RouteSheetItem> factory = DaoFactory.getInstance();
        IGenericDaoGUI<RouteSheetItem> dao = factory.getGenericDao();
        List<QueryProperty> criteria = new ArrayList<>();

        criteria.add(new QueryProperty("date_start", datePeriodPicker.getDatePickerBegin()));
        criteria.add(new QueryProperty("date_end", datePeriodPicker.getDatePickerEnd()));

        try {
            listItems.clear();
            listItems.addAll(dao.getEntityListByNamedQueryGUI(RouteSheetItem.class, "RouteSheetItems.findByDate", criteria));

        } catch (final Exception e) {
            e.printStackTrace();
        }

        frameViewPort.updateContent();
        gridViewPortItems.updateViewPort();
    }

    private void clearContentItems() {
        listItems.clear();
        gridViewPortItems.updateViewPort();
    }

    private void printContentItems(String nameReport) {
        RouteSheetReportData reportData = new RouteSheetReportData();
        List<RouteSheetItem> reportList = new ArrayList<>();

        for (int index = 0; index < gridViewPortItems.getTable().getRowCount(); index++) {
            int modelIndex = gridViewPortItems.getTable().convertRowIndexToModel(index);
            RouteSheetItem item = (RouteSheetItem) listItems.get(modelIndex);
            reportList.add(item);
        }

        reportData.setData((ArrayList<RouteSheetItem>) reportList);

        try {
            RouteSheetOO oo = new RouteSheetOO(
                    modeName
                            + " за период: "
                            + DateUtils.getNormalDateFormat(datePeriodPicker.getDatePickerBegin())
                            + "-"
                            + DateUtils.getNormalDateFormat(datePeriodPicker.getDatePickerEnd()),
                    reportData);
            oo.createReport(nameReport);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Ошибка ", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void addRecord() {
        // Создаем пустую форму диалога
        BaseEditorDialog editor = new BaseEditorDialog(controller,
                RecordOperationType.NEW);

        List<Integer> idList = new ArrayList<>();

        for (Object object : list) {
            RouteSheetSelectedView routeSheetSelected = (RouteSheetSelectedView) object;
            idList.add(routeSheetSelected.getId());
        }

        editingPane = new RouteSheetEditor(
                controller,
                datePeriodPicker.getDatePickerBegin(),
                datePeriodPicker.getDatePickerEnd(),
                idList);

        editor.setParentTitle("маршрутный лист");

        editingPane.setSourceEntity(null);
        // Для созданного пустого диалога устанавливаем панель редактирования
        editor.setEditorPane(editingPane);
        // Модально показываем форму и ожидаем закрытия
        if (editor.showModal()) {
            // Форма закрыта со значением true
            // Получаем DAO слой
            final DaoFactory factory = DaoFactory.getInstance();
            // ПОлучаем интерфейс для работы с БД
            final ICommonDao dao = factory.getCommonDao();
            try {
                // Сохраняем сущность в БД
                Set<RouteSheetSelected> routeSheetSelected = (Set<RouteSheetSelected>) editingPane.getSourceEntity();

                if (routeSheetSelected != null) {
                    for (RouteSheetSelected sheet : routeSheetSelected)
                        dao.saveEntity(sheet);
                }

            } catch (final SQLException e) {
                e.printStackTrace();
            }
            // ПОсле сохранения данных обновляем содержимое грида
            updateContent();
            clearContentItems();
        }
    }

    @Override
    public void editRecord() {

    }

    @Override
    public void deleteRecord() {
        final int answer = Dialogs.showDeleteDialog(gridViewPort.getSelectedItem());
        if (answer == 0) {
            // Юзер нажал ДА
            // Получаем DAO слой
            final DaoFactory factory = DaoFactory.getInstance();
            // Получаем интерфейс для работы с БД
            final ICommonDao dao = factory.getCommonDao();
            // Удаляем запись из БД
            try {
                // Получаем ID записи, и так как мы не знаем с какой именно
                // сущностью мы работам
                // Узнаем ID приведя объект к базовой сущности
                final RouteSheetSelectedView item = (RouteSheetSelectedView) gridViewPort.getSelectedItem();
                // Пометка на удаление в гриде
                gridViewPort.setDeletedObject(gridViewPort.getSelectedItem());
                // Запрос к DAO на удаление объекта
                dao.deleteEntity(RouteSheetSelected.class, item.getIdMarh2());
            } catch (final Exception e) {
                e.printStackTrace();
            }
            // Обновляем содержимое грида
            updateContent();
            clearContentItems();
        }
    }


    private void changeDepartmentDocument() {

        RouteSheetToolsDialog dialog = new RouteSheetToolsDialog(controller);
        boolean d = dialog.showDialog();
        if (d) {

            updateContent();
        }
    }
}
