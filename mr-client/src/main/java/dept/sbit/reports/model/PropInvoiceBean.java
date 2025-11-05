package dept.sbit.reports.model;

import java.util.Date;

public class PropInvoiceBean {
    private String series;
    private String number;
    private Date date;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getSeries() {
        return series;
    }

    public void setSeries(String series) {
        this.series = series.toUpperCase();
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    @Override
    public String toString() {
        return "PropInvoiceBean [series=" + series + ", number=" + number + "]";
    }

}
