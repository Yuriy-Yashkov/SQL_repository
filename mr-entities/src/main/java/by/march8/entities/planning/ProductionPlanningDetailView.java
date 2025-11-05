package by.march8.entities.planning;

import by.march8.api.BaseEntity;
import by.march8.api.RedirectToEntityManager;
import by.march8.api.TableHeader;
import by.march8.api.MarchDataSourceEnum;

import javax.persistence.*;


@NamedQueries({
        @NamedQuery(name = "ProductionPlanningDetailView.findByDocumentId",
                query = "SELECT detail FROM ProductionPlanningDetailView detail WHERE detail.documentId = :documentId ")
})

/**
 * @author Andy 27.11.2018 - 8:56.
 */
@Entity
@Table(name = "VIEW_PRODUCTION_PLANNING_DETAIL")
@RedirectToEntityManager(redirectTo = MarchDataSourceEnum.DS_SQLMARCH8)
public class ProductionPlanningDetailView extends BaseEntity {

    @Id
    @Column(name = "PRODUCT_ID")
    @TableHeader(name = "ID", width = -50, sequence = 0)
    private int id;
    @Column(name="DOCUMENT_ID")
    @TableHeader(name = "DOC_ID", width = -50, sequence = 10)
    private int documentId ;
    @Column(name="ITEM_NAME")
    private String itemName ;
    @Column(name="ITEM_ARTICLE")
    private String articleNumber ;
    @Column(name="ITEM_MODEL")
    private int modelNumber ;
    @Column(name="AMOUNT")
    private int amount ;
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

    public int getDocumentId() {
        return documentId;
    }

    public void setDocumentId(int documentId) {
        this.documentId = documentId;
    }

    @TableHeader(name = "Наименование", sequence = 20)
    public String getItemName() {
        if(itemName!=null) {
            return itemName.trim();
        }else{
            return "";
        }
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    @TableHeader(name = "Артикул", sequence = 30)
    public String getArticleNumber() {
        if(articleNumber!=null) {
            return articleNumber.trim();
        }else{
            return "" ;
        }
    }

    public void setArticleNumber(String articleNumber) {
        this.articleNumber = articleNumber;
    }

    @TableHeader(name = "Модель" ,sequence = 40)
    public int getModelNumber() {
        return modelNumber;
    }

    public void setModelNumber(int modelNumber) {
        this.modelNumber = modelNumber;
    }

    @TableHeader(name = "Количество", width = -120 ,sequence = 100, footer = true)
    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getArticleId() {
        return articleId;
    }

    public void setArticleId(int articleId) {
        this.articleId = articleId;
    }
}
