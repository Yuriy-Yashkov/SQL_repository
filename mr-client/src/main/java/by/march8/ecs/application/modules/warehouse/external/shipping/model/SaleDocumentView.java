package by.march8.ecs.application.modules.warehouse.external.shipping.model;

/**
 * @author Andy 06.08.2015.
 */

import by.march8.api.BaseEntity;
import by.march8.api.TableHeader;

import java.util.Date;

/**
 * Сущность описывает накладную отгрузки готовой продукции
 */
@SuppressWarnings("all")
public class SaleDocumentView extends BaseEntity {

    /**
     * ID записи
     */
    private int id;

    /**
     * Дата документа
     */
    @TableHeader(name = "Дата_Документа", sequence = 0, width = -90)
    private Date date;

    @TableHeader(name = "Дата_Реализации", sequence = 1, width = -90)
    private Date saleDate;

    /**
     * Номер документа
     */
    @TableHeader(name = "Номер", sequence = 10)
    private String documentNumber;
    /**
     * Тип документа, операция
     */
    @TableHeader(name = "Операция", sequence = 20)
    private String operation;
    /**
     * Код контрагента
     */
    @TableHeader(name = "Код пол.", sequence = 30, width = -60)
    private int contractorCode;
    /**
     * Наименование контрагента
     */
    @TableHeader(name = "Получатель", sequence = 40)
    private String contractorName;

    /**
     * Сумма по документу без НДС
     */
    // @TableHeader(name = "Сумма без НДС", sequence = 5)
    private double valueSum;
    /**
     * Размер НДС
     */

    private double valueVat;
    /**
     * Сумма по документуу с НДС
     */
    private double valueSumAndVat;
    /**
     * Статус документа, ID
     */
    private int statusId;
    /**
     * Статус документа, Наименование
     */
    @TableHeader(name = "Статус", sequence = 80)
    private String statusText;
    /**
     * Номер заявки
     */
    @TableHeader(name = "№ Заявки", width = 0, sequence = 90)
    private int bidNumber;
    @TableHeader(name = "Saved", width = 0, sequence = 100)
    private Boolean isSaved;
    private int contractorId;
    private int contractId;
    private int adjustmentType;
    private String adjustmentDocumentNumber;

    @TableHeader(name = "Сумма без НДС", sequence = 50)
    public String getSumValue() {
        return String.format("%.2f", valueSum);
    }

    @TableHeader(name = "НДС", sequence = 60)
    public String getVatValue() {
        return String.format("%.2f", valueVat);
    }

    @TableHeader(name = "Сумма с НДС", sequence = 70)
    public String getSumAndVatValue() {
        return String.format("%.2f", valueSumAndVat);
    }

    public int getId() {
        return id;
    }

    public void setId(final int id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(final Date date) {
        this.date = date;
    }

    public String getDocumentNumber() {
        if (documentNumber != null) {
            return documentNumber.trim();
        } else {
            return "";
        }
    }

    public void setDocumentNumber(final String documentNumber) {
        this.documentNumber = documentNumber;
    }

    public String getOperation() {
        if (operation != null) {
            return operation.trim();
        } else {
            return "";
        }
    }

    public void setOperation(final String operation) {
        this.operation = operation;
    }

    public int getContractorCode() {
        return contractorCode;
    }

    public void setContractorCode(final int contractorCode) {
        this.contractorCode = contractorCode;
    }

    public String getContractorName() {
        return contractorName;
    }

    public void setContractorName(final String contractorName) {
        this.contractorName = contractorName;
    }

    public double getValueSum() {
        return valueSum;
    }

    public void setValueSum(final double valueSum) {
        this.valueSum = valueSum;
    }

    public double getValueVat() {
        return valueVat;
    }

    public void setValueVat(final double valueVat) {
        this.valueVat = valueVat;
    }

    public double getValueSumAndVat() {
        return valueSumAndVat;
    }

    public void setValueSumAndVat(final double valueSumAndVat) {
        this.valueSumAndVat = valueSumAndVat;
    }

    public int getStatusId() {
        return statusId;
    }

    public void setStatusId(final int statusId) {
        this.statusId = statusId;
    }

    public String getStatusText() {
        return statusText;
    }

    public void setStatusText(final String statusText) {
        this.statusText = statusText;
    }

    public int getBidNumber() {
        return bidNumber;
    }

    public void setBidNumber(final int bidNumber) {
        this.bidNumber = bidNumber;
    }

    @Override
    public String toString() {
        return "SaleDocument{" +
                "id=" + id +
                ", date=" + date +
                ", documentNumber='" + documentNumber + '\'' +
                ", operation='" + operation + '\'' +
                ", contractorCode=" + contractorCode +
                ", contractorName='" + contractorName + '\'' +
                ", valueSum=" + valueSum +
                ", valueVat=" + valueVat +
                ", valueSumAndVat=" + valueSumAndVat +
                ", statusId=" + statusId +
                ", statusText='" + statusText + '\'' +
                ", bidNumber=" + bidNumber +
                '}';
    }

    public Boolean getIsSaved() {
        return isSaved;
    }

    public void setIsSaved(final Boolean isSaved) {
        this.isSaved = isSaved;
    }

    public int getContractorId() {
        return contractorId;
    }

    public void setContractorId(final int contractorId) {
        this.contractorId = contractorId;
    }

    public int getContractId() {
        return contractId;
    }

    public void setContractId(int contractId) {
        this.contractId = contractId;
    }

    public int getAdjustmentType() {
        return adjustmentType;
    }

    public void setAdjustmentType(int adjustmentType) {
        this.adjustmentType = adjustmentType;
    }

    public String getAdjustmentDocumentNumber() {
        return adjustmentDocumentNumber;
    }

    public void setAdjustmentDocumentNumber(String adjustmentDocumentNumber) {
        this.adjustmentDocumentNumber = adjustmentDocumentNumber;
    }

    public Date getSaleDate() {
        return saleDate;
    }

    public void setSaleDate(Date saleDate) {
        this.saleDate = saleDate;
    }
}
