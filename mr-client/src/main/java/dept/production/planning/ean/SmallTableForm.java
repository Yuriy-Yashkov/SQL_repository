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
 *
 * @author lidashka
 */
public class SmallTableForm extends JDialog {
    private JPanel osnovaPanel;
    private JPanel headPanel;
    private JPanel buttPanel;
    private JButton buttClose;
    private JButton buttOpen;
    private JTable table;
    private TableFilterHeader filterHeader;
    private DefaultTableModel tModel;
    private TableRowSorter<TableModel> sorter;
    private Vector colShowDetal;
    private JButton buttPrint;
    private Vector row;
    private ProgressBar pb;
    private EanList dataEanlist;
    private Vector colShow;

    private MainController controller;

    public SmallTableForm(MainController mainController, boolean modal, Vector row) {
        super(mainController.getMainForm(), modal);
        controller = mainController;

        init();

        buttPanel.add(buttOpen);

        this.row = row;
        createTableShowEan(row);

        this.setTitle("Размеры подробно");
        this.setLocationRelativeTo(controller.getMainForm());
        this.setVisible(true);
    }

    public SmallTableForm(MainController mainController, boolean modal, Vector row, String date) {
        super(mainController.getMainForm(), modal);
        controller = mainController;

        init();

        this.row = row;
        createTable(row);

        this.setTitle("Маршрутные листы подробно с " + date);
        this.setLocationRelativeTo(controller.getMainForm());
        this.setVisible(true);
    }

    private void init() {
        setMinimumSize(new Dimension(550, 550));
        setPreferredSize(new Dimension(900, 680));

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

        buttOpen = new JButton("Открыть заявку");
        buttOpen.addActionListener(evt -> buttOpenActionPerformed(evt));

        buttPrint = new JButton("Печать");
        buttPrint.addActionListener(evt -> buttPrintActionPerformed(evt));

        colShowDetal = new Vector();
        colShowDetal.add("");
        colShowDetal.add("№ заявки");
        colShowDetal.add("Статус");
        colShowDetal.add("Дата");
        colShowDetal.add("Модель");
        colShowDetal.add("Артикул");
        colShowDetal.add("Сорт");
        colShowDetal.add("Цвет");
        colShowDetal.add("Размеры");
        colShowDetal.add("EAN13/ИМПОРТ");

        colShow = new Vector();
        colShow.add("");
        colShow.add("№");
        colShow.add("Дата");
        colShow.add("Бригадир");
        colShow.add("Модель");
        colShow.add("Артикул");
        colShow.add("Цвет");
        colShow.add("Всего");
        colShow.add("Рост");
        colShow.add("Размер");
        colShow.add("Кол-во");
        colShow.add("Полотно");

        table = new JTable();
        table.setAutoCreateColumnsFromModel(true);
        table.getTableHeader().setReorderingAllowed(false);
        filterHeader = new TableFilterHeader(table, AutoChoices.ENABLED);

        tModel = new DefaultTableModel();

        buttPanel.add(buttClose);
        buttPanel.add(buttPrint);

        osnovaPanel.add(new JScrollPane(table), BorderLayout.CENTER);
        osnovaPanel.add(buttPanel, BorderLayout.SOUTH);

        getContentPane().add(osnovaPanel);
        pack();
    }

    private void buttCloseActionPerformed(ActionEvent evt) {
        dispose();
    }

    private void buttOpenActionPerformed(ActionEvent evt) {
        try {
            if (table.getSelectedRow() != -1) {
                dataEanlist = new EanList();

                pb = new ProgressBar(this, false, "Сбор данных ...");
                SwingWorker sw = new SwingWorker() {
                    private EanPDB epdb;

                    protected Object doInBackground() {
                        try {
                            epdb = new EanPDB();

                            int idEanlist = Integer.valueOf(table.getValueAt(table.getSelectedRow(), 1).toString());

                            dataEanlist = epdb.getDataEanlist(idEanlist);

                        } catch (Exception e) {
                            dataEanlist = new EanList();
                            JOptionPane.showMessageDialog(null,
                                    "Данные заявки не загружены!\n " + e.getMessage(),
                                    "Ошибка",
                                    javax.swing.JOptionPane.ERROR_MESSAGE);
                        } finally {
                            epdb.disConn();
                        }
                        return null;
                    }

                    @Override
                    protected void done() {
                        pb.dispose();
                    }
                };
                sw.execute();
                pb.setVisible(true);

                new EanDetalForm(controller, true, dataEanlist, UtilEan.OPEN);
            } else
                JOptionPane.showMessageDialog(null,
                        "Вы ничего не выбрали!",
                        "Внимание",
                        javax.swing.JOptionPane.WARNING_MESSAGE);
        } catch (HeadlessException e) {
            JOptionPane.showMessageDialog(null,
                    "Ошибка " + e.getMessage(),
                    "Ошибка",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buttPrintActionPerformed(ActionEvent evt) {
        try {
            EanOO oo = new EanOO(
                    "",
                    tModel,
                    table.getColumnModel());
            oo.createReport("DefaultTableAlbumFormatCheck.ots", false);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Ошибка " + e.getMessage(),
                    "Ошибка",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void createTableShowEan(final Vector row) {
        tModel = new DefaultTableModel(row, colShowDetal) {
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

        table.getColumnModel().getColumn(0).setPreferredWidth(1);
        table.getColumnModel().getColumn(2).setPreferredWidth(45);
        table.getColumnModel().getColumn(3).setPreferredWidth(45);
        table.getColumnModel().getColumn(4).setPreferredWidth(30);
        table.getColumnModel().getColumn(5).setPreferredWidth(65);
        table.getColumnModel().getColumn(6).setPreferredWidth(1);
        table.getColumnModel().getColumn(7).setPreferredWidth(65);
        table.getColumnModel().getColumn(8).setPreferredWidth(90);
        table.getColumnModel().getColumn(9).setPreferredWidth(150);

        sorter = new TableRowSorter<>(tModel);
        table.setRowSorter(sorter);

        for (int i = 0; i < table.getRowCount(); i++) {
            int rowHeight = table.getRowHeight();

            Component comp = table.prepareRenderer(table.getCellRenderer(i, 9), i, 9);
            rowHeight = Math.max(rowHeight, comp.getPreferredSize().height);

            table.setRowHeight(i, rowHeight);
        }
    }

    private void createTable(final Vector row) {
        tModel = new DefaultTableModel(row, colShow) {
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

        sorter = new TableRowSorter<>(tModel);
        table.setRowSorter(sorter);
    }
}
