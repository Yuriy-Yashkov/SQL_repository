package by.march8.ecs.application.modules.warehouse.external.shipping.reports;

import by.gomel.freedev.ucframework.uccore.report.model.ReportProperties;
import by.march8.ecs.application.modules.warehouse.external.shipping.model.SaleDocumentReport;
import by.march8.ecs.application.modules.warehouse.external.shipping.model.TotalSummingUp;
import by.march8.ecs.framework.helpers.digits.CurrencyType;
import by.march8.ecs.framework.helpers.digits.DigitToWords;
import com.sun.star.container.XNamed;
import com.sun.star.lang.XComponent;
import com.sun.star.sheet.XSpreadsheet;
import com.sun.star.sheet.XSpreadsheetDocument;
import com.sun.star.sheet.XSpreadsheets;
import com.sun.star.table.XCell;
import com.sun.star.uno.UnoRuntime;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Andy 16.01.2019 - 9:37.
 */
public class AdjustmentDocumentReport extends AbstractInvoiceReport {
    public AdjustmentDocumentReport(SaleDocumentReport report, SaleDocumentReport reportOld) {
        super(report, reportOld);
        documentReport = report;
        documentReportOld = reportOld;
        create();
    }

    @Override
    ReportProperties prepareProperties(ReportProperties properties) {
        // Устанавливаем имя файла шаблона
        properties.setBlankName("adjustment_invoice_" + saleDocumentBase.getDocumentNumber());
        properties.getTemplate().setTemplateName("adjustment_sale_document.ots");
        return properties;
    }

    @Override
    boolean populateData(XComponent component) {

        try {

            XSpreadsheetDocument xSpreadsheetDocument = UnoRuntime
                    .queryInterface(XSpreadsheetDocument.class, component);
            XSpreadsheets xSpreadsheets = xSpreadsheetDocument.getSheets();
            Object sheet = xSpreadsheets.getByName("Лист1");
            XSpreadsheet xSpreadsheet = UnoRuntime.queryInterface(XSpreadsheet.class, sheet);
            XNamed xNamed = UnoRuntime.queryInterface(XNamed.class, xSpreadsheet);
            xNamed.setName("Корректировочный акт к ТТН №" + saleDocumentBase.getDocumentNumber() + " (" + detailMap.get("TIMESTAMP") + ")");
            // ----------------Заполнение шапки документа
            XCell xCell = xSpreadsheet.getCellByPosition(0, 2);
            xCell.setFormula("К ТТН № " + saleDocumentBase.getDocumentNumber() + " от " + getDataAsString("DOCUMENT_DATE") + "г.");
            //xCell.setFormula("К ТТН серии № " + saleDocumentBase.getDocumentNumber().substring(0,2) + " № "+ saleDocumentBase.getDocumentNumber().substring(2, saleDocumentBase.getDocumentNumber().length()) + " от " + getDataAsString("DOCUMENT_DATE")+"г.");

            xCell = xSpreadsheet.getCellByPosition(0, 3);
            xCell.setFormula("г. Гомель");
            xCell = xSpreadsheet.getCellByPosition(12, 3);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMMM YYYY");
            xCell.setFormula(simpleDateFormat.format(new Date()));

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

            xCell = xSpreadsheet.getCellByPosition(0, 13);
            xCell.setFormula(getDataAsString("PRODUCT_NAME"));

            xCell = xSpreadsheet.getCellByPosition(3, 13);
            xCell.setFormula(getDataAsString("UNIT_OF_MEASURE"));

            // Итого количество
            xCell = xSpreadsheet.getCellByPosition(4, 13);
            xCell.setValue(summingUp.getAmount());
            // Ставка НДС
            xCell = xSpreadsheet.getCellByPosition(7, 13);

            if (getDataAsObject("VAT_VALUE").equals("-")) {
                xCell.setFormula("-");
            } else {
                xCell.setValue((float) getDataAsObject("VAT_VALUE"));
            }

            // Стоимость
            xCell = xSpreadsheet.getCellByPosition(6, 13);
            xCell.setValue(summingUp.getValueSumCost());
            // Сумма НДС
            xCell = xSpreadsheet.getCellByPosition(8, 13);
            xCell.setValue(summingUp.getValueSumVat());
            // Стоимость с НДС
            xCell = xSpreadsheet.getCellByPosition(9, 13);
            xCell.setValue(summingUp.getValueSumCostAndVat());


            // Грузоместа
            xCell = xSpreadsheet.getCellByPosition(10, 13);
            xCell.setValue((double) getDataAsObject("CARGO_SPACE"));
            //Масса, всего
            xCell = xSpreadsheet.getCellByPosition(11, 13);
            xCell.setValue(summingUp.getWeight());
            // Примечание
            xCell = xSpreadsheet.getCellByPosition(12, 13);
            xCell.setFormula(getDataAsString("SUPPORT_DOCUMENT") + "\n" + getDataAsString("NOTE") + "\n" + getDataAsString("PAGE_COUNT"));

            // ***************************************************************************
            // Итого количество
            xCell = xSpreadsheet.getCellByPosition(4, 15);
            xCell.setValue(summingUp.getAmount());
            // Стоимость
            xCell = xSpreadsheet.getCellByPosition(6, 15);
            xCell.setValue(summingUp.getValueSumCost());
            // Сумма НДС
            xCell = xSpreadsheet.getCellByPosition(8, 15);
            xCell.setValue(summingUp.getValueSumVat());
            // Стоимость с НДС
            xCell = xSpreadsheet.getCellByPosition(9, 15);
            xCell.setValue(summingUp.getValueSumCostAndVat());
            // Грузоместа
            xCell = xSpreadsheet.getCellByPosition(10, 15);
            xCell.setValue((double) getDataAsObject("CARGO_SPACE"));
            //Масса, всего
            xCell = xSpreadsheet.getCellByPosition(11, 15);
            xCell.setValue(summingUp.getWeight());

            long vat = (long) summingUp.getValueSumVat();
            long sumAndVat = (long) summingUp.getValueSumCostAndVat();
            double kopVat = ((summingUp.getValueSumVat() - vat));
            BigDecimal bd = new BigDecimal(Double.toString(kopVat));
            bd = bd.setScale(2, RoundingMode.HALF_UP);
            kopVat = (int) (bd.doubleValue() * 100);
            double kopSumAndVat = ((summingUp.getValueSumCostAndVat() - sumAndVat));
            bd = new BigDecimal(Double.toString(kopSumAndVat));
            bd = bd.setScale(2, RoundingMode.HALF_UP);
            kopSumAndVat = (int) (bd.doubleValue() * 100);
            DigitToWords strSumVAT = new DigitToWords(vat, CurrencyType.BYN);
            DigitToWords strSumCostAndVAT = new DigitToWords(sumAndVat, CurrencyType.BYN);

            //Масса, всего
            xCell = xSpreadsheet.getCellByPosition(3, 16);
            xCell.setFormula(strSumVAT.num2str(true).trim());
            xCell = xSpreadsheet.getCellByPosition(3, 18);
            xCell.setFormula(strSumCostAndVAT.num2str(true).trim());
            xCell = xSpreadsheet.getCellByPosition(11, 16);
            xCell.setValue((int) kopVat);
            xCell = xSpreadsheet.getCellByPosition(11, 18);
            xCell.setValue((int) kopSumAndVat);


            /*************************************************************************НОВЫЙ ВАРИАНТ!!!***********************************************/
            //*****************************************************************************
            // Итого по СТАРОЙ НАКЛАДНОЙ
            //*****************************************************************************
            TotalSummingUp summingUpOld = documentReportOld.getSummingUp();

            xCell = xSpreadsheet.getCellByPosition(0, 22);
            xCell.setFormula(getDataAsString("PRODUCT_NAME"));

            xCell = xSpreadsheet.getCellByPosition(3, 22);
            xCell.setFormula(getDataAsString("UNIT_OF_MEASURE"));

            // Итого количество
            xCell = xSpreadsheet.getCellByPosition(4, 22);
            xCell.setValue(summingUp.getAmount() - summingUpOld.getAmount());
            // Ставка НДС
            xCell = xSpreadsheet.getCellByPosition(7, 22);

            if (getDataAsObject("VAT_VALUE").equals("-")) {
                xCell.setFormula("-");
            } else {
                xCell.setValue((float) getDataAsObject("VAT_VALUE"));
            }

            // Стоимость
            xCell = xSpreadsheet.getCellByPosition(6, 22);
            xCell.setValue(summingUp.getValueSumCost() - summingUpOld.getValueSumCost());
            // Сумма НДС
            xCell = xSpreadsheet.getCellByPosition(8, 22);
            xCell.setValue(summingUp.getValueSumVat() - summingUpOld.getValueSumVat());
            // Стоимость с НДС
            xCell = xSpreadsheet.getCellByPosition(9, 22);
            xCell.setValue(summingUp.getValueSumCostAndVat() - summingUpOld.getValueSumCostAndVat());


            // Грузоместа
            xCell = xSpreadsheet.getCellByPosition(10, 22);
            xCell.setValue((double) getDataAsObject("CARGO_SPACE"));
            //Масса, всего
            xCell = xSpreadsheet.getCellByPosition(11, 22);
            xCell.setValue(summingUp.getWeight() - summingUpOld.getWeight());
            // Примечание
            xCell = xSpreadsheet.getCellByPosition(12, 22);
            xCell.setFormula(getDataAsString("SUPPORT_DOCUMENT") + "\n" + getDataAsString("NOTE") + "\n" + getDataAsString("PAGE_COUNT"));

            // ***************************************************************************
            // Итого количество
            xCell = xSpreadsheet.getCellByPosition(4, 24);
            xCell.setValue(summingUp.getAmount() - summingUpOld.getAmount());
            // Стоимость
            xCell = xSpreadsheet.getCellByPosition(6, 24);
            xCell.setValue(summingUp.getValueSumCost() - summingUpOld.getValueSumCost());
            // Сумма НДС
            xCell = xSpreadsheet.getCellByPosition(8, 24);
            xCell.setValue(summingUp.getValueSumVat() - summingUpOld.getValueSumVat());
            // Стоимость с НДС
            xCell = xSpreadsheet.getCellByPosition(9, 24);
            xCell.setValue(summingUp.getValueSumCostAndVat() - summingUpOld.getValueSumCostAndVat());
            // Грузоместа
            xCell = xSpreadsheet.getCellByPosition(10, 24);
            xCell.setValue((double) getDataAsObject("CARGO_SPACE"));
            //Масса, всего
            xCell = xSpreadsheet.getCellByPosition(11, 24);
            xCell.setValue(summingUp.getWeight() - summingUpOld.getWeight());

            BigDecimal bdRunding;
            double vatNew = (summingUp.getValueSumVat() - summingUpOld.getValueSumVat());
            double vatNewInteger = (int) vatNew;
            bdRunding = new BigDecimal(vatNew - vatNewInteger).setScale(2, RoundingMode.HALF_UP);
            int vatNewPenny = (int) (bdRunding.doubleValue() * 100);


            double allSumNew = (summingUp.getValueSumCostAndVat() - summingUpOld.getValueSumCostAndVat());
            double allSumNewInteger = (int) allSumNew;
            bdRunding = new BigDecimal(allSumNew - allSumNewInteger).setScale(2, RoundingMode.HALF_UP);
            int allSumNewPenny = (int) (bdRunding.doubleValue() * 100);

            DigitToWords strSumVATNew = new DigitToWords(vatNewInteger, CurrencyType.BYN);
            DigitToWords strSumCostAndVATNew = new DigitToWords(allSumNewInteger, CurrencyType.BYN);

            xCell = xSpreadsheet.getCellByPosition(3, 25);
            xCell.setFormula(strSumVATNew.num2str(true).trim());
            xCell = xSpreadsheet.getCellByPosition(3, 27);
            xCell.setFormula(strSumCostAndVATNew.num2str(true).trim());
            xCell = xSpreadsheet.getCellByPosition(11, 25);
            xCell.setValue(vatNewPenny);
            xCell = xSpreadsheet.getCellByPosition(11, 27);
            xCell.setValue(allSumNewPenny);
        } catch (Exception e) {
            System.out.println("Ошибка формирования отчета ");
            e.printStackTrace();
        }
        return true;
    }
}
