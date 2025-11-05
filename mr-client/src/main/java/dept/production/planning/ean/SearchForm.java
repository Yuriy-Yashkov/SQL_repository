package dept.production.planning.ean;

import com.jhlabs.awt.ParagraphLayout;
import common.ProgressBar;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Vector;

/**
 * Форма для поиска кодов классификации, вида упаковки, цвета.
 *
 * @author lidashka
 */
public class SearchForm extends JDialog {
    private final String typeSearch;
    private JPanel osnovaPanel;
    private JPanel centrPanel;
    private JPanel buttPanel;
    private JPanel headPanel;
    private JPanel footPanel;
    private JButton buttSearch;
    private JButton buttClose;
    private JButton buttSelect;
    private JLabel title;
    private JTextField searchText;
    private JTextField otherText;
    private JCheckBox checkbox;
    private Vector col;
    private Vector row;
    private JTable table;
    private DefaultTableModel tModel;
    private TableRowSorter sorter;
    private EanPDB epdb;
    private ProgressBar pb;
    private EanDB edb;

    /**
     * Конструктор поиска кода или цвета, проверка артикула.
     * @param parent
     * @param modal
     * @param type
     * @param nar
     */
    public SearchForm(java.awt.Dialog parent, boolean modal, String type, String nar) {
        super(parent, modal);

        typeSearch = type;

        cleanConstants();
        initMenu();
        initPropSetting();
        init(type);
        initData(type, nar);

        this.setTitle("Поиск");
        this.setLocationRelativeTo(parent);
        this.setVisible(true);
    }

    private void cleanConstants() {
        UtilEan.ACTION_BUTT_SELECT = false;
        UtilEan.ITEM_ADD_SELECT_ITEM = "";
        UtilEan.ITEM_ADD_SELECT_ITEM_ID = "";
    }

    private void initMenu() {
    }

    private void initPropSetting() {
    }

    private void init(String type) {
        setMinimumSize(new Dimension(350, 450));
        setPreferredSize(new Dimension(350, 480));

        if (type.equals(UtilEan.SELECT_COLOR))
            setPreferredSize(new Dimension(350, 530));

        osnovaPanel = new JPanel();
        osnovaPanel.setLayout(new BorderLayout(1, 1));
        osnovaPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        centrPanel = new JPanel();
        centrPanel.setLayout(new BorderLayout(1, 1));

        headPanel = new JPanel();
        headPanel.setLayout(new ParagraphLayout());

        footPanel = new JPanel();
        footPanel.setLayout(new ParagraphLayout());

        buttPanel = new JPanel();
        buttPanel.setLayout(new GridLayout(0, 3, 5, 5));
        buttPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        buttPanel.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);

        buttClose = new JButton("Закрыть");
        buttClose.addActionListener(evt -> buttCloseActionPerformed(evt));

        buttSelect = new JButton("Выбрать");
        buttSelect.addActionListener(evt -> buttSelectActionPerformed(evt));

        buttSearch = new JButton("Найти");
        buttSearch.addActionListener(evt -> buttSearchActionPerformed(evt));

        title = new JLabel("");
        title.setHorizontalAlignment(JLabel.CENTER);
        title.setFont(new Font("serif", Font.PLAIN, 24));

        searchText = new JTextField();
        searchText.setPreferredSize(new Dimension(250, 20));
        searchText.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyPressed(java.awt.event.KeyEvent evt) {
                if (evt.getKeyCode() == evt.VK_ENTER) buttSearch.doClick();
            }
        });

        otherText = new JTextField();
        otherText.setPreferredSize(new Dimension(250, 20));
        otherText.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyPressed(java.awt.event.KeyEvent evt) {
                if (evt.getKeyCode() == evt.VK_ENTER) {
                    checkbox.setSelected(true);
                    buttSelect.doClick();
                }
            }
        });

        checkbox = new JCheckBox();
        checkbox.setText("Другой");

        col = new Vector();
        col.add("Наименование");

        if (type.equals(UtilEan.CHECK_FAS_NAR)) {
            col = new Vector();
            col.add("Наименование");
            col.add("Модель");
            col.add("Артикул");
            col.add("Шифр");
        }

        if (type.equals(UtilEan.SELECT_COLOR)) {
            col = new Vector();
            col.add("ID");
            col.add("Наименование");
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

        headPanel.add(new JLabel(""), ParagraphLayout.NEW_PARAGRAPH);
        headPanel.add(title, ParagraphLayout.NEW_LINE_STRETCH_H);
        headPanel.add(new JLabel(""), ParagraphLayout.NEW_PARAGRAPH);
        headPanel.add(searchText, ParagraphLayout.NEW_LINE_STRETCH_H);
        headPanel.add(buttSearch);

        footPanel.add(checkbox, ParagraphLayout.NEW_PARAGRAPH);
        footPanel.add(otherText, ParagraphLayout.NEW_LINE_STRETCH_H);

        centrPanel.add(new JScrollPane(table), BorderLayout.CENTER);
        centrPanel.add(footPanel, BorderLayout.SOUTH);

        buttPanel.add(buttClose);
        buttPanel.add(buttSelect);

        if (type.equals(UtilEan.CHECK_FAS_NAR)) {
            footPanel.setVisible(false);
            buttSelect.setVisible(false);
        }

        if (type.equals(UtilEan.SELECT_COLOR)) {
            footPanel.setVisible(false);
        }

        osnovaPanel.add(headPanel, BorderLayout.NORTH);
        osnovaPanel.add(centrPanel, BorderLayout.CENTER);
        osnovaPanel.add(buttPanel, BorderLayout.SOUTH);

        getContentPane().add(osnovaPanel);
        pack();
    }

    private void buttCloseActionPerformed(ActionEvent evt) {
        dispose();
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

    private void buttSelectActionPerformed(ActionEvent evt) {
        try {
            if (checkbox.isSelected()) {

                if (typeSearch.equals(UtilEan.DATA_KOD_GPC_SEG) ||
                        typeSearch.equals(UtilEan.DATA_KOD_GPC_SEM) ||
                        typeSearch.equals(UtilEan.DATA_KOD_GPC_KL) ||
                        typeSearch.equals(UtilEan.DATA_KOD_GPC_BR)) {

                    try {
                        if (!otherText.getText().trim().equals("")) {
                            Integer.valueOf(otherText.getText().trim());

                            UtilEan.ACTION_BUTT_SELECT = true;
                            UtilEan.ITEM_ADD_SELECT_ITEM = otherText.getText().trim().toUpperCase();
                        } else {
                            JOptionPane.showMessageDialog(null,
                                    "GPC код не задан!",
                                    "Внимание",
                                    javax.swing.JOptionPane.WARNING_MESSAGE);
                        }
                    } catch (Exception e) {
                        cleanConstants();

                        JOptionPane.showMessageDialog(null,
                                "GPC код задан некорректно!",
                                "Внимание",
                                javax.swing.JOptionPane.WARNING_MESSAGE);
                    }

                } else {
                    UtilEan.ACTION_BUTT_SELECT = true;
                    UtilEan.ITEM_ADD_SELECT_ITEM = otherText.getText().trim().toUpperCase();
                }

            } else {
                if (table.getSelectedRow() != -1) {
                    UtilEan.ACTION_BUTT_SELECT = true;

                    if (typeSearch.equals(UtilEan.SELECT_COLOR)) {
                        UtilEan.ITEM_ADD_SELECT_ITEM_ID = table.getValueAt(table.getSelectedRow(), 0).toString();
                        UtilEan.ITEM_ADD_SELECT_ITEM = table.getValueAt(table.getSelectedRow(), 1).toString();
                    } else
                        UtilEan.ITEM_ADD_SELECT_ITEM = table.getValueAt(table.getSelectedRow(), 0).toString();


                } else
                    JOptionPane.showMessageDialog(null,
                            "Вы ничего не выбрали!",
                            "Внимание",
                            javax.swing.JOptionPane.WARNING_MESSAGE);
            }

            if (UtilEan.ACTION_BUTT_SELECT)
                dispose();
        } catch (Exception e) {
            cleanConstants();

            JOptionPane.showMessageDialog(null,
                    "Ошибка. " + e.getMessage(),
                    "Ошибка",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void initData(String type, String nar) {
        Vector data = new Vector();

        try {
            if (type.equals(UtilEan.CHECK_FAS_NAR))
                data = getDataDB(type, nar);
            else if (type.equals(UtilEan.SELECT_COLOR))
                data = getDataDB(type, "");
            else
                data = getDataPDB(type);

            if (type.equals(UtilEan.DATA_KOD_GPC_SEG)) {
                title.setText("GPC сегмент");
            } else if (type.equals(UtilEan.DATA_KOD_GPC_SEM)) {
                title.setText("GPC семейство");
            } else if (type.equals(UtilEan.DATA_KOD_GPC_KL)) {
                title.setText("GPC класс");
            } else if (type.equals(UtilEan.DATA_KOD_GPC_BR)) {
                title.setText("GPC брик");
            } else if (type.equals(UtilEan.DATA_KOD_OKRB)) {
                title.setText("Код по ОКРБ-007");
            } else if (type.equals(UtilEan.DATA_KOD_THB)) {
                title.setText("Код по ТНВЭДТ");
            } else if (type.equals(UtilEan.DATA_KOD_GOST)) {
                title.setText("ГОСТ");
            } else if (type.equals(UtilEan.CHECK_FAS_NAR)) {
                title.setText("Проверка артикула");
            }

        } catch (Exception e) {
            data = new Vector();
            JOptionPane.showMessageDialog(null,
                    "Ошибка! " + e.getMessage(),
                    "Ошибка",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
        }

        createTableSearch(data);

    }

    private Vector getDataPDB(final String type) {
        row = new Vector();
        try {
            epdb = new EanPDB();
            EanDB eanDB = new EanDB();
            pb = new ProgressBar(this, false, "Сбор данных...");
            final SwingWorker sw = new SwingWorker() {
                @Override
                protected Object doInBackground() throws Exception {
                    try {
                        pb.setMessage("Сбор данных ...");
                        if (type.equals(UtilEan.DATA_KOD_GPC_SEG)) {
                            row = epdb.getGPCSeg();
                        } else if (type.equals(UtilEan.DATA_KOD_GPC_SEM)) {
                            row = epdb.getGPCSem();
                        } else if (type.equals(UtilEan.DATA_KOD_GPC_KL)) {
                            row = epdb.getGPCKl();
                        } else if (type.equals(UtilEan.DATA_KOD_GPC_BR)) {
                            row = epdb.getGPCBr();
                        } else if (type.equals(UtilEan.DATA_KOD_OKRB)) {
                            row = epdb.getKodOkrb();
                        } else if (type.equals(UtilEan.DATA_KOD_THB)) {
                            row = eanDB.getKodThb();
                        } else if (type.equals(UtilEan.DATA_KOD_GOST)) {
                            row = epdb.getKodGost();
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
            epdb.disConn();
        }
        return row;
    }

    private void createTableSearch(final Vector row) {
        tModel = new DefaultTableModel(row, col) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };

        table.setModel(tModel);
        table.setAutoCreateColumnsFromModel(true);

        if (typeSearch.equals(UtilEan.SELECT_COLOR)) {
            table.getColumnModel().getColumn(0).setMinWidth(0);
            table.getColumnModel().getColumn(0).setMaxWidth(0);
        }

        sorter = new TableRowSorter<TableModel>(tModel);
        table.setRowSorter(sorter);
    }

    private Vector getDataDB(final String type, final String nar) {
        row = new Vector();
        try {
            edb = new EanDB();
            pb = new ProgressBar(this, false, "Сбор данных...");
            final SwingWorker sw = new SwingWorker() {
                @Override
                protected Object doInBackground() throws Exception {
                    try {
                        pb.setMessage("Сбор данных ...");
                        if (type.equals(UtilEan.CHECK_FAS_NAR)) {
                            if (!nar.equals(""))
                                row = edb.getCheckFasNar(nar);
                        } else if (type.equals(UtilEan.SELECT_COLOR)) {
                            row = edb.getColor();
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
            edb.disConn();
        }
        return row;
    }

}
