package by.march8.ecs.application.modules.warehouse.external.shipping.mode;

import by.gomel.freedev.ucframework.uccore.enums.MarchWindowType;
import by.gomel.freedev.ucframework.uccore.enums.RecordOperationType;
import by.gomel.freedev.ucframework.uccore.enums.RightEnum;
import by.gomel.freedev.ucframework.uccore.modes.abstracts.AbstractFunctionalMode;
import by.gomel.freedev.ucframework.uccore.viewport.frameviewport.FrameViewPort;
import by.gomel.freedev.ucframework.uccore.viewport.gridviewport.GridViewPort;
import by.gomel.freedev.ucframework.uccore.viewport.gridviewport.abstracts.TableEventAdapter;
import by.gomel.freedev.ucframework.ucdao.DaoFactory;
import by.gomel.freedev.ucframework.ucdao.interfaces.ICommonDao;
import by.gomel.freedev.ucframework.ucswing.dialog.BaseEditorDialog;
import by.gomel.freedev.ucframework.ucswing.dialog.DateSelectorDialog;
import by.gomel.freedev.ucframework.ucswing.dialog.Dialogs;
import by.gomel.freedev.ucframework.ucswing.uicontrols.EditingPane;
import by.gomel.freedev.ucframework.ucswing.uicontrols.UCDatePeriodPicker;
import by.gomel.freedev.ucframework.ucswing.uicontrols.UCToolBar;
import by.march8.api.BaseEntity;
import by.march8.api.utils.DatePeriod;
import by.march8.api.utils.DateUtils;
import by.march8.ecs.MainController;
import by.march8.ecs.application.modules.warehouse.external.shipping.analysis.SaleMonitorVESAnalysis;
import by.march8.ecs.application.modules.warehouse.external.shipping.dao.DAOSaleDocumentFactory;
import by.march8.ecs.application.modules.warehouse.external.shipping.dao.SaleDocumentJDBC;
import by.march8.ecs.application.modules.warehouse.external.shipping.dao.interfaces.ISaleDocumentDao;
import by.march8.ecs.application.modules.warehouse.external.shipping.editor.RefundInformationEditor;
import by.march8.ecs.application.modules.warehouse.external.shipping.editor.SaleDocumentContractInformationEditor;
import by.march8.ecs.application.modules.warehouse.external.shipping.editor.SaleDocumentEditor;
import by.march8.ecs.application.modules.warehouse.external.shipping.enums.SaleDocumentStatus;
import by.march8.ecs.application.modules.warehouse.external.shipping.forms.CreateAdjustmentDocumentDialog;
import by.march8.ecs.application.modules.warehouse.external.shipping.forms.DocumentUploadDialog;
import by.march8.ecs.application.modules.warehouse.external.shipping.forms.ExitTradeDialog;
import by.march8.ecs.application.modules.warehouse.external.shipping.forms.InventoryDialog;
import by.march8.ecs.application.modules.warehouse.external.shipping.forms.MatrixDocumentDialog;
import by.march8.ecs.application.modules.warehouse.external.shipping.forms.MergingDocumentsDialog;
import by.march8.ecs.application.modules.warehouse.external.shipping.manager.SaleDocumentManager;
import by.march8.ecs.application.modules.warehouse.external.shipping.model.DocumentUploadPreset;
import by.march8.ecs.application.modules.warehouse.external.shipping.model.FinishedGoodsCellRender;
import by.march8.ecs.application.modules.warehouse.external.shipping.model.FinishedGoodsHelper;
import by.march8.ecs.application.modules.warehouse.external.shipping.model.PresetContractor;
import by.march8.ecs.application.modules.warehouse.external.shipping.model.SaleDocumentView;
import by.march8.ecs.application.modules.warehouse.external.shipping.reports.InventoryReport;
import by.march8.ecs.application.modules.warehouse.external.shipping.reports.SaleMonitorReportCreator;
import by.march8.ecs.application.modules.warehouse.external.shipping.reports.WriteOffFormsReport;
import by.march8.ecs.application.modules.warehouse.external.shipping.services.SaleDocumentHistoryService;
import by.march8.ecs.framework.common.BackgroundTask;
import by.march8.entities.warehouse.SaleDocumentAmount;
import by.march8.entities.warehouse.SaleDocumentBase;
import by.march8.entities.warehouse.SaleDocumentContractInformation;
import by.march8.entities.warehouse.SaleDocumentRefundInformation;
import by.march8.tasks.mnsati.InvoiceController;
import by.march8.tasks.mnsati.logic.IssuanceFactory;
import dept.sbit.SendMailForm;
import dept.sklad.PrintTTN;
import dept.sklad.SkladDB;
import dept.sklad.VozvratValuta;

import javax.swing.*;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Функциональный режим рабочего места оператора склада готовой продукции
 * Created by Andy on 06.08.2015.
 */
@SuppressWarnings("all")
public class FinishedGoodsMode extends AbstractFunctionalMode {

    private final ArrayList<Object> data;
    private MainController controller;
    private RightEnum right;
    private EditingPane editingPane;
    private UCDatePeriodPicker datePeriodPicker;
    private DaoFactory factory = DaoFactory.getInstance();
    private ICommonDao commonDao;

    private JComboBox<String> cbDocumentType = new JComboBox<String>(FinishedGoodsHelper.INVOICES_TYPES);
    private JComboBox<String> cbDocumentCurrency = new JComboBox<String>(FinishedGoodsHelper.CURRENCY_TYPES);
    private JTextField tfDocumentNumber = new JTextField();

    private JButton btnDetail;
    //private JButton btnReportNew;

    private JMenuItem miRefundInformation = new JMenuItem("Информация о возврате");
    private JMenuItem miAdjustmentDocument;
    private JMenuItem miInventarDocument;

    private TableRowSorter<TableModel> sorter;
    private JPopupMenu popMenu;
    private JButton btnUpdate = new JButton();
    //private JButton btnReportNew = new JButton();
    private JButton btnTest = new JButton();
    private boolean isLimitAccess = false;
    private boolean isCanDetailView = true;

    //private JButton btnRefundInfo = new JButton();
    //private JButton btnDocumentBridge = new JButton();
    private JButton btnDocumentTools = new JButton();

    private String userSign = "";

    public FinishedGoodsMode(MainController mainController) {
        controller = mainController;
        modeName = "Накладные на отгрузку готовой продукции";
        frameViewPort = new FrameViewPort(mainController, MarchWindowType.INTERNALFRAME);
        frameViewPort.getFrameControl().setTitleFrame(modeName);
        //right = controller.getRight(modeName);
        right = RightEnum.WRITE;
        final UCToolBar toolBar = frameViewPort.getFrameRegion().getToolBar();
        toolBar.registerEvents(this);
        toolBar.setRight(right);

        String editorProperty = controller.getWorkSession().getUserProperty("SaleDocument");


        if (editorProperty == null) {
            editorProperty = "";
        } else if (editorProperty.trim().equals("sale-dept")) {
            userSign = "sale-dept";
        }


        //************ Скрытие элементов управления **************
/*
        toolBar.getBtnNewItem().setVisible(false);
        toolBar.getBtnEditItem().setVisible(false);
        toolBar.getBtnDeleteItem().setVisible(false);*/

        toolBar.getBtnReport().setVisible(false);
        //********************************************************

        // Спецификация накладной
        btnDetail = new JButton();
        btnDetail.setIcon(new ImageIcon(MainController.getRunPath() + "/Img/view_detail.png", "Спецификация"));
        btnDetail.setToolTipText("Спецификация накладной");

        btnTest = new JButton();
        btnTest.setIcon(new ImageIcon(MainController.getRunPath() + "/Img/scanner.png", "Терминал"));
        btnTest.setToolTipText("Принять с терминала");

        //btnReportNew = new JButton();
        //btnReportNew.setIcon(new ImageIcon(MainController.getRunPath() + "/Img/excel_24.png", "Ведомость"));
        //btnReportNew.setToolTipText("Ведомость регистрации БСО");

        btnUpdate.setIcon(new ImageIcon(MainController.getRunPath() + "/Img/update_data.png", "Обновить данные"));
        btnUpdate.setToolTipText("Обновить данные");

        // btnRefundInfo.setIcon(new ImageIcon(MainController.getRunPath() + "/Img/refund_20.png", "Обновть данные"));
        // btnRefundInfo.setToolTipText("Информация о возврате");

        //btnDocumentBridge.setIcon(new ImageIcon(MainController.getRunPath() + "/Img/bank_22.png", "Выгрузка данных для Беларусбанка"));
        //btnDocumentBridge.setToolTipText("Выгрузка данных для Беларусбанка");

        btnDocumentTools.setIcon(new ImageIcon(MainController.getRunPath() + "/Img/admin_22.png", "Изменение атрибутов документа"));
        btnDocumentTools.setToolTipText("Изменение атрибутов документа");

        toolBar.add(btnDetail);
        toolBar.add(btnTest);
        //toolBar.add(btnReportNew);
        //toolBar.add(btnRefundInfo);
        //toolBar.add(btnDocumentBridge);
        //       toolBar.add(btnReportNew);

/*        if (controller.getWorkSession().getAccount().getUserLogin().equals("admin")) {

        }*/

        toolBar.add(btnDocumentTools);
        // Комбик выбора типа документа
        JPanel pSearchPanel = new JPanel(null);
        JLabel lblDocumentNumber = new JLabel("ТТН :");
        lblDocumentNumber.setBounds(5, 4, 40, 20);
        tfDocumentNumber.setBounds(50, 4, 80, 20);

        pSearchPanel.add(lblDocumentNumber);
        pSearchPanel.add(tfDocumentNumber);

        cbDocumentType.setBounds(140, 4, 160, 20);
        pSearchPanel.add(cbDocumentType);

        cbDocumentCurrency.setBounds(315, 4, 75, 20);
        pSearchPanel.add(cbDocumentCurrency);

        pSearchPanel.setPreferredSize(new Dimension(390, 28));
        pSearchPanel.setOpaque(false);
        toolBar.add(pSearchPanel);


        // Селектор периода просмотра документов
        datePeriodPicker = new UCDatePeriodPicker();
        datePeriodPicker.setName("datePeriodPicker");
        datePeriodPicker.setDatePickerBegin(DateUtils.getFirstDay(datePeriodPicker.getDatePickerEnd()));
        datePeriodPicker.setEditable(true);


        toolBar.add(datePeriodPicker);
        toolBar.add(btnUpdate);
        toolBar.add(new Box(BoxLayout.X_AXIS));

        toolBar.setLayout(new FlowLayout(FlowLayout.LEFT));
        toolBar.setPreferredSize(new Dimension(0, 47));

        //TODO Перевести на ENUMS
        if (editorProperty.equals("returns")) {
            isLimitAccess = true;
            btnDetail.setVisible(false);

            toolBar.getBtnNewItem().setVisible(false);
            toolBar.getBtnEditItem().setVisible(false);
            toolBar.getBtnDeleteItem().setVisible(false);
            isCanDetailView = false;
        } else if (userSign.equals("sale-dept")) {
            isLimitAccess = true;
            btnDetail.setVisible(true);
            isCanDetailView = true;

            //btnDocumentBridge.setVisible(false);

            toolBar.getBtnNewItem().setVisible(false);
            toolBar.getBtnEditItem().setVisible(false);
            toolBar.getBtnDeleteItem().setVisible(false);
        } else {
            isLimitAccess = false;
            btnDetail.setVisible(true);

            isCanDetailView = true;

            toolBar.getBtnNewItem().setVisible(true);
            toolBar.getBtnEditItem().setVisible(true);
            toolBar.getBtnDeleteItem().setVisible(true);
        }


        // Инициализация грида
        gridViewPort = new GridViewPort(SaleDocumentView.class, false);
        // Получаем ссылку на модель данных грида
        data = gridViewPort.getDataModel();
        frameViewPort.setGridViewPort(gridViewPort);


        // Устанавливаем кастомный рендер гриду
        gridViewPort.setCustomCellRender(new FinishedGoodsCellRender());

        initialGridSorter();
        initEvents();
        initPopupMenu();

        // Инициализируем нижнюю панель информации грида,
        // и устанавливаем ее свойства отображения
        gridViewPort.initialFooter();

        Container footer = gridViewPort.getFooterPanel();
        footer.setLayout(new FlowLayout(FlowLayout.LEFT));
        JLabel label = gridViewPort.getFooterTextComponent();
        label.setFont(new Font(label.getFont().getName(), Font.BOLD, 12));

        // ВЕшаем слушателя на изменение курсора у грида
        gridViewPort.setTableEventHandler(new TableEventAdapter() {
            @Override
            public void onSelectChanged(final int rowIndex, final Object object) {
                SaleDocumentView selectedItem = (SaleDocumentView) object;
                if (selectedItem != null) {
                    //System.out.println("Ативный документ ["+selectedItem.getId()+"]");
                    try {
                        SaleDocumentAmount item = (SaleDocumentAmount) commonDao.getEntityById(SaleDocumentAmount.class, selectedItem.getId());
                        if (item != null) {
                            gridViewPort.setFooterValue(item.getItemAmount());
                        }

                        if (!selectedItem.getStatusText().trim().equals("Закрыт")) {
                            //toolBar.getBtnNewItem().setEnabled(true);
                            toolBar.getBtnEditItem().setEnabled(true);
                            toolBar.getBtnDeleteItem().setEnabled(true);
                        } else {
                            //toolBar.getBtnNewItem().setEnabled(false);
                            toolBar.getBtnEditItem().setEnabled(false);
                            toolBar.getBtnDeleteItem().setEnabled(false);
                        }

                        if ((selectedItem.getOperation().trim().equals("Возврат материала")) || (selectedItem.getOperation().trim().equals("Возврат от покупателя"))) {
                            miRefundInformation.setEnabled(true);
                        } else {
                            miRefundInformation.setEnabled(false);
                        }

                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onDoubleClick(final int rowIndex, int columnIndex, final Object object) {
                viewDetail();
            }
        });

        commonDao = factory.getCommonDao();

        editingPane = new SaleDocumentEditor(frameViewPort);
        editingPane.setRight(right);
        editingPane.setSourceEntityClass(SaleDocumentView.class);

        controller.getPersonalization().getPersonalSettings(this, datePeriodPicker);
        datePeriodPicker.setDatePickerEnd(new Date());
        updateContent();

        gridViewPort.primaryInitialization();
        frameViewPort.getFrameControl().showFrame();
    }

    private void initialGridSorter() {
        sorter = new TableRowSorter<TableModel>(gridViewPort.getTableModel());
        sorter.setSortsOnUpdates(true);
        gridViewPort.getTable().setRowSorter(sorter);
    }

    private void initEvents() {
        datePeriodPicker.addOnChangeAction(a -> {
            updateContent();
            controller.getPersonalization().setPersonalSettings(this, datePeriodPicker);
        });

        cbDocumentType.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                updateContent();
            }
        });

        cbDocumentCurrency.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                updateContent();
            }
        });

        btnDetail.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                viewDetail();
            }
        });

        tfDocumentNumber.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(java.awt.event.KeyEvent evt) {
                if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
                    sorter.setRowFilter(RowFilter.regexFilter(tfDocumentNumber.getText().trim(), 2));

                } else if (evt.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    //if (rows.size() > 0) {
                    sorter.setRowFilter(null);
                    // }
                    tfDocumentNumber.setText("");
                }
            }
        });

        btnUpdate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                updateContent();
            }
        });

        miRefundInformation.addActionListener(a -> {
            refundInformationControl();
        });


        JPopupMenu popupMenuTools = new JPopupMenu();

        miAdjustmentDocument = new JMenuItem("!!!!! Корректировочный акт !!!!!");
        miAdjustmentDocument.setForeground(Color.red);
        miInventarDocument = new JMenuItem("Инвентаризация для магазина");
        miInventarDocument.setForeground(Color.green);
        JMenuItem miNumberChange = new JMenuItem("Изменение номера документа");
        JMenuItem miContractChange = new JMenuItem("Изменение договора");
        JMenuItem miDateChange = new JMenuItem("Изменение даты документа");
        JMenuItem miDocumentUpload = new JMenuItem("Выгрузка данных");
        JMenuItem miBridge = new JMenuItem("Выгрузка данных Беларусбанк");
        JMenuItem miExitTrade = new JMenuItem("Документ выездной торговли");
        JMenuItem miBSODocument = new JMenuItem("Ведомость регистрации БСО");
        JMenuItem miInventory = new JMenuItem("Инвентаризация филиала");
        JMenuItem miSales = new JMenuItem("Отгрузки по контрагенту");
        JMenuItem miMatrix = new JMenuItem("Матрица отгрузки продукции");
        JMenuItem miAnalysisVES = new JMenuItem("Анализ данных - ОВЭС");
        JMenuItem miMergingDocuments = new JMenuItem("Объединение документов с разбивкой");

        miMergingDocuments.setForeground(Color.BLUE);


        miContractChange.setVisible(true);
        miDocumentUpload.setVisible(true);

        popupMenuTools.add(miAdjustmentDocument);
        popupMenuTools.add(miRefundInformation);
        popupMenuTools.add(miInventarDocument);
        popupMenuTools.add(miBSODocument);
        popupMenuTools.add(miBridge);
        popupMenuTools.addSeparator();
        popupMenuTools.add(miExitTrade);

        popupMenuTools.addSeparator();
        popupMenuTools.add(miNumberChange);
        popupMenuTools.add(miDateChange);
        popupMenuTools.add(miContractChange);
        popupMenuTools.addSeparator();
        popupMenuTools.add(miDocumentUpload);
        popupMenuTools.add(miInventory);
        popupMenuTools.add(miSales);
        popupMenuTools.add(miMatrix);
        popupMenuTools.add(miAnalysisVES);
        popupMenuTools.add(miMergingDocuments);

        miMergingDocuments.addActionListener(a -> {
            doMergingDocuments();
        });


        miContractChange.addActionListener(a -> {
            changeContractDate();
        });

        miDateChange.addActionListener(a -> {
            SaleDocumentView item = (SaleDocumentView) gridViewPort.getSelectedItem();
            if (item != null) {
                changeDocumentDate(item);
            }
        });

        miNumberChange.addActionListener(a -> {
            SaleDocumentView item = (SaleDocumentView) gridViewPort.getSelectedItem();
            if (item != null) {
                changeDocumentNumber(item);
            }
        });

        miDocumentUpload.addActionListener(a -> {
            uploadDocument();
        });

        miBridge.addActionListener(a -> {
            createBridgeDcoument();
        });

        miExitTrade.addActionListener(a -> {
            createExitTradeDocument();
        });

        miSales.addActionListener(a -> {
            createSaleMonitorDocument();
        });

        miMatrix.addActionListener(a -> {
            MatrixDocumentDialog dialog = new MatrixDocumentDialog(controller);
         /*   class Task extends BackgroundTask {
                public Task(final String messageText) {
                    super(messageText);
                }

                @Override
                protected Boolean doInBackground() throws Exception {
                    createSaleMonitorMatrixDocument();
                    return true;
                }
            }
            Task task = new Task("Формирование матрицы отгрузки...");
            task.executeTask();*/
        });

        miBSODocument.addActionListener(a -> {
            DateSelectorDialog dialogDateSelector = new DateSelectorDialog(controller);
            dialogDateSelector.setTitle("Установка периода для формирования отчета");
            Date d = dialogDateSelector.selectCustomDate(DateUtils.getDateNow());
            if (d == null) {
                return;
            }

            if (!d.equals(DateUtils.getDateNow())) {
                new WriteOffFormsReport(d);
            }
        });


        btnDocumentTools.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                if (btnDocumentTools.isEnabled()) {
                    SaleDocumentView selectedItem = (SaleDocumentView) gridViewPort.getSelectedItem();
                    if (selectedItem != null) {
                        //SaleDocumentManager.isDocumentClosed(selectedItem) &&
                        if (SaleDocumentManager.isDocumentClosed(selectedItem) && SaleDocumentManager.isDocumentCanBeAdjusted(selectedItem)) {
                            miAdjustmentDocument.setVisible(true);
                        } else {
                            miAdjustmentDocument.setVisible(false);
                        }
                    }
                    popupMenuTools.show(e.getComponent(), 0, e.getComponent().getHeight());
                }
            }
        });

        miInventory.addActionListener(a -> {
            createInventoryCard();
        });

        miAdjustmentDocument.addActionListener(a -> {
            SaleDocumentView selectedItem = (SaleDocumentView) gridViewPort.getSelectedItem();
            if (selectedItem != null) {
                createAdjustmentDocument(selectedItem);
            }
        });

        miInventarDocument.addActionListener(a -> {
            SaleDocumentView selectedItem = (SaleDocumentView) gridViewPort.getSelectedItem();
            if (selectedItem != null) {
                InventoryReport report = new InventoryReport();
                report.createInventoryDocument(selectedItem);
            }
        });

        miAnalysisVES.addActionListener(a -> {
            new SaleMonitorVESAnalysis();
        });
    }


    private void createSaleMonitorMatrixDocument() {
        SaleMonitorReportCreator creator = new SaleMonitorReportCreator();
        List<PresetContractor> contractors = new ArrayList<>();
        contractors.add(new PresetContractor(6250));
        contractors.add(new PresetContractor(3850));
        contractors.add(new PresetContractor(2869));
        contractors.add(new PresetContractor(5222));
        contractors.add(new PresetContractor(2236));
        contractors.add(new PresetContractor(4474));
        contractors.add(new PresetContractor(4505));
        contractors.add(new PresetContractor(4506));
        contractors.add(new PresetContractor(4576));
        contractors.add(new PresetContractor(4591));
        contractors.add(new PresetContractor(5270, true));
        contractors.add(new PresetContractor(8889));
        contractors.add(new PresetContractor(2281));
        contractors.add(new PresetContractor(3451));
        contractors.add(new PresetContractor(5417));

        DatePeriod period = new DatePeriod();
        period.setBegin(DateUtils.getDateByStringValue("01.02.2020"));
        // period.setEnd(DateUtils.getDateByStringValue("20.03.2019"));
        period.setEnd(new Date());


        /*
             // Январь 2019
                int[] models = new int[]{
                13623, 13628, 13626, 14067, 14556, 14554, 14553, 14905,
                14446, 14555, 14885, 14878, 14602, 14065, 14869, 14901,
                14721, 14887, 14902, 14904, 14873, 14808, 14807, 14639
        };*/

        // Февраль 2019
        int[] models = new int[]{
                14991, 14881, 14877, 14879, 14882, 14884, 14921, 14922, 14924, 14925, 14944, 14945, 14950, 14951, 14872, 14867, 14864, 14865, 14866, 14858,
                13725, 13651, 13724, 13722, 13700, 13714, 13721, 13704, 13719, 13673, 13718, 13715, 13713, 13717, 13727, 13728, 13729
        };


        creator.createMatrix(contractors, models, period);
    }

    private void createSaleMonitorDocument() {
        SaleMonitorReportCreator creator = new SaleMonitorReportCreator();
        creator.createDocument(new int[]{4955, 1527, 5015}, new int[]{2017, 2018});
    }

    private void createAdjustmentDocument(SaleDocumentView selectedItem) {
        CreateAdjustmentDocumentDialog dialog = new CreateAdjustmentDocumentDialog(controller, selectedItem);
        // Если над документом произведены предварительные корректировочные процессы
        // то обновим выборку, и далее работаем с документом как обычно
        if (dialog.showDialog()) {
            updateContent();
        }
    }

    private void createInventoryCard() {
        InventoryDialog dialog = new InventoryDialog(controller);

        DocumentUploadPreset preset = dialog.showDialog();
        if (preset != null) {
            class Task extends BackgroundTask {
                public Task(final String messageText) {
                    super(messageText);
                }

                @Override
                protected Boolean doInBackground() throws Exception {
                    dialog.createReport(this, preset);
                    return true;
                }
            }
            Task task = new Task("Формирование инвентаризационной описи...");
            task.executeTask();
        }
    }

    private void createExitTradeDocument() {
        //
        String value = JOptionPane.showInputDialog(null, "Введите код контрагента : ", 5863);
        try {
            int code = Integer.valueOf(value.trim());
            ExitTradeDialog dialogDialog = new ExitTradeDialog(controller, datePeriodPicker, code);
            dialogDialog.showDialog();
            updateContent();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void uploadDocument() {
        DocumentUploadDialog dialog = new DocumentUploadDialog(controller);
        DocumentUploadPreset resultPreset = dialog.showDialog(null);
        if (resultPreset != null) {
            uploadDocuments(resultPreset);
        }
    }

    private void uploadDocuments(final DocumentUploadPreset preset) {
        class Task extends BackgroundTask {
            public Task(final String messageText) {
                super(messageText);
            }

            @Override
            protected Boolean doInBackground() throws Exception {
                InvoiceController creator = new InvoiceController(preset);
                return true;
            }
        }
        Task task = new Task("Формирование выгрузки ...");
        task.executeTask();
    }

    private void createBridgeDcoument() {
        class Task extends BackgroundTask {
            public Task(final String messageText) {
                super(messageText);
            }

            @Override
            protected Boolean doInBackground() throws Exception {
                InvoiceController creator = new InvoiceController(null, 3061);
                return true;
            }
        }
        Task task = new Task("Формирование документа <МОСТ>...");
        task.executeTask();
    }

    @Override
    public void addRecord() {
        // Создаем пустую форму диалога
        BaseEditorDialog editor = new BaseEditorDialog(controller,
                RecordOperationType.EDIT);
        editor.setParentTitle("Накладная на отгрузку");
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
                Object o = dao.saveEntity(editingPane.getSourceEntity());
                gridViewPort.setUpdatedObject(o);


                SaleDocumentBase item = (SaleDocumentBase) o;
                if (item != null) {
                    SaleDocumentHistoryService history = SaleDocumentHistoryService.getInstance();
                    history.historyCreateDocument(item.getId(), item.getDocumentNumber());
                }

            } catch (final SQLException e) {
                e.printStackTrace();
            }
            // ПОсле сохранения данных обновляем содержимое грида
            updateContent();
        }
    }

    @Override
    public void editRecord() {
        // Создаем пустую форму диалога
        BaseEditorDialog editor = new BaseEditorDialog(controller,
                RecordOperationType.EDIT);
        editor.setParentTitle("Накладная на отгрузку");
        editingPane.setSourceEntity(gridViewPort.getSelectedItem());

        SaleDocumentView item = (SaleDocumentView) gridViewPort.getSelectedItem();
        String oldDocumentNumber = null;
        if (item != null) {
            oldDocumentNumber = item.getDocumentNumber();
        }

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
                // Обновляем сущность в БД
                Object o = editingPane.getSourceEntity();
                dao.updateEntity(o);
                gridViewPort.setUpdatedObject(o);

                SaleDocumentBase item_ = (SaleDocumentBase) o;
                if (item_ != null) {
                    SaleDocumentHistoryService history = SaleDocumentHistoryService.getInstance();
                    // Фиксируем факт изменения номера документа
                    if (oldDocumentNumber != null) {
                        if (!oldDocumentNumber.trim().equals(item_.getDocumentNumber().trim())) {
                            history.historyEditNumberDocument(item_.getId(), item_.getDocumentNumber(), oldDocumentNumber);
                        }
                    }
                    // Фиксируем факт редактирования документа
                    history.historyEditDocument(item_.getId(), item_.getDocumentNumber());
                }

                // Изменяем статус расчета по документу как РАСЧИТАН И СОХРАНЕН
                SaleDocumentManager.checkDocument(((BaseEntity) o).getId(), false);
            } catch (final SQLException e) {
                e.printStackTrace();
            }
            // ПОсле сохранения данных обновляем содержимое грида
            updateContent();
        }
    }

    @Override
    public void deleteRecord() {
        SaleDocumentView document = (SaleDocumentView) gridViewPort.getSelectedItem();
        if (!SaleDocumentManager.isDocumentClosed(document)) {
            SaleDocumentHistoryService historyService = SaleDocumentHistoryService.getInstance();

            if (SaleDocumentManager.isDocumentDeleted(document)) {
                final int answer = Dialogs.showQuestionDialog("Вы действительно хотите восстановить документ?\n" +
                        "\nСтатус документа будет изменен на [формируется].", "Восстановление документа");
                if (answer == 0) {
                    SaleDocumentManager.changeDocumentStatus(document.getId(), SaleDocumentStatus.PRE_FORMED);
                    historyService.historyOpenDocument(document.getId(), document.getDocumentNumber());
                    updateContent();
                }
            } else {
                final int answer = Dialogs.showQuestionDialog("Вы действительно хотите пометить документ на удаление?\n" +
                        "\nДокумент не будет принят к учету.", "Пометить документ на удаление");
                if (answer == 0) {
                    SaleDocumentManager.changeDocumentStatus(document.getId(), SaleDocumentStatus.DELETED);
                    historyService.historyDeleteDocument(document.getId(), document.getDocumentNumber());
                    updateContent();
                }
            }

        }
    }

    private void viewDetail() {

        if (!isCanDetailView) {
            return;
        }

        SaleDocumentView document = (SaleDocumentView) gridViewPort.getSelectedItem();
        if (document != null) {
            String operation = document.getOperation().trim();

            FinishedGoodsDetailMode detailMode = new FinishedGoodsDetailMode(controller, gridViewPort.getSelectedItem());
            if (detailMode.showMode()) {
                updateContent();
            }
        }
    }

    @Override
    public void updateContent() {
        DAOSaleDocumentFactory factory = DAOSaleDocumentFactory.getInstance();
        ISaleDocumentDao dao = factory.getSaleDocumentDao();
        try {
            int currIndex = cbDocumentCurrency.getSelectedIndex() - 1;
            data.clear();
            data.addAll(dao.getSaleDocumentsByPeriodThread(
                    cbDocumentType.getItemAt(cbDocumentType.getSelectedIndex()),
                    datePeriodPicker.getDatePickerBegin(),
                    datePeriodPicker.getDatePickerEnd(),
                    currIndex));
        } catch (final Exception e) {
            e.printStackTrace();
        }
        frameViewPort.updateContent();
    }

    private void initPopupMenu() {
        // Создаём высплывающее меню
        popMenu = new JPopupMenu();

        JMenuItem mDrivingDocumentEditor = new JMenuItem("Формирование документов");
        mDrivingDocumentEditor.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                try {
                    SaleDocumentView document = (SaleDocumentView) gridViewPort.getSelectedItem();
                    if (document != null) {
                        new PrintTTN(controller.getMainForm(), true, document.getDocumentNumber());
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        JMenuItem mSpecification = new JMenuItem("Спецификация");
        mSpecification.addActionListener(new ActionListener() {
                                             @Override
                                             public void actionPerformed(final ActionEvent e) {
                                                 viewDetail();
                                             }
                                         }
        );

        final JMenuItem mRefundDocument = new JMenuItem("Возвраты в валюте");
        mRefundDocument.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                final SaleDocumentView document = (SaleDocumentView) gridViewPort.getSelectedItem();
                if (document != null) {
                    class Task extends BackgroundTask {
                        public Task(final String messageText) {
                            super(messageText);
                        }

                        @Override
                        protected Boolean doInBackground() throws Exception {
                            controller.openInternalFrame(new VozvratValuta(controller, gridViewPort.getFooterTextComponent().getText(),
                                    document.getDocumentNumber(),
                                    String.valueOf(document.getContractorCode())));
                            return true;
                        }
                    }
                    Task task = new Task("Формирование возврата...");
                    task.executeTask();
                }
            }
        });

        final JMenuItem mOpenDocument = new JMenuItem("Открыть накладную");
        mOpenDocument.addActionListener(new ActionListener() {
                                            @Override
                                            public void actionPerformed(final ActionEvent e) {
                                                SaleDocumentView document = (SaleDocumentView) gridViewPort.getSelectedItem();
                                                if (document != null) {
                                                    final int answer = Dialogs.showQuestionDialog("Вы действительно хотите открыть документ?\n" +
                                                            "Факт изменения статуса документа будет занесен в журнал событий.", "Изменение статуса документа");
                                                    if (answer == 0) {
                                                        SkladDB sdb = new SkladDB();
                                                        if (sdb.OpenCloseTTN(document.getDocumentNumber())) {
                                                            JOptionPane.showMessageDialog(null,
                                                                    "Накладная открыта");
                                                            updateContent();
                                                            SaleDocumentHistoryService historyService = SaleDocumentHistoryService.getInstance();
                                                            historyService.historyOpenDocument(document.getId(), document.getDocumentNumber());
                                                        }
                                                        sdb.disConn();
                                                    }
                                                }
                                            }
                                        }
        );

        final JMenuItem mCreateIssued = new JMenuItem("Создать счет-фактуру");

        mCreateIssued.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                SaleDocumentView document = (SaleDocumentView) gridViewPort.getSelectedItem();
                if (document != null) {
                    IssuanceFactory factory = IssuanceFactory.getInstance();
                    factory.createIssuanceByDocumentNumberDialog(document.getDocumentNumber());
                }
            }
        });

        JMenuItem mSendInvoices = new JMenuItem("Отправить накладную");
        mSendInvoices.setForeground(Color.blue);

        mSendInvoices.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                SaleDocumentView document = (SaleDocumentView) gridViewPort.getSelectedItem();
                if (document != null) {
                    new SendMailForm(controller, null, true, false, document);
                }
            }
        });

        JMenuItem mHistory = new JMenuItem("Журнал изменений");
        //mHistory.setForeground(Color.orange);

        mHistory.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                SaleDocumentView document = (SaleDocumentView) gridViewPort.getSelectedItem();
                if (document != null) {
                    new SaleDocumentHistoryMode(controller, document.getId());
                }
            }
        });

        //popMenu.add(mDrivingDocumentEditor);
        popMenu.add(mSpecification);
        popMenu.add(mRefundDocument);
        popMenu.add(mOpenDocument);


        popMenu.add(mCreateIssued);
        popMenu.add(mSendInvoices);
        popMenu.addSeparator();


        popMenu.add(mHistory);


        gridViewPort.getTable().
                addMouseListener(new MouseAdapter() {
                                     @Override
                                     public void mouseClicked(MouseEvent e) {
                                         if (e.isPopupTrigger()) {
                                             Point point = e.getPoint();
                                             int column = gridViewPort.getTable().columnAtPoint(point);
                                             int row = gridViewPort.getTable().rowAtPoint(point);

                                             // выполняем проверку
                                             if (column != -1 && row != -1) {
                                                 gridViewPort.getTable().setColumnSelectionInterval(column, column);
                                                 gridViewPort.getTable().setRowSelectionInterval(row, row);
                                             }
                                             if (!isLimitAccess) popMenu.show(e.getComponent(), e.getX(), e.getY());
                                         }
                                     }

                                     @Override
                                     public void mousePressed(MouseEvent e) {
                                         if (e.isPopupTrigger()) {
                                             Point point = e.getPoint();
                                             int column = gridViewPort.getTable().columnAtPoint(point);
                                             int row = gridViewPort.getTable().rowAtPoint(point);

                                             // выполняем проверку
                                             if (column != -1 && row != -1) {
                                                 gridViewPort.getTable().setColumnSelectionInterval(column, column);
                                                 gridViewPort.getTable().setRowSelectionInterval(row, row);
                                             }
                                             if (!isLimitAccess) popMenu.show(e.getComponent(), e.getX(), e.getY());
                                         }
                                     }

                                     @Override
                                     public void mouseReleased(MouseEvent e) {
                                         if (e.isPopupTrigger()) {
                                             Point point = e.getPoint();
                                             int column = gridViewPort.getTable().columnAtPoint(point);
                                             int row = gridViewPort.getTable().rowAtPoint(point);

                                             // выполняем проверку
                                             if (column != -1 && row != -1) {
                                                 gridViewPort.getTable().setColumnSelectionInterval(column, column);
                                                 gridViewPort.getTable().setRowSelectionInterval(row, row);
                                             }
                                             if (!isLimitAccess) popMenu.show(e.getComponent(), e.getX(), e.getY());
                                         }
                                     }
                                 }
                );

        popMenu.addPopupMenuListener(
                new PopupMenuListener() {
                    @Override
                    public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
                        SaleDocumentView document = (SaleDocumentView) gridViewPort.getSelectedItem();
                        if (document != null) {
                            if (document.getOperation().trim().equals("Возврат от покупателя") && document.getStatusText().trim().equals("Формируется")) {
                                mRefundDocument.setVisible(true);
                            } else {
                                mRefundDocument.setVisible(false);
                            }

                            if (document.getStatusText().equals("Закрыт")) {
                                mOpenDocument.setVisible(true);
                            } else {
                                mOpenDocument.setVisible(false);
                            }

                            if (document.getStatusText().equals("Формируется")) {
                                mCreateIssued.setVisible(false);
                                mOpenDocument.setVisible(false);
                            } else {
                                if (document.getStatusText().equals("Удалён")) {
                                    mOpenDocument.setVisible(false);
                                    mCreateIssued.setVisible(false);
                                } else {
                                    mCreateIssued.setVisible(true);
                                }
                            }
                        }
                    }

                    @Override
                    public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
                    }

                    @Override
                    public void popupMenuCanceled(PopupMenuEvent e) {
                    }
                }

        );
    }

    private void refundInformationControl() {
        // Создаем пустую форму диалога
        BaseEditorDialog editor = new BaseEditorDialog(controller,
                RecordOperationType.EDIT);

        DaoFactory factory = DaoFactory.getInstance();
        // ПОлучаем интерфейс для работы с БД
        ICommonDao dao = factory.getCommonDao();

        editor.setParentTitle("Информация о возврате");

        SaleDocumentRefundInformation refundInformation = null;
        // Получаем идентификатор
        int id = ((BaseEntity) gridViewPort.getSelectedItem()).getId();
        try {
            refundInformation = (SaleDocumentRefundInformation) dao.getEntityById(SaleDocumentRefundInformation.class, id);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (refundInformation == null) {
            return;
        }

        EditingPane editPane = new RefundInformationEditor(controller);
        // Для созданного пустого диалога устанавливаем панель редактирования
        editor.setEditorPane(editPane);

        editPane.setSourceEntity(refundInformation);
        // Модально показываем форму и ожидаем закрытия
        if (editor.showModal()) {
            // Форма закрыта со значением true
            // Получаем DAO слой
            DaoFactory factoryMarch = DaoFactory.getInstance();
            // ПОлучаем интерфейс для работы с БД
            ICommonDao daoMarch = factory.getCommonDao();
            try {

                // Обновляем сущность в БД
                Object o = editPane.getSourceEntity();
                daoMarch.updateEntity(o);

                gridViewPort.setUpdatedObject(o);
                // Изменяем статус расчета по документу как РАСЧИТАН И СОХРАНЕН
                //SaleDocumentManager.checkDocument(((BaseEntity) o).getId(), false);
            } catch (final SQLException e) {
                e.printStackTrace();
            }
            // ПОсле сохранения данных обновляем содержимое грида
            updateContent();
        }
    }

    public void changeContractDate() {
        // Создаем пустую форму диалога
        BaseEditorDialog editor = new BaseEditorDialog(controller,
                RecordOperationType.EDIT);

        DaoFactory factory = DaoFactory.getInstance();
        // ПОлучаем интерфейс для работы с БД
        ICommonDao dao = factory.getCommonDao();

        editor.setParentTitle("Первичная информация о документе");

        SaleDocumentContractInformation contractInformation = null;
        // Получаем идентификатор
        SaleDocumentView item = (SaleDocumentView) gridViewPort.getSelectedItem();
        try {
            contractInformation = (SaleDocumentContractInformation) dao.getEntityById(SaleDocumentContractInformation.class, item.getId());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (contractInformation == null) {
            return;
        }

        contractInformation.setContractorId(item.getContractorId());

        EditingPane editPane = new SaleDocumentContractInformationEditor(controller);
        // Для созданного пустого диалога устанавливаем панель редактирования
        editor.setEditorPane(editPane);

        editPane.setSourceEntity(contractInformation);
        // Модально показываем форму и ожидаем закрытия
        if (editor.showModal()) {
            // Форма закрыта со значением true
            // Получаем DAO слой
            DaoFactory factoryMarch = DaoFactory.getInstance();
            // ПОлучаем интерфейс для работы с БД
            ICommonDao daoMarch = factory.getCommonDao();
            try {
                // Обновляем сущность в БД
                Object o = editPane.getSourceEntity();
                daoMarch.updateEntity(o);

                gridViewPort.setUpdatedObject(o);
                // Изменяем статус расчета по документу как РАСЧИТАН И СОХРАНЕН
                //SaleDocumentManager.checkDocument(((BaseEntity) o).getId(), false);
            } catch (final SQLException e) {
                e.printStackTrace();
            }
            // ПОсле сохранения данных обновляем содержимое грида
            updateContent();
        }
    }

    public void changeDocumentDate(SaleDocumentView document) {
        DateSelectorDialog dialogDateSelector = new DateSelectorDialog(controller);
        SaleDocumentJDBC db = new SaleDocumentJDBC();
        Date date = db.getSaleDateForDocumentId(document.getId());
        Date d = dialogDateSelector.selectDate(date);
        if (d != null) {
            db.updateSaleDateForDocumentId(document.getId(), d);
            updateContent();
        }
    }

    public void changeDocumentNumber(SaleDocumentView document) {
        SaleDocumentJDBC db = new SaleDocumentJDBC();
        String number = db.getDocumentNumberForDocumentId(document.getId());
        if (number != null) {
            String input = JOptionPane.showInputDialog(null,
                    "Введите новый номер документа", number);
            if (input != null) {
                if (!input.trim().equals(number)) {
                    db.updateDocumentNumberForDocumentId(document.getId(), input.trim());
                    updateContent();
                }
            }
        }
    }


    private void doMergingDocuments() {
        MergingDocumentsDialog dialog = new MergingDocumentsDialog(controller);

        if (dialog.showDialog()) {
            updateContent();
        }
    }
}
