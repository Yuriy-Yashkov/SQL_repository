package by.march8.entities.classifier;

import by.march8.api.BaseEntity;
import by.march8.api.MarchDataSourceEnum;
import by.march8.api.RedirectToEntityManager;
import by.march8.api.TableHeader;

import javax.persistence.*;

@NamedQueries({
        @NamedQuery(name = "EANCodeItem.likeByEanCode",
                query = "SELECT item FROM EANCodeItem item WHERE item.eanCode like :eanCode  " +
                        "order by " +
                        "item.eanCode"),
})

@Entity
@Table(name = "V_NSI_EAN_CLASSIFIER")
@RedirectToEntityManager(redirectTo = MarchDataSourceEnum.DS_SQLMARCH8)
public class EANCodeItem extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private int id;
    @Column(name = "ITEM_CODE")
    private int productId;
    @Column(name = "EANCODE")
    private String eanCode;
    @Column(name = "REF_COLOR_ID")
    private int colorId;
    @Column(name = "COLOR_NAME")
    private String colorName;
    @Column(name = "ITEM_NAME")
    private String itemName;
    @Column(name = "MODEL")
    private int model;
    @Column(name = "ARTICLE_NUMBER")
    private String articleName;
    @Column(name = "GRADE")
    private Integer grade;
    @Column(name = "SIZE")
    private int size;
    @Column(name = "GROWTH")
    private int growth;
    @Column(name = "SIZE_PRINT")
    private String sizePrint;
    @Column(name = "PRICE")
    private double price;
    @Column(name = "VAT")
    private double vat;

    @Override
    @TableHeader(name = "ID", width = 0, sequence = 999)
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

    @TableHeader(name = "Цвет_EAN код", sequence = 49)
    public String getEanCode() {
        return eanCode;
    }

    public void setEanCode(String eanCode) {
        this.eanCode = eanCode;
    }

    @TableHeader(name = "Цвет_Код", sequence = 45)
    public int getColorId() {
        return colorId;
    }

    public void setColorId(int colorId) {
        this.colorId = colorId;
    }

    @TableHeader(name = "Цвет_Наименование", sequence = 40)
    public String getColorName() {
        if (colorName == null) {
            return "";
        }
        return colorName.trim();
    }

    public void setColorName(String colorName) {
        this.colorName = colorName;
    }

    @TableHeader(name = "Наименование", sequence = 10)
    public String getItemName() {
        if (itemName == null) {
            return "";
        }
        return itemName.trim();
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    @TableHeader(name = "Модель", sequence = 20)
    public int getModel() {
        return model;
    }

    public void setModel(int model) {
        this.model = model;
    }

    @TableHeader(name = "Артикул", sequence = 30)
    public String getArticleName() {
        if (articleName == null) {
            return "";
        }
        return articleName.trim();
    }

    public void setArticleName(String articleName) {
        this.articleName = articleName;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
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
        return sizePrint;
    }

    public void setSizePrint(String sizePrint) {
        this.sizePrint = sizePrint;
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
}
