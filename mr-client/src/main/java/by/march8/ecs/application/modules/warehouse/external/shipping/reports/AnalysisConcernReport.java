package by.march8.ecs.application.modules.warehouse.external.shipping.reports;

import by.gomel.freedev.ucframework.uccore.report.model.ReportProperties;
import by.march8.ecs.application.modules.references.currency.mode.CurrencyRateMonitorMode;
import by.march8.ecs.application.modules.warehouse.external.shipping.dao.CourseDateByOrderDB;
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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Andy 17.06.2016.
 */
public class AnalysisConcernReport extends AbstractInvoiceReport {

    public AnalysisConcernReport(final SaleDocumentReport report) {
        super(report);
        documentReport = report;
        create();
    }

    @Override
    ReportProperties prepareProperties(final ReportProperties properties) {
        // Устанавливаем имя файла шаблона
        properties.setBlankName("analysis_concern_" + saleDocumentBase.getDocumentNumber());
        properties.getTemplate().setTemplateName("analysisConcern.ots");
        return properties;
    }


    @Override
    boolean populateData(final XComponent component) {
        List<SaleDocumentDetailItemReport> groupList = new ArrayList<>();

        try {
            XSpreadsheetDocument xSpreadsheetDocument = UnoRuntime
                    .queryInterface(XSpreadsheetDocument.class, component);
            XSpreadsheets xSpreadsheets = xSpreadsheetDocument.getSheets();
            Object sheet = xSpreadsheets.getByName("Расчет");
            XSpreadsheet xSpreadsheet = UnoRuntime
                    .queryInterface(XSpreadsheet.class, sheet);

            XNamed xNamed = UnoRuntime.queryInterface(XNamed.class,
                    xSpreadsheet);

            //xNamed.setName("ПСЦ к ТТН№ " + saleDocumentBase.getDocumentNumber());

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
            //  xCell.setValue(Double.valueOf(new CourseDateByOrderDB().getCourseByOrderStr()));
            // xCell.setValue(documentReport.getDocument().getCurrencyRateFixed());
            /**
             * Идентификатор валюты формата March8
             * 1 - Бел. руб.-
             * 2 - Росс.руб.-17
             * 3 - Долл. США-15
             * 4 - Евро-19
             * 5 - Гривна-18
             */
            int val = 0;
            switch (documentReport.getDocument().getCurrencyId()) {
                case 2:
                    val = 17;
                    break;
                case 3:
                    val = 15;
                    break;
                case 4:
                    val = 19;
                    break;
                case 5:
                    val = 18;
                    break;
            }
            xCell.setValue(CurrencyRateMonitorMode.getCurrencyRateByDate(new CourseDateByOrderDB().getCourseByOrderStr(), val));

            xCell = xSpreadsheet.getCellByPosition(18, 3);
            xCell.setValue(documentReport.getDocument().getCurrencyRateSale());

            int row = 10;

            Date d = new Date();


            String key = "";
            SaleDocumentDetailItemReport newItem = null;

            for (SaleDocumentDetailItemReport item : detailList) {

                String currKey = item.getArticleName() + "_" + String.valueOf(item.getAccountingPrice());
                System.out.println(currKey);

                if (!key.equals(currKey)) {
                    // Новое изделие
                    newItem = new SaleDocumentDetailItemReport();
                    newItem.setItemName(item.getItemName());
                    newItem.setArticleNumber(item.getArticleNumber());
                    newItem.setMeasureUnit(item.getMeasureUnit());
                    newItem.setAmountPrint(item.getAmountPrint());
                    newItem.setValueAccountingPrice(item.getAccountingPrice());
                    newItem.setPlanPrimeCost(item.getPlanPrimeCost());
                    newItem.setPlanProfitability(item.getPlanProfitability());
                    newItem.setId(row);
                    groupList.add(newItem);
                    key = currKey;
                } else {
                    newItem.setAmountPrint(newItem.getAmountPrint() + item.getAmountPrint());
                }

                xCell = xSpreadsheet.getCellByPosition(0, row);
                xCell.setFormula(String.valueOf(row - 9));

                xCell = xSpreadsheet.getCellByPosition(1, row);
                xCell.setFormula(item.getItemName());

                xCell = xSpreadsheet.getCellByPosition(2, row);
                xCell.setFormula(item.getArticleName());

                xCell = xSpreadsheet.getCellByPosition(3, row);
                xCell.setFormula("'" + item.getModelNumber());

                xCell = xSpreadsheet.getCellByPosition(4, row);
                xCell.setFormula(item.getGradeAsString());

                xCell = xSpreadsheet.getCellByPosition(5, row);
                xCell.setFormula("'" + item.getItemSize());

                xCell = xSpreadsheet.getCellByPosition(6, row);
                xCell.setFormula(item.getMeasureUnit());

                xCell = xSpreadsheet.getCellByPosition(7, row);
                xCell.setValue(item.getAmountPrint());

                xCell = xSpreadsheet.getCellByPosition(8, row);
                xCell.setValue(item.getAccountingPrice());

                xCell = xSpreadsheet.getCellByPosition(9, row);
                xCell.setValue(item.getPlanProfitability());

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
                //На цену изделия - 1


                if (documentReport.getDocument().getDiscountType() == 1) {
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

        try {
            XSpreadsheetDocument xSpreadsheetDocument = UnoRuntime
                    .queryInterface(XSpreadsheetDocument.class, component);
            XSpreadsheets xSpreadsheets = xSpreadsheetDocument.getSheets();
            Object sheet = xSpreadsheets.getByName("Концерн");
            XSpreadsheet xSpreadsheet = UnoRuntime
                    .queryInterface(XSpreadsheet.class, sheet);
            prepareConcernPage(xSpreadsheet, groupList);

        } catch (Exception e) {
            System.err.println("Ошибка в получении ссылки на лист КОНЦЕРН");
        }

        return true;
    }

    private void prepareConcernPage(XSpreadsheet xSpreadsheet, List<SaleDocumentDetailItemReport> groupList) {
        int row = 11;
        try {
            XCell xCell = xSpreadsheet.getCellByPosition(17, 1);

            for (SaleDocumentDetailItemReport item : groupList) {

                xCell = xSpreadsheet.getCellByPosition(0, row);
                xCell.setFormula(String.valueOf(row - 10));

                xCell = xSpreadsheet.getCellByPosition(1, row);
                xCell.setFormula(item.getItemName() + " (" + item.getArticleNumber() + ")");

                xCell = xSpreadsheet.getCellByPosition(2, row);
                //xCell.setFormula(item.getArticleName());
                xCell.setFormula(getProductProductionDate(item.getArticleNumber()));

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
                xCell.setFormula("=Расчет.U" + (item.getId() + 1));

                xCell = xSpreadsheet.getCellByPosition(9, row);
                xCell.setFormula("=Расчет.T" + (item.getId() + 1));

                xCell = xSpreadsheet.getCellByPosition(10, row);
                xCell.setFormula("=Расчет.V" + (item.getId() + 1));

                int line = row + 1;

                xCell = xSpreadsheet.getCellByPosition(11, row);
                xCell.setFormula("=I" + line + "-F" + line);

                xCell = xSpreadsheet.getCellByPosition(12, row);
                xCell.setFormula("=I" + line + "-G" + line);

                xCell = xSpreadsheet.getCellByPosition(13, row);
                xCell.setFormula("=(L" + line + "*E" + line + ")/1000");

                xCell = xSpreadsheet.getCellByPosition(14, row);
                xCell.setFormula("=(M" + line + "*E" + line + ")/1000");

                xCell = xSpreadsheet.getCellByPosition(15, row);
                xCell.setFormula(by.march8.api.utils.DateUtils.getMonthNameByDate(documentReport.getDocument().getDocumentDate()));

                rowFormatDouble(xSpreadsheet, row - 1, 16, 10);

                row++;
            }

            rowFormatDouble(xSpreadsheet, row - 1, 16, 10);
            rowFormatDouble(xSpreadsheet, row, 16, 10);

            setFontBoldForCellRange(xSpreadsheet, 0, 15, row);

            xCell = xSpreadsheet.getCellByPosition(1, row);
            xCell.setFormula("Итого:");

            xCell = xSpreadsheet.getCellByPosition(4, row);
            xCell.setFormula("=SUM(E" + (12) + ":E"
                    + (row) + ")");

            xCell = xSpreadsheet.getCellByPosition(13, row);
            xCell.setFormula("=SUM(N" + (12) + ":N"
                    + (row) + ")");

            xCell = xSpreadsheet.getCellByPosition(14, row);
            xCell.setFormula("=SUM(O" + (12) + ":O"
                    + (row) + ")");

            row += 2;

            xCell = xSpreadsheet.getCellByPosition(1, row);
            xCell.setFormula("Заместитель директора по экономике");
            xPropSet = UnoRuntime
                    .queryInterface(XPropertySet.class,
                            xCell);
            xPropSet.setPropertyValue("HoriJustify", CellHoriJustify.LEFT);

            xCell = xSpreadsheet.getCellByPosition(11, row);
            xCell.setFormula("__________________/ О.А. Живодер /");
            xPropSet = UnoRuntime
                    .queryInterface(XPropertySet.class,
                            xCell);
            xPropSet.setPropertyValue("HoriJustify", CellHoriJustify.LEFT);

        } catch (Exception e) {
            System.err.println("Ошибка при заполнении ласта КОНЦЕРН");
        }
    }

    private String getProductProductionDate(String article) {
        if (!article.isEmpty()) {
            String d = article.substring(0, 1);
            if (d.equals("0")) {
                return "2020г.";
            } else {
                return "201" + d + "г.";
            }
        } else {
            return "";
        }
    }
}
