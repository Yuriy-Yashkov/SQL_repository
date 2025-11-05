package dept.sklad.ho;

import by.march8.ecs.MainController;
import com.jhlabs.awt.ParagraphLayout;
import com.toedter.calendar.JDateChooser;
import common.CheckBoxHeader;
import common.FormMenu;
import common.Item;
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
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.TreeMap;
import java.util.Vector;

/**
 *
 * @author lidashka
 */
@SuppressWarnings("all")
public class SkladHOForm extends javax.swing.JDialog implements FormMenu {
    private static TreeMap menu = new TreeMap();
    int minSelectedMoveRow = -1;
    int maxSelectedMoveRow = -1;
    boolean tableModelMoveListenerIsChanging = false;
    int minSelectedProductRow = -1;
    int maxSelectedProductRow = -1;
    boolean tableModelProductListenerIsChanging = false;
    int minSelectedMaterialRow = -1;
    int maxSelectedMaterialRow = -1;
    boolean tableModelMaterialListenerIsChanging = false;
    int minSelectedIzdRow = -1;
    int maxSelectedIzdRow = -1;
    boolean tableModelIzdListenerIsChanging = false;
    private User user = User.getInstance();
    private HashMap<String, String> mapSettingMove;
    private HashMap<String, String> mapSettingProduct;
    private HashMap<String, String> mapSettingMaterial;
    private HashMap<String, String> mapSettingIzd;
    private ProgressBar pb;
    private Vector dataTable;
    private PDB pdb;
    private SkladHOPDB spdb;
    private JPanel osnova;
    private JPanel searchPanel;
    private JPanel searchButtPanel;
    private JPanel upPanel;
    private JPanel buttPanel;
    private JPanel filterRow;
    private JPanel tablePanel;
    private JPanel panelCheckBox;
    private JButton buttSearch;
    private JButton buttAdd;
    private JButton buttPrint;
    private JButton buttOpen;
    private JButton buttEdit;
    private JButton buttCopy;
    private JCheckBox checkboxSt;
    private JCheckBox checkboxIns;
    private JDateChooser sStDate;
    private JDateChooser eStDate;
    private JDateChooser sInsDate;
    private JDateChooser eInsDate;
    private ButtonGroup buttonGroup;
    private JRadioButton jRadioButton1;
    private JRadioButton jRadioButton2;
    private JRadioButton jRadioButton3;
    private JRadioButton jRadioButton4;
    private JTabbedPane tableTabPane;
    private JPanel tableMovePanel;
    private JPanel tableProductPanel;
    private JPanel tableMaterialPanel;
    private JPanel tableIzdPanel;
    private JPanel filterMoveRow;
    private JPanel filterProductRow;
    private JPanel filterMaterialRow;
    private JPanel filterIzdRow;
    private JTable tableMove;
    private JTable tableProduct;
    private JTable tableMaterial;
    private JTable tableIzd;
    private Vector colMove;
    private Vector colProduct;
    private Vector colMaterial;
    private Vector colIzd;
    private DefaultTableModel tModelMove;
    private DefaultTableModel tModelProduct;
    private DefaultTableModel tModelMaterial;
    private DefaultTableModel tModelIzd;
    private TableRowSorter<TableModel> sorterMove;
    private TableRowSorter<TableModel> sorterProduct;
    private TableRowSorter<TableModel> sorterMaterial;
    private TableRowSorter<TableModel> sorterIzd;
    private TableFilterHeader filterHeaderMove;
    private TableFilterHeader filterHeaderProduct;
    private TableFilterHeader filterHeaderMaterial;
    private TableFilterHeader filterHeaderIzd;
    private DefaultTableCellRenderer renderer;
    private MainController controller;
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenu jMenu4;
    private javax.swing.JMenu jMenu5;
    private javax.swing.JMenu jMenu6;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem10;
    private javax.swing.JMenuItem jMenuItem11;
    private javax.swing.JMenuItem jMenuItem12;
    private javax.swing.JMenuItem jMenuItem13;
    private javax.swing.JMenuItem jMenuItem14;
    private javax.swing.JMenuItem jMenuItem15;
    private javax.swing.JMenuItem jMenuItem16;
    private javax.swing.JMenuItem jMenuItem17;
    private javax.swing.JMenuItem jMenuItem18;
    private javax.swing.JMenuItem jMenuItem19;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem20;
    private javax.swing.JMenuItem jMenuItem21;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JMenuItem jMenuItem5;
    private javax.swing.JMenuItem jMenuItem6;
    private javax.swing.JMenuItem jMenuItem7;
    private javax.swing.JMenuItem jMenuItem8;
    private javax.swing.JMenuItem jMenuItem9;
    public SkladHOForm() {
        initMenu();
    }
    public SkladHOForm(MainController mainController, boolean modal) {
        super(mainController.getMainForm(), modal);
        controller = mainController;
        if (user.getFio() != null) {

            initData();

            init();

            try {
                String[][] arr = UtilFunctions.readPropFileStringsArray(UtilSkladHO.SETTING_MAP_MOVE_EDIT);

                for (int i = 0; i < arr.length; i++)
                    mapSettingMove.put(arr[i][0], arr[i][1]);

            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Ошибка! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
            }

            try {
                String[][] arr = UtilFunctions.readPropFileStringsArray(UtilSkladHO.SETTING_MAP_PRODUCT_EDIT);

                for (int i = 0; i < arr.length; i++)
                    mapSettingProduct.put(arr[i][0], arr[i][1]);

            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Ошибка! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
            }

            try {
                String[][] arr = UtilFunctions.readPropFileStringsArray(UtilSkladHO.SETTING_MAP_MATERIAL_EDIT);

                for (int i = 0; i < arr.length; i++)
                    mapSettingMaterial.put(arr[i][0], arr[i][1]);

            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Ошибка! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
            }

            try {
                String[][] arr = UtilFunctions.readPropFileStringsArray(UtilSkladHO.SETTING_MAP_IZD_EDIT);

                for (int i = 0; i < arr.length; i++)
                    mapSettingIzd.put(arr[i][0], arr[i][1]);

            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Ошибка! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
            }

            java.util.Date d = (Calendar.getInstance()).getTime();
            d.setDate(1);

            sStDate.setDate(d);
            eStDate.setDate((Calendar.getInstance()).getTime());

            sInsDate.setDate(d);
            eInsDate.setDate((Calendar.getInstance()).getTime());

            buttSearch.doClick();

            setLocationRelativeTo(controller.getMainForm());
            setVisible(true);

        } else
            JOptionPane.showMessageDialog(null, "Такой логин и пароль не присвоен ни одному пользователю. \n Обратитесь к администратору! ", "Вход", javax.swing.JOptionPane.WARNING_MESSAGE);

    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem8 = new javax.swing.JMenuItem();
        jMenu3 = new javax.swing.JMenu();
        jMenuItem3 = new javax.swing.JMenuItem();
        jMenuItem5 = new javax.swing.JMenuItem();
        jMenuItem19 = new javax.swing.JMenuItem();
        jMenuItem15 = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenuItem14 = new javax.swing.JMenuItem();
        jMenuItem20 = new javax.swing.JMenuItem();
        jMenu4 = new javax.swing.JMenu();
        jMenuItem16 = new javax.swing.JMenuItem();
        jMenuItem4 = new javax.swing.JMenuItem();
        jMenuItem7 = new javax.swing.JMenuItem();
        jMenuItem10 = new javax.swing.JMenuItem();
        jMenuItem9 = new javax.swing.JMenuItem();
        jMenuItem17 = new javax.swing.JMenuItem();
        jMenu5 = new javax.swing.JMenu();
        jMenu6 = new javax.swing.JMenu();
        jMenuItem12 = new javax.swing.JMenuItem();
        jMenuItem13 = new javax.swing.JMenuItem();
        jMenuItem11 = new javax.swing.JMenuItem();
        jMenuItem18 = new javax.swing.JMenuItem();
        jMenuItem6 = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jMenu1.setText("Правка");

        jMenuItem1.setText("Удалить");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem1);

        jMenuItem8.setText("Закрытые остатки");
        jMenuItem8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem8ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem8);

        jMenuBar1.add(jMenu1);

        jMenu3.setText("Создать");

        jMenuItem3.setText("Новое движение ТМЦ");
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem3);

        jMenuItem5.setText("Новая карта раскроя");
        jMenuItem5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem5ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem5);

        jMenuItem19.setText("Новая сдача на склад");
        jMenuItem19.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem19ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem19);

        jMenuItem15.setText("Закрыть период");
        jMenuItem15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem15ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem15);

        jMenuBar1.add(jMenu3);

        jMenu2.setText("Просмотр");

        jMenuItem2.setText("Остатки в кладовой");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem2);

        jMenuItem14.setText("Остатки в ХЭО/Внедр.");
        jMenuItem14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem14ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem14);

        jMenuItem20.setText("Остатки моделей в кладовой ");
        jMenuItem20.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem20ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem20);

        jMenuBar1.add(jMenu2);

        jMenu4.setText("Документы");

        jMenuItem16.setText("Накладная на выдачу");
        jMenuItem16.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem16ActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItem16);

        jMenuItem4.setText("Карта раскроя");
        jMenuItem4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem4ActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItem4);

        jMenuItem7.setText("Движение фабричного полотна");
        jMenuItem7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem7ActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItem7);

        jMenuItem10.setText("Движение импортного полотна");
        jMenuItem10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem10ActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItem10);

        jMenuItem9.setText("Движение всп. материалов");
        jMenuItem9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem9ActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItem9);

        jMenuItem17.setText("ПО по движению импортного полотна");
        jMenuItem17.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem17ActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItem17);

        jMenuItem21.setText("Инвентаризационная опись");
        jMenuItem21.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem21ActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItem21);

        jMenuBar1.add(jMenu4);

        jMenu5.setText("Параметры");

        jMenu6.setText("Столбцы таблиц");
        jMenu6.setToolTipText("");

        jMenuItem12.setText("Движение ТМЦ");
        jMenuItem12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem12ActionPerformed(evt);
            }
        });
        jMenu6.add(jMenuItem12);

        jMenuItem13.setText("Карты раскроя");
        jMenuItem13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem13ActionPerformed(evt);
            }
        });
        jMenu6.add(jMenuItem13);

        jMenuItem11.setText("ТМЦ в К/р");
        jMenuItem11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem11ActionPerformed(evt);
            }
        });
        jMenu6.add(jMenuItem11);

        jMenuItem18.setText("Сдача на склад");
        jMenuItem18.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem18ActionPerformed(evt);
            }
        });
        jMenu6.add(jMenuItem18);

        jMenu5.add(jMenu6);

        jMenuItem6.setText("Коэффициент кондиционной массы");
        jMenuItem6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem6ActionPerformed(evt);
            }
        });
        jMenu5.add(jMenuItem6);

        jMenuBar1.add(jMenu5);

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

            JTable table = new JTable();
            DefaultTableModel tModel = new DefaultTableModel();

            if (tableTabPane.getModel().getSelectedIndex() == 0) {
                tModel = tModelMove;
                table = tableMove;
            } else if (tableTabPane.getModel().getSelectedIndex() == 1) {
                tModel = tModelProduct;
                table = tableProduct;
            } else if (tableTabPane.getModel().getSelectedIndex() == 2) {
                tModel = tModelMaterial;
                table = tableMaterial;
            } else if (tableTabPane.getModel().getSelectedIndex() == 3) {
                tModel = tModelIzd;
                table = tableIzd;
            }

            for (int i = 0; i < table.getRowCount(); i++) {
                Vector vec = (Vector) tModel.getDataVector()
                        .get(table.convertRowIndexToModel(i));

                if (vec.get(0).toString().equals("true")) {
                    flagSelect = true;
                    break;
                }
            }

            if (flagSelect) {
                if (JOptionPane.showOptionDialog(null,
                        "Удалить выделенные записи из справочника?",
                        "Удаление",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        new Object[]{"Да", "Отмена"}, "Да") == JOptionPane.YES_OPTION) {

                    try {
                        spdb = new SkladHOPDB();

                        if (tableTabPane.getModel().getSelectedIndex() == 0) {

                            for (int i = 0; i < tableMove.getRowCount(); i++) {
                                Vector vec = (Vector) tModelMove.getDataVector()
                                        .get(tableMove.convertRowIndexToModel(i));

                                if (vec.get(0).toString().equals("true")) {
                                    if (!spdb.deleteMoveTMC(Integer.valueOf(vec.get(1).toString()))) {
                                        JOptionPane.showMessageDialog(null,
                                                "Сбой удаления! ",
                                                "Ошибка",
                                                javax.swing.JOptionPane.ERROR_MESSAGE);
                                        break;
                                    }
                                }
                            }

                            createSkladMoveTable(getDataTable(UtilSkladHO.DATA_MOVE));

                        } else if (tableTabPane.getModel().getSelectedIndex() == 1) {

                            for (int i = 0; i < tableProduct.getRowCount(); i++) {
                                Vector vec = (Vector) tModelProduct.getDataVector()
                                        .get(tableProduct.convertRowIndexToModel(i));

                                if (vec.get(0).toString().equals("true")) {
                                    if (!spdb.deleteProd(Integer.valueOf(vec.get(1).toString()))) {
                                        JOptionPane.showMessageDialog(null,
                                                "Сбой удаления! ",
                                                "Ошибка",
                                                javax.swing.JOptionPane.ERROR_MESSAGE);
                                        break;
                                    }
                                }
                            }

                            createSkladMoveTable(getDataTable(UtilSkladHO.DATA_MOVE));
                            createSkladProductTable(getDataTable(UtilSkladHO.DATA_PRODUCT));
                            createSkladMaterialTable(getDataTable(UtilSkladHO.DATA_MATERIAL));
                            createSkladIzdTable(getDataTable(UtilSkladHO.DATA_IZD));

                        } else if (tableTabPane.getModel().getSelectedIndex() == 2) {

                            for (int i = 0; i < tableMaterial.getRowCount(); i++) {
                                Vector vec = (Vector) tModelMaterial.getDataVector()
                                        .get(tableMaterial.convertRowIndexToModel(i));

                                if (vec.get(0).toString().equals("true")) {
                                    if (!spdb.deleteProdItem(Integer.valueOf(vec.get(1).toString()))) {
                                        JOptionPane.showMessageDialog(null,
                                                "Сбой удаления! ",
                                                "Ошибка",
                                                javax.swing.JOptionPane.ERROR_MESSAGE);
                                        break;
                                    }
                                }
                            }

                            createSkladProductTable(getDataTable(UtilSkladHO.DATA_PRODUCT));
                            createSkladMaterialTable(getDataTable(UtilSkladHO.DATA_MATERIAL));

                        } else if (tableTabPane.getModel().getSelectedIndex() == 3) {

                            for (int i = 0; i < tableIzd.getRowCount(); i++) {
                                Vector vec = (Vector) tModelIzd.getDataVector()
                                        .get(tableIzd.convertRowIndexToModel(i));

                                if (vec.get(0).toString().equals("true")) {
                                    if (!spdb.deleteIzd(Integer.valueOf(vec.get(1).toString()))) {
                                        JOptionPane.showMessageDialog(null,
                                                "Сбой удаления! ",
                                                "Ошибка",
                                                javax.swing.JOptionPane.ERROR_MESSAGE);
                                        break;
                                    }
                                }
                            }

                            createSkladIzdTable(getDataTable(UtilSkladHO.DATA_IZD));
                        }

                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(null,
                                "Ошибка удаления! " + e.getMessage(),
                                "Ошибка",
                                javax.swing.JOptionPane.ERROR_MESSAGE);
                    } finally {
                        spdb.disConn();
                    }
                }
            } else {
                JOptionPane.showMessageDialog(null,
                        "Вы не выбрали записи для удаления!",
                        "Внимание",
                        javax.swing.JOptionPane.WARNING_MESSAGE);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Ошибка " + e.getMessage(),
                    "Ошибка",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed
        try {
            tableTabPane.setSelectedIndex(0);

            buttAdd.doClick();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Ошибка ", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jMenuItem3ActionPerformed

    private void jMenuItem5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem5ActionPerformed
        try {
            tableTabPane.setSelectedIndex(1);

            buttAdd.doClick();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Ошибка ", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jMenuItem5ActionPerformed

    private void jMenuItem6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem6ActionPerformed
        try {
            UtilSkladHO.editMoistureMaterial(UtilSkladHO.ACTUAL_MOISTURE);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Ошибка ", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jMenuItem6ActionPerformed

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        new SkladOstDataForm(this, true);
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void jMenuItem4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem4ActionPerformed
        new VedomostForm(this, true, sStDate.getDate(), eStDate.getDate());
    }//GEN-LAST:event_jMenuItem4ActionPerformed

    private void jMenuItem7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem7ActionPerformed
        new VedomostForm(this, true, UtilSkladHO.REPORT_POLOTNO_F, sStDate.getDate(), eStDate.getDate());
    }//GEN-LAST:event_jMenuItem7ActionPerformed

    private void jMenuItem8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem8ActionPerformed
        new SmallTableForm(SkladHOForm.this, true);
    }//GEN-LAST:event_jMenuItem8ActionPerformed

    private void jMenuItem9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem9ActionPerformed
        new VedomostForm(this, true, UtilSkladHO.REPORT_VMATERIAL, sStDate.getDate(), eStDate.getDate());
    }//GEN-LAST:event_jMenuItem9ActionPerformed

    private void jMenuItem10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem10ActionPerformed
        new VedomostForm(this, true, UtilSkladHO.REPORT_POLOTNO_I, sStDate.getDate(), eStDate.getDate());
    }//GEN-LAST:event_jMenuItem10ActionPerformed

    private void jMenuItem12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem12ActionPerformed
        try {
            createColTable(tableMove, mapSettingMove, UtilSkladHO.SETTING_MAP_MOVE_EDIT);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Ошибка ", javax.swing.JOptionPane.ERROR_MESSAGE);
        } finally {
            createSkladMoveTable(tModelMove.getDataVector());
        }
    }//GEN-LAST:event_jMenuItem12ActionPerformed

    private void jMenuItem13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem13ActionPerformed
        try {
            createColTable(tableProduct, mapSettingProduct, UtilSkladHO.SETTING_MAP_PRODUCT_EDIT);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Ошибка ", javax.swing.JOptionPane.ERROR_MESSAGE);
        } finally {
            createSkladProductTable(tModelProduct.getDataVector());
        }
    }//GEN-LAST:event_jMenuItem13ActionPerformed

    private void jMenuItem11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem11ActionPerformed
        try {
            createColTable(tableMaterial, mapSettingMaterial, UtilSkladHO.SETTING_MAP_MATERIAL_EDIT);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Ошибка ", javax.swing.JOptionPane.ERROR_MESSAGE);
        } finally {
            createSkladMaterialTable(tModelMaterial.getDataVector());
        }
    }//GEN-LAST:event_jMenuItem11ActionPerformed

    private void jMenuItem14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem14ActionPerformed
        new SkladOstDataForm(this, true, "");
    }//GEN-LAST:event_jMenuItem14ActionPerformed

    private void jMenuItem15ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem15ActionPerformed
        try {
            new VedomostForm(SkladHOForm.this, true);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ошибка. " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jMenuItem15ActionPerformed

    private void jMenuItem16ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem16ActionPerformed
        try {
            Vector tmcOtchet = new Vector();

            try {
                tmcOtchet = new Vector();

                for (Object rows : tModelMove.getDataVector()) {
                    if (((Vector) rows).get(0).toString().equals("true")) {
                        Vector tmp = new Vector();

                        tmp.add(((Vector) rows).get(6).toString());
                        tmp.add(((Vector) rows).get(7).toString());
                        tmp.add(((Vector) rows).get(9).toString());
                        tmp.add(((Vector) rows).get(10).toString());
                        tmp.add(((Vector) rows).get(11).toString());
                        tmp.add(Integer.valueOf(((Vector) rows).get(14).toString()));
                        tmp.add(Double.valueOf(((Vector) rows).get(13).toString()));
                        tmcOtchet.add(tmp);
                    }
                }
            } catch (Exception e) {
                tmcOtchet = new Vector();
                JOptionPane.showMessageDialog(null, "Ошибка. " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
            }


            new VedomostForm(this, true, tmcOtchet);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ошибка. " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jMenuItem16ActionPerformed

    private void jMenuItem17ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem17ActionPerformed
        new VedomostForm(this, true, UtilSkladHO.REPORT_POLOTNO_I_PO, sStDate.getDate(), eStDate.getDate());
    }//GEN-LAST:event_jMenuItem17ActionPerformed

    private void jMenuItem18ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem18ActionPerformed
        try {
            createColTable(tableIzd, mapSettingIzd, UtilSkladHO.SETTING_MAP_IZD_EDIT);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Ошибка ", javax.swing.JOptionPane.ERROR_MESSAGE);
        } finally {
            createSkladIzdTable(tModelIzd.getDataVector());
        }
    }//GEN-LAST:event_jMenuItem18ActionPerformed

    private void jMenuItem19ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem19ActionPerformed
        try {
            tableTabPane.setSelectedIndex(3);

            buttAdd.doClick();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Ошибка ", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jMenuItem19ActionPerformed

    private void jMenuItem20ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem20ActionPerformed
        new SkladOstDataForm(this, true, 0);
    }//GEN-LAST:event_jMenuItem20ActionPerformed

    private void jMenuItem21ActionPerformed(java.awt.event.ActionEvent evt) {
        new VedomostForm(this, true, UtilSkladHO.REPORT_INVENTORY, sStDate.getDate());
    }
    // End of variables declaration//GEN-END:variables

    private void init() {
        setTitle(" ХЭО. Учет ТМЦ.");

        setMinimumSize(new Dimension(640, 500));
        setPreferredSize(new Dimension(900, 700));

        initMenu();
        controller.menuFormInitialisation(getClass().getName(), menu);

        osnova = new JPanel();
        searchPanel = new JPanel();
        searchButtPanel = new JPanel();
        upPanel = new JPanel();
        buttPanel = new JPanel();
        tablePanel = new JPanel();
        filterMoveRow = new JPanel();
        filterProductRow = new JPanel();
        filterMaterialRow = new JPanel();
        filterIzdRow = new JPanel();
        tableMovePanel = new JPanel();
        tableProductPanel = new JPanel();
        tableMaterialPanel = new JPanel();
        tableIzdPanel = new JPanel();
        panelCheckBox = new JPanel();

        tableTabPane = new JTabbedPane();
        sStDate = new JDateChooser();
        eStDate = new JDateChooser();
        sInsDate = new JDateChooser();
        eInsDate = new JDateChooser();
        checkboxSt = new JCheckBox("Дата");
        checkboxIns = new JCheckBox("Корр-ка");
        buttAdd = new JButton("Добавить");
        buttPrint = new JButton("Печать");
        buttOpen = new JButton("Открыть");
        buttEdit = new JButton("Редактировать");
        buttCopy = new JButton("Копировать");
        buttSearch = new JButton("Поиск");
        tableMove = new JTable();
        tableProduct = new JTable();
        tableMaterial = new JTable();
        tableIzd = new JTable();
        filterHeaderMove = new TableFilterHeader(tableMove, AutoChoices.ENABLED);
        filterHeaderProduct = new TableFilterHeader(tableProduct, AutoChoices.ENABLED);
        filterHeaderMaterial = new TableFilterHeader(tableMaterial, AutoChoices.ENABLED);
        filterHeaderIzd = new TableFilterHeader(tableIzd, AutoChoices.ENABLED);
        mapSettingMove = new HashMap<String, String>();
        mapSettingProduct = new HashMap<String, String>();
        mapSettingMaterial = new HashMap<String, String>();
        mapSettingIzd = new HashMap<String, String>();
        colMove = new Vector();
        colProduct = new Vector();
        colMaterial = new Vector();
        colIzd = new Vector();
        buttonGroup = new ButtonGroup();
        jRadioButton1 = new JRadioButton();
        jRadioButton2 = new JRadioButton();
        jRadioButton3 = new JRadioButton();
        jRadioButton4 = new JRadioButton();

        osnova.setLayout(new BorderLayout(1, 1));
        osnova.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        searchPanel.setLayout(new ParagraphLayout());
        searchButtPanel.setLayout(new GridLayout(0, 2, 5, 5));
        searchButtPanel.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        buttPanel.setLayout(new GridLayout(0, 6, 5, 5));
        buttPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        buttPanel.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        upPanel.setLayout(new BorderLayout(1, 1));
        upPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        tablePanel.setLayout(new BorderLayout(1, 1));
        filterMoveRow.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
        filterProductRow.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
        filterMaterialRow.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
        filterIzdRow.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
        tableMovePanel.setLayout(new BorderLayout(1, 1));
        tableProductPanel.setLayout(new BorderLayout(1, 1));
        tableMaterialPanel.setLayout(new BorderLayout(1, 1));
        tableIzdPanel.setLayout(new BorderLayout(1, 1));
        panelCheckBox.setLayout(new GridLayout(0, 3, 5, 5));

        buttSearch.setPreferredSize(new Dimension(200, 20));
        sStDate.setPreferredSize(new Dimension(120, 20));
        eStDate.setPreferredSize(new Dimension(120, 20));
        sInsDate.setPreferredSize(new Dimension(120, 20));
        eInsDate.setPreferredSize(new Dimension(120, 20));

        jRadioButton1.setFont(new java.awt.Font("Dialog", 0, 13));
        jRadioButton2.setFont(new java.awt.Font("Dialog", 0, 13));
        jRadioButton3.setFont(new java.awt.Font("Dialog", 0, 13));
        jRadioButton4.setFont(new java.awt.Font("Dialog", 0, 13));

        jRadioButton1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jRadioButton2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jRadioButton3.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jRadioButton4.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        jRadioButton1.setText("Все;");
        jRadioButton2.setText("формируется;");
        jRadioButton3.setText("закрыт;");
        jRadioButton4.setText("удалён;");

        jRadioButton1.setActionCommand("");
        jRadioButton2.setActionCommand("0");
        jRadioButton3.setActionCommand("1");
        jRadioButton4.setActionCommand("-1");

        jRadioButton1.setSelected(true);
        checkboxIns.setSelected(true);

        sStDate.setEnabled(false);
        eStDate.setEnabled(false);

        tableMove.setAutoCreateColumnsFromModel(true);
        tableProduct.setAutoCreateColumnsFromModel(true);
        tableMaterial.setAutoCreateColumnsFromModel(true);
        tableIzd.setAutoCreateColumnsFromModel(true);
        tableMove.getTableHeader().setReorderingAllowed(false);
        tableProduct.getTableHeader().setReorderingAllowed(false);
        tableMaterial.getTableHeader().setReorderingAllowed(false);
        tableIzd.getTableHeader().setReorderingAllowed(false);

        tableMove.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                if (e.getValueIsAdjusting()) {
                    return;
                }
                minSelectedMoveRow = (((DefaultListSelectionModel) e.getSource()).getMinSelectionIndex() == ((DefaultListSelectionModel) e.getSource()).getMaxSelectionIndex()) ?
                        -1 : ((DefaultListSelectionModel) e.getSource()).getMinSelectionIndex();
                maxSelectedMoveRow = (((DefaultListSelectionModel) e.getSource()).getMinSelectionIndex() == ((DefaultListSelectionModel) e.getSource()).getMaxSelectionIndex()) ?
                        -1 : ((DefaultListSelectionModel) e.getSource()).getMaxSelectionIndex();
            }
        });

        tableProduct.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                if (e.getValueIsAdjusting()) {
                    return;
                }
                minSelectedProductRow = (((DefaultListSelectionModel) e.getSource()).getMinSelectionIndex() == ((DefaultListSelectionModel) e.getSource()).getMaxSelectionIndex()) ?
                        -1 : ((DefaultListSelectionModel) e.getSource()).getMinSelectionIndex();
                maxSelectedProductRow = (((DefaultListSelectionModel) e.getSource()).getMinSelectionIndex() == ((DefaultListSelectionModel) e.getSource()).getMaxSelectionIndex()) ?
                        -1 : ((DefaultListSelectionModel) e.getSource()).getMaxSelectionIndex();
            }
        });

        tableMaterial.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                if (e.getValueIsAdjusting()) {
                    return;
                }
                minSelectedMaterialRow = (((DefaultListSelectionModel) e.getSource()).getMinSelectionIndex() == ((DefaultListSelectionModel) e.getSource()).getMaxSelectionIndex()) ?
                        -1 : ((DefaultListSelectionModel) e.getSource()).getMinSelectionIndex();
                maxSelectedMaterialRow = (((DefaultListSelectionModel) e.getSource()).getMinSelectionIndex() == ((DefaultListSelectionModel) e.getSource()).getMaxSelectionIndex()) ?
                        -1 : ((DefaultListSelectionModel) e.getSource()).getMaxSelectionIndex();
            }
        });

        tableIzd.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                if (e.getValueIsAdjusting()) {
                    return;
                }
                minSelectedIzdRow = (((DefaultListSelectionModel) e.getSource()).getMinSelectionIndex() == ((DefaultListSelectionModel) e.getSource()).getMaxSelectionIndex()) ?
                        -1 : ((DefaultListSelectionModel) e.getSource()).getMinSelectionIndex();
                maxSelectedIzdRow = (((DefaultListSelectionModel) e.getSource()).getMinSelectionIndex() == ((DefaultListSelectionModel) e.getSource()).getMaxSelectionIndex()) ?
                        -1 : ((DefaultListSelectionModel) e.getSource()).getMaxSelectionIndex();
            }
        });

        tableMove.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                    buttOpen.doClick();
                }
            }
        });

        tableProduct.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                    buttOpen.doClick();
                }
            }
        });

        tableMaterial.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                    buttOpen.doClick();
                }
            }
        });

        tableIzd.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                    buttOpen.doClick();
                }
            }
        });

        renderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                try {
                    switch (Integer.valueOf(table.getValueAt(row, table.getColumnCount() - 1).toString())) {
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

        buttPrint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttPrintActionPerformed(evt);
            }
        });

        colMove.add("");
        colMove.add("№");
        colMove.add("Дата");
        colMove.add("idТип");
        colMove.add("Тип");
        colMove.add("Код ТМЦ");
        colMove.add("Название");
        colMove.add("Артикул");
        colMove.add("Шифр");
        colMove.add("Вид");
        colMove.add("Пр-во");
        colMove.add("Ед.изм.");
        colMove.add("Кол-во Ф");
        colMove.add("Кол-во K");
        colMove.add("Куски");
        colMove.add("Дата цена");
        colMove.add("Цена");
        colMove.add("Сумма");
        colMove.add("idЦех.");
        colMove.add("Цех");
        colMove.add("idРаб");
        colMove.add("Работник");
        colMove.add("Документ");
        colMove.add("Примечание");
        colMove.add("Дата ввода");
        colMove.add("Автор ввода");
        colMove.add("Дата корр.");
        colMove.add("Автор корр.");
        colMove.add("К/р");
        colMove.add("Статус");
        colMove.add("idStatus");

        mapSettingMove.put("", "true");
        mapSettingMove.put("№", "true");
        mapSettingMove.put("Дата", "true");
        mapSettingMove.put("Тип", "true");
        mapSettingMove.put("Артикул", "true");
        mapSettingMove.put("Кол-во K", "true");
        mapSettingMove.put("Цех", "true");
        mapSettingMove.put("Работник", "true");
        mapSettingMove.put("К/р", "true");
        mapSettingMove.put("Дата корр.", "true");
        mapSettingMove.put("Статус", "true");

        colProduct.add("");
        colProduct.add("№");
        colProduct.add("Дата");
        colProduct.add("Модель");
        colProduct.add("Название");
        colProduct.add("Артикул");
        colProduct.add("idВид");
        colProduct.add("Вид");
        colProduct.add("Кол-во");
        colProduct.add("Шкала");
        colProduct.add("Примечание");
        colProduct.add("idЦех");
        colProduct.add("Цех");
        colProduct.add("idАвтор");
        colProduct.add("Автор");
        colProduct.add("Дата ввода");
        colProduct.add("Автор ввода");
        colProduct.add("Дата корр.");
        colProduct.add("Автор корр.");
        colProduct.add("Статус");
        colProduct.add("idStatus");

        mapSettingProduct.put("", "true");
        mapSettingProduct.put("№", "true");
        mapSettingProduct.put("Дата", "true");
        mapSettingProduct.put("Модель", "true");
        mapSettingProduct.put("Название", "true");
        mapSettingProduct.put("Кол-во", "true");
        mapSettingProduct.put("Цех", "true");
        mapSettingProduct.put("Автор", "true");
        mapSettingProduct.put("Дата корр.", "true");
        mapSettingProduct.put("Статус", "true");

        colMaterial.add("");
        colMaterial.add("№");
        colMaterial.add("№ К/р");
        colMaterial.add("Дата");
        colMaterial.add("Модель");
        colMaterial.add("Код ТМЦ");
        colMaterial.add("Название");
        colMaterial.add("Артикул");
        colMaterial.add("Шифр");
        colMaterial.add("Вид");
        colMaterial.add("Пр-во");
        colMaterial.add("Ед.изм.");
        colMaterial.add("Кол-во");
        colMaterial.add("Куски");
        colMaterial.add("Отходы");
        colMaterial.add("Дата цена");
        colMaterial.add("Цена");
        colMaterial.add("Сумма");
        colMaterial.add("idDept");
        colMaterial.add("Цех");
        colMaterial.add("idРаб");
        colMaterial.add("Работник");
        colMaterial.add("Дата корр.");

        mapSettingMaterial.put("", "true");
        mapSettingMaterial.put("№", "true");
        mapSettingMaterial.put("№ К/р", "true");
        mapSettingMaterial.put("Дата", "true");
        mapSettingMaterial.put("Модель", "true");
        mapSettingMaterial.put("Артикул", "true");
        mapSettingMaterial.put("Кол-во", "true");
        mapSettingMaterial.put("Отходы", "true");
        mapSettingMaterial.put("Цех", "true");
        mapSettingMaterial.put("Работник", "true");
        mapSettingMaterial.put("Дата корр.", "true");

        colIzd.add("");
        colIzd.add("№ Сдачи");
        colIzd.add("Дата");
        colIzd.add("Документ");
        colIzd.add("idЦех");
        colIzd.add("Цех");
        colIzd.add("idРаботника");
        colIzd.add("Работник");
        colIzd.add("№ К/р");
        colIzd.add("Модель");
        colIzd.add("Название");
        colIzd.add("Артикул");
        colIzd.add("idВид");
        colIzd.add("Вид");
        colIzd.add("Кол-во");
        colIzd.add("Шкала");
        colIzd.add("Примечание");
        colIzd.add("Дата ввода");
        colIzd.add("Автор ввода");
        colIzd.add("Дата корр.");
        colIzd.add("Автор корр.");
        colIzd.add("Статус");
        colIzd.add("idStatus");

        mapSettingIzd.put("", "true");
        mapSettingIzd.put("№ Сдачи", "true");
        mapSettingIzd.put("Дата", "true");
        mapSettingIzd.put("Документ", "true");
        mapSettingIzd.put("Модель", "true");
        mapSettingIzd.put("Кол-во", "true");
        mapSettingIzd.put("Статус", "true");

        buttonGroup.add(jRadioButton1);
        buttonGroup.add(jRadioButton2);
        buttonGroup.add(jRadioButton3);
        buttonGroup.add(jRadioButton4);

        searchButtPanel.add(buttSearch);

        searchPanel.add(checkboxSt, ParagraphLayout.NEW_PARAGRAPH);
        searchPanel.add(new JLabel(" с "));
        searchPanel.add(sStDate);
        searchPanel.add(new JLabel(" по "));
        searchPanel.add(eStDate);
        searchPanel.add(new JLabel("    Статус:"));
        searchPanel.add(jRadioButton1);
        searchPanel.add(jRadioButton2);
        searchPanel.add(jRadioButton3);
        searchPanel.add(jRadioButton4);
        searchPanel.add(checkboxIns, ParagraphLayout.NEW_PARAGRAPH);
        searchPanel.add(new JLabel(" с "));
        searchPanel.add(sInsDate);
        searchPanel.add(new JLabel(" по "));
        searchPanel.add(eInsDate);
        searchPanel.add(searchButtPanel);

        buttPanel.add(buttOpen);
        buttPanel.add(buttEdit);
        buttPanel.add(buttCopy);
        buttPanel.add(buttAdd);
        buttPanel.add(new JLabel());
        buttPanel.add(buttPrint);

        tableMovePanel.add(new JScrollPane(tableMove), BorderLayout.CENTER);
        tableMovePanel.add(filterMoveRow, BorderLayout.SOUTH);

        tableProductPanel.add(new JScrollPane(tableProduct), BorderLayout.CENTER);
        tableProductPanel.add(filterProductRow, BorderLayout.SOUTH);

        tableMaterialPanel.add(new JScrollPane(tableMaterial), BorderLayout.CENTER);
        tableMaterialPanel.add(filterMaterialRow, BorderLayout.SOUTH);

        tableIzdPanel.add(new JScrollPane(tableIzd), BorderLayout.CENTER);
        tableIzdPanel.add(filterIzdRow, BorderLayout.SOUTH);

        tableTabPane.addTab("Движение ТМЦ", tableMovePanel);
        tableTabPane.addTab("Карты раскроя", tableProductPanel);
        tableTabPane.addTab("ТМЦ в К/р", tableMaterialPanel);
        tableTabPane.addTab("Сдача на склад", tableIzdPanel);

        osnova.add(searchPanel, BorderLayout.NORTH);
        osnova.add(tableTabPane, BorderLayout.CENTER);
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

    private void createColTable(JTable table, HashMap<String, String> mapSetting, String setting) {
        try {
            final HashMap<String, String> tempMap = new HashMap<String, String>();

            panelCheckBox.removeAll();

            for (int i = 2; i < table.getColumnCount(); i++) {
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
                    UtilFunctions.setSettingPropFile(rezaltPrint, setting);

                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "Ошибка! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ошибка. " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buttAddActionPerformed(ActionEvent evt) {
        try {
            if (tableTabPane.getModel().getSelectedIndex() == 0) {
                new SkladMoveTMCItemForm(controller, true);

                if (UtilSkladHO.BUTT_ACTION_EDIT)
                    createSkladMoveTable(getDataTable(UtilSkladHO.DATA_MOVE));

            } else if (tableTabPane.getModel().getSelectedIndex() == 1 || tableTabPane.getModel().getSelectedIndex() == 2) {
                new SkladProductItemForm(controller, true);

                if (UtilSkladHO.BUTT_ACTION_EDIT) {
                    createSkladMoveTable(getDataTable(UtilSkladHO.DATA_MOVE));
                    createSkladProductTable(getDataTable(UtilSkladHO.DATA_PRODUCT));
                    createSkladMaterialTable(getDataTable(UtilSkladHO.DATA_MATERIAL));
                }
            } else if (tableTabPane.getModel().getSelectedIndex() == 3) {
                new SkladStorageItemForm(controller, true);

                if (UtilSkladHO.BUTT_ACTION_EDIT)
                    createSkladIzdTable(getDataTable(UtilSkladHO.DATA_IZD));

            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ошибка " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buttEditActionPerformed(ActionEvent evt) {
        try {
            if (tableTabPane.getModel().getSelectedIndex() == 0) {
                if (tableMove.getSelectedRow() != -1) {
                    try {
                        new SkladMoveTMCItemForm(controller, true,
                                UtilSkladHO.getItemTable(tableMove),
                                UtilSkladHO.TYPE_EDIT);

                        if (UtilSkladHO.BUTT_ACTION_EDIT)
                            createSkladMoveTable(getDataTable(UtilSkladHO.DATA_MOVE));

                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(null, "Ошибка " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Вы не выбрали запись для изменения!", "Внимание", javax.swing.JOptionPane.WARNING_MESSAGE);
                }
            } else if (tableTabPane.getModel().getSelectedIndex() == 1) {
                if (tableProduct.getSelectedRow() != -1) {

                    new SkladProductItemForm(controller, true,
                            UtilSkladHO.getItemTable(tableProduct),
                            jMenuItem1.isVisible(),
                            UtilSkladHO.TYPE_EDIT_RETURN);

                    if (UtilSkladHO.BUTT_ACTION_EDIT)
                        buttSearch.doClick();
                } else {
                    JOptionPane.showMessageDialog(null, "Вы не выбрали запись для изменения!", "Внимание", javax.swing.JOptionPane.WARNING_MESSAGE);
                }

            } else if (tableTabPane.getModel().getSelectedIndex() == 2) {
                if (tableMaterial.getSelectedRow() != -1) {

                    Vector data = new Vector();
                    try {
                        spdb = new SkladHOPDB();

                        data = spdb.getDataProductSklad(Integer.valueOf(tableMaterial.getValueAt(tableMaterial.getSelectedRow(), 2).toString()));

                    } catch (Exception e) {
                        data = new Vector();
                    } finally {
                        spdb.disConn();
                    }

                    new SkladProductItemForm(controller, true,
                            data,
                            jMenuItem1.isVisible(),
                            UtilSkladHO.TYPE_EDIT_RETURN);

                    if (UtilSkladHO.BUTT_ACTION_EDIT)
                        buttSearch.doClick();

                } else {
                    JOptionPane.showMessageDialog(null, "Вы не выбрали запись для изменения!", "Внимание", javax.swing.JOptionPane.WARNING_MESSAGE);
                }
            } else if (tableTabPane.getModel().getSelectedIndex() == 3) {
                if (tableIzd.getSelectedRow() != -1) {

                    new SkladStorageItemForm(controller, true,
                            UtilSkladHO.getItemTable(tableIzd),
                            jMenuItem1.isVisible(),
                            UtilSkladHO.TYPE_EDIT);

                    if (UtilSkladHO.BUTT_ACTION_EDIT)
                        buttSearch.doClick();
                } else {
                    JOptionPane.showMessageDialog(null, "Вы не выбрали запись для изменения!", "Внимание", javax.swing.JOptionPane.WARNING_MESSAGE);
                }

            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ошибка " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buttCopyActionPerformed(ActionEvent evt) {
        try {
            if (tableTabPane.getModel().getSelectedIndex() == 0) {
                if (tableMove.getSelectedRow() != -1) {
                    try {
                        new SkladMoveTMCItemForm(controller, true, UtilSkladHO.getItemTable(tableMove));

                        if (UtilSkladHO.BUTT_ACTION_EDIT)
                            createSkladMoveTable(getDataTable(UtilSkladHO.DATA_MOVE));

                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(null, "Ошибка " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Вы не выбрали запись для изменения!", "Внимание", javax.swing.JOptionPane.WARNING_MESSAGE);
                }
            } else if (tableTabPane.getModel().getSelectedIndex() == 1) {
                JOptionPane.showMessageDialog(null, "Копировать можно только движение ТМЦ!", "Внимание", javax.swing.JOptionPane.INFORMATION_MESSAGE);

            } else if (tableTabPane.getModel().getSelectedIndex() == 2) {
                JOptionPane.showMessageDialog(null, "Копировать можно только движение ТМЦ!", "Внимание", javax.swing.JOptionPane.INFORMATION_MESSAGE);

            } else if (tableTabPane.getModel().getSelectedIndex() == 3) {
                JOptionPane.showMessageDialog(null, "Копировать можно только движение ТМЦ!", "Внимание", javax.swing.JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ошибка " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buttOpenActionPerformed(ActionEvent evt) {
        try {
            if (tableTabPane.getModel().getSelectedIndex() == 0) {
                if (tableMove.getSelectedRow() != -1) {

                    new SkladMoveTMCForm(controller, true, UtilSkladHO.getItemTable(tableMove));

                } else
                    JOptionPane.showMessageDialog(null, "Вы не выбрали запись!", "Внимание", javax.swing.JOptionPane.WARNING_MESSAGE);

            } else if (tableTabPane.getModel().getSelectedIndex() == 1) {
                if (tableProduct.getSelectedRow() != -1) {

                    new SkladProductItemForm(controller, true,
                            UtilSkladHO.getItemTable(tableProduct),
                            UtilSkladHO.TYPE_OPEN);

                } else {
                    JOptionPane.showMessageDialog(null, "Вы не выбрали запись!", "Внимание", javax.swing.JOptionPane.WARNING_MESSAGE);
                }

            } else if (tableTabPane.getModel().getSelectedIndex() == 2) {
                if (tableMaterial.getSelectedRow() != -1) {

                    Vector data = new Vector();
                    try {
                        spdb = new SkladHOPDB();

                        data = spdb.getDataProductSklad(Integer.valueOf(tableMaterial.getValueAt(tableMaterial.getSelectedRow(), 2).toString()));

                    } catch (Exception e) {
                        data = new Vector();
                    } finally {
                        spdb.disConn();
                    }

                    new SkladProductItemForm(controller, true,
                            data,
                            UtilSkladHO.TYPE_OPEN);

                } else {
                    JOptionPane.showMessageDialog(null, "Вы не выбрали запись!", "Внимание", javax.swing.JOptionPane.WARNING_MESSAGE);
                }
            } else if (tableTabPane.getModel().getSelectedIndex() == 3) {
                if (tableIzd.getSelectedRow() != -1) {

                    new SkladStorageItemForm(controller, true,
                            UtilSkladHO.getItemTable(tableIzd),
                            UtilSkladHO.TYPE_OPEN);

                } else {
                    JOptionPane.showMessageDialog(null, "Вы не выбрали запись!", "Внимание", javax.swing.JOptionPane.WARNING_MESSAGE);
                }

            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ошибка " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buttSearchActionPerformed(ActionEvent evt) {
        try {
            createSkladMoveTable(getDataTable(UtilSkladHO.DATA_MOVE));
            createSkladProductTable(getDataTable(UtilSkladHO.DATA_PRODUCT));
            createSkladMaterialTable(getDataTable(UtilSkladHO.DATA_MATERIAL));
            createSkladIzdTable(getDataTable(UtilSkladHO.DATA_IZD));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ошибка " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buttPrintActionPerformed(ActionEvent evt) {
        try {
            String str = tableTabPane.getTitleAt(tableTabPane.getSelectedIndex()) +
                    (checkboxSt.isSelected() ? " период с" + new SimpleDateFormat("dd.MM.yyyy").format(sStDate.getDate()) +
                            " по " + new SimpleDateFormat("dd.MM.yyyy").format(eStDate.getDate()) + ";" : "") +
                    (checkboxIns.isSelected() ? " редакт. с" + new SimpleDateFormat("dd.MM.yyyy").format(sInsDate.getDate()) +
                            " по " + new SimpleDateFormat("dd.MM.yyyy").format(eInsDate.getDate()) + ";" : "");

            if (tableTabPane.getModel().getSelectedIndex() == 0) {
                SkladHOOO oo = new SkladHOOO(str, tModelMove, tableMove.getColumnModel());
                oo.createReport("DefaultTableAlbumFormatCheck.ots");
            } else if (tableTabPane.getModel().getSelectedIndex() == 1) {
                SkladHOOO oo = new SkladHOOO(str, tModelProduct, tableProduct.getColumnModel());
                oo.createReport("DefaultTableAlbumFormatCheck.ots");
            } else if (tableTabPane.getModel().getSelectedIndex() == 2) {
                SkladHOOO oo = new SkladHOOO(str, tModelMaterial, tableMaterial.getColumnModel());
                oo.createReport("DefaultTableAlbumFormatCheck.ots");
            } else if (tableTabPane.getModel().getSelectedIndex() == 3) {
                SkladHOOO oo = new SkladHOOO(str, tModelIzd, tableIzd.getColumnModel());
                oo.createReport("DefaultTableAlbumFormatCheck.ots");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Ошибка ", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void initData() {
        try {
            UtilSkladHO.DEPT_SELECT_ITEM = UtilFunctions.readPropFile(UtilSkladHO.SETTING_DEPT_SELECT_ITEM);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ошибка! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }

        try {
            UtilSkladHO.TIP_MOVE_SELECT_ITEM = UtilFunctions.readPropFile(UtilSkladHO.SETTING_TIP_MOVE_SELECT_ITEM);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ошибка! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }

        try {
            double mf = UtilFunctions.readPropFileString(UtilSkladHO.SETTING_MOISTURE).equals("") ? UtilSkladHO.ACTUAL_MOISTURE : Double.valueOf(UtilFunctions.readPropFileString(UtilSkladHO.SETTING_MOISTURE));
            //double mk = UtilFunctions.readPropFileString(UtilSkladHO.SETTING_MOISTURE_K).equals("")?UtilSkladHO.ACTUAL_MOISTURE_K:Double.valueOf(UtilFunctions.readPropFileString(UtilSkladHO.SETTING_MOISTURE_K));

            UtilSkladHO.ACTUAL_MOISTURE = mf;
            //UtilSkladHO.ACTUAL_MOISTURE_K = mk;            

        } catch (Exception e) {
            UtilSkladHO.ACTUAL_MOISTURE = 1;
            //UtilSkladHO.ACTUAL_MOISTURE_K = 1;

            JOptionPane.showMessageDialog(null, "Ошибка! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }

        try {
            spdb = new SkladHOPDB();
            Vector data = spdb.getDept();

            UtilSkladHO.DEPT_MODEL = new Vector();
            UtilSkladHO.DEPT_MODEL.add(new Item(-1, "-", ""));
            UtilFunctions.fullModel(UtilSkladHO.DEPT_MODEL, data);
            UtilSkladHO.DEPT_MODEL.remove(1);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Список подразделений не загружен! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        } finally {
            spdb.disConn();
        }

        try {
            UtilSkladHO.TIP_MOVE_MODEL = new Vector();
            UtilSkladHO.TIP_MOVE_MODEL.add(new Item(1, UtilSkladHO.TYPE_MOVE_PR, "1"));
            UtilSkladHO.TIP_MOVE_MODEL.add(new Item(2, UtilSkladHO.TYPE_MOVE_RS, "2"));
            UtilSkladHO.TIP_MOVE_MODEL.add(new Item(3, UtilSkladHO.TYPE_MOVE_VZ, "3"));
            UtilSkladHO.TIP_MOVE_MODEL.add(new Item(4, UtilSkladHO.TYPE_MOVE_AK, "4"));

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Список подразделений не загружен! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void initMenu() {
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem8 = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenuItem14 = new javax.swing.JMenuItem();
        jMenuItem20 = new javax.swing.JMenuItem();
        jMenu3 = new javax.swing.JMenu();
        jMenuItem3 = new javax.swing.JMenuItem();
        jMenuItem5 = new javax.swing.JMenuItem();
        jMenuItem19 = new javax.swing.JMenuItem();
        jMenuItem15 = new javax.swing.JMenuItem();
        jMenu4 = new javax.swing.JMenu();
        jMenuItem16 = new javax.swing.JMenuItem();
        jMenuItem4 = new javax.swing.JMenuItem();
        jMenuItem7 = new javax.swing.JMenuItem();
        jMenuItem10 = new javax.swing.JMenuItem();
        jMenuItem9 = new javax.swing.JMenuItem();
        jMenuItem17 = new javax.swing.JMenuItem();
        jMenuItem21 = new javax.swing.JMenuItem();
        jMenu5 = new javax.swing.JMenu();
        jMenu6 = new javax.swing.JMenu();
        jMenuItem12 = new javax.swing.JMenuItem();
        jMenuItem13 = new javax.swing.JMenuItem();
        jMenuItem11 = new javax.swing.JMenuItem();
        jMenuItem18 = new javax.swing.JMenuItem();
        jMenuItem6 = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jMenu1.setText("Правка");

        jMenuItem1.setText("Удалить");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem1);

        jMenuItem8.setText("Закрытые остатки");
        jMenuItem8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem8ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem8);

        jMenuBar1.add(jMenu1);

        jMenu3.setText("Создать");

        jMenuItem3.setText("Новое движение ТМЦ");
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem3);

        jMenuItem5.setText("Новая карта раскроя");
        jMenuItem5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem5ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem5);

        jMenuItem19.setText("Новая сдача на склад");
        jMenuItem19.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem19ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem19);

        jMenuItem15.setText("Закрыть период");
        jMenuItem15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem15ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem15);

        jMenuBar1.add(jMenu3);

        jMenu2.setText("Просмотр");

        jMenuItem2.setText("Остатки в кладовой");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem2);

        jMenuItem14.setText("Остатки в ХЭО/Внедр.");
        jMenuItem14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem14ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem14);

        jMenuItem20.setText("Остатки моделей в кладовой ");
        jMenuItem20.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem20ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem20);

        jMenuBar1.add(jMenu2);

        jMenu4.setText("Документы");

        jMenuItem16.setText("Накладная на выдачу");
        jMenuItem16.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem16ActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItem16);

        jMenuItem4.setText("Карта раскроя");
        jMenuItem4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem4ActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItem4);

        jMenuItem7.setText("Движение фабричного полотна");
        jMenuItem7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem7ActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItem7);

        jMenuItem10.setText("Движение импортного полотна");
        jMenuItem10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem10ActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItem10);

        jMenuItem9.setText("Движение всп. материалов");
        jMenuItem9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem9ActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItem9);

        jMenuItem17.setText("ПО по движению импортного полотна");
        jMenuItem17.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem17ActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItem17);

        jMenuItem21.setText("Инвентаризационная опись");
        jMenuItem21.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem21ActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItem21);

        jMenuBar1.add(jMenu4);

        jMenu5.setText("Параметры");

        jMenu6.setText("Столбцы таблиц");
        jMenu6.setToolTipText("");

        jMenuItem12.setText("Движение ТМЦ");
        jMenuItem12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem12ActionPerformed(evt);
            }
        });
        jMenu6.add(jMenuItem12);

        jMenuItem13.setText("Карты раскроя");
        jMenuItem13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem13ActionPerformed(evt);
            }
        });
        jMenu6.add(jMenuItem13);

        jMenuItem11.setText("ТМЦ в К/р");
        jMenuItem11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem11ActionPerformed(evt);
            }
        });
        jMenu6.add(jMenuItem11);

        jMenuItem18.setText("Сдача на склад");
        jMenuItem18.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem18ActionPerformed(evt);
            }
        });
        jMenu6.add(jMenuItem18);

        jMenu5.add(jMenu6);

        jMenuItem6.setText("Коэффициент кондиционной массы");
        jMenuItem6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem6ActionPerformed(evt);
            }
        });
        jMenu5.add(jMenuItem6);

        jMenuBar1.add(jMenu5);

        setJMenuBar(jMenuBar1);

        jMenu1.setVisible(false);
        jMenuItem1.setVisible(false);

        menu.put("1", jMenu1);              //Правка
        menu.put("1-1", jMenuItem1);            //Удалить
        menu.put("1-2", jMenuItem8);            //Закрытые остатки
    }
    /*
    private void setFooterTable() {
        try {
            double summ = 0;

            for (int i = 0; i < table.getRowCount(); i++) {
                summ += Double.valueOf(table.getValueAt(i, 11).toString());
            }    

            filterRow.removeAll();     
            for (int i = 0; i < table.getColumnModel().getColumnCount(); i++) {
                if (i == 12){
                    filterRow.add(new JTextField(String.valueOf(new DecimalFormat("###,###.##").format(summ)))); 
                } else{
                    filterRow.add(new JTextField()); 
                }    
            }

            TableColumnModel tcm = table.getColumnModel();
            if(filterRow.getComponents().length>0){
                for (int i = 0; i < tcm.getColumnCount(); i++) {
                    JTextField textField = (JTextField) filterRow.getComponent(i);
                    Dimension d = textField.getPreferredSize();
                    d.width = tcm.getColumn(i).getWidth();
                    textField.setPreferredSize(d);
                }
            }
            filterRow.revalidate();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Ошибка ", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }
    */

    private void createSkladMoveTable(final Vector data) {
        tModelMove = new DefaultTableModel(data, colMove) {
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

        tModelMove.addTableModelListener(new TableModelListener() {
            public void tableChanged(TableModelEvent e) {
                if (tableModelMoveListenerIsChanging) {
                    return;
                }
                int firstRow = e.getFirstRow();
                int column = e.getColumn();

                if (column != 0 || maxSelectedMoveRow == -1 || minSelectedMoveRow == -1) {
                    return;
                }
                tableModelMoveListenerIsChanging = true;
                boolean value = ((Boolean) tModelMove.getValueAt(firstRow, column)).booleanValue();
                for (int i = minSelectedMoveRow; i <= maxSelectedMoveRow; i++) {
                    tModelMove.setValueAt(Boolean.valueOf(value), tableMove.convertRowIndexToModel(i), column);
                }

                minSelectedMoveRow = -1;
                maxSelectedMoveRow = -1;

                tableModelMoveListenerIsChanging = false;
            }
        });

        tableMove.setModel(tModelMove);
        tableMove.setAutoCreateColumnsFromModel(true);

        try {
            UtilSkladHO.initColumTableMap(tableMove, mapSettingMove);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ошибка! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }

        sorterMove = new TableRowSorter<TableModel>(tModelMove);
        tableMove.setRowSorter(sorterMove);
        tableMove.getColumnModel().getColumn(0).setHeaderRenderer(new CheckBoxHeader(tableMove.getTableHeader(), 0, ""));

        tableMove.getColumnModel().getColumn(29).setCellRenderer(renderer);
    }

    private void createSkladProductTable(final Vector data) {
        tModelProduct = new DefaultTableModel(data, colProduct) {
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

        tModelProduct.addTableModelListener(new TableModelListener() {
            public void tableChanged(TableModelEvent e) {
                if (tableModelProductListenerIsChanging) {
                    return;
                }
                int firstRow = e.getFirstRow();
                int column = e.getColumn();

                if (column != 0 || maxSelectedProductRow == -1 || minSelectedProductRow == -1) {
                    return;
                }
                tableModelProductListenerIsChanging = true;
                boolean value = ((Boolean) tModelProduct.getValueAt(firstRow, column)).booleanValue();
                for (int i = minSelectedProductRow; i <= maxSelectedProductRow; i++) {
                    tModelProduct.setValueAt(Boolean.valueOf(value), tableProduct.convertRowIndexToModel(i), column);
                }

                minSelectedProductRow = -1;
                maxSelectedProductRow = -1;

                tableModelProductListenerIsChanging = false;
            }
        });

        tableProduct.setModel(tModelProduct);
        tableProduct.setAutoCreateColumnsFromModel(true);

        try {
            UtilSkladHO.initColumTableMap(tableProduct, mapSettingProduct);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ошибка! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }

        sorterProduct = new TableRowSorter<TableModel>(tModelProduct);
        tableProduct.setRowSorter(sorterProduct);
        tableProduct.getColumnModel().getColumn(0).setHeaderRenderer(new CheckBoxHeader(tableProduct.getTableHeader(), 0, ""));

        tableProduct.getColumnModel().getColumn(19).setCellRenderer(renderer);
    }


    private void createSkladMaterialTable(final Vector data) {
        tModelMaterial = new DefaultTableModel(data, colMaterial) {
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

        tModelMaterial.addTableModelListener(new TableModelListener() {
            public void tableChanged(TableModelEvent e) {
                if (tableModelMaterialListenerIsChanging) {
                    return;
                }
                int firstRow = e.getFirstRow();
                int column = e.getColumn();

                if (column != 0 || maxSelectedMaterialRow == -1 || minSelectedMaterialRow == -1) {
                    return;
                }
                tableModelMaterialListenerIsChanging = true;
                boolean value = ((Boolean) tModelMaterial.getValueAt(firstRow, column)).booleanValue();
                for (int i = minSelectedMaterialRow; i <= maxSelectedMaterialRow; i++) {
                    tModelMaterial.setValueAt(Boolean.valueOf(value), tableMaterial.convertRowIndexToModel(i), column);
                }

                minSelectedMaterialRow = -1;
                maxSelectedMaterialRow = -1;

                tableModelMaterialListenerIsChanging = false;
            }
        });

        tableMaterial.setModel(tModelMaterial);
        tableMaterial.setAutoCreateColumnsFromModel(true);

        try {
            UtilSkladHO.initColumTableMap(tableMaterial, mapSettingMaterial);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ошибка! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }

        sorterMaterial = new TableRowSorter<TableModel>(tModelMaterial);
        tableMaterial.setRowSorter(sorterMaterial);
        tableMaterial.getColumnModel().getColumn(0).setHeaderRenderer(new CheckBoxHeader(tableMaterial.getTableHeader(), 0, ""));

        //tableMaterial.getColumnModel().getColumn(18).setCellRenderer(renderer); 
    }

    private void createSkladIzdTable(final Vector data) {
        tModelIzd = new DefaultTableModel(data, colIzd) {
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

        tModelIzd.addTableModelListener(new TableModelListener() {
            public void tableChanged(TableModelEvent e) {
                if (tableModelIzdListenerIsChanging) {
                    return;
                }
                int firstRow = e.getFirstRow();
                int column = e.getColumn();

                if (column != 0 || maxSelectedIzdRow == -1 || minSelectedIzdRow == -1) {
                    return;
                }
                tableModelIzdListenerIsChanging = true;
                boolean value = ((Boolean) tModelIzd.getValueAt(firstRow, column)).booleanValue();
                for (int i = minSelectedIzdRow; i <= maxSelectedIzdRow; i++) {
                    tModelIzd.setValueAt(Boolean.valueOf(value), tableIzd.convertRowIndexToModel(i), column);
                }

                minSelectedIzdRow = -1;
                maxSelectedIzdRow = -1;

                tableModelIzdListenerIsChanging = false;
            }
        });

        tableIzd.setModel(tModelIzd);
        tableIzd.setAutoCreateColumnsFromModel(true);

        try {
            UtilSkladHO.initColumTableMap(tableIzd, mapSettingIzd);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ошибка! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }

        sorterIzd = new TableRowSorter<TableModel>(tModelIzd);
        tableIzd.setRowSorter(sorterIzd);
        tableIzd.getColumnModel().getColumn(0).setHeaderRenderer(new CheckBoxHeader(tableIzd.getTableHeader(), 0, ""));

        //tableIzd.getColumnModel().getColumn(18).setCellRenderer(renderer); 
    }

    private Vector getDataTable(final String type) {
        dataTable = new Vector();

        try {
            pb = new ProgressBar(this, false, "Сбор данных ...");
            SwingWorker sw = new SwingWorker() {
                protected Object doInBackground() {
                    try {
                        spdb = new SkladHOPDB();

                        if (type.equals(UtilSkladHO.DATA_MOVE)) {
                            pb.setMessage("Обновление таблицы движения ТМЦ ...");
                            dataTable = spdb.getDataMoveSklad(checkboxSt.isSelected(),
                                    UtilFunctions.convertDateStrToLong(new SimpleDateFormat("dd.MM.yyyy").format(sStDate.getDate())),
                                    UtilFunctions.convertDateStrToLong(new SimpleDateFormat("dd.MM.yyyy").format(eStDate.getDate())),
                                    checkboxIns.isSelected(),
                                    UtilFunctions.convertDateStrToLong(new SimpleDateFormat("dd.MM.yyyy").format(sInsDate.getDate())),
                                    UtilFunctions.convertDateStrToLong(new SimpleDateFormat("dd.MM.yyyy").format(eInsDate.getDate())),
                                    buttonGroup.getSelection().getActionCommand());
                        } else if (type.equals(UtilSkladHO.DATA_PRODUCT)) {
                            pb.setMessage("Обновление таблицы карт раскроя ...");
                            dataTable = spdb.getDataProductSklad(checkboxSt.isSelected(),
                                    UtilFunctions.convertDateStrToLong(new SimpleDateFormat("dd.MM.yyyy").format(sStDate.getDate())),
                                    UtilFunctions.convertDateStrToLong(new SimpleDateFormat("dd.MM.yyyy").format(eStDate.getDate())),
                                    checkboxIns.isSelected(),
                                    UtilFunctions.convertDateStrToLong(new SimpleDateFormat("dd.MM.yyyy").format(sInsDate.getDate())),
                                    UtilFunctions.convertDateStrToLong(new SimpleDateFormat("dd.MM.yyyy").format(eInsDate.getDate())),
                                    buttonGroup.getSelection().getActionCommand());

                        } else if (type.equals(UtilSkladHO.DATA_MATERIAL)) {
                            pb.setMessage("Обновление таблицы расхода материалов ...");
                            dataTable = spdb.getDataMaterialSklad(checkboxSt.isSelected(),
                                    UtilFunctions.convertDateStrToLong(new SimpleDateFormat("dd.MM.yyyy").format(sStDate.getDate())),
                                    UtilFunctions.convertDateStrToLong(new SimpleDateFormat("dd.MM.yyyy").format(eStDate.getDate())),
                                    checkboxIns.isSelected(),
                                    UtilFunctions.convertDateStrToLong(new SimpleDateFormat("dd.MM.yyyy").format(sInsDate.getDate())),
                                    UtilFunctions.convertDateStrToLong(new SimpleDateFormat("dd.MM.yyyy").format(eInsDate.getDate())),
                                    buttonGroup.getSelection().getActionCommand());

                        } else if (type.equals(UtilSkladHO.DATA_IZD)) {
                            pb.setMessage("Обновление таблицы сдача на склад ...");
                            dataTable = spdb.getDataIzdSklad(checkboxSt.isSelected(),
                                    UtilFunctions.convertDateStrToLong(new SimpleDateFormat("dd.MM.yyyy").format(sStDate.getDate())),
                                    UtilFunctions.convertDateStrToLong(new SimpleDateFormat("dd.MM.yyyy").format(eStDate.getDate())),
                                    checkboxIns.isSelected(),
                                    UtilFunctions.convertDateStrToLong(new SimpleDateFormat("dd.MM.yyyy").format(sInsDate.getDate())),
                                    UtilFunctions.convertDateStrToLong(new SimpleDateFormat("dd.MM.yyyy").format(eInsDate.getDate())),
                                    buttonGroup.getSelection().getActionCommand());

                        }
                    } catch (Exception e) {
                        dataTable = new Vector();
                        JOptionPane.showMessageDialog(null, "Сбой обновления! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
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
            JOptionPane.showMessageDialog(null, "Ошибка! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
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
