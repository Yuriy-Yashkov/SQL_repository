package dept.calculationprice.model;

/**
 * @author Andy 27.02.2017.
 */
public class ProtocolPreset {
    private int protocolType;
    private int currencyType;
    private double currencyRate;
    private double[] currencyRateSet;
    private boolean showCurrency;
    private boolean showCredit;

    public int getProtocolType() {
        return protocolType;
    }

    public void setProtocolType(final int protocolType) {
        this.protocolType = protocolType;
    }

    public int getCurrencyType() {
        return currencyType;
    }

    public void setCurrencyType(final int currencyType) {
        this.currencyType = currencyType;
    }

    public double getCurrencyRate() {
        return currencyRate;
    }

    public void setCurrencyRate(final double currencyRate) {
        this.currencyRate = currencyRate;
    }

    public double[] getCurrencyRateSet() {
        return currencyRateSet;
    }

    public void setCurrencyRateSet(final double[] currencyRateSet) {
        this.currencyRateSet = currencyRateSet;
    }

    public boolean isShowCurrency() {
        return showCurrency;
    }

    public void setShowCurrency(final boolean showCurrency) {
        this.showCurrency = showCurrency;
    }

    public boolean isShowCredit() {
        return showCredit;
    }

    public void setShowCredit(boolean showCredit) {
        this.showCredit = showCredit;
    }
}
