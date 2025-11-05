package dept.sklad.ho;

import common.CheckBoxHeader;
import common.UtilFunctions;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Vector;

/**
 *
 * @author lidashka
 */
public class SmallTableForm extends javax.swing.JDialog {
    private JPanel osnova;
    private JPanel centerPanel;
    private JPanel buttPanel;
    private JButton buttClose;
    private JButton buttPrint;
    private JButton buttDelete;

    private JLabel title;
    private Vector col;
    private JTable table;
    private DefaultTableModel tModel;
    private TableRowSorter<TableModel> sorter;
    private SkladHOPDB spdb;
    private Vector dataTable;

    public SmallTableForm(CenaTMCSkHOForm parent, boolean modal, String text, Vector dataTable) {
        super(parent, modal);
        setTitle("Изменение цены");

        setPreferredSize(new Dimension(600, 400));

        init();

        title.setText(text);

        col.add("");
        col.add("idPrice");
        col.add("Дата");
        col.add("Цена");
        col.add("Дата корр.");
        col.add("Автор корр.");

        createDefaultТable(dataTable, col);

        table.getColumnModel().getColumn(1).setMinWidth(0);
        table.getColumnModel().getColumn(1).setMaxWidth(0);

        setLocationRelativeTo(parent);
        setVisible(true);
    }

    public SmallTableForm(CenaTMCSkHOForm parent, boolean modal, Vector dataTable, String text) {
        super(parent, modal);
        setTitle("Актуальные цены");

        setPreferredSize(new Dimension(750, 550));

        init();

        title.setText("Цены на " + text);

        col.add("");
        col.add("№");
        col.add("Вид");
        col.add("Пр-во");
        col.add("Шифр");
        col.add("Название");
        col.add("Артикул");
        col.add("Цена");
        col.add("Дата");

        createDefaultТable(dataTable, col);

        setLocationRelativeTo(parent);
        setVisible(true);
    }

    public SmallTableForm(SpravTMCSkHOForm parent, boolean modal, String date, Vector data) {
        super(parent, modal);
        setTitle("Закрытые остатки");

        setPreferredSize(new Dimension(750, 500));

        init();

        setTitle("Остатки ТМЦ на " + date);

        col.add("");
        col.add("Группа");
        col.add("Koд ТМЦ");
        col.add("Название");
        col.add("Артикул");
        col.add("Шифр");
        col.add("Вид");
        col.add("Пр-во");
        col.add("Ед.изм.");
        col.add("Нач/п. Ф");
        col.add("Приход Ф");
        col.add("Расход Ф");
        col.add("Итого Ф");
        col.add("Итого K");
        col.add("Куски");

        createDefaultТable(data, col);

        table.getColumnModel().getColumn(5).setMinWidth(0);
        table.getColumnModel().getColumn(5).setMaxWidth(0);
        table.getColumnModel().getColumn(6).setMinWidth(0);
        table.getColumnModel().getColumn(6).setMaxWidth(0);
        table.getColumnModel().getColumn(7).setMinWidth(0);
        table.getColumnModel().getColumn(7).setMaxWidth(0);
        table.getColumnModel().getColumn(8).setMinWidth(0);
        table.getColumnModel().getColumn(8).setMaxWidth(0);
        table.getColumnModel().getColumn(10).setMinWidth(0);
        table.getColumnModel().getColumn(10).setMaxWidth(0);
        table.getColumnModel().getColumn(11).setMinWidth(0);
        table.getColumnModel().getColumn(11).setMaxWidth(0);
        table.getColumnModel().getColumn(14).setMinWidth(0);
        table.getColumnModel().getColumn(14).setMaxWidth(0);

        setLocationRelativeTo(parent);
        setVisible(true);
    }

    public SmallTableForm(SkladHOForm parent, boolean modal) {
        super(parent, modal);
        setTitle("Закрытые остатки");

        setPreferredSize(new Dimension(600, 500));

        init();

        col.add("");
        col.add("Дата");
        col.add("Автор ввода");
        col.add("Дата ввода");

        buttDelete = new JButton("Удалить");
        buttDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                try {
                    if (table.getSelectedRow() != -1) {
                        spdb = new SkladHOPDB();

                        if (spdb.deletePeriodOstSkladHO(UtilFunctions.convertDateStrToLong(table.getValueAt(table.getSelectedRow(), 1).toString().replace("-", ".")))) {

                            JOptionPane.showMessageDialog(null, "Закрытый период успешно удален! ", "Завершено", javax.swing.JOptionPane.INFORMATION_MESSAGE);

                            createPeriodOstTable(col);
                        }
                    } else
                        JOptionPane.showMessageDialog(null, "Вы не выбрали запись!", "Внимание", javax.swing.JOptionPane.WARNING_MESSAGE);

                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, e.getMessage(), "Ошибка ", javax.swing.JOptionPane.ERROR_MESSAGE);
                } finally {
                    spdb.disConn();
                }
            }

        });

        buttPanel.add(new JLabel());
        buttPanel.add(buttDelete);

        createPeriodOstTable(col);

        setLocationRelativeTo(parent);
        setVisible(true);
    }

    public SmallTableForm(SkladOstDataForm parent, boolean modal, String text, Vector dataTable, int flagVid) {
        super(parent, modal);

        setPreferredSize(new Dimension(600, 500));

        init();

        if (flagVid != 3) {
            setTitle("ТМЦ №" + text);

            col.add("");
            col.add("Тип");
            col.add("№");
            col.add("Дата");
            col.add("Группа");
            col.add("Koд ТМЦ");
            col.add("Название");
            col.add("Артикул");
            col.add("Шифр");
            col.add("Вид");
            col.add("Пр-во");
            col.add("Ед.изм.");
            col.add("Итого Ф");
            col.add("Итого K");
            col.add("Куски");
        } else {
            setTitle("Модель " + text);

            col.add("");
            col.add("Тип");
            col.add("№");
            col.add("Дата");
            col.add("Модель");
            col.add("Название");
            col.add("idВид");
            col.add("Вид");
            col.add("Итого Ф");
        }

        createDefaultТable(dataTable, col);

        if (flagVid != 3) {
            table.getColumnModel().getColumn(4).setMinWidth(0);
            table.getColumnModel().getColumn(4).setMaxWidth(0);
            table.getColumnModel().getColumn(5).setMinWidth(0);
            table.getColumnModel().getColumn(5).setMaxWidth(0);
            table.getColumnModel().getColumn(6).setMinWidth(0);
            table.getColumnModel().getColumn(6).setMaxWidth(0);
            table.getColumnModel().getColumn(7).setMinWidth(0);
            table.getColumnModel().getColumn(7).setMaxWidth(0);
            table.getColumnModel().getColumn(8).setMinWidth(0);
            table.getColumnModel().getColumn(8).setMaxWidth(0);
            table.getColumnModel().getColumn(9).setMinWidth(0);
            table.getColumnModel().getColumn(9).setMaxWidth(0);
            table.getColumnModel().getColumn(10).setMinWidth(0);
            table.getColumnModel().getColumn(10).setMaxWidth(0);
        }

        setLocationRelativeTo(parent);
        setVisible(true);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 300, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables

    private void init() {
        setMinimumSize(new Dimension(350, 350));

        osnova = new JPanel();
        centerPanel = new JPanel();
        buttPanel = new JPanel();

        title = new JLabel();
        buttClose = new JButton("Закрыть");
        buttPrint = new JButton("Печать");
        col = new Vector();
        table = new JTable();
        tModel = new DefaultTableModel();

        osnova.setLayout(new BorderLayout(1, 1));
        osnova.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        centerPanel.setLayout(new BorderLayout(1, 1));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        buttPanel.setLayout(new GridLayout(0, 4, 5, 5));
        buttPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        buttPanel.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);

        title.setHorizontalAlignment(JLabel.CENTER);
        title.setFont(new Font("serif", Font.PLAIN, 14));

        table.setAutoCreateColumnsFromModel(true);
        table.getTableHeader().setReorderingAllowed(false);

        buttPrint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttPrintActionPerformed(evt);
            }
        });

        buttClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttCloseTableActionPerformed(evt);
            }
        });

        centerPanel.add(new JScrollPane(table), BorderLayout.CENTER);

        buttPanel.add(buttClose);
        buttPanel.add(buttPrint);

        osnova.add(title, BorderLayout.NORTH);
        osnova.add(centerPanel, BorderLayout.CENTER);
        osnova.add(buttPanel, BorderLayout.SOUTH);

        getContentPane().add(osnova);
        pack();
    }

    private void buttPrintActionPerformed(ActionEvent evt) {
        try {
            SkladHOOO oo = new SkladHOOO(title.getText(), tModel, table.getColumnModel());
            oo.createReport("DefaultTableAlbumFormatCheck.ots");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Ошибка ", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buttCloseTableActionPerformed(ActionEvent evt) {
        dispose();
    }

    private void createDefaultТable(final Vector row, Vector col) {
        tModel = new DefaultTableModel(row, col) {
            @Override
            public Class<?> getColumnClass(int col) {
                if (row.isEmpty())
                    return super.getClass();
                else
                    return getValueAt(0, col).getClass();
            }

            @Override
            public boolean isCellEditable(int row, int col) {
                return col == 0 ? true : false;
            }
        };

        table.setModel(tModel);
        table.setAutoCreateColumnsFromModel(true);

        table.getColumnModel().getColumn(0).setPreferredWidth(1);

        sorter = new TableRowSorter<TableModel>(tModel);
        table.setRowSorter(sorter);
        table.getColumnModel().getColumn(0).setHeaderRenderer(new CheckBoxHeader(table.getTableHeader(), 0, ""));
    }


    private void createPeriodOstTable(Vector col) {
        dataTable = new Vector();
        try {
            spdb = new SkladHOPDB();

            dataTable = spdb.getAllPeriodOstSkladHO();
        } catch (Exception e) {
            dataTable = new Vector();
            JOptionPane.showMessageDialog(null, e.getMessage(), "Ошибка ", javax.swing.JOptionPane.ERROR_MESSAGE);
        } finally {
            spdb.disConn();
        }

        tModel = new DefaultTableModel(dataTable, col) {
            @Override
            public Class<?> getColumnClass(int col) {
                if (dataTable.isEmpty())
                    return super.getClass();
                else
                    return getValueAt(0, col).getClass();
            }

            @Override
            public boolean isCellEditable(int row, int col) {
                return col == 0 ? true : false;
            }
        };

        table.setModel(tModel);
        table.setAutoCreateColumnsFromModel(true);

        table.getColumnModel().getColumn(0).setPreferredWidth(1);

        sorter = new TableRowSorter<TableModel>(tModel);
        table.setRowSorter(sorter);
        table.getColumnModel().getColumn(0).setHeaderRenderer(new CheckBoxHeader(table.getTableHeader(), 0, ""));
    }
}
