package by.march8.ecs.application.modules.warehouse.external.shipping.reports;

import by.gomel.freedev.ucframework.uccore.report.model.ReportProperties;
import by.march8.api.utils.DateUtils;
import com.sun.star.beans.XPropertySet;
import com.sun.star.container.XNamed;
import com.sun.star.lang.XComponent;
import com.sun.star.sheet.XSpreadsheet;
import com.sun.star.sheet.XSpreadsheetDocument;
import com.sun.star.sheet.XSpreadsheets;
import com.sun.star.table.CellHoriJustify;
import com.sun.star.table.XCell;
import com.sun.star.uno.UnoRuntime;
import dept.ves.model.AnalysisDetailRegion;
import dept.ves.model.AnalysisDetailsGroup;
import dept.ves.model.AnalysisReportPartition;
import dept.ves.model.DetailAnalysisDataSet;

import java.util.Date;
import java.util.List;

public class AnalysisDetailConcernReport extends AbstractInvoiceReport {

    private final List<AnalysisReportPartition> partitionList;
    private final DetailAnalysisDataSet reportData;

    public AnalysisDetailConcernReport(DetailAnalysisDataSet reportData) {
        super();
        this.reportData = reportData;
        this.partitionList = reportData.getPartitionList();
        createCustom();
    }

    @Override
    ReportProperties prepareProperties(ReportProperties properties) {
        // Устанавливаем имя файла шаблона
        System.out.println("Берем шаблон");
        properties.setBlankName("analysis_detail_concern_" + by.march8.api.utils.DateUtils.getMessageDateTime(new Date()));
        properties.getTemplate().setTemplateName("analysisDetailsConcern.ots");
        return properties;
    }

    @Override
    boolean populateData(XComponent component) {
        try {
            XSpreadsheetDocument xSpreadsheetDocument = UnoRuntime
                    .queryInterface(XSpreadsheetDocument.class, component);
            XSpreadsheets xSpreadsheets = xSpreadsheetDocument.getSheets();
            Object sheet = xSpreadsheets.getByName("Лист1");
            XSpreadsheet xSpreadsheet = UnoRuntime
                    .queryInterface(XSpreadsheet.class, sheet);

            XNamed xNamed = UnoRuntime.queryInterface(XNamed.class,
                    xSpreadsheet);

            xNamed.setName("Анализ данных");

            XCell xCell = xSpreadsheet.getCellByPosition(0, 2);
            xCell.setFormula(" за период с " + DateUtils.getNormalDateFormat(reportData.getPeriod().getBegin())
                    + " по "
                    + DateUtils.getNormalDateFormat(reportData.getPeriod().getEnd())
                    + "гг.");

            int row = 9;

            for (AnalysisReportPartition partition : partitionList) {
                xCell = xSpreadsheet.getCellByPosition(0, row);
                xCell.setFormula(partition.getName());
                mergeCellRange(xSpreadsheet, 0, row, 12, row);
                setFontBoldForCellRange(xSpreadsheet, 0, 12, row);
                row++;
                for (AnalysisDetailRegion region : partition.getData()) {
                    xCell = xSpreadsheet.getCellByPosition(0, row);
                    xCell.setFormula(region.getName());
                    mergeCellRange(xSpreadsheet, 0, row, 0, row + 3);

                    insertData(xSpreadsheet, region.getGroups().get("41"), 41, row);
                    insertData(xSpreadsheet, region.getGroups().get("42"), 42, row + 1);
                    insertData(xSpreadsheet, region.getGroups().get("43"), 43, row + 2);
                    insertData(xSpreadsheet, region.getGroups().get("47"), 47, row + 3);
                    row += 4;
                }
                insertTotalInformation(xSpreadsheet, partition, row);
                row += 2;
            }

            insertFooter(xSpreadsheet, reportData, row);
            row++;
            setFullBorderForCellRange(xSpreadsheet, 0, 12, 9, row);

            xCell = xSpreadsheet.getCellByPosition(0, row + 3);
            xCell.setFormula("Директор ОАО \"8 Марта\"");
            xPropSet = UnoRuntime
                    .queryInterface(XPropertySet.class,
                            xCell);
            xPropSet.setPropertyValue("HoriJustify", CellHoriJustify.RIGHT);


            xCell = xSpreadsheet.getCellByPosition(4, row + 3);
            xCell.setFormula("__________________/ С.Ю. Комков/");
            xPropSet = UnoRuntime
                    .queryInterface(XPropertySet.class,
                            xCell);
            xPropSet.setPropertyValue("HoriJustify", CellHoriJustify.LEFT);


        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    private void insertTotalInformation(XSpreadsheet sheet, AnalysisReportPartition item, int row) {
        try {
            XCell xCell = sheet.getCellByPosition(3, row);
            xCell.setFormula("Всего :");

            xCell = sheet.getCellByPosition(4, row);
            xCell.setValue(item.getSumCost());

            xCell = sheet.getCellByPosition(5, row);
            xCell.setValue(item.getSumCostAndVat());

            xCell = sheet.getCellByPosition(6, row);
            xCell.setValue(item.getSumCostAndVatCurrency());

            xCell = sheet.getCellByPosition(7, row);
            xCell.setValue(item.getRevenue());

            xCell = sheet.getCellByPosition(8, row);
            xCell.setValue(item.getRevenueVat());


            xCell = sheet.getCellByPosition(9, row);
            xCell.setValue(item.getPrimeCostPlan());

            xCell = sheet.getCellByPosition(10, row);
            xCell.setValue(item.getPrimeCost());

            xCell = sheet.getCellByPosition(11, row);
            xCell.setValue(item.getProfit());

            setFontBoldForCellRange(sheet, 0, 12, row);

        } catch (Exception e) {

        }
    }

    private void insertFooter(XSpreadsheet sheet, DetailAnalysisDataSet item, int row) {
        try {
            XCell xCell = sheet.getCellByPosition(3, row);
            xCell.setFormula("Всего экспорт:");

            xCell = sheet.getCellByPosition(4, row);
            xCell.setValue(item.getTotalSumCost() - item.getCountrySumCost());

            xCell = sheet.getCellByPosition(5, row);
            xCell.setValue(item.getTotalSumCostAndVat() - item.getCountrySumCostAndVat());

            xCell = sheet.getCellByPosition(6, row);
            xCell.setValue(item.getTotalSumCostAndVatCurrency() - item.getCountrySumCostAndVatCurrency());

            xCell = sheet.getCellByPosition(7, row);
            xCell.setValue(item.getTotalRevenue() - item.getCountryRevenue());

            xCell = sheet.getCellByPosition(8, row);
            xCell.setValue(item.getTotalRevenueVat() - item.getCountryRevenueVat());

            xCell = sheet.getCellByPosition(9, row);
            xCell.setValue(item.getTotalPrimeCostPlan() - item.getCountryPrimeCostPlan());

            xCell = sheet.getCellByPosition(10, row);
            xCell.setValue(item.getTotalPrimeCost() - item.getCountryPrimeCost());

            xCell = sheet.getCellByPosition(11, row);
            xCell.setValue(item.getTotalProfit() - item.getCountryProfit());

            setFontBoldForCellRange(sheet, 0, 12, row);

            row++;

            xCell = sheet.getCellByPosition(3, row);
            xCell.setFormula("Итого:");

            xCell = sheet.getCellByPosition(4, row);
            xCell.setValue(item.getTotalSumCost());

            xCell = sheet.getCellByPosition(5, row);
            xCell.setValue(item.getTotalSumCostAndVat());

            xCell = sheet.getCellByPosition(6, row);
            xCell.setValue(item.getTotalSumCostAndVatCurrency());

            xCell = sheet.getCellByPosition(7, row);
            xCell.setValue(item.getTotalRevenue());

            xCell = sheet.getCellByPosition(8, row);
            xCell.setValue(item.getTotalRevenueVat());

            xCell = sheet.getCellByPosition(9, row);
            xCell.setValue(item.getTotalPrimeCostPlan());

            xCell = sheet.getCellByPosition(10, row);
            xCell.setValue(item.getTotalPrimeCost());

            xCell = sheet.getCellByPosition(11, row);
            xCell.setValue(item.getTotalProfit());

            setFontBoldForCellRange(sheet, 0, 12, row);

        } catch (Exception e) {

        }
    }

    private void insertData(XSpreadsheet sheet, AnalysisDetailsGroup item, int groupId, int row) {
        try {
            XCell xCell = sheet.getCellByPosition(1, row);
            xCell.setFormula(getGroupNameByGroupId(groupId));

            xCell = sheet.getCellByPosition(2, row);
            xCell.setValue(item.getAmountSet());

            xCell = sheet.getCellByPosition(3, row);
            xCell.setValue(item.getAmountItem());

            xCell = sheet.getCellByPosition(4, row);
            xCell.setValue(item.getSumCost());

            xCell = sheet.getCellByPosition(5, row);
            xCell.setValue(item.getSumCostAndVat());

            xCell = sheet.getCellByPosition(6, row);
            xCell.setValue(item.getSumCostAndVatCurrency());

            xCell = sheet.getCellByPosition(7, row);
            xCell.setValue(item.getRevenue());

            xCell = sheet.getCellByPosition(8, row);
            xCell.setValue(item.getRevenueVat());

            xCell = sheet.getCellByPosition(9, row);
            xCell.setValue(item.getPrimeCostPlan());

            xCell = sheet.getCellByPosition(10, row);
            xCell.setValue(item.getPrimeCost());

            xCell = sheet.getCellByPosition(11, row);
            xCell.setValue(item.getProfit());

            xCell = sheet.getCellByPosition(12, row);
            xCell.setValue(item.getProfitability());

        } catch (Exception e) {

        }
    }

    private String getGroupNameByGroupId(int groupId) {
        switch (groupId) {
            case 41:
                return "Бельевой трикотаж, тыс. шт.";
            case 42:
                return "Верхний трикотаж, тыс. шт.";
            case 43:
                return "Чулочно-носочные, тыс. пар";
            case 47:
                return "Полотно, т";
            default:
                return "неизвестно";
        }
    }

}
