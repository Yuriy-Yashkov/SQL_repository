package by.march8.entities.classifier;

import by.march8.api.BaseEntity;
import by.march8.api.RedirectToEntityManager;
import by.march8.api.TableHeader;
import by.march8.api.MarchDataSourceEnum;

import javax.persistence.*;
import java.util.Date;

/**
 * @author Andy 13.03.2017.
 */

@NamedQueries({
        @NamedQuery(name = "RemainPriceListSearchItem.findByModelNumber",
                query = "SELECT item FROM RemainPriceListSearchItem item  " +
                        " where lower(item.modelNumber) like :model " +
                        "order by item.documentDate, item.itemSize" +
                        "  "),

        @NamedQuery(name = "RemainPriceListSearchItem.findByArticleNumber",
                query = "SELECT item FROM RemainPriceListSearchItem item  " +
                        " where lower(item.articleNumber) like :article " +
                        "order by item.documentDate, item.itemSize" +
                        "  ")
})

@Entity
@Table(name = "V_REMAINS_PRICELIST_SEARCH")
@RedirectToEntityManager(redirectTo = MarchDataSourceEnum.DS_SQLMARCH8)
public class RemainPriceListSearchItem extends BaseEntity {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "ITEM_NAME")
    @TableHeader(name = "Номенклатура_Наименование",sequence = 10)
    private String name ;

    @Column(name = "ITEM_ARTICLE_NUMBER")
    @TableHeader(name = "Номенклатура_Артикул", sequence = 12)
    private String articleNumber ;

    @Column(name="ITEM_ARTICLE_CODE")
    private int articleCode ;

    @Column(name = "ITEM_MODEL")
    @TableHeader(name = "Номенклатура_Модель",width = -100, sequence = 11)
    private String modelNumber ;

    @Column(name="REF_REMAINS_PRICELIST")
    private int priceLilstId ;

    @Column(name="ITEM_SIZE")
    @TableHeader(name = "Номенклатура_Размер",width = -90,sequence = 19)
    private String itemSize ;

    @Column(name="PRICE_1ST_GRADE")
    @TableHeader(name = "Цена реализации_1 сорт",width = -80,sequence = 25)
    private double price1stGrade ;

    @Column(name="PRICE_2ND_GRADE")
    @TableHeader(name = "Цена реализации_2 сорт",width = -80, sequence = 25)
    private double price2ndGrade ;

    @Column(name="VAT_VALUE")
    @TableHeader(name = "Ставка_НДС",width = -50, sequence = 50)
    private int vatValue ;

    @TableHeader(name="Прейскурант_Номер", sequence = 0)
    @Column(name = "DOCUMENT_NUMBER")
    private String documentNumber ;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "DOCUMENT_DATE")
    @TableHeader(name="Прейскурант_Дата", width = -100, sequence = 1)
    private Date documentDate;

    @Column(name="TRADE_ALLOWANCE_VALUE")
    @TableHeader(name="Прейскурант_Надбавка", width = -90, sequence = 3)
    private double tradeAllowanceValue ;

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(final int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getArticleNumber() {
        return articleNumber;
    }

    public void setArticleNumber(final String articleNumber) {
        this.articleNumber = articleNumber;
    }

    public int getArticleCode() {
        return articleCode;
    }

    public void setArticleCode(final int articleCode) {
        this.articleCode = articleCode;
    }

    public String getModelNumber() {
        return modelNumber;
    }

    public void setModelNumber(final String modelNumber) {
        this.modelNumber = modelNumber;
    }

    public int getPriceLilstId() {
        return priceLilstId;
    }

    public void setPriceLilstId(final int priceLilstId) {
        this.priceLilstId = priceLilstId;
    }

    public String getItemSize() {
        return itemSize;
    }

    public void setItemSize(final String itemSize) {
        this.itemSize = itemSize;
    }

    public double getPrice1stGrade() {
        return price1stGrade;
    }

    public void setPrice1stGrade(final double price1stGrade) {
        this.price1stGrade = price1stGrade;
    }

    public double getPrice2ndGrade() {
        return price2ndGrade;
    }

    public void setPrice2ndGrade(final double price2ndGrade) {
        this.price2ndGrade = price2ndGrade;
    }

    public int getVatValue() {
        return vatValue;
    }

    public void setVatValue(final int vatValue) {
        this.vatValue = vatValue;
    }

    public String getDocumentNumber() {
        return documentNumber;
    }

    public void setDocumentNumber(final String documentNumber) {
        this.documentNumber = documentNumber;
    }

    public Date getDocumentDate() {
        return documentDate;
    }

    public void setDocumentDate(final Date documentDate) {
        this.documentDate = documentDate;
    }

    public double getTradeAllowanceValue() {
        return tradeAllowanceValue;
    }

    public void setTradeAllowanceValue(final double tradeAllowanceValue) {
        this.tradeAllowanceValue = tradeAllowanceValue;
    }
}
