package by.march8.ecs.application.modules.warehouse.external.shipping.reports;

import by.gomel.freedev.ucframework.uccore.report.model.ReportProperties;
import by.march8.api.utils.DateUtils;
import by.march8.ecs.application.modules.warehouse.external.shipping.model.SaleDocumentSet;
import by.march8.ecs.application.modules.warehouse.external.shipping.model.SaleDocumentSheet;
import by.march8.entities.readonly.ContractorEntityView;
import com.sun.star.container.XNamed;
import com.sun.star.lang.XComponent;
import com.sun.star.sheet.XSpreadsheet;
import com.sun.star.sheet.XSpreadsheetDocument;
import com.sun.star.sheet.XSpreadsheets;
import com.sun.star.table.XCell;
import com.sun.star.uno.UnoRuntime;

import java.util.Date;
import java.util.List;

/**
 * @author Andy 22.01.2019 - 8:24.
 */
public class SaleMonitorReport extends AbstractInvoiceReport {
    private List<SaleDocumentSheet> documents;

    public SaleMonitorReport(List<SaleDocumentSheet> sheets) {
        super();
        documents = sheets;
        createCustom();
    }

    @Override
    ReportProperties prepareProperties(ReportProperties properties) {
        // Устанавливаем имя файла шаблона
        properties.setBlankName("sale_monitor_" + DateUtils.getNormalDateTimeFormatPlus(new Date()));
        properties.getTemplate().setTemplateName("saleMonitor.ots");
        return properties;
    }

    @Override
    boolean populateData(XComponent component) {
        try {
            XSpreadsheetDocument xSpreadsheetDocument = UnoRuntime
                    .queryInterface(XSpreadsheetDocument.class, component);
            XSpreadsheets xSpreadsheets = xSpreadsheetDocument.getSheets();
            Object sheet = xSpreadsheets.getByName("Лист1");
            XSpreadsheet xSpreadsheet = UnoRuntime
                    .queryInterface(XSpreadsheet.class, sheet);

            XNamed xNamed = UnoRuntime.queryInterface(XNamed.class,
                    xSpreadsheet);

            xNamed.setName("Монитор отгрузок");

            XCell xCell = xSpreadsheet.getCellByPosition(2, 0);
            xCell.setFormula("между " + getDataAsString("RECIPIENT_NAME") + " и " + getDataAsString("SENDER_NAME") + "\"");

            int row = 6;


            // Контрагенты
            for (SaleDocumentSheet sheet_ : documents) {
                ContractorEntityView contractor = sheet_.getContractor();
                // Года по контрагенту
                xCell = xSpreadsheet.getCellByPosition(0, row - 1);
                xCell.setFormula("'" + contractor.getName());
                int start = row;
                for (List<SaleDocumentSet> set_ : sheet_.getDocumentSet()) {

                    int column = 2;
                    xCell = xSpreadsheet.getCellByPosition(1, row);
                    xCell.setFormula("Суммы");
                    xCell = xSpreadsheet.getCellByPosition(1, row + 1);
                    xCell.setFormula("Скидки");
                    //
                    mergeCellRange(xSpreadsheet, 0, row, 0, row + 1);
                    setCharHeightForCell(xSpreadsheet.getCellByPosition(0, row - 1), 12.0f);
                    setFontBoldForCellRange(xSpreadsheet, 0, 0, row);

                    // Для каждого месяца
                    for (SaleDocumentSet documentSet : set_) {
                        xCell = xSpreadsheet.getCellByPosition(0, row);
                        xCell.setValue(documentSet.getYear());
                        rowFormatCenterCenter(xCell);
                        // Суммы
                        xCell = xSpreadsheet.getCellByPosition(column, row);
                        xCell.setValue(documentSet.getSalesValue());
                        // Скидки
                        xCell = xSpreadsheet.getCellByPosition(column, row + 1);
                        xCell.setFormula("'" + documentSet.getDiscountList());
                        column++;
                    }

                    xCell = xSpreadsheet.getCellByPosition(14, row);
                    xCell.setFormula("=SUM(C" + (row + 1) + ":N"
                            + (row + 1) + ")");

                    setFullBorderForCell(xSpreadsheet, 0, 14, row);
                    setFullBorderForCell(xSpreadsheet, 0, 14, row + 1);
                    row += 2;
                }

                xCell = xSpreadsheet.getCellByPosition(14, row);
                xCell.setFormula("=SUM(O" + (start + 1) + ":O"
                        + (row) + ")");

                row += 2;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
