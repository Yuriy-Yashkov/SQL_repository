package by.march8.ecs.application.modules.marketing.model;

import by.march8.api.TableHeader;

/**
 * @author Andy 01.11.2017.
 */
public class ScaleItem {

    private int minSize;
    private int maxSize;
    private boolean saveable = true;

    private int id;

    @TableHeader(name = "Действующая цена", width = -100, sequence = 50)
    private float price;
    @TableHeader(name = "Себестоимость", width = -100, sequence = 30)
    private float primeCost;
    @TableHeader(name = "Рентабельность", width = -100, sequence = 40)
    private float profitability;

    public ScaleItem(final int minSize, final int maxSize, final float price) {
        this.minSize = minSize;
        this.maxSize = maxSize;
        this.price = price;
    }

    public int getMinSize() {
        return minSize;
    }

    public void setMinSize(final int minSize) {
        this.minSize = minSize;
    }

    public int getMaxSize() {
        return maxSize;
    }

    public void setMaxSize(final int maxSize) {
        this.maxSize = maxSize;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(final float price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return getSizeScale();
    }

    @TableHeader(name = "Размерная шкала", sequence = 1)
    public String getSizeScale() {
        if (minSize == maxSize) {
            return String.valueOf(minSize);
        } else {
            return minSize + "-" + maxSize;
        }
    }

    public float getPrimeCost() {
        return primeCost;
    }

    public void setPrimeCost(final float primeCost) {
        this.primeCost = primeCost;
    }

    public float getProfitability() {
        return profitability;
    }

    public void setProfitability(final float profitability) {
        this.profitability = profitability;
    }

    public int getId() {
        return id;
    }

    public void setId(final int id) {
        this.id = id;
    }

    public boolean isSaveable() {
        return saveable;
    }

    public void setSaveable(final boolean saveable) {
        this.saveable = saveable;
    }

}
