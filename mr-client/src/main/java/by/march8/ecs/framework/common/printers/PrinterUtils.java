package by.march8.ecs.framework.common.printers;

import javax.print.PrintService;
import javax.print.PrintServiceLookup;

/**
 * Created by Andy on 21.10.2014.
 */
public class PrinterUtils {

    public static PrintService[] getPrinters() {
        return PrintServiceLookup.lookupPrintServices(null, null);
    }
}
