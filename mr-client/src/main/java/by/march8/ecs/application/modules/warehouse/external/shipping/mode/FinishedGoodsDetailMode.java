package by.march8.ecs.application.modules.warehouse.external.shipping.mode;

import by.gomel.freedev.ucframework.uccore.enums.MarchWindowType;
import by.gomel.freedev.ucframework.uccore.enums.RecordOperationType;
import by.gomel.freedev.ucframework.uccore.enums.RightEnum;
import by.gomel.freedev.ucframework.uccore.modes.abstracts.AbstractFunctionalMode;
import by.gomel.freedev.ucframework.uccore.viewport.frameviewport.FrameViewPort;
import by.gomel.freedev.ucframework.uccore.viewport.frameviewport.abstracts.ButtonActionAdapter;
import by.gomel.freedev.ucframework.uccore.viewport.gridviewport.GridViewPort;
import by.gomel.freedev.ucframework.uccore.viewport.gridviewport.abstracts.TableEventAdapter;
import by.gomel.freedev.ucframework.ucdao.DaoFactory;
import by.gomel.freedev.ucframework.ucdao.interfaces.ICommonDao;
import by.gomel.freedev.ucframework.ucdao.interfaces.ICommonDaoThread;
import by.gomel.freedev.ucframework.ucdao.interfaces.IGenericDao;
import by.gomel.freedev.ucframework.ucdao.model.QueryProperty;
import by.gomel.freedev.ucframework.ucswing.dialog.BaseDialog;
import by.gomel.freedev.ucframework.ucswing.dialog.BaseEditorDialog;
import by.gomel.freedev.ucframework.ucswing.dialog.CustomDialog;
import by.gomel.freedev.ucframework.ucswing.dialog.DateSelectorDialog;
import by.gomel.freedev.ucframework.ucswing.dialog.Dialogs;
import by.gomel.freedev.ucframework.ucswing.uicontrols.EditingPane;
import by.gomel.freedev.ucframework.ucswing.uicontrols.UCImageLabel;
import by.gomel.freedev.ucframework.ucswing.uicontrols.UCToolBar;
import by.march8.api.BaseEntity;
import by.march8.api.utils.DateUtils;
import by.march8.ecs.MainController;
import by.march8.ecs.application.modules.references.currency.mode.CurrencyRateMonitorMode;
import by.march8.ecs.application.modules.marketing.model.ModelImageSize;
import by.march8.ecs.application.modules.terminal.TerminalHandler;
import by.march8.ecs.application.modules.warehouse.external.shipping.dao.DatabaseDocumentUpdater;
import by.march8.ecs.application.modules.warehouse.external.shipping.dao.DocumentUpdater;
import by.march8.ecs.application.modules.warehouse.external.shipping.dao.SaleDocumentJDBC;
import by.march8.ecs.application.modules.warehouse.external.shipping.editor.DrivingDocumentEditor;
import by.march8.ecs.application.modules.warehouse.external.shipping.editor.EanCodeSaleDocumentEditor;
import by.march8.ecs.application.modules.warehouse.external.shipping.editor.ManualInputItemEditor;
import by.march8.ecs.application.modules.warehouse.external.shipping.editor.SaleDocumentEditor;
import by.march8.ecs.application.modules.warehouse.external.shipping.editor.SaleDocumentItemsTableEditor;
import by.march8.ecs.application.modules.warehouse.external.shipping.editor.SaleDocumentRefundEditor;
import by.march8.ecs.application.modules.warehouse.external.shipping.enums.SaleDocumentStatus;
import by.march8.ecs.application.modules.warehouse.external.shipping.forms.DocumentTypePickDialog;
import by.march8.ecs.application.modules.warehouse.external.shipping.forms.SaleDocumentInformationPanel;
import by.march8.ecs.application.modules.warehouse.external.shipping.manager.SaleDocumentCalculator;
import by.march8.ecs.application.modules.warehouse.external.shipping.manager.SaleDocumentDataProvider;
import by.march8.ecs.application.modules.warehouse.external.shipping.manager.SaleDocumentManager;
import by.march8.ecs.application.modules.warehouse.external.shipping.model.CallBack;
import by.march8.ecs.application.modules.warehouse.external.shipping.model.CurrencyName;
import by.march8.ecs.application.modules.warehouse.external.shipping.model.DocumentTypePreset;
import by.march8.ecs.application.modules.warehouse.external.shipping.model.EanCodeSaleDocumentItem;
import by.march8.ecs.application.modules.warehouse.external.shipping.model.FinishedGoodsDetailCellRenderer;
import by.march8.ecs.application.modules.warehouse.external.shipping.model.SaleDocumentReport;
import by.march8.ecs.application.modules.warehouse.external.shipping.model.SaleDocumentView;
import by.march8.ecs.application.modules.warehouse.external.shipping.model.TotalSummingUp;
import by.march8.ecs.application.modules.warehouse.external.shipping.reports.AbstractInvoiceReport;
import by.march8.ecs.application.modules.warehouse.external.shipping.reports.AdditionHTMLReport;
import by.march8.ecs.application.modules.warehouse.external.shipping.reports.AdjustmentDocumentExportReport;
import by.march8.ecs.application.modules.warehouse.external.shipping.reports.AdjustmentDocumentReport;
import by.march8.ecs.application.modules.warehouse.external.shipping.reports.AnnexSimpleTTNDocumentDiscountReport;
import by.march8.ecs.application.modules.warehouse.external.shipping.reports.AnnexTTNDocumentDiscountReport;
import by.march8.ecs.application.modules.warehouse.external.shipping.reports.AnnexTTNExportReport;
import by.march8.ecs.application.modules.warehouse.external.shipping.reports.AnnexTTNProductDiscountReport;
import by.march8.ecs.application.modules.warehouse.external.shipping.reports.AnnexTTNReport;
import by.march8.ecs.application.modules.warehouse.external.shipping.reports.AnnexTTNRetailReport;
import by.march8.ecs.application.modules.warehouse.external.shipping.reports.AnnexTTNTradeReport;
import by.march8.ecs.application.modules.warehouse.external.shipping.reports.InternetMarketExportReport;
import by.march8.ecs.application.modules.warehouse.external.shipping.reports.InvoiceReport;
import by.march8.ecs.application.modules.warehouse.external.shipping.reports.InvoiceReportBy;
import by.march8.ecs.application.modules.warehouse.external.shipping.reports.ReferenceTTNExportReport;
import by.march8.ecs.application.modules.warehouse.external.shipping.reports.ReferenceTTNRetailReport;
import by.march8.ecs.application.modules.warehouse.external.shipping.reports.SimpleTTNReport;
import by.march8.ecs.application.modules.warehouse.external.shipping.services.SaleDocumentHistoryService;
import by.march8.ecs.framework.common.BackgroundTask;
import by.march8.ecs.framework.common.Settings;
import by.march8.ecs.framework.common.model.Breaker;
import by.march8.ecs.framework.sdk.hardware.CipherReader;
import by.march8.ecs.services.images.ImageService;
import by.march8.ecs.services.images.ModelImageServiceDB;
import by.march8.entities.warehouse.InvoiceType;
import by.march8.entities.warehouse.SaleDocument;
import by.march8.entities.warehouse.SaleDocumentBase;
import by.march8.entities.warehouse.SaleDocumentContractInformation;
import by.march8.entities.warehouse.SaleDocumentDriving;
import by.march8.entities.warehouse.SaleDocumentDrivingEntity;
import by.march8.entities.warehouse.SaleDocumentEntity;
import by.march8.entities.warehouse.SaleDocumentItem;
import by.march8.entities.warehouse.SaleDocumentItemBase;
import by.march8.entities.warehouse.SaleDocumentItemView;
import dept.ves.mode.SaleDocumentPicker;

import javax.swing.*;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.lang.reflect.Constructor;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Спецификация накладной.
 * Часть функционального режима оператора скалада готовой продукции
 *
 * @author Andy 30.09.2015.
 */
@SuppressWarnings("all")
public class FinishedGoodsDetailMode extends AbstractFunctionalMode implements CallBack, BreakableThread {

    private static final int IMAGE_COLUMN = 19;
    private RightEnum right;
    private SaleDocumentView documentView;
    private SaleDocument document;
    private SaleDocumentBase documentBase;
    private SaleDocumentManager manager;
    private ArrayList<Object> data;
    private JButton btnTerminal;
    private JButton btnImport;
    private JButton btnInformation;
    private JButton btnAddItem;
    private JButton btnEditItem;
    private JButton btnDeleteItem;
    private JButton btnTableEditor;
    private JButton btnFixEanCodes;
    private JButton btnCurrency;
    private JButton btnCalculate;
    private JButton btnDocumentation;
    private JButton btnPrint;
    private JButton btnExport;
    private JButton btnAdditional;
    private JPopupMenu popupAdditional;
    private JPopupMenu popupPrint;
    private JPopupMenu popupExport;
    private JMenuItem miCheckDocument;
    private JMenuItem miCloseDocument;
    private JMenuItem miDeleteDocument;
    private JMenuItem miUpdatePrice;
    private JMenuItem miExportWeb;
    private JButton btnUpdate;
    private JMenuItem miPreview;
    private JMenuItem miCreateDocument;
    private JMenu mPrice;
    private JMenu mDocument;
    private EditingPane drivingDocumentEditor;
    private JPanel pFooter;
    private JPanel pInformation;
    private JPanel pPrice;
    private JPanel pControl;
    private EditingPane editingPane;
    private DateSelectorDialog dialogDateSelector;
    private SaleDocumentInformationPanel informationPanel;
    private TotalSummingUp summingUp;
    private boolean documentIsChanged = false;
    private boolean documentIsRecalculated = false;
    private boolean detailIsChanged = false;
    private boolean needToUpdate = false;
    private boolean manualChange = false;
    private SaleDocumentRefundEditor cellEditor;
    private String userSign;
    private JPopupMenu mEanEditor;
    private JMenuItem miEanEdit;
    private JMenuItem miUpdateDiscount;


    /**
     * Клавный конструктор формы. Прилетает ссылка на контроллер и объект SaleDocumentView
     * активный в гриде, из его вытаскиваем id документа и уже в этой части функц. режима
     * работаем с реальным объектом документа SaleDocument
     *
     * @param mainController главный контроллер приложения
     * @param object         ссылка на активный объект документа SaleDocumentView
     */
    public FinishedGoodsDetailMode(MainController mainController, Object object) {
        controller = mainController;
        documentView = (SaleDocumentView) object;

        String editorProperty = controller.getWorkSession().getUserProperty("SaleDocument");

        userSign = "";
        if (editorProperty == null) {
            editorProperty = "";
        } else if (editorProperty.trim().equals("sale-dept")) {
            userSign = "sale-dept";
        }

        String deptName = "Склад ГП";

        switch (userSign) {
            case "sale-dept":
                deptName = "Отдел сбыта";
                break;
            default:
                deptName = "Склад ГП";
                break;
        }


        modeName = "Спецификация накладной отгрузки №" + documentView.getDocumentNumber() + " : " + deptName;
        frameViewPort = new FrameViewPort(mainController, MarchWindowType.DIALOGSIMPLE);
        frameViewPort.getFrameControl().setTitleFrame(modeName);

        right = controller.getRight(modeName);

        initComponents();

        gridViewPort = new GridViewPort(SaleDocumentItemView.class, false);
        // Устанавливаем кастомный рендер гриду
        gridViewPort.setCustomCellRender(new FinishedGoodsDetailCellRenderer());
        cellEditor = new SaleDocumentRefundEditor(gridViewPort, documentBase);

        gridViewPort.setCustomCellEditor(cellEditor);
        gridViewPort.setIgnoreNotEditableCells(true);
        gridViewPort.getTable().setRowSelectionAllowed(true);

        data = gridViewPort.getDataModel();
        frameViewPort.setGridViewPort(gridViewPort);

        if (userSign.equals("sale-dept")) {
            //gridViewPort.getTable().setRowHeight(120);
            final TableColumnModel columnModel = gridViewPort.getTable().getColumnModel();
            columnModel.getColumn(IMAGE_COLUMN).setMinWidth(0);
            columnModel.getColumn(IMAGE_COLUMN).setPreferredWidth(0);
            columnModel.getColumn(IMAGE_COLUMN).setMaxWidth(0);

            btnAddItem.setVisible(false);
            btnEditItem.setVisible(false);
            btnDeleteItem.setVisible(false);
            btnAdditional.setVisible(false);
            btnInformation.setVisible(false);
            btnTableEditor.setVisible(false);
            btnCalculate.setVisible(false);
            btnTerminal.setVisible(false);
            btnImport.setVisible(false);
            btnDocumentation.setVisible(false);
            btnPrint.setVisible(false);
            btnUpdate.setVisible(false);

            btnExport.setVisible(true);

        } else {
            final TableColumnModel columnModel = gridViewPort.getTable().getColumnModel();
            columnModel.getColumn(IMAGE_COLUMN).setMinWidth(0);
            columnModel.getColumn(IMAGE_COLUMN).setPreferredWidth(0);
            columnModel.getColumn(IMAGE_COLUMN).setMaxWidth(0);

            btnExport.setVisible(false);
        }

        editorProperty = controller.getWorkSession().getUserProperty("SaleDocumentTools");
        if (editorProperty != null) {
            if (editorProperty.trim().equals("admin-tools")) {
                System.out.println("*********************************************************");
                initAdminTools();
            }
        }

        // События в гриде
        gridViewPort.setTableEventHandler(new TableEventAdapter() {
            @Override
            public void onDoubleClick(int rowIndex, int columnIndex, Object object) {
/*                SaleDocumentItemView selectedItem = (SaleDocumentItemView) object;
                if (selectedItem != null) {
                    // Вызываем форму картинок по модели
                    ImageSelectorDialog selector = new ImageSelectorDialog(controller, selectedItem);
                    selector.selectImage();
                }*/
            }

            @Override
            public void onSelectChanged(final int rowIndex, final Object object) {
                SaleDocumentItemView selectedItem = (SaleDocumentItemView) object;
                if (selectedItem != null) {
                    System.out.println("Активное изделие [" + selectedItem.getId() + "] для документа [" + selectedItem.getDocumentId() + "]");
                }
            }
        });

        manager = new SaleDocumentManager();
        editingPane = new ManualInputItemEditor(frameViewPort);

        if (documentView != null) {
            gridViewPort.primaryInitialization();
            updateContent();
            frameViewPort.getFrameControl().setFrameSize(new Dimension(1100, 700));
        }
    }

    /**
     * Инициализация компонентов на форме, в основном менюшек и кнопок
     * Инициализация компонентов на форме, в основном менюшек и кнопок
     */
    private void initComponents() {
        final UCToolBar toolBar = frameViewPort.getFrameRegion().getToolBar();
        toolBar.getBtnNewItem().setVisible(false);
        toolBar.getBtnEditItem().setVisible(false);
        toolBar.getBtnDeleteItem().setVisible(false);
        toolBar.getBtnReport().setVisible(false);
        toolBar.setVisibleSearchControls(false);

        pFooter = new JPanel(new BorderLayout());
        pFooter.setPreferredSize(new Dimension(100, 270));
        pFooter.setBackground(Color.black);

        frameViewPort.getFrameRegion().getBottomContentPanel().setLayout(new BorderLayout());
        frameViewPort.getFrameRegion().getBottomContentPanel().add(pFooter, BorderLayout.SOUTH);

        if (frameViewPort.getFrame() instanceof BaseDialog) {
            BaseDialog dialog = (BaseDialog) frameViewPort.getFrame();
            frameViewPort.getButtonControl().getButtonPanel().setVisible(false);
            dialog.setResizable(true);
        }

        informationPanel = new SaleDocumentInformationPanel();

        pControl = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        pControl.setPreferredSize(new Dimension(0, 37));

        pPrice = new JPanel(new FlowLayout(FlowLayout.LEFT));
        //pPrice.add(new Button("Кнопка"));
        pPrice.add(informationPanel);
        pPrice.setPreferredSize(new Dimension(0, 200));

        pFooter.add(pPrice, BorderLayout.CENTER);
        pFooter.add(pControl, BorderLayout.SOUTH);

        btnInformation = new JButton();
        btnInformation.setIcon(new ImageIcon(MainController.getRunPath() + "/Img/document_24.png", "Информация о документе"));

        btnTerminal = new JButton();
        btnTerminal.setIcon(new ImageIcon(MainController.getRunPath() + "/Img/scanner.png", "Получить со сканера"));

        btnImport = new JButton();
        btnImport.setIcon(new ImageIcon(MainController.getRunPath() + "/Img/import_24.png", "Импорт из файла"));

        btnCurrency = new JButton();
        btnCurrency.setIcon(new ImageIcon(MainController.getRunPath() + "/Img/currency_24.png", "Монитор курсов валюты"));
        btnCurrency.setToolTipText("Монитор курсво валюты");

        btnCalculate = new JButton();
        btnCalculate.setIcon(new ImageIcon(MainController.getRunPath() + "/Img/sum_24.png", "Расчет накладной"));
        btnCalculate.setText("Расчет накладной");

        btnDocumentation = new JButton();
        btnDocumentation.setIcon(new ImageIcon(MainController.getRunPath() + "/Img/car_24.png", "Путевой лист"));
        btnDocumentation.setText("Путевой лист");

        btnPrint = new JButton();
        btnPrint.setIcon(new ImageIcon(MainController.getRunPath() + "/Img/printer.png", "Печать документа"));
        btnPrint.setText("Сформировать");

        btnExport = new JButton();
        btnExport.setIcon(new ImageIcon(MainController.getRunPath() + "/Img/addressbook24.png", "Экспорт"));
        btnExport.setText("Экспорт");

        btnFixEanCodes = new JButton();
        btnFixEanCodes.setIcon(new ImageIcon(MainController.getRunPath() + "/Img/addressbook24.png", "FIX EAN"));
        btnFixEanCodes.setText("ФИКС EAN");

        btnAddItem = new JButton();
        btnAddItem.setIcon(new ImageIcon(MainController.getRunPath() + "/Img/add24.png", "Действия на документом"));

        btnEditItem = new JButton();
        btnEditItem.setIcon(new ImageIcon(MainController.getRunPath() + "/Img/edit24.png", "Действия на документом"));

        btnDeleteItem = new JButton();
        btnDeleteItem.setIcon(new ImageIcon(MainController.getRunPath() + "/Img/delete24.png", "Действия на документом"));

        btnTableEditor = new JButton();
        btnTableEditor.setIcon(new ImageIcon(MainController.getRunPath() + "/Img/table_edit_24.png", "Редактирование в таблице"));

        btnAdditional = new JButton();
        btnAdditional.setIcon(new ImageIcon(MainController.getRunPath() + "/Img/money_24.png", "Дополнительно"));
        btnAdditional.setText("Дополнительно");

        btnUpdate = new JButton();
        btnUpdate.setIcon(new ImageIcon(MainController.getRunPath() + "/Img/update_data.png", "Обновить"));
        btnUpdate.setText("Обновить");
        //btnAdditional.setEnabled(false);

        ButtonGroup bg = new ButtonGroup(); // создаем группу взаимного исключения
        //bg.add(tbImage);

        toolBar.add(btnInformation);
        toolBar.add(btnTerminal);
        toolBar.add(btnImport);
        toolBar.add(btnCurrency);
        toolBar.addSeparator();
        toolBar.add(btnAddItem);
        toolBar.add(btnEditItem);
        toolBar.add(btnDeleteItem);
        toolBar.addSeparator();
        toolBar.add(btnTableEditor);
        toolBar.addSeparator();
        toolBar.add(btnUpdate);
        toolBar.addSeparator();
        //toolBar.add(tbImage);

        btnAdditional.setPreferredSize(Settings.BUTTON_HALF_BIG_SIZE);
        btnCalculate.setPreferredSize(Settings.BUTTON_HALF_BIG_SIZE);
        btnDocumentation.setPreferredSize(Settings.BUTTON_HALF_BIG_SIZE);
        btnPrint.setPreferredSize(Settings.BUTTON_HALF_BIG_SIZE);
        btnExport.setPreferredSize(Settings.BUTTON_HALF_BIG_SIZE);

        pControl.add(btnAdditional);
        pControl.add(btnCalculate);
        pControl.add(btnDocumentation);
        pControl.add(btnPrint);
        pControl.add(btnExport);
        // pControl.add(btnFixEanCodes);

        miCheckDocument = new JMenuItem("Проверить документ");
        miCheckDocument.setEnabled(false);

        mPrice = new JMenu("Цена изделия");
        miUpdatePrice = new JMenuItem("Обновить цену изделий из классификатора");
        miUpdateDiscount = new JMenuItem("Получить размеры скидок");
        mPrice.add(miUpdatePrice);
        mPrice.add(miUpdateDiscount);

        mDocument = new JMenu("Документ");
        miCloseDocument = new JMenuItem("Закрыть документ");
        miDeleteDocument = new JMenuItem("Удалить документ");
        mDocument.add(miCloseDocument);
        mDocument.add(miDeleteDocument);

        btnFixEanCodes.addActionListener(a -> {
            try {
                UpdateEanCodeForDocument();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        popupAdditional = new JPopupMenu();

        btnAdditional.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                if (!btnAdditional.isEnabled()) {
                    return;
                }
                popupAdditional.show(e.getComponent(), 0, e.getComponent().getHeight());

                if (documentBase != null) {
                    //miCloseDocument.setVisible(true);
                    //miDeleteDocument.setVisible(false);

                    if (manager.isDocumentOpened(documentBase)) {
                        miDeleteDocument.setVisible(true);
                        miCloseDocument.setText("Закрыть документ");
                        miUpdatePrice.setVisible(true);
                        mPrice.setEnabled(true);
                    } else if (manager.isDocumentClosed(documentBase)) {
                        miDeleteDocument.setVisible(false);
                        miCloseDocument.setText("Открыть документ");
                        miUpdatePrice.setVisible(false);
                        mPrice.setEnabled(false);
                    }

                    if (manager.isDocumentDeleted(documentBase)) {
                        miCloseDocument.setVisible(false);
                        miDeleteDocument.setText("Восстановить документ");
                        miUpdatePrice.setVisible(false);
                        mPrice.setEnabled(true);
                    } else if (!manager.isDocumentClosed(documentBase)) {
                        miDeleteDocument.setText("Удалить документ");
                        miUpdatePrice.setVisible(true);
                        miCloseDocument.setVisible(true);
                    } else {

                    }

                    if (manager.isExportDocument(documentBase) && manager.isDocumentRefund(documentBase)
                            && manager.isDocumentOpened(documentBase)) {
                        miUpdateDiscount.setVisible(true);
                    } else {
                        miUpdateDiscount.setVisible(false);
                    }
                }

            }
        });

        popupAdditional.add(miCheckDocument);
        popupAdditional.add(mPrice);
        popupAdditional.add(mDocument);

        popupPrint = new JPopupMenu();
        btnPrint.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                if (btnPrint.isEnabled()) {
                    popupPrint.show(e.getComponent(), 0, e.getComponent().getHeight());
                }
            }
        });

        popupExport = new JPopupMenu();
        btnExport.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                if (btnExport.isEnabled()) {
                    popupExport.show(e.getComponent(), 0, e.getComponent().getHeight());
                }
            }
        });

        btnCurrency.addActionListener(a -> new CurrencyRateMonitorMode(controller));

        miPreview = new JMenuItem("Приложение бригадиру");
        miPreview.addActionListener(e -> preview());
        miPreview.setEnabled(true);

        miCreateDocument = new JMenuItem("Сформировать документ");

        popupPrint.add(miPreview);
        popupPrint.add(miCreateDocument);


        miExportWeb = new JMenuItem("Приложение - каталог");
        miExportWeb.addActionListener(a -> {
            exportToWebPage();
        });

        JMenuItem miExportInternetMarkt = new JMenuItem("Выгрузка для интернет магазина");
        miExportInternetMarkt.addActionListener(a -> {
            exportToInternetMarkt();
        });

        miUpdateDiscount.addActionListener(a -> {
            final int answer = Dialogs.showQuestionDialog("Применить скидки в документ возврата на основании скидок документа отгрузки ? ", "Применение скидок к документу");
            if (answer == 0) {
                updateDiscount();
            }
        });

        //popupExport.add(miExportWeb);
        popupPrint.add(miExportInternetMarkt);
        popupExport.add(miExportInternetMarkt);


        miCloseDocument.setEnabled(true);
        miUpdatePrice.setEnabled(true);

        // Кнопка прием с терминала
        btnTerminal.addActionListener(e -> getDataFromTerminal());
        // Кнопка пересчета доукмента
        btnCalculate.addActionListener(e -> recalculateDocument());
        // Кнопка ПУТЕВОЙ ЛИСТ
        btnDocumentation.addActionListener(e -> drivingDirections());

        miCreateDocument.addActionListener(e -> createDocumentSelector());

        btnAddItem.addActionListener(e -> addRecord());
        btnEditItem.addActionListener(e -> editRecord());
        btnDeleteItem.addActionListener(e -> deleteRecord());

        btnInformation.addActionListener(e -> editDocumentInformation());
        btnTableEditor.addActionListener(e -> activateTableEditor());

        dialogDateSelector = new DateSelectorDialog(controller);

        miCloseDocument.addActionListener(e -> {
            if (documentBase != null) {
                if (miCloseDocument.getText().equals("Закрыть документ")) {
                    if (documentBase.getCheck() != Boolean.TRUE) {
                        Dialogs.showInformationDialog("Перед изменением статуса документ должен быт рассчитан!");
                    } else {
                        // проверка на наличие путевого листа по документу
                        SaleDocumentDrivingEntity driving = manager.getDrivingEntityByDocumentId(documentBase.getId());
                        if (driving == null) {
                            Dialogs.showInformationDialog("<html><div style=\"text-align: left;\">Путевой лист не заполнен<br>" +
                                    "<p>Поле, обязательное для заполнения: " +
                                    "<font color=\"red\"> ДАТА ПУТЕВОГО ЛИСТА</font></div></html>");
                            return;
                        }

                        final int answer = Dialogs.showQuestionDialog("Вы действительно хотите закрыть документ?\n" +
                                "Дальнейшее внесение изменений в документ будет недоступно", "Изменение статуса документа");
                        if (answer == 0) {
                            manager.changeDocumentStatus(documentBase.getId(), SaleDocumentStatus.CLOSED);
                            needToUpdate = true;

                            SaleDocumentHistoryService historyService = SaleDocumentHistoryService.getInstance();
                            historyService.historyCloseDocument(documentBase.getId(), documentBase.getDocumentNumber());

                            updateContent();
                        }
                    }
                } else {
                    final int answer = Dialogs.showQuestionDialog("Вы действительно хотите открыть документ?\n" +
                            "Факт изменения статуса документа будет занесен в журнал событий.", "Изменение статуса документа");
                    if (answer == 0) {
                        manager.changeDocumentStatus(documentBase.getId(), SaleDocumentStatus.PRE_FORMED);
                        needToUpdate = true;

                        SaleDocumentHistoryService historyService = SaleDocumentHistoryService.getInstance();
                        historyService.historyOpenDocument(documentBase.getId(), documentBase.getDocumentNumber());

                        updateContent();
                    }
                }
            }
        });

        miDeleteDocument.addActionListener(e -> {
            if (documentBase != null) {
                if (miDeleteDocument.getText().equals("Удалить документ")) {
                    final int answer = Dialogs.showQuestionDialog("Вы действительно хотите пометить документ на удаление?\n" +
                            "\nДокумент не будет принят к учету.", "Пометить документ на удаление");
                    if (answer == 0) {
                        manager.changeDocumentStatus(documentBase.getId(), SaleDocumentStatus.DELETED);
                        needToUpdate = true;

                        SaleDocumentHistoryService historyService = SaleDocumentHistoryService.getInstance();
                        historyService.historyDeleteDocument(documentBase.getId(), documentBase.getDocumentNumber());

                        updateContent();
                    }
                } else {
                    final int answer = Dialogs.showQuestionDialog("Вы действительно хотите восстановить документ?\n" +
                            "\nСтатус документа будет изменен на [формируется].", "Восстановление документа");
                    if (answer == 0) {
                        manager.changeDocumentStatus(documentBase.getId(), SaleDocumentStatus.PRE_FORMED);
                        needToUpdate = true;

                        SaleDocumentHistoryService historyService = SaleDocumentHistoryService.getInstance();
                        historyService.historyOpenDocument(documentBase.getId(), documentBase.getDocumentNumber());

                        updateContent();
                    }
                }
            }
        });

        miUpdatePrice.addActionListener(e -> {
            final int answer = Dialogs.showQuestionDialog("<html><div style=\"text-align: left;\">Вы действительно хотите получить новые цены на изделия ?<br>" +
                    "<p>Текущие значения цен будут утеряны," +
                    " и<font color=\"red\"> будет произведена группировка изделий!</font></div></html>", "Обновление цен на изделия");
            if (answer == 0) {

                //
                try {
                    SaleDocument document = manager.getSaleDocumentById(documentBase.getId());
                    SaleDocumentDataProvider provider = new SaleDocumentDataProvider();
                    provider.updateEanCodeAndColor(document);
                    DocumentUpdater updater = new DatabaseDocumentUpdater();
                    updater.updateDocument(document);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

                manager.updateSaleDocumentPrice(documentBase.getId(), 1);
                // Изменяем статус расчета по документу как РАСЧИТАН И СОХРАНЕН
                manager.checkDocument(documentBase.getId(), false);
                documentIsChanged = true;
                documentIsRecalculated = false;
                updateContent();
            }
        });

        btnUpdate.addActionListener(a -> {
            updateContent();
        });
    }

    private void updateDiscount() {
        SaleDocumentPicker picker = new SaleDocumentPicker(controller, true);
        SaleDocumentEntity entity = picker.selectClosedSaleDocument(documentBase.getRecipientCode(), common.DateUtils.getFirstDay(documentBase.getDocumentDate()));

        if (entity != null) {
            final int answer = Dialogs.showQuestionDialog("Документу возврата № "
                    + documentBase.getDocumentNumber()
                    + " будут установлены скидки из документа №" + entity.getDocumentNumber() + " , продолжить ?", "Применение скидок к документу");

            if (answer == 0) {

                class Task extends BackgroundTask {
                    public Task(final String messageText) {
                        super(messageText);
                    }

                    @Override
                    protected Boolean doInBackground() throws Exception {
                        boolean errorResult = false;
                        // Формируем мапу
                        HashMap<String, SaleDocumentItem> map = new HashMap<>();

                        SaleDocumentManager manager = new SaleDocumentManager();
                        SaleDocument saleDocument = manager.getSaleDocumentById(entity.getId());

                        for (SaleDocumentItem item : saleDocument.getDetailList()) {
                            String key = item.getItem() + "_" + item.getItemEanCode();
                            map.put(key, item);
                        }

                        List<String> errorList = new ArrayList<>();

                        // Получаем текущий документ возврата
                        SaleDocument refundDocument = manager.getSaleDocumentById(documentBase.getId());


                        // Апдейтим изделия документа на актуальный скидон
                        for (SaleDocumentItem item : refundDocument.getDetailList()) {
                            String key = item.getItem() + "_" + item.getItemEanCode();
                            SaleDocumentItem item_ = map.get(key);
                            if (item_ != null) {
                                item.setDiscount(item_.getDiscount());
                            } else {
                                errorList.add("Изделие с ключем " + key + " не найдено в документе отгрузки");
                                errorResult = true;
                            }
                        }

                        if (!errorResult) {
                            try {
                                refundDocument.setDiscountType(3);
                                refundDocument.setDiscountValue(saleDocument.getDiscountValue());
                                refundDocument.setCurrencyId(saleDocument.getCurrencyId());
                                refundDocument.setCurrencyRateFixed(saleDocument.getCurrencyRateFixed());
                                refundDocument.setCurrencyRateSale(saleDocument.getCurrencyRateSale());

                                DocumentUpdater updater = new DatabaseDocumentUpdater();
                                updater.updateDocument(refundDocument);

                                SaleDocumentManager.checkDocument(refundDocument.getId(), false);
                                documentIsChanged = true;
                                documentIsRecalculated = false;
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

                        updateContent();
                        return true;
                    }
                }

                Task task = new Task("Обработка данных запроса...");
                task.executeTask();
            }
        }

    }

    private void exportToInternetMarkt() {
        JFileChooser fc = new JFileChooser();
        fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        if (fc.showSaveDialog(controller.getMainForm()) == JFileChooser.APPROVE_OPTION) {
            if (document == null) {
                document = manager.getSaleDocumentById(documentBase.getId());
            }

            SaleDocumentDataProvider provider = new SaleDocumentDataProvider();
            SaleDocumentReport reportData = provider.prepareDocument(document);
            provider.prepareMaterialComposition(reportData);
            new InternetMarketExportReport(reportData, fc.getSelectedFile().getPath() + File.separator);
        }
    }

    private void initAdminTools() {
        mEanEditor = new JPopupMenu();
        miEanEdit = new JMenuItem("Корректировка EAN-кода");
        mEanEditor.add(miEanEdit);
        gridViewPort.getTable().addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    Point point = e.getPoint();
                    int column = gridViewPort.getTable().columnAtPoint(point);
                    int row = gridViewPort.getTable().rowAtPoint(point);

                    // выполняем проверку
                    if (column != -1 && row != -1) {
                        gridViewPort.getTable().setColumnSelectionInterval(column, column);
                        gridViewPort.getTable().setRowSelectionInterval(row, row);
                    }

                    mEanEditor.show(e.getComponent(), e.getX(), e.getY());
                }
            }
        });

        miEanEdit.addActionListener(a -> {
            // Создаем пустую форму диалога
            BaseEditorDialog editor = new BaseEditorDialog(controller, RecordOperationType.EDIT);
            EditingPane editPane = new EanCodeSaleDocumentEditor(controller);

            DaoFactory factory = DaoFactory.getInstance();
            // ПОлучаем интерфейс для работы с БД
            ICommonDao dao = factory.getCommonDao();
            editor.setParentTitle("Редактирование EAN кода");

            SaleDocumentContractInformation contractInformation = null;
            // Получаем идентификатор
            SaleDocumentItemView item = (SaleDocumentItemView) gridViewPort.getSelectedItem();
            EanCodeSaleDocumentItem eanCodeItem = null;
            try {
                eanCodeItem = (EanCodeSaleDocumentItem) dao.getEntityById(EanCodeSaleDocumentItem.class, item.getId());
            } catch (SQLException e) {
                e.printStackTrace();
            }

            // Для созданного пустого диалога устанавливаем панель редактирования
            editor.setEditorPane(editPane);
            editPane.setSourceEntity(eanCodeItem);
            // Модально показываем форму и ожидаем закрытия
            if (editor.showModal()) {
                // Форма закрыта со значением true
                try {
                    Object o = editPane.getSourceEntity();
                    SaleDocumentJDBC jdbc = new SaleDocumentJDBC();
                    jdbc.updateEanCode((EanCodeSaleDocumentItem) o);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    private void exportToWebPage() {
        JFileChooser fc = new JFileChooser();
        fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        if (fc.showSaveDialog(controller.getMainForm()) == JFileChooser.APPROVE_OPTION) {

            // Получаем документ если ранее не получен
            if (document == null) {
                document = manager.getSaleDocumentById(documentBase.getId());
            }
            // Создаем провайдера данных
            SaleDocumentDataProvider provider = new SaleDocumentDataProvider();
            // Получаем данные по документу
            SaleDocumentReport reportData = provider.prepareDocument(document);

            AdditionHTMLReport report = new AdditionHTMLReport(reportData);
            report.create(fc.getSelectedFile().getPath() + File.separator);

            SaleDocumentHistoryService historyService = SaleDocumentHistoryService.getInstance();
            historyService.historyCreateWEBDocument(documentBase.getId(), documentBase.getDocumentNumber());
        }
    }

    // Формирование шапки
    private void drivingDirections() {
        // Проверка корректности контрагента на наличие адресов и договоров
        if (!manager.checkContractor(documentBase.getRecipientId())) {
            Dialogs.showInformationDialog("Обнаружены ошибки при заполнении контрагента.\nОформление путевого листа невозможно.");
            return;
        }

        // ПОлучаем путевой лист
        SaleDocumentDriving driving = manager.getDrivingDocumentationByDocumentId(documentBase.getId());
        drivingDocumentEditor = new DrivingDocumentEditor(controller, manager, documentBase);

        // Создаем пустую форму диалога
        BaseEditorDialog editor = new BaseEditorDialog(controller,
                RecordOperationType.EDIT);
        editor.setParentTitle("Путевой лист для накладной " + documentBase.getDocumentNumber());
        drivingDocumentEditor.setSourceEntity(driving);

        if (manager.isDocumentClosed(documentBase)) {
            editor.getBtnSave().setEnabled(false);
        } else {
            editor.getBtnSave().setEnabled(true);
        }

        // Для созданного пустого диалога устанавливаем панель редактирования
        editor.setEditorPane(drivingDocumentEditor);
        // Модально показываем форму и ожидаем закрытия
        if (editor.showModal()) {
            // Форма закрыта со значением true
            // Получаем DAO слой
            final DaoFactory factory = DaoFactory.getInstance();
            // ПОлучаем интерфейс для работы с БД
            final ICommonDao dao = factory.getCommonDao();
            try {
                SaleDocumentDriving drivingDocument = (SaleDocumentDriving) drivingDocumentEditor.getSourceEntity();
                if (drivingDocument.getId() == 0) {
                    // Обновляем сущность в БД
                    dao.saveEntity(drivingDocument);
                } else {
                    // Обновляем сущность в БД
                    dao.updateEntity(drivingDocument);
                }

                if (documentBase.getCheck()) {
                    dao.updateEntity(documentBase);
                    manager.checkDocument(documentBase.getId(), true);
                } else {
                    dao.updateEntity(documentBase);
                }

                SaleDocumentHistoryService historyService = SaleDocumentHistoryService.getInstance();
                historyService.historyEditBSODocument(documentBase.getId(), documentBase.getDocumentNumber());

            } catch (final SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Метод активирует режим ввода данных непосредственно в таблицу
     * без использования справочников и внешних форм редактирования
     */
    private void activateTableEditor() {
        // Создаем пустую форму диалога
        BaseEditorDialog editor = new BaseEditorDialog(controller,
                RecordOperationType.EDIT);
        editor.setButtonAction(new ButtonActionAdapter() {
            @Override
            public boolean canCancel() {
                final int answer = Dialogs.showQuestionDialog("Вы действительно хотите выйти из режима редактирования?\n" +
                        "\nВсе изменения по документу не будут сохранены в базе.", "Выход из режима редактирования");
                if (answer == 0) {
                    return true;
                }
                return false;
            }

            @Override
            public boolean canSave() {
                return true;
            }
        });
        editor.setParentTitle("Спецификация накладной №" + documentBase.getDocumentNumber());
        EditingPane editingPanel = new SaleDocumentItemsTableEditor(frameViewPort, documentBase);
        editingPanel.setSourceEntity(documentBase);

        // Для созданного пустого диалога устанавливаем панель редактирования
        editor.setEditorPane(editingPanel);
        // Модально показываем форму и ожидаем закрытия
        if (editor.showModal()) {
            // Форма закрыта со значением true
            // Получаем DAO слой
            final DaoFactory factory = DaoFactory.getInstance();
            // Получаем интерфейс для работы с БД
            final ICommonDaoThread dao = factory.getCommonDaoThread();
            try {
                dao.updateCollectionThread(editingPanel.getSourceEntity());
            } catch (final SQLException e) {
                e.printStackTrace();
            }
            // ПОсле сохранения данных обновляем содержимое грида
            updateContent();
        }
    }

    private void preview() {
        // new SaleDocumentViewer(controller, data);

        // Создаем провайдера данных
        SaleDocumentDataProvider provider = new SaleDocumentDataProvider();
        // Получаем данные по документу
        SaleDocumentReport reportData = provider.prepareDocument(documentBase.getId(), true);

        new SimpleTTNReport(reportData);
    }

    private boolean checkZeroPrice() {
        if (manager.isDocumentMaterialsSale(documentBase)) {
            return false;
        }
        // Если экспортный документ и уцененка
        if (manager.isExportDocument(documentBase)) {
            if (documentBase.getPriceReduction3Grade() > 0.0) {
                return false;
            }
        }

        for (Object o : data) {
            SaleDocumentItemView selectedItem = (SaleDocumentItemView) o;
            if (o != null) {
                if (selectedItem.getAccountingPrice() == 0.0) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Метод обновления контролов формы данными из текущего документа
     */
    private void updateDocumentContent() {

        cellEditor.setCallBack(this);
        informationPanel.setVisible(true);

        btnUpdate.setEnabled(false);
        btnAdditional.setEnabled(true);
        btnInformation.setEnabled(true);

        informationPanel.setZeroPriceControl(false);

        boolean zeroPrice = checkZeroPrice();


        if (documentBase != null) {
            btnTerminal.setEnabled(false);

            // Документ отгружает/возвращает материалы
            if (manager.isDocumentMaterialsSale(documentBase)) {
                btnImport.setVisible(false);
                btnTerminal.setVisible(false);

                btnAddItem.setVisible(true);
                btnEditItem.setVisible(true);
                btnDeleteItem.setVisible(true);
                btnTableEditor.setVisible(true);
            } else {

                btnAddItem.setVisible(false);
                btnEditItem.setVisible(false);
                btnDeleteItem.setVisible(false);
                btnTableEditor.setVisible(false);

                btnTerminal.setVisible(true);

                if (manager.isDocumentRefund(documentBase)) {
                    btnImport.setVisible(true);
                } else {
                    btnImport.setVisible(false);
                }
            }

            final TableColumnModel columnModel = gridViewPort.getTable().getColumnModel();

            if (manager.isExportDocument(documentBase)) {
                for (int c = 12; c < 15; c++) {
                    columnModel.getColumn(c).setMaxWidth(100);
                    columnModel.getColumn(c).setMinWidth(100);
                    columnModel.getColumn(c).setPreferredWidth(100);
                }
            } else {
                for (int c = 12; c < 15; c++) {
                    columnModel.getColumn(c).setMaxWidth(0);
                    columnModel.getColumn(c).setMinWidth(0);
                    columnModel.getColumn(c).setPreferredWidth(0);
                }
            }

            if (manager.isRetailDocument(documentBase)) {
                for (int c = 15; c < 17; c++) {
                    columnModel.getColumn(c).setMaxWidth(100);
                    columnModel.getColumn(c).setMinWidth(100);
                    columnModel.getColumn(c).setPreferredWidth(100);
                }
            } else {
                for (int c = 15; c < 17; c++) {
                    columnModel.getColumn(c).setMaxWidth(0);
                    columnModel.getColumn(c).setMinWidth(0);
                    columnModel.getColumn(c).setPreferredWidth(0);
                }
            }

            // Если документ закрыт
            if (SaleDocumentManager.isDocumentClosed(documentBase)) {
                btnCalculate.setEnabled(false);
                btnCalculate.setText("Документ закрыт");
                btnTerminal.setEnabled(false);

                btnImport.setEnabled(false);

                btnAddItem.setEnabled(false);
                btnEditItem.setEnabled(false);
                btnDeleteItem.setEnabled(false);

                btnTableEditor.setEnabled(false);
            } else {

                btnCalculate.setText("Расчет накладной");
                btnTerminal.setEnabled(true);

                // Если документ изменялся оператором - необходимо пересчитать
                if (documentIsChanged || detailIsChanged || manualChange) {
                    btnCalculate.setEnabled(true);
                }

                // Если документ уже пересчитан
                if (documentIsRecalculated) {
                    btnCalculate.setEnabled(false);
                }

                if (documentBase.getCheck() == null) {
                    btnCalculate.setEnabled(true);
                } else {
                    if (documentBase.getCheck() == Boolean.TRUE) {
                        btnCalculate.setEnabled(false);
                    }
                }

                btnAddItem.setEnabled(true);
                btnEditItem.setEnabled(true);
                btnDeleteItem.setEnabled(true);

                btnTableEditor.setEnabled(true);

                if (zeroPrice) {
                    btnCalculate.setEnabled(true);
                    informationPanel.setZeroPriceControl(true);
                } else {
                    informationPanel.setZeroPriceControl(false);
                }

                if (btnCalculate.isEnabled()) {
                    btnPrint.setEnabled(false);
                } else {
                    btnPrint.setEnabled(true);
                }
            }

            informationPanel.setSummaryInformation(documentBase.getItemAmount());

            if (documentBase.getCurrencyId() > 1) {

                informationPanel.getSecondInfoPanel().setVisible(true);
                CurrencyName currency = manager.getCurrencyNameById(documentBase.getCurrencyId());

                informationPanel.getSecondInfoPanel().setCurrencyName(currency.getName() + " (" + documentBase.getCurrencyRateFixed() + " руб.)");
                if (documentBase.getCurrencyRateFixed() == 160.0 || documentBase.getCurrencyRateFixed() == 0.0) {
                    informationPanel.getSecondInfoPanel().wrongCurrencyRate();
                }

                informationPanel.getFirstInfoPanel().setCurrencyName("Бел. руб. (" + documentBase.getCurrencyRateSale() + " руб.)");

                if (documentBase.getCurrencyRateSale() == 160.0 || documentBase.getCurrencyRateSale() == 0.0) {
                    informationPanel.getFirstInfoPanel().wrongCurrencyRate();
                }

            } else {
                informationPanel.getSecondInfoPanel().setVisible(false);
                informationPanel.getFirstInfoPanel().setCurrencyName("Бел. руб.");
            }

            informationPanel.updateNDS(manager.checkNDSDocument(data));
            informationPanel.updateType(manager.checkChildDocument(data));
            informationPanel.setSummaryInformation(documentBase.getItemAmount());
            /*
            // Проверка на наличие изделий с разным НДС (например детское по 10% и 20%)
            informationPanel.updateType(manager.checkNDSDocument(data));

            // Проверка документа на детский/взрослый в документе
            informationPanel.updateType(manager.checkChildDocument(data));
*/
            // Проверка документа на несорт в документе сорта
            informationPanel.updateNoSort(manager.checkNoSortDocument(documentBase, data));


        }
    }

    public void editDocumentInformation() {
        // Создаем пустую форму диалога
        BaseEditorDialog editor = new BaseEditorDialog(controller,
                RecordOperationType.EDIT);
        editor.setParentTitle("Накладная на отгрузку");
        EditingPane editingPanel = new SaleDocumentEditor(frameViewPort);
        editingPanel.setSourceEntity(documentView);

        // Если документ закрыт - запрет на сохраниение изменений
        if (manager.isDocumentClosed(documentBase)) {
            editor.getBtnSave().setEnabled(false);
        } else {
            editor.getBtnSave().setEnabled(true);
        }

        String oldDocumentNumber = null;
        if (documentView != null) {
            oldDocumentNumber = documentView.getDocumentNumber();
        }

        // Для созданного пустого диалога устанавливаем панель редактирования
        editor.setEditorPane(editingPanel);
        // Модально показываем форму и ожидаем закрытия
        if (editor.showModal()) {
            // Форма закрыта со значением true
            // Получаем DAO слой
            final DaoFactory factory = DaoFactory.getInstance();
            // Получаем интерфейс для работы с БД
            final ICommonDao dao = factory.getCommonDao();
            try {
                // Обновляем сущность в БД
                Object o = editingPanel.getSourceEntity();
                dao.updateEntity(o);
                // Статус по документу как НЕОБХОДИМ РАСЧЕТ
                manager.checkDocument(documentBase.getId(), false);

                SaleDocumentBase item = (SaleDocumentBase) o;
                if (item != null) {
                    SaleDocumentHistoryService history = SaleDocumentHistoryService.getInstance();
                    // Фиксируем факт изменения номера документа
                    if (oldDocumentNumber != null) {
                        if (!oldDocumentNumber.trim().equals(item.getDocumentNumber().trim())) {
                            history.historyEditNumberDocument(item.getId(), item.getDocumentNumber(), oldDocumentNumber);
                        }
                    }
                    // Фиксируем факт редактирования документа
                    history.historyEditDocument(item.getId(), item.getDocumentNumber());
                }

                documentIsChanged = true;
                documentIsRecalculated = false;

            } catch (final SQLException e) {
                e.printStackTrace();
            }
            // ПОсле сохранения данных обновляем содержимое грида
            updateContent();
        }
    }

    @Override
    public void addRecord() {

        editingPane.beforeEditing(documentBase);

        // Создаем пустую форму диалога
        BaseEditorDialog editor = new BaseEditorDialog(controller,
                RecordOperationType.EDIT);
        editor.setParentTitle("Материал к отгрузке");
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
                SaleDocumentItemBase item = (SaleDocumentItemBase) editingPane.getSourceEntity();
                item.setDocumentId(documentView.getId());
                Object o = dao.saveEntity(item);
                gridViewPort.setUpdatedObject(o);
                detailIsChanged = true;
                documentIsRecalculated = false;
            } catch (final SQLException e) {
                e.printStackTrace();
            }
            updateContent();
        }
    }

    @Override
    public void editRecord() {

        if (gridViewPort.getSelectedItem() == null) {
            return;
        }

        editingPane.beforeEditing(documentBase);

        // Создаем пустую форму диалога
        BaseEditorDialog editor = new BaseEditorDialog(controller,
                RecordOperationType.EDIT);
        DaoFactory factory = DaoFactory.getInstance();
        ICommonDao dao = factory.getCommonDao();
        SaleDocumentItemBase editingItem = null;
        try {
            editingItem = (SaleDocumentItemBase) dao.getEntityById(SaleDocumentItemBase.class, ((BaseEntity) gridViewPort.getSelectedItem()).getId());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (editingItem == null) {
            return;
        }

        editor.setParentTitle("Материал к отгрузке: " +
                editingItem.getItem().getModel().getName().trim());
        editingPane.setSourceEntity(editingItem);
        // Для созданного пустого диалога устанавливаем панель редактирования
        editor.setEditorPane(editingPane);
        // Модально показываем форму и ожидаем закрытия
        if (editor.showModal()) {
            // Форма закрыта со значением true
            // Получаем DAO слой
            factory = DaoFactory.getInstance();
            // ПОлучаем интерфейс для работы с БД
            dao = factory.getCommonDao();
            try {
                // Обновляем сущность в БД
                dao.updateEntity(editingPane.getSourceEntity());
                gridViewPort.setUpdatedObject(gridViewPort.getSelectedItem());
                detailIsChanged = true;
                documentIsRecalculated = false;
            } catch (final SQLException e) {
                e.printStackTrace();
            }
            updateContent();
        }
    }

    @Override
    public void deleteRecord() {
        if (gridViewPort.getSelectedItem() == null) {
            return;
        }
        SaleDocumentItemBase deletedObject = null;
        final DaoFactory factory = DaoFactory.getInstance();
        // Получаем интерфейс для работы с БД
        final ICommonDao dao = factory.getCommonDao();
        try {
            deletedObject = (SaleDocumentItemBase) dao.getEntityById(SaleDocumentItemBase.class, ((BaseEntity) gridViewPort.getSelectedItem()).getId());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        final int answer = Dialogs.showDeleteDialog(deletedObject.getItem().getModel().getName().trim());

        if (answer == 0) {
            try {
                final BaseEntity baseEntity = (BaseEntity) gridViewPort.getSelectedItem();
                gridViewPort.setDeletedObject(gridViewPort.getSelectedItem());
                // Запрос к DAO на удаление объекта
                dao.deleteEntity(SaleDocumentItemBase.class, baseEntity.getId());
                detailIsChanged = true;
                documentIsRecalculated = false;
            } catch (Exception e) {
                MainController.exception(e, "Ошибка удаления записи");
            }
            updateContent();
        }
    }


    @Override
    public void updateContent() {
        class Task extends BackgroundTask {
            public Task(final String messageText, BreakableThread event) {
                super(messageText, event);
            }

            @Override
            protected Boolean doInBackground() throws Exception {
                DaoFactory<SaleDocumentItemView> factory = DaoFactory.getInstance();
                IGenericDao<SaleDocumentItemView> dao = factory.getGenericDao();
                List<QueryProperty> criteria = new ArrayList<>();
                criteria.add(new QueryProperty("document", documentView.getDocumentNumber()));
                List<SaleDocumentItemView> list = null;
                data.clear();
                try {
                    // list = dao.getEntityListByNamedQuery(SaleDocumentItemView.class, "SaleDocumentItemView.findByDocumentNumber", criteria);
                    list = manager.getEntityListByNamedQuery(documentView.getDocumentNumber());
                    data.addAll(list);
                    documentBase = manager.getSaleDocumentBaseById(documentView.getId());
                    cellEditor.updateDocument(documentBase);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (userSign.equals("sale-dept_")) {
                    // Подгрузка изображений только для отдела сбыта
                    ImageService modelImageService = ModelImageServiceDB.getInstance();

                    for (Object o : data) {
                        SaleDocumentItemView selectedItem = (SaleDocumentItemView) o;
                        if (o != null) {
                            UCImageLabel label = (UCImageLabel) selectedItem.getImage();
                            try {
                                label.setImageFile(modelImageService.getDefaultImageFileByModelNumber(selectedItem.getItemModelNumber(), ModelImageSize.SMALL));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }

                // Экземпляр калькулятора накладной
                SaleDocumentCalculator calculator = SaleDocumentCalculator.getInstance();
                // Итоги для GUI пользователю
                summingUp = calculator.summingUpCost(list, documentBase.getDocumentType(), documentBase.getTradeMarkValue());

                // Обновляем панель
                informationPanel.updateInformation(summingUp, documentBase);

                frameViewPort.updateContent();
                updateDocumentContent();
                return true;
            }
        }
        Task task = new Task("Получение спецификации...", this);
        task.executeTask();
    }

    @Override
    public boolean doBreak() {
        btnAddItem.setEnabled(false);
        btnEditItem.setEnabled(false);
        btnDeleteItem.setEnabled(false);

        btnAdditional.setEnabled(false);
        btnCurrency.setEnabled(false);
        btnInformation.setEnabled(false);


        btnTableEditor.setEnabled(false);

        btnCalculate.setEnabled(false);
        btnTerminal.setEnabled(false);
        btnImport.setEnabled(false);
        btnDocumentation.setEnabled(false);

        informationPanel.setVisible(false);
        btnPrint.setEnabled(false);

        return false;
    }

    /**
     * Метод получения данных со сканера, прототип
     * //TODO Вынести в TerminalHandler максимально
     */
    private void getDataFromTerminal() {

        if (Dialogs.showQuestionDialog("Получить данные с терминала ?", "Получение данных") == 0) {
            String port = MainController.getConfiguration().getProperty("serial-port", "com1");
            CipherReader cipherReader = new CipherReader(port);
            String[] res = cipherReader.getCodeListExternal(new Breaker());
            if (res != null) {
                int count = cipherReader.getRecordCount();
                if (count != res.length) {
                    // Конпроль количества строк
                    JOptionPane.showMessageDialog(null, "Не все данные были приняты из терминала (Количество)", "Ошибка!!!", javax.swing.JOptionPane.ERROR_MESSAGE);
                } else {

                    // Контроль длины строк
                    boolean wrongLength = false;
                    for (String s : res) {
                        if (s.length() != 22) {
                            wrongLength = true;
                        }
                    }

                    if (wrongLength) {
                        JOptionPane.showMessageDialog(null, "Не все данные были приняты из терминала (Длина строки)", "Ошибка!!!", javax.swing.JOptionPane.ERROR_MESSAGE);
                    } else if (!TerminalHandler.checkControlSum(res)) {
                        JOptionPane.showMessageDialog(null, "Не все данные были приняты из терминала (Контрольная сумма)", "Ошибка!!!", javax.swing.JOptionPane.ERROR_MESSAGE);
                    } else {
                        class Task extends BackgroundTask {
                            public Task(final String messageText) {
                                super(messageText);
                            }

                            @Override
                            protected Boolean doInBackground() throws Exception {
                                int id = ((BaseEntity) documentBase).getId();
                                TerminalHandler termHandler = new TerminalHandler(controller, res);
                                termHandler.saveToDocument((BaseEntity) documentBase);
                                manager.checkDocument(documentBase.getId(), false);

                                documentIsChanged = true;
                                documentIsRecalculated = false;

                                updateContent();
                                return true;
                            }
                        }
                        Task task = new Task("Сохранение данных...");
                        task.executeTask();
                    }
                }
            }
        }
    }

    private void UpdateEanCodeForDocument() throws Exception {
        document = manager.getSaleDocumentById(documentBase.getId());
        SaleDocumentDataProvider provider = new SaleDocumentDataProvider();
        provider.updateEanCodeByCodeAndColor(document);

        DocumentUpdater updater = new DatabaseDocumentUpdater();
        updater.updateDocument(document);
    }

    /**
     * Метод обработки нажатия кнопки "Расчет документа"
     */
    private void recalculateDocument() {

        // ***********************************************************************
        // Регистрируем изменения в таблице для возвратных документов
        // ***********************************************************************
        if (manager.isDocumentRefund(documentBase)) {
            if (manager.isDetailsChanged(data)) {
                if (Dialogs.showQuestionDialog("В возвратном документе произведены изменения цен для изделий.\n Сохранить эти изменения?", "Сохранение цены") == 0) {
                    if (!manager.saveChangesForDetails(data)) {
                        manualChange = false;
                        return;
                    }
                }
            }
        }

        // ***********************************************************************
        // Регистрируем наличие уцененных изделий в накладной с не уцененными изд-ями
        // Но только для документов розничным/фирменным магазинам
        // И если найдены нестыковки - выходим и не расчитываем документ
        // ***********************************************************************
        if (manager.isDocumentRetail(documentBase)) {
            if (manager.isExistRemainsReduction(documentBase, data)) {
                gridViewPort.updateViewPort();
                return;
            }
        }

        // ***********************************************************************
        // ДИАЛОГ РАСЧЕТА ДОКУМЕНТА
        // ***********************************************************************
        if (Dialogs.showQuestionDialog("Произвести пересчет по документу ?", "Пересчет документа") == 0) {
            document = manager.getSaleDocumentById(documentBase.getId());
            // document = manager.generationSaleDocument(documentBase);
            // Показать диалог выбора даты только для экспортных документов, иначе
            // дата отгрузки считать на текущую дату, если не null
            Date d = dialogDateSelector.selectDate(document.getDocumentSaleDate());
            if (d != null) {
                System.out.println("Дата установленка как " + d);
                document.setDocumentSaleDate(d);
                // Тут получаем курс валюты на выбраннуюу дату
                // но только для экспортных документов
                if (document.getDocumentExport() == 1) {
                    float rate = 0.f;
                    if (!document.isPrepayment()) {
                        rate = manager.getCurrencyRateValue(document.getCurrencyId(), document.getDocumentSaleDate(), false);
                        document.setCurrencyRateSale(rate);
                    }
                }


                // Получение EAN13 согласно спецификации документа
                //
                //
                SaleDocumentDataProvider provider = new SaleDocumentDataProvider();
                provider.updateEanCodeAndColor(document);

                // Здесь перед началом пересчета изменяются необходимые настройки документа

                // Проверка корректности заполнения НДС если необходимо расчитать документ с НДС
                if (documentBase.getDocumentVATType() != 2) {
                    SaleDocumentJDBC db = new SaleDocumentJDBC();
                    int vatClassifier = db.getClassifierVatValue(document.getId());
                    if (document.getDocumentVatValue() == 0) {
                        document.setDocumentVatValue(vatClassifier);
                    }
                    if (document.getDocumentVatValue() != vatClassifier) {
                        Object[] optionsQuestionsTerminal = {"Заменить",
                                "Не заменять"};
                        if (Dialogs.showQuestionDialog("<html>Документ имеет ставку НДС " +
                                "<font color=\"green\">" + document.getDocumentVatValue() + "%" + "</font>" +
                                " но в классификаторе установлена ставка <font color=\"green\">\"+" + vatClassifier + "%" + "</font>." + "<p>Заменить ставкой из классификатора?</html>", "Ставка НДС", optionsQuestionsTerminal) == 0) {
                            document.setDocumentVatValue(vatClassifier);
                        }
                    }
                }

                // Экземпляр калькулятора накладной
                SaleDocumentCalculator calculator = SaleDocumentCalculator.getInstance(document);
                //calculator.calculateSaleDocumentItemsAmount(document);

                // Расчет документа в калькуляторе, главный метод !!!!!
                // Передаем ему мапу продукции для получения спец цен экспорта
                calculator.calculate(manager.getProductionItemMapByDocuments(document.getId()));

                // Итоги для GUI пользователю
                summingUp = calculator.summingUpCost(document.getDetailList(), document.getDocumentType(), document.getValueTradeMarkup());
                // Обновляем панель
                informationPanel.updateInformation(summingUp, documentBase);

                try {

                    //DocumentUpdater updater = new DatabaseDocumentSQLUpdater();
                    //document.setSaved(true);

                    DocumentUpdater updater = new DatabaseDocumentUpdater();
                    updater.updateDocument(document);

                    /*
                    if(manager.isDocumentMaterialsSale(document)) {
                        DocumentUpdater updater = new DatabaseDocumentUpdater();
                        updater.updateDocument(document);
                    }else{
                        DocumentUpdater updater = new NativeDocumentUpdater();
                       if(!updater.updateDocument(document)){
                           System.err.println("Ошибка во время сохранения документа");
                       }
                    }
                    */
                    // Проверки после сохранения в базу с фактическими данными после расчета

                    // ПОлучаем сумму с НДС из базы
                    double totalSum = manager.getTotalSumByDocumentId(document.getId());
                    Date saleDate = manager.getSaleDateByDocumentId(document.getId());

                    // Сравниваем с суммой НДС
                    // И предполагаем, что во время сохранения в базу произошла ошибка сохранения
                    if (totalSum != summingUp.getValueSumCostAndVat()) {
                        JOptionPane.showMessageDialog(null,
                                "<html><div style=\"text-align: left;\"> " + "<font color=\"red\">" +
                                        "В процессе сохранения документа в базу произошла ошибка, накладная не расчитана." +
                                        "</font>" + "<p> Повторите расчет документа"
                                        + "</html>");
                    } else {
                        // Документ перерасчитан
                        documentIsRecalculated = true;
                        // Нужно обновить данные в GUI
                        needToUpdate = true;
                        // Документ сохранен и неизменен
                        documentIsChanged = false;
                        // Данные в спецификации изменены
                        detailIsChanged = false;
                        // Изменяем статус расчета по документу как РАСЧИТАН И СОХРАНЕН
                        manager.checkDocument(documentBase.getId(), true);

                        SaleDocumentHistoryService historyService = SaleDocumentHistoryService.getInstance();
                        historyService.historyRecalculateDocument(documentBase.getId(), documentBase.getDocumentNumber());
                    }
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null,
                            "В процессе расчета накладной произошла ошибка");
                    e.printStackTrace();
                }
                // Обновим содержимое грида и состояние контролов
                updateContent();
            }
        }
    }

    /**
     * Метод отображает форму и в случае, если документ был изменен и расчитан,
     * после закрытия формы произойдет обновление данных в главной таблице
     *
     * @return true - документ был расчитан, нужно произвести обновление содержимого грида
     */
    public boolean showMode() {
        if (documentView != null) {
            frameViewPort.getFrameControl().showFrame();
        } else {
            frameViewPort.getFrameControl().closeFrame();
        }
        return needToUpdate;
    }


    /**
     * Метод отображает форму перечня создаваемых документов
     * с возможностью выбора какие из документов формировать.
     * <p>
     * Предварительно использует объект SaleDocumentBase,
     */
    private void createDocumentSelector() {
        DocumentTypePickDialog dialog = new DocumentTypePickDialog(controller, documentBase);
        boolean isRefund = manager.isDocumentRefund(documentBase);
        int invoice = 1;
        if (isRefund) {
            invoice = 0;
        }

        // Тут формируем условия документа для дальнейшей обработки
        DocumentTypePreset preset = new DocumentTypePreset(invoice, true, !isRefund, !isRefund, documentBase.getRecipientCode());

        boolean isMaterial = false;

        if (documentBase.getDocumentType().equals(InvoiceType.DOCUMENT_SALE_MATERIAL) || documentBase.getDocumentType().equals(InvoiceType.DOCUMENT_REFUND_MATERIAL)) {
            isMaterial = true;
        }

        DocumentTypePreset resultPreset = dialog.showDialog(preset, isMaterial);

        if (resultPreset != null) {
            if (Dialogs.showQuestionDialog("<html><div style=\"text-align: center;\"> Убедитесь, что документ ШАПКИ и приложение для накладной " + "<font color=\"red\">" +
                    documentBase.getDocumentNumber() + "</font>" + " закрыты, и нажмите [Продолжить]"
                    + "</html>", "Формирование документов") == 0) {
                createDocument(resultPreset);
            }
        } else {
            System.out.println("Отмена формирования документа");
        }
    }

    /**
     * В качестве временного решения
     */
    private void createDocument(DocumentTypePreset preset) {
        // Получаем документ если ранее не получен
//
        document = manager.getSaleDocumentById(documentBase.getId());

        Date timestamp = new Date();


        // Создаем провайдера данных
        SaleDocumentDataProvider provider = new SaleDocumentDataProvider(preset);
        // Получаем данные по документу
        SaleDocumentReport reportData = provider.prepareDocument(document);

        reportData.getDetailMap().put("TIMESTAMP", DateUtils.getNormalDateTimeFormatPlus(timestamp));

        // Если документ определен как услуга, установим тип для БСО "УСЛУГА"
        if (document.isGiveTake()) {

            if (document.getServiceType() == 0) {
                reportData.getDetailMap().put("PRODUCT_NAME", "Услуги крашения");
            }

            if (document.getServiceType() == 1) {
                reportData.getDetailMap().put("PRODUCT_NAME", "Услуги крашения");
            }

            if (document.getServiceType() == 2) {
                reportData.getDetailMap().put("PRODUCT_NAME", "Пошив из давальческого сырья");
            }
        }

        // Если формируем приложение
        // TODO Заготовка
        if (preset.isAnnexToInvoice()) {
            // Если документ экспортный
            if (manager.isExportDocument(documentBase)) {
                // Экспортное приложение
                new AnnexTTNExportReport(reportData);
            } else {
                // еСли документ розничный
                if (manager.isRetailDocument(documentBase)) {
                    // Приложение фирменным магазинам
                    new AnnexTTNRetailReport(reportData);
                } else {

                    // Если нет скидки
                    if (documentBase.getDiscountType() == 0) {
                        if (documentBase.getTradeMarkType() > 0) {
                            // Торговая надбавка
                            new AnnexTTNTradeReport(reportData);
                        } else {
                            // Простое приложение
                            new AnnexTTNReport(reportData);
                        }
                    } else {

                        // Скидка на цену изделия
                        if (documentBase.getDiscountType() == 1) {
                            if (preset.isAnnexSimple()) {
                                new AnnexTTNProductDiscountReport(reportData);
                            } else {
                                new AnnexTTNReport(reportData);
                            }
                        }

                        // Скидка на сумму документа
                        if (documentBase.getDiscountType() == 2) {
                            if (preset.isAnnexSimple()) {
                                new AnnexTTNDocumentDiscountReport(reportData);
                            } else {
                                new AnnexSimpleTTNDocumentDiscountReport(reportData);
                            }
                        }
                    }
                }
            }
        }

        // Если выбрано формирование товарной накладной
        if (preset.getInvoiceType() != 0) {
            String templateName = "";
            if (preset.getInvoiceType() == 1) {
                // Ветвления в зависимости от типа документа
                if (manager.isExportDocument(document)) {
                    // Экспортный ТТН-1
                    templateName = "document_ttn1_export.ott";
                } else {
                    // ТТН-1 по стране
                    templateName = "document_ttn1.ott";
                }
                new InvoiceReport(reportData, templateName);
            } else if (preset.getInvoiceType() == 2) {
                if (manager.isExportDocument(document)) {
                    // Экспортный ТН-2
                    templateName = "document_tn2_export.ott";
                } else {
                    // ТН-2 по стране
                    templateName = "document_tn2.ott";
                }
                new InvoiceReport(reportData, templateName);
            } else if (preset.getInvoiceType() == 3) {
                if (manager.isExportDocument(document)) {
                    // Экспортный корректировочный документ
                    new AdjustmentDocumentExportReport(reportData);
                } else {
                    document = manager.getSaleDocumentByNumber(documentBase.getDocumentNumber());
                    provider = new SaleDocumentDataProvider(preset);
                    SaleDocumentReport reportData1 = provider.prepareDocument(document);
                    new AdjustmentDocumentReport(reportData, reportData1);
                }
            }
        }

        // Если печатаем справки
        if (preset.isReferenceToInvoice()) {
            System.out.println("Печать справки ВЭС");
            //Если документ экспортный
            if (manager.isExportDocument(documentBase)) {
                System.out.println("Печать справки ВЭС");
                new ReferenceTTNExportReport(reportData);
            } else {
                // Еcли документ розничный
                if (manager.isRetailDocument(documentBase)) {
                    new ReferenceTTNRetailReport(reportData);
                }
            }
        }

        // Если печатаем счет-фактуру
        if (preset.isInvoiceTTN()) {
            System.out.println("Печать счет-фактуры...");
            new InvoiceReportBy(reportData);
        }

        // Если формируем протокол согласования цен
        // Вызываем рефлексией через известный класс протокола
        if (preset.isPriceAgreement()) {
            Class<? extends AbstractInvoiceReport> class_ = preset.getProtocolReport().getProtocolClass();
            if (class_ != null) {
                try {
                    Constructor<?> cons = class_.getConstructor(SaleDocumentReport.class);
                    cons.newInstance(reportData);
                } catch (Exception e) {
                    System.out.println("Ошибка вызова протокола согласования цен для класса [" + preset.getProtocolReport().getProtocolClass() + "]");
                    e.printStackTrace();
                }
            }
        }

        SaleDocumentHistoryService historyService = SaleDocumentHistoryService.getInstance();
        historyService.historyCreateReport(documentBase.getId(), documentBase.getDocumentNumber());
    }

    @Override
    public boolean onCallBack() {
        if (!SaleDocumentManager.isDocumentClosed(documentBase)) {
            detailIsChanged = true;
            documentIsRecalculated = false;
            manualChange = true;
            updateDocumentContent();
            btnCalculate.setEnabled(true);
        }
        return true;
    }


}
