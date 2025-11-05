package by.march8.ecs.application.modules.warehouse.external.shipping.reports;

import by.gomel.freedev.ucframework.uccore.report.api.openoffice.DocumentType;
import by.gomel.freedev.ucframework.uccore.report.model.ReportProperties;
import by.march8.ecs.application.modules.warehouse.external.shipping.model.SaleDocumentReport;
import com.sun.star.beans.XPropertySet;
import com.sun.star.container.XEnumeration;
import com.sun.star.container.XEnumerationAccess;
import com.sun.star.lang.XComponent;
import com.sun.star.lang.XServiceInfo;
import com.sun.star.text.XTextFieldsSupplier;
import com.sun.star.uno.UnoRuntime;
import com.sun.star.util.XRefreshable;

/**
 * @author Andy 19.05.2016.
 */
public class InvoiceReport extends AbstractInvoiceReport {
    private String template;
    private SaleDocumentReport document;

    public InvoiceReport(SaleDocumentReport report, String templateName) {
        super(report);
        template = templateName;
        document = report;
        create();
    }

    @Override
    ReportProperties prepareProperties(final ReportProperties properties) {
        properties.setBlankName("invoice_" + document.getDocument().getDocumentNumber());
        properties.getTemplate().setTemplateName(template);
        properties.getTemplate().setDocumentType(DocumentType.DOCUMENT_ODT);
        return properties;
    }

    @Override
    boolean populateData(final XComponent component) {

        com.sun.star.beans.PropertyValue[] lParams = new com.sun.star.beans.PropertyValue[0];
        try {
            XTextFieldsSupplier xTextFieldsSupplier = (XTextFieldsSupplier) UnoRuntime
                    .queryInterface(XTextFieldsSupplier.class, component);

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
                    Object content = document.getDetailMap().get(name);

                    //System.out.println(name+" / "+content);
                    if (content != null) {
                        xPropertySet.setPropertyValue("SubType", com.sun.star.text.SetVariableType.STRING);

                        if (content.equals("")) {
                            xPropertySet.setPropertyValue("Content", "  ");
                        } else {
                            xPropertySet.setPropertyValue("Content",
                                    content.toString());
                        }
                        xPropertySet.setPropertyValue("IsVisible", true);
                    }
                }
            }
            xRefreshable.refresh();
            //saveAsDocument(currentDocument, Settings.HOME_DIR + dataMap.get("ttn").toString()
            //        + ".odt", lParams);
            return true;
        } catch (Exception e) {
            e.printStackTrace();

            //System.out.println("Ошибка печати документа " + e);
            return false;
        }
    }
}
