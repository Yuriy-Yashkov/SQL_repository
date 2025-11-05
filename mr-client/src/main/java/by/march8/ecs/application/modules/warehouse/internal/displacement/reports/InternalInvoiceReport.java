package by.march8.ecs.application.modules.warehouse.internal.displacement.reports;

import by.gomel.freedev.ucframework.uccore.report.model.ReportProperties;
import by.march8.entities.warehouse.VInternalInvoiceReport;
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
public class InternalInvoiceReport extends AbstractReport {
    private ArrayList<Object> dataList;

    public InternalInvoiceReport(ArrayList<Object> data) {
        dataList = data;
        create();
    }

    @Override
    ReportProperties prepareProperties(final ReportProperties properties) {
        // Устанавливаем имя файла шаблона
        properties.setBlankName("transfer_to_warehouse_" + DateUtils.getTimestampFullSale(new Date()));
        properties.getTemplate().setTemplateName("transfer_to_warehouse.ots");
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

            // xCell.setFormula("Грузоотправитель: " + getDataAsString("SENDER_NAME") + " " + getDataAsString("SENDER_ADDRESS"));
            setCharHeightToCell(xCell);

            xCell = xSpreadsheet.getCellByPosition(0, 1);
            //xCell.setFormula("Грузополучатель: " + getDataAsString("RECIPIENT_NAME") + " " + getDataAsString("RECIPIENT_ADDRESS"));
            setCharHeightToCell(xCell);

            xCell = xSpreadsheet.getCellByPosition(0, 2);
            //xCell.setFormula(SaleDocumentShippingReport.getContractDescription(documentReport));
            setCharHeightToCell(xCell);

            // ----------------Заполнение тела документа
            int row = 2;
            lineStart = row;
            //List<SaleDocumentDetailItemReport> detailList = new ArrayList<>();
            int counter = 1;
            for (Object obj : dataList) {
                VInternalInvoiceReport item = (VInternalInvoiceReport) obj;

                pb.setMessageText("Обработано " + counter + " из " + dataList.size() + " записей ...");

                xCell = xSpreadsheet.getCellByPosition(0, row);
                xCell.setValue(counter);

                xCell = xSpreadsheet.getCellByPosition(1, row);
                xCell.setFormula(DateUtils.getNormalDateFormat(item.getDocumentDate()));

                xCell = xSpreadsheet.getCellByPosition(2, row);
                xCell.setFormula("'" + item.getDocumentNumber());

                xCell = xSpreadsheet.getCellByPosition(3, row);
                xCell.setFormula("'" + item.getRoutingNumber());

                xCell = xSpreadsheet.getCellByPosition(4, row);
                //xCell.setFormula(hm.get("kol").toString());
                xCell.setFormula("'" + item.getModelNumber());

                xCell = xSpreadsheet.getCellByPosition(5, row);
                //xCell.setFormula(hm.get("cena").toString());
                xCell.setFormula(item.getArticleNumber());

                xCell = xSpreadsheet.getCellByPosition(6, row);
                //xCell.setFormula(hm.get("summa").toString());
                xCell.setFormula(item.getItemName());

                xCell = xSpreadsheet.getCellByPosition(7, row);
                //xCell.setFormula(dis.get("nds").toString());
                xCell.setFormula(item.getItemSize());

                xCell = xSpreadsheet.getCellByPosition(8, row);
                //xCell.setFormula(hm.get("summa_nds").toString());
                xCell.setFormula(item.getItemGrade());

                xCell = xSpreadsheet.getCellByPosition(9, row);
                //xCell.setFormula(hm.get("itogo").toString());
                xCell.setFormula(item.getItemColor());

                xCell = xSpreadsheet.getCellByPosition(10, row);
                //xCell.setFormula(hm.get("massa").toString());
                xCell.setValue(item.getCost());

                xCell = xSpreadsheet.getCellByPosition(11, row);
                //xCell.setFormula(hm.get("kkr").toString());
                xCell.setValue(item.getQuantityPack());

                xCell = xSpreadsheet.getCellByPosition(12, row);
                //xCell.setFormula(hm.get("massa").toString());
                xCell.setValue(item.getCostAll());

                xCell = xSpreadsheet.getCellByPosition(13, row);
                //xCell.setFormula(hm.get("kkr").toString());
                xCell.setValue(item.getNormByItem());

                xCell = xSpreadsheet.getCellByPosition(14, row);
                //xCell.setFormula(hm.get("massa").toString());
                xCell.setValue(item.getNormByAmount());


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
