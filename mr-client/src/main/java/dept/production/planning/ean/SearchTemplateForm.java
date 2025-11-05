package dept.production.planning.ean;

import com.jhlabs.awt.ParagraphLayout;
import com.toedter.calendar.JDateChooser;
import common.ProgressBar;
import common.UtilFunctions;
import net.coderazzi.filters.gui.AutoChoices;
import net.coderazzi.filters.gui.TableFilterHeader;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Vector;

/**
 * Форма для поиска кодов классификации и вида упаковки.
 *
 * @author lidashka
 */

public class SearchTemplateForm extends JDialog {
    private final String typeSearch;
    private JPanel osnovaPanel;
    private JPanel buttPanel;
    private JPanel headPanel;
    private JButton buttSearch;
    private JButton buttClose;
    private JButton buttSelect;
    private JLabel title;
    private JTextField fasNum;
    private JTextField fasName;
    private Vector col;
    private Vector row;
    private JTable table;
    private DefaultTableModel tModel;
    private TableRowSorter sorter;
    private TableFilterHeader filterHeader;
    private EanPDB epdb;
    private ProgressBar pb;
    private JDateChooser dateSearch;

    public SearchTemplateForm(java.awt.Dialog parent, boolean modal, String type, String fas) {
        super(parent, modal);

        typeSearch = type;

        cleanConstants();
        initMenu();
        initPropSetting();
        init();
        initData(type, fas);

        this.setTitle("Поиск");
        this.setLocationRelativeTo(parent);
        this.setVisible(true);
    }

    private void cleanConstants() {
        UtilEan.ACTION_BUTT_SELECT = false;
        UtilEan.ITEM_ADD_FAS_KOD_DEFAULT = new Vector();
    }

    private void initMenu() {
    }

    private void initPropSetting() {
    }

    private void init() {
        setMinimumSize(new Dimension(450, 350));
        setPreferredSize(new Dimension(750, 450));

        osnovaPanel = new JPanel();
        osnovaPanel.setLayout(new BorderLayout(1, 1));
        osnovaPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        headPanel = new JPanel();
        headPanel.setLayout(new ParagraphLayout());

        buttPanel = new JPanel();
        buttPanel.setLayout(new GridLayout(0, 3, 5, 5));
        buttPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        buttPanel.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);

        dateSearch = new JDateChooser();
        dateSearch.setPreferredSize(new Dimension(150, 20));

        buttClose = new JButton("Закрыть");
        buttClose.addActionListener(evt -> buttCloseActionPerformed(evt));

        buttSelect = new JButton("Выбрать");
        buttSelect.addActionListener(evt -> buttSelectActionPerformed(evt));

        buttSearch = new JButton("Найти");
        buttSearch.addActionListener(evt -> buttSearchActionPerformed(evt));

        title = new JLabel("");
        title.setHorizontalAlignment(JLabel.CENTER);
        title.setFont(new Font("serif", Font.PLAIN, 24));

        fasNum = new JTextField();
        fasNum.setPreferredSize(new Dimension(150, 20));
        fasNum.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                if (evt.getKeyCode() == evt.VK_ENTER) buttSearch.doClick();
            }
        });

        fasName = new JTextField();
        fasName.setPreferredSize(new Dimension(250, 20));
        fasName.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                if (evt.getKeyCode() == evt.VK_ENTER) buttSearch.doClick();
            }
        });

        col = new Vector();
        col.add("Модель");
        col.add("Наименование");
        col.add("gpc сег.");
        col.add("gpc сем.");
        col.add("gpc кл.");
        col.add("gpc бр.");
        col.add("gpc окрб.");
        col.add("gpc тнв.");
        col.add("gpc гост.");

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
        filterHeader = new TableFilterHeader(table, AutoChoices.ENABLED);

        tModel = new DefaultTableModel();

        headPanel.add(new JLabel(""), ParagraphLayout.NEW_PARAGRAPH);
        headPanel.add(title, ParagraphLayout.NEW_LINE_STRETCH_H);
        headPanel.add(new JLabel("Дата с: "), ParagraphLayout.NEW_PARAGRAPH);
        headPanel.add(dateSearch);
        headPanel.add(new JLabel("Модель: "), ParagraphLayout.NEW_PARAGRAPH);
        headPanel.add(fasNum);
        headPanel.add(fasName, ParagraphLayout.NEW_LINE_STRETCH_H);
        headPanel.add(buttSearch);

        buttPanel.add(buttClose);
        buttPanel.add(buttSelect);

        osnovaPanel.add(headPanel, BorderLayout.NORTH);
        osnovaPanel.add(new JScrollPane(table), BorderLayout.CENTER);
        osnovaPanel.add(buttPanel, BorderLayout.SOUTH);

        getContentPane().add(osnovaPanel);
        pack();
    }

    private void buttCloseActionPerformed(ActionEvent evt) {
        dispose();
    }

    private void buttSearchActionPerformed(ActionEvent evt) {
        try {

            createTableSearch(
                    getData(
                            typeSearch,
                            fasNum.getText().trim(),
                            fasName.getText().trim(),
                            new SimpleDateFormat("dd.MM.yyyy").format(dateSearch.getDate()))
            );

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Ошибка " + e.getMessage(),
                    "Ошибка",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buttSelectActionPerformed(ActionEvent evt) {
        try {
            if (table.getSelectedRow() != -1) {

                UtilEan.ACTION_BUTT_SELECT = true;

                for (int i = 2; i < table.getColumnCount(); i++) {
                    UtilEan.ITEM_ADD_FAS_KOD_DEFAULT.add(table.getValueAt(table.getSelectedRow(), i));
                }

                dispose();
            } else
                JOptionPane.showMessageDialog(null,
                        "Вы ничего не выбрали!",
                        "Внимание",
                        javax.swing.JOptionPane.WARNING_MESSAGE);

        } catch (HeadlessException e) {
            cleanConstants();

            JOptionPane.showMessageDialog(null,
                    "Ошибка. " + e.getMessage(),
                    "Ошибка",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void initData(String type, String fas) {
        title.setText("ШАБЛОН");

        fasNum.setText(fas);
        fasNum.requestFocusInWindow();

        try {
            dateSearch.setDate(new SimpleDateFormat("dd.MM.yyyy").parse("01.01.2017"));

        } catch (ParseException e) {
            java.util.Date d = (Calendar.getInstance()).getTime();
            d.setDate(1);

            dateSearch.setDate(d);
        }

        Vector data = new Vector();
        try {
            data = getData(type, fas, "", new SimpleDateFormat("dd.MM.yyyy").format(dateSearch.getDate()));
        } catch (Exception e) {
            data = new Vector();
            JOptionPane.showMessageDialog(null,
                    "Ошибка! " + e.getMessage(),
                    "Ошибка",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
        }

        createTableSearch(data);
    }

    private Vector getData(final String type, final String fas, final String fasName, final String date) {
        row = new Vector();
        try {
            epdb = new EanPDB();
            pb = new ProgressBar(this, false, "Сбор данных...");
            final SwingWorker sw = new SwingWorker() {
                @Override
                protected Object doInBackground() throws Exception {
                    try {
                        pb.setMessage("Сбор данных ...");
                        if (type.equals(UtilEan.DATA_KOD_DEFAULT)) {
                            row = epdb.getKodTemplates(
                                    fas,
                                    fasName,
                                    UtilFunctions.convertDateStrToLong(date)
                            );
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
        table.getColumnModel().getColumn(0).setPreferredWidth(50);
        table.getColumnModel().getColumn(1).setPreferredWidth(120);

        sorter = new TableRowSorter<TableModel>(tModel);
        table.setRowSorter(sorter);
    }

}

