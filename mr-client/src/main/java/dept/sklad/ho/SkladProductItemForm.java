package dept.sklad.ho;

import by.march8.ecs.MainController;
import com.jhlabs.awt.ParagraphLayout;
import com.toedter.calendar.JDateChooser;
import common.CheckBoxHeader;
import common.Item;
import common.ProgressBar;
import common.User;
import common.UtilFunctions;
import dept.sprav.employe.EmployeForm;
import dept.sprav.employe.UtilEmploye;
import dept.sprav.model.ModelForm;
import dept.sprav.model.UtilModel;
import net.coderazzi.filters.gui.AutoChoices;
import net.coderazzi.filters.gui.TableFilterHeader;

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
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

/**
 *
 * @author lidashka
 */
@SuppressWarnings("all")
public class SkladProductItemForm extends javax.swing.JDialog {
    public SkladHOPDB spdb;
    int minSelectedMoveRow = -1;
    int maxSelectedMoveRow = -1;
    boolean tableModelMoveListenerIsChanging = false;
    int minSelectedReturnRow = -1;
    int maxSelectedReturnRow = -1;
    boolean tableModelReturnListenerIsChanging = false;
    private User user = User.getInstance();
    private JPanel osnova;
    private JPanel returnPanel;
    private JPanel buttPanel;
    private JPanel buttEastTab1Panel;
    private JPanel buttEastTab2Panel;
    private JPanel materialPanel;
    private JPanel rightPanel;
    private JPanel prodPanel;
    private JPanel upPanel;
    private JPanel downPanel;
    private JPanel panelCheckBox;
    private JLabel vvodEmpl;
    private JLabel vvodDate;
    private JLabel insEmpl;
    private JLabel insDate;
    private JLabel numEmpl;
    private JLabel emplName;
    private JLabel numTmc;
    private JComboBox dept;
    private JTextField numFas;
    private JTextField nameFas;
    private JTextField narFas;
    private JTextField shkala;
    private JTextField kolvo;
    private JTextField part;
    private JButton buttSave;
    private JButton buttClose;
    private JButton buttFas;
    private JButton buttTab1Plus;
    private JButton buttTab1Minus;
    private JButton buttTab1TMC;
    private JButton buttTab1Move;
    private JButton buttTab1Open;
    private JButton buttTab2Plus;
    private JButton buttTab2Minus;
    private JButton buttTab2Edit;
    private JButton buttEmpl;
    private JDateChooser stDate;
    private JTextPane note;
    private int idProduct;
    private ButtonGroup buttonGroup;
    private JRadioButton jRadioButton2;
    private JRadioButton jRadioButton3;
    private JRadioButton jRadioButton4;
    private ButtonGroup buttonGroupFasVid;
    private JRadioButton jRadioButtonf0;
    private JRadioButton jRadioButtonf1;
    private JRadioButton jRadioButtonf2;
    private JRadioButton jRadioButtonf3;
    private JTabbedPane tableTabPane;
    private JTable tableMaterial;
    private JTable tableReturn;
    private Vector colMaterial;
    private Vector colReturn;
    private DefaultTableModel tModelMaterial;
    private DefaultTableModel tModelMove;
    private TableRowSorter<TableModel> sorterMove;
    private TableRowSorter<TableModel> sorterReturn;
    private TableFilterHeader filterHeaderMove;
    private TableFilterHeader filterHeaderProduct;
    private DefaultTableCellRenderer renderer;
    private HashMap<String, String> mapSettingTmc;
    private HashMap<String, String> mapSettingReturn;
    private boolean EDIT;
    private boolean LOOK;
    private String TYPE;
    private boolean ACCESS;
    private ProgressBar pb;
    private Vector dataTable;
    private JButton buttTab1Move_;
    private JButton buttPrint;
    private MainController controller;
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;

    public SkladProductItemForm(MainController mainController, boolean modal) {
        super(mainController.getMainForm(), modal);
        controller = mainController;
        setTitle("Новая карта раскроя");
        setPreferredSize(new Dimension(900, 360));

        TYPE = UtilSkladHO.TYPE_ADD_RETURN;

        init();

        EDIT = true;

        stDate.setDate((Calendar.getInstance()).getTime());
        try {
            if (!UtilSkladHO.DATE_VVOD.equals(""))
                if ((new SimpleDateFormat("MM.yyyy").format(stDate.getDate())).equals(UtilSkladHO.DATE_VVOD.substring(3)))
                    stDate.setDate(new SimpleDateFormat("dd.MM.yyyy").parse(UtilSkladHO.DATE_VVOD));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ошибка! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }

        try {
            readSettings();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ошибка! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }

        try {
            pb = new ProgressBar(SkladProductItemForm.this, false, "Сбор данных ...");
            SwingWorker sw = new SwingWorker() {
                protected Object doInBackground() {
                    try {
                        spdb = new SkladHOPDB();
                        LOOK = spdb.createTempSkladMoveTable(idProduct);

                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(null, "Данные проекта плана не загружены! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
                    }
                    return null;
                }

                protected void done() {
                    pb.dispose();
                }
            };
            sw.execute();
            pb.setVisible(true);

            if (LOOK) {
                createMaterialTable(new Vector());
                createMoveTable(new Vector());

            } else {
                throw new Exception("Ошибка при формировании временной таблицы! ");
            }

            setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
            addWindowListener(new WindowListener() {
                public void windowClosed(WindowEvent e) {
                }

                public void windowClosing(WindowEvent e) {
                    buttClose.doClick();
                }

                public void windowOpened(WindowEvent e) {
                }

                public void windowIconified(WindowEvent e) {
                }

                public void windowDeiconified(WindowEvent e) {
                }

                public void windowActivated(WindowEvent e) {
                }

                public void windowDeactivated(WindowEvent e) {
                }
            });
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ошибка при формировании карты раскроя! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
            dispose();
        }

        setLocationRelativeTo(controller.getMainForm());
        setVisible(true);
    }

    public SkladProductItemForm(MainController mainController, boolean modal, Vector dataEdit, boolean flagAccess, String type) {
        super(mainController.getMainForm(), modal);
        controller = mainController;
        setTitle("Карта раскроя №" + dataEdit.get(1).toString());
        setPreferredSize(new Dimension(900, 430));

        TYPE = type;

        init();

        idProduct = Integer.valueOf(dataEdit.get(1).toString());
        EDIT = true;
        ACCESS = flagAccess;

        prodPanel.add(new JLabel("Дата ввода:"), ParagraphLayout.NEW_PARAGRAPH);
        prodPanel.add(vvodDate);
        prodPanel.add(new JLabel("    Автор:"));
        prodPanel.add(vvodEmpl);
        prodPanel.add(new JLabel("Дата корр.:"), ParagraphLayout.NEW_PARAGRAPH);
        prodPanel.add(insDate);
        prodPanel.add(new JLabel("    Автор:"));
        prodPanel.add(insEmpl);

        stDate.setDate((Calendar.getInstance()).getTime());
        try {
            if (!UtilSkladHO.DATE_VVOD.equals(""))
                if ((new SimpleDateFormat("MM.yyyy").format(stDate.getDate())).equals(UtilSkladHO.DATE_VVOD.substring(3)))
                    stDate.setDate(new SimpleDateFormat("dd.MM.yyyy").parse(UtilSkladHO.DATE_VVOD));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ошибка! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }

        try {
            readSettings();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ошибка! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }

        try {
            pb = new ProgressBar(SkladProductItemForm.this, false, "Сбор данных ...");
            SwingWorker sw = new SwingWorker() {
                protected Object doInBackground() {
                    try {
                        spdb = new SkladHOPDB();
                        LOOK = spdb.createTempSkladMoveTable(idProduct);

                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(null, "Данные проекта плана не загружены! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
                    }
                    return null;
                }

                protected void done() {
                    pb.dispose();
                }
            };
            sw.execute();
            pb.setVisible(true);

            if (LOOK) {
                createMaterialTable(getDataTable(UtilSkladHO.DATA_MATERIAL));
                createMoveTable(getDataTable(UtilSkladHO.DATA_RETURN));

            } else {
                createMaterialTable(new Vector());
                createMoveTable(new Vector());
                throw new Exception("Ошибка при формировании временной таблицы! ");
            }

            setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
            addWindowListener(new WindowListener() {
                public void windowClosed(WindowEvent e) {
                }

                public void windowClosing(WindowEvent e) {
                    buttClose.doClick();
                }

                public void windowOpened(WindowEvent e) {
                }

                public void windowIconified(WindowEvent e) {
                }

                public void windowDeiconified(WindowEvent e) {
                }

                public void windowActivated(WindowEvent e) {
                }

                public void windowDeactivated(WindowEvent e) {
                }
            });
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ошибка при формировании карты раскроя! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
            dispose();
        }

        try {

            if (!ACCESS && Integer.valueOf(dataEdit.get(18).toString()) == 0) {
                ACCESS = true;
            }

            stDate.setDate(new SimpleDateFormat("dd-MM-yyyy").parse(dataEdit.get(2).toString()));
            numFas.setText(dataEdit.get(3).toString());
            nameFas.setText(dataEdit.get(4).toString());
            narFas.setText(dataEdit.get(5).toString());
            switch (Integer.valueOf(dataEdit.get(6).toString())) {
                case 0:
                    jRadioButtonf0.setSelected(true);
                    break;
                case 1:
                    jRadioButtonf1.setSelected(true);
                    break;
                case 2:
                    jRadioButtonf2.setSelected(true);
                    break;
                case 3:
                    jRadioButtonf3.setSelected(true);
                    break;
                default:
                    jRadioButtonf0.setSelected(true);
                    break;
            }
            kolvo.setText(dataEdit.get(8).toString());
            shkala.setText(dataEdit.get(9).toString());
            note.setText(dataEdit.get(10).toString());
            dept.setSelectedItem(UtilFunctions.getItemsModel(UtilSkladHO.DEPT_MODEL, Integer.valueOf(dataEdit.get(11).toString())));
            numEmpl.setText(dataEdit.get(13).toString());
            emplName.setText(dataEdit.get(14).toString());
            vvodDate.setText(dataEdit.get(15).toString());
            vvodEmpl.setText(dataEdit.get(16).toString());
            insDate.setText(dataEdit.get(17).toString());
            insEmpl.setText(dataEdit.get(18).toString());

            switch (Integer.valueOf(dataEdit.get(20).toString())) {
                case 0:
                    jRadioButton2.setSelected(true);
                    break;
                case 1:
                    jRadioButton3.setSelected(true);
                    break;
                case -1:
                    jRadioButton4.setSelected(true);
                    break;
                default:
                    jRadioButton3.setSelected(true);
                    break;
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ошибка! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
            dispose();
        }

        setLocationRelativeTo(controller.getMainForm());
        setVisible(true);
    }
    public SkladProductItemForm(MainController mainController, boolean modal, Vector dataEdit, String type) {
        super(mainController.getMainForm(), modal);
        controller = mainController;
        setTitle("Карта раскроя №" + dataEdit.get(1).toString());
        setPreferredSize(new Dimension(800, 430));

        TYPE = type;

        init();

        idProduct = Integer.valueOf(dataEdit.get(1).toString());
        EDIT = false;

        prodPanel.add(new JLabel("Дата ввода:"), ParagraphLayout.NEW_PARAGRAPH);
        prodPanel.add(vvodDate);
        prodPanel.add(new JLabel("    Автор:"));
        prodPanel.add(vvodEmpl);
        prodPanel.add(new JLabel("Дата корр.:"), ParagraphLayout.NEW_PARAGRAPH);
        prodPanel.add(insDate);
        prodPanel.add(new JLabel("    Автор:"));
        prodPanel.add(insEmpl);

        buttEastTab1Panel.setVisible(false);
        buttEastTab2Panel.setVisible(false);
        buttSave.setVisible(false);

        buttEmpl.setEnabled(false);
        buttFas.setEnabled(false);
        dept.setEnabled(false);
        stDate.setEnabled(false);
        kolvo.setEnabled(false);
        shkala.setEnabled(false);
        note.setEnabled(false);
        numFas.setEnabled(false);
        nameFas.setEnabled(false);
        nameFas.setEnabled(false);
        jRadioButton2.setEnabled(false);
        jRadioButton3.setEnabled(false);
        jRadioButton4.setEnabled(false);
        jRadioButtonf0.setEnabled(false);
        jRadioButtonf1.setEnabled(false);
        jRadioButtonf2.setEnabled(false);
        jRadioButtonf3.setEnabled(false);
        tableReturn.setEnabled(false);
        tableMaterial.setEnabled(false);

        stDate.setDate((Calendar.getInstance()).getTime());
        try {
            if (!UtilSkladHO.DATE_VVOD.equals(""))
                if ((new SimpleDateFormat("MM.yyyy").format(stDate.getDate())).equals(UtilSkladHO.DATE_VVOD.substring(3)))
                    stDate.setDate(new SimpleDateFormat("dd.MM.yyyy").parse(UtilSkladHO.DATE_VVOD));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ошибка! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }

        try {
            readSettings();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ошибка! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }

        try {
            stDate.setDate(new SimpleDateFormat("dd-MM-yyyy").parse(dataEdit.get(2).toString()));
            numFas.setText(dataEdit.get(3).toString());
            nameFas.setText(dataEdit.get(4).toString());
            narFas.setText(dataEdit.get(5).toString());
            switch (Integer.valueOf(dataEdit.get(6).toString())) {
                case 0:
                    jRadioButtonf0.setSelected(true);
                    break;
                case 1:
                    jRadioButtonf1.setSelected(true);
                    break;
                case 2:
                    jRadioButtonf2.setSelected(true);
                    break;
                case 3:
                    jRadioButtonf3.setSelected(true);
                    break;
                default:
                    jRadioButtonf0.setSelected(true);
                    break;
            }
            kolvo.setText(dataEdit.get(8).toString());
            shkala.setText(dataEdit.get(9).toString());
            note.setText(dataEdit.get(10).toString());
            dept.setSelectedItem(UtilFunctions.getItemsModel(UtilSkladHO.DEPT_MODEL, Integer.valueOf(dataEdit.get(11).toString())));
            numEmpl.setText(dataEdit.get(13).toString());
            emplName.setText(dataEdit.get(14).toString());
            vvodDate.setText(dataEdit.get(15).toString());
            vvodEmpl.setText(dataEdit.get(16).toString());
            insDate.setText(dataEdit.get(17).toString());
            insEmpl.setText(dataEdit.get(18).toString());

            switch (Integer.valueOf(dataEdit.get(20).toString())) {
                case 0:
                    jRadioButton2.setSelected(true);
                    break;
                case 1:
                    jRadioButton3.setSelected(true);
                    break;
                case -1:
                    jRadioButton4.setSelected(true);
                    break;
                default:
                    jRadioButton3.setSelected(true);
                    break;
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ошибка! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
            dispose();
        }

        createMaterialTable(getDataTable(UtilSkladHO.DATA_MATERIAL));
        createMoveTable(getDataTable(UtilSkladHO.DATA_RETURN));

        setLocationRelativeTo(controller.getMainForm());
        setVisible(true);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenu3 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jMenu1.setText("Параметры");

        jMenu3.setText("Столбцы таблиц");

        jMenuItem1.setText("ТМЦ");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem1);

        jMenuItem2.setText("Движение");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem2);

        jMenu1.add(jMenu3);

        jMenuBar1.add(jMenu1);

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
        try {
            createColTable(tableReturn, mapSettingReturn, UtilSkladHO.SETTING_MAP_PRODUCT_MOVE);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Ошибка ", javax.swing.JOptionPane.ERROR_MESSAGE);
        } finally {
            createMoveTable(tModelMove.getDataVector());
        }
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        try {
            createColTable(tableMaterial, mapSettingTmc, UtilSkladHO.SETTING_MAP_PRODUCT_TMC);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Ошибка ", javax.swing.JOptionPane.ERROR_MESSAGE);
        } finally {
            createMaterialTable(tModelMaterial.getDataVector());
        }
    }//GEN-LAST:event_jMenuItem1ActionPerformed
    // End of variables declaration//GEN-END:variables

    private void init() {
        UtilSkladHO.BUTT_ACTION_EDIT = false;
        EDIT = false;
        LOOK = false;
        ACCESS = false;
        idProduct = -1;

        setMinimumSize(new Dimension(900, 650));

        initMenu();

        osnova = new JPanel();
        buttPanel = new JPanel();
        upPanel = new JPanel();
        downPanel = new JPanel();
        returnPanel = new JPanel();
        materialPanel = new JPanel();
        rightPanel = new JPanel();
        prodPanel = new JPanel();
        buttEastTab1Panel = new JPanel();
        buttEastTab2Panel = new JPanel();
        panelCheckBox = new JPanel();

        tableTabPane = new JTabbedPane();
        numFas = new JTextField();
        nameFas = new JTextField();
        narFas = new JTextField();
        numTmc = new JLabel();
        numEmpl = new JLabel();
        emplName = new JLabel();
        tableMaterial = new JTable();
        tableReturn = new JTable();
        filterHeaderMove = new TableFilterHeader(tableMaterial, AutoChoices.ENABLED);
        filterHeaderProduct = new TableFilterHeader(tableReturn, AutoChoices.ENABLED);
        colMaterial = new Vector();
        colReturn = new Vector();
        kolvo = new JTextField("0");
        part = new JTextField("0");
        shkala = new JTextField();
        note = new JTextPane();
        dept = new JComboBox(UtilSkladHO.DEPT_MODEL);
        stDate = new JDateChooser();
        buttPrint = new JButton("Печать");
        buttFas = new JButton("Модель");
        buttEmpl = new JButton("Автор");
        buttSave = new JButton("Сохранить");
        buttClose = new JButton("Закрыть");
        buttTab1Plus = new JButton("+");
        buttTab1Minus = new JButton("-");
        buttTab1TMC = new JButton("ТМЦ");
        buttTab1Move = new JButton("Список");
        buttTab1Open = new JButton("Детали");
        buttTab1Move_ = new JButton("Движение");
        buttTab2Plus = new JButton("+");
        buttTab2Minus = new JButton("-");
        buttTab2Edit = new JButton("Изм.");
        buttonGroup = new ButtonGroup();
        buttonGroupFasVid = new ButtonGroup();
        jRadioButton2 = new JRadioButton();
        jRadioButton3 = new JRadioButton();
        jRadioButton4 = new JRadioButton();
        jRadioButtonf0 = new JRadioButton();
        jRadioButtonf1 = new JRadioButton();
        jRadioButtonf2 = new JRadioButton();
        jRadioButtonf3 = new JRadioButton();
        vvodEmpl = new JLabel();
        vvodDate = new JLabel();
        insEmpl = new JLabel();
        insDate = new JLabel();
        mapSettingTmc = new HashMap<String, String>();
        mapSettingReturn = new HashMap<String, String>();

        osnova.setLayout(new BorderLayout(1, 1));
        osnova.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        returnPanel.setLayout(new BorderLayout(1, 1));
        returnPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        upPanel.setLayout(new ParagraphLayout());
        downPanel.setLayout(new ParagraphLayout());
        rightPanel.setLayout(new ParagraphLayout());
        prodPanel.setLayout(new ParagraphLayout());
        materialPanel.setLayout(new BorderLayout(1, 1));
        materialPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        buttPanel.setLayout(new GridLayout(0, 4, 5, 5));
        buttPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        buttPanel.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        dept.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 15));
        buttEastTab1Panel.setLayout(new GridLayout(7, 0, 5, 5));
        buttEastTab1Panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        buttEastTab2Panel.setLayout(new GridLayout(7, 0, 5, 5));
        buttEastTab2Panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        panelCheckBox.setLayout(new GridLayout(0, 3, 5, 5));

        dept.setPreferredSize(new Dimension(310, 20));
        numFas.setPreferredSize(new Dimension(70, 20));
        nameFas.setPreferredSize(new Dimension(350, 20));
        narFas.setPreferredSize(new Dimension(100, 20));
        numTmc.setPreferredSize(new Dimension(80, 20));
        emplName.setPreferredSize(new Dimension(250, 20));
        numEmpl.setPreferredSize(new Dimension(80, 20));
        stDate.setPreferredSize(new Dimension(120, 20));
        note.setPreferredSize(new Dimension(250, 40));
        vvodEmpl.setPreferredSize(new Dimension(240, 20));
        vvodDate.setPreferredSize(new Dimension(120, 20));
        insEmpl.setPreferredSize(new Dimension(240, 20));
        insDate.setPreferredSize(new Dimension(120, 20));
        kolvo.setPreferredSize(new Dimension(250, 20));
        part.setPreferredSize(new Dimension(150, 20));
        shkala.setPreferredSize(new Dimension(300, 20));

        numTmc.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        nameFas.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        numFas.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        narFas.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        numEmpl.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        emplName.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        note.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        vvodEmpl.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        vvodDate.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        insEmpl.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        insDate.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        jRadioButton2.setFont(new java.awt.Font("Dialog", 0, 13));
        jRadioButton3.setFont(new java.awt.Font("Dialog", 0, 13));
        jRadioButton4.setFont(new java.awt.Font("Dialog", 0, 13));
        jRadioButtonf0.setFont(new java.awt.Font("Dialog", 0, 13));
        jRadioButtonf1.setFont(new java.awt.Font("Dialog", 0, 13));
        jRadioButtonf2.setFont(new java.awt.Font("Dialog", 0, 13));
        jRadioButtonf3.setFont(new java.awt.Font("Dialog", 0, 13));

        jRadioButton2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jRadioButton3.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jRadioButton4.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jRadioButtonf0.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jRadioButtonf1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jRadioButtonf2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jRadioButtonf3.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        jRadioButton2.setText("формируется;");
        jRadioButton3.setText("закрыт;");
        jRadioButton4.setText("удалён;");
        jRadioButtonf0.setText("нет");
        jRadioButtonf1.setText("муж.");
        jRadioButtonf2.setText("жен.");
        jRadioButtonf3.setText("дет.");

        jRadioButton2.setActionCommand("0");
        jRadioButton3.setActionCommand("1");
        jRadioButton4.setActionCommand("-1");
        jRadioButtonf0.setActionCommand("0");
        jRadioButtonf1.setActionCommand("1");
        jRadioButtonf2.setActionCommand("2");
        jRadioButtonf3.setActionCommand("3");

        jRadioButton2.setSelected(true);
        jRadioButtonf0.setSelected(true);

        tableMaterial.setAutoCreateColumnsFromModel(true);
        tableReturn.setAutoCreateColumnsFromModel(true);
        tableMaterial.getTableHeader().setReorderingAllowed(false);
        tableReturn.getTableHeader().setReorderingAllowed(false);

        tableMaterial.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "key-enter");
        tableMaterial.getActionMap().put("key-enter", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                int colum = tableMaterial.getSelectedColumn();
                if (colum >= 2 && colum <= 8) {
                    buttTab1TMC.doClick();
                } else {
                    tableMaterial.editCellAt(tableMaterial.getSelectedRow(), tableMaterial.getSelectedColumn());
                    Component editor = tableMaterial.getEditorComponent();
                    if (editor != null) {
                        editor.requestFocus();
                        if (editor instanceof JTextField) {
                            ((JTextField) editor).selectAll();
                        }
                    }
                }
            }
        });

        tableMaterial.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
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

        tableReturn.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                if (e.getValueIsAdjusting()) {
                    return;
                }
                minSelectedReturnRow = (((DefaultListSelectionModel) e.getSource()).getMinSelectionIndex() == ((DefaultListSelectionModel) e.getSource()).getMaxSelectionIndex()) ?
                        -1 : ((DefaultListSelectionModel) e.getSource()).getMinSelectionIndex();
                maxSelectedReturnRow = (((DefaultListSelectionModel) e.getSource()).getMinSelectionIndex() == ((DefaultListSelectionModel) e.getSource()).getMaxSelectionIndex()) ?
                        -1 : ((DefaultListSelectionModel) e.getSource()).getMaxSelectionIndex();
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

        dept.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deptActionPerformed(evt);
            }
        });

        buttClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttCloseActionPerformed(evt);
            }
        });

        buttSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttSaveActionPerformed(evt);
            }
        });

        buttFas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttFasActionPerformed(evt);
            }
        });

        buttPrint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttPrintActionPerformed(evt);
            }
        });

        buttEmpl.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttEmplActionPerformed(evt);
            }
        });

        buttTab1Plus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttTab1PlusActionPerformed(evt);
            }
        });

        buttTab2Plus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttTab2PlusActionPerformed(evt);
            }
        });

        buttTab1Move.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttTab1MoveActionPerformed(evt);
            }
        });

        buttTab1Move_.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttTab2PlusActionPerformed(evt);
            }
        });

        buttTab1Minus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttTab1MinusActionPerformed(evt);
            }
        });

        buttTab2Minus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttTab2MinusActionPerformed(evt);
            }
        });

        buttTab1TMC.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttTab1TMCActionPerformed(evt);
            }
        });

        buttTab2Edit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttTab2EditActionPerformed(evt);
            }
        });

        colMaterial.add("");
        colMaterial.add("№");
        colMaterial.add("idТmc");
        colMaterial.add("Название");
        colMaterial.add("Артикул");
        colMaterial.add("Шифр");
        colMaterial.add("Вид");
        colMaterial.add("Пр-во");
        colMaterial.add("Ед.изм.");
        colMaterial.add("Кол. Ф");
        colMaterial.add("Кол. К");
        colMaterial.add("Куски");
        colMaterial.add("Отходы");
        colMaterial.add("Движение по складу");
        colMaterial.add("Док.");
        colMaterial.add("Прим.");

        mapSettingTmc.put("", "true");
        mapSettingTmc.put("№", "true");
        mapSettingTmc.put("Название", "true");
        mapSettingTmc.put("Артикул", "true");
        mapSettingTmc.put("Кол. Ф", "true");
        mapSettingTmc.put("Кол. К", "true");
        mapSettingTmc.put("Куски", "true");
        mapSettingTmc.put("Отходы", "true");

        colReturn.add("");
        colReturn.add("№");
        colReturn.add("Дата");
        colReturn.add("idТип");
        colReturn.add("Тип");
        colReturn.add("idТmc");
        colReturn.add("Название");
        colReturn.add("Артикул");
        colReturn.add("Шифр");
        colReturn.add("Вид");
        colReturn.add("Пр-во");
        colReturn.add("Ед.изм.");
        colReturn.add("Кол-во Ф");
        colReturn.add("Кол-во K");
        colReturn.add("Куски");
        colReturn.add("Дата цена");
        colReturn.add("Цена");
        colReturn.add("Сумма");
        colReturn.add("idЦех.");
        colReturn.add("Цех");
        colReturn.add("idРаб");
        colReturn.add("Работник");
        colReturn.add("Док.");
        colReturn.add("Прим.");
        colReturn.add("Дата ввода");
        colReturn.add("Автор ввода");
        colReturn.add("Дата корр.");
        colReturn.add("Автор корр.");
        colReturn.add("К/р");
        colReturn.add("Статус");
        colReturn.add("idStatus");

        mapSettingReturn.put("", "true");
        mapSettingReturn.put("Дата", "true");
        mapSettingReturn.put("Тип", "true");
        mapSettingReturn.put("Название", "true");
        mapSettingReturn.put("Артикул", "true");
        mapSettingReturn.put("Кол-во Ф", "true");
        mapSettingReturn.put("Кол-во K", "true");
        mapSettingReturn.put("Куски", "true");
        mapSettingReturn.put("Статус", "true");

        buttonGroup.add(jRadioButton2);
        buttonGroup.add(jRadioButton3);
        buttonGroup.add(jRadioButton4);

        buttonGroupFasVid.add(jRadioButtonf0);
        buttonGroupFasVid.add(jRadioButtonf1);
        buttonGroupFasVid.add(jRadioButtonf2);
        buttonGroupFasVid.add(jRadioButtonf3);

        upPanel.add(new JLabel("Дата :"), ParagraphLayout.NEW_PARAGRAPH);
        upPanel.add(stDate);
        upPanel.add(new JLabel("    Статус:"));
        upPanel.add(jRadioButton2);
        upPanel.add(jRadioButton3);
        upPanel.add(jRadioButton4);
        upPanel.add(new JLabel("Цех:"), ParagraphLayout.NEW_PARAGRAPH);
        upPanel.add(dept);
        upPanel.add(buttEmpl);
        upPanel.add(numEmpl);
        upPanel.add(emplName);

        prodPanel.add(buttFas, ParagraphLayout.NEW_PARAGRAPH);
        prodPanel.add(numFas);
        prodPanel.add(nameFas);
        prodPanel.add(new JLabel("  Артикул:"));
        prodPanel.add(narFas);
        prodPanel.add(new JLabel("  Вид:"), ParagraphLayout.NEW_PARAGRAPH);
        prodPanel.add(jRadioButtonf0);
        prodPanel.add(jRadioButtonf1);
        prodPanel.add(jRadioButtonf2);
        prodPanel.add(jRadioButtonf3);
        prodPanel.add(new JLabel("Кол-во:"), ParagraphLayout.NEW_PARAGRAPH);
        prodPanel.add(kolvo);
        prodPanel.add(new JLabel("шт."));
        prodPanel.add(new JLabel("Размер:"), ParagraphLayout.NEW_PARAGRAPH);
        prodPanel.add(shkala);
        prodPanel.add(new JLabel("Примечание :"), ParagraphLayout.NEW_PARAGRAPH);
        prodPanel.add(note, ParagraphLayout.NEW_LINE_STRETCH_H);

        buttEastTab1Panel.add(buttTab1Plus);
        buttEastTab1Panel.add(buttTab1Minus);
        buttEastTab1Panel.add(buttTab1TMC);
        buttEastTab1Panel.add(buttTab1Move_);
        buttEastTab1Panel.add(buttTab1Move);
        //    buttEastTab1Panel.add(buttTab1Open);

        buttEastTab2Panel.add(buttTab2Plus);
        buttEastTab2Panel.add(buttTab2Minus);
        buttEastTab2Panel.add(buttTab2Edit);

        materialPanel.add(new JScrollPane(tableMaterial), BorderLayout.CENTER);
        materialPanel.add(buttEastTab1Panel, BorderLayout.EAST);

        returnPanel.add(new JScrollPane(tableReturn), BorderLayout.CENTER);
        returnPanel.add(buttEastTab2Panel, BorderLayout.EAST);

        buttPanel.add(buttClose);
        buttPanel.add(buttSave);
        buttPanel.add(new JLabel());
        buttPanel.add(buttPrint);

        tableTabPane.addTab("Изделие", prodPanel);
        tableTabPane.addTab("ТМЦ", materialPanel);
        tableTabPane.addTab("Движение", returnPanel);

        osnova.add(upPanel, BorderLayout.NORTH);
        osnova.add(tableTabPane, BorderLayout.CENTER);
        osnova.add(buttPanel, BorderLayout.SOUTH);

        getContentPane().add(osnova);
        pack();
    }


    private void initMenu() {
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenu3 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jMenu1.setText("Параметры");

        jMenu3.setText("Столбцы таблиц");

        jMenuItem1.setText("ТМЦ");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem1);

        jMenuItem2.setText("Движение");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem2);

        jMenu1.add(jMenu3);

        jMenuBar1.add(jMenu1);

        setJMenuBar(jMenuBar1);
    }

    private void deptActionPerformed(ActionEvent evt) {
        try {
            UtilSkladHO.DEPT_SELECT_ITEM = ((Item) dept.getSelectedItem()).getId();
            UtilFunctions.setSettingPropFile(String.valueOf(((Item) dept.getSelectedItem()).getId()), UtilSkladHO.SETTING_DEPT_SELECT_ITEM);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ошибка! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buttCloseActionPerformed(ActionEvent evt) {
        try {
            if (TYPE.equals(UtilSkladHO.TYPE_OPEN)) {
                dispose();
            } else {
                if (JOptionPane.showOptionDialog(null, "Сохранить изменения?", "Сохранение...", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, new Object[]{"Да", "Нет"}, "Нет") == JOptionPane.YES_OPTION)
                    buttSave.doClick();
                else {
                    spdb.disConn();
                    dispose();
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ошибка! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buttSaveActionPerformed(ActionEvent evt) {
        try {
            if (EDIT) {
                boolean saveFlag = true;
                String str = "";

                try {
                    if (UtilFunctions.checkDate(new SimpleDateFormat("dd.MM.yyyy").format(stDate.getDate()))) {
                        UtilSkladHO.DATE_VVOD = new SimpleDateFormat("dd.MM.yyyy").format(stDate.getDate());
                        UtilFunctions.setSettingPropFile(new SimpleDateFormat("dd.MM.yyyy").format(stDate.getDate()), UtilSkladHO.SETTING_DATE_VVOD);
                    }
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "Ошибка сохранения в файл настроек даты ввода! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
                }

                try {
                    if (!UtilFunctions.checkDate(new SimpleDateFormat("dd.MM.yyyy").format(stDate.getDate()))) {
                        saveFlag = false;
                    }
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "Ошибка. " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
                }

                try {
                    Integer.valueOf(((Item) dept.getSelectedItem()).getId());
                } catch (Exception e) {
                    saveFlag = false;
                    str += "Цех задан некорректно!\n";
                }

                try {
                    if (numEmpl.getText().trim().equals("") || numEmpl.getText().trim().equals("-1")) {
                        saveFlag = false;
                        str += "Вы не выбрали работника!\n";
                    } else
                        Integer.valueOf(numEmpl.getText().trim());
                } catch (Exception e) {
                    saveFlag = false;
                    str += "Код работника задан некорректно!\n";
                }

                try {
                    if (numFas.getText().trim().equals("") || numFas.getText().trim().equals("-1")) {
                        saveFlag = false;
                        str += "Вы не выбрали модель!\n";
                    } else
                        Integer.valueOf(numFas.getText().trim());
                } catch (Exception e) {
                    saveFlag = false;
                    str += "Модель задана некорректно!\n";
                }

                try {
                    if (!kolvo.getText().trim().equals(""))
                        Double.valueOf(kolvo.getText().trim().replace(",", "."));
                    else {
                        saveFlag = false;
                        str += "Кол-во задано некорректно!\n";
                    }
                } catch (Exception e) {
                    saveFlag = false;
                    str += "Кол-во задано некорректно!\n";
                }

                if (tModelMaterial.getRowCount() < 1) {
                    saveFlag = false;
                    str += "Вы не заполнили таблицу материалов!\n";
                }

                for (Iterator it = tModelMaterial.getDataVector().iterator(); it.hasNext(); ) {
                    Vector vrow = (Vector) it.next();
                    if (Integer.valueOf(vrow.get(2).toString()) == -1) {
                        saveFlag = false;
                        str += "В таблице материалов не заполнены столбецы ТМЦ!\n";
                        break;
                    }
                }

                if (!saveFlag) {
                    JOptionPane.showMessageDialog(null, str, "Внимание", javax.swing.JOptionPane.WARNING_MESSAGE);
                }

                if (saveFlag) {
                    if (TYPE.equals(UtilSkladHO.TYPE_ADD_RETURN)) {
                        if (spdb.addSkladProduct(
                                UtilFunctions.convertDateStrToLong(new SimpleDateFormat("dd.MM.yyyy").format(stDate.getDate())),
                                Integer.valueOf(buttonGroup.getSelection().getActionCommand()),
                                Integer.valueOf(((Item) dept.getSelectedItem()).getId()),
                                Integer.valueOf(numEmpl.getText().trim()),
                                Integer.valueOf(numFas.getText().trim()),
                                nameFas.getText().trim().toUpperCase(),
                                narFas.getText().trim().toUpperCase(),
                                Integer.valueOf(buttonGroupFasVid.getSelection().getActionCommand()),
                                Integer.valueOf(kolvo.getText().trim()),
                                shkala.getText().trim().toLowerCase(),
                                note.getText().trim().toLowerCase(),
                                tModelMaterial.getDataVector(),
                                Integer.valueOf(user.getIdEmployee()))) {
                            JOptionPane.showMessageDialog(null, "Запись успешно добавлена! ", "Завершено", javax.swing.JOptionPane.INFORMATION_MESSAGE);

                            UtilSkladHO.BUTT_ACTION_EDIT = true;

                            dispose();
                        }
                    } else if (TYPE.equals(UtilSkladHO.TYPE_EDIT_RETURN)) {
                        if (spdb.editSkladProduct(
                                idProduct,
                                UtilFunctions.convertDateStrToLong(new SimpleDateFormat("dd.MM.yyyy").format(stDate.getDate())),
                                Integer.valueOf(buttonGroup.getSelection().getActionCommand()),
                                Integer.valueOf(((Item) dept.getSelectedItem()).getId()),
                                Integer.valueOf(numEmpl.getText().trim()),
                                Integer.valueOf(numFas.getText().trim()),
                                nameFas.getText().trim().toUpperCase(),
                                narFas.getText().trim().toUpperCase(),
                                Integer.valueOf(buttonGroupFasVid.getSelection().getActionCommand()),
                                Integer.valueOf(kolvo.getText().trim()),
                                shkala.getText().trim().toLowerCase(),
                                note.getText().trim().toLowerCase(),
                                tModelMaterial.getDataVector(),
                                Integer.valueOf(user.getIdEmployee()))) {
                            JOptionPane.showMessageDialog(null, "Запись успешно добавлена! ", "Завершено", javax.swing.JOptionPane.INFORMATION_MESSAGE);

                            UtilSkladHO.BUTT_ACTION_EDIT = true;

                            dispose();
                        }
                    }
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ошибка. " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buttEmplActionPerformed(ActionEvent evt) {
        try {
            new EmployeForm(controller, true, ((Item) dept.getSelectedItem()).getId(), "");

            numEmpl.setText(UtilEmploye.NUM);
            emplName.setText(UtilEmploye.NAIM);

        } catch (Exception e) {
            numEmpl.setText("");
            emplName.setText("");

            JOptionPane.showMessageDialog(null, "Ошибка. " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buttFasActionPerformed(ActionEvent evt) {
        try {
            new ModelForm(controller, true, true);

            numFas.setText(UtilModel.MODEL);
            nameFas.setText(UtilModel.NAIM);

        } catch (Exception e) {
            numFas.setText("");
            nameFas.setText("");

            JOptionPane.showMessageDialog(null, "Ошибка. " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buttPrintActionPerformed(ActionEvent evt) {
        try {
            Vector tempOtchet = new Vector();
            Vector tempFasOtchet = new Vector();
            String str = "";

            if (UtilFunctions.checkDate(new SimpleDateFormat("dd.MM.yyyy").format(stDate.getDate()))) {

                tempOtchet.add(new SimpleDateFormat("dd-MM-yyyy").format(stDate.getDate()));
                tempOtchet.add(((Item) dept.getSelectedItem()).getDescription());
                tempOtchet.add(emplName.getText());
                tempOtchet.add(idProduct == -1 ? "" : String.valueOf(idProduct));

                tempFasOtchet.add(numFas.getText().trim().toLowerCase());
                tempFasOtchet.add(nameFas.getText().trim().toLowerCase());

                if (jRadioButtonf0.isSelected())
                    str = jRadioButtonf0.getText();
                else if (jRadioButtonf1.isSelected())
                    str = jRadioButtonf1.getText();
                else if (jRadioButtonf2.isSelected())
                    str = jRadioButtonf2.getText();
                else if (jRadioButtonf3.isSelected())
                    str = jRadioButtonf3.getText();

                tempFasOtchet.add(str);
                tempFasOtchet.add(shkala.getText().trim().toLowerCase());
                tempFasOtchet.add(Integer.valueOf(kolvo.getText()));

                SkladHOOO oo = new SkladHOOO("",
                        tempOtchet,
                        tempFasOtchet,
                        tModelMaterial.getDataVector(),
                        tModelMove.getDataVector(),
                        1);

                oo.createReport(UtilSkladHO.OTCHET_MAP);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ошибка. " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buttTab1PlusActionPerformed(ActionEvent evt) {
        try {
            Vector data = new Vector();

            data.add(false);
            data.add(0);
            data.add(new Integer(-1));
            data.add("");
            data.add("");
            data.add(0);
            data.add("");
            data.add("");
            data.add("");
            data.add(new Double(0));
            data.add(new Double(0));
            data.add(new Integer(0));
            data.add(new Double(0));
            data.add(new Integer(-1));
            data.add("");
            data.add("");
            tModelMaterial.insertRow(tableMaterial.getSelectedRow() != -1 ? tableMaterial.getSelectedRow() : tableMaterial.getRowCount(), data);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ошибка. " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buttTab1MoveActionPerformed(ActionEvent evt) {
        try {
            String str = "";
            Vector dataTMC = new Vector();
            ItemTMC itemTmc = new ItemTMC();

            for (int i = 0; i < tableMaterial.getRowCount(); i++) {

                if ((Boolean) tableMaterial.getValueAt(i, 0)) {

                    if (Integer.valueOf(tableMaterial.getValueAt(i, 2).toString()) > -1) {
                        itemTmc = new ItemTMC(
                                Integer.valueOf(tableMaterial.getValueAt(i, 2).toString()),
                                tableMaterial.getValueAt(i, 3).toString(),
                                tableMaterial.getValueAt(i, 4).toString(),
                                tableMaterial.getValueAt(i, 6).toString(),
                                tableMaterial.getValueAt(i, 8).toString(),
                                Double.valueOf(UtilFunctions.formatNorm(
                                        Double.valueOf(tableMaterial.getValueAt(i, 9).toString()) + Double.valueOf(tableMaterial.getValueAt(i, 12).toString()),
                                        3)),
                                Integer.valueOf(tableMaterial.getValueAt(i, 11).toString()));

                        dataTMC.add(itemTmc);

                    } else
                        tableMaterial.setValueAt(false, i, 0);
                }
            }

            if (dataTMC.size() > 0) {

                if (numEmpl.getText().trim().equals("") || numEmpl.getText().trim() == null)
                    str += "Вы не выбрали автора карты раскроя.\n";

                if ((((Item) dept.getSelectedItem()).getId()) == -1)
                    str += "Вы не выбрали цех.\n";

                if (!str.equals(""))
                    JOptionPane.showMessageDialog(null,
                            str,
                            "Внимание",
                            javax.swing.JOptionPane.WARNING_MESSAGE);

                if (tableTabPane.getModel().getSelectedIndex() == 1) {

                    new SkladMoveTMCItemForm(this, true,
                            new SimpleDateFormat("dd.MM.yyyy").format(stDate.getDate()),
                            Integer.valueOf(((Item) dept.getSelectedItem()).getId()) == -1 ? null : Integer.valueOf(((Item) dept.getSelectedItem()).getId()),
                            numEmpl.getText().trim(),
                            emplName.getText().trim(),
                            buttonGroup.getSelection().getActionCommand(),
                            dataTMC);

                }

                if (UtilSkladHO.BUTT_ACTION_EDIT)
                    createMoveTable(getDataTable(UtilSkladHO.DATA_RETURN));

            } else
                JOptionPane.showMessageDialog(null,
                        "Вы не выбрали запись в таблице материалов.\n",
                        "Внимание",
                        javax.swing.JOptionPane.WARNING_MESSAGE);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Ошибка. " + e.getMessage(),
                    "Ошибка",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buttTab2PlusActionPerformed(ActionEvent evt) {
        try {
            String str = "";

            if (numEmpl.getText().trim().equals("") || numEmpl.getText().trim() == null)
                str += "Вы не выбрали автора карты раскроя.\n";

            if ((((Item) dept.getSelectedItem()).getId()) == -1)
                str += "Вы не выбрали цех.\n";

            if (!str.equals(""))
                JOptionPane.showMessageDialog(null, str, "Внимание", javax.swing.JOptionPane.WARNING_MESSAGE);

            if (tableTabPane.getModel().getSelectedIndex() == 1) {

                if (tableMaterial.getSelectedRowCount() > 0) {

                    new SkladMoveTMCItemForm(this, true,
                            new SimpleDateFormat("dd.MM.yyyy").format(stDate.getDate()),
                            Integer.valueOf(tableMaterial.getValueAt(tableMaterial.getSelectedRow(), 2).toString()),
                            tableMaterial.getValueAt(tableMaterial.getSelectedRow(), 3).toString(),
                            tableMaterial.getValueAt(tableMaterial.getSelectedRow(), 4).toString(),
                            tableMaterial.getValueAt(tableMaterial.getSelectedRow(), 6).toString(),
                            tableMaterial.getValueAt(tableMaterial.getSelectedRow(), 8).toString(),
                            Integer.valueOf(((Item) dept.getSelectedItem()).getId()) == -1 ? null : Integer.valueOf(((Item) dept.getSelectedItem()).getId()),
                            numEmpl.getText().trim(),
                            emplName.getText().trim(),
                            buttonGroup.getSelection().getActionCommand());

                } else
                    JOptionPane.showMessageDialog(null,
                            "Вы не выбрали запись в таблице материалов.\n",
                            "Внимание",
                            javax.swing.JOptionPane.WARNING_MESSAGE);

            } else if (tableTabPane.getModel().getSelectedIndex() == 2) {

                new SkladMoveTMCItemForm(this, true,
                        new SimpleDateFormat("dd.MM.yyyy").format(stDate.getDate()),
                        -1, "", "", "", "",
                        Integer.valueOf(((Item) dept.getSelectedItem()).getId()) == -1 ? null : Integer.valueOf(((Item) dept.getSelectedItem()).getId()),
                        numEmpl.getText().trim(),
                        emplName.getText().trim(),
                        buttonGroup.getSelection().getActionCommand());
            }

            if (UtilSkladHO.BUTT_ACTION_EDIT)
                createMoveTable(getDataTable(UtilSkladHO.DATA_RETURN));

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ошибка. " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buttTab1MinusActionPerformed(ActionEvent evt) {
        try {
            if (JOptionPane.showOptionDialog(null, "Удалить отмеченные строки?", "Удаление ", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, new Object[]{"Удалить", "Отмена"}, "Удалить") == JOptionPane.YES_OPTION) {
                for (int i = 0; i < tModelMaterial.getDataVector().size(); i++) {
                    if (Boolean.valueOf(((Vector) tModelMaterial.getDataVector().get(i)).elementAt(0).toString())) {
                        tModelMaterial.getDataVector().remove(i);
                        i--;
                    }
                }

                createMaterialTable(tModelMaterial.getDataVector());
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ошибка. " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buttTab2MinusActionPerformed(ActionEvent evt) {
        try {
            boolean flagSelect = false;
            for (Object rows : tModelMove.getDataVector()) {
                if (((Vector) rows).get(0).toString().equals("true")) {
                    flagSelect = true;
                    break;
                }
            }

            if (flagSelect) {
                if (JOptionPane.showOptionDialog(null, "Удалить выделенные записи?", "Удаление", JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE, null, new Object[]{"Да", "Отмена"}, "Да") == JOptionPane.YES_OPTION) {

                    try {
                        if (TYPE.equals(UtilSkladHO.TYPE_ADD_RETURN) || TYPE.equals(UtilSkladHO.TYPE_EDIT_RETURN)) {
                            for (Object rows : tModelMove.getDataVector()) {
                                if (((Vector) rows).get(0).toString().equals("true"))
                                    if (!spdb.deleteMoveTMCTemp(Integer.valueOf(((Vector) rows).get(1).toString()))) {
                                        JOptionPane.showMessageDialog(null, "Сбой удаления! ", "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
                                        break;
                                    }
                            }
                        } else if (TYPE.equals(UtilSkladHO.TYPE_OPEN)) {
                        }
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(null, "Ошибка удаления! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
                    }

                    createMoveTable(getDataTable(UtilSkladHO.DATA_RETURN));
                }
            } else {
                JOptionPane.showMessageDialog(null, "Вы не выбрали записи для удаления!", "Внимание", javax.swing.JOptionPane.WARNING_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ошибка " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buttTab1TMCActionPerformed(ActionEvent evt) {
        try {
            if (tableMaterial.getSelectedRow() != -1) {

                new SpravTMCSkHOForm(controller, true, new SimpleDateFormat("dd-MM-yyyy").format(stDate.getDate()));

                if (UtilSkladHO.BUTT_ACTION_SELECT_SPRAV) {
                    tableMaterial.setValueAt(UtilSkladHO.SPRAV_TMC_ID, tableMaterial.getSelectedRow(), 2);
                    tableMaterial.setValueAt(UtilSkladHO.SPRAV_TMC_NAME.equals("") ? null : UtilSkladHO.SPRAV_TMC_NAME, tableMaterial.getSelectedRow(), 3);
                    tableMaterial.setValueAt(UtilSkladHO.SPRAV_TMC_NAR.equals("") ? null : UtilSkladHO.SPRAV_TMC_NAR, tableMaterial.getSelectedRow(), 4);
                    tableMaterial.setValueAt(UtilSkladHO.SPRAV_TMC_SAR, tableMaterial.getSelectedRow(), 5);
                    tableMaterial.setValueAt(UtilSkladHO.SPRAV_TMC_VID.equals("") ? null : UtilSkladHO.SPRAV_TMC_VID, tableMaterial.getSelectedRow(), 6);
                    tableMaterial.setValueAt(UtilSkladHO.SPRAV_TMC_MADE.equals("") ? null : UtilSkladHO.SPRAV_TMC_MADE, tableMaterial.getSelectedRow(), 7);
                    tableMaterial.setValueAt(UtilSkladHO.SPRAV_TMC_EDIZM.equals("") ? null : UtilSkladHO.SPRAV_TMC_EDIZM, tableMaterial.getSelectedRow(), 8);

                    tableMaterial.setValueAt(tableMaterial.getValueAt(tableMaterial.getSelectedRow(), 9), tableMaterial.getSelectedRow(), 9);
                }

            } else
                JOptionPane.showMessageDialog(null, "Вы не выбрали строку!", "Внимание", javax.swing.JOptionPane.WARNING_MESSAGE);

        } catch (Exception e) {
            tableMaterial.setValueAt(UtilSkladHO.SPRAV_TMC_ID, -1, 2);
            tableMaterial.setValueAt("", tableMaterial.getSelectedRow(), 3);
            tableMaterial.setValueAt("", tableMaterial.getSelectedRow(), 4);
            tableMaterial.setValueAt(0, tableMaterial.getSelectedRow(), 5);
            tableMaterial.setValueAt("", tableMaterial.getSelectedRow(), 6);
            tableMaterial.setValueAt("", tableMaterial.getSelectedRow(), 7);
            tableMaterial.setValueAt("", tableMaterial.getSelectedRow(), 8);

            JOptionPane.showMessageDialog(null, "Ошибка. " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buttTab2EditActionPerformed(ActionEvent evt) {
        try {
            if (tableReturn.getSelectedRow() != -1) {
                try {
                    new SkladMoveTMCItemForm(controller, true,
                            UtilSkladHO.getItemTable(tableReturn),
                            UtilSkladHO.TYPE_EDIT_RETURN);

                    if (UtilSkladHO.BUTT_ACTION_EDIT)
                        createMoveTable(getDataTable(UtilSkladHO.DATA_RETURN));

                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "Ошибка " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(null, "Вы не выбрали запись для изменения!", "Внимание", javax.swing.JOptionPane.WARNING_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ошибка " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void createMaterialTable(final Vector data) {
        tModelMaterial = new DefaultTableModel(data, colMaterial) {
            @Override
            public Class<?> getColumnClass(int col) {
                if (data.isEmpty())
                    return super.getClass();
                else
                    return (col == 0) ? getValueAt(0, col).getClass() : Object.class;
            }

            @Override
            public boolean isCellEditable(int row, int col) {
                return (col == 0 || col == 1 || col == 9 || col == 11 || col == 12 || col == 14 || col == 15) ? true : false;
            }

            @Override
            public void setValueAt(Object value, int row, int col) {
                try {
                    Vector rowVector = (Vector) dataVector.elementAt(row);

                    if (value != null) {
                        if (col == 9) {
                            rowVector.setElementAt(Double.valueOf(value.toString().trim().replace(",", ".")).doubleValue(), col);
                            rowVector.setElementAt(rowVector.get(6).toString().trim().toLowerCase().equals(UtilSkladHO.SKLAD_VID_2) ?
                                    Double.valueOf(value.toString().trim().replace(",", ".")).doubleValue() :
                                    UtilSkladHO.countKolvoK(Double.valueOf(value.toString().trim().replace(",", ".")).doubleValue()), 10);
                            fireTableCellUpdated(row, 10);
                        } else if (col == 11) {
                            rowVector.setElementAt(Integer.valueOf(value.toString().trim()).intValue(), col);
                        } else if (col == 12) {
                            rowVector.setElementAt(Double.valueOf(value.toString().trim().replace(",", ".")).doubleValue(), col);
                        } else
                            rowVector.setElementAt(value, col);
                    } else
                        rowVector.setElementAt(value, col);

                    fireTableCellUpdated(row, col);
                } catch (Exception ee) {
                    JOptionPane.showMessageDialog(null, "Введено некорректное значение: " + ee.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
                }
            }
        };

        tModelMaterial.addTableModelListener(new TableModelListener() {
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
                boolean value = ((Boolean) tModelMaterial.getValueAt(firstRow, column)).booleanValue();
                for (int i = minSelectedMoveRow; i <= maxSelectedMoveRow; i++) {
                    tModelMaterial.setValueAt(Boolean.valueOf(value), tableMaterial.convertRowIndexToModel(i), column);
                }

                minSelectedMoveRow = -1;
                maxSelectedMoveRow = -1;

                tableModelMoveListenerIsChanging = false;
            }
        });

        tableMaterial.setModel(tModelMaterial);
        tableMaterial.setAutoCreateColumnsFromModel(true);

        try {
            UtilSkladHO.initColumTableMap(tableMaterial, mapSettingTmc);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ошибка! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }

        sorterMove = new TableRowSorter<TableModel>(tModelMaterial);
        tableMaterial.setRowSorter(sorterMove);
        tableMaterial.getColumnModel().getColumn(0).setHeaderRenderer(new CheckBoxHeader(tableMaterial.getTableHeader(), 0, ""));

    }

    private void createMoveTable(final Vector data) {
        tModelMove = new DefaultTableModel(data, colReturn) {
            @Override
            public Class<?> getColumnClass(int col) {
                if (data.isEmpty())
                    return super.getClass();
                else
                    return (col == 0) ? getValueAt(0, col).getClass() : Object.class;
            }

            @Override
            public boolean isCellEditable(int row, int col) {
                return col == 0 ? true : false;
            }
        };

        tModelMove.addTableModelListener(new TableModelListener() {
            public void tableChanged(TableModelEvent e) {
                if (tableModelReturnListenerIsChanging) {
                    return;
                }
                int firstRow = e.getFirstRow();
                int column = e.getColumn();

                if (column != 0 || maxSelectedReturnRow == -1 || minSelectedReturnRow == -1) {
                    return;
                }
                tableModelReturnListenerIsChanging = true;
                boolean value = ((Boolean) tModelMove.getValueAt(firstRow, column)).booleanValue();
                for (int i = minSelectedReturnRow; i <= maxSelectedReturnRow; i++) {
                    tModelMove.setValueAt(Boolean.valueOf(value), tableReturn.convertRowIndexToModel(i), column);
                }

                minSelectedReturnRow = -1;
                maxSelectedReturnRow = -1;

                tableModelReturnListenerIsChanging = false;
            }
        });

        tableReturn.setModel(tModelMove);
        tableReturn.setAutoCreateColumnsFromModel(true);

        try {
            UtilSkladHO.initColumTableMap(tableReturn, mapSettingReturn);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ошибка! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }

        sorterReturn = new TableRowSorter<TableModel>(tModelMove);
        tableReturn.setRowSorter(sorterReturn);
        tableReturn.getColumnModel().getColumn(0).setHeaderRenderer(new CheckBoxHeader(tableReturn.getTableHeader(), 0, ""));

        tableReturn.getColumnModel().getColumn(29).setCellRenderer(renderer);
    }

    private Vector getDataTable(final String type) {
        dataTable = new Vector();

        try {
            pb = new ProgressBar(this, false, "Сбор данных ...");
            SwingWorker sw = new SwingWorker() {
                protected Object doInBackground() {
                    try {

                        if (type.equals(UtilSkladHO.DATA_MATERIAL)) {
                            pb.setMessage("Обновление таблицы материалов ...");

                            if (TYPE.equals(UtilSkladHO.TYPE_ADD_RETURN) || TYPE.equals(UtilSkladHO.TYPE_EDIT_RETURN)) {
                                dataTable = spdb.getDataProductMaterialTable(idProduct);

                            } else if (TYPE.equals(UtilSkladHO.TYPE_OPEN)) {
                                try {
                                    spdb = new SkladHOPDB();
                                    dataTable = spdb.getDataProductMaterialTable(idProduct);

                                } catch (Exception e) {
                                    dataTable = new Vector();
                                    JOptionPane.showMessageDialog(null, "Ошибка! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
                                } finally {
                                    spdb.disConn();
                                }
                            }
                        } else if (type.equals(UtilSkladHO.DATA_RETURN)) {
                            pb.setMessage("Обновление таблицы возвраты ...");

                            if (TYPE.equals(UtilSkladHO.TYPE_ADD_RETURN) || TYPE.equals(UtilSkladHO.TYPE_EDIT_RETURN)) {
                                dataTable = spdb.getDataProductReturnTableTemp();

                            } else if (TYPE.equals(UtilSkladHO.TYPE_OPEN)) {
                                try {
                                    spdb = new SkladHOPDB();
                                    dataTable = spdb.getDataProductReturnTable(idProduct);

                                } catch (Exception e) {
                                    dataTable = new Vector();
                                    JOptionPane.showMessageDialog(null, "Ошибка! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
                                } finally {
                                    spdb.disConn();
                                }
                            }
                        }
                    } catch (Exception e) {
                        dataTable = new Vector();
                        JOptionPane.showMessageDialog(null, "Сбой обновления! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
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

            if (JOptionPane.showOptionDialog(null,
                    panelCheckBox,
                    "Столбцы таблицы",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    new Object[]{"Сохранить", "Отмена"},
                    "Сохранить") == JOptionPane.YES_OPTION) {

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
                    JOptionPane.showMessageDialog(null,
                            "Ошибка! " + e.getMessage(),
                            "Ошибка",
                            javax.swing.JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Ошибка. " + e.getMessage(),
                    "Ошибка",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void readSettings() throws Exception {
        try {
            Item item = UtilFunctions.getItemsModel(UtilSkladHO.DEPT_MODEL, UtilSkladHO.DEPT_SELECT_ITEM);

            if (item != null)
                dept.setSelectedItem(item);
        } catch (Exception e) {
            throw new Exception("Ошибка! " + e.getMessage());
        }

        try {
            String[][] arr = UtilFunctions.readPropFileStringsArray(UtilSkladHO.SETTING_MAP_PRODUCT_TMC);

            for (int i = 0; i < arr.length; i++)
                mapSettingTmc.put(arr[i][0], arr[i][1]);

        } catch (Exception e) {
            mapSettingTmc = new HashMap<String, String>();
            throw new Exception("Ошибка! " + e.getMessage());
        }

        try {
            String[][] arr = UtilFunctions.readPropFileStringsArray(UtilSkladHO.SETTING_MAP_PRODUCT_MOVE);

            for (int i = 0; i < arr.length; i++)
                mapSettingReturn.put(arr[i][0], arr[i][1]);

        } catch (Exception e) {
            mapSettingReturn = new HashMap<String, String>();
            throw new Exception("Ошибка! " + e.getMessage());
        }
    }

    public MainController getController() {
        return controller;
    }

    public void setController(final MainController controller) {
        this.controller = controller;
    }
}
