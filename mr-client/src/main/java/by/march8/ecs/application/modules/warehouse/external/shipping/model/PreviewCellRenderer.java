package by.march8.ecs.application.modules.warehouse.external.shipping.model;

import by.gomel.freedev.ucframework.uccore.viewport.gridviewport.model.GeneralTableModel;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

/**
 * @author Andy 03.11.2015.
 */
public class PreviewCellRenderer extends DefaultTableCellRenderer {

    private Font customFont = new Font("Sans", Font.BOLD, 13);

    @Override
    public Component getTableCellRendererComponent(final JTable table, final Object value, final boolean isSelected, final boolean hasFocus, final int row, final int column) {
        Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        if (value != null) {
            GeneralTableModel model = (GeneralTableModel) table.getModel();
            if (isSelected) {
                if (((table.getValueAt(row, column)).toString()).equals("-1") || ((table.getValueAt(row, column)).toString()).equals("-1.0")) {

                    c.setBackground(Color.LIGHT_GRAY);
                    c.setForeground(Color.LIGHT_GRAY);
                } else {
                    c.setBackground(Color.LIGHT_GRAY);
                    c.setForeground(Color.BLACK);
                }
            } else {
                if (((table.getValueAt(row, column)).toString()).equals("-1") || ((table.getValueAt(row, column)).toString()).equals("-1.0")) {
                    c.setBackground(Color.white);
                    c.setForeground(Color.white);

                } else {
                    c.setBackground(Color.WHITE);
                    c.setForeground(Color.BLACK);
                }
            }

            if (((PreviewEntity) (model.getDataModel().get(row))).isFooter()) {
                c.setFont(customFont);
            } else {
                c.setFont(table.getFont());
            }

            /*if (isSelected) {
                c.setBackground(Color.LIGHT_GRAY);
            } else if (((table.getValueAt(row, 2)).toString()).equals("Формируется")) {
                c.setBackground(Color.YELLOW);
            } else if ((table.getValueAt(row, 2)).equals("Удалён")) {
                c.setBackground(Color.PINK);
            } else if ((table.getValueAt(row, 2).toString().trim()).equals("Непонятно =)")) {
                c.setBackground(Color.BLUE);
            } else if (((table.getValueAt(row, 2)).toString()).equals("Закрыт")) {
                c.setBackground(Color.white);
            }*/
        }
        return c;
    }
}
