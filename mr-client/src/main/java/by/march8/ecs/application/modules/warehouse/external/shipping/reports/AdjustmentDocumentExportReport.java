package by.march8.ecs.application.modules.warehouse.external.shipping.reports;

import by.gomel.freedev.ucframework.uccore.report.model.ReportProperties;
import by.march8.ecs.application.modules.warehouse.external.shipping.model.SaleDocumentReport;
import by.march8.ecs.application.modules.warehouse.external.shipping.model.TotalSummingUp;
import com.sun.star.container.XNamed;
import com.sun.star.lang.XComponent;
import com.sun.star.sheet.XSpreadsheet;
import com.sun.star.sheet.XSpreadsheetDocument;
import com.sun.star.sheet.XSpreadsheets;
import com.sun.star.table.XCell;
import com.sun.star.uno.UnoRuntime;

/**
 * @author Andy 16.01.2019 - 9:37.
 */
public class AdjustmentDocumentExportReport extends AbstractInvoiceReport {

    public AdjustmentDocumentExportReport(SaleDocumentReport report) {
        super(report);
        documentReport = report;
        create();
    }

    @Override
    ReportProperties prepareProperties(ReportProperties properties) {
        // Устанавливаем имя файла шаблона
        properties.setBlankName("adjustment_invoice_" + saleDocumentBase.getDocumentNumber());
        properties.getTemplate().setTemplateName("adjustment_sale_document_export.ots");
        return properties;
    }

    @Override
    boolean populateData(XComponent component) {
        try {
            XSpreadsheetDocument xSpreadsheetDocument = UnoRuntime
                    .queryInterface(XSpreadsheetDocument.class, component);
            XSpreadsheets xSpreadsheets = xSpreadsheetDocument.getSheets();
            Object sheet = xSpreadsheets.getByName("Лист1");
            XSpreadsheet xSpreadsheet = UnoRuntime
                    .queryInterface(XSpreadsheet.class, sheet);

            XNamed xNamed = UnoRuntime.queryInterface(XNamed.class,
                    xSpreadsheet);

            xNamed.setName("Корректировочный акт к ТТН №" + saleDocumentBase.getDocumentNumber() + " (" + detailMap.get("TIMESTAMP") + ")");

            // ----------------Заполнение шапки документа
            XCell xCell = xSpreadsheet.getCellByPosition(0, 1);
            //xCell.setFormula("К ТТН серии №" + saleDocumentBase.getDocumentNumber().subSequence(0,1) + " № "+ saleDocumentBase.getDocumentNumber() + "от " + getDataAsString("DOCUMENT_DATE")+"г.");
            xCell.setFormula("К ТТН № " + saleDocumentBase.getDocumentNumber() + " от " + getDataAsString("DOCUMENT_DATE") + "г.");

            xCell = xSpreadsheet.getCellByPosition(2, 5);
            xCell.setFormula(getDataAsString("SENDER_NAME") + " " + getDataAsString("SENDER_ADDRESS"));

            xCell = xSpreadsheet.getCellByPosition(2, 7);
            xCell.setFormula(getDataAsString("RECIPIENT_NAME") + " " + getDataAsString("RECIPIENT_ADDRESS"));

            xCell = xSpreadsheet.getCellByPosition(6, 9);
            xCell.setFormula(getDataAsString("ADJUSTMENT_CAUSE"));

            //*****************************************************************************
            // Итого по накладной
            //*****************************************************************************
            TotalSummingUp summingUp = documentReport.getSummingUp();

            xCell = xSpreadsheet.getCellByPosition(0, 14);
            xCell.setFormula(getDataAsString("PRODUCT_NAME"));

            xCell = xSpreadsheet.getCellByPosition(3, 14);
            xCell.setFormula(getDataAsString("UNIT_OF_MEASURE"));


            // Итого количество
            xCell = xSpreadsheet.getCellByPosition(4, 14);
            xCell.setValue(summingUp.getAmount());
            // Ставка НДС
            xCell = xSpreadsheet.getCellByPosition(7, 14);
            xCell.setValue((float) getDataAsObject("VAT_VALUE"));

            // Стоимость
            xCell = xSpreadsheet.getCellByPosition(6, 14);
            xCell.setValue(summingUp.getValueSumCostCurrency());
            // Сумма НДС
            xCell = xSpreadsheet.getCellByPosition(8, 14);
            xCell.setValue(summingUp.getValueSumVatCurrency());
            // Стоимость с НДС
            xCell = xSpreadsheet.getCellByPosition(9, 14);
            xCell.setValue(summingUp.getValueSumCostAndVatCurrency());

            // Грузоместа
            xCell = xSpreadsheet.getCellByPosition(10, 14);
            xCell.setValue((double) getDataAsObject("CARGO_SPACE"));
            //Масса, всего
            xCell = xSpreadsheet.getCellByPosition(11, 14);
            xCell.setValue(summingUp.getWeight());
            // Примечание
            xCell = xSpreadsheet.getCellByPosition(12, 15);
            xCell.setFormula(getDataAsString("SUPPORT_DOCUMENT") + "\n" + getDataAsString("NOTE") + "\n" + getDataAsString("PAGE_COUNT"));

            // Стоимость
            xCell = xSpreadsheet.getCellByPosition(12, 14);
            xCell.setValue(summingUp.getValueSumCost());
            // Сумма НДС
            xCell = xSpreadsheet.getCellByPosition(13, 14);
            xCell.setValue(summingUp.getValueSumVat());
            // Стоимость с НДС
            xCell = xSpreadsheet.getCellByPosition(14, 14);
            xCell.setValue(summingUp.getValueSumCostAndVat());


            // ***************************************************************************
            // Итого количество
            xCell = xSpreadsheet.getCellByPosition(4, 16);
            xCell.setValue(summingUp.getAmount());

            // Стоимость
            xCell = xSpreadsheet.getCellByPosition(6, 16);
            xCell.setValue(summingUp.getValueSumCostCurrency());
            // Сумма НДС
            xCell = xSpreadsheet.getCellByPosition(8, 16);
            xCell.setValue(summingUp.getValueSumVatCurrency());
            // Стоимость с НДС
            xCell = xSpreadsheet.getCellByPosition(9, 16);
            xCell.setValue(summingUp.getValueSumCostAndVatCurrency());

            // Стоимость
            xCell = xSpreadsheet.getCellByPosition(12, 16);
            xCell.setValue(summingUp.getValueSumCost());
            // Сумма НДС
            xCell = xSpreadsheet.getCellByPosition(13, 16);
            xCell.setValue(summingUp.getValueSumVat());
            // Стоимость с НДС
            xCell = xSpreadsheet.getCellByPosition(14, 16);
            xCell.setValue(summingUp.getValueSumCostAndVat());

            // Грузоместа
            xCell = xSpreadsheet.getCellByPosition(10, 16);
            xCell.setValue((double) getDataAsObject("CARGO_SPACE"));
            //Масса, всего
            xCell = xSpreadsheet.getCellByPosition(11, 16);
            xCell.setValue(summingUp.getWeight());
        } catch (Exception e) {
            System.out.println("Ошибка формирования отчета ");
            e.printStackTrace();
        }
        return true;
    }
}
