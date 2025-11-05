package by.march8.ecs.application.modules.warehouse.internal.displacement;

/**
 * @author Andy 27.07.2016.
 */
public class ReductionPriceNSI {

    private int itemId;
    private double price1Grade;
    private double price3Grade;
    private double summ1Grade;
    private int amount;

    private int grade;


    public int getItemId() {
        return itemId;
    }

    public void setItemId(final int itemId) {
        this.itemId = itemId;
    }

    public double getPrice1Grade() {
        return price1Grade;
    }

    public void setPrice1Grade(final double price1Grade) {
        this.price1Grade = price1Grade;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(final int amount) {
        this.amount = amount;
    }

    public double getPrice3Grade() {
        return price3Grade;
    }

    public void setPrice3Grade(final double price3Grade) {
        this.price3Grade = price3Grade;
    }

    public double getSumm1Grade() {
        return summ1Grade;
    }

    public void setSumm1Grade(final double summ1Grade) {
        this.summ1Grade = summ1Grade;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }
}
