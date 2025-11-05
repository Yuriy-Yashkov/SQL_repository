package by.march8.ecs.application.modules.warehouse.external.shipping.model;

public class PresetContractor {
    private int code;
    private boolean additionSorting;

    public PresetContractor(int code) {
        this(code, false);
    }

    public PresetContractor(int code, boolean additionSorting) {
        this.code = code;
        this.additionSorting = additionSorting;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public boolean isAdditionSorting() {
        return additionSorting;
    }

    public void setAdditionSorting(boolean additionSorting) {
        this.additionSorting = additionSorting;
    }
}
