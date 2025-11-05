package by.march8.ecs.application.modules.warehouse.external.shipping.model;

import by.march8.api.BaseEntity;

import java.util.Date;

/**
 * @author Andy 07.04.2017.
 */
public class WriteOffFormsItem extends BaseEntity {
    private int id;
    private Date date;
    private String fullNumber;
    private String number;
    private String blankCode;
    private Double summ;

    private String clientName;

    private int export;

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(final int id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(final Date date) {
        this.date = date;
    }

    public String getFullNumber() {
        return fullNumber;
    }

    public void setFullNumber(final String fullNumber) {
        this.fullNumber = fullNumber;
        if (fullNumber != null) {
            number = fullNumber.substring(2, fullNumber.length());
            blankCode = fullNumber.substring(0, 2);
        }
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(final String number) {
        this.number = number;
    }

    public String getBlankCode() {
        return blankCode;
    }

    public void setBlankCode(final String blankCode) {
        this.blankCode = blankCode;
    }

    public Double getSumm() {
        return summ;
    }

    public void setSumm(final Double summ) {
        this.summ = summ;
    }

    public int getExport() {
        return export;
    }

    public void setExport(final int export) {
        this.export = export;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(final String clientName) {
        this.clientName = clientName;
    }
}
