package by.march8.ecs.application.modules.art.mode;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

/**
 * @author Andy 24.01.2017.
 */
public class ProductionReportCellRenderer extends DefaultTableCellRenderer {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private Color c1 = new Color(0xE3F2EB);
    private Color c2 = new Color(0xD0E6F5);
    private Font fontPlain = new Font("Dialog", Font.PLAIN, 12);
    private Font fontBold = new Font("Dialog", Font.BOLD, 12);

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        if (column > 6) {
            this.setHorizontalAlignment(JLabel.CENTER);
        } else {
            this.setHorizontalAlignment(JLabel.LEFT);
        }

        if (isSelected) {
            c.setBackground(Color.LIGHT_GRAY);
            c.setForeground(Color.BLACK);
            c.setFont(fontPlain);
        }
        if (column == 7) {
            c.setBackground(c1);
        } else if (column == 8) {
            c.setBackground(c2);
        } else if (column == 9) {
            String numberValue = String.valueOf(value);

            if (!numberValue.equals("0")) {
                c.setBackground(Color.white);
                c.setForeground(Color.RED);
                c.setFont(fontBold);
            } else {
                c.setBackground(Color.white);
                c.setForeground(Color.BLACK);
                c.setFont(fontPlain);
            }
        } else if (column == 10) {
            String versionVal = String.valueOf(value);

            if (!versionVal.equals("0")) {
                c.setBackground(Color.white);
                c.setForeground(Color.RED);
                c.setFont(fontBold);
            } else {
                c.setBackground(Color.white);
                c.setForeground(Color.BLACK);
                c.setFont(fontPlain);
            }
        } else if (column == 11) {
            String versionVal = String.valueOf(value);

            if (!versionVal.equals("0")) {
                c.setBackground(Color.white);
                c.setForeground(Color.RED);
                c.setFont(fontBold);
            } else {
                c.setBackground(Color.white);
                c.setForeground(Color.BLACK);
                c.setFont(fontPlain);
            }
        } else {
            c.setBackground(Color.white);
            c.setForeground(Color.BLACK);
        }
        return c;
    }
}
