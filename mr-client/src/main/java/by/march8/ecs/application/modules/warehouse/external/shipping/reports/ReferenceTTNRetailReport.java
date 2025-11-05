package by.march8.ecs.application.modules.warehouse.external.shipping.reports;

import by.gomel.freedev.ucframework.uccore.report.model.ReportProperties;
import by.march8.ecs.application.modules.warehouse.external.shipping.model.SaleDocumentReport;
import by.march8.ecs.application.modules.warehouse.external.shipping.model.TotalSummingUp;
import com.sun.star.lang.XComponent;
import com.sun.star.sheet.XSpreadsheet;
import com.sun.star.sheet.XSpreadsheetDocument;
import com.sun.star.sheet.XSpreadsheets;
import com.sun.star.uno.UnoRuntime;

/**
 * Документ Справка ТТН 2 для фирменных магазинов
 * @author Andy 27.05.2016.
 */
public class ReferenceTTNRetailReport extends AbstractInvoiceReport {

    public ReferenceTTNRetailReport(final SaleDocumentReport report) {
        super(report);
        documentReport = report;
        create();
    }

    @Override
    ReportProperties prepareProperties(final ReportProperties properties) {
        // Устанавливаем имя файла шаблона
        properties.setBlankName("reference_retail_" + saleDocumentBase.getDocumentNumber());
        properties.getTemplate().setTemplateName("Справка.ots");
        return properties;
    }

    @Override
    boolean populateData(final XComponent component) {
        float charSize = (float) 8;
        try {

            XSpreadsheetDocument xSpreadsheetDocument = UnoRuntime
                    .queryInterface(XSpreadsheetDocument.class, component);
            XSpreadsheets xSpreadsheets = xSpreadsheetDocument.getSheets();
            Object sheet = xSpreadsheets.getByName("Лист1");
            XSpreadsheet xSpreadsheet = UnoRuntime
                    .queryInterface(XSpreadsheet.class, sheet);

            com.sun.star.table.BorderLine aLine = new com.sun.star.table.BorderLine();
            aLine.Color = 0x000000;
            aLine.Color = 0x000000;
            aLine.InnerLineWidth = 0;
            aLine.LineDistance = 0;
            aLine.OuterLineWidth = 25;
            com.sun.star.table.TableBorder aBorder = new com.sun.star.table.TableBorder();
            aBorder.BottomLine = aLine;
            aBorder.IsBottomLineValid = true;

            TotalSummingUp summingUp = documentReport.getSummingUp();

            setPropertyAndValue(xSpreadsheet, 0, 0,
                    "Справка об итогах по ТТН № " + saleDocumentBase.getDocumentNumber(),
                    charSize, null, "LEFT");
            setPropertyAndValue(xSpreadsheet, 0, 1, "Грузополучатель: "
                            + getDataAsString("RECIPIENT_NAME") + " " + getDataAsString("RECIPIENT_ADDRESS")
                    , charSize, null, "LEFT");
            setPropertyAndValue(xSpreadsheet, 0, 3, saleDocumentBase.getDocumentNumber()
                    , charSize, null, "LEFT");
            setPropertyAndValue(xSpreadsheet, 1, 3, String.valueOf((double) getDataAsObject("CARGO_SPACE"))
                    , charSize, null, "LEFT");
            setPropertyAndValue(xSpreadsheet, 2, 3, String.valueOf(summingUp.getAmount()),
                    charSize, null, "LEFT");
            setPropertyAndValue(xSpreadsheet, 3, 3, String.valueOf(summingUp.getValueSumCost()),
                    charSize, null, "LEFT");
            setPropertyAndValue(xSpreadsheet, 4, 3, String.valueOf(summingUp.getValueSumVatRetail()),
                    charSize, null, "LEFT");
            setPropertyAndValue(xSpreadsheet, 5, 3, String.valueOf(summingUp.getValueSumCostRetail()),
                    charSize, null, "LEFT");
            setPropertyAndValue(xSpreadsheet, 6, 3, String.valueOf(summingUp.getWeight()),
                    charSize, null, "LEFT");
            setPropertyAndValue(xSpreadsheet, 1, 4, String.valueOf((double) getDataAsObject("CARGO_SPACE")),
                    charSize, null, "LEFT");
            setPropertyAndValue(xSpreadsheet, 2, 4, String.valueOf(summingUp.getAmount())
                    , charSize, null, "LEFT");
            setPropertyAndValue(xSpreadsheet, 3, 4, String.valueOf(summingUp.getValueSumCost()),
                    charSize, null, "LEFT");
            setPropertyAndValue(xSpreadsheet, 4, 4, String.valueOf(summingUp.getValueSumVatRetail())
                    , charSize, null, "LEFT");
            setPropertyAndValue(xSpreadsheet, 5, 4, String.valueOf(summingUp.getValueSumCostRetail())
                    , charSize, null, "LEFT");
            setPropertyAndValue(xSpreadsheet, 6, 4, String.valueOf(summingUp.getWeight())
                    , charSize, null, "LEFT");

            // spravka 2
            setPropertyAndValue(xSpreadsheet, 0, 6,
                    "Справка об итогах по ТТН № " + saleDocumentBase.getDocumentNumber()
                            + " (c учетом округления)", charSize, null, "LEFT");
            setPropertyAndValue(xSpreadsheet, 0, 7, "Грузополучатель: "
                            + getDataAsString("RECIPIENT_NAME") + " " + getDataAsString("RECIPIENT_ADDRESS")
                    , charSize, null, "LEFT");
            setPropertyAndValue(xSpreadsheet, 0, 9, saleDocumentBase.getDocumentNumber()
                    , charSize, null, "LEFT");
            setPropertyAndValue(xSpreadsheet, 1, 9, String.valueOf((double) getDataAsObject("CARGO_SPACE"))
                    , charSize, null, "LEFT");
            setPropertyAndValue(xSpreadsheet, 2, 9, String.valueOf(summingUp.getAmount())
                    , charSize, null, "LEFT");
            setPropertyAndValue(xSpreadsheet, 3, 9, String.valueOf(summingUp.getValueSumCost()),
                    charSize, null, "LEFT");
            setPropertyAndValue(xSpreadsheet, 4, 9, String.valueOf(summingUp.getValueSumVatRetail())
                    , charSize, null, "LEFT");
            setPropertyAndValue(xSpreadsheet, 5, 9, String.valueOf(summingUp.getValueSumCostRetail())
                    , charSize, null, "LEFT");
            setPropertyAndValue(xSpreadsheet, 6, 9, String.valueOf(summingUp.getWeight())
                    , charSize, null, "LEFT");
            setPropertyAndValue(xSpreadsheet, 1, 10, String.valueOf((double) getDataAsObject("CARGO_SPACE"))
                    , charSize, null, "LEFT");
            setPropertyAndValue(xSpreadsheet, 2, 10, String.valueOf(summingUp.getAmount())
                    , charSize, null, "LEFT");
            setPropertyAndValue(xSpreadsheet, 3, 10, String.valueOf(summingUp.getValueSumCost()),
                    charSize, null, "LEFT");
            setPropertyAndValue(xSpreadsheet, 4, 10, String.valueOf(summingUp.getValueSumVatRetail())
                    , charSize, null, "LEFT");
            setPropertyAndValue(xSpreadsheet, 5, 10, String.valueOf(summingUp.getValueSumCostRetail())
                    , charSize, null, "LEFT");
            setPropertyAndValue(xSpreadsheet, 6, 10, String.valueOf(summingUp.getWeight())
                    , charSize, null, "LEFT");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
}
