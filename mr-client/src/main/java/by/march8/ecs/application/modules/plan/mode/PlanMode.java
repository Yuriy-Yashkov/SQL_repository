package by.march8.ecs.application.modules.plan.mode;

import by.gomel.freedev.ucframework.uccore.enums.MarchPlanType;
import by.gomel.freedev.ucframework.uccore.enums.MarchWindowType;
import by.gomel.freedev.ucframework.uccore.enums.RecordOperationType;
import by.gomel.freedev.ucframework.uccore.modes.abstracts.AbstractFunctionalMode;
import by.gomel.freedev.ucframework.uccore.report.ReportManager;
import by.gomel.freedev.ucframework.uccore.report.api.openoffice.DocumentType;
import by.gomel.freedev.ucframework.uccore.viewport.frameviewport.FrameViewPort;
import by.gomel.freedev.ucframework.uccore.viewport.gridviewport.GridViewPort;
import by.gomel.freedev.ucframework.uccore.viewport.gridviewport.abstracts.TableEventAdapter;
import by.gomel.freedev.ucframework.uccore.viewport.gridviewport.branch.NiceJTable;
import by.gomel.freedev.ucframework.uccore.viewport.gridviewport.interfaces.IGridToolTipEvent;
import by.gomel.freedev.ucframework.ucdao.DaoFactory;
import by.gomel.freedev.ucframework.ucdao.interfaces.ICommonDao;
import by.gomel.freedev.ucframework.ucdao.interfaces.ICommonDaoThread;
import by.gomel.freedev.ucframework.ucdao.model.CriteriaItem;
import by.gomel.freedev.ucframework.ucdao.model.QueryBuilder;
import by.gomel.freedev.ucframework.ucswing.dialog.BaseEditorDialog;
import by.gomel.freedev.ucframework.ucswing.dialog.Dialogs;
import by.gomel.freedev.ucframework.ucswing.uicontrols.EditingPane;
import by.gomel.freedev.ucframework.ucswing.uicontrols.UCToolBar;
import by.march8.ecs.MainController;
import by.march8.ecs.application.modules.plan.editors.PlanChemEditor;
import by.march8.ecs.application.modules.plan.editors.PlanGeneralEditor;
import by.march8.ecs.application.modules.plan.editors.PlanMarketingEditor;
import by.march8.ecs.application.modules.plan.editors.PlanNciEditor;
import by.march8.ecs.application.modules.plan.editors.PlanPdoEditor;
import by.march8.ecs.application.modules.plan.editors.PlanTbEditor;
import by.march8.ecs.application.modules.plan.editors.PlanTsoEditor;
import by.march8.ecs.application.modules.plan.render.PlanFontCellRenderer;
import by.march8.ecs.application.modules.plan.render.PlanPictureCellRender;
import by.march8.ecs.application.modules.plan.report.PlanReport;
import by.march8.entities.plan.PlanItem;
import by.march8.entities.plan.TypeItem;
import scriptdb.MainFrame;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * @author Andy 22.03.2016.
 */
public class PlanMode extends AbstractFunctionalMode implements IGridToolTipEvent {

    private final ArrayList<Object> data;

    private JComboBox<String> cbMonthName = new JComboBox<String>();
    private JComboBox<String> cbYearNumber = new JComboBox<String>();

    private JButton btnPrint;
    private JButton btnDetail;
    private JButton btnRefresh;
    private JButton btnUpdateContent;

    private JToggleButton btnSummary;
    private EditingPane editingPane;
    private JPanel pFooter;
    private JLabel lblFooterText = new JLabel("ИНФО");

    public PlanMode(MainController mainController) {
        controller = mainController;
        modeName = "Выполнение плана";
        frameViewPort = new FrameViewPort(mainController, MarchWindowType.INTERNALFRAME);
        frameViewPort.getFrameControl().setTitleFrame(modeName);

        final UCToolBar toolBar = frameViewPort.getFrameRegion().getToolBar();

        toolBar.setLayout(new FlowLayout(FlowLayout.LEFT));
        //toolBar.setPreferredSize(new Dimension(0,47));
        toolBar.addSeparator();
        toolBar.add(new JLabel("Просмотр за:"));
        cbMonthName.setPreferredSize(new Dimension(120, 20));
        cbYearNumber.setPreferredSize(new Dimension(100, 20));

        toolBar.add(cbMonthName);
        toolBar.add(cbYearNumber);
        //toolBar.addSeparator();

        btnPrint = new JButton();
        btnPrint.setIcon(new ImageIcon(MainController.getRunPath() + "/Img/printer.png", "Печать документа"));
        btnPrint.setToolTipText("Печать документа");

        btnDetail = new JButton();
        btnDetail.setIcon(new ImageIcon(MainController.getRunPath() + "/Img/view_detail.png", "Детализация плана"));
        btnDetail.setToolTipText("Детализация плана");

        btnSummary = new JToggleButton();
        btnSummary.setIcon(new ImageIcon(MainController.getRunPath() + "/Img/sum_24.png", "Подведение итогов"));
        btnSummary.setToolTipText("Подведение итогов");

        btnUpdateContent = new JButton();
        btnUpdateContent.setIcon(new ImageIcon(MainController.getRunPath() + "/Img/update_24.png", "Обновление данных"));
        btnUpdateContent.setToolTipText("Обновить таблицу");

        btnRefresh = new JButton();
        btnRefresh.setIcon(new ImageIcon(MainController.getRunPath() + "/Img/database_refresh_24.png", "Обновление данных"));
        btnRefresh.setToolTipText("Обновление данных");

        toolBar.add(btnUpdateContent);
        toolBar.add(btnSummary);
        toolBar.addSeparator();

        toolBar.add(btnDetail);
        toolBar.add(btnPrint);
        toolBar.addSeparator();
        toolBar.add(btnRefresh);

        toolBar.getBtnDeleteItem().setEnabled(false);
        toolBar.getBtnNewItem().setEnabled(false);

        fillingComboBox();

        // Инициализация грида
        gridViewPort = new GridViewPort(PlanItem.class, false);
        // Получаем ссылку на модель данных грида
        data = gridViewPort.getDataModel();
        frameViewPort.setGridViewPort(gridViewPort);
        initGridViewPort(gridViewPort.getTable());

        // Нижняя панель
        pFooter = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pFooter.setPreferredSize(new Dimension(100, 200));
        pFooter.add(lblFooterText);


        frameViewPort.getFrameRegion().getBottomContentPanel().setLayout(new BorderLayout());
        frameViewPort.getFrameRegion().getBottomContentPanel().add(pFooter, BorderLayout.SOUTH);


        pFooter.setVisible(false);

        generateInstance(MarchPlanType.PLAN_ANALYSIS);
        initEvents();
        updateContent();

        toolBar.getBtnDeleteItem().setVisible(false);
        toolBar.getBtnNewItem().setVisible(false);

        if (editingPane == null) {
            toolBar.getBtnEditItem().setVisible(false);
        }

        gridViewPort.primaryInitialization();
        frameViewPort.getFrameControl().showFrame();
    }

    private void initGridViewPort(NiceJTable table) {

        //ToolTipManager.sharedInstance().registerComponent(gridViewPort.getTable());
        // Устанавливаем кастомный рендер гриду
        DefaultTableCellRenderer renderer = new PlanPictureCellRender();
        table.getColumnModel().getColumn(4).setCellRenderer(renderer);
        table.setRowHeight(50);

        gridViewPort.getTable().setToolTipHandler(this);
        for (int i = 0; i < table.getColumnCount(); i++) {
            switch (i) {
                case 16:
                    table.getColumnModel().getColumn(i).setCellRenderer(new PlanFontCellRenderer(new Font("Tahoma", Font.BOLD, 14), Color.RED));
                    break;
                case 17:
                    table.getColumnModel().getColumn(i).setCellRenderer(new PlanFontCellRenderer(new Font("Tahoma", Font.BOLD, 14), Color.RED));
                    break;
                case 18:
                    table.getColumnModel().getColumn(i).setCellRenderer(new PlanFontCellRenderer(new Font("Tahoma", Font.BOLD, 14), Color.BLUE));
                    break;
                case 4:
                    break;
                default:
                    table.getColumnModel().getColumn(i).setCellRenderer(new PlanFontCellRenderer(new Font("Tahoma", Font.BOLD, 14), Color.BLACK));
                    break;
            }
        }

        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    private void initEvents() {
        Calendar c = Calendar.getInstance();
        cbMonthName.setSelectedIndex(c.get(Calendar.MONTH));

        for (int i = 0; i < cbYearNumber.getItemCount(); i++) {
            if (cbYearNumber.getItemAt(i).equals(String.valueOf(c.get(Calendar.YEAR)))) {
                cbYearNumber.setSelectedIndex(i);
                break;
            }
        }

        cbMonthName.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                updateContent();
            }
        });
        cbYearNumber.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                updateContent();
            }
        });

        final UCToolBar toolBar = frameViewPort.getFrameRegion().getToolBar();
        toolBar.registerEvents(this);

        gridViewPort.setTableEventHandler(new TableEventAdapter() {
            @Override
            public void onDoubleClick(final int rowIndex, int columnIndex, final Object object) {
                showDetail();
            }
        });

        btnSummary.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                createFooterText();
            }
        });

        btnDetail.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                showDetail();
            }
        });

        btnPrint.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                createReport();
            }
        });

        btnRefresh.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                refreshDate();
            }
        });

        btnUpdateContent.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                {
                    if (btnSummary.isSelected()) {
                        // Эмулируем нажатие что бы скрыть панель итогов
                        btnSummary.doClick();
                    }
                    updateContent();
                }
            }
        });

    }

    private void refreshDate() {
        MainFrame scriptDb = new MainFrame();
        scriptDb.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
    }

    private void createReport() {

        if (Dialogs.showQuestionDialog("Сформировать документ по выполнению плана", "Формирование отчета") != 0) {
            return;
        }

        updateContent();

        if (data.size() < 1) {
            return;
        }

        ReportManager manager = new ReportManager(controller, new PlanReport(controller, MarchPlanType.PLAN_REPORT, data));
        try {
            // Создаем документ
            manager.createDocument();
            // Диалог на сохранение документа
            String file = manager.saveDocumentDialog(DocumentType.DOCUMENT_ODS);

            // Диалог на печать документа
            // manager.printDocumentDialog();
            // Закрываем документ и отсоединяемся от сервера OO
            manager.closeDocument();
            // Открываем сохраненный документ
            manager.openDocument(file);

        } catch (Exception ex) {
            MainController.exception(ex, "Ошибка формирования отчета");
        }
    }

    @Override
    public void addRecord() {

    }

    @Override
    public void editRecord() {
        if (editingPane == null) {
            System.out.println("Пустой");
            return;
        }
        // Создаем пустую форму диалога
        final BaseEditorDialog editor = new BaseEditorDialog(controller,
                RecordOperationType.EDIT);
        // Передаем на панель редактирования объект редактирования
        PlanItem item = (PlanItem) gridViewPort.getSelectedItem();
        if (item != null) {
            editor.setParentTitle(item.getAssortmentName());
        }

        editingPane.setSourceEntity(gridViewPort.getSelectedItem());
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
                // Обновляем запись в БД
                dao.updateEntity(editingPane.getSourceEntity());
            } catch (final SQLException e) {
                e.printStackTrace();
            }
            // ПОсле сохранения данных обновляем содержимое грида
            updateContent();
        }
        // диспозим окно диалога
        editor.dispose();
    }


    @Override
    public void deleteRecord() {

    }

    private void showDetail() {
        PlanItem item = (PlanItem) gridViewPort.getSelectedItem();
        if (item != null) {
            new PlanDetailMode(controller, item);
        }
    }


    /**
     * Метод устанавливает для текущего типа справочника его модель таблицы и
     * его панель редактирования
     */
    private void generateInstance(final MarchPlanType plan) {
        //String query;
        switch (plan) {
            case PLAN_ANALYSIS:
                //query = "SELECT c FROM PlanItem c WHERE c.planid.id = :first_id";
                //filterPane = new PlanFilter(this);

                String editorProperty = controller.getWorkSession().getUserProperty("PlanModeEditor");

                if (editorProperty == null) {
                    editorProperty = "";
                }

                if (editorProperty.equals("")) {
                    btnRefresh.setVisible(false);
                } else {
                    btnRefresh.setVisible(true);
                }

                /**
                 * Отдел Маркетинга
                 */
                if (editorProperty.equals("om")) {
                    editingPane = new PlanMarketingEditor(controller);
                }

                /**
                 * Отдел ПДО (подключен)
                 */
                if (editorProperty.equals("pdo")) {
                    editingPane = new PlanPdoEditor(controller);
                }

                /**
                 * Отдел Химической Лаборатории  (подключен)
                 */
                if (editorProperty.equals("himlab")) {
                    editingPane = new PlanChemEditor(controller);
                }

                /**
                 * Отдел ТБШП  (подключен) добавить редактор для ПДО
                 */
                if (editorProperty.equals("tbshp")) {
                    editingPane = new PlanTbEditor(controller);
                }

                /**
                 * Отдел НЦИ
                 */
                if (editorProperty.equals("nci")) {
                    editingPane = new PlanNciEditor(controller);
                }

                /**
                 * Отдел НЦИ
                 */
                if (editorProperty.equals("tso")) {
                    editingPane = new PlanTsoEditor(controller);
                }

                /**
                 * НАЧАЛЬСТВО ( только чтение)
                 */
                if (editorProperty.equals("general")) {
                    editingPane = new PlanGeneralEditor(controller);
                }
                break;
/*            case PLAN_DETALIZATION:
                query = "SELECT c FROM PlanDetalization c WHERE c.planItem = "+idPlanItem;
                filterPane = new PlanDetalFilter(controller, this,
                        MarchPlanType.PLAN_DETALIZATION, query);
                break;*/
            default:
                break;
        }
    }


    @Override
    public void updateContent() {

        QueryBuilder query = new QueryBuilder(PlanItem.class);
        int monthIndex = cbMonthName.getSelectedIndex();
        int yearIndex = cbYearNumber.getSelectedIndex();
        ++monthIndex;
        int index = monthIndex;

        if (yearIndex > 0) {
            index = index + ((yearIndex) * 12);
        }


        query.addCriteria(new CriteriaItem(index, "planId", "="));
        System.out.println(query.getQuery());

        DaoFactory factory = DaoFactory.getInstance();
        ICommonDaoThread dao = factory.getCommonDaoThread();

        data.clear();

        try {
            data.addAll(dao.getAllEntityByStringQueryThread(PlanItem.class, query.getQuery() + "order by assortment_name"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        gridViewPort.updateViewPort();

        final TableColumnModel columnModel = gridViewPort.getTable().getColumnModel();

        columnModel.getColumn(9).setMaxWidth(0);
        columnModel.getColumn(9).setMinWidth(0);
        columnModel.getColumn(9).setPreferredWidth(0);

        columnModel.getColumn(4).setMaxWidth(0);
        columnModel.getColumn(4).setMinWidth(0);
        columnModel.getColumn(4).setPreferredWidth(0);

        columnModel.getColumn(20).setMaxWidth(0);
        columnModel.getColumn(20).setMinWidth(0);
        columnModel.getColumn(20).setPreferredWidth(0);
    }

    @Override
    public String onToolTipActivated(final Object object, final int row, final int column, final int rowModel) {

        if (column == 4) {
            if (object == null) {
                return "Нет изображения";
            }

            File temp;
            try {
                temp = File.createTempFile("jtrtr", ".jpg");
            } catch (IOException e) {
                return "";
            }
            BufferedImage img;
            try {
                img = ImageIO.read(new ByteArrayInputStream((byte[]) object));
            } catch (IOException e) {
                return "";
            }
            try {
                ImageIO.write(img, "jpg", temp);
            } catch (IOException e) {
                return "";
            }

            URL fileURL;
            try {
                fileURL = temp.toURI().toURL();
            } catch (MalformedURLException e) {
                return "";
            }

            return "<html><body>"
                    + "<img src='"
                    + fileURL
                    + "' width=250 height=250> ";
        }
        return "";
    }

    @SuppressWarnings("unchecked")
    private void fillingComboBox() {
        ArrayList<String> array = new ArrayList<String>();
        array.add("Январь");
        array.add("Февраль");
        array.add("Март");
        array.add("Апрель");
        array.add("Май");
        array.add("Июнь");
        array.add("Июль");
        array.add("Август");
        array.add("Сентябрь");
        array.add("Октябрь");
        array.add("Ноябрь");
        array.add("Декабрь");


        cbMonthName.setModel(new DefaultComboBoxModel(array.toArray()));
        cbMonthName.setSelectedIndex(0);

        array = new ArrayList<String>();
        array.add("2015");
        array.add("2016");
        array.add("2017");
        array.add("2018");
        array.add("2019");
        array.add("2020");
        array.add("2021");
        array.add("2022");
        array.add("2023");
        array.add("2024");
        array.add("2025");

        cbYearNumber.setModel(new DefaultComboBoxModel(array.toArray()));
        cbYearNumber.setSelectedIndex(0);
    }

    private void createFooterText() {

        if (btnSummary.isSelected()) {

            NiceJTable table = gridViewPort.getTable();
            int count = 0, countF = 0, countC = 0, countM = 0;
            int close = 0, closeDeviation = 0, plan = 0, rentedWarehouse = 0, rentedWarehouseContractors = 0, planDeviation = 0, planDeaviationPercent = 0, issuedTinctorial = 0, issuedClose = 0,
                    issuedTinctorialDeviation = 0, issuedCloseDeviation = 0, carryovers = 0;

            //for (int i = 0; i < table.getRowCount(); i++) {
            for (Object object : data) {
                PlanItem item = (PlanItem) object;
                if (item == null) {
                    continue;
                }

                TypeItem typeItem = item.getType();
                if (typeItem.getName().trim().equals("муж")) {
                    countM++;
                }

                //if (table.getValueAt(i, 0).toString().trim().equals("жен")) {
                if (typeItem.getName().trim().equals("жен")) {
                    countF++;
                }

                //if (table.getValueAt(i, 0).toString().trim().equals("дет")) {
                if (typeItem.getName().trim().equals("дет")) {
                    countC++;
                }

                //issuedClose += Integer.valueOf(table.getValueAt(i, 14).toString().trim());
                issuedClose += item.getIssuedClose();

                //issuedTinctorial += Integer.valueOf(table.getValueAt(i, 12).toString().trim());
                issuedTinctorial += item.getIssuedTinctorial();

                //close += Integer.valueOf(table.getValueAt(i, 17).toString().trim());
                close += item.getClose();

//                rentedWarehouse += Integer.valueOf(table.getValueAt(i, 21).toString().trim());
                rentedWarehouse += item.getRented_warehouse();

                // rentedWarehouseContractors += Integer.valueOf(table.getValueAt(i, 22).toString().trim());
                rentedWarehouseContractors += item.getRented_warehouse_contractors();

                //plan += Integer.valueOf(table.getValueAt(i, 23).toString().trim());
                plan += item.getPlan();

                //carryovers += Integer.valueOf(table.getValueAt(i, 11).toString().trim());
                carryovers += item.getCarryovers();

                count++;
            }

            closeDeviation = ((close) * 100 / plan);
            planDeaviationPercent = ((rentedWarehouse + rentedWarehouseContractors) * 100 / plan);
            issuedTinctorialDeviation = (issuedTinctorial * 100 / plan);
            issuedCloseDeviation = (issuedClose * 100 / plan);

            int sum = close;

            lblFooterText.setText("<html>Итого: " + count
                    + "<b> Мужской:</b> " + countM
                    + "<b> Женский: </b>" + countF
                    + "<b> Детский </b>" + countC
                    + "<font color=\"blue\"><br><br><b> Выдано в красилку: </b>" + issuedTinctorial
                    + " <b>Выполнено красилкой: </b>" + issuedTinctorialDeviation + " %"
                    + "<br><b>Выдано на закрой: </b>" + issuedClose
                    + "<b> Выполнено закроем: </b>" + issuedCloseDeviation + " %"

                    + "</font><font color=\"green\"><br><br><b> Закроено : </b>" + sum + " (" + closeDeviation + " %)"
                    + "<b><br> Переходящие остатки: </b>" + carryovers

                    /*+ "<br><br><b> Закроено + перемещение остатков: </b>" + sum
                    + "<b> Выполнено закроем + перемещение остатков: </b>" + closeDeviation + " %"*/
                    + "</font><br><br><b> План: </b>" + plan
                    + "<b> Сдано:  </b>" + rentedWarehouse
                    + "<b> Сдано от подрядчика: </b>" + rentedWarehouseContractors
                    + "<br><br><b> Выполнение: </b>" + planDeaviationPercent + " %"
                    + "</html>");

            pFooter.setVisible(true);
        } else {
            pFooter.setVisible(false);
        }
    }
}
