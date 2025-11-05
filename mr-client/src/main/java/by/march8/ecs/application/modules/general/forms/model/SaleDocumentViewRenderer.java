package by.march8.ecs.application.modules.general.forms.model;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class SaleDocumentViewRenderer extends DefaultTableCellRenderer {
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        if (value != null) {
            int status = Integer.valueOf(table.getValueAt(row, 3).toString());
            if (isSelected) {
                c.setBackground(Color.LIGHT_GRAY);
            } else if (status == 3) {
                c.setBackground(Color.YELLOW);
            } else if (status == 1) {
                c.setBackground(Color.PINK);
            } else {
                c.setBackground(Color.WHITE);
            }
        }
        return c;
    }
}
