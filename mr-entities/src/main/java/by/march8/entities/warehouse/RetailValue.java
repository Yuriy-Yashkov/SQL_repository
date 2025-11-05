package by.march8.entities.warehouse;

/**
 * @author Andy 25.05.2016.
 */
public class RetailValue {
    private double valueTradeMarkup ;
    /**
     * Размер надбавки на единицу изделия (2)
     */
    private double valueAllowance;
    /**
     * Сумма надбавок для всех изделий(3)
     */
    private double valueSumAllowance ;

    /**
     * Стоимость единицы изделия с учетом надбавки(3.5)
     */
    private double valueCostAndAllowance;

    /**
     * Размер НДС единицы изделия с учетом надбавки(4)
     */
    private double valueVatRetail;

    /**
     * Розничная цена изделия(5)
     */
    private double valueCostRetail;
    /**
     * Сумма НДС на розничную цену изделий(6)
     */
    private double valueSumVatRetail ;

    /**
     * Сумма розничной цены изделий(7)
     */
    private double valueSumCostRetail;


    public double getValueAllowance() {
        return valueAllowance;
    }

    public void setValueAllowance(final double valueAllowance) {
        this.valueAllowance = valueAllowance;
    }

    public double getValueSumAllowance() {
        return valueSumAllowance;
    }

    public void setValueSumAllowance(final double valueSumAllowance) {
        this.valueSumAllowance = valueSumAllowance;
    }

    public double getValueCostAndAllowance() {
        return valueCostAndAllowance;
    }

    public void setValueCostAndAllowance(final double valueCostAndAllowance) {
        this.valueCostAndAllowance = valueCostAndAllowance;
    }

    public double getValueVatRetail() {
        return valueVatRetail;
    }

    public void setValueVatRetail(final double valueVatRetail) {
        this.valueVatRetail = valueVatRetail;
    }

    public double getValueCostRetail() {
        return valueCostRetail;
    }

    public void setValueCostRetail(final double valueCostRetail) {
        this.valueCostRetail = valueCostRetail;
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

    public double getValueTradeMarkup() {
        return valueTradeMarkup;
    }

    public void setValueTradeMarkup(final double valueTradeMarkup) {
        this.valueTradeMarkup = valueTradeMarkup;
    }
}
