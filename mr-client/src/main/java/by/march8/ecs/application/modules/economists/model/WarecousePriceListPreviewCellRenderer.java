package by.march8.ecs.application.modules.economists.model;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

/**
 * @author Andy 13.03.2017.
 */
public class WarecousePriceListPreviewCellRenderer extends DefaultTableCellRenderer {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private Color c1 = new Color(0xF2D6A0);
    private Color c2 = new Color(0xD0E6F5);
    private Font customFont = new Font("Sans", Font.BOLD, 12);

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        if (isSelected) {
            c.setBackground(Color.LIGHT_GRAY);
            if (column == 2) {
                c.setFont(customFont);
            } else {
                c.setFont(table.getFont());
            }
        } else {
            if (column == 2) {
                c.setBackground(c1);
                c.setFont(customFont);
            } else {
                c.setBackground(Color.white);
                c.setFont(table.getFont());
            }
        }
        return c;
    }
}
