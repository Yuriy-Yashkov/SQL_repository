package by.march8.ecs.application.modules.warehouse.internal.displacement.mode;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

/**
 * @author Andy 26.07.2016.
 */
public class DisplacementCellRender extends DefaultTableCellRenderer {

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        if (value != null) {
            if (isSelected) {
                c.setBackground(Color.LIGHT_GRAY);
            } else if (((table.getValueAt(row, 7)).toString()).equals("ФОРМИРОВАНИЕ")) {
                c.setBackground(Color.YELLOW);
            } else if ((table.getValueAt(row, 7)).equals("УДАЛЕН")) {
                c.setBackground(Color.PINK);
            } else if ((table.getValueAt(row, 7).toString().trim()).equals("НЕТ ДАННЫХ")) {
                c.setBackground(Color.RED);
            } else if (((table.getValueAt(row, 7)).toString()).equals("ОТПРАВЛЕН")) {
                c.setBackground(Color.GREEN);
            } else if (((table.getValueAt(row, 7)).toString()).equals("ЗАКРЫТ")) {
                c.setBackground(Color.WHITE);
            }
        }
        return c;
    }
}