package by.march8.entities.product;

import by.march8.api.BaseEntity;
import by.march8.api.RedirectToEntityManager;
import by.march8.api.MarchDataSourceEnum;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;

@NamedQueries({
        @NamedQuery(name = "ProductionCatalogProductReport.findByDocumentId",
                query = "SELECT item FROM ProductionCatalogProductReport item WHERE item.documentId =:document " +
                        "ORDER BY   item.category, item.itemName, item.modelNumber, item.articleNumber, item.grade, item.size, item.growth")
})

@Entity
@Table(name = "VIEW_PRODUCTION_CATALOG_REPORT")
@RedirectToEntityManager(redirectTo = MarchDataSourceEnum.DS_SQLMARCH8)
public class ProductionCatalogProductReport extends BaseEntity implements Serializable {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "DOCUMENT_ID")
    private int documentId;
    @Column(name = "ARTICLE_ID")
    private int articleId;
    @Column(name = "KLD_ID")
    private int articleCode;
    @Column(name = "SD_ID")
    private int productCode;
    @Column(name = "ITEM_NAME")
    private String itemName;
    @Column(name = "MODEL_NUMBER")
    private int modelNumber;
    @Column(name = "ARTICLE_NUMBER")
    private String articleNumber;
    @Column(name = "ARTICLE_CODE")
    private String articleCipher;
    @Column(name = "CATEGORY")
    private String category;
    @Column(name = "DEFAULT_IMAGE")
    private String image;
    @Column(name = "ARTICLE_COLORS")
    private String colorsArticle;
    @Column(name = "PRODUCT_COLORS")
    private String colorsProduct;
    @Column(name = "RAW_MATERIAL_COMPOSITION")
    private String materialComposition;
    @Column(name = "BRAND_NAME")
    private String brandName;
    @Column(name = "SIZE")
    private int size;
    @Column(name = "GROWTH")
    private int growth;
    @Column(name = "SIZE_PRINT")
    private String sizePrint;
    @Column(name = "PRICE_VALUE")
    private double priceVale;
    @Column(name = "DISCOUNT_VALUE")
    private double discountValue;
    @Column(name = "PRICE_DISCOUNT_VALUE")
    private double priceDiscountValue;
    @Column(name = "PRICE_RANGE")
    private String priceRange;
    @Column(name="SIZE_RANGE")
    private String sizeRange;
    @Column(name="AMOUNT")
    private int amount ;
    @Column(name="GRADE")
    private int grade ;


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

    public int getArticleId() {
        return articleId;
    }

    public void setArticleId(int articleId) {
        this.articleId = articleId;
    }

    public int getArticleCode() {
        return articleCode;
    }

    public void setArticleCode(int articleCode) {
        this.articleCode = articleCode;
    }

    public int getProductCode() {
        return productCode;
    }

    public void setProductCode(int productCode) {
        this.productCode = productCode;
    }

    public String getItemName() {
        if(itemName!=null) {
            return itemName.trim();
        }
        return null ;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public int getModelNumber() {
        return modelNumber;
    }

    public void setModelNumber(int modelNumber) {
        this.modelNumber = modelNumber;
    }

    public String getArticleNumber() {
        return articleNumber;
    }

    public void setArticleNumber(String articleNumber) {
        this.articleNumber = articleNumber;
    }

    public String getArticleCipher() {
        return articleCipher;
    }

    public void setArticleCipher(String articleCipher) {
        this.articleCipher = articleCipher;
    }

    public String getCategory() {
        if(category!=null) {
            return category.trim();
        }
        return "";
    }

    public void setCategory(String category) {
        this.category = category;
    }


    public String getImage() {
        if(image!=null) {
            return image.trim();
        }
        return null ;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getColorsArticle() {
        return colorsArticle;
    }

    public void setColorsArticle(String colorsArticle) {
        this.colorsArticle = colorsArticle;
    }

    public String getColorsProduct() {
        return colorsProduct;
    }

    public void setColorsProduct(String colorsProduct) {
        this.colorsProduct = colorsProduct;
    }

    public String getMaterialComposition() {
        return materialComposition;
    }

    public void setMaterialComposition(String materialComposition) {
        this.materialComposition = materialComposition;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getGrowth() {
        return growth;
    }

    public void setGrowth(int growth) {
        this.growth = growth;
    }


    public String getSizePrint() {
        if(sizePrint!=null) {
            return sizePrint.trim();
        }
        return sizePrint;
    }

    public void setSizePrint(String sizePrint) {
        this.sizePrint = sizePrint;
    }

    public double getPriceVale() {
        return priceVale;
    }

    public void setPriceVale(double priceVale) {
        this.priceVale = priceVale;
    }

    public double getDiscountValue() {
        return discountValue;
    }

    public void setDiscountValue(double discountValue) {
        this.discountValue = discountValue;
    }

    public double getPriceDiscountValue() {
        return new BigDecimal(priceDiscountValue).setScale(2,RoundingMode.HALF_EVEN).doubleValue();
    }

    public void setPriceDiscountValue(double priceDiscountValue) {
        this.priceDiscountValue = priceDiscountValue;
    }

    public String getPriceRange() {
        return priceRange;
    }

    public void setPriceRange(String priceRange) {
        this.priceRange = priceRange;
    }

    public String getSizeRange() {
        return sizeRange;
    }

    public void setSizeRange(String sizeRange) {
        this.sizeRange = sizeRange;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }
}
