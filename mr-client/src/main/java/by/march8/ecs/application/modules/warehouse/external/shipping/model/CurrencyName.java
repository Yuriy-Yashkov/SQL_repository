package by.march8.ecs.application.modules.warehouse.external.shipping.model;

/**
 * @author Andy 01.03.2016.
 */
public class CurrencyName {
    private String name;
    private String symbol;

    public CurrencyName(final String name, final String symbol) {
        this.name = name;
        this.symbol = symbol;
    }

    public CurrencyName() {

    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(final String symbol) {
        this.symbol = symbol;
    }
}
