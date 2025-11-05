package dept.production.zsh.spec;

import by.march8.ecs.MainController;
import by.march8.ecs.framework.common.LogCrutch;
import com.jhlabs.awt.ParagraphLayout;
import com.toedter.calendar.JDateChooser;
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
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.TreeMap;
import java.util.Vector;

/**
 * @author lidashka
 */
@SuppressWarnings("all")
public class SpecForm extends javax.swing.JDialog implements FormMenu {
    // private static final Logger log = new Log().getLoger(SpecForm.class);
    private static final LogCrutch log = new LogCrutch();
    private static TreeMap menu = new TreeMap();
    JButton buttSearch;
    JButton buttCopy;
    JButton buttEdit;
    JButton buttOpen;
    JButton buttAdd;
    JButton buttSelect;
    JButton buttSelectList;
    JDateChooser sStDate;
    JDateChooser eStDate;
    JDateChooser sInsDate;
    JDateChooser eInsDate;
    JComboBox dept;
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
    JTextField modelNaim;
    JTextField specNum;
    JPanel panelQuestion;
    DefaultTableModel tModel;
    Vector col;
    Vector rowAllSpec;
    TableRowSorter sorter;
    int minSelectedRow = -1;
    int maxSelectedRow = -1;
    boolean tableModelListenerIsChanging = false;
    SpecPDB spdb;
    PDB pdb;
    private ButtonGroup buttonGroupQuestion;
    private JTextField textReport;
    private User user = User.getInstance();
    private ProgressBar pb;

    private MainController controller;

    private Vector dataSelected;
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

    public SpecForm() {
        initMenu();
    }

    public SpecForm(MainController mainController, boolean modal) {
        super(mainController.getMainForm(), modal);
        controller = mainController;

        if (user.getFio() != null) {
            try {
                spdb = new SpecPDB();
                int i = 0;
                Vector data = spdb.getAllDept();
                UtilSpec.DEPT_ITEMS = new Object[data.size() + 1];

                UtilSpec.DEPT_MODEL.add(new Item(-1, "Все...", ""));
                UtilSpec.DEPT_ITEMS[i++] = "Все...";
                for (int j = 0; j < data.size(); j++) {
                    UtilSpec.DEPT_MODEL.add(new Item(Integer.parseInt(((Vector) data.elementAt(j)).get(0).toString()), ((Vector) data.elementAt(j)).get(1).toString(), ""));
                    UtilSpec.DEPT_ITEMS[i++] = ((Vector) data.elementAt(j)).get(1).toString();
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Список подразделений не загружен! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
            } finally {
                spdb.disConn();
            }

            try {
                UtilSpec.DEPT_SELECT_ITEM = UtilFunctions.readPropFile(UtilSpec.SETTING_DEPT_SELECT_ITEM);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Ошибка! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
            }

            try {
                UtilSpec.ROUNDING_NORM = (UtilFunctions.readPropFile(UtilSpec.SETTING_ROUNDING_NORM) != -1) ? UtilFunctions.readPropFile(UtilSpec.SETTING_ROUNDING_NORM) : 4;
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Ошибка! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
            }

            init();

            java.util.Date d = (Calendar.getInstance()).getTime();
            d.setDate(1);

            sStDate.setDate(d);
            eStDate.setDate((Calendar.getInstance()).getTime());

            sInsDate.setDate(d);
            eInsDate.setDate((Calendar.getInstance()).getTime());

            createTableSpec(getAllSpec(true));

            setLocationRelativeTo(controller.getMainForm());
            setVisible(true);
        } else
            JOptionPane.showMessageDialog(null, "Такой логин и пароль не присвоен ни одному пользователю. \n Обратитесь к администратору! ", "Вход", javax.swing.JOptionPane.WARNING_MESSAGE);
    }

    public SpecForm(MainController mainController, boolean modal, String str) {
        super(mainController.getMainForm(), modal);
        controller = mainController;

        try {
            UtilSpec.DEPT_SELECT_ITEM = UtilFunctions.readPropFile(UtilSpec.SETTING_DEPT_SELECT_ITEM_DIALOG);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Ошибка! " + e.getMessage(),
                    "Ошибка",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
        }

        init();

        buttPanel.removeAll();
        buttPanel.add(buttOpen);
        buttPanel.add(buttSelectList);
        buttPanel.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);

        UtilSpec.SPEC_BUTT_SELECT_ACTION = false;

        java.util.Date d = (Calendar.getInstance()).getTime();
        d.setDate(1);

        sStDate.setDate(d);
        eStDate.setDate((Calendar.getInstance()).getTime());

        sInsDate.setDate(d);
        eInsDate.setDate((Calendar.getInstance()).getTime());

        createTableSpec(getAllSpec(true));

        setLocationRelativeTo(controller.getMainForm());
        setVisible(true);
    }
    public SpecForm(MainController mainController, boolean modal, int model) {
        super(mainController.getMainForm(), modal);
        controller = mainController;

        if (user.getFio() != null) {
            try {
                spdb = new SpecPDB();
                int i = 0;
                Vector data = spdb.getAllDept();
                UtilSpec.DEPT_ITEMS = new Object[data.size() + 1];

                UtilSpec.DEPT_MODEL.add(new Item(-1, "Все...", ""));
                UtilSpec.DEPT_ITEMS[i++] = "Все...";
                for (int j = 0; j < data.size(); j++) {
                    UtilSpec.DEPT_MODEL.add(new Item(Integer.parseInt(((Vector) data.elementAt(j)).get(0).toString()), ((Vector) data.elementAt(j)).get(1).toString(), ""));
                    UtilSpec.DEPT_ITEMS[i++] = ((Vector) data.elementAt(j)).get(1).toString();
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Список подразделений не загружен! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
            } finally {
                spdb.disConn();
            }

            try {
                UtilSpec.DEPT_SELECT_ITEM = UtilFunctions.readPropFile(UtilSpec.SETTING_DEPT_SELECT_ITEM);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Ошибка! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
            }

            try {
                UtilSpec.ROUNDING_NORM = (UtilFunctions.readPropFile(UtilSpec.SETTING_ROUNDING_NORM) != -1) ? UtilFunctions.readPropFile(UtilSpec.SETTING_ROUNDING_NORM) : 4;
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Ошибка! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
            }

            init();

            buttPanel.removeAll();
            buttPanel.add(buttOpen);
            buttPanel.add(buttSelect);
            buttPanel.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);

            UtilSpec.SPEC_BUTT_SELECT_ACTION = false;

            java.util.Date d = (Calendar.getInstance()).getTime();
            d.setDate(1);

            sStDate.setDate(d);
            eStDate.setDate((Calendar.getInstance()).getTime());

            sInsDate.setDate(d);
            eInsDate.setDate((Calendar.getInstance()).getTime());

            modelNum.setText(String.valueOf(model));

            buttSearch.doClick();

            setLocationRelativeTo(controller.getMainForm());
            setVisible(true);
        } else
            JOptionPane.showMessageDialog(null, "Такой логин и пароль не присвоен ни одному пользователю. \n Обратитесь к администратору! ", "Вход", javax.swing.JOptionPane.WARNING_MESSAGE);
    }

    private void initMenu() {
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu2 = new javax.swing.JMenu();
        jMenuItem3 = new javax.swing.JMenuItem();
        jMenu3 = new javax.swing.JMenu();
        jMenuItem5 = new javax.swing.JMenuItem();
        jMenu4 = new javax.swing.JMenu();
        jMenuItem6 = new javax.swing.JMenuItem();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenuItem7 = new javax.swing.JMenuItem();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem4 = new javax.swing.JMenuItem();


        jMenu2.setText("Правка");

        jMenuItem3.setText("Удалить");
        jMenu2.add(jMenuItem3);

        jMenuBar1.add(jMenu2);

        jMenu3.setText("Переход");

        jMenuItem5.setText("Мои спецификации");
        jMenu3.add(jMenuItem5);

        jMenuBar1.add(jMenu3);

        jMenu4.setText("Сервис");

        jMenuItem6.setText("Округление");

        jMenu4.add(jMenuItem6);

        jMenuBar1.add(jMenu4);

        jMenu1.setText("Документы");

        jMenuItem2.setText("Ведомость Тр-2(з-шв)");

        jMenu1.add(jMenuItem2);

        jMenuItem7.setText("Ведомость Тр-2(з-шв) + Цены");
        jMenu1.add(jMenuItem7);

        jMenuItem1.setText("Накопительная ведомость норм");

        jMenu1.add(jMenuItem1);

        jMenuItem4.setText("Накопительная ведомость оборудования");

        jMenu1.add(jMenuItem4);

        jMenuBar1.add(jMenu1);

        setJMenuBar(jMenuBar1);

        jMenu2.setVisible(false);
        jMenuItem3.setVisible(false);
        jMenu3.setVisible(false);
        jMenuItem5.setVisible(false);
        jMenuItem2.setVisible(false);
        jMenuItem7.setVisible(false);
        jMenuItem1.setVisible(false);
        jMenuItem4.setVisible(false);

        menu.put("1", jMenu2);              //Правка
        menu.put("1-1", jMenuItem3);            //Удалить
        menu.put("2", jMenu3);              //Переход
        menu.put("2-1", jMenuItem5);            //Мои спецификации
        menu.put("3", jMenu4);              //Сервис
        menu.put("3-1", jMenuItem6);            //Округление
        menu.put("4", jMenu1);              //Документы
        menu.put("4-1", jMenuItem2);            //Ведомость Тр-2(з-шв)
        menu.put("4-2", jMenuItem7);            //Ведомость Тр-2(з-шв) + Цены
        menu.put("4-3", jMenuItem1);            //Накопительная ведомость норм
        menu.put("4-4", jMenuItem4);            //Накопительная ведомость оборудования

    }

    private void init() {
        setTitle("Поиск спецификаций");

        setMinimumSize(new Dimension(580, 500));
        setPreferredSize(new Dimension(880, 700));
        initMenu();
        controller.menuFormInitialisation(getClass().getName(), menu);

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

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
        buttCopy = new JButton("Копировать");
        buttEdit = new JButton("Изменить");
        buttOpen = new JButton("Открыть");
        buttAdd = new JButton("Добавить");
        buttSelect = new JButton("Выбрать");
        buttSelectList = new JButton("Выбрать");
        col = new Vector();
        table = new JTable();
        modelNum = new JTextField();
        modelNaim = new JTextField();
        specNum = new JTextField();
        title = new JLabel("Спецификации моделей");
        checkboxSt = new JCheckBox("Дата ввода");
        checkboxIns = new JCheckBox("Корректировка");
        dept = new JComboBox(UtilSpec.DEPT_ITEMS);

        osnova.setLayout(new BorderLayout(1, 1));
        osnova.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        searchPanel.setLayout(new ParagraphLayout());
        searchButtPanel.setLayout(new GridLayout(0, 3, 5, 5));
        searchButtPanel.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        buttPanel.setLayout(new GridLayout(0, 4, 5, 5));
        buttPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        upPanel.setLayout(new BorderLayout(1, 1));
        upPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        dept.setPreferredSize(new Dimension(300, 20));
        modelNum.setPreferredSize(new Dimension(100, 20));
        modelNaim.setPreferredSize(new Dimension(300, 20));
        specNum.setPreferredSize(new Dimension(150, 20));
        sStDate.setPreferredSize(new Dimension(200, 20));
        eStDate.setPreferredSize(new Dimension(200, 20));
        sInsDate.setPreferredSize(new Dimension(200, 20));
        eInsDate.setPreferredSize(new Dimension(200, 20));
        buttSearch.setPreferredSize(new Dimension(50, 20));

        final JRadioButton jrOne = new JRadioButton();
        jrOne.setFont(new java.awt.Font("Dialog", 0, 13));
        jrOne.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jrOne.setText("Одну;");
        jrOne.setActionCommand("true");
        jrOne.setSelected(true);

        final JRadioButton jrMore = new JRadioButton();
        jrMore.setFont(new java.awt.Font("Dialog", 0, 13));
        jrMore.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jrMore.setText("Несколько;");
        jrMore.setActionCommand("false");

        buttonGroupQuestion = new ButtonGroup();
        buttonGroupQuestion.add(jrOne);
        buttonGroupQuestion.add(jrMore);

        panelQuestion = new JPanel();
        panelQuestion.setLayout(new ParagraphLayout());
        panelQuestion.add(new JLabel(), ParagraphLayout.NEW_PARAGRAPH);
        panelQuestion.add(new JLabel("Сформировать спецификацию:"));
        panelQuestion.add(new JLabel(), ParagraphLayout.NEW_PARAGRAPH);
        panelQuestion.add(jrOne);
        panelQuestion.add(jrMore);

        modelNum.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                searchKeyPressed(evt);
            }
        });

        modelNaim.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                searchKeyPressed(evt);
            }
        });

        specNum.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                searchKeyPressed(evt);
            }
        });

        sStDate.setEnabled(false);
        eStDate.setEnabled(false);
        sInsDate.setEnabled(false);
        eInsDate.setEnabled(false);

        dept.setSelectedItem(UtilSpec.getDept(UtilSpec.DEPT_MODEL, UtilSpec.DEPT_SELECT_ITEM));

        UtilSpec.SPEC_ID = 1;
        UtilSpec.SPEC_NAME = "Спецификация отсутствует";
        UtilSpec.SPEC_NORM = 0;
        UtilSpec.SPEC_MODEL = -1;
        UtilSpec.SPEC_SELECTED_MODEL = new Vector();

        title.setHorizontalAlignment(JLabel.CENTER);
        title.setFont(new Font("serif", Font.PLAIN, 24));

        table.setAutoCreateColumnsFromModel(true);

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

        TableFilterHeader filterHeader = new TableFilterHeader(table, AutoChoices.ENABLED);

        dept.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deptActionPerformed(evt);
            }
        });

        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });

        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });

        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });

        jMenuItem4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem4ActionPerformed(evt);
            }
        });

        jMenuItem5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem5ActionPerformed(evt);
            }
        });

        jMenuItem6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem6ActionPerformed(evt);
            }
        });

        jMenuItem7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem7ActionPerformed(evt);
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

        buttEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttEditActionPerformed(evt);
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

        buttSelect.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttSelectActionPerformed(evt);
            }
        });

        buttSelectList.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttSelectListActionPerformed(evt);
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

        col.add("");
        col.add("Модель");
        col.add("Цех");
        col.add("№");
        col.add("Спецификация");
        col.add("Бриг. норма");
        col.add("Дата ввода");
        col.add("Дата корр.");
        col.add("Осн.");


        searchPanel.add(new JLabel("Спецификация №"), ParagraphLayout.NEW_PARAGRAPH);
        searchPanel.add(specNum);
        searchPanel.add(new JLabel("    Цех:"));
        searchPanel.add(dept);
        searchPanel.add(new JLabel("Модель:"), ParagraphLayout.NEW_PARAGRAPH);
        searchPanel.add(modelNum);
        searchPanel.add(modelNaim);
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

        buttPanel.add(buttCopy);
        buttPanel.add(buttAdd);
        buttPanel.add(buttEdit);
        buttPanel.add(buttOpen);

        if (!jMenuItem5.isVisible()) {
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

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu2 = new javax.swing.JMenu();
        jMenuItem3 = new javax.swing.JMenuItem();
        jMenu3 = new javax.swing.JMenu();
        jMenuItem5 = new javax.swing.JMenuItem();
        jMenu4 = new javax.swing.JMenu();
        jMenuItem6 = new javax.swing.JMenuItem();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenuItem7 = new javax.swing.JMenuItem();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem4 = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jMenu2.setText("Правка");

        jMenuItem3.setText("Удалить");
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem3);

        jMenuBar1.add(jMenu2);

        jMenu3.setText("Переход");

        jMenuItem5.setText("Мои спецификации");
        jMenuItem5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem5ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem5);

        jMenuBar1.add(jMenu3);

        jMenu4.setText("Сервис");

        jMenuItem6.setText("Округление");
        jMenuItem6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem6ActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItem6);

        jMenuBar1.add(jMenu4);

        jMenu1.setText("Документы");

        jMenuItem2.setText("Ведомость Тр-2(з-шв)");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem2);

        jMenuItem7.setText("Ведомость Тр-2(з-шв) + Цены");
        jMenuItem7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem7ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem7);

        jMenuItem1.setText("Накопительная ведомость норм");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem1);

        jMenuItem4.setText("Накопительная ведомость оборудования");
        jMenuItem4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem4ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem4);

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
                        .addGap(0, 273, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed
        if (JOptionPane.showOptionDialog(
                null,
                "Удалить?", "Удаление",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,
                null,
                new Object[]{"Да", "Нет"}, "Да") == JOptionPane.YES_OPTION) {

            try {
                spdb = new SpecPDB();
                for (Object row : tModel.getDataVector()) {
                    if (((Vector) row).get(0).toString().equals("true")) {
                        if (spdb.testDeleteSpec(Integer.valueOf(((Vector) row).get(3).toString()))) {
                            JOptionPane.showMessageDialog(null, "Спецификация №" + Integer.valueOf(((Vector) row).get(3).toString()) + " используется в листках запуска!", "Внимание!", javax.swing.JOptionPane.WARNING_MESSAGE);
                            break;
                        } else
                            spdb.deleteSpec(Integer.valueOf(((Vector) row).get(3).toString()));
                    }
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Ошибка удаления! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
            } finally {
                spdb.disConn();
            }
            createTableSpec(getAllSpec(true));
        }
    }//GEN-LAST:event_jMenuItem3ActionPerformed

    private void jMenuItem5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem5ActionPerformed
        try {
            spdb = new SpecPDB();
            JDateChooser format = new JDateChooser();
            format.setDate((Calendar.getInstance()).getTime());
            if (JOptionPane.showOptionDialog(null, format, "Показать начиная с: ", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, new Object[]{"Да", "Отмена"}, format) == JOptionPane.YES_OPTION) {
                if (UtilFunctions.checkDate(new SimpleDateFormat("dd.MM.yyyy").format(format.getDate())))
                    createTableSpec(spdb.searchSpec(Integer.valueOf(user.getIdEmployee()),
                            UtilFunctions.convertDateStrToLong(new SimpleDateFormat("dd.MM.yyyy").format(format.getDate()))));
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ошибка! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        } finally {
            spdb.disConn();
        }
    }//GEN-LAST:event_jMenuItem5ActionPerformed

    private void jMenuItem6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem6ActionPerformed
        try {
            JSpinner rounding = new JSpinner();
            rounding.setValue(UtilSpec.ROUNDING_NORM);
            if (JOptionPane.showOptionDialog(null, rounding, "Округлять до знака: ", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, new Object[]{"Да", "Отмена"}, rounding) == JOptionPane.YES_OPTION) {
                UtilSpec.ROUNDING_NORM = Integer.valueOf(rounding.getValue().toString());
                UtilFunctions.setSettingPropFile(rounding.getValue().toString(), UtilSpec.SETTING_ROUNDING_NORM);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ошибка! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jMenuItem6ActionPerformed

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        if (table.getSelectedRow() != -1) {
            try {
                spdb = new SpecPDB();
                SpecOO oo = new SpecOO(table.getValueAt(table.getSelectedRow(), 4).toString(),
                        table.getValueAt(table.getSelectedRow(), 6).toString(),
                        new BigDecimal(Double.valueOf(table.getValueAt(table.getSelectedRow(), 5).toString())).setScale(UtilSpec.ROUNDING_NORM, RoundingMode.HALF_UP).doubleValue(),
                        spdb.getDataSpecItem(Integer.valueOf(table.getValueAt(table.getSelectedRow(), 3).toString())),
                        spdb.getDataSpecTech(Integer.valueOf(table.getValueAt(table.getSelectedRow(), 3).toString())),
                        ((Item) UtilSpec.DEPT_MODEL.get(dept.getSelectedIndex())).getId());
                oo.createReport("SpecТр-2.ots");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Ошибка! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
            } finally {
                spdb.disConn();
            }
        } else
            JOptionPane.showMessageDialog(null, "Вы не выбрали спецификацию!", "Внимание", javax.swing.JOptionPane.WARNING_MESSAGE);

    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void jMenuItem7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem7ActionPerformed
        if (table.getSelectedRow() != -1) {
            try {
                spdb = new SpecPDB();
                SpecOO oo = new SpecOO(table.getValueAt(table.getSelectedRow(), 4).toString(),
                        table.getValueAt(table.getSelectedRow(), 6).toString(),
                        new BigDecimal(Double.valueOf(table.getValueAt(table.getSelectedRow(), 5).toString())).setScale(UtilSpec.ROUNDING_NORM, RoundingMode.HALF_UP).doubleValue(),
                        spdb.getDataSpecItem(Integer.valueOf(table.getValueAt(table.getSelectedRow(), 3).toString())),
                        spdb.getDataSpecTech(Integer.valueOf(table.getValueAt(table.getSelectedRow(), 3).toString())),
                        ((Item) UtilSpec.DEPT_MODEL.get(dept.getSelectedIndex())).getId());
                oo.createReport("SpecТр-2Price.ots");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Ошибка! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
            } finally {
                spdb.disConn();
            }
        } else
            JOptionPane.showMessageDialog(null, "Вы не выбрали спецификацию!", "Внимание", javax.swing.JOptionPane.WARNING_MESSAGE);

    }//GEN-LAST:event_jMenuItem7ActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        try {
            spdb = new SpecPDB();
            Vector dataOO = new Vector();
            for (int i = 0; i < tModel.getDataVector().size(); i++) {
                if (((Vector) tModel.getDataVector().elementAt(i)).get(0).toString().equals("true")) {
                    Vector tmp = new Vector();
                    tmp.add(tModel.getValueAt(i, 4).toString());
                    tmp.add(tModel.getValueAt(i, 6).toString());
                    tmp.add(new BigDecimal(Double.valueOf(tModel.getValueAt(i, 5).toString())).setScale(UtilSpec.ROUNDING_NORM, RoundingMode.HALF_UP).doubleValue());
                    tmp.add(spdb.getDataSpecItem(Integer.valueOf(tModel.getValueAt(i, 3).toString())));
                    dataOO.add(tmp);
                }
            }
            SpecOO oo = new SpecOO(dataOO, ((Item) UtilSpec.DEPT_MODEL.get(dept.getSelectedIndex())).getId());
            oo.createReport("SpecVedomostNorm.ots");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ошибка! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        } finally {
            spdb.disConn();
        }
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jMenuItem4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem4ActionPerformed
        try {
            spdb = new SpecPDB();
            Vector dataOO = new Vector();
            for (int i = 0; i < tModel.getDataVector().size(); i++) {
                if (((Vector) tModel.getDataVector().elementAt(i)).get(0).toString().equals("true")) {
                    Vector tmp = new Vector();
                    tmp.add(tModel.getValueAt(i, 4).toString());
                    tmp.add(tModel.getValueAt(i, 6).toString());
                    tmp.add(spdb.getDataSpecTech(Integer.valueOf(tModel.getValueAt(i, 3).toString())));
                    dataOO.add(tmp);
                }
            }
            SpecOO oo = new SpecOO(dataOO, ((Item) UtilSpec.DEPT_MODEL.get(dept.getSelectedIndex())).getId());
            oo.createReport("SpecVedomostNakopTech.ots");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ошибка! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        } finally {
            spdb.disConn();
        }
    }//GEN-LAST:event_jMenuItem4ActionPerformed
    // End of variables declaration//GEN-END:variables

    private void searchKeyPressed(java.awt.event.KeyEvent evt) {
        if (evt.getKeyCode() == evt.VK_ENTER) buttSearch.doClick();
    }

    private void deptActionPerformed(ActionEvent evt) {
        try {
            UtilSpec.DEPT_SELECT_ITEM = ((Item) UtilSpec.DEPT_MODEL.get(dept.getSelectedIndex())).getId();
            UtilFunctions.setSettingPropFile(
                    String.valueOf(((Item) UtilSpec.DEPT_MODEL.get(dept.getSelectedIndex())).getId()),
                    UtilSpec.SETTING_DEPT_SELECT_ITEM);
            createTableSpec(getAllSpec(true));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Ошибка! " + e.getMessage(),
                    "Ошибка",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buttSearchActionPerformed(ActionEvent evt) {
        rowAllSpec = new Vector();

        try {
            spdb = new SpecPDB();

            pb = new ProgressBar(this, false, "Поиск спецификаций...");
            final SwingWorker sw = new SwingWorker() {
                @Override
                protected Object doInBackground() throws Exception {
                    rowAllSpec = spdb.searchSpec(specNum.getText().trim(),
                            ((Item) UtilSpec.DEPT_MODEL.get(dept.getSelectedIndex())).getId(),
                            modelNum.getText().trim(),
                            modelNaim.getText().trim().toUpperCase(),
                            checkboxSt.isSelected(),
                            new SimpleDateFormat("dd.MM.yyyy").format(sStDate.getDate()),
                            new SimpleDateFormat("dd.MM.yyyy").format(eStDate.getDate()),
                            checkboxIns.isSelected(),
                            new SimpleDateFormat("dd.MM.yyyy").format(sInsDate.getDate()),
                            new SimpleDateFormat("dd.MM.yyyy").format(eInsDate.getDate()));
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
            rowAllSpec = new Vector();
            JOptionPane.showMessageDialog(null, "Ошибка поиска! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        } finally {
            spdb.disConn();
        }

        createTableSpec(rowAllSpec);
    }

    private void buttCopyActionPerformed(ActionEvent evt) {
        getDataEditOrCopySpec(1);
        if (UtilSpec.SPEC_BUTT_SAVE_ACTION)
            createTableSpec(getAllSpec(false));
    }

    private void buttEditActionPerformed(ActionEvent evt) {
        getDataEditOrCopySpec(2);
        if (UtilSpec.SPEC_BUTT_SAVE_ACTION)
            createTableSpec(getAllSpec(false));
    }

    private void buttOpenActionPerformed(ActionEvent evt) {
        getDataEditOrCopySpec(3);
    }

    private void buttAddActionPerformed(ActionEvent evt) {
        try {
            if (((Item) UtilSpec.DEPT_MODEL.get(dept.getSelectedIndex())).getId() == UtilSpec.ID_DEPT_OTK) {
                if (JOptionPane.showOptionDialog(null,
                        panelQuestion,
                        "Сформировать",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE,
                        null, new Object[]{"Продолжить", "Отмена"},
                        "Да") == JOptionPane.YES_OPTION) {

                    if (Boolean.valueOf(buttonGroupQuestion.getSelection().getActionCommand())) {
                        new NormForm(controller, true);
                    } else {
                        new NormAddMoreForm(controller,
                                true,
                                ((Item) UtilSpec.DEPT_MODEL.get(dept.getSelectedIndex())).getId(),
                                null);
                    }

                }

            } else {
                new NormForm(controller, true);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    e.getMessage(),
                    "Ошибка",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
        }

        if (UtilSpec.SPEC_BUTT_SAVE_ACTION)
            createTableSpec(getAllSpec(false));

    }

    private void buttSelectActionPerformed(ActionEvent evt) {
        try {
            if (table.getSelectedRow() != -1) {
                UtilSpec.SPEC_BUTT_SELECT_ACTION = true;
                UtilSpec.SPEC_ID = Integer.valueOf(table.getValueAt(table.getSelectedRow(), 3).toString());
                UtilSpec.SPEC_NAME = table.getValueAt(table.getSelectedRow(), 4).toString();
                UtilSpec.SPEC_NORM = Double.valueOf(table.getValueAt(table.getSelectedRow(), 5).toString());
                UtilSpec.SPEC_MODEL = Integer.valueOf(table.getValueAt(table.getSelectedRow(), 1).toString());
                UtilSpec.SPEC_SELECTED_MODEL = new Vector();

                dispose();
            } else {
                JOptionPane.showMessageDialog(null,
                        "Вы не выбрали спецификацию!",
                        "Внимание",
                        javax.swing.JOptionPane.WARNING_MESSAGE);
            }
        } catch (Exception e) {
            UtilSpec.SPEC_BUTT_SELECT_ACTION = false;
            UtilSpec.SPEC_ID = 1;
            UtilSpec.SPEC_NAME = "Спецификация отсутствует";
            UtilSpec.SPEC_NORM = 0;
            UtilSpec.SPEC_MODEL = -1;
            UtilSpec.SPEC_SELECTED_MODEL = new Vector();
        }

    }

    private void buttSelectListActionPerformed(ActionEvent evt) {
        try {

            dataSelected = new Vector();

            pb = new ProgressBar(this, false, "Обработка...");
            final SwingWorker sw = new SwingWorker() {
                @Override
                protected Object doInBackground() throws Exception {
                    for (int i = 0; i < table.getRowCount(); i++) {
                        if ((Boolean) table.getValueAt(i, 0)) {
                            Vector tmp = new Vector();

                            tmp.add(Integer.valueOf(table.getValueAt(i, 1).toString())); //model
                            tmp.add(table.getValueAt(i, 4).toString());                  //name

                            dataSelected.add(tmp);

                        }
                    }
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

            if (!dataSelected.isEmpty()) {
                UtilSpec.SPEC_BUTT_SELECT_ACTION = true;
                UtilSpec.SPEC_ID = 1;
                UtilSpec.SPEC_NAME = "Спецификация отсутствует";
                UtilSpec.SPEC_NORM = 0;
                UtilSpec.SPEC_MODEL = -1;
                UtilSpec.SPEC_SELECTED_MODEL = dataSelected;

                UtilFunctions.setSettingPropFile(
                        String.valueOf(((Item) UtilSpec.DEPT_MODEL.get(dept.getSelectedIndex())).getId()),
                        UtilSpec.SETTING_DEPT_SELECT_ITEM_DIALOG);

                dispose();
            } else {
                JOptionPane.showMessageDialog(null,
                        "Вы не выбрали спецификации!",
                        "Внимание",
                        javax.swing.JOptionPane.WARNING_MESSAGE);
            }
        } catch (Exception e) {
            UtilSpec.SPEC_BUTT_SELECT_ACTION = false;
            UtilSpec.SPEC_ID = 1;
            UtilSpec.SPEC_NAME = "Спецификация отсутствует";
            UtilSpec.SPEC_NORM = 0;
            UtilSpec.SPEC_MODEL = -1;
            UtilSpec.SPEC_SELECTED_MODEL = new Vector();
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

    private void createTableSpec(final Vector data) {
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
        table.getColumnModel().getColumn(2).setPreferredWidth(100);
        table.getColumnModel().getColumn(3).setPreferredWidth(10);
        table.getColumnModel().getColumn(4).setPreferredWidth(210);
        table.getColumnModel().getColumn(5).setPreferredWidth(60);
        table.getColumnModel().getColumn(6).setPreferredWidth(60);
        table.getColumnModel().getColumn(7).setPreferredWidth(70);
        table.getColumnModel().getColumn(8).setPreferredWidth(10);

        sorter = new TableRowSorter<TableModel>(tModel) {
            @Override
            public Comparator<?> getComparator(int column) {
                if (column == 6 || column == 7) {
                    return new Comparator<String>() {
                        public int compare(String s1, String s2) {
                            try {
                                return ((Date) new SimpleDateFormat("dd-MM-yyyy").parse(s2)).compareTo((Date) new SimpleDateFormat("dd-MM-yyyy").parse(s1));
                            } catch (Exception e) {
                                return s2.compareTo(s1);
                            }
                        }
                    };
                }
                return super.getComparator(column);
            }
        };
        table.setRowSorter(sorter);
        //table.getColumnModel().getColumn(0).setHeaderRenderer(new CheckBoxHeader(table.getTableHeader(), 0, ""));

    }

    private Vector getAllSpec(final boolean paramSort) {
        rowAllSpec = new Vector();
        try {
            spdb = new SpecPDB();

            pb = new ProgressBar(this, false, "Поиск спецификаций...");
            final SwingWorker sw = new SwingWorker() {
                @Override
                protected Object doInBackground() throws Exception {
                    rowAllSpec = ((Item) UtilSpec.DEPT_MODEL.get(dept.getSelectedIndex())).getId() == -1 ?
                            spdb.getAllSpec(paramSort) :
                            spdb.getAllSpec(((Item) UtilSpec.DEPT_MODEL.get(dept.getSelectedIndex())).getId(),
                                    paramSort);
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
            rowAllSpec = new Vector();
            JOptionPane.showMessageDialog(null, "Список спецификаций не загружен! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        } finally {
            spdb.disConn();
        }
        return rowAllSpec;
    }

    private void getDataEditOrCopySpec(int type) {
        if (table.getSelectedRow() != -1) {
            Vector dataSpec = new Vector();
            Vector dataItem = new Vector();
            Vector dataTech = new Vector();

            try {
                spdb = new SpecPDB();
                dataSpec = spdb.getDataSpec(Integer.valueOf(table.getValueAt(table.getSelectedRow(), 3).toString()));
                dataItem = type == 2 ?
                        spdb.getDataSpecItemToEdit(Integer.valueOf(table.getValueAt(table.getSelectedRow(), 3).toString())) :
                        spdb.getDataSpecItem(Integer.valueOf(table.getValueAt(table.getSelectedRow(), 3).toString()));
                dataTech = spdb.getDataSpecTech(Integer.valueOf(table.getValueAt(table.getSelectedRow(), 3).toString()));

                switch (type) {
                    case 1:
                        new NormForm(controller, true, dataSpec, dataItem, dataTech, true);
                        break;
                    case 2:
                        new NormForm(controller, true, dataSpec, dataItem, dataTech, false);
                        break;
                    case 3:
                        new NormForm(controller, true, dataSpec, dataItem, dataTech);
                        break;
                    default:
                        break;
                }

            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Данные спецификаций не загружены! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
            } finally {
                spdb.disConn();
            }
        } else
            JOptionPane.showMessageDialog(null, "Вы не выбрали спецификацию!", "Внимание", javax.swing.JOptionPane.WARNING_MESSAGE);
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
