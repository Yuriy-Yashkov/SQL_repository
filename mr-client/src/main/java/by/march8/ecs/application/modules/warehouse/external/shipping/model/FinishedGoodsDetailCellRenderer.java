package by.march8.ecs.application.modules.warehouse.external.shipping.model;

import by.gomel.freedev.ucframework.ucswing.uicontrols.UCImageLabel;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

/**
 * Рендер ячеек
 * @author Andy 15.06.2016.
 */
public class FinishedGoodsDetailCellRenderer extends DefaultTableCellRenderer {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private Color c1 = new Color(0xDCEAF2);
    private Color c2 = new Color(0xD0E6F5);
    private Color wrong = new Color(0xF5A9B9);

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        if (value != null) {
            if (isSelected) {
                c.setBackground(Color.LIGHT_GRAY);
            } else if ((((table.getValueAt(row, 18)).toString()).equals("true"))) {
                c.setBackground(wrong);
            } else {
                if (column == 9) {
                    c.setBackground(c1);
                } else if (column == 12) {
                    c.setBackground(c2);
                } else {
                    c.setBackground(Color.white);
                }
            }
        }

        if (column == 19) {
            UCImageLabel label = (UCImageLabel) value;
            if (label != null) {
                try {
                    label.setImage();
                    return label;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        return c;
    }

}
