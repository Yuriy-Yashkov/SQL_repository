package by.march8.ecs.application.modules.warehouse.external.shipping.reports;

import by.gomel.freedev.ucframework.uccore.report.model.ReportProperties;
import by.march8.api.utils.DateUtils;
import by.march8.ecs.application.modules.warehouse.external.shipping.dao.WriteOffFormsDB;
import by.march8.ecs.application.modules.warehouse.external.shipping.model.WriteOffFormsItem;
import com.sun.star.lang.XComponent;
import com.sun.star.sheet.XSpreadsheet;
import com.sun.star.sheet.XSpreadsheetDocument;
import com.sun.star.sheet.XSpreadsheets;
import com.sun.star.table.XCell;
import com.sun.star.uno.UnoRuntime;

import java.util.Date;
import java.util.List;

/**
 * @author Andy 07.04.2017.
 */
public class WriteOffFormsReport extends AbstractInvoiceReport {
    private XComponent currentDocument;

    private List<WriteOffFormsItem> list;


    private Date date;

    public WriteOffFormsReport(Date date) {
        super();
        this.date = date;
        // Формируем Коллекцию накладных для РБ за период
        WriteOffFormsDB db = new WriteOffFormsDB();
        list = db.getWriteOffFormsList(DateUtils.getFirstDay(date), DateUtils.getLastDay(date));
        createCustom();
    }


    @Override
    ReportProperties prepareProperties(final ReportProperties properties) {
        // Устанавливаем имя файла шаблона
        properties.setBlankName("writeoff_forms_" + DateUtils.getNormalDateFormat(DateUtils.getDateNow()));
        properties.getTemplate().setTemplateName("WriteOffForms.ots");
        return properties;
    }

    @Override
    boolean populateData(final XComponent component) {
        try {
            XSpreadsheetDocument xSpreadsheetDocument = UnoRuntime
                    .queryInterface(XSpreadsheetDocument.class, component);
            XSpreadsheets xSpreadsheets = xSpreadsheetDocument.getSheets();
            Object sheet = xSpreadsheets.getByName("БСО РБ");
            XSpreadsheet xSpreadsheet = UnoRuntime
                    .queryInterface(XSpreadsheet.class, sheet);

            int startLine = 8;
            int row = startLine;
            XCell xCell;

            String month = DateUtils.getMonthNameByDate(date).toLowerCase();
            String year = DateUtils.getYearAsStringByDate(date).toLowerCase();
            String owner = "Склад готовой продукции (Склад 6)";
            String eventDate = "за " + month + " месяц " + year + " г.";


            xCell = xSpreadsheet.getCellByPosition(0, 4);
            xCell.setFormula(owner);

            xCell = xSpreadsheet.getCellByPosition(0, 6);
            xCell.setFormula(eventDate);

            for (WriteOffFormsItem item : list) {
                if (item != null) {
                    if (item.getExport() == 0) {
                        xCell = xSpreadsheet.getCellByPosition(0, row);
                        xCell.setFormula(DateUtils.getNormalDateFormat(item.getDate()) + "г.");

                        xCell = xSpreadsheet.getCellByPosition(1, row);
                        xCell.setFormula(item.getBlankCode());

                        xCell = xSpreadsheet.getCellByPosition(2, row);
                        xCell.setFormula("'" + item.getNumber());

                        xCell = xSpreadsheet.getCellByPosition(3, row);
                        String clientName = item.getClientName();
                        if (clientName != null) {
                            xCell.setFormula(clientName.trim());
                        }

                        xCell = xSpreadsheet.getCellByPosition(4, row);
                        xCell.setValue(item.getSumm());

                        rowFormat(xSpreadsheet, row, 5, 8);
                        row++;
                    }
                }
            }

            xCell = xSpreadsheet.getCellByPosition(1, row + 2);
            xCell.setFormula("Материально ответственное лицо");

            row = startLine;
            sheet = xSpreadsheets.getByName("БСО Экспорт");
            xSpreadsheet = UnoRuntime
                    .queryInterface(XSpreadsheet.class, sheet);

            xCell = xSpreadsheet.getCellByPosition(0, 4);
            xCell.setFormula(owner);

            xCell = xSpreadsheet.getCellByPosition(0, 6);
            xCell.setFormula(eventDate);

            for (WriteOffFormsItem item : list) {
                if (item != null) {
                    if (item.getExport() == 1) {
                        xCell = xSpreadsheet.getCellByPosition(0, row);
                        xCell.setFormula(DateUtils.getNormalDateFormat(item.getDate()) + "г.");

                        xCell = xSpreadsheet.getCellByPosition(1, row);
                        xCell.setFormula(item.getBlankCode());

                        xCell = xSpreadsheet.getCellByPosition(2, row);
                        xCell.setFormula("'" + item.getNumber());

                        xCell = xSpreadsheet.getCellByPosition(3, row);
                        xCell.setFormula(item.getClientName().trim());

                        xCell = xSpreadsheet.getCellByPosition(4, row);
                        xCell.setValue(item.getSumm());

                        rowFormat(xSpreadsheet, row, 5, 8);
                        row++;
                    }
                }
            }
            xCell = xSpreadsheet.getCellByPosition(1, row + 2);
            xCell.setFormula("Материально ответственное лицо");
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
