package dept.production.planning.ean;

import by.march8.ecs.MainController;
import com.jhlabs.awt.ParagraphLayout;
import common.ProgressBar;
import net.coderazzi.filters.gui.AutoChoices;
import net.coderazzi.filters.gui.TableFilterHeader;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 *
 * @author lidashka
 */
public class EanImportForm extends JDialog {
    private JPanel osnovaPanel;
    private JPanel headPanel;
    private JPanel buttPanel;
    private JButton buttClose;
    private JButton buttImport;
    private JButton buttPrint;
    private JButton buttDone;
    private JTable table;
    private DefaultTableModel tModel;
    private TableRowSorter<TableModel> sorter;
    private Object[] colImport;
    private ProgressBar pb;
    private EanList dataEanlist;

    private int minSelectedRow;
    private int maxSelectedRow;
    private boolean tableModelListenerIsChanging;

    private EanPDB epdb;
    private EanDB edb;

    private JLabel title;
    private DefaultTableCellRenderer renderer;

    private MainController controller;

    public EanImportForm(MainController mainController, boolean modal, ArrayList<EanList> eanLists, String title) {
        super(mainController.getMainForm(), modal);
        controller = mainController;

        init();
        initData(eanLists, title);

        this.setTitle("Импорт EAN-кодов");
        this.setLocationRelativeTo(controller.getMainForm());
        this.setVisible(true);
    }

    private void init() {
        setMinimumSize(new Dimension(550, 550));
        setPreferredSize(new Dimension(1000, 700));

        title = new JLabel();
        title.setPreferredSize(new Dimension(150, 20));
        title.setHorizontalAlignment(JLabel.CENTER);
        title.setFont(new Font("serif", Font.PLAIN, 16));

        osnovaPanel = new JPanel();
        osnovaPanel.setLayout(new BorderLayout(1, 1));
        osnovaPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        headPanel = new JPanel();
        headPanel.setLayout(new ParagraphLayout());

        buttPanel = new JPanel();
        buttPanel.setLayout(new GridLayout(0, 5, 5, 5));
        buttPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        buttPanel.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);

        buttClose = new JButton("Закрыть");
        buttClose.addActionListener(evt -> buttCloseActionPerformed(evt));

        buttImport = new JButton("Импортировать");
        buttImport.addActionListener(evt -> buttImportActionPerformed(evt));

        buttDone = new JButton("Сохранить");
        buttDone.addActionListener(evt -> buttDoneActionPerformed(evt));

        buttPrint = new JButton("Печать");
        buttPrint.addActionListener(evt -> buttPrintActionPerformed(evt));

        colImport = new Object[16];
        colImport[0] = "";
        colImport[1] = "№";
        colImport[2] = "Заявка";
        colImport[3] = "Название";
        colImport[4] = "Модель";
        colImport[5] = "Артикул";
        colImport[6] = "Сорт";
        colImport[7] = "ID Цвет";
        colImport[8] = "Цвет";
        colImport[9] = "Рост";
        colImport[10] = "Размер";
        colImport[11] = "Этикетка";
        colImport[12] = "EAN13";
        colImport[13] = "Импорт";
        colImport[14] = "EanItem";
        colImport[15] = "Примечание";

        table = new JTable();
        table.setAutoCreateColumnsFromModel(true);
        table.getTableHeader().setReorderingAllowed(false);
        table.getSelectionModel().addListSelectionListener(e -> {
            if (e.getValueIsAdjusting()) {
                return;
            }
            minSelectedRow = (((DefaultListSelectionModel) e.getSource()).getMinSelectionIndex()
                    == ((DefaultListSelectionModel) e.getSource()).getMaxSelectionIndex()) ?
                    -1 : ((DefaultListSelectionModel) e.getSource()).getMinSelectionIndex();
            maxSelectedRow = (((DefaultListSelectionModel) e.getSource()).getMinSelectionIndex()
                    == ((DefaultListSelectionModel) e.getSource()).getMaxSelectionIndex()) ?
                    -1 : ((DefaultListSelectionModel) e.getSource()).getMaxSelectionIndex();
        });
        table.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() > 1) {
                    showPopupMenuTablePerformed();
                }
            }
        });

        final JPopupMenu popupMenu = new JPopupMenu();
        JMenuItem showItem = new JMenuItem("Открыть заявку");
        showItem.addActionListener(evt -> showPopupMenuTablePerformed());
        popupMenu.add(showItem);
        table.setComponentPopupMenu(popupMenu);

        new TableFilterHeader(table, AutoChoices.ENABLED);

        tModel = new DefaultTableModel();

        renderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                try {
                    if ((Boolean) table.getValueAt(row, 13))
                        cell.setBackground(Color.GREEN);
                    else
                        cell.setBackground(table.getBackground());

                } catch (Exception e) {
                    JOptionPane.showMessageDialog(
                            null,
                            "Ошибка! " + e.getMessage(),
                            "Ошибка",
                            javax.swing.JOptionPane.ERROR_MESSAGE);
                }
                cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                return cell;
            }

            ;
        };

        buttPanel.add(buttClose);
        buttPanel.add(buttPrint);
        buttPanel.add(new JLabel());
        buttPanel.add(buttDone);
        buttPanel.add(buttImport);

        osnovaPanel.add(title, BorderLayout.NORTH);
        osnovaPanel.add(new JScrollPane(table), BorderLayout.CENTER);
        osnovaPanel.add(buttPanel, BorderLayout.SOUTH);

        getContentPane().add(osnovaPanel);
        pack();
    }

    private void showPopupMenuTablePerformed() {
        try {
            if (table.getSelectedRow() != -1) {
                dataEanlist = new EanList();

                pb = new ProgressBar(this, false, "Сбор данных ...");
                SwingWorker sw = new SwingWorker() {
                    protected Object doInBackground() {
                        try {
                            epdb = new EanPDB();

                            dataEanlist = epdb.getDataEanlist(Integer.valueOf(table.getValueAt(table.getSelectedRow(), 1).toString()));

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
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Ошибка. " + e.getMessage(),
                    "Ошибка",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buttCloseActionPerformed(ActionEvent evt) {
        dispose();
    }

    private void buttDoneActionPerformed(ActionEvent evt) {
        try {
            ArrayList<EanItemListSize> listSizes = new ArrayList<>();
            for (int i = 0; i < table.getRowCount(); i++) {
                if ((Boolean) table.getValueAt(i, 13)) {
                    for (EanItemListSize size : ((EanItem) table.getValueAt(i, 14)).getDataSize()) {
                        if (size.getRst() == Integer.valueOf(table.getValueAt(i, 9).toString()) &&
                                size.getRzm() == Integer.valueOf(table.getValueAt(i, 10).toString()))
                            listSizes.add(size);
                    }
                }
            }

            epdb = new EanPDB();
            try {
                epdb.setEanImport(listSizes);

                JOptionPane.showMessageDialog(null,
                        "Сохранение завершено! ",
                        "Завершено",
                        javax.swing.JOptionPane.INFORMATION_MESSAGE);

                dispose();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null,
                        "Ошибка. " + e.getMessage(),
                        "Ошибка",
                        javax.swing.JOptionPane.ERROR_MESSAGE);
            } finally {
                epdb.disConn();
            }

        } catch (HeadlessException e) {
            JOptionPane.showMessageDialog(null,
                    "Ошибка " + e.getMessage(),
                    "Ошибка",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buttImportActionPerformed(ActionEvent evt) {
        try {
            if (JOptionPane.showOptionDialog(
                    null,
                    "Выполнить импорт EAN-кодов?",
                    "Выполнить",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    new Object[]{"Да", "Отмена"},
                    "Отмена") == JOptionPane.YES_OPTION) {

                pb = new ProgressBar(this, false, "Импорт ...");
                SwingWorker sw = new SwingWorker() {
                    protected Object doInBackground() {
                        try {
                            String message;
                            int rst;
                            int rzm;
                            int idColor;

                            boolean flagSelect = false;
                            boolean flagImport;

                            edb = new EanDB();

                            // Проверка отмеченных строк checkbox
                            for (int i = 0; i < table.getRowCount(); i++) {
                                if ((Boolean) table.getValueAt(i, 0)) {
                                    flagSelect = true;
                                    break;
                                }
                            }

                            // Импорт всех строк из таблицы или только тех что отмечены чекбоксами
                            for (int i = 0; i < table.getRowCount(); i++) {
                                flagImport = flagSelect ? (Boolean) table.getValueAt(i, 0) : true;

                                if (flagImport) {
                                    message = "НЕУДАЧА";

                                    rst = Integer.valueOf(table.getValueAt(i, 9).toString());
                                    rzm = Integer.valueOf(table.getValueAt(i, 10).toString());
                                    idColor = Integer.valueOf(table.getValueAt(i, 7).toString());

                                    try {
                                        // Проверка на цвет
                                        if (idColor == 2)
                                            // импорт в таблицу nsi_sd
                                            message = edb.importEancode(
                                                    (EanItem) table.getValueAt(i, 14),
                                                    rst,
                                                    rzm);

                                        else
                                            // импорт в таблицу NSI_EANCODE
                                            message = edb.importEancodeWithColor(
                                                    (EanItem) table.getValueAt(i, 14),
                                                    rst,
                                                    rzm);

                                    } catch (Exception e) {
                                        message = "ОШИБКА";
                                    }
                                    // Записываем в таблицу просмотра результат импорта
                                    table.setValueAt(message, i, 15);
                                    updateRowHeight(i, 15);

                                    if (message.contains("ИМПОРТ"))
                                        table.setValueAt(true, i, 13);

                                }
                            }

                        } catch (Exception e) {
                            JOptionPane.showMessageDialog(null,
                                    "Данные не загружены!\n " + e.getMessage(),
                                    "Ошибка",
                                    javax.swing.JOptionPane.ERROR_MESSAGE);
                        } finally {
                            edb.disConn();
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

                JOptionPane.showMessageDialog(null,
                        "Импорт завершен! ",
                        "Завершено",
                        javax.swing.JOptionPane.INFORMATION_MESSAGE);
            }
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
                    "Импорт EAN-кодов от " + new SimpleDateFormat("dd.MM.yyyy").format((Calendar.getInstance()).getTime()),
                    tModel,
                    table.getColumnModel());
            oo.createReport("EanImportDB.ots", false);


        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Ошибка " + e.getMessage(),
                    "Ошибка",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void createTable(final Object[][] row) {
        tModel = new DefaultTableModel(row, colImport) {
            @Override
            public Class<?> getColumnClass(int col) {
                return col == 0 ? Boolean.class : super.getClass();
            }

            @Override
            public boolean isCellEditable(int row, int col) {
                return (col == 0);
            }
        };

        tModel.addTableModelListener(e -> {
            if (tableModelListenerIsChanging) {
                return;
            }
            int firstRow = e.getFirstRow();
            int column = e.getColumn();

            if (column != 0 || maxSelectedRow == -1 || minSelectedRow == -1) {
                return;
            }
            tableModelListenerIsChanging = true;
            boolean value = ((Boolean) tModel.getValueAt(firstRow, column));
            for (int i = minSelectedRow; i <= maxSelectedRow; i++) {
                tModel.setValueAt(value, table.convertRowIndexToModel(i), column);
            }

            minSelectedRow = -1;
            maxSelectedRow = -1;

            tableModelListenerIsChanging = false;
        });

        table.setModel(tModel);
        table.setAutoCreateColumnsFromModel(true);


        table.getColumnModel().getColumn(0).setPreferredWidth(1);
        table.getColumnModel().getColumn(1).setMinWidth(0);
        table.getColumnModel().getColumn(1).setMaxWidth(0);
        table.getColumnModel().getColumn(2).setPreferredWidth(80);
        table.getColumnModel().getColumn(3).setPreferredWidth(75);
        table.getColumnModel().getColumn(4).setPreferredWidth(35);
        table.getColumnModel().getColumn(5).setPreferredWidth(55);
        table.getColumnModel().getColumn(6).setPreferredWidth(1);
        table.getColumnModel().getColumn(7).setMinWidth(0);
        table.getColumnModel().getColumn(7).setMaxWidth(0);
        table.getColumnModel().getColumn(8).setPreferredWidth(55);
        table.getColumnModel().getColumn(9).setPreferredWidth(1);
        table.getColumnModel().getColumn(10).setPreferredWidth(1);
        table.getColumnModel().getColumn(11).setPreferredWidth(90);
        table.getColumnModel().getColumn(12).setPreferredWidth(90);
        table.getColumnModel().getColumn(13).setMinWidth(0);
        table.getColumnModel().getColumn(13).setMaxWidth(0);
        table.getColumnModel().getColumn(14).setMinWidth(0);
        table.getColumnModel().getColumn(14).setMaxWidth(0);
        table.getColumnModel().getColumn(15).setPreferredWidth(120);

        sorter = new TableRowSorter<>(tModel);
        table.setRowSorter(sorter);

        table.getColumnModel().getColumn(15).setCellRenderer(renderer);

        for (int i = 0; i < table.getRowCount(); i++) {
            updateRowHeight(i, 15);
        }
    }

    /**
     * Метод заполняет данными таблицу формы просмотра.
     * @param eanLists
     * @throws Exception
     */
    private void setDataInTable(ArrayList<EanList> eanLists) throws Exception {
        try {
            for (EanList eanList : eanLists) {
                for (EanItem eanItem : eanList.getEanItems()) {
                    for (EanItemListSize eanItemListSize : eanItem.getDataSize()) {
                        tModel
                                .addRow(new Object[]{
                                        false,
                                        eanList.getIdEanlist(),
                                        eanList.getEanNum() + "   " + eanList.getDateVvod(),
                                        eanItem.getFasName(),
                                        eanItem.getFasNum(),
                                        eanItem.getFasNar(),
                                        eanItem.getFasSrt(),
                                        eanItem.getColorNum(),
                                        eanItem.getColorName(),
                                        eanItemListSize.getRst(),
                                        eanItemListSize.getRzm(),
                                        eanItemListSize.getSizePrint(),
                                        eanItemListSize.getEan13(),
                                        eanItemListSize.isEanImport(),
                                        eanItem,
                                        ""});
                    }
                }
            }

        } catch (NumberFormatException e) {
            System.out.println(e.getMessage());
            throw new Exception("Ошибка setDataInTable() " + e.getMessage(), e);
        }
    }

    private void initData(ArrayList<EanList> eanLists, String text) {
        title.setText(text);

        createTable(new Object[][]{});

        try {
            if (!eanLists.isEmpty())
                setDataInTable(eanLists);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null,
                    "Данные не загружены" + ex.getMessage(),
                    "Ошибка ",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateRowHeight(int nRow, int nCol) {
        // изменение высоты строки в таблице по столбцу 
        int rowHeight = table.getRowHeight();

        Component comp = table.prepareRenderer(table.getCellRenderer(nRow, nCol), nRow, nCol);
        rowHeight = Math.max(rowHeight, comp.getPreferredSize().height);

        table.setRowHeight(nRow, rowHeight);
    }
}
