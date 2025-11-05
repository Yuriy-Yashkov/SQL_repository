package dept.production.planning;

import by.march8.ecs.MainController;
import com.jhlabs.awt.ParagraphLayout;
import com.toedter.calendar.JDateChooser;
import common.CheckBoxHeader;
import common.FormMenu;
import common.Item;
import common.ProgressBar;
import common.User;
import common.UtilFunctions;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Map;
import java.util.TreeMap;
import java.util.Vector;

/**
 *
 * @author lidashka
 */
public class PlanProductioForm extends javax.swing.JDialog implements FormMenu {
    private static Map menu = new TreeMap();
    private ProgressBar pb;
    private PlanPDB ppdb;
    private DefaultTableModel tModel;
    private Vector col;
    private Vector row;
    private TableRowSorter sorter;
    private DefaultTableCellRenderer renderer;
    private int minSelectedRow = -1;
    private int maxSelectedRow = -1;
    private boolean tableModelListenerIsChanging = false;
    private User user = User.getInstance();
    private JPanel osnova;
    private JPanel searchPanel;
    private JPanel searchButtPanel;
    private JPanel buttPanel;
    private JPanel upPanel;
    private JDateChooser sStDate;
    private JDateChooser eStDate;
    private JDateChooser sInsDate;
    private JDateChooser eInsDate;
    private JButton buttSearch;
    private JButton buttTableSearch;
    private JButton buttCopy;
    private JButton buttOpen;
    private JButton buttSelect;
    private JButton buttEdit;
    private JTable table;
    private JTextField listNum;
    private JTextField textTableSearch;
    private JLabel title;
    private JCheckBox checkboxSt;
    private JCheckBox checkboxIns;
    private JComboBox dept;
    private ButtonGroup buttonGroup;
    private JRadioButton jRadioButton1;
    private JRadioButton jRadioButton2;
    private JRadioButton jRadioButton3;
    private JRadioButton jRadioButton4;
    private JRadioButton jRadioButton5;
    private Vector dataPlan;
    private Vector dataItem;
    private Vector dataItemDetal;
    private Vector dataItemConv;
    private Vector dataItemDekad;
    private Vector dataReport;
    private Vector dataSearch;
    private JButton buttPowerSearch;
    private MainController controller;
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenu jMenu4;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JMenuItem jMenuItem5;
    private javax.swing.JMenuItem jMenuItem6;
    private javax.swing.JMenuItem jMenuItem7;
    private javax.swing.JMenuItem jMenuItem8;
    private javax.swing.JMenuItem jMenuItem9;
    public PlanProductioForm() {
        initMenu();
    }
    public PlanProductioForm(MainController mainController, boolean modal, boolean flagSelectDialog) {
        super(mainController.getMainForm(), modal);
        controller = mainController;

        if (user.getFio() != null) {

            cleanConstants();
            initMenu();
            initPropSetting();
            initData();
            init();
            if (flagSelectDialog)
                initSelectDialog();

            java.util.Date d = (Calendar.getInstance()).getTime();
            d.setDate(1);

            sStDate.setDate(d);
            eStDate.setDate((Calendar.getInstance()).getTime());

            sInsDate.setDate(d);
            eInsDate.setDate((Calendar.getInstance()).getTime());

            createPlanTable(getPlans());

            setLocationRelativeTo(controller.getMainForm());
            setVisible(true);
        } else
            JOptionPane.showMessageDialog(null, "Такой логин и пароль не присвоен ни одному пользователю. \n Обратитесь к администратору! ", "Вход", javax.swing.JOptionPane.WARNING_MESSAGE);

    }
    public PlanProductioForm(MainController mainController, boolean modal, String nameType) {
        super(mainController.getMainForm(), modal);
        controller = mainController;

        cleanConstants();
        initMenu();
        initPropSetting();
        initData();
        init();
        initSelectDialog();

        searchButtPanel.add(new JLabel());
        searchButtPanel.add(new JLabel());
        searchButtPanel.add(buttPowerSearch);

        java.util.Date d = (Calendar.getInstance()).getTime();
        d.setDate(1);
        d.setMonth(d.getMonth() - 3);

        sInsDate.setDate(d);
        eInsDate.setDate((Calendar.getInstance()).getTime());

        sStDate.setDate(d);
        eStDate.setDate((Calendar.getInstance()).getTime());

        createPlanTable(getPlans(nameType.trim().toLowerCase()));

        setLocationRelativeTo(controller.getMainForm());
        setVisible(true);

    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jMenuItem5 = new javax.swing.JMenuItem();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem6 = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenuItem7 = new javax.swing.JMenuItem();
        jMenu3 = new javax.swing.JMenu();
        jMenuItem3 = new javax.swing.JMenuItem();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem4 = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        jMenuItem8 = new javax.swing.JMenuItem();
        jMenu4 = new javax.swing.JMenu();
        jMenuItem9 = new javax.swing.JMenuItem();

        jMenuItem5.setText("jMenuItem5");

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jMenu1.setText("Правка");

        jMenuItem6.setText("Удалить");
        jMenuItem6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem6ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem6);

        jMenuItem2.setText("Импорт из DBF");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem2);

        jMenuItem7.setText("Объединить ");
        jMenuItem7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem7ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem7);

        jMenuBar1.add(jMenu1);

        jMenu3.setText("Сервис");

        jMenuItem3.setText("Различия");
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem3);

        jMenuItem1.setText("Обновить спецификации");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem1);

        jMenuItem4.setText("Округление");
        jMenuItem4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem4ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem4);

        jMenuBar1.add(jMenu3);

        jMenu2.setText("Документы");

        jMenuItem8.setText("Трудоемкость (з/ш цех)");
        jMenuItem8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem8ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem8);

        jMenuBar1.add(jMenu2);

        jMenu4.setText("Просмотр");

        jMenuItem9.setText("Расширенный поиск");
        jMenuItem9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem9ActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItem9);

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

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        new PlanImportForm(this, true);
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void jMenuItem4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem4ActionPerformed
        try {
            JSpinner rounding = new JSpinner();
            rounding.setValue(UtilPlan.ROUNDING_NORM);
            if (JOptionPane.showOptionDialog(null, rounding, "Нормы округлять до: ", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, new Object[]{"Да", "Отмена"}, rounding) == JOptionPane.YES_OPTION) {
                UtilPlan.ROUNDING_NORM = Integer.valueOf(rounding.getValue().toString());
                UtilFunctions.setSettingPropFile(rounding.getValue().toString(), UtilPlan.SETTING_ROUNDING_NORM);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ошибка! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jMenuItem4ActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        try {
            if (table.getSelectedRow() != -1) {
                if (JOptionPane.showOptionDialog(null, "Все спецификации в плане № " + table.getValueAt(table.getSelectedRow(), 2).toString() + " \nбудут обновлены! Обновление займёт некоторое время!\nОбновить?", "Внимание!", JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE, null, new Object[]{"Да", "Отмена"}, "Да") == JOptionPane.YES_OPTION) {
                    pb = new ProgressBar(PlanProductioForm.this, false, "Обновление спецификаций плана №" + table.getValueAt(table.getSelectedRow(), 2).toString() + " ...");
                    SwingWorker sw = new SwingWorker() {
                        protected Object doInBackground() {
                            try {
                                ppdb = new PlanPDB();

                                if (ppdb.updateSpecInPlanProduction(Integer.valueOf(table.getValueAt(table.getSelectedRow(), 2).toString()))) {
                                    JOptionPane.showMessageDialog(null, "Обновление спецификаций плана завершено успешно!", "Внимание", javax.swing.JOptionPane.INFORMATION_MESSAGE);
                                }
                            } catch (Exception e) {
                                JOptionPane.showMessageDialog(null, "Сбой обновления спецификаций плана!" + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
                            } finally {
                                ppdb.disConn();
                            }
                            return null;
                        }

                        protected void done() {
                            pb.dispose();
                        }
                    };
                    sw.execute();
                    pb.setVisible(true);
                }
            } else
                JOptionPane.showMessageDialog(null, "Вы не выбрали план производства для обновления спецификаций!", "Внимание", javax.swing.JOptionPane.WARNING_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ошибка! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jMenuItem6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem6ActionPerformed
        try {
            boolean flagSelect = false;
            for (Object rows : tModel.getDataVector()) {
                if (((Vector) rows).get(0).toString().equals("true")) {
                    flagSelect = true;
                    break;
                }
            }

            if (flagSelect) {
                if (JOptionPane.showOptionDialog(null, "Удалить выделенные планы производства?", "Удаление", JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE, null, new Object[]{"Да", "Отмена"}, "Да") == JOptionPane.YES_OPTION) {

                    try {
                        ppdb = new PlanPDB();
                        for (Object rows : tModel.getDataVector()) {
                            if (((Vector) rows).get(0).toString().equals("true"))
                                ppdb.deletePlan(Integer.valueOf(((Vector) rows).get(2).toString()));
                        }
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(null, "Ошибка удаления! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
                    } finally {
                        ppdb.disConn();
                    }
                    buttSearch.doClick();
                }
            } else {
                JOptionPane.showMessageDialog(null, "Вы не выбрали план производства для удаления!", "Внимание", javax.swing.JOptionPane.WARNING_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ошибка " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jMenuItem6ActionPerformed

    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed
        try {
            boolean flagSelect = false;

            for (Object rows : tModel.getDataVector()) {
                if (((Vector) rows).get(0).toString().equals("true")) {
                    flagSelect = true;
                    break;
                }
            }

            if (flagSelect) {
                Vector compare = new Vector();
                for (Object rows : tModel.getDataVector()) {
                    if (((Vector) rows).get(0).toString().equals("true")) {
                        compare.add(Integer.valueOf(((Vector) rows).get(2).toString()));
                        if (compare.size() > 2) {
                            JOptionPane.showMessageDialog(null, "Для сравнения можно выбрать только два плана производства!", "Внимание", javax.swing.JOptionPane.WARNING_MESSAGE);
                            break;
                        }
                    }
                }

                if (compare.size() == 2) {
                    if (JOptionPane.showOptionDialog(null, "Сравнить выделенные планы производства?", "Сравнить", JOptionPane.YES_NO_OPTION,
                            JOptionPane.QUESTION_MESSAGE, null, new Object[]{"Да", "Отмена"}, "Да") == JOptionPane.YES_OPTION) {

                        try {
                            ppdb = new PlanPDB();

                            new SmallTableForm(controller, PlanProductioForm.this, true,
                                    ppdb.compareSelectedPlanProduction(Integer.valueOf(compare.get(0).toString()), Integer.valueOf(compare.get(1).toString())),
                                    "Различия между планами №" + compare.get(0).toString() + " и №" + compare.get(1).toString());

                        } catch (Exception e) {
                            JOptionPane.showMessageDialog(null, "Ошибка сравнения! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
                        } finally {
                            ppdb.disConn();
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Для сравнения выберите два плана!\n", "Внимание", javax.swing.JOptionPane.WARNING_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(null, "Вы не выбрали планы производства для сравнения!", "Внимание", javax.swing.JOptionPane.WARNING_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ошибка " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jMenuItem3ActionPerformed

    private void jMenuItem7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem7ActionPerformed
        showEditPanel();
    }//GEN-LAST:event_jMenuItem7ActionPerformed

    private void jMenuItem8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem8ActionPerformed
        dataReport = new Vector();
        try {
            if (table.getSelectedRow() != -1) {
                pb = new ProgressBar(PlanProductioForm.this, false, "Сбор данных ...");
                SwingWorker sw = new SwingWorker() {
                    protected Object doInBackground() {
                        try {
                            ppdb = new PlanPDB();

                            int tt = Integer.valueOf(table.getValueAt(table.getSelectedRow(), 2).toString());

                            dataReport = ppdb.getDataAllNormPlan(tt);

                        } catch (Exception e) {
                            dataReport = new Vector();
                            JOptionPane.showMessageDialog(null,
                                    "Сбой!" + e.getMessage(),
                                    "Ошибка",
                                    javax.swing.JOptionPane.ERROR_MESSAGE);

                        } finally {
                            ppdb.disConn();
                        }
                        return null;
                    }

                    protected void done() {
                        pb.dispose();
                    }
                };
                sw.execute();
                pb.setVisible(true);

                PlanOO oo = new PlanOO(table.getValueAt(table.getSelectedRow(), 4).toString(), dataReport);
                oo.createReport("ProductionPlanDocAllNorm.ots");

            } else
                JOptionPane.showMessageDialog(null,
                        "Вы не выбрали план!",
                        "Внимание", javax.swing.JOptionPane.WARNING_MESSAGE);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Ошибка! " + e.getMessage(),
                    "Ошибка",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jMenuItem8ActionPerformed

    private void jMenuItem9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem9ActionPerformed
        buttPowerSearch.doClick();
    }//GEN-LAST:event_jMenuItem9ActionPerformed
    // End of variables declaration//GEN-END:variables

    private void initMenu() {
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem6 = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenuItem7 = new javax.swing.JMenuItem();
        jMenu3 = new javax.swing.JMenu();
        jMenuItem3 = new javax.swing.JMenuItem();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem4 = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        jMenuItem8 = new javax.swing.JMenuItem();
        jMenu4 = new javax.swing.JMenu();
        jMenuItem9 = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jMenu1.setText("Правка");

        jMenuItem6.setText("Удалить");
        jMenuItem6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem6ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem6);

        jMenuItem2.setText("Импорт из DBF");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem2);

        jMenuItem7.setText("Объединить ");
        jMenuItem7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem7ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem7);

        jMenuBar1.add(jMenu1);

        jMenu3.setText("Сервис");

        jMenuItem3.setText("Различия");
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem3);

        jMenuItem1.setText("Обновить спецификации");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem1);

        jMenuItem4.setText("Округление");
        jMenuItem4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem4ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem4);

        jMenuBar1.add(jMenu3);

        jMenu2.setText("Документы");

        jMenuItem8.setText("Трудоемкость (з/ш цех)");
        jMenuItem8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem8ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem8);

        jMenuBar1.add(jMenu2);

        jMenu4.setText("Просмотр");

        jMenuItem9.setText("Расширенный поиск");
        jMenuItem9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem9ActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItem9);

        jMenuBar1.add(jMenu4);

        setJMenuBar(jMenuBar1);

        jMenu1.setVisible(false);
        jMenuItem2.setVisible(false);
        jMenuItem6.setVisible(false);

        menu.put("1", jMenu1);              //Правка
        menu.put("1-1", jMenuItem6);            //Удалить
        menu.put("1-2", jMenuItem2);            //Импорт из DBF
        menu.put("1-3", jMenuItem7);            //Объединить
        menu.put("2", jMenu3);              //Сервис                
        menu.put("2-1", jMenuItem3);            //Различия
        menu.put("2-2", jMenuItem1);            //Обновить спецификации
        menu.put("2-3", jMenuItem4);            //Округление      
        menu.put("3", jMenu2);              //Документы                
        menu.put("3-1", jMenuItem8);            //Трудоемкость (з/ш цех)
        menu.put("4", jMenu4);              //Просмотр                
        menu.put("4-1", jMenuItem9);            //Расширенный поиск
    }

    private void initPropSetting() {
        try {
            UtilPlan.DEPT_SELECT_ITEM = UtilFunctions.readPropFile(UtilPlan.SETTING_DEPT_SELECT_ITEM);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ошибка! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }

        try {
            UtilPlan.ROUNDING_NORM = (UtilFunctions.readPropFile(UtilPlan.SETTING_ROUNDING_NORM) != -1) ? UtilFunctions.readPropFile(UtilPlan.SETTING_ROUNDING_NORM) : 4;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ошибка! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void initData() {
        try {
            ppdb = new PlanPDB();
            Vector data = ppdb.getDept();

            UtilPlan.DEPT_MODEL = new Vector();
            UtilFunctions.fullModel(UtilPlan.DEPT_MODEL, data);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Список подразделений не загружен! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        } finally {
            ppdb.disConn();
        }
    }

    private void initSelectDialog() {
        buttPanel.removeAll();

        buttPanel.add(buttSelect);
        buttPanel.add(new JLabel());
        buttPanel.add(new JLabel());
        buttPanel.add(new JLabel());
        buttPanel.add(buttOpen);

        jMenuBar1.setVisible(false);

        jRadioButton2.setSelected(true);
    }

    private void init() {
        setTitle("План производства");

        setMinimumSize(new Dimension(640, 500));
        setPreferredSize(new Dimension(850, 700));

        controller.menuFormInitialisation(getClass().getName(), menu);
        osnova = new JPanel();
        searchPanel = new JPanel();
        searchButtPanel = new JPanel();
        buttPanel = new JPanel();
        upPanel = new JPanel();
        sStDate = new JDateChooser();
        eStDate = new JDateChooser();
        sInsDate = new JDateChooser();
        eInsDate = new JDateChooser();
        buttSearch = new JButton("Поиск");
        buttPowerSearch = new JButton("Расш. поиск");
        buttTableSearch = new JButton("Поиск");
        buttCopy = new JButton("Копировать");
        buttOpen = new JButton("Открыть");
        buttSelect = new JButton("Выбрать");
        buttEdit = new JButton("Редактировать");
        col = new Vector();
        table = new JTable();
        listNum = new JTextField();
        textTableSearch = new JTextField();
        title = new JLabel("Планы производства");
        checkboxSt = new JCheckBox("Дата");
        checkboxIns = new JCheckBox("Корректировка");
        dept = new JComboBox(UtilPlan.DEPT_MODEL);
        buttonGroup = new ButtonGroup();
        jRadioButton1 = new JRadioButton();
        jRadioButton2 = new JRadioButton();
        jRadioButton3 = new JRadioButton();
        jRadioButton4 = new JRadioButton();
        jRadioButton5 = new JRadioButton();

        osnova.setLayout(new BorderLayout(1, 1));
        osnova.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        searchPanel.setLayout(new ParagraphLayout());
        searchButtPanel.setLayout(new GridLayout(0, 4, 5, 5));
        searchButtPanel.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        buttPanel.setLayout(new GridLayout(0, 5, 5, 5));
        buttPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        upPanel.setLayout(new BorderLayout(1, 1));
        upPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        dept.setPreferredSize(new Dimension(300, 20));
        listNum.setPreferredSize(new Dimension(120, 20));
        sStDate.setPreferredSize(new Dimension(200, 20));
        eStDate.setPreferredSize(new Dimension(200, 20));
        sInsDate.setPreferredSize(new Dimension(200, 20));
        eInsDate.setPreferredSize(new Dimension(200, 20));
        buttSearch.setPreferredSize(new Dimension(50, 20));

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

        jRadioButton1.setText("Все;");
        jRadioButton2.setText("Главный;");
        jRadioButton3.setText("Проект;");
        jRadioButton4.setText("Копия;");
        jRadioButton5.setText("Удалён;");

        jRadioButton1.setActionCommand("");
        jRadioButton2.setActionCommand("0");
        jRadioButton3.setActionCommand("1");
        jRadioButton4.setActionCommand("2");
        jRadioButton5.setActionCommand("-1");

        jRadioButton1.setSelected(true);
        checkboxIns.setSelected(true);

        sStDate.setEnabled(false);
        eStDate.setEnabled(false);

        try {
            dept.setSelectedItem(UtilFunctions.getItemsModel(UtilPlan.DEPT_MODEL, UtilPlan.DEPT_SELECT_ITEM));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ошибка! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }

        title.setHorizontalAlignment(JLabel.CENTER);
        title.setFont(new Font("serif", Font.PLAIN, 24));

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
                    switch (Integer.valueOf(table.getValueAt(row, 8).toString())) {
                        case -1:
                            cell.setBackground(Color.PINK);
                            break;
                        case 0:
                            cell.setBackground(Color.YELLOW);
                            break;
                        case 1:
                            cell.setBackground(Color.GREEN);
                            break;
                        default:
                            cell.setBackground(table.getBackground());
                            break;
                    }
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "Ошибка! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
                }
                cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                return cell;
            }
        };

        dept.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deptActionPerformed(evt);
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

        buttTableSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttTableSearchActionPerformed(evt);
            }
        });

        textTableSearch.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                textTableSearchKeyPressed(evt);
            }
        });

        buttSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttSearchActionPerformed(evt);
            }
        });

        buttPowerSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttPowerSearchActionPerformed(evt);
            }
        });

        buttCopy.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttCopyActionPerformed(evt);
            }
        });

        buttOpen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttOpenActionPerformed(evt);
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

        col.add("");
        col.add("Цех");
        col.add("План №");
        col.add("Дата");
        col.add("Название");
        col.add("Автор");
        col.add("Дата коррект.");
        col.add("Статус");
        col.add("id cтатус");

        buttonGroup.add(jRadioButton1);
        buttonGroup.add(jRadioButton2);
        buttonGroup.add(jRadioButton3);
        buttonGroup.add(jRadioButton4);
        buttonGroup.add(jRadioButton5);

        searchPanel.add(new JLabel("План №:"));
        searchPanel.add(listNum);
        searchPanel.add(new JLabel("    Цех:"));
        searchPanel.add(dept);
        searchPanel.add(new JLabel("    Статус:"), ParagraphLayout.NEW_PARAGRAPH);
        searchPanel.add(jRadioButton1);
        searchPanel.add(jRadioButton2);
        searchPanel.add(jRadioButton3);
        searchPanel.add(jRadioButton4);
        searchPanel.add(jRadioButton5);
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

        searchButtPanel.add(buttSearch);

        upPanel.add(title, BorderLayout.NORTH);
        upPanel.add(searchPanel, BorderLayout.CENTER);
        upPanel.add(searchButtPanel, BorderLayout.SOUTH);

        buttPanel.add(textTableSearch);
        buttPanel.add(buttTableSearch);
        buttPanel.add(buttCopy);
        buttPanel.add(buttEdit);
        buttPanel.add(buttOpen);

        osnova.add(upPanel, BorderLayout.NORTH);
        osnova.add(new JScrollPane(table), BorderLayout.CENTER);
        osnova.add(buttPanel, BorderLayout.SOUTH);

        getContentPane().add(osnova);
        pack();
    }

    private void deptActionPerformed(ActionEvent evt) {
        try {
            UtilPlan.DEPT_SELECT_ITEM = ((Item) dept.getSelectedItem()).getId();
            UtilFunctions.setSettingPropFile(String.valueOf(((Item) dept.getSelectedItem()).getId()), UtilPlan.SETTING_DEPT_SELECT_ITEM);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ошибка! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
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

    private void buttTableSearchActionPerformed(ActionEvent evt) {
        String text = textTableSearch.getText().trim();
        if (text.length() == 0) {
            sorter.setRowFilter(null);
        } else {
            try {
                sorter.setRowFilter(RowFilter.regexFilter("(?iu)" + text));
                if (table.getRowCount() == 0) {
                    JOptionPane.showMessageDialog(null, "По заданному запросу ничего не найдено!", "Внимание", javax.swing.JOptionPane.WARNING_MESSAGE);
                    sorter.setRowFilter(null);
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Ошибка " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void textTableSearchKeyPressed(java.awt.event.KeyEvent evt) {
        if (evt.getKeyCode() == evt.VK_ENTER) buttTableSearch.doClick();
    }

    private void buttCopyActionPerformed(ActionEvent evt) {
        getDataPlanProduction(UtilPlan.PLAN_COPY);
    }

    private void buttOpenActionPerformed(ActionEvent evt) {
        getDataPlanProduction(UtilPlan.PLAN_OPEN);
    }

    private void buttEditActionPerformed(ActionEvent evt) {
        getDataPlanProduction(UtilPlan.PLAN_EDIT);
    }

    private void buttSearchActionPerformed(ActionEvent evt) {
        createPlanTable(getPlans());
    }

    private void buttPowerSearchActionPerformed(ActionEvent evt) {
        try {
            JPanel panel = new JPanel();
            final JTextField textFas = new JTextField("");
            final JTextField textNar = new JTextField("");
            final JTextField textSar = new JTextField("");

            panel.setLayout(new ParagraphLayout());

            textFas.setPreferredSize(new Dimension(200, 20));
            textNar.setPreferredSize(new Dimension(200, 20));
            textSar.setPreferredSize(new Dimension(200, 20));

            textFas.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
            textNar.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
            textSar.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

            panel.add(new JLabel("Шифр:"), ParagraphLayout.NEW_PARAGRAPH);
            panel.add(new JScrollPane(textSar));
            panel.add(new JLabel("Артикул:"), ParagraphLayout.NEW_PARAGRAPH);
            panel.add(new JScrollPane(textNar));
            panel.add(new JLabel("Модель:"), ParagraphLayout.NEW_PARAGRAPH);
            panel.add(new JScrollPane(textFas));

            if (JOptionPane.showOptionDialog(null,
                    panel,
                    "Расширенный поиск",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    new Object[]{"Найти", "Отмена"}, "Найти") == JOptionPane.YES_OPTION) {


                new SmallTableForm(controller,
                        PlanProductioForm.this,
                        true,
                        getPowerSearch(
                                textSar.getText().trim(),
                                textNar.getText().trim(),
                                textFas.getText().trim()));
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Ошибка! " + e.getMessage(),
                    "Ошибка",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buttSelectActionPerformed(ActionEvent evt) {
        try {
            if (table.getSelectedRow() != -1) {
                UtilPlan.PLAN_SELECT_NUM = Integer.valueOf(table.getValueAt(table.getSelectedRow(), 2).toString());
                UtilPlan.PLAN_SELECT_NAME = table.getValueAt(table.getSelectedRow(), 4).toString();
                UtilPlan.ACTION_BUTT_PLAN_SELECT = true;

                dispose();

            } else
                JOptionPane.showMessageDialog(
                        null,
                        "Вы ничего не выбрали!",
                        "Внимание",
                        javax.swing.JOptionPane.WARNING_MESSAGE);

        } catch (Exception e) {
            cleanConstants();
            JOptionPane.showMessageDialog(
                    null,
                    "Выбрать план производства не удалось!\n " + e.getMessage(),
                    "Ошибка",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void createPlanTable(final Vector data) {
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
        table.getColumnModel().getColumn(1).setPreferredWidth(40);
        table.getColumnModel().getColumn(2).setPreferredWidth(5);
        table.getColumnModel().getColumn(3).setPreferredWidth(10);
        table.getColumnModel().getColumn(4).setPreferredWidth(90);
        table.getColumnModel().getColumn(5).setPreferredWidth(60);
        table.getColumnModel().getColumn(6).setPreferredWidth(20);
        table.getColumnModel().getColumn(7).setPreferredWidth(10);
        table.getColumnModel().getColumn(8).setMinWidth(0);
        table.getColumnModel().getColumn(8).setMaxWidth(0);

        sorter = new TableRowSorter<TableModel>(tModel);
        table.setRowSorter(sorter);
        table.getColumnModel().getColumn(0).setHeaderRenderer(new CheckBoxHeader(table.getTableHeader(), 0, ""));

        table.getColumnModel().getColumn(7).setCellRenderer(renderer);
    }

    private Vector getPlans() {
        row = new Vector();
        try {
            ppdb = new PlanPDB();
            pb = new ProgressBar(this, false, "Поиск планов производства...");
            final SwingWorker sw = new SwingWorker() {
                @Override
                protected Object doInBackground() throws Exception {
                    row = ppdb.getAllPlans(
                            ((Item) dept.getSelectedItem()).getId(),
                            checkboxSt.isSelected(),
                            UtilFunctions.convertDateStrToLong(new SimpleDateFormat("dd.MM.yyyy").format(sStDate.getDate())),
                            UtilFunctions.convertDateStrToLong(new SimpleDateFormat("dd.MM.yyyy").format(eStDate.getDate())),
                            checkboxIns.isSelected(),
                            UtilFunctions.convertDateStrToLong(new SimpleDateFormat("dd.MM.yyyy").format(sInsDate.getDate())),
                            UtilFunctions.convertDateStrToLong(new SimpleDateFormat("dd.MM.yyyy").format(eInsDate.getDate())),
                            listNum.getText().trim(),
                            buttonGroup.getSelection().getActionCommand());
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
            row = new Vector();
            JOptionPane.showMessageDialog(null, "Ошибка поиска планов производства! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        } finally {
            ppdb.disConn();
        }
        return row;
    }

    private Vector getPowerSearch(final String sar, final String nar, final String fas) {
        dataSearch = new Vector();

        try {

            ppdb = new PlanPDB();
            pb = new ProgressBar(this, false, "Сбор данных...");

            final SwingWorker sw = new SwingWorker() {
                @Override
                protected Object doInBackground() throws Exception {
                    dataSearch = ppdb.getPowerSearch(sar, nar, fas);
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
            dataSearch = new Vector();
            JOptionPane.showMessageDialog(null,
                    "Ошибка поиска! " + e.getMessage(),
                    "Ошибка",
                    javax.swing.JOptionPane.ERROR_MESSAGE);

        } finally {
            ppdb.disConn();
        }

        return dataSearch;
    }

    private Vector getPlans(final String nameType) {
        row = new Vector();
        try {
            ppdb = new PlanPDB();
            pb = new ProgressBar(this, false, "Поиск планов производства...");
            final SwingWorker sw = new SwingWorker() {
                @Override
                protected Object doInBackground() throws Exception {
                    row = ppdb.getAllPlans(
                            nameType,
                            checkboxSt.isSelected(),
                            UtilFunctions.convertDateStrToLong(new SimpleDateFormat("dd.MM.yyyy").format(sStDate.getDate())),
                            UtilFunctions.convertDateStrToLong(new SimpleDateFormat("dd.MM.yyyy").format(eStDate.getDate())),
                            checkboxIns.isSelected(),
                            UtilFunctions.convertDateStrToLong(new SimpleDateFormat("dd.MM.yyyy").format(sInsDate.getDate())),
                            UtilFunctions.convertDateStrToLong(new SimpleDateFormat("dd.MM.yyyy").format(eInsDate.getDate())),
                            listNum.getText().trim(),
                            buttonGroup.getSelection().getActionCommand());
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
            row = new Vector();
            JOptionPane.showMessageDialog(null,
                    "Ошибка поиска планов производства! " + e.getMessage(),
                    "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        } finally {
            ppdb.disConn();
        }
        return row;
    }

    private void getDataPlanProduction(String type) {
        if (table.getSelectedRow() != -1) {
            dataPlan = new Vector();
            dataItem = new Vector();
            dataItemDetal = new Vector();
            dataItemConv = new Vector();
            dataItemDekad = new Vector();

            if (type.equals(UtilPlan.PLAN_OPEN)) {
                pb = new ProgressBar(PlanProductioForm.this, false, "Сбор данных ...");
                SwingWorker sw = new SwingWorker() {
                    protected Object doInBackground() {
                        try {
                            ppdb = new PlanPDB();

                            dataPlan = ppdb.getDataPlan(Integer.valueOf(table.getValueAt(table.getSelectedRow(), 2).toString()));
                            dataItem = ppdb.getDataItemPlan(Integer.valueOf(table.getValueAt(table.getSelectedRow(), 2).toString()));
                            dataItemDetal = ppdb.getDataItemPlanDetal(Integer.valueOf(table.getValueAt(table.getSelectedRow(), 2).toString()));
                            dataItemConv = ppdb.getDataItemPlanConv(Integer.valueOf(table.getValueAt(table.getSelectedRow(), 2).toString()));
                            dataItemDekad = ppdb.getDataItemPlanDekad(Integer.valueOf(table.getValueAt(table.getSelectedRow(), 2).toString()));

                        } catch (Exception e) {
                            dataPlan = new Vector();
                            dataItem = new Vector();
                            dataItemDetal = new Vector();
                            dataItemConv = new Vector();
                            dataItemDekad = new Vector();
                            JOptionPane.showMessageDialog(null, "Данные плана производства не загружены! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
                        } finally {
                            ppdb.disConn();
                        }
                        return null;
                    }

                    protected void done() {
                        pb.dispose();
                    }
                };
                sw.execute();
                pb.setVisible(true);

                new PlanDetalForm(controller, true, dataPlan, dataItem, dataItemDetal, dataItemConv, dataItemDekad,
                        Integer.valueOf(table.getValueAt(table.getSelectedRow(), 2).toString()));

            } else if (type.equals(UtilPlan.PLAN_EDIT) || type.equals(UtilPlan.PLAN_COPY)) {
                try {
                    ppdb = new PlanPDB();
                    dataPlan = ppdb.getDataPlan(Integer.valueOf(table.getValueAt(table.getSelectedRow(), 2).toString()));

                } catch (Exception e) {
                    dataPlan = new Vector();
                    JOptionPane.showMessageDialog(null, "Данные плана производства не загружены! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
                } finally {
                    ppdb.disConn();
                }

                new PlanDetalForm(controller, true, dataPlan,
                        Integer.valueOf(table.getValueAt(table.getSelectedRow(), 2).toString()), type);
            }

        } else
            JOptionPane.showMessageDialog(null, "Вы ничего не выбрали!", "Внимание", javax.swing.JOptionPane.WARNING_MESSAGE);
    }

    public Map getMenuMap() {
        return menu;
    }

    public void setFormVisible() {
        this.setVisible(false);
    }

    public void disposeForm() {
        dispose();
    }

    private void cleanConstants() {
        UtilPlan.ACTION_BUTT_PLAN_SELECT = false;
        UtilPlan.PLAN_SELECT_NUM = -1;
        UtilPlan.PLAN_SELECT_NAME = "";
    }

    private void showEditPanel() {
        try {

            final JTextField val1Edit = new JTextField();
            final JTextField val2Edit = new JTextField();

            JPanel editPanel = new JPanel();
            editPanel.setLayout(new GridLayout(0, 1, 5, 5));
            editPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
            editPanel.add(new JLabel("Содержимое плана №:"));
            editPanel.add(val1Edit);
            editPanel.add(new JLabel("Перенести в план №:"));
            editPanel.add(val2Edit);


            if (JOptionPane.showOptionDialog(
                    null,
                    editPanel,
                    "Перенести содержимое?",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.INFORMATION_MESSAGE,
                    null,
                    new Object[]{"Да", "Отмена"},
                    "Да") == JOptionPane.YES_OPTION) {

                pb = new ProgressBar(PlanProductioForm.this,
                        false,
                        "Выполняется объединение...");
                SwingWorker sw = new SwingWorker() {
                    protected Object doInBackground() {
                        try {
                            ppdb = new PlanPDB();
                            if (ppdb.mixPlan(
                                    Integer.valueOf(val1Edit.getText().trim()),
                                    Integer.valueOf(val2Edit.getText().trim()),
                                    Integer.valueOf(user.getIdEmployee()))) {

                                JOptionPane.showMessageDialog(null,
                                        "Записи успешно объединены! ",
                                        "Завершено",
                                        javax.swing.JOptionPane.INFORMATION_MESSAGE);
                            }
                        } catch (Exception e) {
                            JOptionPane.showMessageDialog(null, "Сбой обновления спецификаций плана!" + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
                        } finally {
                            ppdb.disConn();
                        }
                        return null;
                    }

                    protected void done() {
                        pb.dispose();
                    }
                };
                sw.execute();
                pb.setVisible(true);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Ошибка. " + e.getMessage(),
                    "Ошибка",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }
}
