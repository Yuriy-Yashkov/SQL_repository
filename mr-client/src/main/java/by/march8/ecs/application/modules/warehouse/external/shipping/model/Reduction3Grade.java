package by.march8.ecs.application.modules.warehouse.external.shipping.model;

import by.march8.api.BaseEntity;

/**
 * @author Andy
 */
public class Reduction3Grade extends BaseEntity {

    private int id;
    private double price;
    private double priceCurrency;

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getPriceCurrency() {
        return priceCurrency;
    }

    public void setPriceCurrency(final double priceCurrency) {
        this.priceCurrency = priceCurrency;
    }
}
