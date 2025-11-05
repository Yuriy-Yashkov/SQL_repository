package dept.sklad;

import common.PanelWihtFone;
import common.ProgressBar;
import workDB.DB;
import workDB.PDB;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

/**
 *
 * @author vova
 */
public class NakladnieDescr extends JDialog {

    JLabel lHead;
    JLabel lFoot = new JLabel();
    JLabel lOldSum;
    JLabel lNewSum;
    JTable table;
    JButton bClose;
    JButton bChange;
    JButton bProtocol;
    JPanel mainPanel;
    DefaultTableModel tModel;
    JScrollPane scrollTable;
    int x = 10;
    int y = 10;
    Vector columns = new Vector();
    Vector rows = new Vector();
    Vector temp = new Vector();
    String ndoc;
    String client;
    ProgressBar pb;
    private long oldSum = 0;
    private double newSum = 0;
    private float cena = 0;
    private JDialog thisD;
    private boolean tableWasChanged;
    private HashMap id_item = new HashMap();

    public NakladnieDescr(JDialog parent, boolean f, String count, String ndoc, String c) {
        super(parent, f);
        thisD = this;
        client = c;

        this.ndoc = ndoc;
        lFoot.setText(count);

        DB db = new DB();
        temp = db.getNaklAllDescr(ndoc, 1);

        PDB pdb = new PDB();
        Iterator it = temp.iterator();
        while (it.hasNext()) {
            Vector v = (Vector) it.next();
            Vector vNew = new Vector();
            vNew.add(v.get(0));
            vNew.add(v.get(12));
            vNew.add(v.get(2));
            vNew.add(v.get(3));
            vNew.add(v.get(4));
            vNew.add(v.get(5));
            vNew.add(v.get(6));
            vNew.add(v.get(7));
            oldSum += (Long) v.get(7);
            cena = pdb.getSstoimost((Long) v.get(12), (Long) v.get(13));
            newSum += cena;
            vNew.add(Math.round(cena));
            vNew.add(v.get(14));
            rows.add(vNew);
        }
        pdb.disConn();

        columns.add("Модель");
        columns.add("Артикул");
        columns.add("Наименование");
        columns.add("Размер");
        columns.add("Сорт");
        columns.add("Цвет");
        columns.add("Кол");
        columns.add("Цена ед.");
        columns.add("Новая Цена");
        columns.add("");

        initComponents();

        lHead.setText(lHead.getText() + ndoc);
        table.getColumnModel().getColumn(0).setPreferredWidth(40);
        table.getColumnModel().getColumn(1).setPreferredWidth(70);
        table.getColumnModel().getColumn(2).setPreferredWidth(150);
        table.getColumnModel().getColumn(3).setPreferredWidth(100);
        table.getColumnModel().getColumn(4).setPreferredWidth(25);
        table.getColumnModel().getColumn(5).setPreferredWidth(120);
        table.getColumnModel().getColumn(6).setPreferredWidth(20);
        table.getColumnModel().getColumn(9).setPreferredWidth(0);
        /*table.getColumnModel().getColumn(9).setWidth(0);
        table.getColumnModel().getColumn(9).setMinWidth(0);
        table.getColumnModel().getColumn(9).setMaxWidth(0);
        */
        add(mainPanel);

        setSize(25 + 280 + columns.size() * 70, 540);
        setLocationRelativeTo(parent);
        setResizable(false);
        setTitle("Накладные на отгрузку");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    private void initComponents() {
        mainPanel = new PanelWihtFone();

        lHead = new JLabel("Накладная на отгрузку № ");
        lHead.setBounds(x + 100, y, 250, 20);
        mainPanel.add(lHead);

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
            public boolean isCellEditable(int row, int column) {
                return (column == 7);
            }

            @Override
            public void setValueAt(Object value, int row, int column) {
                if (column == 7) {
                    float v = Float.parseFloat(value.toString());
                    if (0 <= v) {
                        super.setValueAt(value, row, column);
                        id_item.put(Integer.parseInt(tModel.getValueAt(row, 9).toString()), Float.parseFloat(tModel.getValueAt(row, 7).toString()));
                        tableWasChanged = true;
                    } else
                        JOptionPane.showMessageDialog(null, "Цена должна быть больше 0", "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
                }
            }
        };
        table = new JTable(tModel);
        final TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(tModel);
        table.setRowSorter(sorter);

        scrollTable = new JScrollPane(table);
        scrollTable.setBounds(x, y + 25, 280 + columns.size() * 70, 400);
        mainPanel.add(scrollTable);

        lFoot.setBounds(x, y + 430, 500, 20);
        mainPanel.add(lFoot);

        lOldSum = new JLabel("Старая цена: " + oldSum);
        lOldSum.setBounds((25 + 280 + columns.size() * 70) / 2 + 100, 440, 200, 20);
        lNewSum = new JLabel(" Новая цена: " + Math.round(newSum));
        mainPanel.add(lOldSum);
        lNewSum.setBounds((25 + 280 + columns.size() * 70) / 2 + 100, 460, 200, 20);
        mainPanel.add(lNewSum);

        bClose = new JButton("Закрыть");
        bClose.setBounds((25 + 280 + columns.size() * 70) / 2 - 60, 470, 120, 20);
        bClose.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (tableWasChanged) {
                    if (JOptionPane.showConfirmDialog(null, "Сохранить изменения таблицы?", "Вопрос", javax.swing.JOptionPane.OK_CANCEL_OPTION) == 0) {
                        pb = new ProgressBar(thisD, true, "Сохранение цен");
                        class SWorker extends SwingWorker<Object, Object> {
                            public SWorker() {
                                pb.setMaxValue(id_item.size() + 1);
                            }

                            @Override
                            protected Object doInBackground() throws Exception {
                                SkladDB db = new SkladDB();
                                try {
                                    int proc = 0;
                                    for (int i = 0; i < tModel.getRowCount(); i++) {
                                        if (id_item.containsKey(Integer.parseInt(tModel.getValueAt(i, 9).toString()))) {
                                            db.setNewPrice(ndoc, Integer.parseInt(tModel.getValueAt(i, 9).toString()), Float.parseFloat(id_item.get(Integer.parseInt(tModel.getValueAt(i, 9).toString())).toString()));
                                            pb.setProgress(++proc);
                                        }
                                    }
                                    db.updateSumTTN(ndoc);
                                    pb.setProgress(id_item.size() + 1);
                                } catch (Exception ex) {
                                    JOptionPane.showMessageDialog(null, ex.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
                                } finally {
                                    db.disConn();
                                }

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
                        SWorker sw = new SWorker();
                        sw.execute();
                        pb.setVisible(true);
                    }
                }
                dispose();
            }
        });
        mainPanel.add(bClose);

        bChange = new JButton("Изменить");
        bChange.setBounds((25 + 280 + columns.size() * 70) / 2 + 300, 470, 120, 20);
        bChange.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                DB db = new DB();
                try {
                    db.setPriceNakl(ndoc, rows);
                    JOptionPane.showMessageDialog(null, "Цены изменены ", "Готово", javax.swing.JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception er) {
                    JOptionPane.showMessageDialog(null, "Ошибка при изменении цены: " + er.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
                } finally {
                    dispose();
                }

            }
        });
        mainPanel.add(bChange);

        bProtocol = new JButton("Протокол согласования");
        bProtocol.setBounds((25 + 280 + columns.size() * 70) / 2 - 400, 470, 220, 20);
        bProtocol.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new ProtSogl(thisD, true, ndoc, client);
            }
        });
        mainPanel.add(bProtocol);
    }

}
