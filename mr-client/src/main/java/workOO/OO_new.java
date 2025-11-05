package workOO;

import by.gomel.freedev.ucframework.uccore.report.OfficeBootStrap;
import by.gomel.freedev.ucframework.uccore.report.connector.BootstrapSocketConnector;
import com.sun.star.beans.PropertyValue;
import com.sun.star.frame.XComponentLoader;
import com.sun.star.frame.XDesktop;
import com.sun.star.frame.XStorable;
import com.sun.star.lang.XComponent;
import com.sun.star.lang.XMultiComponentFactory;
import com.sun.star.uno.UnoRuntime;
import com.sun.star.uno.XComponentContext;
import com.sun.star.util.XCloseable;
import com.sun.star.util.XURLTransformer;
import dept.MyReportsModule;

import javax.swing.*;


/**
 * @author vova
 */
public class OO_new {
    private static XComponentContext xRemoteContext = null;
    private static XMultiComponentFactory xRemoteServiceManager = null;
    private static XURLTransformer xTransformer = null;
    private static XComponentLoader xComponentLoader = null;
    private static XDesktop xDesktop = null;

    /* соедиенение с экземпляром OpenOffice.org
     * и инициализация нужных переменных
     */
    public static void connect() throws Exception {
        try {
            String oooExeFolder = "";
/*            java.io.File fC = new java.io.File("c:\\windows\\explorer.exe");
            if(!fC.exists()){
                oooExeFolder = "/usr/bin/soffice";
            }else {
                fC = new java.io.File("c:\\Program Files\\LibreOffice 3.5\\program\\soffice.exe");
                if(fC.exists())
                    oooExeFolder = "c://Program Files//LibreOffice 3.5//program//";
                else{
                    fC = new java.io.File("c:\\Program Files\\OpenOffice.org 3\\program\\soffice.exe");
                    if(fC.exists()){
                        oooExeFolder = "c://Program Files//OpenOffice.org 3//program//";
                        }else{
                        	//c:\Program Files (x86)\LibreOffice 3.6\
                        	fC = new java.io.File("c:\\Program Files (x86)\\LibreOffice 3.6\\program\\soffice.exe");
                        	if (fC.exists()){
                        		oooExeFolder = "c://Program Files (x86)//LibreOffice 3.6//program//";
                        	}
                        }
                    
                }
            }*/

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
            xDesktop = UnoRuntime.queryInterface(XDesktop.class, desktop);

            // данный интерфейс позволяет загружать и сохранять документы
            xComponentLoader = UnoRuntime.queryInterface(
                    XComponentLoader.class, desktop);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Ошибка соединения с OpenOffice.", "Ошибка!!!", JOptionPane.ERROR_MESSAGE);
        }
    }

    /* Открыть документ, на который указывает sURL
     * для простоты предполагаем, что sURL - путь к файлу
     */

    /**
     * @deprecated
     */
    public static XComponent openDocumentOld(String sURL) throws Exception {

        try {

            StringBuffer sTmp = new StringBuffer("file:///");
            //sTmp.append(MainForm.progPath);
            sTmp.append(MyReportsModule.progPath);
            sTmp.append("/");

            sURL = sTmp.toString().replace('\\', '/') + sURL;
            PropertyValue[] loadProps = new PropertyValue[0];

            return xComponentLoader.loadComponentFromURL(sURL, "_blank", 0, loadProps);
        } catch (Exception e) {
            System.err.println(e);
            JOptionPane.showMessageDialog(null, "Ошибка открытия шаблона " + sURL, "Ошибка!!!", JOptionPane.ERROR_MESSAGE);
        }
        return null;
    }

    public static XComponent openDocument(String sURL) throws Exception {

        try {

            StringBuffer sTmp = new StringBuffer("file:///");
            sURL = sTmp.toString().replace('\\', '/') + sURL;
            PropertyValue[] loadProps = new PropertyValue[0];
            return xComponentLoader.loadComponentFromURL(sURL, "_blank", 0, loadProps);
        } catch (Exception e) {
            System.out.println(e + sURL);
            JOptionPane.showMessageDialog(null, "Ошибка открытия шаблона " + sURL, "Ошибка!!!", JOptionPane.ERROR_MESSAGE);
        }
        return null;
    }

    public static XComponent openDocumentSMPL(String sURL) throws Exception {

        try {

            StringBuffer sTmp = new StringBuffer("file:///");
            sURL = sTmp.toString().replace('\\', '/') + sURL;
            PropertyValue[] loadProps = new PropertyValue[0];
            return xComponentLoader.loadComponentFromURL(sURL, "_blank", 0, loadProps);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(sURL);
            JOptionPane.showMessageDialog(null, "Ошибка открытия шаблона " + sURL, "Ошибка!!!", JOptionPane.ERROR_MESSAGE);
        }
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
            Thread.sleep(200);
        } catch (Exception e) {
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

    public static boolean closeDocument(String path, boolean askIfVetoed) {
        XComponent comp = null;
        try {

            StringBuffer sTmp = new StringBuffer("file:///");
            path = sTmp.toString().replace('\\', '/') + path;
            PropertyValue[] loadProps = new PropertyValue[0];
            comp = xComponentLoader.loadComponentFromURL(path, "_blank", 0, loadProps);
        } catch (Exception e) {
            System.out.println(e + path);
            JOptionPane.showMessageDialog(null, "Ошибка открытия шаблона " + path, "Ошибка!!!", JOptionPane.ERROR_MESSAGE);
        }
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

    /* Сохранить документ под текущим именем
     */
    public static void saveDocument(XComponent comp, PropertyValue[] props) {
        XStorable store = UnoRuntime.queryInterface(XStorable.class, comp);
        saveAsDocument(comp, store.getLocation(), props);
    }

    /* Сохранить документ под именем, на которое указывает aURL
     */
    public static void saveAsDocument(XComponent comp, String aURL, PropertyValue[] props) {
        XStorable store = UnoRuntime.queryInterface(XStorable.class, comp);
        StringBuffer sTmp = new StringBuffer("file:///");
        aURL = sTmp.toString().replace('\\', '/') + aURL.replace('\\', '/');
        try {
            store.storeToURL(aURL, props);
        } catch (Exception e) {
            System.err.println("Не могу сохранить файл!" + e);
        }
    }
}
