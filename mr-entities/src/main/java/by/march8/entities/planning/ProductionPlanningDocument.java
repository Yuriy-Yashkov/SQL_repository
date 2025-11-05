package by.march8.entities.planning;

import by.march8.api.BaseEntity;
import by.march8.api.RedirectToEntityManager;
import by.march8.api.utils.DateUtils;
import by.march8.api.MarchDataSourceEnum;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Andy 28.11.2018 - 7:10.
 */
@Entity
@Table(name = "MOD_PLANNING_DOCUMENT")
@RedirectToEntityManager(redirectTo = MarchDataSourceEnum.DS_SQLMARCH8)
public class ProductionPlanningDocument extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private int id;
    @Column(name = "DOCUMENT_NUMBER")
    private String documentNumber;
    @Column(name = "DOCUMENT_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date documentDate;
    @Column(name = "DOCUMENT_TYPE")
    private int documentType;
    @Column(name = "STATUS")
    private int status;

    @OneToMany(mappedBy = "document", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @OrderBy(value = "useValue desc")
    private Set<ProductionPlanningComposition> compositionList = new HashSet<>();

    @OneToMany(mappedBy = "document", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @OrderBy(value = "sizePrint")
    private Set<ProductionPlanningItem> productionList = new HashSet<>();


    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    public String getDocumentNumber() {
        if (documentNumber != null) {
            return documentNumber.trim();
        } else {
            return "";
        }
    }

    public void setDocumentNumber(String documentNumber) {
        this.documentNumber = documentNumber;
    }

    public Date getDocumentDate() {
        return documentDate;
    }

    public void setDocumentDate(Date documentDate) {
        this.documentDate = documentDate;
    }

    public int getDocumentType() {
        return documentType;
    }

    public void setDocumentType(int documentType) {
        this.documentType = documentType;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Set<ProductionPlanningComposition> getCompositionList() {
        return compositionList;
    }

    public void setCompositionList(Set<ProductionPlanningComposition> compositionList) {
        this.compositionList = compositionList;
    }

    public Set<ProductionPlanningItem> getProductionList() {
        return productionList;
    }

    public void setProductionList(Set<ProductionPlanningItem> productionList) {
        this.productionList = productionList;
    }

    public String getDocumentInformation() {
        return "№ " + getDocumentNumber() + " от " + DateUtils.getNormalDateFormat(getDocumentDate()) + "г.";
    }
}
