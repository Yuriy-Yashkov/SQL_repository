package by.march8.ecs.application.modules.filemanager.model;

import javax.swing.*;
import java.awt.*;

/**
 * @author Andy 23.01.2019 - 10:50.
 */
public class ColorListRenderer extends DefaultListCellRenderer {

    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index,
                                                  boolean isSelected, boolean cellHasFocus) {
        Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        ColorTextItem item = (ColorTextItem) value;
        if (item != null) {
            setText(item.getName());
            setBackground(item.getColor());
        }
        if (isSelected) {
            setBackground(getBackground().darker());
        } else {
            setBackground(getBackground());
        }
        return c;
    }

}
