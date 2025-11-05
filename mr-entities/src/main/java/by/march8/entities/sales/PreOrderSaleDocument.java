package by.march8.entities.sales;

import by.march8.api.BaseEntity;
import by.march8.api.MarchDataSourceEnum;
import by.march8.api.RedirectToEntityManager;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "PRE_ORDER_SALE_DOCUMENT")
@RedirectToEntityManager(redirectTo = MarchDataSourceEnum.DS_SQLMARCH8)
@SuppressWarnings("all")
public class PreOrderSaleDocument extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private int id;



    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "DOCUMENT_DATE")
    private Date documentDate;
    @Column(name = "DOCUMENT_NUMBER")
    private String documentNumber;
    @Column(name = "REF_PRODUCT_CATALOG_ID")
    private Integer catalogDocumentId = 0;
    @Column(name = "TYPE")
    private int documentType;
    @Column(name = "STATUS")
    private int status;
    @Column(name = "VISIBLE")
    private boolean visible;
    @Column(name = "CURRENCY_ID")
    private int currencyId;
    @Column(name = "CURR_RATE_1")
    private double currencyRateCommon;
    @Column(name = "CURR_RATE_2")
    private double currencyRateAddition;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "DATE_RATE_1")
    private Date currencyRateCommonDate;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "DATE_RATE_2")
    private Date currencyRateAdditionDate;
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
    @Column(name = "DISCOUNT_TYPE")
    private int discountType;
    @Column(name = "DISCOUNT_VALUE")
    private double discountValue;
    @Column(name = "TRADE_ALLOWANCE_TYPE")
    private int tradeAllowanceType;
    @Column(name = "TRADE_ALLOWANCE_VALUE")
    private double tradeAllowanceValue;
    @Column(name = "GRADE_MARKDOWN_TYPE")
    private int gradeMarkdownType;
    @Column(name = "GRADE_MARKDOWN_VALUE")
    private double gradeMarkdownValue;
    @Column(name = "VAT_TYPE")
    private int vatType;
    @Column(name = "VAT_VALUE")
    private double vatValue;
    @Column(name = "CONTRACTOR_ID")
    private int contractorId;
    @Column(name = "CONTRACT_ID")
    private int contractId;
    @Column(name = "AMOUNT")
    private double amount;
    @Transient
    private int contractorCode ;

    @OneToMany(mappedBy = "document", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<PreOrderSaleDocumentItem> specification;

    public PreOrderSaleDocument() {
    }

    public PreOrderSaleDocument(PreOrderSaleDocument other) {
        this.id = other.id;
        this.documentDate = other.documentDate;
        this.documentNumber = other.documentNumber;
        this.catalogDocumentId = other.catalogDocumentId;
        this.documentType = other.documentType;
        this.status = other.status;
        this.visible = other.visible;
        this.currencyId = other.currencyId;
        this.currencyRateCommon = other.currencyRateCommon;
        this.currencyRateAddition = other.currencyRateAddition;
        this.currencyRateCommonDate = other.currencyRateCommonDate;
        this.currencyRateAdditionDate = other.currencyRateAdditionDate;
        this.sumPriceValue = other.sumPriceValue;
        this.sumVatValue = other.sumVatValue;
        this.sumPriceVatValue = other.sumPriceVatValue;
        this.sumPriceCurrencyValue = other.sumPriceCurrencyValue;
        this.sumVatCurrencyValue = other.sumVatCurrencyValue;
        this.sumPriceVatCurrencyValue = other.sumPriceVatCurrencyValue;
        this.discountType = other.discountType;
        this.discountValue = other.discountValue;
        this.tradeAllowanceType = other.tradeAllowanceType;
        this.tradeAllowanceValue = other.tradeAllowanceValue;
        this.gradeMarkdownType = other.gradeMarkdownType;
        this.gradeMarkdownValue = other.gradeMarkdownValue;
        this.vatType = other.vatType;
        this.vatValue = other.vatValue;
        this.contractorId = other.contractorId;
        this.contractId = other.contractId;
        this.amount = other.amount;
        this.specification = other.specification;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    public Date getDocumentDate() {
        return documentDate;
    }

    public void setDocumentDate(Date documentDate) {
        this.documentDate = documentDate;
    }

    public String getDocumentNumber() {
        return documentNumber;
    }

    public void setDocumentNumber(String documentNumber) {
        this.documentNumber = documentNumber;
    }

    public int getCatalogDocumentId() {
        return catalogDocumentId;
    }

    public void setCatalogDocumentId(int catalogDocumentId) {
        this.catalogDocumentId = catalogDocumentId;
    }

    public int getDocumentType() {
        return documentType;
    }

    public void setDocumentType(int documentType) {
        this.documentType = documentType;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public int getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(int currencyId) {
        this.currencyId = currencyId;
    }

    public double getCurrencyRateCommon() {
        return currencyRateCommon;
    }

    public void setCurrencyRateCommon(double currencyRateCommon) {
        this.currencyRateCommon = currencyRateCommon;
    }

    public double getCurrencyRateAddition() {
        return currencyRateAddition;
    }

    public void setCurrencyRateAddition(double currencyRateAddition) {
        this.currencyRateAddition = currencyRateAddition;
    }

    public Date getCurrencyRateCommonDate() {
        return currencyRateCommonDate;
    }

    public void setCurrencyRateCommonDate(Date currencyRateCommonDate) {
        this.currencyRateCommonDate = currencyRateCommonDate;
    }

    public Date getCurrencyRateAdditionDate() {
        return currencyRateAdditionDate;
    }

    public void setCurrencyRateAdditionDate(Date currencyRateAdditionDate) {
        this.currencyRateAdditionDate = currencyRateAdditionDate;
    }

    public double getSumPriceValue() {
        return sumPriceValue;
    }

    public void setSumPriceValue(double sumPriceValue) {
        this.sumPriceValue = sumPriceValue;
    }

    public double getSumVatValue() {
        return sumVatValue;
    }

    public void setSumVatValue(double sumVatValue) {
        this.sumVatValue = sumVatValue;
    }

    public double getSumPriceVatValue() {
        return sumPriceVatValue;
    }

    public void setSumPriceVatValue(double sumPriceVatValue) {
        this.sumPriceVatValue = sumPriceVatValue;
    }

    public double getSumPriceCurrencyValue() {
        return sumPriceCurrencyValue;
    }

    public void setSumPriceCurrencyValue(double sumPriceCurrencyValue) {
        this.sumPriceCurrencyValue = sumPriceCurrencyValue;
    }

    public double getSumVatCurrencyValue() {
        return sumVatCurrencyValue;
    }

    public void setSumVatCurrencyValue(double sumVatCurrencyValue) {
        this.sumVatCurrencyValue = sumVatCurrencyValue;
    }

    public double getSumPriceVatCurrencyValue() {
        return sumPriceVatCurrencyValue;
    }

    public void setSumPriceVatCurrencyValue(double sumPriceVatCurrencyValue) {
        this.sumPriceVatCurrencyValue = sumPriceVatCurrencyValue;
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

    public int getTradeAllowanceType() {
        return tradeAllowanceType;
    }

    public void setTradeAllowanceType(int tradeAllowanceType) {
        this.tradeAllowanceType = tradeAllowanceType;
    }

    public double getTradeAllowanceValue() {
        return tradeAllowanceValue;
    }

    public void setTradeAllowanceValue(double tradeAllowanceValue) {
        this.tradeAllowanceValue = tradeAllowanceValue;
    }

    public int getGradeMarkdownType() {
        return gradeMarkdownType;
    }

    public void setGradeMarkdownType(int gradeMarkdownType) {
        this.gradeMarkdownType = gradeMarkdownType;
    }

    public double getGradeMarkdownValue() {
        return gradeMarkdownValue;
    }

    public void setGradeMarkdownValue(double gradeMarkdownValue) {
        this.gradeMarkdownValue = gradeMarkdownValue;
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

    public int getContractorId() {
        return contractorId;
    }

    public void setContractorId(int contractorId) {
        this.contractorId = contractorId;
    }

    public int getContractId() {
        return contractId;
    }

    public void setContractId(int contractId) {
        this.contractId = contractId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public List<PreOrderSaleDocumentItem> getSpecification() {
        if (specification == null) {
            specification = new ArrayList<>();
        }
        return specification;
    }

    public void setSpecification(List<PreOrderSaleDocumentItem> specification) {
        this.specification = specification;
    }

    public void setCatalogDocumentId(Integer catalogDocumentId) {
        this.catalogDocumentId = catalogDocumentId;
    }

    public int getContractorCode() {
        return contractorCode;
    }

    public void setContractorCode(int contractorCode) {
        this.contractorCode = contractorCode;
    }
}
