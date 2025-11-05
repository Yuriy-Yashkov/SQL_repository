package by.march8.entities.sales;

import by.march8.api.BaseEntity;
import by.march8.api.RedirectToEntityManager;
import by.march8.api.MarchDataSourceEnum;
import by.march8.api.TableHeader;
import by.march8.entities.classifier.ClassifierItem;
import by.march8.entities.product.OrderItem;

import javax.persistence.*;

@Entity
@Table(name = "PRE_ORDER_SALE_DOCUMENT_ITEM")
@RedirectToEntityManager(redirectTo = MarchDataSourceEnum.DS_SQLMARCH8)
public class PreOrderSaleDocumentItem extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    @TableHeader(name = "ID", sequence = 99)
    private int id;
    @ManyToOne
    @JoinColumn(name = "REF_PRE_ORDER_SALE_DOCUMENT_ID")
    private PreOrderSaleDocument document;
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
    @TableHeader(name = "Количество", sequence = 30)
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
    @Column(name = "SPECIAL_PRICE")
    private int specialPrice ;

    @Transient
    private String note;

    public PreOrderSaleDocumentItem() {
    }

    public PreOrderSaleDocumentItem(PreOrderSaleDocumentItem other) {
        this.productId = other.productId;
        this.accountingPrice = other.accountingPrice;
        this.vat = other.vat;
        this.tradeAllowance = other.tradeAllowance;
        this.discount = other.discount;
        this.itemColor = other.itemColor;
        this.amount = other.amount;
        this.status = other.status;
        this.retailPrice = other.retailPrice;
        this.cost = other.cost;
        this.sumCost = other.sumCost;
        this.sumVat = other.sumVat;
        this.sumCostVat = other.sumCostVat;
        this.costCurrency = other.costCurrency;
        this.sumCostCurrency = other.sumCostCurrency;
        this.sumVatCurrency = other.sumVatCurrency;
        this.sumCostVatCurrency = other.sumCostVatCurrency;
    }

    public PreOrderSaleDocumentItem(PreOrderSaleDocumentItemBase other) {
        this.productId = other.getProductId();
        this.accountingPrice = other.getAccountingPrice();
        this.vat = other.getVat();
        this.tradeAllowance = other.getTradeAllowance();
        this.discount = other.getDiscount();
        this.itemColor = other.getItemColor();
        this.amount = other.getAmount();
        this.status = other.getStatus();
        this.retailPrice = other.getRetailPrice();
        this.cost = other.getCost();
        this.sumCost = other.getSumCost();
        this.sumVat = other.getSumVat();
        this.sumCostVat = other.getSumCostVat();
        this.costCurrency = other.getCostCurrency();
        this.sumCostCurrency = other.getSumCostCurrency();
        this.sumVatCurrency = other.getSumVatCurrency();
        this.sumCostVatCurrency = other.getSumCostVatCurrency();
    }

    public PreOrderSaleDocumentItem(OrderItem item) {
        this.productId = item.getProductId();
        this.itemColor = item.getItemColor();
        this.amount = item.getAmount();
        this.cost = item.getItemPrice();
    }

    public PreOrderSaleDocumentItem(ClassifierItem item) {
        this.productId = item.getId();
        this.itemColor = "";
        this.amount = 1;
        this.cost = item.getPriceWholesale();
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    public PreOrderSaleDocument getDocument() {
        return document;
    }

    public void setDocument(PreOrderSaleDocument document) {
        this.document = document;
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

    @TableHeader(name = "Цвет", sequence = 20)
    public String getItemColor() {
        if (itemColor != null) {
            return itemColor.trim();
        }
        return "";
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

    @TableHeader(name = "Росто-размер", sequence = 10)
    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public int getSpecialPrice() {
        return specialPrice;
    }

    public void setSpecialPrice(int specialPrice) {
        this.specialPrice = specialPrice;
    }
}
