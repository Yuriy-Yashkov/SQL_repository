package dept.ves.mode;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

/**
 * @author Andy on 02.03.2020 14:19
 */
public class VolumeOfPurchasesCellRender extends DefaultTableCellRenderer {

    private static Color color_2 = new Color(0xCDF2E3);
    private static Color color_4 = new Color(0xB0F2CD);
    private static Color color_6 = new Color(0x91F2D8);
    private static Color color_8 = new Color(0x7EF2E4);
    private static Color color_10 = new Color(0x56F2DA);
    private static Color color_15 = new Color(0x2CCBF2);
    private static Color color_20 = new Color(0x17AEF2);

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        if (value != null) {
            int discount = Integer.valueOf(table.getValueAt(row, 3).toString());
            Color color = getColorByDiscount(discount);
            if (isSelected) {
                c.setBackground(Color.LIGHT_GRAY);
            } else if (discount == 0) {
                c.setBackground(Color.WHITE);
            } else {
                c.setBackground(color);
            }
        }
        return c;
    }

    private Color getColorByDiscount(int discountValue) {
        switch (discountValue) {
            case 2:
                return color_2;
            case 4:
                return color_4;
            case 6:
                return color_6;
            case 7:
                return color_8;
            case 10:
                return color_10;
            case 15:
                return color_15;
            case 20:
                return color_20;
            default:
                return Color.WHITE;
        }
    }
}
