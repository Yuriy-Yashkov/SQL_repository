package by.march8.entities.product;


import by.march8.api.BaseEntity;
import by.march8.api.RedirectToEntityManager;
import by.march8.api.TableHeader;
import by.march8.api.MarchDataSourceEnum;

import javax.persistence.*;
import javax.swing.*;

@NamedQueries({
        @NamedQuery(name = "ProductionCatalogArticleView.findAllByDocumentId",
                query = "SELECT item FROM ProductionCatalogArticleView item WHERE item.documentId = :document " +
                        "ORDER BY item.itemName, item.modelNumber, item.articleNumber"),
        @NamedQuery(name = "ProductionCatalogArticleView.findByDocumentId",
                query = "SELECT item FROM ProductionCatalogArticleView item WHERE item.documentId = :document " +
                        "ORDER BY item.itemName, item.modelNumber, item.articleNumber"),
        @NamedQuery(name = "ProductionCatalogArticleView.findByDocumentIdAndGroup",
                query = "SELECT item FROM ProductionCatalogArticleView item WHERE item.documentId = :document " +
                        "AND item.category like :category " +
                        "ORDER BY item.itemName, item.modelNumber, item.articleNumber"),
        @NamedQuery(name = "ProductionCatalogArticleView.findByDocumentIdAndCategory",
                query = "SELECT item FROM ProductionCatalogArticleView item WHERE item.documentId = :document " +
                        "AND item.category like :category " +
                        "ORDER BY item.itemName, item.modelNumber, item.articleNumber"),
        @NamedQuery(name = "ProductionCatalogArticleView.findById",
                query = "SELECT item FROM ProductionCatalogArticleView item WHERE item.id = :id "),
        @NamedQuery(name = "ProductionCatalogArticleView.findByCategoryAndAssortment",
                query = "SELECT item FROM ProductionCatalogArticleView item WHERE item.documentId = :document " +
                        "AND item.category like :category " +
                        "AND item.itemName = :name " +
                        "ORDER BY item.itemName, item.modelNumber, item.articleNumber"),
        @NamedQuery(name = "ProductionCatalogArticleView.findByModel",
                query = "SELECT item FROM ProductionCatalogArticleView item WHERE item.documentId = :document " +
                        "AND item.modelNumber = :model " +
                        "ORDER BY item.itemName, item.modelNumber, item.articleNumber")
})
/**
 * @author Andy 21.01.2019 - 9:08.
 */
@Entity
@Table(name = "VIEW_PRODUCTION_CATALOG_ARTICLE")
@RedirectToEntityManager(redirectTo = MarchDataSourceEnum.DS_SQLMARCH8)
public class ProductionCatalogArticleView extends BaseEntity {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "REF_PRODUCTION_CATALOG_ID")
    private int documentId;
    @Column(name = "ITEM_NAME")
    private String itemName;
    @Column(name = "ARTICLE_NUMBER")
    private String articleNumber;
    @Column(name = "ARTICLE_CODE")
    private String articleCode;
    @Column(name = "CATEGORY")
    private String category;
    @Column(name = "MODEL_NUMBER")
    private int modelNumber;
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
    @Column(name = "BRAND")
    private String brand;
    @Column(name = "NOTE")
    private String note;
    @Column(name = "PRICE_RANGE")
    private String priceRange;
    @Column(name = "SIZE_RANGE")
    private String sizeRange;
    @Transient
    private String colorToHTML;
    @Transient
    private String compositionToHTML;
    @Transient
    private int lineHeight;
    @Transient
    private JLabel image;
    @Transient
    private String sizeRangeHTML;
    @Transient
    private String priceRangeHTML;

    @Column(name="AMOUNT")
    private int amount ;

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

    @TableHeader(name = "Наименование", sequence = 10)
    public String getItemName() {
        if (itemName != null) {
            return itemName.trim();
        }
        return null;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    @TableHeader(name = "Артикул", width = -100, sequence = 20)
    public String getArticleNumber() {
        if (articleNumber != null) {
            return articleNumber.trim();
        }
        return null;
    }

    public void setArticleNumber(String articleNumber) {
        this.articleNumber = articleNumber;
    }

    //@TableHeader(name = "Категория", width = -30, sequence = 25)
    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @TableHeader(name = "Модель", width = -80, sequence = 30)
    public int getModelNumber() {
        return modelNumber;
    }

    public void setModelNumber(int modelNumber) {
        this.modelNumber = modelNumber;
    }

    public int getArticleId() {
        return articleId;
    }

    public void setArticleId(int articleId) {
        this.articleId = articleId;
    }

    //@TableHeader(name = "Сырьевой состав", width = -100, sequence = 100)
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

    //@TableHeader(name = "Цена", width = -50, sequence = 200)
    public double getPriceValue() {
        return priceValue;
    }

    public void setPriceValue(double priceValue) {
        this.priceValue = priceValue;
    }

    //@TableHeader(name = "Скидка", width = -50, sequence = 210)
    public double getDiscountValue() {
        return discountValue;
    }

    public void setDiscountValue(double discountValue) {
        this.discountValue = discountValue;
    }


    public String getDefaultImage() {
        if (defaultImage != null) {
            return defaultImage.trim();
        }
        return null;
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

    public String getArticleCode() {
        return articleCode;
    }

    public void setArticleCode(String articleCode) {
        this.articleCode = articleCode;
    }

    public Object getArticleInformation() {
        return getItemName() + "(арт." + getArticleNumber() + ")";
    }

    @TableHeader(name = "Доступные цвета", sequence = 50)
    public String getColorToHTML() {
        return colorToHTML;
    }

    public void setColorToHTML(String colorToHTML) {
        this.colorToHTML = colorToHTML;
    }

    public int getLineHeight() {
        if (lineHeight == 0) {
            lineHeight = 16;
        }
        return lineHeight;
    }

    public void setLineHeight(int lineHeight) {
        this.lineHeight = lineHeight;
    }

    @TableHeader(name = "Изображение", sequence = 1)
    public JLabel getImage() {
        return image;
    }

    public void setImage(final JLabel image) {
        this.image = image;
    }

    public void prepareComposition() {
        if (materialComposition != null) {
            String[] composition_ = materialComposition.split(",");
            int height = composition_.length * 16;
            if (height > lineHeight) {
                lineHeight = height;
            }
            StringBuilder c_ = new StringBuilder();
            for (String c : composition_) {
                c_.append("<p>").append(c);
            }
            compositionToHTML = "<html>" + c_ + "</html>";
        } else {
            compositionToHTML = "";
        }
    }

    @TableHeader(name = "Сырьевой состав", sequence = 100)
    public String getCompositionToHTML() {
        return compositionToHTML;
    }

    public void setCompositionToHTML(String compositionToHTML) {
        this.compositionToHTML = compositionToHTML;
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

    public void preparePrices() {
        if (priceRange != null) {
            String[] prices_ = priceRange.trim().split("_");
            int height = prices_.length * 16;
            if (height > lineHeight) {
                lineHeight = height;
            }
            StringBuilder c_ = new StringBuilder();
            for (String c : prices_) {
                c_.append("<p>").append(c);
            }
            priceRangeHTML = "<html>" + c_ + "</html>";
        } else {
            priceRangeHTML = "";
        }
    }

    public void prepareSizes() {
        if (sizeRange != null) {
            String[] sizes_ = sizeRange.trim().split("_");
            int height = sizes_.length * 16;
            if (height > lineHeight) {
                lineHeight = height;
            }
            StringBuilder c_ = new StringBuilder();
            for (String c : sizes_) {
                c_.append("<p>").append(c);
            }
            sizeRangeHTML = "<html>" + c_ + "</html>";
        } else {
            sizeRangeHTML = "";
        }
    }

    public void setSizeRangeHTML(String sizeRangeHTML) {
        this.sizeRangeHTML = sizeRangeHTML;
    }

    public void setPriceRangeHTML(String priceRangeHTML) {
        this.priceRangeHTML = priceRangeHTML;
    }

    @TableHeader(name = "Цена", width = -50, sequence = 500)
    public String getPriceRangeHTML() {
        return priceRangeHTML;
    }

    @TableHeader(name = "Размер", width = 50, sequence = 90)
    public String getSizeRangeHTML() {
        return sizeRangeHTML;
    }

    @TableHeader(name = "Остаток", width = -50, sequence = 550)
    public int getAmount() {
        return amount;
    }


    public void setAmount(int amount) {
        this.amount = amount;
    }
}
