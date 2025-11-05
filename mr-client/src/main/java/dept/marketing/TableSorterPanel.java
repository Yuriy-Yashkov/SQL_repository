package dept.marketing;

import workDB.DB;
import workDB.PDB;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DecimalFormat;
import java.util.HashMap;

public class TableSorterPanel extends JPanel {
    static boolean flagSelect = true;
    JTable table;
    CartForm cartform;
    PDB pdb;
    DB db;
    int rowSelect;
    String str[][] = {{"Кол-во", "9"}, {"Примечание", "14"}, {"Изделие", "5"}, {"Артикул", "16"}};
    HashMap<String, String> stolbec = new HashMap<String, String>();
    JComboBox jc;
    boolean cancel = false;
    boolean flagAll = false;

    public TableSorterPanel(CartForm aThis, boolean modal) {
        super(new GridLayout(1, 0));
        cartform = aThis;
        pdb = cartform.pdb;
        rowSelect = cartform.selectRow;

        TableSorter sorter = new TableSorter(new MyTableModel());
        table = new JTable(sorter);

        cartform.summa = sorter.toString();

        sorter.setTableHeader(table.getTableHeader());

        //Задаём размеры столбцов
        initColumnSizes(table);

        //Задаём рендер для столбцов
        setRendererColumn(table);

        //Настройка подсказки для заголовков столбцов.
        table.getTableHeader().setToolTipText("Щелкните, чтобы задать сортировку");

        JPanel jp = new JPanel(new BorderLayout());
        JPanel jp_butt = new JPanel();
        JPanel jp_text = new JPanel();

        jc = new JComboBox();
        for (int i = 0; i < str.length; i++) {
            stolbec.put(str[i][0], str[i][1]);
            jc.addItem(str[i][0]);
        }

        JButton jb_e = new JButton("Редактировать");
        JButton jb_u = new JButton("Удалить");
        JButton jb_uall = new JButton("Выделить всё");

        jp_butt.add(jc);
        jp_butt.add(jb_e);
        jp_butt.add(jb_uall);
        jp_butt.add(jb_u);

        jp_text.setLayout(new BorderLayout());
        jp_text.add(jp_butt, BorderLayout.EAST);

        jp.add(new JScrollPane(table));
        jp.add(jp_text, BorderLayout.SOUTH);

        if (rowSelect != 0) {
            table.getSelectionModel().setSelectionInterval(rowSelect, rowSelect);
            table.scrollRectToVisible(table.getCellRect(rowSelect - 1, rowSelect - 1, true));
        }// else table.scrollRectToVisible(table.getCellRect(table.getRowCount() - 1, table.getColumnCount() - 1, true));


        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                cartform.selectRow = table.getSelectedRow();
            }
        });

        jb_e.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                editMouseClicked(evt);
            }
        });

        jb_u.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                deleteMouseClicked(evt);
            }
        });

        jb_uall.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                selectallMouseClicked(evt);
            }
        });
        add(jp);
    }

    private void initColumnSizes(JTable table) {
        TableColumn column = null;
        for (int i = 0; i < 17; i++) {
            column = table.getColumnModel().getColumn(i);
            switch (i) {
                case 0:
                case 15:
                    column.setPreferredWidth(25);
                    break;
                case 1:
                case 8:
                case 11:
                    column.setPreferredWidth(80);
                    break;
                case 2:
                case 12:
                case 13:
                case 14:
                    column.setPreferredWidth(90);
                    break;
                case 3:
                case 7:
                case 9:
                    column.setPreferredWidth(50);
                    break;
                case 4:
                case 6:
                    column.setPreferredWidth(40);
                    break;
                case 5:
                    column.setPreferredWidth(110);
                    break;
                case 10:
                    column.setPreferredWidth(60);
                    break;
                case 16:
                    column.setMinWidth(0);
                    column.setMaxWidth(0);
                default:
                    break;
            }
        }
    }

    private void setRendererColumn(JTable table) {
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (!Boolean.valueOf(table.getValueAt(row, 16).toString()))
                    cell.setBackground(Color.PINK);
                else
                    cell.setBackground(table.getBackground());

                cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                return cell;
            }
        };
        table.getColumnModel().getColumn(2).setCellRenderer(renderer);

    }

    /*     * Метод помечает все строки true CheckBox  */
    public void editMouseClicked(MouseEvent evt) {
        int count = 0;
        int rezOptionPane = 0;
        String rezStr = "";
        cancel = false;
        flagAll = false;
        for (int i = 0; i < (table.getModel().getRowCount()); i++) {
            if ((Boolean) (table.getModel().getValueAt(i, 15))) {
                count++;
                break;
            }
        }
        if (count > 0) {
            if (jc.getSelectedItem().toString().equals("Артикул")) {
                JCheckBox checkbox = new JCheckBox("Использовать артикул;");
                rezOptionPane = JOptionPane.showOptionDialog(null, checkbox, "Введите новое значение:", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, new Object[]{"Да", "Отмена"}, checkbox);
                rezStr = String.valueOf(checkbox.isSelected());
            } else {
                JTextField format = new JTextField();
                rezOptionPane = JOptionPane.showOptionDialog(null, format, "Введите новое значение:", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, new Object[]{"Да", "Отмена"}, format);
                rezStr = String.valueOf(format.getText().trim());
            }

            if (rezOptionPane == JOptionPane.YES_OPTION) {
                for (int i = 0; i < (table.getModel().getRowCount()); i++) {
                    if (cancel) break;
                    else {
                        if ((Boolean) (table.getModel().getValueAt(i, 15)))
                            table.setValueAt(rezStr, i, Integer.parseInt(stolbec.get(jc.getSelectedItem().toString())));
                    }
                }
            }
        } else
            JOptionPane.showMessageDialog(null, "Ничего не выбрано!", "Предупреждение", javax.swing.JOptionPane.WARNING_MESSAGE);
    }

    /*     * Метод помечает все строки true CheckBox  */
    public void selectallMouseClicked(MouseEvent evt) {
        if (table.getModel().getRowCount() > 0) {
            pdb.selectAll(flagSelect);
            flagSelect = !flagSelect;
            cartform.cartFormUpdate();
        } else
            JOptionPane.showMessageDialog(null, "Нет строк для выделения!", "Предупреждение", javax.swing.JOptionPane.WARNING_MESSAGE);
    }

    /*      * Метод удаляет выделенный CheckBox     */
    public void deleteMouseClicked(MouseEvent evt) {
        int count = 0;
        for (int i = 0; i < (table.getModel().getRowCount()); i++) {
            if ((Boolean) (table.getModel().getValueAt(i, 15))) {
                count++;
                break;
            }
        }
        if (count > 0) {
            int result = JOptionPane.showOptionDialog(null, "Удалить выбранные строки?", "Удалить?", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, null, new Object[]{"Да", "Нет"}, "Да");
            if (result == JOptionPane.YES_OPTION) {
                pdb.deleteChekBox();
                cartform.cartFormUpdate();
            }
        } else
            JOptionPane.showMessageDialog(null, "Ничего не выбрано!", "Предупреждение", javax.swing.JOptionPane.WARNING_MESSAGE);
    }

    public class MyTableModel extends AbstractTableModel {
        private String[] columnNames = {"№", "Шифр", "Артикул", "Модель", "Сорт", "Изделие", "Рост", "Размер", "Цвет", "Кол-во", "Цена", "Сумма", "НДС", "Сумма с НДС", "Примечание", "Edit", "Флаг"};
        private Object[][] data = pdb.returnDataCart(CartForm.kodValutaSetting, CartForm.kursSetting, CartForm.ndsValueSetting, false);

        public int getColumnCount() {
            return columnNames.length;
        }

        public int getRowCount() {
            return data.length;
        }

        @Override
        public String getColumnName(int col) {
            return columnNames[col];
        }

        public Object getValueAt(int row, int col) {
            return data[row][col];
        }

        /*       * Метод для CheckBox         */
        @Override
        public Class getColumnClass(int c) {
            return getValueAt(0, c).getClass();
        }

        /*      * Редактирование ячейки.      */
        @Override
        public boolean isCellEditable(int row, int col) {
            return col == 9 || col == 14 || col == 15;
        }

        @Override
        public void setValueAt(Object value, int row, int col) {
            try {
                if (col == 5) {
                    if (value.toString().length() > 0 && value.toString().length() < 20) {
                        if (Integer.parseInt(table.getModel().getValueAt(row, 1).toString()) == 40100000 || Integer.parseInt(table.getModel().getValueAt(row, 1).toString()) == 40200000 ||
                                Integer.parseInt(table.getModel().getValueAt(row, 1).toString()) == 40300000 || Integer.parseInt(table.getModel().getValueAt(row, 1).toString()) == 43000000) {
                            value = value.toString() + " (новая модель)";
                            pdb.editNameIzdelie(value, row + 1);
                            data[row][col] = value;
                            fireTableCellUpdated(row, col);
                        } else {
                            cancel = true;
                            JOptionPane.showMessageDialog(null, "Редактировать наименование изделия можно только у новой модели.", "Предупреждение", javax.swing.JOptionPane.WARNING_MESSAGE);
                            cartform.cartFormUpdate();
                        }
                    } else {
                        cancel = true;
                        JOptionPane.showMessageDialog(null, "Введено некорректное значение!", "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
                        cartform.cartFormUpdate();
                    }
                }
                if (col == 9) {
                    boolean flag = false;
                    if (value != null && Integer.parseInt(value.toString()) > 0) {
                        flag = true;
                        if (!flagAll) {
                            db = new DB();
                            int ostKol = db.getOstatokIzd(data[row][1], data[row][2], data[row][3], data[row][4], data[row][6], data[row][7], data[row][8]);
                            if (ostKol < Integer.parseInt(value.toString())) {
                                flag = false;
                                int tmp = JOptionPane.showOptionDialog(null, "Остаток на складе модели " + data[row][3] + " (" + data[row][6] + " - " + data[row][7] + ") = " + ostKol + "! Изменить кол-во?", "Предупреждение",
                                        JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE, null, new Object[]{"Да", "Нет", "Да-для всех", "Нет-для всех"}, "Да");
                                if (tmp == 0 || tmp == 2) flag = true;
                                if (tmp == 2) flagAll = true;
                                if (tmp == 3) cancel = true;
                            }
                        }
                        if (flag) {
                            pdb.editKolvo(Integer.parseInt(getValueAt(row, 0).toString()), Integer.parseInt(value.toString()));
                            cartform.cartFormUpdate();
                            data[row][col] = value;
                            fireTableCellUpdated(row, col);
                        }
                    } else {
                        cancel = true;
                        JOptionPane.showMessageDialog(null, "В столбце 'Кол-во':\n   *нельзя оставлять пустые строки;\n   *можно задавать только положительные значения; ", "Предупреждение", javax.swing.JOptionPane.WARNING_MESSAGE);
                        cartform.cartFormUpdate();
                    }
                }
                if (col == 14) {
                    if (value.toString().length() < 150) {
                        pdb.editNote(Integer.parseInt(getValueAt(row, 0).toString()), value);
                        data[row][col] = value;
                        fireTableCellUpdated(row, col);
                    } else {
                        cancel = true;
                        JOptionPane.showMessageDialog(null, "Слишком большое значение!", "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
                        cartform.cartFormUpdate();
                    }
                }
                if (col == 15) {
                    pdb.editChekBox(Integer.parseInt(getValueAt(row, 0).toString()), (Boolean) value);
                    data[row][col] = value;
                    fireTableCellUpdated(row, col);
                }
                if (col == 16) {
                    pdb.editFlagArtikul(Integer.parseInt(getValueAt(row, 0).toString()), Boolean.valueOf(value.toString()));
                    data[row][col] = value;
                    fireTableCellUpdated(row, col);
                    table.revalidate();
                    table.repaint();
                }
            } catch (Exception ee) {
                cancel = true;
                JOptionPane.showMessageDialog(null, "Введено некорректное значение: " + ee.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
                cartform.cartFormUpdate();
            }
        }

        @Override
        public String toString() {
            double summsnds = 0;
            for (int i = 0; i < getRowCount(); i++) {
                summsnds += Double.valueOf(data[i][13].toString()).doubleValue();
            }
            return new DecimalFormat("###,###.###").format(summsnds);
        }

    }
}