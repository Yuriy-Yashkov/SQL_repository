package by.march8.ecs.application.modules.warehouse.internal.storage.model;

import by.march8.entities.storage.BalanceItem;

public class BalanceSize {

    private int minSize;
    private int maxSize;
    private int minGrowth;
    private int maxGrowth;

    private int amount;
    private double price;
    private double cost;

    private int amountAccess;
    private double priceAccess;
    private double costAccess;

    private boolean used = false;

    public BalanceSize(BalanceItem item) {
        amount = item.getAmount();
        price = item.getPrice();
        if (item.isAccessTypeData()) {
            minSize = item.getMinSize();
            maxSize = item.getMaxSize();
            minGrowth = item.getMinGrowth();
            maxGrowth = item.getMaxGrowth();
            cost = item.getCost();
        } else {
            cost = amount * price;
            minSize = item.getSize();
            maxSize = item.getSize();
            minGrowth = item.getGrowth();
            maxGrowth = item.getGrowth();
        }
    }

    public int getMinSize() {
        return minSize;
    }

    public void setMinSize(int minSize) {
        this.minSize = minSize;
    }

    public int getMaxSize() {
        return maxSize;
    }

    public void setMaxSize(int maxSize) {
        this.maxSize = maxSize;
    }

    public int getMinGrowth() {
        return minGrowth;
    }

    public void setMinGrowth(int minGrowth) {
        this.minGrowth = minGrowth;
    }

    public int getMaxGrowth() {
        return maxGrowth;
    }

    public void setMaxGrowth(int maxGrowth) {
        this.maxGrowth = maxGrowth;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public String getSizeRange() {
        return minSize + "-" + maxSize;
    }

    public String getGrowthRange() {
        return minGrowth + "-" + maxGrowth;
    }

    public void attach(BalanceItem item) {
        // ПРоверка пределов для размера
        // Если значение меньше минимума то принять как минималный
        if (!item.isAccessTypeData()) {
            if (item.getSize() < minSize) {
                minSize = item.getSize();
            }

            if (item.getSize() > maxSize) {
                maxSize = item.getSize();
            }

            if (item.getGrowth() < minGrowth) {
                minGrowth = item.getGrowth();
            }

            if (item.getGrowth() > maxGrowth) {
                maxGrowth = item.getGrowth();
            }

            amount += item.getAmount();
            cost += item.getAmount() * item.getPrice();
        }
    }

    @Override
    public String toString() {
        return getSizeRange() + "\t" + getGrowthRange() + "\t" + getPrice() + "\t" + getAmount() + "\t" + getCost();
    }

    public int getAmountAccess() {
        return amountAccess;
    }

    public void setAmountAccess(int amountAccess) {
        this.amountAccess = amountAccess;
    }

    public double getPriceAccess() {
        return priceAccess;
    }

    public void setPriceAccess(double priceAccess) {
        this.priceAccess = priceAccess;
    }

    public double getCostAccess() {
        return costAccess;
    }

    public void setCostAccess(double costAccess) {
        this.costAccess = costAccess;
    }

    public boolean isUsed() {
        return used;
    }

    public void setUsed(boolean used) {
        this.used = used;
    }
}
