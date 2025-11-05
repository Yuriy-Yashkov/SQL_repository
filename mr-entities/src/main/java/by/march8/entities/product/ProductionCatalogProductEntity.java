package by.march8.entities.product;

import by.march8.api.BaseEntity;
import by.march8.api.RedirectToEntityManager;
import by.march8.api.MarchDataSourceEnum;

import javax.persistence.*;

/**
 * @author Andy 23.01.2019 - 7:55.
 */
@Entity
@Table(name="PRODUCTION_CATALOG_PRODUCT")
@RedirectToEntityManager(redirectTo = MarchDataSourceEnum.DS_SQLMARCH8)
public class ProductionCatalogProductEntity extends BaseEntity {

    @Id
    @Column(name="ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id ;
    @Column(name="REF_CATALOG_PRODUCTION_ARTICLE_ID")
    private int articleId ;
    @Column(name="SD_ID")
    private int productId ;
    @Column(name = "COLORS")
    private String colors ;
    @Column(name="PRICE_VALUE")
    private double priceValue ;
    @Column(name= "DISCOUNT_VALUE")
    private double discountValue ;
    @Column(name="PRICE_DISCOUNT_VALUE")
    private double priceDiscountValue ;
    @Column(name = "SIZE")
    private int size;
    @Column(name = "GROWTH")
    private int growth ;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getArticleId() {
        return articleId;
    }

    public void setArticleId(int articleId) {
        this.articleId = articleId;
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
}
