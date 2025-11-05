package dept.marketing.cena;

import common.CheckBoxHeader;
import common.ProgressBar;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

/**
 *
 * @author lidashka
 */
public class ChangeForm extends JDialog {
    CenaForm cenaform;

    JPanel osnova;
    JPanel downPanel;
    JMenuBar menuBar;
    JMenu menuF;
    JMenu menuS;
    JMenuItem menuF_Item1;
    JMenuItem menuS_Item1;
    JButton buttonCancel;
    JButton buttonSave;
    JButton buttonSearch;
    JButton btnSpecialPrice;
    JTable table;
    JTextField search;
    Vector col;
    Vector dataTable;

    DefaultTableModel tModel;
    HashMap<String, String> map;
    CenaForm _parent;
    JDialog jdParent;
    ProgressBar pb;
    CenaPDB pdb;
    CenaDB db;

    int minSelectedRow = -1;
    int maxSelectedRow = -1;
    boolean tableModelListenerIsChanging = false;
    TableRowSorter sorter;

    public ChangeForm(CenaForm parent, boolean modal, Vector dataTab, HashMap<String, String> mapSettingChenge) {
        super(parent, modal);
        init();

        _parent = parent;
        cenaform = parent;
        map = mapSettingChenge;
        dataTable = modifyDataTable(dataTab, UtilCena.CHANGE_MODIFI);
        jdParent = this;
        createTableChange();

        setLocationRelativeTo(parent);
        setVisible(true);
    }

    private void init() {
        this.setTitle("Корректировка цен");

        setMinimumSize(new Dimension(850, 500));
        setPreferredSize(new Dimension(950, 600));

        osnova = new JPanel();
        downPanel = new JPanel();
        menuBar = new JMenuBar();
        menuF = new JMenu("Файл");
        menuS = new JMenu("Сервис");
        menuF_Item1 = new JMenuItem("Печать");
        menuS_Item1 = new JMenuItem("Параметры");
        buttonCancel = new JButton("Закрыть");
        buttonSave = new JButton("Сохранить");
        btnSpecialPrice = new JButton("Спеццена арт.");
        buttonSearch = new JButton("Поиск");
        table = new JTable();
        col = new Vector();
        dataTable = new Vector();
        search = new JTextField();

        osnova.setLayout(new BorderLayout(1, 1));
        osnova.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        downPanel.setLayout(new GridLayout(0, 5, 5, 5));
        downPanel.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        menuBar.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));

        table.setAutoCreateColumnsFromModel(true);

        col = new Vector();
        col.add("");
        col.add("Модель");
        col.add("Шифр. артикул");
        col.add("Артикул");
        col.add("Изделие");
        col.add("Сорт");
        col.add("min рост");
        col.add("max рост");
        col.add("Рост");
        col.add("min размер");
        col.add("max размер");
        col.add("Размер");
        col.add("Состав сырья");
        col.add("Цена плановая");
        col.add("НДС(%)");
        col.add("Цена реализации");
        col.add("ТН(%)");
        col.add("Цена розничная");
        col.add("Себестоимость");
        col.add("Рентабельность");
        col.add("Цена в RUB");
        col.add("ВЦ в USD");
        col.add("ВЦ в EUR");
        col.add("Себ-сть");
        col.add("Рен-сть");
        col.add("Новая цена в RUB");


        table.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "key-enter");
        table.getActionMap().put("key-enter", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                table.editCellAt(table.getSelectedRow(), table.getSelectedColumn());
                Component editor = table.getEditorComponent();
                if (editor != null) {
                    editor.requestFocus();
                    if (editor instanceof JTextField) {
                        ((JTextField) editor).selectAll();
                    }
                }
            }
        });

        table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                if (e.getValueIsAdjusting()) {
                    return;
                }
                minSelectedRow = (((DefaultListSelectionModel) e.getSource()).getMinSelectionIndex() == ((DefaultListSelectionModel) e.getSource()).getMaxSelectionIndex()) ?
                        -1 : ((DefaultListSelectionModel) e.getSource()).getMinSelectionIndex();
                maxSelectedRow = (((DefaultListSelectionModel) e.getSource()).getMinSelectionIndex() == ((DefaultListSelectionModel) e.getSource()).getMaxSelectionIndex()) ?
                        -1 : ((DefaultListSelectionModel) e.getSource()).getMaxSelectionIndex();
            }
        });

        menuS_Item1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                new SettingForm(_parent, ChangeForm.this, true, map, UtilCena.TABLE_CHANGE);
            }
        });

        menuF_Item1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                new PrintForm(_parent, ChangeForm.this, true, tModel, map, UtilCena.TABLE_CHANGE);
            }
        });

        buttonCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dispose();
            }
        });

        buttonSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonSaveActionPerformed();
            }
        });

        buttonSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonSearchActionPerformed();
            }
        });

        search.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                searchKeyPressed(evt);
            }
        });

        btnSpecialPrice.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //JTable mtt = table;
                int i = 0;
                HashMap myArray = new HashMap();
                Vector vec = new Vector();
                boolean flag = true;
                for (Iterator it = dataTable.iterator(); it.hasNext(); ) {
                    vec = (Vector) it.next();
                    if (Boolean.valueOf(vec.get(0).toString()) && flag) {
                        HashMap hmData = new HashMap();
                        hmData.put("mod", vec.get(1).toString());
                        hmData.put("art", vec.get(3).toString());
                        hmData.put("sort", vec.get(5).toString());
                        hmData.put("rstmin", vec.get(6).toString());
                        hmData.put("rstmax", vec.get(7).toString());
                        hmData.put("rzmmin", vec.get(9).toString());
                        hmData.put("rzmmax", vec.get(10).toString());
                        myArray.put(i, hmData);
                        i++;
                    }
                }
                new dept.sklad.specCena.SpecCena(jdParent, myArray);
            }
        });


        menuBar.add(menuF);
        menuBar.add(menuS);
        menuF.add(menuF_Item1);
        menuS.add(menuS_Item1);

        downPanel.add(buttonCancel);
        downPanel.add(buttonSave);
        downPanel.add(btnSpecialPrice);
        downPanel.add(buttonSearch);
        downPanel.add(search);

        osnova.add(downPanel, BorderLayout.SOUTH);
        osnova.add(new JScrollPane(table), BorderLayout.CENTER);
        osnova.add(menuBar, BorderLayout.NORTH);

        getContentPane().add(osnova);
        pack();
    }

    private void buttonSaveActionPerformed() {
        try {
            boolean flag_zero = true;
            boolean flag_check = false;

            for (Iterator it = dataTable.iterator(); it.hasNext(); ) {
                Vector vec = (Vector) it.next();
                if (Boolean.valueOf(vec.get(0).toString())) {
                    flag_check = true;
                    if (Double.valueOf(vec.get(25).toString()).doubleValue() == 0 && Boolean.valueOf(vec.get(0).toString())) {
                        flag_zero = false;
                        break;
                    }
                }
            }

            if (!flag_zero)
                if (JOptionPane.showOptionDialog(null, "В новых ценах есть 0. \nПродолжить?", "Внимание! ", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, new Object[]{"Да", "Отмена"}, "Да") == JOptionPane.YES_OPTION) {
                    flag_zero = true;
                }

            if (flag_zero && flag_check) {
                search.setText("true");
                buttonSearch.doClick();
                if (JOptionPane.showOptionDialog(null, "Сохранить выбранные элементы?", "Сохранение... ", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, new Object[]{"Да", "Отмена"}, "Да") == JOptionPane.YES_OPTION) {
                    db = new CenaDB();
                    pb = new ProgressBar(ChangeForm.this, false, "Сохранение новых цен...");
                    final SwingWorker sw = new SwingWorker() {
                        @Override
                        protected Object doInBackground() throws Exception {
                            Vector vec = new Vector();
                            try {
                                for (Iterator it = dataTable.iterator(); it.hasNext(); ) {
                                    vec = (Vector) it.next();
                                    if (Double.valueOf(vec.get(25).toString()).doubleValue() > -1 && Boolean.valueOf(vec.get(0).toString())) {
                                        db.setNewCenaV(Integer.valueOf(vec.get(2).toString()), //sar
                                                vec.get(3).toString(),                             //nar
                                                Integer.valueOf(vec.get(1).toString()),            //fas
                                                Integer.valueOf(vec.get(5).toString()),            //srt
                                                Integer.valueOf(vec.get(6).toString()),            //minrst
                                                Integer.valueOf(vec.get(7).toString()),            //maxrst
                                                Integer.valueOf(vec.get(9).toString()),            //minrzm
                                                Integer.valueOf(vec.get(10).toString()),           //maxrzm
                                                Double.valueOf(vec.get(13).toString()),            //cno
                                                Double.valueOf(vec.get(20).toString()),            //cnp
                                                Integer.valueOf(vec.get(14).toString()),           //nds
                                                Double.valueOf(vec.get(25).toString()));           //newcene
                                    }
                                }
                                JOptionPane.showMessageDialog(null, "Сохранение цен завершено успешно!", "Завершено!", javax.swing.JOptionPane.INFORMATION_MESSAGE);
                            } catch (Exception e) {
                                System.err.println(e.getMessage());
                                throw new Exception("Введено некорректное значение: " + e.getMessage() +
                                        "\n Процесс сохранения цен остановился на модели " + vec.get(1).toString());
                            }
                            return null;
                        }

                        @Override
                        protected void done() {
                            pb.dispose();
                        }
                    };
                    sw.execute();
                    pb.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
                    pb.setVisible(true);

                    cenaform.clearDataTable();

                    dataTable = modifyDataTable(dataTable, UtilCena.CHANGE_UPDATE);
                    createTableChange();
                }
                search.setText("");
                buttonSearch.doClick();
            } else if (!flag_check)
                JOptionPane.showMessageDialog(null, "Для сохранения ничего не выбрано!", "Внимание!", javax.swing.JOptionPane.WARNING_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ошибка: " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buttonSearchActionPerformed() {
        String text = search.getText();
        if (text.length() == 0) {
            sorter.setRowFilter(null);
        } else {
            try {
                sorter.setRowFilter(RowFilter.regexFilter("(?iu)" + text));
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Ошибка! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void searchKeyPressed(java.awt.event.KeyEvent evt) {
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) buttonSearch.doClick();
    }

    public void createTableChange() {
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
                return col == 0 || col == 25;
            }

            public void setValueAt(Object value, int row, int col) {
                try {
                    Vector rowVector = (Vector) dataVector.elementAt(row);
                    if (col == 25) {
                        if (String.valueOf(value).equals("null")) value = new Double(0);
                        rowVector.setElementAt(Double.valueOf(value.toString()).doubleValue(), col);

                        if (dataVector.size() > row + 1) {
                            Vector rowVector1 = (Vector) dataVector.elementAt(row + 1);
                            if (rowVector.get(1).toString().equals(rowVector1.get(1).toString()) &&
                                    rowVector.get(2).toString().equals(rowVector1.get(2).toString()) &&
                                    rowVector.get(3).toString().equals(rowVector1.get(3).toString()) &&
                                    rowVector.get(8).toString().equals(rowVector1.get(8).toString()) &&
                                    rowVector.get(11).toString().equals(rowVector1.get(11).toString()) &&
                                    rowVector1.get(5).toString().equals("2")) {
                                rowVector1.setElementAt(new BigDecimal(Double.valueOf(value.toString()) * 0.95).setScale(2, RoundingMode.HALF_UP).doubleValue(), col);
                                fireTableCellUpdated(row + 1, col);
                            }
                        }
                    } else if (col == 0) rowVector.setElementAt(value, col);
                    fireTableCellUpdated(row, col);
                } catch (Exception ee) {
                    JOptionPane.showMessageDialog(null, "Введено некорректное значение: " + ee.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
                }
            }
        };

        tModel.addTableModelListener(new TableModelListener() {
            public void tableChanged(TableModelEvent e) {
                if (tableModelListenerIsChanging) {
                    return;
                }
                int firstRow = e.getFirstRow();
                int column = e.getColumn();

                if (column != 0 || maxSelectedRow == -1 || minSelectedRow == -1) {
                    return;
                }
                tableModelListenerIsChanging = true;
                boolean value = ((Boolean) tModel.getValueAt(firstRow, column)).booleanValue();
                for (int i = minSelectedRow; i <= maxSelectedRow; i++) {
                    tModel.setValueAt(Boolean.valueOf(value), table.convertRowIndexToModel(i), column);
                }

                minSelectedRow = -1;
                maxSelectedRow = -1;

                tableModelListenerIsChanging = false;
            }
        });

        table.setModel(tModel);

        UtilCena.initColumTableMap(table, map);

        sorter = new TableRowSorter<TableModel>(tModel);
        table.setRowSorter(sorter);
        table.getColumnModel().getColumn(0).setHeaderRenderer(new CheckBoxHeader(table.getTableHeader(), 0, ""));
    }

    private Vector modifyDataTable(final Vector data, final String type) {
        final Vector rezalt = new Vector();
        try {
            db = new CenaDB();
            pb = new ProgressBar(ChangeForm.this, false, "Обработка... ");
            final SwingWorker sw = new SwingWorker() {
                @Override
                protected Object doInBackground() throws Exception {
                    try {
                        for (Iterator it = data.iterator(); it.hasNext(); ) {
                            Vector vrow = (Vector) it.next();
                            Vector tmpS1 = new Vector();
                            for (Iterator it1 = vrow.iterator(); it1.hasNext(); ) {
                                Object velement = it1.next();
                                tmpS1.add(velement);
                            }

                            if ((Boolean) vrow.get(0)) {
                                if (type.equals(UtilCena.CHANGE_MODIFI)) {
                                    pb.setMessage("Поиск 2-го сорта модели - " + tmpS1.get(1) + "...");
                                    tmpS1.add(new Double(0));
                                    tmpS1.set(0, true);
                                    //1 (4) сорт
                                    rezalt.add(tmpS1);

                                    Iterator it_ = db.getFullTableIzd(vrow.get(2),//sar
                                            vrow.get(3),                          //nar
                                            vrow.get(1),                          //fas
                                            2,                                    //srt
                                            vrow.get(6),                          //minrst
                                            vrow.get(7),                          //maxrst
                                            vrow.get(9),                          //minrzm
                                            vrow.get(10)).iterator();             //maxrzm        
                                    if (it_.hasNext()) {
                                        Vector tmpS2 = (Vector) it_.next();

                                        tmpS2.add(0, true);
                                        if (Integer.valueOf(tmpS2.get(10).toString()) % 2 == 1 && Integer.valueOf(tmpS2.get(10).toString()) <= 33 && tmpS2.get(2).toString().substring(0, 4).equals("4334"))
                                            tmpS2.add(11, Integer.valueOf(tmpS2.get(9).toString()) + "--" + new Integer(Integer.valueOf(tmpS2.get(10).toString()) + 1));
                                        tmpS2.add(tmpS2.size() - 1, Double.valueOf(vrow.get(18).toString()));  // себ-сть расчётная
                                        tmpS2.add(tmpS2.size() - 1, Double.valueOf(vrow.get(19).toString()));  // рен-сть из справ.
                                        tmpS2.add(new BigDecimal(Double.valueOf(tmpS2.get(20).toString()) / Double.parseDouble(UtilCena.KURS_USD)).setScale(2, RoundingMode.HALF_UP).doubleValue()); //цена в USD
                                        tmpS2.add(new BigDecimal(Double.valueOf(tmpS2.get(20).toString()) / Double.parseDouble(UtilCena.KURS_EUR)).setScale(2, RoundingMode.HALF_UP).doubleValue()); //цена в EUR
                                        tmpS2.add(Double.valueOf(vrow.get(23).toString()));  // себ-сть из справ.
                                        tmpS2.add(Double.valueOf(vrow.get(23).toString()) <= 0 ? 0 : new BigDecimal(((Double.valueOf(tmpS2.get(13).toString()) -
                                                Double.valueOf(vrow.get(23).toString())) / Double.valueOf(vrow.get(23).toString())) * 100).setScale(1, RoundingMode.HALF_UP).doubleValue());    // рен-сть расчётная
                                        tmpS2.add(new Double(0));

                                        //2 сорт
                                        rezalt.add(tmpS2);
                                    }
                                } else if (type.equals(UtilCena.CHANGE_UPDATE)) {
                                    pb.setMessage("Обновление цены модели - " + tmpS1.get(1) + "...");
                                    double cenaV = db.getCenaV(Integer.valueOf(vrow.get(2).toString()), //sar
                                            vrow.get(3).toString(),                             //nar
                                            Integer.valueOf(vrow.get(1).toString()),            //fas
                                            Integer.valueOf(vrow.get(5).toString()),            //srt
                                            Integer.valueOf(vrow.get(6).toString()),            //minrst
                                            Integer.valueOf(vrow.get(7).toString()),            //maxrst
                                            Integer.valueOf(vrow.get(9).toString()),            //minrzm
                                            Integer.valueOf(vrow.get(10).toString()),           //maxrzm
                                            Double.valueOf(vrow.get(13).toString()),            //cno
                                            Integer.valueOf(vrow.get(14).toString()));           //nds    
                                    tmpS1.set(20, cenaV);
                                    tmpS1.set(21, new BigDecimal(cenaV / Double.parseDouble(UtilCena.KURS_USD)).setScale(2, RoundingMode.HALF_UP).doubleValue());
                                    tmpS1.set(22, new BigDecimal(cenaV / Double.parseDouble(UtilCena.KURS_EUR)).setScale(2, RoundingMode.HALF_UP).doubleValue());
                                    rezalt.add(tmpS1);
                                }
                            }
                        }
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(null, "Ошибка поиска: " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
                    }
                    return null;
                }

                @Override
                protected void done() {
                    pb.dispose();
                }
            };
            sw.execute();
            pb.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
            pb.setVisible(true);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ошибка: " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
        return rezalt;
    }
}
