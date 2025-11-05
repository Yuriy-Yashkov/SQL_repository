package dept.sklad.ho;

import com.jhlabs.awt.ParagraphLayout;
import common.CheckBoxHeader;
import common.User;
import net.coderazzi.filters.gui.AutoChoices;
import net.coderazzi.filters.gui.TableFilterHeader;
import workDB.PDB;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.TreeMap;
import java.util.Vector;

/**
 *
 * @author lidashka
 */
public class SpravTMCGroupForm extends javax.swing.JDialog {
    private static TreeMap menu = new TreeMap();
    private User user = User.getInstance();

    private JPanel osnova;
    private JPanel buttPanel;
    private JPanel upPanel;
    private JButton buttSelect;
    private JButton buttEdit;
    private JButton buttAdd;
    private JButton buttClose;
    private JTextField groupTmcName;
    private JTable table;
    private DefaultTableModel tModel;
    private Vector col;
    private JButton buttPrint;
    private TableRowSorter<TableModel> sorter;
    private TableFilterHeader filterHeader;
    private JPanel centerPanel;
    private SkladHOPDB spdb;
    private JMenuBar jMenuBar1;
    private JMenu jMenu1;
    private JMenuItem jMenuItem1;
    private PDB pdb;

    public SpravTMCGroupForm(java.awt.Dialog parent, boolean modal) {
        super(parent, modal);
        setTitle("Группы ТМЦ");

        init();

        createGroupTmcТable(getGroup());

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
        UtilSkladHO.BUTT_ACTION_SELECT_GROUP = false;

        setMinimumSize(new Dimension(550, 450));

        initMenu();

        osnova = new JPanel();
        buttPanel = new JPanel();
        upPanel = new JPanel();
        centerPanel = new JPanel();
        buttSelect = new JButton("Выбрать");
        buttEdit = new JButton("Редактировать");
        buttAdd = new JButton("Добавить");
        buttClose = new JButton("Закрыть");
        buttPrint = new JButton("Печать");
        groupTmcName = new JTextField();
        col = new Vector();
        table = new JTable();
        filterHeader = new TableFilterHeader(table, AutoChoices.ENABLED);
        tModel = new DefaultTableModel();

        osnova.setLayout(new BorderLayout(1, 1));
        osnova.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        centerPanel.setLayout(new BorderLayout(1, 1));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        upPanel.setLayout(new ParagraphLayout());
        buttPanel.setLayout(new GridLayout(0, 4, 5, 5));
        buttPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        buttPanel.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);

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

        buttClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttCloseActionPerformed(evt);
            }
        });

        buttAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttAddActionPerformed(evt);
            }
        });

        buttSelect.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttSelectActionPerformed(evt);
            }
        });

        buttEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttEditActionPerformed(evt);
            }
        });

        buttPrint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttPrintActionPerformed(evt);
            }
        });

        col.add("");
        col.add("Код");
        col.add("Наименование");

        upPanel.add(new JLabel("Новая группа:"), ParagraphLayout.NEW_PARAGRAPH);
        upPanel.add(groupTmcName, ParagraphLayout.NEW_LINE_STRETCH_H);
        upPanel.add(buttAdd);

        centerPanel.add(new JScrollPane(table));

        buttPanel.add(buttClose);
        buttPanel.add(buttPrint);
        buttPanel.add(buttEdit);
        buttPanel.add(buttSelect);

        osnova.add(upPanel, BorderLayout.NORTH);
        osnova.add(centerPanel);
        osnova.add(buttPanel, BorderLayout.SOUTH);

        getContentPane().add(osnova);
        pack();

    }

    private void buttPrintActionPerformed(ActionEvent evt) {
        try {
            SkladHOOO oo = new SkladHOOO(SpravTMCGroupForm.this.getTitle(), tModel, table.getColumnModel());
            oo.createReport("DefaultTableAlbumFormatCheck.ots");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Ошибка ", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buttCloseActionPerformed(ActionEvent evt) {
        dispose();
    }

    private void buttSelectActionPerformed(ActionEvent evt) {
        try {
            if (table.getSelectedRow() != -1) {
                UtilSkladHO.SPRAV_GROUPTMC_ID = Integer.valueOf(table.getValueAt(table.getSelectedRow(), 1).toString());
                UtilSkladHO.SPRAV_GROUPTMC_NAME = table.getValueAt(table.getSelectedRow(), 2).toString();

                UtilSkladHO.BUTT_ACTION_SELECT_GROUP = true;

                dispose();
            } else
                JOptionPane.showMessageDialog(null, "Вы не выбрали запись!", "Внимание", javax.swing.JOptionPane.WARNING_MESSAGE);

        } catch (Exception e) {
            UtilSkladHO.SPRAV_GROUPTMC_ID = -1;
            UtilSkladHO.SPRAV_GROUPTMC_NAME = "";

            UtilSkladHO.BUTT_ACTION_SELECT_GROUP = false;
            JOptionPane.showMessageDialog(null, e.getMessage(), "Ошибка ", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buttEditActionPerformed(ActionEvent evt) {
        try {
            if (table.getSelectedRow() != -1) {
                JPanel panel = new JPanel();

                final JLabel textNum =
                        new JLabel(table.getValueAt(table.getSelectedRow(), 1).toString());

                final JTextField textName =
                        new JTextField(table.getValueAt(table.getSelectedRow(), 2).toString());

                panel.setLayout(new ParagraphLayout());

                textName.setPreferredSize(new Dimension(350, 20));
                textName.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

                panel.add(new JLabel("Группа №:"), ParagraphLayout.NEW_PARAGRAPH);
                panel.add(textNum);
                panel.add(textName);

                if (JOptionPane.showOptionDialog(null, panel, "Редактировать", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, new Object[]{"Сохранить", "Отмена"}, "Отмена") == JOptionPane.YES_OPTION) {
                    try {

                        spdb = new SkladHOPDB();

                        if (spdb.editGroupTmcSkladHO(Integer.valueOf(table.getValueAt(table.getSelectedRow(), 1).toString()),
                                textName.getText().trim().toUpperCase(),
                                Integer.valueOf(user.getIdEmployee()))) {

                            JOptionPane.showMessageDialog(null, "Запись успешно изменена! ", "Завершено", javax.swing.JOptionPane.INFORMATION_MESSAGE);
                        }

                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(null, "Ошибка! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
                    } finally {
                        spdb.disConn();
                    }
                }

            } else
                JOptionPane.showMessageDialog(null, "Вы не выбрали запись!", "Внимание", javax.swing.JOptionPane.WARNING_MESSAGE);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ошибка! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }

        createGroupTmcТable(getGroup());
    }

    private void buttAddActionPerformed(ActionEvent evt) {
        try {
            spdb = new SkladHOPDB();

            if (spdb.addGroupTmcSkladHO(groupTmcName.getText().trim().toUpperCase(), Integer.valueOf(user.getIdEmployee()))) {
                JOptionPane.showMessageDialog(null, "Запись успешно добавлена! ", "Завершено", javax.swing.JOptionPane.INFORMATION_MESSAGE);
            }

        } catch (Exception e) {

            JOptionPane.showMessageDialog(null, "Ошибка! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        } finally {
            spdb.disConn();
        }

        createGroupTmcТable(getGroup());
    }

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {
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
                                if (!spdb.deleteGroupTMC(Integer.valueOf(((Vector) rows).get(1).toString()))) {
                                    JOptionPane.showMessageDialog(null, "Сбой удаления! ", "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
                                    break;
                                }
                        }
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(null, "Ошибка удаления! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
                    } finally {
                        spdb.disConn();
                    }

                    createGroupTmcТable(getGroup());
                }
            } else {
                JOptionPane.showMessageDialog(null, "Вы не выбрали записи для удаления!", "Внимание", javax.swing.JOptionPane.WARNING_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ошибка " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void createGroupTmcТable(final Vector row) {
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
        table.getColumnModel().getColumn(1).setPreferredWidth(15);
        table.getColumnModel().getColumn(2).setPreferredWidth(250);

        sorter = new TableRowSorter<TableModel>(tModel);
        table.setRowSorter(sorter);
        table.getColumnModel().getColumn(0).setHeaderRenderer(new CheckBoxHeader(table.getTableHeader(), 0, ""));
    }

    private Vector getGroup() {
        Vector dataTable = new Vector();

        try {
            spdb = new SkladHOPDB();

            dataTable = spdb.getAllGroupTMC();

        } catch (Exception e) {
            dataTable = new Vector();
            JOptionPane.showMessageDialog(null, "Ошибка! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        } finally {
            spdb.disConn();
        }

        return dataTable;
    }

    private void initMenu() {
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();

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

        setJMenuBar(jMenuBar1);

        jMenu1.setVisible(false);
        jMenuItem1.setVisible(false);

        menu.put("1", jMenu1);              //Правка
        menu.put("1-1", jMenuItem1);            //Удалить

        String right = null;
        try {
            pdb = new PDB();
            right = pdb.getMenuRights(user.getName(), user.getPassword(), SpravTMCSkHOForm.class.getName());

            if (right != null) {
                String aRights[] = right.split(",");
                for (int i = 0; i < aRights.length; i++) {
                    JMenuItem mi = (JMenuItem) menu.get(aRights[i]);
                    if (mi != null) mi.setVisible(true);
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ошибка! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        } finally {
            pdb.disConn();
        }
    }
}
