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
public class ProtocolTabakInvestReport extends AbstractInvoiceReport {

    public ProtocolTabakInvestReport(final SaleDocumentReport report) {
        super(report);
        documentReport = report;
        create();
    }

    @Override
    ReportProperties prepareProperties(final ReportProperties properties) {
        // Устанавливаем имя файла шаблона
        properties.setBlankName("protocol_" + saleDocumentBase.getDocumentNumber());
        properties.getTemplate().setTemplateName("протоколТабакИнвест.ots");
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

            XCell xCell = xSpreadsheet.getCellByPosition(10, 3);
            xCell.setFormula(getDataAsString("CONTRACT_NUMBER"));

            xCell = xSpreadsheet.getCellByPosition(12, 3);
            xCell.setFormula(getDataAsString("CONTRACT_DATE_BEGIN"));

            xCell = xSpreadsheet.getCellByPosition(3, 9);
            xCell.setFormula("между " + getDataAsString("RECIPIENT_NAME") + " и " + getDataAsString("SENDER_NAME"));

            xCell = xSpreadsheet.getCellByPosition(3, 11);
            xCell.setFormula("срок действия с " + getDataAsString("CONTRACT_DATE_BEGIN") + " по " + getDataAsString("CONTRACT_DATE_END") + "гг.");

            if (saleDocumentBase.isOldMoney()) {
                xCell = xSpreadsheet.getCellByPosition(13, 13);
                xCell.setFormula("Цена без НДС после\nденоминации");
            }

            int row = 15;

            for (SaleDocumentDetailItemReport item : detailList) {

                xCell = xSpreadsheet.getCellByPosition(1, row);
                xCell.setFormula(String.valueOf(row - 14));

                xCell = xSpreadsheet.getCellByPosition(2, row);
                xCell.setFormula(item.getEanCode());

                xCell = xSpreadsheet.getCellByPosition(3, row);
                xCell.setFormula(item.getProductNameSizeString());

                xCell = xSpreadsheet.getCellByPosition(4, row);
                xCell.setFormula(REPUBLIC);

                xCell = xSpreadsheet.getCellByPosition(7, row);
                xCell.setFormula(item.getMeasureUnit());

                xCell = xSpreadsheet.getCellByPosition(5, row);
                xCell.setFormula(item.getTnvedCode());

                xCell = xSpreadsheet.getCellByPosition(6, row);
                xCell.setValue(item.getAmountPrint());

                xCell = xSpreadsheet.getCellByPosition(8, row);
                xCell.setValue(item.getAccountingPrice());

                xCell = xSpreadsheet.getCellByPosition(9, row);
                xCell.setValue(saleDocumentBase.getDiscountValue());

                xCell = xSpreadsheet.getCellByPosition(10, row);
                xCell.setValue(item.getValuePrice());

                xCell = xSpreadsheet.getCellByPosition(11, row);
                xCell.setValue(item.getValueVAT());

                xCell = xSpreadsheet.getCellByPosition(12, row);
                xCell.setValue(item.getValueSumCostAndVat());

                xCell = xSpreadsheet.getCellByPosition(13, row);
                xCell.setValue(item.getValueAccountingPriceAlt());

                row++;

                XCellRange xCellRange = xSpreadsheet
                        .getCellRangeByPosition(0, row - 1, 0,
                                row - 1);
                setRowHeight(xCellRange, 1300);
                rowFormat(xSpreadsheet, row - 1, 14, CHAR_SIZE);
            }

            XPropertySet xPropSet;

            xCell = xSpreadsheet.getCellByPosition(9, row + 2);
            xCell.setFormula("Подписи сторон: ");
            xPropSet = UnoRuntime
                    .queryInterface(XPropertySet.class,
                            xCell);
            xPropSet.setPropertyValue("HoriJustify", CellHoriJustify.LEFT);


            xCell = xSpreadsheet.getCellByPosition(1, row + 4);
            xCell.setFormula("Покупатель: ");
            xPropSet = UnoRuntime
                    .queryInterface(XPropertySet.class,
                            xCell);
            xPropSet.setPropertyValue("HoriJustify", CellHoriJustify.LEFT);


            xCell = xSpreadsheet.getCellByPosition(1, row + 5);
            xCell.setFormula(getDataAsString("RECIPIENT_NAME"));


            XCellRange xCellRange = xSpreadsheet
                    .getCellRangeByPosition(1, row + 7, 6, row + 7);

            com.sun.star.util.XMergeable xMerge = UnoRuntime
                    .queryInterface(com.sun.star.util.XMergeable.class,
                            xCellRange);
            xMerge.merge(true);
            xPropSet = UnoRuntime
                    .queryInterface(XPropertySet.class,
                            xCell);
            xPropSet.setPropertyValue("HoriJustify", CellHoriJustify.LEFT);

            xCell = xSpreadsheet.getCellByPosition(1, row + 7);
            xCell.setFormula("_________________________/______________/");

            xPropSet = UnoRuntime
                    .queryInterface(XPropertySet.class,
                            xCell);
            xPropSet.setPropertyValue("HoriJustify", CellHoriJustify.LEFT);

            xCell = xSpreadsheet.getCellByPosition(9, row + 4);
            xCell.setFormula("Поставщик: ");
            xPropSet = UnoRuntime
                    .queryInterface(XPropertySet.class,
                            xCell);
            xPropSet.setPropertyValue("HoriJustify", CellHoriJustify.LEFT);


            xCell = xSpreadsheet.getCellByPosition(9, row + 5);
            xCell.setFormula(COMPANY_NAME);

            xCellRange = xSpreadsheet
                    .getCellRangeByPosition(9, row + 7, 12, row + 7);

            xMerge = UnoRuntime
                    .queryInterface(com.sun.star.util.XMergeable.class,
                            xCellRange);
            xMerge.merge(true);
            xPropSet = UnoRuntime
                    .queryInterface(XPropertySet.class,
                            xCell);
            xPropSet.setPropertyValue("HoriJustify", CellHoriJustify.LEFT);


            xCell = xSpreadsheet.getCellByPosition(9, row + 7);
            xCell.setFormula("_________________________/______________/");

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
}
