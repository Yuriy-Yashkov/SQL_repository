package by.march8.ecs.framework.common.printers;

import com.sun.star.beans.PropertyValue;

import javax.print.PrintService;

/**
 * Параметры печати
 * Created by Andy on 24.10.2014.
 */
public class PrinterSettings {

    private PropertyValue[] printerProperties;
    private PropertyValue[] documentProperties;

    public PropertyValue[] getPrinterProperties() {
        return printerProperties;
    }

    public void setPrinterProperties(final PropertyValue[] printerProperties) {
        this.printerProperties = printerProperties;
    }

    public void setPrinterProperties(final PrintService printService) {
        printerProperties = new PropertyValue[1];
        printerProperties[0] = new PropertyValue();
        printerProperties[0].Name = "Name";
        printerProperties[0].Value = printService.getName();
    }

    public PropertyValue[] getDocumentProperties() {
        return documentProperties;
    }

    public void setDocumentProperties(final PropertyValue[] documentProperties) {
        this.documentProperties = documentProperties;
    }
}
