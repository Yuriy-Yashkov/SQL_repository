package by.gomel.freedev.ucframework.uccore.report.api.openoffice;


import by.gomel.freedev.ucframework.uccore.report.OfficeBootStrap;
import by.gomel.freedev.ucframework.uccore.report.connector.BootstrapSocketConnector;
import by.gomel.freedev.ucframework.uccore.report.model.ReportTemplate;
import by.march8.ecs.MainController;
import by.march8.ecs.framework.common.Configuration;
import by.march8.ecs.framework.common.Settings;
import by.march8.ecs.framework.common.printers.PrinterSettings;
import com.sun.star.beans.PropertyValue;
import com.sun.star.beans.XPropertySet;
import com.sun.star.container.XNameAccess;
import com.sun.star.container.XNamed;
import com.sun.star.frame.XComponentLoader;
import com.sun.star.frame.XDesktop;
import com.sun.star.frame.XStorable;
import com.sun.star.lang.IndexOutOfBoundsException;
import com.sun.star.lang.XComponent;
import com.sun.star.lang.XMultiComponentFactory;
import com.sun.star.sheet.XNamedRanges;
import com.sun.star.sheet.XSpreadsheet;
import com.sun.star.sheet.XSpreadsheetDocument;
import com.sun.star.sheet.XSpreadsheets;
import com.sun.star.table.XCell;
import com.sun.star.table.XCellRange;
import com.sun.star.uno.UnoRuntime;
import com.sun.star.uno.XComponentContext;
import com.sun.star.uno.XInterface;
import com.sun.star.util.CellProtection;
import com.sun.star.util.XCloseable;
import com.sun.star.util.XProtectable;
import com.sun.star.util.XURLTransformer;
import com.sun.star.view.XPrintable;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

@SuppressWarnings("all")
public class OpenOfficeConnector {

    private static String OPEN_OFFICE_TEMPLATE;
    private final Configuration configuration;


    private XComponentContext xRemoteContext = null;
    private XMultiComponentFactory xRemoteServiceManager = null;
    private XURLTransformer xTransformer = null;
    private XComponentLoader xComponentLoader = null;
    private XDesktop xDesktop = null;
    private XComponent component;
    private XSpreadsheets sheetList = null;
    private String openOfficeRunPath;
    private boolean newBootstrap = false;


    /**
     * Метод создает подключение к OpenOffice Server c открытием шаблона
     */
    public OpenOfficeConnector(ReportTemplate template) throws UnsupportedEncodingException {
        configuration = new Configuration(Settings.CONFIG_FILE_NAME);

        //OPEN_OFFICE_TEMPLATE =  Template.class.getProtectionDomain().getCodeSource().getLocation()+"";

        String path1 = OpenOfficeConnector.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        String decodedPath = URLDecoder.decode(path1, "UTF-8");
        // OPEN_OFFICE_TEMPLATE = configuration.getProperty(Configuration.RUN_PATH) + Settings.TEMPLATE_OPENOFFICE;

        // ПРоверка на наличие новой версии загрузчика soffice
        String signBootstrap = configuration.getProperty(Settings.PROPERTY_OFFICE_BOOTSTRAP);
        if (signBootstrap.equals("1")) {
            newBootstrap = true;
        }

        //OPEN_OFFICE_TEMPLATE = "D:\\GeneralProject\\templates\\openoffice";

        OPEN_OFFICE_TEMPLATE = Settings.TEMPLATE_DIR;
        if (createConnectToServer()) {
            component = openTemplate(template);

            // Для Write документов пропускаем данную обработку
            if (template.getDocumentType() != DocumentType.DOCUMENT_ODT) {
                XSpreadsheetDocument xSpreadsheetDocument = (XSpreadsheetDocument) UnoRuntime
                        .queryInterface(XSpreadsheetDocument.class, component);
                sheetList = xSpreadsheetDocument.getSheets();
            }
        }
    }

    /**
     * Подключаемся к серверу OpenOffice
     */
    private boolean createConnectToServer() {
        OfficeBootStrap office = OfficeBootStrap.getInstance();

        openOfficeRunPath = office.getBootPath();

        if (openOfficeRunPath.trim().equals("")) {
            return false;
        }
        // билдер инициализация сервера OpenOffice согласно спецификации JUNOI
        try {


            XComponentContext xContext = BootstrapSocketConnector.bootstrap(openOfficeRunPath, newBootstrap);
            xRemoteServiceManager = xContext.getServiceManager();

            Object transformer = xRemoteServiceManager.createInstanceWithContext("com.sun.star.util.URLTransformer", xContext);
            xTransformer = (XURLTransformer) UnoRuntime.queryInterface(XURLTransformer.class, transformer);

            XInterface desktop = (XInterface) xRemoteServiceManager.createInstanceWithContext(
                    "com.sun.star.frame.Desktop", xContext);
            xDesktop = (XDesktop) UnoRuntime.queryInterface(XDesktop.class, desktop);

            xComponentLoader = (XComponentLoader) UnoRuntime.queryInterface(
                    XComponentLoader.class, desktop);

        } catch (Exception e) {
            MainController.exception(e, "Ошибка подключения к серверу OpenOffice");
            return false;
        }
        return true;
    }

    private XComponent openTemplate(ReportTemplate reportTemplate) {
        try {
            PropertyValue[] property = new PropertyValue[1];
            PropertyValue prop = new PropertyValue();
            property[0] = prop;

            prop.Name = "Hidden";
            if (reportTemplate.isVisible()) {
                prop.Value = Boolean.FALSE;
            } else {
                prop.Value = Boolean.TRUE;
            }

            //String path = "file:///" + OPEN_OFFICE_TEMPLATE.replace('\\', '/') + reportTemplate.getTemplateName();
            //String path =  "file:///" +OPEN_OFFICE_TEMPLATE.replace('\\', '/') +"/"+ reportTemplate.getTemplateName();
            File fC = new File("c:\\windows\\explorer.exe");
            String path = "";

            if (reportTemplate.isDirectPath()) {
                path = "file:///" + reportTemplate.getTemplateName().replace('\\', '/');
            } else {
                path = "file:///" + MainController.getRunPath().replace('\\', '/') + "/" + "Templates/" + reportTemplate.getTemplateName();
            }

            System.out.println("Используется шаблон для документа : " + path);
            return xComponentLoader.loadComponentFromURL(path, "_blank", 0, property);

        } catch (Exception e) {
            MainController.exception(e, "Ошибка открытия шаблона OpenOffice " + reportTemplate.getTemplateName());
        }
        return null;
    }

    /**
     * Метод выводит на текущий принтер активный документ
     *
     * @param settings параметры печати
     */
    public void printDocument(PrinterSettings settings) {
        XPrintable xPrintable = (XPrintable) UnoRuntime.queryInterface(XPrintable.class, component);
        try {
            if (settings != null) {
                if (settings.getPrinterProperties() != null) {
                    xPrintable.setPrinter(settings.getPrinterProperties());
                }
                if (settings.getDocumentProperties() != null) {
                    xPrintable.print(settings.getDocumentProperties());
                } else {
                    xPrintable.print(new PropertyValue[0]);
                }
            } else {
                xPrintable.print(new PropertyValue[0]);
            }
        } catch (Exception e) {
            MainController.exception(e, "Ошибка печати документа");
        }
    }

    /**
     * Метод сохраняет документ под имененем из свойства <code>SaveName</code> шаблона в каталог
     * из свойства <code>SavePath</code> шаблона
     *
     * @param template объект шаблона
     * @param fileType в какой тип сохранять документ
     * @return путь к сохраненному файлу
     */
    public String saveDocument(ReportTemplate template, DocumentType fileType) {
        XStorable storable = (XStorable) UnoRuntime.queryInterface(XStorable.class, component);
        String path = template.getSavePath() + "/" + template.getSaveName();
        StringBuffer sTmp = new StringBuffer("file:///");
        String aURL = sTmp.toString() + path;
        aURL = aURL.replace('\\', '/');
        try {
            storable.storeToURL(aURL + "." + fileType.getExtension(), fileType.getProperties());
        } catch (Exception e) {
            MainController.exception(e, "Закройте это окно и работайте  " + aURL);
        }
        return path;
    }

    /**
     * Метод закрывает документ
     */
    public void closeDocument() {
        XCloseable c = (XCloseable) UnoRuntime.queryInterface(XCloseable.class, component);
        try {
            c.close(false);
            Thread.sleep(1000);

            //xDesktop.terminate();
        } catch (Exception e) {
            component.dispose();
            MainController.exception(e, "Ошибка закрытия документа OpenOffice ");
        }

       /* if (xDesktop != null) {
            boolean succed = xDesktop.terminate();
        }
        */
    }


    /**
     * Возвращает ссылку на компонентную часть Документа OO
     */
    public XComponent getXComponent() {
        return component;
    }

    /**
     * Возвращает лист по его имени
     */
    public XSpreadsheet getSheetByName(String name) {
        Object objSheet = null;
        XSpreadsheet sheet = null;
        try {
            objSheet = sheetList.getByName(name);
            sheet = (XSpreadsheet) UnoRuntime
                    .queryInterface(XSpreadsheet.class, objSheet);
        } catch (Exception e) {
            MainController.exception(e, "Ошибка получения листа документа по имени " + name);
        }
        return sheet;
    }

    /**
     * Возвращает массив листов в документе
     */
    public XSpreadsheets getSheetList() {
        return sheetList;
    }

    /**
     * Устанавливает имя для листа
     */
    public void setSheetName(XSpreadsheet xSheet, String sheetName) {
        try {
            XNamed xNamed = (XNamed) UnoRuntime.queryInterface(XNamed.class,
                    xSheet);
            xNamed.setName(sheetName);
        } catch (Exception e) {
            MainController.exception(e, "Ошибка получения коллекции листов документа");
        }
    }

    /**
     * Открывает документ в LibreOffice
     */
    public void openDocument(final String fileName) {
        if (fileName == null) {
            return;
        }
        try {
            Process process;
            System.out.println("Запуск : " + openOfficeRunPath);
            String runnerString = "";
            if (newBootstrap) {
                runnerString = "--accept=socket,host=localhost,port=8100;urp;";
            } else {
                runnerString = "-nologo -headless -accept=socket,host=127.0.0.1,port=8100;urp; -nofirststartwizard";
            }
            process = new ProcessBuilder(openOfficeRunPath, fileName, runnerString).start();
            // process.destroy();
        } catch (IOException e) {
            e.printStackTrace();
        }

/*        process.

        InputStream is = process.getInputStream();
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader br = new BufferedReader(isr);
        String line;

        try {
            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            MainController.exception(e, "Ошибка открытия документа OpenOffice " + fileName);
        }*/
    }

    /**
     * Устанавливает / Снимает блокировку с ячейки
     */
    public void setCellProtection(XCell cell, boolean protect) {
        XPropertySet cellProperty = (XPropertySet)
                UnoRuntime.queryInterface(XPropertySet.class, cell);
        CellProtection cellProtection = new CellProtection();
        cellProtection.IsLocked = protect;
        try {
            cellProperty.setPropertyValue("CellProtection", cellProtection);
        } catch (Exception e) {
            MainController.exception(e, "Ошибка установки защиты для ячейки");
        }
    }

    /**
     * Устанавливает / Снимает блокировку с диапазона ячеек
     */
    public void setCellRangeProtection(XCellRange cellRange, boolean protect) {
        XPropertySet cellRangeProperty = (XPropertySet)
                UnoRuntime.queryInterface(XPropertySet.class, cellRange);
        CellProtection cellProtection = new CellProtection();
        cellProtection.IsLocked = protect;
        try {
            cellRangeProperty.setPropertyValue("CellProtection", cellProtection);
        } catch (Exception e) {
            MainController.exception(e, "Ошибка установки защиты для диапазона ячеек");
        }
    }

    /**
     * Устанавливает защиту листа паролем
     */
    public void protectSheet(XSpreadsheet sheet, String password) {
        XProtectable sheetProtectable = (XProtectable)
                UnoRuntime.queryInterface(XProtectable.class, sheet);
        sheetProtectable.protect(password);
    }

    /**
     * Снимает защиту с листа
     */
    public void unprotectSheet(XSpreadsheet sheet, String password) {
        XProtectable sheetProtectable = (XProtectable)
                UnoRuntime.queryInterface(XProtectable.class, sheet);
        try {
            sheetProtectable.unprotect(password);
        } catch (com.sun.star.lang.IllegalArgumentException e) {
            MainController.exception(e, "Ошибка снятия защиты с листа");
        }
    }

    /**
     * Возвращает ячейку по зарегистрированному имени в документе,
     * если имя принадлежит интервалу - вернет ячейку [0,0] этого интервала
     */
    public XCell getCellByName(final XSpreadsheet sheet, final String name) {
        XCellRange range = sheet.getCellRangeByName(name);
        try {
            return range.getCellByPosition(0, 0);
        } catch (IndexOutOfBoundsException e) {
            MainController.exception(e, "Ошибка получения доступа к ячейке " + name);
        }
        return null;
    }

    /**
     * Возвращает массив зарегистрированных имен в документе
     */
    public String[] getElementNames() {
        String[] result = null;
        try {
            XPropertySet xPropertySet = UnoRuntime.queryInterface(
                    XPropertySet.class, component);
            XNamedRanges xNamedRanges = UnoRuntime.queryInterface(XNamedRanges.class, xPropertySet.getPropertyValue("NamedRanges"));

            XNameAccess xNameAccess = UnoRuntime.queryInterface(
                    XNameAccess.class, xNamedRanges);
            result = xNameAccess.getElementNames();
        } catch (Exception e) {
            MainController.exception(e, "Ошибка получения именованных эллементов документа");
        }


        return result;
    }
}

