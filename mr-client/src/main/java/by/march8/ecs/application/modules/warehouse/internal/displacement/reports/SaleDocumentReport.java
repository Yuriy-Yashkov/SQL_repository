package by.march8.ecs.application.modules.warehouse.internal.displacement.reports;

import by.gomel.freedev.ucframework.uccore.report.model.ReportProperties;
import by.march8.entities.warehouse.VSaleDocumentReport;
import com.sun.star.container.XNamed;
import com.sun.star.lang.XComponent;
import com.sun.star.sheet.XSpreadsheet;
import com.sun.star.sheet.XSpreadsheetDocument;
import com.sun.star.sheet.XSpreadsheets;
import com.sun.star.table.XCell;
import com.sun.star.uno.UnoRuntime;
import common.DateUtils;

import java.util.ArrayList;
import java.util.Date;

/**
 * @author Andy on 19.11.2021 14:30
 */
public class SaleDocumentReport extends AbstractReport {
    private ArrayList<Object> dataList;

    public SaleDocumentReport(ArrayList<Object> data) {
        dataList = data;
        create();
    }

    @Override
    ReportProperties prepareProperties(final ReportProperties properties) {
        // Устанавливаем имя файла шаблона
        properties.setBlankName("sale_from_warehouse_" + DateUtils.getTimestampFullSale(new Date()));
        properties.getTemplate().setTemplateName("sale_from_warehouse.ots");
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
            int lineStart = 1;

            //xNamed.setName("Приложение к ТТН №" + saleDocumentBase.getDocumentNumber()+" ("+detailMap.get("TIMESTAMP")+")");

            // ----------------Заполнение шапки документа
            XCell xCell = xSpreadsheet.getCellByPosition(0, 0);

            // ----------------Заполнение тела документа
            int row = 3;
            lineStart = row;
            //List<SaleDocumentDetailItemReport> detailList = new ArrayList<>();
            int counter = 1;
            for (Object obj : dataList) {
                VSaleDocumentReport item = (VSaleDocumentReport) obj;

                pb.setMessageText("Обработано " + counter + " из " + dataList.size() + " записей ...");

                xCell = xSpreadsheet.getCellByPosition(0, row);
                xCell.setValue(counter);

                xCell = xSpreadsheet.getCellByPosition(1, row);
                xCell.setFormula(DateUtils.getNormalDateFormat(item.getDocumentDate()));

                xCell = xSpreadsheet.getCellByPosition(2, row);
                xCell.setFormula("'" + item.getDocumentNumber());

                xCell = xSpreadsheet.getCellByPosition(3, row);
                xCell.setFormula(item.getDocumentType());

                xCell = xSpreadsheet.getCellByPosition(4, row);
                //xCell.setFormula(hm.get("kol").toString());
                xCell.setFormula("'" + item.getContractorCode());

                xCell = xSpreadsheet.getCellByPosition(5, row);
                //xCell.setFormula(hm.get("cena").toString());
                xCell.setFormula(item.getContractorName());

                xCell = xSpreadsheet.getCellByPosition(6, row);
                //xCell.setFormula(hm.get("summa").toString());
                xCell.setValue(item.getItemModel());

                xCell = xSpreadsheet.getCellByPosition(7, row);
                //xCell.setFormula(dis.get("nds").toString());
                xCell.setFormula(item.getItemArticle());

                xCell = xSpreadsheet.getCellByPosition(8, row);
                //xCell.setFormula(hm.get("summa_nds").toString());
                xCell.setFormula(item.getItemName());

                xCell = xSpreadsheet.getCellByPosition(9, row);
                //xCell.setFormula(hm.get("itogo").toString());
                xCell.setFormula(item.getItemSize());

                xCell = xSpreadsheet.getCellByPosition(10, row);
                //xCell.setFormula(hm.get("massa").toString());
                xCell.setValue(item.getItemGrade());

                xCell = xSpreadsheet.getCellByPosition(11, row);
                //xCell.setFormula(hm.get("kkr").toString());
                xCell.setFormula(item.getItemColor());

                xCell = xSpreadsheet.getCellByPosition(12, row);
                //xCell.setFormula(hm.get("massa").toString());
                xCell.setValue(item.getAmount());

                xCell = xSpreadsheet.getCellByPosition(13, row);
                //xCell.setFormula(hm.get("kkr").toString());
                xCell.setFormula(item.getCurrencyName());

                xCell = xSpreadsheet.getCellByPosition(14, row);
                //xCell.setFormula(hm.get("massa").toString());
                xCell.setValue(item.getCurrencyFixedRate());

                xCell = xSpreadsheet.getCellByPosition(15, row);
                xCell.setValue(item.getCurrencySaleRate());

                xCell = xSpreadsheet.getCellByPosition(16, row);
                xCell.setValue(item.getValueVAT());

                xCell = xSpreadsheet.getCellByPosition(17, row);
                xCell.setValue(item.getValuePrice());
                xCell = xSpreadsheet.getCellByPosition(18, row);
                xCell.setValue(item.getValueCost());
                xCell = xSpreadsheet.getCellByPosition(19, row);
                xCell.setValue(item.getValueSumVat());
                xCell = xSpreadsheet.getCellByPosition(20, row);
                xCell.setValue(item.getValueSumCostAndVat());

                xCell = xSpreadsheet.getCellByPosition(21, row);
                xCell.setValue(item.getValuePriceCurrency());
                xCell = xSpreadsheet.getCellByPosition(22, row);
                xCell.setValue(item.getValueCosCurrencyt());
                xCell = xSpreadsheet.getCellByPosition(23, row);
                xCell.setValue(item.getValueSumVatCurrency());
                xCell = xSpreadsheet.getCellByPosition(24, row);
                xCell.setValue(item.getValueSumCostAndVatCurrency());

                xCell = xSpreadsheet.getCellByPosition(25, row);
                xCell.setValue(item.getRateCreateItem());
                xCell = xSpreadsheet.getCellByPosition(26, row);
                xCell.setValue(item.getRateCreateAll());
                xCell = xSpreadsheet.getCellByPosition(27, row);
                xCell.setValue(item.getRateVariableItem());
                xCell = xSpreadsheet.getCellByPosition(28, row);
                xCell.setValue(item.getRateVariableAll());


                //setCharHeightForRangeCell(xSpreadsheet, 0, row, 12);
                row++;
                counter++;
            }
        } catch (Exception e) {
            System.out.println("Ошибка формирования отчета ");
            e.printStackTrace();
        }

        return true;
    }
}
