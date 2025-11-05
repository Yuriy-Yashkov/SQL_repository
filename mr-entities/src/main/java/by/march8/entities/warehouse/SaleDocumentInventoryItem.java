package by.march8.entities.warehouse;

import by.march8.api.BaseEntity;
import by.march8.api.RedirectToEntityManager;
import by.march8.api.MarchDataSourceEnum;

import javax.persistence.*;
import java.util.Date;


@NamedQueries({
        @NamedQuery(name = "SaleDocumentInventoryItem.findByDocumentId",
                query = "SELECT detail FROM SaleDocumentInventoryItem detail WHERE detail.documentId = :document ")
})


/**
 * @author Andy 11.01.2019 - 8:34.
 */
@Entity
@Table(name = "VIEW_INVENTORY_OTGRUZ2")
@RedirectToEntityManager(redirectTo = MarchDataSourceEnum.DS_SQLMARCH8)
public class SaleDocumentInventoryItem extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private int id;
    @Column(name="DOCUMENT_ID")
    private int documentId;
    @Column(name="PRODUCT_ID")
    private int productId;
    @Column(name="COLOR")
    private String color ;
    @Column(name="EANCODE")
    private String eancode ;
    @Column(name="SIZE_PRINT")
    private String sizePrint ;
    @Column(name="ITEM_SIZE")
    private int itemSize;
    @Column(name="ITEM_GROWTH")
    private int itemGrowth ;
    @Column(name="ITEM_GRADE")
    private int itemGrade ;
    @Column(name="WEIGHT")
    private double weight ;
    @Column(name="ACCOUNTING_PRICE")
    private double accountingPrice ;
    @Column(name="PRICE")
    private double price ;
    @Column(name="VAT")
    private double vat ;
    @Column(name="TRADE_ALLOWANCE")
    private double tradeAllowance ;
    @Column(name="RETAIL_PRICE")
    private double retailPrice ;
    @Column(name = "ITEM_NAME")
    private String itemName ;
    @Column(name="MODEL_NUMBER")
    private String modelNumber ;
    @Column(name="ARTICLE_CODE")
    private String articleCode ;
    @Column(name="ARTICLE_NAME")
    private String articleName ;
    @Column(name="KKR_CODE")
    private int codeKKR ;
    @Column(name="TNVD_CODE")
    private String codeTNVED ;
    @Column(name="PTK_CODE")
    private int codePTK ;
    @Column(name="PRICELIST")
    private String priceList ;

    @Transient
    private double vatWholesale ;


    @Transient
    private Date documentDate;
    @Transient
    private String documentNumber;


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

    public String getColor() {
        if(color!=null) {
            return color.trim();
        }else{
            return "";
        }
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getEancode() {
        if(eancode!=null) {
            return eancode.trim();
        }else{
            return "";
        }
    }

    public void setEancode(String eancode) {
        this.eancode = eancode;
    }

    public String getSizePrint() {
        if(sizePrint!=null) {
            return sizePrint.trim();
        }else{
            return "";
        }
    }

    public void setSizePrint(String sizePrint) {
        this.sizePrint = sizePrint;
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

    public int getItemGrade() {
        return itemGrade;
    }

    public void setItemGrade(int itemGrade) {
        this.itemGrade = itemGrade;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public double getAccountingPrice() {
        return accountingPrice;
    }

    public void setAccountingPrice(double accountingPrice) {
        this.accountingPrice = accountingPrice;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
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

    public double getRetailPrice() {
        return retailPrice;
    }

    public void setRetailPrice(double retailPrice) {
        this.retailPrice = retailPrice;
    }

    public String getItemName() {
        if(itemName!=null) {
            return itemName.trim();
        }else{
            return "";
        }
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getModelNumber() {
        if(modelNumber!=null) {
            return modelNumber.trim();
        }else{
            return "";
        }
    }

    public void setModelNumber(String modelNumber) {
        this.modelNumber = modelNumber;
    }

    public String getArticleCode() {
        if(articleCode!=null) {
            return articleCode.trim();
        }else{
            return "";
        }
    }

    public void setArticleCode(String articleCode) {
        this.articleCode = articleCode;
    }

    public String getArticleName() {
        if(articleName!=null) {
            return articleName.trim();
        }else{
            return "";
        }
    }

    public void setArticleName(String articleName) {
        this.articleName = articleName;
    }

    public int getCodeKKR() {
        return codeKKR;
    }

    public void setCodeKKR(int codeKKR) {
        this.codeKKR = codeKKR;
    }

    public String getCodeTNVED() {
        if(codeTNVED!=null) {
            return codeTNVED.trim();
        }else{
            return "";
        }
    }

    public void setCodeTNVED(String codeTNVED) {
        this.codeTNVED = codeTNVED;
    }

    public int getCodePTK() {
        return codePTK;
    }

    public void setCodePTK(int codePTK) {
        this.codePTK = codePTK;
    }

    public void setDocumentDate(Date documentDate) {
        this.documentDate = documentDate;
    }

    public void setDocumentNumber(String documentNumber) {
        this.documentNumber = documentNumber;
    }

    public Date getDocumentDate() {
        return documentDate;
    }

    public String getDocumentNumber() {
        if(documentNumber!=null) {
            return documentNumber.trim();
        }else{
            return "";
        }
    }

    public String getGradeAsStringPlus() {
            return itemGrade +" сорт";
    }

    public String getPriceList() {
        if(priceList!=null) {
            return priceList.trim();
        }else{
            return "";
        }
    }

    public void setPriceList(String priceList) {
        this.priceList = priceList;
    }

    public double getVatWholesale() {
        return vatWholesale;
    }

    public void setVatWholesale(double vatWholesale) {
        this.vatWholesale = vatWholesale;
    }
}
