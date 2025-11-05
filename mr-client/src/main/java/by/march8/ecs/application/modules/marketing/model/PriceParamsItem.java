package by.march8.ecs.application.modules.marketing.model;

/**
 * @author Andy 30.10.2017.
 */

/**
 * Класс описывает Ценовые параметры изделия
 */
public class PriceParamsItem {

    // Шифр артикула
    private int articleCode;
    // Размеры
    private int size;


    // Себестоимость
    private double primeCostValue;
    //Рентабельность
    private double profitabilityValue;
    // Действующая цена
    private double effectivePriceValue;
    // Предложенная цена
    private double suggestedPriceValue;


    public int getArticleCode() {
        return articleCode;
    }

    public void setArticleCode(final int articleCode) {
        this.articleCode = articleCode;
    }

    public int getSize() {
        return size;
    }

    public void setSize(final int size) {
        this.size = size;
    }

    public double getPrimeCostValue() {
        return primeCostValue;
    }

    public void setPrimeCostValue(final double primeCostValue) {
        this.primeCostValue = primeCostValue;
    }

    public double getProfitabilityValue() {
        return profitabilityValue;
    }

    public void setProfitabilityValue(final double profitabilityValue) {
        this.profitabilityValue = profitabilityValue;
    }

    public double getEffectivePriceValue() {
        return effectivePriceValue;
    }

    public void setEffectivePriceValue(final double effectivePriceValue) {
        this.effectivePriceValue = effectivePriceValue;
    }

    public double getSuggestedPriceValue() {
        return suggestedPriceValue;
    }

    public void setSuggestedPriceValue(final double suggestedPriceValue) {
        this.suggestedPriceValue = suggestedPriceValue;
    }
}
