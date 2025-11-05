package by.gomel.freedev.ucframework.uccore.viewport.gridviewport.model;

import by.march8.api.utils.DateUtils;

import javax.swing.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Andy 24.12.2014.
 */
public class ValueFormatter {

    public static Object getObjectValue(Object object) {

        if (object instanceof Date) {
            SimpleDateFormat formatDate = new SimpleDateFormat("dd.MM.yyyy");
            SimpleDateFormat formatDateTime = new SimpleDateFormat("dd.MM.yyyy hh:mm:ss");
            String[] dateTime = DateUtils.getNormalDateTimeFormat((Date) object).split("_");
            if (dateTime[1].equals("12:00:00")) {
                //return dateTime[0];
                return formatDate.format(object);
            } else {
                return formatDateTime.format(object);
                // return dateTime[0] + " " + dateTime[1];
            }

            // SimpleDateFormat f = new SimpleDateFormat("MM.dd.yyyy");
            //  return f.format(object);
        } else if (object instanceof JLabel) {
            return (JLabel) object;
        } else
/*        if (object instanceof byte[]){
            return object;
        }*/
            return object;
    }
}
