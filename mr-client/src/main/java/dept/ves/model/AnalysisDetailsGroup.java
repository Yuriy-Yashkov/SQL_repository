package dept.ves.model;

public class AnalysisDetailsGroup {
    private int amountSet;
    private double amountItem;
    private double sumCost;
    private double sumCostAndVat;
    private double sumCostAndVatCurrency;

    private double primeCostPlan;
    private double primeCost;
    private double percentPrimeCost;

    private double percentRevenue;
    private double revenue;
    private double percentRevenueVat;
    private double revenueVat;

    private double profit;
    private double profitability;

    public int getAmountSet() {
        return amountSet;
    }

    public void setAmountSet(int amountSet) {
        this.amountSet = amountSet;
    }

    public double getAmountItem() {
        return amountItem;
    }

    public void setAmountItem(double amountItem) {
        this.amountItem = amountItem;
    }

    public double getSumCostAndVatCurrency() {
        return sumCostAndVatCurrency;
    }

    public void setSumCostAndVatCurrency(double sumCostAndVatCurrency) {
        this.sumCostAndVatCurrency = sumCostAndVatCurrency;
    }

    public double getRevenue() {
        return revenue;
    }

    public void setRevenue(double revenue) {
        this.revenue = revenue;
    }

    public double getProfit() {
        return profit;
    }

    public void setProfit(double profit) {
        this.profit = profit;
    }

    public double getProfitability() {
        return profitability;
    }

    public void setProfitability(double profitability) {
        this.profitability = profitability;
    }

    public double getSumCost() {
        return sumCost;
    }

    public void setSumCost(double sumCost) {
        this.sumCost = sumCost;
    }

    public double getSumCostAndVat() {
        return sumCostAndVat;
    }

    public void setSumCostAndVat(double sumCostAndVat) {
        this.sumCostAndVat = sumCostAndVat;
    }

    public double getPrimeCostPlan() {
        return primeCostPlan;
    }

    public void setPrimeCostPlan(double primeCostPlan) {
        this.primeCostPlan = primeCostPlan;
    }

    public double getPercentPrimeCost() {
        return percentPrimeCost;
    }

    public void setPercentPrimeCost(double percent) {
        this.percentPrimeCost = percent;
    }

    public double getPrimeCost() {
        return primeCost;
    }

    public void setPrimeCost(double primeCost) {
        this.primeCost = primeCost;
    }

    public double getRevenueVat() {
        return revenueVat;
    }

    public void setRevenueVat(double revenueVat) {
        this.revenueVat = revenueVat;
    }

    public double getPercentRevenue() {
        return percentRevenue;
    }

    public void setPercentRevenue(double percentRevenue) {
        this.percentRevenue = percentRevenue;
    }

    public double getPercentRevenueVat() {
        return percentRevenueVat;
    }

    public void setPercentRevenueVat(double percentRevenueVat) {
        this.percentRevenueVat = percentRevenueVat;
    }
}
