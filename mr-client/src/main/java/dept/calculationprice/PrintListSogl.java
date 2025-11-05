/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dept.calculationprice;

import by.gomel.freedev.ucframework.uccore.report.OfficeBootStrap;
import by.gomel.freedev.ucframework.uccore.report.connector.BootstrapSocketConnector;
import by.march8.api.utils.DateUtils;
import by.march8.ecs.application.modules.warehouse.external.shipping.dao.SaleDocumentJDBC;
import by.march8.ecs.application.modules.warehouse.external.shipping.manager.SaleDocumentDataProvider;
import com.sun.star.beans.PropertyValue;
import com.sun.star.beans.XPropertySet;
import com.sun.star.comp.helper.BootstrapException;
import com.sun.star.container.NoSuchElementException;
import com.sun.star.frame.XComponentLoader;
import com.sun.star.frame.XDesktop;
import com.sun.star.io.IOException;
import com.sun.star.lang.IllegalArgumentException;
import com.sun.star.lang.IndexOutOfBoundsException;
import com.sun.star.lang.WrappedTargetException;
import com.sun.star.lang.XComponent;
import com.sun.star.lang.XMultiComponentFactory;
import com.sun.star.sheet.XSpreadsheet;
import com.sun.star.sheet.XSpreadsheetDocument;
import com.sun.star.sheet.XSpreadsheets;
import com.sun.star.table.BorderLine;
import com.sun.star.table.TableBorder;
import com.sun.star.table.XCell;
import com.sun.star.uno.UnoRuntime;
import com.sun.star.uno.XComponentContext;
import com.sun.star.uno.XInterface;
import com.sun.star.util.XURLTransformer;
import common.User;
import dept.MyReportsModule;
import dept.calculationprice.model.ProtocolPreset;

import javax.swing.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * @author user
 */
@SuppressWarnings("all")
public class PrintListSogl {

    private static final long serialVersionUID = 1L;
    private static XMultiComponentFactory xRemoteServiceManager = null;
    private static XURLTransformer xTransformer = null;
    private static XDesktop xDesktop = null;
    private static XComponentLoader xComponentLoader = null;
    private static PropertyValue[] loadProps = new PropertyValue[0];
    List<Integer> listSoglId;
    private PricePDB dao;
    private BorderLine aLine = new BorderLine();
    private TableBorder aBorder = new TableBorder();

    public PrintListSogl(List<Integer> listSoglId) {
        this.listSoglId = listSoglId;
        connect();
        dao = new PricePDB();
        dao.conn();

        aLine.Color = 0x000000;
        aLine.InnerLineWidth = 0;
        aLine.LineDistance = 0;
        aLine.OuterLineWidth = 25;

        aBorder.BottomLine = aLine;
        aBorder.IsBottomLineValid = true;
    }

    public static void connect() {
        // получим контекст удаленного компонента офиса

        try {
            String oooExeFolder;
/*            java.io.File fC = new java.io.File("c:\\windows\\explorer.exe");
            if (!fC.exists()) {
                oooExeFolder = "/usr/bin/soffice";
            } else {
                oooExeFolder = "c://Program Files//OpenOffice.org 3//program//";
            }*/

            OfficeBootStrap office = OfficeBootStrap.getInstance();
            oooExeFolder = office.getBootPath();

            XComponentContext xContext = BootstrapSocketConnector.bootstrap(oooExeFolder, false);
            xRemoteServiceManager = xContext.getServiceManager();
            // создадим сервис, который понадобится при печати
            Object transformer = xRemoteServiceManager.createInstanceWithContext("com.sun.star.util.URLTransformer", xContext);
            xTransformer = (XURLTransformer) UnoRuntime.queryInterface(XURLTransformer.class, transformer);
            //Object transformer = xRemoteServiceManager.createInstanceWithContext();

            // получим сервис Desktop
            Object desktop = (XInterface) xRemoteServiceManager.createInstanceWithContext(
                    "com.sun.star.frame.Desktop", xContext);
            xDesktop = (XDesktop) UnoRuntime.queryInterface(
                    XDesktop.class, desktop);

            // данный интерфейс позволяет загружать и сохранять документы
            xComponentLoader = (XComponentLoader) UnoRuntime.queryInterface(
                    XComponentLoader.class, desktop);
        } catch (BootstrapException e) {
            System.err.println(e.getMessage());
            JOptionPane.showMessageDialog(null, "Ошибка соединения с OpenOffice.", "Ошибка!!!", JOptionPane.ERROR_MESSAGE);
        } catch (com.sun.star.uno.Exception e) {
            System.err.println(e.getMessage());
            JOptionPane.showMessageDialog(null, "Ошибка соединения с OpenOffice.", "Ошибка!!!", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static XComponent openDocument(String sURL) {
        // преобразуем путь к файлу в URL

        try {
            //java.io.File sourceFile = new java.io.File("");
            StringBuffer sTmp;
            sTmp = new StringBuffer("file:///");
            //sTmp.append(MainForm.progPath);
            sTmp.append(MyReportsModule.progPath);
            sTmp.append("/");
            sURL = sTmp.toString().replace('\\', '/') + sURL;
            System.err.println(sURL);
            return xComponentLoader.loadComponentFromURL(sURL, "_blank", 0, loadProps);
        } catch (IOException e) {

            System.out.println(e.getMessage());
            JOptionPane.showMessageDialog(null, "Ошибка открытия шаблона " + sURL, "Ошибка!!!", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            JOptionPane.showMessageDialog(null, "Ошибка открытия шаблона " + sURL, "Ошибка!!!", JOptionPane.ERROR_MESSAGE);
        }

        return null;
    }

    public void printListAdvanced(ProtocolPreset preset) throws NoSuchElementException, WrappedTargetException, IndexOutOfBoundsException {
        String tmplt = "list.ots";
        String[] percent = {"5%", "10%", "15%", "20%", "25%", "30%"};
        if (preset.getProtocolType() == 2) {
            if (!preset.isShowCurrency()) {
                tmplt = "list_no_currency.ots";
            } else if (preset.isShowCurrency()) {
                tmplt = "list_currency.ots";
            }
        }

        if (preset.getProtocolType() == 1) {
            tmplt = "approval_protocol_35.ots";
        }

        XComponent currentDocument = openDocument("Templates/" + tmplt);

        ValueCalculation valueCalculation;
        int pos = 8;

        XSpreadsheetDocument xSpreadsheetDocument;
        xSpreadsheetDocument = (XSpreadsheetDocument) UnoRuntime.queryInterface(XSpreadsheetDocument.class, currentDocument);
        XSpreadsheets xSpreadsheets = xSpreadsheetDocument.getSheets();
        Object sheet = xSpreadsheets.getByName("Лист1");
        XSpreadsheet xSpreadsheet;
        xSpreadsheet = (XSpreadsheet) UnoRuntime.queryInterface(XSpreadsheet.class, sheet);
        XCell xCell;

        xCell = xSpreadsheet.getCellByPosition(15, 1);
        xCell.setFormula(new SimpleDateFormat("dd.MM.yyyy").format(new GregorianCalendar().getTime()));
        xCell = xSpreadsheet.getCellByPosition(15, 2);
        xCell.setFormula(dao.getKurs()[0] + "");

        if (preset.getProtocolType() == 1) {
            // Если валютный протокол - опишем
            xCell.setFormula(String.valueOf(preset.getCurrencyRate()));

            // Меняем наименование валюты в шапке

            String currShort = "";
            String currLong = "";
            String currLongPlus = "";

            switch (preset.getCurrencyType()) {
                case 0:
                    currShort = "Росс.руб.";
                    currLong = "Российские рубли";
                    currLongPlus = "российских рублях";
                    break;
                case 1:
                    currShort = "Долл.";
                    currLong = "Доллары США";
                    currLongPlus = "долларах США";
                    break;
                case 2:
                    currShort = "Евро";
                    currLong = "Евро";
                    currLongPlus = "евро";
                    break;
            }


            xCell = xSpreadsheet.getCellByPosition(5, 5);
            xCell.setFormula(currShort);

            xCell = xSpreadsheet.getCellByPosition(9, 4);
            xCell.setFormula("Предлагаемая цена в " + currLongPlus);

            xCell = xSpreadsheet.getCellByPosition(12, 5);
            xCell.setFormula(currLong);

            for (int i = 0; i < 6; i++) {
                xCell = xSpreadsheet.getCellByPosition(6, i + 8);
                xCell.setFormula(percent[i]);

                xCell = xSpreadsheet.getCellByPosition(9, i + 8);
                xCell.setFormula(percent[i]);

                xCell = xSpreadsheet.getCellByPosition(6, i + 14);
                xCell.setFormula(percent[i]);

                xCell = xSpreadsheet.getCellByPosition(9, i + 14);
                xCell.setFormula(percent[i]);

                xCell = xSpreadsheet.getCellByPosition(6, i + 20);
                xCell.setFormula(percent[i]);

                xCell = xSpreadsheet.getCellByPosition(9, i + 20);
                xCell.setFormula(percent[i]);
            }
        } else if (preset.getProtocolType() == 2) {

            if (!preset.isShowCurrency()) {

                xCell = xSpreadsheet.getCellByPosition(10, 1);
                xCell.setFormula("'" + DateUtils.getNormalDateFormat(new Date()));

                xCell = xSpreadsheet.getCellByPosition(15, 1);
                xCell.setFormula("");
                xCell = xSpreadsheet.getCellByPosition(15, 2);
                xCell.setFormula("");
                for (int i = 0; i < 3; i++) {
                    xCell = xSpreadsheet.getCellByPosition(5, i + 8);
                    xCell.setFormula(percent[i]);

                    xCell = xSpreadsheet.getCellByPosition(8, i + 8);
                    xCell.setFormula(percent[i]);

                }

                valueCalculation = dao.getDataCalculation(listSoglId.get(0));
                SaleDocumentJDBC jdbc = new SaleDocumentJDBC();
                String sar = jdbc.getArticleCodeByModelNumber(valueCalculation.getFas());
                String TNVD = jdbc.getTNVDBySar(sar);
                double pref = 1.2;
                double percent_ = 0.05;

                if (sar != null) {
                    try {

                        double vat = getVatValueByArticleCode(sar.trim(), TNVD);
                        pref = 1 + vat / 100;
                    } catch (Exception e) {

                    }
                }

                xCell = xSpreadsheet.getCellByPosition(6, 8);
                xCell.setFormula("=ROUNDUP(E$9+E$9*G9;4)");
                xCell = xSpreadsheet.getCellByPosition(6, 9);
                xCell.setFormula("=ROUNDUP(E$9+E$9*G9;4)");
                xCell = xSpreadsheet.getCellByPosition(6, 10);
                xCell.setFormula("=ROUNDUP(E$9+E$9*G9;4)");

                xCell = xSpreadsheet.getCellByPosition(9, 8);
                xCell.setFormula("=ROUNDUP(G9*" + pref + ";2)");
                xCell = xSpreadsheet.getCellByPosition(9, 9);
                xCell.setFormula("=ROUNDUP(G10*" + pref + ";2)");
                xCell = xSpreadsheet.getCellByPosition(9, 10);
                xCell.setFormula("=ROUNDUP(G11*" + pref + ";2)");

            } else {
                for (int i = 0; i < 3; i++) {
                    xCell = xSpreadsheet.getCellByPosition(6, i + 8);
                    xCell.setFormula(percent[i]);

                    xCell = xSpreadsheet.getCellByPosition(9, i + 8);
                    xCell.setFormula(percent[i]);

                    xCell = xSpreadsheet.getCellByPosition(6, i + 11);
                    xCell.setFormula(percent[i]);

                    xCell = xSpreadsheet.getCellByPosition(9, i + 11);
                    xCell.setFormula(percent[i]);

                    xCell = xSpreadsheet.getCellByPosition(6, i + 14);
                    xCell.setFormula(percent[i]);

                    xCell = xSpreadsheet.getCellByPosition(9, i + 14);
                    xCell.setFormula(percent[i]);
                }

                xCell = xSpreadsheet.getCellByPosition(7, 8);
                xCell.setFormula("=ROUNDUP(E$9+E$9*G9;4)");
                xCell = xSpreadsheet.getCellByPosition(7, 9);
                xCell.setFormula("=ROUNDUP(E$9+E$9*G10;4)");
                xCell = xSpreadsheet.getCellByPosition(7, 10);
                xCell.setFormula("=ROUNDUP(E$9+E$9*G11;4)");

                xCell = xSpreadsheet.getCellByPosition(7, 11);
                xCell.setFormula("=ROUNDUP(E$12+E$12*G12;4)");
                xCell = xSpreadsheet.getCellByPosition(7, 12);
                xCell.setFormula("=ROUNDUP(E$12+E$12*G13;4)");
                xCell = xSpreadsheet.getCellByPosition(7, 13);
                xCell.setFormula("=ROUNDUP(E$12+E$12*G14;4)");

                xCell = xSpreadsheet.getCellByPosition(7, 14);
                xCell.setFormula("=ROUNDUP(E$15+E$15*G15;4)");
                xCell = xSpreadsheet.getCellByPosition(7, 15);
                xCell.setFormula("=ROUNDUP(E$15+E$15*G16;4)");
                xCell = xSpreadsheet.getCellByPosition(7, 16);
                xCell.setFormula("=ROUNDUP(E$15+E$15*G17;4)");

            }
        } else if (preset.getProtocolType() == 0) {
            for (int i = 0; i < 5; i++) {
                xCell = xSpreadsheet.getCellByPosition(6, i + 8);
                xCell.setFormula(percent[i]);

                xCell = xSpreadsheet.getCellByPosition(9, i + 8);
                xCell.setFormula(percent[i]);

                xCell = xSpreadsheet.getCellByPosition(6, i + 13);
                xCell.setFormula(percent[i]);

                xCell = xSpreadsheet.getCellByPosition(9, i + 13);
                xCell.setFormula(percent[i]);

                xCell = xSpreadsheet.getCellByPosition(6, i + 18);
                xCell.setFormula(percent[i]);

                xCell = xSpreadsheet.getCellByPosition(9, i + 18);
                xCell.setFormula(percent[i]);
            }
        }

        for (Integer listSoglId1 : listSoglId) {
            valueCalculation = dao.getDataCalculation(listSoglId1);
            xCell = xSpreadsheet.getCellByPosition(0, pos);
            xCell.setFormula(valueCalculation.getFas());
            xCell = xSpreadsheet.getCellByPosition(1, pos);
            xCell.setFormula(valueCalculation.getNar());
            xCell = xSpreadsheet.getCellByPosition(2, pos);
            xCell.setFormula(valueCalculation.getNiz() + "\n" + valueCalculation.getPol());
            xCell = xSpreadsheet.getCellByPosition(3, pos);
            xCell.setFormula(valueCalculation.getRzmn() + " - " + valueCalculation.getRzmk());
            xCell = xSpreadsheet.getCellByPosition(4, pos);

            if (preset.isShowCredit()) {
                xCell.setValue(valueCalculation.getPrimeCostCredit());
            } else {
                xCell.setValue(valueCalculation.getCc());
            }

            if (preset.getProtocolType() == 2 && !preset.isShowCurrency()) {
                double primeCost = 0;

                if (preset.isShowCredit()) {
                    primeCost = valueCalculation.getPrimeCostCredit();
                } else {
                    primeCost = valueCalculation.getCc();
                }
            }

            if (preset.getProtocolType() == 1) {
                pos += 9;
            } else {
                pos += 5;
            }
        }

        User user = User.getInstance();

        String name = "";
        if (MyReportsModule.UserName.equals("cen01")) {
            name = "А.С. Симоненко";
        }
        if (MyReportsModule.UserName.equals("cen02")) {
            name = "Н.А. Шавловская";
        }
        if (MyReportsModule.UserName.equals("cen03")) {
            name = "А.В. Телюкова ";
        }
        if (MyReportsModule.UserName.equals("cen04")) {
            name = "А.Н. Кучева ";
        }
        if (MyReportsModule.UserName.equals("pln05")) {
            name = "А.Д. Курек ";
        }

        if (MyReportsModule.UserName.equals("admin")) {
            name = "Developer";
        }

        int colIndex = 10;

        if (preset.getProtocolType() == 1) {
            //ОВЭС
            xCell = xSpreadsheet.getCellByPosition(2, 27);
            xCell.setFormula(dao.getLastName()[7]);
            xCell = xSpreadsheet.getCellByPosition(10, 27);
            xCell.setFormula(dao.getLastName()[6]);
            xCell = xSpreadsheet.getCellByPosition(2, 27);
            xCell.setFormula("Начальник отдела продаж");
            xCell = xSpreadsheet.getCellByPosition(10, 27);
            xCell.setFormula("Ю.В. Чваркова");
            xCell = xSpreadsheet.getCellByPosition(colIndex, 30);
            xCell.setFormula(dao.getLastName()[1]);
            xCell = xSpreadsheet.getCellByPosition(colIndex, 32);
            xCell.setFormula(name);
        } else if (preset.getProtocolType() == 2) {
            //Тендер
            xCell = xSpreadsheet.getCellByPosition(2, 18);
            xCell.setFormula("Начальник отдела продаж");
            xCell = xSpreadsheet.getCellByPosition(8, 18);
            xCell.setFormula("Ю.В. Чваркова");
            xCell = xSpreadsheet.getCellByPosition(2, 21);
            xCell.setFormula(dao.getLastName()[4]);
            xCell = xSpreadsheet.getCellByPosition(8, 21);
            xCell.setFormula(dao.getLastName()[1]);
            xCell = xSpreadsheet.getCellByPosition(8, 23);
            xCell.setFormula(name);
        } else {
            // ОМ
            xCell = xSpreadsheet.getCellByPosition(2, 25);
            xCell.setFormula(dao.getLastName()[8]); // Поле в котором отображается должность - Зам по коммерции //8
            xCell = xSpreadsheet.getCellByPosition(8, 25);
            xCell.setFormula(dao.getLastName()[9]); // Поле в котором отображается ФИО - Зам по коммерции //9
            xCell = xSpreadsheet.getCellByPosition(2, 28);
            xCell.setFormula(dao.getLastName()[4]);
            xCell = xSpreadsheet.getCellByPosition(8, 28);
            xCell.setFormula(dao.getLastName()[1]);
            xCell = xSpreadsheet.getCellByPosition(8, 30);
            xCell.setFormula(name);
        }
    }

    public void printListDet() throws NoSuchElementException, WrappedTargetException, IndexOutOfBoundsException {
        XComponent currentDocument = openDocument("Templates/" + "listDet.ots");
        ValueCalculation valueCalculation;
        int pos = 8;

        XSpreadsheetDocument xSpreadsheetDocument;
        xSpreadsheetDocument = (XSpreadsheetDocument) UnoRuntime.queryInterface(XSpreadsheetDocument.class, currentDocument);
        XSpreadsheets xSpreadsheets = xSpreadsheetDocument.getSheets();
        Object sheet = xSpreadsheets.getByName("Лист1");
        XSpreadsheet xSpreadsheet;
        xSpreadsheet = (XSpreadsheet) UnoRuntime.queryInterface(XSpreadsheet.class, sheet);


        XCell xCell;

        xCell = xSpreadsheet.getCellByPosition(15, 1);
        xCell.setFormula(new SimpleDateFormat("dd.MM.yyyy").format(new GregorianCalendar().getTime()));
        xCell = xSpreadsheet.getCellByPosition(15, 2);
        xCell.setFormula(dao.getKurs()[0] + "");

        for (Integer listSoglId1 : listSoglId) {
            valueCalculation = dao.getDataCalculation(listSoglId1);
            xCell = xSpreadsheet.getCellByPosition(0, pos);
            xCell.setFormula(valueCalculation.getFas());
            xCell = xSpreadsheet.getCellByPosition(1, pos);
            xCell.setFormula(valueCalculation.getNar());
            xCell = xSpreadsheet.getCellByPosition(2, pos);
            xCell.setFormula(valueCalculation.getNiz() + "\n" + valueCalculation.getPol());
            xCell = xSpreadsheet.getCellByPosition(3, pos);
            xCell.setFormula("'" + valueCalculation.getRzmn() + "" + " - " + valueCalculation.getRzmk() + "");
            xCell = xSpreadsheet.getCellByPosition(4, pos);
            xCell.setFormula(valueCalculation.getCc() + "");

            setBorderForRangeCell(xSpreadsheet, 0, 13, 17);
            setBorderForRangeCell(xSpreadsheet, 0, 18, 17);
            setBorderForRangeCell(xSpreadsheet, 0, 23, 17);
            pos += 4;
        }
        String name = "";
        if (MyReportsModule.UserName.equals("cen01")) {
            name = "В.И.Дасько ";
        }
        if (MyReportsModule.UserName.equals("cen02")) {
            name = "Н.А.Шавловская ";
        }
        if (MyReportsModule.UserName.equals("cen03")) {
            name = "А.В.Телюкова ";
        }
        if (MyReportsModule.UserName.equals("cen04")) {
            name = "А.Н.Кучева ";
        }

        xCell = xSpreadsheet.getCellByPosition(8, 30);
        xCell.setFormula(dao.getLastName()[1]);

        xCell = xSpreadsheet.getCellByPosition(8, 32);
        xCell.setFormula(name);

    }

    protected void setBorderForRangeCell(XSpreadsheet xSpreadsheet, int column, int row, int size) {
        for (int p = column; p < size; p++) {
            try {
                XCell xCell = xSpreadsheet.getCellByPosition(p, row - 1);
                XPropertySet xPropSet = UnoRuntime
                        .queryInterface(
                                XPropertySet.class,
                                xCell);
                xPropSet.setPropertyValue("TableBorder", aBorder);
            } catch (Exception e) {
                System.err.println("Ошибка установки параметра [TableBorder] для диапазона");
                e.printStackTrace();
            }
        }
    }

    private int getVatValueByArticleCode(String articleCode, String TNVD) {
        if (SaleDocumentDataProvider.isChildItem(articleCode)) {
            // если это мать его бюстье
            if ((articleCode.equals("41347900") ||
                    articleCode.equals("41347901") ||
                    articleCode.equals("41347902") ||
                    articleCode.equals("41347903") ||
                    articleCode.equals("41347904") ||
                    articleCode.equals("41347905") ||
                    articleCode.equals("41347906") ||
                    articleCode.equals("41347907") ||
                    articleCode.equals("41679808"))
                    && !((TNVD.equals("6101209000") && articleCode.substring(0, 4).equals("4233"))
                    || (TNVD.equals("6102209000") && articleCode.substring(0, 4).equals("4233"))
                    || ((TNVD.substring(0, 4).equals("6103")) && (articleCode.substring(0, 4).equals("4231")
                    || articleCode.substring(0, 4).equals("4233") || articleCode.substring(0, 4).equals("4238")
                    || articleCode.substring(0, 4).equals("4261")))
                    || ((TNVD.substring(0, 4).equals("6104")) && (articleCode.substring(0, 4).equals("4231")
                    || articleCode.substring(0, 4).equals("4233") || articleCode.substring(0, 4).equals("4238")
                    || articleCode.substring(0, 4).equals("4236") || articleCode.substring(0, 4).equals("4268")))
                    || ((TNVD.substring(0, 4).equals("6112")) && (articleCode.substring(0, 4).equals("4166")
                    || articleCode.substring(0, 5).equals("41678") || articleCode.substring(0, 5).equals("41674")
                    || articleCode.substring(0, 5).equals("41679") || articleCode.substring(0, 4).equals("4265"))))
            ) {
                return 20;
            } else {
                return 10;
            }
        } else {
            return 20;
        }
    }

    public double moneyRound(double value) {

        double value_ = new BigDecimal(value)
                .setScale(3, RoundingMode.HALF_UP).setScale(2, RoundingMode.HALF_UP).doubleValue();
        int intPart = (int) value_;
        double x = new BigDecimal(value_ - intPart)
                .setScale(3, RoundingMode.HALF_UP).setScale(2, RoundingMode.HALF_UP).doubleValue();
        int floatPart = (int) ((x) * 100);

        String digit = String.valueOf(floatPart);
        int iFirst, iSecond;

        if (digit.length() > 0) {

            if (digit.length() == 1) {
                iFirst = 0;
                iSecond = Integer.valueOf(digit.substring(0, 1));
            } else {
                iFirst = Integer.valueOf(digit.substring(0, 1));
                iSecond = Integer.valueOf(digit.substring(1, 2));
            }

            if (iSecond > 0 && iSecond <= 5) {
                iSecond = 5;
            }

            if (iSecond > 5) {
                iSecond = 0;
                iFirst += 1;
                if (iFirst > 9) {
                    iFirst = 0;
                    intPart += 1;
                }
            }

        } else {
            iFirst = 0;
            iSecond = 0;
        }

        String sFinalValue = intPart + "." + iFirst + "" + iSecond;

        return Double.valueOf(sFinalValue);
    }

}
