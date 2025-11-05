package by.march8.entities.warehouse;

import by.march8.api.RedirectToEntityManager;
import by.march8.api.TableHeader;
import by.march8.api.MarchDataSourceEnum;

import javax.persistence.*;

/**
 * @author Andy 17.12.2014.
 */
@NamedQueries({


        @NamedQuery(name = "VInternalInvoice.findByDocumentId",
                query = "SELECT detail FROM VInternalInvoiceItem detail WHERE detail.documentId = :document ")
})

@Entity
@Table(name = "V_INTERNAL_INVOICE")
@RedirectToEntityManager(redirectTo = MarchDataSourceEnum.DS_SQLMARCH8)
@SuppressWarnings("all")
public class VInternalInvoiceItem {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "scanCode")
    @TableHeader(name="Штрихкод",sequence =14 , width = -80)
    private Long scanCode ;

    @TableHeader(name="Модель",sequence = 0, width = -50)
    @Column(name = "model")
    private String model ;

    @TableHeader(name="Артикул",sequence = 1, width = -80)
    private String articleNumber ;


    private String name ;

    @TableHeader(name="Размер",sequence = 3, width = -80)
    private String size ;

    private String sort ;

    @TableHeader(name="Цвет",sequence = 5 , width = 50)
    private String color ;

    @TableHeader(name="В уп.",sequence = 7, width = -30)
    private int quantityPack ;

    @TableHeader(name="Сдано_ед.",sequence = 9, width = -20)
    private int quantityAll ;

    @TableHeader(name="Сдано_уп",sequence = 8 , width = -20)
    private int quantity ;

    @TableHeader(name="Сдано_сумм",sequence =10 , width = -80)
    private float costAll ;

    @TableHeader(name="Цена",sequence = 6 , width = 50)
    private double cost ;

    @TableHeader(name="П",sequence =15 , width = -10)
    private int part ;

    @TableHeader(name="М.лист",sequence =16 , width = -80)
    private Double routingNumber = 0d ;

    @Column(name = "articleCode")
    private String articleCode ;

    @Column(name = "ean")
    private String eanCode ;
    @Column(name = "narp")
    private String tnvedCode;
    @Column(name = "ptk")
    private int ptkCode ;

    @Column(name="cno")
    private Double accountingPrice ;
    @Column(name="nds")
    private Double accountingVat ;

    public Double getWeight() {
        return weight;
    }



    public void setWeight(final Double weidht) {
        this.weight = weidht;
    }

    @Column(name = "massa")
    private Double weight;

    private int type ;
    private int itemId ;
    private int sortMarch ;
    private int growthMarch ;
    private int sizeMarch ;
    private int codeOne ;
    private int codeTwo ;
    private int sc ;

    @TableHeader(name="Принято_уп.",sequence = 11 , width = -20)
    private int quantityPattern ;

    @TableHeader(name="Принято_ед.",sequence = 12 , width = -20)
    private int quantityPatternAll ;

    @TableHeader(name="Принято_сумм.",sequence = 13 , width = -80)
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

    @TableHeader(name="Наименование",sequence = 2, width = 100)
    public String getName() {
        return name.trim();
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getSize() {
        return size.trim();
    }

    public void setSize(final String size) {
        this.size = size;
    }

    @TableHeader(name="Сорт",sequence = 4 , width = -60)
    public String getSort() {
        return sort.trim();
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

    public int getQuantityPattern() {
        return quantityPattern;
    }

    public void setQuantityPattern(final int quantityPattern) {
        this.quantityPattern = quantityPattern;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(final double cost) {
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

    public int getQuantityPatternAll() {
        return quantityPatternAll;
    }

    public void setQuantityPatternAll(final int quantityPatternAll) {
        this.quantityPatternAll = quantityPatternAll;
    }

    public float getCostAll() {
        return costAll;
    }

    public void setCostAll(final float costAll) {
        this.costAll = costAll;
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

    public String getEanCode() {
        return eanCode;
    }

    public void setEanCode(final String eanCode) {
        this.eanCode = eanCode;
    }

    public String getTnvedCode() {
        return tnvedCode;
    }

    public void setTnvedCode(final String TNVEDCode) {
        this.tnvedCode = TNVEDCode;
    }

    public int getPtkCode() {
        return ptkCode;
    }

    public void setPtkCode(final int ptkCode) {
        this.ptkCode = ptkCode;
    }

    public Double getAccountingPrice() {
        return accountingPrice;
    }

    public void setAccountingPrice(final Double accountingPrice) {
        this.accountingPrice = accountingPrice;
    }

    public Double getAccountingVat() {
        return accountingVat;
    }

    public void setAccountingVat(final Double accountingVat) {
        this.accountingVat = accountingVat;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
