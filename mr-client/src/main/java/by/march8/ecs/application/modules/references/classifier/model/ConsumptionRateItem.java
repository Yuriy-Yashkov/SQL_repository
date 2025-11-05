package by.march8.ecs.application.modules.references.classifier.model;

import by.march8.api.BaseEntity;
import by.march8.api.TableHeader;

import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

@NamedQueries({
        @NamedQuery(name = "ConsumptionRateItem.deleteByProductAndComponentIds",
                query = "DELETE FROM ConsumptionRateItem item  " +
                        " where item. " +
                        " order by item.documentDate, item.itemSize" +
                        "  ")
})

/**
 * @author Andy 20.11.2018 - 7:49.
 */
public class ConsumptionRateItem extends BaseEntity {

    @TableHeader(name = "ID", width = -30, sequence = 80)
    private int id;

    @TableHeader(name = "Материал", sequence = 20)
    private String material;

    @TableHeader(name = "+", width = -30, sequence = 10)
    private Boolean selected;

    @TableHeader(name = "Расход", width = -70, sequence = 40)
    private double rate;

    //@TableHeader(name = "IID", sequence = 50)
    private int productId;

    //@TableHeader(name = "MID", sequence = 60)
    private int materialId;

    //@TableHeader(name = "RID", sequence = 70)
    private int rateId;

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public boolean isSelected() {
        return selected;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public Boolean getSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public void setSelected(Boolean selected) {
        this.selected = selected;
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

    public int getRateId() {
        return rateId;
    }

    public void setRateId(int rateId) {
        this.rateId = rateId;
    }
}
