package by.march8.entities.warehouse;


import java.util.Date;

public class SaleDocumentAnalysisItem {
    private Date date ;
    private double amount ;
    private double cost ;
    private double costCurrency ;
    private String articleCode ;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public double getCostCurrency() {
        return costCurrency;
    }

    public void setCostCurrency(double costCurrency) {
        this.costCurrency = costCurrency;
    }

    public String getArticleCode() {
        return articleCode;
    }

    public void setArticleCode(String articleCode) {
        this.articleCode = articleCode;
    }

    public String getItemCategory() {
        if (!getArticleCode().equals("")) {
            return articleCode.substring(0, 3);
        } else {
            return "000";
        }
    }
}
