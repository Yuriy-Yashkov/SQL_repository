package by.march8.entities.planning;

import by.march8.api.BaseEntity;
import by.march8.api.RedirectToEntityManager;
import by.march8.api.MarchDataSourceEnum;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@NamedQueries({
        @NamedQuery(name = "ProductionPlanningItem.findById",
                query = "SELECT item FROM ProductionPlanningItem item WHERE item.id = :itemId")
})

/**
 * @author Andy 28.11.2018 - 8:54.
 */
@Entity
@Table(name = "MOD_PLANNING_DOCUMENT_DETAIL")
@RedirectToEntityManager(redirectTo = MarchDataSourceEnum.DS_SQLMARCH8)
public class ProductionPlanningItem extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private int id;

    @Column(name = "REF_SD_ID")
    private int productId;

    @Column(name = "REF_KLD_ID")
    private int articleId;

    @Column(name="SIZE_PRINT")
    private String sizePrint ;

    @Column(name = "PLANNING_AMOUNT")
    private int amount;

    @Column(name = "REF_EQUIPMENT_ID")
    private int equipmentId;

    @ManyToOne()
    @JoinColumn(name = "REF_PLANNING_DOCUMENT_ID")
    private ProductionPlanningDocument document;

    @Column(name = "PERFORMANCE")
    private double performance;

    @Column(name = "EQUIPMENT_COUNT")
    private double equipmentCount;

    @Column(name = "PLANNING_PERCENT")
    private double percent;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private Set<PlanningItemComponent> componentList = new HashSet<>();

    @Transient
    private boolean using;

    @Transient
    private String itemName;
    @Transient
    private String articleNumber;
    @Transient
    private int modelNumber;
    @Transient
    private String equipmentName;


    public PlanningItemComponent getComponentValue(ProductionPlanningComposition component) {
        for (PlanningItemComponent item : componentList) {
            if (component.getId() == item.getComponent().getId()) {
                return item;
            }
        }

        return null;
    }


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

    public int getArticleId() {
        return articleId;
    }

    public void setArticleId(int articleId) {
        this.articleId = articleId;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getEquipmentId() {
        return equipmentId;
    }

    public void setEquipmentId(int equipmentId) {
        this.equipmentId = equipmentId;
    }

    public ProductionPlanningDocument getDocument() {
        return document;
    }

    public void setDocument(ProductionPlanningDocument document) {
        this.document = document;
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

    public double getPercent() {
        return percent;
    }

    public void setPercent(double percent) {
        this.percent = percent;
    }

    public Set<PlanningItemComponent> getComponentList() {
        return componentList;
    }

    public void setComponentList(Set<PlanningItemComponent> componentList) {
        this.componentList = componentList;
    }

    public boolean isUsing() {
        return using;
    }

    public void setUsing(boolean using) {
        this.using = using;
    }

    public String getItemName() {
        if (itemName != null) {
            return itemName.trim();
        } else {
            return "";
        }
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getArticleNumber() {
        if (articleNumber != null) {
            return articleNumber.trim();
        } else {
            return "";
        }
    }

    public void setArticleNumber(String articleNumber) {
        this.articleNumber = articleNumber;
    }

    public int getModelNumber() {
        return modelNumber;
    }

    public void setModelNumber(int modelNumber) {
        this.modelNumber = modelNumber;
    }

    public String getSizePrint() {
        if(sizePrint!=null) {
            return sizePrint.trim();
        }else{
            return "" ;
        }
    }

    public void setSizePrint(String sizePrint) {
        this.sizePrint = sizePrint;
    }

    public String getEquipmentName() {
        return equipmentName;
    }

    public void setEquipmentName(String equipmentName) {
        this.equipmentName = equipmentName;
    }
}
