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
 * Приложение к накладной со скидкой на сумму документа
 *
 * @author Andy 25.05.2016.
 */
public class AnnexTTNDocumentDiscountReport extends AbstractInvoiceReport {

    public AnnexTTNDocumentDiscountReport(final SaleDocumentReport report) {
        super(report);
        documentReport = report;
        create();
    }

    @Override
    ReportProperties prepareProperties(final ReportProperties properties) {
        // Устанавливаем имя файла шаблона
        properties.setBlankName("annex_invoice_doc_discount_" + saleDocumentBase.getDocumentNumber());
        properties.getTemplate().setTemplateName("ПриложениеТТН Скидка сумма док.ots");
        return properties;
    }

    @Override
    boolean populateData(final XComponent component) {
        int lineStart;
        try {
            XSpreadsheetDocument xSpreadsheetDocument = UnoRuntime
                    .queryInterface(XSpreadsheetDocument.class, component);
            XSpreadsheets xSpreadsheets = xSpreadsheetDocument.getSheets();
            Object sheet = xSpreadsheets.getByName("Лист1");
            XSpreadsheet xSpreadsheet = UnoRuntime
                    .queryInterface(XSpreadsheet.class, sheet);

            XNamed xNamed = UnoRuntime.queryInterface(XNamed.class,
                    xSpreadsheet);

            //DateFormat df = new SimpleDateFormat("ddMMyyyyHHmmss");
            xNamed.setName("Приложение к ТТН №" + saleDocumentBase.getDocumentNumber() + "    ");
//          + " ("+ df.format(saleDocumentBase.getDocumentSaleDate().getTime()) +")");

            // ----------------Заполнение шапки документа
            XCell xCell = xSpreadsheet.getCellByPosition(0, 0);

            xCell.setFormula("Грузоотправитель: " + getDataAsString("SENDER_NAME") + " " + getDataAsString("SENDER_ADDRESS"));


            xCell = xSpreadsheet.getCellByPosition(0, 1);
            xCell.setFormula("Грузополучатель: " + getDataAsString("RECIPIENT_NAME") + " " + getDataAsString("RECIPIENT_ADDRESS"));
            setCharHeightForCell(xCell, CHAR_SIZE);

            xCell = xSpreadsheet.getCellByPosition(0, 2);
            xCell.setFormula(SaleDocumentReport.getContractDescription(documentReport));
            setCharHeightForCell(xCell, CHAR_SIZE);

            // ----------------Заполнение тела документа
            int row = 7;
            lineStart = row;
            String articleCode = "";

            for (SaleDocumentDetailItemReport item : detailList) {
                if (!articleCode.equals(item.getArticleCode())) {
                    if (row != lineStart) {
                        setBorderForRangeCell(xSpreadsheet, 0, row, 17);

                        xCell = xSpreadsheet.getCellByPosition(0, row);
                        xCell.setFormula("ИТОГО по артикулу");

                        xCell = xSpreadsheet.getCellByPosition(4, row);
                        xCell.setFormula("=SUM(E" + (lineStart + 3) + ":E"
                                + (row) + ")");

                        xCell = xSpreadsheet.getCellByPosition(6, row);
                        xCell.setFormula("=SUM(G" + (lineStart + 3) + ":G"
                                + (row) + ")");

                        xCell = xSpreadsheet.getCellByPosition(8, row);
                        xCell.setFormula("=SUM(I" + (lineStart + 3) + ":I"
                                + (row) + ")");

                        xCell = xSpreadsheet.getCellByPosition(9, row);
                        xCell.setFormula("=SUM(J" + (lineStart + 3) + ":J"
                                + (row) + ")");

                        xCell = xSpreadsheet.getCellByPosition(10, row);
                        xCell.setFormula("=SUM(K" + (lineStart + 3) + ":K"
                                + (row) + ")");

                        xCell = xSpreadsheet.getCellByPosition(11, row);
                        xCell.setFormula("=SUM(L" + (lineStart + 3) + ":L"
                                + row + ")");

                        xCell = xSpreadsheet.getCellByPosition(13, row);
                        xCell.setFormula("=SUM(N" + (lineStart + 3) + ":N"
                                + row + ")");

                        xCell = xSpreadsheet.getCellByPosition(14, row);
                        xCell.setFormula("=SUM(O" + (lineStart + 3) + ":O"
                                + row + ")");

                        xCell = xSpreadsheet.getCellByPosition(15, row);
                        xCell.setFormula("=SUM(P" + (lineStart + 3) + ":P"
                                + row + ")");

                        setCharHeightForRangeCell(xSpreadsheet, 0, row, 17, CHAR_SIZE);

                        row++;
                        lineStart = row;
                    }

                    // Маркер артикула
                    articleCode = item.getArticleCode();

                    xCell = xSpreadsheet.getCellByPosition(0, row);
                    xCell.setFormula(articleCode + " ТНВЭД - " + item.getTnvedCode());
                    setCharHeightForCell(xCell, CHAR_SIZE);

                    xCell = xSpreadsheet.getCellByPosition(4, row);
                    xCell.setFormula(item.getProductNameString());
                    setCharHeightForCell(xCell, CHAR_SIZE_10, FontStyle.Bold);

                    xCell = xSpreadsheet.getCellByPosition(16, row);
                    xCell.setFormula(item.getItemPriceList());
                    setCharHeightForCell(xCell, CHAR_SIZE);
                    row++;

                    xCell = xSpreadsheet.getCellByPosition(0, row);
                    xCell.setFormula(item.getCertificateDescription());
                    setCharHeightForCell(xCell, CHAR_SIZE);

/*                    xCell = xSpreadsheet.getCellByPosition(16, row);
                    xCell.setFormula("Скан-код");
                    setCharHeightForCell(xCell);
                    */
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
                xCell.setFormula(String.valueOf(item.getAmountPrint()));

                xCell = xSpreadsheet.getCellByPosition(5, row);
                xCell.setFormula(String.valueOf(item.getValuePrice()));

                xCell = xSpreadsheet.getCellByPosition(6, row);
                xCell.setFormula(String.valueOf(item.getValueSumCost()));

                xCell = xSpreadsheet.getCellByPosition(7, row);
                //if()
                xCell.setFormula(item.getValueVATCode());

                xCell = xSpreadsheet.getCellByPosition(8, row);
                xCell.setFormula(String.valueOf(item.getValueSumVat()));

                xCell = xSpreadsheet.getCellByPosition(9, row);
                xCell.setFormula(String.valueOf(item.getValueSumCostAndVat()));

                // Грузоместа
                xCell = xSpreadsheet.getCellByPosition(10, row);
                xCell.setFormula(String.valueOf(item.getAmount()));

                xCell = xSpreadsheet.getCellByPosition(11, row);
                xCell.setValue(item.getWeight());

                //********************************************************************************
                // РАСЧЕТ СТОИМОСТИ ПО ПРЕЙСКУРАНТУ
                //********************************************************************************
                PriceListValue retail = item.getPriceListValue();
                if (retail != null) {
                    // Ставка торговой надбавки
                    xCell = xSpreadsheet.getCellByPosition(12, row);
                    xCell.setValue(item.getAccountingPrice());
                    //Торговая надбавка на единицу изделия
                    xCell = xSpreadsheet.getCellByPosition(13, row);
                    xCell.setValue(retail.getPriceListSumCost());
                    //Сумма торговой надбавки
                    xCell = xSpreadsheet.getCellByPosition(14, row);
                    xCell.setValue(retail.getPriceListSumVat());
                    //НДС единицы
                    xCell = xSpreadsheet.getCellByPosition(15, row);
                    xCell.setValue(retail.getPriceListSumCostAndVat());
                }

                xCell = xSpreadsheet.getCellByPosition(16, row);
                xCell.setFormula(item.getEanCode().trim() + "\n Опт.надбавка:0%"
                        + "\n Опт. скидка :" + saleDocumentBase.getDiscountValue() + "%");

                setCharHeightForRangeCell(xSpreadsheet, 0, row, 17, CHAR_SIZE);
                row++;
            }

            xCell = xSpreadsheet.getCellByPosition(0, row);
            xCell.setFormula("ИТОГО по артикулу");

            xCell = xSpreadsheet.getCellByPosition(4, row);
            xCell.setFormula("=SUM(E" + (lineStart + 3) + ":E"
                    + (row) + ")");

            xCell = xSpreadsheet.getCellByPosition(6, row);
            xCell.setFormula("=SUM(G" + (lineStart + 3) + ":G"
                    + (row) + ")");

            xCell = xSpreadsheet.getCellByPosition(8, row);
            xCell.setFormula("=SUM(I" + (lineStart + 3) + ":I"
                    + (row) + ")");

            xCell = xSpreadsheet.getCellByPosition(9, row);
            xCell.setFormula("=SUM(J" + (lineStart + 3) + ":J"
                    + (row) + ")");

            xCell = xSpreadsheet.getCellByPosition(10, row);
            xCell.setFormula("=SUM(K" + (lineStart + 3) + ":K"
                    + (row) + ")");

            xCell = xSpreadsheet.getCellByPosition(11, row);
            xCell.setFormula("=SUM(L" + (lineStart + 3) + ":L"
                    + row + ")");

            xCell = xSpreadsheet.getCellByPosition(13, row);
            xCell.setFormula("=SUM(N" + (lineStart + 3) + ":N"
                    + row + ")");

            xCell = xSpreadsheet.getCellByPosition(14, row);
            xCell.setFormula("=SUM(O" + (lineStart + 3) + ":O"
                    + row + ")");

            xCell = xSpreadsheet.getCellByPosition(15, row);
            xCell.setFormula("=SUM(P" + (lineStart + 3) + ":P"
                    + row + ")");

            setBorderForRangeCell(xSpreadsheet, 0, row, 17);
            setCharHeightForRangeCell(xSpreadsheet, 0, row, 17, CHAR_SIZE);
            row++;

            //*****************************************************************************
            // Итого по накладной
            //*****************************************************************************

            TotalSummingUp summingUp = documentReport.getSummingUp();

            xCell = xSpreadsheet.getCellByPosition(0, row);
            xCell.setFormula("ИТОГО по накладной");

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
            xCell.setValue((double) getDataAsObject("CARGO_SPACE"));

            xCell = xSpreadsheet.getCellByPosition(11, row);
            xCell.setValue(summingUp.getWeight());

            //******************************************************
            // Блок дополнений
            //******************************************************
            xCell = xSpreadsheet.getCellByPosition(13, row);
            xCell.setValue(summingUp.getPriceListSumCost());

            xCell = xSpreadsheet.getCellByPosition(14, row);
            xCell.setValue(summingUp.getPriceListSumVat());

            xCell = xSpreadsheet.getCellByPosition(15, row);
            xCell.setValue(summingUp.getPriceListSumCostAndVat());

            xCell = xSpreadsheet.getCellByPosition(4, row);
            xCell.setValue(summingUp.getAmount());

            setCharHeightForRangeCell(xSpreadsheet, 0, row, 17, CHAR_SIZE);

            row++;

            xCell = xSpreadsheet.getCellByPosition(13, row);
            xCell.setValue(summingUp.getPriceListSumCost() - summingUp.getValueSumCost());

            xCell = xSpreadsheet.getCellByPosition(14, row);
            xCell.setValue(summingUp.getPriceListSumVat() - summingUp.getValueSumVat());

            xCell = xSpreadsheet.getCellByPosition(15, row);
            xCell.setValue((summingUp.getPriceListSumCost() - summingUp.getValueSumCost()) +
                    (summingUp.getPriceListSumVat() - summingUp.getValueSumVat()));

            xCell = xSpreadsheet.getCellByPosition(16, row);
            xCell.setFormula("Опт. Скидка " + saleDocumentBase.getDiscountValue() + "%");


/*            double skid = saleDocumentBase.getDiscountValue() / 100;
            xCell = xSpreadsheet.getCellByPosition(13, row - 1);
            double s = xCell.getValue();
            xCell = xSpreadsheet.getCellByPosition(13, row);
            s = s * skid;
            BigDecimal n = new BigDecimal(s);
            n = n.setScale(2, BigDecimal.ROUND_HALF_UP);
            //n = n.setScale(0, BigDecimal.ROUND_DOWN);
            xCell.setValue(n.doubleValue());
            double st = n.doubleValue();

            xCell = xSpreadsheet.getCellByPosition(14, row - 1);
            s = xCell.getValue();
            xCell = xSpreadsheet.getCellByPosition(14, row);
            s = s * skid;
            n = new BigDecimal(s);
            n = n.setScale(2, BigDecimal.ROUND_HALF_UP);
            xCell.setValue(n.doubleValue());
            double st_n = n.doubleValue();

            xCell = xSpreadsheet.getCellByPosition(15, row - 1);
            s = xCell.getValue();
            xCell = xSpreadsheet.getCellByPosition(15, row);
            s = s * skid;
            n = new BigDecimal(s);
            n = n.setScale(2, BigDecimal.ROUND_HALF_UP);
            // xCell.setValue(n.toBigInteger().intValue());
            xCell.setValue(st_n + st);*/

            setCharHeightForRangeCell(xSpreadsheet, 0, row, 17, CHAR_SIZE);


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

            setPageCount(row);

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return false;
    }
}
