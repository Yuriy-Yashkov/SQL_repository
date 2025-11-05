package by.march8.ecs.application.modules.art.model;

/**
 * @author Andy 24.01.2017.
 */
public class ProductionReportTotal {

    // Колонка 3
    private int remainsBeginPeriod;

    // Колонка 4
    private int inComingCuttingDept;

    // Колонка 5
    private int inComing708Dept;

    // Колонка 6
    private int outComingWarehouse;

    // Колонка 7
    private int outComingCuttingDept;

    // Колонка 8
    private int remainsEndPeriod;

    public int getRemainsBeginPeriod() {
        return remainsBeginPeriod;
    }

    public void setRemainsBeginPeriod(final int remainsBeginPeriod) {
        this.remainsBeginPeriod = remainsBeginPeriod;
    }

    public int getInComingCuttingDept() {
        return inComingCuttingDept;
    }

    public void setInComingCuttingDept(final int inComingCuttingDept) {
        this.inComingCuttingDept = inComingCuttingDept;
    }

    public int getInComing708Dept() {
        return inComing708Dept;
    }

    public void setInComing708Dept(final int inComing708Dept) {
        this.inComing708Dept = inComing708Dept;
    }

    public int getOutComingWarehouse() {
        return outComingWarehouse;
    }

    public void setOutComingWarehouse(final int outComingWarehouse) {
        this.outComingWarehouse = outComingWarehouse;
    }

    public int getOutComingCuttingDept() {
        return outComingCuttingDept;
    }

    public void setOutComingCuttingDept(final int outComingCuttingDept) {
        this.outComingCuttingDept = outComingCuttingDept;
    }

    public int getRemainsEndPeriod() {
        return remainsEndPeriod;
    }

    public void setRemainsEndPeriod(final int remainsEndPeriod) {
        this.remainsEndPeriod = remainsEndPeriod;
    }

    @Override
    public String toString() {
        return
                remainsBeginPeriod +
                        "\t" + inComingCuttingDept +
                        "\t" + inComing708Dept +
                        "\t" + outComingWarehouse +
                        "\t" + outComingCuttingDept +
                        "\t" + remainsEndPeriod;
    }
}
