package by.march8.api.utils;

import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Вспомогательный класс для работы с паттернами календаря
 *
 * @author andy-linux
 */
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
        if (date == null) {
            return "{НЕТ ДАННЫХ}";
        }
        //2008-12-20 00:00:00
        SimpleDateFormat ft = new SimpleDateFormat("dd.MM.yyyy");
        return ft.format(date);
    }

    public static String getReverseDateWithDash(Date date) {
        if (date == null) {
            return "{НЕТ ДАННЫХ}";
        }
        //2008-12-20 00:00:00
        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");
        return ft.format(date);
    }

    public static String getNormalDateFormatDelimiter(Date date) {
        //2008-12-20 00:00:00
        SimpleDateFormat ft = new SimpleDateFormat("dd_MM_yyyy");
        return ft.format(date);
    }

    public static String getShortNormalDateFormat(Date date) {
        //2008-12-20 00:00:00
        SimpleDateFormat ft = new SimpleDateFormat("dd.MM.yy");
        return ft.format(date);
    }

    public static String getNormalDateTimeFormat(Date date) {
        //2008-12-20 00:00:00
        SimpleDateFormat ft = new SimpleDateFormat("dd.MM.yyy_hh:mm:ss");
        return ft.format(date);
    }

    public static String getNormalDateTimeFormatPlus(Date date) {
        //2008-12-20 00:00:00
        SimpleDateFormat ft = new SimpleDateFormat("ddMMyyyyHHmmss");
        return ft.format(date);
    }

    public static String getNormalDateTimeFormatPlain(Date date) {
        //2008-12-20 00:00:00
        SimpleDateFormat ft = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        return ft.format(date);
    }


    public static String getTimeForDBF(Date date) {

        SimpleDateFormat ft = new SimpleDateFormat("ddMMyyHHmm");
        return ft.format(date);
    }

    public static Date getFirstDay(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.DAY_OF_MONTH, Calendar.getInstance().getActualMinimum(Calendar.DAY_OF_MONTH));
        return cal.getTime();
    }

    public static Date getLastDay(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.DATE, cal.getActualMaximum(Calendar.DATE));
        //cal.set(Calendar.DAY_OF_MONTH, Calendar.getInstance().getActualMaximum(Calendar.DAY_OF_MONTH));
        return cal.getTime();
    }

    public static Date getDateByStringValue(final String value) {
        if (value == null) {
            return null;
        }

        if (value.equals("")) {
            return null;
        }
        try {
            return new SimpleDateFormat("dd.MM.yyyy").parse(value);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Date getDateOnlyByDate(final Date value) {
        try {
            return new SimpleDateFormat("dd.MM.yyyy").parse(getNormalDateFormat(value));
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static java.sql.Date getDateAsSQLDate(final Date value) {
        if (value == null) {
            return null;
        }

        try {
            return java.sql.Date.valueOf(dateToSQLTimestampShort(value));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public static Date getDateByStringValueSimple(final String value) {
        try {
            return new SimpleDateFormat("dd.MM.yy").parse(value);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }


    public static XMLGregorianCalendar getGregorianDate(Date date) {
        GregorianCalendar c = new GregorianCalendar();
        c.setTime(date);
        try {
            return DatatypeFactory.newInstance().newXMLGregorianCalendar(c);
        } catch (Exception e) {
            System.out.println("Ошибка формирования даты типа XMLGregorianCalendar для " + date);
        }
        return null;
    }


    public static String getMonthNameByDate(final Date date) {

        Calendar cal = Calendar.getInstance();
        cal.setTime(date);

        String month;

        switch (cal.get(Calendar.MONTH) + 1) {
            case 1:
                month = "Январь" ;
                break;
            case 2:
                month = "Февраль";
                break;
            case 3:
                month = "Март";
                break;
            case 4:
                month = "Апрель";
                break;
            case 5:
                month = "Май";
                break;
            case 6:
                month = "Июнь";
                break;
            case 7:
                month = "Июль";
                break;
            case 8:
                month = "Август";
                break;
            case 9:
                month = "Сентябрь";
                break;
            case 10:
                month = "Октябрь";
                break;
            case 11:
                month = "Ноябрь";
                break;
            case 12:
                month = "Декабрь";
                break;
            default:
                month = "------";
                break;
        }

        return month;
    }

    public static String getYearAsStringByDate(final Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);

        return String.valueOf(cal.get(Calendar.YEAR));
    }

    public static String getMessageDateTime(final Date date) {
        SimpleDateFormat ft = new SimpleDateFormat("yyyyMMddhhmmss");
        return ft.format(date);
    }

    public static String getMessageDateTimeShort(final Date date) {
        if (date == null) {
            return null;
        }
        SimpleDateFormat ft = new SimpleDateFormat("yyyyMMdd");
        return ft.format(date);
    }

    public static String getMessageDateTimeShortPlain(final Date date) {
        if (date == null) {
            return null;
        }

        SimpleDateFormat ft = new SimpleDateFormat("ddMMyyyy");
        return ft.format(date);
    }

    public static List<DatePeriod> getPeriodListForYear(int year_) {
        List<DatePeriod> list = new ArrayList<>();

        Calendar calendar = Calendar.getInstance();

        int year = year_;
        int month = Calendar.FEBRUARY;
        int date = 1;
        int maxDay = 0;

        for (int i = 0; i < 12; i++) {
            calendar.set(year, i, date, 0, 0, 0);
            Date begin_ = calendar.getTime();
            maxDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
            calendar.set(year, i, maxDay, 23, 59, 59);
            Date end_ = calendar.getTime();
            DatePeriod period = new DatePeriod();
            period.setBegin(begin_);
            period.setEnd(end_);
            list.add(period);
        }

        return list;
    }

    public static DatePeriod getPeriodForDate(Date date) {
        Calendar calendar = Calendar.getInstance();

        int maxDay = 0;
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        Date begin_ = calendar.getTime();
        maxDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        calendar.set(Calendar.DAY_OF_MONTH, maxDay);
        calendar.set(Calendar.HOUR, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 0);

        Date end_ = calendar.getTime();
        DatePeriod period = new DatePeriod();
        period.setBegin(begin_);
        period.setEnd(end_);
        return period ;
    }



}
