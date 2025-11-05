package dept.ves.model;

import java.util.HashMap;

public class AnalysisDetailRegion {
    private String id;
    private String name;
    private HashMap<String, AnalysisDetailsGroup> groups;
    private double sumCost;
    private double sumCostAndVat;
    private double sumCostAndVatCurrency;

    private double revenue;
    private double revenueVat;

    private double primeCostPlan;
    private double primeCost;

    private double profit;


    public AnalysisDetailRegion(String id, String name) {
        this.name = name;
        groups = new HashMap<>();
        groups.put("41", new AnalysisDetailsGroup());
        groups.put("42", new AnalysisDetailsGroup());
        groups.put("43", new AnalysisDetailsGroup());
        groups.put("47", new AnalysisDetailsGroup());
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public HashMap<String, AnalysisDetailsGroup> getGroups() {
        return groups;
    }

    public void setGroups(HashMap<String, AnalysisDetailsGroup> groups) {
        this.groups = groups;
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
