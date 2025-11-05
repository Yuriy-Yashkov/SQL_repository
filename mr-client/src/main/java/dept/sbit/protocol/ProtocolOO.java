package dept.sbit.protocol;

import by.march8.ecs.framework.common.LogCrutch;
import com.sun.star.lang.XComponent;
import com.sun.star.sheet.XSpreadsheet;
import com.sun.star.sheet.XSpreadsheetDocument;
import com.sun.star.sheet.XSpreadsheets;
import com.sun.star.table.XCell;
import com.sun.star.uno.UnoRuntime;
import workOO.OO_new;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import java.util.Map;
import java.util.Vector;

//import org.apache.log4j.Logger;

/**
 *
 * @author lidashka
 */
public class ProtocolOO extends OO_new {
    private static final LogCrutch log = new LogCrutch();
    static String path;
    static int col;
    static String nameTamplates;
    static Vector dataTamplates;
    static DefaultTableModel tModel;
    static TableColumnModel tModelCol;

    public ProtocolOO(String path, int col) {
        ProtocolOO.path = path;
        ProtocolOO.col = col;
    }

    public ProtocolOO(String nameReport, Vector dataReport) {
        nameTamplates = nameReport;
        dataTamplates = dataReport;
    }

    public ProtocolOO(String nameReport, DefaultTableModel tm, TableColumnModel tc) {
        tModel = tm;
        tModelCol = tc;
        nameTamplates = nameReport;
    }

    public void createReport(String nameTamplates) throws Exception {
        try {
            connect();
            XComponent currentDocument = openDocumentOld("Templates/" + nameTamplates);

            if (nameTamplates.equals("DefaultTableBookFormatCheck.ots")) {
                fildTableDefaultCheck(currentDocument);
            } else if (nameTamplates.equals("SbitProtocolClientZayavka.ots")) {
                fildTableProtocolClientZayavka(currentDocument);
            } else if (nameTamplates.equals("SbitDetalProtocolClientZayavka.ots")) {
                fildTableProtocolClientZayavka(currentDocument);
            } else if (nameTamplates.equals("SbitDetalProtocolGreenClientZayavka.ots")) {
                fildTableProtocolGreenClientZayavka(currentDocument);
            }

            JOptionPane.showMessageDialog(null,
                    "Отчёт сформирован",
                    "Отчёт сформирован",
                    JOptionPane.INFORMATION_MESSAGE);

        } catch (java.lang.Exception e) {
            System.out.println(e.getMessage());
            throw new Exception("Ошибка при формировании отчёта ", e);
        }
    }

    public Vector openReport(ProtocolForm dialog) throws Exception {
        Vector list = new Vector();
        try {
            connect();
            XComponent currentDocument = openDocument(path);

            dialog.toFront();

            if (col > 1 || col < 10) {

                list = openZayavka(currentDocument);

                closeDocument(currentDocument, false);
            } else
                throw new Exception();

        } catch (java.lang.Exception e) {
            list = new Vector();
            System.out.println(e.getMessage());
            throw new Exception("Ошибка просмотра отчёта ", e);
        }
        return list;
    }

    private Vector openZayavka(XComponent currentDocument) throws Exception {
        Vector list = new Vector();

        try {

            int nCol;
            int nRow;
            int k;

            XSpreadsheetDocument xSpreadsheetDocument = UnoRuntime.queryInterface(XSpreadsheetDocument.class, currentDocument);
            XSpreadsheets xSpreadsheets = xSpreadsheetDocument.getSheets();
            Object sheet = xSpreadsheets.getByName("Лист1");
            XSpreadsheet xSpreadsheet = UnoRuntime.queryInterface(XSpreadsheet.class, sheet);
            com.sun.star.beans.XPropertySet xPropSet = null;
            XCell xCell = null;

            com.sun.star.table.BorderLine aLine = new com.sun.star.table.BorderLine();
            aLine.Color = 0x000000;
            aLine.InnerLineWidth = aLine.LineDistance = 0;
            aLine.OuterLineWidth = 1;
            com.sun.star.table.TableBorder aBorder = new com.sun.star.table.TableBorder();
            aBorder.TopLine = aBorder.BottomLine = aBorder.LeftLine = aBorder.RightLine = aLine;
            aBorder.IsTopLineValid = aBorder.IsBottomLineValid = true;
            aBorder.IsLeftLineValid = aBorder.IsRightLineValid = true;

            nCol = col;
            nRow = 0;
            k = 0;

            for (nRow = 0; nRow < 500; nRow++) {
                xCell = xSpreadsheet.getCellByPosition(nCol, nRow);
                xCell.getFormula();

                try {

                    if (!xCell.getFormula().equals("") && xCell.getFormula() != null) {
                        k = 0;

                        if (!xCell.getFormula().toString().trim().equals(""))
                            list.add(xCell.getFormula().toString().trim().toUpperCase());

                    } else {
                        k++;

                        if (k > 10) {
                            break;
                        }
                    }

                } catch (Exception e) {
                    list = new Vector();
                    System.out.println(e.getMessage());
                    throw new Exception("Отчет распознан с ошибкой!  ", e);
                }
            }

        } catch (Exception e) {
            list = new Vector();
            log.error("Ошибка при чтении отчёта", e);
            throw new Exception("Ошибка при чтении отчёта ", e);
        }
        return list;
    }

    private void fildTableProtocolClientZayavka(XComponent currentDocument) throws Exception {
        try {
            XSpreadsheetDocument xSpreadsheetDocument = UnoRuntime.queryInterface(XSpreadsheetDocument.class, currentDocument);
            XSpreadsheets xSpreadsheets = xSpreadsheetDocument.getSheets();
            Object sheet = xSpreadsheets.getByName("Лист1");
            XSpreadsheet xSpreadsheet = UnoRuntime.queryInterface(XSpreadsheet.class, sheet);
            com.sun.star.beans.XPropertySet xPropSet = null;
            com.sun.star.table.BorderLine aLine = new com.sun.star.table.BorderLine();
            aLine.Color = 0x000000;
            aLine.InnerLineWidth = aLine.LineDistance = 0;
            aLine.OuterLineWidth = 1;
            com.sun.star.table.TableBorder aBorder = new com.sun.star.table.TableBorder();
            aBorder.TopLine = aBorder.BottomLine = aBorder.LeftLine = aBorder.RightLine = aLine;
            aBorder.IsTopLineValid = aBorder.IsBottomLineValid = true;
            aBorder.IsLeftLineValid = aBorder.IsRightLineValid = true;

            XCell xCell = xSpreadsheet.getCellByPosition(1, 1);

            //----------------Заполнение шапки документа
            xCell.setFormula(nameTamplates);

            boolean flag = false;

            int nCol = 1;
            int nRow = 4;
            int kol = 1;

            //----------------Заполнение тела документа
            for (Object row : dataTamplates) {

                xCell = xSpreadsheet.getCellByPosition(nCol++, nRow);
                xCell.setValue(kol++);
                xPropSet = UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
                xPropSet.setPropertyValue("TableBorder", aBorder);

                Vector vec = (Vector) row;
                for (int i = 3; i < vec.size(); i++) {
                    xCell = xSpreadsheet.getCellByPosition(nCol++, nRow);
                    xCell.setFormula(((Vector) row).get(i) != null ? ((Vector) row).get(i).toString() : "");
                    xPropSet = UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
                    xPropSet.setPropertyValue("TableBorder", aBorder);
                }

                nCol = 1;
                nRow++;
            }
        } catch (java.lang.Exception e) {
            log.error("Ошибка при заполнении отчёта.", e);
            throw new Exception("Ошибка при заполнении отчёта.", e);
        }
    }

    private void fildTableDefaultCheck(XComponent currentDocument) throws Exception {
        try {
            XSpreadsheetDocument xSpreadsheetDocument = UnoRuntime.queryInterface(XSpreadsheetDocument.class, currentDocument);
            XSpreadsheets xSpreadsheets = xSpreadsheetDocument.getSheets();
            Object sheet = xSpreadsheets.getByName("Лист1");
            XSpreadsheet xSpreadsheet = UnoRuntime.queryInterface(XSpreadsheet.class, sheet);
            com.sun.star.beans.XPropertySet xPropSet = null;
            com.sun.star.table.BorderLine aLine = new com.sun.star.table.BorderLine();
            aLine.Color = 0x000000;
            aLine.InnerLineWidth = aLine.LineDistance = 0;
            aLine.OuterLineWidth = 1;
            com.sun.star.table.TableBorder aBorder = new com.sun.star.table.TableBorder();
            aBorder.TopLine = aBorder.BottomLine = aBorder.LeftLine = aBorder.RightLine = aLine;
            aBorder.IsTopLineValid = aBorder.IsBottomLineValid = true;
            aBorder.IsLeftLineValid = aBorder.IsRightLineValid = true;

            XCell xCell = xSpreadsheet.getCellByPosition(0, 0);

            //----------------Заполнение шапки документа
            xCell.setFormula(nameTamplates);

            boolean flag = false;

            int nCol = 0;
            for (int i = 1; i < tModel.getColumnCount(); i++) {
                if (tModelCol.getColumn(i).getWidth() > 0) {
                    xCell = xSpreadsheet.getCellByPosition(nCol++, 1);
                    xCell.setFormula(tModel.getColumnName(i));
                    xPropSet = UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
                    xPropSet.setPropertyValue("TableBorder", aBorder);
                }
            }

            //----------------Заполнение тела документа
            for (Object row : tModel.getDataVector()) {
                if (((Vector) row).get(0).toString().equals("true")) {
                    flag = true;
                    break;
                }
            }

            nCol = 0;
            int nRow = 2;
            for (Object row : tModel.getDataVector()) {
                if (flag) {
                    if (((Vector) row).get(0).toString().equals("true")) {
                        for (int i = 1; i < tModel.getColumnCount(); i++) {
                            if (tModelCol.getColumn(i).getWidth() > 0) {
                                xCell = xSpreadsheet.getCellByPosition(nCol++, nRow);
                                xCell.setFormula(((Vector) row).get(i) != null ? ((Vector) row).get(i).toString() : "");
                                xPropSet = UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
                                xPropSet.setPropertyValue("TableBorder", aBorder);
                            }
                        }
                        nCol = 0;
                        nRow++;
                    }
                } else {
                    for (int i = 1; i < tModel.getColumnCount(); i++) {
                        if (tModelCol.getColumn(i).getWidth() > 0) {
                            xCell = xSpreadsheet.getCellByPosition(nCol++, nRow);
                            xCell.setFormula(((Vector) row).get(i) != null ? ((Vector) row).get(i).toString() : "");
                            xPropSet = UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
                            xPropSet.setPropertyValue("TableBorder", aBorder);
                        }
                    }
                    nCol = 0;
                    nRow++;
                }
            }
        } catch (java.lang.Exception e) {
            log.error("Ошибка при заполнении отчёта.", e);
            throw new Exception("Ошибка при заполнении отчёта.", e);
        }
    }

    private void fildTableProtocolGreenClientZayavka(XComponent currentDocument) throws Exception {
        try {
            XSpreadsheetDocument xSpreadsheetDocument = UnoRuntime.queryInterface(XSpreadsheetDocument.class, currentDocument);
            XSpreadsheets xSpreadsheets = xSpreadsheetDocument.getSheets();
            Object sheet = xSpreadsheets.getByName("Лист1");
            XSpreadsheet xSpreadsheet = UnoRuntime.queryInterface(XSpreadsheet.class, sheet);
            com.sun.star.beans.XPropertySet xPropSet;
            com.sun.star.table.BorderLine aLine = new com.sun.star.table.BorderLine();
            aLine.Color = 0x000000;
            aLine.InnerLineWidth = aLine.LineDistance = 0;
            aLine.OuterLineWidth = 1;
            com.sun.star.table.TableBorder aBorder = new com.sun.star.table.TableBorder();
            aBorder.TopLine = aBorder.BottomLine = aBorder.LeftLine = aBorder.RightLine = aLine;
            aBorder.IsTopLineValid = aBorder.IsBottomLineValid = true;
            aBorder.IsLeftLineValid = aBorder.IsRightLineValid = true;

            XCell xCell = xSpreadsheet.getCellByPosition(1, 1);

            //----------------Заполнение шапки документа
            xCell.setFormula(nameTamplates);
            //----------------Заполнение тела документа
            int startRow = 15;
            for (Object row : dataTamplates) {
                Map<Integer, Object> data = (Map<Integer, Object>) row;
                for (Map.Entry<Integer, Object> el : data.entrySet()) {
                    xCell = xSpreadsheet.getCellByPosition(el.getKey(), startRow);
                    xCell.setFormula(el.getValue() != null ? el.getValue().toString() : "");
                    xPropSet = UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
                    xPropSet.setPropertyValue("TableBorder", aBorder);
                }
                startRow++;
            }
        } catch (java.lang.Exception e) {
            log.error("Ошибка при заполнении отчёта.", e);
            throw new Exception("Ошибка при заполнении отчёта.", e);
        }
    }
}
