package by.march8.ecs.application.modules.marketing.model;

import by.march8.ecs.framework.common.Settings;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

/**
 * @author Andy 14.11.2017.
 */
public class MarketingPriceListCellRenderer extends DefaultTableCellRenderer {

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        if (value != null) {
            if (isSelected) {
                c.setBackground(Color.LIGHT_GRAY);
            } else {
                if ((int) (table.getValueAt(row, 0)) == 0) {
                    c.setBackground(Color.WHITE);
                } else if ((int) (table.getValueAt(row, 0)) == 1) {
                    c.setBackground(Settings.COLOR_RECALCULATE);
                } else if ((int) (table.getValueAt(row, 0)) == 2) {
                    c.setBackground(Color.YELLOW);
                } else if ((int) (table.getValueAt(row, 0)) == 3) {
                    c.setBackground(Color.PINK);
                }
            }
        }
        return c;
    }
}