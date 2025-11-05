package by.march8.ecs.application.modules.warehouse.external.shipping.reports;

import by.gomel.freedev.ucframework.uccore.report.model.ReportProperties;
import by.march8.api.utils.DateUtils;
import by.march8.ecs.application.modules.warehouse.external.shipping.model.SaleDocumentReport;
import by.march8.entities.warehouse.SaleDocumentDetailItemReport;
import com.sun.star.container.XNamed;
import com.sun.star.lang.XComponent;
import com.sun.star.sheet.XSpreadsheet;
import com.sun.star.sheet.XSpreadsheetDocument;
import com.sun.star.sheet.XSpreadsheets;
import com.sun.star.table.CellHoriJustify;
import com.sun.star.table.XCell;
import com.sun.star.table.XCellRange;
import com.sun.star.uno.UnoRuntime;

import java.util.Calendar;
import java.util.Date;

/**
 * @author Andy 17.06.2016.
 */
public class ProtocolBelVillesdenReport extends AbstractInvoiceReport {

    public ProtocolBelVillesdenReport(final SaleDocumentReport report) {
        super(report);
        documentReport = report;
        create();
    }

    @Override
    ReportProperties prepareProperties(final ReportProperties properties) {
        // Устанавливаем имя файла шаблона
        properties.setBlankName("protocol_" + saleDocumentBase.getDocumentNumber());
        properties.getTemplate().setTemplateName("протоколБелВиллесденЕщеНовее.ots");
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

            xNamed.setName("ПСЦ к ТТН№ " + saleDocumentBase.getDocumentNumber());

            XCell xCell = xSpreadsheet.getCellByPosition(2, 0);
            xCell.setFormula("между " + getDataAsString("RECIPIENT_NAME") + " и " + getDataAsString("SENDER_NAME") + "\"");
            //xCell.setFormula("№ 12-11/ЗИП-2232 поставщика ОАО \"8 Марта\" от 31.12.2011г.");

            xCell = xSpreadsheet.getCellByPosition(2, 1);
            xCell.setFormula(getDataAsString("SENDER_NAME") + "\"");

            xCell = xSpreadsheet.getCellByPosition(2, 2);
            xCell.setFormula("№ " + getDataAsString("CONTRACT_NUMBER") + " от " + getDataAsString("CONTRACT_DATE_BEGIN") + "г.");

            xCell = xSpreadsheet.getCellByPosition(2, 3);
            //xCell.setFormula(getDataAsString("CONTRACT_DATE_BEGIN"));
            xCell.setFormula("");

            Date d = DateUtils.getDateByStringValue(getDataAsString("DOCUMENT_DATE"));
            Calendar c = Calendar.getInstance();
            c.setTime(d);
            c.set(Calendar.YEAR, c.get(Calendar.YEAR) + 1);


            xCell = xSpreadsheet.getCellByPosition(5, 4);
            xCell.setFormula(DateUtils.getNormalDateFormat(c.getTime()));

            int row = 6;

            for (SaleDocumentDetailItemReport item : detailList) {

                xCell = xSpreadsheet.getCellByPosition(0, row);
                xCell.setFormula(String.valueOf(row - 5));

                xCell = xSpreadsheet.getCellByPosition(1, row);
                xCell.setFormula(item.getEanCode());

                xCell = xSpreadsheet.getCellByPosition(2, row);
                xCell.setFormula(item.getItemName());


                //***************************************************
                xCell = xSpreadsheet.getCellByPosition(3, row);
                xCell.setFormula(item.getModelNumber());

                xCell = xSpreadsheet.getCellByPosition(4, row);
                xCell.setFormula(item.getArticleNumber());

                xCell = xSpreadsheet.getCellByPosition(5, row);
                xCell.setFormula(item.getItemColor());

                xCell = xSpreadsheet.getCellByPosition(6, row);
                xCell.setFormula("'" + item.getItemSizePrint());


                xCell = xSpreadsheet.getCellByPosition(7, row);
                xCell.setFormula(item.getComposition());

                xCell = xSpreadsheet.getCellByPosition(8, row);
                xCell.setFormula("ОАО \"8 Марта\"");
                //***********************************************

                xCell = xSpreadsheet.getCellByPosition(9, row);
                xCell.setFormula("ОАО \"8 Марта\"");

                xCell = xSpreadsheet.getCellByPosition(10, row);
                xCell.setFormula("РБ");

                xCell = xSpreadsheet.getCellByPosition(11, row);
                xCell.setFormula(item.getTnvedCode());

                xCell = xSpreadsheet.getCellByPosition(12, row);
                xCell.setFormula(item.getMeasureUnit());


                xCell = xSpreadsheet.getCellByPosition(13, row);
                xCell.setValue(item.getAmountPrint());

                xCell = xSpreadsheet.getCellByPosition(14, row);
                xCell.setValue(item.getAccountingPrice());

                xCell = xSpreadsheet.getCellByPosition(15, row);
                xCell.setValue(item.getAccountingPrice());

                xCell = xSpreadsheet.getCellByPosition(16, row);
                xCell.setValue(0);

                xCell = xSpreadsheet.getCellByPosition(17, row);
                xCell.setValue(saleDocumentBase.getDiscountValue());

                xCell = xSpreadsheet.getCellByPosition(18, row);
                xCell.setValue(item.getValuePrice());

                xCell = xSpreadsheet.getCellByPosition(19, row);
                xCell.setValue(item.getValuePrice());

                xCell = xSpreadsheet.getCellByPosition(20, row);
                xCell.setValue(0);

                xCell = xSpreadsheet.getCellByPosition(21, row);
                xCell.setValue(item.getValueVAT());

                xCell = xSpreadsheet.getCellByPosition(22, row);
                xCell.setValue(item.getValueSumCostAndVat());

                xCell = xSpreadsheet.getCellByPosition(23, row);
                xCell.setValue(item.getValueSumCostAndVat());

                xCell = xSpreadsheet.getCellByPosition(34, row);
                xCell.setValue(item.getWeight());
                xCell = xSpreadsheet.getCellByPosition(38, row);
                xCell.setFormula("средний");
                xCell = xSpreadsheet.getCellByPosition(39, row);
                xCell.setValue(50);

                row++;

/*                XCellRange xCellRange = xSpreadsheet
                        .getCellRangeByPosition(0, row - 1, 0,
                                row - 1);
                setRowHeight(xCellRange, 1500);*/
                rowFormat(xSpreadsheet, row - 1, 40, 8);
            }

            xCell = xSpreadsheet.getCellByPosition(0, row + 2);
            xCell.setFormula("ПОКУПАТЕЛЬ ");
            xPropSet = UnoRuntime
                    .queryInterface(com.sun.star.beans.XPropertySet.class,
                            xCell);
            xPropSet.setPropertyValue("HoriJustify", CellHoriJustify.LEFT);


            xCell = xSpreadsheet.getCellByPosition(0, row + 3);
            xCell.setFormula("Иностранное унитарное предприятие \"БелВиллесден\"");

            xCell = xSpreadsheet.getCellByPosition(0, row + 4);
            xCell.setFormula("г.Минск, пер.Асаналиева, 3, ком.20");

            xCell = xSpreadsheet.getCellByPosition(0, row + 5);
            xCell.setFormula("Р/с BY18PJCB30120003551000000933  в ОАО \"Приорбанк\" ЦБУ 100 г. Минск, ул. Радиальная, 38а код PJCBBY2X");

            xCell = xSpreadsheet.getCellByPosition(0, row + 6);
            xCell.setFormula("адрес: 220070, г.Минск, ул. Радиальная, 38а");

            xCell = xSpreadsheet.getCellByPosition(0, row + 7);
            xCell.setFormula("УНП: 800001064");

            xCell = xSpreadsheet.getCellByPosition(0, row + 10);
            xCell.setFormula("От имени покупателя");

            xCell = xSpreadsheet.getCellByPosition(0, row + 12);
            xCell.setFormula("Начальник КУ");

            xCell = xSpreadsheet.getCellByPosition(2, row + 12);
            xCell.setFormula("__________________/Ефимов В.И./");

            xCell = xSpreadsheet.getCellByPosition(0, row + 14);
            xCell.setFormula("Менеджер группы");

            xCell = xSpreadsheet.getCellByPosition(2, row + 14);
            xCell.setFormula("__________________/Иващенко А.С./");

            xCell = xSpreadsheet.getCellByPosition(0, row + 16);
            xCell.setFormula("Главный специалист");

            xCell = xSpreadsheet.getCellByPosition(2, row + 16);
            xCell.setFormula("__________________/Киселева И.А./");

            xCell = xSpreadsheet.getCellByPosition(0, row + 18);
            xCell.setFormula("Ведущий специалист");

            xCell = xSpreadsheet.getCellByPosition(2, row + 18);
            xCell.setFormula("__________________/Володченко Т.А./");

            ///***************************************************************************************
            xCell = xSpreadsheet.getCellByPosition(11, row + 2);
            xCell.setFormula("ПОСТАВЩИК ");
            xPropSet = UnoRuntime
                    .queryInterface(com.sun.star.beans.XPropertySet.class,
                            xCell);
            xPropSet.setPropertyValue("HoriJustify", CellHoriJustify.LEFT);

            xCell = xSpreadsheet.getCellByPosition(11, row + 3);
            xCell.setFormula(getDataAsString("SENDER_NAME"));

            xCell = xSpreadsheet.getCellByPosition(11, row + 4);
            xCell.setFormula("246708, РБ, г.Гомель, ул.Советская, 41");

            xCell = xSpreadsheet.getCellByPosition(11, row + 5);
            xCell.setFormula("Р/сч: BY 82BLBB30120400078265001001 в Дирекции ОАО \"Белинвестбанк\" БИК BLBBBY2X");

            xCell = xSpreadsheet.getCellByPosition(11, row + 6);
            xCell.setFormula("адрес: 246022, г.Гомель, ул. Советская, 48");

            xCell = xSpreadsheet.getCellByPosition(11, row + 7);
            xCell.setFormula("УНП: 400078265, ОКПО: 00311935");

            xCell = xSpreadsheet.getCellByPosition(11, row + 10);
            xCell.setFormula("От имени Поставщика");

            xCell = xSpreadsheet.getCellByPosition(11, row + 12);
            xCell.setFormula("Начальник отдела сбыта     _____________/Кретова Е.А.");

            xCell = xSpreadsheet.getCellByPosition(11, row + 14);
            xCell.setFormula("Начальник ПЭО                  _____________/Кучева Н.В.");

            xPropSet = UnoRuntime
                    .queryInterface(com.sun.star.beans.XPropertySet.class,
                            xCell);

            XCellRange xCellRange = xSpreadsheet
                    .getCellRangeByPosition(0, row + 2, 28, row + 20);
            xPropSet = UnoRuntime
                    .queryInterface(com.sun.star.beans.XPropertySet.class,
                            xCellRange);
            xPropSet.setPropertyValue("CharHeight", 10);

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
