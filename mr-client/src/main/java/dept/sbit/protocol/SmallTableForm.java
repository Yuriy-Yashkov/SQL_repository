package dept.sbit.protocol;

import by.march8.ecs.MainController;
import common.CheckBoxHeader;
import common.ProgressBar;
import dept.sbit.protocol.forms.ArticlePicker;
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
public class SmallTableForm extends javax.swing.JDialog {
    private JPanel osnova;
    private JPanel centerPanel;
    private JPanel buttPanel;
    private JPanel dialogPanel;
    private JButton buttClose;
    private JButton buttPrint;
    private JButton buttSearchDB;

    private ProgressBar pb;

    private JLabel title;
    private Vector col;
    private JTable table;
    private DefaultTableModel tModel;
    private TableRowSorter<TableModel> sorter;
    private TableFilterHeader filterHeader;
    private JButton buttCreate;
    private Vector dataReport;

    private ProtocolDB pdb;

    private int minSelectedRow = -1;
    private int maxSelectedRow = -1;
    private boolean tableModelListenerIsChanging = false;

    private String articleList = "7С";

    private boolean checkEan;
    private ButtonGroup buttonGroup;
    private JTextField textFieldInput;

    public SmallTableForm(MainController mainController, ProtocolForm parent, boolean modal, Vector dataTable, boolean checkEan) {
        super(parent, modal);
        setTitle("Отчет поиска артикулов");

        setPreferredSize(new Dimension(600, 400));

        init();

        col.add("");
        col.add("Модель");
        col.add("Артикул");

        this.checkEan = checkEan;

        createDefaultТable(dataTable, col);

        setLocationRelativeTo(parent);
        setVisible(true);
    }

    private void init() {
        UtilProtocol.BUTT_ACTION_CREATE = false;

        setMinimumSize(new Dimension(450, 550));

        title = new JLabel();
        title.setHorizontalAlignment(JLabel.CENTER);
        title.setFont(new Font("serif", Font.PLAIN, 14));

        buttCreate = new JButton("Продолжить");
        buttCreate.addActionListener(evt -> buttCreateTableActionPerformed(evt));

        buttClose = new JButton("Закрыть");
        buttClose.addActionListener(evt -> buttCloseTableActionPerformed(evt));

        buttPrint = new JButton("Печать");
        buttPrint.addActionListener(evt -> buttPrintActionPerformed(evt));

        buttSearchDB = new JButton("Поиск в БД");
        buttSearchDB.addActionListener(evt -> buttSearchDBTableActionPerformed(evt));

        col = new Vector();
        tModel = new DefaultTableModel();
        filterHeader = new TableFilterHeader(table, AutoChoices.ENABLED);

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
        JRadioButton radioButton1 = new JRadioButton("в одной ячейке;");
        radioButton1.setActionCommand(UtilProtocol.TYPE_PROTOCOL);
        radioButton1.setSelected(true);

        JRadioButton radioButton2 = new JRadioButton("подробно;");
        radioButton2.setActionCommand(UtilProtocol.TYPE_PROTOCOL_DETAL);

        JRadioButton radioButton3 = new JRadioButton("Green;");
        radioButton3.setActionCommand(UtilProtocol.TYPE_PROTOCOL_CLIENT);

        buttonGroup = new ButtonGroup();
        buttonGroup.add(radioButton1);
        buttonGroup.add(radioButton2);
        buttonGroup.add(radioButton3);
        textFieldInput = new JTextField("0");

        dialogPanel = new JPanel();
        dialogPanel.setLayout(new GridLayout(5, 0, 5, 5));
        dialogPanel.add(new JLabel("Введите размер скидки для расчета протокола  "));
        dialogPanel.add(textFieldInput);
        dialogPanel.add(new JLabel("Наименование товара:"));
        dialogPanel.add(radioButton1);
        dialogPanel.add(new JLabel());
        dialogPanel.add(radioButton2);
        dialogPanel.add(new JLabel());
        dialogPanel.add(radioButton3);
        centerPanel = new JPanel();
        centerPanel.setLayout(new BorderLayout(1, 1));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        centerPanel.add(new JScrollPane(table), BorderLayout.CENTER);

        buttPanel = new JPanel();
        buttPanel.setLayout(new GridLayout(0, 4, 5, 5));
        buttPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        buttPanel.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        buttPanel.add(buttClose);
        buttPanel.add(buttCreate);
        buttPanel.add(buttSearchDB);
        buttPanel.add(buttPrint);

        osnova = new JPanel();
        osnova.setLayout(new BorderLayout(1, 1));
        osnova.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        osnova.add(title, BorderLayout.NORTH);
        osnova.add(centerPanel, BorderLayout.CENTER);
        osnova.add(buttPanel, BorderLayout.SOUTH);

        getContentPane().add(osnova);
        pack();
    }

    private void buttPrintActionPerformed(ActionEvent evt) {
        try {
            ProtocolOO oo = new ProtocolOO(title.getText(), tModel, table.getColumnModel());
            oo.createReport("DefaultTableBookFormatCheck.ots");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Ошибка ", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buttCloseTableActionPerformed(ActionEvent evt) {
        dispose();
    }

    private void buttCreateTableActionPerformed(ActionEvent evt) {
        //  поиск данных для отчета
        UtilProtocol.BUTT_ACTION_CREATE = false;

        if (JOptionPane.showOptionDialog(null, dialogPanel, "Сформировать отчет ", JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE, null, new Object[]{"Да", "Отмена"}, "Да") == JOptionPane.YES_OPTION) {

            String input = textFieldInput.getText().trim();

            int discount = 0;
            try {
                discount = Integer.valueOf(input.trim());
            } catch (Exception e) {
                e.printStackTrace();
            }

            pb = new ProgressBar(this, false, "Поиск цен и кодов ean13...");
            final int finalDiscount = discount;

//            SwingWorker sw = new SwingWorker() {
//                private ProtocolDB db;
                ProtocolDB db;

//                @Override
//                protected Object doInBackground() {
                    try {
                        db = new ProtocolDB();
                        UtilProtocol.ERROR_MESSAGE = "";
                        dataReport = new Vector();

                        for (Object tmp : tModel.getDataVector()) {
                            if (buttonGroup.getSelection().getActionCommand().equals((UtilProtocol.TYPE_PROTOCOL))) {
                                dataReport.addAll(
                                        db.getInfoModelProduct(
                                                Integer.valueOf(((Vector) tmp).get(1).toString()),
                                                ((Vector) tmp).get(2).toString(),
                                                checkEan, finalDiscount));

                            } else if (buttonGroup.getSelection().getActionCommand().equals((UtilProtocol.TYPE_PROTOCOL_DETAL))) {
                                dataReport.addAll(
                                        db.getDetalInfoModelProduct(
                                                Integer.valueOf(((Vector) tmp).get(1).toString()),
                                                ((Vector) tmp).get(2).toString(),
                                                checkEan, finalDiscount));
                            } else if (buttonGroup.getSelection().getActionCommand().equals(((UtilProtocol.TYPE_PROTOCOL_CLIENT)))) {
                                dataReport.addAll(
                                        db.getGreenDetailInfoModelProduct(
                                                Integer.valueOf(((Vector) tmp).get(1).toString()),
                                                ((Vector) tmp).get(2).toString(),
                                                checkEan, finalDiscount
                                        )
                                );
                            }
                        }

                    } catch (Exception e) {
                        dataReport = new Vector();
                        JOptionPane.showMessageDialog(null,
                                "Ошибка. " + e.getMessage(),
                                "Ошибка",
                                javax.swing.JOptionPane.ERROR_MESSAGE);

                    }
//                    finally {
//                        db.disConn();
//                    }
//                    return null;
//                }

//                protected void done() {
//                    pb.dispose();
//                }
//            };
//            sw.execute();
            pb.setVisible(true);

            if (UtilProtocol.ERROR_MESSAGE.length() > 0) {
                JOptionPane.showMessageDialog(null,
                        new JScrollPane(new JTextArea(UtilProtocol.ERROR_MESSAGE + "\n")),
                        "Не найдено!",
                        javax.swing.JOptionPane.WARNING_MESSAGE);

                if (JOptionPane.showOptionDialog(null,
                        "Найдены не все артикула!\n"
                                + " Продолжить формирование отчета?",
                        "Продолжить...",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        new Object[]{"Да", "Отмена"},
                        "Да") == JOptionPane.YES_OPTION) {

                    UtilProtocol.BUTT_ACTION_CREATE = true;
                    dispose();
                } else
                    UtilProtocol.BUTT_ACTION_CREATE = false;
            } else {
                UtilProtocol.BUTT_ACTION_CREATE = true;
                dispose();
            }
        }
    }

    private void buttSearchDBTableActionPerformed(ActionEvent evt) {
        try {
            boolean selectRow = false;
            for (int i = 0; i < tModel.getDataVector().size(); i++) {
                if (Boolean.valueOf(((Vector) tModel.getDataVector().get(i)).elementAt(0).toString())) {
                    selectRow = true;
                    break;
                }
            }

            if (selectRow) {
                if (JOptionPane.showOptionDialog(null,
                        "Искать отмеченые модели в базе артикулов?",
                        "Поиск в БД",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        new Object[]{"Да", "Отмена"},
                        "Да") == JOptionPane.YES_OPTION) {

                    ArticlePicker picker = new ArticlePicker(SmallTableForm.this, articleList);
                    String result = picker.selectArticles();
                    if (result != null) {
                        articleList = result;
                    }

                    pb = new ProgressBar(this, false, "Поиск артикулов в БД...");
                    SwingWorker sw = new SwingWorker() {
                        protected Object doInBackground() {
                            try {

                                pdb = new ProtocolDB();

                                for (int i = 0; i < tModel.getDataVector().size(); i++)
                                    if (Boolean.valueOf(((Vector) tModel.getDataVector().get(i))
                                            .elementAt(0).toString())) {
                                        tModel.setValueAt(pdb
                                                .getAllNarModels(
                                                        tModel.getValueAt(i, 1).toString().trim(),
                                                        "%",
                                                        articleList).toString().replace("[", "").replace("]", ""), i, 2);
                                    }

                            } catch (Exception ex) {

                                JOptionPane.showMessageDialog(
                                        null,
                                        "Ошибка поиска: " + ex.getMessage(),
                                        "Ошибка!",
                                        JOptionPane.ERROR_MESSAGE);

                            }
                            return null;
                        }

                        protected void done() {
                            pb.dispose();
                        }

                    };
                    sw.execute();
                    pb.setVisible(true);

                    createDefaultТable(tModel.getDataVector(), col);
                }

            } else
                JOptionPane.showMessageDialog(null,
                        "Вы ничего не выбрали!",
                        "Внимание",
                        javax.swing.JOptionPane.WARNING_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    e.getMessage(),
                    "Ошибка ",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void createDefaultТable(final Vector row, Vector col) {
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
                if (col == 0 || col == 2)
                    return true;
                else
                    return false;
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
            boolean value = ((Boolean) tModel.getValueAt(firstRow, column)).booleanValue();
            for (int i = minSelectedRow; i <= maxSelectedRow; i++) {
                tModel.setValueAt(Boolean.valueOf(value), table.convertRowIndexToModel(i), column);
            }

            minSelectedRow = -1;
            maxSelectedRow = -1;

            tableModelListenerIsChanging = false;
        });
        table.setModel(tModel);
        table.setAutoCreateColumnsFromModel(true);

        table.getColumnModel().getColumn(0).setPreferredWidth(5);
        table.getColumnModel().getColumn(1).setPreferredWidth(70);
        table.getColumnModel().getColumn(2).setPreferredWidth(350);

        sorter = new TableRowSorter<TableModel>(tModel);
        table.setRowSorter(sorter);
        table.getColumnModel().getColumn(0).setHeaderRenderer(new CheckBoxHeader(table.getTableHeader(), 0, ""));
    }

    public Vector getDataReport() {
        return dataReport;
    }

    public String getTypeDocument() {
        return buttonGroup.getSelection().getActionCommand();
    }
}