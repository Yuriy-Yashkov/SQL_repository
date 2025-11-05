package by.march8.ecs.framework.common.model;

import java.util.Date;

/**
 * @ author Andy 21.10.2015.
 */
public class CurrencySet {
    private int id;
    private String name;
    private float rate;
    private Date date;


    public int getId() {
        return id;
    }

    public void setId(final int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public float getRate() {
        return rate;
    }

    public void setRate(final float rate) {
        this.rate = rate;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(final Date date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "CurrencySet{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", rate=" + rate +
                ", date=" + date +
                '}';
    }
}
