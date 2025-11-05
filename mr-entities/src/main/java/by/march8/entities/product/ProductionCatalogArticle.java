package by.march8.entities.product;

import by.march8.api.BaseEntity;
import by.march8.api.RedirectToEntityManager;
import by.march8.api.MarchDataSourceEnum;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@NamedQueries({
        @NamedQuery(name = "ProductionCatalogArticle.findByDocumentAndArticleId",
                query = "SELECT item FROM ProductionCatalogArticle item WHERE item.documentId = :document AND item.articleId = :article ")
})

/**
 * @author Andy 21.01.2019 - 7:43.
 */
@Entity
@Table(name = "PRODUCTION_CATALOG_ARTICLE")
@RedirectToEntityManager(redirectTo = MarchDataSourceEnum.DS_SQLMARCH8)
public class ProductionCatalogArticle extends BaseEntity {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "REF_PRODUCTION_CATALOG_ID")
    private int documentId;
    @Column(name = "KLD_ID")
    private int articleId;
    @Column(name = "DISCOUNT_VALUE")
    private double discountValue;
    @Column(name = "RAW_MATERIAL_COMPOSITION")
    private String materialComposition;
    @Column(name = "COLORS")
    private String colors;
    @Column(name = "PRICE_VALUE")
    private double priceValue;
    @Column(name = "DEFAULT_IMAGE")
    private String defaultImage;
    @Column(name="BRAND")
    private String brand ;
    @Column(name = "NOTE")
    private String note;
    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<ProductionCatalogProduct> productList = new ArrayList<>();
    @Column(name = "PRICE_RANGE")
    private String priceRange;
    @Column(name="SIZE_RANGE")
    private String sizeRange;
    @Column(name="AMOUNT")
    private int amount ;

    @Transient
    private int modelNumber ;

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

    public int getArticleId() {
        return articleId;
    }

    public void setArticleId(int articleId) {
        this.articleId = articleId;
    }

    public double getDiscountValue() {
        return discountValue;
    }

    public void setDiscountValue(double discountValue) {
        this.discountValue = discountValue;
    }

    public String getMaterialComposition() {
        return materialComposition;
    }

    public void setMaterialComposition(String materialComposition) {
        this.materialComposition = materialComposition;
    }

    public String getColors() {
        return colors;
    }

    public void setColors(String colors) {
        this.colors = colors;
    }

    public double getPriceValue() {
        return priceValue;
    }

    public void setPriceValue(double preiceValue) {
        this.priceValue = preiceValue;
    }

    public String getDefaultImage() {
        return defaultImage;
    }

    public void setDefaultImage(String defaultImage) {
        this.defaultImage = defaultImage;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public List<ProductionCatalogProduct> getProductList() {
        if (productList == null) {
            productList = new ArrayList<>();
        }
        return productList;
    }

    public void setProductList(List<ProductionCatalogProduct> productList) {
        this.productList = productList;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
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

    public int getModelNumber() {
        return modelNumber;
    }

    public void setModelNumber(int modelNumber) {
        this.modelNumber = modelNumber;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
