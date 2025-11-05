package by.march8.ecs.application.modules.warehouse.external.shipping.model;

import java.util.Date;

/**
 * @author Andy 06.09.2018 - 7:29.
 */
public class DocumentUploadPreset {

    private Date periodBegin;
    private Date periodEnd;

    private int contractorCode;

    private boolean saveAs;
    private int formatType;

    private boolean fixedTradeAllowance;
    private int childAllowance;
    private int adultAllowance;

    private int addressId;


    public Date getPeriodBegin() {
        return periodBegin;
    }

    public void setPeriodBegin(Date periodBegin) {
        this.periodBegin = periodBegin;
    }

    public Date getPeriodEnd() {
        return periodEnd;
    }

    public void setPeriodEnd(Date periodEnd) {
        this.periodEnd = periodEnd;
    }

    public int getContractorCode() {
        return contractorCode;
    }

    public void setContractorCode(int contractorCode) {
        this.contractorCode = contractorCode;
    }

    public boolean isSaveAs() {
        return saveAs;
    }

    public void setSaveAs(boolean saveAs) {
        this.saveAs = saveAs;
    }

    public int getFormatType() {
        return formatType;
    }

    public void setFormatType(int formatType) {
        this.formatType = formatType;
    }

    public int getChildAllowance() {
        return childAllowance;
    }

    public void setChildAllowance(int childAllowance) {
        this.childAllowance = childAllowance;
    }

    public int getAdultAllowance() {
        return adultAllowance;
    }

    public void setAdultAllowance(int adultAllowance) {
        this.adultAllowance = adultAllowance;
    }

    public boolean isFixedTradeAllowance() {
        return fixedTradeAllowance;
    }

    public void setFixedTradeAllowance(boolean fixedTradeAllowance) {
        this.fixedTradeAllowance = fixedTradeAllowance;
    }

    public int getAddressId() {
        return addressId;
    }

    public void setAddressId(int addressId) {
        this.addressId = addressId;
    }
}
