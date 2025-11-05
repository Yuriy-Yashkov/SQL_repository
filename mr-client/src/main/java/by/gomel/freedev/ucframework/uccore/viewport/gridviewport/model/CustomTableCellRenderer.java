package by.gomel.freedev.ucframework.uccore.viewport.gridviewport.model;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

/**
 * Кастомный рендер строк JTable. Исходя из некоего значения в строке таблицы,
 * данную строку окрашивает в определенный цвет
 */
public class CustomTableCellRenderer extends DefaultTableCellRenderer {
    /**
     *
     */
    private static final long serialVersionUID = 905778823847717067L;
    private int statusColumnId = 0;

    public CustomTableCellRenderer(int statusColumnId) {
        this.statusColumnId = statusColumnId;
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        if (isSelected) {
            c.setBackground(Color.LIGHT_GRAY);
            c.setForeground(Color.BLACK);
        } else if ((int) table.getValueAt(row, statusColumnId) == -1) {
            c.setBackground(Color.RED);
        } else if ((int) table.getValueAt(row, statusColumnId) == 0) {
            c.setBackground(Color.WHITE);
        } else if ((int) table.getValueAt(row, statusColumnId) == 1) {
            c.setBackground(Color.YELLOW);
        } else if ((int) table.getValueAt(row, statusColumnId) == 2) {
            c.setBackground(Color.GREEN.brighter());
        }
        return c;
    }
}