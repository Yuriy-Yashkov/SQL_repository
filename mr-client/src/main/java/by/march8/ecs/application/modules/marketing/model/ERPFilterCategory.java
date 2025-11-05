package by.march8.ecs.application.modules.marketing.model;

import by.march8.api.BaseEntity;

public class ERPFilterCategory extends BaseEntity {
    private int id;
    private String name;
    private int[] category;

    public ERPFilterCategory(int id, String name, int[] category) {
        this.id = id;
        this.name = name;
        this.category = category;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int[] getCategory() {
        return category;
    }

    public void setCategory(int[] category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return name.trim();
    }
}
