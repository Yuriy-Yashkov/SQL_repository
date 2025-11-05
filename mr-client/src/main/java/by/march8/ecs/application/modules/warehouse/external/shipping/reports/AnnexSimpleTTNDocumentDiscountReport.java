package by.march8.ecs.application.modules.warehouse.external.shipping.reports;

import by.gomel.freedev.ucframework.uccore.report.enums.FontStyle;
import by.gomel.freedev.ucframework.uccore.report.model.ReportProperties;
import by.march8.ecs.application.modules.warehouse.external.shipping.model.SaleDocumentReport;
import by.march8.ecs.application.modules.warehouse.external.shipping.model.TotalSummingUp;
import by.march8.entities.warehouse.PriceListValue;
import by.march8.entities.warehouse.SaleDocumentDetailItemReport;
import com.sun.star.container.XNamed;
import com.sun.star.lang.XComponent;
import com.sun.star.sheet.XSpreadsheet;
import com.sun.star.sheet.XSpreadsheetDocument;
import com.sun.star.sheet.XSpreadsheets;
import com.sun.star.table.XCell;
import com.sun.star.uno.UnoRuntime;

/**
 * @author Andy 05.07.2016.
 */
public class AnnexSimpleTTNDocumentDiscountReport extends AbstractInvoiceReport {


    public AnnexSimpleTTNDocumentDiscountReport(SaleDocumentReport report) {
        super(report);
        documentReport = report;
        create();
    }

    @Override
    ReportProperties prepareProperties(final ReportProperties properties) {
        // Устанавливаем имя файла шаблона
        properties.setBlankName("annex_invoice_simple_" + saleDocumentBase.getDocumentNumber());
        properties.getTemplate().setTemplateName("ПриложениеТТН.ots");
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

            String articleCode = "";
            int lineStart;

            //DateFormat df = new SimpleDateFormat("ddMMyyyyHHmmss");
            xNamed.setName("Приложение к ТТН №" + saleDocumentBase.getDocumentNumber() + "    ");
//          + " ("+ df.format(saleDocumentBase.getDocumentSaleDate().getTime()) +")");

            // ----------------Заполнение шапки документа
            XCell xCell = xSpreadsheet.getCellByPosition(0, 0);

            xCell.setFormula("Грузоотправитель: " + getDataAsString("SENDER_NAME") + " " + getDataAsString("SENDER_ADDRESS"));
            setCharHeightForCell(xCell, CHAR_SIZE);

            xCell = xSpreadsheet.getCellByPosition(0, 1);
            xCell.setFormula("Грузополучатель: " + getDataAsString("RECIPIENT_NAME") + " " + getDataAsString("RECIPIENT_ADDRESS"));
            setCharHeightForCell(xCell, CHAR_SIZE);

            xCell = xSpreadsheet.getCellByPosition(0, 2);
            xCell.setFormula(SaleDocumentReport.getContractDescription(documentReport));
            setCharHeightForCell(xCell, CHAR_SIZE);

            // ----------------Заполнение тела документа
            int row = 8;
            lineStart = row;

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

                        setCharHeightForRangeCell(xSpreadsheet, 0, row, 13, CHAR_SIZE);
                        row++;
                        lineStart = row;
                    }
                    // Маркер артикула
                    articleCode = item.getArticleCode();

                    xCell = xSpreadsheet.getCellByPosition(0, row);
                    xCell.setFormula(articleCode + " ТНВЭД - " + item.getTnvedCode());
                    setCharHeightForCell(xCell, CHAR_SIZE);

                    // ФОрмируем наименование изделия
                    xCell = xSpreadsheet.getCellByPosition(4, row);
                    xCell.setFormula(item.getProductNameString());
                    setCharHeightForCell(xCell, CHAR_SIZE_10, FontStyle.Bold);

                    xCell = xSpreadsheet.getCellByPosition(12, row);
                    xCell.setFormula(item.getItemPriceList());
                    setCharHeightForCell(xCell, CHAR_SIZE);
                    row++;

                    xCell = xSpreadsheet.getCellByPosition(0, row);
                    xCell.setFormula(item.getCertificateDescription());
                    setCharHeightForCell(xCell, CHAR_SIZE);
                    row++;
                }

                xCell = xSpreadsheet.getCellByPosition(0, row);
                xCell.setFormula(item.getItemColor());

                xCell = xSpreadsheet.getCellByPosition(1, row);
                xCell.setFormula(item.getGradeAsString());

                xCell = xSpreadsheet.getCellByPosition(2, row);
                xCell.setFormula("'" + item.getItemSizePrint());

                xCell = xSpreadsheet.getCellByPosition(3, row);
                xCell.setFormula(getDataAsString("UNIT_OF_MEASURE"));

                xCell = xSpreadsheet.getCellByPosition(4, row);
                //xCell.setFormula(hm.get("kol").toString());
                xCell.setFormula(String.valueOf(item.getAmountPrint()));


                PriceListValue retail = item.getPriceListValue();
                if (retail != null) {

                    xCell = xSpreadsheet.getCellByPosition(5, row);
                    //xCell.setFormula(hm.get("cena").toString());
                    xCell.setFormula(String.valueOf(item.getAccountingPrice()));

                    xCell = xSpreadsheet.getCellByPosition(6, row);
                    //xCell.setFormula(hm.get("summa").toString());
                    xCell.setFormula(String.valueOf(retail.getPriceListSumCost()));

                    xCell = xSpreadsheet.getCellByPosition(8, row);
                    //xCell.setFormula(hm.get("summa_nds").toString());
                    xCell.setFormula(String.valueOf(retail.getPriceListSumVat()));

                    xCell = xSpreadsheet.getCellByPosition(9, row);
                    //xCell.setFormula(hm.get("itogo").toString());
                    xCell.setFormula(String.valueOf(retail.getPriceListSumCostAndVat()));
                }

                xCell = xSpreadsheet.getCellByPosition(7, row);
                //xCell.setFormula(dis.get("nds").toString());
                xCell.setFormula(item.getValueVATCode());

                // Грузоместа
                xCell = xSpreadsheet.getCellByPosition(10, row);
                //xCell.setFormula(hm.get("kkr").toString());
                xCell.setFormula(String.valueOf(item.getAmount()));

                xCell = xSpreadsheet.getCellByPosition(11, row);
                //xCell.setFormula(hm.get("massa").toString());
                xCell.setValue(item.getWeight());

                xCell = xSpreadsheet.getCellByPosition(12, row);
                xCell.setFormula(item.getEanCode().trim() + "\nЦена произв.: " + String.valueOf(item.getAccountingPrice()) + "\nопт. надбавка:0%\nопт. скидка: 0%");

                setCharHeightForRangeCell(xSpreadsheet, 0, row, 13, 6.5f);
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

            setCharHeightForRangeCell(xSpreadsheet, 0, row, 13, 6.5f);
            row++;

            //*****************************************************************************
            // Итого по накладной
            //*****************************************************************************

            xCell = xSpreadsheet.getCellByPosition(0, row);
            xCell.setFormula("ИТОГО по накладной");

            TotalSummingUp summingUp = documentReport.getSummingUp();

            // Итого количество
            xCell = xSpreadsheet.getCellByPosition(4, row);
            xCell.setValue(summingUp.getAmount());

            // Сумма
            xCell = xSpreadsheet.getCellByPosition(6, row);
            xCell.setValue(summingUp.getPriceListSumCost());

            // Сумма НДС
            xCell = xSpreadsheet.getCellByPosition(8, row);
            xCell.setValue(summingUp.getPriceListSumVat());

            xCell = xSpreadsheet.getCellByPosition(9, row);
            xCell.setValue(summingUp.getPriceListSumCostAndVat());

            xCell = xSpreadsheet.getCellByPosition(10, row);
            xCell.setValue((double) getDataAsObject("CARGO_SPACE"));

            xCell = xSpreadsheet.getCellByPosition(11, row);
            xCell.setValue(summingUp.getWeight());
            setCharHeightForRangeCell(xSpreadsheet, 0, row, 13, 6.5f);

            row++;

            xCell = xSpreadsheet.getCellByPosition(0, row);
            xCell.setFormula("ИТОГО c учетом скидки " + saleDocumentBase.getDiscountValue() + "%");

            xCell = xSpreadsheet.getCellByPosition(6, row);
            xCell.setValue(summingUp.getValueSumCost());

            xCell = xSpreadsheet.getCellByPosition(8, row);
            xCell.setValue(summingUp.getValueSumVat());

            xCell = xSpreadsheet.getCellByPosition(9, row);
            xCell.setValue((summingUp.getValueSumCostAndVat()));
            setCharHeightForRangeCell(xSpreadsheet, 0, row, 13, 6.5f);


            row++;
            xCell = xSpreadsheet.getCellByPosition(0, row);
            xCell.setFormula("Размер скидки по документу ");

            xCell = xSpreadsheet.getCellByPosition(6, row);
            xCell.setValue(summingUp.getPriceListSumCost() - summingUp.getValueSumCost());

            xCell = xSpreadsheet.getCellByPosition(8, row);
            xCell.setValue(summingUp.getPriceListSumVat() - summingUp.getValueSumVat());

            xCell = xSpreadsheet.getCellByPosition(9, row);
            xCell.setValue((summingUp.getPriceListSumCost() - summingUp.getValueSumCost()) +
                    (summingUp.getPriceListSumVat() - summingUp.getValueSumVat()));

            setCharHeightForRangeCell(xSpreadsheet, 0, row, 13, CHAR_SIZE);
            row++;

/*            xCell = xSpreadsheet.getCellByPosition(4, row - 1);
            xCell.setValue(summingUp.getAmount());*/

            xCell = xSpreadsheet.getCellByPosition(0, row);
            xCell.setFormula("Всего отпущено : " + getDataAsString("AMOUNT_STRING"));

            setCharHeightForCell(xCell, CHAR_SIZE);
            row++;

            xCell = xSpreadsheet.getCellByPosition(0, row);
            xCell.setFormula("Всего сумма НДС: " + getDataAsString("SUM_VAT_STRING"));
            setCharHeightForCell(xCell, CHAR_SIZE);
            row++;

            xCell = xSpreadsheet.getCellByPosition(0, row);
            xCell.setFormula("Всего стоимость с НДС: " + getDataAsString("SUM_COST_AND_VAT_STRING"));
            setCharHeightForCell(xCell, CHAR_SIZE);
            row++;

            xCell = xSpreadsheet.getCellByPosition(2, row);
            xCell.setFormula("Сдал_________________________________ ");
            setCharHeightForCell(xCell, CHAR_SIZE);

            xCell = xSpreadsheet.getCellByPosition(7, row);
            xCell.setFormula("Принял_______________________________ ");
            setCharHeightForCell(xCell, CHAR_SIZE);

        } catch (Exception e) {
            System.out.println("Ошибка формирования отчета ");
            e.printStackTrace();
        }

        return true;
    }
}
