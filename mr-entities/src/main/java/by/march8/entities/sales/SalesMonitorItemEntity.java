package by.march8.entities.sales;

import by.march8.api.BaseEntity;
import by.march8.api.TableHeader;


public class SalesMonitorItemEntity extends BaseEntity {

    private int id ;
    private int size;
    private int growth ;
    private int amount ;

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    @TableHeader(name = "Размер", sequence = 10)
    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    @TableHeader(name = "Рост", sequence = 20)
    public int getGrowth() {
        return growth;
    }

    public void setGrowth(int growth) {
        this.growth = growth;
    }

    @TableHeader(name = "Количество", sequence = 0)
    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
