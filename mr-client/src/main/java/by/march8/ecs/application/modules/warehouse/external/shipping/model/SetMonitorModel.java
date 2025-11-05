package by.march8.ecs.application.modules.warehouse.external.shipping.model;

public class SetMonitorModel {
    private int amount;
    private int grade;
    private int size;
    private int growth;
    private int modelNumber;
    private int unloadingId;
    private int contractorCode;

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getGrowth() {
        return growth;
    }

    public void setGrowth(int growth) {
        this.growth = growth;
    }

    public int getModelNumber() {
        return modelNumber;
    }

    public void setModelNumber(int modelNumber) {
        this.modelNumber = modelNumber;
    }

    public int getUnloadingId() {
        return unloadingId;
    }

    public void setUnloadingId(int unloadingId) {
        this.unloadingId = unloadingId;
    }

    public int getContractorCode() {
        return contractorCode;
    }

    public void setContractorCode(int contractorCode) {
        this.contractorCode = contractorCode;
    }
}
