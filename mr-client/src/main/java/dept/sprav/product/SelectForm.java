package dept.sprav.product;

import com.jhlabs.awt.ParagraphLayout;
import common.ProgressBar;
import net.coderazzi.filters.gui.AutoChoices;
import net.coderazzi.filters.gui.TableFilterHeader;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.Vector;

/**
 * Форма выбора/поиска
 *
 * @author lidashka
 */
public class SelectForm extends JDialog {
    private JPanel osnovaPanel;
    private JPanel centrPanel;
    private JPanel headPanel;
    private JPanel buttPanel;
    private JPanel editPanel;
    private JButton buttClose;
    private JButton buttSelect;
    private JButton buttSearch;
    private Vector col;
    private Vector row;
    private JLabel title;
    private JTable table;
    private DefaultTableModel tModel;
    private TableRowSorter<TableModel> sorter;
    private TableFilterHeader filterHeader;
    private JTextField searchText;
    private JTextField nameEdit;
    private JTextField idEdit;
    private JTextField numEdit;
    private JTextField val1Edit;
    private JTextField val2Edit;
    private JTextPane noteEdit;
    private ProgressBar pb;
    private ProductPDB pdb;

    private JMenuBar jMenuBar;
    private JMenu jMenu2;
    private JMenu jMenu1;
    private JMenuItem jMenuItem1;
    private JMenuItem jMenuItem2;
    private JMenuItem jMenuItem3;
    private JMenuItem jMenuItem4;

    private String typeSearch;
    private String typeForm;

    public SelectForm() {
    }

    /**
     * Конструктор 
     *
     * @param parent
     * @param modal
     * @param type - тип выбора (что ищем).
     */
    public SelectForm(java.awt.Dialog parent,
                      boolean modal,
                      String type) {

        super(parent, modal);


        this.typeSearch = type;
        this.typeForm = UtilProduct.SELECT;

        cleanConstants();
        initMenu();
        initPropSetting();
        init(type);
        initData(type);
        factorForm(type, typeForm);

        this.setTitle("Поиск");
        this.setLocationRelativeTo(parent);
        this.setVisible(true);
    }

    /**
     * Конструктор выбора/поиска тех. описания 
     *
     * @param parent
     * @param modal
     * @param type      - тип выбора (что ищем)
     * @param typeForm  - тип формы 
     */
    public SelectForm(java.awt.Dialog parent,
                      boolean modal,
                      String type,
                      String typeForm) {

        super(parent, modal);

        this.typeSearch = type;
        this.typeForm = typeForm;

        cleanConstants();
        initMenu();
        initPropSetting();
        init(type);
        initData(type);
        factorForm(type, typeForm);

        this.setTitle("Поиск");
        this.setLocationRelativeTo(parent);
        this.setVisible(true);
    }

    /**
     * Очищает константы
     */
    private void cleanConstants() {
        UtilProduct.ACTION_BUTT_SELECT_SEARCH = false;
        UtilProduct.ITEM_SELECT_SEARCH = new ItemSelect();
        UtilProduct.ITEM_SELECT_SEARCH_TO = new ItemSelectTO();

    }

    /**
     * Инициализирует меню формы
     */
    private void initMenu() {
        jMenuBar = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenuItem3 = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        jMenuItem4 = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jMenu2.setText("Файл");

        jMenuItem4.setText("Печать");
        jMenuItem4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                //jMenuItem4ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem4);

        jMenuBar.add(jMenu2);

        jMenu1.setText("Правка");

        jMenuItem1.setText("Добавить запись");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem1);

        jMenuItem2.setText("Изменить запись");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem2);

        jMenuItem3.setText("Удалить запись");
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                //jMenuItem3ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem3);

        jMenuBar.add(jMenu1);

        setJMenuBar(jMenuBar);
    }

    /**
     * Читает настройки из config-файла
     */
    private void initPropSetting() {

    }

    /**
     * Первоначальные параметры в соответствии с типом
     */
    private void initData(String type) {
        Vector data = new Vector();

        try {
            data = getData(type);

        } catch (Exception e) {
            data = new Vector();
            JOptionPane.showMessageDialog(null,
                    "Ошибка! " + e.getMessage(),
                    "Ошибка",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
        }

        createTable(data);
    }

    /**
     * Инициализирует параметры и компоненты формы в соответствии с типом
     */
    private void init(String type) {
        setMinimumSize(new Dimension(350, 450));
        setPreferredSize(new Dimension(450, 500));

        buttClose = new JButton("Закрыть");
        buttClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttCloseActionPerformed(evt);
            }
        });

        buttSelect = new JButton("Выбрать");
        buttSelect.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttSelectActionPerformed(evt);
            }
        });

        buttSearch = new JButton("Найти");
        buttSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttSearchActionPerformed(evt);
            }
        });

        title = new JLabel("");
        title.setHorizontalAlignment(JLabel.CENTER);
        title.setFont(new Font("serif", Font.PLAIN, 18));

        searchText = new JTextField();
        searchText.setPreferredSize(new Dimension(250, 20));
        searchText.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                if (evt.getKeyCode() == KeyEvent.VK_ENTER) buttSearch.doClick();
            }
        });

        col = new Vector();
        col.add("Код");
        if (typeForm.equals(UtilProduct.SELECT_TO)) {
            col.add("№");
            col.add("ID_Тип");
            col.add("Тип");
            col.add("Наименование");
            col.add("Допуск 1");
            col.add("Допуск 2");
            col.add("Примечение");

        } else {
            col.add("Код");
            col.add("Наименование");
            col.add("Примечение");
        }

        table = new JTable();
        table.setAutoCreateColumnsFromModel(true);
        table.getTableHeader().setReorderingAllowed(false);
        table.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() > 1) {
                    try {
                        buttSelect.doClick();
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(null,
                                "Ошибка! " + ex.getMessage(),
                                "Ошибка",
                                javax.swing.JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        tModel = new DefaultTableModel();

        filterHeader = new TableFilterHeader(table, AutoChoices.ENABLED);

        idEdit = new JTextField();
        idEdit.setPreferredSize(new Dimension(100, 20));

        nameEdit = new JTextField();
        nameEdit.setPreferredSize(new Dimension(400, 20));

        noteEdit = new JTextPane();
        noteEdit.setPreferredSize(new Dimension(400, 20));

        numEdit = new JTextField();
        numEdit.setPreferredSize(new Dimension(400, 20));

        val1Edit = new JTextField();
        val1Edit.setPreferredSize(new Dimension(400, 20));

        val2Edit = new JTextField();
        val2Edit.setPreferredSize(new Dimension(400, 20));

        editPanel = new JPanel();
        editPanel.setLayout(new GridLayout(0, 1, 5, 5));
        editPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        editPanel.add(new JLabel("Наименование:"));
        editPanel.add(nameEdit);
        editPanel.add(new JLabel("Примечание:"));
        editPanel.add(new JScrollPane(noteEdit));

        headPanel = new JPanel();
        headPanel.setLayout(new ParagraphLayout());
        headPanel.add(new JLabel(""), ParagraphLayout.NEW_PARAGRAPH);
        headPanel.add(title, ParagraphLayout.NEW_LINE_STRETCH_H);
        headPanel.add(new JLabel(""), ParagraphLayout.NEW_PARAGRAPH);
        headPanel.add(searchText, ParagraphLayout.NEW_LINE_STRETCH_H);
        headPanel.add(buttSearch);

        centrPanel = new JPanel();
        centrPanel.setLayout(new BorderLayout(1, 1));
        centrPanel.add(new JScrollPane(table), BorderLayout.CENTER);

        buttPanel = new JPanel();
        buttPanel.setLayout(new GridLayout(0, 3, 5, 5));
        buttPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        buttPanel.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        buttPanel.add(buttClose);
        buttPanel.add(buttSelect);

        osnovaPanel = new JPanel();
        osnovaPanel.setLayout(new BorderLayout(1, 1));
        osnovaPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        osnovaPanel.add(headPanel, BorderLayout.NORTH);
        osnovaPanel.add(centrPanel, BorderLayout.CENTER);
        osnovaPanel.add(buttPanel, BorderLayout.SOUTH);

        getContentPane().add(osnovaPanel);
        pack();
    }

    private void buttCloseActionPerformed(ActionEvent evt) {
        dispose();
    }

    private void buttSelectActionPerformed(ActionEvent evt) {
        try {
            cleanConstants();

            if (table.getSelectedRow() != -1) {

                UtilProduct.ACTION_BUTT_SELECT_SEARCH = true;

                if (typeForm.equals(UtilProduct.SELECT)) {
                    UtilProduct.ITEM_SELECT_SEARCH = new ItemSelect(
                            Integer.valueOf(table.getValueAt(table.getSelectedRow(), 0).toString()),
                            table.getValueAt(table.getSelectedRow(), 1).toString(),
                            table.getValueAt(table.getSelectedRow(), 2).toString());
                    dispose();
                } else if (typeForm.equals(UtilProduct.SELECT_TO)) {
                    UtilProduct.ITEM_SELECT_SEARCH_TO = new ItemSelectTO(
                            Integer.valueOf(table.getValueAt(table.getSelectedRow(), 0).toString()),
                            table.getValueAt(table.getSelectedRow(), 1).toString(),
                            Integer.valueOf(table.getValueAt(table.getSelectedRow(), 2).toString()),
                            table.getValueAt(table.getSelectedRow(), 4).toString(),
                            Double.valueOf(table.getValueAt(table.getSelectedRow(), 5).toString()),
                            Double.valueOf(table.getValueAt(table.getSelectedRow(), 6).toString()),
                            table.getValueAt(table.getSelectedRow(), 7).toString());
                    dispose();
                }


            } else
                JOptionPane.showMessageDialog(null,
                        "Вы ничего не выбрали!",
                        "Внимание",
                        javax.swing.JOptionPane.WARNING_MESSAGE);

        } catch (Exception e) {
            cleanConstants();

            JOptionPane.showMessageDialog(null,
                    "Ошибка. " + e.getMessage(),
                    "Ошибка",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buttSearchActionPerformed(ActionEvent evt) {
        try {
            String text = searchText.getText().trim();
            if (text.length() == 0) {
                sorter.setRowFilter(null);
            } else {
                try {
                    sorter.setRowFilter(RowFilter.regexFilter("(?iu)" + text));
                    if (table.getRowCount() == 0)
                        JOptionPane.showMessageDialog(null,
                                "По заданному запросу ничего не найдено!",
                                "Внимание",
                                javax.swing.JOptionPane.WARNING_MESSAGE);
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null,
                            "Ошибка " + e.getMessage(),
                            "Ошибка",
                            javax.swing.JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Ошибка " + e.getMessage(),
                    "Ошибка",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void jMenuItem1ActionPerformed(ActionEvent evt) {
        if (typeForm.equals(UtilProduct.SELECT)) {
            showEditPanel(0);
        } else {
            showEditPanel(2);
        }
    }

    private void jMenuItem2ActionPerformed(ActionEvent evt) {
        if (typeForm.equals(UtilProduct.SELECT)) {
            showEditPanel(1);
        } else {
            showEditPanel(3);
        }
    }

    private void createTable(Vector data) {
        tModel = new DefaultTableModel(data, col) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };

        table.setModel(tModel);
        table.setAutoCreateColumnsFromModel(true);
        table.getColumnModel().getColumn(0).setPreferredWidth(20);

        if (typeForm.equals(UtilProduct.SELECT_TO)) {
            table.getColumnModel().getColumn(2).setMinWidth(0);
            table.getColumnModel().getColumn(2).setMaxWidth(0);
            table.getColumnModel().getColumn(3).setMinWidth(0);
            table.getColumnModel().getColumn(3).setMaxWidth(0);
        }

        sorter = new TableRowSorter<TableModel>(tModel);
        table.setRowSorter(sorter);
    }

    /**
     * Вносит изменения в данные или компоненты формы в соответствии с типом
     *
     * @param type
     */
    private void factorForm(String type, String typeForm) {
        try {
            if (typeForm.equals(UtilProduct.SELECT_TO)) {

                editPanel.add(new JLabel("Код из ст. справочника:"));
                editPanel.add(numEdit);
                editPanel.add(new JLabel("Допуск 1:"));
                editPanel.add(val1Edit);
                editPanel.add(new JLabel("Допуск 2:"));
                editPanel.add(val2Edit);

            }

            if (type.equals(UtilProduct.TYPE_TO_GENERAL)) {
                title.setText("Обмеры основные");

            } else if (type.equals(UtilProduct.TYPE_TO_RST_ON)) {
                title.setText("Обмеры зависящие от роста");

            } else if (type.equals(UtilProduct.TYPE_TO_RST_OFF)) {
                title.setText("Обмеры не зависящие от роста");

            } else if (type.equals(UtilProduct.TYPE_IZD_NAME)) {
                title.setText("Наименование");

                jMenuBar.setVisible(false);

                table.getColumnModel().getColumn(0).setMinWidth(0);
                table.getColumnModel().getColumn(0).setMaxWidth(0);

            } else if (type.equals(UtilProduct.TYPE_IZD_BRAND)) {
                title.setText("Колекции");

            } else if (type.equals(UtilProduct.TYPE_IZD_IZM)) {
                title.setText("Ед. измерения");

            } else if (type.equals(UtilProduct.TYPE_IZD_TIP)) {
                title.setText("Тип продукции");

            } else if (type.equals(UtilProduct.TYPE_IZD_VID)) {
                title.setText("Вид продукции");

            } else if (type.equals(UtilProduct.TYPE_IZD_GROUP)) {
                title.setText("Группа продукции");

            } else if (type.equals(UtilProduct.TYPE_IZD_ASSORT)) {
                title.setText("Ассортимент продукции");

            } else if (type.equals(UtilProduct.TYPE_IZD_KOD_SEG)) {
                title.setText("GPC сегмент");

            } else if (type.equals(UtilProduct.TYPE_IZD_KOD_SEM)) {
                title.setText("GPC семейство");

            } else if (type.equals(UtilProduct.TYPE_IZD_KOD_KL)) {
                title.setText("GPC класс");

            } else if (type.equals(UtilProduct.TYPE_IZD_KOD_BR)) {
                title.setText("GPC брик");

            } else if (type.equals(UtilProduct.TYPE_IZD_KOD_GOST)) {
                title.setText("ГОСТ");

            } else if (type.equals(UtilProduct.TYPE_IZD_KOD_TNV)) {
                title.setText("ТНВЭД");

            } else if (type.equals(UtilProduct.TYPE_IZD_KOD_OKRB)) {
                title.setText("ОКРБ");

            } else if (type.equals(UtilProduct.TYPE_EMPL_DESIGNER)) {
                title.setText("Художник");

            } else if (type.equals(UtilProduct.TYPE_EMPL_MASTER)) {
                title.setText("Конструктор");

            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Ошибка! " + e.getMessage(),
                    "Ошибка",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private Vector getData(final String type) {
        row = new Vector();
        try {
            pdb = new ProductPDB();
            pb = new ProgressBar(this, false, "Сбор данных...");
            final SwingWorker sw = new SwingWorker() {
                @Override
                protected Object doInBackground() throws Exception {
                    try {
                        pb.setMessage("Сбор данных ...");

                        if (type.equals(UtilProduct.TYPE_TO_GENERAL)) {
                            row = pdb.getTehOp(UtilProduct.KOD_TO_GENERAL);

                        } else if (type.equals(UtilProduct.TYPE_TO_RST_ON)) {
                            row = pdb.getTehOp(UtilProduct.KOD_TO_RST_ON);

                        } else if (type.equals(UtilProduct.TYPE_TO_RST_OFF)) {
                            row = pdb.getTehOp(UtilProduct.KOD_TO_RST_OFF);

                        } else if (type.equals(UtilProduct.TYPE_IZD_NAME)) {
                            row = pdb.getIzdName();

                        } else if (type.equals(UtilProduct.TYPE_IZD_BRAND)) {
                            row = pdb.getIzdBrand();

                        } else if (type.equals(UtilProduct.TYPE_IZD_IZM)) {
                            row = pdb.getIzdIzm();

                        } else if (type.equals(UtilProduct.TYPE_IZD_TIP)) {
                            row = pdb.getIzdTip();

                        } else if (type.equals(UtilProduct.TYPE_IZD_VID)) {
                            row = pdb.getIzdVid();

                        } else if (type.equals(UtilProduct.TYPE_IZD_GROUP)) {
                            row = pdb.getIzdGroup();

                        } else if (type.equals(UtilProduct.TYPE_IZD_ASSORT)) {
                            row = pdb.getIzdAssort();

                        } else if (type.equals(UtilProduct.TYPE_IZD_KOD_SEG)) {
                            row = pdb.getIzdKod(UtilProduct.KOD_SEG);

                        } else if (type.equals(UtilProduct.TYPE_IZD_KOD_SEM)) {
                            row = pdb.getIzdKod(UtilProduct.KOD_SEM);

                        } else if (type.equals(UtilProduct.TYPE_IZD_KOD_KL)) {
                            row = pdb.getIzdKod(UtilProduct.KOD_KL);

                        } else if (type.equals(UtilProduct.TYPE_IZD_KOD_BR)) {
                            row = pdb.getIzdKod(UtilProduct.KOD_BR);

                        } else if (type.equals(UtilProduct.TYPE_IZD_KOD_GOST)) {
                            row = pdb.getIzdKod(UtilProduct.KOD_GOST);

                        } else if (type.equals(UtilProduct.TYPE_IZD_KOD_TNV)) {
                            row = pdb.getIzdKod(UtilProduct.KOD_TNV);

                        } else if (type.equals(UtilProduct.TYPE_IZD_KOD_OKRB)) {
                            row = pdb.getIzdKod(UtilProduct.KOD_OKRB);

                        } else if (type.equals(UtilProduct.TYPE_EMPL_DESIGNER)) {
                            row = pdb.getEmpl(UtilProduct.KOD_DESIGNER);

                        } else if (type.equals(UtilProduct.TYPE_EMPL_MASTER)) {
                            row = pdb.getEmpl(UtilProduct.KOD_MASTER);

                        }
                    } catch (Exception e) {
                        row = new Vector();
                        JOptionPane.showMessageDialog(null,
                                "Сбой обновления! " + e.getMessage(),
                                "Ошибка",
                                javax.swing.JOptionPane.ERROR_MESSAGE);
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
        } catch (Exception e) {
            row = new Vector();
            JOptionPane.showMessageDialog(null,
                    "Ошибка! " + e.getMessage(),
                    "Ошибка",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
        } finally {
            pdb.disConn();
        }
        return row;
    }

    private void showEditPanel(int type) {
        try {
            boolean flag = false;

            idEdit.setText("");
            nameEdit.setText("");
            noteEdit.setText("");
            numEdit.setText("");
            val1Edit.setText("");
            val2Edit.setText("");

            if (JOptionPane.showOptionDialog(
                    null,
                    editPanel,
                    type == 0 ? "Добавить запись" : "Изменить запись" + " : " + title.getText(),
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.INFORMATION_MESSAGE,
                    null,
                    new Object[]{"Сохранить", "Отмена"},
                    "Сохранить") == JOptionPane.YES_OPTION) {
                try {
                    if (type == 0) {
                        if (addNewItem(
                                typeSearch,
                                nameEdit.getText().trim().toUpperCase(),
                                noteEdit.getText().trim().toUpperCase())) {

                            JOptionPane.showMessageDialog(null,
                                    "Запись успешно добавлена! ",
                                    "Завершено",
                                    javax.swing.JOptionPane.INFORMATION_MESSAGE);

                            flag = true;
                        }
                    } else if (type == 1) {
                        if (editItem(
                                typeSearch,
                                Integer.valueOf(idEdit.getText().trim()),
                                nameEdit.getText().trim().toUpperCase(),
                                noteEdit.getText().trim().toUpperCase())) {

                            JOptionPane.showMessageDialog(null,
                                    "Запись успешно изменена! ",
                                    "Завершено",
                                    javax.swing.JOptionPane.INFORMATION_MESSAGE);

                            flag = true;
                        }
                    } else if (type == 2) {
                        if (addNewItemTO(
                                typeSearch,
                                nameEdit.getText().trim().toUpperCase(),
                                noteEdit.getText().trim().toUpperCase(),
                                numEdit.getText().trim(),
                                Double.valueOf(val1Edit.getText().trim()),
                                Double.valueOf(val2Edit.getText().trim()))) {

                            JOptionPane.showMessageDialog(null,
                                    "Запись успешно добавлена! ",
                                    "Завершено",
                                    javax.swing.JOptionPane.INFORMATION_MESSAGE);

                            flag = true;
                        }
                    } else if (type == 3) {
                        if (editItemTO(
                                typeSearch,
                                Integer.valueOf(idEdit.getText().trim()),
                                nameEdit.getText().trim().toUpperCase(),
                                noteEdit.getText().trim().toUpperCase(),
                                numEdit.getText().trim(),
                                Double.valueOf(val1Edit.getText().trim()),
                                Double.valueOf(val2Edit.getText().trim()))) {

                            JOptionPane.showMessageDialog(null,
                                    "Запись успешно добавлена! ",
                                    "Завершено",
                                    javax.swing.JOptionPane.INFORMATION_MESSAGE);

                            flag = true;
                        }
                    }

                } catch (Exception e) {
                    flag = false;

                    JOptionPane.showMessageDialog(
                            null,
                            "Ошибка! " + e.getMessage(),
                            "Ошибка",
                            javax.swing.JOptionPane.ERROR_MESSAGE);
                }

                if (flag)
                    createTable(getData(typeSearch));
            }
        } catch (Exception e) {
            idEdit.setText("");
            nameEdit.setText("");
            noteEdit.setText("");
            numEdit.setText("");
            val1Edit.setText("");
            val2Edit.setText("");

            JOptionPane.showMessageDialog(null,
                    "Ошибка. " + e.getMessage(),
                    "Ошибка",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean addNewItem(String type,
                               String name,
                               String note) throws Exception {

        boolean rez = false;
        try {
            pdb = new ProductPDB();

            if (type.equals(UtilProduct.TYPE_IZD_BRAND)) {
                rez = pdb.addIzdBrand(name, note);

            } else if (type.equals(UtilProduct.TYPE_IZD_IZM)) {
                rez = pdb.addIzdIzm(name, note);

            } else if (type.equals(UtilProduct.TYPE_IZD_TIP)) {
                rez = pdb.addIzdTip(name, note);

            } else if (type.equals(UtilProduct.TYPE_IZD_VID)) {
                rez = pdb.addIzdVid(name, note);

            } else if (type.equals(UtilProduct.TYPE_IZD_GROUP)) {
                rez = pdb.addIzdGroup(name, note);

            } else if (type.equals(UtilProduct.TYPE_IZD_ASSORT)) {
                rez = pdb.addIzdAssort(name, note);

            } else if (type.equals(UtilProduct.TYPE_IZD_KOD_SEG)) {
                rez = pdb.addKod(name, note, UtilProduct.KOD_SEG);

            } else if (type.equals(UtilProduct.TYPE_IZD_KOD_SEM)) {
                rez = pdb.addKod(name, note, UtilProduct.KOD_SEM);

            } else if (type.equals(UtilProduct.TYPE_IZD_KOD_KL)) {
                rez = pdb.addKod(name, note, UtilProduct.KOD_KL);

            } else if (type.equals(UtilProduct.TYPE_IZD_KOD_BR)) {
                rez = pdb.addKod(name, note, UtilProduct.KOD_BR);

            } else if (type.equals(UtilProduct.TYPE_IZD_KOD_GOST)) {
                rez = pdb.addKod(name, note, UtilProduct.KOD_GOST);

            } else if (type.equals(UtilProduct.TYPE_IZD_KOD_TNV)) {
                rez = pdb.addKod(name, note, UtilProduct.KOD_TNV);

            } else if (type.equals(UtilProduct.TYPE_IZD_KOD_OKRB)) {
                rez = pdb.addKod(name, note, UtilProduct.KOD_OKRB);

            } else if (type.equals(UtilProduct.TYPE_EMPL_DESIGNER)) {
                rez = pdb.addEmpl(name, note, UtilProduct.KOD_DESIGNER);

            } else if (type.equals(UtilProduct.TYPE_EMPL_MASTER)) {
                rez = pdb.addEmpl(name, note, UtilProduct.KOD_MASTER);

            }

        } catch (Exception e) {
            System.err.println("Ошибка addNewItem() " + e);
            throw new Exception("Ошибка addNewItem() " + e.getMessage(), e);
        } finally {
            pdb.disConn();
        }
        return rez;
    }

    private boolean editItem(String type,
                             int id,
                             String name,
                             String note) throws Exception {
        boolean rez = false;
        try {
            pdb = new ProductPDB();

            if (type.equals(UtilProduct.TYPE_IZD_BRAND)) {
                rez = pdb.editBrand(id, name, note);
            }

        } catch (Exception e) {
            System.err.println("Ошибка editItem() " + e);
            throw new Exception("Ошибка editItem() " + e.getMessage(), e);
        } finally {
            pdb.disConn();
        }
        return rez;
    }

    private boolean addNewItemTO(String type,
                                 String name,
                                 String note,
                                 String num,
                                 Double val1,
                                 Double val2) throws Exception {

        boolean rez = false;
        try {
            pdb = new ProductPDB();

            if (type.equals(UtilProduct.TYPE_TO_GENERAL)) {
                rez = pdb.addTehOp(num, name, val1, val2, note, UtilProduct.KOD_TO_GENERAL);

            } else if (type.equals(UtilProduct.TYPE_TO_RST_OFF)) {
                rez = pdb.addTehOp(num, name, val1, val2, note, UtilProduct.KOD_TO_RST_OFF);

            } else if (type.equals(UtilProduct.TYPE_TO_RST_ON)) {
                rez = pdb.addTehOp(num, name, val1, val2, note, UtilProduct.KOD_TO_RST_ON);
            }

        } catch (Exception e) {
            System.err.println("Ошибка addNewItemTO() " + e);
            throw new Exception("Ошибка addNewItemTO() " + e.getMessage(), e);
        } finally {
            pdb.disConn();
        }
        return rez;
    }

    private boolean editItemTO(String type,
                               int id,
                               String name,
                               String note,
                               String num,
                               Double val1,
                               Double val2) throws Exception {
        boolean rez = false;
        try {
            pdb = new ProductPDB();

            if (type.equals(UtilProduct.TYPE_TO_GENERAL)) {
                rez = pdb.editTehOp(id, num, name, val1, val2, note, UtilProduct.KOD_TO_GENERAL);

            }

        } catch (Exception e) {
            System.err.println("Ошибка editItemTO() " + e);
            throw new Exception("Ошибка editItemTO() " + e.getMessage(), e);
        } finally {
            pdb.disConn();
        }
        return rez;
    }
}
