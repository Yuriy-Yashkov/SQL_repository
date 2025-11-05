package dept.ves.mode;

import by.march8.ecs.framework.common.Settings;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class SaleDocumentPickerCellRenderer extends DefaultTableCellRenderer {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private Color c1 = new Color(0xDCEAF2);
    private Color c2 = new Color(0xD0E6F5);
    private int COLUMN_STATUS = 3;
    private int COLUMN_CALCULATED = 4;

    public SaleDocumentPickerCellRenderer(boolean singleSelect) {
        if (singleSelect) {
            COLUMN_STATUS = 2;
            COLUMN_CALCULATED = 3;
        }
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        if (isSelected) {
            c.setBackground(Color.LIGHT_GRAY);
        } else if (((table.getValueAt(row, COLUMN_STATUS))) == null) {

        } else if (((table.getValueAt(row, COLUMN_STATUS)).toString()).equals("3")) {
            if ((((table.getValueAt(row, COLUMN_CALCULATED)).toString()).equals("true"))) {
                c.setBackground(Color.YELLOW);
            } else {
                c.setBackground(Settings.COLOR_RECALCULATE);
            }
        } else if ((table.getValueAt(row, COLUMN_STATUS)).equals("1")) {
            c.setBackground(Color.PINK);
        } else if ((table.getValueAt(row, COLUMN_STATUS).toString().trim()).equals("Непонятно =)")) {
            c.setBackground(Color.BLUE);
        } else if (((table.getValueAt(row, COLUMN_STATUS)).toString()).equals("0")) {
            c.setBackground(Color.white);
        }

        return c;
    }
}
