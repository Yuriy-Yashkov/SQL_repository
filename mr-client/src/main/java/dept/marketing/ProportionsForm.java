package dept.marketing;

import common.CheckBoxHeader;
import workDB.DB;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.Vector;

public class ProportionsForm extends javax.swing.JDialog {
    Vector col = new Vector();
    Vector razmer = new Vector();
    Vector rost = new Vector();
    JPopupMenu popupMenu = new JPopupMenu("PopupMenu");
    Vector<Vector<Object>> tableData = new Vector<Vector<Object>>();
    DB db = new DB();
    MarketingForm marketingform = null;
    Object sar;
    String nar, nptk, ngpr, color;
    int fason;
    int sort;
    boolean checkbox;
    DefaultTableModel tm;
    TableColumn tc;
    boolean test = false;
    boolean treefasony = false;
    boolean addrst = false;
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JToggleButton jToggleButton1;
    ProportionsForm(javax.swing.JDialog parent, boolean modal, final Object sar, final String nar, String nptk, String ngpr, final int fas, final int srt, final String color, boolean checkbox, boolean treefasony) {
        super(parent, modal);
        initComponents();
        this.setTitle("Размеры");

        marketingform = (MarketingForm) parent;
        this.sar = sar;
        this.nar = nar;
        this.nptk = nptk;
        this.fason = fas;
        this.sort = srt;
        this.color = color;
        this.ngpr = ngpr;
        this.checkbox = checkbox;
        this.treefasony = treefasony;

        createTable();
        createProportionsForm();

        jTable1.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                JTable table = (JTable) e.getSource();
                if (SwingUtilities.isRightMouseButton(e)) {
                    int row = table.rowAtPoint(e.getPoint());
                    int colum = table.columnAtPoint(e.getPoint());
                    if (row >= 0 && (colum == 9 || colum == 10)) {
                        table.changeSelection(row, colum, false, false);
                        if (e.isPopupTrigger()) {
                            popupMenu.removeAll();
                            Vector<String> clients = new Vector<String>();
                            if (Integer.parseInt(table.getValueAt(row, colum).toString()) != 0) {
                                if (colum == 9)
                                    clients = marketingform.pdb.getClientsPostponed(jTable1.getValueAt(jTable1.getSelectedRow(), 1), jTable1.getValueAt(jTable1.getSelectedRow(), 2), fas, srt, color, jTable1.getValueAt(jTable1.getSelectedRow(), 4), jTable1.getValueAt(jTable1.getSelectedRow(), 5));
                                if (colum == 10)
                                    clients = db.getClientsPostponed(jTable1.getValueAt(jTable1.getSelectedRow(), 1), jTable1.getValueAt(jTable1.getSelectedRow(), 2), fas, srt, color, jTable1.getValueAt(jTable1.getSelectedRow(), 4), jTable1.getValueAt(jTable1.getSelectedRow(), 5));
                            }
                            if (!clients.isEmpty()) {
                                for (int i = 0; i < clients.size(); i++) {
                                    popupMenu.add(new JMenuItem(clients.elementAt(i)));
                                }
                            } else
                                popupMenu.add(new JMenuItem("нет записей"));
                            popupMenu.repaint();
                            popupMenu.show(e.getComponent(), e.getX(), e.getY());
                        }
                    }
                }
            }
        });

        jToggleButton1.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (!jToggleButton1.isSelected() && (jTable1.getRowCount() > 0)) {
                    for (int i = 0; i < tableData.size(); i++) {
                        Vector vv = tableData.elementAt(i);
                        vv.remove(vv.size() - 1);
                    }
                    col.remove(col.size() - 1);

                    for (int i = 0; i < tableData.size(); i++) {
                        Vector vv = tableData.elementAt(i);
                        vv.remove(vv.size() - 1);
                    }
                    col.remove(col.size() - 1);

                    tm.fireTableStructureChanged();
                    jTable1.getColumnModel().getColumn(0).setHeaderRenderer(new CheckBoxHeader(jTable1.getTableHeader(), 0, ""));
                    initColumnSizes();
                } else if (jTable1.getRowCount() > 0) {
                    for (int i = 0; i < tableData.size(); i++) {
                        Vector vv = tableData.elementAt(i);
                        vv.addElement(marketingform.pdb.getOstatokPostponed(vv.get(1), vv.get(2), fas, srt, vv.get(4), vv.get(5), color));
                    }
                    tm.addColumn("Отложено");
                    jTable1.getColumnModel().getColumn(9).setPreferredWidth(60);

                    db.createNDocTable();
                    for (int i = 0; i < tableData.size(); i++) {
                        Vector vv = tableData.elementAt(i);
                        vv.addElement(db.getOstatokPostponed(vv.get(1), vv.get(2), fas, srt, vv.get(4), vv.get(5), color));
                    }
                    tm.addColumn("Собрано");
                    jTable1.getColumnModel().getColumn(10).setPreferredWidth(70);

                    tm.fireTableStructureChanged();
                    jTable1.getColumnModel().getColumn(0).setHeaderRenderer(new CheckBoxHeader(jTable1.getTableHeader(), 0, ""));
                    initColumnSizes();
                }
            }
        });

        jLabel1.setText("Модель:  " + fas);

        if (treefasony || checkbox)
            jButton4.setVisible(false);

        setLocationRelativeTo(parent);
        setResizable(false);
        setVisible(true);
    }

    private void createProportionsForm() {
        jButton2.setIcon(MarketingForm.createIcon("/Img/camera_test.png"));
        jLabel3.setText("ИТОГО:     " + new DecimalFormat("###,###.###").format(0) + " шт.");

        jTextField1.setDocument(new PlainDocument() {
            @Override
            public void insertString(int offset, String str, AttributeSet attr) throws BadLocationException {
                if (str == null)
                    return;
                if ((getLength() + str.length()) < 9) {
                    super.insertString(offset, str, attr);
                }
            }
        });

        jTextField1.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                String s = jTextField1.getText().trim();
                char[] ch = s.toCharArray();
                for (int i = 0; i < ch.length; i++)
                    if (!Character.isDigit(ch[i])) s = s.replace(ch[i], ' ');
                jTextField1.setText(s.replace(" ", ""));
            }
        });

        jTextField2.setDocument(new PlainDocument() {
            @Override
            public void insertString(int offset, String str, AttributeSet attr) throws BadLocationException {
                if (str == null)
                    return;
                if ((getLength() + str.length()) < 100) {
                    super.insertString(offset, str, attr);
                }
            }
        });

        addWindowListener(new WindowListener() {
            public void windowClosed(WindowEvent e) {
            }

            public void windowClosing(WindowEvent e) {
            }

            public void windowOpened(WindowEvent e) {
            }

            public void windowIconified(WindowEvent e) {
            }

            public void windowDeiconified(WindowEvent e) {
            }

            public void windowActivated(WindowEvent e) {
            }

            public void windowDeactivated(WindowEvent e) {
            }
        });
    }

    private void createTable() {
        col.add("");
        col.add("шифр ар.");
        col.add("артикул");
        col.add("изделие");
        col.add("Рост");
        col.add("Размер");
        col.add("Цена");
        col.add("Остаток");
        col.add("Кол-во");

        if (!nar.equals("0"))
            tableData = db.getRazmer(sar, nar, nptk, fason, sort, color, CartForm.kodValutaSetting, CartForm.kursSetting, checkbox);
        else
            tableData = db.getAllRazmer(sar, nar, ngpr, marketingform.getRzm1(), marketingform.getRzm2(), marketingform.getRst1(), marketingform.getRst2());

        tm = new DefaultTableModel(tableData, col) {
            private static final long serialVersionUID = 1L;

            @SuppressWarnings("unchecked")
            @Override
            public Class<?> getColumnClass(int col) {
                Vector row = (Vector<Vector<?>>) dataVector.get(0);
                return row.get(col).getClass();
            }

            @Override
            public boolean isCellEditable(int row, int col) {
                return col == 0 || col == 8;
            }

            @Override
            public void setValueAt(Object value, int row, int col) {
                if (value != null) {
                    boolean flag = true;
                    if (col == 8) {
                        flag = false;
                        if (Integer.parseInt(value.toString()) > 0)
                            flag = true;
                        else
                            JOptionPane.showMessageDialog(null, "В столбце 'Кол-во':\n   *нельзя оставлять пустые строки;\n   *можно задавать только положительные значения;", "Предупреждение", javax.swing.JOptionPane.WARNING_MESSAGE);
                    }
                    if (flag) {
                        Vector rowVector = (Vector) dataVector.elementAt(row);
                        rowVector.setElementAt(value, col);
                        fireTableCellUpdated(row, col);
                        long sum = 0;
                        for (int i = 0; i < tableData.size(); i++) {
                            if (jTable1.getValueAt(i, 0).equals(true))
                                sum += Long.parseLong(jTable1.getValueAt(i, 8).toString());
                        }
                        jLabel3.setText("ИТОГО:     " + new DecimalFormat("###,###.###").format(sum) + " шт.");
                        jLabel3.repaint();
                    }
                }
            }
        };
        jTable1.setModel(tm);
        jTable1.setAutoCreateColumnsFromModel(true);
        initColumnSizes();

        for (int row = 0; row < jTable1.getRowCount(); row++) {
            DefaultCellEditor editor = (DefaultCellEditor) jTable1.getCellEditor(row, 7);
            editor.setClickCountToStart(1);
        }

        if (!tableData.isEmpty()) {
            final TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(tm);
            jTable1.setRowSorter(sorter);

            jTable1.getColumnModel().getColumn(0).setHeaderRenderer(new CheckBoxHeader(jTable1.getTableHeader(), 0, ""));
        }
    }

    private void initColumnSizes() {
        TableColumn column = null;
        for (int i = 0; i < jTable1.getColumnCount(); i++) {
            column = jTable1.getColumnModel().getColumn(i);
            switch (i) {
                case 0:
                    column.setPreferredWidth(20);
                    break;
                case 1:
                case 2:
                case 3:
                    if (treefasony)
                        column.setPreferredWidth(60);
                    else {
                        column.setMinWidth(0);
                        column.setMaxWidth(0);
                    }
                    break;
                case 4:
                case 5:
                case 8:
                    column.setPreferredWidth(20);
                    break;
                case 6:
                    column.setPreferredWidth(50);
                    break;
                case 7:
                    column.setPreferredWidth(30);
                    break;
                case 9:
                case 10:
                    column.setPreferredWidth(40);
                    break;
                default:
                    break;
            }
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jButton2 = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jTextField2 = new javax.swing.JTextField();
        jToggleButton1 = new javax.swing.JToggleButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jButton4 = new javax.swing.JButton();
        jCheckBox1 = new javax.swing.JCheckBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jButton2.setFont(new java.awt.Font("Dialog", 0, 13));
        jButton2.setText("В корзину");
        jButton2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton1.setFont(new java.awt.Font("Dialog", 0, 13));
        jButton1.setText("Отмена");
        jButton1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton3.setText("Изменить");
        jButton3.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);

        jLabel2.setText("Кол-во:");

        jLabel4.setText("Примечание:");

        jTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField1ActionPerformed(evt);
            }
        });

        jToggleButton1.setFont(new java.awt.Font("Dialog", 0, 13));
        jToggleButton1.setText("Детали");

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][]{
                        {null, null, null, null},
                        {null, null, null, null},
                        {null, null, null, null},
                        {null, null, null, null}
                },
                new String[]{
                        "Title 1", "Title 2", "Title 3", "Title 4"
                }
        ));
        jTable1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jScrollPane1.setViewportView(jTable1);

        jButton4.setFont(new java.awt.Font("Dialog", 0, 13)); // NOI18N
        jButton4.setText("Рост \"0\"");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jCheckBox1.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        jCheckBox1.setText("Без артикула;");
        jCheckBox1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jCheckBox1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 524, Short.MAX_VALUE)
                                .addContainerGap())
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 524, Short.MAX_VALUE)
                                .addContainerGap())
                        .addGroup(layout.createSequentialGroup()
                                .addGap(107, 107, 107)
                                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(24, 24, 24)
                                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(128, 128, 128))
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(6, 6, 6)
                                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 18, Short.MAX_VALUE)
                                .addComponent(jCheckBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap())
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                                .addComponent(jToggleButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                                .addComponent(jLabel4)
                                                .addGap(18, 18, 18)
                                                .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 414, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(18, 18, 18))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jCheckBox1)
                                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jButton3))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 308, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                .addComponent(jToggleButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(layout.createSequentialGroup()
                                                .addGap(5, 5, 5)
                                                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(12, 12, 12)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 15, Short.MAX_VALUE)
                                        .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        if (marketingform.getNameKlient().equals("не выбран")) {
            JOptionPane.showMessageDialog(null, "Вы не выбрали заказчика!", "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        } else {
            boolean yes = false;
            int count = 0;
            int tableSize = jTable1.getRowCount();

            while (tableSize-- > 0) {
                if ((jTable1.getValueAt(tableSize, 0)).equals(true)) {
                    count++;
                    break;
                }
            }

            if (count > 0) {
                for (int i = 0; i < jTable1.getRowCount(); i++) {
                    if ((jTable1.getValueAt(i, 0)).equals(true) && jTable1.getValueAt(i, 8) != null) {
                        if (Integer.parseInt(jTable1.getValueAt(i, 7).toString()) >= Integer.parseInt(jTable1.getValueAt(i, 8).toString()) || yes) {
                            marketingform.pdb.addInCart(Integer.parseInt(jTable1.getValueAt(i, 1).toString()), jTable1.getValueAt(i, 2).toString(), jTable1.getValueAt(i, 3).toString(), fason, sort,
                                    Integer.parseInt(jTable1.getValueAt(i, 4).toString()), Integer.parseInt(jTable1.getValueAt(i, 5).toString()), color, Integer.parseInt(jTable1.getValueAt(i, 8).toString()), jTextField2.getText(), !jCheckBox1.isSelected());
                        } else {
                            switch (JOptionPane.showOptionDialog(null, "Остаток (" + Integer.parseInt(jTable1.getValueAt(i, 4).toString()) + " - " + Integer.parseInt(jTable1.getValueAt(i, 5).toString()) + ") на складе меньше, чем заданное кол-во!\n В корзину?", "Предупреждение",
                                    JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE, null, new Object[]{"Да", "Нет", "Да - для всех", "Нет - для всех"}, new Object[]{"Да", "Нет", "Да - для всех", "Нет - для всех"})) {
                                case 0:
                                    marketingform.pdb.addInCart(Integer.parseInt(jTable1.getValueAt(i, 1).toString()), jTable1.getValueAt(i, 2).toString(), jTable1.getValueAt(i, 3).toString(), fason, sort,
                                            Integer.parseInt(jTable1.getValueAt(i, 4).toString()), Integer.parseInt(jTable1.getValueAt(i, 5).toString()), color, Integer.parseInt(jTable1.getValueAt(i, 8).toString()), jTextField2.getText(), !jCheckBox1.isSelected());
                                    break;
                                case 2:
                                    i--;
                                    yes = true;
                                    break;
                                case 3:
                                    i = jTable1.getRowCount();
                                    dispose();
                                    break;
                                default:
                                    break;
                            }
                        }
                    }
                }
                dispose();
            } else
                JOptionPane.showMessageDialog(null, "Ничего не выбрано!", "Предупреждение", javax.swing.JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        dispose();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        try {
            for (int i = 0; i < jTable1.getRowCount(); i++) {
                if ((jTable1.getValueAt(i, 0)).equals(true) && !jTextField1.getText().equals("") && jTextField1.getText() != null)
                    jTable1.setValueAt(Long.parseLong(jTextField1.getText().trim()), i, 8);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ошибка! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed
        jButton3ActionPerformed(evt);
    }//GEN-LAST:event_jTextField1ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        if (!addrst) {
            addrst = true;
            Vector rst = new Vector();
            boolean addrowrst;
            int row = 0;

            for (int i = 0; i < jTable1.getRowCount(); i++) {
                if (!jTable1.getValueAt(i, 4).toString().equals("0")) {
                    addrowrst = true;
                    Vector tmp = new Vector();
                    tmp.add(false);
                    tmp.add(jTable1.getValueAt(i, 1));                          //шифр ар.
                    tmp.add(jTable1.getValueAt(i, 2));                          //артикул
                    tmp.add(jTable1.getValueAt(i, 3));                          //изделие
                    tmp.add(0);                                                 //Рост
                    tmp.add(jTable1.getValueAt(i, 5));                          //Размер
                    tmp.add(0);                                                 //Цена
                    tmp.add(0);                                                 //Остаток
                    tmp.add(1);                                                 //Кол-во
                    tmp.add(0);                                                 //Отложено
                    tmp.add(0);                                                 //Собрано

                    if (!rst.contains(tmp)) {
                        for (int j = 0; j < jTable1.getRowCount(); j++) {
                            if (jTable1.getValueAt(j, 4).toString().equals("0") &&
                                    jTable1.getValueAt(j, 5).toString().equals(jTable1.getValueAt(i, 5).toString()))
                                addrowrst = false;
                        }
                        if (addrowrst) rst.add(tmp);
                    }
                }
            }

            for (Iterator it = rst.iterator(); it.hasNext(); ) {
                Vector object = (Vector) it.next();
                tm.insertRow(row++, object);
            }

            jTable1.scrollRectToVisible(jTable1.getCellRect(0, 0, true));
        }
    }//GEN-LAST:event_jButton4ActionPerformed
    // End of variables declaration//GEN-END:variables
}
