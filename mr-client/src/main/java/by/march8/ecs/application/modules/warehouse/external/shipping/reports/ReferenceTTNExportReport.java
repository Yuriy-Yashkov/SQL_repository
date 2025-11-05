package by.march8.ecs.application.modules.warehouse.external.shipping.reports;

import by.gomel.freedev.ucframework.uccore.report.model.ReportProperties;
import by.march8.ecs.application.modules.warehouse.external.shipping.model.SaleDocumentReport;
import by.march8.ecs.application.modules.warehouse.external.shipping.model.TotalSummingUp;
import by.march8.entities.warehouse.ContractorBank;
import by.march8.entities.warehouse.SaleDocumentDetailItemReport;
import com.sun.star.beans.XPropertySet;
import com.sun.star.lang.XComponent;
import com.sun.star.sheet.XSpreadsheet;
import com.sun.star.sheet.XSpreadsheetDocument;
import com.sun.star.sheet.XSpreadsheets;
import com.sun.star.table.XCell;
import com.sun.star.uno.UnoRuntime;
import com.sun.star.util.XNumberFormats;
import com.sun.star.util.XNumberFormatsSupplier;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author Andy 27.05.2016.
 */
public class ReferenceTTNExportReport extends AbstractInvoiceReport {

    private int numberFormat = 0;

    public ReferenceTTNExportReport(final SaleDocumentReport report) {
        super(report);
        documentReport = report;
        create();
    }

    @Override
    ReportProperties prepareProperties(final ReportProperties properties) {
        // Устанавливаем имя файла шаблона
        properties.setBlankName("reference_export_" + saleDocumentBase.getDocumentNumber());
        properties.getTemplate().setTemplateName("Справки для россии.ots");
        return properties;
    }

    private void createNewNumberFormat() {
        // add a new custom number format and get the new key

    }

    @Override
    boolean populateData(final XComponent component) {


        // HashMap<String, Object> hm = new HashMap();
        // HashMap dis = new HashMap();
        int lineStart;
        float charSize = (float) 9;
        // BigDecimal n;

        //double mass, summar, summa_ndsr, summa_allr;
        try {
            XSpreadsheetDocument xSpreadsheetDocument = UnoRuntime
                    .queryInterface(XSpreadsheetDocument.class, component);
            XSpreadsheets xSpreadsheets = xSpreadsheetDocument.getSheets();


            int nNewNumberFormat = 0;
            XNumberFormatsSupplier aNumFmtSupp = UnoRuntime.queryInterface(
                    XNumberFormatsSupplier.class, xSpreadsheetDocument);

            if (aNumFmtSupp != null) {
                XNumberFormats aFormats = aNumFmtSupp.getNumberFormats();
                com.sun.star.lang.Locale aLocale = new com.sun.star.lang.Locale("de", "DE", "de");

                String aFormatStr = aFormats.generateFormat(nNewNumberFormat, aLocale, true, true, (short) 3, (short) 1);
                numberFormat = aFormats.addNew(aFormatStr, aLocale);
            }

            com.sun.star.table.BorderLine aLine = new com.sun.star.table.BorderLine();
            aLine.Color = 0x000000;
            aLine.InnerLineWidth = 0;
            aLine.LineDistance = 0;
            aLine.OuterLineWidth = 25;
            com.sun.star.table.TableBorder aBorder = new com.sun.star.table.TableBorder();
            aBorder.BottomLine = aLine;
            aBorder.LeftLine = aLine;
            aBorder.RightLine = aLine;
            aBorder.TopLine = aLine;
            aBorder.IsBottomLineValid = true;
            aBorder.IsLeftLineValid = true;
            aBorder.IsRightLineValid = true;
            aBorder.IsTopLineValid = true;

            //*************************************************************************************
            // Спецификация 1й лист
            //*************************************************************************************
            Object sheet = xSpreadsheets.getByName("Спецификация");
            XSpreadsheet xSpreadsheet = UnoRuntime
                    .queryInterface(XSpreadsheet.class, sheet);
            // ----------------Заполнение шапки документа
            setPropertyAndValue(
                    xSpreadsheet,
                    0,
                    0,
                    "Спецификация № ________________ к договору № _______________________",
                    charSize, null, "CENTER");
            //
            setPropertyAndValue(xSpreadsheet, 0, 1,
                    "от «____» __________ 202__ года", charSize, null, "CENTER");

            setPropertyAndValue(xSpreadsheet, 0, 2,
                    "Поставщик: " + getDataAsString("SENDER_NAME") + " " + getDataAsString("SENDER_ADDRESS"), charSize, null,
                    "LEFT");
            setPropertyAndValue(xSpreadsheet, 0, 3,
                    "Покупатель: " + getDataAsString("RECIPIENT_NAME") + " " + getDataAsString("RECIPIENT_ADDRESS"), charSize, null,
                    "LEFT");
            setPropertyAndValue(xSpreadsheet, 0, 4,
                    "Т.Т.Н. № : " + saleDocumentBase.getDocumentNumber(), charSize, null, "LEFT");
            // ----------------Заполнение тела документа
            int row = 8;

            //for (SaleDocumentDetailItemReport item : getDetailItemSetByArticleCodeAndCurrencyPrice()) {
            for (SaleDocumentDetailItemReport item : getDetailItemSetByModelArticlePrice(detailList)) {

                setPropertyAndValue(xSpreadsheet, 0, row - 1, item.getItemName(), charSize, aBorder, "LEFT");
                setPropertyAndValue(xSpreadsheet, 1, row - 1, item.getArticleNumber(), charSize, aBorder, "LEFT");
                setPropertyAndValue(xSpreadsheet, 2, row - 1, item.getModelNumber(), charSize, aBorder, "LEFT");
                setPropertyAndValue(xSpreadsheet, 3, row - 1, String.valueOf(item.getValuePriceCurrency()), charSize, aBorder, "RIGHT");
                setPropertyAndValue(xSpreadsheet, 4, row - 1, "", charSize,
                        aBorder, "LEFT");
                row++;
            }
            row++;
            row++;
            row++;
            row++;
            setPropertyAndValue(
                    xSpreadsheet,
                    0,
                    row,
                    "Поставщик:                                                                                                 Покупатель:_________________________",
                    charSize, null, "LEFT");
            row++;
            setPropertyAndValue(xSpreadsheet, 0, row,
                    "Руководитель: _______________________", charSize, null,
                    "LEFT");
            row++;
            row++;
            row++;
            row++;
            setPropertyAndValue(xSpreadsheet, 0, row, "Виза:", charSize, null,
                    "LEFT");
            row++;
            setPropertyAndValue(xSpreadsheet, 0, row,
                    "Специалист по продажам: ______________", charSize, null,
                    "LEFT");

            //*************************************************************************************
            // Спецификация 2й лист
            //*************************************************************************************

            sheet = xSpreadsheets.getByName("Спецификация1");
            xSpreadsheet = UnoRuntime.queryInterface(
                    XSpreadsheet.class, sheet);
            // ----------------Заполнение шапки документа
            setPropertyAndValue(
                    xSpreadsheet,
                    0,
                    0,
                    "Спецификация № ________________ к договору № _______________________",
                    charSize, null, "CENTER");
            //
            setPropertyAndValue(xSpreadsheet, 0, 1,
                    "от «____» __________ 202__ года", charSize, null, "CENTER");

            setPropertyAndValue(xSpreadsheet, 0, 2,
                    "Поставщик: " + getDataAsString("SENDER_NAME") + " " + getDataAsString("SENDER_ADDRESS"), charSize, null,
                    "LEFT");
            setPropertyAndValue(xSpreadsheet, 0, 3,
                    "Покупатель: " + getDataAsString("RECIPIENT_NAME") + " " + getDataAsString("RECIPIENT_ADDRESS"), charSize, null,
                    "LEFT");
            setPropertyAndValue(xSpreadsheet, 0, 4,
                    "Т.Т.Н. № : " + saleDocumentBase.getDocumentNumber(), charSize, null, "LEFT");
            // ----------------Заполнение тела документа
            row = 8;

            //for (SaleDocumentDetailItemReport item : getDetailItemSetByArticleCodeAndCurrencyPrice()) {
            for (SaleDocumentDetailItemReport item : getDetailItemSetByModelArticlePrice(detailList)) {

                setPropertyAndValue(xSpreadsheet, 0, row - 1, item.getItemName(), charSize, aBorder, "LEFT");
                setPropertyAndValue(xSpreadsheet, 1, row - 1, item.getArticleNumber(), charSize, aBorder, "LEFT");
                setPropertyAndValue(xSpreadsheet, 2, row - 1, item.getModelNumber(), charSize, aBorder, "LEFT");
                setPropertyAndValue(xSpreadsheet, 3, row - 1, String.valueOf(item.getValuePriceCurrency()), charSize, aBorder, "RIGHT");
                setPropertyAndValue(xSpreadsheet, 4, row - 1, "", charSize,
                        aBorder, "LEFT");
                row++;
            }
            row++;
            row++;
            row++;
            row++;
            setPropertyAndValue(
                    xSpreadsheet,
                    0,
                    row,
                    "Поставщик:                                                                                                 Покупатель:_________________________",
                    charSize, null, "LEFT");
            row++;
            setPropertyAndValue(xSpreadsheet, 0, row,
                    "Руководитель: ______________________________", charSize,
                    null, "LEFT");
            row++;
            row++;
            row++;
            row++;
            setPropertyAndValue(xSpreadsheet, 0, row, "Виза:", charSize, null,
                    "LEFT");
            row++;
            setPropertyAndValue(xSpreadsheet, 0, row,
                    "Специалист по продажам: _____________________", charSize,
                    null, "LEFT");

            //*************************************************************************************
            // Справки для ВЭС 1я таблица
            //*************************************************************************************
            //
            lineStart = 4;
            sheet = xSpreadsheets.getByName("Справки для ВЭС");
            xSpreadsheet = UnoRuntime.queryInterface(
                    XSpreadsheet.class, sheet);

            setPropertyAndValue(xSpreadsheet, 0, 0, "Справки для ВЭС", charSize, null, "CENTER");
            setPropertyAndValue(xSpreadsheet, 0, 1,
                    "Отгрузочная спецификация к накладной № "
                            + saleDocumentBase.getDocumentNumber() + " (В РУБЛЯХ РОССИИ)", charSize, null, "CENTER");
            setPropertyAndValue(xSpreadsheet, 0, 2,
                    "Поставщик: " + getDataAsString("SENDER_NAME") + " " + getDataAsString("SENDER_ADDRESS"), charSize, null, "LEFT");
            setPropertyAndValue(xSpreadsheet, 0, 3,
                    "Получатель: " + getDataAsString("RECIPIENT_NAME") + " " + getDataAsString("RECIPIENT_ADDRESS"), charSize, null, "LEFT");
            // Таблица
            setPropertyAndValue(xSpreadsheet, 0, lineStart, "ТН ВЭД СНГ", charSize, aBorder, "LEFT");
            setPropertyAndValue(xSpreadsheet, 1, lineStart, "Наименование", charSize, aBorder, "LEFT");
            setPropertyAndValue(xSpreadsheet, 2, lineStart, "Кол-во", charSize, aBorder, "LEFT");
            setPropertyAndValue(xSpreadsheet, 3, lineStart, "Единицы", charSize, aBorder, "LEFT");
            setPropertyAndValue(xSpreadsheet, 4, lineStart, "Сумма", charSize, aBorder, "LEFT");
            setPropertyAndValue(xSpreadsheet, 5, lineStart, "Вес (кг.)", charSize, aBorder, "LEFT");

            lineStart++;

            XCell xCell;

            for (SaleDocumentDetailItemReport item : getDetailItemSetByTnvdAndName(detailList)) {

                setPropertyAndValue(xSpreadsheet, 0, lineStart, item.getTnvedCode(), charSize, aBorder, "LEFT");
                setPropertyAndValue(xSpreadsheet, 1, lineStart, item.getItemName(), charSize, aBorder, "LEFT");
                setPropertyAndValue(xSpreadsheet, 2, lineStart, String.valueOf(item.getAmount()), charSize, aBorder,
                        "LEFT");
                setPropertyAndValue(xSpreadsheet, 3, lineStart, String.valueOf(item.getAmountPrint()), charSize, aBorder,
                        "LEFT");
                setPropertyAndValue(xSpreadsheet, 4,
                        lineStart, String.valueOf(item.getValueSumCostAndVatCurrency()), charSize, aBorder, "RIGHT");

                setPropertyAndValue(xSpreadsheet, 5, lineStart, String.valueOf(item.getWeight()), charSize,
                        aBorder, "RIGHT");

                xCell = xSpreadsheet.getCellByPosition(5, lineStart);

                xPropSet = UnoRuntime
                        .queryInterface(XPropertySet.class,
                                xCell);

                xPropSet.setPropertyValue("NumberFormat", numberFormat);

                lineStart++;
            }
            setPropertyAndValue(xSpreadsheet, 1, lineStart, "ИТОГО:", charSize, null, "LEFT");
            setPropertyAndValue(xSpreadsheet, 2, lineStart, "=SUM(C5:C"
                    + String.valueOf(lineStart) + ")", charSize, aBorder, "LEFT");
            setPropertyAndValue(xSpreadsheet, 3, lineStart, "=SUM(D5:D"
                    + String.valueOf(lineStart) + ")", charSize, aBorder, "LEFT");

            TotalSummingUp summingUp = documentReport.getSummingUp();

            setPropertyAndValue(xSpreadsheet, 4, lineStart,
                    String.valueOf(summingUp.getValueSumCostAndVatCurrency()), charSize, aBorder, "RIGHT");


            xCell = xSpreadsheet.getCellByPosition(4, lineStart);
            xPropSet = UnoRuntime
                    .queryInterface(XPropertySet.class,
                            xCell);

            xPropSet.setPropertyValue("NumberFormat", 2);

            setPropertyAndValue(xSpreadsheet, 5, lineStart, String.valueOf(summingUp.getWeight()), charSize, aBorder, "RIGHT");
            xCell = xSpreadsheet.getCellByPosition(5, lineStart);
            xPropSet = UnoRuntime
                    .queryInterface(XPropertySet.class,
                            xCell);

            xPropSet.setPropertyValue("NumberFormat", numberFormat);

            //*************************************************************************************
            // Справки для ВЭС 2я таблица
            //*************************************************************************************
            lineStart++;
            lineStart++;

            setPropertyAndValue(xSpreadsheet, 0, lineStart, "Наименование", charSize, aBorder, "LEFT");
            setPropertyAndValue(xSpreadsheet, 1, lineStart, "Модель", charSize, aBorder, "LEFT");
            setPropertyAndValue(xSpreadsheet, 2, lineStart, "Кол-во", charSize, aBorder, "LEFT");
            setPropertyAndValue(xSpreadsheet, 3, lineStart, "Вес (кг.)", charSize, aBorder, "LEFT");
            setPropertyAndValue(xSpreadsheet, 4, lineStart, "Единицы", charSize, aBorder, "LEFT");
            setPropertyAndValue(xSpreadsheet, 5, lineStart, "Сумма без НДС", charSize, aBorder, "LEFT");
            setPropertyAndValue(xSpreadsheet, 6, lineStart, "Сумма НДС", charSize, aBorder, "LEFT");

            lineStart++;
            int lineEnd = lineStart;


            // newHash = sdb.getRazrezArtALL(dis.get("ttn").toString());
            for (SaleDocumentDetailItemReport item : getDetailItemSetByArticleCode(detailList)) {

                setPropertyAndValue(xSpreadsheet, 0, lineStart, item.getItemName(), charSize, aBorder, "LEFT");
                setPropertyAndValue(xSpreadsheet, 1, lineStart, item.getModelNumber(), charSize, aBorder, "LEFT");
                setPropertyAndValue(xSpreadsheet, 2, lineStart, String.valueOf(item.getAmount()), charSize, aBorder, "LEFT");
                setPropertyAndValue(xSpreadsheet, 3, lineStart, String.valueOf(item.getWeight()), charSize, aBorder, "RIGHT");

                xCell = xSpreadsheet.getCellByPosition(3, lineStart);
                xPropSet = UnoRuntime
                        .queryInterface(XPropertySet.class,
                                xCell);
                xPropSet.setPropertyValue("NumberFormat", numberFormat);

                setPropertyAndValue(xSpreadsheet, 4, lineStart, String.valueOf(item.getAmountPrint()), charSize, aBorder, "LEFT");
                setPropertyAndValue(xSpreadsheet, 5, lineStart,
                        String.valueOf(item.getValueSumCostCurrency()), charSize, aBorder, "RIGHT");

                setPropertyAndValue(xSpreadsheet, 6, lineStart,
                        String.valueOf(item.getValueSumVatCurrency()), charSize, aBorder, "RIGHT");
                lineStart++;
            }
            setPropertyAndValue(xSpreadsheet, 1, lineStart, "ИТОГО:", charSize, null, "RIGHT");
            setPropertyAndValue(xSpreadsheet, 2, lineStart,
                    "=SUM(C" + String.valueOf(lineEnd) + ":C"
                            + String.valueOf(lineStart) + ")", charSize, aBorder, "LEFT");

            setPropertyAndValue(xSpreadsheet, 3, lineStart, String.valueOf(summingUp.getWeight())
                    , charSize, aBorder, "RIGHT");

            xCell = xSpreadsheet.getCellByPosition(3, lineStart);
            xPropSet = UnoRuntime
                    .queryInterface(XPropertySet.class,
                            xCell);

            xPropSet.setPropertyValue("NumberFormat", numberFormat);


            setPropertyAndValue(xSpreadsheet, 4, lineStart,
                    "=SUM(E" + String.valueOf(lineEnd) + ":E"
                            + String.valueOf(lineStart) + ")", charSize, aBorder, "RIGHT");

            setPropertyAndValue(xSpreadsheet, 5, lineStart,
                    String.valueOf(summingUp.getValueSumCostCurrency()), charSize, aBorder, "RIGHT");

            setPropertyAndValue(xSpreadsheet, 6, lineStart, String.valueOf(summingUp.getValueSumVatCurrency()), charSize, aBorder, "RIGHT");

            //*************************************************************************************
            // Счет фактура
            //*************************************************************************************

            sheet = xSpreadsheets.getByName("Счет-фактура");
            xSpreadsheet = UnoRuntime.queryInterface(
                    XSpreadsheet.class, sheet);

            Object o = getDataAsObject("SENDER_BANK");
            ContractorBank senderBank = (ContractorBank) getDataAsObject("SENDER_BANK");
            ContractorBank recipientBank = (ContractorBank) getDataAsObject("RECIPIENT_BANK");

            if (senderBank == null) {
                senderBank = new ContractorBank();
            }

            if (recipientBank == null) {
                recipientBank = new ContractorBank();
            }

            lineStart = 0;
            //newHash = sdb.returnSchetFaktura(dis.get("kod_klienta").toString());

            setPropertyAndValue(xSpreadsheet, 0, lineStart,
                    "Счет-фактура  (к ТТН №)" + saleDocumentBase.getDocumentNumber(), charSize, null,
                    "CENTER");
            lineStart++;
            setPropertyAndValue(xSpreadsheet, 0, lineStart, "Поставщик: "
                            + getDataAsString("SENDER_NAME"), charSize, null,
                    "LEFT");
            lineStart++;
            setPropertyAndValue(xSpreadsheet, 0, lineStart, "Адрес: "
                            + getDataAsString("SENDER_ADDRESS"), charSize, null,
                    "LEFT");
            lineStart++;

            setPropertyAndValue(xSpreadsheet, 0, lineStart, senderBank.getAccountFormat(), charSize, null, "LEFT");

            lineStart++;
            setPropertyAndValue(xSpreadsheet, 0, lineStart, "Банк: " + senderBank.getName(), charSize, null, "LEFT");
            lineStart++;
            setPropertyAndValue(xSpreadsheet, 0, lineStart, senderBank.getAddress() + " " + senderBank.getCodeMFOFormat(), charSize, null, "LEFT");
            lineStart++;
            setPropertyAndValue(xSpreadsheet, 0, lineStart, senderBank.getAccountCorrespondentNumberFormat() + " "
                    + senderBank.getCodeUNNFormat(), charSize, null, "LEFT");

            lineStart++;
            setPropertyAndValue(xSpreadsheet, 0, lineStart, "БАНК : СБ РФ г.Москва БИК 044525225 к/с 30101810400000000225 в ОПЕРУ ГУ ЦБ РФ по г.Москва", charSize, null, "LEFT");
            lineStart++;
            setPropertyAndValue(xSpreadsheet, 0, lineStart, "Покупатель: "
                            + getDataAsString("RECIPIENT_NAME"), charSize, null,
                    "LEFT");
            lineStart++;
            setPropertyAndValue(xSpreadsheet, 0, lineStart, "Адрес: "
                            + getDataAsString("RECIPIENT_ADDRESS"), charSize, null,
                    "LEFT");
            lineStart++;
            setPropertyAndValue(xSpreadsheet, 0, lineStart, recipientBank.getAccountFormat(), charSize, null, "LEFT");
            lineStart++;
            setPropertyAndValue(xSpreadsheet, 0, lineStart, "Банк: " + recipientBank.getName(), charSize, null, "LEFT");
            lineStart++;
            setPropertyAndValue(xSpreadsheet, 0, lineStart, recipientBank.getAddress() + " " + recipientBank.getCodeMFOFormat(), charSize, null, "LEFT");
            lineStart++;
            setPropertyAndValue(xSpreadsheet, 0, lineStart, senderBank.getAccountCorrespondentNumberFormat() + " "
                    + senderBank.getCodeUNNFormat(), charSize, null, "LEFT");


            // newHash = sdb.getDataForFactura(dis.get("ttn").toString());
            row = 15;
            for (SaleDocumentDetailItemReport item : getDetailListForInvoice(detailList)) {

                setPropertyAndValue(xSpreadsheet, 0, row, item.getItemName(), charSize, aBorder, "LEFT");
                setPropertyAndValue(xSpreadsheet, 1, row, item.getArticleNumber(), charSize, aBorder, "LEFT");
                setPropertyAndValue(xSpreadsheet, 3, row, item.getMeasureUnit(), charSize, aBorder, "LEFT");
                setPropertyAndValue(xSpreadsheet, 2, row, String.valueOf(item.getAmount()), charSize, aBorder, "LEFT");
                setPropertyAndValue(xSpreadsheet, 4, row, String.valueOf(item.getAmountPrint()), charSize, aBorder, "LEFT");
                setPropertyAndValue(
                        xSpreadsheet,
                        5,
                        row,
                        String.valueOf(item.getValuePriceCurrency()).replace(',', '.'), charSize, aBorder, "RIGHT");
                setPropertyAndValue(
                        xSpreadsheet,
                        6,
                        row,
                        String.valueOf(item.getValueSumCostCurrency()).replace(',', '.'), charSize, aBorder, "RIGHT");

                setPropertyAndValue(xSpreadsheet, 7, row, String.valueOf(item.getValueVat()), charSize, aBorder, "LEFT");
                setPropertyAndValue(
                        xSpreadsheet,
                        8,
                        row,
                        String.valueOf(item.getValueSumVatCurrency()).replace(',', '.'), charSize, aBorder, "RIGHT");

                setPropertyAndValue(
                        xSpreadsheet,
                        9,
                        row,
                        String.valueOf(item.getValueSumCostAndVatCurrency()).replace(',', '.'), charSize, aBorder, "RIGHT");

                setPropertyAndValue(xSpreadsheet, 10, row, item.getTnvedCode(), charSize, aBorder, "LEFT");
                row++;
            }

            lineStart = row;
            lineStart++;

            setPropertyAndValue(xSpreadsheet, 0, lineStart, "Всего к оплате: ",
                    charSize, aBorder, "LEFT");
            setPropertyAndValue(
                    xSpreadsheet,
                    2,
                    lineStart,
                    "=SUM(C" + String.valueOf(15) + ":C"
                            + String.valueOf(lineStart) + ")", charSize,
                    aBorder, "LEFT");
            setPropertyAndValue(
                    xSpreadsheet,
                    4,
                    lineStart,
                    "=SUM(E" + String.valueOf(15) + ":E"
                            + String.valueOf(lineStart) + ")", charSize,
                    aBorder, "LEFT");
            setPropertyAndValue(
                    xSpreadsheet,
                    5,
                    lineStart,
                    "=SUM(F" + String.valueOf(15) + ":F"
                            + String.valueOf(lineStart) + ")", charSize,
                    aBorder, "LEFT");

            setPropertyAndValue(xSpreadsheet, 6, lineStart,
                    String.valueOf(summingUp.getValueSumCostCurrency()).replace(',', '.'), charSize, aBorder,
                    "RIGHT");
            setPropertyAndValue(xSpreadsheet, 7, lineStart, "", charSize,
                    aBorder, "LEFT");

            setPropertyAndValue(xSpreadsheet, 8, lineStart,
                    String.valueOf(summingUp.getValueSumVatCurrency()).replace(',', '.'), charSize,
                    aBorder, "RIGHT");

            setPropertyAndValue(xSpreadsheet, 9, lineStart,
                    String.valueOf(summingUp.getValueSumCostAndVatCurrency()).replace(',', '.'), charSize,
                    aBorder, "RIGHT");
            setPropertyAndValue(xSpreadsheet, 10, lineStart, "", charSize,
                    aBorder, "LEFT");
            setPropertyAndValue(xSpreadsheet, 1, lineStart, "", charSize,
                    aBorder, "LEFT");
            setPropertyAndValue(xSpreadsheet, 3, lineStart, "", charSize,
                    aBorder, "LEFT");
            lineStart++;
            setPropertyAndValue(xSpreadsheet, 0, lineStart, "Вес: "
                            + String.format("%.3f", summingUp.getWeight()) + " кг.", charSize,
                    null, "LEFT");
            lineStart++;
            setPropertyAndValue(xSpreadsheet, 0, lineStart,
                    "Страна происхождения - Беларусь", charSize, null, "LEFT");
            lineStart++;
            setPropertyAndValue(
                    xSpreadsheet,
                    0,
                    lineStart,
                    "Руководитель: __________________                          Гл. бухгалтер: __________________",
                    charSize, null, "LEFT");
            lineStart++;
            setPropertyAndValue(
                    xSpreadsheet,
                    0,
                    lineStart,
                    "                                                         М.П.",
                    charSize, null, "LEFT");
            lineStart++;
            setPropertyAndValue(
                    xSpreadsheet,
                    0,
                    lineStart,
                    "Получил: _______________________                          Выдал: __________________________",
                    charSize, null, "LEFT");

            //*************************************************************************************
            // Протокол согласования цен
            //*************************************************************************************

            sheet = xSpreadsheets.getByName("Протокол");
            xSpreadsheet = UnoRuntime.queryInterface(
                    XSpreadsheet.class, sheet);
            setPropertyAndValue(xSpreadsheet, 0, 2,
                    "Поставщик: " + getDataAsString("SENDER_NAME") + " " + getDataAsString("SENDER_ADDRESS"), charSize, null,
                    "LEFT");
            setPropertyAndValue(xSpreadsheet, 0, 3,
                    "Получатель: " + getDataAsString("RECIPIENT_NAME") + " " + getDataAsString("RECIPIENT_ADDRESS"), charSize, null,
                    "LEFT");
            setPropertyAndValue(xSpreadsheet, 0, 4, "ТТН №: "
                    + saleDocumentBase.getDocumentNumber(), charSize, null, "LEFT");
            charSize = 10;

            row = 8;
            for (SaleDocumentDetailItemReport item : getDetailListForProtocol(detailList)) {

                setPropertyAndValue(xSpreadsheet, 0, row, item.getItemName(), charSize, aBorder, "LEFT");
                setPropertyAndValue(xSpreadsheet, 1, row, item.getArticleNumber(), charSize, aBorder, "LEFT");
                setPropertyAndValue(xSpreadsheet, 2, row, item.getModelNumber(), charSize, aBorder, "LEFT");
                setPropertyAndValue(xSpreadsheet, 3, row, String.valueOf(item.getValuePrice()), charSize, aBorder, "LEFT");
                setPropertyAndValue(xSpreadsheet, 4, row, String.valueOf(item.getValuePriceCurrency()),
                        charSize, aBorder, "LEFT");
                setPropertyAndValue(xSpreadsheet, 5, row, "", charSize,
                        aBorder, "LEFT");

                row++;
            }
            row++;

            setPropertyAndValue(
                    xSpreadsheet,
                    0,
                    row,
                    "Поставщик: _______________________                          Покупатель: __________________________",
                    charSize, null, "LEFT");

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }


        return false;
    }

    private List<SaleDocumentDetailItemReport> getDetailListForProtocol(final List<SaleDocumentDetailItemReport> detailList) {
        HashMap<String, SaleDocumentDetailItemReport> map = new HashMap<String, SaleDocumentDetailItemReport>();
        List<SaleDocumentDetailItemReport> result = new ArrayList<SaleDocumentDetailItemReport>();
        // Получаем мапу уникальных изделий
        for (SaleDocumentDetailItemReport item : detailList) {
            // Формируем ключ для мапы
            String key = item.getItemName()         // Наименование
                    + item.getValuePriceCurrency()   // Цена изделия в валюте
                    + item.getValuePrice()
                    + item.getArticleCode()          // Шифр артикула;
                    + item.getArticleNumber();        //Номер артикула


            // Смотрим, есть ли на этот ключ объект в мапе
            SaleDocumentDetailItemReport getItem = map.get(key);

            if (getItem == null) {
                SaleDocumentDetailItemReport newItem = new SaleDocumentDetailItemReport();

                // НЕ вычисляемые поля
                newItem.setItemName(item.getItemName());
                newItem.setArticleNumber(item.getArticleNumber());
                newItem.setModelNumber(item.getModelNumber());
                newItem.setValuePriceCurrency(item.getValuePriceCurrency());
                newItem.setValuePrice(item.getValuePrice());

                map.put(key, newItem);
            }
        }

        // Пробегаемся по мапе и заполняем новый список из мапы
        Iterator it = map.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            SaleDocumentDetailItemReport item = (SaleDocumentDetailItemReport) pair.getValue();
            result.add(item);
            it.remove(); // avoids a ConcurrentModificationException
        }

        // СОртировка списка по шифру артикула
        Collections.sort(result, new Comparator<SaleDocumentDetailItemReport>() {
            @Override
            public int compare(SaleDocumentDetailItemReport item2, SaleDocumentDetailItemReport item1) {
                return item2.getItemName().compareTo(item1.getItemName());
            }

        });

        return result;
    }

    private List<SaleDocumentDetailItemReport> getDetailListForInvoice(final List<SaleDocumentDetailItemReport> detailList) {
        HashMap<String, SaleDocumentDetailItemReport> map = new HashMap<String, SaleDocumentDetailItemReport>();
        List<SaleDocumentDetailItemReport> result = new ArrayList<SaleDocumentDetailItemReport>();
        // Получаем мапу уникальных изделий
        for (SaleDocumentDetailItemReport item : detailList) {
            // Формируем ключ для мапы
            String key = item.getItemName()         // Наименование
                    + item.getValuePriceCurrency()   // Цена изделия в валюте
                    + item.getArticleCode()          // Шифр артикула;
                    + item.getArticleNumber()        //Номер артикула
                    + item.getTnvedCode();           // Код ТНВЭД

            // Смотрим, есть ли на этот ключ объект в мапе
            SaleDocumentDetailItemReport getItem = map.get(key);

            if (getItem == null) {
                SaleDocumentDetailItemReport newItem = new SaleDocumentDetailItemReport();

                // НЕ вычисляемые поля
                newItem.setItemName(item.getItemName());
                newItem.setArticleNumber(item.getArticleNumber());
                newItem.setMeasureUnit(item.getMeasureUnit());
                newItem.setTnvedCode(item.getTnvedCode());
                newItem.setValuePriceCurrency(item.getValuePriceCurrency());

                // Вычисляемые поля
                newItem.setAmountPrint(item.getAmountPrint());
                newItem.setAmount(item.getAmount());

                newItem.setValueSumCostCurrency(item.getValueSumCostCurrency());
                newItem.setValueSumVatCurrency(item.getValueSumVatCurrency());
                newItem.setValueSumCostAndVatCurrency(item.getValueSumCostAndVatCurrency());

                map.put(key, newItem);
            } else {
                // Суммируем значения для группируемых по ключу объектов
                getItem.setAmountPrint(getItem.getAmountPrint() + item.getAmountPrint());
                getItem.setAmount(getItem.getAmount() + item.getAmount());

                getItem.setValueSumCostCurrency(getItem.getValueSumCostCurrency() + item.getValueSumCostCurrency());
                getItem.setValueSumVatCurrency(getItem.getValueSumVatCurrency() + item.getValueSumVatCurrency());
                getItem.setValueSumCostAndVatCurrency(getItem.getValueSumCostAndVatCurrency() + item.getValueSumCostAndVatCurrency());
            }
        }

        // Пробегаемся по мапе и заполняем новый список из мапы
        Iterator it = map.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            SaleDocumentDetailItemReport item = (SaleDocumentDetailItemReport) pair.getValue();
            result.add(item);
            it.remove(); // avoids a ConcurrentModificationException
        }

        // СОртировка списка по шифру артикула
        Collections.sort(result, new Comparator<SaleDocumentDetailItemReport>() {
            @Override
            public int compare(SaleDocumentDetailItemReport item2, SaleDocumentDetailItemReport item1) {
                return item2.getItemName().compareTo(item1.getItemName());
            }
        });

        return result;
    }

    private boolean isNewTNVDCOde(List<SaleDocumentDetailItemReport> list, String key) {
        for (SaleDocumentDetailItemReport last : list) {
            if (key.equals(last.getTnvedCode() + last.getItemName())) {
                //System.out.println("ГРУППА "+key+" УЖЕ СОЗДАНА");
                return false;
            }
        }
        //System.out.println("СОЗДАЕТСЯ ГРУППА "+key);
        return true;
    }

    /**
     * Срез по TNVD коду
     *
     * @return итого
     */
    private List<SaleDocumentDetailItemReport> getDetailItemSetByTnvdAndName(final List<SaleDocumentDetailItemReport> detailList) {
        HashMap<String, SaleDocumentDetailItemReport> map = new HashMap<String, SaleDocumentDetailItemReport>();
        List<SaleDocumentDetailItemReport> result = new ArrayList<SaleDocumentDetailItemReport>();
        // Получаем мапу уникальных изделий
        for (SaleDocumentDetailItemReport item : detailList) {
            // Формируем ключ для мапы
            String key = item.getItemName()         // Наименование
                    + item.getTnvedCode();           // Код ТНВЭД

            // Смотрим, есть ли на этот ключ объект в мапе
            SaleDocumentDetailItemReport getItem = map.get(key);

            if (getItem == null) {
                SaleDocumentDetailItemReport newItem = new SaleDocumentDetailItemReport();

                // НЕ вычисляемые поля
                newItem.setItemName(item.getItemName());
                newItem.setArticleNumber(item.getArticleNumber());
                newItem.setMeasureUnit(item.getMeasureUnit());
                newItem.setTnvedCode(item.getTnvedCode());

                // Вычисляемые поля
                newItem.setAmountPrint(item.getAmountPrint());
                newItem.setAmount(item.getAmount());
                newItem.setWeight(item.getWeight());
                newItem.setValueSumCostAndVatCurrency(item.getValueSumCostAndVatCurrency());

                map.put(key, newItem);
            } else {
                // Суммируем значения для группируемых по ключу объектов
                getItem.setAmountPrint(getItem.getAmountPrint() + item.getAmountPrint());
                getItem.setAmount(getItem.getAmount() + item.getAmount());

                getItem.setWeight(getItem.getWeight() + item.getWeight());

                getItem.setValueSumCostAndVatCurrency(getItem.getValueSumCostAndVatCurrency() + item.getValueSumCostAndVatCurrency());
            }
        }

        // Пробегаемся по мапе и заполняем новый список из мапы
        Iterator it = map.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            SaleDocumentDetailItemReport item = (SaleDocumentDetailItemReport) pair.getValue();
            result.add(item);
            it.remove(); // avoids a ConcurrentModificationException
        }

        // СОртировка списка по шифру артикула
        Collections.sort(result, new Comparator<SaleDocumentDetailItemReport>() {
            @Override
            public int compare(SaleDocumentDetailItemReport item2, SaleDocumentDetailItemReport item1) {
                return item2.getTnvedCode().compareTo(item1.getTnvedCode());
            }
        });


        // Законсервировано до ХУДШИХ ВРЕМЕН
        //AbstractJDBC db = new AbstractJDBC();
        //result = db.getTNVDCodeSliceByDocumentNumber(saleDocumentBase.getDocumentNumber());


        return result;
    }

    /**
     * Возвращает уникальный набор изделий.
     * Уникальность определяется по шифру артикула и цене в валюте
     *
     * @return набор уникальных изделий
     */
    private List<SaleDocumentDetailItemReport> getDetailItemSetByArticleCodeAndCurrencyPrice() {
        List<SaleDocumentDetailItemReport> result = new ArrayList<SaleDocumentDetailItemReport>();
        String articleCodeAndPrice = "";
        for (SaleDocumentDetailItemReport item : detailList) {
            if (!articleCodeAndPrice.equals(item.getArticleCode() + String.valueOf(item.getValuePriceCurrency()))) {
                result.add(item);
            }
            articleCodeAndPrice = item.getArticleCode() + String.valueOf(item.getValuePriceCurrency());
        }
        return result;
    }

    private boolean isNewArticleCode(List<SaleDocumentDetailItemReport> list, String key) {
        for (SaleDocumentDetailItemReport last : list) {
            if (key.equals(last.getTnvedCode() + last.getItemName())) {
                //System.out.println("ГРУППА "+key+" УЖЕ СОЗДАНА");
                return false;
            }
        }
        //System.out.println("СОЗДАЕТСЯ ГРУППА "+key);
        return true;
    }


    /**
     * Возвращает уникальный набор изделий.
     * Уникальность определяется по шифру артикула
     *
     * @return набор уникальных изделий
     */
    private List<SaleDocumentDetailItemReport> getDetailItemSetByArticleCode(List<SaleDocumentDetailItemReport> detailList) {
        HashMap<String, SaleDocumentDetailItemReport> map = new HashMap<String, SaleDocumentDetailItemReport>();
        List<SaleDocumentDetailItemReport> result = new ArrayList<SaleDocumentDetailItemReport>();
        // Получаем мапу уникальных изделий
        for (SaleDocumentDetailItemReport item : detailList) {

            // Формируем ключ для мапы
            String key = item.getItemName()          // Наименование
                    + item.getArticleCode()          // Шифр артикула;
                    + item.getArticleNumber()        //Номер артикула
                    + item.getModelNumber();         // Модель

            // Смотрим, есть ли на этот ключ объект в мапе
            SaleDocumentDetailItemReport getItem = map.get(key);

            if (getItem == null) {
                SaleDocumentDetailItemReport newItem = new SaleDocumentDetailItemReport();

                // НЕ вычисляемые поля
                newItem.setItemName(item.getItemName());
                newItem.setModelNumber(item.getModelNumber());
                newItem.setArticleNumber(item.getArticleNumber());
                newItem.setMeasureUnit(item.getMeasureUnit());
                newItem.setTnvedCode(item.getTnvedCode());
                newItem.setValuePriceCurrency(item.getValuePriceCurrency());

                // Вычисляемые поля
                newItem.setAmountPrint(item.getAmountPrint());
                newItem.setAmount(item.getAmount());
                newItem.setWeight(item.getWeight());


                newItem.setValueSumCostCurrency(item.getValueSumCostCurrency());
                newItem.setValueSumVatCurrency(item.getValueSumVatCurrency());
                newItem.setValueSumCostAndVatCurrency(item.getValueSumCostAndVatCurrency());

                map.put(key, newItem);
            } else {
                // Суммируем значения для группируемых по ключу объектов
                getItem.setAmountPrint(getItem.getAmountPrint() + item.getAmountPrint());
                getItem.setAmount(getItem.getAmount() + item.getAmount());
                getItem.setWeight(getItem.getWeight() + item.getWeight());

                getItem.setValueSumCostCurrency(getItem.getValueSumCostCurrency() + item.getValueSumCostCurrency());
                getItem.setValueSumVatCurrency(getItem.getValueSumVatCurrency() + item.getValueSumVatCurrency());
                getItem.setValueSumCostAndVatCurrency(getItem.getValueSumCostAndVatCurrency() + item.getValueSumCostAndVatCurrency());
            }
        }

        // Пробегаемся по мапе и заполняем новый список из мапы
        Iterator it = map.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            SaleDocumentDetailItemReport item = (SaleDocumentDetailItemReport) pair.getValue();
            result.add(item);
            it.remove(); // avoids a ConcurrentModificationException
        }

        // СОртировка списка по шифру артикула
        Collections.sort(result, new Comparator<SaleDocumentDetailItemReport>() {
            @Override
            public int compare(SaleDocumentDetailItemReport item2, SaleDocumentDetailItemReport item1) {
                return item2.getItemName().compareTo(item1.getItemName());
            }
        });


        ///  AbstractJDBC db = new AbstractJDBC();
        // result = db.getArticleCodeSliceByDocumentNumber(saleDocumentBase.getDocumentNumber());

        return result;
    }


    private List<SaleDocumentDetailItemReport> getDetailItemSetByModelArticlePrice(List<SaleDocumentDetailItemReport> detailList) {
        HashMap<String, SaleDocumentDetailItemReport> map = new HashMap<String, SaleDocumentDetailItemReport>();
        List<SaleDocumentDetailItemReport> result = new ArrayList<SaleDocumentDetailItemReport>();
        // Получаем мапу уникальных изделий
        for (SaleDocumentDetailItemReport item : detailList) {

            // Формируем ключ для мапы
            String key = item.getArticleNumber()        //Номер артикула
                    + item.getModelNumber()             // Модель
                    + item.getValuePriceCurrency();      //ЦЕна в валюте

            // Смотрим, есть ли на этот ключ объект в мапе
            SaleDocumentDetailItemReport getItem = map.get(key);

            if (getItem == null) {
                SaleDocumentDetailItemReport newItem = new SaleDocumentDetailItemReport();

                // НЕ вычисляемые поля
                newItem.setItemName(item.getItemName());
                newItem.setModelNumber(item.getModelNumber());
                newItem.setArticleNumber(item.getArticleNumber());
                newItem.setMeasureUnit(item.getMeasureUnit());
                newItem.setTnvedCode(item.getTnvedCode());
                newItem.setValuePriceCurrency(item.getValuePriceCurrency());

                // Вычисляемые поля
                newItem.setAmountPrint(item.getAmountPrint());
                newItem.setAmount(item.getAmount());
                newItem.setWeight(item.getWeight());


                newItem.setValuePriceCurrency(item.getValuePriceCurrency());
                newItem.setValueSumCostCurrency(item.getValueSumCostCurrency());
                newItem.setValueSumVatCurrency(item.getValueSumVatCurrency());
                newItem.setValueSumCostAndVatCurrency(item.getValueSumCostAndVatCurrency());

                map.put(key, newItem);
            } else {
                // Суммируем значения для группируемых по ключу объектов
                getItem.setAmountPrint(getItem.getAmountPrint() + item.getAmountPrint());
                getItem.setAmount(getItem.getAmount() + item.getAmount());
                getItem.setWeight(getItem.getWeight() + item.getWeight());


                //getItem.setValuePriceCurrency(getItem.getValuePriceCurrency() + item.getValuePriceCurrency());
                getItem.setValueSumCostCurrency(getItem.getValueSumCostCurrency() + item.getValueSumCostCurrency());
                getItem.setValueSumVatCurrency(getItem.getValueSumVatCurrency() + item.getValueSumVatCurrency());
                getItem.setValueSumCostAndVatCurrency(getItem.getValueSumCostAndVatCurrency() + item.getValueSumCostAndVatCurrency());
            }
        }

        // Пробегаемся по мапе и заполняем новый список из мапы
        Iterator it = map.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            SaleDocumentDetailItemReport item = (SaleDocumentDetailItemReport) pair.getValue();
            result.add(item);
            it.remove(); // avoids a ConcurrentModificationException
        }

        // СОртировка списка по шифру артикула
        Collections.sort(result, new Comparator<SaleDocumentDetailItemReport>() {
            @Override
            public int compare(SaleDocumentDetailItemReport item2, SaleDocumentDetailItemReport item1) {
                int e1 = Integer.valueOf(item1.getModelNumber());
                int e2 = Integer.valueOf(item2.getModelNumber());
                if (e1 > e2) {
                    return -1;
                } else if (e2 > e1) {
                    return 1;
                } else {
                    return 0;
                }

                //return  item2.getModelNumber().compareTo(item1.getModelNumber());
            }
        });


        ///  AbstractJDBC db = new AbstractJDBC();
        // result = db.getArticleCodeSliceByDocumentNumber(saleDocumentBase.getDocumentNumber());

        return result;
    }

}
