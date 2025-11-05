package dept.ves.model;

import by.march8.ecs.application.modules.references.classifier.model.ClassifierParser;
import by.march8.ecs.application.modules.references.classifier.model.ProductCategoryItem;
import by.march8.ecs.application.modules.warehouse.external.shipping.dao.SaleDocumentJDBC;
import by.march8.ecs.framework.common.BackgroundTask;
import dept.marketing.cena.CenaPDB;
import dept.marketing.cena.ProfitabilityItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static dept.ves.model.AnalysisConstants.REGION_1;
import static dept.ves.model.AnalysisConstants.REGION_10;
import static dept.ves.model.AnalysisConstants.REGION_11;
import static dept.ves.model.AnalysisConstants.REGION_12;
import static dept.ves.model.AnalysisConstants.REGION_13;
import static dept.ves.model.AnalysisConstants.REGION_14;
import static dept.ves.model.AnalysisConstants.REGION_15;
import static dept.ves.model.AnalysisConstants.REGION_16;
import static dept.ves.model.AnalysisConstants.REGION_17;
import static dept.ves.model.AnalysisConstants.REGION_18;
import static dept.ves.model.AnalysisConstants.REGION_19;
import static dept.ves.model.AnalysisConstants.REGION_2;
import static dept.ves.model.AnalysisConstants.REGION_3;
import static dept.ves.model.AnalysisConstants.REGION_4;
import static dept.ves.model.AnalysisConstants.REGION_5;
import static dept.ves.model.AnalysisConstants.REGION_6;
import static dept.ves.model.AnalysisConstants.REGION_7;
import static dept.ves.model.AnalysisConstants.REGION_8;
import static dept.ves.model.AnalysisConstants.REGION_9;

public class AnalysisDataProvider {
    private final DetailAnalysisDataSet dataReport;
    private Map<String, AnalysisDetailRegion> map;
    private Map<Integer, String> regionKeyMap;
    private List<AnalysisReportPartition> partitions = null;

    public AnalysisDataProvider(List<AnalysisDetailItem> list, DetailAnalysisDataSet dataReport_, BackgroundTask task) {
        this.dataReport = dataReport_;

        map = mapInitialisation();
        regionKeyMap = regionMapInitialisation();
        partitions = partitionsInitialisation();
        dataReport.setPartitionList(partitions);

        int recordCount = list.size();
        int currentRecord = 1;

        double totalSale = 0;
        double totalSaleVat = 0;
        double totalPrimeCost = 0;

        for (AnalysisDetailItem item : list) {


            if (task != null) {
                task.setText("Обработка " + currentRecord + " из " + recordCount);
            }
            String key = String.valueOf(item.getArticleCode()).substring(0, 2);
            if (key.equals("48")) {
                continue;//;key = "47";
            }

            if (key.equals("45")) {
                key = "41";
            }

            ProductCategoryItem item_ = new ProductCategoryItem();
            item_.setRawName(item.getItemName().trim());
            ClassifierParser.prepareSetInformation(item_);


            AnalysisDetailRegion region = map.get(getRegionKeyByItem(item));
            if (region != null) {
                AnalysisDetailsGroup group = region.getGroups().get(key);
                if (group != null) {
                    if (key.equals("47")) {
                        // Для полотен количество считаем как вес
                        group.setAmountSet(0);
                        group.setAmountItem(group.getAmountItem() + ((double) (item.getAmount() / 100) / 1000));
                    } else {
                        int size = item_.getSetSize();
                        if (size > 0) {
                            group.setAmountSet(group.getAmountSet() + (item_.getSetSize() * item.getAmount()));
                        } else {
                            group.setAmountSet(group.getAmountSet() + item.getAmount());
                        }
                        group.setAmountItem(group.getAmountItem() + item.getAmount());
                    }

                    // Объем без НДС
                    group.setSumCost(group.getSumCost() + (item.getSumCost()));
                    // Объем с НДС
                    group.setSumCostAndVat(group.getSumCostAndVat() + (item.getSumCostAndVat()));

                    // Плановая себестоимость, сумма
                    if (key.equals("47")) {
                        group.setPrimeCostPlan(group.getPrimeCostPlan() + ((item.getPrimeCost() * (item.getAmount() / 100)) / 1000));
                        totalPrimeCost += ((item.getPrimeCost() * (item.getAmount() / 100)) / 1000);
                    } else {
                        group.setPrimeCostPlan(group.getPrimeCostPlan() + (item.getPrimeCost() * item.getAmount() / 1000));
                        totalPrimeCost += (item.getPrimeCost() * item.getAmount() / 1000);
                    }


                    totalSale += (item.getSumCost() / 1000);
                    totalSaleVat += (item.getSumCostAndVat() / 1000);

                } else {
                    System.out.println("Ошибка 2 для " + key);
                }
            } else {
                System.out.println("Ошибка 1");
            }


            currentRecord++;
        }

        double factRevenue = 12982;
        double factRevenueVat = 14964;

        double factPrimeCost = 14718;

        for (int i = 1; i <= map.size() + 1; i++) {
            AnalysisDetailRegion region = map.get(String.valueOf(i));
            if (region != null) {
                calculateRegion(region,
                        dataReport.getCurrencyRate(),
                        totalSale, totalSaleVat, totalPrimeCost,
                        factRevenue, factRevenueVat, factPrimeCost);
            }
        }

        prepareReportPartitions(dataReport, map);
    }

    public static void updatePrimeCost(BackgroundTask task, List<AnalysisDetailItem> list) {
/*
        for(AnalysisDetailItem item: list){
            System.out.println(item.getArticleCode()+" - "+item.getSize());
        }*/

        SaleDocumentJDBC db = new SaleDocumentJDBC();

        db.updatePrimeCostBatch(task, list);


        int count = list.size();
        int current = 1;
        CenaPDB cdb = new CenaPDB();

        for (AnalysisDetailItem item : list) {
            task.setText("Обработка " + current + " из " + count);
            current++;
            ProfitabilityItem value = cdb.getSstoimostRentabelAdv(item.getArticleCode(), item.getSize());
            if (value != null) {
                db.updatePrimeCost(item.getArticleCode(), item.getSize(), value.getPrimeCostReference());
            }
        }
    }

    private void calculateRegion(AnalysisDetailRegion region, double currencyRate, double totalSale, double totalSaleVat, double totalPrimeCost, double factRevenue, double factRevenueVat, double factPrimeCost) {
        HashMap<String, AnalysisDetailsGroup> groups = region.getGroups();
        String[] aGroups = new String[]{"41", "42", "43", "47"};

        for (String groupKey : aGroups) {
            AnalysisDetailsGroup group = groups.get(groupKey);

            group.setSumCost(group.getSumCost() / 1000);
            group.setSumCostAndVat(group.getSumCostAndVat() / 1000);
            group.setSumCostAndVatCurrency(group.getSumCostAndVat() / currencyRate);

            group.setPercentRevenue((group.getSumCost()) * 100 / totalSale);
            group.setPercentRevenueVat((group.getSumCostAndVat()) * 100 / totalSaleVat);
            group.setPercentPrimeCost((group.getPrimeCostPlan()) * 100 / totalPrimeCost);

            group.setRevenue(factRevenue * group.getPercentRevenue() / 100);
            group.setRevenueVat(factRevenueVat * group.getPercentRevenueVat() / 100);
            group.setPrimeCost(factPrimeCost * group.getPercentPrimeCost() / 100);

            group.setProfit(group.getRevenue() - group.getPrimeCost());

            group.setProfitability(group.getProfit() / group.getRevenueVat() * 100);

            region.setSumCost(region.getSumCost() + group.getSumCost());
            region.setSumCostAndVat(region.getSumCostAndVat() + group.getSumCostAndVat());
            region.setSumCostAndVatCurrency(region.getSumCostAndVatCurrency() + group.getSumCostAndVatCurrency());

            region.setRevenue(region.getRevenue() + group.getRevenue());
            region.setRevenueVat(region.getRevenueVat() + group.getRevenueVat());

            region.setPrimeCost(region.getPrimeCost() + group.getPrimeCost());
            region.setPrimeCostPlan(region.getPrimeCostPlan() + group.getPrimeCostPlan());

            region.setProfit(region.getProfit() + group.getProfit());
        }
    }

    private void calculateRegion(AnalysisDetailRegion region, double currencyRate, double total, double profit) {
        HashMap<String, AnalysisDetailsGroup> groups = region.getGroups();
        String[] aGroups = new String[]{"41", "42", "43", "47"};
        double sumCost_ = 0.0;
        double sumCostCurrency_ = 0.0;
        double revenue_ = 0.0;
        double profit_ = 0.0;

        for (String groupKey : aGroups) {
            AnalysisDetailsGroup group = groups.get(groupKey);
            group.setSumCost(group.getSumCost() / 1000);
            group.setSumCostAndVatCurrency(group.getSumCost() / currencyRate);


            group.setRevenue(group.getRevenue() / 1000);
            //group.setFactRevenue(revenue * group.getPercentPrimeCost() / 100);
            //group.setPrimeCost()
            //group.setProfit(profit * group.getPercentPrimeCost() / 100);
            group.setPercentPrimeCost((group.getPrimeCostPlan()) * 100 / total);
            group.setPrimeCostPlan(profit * group.getPercentPrimeCost() / 100);

            group.setProfit(group.getRevenue() - group.getPrimeCostPlan());


            group.setProfitability(group.getProfit() / group.getSumCost() * 100);

            sumCost_ += group.getSumCost();
            sumCostCurrency_ += group.getSumCostAndVatCurrency();
            revenue_ += group.getRevenue();
            profit_ += group.getProfit();
        }

        region.setSumCost(sumCost_);
        region.setSumCostAndVatCurrency(sumCostCurrency_);
        region.setRevenue(revenue_);
        region.setProfit(profit_);
    }

    private void prepareReportPartitions(DetailAnalysisDataSet dataSet, Map<String, AnalysisDetailRegion> map) {


        for (AnalysisReportPartition partition : dataSet.getPartitionList()) {


            for (String regionKey : partition.getRegionSequence()) {
                AnalysisDetailRegion region = map.get(regionKey);
                partition.getData().add(region);

                partition.setSumCost(partition.getSumCost() + region.getSumCost());
                partition.setSumCostAndVat(partition.getSumCostAndVat() + region.getSumCostAndVat());
                partition.setSumCostAndVatCurrency(partition.getSumCostAndVatCurrency() + region.getSumCostAndVatCurrency());

                partition.setRevenue(partition.getRevenue() + region.getRevenue());
                partition.setRevenueVat(partition.getRevenueVat() + region.getRevenueVat());

                partition.setPrimeCost(partition.getPrimeCost() + region.getPrimeCost());
                partition.setPrimeCostPlan(partition.getPrimeCostPlan() + region.getPrimeCostPlan());

                partition.setProfit(partition.getProfit() + region.getProfit());

            }


            dataSet.setTotalSumCost(dataSet.getTotalSumCost() + partition.getSumCost());
            dataSet.setTotalSumCostAndVat(dataSet.getTotalSumCostAndVat() + partition.getSumCostAndVat());
            dataSet.setTotalSumCostAndVatCurrency(dataSet.getTotalSumCostAndVatCurrency() + partition.getSumCostAndVatCurrency());

            dataSet.setTotalRevenue(dataSet.getTotalRevenue() + partition.getRevenue());
            dataSet.setTotalRevenueVat(dataSet.getTotalRevenueVat() + partition.getRevenueVat());

            dataSet.setTotalPrimeCost(dataSet.getTotalPrimeCost() + partition.getPrimeCost());
            dataSet.setTotalPrimeCostPlan(dataSet.getTotalPrimeCostPlan() + partition.getPrimeCostPlan());

            dataSet.setTotalProfit(dataSet.getTotalProfit() + partition.getProfit());


            if (partition.getOrder().equals(REGION_18)) {

                dataSet.setCountrySumCost(partition.getSumCost());
                dataSet.setCountrySumCostAndVat(partition.getSumCostAndVat());
                dataSet.setCountrySumCostAndVatCurrency(partition.getSumCostAndVatCurrency());

                dataSet.setCountryRevenue(partition.getRevenue());
                dataSet.setCountryRevenueVat(partition.getRevenueVat());

                dataSet.setCountryPrimeCostPlan(partition.getPrimeCostPlan());
                dataSet.setCountryPrimeCost(partition.getPrimeCost());

                dataSet.setCountryProfit(partition.getProfit());
            }
        }
    }

    private String getRegionKeyByItem(AnalysisDetailItem item) {
        if (item.getExport() > 0) {
            String regionKey = regionKeyMap.get(item.getContractorCode());
            if (regionKey == null) {
                return REGION_19;
            } else {
                return regionKey;
            }
        } else {
            return REGION_18;
        }
    }

    public List<AnalysisReportPartition> getPartitionList() {
        return partitions;
    }

    private Map<Integer, String> regionMapInitialisation() {
        Map<Integer, String> result = new HashMap<>();

        result.put(5015, REGION_1);
        result.put(5034, REGION_1);
        result.put(4955, REGION_1);
        result.put(4163, REGION_1);
        result.put(4272, REGION_1);
        result.put(3920, REGION_1);
        result.put(5032, REGION_1);
        result.put(4176, REGION_1);
        result.put(1457, REGION_1);
        result.put(4120, REGION_1);
        result.put(1817, REGION_1);
        result.put(2012, REGION_1);
        result.put(3914, REGION_1);
        result.put(4125, REGION_1);
        result.put(4525, REGION_1);
        result.put(5199, REGION_1);
        result.put(5037, REGION_1);
        result.put(5035, REGION_1);
        result.put(5026, REGION_1);
        result.put(5036, REGION_1);
        result.put(4115, REGION_1);


        result.put(1624, REGION_2);
        result.put(1433, REGION_2);
        result.put(5025, REGION_2);

        result.put(4888, REGION_3);
        result.put(1455, REGION_3);
        result.put(1484, REGION_3);
        result.put(1527, REGION_3);
        result.put(4106, REGION_3);
        result.put(5019, REGION_3);

        result.put(4872, REGION_4);
        result.put(3913, REGION_4);
        result.put(1621, REGION_4);
        result.put(4062, REGION_4);
        result.put(5010, REGION_4);
        result.put(4094, REGION_4);
        result.put(5133, REGION_4);

        result.put(3971, REGION_5);

        result.put(2009, REGION_7);
        result.put(1906, REGION_7);
        result.put(4844, REGION_7);
        result.put(1665, REGION_7);
        result.put(1666, REGION_7);

        result.put(1951, REGION_8);
        result.put(2071, REGION_8);
        result.put(3907, REGION_8);
        result.put(2217, REGION_8);

        result.put(4943, REGION_9);

        result.put(2199, REGION_10);


        result.put(4262, REGION_11);
        result.put(4266, REGION_11);
        result.put(1814, REGION_11);

        result.put(5031, REGION_15);
        result.put(4959, REGION_15);
        return result;
    }

    private HashMap<String, AnalysisDetailRegion> mapInitialisation() {

        HashMap<String, AnalysisDetailRegion> result = new HashMap<String, AnalysisDetailRegion>();
        result.put(AnalysisConstants.REGION_1, new AnalysisDetailRegion(REGION_1, "Центральный федеральный округ"));
        result.put(AnalysisConstants.REGION_2, new AnalysisDetailRegion(REGION_2, "Приволжский федеральный округ"));
        result.put(AnalysisConstants.REGION_3, new AnalysisDetailRegion(REGION_3, "Северозападный федеральный округ"));
        result.put(AnalysisConstants.REGION_4, new AnalysisDetailRegion(REGION_4, "Южный федеральный округ"));
        result.put(AnalysisConstants.REGION_5, new AnalysisDetailRegion(REGION_5, "Северо-кавказский федеральный округ"));
        result.put(AnalysisConstants.REGION_6, new AnalysisDetailRegion(REGION_6, "Крым и г.Севастополь"));
        result.put(AnalysisConstants.REGION_7, new AnalysisDetailRegion(REGION_7, "Уральский федеральный округ"));
        result.put(AnalysisConstants.REGION_8, new AnalysisDetailRegion(REGION_8, "Сибирский федеральный округ"));
        result.put(AnalysisConstants.REGION_9, new AnalysisDetailRegion(REGION_9, "Дальневосточный федеральный округ"));
        result.put(AnalysisConstants.REGION_10, new AnalysisDetailRegion(REGION_10, "Украина"));
        result.put(AnalysisConstants.REGION_11, new AnalysisDetailRegion(REGION_11, "Казахстан"));
        result.put(AnalysisConstants.REGION_12, new AnalysisDetailRegion(REGION_12, "Азербайжан"));
        result.put(AnalysisConstants.REGION_13, new AnalysisDetailRegion(REGION_13, "Эстония"));
        result.put(AnalysisConstants.REGION_14, new AnalysisDetailRegion(REGION_14, "Литва"));
        result.put(AnalysisConstants.REGION_15, new AnalysisDetailRegion(REGION_15, "Латвия"));
        result.put(AnalysisConstants.REGION_16, new AnalysisDetailRegion(REGION_16, "Финляндия"));
        result.put(AnalysisConstants.REGION_17, new AnalysisDetailRegion(REGION_17, "Чехия"));
        result.put(AnalysisConstants.REGION_18, new AnalysisDetailRegion(REGION_18, "Внутренний рынок"));
        result.put(AnalysisConstants.REGION_19, new AnalysisDetailRegion(REGION_19, "Прочие экспортные"));

        return result;
    }

    private List<AnalysisReportPartition> partitionsInitialisation() {
        List<AnalysisReportPartition> result = new ArrayList<>();
        result.add(new AnalysisReportPartition("Российская Федерация", REGION_1 + ";" + REGION_2 + ";" + REGION_3 + ";" + REGION_4 + ";" + REGION_5 + ";" + REGION_6 + ";" + REGION_7 + ";" + REGION_8 + ";" + REGION_9));
        result.add(new AnalysisReportPartition("Страны ближнего зарубежья", REGION_10 + ";" + REGION_11 + ";" + REGION_12));
        result.add(new AnalysisReportPartition("Страны дальнего зарубежья", REGION_13 + ";" + REGION_14 + ";" + REGION_15 + ";" + REGION_16 + ";" + REGION_17));
        result.add(new AnalysisReportPartition("Прочие экспортные", REGION_19));
        result.add(new AnalysisReportPartition("Внутренний рынок РБ", REGION_18));
        return result;
    }

    public DetailAnalysisDataSet getDataReport() {
        return dataReport;
    }
}
