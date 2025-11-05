package by.march8.entities.sales;

import by.march8.api.BaseEntity;
import by.march8.api.TableHeader;

public class PreOrderCalculatorItem extends BaseEntity {

    private int id;
    private String itemColor;
    private String itemName;
    private String articleNumber;
    private String articleCode;
    private int modelNumber;
    private int itemGrade;
    private int itemSize;
    private int itemGrowth;
    private double amount;

    private double primeCost ;
    private double accountingPrice;
    private double profitability ;
    private double profitabilityCalc ;

    private int specialPrice ;
    private double discount;
    private double cost;
    private double costSelling;
    private double costCurrency;
    private double profitabilityFact;
    private double primeCostCalc;


    public PreOrderCalculatorItem(PreOrderSaleDocumentItemView other) {
        this.id = other.getId();
        this.itemColor = other.getItemColor();
        this.itemName = other.getItemName();
        this.articleNumber = other.getArticleNumber();
        this.articleCode = other.getArticleCode();
        this.modelNumber = other.getModelNumber();
        this.itemGrade = other.getItemGrade();
        this.itemSize = other.getItemSize();
        this.itemGrowth = other.getItemGrowth();
        this.amount = other.getAmount();

        this.accountingPrice = other.getAccountingPrice();

        this.specialPrice = other.getSpecialPrice() ;
        this.discount = other.getDiscount();
        this.cost = other.getCost();
        this.costCurrency = other.getCostCurrency();
        this.profitabilityFact = other.getAccountingPrice();
    }

    public String getItemColor() {
        return itemColor;
    }

    public void setItemColor(String itemColor) {
        this.itemColor = itemColor;
    }

    @TableHeader(name = "Наименование изделия", sequence = 10)
    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    @TableHeader(name = "Артикул", sequence = 20)
    public String getArticleNumber() {
        return articleNumber;
    }

    public void setArticleNumber(String articleNumber) {
        this.articleNumber = articleNumber;
    }
    @TableHeader(name = "Модель",width = -80, sequence = 30)
    public int getModelNumber() {
        return modelNumber;
    }

    public void setModelNumber(int modelNumber) {
        this.modelNumber = modelNumber;
    }

    @TableHeader(name = "Сорт",width = -30, sequence = 40)
    public int getItemGrade() {
        return itemGrade;
    }

    public void setItemGrade(int itemGrade) {
        this.itemGrade = itemGrade;
    }

    @TableHeader(name = "Размер",width = -30, sequence = 50)
    public int getItemSize() {
        return itemSize;
    }

    public void setItemSize(int itemSize) {
        this.itemSize = itemSize;
    }

    @TableHeader(name = "Рост",width = -30, sequence = 60)
    public int getItemGrowth() {
        return itemGrowth;
    }

    public void setItemGrowth(int itemGrowth) {
        this.itemGrowth = itemGrowth;
    }

    @TableHeader(name = "Кол-во", width = -30, sequence = 70)
    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }



    public void setPrimeCost(double primeCost) {
        this.primeCost = primeCost;
    }

    @TableHeader(name = "Плановые характеристики_Цена", sequence = 110)
    public double getAccountingPrice() {
        return accountingPrice;
    }

    public void setAccountingPrice(double accountingPrice) {
        this.accountingPrice = accountingPrice;
    }



    public void setProfitability(double profitability) {
        this.profitability = profitability;
    }

    @TableHeader(name = "Переменные_Скидка",width = -50, sequence = 200)
    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    @TableHeader(name = "Переменные_Цена Ск", width = -50 , sequence = 202)
    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    @TableHeader(name = "Расчетные характеристики_Цена РФ", sequence = 220)
    public double getCostCurrency() {
        return costCurrency;
    }

    public void setCostCurrency(double costCurrency) {
        this.costCurrency = costCurrency;
    }

    @TableHeader(name = "Расчетные характеристики_Р",width = -50, sequence = 230)
    public double getProfitabilityFact() {
        return profitabilityFact;
    }

    public void setProfitabilityFact(double profitabilityFact) {
        this.profitabilityFact = profitabilityFact;
    }

    public String getArticleCode() {
        return articleCode;
    }

    public void setArticleCode(String articleCode) {
        this.articleCode = articleCode;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    @TableHeader(name = "Плановые характеристики_CC", sequence = 100)
    public double getPrimeCost() {
        return primeCost;
    }

    @TableHeader(name = "Плановые характеристики_Р",width = 0, sequence = 120)
    public double getProfitability() {
        return profitability;
    }

    @TableHeader(name = "Плановые характеристики_СCР ", width = 0, sequence = 100)
    public double getPrimeCostCalc() {
        return primeCostCalc;
    }

    @TableHeader(name = "Плановые характеристики_РР",width = -40, sequence = 121)
    public double getProfitabilityCalc() {
        return profitabilityCalc;
    }

    public void setProfitabilityCalc(double profitabilityCalc) {
        this.profitabilityCalc = profitabilityCalc;
    }

    public void setPrimeCostCalc(double primeCostCalc) {
        this.primeCostCalc = primeCostCalc;
    }

    @TableHeader(name = "Переменные_СЦ",width = -20, sequence = 201)
    public int getSpecialPrice() {
        return specialPrice;
    }

    public void setSpecialPrice(int specialPrice) {
        this.specialPrice = specialPrice;
    }
    @TableHeader(name = "Расчетные характеристики_Цена РБ", sequence = 210)
    public double getCostSelling() {
        return costSelling;
    }

    public void setCostSelling(double costSelling) {
        this.costSelling = costSelling;
    }
}
