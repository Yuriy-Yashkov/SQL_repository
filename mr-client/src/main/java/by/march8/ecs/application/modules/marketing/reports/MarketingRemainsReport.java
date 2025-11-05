package by.march8.ecs.application.modules.marketing.reports;

import by.gomel.freedev.ucframework.uccore.report.model.ReportProperties;
import by.march8.api.utils.DateUtils;
import by.march8.ecs.application.modules.marketing.model.MarketingRemainsReportData;
import by.march8.entities.warehouse.WarehouseRemainsItem;
import com.sun.star.container.XNamed;
import com.sun.star.lang.XComponent;
import com.sun.star.sheet.XSpreadsheet;
import com.sun.star.sheet.XSpreadsheetDocument;
import com.sun.star.sheet.XSpreadsheets;
import com.sun.star.table.XCell;
import com.sun.star.table.XCellRange;
import com.sun.star.uno.UnoRuntime;

import java.util.Date;

/**
 * @author Andy 15.05.2018 - 11:09.
 */
public class MarketingRemainsReport extends AbstractMarketingRemainsReport {
    public MarketingRemainsReport(final MarketingRemainsReportData report) {
        super(report);
        create();
    }

    @Override
    ReportProperties prepareProperties(final ReportProperties properties) {
        // Устанавливаем имя файла шаблона
        properties.setBlankName("marketing_remains_" + DateUtils.getNormalDateTimeFormatPlus(new Date()));
        properties.getTemplate().setTemplateName("marketingRemains.ots");
        return properties;
    }

    @Override
    boolean populateData(final XComponent component) {
        try {
            XSpreadsheetDocument xSpreadsheetDocument = UnoRuntime
                    .queryInterface(XSpreadsheetDocument.class, component);
            XSpreadsheets xSpreadsheets = xSpreadsheetDocument.getSheets();
            Object sheet = xSpreadsheets.getByName("Лист1");
            XSpreadsheet xSpreadsheet = UnoRuntime
                    .queryInterface(XSpreadsheet.class, sheet);

            XNamed xNamed = UnoRuntime.queryInterface(XNamed.class,
                    xSpreadsheet);

            xNamed.setName("Отбор по остаткам");
            XCell xCell;
            int row = 7;
            for (WarehouseRemainsItem item : detailList) {

                if (item != null) {
                    String[] x = item.getSizePrint().split("<br>");
                    int itemHeight = x.length * 440;


                    xCell = xSpreadsheet.getCellByPosition(0, row);
                    xCell.setFormula(String.valueOf(row - 3));

                    xCell = xSpreadsheet.getCellByPosition(1, row);
                    xCell.setFormula(item.getItemNamePrint());

                    xCell = xSpreadsheet.getCellByPosition(2, row);
                    xCell.setFormula(item.getType());

                    xCell = xSpreadsheet.getCellByPosition(3, row);
                    xCell.setValue(item.getModelNumber());

                    xCell = xSpreadsheet.getCellByPosition(4, row);
                    xCell.setFormula(item.getArticleNumber());

                    xCell = xSpreadsheet.getCellByPosition(5, row);
                    xCell.setValue(item.getGrade());

                    xCell = xSpreadsheet.getCellByPosition(6, row);
                    xCell.setFormula(replacer(item.getSizePrint()).replace("<br>", "\n"));

                    xCell = xSpreadsheet.getCellByPosition(7, row);
                    xCell.setFormula(replacer(item.getGrowthPrint()).replace("<br>", "\n"));

                    xCell = xSpreadsheet.getCellByPosition(8, row);
                    xCell.setValue(item.getAmountByModel());

                    xCell = xSpreadsheet.getCellByPosition(9, row);
                    xCell.setValue(item.getAmountByArticle());

                    xCell = xSpreadsheet.getCellByPosition(10, row);
                    xCell.setFormula(replacer(item.getAmountPrint()).replace("<br>", "\n"));

                    xCell = xSpreadsheet.getCellByPosition(11, row);
                    xCell.setFormula(replacer(item.getCostPrint()).replace("<br>", "\n"));

                    XCellRange xCellRange = xSpreadsheet
                            .getCellRangeByPosition(12, row, 12,
                                    row);

                    xCell = xSpreadsheet.getCellByPosition(12, row);
                    insertImageToDocAndResize(xSpreadsheetDocument, xSpreadsheet,
                            xCell, String.valueOf(item.getModelNumber()), xCellRange, itemHeight);


                }
                rowFormatCenterLeft(xSpreadsheet, row, 13, 10.0f);
                row++;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private String replacer(String inString) {
        return inString.replace("<html>", "").replace("</html>", "").replace("<font color=\"red\">", "")
                .replace("</font>", "");
    }
}
