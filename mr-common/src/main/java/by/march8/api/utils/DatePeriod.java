package by.march8.api.utils;

import java.util.Date;

/**
 * @author Andy 22.01.2019 - 7:13.
 */
public class DatePeriod {
    private Date begin ;
    private Date end ;

    public Date getBegin() {
        return begin;
    }

    public void setBegin(Date begin) {
        this.begin = begin;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public String printPeriod() {
        return DateUtils.getNormalDateTimeFormatPlain(begin)+" - "+DateUtils.getNormalDateTimeFormatPlain(end);
    }
}
