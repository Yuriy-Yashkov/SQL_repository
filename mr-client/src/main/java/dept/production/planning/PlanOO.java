package dept.production.planning;

import by.march8.api.utils.DateUtils;
import by.march8.ecs.framework.common.LogCrutch;
import by.march8.ecs.framework.common.model.CurrencySet;
import com.sun.star.awt.Size;
import com.sun.star.beans.XPropertySet;
import com.sun.star.container.XNameContainer;
import com.sun.star.drawing.XDrawPage;
import com.sun.star.drawing.XDrawPageSupplier;
import com.sun.star.drawing.XShape;
import com.sun.star.frame.XModel;
import com.sun.star.lang.Locale;
import com.sun.star.lang.XComponent;
import com.sun.star.lang.XMultiServiceFactory;
import com.sun.star.sheet.XSpreadsheet;
import com.sun.star.sheet.XSpreadsheetDocument;
import com.sun.star.sheet.XSpreadsheets;
import com.sun.star.table.XCell;
import com.sun.star.table.XCellRange;
import com.sun.star.uno.AnyConverter;
import com.sun.star.uno.UnoRuntime;
import com.sun.star.util.XNumberFormats;
import com.sun.star.util.XNumberFormatsSupplier;
import common.UtilFunctions;
import dept.tools.imgmanager.ImageTools;
import workOO.OO_new;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Vector;

/**
 *
 * @author lidashka
 */
@SuppressWarnings("all")
public class PlanOO extends OO_new {
    // private static final Logger log = new Log().getLoger(PlanOO.class);
    private static final LogCrutch log = new LogCrutch();
    static String nameTamplates;
    static DefaultTableModel tModel;
    static TableColumnModel tModelCol;
    static Vector vec;
    int maxSizeCol;
    private CurrencySet[] currencySet = null;

    public PlanOO(String nameReport, DefaultTableModel tm, TableColumnModel tc) {
        this.tModel = tm;
        this.tModelCol = tc;
        this.nameTamplates = nameReport;
    }

    public PlanOO(String nameReport, Vector vec) {
        this.vec = vec;
        this.nameTamplates = nameReport;
    }

    public PlanOO(String nameReport, Vector vec, CurrencySet[] currencySet) {
        this(nameReport, vec);
        this.currencySet = currencySet;
    }

    private static void setNumberFormat(XSpreadsheetDocument spreadsheetDocument, XCell cell, String numberFormatString) {
        try {
            XPropertySet cellProp = (XPropertySet) UnoRuntime.queryInterface(XPropertySet.class, cell);
            int numberFormatKey = getNumberFormat(spreadsheetDocument, numberFormatString);
            cellProp.setPropertyValue("NumberFormat", new Integer(numberFormatKey));
        } catch (Exception e) {
            System.out.println("Ошибка! SetNumberFormat " + e.getMessage());
        }
    }

    private static int getNumberFormat(XSpreadsheetDocument spreadsheetDocument, String numberFormatString) {
        if (spreadsheetDocument == null || numberFormatString == null || numberFormatString.trim().equals(""))
            return 0;
        XNumberFormatsSupplier xNumberFormatsSupplier = (XNumberFormatsSupplier) UnoRuntime.queryInterface(XNumberFormatsSupplier.class, spreadsheetDocument);
        XNumberFormats xNumberFormats = xNumberFormatsSupplier.getNumberFormats();
        Locale defaultLocale = new Locale();
        int numberFormatKey = xNumberFormats.queryKey(numberFormatString, defaultLocale, false);
        if (numberFormatKey == -1) {
            try {
                numberFormatKey = xNumberFormats.addNew(numberFormatString, defaultLocale);
            } catch (Exception e) {
                System.out.println("Ошибка! GetNumberFormat " + e.getMessage());
                numberFormatKey = -1;
            }
        }
        return numberFormatKey;
    }

    public void createReport(String nameTemplates) throws Exception {
        try {
            connect();
            XComponent currentDocument = openDocumentOld("Templates/" + nameTemplates);

            if (nameTemplates.equals("DefaultTableBookFormatCheck.ots")) {
                fildTableDefaultCheck(currentDocument);
            } else if (nameTemplates.equals("DefaultTableAlbumFormatCheck.ots")) {
                fildTableDefaultCheck(currentDocument);
            } else if (nameTemplates.equals("ProductionPlanCapacityDept.ots")) {
                fildCapacityDept(currentDocument);
            } else if (nameTemplates.equals("ProjectPlanStok.ots")) {
                fildProjectPlanDoc(currentDocument, 1);
            } else if (nameTemplates.equals("ProjectPlanVnorm.ots")) {
                fildProjectPlanDoc(currentDocument, 2);
            } else if (nameTemplates.equals("ProjectPlanMaterials.ots")) {
                fildProjectPlanMaterials(currentDocument);
            } else if (nameTemplates.equals("ProjectPlanMaterialsModel.ots")) {
                fildProjectPlanMaterialsModel(currentDocument);
            } else if (nameTemplates.equals("ProductionPlanDocAllNorm.ots")) {
                fildProductionPlanDocAllNorm(currentDocument);
            } else if (nameTemplates.equals("ProjectPlanStokCena.ots")) {
                fildProjectPlanStokCenaDoc(currentDocument, false);
            } else if (nameTemplates.equals("ProjectPlanStokCenaImage.ots")) {
                fildProjectPlanStokCenaDoc(currentDocument, true);
            } else if (nameTemplates.equals("ProjectPlanCena.ots")) {
                PlanOO.this.fildProjectPlanCenaDoc(currentDocument);
            } else if (nameTemplates.equals("ProjectPlanCenaRBImage.ots")) {
                fildProjectPlanCenaDoc(currentDocument, "RB");
            } else if (nameTemplates.equals("ProjectPlanCenaRFImage.ots")) {
                fildProjectPlanCenaDoc(currentDocument, "RF");
            } else if (nameTemplates.equals("ProjectPlanCenaAll.ots")) {
                fildProjectPlanCenaDoc(currentDocument, "ALL");
            }

            JOptionPane.showMessageDialog(null, "Отчёт сформирован", "Отчёт сформирован", JOptionPane.INFORMATION_MESSAGE);

        } catch (java.lang.Exception e) {
            System.out.println(e.getMessage());
            throw new Exception("Ошибка при формировании отчёта ", e);
        }
    }

    private void fildTableDefaultCheck(XComponent currentDocument) throws Exception {
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
            xCell.setFormula(nameTamplates);

            boolean flag = false;

            int nCol = 0;
            for (int i = 1; i < tModel.getColumnCount(); i++) {
                if (tModelCol.getColumn(i).getWidth() > 0) {
                    xCell = xSpreadsheet.getCellByPosition(nCol++, 1);
                    xCell.setFormula(tModel.getColumnName(i));
                    xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
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
                                xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
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

    private void fildProjectPlanMaterialsModel(XComponent currentDocument) throws Exception {
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

            XCell xCell = xSpreadsheet.getCellByPosition(6, 1);

            //----------------Заполнение шапки документа

            xCell.setFormula(xCell.getFormula() + " '" + nameTamplates + "г.");

            //----------------Заполнение тела документа

            String format = UtilFunctions.getFormatRoundingNorm(UtilPlan.ROUNDING_NORM).replace(".", ",");

            int nCol = 1;
            int nRow = 2;

            int vid = -1;
            int histRow = 4;
            int kol = 0;

            Vector newFas = new Vector();
            Vector fas = new Vector();

            for (Object obj : vec) {
                Vector item = (Vector) obj;

                nRow++;
                xCell = xSpreadsheet.getCellByPosition(0, nRow);
                xCell.setValue(++kol);

                for (int i = 0; i <= item.size(); i++) {
                    if (i == item.size()) {
                        xCell = xSpreadsheet.getCellByPosition(nCol++, nRow);
                        xCell.setFormula("=(F" + (nRow + 1) + "*H" + (nRow + 1) + ")/1000");
                        xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
                        xPropSet.setPropertyValue("TableBorder", aBorder);
                    } else {
                        if (!fas.contains(Integer.valueOf(item.get(2).toString())))
                            fas.add(Integer.valueOf(item.get(2).toString()));

                        if (String.valueOf(item.get(0)).trim().toUpperCase().equals("V"))
                            if (!newFas.contains(Integer.valueOf(item.get(2).toString())))
                                newFas.add(Integer.valueOf(item.get(2).toString()));

                        xCell = xSpreadsheet.getCellByPosition(nCol++, nRow);
                        xCell.setFormula((item).get(i) != null ? (item).get(i).toString() : "");
                        xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
                        xPropSet.setPropertyValue("TableBorder", aBorder);
                    }
                }
                nCol = 1;
            }

            nRow++;
            xCell = xSpreadsheet.getCellByPosition(8, nRow);
            xCell.setFormula("=SUM(I4:I" + (nRow) + ")");

            nRow += 2;
            xCell = xSpreadsheet.getCellByPosition(2, nRow);
            xCell.setFormula("Кол-во моделей");
            xCell = xSpreadsheet.getCellByPosition(6, nRow);
            xCell.setFormula("шт.");
            xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
            xPropSet.setPropertyValue("CharHeight", (float) (9));
            xCell = xSpreadsheet.getCellByPosition(5, nRow);
            xCell.setValue(fas.size());

            nRow++;
            xCell = xSpreadsheet.getCellByPosition(2, nRow);
            xCell.setFormula("Кол-во новинок");
            xCell = xSpreadsheet.getCellByPosition(6, nRow);
            xCell.setFormula("шт.");
            xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
            xPropSet.setPropertyValue("CharHeight", (float) (9));
            xCell = xSpreadsheet.getCellByPosition(5, nRow);
            xCell.setValue(newFas.size());

        } catch (java.lang.Exception e) {
            log.error("Ошибка при заполнении отчёта.", e);
            throw new Exception("Ошибка при заполнении отчёта.", e);
        }
    }

    private void fildProjectPlanMaterials(XComponent currentDocument) throws Exception {
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

            int nCol = 1;
            int nRow = 3;

            XCell xCell = xSpreadsheet.getCellByPosition(3, 1);

            //----------------Заполнение шапки документа
            xCell.setFormula(xCell.getFormula() + " '" + nameTamplates + "г.");

            for (Object obj : vec) {
                Vector item = (Vector) obj;

                for (int i = 0; i < item.size(); i++) {

                    xCell = xSpreadsheet.getCellByPosition(nCol++, nRow);
                    xCell.setFormula((item).get(i) != null ? (item).get(i).toString() : "");
                    xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
                    xPropSet.setPropertyValue("TableBorder", aBorder);
                }

                xCell = xSpreadsheet.getCellByPosition(3, nRow);
                xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
                xPropSet.setPropertyValue("TableBorder", aBorder);

                nCol = 1;
                nRow++;
            }

            nRow++;

            xCell = xSpreadsheet.getCellByPosition(1, nRow);
            xCell.setFormula("ИТОГО");

            xCell = xSpreadsheet.getCellByPosition(2, nRow);
            xCell.setFormula("=SUM(C4:C" + (nRow - 1) + ")");

            xCell = xSpreadsheet.getCellByPosition(3, nRow);
            xCell.setFormula(" г");
        } catch (java.lang.Exception e) {
            log.error("Ошибка при заполнении отчёта.", e);
            throw new Exception("Ошибка при заполнении отчёта.", e);
        }
    }

    private void fildCapacityDept(XComponent currentDocument) throws Exception {
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

            XCell xCell = xSpreadsheet.getCellByPosition(0, 1);

            //----------------Заполнение шапки документа

            xCell.setFormula(xCell.getFormula() + " " + nameTamplates);

            //----------------Заполнение тела документа

            int nCol = 2;

            //Кол-во швей всего:
            xCell = xSpreadsheet.getCellByPosition(nCol, 3);
            xCell.setFormula(vec.elementAt(0).toString());
            //Отпуск:
            xCell = xSpreadsheet.getCellByPosition(nCol, 4);
            xCell.setFormula(vec.elementAt(1).toString());
            //Больничные лист:
            xCell = xSpreadsheet.getCellByPosition(nCol, 7);
            xCell.setFormula(vec.elementAt(6).toString());
            //Сессии:
            xCell = xSpreadsheet.getCellByPosition(nCol, 8);
            xCell.setFormula(vec.elementAt(7).toString());
            //Кол-во рабочих дней:
            xCell = xSpreadsheet.getCellByPosition(nCol, 11);
            xCell.setFormula(vec.elementAt(2).toString());
            //Рабочие часы:
            xCell = xSpreadsheet.getCellByPosition(nCol, 12);
            xCell.setFormula(vec.elementAt(3).toString());
            //Кол-во коротких рабочих дней:
            xCell = xSpreadsheet.getCellByPosition(nCol, 13);
            xCell.setFormula(vec.elementAt(4).toString());
            //Короткие рабочие часы:
            xCell = xSpreadsheet.getCellByPosition(nCol, 14);
            xCell.setFormula(vec.elementAt(5).toString());
            //Выпуск кол-во ед.:
            xCell = xSpreadsheet.getCellByPosition(nCol, 17);
            xCell.setFormula(vec.elementAt(8).toString());
            //Трудоемкость на выпуск:
            xCell = xSpreadsheet.getCellByPosition(nCol, 18);
            xCell.setFormula(vec.elementAt(9).toString());
          /*  //Загрузка цеха на месяц:
            xCell = xSpreadsheet.getCellByPosition(nCol, 20);
            xCell.setFormula(vec.elementAt(10).toString());
            //Пропускная способность цеха:
            xCell = xSpreadsheet.getCellByPosition(nCol, 21);
            xCell.setFormula(vec.elementAt(11).toString());*/

        } catch (java.lang.Exception e) {
            log.error("Ошибка при заполнении отчёта.", e);
            throw new Exception("Ошибка при заполнении отчёта.", e);
        }
    }

    private void fildProjectPlanDoc(XComponent currentDocument, int vidDoc) throws Exception {
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

            XCell xCell = xSpreadsheet.getCellByPosition(8, 1);

            //----------------Заполнение шапки документа

            xCell.setFormula(xCell.getFormula() + " '" + nameTamplates + "г.");

            //----------------Заполнение тела документа

            String format = UtilFunctions.getFormatRoundingNorm(UtilPlan.ROUNDING_NORM).replace(".", ",");

            int nCol = 1;
            int nRow = 2;

            int vid = -1;
            int histRow = 4;
            int kol = 0;

            Vector newFas = new Vector();
            Vector fas = new Vector();

            for (Object obj : vec) {
                Vector item = (Vector) obj;

                if (vid != Integer.valueOf(item.get(0).toString())) {
                    if (vid != -1) {
                        nRow++;
                        xCell = xSpreadsheet.getCellByPosition(2, nRow);
                        xCell.setFormula("ИТОГО");
                        switch (vid) {
                            case 1:
                                xCell.setFormula(xCell.getFormula() + " ПО МУЖ. АССОРТ.");
                                break;
                            case 2:
                                xCell.setFormula(xCell.getFormula() + " ПО ЖЕН. АССОРТ.");
                                break;
                            case 3:
                                xCell.setFormula(xCell.getFormula() + " ПО ДЕТ. АССОРТ.");
                                break;
                            default:
                                break;
                        }

                        xCell = xSpreadsheet.getCellByPosition(5, nRow);
                        xCell.setFormula("=SUMPRODUCT(F" + (histRow + 2) + ":F" + (nRow) + ";G" + (histRow + 2) + ":G" + (nRow) + ")");

                        if (vidDoc == 2) {
                            xCell = xSpreadsheet.getCellByPosition(8, nRow);
                            xCell.setFormula("=SUM(I" + (histRow + 2) + ":I" + (nRow) + ")");
                        }

                        nRow++;
                    }
                    histRow = nRow;
                    vid = Integer.valueOf(item.get(0).toString());
                }

                nRow++;
                xCell = xSpreadsheet.getCellByPosition(0, nRow);
                xCell.setValue(++kol);

                for (int i = 1; i < item.size(); i++) {
                    if (!fas.contains(Integer.valueOf(item.get(3).toString())))
                        fas.add(Integer.valueOf(item.get(3).toString()));

                    if (String.valueOf(item.get(1)).trim().toUpperCase().equals("V"))
                        if (!newFas.contains(Integer.valueOf(item.get(3).toString())))
                            newFas.add(Integer.valueOf(item.get(3).toString()));

                    xCell = xSpreadsheet.getCellByPosition(nCol++, nRow);
                    if (vidDoc == 2 && i == 7)
                        setNumberFormat(xSpreadsheetDocument, xCell, format);
                    xCell.setFormula((item).get(i) != null ? (item).get(i).toString() : "");
                    xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
                    xPropSet.setPropertyValue("TableBorder", aBorder);
                }
                nCol = 1;
            }
            nRow++;
            xCell = xSpreadsheet.getCellByPosition(2, nRow);
            xCell.setFormula("ИТОГО");
            switch (vid) {
                case 1:
                    xCell.setFormula(xCell.getFormula() + " ПО МУЖ. АССОРТ.");
                    break;
                case 2:
                    xCell.setFormula(xCell.getFormula() + " ПО ЖЕН. АССОРТ.");
                    break;
                case 3:
                    xCell.setFormula(xCell.getFormula() + " ПО ДЕТ. АССОРТ.");
                    break;
                default:
                    break;
            }
            xCell = xSpreadsheet.getCellByPosition(5, nRow);
            xCell.setFormula("=SUMPRODUCT(F" + (histRow + 2) + ":F" + (nRow) + ";G" + (histRow + 2) + ":G" + (nRow) + ")");

            if (vidDoc == 2) {
                xCell = xSpreadsheet.getCellByPosition(8, nRow);
                xCell.setFormula("=SUM(I" + (histRow + 2) + ":I" + (nRow) + ")");
            }

            nRow += 2;
            histRow = nRow - 1;
            xCell = xSpreadsheet.getCellByPosition(2, nRow);
            xCell.setFormula("ВСЕГО");
            xCell = xSpreadsheet.getCellByPosition(6, nRow);
            xCell.setFormula("шт.");
            xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
            xPropSet.setPropertyValue("CharHeight", (float) (9));
            xCell = xSpreadsheet.getCellByPosition(5, nRow);
            xCell.setFormula("=SUMPRODUCT(F4:F" + (histRow) + ";G4:G" + (histRow) + ")");

            if (vidDoc == 2) {
                xCell = xSpreadsheet.getCellByPosition(8, nRow++);
                xCell.setFormula("=SUM(I4:I" + (histRow) + ")/2");
            }

            nRow++;
            xCell = xSpreadsheet.getCellByPosition(2, nRow);
            xCell.setFormula("ВСЕГО (б/комплект)");
            xCell = xSpreadsheet.getCellByPosition(6, nRow);
            xCell.setFormula("шт.");
            xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
            xPropSet.setPropertyValue("CharHeight", (float) (9));
            xCell = xSpreadsheet.getCellByPosition(5, nRow);
            xCell.setFormula("=SUMIF(D4:D" + (histRow) + ";\"<>''\";F4:F" + (histRow) + ")");

            nRow++;
            xCell = xSpreadsheet.getCellByPosition(2, nRow);
            xCell.setFormula("ВСЕГО (новинка)");
            xCell = xSpreadsheet.getCellByPosition(6, nRow);
            xCell.setFormula("шт.");
            xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
            xPropSet.setPropertyValue("CharHeight", (float) (9));
            xCell = xSpreadsheet.getCellByPosition(5, nRow);
            xCell.setFormula("=SUMPRODUCT(+B4:B" + (histRow) + "=\"V\";F4:F" + (histRow) + ";G4:G" + (histRow) + ")");

            nRow++;
            xCell = xSpreadsheet.getCellByPosition(2, nRow);
            xCell.setFormula("ВСЕГО (нов. б/компл.)");
            xCell = xSpreadsheet.getCellByPosition(6, nRow);
            xCell.setFormula("шт.");
            xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
            xPropSet.setPropertyValue("CharHeight", (float) (9));
            xCell = xSpreadsheet.getCellByPosition(5, nRow);
            xCell.setFormula("=SUMIF(B4:B" + (histRow) + ";\"V\";F4:F" + (histRow) + ")");

            if (vidDoc == 1) {
                nRow++;
                xCell = xSpreadsheet.getCellByPosition(2, nRow);
                xCell.setFormula("ВСЕГО (шт. печать)");
                xCell = xSpreadsheet.getCellByPosition(6, nRow);
                xCell.setFormula("шт.");
                xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
                xPropSet.setPropertyValue("CharHeight", (float) (9));
                xCell = xSpreadsheet.getCellByPosition(5, nRow);
                xCell.setFormula("=SUMIF(K4:K" + (histRow) + ";\"1\";F4:F" + (histRow) + ")");

                nRow += 2;
                xCell = xSpreadsheet.getCellByPosition(2, nRow);
                xCell.setFormula("Кол-во моделей");
                xCell = xSpreadsheet.getCellByPosition(6, nRow);
                xCell.setFormula("шт.");
                xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
                xPropSet.setPropertyValue("CharHeight", (float) (9));
                xCell = xSpreadsheet.getCellByPosition(5, nRow);
                xCell.setValue(fas.size());

                nRow++;
                xCell = xSpreadsheet.getCellByPosition(2, nRow);
                xCell.setFormula("Кол-во новинок");
                xCell = xSpreadsheet.getCellByPosition(6, nRow);
                xCell.setFormula("шт.");
                xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
                xPropSet.setPropertyValue("CharHeight", (float) (9));
                xCell = xSpreadsheet.getCellByPosition(5, nRow);
                xCell.setValue(newFas.size());
            }

            nRow += 2;
            xCell = xSpreadsheet.getCellByPosition(2, nRow);
            xCell.setFormula("Помощник руководителя организации по производственным вопросам и закупкам");
            xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
            xPropSet.setPropertyValue("HoriJustify", com.sun.star.table.CellHoriJustify.LEFT);
            xPropSet.setPropertyValue("CharHeight", (float) (10));

            xCell = xSpreadsheet.getCellByPosition((vidDoc == 1) ? 9 : 8, nRow);
            xCell.setFormula("Карпей");
            xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
            xPropSet.setPropertyValue("HoriJustify", com.sun.star.table.CellHoriJustify.RIGHT);
            xPropSet.setPropertyValue("CharHeight", (float) (10));

            xCell = xSpreadsheet.getCellByPosition((vidDoc == 1) ? 10 : 9, nRow);
            xCell.setFormula("М.И.");
            xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
            xPropSet.setPropertyValue("HoriJustify", com.sun.star.table.CellHoriJustify.CENTER);
            xPropSet.setPropertyValue("CharHeight", (float) (10));

        } catch (java.lang.Exception e) {
            log.error("Ошибка при заполнении отчёта.", e);
            throw new Exception("Ошибка при заполнении отчёта.", e);
        }
    }

    private void fildProjectPlanStokCenaDoc(XComponent currentDocument, boolean flagImage) throws Exception {
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

            XCell xCell = xSpreadsheet.getCellByPosition(1, 1);

            //----------------Заполнение шапки документа

            xCell.setFormula(xCell.getFormula() + " Проект плана производства. Ассортимент на " + nameTamplates + ".");

            //----------------Заполнение тела документа

            String format = UtilFunctions.getFormatRoundingNorm(UtilPlan.ROUNDING_NORM).replace(".", ",");

            int nCol = 2;
            int nRow = 2;

            int vid = -1;
            int histRow = 4;
            int kol = 0;

            maxSizeCol = 0;

            Vector newFas = new Vector();
            Vector fas = new Vector();

            for (Object obj : vec) {
                Vector item = (Vector) obj;

                if (vid != Integer.valueOf(item.get(1).toString())) {
                    histRow = nRow;
                    vid = Integer.valueOf(item.get(1).toString());

                    nRow = nRow + 2;
                    xCell = xSpreadsheet.getCellByPosition(2, nRow);
                    switch (vid) {
                        case 1:
                            xCell.setFormula(xCell.getFormula() + " МУЖ. АССОРТИМЕНТ");
                            break;
                        case 2:
                            xCell.setFormula(xCell.getFormula() + " ЖЕН. АССОРТИМЕНТ");
                            break;
                        case 3:
                            xCell.setFormula(xCell.getFormula() + " ДЕТ. АССОРТИМЕНТ");
                            break;
                        default:
                            break;
                    }
                }

                nRow++;
                xCell = xSpreadsheet.getCellByPosition(0, nRow);
                xCell.setValue(++kol);

                if (!fas.contains(Integer.valueOf(item.get(3).toString())))
                    fas.add(Integer.valueOf(item.get(3).toString()));

                xCell = xSpreadsheet.getCellByPosition(1, nRow);
                xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
                xPropSet.setPropertyValue("TableBorder", aBorder);
                if (String.valueOf(item.get(13)).trim().toUpperCase().equals("НОВИНКА")) {
                    xCell.setFormula("V");

                    if (!newFas.contains(Integer.valueOf(item.get(3).toString())))
                        newFas.add(Integer.valueOf(item.get(3).toString()));
                }

                xCell = xSpreadsheet.getCellByPosition(2, nRow);
                xCell.setFormula((item).get(2) != null ? (item).get(2).toString() : "");
                xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
                xPropSet.setPropertyValue("TableBorder", aBorder);

                xCell = xSpreadsheet.getCellByPosition(3, nRow);
                xCell.setFormula((item).get(3) != null ? (item).get(3).toString() : "");
                xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
                xPropSet.setPropertyValue("TableBorder", aBorder);

                xCell = xSpreadsheet.getCellByPosition(4, nRow);
                xCell.setFormula((item).get(7) != null ? (item).get(7).toString() : "");
                xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
                xPropSet.setPropertyValue("TableBorder", aBorder);

                xCell = xSpreadsheet.getCellByPosition(5, nRow);
                xCell.setFormula((item).get(9) != null ? (item).get(9).toString() : "");
                xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
                xPropSet.setPropertyValue("TableBorder", aBorder);

                xCell = xSpreadsheet.getCellByPosition(6, nRow);
                xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
                xPropSet.setPropertyValue("TableBorder", aBorder);
                xCell.setFormula(xCell.getFormula() + "\n");
                if ((item).get(14) != null) {
                    Vector tmp = (Vector) (item).get(14);
                    for (Object tmp1 : tmp) {
                        xCell.setFormula(xCell.getFormula() + new BigDecimal(tmp1.toString()).setScale(0, RoundingMode.HALF_UP));
                        xCell.setFormula(xCell.getFormula() + "\n");
                    }
                }

                xCell = xSpreadsheet.getCellByPosition(7, nRow);
                xCell.setFormula((item).get(5) != null ? (item).get(5).toString() : "");
                xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
                xPropSet.setPropertyValue("TableBorder", aBorder);

                xCell = xSpreadsheet.getCellByPosition(8, nRow);
                xCell.setFormula((item).get(4) != null ? (item).get(4).toString() : "");
                xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
                xPropSet.setPropertyValue("TableBorder", aBorder);

                xCell = xSpreadsheet.getCellByPosition(9, nRow);
                if ((item).get(11) != null) {
                    if (((item).get(11).toString()).contains("1"))
                        xCell.setFormula(xCell.getFormula() + " шт. печать");
                    if (((item).get(11).toString()).contains("2"))
                        xCell.setFormula(xCell.getFormula() + " вышивка");
                }
                xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
                xPropSet.setPropertyValue("TableBorder", aBorder);

                xCell = xSpreadsheet.getCellByPosition(10, nRow);
                xCell.setFormula((item).get(10) != null ? (item).get(10).toString() : "");
                xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
                xPropSet.setPropertyValue("TableBorder", aBorder);

                xCell = xSpreadsheet.getCellByPosition(11, nRow);
                xCell.setFormula((item).get(12) != null ? (item).get(12).toString() : "");
                xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
                xPropSet.setPropertyValue("TableBorder", aBorder);

                if (flagImage) {
                    xCell = xSpreadsheet.getCellByPosition(12, nRow);
                    xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
                    xPropSet.setPropertyValue("TableBorder", aBorder);

                    String path = getPath((item).get(3).toString());

                    if (!path.equals("")) {
                        insertImage(xSpreadsheetDocument,
                                xSpreadsheet,
                                xCell,
                                path,
                                (item).get(3).toString(),
                                nRow);
                    }
                }
                nCol = 2;
            }

            XCellRange xCellRange = xSpreadsheet.getCellRangeByPosition(12, 0, 13, 0);
            setCustomColumn(xCellRange, maxSizeCol);

            nRow += 2;
            xCell = xSpreadsheet.getCellByPosition(2, nRow);
            xCell.setFormula("Кол-во моделей");
            xCell = xSpreadsheet.getCellByPosition(6, nRow);
            xCell.setFormula("шт.");
            xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
            xPropSet.setPropertyValue("CharHeight", (float) (9));
            xCell = xSpreadsheet.getCellByPosition(5, nRow);
            xCell.setValue(fas.size());

            nRow++;
            xCell = xSpreadsheet.getCellByPosition(2, nRow);
            xCell.setFormula("Кол-во новинок");
            xCell = xSpreadsheet.getCellByPosition(6, nRow);
            xCell.setFormula("шт.");
            xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
            xPropSet.setPropertyValue("CharHeight", (float) (9));
            xCell = xSpreadsheet.getCellByPosition(5, nRow);
            xCell.setValue(newFas.size());

        } catch (java.lang.Exception e) {
            log.error("Ошибка при заполнении отчёта.", e);
            throw new Exception("Ошибка при заполнении отчёта.", e);
        }
    }

    private void fildProjectPlanCenaDoc(XComponent currentDocument) throws Exception {
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

            XCell xCell = xSpreadsheet.getCellByPosition(1, 1);

            //----------------Заполнение шапки документа

            xCell.setFormula(xCell.getFormula() + " Проект плана производства. " + nameTamplates + ".");

            //----------------Заполнение тела документа

            int nCol = 2;
            int nRow = 2;

            int vid = -1;
            int histRow = 4;
            int kol = 0;

            Vector newFas = new Vector();
            Vector fas = new Vector();

            for (Object obj : vec) {
                Vector item = (Vector) obj;

                if (vid != Integer.valueOf(item.get(0).toString())) {
                    histRow = nRow;
                    vid = Integer.valueOf(item.get(0).toString());

                    nRow = nRow + 2;
                    xCell = xSpreadsheet.getCellByPosition(2, nRow);
                    switch (vid) {
                        case 1:
                            xCell.setFormula(xCell.getFormula() + " МУЖ. АССОРТИМЕНТ");
                            break;
                        case 2:
                            xCell.setFormula(xCell.getFormula() + " ЖЕН. АССОРТИМЕНТ");
                            break;
                        case 3:
                            xCell.setFormula(xCell.getFormula() + " ДЕТ. АССОРТИМЕНТ");
                            break;
                        default:
                            break;
                    }
                }

                nRow++;
                xCell = xSpreadsheet.getCellByPosition(0, nRow);
                xCell.setValue(++kol);

                if (!fas.contains(Integer.valueOf(item.get(3).toString())))
                    fas.add(Integer.valueOf(item.get(3).toString()));

                xCell = xSpreadsheet.getCellByPosition(1, nRow);
                xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
                xPropSet.setPropertyValue("TableBorder", aBorder);
                if (String.valueOf(item.get(1)).trim().toUpperCase().equals("V")) {
                    xCell.setFormula("V");

                    if (!newFas.contains(Integer.valueOf(item.get(3).toString())))
                        newFas.add(Integer.valueOf(item.get(3).toString()));
                }

                xCell = xSpreadsheet.getCellByPosition(2, nRow);
                xCell.setFormula((item).get(2) != null ? (item).get(2).toString() : "");
                xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
                xPropSet.setPropertyValue("TableBorder", aBorder);

                xCell = xSpreadsheet.getCellByPosition(3, nRow);
                xCell.setFormula((item).get(3) != null ? (item).get(3).toString() : "");
                xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
                xPropSet.setPropertyValue("TableBorder", aBorder);

                xCell = xSpreadsheet.getCellByPosition(4, nRow);
                xCell.setFormula((item).get(4) != null ? (item).get(4).toString() : "");
                xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
                xPropSet.setPropertyValue("TableBorder", aBorder);

                xCell = xSpreadsheet.getCellByPosition(5, nRow);
                xCell.setFormula((item).get(10) != null ? (item).get(10).toString() : "");
                xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
                xPropSet.setPropertyValue("TableBorder", aBorder);

                xCell = xSpreadsheet.getCellByPosition(6, nRow);
                xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
                xPropSet.setPropertyValue("TableBorder", aBorder);
                xCell.setFormula(xCell.getFormula() + "\n");
                if ((item).get(16) != null) {
                    Vector tmp = (Vector) (item).get(16);
                    for (Object tmp1 : tmp) {
                        xCell.setFormula(xCell.getFormula() + tmp1.toString());
                        xCell.setFormula(xCell.getFormula() + "\n");
                    }
                }

                xCell = xSpreadsheet.getCellByPosition(7, nRow);
                xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
                xPropSet.setPropertyValue("TableBorder", aBorder);
                xCell.setFormula(xCell.getFormula() + "\n");
                if ((item).get(17) != null) {
                    Vector tmp = (Vector) (item).get(17);
                    for (Object tmp1 : tmp) {
                        xCell.setFormula(xCell.getFormula() + tmp1.toString());
                        xCell.setFormula(xCell.getFormula() + "\n");
                    }
                }

                xCell = xSpreadsheet.getCellByPosition(8, nRow);
                xCell.setFormula((item).get(11) != null ? (item).get(11).toString() : "");
                xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
                xPropSet.setPropertyValue("TableBorder", aBorder);

                xCell = xSpreadsheet.getCellByPosition(9, nRow);
                if ((item).get(15) != null) {
                    if (((item).get(15).toString()).contains("1"))
                        xCell.setFormula(xCell.getFormula() + " шт. печать");
                    if (((item).get(15).toString()).contains("2"))
                        xCell.setFormula(xCell.getFormula() + " вышивка");
                }
                xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
                xPropSet.setPropertyValue("TableBorder", aBorder);

                nCol = 2;
            }

            nRow += 2;
            xCell = xSpreadsheet.getCellByPosition(2, nRow);
            xCell.setFormula("Кол-во моделей");
            xCell = xSpreadsheet.getCellByPosition(6, nRow);
            xCell.setFormula("шт.");
            xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
            xPropSet.setPropertyValue("CharHeight", (float) (9));
            xCell = xSpreadsheet.getCellByPosition(5, nRow);
            xCell.setValue(fas.size());

            nRow++;
            xCell = xSpreadsheet.getCellByPosition(2, nRow);
            xCell.setFormula("Кол-во новинок");
            xCell = xSpreadsheet.getCellByPosition(6, nRow);
            xCell.setFormula("шт.");
            xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
            xPropSet.setPropertyValue("CharHeight", (float) (9));
            xCell = xSpreadsheet.getCellByPosition(5, nRow);
            xCell.setValue(newFas.size());

        } catch (java.lang.Exception e) {
            log.error("Ошибка при заполнении отчёта.", e);
            throw new Exception("Ошибка при заполнении отчёта.", e);
        }
    }

    private String getCurrencyString(CurrencySet currency) {
        if (currency.getRate() < 1) {
            return "-";
        } else {
            return "Курс на " + DateUtils.getNormalDateFormat(currency.getDate()) + "\n" + currency.getRate() + "руб.";
        }
    }

    private String parseCurrencyPrice() {
        return "";
    }

    private void fildProjectPlanCenaDoc(XComponent currentDocument, String type) throws Exception {
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

            XCell xCell = xSpreadsheet.getCellByPosition(1, 1);

            //----------------Заполнение шапки документа

            xCell.setFormula(xCell.getFormula() + " Проект плана производства. " + nameTamplates + ".");

            // Меняем курсы в шапке для АССОРТИМЕНТ ВСЕ ЦЕНЫ
            if (type.equals("ALL")) {
                // Курсы
                // RUB
                xCell = xSpreadsheet.getCellByPosition(7, 2);
                xCell.setFormula(xCell.getFormula() + "\n" + getCurrencyString(currencySet[0]));
                // USD
                xCell = xSpreadsheet.getCellByPosition(8, 2);
                xCell.setFormula(xCell.getFormula() + "\n" + getCurrencyString(currencySet[1]));
                //EUR
                xCell = xSpreadsheet.getCellByPosition(9, 2);
                xCell.setFormula(xCell.getFormula() + "\n" + getCurrencyString(currencySet[2]));
            }

            //----------------Заполнение тела документа

            int nCol = 2;
            int nRow = 2;

            int vid = -1;
            int histRow = 4;
            int kol = 0;

            Vector newFas = new Vector();
            Vector fas = new Vector();

            for (Object obj : vec) {
                Vector item = (Vector) obj;

                if (vid != Integer.valueOf(item.get(0).toString())) {
                    histRow = nRow;
                    vid = Integer.valueOf(item.get(0).toString());

                    nRow = nRow + 2;
                    xCell = xSpreadsheet.getCellByPosition(2, nRow);
                    switch (vid) {
                        case 1:
                            xCell.setFormula(xCell.getFormula() + " МУЖ. АССОРТИМЕНТ");
                            break;
                        case 2:
                            xCell.setFormula(xCell.getFormula() + " ЖЕН. АССОРТИМЕНТ");
                            break;
                        case 3:
                            xCell.setFormula(xCell.getFormula() + " ДЕТ. АССОРТИМЕНТ");
                            break;
                        default:
                            break;
                    }
                }

                nRow++;
                xCell = xSpreadsheet.getCellByPosition(0, nRow);
                xCell.setValue(++kol);

                if (!fas.contains(Integer.valueOf(item.get(3).toString())))
                    fas.add(Integer.valueOf(item.get(3).toString()));

                xCell = xSpreadsheet.getCellByPosition(1, nRow);
                xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
                xPropSet.setPropertyValue("TableBorder", aBorder);
                if (String.valueOf(item.get(1)).trim().toUpperCase().equals("V")) {
                    xCell.setFormula("V");

                    if (!newFas.contains(Integer.valueOf(item.get(3).toString())))
                        newFas.add(Integer.valueOf(item.get(3).toString()));
                }

                //наименование
                xCell = xSpreadsheet.getCellByPosition(2, nRow);
                xCell.setFormula((item).get(2) != null ? (item).get(2).toString() : "");
                xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
                xPropSet.setPropertyValue("TableBorder", aBorder);

                //модель
                xCell = xSpreadsheet.getCellByPosition(3, nRow);
                xCell.setFormula((item).get(3) != null ? (item).get(3).toString() : "");
                xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
                xPropSet.setPropertyValue("TableBorder", aBorder);

                //размер
                xCell = xSpreadsheet.getCellByPosition(4, nRow);
                xCell.setFormula((item).get(10) != null ? (item).get(10).toString() : "");
                xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
                xPropSet.setPropertyValue("TableBorder", aBorder);

                //состав
                xCell = xSpreadsheet.getCellByPosition(5, nRow);
                // xCell.setFormula((item).get(10)!=null?(item).get(10).toString():"");
                xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
                xPropSet.setPropertyValue("TableBorder", aBorder);

                if (type.equals("RB")) {
                    //цена BYR
                    xCell = xSpreadsheet.getCellByPosition(6, nRow);
                    xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
                    xPropSet.setPropertyValue("TableBorder", aBorder);
                    xCell.setFormula(xCell.getFormula() + "\n");
                    if ((item).get(16) != null) {
                        Vector tmp = (Vector) (item).get(16);
                        for (Object tmp1 : tmp) {
                            xCell.setFormula(xCell.getFormula() + tmp1.toString());
                            xCell.setFormula(xCell.getFormula() + "\n");
                        }
                    }

                    //выпуск(шт.)
                    xCell = xSpreadsheet.getCellByPosition(7, nRow);
                    xCell.setFormula((item).get(13) != null ? (item).get(13).toString() : "");
                    xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
                    xPropSet.setPropertyValue("TableBorder", aBorder);

                    //расцветка
                    xCell = xSpreadsheet.getCellByPosition(8, nRow);
                    xCell.setFormula((item).get(12) != null ? (item).get(12).toString() : "");
                    xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
                    xPropSet.setPropertyValue("TableBorder", aBorder);

                    //изображение
                    xCell = xSpreadsheet.getCellByPosition(9, nRow);
                    xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
                    xPropSet.setPropertyValue("TableBorder", aBorder);

                    String path = getPath((item).get(3).toString());

                    if (!path.equals("")) {
                        insertImage(xSpreadsheetDocument,
                                xSpreadsheet,
                                xCell,
                                path,
                                (item).get(3).toString(),
                                nRow);
                    }

                } else if (type.equals("RF")) {
                    //цена RUB
                    xCell = xSpreadsheet.getCellByPosition(6, nRow);
                    xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
                    xPropSet.setPropertyValue("TableBorder", aBorder);
                    xCell.setFormula(xCell.getFormula() + "\n");
                    if ((item).get(17) != null) {
                        Vector tmp = (Vector) (item).get(17);
                        for (Object tmp1 : tmp) {
                            xCell.setFormula(xCell.getFormula() + tmp1.toString());
                            xCell.setFormula(xCell.getFormula() + "\n");
                        }
                    }

                    //цена USD
                    xCell = xSpreadsheet.getCellByPosition(7, nRow);
                    xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
                    xPropSet.setPropertyValue("TableBorder", aBorder);
                    xCell.setFormula(xCell.getFormula() + "\n");
                    if ((item).get(18) != null) {
                        Vector tmp = (Vector) (item).get(18);
                        for (Object tmp1 : tmp) {
                            xCell.setFormula(xCell.getFormula() + tmp1.toString());
                            xCell.setFormula(xCell.getFormula() + "\n");
                        }
                    }

                    //цена EUR
                    xCell = xSpreadsheet.getCellByPosition(8, nRow);
                    xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
                    xPropSet.setPropertyValue("TableBorder", aBorder);
                    xCell.setFormula(xCell.getFormula() + "\n");
                    if ((item).get(19) != null) {
                        Vector tmp = (Vector) (item).get(19);
                        for (Object tmp1 : tmp) {
                            xCell.setFormula(xCell.getFormula() + tmp1.toString());
                            xCell.setFormula(xCell.getFormula() + "\n");
                        }
                    }

                    //выпуск(шт.)
                    xCell = xSpreadsheet.getCellByPosition(9, nRow);
                    xCell.setFormula((item).get(13) != null ? (item).get(13).toString() : "");
                    xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
                    xPropSet.setPropertyValue("TableBorder", aBorder);

                    //расцветка
                    xCell = xSpreadsheet.getCellByPosition(10, nRow);
                    xCell.setFormula((item).get(12) != null ? (item).get(12).toString() : "");
                    xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
                    xPropSet.setPropertyValue("TableBorder", aBorder);

                    //изображение
                    xCell = xSpreadsheet.getCellByPosition(11, nRow);
                    xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
                    xPropSet.setPropertyValue("TableBorder", aBorder);

                    String path = getPath((item).get(3).toString());

                    if (!path.equals("")) {
                        insertImage(xSpreadsheetDocument,
                                xSpreadsheet,
                                xCell,
                                path,
                                (item).get(3).toString(),
                                nRow);
                    }
                } else if (type.equals("ALL")) {
                    //цена BYR
                    xCell = xSpreadsheet.getCellByPosition(6, nRow);
                    xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
                    xPropSet.setPropertyValue("TableBorder", aBorder);
                    xCell.setFormula(xCell.getFormula() + "\n");
                    if ((item).get(16) != null) {
                        Vector tmp = (Vector) (item).get(16);
                        for (Object tmp1 : tmp) {

                            xCell.setFormula(xCell.getFormula() + tmp1.toString());
                            xCell.setFormula(xCell.getFormula() + "\n");
                        }
                    }

                    //цена RUB
                    xCell = xSpreadsheet.getCellByPosition(7, nRow);
                    xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
                    xPropSet.setPropertyValue("TableBorder", aBorder);
                    xCell.setFormula(xCell.getFormula() + "\n");
                    if ((item).get(17) != null) {
                        Vector tmp = (Vector) (item).get(17);
                        for (Object tmp1 : tmp) {
                            System.out.println("RUB " + tmp1);
                            xCell.setFormula(xCell.getFormula() + tmp1.toString());
                            xCell.setFormula(xCell.getFormula() + "\n");
                        }
                    }

                    //цена USD
                    xCell = xSpreadsheet.getCellByPosition(8, nRow);
                    xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
                    xPropSet.setPropertyValue("TableBorder", aBorder);
                    xCell.setFormula(xCell.getFormula() + "\n");
                    if ((item).get(18) != null) {
                        Vector tmp = (Vector) (item).get(18);
                        for (Object tmp1 : tmp) {
                            System.out.println("USD " + tmp1);
                            xCell.setFormula(xCell.getFormula() + tmp1.toString());
                            xCell.setFormula(xCell.getFormula() + "\n");
                        }
                    }

                    //цена EUR
                    xCell = xSpreadsheet.getCellByPosition(9, nRow);
                    xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
                    xPropSet.setPropertyValue("TableBorder", aBorder);
                    xCell.setFormula(xCell.getFormula() + "\n");
                    if ((item).get(19) != null) {
                        Vector tmp = (Vector) (item).get(19);
                        for (Object tmp1 : tmp) {
                            System.out.println("EUR " + tmp1);
                            xCell.setFormula(xCell.getFormula() + tmp1.toString());
                            xCell.setFormula(xCell.getFormula() + "\n");
                        }
                    }

                    //выпуск(шт.)
                    xCell = xSpreadsheet.getCellByPosition(10, nRow);
                    xCell.setFormula((item).get(13) != null ? (item).get(13).toString() : "");
                    xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
                    xPropSet.setPropertyValue("TableBorder", aBorder);

                    //расцветка
                    xCell = xSpreadsheet.getCellByPosition(11, nRow);
                    xCell.setFormula((item).get(12) != null ? (item).get(12).toString() : "");
                    xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
                    xPropSet.setPropertyValue("TableBorder", aBorder);
                }

                nCol = 2;
            }

            nRow += 2;
            xCell = xSpreadsheet.getCellByPosition(2, nRow);
            xCell.setFormula("Кол-во моделей");
            xCell = xSpreadsheet.getCellByPosition(6, nRow);
            xCell.setFormula("шт.");
            xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
            xPropSet.setPropertyValue("CharHeight", (float) (9));
            xCell = xSpreadsheet.getCellByPosition(5, nRow);
            xCell.setValue(fas.size());

            nRow++;
            xCell = xSpreadsheet.getCellByPosition(2, nRow);
            xCell.setFormula("Кол-во новинок");
            xCell = xSpreadsheet.getCellByPosition(6, nRow);
            xCell.setFormula("шт.");
            xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
            xPropSet.setPropertyValue("CharHeight", (float) (9));
            xCell = xSpreadsheet.getCellByPosition(5, nRow);
            xCell.setValue(newFas.size());

        } catch (java.lang.Exception e) {
            log.error("Ошибка при заполнении отчёта.", e);
            throw new Exception("Ошибка при заполнении отчёта.", e);
        }
    }

    private void fildProductionPlanDocAllNorm(XComponent currentDocument) throws Exception {
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

            XCell xCell = xSpreadsheet.getCellByPosition(2, 1);

            //----------------Заполнение шапки документа

            xCell.setFormula(xCell.getFormula() + nameTamplates);

            //----------------Заполнение тела документа

            String format = UtilFunctions.getFormatRoundingNorm(UtilPlan.ROUNDING_NORM).replace(".", ",");

            int nCol = 1;
            int nRow = 6;

            int kol = 0;

            for (Object obj : vec) {
                Vector item = (Vector) obj;

                xCell = xSpreadsheet.getCellByPosition(nCol++, nRow);
                xCell.setValue(++kol);
                xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
                xPropSet.setPropertyValue("TableBorder", aBorder);

                for (int i = 0; i < item.size(); i++) {
                    xCell = xSpreadsheet.getCellByPosition(nCol++, nRow);

                    xCell.setFormula((item).get(i) != null ? (item).get(i).toString() : "");
                    xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
                    xPropSet.setPropertyValue("TableBorder", aBorder);

                    if (i == 2) {
                        xCell.setValue(Double.valueOf((item).get(i).toString()));
                    } else if (i > 2) {
                        setNumberFormat(xSpreadsheetDocument, xCell, format);

                        xCell = xSpreadsheet.getCellByPosition(nCol++, nRow);
                        xCell.setFormula("=" + Double.valueOf((item).get(i).toString()) + "*E" + (nRow + 1) + "");
                        xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
                        xPropSet.setPropertyValue("TableBorder", aBorder);
                    }
                }

                xCell = xSpreadsheet.getCellByPosition(nCol++, nRow);
                xCell.setFormula("=G" + (nRow + 1) + "+I" + (nRow + 1) + "+K" + (nRow + 1) + "+M" + (nRow + 1) + "");
                xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
                xPropSet.setPropertyValue("TableBorder", aBorder);

                nCol = 1;
                nRow++;
            }
            nRow++;

            xCell = xSpreadsheet.getCellByPosition(2, nRow);
            xCell.setFormula("ВСЕГО");

            xCell = xSpreadsheet.getCellByPosition(4, nRow);
            xCell.setFormula("=SUM(E7:E" + (nRow - 1) + ")");

            xCell = xSpreadsheet.getCellByPosition(6, nRow);
            xCell.setFormula("=SUM(G7:G" + (nRow - 1) + ")");

            xCell = xSpreadsheet.getCellByPosition(8, nRow);
            xCell.setFormula("=SUM(I7:I" + (nRow - 1) + ")");

            xCell = xSpreadsheet.getCellByPosition(10, nRow);
            xCell.setFormula("=SUM(K7:K" + (nRow - 1) + ")");

            xCell = xSpreadsheet.getCellByPosition(12, nRow);
            xCell.setFormula("=SUM(M7:M" + (nRow - 1) + ")");

            xCell = xSpreadsheet.getCellByPosition(13, nRow);
            xCell.setFormula("=SUM(N7:N" + (nRow - 1) + ")");

        } catch (java.lang.Exception e) {
            log.error("Ошибка при заполнении отчёта.", e);
            throw new Exception("Ошибка при заполнении отчёта.", e);
        }
    }

    public void insertImage(XSpreadsheetDocument document,
                            XSpreadsheet sheet,
                            XCell cell,
                            String sPath,
                            String idImage,
                            int nRow) {

        try {

            String index = idImage;
            Size size = getImageSize(System.getProperty("user.home") + "/image.jpg");

            XModel xSpreadsheetModel = (XModel) UnoRuntime.queryInterface(XModel.class, document);
            XMultiServiceFactory xmsf = (XMultiServiceFactory) UnoRuntime.queryInterface(XMultiServiceFactory.class, xSpreadsheetModel);

            XNameContainer xBitmapContainer = (XNameContainer) UnoRuntime.queryInterface(XNameContainer.class, xmsf.createInstance("com.sun.star.drawing.BitmapTable"));

            if (!xBitmapContainer.hasByName(index))
                xBitmapContainer.insertByName(index, sPath);
            String internalURL = AnyConverter.toString(xBitmapContainer.getByName(index));

            Object oGraphic = xmsf.createInstance("com.sun.star.drawing.GraphicObjectShape");

            com.sun.star.beans.XPropertySet xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, cell);
            com.sun.star.awt.Point point = (com.sun.star.awt.Point) xPropSet.getPropertyValue("Position");

            XPropertySet xGraphicProps = (XPropertySet) UnoRuntime.queryInterface(XPropertySet.class, oGraphic);
            xGraphicProps.setPropertyValue("GraphicURL", internalURL);

            XShape xGraphicShape = (XShape) UnoRuntime.queryInterface(XShape.class, oGraphic);

            XDrawPageSupplier xDrawPageSupplier = (XDrawPageSupplier) UnoRuntime.queryInterface(XDrawPageSupplier.class, sheet);
            XDrawPage xDrawPage = xDrawPageSupplier.getDrawPage();

            try {
                xDrawPage.add(xGraphicShape);
                xGraphicShape.setSize(new Size(size.Width * 10, size.Height * 10));
                xGraphicShape.setPosition(point);
            } catch (Exception e) {
                System.out.println("Ошибка");
            }

            XCellRange xCellRange = sheet.getCellRangeByPosition(0, nRow, 0, nRow + 1);
            setCustomRow(xCellRange, (size.Height * 10) + 50);

            if (maxSizeCol < (size.Width * 10) + 50)
                maxSizeCol = (size.Width * 10) + 50;

        } catch (Exception ex) {
            log.error("Ошибка в методе insertImageToDoc() ", ex);
            System.err.println("Ошибка в методе insertImageToDoc(): " + ex.getMessage());
        }
    }

    private void setCustomRow(XCellRange range, int width) {
        com.sun.star.table.XColumnRowRange xColRowRange = (com.sun.star.table.XColumnRowRange) UnoRuntime.queryInterface(com.sun.star.table.XColumnRowRange.class, range);
        com.sun.star.table.XTableRows xRows = xColRowRange.getRows();
        try {
            Object aRowObj = xRows.getByIndex(0);
            XPropertySet xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, aRowObj);
            xPropSet.setPropertyValue("Height", new Integer(width) + 50);
        } catch (Exception e) {
            System.out.println("Ошибка");
        }
    }

    private void setCustomColumn(XCellRange range, int width) {
        com.sun.star.table.XColumnRowRange xColRowRange = (com.sun.star.table.XColumnRowRange) UnoRuntime.queryInterface(com.sun.star.table.XColumnRowRange.class, range);
        com.sun.star.table.XTableColumns xColumns = xColRowRange.getColumns();
        try {
            Object aColumnObj = xColumns.getByIndex(0);
            XPropertySet xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, aColumnObj);
            xPropSet.setPropertyValue("Width", new Integer(width) + 50);
        } catch (Exception e) {
            System.out.println("Ошибка");
        }
    }

    private Size getImageSize(String fName) {
        BufferedImage img = null;
        try {
            img = ImageIO.read(new File(fName));
        } catch (IOException e) {
            System.err.println("Ошибка метода getImageSize()" + e);
            log.error("Ошибка метода getImageSize()", e);
            return new Size(200, 250);
        }
        return new Size(img.getWidth(), img.getHeight());
    }

    private String getPath(String fas) {
        String sPath = "";
        try {

            sPath = (UtilFunctions.getCatalogImageDir() + "/" + fas + ".jpg");//.replace("/", "\\");

            String pref = !UtilFunctions.isWindows() ? "/" : "";

            if ((new File(sPath)).exists()) {
                ImageTools imgTools = new ImageTools();
                BufferedImage buffImage = imgTools.imageOpen(sPath);
                //BufferedImage imgWater = imgTools.placeTextOnImage(buffImage,"www.8marta.com") ;
                imgTools.imageSaveAs(buffImage, System.getProperty("user.home"), "/image.jpg");

                if (UtilFunctions.isWindows()) {
                    sPath = "file:///" + pref + System.getProperty("user.home") + "/image.jpg";
                } else {
                    sPath = "file:/" + pref + System.getProperty("user.home") + "/image.jpg";
                }

            } else {
                sPath = "";
            }
        } catch (Exception e) {
            sPath = "";
            System.out.println("Ошибка " + e.getMessage());
        }
        return sPath;
    }
}
