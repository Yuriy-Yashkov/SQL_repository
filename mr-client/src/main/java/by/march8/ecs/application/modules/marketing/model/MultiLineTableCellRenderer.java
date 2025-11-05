package by.march8.ecs.application.modules.marketing.model;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

/**
 * @author Andy 02.05.2018 - 11:49.
 */
public class MultiLineTableCellRenderer extends JLabel implements TableCellRenderer {

    private Color c1 = new Color(0xF2F1C9);
    private Color c11 = new Color(0xDDF2BB);
    private Color c12 = new Color(0xF2D4BC);


    public MultiLineTableCellRenderer() {
        //setLineWrap(true);
        //setWrapStyleWord(true);
        setOpaque(true);
    }

    public Component getTableCellRendererComponent(JTable table, Object value,
                                                   boolean isSelected, boolean hasFocus, int row, int column) {
        if (isSelected) {
            setBackground(Color.LIGHT_GRAY);
        } else {
            if (column == 7) {
                setBackground(c1);
            } else if (column == 8) {
                setBackground(c11);
            } else if (column == 9) {
                setBackground(c12);
            } else {
                setBackground(Color.white);
            }
        }
        setText((value == null) ? "" : value.toString());

        return this;
    }
}
