package by.march8.entities.warehouse;

import by.march8.api.TableHeader;


/**
 * Сущность в виде гномика для спецификации накладной внутреннего перемещения
 */
public class DisplacementInvoiceDetail {
    private int id ;

    @TableHeader(name="Штрихкод",sequence =13 , width = -80)
    private Long scanCode ;

    @TableHeader(name="Модель",sequence = 0, width = -50)
    private String model ;

    @TableHeader(name="Артикул",sequence = 1, width = -80)
    private String articleNumber ;

    @TableHeader(name="Наименование",sequence = 2, width = 100)
    private String name ;

    @TableHeader(name="Размер",sequence = 3, width = -80)
    private String size ;

    @TableHeader(name="Сорт",sequence = 4 , width = -60)
    private String sort ;

    @TableHeader(name="Цвет",sequence = 5 , width = 50)
    private String color ;

    @TableHeader(name="В уп.",sequence = 6, width = -30)
    private int quantityPack ;

    @TableHeader(name="Сдано_ед.",sequence = 8, width = -20)
    private int quantityAll ;

    @TableHeader(name="Сдано_уп",sequence = 7 , width = -20)
    private int quantity ;

    @TableHeader(name="Сдано_сумм",sequence =9 , width = -80)
    private float costAll ;
    private float cost ;

    @TableHeader(name="П",sequence =14 , width = -10)
    private int part ;
    @TableHeader(name="М.лист",sequence =15 , width = -80)
    private Double routingNumber ;
    private String articleCode ;
    private int type ;
    private int itemId ;
    private int sortMarch ;
    private int growthMarch ;
    private int sizeMarch ;
    private int codeOne ;
    private int codeTwo ;
    private int sc ;

    public int getId() {
        return id;
    }

    public void setId(final int id) {
        this.id = id;
    }

    @TableHeader(name="Принято_уп.",sequence =10 , width = -20)
    private int quantityPattern ;

    @TableHeader(name="Принято_ед.",sequence =11 , width = -20)
    private int quantityPatternAll ;

    @TableHeader(name="Принято_сумм.",sequence =12 , width = -80)
    private float costPattern ;
    private int routingCode ;
    private int documentId ;

    public Long getScanCode() {
        return scanCode;
    }

    public void setScanCode(final Long scanCode) {
        this.scanCode = scanCode;
    }

    public String getModel() {
        return model;
    }

    public void setModel(final String model) {
        this.model = model;
    }

    public String getArticleNumber() {
        return articleNumber;
    }

    public void setArticleNumber(final String articleNumber) {
        this.articleNumber = articleNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getSize() {
        return size;
    }

    public void setSize(final String size) {
        this.size = size;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(final String sort) {
        this.sort = sort;
    }

    public String getColor() {
        return color;
    }

    public void setColor(final String color) {
        this.color = color;
    }

    public int getQuantityPack() {
        return quantityPack;
    }

    public void setQuantityPack(final int quantityPack) {
        this.quantityPack = quantityPack;
    }

    public int getQuantityAll() {
        return quantityAll;
    }

    public void setQuantityAll(final int quantityAll) {
        this.quantityAll = quantityAll;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(final int quantity) {
        this.quantity = quantity;
    }

    public float getCostAll() {
        return costAll;
    }

    public void setCostAll(final float costAll) {
        this.costAll = costAll;
    }

    public float getCost() {
        return cost;
    }

    public void setCost(final float cost) {
        this.cost = cost;
    }

    public int getPart() {
        return part;
    }

    public void setPart(final int part) {
        this.part = part;
    }

    public Double getRoutingNumber() {
        return routingNumber;
    }

    public void setRoutingNumber(final Double routingNumber) {
        this.routingNumber = routingNumber;
    }

    public String getArticleCode() {
        return articleCode;
    }

    public void setArticleCode(final String articleCode) {
        this.articleCode = articleCode;
    }

    public int getType() {
        return type;
    }

    public void setType(final int type) {
        this.type = type;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(final int itemId) {
        this.itemId = itemId;
    }

    public int getSortMarch() {
        return sortMarch;
    }

    public void setSortMarch(final int sortMarch) {
        this.sortMarch = sortMarch;
    }

    public int getGrowthMarch() {
        return growthMarch;
    }

    public void setGrowthMarch(final int growthMarch) {
        this.growthMarch = growthMarch;
    }

    public int getSizeMarch() {
        return sizeMarch;
    }

    public void setSizeMarch(final int sizeMarch) {
        this.sizeMarch = sizeMarch;
    }

    public int getCodeOne() {
        return codeOne;
    }

    public void setCodeOne(final int codeOne) {
        this.codeOne = codeOne;
    }

    public int getCodeTwo() {
        return codeTwo;
    }

    public void setCodeTwo(final int codeTwo) {
        this.codeTwo = codeTwo;
    }

    public int getSc() {
        return sc;
    }

    public void setSc(final int sc) {
        this.sc = sc;
    }

    public int getQuantityPattern() {
        return quantityPattern;
    }

    public void setQuantityPattern(final int quantityPattern) {
        this.quantityPattern = quantityPattern;
    }

    public int getQuantityPatternAll() {
        return quantityPatternAll;
    }

    public void setQuantityPatternAll(final int quantityPatternAll) {
        this.quantityPatternAll = quantityPatternAll;
    }

    public float getCostPattern() {
        return costPattern;
    }

    public void setCostPattern(final float costPattern) {
        this.costPattern = costPattern;
    }

    public int getRoutingCode() {
        return routingCode;
    }

    public void setRoutingCode(final int routingCode) {
        this.routingCode = routingCode;
    }

    public int getDocumentId() {
        return documentId;
    }

    public void setDocumentId(final int documentId) {
        this.documentId = documentId;
    }
}
