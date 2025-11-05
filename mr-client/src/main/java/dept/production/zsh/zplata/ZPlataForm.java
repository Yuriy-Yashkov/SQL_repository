package dept.production.zsh.zplata;

import by.march8.ecs.MainController;
import by.march8.ecs.framework.common.LogCrutch;
import com.jhlabs.awt.ParagraphLayout;
import com.toedter.calendar.JDateChooser;
import common.CheckBoxHeader;
import common.FormMenu;
import common.ProgressBar;
import common.User;
import common.UtilFunctions;
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
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import java.util.Vector;

/**
 *
 * @author lidashka
 */
public class ZPlataForm extends javax.swing.JDialog implements FormMenu {
    //private static final Logger log = new Log().getLoger(ZPlataForm.class);
    private static final LogCrutch log = new LogCrutch();
    public static Map<String, JMenuItem> menu = new TreeMap();
    ZPlataPDB zpdb = null;
    PDB pdb = null;
    ButtonGroup buttonGroup;
    JButton buttSearch;
    JButton buttTableSearch;
    JButton buttEdit;
    JButton buttOpen;
    JButton buttAdd;
    JDateChooser sStDate;
    JDateChooser eStDate;
    JDateChooser sInsDate;
    JDateChooser eInsDate;
    JComboBox dept;
    JComboBox brig;
    JComboBox brigadir;
    JCheckBox checkboxSt;
    JCheckBox checkboxIns;
    JLabel title;
    JPanel osnova;
    JPanel searchPanel;
    JPanel searchButtPanel;
    JPanel buttPanel;
    JPanel upPanel;
    JTable table;
    JTextField modelNum;
    JTextField marshrutNum;
    JTextField specNum;
    JTextField listNum;
    JTextField textTableSearch;
    DefaultTableModel tModel;
    Vector col;
    Vector row;
    Vector dataBrigadir;
    TableRowSorter sorter;
    DefaultTableCellRenderer renderer;
    JRadioButton jRadioButton1;
    JRadioButton jRadioButton2;
    JRadioButton jRadioButton3;
    JRadioButton jRadioButton4;
    ProgressBar pb;
    int minSelectedRow = -1;
    int maxSelectedRow = -1;
    boolean tableModelListenerIsChanging = false;
    private User user = User.getInstance();
    private MainController controller;
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenu jMenu4;
    private javax.swing.JMenu jMenu5;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem10;
    private javax.swing.JMenuItem jMenuItem11;
    private javax.swing.JMenuItem jMenuItem12;
    private javax.swing.JMenuItem jMenuItem13;
    private javax.swing.JMenuItem jMenuItem14;
    private javax.swing.JMenuItem jMenuItem15;
    private javax.swing.JMenuItem loadingEquipment;
    private javax.swing.JMenuItem jMenuItem17;
    private javax.swing.JMenuItem jMenuItem18;
    private javax.swing.JMenuItem jMenuItem19;
    private javax.swing.JMenuItem jMenuItem20;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JMenuItem jMenuItem5;
    private javax.swing.JMenuItem jMenuItem6;
    private javax.swing.JMenuItem jMenuItem7;
    private javax.swing.JMenuItem jMenuItem8;
    private javax.swing.JMenuItem jMenuItem9;
    public ZPlataForm() {
        initMenu();
    }
    public ZPlataForm(MainController mainController, boolean modal) {
        super(mainController.getMainForm(), modal);
        controller = mainController;

        if (user.getFio() != null) {
            int read = -1;
            String readStr = "";

            try {
                UtilZPlata.DEPT_SELECT_ITEM = UtilFunctions.readPropFile(UtilZPlata.SETTING_DEPT_SELECT_ITEM);
                UtilZPlata.BRIG_SELECT_ITEM = UtilFunctions.readPropFile(UtilZPlata.SETTING_BRIG_SELECT_ITEM);
                UtilZPlata.BRIGADIR_SELECT_ITEM = UtilFunctions.readPropFile(UtilZPlata.SETTING_BRIGADIR_SELECT_ITEM);
                read = UtilFunctions.readPropFile(UtilZPlata.SETTING_ROUNDING_NORM);
                UtilZPlata.ROUNDING_NORM = (read != -1) ? read : 4;
                read = UtilFunctions.readPropFile(UtilZPlata.SETTING_PLAN_WORKING_TIME);
                UtilZPlata.PLAN_WORKING_TIME = (read != -1) ? read : 169;
                readStr = UtilFunctions.readPropFileString(UtilZPlata.SETTING_RATE_1ST_CATEGORY).trim();
                UtilZPlata.RATE_1ST_CATEGORY = (!readStr.isEmpty())
                        ? BigDecimal.valueOf(Double.parseDouble(readStr.trim().replace(",", ".")))
                        .setScale(2, RoundingMode.HALF_UP).doubleValue()
                        : UtilZPlata.RATE_1ST_CATEGORY;

            } catch (Exception e) {
                JOptionPane.showMessageDialog(null,
                        "Ошибка! " + e.getMessage(),
                        "Ошибка",
                        javax.swing.JOptionPane.ERROR_MESSAGE);
            }

            try {
                readStr = UtilFunctions.readPropFileString(UtilZPlata.SETTING_STAFF_1).trim();
                UtilZPlata.STAFF_1 = (!readStr.equals("") ? readStr : UtilZPlata.STAFF_1);
                readStr = UtilFunctions.readPropFileString(UtilZPlata.SETTING_STAFF_2).trim();
                UtilZPlata.STAFF_2 = (!readStr.equals("") ? readStr : UtilZPlata.STAFF_2);
                UtilZPlata.STAFF_3 = UtilFunctions.readPropFileString(UtilZPlata.SETTING_STAFF_3);
                UtilZPlata.STAFF_4 = UtilFunctions.readPropFileString(UtilZPlata.SETTING_STAFF_4);
                UtilZPlata.STAFF_5 = UtilFunctions.readPropFileString(UtilZPlata.SETTING_STAFF_5);
            } catch (Exception e) {
                UtilZPlata.STAFF_1 = "";
                UtilZPlata.STAFF_2 = "";
                UtilZPlata.STAFF_3 = "";
                UtilZPlata.STAFF_4 = "";
                UtilZPlata.STAFF_5 = "";
                JOptionPane.showMessageDialog(null,
                        "Ошибка! " + e.getMessage(),
                        "Ошибка",
                        javax.swing.JOptionPane.ERROR_MESSAGE);
            }

            try {
                readStr = UtilFunctions.readPropFileString(UtilZPlata.SETTING_DATE_VVOD).trim();

                if (!readStr.equals("") && UtilFunctions.checkDate(readStr))
                        UtilZPlata.DATE_VVOD = readStr;
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Ошибка! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
                try {
                    UtilFunctions.setSettingPropFile(new SimpleDateFormat("dd.MM.yyyy").format((Calendar.getInstance()).getTime()), UtilZPlata.SETTING_DATE_VVOD);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Ошибка! " + ex.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
                }
            }

            try {
                zpdb = new ZPlataPDB();
                Vector data = zpdb.getDept();

                UtilZPlata.DEPT_MODEL = new Vector();
                UtilZPlata.fullModel(UtilZPlata.DEPT_MODEL, data);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Список подразделений не загружен! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
            } finally {
                zpdb.disConn();
            }

            try {
                zpdb = new ZPlataPDB();
                Vector data = zpdb.getBrig();

                UtilZPlata.BRIG_MODEL = new Vector();
                UtilZPlata.fullModel(UtilZPlata.BRIG_MODEL, data);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Список бригад не загружен! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
            } finally {
                zpdb.disConn();
            }

            try {
                zpdb = new ZPlataPDB();
                Vector data = zpdb.getBrigadir(UtilZPlata.DEPT_SELECT_ITEM);

                UtilZPlata.BRIGADIR_MODEL = new Vector();
                UtilZPlata.fullModel(UtilZPlata.BRIGADIR_MODEL, data);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Список бригадиров не загружен! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
            } finally {
                zpdb.disConn();
            }

            init();

            java.util.Date d = (Calendar.getInstance()).getTime();
            d.setDate(1);

            sStDate.setDate(d);
            eStDate.setDate((Calendar.getInstance()).getTime());

            sInsDate.setDate(d);
            eInsDate.setDate((Calendar.getInstance()).getTime());

            createZPTable(getLists());

            setLocationRelativeTo(controller.getMainForm());
            setVisible(true);
        } else
            JOptionPane.showMessageDialog(null, "Такой логин и пароль не присвоен ни одному пользователю. \n Обратитесь к администратору! ", "Вход", javax.swing.JOptionPane.WARNING_MESSAGE);
    }

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        try {
            zpdb = new ZPlataPDB();
            for (Object rows : tModel.getDataVector()) {
                if (((Vector) rows).get(0).toString().equals("true"))
                    zpdb.deleteList(Integer.valueOf(((Vector) rows).get(4).toString()),
                            Integer.valueOf(user.getIdEmployee()));
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ошибка удаления! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        } finally {
            zpdb.disConn();
        }
        createZPTable(getLists());
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jMenuItem5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem5ActionPerformed
        new VedomostForm(controller, true, sStDate.getDate(), eStDate.getDate());
    }//GEN-LAST:event_jMenuItem5ActionPerformed

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        try {
            zpdb = new ZPlataPDB();
            JDateChooser format = new JDateChooser();
            format.setDate((Calendar.getInstance()).getTime());
            if (JOptionPane.showOptionDialog(null, format, "Показать начиная с: ", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, new Object[]{"Да", "Отмена"}, format) == JOptionPane.YES_OPTION) {
                if (UtilFunctions.checkDate(new SimpleDateFormat("dd.MM.yyyy").format(format.getDate()))) {
                    Vector listEmploye = zpdb.searchList(Integer.valueOf(user.getIdEmployee()),
                            UtilFunctions.convertDateStrToLong(new SimpleDateFormat("dd.MM.yyyy").format(format.getDate())));

                    if (!listEmploye.isEmpty()) {
                        createZPTable(listEmploye);
                        listNum.setText("");
                        marshrutNum.setText("");
                        modelNum.setText("");
                        jRadioButton1.setSelected(true);

                        try {
                            dept.setSelectedItem(UtilZPlata.getIndexModel(UtilZPlata.DEPT_MODEL, -1));
                        } catch (Exception e) {
                            JOptionPane.showMessageDialog(null, "Ошибка! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
                        }

                        try {
                            brig.setSelectedItem(UtilZPlata.getIndexModel(UtilZPlata.BRIG_MODEL, -1));
                        } catch (Exception e) {
                            JOptionPane.showMessageDialog(null, "Ошибка! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
                        }

                        try {
                            brigadir.setSelectedItem(UtilZPlata.getIndexModel(UtilZPlata.BRIGADIR_MODEL, UtilZPlata.getItemsModel(UtilZPlata.BRIGADIR_MODEL, user.getFio())));
                        } catch (Exception e) {
                            JOptionPane.showMessageDialog(null, "Ошибка! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
                        }

                        checkboxIns.setSelected(false);

                        checkboxSt.setSelected(true);
                        sStDate.setDate(new SimpleDateFormat("dd.MM.yyyy").parse(new SimpleDateFormat("dd.MM.yyyy").format(format.getDate())));
                        eStDate.setDate((Calendar.getInstance()).getTime());
                    } else
                        JOptionPane.showMessageDialog(null, "Не найдено ни одного листка запуска!", "Внимание!", javax.swing.JOptionPane.WARNING_MESSAGE);

                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ошибка! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        } finally {
            zpdb.disConn();
        }
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed
        new VedomostForm(controller, true, UtilZPlata.OTCHET_NV, sStDate.getDate(), eStDate.getDate());
    }//GEN-LAST:event_jMenuItem3ActionPerformed

    private void jMenuItem7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem7ActionPerformed
        try {
            JSpinner rounding = new JSpinner();
            rounding.setValue(UtilZPlata.ROUNDING_NORM);

            if (JOptionPane.showOptionDialog(null,
                    rounding,
                    "Округлять до знака: ",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null, new Object[]{"Да", "Отмена"},
                    rounding) == JOptionPane.YES_OPTION) {

                UtilZPlata.ROUNDING_NORM = Integer.valueOf(rounding.getValue().toString());
                UtilFunctions.setSettingPropFile(
                        String.valueOf(UtilZPlata.ROUNDING_NORM),
                        UtilZPlata.SETTING_ROUNDING_NORM);

            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Ошибка! " + e.getMessage(),
                    "Ошибка",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jMenuItem7ActionPerformed

    private void jMenuItem6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem6ActionPerformed
        new VedomostForm(controller, true, sStDate.getDate(), eStDate.getDate(), "");
    }//GEN-LAST:event_jMenuItem6ActionPerformed

    private void jMenuItem8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem8ActionPerformed
        new VedomostForm(controller, true, sStDate.getDate(), eStDate.getDate(), true);
    }//GEN-LAST:event_jMenuItem8ActionPerformed

    private void jMenuItem9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem9ActionPerformed
        new VedomostForm(controller, true, UtilZPlata.OTCHET_NV_EMPL, sStDate.getDate(), eStDate.getDate());
    }//GEN-LAST:event_jMenuItem9ActionPerformed

    private void jMenuItem10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem10ActionPerformed
        new VedomostForm(controller, true, false, sStDate.getDate(), eStDate.getDate());
    }//GEN-LAST:event_jMenuItem10ActionPerformed

    private void jMenuItem20ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem10ActionPerformed
        new VedomostForm(controller, sStDate.getDate(), eStDate.getDate());
    }//GEN-LAST:event_jMenuItem20ActionPerformed

    private void jMenuItem11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem11ActionPerformed
        new VedomostForm(controller, true, UtilZPlata.OTCHET_T4_NEW, sStDate.getDate(), eStDate.getDate());
    }//GEN-LAST:event_jMenuItem11ActionPerformed

    private void jMenuItem12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem12ActionPerformed
        new BuhVedomostForm(this, true, UtilZPlata.ADD);
    }//GEN-LAST:event_jMenuItem12ActionPerformed

    private void jMenuItem13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem13ActionPerformed
        new BuhVedomostForm(this, true, UtilZPlata.OPEN);
    }//GEN-LAST:event_jMenuItem13ActionPerformed

    private void jMenuItem14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem14ActionPerformed
        try {
            JTextField rate = new JTextField();
            rate.setText(String.valueOf(UtilZPlata.RATE_1ST_CATEGORY));

            if (JOptionPane.showOptionDialog(null, rate,
                    "Тарифная ставка 1-го р.: ",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.INFORMATION_MESSAGE,
                    null,
                    new Object[]{"Сохранить", "Отмена"},
                    rate) == JOptionPane.YES_OPTION) {

                UtilZPlata.RATE_1ST_CATEGORY =
                        new BigDecimal(Double.valueOf(rate.getText().trim().replace(",", ".")))
                                .setScale(2, RoundingMode.HALF_UP).doubleValue();

                UtilFunctions.setSettingPropFile(
                        String.valueOf(UtilZPlata.RATE_1ST_CATEGORY),
                        UtilZPlata.SETTING_RATE_1ST_CATEGORY);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Ошибка! " + e.getMessage(),
                    "Ошибка",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jMenuItem14ActionPerformed

    private void jMenuItem15ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem15ActionPerformed
        try {
            JTextField workigTime = new JTextField();
            workigTime.setText(String.valueOf(UtilZPlata.PLAN_WORKING_TIME));

            if (JOptionPane.showOptionDialog(null, workigTime,
                    "Плановое раб. время: ",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.INFORMATION_MESSAGE,
                    null,
                    new Object[]{"Сохранить", "Отмена"},
                    workigTime) == JOptionPane.YES_OPTION) {

                UtilZPlata.PLAN_WORKING_TIME = Integer.valueOf(workigTime.getText().trim());

                UtilFunctions.setSettingPropFile(
                        String.valueOf(UtilZPlata.PLAN_WORKING_TIME),
                        UtilZPlata.SETTING_PLAN_WORKING_TIME);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Ошибка! " + e.getMessage(),
                    "Ошибка",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jMenuItem15ActionPerformed

    private void loadingEquipmentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem16ActionPerformed
        new VedomostForm(controller, true, false, sStDate.getDate(), eStDate.getDate(), "");
    }//GEN-LAST:event_jMenuItem16ActionPerformed

    private void jMenuItem17ActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            final JTextField staff_1 = new JTextField(UtilZPlata.STAFF_1);
            staff_1.setPreferredSize(new Dimension(300, 20));
            staff_1.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

            final JTextField staff_2 = new JTextField(UtilZPlata.STAFF_2);
            staff_2.setPreferredSize(new Dimension(300, 20));
            staff_2.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

            final JTextField staff_3 = new JTextField(UtilZPlata.STAFF_3);
            staff_3.setPreferredSize(new Dimension(300, 20));
            staff_3.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

            final JTextField staff_4 = new JTextField(UtilZPlata.STAFF_4);
            staff_4.setPreferredSize(new Dimension(300, 20));
            staff_4.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

            final JTextField staff_5 = new JTextField(UtilZPlata.STAFF_5);
            staff_5.setPreferredSize(new Dimension(300, 20));
            staff_5.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

            JPanel panel = new JPanel();
            panel.setLayout(new ParagraphLayout());
            panel.add(new JLabel(UtilZPlata.PROF_STAFF_1), ParagraphLayout.NEW_PARAGRAPH);
            panel.add(new JScrollPane(staff_1));
            panel.add(new JLabel(UtilZPlata.PROF_STAFF_2), ParagraphLayout.NEW_PARAGRAPH);
            panel.add(new JScrollPane(staff_2));
            panel.add(new JLabel(UtilZPlata.PROF_STAFF_3), ParagraphLayout.NEW_PARAGRAPH);
            panel.add(new JScrollPane(staff_3));
            panel.add(new JLabel(UtilZPlata.PROF_STAFF_4), ParagraphLayout.NEW_PARAGRAPH);
            panel.add(new JScrollPane(staff_4));
            panel.add(new JLabel(UtilZPlata.PROF_STAFF_5), ParagraphLayout.NEW_PARAGRAPH);
            panel.add(new JScrollPane(staff_5));

            if (JOptionPane.showOptionDialog(null,
                    panel,
                    "ФИО сотрудников (для отчетов)",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.INFORMATION_MESSAGE,
                    null,
                    new Object[]{"Сохранить", "Отмена"},
                    "Сохранить") == JOptionPane.YES_OPTION) {

                try {
                    UtilZPlata.STAFF_1 = staff_1.getText().trim();

                    UtilFunctions.setSettingPropFile(UtilZPlata.STAFF_1, UtilZPlata.SETTING_STAFF_1);
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "Ошибка! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
                }
                try {
                    UtilZPlata.STAFF_2 = staff_2.getText().trim();

                    UtilFunctions.setSettingPropFile(UtilZPlata.STAFF_2, UtilZPlata.SETTING_STAFF_2);
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "Ошибка! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
                }
                try {
                    UtilZPlata.STAFF_3 = staff_3.getText().trim();

                    UtilFunctions.setSettingPropFile(UtilZPlata.STAFF_3, UtilZPlata.SETTING_STAFF_3);
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "Ошибка! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
                }
                try {
                    UtilZPlata.STAFF_4 = staff_4.getText().trim();

                    UtilFunctions.setSettingPropFile(UtilZPlata.STAFF_4, UtilZPlata.SETTING_STAFF_4);
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "Ошибка! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
                }
                try {
                    UtilZPlata.STAFF_5 = staff_5.getText().trim();

                    UtilFunctions.setSettingPropFile(UtilZPlata.STAFF_5, UtilZPlata.SETTING_STAFF_5);
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "Ошибка! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
                }
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ошибка! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void jMenuItem18ActionPerformed(java.awt.event.ActionEvent evt) {
        new VedomostForm(controller, true, UtilZPlata.OTCHET_NV_MODEL, sStDate.getDate(), eStDate.getDate());
    }

    private void jMenuItem19ActionPerformed(java.awt.event.ActionEvent evt) {
        new VedomostForm(controller, true, UtilZPlata.OTCHET_T4_NEW_BY_PERSONAL_NUMBER, sStDate.getDate(), eStDate.getDate());
    }
    // End of variables declaration//GEN-END:variables

    private void initMenu() {
        jMenuBar1 = new JMenuBar();
        jMenu1 = new JMenu();
        jMenuItem1 = new JMenuItem();
        jMenu4 = new JMenu();
        jMenuItem2 = new JMenuItem();
        jMenuItem5 = new JMenuItem();
        jMenuItem6 = new JMenuItem();
        jMenuItem8 = new JMenuItem();
        jMenuItem10 = new JMenuItem();
        jMenuItem20 = new JMenuItem();
        loadingEquipment = new JMenuItem();
        jMenu2 = new JMenu();
        jMenuItem7 = new JMenuItem();
        jMenuItem15 = new JMenuItem();
        jMenuItem14 = new JMenuItem();
        jMenuItem17 = new JMenuItem();
        jMenuItem18 = new JMenuItem();
        jMenu3 = new JMenu();
        jMenuItem4 = new JMenuItem();
        jMenuItem11 = new JMenuItem();
        jMenuItem19 = new JMenuItem();
        jMenuItem3 = new JMenuItem();
        jMenuItem9 = new JMenuItem();
        jMenu5 = new JMenu();
        jMenuItem12 = new JMenuItem();
        jMenuItem13 = new JMenuItem();
        JMenuItem brigVedomost = new JMenuItem();

        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        jMenu1.setText("Правка");

        jMenuItem1.setText("Удалить");
        jMenuItem1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem1);

        jMenuBar1.add(jMenu1);

        jMenu4.setText("Просмотр");

        jMenuItem2.setText("Мои листки запуска");
        jMenuItem2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItem2);

        jMenuItem5.setText("Выработка");
        jMenuItem5.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                jMenuItem5ActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItem5);

        jMenuItem6.setText("Количество");
        jMenuItem6.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                jMenuItem6ActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItem6);

        jMenuItem8.setText("Несортные изд.");
        jMenuItem8.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                jMenuItem8ActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItem8);

        jMenuItem10.setText("Выполнение");
        jMenuItem10.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                jMenuItem10ActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItem10);

        jMenuItem20.setText("Использование оборудования");
        jMenuItem20.addActionListener(this::jMenuItem20ActionPerformed);
        jMenu4.add(jMenuItem20);

        loadingEquipment.setText("Загрузка оборудования");
        loadingEquipment.addActionListener(this::loadingEquipmentActionPerformed);
        jMenu4.add(loadingEquipment);

        jMenuBar1.add(jMenu4);

        jMenu2.setText("Сервис");

        jMenuItem7.setText("Округление");
        jMenuItem7.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                jMenuItem7ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem7);

        jMenuItem15.setText("Плановое раб. время");
        jMenuItem15.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                jMenuItem15ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem15);

        jMenuItem14.setText("Ставка 1-го разряда");
        jMenuItem14.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                jMenuItem14ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem14);

        jMenuItem17.setText("ФИО (для отчетов)");
        jMenuItem17.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                jMenuItem17ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem17);

        jMenuBar1.add(jMenu2);

        jMenu3.setText("Документы");

        jMenu3.add(jMenuItem4);

        jMenuItem11.setText("НОВАЯ Ведомость T4 ");
        jMenuItem11.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                jMenuItem11ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem11);

        jMenuItem19.setText("НОВАЯ Ведомость T4(по таб.№)");
        jMenuItem19.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                jMenuItem19ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem19);

        jMenuItem3.setText("Накопительная ведомость");
        jMenuItem3.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem3);

        jMenuItem9.setText("НВ по рабочим");
        jMenuItem9.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                jMenuItem9ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem9);

        jMenuItem18.setText("НВ по моделям");
        jMenuItem18.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                jMenuItem18ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem18);

        brigVedomost.setText("НВ по бригадирам");
        brigVedomost.addActionListener(a -> {
                ZPlataOO zPlataOO = new ZPlataOO();
            try {
                zPlataOO.createReport(UtilZPlata.NV_BRIG_REP);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        jMenu3.add(brigVedomost);

        jMenuBar1.add(jMenu3);

        jMenu5.setText("Бухгалтерия");

        jMenuItem12.setText("Отправить ведомость Т4");
        jMenuItem12.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                jMenuItem12ActionPerformed(evt);
            }
        });
        jMenu5.add(jMenuItem12);

        jMenuItem13.setText("Просмотр сохраненных данных");
        jMenuItem13.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                jMenuItem13ActionPerformed(evt);
            }
        });
        jMenu5.add(jMenuItem13);

        jMenuBar1.add(jMenu5);

        setJMenuBar(jMenuBar1);

        jMenu1.setVisible(true);
        jMenu3.setVisible(true);
        jMenuItem1.setVisible(true);
        jMenuItem2.setVisible(false);
        jMenuItem3.setVisible(false);
        jMenuItem4.setVisible(false);
        jMenuItem18.setVisible(false);

        menu.put("1", jMenu1);              //Правка
        menu.put("1-1", jMenuItem1);            //Удалить
        menu.put("2", jMenu4);              //Просмотр
        menu.put("2-1", jMenuItem2);            //Мои листки запуска
        menu.put("2-2", jMenuItem5);            //Выработка
        menu.put("2-3", jMenuItem6);            //Количество
        menu.put("2-4", jMenuItem8);            //Несортные изд.
        menu.put("2-5", jMenuItem10);           //Выполнение
        menu.put("2-6", jMenuItem20);
        menu.put("3", jMenu2);              //Сервис    
        menu.put("3-1", jMenuItem7);            //Округление
        menu.put("3-3", jMenuItem14);           //Ставка 1-го разр.
        menu.put("3-4", jMenuItem17);           //ФИО (для отчетов)
        menu.put("4", jMenu3);              //Документы
        //menu.put("4-1", jMenuItem4);          //Ведомость Т4           
        menu.put("4-5", jMenuItem11);           //Ведомость Т4 новая
        menu.put("4-6", jMenuItem19);           //Ведомость Т4 по таб.№
        menu.put("4-2", jMenuItem3);            //Накопительная ведомость    
        menu.put("4-3", jMenuItem9);            //НВ по рабочим
        menu.put("4-4", jMenuItem18);           //НВ по моделям
        menu.put("5", jMenu5);              //Бухг.
        menu.put("5-2", jMenuItem12);           //Сохранить ведомость Т4 
        menu.put("5-3", jMenuItem13);           //Просмотр сохраненных данных
    }

    private void init() {
        setTitle("Учет выработки рабочих");

        setMinimumSize(new Dimension(580, 500));
        setPreferredSize(new Dimension(880, 750));

        initMenu();

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
        buttTableSearch = new JButton("Поиск");
        buttEdit = new JButton("Изменить");
        buttOpen = new JButton("Открыть");
        buttAdd = new JButton("Добавить");
        col = new Vector();
        table = new JTable();
        listNum = new JTextField();
        modelNum = new JTextField();
        marshrutNum = new JTextField();
        specNum = new JTextField();
        textTableSearch = new JTextField();
        title = new JLabel("Листки запуска и передачи полуфабриката");
        checkboxSt = new JCheckBox("Дата");
        checkboxIns = new JCheckBox("Корр-ка");
        dept = new JComboBox(UtilZPlata.DEPT_MODEL);
        brig = new JComboBox(UtilZPlata.BRIG_MODEL);
        brigadir = new JComboBox(UtilZPlata.BRIGADIR_MODEL);
        buttonGroup = new ButtonGroup();
        jRadioButton1 = new JRadioButton();
        jRadioButton2 = new JRadioButton();
        jRadioButton3 = new JRadioButton();
        jRadioButton4 = new JRadioButton();

        osnova.setLayout(new BorderLayout(1, 1));
        osnova.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        searchPanel.setLayout(new ParagraphLayout());
        searchButtPanel.setLayout(new GridLayout(0, 3, 5, 5));
        searchButtPanel.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        buttPanel.setLayout(new GridLayout(0, 5, 5, 5));
        buttPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        upPanel.setLayout(new BorderLayout(1, 1));
        upPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        dept.setPreferredSize(new Dimension(300, 20));
        brig.setPreferredSize(new Dimension(150, 20));
        brigadir.setPreferredSize(new Dimension(300, 20));
        listNum.setPreferredSize(new Dimension(120, 20));
        modelNum.setPreferredSize(new Dimension(120, 20));
        marshrutNum.setPreferredSize(new Dimension(120, 20));
        specNum.setPreferredSize(new Dimension(200, 20));
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
        jRadioButton2.setText("Закрыт;");
        jRadioButton3.setText("Формируется;");
        jRadioButton4.setText("Удалён;");

        jRadioButton1.setActionCommand("");
        jRadioButton2.setActionCommand("1");
        jRadioButton3.setActionCommand("0");
        jRadioButton4.setActionCommand("-1");

        jRadioButton1.setSelected(true);
        checkboxSt.setSelected(true);

        sInsDate.setEnabled(false);
        eInsDate.setEnabled(false);

        try {
            dept.setSelectedItem(UtilZPlata.getIndexModel(UtilZPlata.DEPT_MODEL, UtilZPlata.DEPT_SELECT_ITEM));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ошибка! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }

        try {
            brig.setSelectedItem(UtilZPlata.getIndexModel(UtilZPlata.BRIG_MODEL, UtilZPlata.BRIG_SELECT_ITEM));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ошибка! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }

        try {
            brigadir.setSelectedItem(UtilZPlata.getIndexModel(UtilZPlata.BRIGADIR_MODEL, UtilZPlata.BRIGADIR_SELECT_ITEM));
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
                    switch (Integer.valueOf(table.getValueAt(row, 12).toString())) {
                        case -1:
                            cell.setBackground(Color.PINK);
                            break;
                        case 0:
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

        dept.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deptActionPerformed(evt);
            }
        });

        brig.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                brigActionPerformed(evt);
            }
        });

        brigadir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                brigadirActionPerformed(evt);
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

        buttEdit.addActionListener(this::buttEditActionPerformed);

        buttOpen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttOpenActionPerformed(evt);
            }
        });

        buttAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttAddActionPerformed(evt);
            }
        });

        col.add("");
        col.add("Цех");
        col.add("Бригада");
        col.add("Бригадир");
        col.add("Листок №");
        col.add("Маршрут №");
        col.add("Модель");
        col.add("Кол-во");
        col.add("Ч Кол-во");
        col.add("Дата ввода");
        col.add("Дата коррект.");
        col.add("Статус");
        col.add("id cтатус");

        buttonGroup.add(jRadioButton1);
        buttonGroup.add(jRadioButton2);
        buttonGroup.add(jRadioButton3);
        buttonGroup.add(jRadioButton4);

        searchPanel.add(new JLabel("    Цех:"));
        searchPanel.add(dept);
        searchPanel.add(new JLabel("    Статус:"));
        searchPanel.add(jRadioButton1);
        searchPanel.add(jRadioButton2);
        searchPanel.add(jRadioButton3);
        searchPanel.add(jRadioButton4);
        searchPanel.add(new JLabel("    Бригада: "), ParagraphLayout.NEW_PARAGRAPH);
        searchPanel.add(brig);
        searchPanel.add(new JLabel("    Бригадир:"));
        searchPanel.add(brigadir);
        searchPanel.add(new JLabel("Листок №:"), ParagraphLayout.NEW_PARAGRAPH);
        searchPanel.add(listNum);
        searchPanel.add(new JLabel("    Маршрут №: "));
        searchPanel.add(marshrutNum);
        searchPanel.add(new JLabel("    Модель №: "));
        searchPanel.add(modelNum);
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
        buttPanel.add(buttAdd);
        buttPanel.add(buttEdit);
        buttPanel.add(buttOpen);

        if (!jMenuItem2.isVisible()) {
            buttPanel.removeAll();
            buttPanel.add(buttOpen);
            buttPanel.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        }

        osnova.add(upPanel, BorderLayout.NORTH);
        osnova.add(new JScrollPane(table), BorderLayout.CENTER);
        osnova.add(buttPanel, BorderLayout.SOUTH);

        getContentPane().add(osnova);
        pack();
    }

    private void deptActionPerformed(ActionEvent evt) {
        try {
            UtilZPlata.DEPT_SELECT_ITEM = ((Item) dept.getSelectedItem()).getId();
            UtilFunctions.setSettingPropFile(String.valueOf(((Item) dept.getSelectedItem()).getId()), UtilZPlata.SETTING_DEPT_SELECT_ITEM);

            dataBrigadir = new Vector();
            try {
                zpdb = new ZPlataPDB();
                pb = new ProgressBar(this, false, "Поиск бригадиров...");
                final SwingWorker sw = new SwingWorker() {
                    @Override
                    protected Object doInBackground() throws Exception {
                        dataBrigadir = zpdb.getBrigadir(((Item) dept.getSelectedItem()).getId());
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
                dataBrigadir = new Vector();
                JOptionPane.showMessageDialog(null, "Ошибка поиска cписка бригадиров! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
            } finally {
                zpdb.disConn();
            }

            brigadir.removeAllItems();

            UtilZPlata.BRIGADIR_MODEL = new Vector();
            UtilZPlata.fullModel(UtilZPlata.BRIGADIR_MODEL, dataBrigadir);

            for (Iterator it = UtilZPlata.BRIGADIR_MODEL.iterator(); it.hasNext(); )
                brigadir.addItem(it.next());

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ошибка! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void brigActionPerformed(ActionEvent evt) {
        try {
            UtilZPlata.BRIG_SELECT_ITEM = ((Item) brig.getSelectedItem()).getId();
            UtilFunctions.setSettingPropFile(String.valueOf(((Item) brig.getSelectedItem()).getId()), UtilZPlata.SETTING_BRIG_SELECT_ITEM);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ошибка! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void brigadirActionPerformed(ActionEvent evt) {
       /* try {
            UtilZPlata.BRIGADIR_SELECT_ITEM = ((Item) UtilZPlata.BRIGADIR_MODEL.get(brigadir.getSelectedIndex())).getId();
            UtilFunctions.setSettingPropFile(String.valueOf(((Item) UtilZPlata.BRIGADIR_MODEL.get(brigadir.getSelectedIndex())).getId()), UtilZPlata.SETTING_BRIGADIR_SELECT_ITEM);                     
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ошибка! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);       
        }       */
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
                if (table.getRowCount() == 0)
                    JOptionPane.showMessageDialog(null, "По заданному запросу ничего не найдено!", "Внимание", javax.swing.JOptionPane.WARNING_MESSAGE);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Ошибка " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void textTableSearchKeyPressed(java.awt.event.KeyEvent evt) {
        if (evt.getKeyCode() == evt.VK_ENTER) buttTableSearch.doClick();
    }

    private void buttEditActionPerformed(ActionEvent evt) {
        getDataEditOrOpenList(1);
        createZPTable(getLists());
    }

    private void buttOpenActionPerformed(ActionEvent evt) {
        getDataEditOrOpenList(2);
    }

    private void buttAddActionPerformed(ActionEvent evt) {
        new ZPlataDetalForm(controller, true);
        createZPTable(getLists());
    }

    private void buttSearchActionPerformed(ActionEvent evt) {
        createZPTable(getLists());
    }

    private void createZPTable(final Vector data) {
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
                return col == 0;
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
        table.getColumnModel().getColumn(2).setPreferredWidth(40);
        table.getColumnModel().getColumn(3).setPreferredWidth(40);
        table.getColumnModel().getColumn(4).setPreferredWidth(40);
        table.getColumnModel().getColumn(5).setPreferredWidth(40);
        table.getColumnModel().getColumn(6).setPreferredWidth(40);
        table.getColumnModel().getColumn(7).setPreferredWidth(40);
        table.getColumnModel().getColumn(8).setPreferredWidth(40);
        table.getColumnModel().getColumn(9).setPreferredWidth(40);
        table.getColumnModel().getColumn(10).setPreferredWidth(40);
        table.getColumnModel().getColumn(11).setPreferredWidth(40);
        table.getColumnModel().getColumn(12).setMinWidth(0);
        table.getColumnModel().getColumn(12).setMaxWidth(0);

        sorter = new TableRowSorter<TableModel>(tModel);
        table.setRowSorter(sorter);
        table.getColumnModel().getColumn(0).setHeaderRenderer(new CheckBoxHeader(table.getTableHeader(), 0, ""));

        table.getColumnModel().getColumn(11).setCellRenderer(renderer);
    }

    private Vector getLists() {
        row = new Vector();
        try {
            zpdb = new ZPlataPDB();
            pb = new ProgressBar(this, false, "Поиск листков запуска...");
            final SwingWorker sw = new SwingWorker() {
                @Override
                protected Object doInBackground() throws Exception {
                    row = zpdb.getAllList(
                            ((Item) dept.getSelectedItem()).getId(),
                            ((Item) brig.getSelectedItem()).getId(),
                            ((Item) brigadir.getSelectedItem()).getId(),
                            checkboxSt.isSelected(),
                            UtilFunctions.convertDateStrToLong(new SimpleDateFormat("dd.MM.yyyy").format(sStDate.getDate())),
                            UtilFunctions.convertDateStrToLong(new SimpleDateFormat("dd.MM.yyyy").format(eStDate.getDate())),
                            checkboxIns.isSelected(),
                            UtilFunctions.convertDateStrToLong(new SimpleDateFormat("dd.MM.yyyy").format(sStDate.getDate())),
                            UtilFunctions.convertDateStrToLong(new SimpleDateFormat("dd.MM.yyyy").format(eStDate.getDate())),
                            listNum.getText().trim(),
                            modelNum.getText().trim(),
                            marshrutNum.getText().trim(),
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
            JOptionPane.showMessageDialog(null, "Ошибка поиска листков запуска! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        } finally {
            zpdb.disConn();
        }
        return row;
    }

    private void getDataEditOrOpenList(int type) {
        if (table.getSelectedRow() != -1) {
            Vector dataList = new Vector();
            Vector dataItem = new Vector();

            try {
                zpdb = new ZPlataPDB();
                dataList = zpdb.getDataList(Integer.parseInt(table.getValueAt(table.getSelectedRow(), 4).toString()));
                dataItem = zpdb.getDataListItem(Integer.parseInt(table.getValueAt(table.getSelectedRow(), 4).toString()));

                switch (type) {
                    case 1:
                        new ZPlataDetalForm(controller, true,
                                dataList,
                                dataItem,
                                Integer.parseInt(table.getValueAt(table.getSelectedRow(), 4).toString()),
                                true);
                        break;
                    case 2:
                        new ZPlataDetalForm(controller, true,
                                dataList,
                                dataItem,
                                Integer.parseInt(table.getValueAt(table.getSelectedRow(), 4).toString()));
                        break;
                    default:
                        break;
                }

            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Данные листка запуска не загружены! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
            } finally {
                zpdb.disConn();
            }
        } else
            JOptionPane.showMessageDialog(null, "Вы не выбрали листок запуска!", "Внимание", javax.swing.JOptionPane.WARNING_MESSAGE);
    }

    public Map<String, JMenuItem> getMenuMap() {
        return menu;
    }

    public void setFormVisible() {
        this.setVisible(false);
    }

    public void disposeForm() {
        dispose();
    }
}