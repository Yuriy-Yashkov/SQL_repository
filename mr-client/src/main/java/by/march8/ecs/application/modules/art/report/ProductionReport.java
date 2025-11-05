package by.march8.ecs.application.modules.art.report;

import by.gomel.freedev.ucframework.uccore.report.model.ReportProperties;
import by.march8.ecs.application.modules.art.model.ProductionReportData;
import by.march8.ecs.application.modules.art.model.ProductionReportItem;
import com.sun.star.container.XNamed;
import com.sun.star.lang.XComponent;
import com.sun.star.sheet.XSpreadsheet;
import com.sun.star.sheet.XSpreadsheetDocument;
import com.sun.star.sheet.XSpreadsheets;
import com.sun.star.table.XCell;
import com.sun.star.uno.UnoRuntime;

import java.util.Calendar;

/**
 * @author Andy 24.01.2017.
 */
public class ProductionReport extends AbstractProductionReport {
    protected String month;

    public ProductionReport(final ProductionReportData report, int monthId) {
        super(report);

        switch (monthId) {
            case 1:
                month = "Январь";
                break;
            case 2:
                month = "Февраль";
                break;
            case 3:
                month = "Март";
                break;
            case 4:
                month = "Апрель";
                break;
            case 5:
                month = "Май";
                break;
            case 6:
                month = "Июнь";
                break;
            case 7:
                month = "Июль";
                break;
            case 8:
                month = "Август";
                break;
            case 9:
                month = "Сентябрь";
                break;
            case 10:
                month = "Октябрь";
                break;
            case 11:
                month = "Ноябрь";
                break;
            case 12:
                month = "Декабрь";
                break;
            default:
                month = "------";
                break;
        }

        create();

    }

    @Override
    ReportProperties prepareProperties(final ReportProperties properties) {
        Calendar c = Calendar.getInstance();

        // Устанавливаем имя файла шаблона
        properties.setBlankName("production_report_" + c.get(Calendar.MILLISECOND));
        properties.getTemplate().setTemplateName("ПроизводственныйОтчетУМиРА.ots");
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

            xNamed.setName("Отчет");

            // ----------------Заполнение шапки документа
            XCell xCell = xSpreadsheet.getCellByPosition(0, 2);

            xCell.setFormula("за " + month + " месяц ");
/*

            xCell = xSpreadsheet.getCellByPosition(0, 1);
            xCell.setFormula("Грузополучатель: " + getDataAsString("RECIPIENT_NAME") + " " + getDataAsString("RECIPIENT_ADDRESS"));
            setCharHeightForCell(xCell);

            xCell = xSpreadsheet.getCellByPosition(0, 2);
            xCell.setFormula(SaleDocumentShippingReport.getContractDescription(documentReport));
            setCharHeightForCell(xCell);
*/
/*            xCell = xSpreadsheet.getCellByPosition(16, 0);
            xCell.setFormula("Скидка клиенту :" + saleDocumentBase.getDiscountValue());
            xCell = xSpreadsheet.getCellByPosition(16, 1);
            xCell.setFormula("Курс расчета валюты :" + saleDocumentBase.getCurrencyRateFixed());
            xCell = xSpreadsheet.getCellByPosition(16, 2);
            xCell.setFormula("Курс расчета рублей :" + saleDocumentBase.getCurrencyRateSale());*/


            // ----------------Заполнение тела документа
            int row = 7;
            lineStart = row;
            String articleCode = "";

            for (ProductionReportItem item : detailList) {

                xCell = xSpreadsheet.getCellByPosition(0, row);
                xCell.setFormula(item.getArticleCode());

                xCell = xSpreadsheet.getCellByPosition(1, row);
                xCell.setFormula(item.getItemName());

                xCell = xSpreadsheet.getCellByPosition(2, row);
                xCell.setFormula(item.getArticleName());

                xCell = xSpreadsheet.getCellByPosition(3, row);
                xCell.setFormula(item.getModelNumber());

                xCell = xSpreadsheet.getCellByPosition(4, row);
                xCell.setFormula(String.valueOf(item.getRemainsBeginPeriod()));

                xCell = xSpreadsheet.getCellByPosition(5, row);
                xCell.setFormula(String.valueOf(item.getInComingCuttingDept()));

                //xCell = xSpreadsheet.getCellByPosition(6, row);
                //xCell.setFormula(String.valueOf(item.getInComing708Dept()));

                xCell = xSpreadsheet.getCellByPosition(7, row);
                xCell.setFormula(String.valueOf(item.getOutComingWarehouse()));

                xCell = xSpreadsheet.getCellByPosition(8, row);
                xCell.setFormula(String.valueOf(item.getOutComingCuttingDept()));

                xCell = xSpreadsheet.getCellByPosition(9, row);
                xCell.setFormula(String.valueOf(item.getRemainsEndPeriod()));


                // setCharHeightForRangeCell(xSpreadsheet, 0, row, 9);
                row++;
            }

            row++;

            //*****************************************************************************
            // Итого по документу
            //*****************************************************************************

            xCell = xSpreadsheet.getCellByPosition(2, row);
            xCell.setFormula("ИТОГО по УМиРА :");

            // Итого количество
            xCell = xSpreadsheet.getCellByPosition(4, row);
            xCell.setValue(reportTotal.getRemainsBeginPeriod());

            // Сумма
            xCell = xSpreadsheet.getCellByPosition(5, row);
            xCell.setValue(reportTotal.getInComingCuttingDept());

            // Сумма НДС
            //xCell = xSpreadsheet.getCellByPosition(6, row);
            //xCell.setValue(reportTotal.getInComing708Dept());

            xCell = xSpreadsheet.getCellByPosition(7, row);
            xCell.setValue(reportTotal.getOutComingWarehouse());

            xCell = xSpreadsheet.getCellByPosition(8, row);
            xCell.setValue(reportTotal.getOutComingCuttingDept());

            xCell = xSpreadsheet.getCellByPosition(9, row);
            xCell.setValue(reportTotal.getRemainsEndPeriod());
            //setCharHeightForRangeCell(xSpreadsheet, 0, row, 10);
            row++;
            row++;
            row++;

            xCell = xSpreadsheet.getCellByPosition(2, row);
            //xCell.setFormula("Сдал_________________________________ ");
            setCharHeightForCell(xCell, 9.0f);

            xCell = xSpreadsheet.getCellByPosition(7, row);
            //xCell.setFormula("Принял_______________________________ ");
            //setCharHeightForCell(xCell);

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }
}
