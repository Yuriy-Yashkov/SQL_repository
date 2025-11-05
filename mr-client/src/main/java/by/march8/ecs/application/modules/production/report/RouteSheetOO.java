package by.march8.ecs.application.modules.production.report;

import by.march8.api.utils.DateUtils;
import by.march8.ecs.application.modules.production.model.RouteSheetReportData;
import by.march8.entities.production.RouteSheetItem;
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
 * Created by lidashka on 22.10.2018.
 */
public class RouteSheetOO extends OO_new {
    String nameTamplates;
    RouteSheetReportData reportData;

    public RouteSheetOO(String nameTamplates, RouteSheetReportData reportData) {
        super();
        this.nameTamplates = nameTamplates;
        this.reportData = reportData;
    }

    public void createReport(String nameTamplates) throws Exception {
        try {
            connect();
            XComponent currentDocument = openDocumentOld("Templates/" + nameTamplates);

            if (nameTamplates.equals("RouteSheetItemsRoport.ots")) {
                fildRouteSheetItemsRoport(currentDocument);
            } else if (nameTamplates.equals("RouteSheetModelsRoport.ots")) {
                fildRouteSheetModelsRoport(currentDocument);
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

    private void fildRouteSheetItemsRoport(XComponent currentDocument) throws Exception {
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
            int startRow = 4;

            XCell xCell = xSpreadsheet.getCellByPosition(nCol, nRow);

            //----------------Заполнение шапки документа
            xCell.setFormula(nameTamplates);

            //----------------Заполнение тела документа

            nRow = 4;

            List<RouteSheetItem> sheetItemList = reportData.getData();

            for (int i = 0; i < sheetItemList.size(); i++) {
                RouteSheetItem rowItem = sheetItemList.get(i);

                current = rowItem.getFas() + rowItem.getNaim() + rowItem.getNar() + rowItem.getRst() + rowItem.getRzm();

                nRow++;

                if (history.equals(current)) {
                    nCol = 7;

                    xCell = xSpreadsheet.getCellByPosition(nCol++, nRow);
                    xCell.setFormula(rowItem.getColor().trim());

                    xCell = xSpreadsheet.getCellByPosition(nCol++, nRow);
                    xCell.setValue(rowItem.getKol());

                    xCell = xSpreadsheet.getCellByPosition(nCol++, nRow);
                    xCell.setValue(rowItem.getRoutingNumber());

                    xCell = xSpreadsheet.getCellByPosition(nCol++, nRow);
                    xCell.setFormula("'" + String.valueOf(DateUtils.getNormalDateFormat(rowItem.getRoutingDate())));

                    xCell = xSpreadsheet.getCellByPosition(nCol++, nRow);
                    xCell.setValue(rowItem.getBrigada());

                } else {
                    if (!history.equals("")) {
                        xCell = xSpreadsheet.getCellByPosition(8, nRow++);
                        xCell.setFormula("=SUM(I" + (startRow + 2) + ":I" + (nRow - 1) + ")");
                    }

                    i--;
                    nRow++;
                    nCol = 1;

                    history = current;
                    startRow = nRow;

                    xCell = xSpreadsheet.getCellByPosition(nCol++, nRow);
                    xCell.setValue(++number);

                    xCell = xSpreadsheet.getCellByPosition(nCol++, nRow);
                    xCell.setValue(rowItem.getFas());

                    xCell = xSpreadsheet.getCellByPosition(nCol++, nRow);
                    xCell.setFormula(rowItem.getNaim().trim());

                    xCell = xSpreadsheet.getCellByPosition(nCol++, nRow);
                    xCell.setFormula(rowItem.getNar().trim());

                    xCell = xSpreadsheet.getCellByPosition(nCol++, nRow);
                    xCell.setFormula(rowItem.getRst().trim());

                    xCell = xSpreadsheet.getCellByPosition(nCol++, nRow);
                    xCell.setFormula(rowItem.getRzm().trim());

                    for (int j = 1; j < 12; j++) {
                        xCell = xSpreadsheet.getCellByPosition(j, nRow);
                        xPropSet = UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
                        xPropSet.setPropertyValue("TableBorder", aBorder);
                    }
                }
            }

            if (sheetItemList.size() > 0) {
                if (!history.equals("")) {
                    nRow++;
                    xCell = xSpreadsheet.getCellByPosition(8, nRow++);
                    xCell.setFormula("=SUM(I" + (startRow + 2) + ":I" + (nRow - 1) + ")");
                }

                xCell = xSpreadsheet.getCellByPosition(7, ++nRow);
                xCell.setFormula("ИТОГО:");

                xCell = xSpreadsheet.getCellByPosition(8, nRow);
                xCell.setFormula("=SUM(I8:I" + (nRow - 1) + ")/2");
            }

        } catch (java.lang.Exception e) {
            throw new Exception("Ошибка при заполнении отчёта.", e);
        }
    }

    private void fildRouteSheetModelsRoport(XComponent currentDocument) throws Exception {
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
            double kolvo = 0;

            XCell xCell = xSpreadsheet.getCellByPosition(nCol, nRow);

            //----------------Заполнение шапки документа
            xCell.setFormula(nameTamplates);

            //----------------Заполнение тела документа

            nRow = 3;

            List<RouteSheetItem> sheetItemList = reportData.getData();

            for (int i = 0; i < sheetItemList.size(); i++) {
                RouteSheetItem rowItem = sheetItemList.get(i);

                current = rowItem.getFas() + rowItem.getNaim() + rowItem.getNar();

                if (history.equals(current)) {
                    kolvo += rowItem.getKol();

                    xCell = xSpreadsheet.getCellByPosition(5, nRow);
                    xCell.setValue(kolvo);

                } else {
                    i--;
                    nRow++;
                    nCol = 1;

                    history = current;

                    xCell = xSpreadsheet.getCellByPosition(nCol++, nRow);
                    xCell.setValue(++number);

                    xCell = xSpreadsheet.getCellByPosition(nCol++, nRow);
                    xCell.setValue(rowItem.getFas());

                    xCell = xSpreadsheet.getCellByPosition(nCol++, nRow);
                    xCell.setFormula(rowItem.getNaim().trim());

                    xCell = xSpreadsheet.getCellByPosition(nCol++, nRow);
                    xCell.setFormula(rowItem.getNar().trim());

                    for (int j = 1; j < 6; j++) {
                        xCell = xSpreadsheet.getCellByPosition(j, nRow);
                        xPropSet = UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
                        xPropSet.setPropertyValue("TableBorder", aBorder);
                    }

                    kolvo = 0;
                }
            }

            if (sheetItemList.size() > 0) {
                xCell = xSpreadsheet.getCellByPosition(4, ++nRow);
                xCell.setFormula("ИТОГО:");

                xCell = xSpreadsheet.getCellByPosition(5, nRow);
                xCell.setFormula("=SUM(F5:F" + nRow + ")");
            }

        } catch (java.lang.Exception e) {
            throw new Exception("Ошибка при заполнении отчёта.", e);
        }
    }
}
