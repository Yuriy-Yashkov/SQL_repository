package by.march8.ecs.application.modules.planning.model;

import by.march8.api.BaseEntity;

/**
 * @author Andy 22.11.2018 - 13:28.
 */

public class ProductionEquipment extends BaseEntity {

    private int id;
    private int productId;
    private int equipmentId;
    private String equipmentName;
    private double performance;


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

    public int getEquipmentId() {
        return equipmentId;
    }

    public void setEquipmentId(int equipmentId) {
        this.equipmentId = equipmentId;
    }

    public String getEquipmentName() {
        if (equipmentName != null) {
            return equipmentName.trim();
        } else {
            return "";
        }
    }

    public void setEquipmentName(String equipmentName) {
        this.equipmentName = equipmentName;
    }

    public double getPerformance() {
        return performance;
    }

    public void setPerformance(double performance) {
        this.performance = performance;
    }
}
