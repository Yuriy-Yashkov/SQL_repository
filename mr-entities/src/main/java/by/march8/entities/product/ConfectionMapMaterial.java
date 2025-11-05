package by.march8.entities.product;

import by.march8.api.BaseEntity;
import by.march8.api.RedirectToEntityManager;
import by.march8.api.TableHeader;
import by.march8.api.MarchDataSourceEnum;

import javax.persistence.*;

/**
 * @author  by lidashka.
 */


@Entity
@Table(name = "MOD_CONFECTION_MAP_ITEM")
@RedirectToEntityManager(redirectTo = MarchDataSourceEnum.DS_SQLSERVER)

public class ConfectionMapMaterial extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "ORDINAL_NUMBER")
    @TableHeader(name = "№", width = 30, sequence = 2)
    private int number;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MOD_CONFECTION_MAP_ID")
    private ConfectionMap confectionMap;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MOD_MATERIAL_ID")
    private ModelMaterial modelMaterial;

    @TableHeader(name = "Прикладные", width = 50, sequence = 0)
    public String getMaterialName() {
        return (modelMaterial!=null)?modelMaterial.getName():null;
    }

    @Column(name = "QUANTITY")
    @TableHeader(name = "Кол-во", width = 30, sequence = 2)
    private float quantity;

    @TableHeader(name = "Ед. изм.", width = 50, sequence = 0)
    public String getMaterialUnit() {
        return (modelMaterial!=null)?modelMaterial.getUnitAbbreviation():null;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public ConfectionMap getConfectionMap() {
        return confectionMap;
    }

    public void setConfectionMap(ConfectionMap confectionMap) {
        this.confectionMap = confectionMap;
    }

    public ModelMaterial getModelMaterial() {
        return modelMaterial;
    }

    public void setModelMaterial(ModelMaterial modelMaterial) {
        this.modelMaterial = modelMaterial;
    }

    public float getQuantity() {
        return quantity;
    }

    public void setQuantity(float quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return  modelMaterial +" кол-во =" + quantity ;
    }
}
