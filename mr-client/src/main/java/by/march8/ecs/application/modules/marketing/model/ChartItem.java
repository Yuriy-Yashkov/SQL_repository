package by.march8.ecs.application.modules.marketing.model;

/**
 * @author Andy 07.05.2018 - 9:07.
 */
public class ChartItem {
    private String date;
    private int amount;

    public String getDate() {

        return date.substring(0, 10);
    }

    public void setDate(final String date) {
        this.date = date;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(final int amount) {
        this.amount = amount;
    }

}
