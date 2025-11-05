package by.march8.ecs.application.modules.warehouse.external.shipping.reports;

import by.gomel.freedev.ucframework.uccore.report.enums.FontStyle;
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

/**
 * @author Andy 25.05.2016.
 */
public class AnnexTTNExportReport extends AbstractInvoiceReport {

    public AnnexTTNExportReport(final SaleDocumentReport report) {
        super(report);
        documentReport = report;
        create();
    }

    @Override
    ReportProperties prepareProperties(final ReportProperties properties) {
        // Устанавливаем имя файла шаблона
        properties.setBlankName("annex_invoice_Export_" + saleDocumentBase.getDocumentNumber());
        properties.getTemplate().setTemplateName("ПриложениеТТНРос.ots");
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

/*            xCell = xSpreadsheet.getCellByPosition(16, 0);
            xCell.setFormula("Скидка клиенту :" + saleDocumentBase.getDiscountValue());
            xCell = xSpreadsheet.getCellByPosition(16, 1);
            xCell.setFormula("Курс расчета валюты :" + saleDocumentBase.getCurrencyRateFixed());
            xCell = xSpreadsheet.getCellByPosition(16, 2);
            xCell.setFormula("Курс расчета рублей :" + saleDocumentBase.getCurrencyRateSale());*/

            // изменения в шапке таблицы
            int curr = saleDocumentBase.getCurrencyId();
            String currencyType = "Бел. руб.";
            switch (curr) {

                case 2:
                    currencyType = "Рос. руб.";
                    break;
                case 3:
                    currencyType = "Долл.США";
                    break;
                case 4:
                    currencyType = "Евро";
                    break;
                case 5:
                    currencyType = "Гривна";
                    break;
            }

            xCell = xSpreadsheet.getCellByPosition(5, 3);
            xCell.setFormula("Цена, " + currencyType);
            xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime
                    .queryInterface(com.sun.star.beans.XPropertySet.class,
                            xCell);
            xPropSet.setPropertyValue("CharHeight", 8);
            xCell = xSpreadsheet.getCellByPosition(6, 3);
            xCell.setFormula("Стоимость, " + currencyType);
            xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime
                    .queryInterface(com.sun.star.beans.XPropertySet.class,
                            xCell);
            xPropSet.setPropertyValue("CharHeight", 8);
            xCell = xSpreadsheet.getCellByPosition(8, 3);
            xCell.setFormula("Сумма НДС, " + currencyType);
            xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime
                    .queryInterface(com.sun.star.beans.XPropertySet.class,
                            xCell);
            xPropSet.setPropertyValue("CharHeight", 8);
            xCell = xSpreadsheet.getCellByPosition(9, 3);
            xCell.setFormula("Стоимость с НДС, "
                    + currencyType);
            xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime
                    .queryInterface(com.sun.star.beans.XPropertySet.class,
                            xCell);
            xPropSet.setPropertyValue("CharHeight", 8);


            // ----------------Заполнение тела документа
            int row = 7;
            lineStart = row;
            String articleCode = "";

            for (SaleDocumentDetailItemReport item : detailList) {
                if (!articleCode.equals(item.getArticleCode())) {
                    if (row != lineStart) {
                        setBorderForRangeCell(xSpreadsheet, 0, row, 16);

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

                        setCharHeightForRangeCell(xSpreadsheet, 0, row, 16, CHAR_SIZE);

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

                    xCell = xSpreadsheet.getCellByPosition(15, row);
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
                xCell.setFormula(item.getMeasureUnit());

                xCell = xSpreadsheet.getCellByPosition(4, row);
                xCell.setFormula(String.valueOf(item.getAmountPrint()));

                xCell = xSpreadsheet.getCellByPosition(5, row);
                xCell.setFormula(String.valueOf(item.getValuePriceCurrency()));

                xCell = xSpreadsheet.getCellByPosition(6, row);
                xCell.setFormula(String.valueOf(item.getValueSumCostCurrency()));

                xCell = xSpreadsheet.getCellByPosition(7, row);
                xCell.setFormula(String.valueOf(item.getValueVAT()));

                xCell = xSpreadsheet.getCellByPosition(8, row);
                xCell.setFormula(String.valueOf(item.getValueSumVatCurrency()));

                xCell = xSpreadsheet.getCellByPosition(9, row);
                xCell.setFormula(String.valueOf(item.getValueSumCostAndVatCurrency()));

                // Грузоместа
                xCell = xSpreadsheet.getCellByPosition(10, row);
                xCell.setFormula(String.valueOf(item.getAmount()));

                xCell = xSpreadsheet.getCellByPosition(11, row);
                xCell.setValue(item.getWeight());

/*                xCell = xSpreadsheet.getCellByPosition(16, row);
                xCell.setValue(item.getAccountingPrice());*/

                //********************************************************************************
                // БЛОК ЦЕНЫ В БЕЛ РУБ
                //********************************************************************************
                // Ставка торговой надбавки
                xCell = xSpreadsheet.getCellByPosition(12, row);
                xCell.setValue(item.getValuePrice());
                //Торговая надбавка на единицу изделия
                xCell = xSpreadsheet.getCellByPosition(13, row);
                xCell.setValue(item.getValueSumCost());
                //Сумма торговой надбавки
                xCell = xSpreadsheet.getCellByPosition(14, row);
                xCell.setValue(item.getValueSumVat());
                //НДС единицы
                xCell = xSpreadsheet.getCellByPosition(15, row);
                xCell.setValue(item.getValueSumCostAndVat());


                setCharHeightForRangeCell(xSpreadsheet, 0, row, 16, CHAR_SIZE);
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

            setBorderForRangeCell(xSpreadsheet, 0, row, 16);
            setCharHeightForRangeCell(xSpreadsheet, 0, row, 16, CHAR_SIZE);
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
            xCell.setValue(summingUp.getValueSumCostCurrency());

            // Сумма НДС
            xCell = xSpreadsheet.getCellByPosition(8, row);
            xCell.setValue(summingUp.getValueSumVatCurrency());

            xCell = xSpreadsheet.getCellByPosition(9, row);
            xCell.setValue(summingUp.getValueSumCostAndVatCurrency());

            xCell = xSpreadsheet.getCellByPosition(10, row);
            xCell.setValue((double) getDataAsObject("CARGO_SPACE"));

            xCell = xSpreadsheet.getCellByPosition(11, row);
            xCell.setValue(summingUp.getWeight());

            //******************************************************
            // Блок дополнений
            //******************************************************
            xCell = xSpreadsheet.getCellByPosition(13, row);
            xCell.setValue(summingUp.getValueSumCost());

            xCell = xSpreadsheet.getCellByPosition(14, row);
            xCell.setValue(summingUp.getValueSumVat());

            xCell = xSpreadsheet.getCellByPosition(15, row);
            xCell.setValue(summingUp.getValueSumCostAndVat());


            setCharHeightForRangeCell(xSpreadsheet, 0, row, 16, CHAR_SIZE);
            row++;

            xCell = xSpreadsheet.getCellByPosition(4, row - 1);
            xCell.setValue(summingUp.getAmount());

            xCell = xSpreadsheet.getCellByPosition(0, row);
            xCell.setFormula("Всего отпущено : " + getDataAsString("AMOUNT_STRING"));
            setCharHeightForCell(xCell, CHAR_SIZE);
            row++;

            xCell = xSpreadsheet.getCellByPosition(0, row);
            xCell.setFormula("Всего сумма НДС: " + getDataAsString("SUM_VAT_CURRENCY_STRING"));
            setCharHeightForCell(xCell, CHAR_SIZE);
            row++;

            xCell = xSpreadsheet.getCellByPosition(0, row);
            xCell.setFormula("Всего стоимость с НДС: " + getDataAsString("SUM_COST_AND_VAT_CURRENCY_STRING"));
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
