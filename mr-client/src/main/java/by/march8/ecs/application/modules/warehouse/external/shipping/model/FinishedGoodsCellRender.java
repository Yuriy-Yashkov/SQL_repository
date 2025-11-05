package by.march8.ecs.application.modules.warehouse.external.shipping.model;

import by.march8.ecs.framework.common.Settings;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

/**
 * @author Andy 29.09.2015.
 */
public class FinishedGoodsCellRender extends DefaultTableCellRenderer {

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        if (value != null) {
            if (isSelected) {
                c.setBackground(Color.LIGHT_GRAY);
            } else if (((table.getValueAt(row, 9)).toString()).equals("Формируется")) {
                if ((((table.getValueAt(row, 11)).toString()).equals("true"))) {
                    c.setBackground(Color.YELLOW);
                } else {
                    c.setBackground(Settings.COLOR_RECALCULATE);
                }
            } else if ((table.getValueAt(row, 9)).equals("Удалён")) {
                c.setBackground(Color.PINK);
            } else if ((table.getValueAt(row, 9).toString().trim()).equals("Непонятно =)")) {
                c.setBackground(Color.BLUE);
            } else if (((table.getValueAt(row, 9)).toString()).equals("Закрыт")) {
                c.setBackground(Color.white);
            }
        }
        return c;
    }
}
