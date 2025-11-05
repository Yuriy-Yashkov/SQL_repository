package dept.ves.model;

import by.march8.api.utils.DatePeriod;

import java.util.List;

public class DetailAnalysisDataSet {

    private DatePeriod period;
    private double currencyRate;
    private double factRevenue;
    private double factRevenueVat;
    private double factPrimeCost;
    private double profit;


    private List<AnalysisReportPartition> partitionList;

    private double countrySumCost;
    private double countrySumCostAndVat;
    private double countrySumCostAndVatCurrency;

    private double countryRevenue;
    private double countryRevenueVat;
    private double countryPrimeCostPlan;
    private double countryPrimeCost;

    private double countryProfit;

    private double totalSumCost;
    private double totalSumCostAndVat;
    private double totalSumCostAndVatCurrency;

    private double totalRevenue;
    private double totalRevenueVat;
    private double totalPrimeCostPlan;
    private double totalPrimeCost;

    private double totalProfit;

    public List<AnalysisReportPartition> getPartitionList() {
        return partitionList;
    }

    public void setPartitionList(List<AnalysisReportPartition> partitionList) {
        this.partitionList = partitionList;
    }

    public DatePeriod getPeriod() {
        return period;
    }

    public void setPeriod(DatePeriod period) {
        this.period = period;
    }

    public double getFactRevenue() {
        return factRevenue;
    }

    public void setFactRevenue(double factRevenue) {
        this.factRevenue = factRevenue;
    }

    public double getProfit() {
        return profit;
    }

    public void setProfit(double profit) {
        this.profit = profit;
    }

    public double getCurrencyRate() {
        return currencyRate;
    }

    public void setCurrencyRate(double currencyRate) {
        this.currencyRate = currencyRate;
    }

    public double getTotalSumCost() {
        return totalSumCost;
    }

    public void setTotalSumCost(double totalSumCost) {
        this.totalSumCost = totalSumCost;
    }

    public double getTotalSumCostAndVatCurrency() {
        return totalSumCostAndVatCurrency;
    }

    public void setTotalSumCostAndVatCurrency(double totalSumCostAndVatCurrency) {
        this.totalSumCostAndVatCurrency = totalSumCostAndVatCurrency;
    }

    public double getTotalRevenue() {
        return totalRevenue;
    }

    public void setTotalRevenue(double totalRevenue) {
        this.totalRevenue = totalRevenue;
    }

    public double getTotalProfit() {
        return totalProfit;
    }

    public void setTotalProfit(double totalProfit) {
        this.totalProfit = totalProfit;
    }

    public double getCountrySumCost() {
        return countrySumCost;
    }

    public void setCountrySumCost(double countrySumCost) {
        this.countrySumCost = countrySumCost;
    }

    public double getCountrySumCostAndVatCurrency() {
        return countrySumCostAndVatCurrency;
    }

    public void setCountrySumCostAndVatCurrency(double countrySumCostAndVatCurrency) {
        this.countrySumCostAndVatCurrency = countrySumCostAndVatCurrency;
    }

    public double getCountryRevenue() {
        return countryRevenue;
    }

    public void setCountryRevenue(double countryRevenue) {
        this.countryRevenue = countryRevenue;
    }

    public double getCountryProfit() {
        return countryProfit;
    }

    public void setCountryProfit(double countryProfit) {
        this.countryProfit = countryProfit;
    }

    public double getFactRevenueVat() {
        return factRevenueVat;
    }

    public void setFactRevenueVat(double factRevenueVat) {
        this.factRevenueVat = factRevenueVat;
    }

    public double getFactPrimeCost() {
        return factPrimeCost;
    }

    public void setFactPrimeCost(double factPrimeCost) {
        this.factPrimeCost = factPrimeCost;
    }

    public double getCountrySumCostAndVat() {
        return countrySumCostAndVat;
    }

    public void setCountrySumCostAndVat(double countrySumCostAndVat) {
        this.countrySumCostAndVat = countrySumCostAndVat;
    }

    public double getCountryRevenueVat() {
        return countryRevenueVat;
    }

    public void setCountryRevenueVat(double countryRevenueVat) {
        this.countryRevenueVat = countryRevenueVat;
    }

    public double getCountryPrimeCostPlan() {
        return countryPrimeCostPlan;
    }

    public void setCountryPrimeCostPlan(double countryPrimeCostPlan) {
        this.countryPrimeCostPlan = countryPrimeCostPlan;
    }

    public double getCountryPrimeCost() {
        return countryPrimeCost;
    }

    public void setCountryPrimeCost(double countryPrimeCost) {
        this.countryPrimeCost = countryPrimeCost;
    }

    public double getTotalSumCostAndVat() {
        return totalSumCostAndVat;
    }

    public void setTotalSumCostAndVat(double totalSumCostAndVat) {
        this.totalSumCostAndVat = totalSumCostAndVat;
    }

    public double getTotalRevenueVat() {
        return totalRevenueVat;
    }

    public void setTotalRevenueVat(double totalRevenueVat) {
        this.totalRevenueVat = totalRevenueVat;
    }

    public double getTotalPrimeCostPlan() {
        return totalPrimeCostPlan;
    }

    public void setTotalPrimeCostPlan(double totalPrimeCostPlan) {
        this.totalPrimeCostPlan = totalPrimeCostPlan;
    }

    public double getTotalPrimeCost() {
        return totalPrimeCost;
    }

    public void setTotalPrimeCost(double totalPrimeCost) {
        this.totalPrimeCost = totalPrimeCost;
    }
}
