package dept.ves.model;

import java.util.ArrayList;
import java.util.List;

public class AnalysisReportPartition {
    private String name;
    private String order;
    private List<AnalysisDetailRegion> data;

    private double sumCost;
    private double sumCostAndVat;
    private double sumCostAndVatCurrency;


    private double revenue;
    private double revenueVat;

    private double primeCostPlan;
    private double primeCost;

    private double profit;

    public AnalysisReportPartition(String name, String order) {
        this.name = name;
        this.order = order;
        data = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public double getSumCost() {
        return sumCost;
    }

    public void setSumCost(double sumCost) {
        this.sumCost = sumCost;
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

    public String[] getRegionSequence() {
        if (order != null) {
            return order.split(";");
        }

        return null;
    }

    public List<AnalysisDetailRegion> getData() {
        return data;
    }

    public void setData(List<AnalysisDetailRegion> data) {
        this.data = data;
    }

    public double getSumCostAndVat() {
        return sumCostAndVat;
    }

    public void setSumCostAndVat(double sumCostAndVat) {
        this.sumCostAndVat = sumCostAndVat;
    }

    public double getRevenueVat() {
        return revenueVat;
    }

    public void setRevenueVat(double revenueVat) {
        this.revenueVat = revenueVat;
    }

    public double getPrimeCostPlan() {
        return primeCostPlan;
    }

    public void setPrimeCostPlan(double primeCostPlan) {
        this.primeCostPlan = primeCostPlan;
    }

    public double getPrimeCost() {
        return primeCost;
    }

    public void setPrimeCost(double primeCost) {
        this.primeCost = primeCost;
    }
}
