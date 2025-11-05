package by.march8.entities.sales;

import by.march8.api.BaseEntity;
import by.march8.api.RedirectToEntityManager;
import by.march8.api.MarchDataSourceEnum;

import javax.persistence.*;

@NamedQueries({
        @NamedQuery(name = "PreOrderProductItem.find1stGradeByArticleId",
                query = "SELECT item FROM PreOrderProductItem item WHERE item.articleId = :article AND item.grade = 1 ORDER BY item.size, item.grade")
})

/**
 * @author Andy 14.01.2019 - 7:39.
 */
@Entity
@Table(name = "V_PREORDER_PRODUCT_ITEM")
@RedirectToEntityManager(redirectTo = MarchDataSourceEnum.DS_SQLMARCH8)
public class PreOrderProductItem extends BaseEntity {

    @Id
    @Column(name = "PRODUCT_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "ARTICLE_ID")
    private int articleId;

    @Column(name = "ITEM_NAME")
    private String name;

    @Column(name = "ARTICLE_NAME")
    private String articleName;

    @Column(name = "MODEL_NUMBER")
    private int modelNumber;

    @Column(name = "ITEM_TNVED")
    private String codeTNVED;

    @Column(name = "PRINT_SIZE")
    private String printSize;

    @Column(name = "ITEM_GRADE")
    private int grade;

    @Column(name = "ITEM_SIZE")
    private int size;

    @Column(name = "ITEM_GROWTH")
    private int growth;

    @Column(name = "PRICE")
    private double price;

    @Column(name = "VAT")
    private double vat;


    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    public int getArticleId() {
        return articleId;
    }

    public void setArticleId(int articleId) {
        this.articleId = articleId;
    }

    public String getName() {
        if(name!=null) {
            return name.trim();
        }else{
            return "";
        }
    }


    public void setName(String name) {
        this.name = name;
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

    public int getModelNumber() {
        return modelNumber;
    }

    public void setModelNumber(int modelNumber) {
        this.modelNumber = modelNumber;
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

    public String getPrintSize() {
        if(printSize!=null) {
            return printSize.trim();
        }else{
            return "";
        }
    }

    public void setPrintSize(String printSize) {
        this.printSize = printSize;
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
