package by.march8.ecs.application.modules.marketing.model;

import by.march8.ecs.application.modules.filemanager.model.ColorPresetHelper;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

/**
 * @author Andy 13.12.2018.
 */
public class ColorSelectorCellRender extends DefaultTableCellRenderer {

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        if (value != null) {
            if (column == 1) {
                if (isSelected) {
                    c.setBackground(Color.LIGHT_GRAY);
                    c.setForeground(Color.BLACK);
                } else {

                    c.setForeground(Color.BLACK);
                    c.setBackground(ColorPresetHelper.getColorByName((String) value).getColor());
                }
            }
        }
        return c;
    }
}
