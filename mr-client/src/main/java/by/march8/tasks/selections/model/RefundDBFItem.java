package by.march8.tasks.selections.model;

import java.util.Date;

/**
 * @author Andy 26.04.2018 - 12:51.
 */
public class RefundDBFItem {
    private String color;
    private double amount;
    private double cost;
    private double sumCost;
    private double vat;
    private double sumVat;
    private double sumCostAndVat;
    private String eanCode;
    private String priceList;
    private Date date;
    private String number;


    public String getColor() {
        return color;
    }

    public void setColor(final String color) {
        this.color = color;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(final double amount) {
        this.amount = amount;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(final double cost) {
        this.cost = cost;
    }

    public double getSumCost() {
        return sumCost;
    }

    public void setSumCost(final double sumCost) {
        this.sumCost = sumCost;
    }

    public double getVat() {
        return vat;
    }

    public void setVat(final double vat) {
        this.vat = vat;
    }

    public double getSumVat() {
        return sumVat;
    }

    public void setSumVat(final double sumVat) {
        this.sumVat = sumVat;
    }

    public double getSumCostAndVat() {
        return sumCostAndVat;
    }

    public void setSumCostAndVat(final double sumCostAndVat) {
        this.sumCostAndVat = sumCostAndVat;
    }

    public String getEanCode() {
        return eanCode;
    }

    public void setEanCode(final String eanCode) {
        this.eanCode = eanCode;
    }

    public String getPriceList() {
        return priceList;
    }

    public void setPriceList(final String priceList) {
        this.priceList = priceList;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(final Date date) {
        this.date = date;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(final String number) {
        this.number = number;
    }
}