/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dept.sklad.ostatki;

import by.march8.ecs.framework.common.LogCrutch;
import com.sun.star.container.NoSuchElementException;
import com.sun.star.container.XNamed;
import com.sun.star.lang.WrappedTargetException;
import com.sun.star.lang.XComponent;
import com.sun.star.sheet.XSpreadsheet;
import com.sun.star.sheet.XSpreadsheetDocument;
import com.sun.star.sheet.XSpreadsheets;
import com.sun.star.table.CellHoriJustify;
import com.sun.star.table.TableBorder;
import com.sun.star.table.XCell;
import com.sun.star.uno.UnoRuntime;
import workOO.OO_new;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Calendar;

/**
 *
 * @author user
 */
@SuppressWarnings("deprecation")
public class RemainsReports extends OO_new {

    //private static final Logger log = new Log().getLoger(RemainsReports.class);
    private static final LogCrutch log = new LogCrutch();
    private String[] month = {"января", "февраля", "марта", "апреля", "мая", "июня", "июля",
            "августа", "сентября", "октября", "ноября", "декабря"};
    private ArrayList<ArrayList<Object>> data;
    private ArrayList<Object> buffer;
    private int lineStart;
    private float charSize;
    private com.sun.star.beans.XPropertySet xPropSet;
    private com.sun.star.table.BorderLine aLine;
    private com.sun.star.table.TableBorder aBorder;
    private XNamed xNamed;
    private XCell xCell;
    private XSpreadsheetDocument xSpreadsheetDocument;
    private XSpreadsheets xSpreadsheets;
    private Object sheet;
    private XSpreadsheet xSpreadsheet;
    private String nowDate;
    private Calendar nowDateCalendar;
    private String nar;

    /**
     *
     * @param printData
     */
    public RemainsReports(ArrayList<ArrayList<Object>> printData) {
        super();
        data = printData;
    }

    /**
     *
     * @param nameTamplates
     */
    public void createReport(String nameTamplates) {
        try {
            connect();
            XComponent currentDocument = openDocumentOld("Templates/" + nameTamplates);
            if (nameTamplates.equals("NowRemains.ots")) {
                reportNowRemains(currentDocument, nameTamplates);
            }
            if (nameTamplates.equals("NowRev.ots")) {
                reportNowRemains(currentDocument, nameTamplates);
            }
        } catch (java.lang.Exception e) {
            System.err.println("Error in createReport in class SkladOO. \n Please report it to the developer \n" + e);
            log.error("Error in createReport in class SkladOO.", e);
            JOptionPane.showMessageDialog(null, "Error in method createReport in class SkladOO. \n Please report it to the developer \n");
        }
    }

    /**
     *
     * @param currentDocument
     * @throws Exception
     */
    public void reportNowRemains(XComponent currentDocument, String nameDocument) throws Exception {
        try {

            nowDateCalendar = Calendar.getInstance();
            nowDate = String.valueOf(nowDateCalendar.get(Calendar.DAY_OF_MONTH)) + " " + month[nowDateCalendar.get(Calendar.MONTH)].toUpperCase() + " " + String.valueOf(nowDateCalendar.get(Calendar.YEAR)) + " г.";

            xSpreadsheetDocument = UnoRuntime.queryInterface(XSpreadsheetDocument.class, currentDocument);
            xSpreadsheets = xSpreadsheetDocument.getSheets();
            sheet = xSpreadsheets.getByName("Лист1");
            xSpreadsheet = UnoRuntime.queryInterface(XSpreadsheet.class, sheet);

            xNamed = UnoRuntime.queryInterface(XNamed.class, xSpreadsheet);

            if (nameDocument.equals("NowRemains.ots")) {
                charSize = 10;
                lineStart = 2;
                //xNamed.setName("Текущие остатки");
            }
            if (nameDocument.equals("NowRev.ots")) {
                charSize = 6;
                lineStart = 3;
                //xNamed.setName("Оборотная ведомость");
            }

            aLine = new com.sun.star.table.BorderLine();
            aLine.Color = 0x000000;
            aLine.InnerLineWidth = 0;
            aLine.LineDistance = 0;
            aLine.OuterLineWidth = 25;
            aBorder = new com.sun.star.table.TableBorder();
            aBorder.BottomLine = aLine;
            aBorder.TopLine = aLine;
            aBorder.LeftLine = aLine;
            aBorder.RightLine = aLine;
            aBorder.IsBottomLineValid = true;
            aBorder.IsTopLineValid = true;
            aBorder.IsLeftLineValid = true;
            aBorder.IsRightLineValid = true;

            //----------------Заполнение шапки документа
            setPropertyAndValue(xSpreadsheet, 0, 0, "Ведомость по текущим остаткам на " + nowDate, charSize, aBorder, "CENTER");
            //----------------Заполнение тела документа
            nar = "";
            for (int i = 0; i < data.size(); i++) {
                buffer = data.get(i);

                if (nar.equals(buffer.get(1).toString().trim())) {
                    for (int j = 0; j < buffer.size(); j++) {
                        setPropertyAndValue(xSpreadsheet, j, lineStart + i, "'" + buffer.get(j).toString(), charSize, aBorder, "LEFT");
                    }
                } else {
                    nar = buffer.get(1).toString().trim();
                    setPropertyAndValue(xSpreadsheet, 0, lineStart + i++, "2313213132132", charSize, aBorder, "LEFT");
                }
            }
        } catch (NoSuchElementException ex) {
            System.err.println("Error in reportNowRemains in class RemainsReports. \nPlease contact developer" + ex);
            log.error("Error in reportNowRemains in class RemainsReports.", ex);
            JOptionPane.showMessageDialog(null, "Error in method reportNowRemains in class RemainsReports. \nPlease contact developer");
        } catch (WrappedTargetException ex) {
            System.err.println("Error in reportNowRemains in class RemainsReports. \nPlease contact developer" + ex);
            log.error("Error in reportNowRemains in class RemainsReports.", ex);
            JOptionPane.showMessageDialog(null, "Error in method reportNowRemains in class RemainsReports. \nPlease contact developer");
        }
    }

    /**
     * Задает свойства ячейке.
     *
     * @param xSpreadsheet - Лист документа
     * @param column - Номер столбца для вставки
     * @param row - Номер строки для вставки
     * @param value - Что будем записывать в строковом формате
     * @param charSize - Размер шрифта
     * @param aBorder - Рисовать границу (параметры переменной должны быть
     * заданны до передачи в эту функцию). Если NULL - то граница не рисуется
     * @param JustifyHor - Выравнять текст по горизонтали (CENTER,RIGHT,LEFT)
     */
    public void setPropertyAndValue(XSpreadsheet xSpreadsheet, int column, int row, String value, float charSize, TableBorder aBorder, String JustifyHor) {
        try {
            xCell = null;
            xPropSet = null;
            xCell = xSpreadsheet.getCellByPosition(column, row);
            xCell.setFormula(value);
            xPropSet = UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
            xPropSet.setPropertyValue("CharHeight", charSize);
            if (aBorder != null) {
                xPropSet.setPropertyValue("TableBorder", aBorder);
            }
            if (JustifyHor.equals("RIGHT")) {
                xPropSet.setPropertyValue("HoriJustify", CellHoriJustify.RIGHT);
            }
            if (JustifyHor.equals("CENTER")) {
                xPropSet.setPropertyValue("HoriJustify", CellHoriJustify.CENTER);
            }
            if (JustifyHor.equals("LEFT")) {
                xPropSet.setPropertyValue("HoriJustify", CellHoriJustify.LEFT);
            }

        } catch (Exception ex) {
            System.err.println("Error in setPropertyAndValue in class SkladOO. \n Please report it to the developer \n" + ex);
            log.error("Error in setPropertyAndValue in class SkladOO.", ex);
            JOptionPane.showMessageDialog(null, "Error in method setPropertyAndValue in class SkladOO. \n Please report it to the developer \n");
        }
    }
}
