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
import com.sun.star.uno.UnoRuntime;
import common.DateUtils;

import java.util.Date;

/**
 * @author Andy 21.02.2019.
 */
public class AnalysisConcernDownPrimeCostReport extends AbstractInvoiceReport {

    public AnalysisConcernDownPrimeCostReport(final SaleDocumentReport report) {
        super(report);
        documentReport = report;
        create();
    }

    @Override
    ReportProperties prepareProperties(final ReportProperties properties) {
        // Устанавливаем имя файла шаблона
        properties.setBlankName("analysis_concern_primecost_" + saleDocumentBase.getDocumentNumber());
        properties.getTemplate().setTemplateName("analysisConcernDownPrimeCost.ots");
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

            // String contract = getDataAsString("CONTRACT_NUMBER") + " от " + getDataAsString("CONTRACT_DATE_BEGIN") + " г.";
            XCell xCell = xSpreadsheet.getCellByPosition(17, 1);
            //xCell.setFormula(contract);

/*            xCell = xSpreadsheet.getCellByPosition(7, 3);
            xCell.setFormula("между " + getDataAsString("RECIPIENT_NAME") + " и " + getDataAsString("SENDER_NAME"));*/

            xCell = xSpreadsheet.getCellByPosition(2, 2);
            xCell.setFormula("'" + documentReport.getDocument().getRecipientCode());

            xCell = xSpreadsheet.getCellByPosition(4, 2);
            xCell.setFormula(getDataAsString("RECIPIENT_NAME"));

            xCell = xSpreadsheet.getCellByPosition(18, 2);
            xCell.setValue(documentReport.getDocument().getCurrencyRateFixed());

            xCell = xSpreadsheet.getCellByPosition(18, 3);
            xCell.setValue(documentReport.getDocument().getCurrencyRateSale());

            int row = 10;

            Date d = new Date();

            for (SaleDocumentDetailItemReport item : detailList) {

                xCell = xSpreadsheet.getCellByPosition(0, row);
                xCell.setFormula(String.valueOf(row - 9));

                xCell = xSpreadsheet.getCellByPosition(1, row);
                xCell.setFormula(item.getItemName());

                xCell = xSpreadsheet.getCellByPosition(2, row);
                //xCell.setFormula(item.getArticleName());

                xCell = xSpreadsheet.getCellByPosition(3, row);
                xCell.setFormula(item.getMeasureUnit());

                xCell = xSpreadsheet.getCellByPosition(4, row);
                xCell.setValue(item.getAmountPrint());

                xCell = xSpreadsheet.getCellByPosition(5, row);
                xCell.setValue(item.getAccountingPrice());

                xCell = xSpreadsheet.getCellByPosition(6, row);
                xCell.setValue(item.getPlanPrimeCost());

                xCell = xSpreadsheet.getCellByPosition(7, row);
                xCell.setValue(item.getPlanProfitability());

                xCell = xSpreadsheet.getCellByPosition(8, row);
                xCell.setValue(item.getValuePrice());

                xCell = xSpreadsheet.getCellByPosition(9, row);
                xCell.setValue(item.getValuePriceCurrency());


                xCell = xSpreadsheet.getCellByPosition(10, row);
                xCell.setValue(item.getValuePriceCurrency());


                xCell = xSpreadsheet.getCellByPosition(11, row);
                xCell.setValue(item.getValuePrice());

                //(((item_.getWholesalePrice() - primeCost) / primeCost) * 100);

                xCell = xSpreadsheet.getCellByPosition(12, row);
                xCell.setValue((((item.getValuePrice() - item.getPlanPrimeCost()) / item.getPlanPrimeCost()) * 100));

                xCell = xSpreadsheet.getCellByPosition(13, row);
                //xCell.setValue(item.getValuePrice() - item.getPlanPrice());
                xCell.setFormula("=L" + (row + 1) + "-Q" + (row + 1) + "");

                xCell = xSpreadsheet.getCellByPosition(14, row);
                xCell.setFormula("=N" + (row + 1) + "*H" + (row + 1) + "");

                xCell = xSpreadsheet.getCellByPosition(15, row);
                xCell.setFormula(DateUtils.getNormalDateFormat(d));

                xCell = xSpreadsheet.getCellByPosition(16, row);
                xCell.setValue(item.getPlanPrimeCost());

                xCell = xSpreadsheet.getCellByPosition(17, row);
                if (documentReport.getDocument().getDiscountType() == 3) {
                    xCell.setValue(item.getDiscountValue());
                } else {
                    xCell.setValue(documentReport.getDocument().getDiscountValue());
                }

                xCell = xSpreadsheet.getCellByPosition(18, row);
                xCell.setFormula("=I" + (row + 1) + "-(I" + (row + 1) + "*R" + (row + 1) + "/100)");

                xCell = xSpreadsheet.getCellByPosition(19, row);
                xCell.setFormula("=ROUND(S" + (row + 1) + "/S3*100;2)");

                xCell = xSpreadsheet.getCellByPosition(20, row);
                xCell.setFormula("=ROUND(T" + (row + 1) + "*S4/100;2)");

                xCell = xSpreadsheet.getCellByPosition(21, row);
                xCell.setFormula("=(U" + (row + 1) + "-Q" + (row + 1) + ")/Q" + (row + 1) + "*100");

                rowFormat(xSpreadsheet, row - 1, 16, 10);
                row++;
            }

            rowFormat(xSpreadsheet, row - 1, 16, 10);

            xCell = xSpreadsheet.getCellByPosition(13, row);
            xCell.setFormula("Итого:");

            xCell = xSpreadsheet.getCellByPosition(14, row);
            xCell.setFormula("=SUM(O" + (11) + ":O"
                    + (row) + ")");

            row++;
            row++;
            xCell = xSpreadsheet.getCellByPosition(1, row);
            xCell.setFormula("Зам. директора по экономике");
            xPropSet = UnoRuntime
                    .queryInterface(XPropertySet.class,
                            xCell);
            xPropSet.setPropertyValue("HoriJustify", CellHoriJustify.LEFT);

            xCell = xSpreadsheet.getCellByPosition(6, row);
            xCell.setFormula("__________________/ О.А. Живодер /");
            xPropSet = UnoRuntime
                    .queryInterface(XPropertySet.class,
                            xCell);
            xPropSet.setPropertyValue("HoriJustify", CellHoriJustify.LEFT);
            row++;
            row++;

            xCell = xSpreadsheet.getCellByPosition(1, row);
            xCell.setFormula("Зам. директора по коммерческим вопросам");
            xPropSet = UnoRuntime
                    .queryInterface(XPropertySet.class,
                            xCell);
            xPropSet.setPropertyValue("HoriJustify", CellHoriJustify.LEFT);

            xCell = xSpreadsheet.getCellByPosition(6, row);
            xCell.setFormula("__________________/ О.В. Шолохова /");
            xPropSet = UnoRuntime
                    .queryInterface(XPropertySet.class,
                            xCell);
            xPropSet.setPropertyValue("HoriJustify", CellHoriJustify.LEFT);
            row++;
            row++;

            xCell = xSpreadsheet.getCellByPosition(1, row);
            xCell.setFormula("Начальник ПЭО");
            xPropSet = UnoRuntime
                    .queryInterface(XPropertySet.class,
                            xCell);
            xPropSet.setPropertyValue("HoriJustify", CellHoriJustify.LEFT);

            xCell = xSpreadsheet.getCellByPosition(6, row);
            xCell.setFormula("__________________/ Н.В. Кучева /");
            xPropSet = UnoRuntime
                    .queryInterface(XPropertySet.class,
                            xCell);
            xPropSet.setPropertyValue("HoriJustify", CellHoriJustify.LEFT);
            row++;
            row++;

            xCell = xSpreadsheet.getCellByPosition(1, row);
            xCell.setFormula("Начальник ОВЭС");
            xPropSet = UnoRuntime
                    .queryInterface(XPropertySet.class,
                            xCell);
            xPropSet.setPropertyValue("HoriJustify", CellHoriJustify.LEFT);
            xCell = xSpreadsheet.getCellByPosition(6, row);
            xCell.setFormula("__________________/ С.Я. Дмитриченко /");
            xPropSet = UnoRuntime
                    .queryInterface(XPropertySet.class,
                            xCell);
            xPropSet.setPropertyValue("HoriJustify", CellHoriJustify.LEFT);
            row++;
            row++;

            xCell = xSpreadsheet.getCellByPosition(1, row);
            xCell.setFormula("Исполнитель");
            xPropSet = UnoRuntime
                    .queryInterface(XPropertySet.class,
                            xCell);
            xPropSet.setPropertyValue("HoriJustify", CellHoriJustify.LEFT);

            xCell = xSpreadsheet.getCellByPosition(6, row);
            xCell.setFormula("__________________/ М.Д. Казубанова /");
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
