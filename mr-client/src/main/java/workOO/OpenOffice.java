package workOO;

import by.gomel.freedev.ucframework.uccore.report.OfficeBootStrap;
import by.gomel.freedev.ucframework.uccore.report.connector.BootstrapSocketConnector;
import by.march8.ecs.framework.common.LogCrutch;
import com.sun.star.beans.PropertyValue;
import com.sun.star.frame.XComponentLoader;
import com.sun.star.frame.XDesktop;
import com.sun.star.lang.XComponent;
import com.sun.star.lang.XMultiComponentFactory;
import com.sun.star.sheet.XSpreadsheet;
import com.sun.star.sheet.XSpreadsheetDocument;
import com.sun.star.sheet.XSpreadsheets;
import com.sun.star.table.XCell;
import com.sun.star.uno.UnoRuntime;
import com.sun.star.uno.XComponentContext;
import com.sun.star.util.XCloseable;
import com.sun.star.util.XURLTransformer;
import dept.MyReportsModule;
import dept.marketing.CartForm;
import dept.marketing.OrdersForm;
import dept.marketing.SkladForm;
import dept.tech.EanList;
import workDB.DB;
import workDB.PDB;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import static common.UtilFunctions.convertDateStrToLong;

public class OpenOffice {

    //private static final Logger log = new Log().getLoger(OpenOffice.class);
    private static final LogCrutch log = new LogCrutch();
    static int uS;
    static int uE;
    static int planE;
    static int planD;
    static int krDays;
    static int trDay;
    static int idValuta;
    static int kurs;
    static int nds;
    static int idOrder;
    static String noteOrder;
    static String dateOrder;
    static String nameCl;
    static String str;
    static CartForm cartForm;
    static SkladForm skladForm;
    static OrdersForm orderForm;
    static DefaultTableModel tModel;
    static PDB pdb;
    static Vector vec;
    static Vector dopVec;
    private static XComponentContext xRemoteContext = null;
    private static XMultiComponentFactory xRemoteServiceManager = null;
    private static XURLTransformer xTransformer = null;
    private static XComponentLoader xComponentLoader = null;
    private static XDesktop xDesktop = null;
    private static PropertyValue[] loadProps = new PropertyValue[0];
    //private static int bL[] = {101, 102, 103, 104, 105, 106, 107, 108, 121, 122, 123, 124, 125, 126, 127, 128};
    private static ArrayList brigList = new ArrayList();
    private static String sDate;
    private static String eDate;
    private static String zSDate;
    private static String zEDate;
    private static String nameTamplate;

    public OpenOffice() {
        //--бригады пошива
        brigList.add(101);
        brigList.add(102);
        brigList.add(103);
        brigList.add(104);
        brigList.add(105);
        brigList.add(106);
        brigList.add(107);
        brigList.add(108);
        brigList.add(121);
        brigList.add(122);
        brigList.add(123);
        brigList.add(124);
        brigList.add(125);
        brigList.add(126);
        brigList.add(127);
        brigList.add(128);
        //-- бригады закроя
        brigList.add(152);
        brigList.add(153);
        brigList.add(154);
        brigList.add(155);
        brigList.add(156);
        brigList.add(157);
        brigList.add(158);
        brigList.add(159);
        brigList.add(160);
        brigList.add(161);
        brigList.add(162);
        brigList.add(163);
        brigList.add(164);
        brigList.add(165);
        brigList.add(166);
        brigList.add(167);
    }


    public OpenOffice(String sDate, String eDate, String nameReport, String zSDate, String zEDate, int uS, int uE, int planE, int planD, int krDays, int trDay) {
        this();
        OpenOffice.sDate = new String(sDate);
        OpenOffice.eDate = new String(eDate);
        OpenOffice.zSDate = new String(zSDate);
        OpenOffice.zEDate = new String(zEDate);
        OpenOffice.uS = uS;
        OpenOffice.uE = uE;
        OpenOffice.planE = planE;
        OpenOffice.planD = planD;
        OpenOffice.krDays = krDays;
        OpenOffice.trDay = trDay;
    }

    public OpenOffice(JDialog aThis, int idOrder, String namecl, String date, int param, int idvaluta, int kurs, int nds, String noteOrder) {
        this();
        OpenOffice.idOrder = idOrder;
        nameCl = new String(namecl);
        idValuta = idvaluta;
        OpenOffice.kurs = kurs;
        OpenOffice.nds = nds;
        dateOrder = date;
        OpenOffice.noteOrder = new String(noteOrder);
        switch (param) {
            case 1:
                cartForm = (CartForm) aThis;
                pdb = cartForm.pdb;
                break;
            case 2:
                skladForm = (SkladForm) aThis;
                pdb = skladForm.pdb;
                break;
            case 3:
                orderForm = (OrdersForm) aThis;
                pdb = orderForm.pdb;
                break;
            default:
                break;
        }
    }

    public OpenOffice(String nameReport, Vector v, String nameClient, String sD, String eD) {
        this();
        vec = v;
        nameCl = nameClient;
        sDate = sD;
        eDate = eD;
        nameTamplate = nameReport;
    }

    public OpenOffice(String nameReport, DefaultTableModel tm) {
        this();
        tModel = tm;
        nameTamplate = new String(nameReport);
    }

    public OpenOffice(String nameReport, DefaultTableModel tm, Vector v) {
        this();
        tModel = tm;
        nameTamplate = new String(nameReport);
        vec = v;
    }

    public OpenOffice(String nameReport, DefaultTableModel tm, Vector v, String s) {
        this();
        tModel = tm;
        nameTamplate = new String(nameReport);
        vec = v;
        str = s;
    }

    /**
     * @deprecated
     */
    public OpenOffice(String nameReport, Vector v) {
        this();
        vec = v;
        nameTamplate = new String(nameReport);
    }

    /**
     * @param nameReport название отчёта
     * @param v          вектор с данными
     * @param dopv       дополнительные параметры
     */
    public OpenOffice(String nameReport, Vector v, Vector dopv) {
        this();
        vec = v;
        dopVec = dopv;
        nameTamplate = new String(nameReport);
    }

    /* соедиенение с экземпляром OpenOffice.org
     * и инициализация нужных переменных
     */

    public static void connect() {
        // получим контекст удаленного компонента офиса

        try {
            String oooExeFolder;
/*            java.io.File fC = new java.io.File("c:\\windows\\explorer.exe");
            if(!fC.exists()){
                oooExeFolder = "/usr/bin/soffice";
            }else oooExeFolder = "c://Program Files//OpenOffice.org 3//program//";*/

            OfficeBootStrap office = OfficeBootStrap.getInstance();
            oooExeFolder = office.getBootPath();

            XComponentContext xContext = BootstrapSocketConnector.bootstrap(oooExeFolder, false);

            xRemoteServiceManager = xContext.getServiceManager();
            // создадим сервис, который понадобится при печати
            Object transformer = xRemoteServiceManager.createInstanceWithContext("com.sun.star.util.URLTransformer", xContext);
            xTransformer = UnoRuntime.queryInterface(XURLTransformer.class, transformer);
            //Object transformer = xRemoteServiceManager.createInstanceWithContext();

            // получим сервис Desktop
            Object desktop = xRemoteServiceManager.createInstanceWithContext(
                    "com.sun.star.frame.Desktop", xContext);
            xDesktop = UnoRuntime.queryInterface(
                    XDesktop.class, desktop);

            // данный интерфейс позволяет загружать и сохранять документы
            xComponentLoader = UnoRuntime.queryInterface(
                    XComponentLoader.class, desktop);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            JOptionPane.showMessageDialog(null, "Ошибка соединения с OpenOffice.", "Ошибка!!!", JOptionPane.ERROR_MESSAGE);
            log.error("Ошибка соединения с OpenOffice:", e);
        }
    }

    public static XComponent openDocument(String sURL) {
        // преобразуем путь к файлу в URL

        try {
            //java.io.File sourceFile = new java.io.File("");
            StringBuffer sTmp = new StringBuffer("file:///");
            //sTmp.append(MainForm.progPath);
            sTmp.append(MyReportsModule.progPath);
            sTmp.append("/");


            sURL = sTmp.toString().replace('\\', '/') + sURL;
            return xComponentLoader.loadComponentFromURL(sURL, "_blank", 0, loadProps);
        } catch (Exception e) {
            log.error("Ошибка открытия шаблона " + sURL, e);
            System.out.println(e.getMessage());
            JOptionPane.showMessageDialog(null, "Ошибка открытия шаблона " + sURL, "Ошибка!!!", JOptionPane.ERROR_MESSAGE);
        }
        //log.warn("Открытие шаблона "+ sURL +" прошло нормально");
        return null;
    }

    /* Закрыть документ, если askIfVetoed=true, то в случае попытки закрытия
     * документа с несохраненными изменениями будет выдан соответствующий запрос
     */
    public static boolean closeDocument(XComponent comp, boolean askIfVetoed) {
        XCloseable c = UnoRuntime.queryInterface(XCloseable.class, comp);
        boolean dispose = true;
        try {
            c.close(false);
        } catch (com.sun.star.util.CloseVetoException e) {
            if (askIfVetoed) {
                int action = JOptionPane.showConfirmDialog(null, "Есть несохраненные изменения!" +
                                "Действительно хотите закрыть?",
                        "Внимание", JOptionPane.WARNING_MESSAGE, JOptionPane.YES_NO_OPTION);
                if (JOptionPane.NO_OPTION == action) {
                    dispose = false;
                }
            }
            if (dispose) {
                comp.dispose();
            }
        }
        return dispose;
    }

    /* Сохранить документ под именем, на которое указывает aURL
     */
	/*public static void saveAsDocument(XComponent comp, PropertyValue[] props) {
	    XStorable store = (XStorable)UnoRuntime.queryInterface(XStorable.class, comp);
	    String aURL = new String("Report/" + sMDate.substring(3, sMDate.length()) + "/" + nameReport);
	    try {
	    	String fname =  new String(aURL+".ods");
	        //System.out.println(fname);

	        java.io.File sourceFile = new java.io.File(aURL);
	  	    StringBuffer sTmp = new StringBuffer("file:///");
	  	    sTmp.append(sourceFile.getCanonicalPath().replace('\\', '/'));
	  	    aURL = sTmp.toString();
	        //System.out.println(aURL);


	        File f =  new File(fname);
	        if (f.exists()) {
	        	Calendar calendar = GregorianCalendar.getInstance();
	        	aURL +=  " (" + calendar.getTime().toString() + ").ods";
	        }else aURL += ".ods";

	       // System.out.println(aURL);

	    	store.storeToURL(aURL, props);
	    }catch (Exception e) {
	      System.out.println( "Не могу сохранить файл!" + e );
		 }
	}*/

    /**
     * заполнение шаблона о выпущенной продукции по ЗШ цеху
     *
     * @param currentDocument
     */
    public static void fildInfProdZakrSh(XComponent currentDocument) {

        DB db = null;
        try {
            XSpreadsheetDocument xSpreadsheetDocument = UnoRuntime.queryInterface(XSpreadsheetDocument.class, currentDocument);

            XSpreadsheets xSpreadsheets = xSpreadsheetDocument.getSheets();
            Object sheet = xSpreadsheets.getByName("Лист1");
            XSpreadsheet xSpreadsheet = UnoRuntime.queryInterface(XSpreadsheet.class, sheet);

            db = new DB();
//            wDBF dbf = new wDBF();

            long sd = convertDateStrToLong(eDate);
            long ed = convertDateStrToLong(eDate);

            XCell xCell = xSpreadsheet.getCellByPosition(0, 0);

//----------------Заполнение шапки документа
            xCell = xSpreadsheet.getCellByPosition(1, 1);
            xCell.setFormula(xCell.getFormula() + " " + eDate);

            xCell = xSpreadsheet.getCellByPosition(9, 1);
            xCell.setValue(krDays);

            xCell = xSpreadsheet.getCellByPosition(1, 2);
            xCell.setValue(trDay);

            xCell = xSpreadsheet.getCellByPosition(6, 2);
            xCell.setValue(planE);
            xCell = xSpreadsheet.getCellByPosition(8, 2);
            xCell.setValue(planD);

//----------------Заполнение бригад пошива за день выпуск--------------------
            Object obj = null;
            int b = 0;
            //int k;
            for (int i = 15; i < 34; i++) {
                xCell = xSpreadsheet.getCellByPosition(6, i);

                obj = xCell.getValue();
                b = (int) Float.parseFloat(obj.toString());

                if (brigList.contains(b)) {
                    //db.poshivBrig(sd, ed, b);
                    xCell = xSpreadsheet.getCellByPosition(9, i);
                    xCell.setValue(db.poshivBrig(sd, ed, b));
                }
                //System.out.println(b + " " +  xCell.getValue());
            }
/*
//----------------Заполнение остатков пошива верх--------------------
            xCell = xSpreadsheet.getCellByPosition(5, 9);
	        xCell.setValue(dbf.ostatkiVerh(eMDate.substring(3, 5)));

//----------------Заполнение остатков закроя верх--------------------
            xCell = xSpreadsheet.getCellByPosition(4, 9);
	        xCell.setValue(dbf.ostatkiKroyVerh(eMDate.substring(3, 5)));*/

//----------------Заполнение стоимости пошива день--------------------
            xCell = xSpreadsheet.getCellByPosition(9, 35);
            xCell.setValue(db.sumUpacovPrin(sd, ed, false));

//----------------Заполнение стоимости пошива день c учётом сорта--------------------
            xCell = xSpreadsheet.getCellByPosition(4, 42);
            xCell.setValue(db.sumUpacovPrin(sd, ed, true));


//----------------Заполнение стоимости запуска день--------------------
            xCell = xSpreadsheet.getCellByPosition(7, 35);
            xCell.setValue(db.sumZapusk(sd, ed));

//----------------Заполнение стоимости закроя день-----------------------------------
            xCell = xSpreadsheet.getCellByPosition(2, 35);
            xCell.setValue(db.sumZakr(convertDateStrToLong(zEDate), convertDateStrToLong(zEDate)));


//----------------Заполнение бригад закроя за день верх--------------------
            xCell = xSpreadsheet.getCellByPosition(2, 9);
            xCell.setValue(db.zakroiVerh(convertDateStrToLong(zEDate), convertDateStrToLong(zEDate)));

//----------------Заполнение бригад пошива за день выпуск верх--------------------
            xCell = xSpreadsheet.getCellByPosition(9, 9);
            xCell.setValue(db.poshivVerh(ed, ed));

//----------------Заполнение запуска за день верх--------------------
            xCell = xSpreadsheet.getCellByPosition(7, 9);
            xCell.setValue(db.zapuskVerh(ed, ed));

//	      ----------------Заполнение запуска бригад пошива за день --------------------

            for (int i = 15; i < 34; i++) {
                xCell = xSpreadsheet.getCellByPosition(6, i);

                obj = xCell.getValue();
                b = (int) Float.parseFloat(obj.toString());

                if (brigList.contains(b)) {
                    //db.poshivBrig(sd, ed, b);

                    xCell = xSpreadsheet.getCellByPosition(7, i);
                    xCell.setValue(db.zapuskBrig(sd, ed, b));
                }

            }

//		    ----------------Заполнение бригад закроя за день --------------------

            for (int i = 27; i < 34; i++) {
                xCell = xSpreadsheet.getCellByPosition(1, i);

                obj = xCell.getValue();
                b = (int) Float.parseFloat(obj.toString());

                if (brigList.contains(b)) {
                    //db.poshivBrig(sd, ed, b);
                    xCell = xSpreadsheet.getCellByPosition(2, i);
                    xCell.setValue(db.zakroyBrig(convertDateStrToLong(zEDate), convertDateStrToLong(zEDate), b));
                }
            }

            //----------------Заполнение бригад пошива за месяц  выпуск--------------------
            sd = convertDateStrToLong(sDate);
            ed = convertDateStrToLong(eDate);
            obj = null;
            b = 0;
            for (int i = 15; i < 34; i++) {
                xCell = xSpreadsheet.getCellByPosition(6, i);

                obj = xCell.getValue();
                b = (int) Float.parseFloat(obj.toString());

                if (brigList.contains(b)) {
                    xCell = xSpreadsheet.getCellByPosition(10, i);
                    xCell.setValue(db.poshivBrig(sd, ed, b));
                }
            }

//----------------Заполнение бригад пошива за месяц выпуск верх--------------------
            xCell = xSpreadsheet.getCellByPosition(10, 9);
            xCell.setValue(db.poshivVerh(sd, ed));

//----------------Заполнение запуска закроя за месяц верх--------------------
            xCell = xSpreadsheet.getCellByPosition(8, 9);
            xCell.setValue(db.zapuskVerh(sd, ed));

//----------------Заполнение бригад закроя за месяц верх--------------------
            xCell = xSpreadsheet.getCellByPosition(3, 9);
            xCell.setValue(db.zakroiVerh(convertDateStrToLong(zSDate), convertDateStrToLong(zEDate)));

//----------------Заполнение запуска бригад пошива за месяц --------------------
            obj = null;
            b = 0;
            for (int i = 15; i < 34; i++) {
                xCell = xSpreadsheet.getCellByPosition(6, i);

                obj = xCell.getValue();
                b = (int) Float.parseFloat(obj.toString());

                if (brigList.contains(b)) {
                    xCell = xSpreadsheet.getCellByPosition(8, i);
                    xCell.setFormula("=" + db.zapuskBrig(sd, ed, b) + "+F" + (i + 1));
                }
            }

//		      ----------------Заполнение бригад закроя за месяц --------------------
            for (int i = 27; i < 34; i++) {
                xCell = xSpreadsheet.getCellByPosition(1, i);
                obj = xCell.getValue();
                b = (int) Float.parseFloat(obj.toString());
                if (brigList.contains(b)) {
                    xCell = xSpreadsheet.getCellByPosition(3, i);
                    xCell.setValue(db.zakroyBrig(convertDateStrToLong(zSDate), convertDateStrToLong(zEDate), b));
                }
            }

//----------------Заполнение стоимости пошива месяц--------------------
            xCell = xSpreadsheet.getCellByPosition(10, 35);
            xCell.setValue(db.sumUpacovPrin(sd, ed, false));

//----------------Заполнение стоимости запуска месяц--------------------
            xCell = xSpreadsheet.getCellByPosition(8, 35);
            xCell.setValue(db.sumZapusk(sd, ed));

//----------------Сдано упаковкой на склад за текущий период--------------------
            xCell = xSpreadsheet.getCellByPosition(2, 38);
            xCell.setValue(db.upacovSdano(sd, ed, uS, false));

//----------------Сдано упаковкой на склад за текущий период--------------------
            xCell = xSpreadsheet.getCellByPosition(3, 38);
            xCell.setValue(db.sumUpacovSdano(sd, ed, uS, false));

//----------------Сдано упаковкой на склад за текущий период 3й сорт --------------------
            xCell = xSpreadsheet.getCellByPosition(2, 40);
            xCell.setValue(db.upacovSdano(sd, ed, uS, true));

//----------------Стадно упаковкой на склад за текущий период 3й сорт --------------------
            xCell = xSpreadsheet.getCellByPosition(3, 40);
            xCell.setValue(db.sumUpacovSdano(sd, ed, uS, true));

//----------------Заполнение стоимости пошива месяц c учётом сорта--------------------
            xCell = xSpreadsheet.getCellByPosition(3, 42);
            xCell.setValue(db.sumUpacovPrin(sd, ed, true));

//----------------Заполнение стоимости закроя месяц-----------------------------------
            xCell = xSpreadsheet.getCellByPosition(3, 35);
            xCell.setValue(db.sumZakr(convertDateStrToLong(zSDate), convertDateStrToLong(zEDate)));

//----------------Заполнение стоимости закроя месяц-----------------------------------
            //   xCell = xSpreadsheet.getCellByPosition(2, 42);
//	        xCell.setValue(db.sdanoNaUpacov(sd, ed));

	     /*   xCell = xSpreadsheet.getCellByPosition(0, 0);
	        xCell.setValue(21);
	        xCell = xSpreadsheet.getCellByPosition(0, 1);
	        xCell.setValue(21);
	        xCell = xSpreadsheet.getCellByPosition(0, 2);
	        xCell.setFormula("=sum(A1:A2)");

	        XPropertySet xCellProps = (XPropertySet)UnoRuntime.queryInterface(XPropertySet.class, xCell);
	        xCellProps.setPropertyValue("CellStyle", "Result");*/

	       /* XModel xSpreadsheetModel = (XModel)UnoRuntime.queryInterface(XModel.class, currentDocument);
	        XController xSpreadsheetController = xSpreadsheetModel.getCurrentController();
	        XSpreadsheetView xSpreadsheetView = (XSpreadsheetView)
	            UnoRuntime.queryInterface(XSpreadsheetView.class, xSpreadsheetController);
	        xSpreadsheetView.setActiveSheet(xSpreadsheet);*/

            // *********************************************************
            // example for use of enum types
            // xCellProps.setPropertyValue("VertJustify", com.sun.star.table.CellVertJustify.TOP);


            // *********************************************************
            // example for a sequence of PropertyValue structs
            // create an array with one PropertyValue struct, it contains
            // references only
            // loadProps = new PropertyValue[1];

            // instantiate PropertyValue struct and set its member fields
	      /*  PropertyValue asTemplate = new PropertyValue();
	        asTemplate.Name = "AsTemplate";
	        asTemplate.Value = new Boolean(true);*/
        } catch (java.lang.Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }


    public static void fildEanPlan(XComponent currentDocument) {
        Vector v = new Vector();
        Iterator it;

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

            EanList el = new EanList();
            v = el.createList();

            it = v.iterator();

            XCell xCell = xSpreadsheet.getCellByPosition(0, 0);

//----------------Заполнение шапки документа
            xCell.setFormula(xCell.getFormula() + " " + el.getMonth() + " месяц");

//----------------Заполнение тела документа
            int i = 2;
            long fas = 0;
            long oldFas = 0;
            while (it.hasNext()) {
                String name = new String(((String) it.next()).trim());
                fas = (Long) it.next();
                if (fas != oldFas) {
                    i++;
                    oldFas = fas;
                }
                xCell = xSpreadsheet.getCellByPosition(0, i);
                xCell.setFormula(name);
                xPropSet = UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
                xPropSet.setPropertyValue("TableBorder", aBorder);

                xCell = xSpreadsheet.getCellByPosition(1, i);
                xCell.setValue(fas);
                xPropSet = UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
                xPropSet.setPropertyValue("TableBorder", aBorder);
                xCell = xSpreadsheet.getCellByPosition(2, i);
                xCell.setFormula((String) it.next());
                xPropSet = UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
                xPropSet.setPropertyValue("TableBorder", aBorder);
                xCell = xSpreadsheet.getCellByPosition(3, i);
                xCell.setValue((Long) it.next());
                xPropSet = UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
                xPropSet.setPropertyValue("TableBorder", aBorder);

                xCell = xSpreadsheet.getCellByPosition(4, i);
                xCell.setFormula((String) it.next());
                xPropSet = UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
                xPropSet.setPropertyValue("TableBorder", aBorder);

                xCell = xSpreadsheet.getCellByPosition(5, i);
                xCell.setFormula((String) it.next());
                xPropSet = UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
                xPropSet.setPropertyValue("TableBorder", aBorder);
                i++;
            }

        } catch (java.lang.Exception e) {
            System.out.println(e.getMessage());
            log.error("Ошибка при заполнении отчёта EanPlan", e);
            JOptionPane.showMessageDialog(null, "Ошибка при заполнении отчёта EanPlan", "Ошибка!!!", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void fildTrudozatPoshiv(XComponent currentDocument) {
        DB db = null;
        PDB pdb = null;

        try {
            db = new DB();
            pdb = new PDB();
            long sd = convertDateStrToLong(sDate);
            long ed = convertDateStrToLong(eDate);
            long sdekada = 0;

            XSpreadsheetDocument xSpreadsheetDocument = UnoRuntime.queryInterface(XSpreadsheetDocument.class, currentDocument);

            XSpreadsheets xSpreadsheets = xSpreadsheetDocument.getSheets();

            Object sheet = xSpreadsheets.getByName("Лист1");
            XSpreadsheet xSpreadsheet = UnoRuntime.queryInterface(XSpreadsheet.class, sheet);
            XCell xCell = xSpreadsheet.getCellByPosition(0, 0);

//----------------Заполнение шапки документа
            xCell.setFormula(xCell.getFormula() + " " + sDate + " по " + eDate);
            Date d = new Date(ed);
            Calendar c = GregorianCalendar.getInstance();
            c.setTime(d);
            if (c.get(Calendar.DAY_OF_MONTH) > 10) {
                c.set(Calendar.DAY_OF_MONTH, 11);
                sdekada = c.getTimeInMillis();
            } else if (c.get(Calendar.DAY_OF_MONTH) > 20) {
                c.set(Calendar.DAY_OF_MONTH, 21);
                sdekada = c.getTimeInMillis();
            } else {
                sdekada = sd;
            }
            /*c.set(Calendar.DAY_OF_MONTH, 1);
            smonth = c.getTimeInMillis();*/


//----------------Заполнение тела документа
            Object obj = null;
            int b = 0;
            float zatr = 0;
            List items;
            Iterator it = null;
            for (int i = 7; i <= 25; i++) {
                xCell = xSpreadsheet.getCellByPosition(0, i);
                obj = xCell.getValue();
                b = (int) Float.parseFloat(obj.toString());
                if (brigList.contains(b)) {
                    //---------- заполнение штук
                    //заполнение за день
                    xCell = xSpreadsheet.getCellByPosition(1, i);
                    xCell.setValue(db.poshivBrig(ed, ed, b));
                    //заполнение за мдекаду
                    xCell = xSpreadsheet.getCellByPosition(2, i);
                    xCell.setValue(db.poshivBrig(sdekada, ed, b));
                    //заполнение за месяц
                    xCell = xSpreadsheet.getCellByPosition(3, i);
                    xCell.setValue(db.poshivBrig(sd, ed, b));
                    //---------- заполнение млн руб
                    //заполнение за день

                    xCell = xSpreadsheet.getCellByPosition(4, i);
                    xCell.setValue(db.sumPoshivBrig(ed, ed, b));
                    //заполнение за мдекаду
                    xCell = xSpreadsheet.getCellByPosition(5, i);
                    xCell.setValue(db.sumPoshivBrig(sdekada, ed, b));
                    //заполнение за месяц
                    xCell = xSpreadsheet.getCellByPosition(6, i);
                    xCell.setValue(db.sumPoshivBrig(sd, ed, b));

                    //-------------заполнение трудозатрат
                    //заполнение за день
                    items = db.trudoZatBrig(ed, ed, b);
                    it = items.iterator();
                    zatr = 0;
                    while (it.hasNext()) {
                        zatr += pdb.getTrudoZat((Integer) it.next(), (Integer) it.next(), (Integer) it.next());
                    }
                    xCell = xSpreadsheet.getCellByPosition(7, i);
                    xCell.setValue(zatr);
                    //заполнение за декаду
                    items = db.trudoZatBrig(sdekada, ed, b);
                    it = items.iterator();
                    zatr = 0;
                    while (it.hasNext()) {
                        zatr += pdb.getTrudoZat((Integer) it.next(), (Integer) it.next(), (Integer) it.next());
                    }
                    xCell = xSpreadsheet.getCellByPosition(8, i);
                    xCell.setValue(zatr);
                    //заполнение за месяц
                    items = db.trudoZatBrig(sd, ed, b);
                    it = items.iterator();
                    zatr = 0;
                    while (it.hasNext()) {
                        zatr += pdb.getTrudoZat((Integer) it.next(), (Integer) it.next(), (Integer) it.next());
                    }
                    xCell = xSpreadsheet.getCellByPosition(9, i);
                    xCell.setValue(zatr);
                }
            }
        } catch (java.lang.Exception e) {
            System.out.println(e.getMessage());
            log.error("Ошибка при заполнении отчёта Трудозатрат пошива", e);
            JOptionPane.showMessageDialog(null, "Ошибка при заполнении отчёта Трудозатрат пошива", "Ошибка!!!", JOptionPane.ERROR_MESSAGE);
        } finally {
            pdb.disConn();
        }
    }

    public static void fildPlanPoshivN4(XComponent currentDocument) {
        DB db = null;
        PDB pdb = null;

        try {
            db = new DB();
            pdb = new PDB();
            long sd = convertDateStrToLong(sDate);
            long ed = convertDateStrToLong(eDate);
            //начало декад
            long sdekada1 = 0;
            long sdekada2 = 0;
            long sdekada3 = 0;
            //конец декад
            long edekada1 = 0;
            long edekada2 = 0;
            long edekada3 = 0;
            long month = 0;

            XSpreadsheetDocument xSpreadsheetDocument = UnoRuntime.queryInterface(XSpreadsheetDocument.class, currentDocument);

            XSpreadsheets xSpreadsheets = xSpreadsheetDocument.getSheets();

            Object sheet = xSpreadsheets.getByName("Лист1");
            XSpreadsheet xSpreadsheet = UnoRuntime.queryInterface(XSpreadsheet.class, sheet);

            XCell xCell = xSpreadsheet.getCellByPosition(13, 4);

//----------------Заполнение шапки документа
            xCell = xSpreadsheet.getCellByPosition(0, 0);
            xCell.setFormula(xCell.getFormula() + " " + sDate + " по " + eDate);
            sdekada1 = sd;
            Date d = new Date(ed);
            Calendar c = GregorianCalendar.getInstance();
            c.setTime(d);
            c.set(Calendar.DAY_OF_MONTH, 11);
            sdekada2 = c.getTimeInMillis();
            c.set(Calendar.DAY_OF_MONTH, 21);
            sdekada3 = c.getTimeInMillis();
            month = sd;

            c.setTime(d);
            if (c.get(Calendar.DAY_OF_MONTH) > 10) {
                c.set(Calendar.DAY_OF_MONTH, 10);
                edekada1 = c.getTimeInMillis();
            } else edekada1 = ed;
            if (c.get(Calendar.DAY_OF_MONTH) > 20) {
                c.set(Calendar.DAY_OF_MONTH, 20);
                edekada2 = c.getTimeInMillis();
            } else edekada2 = ed;
            edekada3 = ed;
//----------------Заполнение тела документа
            Object obj = null;
            int b = 0;
            float zatr = 0;
            List items = new ArrayList();
            Iterator it = null;
            float days = (dept.poshiv.DataForm.getDek1() + dept.poshiv.DataForm.getDek2() + dept.poshiv.DataForm.getDek3());
            for (int i = 8; i <= 26; i++) {
                xCell = xSpreadsheet.getCellByPosition(0, i);
                obj = xCell.getValue();
                b = (int) Float.parseFloat(obj.toString());
                if (brigList.contains(b)) {

                    xCell = xSpreadsheet.getCellByPosition(1, i);
                    //"=(P*D*B)/(L*M)"
                    xCell.setFormula("=(" + dept.poshiv.DataForm.getFrv() + "*" + dept.poshiv.DataForm.getDek1() + "*N" + (i + 1) + ")" + "/(N5*" + days + ")");
                    xCell = xSpreadsheet.getCellByPosition(4, i);

                    xCell.setFormula("=(" + dept.poshiv.DataForm.getFrv() + "*" + dept.poshiv.DataForm.getDek2() + "*N" + (i + 1) + ")" + "/(N5*" + days + ")");
                    xCell = xSpreadsheet.getCellByPosition(7, i);

                    xCell.setFormula("=(" + dept.poshiv.DataForm.getFrv() + "*" + dept.poshiv.DataForm.getDek3() + "*N" + (i + 1) + ")" + "/(N5*" + days + ")");
                    xCell = xSpreadsheet.getCellByPosition(10, i);

                    xCell.setFormula("=(" + dept.poshiv.DataForm.getFrv() + "*" + days + "*N" + (i + 1) + ")" + "/(N5*" + days + ")");
                    xCell = xSpreadsheet.getCellByPosition(0, i);
                    //-------------заполнение трудозатрат
                    //заполнение за 1-ю декаду
                    items = db.trudoZatBrig(sdekada1, edekada1, b);
                    it = items.iterator();
                    zatr = 0;
                    while (it.hasNext()) {
                        zatr += pdb.getTrudoZat((Integer) it.next(), (Integer) it.next(), (Integer) it.next());
                    }
                    xCell = xSpreadsheet.getCellByPosition(2, i);
                    xCell.setValue(zatr);
                    //заполнение за 2 декаду
                    items = db.trudoZatBrig(sdekada2, edekada2, b);
                    it = items.iterator();
                    zatr = 0;
                    while (it.hasNext()) {
                        zatr += pdb.getTrudoZat((Integer) it.next(), (Integer) it.next(), (Integer) it.next());
                    }
                    xCell = xSpreadsheet.getCellByPosition(5, i);
                    xCell.setValue(zatr);
                    //заполнение за 2 декаду
                    items = db.trudoZatBrig(sdekada3, edekada3, b);
                    it = items.iterator();
                    zatr = 0;
                    while (it.hasNext()) {
                        zatr += pdb.getTrudoZat((Integer) it.next(), (Integer) it.next(), (Integer) it.next());
                    }
                    xCell = xSpreadsheet.getCellByPosition(8, i);
                    xCell.setValue(zatr);
                    //заполнение за месяц
                    items = db.trudoZatBrig(month, ed, b);
                    it = items.iterator();
                    zatr = 0;
                    while (it.hasNext()) {
                        zatr += pdb.getTrudoZat((Integer) it.next(), (Integer) it.next(), (Integer) it.next());
                    }
                    xCell = xSpreadsheet.getCellByPosition(11, i);
                    xCell.setValue(zatr);
                }
            }
        } catch (java.lang.Exception e) {
            System.out.println(e.getMessage());
            log.error("Ошибка при заполнении отчёта план пошива в нормочасах", e);
            JOptionPane.showMessageDialog(null, "Ошибка при заполнении отчёта план пошива в нормочасах", "Ошибка!!!", JOptionPane.ERROR_MESSAGE);
        } finally {
            pdb.disConn();
        }
    }

    public static void fildPlanPoshivMR(XComponent currentDocument) {
        DB db = null;
        PDB pdb = null;

        try {
            db = new DB();
            pdb = new PDB();
            long sd = convertDateStrToLong(sDate);
            long ed = convertDateStrToLong(eDate);
            //начало декад
            long sdekada1 = 0;
            long sdekada2 = 0;
            long sdekada3 = 0;
            //конец декад
            long edekada1 = 0;
            long edekada2 = 0;
            long edekada3 = 0;
            long month = 0;
            float plan = (float) dept.poshiv.DataForm.getPlan() / (float) (dept.poshiv.DataForm.getDek1() + dept.poshiv.DataForm.getDek2() + dept.poshiv.DataForm.getDek3()) / (float) 16;

            XSpreadsheetDocument xSpreadsheetDocument = UnoRuntime.queryInterface(XSpreadsheetDocument.class, currentDocument);

            XSpreadsheets xSpreadsheets = xSpreadsheetDocument.getSheets();

            Object sheet = xSpreadsheets.getByName("Лист1");
            XSpreadsheet xSpreadsheet = UnoRuntime.queryInterface(XSpreadsheet.class, sheet);
            XCell xCell = xSpreadsheet.getCellByPosition(0, 0);

//----------------Заполнение шапки документа
            xCell.setFormula(xCell.getFormula() + " " + sDate + " по " + eDate);
            sdekada1 = sd;
            Date d = new Date(ed);
            Calendar c = GregorianCalendar.getInstance();
            c.setTime(d);
            c.set(Calendar.DAY_OF_MONTH, 11);
            sdekada2 = c.getTimeInMillis();
            c.set(Calendar.DAY_OF_MONTH, 21);
            sdekada3 = c.getTimeInMillis();
            month = sd;

            c.setTime(d);
            if (c.get(Calendar.DAY_OF_MONTH) > 10) {
                c.set(Calendar.DAY_OF_MONTH, 10);
                edekada1 = c.getTimeInMillis();
            } else edekada1 = ed;
            if (c.get(Calendar.DAY_OF_MONTH) > 20) {
                c.set(Calendar.DAY_OF_MONTH, 20);
                edekada2 = c.getTimeInMillis();
            } else edekada2 = ed;
            edekada3 = ed;
//----------------Заполнение тела документа
            Object obj = null;
            int b = 0;
            for (int i = 8; i <= 26; i++) {
                xCell = xSpreadsheet.getCellByPosition(0, i);
                obj = xCell.getValue();
                b = (int) Float.parseFloat(obj.toString());
                if (brigList.contains(b)) {
                    xCell = xSpreadsheet.getCellByPosition(1, i);
                    xCell.setValue(plan * dept.poshiv.DataForm.getDek1());
                    xCell = xSpreadsheet.getCellByPosition(4, i);
                    xCell.setValue(plan * dept.poshiv.DataForm.getDek2());
                    xCell = xSpreadsheet.getCellByPosition(7, i);
                    xCell.setValue(plan * dept.poshiv.DataForm.getDek3());
                    xCell = xSpreadsheet.getCellByPosition(10, i);
                    xCell.setValue(plan * (dept.poshiv.DataForm.getDek1() + dept.poshiv.DataForm.getDek2() + dept.poshiv.DataForm.getDek3()));
                    xCell = xSpreadsheet.getCellByPosition(0, i);
                    //заполнение за 1-ю декаду
                    xCell = xSpreadsheet.getCellByPosition(2, i);
                    xCell.setValue(db.sumPoshivBrig(sdekada1, edekada1, b));
                    //заполнение за 2-ю декаду
                    xCell = xSpreadsheet.getCellByPosition(5, i);
                    xCell.setValue(db.sumPoshivBrig(sdekada2, edekada2, b));
                    //заполнение за 3-ю декаду
                    xCell = xSpreadsheet.getCellByPosition(8, i);
                    xCell.setValue(db.sumPoshivBrig(sdekada3, edekada3, b));
                    //заполнение за месяц
                    xCell = xSpreadsheet.getCellByPosition(11, i);
                    xCell.setValue(db.sumPoshivBrig(month, ed, b));
                }
            }
        } catch (java.lang.Exception e) {
            System.out.println(e.getMessage());
            log.error("Ошибка при заполнении отчёта план пошива в млн руб", e);
            JOptionPane.showMessageDialog(null, "Ошибка при заполнении отчёта план пошива в млн руб", "Ошибка!!!", JOptionPane.ERROR_MESSAGE);
        } finally {
            pdb.disConn();
        }
    }

    public static void fildPrinZdanoUpack(XComponent currentDocument) {
        DB db = null;
        PDB pdb = null;
        Vector date = new Vector(tModel.getDataVector());

        try {
            db = new DB();
            pdb = new PDB();

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
            xCell.setFormula(nameTamplate);

//----------------Заполнение тела документа
            int nRow = 2;
            for (Object row : date) {
                for (int i = 0; i < 9; i++) {
                    xCell = xSpreadsheet.getCellByPosition(i, nRow);
                    xCell.setFormula(((Vector) row).get(i).toString());
                    xPropSet = UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
                    xPropSet.setPropertyValue("TableBorder", aBorder);
                }
                nRow++;
            }
        } catch (java.lang.Exception e) {
            System.out.println(e.getMessage());
            log.error("Ошибка при заполнении отчёта принято/сдано упаковкой", e);
            JOptionPane.showMessageDialog(null, "Ошибка при заполнении отчёта принято/сдано упаковкой", "Ошибка!!!", JOptionPane.ERROR_MESSAGE);
        } finally {
            pdb.disConn();
        }
    }

    public static void fildOstatki(XComponent currentDocument) {
        DB db = null;
        PDB pdb = null;
        Vector date = new Vector(tModel.getDataVector());

        try {
            db = new DB();
            pdb = new PDB();

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
            xCell.setFormula(nameTamplate);

//----------------Заполнение тела документа
            for (int i = 0; i < tModel.getColumnCount(); i++) {
                xCell = xSpreadsheet.getCellByPosition(i, 1);
                xCell.setFormula(tModel.getColumnName(i));
                xPropSet = UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
                xPropSet.setPropertyValue("TableBorder", aBorder);
            }

            int nRow = 2;
            for (Object row : date) {
                for (int i = 0; i < tModel.getColumnCount(); i++) {
                    xCell = xSpreadsheet.getCellByPosition(i, nRow);
                    xCell.setFormula(((Vector) row).get(i).toString());
                    xPropSet = UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
                    xPropSet.setPropertyValue("TableBorder", aBorder);
                }
                nRow++;
            }
            xCell = xSpreadsheet.getCellByPosition(0, nRow);
            xCell.setFormula("Итого");

            char c = 64;
            c += tModel.getColumnCount();
            if (tModel.getColumnName(tModel.getColumnCount() - 1).equals("Сумма")) { // Кол-во
                xCell = xSpreadsheet.getCellByPosition(tModel.getColumnCount() - 1, nRow);
                xCell.setFormula("=sum(" + c + "1:" + c + "" + (nRow) + ")");
                xCell = xSpreadsheet.getCellByPosition(tModel.getColumnCount() - 2, nRow);
                xCell.setFormula("=sum(" + (char) (c - 1) + "1:" + (char) (c - 1) + "" + (nRow) + ")");
                xCell = xSpreadsheet.getCellByPosition(tModel.getColumnCount() - 3, nRow);
                xCell.setFormula("=sum(" + (char) (c - 2) + "1:" + (char) (c - 2) + "" + (nRow) + ")");
            } else {
                xCell = xSpreadsheet.getCellByPosition(tModel.getColumnCount() - 1, nRow);
                xCell.setFormula("=sum(" + c + "1:" + c + "" + (nRow) + ")");
                xCell = xSpreadsheet.getCellByPosition(tModel.getColumnCount() - 2, nRow);
                xCell.setFormula("=sum(" + (char) (c - 1) + "1:" + (char) (c - 1) + "" + (nRow) + ")");
                /*
                xCell = xSpreadsheet.getCellByPosition(tModel.getColumnCount()-2, nRow);
                xCell.setFormula("=sum(" + (char)(c-1) +"1:" + (char)(c-1) + "" + (nRow)+")");
                xCell = xSpreadsheet.getCellByPosition(tModel.getColumnCount()-1, nRow);
                xCell.setFormula("=SUMPRODUCT(" + (char)(c-1) +"1:" + (char)(c-1) + "" + (nRow)+ ";"+ c +"1:" + c + "" + (nRow) +")");
                */
            }
        } catch (java.lang.Exception e) {
            System.out.println(e.getMessage());
            log.error("Ошибка при заполнении отчёта принято/сдано упаковкой", e);
            JOptionPane.showMessageDialog(null, "Ошибка при заполнении отчёта принято/сдано упаковкой", "Ошибка!!!", JOptionPane.ERROR_MESSAGE);
        } finally {
            pdb.disConn();
        }
    }

    public static void fildNakladnieDescr(XComponent currentDocument) {
        Vector item = new Vector();
        String type;
        int nrow = 0;
        int totalCount = 0;
        double totalSumm = 0;
        double totalSummNDS = 0;

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
            xCell.setFormula(nameTamplate);
//----------------Заполнение тела документа

            xCell = xSpreadsheet.getCellByPosition(0, 2);
            item = (Vector) vec.get(0);
            type = item.get(2).toString() + "  " + item.get(1).toString() + " ,модель " + item.get(0).toString();
            xCell.setFormula("  " + type);
            nrow = 2;
            String t;
            int oldRow = 3;
            for (int i = 0; i < vec.size(); i++) {
                item = (Vector) vec.get(i);
                t = item.get(2).toString() + "  " + item.get(1).toString() + " ,модель " + item.get(0).toString();
                if (!type.equals(t)) {
                    ++nrow;
                    type = t;
                    xCell = xSpreadsheet.getCellByPosition(0, nrow);
                    xCell.setFormula("Итого:");

                    xCell = xSpreadsheet.getCellByPosition(3, nrow);
                    xCell.setFormula("=sum(D" + (oldRow + 1) + ":D" + nrow + ")");

                    xCell = xSpreadsheet.getCellByPosition(5, nrow);
                    xCell.setFormula("=sum(F" + (oldRow + 1) + ":F" + nrow + ")");
                    xCell = xSpreadsheet.getCellByPosition(8, nrow);
                    xCell.setFormula("=sum(I" + (oldRow + 1) + ":I" + nrow + ")");
                    for (int j = 0; j < 9; j++) {
                        xCell = xSpreadsheet.getCellByPosition(j, nrow - 1);
                        xPropSet = UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
                        xPropSet.setPropertyValue("TableBorder", aBorder);
                    }

                    nrow += 2;
                    xCell = xSpreadsheet.getCellByPosition(0, nrow);
                    xCell.setFormula("  " + type);
                    oldRow = nrow + 1;
                }

                ++nrow;
                xCell = xSpreadsheet.getCellByPosition(0, nrow);
                xCell.setFormula(item.get(3).toString());
                xCell = xSpreadsheet.getCellByPosition(1, nrow);
                xCell.setFormula(item.get(4).toString());
                xCell = xSpreadsheet.getCellByPosition(2, nrow);
                xCell.setFormula(item.get(5).toString());
                xCell = xSpreadsheet.getCellByPosition(3, nrow);
                xCell.setFormula(item.get(6).toString());
                totalCount += Double.parseDouble(item.get(6).toString());
                xCell = xSpreadsheet.getCellByPosition(4, nrow);
                xCell.setFormula(item.get(7).toString());
                xCell = xSpreadsheet.getCellByPosition(5, nrow);
                xCell.setFormula(item.get(8).toString());
                totalSumm += Double.parseDouble(item.get(8).toString());


                xCell = xSpreadsheet.getCellByPosition(6, nrow);
                xCell.setFormula(item.get(9).toString());
                xCell = xSpreadsheet.getCellByPosition(7, nrow);
                xCell.setFormula(item.get(10).toString());
                xCell = xSpreadsheet.getCellByPosition(8, nrow);
                xCell.setFormula(item.get(11).toString());
                totalSummNDS += Double.parseDouble(item.get(11).toString());
            }

            ++nrow;

            xCell = xSpreadsheet.getCellByPosition(0, nrow);
            xCell.setFormula("Итого:");

            xCell = xSpreadsheet.getCellByPosition(3, nrow);
            xCell.setFormula("=sum(D" + (oldRow + 1) + ":D" + nrow + ")");

            xCell = xSpreadsheet.getCellByPosition(5, nrow);
            xCell.setFormula("=sum(F" + (oldRow + 1) + ":F" + nrow + ")");
            xCell = xSpreadsheet.getCellByPosition(8, nrow);
            xCell.setFormula("=sum(I" + (oldRow + 1) + ":I" + nrow + ")");
            for (int j = 0; j < 9; j++) {
                xCell = xSpreadsheet.getCellByPosition(j, nrow - 1);
                xPropSet = UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
                xPropSet.setPropertyValue("TableBorder", aBorder);
            }
            nrow += 2;
            xCell = xSpreadsheet.getCellByPosition(0, nrow);
            xCell.setFormula("Итого всего:");

            xCell = xSpreadsheet.getCellByPosition(3, nrow);
            xCell.setValue(totalCount);

            xCell = xSpreadsheet.getCellByPosition(5, nrow);
            xCell.setValue(totalSumm);
            xCell = xSpreadsheet.getCellByPosition(8, nrow);
            xCell.setValue(totalSummNDS);
            for (int j = 0; j < 9; j++) {
                xCell = xSpreadsheet.getCellByPosition(j, nrow - 1);
                xPropSet = UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
                xPropSet.setPropertyValue("TableBorder", aBorder);
            }
        } catch (java.lang.Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
            log.error("Ошибка при заполнении отчёта ", e);
            JOptionPane.showMessageDialog(null, "Ошибка при заполнении отчёта принято/сдано упаковкой", "Ошибка!!!", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void fildNakopVedomost(XComponent currentDocument) {
        DB db = null;
        PDB pdb = null;

        try {
            db = new DB();
            pdb = new PDB();
            long sd = convertDateStrToLong(sDate);
            long ed = convertDateStrToLong(eDate);

            float plan = (float) dept.poshiv.DataForm.getPlan() / (float) (dept.poshiv.DataForm.getDek1() + dept.poshiv.DataForm.getDek2() + dept.poshiv.DataForm.getDek3()) / (float) 16;

            XSpreadsheetDocument xSpreadsheetDocument = UnoRuntime.queryInterface(XSpreadsheetDocument.class, currentDocument);

            XSpreadsheets xSpreadsheets = xSpreadsheetDocument.getSheets();

            Object sheet = xSpreadsheets.getByName("Лист1");
            XSpreadsheet xSpreadsheet = UnoRuntime.queryInterface(XSpreadsheet.class, sheet);
            XCell xCell = xSpreadsheet.getCellByPosition(0, 0);

//----------------Заполнение шапки документа
            xCell.setFormula(xCell.getFormula() + " " + sDate + " по " + eDate);
//----------------Заполнение тела документа
            float days = (dept.poshiv.DataForm.getDek1() + dept.poshiv.DataForm.getDek2() + dept.poshiv.DataForm.getDek3());
            float zatr = 0;
            List items = new ArrayList();
            Iterator it = null;
            Object obj = null;
            int b = 0;
            for (int i = 6; i <= 24; i++) {
                xCell = xSpreadsheet.getCellByPosition(0, i);
                obj = xCell.getValue();
                b = (int) Float.parseFloat(obj.toString());
                if (brigList.contains(b)) {
                    //нормочасы
                    items = db.trudoZatBrig(sd, ed, b);
                    it = items.iterator();
                    zatr = 0;
                    while (it.hasNext()) {
                        zatr += pdb.getTrudoZat((Integer) it.next(), (Integer) it.next(), (Integer) it.next());
                    }
                    xCell = xSpreadsheet.getCellByPosition(4, i);
                    xCell.setValue(zatr);
                    //млн руб
                    xCell = xSpreadsheet.getCellByPosition(9, i);
                    xCell.setValue(db.sumPoshivBrig(sd, ed, b));
                    //дневной план
                    xCell = xSpreadsheet.getCellByPosition(6, i);
                    xCell.setFormula("=(" + dept.poshiv.DataForm.getPlan() + "*" + "B" + (i + 1) + ")" + "/(B3*" + days + ")");
                    //фрв
                    xCell = xSpreadsheet.getCellByPosition(2, i);
                    xCell.setFormula("=(" + dept.poshiv.DataForm.getFrv() + "*" + "H" + (i + 1) + ")" + "/(B3*" + days + ")");
                }
            }
        } catch (java.lang.Exception e) {
            System.out.println(e.getMessage());
            log.error("Ошибка при заполнении отчёта план пошива в млн руб", e);
            JOptionPane.showMessageDialog(null, "Ошибка при заполнении отчёта план пошива в млн руб", "Ошибка!!!", JOptionPane.ERROR_MESSAGE);
        } finally {
            pdb.disConn();
        }
    }

    public static void fildOrdersGeneralForm(XComponent currentDocument) {
        Object[][] zakaz = null;
        try {
            zakaz = pdb.returnDataCart(idValuta, kurs, nds, false);

            XSpreadsheetDocument xSpreadsheetDocument = UnoRuntime.queryInterface(XSpreadsheetDocument.class, currentDocument);
            XSpreadsheets xSpreadsheets = xSpreadsheetDocument.getSheets();
            Object sheet = xSpreadsheets.getByName("Лист1");
            XSpreadsheet xSpreadsheet = UnoRuntime.queryInterface(XSpreadsheet.class, sheet);

            //----------------Заполнение шапки документа
            if (idOrder > 0) {
                XCell xCell = xSpreadsheet.getCellByPosition(0, 0);
                xCell.setFormula("ЗАЯВКА №" + idOrder + " на поставку продукции ОАО«8 МАРТА»");
            }

            XCell xCell = xSpreadsheet.getCellByPosition(5, 0);
            xCell.setFormula(xCell.getFormula() + " " + nameCl);

            xCell = xSpreadsheet.getCellByPosition(12, 0);
            xCell.setFormula(xCell.getFormula() + " " + dateOrder);

            //----------------Заполнение тела документа
            for (int i = 3; i < zakaz.length + 3; i++) {
                xCell = xSpreadsheet.getCellByPosition(0, i);
                xCell.setValue(i - 2);

                xCell = xSpreadsheet.getCellByPosition(1, i);
                xCell.setValue(Double.valueOf(zakaz[i - 3][1].toString()).doubleValue());

                xCell = xSpreadsheet.getCellByPosition(2, i);
                xCell.setFormula(zakaz[i - 3][2].toString());

                xCell = xSpreadsheet.getCellByPosition(3, i);
                xCell.setValue(Double.valueOf(zakaz[i - 3][3].toString()).doubleValue());

                xCell = xSpreadsheet.getCellByPosition(4, i);
                xCell.setValue(Double.valueOf(zakaz[i - 3][4].toString()).doubleValue());

                xCell = xSpreadsheet.getCellByPosition(5, i);
                xCell.setFormula(zakaz[i - 3][5].toString().trim().toLowerCase());

                xCell = xSpreadsheet.getCellByPosition(6, i);
                xCell.setValue(Double.valueOf(zakaz[i - 3][6].toString()).doubleValue());

                xCell = xSpreadsheet.getCellByPosition(7, i);
                xCell.setValue(Double.valueOf(zakaz[i - 3][7].toString()).doubleValue());

                xCell = xSpreadsheet.getCellByPosition(8, i);
                xCell.setFormula(zakaz[i - 3][8].toString().trim().toLowerCase());

                xCell = xSpreadsheet.getCellByPosition(9, i);
                xCell.setValue(Double.valueOf(zakaz[i - 3][9].toString()).doubleValue());

                xCell = xSpreadsheet.getCellByPosition(10, i);
                xCell.setValue(Double.valueOf(zakaz[i - 3][10].toString()).doubleValue());

                xCell = xSpreadsheet.getCellByPosition(11, i);
                xCell.setFormula(zakaz[i - 3][12].toString());

                xCell = xSpreadsheet.getCellByPosition(12, i);
                xCell.setValue(Double.valueOf(zakaz[i - 3][13].toString()).doubleValue());
            }

            xCell = xSpreadsheet.getCellByPosition(8, zakaz.length + 4);
            xCell.setFormula("ИТОГО: ");

            xCell = xSpreadsheet.getCellByPosition(9, zakaz.length + 4);
            xCell.setFormula("=SUM(J4:J" + (zakaz.length + 3) + ")");

            xCell = xSpreadsheet.getCellByPosition(12, zakaz.length + 4);
            xCell.setFormula("=SUM(M4:M" + (zakaz.length + 3) + ")");

        } catch (java.lang.Exception e) {
            log.error("Ошибка при заполнении отчёта 'Оформление заказа' ", e);
            JOptionPane.showMessageDialog(null, "Ошибка при заполнении отчёта", "Ошибка", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void fildOrdersSkladForm(XComponent currentDocument) {
        Object[][] zakaz = null;
        HashMap<String, String> mapChulki = new HashMap<String, String>();
        HashMap<String, String> mapChildren = new HashMap<String, String>();
        HashMap<String, String> mapPeople = new HashMap<String, String>();
        String chulki[] = {"9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "27", "29", "31"};
        String children[] = {"18", "36", "20", "40", "22", "44", "24", "48", "26", "52", "28", "56", "30", "60", "32", "64", "34", "68", "!!!!", "72", "38", "76", "!!!!!", "80", "42", "84"};
        String people_1[] = {"38", "76", "40", "80", "42", "84", "44", "88", "46", "92", "48", "96", "50", "100", "52", "104", "54", "108", "56", "112", "58", "116", "60", "120",
                "62", "124", "64", "128", "66", "132", "68", "136", "70", "140", "72", "144", "!", "148", "!!", "152"};
        String people_2[] = {"74", "78", "82", "86", "90", "94", "98", "102", "106", "110", "114", "118", "122", "126", "130", "134", "138", "142", "146", "!!!"};
        String col[] = {"3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22"};
        String sarHistory = null, narHistory = null, fasHistory = null, colorHistory = null, rostHistory = null, sortHistory = null, noteHistory = null, rzmHistory = null, falagNarHistory = null;
        int row = 6, j = 0;

        try {
            zakaz = pdb.returnDataCart(idValuta, kurs, nds, true);

            XSpreadsheetDocument xSpreadsheetDocument = UnoRuntime.queryInterface(XSpreadsheetDocument.class, currentDocument);
            XSpreadsheets xSpreadsheets = xSpreadsheetDocument.getSheets();
            Object sheet = xSpreadsheets.getByName("Лист1");
            XSpreadsheet xSpreadsheet = UnoRuntime.queryInterface(XSpreadsheet.class, sheet);

            for (int i = 0; i < chulki.length; i++) {
                mapChulki.put(chulki[i], col[i]);
            }

            j = 2;
            for (int i = 0; i < children.length; i++) {
                mapChildren.put(children[i], col[j]);
                if (i % 2 != 0)
                    j++;
            }

            j = 0;
            for (int i = 0; i < people_1.length; i++) {
                mapPeople.put(people_1[i], col[j]);
                if (i % 2 != 0)
                    j++;
            }

            for (int i = 0; i < people_2.length; i++) {
                mapPeople.put(people_2[i], col[i]);
            }

            //----------------Заполнение шапки документа
            if (idOrder > 0) {
                XCell xCell = xSpreadsheet.getCellByPosition(0, 0);
                xCell.setFormula("ЗАЯВКА №" + idOrder + " на поставку продукции ОАО«8 МАРТА»");
            }

            XCell xCell = xSpreadsheet.getCellByPosition(6, 0);
            xCell.setFormula(xCell.getFormula() + " " + nameCl.trim());

            xCell = xSpreadsheet.getCellByPosition(22, 0);
            xCell.setFormula(xCell.getFormula() + " " + dateOrder.trim());

            if (!noteOrder.isEmpty()) {
                xCell = xSpreadsheet.getCellByPosition(0, 2);
                xCell.setFormula("Примечание: " + noteOrder);
            }

            //----------------Заполнение тела документа
            for (int i = 0; i < zakaz.length; i++) {
                if (zakaz[i][1].toString().trim().equals(sarHistory) && (zakaz[i][2].toString().trim().equals(narHistory))
                        && (zakaz[i][3].toString().trim().equals(fasHistory)) && (zakaz[i][8].toString().trim().equals(colorHistory))
                        && zakaz[i][6].toString().trim().equals(rostHistory) && zakaz[i][4].toString().trim().equals(sortHistory)
                        && zakaz[i][14].toString().trim().equals(noteHistory) && zakaz[i][16].toString().trim().equals(falagNarHistory)) {
                    // чулки
                    if (Integer.parseInt(zakaz[i][1].toString().substring(1, 2)) == 3) {
                        xCell = xSpreadsheet.getCellByPosition(Integer.parseInt(mapChulki.get(zakaz[i][7].toString())), row);
                        xCell.setValue(xCell.getValue() + Integer.parseInt(zakaz[i][9].toString()));
                    } else {
                        // детское
                        if (Integer.parseInt(zakaz[i][1].toString().substring(2, 3)) == 3 || Integer.parseInt(zakaz[i][1].toString().substring(2, 3)) == 6) {
                            if (mapChildren.get(zakaz[i][7].toString()) != null) {
                                xCell = xSpreadsheet.getCellByPosition(Integer.parseInt(mapChildren.get(zakaz[i][7].toString())), row);
                                xCell.setValue(xCell.getValue() + Integer.parseInt(zakaz[i][9].toString()));
                            } else {
                                // остальное
                                if (rzmHistory == null || rzmHistory.equals(zakaz[i][7].toString().trim())) {
                                    rzmHistory = zakaz[i][7].toString().trim();

                                    xCell = xSpreadsheet.getCellByPosition(23, row);
                                    xCell.setValue(Integer.parseInt(zakaz[i][7].toString()));

                                    xCell = xSpreadsheet.getCellByPosition(24, row);
                                    xCell.setValue(xCell.getValue() + Integer.parseInt(zakaz[i][9].toString()));
                                } else {
                                    sarHistory = null;
                                    fasHistory = null;
                                    i--;
                                }
                            }
                        } else {
                            // взрослое
                            if (mapPeople.get(zakaz[i][7].toString()) != null) {
                                xCell = xSpreadsheet.getCellByPosition(Integer.parseInt(mapPeople.get(zakaz[i][7].toString())), row);
                                xCell.setValue(xCell.getValue() + Integer.parseInt(zakaz[i][9].toString()));
                            } else {
                                // остальное
                                if (rzmHistory == null || rzmHistory.equals(zakaz[i][7].toString().trim())) {
                                    rzmHistory = zakaz[i][7].toString().trim();

                                    xCell = xSpreadsheet.getCellByPosition(23, row);
                                    xCell.setValue(Integer.parseInt(zakaz[i][7].toString()));

                                    xCell = xSpreadsheet.getCellByPosition(24, row);
                                    xCell.setValue(xCell.getValue() + Integer.parseInt(zakaz[i][9].toString()));
                                } else {
                                    sarHistory = null;
                                    fasHistory = null;
                                    i--;
                                }
                            }
                        }
                    }
                } else {
                    sarHistory = zakaz[i][1].toString().trim();
                    narHistory = zakaz[i][2].toString().trim();
                    fasHistory = zakaz[i][3].toString().trim();
                    colorHistory = zakaz[i][8].toString().trim();
                    rostHistory = zakaz[i][6].toString().trim();
                    sortHistory = zakaz[i][4].toString().trim();
                    noteHistory = zakaz[i][14].toString().trim();
                    falagNarHistory = zakaz[i][16].toString().trim();
                    rzmHistory = null;
                    row++;

                    xCell = xSpreadsheet.getCellByPosition(0, row);
                    xCell.setFormula(zakaz[i][5].toString().trim().toLowerCase());

                    xCell = xSpreadsheet.getCellByPosition(1, row);
                    xCell.setFormula(zakaz[i][3].toString().trim());

                    if (Boolean.valueOf(falagNarHistory))
                        xCell.setFormula(xCell.getFormula() + ", " + zakaz[i][2].toString());

                    // примечание
                    if (!zakaz[i][14].toString().trim().isEmpty()) {
                        xCell = xSpreadsheet.getCellByPosition(2, row);
                        xCell.setFormula(xCell.getFormula() + "примечание: «" + zakaz[i][14].toString().trim() + "».\n");
                    }

                    // цвет, сорт, рост
                    xCell = xSpreadsheet.getCellByPosition(2, row);
                    xCell.setFormula(xCell.getFormula() + zakaz[i][8].toString().trim().toLowerCase() + ", " + zakaz[i][4].toString().trim() + ", " + zakaz[i][6].toString().trim());

                    i--;
                }
            }
        } catch (Exception e) {
            log.error("Ошибка при заполнении отчёта 'Оформление заказа' ", e);
            JOptionPane.showMessageDialog(null, "Ошибка при заполнении отчёта", "Ошибка", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void fildUpackPoModel(XComponent currentDocument) {

        try {

            XSpreadsheetDocument xSpreadsheetDocument = UnoRuntime.queryInterface(XSpreadsheetDocument.class, currentDocument);

            XSpreadsheets xSpreadsheets = xSpreadsheetDocument.getSheets();

            Object sheet = xSpreadsheets.getByName("Лист1");
            XSpreadsheet xSpreadsheet = UnoRuntime.queryInterface(XSpreadsheet.class, sheet);

            XCell xCell = xSpreadsheet.getCellByPosition(0, 0);

//----------------Заполнение шапки документа
            xCell.setFormula(nameTamplate);

//----------------Заполнение тела документа
            int nRow = 2;
            for (Object row : vec) {
                xCell = xSpreadsheet.getCellByPosition(0, nRow);
                xCell.setFormula(((Vector) row).get(0).toString());
                xCell = xSpreadsheet.getCellByPosition(1, nRow);
                xCell.setFormula(((Vector) row).get(1).toString());
                nRow++;
            }
        } catch (java.lang.Exception e) {
            System.out.println(e.getMessage());
            log.error("Ошибка при заполнении отчёта принято/сдано упаковкой", e);
            JOptionPane.showMessageDialog(null, "Ошибка при заполнении отчёта принято/сдано упаковкой", "Ошибка!!!", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static void fildOtgruzClient(XComponent currentDocument) {
        Vector tmpv = null;
        String izdAssortm = "";
        String izdGroup = "";
        int lineStart = 0;
        int lineEnd = 0;

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

            //----------------Заполнение шапки документа
            XCell xCell = xSpreadsheet.getCellByPosition(0, 0);
            xCell.setFormula(nameTamplate);

            //----------------Заполнение тела документа
            int row = 2;
            for (int i = 0; i < vec.size(); i++) {
                tmpv = (Vector) vec.get(i);

                if (!izdGroup.equals(tmpv.get(1).toString()) && lineStart != 0) {
                    for (int j = 0; j < 7; j++) {
                        xCell = xSpreadsheet.getCellByPosition(j, row - 1);
                        xPropSet = UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
                        xPropSet.setPropertyValue("TableBorder", aBorder);
                    }

                    xCell = xSpreadsheet.getCellByPosition(0, row);
                    xCell.setFormula("ИТОГО: ");

                    xCell = xSpreadsheet.getCellByPosition(2, row);
                    xCell.setFormula("=SUM(C" + lineStart + ":C" + lineEnd + ")");

                    xCell = xSpreadsheet.getCellByPosition(3, row);
                    xCell.setFormula("=SUM(D" + lineStart + ":D" + lineEnd + ")");

                    xCell = xSpreadsheet.getCellByPosition(4, row);
                    xCell.setFormula("=SUM(E" + lineStart + ":E" + lineEnd + ")");

                    xCell = xSpreadsheet.getCellByPosition(5, row);
                    xCell.setFormula("=SUM(F" + lineStart + ":F" + lineEnd + ")");

                    xCell = xSpreadsheet.getCellByPosition(6, row);
                    xCell.setFormula("=IF(C" + (row + 1) + "=0;\"-\";(D" + (row + 1) + ")/C" + (row + 1) + ")");

                    row++;
                    lineStart = 0;
                    lineEnd = 0;
                }

                if (!izdAssortm.equals(tmpv.get(0).toString())) {
                    row++;
                    for (int j = 0; j < 7; j++) {
                        xCell = xSpreadsheet.getCellByPosition(j, row - 1);
                        xPropSet = UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
                        xPropSet.setPropertyValue("TableBorder", aBorder);
                    }

                    xCell = xSpreadsheet.getCellByPosition(3, row);
                    xCell.setFormula(tmpv.get(0).toString());

                    row++;
                    for (int j = 0; j < 7; j++) {
                        xCell = xSpreadsheet.getCellByPosition(j, row - 1);
                        xPropSet = UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
                        xPropSet.setPropertyValue("TableBorder", aBorder);
                    }

                    i--;
                    izdAssortm = tmpv.get(0).toString();
                    izdGroup = "";
                } else {
                    if (!izdGroup.equals(tmpv.get(1).toString())) {
                        row++;
                        xCell = xSpreadsheet.getCellByPosition(0, row);
                        xCell.setFormula("   " + tmpv.get(1).toString().toUpperCase());
                        row++;

                        i--;
                        izdGroup = tmpv.get(1).toString();
                        lineStart = row + 1;
                    } else {
                        for (int j = 0; j < 7; j++) {
                            xCell = xSpreadsheet.getCellByPosition(j, row);
                            if (j == 0 || j == 1) xCell.setFormula(tmpv.get(j + 1).toString());
                            else if (j == 6)
                                xCell.setFormula("=IF(C" + (row + 1) + "=0;\"-\";(D" + (row + 1) + ")/C" + (row + 1) + ")");
                            else xCell.setValue(Double.valueOf(tmpv.get(j + 1).toString()).doubleValue());
                        }
                        row++;
                        lineEnd = row;
                    }
                }
            }
            if (lineStart != 0) {
                for (int j = 0; j < 7; j++) {
                    xCell = xSpreadsheet.getCellByPosition(j, row - 1);
                    xPropSet = UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
                    xPropSet.setPropertyValue("TableBorder", aBorder);
                }

                xCell = xSpreadsheet.getCellByPosition(0, row);
                xCell.setFormula("ИТОГО: ");

                xCell = xSpreadsheet.getCellByPosition(2, row);
                xCell.setFormula("=SUM(C" + lineStart + ":C" + lineEnd + ")");

                xCell = xSpreadsheet.getCellByPosition(3, row);
                xCell.setFormula("=SUM(D" + lineStart + ":D" + lineEnd + ")");

                xCell = xSpreadsheet.getCellByPosition(4, row);
                xCell.setFormula("=SUM(E" + lineStart + ":E" + lineEnd + ")");

                xCell = xSpreadsheet.getCellByPosition(5, row);
                xCell.setFormula("=SUM(F" + lineStart + ":F" + lineEnd + ")");

                xCell = xSpreadsheet.getCellByPosition(6, row);
                xCell.setFormula("=IF(C" + (row + 1) + "=0;\"-\";(D" + (row + 1) + ")/C" + (row + 1) + ")");

                row += 2;
                for (int j = 0; j < 7; j++) {
                    xCell = xSpreadsheet.getCellByPosition(j, row - 1);
                    xPropSet = UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
                    xPropSet.setPropertyValue("TableBorder", aBorder);
                }

                lineEnd++;
                xCell = xSpreadsheet.getCellByPosition(0, row);
                xCell.setFormula("ИТОГО ВСЕГО: ");

                xCell = xSpreadsheet.getCellByPosition(2, row);
                xCell.setFormula("=SUM(C7:C" + lineEnd + ")/2");

                xCell = xSpreadsheet.getCellByPosition(3, row);
                xCell.setFormula("=SUM(D7:D" + lineEnd + ")/2");

                xCell = xSpreadsheet.getCellByPosition(4, row);
                xCell.setFormula("=SUM(E7:E" + lineEnd + ")/2");

                xCell = xSpreadsheet.getCellByPosition(5, row);
                xCell.setFormula("=SUM(F7:F" + lineEnd + ")/2");

                xCell = xSpreadsheet.getCellByPosition(6, row);
                xCell.setFormula("=IF(C" + (row + 1) + "=0;\"-\";(D" + (row + 1) + ")/C" + (row + 1) + ")");

            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            log.error("Ошибка при заполнении отчёта отгрузка покупателю", e);
            JOptionPane.showMessageDialog(null, "Ошибка при заполнении отчёта отгрузка покупателю", "Ошибка!!!", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static void fildOtgruzClientObshchi(XComponent currentDocument) {
        Vector tmpv = null;
        String izdAssortm = "";
        String izdGroup = "";
        int lineStart = 0;
        int lineEnd = 0;

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

            //----------------Заполнение шапки документа
            XCell xCell = xSpreadsheet.getCellByPosition(0, 0);
            xCell.setFormula(nameTamplate);

            //----------------Заполнение тела документа
            int row = 2;
            for (int i = 0; i < vec.size(); i++) {
                tmpv = (Vector) vec.get(i);

                if (!izdGroup.equals(tmpv.get(1).toString()) && lineStart != 0) {
                    for (int j = 0; j < 8; j++) {
                        xCell = xSpreadsheet.getCellByPosition(j, row - 1);
                        xPropSet = UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
                        xPropSet.setPropertyValue("TableBorder", aBorder);
                    }

                    xCell = xSpreadsheet.getCellByPosition(0, row);
                    xCell.setFormula("ИТОГО: ");

                    xCell = xSpreadsheet.getCellByPosition(3, row);
                    xCell.setFormula("=SUM(D" + lineStart + ":D" + lineEnd + ")");

                    xCell = xSpreadsheet.getCellByPosition(4, row);
                    xCell.setFormula("=SUM(E" + lineStart + ":E" + lineEnd + ")");

                    xCell = xSpreadsheet.getCellByPosition(5, row);
                    xCell.setFormula("=SUM(F" + lineStart + ":F" + lineEnd + ")");

                    xCell = xSpreadsheet.getCellByPosition(6, row);
                    xCell.setFormula("=SUM(G" + lineStart + ":G" + lineEnd + ")");

                    xCell = xSpreadsheet.getCellByPosition(7, row);
                    xCell.setFormula("=IF(D" + (row + 1) + "=0;\"-\";(E" + (row + 1) + ")/D" + (row + 1) + ")");

                    row++;
                    lineStart = 0;
                    lineEnd = 0;
                }

                if (!izdAssortm.equals(tmpv.get(0).toString())) {
                    row++;
                    for (int j = 0; j < 8; j++) {
                        xCell = xSpreadsheet.getCellByPosition(j, row - 1);
                        xPropSet = UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
                        xPropSet.setPropertyValue("TableBorder", aBorder);
                    }

                    xCell = xSpreadsheet.getCellByPosition(4, row);
                    xCell.setFormula(tmpv.get(0).toString());

                    row++;
                    for (int j = 0; j < 8; j++) {
                        xCell = xSpreadsheet.getCellByPosition(j, row - 1);
                        xPropSet = UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
                        xPropSet.setPropertyValue("TableBorder", aBorder);
                    }

                    i--;
                    izdAssortm = tmpv.get(0).toString();
                    izdGroup = "";
                } else {
                    if (!izdGroup.equals(tmpv.get(1).toString())) {
                        row++;
                        xCell = xSpreadsheet.getCellByPosition(0, row);
                        xCell.setFormula("   " + tmpv.get(1).toString().toUpperCase());
                        row++;

                        i--;
                        izdGroup = tmpv.get(1).toString();
                        lineStart = row + 1;
                    } else {
                        for (int j = 0; j < 8; j++) {
                            xCell = xSpreadsheet.getCellByPosition(j, row);
                            if (j == 0 || j == 1) xCell.setFormula(tmpv.get(j + 1).toString());
                            else if (j == 2) xCell.setValue(Double.valueOf(tmpv.get(7).toString()).doubleValue());
                            else if (j == 7)
                                xCell.setFormula("=IF(D" + (row + 1) + "=0;\"-\";(E" + (row + 1) + ")/D" + (row + 1) + ")");
                            else xCell.setValue(Double.valueOf(tmpv.get(j).toString()).doubleValue());
                        }
                        row++;
                        lineEnd = row;
                    }
                }
            }
            if (lineStart != 0) {
                for (int j = 0; j < 8; j++) {
                    xCell = xSpreadsheet.getCellByPosition(j, row - 1);
                    xPropSet = UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
                    xPropSet.setPropertyValue("TableBorder", aBorder);
                }

                xCell = xSpreadsheet.getCellByPosition(0, row);
                xCell.setFormula("ИТОГО: ");

                xCell = xSpreadsheet.getCellByPosition(3, row);
                xCell.setFormula("=SUM(D" + lineStart + ":D" + lineEnd + ")");

                xCell = xSpreadsheet.getCellByPosition(4, row);
                xCell.setFormula("=SUM(E" + lineStart + ":E" + lineEnd + ")");

                xCell = xSpreadsheet.getCellByPosition(5, row);
                xCell.setFormula("=SUM(F" + lineStart + ":F" + lineEnd + ")");

                xCell = xSpreadsheet.getCellByPosition(6, row);
                xCell.setFormula("=SUM(G" + lineStart + ":G" + lineEnd + ")");

                xCell = xSpreadsheet.getCellByPosition(7, row);
                xCell.setFormula("=IF(D" + (row + 1) + "=0;\"-\";(E" + (row + 1) + ")/D" + (row + 1) + ")");

                row += 2;
                for (int j = 0; j < 8; j++) {
                    xCell = xSpreadsheet.getCellByPosition(j, row - 1);
                    xPropSet = UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
                    xPropSet.setPropertyValue("TableBorder", aBorder);
                }

                lineEnd++;
                xCell = xSpreadsheet.getCellByPosition(0, row);
                xCell.setFormula("ИТОГО ВСЕГО: ");

                xCell = xSpreadsheet.getCellByPosition(3, row);
                xCell.setFormula("=SUM(D7:D" + lineEnd + ")/2");

                xCell = xSpreadsheet.getCellByPosition(4, row);
                xCell.setFormula("=SUM(E7:E" + lineEnd + ")/2");

                xCell = xSpreadsheet.getCellByPosition(5, row);
                xCell.setFormula("=SUM(F7:F" + lineEnd + ")/2");

                xCell = xSpreadsheet.getCellByPosition(6, row);
                xCell.setFormula("=SUM(G7:G" + lineEnd + ")/2");

                xCell = xSpreadsheet.getCellByPosition(7, row);
                xCell.setFormula("=IF(D" + (row + 1) + "=0;\"-\";(E" + (row + 1) + ")/D" + (row + 1) + ")");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            log.error("Ошибка при заполнении отчёта отгрузка покупателю", e);
            JOptionPane.showMessageDialog(null, "Ошибка при заполнении отчёта отгрузка покупателю", "Ошибка!!!", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void fildSales(XComponent currentDocument) {
        Vector date = new Vector(tModel.getDataVector());
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
            xCell.setFormula(nameTamplate);

            //----------------Заполнение тела документа
            for (int i = 0; i < tModel.getColumnCount(); i++) {
                xCell = xSpreadsheet.getCellByPosition(i, 1);
                xCell.setFormula(tModel.getColumnName(i));
                xPropSet = UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
                xPropSet.setPropertyValue("TableBorder", aBorder);
            }

            int nRow = 2;
            for (Object row : date) {
                for (int i = 0; i < tModel.getColumnCount(); i++) {
                    xCell = xSpreadsheet.getCellByPosition(i, nRow);
                    xCell.setFormula(((Vector) row).get(i).toString());
                    xPropSet = UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
                    xPropSet.setPropertyValue("TableBorder", aBorder);
                }
                nRow++;
            }
        } catch (java.lang.Exception e) {
            log.error("Ошибка при заполнении отчёта.", e);
            JOptionPane.showMessageDialog(null, "Ошибка при заполнении отчёта.", "Ошибка!", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void fildProtokolSoglasovania(XComponent currentDocument) {
        float kurs = Float.parseFloat(dopVec.get(1).toString());
        float kursP = Float.parseFloat(dopVec.get(4).toString());
        float kMorja = (((kurs * 100) / kursP) - 100);
        BigDecimal km = new BigDecimal(kMorja);
        km = km.setScale(2, BigDecimal.ROUND_HALF_UP);
        kMorja = Float.parseFloat(km.toString());

        try {
            com.sun.star.beans.XPropertySet xPropSet = null;
            com.sun.star.table.BorderLine aLine = new com.sun.star.table.BorderLine();
            aLine.Color = 0x000000;
            aLine.InnerLineWidth = aLine.LineDistance = 0;
            aLine.OuterLineWidth = 1;
            com.sun.star.table.TableBorder aBorder = new com.sun.star.table.TableBorder();
            aBorder.TopLine = aBorder.BottomLine = aBorder.LeftLine = aBorder.RightLine = aLine;
            aBorder.IsTopLineValid = aBorder.IsBottomLineValid = true;
            aBorder.IsLeftLineValid = aBorder.IsRightLineValid = true;
            aBorder.IsBottomLineValid = true;

            XSpreadsheetDocument xSpreadsheetDocument = UnoRuntime.queryInterface(XSpreadsheetDocument.class, currentDocument);

            XSpreadsheets xSpreadsheets = xSpreadsheetDocument.getSheets();
//формирование листа "Общий"
            Object sheet = xSpreadsheets.getByName("Общий");
            XSpreadsheet xSpreadsheet = UnoRuntime.queryInterface(XSpreadsheet.class, sheet);

//----------------Заполнение шапки документа
            XCell xCell = xSpreadsheet.getCellByPosition(0, 0);
            xCell.setFormula(nameTamplate);

            xCell = xSpreadsheet.getCellByPosition(0, 1);
            xCell.setFormula("Поставщик ОАО «8 Марта» Покупатель: " + dopVec.get(2).toString().trim());

            xCell = xSpreadsheet.getCellByPosition(0, 2);
            xCell.setFormula(dopVec.get(0).toString());


//----------------Заполнение тела документа

            int nRow = 5;
            for (Object item : vec) {
                xCell = xSpreadsheet.getCellByPosition(0, nRow);
                xCell.setFormula(((Vector) item).get(0).toString());

                xCell = xSpreadsheet.getCellByPosition(1, nRow);
                xCell.setFormula(((Vector) item).get(1).toString());

                xCell = xSpreadsheet.getCellByPosition(2, nRow);
                xCell.setFormula(((Vector) item).get(2).toString());

                xCell = xSpreadsheet.getCellByPosition(3, nRow);
                float f = Float.parseFloat(((Vector) item).get(6).toString());
                xCell.setFormula(((Vector) item).get(5).toString());

                for (int i = 0; i < 4; i++) {
                    xCell = xSpreadsheet.getCellByPosition(i, nRow);
                    xPropSet = UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
                    xPropSet.setPropertyValue("TableBorder", aBorder);
                    xPropSet.setPropertyValue("CharHeight", (float) (8));
                }

                nRow++;
            }

            xCell = xSpreadsheet.getCellByPosition(0, ++nRow);
            xCell.setFormula("Поставщик:");

            xCell = xSpreadsheet.getCellByPosition(0, ++nRow);
            xCell.setFormula("Начальник ОВЭС");
            nRow++;

            xCell = xSpreadsheet.getCellByPosition(0, ++nRow);
            xCell.setFormula("Визы:");
            nRow++;

            xCell = xSpreadsheet.getCellByPosition(0, nRow);
            xCell.setFormula("Специалист по продажам(товаровед):");

//формирование листа "Со скидкой"
            sheet = xSpreadsheets.getByName("Со скидкой");
            xSpreadsheet = UnoRuntime.queryInterface(XSpreadsheet.class, sheet);

//----------------Заполнение шапки документа
            xCell = xSpreadsheet.getCellByPosition(0, 0);
            xCell.setFormula(nameTamplate);

            xCell = xSpreadsheet.getCellByPosition(0, 1);
            xCell.setFormula("Поставщик ОАО «8 Марта» Покупатель: " + dopVec.get(2).toString().trim());

            xCell = xSpreadsheet.getCellByPosition(0, 2);
            xCell.setFormula(dopVec.get(0).toString());

            xCell = xSpreadsheet.getCellByPosition(5, 3);
            xCell.setFormula("Скидка по договору: " + dopVec.get(3).toString() + "%");

            xCell = xSpreadsheet.getCellByPosition(5, 5);
            xCell.setFormula("Курс по прейскуранту: " + kursP);

            xCell = xSpreadsheet.getCellByPosition(5, 6);
            xCell.setFormula("Курс на момент отгрузки: " + kurs);

            xCell = xSpreadsheet.getCellByPosition(5, 7);
            xCell.setFormula("Курсовая моржа:" + kMorja + "%");


//----------------Заполнение тела документа

            nRow = 10;
            for (Object item : vec) {

                for (int i = 0; i < 7; i++) {
                    xCell = xSpreadsheet.getCellByPosition(i, nRow);
                    xCell.setFormula(((Vector) item).get(i).toString());
                }

                xCell = xSpreadsheet.getCellByPosition(4, nRow);
                float f = Float.parseFloat(((Vector) item).get(3).toString());
                f = f / kursP;
                km = new BigDecimal(f);
                xCell.setFormula(String.valueOf(km.setScale(2, RoundingMode.HALF_UP)));

                xCell = xSpreadsheet.getCellByPosition(6, nRow);
                f = Float.parseFloat(((Vector) item).get(5).toString());
                f = f * kurs;
                km = new BigDecimal(f);
                xCell.setFormula(String.valueOf(km.setScale(-1, RoundingMode.HALF_UP)));

                xCell = xSpreadsheet.getCellByPosition(7, nRow);
                f = Float.parseFloat(((Vector) item).get(5).toString()) * kurs * 100;
                f = f / Float.parseFloat(((Vector) item).get(3).toString()) - 100;
                km = new BigDecimal(f);
                xCell.setFormula(String.valueOf(km.setScale(2, RoundingMode.HALF_UP)));

                for (int i = 0; i < 8; i++) {
                    xCell = xSpreadsheet.getCellByPosition(i, nRow);
                    xPropSet = UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
                    xPropSet.setPropertyValue("TableBorder", aBorder);
                    xPropSet.setPropertyValue("CharHeight", (float) (8));
                }

                nRow++;
            }

            xCell = xSpreadsheet.getCellByPosition(0, ++nRow);
            xCell.setFormula("Продавец:                                                                            Покупатель:");

            xCell = xSpreadsheet.getCellByPosition(0, ++nRow);
            xCell.setFormula("Директор ОАО \"8 Марта\"                          В.В. Шитиков");

            xCell = xSpreadsheet.getCellByPosition(0, ++nRow);
            xCell.setFormula("Визы:");

            xCell = xSpreadsheet.getCellByPosition(0, ++nRow);
            xCell.setFormula("Зам. по экономике:                                   Л.В. Грищенко");

            xCell = xSpreadsheet.getCellByPosition(0, ++nRow);
            xCell.setFormula("Зам. по маркетингу и сбыту:                     Х.Е. Можная");

            xCell = xSpreadsheet.getCellByPosition(0, ++nRow);
            xCell.setFormula("Начальник ОВЭС:                                    Д.А. Жабин");


//формирование листа "Без скидкой"
            sheet = xSpreadsheets.getByName("Без скидки");
            xSpreadsheet = UnoRuntime.queryInterface(XSpreadsheet.class, sheet);

//----------------Заполнение шапки документа
            xCell = xSpreadsheet.getCellByPosition(0, 0);
            xCell.setFormula(nameTamplate);

            xCell = xSpreadsheet.getCellByPosition(0, 1);
            xCell.setFormula("Поставщик ОАО «8 Марта» Покупатель: " + dopVec.get(2).toString().trim());

            xCell = xSpreadsheet.getCellByPosition(0, 2);
            xCell.setFormula(dopVec.get(0).toString());

            xCell = xSpreadsheet.getCellByPosition(5, 3);
            xCell.setFormula("Курс по прейскуранту: " + kursP);

            xCell = xSpreadsheet.getCellByPosition(5, 4);
            xCell.setFormula("Курс на момент отгрузки: " + kurs);

            xCell = xSpreadsheet.getCellByPosition(5, 5);
            xCell.setFormula("Курсовая моржа:" + kMorja + "%");


//----------------Заполнение тела документа

            nRow = 8;
            for (Object item : vec) {

                for (int i = 0; i < 7; i++) {
                    xCell = xSpreadsheet.getCellByPosition(i, nRow);
                    xCell.setFormula(((Vector) item).get(i).toString());
                }

                xCell = xSpreadsheet.getCellByPosition(4, nRow);
                float f = Float.parseFloat(((Vector) item).get(3).toString());
                f = f / kursP;
                km = new BigDecimal(f);
                xCell.setFormula(String.valueOf(km.setScale(2, RoundingMode.HALF_UP)));

                xCell = xSpreadsheet.getCellByPosition(6, nRow);
                f = Float.parseFloat(((Vector) item).get(5).toString());
                f = f * kurs;
                km = new BigDecimal(f);
                xCell.setFormula(String.valueOf(km.setScale(-1, RoundingMode.HALF_UP)));

                xCell = xSpreadsheet.getCellByPosition(7, nRow);
                f = Float.parseFloat(((Vector) item).get(5).toString()) * kurs * 100;
                f = f / Float.parseFloat(((Vector) item).get(3).toString()) - 100;
                km = new BigDecimal(f);
                xCell.setFormula(String.valueOf(km.setScale(2, RoundingMode.HALF_UP)));

                for (int i = 0; i < 8; i++) {
                    xCell = xSpreadsheet.getCellByPosition(i, nRow);
                    xPropSet = UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
                    xPropSet.setPropertyValue("TableBorder", aBorder);
                    xPropSet.setPropertyValue("CharHeight", (float) (8));
                }

                nRow++;
            }

            xCell = xSpreadsheet.getCellByPosition(0, ++nRow);
            xCell.setFormula("Поставщик:");
            ++nRow;

            xCell = xSpreadsheet.getCellByPosition(0, ++nRow);
            xCell.setFormula("Начальник ОВЭС:                                       Д.А. Жабин");
            nRow++;

            xCell = xSpreadsheet.getCellByPosition(0, ++nRow);
            xCell.setFormula("Визы:");
            nRow++;

            xCell = xSpreadsheet.getCellByPosition(0, nRow);
            xCell.setFormula("Специалист по продажам:");

        } catch (java.lang.Exception e) {
            System.out.println(e.getMessage());
            log.error("Ошибка при заполнении протокола согласования цен", e);
            JOptionPane.showMessageDialog(null, "Ошибка при заполнении протокола согласования цен", "Ошибка!!!", JOptionPane.ERROR_MESSAGE);
        } finally {
        }
    }

    public static void fildPereuchZH(XComponent currentDocument) {
        try {
            com.sun.star.beans.XPropertySet xPropSet = null;
            com.sun.star.table.BorderLine aLine = new com.sun.star.table.BorderLine();
            aLine.Color = 0x000000;
            aLine.InnerLineWidth = aLine.LineDistance = 0;
            aLine.OuterLineWidth = 1;
            com.sun.star.table.TableBorder aBorder = new com.sun.star.table.TableBorder();
            aBorder.TopLine = aBorder.BottomLine = aBorder.LeftLine = aBorder.RightLine = aLine;
            aBorder.IsTopLineValid = aBorder.IsBottomLineValid = true;
            aBorder.IsLeftLineValid = aBorder.IsRightLineValid = true;
            aBorder.IsBottomLineValid = true;

            XSpreadsheetDocument xSpreadsheetDocument = UnoRuntime.queryInterface(XSpreadsheetDocument.class, currentDocument);

            XSpreadsheets xSpreadsheets = xSpreadsheetDocument.getSheets();
//формирование листа "Общий"
            Object sheet = xSpreadsheets.getByName("Лист1");
            XSpreadsheet xSpreadsheet = UnoRuntime.queryInterface(XSpreadsheet.class, sheet);

//----------------Заполнение шапки документа
            XCell xCell = xSpreadsheet.getCellByPosition(0, 1);
            xCell.setFormula(nameTamplate);

//----------------Заполнение тела документа

            int nRow = 4;
            for (Object item : vec) {
                for (int i = 0; i < 6; i++) {
                    xCell = xSpreadsheet.getCellByPosition(i, nRow);
                    xCell.setFormula(((Vector) item).get(i).toString());
                    xCell = xSpreadsheet.getCellByPosition(i, nRow);
                    xPropSet = UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
                    xPropSet.setPropertyValue("TableBorder", aBorder);
                }
                nRow++;
            }

            xCell = xSpreadsheet.getCellByPosition(0, nRow);
            xCell.setFormula("ИТОГО:");
            xCell = xSpreadsheet.getCellByPosition(5, nRow);
            xCell.setFormula("=SUM(F5:F" + nRow + ")");

            nRow++;
            xCell = xSpreadsheet.getCellByPosition(0, ++nRow);
            xCell.setFormula("Ответсвенный за переучёт пом. мастера ___________:");

            xCell = xSpreadsheet.getCellByPosition(4, nRow);
            xCell.setFormula("ФИО: ___________________");
            nRow++;

        } catch (java.lang.Exception e) {
            System.out.println(e.getMessage());
            log.error("Ошибка при заполнении переучёта н/с продукции ЗШ цеха", e);
            JOptionPane.showMessageDialog(null, "Ошибка при заполнении переучёта н/с продукции ЗШ цеха", "Ошибка!!!", JOptionPane.ERROR_MESSAGE);
        } finally {
        }
    }

    public static void fildPeremNoMassa(XComponent currentDocument) {
        DB db = null;
        PDB pdb = null;
        Vector date = new Vector(tModel.getDataVector());

        try {
            db = new DB();
            pdb = new PDB();

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
            xCell.setFormula(nameTamplate);

//----------------Заполнение тела документа
            int nRow = 2;
            for (Object row : date) {
                for (int i = 0; i < 5; i++) {
                    xCell = xSpreadsheet.getCellByPosition(i, nRow);
                    xCell.setFormula(((Vector) row).get(i).toString());
                    xPropSet = UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
                    xPropSet.setPropertyValue("TableBorder", aBorder);
                }
                nRow++;
            }
        } catch (java.lang.Exception e) {
            System.out.println(e.getMessage());
            log.error("Ошибка при заполнении отчёта принято/сдано упаковкой", e);
            JOptionPane.showMessageDialog(null, "Ошибка при заполнении отчёта принято/сдано упаковкой", "Ошибка!!!", JOptionPane.ERROR_MESSAGE);
        } finally {
            pdb.disConn();
        }
    }

    public static void fildInventOpis() {
        try {
            connect();
            XComponent currentDocument = openDocument("m:\\MyReports20\\Templates\\inventOpis.ots");
            XSpreadsheetDocument xSpreadsheetDocument = UnoRuntime.queryInterface(XSpreadsheetDocument.class, currentDocument);

            XSpreadsheets xSpreadsheets = xSpreadsheetDocument.getSheets();

            Object sheet = xSpreadsheets.getByName("Лист1");
            XSpreadsheet xSpreadsheet = UnoRuntime.queryInterface(XSpreadsheet.class, sheet);
            XCell xCell = xSpreadsheet.getCellByPosition(0, 0);
//----------------Заполнение шапки документа
            xCell.setFormula(nameTamplate);

        } catch (java.lang.Exception e) {
            System.out.println(e.getMessage());
            log.error("Ошибка при заполнении ", e);
            JOptionPane.showMessageDialog(null, "Ошибка при заполнении", "Ошибка!!!", JOptionPane.ERROR_MESSAGE);
        } finally {

        }

    }

    public void createReport(String nameTamplates) {
        try {
            connect();
            XComponent currentDocument = openDocument("Templates/" + nameTamplates);

            if (nameTamplates.equals("InfProdZakrSh.ots")) {
                fildInfProdZakrSh(currentDocument);
            } else if (nameTamplates.equals("EanPlan.ots")) {
                fildEanPlan(currentDocument);
            } else if (nameTamplates.equals("ТрудозатПошив.ots")) {
                fildTrudozatPoshiv(currentDocument);
            } else if (nameTamplates.equals("планПошивНЧ.ots")) {
                fildPlanPoshivN4(currentDocument);
            } else if (nameTamplates.equals("планПошивМР.ots")) {
                fildPlanPoshivMR(currentDocument);
            } else if (nameTamplates.equals("пошивПодекадныйВыпуск.ots")) {
                fildNakopVedomost(currentDocument);
            } else if (nameTamplates.equals("ЗаказыОбщаяФорма.ots")) {
                fildOrdersGeneralForm(currentDocument);
            } else if (nameTamplates.equals("ЗаявкаСклад.ots")) {
                fildOrdersSkladForm(currentDocument);
            } else if (nameTamplates.equals("ПринятоСданоУпаковка.ots")) {
                fildPrinZdanoUpack(currentDocument);
            } else if (nameTamplates.equals("Остатки.ots")) {
                fildOstatki(currentDocument);
            } else if (nameTamplates.equals("NakladnieDescr.ots")) {
                fildNakladnieDescr(currentDocument);
            } else if (nameTamplates.equals("UpackPoModel.ots")) {
                fildUpackPoModel(currentDocument);
            } else if (nameTamplates.equals("OtgruzClient.ots")) {
                fildOtgruzClient(currentDocument);
            } else if (nameTamplates.equals("OtgruzClientObshchi.ots")) {
                fildOtgruzClientObshchi(currentDocument);
            } else if (nameTamplates.equals("Sales.ots")) {
                fildSales(currentDocument);
            } else if (nameTamplates.equals("протоколСогласования.ots")) {
                fildProtokolSoglasovania(currentDocument);
            } else if (nameTamplates.equals("ПереучётЗШ.ots")) {
                fildPereuchZH(currentDocument);
            } else if (nameTamplates.equals("ПеремещениеБезМассы.ots")) {
                fildPeremNoMassa(currentDocument);
            }
            JOptionPane.showMessageDialog(null, "Отчёт сформирован", "Отчёт сформирован", JOptionPane.INFORMATION_MESSAGE);

        } catch (java.lang.Exception e) {
            System.out.println(e.getMessage());
            JOptionPane.showMessageDialog(null, "Ошибка при попытке создания отчёта: " + e.getMessage(), "Ошибка!!!", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

}
