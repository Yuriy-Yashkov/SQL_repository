package by.march8.entities.classifier;

import by.march8.api.BaseEntity;
import by.march8.api.RedirectToEntityManager;
import by.march8.api.MarchDataSourceEnum;

import javax.persistence.*;

/**
 * @author Andy 19.11.2018 - 9:39.
 */
@Entity
@Table(name="NSI_COMPOSITION_MATERIAL")
@RedirectToEntityManager(redirectTo = MarchDataSourceEnum.DS_SQLMARCH8)
public class CompositionMaterial extends BaseEntity {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id ;

    @Column(name="REF_KLD_ID")
    private int productId ;

    @Column(name="REF_MATERIAL_ID")
    private int materialId;

    @Column(name="REF_MATERIAL_TYPE")
    private int materialType ;

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

    public int getMaterialType() {
        return materialType;
    }

    public void setMaterialType(int materialType) {
        this.materialType = materialType;
    }

    public int getMaterialId() {
        return materialId;
    }

    public void setMaterialId(int materialId) {
        this.materialId = materialId;
    }

    @Override
    public String toString() {
        return "CompositionMaterial{" +
                "id=" + id +
                ", productId=" + productId +
                ", materialId=" + materialId +
                ", materialType=" + materialType +
                '}';
    }
}
