package dept.production.planning.ean;

import by.march8.ecs.MainController;
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
import java.util.Vector;

/**
 * Класс для проверки заявок на повторы. 
 * Варианты проверки: по модели, по артикулу (без сорта, только по сорту, сорт и размер). 
 * 8
 * @author lidashka
 */
public class CheckEanList extends JDialog {
    private JPanel osnovaPanel;
    private JPanel buttPanel;
    private JPanel headPanel;
    private JButton buttSearch;
    private JButton buttClose;
    private JButton buttOpen;
    private JLabel title;
    private JCheckBox box1;
    private JCheckBox box2;
    private JRadioButton box3;
    private JRadioButton box4;
    private JRadioButton box5;
    private ButtonGroup buttonGroupBox;
    private Vector col;
    private Vector row;
    private JTable table;
    private DefaultTableModel tModel;
    private TableRowSorter sorter;

    private EanPDB epdb;
    private ProgressBar pb;

    private MainController controller;

    public CheckEanList(MainController mainController, boolean modal) {
        super(mainController.getMainForm(), modal);

        init();
        initData();

        this.setTitle("Проверка");
        this.setLocationRelativeTo(controller.getMainForm());
        this.setVisible(true);
    }

    private void init() {
        setMinimumSize(new Dimension(550, 400));
        setPreferredSize(new Dimension(800, 600));

        osnovaPanel = new JPanel();
        osnovaPanel.setLayout(new BorderLayout(1, 1));
        osnovaPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        headPanel = new JPanel();
        headPanel.setLayout(new ParagraphLayout());

        buttPanel = new JPanel();
        buttPanel.setLayout(new GridLayout(0, 4, 5, 5));
        buttPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        buttPanel.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);

        buttClose = new JButton("Закрыть");
        buttClose.addActionListener(evt -> buttCloseActionPerformed(evt));

        buttOpen = new JButton("Открыть");
        buttOpen.addActionListener(evt -> buttOpenActionPerformed(evt));

        buttSearch = new JButton("Проверить");
        buttSearch.addActionListener(evt -> buttSearchActionPerformed(evt));

        title = new JLabel("");
        title.setHorizontalAlignment(JLabel.CENTER);
        title.setFont(new Font("serif", Font.PLAIN, 24));

        box1 = new JCheckBox();
        box1.setFont(new java.awt.Font("Dialog", 0, 13));
        box1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        box1.setText("модель;");
        box1.setSelected(true);

        box2 = new JCheckBox();
        box2.setFont(new java.awt.Font("Dialog", 0, 13));
        box2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        box2.setText("артикул;");
        box2.setSelected(true);

        box3 = new JRadioButton();
        box3.setFont(new java.awt.Font("Dialog", 0, 13));
        box3.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        box3.setText("без сорта;");
        box3.setActionCommand("0");

        box4 = new JRadioButton();
        box4.setFont(new java.awt.Font("Dialog", 0, 13));
        box4.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        box4.setText("сорт;");
        box4.setActionCommand("1");

        box5 = new JRadioButton();
        box5.setFont(new java.awt.Font("Dialog", 0, 13));
        box5.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        box5.setText("сорт, размер;");
        box5.setActionCommand("2");
        box5.setSelected(true);

        buttonGroupBox = new ButtonGroup();
        buttonGroupBox.add(box3);
        buttonGroupBox.add(box4);
        buttonGroupBox.add(box5);

        col = new Vector();
        col.add("Код");
        col.add("Уч.номер");
        col.add("Наименование");
        col.add("Модель");
        col.add("Артикул");
        col.add("Сорт");
        col.add("Рост");
        col.add("Размер");

        table = new JTable();
        table.setAutoCreateColumnsFromModel(true);
        table.getTableHeader().setReorderingAllowed(false);
        new TableFilterHeader(table, AutoChoices.ENABLED);

        tModel = new DefaultTableModel();

        headPanel.add(title, ParagraphLayout.NEW_LINE_STRETCH_H);
        headPanel.add(new JLabel("Повторы:"), ParagraphLayout.NEW_PARAGRAPH);
        headPanel.add(box1);
        headPanel.add(box2);
        headPanel.add(new JLabel(""), ParagraphLayout.NEW_PARAGRAPH);
        headPanel.add(box3);
        headPanel.add(box4);
        headPanel.add(box5);
        headPanel.add(new JLabel("      "));
        headPanel.add(buttSearch);

        buttPanel.add(buttClose);
        //buttPanel.add(buttOpen);

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
            createTable(getData());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Ошибка " + e.getMessage(),
                    "Ошибка",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buttOpenActionPerformed(ActionEvent evt) {
        try {
            if (table.getSelectedRow() != -1) {
                new EanDetalForm(controller, true, new EanList(), UtilEan.OPEN);

            } else
                JOptionPane.showMessageDialog(null,
                        "Вы ничего не выбрали!",
                        "Внимание",
                        javax.swing.JOptionPane.WARNING_MESSAGE);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Ошибка " + e.getMessage(),
                    "Ошибка",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void initData() {
        createTable(new Vector());
    }

    private Vector getData() {
        row = new Vector();
        try {
            epdb = new EanPDB();
            pb = new ProgressBar(this, false, "Сбор данных...");
            final SwingWorker sw = new SwingWorker() {
                @Override
                protected Object doInBackground() throws Exception {
                    try {
                        pb.setMessage("Сбор данных ...");

                        switch (Integer.valueOf(buttonGroupBox.getSelection().getActionCommand())) {
                            case 0:
                                row = epdb.getCheckEanlist(
                                        box1.isSelected(),
                                        box2.isSelected());
                                break;
                            case 1:
                                row = epdb.getCheckEanlistFasSrt(
                                        box1.isSelected(),
                                        box2.isSelected());
                                break;
                            case 2:
                                row = epdb.getCheckEanlistFasRzm(
                                        box1.isSelected(),
                                        box2.isSelected());
                                break;
                            default:
                                row = epdb.getCheckEanlist(
                                        box1.isSelected(),
                                        box2.isSelected());
                                break;
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

    private void createTable(final Vector row) {
        tModel = new DefaultTableModel(row, col) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };

        table.setModel(tModel);
        table.setAutoCreateColumnsFromModel(true);

        sorter = new TableRowSorter<TableModel>(tModel);
        table.setRowSorter(sorter);
        table.getColumnModel().getColumn(0).setPreferredWidth(15);
        table.getColumnModel().getColumn(1).setPreferredWidth(30);
        table.getColumnModel().getColumn(2).setPreferredWidth(130);
    }

}
