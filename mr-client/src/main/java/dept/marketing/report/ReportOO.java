package dept.marketing.report;

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
import java.util.Vector;

/**
 *
 * @author lidashka
 */
public class ReportOO extends OO_new {
    //private static final Logger log = new Log().getLoger(ReportOO.class);
    private static final LogCrutch log = new LogCrutch();
    static String noteOrder;
    static String dateOrder;
    static String nameCl;
    static String str;
    static DefaultTableModel tModel;
    static Vector vdata;
    static Vector vec;
    static Vector vvec;
    static Vector dopVec;
    static String dateReport;
    static String nameTamplate;
    static String[] srt;

    public ReportOO(String nameReport, DefaultTableModel tm, String[] v, Vector vv) {
        tModel = tm;
        nameTamplate = nameReport;
        srt = v;
        vec = vv;
    }

    public ReportOO(String nameReport, String[] v, Vector vv) {
        nameTamplate = nameReport;
        srt = v;
        vec = vv;
    }

    public void createReport(String nameTamplates) throws Exception {
        try {
            connect();
            XComponent currentDocument = openDocumentOld("Templates/" + nameTamplates);

            if (nameTamplates.equals("AnalizRR.ots")) {
                fildAnalizRR(currentDocument);
            } else if (nameTamplates.equals("AnalizVolumeOtgruz.ots")) {
                fildAnalizVolumeOtgruz(currentDocument);
            } else if (nameTamplates.equals("AnalizOtgruzClient.ots")) {
                fildAnalizOtgruzClient(currentDocument);
            }

            JOptionPane.showMessageDialog(null, "Отчёт сформирован", "Отчёт сформирован", JOptionPane.INFORMATION_MESSAGE);

        } catch (java.lang.Exception e) {
            System.out.println(e.getMessage());
            throw new Exception("Ошибка при формировании отчёта ", e);
        }
    }

    private void fildAnalizOtgruzClient(XComponent currentDocument) throws Exception {
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
            XCell xCell;

            String[][] vidIzd = {
                    {"43______", "ЧУЛОЧНО-НОСОЧНЫЙ АССОРТИМЕНТ"},
                    {"4_0_____", "МЕБЕЛЬ"},
                    {"4_1_____", "МУЖСКОЙ АССОРТИМЕНТ"},
                    {"4_2_____", "ЖЕНСКИЙ АССОРТИМЕНТ"},
                    {"4_3_____", "ДЕТСКИЙ АССОРТИМЕНТ"},
                    {"4_5_____", "СПОРТ. ВЗР. АССОРТИМЕНТ"},
                    {"4_6_____", "СПОРТ. ДЕТ. АССОРТИМЕНТ"},
                    {"4_7_____", "ИГРУШКИ"},
                    {"4_8_____", "НАБОРЫ"}};

            int nCol;
            int nRow;
            int kol = 0;

            Vector row;

            //----------------Заполнение шапки документа
            xCell = xSpreadsheet.getCellByPosition(1, 0);
            xCell.setFormula(nameTamplate);

            for (String str : srt) {
                xCell = xSpreadsheet.getCellByPosition(1, 1);
                xCell.setFormula(xCell.getFormula() + str + "\n");
            }

            //----------------Заполнение тела документа

            nRow = 2;

            for (int i = 0; i < vec.size(); i++) {
                row = (Vector) vec.get(i);

                nCol = 0;
                nRow++;

                xCell = xSpreadsheet.getCellByPosition(nCol++, nRow);
                xCell.setValue(++kol);
                xPropSet = UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
                xPropSet.setPropertyValue("TableBorder", aBorder);

                xCell = xSpreadsheet.getCellByPosition(nCol++, nRow);
                xCell.setFormula(row.get(1).toString().trim().toUpperCase());
                xPropSet = UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
                xPropSet.setPropertyValue("TableBorder", aBorder);

                xCell = xSpreadsheet.getCellByPosition(nCol++, nRow);
                xCell.setFormula(row.get(2).toString());
                xPropSet = UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
                xPropSet.setPropertyValue("TableBorder", aBorder);

                xCell = xSpreadsheet.getCellByPosition(nCol++, nRow);
                xCell.setFormula(row.get(3).toString().trim().toUpperCase());
                xPropSet = UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
                xPropSet.setPropertyValue("TableBorder", aBorder);
            }

            nRow++;

            xCell = xSpreadsheet.getCellByPosition(1, nRow);
            xCell.setFormula("ИТОГО");

            xCell = xSpreadsheet.getCellByPosition(2, nRow);
            xCell.setFormula("=SUM(C" + 4 + ":C" + (nRow) + ")");

            xCell = xSpreadsheet.getCellByPosition(3, nRow);
            xCell.setFormula("=SUM(D" + 4 + ":D" + (nRow) + ")");

        } catch (Exception e) {
            log.error("Ошибка при заполнении отчёта fildAnalizOtgruzClient.", e);
            throw new Exception("Ошибка при заполнении отчёта fildAnalizOtgruzClient.", e);
        }
    }

    private void fildAnalizRR(XComponent currentDocument) throws Exception {
        Vector date = new Vector(tModel.getDataVector());
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

            XCell xCell = xSpreadsheet.getCellByPosition(0, 1);

            //----------------Заполнение шапки документа
            xCell.setFormula(xCell.getFormula() + " " + nameTamplate);

            xCell = xSpreadsheet.getCellByPosition(0, 2);
            xCell.setFormula("Изделия");
            xPropSet = UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
            xPropSet.setPropertyValue("TableBorder", aBorder);

            xCell = xSpreadsheet.getCellByPosition(tModel.getColumnCount() - 3, 2);
            xCell.setFormula("Итого");
            xPropSet = UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
            xPropSet.setPropertyValue("TableBorder", aBorder);

            int nCol = 1;
            for (int i = 0; i < srt.length; i++) {
                xCell = xSpreadsheet.getCellByPosition(nCol, 2);
                xCell.setFormula(srt[i].replaceAll("\n", ""));
                xPropSet = UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
                xPropSet.setPropertyValue("TableBorder", aBorder);
                nCol += 2;
            }

            nCol = 0;
            for (int i = 1; i < tModel.getColumnCount(); i++) {
                xCell = xSpreadsheet.getCellByPosition(nCol++, 3);
                xCell.setFormula(tModel.getColumnName(i));
                xPropSet = UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
                xPropSet.setPropertyValue("TableBorder", aBorder);
            }

            //----------------Заполнение тела документа
            nCol = 0;
            int nRow = 4;
            for (Object row : date) {
                for (int i = 1; i < tModel.getColumnCount(); i++) {
                    xCell = xSpreadsheet.getCellByPosition(nCol++, nRow);
                    xCell.setFormula(((Vector) row).get(i).toString());
                    xPropSet = UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
                    xPropSet.setPropertyValue("TableBorder", aBorder);
                }
                nCol = 0;
                nRow++;
            }

            xCell = xSpreadsheet.getCellByPosition(0, nRow);
            xCell.setFormula("ИТОГО");
            xPropSet = UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
            xPropSet.setPropertyValue("TableBorder", aBorder);

            nCol = 1;
            for (int i = 2; i < ((Vector) vec.get(0)).size(); i++) {
                xCell = xSpreadsheet.getCellByPosition(nCol++, nRow);
                xCell.setFormula(((Vector) vec.get(0)).elementAt(i).toString());
                xPropSet = UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
                xPropSet.setPropertyValue("TableBorder", aBorder);
            }
        } catch (java.lang.Exception e) {
            log.error("Ошибка при заполнении отчёта.", e);
            throw new Exception("Ошибка при заполнении отчёта.", e);
        }
    }

    private void fildAnalizVolumeOtgruz(XComponent currentDocument) throws Exception {
        Vector date = new Vector(tModel.getDataVector());
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

            XCell xCell = xSpreadsheet.getCellByPosition(0, 1);

            //----------------Заполнение шапки документа
            xCell.setFormula(xCell.getFormula() + " " + nameTamplate);


            //----------------Заполнение тела документа
            int nCol = 0;
            int nRow = 4;
            for (Object row : date) {
                for (int i = 1; i < tModel.getColumnCount(); i++) {
                    xCell = xSpreadsheet.getCellByPosition(nCol++, nRow);
                    xCell.setFormula(((Vector) row).get(i).toString());
                    xPropSet = UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
                    xPropSet.setPropertyValue("TableBorder", aBorder);
                }
                nCol = 0;
                nRow++;
            }

            xCell = xSpreadsheet.getCellByPosition(0, nRow);
            xCell.setFormula("ИТОГО");
            xPropSet = UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
            xPropSet.setPropertyValue("TableBorder", aBorder);

            nCol = 1;
            for (int i = 2; i < ((Vector) vec.get(0)).size(); i++) {
                xCell = xSpreadsheet.getCellByPosition(nCol++, nRow);
                xCell.setFormula(((Vector) vec.get(0)).elementAt(i).toString());
                xPropSet = UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
                xPropSet.setPropertyValue("TableBorder", aBorder);
            }
        } catch (java.lang.Exception e) {
            log.error("Ошибка при заполнении отчёта.", e);
            throw new Exception("Ошибка при заполнении отчёта.", e);
        }
    }

}
