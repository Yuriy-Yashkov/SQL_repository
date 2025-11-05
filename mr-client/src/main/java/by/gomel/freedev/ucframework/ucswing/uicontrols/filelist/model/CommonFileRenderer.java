package by.gomel.freedev.ucframework.ucswing.uicontrols.filelist.model;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.io.File;

/**
 * @author Andy 18.12.2018 - 12:07.
 */
public class CommonFileRenderer extends DefaultListCellRenderer {

    private Border border = new EmptyBorder(3, 3, 3, 3);


    @Override
    public Component getListCellRendererComponent(
            JList list,
            Object value,
            int index,
            boolean isSelected,
            boolean cellHasFocus) {

        Component c = super.getListCellRendererComponent(
                list, value, index, isSelected, cellHasFocus);
        JLabel label = (JLabel) c;
        File file = (File) value;

        label.setText(file.getName());
        label.setIcon(FileSystemView.getFileSystemView().getSystemIcon(file));
        label.setBorder(border);
        return label;
    }
}