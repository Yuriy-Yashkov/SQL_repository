package by.march8.entities.sales;

import by.march8.api.BaseEntity;
import by.march8.api.RedirectToEntityManager;
import by.march8.api.MarchDataSourceEnum;

import javax.persistence.*;

@Entity
@Table(name = "PRE_ORDER_SALE_DOCUMENT_ITEM")
@RedirectToEntityManager(redirectTo = MarchDataSourceEnum.DS_SQLMARCH8)
public class PreOrderSaleDocumentItemEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private int id;

    @Column(name = "REF_PRE_ORDER_SALE_DOCUMENT_ID")
    private int documentId;
    @Column(name = "PRODUCT_ID")
    private int productId;
    @Column(name = "ACCOUNTING_PRICE_VALUE")
    private double accountingPrice;
    @Column(name = "VAT_VALUE")
    private double vat;
    @Column(name = "TRADE_ALLOWANCE_VALUE")
    private double tradeAllowance;
    @Column(name = "DISCOUNT_VALUE")
    private double discount;
    @Column(name = "COLOR")
    private String itemColor;
    @Column(name = "AMOUNT")
    private double amount;
    @Column(name = "STATUS")
    private int status;
    @Column(name = "RETAIL_PRICE")
    private double retailPrice;
    @Column(name = "COST")
    private double cost;
    @Column(name = "SUM_COST")
    private double sumCost;
    @Column(name = "SUM_VAT")
    private double sumVat;
    @Column(name = "SUM_COST_VAT")
    private double sumCostVat;
    @Column(name = "COST_CURRENCY")
    private double costCurrency;
    @Column(name = "SUM_COST_CURRENCY")
    private double sumCostCurrency;
    @Column(name = "SUM_VAT_CURRENCY")
    private double sumVatCurrency;
    @Column(name = "SUM_COST_VAT_CURRENCY")
    private double sumCostVatCurrency;

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    public int getDocumentId() {
        return documentId;
    }

    public void setDocumentId(int documentId) {
        this.documentId = documentId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public double getAccountingPrice() {
        return accountingPrice;
    }

    public void setAccountingPrice(double accountingPrice) {
        this.accountingPrice = accountingPrice;
    }

    public double getVat() {
        return vat;
    }

    public void setVat(double vat) {
        this.vat = vat;
    }

    public double getTradeAllowance() {
        return tradeAllowance;
    }

    public void setTradeAllowance(double tradeAllowance) {
        this.tradeAllowance = tradeAllowance;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public String getItemColor() {
        return itemColor;
    }

    public void setItemColor(String itemColor) {
        this.itemColor = itemColor;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public double getRetailPrice() {
        return retailPrice;
    }

    public void setRetailPrice(double retailPrice) {
        this.retailPrice = retailPrice;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public double getSumCost() {
        return sumCost;
    }

    public void setSumCost(double sumCost) {
        this.sumCost = sumCost;
    }

    public double getSumVat() {
        return sumVat;
    }

    public void setSumVat(double sumVat) {
        this.sumVat = sumVat;
    }

    public double getSumCostVat() {
        return sumCostVat;
    }

    public void setSumCostVat(double sumCostVat) {
        this.sumCostVat = sumCostVat;
    }

    public double getCostCurrency() {
        return costCurrency;
    }

    public void setCostCurrency(double costCurrency) {
        this.costCurrency = costCurrency;
    }

    public double getSumCostCurrency() {
        return sumCostCurrency;
    }

    public void setSumCostCurrency(double sumCostCurrency) {
        this.sumCostCurrency = sumCostCurrency;
    }

    public double getSumVatCurrency() {
        return sumVatCurrency;
    }

    public void setSumVatCurrency(double sumVatCurrency) {
        this.sumVatCurrency = sumVatCurrency;
    }

    public double getSumCostVatCurrency() {
        return sumCostVatCurrency;
    }

    public void setSumCostVatCurrency(double sumCostVatCurrency) {
        this.sumCostVatCurrency = sumCostVatCurrency;
    }
}
