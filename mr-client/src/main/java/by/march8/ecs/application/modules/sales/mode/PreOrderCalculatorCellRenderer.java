package by.march8.ecs.application.modules.sales.mode;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

/**
 * Рендер ячеек
 *
 * @author Andy 15.06.2016.
 */
public class PreOrderCalculatorCellRenderer extends DefaultTableCellRenderer {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private Color c1 = new Color(0xD4F2D1);
    private Color c2 = new Color(0xD9DAFF);
    private Color c3 = new Color(0xC9F5BE);
    private Color wrong = new Color(0xF5A9B9);
    private Color empty = new Color(0xF5A26A);

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        if (value != null) {
            double primeCost = Double.valueOf(table.getValueAt(row, 17).toString());

            if (isSelected) {
                c.setBackground(Color.LIGHT_GRAY);
            } else if (primeCost == 0) {
                c.setBackground(empty);
            } else {
                if (column == 7 || column == 11) {
                    c.setBackground(c2);
                } else if (column == 12) {
                    c.setBackground(c3);
                } else if (column == 9 || column == 15) {
                    c.setBackground(c1);
                } else if (column == 17 && primeCost < PreOrderCalculatorMode.PROFITABILITY_LIMIT) {
                    c.setBackground(wrong);
                } else {
                    c.setBackground(Color.white);
                }
            }
        }

        return c;
    }

}
