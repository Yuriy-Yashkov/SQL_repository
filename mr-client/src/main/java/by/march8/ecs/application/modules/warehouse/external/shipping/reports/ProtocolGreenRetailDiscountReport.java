package by.march8.ecs.application.modules.warehouse.external.shipping.reports;

import by.gomel.freedev.ucframework.uccore.report.model.ReportProperties;
import by.march8.ecs.application.modules.warehouse.external.shipping.model.SaleDocumentReport;
import by.march8.entities.warehouse.SaleDocumentDetailItemReport;
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
 * @author Andy 17.06.2016.
 */
public class ProtocolGreenRetailDiscountReport extends AbstractInvoiceReport {

    public ProtocolGreenRetailDiscountReport(final SaleDocumentReport report) {
        super(report);
        documentReport = report;
        create();
    }

    @Override
    ReportProperties prepareProperties(final ReportProperties properties) {
        // Устанавливаем имя файла шаблона
        properties.setBlankName("protocol_" + saleDocumentBase.getDocumentNumber());
        properties.getTemplate().setTemplateName("протоколГринРозница.ots");
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

            xNamed.setName("ПСЦ к ТТН№" + saleDocumentBase.getDocumentNumber());


            XCell xCell = xSpreadsheet.getCellByPosition(6, 4);
            xCell.setFormula("'" + getDataAsString("CONTRACT_NUMBER"));


            xCell = xSpreadsheet.getCellByPosition(8, 4);
            xCell.setFormula(getDataAsString("CONTRACT_DATE_BEGIN") + "г.");

            xCell = xSpreadsheet.getCellByPosition(3, 6);
            xCell.setFormula("между " + getDataAsString("RECIPIENT_NAME") + " и " + getDataAsString("SENDER_NAME"));

            xCell = xSpreadsheet.getCellByPosition(3, 8);
            //xCell.setFormula("действует с " + getDataAsString("CONTRACT_DATE_BEGIN") + " по " + getDataAsString("CONTRACT_DATE_END") + "гг.");

            int row = 12;
            for (SaleDocumentDetailItemReport item : detailList) {

                xCell = xSpreadsheet.getCellByPosition(0, row);
                xCell.setFormula(String.valueOf(row - 11));

                xCell = xSpreadsheet.getCellByPosition(1, row);
                xCell.setFormula("");

                xCell = xSpreadsheet.getCellByPosition(2, row);
                xCell.setFormula("02.08.17 - 15.08.17 гг.");

                xCell = xSpreadsheet.getCellByPosition(3, row);
                xCell.setFormula(getDataAsString("PRODUCT_NAME"));

                xCell = xSpreadsheet.getCellByPosition(4, row);
                xCell.setFormula("ОАО \"8 Марта\"");

                xCell = xSpreadsheet.getCellByPosition(5, row);
                xCell.setFormula(item.getEanCode());

                xCell = xSpreadsheet.getCellByPosition(6, row);
                xCell.setFormula("РБ");

                xCell = xSpreadsheet.getCellByPosition(7, row);
                xCell.setFormula(item.getProductNameSizeString());

                xCell = xSpreadsheet.getCellByPosition(8, row);
                xCell.setValue(item.getAccountingPrice());

                // xCell.setFormula(saleDocumentBase.getDiscountValue() + "%");
                xCell = xSpreadsheet.getCellByPosition(9, row);
                xCell.setFormula("2%");
                xCell = xSpreadsheet.getCellByPosition(10, row);
                xCell.setValue(0.0);

/*                xCell = xSpreadsheet.getCellByPosition(6, row);
                xCell.setValue(item.getAmountPrint());

                xCell = xSpreadsheet.getCellByPosition(7, row);
                xCell.setFormula(item.getMeasureUnit());*/

                xCell = xSpreadsheet.getCellByPosition(11, row);
                xCell.setFormula("5%");
                xCell = xSpreadsheet.getCellByPosition(12, row);
                xCell.setFormula("-");
                xCell = xSpreadsheet.getCellByPosition(13, row);
                xCell.setValue(item.getValueSumCostAndVat());

                xCell = xSpreadsheet.getCellByPosition(14, row);
                xCell.setValue(item.getValueVAT());

                xCell = xSpreadsheet.getCellByPosition(15, row);
                xCell.setFormula("-");
                xCell = xSpreadsheet.getCellByPosition(16, row);
                xCell.setFormula("-");
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
                xCell.setFormula("-");
                xCell = xSpreadsheet.getCellByPosition(23, row);
                xCell.setFormula("-");

                row++;

                XCellRange xCellRange = xSpreadsheet
                        .getCellRangeByPosition(0, row - 1, 0,
                                row - 1);
                setRowHeight(xCellRange, 1600);
                rowFormat(xSpreadsheet, row - 1, 14, CHAR_SIZE);
            }

            xCell = xSpreadsheet.getCellByPosition(1, row + 2);
            xCell.setFormula("Покупатель: ");
            xPropSet = UnoRuntime
                    .queryInterface(com.sun.star.beans.XPropertySet.class,
                            xCell);
            xPropSet.setPropertyValue("HoriJustify", CellHoriJustify.LEFT);


            xCell = xSpreadsheet.getCellByPosition(1, row + 3);
            xCell.setFormula(getDataAsString("RECIPIENT_NAME"));
            xPropSet = UnoRuntime
                    .queryInterface(com.sun.star.beans.XPropertySet.class,
                            xCell);

            XCellRange xCellRange = xSpreadsheet
                    .getCellRangeByPosition(1, row + 5, 6, row + 5);

            com.sun.star.util.XMergeable xMerge = UnoRuntime
                    .queryInterface(com.sun.star.util.XMergeable.class,
                            xCellRange);
            xMerge.merge(true);
            xPropSet = UnoRuntime
                    .queryInterface(com.sun.star.beans.XPropertySet.class,
                            xCell);
            xPropSet.setPropertyValue("HoriJustify", CellHoriJustify.LEFT);

            xCell = xSpreadsheet.getCellByPosition(1, row + 5);
            xCell.setFormula("_________________________/______________/");

            xPropSet = UnoRuntime
                    .queryInterface(com.sun.star.beans.XPropertySet.class,
                            xCell);
            xPropSet.setPropertyValue("HoriJustify", CellHoriJustify.LEFT);

            xCell = xSpreadsheet.getCellByPosition(9, row + 2);
            xCell.setFormula("Поставщик: ");
            xPropSet = UnoRuntime
                    .queryInterface(com.sun.star.beans.XPropertySet.class,
                            xCell);
            xPropSet.setPropertyValue("HoriJustify", CellHoriJustify.LEFT);


            xCell = xSpreadsheet.getCellByPosition(9, row + 3);
            xCell.setFormula(getDataAsString("SENDER_NAME"));

            xCellRange = xSpreadsheet
                    .getCellRangeByPosition(9, row + 5, 13, row + 5);

            xMerge = UnoRuntime
                    .queryInterface(com.sun.star.util.XMergeable.class,
                            xCellRange);
            xMerge.merge(true);
            xPropSet = UnoRuntime
                    .queryInterface(com.sun.star.beans.XPropertySet.class,
                            xCell);
            xPropSet.setPropertyValue("HoriJustify", CellHoriJustify.LEFT);


            xCell = xSpreadsheet.getCellByPosition(9, row + 5);
            xCell.setFormula("_________________________/______________/");

            xPropSet = UnoRuntime
                    .queryInterface(com.sun.star.beans.XPropertySet.class,
                            xCell);
            xPropSet.setPropertyValue("HoriJustify", CellHoriJustify.LEFT);


        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }
}
