package by.march8.ecs.application.modules.production.report;

import by.march8.ecs.application.modules.production.model.JournalEanCodeViewReportData;
import by.march8.entities.production.EanCodeByColorsView;
import com.sun.star.lang.XComponent;
import com.sun.star.sheet.XSpreadsheet;
import com.sun.star.sheet.XSpreadsheetDocument;
import com.sun.star.sheet.XSpreadsheets;
import com.sun.star.table.XCell;
import com.sun.star.uno.UnoRuntime;
import workOO.OO_new;

import javax.swing.*;
import java.util.List;

/**
 * Created by lidashka on 21.12.2018.
 */
public class JournalEanCodeViewOO extends OO_new {
    String nameTamplates;
    JournalEanCodeViewReportData reportData;

    public JournalEanCodeViewOO(String nameTamplates, JournalEanCodeViewReportData reportData) {
        super();
        this.nameTamplates = nameTamplates;
        this.reportData = reportData;
    }

    public void createReport(String nameTamplates) throws Exception {
        try {
            connect();
            XComponent currentDocument = openDocumentOld("Templates/" + nameTamplates);

            if (nameTamplates.equals("JournalEanCodeRoport.ots")) {
                fildJournalEanCodeRoport(currentDocument);
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

    private void fildJournalEanCodeRoport(XComponent currentDocument) throws Exception {
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

            int nCol = 1;
            int nRow = 1;
            int number = 0;
            String current;
            String history = "";

            XCell xCell = xSpreadsheet.getCellByPosition(nCol, nRow);

            //----------------Заполнение шапки документа
            xCell.setFormula(nameTamplates);

            //----------------Заполнение тела документа

            nRow = 2;

            List<EanCodeByColorsView> sheetItemList = reportData.getData();

            for (int i = 0; i < sheetItemList.size(); i++) {
                EanCodeByColorsView rowItem = sheetItemList.get(i);

                current = rowItem.getFas() + rowItem.getSar() + rowItem.getNar() + rowItem.getNaim();

                nRow++;

                if (history.equals(current)) {
                    nCol = 5;

                    xCell = xSpreadsheet.getCellByPosition(nCol++, nRow);
                    xCell.setFormula(rowItem.getSizePrint().trim());

                    xCell = xSpreadsheet.getCellByPosition(nCol++, nRow);
                    xCell.setValue(rowItem.getSrt());

                    xCell = xSpreadsheet.getCellByPosition(nCol++, nRow);
                    xCell.setFormula(rowItem.getColor().trim());

                    xCell = xSpreadsheet.getCellByPosition(nCol++, nRow);
                    xCell.setFormula(rowItem.getEanWithColor().trim());

                } else {


                    i--;
                    nRow++;
                    nCol = 1;

                    history = current;

                    xCell = xSpreadsheet.getCellByPosition(nCol++, nRow);
                    xCell.setValue(++number);

                    xCell = xSpreadsheet.getCellByPosition(nCol++, nRow);
                    xCell.setFormula(rowItem.getFas());

                    xCell = xSpreadsheet.getCellByPosition(nCol++, nRow);
                    xCell.setFormula(rowItem.getNaim().trim());

                    xCell = xSpreadsheet.getCellByPosition(nCol++, nRow);
                    xCell.setFormula(rowItem.getNar().trim());

                    for (int j = 1; j < 9; j++) {
                        xCell = xSpreadsheet.getCellByPosition(j, nRow);
                        xPropSet = UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
                        xPropSet.setPropertyValue("TableBorder", aBorder);
                    }
                }
            }
        } catch (java.lang.Exception e) {
            throw new Exception("Ошибка при заполнении отчёта.", e);
        }
    }
}
