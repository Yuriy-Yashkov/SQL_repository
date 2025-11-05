package dept.marketing.cena;

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
import java.util.Iterator;
import java.util.Vector;

/**
 * @author lidashka
 */
@SuppressWarnings("all")
public class CenaOO extends OO_new {
    //private static final Logger log = new Log().getLoger(CenaOO.class);
    private static final LogCrutch log = new LogCrutch();
    static String noteOrder;
    static String dateOrder;
    static String nameCl;
    /**
     * Вспомогательная информация (курсы валют)
     */
    static String str;
    static DefaultTableModel tModel;
    /**
     * Табличная часть документа
     */
    static Vector vdata;
    /**
     * Повышающие коэффициенты расчета
     */
    static Vector vec;
    /**
     * Список клиентов
     */
    static Vector vvec;
    static Vector dopVec;
    /**
     * Дата отчета
     */
    static String dateReport;
    /**
     * Номер документа
     */
    static String nameTamplate;

    public CenaOO(String nameReport, DefaultTableModel tm, Vector v) {
        tModel = tm;
        nameTamplate = nameReport;
        vec = v;
    }

    public CenaOO(String nameReport, DefaultTableModel tm, Vector v, String s) {
        tModel = tm;
        nameTamplate = new String(nameReport);
        vec = v;
        str = s;
    }

    public CenaOO(String nameReport, Vector data, Vector v, Vector vv, String date, String s) {
        nameTamplate = nameReport;
        dateReport = date;
        vdata = data;
        vec = v;
        vvec = vv;
        str = s;
    }

    private static void fildPriceHistory(XComponent currentDocument) throws Exception {
        Vector date = new Vector(tModel.getDataVector());
        Vector tmpv = null;
        String izdSar = "";
        String izdNar = "";
        String izdNgpr = "";
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
            aBorder.BottomLine = aLine;
            aBorder.IsBottomLineValid = true;

            //----------------Заполнение шапки документа
            XCell xCell = xSpreadsheet.getCellByPosition(0, 0);
            xCell.setFormula(nameTamplate);

            int nCol = 0;
            for (int i = 0; i < tModel.getColumnCount(); i++) {
                if (vec.contains(i)) {
                    xCell = xSpreadsheet.getCellByPosition(nCol++, 1);
                    xCell.setFormula(tModel.getColumnName(i));
                    xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
                    xPropSet.setPropertyValue("TableBorder", aBorder);
                }
            }

            //----------------Заполнение тела документа
            int nRow = 2;
            for (Object row : date) {
                nCol = 0;
                tmpv = (Vector) row;

                if (!izdSar.equals(tmpv.get(0).toString()) || !izdNar.equals(tmpv.get(2).toString()) || !izdNgpr.equals(tmpv.get(3).toString())) {
                    nRow++;
                    xCell = xSpreadsheet.getCellByPosition(0, nRow);
                    xCell.setFormula("     " + tmpv.get(2).toString() + "   " + tmpv.get(1).toString() + "   " + tmpv.get(3).toString());

                    for (int i = 0; i < tModel.getColumnCount(); i++) {
                        if (vec.contains(i)) {
                            xCell = xSpreadsheet.getCellByPosition(nCol, nRow);
                            xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
                            xPropSet.setPropertyValue("TableBorder", aBorder);

                            xCell = xSpreadsheet.getCellByPosition(nCol, nRow + 1);
                            if (!tmpv.get(i).toString().equals(""))
                                xCell.setFormula("'" + tmpv.get(i).toString());

                            nCol++;
                        }
                    }
                    nRow += 2;
                    izdSar = tmpv.get(0).toString();
                    izdNar = tmpv.get(2).toString();
                    izdNgpr = tmpv.get(3).toString();
                } else {
                    for (int i = 0; i < tModel.getColumnCount(); i++) {
                        if (vec.contains(i)) {
                            xCell = xSpreadsheet.getCellByPosition(nCol++, nRow);
                            if (!tmpv.get(i).toString().equals(""))
                                xCell.setFormula("'" + tmpv.get(i).toString());
                        }
                    }
                    nRow++;
                }
            }
        } catch (Exception e) {
            log.error("Ошибка при заполнении отчёта", e);
            JOptionPane.showMessageDialog(null, "Ошибка при заполнении отчёта", "Ошибка!!!", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void createReport(String nameTamplates) throws Exception {
        try {
            connect();
            XComponent currentDocument = openDocumentOld("Templates/" + nameTamplates);

            if (nameTamplates.equals("PriceTable.ots")) {
                fildPriceTable(currentDocument);
            }
            if (nameTamplates.equals("PriceTableDocument.ots")) {
                fildPriceTableDocument(currentDocument);
            }
            if (nameTamplates.equals("PriceTableDocumentPBCRF.ots")) {
                fildPriceTableDocumentPBCRF(currentDocument);
            }
            if (nameTamplates.equals("PriceTableDocumentPBCRFSimple.ots")) {
                fildPriceTableDocumentPBCRFSimple(currentDocument);
            }
            if (nameTamplates.equals("PriceHistory.ots")) {
                fildPriceHistory(currentDocument);
            }

            JOptionPane.showMessageDialog(null, "Отчёт сформирован", "Отчёт сформирован", JOptionPane.INFORMATION_MESSAGE);

        } catch (java.lang.Exception e) {
            System.out.println(e.getMessage());
            throw new Exception("Ошибка при формировании отчёта ", e);
        }
    }

    private void fildPriceTable(XComponent currentDocument) throws Exception {
        Vector date = new Vector(tModel.getDataVector());
        try {
            XSpreadsheetDocument xSpreadsheetDocument = (XSpreadsheetDocument)
                    UnoRuntime.queryInterface(XSpreadsheetDocument.class, currentDocument);
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

            XCell xCell = xSpreadsheet.getCellByPosition(0, 0);

            //----------------Заполнение шапки документа
            xCell.setFormula(nameTamplate);

            int nCol = 0;
            for (int i = 0; i < tModel.getColumnCount(); i++) {
                if (vec.contains(i)) {
                    xCell = xSpreadsheet.getCellByPosition(nCol++, 1);
                    xCell.setFormula(tModel.getColumnName(i));
                    xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
                    xPropSet.setPropertyValue("TableBorder", aBorder);
                }
            }

            //----------------Заполнение тела документа
            nCol = 0;
            int nRow = 2;
            for (Object row : date) {
                if (((Vector) row).get(0).toString().equals("true")) {
                    for (int i = 0; i < tModel.getColumnCount(); i++) {
                        if (vec.contains(i)) {
                            xCell = xSpreadsheet.getCellByPosition(nCol++, nRow);
                            xCell.setFormula(((Vector) row).get(i).toString());
                            xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
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

    private void fildPriceTableDocument(XComponent currentDocument) throws Exception {
        Vector data = new Vector(tModel.getDataVector());
        try {
            XSpreadsheetDocument xSpreadsheetDocument = (XSpreadsheetDocument)
                    UnoRuntime.queryInterface(XSpreadsheetDocument.class, currentDocument);
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

            XCell xCell = xSpreadsheet.getCellByPosition(0, 0);

            //----------------Заполнение шапки документа

            xCell = xSpreadsheet.getCellByPosition(11, 6);
            xCell.setFormula(nameTamplate);

            xCell = xSpreadsheet.getCellByPosition(10, 7);
            xCell.setFormula(str);

            //----------------Заполнение тела документа

            int nCol = 1;
            int nRow = 11;
            for (Object row : data) {
                if (((Vector) row).get(0).toString().equals("true")) {
                    for (int i = 0; i < tModel.getColumnCount(); i++) {
                        if (vec.contains(i)) {
                            xCell = xSpreadsheet.getCellByPosition(nCol++, nRow);
                            xCell.setFormula(((Vector) row).get(i).toString());
                            xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
                            xPropSet.setPropertyValue("TableBorder", aBorder);
                        }
                    }
                    nCol = 1;
                    nRow++;
                }
            }

            nRow += 2;

            xCell = xSpreadsheet.getCellByPosition(1, nRow++);
            xCell.setFormula("Договорную цену в рублях РФ подтверждаю:");
            xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
            xPropSet.setPropertyValue("HoriJustify", com.sun.star.table.CellHoriJustify.LEFT);
            xPropSet.setPropertyValue("CharHeight", (float) (13));

            xCell = xSpreadsheet.getCellByPosition(1, nRow);
            xCell.setFormula("Заместитель директора по маркетингу и сбыту");
            xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
            xPropSet.setPropertyValue("HoriJustify", com.sun.star.table.CellHoriJustify.LEFT);
            xPropSet.setPropertyValue("CharHeight", (float) (15));

            xCell = xSpreadsheet.getCellByPosition(13, nRow++);
            xCell.setFormula("Можная Х.Е.");
            xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
            xPropSet.setPropertyValue("HoriJustify", com.sun.star.table.CellHoriJustify.LEFT);
            xPropSet.setPropertyValue("CharHeight", (float) (15));

            nRow += 2;

            xCell = xSpreadsheet.getCellByPosition(1, nRow++);
            xCell.setFormula("Нормы расхода сырья и вспомогательных материалов, состав полотна -");
            xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
            xPropSet.setPropertyValue("HoriJustify", com.sun.star.table.CellHoriJustify.LEFT);
            xPropSet.setPropertyValue("CharHeight", (float) (13));

            xCell = xSpreadsheet.getCellByPosition(1, nRow++);
            xCell.setFormula("подтверждаю:");
            xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
            xPropSet.setPropertyValue("HoriJustify", com.sun.star.table.CellHoriJustify.LEFT);
            xPropSet.setPropertyValue("CharHeight", (float) (13));

            xCell = xSpreadsheet.getCellByPosition(1, nRow);
            xCell.setFormula("Помощник руководителя организации по производственным вопросам и закупкам");
            xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
            xPropSet.setPropertyValue("HoriJustify", com.sun.star.table.CellHoriJustify.LEFT);
            xPropSet.setPropertyValue("CharHeight", (float) (15));

            xCell = xSpreadsheet.getCellByPosition(13, nRow++);
            xCell.setFormula("Карпей М.И.");
            xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
            xPropSet.setPropertyValue("HoriJustify", com.sun.star.table.CellHoriJustify.LEFT);
            xPropSet.setPropertyValue("CharHeight", (float) (15));

            nRow += 2;

            xCell = xSpreadsheet.getCellByPosition(1, nRow++);
            xCell.setFormula("Расчет себестоимости в белорусских рублях подтверждаю:");
            xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
            xPropSet.setPropertyValue("HoriJustify", com.sun.star.table.CellHoriJustify.LEFT);
            xPropSet.setPropertyValue("CharHeight", (float) (13));

            xCell = xSpreadsheet.getCellByPosition(1, nRow);
            xCell.setFormula("Заместитель директора по экономике");
            xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
            xPropSet.setPropertyValue("HoriJustify", com.sun.star.table.CellHoriJustify.LEFT);
            xPropSet.setPropertyValue("CharHeight", (float) (15));

            xCell = xSpreadsheet.getCellByPosition(13, nRow++);
            xCell.setFormula("Грищенко Л.В.");
            xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
            xPropSet.setPropertyValue("HoriJustify", com.sun.star.table.CellHoriJustify.LEFT);
            xPropSet.setPropertyValue("CharHeight", (float) (15));

        } catch (Exception e) {
            log.error("Ошибка при заполнении отчёта.", e);
            JOptionPane.showMessageDialog(null, "Ошибка при заполнении отчёта.", "Ошибка!", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void fildPriceTableDocumentPBCRF(XComponent currentDocument) {
        try {
            XSpreadsheetDocument xSpreadsheetDocument = (XSpreadsheetDocument)
                    UnoRuntime.queryInterface(XSpreadsheetDocument.class, currentDocument);
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

            XCell xCell = xSpreadsheet.getCellByPosition(0, 0);

            //----------------Заполнение шапки документа

            xCell = xSpreadsheet.getCellByPosition(15, 6);
            xCell.setFormula(str);

            xCell = xSpreadsheet.getCellByPosition(1, 8);
            xCell.setFormula("Перечень №" + nameTamplate + " базовых цен для оптовых покупателей РФ при реализации за пределы Республики Беларусь с учетом повышающих коэффициентов");

            int nCol = 7;
            int nRow = 8;
            for (int i = 0; i < 5; i++) {
                xCell = xSpreadsheet.getCellByPosition(nCol, nRow);
                xCell.setFormula(xCell.getFormula() + " " + vec.get(i));

                xCell = xSpreadsheet.getCellByPosition(nCol, nRow + 1);
                xCell.setFormula(xCell.getFormula() + " " + vvec.get(i));

                nCol += 3;
            }

            nCol = 7;
            nRow += 2;
            for (int i = 0; i < 15; i++) {
                xCell = xSpreadsheet.getCellByPosition(nCol++, nRow);
                xCell.setFormula(xCell.getFormula() + " с " + dateReport);
            }

            //----------------Заполнение тела документа

            for (Iterator it = vdata.iterator(); it.hasNext(); ) {
                Vector row = (Vector) it.next();
                nCol = 1;
                nRow++;
                for (Iterator it1 = row.iterator(); it1.hasNext(); ) {
                    Object element = it1.next();
                    xCell = xSpreadsheet.getCellByPosition(nCol++, nRow);
                    xCell.setFormula(xCell.getFormula() + element);
                    xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
                    xPropSet.setPropertyValue("TableBorder", aBorder);
                }
            }

            nCol = 1;
            nRow += 4;
            xCell = xSpreadsheet.getCellByPosition(1, nRow++);
            xCell.setFormula("Договорную цену в рублях РФ подтверждаю:");
            xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
            xPropSet.setPropertyValue("HoriJustify", com.sun.star.table.CellHoriJustify.LEFT);
            xPropSet.setPropertyValue("CharHeight", (float) (16));

            xCell = xSpreadsheet.getCellByPosition(1, nRow);
            xCell.setFormula("Заместитель директора по маркетингу и сбыту");
            xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
            xPropSet.setPropertyValue("HoriJustify", com.sun.star.table.CellHoriJustify.LEFT);
            xPropSet.setPropertyValue("CharHeight", (float) (17));

            xCell = xSpreadsheet.getCellByPosition(20, nRow++);
            xCell.setFormula("Можная Х.Е.");
            xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
            xPropSet.setPropertyValue("HoriJustify", com.sun.star.table.CellHoriJustify.LEFT);
            xPropSet.setPropertyValue("CharHeight", (float) (17));

            nRow += 2;
            xCell = xSpreadsheet.getCellByPosition(1, nRow++);
            xCell.setFormula("Нормы расхода сырья и вспомогательных материалов, состав полотна -");
            xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
            xPropSet.setPropertyValue("HoriJustify", com.sun.star.table.CellHoriJustify.LEFT);
            xPropSet.setPropertyValue("CharHeight", (float) (16));

            xCell = xSpreadsheet.getCellByPosition(1, nRow++);
            xCell.setFormula("подтверждаю:");
            xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
            xPropSet.setPropertyValue("HoriJustify", com.sun.star.table.CellHoriJustify.LEFT);
            xPropSet.setPropertyValue("CharHeight", (float) (16));

            xCell = xSpreadsheet.getCellByPosition(1, nRow);
            xCell.setFormula("Помощник руководителя организации по производственным вопросам и закупкам");
            xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
            xPropSet.setPropertyValue("HoriJustify", com.sun.star.table.CellHoriJustify.LEFT);
            xPropSet.setPropertyValue("CharHeight", (float) (17));

            xCell = xSpreadsheet.getCellByPosition(20, nRow++);
            xCell.setFormula("Карпей М.И.");
            xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
            xPropSet.setPropertyValue("HoriJustify", com.sun.star.table.CellHoriJustify.LEFT);
            xPropSet.setPropertyValue("CharHeight", (float) (17));

            nRow += 2;
            xCell = xSpreadsheet.getCellByPosition(1, nRow++);
            xCell.setFormula("Расчет себестоимости в белорусских рублях подтверждаю:");
            xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
            xPropSet.setPropertyValue("HoriJustify", com.sun.star.table.CellHoriJustify.LEFT);
            xPropSet.setPropertyValue("CharHeight", (float) (16));

            xCell = xSpreadsheet.getCellByPosition(1, nRow);
            xCell.setFormula("Заместитель директора по экономике");
            xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
            xPropSet.setPropertyValue("HoriJustify", com.sun.star.table.CellHoriJustify.LEFT);
            xPropSet.setPropertyValue("CharHeight", (float) (17));

            xCell = xSpreadsheet.getCellByPosition(20, nRow++);
            xCell.setFormula("Грищенко Л.В.");
            xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
            xPropSet.setPropertyValue("HoriJustify", com.sun.star.table.CellHoriJustify.LEFT);
            xPropSet.setPropertyValue("CharHeight", (float) (17));

        } catch (Exception e) {
            log.error("Ошибка при заполнении отчёта", e);
            JOptionPane.showMessageDialog(null, "Ошибка при заполнении отчёта", "Ошибка!!!", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void fildPriceTableDocumentPBCRFSimple(XComponent currentDocument) {
        try {
            XSpreadsheetDocument xSpreadsheetDocument = (XSpreadsheetDocument)
                    UnoRuntime.queryInterface(XSpreadsheetDocument.class, currentDocument);
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

            XCell xCell = xSpreadsheet.getCellByPosition(0, 0);

            //----------------Заполнение шапки документа

            xCell = xSpreadsheet.getCellByPosition(4, 6);
            xCell.setFormula(str);

            xCell = xSpreadsheet.getCellByPosition(1, 8);
            xCell.setFormula("Перечень №" + nameTamplate + " отпускных цен для оптовых покупателей при реализации продукции собственного производства за пределы Республики Беларусь");
            int nCol = 7;
            int nRow = 8;
            for (int i = 0; i < 1; i++) {
                xCell = xSpreadsheet.getCellByPosition(nCol, nRow);
                xCell.setFormula(xCell.getFormula() + " " + vec.get(i));

                xCell = xSpreadsheet.getCellByPosition(nCol, nRow + 1);
                xCell.setFormula(xCell.getFormula() + " " + vvec.get(i));

                nCol += 3;
            }

            nCol = 7;
            nRow += 2;
            for (int i = 0; i < 3; i++) {
                xCell = xSpreadsheet.getCellByPosition(nCol++, nRow);
                xCell.setFormula(xCell.getFormula() + " с " + dateReport);
            }

            //----------------Заполнение тела документа

            for (Iterator it = vdata.iterator(); it.hasNext(); ) {
                Vector row = (Vector) it.next();
                nCol = 1;
                nRow++;
                for (Iterator it1 = row.iterator(); it1.hasNext(); ) {
                    Object element = it1.next();
                    xCell = xSpreadsheet.getCellByPosition(nCol++, nRow);
                    xCell.setFormula(xCell.getFormula() + element);
                    xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
                    xPropSet.setPropertyValue("TableBorder", aBorder);
                }
            }

            nCol = 1;
            nRow += 4;
            /*xCell = xSpreadsheet.getCellByPosition(1, nRow++);
            xCell.setFormula("Договорную цену в рублях РФ подтверждаю:");
            xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
            xPropSet.setPropertyValue("HoriJustify", com.sun.star.table.CellHoriJustify.LEFT);
            xPropSet.setPropertyValue("CharHeight", (float) (16));
            */


            xCell = xSpreadsheet.getCellByPosition(1, nRow);
            xCell.setFormula("Заместитель директора по коммерческим вопросам");
            xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
            xPropSet.setPropertyValue("HoriJustify", com.sun.star.table.CellHoriJustify.LEFT);
            xPropSet.setPropertyValue("CharHeight", (float) (17));

            xCell = xSpreadsheet.getCellByPosition(8, nRow++);
            xCell.setFormula("Т.В. Бандуренко");
            xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
            xPropSet.setPropertyValue("HoriJustify", com.sun.star.table.CellHoriJustify.LEFT);
            xPropSet.setPropertyValue("CharHeight", (float) (17));


            nRow += 2;
            xCell = xSpreadsheet.getCellByPosition(1, nRow++);
            xCell.setFormula("Помощник руководителя организации по производственным вопросам и закупкам");
            xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
            xPropSet.setPropertyValue("HoriJustify", com.sun.star.table.CellHoriJustify.LEFT);
            xPropSet.setPropertyValue("CharHeight", (float) (16));

            xCell = xSpreadsheet.getCellByPosition(8, nRow++);
            xCell.setFormula("М.И. Карпей ");
            xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
            xPropSet.setPropertyValue("HoriJustify", com.sun.star.table.CellHoriJustify.LEFT);
            xPropSet.setPropertyValue("CharHeight", (float) (16));

            nRow += 2;

            xCell = xSpreadsheet.getCellByPosition(1, nRow);
            xCell.setFormula("Заместитель директора по экономике");
            xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
            xPropSet.setPropertyValue("HoriJustify", com.sun.star.table.CellHoriJustify.LEFT);
            xPropSet.setPropertyValue("CharHeight", (float) (17));

            xCell = xSpreadsheet.getCellByPosition(8, nRow++);
            xCell.setFormula("_________________");
            xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
            xPropSet.setPropertyValue("HoriJustify", com.sun.star.table.CellHoriJustify.LEFT);
            xPropSet.setPropertyValue("CharHeight", (float) (17));

/*            nRow += 2;
            xCell = xSpreadsheet.getCellByPosition(1, nRow++);
            xCell.setFormula("Расчет себестоимости в белорусских рублях подтверждаю:");
            xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
            xPropSet.setPropertyValue("HoriJustify", com.sun.star.table.CellHoriJustify.LEFT);
            xPropSet.setPropertyValue("CharHeight", (float) (16));

            xCell = xSpreadsheet.getCellByPosition(1, nRow);
            xCell.setFormula("Заместитель директора по экономике");
            xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
            xPropSet.setPropertyValue("HoriJustify", com.sun.star.table.CellHoriJustify.LEFT);
            xPropSet.setPropertyValue("CharHeight", (float) (17));

            xCell = xSpreadsheet.getCellByPosition(8, nRow++);
            xCell.setFormula("Грищенко Л.В.");
            xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
            xPropSet.setPropertyValue("HoriJustify", com.sun.star.table.CellHoriJustify.LEFT);
            xPropSet.setPropertyValue("CharHeight", (float) (17));*/

        } catch (Exception e) {
            log.error("Ошибка при заполнении отчёта в методе fildPriceTableDocumentPBCRFSimple", e);
            JOptionPane.showMessageDialog(null, "Ошибка при заполнении отчёта", "Ошибка!!!", JOptionPane.ERROR_MESSAGE);
        }
    }
}
