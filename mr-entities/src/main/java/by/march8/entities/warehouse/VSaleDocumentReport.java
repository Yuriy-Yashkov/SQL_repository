package by.march8.entities.warehouse;

import by.march8.api.MarchDataSourceEnum;
import by.march8.api.RedirectToEntityManager;
import by.march8.api.TableHeader;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;

@NamedQueries({


        @NamedQuery(name = "VSaleDocumentReport.findByPeriodAndType",
                query = "SELECT detail FROM VSaleDocumentReport detail " +
                        "WHERE currencyId = :currency_id and documentType = :document_type and " +
                        "documentDate between :period_begin AND :period_end " +
                        "ORDER BY documentDate, documentNumber, itemName, itemColor, itemSize, itemGrade ")
})

/**
 * @author tmp on 01.12.2021 11:13
 */
@Entity
@Table(name = "V_SALE_DOCUMENT_REPORT")
@RedirectToEntityManager(redirectTo = MarchDataSourceEnum.DS_SQLMARCH8)
@XmlRootElement(name = "ReportItem")
@XmlAccessorType(XmlAccessType.FIELD)
public class VSaleDocumentReport {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int id ;
    @Column(name = "DOCUMENT_ID")
    public int documentId ;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "DOCUMENT_DATE")
    @TableHeader(name = "Документ_Дата", sequence = 10)
    private Date documentDate;
    @Column(name = "DOCUMENT_NUMBER")
    @TableHeader(name = "Документ_Номер", sequence = 20)
    private String documentNumber;
    @Column(name = "DOCUMENT_TYPE")
    @TableHeader(name = "Документ_Тип",width = 100, sequence = 40)
    private String documentType;
    @Column(name = "CONTRACTOR_CODE")
    @TableHeader(name = "Контрагент_Код", sequence = 50)
    public int contractorCode ;
    @Column(name = "CONTRACTOR_NAME")
    @TableHeader(name = "Контрагент_Наименование", width = 150, sequence = 60)
    private String contractorName;

    @Column(name = "ITEM_NAME")
    @TableHeader(name = "Продукция_Наименование", width = 150, sequence = 120)
    private String itemName;
    @Column(name = "ITEM_COLOR")
    @TableHeader(name = "Продукция_Цвет", sequence = 150)
    private String itemColor;
    @Column(name = "ITEM_MODEL")
    @TableHeader(name = "Продукция_Модель", sequence = 100)
    private int itemModel;
    @Column(name = "ITEM_ARTICLE")
    @TableHeader(name = "Продукция_Артикул", sequence = 110)
    private String itemArticle;
    @Column(name = "ITEM_SIZE")
    @TableHeader(name = "Продукция_Размер", sequence = 130)
    private String itemSize;
    @Column(name = "ITEM_GRADE")
    @TableHeader(name = "Продукция_Сорт", width = 30, sequence = 140)
    private int itemGrade;

    @Column(name = "CURRENCY_ID")
    //@TableHeader(name = "", sequence = 0)
    private int currencyId ;

    @Transient
    //@TableHeader(name = "", sequence = 0)
    private String currencyName ;
    @Column(name = "CURRENCY_RATE_FIXED")
    @TableHeader(name = "Курс валюты_Фикс.", sequence = 220)
    private float currencyFixedRate;
    @Column(name = "CURRENCY_RATE_SALE")
    @TableHeader(name = "Курс валюты_Прод.", sequence = 230)
    private float currencySaleRate;

    @Column(name = "AMOUNT")
    @TableHeader(name = "Кол-во", sequence = 200)
    public int amount ;

    @Column(name = "VAT")
    @TableHeader(name = "НДС", width = -30,sequence = 250)
    private double valueVAT;

    @Column(name = "PRICE")
    @TableHeader(name = "Белорусские рубли_Цена", width = 30, sequence = 300)
    private double valuePrice;
    @Column(name = "COST")
    @TableHeader(name = "Белорусские рубли_Стоимость", sequence = 310)
    private double valueCost;
    @Column(name = "SUM_VAT")
    @TableHeader(name = "Белорусские рубли_НДС", width = 30, sequence = 320)
    private double valueSumVat;
    @Column(name = "SUM_COST_AND_VAT")
    @TableHeader(name = "Белорусские рубли_Итого", sequence = 330)
    private double valueSumCostAndVat;

    @Column(name = "PRICE_CURRENCY")
    @TableHeader(name = "Иностранная валюта_Цена", width = 30, sequence = 340)
    private double valuePriceCurrency;
    @Column(name = "COST_CURRENCY")
    @TableHeader(name = "Иностранная валюта_Стоимость", sequence = 350)
    private double valueCosCurrencyt;
    @Column(name = "SUM_VAT_CURRENCY")
    @TableHeader(name = "Иностранная валюта_НДС", width = 30, sequence = 360)
    private double valueSumVatCurrency;
    @Column(name = "SUM_COST_AND_VAT_CURRENCY")
    @TableHeader(name = "Иностранная валюта_Итого", sequence = 370)
    private double valueSumCostAndVatCurrency;
    @Transient
    @TableHeader(name = "Нормативное время_На ед. изд.", sequence = 400)
    private double rateCreateItem;
    @Transient
    @TableHeader(name = "Нормативное время_На кол-во", sequence = 410)
    private double rateCreateAll;
    @Transient
    @TableHeader(name = "Нормативные переменные_На ед. изд.", sequence = 420)
    private double rateVariableItem;
    @Transient
    @TableHeader(name = "Нормативные переменные_На кол-во", sequence = 430)
    private double rateVariableAll;

    @TableHeader(name = "Валюта отгрузки", sequence = 210)
    public String getCurrencyName() {
        switch (currencyId){
            case 0: return "";
            case 1: return "BYN";
            case 2: return "RUB";
            case 3: return "USD";
            case 4: return "EUR";
            case 5: return "UAH";
        }

        return "";
    }

    public void setCurrencyName(String currencyName) {
        this.currencyName = currencyName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDocumentId() {
        return documentId;
    }

    public void setDocumentId(int documentId) {
        this.documentId = documentId;
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

    public String getDocumentType() {
        return documentType;
    }

    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }

    public int getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(int currencyId) {
        this.currencyId = currencyId;
    }

    public float getCurrencyFixedRate() {
        return currencyFixedRate;
    }

    public void setCurrencyFixedRate(float currencyFixedRate) {
        this.currencyFixedRate = currencyFixedRate;
    }

    public float getCurrencySaleRate() {
        return currencySaleRate;
    }

    public void setCurrencySaleRate(float currencySaleRate) {
        this.currencySaleRate = currencySaleRate;
    }

    public int getContractorCode() {
        return contractorCode;
    }

    public void setContractorCode(int contractorCode) {
        this.contractorCode = contractorCode;
    }

    public String getContractorName() {
        return contractorName;
    }

    public void setContractorName(String contractorName) {
        this.contractorName = contractorName;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public double getValueVAT() {
        return valueVAT;
    }

    public void setValueVAT(double valueVAT) {
        this.valueVAT = valueVAT;
    }

    public double getValuePrice() {
        return valuePrice;
    }

    public void setValuePrice(double valuePrice) {
        this.valuePrice = valuePrice;
    }

    public double getValueCost() {
        return valueCost;
    }

    public void setValueCost(double valueCost) {
        this.valueCost = valueCost;
    }

    public double getValueSumVat() {
        return valueSumVat;
    }

    public void setValueSumVat(double valueSumVat) {
        this.valueSumVat = valueSumVat;
    }

    public double getValueSumCostAndVat() {
        return valueSumCostAndVat;
    }

    public void setValueSumCostAndVat(double valueSumCostAndVat) {
        this.valueSumCostAndVat = valueSumCostAndVat;
    }

    public double getValuePriceCurrency() {
        return valuePriceCurrency;
    }

    public void setValuePriceCurrency(double valuePriceCurrency) {
        this.valuePriceCurrency = valuePriceCurrency;
    }

    public double getValueCosCurrencyt() {
        return valueCosCurrencyt;
    }

    public void setValueCosCurrencyt(double valueCosCurrencyt) {
        this.valueCosCurrencyt = valueCosCurrencyt;
    }

    public double getValueSumVatCurrency() {
        return valueSumVatCurrency;
    }

    public void setValueSumVatCurrency(double valueSumVatCurrency) {
        this.valueSumVatCurrency = valueSumVatCurrency;
    }

    public double getValueSumCostAndVatCurrency() {
        return valueSumCostAndVatCurrency;
    }

    public void setValueSumCostAndVatCurrency(double valueSumCostAndVatCurrency) {
        this.valueSumCostAndVatCurrency = valueSumCostAndVatCurrency;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemColor() {
        return itemColor;
    }

    public void setItemColor(String itemColor) {
        this.itemColor = itemColor;
    }

    public int getItemModel() {
        return itemModel;
    }

    public void setItemModel(int itemModel) {
        this.itemModel = itemModel;
    }

    public String getItemArticle() {
        return itemArticle;
    }

    public void setItemArticle(String itemArticle) {
        this.itemArticle = itemArticle;
    }

    public String getItemSize() {
        return itemSize;
    }

    public void setItemSize(String itemSize) {
        this.itemSize = itemSize;
    }

    public int getItemGrade() {
        return itemGrade;
    }

    public void setItemGrade(int itemGrade) {
        this.itemGrade = itemGrade;
    }

    public double getRateCreateItem() {
        return rateCreateItem;
    }

    public void setRateCreateItem(double rateCreateItem) {

        this.rateCreateItem = new BigDecimal(rateCreateItem).setScale(4, RoundingMode.HALF_UP).doubleValue() ;
        this.rateCreateAll = new BigDecimal(this.rateCreateItem*this.amount).setScale(4, RoundingMode.HALF_UP).doubleValue() ;
    }

    public double getRateCreateAll() {
        return rateCreateAll;
    }

    public void setRateCreateAll(double rateCreateAll) {
        this.rateCreateAll = rateCreateAll;
    }

    public double getRateVariableItem() {
        return rateVariableItem;
    }

    public void setRateVariableItem(double rateVariableItem) {
        this.rateVariableItem = rateVariableItem;
    }

    public double getRateVariableAll() {
        return rateVariableAll;
    }

    public void setRateVariableAll(double rateVariableAll) {
        this.rateVariableAll = rateVariableAll;
    }
}
