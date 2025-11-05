package dept.production.planning.ean;

import by.march8.ecs.framework.common.LogCrutch;
import com.sun.star.lang.XComponent;
import com.sun.star.sheet.XSpreadsheet;
import com.sun.star.sheet.XSpreadsheetDocument;
import com.sun.star.sheet.XSpreadsheets;
import com.sun.star.table.XCell;
import com.sun.star.uno.UnoRuntime;
import dept.MyReportsModule;
import dept.production.planning.PlanDB;
import workOO.OO_new;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;
import java.util.Vector;
import java.util.stream.Collectors;

import static dept.production.planning.ean.UtilEan.getGenderMap;


/**
 *
 * @author lidashka
 */
public class EanOO extends OO_new {
    static final String[] TNVED = {"6106", "420310000", "6105", "4304000000", "611300", "6210", "6101", "6102", "6205", "6206", "6214", "6215", "6211200000", "621132", "621133", "6211390000", "621142", "621143", "621149000", "6103", "6104", "6203", "6204", "6112110000", "6112120000", "6112190000", "6112200000", "6110", "6107", "6207", "6108", "6208", "6109", "6111", "6209", "611231", "611239", "611241", "611249", "6211110000", "6211120000", "6115", "4203210000", "420329", "6116", "6216000000", "6114", "6117100000", "6117801009", "6117808009", "6217100000", "6213", "6117808001", "6212", "6406909000", "6504000000", "650500", "650699"};
    private static final LogCrutch log = new LogCrutch();
    static String nameTamplates;
    static String dateTamplates;
    static DefaultTableModel tModel;
    static TableColumnModel tModelCol;
    static ArrayList<EanList> list;
    static String path;
    static List<String> paths;
    static ArrayList<EanItem> items;
    static ArrayList<Object> objects;
    static EanDB edb;
    static Vector sar;
    static String shifrArt = "1N";
    static String shifrWight = "KG";

    public EanOO(String path) {
        EanOO.path = path;
    }

    public EanOO(List<String> paths) {
        EanOO.paths = paths;
    }

    public EanOO(String nameReport, DefaultTableModel tm, TableColumnModel tc) {
        tModel = tm;
        tModelCol = tc;
        nameTamplates = nameReport;
    }

    public EanOO(String nameReport, String dateReport, ArrayList<EanItem> items) {
        EanOO.items = items;
        nameTamplates = nameReport;
        dateTamplates = dateReport;
    }

    public EanOO(String nameReport, ArrayList<Object> objects, String dateReport) {
        EanOO.objects = objects;
        nameTamplates = nameReport;
        dateTamplates = dateReport;
    }

    public void createReport(String nameTamplates, boolean flagSave) throws Exception {
        try {
            connect();
            XComponent currentDocument = openDocumentOld("Templates/" + nameTamplates);

            if (nameTamplates.equals("DefaultTableBookFormatCheck.ots")) {
                fildTableDefaultCheck(currentDocument);
            } else if (nameTamplates.equals("DefaultTableAlbumFormatCheck.ots")) {
                fildTableDefaultCheck(currentDocument);
            } else if (nameTamplates.equals("ZayavkaEanZO.ots")) {
                fildZayavkaEanZOSeparateDocuments(currentDocument, openDocumentOld("Templates/" + "ZayavkaEanZOGeneral.ots"), flagSave);
            } else if (nameTamplates.equals("ZayavkaEan.ots")) {
                fildZayavkaEanZO(currentDocument, flagSave);
            } else if (nameTamplates.equals("ZayavkaEanSpisok.ots")) {
                //   fildZayavkaEanSpisok(currentDocument, flagSave);
            } else if (nameTamplates.equals("ZayavkaEanImportDB.ots")) {
                fildZayavkaEanImportDB(currentDocument, flagSave);
            } else if (nameTamplates.equals("EanImportDB.ots")) {
                fieldResultImportDB(currentDocument, flagSave);
            } else if (nameTamplates.equals("ZayavkaEanByMarshList.ots")) {
                fieldEanByMarshList(currentDocument);
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

    public ArrayList<EanItem> openReport(EanForm dialog) throws Exception {
        ArrayList<EanItem> eanItems = new ArrayList<>();
        try {
            connect();
            XComponent currentDocument = openDocument(path);

            eanItems = openZayavkaEanZO(currentDocument);

            dialog.toFront();

        } catch (java.lang.Exception e) {
            eanItems = new ArrayList<>();
            System.out.println(e.getMessage());
            throw new Exception("Ошибка просмотра отчёта ", e);
        }
        return eanItems;
    }

    public ArrayList<EanItem> openReports(EanForm dialog) throws Exception {
        final List<EanItem> eanItems = new ArrayList<>();
//        try {
        if (paths.size() > 2) {
            throw new Exception("Ошибка выбрано более 2-х файлов");
        }
        connect();
        paths.stream().forEach(el -> {
            try {
                XComponent currentDocument = openDocument(el);
                eanItems.addAll(openZayavkaEanZO(currentDocument));
            } catch (Exception e) {
                System.out.println(e.getMessage());
                try {
                    throw new Exception("Ошибка просмотра отчёта ", e);
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
        });


//            XComponent currentDocument = openDocument(paths.get(0));
//
//            eanItems = openZayavkaEanZO(currentDocument);
//
//            dialog.toFront();

//        }catch (java.lang.Exception e){
//            eanItems = new ArrayList<>();
//            System.out.println(e.getMessage());
//            throw new Exception("Ошибка просмотра отчёта ", e);
//        }
        return (ArrayList) eanItems;
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
            for (int i = 0; i < tModel.getColumnCount(); i++) {
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
                        for (int i = 0; i < tModel.getColumnCount(); i++) {
                            if (tModelCol.getColumn(i).getWidth() > 0) {
                                xCell = xSpreadsheet.getCellByPosition(nCol++, nRow);
                                xCell.setFormula(((Vector) row).get(i) != null ?
                                        ((Vector) row).get(i).toString().replace("<html>", "").replace("</html>", "").replace("<br>", "") :
                                        "");
                                xPropSet = UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
                                xPropSet.setPropertyValue("TableBorder", aBorder);
                            }
                        }
                        nCol = 0;
                        nRow++;
                    }
                } else {
                    for (int i = 0; i < tModel.getColumnCount(); i++) {
                        if (tModelCol.getColumn(i).getWidth() > 0) {
                            xCell = xSpreadsheet.getCellByPosition(nCol++, nRow);
                            xCell.setFormula(((Vector) row).get(i) != null ?
                                    ((Vector) row).get(i).toString().replace("<html>", "").replace("</html>", "").replace("<br>", "") :
                                    "");
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

    private void fildZayavkaEanZO(XComponent currentDocument, boolean flagSave) throws Exception {
        try {
            edb = new EanDB();
            XSpreadsheetDocument xSpreadsheetDocument = UnoRuntime.queryInterface(XSpreadsheetDocument.class, currentDocument);
            XSpreadsheets xSpreadsheets = xSpreadsheetDocument.getSheets();
            Object sheet = xSpreadsheets.getByName("Товарная база");
            XSpreadsheet xSpreadsheet = UnoRuntime.queryInterface(XSpreadsheet.class, sheet);


            XCell xCell;

            //----------------Заполнение шапки документа
            xCell = xSpreadsheet.getCellByPosition(12, 0);
            xCell.setFormula("В Ассоциацию автоматической идентификации ГС1 Бел.\n" +
                    "В Государственное предприятие «Центр систем идентификации»\n" +
                    "Наименование заказчика: " + UtilEan.EIGHT_MARCH + "\n" +
                    "GLN-номер Заказчика, заключившего договор на получение GTIN: " + UtilEan.GLN + "\n" +
                    "Дата подачи заявки: " + dateTamplates + "г.\n" +
                    "Заявку подготовил(а): " + UtilEan.FACTORY_EMPL + " " + UtilEan.FACTORY_TEL);


            xCell = xSpreadsheet.getCellByPosition(10, 2);
            String tmp = xCell.getFormula();
            xCell.setFormula("ЗАЯВКА " + nameTamplates + tmp);

            //----------------Заполнение тела документа

            int nCol = 10;
            int nRow = 5;

            List<Integer> models = items.stream().map(item -> item.getFasNum()).collect(Collectors.toList());
            PlanDB db = new PlanDB();
            for (EanItem item : items) {
                for (EanItemListSize listSize : item.getDataSize()) {
                    //System.out.println(item.getColorNum()+ " "+item.getColorName() + " "+item.getFasSrt()+"  "+listSize.getSizePrint());

                    nRow++;

                    xCell = xSpreadsheet.getCellByPosition(10, nRow);
                    xCell.setFormula(item.getFasName().trim().toLowerCase());

                    xCell = xSpreadsheet.getCellByPosition(11, nRow);
                    xCell.setFormula("8 Марта");

                    xCell = xSpreadsheet.getCellByPosition(13, nRow);
                    xCell.setFormula("Модель " + item.getFasNum() +
                            ", артикул " + item.getFasNar().trim().toLowerCase() +
                            ", размер " + listSize.getSizePrint() +
                            ", сорт " + UtilEan.getSrtIzd(item.getFasSrt()) +
                            ", " + item.getUpacText().trim().toLowerCase() +
                            (item.getColorName().trim().length() > 0 ?
                                    ", " + item.getColorName() :
                                    "")
                    );

                    xCell = xSpreadsheet.getCellByPosition(14, nRow);
                    xCell.setFormula("ГОСТ " + item.getTextGOST().trim().toUpperCase());

                    sar = edb.getSARFromKLD(String.valueOf(item.getFasNum()), item.getFasNar());
                    shifrArt = "1N";
                    if (sar.size() > 0 && (
                            ((Integer) ((Vector) sar.get(0)).get(1)).toString().startsWith("4314")
                                    || ((Integer) ((Vector) sar.get(0)).get(1)).toString().startsWith("4324")
                                    || ((Integer) ((Vector) sar.get(0)).get(1)).toString().startsWith("4331")
                                    || ((Integer) ((Vector) sar.get(0)).get(1)).toString().startsWith("4333")
                                    || ((Integer) ((Vector) sar.get(0)).get(1)).toString().startsWith("4334")
                    )) {
                        shifrArt = "PR";
                    }

                    xCell = xSpreadsheet.getCellByPosition(21, nRow);
                    xCell.setFormula(shifrArt);

                    xCell = xSpreadsheet.getCellByPosition(22, nRow);
                    xCell.setValue(item.getKolX());

                    xCell = xSpreadsheet.getCellByPosition(23, nRow);
                    xCell.setFormula(shifrWight);

                    xCell = xSpreadsheet.getCellByPosition(24, nRow);
                    xCell.setFormula(item.getDataSize().get(0).getWeight());

                    xCell = xSpreadsheet.getCellByPosition(25, nRow);
                    xCell.setFormula(item.getDataSize().get(0).getWeight());

                    xCell = xSpreadsheet.getCellByPosition(29, nRow);
                    xCell.setFormula(item.getTextOKRB());

                    xCell = xSpreadsheet.getCellByPosition(30, nRow);
                    xCell.setValue(item.getTextGpcSeg());

                    xCell = xSpreadsheet.getCellByPosition(31, nRow);
                    xCell.setValue(item.getTextGpcSem());

                    xCell = xSpreadsheet.getCellByPosition(32, nRow);
                    xCell.setValue(item.getTextGpcKl());

                    xCell = xSpreadsheet.getCellByPosition(33, nRow);
                    xCell.setValue(item.getTextGpcBr());

                    xCell = xSpreadsheet.getCellByPosition(35, nRow);
                    xCell.setFormula(item.getTextTHB());

                    xCell = xSpreadsheet.getCellByPosition(40, nRow);
                    xCell.setFormula("Базовый уровень (товар нельзя разделить на другие товары) [BASE_UNIT_OR_EACH]");

                    xCell = xSpreadsheet.getCellByPosition(41, nRow);
                    xCell.setFormula("потребительской единицей (Consumer unit) [CU]");

                    xCell = xSpreadsheet.getCellByPosition(43, nRow);
                    xCell.setFormula("Беларусь [112]");

                    xCell = xSpreadsheet.getCellByPosition(44, nRow);
                    xCell.setFormula("Беларусь [112]");

                    xCell = xSpreadsheet.getCellByPosition(45, nRow);
                    xCell.setFormula("Беларусь [112]");

                    xCell = xSpreadsheet.getCellByPosition(46, nRow);
                    xCell.setFormula(UtilEan.GLN);

                    xCell = xSpreadsheet.getCellByPosition(47, nRow);
                    xCell.setFormula(UtilEan.EIGHT_MARCH);

                    xCell = xSpreadsheet.getCellByPosition(48, nRow);
                    xCell.setFormula(UtilEan.GLN);

                    xCell = xSpreadsheet.getCellByPosition(49, nRow);
                    xCell.setFormula(UtilEan.FACTORY_NAME);

                    xCell = xSpreadsheet.getCellByPosition(49, nRow);
                    xCell.setFormula(UtilEan.FACTORY_NAME);

                    xCell = xSpreadsheet.getCellByPosition(55, nRow);
                    xCell.setFormula(db.getCompositionsByParameters(item.getFasNum(), item.getFasSar(), item.getFasNar()));

                    xCell = xSpreadsheet.getCellByPosition(63, nRow);
                    xCell.setValue(1);

                    xCell = xSpreadsheet.getCellByPosition(64, nRow);
                    xCell.setValue(12);

                    xCell = xSpreadsheet.getCellByPosition(66, nRow);
                    xCell.setFormula("Пакет [PA]");

                    xCell = xSpreadsheet.getCellByPosition(95, nRow);
                    xCell.setFormula(item.getFasName().trim().toLowerCase());

                    xCell = xSpreadsheet.getCellByPosition(96, nRow);
                    xCell.setFormula(item.getFasName().trim().toLowerCase() +
                            ", модель " + item.getFasNum() +
                            ", артикул " + item.getFasNar().trim().toLowerCase() +
                            (item.getColorName().trim().length() > 0 ?
                                    ", цвет " + item.getColorName() :
                                    "") +
                            ", размер " + listSize.getSizePrint() +
                            ", сорт " + UtilEan.getSrtIzd(item.getFasSrt())
                    );

                    xCell = xSpreadsheet.getCellByPosition(97, nRow);
                    xCell.setFormula(listSize.getSizePrint());

                    xCell = xSpreadsheet.getCellByPosition(98, nRow);
                    xCell.setFormula(item.getColorName());

                    xCell = xSpreadsheet.getCellByPosition(99, nRow);
                    String gender = "";
                    if (Objects.nonNull(item.getFasSar()) && item.getFasSar() > 0) {
                        Integer code = Integer.valueOf(item.getFasSar().toString().substring(0, 3));
                        gender = getGenderMap().getOrDefault(code, "");
                    } else {
                        Integer sar = edb.getSarByFasAndNAr(item.getFasNum(), item.getFasNar());
                        if (Objects.nonNull(sar) && sar > 0) {
                            Integer code = Integer.valueOf(sar.toString().substring(0, 3));
                            gender = getGenderMap().getOrDefault(code, "");
                        }
                    }
                    if (gender.equals("")) {
                        if (item.getFasName().trim().toLowerCase().contains("муж") || item.getFasName().trim().toLowerCase().contains("мал")) {
                            gender = "Мужской";
                        }
                        if (item.getFasName().trim().toLowerCase().contains("жен") || item.getFasName().trim().toLowerCase().contains("дев")) {
                            gender = "Женский";
                        }
                    }
                    xCell.setFormula(gender.equals("") ? "Унисекс" : gender);

                    xCell = xSpreadsheet.getCellByPosition(100, nRow);
                    xCell.setFormula(Integer.toString(item.getFasNum()));

                    xCell = xSpreadsheet.getCellByPosition(101, nRow);
                    xCell.setFormula("ТР ТС 017/2011 О БЕЗОПАСНОСТИ ПРОДУКЦИИ ЛЕГКОЙ ПРОМЫШЛЕННОСТИ");

                    xCell = xSpreadsheet.getCellByPosition(102, nRow);
                    xCell.setFormula("Беларусь [112]");

                    xCell = xSpreadsheet.getCellByPosition(103, nRow);
                    xCell.setFormula("400078265");
                }
            }

            if (flagSave) {
                com.sun.star.beans.PropertyValue[] lParams = new com.sun.star.beans.PropertyValue[0];
                String savePath = MyReportsModule.confPath + UtilEan.PATH_EANLIST;
                saveAsDocument(currentDocument, savePath + "/" + nameTamplates + ".xls", lParams);
            }

        } catch (java.lang.Exception e) {
            log.error("Ошибка при заполнении отчёта.", e);
            throw new Exception("Ошибка при заполнении отчёта.", e);
        }
    }

    private void fildZayavkaEanZOSeparateDocuments(XComponent firstCurrentDocument, XComponent secondCurrentDocument, boolean flagSave) throws Exception {
        try {
            edb = new EanDB();
            XSpreadsheet xSpreadsheet;

            XSpreadsheetDocument firstxSpreadsheetDocument = UnoRuntime.queryInterface(XSpreadsheetDocument.class, firstCurrentDocument);
            XSpreadsheets firstxSpreadsheets = firstxSpreadsheetDocument.getSheets();
            Object firstSheet = firstxSpreadsheets.getByName("Товарная база");
            XSpreadsheet firstxSpreadsheet = UnoRuntime.queryInterface(XSpreadsheet.class, firstSheet);

            XSpreadsheetDocument secondxSpreadsheetDocument = UnoRuntime.queryInterface(XSpreadsheetDocument.class, secondCurrentDocument);
            XSpreadsheets secondxSpreadsheets = secondxSpreadsheetDocument.getSheets();
            Object secondSheet = secondxSpreadsheets.getByName("Товарная база");
            XSpreadsheet secondxSpreadsheet = UnoRuntime.queryInterface(XSpreadsheet.class, secondSheet);

            XCell xCell;
            XCell firstCell;
            XCell secondCell;

            //----------------Заполнение шапки документа
            firstCell = firstxSpreadsheet.getCellByPosition(12, 0);
            firstCell.setFormula("В Ассоциацию автоматической идентификации ГС1 Бел.\n" +
                    "В Государственное предприятие «Центр систем идентификации»\n" +
                    "Наименование заказчика: " + UtilEan.EIGHT_MARCH + "\n" +
                    "GLN-номер Заказчика, заключившего договор на получение GTIN: " + UtilEan.GLN + "\n" +
                    "Дата подачи заявки: " + dateTamplates + "г.\n" +
                    "Заявку подготовил(а): " + UtilEan.FACTORY_EMPL + " " + UtilEan.FACTORY_TEL);


            firstCell = firstxSpreadsheet.getCellByPosition(10, 2);
            String tmp = firstCell.getFormula();
            firstCell.setFormula("ЗАЯВКА " + nameTamplates + tmp);

            //----------------Заполнение тела документа
            // ----------------Заполнение шапки документа
            secondCell = secondxSpreadsheet.getCellByPosition(12, 0);
            secondCell.setFormula("В Ассоциацию автоматической идентификации ГС1 Бел.\n" +
                    "В Государственное предприятие «Центр систем идентификации»\n" +
                    "Наименование заказчика: " + UtilEan.EIGHT_MARCH + "\n" +
                    "GLN-номер Заказчика, заключившего договор на получение GTIN: " + UtilEan.GLN + "\n" +
                    "Дата подачи заявки: " + dateTamplates + "г.\n" +
                    "Заявку подготовил(а): " + UtilEan.FACTORY_EMPL + " " + UtilEan.FACTORY_TEL);


            secondCell = secondxSpreadsheet.getCellByPosition(10, 2);
            tmp = secondCell.getFormula();
            secondCell.setFormula("ЗАЯВКА " + nameTamplates + tmp);

            //----------------Заполнение тела документа

            int nCol = 10;
            Integer nRow;
            Integer firstRow = 5;
            Integer secondRow = 5;

            List<Integer> models = items.stream().map(item -> item.getFasNum()).collect(Collectors.toList());
            PlanDB db = new PlanDB();

            for (EanItem item : items) {
                boolean isExceptedTNVED = Arrays.asList(TNVED).stream().anyMatch(el -> item.getTextTHB().startsWith(el));
                if (isExceptedTNVED) {
                    xSpreadsheet = firstxSpreadsheet;
                    nRow = firstRow;
                } else {
                    xSpreadsheet = secondxSpreadsheet;
                    nRow = secondRow;
                }

                for (EanItemListSize listSize : item.getDataSize()) {
                    nRow++;

                    xCell = xSpreadsheet.getCellByPosition(10, nRow);
                    xCell.setFormula(item.getFasName().trim().toLowerCase());

                    xCell = xSpreadsheet.getCellByPosition(11, nRow);
                    xCell.setFormula("8 Марта");

                    xCell = xSpreadsheet.getCellByPosition(13, nRow);
                    xCell.setFormula("Модель " + item.getFasNum() +
                            ", артикул " + item.getFasNar().trim().toLowerCase() +
                            ", размер " + listSize.getSizePrint() +
                            ", сорт " + UtilEan.getSrtIzd(item.getFasSrt()) +
                            ", " + item.getUpacText().trim().toLowerCase() +
                            (item.getColorName().trim().length() > 0 ?
                                    ", " + item.getColorName() :
                                    "")
                    );

                    xCell = xSpreadsheet.getCellByPosition(14, nRow);
                    xCell.setFormula("ГОСТ " + item.getTextGOST().trim().toUpperCase());

                    sar = edb.getSARFromKLD(String.valueOf(item.getFasNum()), item.getFasNar());
                    shifrArt = "1N";
                    if (sar.size() > 0 && (
                            ((Integer) ((Vector) sar.get(0)).get(1)).toString().startsWith("4314")
                                    || ((Integer) ((Vector) sar.get(0)).get(1)).toString().startsWith("4324")
                                    || ((Integer) ((Vector) sar.get(0)).get(1)).toString().startsWith("4331")
                                    || ((Integer) ((Vector) sar.get(0)).get(1)).toString().startsWith("4333")
                                    || ((Integer) ((Vector) sar.get(0)).get(1)).toString().startsWith("4334")
                    )) {
                        shifrArt = "PR";
                    }

                    xCell = xSpreadsheet.getCellByPosition(21, nRow);
                    xCell.setFormula(shifrArt);

                    xCell = xSpreadsheet.getCellByPosition(22, nRow);
                    xCell.setValue(item.getKolX());

                    xCell = xSpreadsheet.getCellByPosition(23, nRow);
                    xCell.setFormula(shifrWight);

                    xCell = xSpreadsheet.getCellByPosition(24, nRow);
                    xCell.setFormula(item.getDataSize().get(0).getWeight());

                    xCell = xSpreadsheet.getCellByPosition(25, nRow);
                    xCell.setFormula(item.getDataSize().get(0).getWeight());

                    xCell = xSpreadsheet.getCellByPosition(29, nRow);
                    xCell.setFormula(item.getTextOKRB());

                    xCell = xSpreadsheet.getCellByPosition(30, nRow);
                    xCell.setValue(item.getTextGpcSeg());

                    xCell = xSpreadsheet.getCellByPosition(31, nRow);
                    xCell.setValue(item.getTextGpcSem());

                    xCell = xSpreadsheet.getCellByPosition(32, nRow);
                    xCell.setValue(item.getTextGpcKl());

                    xCell = xSpreadsheet.getCellByPosition(33, nRow);
                    xCell.setValue(item.getTextGpcBr());

                    xCell = xSpreadsheet.getCellByPosition(35, nRow);
                    xCell.setFormula(item.getTextTHB());

                    xCell = xSpreadsheet.getCellByPosition(40, nRow);
                    xCell.setFormula("Базовый уровень (товар нельзя разделить на другие товары) [BASE_UNIT_OR_EACH]");

                    xCell = xSpreadsheet.getCellByPosition(41, nRow);
                    xCell.setFormula("потребительской единицей (Consumer unit) [CU]");

                    xCell = xSpreadsheet.getCellByPosition(43, nRow);
                    xCell.setFormula("Беларусь [112]");

                    xCell = xSpreadsheet.getCellByPosition(44, nRow);
                    xCell.setFormula("Беларусь [112]");

                    xCell = xSpreadsheet.getCellByPosition(45, nRow);
                    xCell.setFormula("Беларусь [112]");

                    xCell = xSpreadsheet.getCellByPosition(46, nRow);
                    xCell.setFormula(UtilEan.GLN);

                    xCell = xSpreadsheet.getCellByPosition(47, nRow);
                    xCell.setFormula(UtilEan.EIGHT_MARCH);

                    xCell = xSpreadsheet.getCellByPosition(48, nRow);
                    xCell.setFormula(UtilEan.GLN);

                    xCell = xSpreadsheet.getCellByPosition(49, nRow);
                    xCell.setFormula(UtilEan.FACTORY_NAME);

                    xCell = xSpreadsheet.getCellByPosition(49, nRow);
                    xCell.setFormula(UtilEan.FACTORY_NAME);

                    xCell = xSpreadsheet.getCellByPosition(55, nRow);
                    xCell.setFormula(db.getCompositionsByParameters(item.getFasNum(), item.getFasSar(), item.getFasNar()));

                    xCell = xSpreadsheet.getCellByPosition(63, nRow);
                    xCell.setValue(1);

                    xCell = xSpreadsheet.getCellByPosition(64, nRow);
                    xCell.setValue(12);

                    xCell = xSpreadsheet.getCellByPosition(66, nRow);
                    xCell.setFormula("Пакет [PA]");
                    if (isExceptedTNVED) {
                        xCell = xSpreadsheet.getCellByPosition(95, nRow);
                        xCell.setFormula(item.getFasName().trim().toLowerCase());

                        xCell = xSpreadsheet.getCellByPosition(96, nRow);
                        xCell.setFormula(item.getFasName().trim().toLowerCase() +
                                ", модель " + item.getFasNum() +
                                ", артикул " + item.getFasNar().trim().toLowerCase() +
                                (item.getColorName().trim().length() > 0 ?
                                        ", цвет " + item.getColorName() :
                                        "") +
                                ", размер " + listSize.getSizePrint() +
                                ", сорт " + UtilEan.getSrtIzd(item.getFasSrt())
                        );

                        xCell = xSpreadsheet.getCellByPosition(97, nRow);
                        xCell.setFormula(listSize.getSizePrint());

                        xCell = xSpreadsheet.getCellByPosition(98, nRow);
                        xCell.setFormula(item.getColorName());

                        xCell = xSpreadsheet.getCellByPosition(99, nRow);
                        String gender = "Унисекс";
                        if (Objects.nonNull(item.getFasSar()) && item.getFasSar() > 0) {
                            Integer code = Integer.valueOf(item.getFasSar().toString().substring(0, 3));
                            gender = getGenderMap().getOrDefault(code, "");
                        } else {
                            Integer sar = edb.getSarByFasAndNAr(item.getFasNum(), item.getFasNar());
                            if (Objects.nonNull(sar) && sar > 0) {
                                Integer code = Integer.valueOf(sar.toString().substring(0, 3));
                                gender = getGenderMap().getOrDefault(code, "");
                            }
                        }
                        if (gender.equals("")) {
                            if (item.getFasName().trim().toLowerCase().contains("муж") || item.getFasName().trim().toLowerCase().contains("мал")) {
                                gender = "Мужской";
                            }
                            if (item.getFasName().trim().toLowerCase().contains("жен") || item.getFasName().trim().toLowerCase().contains("дев")) {
                                gender = "Женский";
                            }
                        }
                        xCell.setFormula(gender.equals("") ? "Унисекс" : gender);

                        xCell = xSpreadsheet.getCellByPosition(100, nRow);
                        xCell.setFormula(Integer.toString(item.getFasNum()));

                        xCell = xSpreadsheet.getCellByPosition(101, nRow);
                        xCell.setFormula("ТР ТС 017/2011 О БЕЗОПАСНОСТИ ПРОДУКЦИИ ЛЕГКОЙ ПРОМЫШЛЕННОСТИ");

                        xCell = xSpreadsheet.getCellByPosition(102, nRow);
                        xCell.setFormula("Беларусь [112]");

                        xCell = xSpreadsheet.getCellByPosition(103, nRow);
                        xCell.setFormula("400078265");
                    }
                }
                if (isExceptedTNVED) {
                    firstRow = nRow;
                } else {
                    secondRow = nRow;
                }
            }

            if (flagSave) {
                com.sun.star.beans.PropertyValue[] lParams = new com.sun.star.beans.PropertyValue[0];
                String savePath = MyReportsModule.confPath + UtilEan.PATH_EANLIST;
                saveAsDocument(firstCurrentDocument, savePath + "/" + nameTamplates + ".xls", lParams);
                saveAsDocument(secondCurrentDocument, savePath + "/" + nameTamplates + "General" + ".xls", lParams);
            }

        } catch (java.lang.Exception e) {
            log.error("Ошибка при заполнении отчёта.", e);
            throw new Exception("Ошибка при заполнении отчёта.", e);
        }
    }

    private ArrayList<EanItem> openZayavkaEanZO(XComponent currentDocument) throws Exception {
        ArrayList<EanItem> eanItems = new ArrayList<>();
        ArrayList<EanItemListSize> listSizes = new ArrayList<>();

        try {
            XSpreadsheetDocument xSpreadsheetDocument = UnoRuntime.queryInterface(XSpreadsheetDocument.class, currentDocument);
            XSpreadsheets xSpreadsheets = xSpreadsheetDocument.getSheets();
            Object sheet = xSpreadsheets.getByName("Товарная база");
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
            String fo32 = xSpreadsheet.getCellByPosition(0, 0).getFormula().trim();


            //----------------Заполнение тела документа

            int nRow = 5;
            List<String> eans = new ArrayList<>();
            for (int i = 0; i < 5000; i++) {
                nRow++;

                xCell = xSpreadsheet.getCellByPosition(10, nRow);
                if (xCell.getFormula().trim().equals(""))
                    break;

                EanItem eanItem = new EanItem();
                EanItemListSize size = new EanItemListSize();
                listSizes = new ArrayList<>();

                xCell = xSpreadsheet.getCellByPosition(9, nRow);
                size.setEan13(xCell.getFormula().toLowerCase().trim());
                String ean = xSpreadsheet.getCellByPosition(9, nRow).getFormula().trim();
                if (ean != null && !ean.isEmpty()) {
                    eans.add(ean);
                }


                xCell = xSpreadsheet.getCellByPosition(10, nRow);
                eanItem.setFasName(xCell.getFormula().trim().toLowerCase());

                xCell = xSpreadsheet.getCellByPosition(13, nRow);
                String str = xCell.getFormula().trim().toLowerCase();
                eanItem.setFasNum(Integer.valueOf(str.substring(str.indexOf("модель") + 6, str.indexOf(",")).trim().replace(" ", "")));
                eanItem.setFasNar(str.substring(str.indexOf("артикул") + 7, str.indexOf(",", str.indexOf("артикул"))).trim().replace(" ", ""));

                size.setSizePrint(str.substring(str.indexOf("размер") + 6, str.indexOf("сорт", str.indexOf("размер"))).trim().replace(" ", ""));
                if (size.getSizePrint().substring(size.getSizePrint().length() - 1, size.getSizePrint().length()).equals(","))
                    size.setSizePrint(size.getSizePrint().substring(0, size.getSizePrint().length() - 1));
                listSizes.add(size);
                eanItem.setDataSize(listSizes);

                eanItem.setFasSrt(UtilEan.setSrtIzd(str.substring(str.indexOf("сорт") + 4, str.indexOf(",", str.indexOf("сорт"))).trim().replace(" ", "")));

                String tmpStr = str.substring(str.indexOf("сорт") + 4);
                if ((tmpStr.length() - tmpStr.replace(",", "").length()) == 2)
                    eanItem.setColorName(str.substring(str.lastIndexOf(",") + 1, str.length()).toUpperCase().trim());
                else
                    eanItem.setColorName("");

                xCell = xSpreadsheet.getCellByPosition(14, nRow);
                eanItem.setTextGOST(xCell.getFormula().toLowerCase().replace("гост", "").trim());

                xCell = xSpreadsheet.getCellByPosition(22, nRow);
                eanItem.setKolX((int) xCell.getValue());

                xCell = xSpreadsheet.getCellByPosition(29, nRow);
                eanItem.setTextOKRB(xCell.getFormula().trim().toLowerCase());

                xCell = xSpreadsheet.getCellByPosition(35, nRow);
                eanItem.setTextTHB(xCell.getFormula().trim().toLowerCase());

                eanItems.add(eanItem);
            }
        } catch (java.lang.Exception e) {
            eanItems = new ArrayList<>();
            log.error("Ошибка отчёта.", e);
            throw new Exception("Ошибка отчёта.", e);
        }
        return eanItems;
    }

    private void fildZayavkaEanImportDB(XComponent currentDocument, boolean flagSave) throws Exception {
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
            aBorder.BottomLine = aLine;
            aBorder.IsBottomLineValid = true;

            XCell xCell = xSpreadsheet.getCellByPosition(0, 0);

            //----------------Заполнение шапки документа
            xCell = xSpreadsheet.getCellByPosition(1, 1);
            xCell.setFormula(nameTamplates + " от " + dateTamplates);

            //----------------Заполнение тела документа

            int nCol = 1;
            int nRow = 5;
            EanItem eanItem = null;
            Vector element;

            for (Object object : objects) {
                element = (Vector) object;
                eanItem = (EanItem) element.get(0);

                nCol = 1;
                nRow++;

                xCell = xSpreadsheet.getCellByPosition(nCol++, nRow);
                xCell.setValue(100);

                xCell = xSpreadsheet.getCellByPosition(nCol++, nRow);
                xCell.setFormula(eanItem.getFasName().trim().toUpperCase());

                xCell = xSpreadsheet.getCellByPosition(nCol++, nRow);
                xCell.setValue(eanItem.getFasNum());

                xCell = xSpreadsheet.getCellByPosition(nCol++, nRow);
                xCell.setFormula(eanItem.getFasNar().trim().toUpperCase() + " " + eanItem.getColorName().trim().toUpperCase());

                for (int j = 1; j < 11; j++) {
                    xCell = xSpreadsheet.getCellByPosition(j, nRow);
                    xPropSet = UnoRuntime.queryInterface(
                            com.sun.star.beans.XPropertySet.class,
                            xCell);
                    xPropSet.setPropertyValue("TableBorder", aBorder);
                }

                for (EanItemListSize eanItemListSize : eanItem.getDataSize()) {
                    nCol = 5;
                    nRow++;
                    xCell = xSpreadsheet.getCellByPosition(nCol++, nRow);
                    xCell.setValue(eanItem.getFasSrt());

                    xCell = xSpreadsheet.getCellByPosition(nCol++, nRow);
                    xCell.setValue(eanItemListSize.getRst());

                    xCell = xSpreadsheet.getCellByPosition(nCol++, nRow);
                    xCell.setValue(eanItemListSize.getRzm());

                    xCell = xSpreadsheet.getCellByPosition(nCol++, nRow);
                    xCell.setFormula(eanItemListSize.getSizePrint());

                    xCell = xSpreadsheet.getCellByPosition(nCol++, nRow);
                    xCell.setFormula(element.get(1).toString());

                    xCell = xSpreadsheet.getCellByPosition(nCol++, nRow);
                    xCell.setFormula(eanItemListSize.getEan13());

                    xCell = xSpreadsheet.getCellByPosition(nCol++, nRow);
                    xCell.setFormula(element.get(2).toString());

                    xCell = xSpreadsheet.getCellByPosition(nCol++, nRow);
                    xCell.setFormula(element.get(3).toString());
                }
            }

            if (flagSave) {
                com.sun.star.beans.PropertyValue[] lParams = new com.sun.star.beans.PropertyValue[0];
                String savePath = MyReportsModule.confPath + UtilEan.PATH_IMPORT_EANLIST;
                saveAsDocument(
                        currentDocument,
                        savePath + "/" + "Импорт " + nameTamplates + " - " +
                                new SimpleDateFormat("dd.MM.yyyy hh.mm").format(Calendar.getInstance().getTime()) + ".xls",
                        lParams);
            }

        } catch (java.lang.Exception e) {
            log.error("Ошибка при заполнении отчёта.", e);
            throw new Exception("Ошибка при заполнении отчёта.", e);
        }
    }

    private void fieldResultImportDB(XComponent currentDocument, boolean flagSave) throws Exception {
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
            aBorder.BottomLine = aLine;
            aBorder.IsBottomLineValid = true;

            XCell xCell = xSpreadsheet.getCellByPosition(0, 0);

            //----------------Заполнение шапки документа
            xCell = xSpreadsheet.getCellByPosition(1, 1);
            xCell.setFormula(nameTamplates);

            //----------------Заполнение тела документа

            int nCol = 1;
            int nRow = 4;
            int kol = 0;

            EanItem eanItem = null;
            EanItem history = null;
            Vector row;

            boolean flagPrint = false;
            boolean flagSelect = false;

            //----------------Заполнение тела документа
            for (Object object : tModel.getDataVector()) {
                if (((Vector) object).get(0).toString().equals("true")) {
                    flagSelect = true;
                    break;
                }
            }

            //-------------------------

            for (int i = 0; i < tModel.getDataVector().size(); i++) {
                row = (Vector) tModel.getDataVector().get(i);

                flagPrint = flagSelect ? (Boolean) row.get(0) : true;

                if (flagPrint) {
                    eanItem = (EanItem) row.get(14);
                    nRow++;

                    if (history == eanItem) {
                        nCol = 7;

                        xCell = xSpreadsheet.getCellByPosition(nCol++, nRow);
                        xCell.setFormula(row.get(9).toString());

                        xCell = xSpreadsheet.getCellByPosition(nCol++, nRow);
                        xCell.setFormula(row.get(10).toString());

                        xCell = xSpreadsheet.getCellByPosition(nCol++, nRow);
                        xCell.setFormula(row.get(11).toString());

                        xCell = xSpreadsheet.getCellByPosition(nCol++, nRow);
                        xCell.setFormula(row.get(12).toString());

                        xCell = xSpreadsheet.getCellByPosition(nCol++, nRow);
                        xCell.setFormula(row.get(15).toString().replace("<html>", "").replace("</html>", "").replace("<br>", "\n"));

                    } else {
                        i--;

                        history = (EanItem) row.get(14);

                        nCol = 1;

                        xCell = xSpreadsheet.getCellByPosition(nCol++, nRow);
                        xCell.setValue(++kol);

                        xCell = xSpreadsheet.getCellByPosition(nCol++, nRow);
                        xCell.setFormula(eanItem.getFasName().trim().toUpperCase());

                        xCell = xSpreadsheet.getCellByPosition(nCol++, nRow);
                        xCell.setValue(eanItem.getFasNum());

                        xCell = xSpreadsheet.getCellByPosition(nCol++, nRow);
                        xCell.setFormula(eanItem.getFasNar().trim().toUpperCase());

                        xCell = xSpreadsheet.getCellByPosition(nCol++, nRow);
                        xCell.setValue(eanItem.getFasSrt());

                        xCell = xSpreadsheet.getCellByPosition(nCol++, nRow);
                        xCell.setFormula(eanItem.getColorName().trim().toUpperCase());

                        for (int j = 1; j < 12; j++) {
                            xCell = xSpreadsheet.getCellByPosition(j, nRow);
                            xPropSet = UnoRuntime.queryInterface(
                                    com.sun.star.beans.XPropertySet.class,
                                    xCell);
                            xPropSet.setPropertyValue("TableBorder", aBorder);
                        }
                    }
                }
            }

            if (flagSave) {
                com.sun.star.beans.PropertyValue[] lParams = new com.sun.star.beans.PropertyValue[0];
                String savePath = MyReportsModule.confPath + UtilEan.PATH_IMPORT_EANLIST;
                saveAsDocument(
                        currentDocument,
                        savePath + "/" + "Импорт " + nameTamplates + " - " +
                                new SimpleDateFormat("dd.MM.yyyy hh.mm").format(Calendar.getInstance().getTime()) + ".xls",
                        lParams);
            }

        } catch (java.lang.Exception e) {
            log.error("Ошибка при заполнении отчёта.", e);
            throw new Exception("Ошибка при заполнении отчёта.", e);
        }
    }

    private void fieldEanByMarshList(XComponent currentDocument) throws Exception {
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
            aBorder.BottomLine = aLine;
            aBorder.IsBottomLineValid = true;

            XCell xCell = xSpreadsheet.getCellByPosition(0, 0);

            int nCol = 1;
            int nRow = 4;
            int kol = 0;

            String current = "";
            String history = "";
            Vector row;

            boolean flagPrint = false;
            boolean flagSelect = false;

            //----------------Заполнение шапки документа
            xCell = xSpreadsheet.getCellByPosition(1, 1);
            xCell.setFormula(nameTamplates);

            //----------------Заполнение тела документа
            for (Object object : tModel.getDataVector()) {
                if (((Vector) object).get(0).toString().equals("true")) {
                    flagSelect = true;
                    break;
                }
            }

            for (int i = 0; i < tModel.getDataVector().size(); i++) {
                row = (Vector) tModel.getDataVector().get(i);

                flagPrint = flagSelect ? (Boolean) row.get(0) : true;

                if (flagPrint) {
                    current = "";
                    for (int j = 1; j < 7; j++) {
                        current += row.get(j).toString();
                    }

                    nRow++;

                    if (history.equals(current)) {
                        nCol = 8;

                        xCell = xSpreadsheet.getCellByPosition(nCol++, nRow);
                        xCell.setFormula(row.get(7).toString());

                        xCell = xSpreadsheet.getCellByPosition(nCol++, nRow);
                        xCell.setFormula(row.get(8).toString());

                        xCell = xSpreadsheet.getCellByPosition(nCol++, nRow);
                        xCell.setFormula(row.get(9).toString());

                        xCell = xSpreadsheet.getCellByPosition(nCol++, nRow);
                        xCell.setFormula(row.get(10).toString());

                        xCell = xSpreadsheet.getCellByPosition(nCol++, nRow);
                        xCell.setFormula(row.get(11).toString());

                        xCell = xSpreadsheet.getCellByPosition(nCol++, nRow);
                        xCell.setFormula(row.get(12).toString());

                        xCell = xSpreadsheet.getCellByPosition(nCol++, nRow);
                        xCell.setFormula(row.get(13).toString());

                    } else {
                        i--;

                        history = "";
                        for (int j = 1; j < 7; j++) {
                            history += row.get(j).toString();
                        }

                        nCol = 1;

                        xCell = xSpreadsheet.getCellByPosition(nCol++, nRow);
                        xCell.setValue(++kol);

                        xCell = xSpreadsheet.getCellByPosition(nCol++, nRow);
                        xCell.setFormula(row.get(1).toString().trim().toUpperCase());

                        xCell = xSpreadsheet.getCellByPosition(nCol++, nRow);
                        xCell.setFormula(row.get(2).toString());

                        xCell = xSpreadsheet.getCellByPosition(nCol++, nRow);
                        xCell.setFormula(row.get(3).toString().trim().toUpperCase());

                        xCell = xSpreadsheet.getCellByPosition(nCol++, nRow);
                        xCell.setFormula(row.get(4).toString());

                        xCell = xSpreadsheet.getCellByPosition(nCol++, nRow);
                        xCell.setFormula(row.get(5).toString().trim().toUpperCase());

                        xCell = xSpreadsheet.getCellByPosition(nCol++, nRow);
                        xCell.setFormula(row.get(6).toString().trim().toUpperCase());

                        for (int j = 1; j < 15; j++) {
                            xCell = xSpreadsheet.getCellByPosition(j, nRow);
                            xPropSet = UnoRuntime.queryInterface(
                                    com.sun.star.beans.XPropertySet.class,
                                    xCell);
                            xPropSet.setPropertyValue("TableBorder", aBorder);
                        }
                    }
                }
            }

        } catch (Exception e) {
            log.error("Ошибка при заполнении отчёта fieldEanByMarshList.", e);
            throw new Exception("Ошибка при заполнении отчёта fieldEanByMarshList.", e);
        }
    }
}
