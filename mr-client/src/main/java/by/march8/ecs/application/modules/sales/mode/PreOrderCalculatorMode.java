package by.march8.ecs.application.modules.sales.mode;

import by.gomel.freedev.ucframework.uccore.enums.MarchWindowType;
import by.gomel.freedev.ucframework.uccore.enums.RightEnum;
import by.gomel.freedev.ucframework.uccore.modes.abstracts.AbstractFunctionalMode;
import by.gomel.freedev.ucframework.uccore.viewport.frameviewport.FrameViewPort;
import by.gomel.freedev.ucframework.uccore.viewport.gridviewport.GridViewPort;
import by.gomel.freedev.ucframework.ucdao.DaoFactory;
import by.gomel.freedev.ucframework.ucdao.interfaces.ICommonDaoThread;
import by.gomel.freedev.ucframework.ucswing.dialog.Dialogs;
import by.gomel.freedev.ucframework.ucswing.uicontrols.UCTextField;
import by.gomel.freedev.ucframework.ucswing.uicontrols.UCToolBar;
import by.march8.ecs.MainController;
import by.march8.ecs.application.modules.marketing.manager.PreOrderSaleDocumentCalculator;
import by.march8.ecs.application.modules.sales.editor.PreOrderCalculatorTableEditor;
import by.march8.ecs.application.modules.sales.manager.PreOrderSaleDocumentManager;
import by.march8.ecs.application.modules.warehouse.external.shipping.model.CallBack;
import by.march8.ecs.application.modules.warehouse.external.shipping.model.SaleDocumentReport;
import by.march8.ecs.application.modules.warehouse.external.shipping.reports.AnalysisConcernReport;
import by.march8.ecs.framework.common.BackgroundTask;
import by.march8.ecs.framework.common.money.RoundUtils;
import by.march8.entities.sales.PreOrderCalculatorItem;
import by.march8.entities.sales.PreOrderSaleDocument;
import by.march8.entities.sales.PreOrderSaleDocumentItem;
import by.march8.entities.sales.PreOrderSaleDocumentItemView;
import by.march8.entities.sales.PreOrderSaleDocumentView;
import by.march8.entities.warehouse.SaleDocumentDetailItemReport;
import dept.marketing.cena.CenaPDB;
import dept.marketing.cena.ProfitabilityItem;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import static by.march8.ecs.application.modules.warehouse.external.shipping.manager.SaleDocumentCalculator.ROUND_XXX_XX;
import static by.march8.ecs.application.modules.warehouse.external.shipping.manager.SaleDocumentCalculator.calculateCurrencyPriceFromPriceValue;
import static by.march8.ecs.application.modules.warehouse.external.shipping.manager.SaleDocumentCalculator.calculatePriceAndDiscountValue;
import static by.march8.ecs.application.modules.warehouse.external.shipping.manager.SaleDocumentCalculator.calculatePriceFromCurrencyPriceValue;
import static by.march8.ecs.application.modules.warehouse.external.shipping.manager.SaleDocumentCalculator.round;

public class PreOrderCalculatorMode extends AbstractFunctionalMode implements CallBack {

    public static double PROFITABILITY_LIMIT = 0.5;
    private final CenaPDB cdb;
    private final PreOrderSaleDocumentCalculator calculator;
    private final PreOrderCalculatorTableEditor cellEditor;
    private final JButton btnCalculateDown;
    private final JButton btnSetDiscount;
    private final JButton btnDocument;
    private final JButton btnSave;
    private JButton btnCalculateUp;
    private RightEnum right;
    private PreOrderSaleDocumentView document;
    private List<PreOrderSaleDocumentItemView> rawData;
    private List<PreOrderCalculatorItem> data;
    private JPanel pFooter;
    private JPanel pSummary;
    private JLabel lblSummary;
    private UCToolBar toolBar;
    private boolean isDataChanged = false;
    private PreOrderSaleDocumentManager manager;
    private UCTextField tfProfitability;
    private JButton btnApplyProfitability;


    public PreOrderCalculatorMode(MainController mainController, PreOrderSaleDocumentView document, List<PreOrderSaleDocumentItemView> list) {

        controller = mainController;
        modeName = "Калькулятор рентабельности для документа №" + document.getDocumentNumber();

        this.document = document;
        rawData = list;

        frameViewPort = new FrameViewPort(mainController, MarchWindowType.DIALOGSIMPLE);
        frameViewPort.getFrameControl().setTitleFrame(modeName);

        right = RightEnum.WRITE;//controller.getRight(modeName);
        toolBar = frameViewPort.getFrameRegion().getToolBar();
        toolBar.registerEvents(this);
        toolBar.setRight(right);
        toolBar.getBtnReport().setVisible(false);

        gridViewPort = new GridViewPort<>(PreOrderCalculatorItem.class, false);
        gridViewPort.setCustomCellRender(new PreOrderCalculatorCellRenderer());

        data = gridViewPort.getDataModel();
        frameViewPort.setGridViewPort(gridViewPort);

        pFooter = new JPanel(new MigLayout());
        pFooter.setPreferredSize(new Dimension(100, 30));

        //pSummary = new JPanel(new FlowLayout(FlowLayout.LEFT));
        //pSummary.setBackground(Color.green.brighter());
        //lblSummary = new JLabel("");
        //pSummary.add(lblSummary);

        tfProfitability = new UCTextField();
        tfProfitability.setComponentParams(null, Float.class, 2);

        btnApplyProfitability = new JButton();
        btnApplyProfitability.setIcon(new ImageIcon(MainController.getRunPath() + "/Img/check_.png", "Применить лимит рентабельности"));
        btnApplyProfitability.setToolTipText("Применить лимит рентабельности");

        //pFooter.add(pSummary, "width 300:20:300, height 20:20, wrap");
        pFooter.add(new JLabel("    Лимит рентабельности "), " width 200:20:200, height 20:20");
        pFooter.add(tfProfitability, " width 50:20:50, height 20:20");
        pFooter.add(btnApplyProfitability, " width 20:20:20, height 20:20");
        tfProfitability.setValue(PROFITABILITY_LIMIT);

        toolBar.getBtnNewItem().setVisible(false);
        toolBar.getBtnEditItem().setVisible(false);
        toolBar.getBtnDeleteItem().setVisible(false);

        btnCalculateDown = new JButton();
        btnCalculateDown.setIcon(new ImageIcon(MainController.getRunPath() + "/Img/calculate_down_24.png", "Подбор скидки на понижение"));
        btnCalculateDown.setToolTipText("Подбор скидки на понижение");

        btnCalculateUp = new JButton();
        btnCalculateUp.setIcon(new ImageIcon(MainController.getRunPath() + "/Img/calculate_up_24.png", "Подбор скидки на повышение"));
        btnCalculateUp.setToolTipText("Подбор скидки на повышение");

        btnSetDiscount = new JButton();
        btnSetDiscount.setIcon(new ImageIcon(MainController.getRunPath() + "/Img/calculate_24.png", "Установить фиксированный размер скидки"));
        btnSetDiscount.setToolTipText("Установить фиксированный размер скидки");

        btnDocument = new JButton();
        btnDocument.setIcon(new ImageIcon(MainController.getRunPath() + "/Img/excel_24.png", "Сформировать документ анализа"));
        btnDocument.setToolTipText("Сформировать документ анализа");

        btnSave = new JButton();
        btnSave.setIcon(new ImageIcon(MainController.getRunPath() + "/Img/save_24.png", "Сохранить изменения"));
        btnSave.setToolTipText("Сохранить изменения");

        btnSave.setEnabled(false);

        toolBar.add(btnSave);
        toolBar.addSeparator();
        toolBar.add(btnSetDiscount);
        toolBar.add(btnCalculateDown);
        toolBar.add(btnCalculateUp);
        toolBar.addSeparator();

        toolBar.add(btnDocument);

        frameViewPort.getFrameRegion().getBottomContentPanel().setLayout(new BorderLayout());
        frameViewPort.getFrameRegion().getBottomContentPanel().removeNotify();
        frameViewPort.getFrameRegion().getBottomContentPanel().add(pFooter, BorderLayout.SOUTH);
        frameViewPort.getFrameRegion().getBottomContentPanel().setPreferredSize(new Dimension(20, 40));

        frameViewPort.getButtonControl().getCancelButton().setVisible(false);
        frameViewPort.getButtonControl().getOkButton().setVisible(false);


        cdb = new CenaPDB();

        init();


        updateContent();

        calculator = PreOrderSaleDocumentCalculator.getInstance();

        cellEditor = new PreOrderCalculatorTableEditor(gridViewPort, this);
        gridViewPort.setCustomCellEditor(cellEditor);
        gridViewPort.setIgnoreNotEditableCells(true);
        gridViewPort.getTable().setRowSelectionAllowed(true);

        gridViewPort.primaryInitialization();
        frameViewPort.getFrameControl().setFrameSize(new Dimension(1024, 700));
        //frameViewPort.getFrameControl().showFrame();
    }

    private void init() {

        manager = PreOrderSaleDocumentManager.getInstance(controller);

        btnCalculateDown.addActionListener(a -> {
            final int answer = Dialogs.showQuestionDialog("Произвести подбор скидки на ПОНИЖЕНИЕ, для получения положительной рентабельности ?", "Подбор скидок для продукции");
            if (answer == 0) {
                selectionDiscountAllDown();
            }
        });

        btnCalculateUp.addActionListener(a -> {
            final int answer = Dialogs.showQuestionDialog("Произвести подбор скидки на ПОВЫШЕНИЕ для получения положительной рентабельности ?", "Подбор скидок для продукции");
            if (answer == 0) {
                selectionDiscountAllUp();
            }
        });

        JDialog frame = (JDialog) frameViewPort.getFrame();

        frame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                if (isDataChanged) {
                    final int answer = Dialogs.showQuestionDialog("Параметры документа были изменены\nСохранить данные изменения ?", "Изменения в документе");
                    if (answer == 0) {
                        saveDocumentChanges();
                    }
                }
            }
        });

        btnSave.addActionListener(a -> {
            saveDocumentChanges();
        });

        btnDocument.addActionListener(a -> {
            createProtocolDocument();
        });

        btnApplyProfitability.addActionListener(a -> {
            PROFITABILITY_LIMIT = tfProfitability.getValueDouble();
            gridViewPort.updateViewPort();
        });

        btnSetDiscount.addActionListener(a -> {
            String input = JOptionPane.showInputDialog("Введите значение скидки");
            if (input.trim().equals("")) {
                return;
            }

            try {
                int discount = Integer.parseInt(input);
                setDiscountAll(discount);
            } catch (Exception e) {
                Dialogs.showInformationDialog("Ошибка при вводе скидки!");
            }
        });
    }

    private void createProtocolDocument() {
        SaleDocumentReport report = manager.prepareDataForReport(document.getId());
        if (report == null) {
            return;
        }

        CenaPDB cdb = new CenaPDB();

        for (SaleDocumentDetailItemReport item : report.getDetailList()) {

            ProfitabilityItem value = cdb.getSstoimostRentabelAdv(item.getArticleCode(), item.getItemSize());
            if (value != null) {
                if (value.getPrimeCostReference() > 0) {
                    value.setProfitabilityCalc(new BigDecimal(((item.getAccountingPrice() - value.getPrimeCostReference()) / value.getPrimeCostReference()) * 100)
                            .setScale(2, RoundingMode.HALF_UP).doubleValue());
                }

                if (value.getProfitabilityReference() > 0) {
                    value.setPrimeCostCalc(new BigDecimal((item.getAccountingPrice() * 100) / (value.getProfitabilityReference() + 100))
                            .setScale(4, RoundingMode.HALF_UP).doubleValue());
                }

                item.setPlanPrice(item.getAccountingPrice());
                item.setPlanProfitability(RoundUtils.round(new BigDecimal(value.getProfitabilityCalc()).setScale(2, RoundingMode.HALF_UP).doubleValue(), 4));
                item.setPlanPrimeCost(new BigDecimal(value.getPrimeCostReference()).setScale(4, RoundingMode.HALF_UP).doubleValue());
            }
        }

        new AnalysisConcernReport(report);
    }

    private void updateControlsState() {
        if (isDataChanged) {
            btnSave.setEnabled(true);
        } else {
            btnSave.setEnabled(false);
        }
    }

    private void saveDocumentChanges() {
        class Task extends BackgroundTask {
            public Task(final String messageText) {
                super(messageText);
            }

            @Override
            protected Boolean doInBackground() throws Exception {

                PreOrderSaleDocument document_ = manager.getPreOrderSaleDocumentByDocumentId(document.getId());
                if (document_ != null) {
                    if (saveDiscounts(document_, data)) {
                        document_.setDiscountType(3);
                        DaoFactory factory = DaoFactory.getInstance();
                        ICommonDaoThread dao = factory.getCommonDaoThread();
                        try {
                            dao.updateEntityThread(document_);
                            isDataChanged = false;
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        updateControlsState();
                    }
                }
                return true;
            }
        }

        Task task = new Task("Сохранение изменений...");
        task.executeTask();
    }

    // Получение данных о себестоимости и рентабельности из БД
    private void requestProfitability() {

        for (PreOrderCalculatorItem item : data) {

            ProfitabilityItem value = cdb.getSstoimostRentabelAdv(item.getArticleCode(), item.getItemSize());
            if (value.getPrimeCostReference() > 0) {
                value.setProfitabilityCalc(new BigDecimal(((item.getAccountingPrice() - value.getPrimeCostReference()) / value.getPrimeCostReference()) * 100)
                        .setScale(2, RoundingMode.HALF_UP).doubleValue());
            }

            if (value.getProfitabilityReference() > 0) {
                value.setPrimeCostCalc(new BigDecimal((item.getAccountingPrice() * 100) / (value.getProfitabilityReference() + 100))
                        .setScale(4, RoundingMode.HALF_UP).doubleValue());
            }

            item.setPrimeCost(new BigDecimal(value.getPrimeCostReference()).setScale(4, RoundingMode.HALF_UP).doubleValue());
            item.setProfitability(new BigDecimal(value.getProfitabilityReference()).setScale(2, RoundingMode.HALF_UP).doubleValue());

            item.setPrimeCostCalc(new BigDecimal(value.getPrimeCostCalc()).setScale(4, RoundingMode.HALF_UP).doubleValue());
            item.setProfitabilityCalc(new BigDecimal(value.getProfitabilityCalc()).setScale(2, RoundingMode.HALF_UP).doubleValue());
        }
    }

    public void calculateItem(PreOrderCalculatorItem item) {
        int currencyId = document.getCurrencyId();

        // Флаг дополнительного понижения курса
        boolean isRateCoefficient = (currencyId == 2 || currencyId == 5);

        double rateCommon = document.getCurrencyRate1();
        double rateAddition = document.getCurrencyRate2();

        if (isRateCoefficient) {
            rateCommon /= 100;
            rateAddition /= 100;
        }

        // Расчет базовой цены в рублях РБ с учетом скидки
        // Получаем цену в рублях с учетом скидки
        item.setCost(
                calculatePriceAndDiscountValue(
                        // Учетная оптовая цена
                        item.getAccountingPrice(),
                        // Процент скидки по документу
                        (float) item.getDiscount(), 1));

        // Расчет цены в рублях РФ если на данную позицию не установлена специальная цена
        if (item.getSpecialPrice() == 0) {
            item.setCostCurrency(round(
                    // Получаем цену в валюте
                    calculateCurrencyPriceFromPriceValue(
                            item.getCost(),
                            //Курс на начало месяца
                            (float) rateCommon), ROUND_XXX_XX));
        }
        // Расчет цены в рублях РБ при отгрузке
        item.setCostSelling(round(calculatePriceFromCurrencyPriceValue(item.getCostCurrency(), (float) rateAddition), ROUND_XXX_XX));

        // Расчет рентабельности для цены в рублях РБ при отгрузке
        double primeCost = item.getPrimeCost();

        if (primeCost != 0) {
            item.setProfitabilityFact(round((((item.getCostSelling() - primeCost) / primeCost) * 100), ROUND_XXX_XX));
        } else {
            item.setProfitabilityFact(0);
        }

    }

    private boolean saveDiscounts(PreOrderSaleDocument savedDocument, List<PreOrderCalculatorItem> list) {
        // Формируем мапу
        HashMap<Integer, PreOrderCalculatorItem> map = new HashMap<>();
        for (PreOrderCalculatorItem item : list) {
            map.put(item.getId(), item);
        }

        // Апдейтим изделия документа на актуальный скидон
        for (PreOrderSaleDocumentItem item : savedDocument.getSpecification()) {
            PreOrderCalculatorItem item_ = map.get(item.getId());
            if (item_ != null) {
                item.setDiscount(item_.getDiscount());

            } else {
                System.out.println("Изделие с ID " + item.getId() + " не найдено в документе");
            }
        }

        return true;
    }

    @Override
    public void updateContent() {

        class Task extends BackgroundTask {
            public Task(final String messageText) {
                super(messageText);
            }

            @Override
            protected Boolean doInBackground() throws Exception {
                data.clear();

                for (PreOrderSaleDocumentItemView item : rawData) {
                    data.add(new PreOrderCalculatorItem(item));
                }

                requestProfitability();
                recalculateAll();

                gridViewPort.updateViewPort();
                return true;
            }
        }

        Task task = new Task("Получение спецификации...");
        task.executeTask();
    }

    private void recalculateAll() {
        for (PreOrderCalculatorItem item : data) {
            calculateItem(item);
        }
    }

    private void selectionDiscountAllDown() {
        for (PreOrderCalculatorItem item : data) {
            selectionDiscountDown(item);
            isDataChanged = true;

        }
        gridViewPort.updateViewPort();
        updateControlsState();
    }

    private void selectionDiscountAllUp() {
        for (PreOrderCalculatorItem item : data) {
            selectionDiscountUp(item);
            isDataChanged = true;
        }
        gridViewPort.updateViewPort();
        updateControlsState();
    }

    private void setDiscountAll(double discountValue) {
        for (PreOrderCalculatorItem item : data) {
            item.setDiscount(discountValue);
            calculateItem(item);
        }

        isDataChanged = true;
        gridViewPort.updateViewPort();
        updateControlsState();
    }

    private void selectionDiscountDown(PreOrderCalculatorItem item) {
        if (item != null) {
            double discount = item.getDiscount();
            double profitability = item.getProfitabilityFact();
            while (profitability <= PROFITABILITY_LIMIT) {
                if (discount > 0) {
                    item.setDiscount(item.getDiscount() - 1);
                    calculateItem(item);
                    profitability = item.getProfitabilityFact();
                    discount = item.getDiscount();
                } else {
                    break;
                }
            }
        }
    }

    private void selectionDiscountUp(PreOrderCalculatorItem item) {
        if (item != null) {
            double discount = item.getDiscount();
            double profitability = item.getProfitabilityFact();
            while (profitability >= PROFITABILITY_LIMIT) {
                if (discount < 100) {
                    item.setDiscount(item.getDiscount() + 1);
                    calculateItem(item);
                    profitability = item.getProfitabilityFact();
                    discount = item.getDiscount();
                } else {
                    break;
                }
            }

            if (profitability < PROFITABILITY_LIMIT) {
                item.setDiscount(item.getDiscount() - 1);
                calculateItem(item);
            }
        }
    }

    @Override
    public void addRecord() {

    }

    @Override
    public void editRecord() {

    }

    @Override
    public void deleteRecord() {

    }

    @Override
    public boolean onCallBack() {
        isDataChanged = true;
        updateControlsState();
        return true;
    }

    public boolean showMode() {
        frameViewPort.getFrameControl().showFrame();
        return true;
    }
}
