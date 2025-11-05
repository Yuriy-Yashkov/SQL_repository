package dept.sklad.ho;

import by.march8.ecs.MainController;
import com.jhlabs.awt.ParagraphLayout;
import com.toedter.calendar.JDateChooser;
import common.CheckBoxHeader;
import common.User;
import common.UtilFunctions;
import net.coderazzi.filters.gui.AutoChoices;
import net.coderazzi.filters.gui.TableFilterHeader;
import workDB.PDB;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
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
import java.util.Vector;

/**
 *
 * @author lidashka
 */
@SuppressWarnings("all")
public class SearchTableForm extends javax.swing.JDialog {
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
    private JPanel panelCheckBox;
    private JTextField model;
    private JDateChooser sDate;
    private JDateChooser eDate;
    private JButton buttSearch;
    private JButton buttSelect;
    private JButton buttOpen;
    private JTable table;
    private JLabel title;
    private Vector col;
    private DefaultTableModel tModel;
    private TableRowSorter<TableModel> sorter;
    private HashMap<String, String> mapSetting;
    private TableFilterHeader filterHeader;

    private int idStorage;
    private MainController controller;
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;

    public SearchTableForm(MainController mainController, boolean modal, String date, int idStor) {
        super(mainController.getMainForm(), modal);
        controller = mainController;

        init();

        idStorage = idStor;

        try {
            java.util.Date d = new SimpleDateFormat("dd.MM.yyyy").parse(date);
            d.setDate(1);

            sDate.setDate(d);
            eDate.setDate(new SimpleDateFormat("dd.MM.yyyy").parse(date));
        } catch (Exception e) {
            sDate.setDate((Calendar.getInstance()).getTime());
            eDate.setDate((Calendar.getInstance()).getTime());
        }

        try {
            model.setText(UtilSkladHO.SEARCH_PRODUCT_MODEL);
            if (UtilSkladHO.SEARCH_PRODUCT_SDATE != "" && UtilSkladHO.SEARCH_PRODUCT_EDATE != "") {
                sDate.setDate(new SimpleDateFormat("dd.MM.yyyy").parse(UtilSkladHO.SEARCH_PRODUCT_SDATE));
                eDate.setDate(new SimpleDateFormat("dd.MM.yyyy").parse(UtilSkladHO.SEARCH_PRODUCT_EDATE));
            }
        } catch (Exception e) {
            model.setText("");
            sDate.setDate((Calendar.getInstance()).getTime());
            eDate.setDate((Calendar.getInstance()).getTime());
        }

        try {
            String[][] arr = UtilFunctions.readPropFileStringsArray(UtilSkladHO.SETTING_MAP_PRODUCT_EDIT);

            for (int i = 0; i < arr.length; i++)
                mapSetting.put(arr[i][0], arr[i][1]);

        } catch (Exception e) {
            mapSetting = new HashMap<String, String>();
            JOptionPane.showMessageDialog(null, "Ошибка! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }

        createSearchTableProdTable(getProd());

        setLocationRelativeTo(controller.getMainForm());
        setVisible(true);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jMenu1.setText("Параметры ");

        jMenuItem1.setText("Столбцы таблицы");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem1);

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
                        .addGap(0, 298, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        try {
            createColTable(table, mapSetting, UtilSkladHO.SETTING_MAP_PRODUCT_EDIT);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Ошибка ", javax.swing.JOptionPane.ERROR_MESSAGE);
        } finally {
            createSearchTableProdTable(tModel.getDataVector());
        }
    }//GEN-LAST:event_jMenuItem1ActionPerformed
    // End of variables declaration//GEN-END:variables

    private void init() {
        UtilSkladHO.BUTT_ACTION_SELECT_PRODUCT = false;

        UtilSkladHO.PRODUCT_DATA_VECTOR = new Vector();

        initMenu();

        setTitle(" Карты раскроя");
        setMinimumSize(new Dimension(690, 550));
        setPreferredSize(new Dimension(850, 550));

        osnova = new JPanel();
        searchPanel = new JPanel();
        searchButtPanel = new JPanel();
        upPanel = new JPanel();
        buttPanel = new JPanel();
        panelCheckBox = new JPanel();
        model = new JTextField();
        sDate = new JDateChooser();
        eDate = new JDateChooser();
        title = new JLabel("Справочник ТМЦ");
        buttSearch = new JButton("Поиск");
        buttSelect = new JButton("Выбрать");
        buttOpen = new JButton("Открыть");
        table = new JTable();
        filterHeader = new TableFilterHeader(table, AutoChoices.ENABLED);
        col = new Vector();
        mapSetting = new HashMap<String, String>();

        osnova.setLayout(new BorderLayout(1, 1));
        osnova.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        searchPanel.setLayout(new ParagraphLayout());
        searchButtPanel.setLayout(new GridLayout(0, 5, 5, 5));
        searchButtPanel.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        buttPanel.setLayout(new GridLayout(0, 3, 5, 5));
        buttPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        buttPanel.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        upPanel.setLayout(new BorderLayout(1, 1));
        upPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        panelCheckBox.setLayout(new GridLayout(0, 3, 5, 5));

        model.setPreferredSize(new Dimension(180, 20));
        buttSearch.setPreferredSize(new Dimension(50, 20));
        sDate.setPreferredSize(new Dimension(120, 20));
        eDate.setPreferredSize(new Dimension(120, 20));

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

        model.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                textModeKeyPressed(evt);
            }
        });

        col.add("");
        col.add("№ К/р");
        col.add("Дата");
        col.add("Модель");
        col.add("Название");
        col.add("Артикул");
        col.add("idВид");
        col.add("Вид");
        col.add("Кол-во");
        col.add("Шкала");
        col.add("Примечание");
        col.add("idЦех");
        col.add("Цех");
        col.add("idАвтор");
        col.add("Автор");
        col.add("Дата ввода");
        col.add("Автор ввода");
        col.add("Дата корр.");
        col.add("Автор корр.");
        col.add("Статус");
        col.add("idStatus");

        mapSetting.put("", "true");
        mapSetting.put("№ К/р", "true");
        mapSetting.put("Дата", "true");
        mapSetting.put("Модель", "true");
        mapSetting.put("Название", "true");
        mapSetting.put("Кол-во", "true");
        mapSetting.put("Цех", "true");
        mapSetting.put("Автор", "true");
        mapSetting.put("Дата корр.", "true");
        mapSetting.put("Статус", "true");

        searchPanel.add(new JLabel("Модель:"));
        searchPanel.add(model);
        searchPanel.add(new JLabel("    Дата"));
        searchPanel.add(new JLabel(" с "));
        searchPanel.add(sDate);
        searchPanel.add(new JLabel(" по "));
        searchPanel.add(eDate);

        searchButtPanel.add(buttSearch);

        upPanel.add(searchPanel, BorderLayout.CENTER);
        upPanel.add(searchButtPanel, BorderLayout.SOUTH);

        buttPanel.add(buttOpen);
        buttPanel.add(new JLabel());
        buttPanel.add(buttSelect);

        osnova.add(upPanel, BorderLayout.NORTH);
        osnova.add(new JScrollPane(table), BorderLayout.CENTER);
        osnova.add(buttPanel, BorderLayout.SOUTH);

        getContentPane().add(osnova);
        pack();
    }

    private void initMenu() {
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jMenu1.setText("Параметры ");

        jMenuItem1.setText("Столбцы таблицы");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem1);

        jMenuBar1.add(jMenu1);

        setJMenuBar(jMenuBar1);
    }

    private void buttSearchActionPerformed(ActionEvent evt) {
        try {
            createSearchTableProdTable(getProd());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ошибка! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buttOpenActionPerformed(ActionEvent evt) {
        try {
            if (table.getSelectedRow() != -1) {
                new SkladProductItemForm(controller, true,
                        UtilSkladHO.getItemTable(table),
                        UtilSkladHO.TYPE_OPEN);

            } else
                JOptionPane.showMessageDialog(null, "Вы не выбрали запись!", "Внимание", javax.swing.JOptionPane.WARNING_MESSAGE);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ошибка " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buttSelectActionPerformed(ActionEvent evt) {

        try {
            if (table.getSelectedRow() != -1) {
                UtilSkladHO.PRODUCT_DATA_VECTOR = UtilSkladHO.getItemTable(table);

                UtilSkladHO.BUTT_ACTION_SELECT_PRODUCT = true;

                try {
                    UtilSkladHO.SEARCH_PRODUCT_MODEL = model.getText().trim();
                    UtilSkladHO.SEARCH_PRODUCT_SDATE = new SimpleDateFormat("dd.MM.yyyy").format(sDate.getDate());
                    UtilSkladHO.SEARCH_PRODUCT_EDATE = new SimpleDateFormat("dd.MM.yyyy").format(eDate.getDate());
                } catch (Exception e) {
                    UtilSkladHO.SEARCH_PRODUCT_MODEL = "";
                    UtilSkladHO.SEARCH_PRODUCT_SDATE = "";
                    UtilSkladHO.SEARCH_PRODUCT_EDATE = "";
                }

                dispose();
            } else
                JOptionPane.showMessageDialog(null, "Вы не выбрали запись!", "Внимание", javax.swing.JOptionPane.WARNING_MESSAGE);

        } catch (Exception e) {
            UtilSkladHO.PRODUCT_DATA_VECTOR = new Vector();
            JOptionPane.showMessageDialog(null, "Ошибка " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void textModeKeyPressed(java.awt.event.KeyEvent evt) {
        if (evt.getKeyCode() == evt.VK_ENTER) buttSearch.doClick();
    }

    private Vector getProd() {
        Vector dataTable = new Vector();

        try {
            spdb = new SkladHOPDB();

            dataTable = spdb.getDataProductSklad(true,
                    UtilFunctions.convertDateStrToLong(new SimpleDateFormat("dd.MM.yyyy").format(sDate.getDate())),
                    UtilFunctions.convertDateStrToLong(new SimpleDateFormat("dd.MM.yyyy").format(eDate.getDate())),
                    model.getText().trim(),
                    idStorage);

        } catch (Exception e) {
            dataTable = new Vector();
            JOptionPane.showMessageDialog(null, "Ошибка! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        } finally {
            spdb.disConn();
        }

        return dataTable;
    }

    private void createSearchTableProdTable(final Vector row) {
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

        try {
            UtilSkladHO.initColumTableMap(table, mapSetting);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ошибка! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }

        sorter = new TableRowSorter<TableModel>(tModel);
        table.setRowSorter(sorter);
        table.getColumnModel().getColumn(0).setHeaderRenderer(new CheckBoxHeader(table.getTableHeader(), 0, ""));
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
}
