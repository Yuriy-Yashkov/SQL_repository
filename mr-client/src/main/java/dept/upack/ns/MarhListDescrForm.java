package dept.upack.ns;

import common.PanelWihtFone;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

/**
 *
 * @author vova
 * @date 17.11.2011
 */

class CustomTableCellRenderer2 extends DefaultTableCellRenderer {
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        setHorizontalAlignment(SwingConstants.CENTER);
        return c;
    }
}

public class MarhListDescrForm extends JDialog {

    JTable table;
    JButton bClose;
    JPanel mainPanel;
    DefaultTableModel tModel;
    JScrollPane scrollTable;
    int x = 10;
    int y = 10;
    Vector columns = new Vector();
    Vector rows = new Vector();
    int kod_marh;
    JDialog temp;
    boolean tableWasChanged;
    private TableColumn tcol;


    public MarhListDescrForm(JDialog parent, boolean f, int kod_marh) {
        super(parent, f);
        temp = this;
        this.kod_marh = kod_marh;

        try {
            UpackNSDB db = new UpackNSDB();
            rows = db.getMarhListDescr(kod_marh);
            db.disConn();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
        columns.add("Наименование");
        columns.add("Модель");
        columns.add("Артикул");
        columns.add("Сорт");
        columns.add("Рост");
        columns.add("Размер");
        columns.add("Кол-во");
        columns.add("Скидка");
        columns.add("Бар код");

        initComponents();

        table.getColumnModel().getColumn(0).setPreferredWidth(180);
        table.getColumnModel().getColumn(1).setPreferredWidth(50);
        table.getColumnModel().getColumn(2).setPreferredWidth(80);
        table.getColumnModel().getColumn(3).setPreferredWidth(30);
        table.getColumnModel().getColumn(4).setPreferredWidth(30);
        table.getColumnModel().getColumn(5).setPreferredWidth(40);
        table.getColumnModel().getColumn(6).setPreferredWidth(40);
        table.getColumnModel().getColumn(7).setPreferredWidth(40);
        table.getColumnModel().getColumn(8).setPreferredWidth(0);
        table.getColumnModel().getColumn(8).setMinWidth(0);
        table.getColumnModel().getColumn(8).setMaxWidth(0);

        add(mainPanel);

        setSize(20 + columns.size() * 70, 350);
        setLocationRelativeTo(parent);
        setResizable(false);
        setTitle("Маршрутный лист " + kod_marh);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    private void initComponents() {
        mainPanel = new PanelWihtFone();

        //создаём таблицу
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

            @Override
            public void setValueAt(Object value, int row, int column) {
                if (column == 7) {
                    int v = Integer.parseInt(value.toString());
                    if (0 <= v && v <= 100) {
                        super.setValueAt(value, row, column);
                        tableWasChanged = true;
                    } else
                        JOptionPane.showMessageDialog(null, "Скидка должна быть от 0 до 100", "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
                }
            }
        };

        table = new JTable(tModel);
        final TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(tModel);
        table.setRowSorter(sorter);
        for (int i = 1; i < table.getColumnCount(); i++) {
            tcol = table.getColumnModel().getColumn(i);
            tcol.setCellRenderer(new CustomTableCellRenderer2());
        }

        scrollTable = new JScrollPane(table);
        scrollTable.setBounds(x, y, columns.size() * 70, 280);
        mainPanel.add(scrollTable);

        bClose = new JButton("Закрыть");
        bClose.setBounds((20 + columns.size() * 70) / 2 - 60, 295, 120, 20);
        bClose.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (tableWasChanged) {
                    if (JOptionPane.showConfirmDialog(null, "Сохранить изменения таблицы?", "Вопрос", javax.swing.JOptionPane.OK_CANCEL_OPTION) == 0) {
                        UpackNSDB db = new UpackNSDB();
                        try {
                            for (int i = 0; i < tModel.getRowCount(); i++) {
                                db.setSkidka(tModel.getValueAt(i, 8).toString(), Integer.parseInt(tModel.getValueAt(i, 7).toString()));
                            }
                        } catch (Exception ex) {
                            JOptionPane.showMessageDialog(null, ex.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
                        } finally {
                            db.disConn();
                        }
                    }
                }
                dispose();
            }
        });
        mainPanel.add(bClose);
    }
}
