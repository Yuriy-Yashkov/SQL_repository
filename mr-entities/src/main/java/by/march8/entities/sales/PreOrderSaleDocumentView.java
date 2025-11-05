package by.march8.entities.sales;

import by.march8.api.BaseEntity;
import by.march8.api.RedirectToEntityManager;
import by.march8.api.TableHeader;
import by.march8.api.utils.DateUtils;
import by.march8.api.MarchDataSourceEnum;

import javax.persistence.*;
import java.util.Date;

@NamedQueries({
        @NamedQuery(name = "PreOrderSaleDocumentView.findByPeriod",
                query = "SELECT item FROM PreOrderSaleDocumentView item WHERE item.documentDate BETWEEN :periodBegin AND :periodEnd " +
                        "ORDER BY item.documentDate"),
        @NamedQuery(name = "PreOrderSaleDocumentView.findByPeriodExport",
                query = "SELECT item FROM PreOrderSaleDocumentView item WHERE item.documentDate BETWEEN :periodBegin AND :periodEnd " +
                        "and item.currencyId >= :currency " +
                        "ORDER BY item.documentDate"),
        @NamedQuery(name = "PreOrderSaleDocumentView.findByPeriodInternal",
                query = "SELECT item FROM PreOrderSaleDocumentView item WHERE item.documentDate BETWEEN :periodBegin AND :periodEnd " +
                        "and item.currencyId = :currency " +
                        "ORDER BY item.documentDate"),

        @NamedQuery(name = "PreOrderSaleDocumentView.findByDocumentId",
                query = "SELECT item FROM PreOrderSaleDocumentView item WHERE item.id = :documentId " +
                        "ORDER BY item.documentDate")
})

@Entity
@Table(name = "VIEW_PRE_ORDER_SALE_DOCUMENT")
@RedirectToEntityManager(redirectTo = MarchDataSourceEnum.DS_SQLMARCH8)
public class PreOrderSaleDocumentView extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private int id;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "DOCUMENT_DATE")
    private Date documentDate;
    @Column(name = "DOCUMENT_NUMBER")
    private String documentNumber;
    @Column(name = "DOCUMENT_TYPE_ID")
    private int documentType;
    @Column(name = "DOCUMENT_TYPE_NAME")
    private String documentTypeName;
    @Column(name = "STATUS")
    private int status;
    @Column(name = "CURRENCY_ID")
    private int currencyId;
    @Column(name = "CONTRACTOR_ID")
    private int contractorId;
    @Column(name = "CONTRACTOR_CODE")
    private int contractorCode;
    @Column(name = "CONTRACTOR_NAME")
    private String contractorName;
    @Column(name = "AMOUNT")
    private double amount;
    @Column(name = "SUM_PRICE")
    private double sumPriceValue;
    @Column(name = "SUM_VAT")
    private double sumVatValue;
    @Column(name = "SUM_PRICE_VAT")
    private double sumPriceVatValue;
    @Column(name = "SUM_PRICE_CURRENCY")
    private double sumPriceCurrencyValue;
    @Column(name = "SUM_VAT_CURRENCY")
    private double sumVatCurrencyValue;
    @Column(name = "SUM_PRICE_VAT_CURRENCY")
    private double sumPriceVatCurrencyValue;

    @Column(name = "CURR_RATE_1")
    private double currencyRate1;
    @Column(name = "CURR_RATE_2")
    private double currencyRate2;

    @Column(name = "VAT_TYPE")
    private int vatType;
    @Column(name = "VAT_VALUE")
    private double vatValue;

    @Column(name = "DISCOUNT_TYPE")
    private int discountType;
    @Column(name = "DISCOUNT_VALUE")
    private double discountValue;


    @Override
    //@TableHeader(name = "ID", width = -50, sequence = 999)
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    @TableHeader(name = "Документ_Дата", sequence = 10)
    public Date getDocumentDate() {
        return documentDate;
    }

    public void setDocumentDate(Date documentDate) {
        this.documentDate = documentDate;
    }

    @TableHeader(name = "Документ_Номер", sequence = 20)
    public String getDocumentNumber() {
        if (documentNumber != null) {
            return documentNumber.trim();
        }
        return null;
    }

    public void setDocumentNumber(String documentNumber) {
        this.documentNumber = documentNumber;
    }

    public int getDocumentType() {
        return documentType;
    }

    public void setDocumentType(int documentType) {
        this.documentType = documentType;
    }

    @TableHeader(name = "Документ_Тип", sequence = 30)
    public String getDocumentTypeName() {
        if (documentTypeName != null) {
            return documentTypeName.trim();
        }
        return null;
    }

    public void setDocumentTypeName(String documentTypeName) {
        this.documentTypeName = documentTypeName;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(int currencyId) {
        this.currencyId = currencyId;
    }

    //@TableHeader(name = "CID", width = -50, sequence = 1000)
    public int getContractorId() {
        return contractorId;
    }

    public void setContractorId(int contractorId) {
        this.contractorId = contractorId;
    }

    @TableHeader(name = "Контрагент_Код", width = -50, sequence = 100)
    public int getContractorCode() {
        return contractorCode;
    }

    public void setContractorCode(int contractorCode) {
        this.contractorCode = contractorCode;
    }

    @TableHeader(name = "Контрагент_Наименование", sequence = 110)
    public String getContractorName() {
        if (contractorName != null) {
            return contractorName.trim();
        }
        return null;
    }

    public void setContractorName(String contractorName) {
        this.contractorName = contractorName;
    }

    @TableHeader(name = "Кол-во", sequence = 200)
    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    @TableHeader(name = "Рубли_Всего", sequence = 300)
    public double getSumPriceValue() {
        return sumPriceValue;
    }

    public void setSumPriceValue(double sumPriceValue) {
        this.sumPriceValue = sumPriceValue;
    }

    @TableHeader(name = "Рубли_Всего НДС", sequence = 310)
    public double getSumVatValue() {
        return sumVatValue;
    }

    public void setSumVatValue(double sumVatValue) {
        this.sumVatValue = sumVatValue;
    }

    @TableHeader(name = "Рубли_Всего с НДС", sequence = 320)
    public double getSumPriceVatValue() {
        return sumPriceVatValue;
    }

    public void setSumPriceVatValue(double sumPriceVatValue) {
        this.sumPriceVatValue = sumPriceVatValue;
    }

    @TableHeader(name = "Валюта_Всего", sequence = 400)
    public double getSumPriceCurrencyValue() {
        return sumPriceCurrencyValue;
    }

    public void setSumPriceCurrencyValue(double sumPriceCurrencyValue) {
        this.sumPriceCurrencyValue = sumPriceCurrencyValue;
    }

    @TableHeader(name = "Валюта_Всего НДС", sequence = 410)
    public double getSumVatCurrencyValue() {
        return sumVatCurrencyValue;
    }

    public void setSumVatCurrencyValue(double sumVatCurrencyValue) {
        this.sumVatCurrencyValue = sumVatCurrencyValue;
    }

    @TableHeader(name = "Валюта_Всего НДС", sequence = 420)
    public double getSumPriceVatCurrencyValue() {
        return sumPriceVatCurrencyValue;
    }

    public void setSumPriceVatCurrencyValue(double sumPriceVatCurrencyValue) {
        this.sumPriceVatCurrencyValue = sumPriceVatCurrencyValue;
    }

    public String getDocumentInformation() {
        return getDocumentNumber() + " от " + DateUtils.getNormalDateFormat(getDocumentDate()) + "г.";
    }

    public double getCurrencyRate1() {
        return currencyRate1;
    }

    public void setCurrencyRate1(double currencyRate1) {
        this.currencyRate1 = currencyRate1;
    }

    public double getCurrencyRate2() {
        return currencyRate2;
    }

    public void setCurrencyRate2(double currencyRate2) {
        this.currencyRate2 = currencyRate2;
    }

    public int getVatType() {
        return vatType;
    }

    public void setVatType(int vatType) {
        this.vatType = vatType;
    }

    public double getVatValue() {
        return vatValue;
    }

    public void setVatValue(double vatValue) {
        this.vatValue = vatValue;
    }

    public int getDiscountType() {
        return discountType;
    }

    public void setDiscountType(int discountType) {
        this.discountType = discountType;
    }

    public double getDiscountValue() {
        return discountValue;
    }

    public void setDiscountValue(double discountValue) {
        this.discountValue = discountValue;
    }
}
