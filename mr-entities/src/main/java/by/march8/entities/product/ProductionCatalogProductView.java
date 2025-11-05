package by.march8.entities.product;

import by.march8.api.BaseEntity;
import by.march8.api.RedirectToEntityManager;
import by.march8.api.TableHeader;
import by.march8.api.MarchDataSourceEnum;

import javax.persistence.*;

@NamedQueries({
        @NamedQuery(name = "ProductionCatalogProductView.findByArticleReference",
                query = "SELECT item FROM ProductionCatalogProductView item WHERE item.articleReference = :reference " +
                        "ORDER BY item.grade, item.size, item.growth")
})

/**
 * @author Andy 21.01.2019 - 7:53.
 */
@Entity
@Table(name="VIEW_PRODUCTION_CATALOG_PRODUCT")
@RedirectToEntityManager(redirectTo = MarchDataSourceEnum.DS_SQLMARCH8)
public class ProductionCatalogProductView extends BaseEntity{

    @Id
    @Column(name="ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    //@TableHeader(name = "ID", width = -50, sequence = 999)
    private int id ;
    @Column(name = "REF_CATALOG_PRODUCTION_ARTICLE_ID")
    //@TableHeader(name = "REF", width = -50, sequence = 888)
    private int articleReference;
    @Column(name = "KLD_ID")
    //@TableHeader(name = "KLDID", width = -50, sequence = 777)
    private int articleId;
    @Column(name="SD_ID")
    //@TableHeader(name = "SDID", width = -50, sequence = 666)
    private int productId ;
    @Column(name="SIZE_PRINT")
    private String sizePrint ;

    @Column(name = "COLORS")
    private String colors ;
    @TableHeader(name = "Цена", width = -50, sequence = 200)
    @Column(name="PRICE_VALUE")
    private double priceValue ;
    @TableHeader(name = "%", width = -50, sequence = 210)
    @Column(name="DISCOUNT_VALUE")
    private double discountValue ;
    @TableHeader(name = "Со скидкой", width = -50, sequence = 230)
    @Column(name="PRICE_DISCOUNT_VALUE")
    private double priceDiscountValue ;
    @Column(name="SIZE")
    private int size;
    @Column(name="GROWTH")
    private int growth ;

    @Column(name = "GRADE")
    private int grade ;

    @Column(name = "AMOUNT")
    private int amount;
    @Transient
    private String colorToHTML;
    @Transient
    private int lineHeight;


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
        if(colors!=null) {
            return colors.trim();
        }
        return null ;
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

    public int getArticleReference() {
        return articleReference;
    }

    public void setArticleReference(int articleReference) {
        this.articleReference = articleReference;
    }

    public int getArticleId() {
        return articleId;
    }

    public void setArticleId(int articleId) {
        this.articleId = articleId;
    }

    @TableHeader(name = "Размер", width = 100, sequence = 10)
    public String getSizePrint() {
        if(sizePrint!=null) {
            return sizePrint.trim();
        }
        return null ;
    }

    public void setSizePrint(String sizePrint) {
        this.sizePrint = sizePrint;
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

    @TableHeader(name = "Цвет", width = 100, sequence = 100)
    public String getColorToHTML() {
        return colorToHTML;
    }

    public void setColorToHTML(String colorToHTML) {
        this.colorToHTML = colorToHTML;
    }

    public int getLineHeight() {
        return lineHeight;
    }

    public void setLineHeight(int lineHeight) {
        this.lineHeight = lineHeight;
    }

    @TableHeader(name = "Сорт", sequence = 9)
    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    @TableHeader(name = "Остаток", sequence = 399)
    public int getAmount() {
        return amount;
    }

    public void setAmount(int amunt) {
        this.amount = amunt;
    }
}
