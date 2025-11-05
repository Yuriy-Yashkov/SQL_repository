package dept.upack.ns;

import by.march8.ecs.framework.common.LogCrutch;
import com.sun.star.lang.XComponent;
import com.sun.star.sheet.XSpreadsheet;
import com.sun.star.sheet.XSpreadsheetDocument;
import com.sun.star.sheet.XSpreadsheets;
import com.sun.star.table.XCell;
import com.sun.star.uno.UnoRuntime;
import workOO.OO_new;

import java.util.Vector;

/**
 *
 * @author vova
 * @date 18.11.2011
 */
public class UpackNSOO extends OO_new {
    // private static final Logger log = new Log().getLoger(UpackNSOO.class);
    private static final LogCrutch log = new LogCrutch();
    private static String nameTamplate;
    Vector vec;
    Vector dopVec;

    /**
     *
     * @param nameReport название отчёта
     * @param v вектор с данными
     * @param dopv дополнительные параметры
     */
    public UpackNSOO(String nameReport, Vector v, Vector dopv) {
        vec = v;
        dopVec = dopv;
        nameTamplate = new String(nameReport);
    }


    public void fildU4otNS(XComponent currentDocument) throws Exception {
        try {
            com.sun.star.beans.XPropertySet xPropSet = null;
            com.sun.star.table.BorderLine aLine = new com.sun.star.table.BorderLine();
            aLine.Color = 0x000000;
            aLine.InnerLineWidth = aLine.LineDistance = 0;
            aLine.OuterLineWidth = 1;
            com.sun.star.table.TableBorder aBorder = new com.sun.star.table.TableBorder();
            aBorder.TopLine = aBorder.BottomLine = aBorder.LeftLine = aBorder.RightLine = aLine;
            aBorder.IsTopLineValid = aBorder.IsBottomLineValid = true;
            aBorder.IsLeftLineValid = aBorder.IsRightLineValid = true;
            aBorder.IsBottomLineValid = true;

            XSpreadsheetDocument xSpreadsheetDocument = UnoRuntime.queryInterface(XSpreadsheetDocument.class, currentDocument);

            XSpreadsheets xSpreadsheets = xSpreadsheetDocument.getSheets();
//формирование листа "Общий"
            Object sheet = xSpreadsheets.getByName("Лист1");
            XSpreadsheet xSpreadsheet = UnoRuntime.queryInterface(XSpreadsheet.class, sheet);

//----------------Заполнение шапки документа
            XCell xCell = xSpreadsheet.getCellByPosition(0, 0);
            xCell.setFormula(nameTamplate);

//----------------Заполнение тела документа

            int nRow = 3;
            for (Object item : vec) {
                for (int i = 0; i < 10; i++) {
                    xCell = xSpreadsheet.getCellByPosition(i, nRow);
                    xCell.setFormula(((Vector) item).get(i).toString());
                    xCell = xSpreadsheet.getCellByPosition(i, nRow);
                    xPropSet = UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
                    xPropSet.setPropertyValue("TableBorder", aBorder);
                }
                nRow++;
            }

            xCell = xSpreadsheet.getCellByPosition(0, nRow);
            xCell.setFormula("ИТОГО:");
            xCell = xSpreadsheet.getCellByPosition(8, nRow);
            xCell.setFormula("=SUM(I4:I" + nRow + ")");

            nRow++;
            xCell = xSpreadsheet.getCellByPosition(0, ++nRow);
            xCell.setFormula("Ответсвенный за переучёт кладовщик ___________:");

            xCell = xSpreadsheet.getCellByPosition(4, nRow);
            xCell.setFormula("ФИО: ___________________");
            nRow++;

        } catch (java.lang.Exception e) {
            System.out.println(e.getMessage());
            log.error("Ошибка при заполнении учёта н/с продукции ", e);
            throw new Exception("Ошибка при заполнении отчёта учёта н/с продукции ", e);
        }
    }

    public void createReport(String nameTamplates) throws Exception {
        try {
            connect();
            XComponent currentDocument = openDocumentOld("Templates/" + nameTamplates);

            if (nameTamplates.equals("УчётНС.ots")) {
                fildU4otNS(currentDocument);
            }

        } catch (java.lang.Exception e) {
            System.out.println(e.getMessage());
            throw new Exception("Ошибка при формировании отчёта ", e);
        }
    }

}