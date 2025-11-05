package by.march8.ecs.application.modules.warehouse.external.shipping.reports;

import by.gomel.freedev.ucframework.uccore.report.model.ReportProperties;
import by.march8.ecs.application.modules.warehouse.external.shipping.model.SaleDocumentReport;
import by.march8.ecs.application.modules.warehouse.external.shipping.model.TotalSummingUp;
import by.march8.entities.warehouse.SaleDocumentDetailItemReport;
import com.sun.star.container.XNamed;
import com.sun.star.lang.XComponent;
import com.sun.star.sheet.XSpreadsheet;
import com.sun.star.sheet.XSpreadsheetDocument;
import com.sun.star.sheet.XSpreadsheets;
import com.sun.star.table.XCell;
import com.sun.star.uno.UnoRuntime;
import common.DateUtils;

public class ContractorInvoiceReport extends AbstractInvoiceReport {

    public ContractorInvoiceReport(final SaleDocumentReport report) {
        super(report);
        documentReport = report;
        create();
    }

    @Override
    ReportProperties prepareProperties(final ReportProperties properties) {
        // Устанавливаем имя файла шаблона
        properties.setBlankName("contractor_invoice_" + saleDocumentBase.getDocumentNumber());
        properties.getTemplate().setTemplateName("contractor_invoice.ots");
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

            xNamed.setName("Счет-фактура № " + saleDocumentBase.getDocumentNumber());

            XCell xCell;

            xCell = xSpreadsheet.getCellByPosition(1, 8);
            xCell.setFormula(getDataAsString("RECIPIENT_NAME"));

            xCell = xSpreadsheet.getCellByPosition(1, 9);
            xCell.setFormula(getDataAsString("RECIPIENT_ADDRESS"));

            xCell = xSpreadsheet.getCellByPosition(0, 12);
            xCell.setFormula("№ "
                    + documentReport.getDocument().getDocumentNumber()
                    + " от " + DateUtils.getNormalDateFormat(documentReport.getDocument().getDocumentDate())
                    + "г.");

            int row = 16;
            com.sun.star.table.XColumnRowRange xCRRange = UnoRuntime.queryInterface(com.sun.star.table.XColumnRowRange.class, xSpreadsheet);
            com.sun.star.table.XTableRows xRows = xCRRange.getRows();

            for (SaleDocumentDetailItemReport item : detailList) {


                xRows.insertByIndex(row + 1, 1);

                xCell = xSpreadsheet.getCellByPosition(0, row);
                xCell.setFormula(String.valueOf(row - 15));

                xCell = xSpreadsheet.getCellByPosition(1, row);
                xCell.setFormula(item.getProductNameSizeStringSingleLine());

                xCell = xSpreadsheet.getCellByPosition(2, row);
                xCell.setFormula(item.getMeasureUnit());

                xCell = xSpreadsheet.getCellByPosition(3, row);
                xCell.setValue(item.getAmountPrint());

                xCell = xSpreadsheet.getCellByPosition(4, row);
                xCell.setValue(item.getValuePrice());

                xCell = xSpreadsheet.getCellByPosition(5, row);
                xCell.setValue(item.getValueSumCost());

                xCell = xSpreadsheet.getCellByPosition(6, row);
                xCell.setValue(item.getValueVat());

                xCell = xSpreadsheet.getCellByPosition(7, row);
                xCell.setFormula(String.valueOf(item.getValueSumVat()));

                xCell = xSpreadsheet.getCellByPosition(8, row);
                xCell.setValue(item.getValueSumCostAndVat());

                rowFormat(xSpreadsheet, row - 1, 8, 10);
                row++;
            }

            rowFormat(xSpreadsheet, row - 1, 8, 10);

            xRows.removeByIndex(row, 1);


            TotalSummingUp summingUp = documentReport.getSummingUp();

            // Итого количество
            xCell = xSpreadsheet.getCellByPosition(3, row);
            xCell.setValue(summingUp.getAmount());
            // Сумма
            xCell = xSpreadsheet.getCellByPosition(5, row);
            xCell.setValue(summingUp.getValueSumCost());

            // Сумма НДС
            xCell = xSpreadsheet.getCellByPosition(7, row);
            xCell.setValue(summingUp.getValueSumVat());

            xCell = xSpreadsheet.getCellByPosition(8, row);
            xCell.setValue(summingUp.getValueSumCostAndVat());

            xCell = xSpreadsheet.getCellByPosition(1, row + 1);
            xCell.setFormula(getDataAsString("SUM_COST_AND_VAT_CURRENCY_STRING") + "\nв т.ч. НДС " + getDataAsString("SUM_VAT_CURRENCY_STRING"));

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }
}
