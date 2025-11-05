package dept.nsi;

import common.PanelWihtFone;
import common.ProgressBar;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 *
 * @author vova
 */

class CustomTableCellRenderer extends DefaultTableCellRenderer {
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        if (isSelected) c.setBackground(Color.LIGHT_GRAY);
        else if ((Boolean) table.getValueAt(row, 0)) {
            c.setBackground(Color.PINK);
        } else {
            c.setBackground(Color.white);
        }
        return c;
    }
}

public class SpravIzdelie extends JDialog {
    JTable table;
    DefaultTableModel tModel;
    Vector columns = new Vector();
    Vector rows = new Vector();
    JScrollPane scrollTable;
    JPanel mainPanel;
    JButton bDelete;
    JLabel lModel;
    JTextField tfModel;
    JDialog thisForm;
    ProgressBar pb;
    int x = 10;
    int y = 10;
    private TableColumn tcol;

    public SpravIzdelie(JFrame parent, boolean f) {
        super(parent, f);
        thisForm = this;

        columns.add("Удалить");
        columns.add("Шифр артикула");
        columns.add("Артикул");
        columns.add("Модель");
        columns.add("Название");
        NsiDB db = new NsiDB();
        rows = new Vector(db.getNSI_KLD());

        initComponents();

        table.getColumnModel().getColumn(0).setPreferredWidth(80);
        table.getColumnModel().getColumn(1).setPreferredWidth(100);
        table.getColumnModel().getColumn(2).setPreferredWidth(100);
        table.getColumnModel().getColumn(3).setPreferredWidth(50);
        table.getColumnModel().getColumn(4).setPreferredWidth(180);
        add(mainPanel);
        setTitle("NSI_KLD");
        this.setSize(635, 560);
        setLocationRelativeTo(parent);
        setVisible(true);
    }

    private void initComponents() {
        mainPanel = new PanelWihtFone();

        tModel = new DefaultTableModel(rows, columns) {
            @Override
            public Class getColumnClass(int column) {
                Class returnValue;
                if ((column >= 0) && (column < getColumnCount())) {
                    returnValue = getValueAt(0, column).getClass();
                } else {
                    returnValue = Object.class;
                }
                return returnValue;
            }
        };
        tModel.addTableModelListener(new TableModelListener() {
            public void tableChanged(TableModelEvent e) {

            }
        });

        table = new JTable(tModel);
        final TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(tModel);
        table.setRowSorter(sorter);

        for (int i = 1; i < table.getColumnCount(); i++) {
            TableColumn tc = table.getColumnModel().getColumn(0);
            tc.setCellEditor(table.getDefaultEditor(Boolean.class));
            tc.setCellRenderer(table.getDefaultRenderer(Boolean.class));
            tcol = table.getColumnModel().getColumn(i);
            tcol.setCellRenderer(new CustomTableCellRenderer());
        }

        scrollTable = new JScrollPane(table);
        scrollTable.setBounds(x, y, 615, 470);
        mainPanel.add(scrollTable);

        bDelete = new JButton("Удалить");
        bDelete.setBounds(x + 290, y + 480, 120, 20);
        bDelete.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                List<List<Object>> elem = new ArrayList<>();
                for (int i = 0; i < tModel.getRowCount(); i++) {
                    if ((Boolean) tModel.getValueAt(i, 0)) {
                        List<Object> v = new ArrayList<>();
                        for (int j = 1; j < 4; j++) {
                            v.add(tModel.getValueAt(i, j));
                        }
                        elem.add(v);
                    }
                }

                int d = JOptionPane.showConfirmDialog(null, "Вы хотите удалить " + elem.size() + " записей?", "Внимание", JOptionPane.OK_CANCEL_OPTION);
                if (d != 0) return;
                pb = new ProgressBar(thisForm, true, "Удаление изделий");
                class SWorker extends SwingWorker<Object, Object> {
                    List elem = new ArrayList();

                    public SWorker(List elem) {
                        this.elem = elem;
                        pb.setMaxValue(elem.size());
                    }

                    @Override
                    protected Object doInBackground() throws Exception {
                        NsiDB db = new NsiDB();
                        if (!db.delNSI_KLD(this.elem, pb)) {
                            JOptionPane.showMessageDialog(null, "Некоторые записи небыли удалены", "Ошибка", JOptionPane.ERROR_MESSAGE);
                        } else
                            JOptionPane.showMessageDialog(null, "Изделия успешно удалены", "Ошибка", JOptionPane.INFORMATION_MESSAGE);
                        rows = new Vector(db.getNSI_KLD());

                        return 0;
                    }

                    @Override
                    protected void done() {
                        try {
                            pb.dispose();
                        } catch (Exception ex) {
                            System.err.println("Ошибка при получении результатов из фонового потока " + ex);
                        }
                    }
                }
                SWorker sw = new SWorker(elem);
                sw.execute();
                pb.setVisible(true);
                    
                 /*Vector row0 = new Vector();
                        row0.add(false);
                        row0.add("");
                        row0.add("");
                        row0.add("");
                        row0.add("");
                        tModel.insertRow(0, row0);*/
                while (tModel.getRowCount() > 1) tModel.removeRow(1);

                for (int i = 0; i < rows.size(); i++) {
                    tModel.addRow(new Vector((Vector) rows.get(i)));
                }
                //tModel.removeRow(0);

            }
        });
        mainPanel.add(bDelete);

        lModel = new JLabel("Модель");
        lModel.setBounds(x + 40, y + 480, 80, 20);
        mainPanel.add(lModel);

        tfModel = new JTextField(9);
        tfModel.setBounds(x + 130, y + 480, 100, 20);
        tfModel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(java.awt.event.KeyEvent evt) {
                if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
                    sorter.setRowFilter(RowFilter.regexFilter("^" + tfModel.getText(), 3));
                } else if (evt.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    tfModel.setText("");
                    sorter.setRowFilter(null);
                }
            }
        });
        mainPanel.add(tfModel);

    }

}
