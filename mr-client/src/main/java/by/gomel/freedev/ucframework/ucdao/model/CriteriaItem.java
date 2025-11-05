package by.gomel.freedev.ucframework.ucdao.model;

import javax.swing.*;

/**
 * @author Andy 13.11.2014.
 */
public class CriteriaItem {
    private JComponent component;
    private String field;
    private String sign;
    private String value;
    private boolean valueIsString = false;

    public CriteriaItem(final JComponent component, final String field, final String sign) {
        this.component = component;
        this.field = field;
        this.setSign(sign);
        value = "0";
    }

    public CriteriaItem(final int number, final String field, final String sign) {
        this.component = null;
        value = String.valueOf(number);
        this.field = field;
        this.setSign(sign);
        valueIsString = false;
    }

    public CriteriaItem(final String value, final String field, final String sign) {
        this.component = null;
        this.value = value;
        this.field = field;
        this.setSign(sign);
        valueIsString = true;
    }


    public JComponent getComponent() {
        return component;
    }

    public void setComponent(final JComponent component) {
        this.component = component;
    }

    public String getField() {
        return field;
    }

    public void setField(final String field) {
        this.field = field;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        if (sign == null) {
            sign = "=";
        }

        if (!sign.equalsIgnoreCase("like") && !sign.equalsIgnoreCase("date equals")) {

            if ((!sign.trim().equals(">")) && (!sign.trim().equals("<")) && (!sign.trim().equals(">="))
                    && (!sign.trim().equals("<=")) && (!sign.trim().equals("="))) {
                sign = "=";
            }
        }

        this.sign = sign;
    }

    public String getValue() {
        return value;
    }

    public void setValue(final String value) {
        this.value = value;
    }

    public boolean isValueIsString() {
        return valueIsString;
    }

    public void setValueIsString(final boolean valueIsString) {
        this.valueIsString = valueIsString;
    }
}
