package by.march8.entities.sales;

import by.march8.api.BaseEntity;
import by.march8.api.RedirectToEntityManager;
import by.march8.api.TableHeader;
import by.march8.api.MarchDataSourceEnum;

import javax.persistence.*;

@NamedQueries({
        @NamedQuery(name = "PreOrderSaleDocumentItemView.findAllByDocumentId",
                query = "SELECT item FROM PreOrderSaleDocumentItemView item WHERE item.documentId = :document " +
                        "ORDER BY item.itemName, item.modelNumber, item.articleNumber"),
        @NamedQuery(name = "PreOrderSaleDocumentItemView.findByDocumentId",
                query = "SELECT item FROM PreOrderSaleDocumentItemView item WHERE item.documentId = :document " +
                        "ORDER BY item.itemName, item.modelNumber, item.articleNumber"),
        @NamedQuery(name = "PreOrderSaleDocumentItemView.findByDocumentIdAndGroup",
                query = "SELECT item FROM PreOrderSaleDocumentItemView item WHERE item.documentId = :document " +
                        "AND item.category like :category " +
                        "ORDER BY item.itemName, item.modelNumber, item.articleNumber"),
        @NamedQuery(name = "PreOrderSaleDocumentItemView.findByDocumentIdAndCategory",
                query = "SELECT item FROM PreOrderSaleDocumentItemView item WHERE item.documentId = :document " +
                        "AND item.category like :category " +
                        "ORDER BY item.itemName, item.modelNumber, item.articleNumber"),
        @NamedQuery(name = "PreOrderSaleDocumentItemView.findById",
                query = "SELECT item FROM PreOrderSaleDocumentItemView item WHERE item.id = :id ")
})

@Entity
@Table(name = "VIEW_PRE_ORDER_SALE_DOCUMENT_ITEM")
@RedirectToEntityManager(redirectTo = MarchDataSourceEnum.DS_SQLMARCH8)
public class PreOrderSaleDocumentItemView extends BaseEntity {
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
    @Column(name = "SPECIAL_PRICE")
    private int specialPrice;
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
    @Column(name = "ARTICLE_ID")
    private int articleId;
    @Column(name = "CATEGORY")
    private String category;
    @Column(name = "ARTICLE_NUMBER")
    private String articleNumber;
    @Column(name = "ARTICLE_CODE")
    private String articleCode;
    @Column(name = "MODEL_NUMBER")
    private int modelNumber;
    @Column(name = "ITEM_NAME")
    private String itemName;
    @Column(name = "SIZE_PRINT")
    private String itemPrintSize;
    @Column(name = "SIZE")
    private int itemSize;
    @Column(name = "GROWTH")
    private int itemGrowth;
    @Column(name = "GRADE")
    private int itemGrade;
    @Column(name = "CLASSIFIER_PRICE")
    private double classifierPrice;
    @Column(name = "CLASSIFIER_VAT")
    private double classifierVat;



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

    @TableHeader(name = "Уч.цена", width = -50, sequence = 200)
    public Double getAccountingPrice() {
        return accountingPrice;
    }

    public void setAccountingPrice(double accountingPrice) {
        this.accountingPrice = accountingPrice;
    }
    @TableHeader(name = "НДС", width = -50, sequence = 201)
    public Double getVat() {
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

    @TableHeader(name = "Скидка", width = -50, sequence = 202)
    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    @TableHeader(name = "Цвет",  width = -100, sequence = 120)
    public String getItemColor() {
        if (itemColor != null) {
            return itemColor.trim();
        }
        return null;
    }

    public void setItemColor(String itemColor) {
        this.itemColor = itemColor;
    }

    @TableHeader(name = "Кол-во",  width = -50, sequence = 120)
    public Double getAmount() {
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

    @TableHeader(name = "Рубли_Цена", width = -50, sequence = 300)
    public Double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    @TableHeader(name = "Рубли_Сумма", width = -50, sequence = 310)
    public Double getSumCost() {
        return sumCost;
    }

    public void setSumCost(double sumCost) {
        this.sumCost = sumCost;
    }

    @TableHeader(name = "Рубли_НДС", width = -50, sequence = 320)
    public Double getSumVat() {
        return sumVat;
    }

    public void setSumVat(double sumVat) {
        this.sumVat = sumVat;
    }

    @TableHeader(name = "Рубли_Всего", width = -50, sequence = 330)
    public Double getSumCostVat() {
        return sumCostVat;
    }

    public void setSumCostVat(double sumCostVat) {
        this.sumCostVat = sumCostVat;
    }

    @TableHeader(name = "Валюта_Цена", width = -50, sequence = 400)
    public Double getCostCurrency() {
        return costCurrency;
    }

    public void setCostCurrency(double costCurrency) {
        this.costCurrency = costCurrency;
    }

    @TableHeader(name = "Валюта_Сумма", width = -50, sequence = 410)
    public Double getSumCostCurrency() {
        return sumCostCurrency;
    }

    public void setSumCostCurrency(double sumCostCurrency) {
        this.sumCostCurrency = sumCostCurrency;
    }

    @TableHeader(name = "Валюта_НДС", width = -50, sequence = 420)
    public Double getSumVatCurrency() {
        return sumVatCurrency;
    }

    public void setSumVatCurrency(double sumVatCurrency) {
        this.sumVatCurrency = sumVatCurrency;
    }

    @TableHeader(name = "Валюта_Всего", width = -50, sequence = 430)
    public Double getSumCostVatCurrency() {
        return sumCostVatCurrency;
    }

    public void setSumCostVatCurrency(double sumCostVatCurrency) {
        this.sumCostVatCurrency = sumCostVatCurrency;
    }

    public int getArticleId() {
        return articleId;
    }

    public void setArticleId(int articleId) {
        this.articleId = articleId;
    }

    public String getCategory() {
        if (category != null) {
            return category.trim();
        }
        return null;
    }

    public void setCategory(String articleCode) {
        this.articleCode = articleCode;
    }

    @TableHeader(name = "Продукция_Артикул",  width = -80,sequence = 30)
    public String getArticleNumber() {
        if (articleNumber != null) {
            return articleNumber.trim();
        }
        return null;
    }

    public void setArticleNumber(String articleNumber) {
        this.articleNumber = articleNumber;
    }

    @TableHeader(name = "Продукция_Модель",  width = -50, sequence = 20)
    public int getModelNumber() {
        return modelNumber;
    }

    public void setModelNumber(int modelNumber) {
        this.modelNumber = modelNumber;
    }

    @TableHeader(name = "Продукция_Наименование", sequence = 10)
    public String getItemName() {
        if (itemName != null) {
            return itemName.trim();
        }
        return null;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    @TableHeader(name = "Продукция_Размер", width = -100, sequence = 40)
    public String getItemPrintSize() {
        if (itemPrintSize != null) {
            return itemPrintSize.trim();
        }
        return null;
    }

    public void setItemPrintSize(String itemPrintSize) {
        this.itemPrintSize = itemPrintSize;
    }

    public int getItemSize() {
        return itemSize;
    }

    public void setItemSize(int itemSize) {
        this.itemSize = itemSize;
    }

    public int getItemGrowth() {
        return itemGrowth;
    }

    public void setItemGrowth(int itemGrowth) {
        this.itemGrowth = itemGrowth;
    }

    @TableHeader(name = "Сорт", width = -30, sequence = 100)
    public int getItemGrade() {
        return itemGrade;
    }

    public void setItemGrade(int itemGrade) {
        this.itemGrade = itemGrade;
    }

    @TableHeader(name = "Справочник_Цена", width = -50, sequence = 500)
    public Double getClassifierPrice() {
        return classifierPrice;
    }

    public void setClassifierPrice(double classifierPrice) {
        this.classifierPrice = classifierPrice;
    }

    @TableHeader(name = "Справочник_НДС", width = -50, sequence = 510)
    public Double getClassifierVat() {
        return classifierVat;
    }

    public void setClassifierVat(double classifierVat) {
        this.classifierVat = classifierVat;
    }

    public String getItemInformation() {
        return "<p>Наименование: "+getItemName() + "<p>Артикул: " + getArticleNumber() + "<p>Цвет: " + getItemColor() + "<p>Размер: " + getItemPrintSize();
    }

    public String getModelNumberAsString() {
        return String.valueOf(modelNumber);
    }


    public String getArticleCode() {
        return articleCode;
    }

    public void setArticleCode(String articleCode) {
        this.articleCode = articleCode;
    }

    public int getSpecialPrice() {
        return specialPrice;
    }

    public void setSpecialPrice(int specialPrice) {
        this.specialPrice = specialPrice;
    }
}
