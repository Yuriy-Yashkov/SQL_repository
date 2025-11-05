package dept.sklad.ho;

import by.march8.ecs.framework.common.LogCrutch;
import com.sun.star.awt.FontWeight;
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
import java.util.Collections;
import java.util.Vector;

/**
 *
 * @author lidashka
 */
@SuppressWarnings("all")
public class SkladHOOO extends OO_new {
    // private static final Logger log = new Log().getLoger(SkladHOOO.class);
    private static final LogCrutch log = new LogCrutch();
    static String nameTamplates;
    static DefaultTableModel tModel;
    static TableColumnModel tModelCol;
    static Vector vec;
    static Vector data;
    static Vector data1;
    static Vector data2;
    static String date;
    static int type;

    public SkladHOOO(String nameReport, DefaultTableModel tm, TableColumnModel tc) {
        nameTamplates = nameReport;
        tModel = tm;
        tModelCol = tc;
    }

    public SkladHOOO(String nameReport, Vector vec, Vector data, Vector data1, Vector data2, int type) {
        nameTamplates = nameReport;
        SkladHOOO.vec = vec;
        SkladHOOO.data = data;
        SkladHOOO.data1 = data1;
        SkladHOOO.data2 = data2;
        SkladHOOO.type = type;
    }

    public SkladHOOO(String nameReport, Vector vec, Vector data) {
        nameTamplates = nameReport;
        SkladHOOO.vec = vec;
        SkladHOOO.data = data;
    }

    public SkladHOOO(String nameReport, String date, Vector data) {
        this.nameTamplates = nameReport;
        this.date = date;
        this.data = data;
    }

    public void createReport(String nameTamplates) throws Exception {
        try {
            connect();
            XComponent currentDocument = openDocumentOld("Templates/" + nameTamplates);

            if (nameTamplates.equals("DefaultTableBookFormatCheck.ots")) {
                fildTableDefaultCheck(currentDocument);
            } else if (nameTamplates.equals("DefaultTableAlbumFormatCheck.ots")) {
                fildTableDefaultCheck(currentDocument);
            } else if (nameTamplates.equals("SkladHOMap.ots")) {
                fildTableMapCheck(currentDocument);
            } else if (nameTamplates.equals("SkladHOMoveNP.ots")) {
                //Движение фабричного полотна
                fildTableMoveNPCheck(currentDocument);
            } else if (nameTamplates.equals("SkladHOMoveIPGroup.ots")) {
                fildTableMoveIPGCheck(currentDocument);
            } else if (nameTamplates.equals("SkladHOMoveP.ots")) {
                //Движение импортного полотна
                fildTableMoveCheck(currentDocument);
            } else if (nameTamplates.equals("SkladHOMoveVMGroup.ots")) {
                fildTableMoveVMGCheck(currentDocument);
            } else if (nameTamplates.equals("SkladHOMoveVM.ots")) {
                //Движение вс. материалов
                fildTableMoveCheck(currentDocument);
            } else if (nameTamplates.equals("SkladHONak.ots")) {
                fildTableNakCheck(currentDocument);
            } else if (nameTamplates.equals("SkladHOPOIP.ots")) {
                //ПО по движению импортного полотна
                fildTablePOIPCheck(currentDocument);
            } else if (nameTamplates.equals("SkladHOInventory.ots")) {
                //Инвентаризационная опись
                fildTableHOInventory(currentDocument);
            }

            JOptionPane.showMessageDialog(null, "Отчёт сформирован", "Отчёт сформирован", JOptionPane.INFORMATION_MESSAGE);

        } catch (java.lang.Exception e) {
            System.out.println(e.getMessage());
            throw new Exception("Ошибка при формировании отчёта ", e);
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

    //Движение фабричного полотна
    private void fildTableMoveNPCheck(XComponent currentDocument) throws Exception {
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

            int nCol = 1;
            int nRow = 1;
            int k = 1;

            //----------------Заполнение шапки документа
            XCell xCell = xSpreadsheet.getCellByPosition(5, 4);
            xCell.setFormula(vec.get(0).toString());

            xCell = xSpreadsheet.getCellByPosition(11, 4);
            xCell.setFormula(UtilSkladHO.SKLADHO_HACH);

            // xCell = xSpreadsheet.getCellByPosition(14, 4);
            //   xCell.setFormula(vec.get(0).toString());

            //----------------Заполнение документа

            nRow = 9;
            for (Object row : data) {

                xCell = xSpreadsheet.getCellByPosition(1, nRow);
                xCell.setValue(k++);

                for (int i = 0; i < ((Vector) row).size(); i++) {
                    xCell = xSpreadsheet.getCellByPosition(nCol++, nRow);
                    xPropSet = UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
                    xPropSet.setPropertyValue("TableBorder", aBorder);
                    if (i == 2)
                        xCell.setFormula(((Vector) row).get(i) != null ? " '" + ((Vector) row).get(i).toString() : "");
                    else
                        xCell.setFormula(((Vector) row).get(i) != null ? ((Vector) row).get(i).toString() : "");

                }

                xCell = xSpreadsheet.getCellByPosition(nCol++, nRow);
                xPropSet = UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
                xPropSet.setPropertyValue("TableBorder", aBorder);
                xCell.setFormula("= F" + (nRow + 1) + " + G" + (nRow + 1) + " + H" + (nRow + 1) + " - I" + (nRow + 1));

                nRow++;
                nCol = 1;
            }

            xCell = xSpreadsheet.getCellByPosition(1, nRow);
            xCell.setFormula("ИТОГО");
            xPropSet = UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
            xPropSet.setPropertyValue("CharWeight", new Float(FontWeight.BOLD));
            xCell = xSpreadsheet.getCellByPosition(5, nRow);
            xCell.setFormula("=SUM(F" + 9 + ":F" + nRow + ")");
            xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
            xPropSet.setPropertyValue("CharWeight", new Float(FontWeight.BOLD));
            xCell = xSpreadsheet.getCellByPosition(6, nRow);
            xCell.setFormula("=SUM(G" + 9 + ":G" + nRow + ")");
            xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
            xPropSet.setPropertyValue("CharWeight", new Float(FontWeight.BOLD));
            xCell = xSpreadsheet.getCellByPosition(7, nRow);
            xCell.setFormula("=SUM(H" + 9 + ":H" + nRow + ")");
            xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
            xPropSet.setPropertyValue("CharWeight", new Float(FontWeight.BOLD));
            xCell = xSpreadsheet.getCellByPosition(8, nRow);
            xCell.setFormula("=SUM(I" + 9 + ":I" + nRow + ")");
            xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
            xPropSet.setPropertyValue("CharWeight", new Float(FontWeight.BOLD));
            xCell = xSpreadsheet.getCellByPosition(9, nRow);
            xCell.setFormula("=SUM(J" + 9 + ":J" + nRow + ")");
            xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
            xPropSet.setPropertyValue("CharWeight", new Float(FontWeight.BOLD));
            xCell = xSpreadsheet.getCellByPosition(10, nRow);
            xCell.setFormula("=SUM(K" + 9 + ":K" + nRow + ")");
            xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
            xPropSet.setPropertyValue("CharWeight", new Float(FontWeight.BOLD));
            xCell = xSpreadsheet.getCellByPosition(11, nRow);
            xCell.setFormula("=SUM(L" + 9 + ":L" + nRow + ")");
            xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
            xPropSet.setPropertyValue("CharWeight", new Float(FontWeight.BOLD));
            xCell = xSpreadsheet.getCellByPosition(12, nRow);
            xCell.setFormula("=SUM(M" + 9 + ":M" + nRow + ")");
            xPropSet = UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
            xPropSet.setPropertyValue("CharWeight", new Float(FontWeight.BOLD));
            nRow++;

            nRow++;
            xCell = xSpreadsheet.getCellByPosition(5, nRow);
            xCell.setFormula("Сделал отчет кладовщик:");
            xPropSet = UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
            xPropSet.setPropertyValue("CharWeight", new Float(FontWeight.BOLD));
            xCell = xSpreadsheet.getCellByPosition(10, nRow++);
            xCell.setFormula(UtilSkladHO.SKLADHO_CLAD);
            xPropSet = UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
            xPropSet.setPropertyValue("CharWeight", new Float(FontWeight.BOLD));

            xCell = xSpreadsheet.getCellByPosition(5, nRow);
            xCell.setFormula("Проверил отчет бухгалтер:");
            xPropSet = UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
            xPropSet.setPropertyValue("CharWeight", new Float(FontWeight.BOLD));
            xCell = xSpreadsheet.getCellByPosition(10, nRow++);
            xCell.setFormula(UtilSkladHO.SKLADHO_BUH);
            xPropSet = UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
            xPropSet.setPropertyValue("CharWeight", new Float(FontWeight.BOLD));
            
            /*
            xCell = xSpreadsheet.getCellByPosition(5, nRow);
            xCell.setFormula("Утвердил начальник отдела:");
            xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
            xPropSet.setPropertyValue("CharWeight", new Float(FontWeight.BOLD));
            xCell = xSpreadsheet.getCellByPosition(10, nRow++);
            xCell.setFormula(UtilSkladHO.SKLADHO_HACH); 
            xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
            xPropSet.setPropertyValue("CharWeight", new Float(FontWeight.BOLD));
            */

        } catch (java.lang.Exception e) {
            log.error("Ошибка при заполнении отчёта.", e);
            throw new Exception("Ошибка при заполнении отчёта.", e);
        }
    }

    private void fildTableMoveIPGCheck(XComponent currentDocument) throws Exception {
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

            int nCol = 1;
            int nRow = 1;
            int k = 1;

            //----------------Заполнение шапки документа
            XCell xCell = xSpreadsheet.getCellByPosition(6, 4);
            xCell.setFormula(vec.get(0).toString());

            xCell = xSpreadsheet.getCellByPosition(18, 4);
            xCell.setFormula(UtilSkladHO.SKLADHO_HACH);

            // xCell = xSpreadsheet.getCellByPosition(18, 4);
            //   xCell.setFormula(vec.get(0).toString());

            //----------------Заполнение документа

            nRow = 9;
            for (Object row : data) {

                xCell = xSpreadsheet.getCellByPosition(1, nRow);
                xCell.setValue(k++);

                for (int i = 0; i < ((Vector) row).size(); i++) {
                    xCell = xSpreadsheet.getCellByPosition(nCol++, nRow);
                    xPropSet = UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
                    xPropSet.setPropertyValue("TableBorder", aBorder);
                    if (i == 1)
                        xCell.setFormula(((Vector) row).get(i) != null ? " '" + ((Vector) row).get(i).toString() : "");
                    else
                        xCell.setFormula(((Vector) row).get(i) != null ? ((Vector) row).get(i).toString() : "");

                }

                xCell = xSpreadsheet.getCellByPosition(nCol++, nRow);
                xPropSet = UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
                xPropSet.setPropertyValue("TableBorder", aBorder);
                xCell.setFormula("= E" + (nRow + 1) + " + G" + (nRow + 1) + " + I" + (nRow + 1) + " - K" + (nRow + 1));

                xCell = xSpreadsheet.getCellByPosition(nCol++, nRow);
                xPropSet = UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
                xPropSet.setPropertyValue("TableBorder", aBorder);
                xCell.setFormula("= F" + (nRow + 1) + " + H" + (nRow + 1) + " + J" + (nRow + 1) + " - L" + (nRow + 1));

                nRow++;
                nCol = 1;
            }

            xCell = xSpreadsheet.getCellByPosition(1, nRow);
            xCell.setFormula("ИТОГО");
            xPropSet = UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
            xPropSet.setPropertyValue("CharWeight", new Float(FontWeight.BOLD));
            xCell = xSpreadsheet.getCellByPosition(4, nRow);
            xCell.setFormula("=SUM(E" + 10 + ":E" + nRow + ")");
            xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
            xPropSet.setPropertyValue("CharWeight", new Float(FontWeight.BOLD));
            xCell = xSpreadsheet.getCellByPosition(5, nRow);
            xCell.setFormula("=SUM(F" + 10 + ":F" + nRow + ")");
            xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
            xPropSet.setPropertyValue("CharWeight", new Float(FontWeight.BOLD));
            xCell = xSpreadsheet.getCellByPosition(6, nRow);
            xCell.setFormula("=SUM(G" + 10 + ":G" + nRow + ")");
            xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
            xPropSet.setPropertyValue("CharWeight", new Float(FontWeight.BOLD));
            xCell = xSpreadsheet.getCellByPosition(7, nRow);
            xCell.setFormula("=SUM(H" + 10 + ":H" + nRow + ")");
            xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
            xPropSet.setPropertyValue("CharWeight", new Float(FontWeight.BOLD));
            xCell = xSpreadsheet.getCellByPosition(8, nRow);
            xCell.setFormula("=SUM(I" + 10 + ":I" + nRow + ")");
            xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
            xPropSet.setPropertyValue("CharWeight", new Float(FontWeight.BOLD));
            xCell = xSpreadsheet.getCellByPosition(9, nRow);
            xCell.setFormula("=SUM(J" + 10 + ":J" + nRow + ")");
            xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
            xPropSet.setPropertyValue("CharWeight", new Float(FontWeight.BOLD));
            xCell = xSpreadsheet.getCellByPosition(10, nRow);
            xCell.setFormula("=SUM(K" + 10 + ":K" + nRow + ")");
            xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
            xPropSet.setPropertyValue("CharWeight", new Float(FontWeight.BOLD));
            xCell = xSpreadsheet.getCellByPosition(11, nRow);
            xCell.setFormula("=SUM(L" + 10 + ":L" + nRow + ")");
            xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
            xPropSet.setPropertyValue("CharWeight", new Float(FontWeight.BOLD));
            xCell = xSpreadsheet.getCellByPosition(12, nRow);
            xCell.setFormula("=SUM(M" + 10 + ":M" + nRow + ")");
            xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
            xPropSet.setPropertyValue("CharWeight", new Float(FontWeight.BOLD));
            xCell = xSpreadsheet.getCellByPosition(13, nRow);
            xCell.setFormula("=SUM(N" + 10 + ":N" + nRow + ")");
            xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
            xPropSet.setPropertyValue("CharWeight", new Float(FontWeight.BOLD));
            xCell = xSpreadsheet.getCellByPosition(14, nRow);
            xCell.setFormula("=SUM(O" + 10 + ":O" + nRow + ")");
            xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
            xPropSet.setPropertyValue("CharWeight", new Float(FontWeight.BOLD));
            xCell = xSpreadsheet.getCellByPosition(15, nRow);
            xCell.setFormula("=SUM(P" + 10 + ":P" + nRow + ")");
            xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
            xPropSet.setPropertyValue("CharWeight", new Float(FontWeight.BOLD));
            xCell = xSpreadsheet.getCellByPosition(16, nRow);
            xCell.setFormula("=SUM(Q" + 10 + ":Q" + nRow + ")");
            xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
            xPropSet.setPropertyValue("CharWeight", new Float(FontWeight.BOLD));
            xCell = xSpreadsheet.getCellByPosition(17, nRow);
            xCell.setFormula("=SUM(R" + 10 + ":R" + nRow + ")");
            xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
            xPropSet.setPropertyValue("CharWeight", new Float(FontWeight.BOLD));
            xCell = xSpreadsheet.getCellByPosition(18, nRow);
            xCell.setFormula("=SUM(S" + 10 + ":S" + nRow + ")");
            xPropSet = UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
            xPropSet.setPropertyValue("CharWeight", new Float(FontWeight.BOLD));
            xCell = xSpreadsheet.getCellByPosition(19, nRow);
            xCell.setFormula("=SUM(T" + 10 + ":T" + nRow + ")");
            xPropSet = UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
            xPropSet.setPropertyValue("CharWeight", new Float(FontWeight.BOLD));
            nRow++;

            nRow++;
            xCell = xSpreadsheet.getCellByPosition(6, nRow);
            xCell.setFormula("Сделал отчет кладовщик:");
            xPropSet = UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
            xPropSet.setPropertyValue("CharWeight", new Float(FontWeight.BOLD));
            xCell = xSpreadsheet.getCellByPosition(11, nRow++);
            xCell.setFormula(UtilSkladHO.SKLADHO_CLAD);
            xPropSet = UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
            xPropSet.setPropertyValue("CharWeight", new Float(FontWeight.BOLD));

            xCell = xSpreadsheet.getCellByPosition(6, nRow);
            xCell.setFormula("Проверил отчет бухгалтер:");
            xPropSet = UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
            xPropSet.setPropertyValue("CharWeight", new Float(FontWeight.BOLD));
            xCell = xSpreadsheet.getCellByPosition(11, nRow++);
            xCell.setFormula(UtilSkladHO.SKLADHO_BUH);
            xPropSet = UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
            xPropSet.setPropertyValue("CharWeight", new Float(FontWeight.BOLD));
            
            /*
            xCell = xSpreadsheet.getCellByPosition(6, nRow);
            xCell.setFormula("Утвердил начальник отдела:");
            xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
            xPropSet.setPropertyValue("CharWeight", new Float(FontWeight.BOLD));
            xCell = xSpreadsheet.getCellByPosition(11, nRow++);
            xCell.setFormula(UtilSkladHO.SKLADHO_HACH); 
            xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
            xPropSet.setPropertyValue("CharWeight", new Float(FontWeight.BOLD));
            */

        } catch (java.lang.Exception e) {
            log.error("Ошибка при заполнении отчёта.", e);
            throw new Exception("Ошибка при заполнении отчёта.", e);
        }
    }

    private void fildTableMoveVMGCheck(XComponent currentDocument) throws Exception {
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

            int nCol = 1;
            int nRow = 1;
            int k = 1;

            //----------------Заполнение шапки документа
            XCell xCell = xSpreadsheet.getCellByPosition(5, 4);
            xCell.setFormula(vec.get(0).toString());

            xCell = xSpreadsheet.getCellByPosition(17, 4);
            xCell.setFormula(UtilSkladHO.SKLADHO_HACH);

            //  xCell = xSpreadsheet.getCellByPosition(17, 4);
            //   xCell.setFormula(vec.get(0).toString());

            //----------------Заполнение документа

            nRow = 9;
            for (Object row : data) {

                xCell = xSpreadsheet.getCellByPosition(1, nRow);
                xCell.setValue(k++);

                for (int i = 0; i < ((Vector) row).size(); i++) {
                    xCell = xSpreadsheet.getCellByPosition(nCol++, nRow);
                    xPropSet = UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
                    xPropSet.setPropertyValue("TableBorder", aBorder);
                    xCell.setFormula(((Vector) row).get(i) != null ? ((Vector) row).get(i).toString() : "");

                }

                xCell = xSpreadsheet.getCellByPosition(nCol++, nRow);
                xPropSet = UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
                xPropSet.setPropertyValue("TableBorder", aBorder);
                xCell.setFormula("= D" + (nRow + 1) + " + F" + (nRow + 1) + " + H" + (nRow + 1) + " - J" + (nRow + 1));

                xCell = xSpreadsheet.getCellByPosition(nCol++, nRow);
                xPropSet = UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
                xPropSet.setPropertyValue("TableBorder", aBorder);
                xCell.setFormula("= E" + (nRow + 1) + " + G" + (nRow + 1) + " + I" + (nRow + 1) + " - K" + (nRow + 1));

                nRow++;
                nCol = 1;
            }

            xCell = xSpreadsheet.getCellByPosition(1, nRow);
            xCell.setFormula("ИТОГО");
            xPropSet = UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
            xPropSet.setPropertyValue("CharWeight", new Float(FontWeight.BOLD));
            xCell = xSpreadsheet.getCellByPosition(3, nRow);
            xCell.setFormula("=SUM(D" + 10 + ":D" + nRow + ")");
            xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
            xPropSet.setPropertyValue("CharWeight", new Float(FontWeight.BOLD));
            xCell = xSpreadsheet.getCellByPosition(4, nRow);
            xCell.setFormula("=SUM(E" + 10 + ":E" + nRow + ")");
            xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
            xPropSet.setPropertyValue("CharWeight", new Float(FontWeight.BOLD));
            xCell = xSpreadsheet.getCellByPosition(5, nRow);
            xCell.setFormula("=SUM(F" + 10 + ":F" + nRow + ")");
            xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
            xPropSet.setPropertyValue("CharWeight", new Float(FontWeight.BOLD));
            xCell = xSpreadsheet.getCellByPosition(6, nRow);
            xCell.setFormula("=SUM(G" + 10 + ":G" + nRow + ")");
            xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
            xPropSet.setPropertyValue("CharWeight", new Float(FontWeight.BOLD));
            xCell = xSpreadsheet.getCellByPosition(7, nRow);
            xCell.setFormula("=SUM(H" + 10 + ":H" + nRow + ")");
            xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
            xPropSet.setPropertyValue("CharWeight", new Float(FontWeight.BOLD));
            xCell = xSpreadsheet.getCellByPosition(8, nRow);
            xCell.setFormula("=SUM(I" + 10 + ":I" + nRow + ")");
            xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
            xPropSet.setPropertyValue("CharWeight", new Float(FontWeight.BOLD));
            xCell = xSpreadsheet.getCellByPosition(9, nRow);
            xCell.setFormula("=SUM(J" + 10 + ":J" + nRow + ")");
            xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
            xPropSet.setPropertyValue("CharWeight", new Float(FontWeight.BOLD));
            xCell = xSpreadsheet.getCellByPosition(10, nRow);
            xCell.setFormula("=SUM(K" + 10 + ":K" + nRow + ")");
            xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
            xPropSet.setPropertyValue("CharWeight", new Float(FontWeight.BOLD));
            xCell = xSpreadsheet.getCellByPosition(11, nRow);
            xCell.setFormula("=SUM(L" + 10 + ":L" + nRow + ")");
            xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
            xPropSet.setPropertyValue("CharWeight", new Float(FontWeight.BOLD));
            xCell = xSpreadsheet.getCellByPosition(12, nRow);
            xCell.setFormula("=SUM(M" + 10 + ":M" + nRow + ")");
            xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
            xPropSet.setPropertyValue("CharWeight", new Float(FontWeight.BOLD));
            xCell = xSpreadsheet.getCellByPosition(13, nRow);
            xCell.setFormula("=SUM(N" + 10 + ":N" + nRow + ")");
            xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
            xPropSet.setPropertyValue("CharWeight", new Float(FontWeight.BOLD));
            xCell = xSpreadsheet.getCellByPosition(14, nRow);
            xCell.setFormula("=SUM(O" + 10 + ":O" + nRow + ")");
            xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
            xPropSet.setPropertyValue("CharWeight", new Float(FontWeight.BOLD));
            xCell = xSpreadsheet.getCellByPosition(15, nRow);
            xCell.setFormula("=SUM(P" + 10 + ":P" + nRow + ")");
            xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
            xPropSet.setPropertyValue("CharWeight", new Float(FontWeight.BOLD));
            xCell = xSpreadsheet.getCellByPosition(16, nRow);
            xCell.setFormula("=SUM(Q" + 10 + ":Q" + nRow + ")");
            xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
            xPropSet.setPropertyValue("CharWeight", new Float(FontWeight.BOLD));
            xCell = xSpreadsheet.getCellByPosition(17, nRow);
            xCell.setFormula("=SUM(R" + 10 + ":R" + nRow + ")");
            xPropSet = UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
            xPropSet.setPropertyValue("CharWeight", new Float(FontWeight.BOLD));
            xCell = xSpreadsheet.getCellByPosition(18, nRow);
            xCell.setFormula("=SUM(S" + 10 + ":S" + nRow + ")");
            xPropSet = UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
            xPropSet.setPropertyValue("CharWeight", new Float(FontWeight.BOLD));
            nRow++;

            nRow++;
            xCell = xSpreadsheet.getCellByPosition(5, nRow);
            xCell.setFormula("Сделал отчет кладовщик:");
            xPropSet = UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
            xPropSet.setPropertyValue("CharWeight", new Float(FontWeight.BOLD));
            xCell = xSpreadsheet.getCellByPosition(10, nRow++);
            xCell.setFormula(UtilSkladHO.SKLADHO_CLAD);
            xPropSet = UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
            xPropSet.setPropertyValue("CharWeight", new Float(FontWeight.BOLD));

            xCell = xSpreadsheet.getCellByPosition(5, nRow);
            xCell.setFormula("Проверил отчет бухгалтер:");
            xPropSet = UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
            xPropSet.setPropertyValue("CharWeight", new Float(FontWeight.BOLD));
            xCell = xSpreadsheet.getCellByPosition(10, nRow++);
            xCell.setFormula(UtilSkladHO.SKLADHO_BUH);
            xPropSet = UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
            xPropSet.setPropertyValue("CharWeight", new Float(FontWeight.BOLD));
            
            /*
            xCell = xSpreadsheet.getCellByPosition(5, nRow);
            xCell.setFormula("Утвердил начальник отдела:");
            xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
            xPropSet.setPropertyValue("CharWeight", new Float(FontWeight.BOLD));
            xCell = xSpreadsheet.getCellByPosition(10, nRow++);
            xCell.setFormula(UtilSkladHO.SKLADHO_HACH); 
            xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
            xPropSet.setPropertyValue("CharWeight", new Float(FontWeight.BOLD));
            */

        } catch (java.lang.Exception e) {
            log.error("Ошибка при заполнении отчёта.", e);
            throw new Exception("Ошибка при заполнении отчёта.", e);
        }
    }

    //Движение импортного полотна
    private void fildTableMoveCheck(XComponent currentDocument) throws Exception {
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

            int nCol = 1;
            int nRow = 1;
            int k = 1;
            int sum = 0;

            //----------------Заполнение шапки документа
            XCell xCell = xSpreadsheet.getCellByPosition(7, 4);
            xCell.setFormula(vec.get(0).toString());

            xCell = xSpreadsheet.getCellByPosition(21, 4);
            xCell.setFormula(UtilSkladHO.SKLADHO_HACH);

            // xCell = xSpreadsheet.getCellByPosition(16, 4);
            // xCell.setFormula(vec.get(0).toString());

            //  xCell = xSpreadsheet.getCellByPosition(22, 4);
            //   xCell.setFormula(vec.get(0).toString());

            //----------------Заполнение документа

            nRow = 9;
            for (Object row : data) {

                xCell = xSpreadsheet.getCellByPosition(1, nRow);
                xCell.setValue(k++);

                for (int i = 0; i < ((Vector) row).size(); i++) {
                    xCell = xSpreadsheet.getCellByPosition(nCol++, nRow);
                    xPropSet = UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
                    xPropSet.setPropertyValue("TableBorder", aBorder);

                    if (sum == 1) {
                        sum = 0;
                        i--;

                        if (i == 6)
                            xCell.setFormula("=" + ((Vector) row).get(i).toString() + " * E" + (nRow + 1));
                        else
                            xCell.setFormula("=" + ((Vector) row).get(i).toString() + " * F" + (nRow + 1));
                    } else {
                        if (i > 5)
                            sum++;
                        if (i == 2)
                            xCell.setFormula(((Vector) row).get(i) != null ? " '" + ((Vector) row).get(i).toString() : "");
                        else
                            xCell.setFormula(((Vector) row).get(i) != null ? ((Vector) row).get(i).toString() : "");
                    }
                }
                xCell = xSpreadsheet.getCellByPosition(nCol++, nRow);
                xPropSet = UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
                xPropSet.setPropertyValue("TableBorder", aBorder);
                xCell.setFormula("=" + ((Vector) row).get(((Vector) row).size() - 1).toString() + " * F" + (nRow + 1));

                xCell = xSpreadsheet.getCellByPosition(nCol++, nRow);
                xPropSet = UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
                xPropSet.setPropertyValue("TableBorder", aBorder);
                xCell.setFormula("= H" + (nRow + 1) + " + J" + (nRow + 1) + " + L" + (nRow + 1) + " - N" + (nRow + 1) + " ");

                xCell = xSpreadsheet.getCellByPosition(nCol++, nRow);
                xPropSet = UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
                xPropSet.setPropertyValue("TableBorder", aBorder);
                xCell.setFormula("= I" + (nRow + 1) + " + K" + (nRow + 1) + " + M" + (nRow + 1) + " - O" + (nRow + 1) + " ");

                nRow++;
                nCol = 1;
                sum = 0;
            }

            xCell = xSpreadsheet.getCellByPosition(1, nRow);
            xCell.setFormula("ИТОГО");
            xPropSet = UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
            xPropSet.setPropertyValue("CharWeight", new Float(FontWeight.BOLD));
            xCell = xSpreadsheet.getCellByPosition(7, nRow);
            xCell.setFormula("=SUM(H" + 10 + ":H" + nRow + ")");
            xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
            xPropSet.setPropertyValue("CharWeight", new Float(FontWeight.BOLD));
            xCell = xSpreadsheet.getCellByPosition(8, nRow);
            xCell.setFormula("=SUM(I" + 10 + ":I" + nRow + ")");
            xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
            xPropSet.setPropertyValue("CharWeight", new Float(FontWeight.BOLD));
            xCell = xSpreadsheet.getCellByPosition(9, nRow);
            xCell.setFormula("=SUM(J" + 10 + ":J" + nRow + ")");
            xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
            xPropSet.setPropertyValue("CharWeight", new Float(FontWeight.BOLD));
            xCell = xSpreadsheet.getCellByPosition(10, nRow);
            xCell.setFormula("=SUM(K" + 10 + ":K" + nRow + ")");
            xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
            xPropSet.setPropertyValue("CharWeight", new Float(FontWeight.BOLD));
            xCell = xSpreadsheet.getCellByPosition(11, nRow);
            xCell.setFormula("=SUM(L" + 10 + ":L" + nRow + ")");
            xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
            xPropSet.setPropertyValue("CharWeight", new Float(FontWeight.BOLD));
            xCell = xSpreadsheet.getCellByPosition(12, nRow);
            xCell.setFormula("=SUM(M" + 10 + ":M" + nRow + ")");
            xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
            xPropSet.setPropertyValue("CharWeight", new Float(FontWeight.BOLD));
            xCell = xSpreadsheet.getCellByPosition(13, nRow);
            xCell.setFormula("=SUM(N" + 10 + ":N" + nRow + ")");
            xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
            xPropSet.setPropertyValue("CharWeight", new Float(FontWeight.BOLD));
            xCell = xSpreadsheet.getCellByPosition(14, nRow);
            xCell.setFormula("=SUM(O" + 10 + ":O" + nRow + ")");
            xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
            xPropSet.setPropertyValue("CharWeight", new Float(FontWeight.BOLD));
            xCell = xSpreadsheet.getCellByPosition(15, nRow);
            xCell.setFormula("=SUM(P" + 10 + ":P" + nRow + ")");
            xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
            xPropSet.setPropertyValue("CharWeight", new Float(FontWeight.BOLD));
            xCell = xSpreadsheet.getCellByPosition(16, nRow);
            xCell.setFormula("=SUM(Q" + 10 + ":Q" + nRow + ")");
            xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
            xPropSet.setPropertyValue("CharWeight", new Float(FontWeight.BOLD));
            xCell = xSpreadsheet.getCellByPosition(17, nRow);
            xCell.setFormula("=SUM(R" + 10 + ":R" + nRow + ")");
            xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
            xPropSet.setPropertyValue("CharWeight", new Float(FontWeight.BOLD));
            xCell = xSpreadsheet.getCellByPosition(18, nRow);
            xCell.setFormula("=SUM(S" + 10 + ":S" + nRow + ")");
            xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
            xPropSet.setPropertyValue("CharWeight", new Float(FontWeight.BOLD));
            xCell = xSpreadsheet.getCellByPosition(19, nRow);
            xCell.setFormula("=SUM(T" + 10 + ":T" + nRow + ")");
            xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
            xPropSet.setPropertyValue("CharWeight", new Float(FontWeight.BOLD));
            xCell = xSpreadsheet.getCellByPosition(20, nRow);
            xCell.setFormula("=SUM(U" + 10 + ":U" + nRow + ")");
            xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
            xPropSet.setPropertyValue("CharWeight", new Float(FontWeight.BOLD));
            xCell = xSpreadsheet.getCellByPosition(21, nRow);
            xCell.setFormula("=SUM(V" + 10 + ":V" + nRow + ")");
            xPropSet = UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
            xPropSet.setPropertyValue("CharWeight", new Float(FontWeight.BOLD));
            xCell = xSpreadsheet.getCellByPosition(22, nRow);
            xCell.setFormula("=SUM(W" + 10 + ":W" + nRow + ")");
            xPropSet = UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
            xPropSet.setPropertyValue("CharWeight", new Float(FontWeight.BOLD));
            nRow++;

            nRow++;
            xCell = xSpreadsheet.getCellByPosition(9, nRow);
            xCell.setFormula("Сделал отчет кладовщик:");
            xPropSet = UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
            xPropSet.setPropertyValue("CharWeight", new Float(FontWeight.BOLD));
            xCell = xSpreadsheet.getCellByPosition(14, nRow++);
            xCell.setFormula(UtilSkladHO.SKLADHO_CLAD);
            xPropSet = UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
            xPropSet.setPropertyValue("CharWeight", new Float(FontWeight.BOLD));

            xCell = xSpreadsheet.getCellByPosition(9, nRow);
            xCell.setFormula("Проверил отчет бухгалтер:");
            xPropSet = UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
            xPropSet.setPropertyValue("CharWeight", new Float(FontWeight.BOLD));
            xCell = xSpreadsheet.getCellByPosition(14, nRow++);
            xCell.setFormula(UtilSkladHO.SKLADHO_BUH);
            xPropSet = UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
            xPropSet.setPropertyValue("CharWeight", new Float(FontWeight.BOLD));
            
            /*
            xCell = xSpreadsheet.getCellByPosition(9, nRow);
            xCell.setFormula("Утвердил начальник отдела:");
            xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
            xPropSet.setPropertyValue("CharWeight", new Float(FontWeight.BOLD));
            xCell = xSpreadsheet.getCellByPosition(14, nRow++);
            xCell.setFormula(UtilSkladHO.SKLADHO_HACH); 
            xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
            xPropSet.setPropertyValue("CharWeight", new Float(FontWeight.BOLD));
            */

        } catch (java.lang.Exception e) {
            log.error("Ошибка при заполнении отчёта.", e);
            throw new Exception("Ошибка при заполнении отчёта.", e);
        }
    }

    private void fildTableMapCheck(XComponent currentDocument) throws Exception {
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

            int nCol = 0;
            int nRow = 0;
            Vector dataDate = new Vector();

            XCell xCell = xSpreadsheet.getCellByPosition(3, 2);
            if (!vec.get(3).toString().equals(""))
                xCell.setFormula(xCell.getFormula() + " №" + vec.get(3).toString());

            xCell = xSpreadsheet.getCellByPosition(2, 4);
            xCell.setFormula(vec.get(1).toString());

            xCell = xSpreadsheet.getCellByPosition(5, 4);
            xCell.setFormula(vec.get(2).toString());

            xCell = xSpreadsheet.getCellByPosition(11, 4);
            xCell.setFormula("'" + vec.get(0).toString());

            nCol = 1;
            nRow = 8;

            if (type == 1) {
                int[] colTMC = {3, 4, 6, 8, 10, 11, 12};
                int[] colReturn = {4, 6, 7, 9, 11, 13, 14};

                for (int i = nCol; i < 10; i++) {
                    xCell = xSpreadsheet.getCellByPosition(nCol++, nRow);
                    xPropSet = UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
                    xPropSet.setPropertyValue("TableBorder", aBorder);
                }

                for (Object element : data) {
                    xCell = xSpreadsheet.getCellByPosition(nCol++, nRow);
                    xCell.setFormula(element.toString());
                    xPropSet = UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
                    xPropSet.setPropertyValue("TableBorder", aBorder);
                }

                xCell = xSpreadsheet.getCellByPosition(nCol++, nRow);
                xPropSet = UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
                xPropSet.setPropertyValue("TableBorder", aBorder);

                nRow++;
                nCol = 1;

                for (Object row : data1) {
                    nCol = 1;
                    xCell = xSpreadsheet.getCellByPosition(nCol++, nRow);
                    xCell.setFormula("'" + vec.get(0).toString());
                    xPropSet = UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
                    xPropSet.setPropertyValue("TableBorder", aBorder);

                    xCell = xSpreadsheet.getCellByPosition(nCol++, nRow);
                    xCell.setFormula("списание");
                    xPropSet = UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
                    xPropSet.setPropertyValue("TableBorder", aBorder);

                    for (int i : colTMC) {
                        xCell = xSpreadsheet.getCellByPosition(nCol++, nRow);
                        xCell.setFormula(((Vector) row).get(i).toString());
                        xPropSet = UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
                        xPropSet.setPropertyValue("TableBorder", aBorder);
                    }

                    for (int i = nCol; i < 16; i++) {
                        xCell = xSpreadsheet.getCellByPosition(nCol++, nRow);
                        xPropSet = UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
                        xPropSet.setPropertyValue("TableBorder", aBorder);
                    }
                    nRow++;
                }

                dataDate.add(vec.get(0).toString().replace(".", "-"));

                for (Object row : data2) {
                    if (!dataDate.contains(((Vector) row).get(2).toString()))
                        dataDate.add(((Vector) row).get(2).toString());
                }

                Collections.sort(dataDate);

                for (Object date : dataDate) {
                    for (Object row : data2) {
                        nCol = 1;
                        if (((Vector) row).get(2).toString().equals(String.valueOf(date))) {
                            xCell = xSpreadsheet.getCellByPosition(nCol++, nRow);
                            xCell.setFormula("'" + date.toString());
                            xPropSet = UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
                            xPropSet.setPropertyValue("TableBorder", aBorder);

                            for (int i : colReturn) {
                                xCell = xSpreadsheet.getCellByPosition(nCol++, nRow);
                                xCell.setFormula(((Vector) row).get(i).toString());
                                xPropSet = UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
                                xPropSet.setPropertyValue("TableBorder", aBorder);
                            }

                            xCell = xSpreadsheet.getCellByPosition(nCol++, nRow);
                            xCell.setValue(0);
                            xPropSet = UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
                            xPropSet.setPropertyValue("TableBorder", aBorder);

                            for (int i = nCol; i < 16; i++) {
                                xCell = xSpreadsheet.getCellByPosition(nCol++, nRow);
                                xPropSet = UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
                                xPropSet.setPropertyValue("TableBorder", aBorder);
                            }

                            nRow++;
                        }
                    }
                }
            } else if (type == 0) {


            }
        } catch (java.lang.Exception e) {
            log.error("Ошибка при заполнении отчёта.", e);
            throw new Exception("Ошибка при заполнении отчёта.", e);
        }
    }

    private void fildTableNakCheck(XComponent currentDocument) throws Exception {
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

            //----------------Заполнение шапки документа
            XCell xCell = xSpreadsheet.getCellByPosition(4, 4);
            xCell.setFormula("'" + vec.get(2).toString());

            xCell = xSpreadsheet.getCellByPosition(1, 5);
            xCell.setFormula(
                    (vec.get(0).toString().equals("-") ? "" : " в " + vec.get(0).toString()) +
                            (vec.get(1).toString().equals("Все...") ? "" : " " + vec.get(1).toString()));

            int nCol = 0;

            //----------------Заполнение тела документа

            nCol = 1;
            int nRow = 9;
            for (Object row : data) {
                for (int i = 0; i < ((Vector) row).size(); i++) {
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

    private void fildTablePOIPCheck(XComponent currentDocument) throws Exception {
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

            int nCol = 1;
            int nRow = 1;
            int k = 1;

            //----------------Заполнение шапки документа
            XCell xCell = xSpreadsheet.getCellByPosition(5, 3);
            xCell.setFormula(vec.get(0).toString());

            xCell = xSpreadsheet.getCellByPosition(12, 3);
            xCell.setFormula(UtilSkladHO.SKLADHO_HACH);

            //----------------Заполнение документа

            nRow = 8;
            for (Object row : data) {

                xCell = xSpreadsheet.getCellByPosition(0, nRow);
                xCell.setValue(k++);

                for (int i = 0; i < ((Vector) row).size(); i++) {
                    xCell = xSpreadsheet.getCellByPosition(nCol++, nRow);
                    xPropSet = UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
                    xPropSet.setPropertyValue("TableBorder", aBorder);
                    if (i > 4) {
                        xCell.setFormula(((Vector) row).get(i) != null ? ((Vector) row).get(i).toString() : "");
                        xCell = xSpreadsheet.getCellByPosition(nCol++, nRow);
                        xPropSet = UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
                        xPropSet.setPropertyValue("TableBorder", aBorder);
                        xCell.setFormula("=" + ((Vector) row).get(i).toString() + " * F" + (nRow + 1));

                    } else {
                        if (i == 2)
                            xCell.setFormula(((Vector) row).get(i) != null ? " '" + ((Vector) row).get(i).toString() : "");
                        else
                            xCell.setFormula(((Vector) row).get(i) != null ? ((Vector) row).get(i).toString() : "");
                    }
                }

                xCell = xSpreadsheet.getCellByPosition(nCol++, nRow);
                xPropSet = UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
                xPropSet.setPropertyValue("TableBorder", aBorder);
                xCell.setFormula("= G" + (nRow + 1) + " + I" + (nRow + 1) + " - K" + (nRow + 1) + " ");

                xCell = xSpreadsheet.getCellByPosition(nCol++, nRow);
                xPropSet = UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
                xPropSet.setPropertyValue("TableBorder", aBorder);
                xCell.setFormula("= H" + (nRow + 1) + " + J" + (nRow + 1) + " - L" + (nRow + 1) + " ");

                nRow++;
                nCol = 1;
            }

            xCell = xSpreadsheet.getCellByPosition(1, nRow);
            xCell.setFormula("ИТОГО");
            xPropSet = UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
            xPropSet.setPropertyValue("CharWeight", new Float(FontWeight.BOLD));
            xCell = xSpreadsheet.getCellByPosition(6, nRow);
            xCell.setFormula("=SUM(G" + 9 + ":G" + nRow + ")");
            xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
            xPropSet.setPropertyValue("CharWeight", new Float(FontWeight.BOLD));
            xCell = xSpreadsheet.getCellByPosition(7, nRow);
            xCell.setFormula("=SUM(H" + 9 + ":H" + nRow + ")");
            xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
            xPropSet.setPropertyValue("CharWeight", new Float(FontWeight.BOLD));
            xCell = xSpreadsheet.getCellByPosition(8, nRow);
            xCell.setFormula("=SUM(I" + 9 + ":I" + nRow + ")");
            xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
            xPropSet.setPropertyValue("CharWeight", new Float(FontWeight.BOLD));
            xCell = xSpreadsheet.getCellByPosition(9, nRow);
            xCell.setFormula("=SUM(J" + 9 + ":J" + nRow + ")");
            xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
            xPropSet.setPropertyValue("CharWeight", new Float(FontWeight.BOLD));
            xCell = xSpreadsheet.getCellByPosition(10, nRow);
            xCell.setFormula("=SUM(K" + 9 + ":K" + nRow + ")");
            xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
            xPropSet.setPropertyValue("CharWeight", new Float(FontWeight.BOLD));
            xCell = xSpreadsheet.getCellByPosition(11, nRow);
            xCell.setFormula("=SUM(L" + 9 + ":L" + nRow + ")");
            xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
            xPropSet.setPropertyValue("CharWeight", new Float(FontWeight.BOLD));
            xCell = xSpreadsheet.getCellByPosition(12, nRow);
            xCell.setFormula("=SUM(M" + 9 + ":M" + nRow + ")");
            xPropSet = UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
            xPropSet.setPropertyValue("CharWeight", new Float(FontWeight.BOLD));
            xCell = xSpreadsheet.getCellByPosition(13, nRow);
            xCell.setFormula("=SUM(N" + 9 + ":N" + nRow + ")");
            xPropSet = UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
            xPropSet.setPropertyValue("CharWeight", new Float(FontWeight.BOLD));
            nRow++;

            nRow++;
            xCell = xSpreadsheet.getCellByPosition(7, nRow);
            xCell.setFormula("Сделал отчет кладовщик:");
            xPropSet = UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
            xPropSet.setPropertyValue("CharWeight", new Float(FontWeight.BOLD));
            xCell = xSpreadsheet.getCellByPosition(12, nRow++);
            xCell.setFormula(UtilSkladHO.SKLADHO_CLAD);
            xPropSet = UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
            xPropSet.setPropertyValue("CharWeight", new Float(FontWeight.BOLD));

            xCell = xSpreadsheet.getCellByPosition(7, nRow);
            xCell.setFormula("Проверил отчет бухгалтер:");
            xPropSet = UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
            xPropSet.setPropertyValue("CharWeight", new Float(FontWeight.BOLD));
            xCell = xSpreadsheet.getCellByPosition(12, nRow++);
            xCell.setFormula(UtilSkladHO.SKLADHO_BUH);
            xPropSet = UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
            xPropSet.setPropertyValue("CharWeight", new Float(FontWeight.BOLD));
            
            /*
            xCell = xSpreadsheet.getCellByPosition(7, nRow);
            xCell.setFormula("Утвердил начальник отдела:");
            xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
            xPropSet.setPropertyValue("CharWeight", new Float(FontWeight.BOLD));
            xCell = xSpreadsheet.getCellByPosition(12, nRow++);
            xCell.setFormula(UtilSkladHO.SKLADHO_HACH); 
            xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
            xPropSet.setPropertyValue("CharWeight", new Float(FontWeight.BOLD));
            */
        } catch (java.lang.Exception e) {
            log.error("Ошибка при заполнении отчёта.", e);
            throw new Exception("Ошибка при заполнении отчёта.", e);
        }
    }

    //Инвентаризационная опись
    private void fildTableHOInventory(XComponent currentDocument) throws Exception {
        try {
            XSpreadsheetDocument xSpreadsheetDocument = (XSpreadsheetDocument) UnoRuntime.queryInterface(XSpreadsheetDocument.class, currentDocument);
            XSpreadsheets xSpreadsheets = xSpreadsheetDocument.getSheets();
            Object sheet = xSpreadsheets.getByName("Лист1");
            XSpreadsheet xSpreadsheet = (XSpreadsheet) UnoRuntime.queryInterface(XSpreadsheet.class, sheet);
            com.sun.star.beans.XPropertySet xPropSet = null;
            com.sun.star.table.BorderLine aLine = new com.sun.star.table.BorderLine();
            aLine.Color = 0x000000;
            aLine.InnerLineWidth = aLine.LineDistance = 0;
            aLine.OuterLineWidth = 1;
            com.sun.star.table.TableBorder aBorder = new com.sun.star.table.TableBorder();
            aBorder.TopLine = aBorder.BottomLine = aBorder.LeftLine = aBorder.RightLine = aLine;
            aBorder.IsTopLineValid = aBorder.IsBottomLineValid = true;
            aBorder.IsLeftLineValid = aBorder.IsRightLineValid = true;

            int nCol = 1;
            int nRow;
            int k = 1;
            int startRowFromFormula;

            //----------------Заполнение шапки документа
            XCell xCell = xSpreadsheet.getCellByPosition(8, 12);
            xCell.setFormula(date);

            //----------------Заполнение документа

            /*
            !полезные вещи для calc
            //форматирование
            XColumnRowRange oColumnRowRange = (XColumnRowRange) UnoRuntime.queryInterface(XColumnRowRange.class, xSpreadsheet);
            XTableRows oRows = (XTableRows) oColumnRowRange.getRows();
            oRows.insertByIndex(30,1);

            //доб.строки
            XTextTablesSupplier xTablesSupplier = (XTextTablesSupplier) UnoRuntime.queryInterface(XTextTablesSupplier.class, xSpreadsheet);
            XTableRows rows = ((XTextTable) UnoRuntime.queryInterface(XTextTable.class, UnoRuntime.queryInterface(XTextTable.class, xTablesSupplier.getTextTables().getByIndex(0)))).getRows();
            rows.removeByIndex(30,1);
            */

            nRow = 30;
            startRowFromFormula = nRow + 1;
            for (Object row : data) {
                if (Double.valueOf(((Vector) row).get(13).toString()) >= 0.005) {
                    xCell = xSpreadsheet.getCellByPosition(nCol++, nRow);
                    xCell.setValue(k++);
                    xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
                    xPropSet.setPropertyValue("TableBorder", aBorder);

                    xCell = xSpreadsheet.getCellByPosition(nCol++, nRow);
                    xCell.setFormula(((Vector) row).get(1).toString() + ", " +
                            ((Vector) row).get(3).toString() + ", " +
                            ((Vector) row).get(4).toString());
                    xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
                    xPropSet.setPropertyValue("TableBorder", aBorder);

                    xCell = xSpreadsheet.getCellByPosition(nCol++, nRow);
                    xCell.setFormula(((Vector) row).get(8).toString());
                    xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
                    xPropSet.setPropertyValue("TableBorder", aBorder);

                    xCell = xSpreadsheet.getCellByPosition(nCol++, nRow);
                    xCell.setValue(Double.valueOf(((Vector) row).get(15).toString()));
                    xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
                    xPropSet.setPropertyValue("TableBorder", aBorder);

                    xCell = xSpreadsheet.getCellByPosition(nCol++, nRow);
                    xCell.setFormula(((Vector) row).get(13).toString());
                    xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
                    xPropSet.setPropertyValue("TableBorder", aBorder);

                    xCell = xSpreadsheet.getCellByPosition(nCol++, nRow);
                    xCell.setFormula("=E" + (nRow + 1) + "*F" + (nRow + 1));
                    xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
                    xPropSet.setPropertyValue("TableBorder", aBorder);

                    xCell = xSpreadsheet.getCellByPosition(nCol++, nRow);
                    xCell.setFormula(((Vector) row).get(13).toString());
                    xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
                    xPropSet.setPropertyValue("TableBorder", aBorder);

                    xCell = xSpreadsheet.getCellByPosition(nCol++, nRow);
                    xCell.setFormula("=E" + (nRow + 1) + "*H" + (nRow + 1));
                    xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
                    xPropSet.setPropertyValue("TableBorder", aBorder);

                    nRow++;
                    nCol = 1;
                }
            }

            xCell = xSpreadsheet.getCellByPosition(2, nRow);
            xCell.setFormula("ИТОГО");
            xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
            xPropSet.setPropertyValue("CharWeight", new Float(FontWeight.BOLD));
            xCell = xSpreadsheet.getCellByPosition(5, nRow);
            xCell.setFormula("=SUM(F" + startRowFromFormula + ":F" + nRow + ")");
            xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
            xPropSet.setPropertyValue("CharWeight", new Float(FontWeight.BOLD));
            xCell = xSpreadsheet.getCellByPosition(6, nRow);
            xCell.setFormula("=SUM(G" + startRowFromFormula + ":G" + nRow + ")");
            xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
            xPropSet.setPropertyValue("CharWeight", new Float(FontWeight.BOLD));
            xCell = xSpreadsheet.getCellByPosition(7, nRow);
            xCell.setFormula("=SUM(H" + startRowFromFormula + ":H" + nRow + ")");
            xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
            xPropSet.setPropertyValue("CharWeight", new Float(FontWeight.BOLD));
            xCell = xSpreadsheet.getCellByPosition(8, nRow);
            xCell.setFormula("=SUM(I" + startRowFromFormula + ":I" + nRow + ")");
            xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
            xPropSet.setPropertyValue("CharWeight", new Float(FontWeight.BOLD));
            nRow++;

        } catch (java.lang.Exception e) {
            log.error("Ошибка при заполнении отчёта.", e);
            throw new Exception("Ошибка при заполнении отчёта.", e);
        }
    }
}

