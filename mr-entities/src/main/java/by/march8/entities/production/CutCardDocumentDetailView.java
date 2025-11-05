package by.march8.entities.production;

import by.march8.api.BaseEntity;
import by.march8.api.MarchDataSourceEnum;
import by.march8.api.RedirectToEntityManager;
import by.march8.api.TableHeader;

import javax.persistence.*;

/**
 * @author Developer on 04.12.2019 9:56
 */

@NamedQueries({
        @NamedQuery(name = "CutCardDocumentDetailView.findByDocumentId",
                query = "SELECT detail FROM CutCardDocumentDetailView detail WHERE detail.documentId = :document ")
})

@Entity
@Table(name = "VIEW_CUTCARD_DETAIL")
@RedirectToEntityManager(redirectTo = MarchDataSourceEnum.DS_SQLMARCH8)
public class CutCardDocumentDetailView extends BaseEntity {

    @Id
    @Column(name="ID")
    private int id ;

    @Column(name="ARTICLE_NUMBER")
    private String articleNumber ;
    @Column(name = "ARTICLE_CODE")
    private String articleCode ;
    @Column(name = "ARTICLE_ID")
    private int articleId ;
    @Column(name = "MODEL_NUMBER")
    private int modelNumber ;
    @Column(name = "ITEM_NAME")
    private String itemName ;
    @Column(name="ITEM_SIZE")
    private int itemSize ;
    @Column(name = "ITEM_GROWTH")
    private int itemGrowth ;
    @Column(name="COLOR_CODE")
    private int colorCode ;
    @Column(name="COLOR_NAME")
    private String colorName ;

    @Column(name="AMOUNT")
    private int amount ;
    @Column(name="AMOUNT_ROUTE")
    private int amountRoute ;

    @Column(name="RECUT")
    private int recut ;

    @Column(name = "DOCUMENT_ID")
    private int documentId ;

    @Override
    @TableHeader(name = "ID", sequence = 999)
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    @TableHeader(name = "Артикул", sequence = 10)
    public String getArticleNumber() {
        return articleNumber;
    }

    public void setArticleNumber(String articleNumber) {
        this.articleNumber = articleNumber;
    }

    @TableHeader(name = "Шифр арт.", sequence = 20)
    public String getArticleCode() {
        return articleCode;
    }

    public void setArticleCode(String articleCode) {
        this.articleCode = articleCode;
    }

    @TableHeader(name = "Код изделия", sequence = 110)
    public int getArticleId() {
        return articleId;
    }

    public void setArticleId(int articleId) {
        this.articleId = articleId;
    }

    @TableHeader(name = "Модель", sequence = 30)
    public int getModelNumber() {
        return modelNumber;
    }

    public void setModelNumber(int modelNumber) {
        this.modelNumber = modelNumber;
    }

    @TableHeader(name = "Изделие", sequence = 70)
    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }
    @TableHeader(name = "Размер", sequence = 40)
    public int getItemSize() {
        return itemSize;
    }

    public void setItemSize(int itemSize) {
        this.itemSize = itemSize;
    }

    @TableHeader(name = "Рост", sequence = 50)
    public int getItemGrowth() {
        return itemGrowth;
    }

    public void setItemGrowth(int itemGrowth) {
        this.itemGrowth = itemGrowth;
    }

    public int getColorCode() {
        return colorCode;
    }

    public void setColorCode(int colorCode) {
        this.colorCode = colorCode;
    }

    @TableHeader(name = "Цвет", sequence = 60)
    public String getColorName() {
        return colorName;
    }

    public void setColorName(String colorName) {
        this.colorName = colorName;
    }

    @TableHeader(name = "Кол-во", sequence = 80)
    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    @TableHeader(name = "Закрыто", sequence = 90)
    public int getAmountRoute() {
        return amountRoute;
    }

    public void setAmountRoute(int amountRoute) {
        this.amountRoute = amountRoute;
    }

    @TableHeader(name = "Перекрой", sequence = 100)
    public int getRecut() {
        return recut;
    }

    public void setRecut(int recut) {
        this.recut = recut;
    }

    public int getDocumentId() {
        return documentId;
    }

    public void setDocumentId(int documentId) {
        this.documentId = documentId;
    }
}
