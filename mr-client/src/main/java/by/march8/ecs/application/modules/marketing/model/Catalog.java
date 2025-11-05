package by.march8.ecs.application.modules.marketing.model;

import java.util.Date;

public class Catalog {
    private int id;
    private String number;
    private Date date;

    private int currId;
    private double currRate;
    private Date currDate;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNumber() {
        if (number != null) {
            return number.trim();
        }
        return null;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getCurrId() {
        return currId;
    }

    public void setCurrId(int currId) {
        this.currId = currId;
    }

    public Date getCurrDate() {
        return currDate;
    }

    public void setCurrDate(Date currDate) {
        this.currDate = currDate;
    }

    public double getCurrRate() {
        return currRate;
    }

    public void setCurrRate(double currRate) {
        this.currRate = currRate;
    }
}
