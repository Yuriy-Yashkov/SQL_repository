package by.march8.ecs.application.modules.marketing.reports;

import by.gomel.freedev.ucframework.uccore.report.model.ReportProperties;
import by.march8.ecs.application.modules.marketing.model.MarketingReportData;
import by.march8.entities.marketing.ViewMarketingPriceListDetailItem;
import com.sun.star.container.XNamed;
import com.sun.star.lang.XComponent;
import com.sun.star.sheet.XSpreadsheet;
import com.sun.star.sheet.XSpreadsheetDocument;
import com.sun.star.sheet.XSpreadsheets;
import com.sun.star.table.XCell;
import com.sun.star.table.XCellRange;
import com.sun.star.uno.UnoRuntime;

/**
 * @author Andy 24.01.2017.
 */
public class MarketingPriceListMarkdownReport extends AbstractMarketingReport {
    protected String month;
    private boolean setImage;

    public MarketingPriceListMarkdownReport(final MarketingReportData report, boolean setImage) {
        super(report);
        this.setImage = setImage;
        create();
    }

    @Override
    ReportProperties prepareProperties(final ReportProperties properties) {
        // Устанавливаем имя файла шаблона
        properties.setBlankName("marketing_md_" + priceList.getDocumentNumber().replace("/", "-"));
        if (setImage) {
            properties.getTemplate().setTemplateName("marketingPriceListAdditionMarkdownImage.ots");
        } else {
            properties.getTemplate().setTemplateName("marketingPriceListAdditionMarkdown.ots");
        }
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

            xNamed.setName("Отчет");
            XCell xCell;
            int row = 4;
            for (Object o : detailList) {

                ViewMarketingPriceListDetailItem item = (ViewMarketingPriceListDetailItem) o;
                if (item != null) {
                    xCell = xSpreadsheet.getCellByPosition(0, row);
                    xCell.setFormula(String.valueOf(row - 3));

                    xCell = xSpreadsheet.getCellByPosition(1, row);
                    xCell.setFormula(item.getItemName());

                    xCell = xSpreadsheet.getCellByPosition(2, row);
                    xCell.setFormula(item.getModelNumber());

                    xCell = xSpreadsheet.getCellByPosition(3, row);
                    xCell.setFormula(item.getArticleNumber());

                    xCell = xSpreadsheet.getCellByPosition(4, row);
                    xCell.setFormula(item.getSizeRange());

                    xCell = xSpreadsheet.getCellByPosition(5, row);
                    xCell.setValue(item.getPrimeCostValue());

                    xCell = xSpreadsheet.getCellByPosition(6, row);
                    xCell.setValue(item.getProfitabilityValue());

                    xCell = xSpreadsheet.getCellByPosition(7, row);
                    xCell.setValue(item.getEffectivePriceValue());

                    xCell = xSpreadsheet.getCellByPosition(8, row);
                    xCell.setValue(item.getSuggestedPriceValue());

                    if (setImage) {
                        XCellRange xCellRange = xSpreadsheet
                                .getCellRangeByPosition(9, row, 9,
                                        row);

                        xCell = xSpreadsheet.getCellByPosition(9, row);
                        int height = insertImageToDoc(xSpreadsheetDocument, xSpreadsheet,
                                xCell, item.getModelNumber(), row);

                        setRowHeight(xCellRange, height);
                    }
                }

                if (setImage) {
                    rowFormatCenterLeft(xSpreadsheet, row, 10, 10.0f);
                } else {
                    rowFormat(xSpreadsheet, row, 9, 10.0f);
                }
                row++;
            }

            row++;

            xCell = xSpreadsheet.getCellByPosition(2, row);
            //xCell.setFormula("Сдал_________________________________ ");
            setCharHeightForCell(xCell, 9.0f);

            xCell = xSpreadsheet.getCellByPosition(7, row);

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
