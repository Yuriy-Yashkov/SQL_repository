package by.march8.entities.warehouse;

import by.march8.api.BaseEntity;
import by.march8.api.MarchDataSourceEnum;
import by.march8.api.RedirectToEntityManager;
import by.march8.api.TableHeader;

import javax.persistence.*;

@NamedQueries({
        @NamedQuery(name = "ERPRemainsEntity.findAll",
                query = "SELECT item FROM ERPRemainsEntity item " +
                        "ORDER BY item.modelNumber, item.articleNumber, item.grade, item.size, item.growth, item.nsiColorName"),

        @NamedQuery(name = "ERPRemainsEntity.findByGroup",
                query = "SELECT item FROM ERPRemainsEntity item WHERE " +
                        "item.category like :category " +
                        "ORDER BY item.modelNumber, item.articleNumber, item.grade, item.size, item.growth, item.nsiColorName"),

        @NamedQuery(name = "ERPRemainsEntity.findByCategory",
                query = "SELECT item FROM ERPRemainsEntity item WHERE " +
                        "item.category like :category " +
                        "ORDER BY item.modelNumber, item.articleNumber, item.grade, item.size, item.growth, item.nsiColorName"),

        @NamedQuery(name = "ERPRemainsEntity.findByCategoryAndAssortment",
                query = "SELECT item FROM ERPRemainsEntity item WHERE " +
                        "item.category like :category " +
                        "AND item.itemName = :name " +
                        "ORDER BY item.modelNumber, item.articleNumber, item.grade, item.size, item.growth, item.nsiColorName"),

        @NamedQuery(name = "ERPRemainsEntity.findByModel",
                query = "SELECT item FROM ERPRemainsEntity item WHERE " +
                        "item.modelNumber = :model " +
                        "ORDER BY item.modelNumber, item.articleNumber, item.grade, item.size, item.growth, item.nsiColorName")
})

@Entity
@Table(name="VIEW_ERP_REMAINS")
@RedirectToEntityManager(redirectTo = MarchDataSourceEnum.DS_SQLMARCH8)
public class ERPRemainsEntity extends BaseEntity {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name="ITEM_NAME")
    private String itemName ;
    @Column(name="ARTICLE_NUMBER")
    private String articleNumber ;
    @Column(name="CATEGORY")
    private String category;
    @Column(name="MODEL_NUMBER")
    private int modelNumber ;
    @Column(name="EANCODE")
    private String eancode ;
    @Column(name="NSI_COLOR_NAME")
    private String nsiColorName ;
    @Column(name="SIZE_PRINT")
    private String sizePrint ;
    @Column(name="SIZE")
    private int size ;
    @Column(name = "GROWTH")
    private int growth ;
    @Column(name="GRADE")
    private int grade ;

    @Column(name="PRICE")
    private float price ;
    @Column(name = "VAT")
    private int vat ;
    @Column(name="AMOUNT")
    private int amount ;
    @Column(name="PRODUCT_ID")
    private int productId ;
    @Column(name="ARTICLE_ID")
    private int articleId ;

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    @TableHeader(name = "Наименование",sequence = 10)
    public String getItemName() {
        if(itemName!=null) {
            return itemName.trim();
        }
        return null ;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }
    @TableHeader(name = "Артикул", width = -100, sequence = 20)
    public String getArticleNumber() {
        if(articleNumber!=null) {
            return articleNumber.trim();
        }
        return null ;
    }

    public void setArticleNumber(String articleNumber) {
        this.articleNumber = articleNumber;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
    @TableHeader(name = "Модель", width = -60, sequence = 30)
    public int getModelNumber() {
        return modelNumber;
    }

    public void setModelNumber(int modelNumber) {
        this.modelNumber = modelNumber;
    }

    public String getEancode() {
        return eancode;
    }

    public void setEancode(String eancode) {
        this.eancode = eancode;
    }

    @TableHeader(name = "Цвет", width = -120, sequence = 40)
    public String getNsiColorName() {
        if(nsiColorName!=null) {
            return nsiColorName.trim();
        }
        return null ;
    }

    public void setNsiColorName(String nsiColorName) {
        if(nsiColorName!=null && nsiColorName.trim().equals("")){
            nsiColorName = "РАЗНОЦВЕТ";
        }
        this.nsiColorName = nsiColorName;
    }

    public String getSizePrint() {
        return sizePrint;
    }

    public void setSizePrint(String sizePrint) {
        this.sizePrint = sizePrint;
    }
    @TableHeader(name = "Размер", width = -40, sequence = 100)
    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
    @TableHeader(name = "Рост", width = -40, sequence = 110)
    public int getGrowth() {
        return growth;
    }

    public void setGrowth(int growth) {
        this.growth = growth;
    }

    @TableHeader(name = "Сорт", width = -40, sequence = 120)
    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    @TableHeader(name = "Цена", width = -50, sequence = 200)
    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public int getVat() {
        return vat;
    }

    public void setVat(int vat) {
        this.vat = vat;
    }
    @TableHeader(name = "Остаток", width = -50, sequence = 500)
    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getArticleId() {
        return articleId;
    }

    public void setArticleId(int articleId) {
        this.articleId = articleId;
    }
}
