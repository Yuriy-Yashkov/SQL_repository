package dept.sklad.ho;

import by.march8.ecs.MainController;
import com.jhlabs.awt.ParagraphLayout;
import com.toedter.calendar.JDateChooser;
import common.CheckBoxHeader;
import common.FormMenu;
import common.ProgressBar;
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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.TreeMap;
import java.util.Vector;

/**
 *
 * @author lidashka
 */
@SuppressWarnings("all")
public class SpravTMCSkHOForm extends javax.swing.JDialog implements FormMenu {
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
    private JTextField groupTmc;
    private JTextField nameTmc;
    private JTextField sarTmc;
    private JTextField narTmc;
    private JTextField numTmc;
    private JComboBox vidTmc;
    private JComboBox madeTmc;
    private JDateChooser sStDate;
    private JButton buttSearch;
    private JButton buttAdd;
    private JButton buttSelect;
    private JButton buttEdit;
    private JButton buttOpen;
    private JButton buttCopy;
    private JButton buttRefresh;
    private JButton buttClean;
    private JTable table;
    private JLabel title;
    private Vector col;
    private DefaultTableModel tModel;
    private TableRowSorter<TableModel> sorter;
    private TableFilterHeader filterHeader;
    private ProgressBar pb;
    private Vector dataTable;
    private MainController controller;
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;

    public SpravTMCSkHOForm() {
        initMenu();
    }

    public SpravTMCSkHOForm(MainController mainController, boolean modal) {
        super(mainController.getMainForm(), modal);
        controller = mainController;

        if (user.getFio() != null) {
            initMenu();
            init();

            createSpravTMCTable(getTmc(new Vector()));

            setLocationRelativeTo(controller.getMainForm());
            setVisible(true);
        } else
            JOptionPane.showMessageDialog(null, "Такой логин и пароль не присвоен ни одному пользователю. \n Обратитесь к администратору! ", "Вход", javax.swing.JOptionPane.WARNING_MESSAGE);

    }
    public SpravTMCSkHOForm(MainController mainController, boolean modal, String date) {
        super(mainController.getMainForm(), modal);
        controller = mainController;

        initMenu();
        init();

        try {
            sStDate.setDate(new SimpleDateFormat("dd-MM-yyyy").parse(date));
        } catch (Exception e) {
            sStDate.setDate((Calendar.getInstance()).getTime());
        }

        buttSelect.setVisible(true);
        buttPanel.add(buttSelect);

        createSpravTMCTable(getTmc(new Vector()));

        setLocationRelativeTo(controller.getMainForm());
        setVisible(true);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        jMenuItem2 = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jMenu1.setText("Правка");

        jMenuItem1.setText("Удалить");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem1);

        jMenuBar1.add(jMenu1);

        jMenu2.setText("Просмотр");

        jMenuItem2.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F3, 0));
        jMenuItem2.setText("Остатки      ");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem2);

        jMenuBar1.add(jMenu2);

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
                JOptionPane.showMessageDialog(null, "Внимание! Убедитесь, что выбраны нужные записи для удаления!");
                if (JOptionPane.showOptionDialog(null, "Удалить выделенные записи из справочника?", "Удаление", JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE, null, new Object[]{"Да", "Отмена"}, "Да") == JOptionPane.YES_OPTION) {

                    try {
                        spdb = new SkladHOPDB();
                        for (Object rows : tModel.getDataVector()) {
                            if (((Vector) rows).get(0).toString().equals("true"))
                                if (!spdb.deleteTMC(Integer.valueOf(((Vector) rows).get(1).toString()))) {
                                    JOptionPane.showMessageDialog(null, "Сбой удаления! ", "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
                                    break;
                                }
                        }
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(null, "Ошибка удаления! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
                    } finally {
                        spdb.disConn();
                    }

                    createSpravTMCTable(getTmc(new Vector()));
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
            Vector temp = new Vector();


            for (int i = 0; i < table.getRowCount(); i++) {
                if ((Boolean) table.getValueAt(i, 0)) {
                    temp.add(table.getValueAt(i, 1));
                }
            }

            if (temp.size() > 0) {

                new SmallTableForm(SpravTMCSkHOForm.this,
                        true,
                        new SimpleDateFormat("dd.MM.yyyy").format(sStDate.getDate()),
                        getTmc(temp));

            } else
                JOptionPane.showMessageDialog(null,
                        "Вы не выбрали запись в таблице.\n",
                        "Внимание",
                        javax.swing.JOptionPane.WARNING_MESSAGE);


        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ошибка " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jMenuItem2ActionPerformed
    // End of variables declaration//GEN-END:variables

    private void init() {
        controller.menuFormInitialisation(getClass().getName(), menu);
        UtilSkladHO.BUTT_ACTION_SELECT_SPRAV = false;

        UtilSkladHO.SPRAV_TMC_ID = -1;
        UtilSkladHO.SPRAV_TMC_GROUP = "";
        UtilSkladHO.SPRAV_TMC_NAME = "";
        UtilSkladHO.SPRAV_TMC_SAR = 0;
        UtilSkladHO.SPRAV_TMC_NAR = "";
        UtilSkladHO.SPRAV_TMC_VID = "";
        UtilSkladHO.SPRAV_TMC_MADE = "";
        UtilSkladHO.SPRAV_TMC_EDIZM = "";

        setTitle(" ХЭО. Справочник ТМЦ");

        setMinimumSize(new Dimension(690, 550));
        setPreferredSize(new Dimension(850, 650));

        osnova = new JPanel();
        searchPanel = new JPanel();
        searchButtPanel = new JPanel();
        upPanel = new JPanel();
        buttPanel = new JPanel();
        groupTmc = new JTextField();
        nameTmc = new JTextField();
        sarTmc = new JTextField();
        narTmc = new JTextField();
        numTmc = new JTextField();
        sStDate = new JDateChooser((Calendar.getInstance()).getTime());
        title = new JLabel("Справочник ТМЦ");
        vidTmc = new JComboBox();
        madeTmc = new JComboBox();
        buttSearch = new JButton("Поиск");
        buttAdd = new JButton("Добавить");
        buttSelect = new JButton("Выбрать");
        buttEdit = new JButton("Редактировать");
        buttOpen = new JButton("Открыть");
        buttCopy = new JButton("Копировать");
        buttRefresh = new JButton("Обновить");
        buttClean = new JButton("Очистить");
        table = new JTable();
        filterHeader = new TableFilterHeader(table, AutoChoices.ENABLED);
        col = new Vector();

        osnova.setLayout(new BorderLayout(1, 1));
        osnova.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        searchPanel.setLayout(new ParagraphLayout());
        searchButtPanel.setLayout(new GridLayout(0, 5, 5, 5));
        searchButtPanel.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        buttPanel.setLayout(new GridLayout(0, 5, 5, 5));
        buttPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        buttPanel.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        upPanel.setLayout(new BorderLayout(1, 1));
        upPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        numTmc.setPreferredSize(new Dimension(180, 20));
        sarTmc.setPreferredSize(new Dimension(180, 20));
        narTmc.setPreferredSize(new Dimension(180, 20));
        nameTmc.setPreferredSize(new Dimension(300, 20));
        groupTmc.setPreferredSize(new Dimension(300, 20));
        vidTmc.setPreferredSize(new Dimension(180, 20));
        madeTmc.setPreferredSize(new Dimension(180, 20));
        buttSearch.setPreferredSize(new Dimension(50, 20));
        sStDate.setPreferredSize(new Dimension(120, 20));

        title.setHorizontalAlignment(JLabel.CENTER);
        title.setFont(new Font("serif", Font.PLAIN, 24));

        buttSelect.setVisible(false);

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

        table.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() > 1 && buttSelect.isVisible()) {
                    try {
                        buttSelect.doClick();
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(null, "Ошибка! " + ex.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        table.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                    buttOpen.doClick();
                }
            }
        });

        table.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_F2) {
                    buttAdd.doClick();
                }
            }
        });

        buttSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttSearchActionPerformed(evt);
            }
        });

        buttSelect.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttSelectActionPerformed(evt);
            }
        });

        buttOpen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttOpenActionPerformed(evt);
            }
        });

        buttRefresh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttRefreshActionPerformed(evt);
            }
        });

        buttClean.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttCleanActionPerformed(evt);
            }
        });

        buttAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttAddActionPerformed(evt);
            }
        });

        buttEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttEditActionPerformed(evt);
            }
        });

        buttCopy.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttCopyActionPerformed(evt);
            }
        });

        nameTmc.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                textTableSearchKeyPressed(evt);
            }
        });

        groupTmc.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                textTableSearchKeyPressed(evt);
            }
        });

        sarTmc.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                textTableSearchKeyPressed(evt);
            }
        });

        narTmc.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                textTableSearchKeyPressed(evt);
            }
        });

        numTmc.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                textTableSearchKeyPressed(evt);
            }
        });

        vidTmc.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                textTableSearchKeyPressed(evt);
            }
        });

        madeTmc.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                textTableSearchKeyPressed(evt);
            }
        });

        vidTmc.addItem("");
        vidTmc.addItem(UtilSkladHO.SKLAD_VID_1);
        vidTmc.addItem(UtilSkladHO.SKLAD_VID_2);

        madeTmc.addItem("");
        madeTmc.addItem(UtilSkladHO.SKLAD_MADE_1);
        madeTmc.addItem(UtilSkladHO.SKLAD_MADE_2);

        col.add("");
        col.add("№");
        col.add("idГруппа");
        col.add("Группа");
        col.add("Наименование");
        col.add("Артикул");
        col.add("Шифр");
        col.add("Вид");
        col.add("Пр-во");
        col.add("Ед.изм.");
        col.add("Цена");
        col.add("Примечание");
        col.add("Создал");
        col.add("Дата");
        col.add("Изменил");
        col.add("Дата");

        searchPanel.add(new JLabel("Наим.:"), ParagraphLayout.NEW_PARAGRAPH);
        searchPanel.add(nameTmc, ParagraphLayout.NEW_LINE_STRETCH_H);
        searchPanel.add(new JLabel("    Артикул:"));
        searchPanel.add(narTmc);
        searchPanel.add(new JLabel("    Дата:"));
        searchPanel.add(sStDate);

        searchButtPanel.add(buttRefresh);
        searchButtPanel.add(buttClean);
        searchButtPanel.add(buttSearch);

        //upPanel.add(title, BorderLayout.NORTH);
        upPanel.add(searchPanel, BorderLayout.CENTER);
        upPanel.add(searchButtPanel, BorderLayout.SOUTH);

        buttPanel.add(buttOpen);
        buttPanel.add(buttEdit);
        buttPanel.add(buttCopy);
        buttPanel.add(buttAdd);

        osnova.add(upPanel, BorderLayout.NORTH);
        osnova.add(new JScrollPane(table), BorderLayout.CENTER);
        osnova.add(buttPanel, BorderLayout.SOUTH);

        getContentPane().add(osnova);
        pack();
    }

    private void buttSearchActionPerformed(ActionEvent evt) {
        try {
            java.util.List<RowFilter<Object, Object>> filters = new ArrayList<RowFilter<Object, Object>>(3);
            filters.add(RowFilter.regexFilter("(?iu)" + nameTmc.getText().trim(), 2));
            filters.add(RowFilter.regexFilter("(?iu)" + sarTmc.getText().trim(), 4));
            filters.add(RowFilter.regexFilter("(?iu)" + narTmc.getText().trim(), 3));
            filters.add(RowFilter.regexFilter("(?iu)" + numTmc.getText().trim(), 1));
            filters.add(RowFilter.regexFilter("(?iu)" + vidTmc.getSelectedItem().toString(), 5));
            filters.add(RowFilter.regexFilter("(?iu)" + madeTmc.getSelectedItem().toString(), 6));

            RowFilter<Object, Object> serviceFilter = RowFilter.andFilter(filters);
            sorter.setRowFilter(serviceFilter);

            if (table.getRowCount() == 0) {
                JOptionPane.showMessageDialog(null, "По заданному запросу ничего не найдено!", "Внимание", javax.swing.JOptionPane.WARNING_MESSAGE);
                sorter.setRowFilter(null);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ошибка " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buttAddActionPerformed(ActionEvent evt) {
        try {
            new SpravTMCItemForm(SpravTMCSkHOForm.this, true);

            if (UtilSkladHO.BUTT_ACTION_EDIT)
                createSpravTMCTable(getTmc(new Vector()));

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ошибка " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buttEditActionPerformed(ActionEvent evt) {
        try {
            if (table.getSelectedRow() != -1) {
                new SpravTMCItemForm(SpravTMCSkHOForm.this, true,
                        Integer.valueOf(table.getValueAt(table.getSelectedRow(), 1).toString()),
                        getItemTable());

                if (UtilSkladHO.BUTT_ACTION_EDIT)
                    createSpravTMCTable(getTmc(new Vector()));
            } else
                JOptionPane.showMessageDialog(null, "Вы не выбрали запись!", "Внимание", javax.swing.JOptionPane.WARNING_MESSAGE);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ошибка " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buttCopyActionPerformed(ActionEvent evt) {
        try {
            if (table.getSelectedRow() != -1) {
                new SpravTMCItemForm(SpravTMCSkHOForm.this, true,
                        getItemTable(),
                        0);

                if (UtilSkladHO.BUTT_ACTION_EDIT)
                    createSpravTMCTable(getTmc(new Vector()));
            } else
                JOptionPane.showMessageDialog(null, "Вы не выбрали запись!", "Внимание", javax.swing.JOptionPane.WARNING_MESSAGE);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ошибка " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buttOpenActionPerformed(ActionEvent evt) {
        try {
            if (table.getSelectedRow() != -1) {
                new SpravTMCItemForm(SpravTMCSkHOForm.this, true, getItemTable());

            } else
                JOptionPane.showMessageDialog(null, "Вы не выбрали запись!", "Внимание", javax.swing.JOptionPane.WARNING_MESSAGE);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ошибка " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buttRefreshActionPerformed(ActionEvent evt) {
        try {
            createSpravTMCTable(getTmc(new Vector()));

            buttClean.doClick();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ошибка " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buttCleanActionPerformed(ActionEvent evt) {
        try {
            nameTmc.setText("");
            narTmc.setText("");
            sarTmc.setText("");
            numTmc.setText("");
            vidTmc.setSelectedItem("");
            madeTmc.setSelectedItem("");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ошибка " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buttSelectActionPerformed(ActionEvent evt) {
        try {
            if (table.getSelectedRow() != -1) {
                UtilSkladHO.SPRAV_TMC_ID = Integer.valueOf(table.getValueAt(table.getSelectedRow(), 1).toString());
                UtilSkladHO.SPRAV_TMC_GROUP = table.getValueAt(table.getSelectedRow(), 3).toString();
                UtilSkladHO.SPRAV_TMC_NAME = table.getValueAt(table.getSelectedRow(), 4).toString();
                UtilSkladHO.SPRAV_TMC_SAR = Integer.valueOf(table.getValueAt(table.getSelectedRow(), 6).toString());
                UtilSkladHO.SPRAV_TMC_NAR = table.getValueAt(table.getSelectedRow(), 5).toString();
                UtilSkladHO.SPRAV_TMC_VID = table.getValueAt(table.getSelectedRow(), 7).toString();
                UtilSkladHO.SPRAV_TMC_MADE = table.getValueAt(table.getSelectedRow(), 8).toString();
                UtilSkladHO.SPRAV_TMC_EDIZM = table.getValueAt(table.getSelectedRow(), 9).toString();

                UtilSkladHO.BUTT_ACTION_SELECT_SPRAV = true;

                dispose();
            } else
                JOptionPane.showMessageDialog(null, "Вы не выбрали запись!", "Внимание", javax.swing.JOptionPane.WARNING_MESSAGE);

        } catch (Exception e) {
            UtilSkladHO.SPRAV_TMC_ID = -1;
            UtilSkladHO.SPRAV_TMC_GROUP = "";
            UtilSkladHO.SPRAV_TMC_NAME = "";
            UtilSkladHO.SPRAV_TMC_SAR = 0;
            UtilSkladHO.SPRAV_TMC_NAR = "";
            UtilSkladHO.SPRAV_TMC_VID = "";
            UtilSkladHO.SPRAV_TMC_MADE = "";
            UtilSkladHO.SPRAV_TMC_EDIZM = "";

            UtilSkladHO.BUTT_ACTION_SELECT_SPRAV = false;
            JOptionPane.showMessageDialog(null, "Ошибка " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void textTableSearchKeyPressed(java.awt.event.KeyEvent evt) {
        if (evt.getKeyCode() == evt.VK_ENTER) buttSearch.doClick();
    }

    private void initMenu() {
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu2 = new javax.swing.JMenu();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();


        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);


        jMenu2.setText("Просмотр");

        jMenuItem2.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F3, 0));
        jMenuItem2.setText("Остатки");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem2);

        jMenuBar1.add(jMenu2);

        jMenu1.setText("Удалить");

        jMenuItem1.setText("Удалить");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem1);

        jMenuBar1.add(jMenu1);

        setJMenuBar(jMenuBar1);

        jMenu1.setVisible(false);
        jMenuItem1.setVisible(false);

        menu.put("1", jMenu1);              //Правка
        menu.put("1-1", jMenuItem1);            //Удалить
        menu.put("2", jMenu2);              //Просмотр
        menu.put("2-1", jMenuItem2);            //Остатки
    }

    private void createSpravTMCTable(final Vector data) {
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

        table.getColumnModel().getColumn(0).setPreferredWidth(10);
        table.getColumnModel().getColumn(1).setPreferredWidth(15);
        table.getColumnModel().getColumn(2).setMinWidth(0);
        table.getColumnModel().getColumn(2).setMaxWidth(0);
        table.getColumnModel().getColumn(3).setPreferredWidth(100);
        table.getColumnModel().getColumn(4).setPreferredWidth(120);
        table.getColumnModel().getColumn(5).setPreferredWidth(70);
        table.getColumnModel().getColumn(6).setPreferredWidth(70);
        table.getColumnModel().getColumn(7).setPreferredWidth(50);
        table.getColumnModel().getColumn(8).setPreferredWidth(50);
        table.getColumnModel().getColumn(9).setPreferredWidth(15);
        table.getColumnModel().getColumn(10).setPreferredWidth(70);
        table.getColumnModel().getColumn(11).setMinWidth(0);
        table.getColumnModel().getColumn(11).setMaxWidth(0);
        table.getColumnModel().getColumn(12).setMinWidth(0);
        table.getColumnModel().getColumn(12).setMaxWidth(0);
        table.getColumnModel().getColumn(13).setMinWidth(0);
        table.getColumnModel().getColumn(13).setMaxWidth(0);
        table.getColumnModel().getColumn(14).setMinWidth(0);
        table.getColumnModel().getColumn(14).setMaxWidth(0);
        table.getColumnModel().getColumn(15).setMinWidth(0);
        table.getColumnModel().getColumn(15).setMaxWidth(0);

        sorter = new TableRowSorter<TableModel>(tModel);
        table.setRowSorter(sorter);
        table.getColumnModel().getColumn(0).setHeaderRenderer(new CheckBoxHeader(table.getTableHeader(), 0, ""));

    }

    private Vector getTmc(final Vector temp) {
        dataTable = new Vector();

        try {
            pb = new ProgressBar(this, false, "Сбор данных ...");
            SwingWorker sw = new SwingWorker() {
                private SkladHOPDB spdb;

                protected Object doInBackground() {
                    try {
                        spdb = new SkladHOPDB();

                        if (temp.size() > 0) {

                            final java.util.Date d = sStDate.getDate();
                            d.setDate(1);

                            dataTable = spdb.getOst1Date(
                                    UtilFunctions.convertDateStrToLong(new SimpleDateFormat("dd.MM.yyyy").format(d)),
                                    UtilFunctions.convertDateStrToLong(new SimpleDateFormat("dd.MM.yyyy").format(d)),
                                    UtilFunctions.convertDateStrToLong(new SimpleDateFormat("dd.MM.yyyy").format(sStDate.getDate())),
                                    false,
                                    temp);
                        } else {
                            dataTable = spdb.getAllTMC(UtilFunctions.convertDateStrToLong(new SimpleDateFormat("dd.MM.yyyy").format(sStDate.getDate())));
                        }

                    } catch (Exception e) {
                        dataTable = new Vector();
                        JOptionPane.showMessageDialog(null,
                                "Ошибка! " + e.getMessage(),
                                "Ошибка",
                                javax.swing.JOptionPane.ERROR_MESSAGE);
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
            dataTable = new Vector();
            JOptionPane.showMessageDialog(null,
                    "Ошибка! " + e.getMessage(),
                    "Ошибка",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
        }
        return dataTable;
    }

    private Vector getItemTable() {
        Vector temp = new Vector();

        try {
            temp.add(Integer.valueOf(table.getValueAt(table.getSelectedRow(), 1).toString()));
            temp.add(Integer.valueOf(table.getValueAt(table.getSelectedRow(), 2).toString()));
            temp.add(table.getValueAt(table.getSelectedRow(), 3).toString());
            temp.add(table.getValueAt(table.getSelectedRow(), 4).toString());
            temp.add(table.getValueAt(table.getSelectedRow(), 7).toString());
            temp.add(table.getValueAt(table.getSelectedRow(), 8).toString());
            temp.add(Integer.valueOf(table.getValueAt(table.getSelectedRow(), 6).toString()));
            temp.add(table.getValueAt(table.getSelectedRow(), 5).toString());
            temp.add(table.getValueAt(table.getSelectedRow(), 9).toString());
            temp.add(table.getValueAt(table.getSelectedRow(), 11).toString());
            temp.add(table.getValueAt(table.getSelectedRow(), 12).toString());
            temp.add(table.getValueAt(table.getSelectedRow(), 13).toString());
            temp.add(table.getValueAt(table.getSelectedRow(), 14).toString());
            temp.add(table.getValueAt(table.getSelectedRow(), 15).toString());

        } catch (Exception e) {
            temp = new Vector();
            JOptionPane.showMessageDialog(null, "Ошибка! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }

        return temp;
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
