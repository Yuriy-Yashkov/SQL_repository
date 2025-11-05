package by.march8.ecs.application.modules.marketing.model;

/**
 * @author Andy 01.11.2017.
 * Класс размерной шкалы изделия
 */
public class ScaleItemPreliminary {

    private int size;
    private double price;

    public ScaleItemPreliminary(final int size, final double price) {
        this.size = size;
        this.price = price;
    }

    public int getSize() {
        return size;
    }

    public void setSize(final int size) {
        this.size = size;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(final double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "ScaleItem{" +
                "size=" + size +
                ", price=" + price +
                '}';
    }
}
