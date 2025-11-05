package dept.production.zsh.spec;

import by.march8.ecs.framework.common.LogCrutch;
import com.sun.star.awt.FontWeight;
import com.sun.star.beans.XPropertySet;
import com.sun.star.lang.Locale;
import com.sun.star.lang.XComponent;
import com.sun.star.sheet.XSpreadsheet;
import com.sun.star.sheet.XSpreadsheetDocument;
import com.sun.star.sheet.XSpreadsheets;
import com.sun.star.table.XCell;
import com.sun.star.uno.UnoRuntime;
import com.sun.star.util.XNumberFormats;
import com.sun.star.util.XNumberFormatsSupplier;
import workOO.OO_new;

import javax.swing.*;
import java.util.Vector;

//import org.apache.log4j.Logger;

/**
 *
 * @author lidashka
 */
@SuppressWarnings("all")
public class SpecOO extends OO_new {
    //private static final Logger log = new Log().getLoger(SpecOO.class);
    private static final LogCrutch log = new LogCrutch();
    static Vector vdata;
    static Vector vtech;
    static Vector vvec;
    static Vector dopVec;
    static String dateReport;
    static String nameTamplate;
    static double norm;
    static int idDept;


    public SpecOO(String nameSpec,
                  String date,
                  double bnorm,
                  Vector dataItem,
                  Vector dataTech,
                  int dept) {

        nameTamplate = nameSpec;
        dateReport = date;
        norm = bnorm;
        vdata = dataItem;
        vtech = dataTech;
        idDept = dept;
    }

    public SpecOO(Vector data, int dept) {
        vdata = data;
        idDept = dept;
    }

    private static void fildVedomostTR2(XComponent currentDocument) throws Exception {
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
            XCell xCell = xSpreadsheet.getCellByPosition(0, 0);
            xCell.setValue(idDept);

            xCell = xSpreadsheet.getCellByPosition(5, 10);
            xCell.setFormula("Вводится с " + dateReport + "г.");
            xPropSet = UnoRuntime.queryInterface(XPropertySet.class, xCell);
            xPropSet.setPropertyValue("CharWeight", new Float(FontWeight.BOLD));

            xCell = xSpreadsheet.getCellByPosition(2, 10);
            xCell.setFormula(nameTamplate);
            xPropSet = UnoRuntime.queryInterface(XPropertySet.class, xCell);
            xPropSet.setPropertyValue("CharWeight", new Float(FontWeight.BOLD));

            //----------------Заполнение тела документа

            String format = UtilSpec.getFormatRoundingNorm().replace(".", ",");
            int nCol = 1;
            int nRow = 11;
            int[] num = {0, 1, 4, 5, 6};
            int sSum = -1;
            int eSum = -1;

            for (Object row : vdata) {
                if (Integer.valueOf(((Vector) row).get(0).toString()) >= 0) {
                    for (int i = 0; i < num.length; i++) {
                        xCell = xSpreadsheet.getCellByPosition(nCol++, nRow);
                        xCell.setFormula(((Vector) row).get(num[i]).toString());
                        if (num[i] == 6)
                            setNumberFormat(xSpreadsheetDocument, xCell, format);
                        xPropSet = UnoRuntime.queryInterface(XPropertySet.class, xCell);
                        xPropSet.setPropertyValue("TableBorder", aBorder);
                    }
                    nRow++;
                }
                nCol = 1;

            }

            for (int i = 0; i < num.length; i++) {
                xCell = xSpreadsheet.getCellByPosition(nCol++, nRow);
                if (i == 1) {
                    xCell.setFormula("Бригадная норма времени");
                    xPropSet = UnoRuntime.queryInterface(XPropertySet.class, xCell);
                    xPropSet.setPropertyValue("CharWeight", new Float(FontWeight.BOLD));
                } else if (i == 4) {
                    xCell.setValue(norm);
                    setNumberFormat(xSpreadsheetDocument, xCell, format);
                    xPropSet = UnoRuntime.queryInterface(XPropertySet.class, xCell);
                    xPropSet.setPropertyValue("CharWeight", new Float(FontWeight.BOLD));
                }
                xPropSet = UnoRuntime.queryInterface(XPropertySet.class, xCell);
                xPropSet.setPropertyValue("TableBorder", aBorder);
            }

            nRow++;
            nCol = 1;
            for (Object row : vdata) {
                if (Integer.valueOf(((Vector) row).get(0).toString()) < 0) {
                    for (int i = 0; i < num.length; i++) {
                        xCell = xSpreadsheet.getCellByPosition(nCol++, nRow);
                        if (num[i] != 0) {
                            xCell.setFormula(((Vector) row).get(num[i]).toString());
                            if (num[i] == 6)
                                setNumberFormat(xSpreadsheetDocument, xCell, format);
                        }
                        xPropSet = UnoRuntime.queryInterface(XPropertySet.class, xCell);
                        xPropSet.setPropertyValue("TableBorder", aBorder);
                    }
                    nRow++;
                }
                nCol = 1;
            }

            if (idDept == UtilSpec.ID_DEPT_SEWING) {
                nRow++;
                xCell = xSpreadsheet.getCellByPosition(2, nRow++);
                xCell.setFormula("Оборудование " + nameTamplate);
                xPropSet = UnoRuntime.queryInterface(XPropertySet.class, xCell);
                xPropSet.setPropertyValue("CharWeight", new Float(FontWeight.BOLD));

                sSum = nRow + 1;
                for (Object row : vtech) {
                    for (int i = 0; i < num.length; i++) {
                        xCell = xSpreadsheet.getCellByPosition(nCol++, nRow);
                        if (i == 1)
                            xCell.setFormula(((Vector) row).get(1).toString());
                        else if (i == 3) {
                            xCell.setFormula(((Vector) row).get(2).toString());
                            setNumberFormat(xSpreadsheetDocument, xCell, format);
                        }
                        xPropSet = UnoRuntime.queryInterface(XPropertySet.class, xCell);
                        xPropSet.setPropertyValue("TableBorder", aBorder);
                    }
                    nCol = 1;
                    eSum = nRow + 1;
                    nRow++;
                }

                for (int i = 0; i < num.length; i++) {
                    xCell = xSpreadsheet.getCellByPosition(nCol++, nRow);
                    if (i == 1) {
                        xCell.setFormula("ИТОГО(по оборудованию)");
                        xPropSet = UnoRuntime.queryInterface(XPropertySet.class, xCell);
                        xPropSet.setPropertyValue("CharWeight", new Float(FontWeight.BOLD));
                    } else if (i == 3) {
                        if (sSum > -1 && eSum > -1) {
                            xCell.setFormula("=SUM(E" + sSum + ":E" + eSum + ")");
                            setNumberFormat(xSpreadsheetDocument, xCell, format);
                            xPropSet = UnoRuntime.queryInterface(XPropertySet.class, xCell);
                            xPropSet.setPropertyValue("CharWeight", new Float(FontWeight.BOLD));
                        }
                    }
                    xPropSet = UnoRuntime.queryInterface(XPropertySet.class, xCell);
                    xPropSet.setPropertyValue("TableBorder", aBorder);
                }
            }

            nRow += 2;

            if (idDept != UtilSpec.ID_DEPT_SEWING) {
                xCell = xSpreadsheet.getCellByPosition(2, nRow);
                xCell.setFormula("Инженер по нормированию труда");

                xCell = xSpreadsheet.getCellByPosition(5, nRow++);
                xCell.setFormula(UtilSpec.ENGINEER_2);
            } else {
                xCell = xSpreadsheet.getCellByPosition(2, nRow);
                xCell.setFormula("Ведущий инженер по нормированию труда");

                xCell = xSpreadsheet.getCellByPosition(5, nRow++);
                xCell.setFormula(UtilSpec.ENGINEER_VED);

                xCell = xSpreadsheet.getCellByPosition(2, nRow);
                xCell.setFormula("Инженер по нормированию труда");

                xCell = xSpreadsheet.getCellByPosition(5, nRow++);
                xCell.setFormula(UtilSpec.ENGINEER_1);

                xCell = xSpreadsheet.getCellByPosition(2, nRow);
                xCell.setFormula("Инженер по нормированию труда");

                xCell = xSpreadsheet.getCellByPosition(5, nRow++);
                xCell.setFormula(UtilSpec.ENGINEER_3);
            }

        } catch (Exception e) {
            log.error("Ошибка при заполнении отчёта", e);
            throw new Exception("Ошибка при формировании отчёта ", e);
        }
    }

    private static void fildVedomostTR2Price(XComponent currentDocument) throws Exception {
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
            XCell xCell = xSpreadsheet.getCellByPosition(0, 0);
            xCell.setValue(idDept);

            xCell = xSpreadsheet.getCellByPosition(5, 10);
            xCell.setFormula("Вводится с " + dateReport + "г.");
            xPropSet = UnoRuntime.queryInterface(XPropertySet.class, xCell);
            xPropSet.setPropertyValue("CharWeight", new Float(FontWeight.BOLD));

            xCell = xSpreadsheet.getCellByPosition(2, 10);
            xCell.setFormula(nameTamplate);
            xPropSet = UnoRuntime.queryInterface(XPropertySet.class, xCell);
            xPropSet.setPropertyValue("CharWeight", new Float(FontWeight.BOLD));

            //----------------Заполнение тела документа

            String format = UtilSpec.getFormatRoundingNorm().replace(".", ",");
            int nCol = 1;
            int nRow = 11;
            int[] num = {0, 1, 4, 5, 6};
            int sSum = -1;
            int eSum = -1;
            double[] tmp = new double[31];

            for (Object row : vdata) {
                if (Integer.valueOf(((Vector) row).get(0).toString()) >= 0) {
                    for (int i = 0; i < num.length; i++) {
                        xCell = xSpreadsheet.getCellByPosition(nCol++, nRow);
                        xCell.setFormula(((Vector) row).get(num[i]).toString());
                        if (num[i] == 4)
                            tmp[Integer.valueOf(((Vector) row).get(4).toString())] += Double.valueOf(((Vector) row).get(6).toString());
                        if (num[i] == 6)
                            setNumberFormat(xSpreadsheetDocument, xCell, format);
                        xPropSet = UnoRuntime.queryInterface(XPropertySet.class, xCell);
                        xPropSet.setPropertyValue("TableBorder", aBorder);
                    }
                    nRow++;
                }
                nCol = 1;
            }

            for (int i = 0; i < num.length; i++) {
                xCell = xSpreadsheet.getCellByPosition(nCol++, nRow);
                if (i == 1) {
                    xCell.setFormula("Бригадная норма времени");
                    xPropSet = UnoRuntime.queryInterface(XPropertySet.class, xCell);
                    xPropSet.setPropertyValue("CharWeight", new Float(FontWeight.BOLD));
                } else if (i == 4) {
                    xCell.setValue(norm);
                    setNumberFormat(xSpreadsheetDocument, xCell, format);
                    xPropSet = UnoRuntime.queryInterface(XPropertySet.class, xCell);
                    xPropSet.setPropertyValue("CharWeight", new Float(FontWeight.BOLD));
                }
                xPropSet = UnoRuntime.queryInterface(XPropertySet.class, xCell);
                xPropSet.setPropertyValue("TableBorder", aBorder);
            }

            nRow++;
            nCol = 1;
            for (Object row : vdata) {
                if (Integer.valueOf(((Vector) row).get(0).toString()) < 0) {
                    for (int i = 0; i < num.length; i++) {
                        xCell = xSpreadsheet.getCellByPosition(nCol++, nRow);
                        if (num[i] != 0) {
                            xCell.setFormula(((Vector) row).get(num[i]).toString());
                            if (num[i] == 6)
                                setNumberFormat(xSpreadsheetDocument, xCell, format);
                        }
                        xPropSet = UnoRuntime.queryInterface(XPropertySet.class, xCell);
                        xPropSet.setPropertyValue("TableBorder", aBorder);
                    }
                    nRow++;
                }
                nCol = 1;
            }

            if (idDept == UtilSpec.ID_DEPT_SEWING) {
                nRow++;

                xCell = xSpreadsheet.getCellByPosition(2, nRow++);
                xCell.setFormula("Оборудование " + nameTamplate);
                xPropSet = UnoRuntime.queryInterface(XPropertySet.class, xCell);
                xPropSet.setPropertyValue("CharWeight", new Float(FontWeight.BOLD));

                sSum = nRow + 1;
                for (Object row : vtech) {
                    for (int i = 0; i < num.length; i++) {
                        xCell = xSpreadsheet.getCellByPosition(nCol++, nRow);
                        if (i == 1)
                            xCell.setFormula(((Vector) row).get(1).toString());
                        else if (i == 3) {
                            xCell.setFormula(((Vector) row).get(2).toString());
                            setNumberFormat(xSpreadsheetDocument, xCell, format);
                        }
                        xPropSet = UnoRuntime.queryInterface(XPropertySet.class, xCell);
                        xPropSet.setPropertyValue("TableBorder", aBorder);
                    }
                    nCol = 1;
                    eSum = nRow + 1;
                    nRow++;
                }

                for (int i = 0; i < num.length; i++) {
                    xCell = xSpreadsheet.getCellByPosition(nCol++, nRow);
                    if (i == 1) {
                        xCell.setFormula("ИТОГО(по оборудованию)");
                        xPropSet = UnoRuntime.queryInterface(XPropertySet.class, xCell);
                        xPropSet.setPropertyValue("CharWeight", new Float(FontWeight.BOLD));
                    } else if (i == 3) {
                        if (sSum > -1 && eSum > -1) {
                            xCell.setFormula("=SUM(E" + sSum + ":E" + eSum + ")");
                            setNumberFormat(xSpreadsheetDocument, xCell, format);
                            xPropSet = UnoRuntime.queryInterface(XPropertySet.class, xCell);
                            xPropSet.setPropertyValue("CharWeight", new Float(FontWeight.BOLD));
                        }
                    }
                    xPropSet = UnoRuntime.queryInterface(XPropertySet.class, xCell);
                    xPropSet.setPropertyValue("TableBorder", aBorder);
                }
                nRow++;
            }

            nRow += 2;

            xCell = xSpreadsheet.getCellByPosition(2, nRow++);
            xCell.setFormula("Цены " + dateReport);
            xPropSet = UnoRuntime.queryInterface(XPropertySet.class, xCell);
            xPropSet.setPropertyValue("CharWeight", new Float(FontWeight.BOLD));

            xCell = xSpreadsheet.getCellByPosition(2, nRow);
            xCell.setFormula("Пошив");

            for (int i = 0; i < tmp.length; i++) {
                if (tmp[i] > 0) {
                    xCell = xSpreadsheet.getCellByPosition(3, nRow);
                    xCell.setValue(i);
                    xCell = xSpreadsheet.getCellByPosition(4, nRow++);
                    xCell.setFormula(UtilSpec.formatNorm(tmp[i]));
                    setNumberFormat(xSpreadsheetDocument, xCell, format);
                }
            }
            nRow++;

            xCell = xSpreadsheet.getCellByPosition(2, nRow++);
            xCell.setFormula("ВТО");

            xCell = xSpreadsheet.getCellByPosition(2, nRow++);
            xCell.setFormula("Упаковка");

            xCell = xSpreadsheet.getCellByPosition(2, nRow++);
            xCell.setFormula("ИТОГО(по пошиву)");

            xCell = xSpreadsheet.getCellByPosition(2, nRow++);
            xCell.setFormula("Раскрой");

            xCell = xSpreadsheet.getCellByPosition(2, nRow++);
            xCell.setFormula("Кант");

            xCell = xSpreadsheet.getCellByPosition(2, nRow++);
            xCell.setFormula("ИТОГО(по раскрою)");

            xCell = xSpreadsheet.getCellByPosition(2, nRow++);
            xCell.setFormula("Мультипла");

            nRow += 2;

            if (idDept != UtilSpec.ID_DEPT_SEWING) {
                xCell = xSpreadsheet.getCellByPosition(2, nRow);
                xCell.setFormula("Инженер по нормированию труда");

                xCell = xSpreadsheet.getCellByPosition(5, nRow++);
                xCell.setFormula(UtilSpec.ENGINEER_2);
            } else {
                xCell = xSpreadsheet.getCellByPosition(2, nRow);
                xCell.setFormula("Ведущий инженер по нормированию труда");

                xCell = xSpreadsheet.getCellByPosition(5, nRow++);
                xCell.setFormula(UtilSpec.ENGINEER_VED);

                xCell = xSpreadsheet.getCellByPosition(2, nRow);
                xCell.setFormula("Инженер по нормированию труда");

                xCell = xSpreadsheet.getCellByPosition(5, nRow++);
                xCell.setFormula(UtilSpec.ENGINEER_1);

                xCell = xSpreadsheet.getCellByPosition(2, nRow);
                xCell.setFormula("Инженер по нормированию труда");

                xCell = xSpreadsheet.getCellByPosition(5, nRow++);
                xCell.setFormula(UtilSpec.ENGINEER_3);
            }

        } catch (Exception e) {
            log.error("Ошибка при заполнении отчёта", e);
            throw new Exception("Ошибка при формировании отчёта ", e);
        }
    }

    private static void fildVedomostNorm(XComponent currentDocument) throws Exception {
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

            String format = UtilSpec.getFormatRoundingNorm().replace(".", ",");
            int nCol;
            int nRow = 10;
            int[] num = {0, 1, 4, 5, 6};

            XCell xCell = xSpreadsheet.getCellByPosition(0, 0);
            xCell.setValue(idDept);

            for (Object data : vdata) {
                //----------------Заполнение шапки документа
                xCell = xSpreadsheet.getCellByPosition(2, nRow);
                xCell.setFormula(((Vector) data).elementAt(0).toString());
                xPropSet = UnoRuntime.queryInterface(XPropertySet.class, xCell);
                xPropSet.setPropertyValue("CharWeight", new Float(FontWeight.BOLD));

                xCell = xSpreadsheet.getCellByPosition(5, nRow);
                xCell.setFormula("Вводится с " + ((Vector) data).elementAt(1) + "г.");
                xPropSet = UnoRuntime.queryInterface(XPropertySet.class, xCell);
                xPropSet.setPropertyValue("CharWeight", new Float(FontWeight.BOLD));

                //----------------Заполнение тела документа

                nCol = 1;
                nRow++;

                for (Object row : (Vector) ((Vector) data).elementAt(3)) {
                    if (Integer.valueOf(((Vector) row).get(0).toString()) >= 0) {
                        for (int i = 0; i < num.length; i++) {
                            xCell = xSpreadsheet.getCellByPosition(nCol++, nRow);
                            xCell.setFormula(((Vector) row).get(num[i]).toString());
                            if (num[i] == 6)
                                setNumberFormat(xSpreadsheetDocument, xCell, format);
                            xPropSet = UnoRuntime.queryInterface(XPropertySet.class, xCell);
                            xPropSet.setPropertyValue("TableBorder", aBorder);
                        }
                        nRow++;
                    }
                    nCol = 1;
                }

                for (int i = 0; i < num.length; i++) {
                    xCell = xSpreadsheet.getCellByPosition(nCol++, nRow);
                    if (i == 1) {
                        xCell.setFormula("Бригадная норма времени");
                        xPropSet = UnoRuntime.queryInterface(XPropertySet.class, xCell);
                        xPropSet.setPropertyValue("CharWeight", new Float(FontWeight.BOLD));
                    } else if (i == 4) {
                        xCell.setFormula(((Vector) data).elementAt(2).toString());
                        setNumberFormat(xSpreadsheetDocument, xCell, format);
                        xPropSet = UnoRuntime.queryInterface(XPropertySet.class, xCell);
                        xPropSet.setPropertyValue("CharWeight", new Float(FontWeight.BOLD));
                    }
                    xPropSet = UnoRuntime.queryInterface(XPropertySet.class, xCell);
                    xPropSet.setPropertyValue("TableBorder", aBorder);
                }

                nRow++;
                nCol = 1;
                for (Object row : (Vector) ((Vector) data).elementAt(3)) {
                    if (Integer.valueOf(((Vector) row).get(0).toString()) < 0) {
                        for (int i = 0; i < num.length; i++) {
                            xCell = xSpreadsheet.getCellByPosition(nCol++, nRow);
                            if (num[i] != 0) {
                                xCell.setFormula(((Vector) row).get(num[i]).toString());
                                if (num[i] == 6)
                                    setNumberFormat(xSpreadsheetDocument, xCell, format);
                            }
                            xPropSet = UnoRuntime.queryInterface(XPropertySet.class, xCell);
                            xPropSet.setPropertyValue("TableBorder", aBorder);
                        }
                        nRow++;
                    }
                    nCol = 1;
                }

                nRow++;
            }

            xCell = xSpreadsheet.getCellByPosition(2, nRow);
            xCell.setFormula("Начальник ООТиЗ");

            xCell = xSpreadsheet.getCellByPosition(5, nRow++);
            xCell.setFormula(UtilSpec.FOREMAN_OOTIZ);

            xCell = xSpreadsheet.getCellByPosition(2, nRow);
            xCell.setFormula("Начальник цеха");

            xCell = xSpreadsheet.getCellByPosition(5, nRow++);
            xCell.setFormula("=IF(A1=8;Лист2.D3;Лист2.D4)");

            if (idDept != UtilSpec.ID_DEPT_SEWING) {
                xCell = xSpreadsheet.getCellByPosition(2, nRow);
                xCell.setFormula("Инженер по Н.Т.");

                xCell = xSpreadsheet.getCellByPosition(5, nRow++);
                xCell.setFormula(UtilSpec.ENGINEER_2);
            } else {
                xCell = xSpreadsheet.getCellByPosition(2, nRow);
                xCell.setFormula("Ведущий инженер по Н.Т.");

                xCell = xSpreadsheet.getCellByPosition(5, nRow++);
                xCell.setFormula(UtilSpec.ENGINEER_VED);

                xCell = xSpreadsheet.getCellByPosition(2, nRow);
                xCell.setFormula("Инженер по Н.Т.");

                xCell = xSpreadsheet.getCellByPosition(5, nRow++);
                xCell.setFormula(UtilSpec.ENGINEER_1);

                xCell = xSpreadsheet.getCellByPosition(2, nRow);
                xCell.setFormula("Инженер по Н.Т.");

                xCell = xSpreadsheet.getCellByPosition(5, nRow++);
                xCell.setFormula(UtilSpec.ENGINEER_3);
            }

        } catch (Exception e) {
            log.error("Ошибка при заполнении отчёта", e);
            throw new Exception("Ошибка при формировании отчёта ", e);
        }
    }

    private static void fildVedomostNakopTech(XComponent currentDocument) throws Exception {
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

            String format = UtilSpec.getFormatRoundingNorm().replace(".", ",");
            int nCol;
            int nRow = 10;
            int sSum = -1;
            int eSum = -1;
            int j = 0;

            XCell xCell = xSpreadsheet.getCellByPosition(0, 0);
            xCell.setValue(idDept);

            for (Object data : vdata) {
                //----------------Заполнение шапки документа
                xCell = xSpreadsheet.getCellByPosition(2, nRow);
                xCell.setFormula(((Vector) data).elementAt(0).toString());
                xPropSet = UnoRuntime.queryInterface(XPropertySet.class, xCell);
                xPropSet.setPropertyValue("CharWeight", new Float(FontWeight.BOLD));

                xCell = xSpreadsheet.getCellByPosition(3, nRow);
                xCell.setFormula("Вводится с " + ((Vector) data).elementAt(1) + "г.");
                xPropSet = UnoRuntime.queryInterface(XPropertySet.class, xCell);
                xPropSet.setPropertyValue("CharWeight", new Float(FontWeight.BOLD));

                //----------------Заполнение тела документа

                nCol = 1;
                nRow++;

                sSum = nRow + 1;
                for (Object row : (Vector) ((Vector) data).elementAt(2)) {
                    xCell = xSpreadsheet.getCellByPosition(nCol++, nRow);
                    xCell.setValue(++j);
                    xPropSet = UnoRuntime.queryInterface(XPropertySet.class, xCell);
                    xPropSet.setPropertyValue("TableBorder", aBorder);

                    xCell = xSpreadsheet.getCellByPosition(nCol++, nRow);
                    xCell.setFormula(((Vector) row).get(1).toString());
                    xPropSet = UnoRuntime.queryInterface(XPropertySet.class, xCell);
                    xPropSet.setPropertyValue("TableBorder", aBorder);

                    xCell = xSpreadsheet.getCellByPosition(nCol++, nRow);
                    xCell.setFormula(((Vector) row).get(2).toString());
                    setNumberFormat(xSpreadsheetDocument, xCell, format);
                    xPropSet = UnoRuntime.queryInterface(XPropertySet.class, xCell);
                    xPropSet.setPropertyValue("TableBorder", aBorder);

                    nCol = 1;
                    eSum = nRow + 1;
                    nRow++;
                }

                xCell = xSpreadsheet.getCellByPosition(2, nRow);
                xCell.setFormula("ИТОГО(по оборудованию)");
                xPropSet = UnoRuntime.queryInterface(XPropertySet.class, xCell);
                xPropSet.setPropertyValue("CharWeight", new Float(FontWeight.BOLD));
                xCell = xSpreadsheet.getCellByPosition(3, nRow);
                if (sSum > -1 && eSum > -1) {
                    xCell.setFormula("=SUM(D" + sSum + ":D" + eSum + ")");
                    setNumberFormat(xSpreadsheetDocument, xCell, format);
                    xPropSet = UnoRuntime.queryInterface(XPropertySet.class, xCell);
                    xPropSet.setPropertyValue("CharWeight", new Float(FontWeight.BOLD));
                }
                xPropSet = UnoRuntime.queryInterface(XPropertySet.class, xCell);
                xPropSet.setPropertyValue("TableBorder", aBorder);

                sSum = -1;
                eSum = -1;
                j = 0;
                nRow += 2;
            }

            if (idDept != UtilSpec.ID_DEPT_SEWING) {
                xCell = xSpreadsheet.getCellByPosition(2, nRow);
                xCell.setFormula("Инженер по нормированию труда");

                xCell = xSpreadsheet.getCellByPosition(3, nRow++);
                xCell.setFormula(UtilSpec.ENGINEER_2);
            } else {
                xCell = xSpreadsheet.getCellByPosition(2, nRow);
                xCell.setFormula("Ведущий инженер по нормированию труда");

                xCell = xSpreadsheet.getCellByPosition(3, nRow++);
                xCell.setFormula(UtilSpec.ENGINEER_VED);

                xCell = xSpreadsheet.getCellByPosition(2, nRow);
                xCell.setFormula("Инженер по нормированию труда");

                xCell = xSpreadsheet.getCellByPosition(3, nRow++);
                xCell.setFormula(UtilSpec.ENGINEER_1);

                xCell = xSpreadsheet.getCellByPosition(2, nRow);
                xCell.setFormula("Инженер по нормированию труда");

                xCell = xSpreadsheet.getCellByPosition(3, nRow++);
                xCell.setFormula(UtilSpec.ENGINEER_3);
            }

        } catch (Exception e) {
            log.error("Ошибка при заполнении отчёта", e);
            throw new Exception("Ошибка при формировании отчёта ", e);
        }
    }

    private static void setNumberFormat(XSpreadsheetDocument spreadsheetDocument, XCell cell, String numberFormatString) {
        try {
            XPropertySet cellProp = UnoRuntime.queryInterface(XPropertySet.class, cell);
            int numberFormatKey = getNumberFormat(spreadsheetDocument, numberFormatString);
            cellProp.setPropertyValue("NumberFormat", new Integer(numberFormatKey));
        } catch (Exception e) {
            System.out.println("Ошибка! SetNumberFormat " + e.getMessage());
        }
    }

    private static int getNumberFormat(XSpreadsheetDocument spreadsheetDocument, String numberFormatString) {
        if (spreadsheetDocument == null || numberFormatString == null || numberFormatString.trim().equals(""))
            return 0;
        XNumberFormatsSupplier xNumberFormatsSupplier = UnoRuntime.queryInterface(XNumberFormatsSupplier.class, spreadsheetDocument);
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

    public void createReport(String nameTamplates) throws Exception {
        try {
            connect();
            XComponent currentDocument = openDocumentOld("Templates/" + nameTamplates);

            if (nameTamplates.equals("SpecТр-2.ots")) {
                fildVedomostTR2(currentDocument);
            } else if (nameTamplates.equals("SpecТр-2Price.ots")) {
                fildVedomostTR2Price(currentDocument);
            } else if (nameTamplates.equals("SpecVedomostNorm.ots")) {
                fildVedomostNorm(currentDocument);
            } else if (nameTamplates.equals("SpecVedomostNakopTech.ots")) {
                fildVedomostNakopTech(currentDocument);
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
}
