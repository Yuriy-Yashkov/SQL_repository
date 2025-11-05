package common;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class CustomTableCellRenderer extends DefaultTableCellRenderer {

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        if (isSelected) {
            c.setBackground(Color.LIGHT_GRAY);
        } else if (((table.getValueAt(row, 8)).toString()).equals("Формируется")) {
            c.setBackground(Color.YELLOW);
        } else if ((table.getValueAt(row, 8)).equals("Удалён")) {
            c.setBackground(Color.PINK);
        } else if ((table.getValueAt(row, 8).toString().trim()).equals("Непонятно =)")) {
            c.setBackground(Color.BLUE);
        } else if (((table.getValueAt(row, 8)).toString()).equals("Закрыт")) {
            c.setBackground(Color.white);
        }
        return c;
    }
}