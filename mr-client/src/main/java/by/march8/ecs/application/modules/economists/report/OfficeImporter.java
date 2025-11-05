package by.march8.ecs.application.modules.economists.report;

import by.march8.entities.classifier.RemainPriceListDetailItem;
import com.sun.star.lang.XComponent;
import com.sun.star.sheet.XSpreadsheet;
import com.sun.star.sheet.XSpreadsheetDocument;
import com.sun.star.sheet.XSpreadsheets;
import com.sun.star.table.XCell;
import com.sun.star.uno.UnoRuntime;
import workOO.OO_new;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Andy 16.03.2017.
 */
public class OfficeImporter extends OO_new {
    private ArrayList<RemainPriceListDetailItem> priceListDetail;
    private XComponent currentDocument;

    public OfficeImporter(final String file) {
        try {
            connect();
            currentDocument = openDocumentSMPL(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public List<RemainPriceListDetailItem> getPriceListDetail() {
        ArrayList<RemainPriceListDetailItem> result = new ArrayList<>();

        // ПОдключения к ОО не произошло
        if (currentDocument == null) {
            return null;
        }

        try {
            XSpreadsheetDocument xSpreadsheetDocument = UnoRuntime.queryInterface(XSpreadsheetDocument.class, currentDocument);
            XSpreadsheets xSpreadsheets = xSpreadsheetDocument.getSheets();
            Object sheet = xSpreadsheets.getByName("Лист1");
            XSpreadsheet xSpreadsheet = UnoRuntime.queryInterface(XSpreadsheet.class, sheet);

            int startLine = 3;

            XCell xCell = xSpreadsheet.getCellByPosition(2, startLine);
            String value = xCell.getFormula();
            while (!value.trim().equals("")) {

                RemainPriceListDetailItem item = new RemainPriceListDetailItem();

                item.setArticleNumber(value.toUpperCase());

                xCell = xSpreadsheet.getCellByPosition(3, startLine);
                item.setItemSize(xCell.getFormula());

                xCell = xSpreadsheet.getCellByPosition(4, startLine);
                item.setPrice1stGrade(Double.valueOf(xCell.getFormula()));

                xCell = xSpreadsheet.getCellByPosition(5, startLine);
                item.setPrice2ndGrade(Double.valueOf(xCell.getFormula()));

                xCell = xSpreadsheet.getCellByPosition(6, startLine);
                item.setVatValue(Integer.valueOf(xCell.getFormula()));

                result.add(item);

                startLine++;
                xCell = xSpreadsheet.getCellByPosition(2, startLine);
                value = xCell.getFormula();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        closeDocument(currentDocument, true);
        return result;
    }
}
