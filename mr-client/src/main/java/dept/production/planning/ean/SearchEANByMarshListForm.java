package dept.production.planning.ean;

import by.march8.ecs.MainController;
import com.jhlabs.awt.ParagraphLayout;
import com.toedter.calendar.JDateChooser;
import common.ProgressBar;
import common.UtilFunctions;
import net.coderazzi.filters.gui.AutoChoices;
import net.coderazzi.filters.gui.TableFilterHeader;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Vector;

/**
 *
 * @author lidashka
 */
public class SearchEANByMarshListForm extends JDialog {
    private JPanel osnovaPanel;
    private JPanel centrPanel;
    private JPanel buttPanel;
    private JPanel headPanel;
    private JPanel footPanel;
    private JButton buttMakeEan;
    private JButton buttShow;
    private JButton buttClose;
    private JButton buttPrint;
    private JTextField searchText;
    private Vector col;
    private Vector rows;
    private JTable table;
    private DefaultTableModel tModel;

    private int minSelectedRow;
    private int maxSelectedRow;
    private boolean tableModelListenerIsChanging;

    private ProgressBar pb;
    private EanDB edb;
    private EanPDB epdb;
    private JDateChooser dateSearchMarhList;
    private JDateChooser dateSearchEanList;
    private ArrayList<EanItem> rowsExistingEan;
    private ArrayList<EanItem> rowsNotExistingEan = new ArrayList<EanItem>();
    private EanList dataEanlist;

    private MainController controller;

    public SearchEANByMarshListForm(MainController controller, boolean modal, String date) {
        super(controller.getMainForm(), modal);
        this.controller = controller;

        initMenu();
        initPropSetting();
        init();
        initData(date);

        createTableSearch(new Vector());

        this.setTitle("EAN-коды по моделям из маршрутных листов");
        this.setLocationRelativeTo(controller.getMainForm());
        this.setVisible(true);
    }

    private void initMenu() {
    }

    private void initPropSetting() {
    }

    private void init() {
        setMinimumSize(new Dimension(980, 700));
        setPreferredSize(new Dimension(500, 500));

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
        buttPanel.setLayout(new GridLayout(0, 4, 5, 5));
        buttPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        buttPanel.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);

        buttClose = new JButton("Закрыть");
        buttClose.addActionListener(evt -> buttCloseActionPerformed(evt));

        buttPrint = new JButton("Печать");
        buttPrint.addActionListener(evt -> buttPrintActionPerformed(evt));

        buttShow = new JButton("Показать");
        buttShow.addActionListener(evt -> buttShowActionPerformed(evt));

        buttMakeEan = new JButton("Сформировать EAN");
        buttMakeEan.addActionListener(evt -> buttMakeEanActionPerformed(evt));

        searchText = new JTextField();
        searchText.setPreferredSize(new Dimension(250, 20));
        searchText.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                if (evt.getKeyCode() == evt.VK_ENTER) buttShow.doClick();
            }
        });

        col = new Vector();
        col.add("");
        col.add("Наименование");
        col.add("Модель");
        col.add("Артикул");
        col.add("Сорт");
        col.add("Цвет М/Л");
        col.add("Цвет");
        col.add("Кол-во");
        col.add("Рост");
        col.add("Размер");
        col.add("Этикетка");
        col.add("EAN");
        col.add("EAN цвет");
        col.add("ГС1 Бел");
        col.add("sar");
        col.add("cw");
        col.add("idColor");
        col.add("idEanList");

        rows = new Vector();

        minSelectedRow = -1;
        maxSelectedRow = -1;
        tableModelListenerIsChanging = false;

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
        new TableFilterHeader(table, AutoChoices.ENABLED);

        tModel = new DefaultTableModel();

        JMenuItem detalItem = new JMenuItem("Подробно м/л");
        detalItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                detalPopupMenuTableMarshListActionPerformed(evt);
            }
        });

        JMenuItem showItem = new JMenuItem("Открыть заявку");
        showItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                showPopupMenuTableMarshListActionPerformed(evt);
            }
        });

        JMenuItem editItem = new JMenuItem("Изменить цвет");
        editItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                editPopupMenuTableMarshListActionPerformed(evt);
            }
        });

        JMenuItem makeEanItem = new JMenuItem("Сформировать Ean-код");
        makeEanItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                makeEanPopupMenuTableMarshListActionPerformed(evt);
            }
        });

        final JPopupMenu popupMenu = new JPopupMenu();
        popupMenu.add(detalItem);
        popupMenu.add(showItem);
        popupMenu.add(editItem);
        popupMenu.add(makeEanItem);

        table.setComponentPopupMenu(popupMenu);

        dateSearchMarhList = new JDateChooser();
        dateSearchMarhList.setPreferredSize(new Dimension(150, 20));

        dateSearchEanList = new JDateChooser();
        dateSearchEanList.setPreferredSize(new Dimension(150, 20));

        headPanel.add(new JLabel("М/Л с: "), ParagraphLayout.NEW_PARAGRAPH);
        headPanel.add(dateSearchMarhList);
        headPanel.add(new JLabel("  "));
        headPanel.add(new JLabel(" Заявки с: "));
        headPanel.add(dateSearchEanList);
        headPanel.add(new JLabel("  "));
        headPanel.add(buttShow);

        centrPanel.add(new JScrollPane(table), BorderLayout.CENTER);
        centrPanel.add(footPanel, BorderLayout.SOUTH);

        buttPanel.add(buttClose);
        buttPanel.add(buttPrint);
        buttPanel.add(new JLabel());
        buttPanel.add(buttMakeEan);

        osnovaPanel.add(headPanel, BorderLayout.NORTH);
        osnovaPanel.add(centrPanel, BorderLayout.CENTER);
        osnovaPanel.add(buttPanel, BorderLayout.SOUTH);

        getContentPane().add(osnovaPanel);
        pack();
    }

    private void buttCloseActionPerformed(ActionEvent evt) {
        dispose();
    }

    private void buttShowActionPerformed(ActionEvent evt) {
        try {
            createTableSearch(getDataDB(
                    new SimpleDateFormat("dd.MM.yyyy").format(dateSearchMarhList.getDate()),
                    new SimpleDateFormat("dd.MM.yyyy").format(dateSearchEanList.getDate())));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Ошибка " + e.getMessage(),
                    "Ошибка",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buttPrintActionPerformed(ActionEvent evt) {
        try {
            EanOO oo = new EanOO(
                    "EAN-коды по моделям из маршрутных листов начиная с " + new SimpleDateFormat("dd.MM.yyyy").format(dateSearchMarhList.getDate()),
                    tModel,
                    table.getColumnModel());
            oo.createReport("ZayavkaEanByMarshList.ots", false);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Ошибка " + e.getMessage(),
                    "Ошибка",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buttMakeEanActionPerformed(ActionEvent evt) {
        try {
            boolean flagSelect = false;

            for (int i = 0; i < table.getRowCount(); i++) {
                if ((Boolean) table.getValueAt(i, 0)) {
                    flagSelect = true;
                    break;
                }
            }

            if (flagSelect) {
                if (JOptionPane.showOptionDialog(
                        null,
                        "Сформировать EAN-код вида '" + UtilEan.EAN_PREFIX + "' для выбранных строк?",
                        "Сформировать",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        new Object[]{"Да", "Отмена"},
                        "Отмена") == JOptionPane.YES_OPTION) {

                    pb = new ProgressBar(this, false, "Формирование EAN...");

                    SwingWorker sw = new SwingWorker() {
                        protected Object doInBackground() {
                            // Формирование и импорт EAN-кодов для строк отмеченых чекбоксами
                            for (int i = 0; i < table.getRowCount(); i++) {
                                if ((Boolean) table.getValueAt(i, 0)) {
                                    String rezalt = makeAndImportEancode(i);

                                    // Записываем в таблицу результат                        
                                    tModel.setValueAt(rezalt, table.convertRowIndexToModel(i), 12);
                                    tModel.setValueAt(false, table.convertRowIndexToModel(i), 0);
                                    tModel.fireTableDataChanged();
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
                    pb.setVisible(true);
                }
            }
        } catch (HeadlessException e) {
            JOptionPane.showMessageDialog(null,
                    "Ошибка " + e.getMessage(),
                    "Ошибка",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void makeEanPopupMenuTableMarshListActionPerformed(ActionEvent evt) {
        try {
            if (table.getSelectedRow() != -1) {
                tModel.setValueAt(true, table.convertRowIndexToModel(table.getSelectedRow()), 0);
                buttMakeEan.doClick();
            }
        } catch (HeadlessException e) {
            JOptionPane.showMessageDialog(null,
                    "Ошибка " + e.getMessage(),
                    "Ошибка",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private String makeAndImportEancode(int selectedRow) {
        String message = "";
        try {
            edb = new EanDB();

            int srt = Integer.valueOf(table.getValueAt(selectedRow, 4).toString());

            if (srt != 3) throw new Exception("Не 3 сорт!");
            else {
                int fas = Integer.valueOf(table.getValueAt(selectedRow, 2).toString());
                String nar = table.getValueAt(selectedRow, 3).toString();
                int rst = Integer.valueOf(table.getValueAt(selectedRow, 8).toString());
                int rzm = Integer.valueOf(table.getValueAt(selectedRow, 9).toString());
                int idColor = Integer.valueOf(table.getValueAt(selectedRow, 16).toString());

                String eancodeByFirstGrade = edb.getEncodeWithColor(fas, nar, 1, rst, rzm, idColor);

                if (eancodeByFirstGrade == null || eancodeByFirstGrade.equals(""))
                    throw new Exception("Для 1 сорта отсутствует Ean-код с цветом!");
                else {
                    String eancodeByThirdGrade = edb.getEncodeWithColor(fas, nar, 3, rst, rzm, idColor);

                    if (eancodeByThirdGrade != null && !eancodeByThirdGrade.equals(""))
                        throw new Exception("Уже присвоен: " + eancodeByThirdGrade);
                    else {
                        EAN13 ean13 = new EAN13();
                        ean13.setPrefix(UtilEan.EAN_PREFIX);
                        eancodeByThirdGrade = ean13.generateEancodeByEanсode(eancodeByFirstGrade);

                        if (eancodeByThirdGrade == null)
                            throw new Exception("Не удалось сгенерировать Ean-код!");
                        else {
                            try {
                                // Проверка на цвет  
                                if (idColor == 2)
                                    throw new Exception("Импорт в NSI_EANCODE не возможен!");
                                else {
                                    // импорт в таблицу NSI_EANCODE
                                    message = edb.importEancodeWithColor(fas,
                                            nar,
                                            srt,
                                            rst,
                                            rzm,
                                            eancodeByThirdGrade,
                                            idColor);

                                    if (message.contains("ИМПОРТ"))
                                        message = eancodeByThirdGrade;
                                }
                            } catch (Exception e) {
                                message = "ОШИБКА";
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "EAN-код не сформирован!\n" + message + "\n " + e.getMessage(),
                    "Ошибка",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
        } finally {
            edb.disConn();
        }
        return message;
    }

    private void detalPopupMenuTableMarshListActionPerformed(ActionEvent evt) {
        try {
            if (table.getSelectedRow() != -1) {
                edb = new EanDB();

                new SmallTableForm(
                        controller,
                        true,
                        edb.getDetalMarshList(Integer.valueOf(table.getValueAt(table.getSelectedRow(), 2).toString()),
                                Integer.valueOf(table.getValueAt(table.getSelectedRow(), 14).toString()),
                                (table.getValueAt(table.getSelectedRow(), 3)).toString(),
                                Integer.valueOf(table.getValueAt(table.getSelectedRow(), 15).toString()),
                                new SimpleDateFormat("dd.MM.yyyy").format(dateSearchMarhList.getDate())),
                        new SimpleDateFormat("dd.MM.yyyy").format(dateSearchMarhList.getDate()));
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Ошибка. " + e.getMessage(),
                    "Ошибка",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
        } finally {
            edb.disConn();
        }
    }

    private void showPopupMenuTableMarshListActionPerformed(ActionEvent evt) {
        try {
            if (table.getSelectedRow() != -1) {
                dataEanlist = new EanList();

                pb = new ProgressBar(this, false, "Сбор данных ...");
                SwingWorker sw = new SwingWorker() {
                    protected Object doInBackground() {
                        try {
                            epdb = new EanPDB();

                            int idEanList = epdb.getIdEanListByIdEanItem(Integer.valueOf(table.getValueAt(table.getSelectedRow(), 17).toString()));

                            if (idEanList > 0)
                                dataEanlist = epdb.getDataEanlist(idEanList);

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

                if (dataEanlist.getIdEanlist() > 0)
                    new EanDetalForm(controller, true, dataEanlist, UtilEan.OPEN);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Ошибка. " + e.getMessage(),
                    "Ошибка",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void editPopupMenuTableMarshListActionPerformed(ActionEvent evt) {
        try {
            if (table.getSelectedRow() != -1) {

                UtilEan.ACTION_BUTT_EDIT_COLOR = false;

                new SmallEditDetalForm(
                        this,
                        true,
                        Integer.valueOf(table.getValueAt(table.getSelectedRow(), 15).toString()),
                        table.getValueAt(table.getSelectedRow(), 5).toString(),
                        Integer.valueOf(table.getValueAt(table.getSelectedRow(), 16).toString()),
                        table.getValueAt(table.getSelectedRow(), 6).toString()
                );

                if (UtilEan.ACTION_BUTT_EDIT_COLOR)
                    buttShow.doClick();

            } else
                JOptionPane.showMessageDialog(null,
                        "Вы ничего не выбрали!",
                        "Внимание",
                        javax.swing.JOptionPane.WARNING_MESSAGE);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Ошибка. " + e.getMessage(),
                    "Ошибка",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private Vector getDataDB(final String dateMarhList, final String dateEanList) {
        rowsExistingEan = new ArrayList<>();
        rowsNotExistingEan = new ArrayList<>();
        rows = new Vector();

        try {
            pb = new ProgressBar(this, false, "Сбор данных...");
            final SwingWorker sw = new SwingWorker() {
                @Override
                protected Object doInBackground() throws Exception {
                    try {
                        edb = new EanDB();
                        rows = edb.getEanByDataMrshList(UtilFunctions.convertDateStrToLong(dateMarhList));

                    } catch (Exception e) {
                        rows = new Vector();
                        JOptionPane.showMessageDialog(null,
                                "Сбой обновления! " + e.getMessage(),
                                "Ошибка",
                                javax.swing.JOptionPane.ERROR_MESSAGE);
                    } finally {
                        edb.disConn();
                    }

                    pb.setMessage("Поиск в заявках...");

                    try {
                        epdb = new EanPDB();
                        rowsExistingEan = epdb.getDataEanItemWithExistingEanByDate(UtilFunctions.convertDateStrToLong(dateEanList));

                        rowsNotExistingEan = epdb.getDataEanItemWithNotExistingEanByDate(UtilFunctions.convertDateStrToLong(dateEanList));

                    } catch (Exception e) {
                        rowsExistingEan = new ArrayList<EanItem>();
                        rowsNotExistingEan = new ArrayList<EanItem>();
                        JOptionPane.showMessageDialog(null,
                                "Сбой обновления! " + e.getMessage(),
                                "Ошибка",
                                javax.swing.JOptionPane.ERROR_MESSAGE);
                    } finally {
                        epdb.disConn();
                    }

                    pb.setMessage("Обработка...");

                    try {
                        Vector row = new Vector();

                        for (int i = 0; i < rows.size(); i++) {
                            row = (Vector) rows.get(i);

                            for (EanItem eanItem : rowsExistingEan) {
                                for (EanItemListSize size : eanItem.getDataSize()) {
                                    if (eanItem.getFasNum() == Integer.valueOf(row.get(2).toString()) &&
                                            eanItem.getFasNar().trim().equals(row.get(3).toString().trim()) &&
                                            eanItem.getFasSrt() == Integer.valueOf(row.get(4).toString()) &&
                                            eanItem.getColorName().trim().equals(row.get(6).toString().trim()) &&
                                            size.getRst() == Integer.valueOf(row.get(8).toString()) &&
                                            size.getRzm() == Integer.valueOf(row.get(9).toString())) {

                                        row.setElementAt(size.getEan13(), 13);
                                        row.setElementAt(eanItem.getId(), 17);

                                        rows.removeElementAt(i);
                                        rows.setElementAt(row, i);
                                    }
                                }
                            }

                            for (EanItem eanItem : rowsNotExistingEan) {
                                for (EanItemListSize size : eanItem.getDataSize()) {
                                    if (eanItem.getFasNum() == Integer.valueOf(row.get(2).toString()) &&
                                            eanItem.getFasNar().trim().equals(row.get(3).toString().trim()) &&
                                            eanItem.getFasSrt() == Integer.valueOf(row.get(4).toString()) &&
                                            eanItem.getColorName().trim().equals(row.get(6).toString().trim()) &&
                                            size.getRst() == Integer.valueOf(row.get(8).toString()) &&
                                            size.getRzm() == Integer.valueOf(row.get(9).toString())) {

                                        row.setElementAt(size.getEan13(), 13);
                                        row.setElementAt(eanItem.getId(), 17);

                                        rows.removeElementAt(i);
                                        rows.setElementAt(row, i);
                                    }
                                }
                            }
                        }
                    } catch (Exception e) {
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
            rows = new Vector();
            JOptionPane.showMessageDialog(null,
                    "Ошибка! " + e.getMessage(),
                    "Ошибка",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
        }
        return rows;
    }

    private void createTableSearch(final Vector row) {
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
                return col == 0;
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
        table.getColumnModel().getColumn(1).setPreferredWidth(50);
        table.getColumnModel().getColumn(2).setPreferredWidth(2);
        table.getColumnModel().getColumn(3).setPreferredWidth(35);
        table.getColumnModel().getColumn(4).setPreferredWidth(1);
        table.getColumnModel().getColumn(5).setPreferredWidth(50);
        table.getColumnModel().getColumn(6).setPreferredWidth(50);
        table.getColumnModel().getColumn(7).setPreferredWidth(1);
        table.getColumnModel().getColumn(8).setPreferredWidth(1);
        table.getColumnModel().getColumn(9).setPreferredWidth(1);
        table.getColumnModel().getColumn(10).setPreferredWidth(65);
        table.getColumnModel().getColumn(11).setPreferredWidth(80);
        table.getColumnModel().getColumn(12).setPreferredWidth(80);
        table.getColumnModel().getColumn(13).setPreferredWidth(80);
        table.getColumnModel().getColumn(14).setMinWidth(0);
        table.getColumnModel().getColumn(14).setMaxWidth(0);
        table.getColumnModel().getColumn(15).setMinWidth(0);
        table.getColumnModel().getColumn(15).setMaxWidth(0);
        table.getColumnModel().getColumn(16).setMinWidth(0);
        table.getColumnModel().getColumn(16).setMaxWidth(0);
        table.getColumnModel().getColumn(17).setMinWidth(0);
        table.getColumnModel().getColumn(17).setMaxWidth(0);

    }

    private void initData(String date) {
        try {
            dateSearchMarhList.setDate(new SimpleDateFormat("dd.MM.yyyy").parse(date));

        } catch (ParseException e) {
            java.util.Date d = (Calendar.getInstance()).getTime();
            d.setDate(1);

            dateSearchMarhList.setDate(d);
        }

        try {
            dateSearchEanList.setDate(new SimpleDateFormat("dd.MM.yyyy").parse(date));

        } catch (ParseException e) {
            java.util.Date d = (Calendar.getInstance()).getTime();
            d.setDate(1);

            dateSearchEanList.setDate(d);
        }
    }
}