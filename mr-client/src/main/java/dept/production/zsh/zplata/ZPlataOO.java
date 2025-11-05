package dept.production.zsh.zplata;

import by.march8.ecs.framework.common.LogCrutch;
import com.sun.star.beans.XPropertySet;
import com.sun.star.container.NoSuchElementException;
import com.sun.star.container.XNameAccess;
import com.sun.star.lang.IndexOutOfBoundsException;
import com.sun.star.lang.Locale;
import com.sun.star.lang.WrappedTargetException;
import com.sun.star.lang.XComponent;
import com.sun.star.sheet.XHeaderFooterContent;
import com.sun.star.sheet.XSpreadsheet;
import com.sun.star.sheet.XSpreadsheetDocument;
import com.sun.star.sheet.XSpreadsheets;
import com.sun.star.style.XStyle;
import com.sun.star.style.XStyleFamiliesSupplier;
import com.sun.star.table.XCell;
import com.sun.star.text.XText;
import com.sun.star.uno.AnyConverter;
import com.sun.star.uno.Type;
import com.sun.star.uno.UnoRuntime;
import com.sun.star.util.XNumberFormats;
import com.sun.star.util.XNumberFormatsSupplier;
import dept.production.zsh.zplata.dto.TechDto;
import workOO.OO_new;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Vector;
import java.util.stream.Collectors;

//import org.apache.log4j.Logger;

/**
 *
 * @author lidashka
 */
public class ZPlataOO extends OO_new {
    // private static final Logger log = new Log().getLoger(ZPlataOO.class);
    private static final LogCrutch log = new LogCrutch();
    static Vector data;
    static Vector dataElement;
    static Vector elements1;
    static Vector workDays;
    static Vector elements2;
    static int idBrig;
    static int workSmena;
    static double brak;
    static String nameTamplates;
    static String path;
    static DefaultTableModel tModel;
    static TableColumnModel tModelCol;

    public ZPlataOO() {
    }

    public ZPlataOO(String path, int smena) {
        ZPlataOO.path = path;
        workSmena = smena;
    }

    public ZPlataOO(String nameTamplates, Vector data, Vector dataElement) {
        ZPlataOO.nameTamplates = nameTamplates;
        ZPlataOO.data = data;
        ZPlataOO.dataElement = dataElement;
    }

    public ZPlataOO(String nameTamplates, Vector data, Vector dataElement, Vector elements) {
        ZPlataOO.nameTamplates = nameTamplates;
        ZPlataOO.data = data;
        ZPlataOO.dataElement = dataElement;
        elements1 = elements;
    }

    public ZPlataOO(String nameTamplates, Vector data, Vector dataElement, Vector elements, Vector vec) {
        ZPlataOO.nameTamplates = nameTamplates;
        ZPlataOO.data = data;
        ZPlataOO.dataElement = dataElement;
        elements1 = elements;
        elements2 = vec;
    }

    public ZPlataOO(Vector dataOtchet, Vector dataElement, Vector day, int smena, double brak, Vector vto) {
        data = dataOtchet;
        ZPlataOO.dataElement = dataElement;
        workDays = day;
        workSmena = smena;
        ZPlataOO.brak = brak;
        elements2 = vto;
    }

    public ZPlataOO(Vector dataOtchet, Vector dataElement, Vector day, int smena, double brak, Vector vto, int idBrig) {
        data = dataOtchet;
        ZPlataOO.dataElement = dataElement;
        workDays = day;
        workSmena = smena;
        ZPlataOO.brak = brak;
        elements2 = vto;
        ZPlataOO.idBrig = idBrig;
    }

    public ZPlataOO(String nameReport, DefaultTableModel tm, Vector vec) {
        tModel = tm;
        elements2 = vec;
        nameTamplates = nameReport;
    }

    public ZPlataOO(String nameReport, DefaultTableModel tm, TableColumnModel tc) {
        tModel = tm;
        tModelCol = tc;
        nameTamplates = nameReport;
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

    public void getReport(ZPlataPDB zpbd, Date sDate, Date eDate, int deparmentId, int brigadeId) {
        List<TechDto> data = zpbd.getTable(sDate, eDate, deparmentId, brigadeId);

        List<String> names = data.stream()
                .map(TechDto::getEmployeeFio)
                .distinct()
                .sorted()
                .collect(Collectors.toList());

        int rows = names.size();

        List<String> tech = data.stream()
                .map(TechDto::getTechName)
                .distinct()
                .sorted()
                .collect(Collectors.toList());

        int colums = tech.size();

        Object[][] matrix = new Object[rows + 2][colums + 2];

        for (int i = 1; i <= names.size(); i++)
            matrix[i][0] = names.get(i - 1);

        for (int i = 1; i <= tech.size(); i++)
            matrix[0][i] = tech.get(i - 1);

        data.forEach(e ->
                matrix[names.indexOf(e.getEmployeeFio()) + 1][tech.indexOf(e.getTechName()) + 1] = e.getWorkTime());

        try {
            connect();
            XComponent currentDocument = openDocumentOld("Templates/report.ots");

            XSpreadsheetDocument xSpreadsheetDocument = UnoRuntime.queryInterface(XSpreadsheetDocument.class, currentDocument);
            XSpreadsheets xSpreadsheets = xSpreadsheetDocument.getSheets();
            Object sheet = xSpreadsheets.getByName("Лист1");
            XSpreadsheet xSpreadsheet = UnoRuntime.queryInterface(XSpreadsheet.class, sheet);
            com.sun.star.beans.XPropertySet xPropSet = null;
            XCell xCell = null;

            for (int i = 0; i <= rows + 1; i++) {
                for (int j = 0; j <= colums + 1; j++) {
                    xCell = xSpreadsheet.getCellByPosition(j, i);
                    xCell.setFormula(matrix[i][j] != null ? matrix[i][j].toString() : "");
                }
            }

            char character = (char) ('A' + colums);

            for (int i = 2; i <= rows + 1; i++) {
                xCell = xSpreadsheet.getCellByPosition(colums + 1, i - 1);
                xCell.setFormula("=SUM($B" + i + ":$" + character + i + ")");
            }

            for (int i = 1; i <= colums; i++) {
                char character1 = (char) ('A' + i);
                xCell = xSpreadsheet.getCellByPosition(i, rows + 1);
                xCell.setFormula("=SUM($" + character1 + "2:$" + character1 + (rows + 1) + ")");
            }

            xCell = xSpreadsheet.getCellByPosition(colums + 1, 0);
            xCell.setFormula("Итого по сотруднику:");

            xCell = xSpreadsheet.getCellByPosition(0, rows + 1);
            xCell.setFormula("Итого по оборудованию:");

            xCell = xSpreadsheet.getCellByPosition(0, rows + 3);
            xCell.setFormula("ИТОГО: ");

            int end = rows + 2;

            xCell = xSpreadsheet.getCellByPosition(1, rows + 3);
            xCell.setFormula("=SUM($B" + end + ":$" + character + end + ")");

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void createReport(String nameTamplates) throws Exception {
        try {
            connect();
            XComponent currentDocument = openDocumentOld("Templates/" + nameTamplates);

            switch (nameTamplates) {
                case "DefaultTableBookFormatCheck.ots":
                case "DefaultTableAlbumFormatCheck.ots":
                    fildTableDefaultCheck(currentDocument);
                    break;
                case "ZPlata(T4_NEW).ots":
                case "ZPlata(T4+_NEW).ots":
                    fildVedomostZPlataT4New(currentDocument);
                    break;
                case "ZPlata(T4_NEW_FOR_SEWING_WORKSHOP).ots":
                case "ZPlata(T4+_NEW_FOR_SEWING_WORKSHOP).ots":
                    fildVedomostZPlataT4NewForSewingWorkshop(currentDocument);
                    break;
                case "ZPlata(T4_NEW_BY_PN).ots":
                case "ZPlata(T4+_NEW_BY_PN).ots":
                    fildVedomostZPlataT4NewByPersonalNumver(currentDocument);
                    break;
                case "ZPlata(T4_NEW_BY_PN_FOR_SEWING_WORKSHOP).ots":
                case "ZPlata(T4+_NEW_BY_PN_FOR_SEWING_WORKSHOP).ots":
                    fildVedomostZPlataT4NewByPersonalNumverForSewingWorkshop(currentDocument);
                    break;
                case "ZPlata(listZapuska).ots":
                    fildVedomostZPlataListZapuska(currentDocument);
                    break;
                case "ZPlataDefaultTable.ots":
                    fildZPlataTable(currentDocument);
                    break;
                case "ZPlata(NV).ots":
                    fildVedomostZPlataNV(currentDocument);
                    break;
                case "ZPlata(NV_EMPL).ots":
                    fildVedomostZPlataNVEmpl(currentDocument);
                    break;
                case "ZPlata(NV_EMPL_KR).ots":
                    fildVedomostZPlataNVEmplKr(currentDocument);
                    break;
                case "ZPlata(NV_MODEL).ots":
                    fildVedomostZPlataNVModel(currentDocument);
                    break;
                case "ZPlata(NV_BRIG_REP).ots":
                    createTemplateForNVBrig(currentDocument);
                    break;
                default:
                    JOptionPane.showMessageDialog(null, "Нет такого шаблона", "Нет такого шаблона!!!", JOptionPane.ERROR_MESSAGE);
                    break;
            }

            JOptionPane.showMessageDialog(null, "Отчёт сформирован", "Отчёт сформирован", JOptionPane.INFORMATION_MESSAGE);

        } catch (java.lang.Exception e) {
            System.out.println(e.getMessage());
            throw new Exception("Ошибка при формировании отчёта ", e);
        }
    }

    public ItemT4 openReport(BuhVedomostForm dialog, String nameTemplates, int smena) throws Exception {
        ItemT4 list;
        try {
            connect();
            XComponent currentDocument = openDocument(path);

            dialog.toFront();

            if (nameTemplates.equals(UtilZPlata.OTCHET_T4_NEW) ||
                    nameTemplates.equals(UtilZPlata.OTCHET_T4_PL_NEW)) {

                workSmena = smena;
                list = openVedomostZPlataT4(currentDocument);
            } else
                throw new Exception();

        } catch (java.lang.Exception e) {
            list = new ItemT4();
            System.out.println(e.getMessage());
            throw new Exception("Ошибка просмотра отчёта ", e);
        }
        return list;
    }

    private ItemT4 openVedomostZPlataT4(XComponent currentDocument) throws Exception {
        ItemT4 rezlist = new ItemT4();
        Vector list = new Vector();

        try {

            String nColBV_;
            String nColBVIt_;
            String nColOtD_;
            String nColOtCl_;
            String nColVD_;
            String nColVCl_;
            String nColTD_;
            String nColTCl_;
            String nColSrD_;
            String nColSrCl_;
            String nColUch_;
            String nColDK_;
            String nColNes_;
            String nColOt_;
            String nColB_;
            String nColPr_;
            String nColCO_;
            String nColDm_;
            String nColMk_;
            String nColOsP_;
            String tempSrt;
            String historySrt;

            double vyrabotka;
            double tmpVyrabotka;
            double value;

            int nCol;
            int nColDept;
            int nColBrig;
            int nColPeriod;
            int nRow;
            int nRowBrak;
            int k;

            int nColBV;
            int nColBVIt;
            int nColBVP;
            int nColStoim;
            int nColPercent;
            int nColMinusPercent;
            int nColTVCl;
            int nColTNCl;
            int nColTNVr;
            int nColOtD;
            int nColOtCl;
            int nColXV;
            int nColVD;
            int nColVCl;
            int nColTD;
            int nColTCl;
            int nColSrD;
            int nColSrCl;
            int nColVr;
            int nColUmT;
            int nColUvT;
            int nColUch;
            int nColDK;
            int nColNes;
            int nColT_D;
            int nColT_Cl;
            int nColO;
            int nColB;
            int nColPr;
            int nColO_;
            int nColDm;
            int nColMk;
            int nColOsP;

            int nColAll;
            int nRowEmpl;
            int nRowBrig;

            int allCl;
            int allD;
            int allVD;
            int allVCl;
            int allTD;
            int allTCl;
            int allVrCl;
            int allT_D;
            int allT_Cl;
            int allSrD;
            int allSrCl;
            int allOD;
            int allBD;
            int allPrD;
            int allO_D;
            int allUch;
            int allDK;
            int allNes;
            int allDm;
            int allMk;
            int allOsP;
            int allUbz;

            int tmp;

            String dataDept;
            String dataBrig;
            String dataDate;

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

            nCol = 4;
            nRow = 7;
            k = 0;

            nColDept = 4;
            nColBrig = 14;
            nColPeriod = 19;

            if (workSmena == 1) {
                nColBV_ = "AG";
                nColBVIt_ = "AH";
                nColOtD_ = "AN";
                nColOtCl_ = "AO";
                nColVD_ = "AQ";
                nColVCl_ = "AR";
                nColTD_ = "AS";
                nColTCl_ = "AT";
                nColSrD_ = "AU";
                nColSrCl_ = "AV";
                nColUch_ = "AZ";
                nColDK_ = "BA";
                nColNes_ = "BB";
                nColOt_ = "BE";
                nColB_ = "BF";
                nColPr_ = "BG";
                nColCO_ = "BH";
                nColDm_ = "BI";
                nColMk_ = "BJ";
                nColOsP_ = "BK";

                nColBV = 32;
                nColBVIt = 33;
                nColBVP = 34;
                nColStoim = 35;
                nColTVCl = 36;
                nColTNCl = 37;
                nColTNVr = 38;
                nColOtD = 39;
                nColOtCl = 40;
                nColXV = 41;
                nColVD = 42;
                nColVCl = 43;
                nColTD = 44;
                nColTCl = 45;
                nColSrD = 46;
                nColSrCl = 47;
                nColVr = 48;
                nColUmT = 49;
                nColUvT = 50;
                nColUch = 51;
                nColDK = 52;
                nColNes = 53;
                nColT_D = 54;
                nColT_Cl = 55;
                nColO = 56;
                nColB = 57;
                nColPr = 58;
                nColO_ = 59;
                nColDm = 60;
                nColMk = 61;
                nColOsP = 62;
                nColPercent = 63;
                nColMinusPercent = 64;
                nColAll = 65;

            } else {
                nColBV_ = "CK";
                nColBVIt_ = "CL";
                nColOtD_ = "CR";
                nColOtCl_ = "CS";
                nColVD_ = "CU";
                nColVCl_ = "CV";
                nColTD_ = "CW";
                nColTCl_ = "CX";
                nColSrD_ = "CY";
                nColSrCl_ = "CZ";
                nColUch_ = "DD";
                nColDK_ = "DE";
                nColNes_ = "DF";
                nColOt_ = "DI";
                nColB_ = "DJ";
                nColPr_ = "DK";
                nColCO_ = "DL";
                nColDm_ = "DM";
                nColMk_ = "DN";
                nColOsP_ = "DO";

                nColBV = 88;
                nColBVIt = 89;
                nColBVP = 90;
                nColStoim = 91;
                nColTVCl = 92;
                nColTNCl = 93;
                nColTNVr = 94;
                nColOtD = 95;
                nColOtCl = 96;
                nColXV = 97;
                nColVD = 98;
                nColVCl = 99;
                nColTD = 100;
                nColTCl = 101;
                nColSrD = 102;
                nColSrCl = 103;
                nColVr = 104;
                nColUmT = 105;
                nColUvT = 106;
                nColUch = 107;
                nColDK = 108;
                nColNes = 109;
                nColT_D = 110;
                nColT_Cl = 111;
                nColO = 112;
                nColB = 113;
                nColPr = 114;
                nColO_ = 115;
                nColDm = 116;
                nColMk = 117;
                nColOsP = 118;
                nColPercent = 119;
                nColMinusPercent = 120;
                nColAll = 121;
            }

            int i = 0;
            int[] num = {0, 1, 2, 3,
                    nColBV,
                    nColBVIt,
                    nColBVP,
                    nColStoim,
                    nColTVCl,
                    nColTNCl,
                    nColTNVr,
                    nColOtD,
                    nColOtCl,
                    nColXV,
                    nColVD,
                    nColVCl,
                    nColTD,
                    nColTCl,
                    nColSrD,
                    nColSrCl,
                    nColVr,
                    nColUmT,
                    nColUvT,
                    nColUch,
                    nColDK,
                    nColNes,
                    nColT_D,
                    nColT_Cl,
                    nColO,
                    nColB,
                    nColPr,
                    nColO_,
                    nColDm,
                    nColMk,
                    nColOsP,
                    nColPercent,
                    nColMinusPercent,
                    nColAll

            };

            xCell = xSpreadsheet.getCellByPosition(nColDept, 4);
            rezlist.setDept(xCell.getFormula().trim());

            xCell = xSpreadsheet.getCellByPosition(nColBrig, 4);
            rezlist.setBrig(xCell.getFormula().trim());

            xCell = xSpreadsheet.getCellByPosition(nColPeriod, 4);
            rezlist.setPeriod(xCell.getFormula().trim().replace("период", ""));

            rezlist.setWorkSmen(new Vector());
            rezlist.setWorkClock(new Vector());
            rezlist.setWorkDay(new Vector());

            for (nRow = 11; nRow < 150; nRow++) {
                nCol = 0;

                xCell = xSpreadsheet.getCellByPosition(nCol, nRow);
                xCell.getFormula();

                try {

                    xCell = xSpreadsheet.getCellByPosition(nColBV, nRow);

                    if (!xCell.getFormula().equals("")) {
                        i = 0;
                        xCell = xSpreadsheet.getCellByPosition(num[i++], nRow);

                        if (!xCell.getFormula().equals("")) {
                            k = 0;

                            ItemBuh blist = new ItemBuh();

                            blist.setTabelNum(xCell.getFormula());

                            xCell = xSpreadsheet.getCellByPosition(num[i++], nRow);
                            blist.setFio(xCell.getFormula());

                            xCell = xSpreadsheet.getCellByPosition(num[i++], nRow);
                            blist.setProf(xCell.getFormula());

                            xCell = xSpreadsheet.getCellByPosition(num[i++], nRow);
                            blist.setRazryad((int) xCell.getValue());

                            blist.setDataTabel(new Vector());

                            xCell = xSpreadsheet.getCellByPosition(num[i++], nRow);
                            blist.setVyrBrig(BigDecimal.valueOf(xCell.getValue()).setScale(2, RoundingMode.HALF_UP).doubleValue());

                            xCell = xSpreadsheet.getCellByPosition(num[i++], nRow);
                            blist.setVyrItog(BigDecimal.valueOf(xCell.getValue()).setScale(2, RoundingMode.HALF_UP).doubleValue());

                            xCell = xSpreadsheet.getCellByPosition(num[i++], nRow);
                            blist.setProcent(BigDecimal.valueOf(xCell.getValue() * 100).setScale(1, RoundingMode.HALF_UP).doubleValue());

                            System.out.println(xCell.getValue() + " " + xCell.getFormula() + " " + xCell.getType());

                            xCell = xSpreadsheet.getCellByPosition(num[i++], nRow);
                            blist.setStoim(BigDecimal.valueOf(xCell.getValue()).setScale(2, RoundingMode.HALF_UP).doubleValue());

                            xCell = xSpreadsheet.getCellByPosition(num[i++], nRow);
                            blist.setClVech(BigDecimal.valueOf(xCell.getValue()).setScale(2, RoundingMode.HALF_UP).doubleValue());

                            xCell = xSpreadsheet.getCellByPosition(num[i++], nRow);
                            blist.setdNoch((int) xCell.getValue());

                            xCell = xSpreadsheet.getCellByPosition(num[i++], nRow);
                            blist.setClNoch(BigDecimal.valueOf(xCell.getValue()).setScale(2, RoundingMode.HALF_UP).doubleValue());

                            xCell = xSpreadsheet.getCellByPosition(num[i++], nRow);
                            blist.setdOtrab((int) xCell.getValue());

                            xCell = xSpreadsheet.getCellByPosition(num[i++], nRow);
                            blist.setClOtrab(BigDecimal.valueOf(xCell.getValue()).setScale(2, RoundingMode.HALF_UP).doubleValue());

                            xCell = xSpreadsheet.getCellByPosition(num[i++], nRow);
                            blist.setVyrX2(BigDecimal.valueOf(xCell.getValue()).setScale(2, RoundingMode.HALF_UP).doubleValue());

                            xCell = xSpreadsheet.getCellByPosition(num[i++], nRow);
                            blist.setdVnedr((int) xCell.getValue());

                            xCell = xSpreadsheet.getCellByPosition(num[i++], nRow);
                            blist.setClVnedr(BigDecimal.valueOf(xCell.getValue()).setScale(2, RoundingMode.HALF_UP).doubleValue());

                            xCell = xSpreadsheet.getCellByPosition(num[i++], nRow);
                            blist.setdTarif((int) xCell.getValue());

                            xCell = xSpreadsheet.getCellByPosition(num[i++], nRow);
                            blist.setClTarif(BigDecimal.valueOf(xCell.getValue()).setScale(2, RoundingMode.HALF_UP).doubleValue());

                            xCell = xSpreadsheet.getCellByPosition(num[i++], nRow);
                            blist.setdSredn((int) xCell.getValue());

                            xCell = xSpreadsheet.getCellByPosition(num[i++], nRow);
                            blist.setClSredn(BigDecimal.valueOf(xCell.getValue()).setScale(2, RoundingMode.HALF_UP).doubleValue());

                            xCell = xSpreadsheet.getCellByPosition(num[i++], nRow);
                            blist.setVrednost(BigDecimal.valueOf(xCell.getValue()).setScale(2, RoundingMode.HALF_UP).doubleValue());

                            xCell = xSpreadsheet.getCellByPosition(num[i++], nRow);
                            blist.setStoimMinus(BigDecimal.valueOf(xCell.getValue()).setScale(2, RoundingMode.HALF_UP).doubleValue());

                            xCell = xSpreadsheet.getCellByPosition(num[i++], nRow);
                            blist.setStoimPlus(BigDecimal.valueOf(xCell.getValue()).setScale(2, RoundingMode.HALF_UP).doubleValue());

                            xCell = xSpreadsheet.getCellByPosition(num[i++], nRow);
                            blist.setKofUch(BigDecimal.valueOf(xCell.getValue()).setScale(2, RoundingMode.HALF_UP).doubleValue());

                            xCell = xSpreadsheet.getCellByPosition(num[i++], nRow);
                            blist.setVyrDK(BigDecimal.valueOf(xCell.getValue()).setScale(2, RoundingMode.HALF_UP).doubleValue());

                            xCell = xSpreadsheet.getCellByPosition(num[i++], nRow);
                            blist.setNesort(BigDecimal.valueOf(xCell.getValue()).setScale(2, RoundingMode.HALF_UP).doubleValue());

                            xCell = xSpreadsheet.getCellByPosition(num[i++], nRow);
                            blist.setdTarif23((int) xCell.getValue());

                            xCell = xSpreadsheet.getCellByPosition(num[i++], nRow);
                            blist.setClTarif23(BigDecimal.valueOf(xCell.getValue()).setScale(2, RoundingMode.HALF_UP).doubleValue());

                            xCell = xSpreadsheet.getCellByPosition(num[i++], nRow);
                            blist.setdOtpusk((int) xCell.getValue());

                            xCell = xSpreadsheet.getCellByPosition(num[i++], nRow);
                            blist.setdBList((int) xCell.getValue());

                            xCell = xSpreadsheet.getCellByPosition(num[i++], nRow);
                            blist.setdPerehod((int) xCell.getValue());

                            xCell = xSpreadsheet.getCellByPosition(num[i++], nRow);
                            blist.setdSOtpusk((int) xCell.getValue());

                            xCell = xSpreadsheet.getCellByPosition(num[i++], nRow);
                            blist.setdDMateri((int) xCell.getValue());

                            xCell = xSpreadsheet.getCellByPosition(num[i++], nRow);
                            blist.setdMk((int) xCell.getValue());

                            xCell = xSpreadsheet.getCellByPosition(num[i++], nRow);
                            blist.setVyrOsvoen(BigDecimal.valueOf(xCell.getValue()).setScale(2, RoundingMode.HALF_UP).doubleValue());

                            xCell = xSpreadsheet.getCellByPosition(num[i++], nRow);
                            blist.setPercent(BigDecimal.valueOf(xCell.getValue() * 100).setScale(1, RoundingMode.HALF_UP).doubleValue());

                            System.out.println(xCell.getValue() + " " + xCell.getFormula() + " " + xCell.getType());

                            xCell = xSpreadsheet.getCellByPosition(num[i], nRow);
                            blist.setMinusPercent(BigDecimal.valueOf(xCell.getValue() * 100).setScale(1, RoundingMode.HALF_UP).doubleValue());

                            list.add(blist);

                        } else {
                            k++;

                            if (k > 10) {
                                break;
                            }
                        }
                    }

                } catch (Exception e) {
                    list = new Vector();
                    System.out.println(e.getMessage());
                    throw new Exception("Отчет распознан с ошибкой!  ", e);
                }
            }

            rezlist.setDataPeople(list);

        } catch (Exception e) {
            rezlist = new ItemT4();
            log.error("Ошибка при чтении отчёта", e);
            throw new Exception("Ошибка при чтении отчёта ", e);
        }
        return rezlist;
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

            nCol = 0;
            int nRow = 2;
            boolean flag = false;

            for (Object row : tModel.getDataVector()) {
                if (((Vector) row).get(0).toString().equals("true")) {
                    flag = true;
                    break;
                }
            }

            for (Object row : tModel.getDataVector()) {
                if (flag) {
                    if (((Vector) row).get(0).toString().equals("true")) {
                        for (int i = 1; i < tModel.getColumnCount(); i++) {
                            if (tModelCol.getColumn(i).getWidth() > 0) {
                                xCell = xSpreadsheet.getCellByPosition(nCol++, nRow);
                                xCell.setFormula(((Vector) row).get(i) != null ? ((Vector) row).get(i).toString() : "");
                                xPropSet = UnoRuntime.queryInterface(XPropertySet.class, xCell);
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
                            xPropSet = UnoRuntime.queryInterface(XPropertySet.class, xCell);
                            xPropSet.setPropertyValue("TableBorder", aBorder);
                        }
                    }
                    nCol = 0;
                    nRow++;
                }
            }
            /*
            for(Object row : tModel.getDataVector()){
                for (int i = 1; i < tModel.getColumnCount(); i++){
                    if(tModelCol.getColumn(i).getWidth()>0){
                        xCell = xSpreadsheet.getCellByPosition(nCol++, nRow);
                        xCell.setFormula(((Vector)row).get(i)!=null?((Vector)row).get(i).toString():"");
                        xPropSet = (com.sun.star.beans.XPropertySet)UnoRuntime.queryInterface( com.sun.star.beans.XPropertySet.class,  xCell);
                        xPropSet.setPropertyValue("TableBorder", aBorder);
                    }
                }
                nCol = 0;
                nRow++;
            }
            */
        } catch (java.lang.Exception e) {
            log.error("Ошибка при заполнении отчёта.", e);
            throw new Exception("Ошибка при заполнении отчёта.", e);
        }
    }

    private void fildVedomostZPlataT4New(XComponent currentDocument) throws Exception {
        try {

            String nColBV_;
            String nColBVIt_;
            String nColOtD_;
            String nColOtCl_;
            String nColVD_;
            String nColVCl_;
            String nColTD_;
            String nColTCl_;
            String nColSrD_;
            String nColSrCl_;
            String nColUch_;
            String nColDK_;
            String nColNes_;
            String nColOt_;
            String nColB_;
            String nColPr_;
            String nColCO_;
            String nColDm_;
            String nColMk_;
            String nColOsP_;
            String tempSrt;
            String historySrt;

            double vyrabotka;
            double tmpVyrabotka;
            double value;

            int nCol;
            int nColDept;
            int nColBrig;
            int nColPeriod;
            int nRow;
            int nRowBrak;
            int k;

            int nColBV;
            int nColBVIt;
            int nColBVP;
            int nColStoim;
            int nColTVCl;
            int nColTNCl;
            int nColTNVr;
            int nColOtD;
            int nColOtCl;
            int nColXV;
            int nColVD;
            int nColVCl;
            int nColTD;
            int nColTCl;
            int nColSrD;
            int nColSrCl;
            int nColVr;
            int nColUmT;
            int nColUvT;
            int nColUch;
            int nColDK;
            int nColNes;
            int nColT_D;
            int nColT_Cl;
            int nColO;
            int nColB;
            int nColPr;
            int nColO_;
            int nColDm;
            int nColMk;
            int nColOsP;

            int nColAll;
            int nRowEmpl;
            int nRowBrig;

            int allCl;
            int allD;
            int allVD;
            int allVCl;
            int allTD;
            int allTCl;
            int allVrCl;
            int allT_D;
            int allT_Cl;
            int allSrD;
            int allSrCl;
            int allOD;
            int allBD;
            int allPrD;
            int allO_D;
            int allUch;
            int allDK;
            int allNes;
            int allDm;
            int allMk;
            int allZ;

            int tmp;

            Vector dataPeredanoDK;
            Vector dataPeredanoDKVTO;
            Vector colData;

            XSpreadsheetDocument xSpreadsheetDocument = UnoRuntime.queryInterface(XSpreadsheetDocument.class, currentDocument);
            XSpreadsheets xSpreadsheets = xSpreadsheetDocument.getSheets();
            Object sheet = xSpreadsheets.getByName("Лист1");
            XSpreadsheet xSpreadsheet = UnoRuntime.queryInterface(XSpreadsheet.class, sheet);
            Object sheet_2 = xSpreadsheets.getByName("Лист2");
            XSpreadsheet xSpreadsheet_2 = UnoRuntime.queryInterface(XSpreadsheet.class, sheet_2);
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

            //------------------------ Лист 2 ----------------------------
            //---------------- Ставка 1-го разряда -----------------------

            xCell = xSpreadsheet_2.getCellByPosition(4, 1);
            xCell.setValue(UtilZPlata.RATE_1ST_CATEGORY);

            xCell = xSpreadsheet_2.getCellByPosition(4, 16);
            xCell.setFormula(UtilZPlata.STAFF_1);

            xCell = xSpreadsheet_2.getCellByPosition(4, 17);
            xCell.setFormula(UtilZPlata.STAFF_2);

            //------------------------ Лист 1 ----------------------------
            //---------------- Заполнение шапки документа ----------------

            nColDept = 4;
            nColBrig = 14;
            nColPeriod = 19;

            xCell = xSpreadsheet.getCellByPosition(nColDept, 4);
            xCell.setFormula(data.get(0).toString().toLowerCase());

            xCell = xSpreadsheet.getCellByPosition(nColBrig, 4);
            xCell.setFormula(data.get(1).toString().toLowerCase());

            xCell = xSpreadsheet.getCellByPosition(nColPeriod, 4);
            xCell.setFormula(data.get(2).toString().toLowerCase());

            if (workSmena == 1) {
                nColBV_ = "AG";
                nColBVIt_ = "AH";
                nColOtD_ = "AN";
                nColOtCl_ = "AO";
                nColVD_ = "AQ";
                nColVCl_ = "AR";
                nColTD_ = "AS";
                nColTCl_ = "AT";
                nColSrD_ = "AU";
                nColSrCl_ = "AV";
                nColUch_ = "AZ";
                nColDK_ = "BA";
                nColNes_ = "BB";
                nColOt_ = "BE";
                nColB_ = "BF";
                nColPr_ = "BG";
                nColCO_ = "BH";
                nColDm_ = "BI";
                nColMk_ = "BJ";
                nColOsP_ = "BK";

                nColBV = 32;
                nColBVIt = 33;
                nColBVP = 34;
                nColStoim = 35;
                nColTVCl = 36;
                nColTNCl = 37;
                nColTNVr = 38;
                nColOtD = 39;
                nColOtCl = 40;
                nColXV = 41;
                nColVD = 42;
                nColVCl = 43;
                nColTD = 44;
                nColTCl = 45;
                nColSrD = 46;
                nColSrCl = 47;
                nColVr = 48;
                nColUmT = 49;
                nColUvT = 50;
                nColUch = 51;
                nColDK = 52;
                nColNes = 53;
                nColT_D = 54;
                nColT_Cl = 55;
                nColO = 56;
                nColB = 57;
                nColPr = 58;
                nColO_ = 59;
                nColDm = 60;
                nColMk = 61;
                nColOsP = 62;

                nColAll = 64;
            } else {
                for (int i = 0; i < 2; i++) {
                    nColDept += 28;
                    nColBrig += 28;
                    nColPeriod += 28;

                    xCell = xSpreadsheet.getCellByPosition(nColDept, 4);
                    xCell.setFormula(data.get(0).toString().toLowerCase());

                    xCell = xSpreadsheet.getCellByPosition(nColBrig, 4);
                    xCell.setFormula(data.get(1).toString().toLowerCase());

                    xCell = xSpreadsheet.getCellByPosition(nColPeriod, 4);
                    xCell.setFormula(data.get(2).toString().toLowerCase());
                }

                nColBV_ = "CK";
                nColBVIt_ = "CL";
                nColOtD_ = "CR";
                nColOtCl_ = "CS";
                nColVD_ = "CU";
                nColVCl_ = "CV";
                nColTD_ = "CW";
                nColTCl_ = "CX";
                nColSrD_ = "CY";
                nColSrCl_ = "CZ";
                nColUch_ = "DD";
                nColDK_ = "DE";
                nColNes_ = "DF";
                nColOt_ = "DI";
                nColB_ = "DJ";
                nColPr_ = "DK";
                nColCO_ = "DL";
                nColDm_ = "DM";
                nColMk_ = "DN";
                nColOsP_ = "DO";

                nColBV = 88;
                nColBVIt = 89;
                nColBVP = 90;
                nColStoim = 91;
                nColTVCl = 92;
                nColTNCl = 93;
                nColTNVr = 94;
                nColOtD = 95;
                nColOtCl = 96;
                nColXV = 97;
                nColVD = 98;
                nColVCl = 99;
                nColTD = 100;
                nColTCl = 101;
                nColSrD = 102;
                nColSrCl = 103;
                nColVr = 104;
                nColUmT = 105;
                nColUvT = 106;
                nColUch = 107;
                nColDK = 108;
                nColNes = 109;
                nColT_D = 110;
                nColT_Cl = 111;
                nColO = 112;
                nColB = 113;
                nColPr = 114;
                nColO_ = 115;
                nColDm = 116;
                nColMk = 117;
                nColOsP = 118;

                nColAll = 120;
            }

            int[] num = {0, 1, 2, 3, nColBV, nColDK, nColNes};

            xCell = xSpreadsheet.getCellByPosition(nColBV, 4);
            xCell.setFormula(data.get(0).toString().toLowerCase());

            xCell = xSpreadsheet.getCellByPosition(nColTNVr, 4);
            xCell.setFormula(data.get(1).toString().toLowerCase());

            xCell = xSpreadsheet.getCellByPosition(nColXV, 4);
            xCell.setFormula(data.get(2).toString().toLowerCase());

            xCell = xSpreadsheet.getCellByPosition(nColUch, 4);
            xCell.setFormula(data.get(0).toString().toLowerCase());

            xCell = xSpreadsheet.getCellByPosition(nColB, 4);
            xCell.setFormula(data.get(1).toString().toLowerCase());

            xCell = xSpreadsheet.getCellByPosition(nColDm, 4);
            xCell.setFormula(data.get(2).toString().toLowerCase());

            //------------------------------Рабочие дни

            nCol = 4;
            nRow = 7;

            for (int i = 0; i < workDays.size(); i++) {
                for (int j = 0; j < workSmena; j++) {
                    xCell = xSpreadsheet.getCellByPosition(nCol, nRow);
                    xCell.setValue(j + 1);

                    xCell = xSpreadsheet.getCellByPosition(nCol, nRow + 1);
                    xCell.setValue(8);

                    xCell = xSpreadsheet.getCellByPosition(nCol++, nRow + 2);
                    xCell.setFormula(String.valueOf(workDays.get(i)));
                }
            }

            //----------------Заполнение тела документа

            //------------------------------Выработка за месяц (бригада, др. конвеера)

            nRow = 11;
            k = 0;

            dataPeredanoDK = new Vector();
            for (Object row : dataElement) {
                if (((Vector) row).get(7).toString().trim().equals(String.valueOf(idBrig))) {
                    k = 0;
                    for (int i = 0; i < nColAll; i++) {
                        xCell = xSpreadsheet.getCellByPosition(i, nRow);
                        xPropSet = UnoRuntime.queryInterface(XPropertySet.class, xCell);
                        xPropSet.setPropertyValue("TableBorder", aBorder);
                        if (k < num.length) {
                            if (i == num[k]) {
                                if (num[k] == nColBV) {
                                    xCell.setValue(Double.parseDouble(UtilZPlata.formatNorm(Double.parseDouble(((Vector) row).get(k).toString()), 2)));
                                } else if (num[k] == nColDK || num[k] == nColNes) {
                                    xCell.setValue(Double.parseDouble(UtilZPlata.formatNorm(Double.parseDouble(((Vector) row).get(k).toString()), 2)));
                                } else if (num[k] == 1) {
                                    xCell.setFormula(((Vector) row).get(k).toString() + " " + ((Vector) row).get(8).toString());
                                } else {
                                    xCell.setFormula(((Vector) row).get(k).toString());
                                }
                                k++;
                            }
                        }
                        if (i == nColBVIt)
                            xCell.setFormula("=IF(" + nColUch_ + (nRow + 1) + ">0;"
                                    + "(" + nColBV_ + (nRow + 1) + "+" + nColDK_ + (nRow + 1) + "+" + nColNes_ + (nRow + 1) + "-IF(" + nColOsP_ + (nRow + 1) + "<>\"\";" + nColOsP_ + (nRow + 1) + ";0))*" + nColUch_ + (nRow + 1) + ";"
                                    + nColBV_ + (nRow + 1) + "+" + nColDK_ + (nRow + 1) + "+" + nColNes_ + (nRow + 1) + "-IF(" + nColOsP_ + (nRow + 1) + "<>\"\";" + nColOsP_ + (nRow + 1) + ";0))");
                        if (i == nColBVP)
                            xCell.setFormula("=IF(" + nColOtCl_ + (nRow + 1) + "=0;\"-\";(" + nColBVIt_ + (nRow + 1) + ")/" + nColOtCl_ + (nRow + 1) + ")");
                        if (i == nColStoim)
                            xCell.setFormula("=ROUND(IF(D" + (nRow + 1) + "=1;ROUND(1*Лист2.E2/169;2)*1.5;"
                                    + "IF(D" + (nRow + 1) + "=2;ROUND(1.16*Лист2.E2/169;2)*1.5;"
                                    + "IF(D" + (nRow + 1) + "=3;ROUND(1.35*Лист2.E2/169;2)*1.5;"
                                    + "IF(D" + (nRow + 1) + "=4;ROUND(1.57*Лист2.E2/169;2)*1.5;"
                                    + "IF(D" + (nRow + 1) + "=5;ROUND(1.73*Лист2.E2/169;2)*1.5;"
                                    + "IF(D" + (nRow + 1) + "=6;ROUND(1.9*Лист2.E2/169;2)*1.5;"
                                    + "IF(D" + (nRow + 1) + "=7;ROUND(2.03*Лист2.E2/169;2)*1.5;\"\")))))));2)");
                        if (i == nColOsP)
                            xCell.setFormula(
                                    "=IF(" + nColBV_ + (nRow + 1) + ">=0;"
                                            + "(IF(C" + (nRow + 1) + "=\"" + UtilZPlata.OTCHET_PROF_OSVOENIE + "\";"
                                            + nColBV_ + (nRow + 1) + "+" + nColDK_ + (nRow + 1) + ";"
                                            + "0))"
                                            + ";\"\")"
                            );
                    }
                    nRow++;
                } else {
                    dataPeredanoDK.add(row);
                }
            }

            nRowBrig = nRow;

            //------------------------------ВТО
            dataPeredanoDKVTO = new Vector();
            if (!elements2.isEmpty()) {
                xCell = xSpreadsheet.getCellByPosition(1, nRow);
                xCell.setFormula("ВТО");
            }
            nRow++;

            for (Object row : elements2) {
                if (((Vector) row).get(7).toString().trim().equals(String.valueOf(idBrig))) {
                    k = 0;
                    for (int i = 0; i < nColAll; i++) {
                        xCell = xSpreadsheet.getCellByPosition(i, nRow);
                        xPropSet = UnoRuntime.queryInterface(XPropertySet.class, xCell);
                        xPropSet.setPropertyValue("TableBorder", aBorder);
                        if (k < num.length) {
                            if (i == num[k]) {
                                if (num[k] == nColBV) {
                                    xCell.setValue(Double.parseDouble(UtilZPlata.formatNorm(Double.parseDouble(((Vector) row).get(k).toString()), 2)));
                                } else if (num[k] == nColDK || num[k] == nColNes) {
                                    xCell.setValue(Double.parseDouble(UtilZPlata.formatNorm(Double.parseDouble(((Vector) row).get(k).toString()), 2)));
                                } else if (num[k] == 1) {
                                    xCell.setFormula(((Vector) row).get(k).toString() + " " + ((Vector) row).get(8).toString());
                                } else {
                                    xCell.setFormula(((Vector) row).get(k).toString());
                                }
                                k++;
                            }
                        }
                        if (i == nColBVIt)
                            xCell.setFormula("=IF(" + nColUch_ + (nRow + 1) + ">0;"
                                    + "(" + nColBV_ + (nRow + 1) + "+" + nColDK_ + (nRow + 1) + "+" + nColNes_ + (nRow + 1) + "-IF(" + nColOsP_ + (nRow + 1) + "<>\"\";" + nColOsP_ + (nRow + 1) + ";0))*" + nColUch_ + (nRow + 1) + ";"
                                    + nColBV_ + (nRow + 1) + "+" + nColDK_ + (nRow + 1) + "+" + nColNes_ + (nRow + 1) + "-IF(" + nColOsP_ + (nRow + 1) + "<>\"\";" + nColOsP_ + (nRow + 1) + ";0))");
                        if (i == nColBVP)
                            xCell.setFormula("=IF(" + nColOtCl_ + (nRow + 1) + "=0;\"-\";(" + nColBVIt_ + (nRow + 1) + ")/" + nColOtCl_ + (nRow + 1) + ")");
                        if (i == nColStoim)
                            xCell.setFormula("=ROUND(IF(D" + (nRow + 1) + "=1;ROUND(1*Лист2.E2/169;2)*1.5;"
                                    + "IF(D" + (nRow + 1) + "=2;ROUND(1.16*Лист2.E2/169;2)*1.5;"
                                    + "IF(D" + (nRow + 1) + "=3;ROUND(1.35*Лист2.E2/169;2)*1.5;"
                                    + "IF(D" + (nRow + 1) + "=4;ROUND(1.57*Лист2.E2/169;2)*1.5;"
                                    + "IF(D" + (nRow + 1) + "=5;ROUND(1.73*Лист2.E2/169;2)*1.5;"
                                    + "IF(D" + (nRow + 1) + "=6;ROUND(1.9*Лист2.E2/169;2)*1.5;"
                                    + "IF(D" + (nRow + 1) + "=7;ROUND(2.03*Лист2.E2/169;2)*1.5;\"\")))))));2)");
                        if (i == nColOsP)
                            xCell.setFormula(
                                    "=IF(" + nColBV_ + (nRow + 1) + ">=0;"
                                            + "(IF(C" + (nRow + 1) + "=\"" + UtilZPlata.OTCHET_PROF_OSVOENIE + "\";"
                                            + nColBV_ + (nRow + 1) + "+" + nColDK_ + (nRow + 1) + ";"
                                            + "0))"
                                            + ";\"\")"
                            );
                    }
                    nRow++;
                } else {
                    dataPeredanoDKVTO.add(row);
                }
            }

            //------------------------------Передано на др. конвейера

            if (dataPeredanoDKVTO.size() > 0) {
                int nRowDKVTO = nRow;
                nRow++;

                for (Object row : dataPeredanoDKVTO) {
                    k = 0;
                    for (int i = 0; i < nColAll; i++) {
                        xCell = xSpreadsheet.getCellByPosition(i, nRow);
                        xPropSet = UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
                        xPropSet.setPropertyValue("TableBorder", aBorder);
                        if (k < num.length) {
                            if (i == num[k]) {
                                if (num[k] == nColBV) {
                                    xCell.setValue(Double.parseDouble(UtilZPlata.formatNorm(Double.parseDouble(((Vector) row).get(k).toString()), 2)));
                                } else if (num[k] == nColDK) {
                                    xCell.setValue(0);
                                } else if (num[k] == nColNes) {
                                    xCell.setValue(0);
                                } else if (num[k] == 1) {
                                    xCell.setFormula("(" + ((Vector) row).get(7).toString() + ")" + ((Vector) row).get(k).toString());
                                } else {
                                    xCell.setFormula(((Vector) row).get(k).toString());
                                }
                                k++;
                            }
                        }
                        if (i == nColBVIt)
                            xCell.setFormula("=" + nColBV_ + (nRow + 1) + "+" + nColDK_ + (nRow + 1) + "+" + nColNes_ + (nRow + 1) + "-IF(" + nColOsP_ + (nRow + 1) + "<>\"\";" + nColOsP_ + (nRow + 1) + ";0)");
                    }
                    nRow++;
                }

                if (nRowDKVTO + 1 != nRow) {
                    xCell = xSpreadsheet.getCellByPosition(1, nRowDKVTO);
                    xCell.setFormula("Передано на ДК ВТО");
                }

            }

            if (!dataPeredanoDK.isEmpty()) {
                int nRowDK = nRow;
                nRow++;

                for (Object row : dataPeredanoDK) {
                    if (Double.parseDouble(UtilZPlata.formatNorm(Double.parseDouble(((Vector) row).get(4).toString()), 2)) != 0) {
                        k = 0;
                        for (int i = 0; i < nColAll; i++) {
                            xCell = xSpreadsheet.getCellByPosition(i, nRow);
                            xPropSet = UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
                            xPropSet.setPropertyValue("TableBorder", aBorder);
                            if (k < num.length) {
                                if (i == num[k]) {
                                    if (num[k] == nColBV) {
                                        xCell.setValue(Double.parseDouble(UtilZPlata.formatNorm(Double.valueOf(((Vector) row).get(k).toString()), 2)));
                                    } else if (num[k] == nColDK) {
                                        xCell.setValue(0);
                                    } else if (num[k] == nColNes) {
                                        xCell.setValue(0);
                                    } else if (num[k] == 1) {
                                        xCell.setFormula("(" + ((Vector) row).get(7).toString() + ")" + ((Vector) row).get(k).toString());
                                    } else {
                                        xCell.setFormula(((Vector) row).get(k).toString());
                                    }
                                    k++;
                                }
                            }
                            if (i == nColBVIt)
                                xCell.setFormula("=" + nColBV_ + (nRow + 1) + "+" + nColDK_ + (nRow + 1) + "+" + nColNes_ + (nRow + 1) + "-IF(" + nColOsP_ + (nRow + 1) + "<>\"\";" + nColOsP_ + (nRow + 1) + ";0)");
                        }
                        nRow++;
                    }
                }

                if (nRowDK + 1 != nRow) {
                    xCell = xSpreadsheet.getCellByPosition(1, nRowDK);
                    xCell.setFormula("Передано на ДК");
                }
            }

            nRowBrak = nRow;

            //------------------------------Табель
            if (JOptionPane.showOptionDialog(null, "Заполните все прогулы, б/листы, отпуска и т.д.\n"
                            + "Далее нажмите кнопку 'Продолжить' для завершения\n"
                            + "формирования отчёта. ", "Внимание!",
                    JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, null,
                    new Object[]{"Продолжить", "Отмена"}, "Продолжить") == JOptionPane.YES_OPTION) {

                nRow = 10;
                nRowEmpl = 11;
                historySrt = "";
                for (int l = 0; l < dataElement.size() + elements2.size() + 1 - dataPeredanoDK.size() - dataPeredanoDKVTO.size(); l++) {
                    nRow++;

                    tempSrt = "";
                    vyrabotka = 0;

                    allCl = 0;
                    allD = 0;
                    allVD = 0;
                    allVCl = 0;
                    allTD = 0;
                    allTCl = 0;
                    allVrCl = 0;
                    allT_D = 0;
                    allT_Cl = 0;
                    allSrD = 0;
                    allSrCl = 0;
                    allOD = 0;
                    allBD = 0;
                    allPrD = 0;
                    allO_D = 0;
                    allUch = 0;
                    allDK = 0;
                    allNes = 0;
                    allDm = 0;
                    allMk = 0;
                    allZ = 0;

                    //------------------------------запоминаем колонки которые рабочие дни
                    //-и подсчитывает кол-во б/лист, отпуск, свой счёт,средний, 2/3 тарифа

                    colData = new Vector();
                    for (int i = 4; i < nColBV; i++) {
                        xCell = xSpreadsheet.getCellByPosition(i, 9);
                        if (!xCell.getFormula().equals("")) {
                            xCell = xSpreadsheet.getCellByPosition(i, nRow);
                            if (xCell.getFormula().equals("")) {
                                colData.add(i);

                                xCell = xSpreadsheet.getCellByPosition(i, 8);
                                allCl += xCell.getValue();
                                allD += 1;

                            } else if (xCell.getFormula().toLowerCase().trim().equals("б")) {
                                allBD += 1;
                            } else if (xCell.getFormula().toLowerCase().trim().equals("о") || xCell.getFormula().toLowerCase().trim().equals("o")) {
                                allOD += 1;
                            } else if (xCell.getFormula().toLowerCase().trim().equals("со") || xCell.getFormula().toLowerCase().trim().equals("co") ||
                                    xCell.getFormula().toLowerCase().trim().equals("а") || xCell.getFormula().toLowerCase().trim().equals("a") ||
                                    xCell.getFormula().toLowerCase().trim().equals("у") || xCell.getFormula().toLowerCase().trim().equals("y") ||
                                    xCell.getFormula().toLowerCase().trim().equals("св") || xCell.getFormula().toLowerCase().trim().equals("cb")) {
                                allO_D += 1;
                            } else if (xCell.getFormula().toLowerCase().trim().equals("п")) {
                                allPrD += 1;
                            } else if (xCell.getFormula().toLowerCase().trim().equals("в") || xCell.getFormula().toLowerCase().trim().equals("b")) {
                                allVD += 1;
                                xCell = xSpreadsheet.getCellByPosition(i, 8);
                                allVCl += xCell.getValue();
                            } else if (xCell.getFormula().toLowerCase().trim().equals("т") || xCell.getFormula().toLowerCase().trim().equals("t")) {
                                allTD += 1;
                                xCell = xSpreadsheet.getCellByPosition(i, 8);
                                allTCl += xCell.getValue();

                                xCell = xSpreadsheet.getCellByPosition(i, 7);
                                if (xCell.getFormula().equals("2") ||
                                        xCell.getFormula().toLowerCase().trim().equals("2х") ||
                                        xCell.getFormula().toLowerCase().trim().equals("2x")) {
                                    xCell = xSpreadsheet.getCellByPosition(nColTVCl, nRow);
                                    xCell.setValue(xCell.getValue() + 1.5);
                                }

                            } else if (xCell.getFormula().toLowerCase().trim().equals("т*") || xCell.getFormula().toLowerCase().trim().equals("t*")) {
                                allT_D += 1;
                                xCell = xSpreadsheet.getCellByPosition(i, 8);
                                allT_Cl += xCell.getValue();
                            } else if (xCell.getFormula().toLowerCase().trim().equals("ср") || xCell.getFormula().toLowerCase().trim().equals("cp")) {
                                allSrD += 1;
                                xCell = xSpreadsheet.getCellByPosition(i, 8);
                                allSrCl += xCell.getValue();
                            } else if (xCell.getFormula().toLowerCase().trim().equals("вр") || xCell.getFormula().toLowerCase().trim().equals("bp")) {
                                xCell = xSpreadsheet.getCellByPosition(i, 8);
                                allVrCl += xCell.getValue();
                            } else if (xCell.getFormula().toLowerCase().trim().equals("дм") || xCell.getFormula().toLowerCase().trim().equals("dm")) {
                                allDm += 1;
                            } else if (xCell.getFormula().toLowerCase().trim().equals("мк") || xCell.getFormula().toLowerCase().trim().equals("mk")) {
                                allMk += 1;
                            }
                        }
                    }

                    //------------------------------выработка по рабочим дням

                    xCell = xSpreadsheet.getCellByPosition(nColBVIt, nRow);
                    if (!xCell.getFormula().equals("")) {
                        vyrabotka = xCell.getValue();
                        tmpVyrabotka = 0;

                        for (int i = 0; i < colData.size() - 1; i++) {
                            tmp = 0;

                            xCell = xSpreadsheet.getCellByPosition(Integer.valueOf(colData.get(i).toString()), nRow);
                            xCell.setValue(Double.valueOf(UtilZPlata.formatNorm(Double.valueOf(vyrabotka / colData.size()), 2)));
                            tmpVyrabotka += Double.valueOf(UtilZPlata.formatNorm(Double.valueOf(vyrabotka / colData.size()), 2));

                            xCell = xSpreadsheet.getCellByPosition(Integer.valueOf(colData.get(i).toString()), 7);
                            if (xCell.getFormula().equals("2") || xCell.getFormula().toLowerCase().trim().equals("2х") || xCell.getFormula().toLowerCase().trim().equals("2x")) {
                                xCell = xSpreadsheet.getCellByPosition(nColTVCl, nRow);
                                xCell.setValue(xCell.getValue() + 1.5);

                            } else if (xCell.getFormula().equals("3")) {
                                xCell = xSpreadsheet.getCellByPosition(Integer.valueOf(colData.get(i).toString()), 8);
                                tmp += xCell.getValue();

                                xCell = xSpreadsheet.getCellByPosition(nColTNCl, nRow);
                                xCell.setValue(xCell.getValue() + tmp);

                                xCell = xSpreadsheet.getCellByPosition(nColTNVr, nRow);
                                xCell.setValue(xCell.getValue() + Double.valueOf(UtilZPlata.formatNorm(Double.valueOf(vyrabotka / colData.size()), 2)));

                            }

                            xCell = xSpreadsheet.getCellByPosition(Integer.valueOf(colData.get(i).toString()), 7);
                            if (xCell.getFormula().toLowerCase().trim().equals("х") || xCell.getFormula().toLowerCase().trim().equals("x") ||
                                    xCell.getFormula().toLowerCase().trim().equals("1х") || xCell.getFormula().toLowerCase().trim().equals("1x") ||
                                    xCell.getFormula().toLowerCase().trim().equals("2х") || xCell.getFormula().toLowerCase().trim().equals("2x")) {
                                xCell = xSpreadsheet.getCellByPosition(nColXV, nRow);
                                xCell.setValue(xCell.getValue() + Double.valueOf(UtilZPlata.formatNorm(Double.valueOf(vyrabotka / colData.size()), 2)));
                            }
                        }

                        tmp = 0;
                        value = 0;

                        xCell = xSpreadsheet.getCellByPosition(nColBVIt, nRow);
                        value = xCell.getValue();

                        if (colData.size() > 0) {
                            xCell = xSpreadsheet.getCellByPosition(Integer.valueOf(colData.get(colData.size() - 1).toString()), nRow);
                            xCell.setValue(Double.valueOf(UtilZPlata.formatNorm(value - tmpVyrabotka, 2)));

                            xCell = xSpreadsheet.getCellByPosition(Integer.valueOf(colData.get(colData.size() - 1).toString()), 7);
                            if (xCell.getFormula().equals("2") || xCell.getFormula().toLowerCase().trim().equals("2х") || xCell.getFormula().toLowerCase().trim().equals("2x")) {
                                xCell = xSpreadsheet.getCellByPosition(nColTVCl, nRow);
                                xCell.setValue(xCell.getValue() + 1.5);

                            } else if (xCell.getFormula().equals("3")) {
                                xCell = xSpreadsheet.getCellByPosition(Integer.valueOf(colData.get(colData.size() - 1).toString()), 8);
                                tmp += xCell.getValue();

                                xCell = xSpreadsheet.getCellByPosition(nColTNCl, nRow);
                                xCell.setValue(xCell.getValue() + tmp);

                                xCell = xSpreadsheet.getCellByPosition(nColTNVr, nRow);
                                xCell.setValue(xCell.getValue() + Double.valueOf(UtilZPlata.formatNorm(value - tmpVyrabotka, 2)));

                            }

                            xCell = xSpreadsheet.getCellByPosition(Integer.valueOf(colData.get(colData.size() - 1).toString()), 7);
                            if (xCell.getFormula().toLowerCase().trim().equals("х") || xCell.getFormula().toLowerCase().trim().equals("x") ||
                                    xCell.getFormula().toLowerCase().trim().equals("1х") || xCell.getFormula().toLowerCase().trim().equals("1x") ||
                                    xCell.getFormula().toLowerCase().trim().equals("2х") || xCell.getFormula().toLowerCase().trim().equals("2x")) {
                                xCell = xSpreadsheet.getCellByPosition(nColXV, nRow);
                                xCell.setValue(xCell.getValue() + Double.valueOf(UtilZPlata.formatNorm(value - tmpVyrabotka, 2)));
                            }
                        }

                        //------------------------------отработанное время

                        xCell = xSpreadsheet.getCellByPosition(nColOtD, nRow);
                        xCell.setValue(allD);

                        xCell = xSpreadsheet.getCellByPosition(nColOtCl, nRow);
                        xCell.setValue(allCl);

                        //------------------------------Внедрение

                        xCell = xSpreadsheet.getCellByPosition(nColVD, nRow);
                        xCell.setValue(allVD);

                        xCell = xSpreadsheet.getCellByPosition(nColVCl, nRow);
                        xCell.setValue(allVCl);

                        //------------------------------Тариф

                        xCell = xSpreadsheet.getCellByPosition(nColTD, nRow);
                        xCell.setValue(allTD);

                        xCell = xSpreadsheet.getCellByPosition(nColTCl, nRow);
                        xCell.setValue(allTCl);


                        //------------------------------Средний

                        xCell = xSpreadsheet.getCellByPosition(nColSrD, nRow);
                        xCell.setFormula("=IF(C" + (nRow + 1) + "=\"" + UtilZPlata.OTCHET_PROF_OSVOENIE + "\";" + nColOtD_ + (nRow + 1) + ";" + (allSrD > 0 ? allSrD : "\"\"") + ")");
                        //xCell.setValue(allSrD);

                        xCell = xSpreadsheet.getCellByPosition(nColSrCl, nRow);
                        xCell.setFormula("=IF(C" + (nRow + 1) + "=\"" + UtilZPlata.OTCHET_PROF_OSVOENIE + "\";" + nColOtCl_ + (nRow + 1) + ";" + (allSrCl > 0 ? allSrCl : "\"\"") + ")");
                        //xCell.setValue(allSrCl);

                        //------------------------------Вредность

                        xCell = xSpreadsheet.getCellByPosition(nColVr, nRow);
                        xCell.setValue(allVrCl);

                        //------------------------------2/3 тарифа

                        xCell = xSpreadsheet.getCellByPosition(nColT_D, nRow);
                        xCell.setValue(allT_D);

                        xCell = xSpreadsheet.getCellByPosition(nColT_Cl, nRow);
                        xCell.setValue(allT_Cl);

                        //------------------------------Отпуск

                        xCell = xSpreadsheet.getCellByPosition(nColO, nRow);
                        xCell.setValue(allOD);

                        //------------------------------Б/лист

                        xCell = xSpreadsheet.getCellByPosition(nColB, nRow);
                        xCell.setValue(allBD);

                        //------------------------------Перевод

                        xCell = xSpreadsheet.getCellByPosition(nColPr, nRow);
                        xCell.setValue(allPrD);

                        //------------------------------Соц. отпуск

                        xCell = xSpreadsheet.getCellByPosition(nColO_, nRow);
                        xCell.setValue(allO_D);

                        //------------------------------День матери

                        xCell = xSpreadsheet.getCellByPosition(nColDm, nRow);
                        xCell.setValue(allDm);

                        //------------------------------Мед.комиссия

                        xCell = xSpreadsheet.getCellByPosition(nColMk, nRow);
                        xCell.setValue(allMk);

                        //------------------------------Процент выполнения

                        xCell = xSpreadsheet.getCellByPosition(0, nRow);
                        tempSrt = xCell.getFormula().trim();

                        xCell = xSpreadsheet.getCellByPosition(1, nRow);
                        tempSrt = tempSrt + xCell.getFormula().trim();

                        if (l == 0) historySrt = tempSrt;

                        if (!tempSrt.equals(historySrt)) {
                            nRowEmpl = nRow;
                            historySrt = tempSrt;
                        } else {
                            xCell = xSpreadsheet.getCellByPosition(nColBVP, nRow);
                            xCell.setFormula("=IF(" + nColOtCl_ + (nRow + 1) + "=0;\"-\";"
                                    + "(SUM(" + nColBVIt_ + (nRowEmpl + 1) + ":" + nColBVIt_ + (nRow + 1) + "))/" + nColOtCl_ + (nRow + 1) + ")");

                            for (int i = nColTVCl; i < nColMk + 1; i++) {
                                if (nRow != 11) {
                                    if (i != nColXV && i != nColUmT && i != nColUvT && i != nColDK && i != nColNes && i != nColUch) {
                                        xCell = xSpreadsheet.getCellByPosition(i, nRow - 1);
                                        xCell.setFormula("");
                                    }
                                }
                            }
                        }

                        for (int i = nColTVCl; i < nColMk + 1; i++) {
                            //  if(nRow != 11){
                            if (i != nColXV && i != nColUmT && i != nColUvT && i != nColDK && i != nColNes && i != nColUch) {
                                xCell = xSpreadsheet.getCellByPosition(i, nRow);
                                if (xCell.getFormula().equals("0"))
                                    xCell.setFormula("");
                            }
                            //    }
                        }
                    }
                }

                nRow = nRowBrak;
                nRow += 2;
                for (int j = 0; j < 2; j++) {
                    for (int i = 0; i < nColAll; i++) {
                        xCell = xSpreadsheet.getCellByPosition(i, nRow);
                        xPropSet = UnoRuntime.queryInterface(XPropertySet.class, xCell);
                        xPropSet.setPropertyValue("TableBorder", aBorder);
                        if (i == 1) {
                            xCell.setFormula(j == 0 ? "Брак" : "ИТОГО");
                        } else if (i == nColBV) {
                            xCell.setValue(j == 0 ? Double.valueOf(UtilZPlata.formatNorm(brak, 2)) : 0);
                        }
                    }
                    nRow++;
                }

                //------------------------------ИТОГО

                //------------------------------Бригад. выработка
                xCell = xSpreadsheet.getCellByPosition(nColBV, nRow - 1);
                xCell.setFormula("=SUM(" + nColBV_ + 12 + ":" + nColBV_ + (nRow - 1) + ")");

                // Итого по выработке
                xCell = xSpreadsheet.getCellByPosition(nColBVIt, nRow - 1);
                xCell.setFormula("=SUM(" + nColBVIt_ + 12 + ":" + nColBVIt_ + (nRow - 1) + ")");

                //------------------------------Отработанное время

                xCell = xSpreadsheet.getCellByPosition(nColOtD, nRow - 1);
                xCell.setFormula("=SUM(" + nColOtD_ + 12 + ":" + nColOtD_ + (nRow - 1) + ")");

                xCell = xSpreadsheet.getCellByPosition(nColOtCl, nRow - 1);
                xCell.setFormula("=SUM(" + nColOtCl_ + 12 + ":" + nColOtCl_ + (nRow - 1) + ")");

                //------------------------------Внедрение

                xCell = xSpreadsheet.getCellByPosition(nColVD, nRow - 1);
                xCell.setFormula("=SUM(" + nColVD_ + 12 + ":" + nColVD_ + (nRow - 1) + ")");

                xCell = xSpreadsheet.getCellByPosition(nColVCl, nRow - 1);
                xCell.setFormula("=SUM(" + nColVCl_ + 12 + ":" + nColVCl_ + (nRow - 1) + ")");

                //------------------------------Тариф

                xCell = xSpreadsheet.getCellByPosition(nColTD, nRow - 1);
                xCell.setFormula("=SUM(" + nColTD_ + 12 + ":" + nColTD_ + (nRow - 1) + ")");

                xCell = xSpreadsheet.getCellByPosition(nColTCl, nRow - 1);
                xCell.setFormula("=SUM(" + nColTCl_ + 12 + ":" + nColTCl_ + (nRow - 1) + ")");

                //------------------------------Средний

                xCell = xSpreadsheet.getCellByPosition(nColSrD, nRow - 1);
                xCell.setFormula("=SUM(" + nColSrD_ + 12 + ":" + nColSrD_ + (nRow - 1) + ")");

                xCell = xSpreadsheet.getCellByPosition(nColSrCl, nRow - 1);
                xCell.setFormula("=SUM(" + nColSrCl_ + 12 + ":" + nColSrCl_ + (nRow - 1) + ")");

                //------------------------------Другие конвеера

                xCell = xSpreadsheet.getCellByPosition(nColDK, nRow - 1);
                xCell.setFormula("=SUM(" + nColDK_ + 12 + ":" + nColDK_ + (nRow - 1) + ")");

                //------------------------------Несорт. брак

                xCell = xSpreadsheet.getCellByPosition(nColNes, nRow - 1);
                xCell.setFormula("=SUM(" + nColNes_ + 12 + ":" + nColNes_ + (nRow - 1) + ")");

                //------------------------------Отпуск

                xCell = xSpreadsheet.getCellByPosition(nColO, nRow - 1);
                xCell.setFormula("=SUM(" + nColOt_ + 12 + ":" + nColOt_ + (nRow - 1) + ")");

                //------------------------------Б/лист

                xCell = xSpreadsheet.getCellByPosition(nColB, nRow - 1);
                xCell.setFormula("=SUM(" + nColB_ + 12 + ":" + nColB_ + (nRow - 1) + ")");

                //------------------------------Переход

                xCell = xSpreadsheet.getCellByPosition(nColPr, nRow - 1);
                xCell.setFormula("=SUM(" + nColPr_ + 12 + ":" + nColPr_ + (nRow - 1) + ")");

                //------------------------------Соц. отпуск

                xCell = xSpreadsheet.getCellByPosition(nColO_, nRow - 1);
                xCell.setFormula("=SUM(" + nColCO_ + 12 + ":" + nColCO_ + (nRow - 1) + ")");

                //------------------------------День матери

                xCell = xSpreadsheet.getCellByPosition(nColDm, nRow - 1);
                xCell.setFormula("=SUM(" + nColDm_ + 12 + ":" + nColDm_ + (nRow - 1) + ")");

                //------------------------------Мед.комиссия

                xCell = xSpreadsheet.getCellByPosition(nColMk, nRow - 1);
                xCell.setFormula("=SUM(" + nColMk_ + 12 + ":" + nColMk_ + (nRow - 1) + ")");

                //------------------------------Освоение профессии

                xCell = xSpreadsheet.getCellByPosition(nColOsP, nRow - 1);
                xCell.setFormula("=SUM(" + nColOsP_ + 12 + ":" + nColOsP_ + (nRow - 1) + ")");

                //------------------------------Итого выработка за месяц в н.-часах

                /*
                xCell = xSpreadsheet.getCellByPosition(nColBVIt, nRow-1);
                xCell.setFormula("="+nColBV_+nRow+"+"+nColDK_+nRow+"-"+nColOsP_+nRow);
                */

                //------------------------------Процент выполнения

                xCell = xSpreadsheet.getCellByPosition(nColBVP, nRow - 1);
                xCell.setFormula("=IF(" + nColOtCl_ + nRow + "=0;\"-\";(" + nColBVIt_ + nRow + ")/" + nColOtCl_ + nRow + ")");

                if (elements2.size() > 0) {
                    for (int i = 0; i < nColAll; i++) {
                        xCell = xSpreadsheet.getCellByPosition(i, nRow);
                        xPropSet = UnoRuntime.queryInterface(XPropertySet.class, xCell);
                        xPropSet.setPropertyValue("TableBorder", aBorder);
                        if (i == 1) {
                            xCell.setFormula("ИТОГО без ВТО и переданных на ДК");
                        } else if (i == nColBV) {
                            xCell.setFormula("=SUM(" + nColBV_ + 12 + ":" + nColBV_ + (nRowBrig) + ")");
                            //   xCell.setValue(Double.valueOf(UtilZPlata.formatNorm(brigVyrabatka,2)));
                        } else if (i == nColDK) {
                            xCell.setFormula("=SUM(" + nColDK_ + 12 + ":" + nColDK_ + (nRowBrig) + ")");
                        } else if (i == nColNes) {
                            xCell.setFormula("=SUM(" + nColNes_ + 12 + ":" + nColNes_ + (nRowBrig) + ")");
                        } else if (i == nColOtD) {
                            xCell.setFormula("=SUM(" + nColOtD_ + 12 + ":" + nColOtD_ + (nRowBrig) + ")");
                        } else if (i == nColOtCl) {
                            xCell.setFormula("=SUM(" + nColOtCl_ + 12 + ":" + nColOtCl_ + (nRowBrig) + ")");
                        } else if (i == nColBVIt) {
                            xCell.setFormula("=SUM(" + nColBVIt_ + 12 + ":" + nColBVIt_ + (nRowBrig) + ")");
                            //  xCell.setFormula("="+nColBV_+(nRow+1)+"+"+nColDK_+(nRow+1)+"-IF("+nColOsP_+(nRow+1)+"<>\"\";"+nColOsP_+(nRow+1)+";0)");
                        } else if (i == nColBVP) {
                            xCell.setFormula("=IF(" + nColOtCl_ + (nRow + 1) + "=0;\"-\";(" + nColBVIt_ + (nRow + 1) + ")/" + nColOtCl_ + (nRow + 1) + ")");
                        }
                    }
                } else if (dataPeredanoDK.size() > 0) {
                    for (int i = 0; i < nColAll; i++) {
                        xCell = xSpreadsheet.getCellByPosition(i, nRow);
                        xPropSet = UnoRuntime.queryInterface(XPropertySet.class, xCell);
                        xPropSet.setPropertyValue("TableBorder", aBorder);
                        if (i == 1) {
                            xCell.setFormula("ИТОГО без переданных на ДК");
                        } else if (i == nColBV) {
                            xCell.setFormula("=SUM(" + nColBV_ + 12 + ":" + nColBV_ + (nRowBrig) + ")");
                            //   xCell.setValue(Double.valueOf(UtilZPlata.formatNorm(brigVyrabatka,2)));
                        } else if (i == nColDK) {
                            xCell.setFormula("=SUM(" + nColDK_ + 12 + ":" + nColDK_ + (nRowBrig) + ")");
                        } else if (i == nColNes) {
                            xCell.setFormula("=SUM(" + nColNes_ + 12 + ":" + nColNes_ + (nRowBrig) + ")");
                        } else if (i == nColOtD) {
                            xCell.setFormula("=SUM(" + nColOtD_ + 12 + ":" + nColOtD_ + (nRowBrig) + ")");
                        } else if (i == nColOtCl) {
                            xCell.setFormula("=SUM(" + nColOtCl_ + 12 + ":" + nColOtCl_ + (nRowBrig) + ")");
                        } else if (i == nColBVIt) {
                            xCell.setFormula("=SUM(" + nColBVIt_ + 12 + ":" + nColBVIt_ + (nRowBrig) + ")");
                            //         xCell.setFormula("="+nColBV_+(nRow+1)+"+"+nColDK_+(nRow+1)+"-IF("+nColOsP_+(nRow+1)+"<>\"\";"+nColOsP_+(nRow+1)+";0)");
                        } else if (i == nColBVP) {
                            xCell.setFormula("=IF(" + nColOtCl_ + (nRow + 1) + "=0;\"-\";(" + nColBVIt_ + (nRow + 1) + ")/" + nColOtCl_ + (nRow + 1) + ")");
                        }
                    }
                }

            }

            //----------------Заполнение колонтитула документа

            String text =
                    UtilZPlata.PROF_STAFF_3 + " " + UtilZPlata.STAFF_3 + " __________________________" + "         "
                            + UtilZPlata.PROF_STAFF_4 + " " + UtilZPlata.STAFF_4 + " __________________________";

            insertTextInFooter(xSpreadsheetDocument, text);

        } catch (Exception e) {
            log.error("Ошибка при заполнении отчёта", e);
            throw new Exception("Ошибка при формировании отчёта ", e);
        }
    }

    private void fildVedomostZPlataT4NewForSewingWorkshop(XComponent currentDocument) throws Exception {
        try {

            String nColBV_;
            String nColBVIt_;
            String nColOtD_;
            String nColOtCl_;
            String nColVD_;
            String nColVCl_;
            String nColTD_;
            String nColTCl_;
            String nColSrD_;
            String nColSrCl_;
            String nColUch_;
            String nColDK_;
            String nColNes_;
            String nColOt_;
            String nColB_;
            String nColPr_;
            String nColCO_;
            String nColDm_;
            String nColMk_;
            String nColOsP_;
            String tempSrt;
            String historySrt;

            double vyrabotka;
            double tmpVyrabotka;
            double value;

            int nCol;
            int nColDept;
            int nColBrig;
            int nColPeriod;
            int nRow;
            int nRowBrak;
            int k;

            int nColBV;
            int nColBVIt;
            int nColBVP;
            int nColStoim;
            int nColPercent;
            int nColMinusPercent;
            int nColTVCl;
            int nColTNCl;
            int nColTNVr;
            int nColOtD;
            int nColOtCl;
            int nColXV;
            int nColVD;
            int nColVCl;
            int nColTD;
            int nColTCl;
            int nColSrD;
            int nColSrCl;
            int nColVr;
            int nColUmT;
            int nColUvT;
            int nColUch;
            int nColDK;
            int nColNes;
            int nColT_D;
            int nColT_Cl;
            int nColO;
            int nColB;
            int nColPr;
            int nColO_;
            int nColDm;
            int nColMk;
            int nColOsP;

            int nColAll;
            int nRowEmpl;
            int nRowBrig;

            int allCl;
            int allD;
            int allVD;
            int allVCl;
            int allTD;
            int allTCl;
            int allVrCl;
            int allT_D;
            int allT_Cl;
            int allSrD;
            int allSrCl;
            int allOD;
            int allBD;
            int allPrD;
            int allO_D;
            int allUch;
            int allDK;
            int allNes;
            int allDm;
            int allMk;
            int allZ;

            int tmp;

            Vector dataPeredanoDK;
            Vector dataPeredanoDKVTO;
            Vector colData;

            XSpreadsheetDocument xSpreadsheetDocument = UnoRuntime.queryInterface(XSpreadsheetDocument.class, currentDocument);
            XSpreadsheets xSpreadsheets = xSpreadsheetDocument.getSheets();
            Object sheet = xSpreadsheets.getByName("Лист1");
            XSpreadsheet xSpreadsheet = UnoRuntime.queryInterface(XSpreadsheet.class, sheet);
            Object sheet_2 = xSpreadsheets.getByName("Лист2");
            XSpreadsheet xSpreadsheet_2 = UnoRuntime.queryInterface(XSpreadsheet.class, sheet_2);
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

            //------------------------ Лист 2 ----------------------------
            //---------------- Ставка 1-го разряда -----------------------

            xCell = xSpreadsheet_2.getCellByPosition(4, 1);
            xCell.setValue(UtilZPlata.RATE_1ST_CATEGORY);

            xCell = xSpreadsheet_2.getCellByPosition(4, 16);
            xCell.setFormula(UtilZPlata.STAFF_1);

            xCell = xSpreadsheet_2.getCellByPosition(4, 17);
            xCell.setFormula(UtilZPlata.STAFF_2);

            //------------------------ Лист 1 ----------------------------
            //---------------- Заполнение шапки документа ----------------

            nColDept = 4;
            nColBrig = 14;
            nColPeriod = 19;

            xCell = xSpreadsheet.getCellByPosition(nColDept, 4);
            xCell.setFormula(data.get(0).toString().toLowerCase());

            xCell = xSpreadsheet.getCellByPosition(nColBrig, 4);
            xCell.setFormula(data.get(1).toString().toLowerCase());

            xCell = xSpreadsheet.getCellByPosition(nColPeriod, 4);
            xCell.setFormula(data.get(2).toString().toLowerCase());

            if (workSmena == 1) {
                nColBV_ = "AG";
                nColBVIt_ = "AH";
                nColOtD_ = "AN";
                nColOtCl_ = "AO";
                nColVD_ = "AQ";
                nColVCl_ = "AR";
                nColTD_ = "AS";
                nColTCl_ = "AT";
                nColSrD_ = "AU";
                nColSrCl_ = "AV";
                nColUch_ = "AZ";
                nColDK_ = "BA";
                nColNes_ = "BB";
                nColOt_ = "BE";
                nColB_ = "BF";
                nColPr_ = "BG";
                nColCO_ = "BH";
                nColDm_ = "BI";
                nColMk_ = "BJ";
                nColOsP_ = "BK";

                nColBV = 32;
                nColBVIt = 33;
                nColBVP = 34;
                nColStoim = 35;
                nColTVCl = 36;
                nColTNCl = 37;
                nColTNVr = 38;
                nColOtD = 39;
                nColOtCl = 40;
                nColXV = 41;
                nColVD = 42;
                nColVCl = 43;
                nColTD = 44;
                nColTCl = 45;
                nColSrD = 46;
                nColSrCl = 47;
                nColVr = 48;
                nColUmT = 49;
                nColUvT = 50;
                nColUch = 51;
                nColDK = 52;
                nColNes = 53;
                nColT_D = 54;
                nColT_Cl = 55;
                nColO = 56;
                nColB = 57;
                nColPr = 58;
                nColO_ = 59;
                nColDm = 60;
                nColMk = 61;
                nColOsP = 62;
                nColPercent = 63;
                nColMinusPercent = 64;
                nColAll = 65;
            } else {
                for (int i = 0; i < 2; i++) {
                    nColDept += 28;
                    nColBrig += 28;
                    nColPeriod += 28;

                    xCell = xSpreadsheet.getCellByPosition(nColDept, 4);
                    xCell.setFormula(data.get(0).toString().toLowerCase());

                    xCell = xSpreadsheet.getCellByPosition(nColBrig, 4);
                    xCell.setFormula(data.get(1).toString().toLowerCase());

                    xCell = xSpreadsheet.getCellByPosition(nColPeriod, 4);
                    xCell.setFormula(data.get(2).toString().toLowerCase());
                }

                nColBV_ = "CK";
                nColBVIt_ = "CL";
                nColOtD_ = "CR";
                nColOtCl_ = "CS";
                nColVD_ = "CU";
                nColVCl_ = "CV";
                nColTD_ = "CW";
                nColTCl_ = "CX";
                nColSrD_ = "CY";
                nColSrCl_ = "CZ";
                nColUch_ = "DD";
                nColDK_ = "DE";
                nColNes_ = "DF";
                nColOt_ = "DI";
                nColB_ = "DJ";
                nColPr_ = "DK";
                nColCO_ = "DL";
                nColDm_ = "DM";
                nColMk_ = "DN";
                nColOsP_ = "DO";

                nColBV = 88;
                nColBVIt = 89;
                nColBVP = 90;
                nColStoim = 91;
                nColTVCl = 92;
                nColTNCl = 93;
                nColTNVr = 94;
                nColOtD = 95;
                nColOtCl = 96;
                nColXV = 97;
                nColVD = 98;
                nColVCl = 99;
                nColTD = 100;
                nColTCl = 101;
                nColSrD = 102;
                nColSrCl = 103;
                nColVr = 104;
                nColUmT = 105;
                nColUvT = 106;
                nColUch = 107;
                nColDK = 108;
                nColNes = 109;
                nColT_D = 110;
                nColT_Cl = 111;
                nColO = 112;
                nColB = 113;
                nColPr = 114;
                nColO_ = 115;
                nColDm = 116;
                nColMk = 117;
                nColOsP = 118;
                nColPercent = 119;
                nColMinusPercent = 120;
                nColAll = 121;
            }

            int[] num = {0, 1, 2, 3, nColBV, nColDK, nColNes};

            xCell = xSpreadsheet.getCellByPosition(nColBV, 4);
            xCell.setFormula(data.get(0).toString().toLowerCase());

            xCell = xSpreadsheet.getCellByPosition(nColTNVr, 4);
            xCell.setFormula(data.get(1).toString().toLowerCase());

            xCell = xSpreadsheet.getCellByPosition(nColXV, 4);
            xCell.setFormula(data.get(2).toString().toLowerCase());

            xCell = xSpreadsheet.getCellByPosition(nColUch, 4);
            xCell.setFormula(data.get(0).toString().toLowerCase());

            xCell = xSpreadsheet.getCellByPosition(nColB, 4);
            xCell.setFormula(data.get(1).toString().toLowerCase());

            xCell = xSpreadsheet.getCellByPosition(nColDm, 4);
            xCell.setFormula(data.get(2).toString().toLowerCase());

            //------------------------------Рабочие дни

            nCol = 4;
            nRow = 7;

            for (Object workDay : workDays) {
                for (int j = 0; j < workSmena; j++) {
                    xCell = xSpreadsheet.getCellByPosition(nCol, nRow);
                    xCell.setValue(j + 1);

                    xCell = xSpreadsheet.getCellByPosition(nCol, nRow + 1);
                    xCell.setValue(8);

                    xCell = xSpreadsheet.getCellByPosition(nCol++, nRow + 2);
                    xCell.setFormula(String.valueOf(workDay));
                }
            }

            //----------------Заполнение тела документа

            //------------------------------Выработка за месяц (бригада, др. конвеера)

            nRow = 11;
            k = 0;

            dataPeredanoDK = new Vector();
            for (Object row : dataElement) {
                if (((Vector) row).get(7).toString().trim().equals(String.valueOf(idBrig))) {
                    k = 0;
                    for (int i = 0; i < nColAll; i++) {
                        xCell = xSpreadsheet.getCellByPosition(i, nRow);
                        xPropSet = UnoRuntime.queryInterface(XPropertySet.class, xCell);
                        xPropSet.setPropertyValue("TableBorder", aBorder);
                        if (k < num.length) {
                            if (i == num[k]) {
                                if (num[k] == nColBV) {
                                    xCell.setValue(Double.valueOf(UtilZPlata.formatNorm(Double.valueOf(((Vector) row).get(k).toString()), 2)));
                                } else if (num[k] == nColDK) {
                                    xCell.setValue(Double.valueOf(UtilZPlata.formatNorm(Double.valueOf(((Vector) row).get(k).toString()), 2)));
                                } else if (num[k] == nColNes) {
                                    xCell.setValue(Double.valueOf(UtilZPlata.formatNorm(Double.valueOf(((Vector) row).get(k).toString()), 2)));
                                } else if (num[k] == 1) {
                                    xCell.setFormula(((Vector) row).get(k).toString() + " " + ((Vector) row).get(8).toString());
                                } else {
                                    xCell.setFormula(((Vector) row).get(k).toString());
                                }
                                k++;
                            }
                        }
                        if (i == nColBVIt)
                            xCell.setFormula("=IF(" + nColUch_ + (nRow + 1) + ">0;"
                                    + "(" + nColBV_ + (nRow + 1) + "+" + nColDK_ + (nRow + 1) + "+" + nColNes_ + (nRow + 1) + "-IF(" + nColOsP_ + (nRow + 1) + "<>\"\";" + nColOsP_ + (nRow + 1) + ";0))*" + nColUch_ + (nRow + 1) + ";"
                                    + nColBV_ + (nRow + 1) + "+" + nColDK_ + (nRow + 1) + "+" + nColNes_ + (nRow + 1) + "-IF(" + nColOsP_ + (nRow + 1) + "<>\"\";" + nColOsP_ + (nRow + 1) + ";0))");
                        if (i == nColBVP)
                            xCell.setFormula("=IF(" + nColOtCl_ + (nRow + 1) + "=0;\"-\";(" + nColBVIt_ + (nRow + 1) + ")/" + nColOtCl_ + (nRow + 1) + ")");
                        if (i == nColStoim) {
                            int x = nRow + 1;
                            xCell.setFormula("=ROUND("
                                    + "IF(D" + x + "=1;ROUND(1*Лист2.E2/169;2);"
                                    + "IF(D" + x + "=2;ROUND(1.16*Лист2.E2/169;2);"
                                    + "IF(D" + x + "=3;ROUND(1.35*Лист2.E2/169;2);"
                                    + "IF(D" + x + "=4;ROUND(1.57*Лист2.E2/169;2);"
                                    + "IF(D" + x + "=5;ROUND(1.73*Лист2.E2/169;2);"
                                    + "IF(D" + x + "=6;ROUND(1.9*Лист2.E2/169;2);"
                                    + "IF(D" + x + "=7;ROUND(2.03*Лист2.E2/169;2);)))))));2)");
                        }
                        if (i == nColPercent) {
                            int x = nRow + 1;
                            xCell.setFormula(
                                    "=IF(C" + x + "=\"швея\";IF(OR(AI" + x
                                            + "=\"-\";AI" + x + "<=50%);0%;IF(AI" + x
                                            + "<=70%;140%;IF(AI" + x + "<=80%;160%;IF(AI" + x
                                            + "<=90%;180%;IF(AI" + x + "<=100%;200%;250%)))));" +
                                            "IF(OR(AI" + x + "=\"-\";AI" + x + "<=60%);0%;IF(AI" + x
                                            + "<=70%;140%;IF(AI" + x + "<=80%;160%;IF(AI" + x
                                            + "<=90%;180%;IF(AI" + x + "<=100%;200%;IF(AI" + x
                                            + "<=140%;210%;220%))))))) - BM" + x + "");
                        }
                        if (i == nColOsP)
                            xCell.setFormula(
                                    "=IF(" + nColBV_ + (nRow + 1) + ">=0;"
                                            + "(IF(C" + (nRow + 1) + "=\"" + UtilZPlata.OTCHET_PROF_OSVOENIE + "\";"
                                            + nColBV_ + (nRow + 1) + "+" + nColDK_ + (nRow + 1) + ";"
                                            + "0))"
                                            + ";\"\")"
                            );
                        if(i == nColMinusPercent)
                            xCell.setFormula("=0%");
                    }
                    nRow++;
                } else {
                    dataPeredanoDK.add(row);
                }
            }

            nRowBrig = nRow;

            //------------------------------ВТО
            dataPeredanoDKVTO = new Vector();
            if (!elements2.isEmpty()) {
                xCell = xSpreadsheet.getCellByPosition(1, nRow);
                xCell.setFormula("ВТО");
            }
            nRow++;

            for (Object row : elements2) {
                if (((Vector) row).get(7).toString().trim().equals(String.valueOf(idBrig))) {
                    k = 0;
                    for (int i = 0; i < nColAll; i++) {
                        xCell = xSpreadsheet.getCellByPosition(i, nRow);
                        xPropSet = UnoRuntime.queryInterface(XPropertySet.class, xCell);
                        xPropSet.setPropertyValue("TableBorder", aBorder);
                        if (k < num.length) {
                            if (i == num[k]) {
                                if (num[k] == nColBV) {
                                    xCell.setValue(Double.valueOf(UtilZPlata.formatNorm(Double.valueOf(((Vector) row).get(k).toString()), 2)));
                                } else if (num[k] == nColDK) {
                                    xCell.setValue(Double.valueOf(UtilZPlata.formatNorm(Double.valueOf(((Vector) row).get(k).toString()), 2)));
                                } else if (num[k] == nColNes) {
                                    xCell.setValue(Double.valueOf(UtilZPlata.formatNorm(Double.valueOf(((Vector) row).get(k).toString()), 2)));
                                } else if (num[k] == 1) {
                                    xCell.setFormula(((Vector) row).get(k).toString() + " " + ((Vector) row).get(8).toString());
                                } else {
                                    xCell.setFormula(((Vector) row).get(k).toString());
                                }
                                k++;
                            }
                        }
                        if (i == nColBVIt)
                            xCell.setFormula("=IF(" + nColUch_ + (nRow + 1) + ">0;"
                                    + "(" + nColBV_ + (nRow + 1) + "+" + nColDK_ + (nRow + 1) + "+" + nColNes_ + (nRow + 1) + "-IF(" + nColOsP_ + (nRow + 1) + "<>\"\";" + nColOsP_ + (nRow + 1) + ";0))*" + nColUch_ + (nRow + 1) + ";"
                                    + nColBV_ + (nRow + 1) + "+" + nColDK_ + (nRow + 1) + "+" + nColNes_ + (nRow + 1) + "-IF(" + nColOsP_ + (nRow + 1) + "<>\"\";" + nColOsP_ + (nRow + 1) + ";0))");
                        if (i == nColBVP)
                            xCell.setFormula("=IF(" + nColOtCl_ + (nRow + 1) + "=0;\"-\";(" + nColBVIt_ + (nRow + 1) + ")/" + nColOtCl_ + (nRow + 1) + ")");
                        if (i == nColStoim) {
                            int x = nRow + 1;
                            xCell.setFormula("=ROUND("
                                    + "IF(D" + x + "=1;ROUND(1*Лист2.E2/169;2);"
                                    + "IF(D" + x + "=2;ROUND(1.16*Лист2.E2/169;2);"
                                    + "IF(D" + x + "=3;ROUND(1.35*Лист2.E2/169;2);"
                                    + "IF(D" + x + "=4;ROUND(1.57*Лист2.E2/169;2);"
                                    + "IF(D" + x + "=5;ROUND(1.73*Лист2.E2/169;2);"
                                    + "IF(D" + x + "=6;ROUND(1.9*Лист2.E2/169;2);"
                                    + "IF(D" + x + "=7;ROUND(2.03*Лист2.E2/169;2);)))))));2)");
                        }
                        if (i == nColPercent) {
                            int x = nRow + 1;
                                    xCell.setFormula(
                                            "=IF(C" + x + "=\"швея\";IF(OR(AI" + x
                                                    + "=\"-\";AI" + x + "<=50%);0%;IF(AI" + x
                                                    + "<=70%;140%;IF(AI" + x + "<=80%;160%;IF(AI" + x
                                                    + "<=90%;180%;IF(AI" + x + "<=100%;200%;250%)))));" +
                                                    "IF(OR(AI" + x + "=\"-\";AI" + x + "<=60%);0%;IF(AI" + x
                                                    + "<=70%;140%;IF(AI" + x + "<=80%;160%;IF(AI" + x
                                                    + "<=90%;180%;IF(AI" + x + "<=100%;200%;IF(AI" + x
                                                    + "<=140%;210%;220%))))))) - BM" + x + "");
                        }
                        if (i == nColOsP)
                            xCell.setFormula(
                                    "=IF(" + nColBV_ + (nRow + 1) + ">=0;"
                                            + "(IF(C" + (nRow + 1) + "=\"" + UtilZPlata.OTCHET_PROF_OSVOENIE + "\";"
                                            + nColBV_ + (nRow + 1) + "+" + nColDK_ + (nRow + 1) + ";"
                                            + "0))"
                                            + ";\"\")"
                            );
                        if(i == nColMinusPercent)
                            xCell.setFormula("=0%");
                    }
                    nRow++;
                } else {
                    dataPeredanoDKVTO.add(row);
                }
            }

            //------------------------------Передано на др. конвейера

            if (dataPeredanoDKVTO.size() > 0) {
                int nRowDKVTO = nRow;
                nRow++;

                for (Object row : dataPeredanoDKVTO) {
                    k = 0;
                    for (int i = 0; i < nColAll; i++) {
                        xCell = xSpreadsheet.getCellByPosition(i, nRow);
                        xPropSet = UnoRuntime.queryInterface(XPropertySet.class, xCell);
                        xPropSet.setPropertyValue("TableBorder", aBorder);
                        if (k < num.length) {
                            if (i == num[k]) {
                                if (num[k] == nColBV) {
                                    xCell.setValue(Double.valueOf(UtilZPlata.formatNorm(Double.valueOf(((Vector) row).get(k).toString()), 2)));
                                } else if (num[k] == nColDK || num[k] == nColNes) {
                                    xCell.setValue(0);
                                } else if (num[k] == 1) {
                                    xCell.setFormula("(" + ((Vector) row).get(7).toString() + ")" + ((Vector) row).get(k).toString());
                                } else {
                                    xCell.setFormula(((Vector) row).get(k).toString());
                                }
                                k++;
                            }
                        }
                        if (i == nColBVIt)
                            xCell.setFormula("=" + nColBV_ + (nRow + 1) + "+" + nColDK_ + (nRow + 1) + "+" + nColNes_ + (nRow + 1) + "-IF(" + nColOsP_ + (nRow + 1) + "<>\"\";" + nColOsP_ + (nRow + 1) + ";0)");
                    }
                    nRow++;
                }

                if (nRowDKVTO + 1 != nRow) {
                    xCell = xSpreadsheet.getCellByPosition(1, nRowDKVTO);
                    xCell.setFormula("Передано на ДК ВТО");
                }

            }

            if (dataPeredanoDK.size() > 0) {
                int nRowDK = nRow;
                nRow++;

                for (Object row : dataPeredanoDK) {
                    if (Double.valueOf(UtilZPlata.formatNorm(Double.valueOf(((Vector) row).get(4).toString()), 2)) != 0) {
                        k = 0;
                        for (int i = 0; i < nColAll; i++) {
                            xCell = xSpreadsheet.getCellByPosition(i, nRow);
                            xPropSet = UnoRuntime.queryInterface(XPropertySet.class, xCell);
                            xPropSet.setPropertyValue("TableBorder", aBorder);
                            if (k < num.length) {
                                if (i == num[k]) {
                                    if (num[k] == nColBV) {
                                        xCell.setValue(Double.valueOf(UtilZPlata.formatNorm(Double.valueOf(((Vector) row).get(k).toString()), 2)));
                                    } else if (num[k] == nColDK) {
                                        xCell.setValue(0);
                                    } else if (num[k] == nColNes) {
                                        xCell.setValue(0);
                                    } else if (num[k] == 1) {
                                        xCell.setFormula("(" + ((Vector) row).get(7).toString() + ")" + ((Vector) row).get(k).toString());
                                    } else {
                                        xCell.setFormula(((Vector) row).get(k).toString());
                                    }
                                    k++;
                                }
                            }
                            if (i == nColBVIt)
                                xCell.setFormula("=" + nColBV_ + (nRow + 1) + "+" + nColDK_ + (nRow + 1) + "+" + nColNes_ + (nRow + 1) + "-IF(" + nColOsP_ + (nRow + 1) + "<>\"\";" + nColOsP_ + (nRow + 1) + ";0)");
                        }
                        nRow++;
                    }
                }

                if (nRowDK + 1 != nRow) {
                    xCell = xSpreadsheet.getCellByPosition(1, nRowDK);
                    xCell.setFormula("Передано на ДК");
                }
            }

            nRowBrak = nRow;

            //------------------------------Табель
            if (JOptionPane.showOptionDialog(null, "Заполните все прогулы, б/листы, отпуска и т.д.\n"
                            + "Далее нажмите кнопку 'Продолжить' для завершения\n"
                            + "формирования отчёта. ", "Внимание!",
                    JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, null,
                    new Object[]{"Продолжить", "Отмена"}, "Продолжить") == JOptionPane.YES_OPTION) {

                nRow = 10;
                nRowEmpl = 11;
                historySrt = "";
                for (int l = 0; l < dataElement.size() + elements2.size() + 1 - dataPeredanoDK.size() - dataPeredanoDKVTO.size(); l++) {
                    nRow++;

                    tempSrt = "";
                    vyrabotka = 0;

                    allCl = 0;
                    allD = 0;
                    allVD = 0;
                    allVCl = 0;
                    allTD = 0;
                    allTCl = 0;
                    allVrCl = 0;
                    allT_D = 0;
                    allT_Cl = 0;
                    allSrD = 0;
                    allSrCl = 0;
                    allOD = 0;
                    allBD = 0;
                    allPrD = 0;
                    allO_D = 0;
                    allUch = 0;
                    allDK = 0;
                    allNes = 0;
                    allDm = 0;
                    allMk = 0;
                    allZ = 0;

                    //------------------------------запоминаем колонки которые рабочие дни
                    //-и подсчитывает кол-во б/лист, отпуск, свой счёт,средний, 2/3 тарифа

                    colData = new Vector();
                    for (int i = 4; i < nColBV; i++) {
                        xCell = xSpreadsheet.getCellByPosition(i, 9);
                        if (!xCell.getFormula().equals("")) {
                            xCell = xSpreadsheet.getCellByPosition(i, nRow);
                            if (xCell.getFormula().equals("")) {
                                colData.add(i);

                                xCell = xSpreadsheet.getCellByPosition(i, 8);
                                allCl += xCell.getValue();
                                allD += 1;

                            } else if (xCell.getFormula().toLowerCase().trim().equals("б")) {
                                allBD += 1;
                            } else if (xCell.getFormula().toLowerCase().trim().equals("о") || xCell.getFormula().toLowerCase().trim().equals("o")) {
                                allOD += 1;
                            } else if (xCell.getFormula().toLowerCase().trim().equals("со") || xCell.getFormula().toLowerCase().trim().equals("co") ||
                                    xCell.getFormula().toLowerCase().trim().equals("а") || xCell.getFormula().toLowerCase().trim().equals("a") ||
                                    xCell.getFormula().toLowerCase().trim().equals("у") || xCell.getFormula().toLowerCase().trim().equals("y") ||
                                    xCell.getFormula().toLowerCase().trim().equals("св") || xCell.getFormula().toLowerCase().trim().equals("cb")) {
                                allO_D += 1;
                            } else if (xCell.getFormula().toLowerCase().trim().equals("п")) {
                                allPrD += 1;
                            } else if (xCell.getFormula().toLowerCase().trim().equals("в") || xCell.getFormula().toLowerCase().trim().equals("b")) {
                                allVD += 1;
                                xCell = xSpreadsheet.getCellByPosition(i, 8);
                                allVCl += xCell.getValue();
                            } else if (xCell.getFormula().toLowerCase().trim().equals("т") || xCell.getFormula().toLowerCase().trim().equals("t")) {
                                allTD += 1;
                                xCell = xSpreadsheet.getCellByPosition(i, 8);
                                allTCl += xCell.getValue();

                                xCell = xSpreadsheet.getCellByPosition(i, 7);
                                if (xCell.getFormula().equals("2") ||
                                        xCell.getFormula().toLowerCase().trim().equals("2х") ||
                                        xCell.getFormula().toLowerCase().trim().equals("2x")) {
                                    xCell = xSpreadsheet.getCellByPosition(nColTVCl, nRow);
                                    xCell.setValue(xCell.getValue() + 1.5);
                                }

                            } else if (xCell.getFormula().toLowerCase().trim().equals("т*") || xCell.getFormula().toLowerCase().trim().equals("t*")) {
                                allT_D += 1;
                                xCell = xSpreadsheet.getCellByPosition(i, 8);
                                allT_Cl += xCell.getValue();
                            } else if (xCell.getFormula().toLowerCase().trim().equals("ср") || xCell.getFormula().toLowerCase().trim().equals("cp")) {
                                allSrD += 1;
                                xCell = xSpreadsheet.getCellByPosition(i, 8);
                                allSrCl += xCell.getValue();
                            } else if (xCell.getFormula().toLowerCase().trim().equals("вр") || xCell.getFormula().toLowerCase().trim().equals("bp")) {
                                xCell = xSpreadsheet.getCellByPosition(i, 8);
                                allVrCl += xCell.getValue();
                            } else if (xCell.getFormula().toLowerCase().trim().equals("дм") || xCell.getFormula().toLowerCase().trim().equals("dm")) {
                                allDm += 1;
                            } else if (xCell.getFormula().toLowerCase().trim().equals("мк") || xCell.getFormula().toLowerCase().trim().equals("mk")) {
                                allMk += 1;
                            }
                        }
                    }

                    //------------------------------выработка по рабочим дням

                    xCell = xSpreadsheet.getCellByPosition(nColBVIt, nRow);
                    if (!xCell.getFormula().isEmpty()) {
                        vyrabotka = xCell.getValue();
                        tmpVyrabotka = 0;

                        for (int i = 0; i < colData.size() - 1; i++) {
                            tmp = 0;

                            xCell = xSpreadsheet.getCellByPosition(Integer.valueOf(colData.get(i).toString()), nRow);
                            xCell.setValue(Double.valueOf(UtilZPlata.formatNorm(Double.valueOf(vyrabotka / colData.size()), 2)));
                            tmpVyrabotka += Double.valueOf(UtilZPlata.formatNorm(Double.valueOf(vyrabotka / colData.size()), 2));

                            xCell = xSpreadsheet.getCellByPosition(Integer.valueOf(colData.get(i).toString()), 7);
                            if (xCell.getFormula().equals("2") || xCell.getFormula().toLowerCase().trim().equals("2х") || xCell.getFormula().toLowerCase().trim().equals("2x")) {
                                xCell = xSpreadsheet.getCellByPosition(nColTVCl, nRow);
                                xCell.setValue(xCell.getValue() + 1.5);

                            } else if (xCell.getFormula().equals("3")) {
                                xCell = xSpreadsheet.getCellByPosition(Integer.valueOf(colData.get(i).toString()), 8);
                                tmp += xCell.getValue();

                                xCell = xSpreadsheet.getCellByPosition(nColTNCl, nRow);
                                xCell.setValue(xCell.getValue() + tmp);

                                xCell = xSpreadsheet.getCellByPosition(nColTNVr, nRow);
                                xCell.setValue(xCell.getValue() + Double.valueOf(UtilZPlata.formatNorm(Double.valueOf(vyrabotka / colData.size()), 2)));

                            }

                            xCell = xSpreadsheet.getCellByPosition(Integer.valueOf(colData.get(i).toString()), 7);
                            if (xCell.getFormula().toLowerCase().trim().equals("х") || xCell.getFormula().toLowerCase().trim().equals("x") ||
                                    xCell.getFormula().toLowerCase().trim().equals("1х") || xCell.getFormula().toLowerCase().trim().equals("1x") ||
                                    xCell.getFormula().toLowerCase().trim().equals("2х") || xCell.getFormula().toLowerCase().trim().equals("2x")) {
                                xCell = xSpreadsheet.getCellByPosition(nColXV, nRow);
                                xCell.setValue(xCell.getValue() + Double.valueOf(UtilZPlata.formatNorm(Double.valueOf(vyrabotka / colData.size()), 2)));
                            }
                        }

                        tmp = 0;
                        value = 0;

                        xCell = xSpreadsheet.getCellByPosition(nColBVIt, nRow);
                        value = xCell.getValue();

                        if (colData.size() > 0) {
                            xCell = xSpreadsheet.getCellByPosition(Integer.valueOf(colData.get(colData.size() - 1).toString()), nRow);
                            xCell.setValue(Double.valueOf(UtilZPlata.formatNorm(value - tmpVyrabotka, 2)));

                            xCell = xSpreadsheet.getCellByPosition(Integer.valueOf(colData.get(colData.size() - 1).toString()), 7);
                            if (xCell.getFormula().equals("2") || xCell.getFormula().toLowerCase().trim().equals("2х") || xCell.getFormula().toLowerCase().trim().equals("2x")) {
                                xCell = xSpreadsheet.getCellByPosition(nColTVCl, nRow);
                                xCell.setValue(xCell.getValue() + 1.5);

                            } else if (xCell.getFormula().equals("3")) {
                                xCell = xSpreadsheet.getCellByPosition(Integer.valueOf(colData.get(colData.size() - 1).toString()), 8);
                                tmp += xCell.getValue();

                                xCell = xSpreadsheet.getCellByPosition(nColTNCl, nRow);
                                xCell.setValue(xCell.getValue() + tmp);

                                xCell = xSpreadsheet.getCellByPosition(nColTNVr, nRow);
                                xCell.setValue(xCell.getValue() + Double.valueOf(UtilZPlata.formatNorm(value - tmpVyrabotka, 2)));

                            }

                            xCell = xSpreadsheet.getCellByPosition(Integer.valueOf(colData.get(colData.size() - 1).toString()), 7);
                            if (xCell.getFormula().toLowerCase().trim().equals("х") || xCell.getFormula().toLowerCase().trim().equals("x") ||
                                    xCell.getFormula().toLowerCase().trim().equals("1х") || xCell.getFormula().toLowerCase().trim().equals("1x") ||
                                    xCell.getFormula().toLowerCase().trim().equals("2х") || xCell.getFormula().toLowerCase().trim().equals("2x")) {
                                xCell = xSpreadsheet.getCellByPosition(nColXV, nRow);
                                xCell.setValue(xCell.getValue() + Double.valueOf(UtilZPlata.formatNorm(value - tmpVyrabotka, 2)));
                            }
                        }

                        //------------------------------отработанное время

                        xCell = xSpreadsheet.getCellByPosition(nColOtD, nRow);
                        xCell.setValue(allD);

                        xCell = xSpreadsheet.getCellByPosition(nColOtCl, nRow);
                        xCell.setValue(allCl);

                        //------------------------------Внедрение

                        xCell = xSpreadsheet.getCellByPosition(nColVD, nRow);
                        xCell.setValue(allVD);

                        xCell = xSpreadsheet.getCellByPosition(nColVCl, nRow);
                        xCell.setValue(allVCl);

                        //------------------------------Тариф

                        xCell = xSpreadsheet.getCellByPosition(nColTD, nRow);
                        xCell.setValue(allTD);

                        xCell = xSpreadsheet.getCellByPosition(nColTCl, nRow);
                        xCell.setValue(allTCl);


                        //------------------------------Средний

                        xCell = xSpreadsheet.getCellByPosition(nColSrD, nRow);
                        xCell.setFormula("=IF(C" + (nRow + 1) + "=\"" + UtilZPlata.OTCHET_PROF_OSVOENIE + "\";" + nColOtD_ + (nRow + 1) + ";" + (allSrD > 0 ? allSrD : "\"\"") + ")");
                        //xCell.setValue(allSrD);

                        xCell = xSpreadsheet.getCellByPosition(nColSrCl, nRow);
                        xCell.setFormula("=IF(C" + (nRow + 1) + "=\"" + UtilZPlata.OTCHET_PROF_OSVOENIE + "\";" + nColOtCl_ + (nRow + 1) + ";" + (allSrCl > 0 ? allSrCl : "\"\"") + ")");
                        //xCell.setValue(allSrCl);

                        //------------------------------Вредность

                        xCell = xSpreadsheet.getCellByPosition(nColVr, nRow);
                        xCell.setValue(allVrCl);

                        //------------------------------2/3 тарифа

                        xCell = xSpreadsheet.getCellByPosition(nColT_D, nRow);
                        xCell.setValue(allT_D);

                        xCell = xSpreadsheet.getCellByPosition(nColT_Cl, nRow);
                        xCell.setValue(allT_Cl);

                        //------------------------------Отпуск

                        xCell = xSpreadsheet.getCellByPosition(nColO, nRow);
                        xCell.setValue(allOD);

                        //------------------------------Б/лист

                        xCell = xSpreadsheet.getCellByPosition(nColB, nRow);
                        xCell.setValue(allBD);

                        //------------------------------Перевод

                        xCell = xSpreadsheet.getCellByPosition(nColPr, nRow);
                        xCell.setValue(allPrD);

                        //------------------------------Соц. отпуск

                        xCell = xSpreadsheet.getCellByPosition(nColO_, nRow);
                        xCell.setValue(allO_D);

                        //------------------------------День матери

                        xCell = xSpreadsheet.getCellByPosition(nColDm, nRow);
                        xCell.setValue(allDm);

                        //------------------------------Мед.комиссия

                        xCell = xSpreadsheet.getCellByPosition(nColMk, nRow);
                        xCell.setValue(allMk);

                        //------------------------------Процент выполнения

                        xCell = xSpreadsheet.getCellByPosition(0, nRow);
                        tempSrt = xCell.getFormula().trim();

                        xCell = xSpreadsheet.getCellByPosition(1, nRow);
                        tempSrt = tempSrt + xCell.getFormula().trim();

                        if (l == 0) historySrt = tempSrt;

                        if (!tempSrt.equals(historySrt)) {
                            nRowEmpl = nRow;
                            historySrt = tempSrt;
                        } else {
                            xCell = xSpreadsheet.getCellByPosition(nColBVP, nRow);
                            xCell.setFormula("=IF(" + nColOtCl_ + (nRow + 1) + "=0;\"-\";"
                                    + "(SUM(" + nColBVIt_ + (nRowEmpl + 1) + ":" + nColBVIt_ + (nRow + 1) + "))/" + nColOtCl_ + (nRow + 1) + ")");

                            for (int i = nColTVCl; i < nColMk + 1; i++) {
                                if (nRow != 11) {
                                    if (i != nColXV && i != nColUmT && i != nColUvT && i != nColDK && i != nColNes && i != nColUch) {
                                        xCell = xSpreadsheet.getCellByPosition(i, nRow - 1);
                                        xCell.setFormula("");
                                    }
                                }
                            }
                        }

                        for (int i = nColTVCl; i < nColMk + 1; i++) {
                            //  if(nRow != 11){
                            if (i != nColXV && i != nColUmT && i != nColUvT && i != nColDK && i != nColNes && i != nColUch) {
                                xCell = xSpreadsheet.getCellByPosition(i, nRow);
                                if (xCell.getFormula().equals("0"))
                                    xCell.setFormula("");
                            }
                            //    }
                        }
                    }
                }

                nRow = nRowBrak;
                nRow += 2;
                for (int j = 0; j < 2; j++) {
                    for (int i = 0; i < nColAll; i++) {
                        xCell = xSpreadsheet.getCellByPosition(i, nRow);
                        xPropSet = UnoRuntime.queryInterface(XPropertySet.class, xCell);
                        xPropSet.setPropertyValue("TableBorder", aBorder);
                        if (i == 1) {
                            xCell.setFormula(j == 0 ? "Брак" : "ИТОГО");
                        } else if (i == nColBV) {
                            xCell.setValue(j == 0 ? Double.valueOf(UtilZPlata.formatNorm(brak, 2)) : 0);
                        }
                    }
                    nRow++;
                }

                //------------------------------ИТОГО

                //------------------------------Бригад. выработка
                xCell = xSpreadsheet.getCellByPosition(nColBV, nRow - 1);
                xCell.setFormula("=SUM(" + nColBV_ + 12 + ":" + nColBV_ + (nRow - 1) + ")");

                // Итого по выработке
                xCell = xSpreadsheet.getCellByPosition(nColBVIt, nRow - 1);
                xCell.setFormula("=SUM(" + nColBVIt_ + 12 + ":" + nColBVIt_ + (nRow - 1) + ")");

                //------------------------------Отработанное время

                xCell = xSpreadsheet.getCellByPosition(nColOtD, nRow - 1);
                xCell.setFormula("=SUM(" + nColOtD_ + 12 + ":" + nColOtD_ + (nRow - 1) + ")");

                xCell = xSpreadsheet.getCellByPosition(nColOtCl, nRow - 1);
                xCell.setFormula("=SUM(" + nColOtCl_ + 12 + ":" + nColOtCl_ + (nRow - 1) + ")");

                //------------------------------Внедрение

                xCell = xSpreadsheet.getCellByPosition(nColVD, nRow - 1);
                xCell.setFormula("=SUM(" + nColVD_ + 12 + ":" + nColVD_ + (nRow - 1) + ")");

                xCell = xSpreadsheet.getCellByPosition(nColVCl, nRow - 1);
                xCell.setFormula("=SUM(" + nColVCl_ + 12 + ":" + nColVCl_ + (nRow - 1) + ")");

                //------------------------------Тариф

                xCell = xSpreadsheet.getCellByPosition(nColTD, nRow - 1);
                xCell.setFormula("=SUM(" + nColTD_ + 12 + ":" + nColTD_ + (nRow - 1) + ")");

                xCell = xSpreadsheet.getCellByPosition(nColTCl, nRow - 1);
                xCell.setFormula("=SUM(" + nColTCl_ + 12 + ":" + nColTCl_ + (nRow - 1) + ")");

                //------------------------------Средний

                xCell = xSpreadsheet.getCellByPosition(nColSrD, nRow - 1);
                xCell.setFormula("=SUM(" + nColSrD_ + 12 + ":" + nColSrD_ + (nRow - 1) + ")");

                xCell = xSpreadsheet.getCellByPosition(nColSrCl, nRow - 1);
                xCell.setFormula("=SUM(" + nColSrCl_ + 12 + ":" + nColSrCl_ + (nRow - 1) + ")");

                //------------------------------Другие конвеера

                xCell = xSpreadsheet.getCellByPosition(nColDK, nRow - 1);
                xCell.setFormula("=SUM(" + nColDK_ + 12 + ":" + nColDK_ + (nRow - 1) + ")");

                //------------------------------Несорт. брак

                xCell = xSpreadsheet.getCellByPosition(nColNes, nRow - 1);
                xCell.setFormula("=SUM(" + nColNes_ + 12 + ":" + nColNes_ + (nRow - 1) + ")");

                //------------------------------Отпуск

                xCell = xSpreadsheet.getCellByPosition(nColO, nRow - 1);
                xCell.setFormula("=SUM(" + nColOt_ + 12 + ":" + nColOt_ + (nRow - 1) + ")");

                //------------------------------Б/лист

                xCell = xSpreadsheet.getCellByPosition(nColB, nRow - 1);
                xCell.setFormula("=SUM(" + nColB_ + 12 + ":" + nColB_ + (nRow - 1) + ")");

                //------------------------------Переход

                xCell = xSpreadsheet.getCellByPosition(nColPr, nRow - 1);
                xCell.setFormula("=SUM(" + nColPr_ + 12 + ":" + nColPr_ + (nRow - 1) + ")");

                //------------------------------Соц. отпуск

                xCell = xSpreadsheet.getCellByPosition(nColO_, nRow - 1);
                xCell.setFormula("=SUM(" + nColCO_ + 12 + ":" + nColCO_ + (nRow - 1) + ")");

                //------------------------------День матери

                xCell = xSpreadsheet.getCellByPosition(nColDm, nRow - 1);
                xCell.setFormula("=SUM(" + nColDm_ + 12 + ":" + nColDm_ + (nRow - 1) + ")");

                //------------------------------Мед.комиссия

                xCell = xSpreadsheet.getCellByPosition(nColMk, nRow - 1);
                xCell.setFormula("=SUM(" + nColMk_ + 12 + ":" + nColMk_ + (nRow - 1) + ")");

                //------------------------------Освоение профессии

                xCell = xSpreadsheet.getCellByPosition(nColOsP, nRow - 1);
                xCell.setFormula("=SUM(" + nColOsP_ + 12 + ":" + nColOsP_ + (nRow - 1) + ")");

                //------------------------------Итого выработка за месяц в н.-часах

                /*
                xCell = xSpreadsheet.getCellByPosition(nColBVIt, nRow-1);
                xCell.setFormula("="+nColBV_+nRow+"+"+nColDK_+nRow+"-"+nColOsP_+nRow);
                */

                //------------------------------Процент выполнения

                xCell = xSpreadsheet.getCellByPosition(nColBVP, nRow - 1);
                xCell.setFormula("=IF(" + nColOtCl_ + nRow + "=0;\"-\";(" + nColBVIt_ + nRow + ")/" + nColOtCl_ + nRow + ")");

                xCell = xSpreadsheet.getCellByPosition(nColPercent, nRow - 1);
                xCell.setFormula("=IF(" +
                        "OR(AI" + nRow + "=\"-\";AI" + nRow + "<=85%);\"90%\";" +
                        "IF(AI" + nRow + "<=90%;\"115%\";" +
                        "IF(AI" + nRow + "<=95%;\"145%\";" +
                        "IF(AI" + nRow + "<=100%;\"170%\";\"190%\"))))");

                if (!elements2.isEmpty()) {
                    for (int i = 0; i < nColAll; i++) {
                        xCell = xSpreadsheet.getCellByPosition(i, nRow);
                        xPropSet = UnoRuntime.queryInterface(XPropertySet.class, xCell);
                        xPropSet.setPropertyValue("TableBorder", aBorder);
                        if (i == 1) {
                            xCell.setFormula("ИТОГО без ВТО и переданных на ДК");
                        } else if (i == nColBV) {
                            xCell.setFormula("=SUM(" + nColBV_ + 12 + ":" + nColBV_ + (nRowBrig) + ")");
                            //   xCell.setValue(Double.valueOf(UtilZPlata.formatNorm(brigVyrabatka,2)));
                        } else if (i == nColDK) {
                            xCell.setFormula("=SUM(" + nColDK_ + 12 + ":" + nColDK_ + (nRowBrig) + ")");
                        } else if (i == nColNes) {
                            xCell.setFormula("=SUM(" + nColNes_ + 12 + ":" + nColNes_ + (nRowBrig) + ")");
                        } else if (i == nColOtD) {
                            xCell.setFormula("=SUM(" + nColOtD_ + 12 + ":" + nColOtD_ + (nRowBrig) + ")");
                        } else if (i == nColOtCl) {
                            xCell.setFormula("=SUM(" + nColOtCl_ + 12 + ":" + nColOtCl_ + (nRowBrig) + ")");
                        } else if (i == nColBVIt) {
                            xCell.setFormula("=SUM(" + nColBVIt_ + 12 + ":" + nColBVIt_ + (nRowBrig) + ")");
                            //  xCell.setFormula("="+nColBV_+(nRow+1)+"+"+nColDK_+(nRow+1)+"-IF("+nColOsP_+(nRow+1)+"<>\"\";"+nColOsP_+(nRow+1)+";0)");
                        } else if (i == nColBVP) {
                            xCell.setFormula("=IF(" + nColOtCl_ + (nRow + 1) + "=0;\"-\";(" + nColBVIt_ + (nRow + 1) + ")/" + nColOtCl_ + (nRow + 1) + ")");
                        }
                    }
                } else if (!dataPeredanoDK.isEmpty()) {
                    for (int i = 0; i < nColAll; i++) {
                        xCell = xSpreadsheet.getCellByPosition(i, nRow);
                        xPropSet = UnoRuntime.queryInterface(XPropertySet.class, xCell);
                        xPropSet.setPropertyValue("TableBorder", aBorder);
                        if (i == 1) {
                            xCell.setFormula("ИТОГО без переданных на ДК");
                        } else if (i == nColBV) {
                            xCell.setFormula("=SUM(" + nColBV_ + 12 + ":" + nColBV_ + (nRowBrig) + ")");
                            //   xCell.setValue(Double.valueOf(UtilZPlata.formatNorm(brigVyrabatka,2)));
                        } else if (i == nColDK) {
                            xCell.setFormula("=SUM(" + nColDK_ + 12 + ":" + nColDK_ + (nRowBrig) + ")");
                        } else if (i == nColNes) {
                            xCell.setFormula("=SUM(" + nColNes_ + 12 + ":" + nColNes_ + (nRowBrig) + ")");
                        } else if (i == nColOtD) {
                            xCell.setFormula("=SUM(" + nColOtD_ + 12 + ":" + nColOtD_ + (nRowBrig) + ")");
                        } else if (i == nColOtCl) {
                            xCell.setFormula("=SUM(" + nColOtCl_ + 12 + ":" + nColOtCl_ + (nRowBrig) + ")");
                        } else if (i == nColBVIt) {
                            xCell.setFormula("=SUM(" + nColBVIt_ + 12 + ":" + nColBVIt_ + (nRowBrig) + ")");
                            //         xCell.setFormula("="+nColBV_+(nRow+1)+"+"+nColDK_+(nRow+1)+"-IF("+nColOsP_+(nRow+1)+"<>\"\";"+nColOsP_+(nRow+1)+";0)");
                        } else if (i == nColBVP) {
                            xCell.setFormula("=IF(" + nColOtCl_ + (nRow + 1) + "=0;\"-\";(" + nColBVIt_ + (nRow + 1) + ")/" + nColOtCl_ + (nRow + 1) + ")");
                        }
                    }
                }

            }

            //----------------Заполнение колонтитула документа

            String text =
                    UtilZPlata.PROF_STAFF_3 + " " + UtilZPlata.STAFF_3 + " __________________________" + "         "
                            + UtilZPlata.PROF_STAFF_4 + " " + UtilZPlata.STAFF_4 + " __________________________";

            XStyleFamiliesSupplier StyleFam = UnoRuntime.queryInterface(XStyleFamiliesSupplier.class, xSpreadsheetDocument);
            XNameAccess StyleFamNames = StyleFam.getStyleFamilies();

            XNameAccess PageStyles = (XNameAccess) AnyConverter.toObject(new Type(XNameAccess.class), StyleFamNames.getByName("PageStyles"));
            XStyle StdStyle = (XStyle) AnyConverter.toObject(new Type(XStyle.class), PageStyles.getByName("Default"));

            xPropSet = UnoRuntime.queryInterface(XPropertySet.class, StdStyle);

            Object obj = xPropSet.getPropertyValue("RightPageFooterContent");
            XHeaderFooterContent RPHFC = (XHeaderFooterContent) AnyConverter.toObject(new com.sun.star.uno.Type(XHeaderFooterContent.class), obj);
            XText center = RPHFC.getCenterText();

            center.setString(text);

            xPropSet.setPropertyValue("RightPageFooterContent", RPHFC);

        } catch (Exception e) {
            log.error("Ошибка при заполнении отчёта", e);
            throw new Exception("Ошибка при формировании отчёта ", e);
        }
    }

    private void fildVedomostZPlataT4NewByPersonalNumver(XComponent currentDocument) throws Exception {
        try {

            String nColBV_;
            String nColBVIt_;
            String nColOtD_;
            String nColOtCl_;
            String nColVD_;
            String nColVCl_;
            String nColTD_;
            String nColTCl_;
            String nColSrD_;
            String nColSrCl_;
            String nColUch_;
            String nColDK_;
            String nColNes_;
            String nColOt_;
            String nColB_;
            String nColPr_;
            String nColCO_;
            String nColDm_;
            String nColMk_;
            String nColOsP_;
            String staffSrt;
            String historySrt;

            double vyrabotka;
            double tmpVyrabotka;
            double value;

            int nCol;
            int nColDept;
            int nColBrig;
            int nColPeriod;
            int nRow;
            int nRowBrak;
            int k;

            int nColBV;
            int nColBVIt;
            int nColBVP;
            int nColStoim;
            int nColTVCl;
            int nColTNCl;
            int nColTNVr;
            int nColOtD;
            int nColOtCl;
            int nColXV;
            int nColVD;
            int nColVCl;
            int nColTD;
            int nColTCl;
            int nColSrD;
            int nColSrCl;
            int nColVr;
            int nColUmT;
            int nColUvT;
            int nColUch;
            int nColDK;
            int nColNes;
            int nColT_D;
            int nColT_Cl;
            int nColO;
            int nColB;
            int nColPr;
            int nColO_;
            int nColDm;
            int nColMk;
            int nColOsP;

            int nColAll;
            int nRowEmpl;
            int nRowBrig;

            int allCl;
            int allD;
            int allVD;
            int allVCl;
            int allTD;
            int allTCl;
            int allVrCl;
            int allT_D;
            int allT_Cl;
            int allSrD;
            int allSrCl;
            int allOD;
            int allBD;
            int allPrD;
            int allO_D;
            int allUch;
            int allDK;
            int allNes;
            int allDm;
            int allMk;
            int allZ;

            int tmp;

            Vector dataPeredanoDK;
            Vector dataPeredanoDKVTO;
            Vector colData;

            XSpreadsheetDocument xSpreadsheetDocument = UnoRuntime.queryInterface(XSpreadsheetDocument.class, currentDocument);
            XSpreadsheets xSpreadsheets = xSpreadsheetDocument.getSheets();
            Object sheet = xSpreadsheets.getByName("Лист1");
            XSpreadsheet xSpreadsheet = UnoRuntime.queryInterface(XSpreadsheet.class, sheet);
            Object sheet_2 = xSpreadsheets.getByName("Лист2");
            XSpreadsheet xSpreadsheet_2 = UnoRuntime.queryInterface(XSpreadsheet.class, sheet_2);
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

            //------------------------ Лист 2 ----------------------------
            //---------------- Ставка 1-го разряда -----------------------

            xCell = xSpreadsheet_2.getCellByPosition(4, 1);
            xCell.setValue(UtilZPlata.RATE_1ST_CATEGORY);

            xCell = xSpreadsheet_2.getCellByPosition(4, 16);
            xCell.setFormula(UtilZPlata.STAFF_1);

            xCell = xSpreadsheet_2.getCellByPosition(4, 17);
            xCell.setFormula(UtilZPlata.STAFF_2);

            //------------------------ Лист 1 ----------------------------
            //---------------- Заполнение шапки документа ----------------

            nColDept = 4;
            nColBrig = 14;
            nColPeriod = 19;

            xCell = xSpreadsheet.getCellByPosition(nColDept, 4);
            xCell.setFormula(data.get(0).toString().toLowerCase());

            xCell = xSpreadsheet.getCellByPosition(nColBrig, 4);
            xCell.setFormula(data.get(1).toString().toLowerCase());

            xCell = xSpreadsheet.getCellByPosition(nColPeriod, 4);
            xCell.setFormula(data.get(2).toString().toLowerCase());

            if (workSmena == 1) {
                nColBV_ = "AG";
                nColBVIt_ = "AH";
                nColOtD_ = "AN";
                nColOtCl_ = "AO";
                nColVD_ = "AQ";
                nColVCl_ = "AR";
                nColTD_ = "AS";
                nColTCl_ = "AT";
                nColSrD_ = "AU";
                nColSrCl_ = "AV";
                nColUch_ = "AZ";
                nColDK_ = "BA";
                nColNes_ = "BB";
                nColOt_ = "BE";
                nColB_ = "BF";
                nColPr_ = "BG";
                nColCO_ = "BH";
                nColDm_ = "BI";
                nColMk_ = "BJ";
                nColOsP_ = "BK";

                nColBV = 32;
                nColBVIt = 33;
                nColBVP = 34;
                nColStoim = 35;
                nColTVCl = 36;
                nColTNCl = 37;
                nColTNVr = 38;
                nColOtD = 39;
                nColOtCl = 40;
                nColXV = 41;
                nColVD = 42;
                nColVCl = 43;
                nColTD = 44;
                nColTCl = 45;
                nColSrD = 46;
                nColSrCl = 47;
                nColVr = 48;
                nColUmT = 49;
                nColUvT = 50;
                nColUch = 51;
                nColDK = 52;
                nColNes = 53;
                nColT_D = 54;
                nColT_Cl = 55;
                nColO = 56;
                nColB = 57;
                nColPr = 58;
                nColO_ = 59;
                nColDm = 60;
                nColMk = 61;
                nColOsP = 62;

                nColAll = 64;
            } else {
                for (int i = 0; i < 2; i++) {
                    nColDept += 28;
                    nColBrig += 28;
                    nColPeriod += 28;

                    xCell = xSpreadsheet.getCellByPosition(nColDept, 4);
                    xCell.setFormula(data.get(0).toString().toLowerCase());

                    xCell = xSpreadsheet.getCellByPosition(nColBrig, 4);
                    xCell.setFormula(data.get(1).toString().toLowerCase());

                    xCell = xSpreadsheet.getCellByPosition(nColPeriod, 4);
                    xCell.setFormula(data.get(2).toString().toLowerCase());
                }

                nColBV_ = "CK";
                nColBVIt_ = "CL";
                nColOtD_ = "CR";
                nColOtCl_ = "CS";
                nColVD_ = "CU";
                nColVCl_ = "CV";
                nColTD_ = "CW";
                nColTCl_ = "CX";
                nColSrD_ = "CY";
                nColSrCl_ = "CZ";
                nColUch_ = "DD";
                nColDK_ = "DE";
                nColNes_ = "DF";
                nColOt_ = "DI";
                nColB_ = "DJ";
                nColPr_ = "DK";
                nColCO_ = "DL";
                nColDm_ = "DM";
                nColMk_ = "DN";
                nColOsP_ = "DO";

                nColBV = 88;
                nColBVIt = 89;
                nColBVP = 90;
                nColStoim = 91;
                nColTVCl = 92;
                nColTNCl = 93;
                nColTNVr = 94;
                nColOtD = 95;
                nColOtCl = 96;
                nColXV = 97;
                nColVD = 98;
                nColVCl = 99;
                nColTD = 100;
                nColTCl = 101;
                nColSrD = 102;
                nColSrCl = 103;
                nColVr = 104;
                nColUmT = 105;
                nColUvT = 106;
                nColUch = 107;
                nColDK = 108;
                nColNes = 109;
                nColT_D = 110;
                nColT_Cl = 111;
                nColO = 112;
                nColB = 113;
                nColPr = 114;
                nColO_ = 115;
                nColDm = 116;
                nColMk = 117;
                nColOsP = 118;

                nColAll = 120;
            }

            int[] num = {0, 1, 2, 3, nColBV, nColDK, nColNes};

            xCell = xSpreadsheet.getCellByPosition(nColBV, 4);
            xCell.setFormula(data.get(0).toString().toLowerCase());

            xCell = xSpreadsheet.getCellByPosition(nColTNVr, 4);
            xCell.setFormula(data.get(1).toString().toLowerCase());

            xCell = xSpreadsheet.getCellByPosition(nColXV, 4);
            xCell.setFormula(data.get(2).toString().toLowerCase());

            xCell = xSpreadsheet.getCellByPosition(nColUch, 4);
            xCell.setFormula(data.get(0).toString().toLowerCase());

            xCell = xSpreadsheet.getCellByPosition(nColB, 4);
            xCell.setFormula(data.get(1).toString().toLowerCase());

            xCell = xSpreadsheet.getCellByPosition(nColDm, 4);
            xCell.setFormula(data.get(2).toString().toLowerCase());

            //------------------------------Рабочие дни

            nCol = 4;
            nRow = 7;

            for (int i = 0; i < workDays.size(); i++) {
                for (int j = 0; j < workSmena; j++) {
                    xCell = xSpreadsheet.getCellByPosition(nCol, nRow);
                    xCell.setValue(j + 1);

                    xCell = xSpreadsheet.getCellByPosition(nCol, nRow + 1);
                    xCell.setValue(8);

                    xCell = xSpreadsheet.getCellByPosition(nCol++, nRow + 2);
                    xCell.setFormula(String.valueOf(workDays.get(i)));
                }
            }

            //----------------Заполнение тела документа

            //------------------------------Выработка за месяц (бригада, др. конвеера)

            nRow = 11;
            k = 0;

            //brigVyrabatka = 0;
            dataPeredanoDK = new Vector();
            for (Object row : dataElement) {
                if (((Vector) row).get(7).toString().trim().equals(String.valueOf(idBrig))) {
                    k = 0;
                    for (int i = 0; i < nColAll; i++) {
                        xCell = xSpreadsheet.getCellByPosition(i, nRow);
                        xPropSet = UnoRuntime.queryInterface(XPropertySet.class, xCell);
                        xPropSet.setPropertyValue("TableBorder", aBorder);
                        if (k < num.length) {
                            if (i == num[k]) {
                                if (num[k] == nColBV) {
                                    xCell.setValue(Double.valueOf(UtilZPlata.formatNorm(Double.valueOf(((Vector) row).get(k).toString()), 2)));
                                } else if (num[k] == nColDK) {
                                    xCell.setValue(Double.valueOf(UtilZPlata.formatNorm(Double.valueOf(((Vector) row).get(k).toString()), 2)));
                                } else if (num[k] == nColNes) {
                                    xCell.setValue(Double.valueOf(UtilZPlata.formatNorm(Double.valueOf(((Vector) row).get(k).toString()), 2)));
                                } else if (num[k] == 1) {
                                    xCell.setFormula(((Vector) row).get(k).toString() + " " + ((Vector) row).get(8).toString());
                                } else {
                                    xCell.setFormula(((Vector) row).get(k).toString());
                                }
                                k++;
                            }
                        }
                        if (i == nColBVIt)
                            xCell.setFormula("=IF(" + nColUch_ + (nRow + 1) + ">0;"
                                    + "(" + nColBV_ + (nRow + 1) + "+" + nColDK_ + (nRow + 1) + "+" + nColNes_ + (nRow + 1) + "-IF(" + nColOsP_ + (nRow + 1) + "<>\"\";" + nColOsP_ + (nRow + 1) + ";0))*" + nColUch_ + (nRow + 1) + ";"
                                    + nColBV_ + (nRow + 1) + "+" + nColDK_ + (nRow + 1) + "+" + nColNes_ + (nRow + 1) + "-IF(" + nColOsP_ + (nRow + 1) + "<>\"\";" + nColOsP_ + (nRow + 1) + ";0))");
                        if (i == nColBVP)
                            xCell.setFormula("=IF(" + nColOtCl_ + (nRow + 1) + "=0;\"-\";(" + nColBVIt_ + (nRow + 1) + ")/" + nColOtCl_ + (nRow + 1) + ")");
                        if (i == nColStoim)
                            xCell.setFormula("=ROUND(IF(D" + (nRow + 1) + "=1;ROUND(1*Лист2.E2/169;2)*1.5;"
                                    + "IF(D" + (nRow + 1) + "=2;ROUND(1.16*Лист2.E2/169;2)*1.5;"
                                    + "IF(D" + (nRow + 1) + "=3;ROUND(1.35*Лист2.E2/169;2)*1.5;"
                                    + "IF(D" + (nRow + 1) + "=4;ROUND(1.57*Лист2.E2/169;2)*1.5;"
                                    + "IF(D" + (nRow + 1) + "=5;ROUND(1.73*Лист2.E2/169;2)*1.5;"
                                    + "IF(D" + (nRow + 1) + "=6;ROUND(1.9*Лист2.E2/169;2)*1.5;"
                                    + "IF(D" + (nRow + 1) + "=7;ROUND(2.03*Лист2.E2/169;2)*1.5;\"\")))))));2)");
                        if (i == nColOsP)
                            xCell.setFormula(
                                    "=IF(" + nColBV_ + (nRow + 1) + ">=0;"
                                            + "(IF(C" + (nRow + 1) + "=\"" + UtilZPlata.OTCHET_PROF_OSVOENIE + "\";"
                                            + nColBV_ + (nRow + 1) + "+" + nColDK_ + (nRow + 1) + ";"
                                            + "0))"
                                            + ";\"\")"
                            );
                    }
                    nRow++;
                } else {
                    dataPeredanoDK.add(row);
                }
            }

            nRowBrig = nRow;

            //------------------------------ВТО
            dataPeredanoDKVTO = new Vector();
            if (elements2.size() > 0) {
                xCell = xSpreadsheet.getCellByPosition(1, nRow);
                xCell.setFormula("ВТО");
            }
            nRow++;

            for (Object row : elements2) {
                if (((Vector) row).get(7).toString().trim().equals(String.valueOf(idBrig))) {
                    k = 0;
                    for (int i = 0; i < nColAll; i++) {
                        xCell = xSpreadsheet.getCellByPosition(i, nRow);
                        xPropSet = UnoRuntime.queryInterface(XPropertySet.class, xCell);
                        xPropSet.setPropertyValue("TableBorder", aBorder);
                        if (k < num.length) {
                            if (i == num[k]) {
                                if (num[k] == nColBV) {
                                    xCell.setValue(Double.valueOf(UtilZPlata.formatNorm(Double.valueOf(((Vector) row).get(k).toString()), 2)));
                                } else if (num[k] == nColDK) {
                                    xCell.setValue(Double.valueOf(UtilZPlata.formatNorm(Double.valueOf(((Vector) row).get(k).toString()), 2)));
                                } else if (num[k] == nColNes) {
                                    xCell.setValue(Double.valueOf(UtilZPlata.formatNorm(Double.valueOf(((Vector) row).get(k).toString()), 2)));
                                } else if (num[k] == 1) {
                                    xCell.setFormula(((Vector) row).get(k).toString() + " " + ((Vector) row).get(8).toString());
                                } else {
                                    xCell.setFormula(((Vector) row).get(k).toString());
                                }
                                k++;
                            }
                        }
                        if (i == nColBVIt)
                            xCell.setFormula("=IF(" + nColUch_ + (nRow + 1) + ">0;"
                                    + "(" + nColBV_ + (nRow + 1) + "+" + nColDK_ + (nRow + 1) + "+" + nColNes_ + (nRow + 1) + "-IF(" + nColOsP_ + (nRow + 1) + "<>\"\";" + nColOsP_ + (nRow + 1) + ";0))*" + nColUch_ + (nRow + 1) + ";"
                                    + nColBV_ + (nRow + 1) + "+" + nColDK_ + (nRow + 1) + "+" + nColNes_ + (nRow + 1) + "-IF(" + nColOsP_ + (nRow + 1) + "<>\"\";" + nColOsP_ + (nRow + 1) + ";0))");
                        if (i == nColBVP)
                            xCell.setFormula("=IF(" + nColOtCl_ + (nRow + 1) + "=0;\"-\";(" + nColBVIt_ + (nRow + 1) + ")/" + nColOtCl_ + (nRow + 1) + ")");
                        if (i == nColStoim)
                            xCell.setFormula("=ROUND(IF(D" + (nRow + 1) + "=1;ROUND(1*Лист2.E2/169;2)*1.5;"
                                    + "IF(D" + (nRow + 1) + "=2;ROUND(1.16*Лист2.E2/169;2)*1.5;"
                                    + "IF(D" + (nRow + 1) + "=3;ROUND(1.35*Лист2.E2/169;2)*1.5;"
                                    + "IF(D" + (nRow + 1) + "=4;ROUND(1.57*Лист2.E2/169;2)*1.5;"
                                    + "IF(D" + (nRow + 1) + "=5;ROUND(1.73*Лист2.E2/169;2)*1.5;"
                                    + "IF(D" + (nRow + 1) + "=6;ROUND(1.9*Лист2.E2/169;2)*1.5;"
                                    + "IF(D" + (nRow + 1) + "=7;ROUND(2.03*Лист2.E2/169;2)*1.5;\"\")))))));2)");
                        if (i == nColOsP)
                            xCell.setFormula(
                                    "=IF(" + nColBV_ + (nRow + 1) + ">=0;"
                                            + "(IF(C" + (nRow + 1) + "=\"" + UtilZPlata.OTCHET_PROF_OSVOENIE + "\";"
                                            + nColBV_ + (nRow + 1) + "+" + nColDK_ + (nRow + 1) + ";"
                                            + "0))"
                                            + ";\"\")"
                            );
                    }
                    nRow++;
                } else {
                    dataPeredanoDKVTO.add(row);
                }
            }

            //------------------------------Передано на др. конвейера

            if (dataPeredanoDKVTO.size() > 0) {
                int nRowDKVTO = nRow;
                nRow++;

                for (Object row : dataPeredanoDKVTO) {
                    k = 0;
                    for (int i = 0; i < nColAll; i++) {
                        xCell = xSpreadsheet.getCellByPosition(i, nRow);
                        xPropSet = UnoRuntime.queryInterface(XPropertySet.class, xCell);
                        xPropSet.setPropertyValue("TableBorder", aBorder);
                        if (k < num.length) {
                            if (i == num[k]) {
                                if (num[k] == nColBV) {
                                    xCell.setValue(Double.valueOf(UtilZPlata.formatNorm(Double.valueOf(((Vector) row).get(k).toString()), 2)));
                                } else if (num[k] == nColDK) {
                                    xCell.setValue(0);
                                } else if (num[k] == nColNes) {
                                    xCell.setValue(0);
                                } else if (num[k] == 1) {
                                    xCell.setFormula("(" + ((Vector) row).get(7).toString() + ")" + ((Vector) row).get(k).toString());
                                } else {
                                    xCell.setFormula(((Vector) row).get(k).toString());
                                }
                                k++;
                            }
                        }
                        if (i == nColBVIt)
                            xCell.setFormula("=" + nColBV_ + (nRow + 1) + "+" + nColDK_ + (nRow + 1) + "+" + nColNes_ + (nRow + 1) + "-IF(" + nColOsP_ + (nRow + 1) + "<>\"\";" + nColOsP_ + (nRow + 1) + ";0)");
                    }
                    nRow++;
                }

                if (nRowDKVTO + 1 != nRow) {
                    xCell = xSpreadsheet.getCellByPosition(1, nRowDKVTO);
                    xCell.setFormula("Передано на ДК ВТО");
                }

            }

            if (dataPeredanoDK.size() > 0) {
                int nRowDK = nRow;
                nRow++;

                for (Object row : dataPeredanoDK) {
                    if (Double.valueOf(UtilZPlata.formatNorm(Double.valueOf(((Vector) row).get(4).toString()), 2)) != 0) {
                        k = 0;
                        for (int i = 0; i < nColAll; i++) {
                            xCell = xSpreadsheet.getCellByPosition(i, nRow);
                            xPropSet = UnoRuntime.queryInterface(XPropertySet.class, xCell);
                            xPropSet.setPropertyValue("TableBorder", aBorder);
                            if (k < num.length) {
                                if (i == num[k]) {
                                    if (num[k] == nColBV) {
                                        xCell.setValue(Double.valueOf(UtilZPlata.formatNorm(Double.valueOf(((Vector) row).get(k).toString()), 2)));
                                    } else if (num[k] == nColDK) {
                                        xCell.setValue(0);
                                    } else if (num[k] == nColNes) {
                                        xCell.setValue(0);
                                    } else if (num[k] == 1) {
                                        xCell.setFormula("(" + ((Vector) row).get(7).toString() + ")" + ((Vector) row).get(k).toString());
                                    } else {
                                        xCell.setFormula(((Vector) row).get(k).toString());
                                    }
                                    k++;
                                }
                            }
                            if (i == nColBVIt)
                                xCell.setFormula("=" + nColBV_ + (nRow + 1) + "+" + nColDK_ + (nRow + 1) + "+" + nColNes_ + (nRow + 1) + "-IF(" + nColOsP_ + (nRow + 1) + "<>\"\";" + nColOsP_ + (nRow + 1) + ";0)");
                        }
                        nRow++;
                    }
                }

                if (nRowDK + 1 != nRow) {
                    xCell = xSpreadsheet.getCellByPosition(1, nRowDK);
                    xCell.setFormula("Передано на ДК");
                }
            }

            nRowBrak = nRow;

            //------------------------------Табель
            if (JOptionPane.showOptionDialog(null, "Заполните все прогулы, б/листы, отпуска и т.д.\n"
                            + "Далее нажмите кнопку 'Продолжить' для завершения\n"
                            + "формирования отчёта. ", "Внимание!",
                    JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, null,
                    new Object[]{"Продолжить", "Отмена"}, "Продолжить") == JOptionPane.YES_OPTION) {

                nRow = 10;
                nRowEmpl = 11;
                historySrt = "";
                for (int l = 0; l < dataElement.size() + elements2.size() + 1 - dataPeredanoDK.size() - dataPeredanoDKVTO.size(); l++) {
                    nRow++;

                    staffSrt = "";
                    vyrabotka = 0;

                    allCl = 0;
                    allD = 0;
                    allVD = 0;
                    allVCl = 0;
                    allTD = 0;
                    allTCl = 0;
                    allVrCl = 0;
                    allT_D = 0;
                    allT_Cl = 0;
                    allSrD = 0;
                    allSrCl = 0;
                    allOD = 0;
                    allBD = 0;
                    allPrD = 0;
                    allO_D = 0;
                    allUch = 0;
                    allDK = 0;
                    allNes = 0;
                    allDm = 0;
                    allMk = 0;
                    allZ = 0;

                    //------------------------------запоминаем колонки которые рабочие дни
                    //-и подсчитывает кол-во б/лист, отпуск, свой счёт,средний, 2/3 тарифа

                    colData = new Vector();
                    for (int i = 4; i < nColBV; i++) {
                        xCell = xSpreadsheet.getCellByPosition(i, 9);
                        if (!xCell.getFormula().equals("")) {
                            xCell = xSpreadsheet.getCellByPosition(i, nRow);
                            if (xCell.getFormula().equals("")) {
                                colData.add(i);

                                xCell = xSpreadsheet.getCellByPosition(i, 8);
                                allCl += xCell.getValue();
                                allD += 1;

                            } else if (xCell.getFormula().toLowerCase().trim().equals("б")) {
                                allBD += 1;
                            } else if (xCell.getFormula().toLowerCase().trim().equals("о") || xCell.getFormula().toLowerCase().trim().equals("o")) {
                                allOD += 1;
                            } else if (xCell.getFormula().toLowerCase().trim().equals("со") || xCell.getFormula().toLowerCase().trim().equals("co") ||
                                    xCell.getFormula().toLowerCase().trim().equals("а") || xCell.getFormula().toLowerCase().trim().equals("a") ||
                                    xCell.getFormula().toLowerCase().trim().equals("у") || xCell.getFormula().toLowerCase().trim().equals("y") ||
                                    xCell.getFormula().toLowerCase().trim().equals("св") || xCell.getFormula().toLowerCase().trim().equals("cb")) {
                                allO_D += 1;
                            } else if (xCell.getFormula().toLowerCase().trim().equals("п")) {
                                allPrD += 1;
                            } else if (xCell.getFormula().toLowerCase().trim().equals("в") || xCell.getFormula().toLowerCase().trim().equals("b")) {
                                allVD += 1;
                                xCell = xSpreadsheet.getCellByPosition(i, 8);
                                allVCl += xCell.getValue();
                            } else if (xCell.getFormula().toLowerCase().trim().equals("т") || xCell.getFormula().toLowerCase().trim().equals("t")) {
                                allTD += 1;
                                xCell = xSpreadsheet.getCellByPosition(i, 8);
                                allTCl += xCell.getValue();

                                xCell = xSpreadsheet.getCellByPosition(i, 7);
                                if (xCell.getFormula().equals("2") ||
                                        xCell.getFormula().toLowerCase().trim().equals("2х") ||
                                        xCell.getFormula().toLowerCase().trim().equals("2x")) {
                                    xCell = xSpreadsheet.getCellByPosition(nColTVCl, nRow);
                                    xCell.setValue(xCell.getValue() + 1.5);
                                }

                            } else if (xCell.getFormula().toLowerCase().trim().equals("т*") || xCell.getFormula().toLowerCase().trim().equals("t*")) {
                                allT_D += 1;
                                xCell = xSpreadsheet.getCellByPosition(i, 8);
                                allT_Cl += xCell.getValue();
                            } else if (xCell.getFormula().toLowerCase().trim().equals("ср") || xCell.getFormula().toLowerCase().trim().equals("cp")) {
                                allSrD += 1;
                                xCell = xSpreadsheet.getCellByPosition(i, 8);
                                allSrCl += xCell.getValue();
                            } else if (xCell.getFormula().toLowerCase().trim().equals("вр") || xCell.getFormula().toLowerCase().trim().equals("bp")) {
                                xCell = xSpreadsheet.getCellByPosition(i, 8);
                                allVrCl += xCell.getValue();
                            } else if (xCell.getFormula().toLowerCase().trim().equals("дм") || xCell.getFormula().toLowerCase().trim().equals("dm")) {
                                allDm += 1;
                            } else if (xCell.getFormula().toLowerCase().trim().equals("мк") || xCell.getFormula().toLowerCase().trim().equals("mk")) {
                                allMk += 1;
                            }
                        }
                    }

                    //------------------------------выработка по рабочим дням

                    xCell = xSpreadsheet.getCellByPosition(nColBVIt, nRow);
                    if (!xCell.getFormula().equals("")) {
                        vyrabotka = xCell.getValue();
                        tmpVyrabotka = 0;

                        for (int i = 0; i < colData.size() - 1; i++) {
                            tmp = 0;

                            xCell = xSpreadsheet.getCellByPosition(Integer.valueOf(colData.get(i).toString()), nRow);
                            xCell.setValue(Double.valueOf(UtilZPlata.formatNorm(Double.valueOf(vyrabotka / colData.size()), 2)));
                            tmpVyrabotka += Double.valueOf(UtilZPlata.formatNorm(Double.valueOf(vyrabotka / colData.size()), 2));

                            xCell = xSpreadsheet.getCellByPosition(Integer.valueOf(colData.get(i).toString()), 7);
                            if (xCell.getFormula().equals("2") || xCell.getFormula().toLowerCase().trim().equals("2х") || xCell.getFormula().toLowerCase().trim().equals("2x")) {
                                xCell = xSpreadsheet.getCellByPosition(nColTVCl, nRow);
                                xCell.setValue(xCell.getValue() + 1.5);

                            } else if (xCell.getFormula().equals("3")) {
                                xCell = xSpreadsheet.getCellByPosition(Integer.valueOf(colData.get(i).toString()), 8);
                                tmp += xCell.getValue();

                                xCell = xSpreadsheet.getCellByPosition(nColTNCl, nRow);
                                xCell.setValue(xCell.getValue() + tmp);

                                xCell = xSpreadsheet.getCellByPosition(nColTNVr, nRow);
                                xCell.setValue(xCell.getValue() + Double.valueOf(UtilZPlata.formatNorm(Double.valueOf(vyrabotka / colData.size()), 2)));

                            }

                            xCell = xSpreadsheet.getCellByPosition(Integer.valueOf(colData.get(i).toString()), 7);
                            if (xCell.getFormula().toLowerCase().trim().equals("х") || xCell.getFormula().toLowerCase().trim().equals("x") ||
                                    xCell.getFormula().toLowerCase().trim().equals("1х") || xCell.getFormula().toLowerCase().trim().equals("1x") ||
                                    xCell.getFormula().toLowerCase().trim().equals("2х") || xCell.getFormula().toLowerCase().trim().equals("2x")) {
                                xCell = xSpreadsheet.getCellByPosition(nColXV, nRow);
                                xCell.setValue(xCell.getValue() + Double.valueOf(UtilZPlata.formatNorm(Double.valueOf(vyrabotka / colData.size()), 2)));
                            }
                        }

                        tmp = 0;
                        value = 0;

                        xCell = xSpreadsheet.getCellByPosition(nColBVIt, nRow);
                        value = xCell.getValue();

                        if (colData.size() > 0) {
                            xCell = xSpreadsheet.getCellByPosition(Integer.valueOf(colData.get(colData.size() - 1).toString()), nRow);
                            xCell.setValue(Double.valueOf(UtilZPlata.formatNorm(value - tmpVyrabotka, 2)));

                            xCell = xSpreadsheet.getCellByPosition(Integer.valueOf(colData.get(colData.size() - 1).toString()), 7);
                            if (xCell.getFormula().equals("2") || xCell.getFormula().toLowerCase().trim().equals("2х") || xCell.getFormula().toLowerCase().trim().equals("2x")) {
                                xCell = xSpreadsheet.getCellByPosition(nColTVCl, nRow);
                                xCell.setValue(xCell.getValue() + 1.5);

                            } else if (xCell.getFormula().equals("3")) {
                                xCell = xSpreadsheet.getCellByPosition(Integer.valueOf(colData.get(colData.size() - 1).toString()), 8);
                                tmp += xCell.getValue();

                                xCell = xSpreadsheet.getCellByPosition(nColTNCl, nRow);
                                xCell.setValue(xCell.getValue() + tmp);

                                xCell = xSpreadsheet.getCellByPosition(nColTNVr, nRow);
                                xCell.setValue(xCell.getValue() + Double.valueOf(UtilZPlata.formatNorm(value - tmpVyrabotka, 2)));

                            }

                            xCell = xSpreadsheet.getCellByPosition(Integer.valueOf(colData.get(colData.size() - 1).toString()), 7);
                            if (xCell.getFormula().toLowerCase().trim().equals("х") || xCell.getFormula().toLowerCase().trim().equals("x") ||
                                    xCell.getFormula().toLowerCase().trim().equals("1х") || xCell.getFormula().toLowerCase().trim().equals("1x") ||
                                    xCell.getFormula().toLowerCase().trim().equals("2х") || xCell.getFormula().toLowerCase().trim().equals("2x")) {
                                xCell = xSpreadsheet.getCellByPosition(nColXV, nRow);
                                xCell.setValue(xCell.getValue() + Double.valueOf(UtilZPlata.formatNorm(value - tmpVyrabotka, 2)));
                            }
                        }

                        //------------------------------отработанное время

                        xCell = xSpreadsheet.getCellByPosition(nColOtD, nRow);
                        xCell.setValue(allD);

                        xCell = xSpreadsheet.getCellByPosition(nColOtCl, nRow);
                        xCell.setValue(allCl);

                        //------------------------------Внедрение

                        xCell = xSpreadsheet.getCellByPosition(nColVD, nRow);
                        xCell.setValue(allVD);

                        xCell = xSpreadsheet.getCellByPosition(nColVCl, nRow);
                        xCell.setValue(allVCl);

                        //------------------------------Тариф

                        xCell = xSpreadsheet.getCellByPosition(nColTD, nRow);
                        xCell.setValue(allTD);

                        xCell = xSpreadsheet.getCellByPosition(nColTCl, nRow);
                        xCell.setValue(allTCl);


                        //------------------------------Средний

                        xCell = xSpreadsheet.getCellByPosition(nColSrD, nRow);
                        xCell.setFormula("=IF(C" + (nRow + 1) + "=\"" + UtilZPlata.OTCHET_PROF_OSVOENIE + "\";" + nColOtD_ + (nRow + 1) + ";" + (allSrD > 0 ? allSrD : "\"\"") + ")");
                        //xCell.setValue(allSrD);

                        xCell = xSpreadsheet.getCellByPosition(nColSrCl, nRow);
                        xCell.setFormula("=IF(C" + (nRow + 1) + "=\"" + UtilZPlata.OTCHET_PROF_OSVOENIE + "\";" + nColOtCl_ + (nRow + 1) + ";" + (allSrCl > 0 ? allSrCl : "\"\"") + ")");
                        //xCell.setValue(allSrCl);

                        //------------------------------Вредность

                        xCell = xSpreadsheet.getCellByPosition(nColVr, nRow);
                        xCell.setValue(allVrCl);

                        //------------------------------2/3 тарифа

                        xCell = xSpreadsheet.getCellByPosition(nColT_D, nRow);
                        xCell.setValue(allT_D);

                        xCell = xSpreadsheet.getCellByPosition(nColT_Cl, nRow);
                        xCell.setValue(allT_Cl);

                        //------------------------------Отпуск

                        xCell = xSpreadsheet.getCellByPosition(nColO, nRow);
                        xCell.setValue(allOD);

                        //------------------------------Б/лист

                        xCell = xSpreadsheet.getCellByPosition(nColB, nRow);
                        xCell.setValue(allBD);

                        //------------------------------Перевод

                        xCell = xSpreadsheet.getCellByPosition(nColPr, nRow);
                        xCell.setValue(allPrD);

                        //------------------------------Соц. отпуск

                        xCell = xSpreadsheet.getCellByPosition(nColO_, nRow);
                        xCell.setValue(allO_D);

                        //------------------------------День матери

                        xCell = xSpreadsheet.getCellByPosition(nColDm, nRow);
                        xCell.setValue(allDm);

                        //------------------------------Мед.комиссия

                        xCell = xSpreadsheet.getCellByPosition(nColMk, nRow);
                        xCell.setValue(allMk);

                        //------------------------------Процент выполнения

                        xCell = xSpreadsheet.getCellByPosition(0, nRow);            // табельный
                        staffSrt = xCell.getFormula().trim();

                        xCell = xSpreadsheet.getCellByPosition(1, nRow);
                        //staffSrt = staffSrt + xCell.getFormula().trim();               // ФИО, присв. разряд

                        if (l == 0) historySrt = staffSrt;

                        if (!staffSrt.equals(historySrt)) {
                            nRowEmpl = nRow;
                            historySrt = staffSrt;
                        } else {
                            xCell = xSpreadsheet.getCellByPosition(nColBVP, nRow);
                            xCell.setFormula("=IF(" + nColOtCl_ + (nRow + 1) + "=0;\"-\";"
                                    + "(SUM(" + nColBVIt_ + (nRowEmpl + 1) + ":" + nColBVIt_ + (nRow + 1) + "))/" + nColOtCl_ + (nRow + 1) + ")");

                            for (int i = nColTVCl; i < nColMk + 1; i++) {
                                if (nRow != 11) {
                                    if (i != nColXV && i != nColUmT && i != nColUvT && i != nColDK && i != nColNes && i != nColUch) {
                                        xCell = xSpreadsheet.getCellByPosition(i, nRow - 1);
                                        xCell.setFormula("");
                                    }
                                }
                            }
                        }

                        for (int i = nColTVCl; i < nColMk + 1; i++) {
                            //  if(nRow != 11){
                            if (i != nColXV && i != nColUmT && i != nColUvT && i != nColDK && i != nColNes && i != nColUch) {
                                xCell = xSpreadsheet.getCellByPosition(i, nRow);
                                if (xCell.getFormula().equals("0"))
                                    xCell.setFormula("");
                            }
                            //    }
                        }
                    }
                }

                nRow = nRowBrak;
                nRow += 2;
                for (int j = 0; j < 2; j++) {
                    for (int i = 0; i < nColAll; i++) {
                        xCell = xSpreadsheet.getCellByPosition(i, nRow);
                        xPropSet = UnoRuntime.queryInterface(XPropertySet.class, xCell);
                        xPropSet.setPropertyValue("TableBorder", aBorder);
                        if (i == 1) {
                            xCell.setFormula(j == 0 ? "Брак" : "ИТОГО");
                        } else if (i == nColBV) {
                            xCell.setValue(j == 0 ? Double.valueOf(UtilZPlata.formatNorm(brak, 2)) : 0);
                        }
                    }
                    nRow++;
                }

                //------------------------------ИТОГО

                //------------------------------Бригад. выработка
                xCell = xSpreadsheet.getCellByPosition(nColBV, nRow - 1);
                xCell.setFormula("=SUM(" + nColBV_ + 12 + ":" + nColBV_ + (nRow - 1) + ")");

                // Итого по выработке
                xCell = xSpreadsheet.getCellByPosition(nColBVIt, nRow - 1);
                xCell.setFormula("=SUM(" + nColBVIt_ + 12 + ":" + nColBVIt_ + (nRow - 1) + ")");

                //------------------------------Отработанное время

                xCell = xSpreadsheet.getCellByPosition(nColOtD, nRow - 1);
                xCell.setFormula("=SUM(" + nColOtD_ + 12 + ":" + nColOtD_ + (nRow - 1) + ")");

                xCell = xSpreadsheet.getCellByPosition(nColOtCl, nRow - 1);
                xCell.setFormula("=SUM(" + nColOtCl_ + 12 + ":" + nColOtCl_ + (nRow - 1) + ")");

                //------------------------------Внедрение

                xCell = xSpreadsheet.getCellByPosition(nColVD, nRow - 1);
                xCell.setFormula("=SUM(" + nColVD_ + 12 + ":" + nColVD_ + (nRow - 1) + ")");

                xCell = xSpreadsheet.getCellByPosition(nColVCl, nRow - 1);
                xCell.setFormula("=SUM(" + nColVCl_ + 12 + ":" + nColVCl_ + (nRow - 1) + ")");

                //------------------------------Тариф

                xCell = xSpreadsheet.getCellByPosition(nColTD, nRow - 1);
                xCell.setFormula("=SUM(" + nColTD_ + 12 + ":" + nColTD_ + (nRow - 1) + ")");

                xCell = xSpreadsheet.getCellByPosition(nColTCl, nRow - 1);
                xCell.setFormula("=SUM(" + nColTCl_ + 12 + ":" + nColTCl_ + (nRow - 1) + ")");

                //------------------------------Средний

                xCell = xSpreadsheet.getCellByPosition(nColSrD, nRow - 1);
                xCell.setFormula("=SUM(" + nColSrD_ + 12 + ":" + nColSrD_ + (nRow - 1) + ")");

                xCell = xSpreadsheet.getCellByPosition(nColSrCl, nRow - 1);
                xCell.setFormula("=SUM(" + nColSrCl_ + 12 + ":" + nColSrCl_ + (nRow - 1) + ")");

                //------------------------------Другие конвеера

                xCell = xSpreadsheet.getCellByPosition(nColDK, nRow - 1);
                xCell.setFormula("=SUM(" + nColDK_ + 12 + ":" + nColDK_ + (nRow - 1) + ")");

                //------------------------------Несорт. брак

                xCell = xSpreadsheet.getCellByPosition(nColNes, nRow - 1);
                xCell.setFormula("=SUM(" + nColNes_ + 12 + ":" + nColNes_ + (nRow - 1) + ")");

                //------------------------------Отпуск

                xCell = xSpreadsheet.getCellByPosition(nColO, nRow - 1);
                xCell.setFormula("=SUM(" + nColOt_ + 12 + ":" + nColOt_ + (nRow - 1) + ")");

                //------------------------------Б/лист

                xCell = xSpreadsheet.getCellByPosition(nColB, nRow - 1);
                xCell.setFormula("=SUM(" + nColB_ + 12 + ":" + nColB_ + (nRow - 1) + ")");

                //------------------------------Переход

                xCell = xSpreadsheet.getCellByPosition(nColPr, nRow - 1);
                xCell.setFormula("=SUM(" + nColPr_ + 12 + ":" + nColPr_ + (nRow - 1) + ")");

                //------------------------------Соц. отпуск

                xCell = xSpreadsheet.getCellByPosition(nColO_, nRow - 1);
                xCell.setFormula("=SUM(" + nColCO_ + 12 + ":" + nColCO_ + (nRow - 1) + ")");

                //------------------------------День матери

                xCell = xSpreadsheet.getCellByPosition(nColDm, nRow - 1);
                xCell.setFormula("=SUM(" + nColDm_ + 12 + ":" + nColDm_ + (nRow - 1) + ")");

                //------------------------------Мед.комиссия

                xCell = xSpreadsheet.getCellByPosition(nColMk, nRow - 1);
                xCell.setFormula("=SUM(" + nColMk_ + 12 + ":" + nColMk_ + (nRow - 1) + ")");

                //------------------------------Освоение профессии

                xCell = xSpreadsheet.getCellByPosition(nColOsP, nRow - 1);
                xCell.setFormula("=SUM(" + nColOsP_ + 12 + ":" + nColOsP_ + (nRow - 1) + ")");

                //------------------------------Итого выработка за месяц в н.-часах

                /*
                xCell = xSpreadsheet.getCellByPosition(nColBVIt, nRow-1);
                xCell.setFormula("="+nColBV_+nRow+"+"+nColDK_+nRow+"-"+nColOsP_+nRow);
                */

                //------------------------------Процент выполнения

                xCell = xSpreadsheet.getCellByPosition(nColBVP, nRow - 1);
                xCell.setFormula("=IF(" + nColOtCl_ + nRow + "=0;\"-\";(" + nColBVIt_ + nRow + ")/" + nColOtCl_ + nRow + ")");

                if (elements2.size() > 0) {
                    for (int i = 0; i < nColAll; i++) {
                        xCell = xSpreadsheet.getCellByPosition(i, nRow);
                        xPropSet = UnoRuntime.queryInterface(XPropertySet.class, xCell);
                        xPropSet.setPropertyValue("TableBorder", aBorder);
                        if (i == 1) {
                            xCell.setFormula("ИТОГО без ВТО и переданных на ДК");
                        } else if (i == nColBV) {
                            xCell.setFormula("=SUM(" + nColBV_ + 12 + ":" + nColBV_ + (nRowBrig) + ")");
                            //   xCell.setValue(Double.valueOf(UtilZPlata.formatNorm(brigVyrabatka,2)));
                        } else if (i == nColDK) {
                            xCell.setFormula("=SUM(" + nColDK_ + 12 + ":" + nColDK_ + (nRowBrig) + ")");
                        } else if (i == nColNes) {
                            xCell.setFormula("=SUM(" + nColNes_ + 12 + ":" + nColNes_ + (nRowBrig) + ")");
                        } else if (i == nColOtD) {
                            xCell.setFormula("=SUM(" + nColOtD_ + 12 + ":" + nColOtD_ + (nRowBrig) + ")");
                        } else if (i == nColOtCl) {
                            xCell.setFormula("=SUM(" + nColOtCl_ + 12 + ":" + nColOtCl_ + (nRowBrig) + ")");
                        } else if (i == nColBVIt) {
                            xCell.setFormula("=SUM(" + nColBVIt_ + 12 + ":" + nColBVIt_ + (nRowBrig) + ")");
                            //  xCell.setFormula("="+nColBV_+(nRow+1)+"+"+nColDK_+(nRow+1)+"-IF("+nColOsP_+(nRow+1)+"<>\"\";"+nColOsP_+(nRow+1)+";0)");
                        } else if (i == nColBVP) {
                            xCell.setFormula("=IF(" + nColOtCl_ + (nRow + 1) + "=0;\"-\";(" + nColBVIt_ + (nRow + 1) + ")/" + nColOtCl_ + (nRow + 1) + ")");
                        }
                    }
                } else if (dataPeredanoDK.size() > 0) {
                    for (int i = 0; i < nColAll; i++) {
                        xCell = xSpreadsheet.getCellByPosition(i, nRow);
                        xPropSet = UnoRuntime.queryInterface(XPropertySet.class, xCell);
                        xPropSet.setPropertyValue("TableBorder", aBorder);
                        if (i == 1) {
                            xCell.setFormula("ИТОГО без переданных на ДК");
                        } else if (i == nColBV) {
                            xCell.setFormula("=SUM(" + nColBV_ + 12 + ":" + nColBV_ + (nRowBrig) + ")");
                            //   xCell.setValue(Double.valueOf(UtilZPlata.formatNorm(brigVyrabatka,2)));
                        } else if (i == nColDK) {
                            xCell.setFormula("=SUM(" + nColDK_ + 12 + ":" + nColDK_ + (nRowBrig) + ")");
                        } else if (i == nColNes) {
                            xCell.setFormula("=SUM(" + nColNes_ + 12 + ":" + nColNes_ + (nRowBrig) + ")");
                        } else if (i == nColOtD) {
                            xCell.setFormula("=SUM(" + nColOtD_ + 12 + ":" + nColOtD_ + (nRowBrig) + ")");
                        } else if (i == nColOtCl) {
                            xCell.setFormula("=SUM(" + nColOtCl_ + 12 + ":" + nColOtCl_ + (nRowBrig) + ")");
                        } else if (i == nColBVIt) {
                            xCell.setFormula("=SUM(" + nColBVIt_ + 12 + ":" + nColBVIt_ + (nRowBrig) + ")");
                            //         xCell.setFormula("="+nColBV_+(nRow+1)+"+"+nColDK_+(nRow+1)+"-IF("+nColOsP_+(nRow+1)+"<>\"\";"+nColOsP_+(nRow+1)+";0)");
                        } else if (i == nColBVP) {
                            xCell.setFormula("=IF(" + nColOtCl_ + (nRow + 1) + "=0;\"-\";(" + nColBVIt_ + (nRow + 1) + ")/" + nColOtCl_ + (nRow + 1) + ")");
                        }
                    }
                }

            }

            //----------------Заполнение колонтитула документа

            String text =
                    UtilZPlata.PROF_STAFF_3 + " " + UtilZPlata.STAFF_3 + " __________________________" + "         "
                            + UtilZPlata.PROF_STAFF_4 + " " + UtilZPlata.STAFF_4 + " __________________________";

            insertTextInFooter(xSpreadsheetDocument, text);

        } catch (Exception e) {
            log.error("Ошибка при заполнении отчёта", e);
            throw new Exception("Ошибка при формировании отчёта ", e);
        }
    }

    private void fildVedomostZPlataT4NewByPersonalNumverForSewingWorkshop(XComponent currentDocument) throws Exception {
        try {

            String nColBV_;
            String nColBVIt_;
            String nColOtD_;
            String nColOtCl_;
            String nColVD_;
            String nColVCl_;
            String nColTD_;
            String nColTCl_;
            String nColSrD_;
            String nColSrCl_;
            String nColUch_;
            String nColDK_;
            String nColNes_;
            String nColOt_;
            String nColB_;
            String nColPr_;
            String nColCO_;
            String nColDm_;
            String nColMk_;
            String nColOsP_;
            String staffSrt;
            String historySrt;

            double vyrabotka;
            double tmpVyrabotka;
            double value;

            int nCol;
            int nColDept;
            int nColBrig;
            int nColPeriod;
            int nRow;
            int nRowBrak;
            int k;

            int nColBV;
            int nColBVIt;
            int nColBVP;
            int nColStoim;
            int nColPercent;
            int nColMinusPercent;
            int nColTVCl;
            int nColTNCl;
            int nColTNVr;
            int nColOtD;
            int nColOtCl;
            int nColXV;
            int nColVD;
            int nColVCl;
            int nColTD;
            int nColTCl;
            int nColSrD;
            int nColSrCl;
            int nColVr;
            int nColUmT;
            int nColUvT;
            int nColUch;
            int nColDK;
            int nColNes;
            int nColT_D;
            int nColT_Cl;
            int nColO;
            int nColB;
            int nColPr;
            int nColO_;
            int nColDm;
            int nColMk;
            int nColOsP;

            int nColAll;
            int nRowEmpl;
            int nRowBrig;

            int allCl;
            int allD;
            int allVD;
            int allVCl;
            int allTD;
            int allTCl;
            int allVrCl;
            int allT_D;
            int allT_Cl;
            int allSrD;
            int allSrCl;
            int allOD;
            int allBD;
            int allPrD;
            int allO_D;
            int allUch;
            int allDK;
            int allNes;
            int allDm;
            int allMk;
            int allZ;

            int tmp;

            Vector dataPeredanoDK;
            Vector dataPeredanoDKVTO;
            Vector colData;

            XSpreadsheetDocument xSpreadsheetDocument = UnoRuntime.queryInterface(XSpreadsheetDocument.class, currentDocument);
            XSpreadsheets xSpreadsheets = xSpreadsheetDocument.getSheets();
            Object sheet = xSpreadsheets.getByName("Лист1");
            XSpreadsheet xSpreadsheet = UnoRuntime.queryInterface(XSpreadsheet.class, sheet);
            Object sheet_2 = xSpreadsheets.getByName("Лист2");
            XSpreadsheet xSpreadsheet_2 = UnoRuntime.queryInterface(XSpreadsheet.class, sheet_2);
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

            //------------------------ Лист 2 ----------------------------
            //---------------- Ставка 1-го разряда -----------------------

            xCell = xSpreadsheet_2.getCellByPosition(4, 1);
            xCell.setValue(UtilZPlata.RATE_1ST_CATEGORY);

            xCell = xSpreadsheet_2.getCellByPosition(4, 16);
            xCell.setFormula(UtilZPlata.STAFF_1);

            xCell = xSpreadsheet_2.getCellByPosition(4, 17);
            xCell.setFormula(UtilZPlata.STAFF_2);

            //------------------------ Лист 1 ----------------------------
            //---------------- Заполнение шапки документа ----------------

            nColDept = 4;
            nColBrig = 14;
            nColPeriod = 19;

            xCell = xSpreadsheet.getCellByPosition(nColDept, 4);
            xCell.setFormula(data.get(0).toString().toLowerCase());

            xCell = xSpreadsheet.getCellByPosition(nColBrig, 4);
            xCell.setFormula(data.get(1).toString().toLowerCase());

            xCell = xSpreadsheet.getCellByPosition(nColPeriod, 4);
            xCell.setFormula(data.get(2).toString().toLowerCase());

            if (workSmena == 1) {
                nColBV_ = "AG";
                nColBVIt_ = "AH";
                nColOtD_ = "AN";
                nColOtCl_ = "AO";
                nColVD_ = "AQ";
                nColVCl_ = "AR";
                nColTD_ = "AS";
                nColTCl_ = "AT";
                nColSrD_ = "AU";
                nColSrCl_ = "AV";
                nColUch_ = "AZ";
                nColDK_ = "BA";
                nColNes_ = "BB";
                nColOt_ = "BE";
                nColB_ = "BF";
                nColPr_ = "BG";
                nColCO_ = "BH";
                nColDm_ = "BI";
                nColMk_ = "BJ";
                nColOsP_ = "BK";

                nColBV = 32;
                nColBVIt = 33;
                nColBVP = 34;
                nColStoim = 35;
                nColTVCl = 36;
                nColTNCl = 37;
                nColTNVr = 38;
                nColOtD = 39;
                nColOtCl = 40;
                nColXV = 41;
                nColVD = 42;
                nColVCl = 43;
                nColTD = 44;
                nColTCl = 45;
                nColSrD = 46;
                nColSrCl = 47;
                nColVr = 48;
                nColUmT = 49;
                nColUvT = 50;
                nColUch = 51;
                nColDK = 52;
                nColNes = 53;
                nColT_D = 54;
                nColT_Cl = 55;
                nColO = 56;
                nColB = 57;
                nColPr = 58;
                nColO_ = 59;
                nColDm = 60;
                nColMk = 61;
                nColOsP = 62;
                nColPercent = 63;
                nColMinusPercent = 64;
                nColAll = 65;
            } else {
                for (int i = 0; i < 2; i++) {
                    nColDept += 28;
                    nColBrig += 28;
                    nColPeriod += 28;

                    xCell = xSpreadsheet.getCellByPosition(nColDept, 4);
                    xCell.setFormula(data.get(0).toString().toLowerCase());

                    xCell = xSpreadsheet.getCellByPosition(nColBrig, 4);
                    xCell.setFormula(data.get(1).toString().toLowerCase());

                    xCell = xSpreadsheet.getCellByPosition(nColPeriod, 4);
                    xCell.setFormula(data.get(2).toString().toLowerCase());
                }

                nColBV_ = "CK";
                nColBVIt_ = "CL";
                nColOtD_ = "CR";
                nColOtCl_ = "CS";
                nColVD_ = "CU";
                nColVCl_ = "CV";
                nColTD_ = "CW";
                nColTCl_ = "CX";
                nColSrD_ = "CY";
                nColSrCl_ = "CZ";
                nColUch_ = "DD";
                nColDK_ = "DE";
                nColNes_ = "DF";
                nColOt_ = "DI";
                nColB_ = "DJ";
                nColPr_ = "DK";
                nColCO_ = "DL";
                nColDm_ = "DM";
                nColMk_ = "DN";
                nColOsP_ = "DO";

                nColBV = 88;
                nColBVIt = 89;
                nColBVP = 90;
                nColStoim = 91;
                nColTVCl = 92;
                nColTNCl = 93;
                nColTNVr = 94;
                nColOtD = 95;
                nColOtCl = 96;
                nColXV = 97;
                nColVD = 98;
                nColVCl = 99;
                nColTD = 100;
                nColTCl = 101;
                nColSrD = 102;
                nColSrCl = 103;
                nColVr = 104;
                nColUmT = 105;
                nColUvT = 106;
                nColUch = 107;
                nColDK = 108;
                nColNes = 109;
                nColT_D = 110;
                nColT_Cl = 111;
                nColO = 112;
                nColB = 113;
                nColPr = 114;
                nColO_ = 115;
                nColDm = 116;
                nColMk = 117;
                nColOsP = 118;
                nColPercent = 119;
                nColMinusPercent = 120;
                nColAll = 121;
            }

            int[] num = {0, 1, 2, 3, nColBV, nColDK, nColNes};

            xCell = xSpreadsheet.getCellByPosition(nColBV, 4);
            xCell.setFormula(data.get(0).toString().toLowerCase());

            xCell = xSpreadsheet.getCellByPosition(nColTNVr, 4);
            xCell.setFormula(data.get(1).toString().toLowerCase());

            xCell = xSpreadsheet.getCellByPosition(nColXV, 4);
            xCell.setFormula(data.get(2).toString().toLowerCase());

            xCell = xSpreadsheet.getCellByPosition(nColUch, 4);
            xCell.setFormula(data.get(0).toString().toLowerCase());

            xCell = xSpreadsheet.getCellByPosition(nColB, 4);
            xCell.setFormula(data.get(1).toString().toLowerCase());

            xCell = xSpreadsheet.getCellByPosition(nColDm, 4);
            xCell.setFormula(data.get(2).toString().toLowerCase());

            //------------------------------Рабочие дни

            nCol = 4;
            nRow = 7;

            for (int i = 0; i < workDays.size(); i++) {
                for (int j = 0; j < workSmena; j++) {
                    xCell = xSpreadsheet.getCellByPosition(nCol, nRow);
                    xCell.setValue(j + 1);

                    xCell = xSpreadsheet.getCellByPosition(nCol, nRow + 1);
                    xCell.setValue(8);

                    xCell = xSpreadsheet.getCellByPosition(nCol++, nRow + 2);
                    xCell.setFormula(String.valueOf(workDays.get(i)));
                }
            }

            //----------------Заполнение тела документа

            //------------------------------Выработка за месяц (бригада, др. конвеера)

            nRow = 11;
            k = 0;

            //brigVyrabatka = 0;
            dataPeredanoDK = new Vector();
            for (Object row : dataElement) {
                if (((Vector) row).get(7).toString().trim().equals(String.valueOf(idBrig))) {
                    k = 0;
                    for (int i = 0; i <= nColAll; i++) {
                        xCell = xSpreadsheet.getCellByPosition(i, nRow);
                        xPropSet = UnoRuntime.queryInterface(XPropertySet.class, xCell);
                        xPropSet.setPropertyValue("TableBorder", aBorder);
                        if (k < num.length) {
                            if (i == num[k]) {
                                if (num[k] == nColBV) {
                                    xCell.setValue(Double.valueOf(UtilZPlata.formatNorm(Double.valueOf(((Vector) row).get(k).toString()), 2)));
                                } else if (num[k] == nColDK) {
                                    xCell.setValue(Double.valueOf(UtilZPlata.formatNorm(Double.valueOf(((Vector) row).get(k).toString()), 2)));
                                } else if (num[k] == nColNes) {
                                    xCell.setValue(Double.valueOf(UtilZPlata.formatNorm(Double.valueOf(((Vector) row).get(k).toString()), 2)));
                                } else if (num[k] == 1) {
                                    xCell.setFormula(((Vector) row).get(k).toString() + " " + ((Vector) row).get(8).toString());
                                } else {
                                    xCell.setFormula(((Vector) row).get(k).toString());
                                }
                                k++;
                            }
                        }
                        if (i == nColBVIt)
                            xCell.setFormula("=IF(" + nColUch_ + (nRow + 1) + ">0;"
                                    + "(" + nColBV_ + (nRow + 1) + "+" + nColDK_ + (nRow + 1) + "+" + nColNes_ + (nRow + 1) + "-IF(" + nColOsP_ + (nRow + 1) + "<>\"\";" + nColOsP_ + (nRow + 1) + ";0))*" + nColUch_ + (nRow + 1) + ";"
                                    + nColBV_ + (nRow + 1) + "+" + nColDK_ + (nRow + 1) + "+" + nColNes_ + (nRow + 1) + "-IF(" + nColOsP_ + (nRow + 1) + "<>\"\";" + nColOsP_ + (nRow + 1) + ";0))");
                        if (i == nColBVP)
                            xCell.setFormula("=IF(" + nColOtCl_ + (nRow + 1) + "=0;\"-\";(" + nColBVIt_ + (nRow + 1) + ")/" + nColOtCl_ + (nRow + 1) + ")");
                        if (i == nColStoim) {
                            int x = nRow + 1;
                            xCell.setFormula("=ROUND("
                                    + "IF(D" + x + "=1;ROUND(1*Лист2.E2/169;2);"
                                    + "IF(D" + x + "=2;ROUND(1.16*Лист2.E2/169;2);"
                                    + "IF(D" + x + "=3;ROUND(1.35*Лист2.E2/169;2);"
                                    + "IF(D" + x + "=4;ROUND(1.57*Лист2.E2/169;2);"
                                    + "IF(D" + x + "=5;ROUND(1.73*Лист2.E2/169;2);"
                                    + "IF(D" + x + "=6;ROUND(1.9*Лист2.E2/169;2);"
                                    + "IF(D" + x + "=7;ROUND(2.03*Лист2.E2/169;2);)))))));2)");
                        }
                        if (i == nColPercent) {
                            int x = nRow + 1;
                            xCell.setFormula(
                                    "=IF(C" + x + "=\"швея\";IF(OR(AI" + x
                                            + "=\"-\";AI" + x + "<=50%);0%;IF(AI" + x
                                            + "<=70%;140%;IF(AI" + x + "<=80%;160%;IF(AI" + x
                                            + "<=90%;180%;IF(AI" + x + "<=100%;200%;250%)))));" +
                                            "IF(OR(AI" + x + "=\"-\";AI" + x + "<=60%);0%;IF(AI" + x
                                            + "<=70%;140%;IF(AI" + x + "<=80%;160%;IF(AI" + x
                                            + "<=90%;180%;IF(AI" + x + "<=100%;200%;IF(AI" + x
                                            + "<=140%;210%;220%))))))) - BM" + x + "");
                        }
                        if (i == nColOsP)
                            xCell.setFormula(
                                    "=IF(" + nColBV_ + (nRow + 1) + ">=0;"
                                            + "(IF(C" + (nRow + 1) + "=\"" + UtilZPlata.OTCHET_PROF_OSVOENIE + "\";"
                                            + nColBV_ + (nRow + 1) + "+" + nColDK_ + (nRow + 1) + ";"
                                            + "0))"
                                            + ";\"\")"
                            );
                        if(i == nColMinusPercent)
                            xCell.setFormula("=0%");
                    }
                    nRow++;
                } else {
                    dataPeredanoDK.add(row);
                }
            }

            nRowBrig = nRow;

            //------------------------------ВТО
            dataPeredanoDKVTO = new Vector();
            if (elements2.size() > 0) {
                xCell = xSpreadsheet.getCellByPosition(1, nRow);
                xCell.setFormula("ВТО");
            }
            nRow++;

            for (Object row : elements2) {
                if (((Vector) row).get(7).toString().trim().equals(String.valueOf(idBrig))) {
                    k = 0;
                    for (int i = 0; i < nColAll; i++) {
                        xCell = xSpreadsheet.getCellByPosition(i, nRow);
                        xPropSet = UnoRuntime.queryInterface(XPropertySet.class, xCell);
                        xPropSet.setPropertyValue("TableBorder", aBorder);
                        if (k < num.length) {
                            if (i == num[k]) {
                                if (num[k] == nColBV) {
                                    xCell.setValue(Double.valueOf(UtilZPlata.formatNorm(Double.valueOf(((Vector) row).get(k).toString()), 2)));
                                } else if (num[k] == nColDK) {
                                    xCell.setValue(Double.valueOf(UtilZPlata.formatNorm(Double.valueOf(((Vector) row).get(k).toString()), 2)));
                                } else if (num[k] == nColNes) {
                                    xCell.setValue(Double.valueOf(UtilZPlata.formatNorm(Double.valueOf(((Vector) row).get(k).toString()), 2)));
                                } else if (num[k] == 1) {
                                    xCell.setFormula(((Vector) row).get(k).toString() + " " + ((Vector) row).get(8).toString());
                                } else {
                                    xCell.setFormula(((Vector) row).get(k).toString());
                                }
                                k++;
                            }
                        }
                        if (i == nColBVIt)
                            xCell.setFormula("=IF(" + nColUch_ + (nRow + 1) + ">0;"
                                    + "(" + nColBV_ + (nRow + 1) + "+" + nColDK_ + (nRow + 1) + "+" + nColNes_ + (nRow + 1) + "-IF(" + nColOsP_ + (nRow + 1) + "<>\"\";" + nColOsP_ + (nRow + 1) + ";0))*" + nColUch_ + (nRow + 1) + ";"
                                    + nColBV_ + (nRow + 1) + "+" + nColDK_ + (nRow + 1) + "+" + nColNes_ + (nRow + 1) + "-IF(" + nColOsP_ + (nRow + 1) + "<>\"\";" + nColOsP_ + (nRow + 1) + ";0))");
                        if (i == nColBVP)
                            xCell.setFormula("=IF(" + nColOtCl_ + (nRow + 1) + "=0;\"-\";(" + nColBVIt_ + (nRow + 1) + ")/" + nColOtCl_ + (nRow + 1) + ")");
                        if (i == nColStoim) {
                            int x = nRow + 1;
                            xCell.setFormula("=ROUND("
                                    + "IF(D" + x + "=1;ROUND(1*Лист2.E2/169;2);"
                                    + "IF(D" + x + "=2;ROUND(1.16*Лист2.E2/169;2);"
                                    + "IF(D" + x + "=3;ROUND(1.35*Лист2.E2/169;2);"
                                    + "IF(D" + x + "=4;ROUND(1.57*Лист2.E2/169;2);"
                                    + "IF(D" + x + "=5;ROUND(1.73*Лист2.E2/169;2);"
                                    + "IF(D" + x + "=6;ROUND(1.9*Лист2.E2/169;2);"
                                    + "IF(D" + x + "=7;ROUND(2.03*Лист2.E2/169;2);)))))));2)");
                        }
                        if (i == nColPercent) {
                            int x = nRow + 1;
                            xCell.setFormula(
                                    "=IF(C" + x + "=\"швея\";IF(OR(AI" + x
                                            + "=\"-\";AI" + x + "<=50%);0%;IF(AI" + x
                                            + "<=70%;140%;IF(AI" + x + "<=80%;160%;IF(AI" + x
                                            + "<=90%;180%;IF(AI" + x + "<=100%;200%;250%)))));" +
                                            "IF(OR(AI" + x + "=\"-\";AI" + x + "<=60%);0%;IF(AI" + x
                                            + "<=70%;140%;IF(AI" + x + "<=80%;160%;IF(AI" + x
                                            + "<=90%;180%;IF(AI" + x + "<=100%;200%;IF(AI" + x
                                            + "<=140%;210%;220%))))))) - BM" + x + "");
                        }
                        if (i == nColOsP)
                            xCell.setFormula(
                                    "=IF(" + nColBV_ + (nRow + 1) + ">=0;"
                                            + "(IF(C" + (nRow + 1) + "=\"" + UtilZPlata.OTCHET_PROF_OSVOENIE + "\";"
                                            + nColBV_ + (nRow + 1) + "+" + nColDK_ + (nRow + 1) + ";"
                                            + "0))"
                                            + ";\"\")"
                            );
                        if(i == nColMinusPercent)
                            xCell.setFormula("=0%");
                    }
                    nRow++;
                } else {
                    dataPeredanoDKVTO.add(row);
                }
            }

            //------------------------------Передано на др. конвейера

            if (dataPeredanoDKVTO.size() > 0) {
                int nRowDKVTO = nRow;
                nRow++;

                for (Object row : dataPeredanoDKVTO) {
                    k = 0;
                    for (int i = 0; i < nColAll; i++) {
                        xCell = xSpreadsheet.getCellByPosition(i, nRow);
                        xPropSet = UnoRuntime.queryInterface(XPropertySet.class, xCell);
                        xPropSet.setPropertyValue("TableBorder", aBorder);
                        if (k < num.length) {
                            if (i == num[k]) {
                                if (num[k] == nColBV) {
                                    xCell.setValue(Double.valueOf(UtilZPlata.formatNorm(Double.valueOf(((Vector) row).get(k).toString()), 2)));
                                } else if (num[k] == nColDK) {
                                    xCell.setValue(0);
                                } else if (num[k] == nColNes) {
                                    xCell.setValue(0);
                                } else if (num[k] == 1) {
                                    xCell.setFormula("(" + ((Vector) row).get(7).toString() + ")" + ((Vector) row).get(k).toString());
                                } else {
                                    xCell.setFormula(((Vector) row).get(k).toString());
                                }
                                k++;
                            }
                        }
                        if (i == nColBVIt)
                            xCell.setFormula("=" + nColBV_ + (nRow + 1) + "+" + nColDK_ + (nRow + 1) + "+" + nColNes_ + (nRow + 1) + "-IF(" + nColOsP_ + (nRow + 1) + "<>\"\";" + nColOsP_ + (nRow + 1) + ";0)");
                    }
                    nRow++;
                }

                if (nRowDKVTO + 1 != nRow) {
                    xCell = xSpreadsheet.getCellByPosition(1, nRowDKVTO);
                    xCell.setFormula("Передано на ДК ВТО");
                }

            }

            if (dataPeredanoDK.size() > 0) {
                int nRowDK = nRow;
                nRow++;

                for (Object row : dataPeredanoDK) {
                    if (Double.valueOf(UtilZPlata.formatNorm(Double.valueOf(((Vector) row).get(4).toString()), 2)) != 0) {
                        k = 0;
                        for (int i = 0; i < nColAll; i++) {
                            xCell = xSpreadsheet.getCellByPosition(i, nRow);
                            xPropSet = UnoRuntime.queryInterface(XPropertySet.class, xCell);
                            xPropSet.setPropertyValue("TableBorder", aBorder);
                            if (k < num.length) {
                                if (i == num[k]) {
                                    if (num[k] == nColBV) {
                                        xCell.setValue(Double.valueOf(UtilZPlata.formatNorm(Double.valueOf(((Vector) row).get(k).toString()), 2)));
                                    } else if (num[k] == nColDK) {
                                        xCell.setValue(0);
                                    } else if (num[k] == nColNes) {
                                        xCell.setValue(0);
                                    } else if (num[k] == 1) {
                                        xCell.setFormula("(" + ((Vector) row).get(7).toString() + ")" + ((Vector) row).get(k).toString());
                                    } else {
                                        xCell.setFormula(((Vector) row).get(k).toString());
                                    }
                                    k++;
                                }
                            }
                            if (i == nColBVIt)
                                xCell.setFormula("=" + nColBV_ + (nRow + 1) + "+" + nColDK_ + (nRow + 1) + "+" + nColNes_ + (nRow + 1) + "-IF(" + nColOsP_ + (nRow + 1) + "<>\"\";" + nColOsP_ + (nRow + 1) + ";0)");
                        }
                        nRow++;
                    }
                }

                if (nRowDK + 1 != nRow) {
                    xCell = xSpreadsheet.getCellByPosition(1, nRowDK);
                    xCell.setFormula("Передано на ДК");
                }
            }

            nRowBrak = nRow;

            //------------------------------Табель
            if (JOptionPane.showOptionDialog(null, "Заполните все прогулы, б/листы, отпуска и т.д.\n"
                            + "Далее нажмите кнопку 'Продолжить' для завершения\n"
                            + "формирования отчёта. ", "Внимание!",
                    JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, null,
                    new Object[]{"Продолжить", "Отмена"}, "Продолжить") == JOptionPane.YES_OPTION) {

                nRow = 10;
                nRowEmpl = 11;
                historySrt = "";
                for (int l = 0; l < dataElement.size() + elements2.size() + 1 - dataPeredanoDK.size() - dataPeredanoDKVTO.size(); l++) {
                    nRow++;

                    staffSrt = "";
                    vyrabotka = 0;

                    allCl = 0;
                    allD = 0;
                    allVD = 0;
                    allVCl = 0;
                    allTD = 0;
                    allTCl = 0;
                    allVrCl = 0;
                    allT_D = 0;
                    allT_Cl = 0;
                    allSrD = 0;
                    allSrCl = 0;
                    allOD = 0;
                    allBD = 0;
                    allPrD = 0;
                    allO_D = 0;
                    allUch = 0;
                    allDK = 0;
                    allNes = 0;
                    allDm = 0;
                    allMk = 0;
                    allZ = 0;

                    //------------------------------запоминаем колонки которые рабочие дни
                    //-и подсчитывает кол-во б/лист, отпуск, свой счёт,средний, 2/3 тарифа

                    colData = new Vector();
                    for (int i = 4; i < nColBV; i++) {
                        xCell = xSpreadsheet.getCellByPosition(i, 9);
                        if (!xCell.getFormula().equals("")) {
                            xCell = xSpreadsheet.getCellByPosition(i, nRow);
                            if (xCell.getFormula().equals("")) {
                                colData.add(i);

                                xCell = xSpreadsheet.getCellByPosition(i, 8);
                                allCl += xCell.getValue();
                                allD += 1;

                            } else if (xCell.getFormula().toLowerCase().trim().equals("б")) {
                                allBD += 1;
                            } else if (xCell.getFormula().toLowerCase().trim().equals("о") || xCell.getFormula().toLowerCase().trim().equals("o")) {
                                allOD += 1;
                            } else if (xCell.getFormula().toLowerCase().trim().equals("со") || xCell.getFormula().toLowerCase().trim().equals("co") ||
                                    xCell.getFormula().toLowerCase().trim().equals("а") || xCell.getFormula().toLowerCase().trim().equals("a") ||
                                    xCell.getFormula().toLowerCase().trim().equals("у") || xCell.getFormula().toLowerCase().trim().equals("y") ||
                                    xCell.getFormula().toLowerCase().trim().equals("св") || xCell.getFormula().toLowerCase().trim().equals("cb")) {
                                allO_D += 1;
                            } else if (xCell.getFormula().toLowerCase().trim().equals("п")) {
                                allPrD += 1;
                            } else if (xCell.getFormula().toLowerCase().trim().equals("в") || xCell.getFormula().toLowerCase().trim().equals("b")) {
                                allVD += 1;
                                xCell = xSpreadsheet.getCellByPosition(i, 8);
                                allVCl += xCell.getValue();
                            } else if (xCell.getFormula().toLowerCase().trim().equals("т") || xCell.getFormula().toLowerCase().trim().equals("t")) {
                                allTD += 1;
                                xCell = xSpreadsheet.getCellByPosition(i, 8);
                                allTCl += xCell.getValue();

                                xCell = xSpreadsheet.getCellByPosition(i, 7);
                                if (xCell.getFormula().equals("2") ||
                                        xCell.getFormula().toLowerCase().trim().equals("2х") ||
                                        xCell.getFormula().toLowerCase().trim().equals("2x")) {
                                    xCell = xSpreadsheet.getCellByPosition(nColTVCl, nRow);
                                    xCell.setValue(xCell.getValue() + 1.5);
                                }

                            } else if (xCell.getFormula().toLowerCase().trim().equals("т*") || xCell.getFormula().toLowerCase().trim().equals("t*")) {
                                allT_D += 1;
                                xCell = xSpreadsheet.getCellByPosition(i, 8);
                                allT_Cl += xCell.getValue();
                            } else if (xCell.getFormula().toLowerCase().trim().equals("ср") || xCell.getFormula().toLowerCase().trim().equals("cp")) {
                                allSrD += 1;
                                xCell = xSpreadsheet.getCellByPosition(i, 8);
                                allSrCl += xCell.getValue();
                            } else if (xCell.getFormula().toLowerCase().trim().equals("вр") || xCell.getFormula().toLowerCase().trim().equals("bp")) {
                                xCell = xSpreadsheet.getCellByPosition(i, 8);
                                allVrCl += xCell.getValue();
                            } else if (xCell.getFormula().toLowerCase().trim().equals("дм") || xCell.getFormula().toLowerCase().trim().equals("dm")) {
                                allDm += 1;
                            } else if (xCell.getFormula().toLowerCase().trim().equals("мк") || xCell.getFormula().toLowerCase().trim().equals("mk")) {
                                allMk += 1;
                            }
                        }
                    }

                    //------------------------------выработка по рабочим дням

                    xCell = xSpreadsheet.getCellByPosition(nColBVIt, nRow);
                    if (!xCell.getFormula().equals("")) {
                        vyrabotka = xCell.getValue();
                        tmpVyrabotka = 0;

                        for (int i = 0; i < colData.size() - 1; i++) {
                            tmp = 0;

                            xCell = xSpreadsheet.getCellByPosition(Integer.valueOf(colData.get(i).toString()), nRow);
                            xCell.setValue(Double.valueOf(UtilZPlata.formatNorm(Double.valueOf(vyrabotka / colData.size()), 2)));
                            tmpVyrabotka += Double.valueOf(UtilZPlata.formatNorm(Double.valueOf(vyrabotka / colData.size()), 2));

                            xCell = xSpreadsheet.getCellByPosition(Integer.valueOf(colData.get(i).toString()), 7);
                            if (xCell.getFormula().equals("2") || xCell.getFormula().toLowerCase().trim().equals("2х") || xCell.getFormula().toLowerCase().trim().equals("2x")) {
                                xCell = xSpreadsheet.getCellByPosition(nColTVCl, nRow);
                                xCell.setValue(xCell.getValue() + 1.5);

                            } else if (xCell.getFormula().equals("3")) {
                                xCell = xSpreadsheet.getCellByPosition(Integer.valueOf(colData.get(i).toString()), 8);
                                tmp += xCell.getValue();

                                xCell = xSpreadsheet.getCellByPosition(nColTNCl, nRow);
                                xCell.setValue(xCell.getValue() + tmp);

                                xCell = xSpreadsheet.getCellByPosition(nColTNVr, nRow);
                                xCell.setValue(xCell.getValue() + Double.valueOf(UtilZPlata.formatNorm(Double.valueOf(vyrabotka / colData.size()), 2)));

                            }

                            xCell = xSpreadsheet.getCellByPosition(Integer.valueOf(colData.get(i).toString()), 7);
                            if (xCell.getFormula().toLowerCase().trim().equals("х") || xCell.getFormula().toLowerCase().trim().equals("x") ||
                                    xCell.getFormula().toLowerCase().trim().equals("1х") || xCell.getFormula().toLowerCase().trim().equals("1x") ||
                                    xCell.getFormula().toLowerCase().trim().equals("2х") || xCell.getFormula().toLowerCase().trim().equals("2x")) {
                                xCell = xSpreadsheet.getCellByPosition(nColXV, nRow);
                                xCell.setValue(xCell.getValue() + Double.valueOf(UtilZPlata.formatNorm(Double.valueOf(vyrabotka / colData.size()), 2)));
                            }
                        }

                        tmp = 0;
                        value = 0;

                        xCell = xSpreadsheet.getCellByPosition(nColBVIt, nRow);
                        value = xCell.getValue();

                        if (colData.size() > 0) {
                            xCell = xSpreadsheet.getCellByPosition(Integer.valueOf(colData.get(colData.size() - 1).toString()), nRow);
                            xCell.setValue(Double.valueOf(UtilZPlata.formatNorm(value - tmpVyrabotka, 2)));

                            xCell = xSpreadsheet.getCellByPosition(Integer.valueOf(colData.get(colData.size() - 1).toString()), 7);
                            if (xCell.getFormula().equals("2") || xCell.getFormula().toLowerCase().trim().equals("2х") || xCell.getFormula().toLowerCase().trim().equals("2x")) {
                                xCell = xSpreadsheet.getCellByPosition(nColTVCl, nRow);
                                xCell.setValue(xCell.getValue() + 1.5);

                            } else if (xCell.getFormula().equals("3")) {
                                xCell = xSpreadsheet.getCellByPosition(Integer.valueOf(colData.get(colData.size() - 1).toString()), 8);
                                tmp += xCell.getValue();

                                xCell = xSpreadsheet.getCellByPosition(nColTNCl, nRow);
                                xCell.setValue(xCell.getValue() + tmp);

                                xCell = xSpreadsheet.getCellByPosition(nColTNVr, nRow);
                                xCell.setValue(xCell.getValue() + Double.valueOf(UtilZPlata.formatNorm(value - tmpVyrabotka, 2)));

                            }

                            xCell = xSpreadsheet.getCellByPosition(Integer.valueOf(colData.get(colData.size() - 1).toString()), 7);
                            if (xCell.getFormula().toLowerCase().trim().equals("х") || xCell.getFormula().toLowerCase().trim().equals("x") ||
                                    xCell.getFormula().toLowerCase().trim().equals("1х") || xCell.getFormula().toLowerCase().trim().equals("1x") ||
                                    xCell.getFormula().toLowerCase().trim().equals("2х") || xCell.getFormula().toLowerCase().trim().equals("2x")) {
                                xCell = xSpreadsheet.getCellByPosition(nColXV, nRow);
                                xCell.setValue(xCell.getValue() + Double.valueOf(UtilZPlata.formatNorm(value - tmpVyrabotka, 2)));
                            }
                        }

                        //------------------------------отработанное время

                        xCell = xSpreadsheet.getCellByPosition(nColOtD, nRow);
                        xCell.setValue(allD);

                        xCell = xSpreadsheet.getCellByPosition(nColOtCl, nRow);
                        xCell.setValue(allCl);

                        //------------------------------Внедрение

                        xCell = xSpreadsheet.getCellByPosition(nColVD, nRow);
                        xCell.setValue(allVD);

                        xCell = xSpreadsheet.getCellByPosition(nColVCl, nRow);
                        xCell.setValue(allVCl);

                        //------------------------------Тариф

                        xCell = xSpreadsheet.getCellByPosition(nColTD, nRow);
                        xCell.setValue(allTD);

                        xCell = xSpreadsheet.getCellByPosition(nColTCl, nRow);
                        xCell.setValue(allTCl);


                        //------------------------------Средний

                        xCell = xSpreadsheet.getCellByPosition(nColSrD, nRow);
                        xCell.setFormula("=IF(C" + (nRow + 1) + "=\"" + UtilZPlata.OTCHET_PROF_OSVOENIE + "\";" + nColOtD_ + (nRow + 1) + ";" + (allSrD > 0 ? allSrD : "\"\"") + ")");
                        //xCell.setValue(allSrD);

                        xCell = xSpreadsheet.getCellByPosition(nColSrCl, nRow);
                        xCell.setFormula("=IF(C" + (nRow + 1) + "=\"" + UtilZPlata.OTCHET_PROF_OSVOENIE + "\";" + nColOtCl_ + (nRow + 1) + ";" + (allSrCl > 0 ? allSrCl : "\"\"") + ")");
                        //xCell.setValue(allSrCl);

                        //------------------------------Вредность

                        xCell = xSpreadsheet.getCellByPosition(nColVr, nRow);
                        xCell.setValue(allVrCl);

                        //------------------------------2/3 тарифа

                        xCell = xSpreadsheet.getCellByPosition(nColT_D, nRow);
                        xCell.setValue(allT_D);

                        xCell = xSpreadsheet.getCellByPosition(nColT_Cl, nRow);
                        xCell.setValue(allT_Cl);

                        //------------------------------Отпуск

                        xCell = xSpreadsheet.getCellByPosition(nColO, nRow);
                        xCell.setValue(allOD);

                        //------------------------------Б/лист

                        xCell = xSpreadsheet.getCellByPosition(nColB, nRow);
                        xCell.setValue(allBD);

                        //------------------------------Перевод

                        xCell = xSpreadsheet.getCellByPosition(nColPr, nRow);
                        xCell.setValue(allPrD);

                        //------------------------------Соц. отпуск

                        xCell = xSpreadsheet.getCellByPosition(nColO_, nRow);
                        xCell.setValue(allO_D);

                        //------------------------------День матери

                        xCell = xSpreadsheet.getCellByPosition(nColDm, nRow);
                        xCell.setValue(allDm);

                        //------------------------------Мед.комиссия

                        xCell = xSpreadsheet.getCellByPosition(nColMk, nRow);
                        xCell.setValue(allMk);

                        //------------------------------Процент выполнения

                        xCell = xSpreadsheet.getCellByPosition(0, nRow);            // табельный
                        staffSrt = xCell.getFormula().trim();

                        xCell = xSpreadsheet.getCellByPosition(1, nRow);
                        //staffSrt = staffSrt + xCell.getFormula().trim();               // ФИО, присв. разряд

                        if (l == 0) historySrt = staffSrt;

                        if (!staffSrt.equals(historySrt)) {
                            nRowEmpl = nRow;
                            historySrt = staffSrt;
                        } else {
                            xCell = xSpreadsheet.getCellByPosition(nColBVP, nRow);
                            xCell.setFormula("=IF(" + nColOtCl_ + (nRow + 1) + "=0;\"-\";"
                                    + "(SUM(" + nColBVIt_ + (nRowEmpl + 1) + ":" + nColBVIt_ + (nRow + 1) + "))/" + nColOtCl_ + (nRow + 1) + ")");

                            for (int i = nColTVCl; i < nColMk + 1; i++) {
                                if (nRow != 11) {
                                    if (i != nColXV && i != nColUmT && i != nColUvT && i != nColDK && i != nColNes && i != nColUch) {
                                        xCell = xSpreadsheet.getCellByPosition(i, nRow - 1);
                                        xCell.setFormula("");
                                    }
                                }
                            }
                        }

                        for (int i = nColTVCl; i < nColMk + 1; i++) {
                            //  if(nRow != 11){
                            if (i != nColXV && i != nColUmT && i != nColUvT && i != nColDK && i != nColNes && i != nColUch) {
                                xCell = xSpreadsheet.getCellByPosition(i, nRow);
                                if (xCell.getFormula().equals("0"))
                                    xCell.setFormula("");
                            }
                            //    }
                        }
                    }
                }

                nRow = nRowBrak;
                nRow += 2;
                for (int j = 0; j < 2; j++) {
                    for (int i = 0; i < nColAll; i++) {
                        xCell = xSpreadsheet.getCellByPosition(i, nRow);
                        xPropSet = UnoRuntime.queryInterface(XPropertySet.class, xCell);
                        xPropSet.setPropertyValue("TableBorder", aBorder);
                        if (i == 1) {
                            xCell.setFormula(j == 0 ? "Брак" : "ИТОГО");
                        } else if (i == nColBV) {
                            xCell.setValue(j == 0 ? Double.valueOf(UtilZPlata.formatNorm(brak, 2)) : 0);
                        }
                    }
                    nRow++;
                }

                //------------------------------ИТОГО

                //------------------------------Бригад. выработка
                xCell = xSpreadsheet.getCellByPosition(nColBV, nRow - 1);
                xCell.setFormula("=SUM(" + nColBV_ + 12 + ":" + nColBV_ + (nRow - 1) + ")");

                // Итого по выработке
                xCell = xSpreadsheet.getCellByPosition(nColBVIt, nRow - 1);
                xCell.setFormula("=SUM(" + nColBVIt_ + 12 + ":" + nColBVIt_ + (nRow - 1) + ")");

                //------------------------------Отработанное время

                xCell = xSpreadsheet.getCellByPosition(nColOtD, nRow - 1);
                xCell.setFormula("=SUM(" + nColOtD_ + 12 + ":" + nColOtD_ + (nRow - 1) + ")");

                xCell = xSpreadsheet.getCellByPosition(nColOtCl, nRow - 1);
                xCell.setFormula("=SUM(" + nColOtCl_ + 12 + ":" + nColOtCl_ + (nRow - 1) + ")");

                //------------------------------Внедрение

                xCell = xSpreadsheet.getCellByPosition(nColVD, nRow - 1);
                xCell.setFormula("=SUM(" + nColVD_ + 12 + ":" + nColVD_ + (nRow - 1) + ")");

                xCell = xSpreadsheet.getCellByPosition(nColVCl, nRow - 1);
                xCell.setFormula("=SUM(" + nColVCl_ + 12 + ":" + nColVCl_ + (nRow - 1) + ")");

                //------------------------------Тариф

                xCell = xSpreadsheet.getCellByPosition(nColTD, nRow - 1);
                xCell.setFormula("=SUM(" + nColTD_ + 12 + ":" + nColTD_ + (nRow - 1) + ")");

                xCell = xSpreadsheet.getCellByPosition(nColTCl, nRow - 1);
                xCell.setFormula("=SUM(" + nColTCl_ + 12 + ":" + nColTCl_ + (nRow - 1) + ")");

                //------------------------------Средний

                xCell = xSpreadsheet.getCellByPosition(nColSrD, nRow - 1);
                xCell.setFormula("=SUM(" + nColSrD_ + 12 + ":" + nColSrD_ + (nRow - 1) + ")");

                xCell = xSpreadsheet.getCellByPosition(nColSrCl, nRow - 1);
                xCell.setFormula("=SUM(" + nColSrCl_ + 12 + ":" + nColSrCl_ + (nRow - 1) + ")");

                //------------------------------Другие конвеера

                xCell = xSpreadsheet.getCellByPosition(nColDK, nRow - 1);
                xCell.setFormula("=SUM(" + nColDK_ + 12 + ":" + nColDK_ + (nRow - 1) + ")");

                //------------------------------Несорт. брак

                xCell = xSpreadsheet.getCellByPosition(nColNes, nRow - 1);
                xCell.setFormula("=SUM(" + nColNes_ + 12 + ":" + nColNes_ + (nRow - 1) + ")");

                //------------------------------Отпуск

                xCell = xSpreadsheet.getCellByPosition(nColO, nRow - 1);
                xCell.setFormula("=SUM(" + nColOt_ + 12 + ":" + nColOt_ + (nRow - 1) + ")");

                //------------------------------Б/лист

                xCell = xSpreadsheet.getCellByPosition(nColB, nRow - 1);
                xCell.setFormula("=SUM(" + nColB_ + 12 + ":" + nColB_ + (nRow - 1) + ")");

                //------------------------------Переход

                xCell = xSpreadsheet.getCellByPosition(nColPr, nRow - 1);
                xCell.setFormula("=SUM(" + nColPr_ + 12 + ":" + nColPr_ + (nRow - 1) + ")");

                //------------------------------Соц. отпуск

                xCell = xSpreadsheet.getCellByPosition(nColO_, nRow - 1);
                xCell.setFormula("=SUM(" + nColCO_ + 12 + ":" + nColCO_ + (nRow - 1) + ")");

                //------------------------------День матери

                xCell = xSpreadsheet.getCellByPosition(nColDm, nRow - 1);
                xCell.setFormula("=SUM(" + nColDm_ + 12 + ":" + nColDm_ + (nRow - 1) + ")");

                //------------------------------Мед.комиссия

                xCell = xSpreadsheet.getCellByPosition(nColMk, nRow - 1);
                xCell.setFormula("=SUM(" + nColMk_ + 12 + ":" + nColMk_ + (nRow - 1) + ")");

                //------------------------------Освоение профессии

                xCell = xSpreadsheet.getCellByPosition(nColOsP, nRow - 1);
                xCell.setFormula("=SUM(" + nColOsP_ + 12 + ":" + nColOsP_ + (nRow - 1) + ")");

                //------------------------------Итого выработка за месяц в н.-часах

                /*
                xCell = xSpreadsheet.getCellByPosition(nColBVIt, nRow-1);
                xCell.setFormula("="+nColBV_+nRow+"+"+nColDK_+nRow+"-"+nColOsP_+nRow);
                */

                //------------------------------Процент выполнения

                xCell = xSpreadsheet.getCellByPosition(nColBVP, nRow - 1);
                xCell.setFormula("=IF(" + nColOtCl_ + nRow + "=0;\"-\";(" + nColBVIt_ + nRow + ")/" + nColOtCl_ + nRow + ")");

                xCell = xSpreadsheet.getCellByPosition(nColPercent, nRow - 1);
                xCell.setFormula("=IF(" +
                        "OR(AI" + nRow + "=\"-\";AI" + nRow + "<=85%);\"90%\";" +
                        "IF(AI" + nRow + "<=90%;\"115%\";" +
                        "IF(AI" + nRow + "<=95%;\"145%\";" +
                        "IF(AI" + nRow + "<=100%;\"170%\";\"190%\"))))");

                if (!elements2.isEmpty()) {
                    for (int i = 0; i < nColAll; i++) {
                        xCell = xSpreadsheet.getCellByPosition(i, nRow);
                        xPropSet = UnoRuntime.queryInterface(XPropertySet.class, xCell);
                        xPropSet.setPropertyValue("TableBorder", aBorder);
                        if (i == 1) {
                            xCell.setFormula("ИТОГО без ВТО и переданных на ДК");
                        } else if (i == nColBV) {
                            xCell.setFormula("=SUM(" + nColBV_ + 12 + ":" + nColBV_ + (nRowBrig) + ")");
                            //   xCell.setValue(Double.valueOf(UtilZPlata.formatNorm(brigVyrabatka,2)));
                        } else if (i == nColDK) {
                            xCell.setFormula("=SUM(" + nColDK_ + 12 + ":" + nColDK_ + (nRowBrig) + ")");
                        } else if (i == nColNes) {
                            xCell.setFormula("=SUM(" + nColNes_ + 12 + ":" + nColNes_ + (nRowBrig) + ")");
                        } else if (i == nColOtD) {
                            xCell.setFormula("=SUM(" + nColOtD_ + 12 + ":" + nColOtD_ + (nRowBrig) + ")");
                        } else if (i == nColOtCl) {
                            xCell.setFormula("=SUM(" + nColOtCl_ + 12 + ":" + nColOtCl_ + (nRowBrig) + ")");
                        } else if (i == nColBVIt) {
                            xCell.setFormula("=SUM(" + nColBVIt_ + 12 + ":" + nColBVIt_ + (nRowBrig) + ")");
                            //  xCell.setFormula("="+nColBV_+(nRow+1)+"+"+nColDK_+(nRow+1)+"-IF("+nColOsP_+(nRow+1)+"<>\"\";"+nColOsP_+(nRow+1)+";0)");
                        } else if (i == nColBVP) {
                            xCell.setFormula("=IF(" + nColOtCl_ + (nRow + 1) + "=0;\"-\";(" + nColBVIt_ + (nRow + 1) + ")/" + nColOtCl_ + (nRow + 1) + ")");
                        }
                    }
                } else if (!dataPeredanoDK.isEmpty()) {
                    for (int i = 0; i < nColAll; i++) {
                        xCell = xSpreadsheet.getCellByPosition(i, nRow);
                        xPropSet = UnoRuntime.queryInterface(XPropertySet.class, xCell);
                        xPropSet.setPropertyValue("TableBorder", aBorder);
                        if (i == 1) {
                            xCell.setFormula("ИТОГО без переданных на ДК");
                        } else if (i == nColBV) {
                            xCell.setFormula("=SUM(" + nColBV_ + 12 + ":" + nColBV_ + (nRowBrig) + ")");
                            //   xCell.setValue(Double.valueOf(UtilZPlata.formatNorm(brigVyrabatka,2)));
                        } else if (i == nColDK) {
                            xCell.setFormula("=SUM(" + nColDK_ + 12 + ":" + nColDK_ + (nRowBrig) + ")");
                        } else if (i == nColNes) {
                            xCell.setFormula("=SUM(" + nColNes_ + 12 + ":" + nColNes_ + (nRowBrig) + ")");
                        } else if (i == nColOtD) {
                            xCell.setFormula("=SUM(" + nColOtD_ + 12 + ":" + nColOtD_ + (nRowBrig) + ")");
                        } else if (i == nColOtCl) {
                            xCell.setFormula("=SUM(" + nColOtCl_ + 12 + ":" + nColOtCl_ + (nRowBrig) + ")");
                        } else if (i == nColBVIt) {
                            xCell.setFormula("=SUM(" + nColBVIt_ + 12 + ":" + nColBVIt_ + (nRowBrig) + ")");
                            //         xCell.setFormula("="+nColBV_+(nRow+1)+"+"+nColDK_+(nRow+1)+"-IF("+nColOsP_+(nRow+1)+"<>\"\";"+nColOsP_+(nRow+1)+";0)");
                        } else if (i == nColBVP) {
                            xCell.setFormula("=IF(" + nColOtCl_ + (nRow + 1) + "=0;\"-\";(" + nColBVIt_ + (nRow + 1) + ")/" + nColOtCl_ + (nRow + 1) + ")");
                        }
                    }
                }

            }

            //----------------Заполнение колонтитула документа

            String text =
                    UtilZPlata.PROF_STAFF_3 + " " + UtilZPlata.STAFF_3 + " __________________________" + "         "
                            + UtilZPlata.PROF_STAFF_4 + " " + UtilZPlata.STAFF_4 + " __________________________";

            insertTextInFooter(xSpreadsheetDocument, text);

        } catch (Exception e) {
            log.error("Ошибка при заполнении отчёта", e);
            throw new Exception("Ошибка при формировании отчёта ", e);
        }
    }

    private void fildVedomostZPlataListZapuska(XComponent currentDocument) throws Exception {
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
            XCell xCell = xSpreadsheet.getCellByPosition(1, 0);
            xCell.setFormula(xCell.getFormula() + nameTamplates);

            xCell = xSpreadsheet.getCellByPosition(2, 2);
            xCell.setFormula(data.get(0).toString());

            xCell = xSpreadsheet.getCellByPosition(7, 2);
            xCell.setFormula(data.get(1).toString());

            xCell = xSpreadsheet.getCellByPosition(2, 3);
            xCell.setFormula(data.get(2).toString());

            xCell = xSpreadsheet.getCellByPosition(7, 3);
            xCell.setFormula(data.get(3).toString());

            xCell = xSpreadsheet.getCellByPosition(2, 4);
            xCell.setFormula(data.get(7).toString());

            xCell = xSpreadsheet.getCellByPosition(7, 4);
            xCell.setFormula(data.get(6).toString());

            xCell = xSpreadsheet.getCellByPosition(2, 5);
            xCell.setFormula(data.get(8).toString() + " " + data.get(9).toString());

            xCell = xSpreadsheet.getCellByPosition(7, 5);
            xCell.setFormula(data.get(5).toString());

            xCell = xSpreadsheet.getCellByPosition(2, 6);
            xCell.setFormula(data.get(10).toString());

            xCell = xSpreadsheet.getCellByPosition(7, 6);
            xCell.setFormula(data.get(4).toString());

            //----------------Заполнение тела документа

            int nCol = 1;
            int nRow = 9;
            int[] num = {2, 3, 5, 7, 8, 11, 12, 13, 14};

            for (Object row : dataElement) {
                for (int i = 0; i < num.length; i++) {
                    xCell = xSpreadsheet.getCellByPosition(nCol++, nRow);
                    xCell.setFormula(((Vector) row).get(num[i]) != null ? ((Vector) row).get(num[i]).toString() : "");
                    if (num[i] == 8)
                        setNumberFormat(xSpreadsheetDocument, xCell, UtilZPlata.getFormatRoundingNorm(UtilZPlata.ROUNDING_NORM).replace(".", ","));
                    if (num[i] == 12)
                        setNumberFormat(xSpreadsheetDocument, xCell, "0,00");
                    xPropSet = UnoRuntime.queryInterface(XPropertySet.class, xCell);
                    xPropSet.setPropertyValue("TableBorder", aBorder);
                }
                nRow++;
                nCol = 1;
            }

        } catch (Exception e) {
            log.error("Ошибка при заполнении отчёта", e);
            throw new Exception("Ошибка при формировании отчёта ", e);
        }
    }

    private void fildZPlataTable(XComponent currentDocument) throws Exception {
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

            int nCol = 0;
            for (int i = 2; i < tModel.getColumnCount(); i++) {
                if (elements2.contains(i)) {
                    xCell = xSpreadsheet.getCellByPosition(nCol++, 1);
                    xCell.setFormula(tModel.getColumnName(i));
                    xPropSet = UnoRuntime.queryInterface(XPropertySet.class, xCell);
                    xPropSet.setPropertyValue("TableBorder", aBorder);
                }
            }

            //----------------Заполнение тела документа
            nCol = 0;
            int nRow = 2;
            for (Object row : tModel.getDataVector()) {
                if (((Vector) row).get(0).toString().equals("true")) {
                    for (int i = 2; i < tModel.getColumnCount(); i++) {
                        if (elements2.contains(i)) {
                            xCell = xSpreadsheet.getCellByPosition(nCol++, nRow);
                            xCell.setFormula(((Vector) row).get(i).toString());
                            xPropSet = UnoRuntime.queryInterface(XPropertySet.class, xCell);
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

    private void fildVedomostZPlataNV(XComponent currentDocument) throws Exception {
        try {
            int nRow = 4;
            int nCol = 0;
            int allnCol = 0;
            int nKol = 0;
            int allnKol = 0;
            double allNormV = 0;
            Vector dataDate = new Vector();
            String[] temp = {"ВСЕГО", "Расценка", "Сумма", "Норма вр.", "Нормо-ч"};
            String model = "";
            String norm = "";
            String operac = "";

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

            //----------------модели,операции, итого

            for (Object row : dataElement) {
                if (!model.equals(((Vector) row).get(1).toString()) || !norm.equals(((Vector) row).get(4).toString()) || !operac.equals(((Vector) row).get(5).toString())) {
                    nCol++;
                    model = ((Vector) row).get(1).toString();
                    norm = ((Vector) row).get(4).toString();
                    operac = ((Vector) row).get(5).toString();
                }

                xCell = xSpreadsheet.getCellByPosition(nCol, nRow);
                xCell.setValue(Integer.valueOf(((Vector) row).get(1).toString()));
                xPropSet = UnoRuntime.queryInterface(XPropertySet.class, xCell);
                xPropSet.setPropertyValue("TableBorder", aBorder);

                xCell = xSpreadsheet.getCellByPosition(nCol, nRow + 1);
                xCell.setFormula(((Vector) row).get(5).toString());
                xPropSet = UnoRuntime.queryInterface(XPropertySet.class, xCell);
                xPropSet.setPropertyValue("TableBorder", aBorder);

                if (!dataDate.contains(((Vector) row).get(0).toString()))
                    dataDate.add(((Vector) row).get(0).toString());
            }

            nCol++;
            xCell = xSpreadsheet.getCellByPosition(nCol, nRow);
            xCell.setFormula("Итого");
            xPropSet = UnoRuntime.queryInterface(XPropertySet.class, xCell);
            xPropSet.setPropertyValue("TableBorder", aBorder);

            xCell = xSpreadsheet.getCellByPosition(nCol, nRow + 1);
            xPropSet = UnoRuntime.queryInterface(XPropertySet.class, xCell);
            xPropSet.setPropertyValue("TableBorder", aBorder);

            allnCol = nCol;

            //----------------рабочие дни

            Collections.sort(dataDate);

            nCol = 0;
            nRow += 2;
            for (Object element : dataDate) {
                xCell = xSpreadsheet.getCellByPosition(nCol, nRow);
                xCell.setFormula("'" + element.toString());
                xPropSet = UnoRuntime.queryInterface(XPropertySet.class, xCell);
                xPropSet.setPropertyValue("TableBorder", aBorder);

                nRow++;
            }

            //----------------ВСЕГО и т.д.

            nRow++;
            for (int i = 0; i < temp.length; i++) {
                xCell = xSpreadsheet.getCellByPosition(nCol, nRow);
                xCell.setFormula(temp[i]);
                xPropSet = UnoRuntime.queryInterface(XPropertySet.class, xCell);
                xPropSet.setPropertyValue("TableBorder", aBorder);

                nRow++;
            }

            //----------------Заполнение тела документа

            nCol = 0;
            nKol = 0;
            allnKol = 0;
            model = "";
            norm = "";
            operac = "";

            for (Object row : dataElement) {
                if (!model.equals(((Vector) row).get(1).toString()) || !norm.equals(((Vector) row).get(4).toString()) || !operac.equals(((Vector) row).get(5).toString())) {
                    nCol++;
                    allNormV += !norm.equals("") ? Double.valueOf(UtilZPlata.formatNorm(Double.valueOf(Double.valueOf(norm) * nKol), 2)) : 0;
                    nKol = 0;
                    model = ((Vector) row).get(1).toString();
                    norm = ((Vector) row).get(4).toString();
                    operac = ((Vector) row).get(5).toString();

                    for (int i = 6; i < dataDate.size() + 6; i++) {
                        xCell = xSpreadsheet.getCellByPosition(nCol, i);
                        xPropSet = UnoRuntime.queryInterface(XPropertySet.class, xCell);
                        xPropSet.setPropertyValue("TableBorder", aBorder);
                    }
                }

                for (int i = 6; i < dataDate.size() + 6; i++) {
                    xCell = xSpreadsheet.getCellByPosition(0, i);
                    if ((((Vector) row).get(0).toString()).equals(xCell.getFormula().replace("'", ""))) {
                        xCell = xSpreadsheet.getCellByPosition(nCol, i);

                        xCell.setValue(Integer.valueOf(((Vector) row).get(2).toString()));
                        nKol += Integer.valueOf(((Vector) row).get(2).toString());
                        allnKol += Integer.valueOf(((Vector) row).get(2).toString());
                    }
                }

                nRow = dataDate.size() + 7;
                for (int i = 0; i < temp.length; i++) {
                    xCell = xSpreadsheet.getCellByPosition(nCol, nRow);
                    switch (i) {
                        case 0:
                            xCell.setValue(nKol);
                            break;
                        case 3:
                            xCell.setValue((Double.valueOf(UtilZPlata.formatNorm(Double.valueOf(norm), UtilZPlata.ROUNDING_NORM))));
                            setNumberFormat(xSpreadsheetDocument, xCell, UtilZPlata.getFormatRoundingNorm(UtilZPlata.ROUNDING_NORM).replace(".", ","));
                            break;
                        case 4:
                            xCell.setValue(Double.valueOf(UtilZPlata.formatNorm(Double.valueOf(norm) * nKol, 2)));
                            setNumberFormat(xSpreadsheetDocument, xCell, UtilZPlata.getFormatRoundingNorm(2).replace(".", ","));
                            break;
                        default:
                            break;
                    }

                    xPropSet = UnoRuntime.queryInterface(XPropertySet.class, xCell);
                    xPropSet.setPropertyValue("TableBorder", aBorder);

                    nRow++;
                }
            }
            allNormV += !norm.equals("") ? Double.valueOf(UtilZPlata.formatNorm(Double.valueOf(Double.valueOf(norm) * nKol), 2)) : 0;

            //----------------Заполнение ИТОГО документа
            nCol++;
            for (int i = 6; i < nRow; i++) {
                xCell = xSpreadsheet.getCellByPosition(nCol, i);

                xPropSet = UnoRuntime.queryInterface(XPropertySet.class, xCell);
                xPropSet.setPropertyValue("TableBorder", aBorder);
            }

            nRow = dataDate.size() + 7;
            for (int i = 0; i < temp.length; i++) {
                xCell = xSpreadsheet.getCellByPosition(nCol, nRow);
                switch (i) {
                    case 0:
                        xCell.setValue(nameTamplates.equals("1") ? allnKol : Integer.valueOf(data.get(3).toString()));
                        break;
                    case 4:
                        xCell.setValue(Double.valueOf(allNormV));
                        setNumberFormat(xSpreadsheetDocument, xCell, UtilZPlata.getFormatRoundingNorm(2).replace(".", ","));
                        break;
                    default:
                        break;
                }
                xPropSet = UnoRuntime.queryInterface(XPropertySet.class, xCell);
                xPropSet.setPropertyValue("TableBorder", aBorder);

                nRow++;
            }

            //------------------------------Заполнение части документа БРАК
            if (!elements1.isEmpty()) {
                nCol = 0;
                nKol = 0;
                nRow += 2;

                xCell = xSpreadsheet.getCellByPosition(nCol, nRow);
                xCell.setFormula("Модель");
                xPropSet = UnoRuntime.queryInterface(XPropertySet.class, xCell);
                xPropSet.setPropertyValue("TableBorder", aBorder);

                xCell = xSpreadsheet.getCellByPosition(nCol++, nRow + 1);
                xCell.setFormula("Брак");
                xPropSet = UnoRuntime.queryInterface(XPropertySet.class, xCell);
                xPropSet.setPropertyValue("TableBorder", aBorder);

                for (Object row : elements1) {
                    xCell = xSpreadsheet.getCellByPosition(nCol, nRow);
                    xCell.setFormula(((Vector) row).get(0).toString());
                    xPropSet = UnoRuntime.queryInterface(XPropertySet.class, xCell);
                    xPropSet.setPropertyValue("TableBorder", aBorder);

                    xCell = xSpreadsheet.getCellByPosition(nCol++, nRow + 1);
                    xCell.setValue(Integer.valueOf(((Vector) row).get(1).toString()));
                    xPropSet = UnoRuntime.queryInterface(XPropertySet.class, xCell);
                    xPropSet.setPropertyValue("TableBorder", aBorder);

                    nKol += Integer.valueOf(((Vector) row).get(1).toString());
                }

                //------------------------------Заполнение части документа БРАК ИТОГО

                xCell = xSpreadsheet.getCellByPosition(nCol, nRow);
                xCell.setFormula("Итого");
                xPropSet = UnoRuntime.queryInterface(XPropertySet.class, xCell);
                xPropSet.setPropertyValue("TableBorder", aBorder);

                xCell = xSpreadsheet.getCellByPosition(nCol, nRow + 1);
                xCell.setValue(nKol);
                xPropSet = UnoRuntime.queryInterface(XPropertySet.class, xCell);
                xPropSet.setPropertyValue("TableBorder", aBorder);

            }

            //------------------------------Заполнение шапки документа

            aBorder.TopLine = aBorder.LeftLine = aBorder.RightLine = null;
            aBorder.BottomLine = aLine;

            for (int i = 0; i < allnCol - 1; i++) {
                xCell = xSpreadsheet.getCellByPosition(i + 4, 1);
                xCell.setFormula("НАКОПИТЕЛЬНАЯ КАРТОЧКА №________ВЫРАБОТКИ И ЗАРПЛАТЫ");

                xCell = xSpreadsheet.getCellByPosition(i + 3, 2);
                xCell.setFormula("Цеха");

                for (int j = i + 4; j < i + 9; j++) {
                    xCell = xSpreadsheet.getCellByPosition(j, 2);
                    xPropSet = UnoRuntime.queryInterface(XPropertySet.class, xCell);
                    xPropSet.setPropertyValue("TableBorder", aBorder);
                }

                xCell = xSpreadsheet.getCellByPosition(i + 4, 2);
                xCell.setFormula(data.get(0).toString().toLowerCase());

                xCell = xSpreadsheet.getCellByPosition(i + 9, 2);
                xCell.setFormula(", бригады");

                for (int j = i + 10; j < i + 13; j++) {
                    xCell = xSpreadsheet.getCellByPosition(j, 2);
                    xPropSet = UnoRuntime.queryInterface(XPropertySet.class, xCell);
                    xPropSet.setPropertyValue("TableBorder", aBorder);
                }

                xCell = xSpreadsheet.getCellByPosition(i + 11, 2);
                xCell.setFormula(data.get(1).toString().toLowerCase());

                xCell = xSpreadsheet.getCellByPosition(i + 14, 2);
                xCell.setFormula(" за ");

                for (int j = i + 15; j < i + 19; j++) {
                    xCell = xSpreadsheet.getCellByPosition(j, 2);
                    xPropSet = UnoRuntime.queryInterface(XPropertySet.class, xCell);
                    xPropSet.setPropertyValue("TableBorder", aBorder);
                }

                xCell = xSpreadsheet.getCellByPosition(i + 15, 2);
                xCell.setFormula(data.get(2).toString().toLowerCase());

                i += 22;
            }

            //----------------Заполнение колонтитула документа

            String text =
                    UtilZPlata.PROF_STAFF_3 + " " + UtilZPlata.STAFF_3 + " __________________________" + "         "
                            + UtilZPlata.PROF_STAFF_4 + " " + UtilZPlata.STAFF_4 + " __________________________" + "         "
                            + UtilZPlata.PROF_STAFF_5 + " " + UtilZPlata.STAFF_5 + " __________________________";

            insertTextInFooter(xSpreadsheetDocument, text);

        } catch (Exception e) {
            log.error("Ошибка при заполнении отчёта", e);
            throw new Exception("Ошибка при формировании отчёта ", e);
        }
    }

    private void fildVedomostZPlataNVEmpl(XComponent currentDocument) throws Exception {
        try {
            int nRow = 4;
            int nCol = 0;
            int allnCol = 0;
            int nKol = 0;
            double nVyr = 0;
            double allVyr = 0;
            double tempVyrb;
            Vector dataEmployer = new Vector();
            String model = "";
            String norm = "";
            String operac = "";

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

            //----------------модели,операции, итого

            for (Object row : dataElement) {
                if (!model.equals(((Vector) row).get(1).toString()) || !norm.equals(((Vector) row).get(4).toString()) || !operac.equals(((Vector) row).get(5).toString())) {
                    nCol++;
                    model = ((Vector) row).get(1).toString();
                    norm = ((Vector) row).get(4).toString();
                    operac = ((Vector) row).get(5).toString();

                    xCell = xSpreadsheet.getCellByPosition(nCol, nRow);
                    xCell.setValue(Integer.valueOf(((Vector) row).get(1).toString()));
                    xPropSet = UnoRuntime.queryInterface(XPropertySet.class, xCell);
                    xPropSet.setPropertyValue("TableBorder", aBorder);

                    xCell = xSpreadsheet.getCellByPosition(nCol, nRow + 1);
                    xCell.setFormula(((Vector) row).get(5).toString());
                    xPropSet = UnoRuntime.queryInterface(XPropertySet.class, xCell);
                    xPropSet.setPropertyValue("TableBorder", aBorder);

                    xCell = xSpreadsheet.getCellByPosition(nCol, nRow + 2);
                    xCell.setValue(Double.valueOf(UtilZPlata.formatNorm(Double.valueOf(norm), UtilZPlata.ROUNDING_NORM)));
                    setNumberFormat(xSpreadsheetDocument, xCell, UtilZPlata.getFormatRoundingNorm(UtilZPlata.ROUNDING_NORM).replace(".", ","));
                    xPropSet = UnoRuntime.queryInterface(XPropertySet.class, xCell);
                    xPropSet.setPropertyValue("TableBorder", aBorder);

                    xCell = xSpreadsheet.getCellByPosition(nCol, nRow + 3);
                    xCell.setFormula("кол-во");
                    xPropSet = UnoRuntime.queryInterface(XPropertySet.class, xCell);
                    xPropSet.setPropertyValue("TableBorder", aBorder);

                    nCol++;
                    xCell = xSpreadsheet.getCellByPosition(nCol, nRow + 3);
                    xCell.setFormula("выраб.");
                    xPropSet = UnoRuntime.queryInterface(XPropertySet.class, xCell);
                    xPropSet.setPropertyValue("TableBorder", aBorder);
                }

                if (!dataEmployer.contains(((Vector) row).get(0).toString()))
                    dataEmployer.add(((Vector) row).get(0).toString());
            }

            nCol++;
            xCell = xSpreadsheet.getCellByPosition(nCol, nRow);
            xCell.setFormula("Итого");
            xPropSet = UnoRuntime.queryInterface(XPropertySet.class, xCell);
            xPropSet.setPropertyValue("TableBorder", aBorder);

            xCell = xSpreadsheet.getCellByPosition(nCol, nRow + 1);
            xPropSet = UnoRuntime.queryInterface(XPropertySet.class, xCell);
            xPropSet.setPropertyValue("TableBorder", aBorder);

            xCell = xSpreadsheet.getCellByPosition(nCol, nRow + 2);
            xPropSet = UnoRuntime.queryInterface(XPropertySet.class, xCell);
            xPropSet.setPropertyValue("TableBorder", aBorder);

            xCell = xSpreadsheet.getCellByPosition(nCol, nRow + 3);
            xPropSet = UnoRuntime.queryInterface(XPropertySet.class, xCell);
            xPropSet.setPropertyValue("TableBorder", aBorder);

            allnCol = nCol;

            //----------------сотрудники

            Collections.sort(dataEmployer);

            nCol = 0;
            nRow += 4;
            for (Object element : dataEmployer) {
                xCell = xSpreadsheet.getCellByPosition(nCol, nRow);
                xCell.setFormula("'" + element.toString());
                xPropSet = UnoRuntime.queryInterface(XPropertySet.class, xCell);
                xPropSet.setPropertyValue("TableBorder", aBorder);

                nRow++;
            }

            //----------------ВСЕГО и т.д.

            nRow++;
            xCell = xSpreadsheet.getCellByPosition(nCol, nRow);
            xCell.setFormula("ВСЕГО");
            xPropSet = UnoRuntime.queryInterface(XPropertySet.class, xCell);
            xPropSet.setPropertyValue("TableBorder", aBorder);

            //----------------Заполнение тела документа

            nCol = -1;
            nRow = dataEmployer.size() + 9;
            nVyr = 0;
            nKol = 0;
            model = "";
            norm = "";
            operac = "";

            for (Object row : dataElement) {
                if (!model.equals(((Vector) row).get(1).toString()) || !norm.equals(((Vector) row).get(4).toString()) || !operac.equals(((Vector) row).get(5).toString())) {
                    nCol += 2;
                    nKol = 0;
                    nVyr = 0;
                    model = ((Vector) row).get(1).toString();
                    norm = ((Vector) row).get(4).toString();
                    operac = ((Vector) row).get(5).toString();

                    for (int i = 8; i < dataEmployer.size() + 8; i++) {
                        xCell = xSpreadsheet.getCellByPosition(nCol, i);
                        xPropSet = UnoRuntime.queryInterface(XPropertySet.class, xCell);
                        xPropSet.setPropertyValue("TableBorder", aBorder);

                        xCell = xSpreadsheet.getCellByPosition(nCol + 1, i);
                        xPropSet = UnoRuntime.queryInterface(XPropertySet.class, xCell);
                        xPropSet.setPropertyValue("TableBorder", aBorder);
                    }
                }

                for (int i = 8; i < dataEmployer.size() + 8; i++) {
                    xCell = xSpreadsheet.getCellByPosition(0, i);
                    if ((((Vector) row).get(0).toString()).equals(xCell.getFormula().replace("'", ""))) {
                        tempVyrb = 0;

                        xCell = xSpreadsheet.getCellByPosition(nCol, i);
                        xCell.setValue(Integer.valueOf(((Vector) row).get(2).toString()));

                        tempVyrb = Double.valueOf(UtilZPlata.formatNorm(Double.valueOf(((Vector) row).get(2).toString()) *
                                (Double.valueOf(UtilZPlata.formatNorm(Double.valueOf(norm), UtilZPlata.ROUNDING_NORM))), 2));

                        xCell = xSpreadsheet.getCellByPosition(nCol + 1, i);
                        xCell.setValue(tempVyrb);
                        setNumberFormat(xSpreadsheetDocument, xCell, UtilZPlata.getFormatRoundingNorm(2).replace(".", ","));

                        nKol += Integer.valueOf(((Vector) row).get(2).toString());
                        nVyr += tempVyrb;
                        allVyr += tempVyrb;
                    }
                }

                xCell = xSpreadsheet.getCellByPosition(nCol, nRow);
                xCell.setValue(nKol);
                xPropSet = UnoRuntime.queryInterface(XPropertySet.class, xCell);
                xPropSet.setPropertyValue("TableBorder", aBorder);

                xCell = xSpreadsheet.getCellByPosition(nCol + 1, nRow);
                xCell.setValue((Double.valueOf(UtilZPlata.formatNorm(Double.valueOf(nVyr), 2))));
                setNumberFormat(xSpreadsheetDocument, xCell, UtilZPlata.getFormatRoundingNorm(2).replace(".", ","));
                xPropSet = UnoRuntime.queryInterface(XPropertySet.class, xCell);
                xPropSet.setPropertyValue("TableBorder", aBorder);

            }

            nCol += 2;
            for (int i = 8; i < nRow - 1; i++) {
                nVyr = 0;
                for (int j = 2; j < allnCol; j += 2) {
                    xCell = xSpreadsheet.getCellByPosition(j, i);
                    nVyr += Double.valueOf(UtilZPlata.formatNorm(Double.valueOf(xCell.getValue()), 2));
                }
                xCell = xSpreadsheet.getCellByPosition(nCol, i);
                xCell.setValue((Double.valueOf(UtilZPlata.formatNorm(Double.valueOf(nVyr), 2))));
                setNumberFormat(xSpreadsheetDocument, xCell, UtilZPlata.getFormatRoundingNorm(2).replace(".", ","));
                xPropSet = UnoRuntime.queryInterface(XPropertySet.class, xCell);
                xPropSet.setPropertyValue("TableBorder", aBorder);
            }

            nRow = dataEmployer.size() + 9;
            xCell = xSpreadsheet.getCellByPosition(nCol, nRow);
            xCell.setValue((Double.valueOf(UtilZPlata.formatNorm(Double.valueOf(allVyr), 2))));
            setNumberFormat(xSpreadsheetDocument, xCell, UtilZPlata.getFormatRoundingNorm(2).replace(".", ","));
            xPropSet = UnoRuntime.queryInterface(XPropertySet.class, xCell);
            xPropSet.setPropertyValue("TableBorder", aBorder);

            //------------------------------Заполнение шапки документа

            aBorder.TopLine = aBorder.LeftLine = aBorder.RightLine = null;
            aBorder.BottomLine = aLine;

            for (int i = 0; i < allnCol - 1; i++) {
                xCell = xSpreadsheet.getCellByPosition(i + 3, 1);
                xCell.setFormula("НАКОПИТЕЛЬНАЯ КАРТОЧКА №________ВЫРАБОТКИ И ЗАРПЛАТЫ");

                xCell = xSpreadsheet.getCellByPosition(i + 2, 2);
                xCell.setFormula("Цеха");

                for (int j = i + 3; j < i + 8; j++) {
                    xCell = xSpreadsheet.getCellByPosition(j, 2);
                    xPropSet = UnoRuntime.queryInterface(XPropertySet.class, xCell);
                    xPropSet.setPropertyValue("TableBorder", aBorder);
                }

                xCell = xSpreadsheet.getCellByPosition(i + 3, 2);
                xCell.setFormula(data.get(0).toString().toLowerCase());

                xCell = xSpreadsheet.getCellByPosition(i + 8, 2);
                xCell.setFormula(", бригады");

                for (int j = i + 10; j < i + 13; j++) {
                    xCell = xSpreadsheet.getCellByPosition(j, 2);
                    xPropSet = UnoRuntime.queryInterface(XPropertySet.class, xCell);
                    xPropSet.setPropertyValue("TableBorder", aBorder);
                }

                xCell = xSpreadsheet.getCellByPosition(i + 10, 2);
                xCell.setFormula(data.get(1).toString().toLowerCase());

                xCell = xSpreadsheet.getCellByPosition(i + 13, 2);
                xCell.setFormula(" за ");

                for (int j = i + 14; j < i + 20; j++) {
                    xCell = xSpreadsheet.getCellByPosition(j, 2);
                    xPropSet = UnoRuntime.queryInterface(XPropertySet.class, xCell);
                    xPropSet.setPropertyValue("TableBorder", aBorder);
                }

                xCell = xSpreadsheet.getCellByPosition(i + 14, 2);
                xCell.setFormula(data.get(2).toString().toLowerCase());

                i += 31;
            }
        } catch (Exception e) {
            log.error("Ошибка при заполнении отчёта", e);
            throw new Exception("Ошибка при формировании отчёта ", e);
        }
    }

    private void fildVedomostZPlataNVEmplKr(XComponent currentDocument) throws Exception {
        try {
            int nRow = 4;
            int nCol = 0;
            int allnCol = 0;
            double nVyr = 0;
            double allVyr = 0;
            Vector dataEmployer = new Vector();
            String model = "";

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

            //----------------модели,операции, итого

            for (Object row : dataElement) {
                if (!model.equals(((Vector) row).get(1).toString())) {
                    nCol++;
                    model = ((Vector) row).get(1).toString();

                    xCell = xSpreadsheet.getCellByPosition(nCol, nRow);
                    xCell.setValue(Integer.valueOf(((Vector) row).get(1).toString()));
                    xPropSet = UnoRuntime.queryInterface(XPropertySet.class, xCell);
                    xPropSet.setPropertyValue("TableBorder", aBorder);

                    xCell = xSpreadsheet.getCellByPosition(nCol, nRow + 1);
                    xCell.setFormula("кол-во");
                    xPropSet = UnoRuntime.queryInterface(XPropertySet.class, xCell);
                    xPropSet.setPropertyValue("TableBorder", aBorder);

                    nCol++;
                    xCell = xSpreadsheet.getCellByPosition(nCol, nRow + 1);
                    xCell.setFormula("выраб.");
                    xPropSet = UnoRuntime.queryInterface(XPropertySet.class, xCell);
                    xPropSet.setPropertyValue("TableBorder", aBorder);
                }

                if (!dataEmployer.contains(((Vector) row).get(0).toString()))
                    dataEmployer.add(((Vector) row).get(0).toString());
            }

            nCol++;
            xCell = xSpreadsheet.getCellByPosition(nCol, nRow);
            xCell.setFormula("Итого");
            xPropSet = UnoRuntime.queryInterface(XPropertySet.class, xCell);
            xPropSet.setPropertyValue("TableBorder", aBorder);

            xCell = xSpreadsheet.getCellByPosition(nCol, nRow + 1);
            xCell.setFormula("выраб.");
            xPropSet = UnoRuntime.queryInterface(XPropertySet.class, xCell);
            xPropSet.setPropertyValue("TableBorder", aBorder);

            allnCol = nCol;

            //----------------сотрудники

            Collections.sort(dataEmployer);

            nCol = 0;
            nRow += 2;
            for (Object element : dataEmployer) {
                xCell = xSpreadsheet.getCellByPosition(nCol, nRow);
                xCell.setFormula(element.toString());
                xPropSet = UnoRuntime.queryInterface(XPropertySet.class, xCell);
                xPropSet.setPropertyValue("TableBorder", aBorder);

                nRow++;
            }

            //----------------ВСЕГО и т.д.

            nRow++;
            xCell = xSpreadsheet.getCellByPosition(nCol, nRow);
            xCell.setFormula("ВСЕГО");
            xPropSet = UnoRuntime.queryInterface(XPropertySet.class, xCell);
            xPropSet.setPropertyValue("TableBorder", aBorder);

            //----------------Заполнение тела документа

            nCol = -1;
            nRow = dataEmployer.size() + 7;
            nVyr = 0;
            model = "";

            for (Object row : dataElement) {
                if (!model.equals(((Vector) row).get(1).toString())) {
                    nCol += 2;
                    model = ((Vector) row).get(1).toString();
                    nVyr = 0;

                    for (int i = 6; i < dataEmployer.size() + 6; i++) {
                        xCell = xSpreadsheet.getCellByPosition(nCol, i);
                        xPropSet = UnoRuntime.queryInterface(XPropertySet.class, xCell);
                        xPropSet.setPropertyValue("TableBorder", aBorder);

                        xCell = xSpreadsheet.getCellByPosition(nCol + 1, i);
                        xPropSet = UnoRuntime.queryInterface(XPropertySet.class, xCell);
                        xPropSet.setPropertyValue("TableBorder", aBorder);
                    }
                }

                for (int i = 6; i < dataEmployer.size() + 6; i++) {
                    xCell = xSpreadsheet.getCellByPosition(0, i);
                    if ((((Vector) row).get(0).toString()).equals(xCell.getFormula())) {
                        xCell = xSpreadsheet.getCellByPosition(nCol, i);
                        xCell.setValue(Integer.valueOf(((Vector) row).get(2).toString()));

                        xCell = xSpreadsheet.getCellByPosition(nCol + 1, i);
                        xCell.setValue(Double.valueOf(UtilZPlata.formatNorm(Double.valueOf(((Vector) row).get(3).toString()), 2)));
                        setNumberFormat(xSpreadsheetDocument, xCell, UtilZPlata.getFormatRoundingNorm(2).replace(".", ","));
                        nVyr += Double.valueOf(UtilZPlata.formatNorm(Double.valueOf(((Vector) row).get(3).toString()), 2));
                        allVyr += Double.valueOf(UtilZPlata.formatNorm(Double.valueOf(((Vector) row).get(3).toString()), 2));
                    }
                }

                xCell = xSpreadsheet.getCellByPosition(nCol, nRow);
                xPropSet = UnoRuntime.queryInterface(XPropertySet.class, xCell);
                xPropSet.setPropertyValue("TableBorder", aBorder);

                xCell = xSpreadsheet.getCellByPosition(nCol + 1, nRow);
                xCell.setValue((Double.valueOf(UtilZPlata.formatNorm(Double.valueOf(nVyr), 2))));
                setNumberFormat(xSpreadsheetDocument, xCell, UtilZPlata.getFormatRoundingNorm(2).replace(".", ","));
                xPropSet = UnoRuntime.queryInterface(XPropertySet.class, xCell);
                xPropSet.setPropertyValue("TableBorder", aBorder);

            }

            //----------------Заполнение ИТОГО документа
            nCol += 2;
            for (int i = 6; i < nRow - 1; i++) {
                nVyr = 0;
                for (int j = 2; j < allnCol; j += 2) {
                    xCell = xSpreadsheet.getCellByPosition(j, i);
                    nVyr += Double.valueOf(UtilZPlata.formatNorm(Double.valueOf(xCell.getValue()), 2));
                }
                xCell = xSpreadsheet.getCellByPosition(nCol, i);
                xCell.setValue((Double.valueOf(UtilZPlata.formatNorm(Double.valueOf(nVyr), 2))));
                setNumberFormat(xSpreadsheetDocument, xCell, UtilZPlata.getFormatRoundingNorm(2).replace(".", ","));
                xPropSet = UnoRuntime.queryInterface(XPropertySet.class, xCell);
                xPropSet.setPropertyValue("TableBorder", aBorder);
            }

            nRow = dataEmployer.size() + 7;
            xCell = xSpreadsheet.getCellByPosition(nCol, nRow);
            xCell.setValue((Double.valueOf(UtilZPlata.formatNorm(Double.valueOf(allVyr), 2))));
            setNumberFormat(xSpreadsheetDocument, xCell, UtilZPlata.getFormatRoundingNorm(2).replace(".", ","));
            xPropSet = UnoRuntime.queryInterface(XPropertySet.class, xCell);
            xPropSet.setPropertyValue("TableBorder", aBorder);

            //------------------------------Заполнение шапки документа

            aBorder.TopLine = aBorder.LeftLine = aBorder.RightLine = null;
            aBorder.BottomLine = aLine;

            for (int i = 0; i < allnCol - 1; i++) {
                xCell = xSpreadsheet.getCellByPosition(i + 3, 1);
                xCell.setFormula("НАКОПИТЕЛЬНАЯ КАРТОЧКА №________ВЫРАБОТКИ И ЗАРПЛАТЫ");

                xCell = xSpreadsheet.getCellByPosition(i + 2, 2);
                xCell.setFormula("Цеха");

                for (int j = i + 3; j < i + 8; j++) {
                    xCell = xSpreadsheet.getCellByPosition(j, 2);
                    xPropSet = UnoRuntime.queryInterface(XPropertySet.class, xCell);
                    xPropSet.setPropertyValue("TableBorder", aBorder);
                }

                xCell = xSpreadsheet.getCellByPosition(i + 3, 2);
                xCell.setFormula(data.get(0).toString().toLowerCase());

                xCell = xSpreadsheet.getCellByPosition(i + 8, 2);
                xCell.setFormula(", бригады");

                for (int j = i + 9; j < i + 12; j++) {
                    xCell = xSpreadsheet.getCellByPosition(j, 2);
                    xPropSet = UnoRuntime.queryInterface(XPropertySet.class, xCell);
                    xPropSet.setPropertyValue("TableBorder", aBorder);
                }

                xCell = xSpreadsheet.getCellByPosition(i + 10, 2);
                xCell.setFormula(data.get(1).toString().toLowerCase());

                xCell = xSpreadsheet.getCellByPosition(i + 13, 2);
                xCell.setFormula(" за ");

                for (int j = i + 14; j < i + 18; j++) {
                    xCell = xSpreadsheet.getCellByPosition(j, 2);
                    xPropSet = UnoRuntime.queryInterface(XPropertySet.class, xCell);
                    xPropSet.setPropertyValue("TableBorder", aBorder);
                }

                xCell = xSpreadsheet.getCellByPosition(i + 14, 2);
                xCell.setFormula(data.get(2).toString().toLowerCase());

                i += 23;
            }
        } catch (Exception e) {
            log.error("Ошибка при заполнении отчёта", e);
            throw new Exception("Ошибка при формировании отчёта ", e);
        }
    }

    private void insertTextInFooter(XSpreadsheetDocument xSpreadsheetDocument, String text) throws Exception {
        try {
            XStyleFamiliesSupplier StyleFam = UnoRuntime.queryInterface(XStyleFamiliesSupplier.class, xSpreadsheetDocument);
            XNameAccess StyleFamNames = StyleFam.getStyleFamilies();

            XNameAccess PageStyles = (XNameAccess) AnyConverter.toObject(new Type(XNameAccess.class), StyleFamNames.getByName("PageStyles"));
            XStyle StdStyle = (XStyle) AnyConverter.toObject(new Type(XStyle.class), PageStyles.getByName("Default"));

            XPropertySet xPropSet = UnoRuntime.queryInterface(XPropertySet.class, StdStyle);

            Object obj = xPropSet.getPropertyValue("RightPageFooterContent");
            XHeaderFooterContent RPHFC = (XHeaderFooterContent) AnyConverter.toObject(new com.sun.star.uno.Type(XHeaderFooterContent.class), obj);
            XText center = RPHFC.getCenterText();

            center.setString(text);

            xPropSet.setPropertyValue("RightPageFooterContent", RPHFC);

        } catch (Exception e) {
            throw new Exception("Ошибка при формировании отчёта ", e);
        }
    }

    private void fildVedomostZPlataNVModel(XComponent currentDocument) {
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

            int nRow = 4;
            int nCol = 0;
            int allNCol = 0;
            Vector brigList = new Vector();
            String model = "";
            String num = "";
            String operac = "";

            //----------------модели,операции, итого

            for (Object row : dataElement) {
                if (!model.equals(((Vector) row).get(0).toString()) || !num.equals(((Vector) row).get(1).toString()) || !operac.equals(((Vector) row).get(2).toString())) {
                    nCol++;
                    model = ((Vector) row).get(0).toString();
                    num = ((Vector) row).get(1).toString();
                    operac = ((Vector) row).get(2).toString();
                }

                xCell = xSpreadsheet.getCellByPosition(nCol, nRow);
                xCell.setFormula(((Vector) row).get(0).toString());
                xPropSet = UnoRuntime.queryInterface(XPropertySet.class, xCell);
                xPropSet.setPropertyValue("TableBorder", aBorder);

                xCell = xSpreadsheet.getCellByPosition(nCol, nRow + 1);
                xCell.setFormula(((Vector) row).get(2).toString());
                xPropSet = UnoRuntime.queryInterface(XPropertySet.class, xCell);
                xPropSet.setPropertyValue("TableBorder", aBorder);

                if (!brigList.contains(((Vector) row).get(3).toString()))
                    brigList.add(((Vector) row).get(3).toString());
            }

            nCol++;

            allNCol = nCol;

            //----------------бригады

            Collections.sort(brigList);

            nCol = 0;
            nRow += 2;
            for (Object element : brigList) {
                xCell = xSpreadsheet.getCellByPosition(nCol, nRow);
                xCell.setFormula("'" + element.toString());
                xPropSet = UnoRuntime.queryInterface(XPropertySet.class, xCell);
                xPropSet.setPropertyValue("TableBorder", aBorder);

                nRow++;
            }

            //----------------Заполнение тела документа

            nCol = 0;
            model = "";
            num = "";
            operac = "";

            for (Object row : dataElement) {
                if (!model.equals(((Vector) row).get(0).toString()) || !num.equals(((Vector) row).get(1).toString()) || !operac.equals(((Vector) row).get(2).toString())) {
                    nCol++;

                    model = ((Vector) row).get(0).toString();
                    num = ((Vector) row).get(1).toString();
                    operac = ((Vector) row).get(2).toString();

                    for (int i = 6; i < brigList.size() + 6; i++) {
                        xCell = xSpreadsheet.getCellByPosition(nCol, i);
                        xPropSet = UnoRuntime.queryInterface(XPropertySet.class, xCell);
                        xPropSet.setPropertyValue("TableBorder", aBorder);
                    }
                }

                for (int i = 6; i < brigList.size() + 6; i++) {
                    xCell = xSpreadsheet.getCellByPosition(0, i);
                    if ((((Vector) row).get(3).toString()).equals(xCell.getFormula().replace("'", ""))) {
                        xCell = xSpreadsheet.getCellByPosition(nCol, i);
                        xCell.setValue(Integer.parseInt(((Vector) row).get(4).toString()));
                    }
                }
            }

            //------------------------------Заполнение ИТОГО документа

            nRow = 4;

            xCell = xSpreadsheet.getCellByPosition(allNCol, nRow);
            xCell.setFormula("Кол-во");
            xPropSet = UnoRuntime.queryInterface(XPropertySet.class, xCell);
            xPropSet.setPropertyValue("TableBorder", aBorder);

            xCell = xSpreadsheet.getCellByPosition(allNCol, nRow + 1);
            xPropSet = UnoRuntime.queryInterface(XPropertySet.class, xCell);
            xPropSet.setPropertyValue("TableBorder", aBorder);

            xCell = xSpreadsheet.getCellByPosition(allNCol + 1, nRow);
            xCell.setFormula("Брак");
            xPropSet = UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
            xPropSet.setPropertyValue("TableBorder", aBorder);

            xCell = xSpreadsheet.getCellByPosition(allNCol + 1, nRow + 1);
            xPropSet = UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
            xPropSet.setPropertyValue("TableBorder", aBorder);

            for (Object row : elements1) {
                for (int i = 6; i < brigList.size() + 6; i++) {
                    xCell = xSpreadsheet.getCellByPosition(0, i);

                    if ((((Vector) row).get(1).toString()).equals(xCell.getFormula().replace("'", ""))) {
                        xCell = xSpreadsheet.getCellByPosition(allNCol, i);
                        xCell.setValue(Integer.parseInt(((Vector) row).get(2).toString()));
                    } else {
                        xCell = xSpreadsheet.getCellByPosition(allNCol, i);
                    }

                    xPropSet = UnoRuntime.queryInterface(XPropertySet.class, xCell);
                    xPropSet.setPropertyValue("TableBorder", aBorder);
                }
            }

            for (Object row : elements2) {
                for (int i = 6; i < brigList.size() + 6; i++) {
                    xCell = xSpreadsheet.getCellByPosition(0, i);

                    if ((((Vector) row).get(1).toString()).equals(xCell.getFormula().replace("'", ""))) {
                        xCell = xSpreadsheet.getCellByPosition(allNCol + 1, i);
                        xCell.setValue(Integer.parseInt(((Vector) row).get(2).toString()));
                    } else {
                        xCell = xSpreadsheet.getCellByPosition(allNCol + 1, i);
                    }

                    xPropSet = UnoRuntime.queryInterface(XPropertySet.class, xCell);
                    xPropSet.setPropertyValue("TableBorder", aBorder);
                }
            }

            nRow = brigList.size() + 6;

            xCell = xSpreadsheet.getCellByPosition(0, nRow);
            xCell.setFormula("Итого");
            xPropSet = UnoRuntime.queryInterface(XPropertySet.class, xCell);
            xPropSet.setPropertyValue("TableBorder", aBorder);

            for (int i = 2; i < allNCol + 3; i++) {

                xCell = xSpreadsheet.getCellByPosition(i - 1, nRow);
                xCell.setFormula("=SUM(INDIRECT(ADDRESS(" + 6 + ";" + i + ")&\":\"&ADDRESS(" + nRow + ";" + i + ")))");

                xPropSet = UnoRuntime.queryInterface(XPropertySet.class, xCell);
                xPropSet.setPropertyValue("TableBorder", aBorder);
            }

            //------------------------------Заполнение шапки документа

            aBorder.TopLine = aBorder.LeftLine = aBorder.RightLine = null;
            aBorder.BottomLine = aLine;

            for (int i = 0; i < allNCol - 1; i++) {
                xCell = xSpreadsheet.getCellByPosition(i + 4, 1);
                xCell.setFormula("НАКОПИТЕЛЬНАЯ КАРТОЧКА №________ВЫРАБОТКИ И ЗАРПЛАТЫ");

                xCell = xSpreadsheet.getCellByPosition(i + 3, 2);
                xCell.setFormula("Цеха");

                for (int j = i + 4; j < i + 9; j++) {
                    xCell = xSpreadsheet.getCellByPosition(j, 2);
                    xPropSet = UnoRuntime.queryInterface(XPropertySet.class, xCell);
                    xPropSet.setPropertyValue("TableBorder", aBorder);
                }

                xCell = xSpreadsheet.getCellByPosition(i + 4, 2);
                xCell.setFormula(data.get(0).toString().toLowerCase());


                xCell = xSpreadsheet.getCellByPosition(i + 9, 2);
                xCell.setFormula(", бригады");

                for (int j = i + 10; j < i + 13; j++) {
                    xCell = xSpreadsheet.getCellByPosition(j, 2);
                    xPropSet = UnoRuntime.queryInterface(XPropertySet.class, xCell);
                    xPropSet.setPropertyValue("TableBorder", aBorder);
                }

                xCell = xSpreadsheet.getCellByPosition(i + 11, 2);
                xCell.setFormula(data.get(1).toString().toLowerCase());

                xCell = xSpreadsheet.getCellByPosition(i + 14, 2);
                xCell.setFormula(" за ");

                for (int j = i + 15; j < i + 19; j++) {
                    xCell = xSpreadsheet.getCellByPosition(j, 2);
                    xPropSet = UnoRuntime.queryInterface(XPropertySet.class, xCell);
                    xPropSet.setPropertyValue("TableBorder", aBorder);
                }

                xCell = xSpreadsheet.getCellByPosition(i + 15, 2);
                xCell.setFormula(data.get(2).toString().toLowerCase());

                i += 22;
            }

        } catch (Exception e) {
            log.error("Ошибка при заполнении отчёта", e);
        }
    }

    private void createTemplateForNVBrig(XComponent currentDocument) {
//        String[] colums = new String[]{"Бригада", "ФИО", "Отработанное время", "Выработка по бригаде", "% выполнения нормы", "% премии"};
//        String[] rows = new String[]{"101", "102", "103", "104", "105", "106", "121", "125"};
//        try {
//            XSpreadsheetDocument xSpreadsheetDocument = UnoRuntime.queryInterface(XSpreadsheetDocument.class, currentDocument);
//            XSpreadsheets xSpreadsheets = xSpreadsheetDocument.getSheets();
//            Object sheet = xSpreadsheets.getByName("Лист1");
//            XSpreadsheet xSpreadsheet = UnoRuntime.queryInterface(XSpreadsheet.class, sheet);
//            com.sun.star.beans.XPropertySet xPropSet = null;
//
//            com.sun.star.table.BorderLine aLine = new com.sun.star.table.BorderLine();
//
//            XCell xCell;
//
//            for(int i = 0; i < colums.length; i++){
//                xCell = xSpreadsheet.getCellByPosition(i, 0);
//                xCell.setFormula(colums[i]);
//            }
//
//            for(int i = 1; i <= rows.length; i++){
//                xCell = xSpreadsheet.getCellByPosition(0, i);
//                xCell.setFormula(rows[i-1]);
//
//                int s = i+1;
//
//                xCell = xSpreadsheet.getCellByPosition(4, i);
//                xCell.setFormula("=IF(OR(D" + s + "=0;C" + s + "=0);\"-\";D" + s + "/C" + s + ")");
//
//                xCell = xSpreadsheet.getCellByPosition(5, i);
//                xCell.setFormula("=IF(" +
//                        "OR(E" + s + "=\"-\";E" + s + "<=85%);\"90%\";" +
//                        "IF(E" + s + "<=90%;\"115%\";" +
//                        "IF(E" + s + "<=95%;\"145%\";" +
//                        "IF(E" + s + "<=100%;\"170%\";\"190%\"))))");
//            }
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
    }
}