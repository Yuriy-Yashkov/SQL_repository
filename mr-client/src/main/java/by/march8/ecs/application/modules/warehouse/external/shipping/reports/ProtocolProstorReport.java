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
public class ProtocolProstorReport extends AbstractInvoiceReport {

    public ProtocolProstorReport(final SaleDocumentReport report) {
        super(report);
        documentReport = report;
        create();
    }

    @Override
    ReportProperties prepareProperties(final ReportProperties properties) {
        // Устанавливаем имя файла шаблона
        properties.setBlankName("protocol_" + saleDocumentBase.getDocumentNumber());
        properties.getTemplate().setTemplateName("протоколПростор.ots");
        return properties;
    }

    @Override
    boolean populateData(final XComponent component) {
        try {
            XSpreadsheetDocument xSpreadsheetDocument = UnoRuntime
                    .queryInterface(XSpreadsheetDocument.class, component);
            XSpreadsheets xSpreadsheets = xSpreadsheetDocument.getSheets();
            Object sheet = xSpreadsheets.getByName("Sheet1");
            XSpreadsheet xSpreadsheet = UnoRuntime
                    .queryInterface(XSpreadsheet.class, sheet);

            XNamed xNamed = UnoRuntime.queryInterface(XNamed.class,
                    xSpreadsheet);

            //xNamed.setName("ШАБЛОН " + saleDocumentBase.getDocumentNumber());

            int row = 6;

            XCell xCell;

            for (SaleDocumentDetailItemReport item : detailList) {


                xCell = xSpreadsheet.getCellByPosition(6, row);
                xCell.setFormula(item.getEanCode());

                xCell = xSpreadsheet.getCellByPosition(7, row);
                xCell.setFormula("-");

                xCell = xSpreadsheet.getCellByPosition(8, row);
                xCell.setFormula(item.getProductNameSizeString());

                xCell = xSpreadsheet.getCellByPosition(9, row);
                xCell.setFormula("штучный");

                xCell = xSpreadsheet.getCellByPosition(10, row);
                xCell.setValue(item.getWeight());

                xCell = xSpreadsheet.getCellByPosition(11, row);
                xCell.setFormula("ОАО \"8 Марта\"");

                xCell = xSpreadsheet.getCellByPosition(12, row);
                xCell.setFormula("ОАО \"8 Марта\"");

                xCell = xSpreadsheet.getCellByPosition(13, row);
                xCell.setFormula("РБ");

                xCell = xSpreadsheet.getCellByPosition(14, row);
                xCell.setFormula("ОАО \"8 Марта\"");

                xCell = xSpreadsheet.getCellByPosition(15, row);
                xCell.setFormula("-");

                xCell = xSpreadsheet.getCellByPosition(16, row);
                xCell.setValue(item.getAmountPrint());

                xCell = xSpreadsheet.getCellByPosition(17, row);
                xCell.setFormula("-");
                xCell = xSpreadsheet.getCellByPosition(18, row);
                xCell.setFormula("-");
                xCell = xSpreadsheet.getCellByPosition(19, row);
                xCell.setFormula("-");
                xCell = xSpreadsheet.getCellByPosition(20, row);
                xCell.setFormula("-");
                xCell = xSpreadsheet.getCellByPosition(21, row);
                xCell.setFormula("-");

                xCell = xSpreadsheet.getCellByPosition(22, row);
                xCell.setValue(item.getValueVAT());

                xCell = xSpreadsheet.getCellByPosition(23, row);
                xCell.setValue(item.getAccountingPrice());

                xCell = xSpreadsheet.getCellByPosition(24, row);
                xCell.setValue(saleDocumentBase.getDiscountValue());

                xCell = xSpreadsheet.getCellByPosition(25, row);
                xCell.setValue(item.getValuePrice());

                xCell = xSpreadsheet.getCellByPosition(26, row);
                xCell.setFormula("-");

                xCell = xSpreadsheet.getCellByPosition(28, row);
                xCell.setFormula("-");

                xCell = xSpreadsheet.getCellByPosition(29, row);
                xCell.setFormula("-");


                xCell = xSpreadsheet.getCellByPosition(5, row);
                xCell.setFormula(item.getTnvedCode());


                xCell = xSpreadsheet.getCellByPosition(7, row);
                xCell.setFormula(item.getMeasureUnit());


                xCell = xSpreadsheet.getCellByPosition(11, row);
                xCell.setValue(item.getValueVAT());

                xCell = xSpreadsheet.getCellByPosition(12, row);
                xCell.setValue(item.getValueSumCostAndVat());

                xCell = xSpreadsheet.getCellByPosition(13, row);
                xCell.setFormula("-");

                row++;

                rowFormat(xSpreadsheet, row - 1, 23, CHAR_SIZE);
            }

            sheet = xSpreadsheets.getByName("Sheet2");
            xSpreadsheet = UnoRuntime
                    .queryInterface(XSpreadsheet.class, sheet);

            row = 6;
            for (SaleDocumentDetailItemReport item : detailList) {

                xCell = xSpreadsheet.getCellByPosition(6, row);
                xCell.setFormula(item.getEanCode());


                xCell = xSpreadsheet.getCellByPosition(7, row);
                xCell.setFormula(item.getProductNameSizeString());

                xCell = xSpreadsheet.getCellByPosition(8, row);
                xCell.setFormula("штучный");

                xCell = xSpreadsheet.getCellByPosition(9, row);
                xCell.setValue(item.getWeight());

                xCell = xSpreadsheet.getCellByPosition(10, row);
                xCell.setFormula("ОАО \"8 Марта\"");

                xCell = xSpreadsheet.getCellByPosition(11, row);
                xCell.setFormula("ОАО \"8 Марта\"");

                xCell = xSpreadsheet.getCellByPosition(12, row);
                xCell.setFormula("РБ");

                xCell = xSpreadsheet.getCellByPosition(13, row);
                xCell.setFormula("ОАО \"8 Марта\"");

                xCell = xSpreadsheet.getCellByPosition(14, row);
                xCell.setFormula("-");

                xCell = xSpreadsheet.getCellByPosition(15, row);
                xCell.setFormula("-");

                xCell = xSpreadsheet.getCellByPosition(16, row);
                xCell.setValue(item.getValueVAT());

                xCell = xSpreadsheet.getCellByPosition(17, row);
                xCell.setValue(item.getAccountingPrice());

                xCell = xSpreadsheet.getCellByPosition(18, row);
                xCell.setValue(saleDocumentBase.getDiscountValue());

                xCell = xSpreadsheet.getCellByPosition(19, row);
                xCell.setValue(item.getValuePrice());

                xCell = xSpreadsheet.getCellByPosition(20, row);
                xCell.setValue(0);

                xCell = xSpreadsheet.getCellByPosition(22, row);
                xCell.setFormula("-");

                xCell = xSpreadsheet.getCellByPosition(23, row);
                xCell.setFormula("нет");

                row++;

                rowFormat(xSpreadsheet, row - 1, 23, CHAR_SIZE);
            }


        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
