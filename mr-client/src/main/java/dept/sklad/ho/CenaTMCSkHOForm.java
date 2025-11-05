package dept.sklad.ho;

import by.march8.ecs.MainController;
import com.jhlabs.awt.ParagraphLayout;
import com.toedter.calendar.JDateChooser;
import common.CheckBoxHeader;
import common.FormMenu;
import common.User;
import common.UtilFunctions;
import net.coderazzi.filters.gui.AutoChoices;
import net.coderazzi.filters.gui.TableFilterHeader;
import workDB.PDB;

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
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TreeMap;
import java.util.Vector;

/**
 *
 * @author lidashka
 */
@SuppressWarnings("all")
public class CenaTMCSkHOForm extends javax.swing.JDialog implements FormMenu {
    private static TreeMap menu = new TreeMap();
    int minSelectedRow = -1;
    int maxSelectedRow = -1;
    boolean tableModelListenerIsChanging = false;
    private User user = User.getInstance();
    private PDB pdb;
    private SkladHOPDB spdb;
    private JPanel osnova;
    private JPanel searchPanel;
    private JPanel searchButtPanel;
    private JPanel upPanel;
    private JPanel buttPanel;
    private JButton buttSearch;
    private JButton buttAdd;
    private JButton buttPrint;
    private JButton buttOpen;
    private JButton buttCopy;
    private JButton buttHistory;
    private JTable table;
    private JLabel title;
    private Vector col;
    private DefaultTableModel tModel;
    private TableRowSorter<TableModel> sorter;
    private TableFilterHeader filterHeader;
    private JCheckBox checkboxSt;
    private JCheckBox checkboxIns;
    private JDateChooser sStDate;
    private JDateChooser eStDate;
    private JDateChooser sInsDate;
    private JDateChooser eInsDate;
    private ButtonGroup buttonGroup;
    private JRadioButton jRadioButton1;
    private JRadioButton jRadioButton2;
    private MainController controller;
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenu jMenu4;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;

    public CenaTMCSkHOForm() {
        initMenu();
    }

    public CenaTMCSkHOForm(MainController mainController, boolean modal) {
        super(mainController.getMainForm(), modal);
        controller = mainController;

        if (user.getFio() != null) {

            try {
                if (!UtilFunctions.readPropFileString(UtilSkladHO.SETTING_DATE_VVOD).trim().equals(""))
                    if (UtilFunctions.checkDate(UtilFunctions.readPropFileString(UtilSkladHO.SETTING_DATE_VVOD)))
                        UtilSkladHO.DATE_VVOD = UtilFunctions.readPropFileString(UtilSkladHO.SETTING_DATE_VVOD);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Ошибка! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
                try {
                    UtilFunctions.setSettingPropFile(new SimpleDateFormat("dd.MM.yyyy").format((Calendar.getInstance()).getTime()), UtilSkladHO.SETTING_DATE_VVOD);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Ошибка! " + ex.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
                }
            }

            init();

            java.util.Date d = (Calendar.getInstance()).getTime();
            d.setDate(1);

            sStDate.setDate(d);
            eStDate.setDate((Calendar.getInstance()).getTime());

            sInsDate.setDate(d);
            eInsDate.setDate((Calendar.getInstance()).getTime());

            createCenaTMCTable(getPrice());

            setLocationRelativeTo(controller.getMainForm());
            setVisible(true);
        } else
            JOptionPane.showMessageDialog(null, "Такой логин и пароль не присвоен ни одному пользователю. \n Обратитесь к администратору! ", "Вход", javax.swing.JOptionPane.WARNING_MESSAGE);

    }
    public CenaTMCSkHOForm(MainController mainController, boolean modal, String idTmc) {
        super(mainController.getMainForm(), modal);
        controller = mainController;

        try {
            if (!UtilFunctions.readPropFileString(UtilSkladHO.SETTING_DATE_VVOD).trim().equals(""))
                if (UtilFunctions.checkDate(UtilFunctions.readPropFileString(UtilSkladHO.SETTING_DATE_VVOD)))
                    UtilSkladHO.DATE_VVOD = UtilFunctions.readPropFileString(UtilSkladHO.SETTING_DATE_VVOD);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ошибка! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
            try {
                UtilFunctions.setSettingPropFile(new SimpleDateFormat("dd.MM.yyyy").format((Calendar.getInstance()).getTime()), UtilSkladHO.SETTING_DATE_VVOD);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Ошибка! " + ex.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
            }
        }

        init();

        java.util.Date d = (Calendar.getInstance()).getTime();
        d.setDate(1);

        sStDate.setDate(d);
        eStDate.setDate((Calendar.getInstance()).getTime());

        sInsDate.setDate(d);
        eInsDate.setDate((Calendar.getInstance()).getTime());

        jMenu3.setVisible(false);
        jMenuItem1.setVisible(false);

        buttPanel.removeAll();
        buttPanel.add(buttOpen);

        createCenaTMCTable(getPrice());

        setLocationRelativeTo(controller.getMainForm());
        setVisible(true);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu3 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenu4 = new javax.swing.JMenu();
        jMenuItem2 = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jMenu3.setText("Правка");

        jMenuItem1.setText("Удалить");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem1);

        jMenuBar1.add(jMenu3);

        jMenu4.setText("Просмотр");

        jMenuItem2.setText("Цены на дату");
        jMenuItem2.setToolTipText("");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItem2);

        jMenuBar1.add(jMenu4);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 279, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        try {
            boolean flagSelect = false;
            for (Object rows : tModel.getDataVector()) {
                if (((Vector) rows).get(0).toString().equals("true")) {
                    flagSelect = true;
                    break;
                }
            }

            if (flagSelect) {
                if (JOptionPane.showOptionDialog(null, "Удалить выделенные записи из справочника?", "Удаление", JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE, null, new Object[]{"Да", "Отмена"}, "Да") == JOptionPane.YES_OPTION) {

                    try {
                        spdb = new SkladHOPDB();
                        for (Object rows : tModel.getDataVector()) {
                            if (((Vector) rows).get(0).toString().equals("true"))
                                if (!spdb.deletePriceTMC(Integer.valueOf(((Vector) rows).get(1).toString()))) {
                                    JOptionPane.showMessageDialog(null, "Сбой удаления! ", "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
                                    break;
                                }
                        }
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(null, "Ошибка удаления! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
                    } finally {
                        spdb.disConn();
                    }

                    createCenaTMCTable(getPrice());
                }
            } else {
                JOptionPane.showMessageDialog(null, "Вы не выбрали записи для удаления!", "Внимание", javax.swing.JOptionPane.WARNING_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ошибка " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        try {
            JDateChooser date = new JDateChooser();

            date.setDate((Calendar.getInstance()).getTime());

            if (JOptionPane.showOptionDialog(null, date, "Актуальные цены на: ", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, new Object[]{"Да", "Отмена"}, date) == JOptionPane.YES_OPTION) {
                Vector dataTable = new Vector();
                try {
                    spdb = new SkladHOPDB();

                    dataTable = spdb.getPriceForDate(UtilFunctions.convertDateStrToLong(new SimpleDateFormat("dd.MM.yyyy").format(date.getDate())));

                } catch (Exception e) {
                    dataTable = new Vector();
                    JOptionPane.showMessageDialog(null, "Ошибка! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
                } finally {
                    spdb.disConn();
                }

                new SmallTableForm(CenaTMCSkHOForm.this, true, dataTable, new SimpleDateFormat("dd.MM.yyyy").format(date.getDate()));
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ошибка " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jMenuItem2ActionPerformed
    // End of variables declaration//GEN-END:variables

    private void init() {
        setTitle(" ХЭО. Цены ТМЦ");

        setMinimumSize(new Dimension(800, 600));
        setPreferredSize(new Dimension(800, 600));

        initMenu();

        controller.menuFormInitialisation(getClass().getName(), menu);

        osnova = new JPanel();
        searchPanel = new JPanel();
        searchButtPanel = new JPanel();
        upPanel = new JPanel();
        buttPanel = new JPanel();

        sStDate = new JDateChooser();
        eStDate = new JDateChooser();
        sInsDate = new JDateChooser();
        eInsDate = new JDateChooser();
        checkboxSt = new JCheckBox("Дата");
        checkboxIns = new JCheckBox("Корр-ка");
        title = new JLabel("Справочник цен ТМЦ");
        buttAdd = new JButton("Добавить");
        buttPrint = new JButton("Печать");
        buttOpen = new JButton("Открыть");
        buttCopy = new JButton("Копировать");
        buttHistory = new JButton("История");
        buttSearch = new JButton("Поиск");
        table = new JTable();
        filterHeader = new TableFilterHeader(table, AutoChoices.ENABLED);
        col = new Vector();
        buttonGroup = new ButtonGroup();
        jRadioButton1 = new JRadioButton();
        jRadioButton2 = new JRadioButton();

        osnova.setLayout(new BorderLayout(1, 1));
        osnova.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        searchPanel.setLayout(new ParagraphLayout());
        searchButtPanel.setLayout(new BorderLayout(1, 1));
        buttPanel.setLayout(new GridLayout(0, 5, 5, 5));
        buttPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        buttPanel.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        upPanel.setLayout(new BorderLayout(1, 1));
        upPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        searchButtPanel.setPreferredSize(new Dimension(200, 20));
        buttSearch.setPreferredSize(new Dimension(50, 20));
        sStDate.setPreferredSize(new Dimension(150, 20));
        eStDate.setPreferredSize(new Dimension(150, 20));
        sInsDate.setPreferredSize(new Dimension(150, 20));
        eInsDate.setPreferredSize(new Dimension(150, 20));

        title.setHorizontalAlignment(JLabel.CENTER);
        title.setFont(new Font("serif", Font.PLAIN, 24));

        jRadioButton1.setFont(new java.awt.Font("Dialog", 0, 13));
        jRadioButton2.setFont(new java.awt.Font("Dialog", 0, 13));

        jRadioButton1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jRadioButton2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        jRadioButton1.setText("все;");
        jRadioButton2.setText("актуальные;");

        jRadioButton1.setActionCommand("");
        jRadioButton2.setActionCommand(UtilSkladHO.PRICE_ACTUAL);

        jRadioButton1.setSelected(true);
        checkboxIns.setSelected(true);

        sStDate.setEnabled(false);
        eStDate.setEnabled(false);

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

        table.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                    buttOpen.doClick();
                }
            }
        });

        checkboxSt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                checkboxStActionPerformed(evt);
            }
        });

        checkboxIns.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                checkboxInsActionPerformed(evt);
            }
        });

        buttSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttSearchActionPerformed(evt);
            }
        });

        buttOpen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttOpenActionPerformed(evt);
            }
        });

        buttCopy.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttCopyActionPerformed(evt);
            }
        });

        buttAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttAddActionPerformed(evt);
            }
        });

        buttHistory.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttHistoryActionPerformed(evt);
            }
        });

        buttPrint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttPrintActionPerformed(evt);
            }
        });

        buttonGroup.add(jRadioButton1);
        buttonGroup.add(jRadioButton2);

        col.add("");
        col.add("idPrice");
        col.add("№");
        col.add("Вид");
        col.add("Пр-во");
        col.add("Шифр");
        col.add("Название");
        col.add("Артикул");
        col.add("Цена");
        col.add("Дата");
        col.add("Автор");
        col.add("Изм.");

        searchPanel.add(checkboxSt, ParagraphLayout.NEW_PARAGRAPH);
        searchPanel.add(new JLabel(" с "));
        searchPanel.add(sStDate);
        searchPanel.add(new JLabel(" по "));
        searchPanel.add(eStDate);
        searchPanel.add(checkboxIns, ParagraphLayout.NEW_PARAGRAPH);
        searchPanel.add(new JLabel(" с "));
        searchPanel.add(sInsDate);
        searchPanel.add(new JLabel(" по "));
        searchPanel.add(eInsDate);

        searchButtPanel.add(buttSearch, BorderLayout.SOUTH);

        upPanel.add(searchPanel, BorderLayout.CENTER);
        upPanel.add(searchButtPanel, BorderLayout.EAST);

        buttPanel.add(buttOpen);
        buttPanel.add(buttCopy);
        buttPanel.add(buttAdd);
        buttPanel.add(buttHistory);
        buttPanel.add(buttPrint);

        osnova.add(upPanel, BorderLayout.NORTH);
        osnova.add(new JScrollPane(table), BorderLayout.CENTER);
        osnova.add(buttPanel, BorderLayout.SOUTH);

        getContentPane().add(osnova);
        pack();
    }

    private void checkboxStActionPerformed(ActionEvent evt) {
        if (checkboxSt.isSelected()) {
            sStDate.setEnabled(true);
            eStDate.setEnabled(true);
        } else {
            sStDate.setEnabled(false);
            eStDate.setEnabled(false);
        }
    }

    private void checkboxInsActionPerformed(ActionEvent evt) {
        if (checkboxIns.isSelected()) {
            sInsDate.setEnabled(true);
            eInsDate.setEnabled(true);
        } else {
            sInsDate.setEnabled(false);
            eInsDate.setEnabled(false);
        }
    }

    private void buttAddActionPerformed(ActionEvent evt) {
        try {
            new CenaTMCItemForm(controller, true);

            if (UtilSkladHO.BUTT_ACTION_EDIT)
                createCenaTMCTable(getPrice());

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ошибка " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buttHistoryActionPerformed(ActionEvent evt) {
        try {
            if (table.getSelectedRow() != -1) {
                Vector dataTable = new Vector();
                String str = "";

                try {
                    spdb = new SkladHOPDB();

                    dataTable = spdb.getHistoryPrice(checkboxSt.isSelected(),
                            UtilFunctions.convertDateStrToLong(new SimpleDateFormat("dd.MM.yyyy").format(sStDate.getDate())),
                            UtilFunctions.convertDateStrToLong(new SimpleDateFormat("dd.MM.yyyy").format(eStDate.getDate())),
                            checkboxIns.isSelected(),
                            UtilFunctions.convertDateStrToLong(new SimpleDateFormat("dd.MM.yyyy").format(sInsDate.getDate())),
                            UtilFunctions.convertDateStrToLong(new SimpleDateFormat("dd.MM.yyyy").format(eInsDate.getDate())),
                            Integer.valueOf(table.getValueAt(table.getSelectedRow(), 2).toString()));

                } catch (Exception e) {
                    dataTable = new Vector();
                    JOptionPane.showMessageDialog(null, "Ошибка! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
                } finally {
                    spdb.disConn();
                }

                str += "ТМЦ №" + table.getValueAt(table.getSelectedRow(), 2);
                str += (checkboxSt.isSelected()) ? " период c " + new SimpleDateFormat("dd.MM.yyyy").format(sStDate.getDate())
                        + " по " + new SimpleDateFormat("dd.MM.yyyy").format(eStDate.getDate()) : "";
                str += (checkboxIns.isSelected()) ? " корр-ка c " + new SimpleDateFormat("dd.MM.yyyy").format(sInsDate.getDate())
                        + " по " + new SimpleDateFormat("dd.MM.yyyy").format(eInsDate.getDate()) : "";
                if (str.equals(""))
                    str = "За весь период.";

                new SmallTableForm(CenaTMCSkHOForm.this, true, str, dataTable);

            } else
                JOptionPane.showMessageDialog(null, "Вы не выбрали запись!", "Внимание", javax.swing.JOptionPane.WARNING_MESSAGE);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ошибка " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buttCopyActionPerformed(ActionEvent evt) {
        try {
            if (table.getSelectedRow() != -1) {
                Vector temp = new Vector();
                try {
                    temp.add(Integer.valueOf(table.getValueAt(table.getSelectedRow(), 2).toString()));
                    temp.add(table.getValueAt(table.getSelectedRow(), 6).toString());
                    temp.add(table.getValueAt(table.getSelectedRow(), 3).toString());
                    temp.add(table.getValueAt(table.getSelectedRow(), 4).toString());
                    temp.add(Integer.valueOf(table.getValueAt(table.getSelectedRow(), 5).toString()));
                    temp.add(table.getValueAt(table.getSelectedRow(), 7).toString());

                } catch (Exception e) {
                    temp = new Vector();
                    JOptionPane.showMessageDialog(null, "Ошибка " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
                }

                new CenaTMCItemForm(controller, true, temp);

                if (UtilSkladHO.BUTT_ACTION_EDIT)
                    createCenaTMCTable(getPrice());

            } else
                JOptionPane.showMessageDialog(null, "Вы не выбрали запись!", "Внимание", javax.swing.JOptionPane.WARNING_MESSAGE);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ошибка " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buttOpenActionPerformed(ActionEvent evt) {
        try {
            if (table.getSelectedRow() != -1) {
                Vector temp = new Vector();
                try {
                    temp.add(Integer.valueOf(table.getValueAt(table.getSelectedRow(), 2).toString()));
                    temp.add(table.getValueAt(table.getSelectedRow(), 6).toString());
                    temp.add(table.getValueAt(table.getSelectedRow(), 3).toString());
                    temp.add(table.getValueAt(table.getSelectedRow(), 4).toString());
                    temp.add(Integer.valueOf(table.getValueAt(table.getSelectedRow(), 5).toString()));
                    temp.add(table.getValueAt(table.getSelectedRow(), 7).toString());
                    temp.add(Double.valueOf(table.getValueAt(table.getSelectedRow(), 8).toString()));
                    temp.add(table.getValueAt(table.getSelectedRow(), 9).toString());
                    temp.add(table.getValueAt(table.getSelectedRow(), 10).toString());
                    temp.add(table.getValueAt(table.getSelectedRow(), 11).toString());

                } catch (Exception e) {
                    temp = new Vector();
                    JOptionPane.showMessageDialog(null, "Ошибка " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
                }

                new CenaTMCItemForm(controller, true, temp, "");

            } else
                JOptionPane.showMessageDialog(null, "Вы не выбрали запись!", "Внимание", javax.swing.JOptionPane.WARNING_MESSAGE);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ошибка " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buttSearchActionPerformed(ActionEvent evt) {
        try {
            createCenaTMCTable(getPrice());

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ошибка " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buttPrintActionPerformed(ActionEvent evt) {
        try {
            SkladHOOO oo = new SkladHOOO(title.getText(), tModel, table.getColumnModel());
            oo.createReport("DefaultTableAlbumFormatCheck.ots");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Ошибка ", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void initMenu() {
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu3 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenu4 = new javax.swing.JMenu();
        jMenuItem2 = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jMenu3.setText("Правка");

        jMenuItem1.setText("Удалить");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem1);

        jMenuBar1.add(jMenu3);

        jMenu4.setText("Просмотр");

        jMenuItem2.setText("Цены на дату");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItem2);

        jMenuBar1.add(jMenu4);

        setJMenuBar(jMenuBar1);

        jMenu3.setVisible(false);
        jMenuItem1.setVisible(false);

        menu.put("1", jMenu3);              //Правка
        menu.put("1-1", jMenuItem1);            //Удалить
        menu.put("2", jMenu4);              //Просмотр
        menu.put("2-1", jMenuItem2);            //Цены на дату
    }

    private void createCenaTMCTable(final Vector data) {
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
                return col == 0 ? true : false;
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

        table.getColumnModel().getColumn(0).setPreferredWidth(1);
        table.getColumnModel().getColumn(1).setMinWidth(0);
        table.getColumnModel().getColumn(1).setMaxWidth(0);
        table.getColumnModel().getColumn(2).setPreferredWidth(30);
        table.getColumnModel().getColumn(3).setPreferredWidth(50);
        table.getColumnModel().getColumn(4).setPreferredWidth(50);
        table.getColumnModel().getColumn(5).setMinWidth(0);
        table.getColumnModel().getColumn(5).setMaxWidth(0);
        table.getColumnModel().getColumn(6).setPreferredWidth(200);
        table.getColumnModel().getColumn(7).setPreferredWidth(80);
        table.getColumnModel().getColumn(8).setPreferredWidth(100);
        table.getColumnModel().getColumn(9).setPreferredWidth(70);
        table.getColumnModel().getColumn(10).setMinWidth(0);
        table.getColumnModel().getColumn(10).setMaxWidth(0);
        table.getColumnModel().getColumn(11).setPreferredWidth(70);

        sorter = new TableRowSorter<TableModel>(tModel);
        table.setRowSorter(sorter);
        table.getColumnModel().getColumn(0).setHeaderRenderer(new CheckBoxHeader(table.getTableHeader(), 0, ""));
    }

    private Vector getPrice() {
        Vector dataTable = new Vector();

        try {
            spdb = new SkladHOPDB();

            dataTable = spdb.getPrice(checkboxSt.isSelected(),
                    UtilFunctions.convertDateStrToLong(new SimpleDateFormat("dd.MM.yyyy").format(sStDate.getDate())),
                    UtilFunctions.convertDateStrToLong(new SimpleDateFormat("dd.MM.yyyy").format(eStDate.getDate())),
                    checkboxIns.isSelected(),
                    UtilFunctions.convertDateStrToLong(new SimpleDateFormat("dd.MM.yyyy").format(sInsDate.getDate())),
                    UtilFunctions.convertDateStrToLong(new SimpleDateFormat("dd.MM.yyyy").format(eInsDate.getDate())));

        } catch (Exception e) {
            dataTable = new Vector();
            JOptionPane.showMessageDialog(null, "Ошибка! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        } finally {
            spdb.disConn();
        }

        return dataTable;
    }

    public TreeMap getMenuMap() {
        return menu;
    }

    public void setFormVisible() {
        this.setVisible(false);
    }

    public void disposeForm() {
        dispose();
    }
}
