package by.march8.entities.planning;

import by.march8.api.BaseEntity;
import by.march8.api.RedirectToEntityManager;
import by.march8.api.MarchDataSourceEnum;

import javax.persistence.*;

/**
 * @author Andy 28.11.2018 - 7:17.
 */
@Entity
@Table(name="MOD_PLANNING_COMPOSITION")
@RedirectToEntityManager(redirectTo = MarchDataSourceEnum.DS_SQLMARCH8)
public class ProductionPlanningComposition extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="ID")
    @PrimaryKeyJoinColumn
    private int id ;

    @ManyToOne()
    @JoinColumn(name = "REF_PLANNING_DOCUMENT_ID")
    private ProductionPlanningDocument document ;

    @Column(name="REF_MATERIAL_ID")
    private int materialId;

    @Column(name="REF_MATERIAL_TYPE")
    private int materialType ;

    @Column(name="USE_VALUE")
    private double useValue ;

    @Column(name="MATERIAL_NAME")
    private String materialName ;

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    public ProductionPlanningDocument getDocument() {
        return document;
    }

    public void setDocument(ProductionPlanningDocument document) {
        this.document = document;
    }

    public int getMaterialId() {
        return materialId;
    }

    public void setMaterialId(int materialId) {
        this.materialId = materialId;
    }

    public int getMaterialType() {
        return materialType;
    }

    public void setMaterialType(int materialType) {
        this.materialType = materialType;
    }

    public double getUseValue() {
        return useValue;
    }

    public void setUseValue(double useValue) {
        this.useValue = useValue;
    }

    public String getMaterialName() {
        if(materialName!=null) {
            return materialName.trim();
        }else{
            return "";
        }
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }
}
