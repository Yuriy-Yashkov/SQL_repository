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
 * @author Andy 17.06.2016.
 */
public class ProtocolUnifudReport extends AbstractInvoiceReport {

    public ProtocolUnifudReport(final SaleDocumentReport report) {
        super(report);
        documentReport = report;
        create();
    }


    @Override
    ReportProperties prepareProperties(final ReportProperties properties) {
        // Устанавливаем имя файла шаблона
        properties.setBlankName("protocol_" + saleDocumentBase.getDocumentNumber());
        properties.getTemplate().setTemplateName("протоколЮнифуд.ots");
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

            int row = 5;
            XCell xCell;

            String COMPANY_NAME = "ОАО \"8 Марта\"";
            String REPUBLIC = "РБ";

            for (SaleDocumentDetailItemReport item : detailList) {

                xCell = xSpreadsheet.getCellByPosition(0, row);
                xCell.setFormula(String.valueOf(row - 4));

                xCell = xSpreadsheet.getCellByPosition(1, row);
                xCell.setFormula(item.getProductNameSizeString());

                xCell = xSpreadsheet.getCellByPosition(3, row);
                xCell.setFormula(item.getArticleNumber());


                xCell = xSpreadsheet.getCellByPosition(4, row);
                xCell.setFormula("производитель");

                xCell = xSpreadsheet.getCellByPosition(5, row);
                xCell.setFormula(REPUBLIC);

                xCell = xSpreadsheet.getCellByPosition(6, row);
                xCell.setFormula("8 Марта");

                xCell = xSpreadsheet.getCellByPosition(7, row);
                xCell.setFormula(item.getMeasureUnit());

                xCell = xSpreadsheet.getCellByPosition(8, row);
                xCell.setValue(item.getAmountPrint());

                xCell = xSpreadsheet.getCellByPosition(9, row);
                xCell.setFormula("средний");

                xCell = xSpreadsheet.getCellByPosition(10, row);
                xCell.setValue(item.getAccountingPrice());

                xCell = xSpreadsheet.getCellByPosition(11, row);
                xCell.setValue(saleDocumentBase.getDiscountValue());

                xCell = xSpreadsheet.getCellByPosition(12, row);
                xCell.setValue(item.getValuePrice());

                xCell = xSpreadsheet.getCellByPosition(13, row);
                xCell.setValue(item.getValueVat());


                xCell = xSpreadsheet.getCellByPosition(14, row);
                xCell.setValue(item.getValueSumCostAndVat());

                xCell = xSpreadsheet.getCellByPosition(15, row);
                xCell.setValue(item.getItemSize());

                xCell = xSpreadsheet.getCellByPosition(16, row);
                xCell.setValue(item.getItemGrowz());

                xCell = xSpreadsheet.getCellByPosition(17, row);
                xCell.setFormula("-");

                xCell = xSpreadsheet.getCellByPosition(18, row);
                xCell.setValue(item.getWeight());

                xCell = xSpreadsheet.getCellByPosition(19, row);
                xCell.setFormula("-");

                xCell = xSpreadsheet.getCellByPosition(20, row);
                xCell.setFormula("-");

                xCell = xSpreadsheet.getCellByPosition(21, row);
                xCell.setFormula(item.getEanCode());

                xCell = xSpreadsheet.getCellByPosition(22, row);
                xCell.setFormula(COMPANY_NAME);

                xCell = xSpreadsheet.getCellByPosition(23, row);
                xCell.setFormula("-");

                rowFormat(xSpreadsheet, row - 1, 24, CHAR_SIZE);
                row++;

                XCellRange xCellRange = xSpreadsheet
                        .getCellRangeByPosition(0, row - 1, 0,
                                row - 1);
                setRowHeight(xCellRange, 1220);

            }
            rowFormat(xSpreadsheet, row - 1, 24, CHAR_SIZE);

            xCell = xSpreadsheet.getCellByPosition(0, row + 2);
            xCell.setFormula("ПОКУПАТЕЛЬ ");

            XPropertySet xPropSet;

            xPropSet = UnoRuntime
                    .queryInterface(XPropertySet.class,
                            xCell);
            xPropSet.setPropertyValue("HoriJustify", CellHoriJustify.LEFT);


            xCell = xSpreadsheet.getCellByPosition(9, row + 2);
            xCell.setFormula("ПОСТАВЩИК ");
            xPropSet = UnoRuntime
                    .queryInterface(XPropertySet.class,
                            xCell);
            xPropSet.setPropertyValue("HoriJustify", CellHoriJustify.LEFT);

            xCell = xSpreadsheet.getCellByPosition(9, row + 3);
            xCell.setFormula(COMPANY_NAME);

            xCell = xSpreadsheet.getCellByPosition(9, row + 4);
            xCell.setFormula("246708, РБ, г.Гомель, ул.Советская, 41");

            xCell = xSpreadsheet.getCellByPosition(9, row + 5);
            xCell.setFormula("Р/сч: 3012310025015 в ЦБУ 301 \"Белинвестбанк\" ОАО код 153001739");

            xCell = xSpreadsheet.getCellByPosition(9, row + 6);
            xCell.setFormula("адрес: 246022, г.Гомель, ул. Советская, 48");

            xCell = xSpreadsheet.getCellByPosition(9, row + 7);
            xCell.setFormula("УНП: 400078265, ОКПО: 00311935");

            XCellRange xCellRange = xSpreadsheet
                    .getCellRangeByPosition(0, row + 2, 8, row + 20);
            xPropSet = UnoRuntime
                    .queryInterface(XPropertySet.class,
                            xCellRange);
            xPropSet.setPropertyValue("CharHeight", 6.5);

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
