package common;

import javax.swing.*;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Класс для JCheckBox в шапке JTable
 *
 * @author lidashka
 */

public class CheckBoxHeader extends JCheckBox implements TableCellRenderer {
    public CheckBoxHeader(JTableHeader header, final int targetColumnIndex, String str) {
        super(str);
        setOpaque(false);
        setFont(header.getFont());
        header.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JTableHeader header = (JTableHeader) e.getSource();
                JTable table = header.getTable();
                TableColumnModel columnModel = table.getColumnModel();
                int viewColumn = columnModel.getColumnIndexAtX(e.getX());
                int modelColumn = table.convertColumnIndexToModel(viewColumn);
                if (modelColumn == targetColumnIndex) {
                    boolean b = !isSelected();
                    setSelected(b);
                    TableModel m = table.getModel();
                    for (int i = 0; i < m.getRowCount(); i++) m.setValueAt(b, i, modelColumn);
                    header.repaint();
                }
            }
        });
    }

    @Override
    public Component getTableCellRendererComponent(JTable tbl, Object val, boolean isS, boolean hasF, int row, int col) {
        TableCellRenderer r = tbl.getTableHeader().getDefaultRenderer();
        JLabel l = (JLabel) r.getTableCellRendererComponent(tbl, val, isS, hasF, row, col);
        l.setIcon(new CheckBoxIcon(this));
        if (l.getPreferredSize().height > 1000) {
            l.setPreferredSize(new Dimension(0, 28));
        }
        return l;
    }

    class CheckBoxIcon implements Icon {
        private final JCheckBox check;

        public CheckBoxIcon(JCheckBox check) {
            this.check = check;
        }

        @Override
        public int getIconWidth() {
            return check.getPreferredSize().width;
        }

        @Override
        public int getIconHeight() {
            return check.getPreferredSize().height;
        }

        @Override
        public void paintIcon(Component c, Graphics g, int x, int y) {
            SwingUtilities.paintComponent(g, check, (Container) c, x, y, getIconWidth(), getIconHeight());
        }
    }
}