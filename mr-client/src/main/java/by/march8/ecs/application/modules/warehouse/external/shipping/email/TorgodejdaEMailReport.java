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
import com.sun.star.uno.UnoRuntime;

/**
 * @author Andy 04.10.2018 - 8:08.
 */
public class TorgodejdaEMailReport extends AbstractEMailReport {

    public TorgodejdaEMailReport(SaleDocumentReport report) {
        super(report);
        documentReport = report;
        create();
    }

    @Override
    ReportProperties prepareProperties(ReportProperties properties) {
        // Устанавливаем имя файла шаблона
        properties.setBlankName("" + saleDocumentBase.getDocumentNumber());
        properties.getTemplate().setTemplateName("email_torgodejda_blank.ots");
        return properties;
    }

    @Override
    boolean populateData(XComponent component) {
        try {
            XSpreadsheetDocument xSpreadsheetDocument = UnoRuntime
                    .queryInterface(XSpreadsheetDocument.class, component);
            XSpreadsheets xSpreadsheets = xSpreadsheetDocument.getSheets();
            Object sheet = xSpreadsheets.getByName("ФорматВыгрузки");
            XSpreadsheet xSpreadsheet = UnoRuntime
                    .queryInterface(XSpreadsheet.class, sheet);

            XNamed xNamed = UnoRuntime.queryInterface(XNamed.class,
                    xSpreadsheet);

            //xNamed.setName("ПСЦ к ТТН№ " + saleDocumentBase.getDocumentNumber());
            String COMPANY_NAME = "ОАО \"8 Марта\"";
            String REPUBLIC = "РБ";


            XCell xCell = xSpreadsheet.getCellByPosition(7, 1);

            int row = 1;

            String signPart = "";
            String digitPart = "";
            if (saleDocumentBase.getDocumentNumber().length() > 7) {
                signPart = saleDocumentBase.getDocumentNumber().substring(0, 2);
                digitPart = saleDocumentBase.getDocumentNumber().substring(2, saleDocumentBase.getDocumentNumber().length());
            }

            for (SaleDocumentDetailItemReport item : detailList) {

                xCell = xSpreadsheet.getCellByPosition(0, row);
                xCell.setFormula(getDataAsString("SENDER_UNP"));

                xCell = xSpreadsheet.getCellByPosition(1, row);
                xCell.setFormula(COMPANY_NAME);

                xCell = xSpreadsheet.getCellByPosition(2, row);
                xCell.setFormula("№ " + getDataAsString("CONTRACT_NUMBER") + " от " + getDataAsString("CONTRACT_DATE_BEGIN") + "г.");

                xCell = xSpreadsheet.getCellByPosition(3, row);
                xCell.setFormula(saleDocumentBase.getDocumentNumber());

                xCell = xSpreadsheet.getCellByPosition(4, row);
                xCell.setFormula(getDataAsString("DOCUMENT_DATE"));

                xCell = xSpreadsheet.getCellByPosition(5, row);
                xCell.setFormula(item.getItemName());

                xCell = xSpreadsheet.getCellByPosition(6, row);
                xCell.setFormula(item.getArticleNumber());

                xCell = xSpreadsheet.getCellByPosition(7, row);
                xCell.setFormula(item.getModelNumber());

                xCell = xSpreadsheet.getCellByPosition(8, row);
                xCell.setFormula(String.valueOf(item.getItemGrade()));

                xCell = xSpreadsheet.getCellByPosition(9, row);
                xCell.setFormula(String.valueOf(item.getItemGrowz()));

                xCell = xSpreadsheet.getCellByPosition(10, row);
                xCell.setFormula(String.valueOf(item.getItemSize()));

                xCell = xSpreadsheet.getCellByPosition(11, row);
                xCell.setFormula(String.valueOf(0));

                xCell = xSpreadsheet.getCellByPosition(12, row);
                xCell.setFormula(item.getItemColorNew());

                xCell = xSpreadsheet.getCellByPosition(13, row);
                xCell.setFormula(item.getCanvasComposition());

                xCell = xSpreadsheet.getCellByPosition(14, row);
                xCell.setFormula("'" + item.getEanCode());

                xCell = xSpreadsheet.getCellByPosition(15, row);
                xCell.setFormula("'" + item.getTnvedCode());

                xCell = xSpreadsheet.getCellByPosition(16, row);
                xCell.setFormula(String.valueOf(112));

                xCell = xSpreadsheet.getCellByPosition(17, row);
                xCell.setFormula(COMPANY_NAME);

                xCell = xSpreadsheet.getCellByPosition(18, row);
                xCell.setFormula(String.valueOf(112));

                xCell = xSpreadsheet.getCellByPosition(19, row);
                xCell.setValue(item.getWeight() / item.getAmountPrint());

                xCell = xSpreadsheet.getCellByPosition(20, row);
                xCell.setFormula(item.getMeasureUnit());

                xCell = xSpreadsheet.getCellByPosition(21, row);
                xCell.setFormula("");
                xCell = xSpreadsheet.getCellByPosition(22, row);
                xCell.setFormula("");

                xCell = xSpreadsheet.getCellByPosition(23, row);
                xCell.setValue(item.getAmountPrint());

                xCell = xSpreadsheet.getCellByPosition(24, row);
                xCell.setValue(item.getAccountingPrice());

                xCell = xSpreadsheet.getCellByPosition(25, row);
                xCell.setValue(item.getAccountingPrice());

                xCell = xSpreadsheet.getCellByPosition(26, row);
                xCell.setValue(item.getValuePrice());

                xCell = xSpreadsheet.getCellByPosition(27, row);
                xCell.setValue(item.getValueVAT());

                xCell = xSpreadsheet.getCellByPosition(28, row);
                xCell.setValue(item.getValueSumVat());

                xCell = xSpreadsheet.getCellByPosition(29, row);
                xCell.setValue(item.getValueSumCostAndVat());

                xCell = xSpreadsheet.getCellByPosition(30, row);
                xCell.setValue(saleDocumentBase.getDiscountValue());

                xCell = xSpreadsheet.getCellByPosition(31, row);
                xCell.setValue(0);

                xCell = xSpreadsheet.getCellByPosition(32, row);
                xCell.setValue(item.getAmountPrint());

                xCell = xSpreadsheet.getCellByPosition(33, row);
                xCell.setValue(item.getAmount());

                xCell = xSpreadsheet.getCellByPosition(34, row);
                xCell.setValue(item.getWeight());

                xCell = xSpreadsheet.getCellByPosition(35, row);
                xCell.setFormula(item.getCertificateDescription());

                xCell = xSpreadsheet.getCellByPosition(36, row);
                xCell.setFormula(item.getItemPriceList());

                xCell = xSpreadsheet.getCellByPosition(37, row);
                xCell.setFormula(signPart);

                xCell = xSpreadsheet.getCellByPosition(38, row);
                xCell.setFormula(digitPart);

                row++;
/*
            XCellRange xCellRange = xSpreadsheet
                    .getCellRangeByPosition(0, row - 1, 0,
                            row - 1);
            //setRowHeight(xCellRange, 1600);
            rowFormat(xSpreadsheet, row - 1, 17);*/
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public void useToken() {

    }
}
