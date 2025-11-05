package by.march8.ecs.application.modules.marketing.model;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

/**
 * @author Andy 03.11.2017.
 */
public class MarketingPriceListDetailCellRenderer extends DefaultTableCellRenderer {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private Color c1 = new Color(0xDCEAF2);
    private Color c2 = new Color(0xD0E6F5);

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        if (column == 7) {
            c.setBackground(c1);
        }/* else if (column == 8) {
            c.setBackground(c2);
        }*/ else {
            c.setBackground(Color.white);
        }
        return c;
    }
}
