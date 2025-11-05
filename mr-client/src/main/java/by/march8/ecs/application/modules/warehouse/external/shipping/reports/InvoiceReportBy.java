package by.march8.ecs.application.modules.warehouse.external.shipping.reports;

import by.gomel.freedev.ucframework.uccore.report.api.openoffice.DocumentType;
import by.gomel.freedev.ucframework.uccore.report.model.ReportProperties;
import by.march8.ecs.application.modules.warehouse.external.shipping.manager.SaleDocumentCalculator;
import by.march8.ecs.application.modules.warehouse.external.shipping.model.SaleDocumentReport;
import by.march8.ecs.framework.helpers.digits.CurrencyType;
import by.march8.ecs.framework.helpers.digits.DigitToWords;
import by.march8.entities.warehouse.SaleDocumentBase;
import com.sun.star.beans.XPropertySet;
import com.sun.star.container.XEnumeration;
import com.sun.star.container.XEnumerationAccess;
import com.sun.star.lang.XComponent;
import com.sun.star.lang.XServiceInfo;
import com.sun.star.text.XTextFieldsSupplier;
import com.sun.star.uno.UnoRuntime;
import com.sun.star.util.XRefreshable;

import java.util.HashMap;

/**
 * @author Andy 19.05.2016.
 */
public class InvoiceReportBy extends AbstractInvoiceReport {
    private SaleDocumentReport document;

    public InvoiceReportBy(SaleDocumentReport report) {
        super(report);
        calculator(report);
        document = report;
        create();
    }

    private void calculator(SaleDocumentReport report_) {

        SaleDocumentBase document = report_.getDocument();
        String invoiceNumber = document.getDocumentNumber().substring(2);
        //Ставка НДС
        double valueVat = document.getDocumentVatValue();
        // Опорное значение - 80% от ВСЕГО С НДС
        double valueSumCostAndVat = SaleDocumentCalculator.roundBigDecimal(document.getValueSumCostAndVat() * 80 / 100, 2).doubleValue();
        // Сумма НДС обратным счетом
        double valueSumVat = SaleDocumentCalculator.roundBigDecimal(valueSumCostAndVat * valueVat / (100 + valueVat), 2).doubleValue();
        ;
        // Сумма
        double valueSumCost = valueSumCostAndVat - valueSumVat;

        HashMap<String, Object> map = report_.getDetailMap();

        map.put("SUM_COST_80", SaleDocumentCalculator.roundAndGetString(valueSumCost, 2));
        map.put("SUM_VAT_80", SaleDocumentCalculator.roundAndGetString(valueSumVat, 2));
        map.put("SUM_COST_AND_VAT_80", SaleDocumentCalculator.roundAndGetString(valueSumCostAndVat, 2));

        map.put("INVOICE_NUMBER", invoiceNumber);

        DigitToWords strSumVAT = new DigitToWords(valueSumVat, CurrencyType.BYN);
        map.put("SUM_VAT_STRING_80", strSumVAT.num2str().trim());

        DigitToWords strSumCostAndVAT = new DigitToWords(valueSumCostAndVat, CurrencyType.BYN);
        map.put("SUM_COST_AND_VAT_STRING_80", strSumCostAndVAT.num2str().trim());
    }

    @Override
    ReportProperties prepareProperties(final ReportProperties properties) {
        properties.setBlankName("invoice_by_" + document.getDocument().getDocumentNumber());
        properties.getTemplate().setTemplateName("invoice_by.ott");
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
