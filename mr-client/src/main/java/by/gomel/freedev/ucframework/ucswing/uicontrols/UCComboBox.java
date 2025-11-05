package by.gomel.freedev.ucframework.ucswing.uicontrols;

import by.march8.ecs.application.modules.filemanager.model.ColorTextItem;

import javax.swing.*;
import java.awt.*;

public class UCComboBox<T extends ColorTextItem> extends JComboBox<T> {


    public UCComboBox() {
        super();
        setRenderer(new MyListCellRenderer());
    }

    class MyListCellRenderer<T extends ColorTextItem> extends DefaultListCellRenderer {


        public MyListCellRenderer() {
            setOpaque(true);
        }

        public Component getListCellRendererComponent(JList jc, Object val, int idx,
                                                      boolean isSelected, boolean cellHasFocus) {
            T item = (T) val;
            if (item != null) {
                setText(item.getName());
                if (isSelected) {
                    setBackground(Color.LIGHT_GRAY);
                } else {
                    setBackground(item.getColor());
                }
            }
            return this;
        }
    }
}
