package by.march8.ecs.application.modules.warehouse.external.shipping.reports;

import by.gomel.freedev.ucframework.uccore.report.model.ReportProperties;
import by.march8.api.utils.DateUtils;
import by.march8.ecs.application.modules.warehouse.external.shipping.model.SaleDocumentReport;
import by.march8.entities.readonly.AddressEntity;
import by.march8.entities.readonly.ContractorEntity;
import com.sun.star.container.XNamed;
import com.sun.star.lang.XComponent;
import com.sun.star.sheet.XSpreadsheet;
import com.sun.star.sheet.XSpreadsheetDocument;
import com.sun.star.sheet.XSpreadsheets;
import com.sun.star.table.XCell;
import com.sun.star.uno.UnoRuntime;
import dept.ves.model.ProductItemOVES;

import java.util.Date;
import java.util.Iterator;
import java.util.Map;

/**
 * @author Andy 22.01.2019 - 8:24.
 */
public class InvoiceOVESReport extends AbstractInvoiceReport {
    private Map<String, ProductItemOVES> data;

    public InvoiceOVESReport(Map<String, ProductItemOVES> map, SaleDocumentReport template) {
        super();
        data = map;
        documentReport = template;

        saleDocumentBase = documentReport.getDocument();
        detailList = documentReport.getDetailList();
        detailMap = documentReport.getDetailMap();

        createCustom();
    }

    @Override
    ReportProperties prepareProperties(ReportProperties properties) {
        // Устанавливаем имя файла шаблона
        properties.setBlankName("invoice_VES_" + DateUtils.getNormalDateTimeFormatPlus(new Date()));
        properties.getTemplate().setTemplateName("invoiceOVES.ots");
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

            xNamed.setName("Счет-фактура");


            XCell xCell = xSpreadsheet.getCellByPosition(1, 7);
            xCell.setFormula(getDataAsString("RECIPIENT_NAME"));

            xCell = xSpreadsheet.getCellByPosition(1, 8);
            xCell.setFormula(getDataAsString("RECIPIENT_ADDRESS"));

            xCell = xSpreadsheet.getCellByPosition(1, 9);
            xCell.setFormula(getDataAsString("RECIPIENT_NAME"));

            xCell = xSpreadsheet.getCellByPosition(1, 10);
            xCell.setFormula(getDataAsString("UNLOADING_ADDRESS"));

            xCell = xSpreadsheet.getCellByPosition(1, 11);
            xCell.setFormula(getDataAsString("CONTRACT_NAME"));

            xCell = xSpreadsheet.getCellByPosition(1, 12);
            xCell.setFormula(getDataAsString("NOTE"));

            int row = 14;

            // Пробегаемся по мапе и заполняем новый список из мапы
            Iterator it = data.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry) it.next();
                ProductItemOVES item = (ProductItemOVES) pair.getValue();

                mergeCellRange(xSpreadsheet, 0, row, 1, row);

                // Наименование
                xCell = xSpreadsheet.getCellByPosition(0, row);
                xCell.setFormula(item.getName());

                // ТНВЭД
                xCell = xSpreadsheet.getCellByPosition(2, row);
                xCell.setFormula(item.getCode());

                // Сырье
                xCell = xSpreadsheet.getCellByPosition(3, row);
                xCell.setFormula("");

                // Грузоместа
                xCell = xSpreadsheet.getCellByPosition(4, row);
                xCell.setValue(item.getPlaceCount());

                // Кол-во
                xCell = xSpreadsheet.getCellByPosition(5, row);
                xCell.setValue(item.getAmount());

                // Брутто
                xCell = xSpreadsheet.getCellByPosition(6, row);
                xCell.setFormula("=H" + (row + 1) + "+E" + (row + 1) + "/100");
                //xCell.setValue(item.getWeight() + item.getAmount() / 100);

                // Нетто
                xCell = xSpreadsheet.getCellByPosition(7, row);
                xCell.setValue(item.getWeight());

                // Сумма
                xCell = xSpreadsheet.getCellByPosition(8, row);
                xCell.setValue(item.getCost());

                rowFormat(xSpreadsheet, row, 9, 9);
                row++;
            }

            xCell = xSpreadsheet.getCellByPosition(3, row);
            xCell.setFormula("ИТОГО:");

            xCell = xSpreadsheet.getCellByPosition(4, row);
            xCell.setFormula("=SUM(E" + String.valueOf(15) + ":E"
                    + String.valueOf(row) + ")");

            xCell = xSpreadsheet.getCellByPosition(5, row);
            xCell.setFormula("=SUM(F" + String.valueOf(15) + ":F"
                    + String.valueOf(row) + ")");

            xCell = xSpreadsheet.getCellByPosition(6, row);
            xCell.setFormula("=SUM(G" + String.valueOf(15) + ":G"
                    + String.valueOf(row) + ")");

            xCell = xSpreadsheet.getCellByPosition(7, row);
            xCell.setFormula("=SUM(H" + String.valueOf(15) + ":H"
                    + String.valueOf(row) + ")");

            xCell = xSpreadsheet.getCellByPosition(8, row);
            xCell.setFormula("=SUM(I" + String.valueOf(15) + ":I"
                    + String.valueOf(row) + ")");


            xCell = xSpreadsheet.getCellByPosition(8, 8);
            xCell.setFormula("=G" + String.valueOf(row + 1));

            xCell = xSpreadsheet.getCellByPosition(8, 9);
            xCell.setFormula("=H" + String.valueOf(row + 1));

            xCell = xSpreadsheet.getCellByPosition(8, 10);
            xCell.setFormula("");

            xCell = xSpreadsheet.getCellByPosition(7, 1);
            xCell.setFormula("Счет-фактура № ХХ от ХХХХХ г.");

            rowFormat(xSpreadsheet, row, 9, 11);

            row += 2;

            xCell = xSpreadsheet.getCellByPosition(1, row);
            xCell.setFormula("Руководитель _____________________________________");


            mergeCellRange(xSpreadsheet, 4, row, 8, row);
            xCell = xSpreadsheet.getCellByPosition(4, row);
            xCell.setFormula("Гл.  бухгалтер ____________________ / Лапезо И.В.___________");


            //**********************************************************************************
            // ПРИЛОЖЕНИЕ
            //**********************************************************************************

            sheet = xSpreadsheets.getByName("Лист2");
            xSpreadsheet = UnoRuntime
                    .queryInterface(XSpreadsheet.class, sheet);

            xNamed = UnoRuntime.queryInterface(XNamed.class,
                    xSpreadsheet);

            xNamed.setName("Приложение");

            xCell = xSpreadsheet.getCellByPosition(0, 1);
            xCell.setFormula(getDataAsString("CONTRACT_NAME"));

            row = 6;

            // Пробегаемся по мапе и заполняем новый список из мапы
            it = data.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry) it.next();
                ProductItemOVES item = (ProductItemOVES) pair.getValue();


                // Наименование
                xCell = xSpreadsheet.getCellByPosition(0, row);
                xCell.setFormula(item.getName());

                // ТНВЭД
                xCell = xSpreadsheet.getCellByPosition(1, row);
                xCell.setValue(item.getAmount());

                // Сумма
                xCell = xSpreadsheet.getCellByPosition(2, row);
                xCell.setValue(item.getCost());

                rowFormat(xSpreadsheet, row, 3, 9);
                row++;

            }

            xCell = xSpreadsheet.getCellByPosition(0, row);
            xCell.setFormula("ИТОГО:");

            xCell = xSpreadsheet.getCellByPosition(1, row);
            xCell.setFormula("=SUM(B" + String.valueOf(7) + ":B"
                    + String.valueOf(row) + ")");

            xCell = xSpreadsheet.getCellByPosition(2, row);
            xCell.setFormula("=SUM(C" + String.valueOf(7) + ":C"
                    + String.valueOf(row) + ")");

            xCell = xSpreadsheet.getCellByPosition(0, 0);
            xCell.setFormula("Приложение № ХХ от ХХ.ХХ.ХXXX г.");

            rowFormat(xSpreadsheet, row, 3, 11);


            row += 2;

            mergeCellRange(xSpreadsheet, 1, row, 2, row);
            mergeCellRange(xSpreadsheet, 1, row + 1, 2, row + 1);
            mergeCellRange(xSpreadsheet, 1, row + 2, 2, row + 2);

            ContractorEntity sender = (ContractorEntity) detailMap.get("CONTRACTOR_SENDER");
            if (sender != null) {
                AddressEntity addressSender = sender.getLegalAddress();
                if (addressSender != null) {


                    xCell = xSpreadsheet.getCellByPosition(0, row);
                    xCell.setFormula(sender.getName());

                    xCell = xSpreadsheet.getCellByPosition(0, row + 1);
                    xCell.setFormula("РБ, " + addressSender.getCity());

                    xCell = xSpreadsheet.getCellByPosition(0, row + 2);
                    xCell.setFormula(addressSender.getStreet() + ", д." + addressSender.getHouseNumber());
                }
            }

            ContractorEntity recipient = (ContractorEntity) detailMap.get("CONTRACTOR_RECIPIENT");
            if (recipient != null) {
                AddressEntity address = recipient.getLegalAddress();
                if (address != null) {
                    xCell = xSpreadsheet.getCellByPosition(1, row);
                    xCell.setFormula(recipient.getName());

                    xCell = xSpreadsheet.getCellByPosition(1, row + 1);
                    xCell.setFormula("РФ, " + address.getCity());

                    xCell = xSpreadsheet.getCellByPosition(1, row + 2);
                    xCell.setFormula(address.getStreet() + ", д." + address.getHouseNumber());
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
