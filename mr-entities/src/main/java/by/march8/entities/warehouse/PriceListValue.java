package by.march8.entities.warehouse;

/**
 * @author Andy 25.05.2016.
 */
public class PriceListValue {
    /**
     * Стоимость по цене прейскуранта
     */
    double priceListSumCost;
    double priceListSumVat;
    double priceListSumCostAndVat;


    public double getPriceListSumCost() {
        return priceListSumCost;
    }

    public void setPriceListSumCost(final double priceListSumCost) {
        this.priceListSumCost = priceListSumCost;
    }

    public double getPriceListSumVat() {
        return priceListSumVat;
    }

    public void setPriceListSumVat(final double priceListSumVat) {
        this.priceListSumVat = priceListSumVat;
    }

    public double getPriceListSumCostAndVat() {
        return priceListSumCostAndVat;
    }

    public void setPriceListSumCostAndVat(final double priceListSumCostAndVat) {
        this.priceListSumCostAndVat = priceListSumCostAndVat;
    }
}
