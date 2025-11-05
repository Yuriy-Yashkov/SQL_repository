package by.march8.ecs.application.modules.sales.mode;

import by.gomel.freedev.ucframework.uccore.enums.MarchWindowType;
import by.gomel.freedev.ucframework.uccore.enums.RecordOperationType;
import by.gomel.freedev.ucframework.uccore.enums.RightEnum;
import by.gomel.freedev.ucframework.uccore.modes.abstracts.AbstractFunctionalMode;
import by.gomel.freedev.ucframework.uccore.viewport.frameviewport.FrameViewPort;
import by.gomel.freedev.ucframework.uccore.viewport.gridviewport.GridViewPort;
import by.gomel.freedev.ucframework.uccore.viewport.gridviewport.abstracts.TableEventAdapter;
import by.gomel.freedev.ucframework.ucdao.DaoFactory;
import by.gomel.freedev.ucframework.ucdao.interfaces.ICommonDao;
import by.gomel.freedev.ucframework.ucdao.interfaces.ICommonDaoThread;
import by.gomel.freedev.ucframework.ucdao.interfaces.IGenericDao;
import by.gomel.freedev.ucframework.ucdao.model.QueryProperty;
import by.gomel.freedev.ucframework.ucswing.dialog.BaseEditorDialog;
import by.gomel.freedev.ucframework.ucswing.dialog.CustomDialog;
import by.gomel.freedev.ucframework.ucswing.dialog.Dialogs;
import by.gomel.freedev.ucframework.ucswing.uicontrols.EditingPane;
import by.gomel.freedev.ucframework.ucswing.uicontrols.UCToolBar;
import by.march8.ecs.MainController;
import by.march8.ecs.application.modules.marketing.manager.PreOrderSaleDocumentCalculator;
import by.march8.ecs.application.modules.references.classifier.enums.ClassifierType;
import by.march8.ecs.application.modules.references.classifier.mode.ClassifierPickMode;
import by.march8.ecs.application.modules.references.classifier.model.ClassifierNode;
import by.march8.ecs.application.modules.references.classifier.model.ClassifierNodeType;
import by.march8.ecs.application.modules.references.classifier.ui.ClassifierTree;
import by.march8.ecs.application.modules.sales.editor.PreOrderSaleDocumentEditor;
import by.march8.ecs.application.modules.sales.manager.PreOrderSaleDocumentManager;
import by.march8.ecs.application.modules.sales.model.PreOrderControlType;
import by.march8.ecs.application.modules.sales.model.PreOrderDetailRenderer;
import by.march8.ecs.application.modules.sales.model.PreOrderImportType;
import by.march8.ecs.application.modules.warehouse.external.shipping.dao.DatabaseDocumentUpdater;
import by.march8.ecs.application.modules.warehouse.external.shipping.dao.DocumentUpdater;
import by.march8.ecs.application.modules.warehouse.external.shipping.forms.SaleDocumentInformationPanel;
import by.march8.ecs.application.modules.warehouse.external.shipping.manager.SaleDocumentDataProvider;
import by.march8.ecs.application.modules.warehouse.external.shipping.manager.SaleDocumentManager;
import by.march8.ecs.application.modules.warehouse.external.shipping.model.CurrencyName;
import by.march8.ecs.application.modules.warehouse.external.shipping.model.SaleDocumentReport;
import by.march8.ecs.application.modules.warehouse.external.shipping.model.TotalSummingUp;
import by.march8.ecs.application.modules.warehouse.external.shipping.reports.AnnexTTNExportReport;
import by.march8.ecs.application.modules.warehouse.external.shipping.reports.AnnexTTNProductDiscountReport;
import by.march8.ecs.application.modules.warehouse.external.shipping.reports.AnnexTTNReport;
import by.march8.ecs.application.modules.warehouse.external.shipping.reports.ContractorInvoiceReport;
import by.march8.ecs.application.modules.warehouse.external.shipping.reports.InternetMarketExportReport;
import by.march8.ecs.application.modules.warehouse.external.shipping.reports.ProtocolBelPostReport;
import by.march8.ecs.framework.common.BackgroundTask;
import by.march8.ecs.framework.common.Settings;
import by.march8.ecs.services.images.ColorImageService;
import by.march8.ecs.services.images.ModelImageServiceDB;
import by.march8.entities.sales.PreOrderSaleDocument;
import by.march8.entities.sales.PreOrderSaleDocumentBase;
import by.march8.entities.sales.PreOrderSaleDocumentItemBase;
import by.march8.entities.sales.PreOrderSaleDocumentItemView;
import by.march8.entities.sales.PreOrderSaleDocumentView;
import by.march8.entities.warehouse.SaleDocument;
import by.march8.entities.warehouse.SaleDocumentBase;
import by.march8.entities.warehouse.SaleDocumentEntity;
import by.march8.entities.warehouse.SaleDocumentItem;
import common.DateUtils;
import dept.ves.mode.SaleDocumentPicker;

import javax.swing.*;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author Andy 14.01.2019 - 7:11.
 */
public class PreOrderSaleDocumentDetailMode extends AbstractFunctionalMode<PreOrderSaleDocumentItemView> {
    private FrameViewPort vpParent;

    private PreOrderSaleDocumentView document;
    private GridViewPort<PreOrderSaleDocumentItemView> gvDetails;
    private List<PreOrderSaleDocumentItemView> dataList;
    private UCToolBar toolBar;
    private ClassifierPickMode classifier;
    private ClassifierTree tree;


    private PreOrderSaleDocumentManager manager;
    private ClassifierNode selectedProductNode;

    private EditingPane<PreOrderSaleDocumentItemView, PreOrderSaleDocumentItemBase> editingPane;
    private ColorImageService imageService;
    private JButton btnExportDocument;

    private JPopupMenu popupMenuTools;
    private JMenuItem miAddItemFromClassifier;
    private JMenuItem miAddFromOrder;
    private JMenuItem miAddFromExcel;
    private JMenuItem miAddFromSaleDocument;
    private JToggleButton btnShowTree;
    private JPanel pCatalog;
    private JSplitPane spVertical;
    private JButton btnCalculate;
    private JButton btnInformation;
    private JPanel pControl;
    private JButton btnPrint;
    private JPanel pFooter;
    private JButton btnSendEMail;
    private SaleDocumentInformationPanel informationPanel;
    private boolean needUpdate = false;
    private JPopupMenu popupExport;
    private JMenuItem miAnnexDocument;
    private JMenuItem miProtocolDocument;
    private JMenuItem miInvoice;
    private JMenuItem miAddArticleFromClassifier;
    private JButton btnCalculator;
    private JMenuItem miApplyDiscount;
    private JMenuItem miInternetMarkt;

    public PreOrderSaleDocumentDetailMode(FrameViewPort parentViewPort, PreOrderSaleDocumentView document, PreOrderControlType type) {
        vpParent = parentViewPort;
        this.document = document;
        controller = vpParent.getController();
        modeName = "Предварительный заказ " + document.getDocumentInformation();
        frameViewPort = new FrameViewPort(controller, MarchWindowType.DIALOGSIMPLE);
        frameViewPort.getFrameControl().setTitleFrame(modeName);

        init();
        initEvents();
        updateContent();

        frameViewPort.getFrameControl().setFrameSize(new Dimension(1200, 750));
        frameViewPort.getButtonControl().getButtonPanel().setVisible(false);
    }

    private void init() {

        frameViewPort.getFrameRegion().getBottomContentPanel().setLayout(new BorderLayout());
        gvDetails = new GridViewPort<>(PreOrderSaleDocumentItemView.class);
        dataList = gvDetails.getDataModel();

        //gvDetails.setCustomCellRender(new ProductionCatalogArticleCellRenderer());

        toolBar = frameViewPort.getFrameRegion().getToolBar();
        toolBar.setRight(RightEnum.WRITE);

        toolBar.getBtnViewItem().setVisible(true);
        toolBar.getBtnViewItem().setVisible(false);

        tree = new ClassifierTree(new int[]{45, 47, 48});
        tree.setExpandLimit(ClassifierNodeType.CATEGORY);

        pCatalog = new JPanel(new BorderLayout());
        pCatalog.add(tree, BorderLayout.CENTER);

        spVertical = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, pCatalog, gvDetails);
        spVertical.setResizeWeight(0.2);
        spVertical.setOneTouchExpandable(true);
        spVertical.setContinuousLayout(true);

        btnExportDocument = new JButton();
        btnExportDocument.setIcon(new ImageIcon(MainController.getRunPath() + "/Img/export_file_24.png", "Обновить данные"));
        btnExportDocument.setToolTipText("Выгрузить каталог");

        btnSendEMail = new JButton();
        btnSendEMail.setIcon(new ImageIcon(MainController.getRunPath() + "/Img/email_24.png", "Отправить электронное письмо"));
        btnSendEMail.setToolTipText("Отправить электронное письмо");

        btnCalculator = new JButton();
        btnCalculator.setIcon(new ImageIcon(MainController.getRunPath() + "/Img/scanner.png", "Калькулятор рентабельности"));
        btnCalculator.setToolTipText("Калькулятор рентабельности");

        btnInformation = new JButton();
        btnInformation.setIcon(new ImageIcon(MainController.getRunPath() + "/Img/document_24.png", "Информация о документе"));

        btnShowTree = new JToggleButton();
        btnShowTree.setIcon(new ImageIcon(MainController.getRunPath() + "/Img/tree_24.png", ""));
        btnShowTree.setToolTipText("Пане");

        btnCalculate = new JButton();
        btnCalculate.setIcon(new ImageIcon(MainController.getRunPath() + "/Img/sum_24.png", "Расчет документа"));
        btnCalculate.setToolTipText("Расчет документа");

        popupMenuTools = new JPopupMenu();

        miAddItemFromClassifier = new JMenuItem("Изделие из классификатора");
        miAddArticleFromClassifier = new JMenuItem("Артикул из классификатора");
        miAddFromOrder = new JMenuItem("Из файла заказа");
        miAddFromExcel = new JMenuItem("Из файла EXCEL");
        miAddFromSaleDocument = new JMenuItem("Из накладной");

        popupMenuTools.add(miAddItemFromClassifier);
        popupMenuTools.add(miAddArticleFromClassifier);
        popupMenuTools.addSeparator();
        popupMenuTools.add(miAddFromOrder);
        popupMenuTools.add(miAddFromExcel);
        popupMenuTools.addSeparator();
        popupMenuTools.add(miAddFromSaleDocument);

        toolBar.add(btnInformation, 0);
        // toolBar.addSeparator();
        // toolBar.add(btnShowTree);
        toolBar.addSeparator();
        toolBar.add(btnCalculator);
        toolBar.addSeparator();
        toolBar.add(btnSendEMail);


        frameViewPort.getFrameRegion().getCenterContentPanel().add(spVertical);

        classifier = new ClassifierPickMode(controller, ClassifierType.CUSTOM);
        manager = PreOrderSaleDocumentManager.getInstance(controller);
        //editingPane = new ProductionCatalogArticleEditor(frameViewPort, manager );
        imageService = ModelImageServiceDB.getInstance();
        pCatalog.setVisible(false);

        pControl = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        pControl.setPreferredSize(new Dimension(0, 37));

        btnPrint = new JButton();
        btnPrint.setIcon(new ImageIcon(MainController.getRunPath() + "/Img/printer.png", "Печать документа"));
        btnPrint.setText("Сформировать");
        btnPrint.setPreferredSize(Settings.BUTTON_BIG_SIZE);
        btnCalculate.setPreferredSize(Settings.BUTTON_BIG_SIZE);

        pControl.add(btnCalculate);
        pControl.add(btnPrint);

        informationPanel = new SaleDocumentInformationPanel();
        informationPanel.visibleLeftPanel(true);

        pFooter = new JPanel(new BorderLayout());
        pFooter.setPreferredSize(new Dimension(200, 225));
        pFooter.setBackground(Color.black);
        pFooter.add(pControl, BorderLayout.SOUTH);
        pFooter.add(informationPanel, BorderLayout.CENTER);

        frameViewPort.getFrameRegion().getBottomContentPanel().add(pFooter, BorderLayout.SOUTH);
        gvDetails.setCustomCellRender(new PreOrderDetailRenderer());

        popupExport = new JPopupMenu();
        miApplyDiscount = new JMenuItem("Применить скидку к накладной");
        miApplyDiscount.setForeground(Color.RED);
        popupExport.add(miApplyDiscount);
        popupExport.addSeparator();

        miInvoice = new JMenuItem("Счет-фактура");
        miAnnexDocument = new JMenuItem("Приложение");
        miProtocolDocument = new JMenuItem("Протокол согласования");
        miInternetMarkt = new JMenuItem("Интернет магазин БУСЛИК");
        popupExport.add(miInvoice);
        popupExport.add(miAnnexDocument);
        popupExport.add(miProtocolDocument);
        popupExport.addSeparator();

        popupExport.add(miInternetMarkt);
    }

    private void initEvents() {
        gvDetails.setTableEventHandler(new TableEventAdapter() {
            @Override
            public void onDoubleClick(final int rowIndex, int columnIndex, final Object object) {

            }
        });

        tree.addTreeListener(a -> {
            if (a != null) {
                getCustomUpdate(a);
            }
        });

        btnExportDocument.addActionListener(a -> {

        });

        toolBar.getBtnNewItem().addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                popupMenuTools.show(e.getComponent(), 0, e.getComponent().getHeight());
            }
        });

        toolBar.getBtnEditItem().addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                editRecord();
            }
        });

        miAddItemFromClassifier.addActionListener(a -> {
            if (manager.importPreOrder(document, PreOrderImportType.CLASSIFIER_ITEM)) {
                updateContent();
            }
        });

        miAddArticleFromClassifier.addActionListener(a -> {
            if (manager.importPreOrder(document, PreOrderImportType.CLASSIFIER_ARTICLE)) {
                updateContent();
            }
        });

        miAddFromExcel.addActionListener(a -> {
            if (manager.importPreOrder(document, PreOrderImportType.EXCEL)) {
                updateContent();
            }
        });

        miAddFromOrder.addActionListener(a -> {
            if (manager.importPreOrder(document, PreOrderImportType.ORDER)) {
                updateContent();
            }
        });

        miAddFromSaleDocument.addActionListener(a -> {
            if (manager.importPreOrder(document, PreOrderImportType.SALE_DOCUMENT)) {
                updateContent();
            }

        });

        toolBar.getBtnDeleteItem().addActionListener(a -> {
            deleteRecord();
        });

        btnShowTree.addActionListener(a -> {
            if (btnShowTree.isSelected()) {
                spVertical.resetToPreferredSizes();
                pCatalog.setVisible(true);
            } else {
                pCatalog.setVisible(false);
            }
        });

        btnCalculate.addActionListener(a -> {
            calculateDocument();
        });

        btnInformation.addActionListener(a -> {
            editDocument();
        });

/*        btnPrint.addActionListener(a -> {



            SaleDocumentShippingReport report = manager.prepareDataForReport(document.getId());
            if (report != null) {
                //new ProtocolBelPostReport(report);
                CommonFormatDBF dbf = new CommonFormatDBF(Settings.TEMPORARY_DIR, "test_dbf");
                dbf.connect();
                dbf.addItem(report);
                dbf.disconnect();
            }

        });*/

        btnPrint.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                if (btnPrint.isEnabled()) {
                    popupExport.show(e.getComponent(), 0, e.getComponent().getHeight());
                }
            }
        });

        btnSendEMail.addActionListener(a -> {
            manager.sendDocumentAsEMail(document);
        });

        miAnnexDocument.addActionListener(a -> {
            createAnnexDocument();
        });

        miInvoice.addActionListener(a -> {
            createInvoiceDocument();
        });

        miProtocolDocument.addActionListener(a -> {
            createProtocolDocument();
        });

        miInternetMarkt.addActionListener(a -> {
            createExportToInternetMarket();
        });

        btnCalculator.addActionListener(a -> {
            PreOrderCalculatorMode calculatorMode = new PreOrderCalculatorMode(controller, document, dataList);
            if (calculatorMode.showMode()) {
                calculateDocument();
            }
        });

        miApplyDiscount.addActionListener(a -> {
            final int answer = Dialogs.showQuestionDialog("Применить скидки предварительного заказа в документ отгрузки продукции ? ", "Применение скидок к документу");
            if (answer == 0) {
                applyDiscount();
            }
        });
    }

    private void createInvoiceDocument() {
        SaleDocumentReport report = manager.prepareDataForReport(document.getId());
        if (report == null) {
            return;
        }

        new ContractorInvoiceReport(report);
    }

    private void createProtocolDocument() {
        SaleDocumentReport report = manager.prepareDataForReport(document.getId());
        if (report == null) {
            return;
        }

        new ProtocolBelPostReport(report);
    }

    private void createExportToInternetMarket() {
        JFileChooser fc = new JFileChooser();
        fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        if (fc.showSaveDialog(controller.getMainForm()) == JFileChooser.APPROVE_OPTION) {

            SaleDocumentDataProvider provider = new SaleDocumentDataProvider();
            SaleDocumentReport report = manager.prepareDataForReport(document.getId());
            if (report == null) {
                return;
            }

            provider.prepareMaterialComposition(report);
            new InternetMarketExportReport(report, fc.getSelectedFile().getPath() + File.separator);
        }
    }


    private void createAnnexDocument() {
        SaleDocumentReport report = manager.prepareDataForReport(document.getId());
        if (report == null) {
            return;
        }

        if (document.getCurrencyId() > 1) {
            // Экспортное приложение
            new AnnexTTNExportReport(report);
        } else {

            // Если нет скидки
            if (document.getDiscountType() == 0) {
                // Простое приложение
                new AnnexTTNReport(report);
            } else {
                new AnnexTTNProductDiscountReport(report);
            }

        }
    }

    private void calculateDocument() {
        PreOrderSaleDocument document_ = manager.getPreOrderSaleDocumentByDocumentId(document.getId());
        PreOrderSaleDocumentCalculator calculator = PreOrderSaleDocumentCalculator.getInstance();
        if (document_ != null) {
            if (calculator.calculate(document_, document, dataList)) {
                DaoFactory factory = DaoFactory.getInstance();
                ICommonDaoThread dao = factory.getCommonDaoThread();
                try {
                    dao.updateEntityThread(document_);
                    needUpdate = true;
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                updateContent();
            }
        }
    }

    private void viewDetail(PreOrderSaleDocumentItemView item) {

    }

    private void applyDiscount() {
        SaleDocumentPicker picker = new SaleDocumentPicker(controller, true);
        SaleDocumentEntity entity = picker.selectSaleDocument(document.getContractorCode(), DateUtils.getFirstDay(document.getDocumentDate()), true);

        if (entity != null) {
            final int answer = Dialogs.showQuestionDialog("Документу отгрузки №"
                    + entity.getDocumentNumber()
                    + " будут установлены скидки из предварительного заказа, продолжить ?", "Применение скидок к документу");

            if (answer == 0) {

                class Task extends BackgroundTask {
                    public Task(final String messageText) {
                        super(messageText);
                    }

                    @Override
                    protected Boolean doInBackground() throws Exception {
                        boolean errorResult = false;
                        // Формируем мапу
                        HashMap<String, PreOrderSaleDocumentItemView> map = new HashMap<>();

                        SaleDocumentManager manager = new SaleDocumentManager();
                        SaleDocument saleDocument = manager.getSaleDocumentById(entity.getId());

                        for (PreOrderSaleDocumentItemView item : dataList) {
                            String key = item.getProductId() + "_" + item.getItemColor();
                            map.put(key, item);
                        }

                        List<String> errorList = new ArrayList<>();

                        // Апдейтим изделия документа на актуальный скидон
                        for (SaleDocumentItem item : saleDocument.getDetailList()) {
                            String key = item.getItem() + "_" + item.getItemColor();
                            PreOrderSaleDocumentItemView item_ = map.get(key);
                            if (item_ != null) {
                                item.setDiscount(String.valueOf((int) item_.getDiscount()));
                            } else {
                                errorList.add("Изделие с ключем " + key + " не найдено в документе");
                                errorResult = true;
                            }
                        }

                        if (!errorResult) {
                            try {
                                saleDocument.setDiscountType(3);
                                DocumentUpdater updater = new DatabaseDocumentUpdater();
                                updater.updateDocument(saleDocument);
                                SaleDocumentManager.checkDocument(saleDocument.getId(), false);
                            } catch (Exception e) {
                                e.printStackTrace();
                                JOptionPane.showMessageDialog(null, "Ошибка сохранения документа");
                            }
                        } else {

                            hideProgressBar();

                            StringBuilder temp = new StringBuilder();
                            for (String st : errorList) {
                                temp.append(st).append("\n");
                            }
                            CustomDialog dialog = new CustomDialog(controller);
                            String text = "<font color=\"green\">Некоторые строки документа не обработаны</font>" +
                                    "<p align=\"center\"><font color=\"black\">При совершении операции обнаружено </font>" +
                                    "<font color=\"blue\">" + errorList.size() + "</font><font color=\"black\"> несоответствий </font>";
                            dialog.setInformation("Ошибки в процессе присвоения скидок", text, temp.toString());
                            dialog.setVisible(true);
                        }
                        return true;
                    }
                }
                Task task = new Task("Обработка данных запроса...");
                task.executeTask();
            }
        }
    }

    @Override
    public void updateContent() {

        class Task extends BackgroundTask {
            public Task(final String messageText) {
                super(messageText);
            }

            @Override
            protected Boolean doInBackground() throws Exception {
                DaoFactory<PreOrderSaleDocumentItemView> factory = DaoFactory.getInstance();
                IGenericDao<PreOrderSaleDocumentItemView> dao = factory.getGenericDao();
                List<QueryProperty> criteria = new ArrayList<>();
                criteria.add(new QueryProperty("document", document.getId()));
                List<PreOrderSaleDocumentItemView> list = null;
                dataList.clear();
                try {
                    list = dao.getEntityListByNamedQuery(PreOrderSaleDocumentItemView.class, "PreOrderSaleDocumentItemView.findByDocumentId", criteria);
                    dataList.addAll(list);
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                document = manager.getPreOrderSaleDocumentViewById(document.getId());
                gvDetails.updateViewPort();
                updateStatusInformation();
                return true;
            }
        }

        Task task = new Task("Получение каталога продукции...");
        task.executeTask();
    }

    private void updateStatusInformation() {

        final TableColumnModel columnModel = gvDetails.getTable().getColumnModel();

        if (document.getCurrencyId() > 1) {
            for (int c = 14; c <= 17; c++) {
                columnModel.getColumn(c).setMaxWidth(60);
                columnModel.getColumn(c).setMinWidth(60);
                columnModel.getColumn(c).setPreferredWidth(60);
            }
        } else {
            for (int c = 14; c <= 17; c++) {
                columnModel.getColumn(c).setMaxWidth(0);
                columnModel.getColumn(c).setMinWidth(0);
                columnModel.getColumn(c).setPreferredWidth(0);
            }
        }


        if (document.getCurrencyId() > 1) {

            informationPanel.getSecondInfoPanel().setVisible(true);
            CurrencyName currency = SaleDocumentManager.getCurrencyNameById(document.getCurrencyId());

            informationPanel.getSecondInfoPanel().setCurrencyName(currency.getName() + " (" + document.getCurrencyRate1() + " руб.)");

            informationPanel.getFirstInfoPanel().setCurrencyName("Бел. руб. (" + document.getCurrencyRate2() + " руб.)");

        } else {
            informationPanel.getSecondInfoPanel().setVisible(false);
            informationPanel.getFirstInfoPanel().setCurrencyName("Бел. руб.");
        }

        // Проверка документа на детский/взрослый в документе
        informationPanel.updateType(false);

        // Проверка документа на несорт в документе сорта
        informationPanel.updateNoSort(false);

        SaleDocumentBase documentBase = new SaleDocumentBase();
        documentBase.setDiscountType(document.getDiscountType());
        documentBase.setDiscountValue((float) document.getDiscountValue());

        //documentBase.
        //documentBase.setDocumentType("");
        documentBase.setDocumentVATType(document.getVatType() > 0 ? document.getVatType() : 2);
        documentBase.setDocumentVatValue((float) document.getVatValue());

        informationPanel.setZeroPriceControl(false);
        TotalSummingUp summingUp = manager.doSummingUp(dataList);
        informationPanel.updateInformation(summingUp, documentBase);

        informationPanel.setSummaryInformation("Всего единиц : " + summingUp.getAmount());
    }

    @Override
    public void addRecord() {
    }

    @Override
    public void editRecord() {
        PreOrderSaleDocumentItemView selectedItem = gvDetails.getSelectedItem();
        if (selectedItem != null) {
            if (manager.editProduct(selectedItem)) {
                updateContent();
            }
        }
    }

    @Override
    public void deleteRecord() {
        PreOrderSaleDocumentItemView selectedItem = gvDetails.getSelectedItem();
        if (selectedItem != null) {
            final int answer = Dialogs.showDeleteDialog(selectedItem.getItemInformation());
            if (answer == 0) {
                try {
                    DaoFactory factory = DaoFactory.getInstance();
                    ICommonDao dao = factory.getCommonDao();
                    dao.deleteEntity(PreOrderSaleDocumentItemBase.class, selectedItem.getId());
                    updateContent();
                } catch (Exception e) {
                    MainController.exception(e, "Ошибка удаления записи");
                }
            }
        }
    }

    @Override
    public void viewRecord() {
        viewDetail(gvDetails.getSelectedItem());
    }

    private void getCustomUpdate(ClassifierNode node) {
        DaoFactory<PreOrderSaleDocumentItemView> factory = DaoFactory.getInstance();
        IGenericDao<PreOrderSaleDocumentItemView> dao = factory.getGenericDao();
        try {
            dataList.clear();
            List<QueryProperty> criteria = new ArrayList<>();
            switch (node.getType()) {
                case ROOT:
                    criteria.add(new QueryProperty("document", document.getId()));
                    dataList.addAll(dao.getEntityListByNamedQuery(PreOrderSaleDocumentItemView.class, "PreOrderSaleDocumentItemView.findAllByDocumentId", criteria));
                    break;
                case GROUP:
                    criteria.add(new QueryProperty("category", node.getCode() + "%"));
                    criteria.add(new QueryProperty("document", document.getId()));
                    dataList.addAll(dao.getEntityListByNamedQuery(PreOrderSaleDocumentItemView.class, "PreOrderSaleDocumentItemView.findByDocumentIdAndGroup", criteria));
                    break;
                case CATEGORY:
                    criteria.add(new QueryProperty("category", node.getCode() + "%"));
                    criteria.add(new QueryProperty("document", document.getId()));
                    dataList.addAll(dao.getEntityListByNamedQuery(PreOrderSaleDocumentItemView.class, "PreOrderSaleDocumentItemView.findByDocumentIdAndCategory", criteria));
            }
        } catch (final Exception e) {
            e.printStackTrace();
        }

        selectedProductNode = node;
        gvDetails.updateViewPort();
    }

    private void editDocument() {
        BaseEditorDialog editor = new BaseEditorDialog(controller,
                RecordOperationType.EDIT);
        EditingPane editingPane_ = new PreOrderSaleDocumentEditor(frameViewPort);
        editor.setParentTitle("Документ предварительного заказа");
        if (document != null) {
            PreOrderSaleDocumentBase editableItem = PreOrderSaleDocumentManager.getDocumentById(document.getId());
            if (editableItem != null) {
                editingPane_.setSourceEntity(editableItem);
                editor.setEditorPane(editingPane_);
                if (editor.showModal()) {
                    try {
                        final DaoFactory factory = DaoFactory.getInstance();
                        final ICommonDao dao = factory.getCommonDao();
                        Object o = editingPane_.getSourceEntity();
                        dao.updateEntity(o);
                    } catch (final SQLException e) {
                        e.printStackTrace();
                    }
                    updateContent();
                }
            }
        }
    }


    public boolean showMode() {
        frameViewPort.getFrameControl().showFrame();
        return true;
    }

}


