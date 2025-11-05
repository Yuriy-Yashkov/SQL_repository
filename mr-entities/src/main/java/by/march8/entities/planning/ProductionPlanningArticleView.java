package by.march8.entities.planning;

import by.march8.api.BaseEntity;
import by.march8.api.RedirectToEntityManager;
import by.march8.api.TableHeader;
import by.march8.api.MarchDataSourceEnum;

import javax.persistence.*;
import java.util.List;

@NamedQueries({
        @NamedQuery(name = "ProductionPlanningArticleView.findByDocumentIdAndArticleId",
                query = "SELECT detail FROM ProductionPlanningArticleView detail WHERE detail.documentId = :documentId AND detail.articleId = :articleId " +
                        "ORDER BY detail.size, detail.growth ASC")
})

/**
 * @author Andy 27.11.2018 - 11:12.
 */
@Entity
@Table(name = "VIEW_PRODUCTION_PLANNING_ARTICLE")
@RedirectToEntityManager(redirectTo = MarchDataSourceEnum.DS_SQLMARCH8)
public class ProductionPlanningArticleView extends BaseEntity {

    @Id
    @Column(name = "ID")
    //@TableHeader(name = "ID", width = -50, sequence = 199)
    private int id;

    @Column(name = "REF_PLANNING_DOCUMENT_ID")
    //@TableHeader(name = "DOCID", width = -50, sequence = 200)
    private int documentId;

    @Column(name = "REF_SD_ID")
    //@TableHeader(name = "SDID", width = -50, sequence = 300)
    private int productId;

    @Column(name = "REF_KLD_ID")
    //@TableHeader(name = "KLDID", width = -50, sequence = 400)
    private int articleId;

    @Column(name = "SIZE")
    @TableHeader(name = "Размер", sequence = 10)
    private int size;

    @Column(name = "GROWTH")
    @TableHeader(name = "Рост", sequence = 20)
    private int growth;

    @Column(name = "PLANNING_PERCENT")
    @TableHeader(name = "Выпуск_%", width = -50, sequence = 25, footer = true)
    private Double percent;

    @Column(name = "PLANNING_AMOUNT")
    @TableHeader(name = "Выпуск_Кол-во", width = -50, sequence = 30, footer = true)
    private int amount;

    @Column(name = "PERFORMANCE")
    @TableHeader(name = "Оборудование_Произв.", width = -50, sequence = 40)
    private double performance;

    @Column(name = "EQUIPMENT_COUNT")
    @TableHeader(name = "Оборудование_Кол-во", width = -50, sequence = 50)
    private double equipmentCount;

    @Column(name = "EQUIPMENT_NAME")
    @TableHeader(name = "Оборудование_Наименование", sequence = 60)
    private String equipmentName;

    @Column(name = "REF_EQUIPMENT_ID")
   // @TableHeader(name = "Оборудование_ID", width = -50, sequence = 70)
    private int equipmentId;

    @Transient
    private List<ProductionPlanningDetailComponentView> componentList;

    @Transient
    private boolean changed;

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

    public double getPercent() {
        return percent;
    }

    public void setPercent(double percent) {
        this.percent = percent;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public double getPerformance() {
        return performance;
    }

    public void setPerformance(double performance) {
        this.performance = performance;
    }

    public double getEquipmentCount() {
        return equipmentCount;
    }

    public void setEquipmentCount(double equipmentCount) {
        this.equipmentCount = equipmentCount;
    }

    public String getEquipmentName() {
        return equipmentName;
    }

    public void setEquipmentName(String equipmentName) {
        this.equipmentName = equipmentName;
    }

    public int getEquipmentId() {
        return equipmentId;
    }

    public void setEquipmentId(int equipmentId) {
        this.equipmentId = equipmentId;
    }

    public boolean isChanged() {
        return changed;
    }

    public void setChanged(boolean changed) {
        this.changed = changed;
    }

    public void setPercent(Double percent) {
        this.percent = percent;
    }

    public List<ProductionPlanningDetailComponentView> getComponentList() {
        return componentList;
    }

    public void setComponentList(List<ProductionPlanningDetailComponentView> componentList) {
        this.componentList = componentList;
    }
}


