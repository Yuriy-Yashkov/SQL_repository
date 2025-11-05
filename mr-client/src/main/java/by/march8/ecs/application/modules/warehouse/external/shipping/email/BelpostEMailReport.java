package by.march8.ecs.application.modules.warehouse.external.shipping.email;

import by.gomel.freedev.ucframework.uccore.report.model.ReportProperties;
import by.march8.ecs.application.modules.warehouse.external.shipping.model.SaleDocumentReport;
import by.march8.entities.warehouse.SaleDocumentDetailItemReport;
import com.sun.star.container.XNamed;
import com.sun.star.lang.XComponent;
import com.sun.star.sheet.XSpreadsheet;
import com.sun.star.sheet.XSpreadsheetDocument;
import com.sun.star.sheet.XSpreadsheets;
import com.sun.star.table.XCell;
import com.sun.star.table.XCellRange;
import com.sun.star.uno.UnoRuntime;

/**
 * @author Andy 29.11.2017.
 */
public class BelpostEMailReport extends AbstractEMailReport {

    public BelpostEMailReport(final SaleDocumentReport report) {
        super(report);
        documentReport = report;
        create();
    }


    @Override
    ReportProperties prepareProperties(final ReportProperties properties) {
        // Устанавливаем имя файла шаблона
        properties.setBlankName("" + saleDocumentBase.getDocumentNumber());
        properties.getTemplate().setTemplateName("email_belpost_blank.ots");
        return properties;
    }

    @Override
    public void useToken() {

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

            //xNamed.setName("ПСЦ к ТТН№ " + saleDocumentBase.getDocumentNumber());
            String COMPANY_NAME = "ОАО \"8 Марта\"";
            String REPUBLIC = "РБ";


            XCell xCell = xSpreadsheet.getCellByPosition(7, 1);
            /*xCell.setFormula("№ " + getDataAsString("CONTRACT_NUMBER") + " от " + getDataAsString("CONTRACT_DATE_BEGIN") + "г.");

            xCell = xSpreadsheet.getCellByPosition(3, 3);
            xCell.setFormula(getDataAsString("RECIPIENT_NAME"));

            xCell = xSpreadsheet.getCellByPosition(3, 4);
            xCell.setFormula(getDataAsString("SENDER_NAME"));

            xCell = xSpreadsheet.getCellByPosition(5, 5);
            xCell.setFormula(getDataAsString("CONTRACT_DATE_BEGIN") + "г.");*/

            int row = 2;

            String signPart = "";
            String digitPart = "";
            if (saleDocumentBase.getDocumentNumber().length() > 7) {
                signPart = saleDocumentBase.getDocumentNumber().substring(0, 2);
                digitPart = saleDocumentBase.getDocumentNumber().substring(2, saleDocumentBase.getDocumentNumber().length());

            }

            for (SaleDocumentDetailItemReport item : detailList) {


                xCell = xSpreadsheet.getCellByPosition(0, row);
                xCell.setFormula(signPart);

                xCell = xSpreadsheet.getCellByPosition(1, row);
                xCell.setFormula(digitPart);

                xCell = xSpreadsheet.getCellByPosition(2, row);
                xCell.setFormula(getDataAsString("DOCUMENT_DATE"));

                xCell = xSpreadsheet.getCellByPosition(3, row);
                xCell.setFormula(getDataAsString("RECIPIENT_UNP"));

                xCell = xSpreadsheet.getCellByPosition(4, row);
                xCell.setFormula(COMPANY_NAME);

                xCell = xSpreadsheet.getCellByPosition(5, row);
                xCell.setFormula("№ " + getDataAsString("CONTRACT_NUMBER") + " от " + getDataAsString("CONTRACT_DATE_BEGIN") + "г.");

                xCell = xSpreadsheet.getCellByPosition(6, row);
                xCell.setFormula(item.getProductNameSizeStringSingleLine());

                xCell = xSpreadsheet.getCellByPosition(7, row);
                xCell.setFormula("'" + item.getEanCode());

                xCell = xSpreadsheet.getCellByPosition(8, row);
                xCell.setFormula(item.getArticleNumber());

                xCell = xSpreadsheet.getCellByPosition(9, row);
                xCell.setFormula(item.getMeasureUnit());


                xCell = xSpreadsheet.getCellByPosition(10, row);
                xCell.setValue(item.getAccountingPrice());

                xCell = xSpreadsheet.getCellByPosition(11, row);
                xCell.setValue(saleDocumentBase.getDiscountValue());

                xCell = xSpreadsheet.getCellByPosition(12, row);
                xCell.setValue(item.getAmountPrint());

                xCell = xSpreadsheet.getCellByPosition(13, row);
                xCell.setValue(item.getValuePrice());

                xCell = xSpreadsheet.getCellByPosition(14, row);
                xCell.setValue(item.getValueVAT());

                xCell = xSpreadsheet.getCellByPosition(15, row);
                xCell.setValue(item.getValueSumVat());

                xCell = xSpreadsheet.getCellByPosition(16, row);
                xCell.setValue(item.getValueSumCostAndVat());

                row++;

                XCellRange xCellRange = xSpreadsheet
                        .getCellRangeByPosition(0, row - 1, 0,
                                row - 1);
                //setRowHeight(xCellRange, 1600);
                rowFormat(xSpreadsheet, row - 1, 17);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;


    }
}
