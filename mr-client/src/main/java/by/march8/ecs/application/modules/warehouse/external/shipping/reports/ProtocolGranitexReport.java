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

import java.math.BigDecimal;

/**
 * @author Andy 17.06.2016.
 */
public class ProtocolGranitexReport extends AbstractInvoiceReport {
    public ProtocolGranitexReport(final SaleDocumentReport report) {
        super(report);
        documentReport = report;
        create();
    }

    @Override
    ReportProperties prepareProperties(final ReportProperties properties) {
        // Устанавливаем имя файла шаблона
        properties.setBlankName("protocol_" + saleDocumentBase.getDocumentNumber());
        properties.getTemplate().setTemplateName("протоколБелпочта.ots");
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
            String COMPANY_NAME = "ОАО \"8 Марта\"";
            String REPUBLIC = "РБ";


            XCell xCell = xSpreadsheet.getCellByPosition(7, 1);
            xCell.setFormula("№ " + getDataAsString("CONTRACT_NUMBER") + " от " + getDataAsString("CONTRACT_DATE_BEGIN") + "г.");

            xCell = xSpreadsheet.getCellByPosition(3, 3);
            xCell.setFormula(getDataAsString("RECIPIENT_NAME"));

            xCell = xSpreadsheet.getCellByPosition(3, 4);
            xCell.setFormula(getDataAsString("SENDER_NAME"));

            xCell = xSpreadsheet.getCellByPosition(5, 5);
            xCell.setFormula(getDataAsString("CONTRACT_DATE_BEGIN") + "г.");

            int row = 11;

            for (SaleDocumentDetailItemReport item : detailList) {

                xCell = xSpreadsheet.getCellByPosition(0, row);
                xCell.setFormula(String.valueOf(row - 10));

                xCell = xSpreadsheet.getCellByPosition(1, row);
                xCell.setFormula(item.getEanCode());

                xCell = xSpreadsheet.getCellByPosition(2, row);
                xCell.setFormula(item.getProductNameSizeString());

                xCell = xSpreadsheet.getCellByPosition(3, row);
                xCell.setFormula(item.getMeasureUnit());

                xCell = xSpreadsheet.getCellByPosition(4, row);
                xCell.setValue(item.getAccountingPrice() * 10000);

                xCell = xSpreadsheet.getCellByPosition(5, row);
                xCell.setValue(item.getAccountingPrice());

                xCell = xSpreadsheet.getCellByPosition(6, row);
                xCell.setValue(saleDocumentBase.getDiscountValue());

                xCell = xSpreadsheet.getCellByPosition(7, row);
                xCell.setValue(item.getValuePrice());

                xCell = xSpreadsheet.getCellByPosition(8, row);
                xCell.setValue(item.getValueVAT());

                xCell = xSpreadsheet.getCellByPosition(9, row);
                xCell.setValue(new BigDecimal(item.getValuePrice() +
                        new BigDecimal(item.getValuePrice() * item.getValueVAT() / 100).setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue()).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());

                xCell = xSpreadsheet.getCellByPosition(10, row);
                xCell.setFormula(REPUBLIC);

                xCell = xSpreadsheet.getCellByPosition(11, row);
                xCell.setFormula(COMPANY_NAME);

                row++;

                XCellRange xCellRange = xSpreadsheet
                        .getCellRangeByPosition(0, row - 1, 0,
                                row - 1);
                setRowHeight(xCellRange, 1600);
                rowFormat(xSpreadsheet, row - 1, 12, CHAR_SIZE);
            }

            XPropertySet xPropSet;

            xCell = xSpreadsheet.getCellByPosition(0, row + 3);
            xCell.setFormula(COMPANY_NAME);
            xPropSet = UnoRuntime
                    .queryInterface(XPropertySet.class,
                            xCell);
            xPropSet.setPropertyValue("HoriJustify", CellHoriJustify.LEFT);

            xCell = xSpreadsheet.getCellByPosition(6, row + 3);
            xCell.setFormula(getDataAsString("RECIPIENT_NAME") + ":_____________/_____________");
            xPropSet = UnoRuntime
                    .queryInterface(XPropertySet.class,
                            xCell);
            xPropSet.setPropertyValue("HoriJustify", CellHoriJustify.LEFT);

            row++;
            xCell = xSpreadsheet.getCellByPosition(0, row + 4);
            xCell.setFormula("Нач. отдела сбыта _____________/ Е.А. Кретова");
            xPropSet = UnoRuntime
                    .queryInterface(XPropertySet.class,
                            xCell);
            xPropSet.setPropertyValue("HoriJustify", CellHoriJustify.LEFT);

            row++;
            xCell = xSpreadsheet.getCellByPosition(0, row + 10);
            xCell.setFormula("Нач. ПЭО              _____________/ Н.В. Кучева");
            xPropSet = UnoRuntime
                    .queryInterface(XPropertySet.class,
                            xCell);
            xPropSet.setPropertyValue("HoriJustify", CellHoriJustify.LEFT);

            row++;

            xCell = xSpreadsheet.getCellByPosition(1, row + 15);
            xCell.setFormula("М.П.");
            xPropSet = UnoRuntime
                    .queryInterface(XPropertySet.class,
                            xCell);
            xPropSet.setPropertyValue("HoriJustify", CellHoriJustify.LEFT);

            xCell = xSpreadsheet.getCellByPosition(7, row + 15);
            xCell.setFormula("М.П.");
            xPropSet = UnoRuntime
                    .queryInterface(XPropertySet.class,
                            xCell);
            xPropSet.setPropertyValue("HoriJustify", CellHoriJustify.LEFT);


            xCell = xSpreadsheet.getCellByPosition(1, row + 17);
            xCell.setFormula("________");
            xPropSet = UnoRuntime
                    .queryInterface(XPropertySet.class,
                            xCell);
            xPropSet.setPropertyValue("HoriJustify", CellHoriJustify.CENTER);

            xCell = xSpreadsheet.getCellByPosition(7, row + 17);
            xCell.setFormula("________");
            xPropSet = UnoRuntime
                    .queryInterface(XPropertySet.class,
                            xCell);
            xPropSet.setPropertyValue("HoriJustify", CellHoriJustify.CENTER);

            xCell = xSpreadsheet.getCellByPosition(1, row + 18);
            xCell.setFormula("(дата)");
            xPropSet = UnoRuntime
                    .queryInterface(XPropertySet.class,
                            xCell);
            xPropSet.setPropertyValue("HoriJustify", CellHoriJustify.CENTER);

            xCell = xSpreadsheet.getCellByPosition(7, row + 18);
            xCell.setFormula("(дата)");
            xPropSet = UnoRuntime
                    .queryInterface(XPropertySet.class,
                            xCell);
            xPropSet.setPropertyValue("HoriJustify", CellHoriJustify.CENTER);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
