package by.march8.ecs.application.modules.warehouse.internal.displacement.reports;

import by.gomel.freedev.ucframework.uccore.report.model.ReportProperties;
import by.march8.ecs.application.modules.warehouse.external.shipping.model.TotalSummingUp;
import by.march8.ecs.application.modules.warehouse.internal.displacement.model.DisplacementReportData;
import by.march8.entities.warehouse.SaleDocumentDetailItemReport;
import com.sun.star.container.XNamed;
import com.sun.star.lang.XComponent;
import com.sun.star.sheet.XSpreadsheet;
import com.sun.star.sheet.XSpreadsheetDocument;
import com.sun.star.sheet.XSpreadsheets;
import com.sun.star.table.XCell;
import com.sun.star.uno.UnoRuntime;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Andy 10.04.2018 - 9:32.
 */
public class DisplacementInvoiceReport extends AbstractDisplacementReport {

    public DisplacementInvoiceReport(final DisplacementReportData report) {
        super(report);
    }

    @Override
    ReportProperties prepareProperties(final ReportProperties properties) {
        return null;
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

            String articleCode = "";
            int lineStart;

            //xNamed.setName("Приложение к ТТН №" + saleDocumentBase.getDocumentNumber()+" ("+detailMap.get("TIMESTAMP")+")");

            // ----------------Заполнение шапки документа
            XCell xCell = xSpreadsheet.getCellByPosition(0, 0);

            // xCell.setFormula("Грузоотправитель: " + getDataAsString("SENDER_NAME") + " " + getDataAsString("SENDER_ADDRESS"));
            setCharHeightToCell(xCell);

            xCell = xSpreadsheet.getCellByPosition(0, 1);
            //xCell.setFormula("Грузополучатель: " + getDataAsString("RECIPIENT_NAME") + " " + getDataAsString("RECIPIENT_ADDRESS"));
            setCharHeightToCell(xCell);

            xCell = xSpreadsheet.getCellByPosition(0, 2);
            //xCell.setFormula(SaleDocumentShippingReport.getContractDescription(documentReport));
            setCharHeightToCell(xCell);

            // ----------------Заполнение тела документа
            int row = 8;
            lineStart = row;
            List<SaleDocumentDetailItemReport> detailList = new ArrayList<>();

            for (SaleDocumentDetailItemReport item : detailList) {
                if (!articleCode.equals(item.getArticleCode())) {
                    if (row != lineStart) {

                        setBorderForRangeCell(xSpreadsheet, 0, row, 13);

                        xCell = xSpreadsheet.getCellByPosition(0, row);
                        xCell.setFormula("ИТОГО по артикулу");

                        xCell = xSpreadsheet.getCellByPosition(4, row);
                        xCell.setFormula("=SUM(E" + (lineStart + 1) + ":E"
                                + (row) + ")");

                        xCell = xSpreadsheet.getCellByPosition(6, row);
                        xCell.setFormula("=SUM(G" + (lineStart + 1) + ":G"
                                + (row) + ")");

                        xCell = xSpreadsheet.getCellByPosition(8, row);
                        xCell.setFormula("=SUM(I" + (lineStart + 1) + ":I"
                                + (row) + ")");

                        xCell = xSpreadsheet.getCellByPosition(9, row);
                        xCell.setFormula("=SUM(J" + (lineStart + 1) + ":J"
                                + (row) + ")");

                        xCell = xSpreadsheet.getCellByPosition(10, row);
                        xCell.setFormula("=SUM(K" + (lineStart + 1) + ":K"
                                + (row) + ")");

                        xCell = xSpreadsheet.getCellByPosition(11, row);
                        xCell.setFormula("=SUM(L" + (lineStart + 1) + ":L"
                                + row + ")");

                        setCharHeightForRangeCell(xSpreadsheet, 0, row, 13);
                        row++;
                        lineStart = row;
                    }
                    // Маркер артикула
                    articleCode = item.getArticleCode();

                    xCell = xSpreadsheet.getCellByPosition(0, row);
                    xCell.setFormula(articleCode + " ТНВЭД - " + item.getTnvedCode());
                    setCharHeightToCell(xCell);

                    // ФОрмируем наименование изделия
                    xCell = xSpreadsheet.getCellByPosition(4, row);
                    xCell.setFormula(item.getProductNameString());


                    xCell = xSpreadsheet.getCellByPosition(12, row);
                    xCell.setFormula(item.getItemPriceList());
                    setCharHeightToCell(xCell);
                    row++;

                    xCell = xSpreadsheet.getCellByPosition(0, row);
                    xCell.setFormula(item.getCertificateDescription());
                    setCharHeightToCell(xCell);
                    row++;
                }

                xCell = xSpreadsheet.getCellByPosition(0, row);
                xCell.setFormula(item.getItemColor());

                xCell = xSpreadsheet.getCellByPosition(1, row);
                xCell.setFormula(item.getGradeAsString());

                xCell = xSpreadsheet.getCellByPosition(2, row);
                xCell.setFormula("'" + item.getItemSizePrint());

                xCell = xSpreadsheet.getCellByPosition(3, row);
                xCell.setFormula(item.getMeasureUnit());

                xCell = xSpreadsheet.getCellByPosition(4, row);
                //xCell.setFormula(hm.get("kol").toString());
                xCell.setFormula(String.valueOf(item.getAmountPrint()));

                xCell = xSpreadsheet.getCellByPosition(5, row);
                //xCell.setFormula(hm.get("cena").toString());
                xCell.setFormula(String.valueOf(item.getValuePrice()));

                xCell = xSpreadsheet.getCellByPosition(6, row);
                //xCell.setFormula(hm.get("summa").toString());
                xCell.setFormula(String.valueOf(item.getValueSumCost()));

                xCell = xSpreadsheet.getCellByPosition(7, row);
                //xCell.setFormula(dis.get("nds").toString());
                xCell.setFormula(item.getValueVATCode());

                xCell = xSpreadsheet.getCellByPosition(8, row);
                //xCell.setFormula(hm.get("summa_nds").toString());
                xCell.setFormula(String.valueOf(item.getValueSumVat()));

                xCell = xSpreadsheet.getCellByPosition(9, row);
                //xCell.setFormula(hm.get("itogo").toString());
                xCell.setFormula(String.valueOf(item.getValueSumCostAndVat()));

                // Грузоместа
                xCell = xSpreadsheet.getCellByPosition(10, row);
                //xCell.setFormula(hm.get("kkr").toString());
                xCell.setFormula(String.valueOf(item.getAmount()));

                xCell = xSpreadsheet.getCellByPosition(11, row);
                //xCell.setFormula(hm.get("massa").toString());
                xCell.setValue(item.getWeight());

                xCell = xSpreadsheet.getCellByPosition(12, row);
                xCell.setFormula(item.getEanCode().trim());


                setCharHeightForRangeCell(xSpreadsheet, 0, row, 13);
                row++;
            }

            setBorderForRangeCell(xSpreadsheet, 0, row, 13);

            xCell = xSpreadsheet.getCellByPosition(0, row);
            xCell.setFormula("ИТОГО по артикулу");

            xCell = xSpreadsheet.getCellByPosition(4, row);
            xCell.setFormula("=SUM(E" + (lineStart + 1) + ":E" + (row) + ")");

            xCell = xSpreadsheet.getCellByPosition(6, row);
            xCell.setFormula("=SUM(G" + (lineStart + 1) + ":G" + (row) + ")");

            xCell = xSpreadsheet.getCellByPosition(8, row);
            xCell.setFormula("=SUM(I" + (lineStart + 1) + ":I" + (row) + ")");

            xCell = xSpreadsheet.getCellByPosition(9, row);
            xCell.setFormula("=SUM(J" + (lineStart + 1) + ":J" + (row) + ")");

            xCell = xSpreadsheet.getCellByPosition(10, row);
            xCell.setFormula("=SUM(K" + (lineStart + 1) + ":K" + (row) + ")");

            xCell = xSpreadsheet.getCellByPosition(11, row);
            xCell.setFormula("=SUM(L" + (lineStart + 1) + ":L" + row + ")");

            setCharHeightForRangeCell(xSpreadsheet, 0, row, 13);
            row++;

            //*****************************************************************************
            // Итого по накладной
            //*****************************************************************************

            xCell = xSpreadsheet.getCellByPosition(0, row);
            xCell.setFormula("ИТОГО по накладной");

            TotalSummingUp summingUp = new TotalSummingUp();//documentReport.getSummingUp();

            // Итого количество
            xCell = xSpreadsheet.getCellByPosition(4, row);
            xCell.setValue(summingUp.getAmount());
            // Сумма
            xCell = xSpreadsheet.getCellByPosition(6, row);
            xCell.setValue(summingUp.getValueSumCost());

            // Сумма НДС
            xCell = xSpreadsheet.getCellByPosition(8, row);
            xCell.setValue(summingUp.getValueSumVat());

            xCell = xSpreadsheet.getCellByPosition(9, row);
            xCell.setValue(summingUp.getValueSumCostAndVat());

            xCell = xSpreadsheet.getCellByPosition(10, row);
            //xCell.setValue((double) getDataAsObject("CARGO_SPACE"));

            xCell = xSpreadsheet.getCellByPosition(11, row);
            xCell.setValue(summingUp.getWeight());

            setCharHeightForRangeCell(xSpreadsheet, 0, row, 13);
            row++;

            xCell = xSpreadsheet.getCellByPosition(4, row);
            xCell.setValue(summingUp.getAmount());

            xCell = xSpreadsheet.getCellByPosition(0, row);
            //xCell.setFormula("Всего отпущено : " + getDataAsString("AMOUNT_STRING"));

            setCharHeightToCell(xCell);
            row++;

            xCell = xSpreadsheet.getCellByPosition(0, row);
            //xCell.setFormula("Всего сумма НДС: " + getDataAsString("SUM_VAT_STRING"));
            setCharHeightToCell(xCell);
            row++;

            xCell = xSpreadsheet.getCellByPosition(0, row);
            //xCell.setFormula("Всего стоимость с НДС: " + getDataAsString("SUM_COST_AND_VAT_STRING"));
            setCharHeightToCell(xCell);
            row++;

            xCell = xSpreadsheet.getCellByPosition(2, row);
            xCell.setFormula("Сдал_________________________________ ");
            setCharHeightToCell(xCell);

            xCell = xSpreadsheet.getCellByPosition(7, row);
            xCell.setFormula("Принял_______________________________ ");
            setCharHeightToCell(xCell);

            //setPageCountVertical(row);
            //setPageCount();


        } catch (Exception e) {
            System.out.println("Ошибка формирования отчета ");
            e.printStackTrace();
        }

        return true;
    }
}
