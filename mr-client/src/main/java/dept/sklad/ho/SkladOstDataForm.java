package dept.sklad.ho;

import com.jhlabs.awt.ParagraphLayout;
import com.toedter.calendar.JDateChooser;
import common.CheckBoxHeader;
import common.ProgressBar;
import common.User;
import common.UtilFunctions;
import net.coderazzi.filters.gui.AutoChoices;
import net.coderazzi.filters.gui.TableFilterHeader;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Vector;

/**
 *
 * @author lidashka
 */
@SuppressWarnings("all")
public class SkladOstDataForm extends JDialog {
    private User user = User.getInstance();

    private JPanel osnova;
    private JPanel buttPanel;
    private JPanel centrePanel;
    private JPanel searchPanel;
    private JButton buttOpen;
    private JButton buttDetal;
    private JButton buttClose;
    private JButton buttPrint;
    private JLabel numEmpl;
    private JLabel emplName;
    private JTable table;
    private DefaultTableModel tModel;
    private TableFilterHeader filterHeader;
    private DefaultTableCellRenderer renderer;
    private Vector data;
    private Vector col;
    private JDateChooser sDate;
    private JCheckBox zeroFlag;
    private ButtonGroup buttonGroupVid;
    private ButtonGroup buttonGroupDept;
    private JRadioButton jRadioButton1;
    private JRadioButton jRadioButton2;
    private JRadioButton jRadioButton3;
    private JRadioButton jRadioButton4;
    private JRadioButton jRadioButton5;

    private ProgressBar pb;

    private HashMap<String, String> mapSetting;
    private JMenuBar jMenuBar1;
    private JMenu jMenu1;
    private JMenuItem jMenuItem1;
    private JMenu jMenu2;
    private JMenuItem jMenuItem2;
    private JPanel panelCheckBox;

    private int minSelectedRow = -1;
    private int maxSelectedRow = -1;
    private boolean tableModelListenerIsChanging = false;

    private int flagVid;

    public SkladOstDataForm(java.awt.Dialog parent, boolean modal) {
        super(parent, modal);
        setTitle("Остатки ТМЦ в кладовой");

        init();

        flagVid = 1;

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

        sDate.setDate((Calendar.getInstance()).getTime());

        try {
            String[][] arr = UtilFunctions.readPropFileStringsArray(UtilSkladHO.SETTING_MAP_OST_KL_EDIT);

            for (int i = 0; i < arr.length; i++)
                mapSetting.put(arr[i][0], arr[i][1]);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ошибка! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }

        jRadioButton4.setSelected(true);

        searchPanel.add(new JLabel("Остаток:"), ParagraphLayout.NEW_PARAGRAPH);
        searchPanel.add(jRadioButton4);
        searchPanel.add(new JLabel("    "));
        searchPanel.add(zeroFlag);

        createOstTable(new Vector());

        setLocationRelativeTo(parent);
        setVisible(true);
    }

    public SkladOstDataForm(java.awt.Dialog parent, boolean modal, String str) {
        super(parent, modal);
        setTitle("Остатки ТМЦ в ХЭО/Внедр.");

        init();

        flagVid = 2;

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
        col.add("Выдано Ф");
        col.add("Списано Ф");
        col.add("Отходы Ф");
        col.add("Возврат Ф");
        col.add("Итого Ф");
        col.add("Итого K");
        col.add("Куски");
        col.add("idЦех.");
        col.add("Цех");
        col.add("idРаб");
        col.add("Работник");

        sDate.setDate((Calendar.getInstance()).getTime());

        try {
            String[][] arr = UtilFunctions.readPropFileStringsArray(UtilSkladHO.SETTING_MAP_OST_DR_EDIT);

            for (int i = 0; i < arr.length; i++)
                mapSetting.put(arr[i][0], arr[i][1]);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ошибка! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }

        jRadioButton5.setSelected(true);
        zeroFlag.setSelected(true);

        searchPanel.add(new JLabel("Остаток:"), ParagraphLayout.NEW_PARAGRAPH);
        searchPanel.add(jRadioButton5);
        searchPanel.add(new JLabel("    "));
        searchPanel.add(zeroFlag);

        createOstTable(new Vector());

        setLocationRelativeTo(parent);
        setVisible(true);
    }

    public SkladOstDataForm(java.awt.Dialog parent, boolean modal, int tmp) {
        super(parent, modal);
        setTitle("Остатки моделей в кладовой");

        init();

        flagVid = 3;

        col.add("");
        col.add("Модель");
        col.add("Название");
        col.add("idВид");
        col.add("Вид");
        col.add("Нач/п. Ф");
        col.add("Приход Ф");
        col.add("Сдано Ф");
        col.add("Итого Ф");

        sDate.setDate((Calendar.getInstance()).getTime());

        try {
            String[][] arr = UtilFunctions.readPropFileStringsArray(UtilSkladHO.SETTING_MAP_OST_MODEL_EDIT);

            for (int i = 0; i < arr.length; i++)
                mapSetting.put(arr[i][0], arr[i][1]);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ошибка! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }

        createOstTable(new Vector());

        setLocationRelativeTo(parent);
        setVisible(true);
    }

    private void init() {
        setMinimumSize(new Dimension(500, 480));
        setPreferredSize(new Dimension(850, 700));

        flagVid = 0;

        initMenu();

        osnova = new JPanel();
        buttPanel = new JPanel();
        searchPanel = new JPanel();
        centrePanel = new JPanel();
        panelCheckBox = new JPanel();
        buttOpen = new JButton("Показать");
        buttDetal = new JButton("Подробно");
        buttClose = new JButton("Закрыть");
        buttPrint = new JButton("Печать");
        numEmpl = new JLabel();
        emplName = new JLabel();
        sDate = new JDateChooser();
        zeroFlag = new JCheckBox("  Показывать нулевые остатки;");
        table = new JTable();
        filterHeader = new TableFilterHeader(table, AutoChoices.ENABLED);
        mapSetting = new HashMap<String, String>();
        col = new Vector();
        buttonGroupVid = new ButtonGroup();
        buttonGroupDept = new ButtonGroup();
        jRadioButton1 = new JRadioButton();
        jRadioButton2 = new JRadioButton();
        jRadioButton3 = new JRadioButton();
        jRadioButton4 = new JRadioButton();
        jRadioButton5 = new JRadioButton();

        osnova.setLayout(new BorderLayout(1, 1));
        osnova.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        searchPanel.setLayout(new ParagraphLayout());
        centrePanel.setLayout(new BorderLayout(1, 1));
        buttPanel.setLayout(new GridLayout(0, 4, 5, 5));
        buttPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        buttPanel.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        panelCheckBox.setLayout(new GridLayout(0, 3, 5, 5));

        buttOpen.setPreferredSize(new Dimension(120, 20));
        sDate.setPreferredSize(new Dimension(120, 20));
        numEmpl.setPreferredSize(new Dimension(80, 20));
        emplName.setPreferredSize(new Dimension(250, 20));

        numEmpl.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        emplName.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        jRadioButton1.setFont(new java.awt.Font("Dialog", 0, 13));
        jRadioButton2.setFont(new java.awt.Font("Dialog", 0, 13));
        jRadioButton3.setFont(new java.awt.Font("Dialog", 0, 13));
        jRadioButton4.setFont(new java.awt.Font("Dialog", 0, 13));
        jRadioButton5.setFont(new java.awt.Font("Dialog", 0, 13));

        jRadioButton1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jRadioButton2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jRadioButton3.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jRadioButton4.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jRadioButton5.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        jRadioButton1.setText("нач. периода;");
        jRadioButton2.setText("на дату;");
        jRadioButton3.setText("текущие;");
        jRadioButton4.setText("в кладовой;");
        jRadioButton5.setText("в ХЭО/Внедр.;");

        jRadioButton1.setActionCommand("0");
        jRadioButton2.setActionCommand("1");
        jRadioButton3.setActionCommand("2");
        jRadioButton4.setActionCommand("0");
        jRadioButton5.setActionCommand("1");

        jRadioButton3.setSelected(true);

        table.setAutoCreateColumnsFromModel(true);
        table.getTableHeader().setReorderingAllowed(false);

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

        renderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                try {
                    cell.setBackground(table.getBackground());

                    if (flagVid == 3) {

                    } else {
                        if (column == 4) {
                            if (Double.valueOf(table.getValueAt(row, 9).toString()) < 0 ||
                                    Double.valueOf(table.getValueAt(row, 10).toString()) < 0 ||
                                    Double.valueOf(table.getValueAt(row, 11).toString()) < 0 ||
                                    Double.valueOf(table.getValueAt(row, 12).toString()) < 0 ||
                                    Double.valueOf(table.getValueAt(row, 13).toString()) < 0 ||
                                    Double.valueOf(table.getValueAt(row, 14).toString()) < 0) {
                                cell.setBackground(Color.PINK);
                            }
                        } else if (column >= 9 || column <= 14) {
                            if (Double.valueOf(table.getValueAt(row, column).toString()) < 0) {
                                cell.setBackground(Color.PINK);
                            }
                        } else
                            cell.setBackground(table.getBackground());
                    }
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "Ошибка! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
                }
                cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                return cell;
            }
        };

        buttOpen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttOpenActionPerformed(evt);
            }
        });

        buttDetal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttDetalActionPerformed(evt);
            }
        });

        buttClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttCloseActionPerformed(evt);
            }
        });

        buttPrint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttPrintActionPerformed(evt);
            }
        });

        mapSetting.put("", "true");
        mapSetting.put("Название", "true");
        mapSetting.put("Итого Ф", "true");
        mapSetting.put("Итого K", "true");

        buttonGroupVid.add(jRadioButton1);
        buttonGroupVid.add(jRadioButton2);
        buttonGroupVid.add(jRadioButton3);

        buttonGroupDept.add(jRadioButton4);
        buttonGroupDept.add(jRadioButton5);

        searchPanel.add(new JLabel("Период:"), ParagraphLayout.NEW_PARAGRAPH);
        searchPanel.add(jRadioButton1);
        searchPanel.add(jRadioButton2);
        searchPanel.add(jRadioButton3);
        searchPanel.add(new JLabel("    Дата:"));
        searchPanel.add(sDate);
        searchPanel.add(new JLabel("    "));
        searchPanel.add(buttOpen);

        buttPanel.add(buttClose);
        buttPanel.add(buttDetal);
        buttPanel.add(new JLabel("    "));
        buttPanel.add(buttPrint);

        osnova.add(searchPanel, BorderLayout.NORTH);
        osnova.add(new JScrollPane(table), BorderLayout.CENTER);
        osnova.add(buttPanel, BorderLayout.SOUTH);

        getContentPane().add(osnova);
        pack();
    }

    private void initMenu() {
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu2 = new javax.swing.JMenu();
        jMenuItem2 = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jMenu2.setText("Параметры");

        jMenuItem2.setText("Столбцы таблицы");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                try {
                    final HashMap<String, String> tempMap = new HashMap<String, String>();

                    panelCheckBox.removeAll();

                    for (int i = 1; i < table.getColumnCount(); i++) {
                        final String name = table.getColumnName(i);

                        if (!name.trim().toLowerCase().substring(0, 2).equals("id")) {

                            final JCheckBox tempBox = new JCheckBox(name);

                            if (Boolean.valueOf(mapSetting.get(name)))
                                tempBox.setSelected(true);

                            tempBox.addItemListener(new ItemListener() {
                                public void itemStateChanged(ItemEvent event) {
                                    if (tempBox.isSelected() == true) tempMap.put(name, "true");
                                    else tempMap.put(name, "false");
                                }
                            });

                            panelCheckBox.add(tempBox);
                        }
                    }

                    if (JOptionPane.showOptionDialog(null, panelCheckBox, "Столбцы таблицы", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, new Object[]{"Сохранить", "Отмена"}, "Сохранить") == JOptionPane.YES_OPTION) {
                        for (int i = 0; i < table.getColumnCount(); i++) {
                            if (tempMap.get(table.getColumnName(i)) != null)
                                mapSetting.put(table.getColumnName(i), tempMap.get(table.getColumnName(i)));
                        }

                        Object[] keys = mapSetting.keySet().toArray();
                        Object[] values = mapSetting.values().toArray();
                        String rezaltPrint = "";

                        for (int row = 0; row < mapSetting.size(); row++) {
                            rezaltPrint += keys[row];
                            rezaltPrint += ",";
                            rezaltPrint += values[row];
                            rezaltPrint += ";";
                        }

                        try {
                            if (flagVid == 1) {
                                UtilFunctions.setSettingPropFile(rezaltPrint, UtilSkladHO.SETTING_MAP_OST_KL_EDIT);
                            } else if (flagVid == 2) {
                                UtilFunctions.setSettingPropFile(rezaltPrint, UtilSkladHO.SETTING_MAP_OST_DR_EDIT);
                            } else if (flagVid == 3) {
                                UtilFunctions.setSettingPropFile(rezaltPrint, UtilSkladHO.SETTING_MAP_OST_MODEL_EDIT);
                            }

                        } catch (Exception e) {
                            JOptionPane.showMessageDialog(null, "Ошибка! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
                        }

                        createOstTable(tModel.getDataVector());
                    }
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "Ошибка. " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
                    dispose();
                }
            }
        });
        jMenu2.add(jMenuItem2);

        jMenuBar1.add(jMenu2);

        setJMenuBar(jMenuBar1);
    }

    private void buttCloseActionPerformed(ActionEvent evt) {
        dispose();
    }

    private void buttDetalActionPerformed(ActionEvent evt) {
        try {
            if (table.getSelectedRow() != -1) {
                int id = -1;
                int idDept = -1;
                int idEmpl = -1;

                if (flagVid == 1) {
                    id = Integer.valueOf(table.getValueAt(table.getSelectedRow(), 2).toString());
                } else if (flagVid == 2) {
                    id = Integer.valueOf(table.getValueAt(table.getSelectedRow(), 2).toString());
                    idDept = Integer.valueOf(table.getValueAt(table.getSelectedRow(), 17).toString());
                    idEmpl = Integer.valueOf(table.getValueAt(table.getSelectedRow(), 19).toString());
                } else if (flagVid == 3) {
                    id = Integer.valueOf(table.getValueAt(table.getSelectedRow(), 1).toString());
                }

                if (id != -1)
                    new SmallTableForm(this, true, String.valueOf(id), getDetalData(id, idDept, idEmpl), flagVid);
            } else
                JOptionPane.showMessageDialog(null, "Вы не выбрали запись!", "Внимание", javax.swing.JOptionPane.WARNING_MESSAGE);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ошибка! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }

    }

    private void buttOpenActionPerformed(ActionEvent evt) {
        try {
            if (buttonGroupVid.getSelection().getActionCommand().equals("0")) {
                java.util.Date d = sDate.getDate();
                d.setDate(1);

                sDate.setDate(d);

            } else if (buttonGroupVid.getSelection().getActionCommand().equals("2")) {
                sDate.setDate((Calendar.getInstance()).getTime());
            }

            createOstTable(getData());

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ошибка! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buttPrintActionPerformed(ActionEvent evt) {
        try {
            SkladHOOO oo = new SkladHOOO("Остатки", tModel, table.getColumnModel());
            oo.createReport("DefaultTableAlbumFormatCheck.ots");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ошибка! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void createOstTable(final Vector data) {
        tModel = new DefaultTableModel(data, col) {
            @Override
            public Class<?> getColumnClass(int col) {
                if (data.isEmpty())
                    return super.getClass();
                else
                    return getValueAt(0, col).getClass();
            }

            @Override
            public boolean isCellEditable(int row, int col) {
                return (col != 0) ? false : true;
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
        table.setAutoCreateColumnsFromModel(true);

        try {
            UtilSkladHO.initColumTableMap(table, mapSetting);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ошибка! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }

        table.getColumnModel().getColumn(0).setHeaderRenderer(new CheckBoxHeader(table.getTableHeader(), 0, ""));

        if (flagVid == 1) {
            table.getColumnModel().getColumn(4).setCellRenderer(renderer);
            table.getColumnModel().getColumn(9).setCellRenderer(renderer);
            table.getColumnModel().getColumn(10).setCellRenderer(renderer);
            table.getColumnModel().getColumn(11).setCellRenderer(renderer);
            table.getColumnModel().getColumn(12).setCellRenderer(renderer);
            table.getColumnModel().getColumn(13).setCellRenderer(renderer);
            table.getColumnModel().getColumn(14).setCellRenderer(renderer);
        } else if (flagVid == 2) {
            table.getColumnModel().getColumn(4).setCellRenderer(renderer);
            table.getColumnModel().getColumn(9).setCellRenderer(renderer);
            table.getColumnModel().getColumn(10).setCellRenderer(renderer);
            table.getColumnModel().getColumn(11).setCellRenderer(renderer);
            table.getColumnModel().getColumn(12).setCellRenderer(renderer);
            table.getColumnModel().getColumn(13).setCellRenderer(renderer);
            table.getColumnModel().getColumn(14).setCellRenderer(renderer);
            table.getColumnModel().getColumn(15).setCellRenderer(renderer);
            table.getColumnModel().getColumn(16).setCellRenderer(renderer);
        } else if (flagVid == 3) {
            table.getColumnModel().getColumn(1).setCellRenderer(renderer);
            table.getColumnModel().getColumn(4).setCellRenderer(renderer);
            table.getColumnModel().getColumn(5).setCellRenderer(renderer);
            table.getColumnModel().getColumn(6).setCellRenderer(renderer);
            table.getColumnModel().getColumn(7).setCellRenderer(renderer);
        }
    }

    private Vector getData() {
        data = new Vector();

        final java.util.Date d = sDate.getDate();
        d.setDate(1);

        try {
            pb = new ProgressBar(this, false, "Сбор данных ...");
            SwingWorker sw = new SwingWorker() {
                private SkladHOPDB spdb;

                protected Object doInBackground() {
                    try {
                        spdb = new SkladHOPDB();

                        switch (Integer.valueOf(buttonGroupVid.getSelection().getActionCommand())) {
                            case 0:
                                switch (flagVid) {
                                    case 1:
                                        data = spdb.getOst1DateStart(UtilFunctions.convertDateStrToLong(new SimpleDateFormat("dd.MM.yyyy").format(sDate.getDate())), zeroFlag.isSelected());
                                        break;
                                    case 2:
                                        data = spdb.getOst2DateStart(UtilFunctions.convertDateStrToLong(new SimpleDateFormat("dd.MM.yyyy").format(sDate.getDate())), zeroFlag.isSelected());
                                        break;
                                    case 3:
                                        data = spdb.getOst3DateStart(UtilFunctions.convertDateStrToLong(new SimpleDateFormat("dd.MM.yyyy").format(sDate.getDate())), zeroFlag.isSelected());
                                        break;

                                    default:
                                        data = new Vector();
                                        break;
                                }
                                break;

                            case 1:
                                switch (flagVid) {
                                    case 1:
                                        data = spdb.getOst1Date(UtilFunctions.convertDateStrToLong(new SimpleDateFormat("dd.MM.yyyy").format(d)),
                                                UtilFunctions.convertDateStrToLong(new SimpleDateFormat("dd.MM.yyyy").format(d)),
                                                UtilFunctions.convertDateStrToLong(new SimpleDateFormat("dd.MM.yyyy").format(sDate.getDate())),
                                                zeroFlag.isSelected());
                                        break;
                                    case 2:
                                        data = spdb.getOst2Date(UtilFunctions.convertDateStrToLong(new SimpleDateFormat("dd.MM.yyyy").format(d)),
                                                UtilFunctions.convertDateStrToLong(new SimpleDateFormat("dd.MM.yyyy").format(d)),
                                                UtilFunctions.convertDateStrToLong(new SimpleDateFormat("dd.MM.yyyy").format(sDate.getDate())),
                                                zeroFlag.isSelected());
                                        break;
                                    case 3:
                                        data = spdb.getOst3Date(UtilFunctions.convertDateStrToLong(new SimpleDateFormat("dd.MM.yyyy").format(d)),
                                                UtilFunctions.convertDateStrToLong(new SimpleDateFormat("dd.MM.yyyy").format(d)),
                                                UtilFunctions.convertDateStrToLong(new SimpleDateFormat("dd.MM.yyyy").format(sDate.getDate())),
                                                zeroFlag.isSelected());
                                        break;

                                    default:
                                        data = new Vector();
                                        break;
                                }
                                break;

                            case 2:
                                switch (flagVid) {
                                    case 1:
                                        data = spdb.getOst1DateCurrent(UtilFunctions.convertDateStrToLong(new SimpleDateFormat("dd.MM.yyyy").format(d)), zeroFlag.isSelected());
                                        break;
                                    case 2:
                                        data = spdb.getOst2DateCurrent(UtilFunctions.convertDateStrToLong(new SimpleDateFormat("dd.MM.yyyy").format(d)), zeroFlag.isSelected());
                                        break;
                                    case 3:
                                        data = spdb.getOst3DateCurrent(UtilFunctions.convertDateStrToLong(new SimpleDateFormat("dd.MM.yyyy").format(d)), zeroFlag.isSelected());
                                        break;

                                    default:
                                        data = new Vector();
                                        break;
                                }
                                break;

                            default:
                                data = new Vector();
                                break;
                        }

                    } catch (Exception e) {
                        data = new Vector();
                        JOptionPane.showMessageDialog(null, "Ошибка! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
                    } finally {
                        spdb.disConn();
                    }
                    return null;
                }

                protected void done() {
                    pb.dispose();
                }
            };
            sw.execute();
            pb.setVisible(true);
        } catch (Exception e) {
            data = new Vector();
            JOptionPane.showMessageDialog(null, "Ошибка! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
        return data;
    }

    private Vector getDetalData(final int id, final int idDept, final int idEmpl) {
        data = new Vector();

        final java.util.Date d = sDate.getDate();
        d.setDate(1);

        try {
            pb = new ProgressBar(this, false, "Сбор данных ...");
            SwingWorker sw = new SwingWorker() {
                private SkladHOPDB spdb;

                protected Object doInBackground() {
                    try {
                        spdb = new SkladHOPDB();

                        switch (flagVid) {
                            case 1:
                                if (Integer.valueOf(buttonGroupVid.getSelection().getActionCommand()) == 0) {
                                    data = spdb.getOstTMCDetalStart(id, 1, UtilFunctions.convertDateStrToLong(new SimpleDateFormat("dd.MM.yyyy").format(sDate.getDate())));

                                } else if (Integer.valueOf(buttonGroupVid.getSelection().getActionCommand()) == 1 || Integer.valueOf(buttonGroupVid.getSelection().getActionCommand()) == 2) {
                                    data = spdb.getOstTMCDetalStart(id, 1, UtilFunctions.convertDateStrToLong(new SimpleDateFormat("dd.MM.yyyy").format(d)));

                                    data.addAll(spdb.getOstTMCDetalMove(id, UtilFunctions.convertDateStrToLong(new SimpleDateFormat("dd.MM.yyyy").format(d)),
                                            UtilFunctions.convertDateStrToLong(new SimpleDateFormat("dd.MM.yyyy").format(sDate.getDate()))));

                                }
                                break;

                            case 2:
                                if (Integer.valueOf(buttonGroupVid.getSelection().getActionCommand()) == 0) {
                                    data = spdb.getOstTMCDetalEmplStart(id,
                                            2,
                                            idDept,
                                            idEmpl,
                                            UtilFunctions.convertDateStrToLong(new SimpleDateFormat("dd.MM.yyyy").format(sDate.getDate())));
                                } else if (Integer.valueOf(buttonGroupVid.getSelection().getActionCommand()) == 1 || Integer.valueOf(buttonGroupVid.getSelection().getActionCommand()) == 2) {
                                    data = spdb.getOstTMCDetalEmplStart(id,
                                            2,
                                            idDept,
                                            idEmpl,
                                            UtilFunctions.convertDateStrToLong(new SimpleDateFormat("dd.MM.yyyy").format(d)));

                                    data.addAll(spdb.getOstTMCDetalDeptMove(id, idDept, idEmpl, UtilFunctions.convertDateStrToLong(new SimpleDateFormat("dd.MM.yyyy").format(d)),
                                            UtilFunctions.convertDateStrToLong(new SimpleDateFormat("dd.MM.yyyy").format(sDate.getDate()))));

                                    data.addAll(spdb.getOstDetalProd(id, idDept, idEmpl, UtilFunctions.convertDateStrToLong(new SimpleDateFormat("dd.MM.yyyy").format(d)),
                                            UtilFunctions.convertDateStrToLong(new SimpleDateFormat("dd.MM.yyyy").format(sDate.getDate()))));

                                }
                                break;

                            case 3:
                                if (Integer.valueOf(buttonGroupVid.getSelection().getActionCommand()) == 0) {
                                    data = spdb.getOstModelDetalStart(id, UtilFunctions.convertDateStrToLong(new SimpleDateFormat("dd.MM.yyyy").format(sDate.getDate())), zeroFlag.isSelected());
                                } else if (Integer.valueOf(buttonGroupVid.getSelection().getActionCommand()) == 1 || Integer.valueOf(buttonGroupVid.getSelection().getActionCommand()) == 2) {
                                    data = spdb.getOstModelDetalStart(id, UtilFunctions.convertDateStrToLong(new SimpleDateFormat("dd.MM.yyyy").format(sDate.getDate())), zeroFlag.isSelected());

                                }
                                break;

                            default:
                                data = new Vector();
                                break;
                        }

                    } catch (Exception e) {
                        data = new Vector();
                        JOptionPane.showMessageDialog(null, "Ошибка! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
                    } finally {
                        spdb.disConn();
                    }
                    return null;
                }

                protected void done() {
                    pb.dispose();
                }
            };
            sw.execute();
            pb.setVisible(true);
        } catch (Exception e) {
            data = new Vector();
            JOptionPane.showMessageDialog(null, "Ошибка! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
        return data;
    }
}
