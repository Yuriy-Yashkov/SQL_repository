/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dept.nsi;

import by.march8.ecs.framework.common.LogCrutch;
import com.sun.star.container.XNamed;
import com.sun.star.lang.XComponent;
import com.sun.star.sheet.XSpreadsheet;
import com.sun.star.sheet.XSpreadsheetDocument;
import com.sun.star.sheet.XSpreadsheets;
import com.sun.star.table.XCell;
import com.sun.star.uno.UnoRuntime;
import workOO.OO_new;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 *
 * @author user
 */
public class ReporpNsi extends OO_new {

    // private static final Logger log = new Log().getLoger(SkladOO.class);
    private static final LogCrutch log = new LogCrutch();

    List data;
    Map result;
    XCell xCell;

    public ReporpNsi(List hm) {
        data = hm;
    }

    public void checkNdsReport() {

        try {
            connect();
            XComponent currentDocument = openDocumentOld("Templates/nds.ots");
            XSpreadsheetDocument xSpreadsheetDocument = UnoRuntime.queryInterface(XSpreadsheetDocument.class, currentDocument);
            XSpreadsheets xSpreadsheets = xSpreadsheetDocument.getSheets();
            //xSpreadsheets.insertNewByName("Приложение к ТТН №" + dis.get("ttn"), (short)0);
            Object sheet = xSpreadsheets.getByName("Лист1");
            XSpreadsheet xSpreadsheet = UnoRuntime.queryInterface(XSpreadsheet.class, sheet);

            XNamed xNamed = UnoRuntime.queryInterface(XNamed.class, xSpreadsheet);

            com.sun.star.beans.XPropertySet xPropSet = null;
            com.sun.star.table.BorderLine aLine = new com.sun.star.table.BorderLine();
            aLine.Color = 0x000000;
            aLine.InnerLineWidth = aLine.LineDistance = 0;
            aLine.OuterLineWidth = 1;
            com.sun.star.table.TableBorder aBorder = new com.sun.star.table.TableBorder();
            aBorder.BottomLine = aLine;
            aBorder.IsBottomLineValid = true;

            for (int i = 0; i < data.size(); i++) {
                result = (HashMap) data.get(i);
                xCell = xSpreadsheet.getCellByPosition(0, i + 2);
                xCell.setFormula(result.get("sar").toString());
                xCell = xSpreadsheet.getCellByPosition(1, i + 2);
                xCell.setFormula(result.get("nar").toString());
                xCell = xSpreadsheet.getCellByPosition(2, i + 2);
                xCell.setFormula(result.get("nds").toString());
            }

            //saveAsDocument(currentDocument,  + dis.get("ttn").toString()+ ".ods", lParams);
        } catch (Exception ex) {
            System.err.println("checkNdsReport()" + ex);
        }
    }

}
