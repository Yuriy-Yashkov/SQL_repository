package by.gomel.freedev.ucframework.ucswing.uicontrols.filelist.model;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

/**
 * @author Andy 18.12.2018 - 14:41.
 */
public class ImageListRenderer extends DefaultListCellRenderer {

    private final Map<String, ImageItem> imageMap;

    public ImageListRenderer(Map<String, ImageItem> map) {
        imageMap = map;
    }

    @Override
    public Component getListCellRendererComponent(
            JList list, Object value, int index,
            boolean isSelected, boolean cellHasFocus) {

        JLabel label = (JLabel) super.getListCellRendererComponent(
                list, value, index, isSelected, cellHasFocus);
        if (label.getIcon() == null) {
            ImageItem imageItem = imageMap.get(value);
            if (imageItem != null) {
                label.setIcon(imageItem.getIcon());
                label.setText("");
                label.setToolTipText(imageItem.getName());
            }
        }
        return label;
    }
}