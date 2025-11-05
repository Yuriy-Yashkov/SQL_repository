package by.march8.ecs.application.modules.warehouse.external.shipping.reports;

import by.gomel.freedev.ucframework.uccore.report.model.ReportProperties;
import by.march8.ecs.application.modules.warehouse.external.shipping.model.SaleDocumentReport;
import by.march8.entities.warehouse.SaleDocumentDetailItemReport;
import com.sun.star.container.XNamed;
import com.sun.star.lang.XComponent;
import com.sun.star.sheet.XSpreadsheet;
import com.sun.star.sheet.XSpreadsheetDocument;
import com.sun.star.sheet.XSpreadsheets;
import com.sun.star.table.XCell;
import com.sun.star.uno.UnoRuntime;

/**
 * @author Andy 17.06.2016.
 */
public class ProtocolHyberMallReport extends AbstractInvoiceReport {

    public ProtocolHyberMallReport(final SaleDocumentReport report) {
        super(report);
        documentReport = report;
        create();
    }

    @Override
    ReportProperties prepareProperties(final ReportProperties properties) {
        // Устанавливаем имя файла шаблона
        properties.setBlankName("protocol_" + saleDocumentBase.getDocumentNumber());
        properties.getTemplate().setTemplateName("протоколГиппермолл.ots");
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

            xNamed.setName("ПСЦ к ТТН№ " + saleDocumentBase.getDocumentNumber());

            XCell xCell;


            int row = 6;
            com.sun.star.table.XColumnRowRange xCRRange = UnoRuntime.queryInterface(com.sun.star.table.XColumnRowRange.class, xSpreadsheet);
            com.sun.star.table.XTableRows xRows = xCRRange.getRows();

            for (SaleDocumentDetailItemReport item : detailList) {


                xRows.insertByIndex(row + 1, 1);

                xCell = xSpreadsheet.getCellByPosition(0, row);
                xCell.setFormula(String.valueOf(row - 5));

                xCell = xSpreadsheet.getCellByPosition(1, row);
                xCell.setFormula(item.getEanCode());

                xCell = xSpreadsheet.getCellByPosition(2, row);
                xCell.setFormula(item.getArticleNumber());

                xCell = xSpreadsheet.getCellByPosition(3, row);
                xCell.setFormula(item.getProductNameSizeStringSingleLine());

                xCell = xSpreadsheet.getCellByPosition(4, row);
                xCell.setFormula(item.getTnvedCode());

                xCell = xSpreadsheet.getCellByPosition(5, row);
                xCell.setValue(item.getAmountPrint());

                xCell = xSpreadsheet.getCellByPosition(6, row);
                xCell.setValue(item.getValuePrice());

                xCell = xSpreadsheet.getCellByPosition(7, row);
                xCell.setValue(item.getValueVat());

                xCell = xSpreadsheet.getCellByPosition(8, row);
                xCell.setValue(item.getValueSumCostAndVat());

                xCell = xSpreadsheet.getCellByPosition(9, row);
                xCell.setFormula("-");
                xCell = xSpreadsheet.getCellByPosition(10, row);
                xCell.setFormula("-");

                rowFormat(xSpreadsheet, row - 1, 11, 10);
                row++;
            }

            rowFormat(xSpreadsheet, row - 1, 11, 10);

            xRows.removeByIndex(row, 1);


        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }
}
