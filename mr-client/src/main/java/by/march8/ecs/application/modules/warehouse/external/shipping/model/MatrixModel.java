package by.march8.ecs.application.modules.warehouse.external.shipping.model;

import by.march8.api.TableHeader;

public class MatrixModel {
    private int id;
    private int modelNumber;
    private String name;

    @TableHeader(name = "п/п", width = -30, sequence = 1)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @TableHeader(name = "Модель", width = -50, sequence = 10)
    public int getModelNumber() {
        return modelNumber;
    }

    public void setModelNumber(int modelNumber) {
        this.modelNumber = modelNumber;
    }

    @TableHeader(name = "Наименование", sequence = 100)
    public String getName() {
        if (name != null) {
            return name.trim();
        }

        return null;
    }

    public void setName(String name) {
        this.name = name;
    }
}
