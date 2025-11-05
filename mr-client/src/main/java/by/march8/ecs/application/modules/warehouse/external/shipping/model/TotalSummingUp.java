package by.march8.ecs.application.modules.warehouse.external.shipping.model;

/**
 * Образ итогов по документу в рублях и в валюте
 * @author Andy 25.01.2016.
 */
public class TotalSummingUp {

    /**
     * Стоимость в прейскуранте
     */
    double priceListSumCost;
    /**
     * Сумма НДС
     */
    double priceListSumVat;
    /**
     * Итого с НДС
     */
    double priceListSumCostAndVat;
    /**
     * Сумма по документу без НДС
     */
    private double valueSumCost;
    /**
     * Сумма НДС
     */
    private double valueSumVat;
    /**
     * Сумма по документу с НДС
     */
    private double valueSumCostAndVat;
    /**
     * Сумма по документу без НДС, валюта
     */
    private double valueSumCostCurrency;
    /**
     * Сумма НДС, валюта
     */
    private double valueSumVatCurrency;
    /**
     * Сумма по документу с НДС, валюта
     */
    private double valueSumCostAndVatCurrency;
    /**
     * Сумма надбавок для всех изделий(3)
     */
    private double valueSumAllowance;
    /**
     * 3.1
     */
    private double valueCostAndAllowance;
    /**
     * Сумма НДС на розничную цену изделий(6)
     */
    private double valueSumVatRetail;
    /**
     * Сумма розничной цены изделий(7)
     */
    private double valueSumCostRetail;
    private double amount;

    private double weight;

    public double getAmount() {
        return amount;
    }

    public void setAmount(final double amount) {
        this.amount = amount;
    }

    public double getValueSumCost() {
        return valueSumCost;
    }

    public void setValueSumCost(final double valueSumCost) {
        this.valueSumCost = valueSumCost;
    }

    public double getValueSumVat() {
        return valueSumVat;
    }

    public void setValueSumVat(final double valueSumVat) {
        this.valueSumVat = valueSumVat;
    }

    public double getValueSumCostAndVat() {
        return valueSumCostAndVat;
    }

    public void setValueSumCostAndVat(final double valueSumCostAndVat) {
        this.valueSumCostAndVat = valueSumCostAndVat;
    }

    public double getValueSumCostCurrency() {
        return valueSumCostCurrency;
    }

    public void setValueSumCostCurrency(final double valueSumCostCurrency) {
        this.valueSumCostCurrency = valueSumCostCurrency;
    }

    public double getValueSumVatCurrency() {
        return valueSumVatCurrency;
    }

    public void setValueSumVatCurrency(final double valueSumVatCurrency) {
        this.valueSumVatCurrency = valueSumVatCurrency;
    }

    public double getValueSumCostAndVatCurrency() {
        return valueSumCostAndVatCurrency;
    }

    public void setValueSumCostAndVatCurrency(final double valueSumCostAndVatCurrency) {
        this.valueSumCostAndVatCurrency = valueSumCostAndVatCurrency;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(final double weight) {
        this.weight = weight;
    }

    public double getValueSumAllowance() {
        return valueSumAllowance;
    }

    public void setValueSumAllowance(final double valueSumAllowance) {
        this.valueSumAllowance = valueSumAllowance;
    }

    public double getValueSumVatRetail() {
        return valueSumVatRetail;
    }

    public void setValueSumVatRetail(final double valueSumVatRetail) {
        this.valueSumVatRetail = valueSumVatRetail;
    }

    public double getValueSumCostRetail() {
        return valueSumCostRetail;
    }

    public void setValueSumCostRetail(final double valueSumCostRetail) {
        this.valueSumCostRetail = valueSumCostRetail;
    }

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

    public double getValueCostAndAllowance() {
        return valueCostAndAllowance;
    }

    public void setValueCostAndAllowance(final double valueCostAndAllowance) {
        this.valueCostAndAllowance = valueCostAndAllowance;
    }
}
