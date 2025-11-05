package dept.sklad;

import by.march8.ecs.framework.common.LogCrutch;
import com.sun.star.beans.XPropertySet;
import com.sun.star.container.XEnumeration;
import com.sun.star.container.XEnumerationAccess;
import com.sun.star.container.XNamed;
import com.sun.star.lang.XComponent;
import com.sun.star.lang.XServiceInfo;
import com.sun.star.sheet.XSpreadsheet;
import com.sun.star.sheet.XSpreadsheetDocument;
import com.sun.star.sheet.XSpreadsheets;
import com.sun.star.table.CellHoriJustify;
import com.sun.star.table.TableBorder;
import com.sun.star.table.XCell;
import com.sun.star.table.XCellRange;
import com.sun.star.text.XTextFieldsSupplier;
import com.sun.star.uno.UnoRuntime;
import com.sun.star.util.XRefreshable;
import common.MoneyToStr;
import dept.MyReportsModule;
import dept.sklad.model.DogovorInfo;
import workOO.OO_new;

import javax.swing.*;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author vova
 */
public class SkladOO extends OO_new {
    // private static final Logger log = new Log().getLoger(SkladOO.class);
    private static final LogCrutch log = new LogCrutch();
    int[] skidka;
    List data;
    List discr;
    Map result;
    int flag = 0;
    String savePath = MyReportsModule.confPath + "Reports/";
    com.sun.star.beans.PropertyValue[] lParams = new com.sun.star.beans.PropertyValue[0];
    private int roundValue = 50;

    public SkladOO() {
        super();
    }

    public SkladOO(List al) {
        super();
        data = al;
    }

    public SkladOO(List al, List dis) {
        super();
        data = al;
        discr = dis;
    }

    private void fildPrilojenieTTN(XComponent currentDocument) {
        result = new HashMap();
        HashMap<String, Object> hm;
        HashMap dis = (HashMap) discr.get(0);
        String sar = "";
        int lineStart;
        float charSize = (float) 6.5;
        Double sum_stoim = .0;
        Double sum_kol = .0;
        Double sum_nds = .0;
        Double sum_rzn = .0;
        Double sum_kgm = .0;
        Double sum_massa = .0;

        SkladDB sdb = new SkladDB();

        try {
            XSpreadsheetDocument xSpreadsheetDocument = (XSpreadsheetDocument) UnoRuntime
                    .queryInterface(XSpreadsheetDocument.class, currentDocument);
            XSpreadsheets xSpreadsheets = xSpreadsheetDocument.getSheets();
            // xSpreadsheets.insertNewByName("Приложение к ТТН №" +
            // dis.get("ttn"), (short)0);
            Object sheet = xSpreadsheets.getByName("Лист1");
            XSpreadsheet xSpreadsheet = (XSpreadsheet) UnoRuntime
                    .queryInterface(XSpreadsheet.class, sheet);

            XNamed xNamed = (XNamed) UnoRuntime.queryInterface(XNamed.class,
                    xSpreadsheet);
            xNamed.setName("Приложение к ТТН №" + dis.get("ttn"));

            com.sun.star.beans.XPropertySet xPropSet;
            com.sun.star.table.BorderLine aLine = new com.sun.star.table.BorderLine();
            aLine.Color = 0x000000;
            aLine.InnerLineWidth = 0;
            aLine.LineDistance = 0;
            aLine.OuterLineWidth = 25;
            com.sun.star.table.TableBorder aBorder = new com.sun.star.table.TableBorder();
            aBorder.BottomLine = aLine;
            aBorder.IsBottomLineValid = true;

            // ----------------Заполнение шапки документа
            XCell xCell = xSpreadsheet.getCellByPosition(0, 0);

            xCell.setFormula("Грузоотправитель: " + dis.get("adres_otprav"));
            xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime
                    .queryInterface(com.sun.star.beans.XPropertySet.class,
                            xCell);
            xPropSet.setPropertyValue("CharHeight", charSize);

            xCell = xSpreadsheet.getCellByPosition(0, 1);
            xCell.setFormula("Грузополучатель: " + dis.get("adres_poluch"));
            xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime
                    .queryInterface(com.sun.star.beans.XPropertySet.class,
                            xCell);
            xPropSet.setPropertyValue("CharHeight", charSize);

            xCell = xSpreadsheet.getCellByPosition(0, 2);
            xCell.setFormula(dis.get("osnovanie").toString() + " код клиента "
                    + dis.get("kod_klienta").toString());
            xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime
                    .queryInterface(com.sun.star.beans.XPropertySet.class,
                            xCell);
            xPropSet.setPropertyValue("CharHeight", charSize);

            // ----------------Заполнение тела документа
            int row = 8;
            lineStart = row;

            for (int i = 0; i < data.size(); i++) {
                hm = (HashMap) data.get(i);

                if (!sar.equals(hm.get("sar").toString())) {
                    if (row != lineStart) {
                        for (int p = 0; p < 13; p++) {
                            xCell = xSpreadsheet.getCellByPosition(p, row - 1);
                            xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime
                                    .queryInterface(
                                            com.sun.star.beans.XPropertySet.class,
                                            xCell);
                            xPropSet.setPropertyValue("TableBorder", aBorder);
                        }

                        xCell = xSpreadsheet.getCellByPosition(0, row);
                        xCell.setFormula("ИТОГО по артикулу");

                        xCell = xSpreadsheet.getCellByPosition(4, row);
                        xCell.setFormula("=SUM(E" + (lineStart + 1) + ":E"
                                + (row) + ")");
                        // sum_kol.append("E"+(row+1) +";");
                        sum_kol = sum_kol + xCell.getValue();

                        xCell = xSpreadsheet.getCellByPosition(6, row);
                        xCell.setFormula("=SUM(G" + (lineStart + 1) + ":G"
                                + (row) + ")");
                        // sum_stoim.append("G"+(row+1) +";");
                        sum_stoim = sum_stoim + xCell.getValue();

                        xCell = xSpreadsheet.getCellByPosition(8, row);
                        xCell.setFormula("=SUM(I" + (lineStart + 1) + ":I"
                                + (row) + ")");
                        // sum_nds.append("I"+(row+1) +";");
                        sum_nds = sum_nds + xCell.getValue();

                        xCell = xSpreadsheet.getCellByPosition(9, row);
                        xCell.setFormula("=SUM(J" + (lineStart + 1) + ":J"
                                + (row) + ")");
                        // sum_rzn.append("J"+(row+1) +";");
                        sum_rzn = sum_rzn + xCell.getValue();

                        xCell = xSpreadsheet.getCellByPosition(10, row);
                        xCell.setFormula("=SUM(K" + (lineStart + 1) + ":K"
                                + (row) + ")");
                        // sum_kgm.append("K"+(row+1) +";");
                        sum_kgm = sum_kgm + xCell.getValue();

                        xCell = xSpreadsheet.getCellByPosition(11, row);
                        xCell.setFormula("=SUM(L" + (lineStart + 1) + ":L"
                                + row + ")");
                        // sum_massa.append("L"+(row+1) +";");
                        sum_massa = sum_massa + xCell.getValue();

                        for (int p = 0; p < 13; p++) {
                            xCell = xSpreadsheet.getCellByPosition(p, row);
                            xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime
                                    .queryInterface(
                                            com.sun.star.beans.XPropertySet.class,
                                            xCell);
                            xPropSet.setPropertyValue("CharHeight", charSize);
                        }
                        row++;
                        lineStart = row;
                    }

                    sar = hm.get("sar").toString();

                    String nSar = hm.get("sar").toString() + " ТНВЭД - "
                            + hm.get("narp").toString();
                    xCell = xSpreadsheet.getCellByPosition(0, row);
                    xCell.setFormula(nSar);
                    xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime
                            .queryInterface(
                                    com.sun.star.beans.XPropertySet.class,
                                    xCell);
                    xPropSet.setPropertyValue("CharHeight", charSize);

                    if (!sar.startsWith("47")) {
                        nSar = hm.get("ngpr").toString() + " "
                                + hm.get("nar").toString() + " модель "
                                + hm.get("fas").toString();
                    } else {
                        nSar = hm.get("ngpr").toString() + " "
                                + hm.get("nar").toString();
                    }
                    xCell = xSpreadsheet.getCellByPosition(4, row);
                    xCell.setFormula(nSar);
                    xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime
                            .queryInterface(
                                    com.sun.star.beans.XPropertySet.class,
                                    xCell);
                    xPropSet.setPropertyValue("CharHeight", (float) (8));

                    xCell = xSpreadsheet.getCellByPosition(12, row);
                    xCell.setFormula(hm.get("preiscur").toString());
                    xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime
                            .queryInterface(
                                    com.sun.star.beans.XPropertySet.class,
                                    xCell);
                    xPropSet.setPropertyValue("CharHeight", charSize);
                    row++;

                    String ser_ggr;
                    if (!sar.trim().startsWith("№")) {
                        ser_ggr = "Сертификат: "
                                + sdb.getSertifikat(sar, Integer.parseInt(dis
                                .get("vid_sert").toString()));
                    } else {
                        ser_ggr = "Декларация: "
                                + sdb.getSertifikat(sar, Integer.parseInt(dis
                                .get("vid_sert").toString()));
                    }

                    xCell = xSpreadsheet.getCellByPosition(0, row);
                    xCell.setFormula(ser_ggr);
                    xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime
                            .queryInterface(
                                    com.sun.star.beans.XPropertySet.class,
                                    xCell);
                    xPropSet.setPropertyValue("CharHeight", charSize);
                    row++;
                }

                xCell = xSpreadsheet.getCellByPosition(0, row);
                xCell.setFormula(hm.get("ncw").toString());

                String srt = hm.get("srt").toString();
                xCell = xSpreadsheet.getCellByPosition(1, row);
                if (sar.startsWith("43")) {
                    if ("1".equals(srt)) {
                        xCell.setFormula("с");
                    } else {
                        xCell.setFormula("у");
                    }
                } else {
                    xCell.setFormula("=LOWER(" + srt + ")");
                }

                xCell = xSpreadsheet.getCellByPosition(2, row);
                if (hm.get("rzm_print") != null) {
                    xCell.setFormula("'" + hm.get("rzm_print").toString());
                } else {
                    xCell.setFormula("'0");
                }

                xCell = xSpreadsheet.getCellByPosition(3, row);

                if (sar.startsWith("43") || sar.startsWith("48")
                        || sar.startsWith("47")) {
                    xCell.setFormula(hm.get("izm").toString());
                } else {
                    xCell.setFormula("шт.");
                }
                if (sar.startsWith("48") || sar.startsWith("47")) {
                    xCell.setFormula(hm.get("izm").toString());
                    flag = 1;
                }

                xCell = xSpreadsheet.getCellByPosition(4, row);
                xCell.setFormula(hm.get("kol").toString());

                xCell = xSpreadsheet.getCellByPosition(5, row);
                xCell.setFormula(hm.get("cena").toString());

                xCell = xSpreadsheet.getCellByPosition(6, row);
                xCell.setFormula(hm.get("summa").toString());

                xCell = xSpreadsheet.getCellByPosition(7, row);
                xCell.setFormula(dis.get("nds").toString());

                xCell = xSpreadsheet.getCellByPosition(8, row);
                xCell.setFormula(hm.get("summa_nds").toString());

                xCell = xSpreadsheet.getCellByPosition(9, row);
                xCell.setFormula(hm.get("itogo").toString());

                xCell = xSpreadsheet.getCellByPosition(10, row);
                xCell.setFormula(hm.get("kkr").toString());

                xCell = xSpreadsheet.getCellByPosition(11, row);
                xCell.setFormula(hm.get("massa").toString());

                for (int p = 0; p < 13; p++) {
                    xCell = xSpreadsheet.getCellByPosition(p, row);
                    xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime
                            .queryInterface(
                                    com.sun.star.beans.XPropertySet.class,
                                    xCell);
                    xPropSet.setPropertyValue("CharHeight", charSize);
                }
                row++;
            }
            row++;
            for (int p = 0; p < 13; p++) {
                xCell = xSpreadsheet.getCellByPosition(p, row - 1);
                xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime
                        .queryInterface(com.sun.star.beans.XPropertySet.class,
                                xCell);
                xPropSet.setPropertyValue("TableBorder", aBorder);
            }

            xCell = xSpreadsheet.getCellByPosition(0, row);
            xCell.setFormula("ИТОГО по артикулу");

            xCell = xSpreadsheet.getCellByPosition(4, row);
            xCell.setFormula("=SUM(E" + (lineStart + 1) + ":E" + (row) + ")");
            // sum_kol.append("E"+(row+1) +";");
            sum_kol = sum_kol + xCell.getValue();

            xCell = xSpreadsheet.getCellByPosition(6, row);
            xCell.setFormula("=SUM(G" + (lineStart + 1) + ":G" + (row) + ")");
            // sum_stoim.append("G"+(row+1) +";");
            sum_stoim = sum_stoim + xCell.getValue();

            xCell = xSpreadsheet.getCellByPosition(8, row);
            xCell.setFormula("=SUM(I" + (lineStart + 1) + ":I" + (row) + ")");
            // sum_nds.append("I"+(row+1) +";");
            sum_nds = sum_nds + xCell.getValue();

            xCell = xSpreadsheet.getCellByPosition(9, row);
            xCell.setFormula("=SUM(J" + (lineStart + 1) + ":J" + (row) + ")");
            // sum_rzn.append("J"+(row+1) +";");
            // BigDecimal n = new BigDecimal(xCell.getValue());
            // n =n.setScale(0, BigDecimal.ROUND_HALF_UP);
            // xCell.setValue(n.floatValue());
            sum_rzn = sum_rzn + xCell.getValue();

            xCell = xSpreadsheet.getCellByPosition(10, row);
            xCell.setFormula("=SUM(K" + (lineStart + 1) + ":K" + (row) + ")");
            // sum_kgm.append("K"+(row+1) +";");
            sum_kgm = sum_kgm + xCell.getValue();

            xCell = xSpreadsheet.getCellByPosition(11, row);
            xCell.setFormula("=SUM(L" + (lineStart + 1) + ":L" + row + ")");
            // sum_massa.append("L"+(row+1) +";");
            sum_massa = sum_massa + xCell.getValue();
            for (int p = 0; p < 13; p++) {
                xCell = xSpreadsheet.getCellByPosition(p, row);
                xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime
                        .queryInterface(com.sun.star.beans.XPropertySet.class,
                                xCell);
                xPropSet.setPropertyValue("CharHeight", charSize);
            }
            row++;

            xCell = xSpreadsheet.getCellByPosition(0, row);
            xCell.setFormula("ИТОГО по накладной");

            // sum_kol.append("0)");
            xCell = xSpreadsheet.getCellByPosition(4, row);

            xCell.setFormula(sum_kol.toString());
            BigDecimal n = new BigDecimal(xCell.getValue());
            n = n.setScale(2, BigDecimal.ROUND_HALF_UP);
            xCell.setValue(n.floatValue());
            result.put("kol", n);

            // sum_stoim.append("0)");
            xCell = xSpreadsheet.getCellByPosition(6, row);
            xCell.setFormula(sum_stoim.toString());
            DecimalFormat df = new DecimalFormat("###.##");
            int st, stn;
            n = new BigDecimal(xCell.getValue());
            n = n.setScale(0, BigDecimal.ROUND_HALF_UP);
            xCell.setValue(n.intValue());
            st = n.intValue();
            result.put("summa", st);
            // result.put("summa", df.format(xCell.getValue()));

            // sum_nds.append("0)");
            xCell = xSpreadsheet.getCellByPosition(8, row);
            xCell.setFormula(sum_nds.toString());
            n = new BigDecimal(xCell.getValue());
            n = n.setScale(0, BigDecimal.ROUND_HALF_UP);
            xCell.setValue(n.intValue());
            stn = n.intValue();
            result.put("summa_nds", stn);

            // sum_rzn.append("0)");
            xCell = xSpreadsheet.getCellByPosition(9, row);
            xCell.setFormula(sum_rzn.toString());
            xCell.setValue(st + stn);
            result.put("summa_i_nds", stn + st);

            // sum_kgm.append("0)");
            xCell = xSpreadsheet.getCellByPosition(10, row);
            xCell.setFormula(sum_kgm.toString());
            result.put("gruz_mest", xCell.getValue());

            // sum_massa.append("0)");
            xCell = xSpreadsheet.getCellByPosition(11, row);
            xCell.setFormula(sum_massa.toString());
            n = new BigDecimal((xCell.getValue()));
            n = n.setScale(2, BigDecimal.ROUND_HALF_UP);
            result.put("massa", n);

            for (int p = 0; p < 13; p++) {
                xCell = xSpreadsheet.getCellByPosition(p, row);
                xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime
                        .queryInterface(com.sun.star.beans.XPropertySet.class,
                                xCell);
                xPropSet.setPropertyValue("CharHeight", charSize);
            }
            row++;

            xCell = xSpreadsheet.getCellByPosition(4, row - 1);
            String str;
            MoneyToStr mts;
            if (flag == 1) {
                mts = new MoneyToStr(df.format(xCell.getValue()), "kg");
                str = mts.num2str(false).trim();
                str = str.substring(0, str.lastIndexOf(" "));
                xCell = xSpreadsheet.getCellByPosition(0, row);
                xCell.setFormula("Всего отпущено: " + str + " грамм");
            } else {
                mts = new MoneyToStr(df.format(xCell.getValue()), dis.get(
                        "valuta").toString());
                str = mts.num2str(true).trim();
                str = str.substring(0, str.lastIndexOf(" "));
                xCell = xSpreadsheet.getCellByPosition(0, row);
                xCell.setFormula("Всего отпущено: " + str + " ед.");
            }

            xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime
                    .queryInterface(com.sun.star.beans.XPropertySet.class,
                            xCell);
            xPropSet.setPropertyValue("CharHeight", charSize);
            row++;

            xCell = xSpreadsheet.getCellByPosition(8, row - 2);
            mts = new MoneyToStr(df.format(xCell.getValue()), dis.get("valuta")
                    .toString());
            str = mts.num2str(true);

            xCell = xSpreadsheet.getCellByPosition(0, row);
            xCell.setFormula("Всего сумма НДС: " + str);
            xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime
                    .queryInterface(com.sun.star.beans.XPropertySet.class,
                            xCell);
            xPropSet.setPropertyValue("CharHeight", charSize);
            row++;

            xCell = xSpreadsheet.getCellByPosition(9, row - 3);
            mts = new MoneyToStr(df.format(xCell.getValue()), dis.get("valuta")
                    .toString());
            str = mts.num2str(true);

            xCell = xSpreadsheet.getCellByPosition(0, row);
            xCell.setFormula("Всего стоимость с НДС: " + str);
            xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime
                    .queryInterface(com.sun.star.beans.XPropertySet.class,
                            xCell);
            xPropSet.setPropertyValue("CharHeight", charSize);
            row++;

            xCell = xSpreadsheet.getCellByPosition(2, row);
            xCell.setFormula("Сдал_________________________________ ");
            xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime
                    .queryInterface(com.sun.star.beans.XPropertySet.class,
                            xCell);
            xPropSet.setPropertyValue("CharHeight", charSize);

            xCell = xSpreadsheet.getCellByPosition(7, row);
            xCell.setFormula("Принял_________________________________ ");
            xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime
                    .queryInterface(com.sun.star.beans.XPropertySet.class,
                            xCell);
            xPropSet.setPropertyValue("CharHeight", charSize);
            // row++;
            int page_count = (row - 58) / 50 + 1;
            page_count += (row - 58) % 50 > 0 ? 1 : 0;
            result.put("page_count", page_count);

            saveAsDocument(currentDocument, savePath
                    + dis.get("ttn").toString() + ".ods", lParams);
            /*
             * SkladDB db = new SkladDB(); db.saveTTNDB(savePath +
             * dis.get("ttn").toString()+ ".ods",
             * Integer.parseInt(dis.get("ttn_id").toString()),
             * dis.get("ttn").toString()); db.disConn();
             */
        } catch (Exception e) {
            System.err.println(e);
            log.error("Ошибка при заполнении отчёта отгрузка покупателю", e);
            JOptionPane.showMessageDialog(null,
                    "Ошибка при заполнении отчёта отгрузка покупателю",
                    "Ошибка!!!", JOptionPane.ERROR_MESSAGE);
        } finally {
            sdb.disConn();
        }

    }

    private void fildPrilojenieTTNBelSkidkaIzd(XComponent currentDocument) {
        result = new HashMap();
        HashMap<String, Object> hm = new HashMap();
        HashMap dis = (HashMap) discr.get(0);
        String sar = "";
        int lineStart = 0;
        float charSize = (float) 6.5;
        int i;

        Double sum_kol = .0;
        Double sum_stoim_s = .0;
        Double sum_nds_s = .0;
        Double sum_stoim_nds_s = .0;
        Double sum_kgm = .0;
        Double sum_massa = .0;
        Double sum_stoim = .0;
        Double sum_nds = .0;
        Double sum_stoim_nds = .0;

        SkladDB sdb = new SkladDB();

        try {
            XSpreadsheetDocument xSpreadsheetDocument = (XSpreadsheetDocument) UnoRuntime
                    .queryInterface(XSpreadsheetDocument.class, currentDocument);
            XSpreadsheets xSpreadsheets = xSpreadsheetDocument.getSheets();
            // xSpreadsheets.insertNewByName("Приложение к ТТН №" +
            // dis.get("ttn"), (short)0);
            Object sheet = xSpreadsheets.getByName("Лист1");
            XSpreadsheet xSpreadsheet = (XSpreadsheet) UnoRuntime
                    .queryInterface(XSpreadsheet.class, sheet);

            XNamed xNamed = (XNamed) UnoRuntime.queryInterface(XNamed.class,
                    xSpreadsheet);
            xNamed.setName("Приложение к ТТН №" + dis.get("ttn"));

            com.sun.star.beans.XPropertySet xPropSet = null;
            com.sun.star.table.BorderLine aLine = new com.sun.star.table.BorderLine();
            aLine.Color = 0x000000;
            aLine.InnerLineWidth = 0;
            aLine.LineDistance = 0;
            aLine.OuterLineWidth = 25;
            com.sun.star.table.TableBorder aBorder = new com.sun.star.table.TableBorder();
            aBorder.BottomLine = aLine;
            aBorder.IsBottomLineValid = true;

            // ----------------Заполнение шапки документа
            XCell xCell = xSpreadsheet.getCellByPosition(0, 0);
            xCell.setFormula("Грузоотправитель: " + dis.get("adres_otprav"));
            xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime
                    .queryInterface(com.sun.star.beans.XPropertySet.class,
                            xCell);
            xPropSet.setPropertyValue("CharHeight", charSize);

            xCell = xSpreadsheet.getCellByPosition(0, 1);
            xCell.setFormula("Грузополучатель: " + dis.get("adres_poluch"));
            xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime
                    .queryInterface(com.sun.star.beans.XPropertySet.class,
                            xCell);
            xPropSet.setPropertyValue("CharHeight", charSize);

            xCell = xSpreadsheet.getCellByPosition(0, 2);
            xCell.setFormula(dis.get("dogovor").toString() + " код клиента "
                    + dis.get("kod_klienta").toString());
            xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime
                    .queryInterface(com.sun.star.beans.XPropertySet.class,
                            xCell);
            xPropSet.setPropertyValue("CharHeight", charSize);

            // ----------------Заполнение тела документа
            int row = 7;
            lineStart = row;

            for (i = 0; i < data.size(); i++) {
                hm = (HashMap) data.get(i);

                if (!sar.equals(hm.get("sar").toString())) {
                    if (row != lineStart) {
                        for (int p = 0; p < 17; p++) {
                            xCell = xSpreadsheet.getCellByPosition(p, row - 1);
                            xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime
                                    .queryInterface(
                                            com.sun.star.beans.XPropertySet.class,
                                            xCell);
                            xPropSet.setPropertyValue("TableBorder", aBorder);
                        }

                        xCell = xSpreadsheet.getCellByPosition(0, row);
                        xCell.setFormula("ИТОГО по артикулу");

                        xCell = xSpreadsheet.getCellByPosition(4, row);
                        xCell.setFormula("=SUM(E" + (lineStart + 1) + ":E"
                                + (row) + ")");
                        // sum_kol.append("E"+(row+1) +";");
                        sum_kol = sum_kol + xCell.getValue();

                        xCell = xSpreadsheet.getCellByPosition(6, row);
                        xCell.setFormula("=SUM(G" + (lineStart + 1) + ":G"
                                + (row) + ")");
                        // sum_stoim_s.append("G"+(row+1) +";");
                        sum_stoim_s = sum_stoim_s + xCell.getValue();

                        xCell = xSpreadsheet.getCellByPosition(8, row);
                        xCell.setFormula("=SUM(I" + (lineStart + 1) + ":I"
                                + (row) + ")");
                        // sum_nds_s.append("I"+(row+1) +";");
                        sum_nds_s = sum_nds_s + xCell.getValue();

                        xCell = xSpreadsheet.getCellByPosition(9, row);
                        xCell.setFormula("=SUM(J" + (lineStart + 1) + ":J"
                                + (row) + ")");
                        // sum_stoim_nds_s.append("J"+(row+1) +";");
                        sum_stoim_nds_s = sum_stoim_nds_s + xCell.getValue();

                        xCell = xSpreadsheet.getCellByPosition(10, row);
                        xCell.setFormula("=SUM(K" + (lineStart + 1) + ":K"
                                + (row) + ")");
                        // sum_kgm.append("K"+(row+1) +";");
                        sum_kgm = sum_kgm + xCell.getValue();

                        xCell = xSpreadsheet.getCellByPosition(11, row);
                        xCell.setFormula("=SUM(L" + (lineStart + 1) + ":L"
                                + row + ")");
                        // sum_massa.append("L"+(row+1) +";");
                        sum_massa = sum_massa + xCell.getValue();

                        xCell = xSpreadsheet.getCellByPosition(13, row);
                        xCell.setFormula("=SUM(N" + (lineStart + 1) + ":N"
                                + row + ")");
                        // sum_stoim.append("N"+(row+1) +";");
                        sum_stoim = sum_stoim + xCell.getValue();

                        xCell = xSpreadsheet.getCellByPosition(14, row);
                        xCell.setFormula("=SUM(O" + (lineStart + 1) + ":O"
                                + row + ")");
                        // sum_nds.append("O"+(row+1) +";");
                        sum_nds = sum_nds + xCell.getValue();

                        xCell = xSpreadsheet.getCellByPosition(15, row);
                        xCell.setFormula("=SUM(P" + (lineStart + 1) + ":P"
                                + row + ")");
                        // sum_stoim_nds.append("P"+(row+1) +";");
                        sum_stoim_nds = sum_stoim_nds + xCell.getValue();
                        for (int p = 0; p < 17; p++) {
                            xCell = xSpreadsheet.getCellByPosition(p, row);
                            xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime
                                    .queryInterface(
                                            com.sun.star.beans.XPropertySet.class,
                                            xCell);
                            xPropSet.setPropertyValue("CharHeight", charSize);
                        }
                        row++;
                        lineStart = row;
                    }
                    sar = hm.get("sar").toString();

                    String nSar = hm.get("sar").toString() + " ТНВЭД - "
                            + hm.get("narp").toString();
                    xCell = xSpreadsheet.getCellByPosition(0, row);
                    xCell.setFormula(nSar);
                    xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime
                            .queryInterface(
                                    com.sun.star.beans.XPropertySet.class,
                                    xCell);
                    xPropSet.setPropertyValue("CharHeight", charSize);

                    nSar = hm.get("ngpr").toString() + " "
                            + hm.get("nar").toString() + " модель "
                            + hm.get("fas").toString();
                    xCell = xSpreadsheet.getCellByPosition(4, row);
                    xCell.setFormula(nSar);
                    xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime
                            .queryInterface(
                                    com.sun.star.beans.XPropertySet.class,
                                    xCell);
                    xPropSet.setPropertyValue("CharHeight", (float) (8));

                    xCell = xSpreadsheet.getCellByPosition(16, row);
                    xCell.setFormula(hm.get("preiscur").toString());
                    xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime
                            .queryInterface(
                                    com.sun.star.beans.XPropertySet.class,
                                    xCell);
                    xPropSet.setPropertyValue("CharHeight", charSize);
                    row++;

                    String ser_ggr = "";
                    if (!sar.trim().startsWith("№")) {
                        ser_ggr = "Сертификат: "
                                + sdb.getSertifikat(sar, Integer.parseInt(dis
                                .get("vid_sert").toString()));
                    } else {
                        ser_ggr = "Декларация: "
                                + sdb.getSertifikat(sar, Integer.parseInt(dis
                                .get("vid_sert").toString()));
                    }

                    xCell = xSpreadsheet.getCellByPosition(0, row);
                    xCell.setFormula(ser_ggr);
                    xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime
                            .queryInterface(
                                    com.sun.star.beans.XPropertySet.class,
                                    xCell);
                    xPropSet.setPropertyValue("CharHeight", charSize);
                    row++;

                    /*
                     * ser_ggr = "Уд. гос. гигиен. рег.: " + sdb.getGGR(sar,
                     * Integer.parseInt(dis.get("vid_ggr").toString())); xCell =
                     * xSpreadsheet.getCellByPosition(0, row);
                     * xCell.setFormula(ser_ggr); xPropSet =
                     * (com.sun.star.beans.
                     * XPropertySet)UnoRuntime.queryInterface(
                     * com.sun.star.beans.XPropertySet.class, xCell);
                     * xPropSet.setPropertyValue("CharHeight", charSize); row++;
                     */
                }

                xCell = xSpreadsheet.getCellByPosition(0, row);
                xCell.setFormula(hm.get("ncw").toString());

                String srt = hm.get("srt").toString();
                xCell = xSpreadsheet.getCellByPosition(1, row);
                if (sar.startsWith("43")) {
                    if ("1".equals(srt)) {
                        xCell.setFormula("с");
                    } else {
                        xCell.setFormula("у");
                    }
                } else {
                    xCell.setFormula("=LOWER(" + srt + ")");
                }

                xCell = xSpreadsheet.getCellByPosition(2, row);
                if (hm.get("rzm_print") != null) {
                    xCell.setFormula("'" + hm.get("rzm_print").toString());
                } else {
                    xCell.setFormula("'0");
                }
                // if (Integer.parseInt(hm.get("rst").toString()) != 0 )
                // xCell.setFormula(hm.get("rst").toString() + " - " +
                // hm.get("rzm").toString());
                // else xCell.setFormula("=LOWER(" + hm.get("rzm").toString() +
                // ")");

                xCell = xSpreadsheet.getCellByPosition(3, row);
                if (sar.startsWith("43") || sar.startsWith("48")
                        || sar.startsWith("47")) {
                    xCell.setFormula(hm.get("izm").toString());
                } else {
                    xCell.setFormula("шт.");
                }

                xCell = xSpreadsheet.getCellByPosition(4, row);
                xCell.setFormula(hm.get("kol").toString());

                xCell = xSpreadsheet.getCellByPosition(5, row);
                xCell.setFormula(hm.get("cena").toString());

                xCell = xSpreadsheet.getCellByPosition(6, row);
                xCell.setFormula(hm.get("summa").toString());

                xCell = xSpreadsheet.getCellByPosition(7, row);
                xCell.setFormula(hm.get("nds").toString());

                xCell = xSpreadsheet.getCellByPosition(8, row);
                xCell.setFormula(hm.get("summa_nds").toString());

                xCell = xSpreadsheet.getCellByPosition(9, row);
                xCell.setFormula(hm.get("itogo").toString());

                xCell = xSpreadsheet.getCellByPosition(10, row);
                xCell.setFormula(hm.get("kkr").toString());

                xCell = xSpreadsheet.getCellByPosition(11, row);
                xCell.setFormula(hm.get("massa").toString());

                xCell = xSpreadsheet.getCellByPosition(12, row);
                xCell.setFormula(hm.get("cenaBS").toString());

                xCell = xSpreadsheet.getCellByPosition(13, row);
                xCell.setFormula(hm.get("summaBS").toString());

                xCell = xSpreadsheet.getCellByPosition(14, row);
                xCell.setFormula(hm.get("summa_ndsBS").toString());

                xCell = xSpreadsheet.getCellByPosition(15, row);
                xCell.setFormula(hm.get("itogoBS").toString());

                for (int p = 0; p < 17; p++) {
                    xCell = xSpreadsheet.getCellByPosition(p, row);
                    xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime
                            .queryInterface(
                                    com.sun.star.beans.XPropertySet.class,
                                    xCell);
                    xPropSet.setPropertyValue("CharHeight", charSize);
                }
                row++;
            }
            // row++;
            for (int p = 0; p < 17; p++) {
                xCell = xSpreadsheet.getCellByPosition(p, row - 1);
                xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime
                        .queryInterface(com.sun.star.beans.XPropertySet.class,
                                xCell);
                xPropSet.setPropertyValue("TableBorder", aBorder);
            }

            xCell = xSpreadsheet.getCellByPosition(0, row);
            xCell.setFormula("ИТОГО по артикулу");

            xCell = xSpreadsheet.getCellByPosition(4, row);
            xCell.setFormula("=SUM(E" + (lineStart + 1) + ":E" + (row) + ")");
            // sum_kol.append("E"+(row+1) +";");
            sum_kol = sum_kol + xCell.getValue();

            xCell = xSpreadsheet.getCellByPosition(6, row);
            xCell.setFormula("=SUM(G" + (lineStart + 1) + ":G" + (row) + ")");
            // sum_stoim_s.append("G"+(row+1) +";");
            sum_stoim_s = sum_stoim_s + xCell.getValue();

            xCell = xSpreadsheet.getCellByPosition(8, row);
            xCell.setFormula("=SUM(I" + (lineStart + 1) + ":I" + (row) + ")");
            // sum_nds_s.append("I"+(row+1) +";");
            sum_nds_s = sum_nds_s + xCell.getValue();

            xCell = xSpreadsheet.getCellByPosition(9, row);
            xCell.setFormula("=SUM(J" + (lineStart + 1) + ":J" + (row) + ")");
            // sum_stoim_nds_s.append("J"+(row+1) +";");
            sum_stoim_nds_s = sum_stoim_nds_s + xCell.getValue();

            xCell = xSpreadsheet.getCellByPosition(10, row);
            xCell.setFormula("=SUM(K" + (lineStart + 1) + ":K" + (row) + ")");
            // sum_kgm.append("K"+(row+1) +";");
            sum_kgm = sum_kgm + xCell.getValue();

            xCell = xSpreadsheet.getCellByPosition(11, row);
            xCell.setFormula("=SUM(L" + (lineStart + 1) + ":L" + row + ")");
            // sum_massa.append("L"+(row+1) +";");
            sum_massa = sum_massa + xCell.getValue();

            xCell = xSpreadsheet.getCellByPosition(13, row);
            xCell.setFormula("=SUM(N" + (lineStart + 1) + ":N" + row + ")");
            // sum_stoim.append("N"+(row+1) +";");
            sum_stoim = sum_stoim + xCell.getValue();

            xCell = xSpreadsheet.getCellByPosition(14, row);
            xCell.setFormula("=SUM(O" + (lineStart + 1) + ":O" + row + ")");
            // sum_nds.append("O"+(row+1) +";");
            sum_nds = sum_nds + xCell.getValue();

            xCell = xSpreadsheet.getCellByPosition(15, row);
            xCell.setFormula("=SUM(P" + (lineStart + 1) + ":P" + row + ")");
            // sum_stoim_nds.append("P"+(row+1) +";");
            sum_stoim_nds = sum_stoim_nds + xCell.getValue();

            for (int p = 0; p < 16; p++) {
                xCell = xSpreadsheet.getCellByPosition(p, row);
                xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime
                        .queryInterface(com.sun.star.beans.XPropertySet.class,
                                xCell);
                xPropSet.setPropertyValue("CharHeight", charSize);
            }
            row++;

            xCell = xSpreadsheet.getCellByPosition(0, row);
            xCell.setFormula("ВСЕГО по накладной");

            int st, st_n;
            // sum_kol.append("0)");
            xCell = xSpreadsheet.getCellByPosition(4, row);
            xCell.setFormula(sum_kol.toString());
            result.put("kol", xCell.getValue());

            // sum_stoim_s.append("0)");
            xCell = xSpreadsheet.getCellByPosition(6, row);
            xCell.setFormula(sum_stoim_s.toString());
            BigDecimal n = new BigDecimal(xCell.getValue());
            n = n.setScale(0, BigDecimal.ROUND_DOWN);
            result.put("summa", n.toString());
            xCell.setValue(n.intValue());
            st = n.intValue();

            // sum_nds_s.append("0)");
            xCell = xSpreadsheet.getCellByPosition(8, row);
            xCell.setFormula(sum_nds_s.toString());
            n = new BigDecimal(xCell.getValue());
            n = n.setScale(0, BigDecimal.ROUND_DOWN);
            result.put("summa_nds", n.toString());
            xCell.setValue(n.intValue());
            st_n = n.intValue();

            // sum_stoim_nds_s.append("0)");
            xCell = xSpreadsheet.getCellByPosition(9, row);
            xCell.setFormula(sum_stoim_nds_s.toString());
            // n = new BigDecimal(xCell.getValue());
            // n = n.setScale(0, BigDecimal.ROUND_HALF_UP);
            result.put("summa_i_nds", st_n + st);
            xCell.setValue(st_n + st);

            // sum_kgm.append("0)");
            xCell = xSpreadsheet.getCellByPosition(10, row);
            xCell.setFormula(sum_kgm.toString());
            result.put("gruz_mest", xCell.getValue());

            // sum_massa.append("0)");
            xCell = xSpreadsheet.getCellByPosition(11, row);
            xCell.setFormula(sum_massa.toString());
            n = new BigDecimal((xCell.getValue()));
            n = n.setScale(2, BigDecimal.ROUND_DOWN);

            result.put("massa", n.toString());

            // sum_stoim.append("0)");
            xCell = xSpreadsheet.getCellByPosition(13, row);
            xCell.setFormula(sum_stoim.toString());
            n = new BigDecimal(xCell.getValue());
            n = n.setScale(0, BigDecimal.ROUND_DOWN);
            result.put("sum_stoim", n.toString());
            st = n.intValue();

            // sum_nds.append("0)");
            xCell = xSpreadsheet.getCellByPosition(14, row);
            xCell.setFormula(sum_nds.toString());
            n = new BigDecimal(xCell.getValue());
            n = n.setScale(0, BigDecimal.ROUND_DOWN);
            result.put("sum_nds", n.toString());
            st_n = n.intValue();

            // sum_stoim_nds.append("0)");
            xCell = xSpreadsheet.getCellByPosition(15, row);
            xCell.setFormula(sum_stoim_nds.toString());
            // n = new BigDecimal(xCell.getValue());
            // n = n.setScale(0, BigDecimal.ROUND_HALF_UP);
            result.put("sum_stoim_nds", st + st_n);

            for (int p = 0; p < 17; p++) {
                xCell = xSpreadsheet.getCellByPosition(p, row);
                xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime
                        .queryInterface(com.sun.star.beans.XPropertySet.class,
                                xCell);
                xPropSet.setPropertyValue("CharHeight", charSize);
            }
            row++;
            hm = (HashMap) data.get(i - 1);
            // xCell = xSpreadsheet.getCellByPosition(16, row);
            // xCell.setFormula("Опт. Скидка "+
            // hm.get("skidka").toString()+"%");
            // discr.add("Скидка "+hm.get("skidka").toString()+"%");
            // String stroka = "Скидка "+hm.get("skidka").toString()+"%";
            // result.put("skidkaSTR", stroka);

            double skid = Float.parseFloat(hm.get("skidka").toString()) / 100;
            xCell = xSpreadsheet.getCellByPosition(13, row - 1);
            double s = xCell.getValue();
            xCell = xSpreadsheet.getCellByPosition(13, row);
            s = s * skid;
            n = new BigDecimal(s);
            n = n.setScale(1, BigDecimal.ROUND_DOWN);
            n = n.setScale(0, BigDecimal.ROUND_DOWN);
            xCell.setValue(n.intValue());
            st = n.intValue();

            xCell = xSpreadsheet.getCellByPosition(14, row - 1);
            s = xCell.getValue();
            xCell = xSpreadsheet.getCellByPosition(14, row);
            s = s * skid;
            n = new BigDecimal(s);
            n = n.setScale(1, BigDecimal.ROUND_DOWN);
            n = n.setScale(0, BigDecimal.ROUND_DOWN);
            xCell.setValue(n.intValue());
            st_n = n.intValue();

            xCell = xSpreadsheet.getCellByPosition(15, row - 1);
            s = xCell.getValue();
            xCell = xSpreadsheet.getCellByPosition(15, row);
            s = s * skid;
            n = new BigDecimal(s);
            n = n.setScale(1, BigDecimal.ROUND_DOWN);
            n = n.setScale(0, BigDecimal.ROUND_DOWN);
            // xCell.setValue(n.toBigInteger().intValue());
            xCell.setValue(st_n + st);
            // result.put("skidka", skid*100);

            for (int p = 0; p < 17; p++) {
                xCell = xSpreadsheet.getCellByPosition(p, row);
                xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime
                        .queryInterface(com.sun.star.beans.XPropertySet.class,
                                xCell);
                xPropSet.setPropertyValue("CharHeight", charSize);
            }
            row++;

            xCell = xSpreadsheet.getCellByPosition(4, row - 2);
            DecimalFormat df = new DecimalFormat("###.##");
            MoneyToStr mts = new MoneyToStr(df.format(xCell.getValue()), dis
                    .get("valuta").toString());
            String str = mts.num2str(true).trim();
            str = str.substring(0, str.lastIndexOf(" "));

            xCell = xSpreadsheet.getCellByPosition(0, row);
            xCell.setFormula("Всего отпущено: " + str + " ед.");
            xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime
                    .queryInterface(com.sun.star.beans.XPropertySet.class,
                            xCell);
            xPropSet.setPropertyValue("CharHeight", charSize);
            row++;

            xCell = xSpreadsheet.getCellByPosition(8, row - 3);
            mts = new MoneyToStr(df.format(xCell.getValue()), dis.get("valuta")
                    .toString());
            str = mts.num2str(true);

            xCell = xSpreadsheet.getCellByPosition(0, row);
            xCell.setFormula("Всего сумма НДС: " + str);
            xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime
                    .queryInterface(com.sun.star.beans.XPropertySet.class,
                            xCell);
            xPropSet.setPropertyValue("CharHeight", charSize);
            row++;

            xCell = xSpreadsheet.getCellByPosition(9, row - 4);
            mts = new MoneyToStr(df.format(xCell.getValue()), dis.get("valuta")
                    .toString());
            str = mts.num2str(true);

            xCell = xSpreadsheet.getCellByPosition(0, row);
            xCell.setFormula("Всего стоимость с НДС: " + str);
            xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime
                    .queryInterface(com.sun.star.beans.XPropertySet.class,
                            xCell);
            xPropSet.setPropertyValue("CharHeight", charSize);
            row++;

            xCell = xSpreadsheet.getCellByPosition(2, row);
            xCell.setFormula("Сдал_________________________________ ");
            xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime
                    .queryInterface(com.sun.star.beans.XPropertySet.class,
                            xCell);
            xPropSet.setPropertyValue("CharHeight", charSize);

            xCell = xSpreadsheet.getCellByPosition(7, row);
            xCell.setFormula("Принял_________________________________ ");
            xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime
                    .queryInterface(com.sun.star.beans.XPropertySet.class,
                            xCell);
            xPropSet.setPropertyValue("CharHeight", charSize);
            // row++;
            int page_count = (row - 36) / 29 + 1;
            page_count += (row - 36) % 29 > 0 ? 1 : 0;
            result.put("page_count", page_count);

            saveAsDocument(currentDocument, savePath
                    + dis.get("ttn").toString() + ".ods", lParams);
            /*
             * SkladDB db = new SkladDB(); db.saveTTNDB(savePath +
             * dis.get("ttn").toString()+ ".ods",
             * Integer.parseInt(dis.get("ttn_id").toString()),
             * dis.get("ttn").toString()); db.disConn();
             */
        } catch (Exception e) {
            System.err.println(e);
            log.error("Ошибка при заполнении отчёта отгрузка покупателю", e);
            JOptionPane.showMessageDialog(null,
                    "Ошибка при заполнении отчёта отгрузка покупателю",
                    "Ошибка!!!", JOptionPane.ERROR_MESSAGE);
        } finally {
            sdb.disConn();
        }
    }

    private void fildPrilojenieTTNBelSkidkaSumDoc(XComponent currentDocument) {
        result = new HashMap();
        HashMap<String, Object> hm = new HashMap();
        HashMap dis = (HashMap) discr.get(0);
        String sar = "";
        int lineStart = 0;
        float charSize = (float) 6.5;
        int i;

        Double sum_kol = .0;
        Double sum_stoim_s = .0;
        Double sum_nds_s = .0;
        Double sum_stoim_nds_s = .0;
        Double sum_kgm = .0;
        Double sum_massa = .0;
        Double sum_stoim = .0;
        Double sum_nds = .0;
        Double sum_stoim_nds = .0;

        SkladDB sdb = new SkladDB();

        try {
            XSpreadsheetDocument xSpreadsheetDocument = (XSpreadsheetDocument) UnoRuntime
                    .queryInterface(XSpreadsheetDocument.class, currentDocument);
            XSpreadsheets xSpreadsheets = xSpreadsheetDocument.getSheets();
            // xSpreadsheets.insertNewByName("Приложение к ТТН №" +
            // dis.get("ttn"), (short)0);
            Object sheet = xSpreadsheets.getByName("Лист1");
            XSpreadsheet xSpreadsheet = (XSpreadsheet) UnoRuntime
                    .queryInterface(XSpreadsheet.class, sheet);

            XNamed xNamed = (XNamed) UnoRuntime.queryInterface(XNamed.class,
                    xSpreadsheet);
            xNamed.setName("Приложение к ТТН №" + dis.get("ttn"));

            com.sun.star.beans.XPropertySet xPropSet = null;
            com.sun.star.table.BorderLine aLine = new com.sun.star.table.BorderLine();
            aLine.Color = 0x000000;
            aLine.InnerLineWidth = 0;
            aLine.LineDistance = 0;
            aLine.OuterLineWidth = 25;
            com.sun.star.table.TableBorder aBorder = new com.sun.star.table.TableBorder();
            aBorder.BottomLine = aLine;
            aBorder.IsBottomLineValid = true;

            // ----------------Заполнение шапки документа
            XCell xCell = xSpreadsheet.getCellByPosition(0, 0);
            xCell.setFormula("Грузоотправитель: " + dis.get("adres_otprav"));
            xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime
                    .queryInterface(com.sun.star.beans.XPropertySet.class,
                            xCell);
            xPropSet.setPropertyValue("CharHeight", charSize);

            xCell = xSpreadsheet.getCellByPosition(0, 1);
            xCell.setFormula("Грузополучатель: " + dis.get("adres_poluch"));
            xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime
                    .queryInterface(com.sun.star.beans.XPropertySet.class,
                            xCell);
            xPropSet.setPropertyValue("CharHeight", charSize);

            xCell = xSpreadsheet.getCellByPosition(0, 2);
            xCell.setFormula(dis.get("dogovor").toString() + " код клиента "
                    + dis.get("kod_klienta").toString());
            xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime
                    .queryInterface(com.sun.star.beans.XPropertySet.class,
                            xCell);
            xPropSet.setPropertyValue("CharHeight", charSize);

            // ----------------Заполнение тела документа
            int row = 7;
            lineStart = row;

            for (i = 0; i < data.size(); i++) {
                hm = (HashMap) data.get(i);

                if (!sar.equals(hm.get("sar").toString())) {
                    if (row != lineStart) {
                        for (int p = 0; p < 17; p++) {
                            xCell = xSpreadsheet.getCellByPosition(p, row - 1);
                            xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime
                                    .queryInterface(
                                            com.sun.star.beans.XPropertySet.class,
                                            xCell);
                            xPropSet.setPropertyValue("TableBorder", aBorder);
                        }

                        xCell = xSpreadsheet.getCellByPosition(0, row);
                        xCell.setFormula("ИТОГО по артикулу");

                        // sum_kol = sum_kol + xCell.getValue();
                        // sum_stoim_s = sum_stoim_s + xCell.getValue();
                        // sum_nds_s = sum_nds_s + xCell.getValue();
                        // sum_stoim_nds_s = sum_stoim_nds_s + xCell.getValue();
                        // sum_kgm = sum_kgm + xCell.getValue();
                        // sum_massa = sum_massa + xCell.getValue();
                        // sum_stoim = sum_stoim + xCell.getValue();
                        // sum_nds = sum_nds + xCell.getValue();
                        // sum_stoim_nds = sum_stoim_nds + xCell.getValue();

                        xCell = xSpreadsheet.getCellByPosition(4, row);
                        xCell.setFormula("=SUM(E" + (lineStart + 1) + ":E"
                                + (row) + ")");
                        // sum_kol.append("E"+(row+1) +";");
                        sum_kol = sum_kol + xCell.getValue();

                        xCell = xSpreadsheet.getCellByPosition(6, row);
                        xCell.setFormula("=SUM(G" + (lineStart + 1) + ":G"
                                + (row) + ")");
                        // sum_stoim_s.append("G"+(row+1) +";");
                        sum_stoim_s = sum_stoim_s + xCell.getValue();

                        xCell = xSpreadsheet.getCellByPosition(8, row);
                        xCell.setFormula("=SUM(I" + (lineStart + 1) + ":I"
                                + (row) + ")");
                        // sum_nds_s.append("I"+(row+1) +";");
                        sum_nds_s = sum_nds_s + xCell.getValue();

                        xCell = xSpreadsheet.getCellByPosition(9, row);
                        xCell.setFormula("=SUM(J" + (lineStart + 1) + ":J"
                                + (row) + ")");
                        // sum_stoim_nds_s.append("J"+(row+1) +";");
                        sum_stoim_nds_s = sum_stoim_nds_s + xCell.getValue();

                        xCell = xSpreadsheet.getCellByPosition(10, row);
                        xCell.setFormula("=SUM(K" + (lineStart + 1) + ":K"
                                + (row) + ")");
                        // sum_kgm.append("K"+(row+1) +";");
                        sum_kgm = sum_kgm + xCell.getValue();

                        xCell = xSpreadsheet.getCellByPosition(11, row);
                        xCell.setFormula("=SUM(L" + (lineStart + 1) + ":L"
                                + row + ")");
                        // sum_massa.append("L"+(row+1) +";");
                        sum_massa = sum_massa + xCell.getValue();

                        xCell = xSpreadsheet.getCellByPosition(13, row);
                        xCell.setFormula("=SUM(N" + (lineStart + 1) + ":N"
                                + row + ")");
                        // sum_stoim.append("N"+(row+1) +";");
                        sum_stoim = sum_stoim + xCell.getValue();

                        xCell = xSpreadsheet.getCellByPosition(14, row);
                        xCell.setFormula("=SUM(O" + (lineStart + 1) + ":O"
                                + row + ")");
                        // sum_nds.append("O"+(row+1) +";");
                        sum_nds = sum_nds + xCell.getValue();

                        xCell = xSpreadsheet.getCellByPosition(15, row);
                        xCell.setFormula("=SUM(P" + (lineStart + 1) + ":P"
                                + row + ")");
                        // sum_stoim_nds.append("P"+(row+1) +";");
                        sum_stoim_nds = sum_stoim_nds + xCell.getValue();

                        for (int p = 0; p < 17; p++) {
                            xCell = xSpreadsheet.getCellByPosition(p, row);
                            xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime
                                    .queryInterface(
                                            com.sun.star.beans.XPropertySet.class,
                                            xCell);
                            xPropSet.setPropertyValue("CharHeight", charSize);
                        }
                        row++;
                        lineStart = row;

                    }

                    sar = hm.get("sar").toString();

                    String nSar = hm.get("sar").toString() + " ТНВЭД - "
                            + hm.get("narp").toString();
                    xCell = xSpreadsheet.getCellByPosition(0, row);
                    xCell.setFormula(nSar);
                    xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime
                            .queryInterface(
                                    com.sun.star.beans.XPropertySet.class,
                                    xCell);
                    xPropSet.setPropertyValue("CharHeight", charSize);

                    nSar = hm.get("ngpr").toString() + " "
                            + hm.get("nar").toString() + " модель "
                            + hm.get("fas").toString();
                    xCell = xSpreadsheet.getCellByPosition(4, row);
                    xCell.setFormula(nSar);
                    xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime
                            .queryInterface(
                                    com.sun.star.beans.XPropertySet.class,
                                    xCell);
                    xPropSet.setPropertyValue("CharHeight", (float) (8));

                    xCell = xSpreadsheet.getCellByPosition(16, row);
                    xCell.setFormula(hm.get("preiscur").toString());
                    xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime
                            .queryInterface(
                                    com.sun.star.beans.XPropertySet.class,
                                    xCell);
                    xPropSet.setPropertyValue("CharHeight", charSize);
                    row++;

                    String ser_ggr = "";
                    String ser = sdb.getSertifikat(sar,
                            Integer.parseInt(dis.get("vid_sert").toString()));
                    if (!ser.trim().startsWith("№")) {
                        ser_ggr = "Сертификат: " + ser;
                    } else {
                        ser_ggr = "Декларация: " + ser;
                    }

                    xCell = xSpreadsheet.getCellByPosition(0, row);
                    xCell.setFormula(ser_ggr);
                    xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime
                            .queryInterface(
                                    com.sun.star.beans.XPropertySet.class,
                                    xCell);
                    xPropSet.setPropertyValue("CharHeight", charSize);
                    row++;

                    /*
                     * ser_ggr = "Уд. гос. гигиен. рег.: " + sdb.getGGR(sar,
                     * Integer.parseInt(dis.get("vid_ggr").toString())); xCell =
                     * xSpreadsheet.getCellByPosition(0, row);
                     * xCell.setFormula(ser_ggr); xPropSet =
                     * (com.sun.star.beans.
                     * XPropertySet)UnoRuntime.queryInterface(
                     * com.sun.star.beans.XPropertySet.class, xCell);
                     * xPropSet.setPropertyValue("CharHeight", charSize); row++;
                     */
                }

                xCell = xSpreadsheet.getCellByPosition(0, row);
                xCell.setFormula(hm.get("ncw").toString());

                String srt = hm.get("srt").toString();
                xCell = xSpreadsheet.getCellByPosition(1, row);
                if (sar.startsWith("43")) {
                    if ("1".equals(srt)) {
                        xCell.setFormula("с");
                    } else {
                        xCell.setFormula("у");
                    }
                } else {
                    xCell.setFormula("=LOWER(" + srt + ")");
                }

                xCell = xSpreadsheet.getCellByPosition(2, row);
                if (hm.get("rzm_print") != null) {
                    xCell.setFormula("'" + hm.get("rzm_print").toString());
                } else {
                    xCell.setFormula("'0");
                }
                // if (Integer.parseInt(hm.get("rst").toString()) != 0 )
                // xCell.setFormula(hm.get("rst").toString() + " - " +
                // hm.get("rzm").toString());
                // else xCell.setFormula("=LOWER(" + hm.get("rzm").toString() +
                // ")");

                xCell = xSpreadsheet.getCellByPosition(3, row);
                if (sar.startsWith("48") || sar.startsWith("47")) {
                    xCell.setFormula(hm.get("izm").toString());
                } else {
                    xCell.setFormula("шт.");
                }

                xCell = xSpreadsheet.getCellByPosition(4, row);
                xCell.setFormula(hm.get("kol").toString());

                xCell = xSpreadsheet.getCellByPosition(5, row);
                xCell.setFormula(hm.get("cena").toString());

                xCell = xSpreadsheet.getCellByPosition(6, row);
                xCell.setFormula(hm.get("summa").toString());

                xCell = xSpreadsheet.getCellByPosition(7, row);
                xCell.setFormula(hm.get("nds").toString());

                xCell = xSpreadsheet.getCellByPosition(8, row);
                xCell.setFormula(hm.get("summa_nds").toString());

                xCell = xSpreadsheet.getCellByPosition(9, row);
                xCell.setFormula(hm.get("itogo").toString());

                xCell = xSpreadsheet.getCellByPosition(10, row);
                xCell.setFormula(hm.get("kkr").toString());

                xCell = xSpreadsheet.getCellByPosition(11, row);
                xCell.setFormula(hm.get("massa").toString());

                xCell = xSpreadsheet.getCellByPosition(12, row);
                xCell.setFormula(hm.get("cenaBS").toString());

                xCell = xSpreadsheet.getCellByPosition(13, row);
                xCell.setFormula(hm.get("summaBS").toString());

                xCell = xSpreadsheet.getCellByPosition(14, row);
                xCell.setFormula(hm.get("summa_ndsBS").toString());

                xCell = xSpreadsheet.getCellByPosition(15, row);
                xCell.setFormula(hm.get("itogoBS").toString());

                for (int p = 0; p < 17; p++) {
                    xCell = xSpreadsheet.getCellByPosition(p, row);
                    xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime
                            .queryInterface(
                                    com.sun.star.beans.XPropertySet.class,
                                    xCell);
                    xPropSet.setPropertyValue("CharHeight", charSize);
                }
                row++;
            }
            // row++;
            for (int p = 0; p < 17; p++) {
                xCell = xSpreadsheet.getCellByPosition(p, row - 1);
                xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime
                        .queryInterface(com.sun.star.beans.XPropertySet.class,
                                xCell);
                xPropSet.setPropertyValue("TableBorder", aBorder);
            }

            xCell = xSpreadsheet.getCellByPosition(0, row);
            xCell.setFormula("ИТОГО по артикулу");

            xCell = xSpreadsheet.getCellByPosition(4, row);
            xCell.setFormula("=SUM(E" + (lineStart + 1) + ":E" + (row) + ")");
            // sum_kol.append("E"+(row+1) +";");
            sum_kol = sum_kol + xCell.getValue();

            xCell = xSpreadsheet.getCellByPosition(6, row);
            xCell.setFormula("=SUM(G" + (lineStart + 1) + ":G" + (row) + ")");
            // sum_stoim_s.append("G"+(row+1) +";");
            sum_stoim_s = sum_stoim_s + xCell.getValue();

            xCell = xSpreadsheet.getCellByPosition(8, row);
            xCell.setFormula("=SUM(I" + (lineStart + 1) + ":I" + (row) + ")");
            // sum_nds_s.append("I"+(row+1) +";");
            sum_nds_s = sum_nds_s + xCell.getValue();

            xCell = xSpreadsheet.getCellByPosition(9, row);
            xCell.setFormula("=SUM(J" + (lineStart + 1) + ":J" + (row) + ")");
            // sum_stoim_nds_s.append("J"+(row+1) +";");
            sum_stoim_nds_s = sum_stoim_nds_s + xCell.getValue();

            xCell = xSpreadsheet.getCellByPosition(10, row);
            xCell.setFormula("=SUM(K" + (lineStart + 1) + ":K" + (row) + ")");
            // sum_kgm.append("K"+(row+1) +";");
            sum_kgm = sum_kgm + xCell.getValue();

            xCell = xSpreadsheet.getCellByPosition(11, row);
            xCell.setFormula("=SUM(L" + (lineStart + 1) + ":L" + row + ")");
            // sum_massa.append("L"+(row+1) +";");
            sum_massa = sum_massa + xCell.getValue();

            xCell = xSpreadsheet.getCellByPosition(13, row);
            xCell.setFormula("=SUM(N" + (lineStart + 1) + ":N" + row + ")");
            // sum_stoim.append("N"+(row+1) +";");
            sum_stoim = sum_stoim + xCell.getValue();

            xCell = xSpreadsheet.getCellByPosition(14, row);
            xCell.setFormula("=SUM(O" + (lineStart + 1) + ":O" + row + ")");
            // sum_nds.append("O"+(row+1) +";");
            sum_nds = sum_nds + xCell.getValue();

            xCell = xSpreadsheet.getCellByPosition(15, row);
            xCell.setFormula("=SUM(P" + (lineStart + 1) + ":P" + row + ")");
            // sum_stoim_nds.append("P"+(row+1) +";");
            sum_stoim_nds = sum_stoim_nds + xCell.getValue();

            //
            // xCell = xSpreadsheet.getCellByPosition(4, row);
            // xCell.setFormula("=SUM(E"+(lineStart + 1) +":E"+(row)+")");
            // sum_kol.append("E"+(row+1) +";");
            //
            // xCell = xSpreadsheet.getCellByPosition(6, row);
            // xCell.setFormula("=SUM(G"+(lineStart + 1)+":G"+(row)+")");
            // sum_stoim_s.append("G"+(row+1) +";");
            //
            // xCell = xSpreadsheet.getCellByPosition(8, row);
            // xCell.setFormula("=SUM(I"+(lineStart + 1)+":I"+(row)+")");
            // sum_nds_s.append("I"+(row+1) +";");
            //
            // xCell = xSpreadsheet.getCellByPosition(9, row);
            // xCell.setFormula("=SUM(J"+(lineStart + 1)+":J"+(row)+")");
            // sum_stoim_nds_s.append("J"+(row+1) +";");
            //
            // xCell = xSpreadsheet.getCellByPosition(10, row);
            // xCell.setFormula("=SUM(K"+(lineStart + 1)+":K"+(row)+")");
            // sum_kgm.append("K"+(row+1) +";");
            //
            // xCell = xSpreadsheet.getCellByPosition(11, row);
            // xCell.setFormula("=SUM(L"+(lineStart + 1)+":L"+row+")");
            // sum_massa.append("L"+(row+1) +";");
            //
            // xCell = xSpreadsheet.getCellByPosition(13, row);
            // xCell.setFormula("=SUM(N"+(lineStart + 1)+":N"+row+")");
            // sum_stoim.append("N"+(row+1) +";");
            //
            // xCell = xSpreadsheet.getCellByPosition(14, row);
            // xCell.setFormula("=SUM(O"+(lineStart + 1)+":O"+row+")");
            // sum_nds.append("O"+(row+1) +";");
            //
            // xCell = xSpreadsheet.getCellByPosition(15, row);
            // xCell.setFormula("=SUM(P"+(lineStart + 1)+":P"+row+")");
            // sum_stoim_nds.append("P"+(row+1) +";");

            for (int p = 0; p < 16; p++) {
                xCell = xSpreadsheet.getCellByPosition(p, row);
                xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime
                        .queryInterface(com.sun.star.beans.XPropertySet.class,
                                xCell);
                xPropSet.setPropertyValue("CharHeight", charSize);
            }
            row++;

            xCell = xSpreadsheet.getCellByPosition(0, row);
            xCell.setFormula("ВСЕГО по накладной");

            int st, st_n;
            // sum_kol.append("0)");
            xCell = xSpreadsheet.getCellByPosition(4, row);
            xCell.setFormula(sum_kol.toString());
            result.put("kol", xCell.getValue());

            // sum_stoim_s.append("0)");
            xCell = xSpreadsheet.getCellByPosition(6, row);
            xCell.setFormula(sum_stoim_s.toString());
            BigDecimal n = new BigDecimal(xCell.getValue());
            n = n.setScale(0, BigDecimal.ROUND_DOWN);
            result.put("summa", n.toString());
            xCell.setValue(n.intValue());
            st = n.intValue();

            // sum_nds_s.append("0)");
            xCell = xSpreadsheet.getCellByPosition(8, row);
            xCell.setFormula(sum_nds_s.toString());
            n = new BigDecimal(xCell.getValue());
            n = n.setScale(0, BigDecimal.ROUND_DOWN);
            result.put("summa_nds", n.toString());
            xCell.setValue(n.intValue());
            st_n = n.intValue();

            // sum_stoim_nds_s.append("0)");
            xCell = xSpreadsheet.getCellByPosition(9, row);
            xCell.setFormula(sum_stoim_nds_s.toString());
            n = new BigDecimal(xCell.getValue());
            n = n.setScale(0, BigDecimal.ROUND_DOWN);
            result.put("summa_i_nds", st_n + st);
            xCell.setValue(st_n + st);

            // sum_kgm.append("0)");
            xCell = xSpreadsheet.getCellByPosition(10, row);
            xCell.setFormula(sum_kgm.toString());
            result.put("gruz_mest", xCell.getValue());

            // sum_massa.append("0)");
            xCell = xSpreadsheet.getCellByPosition(11, row);
            xCell.setFormula(sum_massa.toString());
            n = new BigDecimal((xCell.getValue()));
            n = n.setScale(2, BigDecimal.ROUND_DOWN);

            result.put("massa", n.toString());

            // sum_stoim.append("0)");
            xCell = xSpreadsheet.getCellByPosition(13, row);
            xCell.setFormula(sum_stoim.toString());
            n = new BigDecimal(xCell.getValue());
            n = n.setScale(0, BigDecimal.ROUND_DOWN);
            result.put("sum_stoim", n.toString());
            st = n.intValue();

            // sum_nds.append("0)");
            xCell = xSpreadsheet.getCellByPosition(14, row);
            xCell.setFormula(sum_nds.toString());
            n = new BigDecimal(xCell.getValue());
            n = n.setScale(0, BigDecimal.ROUND_DOWN);
            result.put("sum_nds", n.toString());
            st_n = n.intValue();

            // sum_stoim_nds.append("0)");
            xCell = xSpreadsheet.getCellByPosition(15, row);
            xCell.setFormula(sum_stoim_nds.toString());
            n = new BigDecimal(xCell.getValue());
            n = n.setScale(0, BigDecimal.ROUND_DOWN);
            result.put("sum_stoim_nds", st + st_n);

            for (int p = 0; p < 17; p++) {
                xCell = xSpreadsheet.getCellByPosition(p, row);
                xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime
                        .queryInterface(com.sun.star.beans.XPropertySet.class,
                                xCell);
                xPropSet.setPropertyValue("CharHeight", charSize);
            }
            row++;
            hm = (HashMap) data.get(i - 1);
            xCell = xSpreadsheet.getCellByPosition(16, row);
            xCell.setFormula("Опт. Скидка " + hm.get("skidka").toString() + "%");
            // discr.add("Скидка "+hm.get("skidka").toString()+"%");
            // String stroka = "Скидка "+hm.get("skidka").toString()+"%";
            // result.put("skidkaSTR", stroka);

            double skid = Float.parseFloat(hm.get("skidka").toString()) / 100;
            xCell = xSpreadsheet.getCellByPosition(13, row - 1);
            double s = xCell.getValue();
            xCell = xSpreadsheet.getCellByPosition(13, row);
            s = s * skid;
            n = new BigDecimal(s);
            n = n.setScale(1, BigDecimal.ROUND_DOWN);
            n = n.setScale(0, BigDecimal.ROUND_DOWN);
            xCell.setValue(n.intValue());
            st = n.intValue();

            xCell = xSpreadsheet.getCellByPosition(14, row - 1);
            s = xCell.getValue();
            xCell = xSpreadsheet.getCellByPosition(14, row);
            s = s * skid;
            n = new BigDecimal(s);
            n = n.setScale(1, BigDecimal.ROUND_DOWN);
            n = n.setScale(0, BigDecimal.ROUND_DOWN);
            xCell.setValue(n.intValue());
            st_n = n.intValue();

            xCell = xSpreadsheet.getCellByPosition(15, row - 1);
            s = xCell.getValue();
            xCell = xSpreadsheet.getCellByPosition(15, row);
            s = s * skid;
            n = new BigDecimal(s);
            n = n.setScale(1, BigDecimal.ROUND_DOWN);
            n = n.setScale(0, BigDecimal.ROUND_DOWN);
            // xCell.setValue(n.toBigInteger().intValue());
            xCell.setValue(st_n + st);
            // result.put("skidka", skid*100);

            for (int p = 0; p < 17; p++) {
                xCell = xSpreadsheet.getCellByPosition(p, row);
                xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime
                        .queryInterface(com.sun.star.beans.XPropertySet.class,
                                xCell);
                xPropSet.setPropertyValue("CharHeight", charSize);
            }
            row++;

            xCell = xSpreadsheet.getCellByPosition(4, row - 2);
            DecimalFormat df = new DecimalFormat("###.##");
            MoneyToStr mts = new MoneyToStr(df.format(xCell.getValue()), dis
                    .get("valuta").toString());
            String str = mts.num2str(true).trim();
            str = str.substring(0, str.lastIndexOf(" "));

            xCell = xSpreadsheet.getCellByPosition(0, row);
            xCell.setFormula("Всего отпущено: " + str + " ед.");
            xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime
                    .queryInterface(com.sun.star.beans.XPropertySet.class,
                            xCell);
            xPropSet.setPropertyValue("CharHeight", charSize);
            row++;

            xCell = xSpreadsheet.getCellByPosition(8, row - 3);
            mts = new MoneyToStr(df.format(xCell.getValue()), dis.get("valuta")
                    .toString());
            str = mts.num2str(true);

            xCell = xSpreadsheet.getCellByPosition(0, row);
            xCell.setFormula("Всего сумма НДС: " + str);
            xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime
                    .queryInterface(com.sun.star.beans.XPropertySet.class,
                            xCell);
            xPropSet.setPropertyValue("CharHeight", charSize);
            row++;

            xCell = xSpreadsheet.getCellByPosition(9, row - 4);
            mts = new MoneyToStr(df.format(xCell.getValue()), dis.get("valuta")
                    .toString());
            str = mts.num2str(true);

            xCell = xSpreadsheet.getCellByPosition(0, row);
            xCell.setFormula("Всего стоимость с НДС: " + str);
            xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime
                    .queryInterface(com.sun.star.beans.XPropertySet.class,
                            xCell);
            xPropSet.setPropertyValue("CharHeight", charSize);
            row++;

            xCell = xSpreadsheet.getCellByPosition(2, row);
            xCell.setFormula("Сдал_________________________________ ");
            xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime
                    .queryInterface(com.sun.star.beans.XPropertySet.class,
                            xCell);
            xPropSet.setPropertyValue("CharHeight", charSize);

            xCell = xSpreadsheet.getCellByPosition(7, row);
            xCell.setFormula("Принял_________________________________ ");
            xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime
                    .queryInterface(com.sun.star.beans.XPropertySet.class,
                            xCell);
            xPropSet.setPropertyValue("CharHeight", charSize);
            // row++;
            int page_count = (row - 36) / 29 + 1;
            page_count += (row - 36) % 29 > 0 ? 1 : 0;
            result.put("page_count", page_count);

            saveAsDocument(currentDocument, savePath
                    + dis.get("ttn").toString() + ".ods", lParams);
            /*
             * SkladDB db = new SkladDB(); db.saveTTNDB(savePath +
             * dis.get("ttn").toString()+ ".ods",
             * Integer.parseInt(dis.get("ttn_id").toString()),
             * dis.get("ttn").toString()); db.disConn();
             */
        } catch (Exception e) {
            System.err.println(e);
            log.error("Ошибка при заполнении отчёта отгрузка покупателю", e);
            JOptionPane.showMessageDialog(null,
                    "Ошибка при заполнении отчёта отгрузка покупателю",
                    "Ошибка!!!", JOptionPane.ERROR_MESSAGE);
        } finally {
            sdb.disConn();
        }
    }

    private void fildPrilojenieTTN2(XComponent currentDocument) {
        result = new HashMap();
        HashMap<String, Object> hm = new HashMap();
        HashMap dis = (HashMap) discr.get(0);
        String sar = "";
        double razn = 0, raznBuf = 0, raznBuf1 = 0, roznCena = 0, cena = 0, tn = 0, ndsTN = 0, kol = 0;
        int lineStart = 0;
        float charSize = (float) 6.5;
        BigDecimal n;
        int st, stn;
        DecimalFormat df;
        Double sumtn = .0;
        Double sumtn_nds = .0;
        Double sumtn_rzn = .0;

        Double sum_stoim = .0;
        Double sum_cena = .0;
        Double sum_kol = .0;
        Double sum_nds = .0;
        Double sum_kgm = .0;
        Double sum_massa = .0;

        SkladDB sdb = new SkladDB();

        try {
            XSpreadsheetDocument xSpreadsheetDocument = (XSpreadsheetDocument) UnoRuntime
                    .queryInterface(XSpreadsheetDocument.class, currentDocument);
            XSpreadsheets xSpreadsheets = xSpreadsheetDocument.getSheets();
            // xSpreadsheets.insertNewByName("Приложение к ТТН №" +
            // dis.get("ttn"), (short)0);
            Object sheet = xSpreadsheets.getByName("Лист1");
            XSpreadsheet xSpreadsheet = (XSpreadsheet) UnoRuntime
                    .queryInterface(XSpreadsheet.class, sheet);

            XNamed xNamed = (XNamed) UnoRuntime.queryInterface(XNamed.class,
                    xSpreadsheet);
            xNamed.setName("Приложение к ТТН №" + dis.get("ttn"));
            result.put("ttn", dis.get("ttn"));

            com.sun.star.beans.XPropertySet xPropSet = null;
            com.sun.star.table.BorderLine aLine = new com.sun.star.table.BorderLine();
            aLine.Color = 0x000000;
            aLine.InnerLineWidth = 0;
            aLine.LineDistance = 0;
            aLine.OuterLineWidth = 25;
            com.sun.star.table.TableBorder aBorder = new com.sun.star.table.TableBorder();
            aBorder.BottomLine = aLine;
            aBorder.IsBottomLineValid = true;

            // ----------------Заполнение шапки документа
            XCell xCell = xSpreadsheet.getCellByPosition(0, 0);
            xCell.setFormula("Грузоотправитель: " + dis.get("adres_otprav"));
            xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime
                    .queryInterface(com.sun.star.beans.XPropertySet.class,
                            xCell);
            xPropSet.setPropertyValue("CharHeight", charSize);

            xCell = xSpreadsheet.getCellByPosition(0, 1);
            xCell.setFormula("Грузополучатель: " + dis.get("adres_poluch"));
            xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime
                    .queryInterface(com.sun.star.beans.XPropertySet.class,
                            xCell);
            xPropSet.setPropertyValue("CharHeight", charSize);

            result.put("adres", dis.get("adres_poluch"));

            xCell = xSpreadsheet.getCellByPosition(0, 2);
            xCell.setFormula(dis.get("dogovor").toString() + " код клиента "
                    + dis.get("kod_klienta").toString());
            xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime
                    .queryInterface(com.sun.star.beans.XPropertySet.class,
                            xCell);
            xPropSet.setPropertyValue("CharHeight", charSize);

            // ----------------Заполнение тела документа
            int row = 7;
            lineStart = row;

            for (int i = 0; i < data.size(); i++) {
                hm = (HashMap) data.get(i);

                if (!sar.equals(hm.get("sar").toString())) {
                    if (row != lineStart) {
                        for (int p = 0; p < 19; p++) {
                            xCell = xSpreadsheet.getCellByPosition(p, row - 1);
                            xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime
                                    .queryInterface(
                                            com.sun.star.beans.XPropertySet.class,
                                            xCell);
                            xPropSet.setPropertyValue("TableBorder", aBorder);
                        }

                        xCell = xSpreadsheet.getCellByPosition(0, row);
                        xCell.setFormula("ИТОГО по артикулу");

                        xCell = xSpreadsheet.getCellByPosition(4, row);
                        xCell.setFormula("=SUM(E" + (lineStart + 3) + ":E"
                                + (row) + ")");
                        // sum_kol.append("E"+(row+1) +";");
                        sum_kol = sum_kol + xCell.getValue();

                        xCell = xSpreadsheet.getCellByPosition(6, row);
                        xCell.setFormula("=SUM(G" + (lineStart + 3) + ":G"
                                + (row) + ")");
                        // sum_stoim.append("G"+(row+1) +";");
                        sum_stoim = sum_stoim + xCell.getValue();

                        xCell = xSpreadsheet.getCellByPosition(8, row);
                        xCell.setFormula("=SUM(I" + (lineStart + 3) + ":I"
                                + (row) + ")");
                        // sum_nds.append("I"+(row+1) +";");
                        sum_nds = sum_nds + xCell.getValue();

                        xCell = xSpreadsheet.getCellByPosition(9, row);
                        xCell.setFormula("=SUM(J" + (lineStart + 3) + ":J"
                                + (row) + ")");
                        // sum_cena.append("J"+(row+1) +";");
                        sum_cena = sum_cena + xCell.getValue();

                        xCell = xSpreadsheet.getCellByPosition(10, row);
                        xCell.setFormula("=SUM(K" + (lineStart + 3) + ":K"
                                + (row) + ")");
                        // sum_kgm.append("K"+(row+1) +";");
                        sum_kgm = sum_kgm + xCell.getValue();

                        xCell = xSpreadsheet.getCellByPosition(11, row);
                        xCell.setFormula("=SUM(L" + (lineStart + 3) + ":L"
                                + row + ")");
                        // sum_massa.append("L"+(row+1) +";");
                        sum_massa = sum_massa + xCell.getValue();

                        xCell = xSpreadsheet.getCellByPosition(14, row);
                        xCell.setFormula("=SUM(O" + (lineStart + 3) + ":O"
                                + row + ")");
                        sumtn = sumtn + xCell.getValue();

                        xCell = xSpreadsheet.getCellByPosition(17, row);
                        xCell.setFormula("=SUM(R" + (lineStart + 3) + ":R"
                                + row + ")");
                        sumtn_nds = sumtn_nds + xCell.getValue(); // .append("R"+(row+1)
                        // +" + ");

                        xCell = xSpreadsheet.getCellByPosition(18, row);
                        xCell.setFormula("=SUM(S" + (lineStart + 3) + ":S"
                                + row + ")");
                        sumtn_rzn = sumtn_rzn + xCell.getValue(); // .append("S"+(row+1)
                        // +" + ");

                        for (int p = 0; p < 19; p++) {
                            xCell = xSpreadsheet.getCellByPosition(p, row);
                            xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime
                                    .queryInterface(
                                            com.sun.star.beans.XPropertySet.class,
                                            xCell);
                            xPropSet.setPropertyValue("CharHeight", charSize);
                        }
                        row++;
                        lineStart = row;
                        razn += raznBuf1;
                        raznBuf = 0;
                        raznBuf1 = 0;

                    }

                    sar = hm.get("sar").toString();

                    String nSar = hm.get("sar").toString() + " ТНВЭД - "
                            + hm.get("narp").toString();
                    xCell = xSpreadsheet.getCellByPosition(0, row);
                    xCell.setFormula(nSar);
                    xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime
                            .queryInterface(
                                    com.sun.star.beans.XPropertySet.class,
                                    xCell);
                    xPropSet.setPropertyValue("CharHeight", charSize);

                    nSar = hm.get("ngpr").toString() + " "
                            + hm.get("nar").toString() + " модель "
                            + hm.get("fas").toString();
                    xCell = xSpreadsheet.getCellByPosition(4, row);
                    xCell.setFormula(nSar);
                    xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime
                            .queryInterface(
                                    com.sun.star.beans.XPropertySet.class,
                                    xCell);
                    xPropSet.setPropertyValue("CharHeight", (float) (8));

                    xCell = xSpreadsheet.getCellByPosition(12, row);
                    xCell.setFormula(hm.get("preiscur").toString());
                    xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime
                            .queryInterface(
                                    com.sun.star.beans.XPropertySet.class,
                                    xCell);
                    xPropSet.setPropertyValue("CharHeight", charSize);
                    row++;
                    String ser_ggr = "";
                    if (!sar.trim().startsWith("№")) {
                        ser_ggr = "Сертификат: "
                                + sdb.getSertifikat(sar, Integer.parseInt(dis
                                .get("vid_sert").toString()));
                    } else {
                        ser_ggr = "Декларация: "
                                + sdb.getSertifikat(sar, Integer.parseInt(dis
                                .get("vid_sert").toString()));
                    }

                    // + "    Уд. гос. гигиен. рег.: " + sdb.getGGR(sar,
                    // Integer.parseInt(dis.get("vid_ggr").toString()));

                    xCell = xSpreadsheet.getCellByPosition(0, row);
                    xCell.setFormula(ser_ggr);
                    xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime
                            .queryInterface(
                                    com.sun.star.beans.XPropertySet.class,
                                    xCell);
                    xPropSet.setPropertyValue("CharHeight", charSize);
                    row++;

                    /*
                     * ser_ggr = "Уд. гос. гигиен. рег.: " + sdb.getGGR(sar,
                     * Integer.parseInt(dis.get("vid_ggr").toString())); xCell =
                     * xSpreadsheet.getCellByPosition(0, row);
                     * xCell.setFormula(ser_ggr); xPropSet =
                     * (com.sun.star.beans.
                     * XPropertySet)UnoRuntime.queryInterface(
                     * com.sun.star.beans.XPropertySet.class, xCell);
                     * xPropSet.setPropertyValue("CharHeight", charSize);
                     *
                     * row++;
                     */
                }

                xCell = xSpreadsheet.getCellByPosition(0, row);
                xCell.setFormula(hm.get("ncw").toString());

                String srt = hm.get("srt").toString();
                xCell = xSpreadsheet.getCellByPosition(1, row);
                if (sar.startsWith("43")) {
                    if ("1".equals(srt)) {
                        xCell.setFormula("с");
                    } else {
                        xCell.setFormula("у");
                    }
                } else {
                    xCell.setFormula("=LOWER(" + srt + ")");
                }

                /*
                 * xCell = xSpreadsheet.getCellByPosition(1, row);
                 * xCell.setFormula("=LOWER(" + hm.get("srt").toString() + ")");
                 */

                xCell = xSpreadsheet.getCellByPosition(2, row);
                if (hm.get("rzm_print") != null) {
                    xCell.setFormula("'" + hm.get("rzm_print").toString());
                } else {
                    xCell.setFormula("'0");
                }
                // if (Integer.parseInt(hm.get("rst").toString()) != 0 )
                // xCell.setFormula(hm.get("rst").toString() + " - " +
                // hm.get("rzm").toString());
                // else xCell.setFormula("=LOWER(" + hm.get("rzm").toString() +
                // ")");

                xCell = xSpreadsheet.getCellByPosition(3, row);
                if (sar.startsWith("43") || sar.startsWith("48")
                        || sar.startsWith("47")) {
                    xCell.setFormula(hm.get("izm").toString());
                } else {
                    xCell.setFormula("шт.");
                }

                xCell = xSpreadsheet.getCellByPosition(4, row);
                xCell.setFormula(hm.get("kol").toString());

                xCell = xSpreadsheet.getCellByPosition(5, row);
                xCell.setFormula(hm.get("cena").toString());

                xCell = xSpreadsheet.getCellByPosition(6, row);
                xCell.setFormula(hm.get("summa").toString());

                xCell = xSpreadsheet.getCellByPosition(7, row);
                xCell.setFormula(hm.get("nds").toString());

                xCell = xSpreadsheet.getCellByPosition(8, row);
                xCell.setFormula(hm.get("summa_nds").toString());

                xCell = xSpreadsheet.getCellByPosition(9, row);
                xCell.setFormula(hm.get("itogo").toString());

                xCell = xSpreadsheet.getCellByPosition(10, row);
                xCell.setFormula(hm.get("kkr").toString());

                xCell = xSpreadsheet.getCellByPosition(11, row);
                xCell.setFormula(hm.get("massa").toString());

                xCell = xSpreadsheet.getCellByPosition(12, row);
                xCell.setFormula(hm.get("torg_nadbavka").toString());

                xCell = xSpreadsheet.getCellByPosition(13, row);
                xCell.setFormula("= F" + (row + 1) + "* M" + (row + 1)
                        + " / 100");
                n = new BigDecimal(xCell.getValue());
                n = n.setScale(0, BigDecimal.ROUND_HALF_UP);
                xCell.setValue(n.intValue());

                xCell = xSpreadsheet.getCellByPosition(14, row);
                xCell.setFormula("= E" + (row + 1) + "* N" + (row + 1));

                xCell = xSpreadsheet.getCellByPosition(15, row);
                xCell.setFormula("=ROUND( (F" + (row + 1) + "+ N" + (row + 1)
                        + ") * H" + (row + 1) + " / 100;2)");
                n = new BigDecimal(xCell.getValue());
                n = n.setScale(2, BigDecimal.ROUND_HALF_UP);
                xCell.setValue(n.doubleValue());

                xCell = xSpreadsheet.getCellByPosition(16, row);
                xCell.setFormula("= F" + (row + 1) + "+ N" + (row + 1) + " +P"
                        + (row + 1));
                long d = Math.round(xCell.getValue());

                int ost = (int) (d % 100);

                d /= 100;
                d *= 100;

/*				if (ost >= 25 && ost < 75) {
					d += 50;
				} else if (ost >= 75) {
					d += 100;
					break  ;
				}
*/
                switch (roundValue) {

                    case 50:
                        if (ost >= 25 && ost < 75) {
                            d += 50;
                        } else if (ost >= 75) {
                            d += 100;
                        }
                        break;
                    case 100:
                        if (ost >= 25 && ost < 50) {
                            d += 0;
                        } else if (ost >= 50) {
                            d += 100;
                        }
                        break;
                }

                // System.out.println("Результат "+d);
                xCell.setValue(d);

                xCell = xSpreadsheet.getCellByPosition(4, row);
                kol = xCell.getValue();
                xCell = xSpreadsheet.getCellByPosition(5, row);
                cena = xCell.getValue();
                xCell = xSpreadsheet.getCellByPosition(13, row);
                tn = xCell.getValue();
                xCell = xSpreadsheet.getCellByPosition(15, row);
                ndsTN = xCell.getValue();
                xCell = xSpreadsheet.getCellByPosition(16, row);
                roznCena = xCell.getValue();

                raznBuf = (roznCena - (cena + ndsTN + tn)) * kol;
                raznBuf1 += raznBuf;

                xCell = xSpreadsheet.getCellByPosition(17, row);
                xCell.setFormula("= P" + (row + 1) + "* E" + (row + 1));

                xCell = xSpreadsheet.getCellByPosition(18, row);
                xCell.setFormula("= Q" + (row + 1) + "* E" + (row + 1));

                for (int p = 0; p < 19; p++) {
                    xCell = xSpreadsheet.getCellByPosition(p, row);
                    xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime
                            .queryInterface(
                                    com.sun.star.beans.XPropertySet.class,
                                    xCell);
                    xPropSet.setPropertyValue("CharHeight", charSize);
                }
                row++;
            }
            row++;
            for (int p = 0; p < 19; p++) {
                xCell = xSpreadsheet.getCellByPosition(p, row - 1);
                xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime
                        .queryInterface(com.sun.star.beans.XPropertySet.class,
                                xCell);
                xPropSet.setPropertyValue("TableBorder", aBorder);
            }

            xCell = xSpreadsheet.getCellByPosition(0, row);
            xCell.setFormula("ИТОГО по артикулу");

            xCell = xSpreadsheet.getCellByPosition(4, row);
            xCell.setFormula("=SUM(E" + (lineStart + 3) + ":E" + (row) + ")");
            // sum_kol.append("E"+(row+1) +";");
            sum_kol = sum_kol + xCell.getValue();

            xCell = xSpreadsheet.getCellByPosition(6, row);
            xCell.setFormula("=SUM(G" + (lineStart + 3) + ":G" + (row) + ")");
            sum_stoim = sum_stoim + xCell.getValue();
            // sum_stoim.append("G"+(row+1) +";");
            n = new BigDecimal(xCell.getValue());
            n = n.setScale(0, BigDecimal.ROUND_DOWN);
            xCell.setValue(n.intValue());

            xCell = xSpreadsheet.getCellByPosition(8, row);
            xCell.setFormula("=SUM(I" + (lineStart + 3) + ":I" + (row) + ")");
            sum_nds = sum_nds + xCell.getValue();
            // sum_nds.append("I"+(row+1) +";");
            n = new BigDecimal(xCell.getValue());
            n = n.setScale(0, BigDecimal.ROUND_DOWN);
            xCell.setValue(n.intValue());

            xCell = xSpreadsheet.getCellByPosition(9, row);
            xCell.setFormula("=SUM(J" + (lineStart + 3) + ":J" + (row) + ")");
            // sum_cena.append("J"+(row+1) +";");
            sum_cena = sum_cena + xCell.getValue();
            n = new BigDecimal(xCell.getValue());
            n = n.setScale(0, BigDecimal.ROUND_DOWN);
            xCell.setValue(n.intValue());

            xCell = xSpreadsheet.getCellByPosition(10, row);
            xCell.setFormula("=SUM(K" + (lineStart + 3) + ":K" + (row) + ")");
            // sum_kgm.append("K"+(row+1) +";");
            sum_kgm = sum_kgm + xCell.getValue();

            xCell = xSpreadsheet.getCellByPosition(11, row);
            xCell.setFormula("=SUM(L" + (lineStart + 3) + ":L" + row + ")");
            // sum_massa.append("L"+(row+1) +";");
            sum_massa = sum_massa + xCell.getValue();

            xCell = xSpreadsheet.getCellByPosition(14, row);
            xCell.setFormula("=SUM(O" + (lineStart + 3) + ":O" + row + ")");
            sumtn = sumtn + xCell.getValue(); // .append("O").append(row+1).append(" + ");

            xCell = xSpreadsheet.getCellByPosition(17, row);
            xCell.setFormula("=SUM(R" + (lineStart + 3) + ":R" + row + ")");
            sumtn_nds = sumtn_nds + xCell.getValue();// .append("R").append(row+1).append(" + ");

            xCell = xSpreadsheet.getCellByPosition(18, row);
            xCell.setFormula("=SUM(S" + (lineStart + 3) + ":S" + row + ")");
            sumtn_rzn = sumtn_rzn + xCell.getValue();// .append("S").append(row+1).append(";");

            for (int p = 0; p < 19; p++) {
                xCell = xSpreadsheet.getCellByPosition(p, row);
                xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime
                        .queryInterface(com.sun.star.beans.XPropertySet.class,
                                xCell);
                xPropSet.setPropertyValue("CharHeight", charSize);
            }
            razn += raznBuf1;
            raznBuf = 0;
            raznBuf1 = 0;
            row++;

            xCell = xSpreadsheet.getCellByPosition(0, row);
            xCell.setFormula("ИТОГО по накладной");

            // sum_kol.append("0)");
            xCell = xSpreadsheet.getCellByPosition(4, row);
            xCell.setFormula(sum_kol.toString());// .toString());

            result.put("kol", xCell.getValue());

            // sum_stoim.append("0)");
            xCell = xSpreadsheet.getCellByPosition(6, row);
            xCell.setFormula(sum_stoim.toString());
            // DecimalFormat df = new DecimalFormat("#");
            n = new BigDecimal(xCell.getValue());
            n = n.setScale(0, BigDecimal.ROUND_UP);
            xCell.setValue(n.intValue());
            result.put("summa", n.toString());
            st = n.intValue();
            // df = new DecimalFormat("#.#");
            // sum_nds.append("0)");
            xCell = xSpreadsheet.getCellByPosition(8, row);
            xCell.setFormula(sum_nds.toString());
            n = new BigDecimal(xCell.getValue());
            n = n.setScale(0, BigDecimal.ROUND_DOWN);
            xCell.setValue(n.intValue());
            result.put("summa_nds", n.toString());
            stn = n.intValue();
            // df = new DecimalFormat("#");
            // sum_cena.append("0)");
            xCell = xSpreadsheet.getCellByPosition(9, row);
            xCell.setFormula(sum_cena.toString());
            n = new BigDecimal(xCell.getValue());
            n = n.setScale(0, BigDecimal.ROUND_DOWN);
            xCell.setValue(n.intValue());
            result.put("summa_i_nds", n.intValue());

            // sum_kgm.append("0)");
            xCell = xSpreadsheet.getCellByPosition(10, row);
            xCell.setFormula(sum_kgm.toString());
            result.put("gruz_mest", xCell.getValue());

            // sum_massa.append("0)");
            xCell = xSpreadsheet.getCellByPosition(11, row);
            xCell.setFormula(sum_massa.toString());
            n = new BigDecimal((xCell.getValue()));
            n = n.setScale(2, BigDecimal.ROUND_DOWN);
            result.put("massa", n);

            // sumtn.append("0");
            xCell = xSpreadsheet.getCellByPosition(14, row);
            xCell.setValue(sumtn);// .toString());
            n = new BigDecimal(xCell.getValue());
            n = n.setScale(0, BigDecimal.ROUND_DOWN);
            xCell.setValue(n.intValue());
            st = n.intValue();
            result.put("summa_ne_okr", st);

            // sumtn_nds.append("0");
            xCell = xSpreadsheet.getCellByPosition(17, row);
            xCell.setValue(sumtn_nds);// .toString());
            n = new BigDecimal(xCell.getValue());
            n = n.setScale(0, BigDecimal.ROUND_DOWN);
            xCell.setValue(n.intValue());
            stn = n.intValue();

            // sumtn_rzn.delete(sumtn_rzn.length()-1, sumtn_rzn.length());
            // sumtn_rzn.append(")");
            xCell = xSpreadsheet.getCellByPosition(18, row);
            xCell.setValue(sumtn_rzn);// .toString());
            // xCell.setValue(st+Integer.parseInt(result.get("summa").toString()));

            for (int p = 0; p < 19; p++) {
                xCell = xSpreadsheet.getCellByPosition(p, row);
                xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime
                        .queryInterface(com.sun.star.beans.XPropertySet.class,
                                xCell);
                xPropSet.setPropertyValue("CharHeight", charSize);
            }
            row++;

            xCell = xSpreadsheet.getCellByPosition(0, row);
            xCell.setFormula("С учетом округления");

            xCell = xSpreadsheet.getCellByPosition(6, row - 1);
            cena = xCell.getValue();
            xCell = xSpreadsheet.getCellByPosition(6, row);
            xCell.setValue(cena);

            xCell = xSpreadsheet.getCellByPosition(14, row - 1);
            tn = xCell.getValue();
            xCell = xSpreadsheet.getCellByPosition(14, row);
            xCell.setValue(tn + razn);
            n = new BigDecimal(xCell.getValue());
            n = n.setScale(0, BigDecimal.ROUND_DOWN);
            xCell.setValue(n.intValue());
            result.put("optTorgNadb", n.intValue());

            xCell = xSpreadsheet.getCellByPosition(17, row - 1);
            ndsTN = xCell.getValue();
            xCell = xSpreadsheet.getCellByPosition(17, row);
            xCell.setValue(ndsTN);
            result.put("ndsTN", ndsTN);

            xCell = xSpreadsheet.getCellByPosition(18, row - 1);
            roznCena = xCell.getValue();
            xCell = xSpreadsheet.getCellByPosition(18, row);
            xCell.setValue(roznCena);
            result.put("roznCena", roznCena);

            for (int p = 0; p < 19; p++) {
                xCell = xSpreadsheet.getCellByPosition(p, row);
                xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime
                        .queryInterface(com.sun.star.beans.XPropertySet.class,
                                xCell);
                xPropSet.setPropertyValue("CharHeight", charSize);
            }
            row++;

            xCell = xSpreadsheet.getCellByPosition(4, row - 2);
            df = new DecimalFormat("#");
            MoneyToStr mts = new MoneyToStr(df.format(xCell.getValue()), dis
                    .get("valuta").toString());
            String str = mts.num2str(true).trim();
            str = str.substring(0, str.lastIndexOf(" "));

            xCell = xSpreadsheet.getCellByPosition(0, row);
            xCell.setFormula("Всего отпущено: " + str + " ед.");
            xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime
                    .queryInterface(com.sun.star.beans.XPropertySet.class,
                            xCell);
            xPropSet.setPropertyValue("CharHeight", charSize);
            row++;

            // mts = new MoneyToStr(dis.get("summa_nds").toString());
            xCell = xSpreadsheet.getCellByPosition(8, row - 2);
            df = new DecimalFormat("#");

            mts = new MoneyToStr(result.get("summa_nds").toString(), dis.get(
                    "valuta").toString());
            str = mts.num2str(true);

            xCell = xSpreadsheet.getCellByPosition(0, row);
            xCell.setFormula("Всего сумма НДС: " + str);
            xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime
                    .queryInterface(com.sun.star.beans.XPropertySet.class,
                            xCell);
            xPropSet.setPropertyValue("CharHeight", charSize);
            row++;

            df = new DecimalFormat("###.##");
            xCell = xSpreadsheet.getCellByPosition(9, row - 2);
            mts = new MoneyToStr(result.get("summa_i_nds").toString(), dis.get(
                    "valuta").toString());
            str = mts.num2str(true);

            xCell = xSpreadsheet.getCellByPosition(0, row);
            xCell.setFormula("Всего стоимость с НДС: " + str);
            xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime
                    .queryInterface(com.sun.star.beans.XPropertySet.class,
                            xCell);
            xPropSet.setPropertyValue("CharHeight", charSize);
            row++;

            xCell = xSpreadsheet.getCellByPosition(2, row);
            xCell.setFormula("Сдал_________________________________ ");
            xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime
                    .queryInterface(com.sun.star.beans.XPropertySet.class,
                            xCell);
            xPropSet.setPropertyValue("CharHeight", charSize);

            xCell = xSpreadsheet.getCellByPosition(7, row);
            xCell.setFormula("Принял_________________________________ ");
            xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime
                    .queryInterface(com.sun.star.beans.XPropertySet.class,
                            xCell);
            xPropSet.setPropertyValue("CharHeight", charSize);
            // row++;
            int page_count = (row - 36) / 29 + 1;
            page_count += (row - 36) % 29 > 0 ? 1 : 0;
            result.put("page_count", page_count);
            currentDocument = openDocumentOld("Templates/Справка.ots");
            spravkaTTN2(result, currentDocument);

            /*
             * XModel model = (XModel) UnoRuntime.queryInterface(XModel.class,
             * currentDocument); XTextViewCursorSupplier supplier =
             * (XTextViewCursorSupplier)
             * UnoRuntime.queryInterface(XTextViewCursorSupplier.class,
             * model.getCurrentController()); XTextViewCursor viewCursor =
             * supplier.getViewCursor(); XPageCursor pageCursor = (XPageCursor)
             * UnoRuntime.queryInterface(XPageCursor.class, viewCursor);
             * pageCursor.jumpToLastPage();
             *
             *
             * pageCursor.jumpToLastPage();
             * System.out.println("Кол-во страниц: " + pageCursor.getPage());
             */
            saveAsDocument(currentDocument, savePath
                    + dis.get("ttn").toString() + ".ods", lParams);
            /*
             * SkladDB db = new SkladDB(); db.saveTTNDB(savePath +
             * dis.get("ttn").toString()+ ".ods",
             * Integer.parseInt(dis.get("ttn_id").toString()),
             * dis.get("ttn").toString()); db.disConn();
             */
        } catch (Exception e) {
            System.out.println(e.getMessage());
            log.error("Ошибка при заполнении отчёта отгрузка покупателю", e);
            JOptionPane.showMessageDialog(null,
                    "Ошибка при заполнении отчёта отгрузка покупателю",
                    "Ошибка!!!", JOptionPane.ERROR_MESSAGE);
        } finally {
            sdb.disConn();
        }

    }

    private void fildPrilojenieTTN2Simple(XComponent currentDocument) {
        result = new HashMap();
        HashMap<String, Object> hm = new HashMap();
        HashMap dis = (HashMap) discr.get(0);
        String sar = "";
        double razn = 0, raznBuf = 0, raznBuf1 = 0, roznCena = 0, cena = 0, tn = 0, ndsTN = 0, kol = 0;
        int lineStart = 0;
        float charSize = (float) 6.5;
        BigDecimal n;
        int st, stn;
        DecimalFormat df;
        Double sumtn = .0;
        Double sumtn_nds = .0;
        Double sumtn_rzn = .0;

        Double sum_stoim = .0;
        Double sum_cena = .0;
        Double sum_kol = .0;
        Double sum_nds = .0;
        Double sum_kgm = .0;
        Double sum_massa = .0;

        SkladDB sdb = new SkladDB();

        try {
            XSpreadsheetDocument xSpreadsheetDocument = (XSpreadsheetDocument) UnoRuntime
                    .queryInterface(XSpreadsheetDocument.class, currentDocument);
            XSpreadsheets xSpreadsheets = xSpreadsheetDocument.getSheets();
            // xSpreadsheets.insertNewByName("Приложение к ТТН №" +
            // dis.get("ttn"), (short)0);
            Object sheet = xSpreadsheets.getByName("Лист1");
            XSpreadsheet xSpreadsheet = (XSpreadsheet) UnoRuntime
                    .queryInterface(XSpreadsheet.class, sheet);

            XNamed xNamed = (XNamed) UnoRuntime.queryInterface(XNamed.class,
                    xSpreadsheet);
            xNamed.setName("Приложение к ТТН №" + dis.get("ttn"));
            result.put("ttn", dis.get("ttn"));

            com.sun.star.beans.XPropertySet xPropSet = null;
            com.sun.star.table.BorderLine aLine = new com.sun.star.table.BorderLine();
            aLine.Color = 0x000000;
            aLine.InnerLineWidth = 0;
            aLine.LineDistance = 0;
            aLine.OuterLineWidth = 25;
            com.sun.star.table.TableBorder aBorder = new com.sun.star.table.TableBorder();
            aBorder.BottomLine = aLine;
            aBorder.IsBottomLineValid = true;

            // ----------------Заполнение шапки документа
            XCell xCell = xSpreadsheet.getCellByPosition(0, 0);
            xCell.setFormula("Грузоотправитель: " + dis.get("adres_otprav"));
            xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime
                    .queryInterface(com.sun.star.beans.XPropertySet.class,
                            xCell);
            xPropSet.setPropertyValue("CharHeight", charSize);

            xCell = xSpreadsheet.getCellByPosition(0, 1);
            xCell.setFormula("Грузополучатель: " + dis.get("adres_poluch"));
            xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime
                    .queryInterface(com.sun.star.beans.XPropertySet.class,
                            xCell);
            xPropSet.setPropertyValue("CharHeight", charSize);

            result.put("adres", dis.get("adres_poluch"));

            xCell = xSpreadsheet.getCellByPosition(0, 2);
            xCell.setFormula(dis.get("dogovor").toString() + " код клиента "
                    + dis.get("kod_klienta").toString());
            xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime
                    .queryInterface(com.sun.star.beans.XPropertySet.class,
                            xCell);
            xPropSet.setPropertyValue("CharHeight", charSize);

            // ----------------Заполнение тела документа
            int row = 7;
            lineStart = row;

            for (int i = 0; i < data.size(); i++) {
                hm = (HashMap) data.get(i);

                if (!sar.equals(hm.get("sar").toString())) {
                    if (row != lineStart) {
                        for (int p = 0; p < 19; p++) {
                            xCell = xSpreadsheet.getCellByPosition(p, row - 1);
                            xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime
                                    .queryInterface(
                                            com.sun.star.beans.XPropertySet.class,
                                            xCell);
                            xPropSet.setPropertyValue("TableBorder", aBorder);
                        }

                        xCell = xSpreadsheet.getCellByPosition(0, row);
                        xCell.setFormula("ИТОГО по артикулу");

                        xCell = xSpreadsheet.getCellByPosition(4, row);
                        xCell.setFormula("=SUM(E" + (lineStart + 3) + ":E"
                                + (row) + ")");
                        // sum_kol.append("E"+(row+1) +";");
                        sum_kol = sum_kol + xCell.getValue();

                        xCell = xSpreadsheet.getCellByPosition(6, row);
                        xCell.setFormula("=SUM(G" + (lineStart + 3) + ":G"
                                + (row) + ")");
                        // sum_stoim.append("G"+(row+1) +";");
                        sum_stoim = sum_stoim + xCell.getValue();

                        xCell = xSpreadsheet.getCellByPosition(8, row);
                        xCell.setFormula("=SUM(I" + (lineStart + 3) + ":I"
                                + (row) + ")");
                        // sum_nds.append("I"+(row+1) +";");
                        sum_nds = sum_nds + xCell.getValue();

                        xCell = xSpreadsheet.getCellByPosition(9, row);
                        xCell.setFormula("=SUM(J" + (lineStart + 3) + ":J"
                                + (row) + ")");
                        // sum_cena.append("J"+(row+1) +";");
                        sum_cena = sum_cena + xCell.getValue();

                        xCell = xSpreadsheet.getCellByPosition(10, row);
                        xCell.setFormula("=SUM(K" + (lineStart + 3) + ":K"
                                + (row) + ")");
                        // sum_kgm.append("K"+(row+1) +";");
                        sum_kgm = sum_kgm + xCell.getValue();

                        xCell = xSpreadsheet.getCellByPosition(11, row);
                        xCell.setFormula("=SUM(L" + (lineStart + 3) + ":L"
                                + row + ")");
                        // sum_massa.append("L"+(row+1) +";");
                        sum_massa = sum_massa + xCell.getValue();

                        xCell = xSpreadsheet.getCellByPosition(14, row);
                        xCell.setFormula("=SUM(O" + (lineStart + 3) + ":O"
                                + row + ")");
                        sumtn = sumtn + xCell.getValue();

                        xCell = xSpreadsheet.getCellByPosition(17, row);
                        xCell.setFormula("=SUM(R" + (lineStart + 3) + ":R"
                                + row + ")");
                        sumtn_nds = sumtn_nds + xCell.getValue(); // .append("R"+(row+1)
                        // +" + ");

                        xCell = xSpreadsheet.getCellByPosition(18, row);
                        xCell.setFormula("=SUM(S" + (lineStart + 3) + ":S"
                                + row + ")");
                        sumtn_rzn = sumtn_rzn + xCell.getValue(); // .append("S"+(row+1)
                        // +" + ");

                        for (int p = 0; p < 19; p++) {
                            xCell = xSpreadsheet.getCellByPosition(p, row);
                            xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime
                                    .queryInterface(
                                            com.sun.star.beans.XPropertySet.class,
                                            xCell);
                            xPropSet.setPropertyValue("CharHeight", charSize);
                        }
                        row++;
                        lineStart = row;
                        razn += raznBuf1;
                        raznBuf = 0;
                        raznBuf1 = 0;

                    }

                    sar = hm.get("sar").toString();

                    String nSar = hm.get("sar").toString() + " ТНВЭД - "
                            + hm.get("narp").toString();
                    xCell = xSpreadsheet.getCellByPosition(0, row);
                    xCell.setFormula(nSar);
                    xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime
                            .queryInterface(
                                    com.sun.star.beans.XPropertySet.class,
                                    xCell);
                    xPropSet.setPropertyValue("CharHeight", charSize);

                    nSar = hm.get("ngpr").toString() + " "
                            + hm.get("nar").toString() + " модель "
                            + hm.get("fas").toString() + " - " + hm.get("preiscur").toString();
                    xCell = xSpreadsheet.getCellByPosition(4, row);
                    xCell.setFormula(nSar);
                    xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime
                            .queryInterface(
                                    com.sun.star.beans.XPropertySet.class,
                                    xCell);
                    xPropSet.setPropertyValue("CharHeight", (float) (8));
/*
                    xCell = xSpreadsheet.getCellByPosition(12, row);
                    xCell.setFormula(hm.get("preiscur").toString());
                    xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime
                            .queryInterface(
                                    com.sun.star.beans.XPropertySet.class,
                                    xCell);
                    xPropSet.setPropertyValue("CharHeight", charSize);
                    */
                    row++;
                    String ser_ggr = "";
                    if (!sar.trim().startsWith("№")) {
                        ser_ggr = "Сертификат: "
                                + sdb.getSertifikat(sar, Integer.parseInt(dis
                                .get("vid_sert").toString()));
                    } else {
                        ser_ggr = "Декларация: "
                                + sdb.getSertifikat(sar, Integer.parseInt(dis
                                .get("vid_sert").toString()));
                    }

                    // + "    Уд. гос. гигиен. рег.: " + sdb.getGGR(sar,
                    // Integer.parseInt(dis.get("vid_ggr").toString()));

                    xCell = xSpreadsheet.getCellByPosition(0, row);
                    xCell.setFormula(ser_ggr);
                    xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime
                            .queryInterface(
                                    com.sun.star.beans.XPropertySet.class,
                                    xCell);
                    xPropSet.setPropertyValue("CharHeight", charSize);
                    row++;

                    /*
                     * ser_ggr = "Уд. гос. гигиен. рег.: " + sdb.getGGR(sar,
                     * Integer.parseInt(dis.get("vid_ggr").toString())); xCell =
                     * xSpreadsheet.getCellByPosition(0, row);
                     * xCell.setFormula(ser_ggr); xPropSet =
                     * (com.sun.star.beans.
                     * XPropertySet)UnoRuntime.queryInterface(
                     * com.sun.star.beans.XPropertySet.class, xCell);
                     * xPropSet.setPropertyValue("CharHeight", charSize);
                     *
                     * row++;
                     */
                }

                xCell = xSpreadsheet.getCellByPosition(0, row);
                xCell.setFormula(hm.get("ncw").toString());

                String srt = hm.get("srt").toString();
                xCell = xSpreadsheet.getCellByPosition(1, row);
                if (sar.startsWith("43")) {
                    if ("1".equals(srt)) {
                        xCell.setFormula("с");
                    } else {
                        xCell.setFormula("у");
                    }
                } else {
                    xCell.setFormula("=LOWER(" + srt + ")");
                }

                /*
                 * xCell = xSpreadsheet.getCellByPosition(1, row);
                 * xCell.setFormula("=LOWER(" + hm.get("srt").toString() + ")");
                 */

                xCell = xSpreadsheet.getCellByPosition(2, row);
                if (hm.get("rzm_print") != null) {
                    xCell.setFormula("'" + hm.get("rzm_print").toString());
                } else {
                    xCell.setFormula("'0");
                }
                // if (Integer.parseInt(hm.get("rst").toString()) != 0 )
                // xCell.setFormula(hm.get("rst").toString() + " - " +
                // hm.get("rzm").toString());
                // else xCell.setFormula("=LOWER(" + hm.get("rzm").toString() +
                // ")");

                xCell = xSpreadsheet.getCellByPosition(3, row);
                if (sar.startsWith("43") || sar.startsWith("48")
                        || sar.startsWith("47")) {
                    xCell.setFormula(hm.get("izm").toString());
                } else {
                    xCell.setFormula("шт.");
                }

                xCell = xSpreadsheet.getCellByPosition(4, row);
                xCell.setFormula(hm.get("kol").toString());

                xCell = xSpreadsheet.getCellByPosition(5, row);
                xCell.setFormula(hm.get("cena").toString());

                xCell = xSpreadsheet.getCellByPosition(6, row);
                xCell.setFormula(hm.get("summa").toString());

                xCell = xSpreadsheet.getCellByPosition(7, row);
                xCell.setFormula(hm.get("nds").toString());

                xCell = xSpreadsheet.getCellByPosition(8, row);
                xCell.setFormula(hm.get("summa_nds").toString());

                xCell = xSpreadsheet.getCellByPosition(9, row);
                xCell.setFormula(hm.get("itogo").toString());

                xCell = xSpreadsheet.getCellByPosition(10, row);
                xCell.setFormula(hm.get("kkr").toString());

                xCell = xSpreadsheet.getCellByPosition(11, row);
                xCell.setFormula(hm.get("massa").toString());

                xCell = xSpreadsheet.getCellByPosition(12, row);
                xCell.setFormula(hm.get("torg_nadbavka").toString());

                xCell = xSpreadsheet.getCellByPosition(13, row);
                xCell.setFormula("= F" + (row + 1) + "* M" + (row + 1)
                        + " / 100");
                n = new BigDecimal(xCell.getValue());
                n = n.setScale(0, BigDecimal.ROUND_HALF_UP);
                xCell.setValue(n.intValue());

                xCell = xSpreadsheet.getCellByPosition(14, row);
                xCell.setFormula("= E" + (row + 1) + "* N" + (row + 1));

                xCell = xSpreadsheet.getCellByPosition(16, row);
                xCell.setFormula("=ROUND( (F" + (row + 1) + "+ N" + (row + 1)
                        + ") * H" + (row + 1) + " / 100;2)");
                n = new BigDecimal(xCell.getValue());
                n = n.setScale(2, BigDecimal.ROUND_HALF_UP);
                xCell.setValue(n.doubleValue());

                xCell = xSpreadsheet.getCellByPosition(15, row);
                xCell.setFormula("= F" + (row + 1) + "+ N" + (row + 1) + " +Q"
                        + (row + 1));
                long d = Math.round(xCell.getValue());

                int ost = (int) (d % 100);
                d /= 100;
                d *= 100;

                int roundV = 100;
                switch (roundV) {
                    case 50:
                        if (ost >= 25 && ost < 75) {
                            d += 50;
                        } else if (ost >= 75) {
                            d += 100;
                        }
                        break;
                    case 100:
                        if (ost >= 25 && ost < 50) {
                            d += 0;
                        } else if (ost >= 50) {
                            d += 100;
                        }
                        break;
                }
/*

                if (ost >= 25 && ost < 75) {
                    d += 50;
                } else if (ost >= 75) {
                    d += 100;
                }*/

                xCell.setValue(d);

                xCell = xSpreadsheet.getCellByPosition(4, row);
                kol = xCell.getValue();
                xCell = xSpreadsheet.getCellByPosition(5, row);
                cena = xCell.getValue();
                xCell = xSpreadsheet.getCellByPosition(13, row);
                tn = xCell.getValue();
                xCell = xSpreadsheet.getCellByPosition(16, row);
                ndsTN = xCell.getValue();
                xCell = xSpreadsheet.getCellByPosition(15, row);
                roznCena = xCell.getValue();

                raznBuf = (roznCena - (cena + ndsTN + tn)) * kol;
                raznBuf1 += raznBuf;

                xCell = xSpreadsheet.getCellByPosition(17, row);
                xCell.setFormula("= Q" + (row + 1) + "* E" + (row + 1));

                xCell = xSpreadsheet.getCellByPosition(18, row);
                xCell.setFormula("= P" + (row + 1) + "* E" + (row + 1));

                for (int p = 0; p < 19; p++) {
                    xCell = xSpreadsheet.getCellByPosition(p, row);
                    xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime
                            .queryInterface(
                                    com.sun.star.beans.XPropertySet.class,
                                    xCell);
                    xPropSet.setPropertyValue("CharHeight", charSize);
                }
                row++;
            }
            row++;
            for (int p = 0; p < 19; p++) {
                xCell = xSpreadsheet.getCellByPosition(p, row - 1);
                xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime
                        .queryInterface(com.sun.star.beans.XPropertySet.class,
                                xCell);
                xPropSet.setPropertyValue("TableBorder", aBorder);
            }

            xCell = xSpreadsheet.getCellByPosition(0, row);
            xCell.setFormula("ИТОГО по артикулу");

            xCell = xSpreadsheet.getCellByPosition(4, row);
            xCell.setFormula("=SUM(E" + (lineStart + 3) + ":E" + (row) + ")");
            // sum_kol.append("E"+(row+1) +";");
            sum_kol = sum_kol + xCell.getValue();

            xCell = xSpreadsheet.getCellByPosition(6, row);
            xCell.setFormula("=SUM(G" + (lineStart + 3) + ":G" + (row) + ")");
            sum_stoim = sum_stoim + xCell.getValue();
            // sum_stoim.append("G"+(row+1) +";");
            n = new BigDecimal(xCell.getValue());
            n = n.setScale(0, BigDecimal.ROUND_DOWN);
            xCell.setValue(n.intValue());

            xCell = xSpreadsheet.getCellByPosition(8, row);
            xCell.setFormula("=SUM(I" + (lineStart + 3) + ":I" + (row) + ")");
            sum_nds = sum_nds + xCell.getValue();
            // sum_nds.append("I"+(row+1) +";");
            n = new BigDecimal(xCell.getValue());
            n = n.setScale(0, BigDecimal.ROUND_DOWN);
            xCell.setValue(n.intValue());

            xCell = xSpreadsheet.getCellByPosition(9, row);
            xCell.setFormula("=SUM(J" + (lineStart + 3) + ":J" + (row) + ")");
            // sum_cena.append("J"+(row+1) +";");
            sum_cena = sum_cena + xCell.getValue();
            n = new BigDecimal(xCell.getValue());
            n = n.setScale(0, BigDecimal.ROUND_DOWN);
            xCell.setValue(n.intValue());

            xCell = xSpreadsheet.getCellByPosition(10, row);
            xCell.setFormula("=SUM(K" + (lineStart + 3) + ":K" + (row) + ")");
            // sum_kgm.append("K"+(row+1) +";");
            sum_kgm = sum_kgm + xCell.getValue();

            xCell = xSpreadsheet.getCellByPosition(11, row);
            xCell.setFormula("=SUM(L" + (lineStart + 3) + ":L" + row + ")");
            // sum_massa.append("L"+(row+1) +";");
            sum_massa = sum_massa + xCell.getValue();

            xCell = xSpreadsheet.getCellByPosition(14, row);
            xCell.setFormula("=SUM(O" + (lineStart + 3) + ":O" + row + ")");
            sumtn = sumtn + xCell.getValue(); // .append("O").append(row+1).append(" + ");

            xCell = xSpreadsheet.getCellByPosition(17, row);
            xCell.setFormula("=SUM(R" + (lineStart + 3) + ":R" + row + ")");
            sumtn_nds = sumtn_nds + xCell.getValue();// .append("R").append(row+1).append(" + ");

            xCell = xSpreadsheet.getCellByPosition(18, row);
            xCell.setFormula("=SUM(S" + (lineStart + 3) + ":S" + row + ")");
            sumtn_rzn = sumtn_rzn + xCell.getValue();// .append("S").append(row+1).append(";");

            for (int p = 0; p < 19; p++) {
                xCell = xSpreadsheet.getCellByPosition(p, row);
                xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime
                        .queryInterface(com.sun.star.beans.XPropertySet.class,
                                xCell);
                xPropSet.setPropertyValue("CharHeight", charSize);
            }
            razn += raznBuf1;
            raznBuf = 0;
            raznBuf1 = 0;
            row++;

            xCell = xSpreadsheet.getCellByPosition(0, row);
            xCell.setFormula("ИТОГО по накладной");

            // sum_kol.append("0)");
            xCell = xSpreadsheet.getCellByPosition(4, row);
            xCell.setFormula(sum_kol.toString());// .toString());

            result.put("kol", xCell.getValue());

            // sum_stoim.append("0)");
            xCell = xSpreadsheet.getCellByPosition(6, row);
            xCell.setFormula(sum_stoim.toString());
            // DecimalFormat df = new DecimalFormat("#");
            n = new BigDecimal(xCell.getValue());
            n = n.setScale(0, BigDecimal.ROUND_UP);
            xCell.setValue(n.intValue());
            result.put("summa", n.toString());
            st = n.intValue();
            // df = new DecimalFormat("#.#");
            // sum_nds.append("0)");
            xCell = xSpreadsheet.getCellByPosition(8, row);
            xCell.setFormula(sum_nds.toString());
            n = new BigDecimal(xCell.getValue());
            n = n.setScale(0, BigDecimal.ROUND_DOWN);
            xCell.setValue(n.intValue());
            result.put("summa_nds", n.toString());
            stn = n.intValue();
            // df = new DecimalFormat("#");
            // sum_cena.append("0)");
            xCell = xSpreadsheet.getCellByPosition(9, row);
            xCell.setFormula(sum_cena.toString());
            n = new BigDecimal(xCell.getValue());
            n = n.setScale(0, BigDecimal.ROUND_DOWN);
            xCell.setValue(n.intValue());
            result.put("summa_i_nds", n.intValue());

            // sum_kgm.append("0)");
            xCell = xSpreadsheet.getCellByPosition(10, row);
            xCell.setFormula(sum_kgm.toString());
            result.put("gruz_mest", xCell.getValue());

            // sum_massa.append("0)");
            xCell = xSpreadsheet.getCellByPosition(11, row);
            xCell.setFormula(sum_massa.toString());
            n = new BigDecimal((xCell.getValue()));
            n = n.setScale(2, BigDecimal.ROUND_DOWN);
            result.put("massa", n);

            // sumtn.append("0");
            xCell = xSpreadsheet.getCellByPosition(14, row);
            xCell.setValue(sumtn);// .toString());
            n = new BigDecimal(xCell.getValue());
            n = n.setScale(0, BigDecimal.ROUND_DOWN);
            xCell.setValue(n.intValue());
            st = n.intValue();
            result.put("summa_ne_okr", st);

            // sumtn_nds.append("0");
            xCell = xSpreadsheet.getCellByPosition(17, row);
            xCell.setValue(sumtn_nds);// .toString());
            n = new BigDecimal(xCell.getValue());
            n = n.setScale(0, BigDecimal.ROUND_DOWN);
            xCell.setValue(n.intValue());
            stn = n.intValue();

            // sumtn_rzn.delete(sumtn_rzn.length()-1, sumtn_rzn.length());
            // sumtn_rzn.append(")");
            xCell = xSpreadsheet.getCellByPosition(18, row);
            xCell.setValue(sumtn_rzn);// .toString());
            n = new BigDecimal(xCell.getValue());
            n = n.setScale(0, BigDecimal.ROUND_DOWN);
            xCell.setValue(n.intValue());
            int summTTN = n.intValue();

            // xCell.setValue(st+Integer.parseInt(result.get("summa").toString()));

            for (int p = 0; p < 19; p++) {
                xCell = xSpreadsheet.getCellByPosition(p, row);
                xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime
                        .queryInterface(com.sun.star.beans.XPropertySet.class,
                                xCell);
                xPropSet.setPropertyValue("CharHeight", charSize);
            }
            row++;

            xCell = xSpreadsheet.getCellByPosition(0, row);
            xCell.setFormula("С учетом округления");

            xCell = xSpreadsheet.getCellByPosition(6, row - 1);
            cena = xCell.getValue();
            xCell = xSpreadsheet.getCellByPosition(6, row);
            xCell.setValue(cena);

            xCell = xSpreadsheet.getCellByPosition(14, row - 1);
            tn = xCell.getValue();
            xCell = xSpreadsheet.getCellByPosition(14, row);
            xCell.setValue(tn + razn);
            n = new BigDecimal(xCell.getValue());
            n = n.setScale(0, BigDecimal.ROUND_DOWN);
            xCell.setValue(n.intValue());
            result.put("optTorgNadb", n.intValue());

            xCell = xSpreadsheet.getCellByPosition(17, row - 1);
            ndsTN = xCell.getValue();
            xCell = xSpreadsheet.getCellByPosition(17, row);
            xCell.setValue(ndsTN);
            result.put("ndsTN", ndsTN);

            xCell = xSpreadsheet.getCellByPosition(18, row - 1);
            roznCena = xCell.getValue();
            xCell = xSpreadsheet.getCellByPosition(18, row);
            xCell.setValue(roznCena);
            n = new BigDecimal(xCell.getValue());
            n = n.setScale(0, BigDecimal.ROUND_DOWN);
            xCell.setValue(n.intValue());
            int roznCenaInt = n.intValue();

            result.put("roznCena", roznCena);

            for (int p = 0; p < 19; p++) {
                xCell = xSpreadsheet.getCellByPosition(p, row);
                xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime
                        .queryInterface(com.sun.star.beans.XPropertySet.class,
                                xCell);
                xPropSet.setPropertyValue("CharHeight", charSize);
            }
            row++;

            xCell = xSpreadsheet.getCellByPosition(4, row - 2);
            df = new DecimalFormat("#");
            MoneyToStr mts = new MoneyToStr(df.format(xCell.getValue()), dis
                    .get("valuta").toString());
            String str = mts.num2str(true).trim();
            str = str.substring(0, str.lastIndexOf(" "));

            xCell = xSpreadsheet.getCellByPosition(0, row);
            xCell.setFormula("Всего отпущено: " + str + " ед.");
            xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime
                    .queryInterface(com.sun.star.beans.XPropertySet.class,
                            xCell);
            xPropSet.setPropertyValue("CharHeight", charSize);
            row++;

            // mts = new MoneyToStr(dis.get("summa_nds").toString());
            xCell = xSpreadsheet.getCellByPosition(8, row - 2);
            df = new DecimalFormat("#");

            //mts = new MoneyToStr(result.get("summa_nds").toString(), dis.get(
            //        "valuta").toString());
            try {
                int _nds = (int) ndsTN;
                mts = new MoneyToStr(_nds, dis.get(
                        "valuta").toString());
                str = mts.num2str(true);
            } catch (Exception ex) {
                System.out.println(String.valueOf(ndsTN) + " / " + dis.get(
                        "valuta").toString());
                str = "";
            }

            xCell = xSpreadsheet.getCellByPosition(0, row);
            xCell.setFormula("Всего сумма НДС: " + str);
            xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime
                    .queryInterface(com.sun.star.beans.XPropertySet.class,
                            xCell);
            xPropSet.setPropertyValue("CharHeight", charSize);
            row++;

            df = new DecimalFormat("###.##");
            xCell = xSpreadsheet.getCellByPosition(9, row - 2);
            // mts = new MoneyToStr(result.get("summa_i_nds").toString(), dis.get(
            //         "valuta").toString());
            mts = new MoneyToStr(roznCenaInt, dis.get(
                    "valuta").toString());
            str = mts.num2str(true);

            xCell = xSpreadsheet.getCellByPosition(0, row);
            xCell.setFormula("Всего стоимость с НДС: " + str);
            xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime
                    .queryInterface(com.sun.star.beans.XPropertySet.class,
                            xCell);
            xPropSet.setPropertyValue("CharHeight", charSize);
            row++;

            xCell = xSpreadsheet.getCellByPosition(0, row);
            xCell.setFormula("Сдал_________________________________ ");
            xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime
                    .queryInterface(com.sun.star.beans.XPropertySet.class,
                            xCell);
            xPropSet.setPropertyValue("CharHeight", charSize);

            xCell = xSpreadsheet.getCellByPosition(7, row);
            xCell.setFormula("Принял_________________________________ ");
            xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime
                    .queryInterface(com.sun.star.beans.XPropertySet.class,
                            xCell);
            xPropSet.setPropertyValue("CharHeight", charSize);
            // row++;
            int page_count = (row - 36) / 29 + 1;
            page_count += (row - 36) % 29 > 0 ? 1 : 0;
            result.put("page_count", page_count);
            currentDocument = openDocumentOld("Templates/Справка.ots");
            spravkaTTN2(result, currentDocument);

            /*
             * XModel model = (XModel) UnoRuntime.queryInterface(XModel.class,
             * currentDocument); XTextViewCursorSupplier supplier =
             * (XTextViewCursorSupplier)
             * UnoRuntime.queryInterface(XTextViewCursorSupplier.class,
             * model.getCurrentController()); XTextViewCursor viewCursor =
             * supplier.getViewCursor(); XPageCursor pageCursor = (XPageCursor)
             * UnoRuntime.queryInterface(XPageCursor.class, viewCursor);
             * pageCursor.jumpToLastPage();
             *
             *
             * pageCursor.jumpToLastPage();
             * System.out.println("Кол-во страниц: " + pageCursor.getPage());
             */
            saveAsDocument(currentDocument, savePath
                    + dis.get("ttn").toString() + ".ods", lParams);
            /*
             * SkladDB db = new SkladDB(); db.saveTTNDB(savePath +
             * dis.get("ttn").toString()+ ".ods",
             * Integer.parseInt(dis.get("ttn_id").toString()),
             * dis.get("ttn").toString()); db.disConn();
             */
        } catch (Exception e) {
            System.out.println(e.getMessage());
            log.error("Ошибка при заполнении отчёта отгрузка покупателю", e);
            JOptionPane.showMessageDialog(null,
                    "Ошибка при заполнении отчёта отгрузка покупателю",
                    "Ошибка!!!", JOptionPane.ERROR_MESSAGE);
        } finally {
            sdb.disConn();
        }

    }


    private void fildPrilojenieTTNRus(XComponent currentDocument) {
        result = new HashMap();
        HashMap<String, Object> hm;
        HashMap dis = (HashMap) discr.get(0);
        String sar = "";
        int lineStart;
        float charSize = (float) 6.5;
        StringBuilder sumtn = new StringBuilder("=");
        StringBuilder sumtn_nds = new StringBuilder("=ROUND(");
        StringBuilder sumtn_rzn = new StringBuilder("=ROUND(");

        StringBuilder sum_stoim = new StringBuilder("=SUM(");
        StringBuilder sum_cena = new StringBuilder("=SUM(");
        StringBuilder sum_kol = new StringBuilder("=SUM(");
        StringBuilder sum_nds = new StringBuilder("=SUM(");
        StringBuilder sum_kgm = new StringBuilder("=SUM(");
        StringBuilder sum_massa = new StringBuilder("=SUM(");

        SkladDB sdb = new SkladDB();

        try {
            XSpreadsheetDocument xSpreadsheetDocument = (XSpreadsheetDocument) UnoRuntime
                    .queryInterface(XSpreadsheetDocument.class, currentDocument);
            XSpreadsheets xSpreadsheets = xSpreadsheetDocument.getSheets();
            // xSpreadsheets.insertNewByName("Приложение к ТТН №" +
            // dis.get("ttn"), (short)0);
            Object sheet = xSpreadsheets.getByName("Лист1");
            XSpreadsheet xSpreadsheet = (XSpreadsheet) UnoRuntime
                    .queryInterface(XSpreadsheet.class, sheet);

            XNamed xNamed = (XNamed) UnoRuntime.queryInterface(XNamed.class,
                    xSpreadsheet);
            xNamed.setName("Приложение к ТТН №" + dis.get("ttn"));

            com.sun.star.beans.XPropertySet xPropSet;
            com.sun.star.table.BorderLine aLine = new com.sun.star.table.BorderLine();
            aLine.Color = 0x000000;
            aLine.Color = 0x000000;
            aLine.InnerLineWidth = 0;
            aLine.LineDistance = 0;
            aLine.OuterLineWidth = 25;
            com.sun.star.table.TableBorder aBorder = new com.sun.star.table.TableBorder();
            aBorder.BottomLine = aLine;
            aBorder.IsBottomLineValid = true;

            // ----------------Заполнение шапки документа
            XCell xCell = xSpreadsheet.getCellByPosition(0, 0);
            xCell.setFormula("Грузоотправитель: " + dis.get("adres_otprav"));
            xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime
                    .queryInterface(com.sun.star.beans.XPropertySet.class,
                            xCell);
            xPropSet.setPropertyValue("CharHeight", charSize);

            xCell = xSpreadsheet.getCellByPosition(0, 1);
            xCell.setFormula("Грузополучатель: " + dis.get("adres_poluch"));
            xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime
                    .queryInterface(com.sun.star.beans.XPropertySet.class,
                            xCell);
            xPropSet.setPropertyValue("CharHeight", charSize);

            xCell = xSpreadsheet.getCellByPosition(0, 2);
            xCell.setFormula(dis.get("dogovor").toString() + " код клиента "
                    + dis.get("kod_klienta").toString());
            xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime
                    .queryInterface(com.sun.star.beans.XPropertySet.class,
                            xCell);
            xPropSet.setPropertyValue("CharHeight", charSize);

            xCell = xSpreadsheet.getCellByPosition(5, 3);
            xCell.setFormula("Цена, " + dis.get("valuta_str").toString());
            xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime
                    .queryInterface(com.sun.star.beans.XPropertySet.class,
                            xCell);
            xPropSet.setPropertyValue("CharHeight", 8);
            xCell = xSpreadsheet.getCellByPosition(6, 3);
            xCell.setFormula("Стоимость, " + dis.get("valuta_str").toString());
            xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime
                    .queryInterface(com.sun.star.beans.XPropertySet.class,
                            xCell);
            xPropSet.setPropertyValue("CharHeight", 8);
            xCell = xSpreadsheet.getCellByPosition(8, 3);
            xCell.setFormula("Сумма НДС, " + dis.get("valuta_str").toString());
            xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime
                    .queryInterface(com.sun.star.beans.XPropertySet.class,
                            xCell);
            xPropSet.setPropertyValue("CharHeight", 8);
            xCell = xSpreadsheet.getCellByPosition(9, 3);
            xCell.setFormula("Стоимость с НДС, "
                    + dis.get("valuta_str").toString());
            xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime
                    .queryInterface(com.sun.star.beans.XPropertySet.class,
                            xCell);
            xPropSet.setPropertyValue("CharHeight", 8);

            // ----------------Заполнение тела документа
            int row = 7;
            lineStart = row;

            for (int i = 0; i < data.size(); i++) {
                hm = (HashMap) data.get(i);

                if (!sar.equals(hm.get("sar").toString())) {
                    if (row != lineStart) {
                        for (int p = 0; p < 16; p++) {
                            xCell = xSpreadsheet.getCellByPosition(p, row - 1);
                            xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime
                                    .queryInterface(
                                            com.sun.star.beans.XPropertySet.class,
                                            xCell);
                            xPropSet.setPropertyValue("TableBorder", aBorder);
                        }

                        xCell = xSpreadsheet.getCellByPosition(0, row);
                        xCell.setFormula("ИТОГО по артикулу");

                        xCell = xSpreadsheet.getCellByPosition(4, row);
                        xCell.setFormula("=SUM(E" + (lineStart + 3) + ":E"
                                + (row) + ")");
                        sum_kol.append("E" + (row + 1) + ";");

                        xCell = xSpreadsheet.getCellByPosition(6, row);
                        xCell.setFormula("=SUM(G" + (lineStart + 3) + ":G"
                                + (row) + ")");
                        sum_stoim.append("G" + (row + 1) + ";");

                        xCell = xSpreadsheet.getCellByPosition(8, row);
                        xCell.setFormula("=SUM(I" + (lineStart + 3) + ":I"
                                + (row) + ")");
                        sum_nds.append("I" + (row + 1) + ";");

                        xCell = xSpreadsheet.getCellByPosition(9, row);
                        xCell.setFormula("=SUM(J" + (lineStart + 3) + ":J"
                                + (row) + ")");
                        sum_cena.append("J" + (row + 1) + ";");

                        xCell = xSpreadsheet.getCellByPosition(10, row);
                        xCell.setFormula("=SUM(K" + (lineStart + 3) + ":K"
                                + (row) + ")");
                        sum_kgm.append("K" + (row + 1) + ";");

                        xCell = xSpreadsheet.getCellByPosition(11, row);
                        xCell.setFormula("=SUM(L" + (lineStart + 3) + ":L"
                                + row + ")");
                        sum_massa.append("L" + (row + 1) + ";");

                        xCell = xSpreadsheet.getCellByPosition(14, row);
                        xCell.setFormula("=SUM(O" + (lineStart + 3) + ":O"
                                + row + ")");
                        sumtn.append("O" + (row + 1) + " + ");

                        xCell = xSpreadsheet.getCellByPosition(13, row);
                        xCell.setFormula("=SUM(N" + (lineStart + 3) + ":N"
                                + row + ")");
                        sumtn_nds.append("N" + (row + 1) + " + ");

                        xCell = xSpreadsheet.getCellByPosition(15, row);
                        xCell.setFormula("=ROUND(SUM(P" + (lineStart + 3)
                                + ":P" + row + ");2");
                        sumtn_rzn.append("P" + (row + 1) + " + ");

                        for (int p = 0; p < 16; p++) {
                            xCell = xSpreadsheet.getCellByPosition(p, row);
                            xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime
                                    .queryInterface(
                                            com.sun.star.beans.XPropertySet.class,
                                            xCell);
                            xPropSet.setPropertyValue("CharHeight", charSize);
                        }
                        row++;
                        lineStart = row;
                    }

                    sar = hm.get("sar").toString();

                    String nSar = hm.get("sar").toString() + " ТНВЭД - "
                            + hm.get("narp").toString();
                    xCell = xSpreadsheet.getCellByPosition(0, row);
                    xCell.setFormula(nSar);

                    xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime
                            .queryInterface(
                                    com.sun.star.beans.XPropertySet.class,
                                    xCell);
                    xPropSet.setPropertyValue("CharHeight", charSize);

                    nSar = hm.get("ngpr").toString() + " "
                            + hm.get("nar").toString() + " модель "
                            + hm.get("fas").toString();
                    xCell = xSpreadsheet.getCellByPosition(4, row);
                    xCell.setFormula(nSar);

                    xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime
                            .queryInterface(
                                    com.sun.star.beans.XPropertySet.class,
                                    xCell);
                    xPropSet.setPropertyValue("CharHeight", (float) (8));

                    xCell = xSpreadsheet.getCellByPosition(12, row);
                    xCell.setFormula(hm.get("preiscur").toString());
                    xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime
                            .queryInterface(
                                    com.sun.star.beans.XPropertySet.class,
                                    xCell);
                    xPropSet.setPropertyValue("CharHeight", charSize);
                    row++;

                    String ser_ggr = "Сертификат: "
                            + sdb.getSertifikat(sar, Integer.parseInt(dis.get(
                            "vid_sert").toString()));// +

                    xCell = xSpreadsheet.getCellByPosition(0, row);
                    xCell.setFormula(ser_ggr);
                    xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime
                            .queryInterface(
                                    com.sun.star.beans.XPropertySet.class,
                                    xCell);
                    xPropSet.setPropertyValue("CharHeight", charSize);
                    row++;

                }

                xCell = xSpreadsheet.getCellByPosition(0, row);
                xCell.setFormula(hm.get("ncw").toString());

                String srt = hm.get("srt").toString();
                xCell = xSpreadsheet.getCellByPosition(1, row);
                if (sar.startsWith("43")) {
                    if ("1".equals(srt)) {
                        xCell.setFormula("с");
                    } else {
                        xCell.setFormula("у");
                    }
                } else {
                    xCell.setFormula("=LOWER(" + srt + ")");
                }

                //xCell.setFormula("=LOWER(" + hm.get("srt").toString() + ")");

                xCell = xSpreadsheet.getCellByPosition(2, row);
                if (hm.get("rzm_print") != null) {
                    xCell.setFormula("'" + hm.get("rzm_print").toString());
                } else {
                    xCell.setFormula("'0");
                }
                // if (Integer.parseInt(hm.get("rst").toString()) != 0 )
                // xCell.setFormula(hm.get("rst").toString() + " - " +
                // hm.get("rzm").toString());
                // else xCell.setFormula("=LOWER(" + hm.get("rzm").toString() +
                // ")");

                xCell = xSpreadsheet.getCellByPosition(3, row);
                if (sar.startsWith("43") || sar.startsWith("48")
                        || sar.startsWith("47")) {
                    xCell.setFormula(hm.get("izm").toString());
                } else {
                    xCell.setFormula("шт.");
                }

                xCell = xSpreadsheet.getCellByPosition(4, row);
                xCell.setFormula(hm.get("kol").toString());

                xCell = xSpreadsheet.getCellByPosition(5, row);
                xCell.setFormula(hm.get("cena_rus").toString());

                xCell = xSpreadsheet.getCellByPosition(6, row);
                xCell.setFormula(hm.get("summa_rus").toString());

                xCell = xSpreadsheet.getCellByPosition(7, row);
                xCell.setFormula(hm.get("nds").toString());

                xCell = xSpreadsheet.getCellByPosition(8, row);
                xCell.setFormula(hm.get("summa_nds_rus").toString());

                xCell = xSpreadsheet.getCellByPosition(9, row);
                xCell.setFormula(hm.get("summa_i_nds_rus").toString());

                xCell = xSpreadsheet.getCellByPosition(10, row);
                xCell.setFormula(hm.get("kkr").toString());

                xCell = xSpreadsheet.getCellByPosition(11, row);
                xCell.setFormula(hm.get("massa").toString());

                xCell = xSpreadsheet.getCellByPosition(12, row);
                xCell.setFormula(hm.get("cena").toString());

                xCell = xSpreadsheet.getCellByPosition(13, row);
                xCell.setFormula(hm.get("summa").toString());

                xCell = xSpreadsheet.getCellByPosition(14, row);
                BigDecimal n = new BigDecimal(hm.get("summa_nds").toString());
                n = n.setScale(2, BigDecimal.ROUND_HALF_UP);
                xCell.setFormula(n.toString());

                xCell = xSpreadsheet.getCellByPosition(15, row);
                n = new BigDecimal(hm.get("itogo").toString());
                n = n.setScale(2, BigDecimal.ROUND_HALF_UP);
                xCell.setFormula(n.toString());

                for (int p = 0; p < 16; p++) {
                    xCell = xSpreadsheet.getCellByPosition(p, row);
                    xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime
                            .queryInterface(
                                    com.sun.star.beans.XPropertySet.class,
                                    xCell);
                    xPropSet.setPropertyValue("CharHeight", charSize);
                }
                row++;
            }
            row++;
            for (int p = 0; p < 16; p++) {
                xCell = xSpreadsheet.getCellByPosition(p, row - 1);
                xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime
                        .queryInterface(com.sun.star.beans.XPropertySet.class,
                                xCell);
                xPropSet.setPropertyValue("TableBorder", aBorder);
            }

            xCell = xSpreadsheet.getCellByPosition(0, row);
            xCell.setFormula("ИТОГО по артикулу");

            xCell = xSpreadsheet.getCellByPosition(4, row);
            xCell.setFormula("=SUM(E" + (lineStart + 3) + ":E" + (row) + ")");
            sum_kol.append("E" + (row + 1) + ";");

            xCell = xSpreadsheet.getCellByPosition(6, row);
            xCell.setFormula("=SUM(G" + (lineStart + 3) + ":G" + (row) + ")");
            sum_stoim.append("G" + (row + 1) + ";");

            xCell = xSpreadsheet.getCellByPosition(8, row);
            xCell.setFormula("=SUM(I" + (lineStart + 3) + ":I" + (row) + ")");
            sum_nds.append("I" + (row + 1) + ";");

            xCell = xSpreadsheet.getCellByPosition(9, row);
            xCell.setFormula("=SUM(J" + (lineStart + 3) + ":J" + (row) + ")");
            sum_cena.append("J" + (row + 1) + ";");

            xCell = xSpreadsheet.getCellByPosition(10, row);
            xCell.setFormula("=SUM(K" + (lineStart + 3) + ":K" + (row) + ")");
            sum_kgm.append("K" + (row + 1) + ";");

            xCell = xSpreadsheet.getCellByPosition(11, row);
            xCell.setFormula("=SUM(L" + (lineStart + 3) + ":L" + row + ")");
            sum_massa.append("L" + (row + 1) + ";");

            xCell = xSpreadsheet.getCellByPosition(14, row);
            xCell.setFormula("=SUM(O" + (lineStart + 3) + ":O" + row + ")");
            sumtn.append("O" + (row + 1) + " + ");

            xCell = xSpreadsheet.getCellByPosition(13, row);
            xCell.setFormula("=SUM(N" + (lineStart + 3) + ":N" + row + ")");
            sumtn_nds.append("N" + (row + 1) + " + ");

            xCell = xSpreadsheet.getCellByPosition(15, row);
            xCell.setFormula("=SUM(P" + (lineStart + 3) + ":P" + row + ")");
            sumtn_rzn.append("P" + (row + 1) + " + ");

            for (int p = 0; p < 16; p++) {
                xCell = xSpreadsheet.getCellByPosition(p, row);
                xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime
                        .queryInterface(com.sun.star.beans.XPropertySet.class,
                                xCell);
                xPropSet.setPropertyValue("CharHeight", charSize);
            }
            row++;

            xCell = xSpreadsheet.getCellByPosition(0, row);
            xCell.setFormula("ИТОГО по накладной");

            sum_kol.append("0)");
            xCell = xSpreadsheet.getCellByPosition(4, row);
            xCell.setFormula(sum_kol.toString());
            result.put("kol", xCell.getValue());

            sum_stoim.append("0)");
            xCell = xSpreadsheet.getCellByPosition(6, row);
            xCell.setFormula(sum_stoim.toString());
            BigDecimal n = new BigDecimal((xCell.getValue()));
            n = n.setScale(2, BigDecimal.ROUND_HALF_UP);
            result.put("summa_rus", n.doubleValue());

            sum_nds.append("0)");
            xCell = xSpreadsheet.getCellByPosition(8, row);
            xCell.setFormula(sum_nds.toString());
            result.put("summa_nds", xCell.getValue());

            sum_cena.append("0)");
            xCell = xSpreadsheet.getCellByPosition(9, row);
            xCell.setFormula(sum_cena.toString());
            result.put("summa_i_nds", xCell.getValue());

            sum_kgm.append("0)");
            xCell = xSpreadsheet.getCellByPosition(10, row);
            xCell.setFormula(sum_kgm.toString());
            result.put("gruz_mest", xCell.getValue());

            sum_massa.append("0)");
            xCell = xSpreadsheet.getCellByPosition(11, row);
            xCell.setFormula(sum_massa.toString());
            n = new BigDecimal((xCell.getValue()));
            n = n.setScale(2, BigDecimal.ROUND_HALF_UP);
            result.put("massa", n.doubleValue());

            sumtn.append("0");
            xCell = xSpreadsheet.getCellByPosition(14, row);
            xCell.setFormula(sumtn.toString());
            n = new BigDecimal((xCell.getValue()));
            n = n.setScale(2, BigDecimal.ROUND_HALF_UP);
            result.put("summa_nds", n.intValue());

            sumtn_nds.append("0;0)");
            xCell = xSpreadsheet.getCellByPosition(13, row);
            xCell.setFormula(sumtn_nds.toString());
            n = new BigDecimal((xCell.getValue()));
            n = n.setScale(2, BigDecimal.ROUND_HALF_UP);
            result.put("summa", n.intValue());

            sumtn_rzn.append("0;0)");
            xCell = xSpreadsheet.getCellByPosition(15, row);
            xCell.setFormula(sumtn_rzn.toString());
            n = new BigDecimal((xCell.getValue()));
            n = n.setScale(2, BigDecimal.ROUND_HALF_UP);
            result.put("summa_i_nds", n.intValue());

            for (int p = 0; p < 16; p++) {
                xCell = xSpreadsheet.getCellByPosition(p, row);
                xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime
                        .queryInterface(com.sun.star.beans.XPropertySet.class,
                                xCell);
                xPropSet.setPropertyValue("CharHeight", charSize);
            }
            row++;

            xCell = xSpreadsheet.getCellByPosition(4, row - 1);
            MoneyToStr mts = new MoneyToStr((Double) (xCell.getValue()), dis
                    .get("valuta").toString());
            String str = mts.num2str(true).trim();
            str = str.substring(0, str.lastIndexOf(" "));

            xCell = xSpreadsheet.getCellByPosition(0, row);
            xCell.setFormula("Всего отпущено: " + str + " ед.");
            xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime
                    .queryInterface(com.sun.star.beans.XPropertySet.class,
                            xCell);
            xPropSet.setPropertyValue("CharHeight", charSize);
            row++;

            // mts = new MoneyToStr(dis.get("summa_nds").toString());
            xCell = xSpreadsheet.getCellByPosition(8, row - 2);
            n = new BigDecimal((xCell.getValue()));
            n = n.setScale(2, BigDecimal.ROUND_HALF_DOWN);
            mts = new MoneyToStr(n.doubleValue(), dis.get("valuta").toString());
            str = mts.num2str(false);
            result.put("summa_nds_rus", n);

            xCell = xSpreadsheet.getCellByPosition(0, row);
            xCell.setFormula("Всего сумма НДС: " + str);
            xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime
                    .queryInterface(com.sun.star.beans.XPropertySet.class,
                            xCell);
            xPropSet.setPropertyValue("CharHeight", charSize);
            row++;

            xCell = xSpreadsheet.getCellByPosition(9, row - 3);
            n = new BigDecimal((xCell.getValue()));
            n = n.setScale(2, BigDecimal.ROUND_HALF_DOWN);
            mts = new MoneyToStr(n.doubleValue(), dis.get("valuta").toString());

            str = mts.num2str(false);
            result.put("summa_i_nds_rus", n);

            xCell = xSpreadsheet.getCellByPosition(0, row);
            xCell.setFormula("Всего стоимость с НДС: " + str);
            xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime
                    .queryInterface(com.sun.star.beans.XPropertySet.class,
                            xCell);
            xPropSet.setPropertyValue("CharHeight", charSize);
            row++;

            xCell = xSpreadsheet.getCellByPosition(2, row);
            xCell.setFormula("Сдал_________________________________ ");
            xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime
                    .queryInterface(com.sun.star.beans.XPropertySet.class,
                            xCell);
            xPropSet.setPropertyValue("CharHeight", charSize);

            xCell = xSpreadsheet.getCellByPosition(7, row);
            xCell.setFormula("Принял_________________________________ ");
            xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime
                    .queryInterface(com.sun.star.beans.XPropertySet.class,
                            xCell);
            xPropSet.setPropertyValue("CharHeight", charSize);
            // row++;
            int page_count = (row - 36) / 29 + 1;
            page_count += (row - 36) % 29 > 0 ? 1 : 0;
            result.put("page_count", page_count + " прил. ");

            saveAsDocument(currentDocument, savePath
                    + dis.get("ttn").toString() + ".ods", lParams);

        } catch (Exception e) {
            System.out.println(e.getMessage());
            log.error("Ошибка при заполнении отчёта отгрузка покупателю", e);
            JOptionPane.showMessageDialog(null,
                    "Ошибка при заполнении отчёта отгрузка покупателю",
                    "Ошибка!!!", JOptionPane.ERROR_MESSAGE);
        } finally {
            sdb.disConn();
        }

    }

    private void fildPutevoiList(XComponent currentDocument) {
        HashMap hm = (HashMap) data.get(0);
        HashMap dis = (HashMap) discr.get(0);

        try {
            XTextFieldsSupplier xTextFieldsSupplier = (XTextFieldsSupplier) UnoRuntime
                    .queryInterface(XTextFieldsSupplier.class, currentDocument);

            // Создадим перечисление всех полей документа
            XEnumerationAccess xEnumerationAccess = xTextFieldsSupplier
                    .getTextFields();
            XEnumeration xTextFieldsEnumeration = xEnumerationAccess
                    .createEnumeration();
            XRefreshable xRefreshable = (XRefreshable) UnoRuntime
                    .queryInterface(XRefreshable.class, xEnumerationAccess);

            while (xTextFieldsEnumeration.hasMoreElements()) {
                Object service = xTextFieldsEnumeration.nextElement();

                XServiceInfo xServiceInfo = (XServiceInfo) UnoRuntime
                        .queryInterface(XServiceInfo.class, service);

                if (xServiceInfo
                        .supportsService("com.sun.star.text.TextField.SetExpression")) {
                    XPropertySet xPropertySet = (XPropertySet) UnoRuntime
                            .queryInterface(XPropertySet.class, service);
                    String name = (String) xPropertySet
                            .getPropertyValue("VariableName");
                    Object content = hm.get(name);

                    xPropertySet.setPropertyValue("SubType", new Short(
                            com.sun.star.text.SetVariableType.STRING));
                    if (content.equals("")) {
                        xPropertySet.setPropertyValue("Content", " 0 ");
                    } else {
                        xPropertySet.setPropertyValue("Content",
                                content.toString());
                    }
                    xPropertySet.setPropertyValue("IsVisible", true);
                }
            }
            xRefreshable.refresh();
            saveAsDocument(currentDocument, savePath + hm.get("ttn").toString()
                    + ".odt", lParams);
        } catch (Exception e) {
            System.out.println("Ошибка печати документа " + e);
        }
    }

    public void createReport(int roundValue, String nameTemplate) {
        this.roundValue = roundValue;
        createReport(nameTemplate);
    }

    public void createReport(String nameTamplates) {
        try {
            System.out.println("Шаблон " + nameTamplates);
            connect();
            XComponent currentDocument = openDocumentOld("Templates/"
                    + nameTamplates);
            if (nameTamplates.equals("ПриложениеТТН.ots")) {
                fildPrilojenieTTN(currentDocument);
            } else if (nameTamplates.equals("ПутевойЛист.ott")
                    || nameTamplates.equals("ПутевойЛистРус.ott")
                    || nameTamplates.equals("ПЛТН2.ott")
                    || nameTamplates.equals("ПутевойЛистРусТТН_new.ott")) {
                fildPutevoiList(currentDocument);
            } else if (nameTamplates.equals("ПриложениеТТН2.ots")) {
                fildPrilojenieTTN2(currentDocument);
            } else if (nameTamplates.equals("ПриложениеТТНРос.ots")) {
                fildPrilojenieTTNRus(currentDocument);
            } else if (nameTamplates
                    .equals("ПриложениеТТН Скидка сумма док.ots")) {
                fildPrilojenieTTNBelSkidkaSumDoc(currentDocument);
            } else if (nameTamplates
                    .equals("ПриложениеТТН Скидка цена изд.ots")) {
                fildPrilojenieTTNBelSkidkaIzd(currentDocument);
            } else if (nameTamplates.equals("ReportActualSpecCen.ots")) {
                reportSpecCena(currentDocument);
            } else if (nameTamplates.equals("firmMag.ots")) {
                svodnayaVedomostPoFirmMag(currentDocument);
            } else if (nameTamplates.equals("протоколЕвроторг.ots")) {
                protocolEvrotorg(currentDocument);
            } else if (nameTamplates.equals("протоколБелВиллесденНовый.ots")) {
                protocolBelVillesdenNew(currentDocument);
            } else if (nameTamplates.equals("протоколЮнифуд.ots")) {
                protocolUnifud(currentDocument);
            } else if (nameTamplates.equals("протоколБелпочта.ots")) {
                protocolBelPost(currentDocument);
            } else if (nameTamplates.equals("протоколГринРозница.ots")) {
                protocolGreen(currentDocument);
            } else if (nameTamplates.equals("протоколСтандартный.ots")) {
                protocolStandart(currentDocument);
            } else if (nameTamplates.equals("протоколПростор.ots")) {
                protocolProstor(currentDocument);
            } else if (nameTamplates.equals("протоколТабакИнвест.ots")) {
                protocolTInvest(currentDocument);
            } else if (nameTamplates.equals("ПриложениеТТН2Простой.ots")) {
                fildPrilojenieTTN2Simple(currentDocument);
            }

        } catch (java.lang.Exception e) {
            System.err
                    .println("Error in createReport in class SkladOO. \n Please report it to the developer \n"
                            + e);
            log.error("Error in createReport in class SkladOO.", e);
            JOptionPane
                    .showMessageDialog(
                            null,
                            "Error in method createReport in class SkladOO. \n Please report it to the developer \n");
        }
    }


    public void spravkaTTN2(Map rez, XComponent currentDocument) {
        float charSize = (float) 8;
        try {
            XSpreadsheetDocument xSpreadsheetDocument = (XSpreadsheetDocument) UnoRuntime
                    .queryInterface(XSpreadsheetDocument.class, currentDocument);
            XSpreadsheets xSpreadsheets = xSpreadsheetDocument.getSheets();
            // xSpreadsheets.insertNewByName("Справка к ТТН №" +
            // result.get("ttn"), (short) 0);
            Object sheet = xSpreadsheets.getByName("Лист1");
            XSpreadsheet xSpreadsheet = (XSpreadsheet) UnoRuntime
                    .queryInterface(XSpreadsheet.class, sheet);
            com.sun.star.table.BorderLine aLine = new com.sun.star.table.BorderLine();
            aLine.Color = 0x000000;
            aLine.Color = 0x000000;
            aLine.InnerLineWidth = 0;
            aLine.LineDistance = 0;
            aLine.OuterLineWidth = 25;
            com.sun.star.table.TableBorder aBorder = new com.sun.star.table.TableBorder();
            aBorder.BottomLine = aLine;
            aBorder.IsBottomLineValid = true;

            setPropertyAndValue(xSpreadsheet, 0, 0,
                    "Справка об итогах по ТТН № " + result.get("ttn"),
                    charSize, null, "LEFT");
            setPropertyAndValue(xSpreadsheet, 0, 1, "Грузополучатель: "
                    + result.get("adres"), charSize, null, "LEFT");
            setPropertyAndValue(xSpreadsheet, 0, 3, result.get("ttn")
                    .toString(), charSize, null, "LEFT");
            setPropertyAndValue(xSpreadsheet, 1, 3, result.get("gruz_mest")
                    .toString(), charSize, null, "LEFT");
            setPropertyAndValue(xSpreadsheet, 2, 3, result.get("kol")
                    .toString(), charSize, null, "LEFT");
            setPropertyAndValue(xSpreadsheet, 3, 3, String.valueOf((Integer
                            .parseInt(result.get("summa").toString()) + Integer
                            .parseInt(result.get("summa_ne_okr").toString()))),
                    charSize, null, "LEFT");
            setPropertyAndValue(xSpreadsheet, 4, 3, result.get("ndsTN")
                    .toString(), charSize, null, "LEFT");
            setPropertyAndValue(xSpreadsheet, 5, 3, result.get("roznCena")
                    .toString(), charSize, null, "LEFT");
            setPropertyAndValue(xSpreadsheet, 6, 3, result.get("massa")
                    .toString(), charSize, null, "LEFT");
            setPropertyAndValue(xSpreadsheet, 1, 4, result.get("gruz_mest")
                    .toString(), charSize, null, "LEFT");
            setPropertyAndValue(xSpreadsheet, 2, 4, result.get("kol")
                    .toString(), charSize, null, "LEFT");
            setPropertyAndValue(xSpreadsheet, 3, 4, String.valueOf((Integer
                            .parseInt(result.get("summa").toString()) + Integer
                            .parseInt(result.get("summa_ne_okr").toString()))),
                    charSize, null, "LEFT");
            setPropertyAndValue(xSpreadsheet, 4, 4, result.get("ndsTN")
                    .toString(), charSize, null, "LEFT");
            setPropertyAndValue(xSpreadsheet, 5, 4, result.get("roznCena")
                    .toString(), charSize, null, "LEFT");
            setPropertyAndValue(xSpreadsheet, 6, 4, result.get("massa")
                    .toString(), charSize, null, "LEFT");

            // spravka 2
            setPropertyAndValue(xSpreadsheet, 0, 6,
                    "Справка об итогах по ТТН № " + result.get("ttn")
                            + " (c учетом округления)", charSize, null, "LEFT");
            setPropertyAndValue(xSpreadsheet, 0, 7, "Грузополучатель: "
                    + result.get("adres"), charSize, null, "LEFT");
            setPropertyAndValue(xSpreadsheet, 0, 9, result.get("ttn")
                    .toString(), charSize, null, "LEFT");
            setPropertyAndValue(xSpreadsheet, 1, 9, result.get("gruz_mest")
                    .toString(), charSize, null, "LEFT");
            setPropertyAndValue(xSpreadsheet, 2, 9, result.get("kol")
                    .toString(), charSize, null, "LEFT");
            setPropertyAndValue(xSpreadsheet, 3, 9, String.valueOf((Integer
                            .parseInt(result.get("summa").toString()) + Integer
                            .parseInt(result.get("optTorgNadb").toString()))),
                    charSize, null, "LEFT");
            setPropertyAndValue(xSpreadsheet, 4, 9, result.get("ndsTN")
                    .toString(), charSize, null, "LEFT");
            setPropertyAndValue(xSpreadsheet, 5, 9, result.get("roznCena")
                    .toString(), charSize, null, "LEFT");
            setPropertyAndValue(xSpreadsheet, 6, 9, result.get("massa")
                    .toString(), charSize, null, "LEFT");
            setPropertyAndValue(xSpreadsheet, 1, 10, result.get("gruz_mest")
                    .toString(), charSize, null, "LEFT");
            setPropertyAndValue(xSpreadsheet, 2, 10, result.get("kol")
                    .toString(), charSize, null, "LEFT");
            setPropertyAndValue(xSpreadsheet, 3, 10, String.valueOf((Integer
                            .parseInt(result.get("summa").toString()) + Integer
                            .parseInt(result.get("optTorgNadb").toString()))),
                    charSize, null, "LEFT");
            setPropertyAndValue(xSpreadsheet, 4, 10, result.get("ndsTN")
                    .toString(), charSize, null, "LEFT");
            setPropertyAndValue(xSpreadsheet, 5, 10, result.get("roznCena")
                    .toString(), charSize, null, "LEFT");
            setPropertyAndValue(xSpreadsheet, 6, 10, result.get("massa")
                    .toString(), charSize, null, "LEFT");
        } catch (Exception e) {
            System.err
                    .println("Error in spravkaTTN2 in class SkladOO. \n Please report it to the developer \n"
                            + e);
            log.error("Error in spravkaTTN2 in class SkladOO.", e);
            JOptionPane
                    .showMessageDialog(
                            null,
                            "Error in method spravkaTTN2 in class SkladOO. \n Please report it to the developer \n");
        }
    }

    /*
     * Метод формирования справок для россии Как работает не знаю, но лучше
     * ничего не менять)
     */
    public void getCertificateForTTN(String nameTamplates) throws Exception {
        connect();
        XComponent currDoc = openDocumentOld("Templates/" + nameTamplates);
        HashMap<String, Object> hm = new HashMap();
        HashMap dis = (HashMap) discr.get(0);
        int lineStart = 0;
        float charSize = (float) 9;
        BigDecimal n;

        double mass, summar, summa_ndsr, summa_allr;
        try {
            XSpreadsheetDocument xSpreadsheetDocument = (XSpreadsheetDocument) UnoRuntime
                    .queryInterface(XSpreadsheetDocument.class, currDoc);
            XSpreadsheets xSpreadsheets = xSpreadsheetDocument.getSheets();
            SkladDB sdb;
            com.sun.star.table.BorderLine aLine = new com.sun.star.table.BorderLine();
            aLine.Color = 0x000000;
            aLine.InnerLineWidth = 0;
            aLine.LineDistance = 0;
            aLine.OuterLineWidth = 25;
            com.sun.star.table.TableBorder aBorder = new com.sun.star.table.TableBorder();
            aBorder.BottomLine = aLine;
            aBorder.LeftLine = aLine;
            aBorder.RightLine = aLine;
            aBorder.TopLine = aLine;
            aBorder.IsBottomLineValid = true;
            aBorder.IsLeftLineValid = true;
            aBorder.IsRightLineValid = true;
            aBorder.IsTopLineValid = true;

            // Спецификация 1й лист
            Object sheet = xSpreadsheets.getByName("Спецификация");
            XSpreadsheet xSpreadsheet = (XSpreadsheet) UnoRuntime
                    .queryInterface(XSpreadsheet.class, sheet);
            // ----------------Заполнение шапки документа
            setPropertyAndValue(
                    xSpreadsheet,
                    0,
                    0,
                    "Спецификация № ________________ к договору № _______________________",
                    charSize, null, "CENTER");
            //
            setPropertyAndValue(xSpreadsheet, 0, 1,
                    "от «____» __________ 202__ года", charSize, null, "CENTER");

            setPropertyAndValue(xSpreadsheet, 0, 2,
                    "Поставщик: " + dis.get("adres_otprav"), charSize, null,
                    "LEFT");
            setPropertyAndValue(xSpreadsheet, 0, 3,
                    "Покупатель: " + dis.get("adres_poluch"), charSize, null,
                    "LEFT");
            setPropertyAndValue(xSpreadsheet, 0, 4,
                    "Т.Т.Н. № : " + dis.get("ttn"), charSize, null, "LEFT");
            // ----------------Заполнение тела документа
            int row = 8;
            lineStart = row;
            Map newHash = getHashMapForCpecific(data);
            for (int i = 0; i < newHash.size(); i++) {
                hm = (HashMap) newHash.get(i);
                setPropertyAndValue(xSpreadsheet, 0, row - 1, hm.get("ngpr")
                        .toString(), charSize, aBorder, "LEFT");
                setPropertyAndValue(xSpreadsheet, 1, row - 1, hm.get("nar")
                        .toString(), charSize, aBorder, "LEFT");
                setPropertyAndValue(xSpreadsheet, 2, row - 1, hm.get("fas")
                        .toString(), charSize, aBorder, "LEFT");
                setPropertyAndValue(xSpreadsheet, 3, row - 1,
                        numericToStr(hm.get("sar").toString())
                                .replace(".", ","), charSize, aBorder, "RIGHT");
                setPropertyAndValue(xSpreadsheet, 4, row - 1, "", charSize,
                        aBorder, "LEFT");
                lineStart = row;
                row++;
            }
            row++;
            row++;
            row++;
            row++;
            setPropertyAndValue(
                    xSpreadsheet,
                    0,
                    row,
                    "Поставщик:                                                                                                 Покупатель:_________________________",
                    charSize, null, "LEFT");
            row++;
            setPropertyAndValue(xSpreadsheet, 0, row,
                    "Руководитель: _______________________", charSize, null,
                    "LEFT");
            row++;
            row++;
            row++;
            row++;
            setPropertyAndValue(xSpreadsheet, 0, row, "Виза:", charSize, null,
                    "LEFT");
            row++;
            setPropertyAndValue(xSpreadsheet, 0, row,
                    "Специалист по продажам: ______________", charSize, null,
                    "LEFT");

            // Спецификация 2й лист
            sheet = xSpreadsheets.getByName("Спецификация1");
            xSpreadsheet = (XSpreadsheet) UnoRuntime.queryInterface(
                    XSpreadsheet.class, sheet);
            // ----------------Заполнение шапки документа
            setPropertyAndValue(
                    xSpreadsheet,
                    0,
                    0,
                    "Спецификация № ________________ к договору № _______________________",
                    charSize, null, "CENTER");
            //
            setPropertyAndValue(xSpreadsheet, 0, 1,
                    "от «____» __________ 202__ года", charSize, null, "CENTER");

            setPropertyAndValue(xSpreadsheet, 0, 2,
                    "Поставщик: " + dis.get("adres_otprav"), charSize, null,
                    "LEFT");
            setPropertyAndValue(xSpreadsheet, 0, 3,
                    "Покупатель: " + dis.get("adres_poluch"), charSize, null,
                    "LEFT");
            setPropertyAndValue(xSpreadsheet, 0, 4,
                    "Т.Т.Н. № : " + dis.get("ttn"), charSize, null, "LEFT");
            // ----------------Заполнение тела документа
            row = 8;
            lineStart = row;
            newHash = getHashMapForCpecific(data);
            for (int i = 0; i < newHash.size(); i++) {
                hm = (HashMap) newHash.get(i);
                setPropertyAndValue(xSpreadsheet, 0, row - 1, hm.get("ngpr")
                        .toString(), charSize, aBorder, "LEFT");
                setPropertyAndValue(xSpreadsheet, 1, row - 1, hm.get("nar")
                        .toString(), charSize, aBorder, "LEFT");
                setPropertyAndValue(xSpreadsheet, 2, row - 1, hm.get("fas")
                        .toString(), charSize, aBorder, "LEFT");
                setPropertyAndValue(xSpreadsheet, 3, row - 1,
                        numericToStr(hm.get("sar").toString())
                                .replace(".", ","), charSize, aBorder, "RIGHT");
                setPropertyAndValue(xSpreadsheet, 4, row - 1, "", charSize,
                        aBorder, "LEFT");
                lineStart = row;
                row++;
            }
            row++;
            row++;
            row++;
            row++;
            setPropertyAndValue(
                    xSpreadsheet,
                    0,
                    row,
                    "Поставщик:                                                                                                 Покупатель:_________________________",
                    charSize, null, "LEFT");
            row++;
            setPropertyAndValue(xSpreadsheet, 0, row,
                    "Руководитель: ______________________________", charSize,
                    null, "LEFT");
            row++;
            row++;
            row++;
            row++;
            setPropertyAndValue(xSpreadsheet, 0, row, "Виза:", charSize, null,
                    "LEFT");
            row++;
            setPropertyAndValue(xSpreadsheet, 0, row,
                    "Специалист по продажам: _____________________", charSize,
                    null, "LEFT");

            // Справки для ВЭС 1я таблица
            lineStart = 4;
            sheet = xSpreadsheets.getByName("Справки для ВЭС");
            xSpreadsheet = (XSpreadsheet) UnoRuntime.queryInterface(
                    XSpreadsheet.class, sheet);
            dis = (HashMap) discr.get(0);
            setPropertyAndValue(xSpreadsheet, 0, 0, "Справки для ВЭС",
                    charSize, null, "CENTER");
            setPropertyAndValue(xSpreadsheet, 0, 1,
                    "Отгрузочная спецификация к накладной № "
                            + dis.get("ttn").toString() + " (В РУБЛЯХ РОССИИ)",
                    charSize, null, "CENTER");
            setPropertyAndValue(xSpreadsheet, 0, 2,
                    "Поставщик: " + dis.get("adres_otprav"), charSize, null,
                    "LEFT");
            setPropertyAndValue(xSpreadsheet, 0, 3,
                    "Получатель: " + dis.get("adres_poluch"), charSize, null,
                    "LEFT");
            // Таблица
            setPropertyAndValue(xSpreadsheet, 0, lineStart, "ТН ВЭД СНГ",
                    charSize, aBorder, "LEFT");
            setPropertyAndValue(xSpreadsheet, 1, lineStart, "Наименование",
                    charSize, aBorder, "LEFT");
            setPropertyAndValue(xSpreadsheet, 2, lineStart, "Кол-во", charSize,
                    aBorder, "LEFT");
            setPropertyAndValue(xSpreadsheet, 3, lineStart, "Единицы",
                    charSize, aBorder, "LEFT");
            setPropertyAndValue(xSpreadsheet, 4, lineStart, "Сумма", charSize,
                    aBorder, "LEFT");
            setPropertyAndValue(xSpreadsheet, 5, lineStart, "Вес (кг.)",
                    charSize, aBorder, "LEFT");
            sdb = new SkladDB();
            lineStart++;
            summar = 0;
            mass = 0;
            hm = (HashMap) data.get(0);
            if (!hm.get("sar").toString().startsWith("47")) {
                newHash = sdb.getRazrezTNVED(dis.get("ttn").toString());
            } else {
                newHash = sdb.getRazrezTNVEDPolotno(dis.get("ttn").toString());
            }
            for (int i = 0; i < newHash.size(); i++) {
                HashMap bufHash = new HashMap();
                bufHash = (HashMap) newHash.get(i);
                setPropertyAndValue(xSpreadsheet, 0, lineStart,
                        bufHash.get("narp").toString(), charSize, aBorder,
                        "LEFT");
                setPropertyAndValue(xSpreadsheet, 1, lineStart,
                        bufHash.get("ngpr").toString(), charSize, aBorder,
                        "LEFT");
                setPropertyAndValue(xSpreadsheet, 2, lineStart,
                        bufHash.get("pach").toString(), charSize, aBorder,
                        "LEFT");
                setPropertyAndValue(xSpreadsheet, 3, lineStart,
                        bufHash.get("kol").toString(), charSize, aBorder,
                        "LEFT");
                setPropertyAndValue(
                        xSpreadsheet,
                        4,
                        lineStart,
                        numericToStr(bufHash.get("itogo").toString()).replace(
                                ".", ","), charSize, aBorder, "RIGHT");
                summar = summar
                        + Double.valueOf(bufHash.get("itogo").toString());
                n = new BigDecimal(bufHash.get("massa").toString());
                n = n.setScale(2, BigDecimal.ROUND_HALF_UP);
                setPropertyAndValue(xSpreadsheet, 5, lineStart,
                        numericToStr(n.toString()).replace(".", ","), charSize,
                        aBorder, "RIGHT");
                mass = mass + Double.valueOf(bufHash.get("massa").toString());
                lineStart++;
            }
            setPropertyAndValue(xSpreadsheet, 1, lineStart, "ИТОГО:", charSize,
                    null, "LEFT");
            setPropertyAndValue(xSpreadsheet, 2, lineStart, "=SUM(C5:C"
                            + String.valueOf(lineStart) + ")", charSize, aBorder,
                    "LEFT");
            setPropertyAndValue(xSpreadsheet, 3, lineStart, "=SUM(D5:D"
                            + String.valueOf(lineStart) + ")", charSize, aBorder,
                    "LEFT");

            n = new BigDecimal(summar);
            n = n.setScale(4, BigDecimal.ROUND_HALF_UP);
            n = n.setScale(3, BigDecimal.ROUND_HALF_UP);
            n = n.setScale(2, BigDecimal.ROUND_HALF_UP);
            summar = n.doubleValue();
            setPropertyAndValue(xSpreadsheet, 4, lineStart,
                    numericToStr(summar).replace(".", ","), charSize, aBorder,
                    "RIGHT");
            n = new BigDecimal(mass);
            n = n.setScale(4, BigDecimal.ROUND_HALF_UP);
            n = n.setScale(3, BigDecimal.ROUND_HALF_UP);
            n = n.setScale(2, BigDecimal.ROUND_HALF_UP);
            mass = n.doubleValue();
            setPropertyAndValue(xSpreadsheet, 5, lineStart, numericToStr(mass)
                    .replace(".", ","), charSize, aBorder, "RIGHT");

            // Справка для ВЭС-2я Таблица
            lineStart++;
            lineStart++;
            mass = 0;
            summar = 0;
            summa_ndsr = 0;
            summa_allr = 0;
            setPropertyAndValue(xSpreadsheet, 0, lineStart, "Наименование",
                    charSize, aBorder, "LEFT");
            setPropertyAndValue(xSpreadsheet, 1, lineStart, "Модель", charSize,
                    aBorder, "LEFT");
            setPropertyAndValue(xSpreadsheet, 2, lineStart, "Кол-во", charSize,
                    aBorder, "LEFT");
            setPropertyAndValue(xSpreadsheet, 3, lineStart, "Вес (кг.)",
                    charSize, aBorder, "LEFT");
            setPropertyAndValue(xSpreadsheet, 4, lineStart, "Единицы",
                    charSize, aBorder, "LEFT");
            setPropertyAndValue(xSpreadsheet, 5, lineStart, "Сумма без НДС",
                    charSize, aBorder, "LEFT");
            setPropertyAndValue(xSpreadsheet, 6, lineStart, "Сумма НДС",
                    charSize, aBorder, "LEFT");
            lineStart++;
            int lineEnd = lineStart;

            hm = (HashMap) data.get(0);
            if (!hm.get("sar").toString().startsWith("47")) {
                newHash = sdb.getRazrezArtALL(dis.get("ttn").toString());
            } else {
                newHash = sdb.getRazrezArtALLPolotno(dis.get("ttn").toString());
            }

            // newHash = sdb.getRazrezArtALL(dis.get("ttn").toString());
            for (int i = 0; i < newHash.size(); i++) {
                HashMap bufHash = new HashMap();
                bufHash = (HashMap) newHash.get(i);
                setPropertyAndValue(xSpreadsheet, 0, lineStart,
                        bufHash.get("ngpr").toString(), charSize, aBorder,
                        "LEFT");
                setPropertyAndValue(xSpreadsheet, 1, lineStart,
                        bufHash.get("fas").toString(), charSize, aBorder,
                        "LEFT");
                setPropertyAndValue(xSpreadsheet, 2, lineStart,
                        bufHash.get("pach").toString(), charSize, aBorder,
                        "LEFT");
                n = new BigDecimal(bufHash.get("massa").toString());
                n = n.setScale(2, BigDecimal.ROUND_HALF_UP);
                setPropertyAndValue(xSpreadsheet, 3, lineStart,
                        numericToStr(n.toString()).replace(".", ","), charSize,
                        aBorder, "RIGHT");
                mass = mass + Double.valueOf(bufHash.get("massa").toString());
                setPropertyAndValue(xSpreadsheet, 4, lineStart,
                        bufHash.get("kol").toString(), charSize, aBorder,
                        "LEFT");
                setPropertyAndValue(
                        xSpreadsheet,
                        5,
                        lineStart,
                        numericToStr(bufHash.get("summa").toString()).replace(
                                ".", ","), charSize, aBorder, "RIGHT");
                summar = summar
                        + Double.valueOf(bufHash.get("summa").toString());
                setPropertyAndValue(xSpreadsheet, 6, lineStart,
                        numericToStr(bufHash.get("summa_nds").toString())
                                .replace(".", ","), charSize, aBorder, "RIGHT");
                summa_ndsr = summa_ndsr
                        + Double.valueOf(bufHash.get("summa_nds").toString());
                lineStart++;
            }
            setPropertyAndValue(xSpreadsheet, 1, lineStart, "ИТОГО:", charSize,
                    null, "RIGHT");
            setPropertyAndValue(
                    xSpreadsheet,
                    2,
                    lineStart,
                    "=SUM(C" + String.valueOf(lineEnd) + ":C"
                            + String.valueOf(lineStart) + ")", charSize,
                    aBorder, "LEFT");
            n = new BigDecimal(mass);
            n = n.setScale(4, BigDecimal.ROUND_HALF_UP);
            n = n.setScale(3, BigDecimal.ROUND_HALF_UP);
            n = n.setScale(2, BigDecimal.ROUND_HALF_UP);
            mass = n.doubleValue();
            setPropertyAndValue(xSpreadsheet, 3, lineStart, numericToStr(mass)
                    .replace(".", ","), charSize, aBorder, "RIGHT");
            setPropertyAndValue(
                    xSpreadsheet,
                    4,
                    lineStart,
                    "=SUM(E" + String.valueOf(lineEnd) + ":E"
                            + String.valueOf(lineStart) + ")", charSize,
                    aBorder, "RIGHT");
            n = new BigDecimal(summar);
            n = n.setScale(4, BigDecimal.ROUND_HALF_UP);
            n = n.setScale(3, BigDecimal.ROUND_HALF_UP);
            n = n.setScale(2, BigDecimal.ROUND_HALF_UP);
            summar = n.doubleValue();
            setPropertyAndValue(xSpreadsheet, 5, lineStart,
                    numericToStr(summar).replace(".", ","), charSize, aBorder,
                    "RIGHT");
            n = new BigDecimal(summa_ndsr);
            n = n.setScale(4, BigDecimal.ROUND_HALF_UP);
            n = n.setScale(3, BigDecimal.ROUND_HALF_UP);
            n = n.setScale(2, BigDecimal.ROUND_HALF_UP);
            summa_ndsr = n.doubleValue();
            setPropertyAndValue(xSpreadsheet, 6, lineStart,
                    numericToStr(summa_ndsr).replace(".", ","), charSize,
                    aBorder, "RIGHT");

            // Счет фактура
            summar = 0;
            summa_ndsr = 0;
            summa_allr = 0;
            sheet = xSpreadsheets.getByName("Счет-фактура");
            xSpreadsheet = (XSpreadsheet) UnoRuntime.queryInterface(
                    XSpreadsheet.class, sheet);
            dis = (HashMap) discr.get(0);
            lineStart = 0;
            newHash = sdb.returnSchetFaktura(dis.get("kod_klienta").toString());
            HashMap bufHash = new HashMap();
            bufHash = (HashMap) newHash.get("otprav");
            setPropertyAndValue(xSpreadsheet, 0, lineStart,
                    "Счет-фактура  (к ТТН №)" + dis.get("ttn"), charSize, null,
                    "CENTER");
            lineStart++;
            setPropertyAndValue(xSpreadsheet, 0, lineStart, "Поставщик: "
                            + bufHash.get("post").toString().trim(), charSize, null,
                    "LEFT");
            lineStart++;
            setPropertyAndValue(xSpreadsheet, 0, lineStart, "Адрес: "
                            + bufHash.get("adres").toString().trim(), charSize, null,
                    "LEFT");
            lineStart++;
            setPropertyAndValue(xSpreadsheet, 0, lineStart, bufHash.get("rs")
                    .toString().trim(), charSize, null, "LEFT");
            lineStart++;
            setPropertyAndValue(xSpreadsheet, 0, lineStart, "Банк: "
                            + bufHash.get("banknaim").toString().trim(), charSize,
                    null, "LEFT");
            lineStart++;
            setPropertyAndValue(xSpreadsheet, 0, lineStart,
                    bufHash.get("bankadr").toString().trim(), charSize, null,
                    "LEFT");
            lineStart++;
            setPropertyAndValue(xSpreadsheet, 0, lineStart, bufHash.get("ks")
                            .toString().trim()
                            + " " + bufHash.get("inn").toString().trim(), charSize,
                    null, "LEFT");
            bufHash = new HashMap();
            bufHash = (HashMap) newHash.get("poluch");
            lineStart++;
            setPropertyAndValue(
                    xSpreadsheet,
                    0,
                    lineStart,
                    "БАНК : СБ РФ г.Москва БИК 044525225 к/с 30101810400000000225 в ОПЕРУ ГУ ЦБ РФ по г.Москва",
                    charSize, null, "LEFT");
            lineStart++;
            setPropertyAndValue(xSpreadsheet, 0, lineStart, "Покупатель: "
                            + bufHash.get("post").toString().trim(), charSize, null,
                    "LEFT");
            lineStart++;
            setPropertyAndValue(xSpreadsheet, 0, lineStart, "Адрес: "
                            + bufHash.get("adres").toString().trim(), charSize, null,
                    "LEFT");
            lineStart++;
            setPropertyAndValue(xSpreadsheet, 0, lineStart, bufHash.get("rs")
                    .toString().trim(), charSize, null, "LEFT");
            lineStart++;
            setPropertyAndValue(xSpreadsheet, 0, lineStart, "Банк: "
                            + bufHash.get("banknaim").toString().trim(), charSize,
                    null, "LEFT");
            lineStart++;
            setPropertyAndValue(xSpreadsheet, 0, lineStart,
                    bufHash.get("bankadr").toString().trim(), charSize, null,
                    "LEFT");
            lineStart++;
            setPropertyAndValue(xSpreadsheet, 0, lineStart, bufHash.get("ks")
                            .toString().trim()
                            + " " + bufHash.get("inn").toString().trim(), charSize,
                    null, "LEFT");
            HashMap hmm = new HashMap();
            hmm = (HashMap) data.get(0);

            hm = (HashMap) data.get(0);
            if (!hm.get("sar").toString().startsWith("47")) {
                newHash = sdb.getDataForFactura(dis.get("ttn").toString());
            } else {
                newHash = sdb.getDataForFacturaPolotno(dis.get("ttn")
                        .toString());
            }

            // newHash = sdb.getDataForFactura(dis.get("ttn").toString());
            row = 15;
            for (int i = 0; i < newHash.size(); i++) {
                hm = (HashMap) newHash.get(i);
                setPropertyAndValue(xSpreadsheet, 0, row, hm.get("ngpr")
                        .toString(), charSize, aBorder, "LEFT");
                setPropertyAndValue(xSpreadsheet, 1, row, hm.get("nar")
                        .toString(), charSize, aBorder, "LEFT");
                setPropertyAndValue(xSpreadsheet, 3, row, hmm.get("izm")
                        .toString(), charSize, aBorder, "LEFT");
                setPropertyAndValue(xSpreadsheet, 2, row, hm.get("pach")
                        .toString(), charSize, aBorder, "LEFT");
                setPropertyAndValue(xSpreadsheet, 4, row, hm.get("kol")
                        .toString(), charSize, aBorder, "LEFT");
                setPropertyAndValue(
                        xSpreadsheet,
                        5,
                        row,
                        numericToStr(hm.get("cenav").toString()).replace(".",
                                ","), charSize, aBorder, "RIGHT");
                setPropertyAndValue(
                        xSpreadsheet,
                        6,
                        row,
                        numericToStr(hm.get("summa").toString()).replace(".",
                                ","), charSize, aBorder, "RIGHT");
                summar = summar
                        + Double.valueOf(numericToStr(hm.get("summa")
                        .toString()));

                setPropertyAndValue(xSpreadsheet, 7, row, hm.get("nds")
                        .toString(), charSize, aBorder, "LEFT");
                setPropertyAndValue(
                        xSpreadsheet,
                        8,
                        row,
                        numericToStr(hm.get("summa_nds").toString()).replace(
                                ".", ","), charSize, aBorder, "RIGHT");
                summa_ndsr = summa_ndsr
                        + Double.valueOf(numericToStr(hm.get("summa_nds")
                        .toString()));
                setPropertyAndValue(
                        xSpreadsheet,
                        9,
                        row,
                        numericToStr(hm.get("summa_all").toString()).replace(
                                ".", ","), charSize, aBorder, "RIGHT");
                summa_allr = summa_allr
                        + Double.valueOf(numericToStr(hm.get("summa_all")
                        .toString()));
                setPropertyAndValue(xSpreadsheet, 10, row, hm.get("narp")
                        .toString(), charSize, aBorder, "LEFT");
                row++;
            }
            lineStart = row++;
            setPropertyAndValue(xSpreadsheet, 0, lineStart, "Всего к оплате: ",
                    charSize, aBorder, "LEFT");
            setPropertyAndValue(
                    xSpreadsheet,
                    2,
                    lineStart,
                    "=SUM(C" + String.valueOf(15) + ":C"
                            + String.valueOf(lineStart) + ")", charSize,
                    aBorder, "LEFT");
            setPropertyAndValue(
                    xSpreadsheet,
                    4,
                    lineStart,
                    "=SUM(E" + String.valueOf(15) + ":E"
                            + String.valueOf(lineStart) + ")", charSize,
                    aBorder, "LEFT");
            setPropertyAndValue(
                    xSpreadsheet,
                    5,
                    lineStart,
                    "=SUM(F" + String.valueOf(15) + ":F"
                            + String.valueOf(lineStart) + ")", charSize,
                    aBorder, "LEFT");
            n = new BigDecimal(summar);
            n = n.setScale(4, BigDecimal.ROUND_HALF_UP);
            n = n.setScale(3, BigDecimal.ROUND_HALF_UP);
            n = n.setScale(2, BigDecimal.ROUND_HALF_UP);
            summar = n.doubleValue();
            setPropertyAndValue(xSpreadsheet, 6, lineStart,
                    numericToStr(summar).replace(".", ","), charSize, aBorder,
                    "RIGHT");
            setPropertyAndValue(xSpreadsheet, 7, lineStart, "", charSize,
                    aBorder, "LEFT");
            n = new BigDecimal(summa_ndsr);
            n = n.setScale(4, BigDecimal.ROUND_HALF_UP);
            n = n.setScale(3, BigDecimal.ROUND_HALF_UP);
            n = n.setScale(2, BigDecimal.ROUND_HALF_UP);
            summa_ndsr = n.doubleValue();
            setPropertyAndValue(xSpreadsheet, 8, lineStart,
                    numericToStr(summa_ndsr).replace(".", ","), charSize,
                    aBorder, "RIGHT");
            n = new BigDecimal(summa_allr);
            n = n.setScale(4, BigDecimal.ROUND_HALF_UP);
            n = n.setScale(3, BigDecimal.ROUND_HALF_UP);
            n = n.setScale(2, BigDecimal.ROUND_HALF_UP);
            summa_allr = n.doubleValue();
            setPropertyAndValue(xSpreadsheet, 9, lineStart,
                    numericToStr(summa_allr).replace(".", ","), charSize,
                    aBorder, "RIGHT");
            setPropertyAndValue(xSpreadsheet, 10, lineStart, "", charSize,
                    aBorder, "LEFT");
            setPropertyAndValue(xSpreadsheet, 1, lineStart, "", charSize,
                    aBorder, "LEFT");
            setPropertyAndValue(xSpreadsheet, 3, lineStart, "", charSize,
                    aBorder, "LEFT");
            lineStart++;
            setPropertyAndValue(xSpreadsheet, 0, lineStart, "Вес: "
                            + numericToStr(mass).replace(".", ",") + "кг.", charSize,
                    null, "LEFT");
            lineStart++;
            setPropertyAndValue(xSpreadsheet, 0, lineStart,
                    "Страна происхождения - Беларусь", charSize, null, "LEFT");
            lineStart++;
            setPropertyAndValue(
                    xSpreadsheet,
                    0,
                    lineStart,
                    "Руководитель: __________________                          Гл. бухгалтер: __________________",
                    charSize, null, "LEFT");
            lineStart++;
            setPropertyAndValue(
                    xSpreadsheet,
                    0,
                    lineStart,
                    "                                                         М.П.",
                    charSize, null, "LEFT");
            lineStart++;
            setPropertyAndValue(
                    xSpreadsheet,
                    0,
                    lineStart,
                    "Получил: _______________________                          Выдал: __________________________",
                    charSize, null, "LEFT");

            // Протокол согласования
            sheet = xSpreadsheets.getByName("Протокол");
            xSpreadsheet = (XSpreadsheet) UnoRuntime.queryInterface(
                    XSpreadsheet.class, sheet);
            setPropertyAndValue(xSpreadsheet, 0, 2,
                    "Поставщик: " + dis.get("adres_otprav"), charSize, null,
                    "LEFT");
            setPropertyAndValue(xSpreadsheet, 0, 3,
                    "Получатель: " + dis.get("adres_poluch"), charSize, null,
                    "LEFT");
            setPropertyAndValue(xSpreadsheet, 0, 4, "ТТН №: "
                    + dis.get("ttn").toString(), charSize, null, "LEFT");
            charSize = 10;
            newHash = sdb.getProtSogl(dis.get("ttn").toString(),
                    Float.valueOf(dis.get("kurs_nbrb").toString()),
                    Float.valueOf(dis.get("nds").toString()));
            row = 8;
            for (int i = 0; i < newHash.size(); i++) {
                hm = (HashMap) newHash.get(i);
                setPropertyAndValue(xSpreadsheet, 0, row, hm.get("ngpr")
                        .toString(), charSize, aBorder, "LEFT");
                setPropertyAndValue(xSpreadsheet, 1, row, hm.get("nar")
                        .toString(), charSize, aBorder, "LEFT");
                setPropertyAndValue(xSpreadsheet, 2, row, hm.get("fas")
                        .toString(), charSize, aBorder, "LEFT");
                setPropertyAndValue(xSpreadsheet, 3, row, hm.get("bc")
                        .toString(), charSize, aBorder, "LEFT");
                setPropertyAndValue(
                        xSpreadsheet,
                        4,
                        row,
                        numericToStr(hm.get("rc").toString()).replace(".", ","),
                        charSize, aBorder, "LEFT");
                setPropertyAndValue(xSpreadsheet, 5, row, "", charSize,
                        aBorder, "LEFT");
                row++;
            }
            row++;
            setPropertyAndValue(
                    xSpreadsheet,
                    0,
                    row,
                    "Поставщик: _______________________                          Покупатель: __________________________",
                    charSize, null, "LEFT");

        } catch (Exception e) {
            System.err
                    .println("Error in getCertificateForTTN in class SkladOO. \n Please report it to the developer \n"
                            + e);
            log.error("Error in getCertificateForTTN in class SkladOO.", e);
            JOptionPane
                    .showMessageDialog(
                            null,
                            "Error in method getCertificateForTTN in class SkladOO. \n Please report it to the developer \n");
        }
    }

    public Map getHashMapForCpecific(List hhm) {
        Map buffer, buffer1, startHashMap, resultHashMap;
        int kolvoResult = 0;
        boolean flag = true;
        buffer = new HashMap();
        startHashMap = new HashMap();
        resultHashMap = new HashMap();
        try {
            for (int i = 0; i < hhm.size(); i++) {
                buffer = new HashMap();
                buffer1 = new HashMap();
                buffer = (HashMap) data.get(i);
                buffer1.put("ngpr", buffer.get("ngpr"));
                buffer1.put("sar", buffer.get("cena_rus"));
                buffer1.put("nar", buffer.get("nar"));
                buffer1.put("fas", buffer.get("fas"));
                startHashMap.put(i, buffer1);
            }
            buffer = new HashMap();
            buffer = (HashMap) startHashMap.get(0);
            resultHashMap.put(0, buffer);
            for (int i = 0; i < startHashMap.size(); i++) {
                buffer = (HashMap) startHashMap.get(i);
                for (int j = 0; j < resultHashMap.size(); j++) {
                    buffer1 = new HashMap();
                    buffer1 = (HashMap) resultHashMap.get(j);
                    if (buffer.get("sar").toString().trim()
                            .equals(buffer1.get("sar").toString().trim())
                            && buffer
                            .get("nar")
                            .toString()
                            .trim()
                            .equals(buffer1.get("nar").toString()
                                    .trim())
                            && buffer
                            .get("fas")
                            .toString()
                            .trim()
                            .equals(buffer1.get("fas").toString()
                                    .trim())
                            && buffer
                            .get("ngpr")
                            .toString()
                            .trim()
                            .equals(buffer1.get("ngpr").toString()
                                    .trim())) {
                        flag = false;
                    }
                }
                if (flag) {
                    kolvoResult++;
                    resultHashMap.put(kolvoResult, buffer);
                } else {
                    flag = true;
                }
            }
        } catch (Exception e) {
            System.err
                    .println("Error in getHashMapForCpecific in class SkladOO. \n Please report it to the developer \n"
                            + e);
            log.error("Error in getHashMapForCpecific in class SkladOO.", e);
            JOptionPane
                    .showMessageDialog(
                            null,
                            "Error in method getHashMapForCpecific in class SkladOO. \n Please report it to the developer \n");
        }
        return resultHashMap;
    }

    /*
     * Формирует отчет за заданную дату с актуальными (на дату формирования)
     * спецценами.
     */
    public void reportForOtgruz(String nameTamplates) {
        try {
            connect();
            XComponent currentDocument = openDocumentOld("Templates/"
                    + nameTamplates);
            XSpreadsheetDocument xSpreadsheetDocument = (XSpreadsheetDocument) UnoRuntime
                    .queryInterface(XSpreadsheetDocument.class, currentDocument);
            XSpreadsheets xSpreadsheets = xSpreadsheetDocument.getSheets();
            // xSpreadsheets.insertNewByName("Актуальные спеццены", (short) 0);
            Object sheet = xSpreadsheets.getByName("Лист1");
            XSpreadsheet xSpreadsheet = (XSpreadsheet) UnoRuntime
                    .queryInterface(XSpreadsheet.class, sheet);
            XNamed xNamed = (XNamed) UnoRuntime.queryInterface(XNamed.class,
                    xSpreadsheet);
            xNamed.setName("Отчет");
            HashMap hm;
            float charSize = 10;
            com.sun.star.table.BorderLine aLine = new com.sun.star.table.BorderLine();
            aLine.Color = 0x000000;
            aLine.InnerLineWidth = 0;
            aLine.LineDistance = 0;
            aLine.OuterLineWidth = 25;
            com.sun.star.table.TableBorder aBorder = new com.sun.star.table.TableBorder();
            aBorder.BottomLine = aLine;
            aBorder.LeftLine = aLine;
            aBorder.TopLine = aLine;
            aBorder.RightLine = aLine;
            aBorder.IsBottomLineValid = true;
            aBorder.IsLeftLineValid = true;
            aBorder.IsRightLineValid = true;
            aBorder.IsTopLineValid = true;
            int lineStart = 1;
            for (int i = 0; i < data.size(); i++) {
                hm = (HashMap) data.get(i);
                setPropertyAndValue(xSpreadsheet, 0, lineStart,
                        "'" + hm.get("name").toString(), charSize, aBorder,
                        "LEFT");
                setPropertyAndValue(xSpreadsheet, 1, lineStart,
                        "'" + hm.get("data").toString(), charSize, aBorder,
                        "LEFT");
                setPropertyAndValue(xSpreadsheet, 2, lineStart,
                        "'" + hm.get("ndoc").toString(), charSize, aBorder,
                        "LEFT");
                setPropertyAndValue(xSpreadsheet, 3, lineStart,
                        "'" + hm.get("summa").toString(), charSize, aBorder,
                        "LEFT");
                setPropertyAndValue(xSpreadsheet, 4, lineStart,
                        "'" + hm.get("summa_nds").toString(), charSize,
                        aBorder, "LEFT");
                setPropertyAndValue(xSpreadsheet, 5, lineStart,
                        "'" + hm.get("summa_all").toString(), charSize,
                        aBorder, "LEFT");
                setPropertyAndValue(xSpreadsheet, 6, lineStart,
                        "'" + hm.get("summav").toString(), charSize, aBorder,
                        "LEFT");
                setPropertyAndValue(xSpreadsheet, 7, lineStart,
                        "'" + hm.get("summa_ndsv").toString(), charSize,
                        aBorder, "LEFT");
                setPropertyAndValue(xSpreadsheet, 8, lineStart,
                        "'" + hm.get("summa_allv").toString(), charSize,
                        aBorder, "LEFT");
                setPropertyAndValue(xSpreadsheet, 9, lineStart,
                        "'" + hm.get("operac").toString(), charSize, aBorder,
                        "LEFT");
                setPropertyAndValue(xSpreadsheet, 10, lineStart,
                        "'" + hm.get("kol").toString(), charSize, aBorder,
                        "LEFT");
                lineStart++;
            }
        } catch (Exception ex) {
            StackTraceElement[] stak = ex.getStackTrace();
            log.error("\nException in class " + stak[0].getClassName()
                    + ",\nin method " + stak[0].getMethodName()
                    + ", in string number " + stak[0].getLineNumber() + ".\n"
                    + "TEXT MESSAGE: \" " + ex.getMessage() + " \"");
        }
    }

    /*
     * Формирует отчет за заданную дату с актуальными (на дату формирования)
     * спецценами.
     */
    public void reportSpecCena(XComponent currentDocument) {
        try {
            HashMap hm;
            XSpreadsheetDocument xSpreadsheetDocument = (XSpreadsheetDocument) UnoRuntime
                    .queryInterface(XSpreadsheetDocument.class, currentDocument);
            XSpreadsheets xSpreadsheets = xSpreadsheetDocument.getSheets();
            // xSpreadsheets.insertNewByName("Актуальные спеццены", (short) 0);
            Object sheet = xSpreadsheets.getByName("Лист1");
            XSpreadsheet xSpreadsheet = (XSpreadsheet) UnoRuntime
                    .queryInterface(XSpreadsheet.class, sheet);
            float charSize = 10;
            com.sun.star.table.BorderLine aLine = new com.sun.star.table.BorderLine();
            aLine.Color = 0x000000;
            aLine.InnerLineWidth = 0;
            aLine.LineDistance = 0;
            aLine.OuterLineWidth = 25;
            com.sun.star.table.TableBorder aBorder = new com.sun.star.table.TableBorder();
            aBorder.BottomLine = aLine;
            aBorder.LeftLine = aLine;
            aBorder.TopLine = aLine;
            aBorder.RightLine = aLine;
            aBorder.IsBottomLineValid = true;
            aBorder.IsLeftLineValid = true;
            aBorder.IsRightLineValid = true;
            aBorder.IsTopLineValid = true;
            int lineStart = 1;
            for (int i = 0; i < data.size(); i++) {
                hm = new HashMap();
                hm = (HashMap) data.get(i);
                setPropertyAndValue(xSpreadsheet, 0, lineStart,
                        "'" + hm.get("skod_kontragenta").toString(), charSize,
                        aBorder, "LEFT");
                setPropertyAndValue(xSpreadsheet, 1, lineStart,
                        "'" + hm.get("smodel").toString(), charSize, aBorder,
                        "LEFT");
                setPropertyAndValue(xSpreadsheet, 2, lineStart,
                        "'" + hm.get("sart").toString(), charSize, aBorder,
                        "LEFT");
                setPropertyAndValue(xSpreadsheet, 3, lineStart,
                        "'" + hm.get("ssort").toString(), charSize, aBorder,
                        "LEFT");
                setPropertyAndValue(
                        xSpreadsheet,
                        4,
                        lineStart,
                        "'" + hm.get("srazmer").toString() + "-"
                                + hm.get("srazmer_end").toString(), charSize,
                        aBorder, "LEFT");
                setPropertyAndValue(
                        xSpreadsheet,
                        5,
                        lineStart,
                        "'" + hm.get("srost").toString() + "-"
                                + hm.get("srost_end").toString(), charSize,
                        aBorder, "LEFT");
                setPropertyAndValue(
                        xSpreadsheet,
                        6,
                        lineStart,
                        numericToStr(hm.get("scena").toString()).replace(".",
                                ","), charSize, aBorder, "RIGHT");
                setPropertyAndValue(xSpreadsheet, 7, lineStart,
                        "'" + hm.get("date_insert_record"), charSize, aBorder,
                        "RIGHT");
                lineStart++;
            }
        } catch (Exception e) {
            System.err
                    .println("Error in reportSpecCena in class SkladOO. \n Please report it to the developer \n"
                            + e);
            log.error("Error in reportSpecCena in class SkladOO.", e);
            JOptionPane
                    .showMessageDialog(
                            null,
                            "Error in method reportSpecCena in class SkladOO. \n Please report it to the developer \n");
        }
    }

    public void svodnayaVedomostPoFirmMag(XComponent currentDocument) {
        try {
            HashMap hm;
            XSpreadsheetDocument xSpreadsheetDocument = (XSpreadsheetDocument) UnoRuntime
                    .queryInterface(XSpreadsheetDocument.class, currentDocument);
            XSpreadsheets xSpreadsheets = xSpreadsheetDocument.getSheets();
            // xSpreadsheets.insertNewByName("Актуальные спеццены", (short) 0);
            Object sheet = xSpreadsheets.getByName("Лист1");
            XSpreadsheet xSpreadsheet = (XSpreadsheet) UnoRuntime
                    .queryInterface(XSpreadsheet.class, sheet);
            float charSize = 10;
            com.sun.star.table.BorderLine aLine = new com.sun.star.table.BorderLine();
            aLine.Color = 0x000000;
            aLine.InnerLineWidth = 0;
            aLine.LineDistance = 0;
            aLine.OuterLineWidth = 25;
            com.sun.star.table.TableBorder aBorder = new com.sun.star.table.TableBorder();
            aBorder.BottomLine = aLine;
            aBorder.LeftLine = aLine;
            aBorder.TopLine = aLine;
            aBorder.RightLine = aLine;
            aBorder.IsBottomLineValid = true;
            aBorder.IsLeftLineValid = true;
            aBorder.IsRightLineValid = true;
            aBorder.IsTopLineValid = true;
            hm = new HashMap();
            hm = (HashMap) discr.get(0);
            setPropertyAndValue(xSpreadsheet, 0, 0, hm.get("key").toString(),
                    11, aBorder, "CENTER");
            setPropertyAndValue(xSpreadsheet, 0, 1, "Накладная", charSize,
                    aBorder, "CENTER");
            setPropertyAndValue(xSpreadsheet, 1, 1, "Приложение", charSize,
                    aBorder, "CENTER");
            setPropertyAndValue(xSpreadsheet, 2, 1, "Количество", charSize,
                    aBorder, "CENTER");
            setPropertyAndValue(xSpreadsheet, 3, 1, "Сум_Опт", charSize,
                    aBorder, "CENTER");
            setPropertyAndValue(xSpreadsheet, 4, 1, "НДС", charSize, aBorder,
                    "CENTER");
            setPropertyAndValue(xSpreadsheet, 5, 1, "Сум_ТН", charSize,
                    aBorder, "CENTER");
            setPropertyAndValue(xSpreadsheet, 6, 1, "НДС_ТН", charSize,
                    aBorder, "CENTER");
            setPropertyAndValue(xSpreadsheet, 7, 1, "ВсегоНДС", charSize,
                    aBorder, "CENTER");
            setPropertyAndValue(xSpreadsheet, 8, 1, "Розн_ДЕТ", charSize,
                    aBorder, "CENTER");
            setPropertyAndValue(xSpreadsheet, 9, 1, "Розн_ВЗР", charSize,
                    aBorder, "CENTER");
            int lineStart = 2;
            for (int i = 0; i < data.size(); i++) {
                hm = new HashMap();
                hm = (HashMap) data.get(i);
                setPropertyAndValue(xSpreadsheet, 0, lineStart,
                        "'" + hm.get("ndoc").toString(), charSize, aBorder,
                        "LEFT");
                setPropertyAndValue(xSpreadsheet, 1, lineStart,
                        "'" + hm.get("put_list").toString(), charSize, aBorder,
                        "LEFT");
                setPropertyAndValue(xSpreadsheet, 2, lineStart, hm.get("kol")
                        .toString(), charSize, aBorder, "RIGHT");
                setPropertyAndValue(xSpreadsheet, 3, lineStart, hm.get("summa")
                        .toString(), charSize, aBorder, "RIGHT");
                setPropertyAndValue(xSpreadsheet, 4, lineStart,
                        hm.get("summa_nds").toString(), charSize, aBorder,
                        "RIGHT");
                setPropertyAndValue(xSpreadsheet, 5, lineStart,
                        hm.get("sum_torg_nad").toString(), charSize, aBorder,
                        "RIGHT");
                setPropertyAndValue(xSpreadsheet, 6, lineStart, hm
                        .get("nds_tn").toString(), charSize, aBorder, "RIGHT");
                setPropertyAndValue(xSpreadsheet, 7, lineStart,
                        hm.get("all_nds").toString(), charSize, aBorder,
                        "RIGHT");
                setPropertyAndValue(xSpreadsheet, 8, lineStart,
                        hm.get("rozn_sum_kid").toString(), charSize, aBorder,
                        "RIGHT");
                setPropertyAndValue(xSpreadsheet, 9, lineStart,
                        hm.get("rozn_sum_adult").toString(), charSize, aBorder,
                        "RIGHT");
                lineStart++;
            }
            setPropertyAndValue(xSpreadsheet, 0, lineStart, "", charSize,
                    aBorder, "LEFT");
            setPropertyAndValue(xSpreadsheet, 1, lineStart, "ИТОГО:", charSize,
                    aBorder, "LEFT");
            setPropertyAndValue(xSpreadsheet, 2, lineStart, "=SUM(C1:C"
                            + String.valueOf(lineStart) + ")", charSize, aBorder,
                    "RIGHT");
            setPropertyAndValue(xSpreadsheet, 3, lineStart, "=SUM(D1:D"
                            + String.valueOf(lineStart) + ")", charSize, aBorder,
                    "RIGHT");
            setPropertyAndValue(xSpreadsheet, 4, lineStart, "=SUM(E1:E"
                            + String.valueOf(lineStart) + ")", charSize, aBorder,
                    "RIGHT");
            setPropertyAndValue(xSpreadsheet, 5, lineStart, "=SUM(F1:F"
                            + String.valueOf(lineStart) + ")", charSize, aBorder,
                    "RIGHT");
            setPropertyAndValue(xSpreadsheet, 6, lineStart, "=SUM(G1:G"
                            + String.valueOf(lineStart) + ")", charSize, aBorder,
                    "RIGHT");
            setPropertyAndValue(xSpreadsheet, 7, lineStart, "=SUM(H1:H"
                            + String.valueOf(lineStart) + ")", charSize, aBorder,
                    "RIGHT");
            setPropertyAndValue(xSpreadsheet, 8, lineStart, "=SUM(I1:I"
                            + String.valueOf(lineStart) + ")", charSize, aBorder,
                    "RIGHT");
            setPropertyAndValue(xSpreadsheet, 9, lineStart, "=SUM(J1:J"
                            + String.valueOf(lineStart) + ")", charSize, aBorder,
                    "RIGHT");
            lineStart++;
            setPropertyAndValue(
                    xSpreadsheet,
                    9,
                    lineStart,
                    "=SUM(I" + String.valueOf(lineStart) + ":J"
                            + String.valueOf(lineStart) + ")", charSize,
                    aBorder, "RIGHT");
        } catch (Exception e) {
            System.err
                    .println("Error in svodnayaVedomostPoFirmMag in class SkladOO. \n Please report it to the developer \n"
                            + e);
            log.error("Error in svodnayaVedomostPoFirmMag in class SkladOO.", e);
            JOptionPane
                    .showMessageDialog(
                            null,
                            "Error in method svodnayaVedomostPoFirmMag in class SkladOO. \n Please report it to the developer \n");
        }
    }

    /*
     *
     * добавляет нолик в конце числа, число передается в String. Пример: 16.1
     * преобразует в 16.10. !!!!!!!На выходе строка!!!!!
     */
    public String numericToStr(String input) {
        String outStr = "";
        try {
            String[] str = input.replace(".", ",").split(",");
            if (str.length == 2) {
                if (str[1].length() == 1) {
                    outStr = input.trim() + "0";
                } else {
                    outStr = input.trim();
                }
            } else {
                outStr = str[0].trim() + ",00";
            }
        } catch (Exception e) {
            System.err
                    .println("Error in numericToStr(String input) in class SkladOO. \n Please report it to the developer \n"
                            + e);
            log.error("Error in numericToStr(String input) in class SkladOO.",
                    e);
            JOptionPane
                    .showMessageDialog(
                            null,
                            "Error in method numericToStr(String input) in class SkladOO. \n Please report it to the developer \n");
        } finally {
            return outStr;
        }
    }

    /*
     * добавляет нолик в конце числа, число передается в double. Пример: 16.1
     * преобразует в 16.10. !!!!!!!На выходе строка!!!!!
     */
    public String numericToStr(double in) {
        String outStr = "";
        try {
            String input = Double.toString(in);
            String[] str = input.replace(".", ",").split(",");
            if (str.length == 2) {
                if (str[1].length() == 1) {
                    outStr = input.trim() + "0";
                } else {
                    outStr = input.trim();
                }
            } else {
                outStr = str[0].trim() + ",00";
            }
        } catch (Exception e) {
            System.err
                    .println("Error in numericToStr(double in) in class SkladOO. \n Please report it to the developer \n"
                            + e);
            log.error("Error in numericToStr(double in) in class SkladOO.", e);
            JOptionPane
                    .showMessageDialog(
                            null,
                            "Error in method numericToStr(double in) in class SkladOO. \n Please report it to the developer \n");
        } finally {
            return outStr;
        }
    }

    /*
     * Задает свойства ячейке. xSpreadsheet - Лист документа column - Номер
     * столбца для вставки row - Номер строки для вставки value - Что будем
     * записывать в строковом формате charSize - Размер шрифта aBorder -
     * Рисовать границу (параметры переменной должны быть заданны до передачи в
     * эту функцию). Если NULL - то граница не рисуется JustifyHor - Выравнять
     * текст по горизонтали к правой стороне true/false.
     */
    public void setPropertyAndValue(XSpreadsheet xSpreadsheet, int column,
                                    int row, String value, float charSize, TableBorder aBorder,
                                    String JustifyHor) {
        try {
            XCell xCell = null;
            com.sun.star.beans.XPropertySet xPropSet = null;
            xCell = xSpreadsheet.getCellByPosition(column, row);
            xCell.setFormula(value);
            xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime
                    .queryInterface(com.sun.star.beans.XPropertySet.class,
                            xCell);
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
            System.err
                    .println("Error in setPropertyAndValue in class SkladOO. \n Please report it to the developer \n"
                            + ex);
            log.error("Error in setPropertyAndValue in class SkladOO.", ex);
            JOptionPane
                    .showMessageDialog(
                            null,
                            "Error in method setPropertyAndValue in class SkladOO. \n Please report it to the developer \n");
        }
    }

    public long rounding(long d, String sart) {
        if (!sart.startsWith("423") && !sart.startsWith("413")) {
            int ost = (int) (d % 100);
            d /= 100;
            d *= 100;
            if (ost >= 25 && ost < 75) {
                d += 50;
            } else if (ost >= 75) {
                d += 100;
            }
        }
        return d;
    }


    private void protocolEvrotorg(XComponent currentDocument) {
        result = new HashMap();
        HashMap<String, Object> hm = new HashMap();
        HashMap dis = (HashMap) discr.get(0);
        String sar = "";
        double razn = 0, raznBuf = 0, raznBuf1 = 0, roznCena = 0, cena = 0, tn = 0, ndsTN = 0, kol = 0;
        int lineStart = 0;
        float charSize = (float) 6.5;
        BigDecimal n;
        int st, stn;
        DecimalFormat df;
        Double sumtn = .0;
        Double sumtn_nds = .0;
        Double sumtn_rzn = .0;

        Double sum_stoim = .0;
        Double sum_cena = .0;
        Double sum_kol = .0;
        Double sum_nds = .0;
        Double sum_kgm = .0;
        Double sum_massa = .0;

        SkladDB sdb = new SkladDB();

        try {
            XSpreadsheetDocument xSpreadsheetDocument = (XSpreadsheetDocument) UnoRuntime
                    .queryInterface(XSpreadsheetDocument.class, currentDocument);
            XSpreadsheets xSpreadsheets = xSpreadsheetDocument.getSheets();
            // xSpreadsheets.insertNewByName("Приложение к ТТН №" +
            // dis.get("ttn"), (short)0);
            Object sheet = xSpreadsheets.getByName("Лист1");
            XSpreadsheet xSpreadsheet = (XSpreadsheet) UnoRuntime
                    .queryInterface(XSpreadsheet.class, sheet);

            XNamed xNamed = (XNamed) UnoRuntime.queryInterface(XNamed.class,
                    xSpreadsheet);
            xNamed.setName("Приложение к ТТН №" + dis.get("ttn"));
            result.put("ttn", dis.get("ttn"));

            com.sun.star.beans.XPropertySet xPropSet = null;
            com.sun.star.table.BorderLine aLine = new com.sun.star.table.BorderLine();
            aLine.Color = 0x000000;
            aLine.InnerLineWidth = (short) 0;
            aLine.LineDistance = (short) 0;
            aLine.OuterLineWidth = (short) 5;
            com.sun.star.table.TableBorder aBorder = new com.sun.star.table.TableBorder();
            aBorder.BottomLine = aLine;
            aBorder.LeftLine = aLine;
            aBorder.RightLine = aLine;
            aBorder.IsBottomLineValid = true;
            aBorder.IsLeftLineValid = true;
            aBorder.IsRightLineValid = true;

            String COMPANY_NAME = "ОАО \"8 Марта\"";
            String REPUBLIC = "РБ";
            System.out.println("Номер документа :" + dis.get("ttn").toString());
            int d_ = sdb.getDogovorIDTTN(dis.get("ttn").toString());
            DogovorInfo dogov = sdb.getClientDogovor(d_);
            String client = sdb.getClientNameByCode(dis.get("ttn").toString());

            SimpleDateFormat ft = new SimpleDateFormat("dd.MM.yyyy");
            String dateDoc = ft.format(dogov.getDataBegin());
            String dateDocEnd = ft.format(dogov.getDataEnd());

            //String dogovor = "К ТТН № "+ dis.get("ttn").toString().trim()+" от "+dis.get("date").toString();
            String dogovor = "к договору № " + dogov.getNumber().trim() + " от " + dateDoc + " г.";

            String dogovor1 = "действует с " + dateDoc + " по " + dateDocEnd + " гг.";


            XCell xCell = xSpreadsheet.getCellByPosition(16, 1);
            xCell.setFormula(dogov.getNumber().trim() + " от " + dateDoc + " г.");

            xCell = xSpreadsheet.getCellByPosition(7, 3);
            xCell.setFormula("между " + client.trim() + " и " + COMPANY_NAME);

            xCell = xSpreadsheet.getCellByPosition(2, 3);
            xCell.setFormula(dogovor);

            //xCell = xSpreadsheet.getCellByPosition(2, 4);
            //xCell.setFormula(dogovor1);

            // ----------------Заполнение тела документа
            int row = 10;
            lineStart = row;

            for (int i = 0; i < data.size(); i++) {
                hm = (HashMap) data.get(i);

                xCell = xSpreadsheet.getCellByPosition(0, row);
                xCell.setFormula(String.valueOf(i + 1));

                xCell = xSpreadsheet.getCellByPosition(1, row);
                xCell.setFormula(hm.get("ean").toString());

                String nSar = hm.get("ngpr").toString() + "\n"
                        + hm.get("nar").toString() + "\nмодель "
                        + hm.get("fas").toString() + "\nРазмер:" + hm.get("rzm_print").toString().trim();

                xCell = xSpreadsheet.getCellByPosition(2, row);
                xCell.setFormula(nSar);

                XCellRange xCellRange = xSpreadsheet
                        .getCellRangeByPosition(3, row, 4, row);

                com.sun.star.util.XMergeable xMerge = (com.sun.star.util.XMergeable) UnoRuntime
                        .queryInterface(com.sun.star.util.XMergeable.class,
                                xCellRange);
                xMerge.merge(true);

                xCell = xSpreadsheet.getCellByPosition(3, row);
                xCell.setFormula(COMPANY_NAME);

                xCell = xSpreadsheet.getCellByPosition(5, row);
                xCell.setFormula("РБ");

                sar = hm.get("sar").toString();

                xCell = xSpreadsheet.getCellByPosition(6, row);
                if (sar.startsWith("43") || sar.startsWith("48")
                        || sar.startsWith("47")) {
                    xCell.setFormula(hm.get("izm").toString());
                } else {
                    xCell.setFormula("шт.");
                }

                xCell = xSpreadsheet.getCellByPosition(7, row);
                xCell.setFormula("м");

                xCell = xSpreadsheet.getCellByPosition(8, row);
                xCell.setFormula(hm.get("kol").toString());

                xCell = xSpreadsheet.getCellByPosition(9, row);
                xCell.setFormula(hm.get("cenaBS").toString());

                xCell = xSpreadsheet.getCellByPosition(10, row);
                xCell.setFormula(hm.get("skidka").toString());

                double skidka = Double.valueOf(hm.get("skidka").toString());
                double cost = Double.valueOf(hm.get("cena").toString());


                xCell = xSpreadsheet.getCellByPosition(11, row);
                xCell.setFormula(hm.get("cena").toString());

                xCell = xSpreadsheet.getCellByPosition(12, row);
                xCell.setFormula(hm.get("nds").toString());

                double nds = Integer.valueOf(hm.get("nds").toString());

                double cost_with_nds = (cost + (cost * nds / 100));

                n = new BigDecimal(cost_with_nds);
                n = n.setScale(0, BigDecimal.ROUND_HALF_UP);

                xCell = xSpreadsheet.getCellByPosition(13, row);
                xCell.setValue(n.doubleValue());

                xCell = xSpreadsheet.getCellByPosition(14, row);
                xCell.setFormula("-");

                xCell = xSpreadsheet.getCellByPosition(15, row);
                xCell.setFormula("-");

                xCell = xSpreadsheet.getCellByPosition(16, row);
                xCell.setFormula("-");

                xCell = xSpreadsheet.getCellByPosition(17, row);
                xCell.setFormula("-");

                xCell = xSpreadsheet.getCellByPosition(18, row);
                xCell.setFormula(hm.get("massa").toString());
                row++;

                for (int z = 0; z < 19; z++) {
                    xCell = xSpreadsheet.getCellByPosition(z, row - 1);
                    xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime
                            .queryInterface(com.sun.star.beans.XPropertySet.class,
                                    xCell);
                    xPropSet.setPropertyValue("CharHeight", charSize);
                    xPropSet.setPropertyValue("TableBorder", aBorder);
                }

            }

            xCell = xSpreadsheet.getCellByPosition(0, row + 2);
            xCell.setFormula("Покупатель: ");
            xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime
                    .queryInterface(com.sun.star.beans.XPropertySet.class,
                            xCell);
            xPropSet.setPropertyValue("HoriJustify", CellHoriJustify.LEFT);


            xCell = xSpreadsheet.getCellByPosition(0, row + 3);
            xCell.setFormula(client.trim());
            xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime
                    .queryInterface(com.sun.star.beans.XPropertySet.class,
                            xCell);

            XCellRange xCellRange = xSpreadsheet
                    .getCellRangeByPosition(0, row + 5, 6, row + 5);

            com.sun.star.util.XMergeable xMerge = (com.sun.star.util.XMergeable) UnoRuntime
                    .queryInterface(com.sun.star.util.XMergeable.class,
                            xCellRange);
            xMerge.merge(true);
            xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime
                    .queryInterface(com.sun.star.beans.XPropertySet.class,
                            xCell);
            xPropSet.setPropertyValue("HoriJustify", CellHoriJustify.LEFT);

            xCell = xSpreadsheet.getCellByPosition(0, row + 5);
            xCell.setFormula("_________________________/ Н.П. Сачук /");

            xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime
                    .queryInterface(com.sun.star.beans.XPropertySet.class,
                            xCell);
            xPropSet.setPropertyValue("HoriJustify", CellHoriJustify.LEFT);

            xCell = xSpreadsheet.getCellByPosition(9, row + 2);
            xCell.setFormula("Поставщик: ");
            xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime
                    .queryInterface(com.sun.star.beans.XPropertySet.class,
                            xCell);
            xPropSet.setPropertyValue("HoriJustify", CellHoriJustify.LEFT);


            xCell = xSpreadsheet.getCellByPosition(9, row + 3);
            xCell.setFormula(COMPANY_NAME);

            xCellRange = xSpreadsheet
                    .getCellRangeByPosition(9, row + 5, 18, row + 5);

            xMerge = (com.sun.star.util.XMergeable) UnoRuntime
                    .queryInterface(com.sun.star.util.XMergeable.class,
                            xCellRange);
            xMerge.merge(true);
            xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime
                    .queryInterface(com.sun.star.beans.XPropertySet.class,
                            xCell);
            xPropSet.setPropertyValue("HoriJustify", CellHoriJustify.LEFT);


            xCell = xSpreadsheet.getCellByPosition(9, row + 5);
            xCell.setFormula("_________________________/______________/");

            xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime
                    .queryInterface(com.sun.star.beans.XPropertySet.class,
                            xCell);
            xPropSet.setPropertyValue("HoriJustify", CellHoriJustify.LEFT);


            saveAsDocument(currentDocument, savePath
                    + "pcs" + dis.get("ttn").toString() + ".ods", lParams);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            log.error("Ошибка при заполнении протокола согласования цен", e);
            JOptionPane.showMessageDialog(null,
                    "Ошибка при заполнении протокола согласования цен",
                    "Ошибка!!!", JOptionPane.ERROR_MESSAGE);
        } finally {
            sdb.disConn();
        }

    }

    private void protocolProstor(XComponent currentDocument) {
        result = new HashMap();
        HashMap<String, Object> hm = new HashMap();
        HashMap dis = (HashMap) discr.get(0);
        String sar = "";
        double razn = 0, raznBuf = 0, raznBuf1 = 0, roznCena = 0, cena = 0, tn = 0, ndsTN = 0, kol = 0;
        int lineStart = 0;
        float charSize = (float) 6.5;
        BigDecimal n;
        int st, stn;
        DecimalFormat df;
        Double sumtn = .0;
        Double sumtn_nds = .0;
        Double sumtn_rzn = .0;

        Double sum_stoim = .0;
        Double sum_cena = .0;
        Double sum_kol = .0;
        Double sum_nds = .0;
        Double sum_kgm = .0;
        Double sum_massa = .0;

        SkladDB sdb = new SkladDB();

        try {
            XSpreadsheetDocument xSpreadsheetDocument = (XSpreadsheetDocument) UnoRuntime
                    .queryInterface(XSpreadsheetDocument.class, currentDocument);
            XSpreadsheets xSpreadsheets = xSpreadsheetDocument.getSheets();
            // xSpreadsheets.insertNewByName("Приложение к ТТН №" +
            // dis.get("ttn"), (short)0);
            Object sheet = xSpreadsheets.getByName("Sheet1");
            XSpreadsheet xSpreadsheet = (XSpreadsheet) UnoRuntime
                    .queryInterface(XSpreadsheet.class, sheet);

/*            XNamed xNamed = (XNamed) UnoRuntime.queryInterface(XNamed.class,
                    xSpreadsheet);
            xNamed.setName("Приложение к ТТН №" + dis.get("ttn"));*/
            result.put("ttn", dis.get("ttn"));

            com.sun.star.beans.XPropertySet xPropSet = null;
            com.sun.star.table.BorderLine aLine = new com.sun.star.table.BorderLine();
            aLine.Color = 0x000000;
            aLine.InnerLineWidth = (short) 0;
            aLine.LineDistance = (short) 0;
            aLine.OuterLineWidth = (short) 5;
            com.sun.star.table.TableBorder aBorder = new com.sun.star.table.TableBorder();
            aBorder.BottomLine = aLine;
            aBorder.LeftLine = aLine;
            aBorder.RightLine = aLine;
            aBorder.IsBottomLineValid = true;
            aBorder.IsLeftLineValid = true;
            aBorder.IsRightLineValid = true;

            String COMPANY_NAME = "ОАО \"8 Марта\"";
            String REPUBLIC = "РБ";
/*            System.out.println("Номер документа :" + dis.get("ttn").toString());
            int d_ = sdb.getDogovorIDTTN(dis.get("ttn").toString());
            DogovorInfo dogov = sdb.getClientDogovor(d_);
            String client = sdb.getClientNameByCode(dis.get("ttn").toString());

            SimpleDateFormat ft = new SimpleDateFormat("dd.MM.yyyy");
            String dateDoc = ft.format(dogov.getDataBegin());
            String dateDocEnd = ft.format(dogov.getDataEnd());*/

/*
            //String dogovor = "К ТТН № "+ dis.get("ttn").toString().trim()+" от "+dis.get("date").toString();
            String dogovor = "к договору № " + dogov.getNumber().trim() + " от " + dateDoc + " г.";

            String dogovor1 = "действует с " + dateDoc + " по " + dateDocEnd + " гг.";
*/


            XCell xCell = xSpreadsheet.getCellByPosition(16, 1);
/*            xCell.setFormula(dogov.getNumber().trim() + " от " + dateDoc + " г.");

            xCell = xSpreadsheet.getCellByPosition(7, 3);
            xCell.setFormula("между " + client.trim() + " и " + COMPANY_NAME);

            xCell = xSpreadsheet.getCellByPosition(2, 3);
            xCell.setFormula(dogovor);*/

            //xCell = xSpreadsheet.getCellByPosition(2, 4);
            //xCell.setFormula(dogovor1);


            // ----------------Заполнение тела документа
            int row = 6;
            lineStart = row;

            for (int i = 0; i < data.size(); i++) {
                hm = (HashMap) data.get(i);

/*                xCell = xSpreadsheet.getCellByPosition(0, row);
                xCell.setFormula(String.valueOf(i + 1));*/

                xCell = xSpreadsheet.getCellByPosition(6, row);
                xCell.setFormula(hm.get("ean").toString());
                xCell = xSpreadsheet.getCellByPosition(7, row);
                xCell.setFormula("-");

                String nSar = hm.get("ngpr").toString() + "\n"
                        + hm.get("nar").toString() + "\nмодель "
                        + hm.get("fas").toString() + "\nРазмер:" + hm.get("rzm_print").toString().trim();

                xCell = xSpreadsheet.getCellByPosition(8, row);
                xCell.setFormula(nSar);

/*                XCellRange xCellRange = xSpreadsheet
                        .getCellRangeByPosition(3, row, 4, row);

                com.sun.star.util.XMergeable xMerge = (com.sun.star.util.XMergeable) UnoRuntime
                        .queryInterface(com.sun.star.util.XMergeable.class,
                                xCellRange);
                xMerge.merge(true);*/

                xCell = xSpreadsheet.getCellByPosition(9, row);
                xCell.setFormula("штучный");

                xCell = xSpreadsheet.getCellByPosition(10, row);
                xCell.setFormula(hm.get("massa").toString());


                xCell = xSpreadsheet.getCellByPosition(11, row);
                xCell.setFormula(COMPANY_NAME);

                xCell = xSpreadsheet.getCellByPosition(12, row);
                xCell.setFormula(COMPANY_NAME);

                xCell = xSpreadsheet.getCellByPosition(13, row);
                xCell.setFormula("РБ");

                xCell = xSpreadsheet.getCellByPosition(14, row);
                xCell.setFormula("8 Марта");

                xCell = xSpreadsheet.getCellByPosition(15, row);
                xCell.setFormula("-");

                xCell = xSpreadsheet.getCellByPosition(16, row);
                xCell.setFormula(hm.get("kol").toString());

                xCell = xSpreadsheet.getCellByPosition(17, row);
                xCell.setFormula("-");
                xCell = xSpreadsheet.getCellByPosition(18, row);
                xCell.setFormula("-");
                xCell = xSpreadsheet.getCellByPosition(19, row);
                xCell.setFormula("-");

                xCell = xSpreadsheet.getCellByPosition(20, row);
                xCell.setFormula("-");

                xCell = xSpreadsheet.getCellByPosition(21, row);
                xCell.setFormula("-");

                xCell = xSpreadsheet.getCellByPosition(22, row);
                xCell.setFormula(hm.get("nds").toString());

                xCell = xSpreadsheet.getCellByPosition(23, row);
                xCell.setFormula(hm.get("cenaBS").toString());

                xCell = xSpreadsheet.getCellByPosition(24, row);
                xCell.setFormula(hm.get("skidka").toString());

                xCell = xSpreadsheet.getCellByPosition(25, row);
                xCell.setFormula(hm.get("cena").toString());

                xCell = xSpreadsheet.getCellByPosition(26, row);
                xCell.setFormula("-");

                xCell = xSpreadsheet.getCellByPosition(28, row);
                xCell.setFormula("-");
                xCell = xSpreadsheet.getCellByPosition(29, row);
                xCell.setFormula("-");

/*                sar = hm.get("sar").toString();

                xCell = xSpreadsheet.getCellByPosition(6, row);
                if (sar.startsWith("43") || sar.startsWith("48")
                        || sar.startsWith("47")) {
                    xCell.setFormula(hm.get("izm").toString());
                } else {
                    xCell.setFormula("шт.");
                }

                xCell = xSpreadsheet.getCellByPosition(7, row);
                xCell.setFormula("м");

                xCell = xSpreadsheet.getCellByPosition(8, row);
                xCell.setFormula(hm.get("kol").toString());

                xCell = xSpreadsheet.getCellByPosition(9, row);
                xCell.setFormula(hm.get("cenaBS").toString());

                xCell = xSpreadsheet.getCellByPosition(10, row);
                xCell.setFormula(hm.get("skidka").toString());

                double skidka = Double.valueOf(hm.get("skidka").toString());
                double cost = Double.valueOf(hm.get("cena").toString());


                xCell = xSpreadsheet.getCellByPosition(11, row);
                xCell.setFormula(hm.get("cena").toString());

                xCell = xSpreadsheet.getCellByPosition(12, row);
                xCell.setFormula(hm.get("nds").toString());

                double nds = Integer.valueOf(hm.get("nds").toString());

                double cost_with_nds = (cost + (cost * nds / 100));

                n = new BigDecimal(cost_with_nds);
                n = n.setScale(0, BigDecimal.ROUND_HALF_UP);

                xCell = xSpreadsheet.getCellByPosition(13, row);
                xCell.setValue(n.doubleValue());

                xCell = xSpreadsheet.getCellByPosition(14, row);
                xCell.setFormula("-");

                xCell = xSpreadsheet.getCellByPosition(15, row);
                xCell.setFormula("-");

                xCell = xSpreadsheet.getCellByPosition(16, row);
                xCell.setFormula("-");

                xCell = xSpreadsheet.getCellByPosition(17, row);
                xCell.setFormula("-");

                xCell = xSpreadsheet.getCellByPosition(18, row);
                xCell.setFormula(hm.get("massa").toString());*/
                row++;

                for (int z = 6; z < 30; z++) {
                    xCell = xSpreadsheet.getCellByPosition(z, row - 1);
                    xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime
                            .queryInterface(com.sun.star.beans.XPropertySet.class,
                                    xCell);
                    xPropSet.setPropertyValue("CharHeight", charSize);
                    xPropSet.setPropertyValue("TableBorder", aBorder);
                }


            }

            sheet = xSpreadsheets.getByName("Sheet2");
            xSpreadsheet = (XSpreadsheet) UnoRuntime
                    .queryInterface(XSpreadsheet.class, sheet);

            // ----------------Заполнение тела документа
            row = 6;
            lineStart = row;

            for (int i = 0; i < data.size(); i++) {
                hm = (HashMap) data.get(i);

/*                xCell = xSpreadsheet.getCellByPosition(0, row);
                xCell.setFormula(String.valueOf(i + 1));*/

                xCell = xSpreadsheet.getCellByPosition(6, row);
                xCell.setFormula(hm.get("ean").toString());

                String nSar = hm.get("ngpr").toString() + "\n"
                        + hm.get("nar").toString() + "\nмодель "
                        + hm.get("fas").toString() + "\nРазмер:" + hm.get("rzm_print").toString().trim();

                xCell = xSpreadsheet.getCellByPosition(7, row);
                xCell.setFormula(nSar);

                xCell = xSpreadsheet.getCellByPosition(8, row);
                xCell.setFormula("штучный");
                xCell = xSpreadsheet.getCellByPosition(9, row);
                xCell.setFormula(hm.get("massa").toString());

                xCell = xSpreadsheet.getCellByPosition(10, row);
                xCell.setFormula(COMPANY_NAME);

                xCell = xSpreadsheet.getCellByPosition(11, row);
                xCell.setFormula(COMPANY_NAME);

                xCell = xSpreadsheet.getCellByPosition(12, row);
                xCell.setFormula("РБ");

                xCell = xSpreadsheet.getCellByPosition(13, row);
                xCell.setFormula("8 Марта");

                xCell = xSpreadsheet.getCellByPosition(14, row);
                xCell.setFormula("-");
                xCell = xSpreadsheet.getCellByPosition(15, row);
                xCell.setFormula("-");
                xCell = xSpreadsheet.getCellByPosition(16, row);
                xCell.setFormula(hm.get("nds").toString());

                xCell = xSpreadsheet.getCellByPosition(17, row);
                xCell.setFormula(hm.get("cenaBS").toString());

                xCell = xSpreadsheet.getCellByPosition(18, row);
                xCell.setFormula(hm.get("skidka").toString() + "%");

                xCell = xSpreadsheet.getCellByPosition(19, row);
                xCell.setFormula(hm.get("cena").toString());
                xCell = xSpreadsheet.getCellByPosition(20, row);
                xCell.setFormula("0%");

                xCell = xSpreadsheet.getCellByPosition(48, row);
                xCell.setFormula(hm.get("massa").toString());

                row++;

                for (int z = 6; z < 9; z++) {
                    xCell = xSpreadsheet.getCellByPosition(z, row - 1);
                    xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime
                            .queryInterface(com.sun.star.beans.XPropertySet.class,
                                    xCell);
                    xPropSet.setPropertyValue("CharHeight", charSize);
                    xPropSet.setPropertyValue("TableBorder", aBorder);
                }


            }

            saveAsDocument(currentDocument, savePath
                    + "pcs" + dis.get("ttn").toString() + ".xls", lParams);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            log.error("Ошибка при заполнении протокола согласования цен", e);
            JOptionPane.showMessageDialog(null,
                    "Ошибка при заполнении протокола согласования цен",
                    "Ошибка!!!", JOptionPane.ERROR_MESSAGE);
        } finally {
            sdb.disConn();
        }

    }

    private void protocolBelVillesden(XComponent currentDocument) {
        result = new HashMap();
        HashMap<String, Object> hm = new HashMap();
        HashMap dis = (HashMap) discr.get(0);
        String sar = "";
        double razn = 0, raznBuf = 0, raznBuf1 = 0, roznCena = 0, cena = 0, tn = 0, ndsTN = 0, kol = 0;
        int lineStart = 0;
        float charSize = (float) 6.5;
        BigDecimal n;
        int st, stn;
        DecimalFormat df;
        Double sumtn = .0;
        Double sumtn_nds = .0;
        Double sumtn_rzn = .0;

        Double sum_stoim = .0;
        Double sum_cena = .0;
        Double sum_kol = .0;
        Double sum_nds = .0;
        Double sum_kgm = .0;
        Double sum_massa = .0;

        SkladDB sdb = new SkladDB();

        try {
            XSpreadsheetDocument xSpreadsheetDocument = (XSpreadsheetDocument) UnoRuntime
                    .queryInterface(XSpreadsheetDocument.class, currentDocument);
            XSpreadsheets xSpreadsheets = xSpreadsheetDocument.getSheets();
            // xSpreadsheets.insertNewByName("Приложение к ТТН №" +
            // dis.get("ttn"), (short)0);
            Object sheet = xSpreadsheets.getByName("Лист1");
            XSpreadsheet xSpreadsheet = (XSpreadsheet) UnoRuntime
                    .queryInterface(XSpreadsheet.class, sheet);

            XNamed xNamed = (XNamed) UnoRuntime.queryInterface(XNamed.class,
                    xSpreadsheet);
            xNamed.setName("Приложение к ТТН №" + dis.get("ttn"));
            result.put("ttn", dis.get("ttn"));

            com.sun.star.beans.XPropertySet xPropSet = null;
            com.sun.star.table.BorderLine aLine = new com.sun.star.table.BorderLine();
            aLine.Color = 0x000000;
            aLine.InnerLineWidth = (short) 0;
            aLine.LineDistance = (short) 0;
            aLine.OuterLineWidth = (short) 5;
            com.sun.star.table.TableBorder aBorder = new com.sun.star.table.TableBorder();
            aBorder.BottomLine = aLine;
            aBorder.LeftLine = aLine;
            aBorder.RightLine = aLine;
            aBorder.IsBottomLineValid = true;
            aBorder.IsLeftLineValid = true;
            aBorder.IsRightLineValid = true;

            String COMPANY_NAME = "ОАО \"8 Марта\"";
            String REPUBLIC = "РБ";
            System.out.println("Номер документа :" + dis.get("ttn").toString());
            int d_ = sdb.getDogovorIDTTN(dis.get("ttn").toString());
            DogovorInfo dogov = sdb.getClientDogovor(d_);

            SimpleDateFormat ft = new SimpleDateFormat("dd.MM.yyyy");
            String dateDoc = ft.format(dogov.getDataBegin());
            String dateDocEnd = ft.format(dogov.getDataEnd());

            //String dogovor = "К ТТН № "+ dis.get("ttn").toString().trim()+" от "+dis.get("date").toString();
            String dogovor = "№ 12-11/ЗИП-2232 от 31.12.2011г.";

            String dogovor1 = "действует с " + dateDoc + " по " + dateDocEnd + " гг.";

            XCell xCell = xSpreadsheet.getCellByPosition(1, 0);
            //xCell.setFormula(dis.get("ttn").toString());

            xCell = xSpreadsheet.getCellByPosition(1, 1);
            //xCell.setFormula(COMPANY_NAME);

            xCell = xSpreadsheet.getCellByPosition(1, 2);
            //xCell.setFormula(dogov.getNumber().trim()+" от "+dateDoc+" г.");

            //xCell = xSpreadsheet.getCellByPosition(2, 4);
            //xCell.setFormula(dogovor1);

            // ----------------Заполнение тела документа
            int row = 6;
            lineStart = row;

            for (int i = 0; i < data.size(); i++) {
                hm = (HashMap) data.get(i);

                xCell = xSpreadsheet.getCellByPosition(0, row);
                xCell.setFormula(String.valueOf(i + 1));

                xCell = xSpreadsheet.getCellByPosition(1, row);
                xCell.setFormula(hm.get("ean").toString());

                String nSar = hm.get("ngpr").toString() + "\n" +
                        hm.get("nar").toString() + "\nмодель "
                        + hm.get("fas").toString() + "\nРазмер:" + hm.get("rzm_print").toString().trim();

                xCell = xSpreadsheet.getCellByPosition(2, row);
                xCell.setFormula(nSar);

                xCell = xSpreadsheet.getCellByPosition(3, row);
                xCell.setFormula(COMPANY_NAME);

                xCell = xSpreadsheet.getCellByPosition(4, row);
                xCell.setFormula(REPUBLIC);

                xCell = xSpreadsheet.getCellByPosition(5, row);
                xCell.setFormula(hm.get("narp").toString());

                sar = hm.get("sar").toString();

                xCell = xSpreadsheet.getCellByPosition(6, row);
                if (sar.startsWith("43") || sar.startsWith("48")
                        || sar.startsWith("47")) {
                    xCell.setFormula(hm.get("izm").toString());
                } else {
                    xCell.setFormula("шт.");
                }

                xCell = xSpreadsheet.getCellByPosition(7, row);
                xCell.setFormula(hm.get("kol").toString());

                //**************************************************************************
                xCell = xSpreadsheet.getCellByPosition(8, row);
                xCell.setFormula(hm.get("cenaBS").toString());

                xCell = xSpreadsheet.getCellByPosition(9, row);
                xCell.setFormula("0");

                xCell = xSpreadsheet.getCellByPosition(10, row);
                xCell.setFormula(hm.get("skidka").toString());

                double skidka = Double.valueOf(hm.get("skidka").toString());
                double cost = Double.valueOf(hm.get("cena").toString());
                double cost_without_nds = (cost - (cost * skidka / 100));

                n = new BigDecimal(cost);
                n = n.setScale(0, BigDecimal.ROUND_HALF_UP);

                xCell = xSpreadsheet.getCellByPosition(11, row);
                xCell.setValue(n.doubleValue());

                xCell = xSpreadsheet.getCellByPosition(12, row);
                xCell.setFormula(hm.get("nds").toString());

                double nds = Integer.valueOf(hm.get("nds").toString());

                double cost_with_nds = (cost + (cost * nds / 100));

                n = new BigDecimal(cost_with_nds);
                n = n.setScale(0, BigDecimal.ROUND_HALF_UP);

                xCell = xSpreadsheet.getCellByPosition(13, row);
                xCell.setValue(n.doubleValue());

                row++;

                for (int z = 0; z < 26; z++) {
                    xCell = xSpreadsheet.getCellByPosition(z, row - 1);
                    xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime
                            .queryInterface(com.sun.star.beans.XPropertySet.class,
                                    xCell);
                    xPropSet.setPropertyValue("CharHeight", charSize);
                    xPropSet.setPropertyValue("TableBorder", aBorder);
                }
            }

            xCell = xSpreadsheet.getCellByPosition(0, row + 2);
            xCell.setFormula("ПОКУПАТЕЛЬ ");
            xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime
                    .queryInterface(com.sun.star.beans.XPropertySet.class,
                            xCell);
            xPropSet.setPropertyValue("HoriJustify", CellHoriJustify.LEFT);


            xCell = xSpreadsheet.getCellByPosition(0, row + 3);
            xCell.setFormula("Иностранное унитарное предприятие \"БелВиллесден\"");

            xCell = xSpreadsheet.getCellByPosition(0, row + 4);
            xCell.setFormula("г.Минск, пер.Асаналиева, 3-20");

            xCell = xSpreadsheet.getCellByPosition(0, row + 5);
            xCell.setFormula("Р/сч: 3012000355014 в ЦБУ 100 \"Приорбанк\" ОАО код 153001749");

            xCell = xSpreadsheet.getCellByPosition(0, row + 6);
            xCell.setFormula("адрес: 220070, г.Минск, ул. Радиальная, 38а");

            xCell = xSpreadsheet.getCellByPosition(0, row + 7);
            xCell.setFormula("УНП: 800001064");

            xCell = xSpreadsheet.getCellByPosition(0, row + 15);
            xCell.setFormula("От имени покупателя");

            xCell = xSpreadsheet.getCellByPosition(2, row + 15);
            xCell.setFormula("__________________/Мазаник-Калиновская И.В./");

            xCell = xSpreadsheet.getCellByPosition(0, row + 17);
            xCell.setFormula("Начальник отдела закупок \nкоммерческого управления \nИностранного унитарного\n" +
                    "предприятия \"БелВиллесден\"");

            xCell = xSpreadsheet.getCellByPosition(0, row + 19);
            xCell.setFormula("Главный специалист");

            xCell = xSpreadsheet.getCellByPosition(2, row + 19);
            xCell.setFormula("__________________/Шавель С.В./");

            xCell = xSpreadsheet.getCellByPosition(0, row + 20);
            xCell.setFormula("Ведущий специалист");

            xCell = xSpreadsheet.getCellByPosition(2, row + 20);
            xCell.setFormula("__________________/Шамак А.П./");

            ///***************************************************************************************
            xCell = xSpreadsheet.getCellByPosition(8, row + 2);
            xCell.setFormula("ПОСТАВЩИК ");
            xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime
                    .queryInterface(com.sun.star.beans.XPropertySet.class,
                            xCell);
            xPropSet.setPropertyValue("HoriJustify", CellHoriJustify.LEFT);

            xCell = xSpreadsheet.getCellByPosition(8, row + 3);
            xCell.setFormula(COMPANY_NAME);

            xCell = xSpreadsheet.getCellByPosition(8, row + 4);
            xCell.setFormula("246708, РБ, г.Гомель, ул.Советская, 41");

            xCell = xSpreadsheet.getCellByPosition(8, row + 5);
            xCell.setFormula("Р/сч: 3012310025015 в ЦБУ 301 \"Белинвестбанк\" ОАО код 153001739");

            xCell = xSpreadsheet.getCellByPosition(8, row + 6);
            xCell.setFormula("адрес: 246022, г.Гомель, ул. Советская, 48");

            xCell = xSpreadsheet.getCellByPosition(8, row + 7);
            xCell.setFormula("УНП: 400078265, ОКПО: 00311935");


            xCell = xSpreadsheet.getCellByPosition(8, row + 15);
            xCell.setFormula("От имени Поставщика");

            xCell = xSpreadsheet.getCellByPosition(8, row + 17);
            xCell.setFormula("Ведущий специалист отдела сбыта __________________");


            xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime
                    .queryInterface(com.sun.star.beans.XPropertySet.class,
                            xCell);

            XCellRange xCellRange = xSpreadsheet
                    .getCellRangeByPosition(0, row + 2, 8, row + 20);
            xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime
                    .queryInterface(com.sun.star.beans.XPropertySet.class,
                            xCellRange);
            xPropSet.setPropertyValue("CharHeight", charSize);

            saveAsDocument(currentDocument, savePath
                    + "pcs" + dis.get("ttn").toString() + ".ods", lParams);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            log.error("Ошибка при заполнении протокола согласования цен", e);
            JOptionPane.showMessageDialog(null,
                    "Ошибка при заполнении протокола согласования цен",
                    "Ошибка!!!", JOptionPane.ERROR_MESSAGE);
        } finally {
            sdb.disConn();
        }

    }


    private void protocolBelVillesdenNew(XComponent currentDocument) {
        result = new HashMap();
        HashMap<String, Object> hm = new HashMap();
        HashMap dis = (HashMap) discr.get(0);
        String sar = "";
        double razn = 0, raznBuf = 0, raznBuf1 = 0, roznCena = 0, cena = 0, tn = 0, ndsTN = 0, kol = 0;
        int lineStart = 0;
        float charSize = (float) 6.5;
        BigDecimal n;
        int st, stn;
        DecimalFormat df;
        Double sumtn = .0;
        Double sumtn_nds = .0;
        Double sumtn_rzn = .0;

        Double sum_stoim = .0;
        Double sum_cena = .0;
        Double sum_kol = .0;
        Double sum_nds = .0;
        Double sum_kgm = .0;
        Double sum_massa = .0;

        SkladDB sdb = new SkladDB();

        try {
            XSpreadsheetDocument xSpreadsheetDocument = (XSpreadsheetDocument) UnoRuntime
                    .queryInterface(XSpreadsheetDocument.class, currentDocument);
            XSpreadsheets xSpreadsheets = xSpreadsheetDocument.getSheets();
            // xSpreadsheets.insertNewByName("Приложение к ТТН №" +
            // dis.get("ttn"), (short)0);
            Object sheet = xSpreadsheets.getByName("Лист1");
            XSpreadsheet xSpreadsheet = (XSpreadsheet) UnoRuntime
                    .queryInterface(XSpreadsheet.class, sheet);

            XNamed xNamed = (XNamed) UnoRuntime.queryInterface(XNamed.class,
                    xSpreadsheet);
            xNamed.setName("ПСЦ к ТТН №" + dis.get("ttn"));
            result.put("ttn", dis.get("ttn"));

            com.sun.star.beans.XPropertySet xPropSet = null;
            com.sun.star.table.BorderLine aLine = new com.sun.star.table.BorderLine();
            aLine.Color = 0x000000;
            aLine.InnerLineWidth = (short) 0;
            aLine.LineDistance = (short) 0;
            aLine.OuterLineWidth = (short) 5;
            com.sun.star.table.TableBorder aBorder = new com.sun.star.table.TableBorder();
            aBorder.BottomLine = aLine;
            aBorder.LeftLine = aLine;
            aBorder.RightLine = aLine;
            aBorder.IsBottomLineValid = true;
            aBorder.IsLeftLineValid = true;
            aBorder.IsRightLineValid = true;

            String COMPANY_NAME = "ОАО \"8 Марта\"";
            String REPUBLIC = "РБ";
            System.out.println("Номер документа :" + dis.get("ttn").toString());
            int d_ = sdb.getDogovorIDTTN(dis.get("ttn").toString());
            DogovorInfo dogov = sdb.getClientDogovor(d_);

            SimpleDateFormat ft = new SimpleDateFormat("dd.MM.yyyy");
            String dateDoc = ft.format(dogov.getDataBegin());
            String dateDocEnd = ft.format(dogov.getDataEnd());

            //String dogovor = "К ТТН № "+ dis.get("ttn").toString().trim()+" от "+dis.get("date").toString();
            String dogovor = "№ 12-11/ЗИП-2232 поставщика ОАО \"8 Марта\" от 31.12.2011г.";

            String dogovor1 = "действует с " + dateDoc + " по " + dateDocEnd + " гг.";

            XCell xCell = xSpreadsheet.getCellByPosition(2, 0);
            xCell.setFormula(dogovor);
            xCell = xSpreadsheet.getCellByPosition(0, 0);
            xCell.setFormula("ПСЦ к договору ");


            xCell = xSpreadsheet.getCellByPosition(3, 4);
            xCell.setFormula(dateDoc);

            xCell = xSpreadsheet.getCellByPosition(5, 4);
            xCell.setFormula(dateDocEnd);

            //xCell = xSpreadsheet.getCellByPosition(2, 4);
            //xCell.setFormula(dogovor1);

            // ----------------Заполнение тела документа
            int row = 6;
            lineStart = row;

            for (int i = 0; i < data.size(); i++) {
                hm = (HashMap) data.get(i);

                xCell = xSpreadsheet.getCellByPosition(0, row);
                xCell.setFormula(String.valueOf(i + 1));

                xCell = xSpreadsheet.getCellByPosition(1, row);
                xCell.setFormula(hm.get("ean").toString());

                String nSar = hm.get("ngpr").toString() + "\n" +
                        hm.get("nar").toString() + "\nмодель "
                        + hm.get("fas").toString() + "\nРазмер:" + hm.get("rzm_print").toString().trim();

                xCell = xSpreadsheet.getCellByPosition(2, row);
                xCell.setFormula(nSar);

                xCell = xSpreadsheet.getCellByPosition(3, row);
                xCell.setFormula(COMPANY_NAME);

                xCell = xSpreadsheet.getCellByPosition(4, row);
                xCell.setFormula(REPUBLIC);

                xCell = xSpreadsheet.getCellByPosition(5, row);
                xCell.setFormula(hm.get("narp").toString());

                sar = hm.get("sar").toString();

                xCell = xSpreadsheet.getCellByPosition(6, row);
                if (sar.startsWith("43") || sar.startsWith("48")
                        || sar.startsWith("47")) {
                    xCell.setFormula(hm.get("izm").toString());
                } else {
                    xCell.setFormula("шт.");
                }

                xCell = xSpreadsheet.getCellByPosition(7, row);
                xCell.setFormula(hm.get("kol").toString());

                //**************************************************************************
                xCell = xSpreadsheet.getCellByPosition(8, row);
                xCell.setFormula(hm.get("cenaBS").toString());

                xCell = xSpreadsheet.getCellByPosition(9, row);
                xCell.setFormula("0");

                xCell = xSpreadsheet.getCellByPosition(10, row);
                xCell.setFormula(hm.get("skidka").toString());

                double skidka = Double.valueOf(hm.get("skidka").toString());
                double cost = Double.valueOf(hm.get("cena").toString());
                double cost_without_nds = (cost - (cost * skidka / 100));

                n = new BigDecimal(cost);
                n = n.setScale(0, BigDecimal.ROUND_HALF_UP);

                xCell = xSpreadsheet.getCellByPosition(11, row);
                xCell.setValue(n.doubleValue());

                xCell = xSpreadsheet.getCellByPosition(12, row);
                xCell.setFormula(hm.get("skidka").toString());

                xCell = xSpreadsheet.getCellByPosition(13, row);
                xCell.setFormula(hm.get("nds").toString());

                double nds = Integer.valueOf(hm.get("nds").toString());

                double cost_with_nds = (cost + (cost * nds / 100));

                n = new BigDecimal(cost_with_nds);
                n = n.setScale(0, BigDecimal.ROUND_HALF_UP);

                xCell = xSpreadsheet.getCellByPosition(14, row);
                xCell.setValue(n.doubleValue());

                //Масса
                xCell = xSpreadsheet.getCellByPosition(25, row);
                String weightText = "";
                try {
                    int amount = Integer.valueOf(hm.get("kol").toString());
                    double weight = Double.valueOf(hm.get("massa").toString());
                    weightText = String.format("%.2f", (double) weight * amount);

                } catch (Exception mathEx) {
                    System.out.println("Ошибка расчета массы");
                    weightText = hm.get("massa").toString();
                }

                xCell.setFormula(weightText);

                //Ценовой сегмент
                xCell = xSpreadsheet.getCellByPosition(26, row);
                double dbl = Double.valueOf(hm.get("cenaBS").toString());
                int costInt = (int) dbl;

                if (costInt < 50000) {
                    xCell.setFormula("эконом");
                } else if (costInt >= 50000 && costInt < 150000) {
                    xCell.setFormula("средний");
                } else if (costInt > 150000) {
                    xCell.setFormula("премиум");
                }

                // Сгибаемость
                xCell = xSpreadsheet.getCellByPosition(27, row);
                xCell.setFormula("50");

                row++;

                for (int z = 0; z < 28; z++) {
                    xCell = xSpreadsheet.getCellByPosition(z, row - 1);
                    xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime
                            .queryInterface(com.sun.star.beans.XPropertySet.class,
                                    xCell);
                    xPropSet.setPropertyValue("CharHeight", charSize);
                    xPropSet.setPropertyValue("TableBorder", aBorder);
                }

                XCellRange xCellRange = xSpreadsheet
                        .getCellRangeByPosition(0, row - 1, 0,
                                row - 1);
                setCustomHeight(xCellRange, 1000);
            }

            xCell = xSpreadsheet.getCellByPosition(0, row + 2);
            xCell.setFormula("ПОКУПАТЕЛЬ ");
            xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime
                    .queryInterface(com.sun.star.beans.XPropertySet.class,
                            xCell);
            xPropSet.setPropertyValue("HoriJustify", CellHoriJustify.LEFT);


            xCell = xSpreadsheet.getCellByPosition(0, row + 3);
            xCell.setFormula("Иностранное унитарное предприятие \"БелВиллесден\"");

            xCell = xSpreadsheet.getCellByPosition(0, row + 4);
            xCell.setFormula("г.Минск, пер.Асаналиева, 3-20");

            xCell = xSpreadsheet.getCellByPosition(0, row + 5);
            xCell.setFormula("Р/сч: 3012000355014 в ЦБУ 100 \"Приорбанк\" ОАО код 153001749");

            xCell = xSpreadsheet.getCellByPosition(0, row + 6);
            xCell.setFormula("адрес: 220070, г.Минск, ул. Радиальная, 38а");

            xCell = xSpreadsheet.getCellByPosition(0, row + 7);
            xCell.setFormula("УНП: 800001064");

            xCell = xSpreadsheet.getCellByPosition(0, row + 10);
            xCell.setFormula("От имени покупателя");

            xCell = xSpreadsheet.getCellByPosition(2, row + 12);
            xCell.setFormula("__________________/Ефимов В.И./");

            xCell = xSpreadsheet.getCellByPosition(0, row + 12);
            xCell.setFormula("Начальник КУ");

            xCell = xSpreadsheet.getCellByPosition(0, row + 13);
            xCell.setFormula("Менеджер группы");

            xCell = xSpreadsheet.getCellByPosition(2, row + 13);
            xCell.setFormula("__________________/Авсянникова О.С./");

            xCell = xSpreadsheet.getCellByPosition(0, row + 14);
            xCell.setFormula("Главный специалист");

            xCell = xSpreadsheet.getCellByPosition(2, row + 14);
            xCell.setFormula("__________________/Шавель С.В./");

            xCell = xSpreadsheet.getCellByPosition(0, row + 15);
            xCell.setFormula("Ведущий специалист");

            xCell = xSpreadsheet.getCellByPosition(2, row + 15);
            xCell.setFormula("__________________/Калинина А.Ю./");

            ///***************************************************************************************
            xCell = xSpreadsheet.getCellByPosition(10, row + 2);
            xCell.setFormula("ПОСТАВЩИК ");
            xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime
                    .queryInterface(com.sun.star.beans.XPropertySet.class,
                            xCell);
            xPropSet.setPropertyValue("HoriJustify", CellHoriJustify.LEFT);

            xCell = xSpreadsheet.getCellByPosition(10, row + 3);
            xCell.setFormula(COMPANY_NAME);

            xCell = xSpreadsheet.getCellByPosition(10, row + 4);
            xCell.setFormula("246708, РБ, г.Гомель, ул.Советская, 41");

            xCell = xSpreadsheet.getCellByPosition(10, row + 5);
            xCell.setFormula("Р/сч: 3012310025015 в ЦБУ 301 \"Белинвестбанк\" ОАО код 153001739");

            xCell = xSpreadsheet.getCellByPosition(10, row + 6);
            xCell.setFormula("адрес: 246022, г.Гомель, ул. Советская, 48");

            xCell = xSpreadsheet.getCellByPosition(10, row + 7);
            xCell.setFormula("УНП: 400078265, ОКПО: 00311935");


            xCell = xSpreadsheet.getCellByPosition(10, row + 10);
            xCell.setFormula("От имени Поставщика");

            xCell = xSpreadsheet.getCellByPosition(10, row + 12);
            xCell.setFormula("Начальник отдела сбыта __________________/Кретова Е.А.");


            xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime
                    .queryInterface(com.sun.star.beans.XPropertySet.class,
                            xCell);

            XCellRange xCellRange = xSpreadsheet
                    .getCellRangeByPosition(0, row + 2, 28, row + 20);
            xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime
                    .queryInterface(com.sun.star.beans.XPropertySet.class,
                            xCellRange);
            xPropSet.setPropertyValue("CharHeight", charSize);

            saveAsDocument(currentDocument, savePath
                    + "pcs" + dis.get("ttn").toString() + ".ods", lParams);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            log.error("Ошибка при заполнении протокола согласования цен", e);
            JOptionPane.showMessageDialog(null,
                    "Ошибка при заполнении протокола согласования цен",
                    "Ошибка!!!", JOptionPane.ERROR_MESSAGE);
        } finally {
            sdb.disConn();
        }

    }

    private void protocolUnifud(XComponent currentDocument) {
        result = new HashMap();
        HashMap<String, Object> hm = new HashMap();
        HashMap dis = (HashMap) discr.get(0);
        String sar = "";
        double razn = 0, raznBuf = 0, raznBuf1 = 0, roznCena = 0, cena = 0, tn = 0, ndsTN = 0, kol = 0;
        int lineStart = 0;
        float charSize = (float) 6.5;
        BigDecimal n;
        int st, stn;
        DecimalFormat df;
        Double sumtn = .0;
        Double sumtn_nds = .0;
        Double sumtn_rzn = .0;

        Double sum_stoim = .0;
        Double sum_cena = .0;
        Double sum_kol = .0;
        Double sum_nds = .0;
        Double sum_kgm = .0;
        Double sum_massa = .0;

        SkladDB sdb = new SkladDB();

        try {
            XSpreadsheetDocument xSpreadsheetDocument = (XSpreadsheetDocument) UnoRuntime
                    .queryInterface(XSpreadsheetDocument.class, currentDocument);
            XSpreadsheets xSpreadsheets = xSpreadsheetDocument.getSheets();
            // xSpreadsheets.insertNewByName("Приложение к ТТН №" +
            // dis.get("ttn"), (short)0);
            Object sheet = xSpreadsheets.getByName("Лист1");
            XSpreadsheet xSpreadsheet = (XSpreadsheet) UnoRuntime
                    .queryInterface(XSpreadsheet.class, sheet);

            XNamed xNamed = (XNamed) UnoRuntime.queryInterface(XNamed.class,
                    xSpreadsheet);
            xNamed.setName("Приложение к ТТН №" + dis.get("ttn"));
            result.put("ttn", dis.get("ttn"));

            com.sun.star.beans.XPropertySet xPropSet = null;
            com.sun.star.table.BorderLine aLine = new com.sun.star.table.BorderLine();
            aLine.Color = 0x000000;
            aLine.InnerLineWidth = (short) 0;
            aLine.LineDistance = (short) 0;
            aLine.OuterLineWidth = (short) 5;
            com.sun.star.table.TableBorder aBorder = new com.sun.star.table.TableBorder();
            aBorder.BottomLine = aLine;
            aBorder.LeftLine = aLine;
            aBorder.RightLine = aLine;
            aBorder.IsBottomLineValid = true;
            aBorder.IsLeftLineValid = true;
            aBorder.IsRightLineValid = true;

            String COMPANY_NAME = "ОАО \"8 Марта\"";
            String REPUBLIC = "РБ";
            String BREND = "8 Марта";
            String COMPANY = "производитель";
            String PRICE_SEGMENT = "средний";

            System.out.println("Номер документа :" + dis.get("ttn").toString());

            int d_ = sdb.getDogovorIDTTN(dis.get("ttn").toString());
            DogovorInfo dogov = sdb.getClientDogovor(d_);

            SimpleDateFormat ft = new SimpleDateFormat("dd.MM.yyyy");
            String dateDoc = ft.format(dogov.getDataBegin());
            String dateDocEnd = ft.format(dogov.getDataEnd());

            //String dogovor = "К ТТН № "+ dis.get("ttn").toString().trim()+" от "+dis.get("date").toString();
            String dogovor = "№ 12-11/ЗИП-2232 от 31.12.2011г.";

            String dogovor1 = "действует с " + dateDoc + " по " + dateDocEnd + " гг.";

            XCell xCell = xSpreadsheet.getCellByPosition(1, 0);
            //xCell.setFormula(dis.get("ttn").toString());

            xCell = xSpreadsheet.getCellByPosition(1, 1);
            //xCell.setFormula(COMPANY_NAME);

            xCell = xSpreadsheet.getCellByPosition(1, 2);
            //xCell.setFormula(dogov.getNumber().trim()+" от "+dateDoc+" г.");

            //xCell = xSpreadsheet.getCellByPosition(2, 4);
            //xCell.setFormula(dogovor1);

            // ----------------Заполнение тела документа
            int row = 5;
            lineStart = row;

            for (int i = 0; i < data.size(); i++) {
                hm = (HashMap) data.get(i);

                xCell = xSpreadsheet.getCellByPosition(0, row);
                xCell.setFormula(String.valueOf(i + 1));

                String nSar = hm.get("ngpr").toString().trim() + "\nмодель "
                        + hm.get("fas").toString() + "\nразмер:" + hm.get("rzm_print").toString().trim();

                xCell = xSpreadsheet.getCellByPosition(1, row);
                xCell.setFormula(nSar);

                //#######СОСТАВ###########
                xCell = xSpreadsheet.getCellByPosition(2, row);
                xCell.setFormula("");

                xCell = xSpreadsheet.getCellByPosition(3, row);
                xCell.setFormula(hm.get("nar").toString());

                xCell = xSpreadsheet.getCellByPosition(4, row);
                xCell.setFormula(COMPANY);

                xCell = xSpreadsheet.getCellByPosition(5, row);
                xCell.setFormula(REPUBLIC);

                xCell = xSpreadsheet.getCellByPosition(6, row);
                xCell.setFormula(BREND);

                sar = hm.get("sar").toString();

                xCell = xSpreadsheet.getCellByPosition(7, row);
                if (sar.startsWith("43") || sar.startsWith("48")
                        || sar.startsWith("47")) {
                    xCell.setFormula(hm.get("izm").toString());
                } else {
                    xCell.setFormula("шт.");
                }

                xCell = xSpreadsheet.getCellByPosition(8, row);
                xCell.setFormula(hm.get("kol").toString());

                xCell = xSpreadsheet.getCellByPosition(9, row);
                xCell.setFormula(PRICE_SEGMENT);

                //**************************************************************************
                xCell = xSpreadsheet.getCellByPosition(10, row);
                xCell.setFormula(hm.get("cena").toString());

                xCell = xSpreadsheet.getCellByPosition(11, row);
                xCell.setFormula(hm.get("skidka").toString());

                double skidka = Double.valueOf(hm.get("skidka").toString());
                double cost = Double.valueOf(hm.get("cena").toString());
                double cost_without_nds = (cost + (cost * skidka / 100));

                n = new BigDecimal(cost_without_nds);
                n = n.setScale(0, BigDecimal.ROUND_HALF_UP);

                xCell = xSpreadsheet.getCellByPosition(12, row);
                xCell.setValue(n.doubleValue());

                xCell = xSpreadsheet.getCellByPosition(13, row);
                xCell.setFormula(hm.get("nds").toString());

                double nds = Integer.valueOf(hm.get("nds").toString());

                double cost_with_nds = (cost_without_nds + (cost_without_nds * nds / 100));

                n = new BigDecimal(cost_with_nds);
                n = n.setScale(0, BigDecimal.ROUND_HALF_UP);

                xCell = xSpreadsheet.getCellByPosition(14, row);
                xCell.setValue(n.doubleValue());

                xCell = xSpreadsheet.getCellByPosition(15, row);
                xCell.setFormula(hm.get("rst").toString());

                xCell = xSpreadsheet.getCellByPosition(16, row);
                xCell.setFormula(hm.get("rzm").toString());

                xCell = xSpreadsheet.getCellByPosition(18, row);
                xCell.setFormula(hm.get("massa").toString());

                xCell = xSpreadsheet.getCellByPosition(21, row);
                xCell.setFormula(hm.get("ean").toString());

                xCell = xSpreadsheet.getCellByPosition(22, row);
                xCell.setFormula(COMPANY_NAME);

                row++;

                for (int z = 0; z < 24; z++) {
                    xCell = xSpreadsheet.getCellByPosition(z, row - 1);
                    xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime
                            .queryInterface(com.sun.star.beans.XPropertySet.class,
                                    xCell);
                    xPropSet.setPropertyValue("CharHeight", charSize);
                    xPropSet.setPropertyValue("TableBorder", aBorder);
                }
            }

            xCell = xSpreadsheet.getCellByPosition(0, row + 2);
            xCell.setFormula("ПОКУПАТЕЛЬ ");
            xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime
                    .queryInterface(com.sun.star.beans.XPropertySet.class,
                            xCell);
            xPropSet.setPropertyValue("HoriJustify", CellHoriJustify.LEFT);

			/*
			xCell = xSpreadsheet.getCellByPosition(0, row+3);
			xCell.setFormula("Закрытое акционерное общество \"Юнифуд\"");


			xCell = xSpreadsheet.getCellByPosition(0, row+4);
			xCell.setFormula("!! г.Минск, пер.Асаналиева, 3-20");

			xCell = xSpreadsheet.getCellByPosition(0, row+5);
			xCell.setFormula("!! Р/сч: 3012000355014 в ЦБУ 100 \"Приорбанк\" ОАО код 153001749");

			xCell = xSpreadsheet.getCellByPosition(0, row+6);
			xCell.setFormula("!! адрес: 220070, г.Минск, ул. Радиальная, 38а");

			xCell = xSpreadsheet.getCellByPosition(0, row+7);
			xCell.setFormula("!! УНП: 800001064");

			xCell = xSpreadsheet.getCellByPosition(0, row+15);
			xCell.setFormula("От имени покупателя");

			xCell = xSpreadsheet.getCellByPosition(2, row+15);
			xCell.setFormula("__________________/Мазаник-Калиновская И.В./");

			xCell = xSpreadsheet.getCellByPosition(0, row+17);
			xCell.setFormula("Начальник отдела закупок \nкоммерческого управления \nИностранного унитарного\n"+
			"предприятия \"БелВиллесден\"");

			xCell = xSpreadsheet.getCellByPosition(0, row+19);
			xCell.setFormula("Главный специалист");

			xCell = xSpreadsheet.getCellByPosition(2, row+19);
			xCell.setFormula("__________________/Шавель С.В./");

			xCell = xSpreadsheet.getCellByPosition(0, row+20);
			xCell.setFormula("Ведущий специалист");

			xCell = xSpreadsheet.getCellByPosition(2, row+20);
			xCell.setFormula("__________________/Шамак А.П./");
			*/
            ///***************************************************************************************
            xCell = xSpreadsheet.getCellByPosition(9, row + 2);
            xCell.setFormula("ПОСТАВЩИК ");
            xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime
                    .queryInterface(com.sun.star.beans.XPropertySet.class,
                            xCell);
            xPropSet.setPropertyValue("HoriJustify", CellHoriJustify.LEFT);

            xCell = xSpreadsheet.getCellByPosition(9, row + 3);
            xCell.setFormula(COMPANY_NAME);

            xCell = xSpreadsheet.getCellByPosition(9, row + 4);
            xCell.setFormula("246708, РБ, г.Гомель, ул.Советская, 41");

            xCell = xSpreadsheet.getCellByPosition(9, row + 5);
            xCell.setFormula("Р/сч: 3012310025015 в ЦБУ 301 \"Белинвестбанк\" ОАО код 153001739");

            xCell = xSpreadsheet.getCellByPosition(9, row + 6);
            xCell.setFormula("адрес: 246022, г.Гомель, ул. Советская, 48");

            xCell = xSpreadsheet.getCellByPosition(9, row + 7);
            xCell.setFormula("УНП: 400078265, ОКПО: 00311935");

			/*
			xCell = xSpreadsheet.getCellByPosition(9, row+15);
			xCell.setFormula("От имени Поставщика");

			xCell = xSpreadsheet.getCellByPosition(9, row+17);
			xCell.setFormula("Ведущий специалист отдела сбыта __________________");
                        */

            xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime
                    .queryInterface(com.sun.star.beans.XPropertySet.class,
                            xCell);

            XCellRange xCellRange = xSpreadsheet
                    .getCellRangeByPosition(0, row + 2, 8, row + 20);
            xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime
                    .queryInterface(com.sun.star.beans.XPropertySet.class,
                            xCellRange);
            xPropSet.setPropertyValue("CharHeight", charSize);

            saveAsDocument(currentDocument, savePath
                    + "pcs" + dis.get("ttn").toString() + ".ods", lParams);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            log.error("Ошибка при заполнении протокола согласования цен", e);
            JOptionPane.showMessageDialog(null,
                    "Ошибка при заполнении протокола согласования цен",
                    "Ошибка!!!", JOptionPane.ERROR_MESSAGE);
        } finally {
            sdb.disConn();
        }

    }

    private void protocolBelPost(XComponent currentDocument) {
        result = new HashMap();
        HashMap<String, Object> hm = new HashMap();
        HashMap dis = (HashMap) discr.get(0);
        String sar = "";
        double razn = 0, raznBuf = 0, raznBuf1 = 0, roznCena = 0, cena = 0, tn = 0, ndsTN = 0, kol = 0;
        int lineStart = 0;
        float charSize = (float) 6.5;
        BigDecimal n;
        int st, stn;
        DecimalFormat df;
        Double sumtn = .0;
        Double sumtn_nds = .0;
        Double sumtn_rzn = .0;

        Double sum_stoim = .0;
        Double sum_cena = .0;
        Double sum_kol = .0;
        Double sum_nds = .0;
        Double sum_kgm = .0;
        Double sum_massa = .0;

        SkladDB sdb = new SkladDB();

        try {
            XSpreadsheetDocument xSpreadsheetDocument = (XSpreadsheetDocument) UnoRuntime
                    .queryInterface(XSpreadsheetDocument.class, currentDocument);
            XSpreadsheets xSpreadsheets = xSpreadsheetDocument.getSheets();
            // xSpreadsheets.insertNewByName("Приложение к ТТН №" +
            // dis.get("ttn"), (short)0);
            Object sheet = xSpreadsheets.getByName("Лист1");
            XSpreadsheet xSpreadsheet = (XSpreadsheet) UnoRuntime
                    .queryInterface(XSpreadsheet.class, sheet);

            XNamed xNamed = (XNamed) UnoRuntime.queryInterface(XNamed.class,
                    xSpreadsheet);
            xNamed.setName("Приложение к ТТН №" + dis.get("ttn"));
            result.put("ttn", dis.get("ttn"));

            com.sun.star.beans.XPropertySet xPropSet = null;
            com.sun.star.table.BorderLine aLine = new com.sun.star.table.BorderLine();
            aLine.Color = 0x000000;
            aLine.InnerLineWidth = (short) 0;
            aLine.LineDistance = (short) 0;
            aLine.OuterLineWidth = (short) 5;
            com.sun.star.table.TableBorder aBorder = new com.sun.star.table.TableBorder();
            aBorder.BottomLine = aLine;
            aBorder.LeftLine = aLine;
            aBorder.RightLine = aLine;
            aBorder.IsBottomLineValid = true;
            aBorder.IsLeftLineValid = true;
            aBorder.IsRightLineValid = true;

            String COMPANY_NAME = "ОАО \"8 Марта\"";
            String REPUBLIC = "РБ";
            System.out.println("Номер документа :" + dis.get("ttn").toString());
            int d_ = sdb.getDogovorIDTTN(dis.get("ttn").toString());
            DogovorInfo dogov = sdb.getClientDogovor(d_);
            String client = sdb.getClientNameByCode(dis.get("ttn").toString());

            SimpleDateFormat ft = new SimpleDateFormat("dd.MM.yyyy");
            String dateDoc = ft.format(dogov.getDataBegin());
            String dateDocEnd = ft.format(dogov.getDataEnd());

            //String dogovor = "К ТТН № "+ dis.get("ttn").toString().trim()+" от "+dis.get("date").toString();
            String dogovor = "к договору № " + dogov.getNumber().trim() + " от " + dateDoc + " г.";

            String dogovor1 = "действует с " + dateDoc + " по " + dateDocEnd + " гг.";


            XCell xCell = xSpreadsheet.getCellByPosition(7, 1);
            xCell.setFormula("№ " + dogov.getNumber().trim() + " от " + dateDoc + "г.");

            xCell = xSpreadsheet.getCellByPosition(3, 3);
            xCell.setFormula(client.trim());

            xCell = xSpreadsheet.getCellByPosition(3, 4);
            xCell.setFormula(COMPANY_NAME);

            xCell = xSpreadsheet.getCellByPosition(4, 5);
            xCell.setFormula(dateDoc);

            //xCell = xSpreadsheet.getCellByPosition(2, 4);
            //xCell.setFormula(dogovor1);

            // ----------------Заполнение тела документа
            int row = 11;
            lineStart = row;
            String eanCode = "";
            String sarNumber = "";
            String sort = "";
            int startRow = row;

            try {
                hm = (HashMap) data.get(0);
                eanCode = hm.get("rzm_print").toString();
                sarNumber = hm.get("sar").toString();
                sort = hm.get("srt").toString();
            } catch (Exception ex) {
                System.out.println("Ошибка получения ЕАН кода");
            }

            for (int i = 0; i < data.size(); i++) {
                hm = (HashMap) data.get(i);
                // Корректировка группы на размер
             /*
                if (!eanCode.equals(hm.get("rzm_print").toString())) {
                    // В пределах одного шифра артикула
                    if (!sarNumber.equals(hm.get("sar").toString())) {

                        XCellRange xVertCellRange = xSpreadsheet
                                .getCellRangeByPosition(1, startRow, 1, row);
                        com.sun.star.util.XMergeable xVertMerge = (com.sun.star.util.XMergeable) UnoRuntime
                                .queryInterface(com.sun.star.util.XMergeable.class,
                                        xVertCellRange);
                        xVertMerge.merge(true);
                        startRow = row + 1;
                    }
                    row++;
                    // Корректировка группы на сорт
                } else if (!sort.equals(hm.get("srt").toString())) {
                    // В пределах одного шифра артикула
                    if (!sarNumber.equals(hm.get("sar").toString())) {

                        XCellRange xVertCellRange = xSpreadsheet
                                .getCellRangeByPosition(1, startRow, 1, row);
                        com.sun.star.util.XMergeable xVertMerge = (com.sun.star.util.XMergeable) UnoRuntime
                                .queryInterface(com.sun.star.util.XMergeable.class,
                                        xVertCellRange);
                        xVertMerge.merge(true);
                        startRow = row + 1;
                    }
                    row++;
                }
                */

                sarNumber = hm.get("sar").toString();
                eanCode = hm.get("rzm_print").toString();
                sort = hm.get("srt").toString();


                xCell = xSpreadsheet.getCellByPosition(0, row);
                xCell.setFormula(String.valueOf(row - 10));

                String nSar = hm.get("ngpr").toString() + "\n"
                        + hm.get("nar").toString() + "\nмодель "
                        + hm.get("fas").toString() + "\nразмер: " + hm.get("rzm_print").toString().trim();

                xCell = xSpreadsheet.getCellByPosition(1, row);
                xCell.setFormula(hm.get("ean").toString());

                xCell = xSpreadsheet.getCellByPosition(2, row);
                xCell.setFormula(nSar);

                xCell = xSpreadsheet.getCellByPosition(3, row);
                xCell.setFormula("шт.");


                //**************************************************************************
                /*xCell = xSpreadsheet.getCellByPosition(8, row);
                xCell.setFormula(hm.get("cenaBS").toString());

                xCell = xSpreadsheet.getCellByPosition(9, row);
                xCell.setFormula("0");

                xCell = xSpreadsheet.getCellByPosition(10, row);
                xCell.setFormula(hm.get("skidka").toString());

                double skidka = Double.valueOf(hm.get("skidka").toString());
                double cost = Double.valueOf(hm.get("cena").toString());
                double cost_without_nds = (cost - (cost * skidka / 100));

                n = new BigDecimal(cost);
                n = n.setScale(0, BigDecimal.ROUND_HALF_UP);

                xCell = xSpreadsheet.getCellByPosition(11, row);
                xCell.setValue(n.doubleValue());

                xCell = xSpreadsheet.getCellByPosition(12, row);
                xCell.setFormula(hm.get("nds").toString());

                double nds = Integer.valueOf(hm.get("nds").toString());

                double cost_with_nds = (cost + (cost * nds / 100));

                n = new BigDecimal(cost_with_nds);
                n = n.setScale(0, BigDecimal.ROUND_HALF_UP);

                xCell = xSpreadsheet.getCellByPosition(13, row);
                xCell.setValue(n.doubleValue());*/


                xCell = xSpreadsheet.getCellByPosition(4, row);
                xCell.setFormula(hm.get("cena").toString());

                xCell = xSpreadsheet.getCellByPosition(5, row);
                xCell.setFormula(hm.get("skidka").toString());

                xCell = xSpreadsheet.getCellByPosition(6, row);
                xCell.setFormula(hm.get("nds").toString());


                double skidka = Double.valueOf(hm.get("skidka").toString());
                double cost = Double.valueOf(hm.get("cena").toString());

                double nds = Integer.valueOf(hm.get("nds").toString());
                double cost_with_nds = (cost + (cost * nds / 100));

                n = new BigDecimal(cost_with_nds);
                n = n.setScale(0, BigDecimal.ROUND_HALF_UP);

                xCell = xSpreadsheet.getCellByPosition(7, row);
                xCell.setValue(n.doubleValue());

                /*

                xCell = xSpreadsheet.getCellByPosition(2, row);
                xCell.setFormula(hm.get("rzm_print").toString().trim());

                xCell = xSpreadsheet.getCellByPosition(3, row);
                xCell.setFormula(hm.get("srt").toString());

                xCell = xSpreadsheet.getCellByPosition(4, row);
                xCell.setFormula(hm.get("cenaBS").toString());

                xCell = xSpreadsheet.getCellByPosition(5, row);
                xCell.setFormula(hm.get("skidka").toString());


                xCell = xSpreadsheet.getCellByPosition(6, row);
                xCell.setFormula(hm.get("cena").toString());

                xCell = xSpreadsheet.getCellByPosition(7, row);
                xCell.setFormula(hm.get("nds").toString());

                double skidka = Double.valueOf(hm.get("skidka").toString());
                double cost = Double.valueOf(hm.get("cena").toString());

                double nds = Integer.valueOf(hm.get("nds").toString());
                double cost_with_nds = (cost + (cost * nds / 100));

                n = new BigDecimal(cost_with_nds);
                n = n.setScale(0, BigDecimal.ROUND_HALF_UP);

                xCell = xSpreadsheet.getCellByPosition(8, row);
                xCell.setValue(n.doubleValue());
                */

                xCell = xSpreadsheet.getCellByPosition(8, row);
                xCell.setFormula(REPUBLIC);

                xCell = xSpreadsheet.getCellByPosition(9, row);
                xCell.setFormula(COMPANY_NAME);

                row++;
                rowFormat(xSpreadsheet, row - 1);
            }

            // rowFormat(xSpreadsheet, row);

            xCell = xSpreadsheet.getCellByPosition(0, row + 3);
            xCell.setFormula(COMPANY_NAME + ": _________________/_________________");
            xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime
                    .queryInterface(com.sun.star.beans.XPropertySet.class,
                            xCell);
            xPropSet.setPropertyValue("HoriJustify", CellHoriJustify.LEFT);

            xCell = xSpreadsheet.getCellByPosition(6, row + 3);
            xCell.setFormula("\"Белпочта\": _________________/ Т.Н. Гвоздева");
            xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime
                    .queryInterface(com.sun.star.beans.XPropertySet.class,
                            xCell);
            xPropSet.setPropertyValue("HoriJustify", CellHoriJustify.LEFT);


            xCell = xSpreadsheet.getCellByPosition(2, row + 5);
            xCell.setFormula("М.П.");
            xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime
                    .queryInterface(com.sun.star.beans.XPropertySet.class,
                            xCell);
            xPropSet.setPropertyValue("HoriJustify", CellHoriJustify.LEFT);

            xCell = xSpreadsheet.getCellByPosition(8, row + 5);
            xCell.setFormula("М.П.");
            xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime
                    .queryInterface(com.sun.star.beans.XPropertySet.class,
                            xCell);
            xPropSet.setPropertyValue("HoriJustify", CellHoriJustify.LEFT);


            xCell = xSpreadsheet.getCellByPosition(1, row + 7);
            xCell.setFormula("_______________");
            xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime
                    .queryInterface(com.sun.star.beans.XPropertySet.class,
                            xCell);
            xPropSet.setPropertyValue("HoriJustify", CellHoriJustify.CENTER);

            xCell = xSpreadsheet.getCellByPosition(7, row + 7);
            xCell.setFormula("_______________");
            xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime
                    .queryInterface(com.sun.star.beans.XPropertySet.class,
                            xCell);
            xPropSet.setPropertyValue("HoriJustify", CellHoriJustify.CENTER);

            xCell = xSpreadsheet.getCellByPosition(1, row + 8);
            xCell.setFormula("(дата)");
            xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime
                    .queryInterface(com.sun.star.beans.XPropertySet.class,
                            xCell);
            xPropSet.setPropertyValue("HoriJustify", CellHoriJustify.CENTER);

            xCell = xSpreadsheet.getCellByPosition(7, row + 8);
            xCell.setFormula("(дата)");
            xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime
                    .queryInterface(com.sun.star.beans.XPropertySet.class,
                            xCell);
            xPropSet.setPropertyValue("HoriJustify", CellHoriJustify.CENTER);


            saveAsDocument(currentDocument, savePath
                    + "pcs" + dis.get("ttn").toString() + ".ods", lParams);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            log.error("Ошибка при заполнении протокола согласования цен", e);
            JOptionPane.showMessageDialog(null,
                    "Ошибка при заполнении протокола согласования цен",
                    "Ошибка!!!", JOptionPane.ERROR_MESSAGE);
        } finally {
            sdb.disConn();
        }
    }

    private void protocolStandart(XComponent currentDocument) {
        result = new HashMap();
        HashMap<String, Object> hm = new HashMap();
        HashMap dis = (HashMap) discr.get(0);
        String sar = "";
        double razn = 0, raznBuf = 0, raznBuf1 = 0, roznCena = 0, cena = 0, tn = 0, ndsTN = 0, kol = 0;
        int lineStart = 0;
        float charSize = (float) 6.5;
        BigDecimal n;
        int st, stn;
        DecimalFormat df;
        Double sumtn = .0;
        Double sumtn_nds = .0;
        Double sumtn_rzn = .0;

        Double sum_stoim = .0;
        Double sum_cena = .0;
        Double sum_kol = .0;
        Double sum_nds = .0;
        Double sum_kgm = .0;
        Double sum_massa = .0;

        SkladDB sdb = new SkladDB();

        try {
            XSpreadsheetDocument xSpreadsheetDocument = (XSpreadsheetDocument) UnoRuntime
                    .queryInterface(XSpreadsheetDocument.class, currentDocument);
            XSpreadsheets xSpreadsheets = xSpreadsheetDocument.getSheets();
            // xSpreadsheets.insertNewByName("Приложение к ТТН №" +
            // dis.get("ttn"), (short)0);
            Object sheet = xSpreadsheets.getByName("Лист1");
            XSpreadsheet xSpreadsheet = (XSpreadsheet) UnoRuntime
                    .queryInterface(XSpreadsheet.class, sheet);

            XNamed xNamed = (XNamed) UnoRuntime.queryInterface(XNamed.class,
                    xSpreadsheet);
            xNamed.setName("Приложение к ТТН №" + dis.get("ttn"));
            result.put("ttn", dis.get("ttn"));

            com.sun.star.beans.XPropertySet xPropSet = null;
            com.sun.star.table.BorderLine aLine = new com.sun.star.table.BorderLine();
            aLine.Color = 0x000000;
            aLine.InnerLineWidth = (short) 0;
            aLine.LineDistance = (short) 0;
            aLine.OuterLineWidth = (short) 5;
            com.sun.star.table.TableBorder aBorder = new com.sun.star.table.TableBorder();
            aBorder.BottomLine = aLine;
            aBorder.LeftLine = aLine;
            aBorder.RightLine = aLine;
            aBorder.IsBottomLineValid = true;
            aBorder.IsLeftLineValid = true;
            aBorder.IsRightLineValid = true;

            String COMPANY_NAME = "ОАО \"8 Марта\"";
            String REPUBLIC = "РБ";
            System.out.println("Номер документа :" + dis.get("ttn").toString());
            int d_ = sdb.getDogovorIDTTN(dis.get("ttn").toString());
            DogovorInfo dogov = sdb.getClientDogovor(d_);
            String client = sdb.getClientNameByCode(dis.get("ttn").toString());

            SimpleDateFormat ft = new SimpleDateFormat("dd.MM.yyyy");
            String dateDoc = ft.format(dogov.getDataBegin());
            String dateDocEnd = ft.format(dogov.getDataEnd());

            //String dogovor = "К ТТН № "+ dis.get("ttn").toString().trim()+" от "+dis.get("date").toString();
            String dogovor = "к договору № " + dogov.getNumber().trim() + " от " + dateDoc + " г.";

            String dogovor1 = "действует с " + dateDoc + " по " + dateDocEnd + " гг.";


            XCell xCell = xSpreadsheet.getCellByPosition(7, 1);
            xCell.setFormula(dogov.getNumber().trim() + " от " + dateDoc + " г.");

            xCell = xSpreadsheet.getCellByPosition(0, 3);
            xCell.setFormula("между " + client.trim() + " и " + COMPANY_NAME);

            xCell = xSpreadsheet.getCellByPosition(2, 3);
            xCell.setFormula(dogovor);

            //xCell = xSpreadsheet.getCellByPosition(2, 4);
            //xCell.setFormula(dogovor1);

            // ----------------Заполнение тела документа
            int row = 11;
            lineStart = row;
            String eanCode = "";
            String sarNumber = "";
            String sort = "";
            int startRow = row;

            try {
                hm = (HashMap) data.get(0);
                eanCode = hm.get("rzm_print").toString();
                sarNumber = hm.get("sar").toString();
                sort = hm.get("srt").toString();
            } catch (Exception ex) {
                System.out.println("Ошибка получения ЕАН кода");
            }

            for (int i = 0; i < data.size(); i++) {
                hm = (HashMap) data.get(i);
                // Корректировка группы на размер
                if (!eanCode.equals(hm.get("rzm_print").toString())) {
                    // В пределах одного шифра артикула
                    if (!sarNumber.equals(hm.get("sar").toString())) {

                        XCellRange xVertCellRange = xSpreadsheet
                                .getCellRangeByPosition(1, startRow, 1, row);
                        com.sun.star.util.XMergeable xVertMerge = (com.sun.star.util.XMergeable) UnoRuntime
                                .queryInterface(com.sun.star.util.XMergeable.class,
                                        xVertCellRange);
                        xVertMerge.merge(true);
                        startRow = row + 1;
                    }
                    row++;
                    // Корректировка группы на сорт
                } else if (!sort.equals(hm.get("srt").toString())) {
                    // В пределах одного шифра артикула
                    if (!sarNumber.equals(hm.get("sar").toString())) {

                        XCellRange xVertCellRange = xSpreadsheet
                                .getCellRangeByPosition(1, startRow, 1, row);
                        com.sun.star.util.XMergeable xVertMerge = (com.sun.star.util.XMergeable) UnoRuntime
                                .queryInterface(com.sun.star.util.XMergeable.class,
                                        xVertCellRange);
                        xVertMerge.merge(true);
                        startRow = row + 1;
                    }
                    row++;
                }

                sarNumber = hm.get("sar").toString();
                eanCode = hm.get("rzm_print").toString();
                sort = hm.get("srt").toString();


                xCell = xSpreadsheet.getCellByPosition(0, row);
                xCell.setFormula(String.valueOf(row - 10));

                String nSar = hm.get("ngpr").toString() + "\n"
                        + hm.get("nar").toString() + "\nмодель "
                        + hm.get("fas").toString();

                xCell = xSpreadsheet.getCellByPosition(1, row);
                xCell.setFormula(nSar);

                xCell = xSpreadsheet.getCellByPosition(2, row);
                xCell.setFormula(hm.get("rzm_print").toString().trim());

                xCell = xSpreadsheet.getCellByPosition(3, row);
                xCell.setFormula(hm.get("srt").toString());

                xCell = xSpreadsheet.getCellByPosition(4, row);
                xCell.setFormula(hm.get("cenaBS").toString());

                xCell = xSpreadsheet.getCellByPosition(5, row);
                xCell.setFormula(hm.get("skidka").toString());


                xCell = xSpreadsheet.getCellByPosition(6, row);
                xCell.setFormula(hm.get("cena").toString());

                xCell = xSpreadsheet.getCellByPosition(7, row);
                xCell.setFormula(hm.get("nds").toString());

                double skidka = Double.valueOf(hm.get("skidka").toString());
                double cost = Double.valueOf(hm.get("cena").toString());

                double nds = Integer.valueOf(hm.get("nds").toString());
                double cost_with_nds = (cost + (cost * nds / 100));

                n = new BigDecimal(cost_with_nds);
                n = n.setScale(0, BigDecimal.ROUND_HALF_UP);

                xCell = xSpreadsheet.getCellByPosition(8, row);
                xCell.setValue(n.doubleValue());

                rowFormat(xSpreadsheet, row - 1);
            }

            rowFormat(xSpreadsheet, row);

            xCell = xSpreadsheet.getCellByPosition(0, row + 2);
            xCell.setFormula("Покупатель: ");
            xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime
                    .queryInterface(com.sun.star.beans.XPropertySet.class,
                            xCell);
            xPropSet.setPropertyValue("HoriJustify", CellHoriJustify.LEFT);


            xCell = xSpreadsheet.getCellByPosition(0, row + 3);
            xCell.setFormula(client.trim());
            xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime
                    .queryInterface(com.sun.star.beans.XPropertySet.class,
                            xCell);

            XCellRange xCellRange = xSpreadsheet
                    .getCellRangeByPosition(0, row + 5, 4, row + 5);

            com.sun.star.util.XMergeable xMerge = (com.sun.star.util.XMergeable) UnoRuntime
                    .queryInterface(com.sun.star.util.XMergeable.class,
                            xCellRange);
            xMerge.merge(true);
            xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime
                    .queryInterface(com.sun.star.beans.XPropertySet.class,
                            xCell);
            xPropSet.setPropertyValue("HoriJustify", CellHoriJustify.LEFT);

            xCell = xSpreadsheet.getCellByPosition(0, row + 5);
            xCell.setFormula("_________________________/______________/");

            xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime
                    .queryInterface(com.sun.star.beans.XPropertySet.class,
                            xCell);
            xPropSet.setPropertyValue("HoriJustify", CellHoriJustify.LEFT);

            xCell = xSpreadsheet.getCellByPosition(5, row + 2);
            xCell.setFormula("Поставщик: ");
            xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime
                    .queryInterface(com.sun.star.beans.XPropertySet.class,
                            xCell);
            xPropSet.setPropertyValue("HoriJustify", CellHoriJustify.LEFT);


            xCell = xSpreadsheet.getCellByPosition(5, row + 3);
            xCell.setFormula(COMPANY_NAME);

            xCellRange = xSpreadsheet
                    .getCellRangeByPosition(5, row + 5, 8, row + 5);

            xMerge = (com.sun.star.util.XMergeable) UnoRuntime
                    .queryInterface(com.sun.star.util.XMergeable.class,
                            xCellRange);
            xMerge.merge(true);
            xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime
                    .queryInterface(com.sun.star.beans.XPropertySet.class,
                            xCell);
            xPropSet.setPropertyValue("HoriJustify", CellHoriJustify.LEFT);


            xCell = xSpreadsheet.getCellByPosition(5, row + 5);
            xCell.setFormula("_________________________/______________/");

            xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime
                    .queryInterface(com.sun.star.beans.XPropertySet.class,
                            xCell);
            xPropSet.setPropertyValue("HoriJustify", CellHoriJustify.LEFT);


            saveAsDocument(currentDocument, savePath
                    + "pcs" + dis.get("ttn").toString() + ".ods", lParams);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            log.error("Ошибка при заполнении протокола согласования цен", e);
            JOptionPane.showMessageDialog(null,
                    "Ошибка при заполнении протокола согласования цен",
                    "Ошибка!!!", JOptionPane.ERROR_MESSAGE);
        } finally {
            sdb.disConn();
        }
    }

    private void rowFormat(XSpreadsheet sheet, int row) {
        float charSize = (float) 6.5;
        com.sun.star.table.BorderLine aLine = new com.sun.star.table.BorderLine();
        aLine.Color = 0x000000;
        aLine.InnerLineWidth = (short) 0;
        aLine.LineDistance = (short) 0;
        aLine.OuterLineWidth = (short) 5;

        com.sun.star.table.TableBorder aBorder = new com.sun.star.table.TableBorder();
        aBorder.BottomLine = aLine;
        aBorder.LeftLine = aLine;
        aBorder.RightLine = aLine;
        aBorder.IsBottomLineValid = true;
        aBorder.IsLeftLineValid = true;
        aBorder.IsRightLineValid = true;
        try {
            for (int z = 0; z < 10; z++) {
                XCell xCell = sheet.getCellByPosition(z, row);
                XPropertySet xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime
                        .queryInterface(com.sun.star.beans.XPropertySet.class,
                                xCell);
                xPropSet.setPropertyValue("CharHeight", charSize);
                xPropSet.setPropertyValue("TableBorder", aBorder);
                xPropSet.setPropertyValue("HoriJustify", com.sun.star.table.CellHoriJustify.CENTER);
                xPropSet.setPropertyValue("VertJustify", com.sun.star.table.CellVertJustify.TOP);
            }
        } catch (Exception e) {
            System.out.println("Ошибка форматирования строки");
        }
    }

    private void protocolGreen(XComponent currentDocument) {
        result = new HashMap();
        HashMap<String, Object> hm = new HashMap();
        HashMap dis = (HashMap) discr.get(0);
        String sar = "";
        double razn = 0, raznBuf = 0, raznBuf1 = 0, roznCena = 0, cena = 0, tn = 0, ndsTN = 0, kol = 0;
        int lineStart = 0;
        float charSize = (float) 6.5;
        BigDecimal n;
        int st, stn;
        DecimalFormat df;
        Double sumtn = .0;
        Double sumtn_nds = .0;
        Double sumtn_rzn = .0;

        Double sum_stoim = .0;
        Double sum_cena = .0;
        Double sum_kol = .0;
        Double sum_nds = .0;
        Double sum_kgm = .0;
        Double sum_massa = .0;

        SkladDB sdb = new SkladDB();

        try {
            XSpreadsheetDocument xSpreadsheetDocument = (XSpreadsheetDocument) UnoRuntime
                    .queryInterface(XSpreadsheetDocument.class, currentDocument);
            XSpreadsheets xSpreadsheets = xSpreadsheetDocument.getSheets();
            // xSpreadsheets.insertNewByName("Приложение к ТТН №" +
            // dis.get("ttn"), (short)0);
            Object sheet = xSpreadsheets.getByName("Лист1");
            XSpreadsheet xSpreadsheet = (XSpreadsheet) UnoRuntime
                    .queryInterface(XSpreadsheet.class, sheet);

            XNamed xNamed = (XNamed) UnoRuntime.queryInterface(XNamed.class,
                    xSpreadsheet);
            xNamed.setName("Приложение к ТТН №" + dis.get("ttn"));
            result.put("ttn", dis.get("ttn"));

            com.sun.star.beans.XPropertySet xPropSet = null;
            com.sun.star.table.BorderLine aLine = new com.sun.star.table.BorderLine();
            aLine.Color = 0x000000;
            aLine.InnerLineWidth = (short) 0;
            aLine.LineDistance = (short) 0;
            aLine.OuterLineWidth = (short) 5;
            com.sun.star.table.TableBorder aBorder = new com.sun.star.table.TableBorder();
            aBorder.BottomLine = aLine;
            aBorder.LeftLine = aLine;
            aBorder.RightLine = aLine;
            aBorder.IsBottomLineValid = true;
            aBorder.IsLeftLineValid = true;
            aBorder.IsRightLineValid = true;

            String COMPANY_NAME = "ОАО \"8 Марта\"";
            String REPUBLIC = "РБ";
            System.out.println("Номер документа :" + dis.get("ttn").toString());
            int d_ = sdb.getDogovorIDTTN(dis.get("ttn").toString());
            DogovorInfo dogov = sdb.getClientDogovor(d_);
            String client = sdb.getClientNameByCode(dis.get("ttn").toString());

            SimpleDateFormat ft = new SimpleDateFormat("dd.MM.yyyy");
            String dateDoc = ft.format(dogov.getDataBegin());
            String dateDocEnd = ft.format(dogov.getDataEnd());

            //String dogovor = "К ТТН № "+ dis.get("ttn").toString().trim()+" от "+dis.get("date").toString();
            String dogovor = "к договору № " + dogov.getNumber().trim() + " от " + dateDoc + " г.";

            String dogovor1 = "действует с " + dateDoc + " по " + dateDocEnd + "гг.";


            XCell xCell = xSpreadsheet.getCellByPosition(6, 4);
            xCell.setFormula(dogov.getNumber().trim());
//            xCell.setFormula(dogov.getNumber().trim() + " от " + dateDoc + " г.");


            xCell = xSpreadsheet.getCellByPosition(8, 4);
            xCell.setFormula(dateDoc + "г.");

            xCell = xSpreadsheet.getCellByPosition(3, 6);
            xCell.setFormula("между " + client.trim() + " и " + COMPANY_NAME);

            xCell = xSpreadsheet.getCellByPosition(3, 8);
            xCell.setFormula(dogovor1);

            //xCell = xSpreadsheet.getCellByPosition(2, 4);
            //xCell.setFormula(dogovor1);

            // ----------------Заполнение тела документа
            int row = 12;
            lineStart = row;

            for (int i = 0; i < data.size(); i++) {
                hm = (HashMap) data.get(i);

                xCell = xSpreadsheet.getCellByPosition(1, row);
                xCell.setFormula(String.valueOf(i + 1));

                xCell = xSpreadsheet.getCellByPosition(2, row);
                xCell.setFormula(hm.get("ean").toString());

                String nSar = hm.get("ngpr").toString() + "\n"
                        + hm.get("nar").toString() + "\nмодель "
                        + hm.get("fas").toString() + "\nРазмер:" + hm.get("rzm_print").toString().trim();

                xCell = xSpreadsheet.getCellByPosition(3, row);
                xCell.setFormula(nSar);

                /*
                XCellRange xCellRange = xSpreadsheet
                        .getCellRangeByPosition(3, row, 4, row);

                com.sun.star.util.XMergeable xMerge = (com.sun.star.util.XMergeable) UnoRuntime
                        .queryInterface(com.sun.star.util.XMergeable.class,
                                xCellRange);
                xMerge.merge(true);
                */

                xCell = xSpreadsheet.getCellByPosition(4, row);
                xCell.setFormula("РБ");

                sar = hm.get("sar").toString();

                xCell = xSpreadsheet.getCellByPosition(7, row);
                if (sar.startsWith("43") || sar.startsWith("48")
                        || sar.startsWith("47")) {
                    xCell.setFormula(hm.get("izm").toString());
                } else {
                    xCell.setFormula("шт.");
                }

                xCell = xSpreadsheet.getCellByPosition(5, row);
                xCell.setFormula(hm.get("narp").toString());

                xCell = xSpreadsheet.getCellByPosition(6, row);
                xCell.setFormula(hm.get("kol").toString());

                xCell = xSpreadsheet.getCellByPosition(8, row);
                xCell.setFormula(hm.get("cenaBS").toString());

                xCell = xSpreadsheet.getCellByPosition(9, row);
                xCell.setFormula(hm.get("skidka").toString());

                double skidka = Double.valueOf(hm.get("skidka").toString());
                double cost = Double.valueOf(hm.get("cena").toString());


                xCell = xSpreadsheet.getCellByPosition(10, row);
                xCell.setFormula(hm.get("cena").toString());

                xCell = xSpreadsheet.getCellByPosition(11, row);
                xCell.setFormula(hm.get("nds").toString());

                double nds = Integer.valueOf(hm.get("nds").toString());

                double cost_with_nds = (cost + (cost * nds / 100));

                n = new BigDecimal(cost_with_nds);
                n = n.setScale(0, BigDecimal.ROUND_HALF_UP);

                xCell = xSpreadsheet.getCellByPosition(12, row);
                xCell.setValue(n.doubleValue());

                xCell = xSpreadsheet.getCellByPosition(13, row);
                xCell.setFormula("-");
                row++;

                for (int z = 1; z < 14; z++) {
                    xCell = xSpreadsheet.getCellByPosition(z, row - 1);
                    xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime
                            .queryInterface(com.sun.star.beans.XPropertySet.class,
                                    xCell);
                    xPropSet.setPropertyValue("CharHeight", charSize);
                    xPropSet.setPropertyValue("TableBorder", aBorder);
                    if (z == 1 || z == 2 || (z >= 4 && z <= 13)) {
                        xCell = xSpreadsheet.getCellByPosition(z, row - 1);
                        xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime
                                .queryInterface(com.sun.star.beans.XPropertySet.class,
                                        xCell);
                        xPropSet.setPropertyValue("HoriJustify", CellHoriJustify.CENTER);
                        xPropSet.setPropertyValue("VertJustify", CellHoriJustify.CENTER);
                    }
                }

                XCellRange xCellRange = xSpreadsheet
                        .getCellRangeByPosition(0, row - 1, 0,
                                row - 1);
                setCustomHeight(xCellRange, 1300);

            }

            xCell = xSpreadsheet.getCellByPosition(1, row + 2);
            xCell.setFormula("Покупатель: ");
            xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime
                    .queryInterface(com.sun.star.beans.XPropertySet.class,
                            xCell);
            xPropSet.setPropertyValue("HoriJustify", CellHoriJustify.LEFT);


            xCell = xSpreadsheet.getCellByPosition(1, row + 3);
            xCell.setFormula(client.trim());
            xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime
                    .queryInterface(com.sun.star.beans.XPropertySet.class,
                            xCell);

            XCellRange xCellRange = xSpreadsheet
                    .getCellRangeByPosition(1, row + 5, 6, row + 5);

            com.sun.star.util.XMergeable xMerge = (com.sun.star.util.XMergeable) UnoRuntime
                    .queryInterface(com.sun.star.util.XMergeable.class,
                            xCellRange);
            xMerge.merge(true);
            xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime
                    .queryInterface(com.sun.star.beans.XPropertySet.class,
                            xCell);
            xPropSet.setPropertyValue("HoriJustify", CellHoriJustify.LEFT);

            xCell = xSpreadsheet.getCellByPosition(1, row + 5);
            xCell.setFormula("_________________________/______________/");

            xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime
                    .queryInterface(com.sun.star.beans.XPropertySet.class,
                            xCell);
            xPropSet.setPropertyValue("HoriJustify", CellHoriJustify.LEFT);

            xCell = xSpreadsheet.getCellByPosition(9, row + 2);
            xCell.setFormula("Поставщик: ");
            xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime
                    .queryInterface(com.sun.star.beans.XPropertySet.class,
                            xCell);
            xPropSet.setPropertyValue("HoriJustify", CellHoriJustify.LEFT);


            xCell = xSpreadsheet.getCellByPosition(9, row + 3);
            xCell.setFormula(COMPANY_NAME);

            xCellRange = xSpreadsheet
                    .getCellRangeByPosition(9, row + 5, 13, row + 5);

            xMerge = (com.sun.star.util.XMergeable) UnoRuntime
                    .queryInterface(com.sun.star.util.XMergeable.class,
                            xCellRange);
            xMerge.merge(true);
            xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime
                    .queryInterface(com.sun.star.beans.XPropertySet.class,
                            xCell);
            xPropSet.setPropertyValue("HoriJustify", CellHoriJustify.LEFT);


            xCell = xSpreadsheet.getCellByPosition(9, row + 5);
            xCell.setFormula("_________________________/______________/");

            xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime
                    .queryInterface(com.sun.star.beans.XPropertySet.class,
                            xCell);
            xPropSet.setPropertyValue("HoriJustify", CellHoriJustify.LEFT);


//xPropSet.setPropertyValue("VertJustify", com.sun.star.table.CellVertJustify.TOP);
            saveAsDocument(currentDocument, savePath
                    + "pcs" + dis.get("ttn").toString() + ".ods", lParams);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            log.error("Ошибка при заполнении протокола согласования цен", e);
            JOptionPane.showMessageDialog(null,
                    "Ошибка при заполнении протокола согласования цен",
                    "Ошибка!!!", JOptionPane.ERROR_MESSAGE);
        } finally {
            sdb.disConn();
        }

    }

    private void protocolTInvest(XComponent currentDocument) {
        result = new HashMap();
        HashMap<String, Object> hm = new HashMap();
        HashMap dis = (HashMap) discr.get(0);
        String sar = "";
        double razn = 0, raznBuf = 0, raznBuf1 = 0, roznCena = 0, cena = 0, tn = 0, ndsTN = 0, kol = 0;
        int lineStart = 0;
        float charSize = (float) 6.5;
        BigDecimal n;
        int st, stn;
        DecimalFormat df;
        Double sumtn = .0;
        Double sumtn_nds = .0;
        Double sumtn_rzn = .0;

        Double sum_stoim = .0;
        Double sum_cena = .0;
        Double sum_kol = .0;
        Double sum_nds = .0;
        Double sum_kgm = .0;
        Double sum_massa = .0;

        SkladDB sdb = new SkladDB();

        try {
            XSpreadsheetDocument xSpreadsheetDocument = (XSpreadsheetDocument) UnoRuntime
                    .queryInterface(XSpreadsheetDocument.class, currentDocument);
            XSpreadsheets xSpreadsheets = xSpreadsheetDocument.getSheets();
            // xSpreadsheets.insertNewByName("Приложение к ТТН №" +
            // dis.get("ttn"), (short)0);
            Object sheet = xSpreadsheets.getByName("Лист1");
            XSpreadsheet xSpreadsheet = (XSpreadsheet) UnoRuntime
                    .queryInterface(XSpreadsheet.class, sheet);

            XNamed xNamed = (XNamed) UnoRuntime.queryInterface(XNamed.class,
                    xSpreadsheet);
            xNamed.setName("Приложение к ТТН №" + dis.get("ttn"));
            result.put("ttn", dis.get("ttn"));

            com.sun.star.beans.XPropertySet xPropSet = null;
            com.sun.star.table.BorderLine aLine = new com.sun.star.table.BorderLine();
            aLine.Color = 0x000000;
            aLine.InnerLineWidth = (short) 0;
            aLine.LineDistance = (short) 0;
            aLine.OuterLineWidth = (short) 5;
            com.sun.star.table.TableBorder aBorder = new com.sun.star.table.TableBorder();
            aBorder.BottomLine = aLine;
            aBorder.LeftLine = aLine;
            aBorder.RightLine = aLine;
            aBorder.IsBottomLineValid = true;
            aBorder.IsLeftLineValid = true;
            aBorder.IsRightLineValid = true;

            String COMPANY_NAME = "ОАО \"8 Марта\"";
            String REPUBLIC = "РБ";
            System.out.println("Номер документа :" + dis.get("ttn").toString());
            int d_ = sdb.getDogovorIDTTN(dis.get("ttn").toString());
            DogovorInfo dogov = sdb.getClientDogovor(d_);
            String client = sdb.getClientNameByCode(dis.get("ttn").toString());

            SimpleDateFormat ft = new SimpleDateFormat("dd.MM.yyyy");
            String dateDoc = ft.format(dogov.getDataBegin());
            String dateDocEnd = ft.format(dogov.getDataEnd());

            //String dogovor = "К ТТН № "+ dis.get("ttn").toString().trim()+" от "+dis.get("date").toString();
            String dogovor = "к договору № " + dogov.getNumber().trim() + " от " + dateDoc + " г.";

            String dogovor1 = "срок действия с " + dateDoc + " по " + dateDocEnd + "гг.";


            XCell xCell = xSpreadsheet.getCellByPosition(10, 3);
            xCell.setFormula(dogov.getNumber().trim());
//            xCell.setFormula(dogov.getNumber().trim() + " от " + dateDoc + " г.");


            xCell = xSpreadsheet.getCellByPosition(12, 3);
            xCell.setFormula(dateDoc + "г.");

            xCell = xSpreadsheet.getCellByPosition(3, 9);
            xCell.setFormula("между " + client.trim() + " и " + COMPANY_NAME);

            xCell = xSpreadsheet.getCellByPosition(3, 11);
            xCell.setFormula(dogovor1);

            //xCell = xSpreadsheet.getCellByPosition(2, 4);
            //xCell.setFormula(dogovor1);

            // ----------------Заполнение тела документа
            int row = 15;
            lineStart = row;

            for (int i = 0; i < data.size(); i++) {
                hm = (HashMap) data.get(i);

                xCell = xSpreadsheet.getCellByPosition(1, row);
                xCell.setFormula(String.valueOf(i + 1));

                xCell = xSpreadsheet.getCellByPosition(2, row);
                xCell.setFormula(hm.get("ean").toString());

                String nSar = hm.get("ngpr").toString() + "\n"
                        + hm.get("nar").toString() + "\nмодель "
                        + hm.get("fas").toString() + "\nРазмер:" + hm.get("rzm_print").toString().trim();

                xCell = xSpreadsheet.getCellByPosition(3, row);
                xCell.setFormula(nSar);

                /*
                XCellRange xCellRange = xSpreadsheet
                        .getCellRangeByPosition(3, row, 4, row);

                com.sun.star.util.XMergeable xMerge = (com.sun.star.util.XMergeable) UnoRuntime
                        .queryInterface(com.sun.star.util.XMergeable.class,
                                xCellRange);
                xMerge.merge(true);
                */

                xCell = xSpreadsheet.getCellByPosition(4, row);
                xCell.setFormula("РБ");

                sar = hm.get("sar").toString();

                xCell = xSpreadsheet.getCellByPosition(7, row);
                if (sar.startsWith("43") || sar.startsWith("48")
                        || sar.startsWith("47")) {
                    xCell.setFormula(hm.get("izm").toString());
                } else {
                    xCell.setFormula("шт.");
                }

                xCell = xSpreadsheet.getCellByPosition(5, row);
                xCell.setFormula(hm.get("narp").toString());

                xCell = xSpreadsheet.getCellByPosition(6, row);
                xCell.setFormula(hm.get("kol").toString());

                xCell = xSpreadsheet.getCellByPosition(8, row);
                xCell.setFormula(hm.get("cenaBS").toString());

                xCell = xSpreadsheet.getCellByPosition(9, row);
                xCell.setFormula(hm.get("skidka").toString());

                double skidka = Double.valueOf(hm.get("skidka").toString());
                double cost = Double.valueOf(hm.get("cena").toString());


                xCell = xSpreadsheet.getCellByPosition(10, row);
                xCell.setFormula(hm.get("cena").toString());

                xCell = xSpreadsheet.getCellByPosition(11, row);
                xCell.setFormula(hm.get("nds").toString());

                double nds = Integer.valueOf(hm.get("nds").toString());

                double cost_with_nds = (cost + (cost * nds / 100));

                n = new BigDecimal(cost_with_nds);
                n = n.setScale(0, BigDecimal.ROUND_HALF_UP);

                xCell = xSpreadsheet.getCellByPosition(12, row);
                xCell.setValue(n.doubleValue());

                row++;

                for (int z = 1; z < 13; z++) {
                    xCell = xSpreadsheet.getCellByPosition(z, row - 1);
                    xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime
                            .queryInterface(com.sun.star.beans.XPropertySet.class,
                                    xCell);
                    xPropSet.setPropertyValue("CharHeight", charSize);
                    xPropSet.setPropertyValue("TableBorder", aBorder);
                    if (z == 1 || z == 2 || (z >= 4 && z <= 13)) {
                        xCell = xSpreadsheet.getCellByPosition(z, row - 1);
                        xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime
                                .queryInterface(com.sun.star.beans.XPropertySet.class,
                                        xCell);
                        xPropSet.setPropertyValue("HoriJustify", CellHoriJustify.CENTER);
                        xPropSet.setPropertyValue("VertJustify", CellHoriJustify.CENTER);
                    }
                }

                XCellRange xCellRange = xSpreadsheet
                        .getCellRangeByPosition(0, row - 1, 0,
                                row - 1);
                setCustomHeight(xCellRange, 1300);

            }

            xCell = xSpreadsheet.getCellByPosition(9, row + 2);
            xCell.setFormula("Подписи сторон: ");
            xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime
                    .queryInterface(com.sun.star.beans.XPropertySet.class,
                            xCell);
            xPropSet.setPropertyValue("HoriJustify", CellHoriJustify.LEFT);


            xCell = xSpreadsheet.getCellByPosition(1, row + 4);
            xCell.setFormula("Покупатель: ");
            xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime
                    .queryInterface(com.sun.star.beans.XPropertySet.class,
                            xCell);
            xPropSet.setPropertyValue("HoriJustify", CellHoriJustify.LEFT);


            xCell = xSpreadsheet.getCellByPosition(1, row + 5);
            xCell.setFormula(client.trim());
            xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime
                    .queryInterface(com.sun.star.beans.XPropertySet.class,
                            xCell);

            XCellRange xCellRange = xSpreadsheet
                    .getCellRangeByPosition(1, row + 7, 6, row + 7);

            com.sun.star.util.XMergeable xMerge = (com.sun.star.util.XMergeable) UnoRuntime
                    .queryInterface(com.sun.star.util.XMergeable.class,
                            xCellRange);
            xMerge.merge(true);
            xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime
                    .queryInterface(com.sun.star.beans.XPropertySet.class,
                            xCell);
            xPropSet.setPropertyValue("HoriJustify", CellHoriJustify.LEFT);

            xCell = xSpreadsheet.getCellByPosition(1, row + 7);
            xCell.setFormula("_________________________/______________/");

            xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime
                    .queryInterface(com.sun.star.beans.XPropertySet.class,
                            xCell);
            xPropSet.setPropertyValue("HoriJustify", CellHoriJustify.LEFT);

            xCell = xSpreadsheet.getCellByPosition(9, row + 4);
            xCell.setFormula("Поставщик: ");
            xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime
                    .queryInterface(com.sun.star.beans.XPropertySet.class,
                            xCell);
            xPropSet.setPropertyValue("HoriJustify", CellHoriJustify.LEFT);


            xCell = xSpreadsheet.getCellByPosition(9, row + 5);
            xCell.setFormula(COMPANY_NAME);

            xCellRange = xSpreadsheet
                    .getCellRangeByPosition(9, row + 7, 12, row + 7);

            xMerge = (com.sun.star.util.XMergeable) UnoRuntime
                    .queryInterface(com.sun.star.util.XMergeable.class,
                            xCellRange);
            xMerge.merge(true);
            xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime
                    .queryInterface(com.sun.star.beans.XPropertySet.class,
                            xCell);
            xPropSet.setPropertyValue("HoriJustify", CellHoriJustify.LEFT);


            xCell = xSpreadsheet.getCellByPosition(9, row + 7);
            xCell.setFormula("_________________________/______________/");

            xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime
                    .queryInterface(com.sun.star.beans.XPropertySet.class,
                            xCell);
            xPropSet.setPropertyValue("HoriJustify", CellHoriJustify.LEFT);


//xPropSet.setPropertyValue("VertJustify", com.sun.star.table.CellVertJustify.TOP);
            saveAsDocument(currentDocument, savePath
                    + "pcs" + dis.get("ttn").toString() + ".ods", lParams);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            log.error("Ошибка при заполнении протокола согласования цен", e);
            JOptionPane.showMessageDialog(null,
                    "Ошибка при заполнении протокола согласования цен",
                    "Ошибка!!!", JOptionPane.ERROR_MESSAGE);
        } finally {
            sdb.disConn();
        }

    }


    private void setCustomHeight(XCellRange range, int width) {
        com.sun.star.table.XColumnRowRange xColRowRange = (com.sun.star.table.XColumnRowRange)
                UnoRuntime.queryInterface(com.sun.star.table.XColumnRowRange.class, range);
        com.sun.star.table.XTableRows xRows = xColRowRange.getRows();
        try {
            Object aRowObj = xRows.getByIndex(0);
            XPropertySet xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime.queryInterface(
                    com.sun.star.beans.XPropertySet.class, aRowObj);
            xPropSet.setPropertyValue("Height", new Integer(width));
        } catch (Exception e) {
            System.out.println("Ошибка");
        }
    }

}
