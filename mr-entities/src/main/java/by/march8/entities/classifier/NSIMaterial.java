package by.march8.entities.classifier;

import by.march8.api.BaseEntity;
import by.march8.api.RedirectToEntityManager;
import by.march8.api.MarchDataSourceEnum;

import javax.persistence.*;

/**
 * @author Andy 19.11.2018 - 10:14.
 */

@NamedQueries({
        @NamedQuery(name = "NSIMaterial.findByProductId",
                query = "SELECT item FROM NSIMaterial item WHERE item.productId = :productId ")
})

@Entity
@Table(name = "VIEW_NSI_COMPOSITION_BY_SD")
@RedirectToEntityManager(redirectTo = MarchDataSourceEnum.DS_SQLMARCH8)
public class NSIMaterial extends BaseEntity{

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id ;

    @Column(name = "REF_NSI_SD_ID")
    private int productId;

    @Column(name="REF_MATERIAL_ID")
    private int materialId ;

    @Column(name="REF_MATERIAL_TYPE")
    private int materialType ;

    @Column(name = "RATE")
    private double rate ;

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

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }
}
