package by.march8.ecs.application.modules.plan.render;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

/**
 * @author Andy 24.03.2016.
 */
public class PlanFontCellRenderer extends DefaultTableCellRenderer implements TableCellRenderer {

    private Font font;
    private Color color;

    public PlanFontCellRenderer(Font font, Color color) {
        super();
        this.font = font;
        this.color = color;
    }


    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        final Component comp = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        comp.setFont(font);

        comp.setForeground(color);
        return comp;
    }
}
