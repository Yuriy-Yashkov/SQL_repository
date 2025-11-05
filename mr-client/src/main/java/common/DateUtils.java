package common;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Pattern;

/**
 * Вспомогательный класс для работы с паттернами календаря
 *
 * @author andy-linux
 */
@SuppressWarnings("unused")
public class DateUtils {

    /**
     * Метод выводит временной штамп формата, заданного пользователем.
     *
     * @param date the date
     * @return the string
     */
    public static String dateToFormatString(Date date) {
        SimpleDateFormat ft = new SimpleDateFormat("hh:mm:ss : dd.MM.yyyy");
        return ft.format(date);
    }

    public static String dateFistToSQLTimestamp(Date date) {
        //2008-12-20 00:00:00
        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
        return ft.format(date);
    }

    public static String dateLastToSQLTimestamp(Date date) {
        //2008-12-20 00:00:00
        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd 23:59:59");
        return ft.format(date);
    }

    public static String dateToSQLTimestampShort(Date date) {
        //2008-12-20 00:00:00
        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");
        return ft.format(date);
    }

    /**
     * Возвращает текущее время
     *
     * @return the date now
     */
    public static Date getDateNow() {
        Calendar c = Calendar.getInstance();
        return c.getTime();
    }

    public static String getNormalDateFormat(Date date) {
        //2008-12-20 00:00:00
        SimpleDateFormat ft = new SimpleDateFormat("dd.MM.yyyy");
        return ft.format(date);
    }

    public static String getFileNameByDate(Date date) {
        //2008-12-20 00:00:00
        SimpleDateFormat ft = new SimpleDateFormat("yyyyMMdd");
        return ft.format(date);
    }

    public static String getNormalDateTimeFormat(Date date) {
        //2008-12-20 00:00:00
        SimpleDateFormat ft = new SimpleDateFormat("dd.MM.yyy_hh:mm:ss");
        return ft.format(date);
    }

    public static String getNormalTimeFormat(Date date) {

        SimpleDateFormat ft = new SimpleDateFormat("hh:mm:ss");
        return ft.format(date);
    }

    public static Date getFirstDay(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.DAY_OF_MONTH, Calendar.getInstance().getActualMinimum(Calendar.DAY_OF_MONTH));
        return cal.getTime();
    }


    public static Date getDateByStringValue(final String value) {
        String[] tempPars = value.split(Pattern.quote("."));
        try {
            return new SimpleDateFormat("dd.MM.yyyy").parse(value);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
        // return new Date(Integer.valueOf(tempPars[2]), Integer.valueOf(tempPars[1]), Integer.valueOf(tempPars[0]));
    }

    public static Date getDateByStringValueNew(final String value) {
        String[] tempPars = value.split(Pattern.quote("."));
        try {
            return new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(value);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
        // return new Date(Integer.valueOf(tempPars[2]), Integer.valueOf(tempPars[1]), Integer.valueOf(tempPars[0]));
    }

    public static String getTimestampSale(Date date) {
        SimpleDateFormat ft = new SimpleDateFormat("ddMMyyyyhh");
        return ft.format(date);
    }

    public static String getTimestampFullSale(Date date) {
        SimpleDateFormat ft = new SimpleDateFormat("ddMMyyyyhhmmss");
        return ft.format(date);
    }
}
