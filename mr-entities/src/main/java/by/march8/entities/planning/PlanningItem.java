package by.march8.entities.planning;

import by.march8.api.BaseEntity;
import by.march8.api.RedirectToEntityManager;
import by.march8.api.MarchDataSourceEnum;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Andy 28.11.2018 - 9:45.
 */
@Entity
@Table(name="MOD_PLANNING_DOCUMENT_DETAIL")
@RedirectToEntityManager(redirectTo = MarchDataSourceEnum.DS_SQLMARCH8)
public class PlanningItem extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="ID")
    private int id ;

    @Column(name="REF_SD_ID")
    private int productId ;

    @Column(name="REF_KLD_ID")
    private int articleId ;

    @Column(name="PLANNING_AMOUNT")
    private int amount ;

    @Column(name="REF_EQUIPMENT_ID")
    private int equipmentId ;

    @JoinColumn(name = "REF_PLANNING_DOCUMENT_ID")
    private int documentId ;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private Set<PlanningItemComponent> componentList  = new HashSet<>();

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

    public int getDocumentId() {
        return documentId;
    }

    public void setDocumentId(int documentId) {
        this.documentId = documentId;
    }

    public Set<PlanningItemComponent> getComponentList() {
        return componentList;
    }

    public void setComponentList(Set<PlanningItemComponent> componentList) {
        this.componentList = componentList;
    }
}
