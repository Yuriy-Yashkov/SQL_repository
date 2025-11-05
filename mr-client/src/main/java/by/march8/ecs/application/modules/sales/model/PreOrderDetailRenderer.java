package by.march8.ecs.application.modules.sales.model;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class PreOrderDetailRenderer extends DefaultTableCellRenderer {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private Color c1 = new Color(0xBEF2AE);
    private Color c2 = new Color(0xD0E6F5);
    private Color wrong = new Color(0xF5A9B9);

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        if (value != null) {
            float price = Float.valueOf(table.getValueAt(row, 18).toString());

            if (isSelected) {
                c.setBackground(c1);
            } else if (price == 0) {
                c.setBackground(Color.RED);
            } else {
                if (column > 17) {
                    c.setBackground(c2);
                } else {
                    c.setBackground(Color.WHITE);
                }
            }

        }
        return c;
    }
}