package by.march8.ecs.application.modules.references.classifier.model;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

/**
 * @author Andy 13.12.2018.
 */
public class CompositionCellRender extends DefaultTableCellRenderer {

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        if (value != null) {
            if (isSelected) {
                c.setBackground(Color.LIGHT_GRAY);
                c.setForeground(Color.BLACK);
            } else if ((((table.getValueAt(row, 0)).toString()).equals("false"))) {
                c.setForeground(Color.LIGHT_GRAY);
                c.setBackground(Color.white);
            } else {
                c.setForeground(Color.BLACK);
                c.setBackground(Color.white);
            }
        }
        return c;
    }
}
