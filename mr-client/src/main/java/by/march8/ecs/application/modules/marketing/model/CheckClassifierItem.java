package by.march8.ecs.application.modules.marketing.model;

import by.march8.api.BaseEntity;
import by.march8.api.TableHeader;

/**
 * @author Andy 21.01.2019 - 11:36.
 */
public class CheckClassifierItem extends BaseEntity {
    public int id;
    public String name;
    public int size;
    public int growth;

    public CheckClassifierItem(int id, String name, int size, int growth) {
        this.id = id;
        this.name = name;
        this.size = size;
        this.growth = growth;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @TableHeader(name = "Размеры", sequence = 0)
    public String getName() {
        if (name != null) {
            return name.trim();
        }
        return null;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name.trim();
    }

    @TableHeader(name = "Размер", sequence = 1)
    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    @TableHeader(name = "Рост", sequence = 2)
    public int getGrowth() {
        return growth;
    }

    public void setGrowth(int growth) {
        this.growth = growth;
    }
}