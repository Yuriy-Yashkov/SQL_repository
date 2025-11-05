package by.march8.ecs.application.modules.warehouse.external.shipping.reports;

import by.gomel.freedev.ucframework.uccore.report.model.ReportProperties;
import by.march8.ecs.application.modules.warehouse.external.shipping.model.SaleDocumentReport;
import by.march8.entities.warehouse.SaleDocumentDetailItemReport;
import com.sun.star.beans.XPropertySet;
import com.sun.star.container.XNamed;
import com.sun.star.lang.XComponent;
import com.sun.star.sheet.XSpreadsheet;
import com.sun.star.sheet.XSpreadsheetDocument;
import com.sun.star.sheet.XSpreadsheets;
import com.sun.star.table.CellHoriJustify;
import com.sun.star.table.XCell;
import com.sun.star.table.XCellRange;
import com.sun.star.uno.UnoRuntime;


/**
 * @author Andy 16.06.2016.
 */
public class ProtocolStandardReport extends AbstractInvoiceReport {

    public ProtocolStandardReport(final SaleDocumentReport report) {
        super(report);
        documentReport = report;
        create();
    }

    @Override
    ReportProperties prepareProperties(final ReportProperties properties) {
        // Устанавливаем имя файла шаблона
        properties.setBlankName("protocol_" + saleDocumentBase.getDocumentNumber());
        properties.getTemplate().setTemplateName("протоколСтандартный.ots");
        return properties;
    }

    @Override
    boolean populateData(final XComponent component) {

        try {
            XPropertySet xPropSet;
            XSpreadsheetDocument xSpreadsheetDocument = UnoRuntime
                    .queryInterface(XSpreadsheetDocument.class, component);
            XSpreadsheets xSpreadsheets = xSpreadsheetDocument.getSheets();
            Object sheet = xSpreadsheets.getByName("Лист1");
            XSpreadsheet xSpreadsheet = UnoRuntime
                    .queryInterface(XSpreadsheet.class, sheet);

            XNamed xNamed = UnoRuntime.queryInterface(XNamed.class,
                    xSpreadsheet);

            xNamed.setName("ПСЦ к ТТН №" + saleDocumentBase.getDocumentNumber());


            String COMPANY_NAME = "ОАО \"8 Марта\"";

            XCell xCell = xSpreadsheet.getCellByPosition(0, 3);
            xCell.setFormula("между " + getDataAsString("RECIPIENT_NAME") + " и " + getDataAsString("SENDER_NAME"));

            // ----------------Заполнение тела документа
            int row = 11;

            for (SaleDocumentDetailItemReport item : detailList) {

                xCell = xSpreadsheet.getCellByPosition(0, row);
                xCell.setFormula(String.valueOf(row - 10));

                xCell = xSpreadsheet.getCellByPosition(1, row);
                xCell.setFormula(item.getProductNameString());

                xCell = xSpreadsheet.getCellByPosition(2, row);
                xCell.setFormula("'" + item.getItemSizePrint());

                xCell = xSpreadsheet.getCellByPosition(3, row);
                xCell.setValue(item.getItemGrade());

                xCell = xSpreadsheet.getCellByPosition(4, row);
                xCell.setValue(item.getAccountingPrice());

                xCell = xSpreadsheet.getCellByPosition(5, row);
                xCell.setValue(saleDocumentBase.getDiscountValue());


                xCell = xSpreadsheet.getCellByPosition(6, row);
                xCell.setValue(item.getValuePrice());

                xCell = xSpreadsheet.getCellByPosition(7, row);
                xCell.setValue(item.getValueVAT());

                xCell = xSpreadsheet.getCellByPosition(8, row);
                xCell.setValue(item.getValueSumCostAndVat());

                rowFormat(xSpreadsheet, row - 1, 9, CHAR_SIZE);
                row++;
            }

            rowFormat(xSpreadsheet, row - 1, 9, CHAR_SIZE);

            xCell = xSpreadsheet.getCellByPosition(0, row + 2);
            xCell.setFormula("Покупатель: ");
            xPropSet = UnoRuntime
                    .queryInterface(com.sun.star.beans.XPropertySet.class,
                            xCell);
            xPropSet.setPropertyValue("HoriJustify", CellHoriJustify.LEFT);


            xCell = xSpreadsheet.getCellByPosition(0, row + 3);
            xCell.setFormula(getDataAsString("RECIPIENT_NAME"));

            XCellRange xCellRange = xSpreadsheet
                    .getCellRangeByPosition(0, row + 5, 4, row + 5);

            com.sun.star.util.XMergeable xMerge = UnoRuntime
                    .queryInterface(com.sun.star.util.XMergeable.class,
                            xCellRange);
            xMerge.merge(true);
            xPropSet = UnoRuntime
                    .queryInterface(com.sun.star.beans.XPropertySet.class,
                            xCell);
            xPropSet.setPropertyValue("HoriJustify", CellHoriJustify.LEFT);

            xCell = xSpreadsheet.getCellByPosition(0, row + 5);
            xCell.setFormula("_________________________/______________/");

            xPropSet = UnoRuntime
                    .queryInterface(com.sun.star.beans.XPropertySet.class,
                            xCell);
            xPropSet.setPropertyValue("HoriJustify", CellHoriJustify.LEFT);

            xCell = xSpreadsheet.getCellByPosition(5, row + 2);
            xCell.setFormula("Поставщик: ");
            xPropSet = UnoRuntime
                    .queryInterface(com.sun.star.beans.XPropertySet.class,
                            xCell);
            xPropSet.setPropertyValue("HoriJustify", CellHoriJustify.LEFT);


            xCell = xSpreadsheet.getCellByPosition(5, row + 3);
            xCell.setFormula(COMPANY_NAME);

            xCellRange = xSpreadsheet
                    .getCellRangeByPosition(5, row + 5, 8, row + 5);

            xMerge = UnoRuntime
                    .queryInterface(com.sun.star.util.XMergeable.class,
                            xCellRange);
            xMerge.merge(true);
            xPropSet = UnoRuntime
                    .queryInterface(com.sun.star.beans.XPropertySet.class,
                            xCell);
            xPropSet.setPropertyValue("HoriJustify", CellHoriJustify.LEFT);


            xCell = xSpreadsheet.getCellByPosition(5, row + 5);
            xCell.setFormula("_________________________/______________/");

            xPropSet = UnoRuntime
                    .queryInterface(com.sun.star.beans.XPropertySet.class,
                            xCell);
            xPropSet.setPropertyValue("HoriJustify", CellHoriJustify.LEFT);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }


}
