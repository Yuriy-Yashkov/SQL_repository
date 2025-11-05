package by.march8.ecs.application.modules.warehouse.external.shipping.analysis;

import by.march8.api.utils.DatePeriod;
import by.march8.api.utils.DateUtils;
import by.march8.ecs.application.modules.warehouse.external.shipping.dao.AbstractPJDBC;
import by.march8.ecs.application.modules.warehouse.external.shipping.manager.SaleDocumentDataProvider;
import by.march8.entities.warehouse.SaleDocumentAnalysisItem;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SaleMonitorVESAnalysis extends AbstractDataAnalysis {

    private final SaleDocumentDataProvider provider;

    private final String CATEGORY_UNDERWEAR = "41";
    private final String CATEGORY_KNITWEAR = "42";
    private final String CATEGORY_SOCK = "43";
    private final String CATEGORY_SOCK_CHILD = "433";

    private final String CATEGORY_REFUSE = "47";
    private final String CATEGORY_CANVAS = "48";
    private final String[] years;

    private HashMap<String, HashMap<String, AnalysisItem>> workMap;

    public SaleMonitorVESAnalysis() {
        provider = new SaleDocumentDataProvider();


        years = new String[]{"2017", "2018", "2019"};
        workMap = new HashMap<>();


        doProcessing();
    }


    private void doProcessing() {

        for (String s : years) {
            workMap.put(s, new HashMap<>());
        }

        // Получаем номера накладных для обработки
        DatePeriod period = new DatePeriod();
        period.setBegin(DateUtils.getDateByStringValue("01.01.2017"));
        period.setEnd(DateUtils.getDateByStringValue("31.01.2020"));
        List<SaleDocumentAnalysisItem> list = getExportSaleDocumentListByPeriod(period.getBegin(), period.getEnd());

        Set<Date> dates = new HashSet<>();
        for (SaleDocumentAnalysisItem item : list) {
            dates.add(item.getDate());
        }

        // Экспортные отгрузки за период 2017-2019

        // В цикле формируем результирующий набор данных
        AbstractPJDBC db = new AbstractPJDBC();
        HashMap<Date, Double> currencyRateList = db.getCurrencyRateList(15, dates);
        System.out.println();

/*        System.out.println("processing for document [" + entity.getDocumentNumber() + "]");
        SaleDocumentShippingReport report = provider.prepareDocument(entity.getId(), false);
        SaleDocumentBase document = report.getDocument();

        float rate = manager.getCurrencyRateValue(3, document.getDocumentSaleDate(), false);*/

        for (SaleDocumentAnalysisItem item : list) {
            try {
                HashMap<String, AnalysisItem> map_ = workMap.get(DateUtils.getYearAsStringByDate(item.getDate()));

                try {
                    double rate = currencyRateList.get(item.getDate());
                    if (rate > 0) {
                        item.setCostCurrency(item.getCost() / rate);
                    } else {
                        item.setCostCurrency(0);
                        System.out.println("Нулевой курс");
                    }
                } catch (Exception c) {
                    System.out.println("Курс не найден: " + item.getDate());
                }

                String category = item.getItemCategory();
                // Отбор для 41 Белье
                if (isItemBelongCategory(category, CATEGORY_UNDERWEAR)) {
                    addItemToAnalysis(map_, item, CATEGORY_UNDERWEAR);
                }

                // Отбор для 42 Трикотаж
                if (isItemBelongCategory(category, CATEGORY_KNITWEAR)) {
                    addItemToAnalysis(map_, item, CATEGORY_KNITWEAR);
                }

                // Отбор для 43 Чулочка
                if (isItemBelongCategory(category, CATEGORY_SOCK)) {
                    addItemToAnalysis(map_, item, CATEGORY_SOCK);
                }

                // Отбор для 433 Тесткая чулочка
                if (isItemBelongCategory(category, CATEGORY_SOCK_CHILD)) {
                    addItemToAnalysis(map_, item, CATEGORY_SOCK_CHILD);
                }

                // Отбор для 47 Тесткая чулочка
                if (isItemBelongCategory(category, CATEGORY_CANVAS)) {
                    addItemToAnalysis(map_, item, CATEGORY_CANVAS);
                }

                // Отбор для 48 Тесткая чулочка
                if (isItemBelongCategory(category, CATEGORY_REFUSE)) {
                    addItemToAnalysis(map_, item, CATEGORY_REFUSE);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        for (String y : years) {
            HashMap<String, AnalysisItem> map = workMap.get(y);
            System.out.println(y + "****************************************************");
            AnalysisItem iUnderWear = map.get(CATEGORY_UNDERWEAR);
            if (iUnderWear != null) {
                System.out.println(CATEGORY_UNDERWEAR + "\t" + iUnderWear.getAmount() + "\t" + iUnderWear.getCost() + "\t" + iUnderWear.getCostCurrency());
            }

            AnalysisItem iKnitWear = map.get(CATEGORY_KNITWEAR);
            if (iKnitWear != null) {
                System.out.println(CATEGORY_KNITWEAR + "\t" + iKnitWear.getAmount() + "\t" + iKnitWear.getCost() + "\t" + iKnitWear.getCostCurrency());
            }

            AnalysisItem iSock = map.get(CATEGORY_SOCK);
            if (iSock != null) {
                System.out.println(CATEGORY_SOCK + "\t" + iSock.getAmount() + "\t" + iSock.getCost() + "\t" + iSock.getCostCurrency());
            }

            AnalysisItem iSockChild = map.get(CATEGORY_SOCK_CHILD);
            if (iSockChild != null) {
                System.out.println(CATEGORY_SOCK_CHILD + "\t" + iSockChild.getAmount() + "\t" + iSockChild.getCost() + "\t" + iSockChild.getCostCurrency());
            }

            AnalysisItem iCanvas = map.get(CATEGORY_CANVAS);
            if (iCanvas != null) {
                System.out.println(CATEGORY_CANVAS + "\t" + iCanvas.getAmount() + "\t" + iCanvas.getCost() + "\t" + iCanvas.getCostCurrency());
            }

            AnalysisItem iRefuse = map.get(CATEGORY_REFUSE);
            if (iRefuse != null) {
                System.out.println(CATEGORY_REFUSE + "\t" + iRefuse.getAmount() + "\t" + iRefuse.getCost() + "\t" + iRefuse.getCostCurrency());
            }
        }

        // Выгрузка данных в ексель
    }


    private void addItemToAnalysis(HashMap<String, AnalysisItem> map, SaleDocumentAnalysisItem item, String category) {
        AnalysisItem item_ = map.get(category);
        if (item_ == null) {
            item_ = new AnalysisItem();
            map.put(category, item_);
        }

        if (category.equals("47")) {
            item_.setAmount(item_.getAmount() + (item.getAmount() / 1000));
        } else if (category.equals("48")) {
            item_.setAmount(item_.getAmount() + (item.getAmount() / 10));
        } else {
            item_.setAmount(item_.getAmount() + (item.getAmount() / 1000));
        }

        item_.setCost(item_.getCost() + item.getCost() / 1000);
        item_.setCostCurrency(item_.getCostCurrency() + (item.getCostCurrency() / 1000));
    }

    private boolean isItemBelongCategory(String articleSegment, String category) {

        return articleSegment.contains(category);
    }


    public class AnalysisItem {

        private String period;
        private double amount;
        private double cost;
        private double costCurrency;


        public String getPeriod() {
            return period;
        }

        public void setPeriod(String period) {
            this.period = period;
        }

        public double getAmount() {
            return amount;
        }

        public void setAmount(double amount) {
            this.amount = amount;
        }

        public double getCost() {
            return cost;
        }

        public void setCost(double cost) {
            this.cost = cost;
        }

        public double getCostCurrency() {
            return costCurrency;
        }

        public void setCostCurrency(double costCurrency) {
            this.costCurrency = costCurrency;
        }
    }
}
