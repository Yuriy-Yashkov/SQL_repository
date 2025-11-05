package by.march8.ecs.application.modules.terminal.model;

/**
 * @author Andy 22.10.2018 - 14:43.
 */
public class BaseTerminalItem {
    private long barCode;
    private int amount;
    private int pack;

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getPack() {
        return pack;
    }

    public void setPack(int pack) {
        this.pack = pack;
    }

    public long getBarCode() {
        return barCode;
    }

    public void setBarCode(long barCode) {
        this.barCode = barCode;
    }

    @Override
    public String toString() {
        return "BaseTerminalItem{" +
                "barCode='" + barCode + '\'' +
                ", amount=" + amount +
                ", pack=" + pack +
                '}';
    }
}
