package dept.ves;

import by.march8.api.utils.DatePeriod;
import by.march8.ecs.MainController;
import by.march8.ecs.application.modules.marketing.dao.MarketingPriceListMarchJDBC;
import by.march8.ecs.application.modules.references.classifier.model.ItemNameReplacer;
import by.march8.ecs.application.modules.warehouse.external.shipping.dao.SaleDocumentJDBC;
import by.march8.ecs.application.modules.warehouse.external.shipping.manager.SaleDocumentCalculator;
import by.march8.ecs.application.modules.warehouse.external.shipping.manager.SaleDocumentDataProvider;
import by.march8.ecs.application.modules.warehouse.external.shipping.manager.SaleDocumentManager;
import by.march8.ecs.application.modules.warehouse.external.shipping.model.Reduction3Grade;
import by.march8.ecs.application.modules.warehouse.external.shipping.model.SaleDocumentReport;
import by.march8.ecs.application.modules.warehouse.external.shipping.reports.AnalysisConcernReport;
import by.march8.ecs.application.modules.warehouse.external.shipping.reports.AnalysisDetailConcernReport;
import by.march8.ecs.application.modules.warehouse.external.shipping.reports.InvoiceOVESReport;
import by.march8.ecs.application.modules.warehouse.external.shipping.reports.ReferenceTTNExportReport;
import by.march8.ecs.application.modules.warehouse.external.shipping.services.Reduction3GradeService;
import by.march8.ecs.application.modules.warehouse.external.shipping.services.SpecialPriceService;
import by.march8.ecs.framework.common.BackgroundTask;
import by.march8.ecs.framework.common.Settings;
import by.march8.ecs.framework.common.money.RoundUtils;
import by.march8.ecs.framework.helpers.digits.CurrencyType;
import by.march8.entities.classifier.ProductionItemBase;
import by.march8.entities.warehouse.SaleDocumentBase;
import by.march8.entities.warehouse.SaleDocumentDetailItemReport;
import by.march8.entities.warehouse.SaleDocumentEntity;
import common.DateUtils;
import common.PanelWihtFone;
import common.ProgressBar;
import dept.marketing.cena.CenaPDB;
import dept.marketing.cena.ProfitabilityItem;
import dept.sklad.SkladDB;
import dept.sklad.SkladOO;
import dept.ves.mode.SaleDocumentPicker;
import dept.ves.mode.VolumeOfPurchasesDialog;
import dept.ves.model.AnalysisDataProvider;
import dept.ves.model.AnalysisDetailItem;
import dept.ves.model.AnalysisReportPartition;
import dept.ves.model.DetailAnalysisDataSet;
import dept.ves.model.ProductItemOVES;
import lombok.SneakyThrows;
import workDB.DB;
import workOO.OpenOffice;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

/**
 * @author vova
 */
@SuppressWarnings("all")
class CustomTableCellRenderer extends DefaultTableCellRenderer {

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        if (isSelected) {
            c.setBackground(Color.LIGHT_GRAY);
        } else if (((table.getValueAt(row, 8)).toString()).equals("Формируется")) {
            if ((((table.getValueAt(row, 10)).toString()).equals("true"))) {
                c.setBackground(Color.YELLOW);
            } else {
                c.setBackground(Settings.COLOR_RECALCULATE);
            }
        } else if ((table.getValueAt(row, 8)).equals("Удалён")) {
            c.setBackground(Color.PINK);
        } else if ((table.getValueAt(row, 8).toString().trim()).equals("Непонятно =)")) {
            c.setBackground(Color.BLUE);
        } else if (((table.getValueAt(row, 8)).toString()).equals("Закрыт")) {
            c.setBackground(Color.white);
        }
        return c;
    }
}

@SuppressWarnings("all")
public class Nakladnie extends JDialog {

    int x = 10;
    int y = 10;
    private JPanel pTools;
    private JButton btnDocumentation;
    private JButton btnDiscounts;
    private JLabel lHead;
    private JLabel lFoot = new JLabel();
    private JTable table;
    private JButton btnClose;
    private JButton bShow;
    private JPanel mainPanel;
    private DefaultTableModel tModel;
    private JScrollPane scrollTable;
    private Vector columns = new Vector();
    private Vector rows = new Vector();
    private MaskFormatter formatter;
    private JFormattedTextField ftDate;
    private String date;
    private TableColumn tcol;
    private JDialog temp;
    private JLabel lKod;
    private JTextField tfKod;
    private ProgressBar pb;

    private MainController controller;
    private SaleDocumentDataProvider provider;

    @SneakyThrows
    public Nakladnie(MainController controller, boolean f) {

        super(controller.getMainForm(), f);
        this.controller = controller;
        temp = this;

        try {
            formatter = new MaskFormatter("##.##.####");
        } catch (Exception e) {
            System.err.println("Ошибка: " + e);
        }
        formatter.setPlaceholderCharacter('0');
        Calendar c = Calendar.getInstance();
        int i = c.get(Calendar.MONTH) + 1;
        String month = new String();
        if (i < 10) {
            month = "0" + i;
        } else {
            month = Integer.toString(i);
        }
        date = new String("01." + month + "." + c.get(Calendar.YEAR));
        DB db = new DB();
        rows = db.getNakl(date);
        columns.add("Дата");
        columns.add("Номер");
        columns.add("Операция");
        columns.add("Код пол.");
        columns.add("Получатель");
        columns.add("Сумма без НДС");
        columns.add("НДС");
        columns.add("Сумма с НДС");
        columns.add("Статус");
        columns.add("№ Заявки");
        columns.add("Расчет");

        setLayout(new BorderLayout());
        setSize(25 + 280 + columns.size() * 70, 580);

        initComponents();

        table.getColumnModel().getColumn(0).setPreferredWidth(70);
        table.getColumnModel().getColumn(1).setPreferredWidth(60);
        table.getColumnModel().getColumn(2).setPreferredWidth(160);
        table.getColumnModel().getColumn(3).setPreferredWidth(40);
        table.getColumnModel().getColumn(4).setPreferredWidth(180);
        table.getColumnModel().getColumn(10).setPreferredWidth(0);
        table.getColumnModel().getColumn(10).setMinWidth(0);
        table.getColumnModel().getColumn(10).setMaxWidth(0);

        setLocationRelativeTo(controller.getMainForm());
        setResizable(false);
        setTitle("Накладные на отгрузку");
        this.setModalityType(ModalityType.DOCUMENT_MODAL);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    private void initComponents() {
        mainPanel = new PanelWihtFone();

        lHead = new JLabel("Накладные на отгрузку c ");
        lHead.setBounds(x + 100, y, 200, 20);
        mainPanel.add(lHead);

        ftDate = new javax.swing.JFormattedTextField(formatter);
        ftDate.setText(date);
        ftDate.setBounds(x + 300, y, 80, 20);
        mainPanel.add(ftDate);

        bShow = new JButton("Показать");
        bShow.setBounds(x + 385, y, 120, 20);
        bShow.addActionListener(new ActionListener() {
            @SneakyThrows
            public void actionPerformed(ActionEvent e) {
                DB db = new DB();
                rows = db.getNakl(ftDate.getValue().toString());
                while (tModel.getRowCount() > 0) {
                    tModel.removeRow(0);
                }
                for (int i = 0; i < rows.size(); i++) {
                    tModel.addRow((Vector) rows.get(i));
                }
            }
        });
        mainPanel.add(bShow);

        lFoot.setBounds(x, y + 430, 500, 20);
        mainPanel.add(lFoot);

        //создаём таблицу
        tModel = new DefaultTableModel(rows, columns) {
            @Override
            public Class getColumnClass(int column) {
                Class returnValue;
                if ((column >= 0) && (column < getColumnCount())) {
                    returnValue = getValueAt(0, column).getClass();
                } else {
                    returnValue = Object.class;
                }
                return returnValue;
            }
        };
        tModel.addTableModelListener(new TableModelListener() {
            public void tableChanged(TableModelEvent e) {
            }
        });

        table = new JTable(tModel);
        final TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(tModel);
        table.setRowSorter(sorter);
        for (int i = 0; i < table.getColumnCount(); i++) {
            tcol = table.getColumnModel().getColumn(i);
            tcol.setCellRenderer(new CustomTableCellRenderer());
        }
        table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                if (table.getSelectedRow() != -1) {
                    DB db = new DB();
                    Integer count[] = db.getNaklDescr((String) table.getValueAt(table.getSelectedRow(), 1));
                    lFoot.setText("Всего едениц: " + count[0] + "   В упаковках: " + (count[0] - count[2]) + "   Россыпью: " + count[2] + "   Упаковок:" + count[1]);
                }
            }
        });

        scrollTable = new JScrollPane(table);
        scrollTable.setBounds(x, y + 25, 280 + columns.size() * 70, 400);
        mainPanel.add(scrollTable);

        btnClose = new JButton("Закрыть");
        btnClose.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        lKod = new JLabel("Код:");
        lKod.setBounds(10, 470, 40, 20);

        tfKod = new JTextField(9);
        tfKod.setBounds(45, 470, 100, 20);
        tfKod.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(java.awt.event.KeyEvent evt) {
                if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
                    sorter.setRowFilter(RowFilter.regexFilter(tfKod.getText(), 3));
                } else if (evt.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    tfKod.setText("");
                    sorter.setRowFilter(null);
                }
            }
        });

        pTools = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnDocumentation = new JButton("Документы");
        btnDiscounts = new JButton("Скидки");
        add(pTools, BorderLayout.SOUTH);

        mainPanel.add(tfKod);
        mainPanel.add(lKod);

        JPopupMenu popupMenuTools = new JPopupMenu();

        JMenuItem miDetails = new JMenuItem("Детали");
        JMenuItem miExportReferences = new JMenuItem("Справки для ОВЭСа");
        JMenuItem miAnalysisExport = new JMenuItem("Анализ отгрузки концерну");
        JMenuItem miDetailAnalysisExport = new JMenuItem("Подробный анализ отгрузки концерну");
        JMenuItem miInvoice = new JMenuItem("Счет-фактура");
        JMenuItem miCurrencyRefresh = new JMenuItem("Пересчёт с завтрашним курсом");

        popupMenuTools.add(miDetails);
        popupMenuTools.add(miExportReferences);
        popupMenuTools.add(miAnalysisExport);
        popupMenuTools.add(miCurrencyRefresh);
        //popupMenuTools.add(miDetailAnalysisExport);
        popupMenuTools.addSeparator();
        popupMenuTools.add(miInvoice);

        miExportReferences.addActionListener(a -> {
            if (table.getSelectedRow() != -1) {
                try {
                    pb = new ProgressBar(temp, false, "Получение деталей...");
                    class SWorker extends SwingWorker<String, Object> {

                        String tn = new String();

                        public SWorker() {
                        }

                        @Override
                        protected String doInBackground() throws Exception {
                            ArrayList a = new ArrayList();
                            ArrayList a1 = new ArrayList();
                            HashMap hm = new HashMap();
                            dept.sklad.SkladDB sdb = new SkladDB();
                            String docNumber = table.getValueAt(table.getSelectedRow(), 1).toString().trim();
                            hm = sdb.getPutListInfo(docNumber);

                            // Проверяем, есть ли информация о документе в старой методике расчета
                            if (sdb.getOtpravAndPoluch(hm.get("ttn").toString()).size() > 0) {
                                hm.put("adres_otprav", sdb.getOtpravAndPoluch(hm.get("ttn").toString()).get(1));
                                hm.put("adres_poluch", sdb.getOtpravAndPoluch(hm.get("ttn").toString()).get(0));
                                a.add(hm);
                                String ttn = hm.get("ttn").toString();
                                Float nds = Float.valueOf(hm.get("nds").toString());
                                HashMap m = new HashMap();
                                m.put("kurs", hm.get("kurs_nbrb"));
                                a1 = sdb.getPrilojenieTTNRus(ttn, nds, m);
                                SkladOO soo = new SkladOO(a1, a);
                                soo.getCertificateForTTN("Справки для россии.ots");
                                JOptionPane.showMessageDialog(null, "Отчет сформирован.");
                            } else {

                                if (sdb.documentIsCalculated(docNumber) || sdb.documentIsClosed(docNumber)) {
                                    // Создаем провайдера данных
                                    SaleDocumentDataProvider provider = new SaleDocumentDataProvider();
                                    // Получаем данные по документу
                                    SaleDocumentReport reportData = provider.prepareDocument(docNumber, false);
                                    // Формируем справку
                                    new ReferenceTTNExportReport(reportData);

                                    JOptionPane.showMessageDialog(null, "Отчет сформирован.");
                                } else {
                                    JOptionPane.showMessageDialog(null, "Накладная ещё не расчитывалась!");
                                }
                            }
                            return null;
                        }

                        @Override
                        protected void done() {
                            try {
                                pb.dispose();
                            } catch (Exception ex) {
                                System.err.println("Ошибка при получении результатов из фонового потока " + ex);
                            }
                        }
                    }
                    SWorker sw = new SWorker();
                    sw.execute();
                    pb.setVisible(true);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            } else {
                JOptionPane.showMessageDialog(null, "Не выбрана накладная");
            }
        });

        miDetails.addActionListener(a -> {
            if (table.getSelectedRow() != -1) {
                DB db = new DB();
                OpenOffice oo = new OpenOffice("Накладная на отгрузку №" + table.getValueAt(table.getSelectedRow(), 1), db.getNaklAllDescr((String) table.getValueAt(table.getSelectedRow(), 1), 0));
                oo.createReport("NakladnieDescr.ots");
            }
        });

        miAnalysisExport.addActionListener(a -> {
            prepareAnalysis();
        });

        miDetailAnalysisExport.addActionListener(a -> {
            prepareDetailAnalysis();
        });

        miInvoice.addActionListener(a -> {
            createInvoice();
        });

        btnDocumentation.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                popupMenuTools.show(e.getComponent(), 0, e.getComponent().getHeight());
            }
        });

        btnDiscounts.addActionListener(a -> {
            VolumeOfPurchasesDialog dialog = new VolumeOfPurchasesDialog(controller);
            dialog.showDialog();
        });

        btnClose.setPreferredSize(Settings.BUTTON_HALF_BIG_SIZE);
        btnDocumentation.setPreferredSize(Settings.BUTTON_BIG_SIZE);

        pTools.add(btnDiscounts);
        pTools.add(btnDocumentation);
        pTools.add(btnClose);
        add(mainPanel, BorderLayout.CENTER);
    }

    private void prepareAnalysis() {
        String docNumber = table.getValueAt(table.getSelectedRow(), 1).toString().trim();
        class Task extends BackgroundTask {
            public Task(final String messageText) {
                super(messageText);
            }

            @Override
            protected Boolean doInBackground() throws Exception {
                // Создаем провайдера данных
                SaleDocumentDataProvider provider = new SaleDocumentDataProvider();
                // Получаем данные по документу
                SaleDocumentReport reportData = provider.prepareDocument(docNumber, true);
                // Расчет документа
                if (calculate(reportData)) {
                    // Формируем документ анализа
                    new AnalysisConcernReport(reportData);
                }
                return true;
            }
        }

        Task task = new Task("Обработка данных запроса...");
        task.executeTask();
    }

    private void prepareDetailAnalysis() {


        class Task extends BackgroundTask {
            public Task(final String messageText) {
                super(messageText);
            }

            @Override
            protected Boolean doInBackground() throws Exception {

                // Формируем период отбора
                DatePeriod period = new DatePeriod();
                period.setBegin(DateUtils.getDateByStringValue("01.01.2020"));
                period.setEnd(DateUtils.getDateByStringValue("30.06.2019"));

                DetailAnalysisDataSet dataReport = new DetailAnalysisDataSet();
                dataReport.setPeriod(period);
                dataReport.setFactRevenue(14964);
                dataReport.setProfit(-1736);
                dataReport.setCurrencyRate(2.0584);


                // JDBC
                SaleDocumentJDBC db = new SaleDocumentJDBC();

                // Получаем данные накладных за период
                List<AnalysisDetailItem> list = db.getAnalysisByPeriod(period);

                // Создаем провайдера данных
                //Формируем модель данных конечного отчета
                AnalysisDataProvider provider = new AnalysisDataProvider(list, dataReport, null);

                // Обновим себестоимость
                //AnalysisDataProvider.updatePrimeCost(this, list);
                // Обработка данных
                //Map<String, AnalysisDetailRegion> map = provider.getPartitionList();

                List<AnalysisReportPartition> partitionList = provider.getPartitionList();

                // Формирование отчета
                if (partitionList != null) {

                    new AnalysisDetailConcernReport(provider.getDataReport());
                }

                return true;
            }
        }

        Task task = new Task("Обработка данных запроса...");
        task.executeTask();
    }

    private boolean calculate(SaleDocumentReport data) {
        SaleDocumentBase document = data.getDocument();
        List<SaleDocumentDetailItemReport> detailList = data.getDetailList();

        MarketingPriceListMarchJDBC db = new MarketingPriceListMarchJDBC();
        CenaPDB cdb = new CenaPDB();
        SaleDocumentManager documentManager = new SaleDocumentManager();

        Date date = null;
        try {
            date = by.march8.api.utils.DateUtils.getFirstDay(document.getDocumentDate());
        } catch (Exception e) {
            System.err.println("Ошибка ввода даты");
        }

        float rate = documentManager.getCurrencyRateValue(2, date, false);
        document.setCurrencyRateFixed(rate);

        rate = documentManager.getCurrencyRateValue(2, new Date(), false);
        document.setCurrencyRateSale(rate);

        float fixedCurrencyRate = SaleDocumentCalculator.getValueCurrencyWithScale(2, document.getCurrencyRateFixed());
        float saleCurrencyRate = SaleDocumentCalculator.getValueCurrencyWithScale(2, document.getCurrencyRateSale());

//        System.out.println("Основной курс валюты :" + fixedCurrencyRate + " (" + document.getCurrencyRateFixed() + ")");
//        System.out.println("Вспомогательный курс валюты :" + saleCurrencyRate + " (" + document.getCurrencyRateSale() + ")");


        // Инициализация сервиса спец. цен
        SpecialPriceService specialPriceService = SpecialPriceService.getInstance();
        // Обновить сервис
        specialPriceService.updateService();
        // Есть ли специальные цены по контрагенту
        boolean isSpecialPrice = specialPriceService.isHaveSpecialPrice(document.getRecipientCode());
        System.out.println("Флаг наличия специальной цены по контрагенту [" + document.getRecipientCode() + "]: " + isSpecialPrice);
        // Блок инициализации российских цен
        boolean isNotVarietal = document.isNotVarietal();
        Reduction3GradeService gradeService = null;

        System.out.println("КУРСЫ " + fixedCurrencyRate + " - " + saleCurrencyRate);

        if (isNotVarietal) {
            gradeService = new Reduction3GradeService(CurrencyType.RUB);
        }

        for (SaleDocumentDetailItemReport item : detailList) {
            ProductionItemBase item_ = db.getPriceItemByArticleNumberAndSizeAndGrade(item.getArticleNumber(), item.getItemSize(), 1);
            if (item_ != null) {

                ProfitabilityItem value = cdb.getSstoimostRentabelAdv(item.getArticleCode(), item.getItemSize());
                if (value.getPrimeCostReference() > 0) {
                    value.setProfitabilityCalc(new BigDecimal(((item.getAccountingPrice() - value.getPrimeCostReference()) / value.getPrimeCostReference()) * 100)
                            .setScale(2, RoundingMode.HALF_UP).doubleValue());
                }

                if (value.getProfitabilityReference() > 0) {
                    value.setPrimeCostCalc(new BigDecimal((item.getAccountingPrice() * 100) / (value.getProfitabilityReference() + 100))
                            .setScale(4, RoundingMode.HALF_UP).doubleValue());
                }

                item.setPlanPrice(item_.getWholesalePrice());
                item.setPlanProfitability(RoundUtils.round(new BigDecimal(value.getProfitabilityCalc()).setScale(2, RoundingMode.HALF_UP).doubleValue(), 4));
                item.setPlanPrimeCost(new BigDecimal(value.getPrimeCostReference()).setScale(4, RoundingMode.HALF_UP).doubleValue());

                /*

                item.setPrimeCost(new BigDecimal(value.getPrimeCostReference()).setScale(4, RoundingMode.HALF_UP).doubleValue());
                item.setProfitability(new BigDecimal(value.getProfitabilityReference()).setScale(2, RoundingMode.HALF_UP).doubleValue());

                item.setPrimeCostCalc(new BigDecimal(value.getPrimeCostCalc()).setScale(4, RoundingMode.HALF_UP).doubleValue());
                item.setProfitabilityCalc(new BigDecimal(value.getProfitabilityCalc()).setScale(2, RoundingMode.HALF_UP).doubleValue());

                */
/*
                try {
                    float[] values = cdb.getSstoimostRentabel(item.getArticleCode(), item.getItemSize());
                    double primeCost = values[0];
                    double profitability = values[1];
                    if (profitability == 0) {
                        profitability = (((item_.getWholesalePrice() - primeCost) / primeCost) * 100);
                    }
                    //double primeCostCalculated = (item_.getWholesalePrice() * 100) / (profitability + 100);
                    item.setPlanPrice(item_.getWholesalePrice());
                    item.setPlanProfitability(RoundUtils.round(profitability, 4));
                    item.setPlanPrimeCost(RoundUtils.round(primeCost, 4));
                } catch (Exception e) {
                    e.printStackTrace();
                }
*/

                item.setValuePriceCurrency(-1);

                /*
                 **********************************************************************************************************
                 * ВАЛЮТНАЯ ЧАСТЬ
                 **********************************************************************************************************
                 */
                if (!isNotVarietal) {
                    // Если есть спеццена у контрагента
                    if (isSpecialPrice) {
                        double specialPriceValue = specialPriceService.getSpecialPriceByCriteria(document.getRecipientCode(),
                                item.getModelNumber(), item.getArticleName(), item.getItemGrade(), item.getItemSize(), item.getItemGrowz());
                        item.setValuePriceCurrency(specialPriceValue);
                    }

                    double specialPriceValue_ = specialPriceService.getGeneralSpecialPriceByCriteria(item.getModelNumber(),
                            item.getArticleName(), item.getItemGrade(), item.getItemSize(), item.getItemGrowz());
                    if (specialPriceValue_ > 0) {
                        item.setValuePriceCurrency(specialPriceValue_);
                    }

                    //System.out.println(item.getValuePriceCurrency());
                    // Если специальная цена не пределена или не установлена
                    if (item.getValuePriceCurrency() < 0) {
                        item.setValuePriceCurrency(
                                // Получаем цену в валюте
                                SaleDocumentCalculator.calculateCurrencyPriceFromPriceValue(
                                        // Получаем цену в рублях с учетом скидки
                                        SaleDocumentCalculator.calculatePriceAndDiscountValue(
                                                // Учетная оптовая цена
                                                item_.getWholesalePrice(),
                                                // Процент скидки по документу
                                                document.getDiscountValue(), document.getCalculationFactor()),
                                        //Курс на начало месяца
                                        fixedCurrencyRate));

                    }
                } else {
                    // Тут вызывается сервис на получение цены в валюте из классификатора по конкретному изделию
                    // Потому что цена посчитана заранее

                    if (document.getPriceReduction3Grade() > 1) {
                        // Получаем цену в валюте для изделий несорта,
                        // заранее посчитанную и указанную в классификаторе

                        Reduction3Grade gradeItem = gradeService.getTopGradeByItemId(document.getId(), item.getId());

                        if (gradeItem != null) {
                            item.setValuePriceCurrency(gradeItem.getPriceCurrency());

                            //item.setValuePriceForAccounting(calculatePriceAndDiscountValue(gradeItem.getPrice(), document.getPriceReduction3Grade()));
                            //item.setValuePriceForAccounting(round(item.getValuePriceForAccounting(), ROUND_XXX_XX));
                        }
                    }
                }

                item.setValuePriceCurrency(SaleDocumentCalculator.round(item.getValuePriceCurrency(), SaleDocumentCalculator.ROUND_XXX_XX));

                // Получаем количество позиций изделия
                int totalAmount = item.getAmount() * item.getAmountInPack();
                // Далее считаем сумму по изделию (количество * цена)
                item.setValueSumCostCurrency(SaleDocumentCalculator.round(SaleDocumentCalculator.calculateSumValue(item.getValuePriceCurrency(), totalAmount), SaleDocumentCalculator.ROUND_XXX_XX));

                // По документу НДС расчитывать не нужно, и поэтому в поле суммы НДС принимаем 0
                item.setValueSumVatCurrency(0);
                item.setValueVAT(0);


                // Далее считаем итоговую сумму по изделию, сумма по изделию + сумма НДС
                item.setValueSumCostAndVatCurrency(
                        SaleDocumentCalculator.calculateTotalValue(item.getValueSumCostCurrency(), item.getValueSumVatCurrency()));

                /*
                 **********************************************************************************************************
                 * РУБЛЕВАЯ ЧАСТЬ
                 **********************************************************************************************************
                 */
                // Для определения цены изделия в рублях используем курс соответствующий курсу валюты на момент отгрузки
                // И так как изначально скидка по контрагенту посчитана в валюте, то больше ее не считаем
                // Получаем цену изделия в рублях через цену в валюте
                item.setValuePrice(
                        SaleDocumentCalculator.round(
                                SaleDocumentCalculator.calculatePriceFromCurrencyPriceValue(
                                        item.getValuePriceCurrency(), saleCurrencyRate), SaleDocumentCalculator.ROUND_XXX_XX));
                System.out.println("ЦЕНА РБ " + item.getValuePrice());
                // Далее считаем сумму по изделию (количество * цена)
                item.setValueSumCost(
                        SaleDocumentCalculator.calculateSumValue(
                                item.getValuePrice(), totalAmount));

                // По документу НДС расчитывать не нужно, и поэтому в поле суммы НДС принимаем 0
                item.setValueSumVat(0);
                item.setValueVAT(0);

                // Округление суммы НДС
                item.setValueSumVat(
                        SaleDocumentCalculator.round(
                                item.getValueSumVat(), SaleDocumentCalculator.ROUND_XXX_XX));

                // Далее считаем итоговую сумму по изделию, сумма по изделию + сумма НДС
                item.setValueSumCostAndVat(
                        SaleDocumentCalculator.round(
                                SaleDocumentCalculator.calculateTotalValue(
                                        item.getValueSumCost(), item.getValueSumVat()), SaleDocumentCalculator.ROUND_XXX_XX));
                // Торговая надбавка
                //item.setTradeMarkValue(0f);


            }


        }

        return true;
    }

    private void createInvoice() {
        String code = tfKod.getText().trim();
        int codeNumber = 0;
        if (!code.isEmpty()) {
            try {
                codeNumber = Integer.valueOf(code);
            } catch (Exception e) {
                e.printStackTrace();
                codeNumber = -1;
            }
        }

        if (codeNumber < 1) {
            JOptionPane.showMessageDialog(null, "Укажите код контрагента, для которого необходимо сформировать счет-фактуру");
            return;
        }

        // Определим на какой накладной курсор
        String docType = table.getValueAt(table.getSelectedRow(), 2).toString().trim();
        boolean isRefund = false;
        if (docType.equals("Возврат от покупателя")) {
            isRefund = true;
        }


        // Покажем форму с накладными для контрагента
        SaleDocumentPicker picker = new SaleDocumentPicker(controller, false);
        Set<SaleDocumentEntity> set_;
        if (isRefund) {
            set_ = picker.selectRefundDocuments(codeNumber, DateUtils.getDateByStringValue(ftDate.getValue().toString()), false);
        } else {
            set_ = picker.selectSaleDocuments(codeNumber, DateUtils.getDateByStringValue(ftDate.getValue().toString()), false);
        }

        class Task extends BackgroundTask {
            public Task(final String messageText) {
                super(messageText);
            }

            @Override
            protected Boolean doInBackground() throws Exception {
                provider = new SaleDocumentDataProvider();
                if (set_ != null) {
                    Map<String, ProductItemOVES> workMap = new HashMap<>();
                    SaleDocumentReport template = null;

                    for (SaleDocumentEntity entity : set_) {
                        try {
                            System.out.println("processing for document [" + entity.getDocumentNumber() + "]");
                            SaleDocumentReport report = provider.prepareDocument(entity.getId(), false);
                            if (template == null) {
                                template = report;
                            }

                            if (report != null) {

                                for (SaleDocumentDetailItemReport item : report.getDetailList()) {
                                    String key = item.getTnvedCode() + "_" + item.getAccountingVat();
                                    ProductItemOVES product = workMap.get(key);
                                    if (product == null) {
                                        product = new ProductItemOVES();
                                        product.setCode(item.getTnvedCode());
                                        product.setName(ItemNameReplacer.transform(item.getItemName()));
                                        product.setMaterial(item.getComposition());
                                        workMap.put(key, product);
                                    } else {
                                        String transName = ItemNameReplacer.transform(item.getItemName());
                                        if (!product.getName().contains(transName)) {
                                            product.setName(product.getName() + ", " + transName);
                                        }
                                    }

                                    product.setCost(product.getCost() + item.getValueSumCostAndVatCurrency());
                                    product.setAmount(product.getAmount() + item.getAmountPrint());
                                    product.setPlaceCount(product.getPlaceCount() + item.getAmount());
                                    product.setWeight(product.getWeight() + item.getWeight());
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            System.out.println("error processing for document [" + entity.getDocumentNumber() + "]");
                        }
                        System.out.println(entity.getDocumentNumber() + " - " + entity.getDocumentDate() + " - " + entity.getId());
                    }
                    new InvoiceOVESReport(workMap, template);
                }
                return true;
            }
        }

        Task task = new Task("Обработка выбранных документов...");
        task.executeTask();
        // Пользователь выберет нужные и погнали
    }
}
