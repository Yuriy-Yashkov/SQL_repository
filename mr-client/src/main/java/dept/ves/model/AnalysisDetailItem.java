package dept.ves.model;

import java.util.Date;

public class AnalysisDetailItem {
    private Date dateSale;
    private int contractorCode;
    private int export;
    private String itemName;
    private int articleCode;
    private int size;
    private int amount;
    private double sumCost;
    private double sumCostAndVat;
    private double primeCost;

    public Date getDateSale() {
        return dateSale;
    }

    public void setDateSale(Date dateSale) {
        this.dateSale = dateSale;
    }

    public int getContractorCode() {
        return contractorCode;
    }

    public void setContractorCode(int contractorCode) {
        this.contractorCode = contractorCode;
    }

    public int getExport() {
        return export;
    }

    public void setExport(int export) {
        this.export = export;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public int getArticleCode() {
        return articleCode;
    }

    public void setArticleCode(int articleCode) {
        this.articleCode = articleCode;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
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

    public double getPrimeCost() {
        return primeCost;
    }

    public void setPrimeCost(double primeCost) {
        this.primeCost = primeCost;
    }
}
