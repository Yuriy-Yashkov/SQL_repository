package by.march8.ecs.application.modules.plan;

import javax.swing.*;

/**
 * Created by Andy on 04.11.2014.
 */
public class SelectionCriteria {
    private JComboBox value;
    private String condition;

    public SelectionCriteria(String condition, JComboBox value) {
        this.value = value;
        this.condition = condition;
    }

    public JComboBox getValue() {
        return value;
    }

    public void setValue(final JComboBox value) {
        this.value = value;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(final String condition) {
        this.condition = condition;
    }
}
