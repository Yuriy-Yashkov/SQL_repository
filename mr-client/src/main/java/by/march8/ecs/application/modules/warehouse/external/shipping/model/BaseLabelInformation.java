package by.march8.ecs.application.modules.warehouse.external.shipping.model;

import by.march8.api.BaseEntity;

/**
 * @author Andy 05.10.2018 - 12:44.
 */
public class BaseLabelInformation extends BaseEntity {

    private String barCode;
    private String eanCode;
    private String color;
    private boolean packLabel;

    public String getBarCode() {
        if (barCode != null) {
            return barCode;
        } else {
            return "";
        }
    }

    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }

    public String getEanCode() {
        if (eanCode != null) {
            return eanCode;
        } else {
            return "";
        }
    }

    public void setEanCode(String eanCode) {
        this.eanCode = eanCode;
    }

    public String getColor() {
        if (color != null) {
            return color;
        } else {
            return "";
        }
    }

    public void setColor(String color) {
        this.color = color;
    }

    public boolean isPackLabel() {
        return packLabel;
    }

    public void setPackLabel(boolean packLabel) {
        this.packLabel = packLabel;
    }

    @Override
    public String toString() {
        return "BaseLabelInformation{" +
                "barCode='" + barCode + '\'' +
                ", eanCode='" + eanCode + '\'' +
                ", color='" + color + '\'' +
                ", packLabel=" + packLabel +
                '}';
    }
}
