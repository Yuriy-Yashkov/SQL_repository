package by.march8.entities.product;

import by.march8.api.BaseEntity;
import by.march8.api.RedirectToEntityManager;
import by.march8.api.MarchDataSourceEnum;

import javax.persistence.*;

/**
 * @author Andy 21.01.2019 - 7:53.
 */
@Entity
@Table(name = "PRODUCTION_CATALOG_PRODUCT")
@RedirectToEntityManager(redirectTo = MarchDataSourceEnum.DS_SQLMARCH8)
public class ProductionCatalogProduct extends BaseEntity {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne()
    @JoinColumn(name = "REF_CATALOG_PRODUCTION_ARTICLE_ID")
    private ProductionCatalogArticle article;
    @Column(name = "SD_ID")
    private int productId;
    @Column(name = "COLORS")
    private String colors;
    @Column(name = "PRICE_VALUE")
    private double priceValue;
    @Column(name = "DISCOUNT_VALUE")
    private double discountValue;
    @Column(name = "PRICE_DISCOUNT_VALUE")
    private double priceDiscountValue;
    @Column(name = "SIZE")
    private int size;
    @Column(name = "GROWTH")
    private int growth;
    @Column(name="AMOUNT")
    private int amount ;

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
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

    public void setPriceValue(double priceValue) {
        this.priceValue = priceValue;
    }

    public double getDiscountValue() {
        return discountValue;
    }

    public void setDiscountValue(double discountValue) {
        this.discountValue = discountValue;
    }

    public double getPriceDiscountValue() {
        return priceDiscountValue;
    }

    public void setPriceDiscountValue(double priceDiscountValue) {
        this.priceDiscountValue = priceDiscountValue;
    }

    public ProductionCatalogArticle getArticle() {
        return article;
    }

    public void setArticle(ProductionCatalogArticle article) {
        this.article = article;
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

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
