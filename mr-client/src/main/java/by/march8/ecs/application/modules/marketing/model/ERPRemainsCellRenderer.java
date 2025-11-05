package by.march8.ecs.application.modules.marketing.model;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

/**
 * @author Andy 14.11.2017.
 */
public class ERPRemainsCellRenderer extends DefaultTableCellRenderer {

    private static final Color CLR_AMOUNT = new Color(148, 198, 255);
    private int COLUMN_REMAINS_AMOUNT = 8;

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        if (value != null) {
            if (isSelected) {
                c.setBackground(Color.LIGHT_GRAY);
            } else {
                if (column == COLUMN_REMAINS_AMOUNT) {
                    c.setBackground(CLR_AMOUNT);
                } else {
                    c.setBackground(Color.WHITE);
                }
            }
        }
        return c;
    }
}