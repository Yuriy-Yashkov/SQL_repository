package dept.production.planning;

import by.gomel.freedev.ucframework.ucswing.uicontrols.UCDatePicker;
import by.march8.ecs.MainController;
import by.march8.ecs.framework.common.model.CurrencySet;
import com.jhlabs.awt.ParagraphLayout;
import com.toedter.calendar.JDateChooser;
import common.CheckBoxHeader;
import common.FormMenu;
import common.ProgressBar;
import common.User;
import common.UtilFunctions;
import dept.sklad.SkladPDB;
import dept.sprav.valuta.ValutaPDB;
import workDB.PDB;

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
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TreeMap;
import java.util.Vector;

/**
 * @author lidashka
 */
@SuppressWarnings("all")
public class ProjectPlanForm extends javax.swing.JDialog implements FormMenu {
    private static TreeMap menu = new TreeMap();
    ProgressBar pb;
    PlanPDB ppdb;
    PlanDB pdb;
    PDB db;
    ValutaPDB vpdb;
    DefaultTableModel tModel;
    Vector col;
    Vector row;
    TableRowSorter sorter;
    DefaultTableCellRenderer renderer;
    int minSelectedRow = -1;
    int maxSelectedRow = -1;
    boolean tableModelListenerIsChanging = false;
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
    private JButton buttAdd;
    private JButton buttCopy;
    private JButton buttOpen;
    private JButton buttAnalysis;
    private JButton buttSelect;
    private JButton buttEdit;
    private JTable table;
    private JTextField listNum;
    private JTextField textTableSearch;
    private JLabel title;
    private JCheckBox checkboxSt;
    private JCheckBox checkboxIns;
    private ButtonGroup buttonGroup;
    private JRadioButton jRadioButton1;
    private JRadioButton jRadioButton2;
    private JRadioButton jRadioButton3;
    private JRadioButton jRadioButton4;
    private Vector dataProj;
    private Vector dataItem;
    private Vector dataItemDetal;
    private Vector dataItemVnorm;
    private Vector dataItemDekad;
    private Vector dataAnalysis;
    private Vector dataItemWork;

    private double kursUSD;
    private double kursEUR;
    private double kursRUB;
    private JPanel panelReport;
    private ButtonGroup buttonGroupReport;
    private JTextField textReport;
    private MainController controller;

    private UCDatePicker datePicker;
    private JLabel lblDatePicker;

    private User user = User.getInstance();
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenu jMenu4;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem10;
    private javax.swing.JMenuItem jMenuItem11;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JMenuItem jMenuItem5;
    private javax.swing.JMenuItem jMenuItem6;
    private javax.swing.JMenuItem jMenuItem7;
    private javax.swing.JMenuItem jMenuItem8;
    private javax.swing.JMenuItem jMenuItem9;

    public ProjectPlanForm() {
        initMenu();
    }

    public ProjectPlanForm(MainController controller, boolean modal) {
        super(controller.getMainForm(), modal);
        this.controller = controller;

        if (user.getFio() != null) {

            cleanConstants();
            initMenu();
            initPropSetting();
            initData();
            init();
            initSelectDialog();

            java.util.Date d = (Calendar.getInstance()).getTime();
            d.setDate(1);

            sStDate.setDate(d);
            eStDate.setDate((Calendar.getInstance()).getTime());

            sInsDate.setDate(d);
            eInsDate.setDate((Calendar.getInstance()).getTime());

            createProjectPlanTable(getPlans());

            setLocationRelativeTo(controller.getMainForm());
            setVisible(true);
        } else
            JOptionPane.showMessageDialog(null,
                    "Такой логин и пароль не присвоен ни одному пользователю. \n "
                            + "Обратитесь к администратору! ",
                    "Вход",
                    javax.swing.JOptionPane.WARNING_MESSAGE);

    }
    public ProjectPlanForm(MainController controller, boolean modal, String title, String t) {
        super(controller.getMainForm(), modal);
        this.controller = controller;
        cleanConstants();
        initMenu();
        postInit();
        initPropSetting();
        initData();
        init();
        initSelectDialog();

        jRadioButton2.setSelected(true);

        java.util.Date d = (Calendar.getInstance()).getTime();
        d.setDate(1);
        d.setMonth(d.getMonth() - 3);

        sInsDate.setDate(d);
        eInsDate.setDate((Calendar.getInstance()).getTime());

        sStDate.setDate(d);
        eStDate.setDate((Calendar.getInstance()).getTime());

        createProjectPlanTable(getPlans());

        setLocationRelativeTo(controller.getMainForm());
        setVisible(true);
    }
    public ProjectPlanForm(MainController controller, boolean modal, boolean isDialog) {
        super(controller.getMainForm(), modal);
        this.controller = controller;
        if(user.getFio() != null){

        cleanConstants();
        initMenu();
        postInit();
        initPropSetting();
        initData();
        init();
        if (isDialog) {
            initSelectDialog();
        }

        java.util.Date d = (Calendar.getInstance()).getTime();
        d.setDate(1);

        sStDate.setDate(d);
        eStDate.setDate((Calendar.getInstance()).getTime());

        sInsDate.setDate(d);
        eInsDate.setDate((Calendar.getInstance()).getTime());

        createProjectPlanTable(getPlans());

        setLocationRelativeTo(controller.getMainForm());
        setVisible(true);

        } else
            JOptionPane.showMessageDialog(null,
                      "Такой логин и пароль не присвоен ни одному пользователю. \n "
                    + "Обратитесь к администратору! ",
                      "Вход",
                      javax.swing.JOptionPane.WARNING_MESSAGE);
    }
    public ProjectPlanForm(MainController controller, boolean modal, String tmp) {
        super(controller.getMainForm(), modal);
        this.controller = controller;
        cleanConstants();
        initMenu();
        postInit();
        initPropSetting();
        initData();
        init();
        initAnalysisDialog();

        java.util.Date d = (Calendar.getInstance()).getTime();
        d.setDate(1);
        d.setMonth(d.getMonth() - 3);

        sStDate.setDate(d);
        eStDate.setDate((Calendar.getInstance()).getTime());

        sInsDate.setDate(d);
        eInsDate.setDate((Calendar.getInstance()).getTime());

        createProjectPlanTable(getPlans());

        setLocationRelativeTo(controller.getMainForm());
        setModal(false);
        setVisible(true);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenuItem4 = new javax.swing.JMenuItem();
        jMenu3 = new javax.swing.JMenu();
        jMenuItem5 = new javax.swing.JMenuItem();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem3 = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        jMenuItem6 = new javax.swing.JMenuItem();
        jMenuItem7 = new javax.swing.JMenuItem();
        jMenuItem8 = new javax.swing.JMenuItem();
        jMenu4 = new javax.swing.JMenu();
        jMenuItem11 = new javax.swing.JMenuItem();
        jMenuItem9 = new javax.swing.JMenuItem();
        jMenuItem10 = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jMenu1.setText("Правка");

        jMenuItem2.setText("Удалить");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem2);

        jMenuItem4.setText("Справочники");
        jMenuItem4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem4ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem4);

        jMenuBar1.add(jMenu1);

        jMenu3.setText("Сервис");

        jMenuItem5.setText("Обновить артикула");
        jMenuItem5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem5ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem5);

        jMenuItem1.setText("Обновить спецификации");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem1);

        jMenuItem3.setText("Округление");
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem3);

        jMenuBar1.add(jMenu3);

        jMenu2.setText("Отдел цен");

        jMenuItem6.setText("Артикула в проекте");
        jMenuItem6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem6ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem6);

        jMenuItem7.setText("Модели ");
        jMenuItem7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem7ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem7);

        jMenuItem8.setText("Ассортимент с ценами");
        jMenuItem8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem8ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem8);

        jMenuBar1.add(jMenu2);

        jMenu4.setText("Документы");

        jMenuItem11.setText("Ассортимент все цены");
        jMenuItem11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem11ActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItem11);

        jMenuItem9.setText("Ассортимент для покупателя РБ");
        jMenuItem9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem9ActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItem9);

        jMenuItem10.setText("Ассортимент для покупателя РФ");
        jMenuItem10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem10ActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItem10);

        jMenuBar1.add(jMenu4);

        this.setJMenuBar(jMenuBar1);
        jMenuBar1.setVisible(true);

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
        this.repaint();
        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        try {
            boolean flagSelect = false;
            for (Object rows : tModel.getDataVector()) {
                if (((Vector) rows).get(0).toString().equals("true")) {
                    flagSelect = true;
                    break;
                }
            }

            if (flagSelect) {
                if (JOptionPane.showOptionDialog(null, "Удалить выделенные проекты?", "Удаление", JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE, null, new Object[]{"Да", "Отмена"}, "Да") == JOptionPane.YES_OPTION) {

                    try {
                        ppdb = new PlanPDB();
                        for (Object rows : tModel.getDataVector()) {
                            if (((Vector) rows).get(0).toString().equals("true"))
                                ppdb.deletePlan(Integer.valueOf(((Vector) rows).get(1).toString()));
                        }
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(null, "Ошибка удаления! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
                    } finally {
                        ppdb.disConn();
                    }
                    buttSearch.doClick();
                }
            } else {
                JOptionPane.showMessageDialog(null, "Вы не выбрали проекты для удаления!", "Внимание", javax.swing.JOptionPane.WARNING_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ошибка " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed
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
    }//GEN-LAST:event_jMenuItem3ActionPerformed

    private void jMenuItem4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem4ActionPerformed
        try {
            JPanel panel = new JPanel();
            final JTextField textModels16 = new JTextField(UtilPlan.DBF_MODELS16);
            final JTextField textRasklad = new JTextField(UtilPlan.DBF_RASHOD);
            final JTextField textRaskladDetal = new JTextField(UtilPlan.DBF_RASHOD_DETAL);

            JButton butModel16 = new JButton("Изменить");
            JButton butRasklad = new JButton("Изменить");
            JButton butRaskladDetal = new JButton("Изменить");

            panel.setLayout(new ParagraphLayout());

            textModels16.setPreferredSize(new Dimension(200, 20));
            textRasklad.setPreferredSize(new Dimension(200, 20));
            textRaskladDetal.setPreferredSize(new Dimension(200, 20));

            textModels16.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
            textRasklad.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
            textRaskladDetal.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

            butModel16.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    File rez = buttSelectSpravActionPerformed(evt, UtilPlan.DBF_MODELS16);
                    if (rez != null) {
                        if (rez.exists()) {
                            textModels16.setText(rez.toString());
                            UtilPlan.DBF_MODELS16 = rez.getPath();
                        } else {
                            textModels16.setText("");
                            UtilPlan.DBF_MODELS16 = "";
                            JOptionPane.showMessageDialog(null, "Файл не существует!", "Ошибка!", javax.swing.JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
            });

            butRasklad.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    File rez = buttSelectSpravActionPerformed(evt, UtilPlan.DBF_RASHOD);
                    if (rez != null) {
                        if (rez.exists()) {
                            textRasklad.setText(rez.toString());
                            UtilPlan.DBF_RASHOD = rez.getPath();
                        } else {
                            textRasklad.setText("");
                            UtilPlan.DBF_RASHOD = "";
                            JOptionPane.showMessageDialog(null, "Файл не существует!", "Ошибка!", javax.swing.JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
            });

            butRaskladDetal.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    File rez = buttSelectSpravActionPerformed(evt, UtilPlan.DBF_RASHOD_DETAL);
                    if (rez != null) {
                        if (rez.exists()) {
                            textRaskladDetal.setText(rez.toString());
                            UtilPlan.DBF_RASHOD_DETAL = rez.getPath();
                        } else {
                            textRaskladDetal.setText("");
                            UtilPlan.DBF_RASHOD_DETAL = "";
                            JOptionPane.showMessageDialog(null, "Файл не существует!", "Ошибка!", javax.swing.JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
            });

            panel.add(new JLabel("Модели16:"), ParagraphLayout.NEW_PARAGRAPH);
            panel.add(new JScrollPane(textModels16));
            panel.add(butModel16);
            panel.add(new JLabel("NRM_BMRD:"), ParagraphLayout.NEW_PARAGRAPH);
            panel.add(new JScrollPane(textRasklad));
            panel.add(butRasklad);
            panel.add(new JLabel("NRM_BLRD:"), ParagraphLayout.NEW_PARAGRAPH);
            panel.add(new JScrollPane(textRaskladDetal));
            panel.add(butRaskladDetal);

            if (JOptionPane.showOptionDialog(null, panel, "Расположение справочников", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, new Object[]{"Сохранить", "Отмена"}, "Сохранить") == JOptionPane.YES_OPTION) {
                try {
                    UtilFunctions.setSettingPropFile(UtilPlan.DBF_MODELS16, UtilPlan.SETTING_DBF_MODELS16);
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "Ошибка! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
                }
                try {
                    UtilFunctions.setSettingPropFile(UtilPlan.DBF_RASHOD, UtilPlan.SETTING_DBF_RASKLAD);
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "Ошибка! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
                }
                try {
                    UtilFunctions.setSettingPropFile(UtilPlan.DBF_RASHOD_DETAL, UtilPlan.SETTING_DBF_RASKLAD_DETAL);
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "Ошибка! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
                }
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ошибка! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jMenuItem4ActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        try {
            if (table.getSelectedRow() != -1) {
                if (JOptionPane.showOptionDialog(null, "Все спецификации в проекте № " + table.getValueAt(table.getSelectedRow(), 1).toString() + " \nбудут обновлены! Обновление займёт некоторое время!\nОбновить?", "Внимание!", JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE, null, new Object[]{"Да", "Отмена"}, "Да") == JOptionPane.YES_OPTION) {
                    pb = new ProgressBar(ProjectPlanForm.this, false, "Обновление спецификаций проекта №" + table.getValueAt(table.getSelectedRow(), 1).toString() + " ...");
                    SwingWorker sw = new SwingWorker() {
                        protected Object doInBackground() {
                            try {
                                ppdb = new PlanPDB();

                                if (ppdb.updateSpecInPlanProduction(Integer.valueOf(table.getValueAt(table.getSelectedRow(), 1).toString()))) {
                                    JOptionPane.showMessageDialog(null, "Обновление спецификаций проекта завершено успешно!", "Внимание", javax.swing.JOptionPane.INFORMATION_MESSAGE);
                                }
                            } catch (Exception e) {
                                JOptionPane.showMessageDialog(null, "Сбой обновления спецификаций проекта!" + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
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
                JOptionPane.showMessageDialog(null, "Вы не выбрали проект производства для обновления спецификаций!", "Внимание", javax.swing.JOptionPane.WARNING_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ошибка! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jMenuItem5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem5ActionPerformed
        try {
            if (table.getSelectedRow() != -1) {
                final JRadioButton jRadioButtonAll = new JRadioButton();
                final JRadioButton jRadioButtonNull = new JRadioButton();

                jRadioButtonAll.setFont(new java.awt.Font("Dialog", 0, 13));
                jRadioButtonNull.setFont(new java.awt.Font("Dialog", 0, 13));

                jRadioButtonAll.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
                jRadioButtonNull.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

                jRadioButtonAll.setText("Все модели;");
                jRadioButtonNull.setText("Без артикулов;");

                jRadioButtonAll.setActionCommand("1");
                jRadioButtonNull.setActionCommand("-1");

                final ButtonGroup buttonGroupUpdate = new ButtonGroup();
                buttonGroupUpdate.add(jRadioButtonAll);
                buttonGroupUpdate.add(jRadioButtonNull);

                jRadioButtonAll.setSelected(true);

                JPanel panelUpdateNar = new JPanel();
                panelUpdateNar.setLayout(new ParagraphLayout());
                panelUpdateNar.add(new JLabel(), ParagraphLayout.NEW_PARAGRAPH);
                panelUpdateNar.add(new JLabel("Артикула в проекте № " + table.getValueAt(table.getSelectedRow(), 1).toString() + " будут обновлены! "));
                panelUpdateNar.add(new JLabel(), ParagraphLayout.NEW_PARAGRAPH);
                panelUpdateNar.add(new JLabel("Обновление займёт некоторое время! Обновить?"));
                panelUpdateNar.add(new JLabel(), ParagraphLayout.NEW_PARAGRAPH);
                panelUpdateNar.add(jRadioButtonAll);
                //    panelUpdateNar.add(jRadioButtonNull);

                if (JOptionPane.showOptionDialog(
                        null,
                        panelUpdateNar,
                        "Внимание!",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE,
                        null, new Object[]{"Да", "Отмена"},
                        "Да") == JOptionPane.YES_OPTION) {

                    pb = new ProgressBar(
                            ProjectPlanForm.this,
                            false,
                            "Обработка ...");

                    SwingWorker sw = new SwingWorker() {
                        protected Object doInBackground() {

                            Vector dataFas = new Vector();
                            Vector dataNar = new Vector();

                            try {
                                pb.setMessage("Поиск артикулов...");

                                ppdb = new PlanPDB();

                                dataFas = ppdb.getFasInPlan(
                                        Integer.valueOf(table.getValueAt(table.getSelectedRow(), 1).toString()),
                                        Integer.valueOf(buttonGroupUpdate.getSelection().getActionCommand()));

                            } catch (Exception e) {
                                dataFas = new Vector();
                                JOptionPane.showMessageDialog(
                                        null,
                                        "Сбой поиска артикулов проекта!" + e.getMessage(),
                                        "Ошибка",
                                        javax.swing.JOptionPane.ERROR_MESSAGE);
                            } finally {
                                ppdb.disConn();
                            }

                            if (!dataFas.isEmpty()) {
                                try {
                                    pdb = new PlanDB();

                                    dataNar = pdb.searchNar(
                                            dataFas,
                                            UtilPlan.YEARS);
                                } catch (Exception e) {
                                    JOptionPane.showMessageDialog(
                                            null,
                                            "Сбой поиска артикулов проекта!" + e.getMessage(),
                                            "Ошибка",
                                            javax.swing.JOptionPane.ERROR_MESSAGE);
                                } finally {
                                    pdb.disConn();
                                }

                                try {
                                    pb.setMessage("Обновление артикулов в проекте плана...");

                                    ppdb = new PlanPDB();

                                    if (ppdb.updateNarInPlanProduction(
                                            Integer.valueOf(table.getValueAt(table.getSelectedRow(), 1).toString()),
                                            Integer.valueOf(buttonGroupUpdate.getSelection().getActionCommand()),
                                            dataNar)) {

                                        JOptionPane.showMessageDialog(
                                                null,
                                                "Обновление артикулов проекта завершено успешно!",
                                                "Внимание",
                                                javax.swing.JOptionPane.INFORMATION_MESSAGE);
                                    }


                                } catch (Exception e) {
                                    JOptionPane.showMessageDialog(
                                            null,
                                            "Сбой обновления артикулов проекта!" + e.getMessage(),
                                            "Ошибка",
                                            javax.swing.JOptionPane.ERROR_MESSAGE);
                                } finally {
                                    ppdb.disConn();
                                }

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
                JOptionPane.showMessageDialog(null,
                        "Вы не выбрали проект производства для обновления артикулов!",
                        "Внимание",
                        javax.swing.JOptionPane.WARNING_MESSAGE);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Ошибка! " + e.getMessage(),
                    "Ошибка",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jMenuItem5ActionPerformed

    private void jMenuItem6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem6ActionPerformed
        try {
            if (table.getSelectedRow() != -1) {

                new ProjectUpdateForm(controller, true,
                        Integer.valueOf(table.getValueAt(table.getSelectedRow(), 1).toString()));

            } else
                JOptionPane.showMessageDialog(null,
                        "Вы не выбрали проект плана производства для обновления артикулов!",
                        "Внимание",
                        javax.swing.JOptionPane.WARNING_MESSAGE);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Ошибка! " + e.getMessage(),
                    "Ошибка",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jMenuItem6ActionPerformed

    private void jMenuItem7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem7ActionPerformed
        try {
            new ProjectWorkModelForm(this, true);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Ошибка! " + e.getMessage(),
                    "Ошибка",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jMenuItem7ActionPerformed

    private void jMenuItem8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem8ActionPerformed
        try {
            if (table.getSelectedRow() != -1) {
                datePicker.setVisible(true);
                lblDatePicker.setVisible(true);
                if (JOptionPane.showOptionDialog(
                        null,
                        panelReport,
                        "Документ",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE,
                        null, new Object[]{"Продолжить", "Отмена"},
                        "Да") == JOptionPane.YES_OPTION) {

                    SkladPDB pdb = new SkladPDB();
                    CurrencySet[] currencySet = pdb.getCurrencyRateAsArray(datePicker.getDate(), false);
                    pdb.disConn();

                    PlanOO oo = new PlanOO(
                            table.getValueAt(table.getSelectedRow(), 2).toString(),
                            getDataItemReport(
                                    false,
                                    Boolean.valueOf(buttonGroupReport.getSelection().getActionCommand()),
                                    textReport.getText().trim(), currencySet), currencySet);

                    oo.createReport("ProjectPlanCena.ots");
                }
            } else
                JOptionPane.showMessageDialog(null,
                        "Вы не выбрали проект плана производства!",
                        "Внимание",
                        javax.swing.JOptionPane.WARNING_MESSAGE);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Ошибка! " + e.getMessage(),
                    "Ошибка",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jMenuItem8ActionPerformed

    private void jMenuItem9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem9ActionPerformed
        try {
            if (table.getSelectedRow() != -1) {
                datePicker.setVisible(false);
                lblDatePicker.setVisible(false);
                if (JOptionPane.showOptionDialog(
                        null,
                        panelReport,
                        "Документ",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE,
                        null, new Object[]{"Продолжить", "Отмена"},
                        "Да") == JOptionPane.YES_OPTION) {

                    PlanOO oo = new PlanOO(
                            table.getValueAt(table.getSelectedRow(), 2).toString(),
                            getDataItemReport(
                                    false,
                                    Boolean.valueOf(buttonGroupReport.getSelection().getActionCommand()),
                                    textReport.getText().trim(), null));

                    oo.createReport("ProjectPlanCenaRBImage.ots");
                }
            } else
                JOptionPane.showMessageDialog(null,
                        "Вы не выбрали проект плана производства!",
                        "Внимание",
                        javax.swing.JOptionPane.WARNING_MESSAGE);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Ошибка! " + e.getMessage(),
                    "Ошибка",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jMenuItem9ActionPerformed

    private void jMenuItem10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem10ActionPerformed
        try {
            if (table.getSelectedRow() != -1) {
                datePicker.setVisible(true);
                lblDatePicker.setVisible(true);
                if (JOptionPane.showOptionDialog(
                        null,
                        panelReport,
                        "Документ",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE,
                        null, new Object[]{"Продолжить", "Отмена"},
                        "Да") == JOptionPane.YES_OPTION) {

                    SkladPDB pdb = new SkladPDB();
                    CurrencySet[] currencySet = pdb.getCurrencyRateAsArray(datePicker.getDate(), false);
                    pdb.disConn();

                    PlanOO oo = new PlanOO(
                            table.getValueAt(table.getSelectedRow(), 2).toString(),
                            getDataItemReport(
                                    true,
                                    Boolean.valueOf(buttonGroupReport.getSelection().getActionCommand()),
                                    textReport.getText().trim(), currencySet), currencySet);

                    oo.createReport("ProjectPlanCenaRFImage.ots");
                }

            } else
                JOptionPane.showMessageDialog(null,
                        "Вы не выбрали проект плана производства!",
                        "Внимание",
                        javax.swing.JOptionPane.WARNING_MESSAGE);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Ошибка! " + e.getMessage(),
                    "Ошибка",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jMenuItem10ActionPerformed

    private void jMenuItem11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem11ActionPerformed
        try {
            if (table.getSelectedRow() != -1) {
                datePicker.setVisible(true);
                lblDatePicker.setVisible(true);
                if (JOptionPane.showOptionDialog(
                        null,
                        panelReport,
                        "Документ",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE,
                        null, new Object[]{"Продолжить", "Отмена"},
                        "Да") == JOptionPane.YES_OPTION) {

                    SkladPDB pdb = new SkladPDB();
                    CurrencySet[] currencySet = pdb.getCurrencyRateAsArray(datePicker.getDate(), false);
                    pdb.disConn();

                    PlanOO oo = new PlanOO(
                            table.getValueAt(table.getSelectedRow(), 2).toString(),
                            getDataItemReport(
                                    true,
                                    Boolean.valueOf(buttonGroupReport.getSelection().getActionCommand()),
                                    textReport.getText().trim(), currencySet), currencySet);

                    oo.createReport("ProjectPlanCenaAll.ots");
                }
                datePicker.setVisible(false);
                lblDatePicker.setVisible(false);
            } else
                JOptionPane.showMessageDialog(null,
                        "Вы не выбрали проект плана производства!",
                        "Внимание",
                        javax.swing.JOptionPane.WARNING_MESSAGE);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Ошибка! " + e.getMessage(),
                    "Ошибка",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }//GEN-LAST:event_jMenuItem11ActionPerformed
    // End of variables declaration//GEN-END:variables

    private void initMenu() {
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenuItem4 = new javax.swing.JMenuItem();
        jMenu3 = new javax.swing.JMenu();
        jMenuItem5 = new javax.swing.JMenuItem();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem3 = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        jMenuItem6 = new javax.swing.JMenuItem();
        jMenuItem7 = new javax.swing.JMenuItem();
        jMenuItem8 = new javax.swing.JMenuItem();
        jMenu4 = new javax.swing.JMenu();
        jMenuItem11 = new javax.swing.JMenuItem();
        jMenuItem9 = new javax.swing.JMenuItem();
        jMenuItem10 = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jMenu1.setText("Правка");

        jMenuItem2.setText("Удалить");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem2);

        jMenuItem4.setText("Справочники");
        jMenuItem4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem4ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem4);

        jMenuBar1.add(jMenu1);

        jMenu3.setText("Сервис");

        jMenuItem5.setText("Обновить артикула");
        jMenuItem5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem5ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem5);

        jMenuItem1.setText("Обновить спецификации");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem1);

        jMenuItem3.setText("Округление");
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem3);

        jMenuBar1.add(jMenu3);

        jMenu2.setText("Отдел цен");

        jMenuItem6.setText("Артикула в проекте");
        jMenuItem6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem6ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem6);

        jMenuItem7.setText("Модели");
        jMenuItem7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem7ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem7);

        jMenuItem8.setText("Ассортимент с ценами");
        jMenuItem8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem8ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem8);

        jMenuBar1.add(jMenu2);

        jMenuBar1.add(jMenu2);

        jMenu4.setText("Документы");

        jMenuItem11.setText("Ассортимент все цены");
        jMenuItem11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem11ActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItem11);

        jMenuItem9.setText("Ассортимент для покупателя РБ");
        jMenuItem9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem9ActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItem9);

        jMenuItem10.setText("Ассортимент для покупателя РФ");
        jMenuItem10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem10ActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItem10);

        jMenuBar1.add(jMenu4);

        setJMenuBar(jMenuBar1);

        jMenu1.setVisible(false);
        jMenuItem1.setVisible(false);
        jMenuItem2.setVisible(false);
        jMenuItem4.setVisible(false);
        jMenuItem5.setVisible(false);
        jMenu2.setVisible(false);
        jMenuItem6.setVisible(false);

        menu.put("1", jMenu1);              //Правка
        menu.put("1-1", jMenuItem2);            //Удалить            
        menu.put("1-2", jMenuItem4);            //Справочники
        menu.put("2", jMenu3);              //Сервис        
        menu.put("2-1", jMenuItem3);            //Округление
        menu.put("2-2", jMenuItem1);            //Обновить спецификации
        menu.put("2-3", jMenuItem5);            //Обновить артикула
        menu.put("3", jMenu2);              //Отдел цен        
        menu.put("3-1", jMenuItem6);            //Артикула в проекте
        menu.put("3-2", jMenuItem7);            //Модели
        menu.put("3-3", jMenuItem8);            //Ассортимен с ценами
        menu.put("4", jMenu4);              //Документы   
        menu.put("4-1", jMenuItem9);            //Ассортимент для покупателя РБ
        menu.put("4-2", jMenuItem10);           //Ассортимент для покупателя РФ
    }

    private void postInit() {
        controller.menuFormInitialisation(getClass().getName(), menu);
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

        try {
            UtilPlan.DBF_MODELS16 = UtilFunctions.readPropFileString(UtilPlan.SETTING_DBF_MODELS16);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ошибка! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }

        try {
            UtilPlan.DBF_RASHOD = UtilFunctions.readPropFileString(UtilPlan.SETTING_DBF_RASKLAD);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ошибка! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }

        try {
            UtilPlan.DBF_RASHOD_DETAL = UtilFunctions.readPropFileString(UtilPlan.SETTING_DBF_RASKLAD_DETAL);
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

        buttPanel.add(buttOpen);
        buttPanel.add(new JLabel());
        buttPanel.add(new JLabel());
        buttPanel.add(buttSelect);

        jMenuBar1.setVisible(false);
    }


    private void initAnalysisDialog() {
        checkboxSt.doClick();
        checkboxIns.doClick();

        jRadioButton2.setSelected(true);

        jRadioButton1.setEnabled(false);
        jRadioButton2.setEnabled(false);
        jRadioButton3.setEnabled(false);
        jRadioButton4.setEnabled(false);

        buttPanel.removeAll();

        buttPanel.add(buttOpen);
        buttPanel.add(buttAnalysis);

        jMenuBar1.setVisible(false);
    }

    private void init() {
        setTitle("Проект плана производства");

        setMinimumSize(new Dimension(640, 500));
        setPreferredSize(new Dimension(850, 700));

        final JRadioButton jrAll = new JRadioButton();
        jrAll.setFont(new java.awt.Font("Dialog", 0, 13));
        jrAll.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jrAll.setText("все");
        jrAll.setActionCommand("false");
        jrAll.setSelected(true);

        final JRadioButton jrText = new JRadioButton();
        jrText.setFont(new java.awt.Font("Dialog", 0, 13));
        jrText.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jrText.setText("только");
        jrText.setActionCommand("true");

        buttonGroupReport = new ButtonGroup();
        buttonGroupReport.add(jrAll);
        buttonGroupReport.add(jrText);

        textReport = new JTextField(UtilPlan.TEXT_SEARCH);
        textReport.setPreferredSize(new Dimension(100, 25));

        datePicker = new UCDatePicker(new Date());
        datePicker.setPreferredSize(new Dimension(100, 20));
        datePicker.setDate(new Date());
        lblDatePicker = new JLabel("Дата курса валюты :");

/*      Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.MONTH, -1);*/

        panelReport = new JPanel();
        panelReport.setLayout(new ParagraphLayout());
        panelReport.add(new JLabel(), ParagraphLayout.NEW_PARAGRAPH);
        panelReport.add(new JLabel("Сформировать дополнение:"));
        panelReport.add(new JLabel(), ParagraphLayout.NEW_PARAGRAPH);
        panelReport.add(lblDatePicker);
        panelReport.add(datePicker);
        panelReport.add(new JLabel(), ParagraphLayout.NEW_PARAGRAPH);
        panelReport.add(jrAll);
        panelReport.add(jrText);
        panelReport.add(textReport);

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
        buttAdd = new JButton("Добавить");
        buttCopy = new JButton("Копировать");
        buttOpen = new JButton("Открыть");
        buttAnalysis = new JButton("Анализ");
        buttSelect = new JButton("Выбрать");
        buttEdit = new JButton("Редактировать");
        col = new Vector();
        table = new JTable();
        listNum = new JTextField();
        textTableSearch = new JTextField();
        title = new JLabel("Проекты плана производства");
        checkboxSt = new JCheckBox("Дата");
        checkboxIns = new JCheckBox("Корректировка");
        buttonGroup = new ButtonGroup();
        jRadioButton1 = new JRadioButton();
        jRadioButton2 = new JRadioButton();
        jRadioButton3 = new JRadioButton();
        jRadioButton4 = new JRadioButton();

        osnova.setLayout(new BorderLayout(1, 1));
        osnova.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        searchPanel.setLayout(new ParagraphLayout());
        searchButtPanel.setLayout(new GridLayout(0, 3, 5, 5));
        buttPanel.setLayout(new GridLayout(0, 4, 5, 5));
        buttPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        upPanel.setLayout(new BorderLayout(1, 1));
        upPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        searchButtPanel.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        buttPanel.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);

        listNum.setPreferredSize(new Dimension(80, 20));
        sStDate.setPreferredSize(new Dimension(200, 20));
        eStDate.setPreferredSize(new Dimension(200, 20));
        sInsDate.setPreferredSize(new Dimension(200, 20));
        eInsDate.setPreferredSize(new Dimension(200, 20));
        buttSearch.setPreferredSize(new Dimension(50, 20));

        jRadioButton1.setFont(new java.awt.Font("Dialog", 0, 13));
        jRadioButton2.setFont(new java.awt.Font("Dialog", 0, 13));
        jRadioButton3.setFont(new java.awt.Font("Dialog", 0, 13));
        jRadioButton4.setFont(new java.awt.Font("Dialog", 0, 13));

        jRadioButton1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jRadioButton2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jRadioButton3.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jRadioButton4.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        jRadioButton1.setText("Все;");
        jRadioButton2.setText("Проект;");
        jRadioButton3.setText("Формируется;");
        jRadioButton4.setText("Удалён;");

        jRadioButton1.setActionCommand("");
        jRadioButton2.setActionCommand("1");
        jRadioButton3.setActionCommand("3");
        jRadioButton4.setActionCommand("-2");

        jRadioButton1.setSelected(true);
        checkboxIns.setSelected(true);

        sStDate.setEnabled(false);
        eStDate.setEnabled(false);

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
                    switch (Integer.valueOf(table.getValueAt(row, 7).toString())) {
                        case -2:
                            cell.setBackground(Color.PINK);
                            break;
                        case 3:
                            cell.setBackground(Color.YELLOW);
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

        buttAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttAddActionPerformed(evt);
            }
        });

        buttSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttSearchActionPerformed(evt);
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

        buttAnalysis.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttAnalysisActionPerformed(evt);
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
        col.add("Проект №");
        col.add("Название");
        col.add("Дата");
        col.add("Автор");
        col.add("Дата коррект.");
        col.add("Статус");
        col.add("id cтатус");

        buttonGroup.add(jRadioButton1);
        buttonGroup.add(jRadioButton2);
        buttonGroup.add(jRadioButton3);
        buttonGroup.add(jRadioButton4);

        searchPanel.add(new JLabel("Проект №:"));
        searchPanel.add(listNum);
        searchPanel.add(new JLabel("    Статус:"));
        searchPanel.add(jRadioButton1);
        searchPanel.add(jRadioButton2);
        searchPanel.add(jRadioButton3);
        searchPanel.add(jRadioButton4);
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

        buttPanel.add(buttOpen);
        buttPanel.add(buttEdit);
        buttPanel.add(buttCopy);
        buttPanel.add(buttAdd);

        if (!jMenu1.isVisible() && !jMenuItem2.isVisible()) {
            jRadioButton1.setEnabled(false);
            jRadioButton2.setEnabled(true);
            jRadioButton3.setEnabled(false);
            jRadioButton4.setEnabled(false);

            jRadioButton2.setSelected(true);
        }

        osnova.add(upPanel, BorderLayout.NORTH);
        osnova.add(new JScrollPane(table), BorderLayout.CENTER);
        osnova.add(buttPanel, BorderLayout.SOUTH);

        getContentPane().add(osnova);
        pack();
    }

    private File buttSelectSpravActionPerformed(ActionEvent evt, String file) {
        final JFileChooser fc = new JFileChooser(file);
        fc.setFileSelectionMode(JFileChooser.OPEN_DIALOG);
        fc.setFileFilter(new javax.swing.filechooser.FileFilter() {
            @Override
            public boolean accept(File f) {
                if (f != null) {
                    if (f.isDirectory()) {
                        return true;
                    }
                }
                if (f.getName().endsWith(".DBF")) {
                    return true;
                }
                if (f.getName().endsWith(".dbf")) {
                    return true;
                }
                return false;
            }

            @Override
            public String getDescription() {
                return "";
            }
        });
        fc.setAcceptAllFileFilterUsed(false);

        if (fc.showDialog(ProjectPlanForm.this, null) == JFileChooser.APPROVE_OPTION) {
            return fc.getSelectedFile();
        }
        return null;
    }

    private void buttSearchActionPerformed(ActionEvent evt) {
        createProjectPlanTable(getPlans());
    }

    private void buttAddActionPerformed(ActionEvent evt) {
        new ProjectDetalForm(controller, true);
    }

    private void buttOpenActionPerformed(ActionEvent evt) {
        getDataProjectPlan(UtilPlan.PROJECT_OPEN);
    }

    private void buttAnalysisActionPerformed(ActionEvent evt) {
        getDataAnalysisPlans();
    }

    private void buttSelectActionPerformed(ActionEvent evt) {
        try {
            if (table.getSelectedRow() != -1) {
                UtilPlan.PLAN_SELECT_NUM = Integer.valueOf(table.getValueAt(table.getSelectedRow(), 1).toString());
                UtilPlan.PLAN_SELECT_NAME = table.getValueAt(table.getSelectedRow(), 2).toString();
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
                    "Выбрать проект плана не удалось!\n " + e.getMessage(),
                    "Ошибка",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buttEditActionPerformed(ActionEvent evt) {
        getDataProjectPlan(UtilPlan.PROJECT_EDIT);
    }

    private void buttCopyActionPerformed(ActionEvent evt) {
        getDataProjectPlan(UtilPlan.PROJECT_COPY);
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

    private void createProjectPlanTable(final Vector data) {
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
        table.getColumnModel().getColumn(1).setPreferredWidth(30);
        table.getColumnModel().getColumn(2).setPreferredWidth(130);
        table.getColumnModel().getColumn(3).setPreferredWidth(50);
        table.getColumnModel().getColumn(4).setPreferredWidth(90);
        table.getColumnModel().getColumn(5).setPreferredWidth(40);
        table.getColumnModel().getColumn(6).setPreferredWidth(50);
        table.getColumnModel().getColumn(7).setMinWidth(0);
        table.getColumnModel().getColumn(7).setMaxWidth(0);

        sorter = new TableRowSorter<TableModel>(tModel);
        table.setRowSorter(sorter);
        table.getColumnModel().getColumn(0).setHeaderRenderer(new CheckBoxHeader(table.getTableHeader(), 0, ""));

        table.getColumnModel().getColumn(6).setCellRenderer(renderer);
    }

    private Vector getPlans() {
        row = new Vector();
        try {
            ppdb = new PlanPDB();
            pb = new ProgressBar(this, false, "Поиск проектов плана производства...");
            final SwingWorker sw = new SwingWorker() {
                @Override
                protected Object doInBackground() throws Exception {
                    row = ppdb.getAllProject(checkboxSt.isSelected(),
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

    private void getDataProjectPlan(String type) {
        if (table.getSelectedRow() != -1) {
            dataProj = new Vector();
            dataItem = new Vector();
            dataItemDetal = new Vector();
            dataItemVnorm = new Vector();
            dataItemDekad = new Vector();

            if (type.equals(UtilPlan.PROJECT_OPEN)) {
                pb = new ProgressBar(ProjectPlanForm.this, false, "Сбор данных ...");
                SwingWorker sw = new SwingWorker() {
                    protected Object doInBackground() {
                        try {
                            ppdb = new PlanPDB();

                            dataProj = ppdb.getDataPlan(Integer.valueOf(table.getValueAt(table.getSelectedRow(), 1).toString()));
                            dataItem = ppdb.getDataItemProjectStok(Integer.valueOf(table.getValueAt(table.getSelectedRow(), 1).toString()));
                            dataItemVnorm = ppdb.getDataItemProjectVnorm(Integer.valueOf(table.getValueAt(table.getSelectedRow(), 1).toString()));
                            dataItemDekad = ppdb.getDataItemProjectDekad(Integer.valueOf(table.getValueAt(table.getSelectedRow(), 1).toString()));
                            dataItemDetal = ppdb.getDataItemProjectDetal(Integer.valueOf(table.getValueAt(table.getSelectedRow(), 1).toString()));

                        } catch (Exception e) {
                            dataProj = new Vector();
                            dataItem = new Vector();
                            dataItemVnorm = new Vector();
                            dataItemDekad = new Vector();
                            dataItemDetal = new Vector();
                            JOptionPane.showMessageDialog(null, "Данные проекта плана производства не загружены! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
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

                new ProjectDetalForm(controller, true, dataProj, dataItem, dataItemDetal, dataItemVnorm, dataItemDekad,
                        Integer.valueOf(table.getValueAt(table.getSelectedRow(), 1).toString()));

            } else if (type.equals(UtilPlan.PROJECT_EDIT) || type.equals(UtilPlan.PROJECT_COPY)) {
                try {
                    ppdb = new PlanPDB();
                    dataProj = ppdb.getDataPlan(Integer.valueOf(table.getValueAt(table.getSelectedRow(), 1).toString()));

                } catch (Exception e) {
                    dataProj = new Vector();
                    JOptionPane.showMessageDialog(null, "Данные проекта плана производства не загружены! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
                } finally {
                    ppdb.disConn();
                }

                new ProjectDetalForm(controller, true, dataProj, Integer.valueOf(table.getValueAt(table.getSelectedRow(), 1).toString()), type);
            }

        } else
            JOptionPane.showMessageDialog(null, "Вы ничего не выбрали!", "Внимание", javax.swing.JOptionPane.WARNING_MESSAGE);
    }

    private void getDataAnalysisPlans() {
        if (table.getSelectedRow() != -1) {
            dataAnalysis = new Vector();

            pb = new ProgressBar(ProjectPlanForm.this, false, "Сбор данных ...");
            SwingWorker sw = new SwingWorker() {
                protected Object doInBackground() {
                    try {
                        ppdb = new PlanPDB();

                        dataAnalysis = ppdb.getDataAnalysisPlan(Integer.valueOf(table.getValueAt(table.getSelectedRow(), 1).toString()));

                    } catch (Exception e) {
                        dataAnalysis = new Vector();
                        JOptionPane.showMessageDialog(null,
                                "Данные не загружены! " + e.getMessage(),
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

            new AnalysisPlansForm(controller, true,
                    Integer.valueOf(table.getValueAt(table.getSelectedRow(), 1).toString()),
                    table.getValueAt(table.getSelectedRow(), 2).toString(),
                    dataAnalysis);


        } else
            JOptionPane.showMessageDialog(null,
                    "Вы ничего не выбрали!",
                    "Внимание",
                    javax.swing.JOptionPane.WARNING_MESSAGE);
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

    private void cleanConstants() {
        UtilPlan.ACTION_BUTT_PLAN_SELECT = false;
        UtilPlan.PLAN_SELECT_NUM = -1;
        UtilPlan.PLAN_SELECT_NAME = "";
    }

    private Vector getDataItemReport(final boolean flagKurs, final boolean flagSearch, final String textSearch, CurrencySet[] currencySet) {
        dataItemDetal = new Vector();
        kursRUB = 1.25;
        kursUSD = 1;
        kursEUR = 1;

        pb = new ProgressBar(ProjectPlanForm.this, false, "Сбор данных ...");

        SwingWorker sw = new SwingWorker() {
            protected Object doInBackground() {
                //данные проекта плана
                try {
                    ppdb = new PlanPDB();
                    dataItemDetal = ppdb.getDataReportProject(
                            Integer.valueOf(table.getValueAt(table.getSelectedRow(), 1).toString()),
                            flagSearch,
                            textSearch);

                } catch (Exception e) {
                    dataItemDetal = new Vector();

                    JOptionPane.showMessageDialog(null,
                            "Данные не загружены! " + e.getMessage(),
                            "Ошибка",
                            javax.swing.JOptionPane.ERROR_MESSAGE);
                } finally {
                    ppdb.disConn();
                }

                // курсы валют
                if (flagKurs) {
                    try {
                        vpdb = new ValutaPDB();
                        Vector kurs = vpdb.getKursList(0);

                        for (Object obj : kurs) {
                            Vector object = (Vector) obj;

                            if (object.get(0).toString().equals("USD")
                                    && object.get(1).toString().equals("RUB"))
                                kursUSD = Double.valueOf(object.get(2).toString());

                            if (object.get(0).toString().equals("EUR")
                                    && object.get(1).toString().equals("RUB"))
                                kursEUR = Double.valueOf(object.get(2).toString());
                        }

                    } catch (Exception e) {
                        kursRUB = 1.25;
                        kursUSD = 1;
                        kursEUR = 1;

                        JOptionPane.showMessageDialog(null,
                                "Данные не загружены! " + e.getMessage(),
                                "Ошибка",
                                javax.swing.JOptionPane.ERROR_MESSAGE);
                    } finally {
                        vpdb.disConn();
                    }
                }

                if (currencySet != null) {
                    kursRUB = currencySet[0].getRate();
                    kursUSD = currencySet[1].getRate();
                    kursEUR = currencySet[2].getRate();

                    // цены из March8
                    try {
                        pdb = new PlanDB();
                        dataItemDetal = pdb.getDataReportCenaProjectPlanFork(
                                dataItemDetal,
                                flagKurs,
                                kursRUB,
                                kursUSD,
                                kursEUR);

                    } catch (Exception e) {
                        dataItemDetal = new Vector();

                        JOptionPane.showMessageDialog(null,
                                "Данные не загружены! " + e.getMessage(),
                                "Ошибка",
                                javax.swing.JOptionPane.ERROR_MESSAGE);
                    } finally {
                        pdb.disConn();
                    }
                }


                // цены из March8
                try {
                    pdb = new PlanDB();
                    dataItemDetal = pdb.getDataReportCenaProjectPlan(
                            dataItemDetal,
                            flagKurs,
                            kursRUB,
                            kursUSD,
                            kursEUR);

                } catch (Exception e) {
                    dataItemDetal = new Vector();

                    JOptionPane.showMessageDialog(null,
                            "Данные не загружены! " + e.getMessage(),
                            "Ошибка",
                            javax.swing.JOptionPane.ERROR_MESSAGE);
                } finally {
                    pdb.disConn();
                }

                return null;
            }

            protected void done() {
                pb.dispose();
            }
        };
        sw.execute();
        pb.setVisible(true);

        return dataItemDetal;
    }
}

