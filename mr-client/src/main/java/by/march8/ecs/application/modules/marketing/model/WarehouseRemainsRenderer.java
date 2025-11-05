package by.march8.ecs.application.modules.marketing.model;

import by.gomel.freedev.ucframework.ucswing.uicontrols.UCImageLabel;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

/**
 * @author Andy 26.04.2018 - 13:49.
 */
public class WarehouseRemainsRenderer extends DefaultTableCellRenderer {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private Color c1 = new Color(0xF2F1C9);
    private Color c11 = new Color(0xDDF2BB);
    private Color c12 = new Color(0xF2D4BC);
    private Color c2 = new Color(0xD0E6F5);

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        if (isSelected) {
            c.setBackground(Color.LIGHT_GRAY);
        } else {
            if (column == 7) {
                if (((int) table.getValueAt(row, 7) < 0)) {
                    c.setForeground(Color.red);
                } else {
                    c.setForeground(Color.BLACK);
                }
                c.setBackground(c1);
            } else if (column == 8) {
                c.setBackground(c11);
                if (((int) table.getValueAt(row, 8) < 0)) {
                    c.setForeground(Color.red);
                } else {
                    c.setForeground(Color.BLACK);
                }
            } else if (column == 9) {
                c.setBackground(c12);
                c.setForeground(Color.BLACK);
            } else if (column == 2) {
                c.setBackground(c2);
                c.setForeground(Color.BLACK);
            } else if (column == 3) {
                c.setBackground(c2);
                c.setForeground(Color.BLACK);
            } else {
                c.setBackground(Color.white);
                c.setForeground(Color.BLACK);
            }
        }

        if (column == 11) {
            UCImageLabel label = (UCImageLabel) value;
            if (label != null) {
                int rowCount = table.getValueAt(row, 10).toString().split("<br>").length;
                try {
                    if (label.setImage()) {
                        if (rowCount < 8) {
                            rowCount = 8;
                        }
                        table.setRowHeight(row, rowCount * 16 + 2);
                    } else {
                        table.setRowHeight(row, rowCount * 16 + 2);
                    }
                    return label;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return c;
    }
}
