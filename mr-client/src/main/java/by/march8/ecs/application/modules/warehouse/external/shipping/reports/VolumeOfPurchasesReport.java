package by.march8.ecs.application.modules.warehouse.external.shipping.reports;

import by.gomel.freedev.ucframework.uccore.report.model.ReportProperties;
import by.march8.api.utils.DateUtils;
import com.sun.star.container.XNamed;
import com.sun.star.lang.XComponent;
import com.sun.star.sheet.XSpreadsheet;
import com.sun.star.sheet.XSpreadsheetDocument;
import com.sun.star.sheet.XSpreadsheets;
import com.sun.star.table.XCell;
import com.sun.star.uno.UnoRuntime;
import dept.ves.model.VolumeOfPurchases;

import java.util.Date;
import java.util.List;

/**
 * @author Developer on 02.03.2020 9:03
 */
public class VolumeOfPurchasesReport extends AbstractInvoiceReport {

    private List<VolumeOfPurchases> documents;
    private String headerText;

    public VolumeOfPurchasesReport(List<VolumeOfPurchases> sheets, String text) {
        super();
        headerText = text;
        documents = sheets;
        createCustom();

    }

    @Override
    ReportProperties prepareProperties(ReportProperties properties) {
        // Устанавливаем имя файла шаблона
        properties.setBlankName("purchases_monitor_" + DateUtils.getNormalDateTimeFormatPlus(new Date()));
        properties.getTemplate().setTemplateName("volumeofpurchases.ots");
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

            XCell xCell = xSpreadsheet.getCellByPosition(0, 0);
            xCell.setFormula(headerText);

            int row = 3;


            // Контрагенты
            for (VolumeOfPurchases item : documents) {
                xCell = xSpreadsheet.getCellByPosition(0, row);
                xCell.setValue(item.getId());

                xCell = xSpreadsheet.getCellByPosition(1, row);
                xCell.setFormula(item.getContractorName());

                xCell = xSpreadsheet.getCellByPosition(2, row);
                xCell.setValue(item.getContractorCode());

                xCell = xSpreadsheet.getCellByPosition(3, row);
                xCell.setValue(item.getSumPurchases());

                xCell = xSpreadsheet.getCellByPosition(4, row);
                xCell.setValue(item.getDiscountValue());

                rowFormat(xSpreadsheet, row, 5, 10);
                row++;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }
}
