package by.march8.ecs.application.modules.warehouse.external.shipping.model;

public class SizeSequencer {
    private int size;
    private int growth;
    private String value;

    public SizeSequencer(String value) {
        this.value = value;
        String[] val = value.split("_");
        if (val.length > 1) {
            size = Integer.valueOf(val[0]);
            growth = Integer.valueOf(val[1]);
        } else {
            size = Integer.valueOf(val[0]);
            growth = 0;
        }
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

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
