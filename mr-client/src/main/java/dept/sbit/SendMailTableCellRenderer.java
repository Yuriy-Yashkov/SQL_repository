package dept.sbit;

import by.march8.ecs.framework.common.Settings;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

/**
 * @author Andy 15.07.2016.
 */
public class SendMailTableCellRenderer extends DefaultTableCellRenderer {

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        if (isSelected) {
            c.setBackground(Color.LIGHT_GRAY);
        } else if (((int) table.getValueAt(row, 2)) == 3) {
            if ((((table.getValueAt(row, 3)).toString()).equals("true"))) {
                c.setBackground(Color.YELLOW);
            } else {
                c.setBackground(Settings.COLOR_RECALCULATE);
            }
        } else {
            c.setBackground(Color.WHITE);
        }
        return c;
    }
}
