package by.march8.entities.classifier;

import by.march8.api.BaseEntity;
import by.march8.api.RedirectToEntityManager;
import by.march8.api.TableHeader;
import by.march8.api.MarchDataSourceEnum;

import javax.persistence.*;

/**
 * @author Andy 19.11.2018 - 11:09.
 */

@NamedQueries({
        @NamedQuery(name = "CompositionMaterialView.findByProductId",
                query = "SELECT item FROM CompositionMaterialView item WHERE item.productId = :productId ")
})

@Entity
@Table(name="VIEW_NSI_COMPOSITION_MATERIAL")
@RedirectToEntityManager(redirectTo = MarchDataSourceEnum.DS_SQLMARCH8)
public class CompositionMaterialView extends BaseEntity {

    @Id
    @Column(name = "ID")
    private int id ;

    @Column(name="ITEM_ID")
    private int productId ;

    @Column(name="MATERIAL_NAME")
    private String materialName ;

    @Column(name="MATERIAL_ID")
    private int materialId;

    @Column(name="MATERIAL_TYPE")
    private int materialTypeId;

    @Column(name="MATERIAL_TYPE_NAME")
    private String materialTypeName ;

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

    @TableHeader(name="Наименование материала", sequence = 20)
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

    public int getMaterialId() {
        return materialId;
    }

    public void setMaterialId(int materialId) {
        this.materialId = materialId;
    }

    public int getMaterialTypeId() {
        return materialTypeId;
    }

    public void setMaterialTypeId(int materialTypeId) {
        this.materialTypeId = materialTypeId;
    }

    @TableHeader(name="Тип материала",width = -220, sequence = 10)
    public String getMaterialTypeName()
    {
        if(materialTypeName!=null) {
            return materialTypeName.trim();
        }else{
            return "";
        }
    }

    public void setMaterialTypeName(String materialTypeName) {
        this.materialTypeName = materialTypeName;
    }

    @Override
    public String toString() {
        return getMaterialName()+"("+getMaterialTypeName()+")";
    }


}
