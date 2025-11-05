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
public class ProtocolTDVlagrans extends AbstractInvoiceReport {

    public ProtocolTDVlagrans(final SaleDocumentReport report) {
        super(report);
        documentReport = report;
        create();
    }

    @Override
    ReportProperties prepareProperties(final ReportProperties properties) {
        // Устанавливаем имя файла шаблона
        properties.setBlankName("protocol_" + saleDocumentBase.getDocumentNumber());
        properties.getTemplate().setTemplateName("протоколТорговыйДомВлагранс.ots");
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

            xNamed.setName("Спецификация к ТТН№ " + saleDocumentBase.getDocumentNumber());

            String COMPANY_NAME = "ОАО \"8 Марта\"";
            String REPUBLIC = "РБ";

            //String contract = getDataAsString("CONTRACT_NUMBER") + " от " + getDataAsString("CONTRACT_DATE_BEGIN") + " г.";
            XCell xCell = xSpreadsheet.getCellByPosition(17, 1);
            /*xCell.setFormula(contract);

            xCell = xSpreadsheet.getCellByPosition(7, 3);
            xCell.setFormula("между " + getDataAsString("RECIPIENT_NAME") + " и " + getDataAsString("SENDER_NAME"));

            xCell = xSpreadsheet.getCellByPosition(2, 3);
            xCell.setFormula("к договору №" + contract);*/

            int row = 1;

            for (SaleDocumentDetailItemReport item : detailList) {

                xCell = xSpreadsheet.getCellByPosition(0, row);
                xCell.setFormula(item.getArticleNumber());

                xCell = xSpreadsheet.getCellByPosition(1, row);
                xCell.setFormula(item.getItemName());

                xCell = xSpreadsheet.getCellByPosition(2, row);
                xCell.setFormula(item.getProductNameString() + " " + item.getItemColor().toLowerCase());

                xCell = xSpreadsheet.getCellByPosition(3, row);
                xCell.setFormula(COMPANY_NAME);

                xCell = xSpreadsheet.getCellByPosition(4, row);
                xCell.setFormula(item.getItemColorNew());

                xCell = xSpreadsheet.getCellByPosition(5, row);
                xCell.setFormula("");

                xCell = xSpreadsheet.getCellByPosition(6, row);
                xCell.setFormula(REPUBLIC);

                xCell = xSpreadsheet.getCellByPosition(7, row);
                xCell.setFormula("");

                xCell = xSpreadsheet.getCellByPosition(8, row);
                xCell.setFormula("");

                xCell = xSpreadsheet.getCellByPosition(9, row);
                xCell.setFormula(item.getComposition());

                xCell = xSpreadsheet.getCellByPosition(10, row);
                xCell.setFormula(REPUBLIC);

                xCell = xSpreadsheet.getCellByPosition(11, row);
                xCell.setFormula("");

                xCell = xSpreadsheet.getCellByPosition(12, row);
                xCell.setFormula("Товар");

                xCell = xSpreadsheet.getCellByPosition(13, row);
                xCell.setValue(item.getAmountPrint());

                xCell = xSpreadsheet.getCellByPosition(14, row);
                xCell.setFormula("Без НДС");

                xCell = xSpreadsheet.getCellByPosition(15, row);
                xCell.setFormula(item.getItemColorNew());

                xCell = xSpreadsheet.getCellByPosition(16, row);
                xCell.setFormula(item.getItemSizePrint());

                xCell = xSpreadsheet.getCellByPosition(17, row);
                xCell.setFormula(item.getItemColorNew());

                xCell = xSpreadsheet.getCellByPosition(18, row);
                int size = 0;
                try {
                    size = item.getItemSize() / 2;
                } catch (Exception e) {
                    size = 0;
                }

                xCell.setValue(size);

                xCell = xSpreadsheet.getCellByPosition(19, row);
                xCell.setFormula(item.getEanCode());

                xCell = xSpreadsheet.getCellByPosition(20, row);
                xCell.setValue(item.getValuePriceCurrency());

                xCell = xSpreadsheet.getCellByPosition(21, row);
                xCell.setValue(0);
                xCell = xSpreadsheet.getCellByPosition(22, row);
                xCell.setValue(0);
                xCell = xSpreadsheet.getCellByPosition(23, row);
                xCell.setValue(0);
                xCell = xSpreadsheet.getCellByPosition(24, row);
                xCell.setValue(0);


                xCell = xSpreadsheet.getCellByPosition(25, row);
                xCell.setFormula(
                        item.getItemGenderType());
                xCell = xSpreadsheet.getCellByPosition(26, row);
                xCell.setFormula(item.getItemType());
                xCell = xSpreadsheet.getCellByPosition(27, row);
                xCell.setFormula(item.getItemType());
                xCell = xSpreadsheet.getCellByPosition(28, row);
                xCell.setFormula(item.getItemType());

                xCell = xSpreadsheet.getCellByPosition(29, row);
                xCell.setValue(item.getWeight());

                xCell = xSpreadsheet.getCellByPosition(30, row);
                xCell.setValue(0);
                xCell = xSpreadsheet.getCellByPosition(31, row);
                xCell.setValue(0);
                xCell = xSpreadsheet.getCellByPosition(32, row);
                xCell.setValue(0);

                xCell = xSpreadsheet.getCellByPosition(33, row);
                xCell.setFormula("");
                xCell = xSpreadsheet.getCellByPosition(34, row);
                xCell.setFormula("");

                row++;

            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }
}
