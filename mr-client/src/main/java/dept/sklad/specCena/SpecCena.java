package dept.sklad.specCena;

import com.jhlabs.awt.ParagraphLayout;
import dept.component.MyButton;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author DzmitryB Форма для просмотра спеццен
 */
public class SpecCena extends JDialog {

    public Map buffer, buff;
    private MyButton btnClose;
    private MyButton btnAdd;
    private MyButton btnEdit;
    private MyButton btnRemove;
    private MyButton btnPrint;
    private MyButton btnFind;
    private JLabel lbHeader;
    private JLabel lbEditorKodAgenta;
    private JLabel lbEditorModel;
    private JLabel lbEditorArtic;
    private JLabel lbEditorSort;
    private JLabel lbEditorSRazm;
    private JLabel lbEditorERazm;
    private JLabel lbEditorSRost;
    private JLabel lbEditorERost;
    private JLabel lbEditorCena;
    private JLabel lbEditorDate;
    private JLabel jlAgent;
    private JLabel jlMod;
    private JTextField tfKodKontra;
    private JTextField tfEditorKodAgenta;
    private JTextField tfEditorModel;
    private JTextField tfEditorArtic;
    private JTextField tfEditorSort;
    private JTextField tfEditorSRazm;
    private JTextField tfEditorERazm;
    private JTextField tfEditorSRost;
    private JTextField tfEditorERost;
    private JTextField tfEditorCena;
    private JTextField tfEditorDate;
    private JTextField jtfAgent;
    private JTextField jtfMod;
    private JPanel mainPanel;
    private JPanel jpBody;
    private JPanel jpDownFooter;
    private JPanel jpUpFooter;
    private JPanel jpFooter;
    private JPanel jpHeader;
    private JTable myTable;
    private JScrollPane jsp;
    private JDialog jfFind;
    private Object jdPar;
    private int NRow = 0;
    private int NCol = 0;
    private SpecCenaDB scdb = new SpecCenaDB();
    private DefaultTableModel tm;
    private DefaultTableModel ttm;
    private TableColumn tcol;
    private JCheckBox jcOneCounterparties;
    private JCheckBox jcManyCounterparties;
    private JLabel label;
    private SpecCenaDB sdb;

    public SpecCena(JDialog parent, Map hm) {
        super(parent);
        jdPar = parent;
        init(hm);
        this.setLocationRelativeTo(parent);
        this.setVisible(true);
    }

    public SpecCena(JFrame parent, Map hm) {
        super(parent);
        jdPar = parent;
        init(hm);
        this.setLocationRelativeTo(parent);
        this.setVisible(true);
    }

    public void init(Map hm) {
        if (!jdPar.getClass().getName().trim().equals("by.march8.ecs.application.shell.general.uicontrol.MainForm")) {
            setMinimumSize(new Dimension(800, 400));
            setPreferredSize(new Dimension(800, 500));

            buffer = new HashMap();
            buff = new HashMap();
            buff = hm;

            lbHeader = new JLabel("Ввод специальных цен для экспортных покупателей");

            btnAdd = new MyButton("Добавить");
            btnAdd.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    try {
                        sdb = new SpecCenaDB();
                        sdb.specCenaAdd(getDataFromTable(), jcManyCounterparties.isSelected());

                        for (int i = 0; i < myTable.getRowCount(); i++) {
                            myTable.setValueAt("", i, 7);
                        }
                        tfKodKontra.setText("");

                        JOptionPane.showMessageDialog(null,
                                "Данные записаны в БД!",
                                "Завершено",
                                javax.swing.JOptionPane.INFORMATION_MESSAGE);

                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(null,
                                " " + ex.getMessage(),
                                "Ошибка",
                                javax.swing.JOptionPane.ERROR_MESSAGE);
                    } finally {
                        sdb.disConn();
                    }
                }
            });

            btnClose = new MyButton("Закрыть");
            btnClose.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    dispose();
                }
            });

            jpBody = new JPanel(new BorderLayout());
            myTable = new JTable();

            tm = new DefaultTableModel() {
                @Override
                public boolean isCellEditable(int row, int column) {
                    boolean booblik = false;
                    if (column == 7) {
                        booblik = true;
                    }
                    return booblik;
                }
            };

            myTable.addKeyListener(new KeyListener() {
                public void keyTyped(KeyEvent e) {
                }

                public void keyPressed(KeyEvent e) {
                    NCol = myTable.getSelectedColumn();
                    NRow = myTable.getSelectedRow();
                }

                public void keyReleased(KeyEvent e) {
                    if (NRow == 0 && NCol == 7 && e.getKeyCode() == KeyEvent.VK_ENTER && myTable.getRowCount() > 1) {
                        float scena = 0;
                        if (!myTable.getValueAt(NRow, NCol).toString().trim().equals("")) {
                            scena = Float.valueOf(myTable.getValueAt(NRow, NCol).toString());
                            if (scena != 0) {
                                BigDecimal n = new BigDecimal(scena * 1);
                                n = n.setScale(2, BigDecimal.ROUND_HALF_UP);
                                myTable.setValueAt(n, myTable.getSelectedRow(), 7);
                            }
                        }
                    }
                }
            });

            updateTable(buff);
            myTable.getColumnModel().getColumn(2).setPreferredWidth(0);
            jsp = new JScrollPane(myTable, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

            jpBody.add(jsp, BorderLayout.CENTER);

            tfKodKontra = new JTextField();
            tfKodKontra.setPreferredSize(new Dimension(120, 30));

            jcOneCounterparties = new JCheckBox("Код контрагента: ");
            jcOneCounterparties.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    if (jcOneCounterparties.isSelected()) {
                        tfKodKontra.setEnabled(true);
                    } else {
                        tfKodKontra.setEnabled(false);
                    }
                }
            });
            jcOneCounterparties.doClick();

            jcManyCounterparties = new JCheckBox("Для всех контрагентов;");
            jcManyCounterparties.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    if (jcOneCounterparties.isSelected()) {
                        tfKodKontra.setEnabled(true);
                    } else {
                        tfKodKontra.setEnabled(false);
                    }
                }
            });

            ButtonGroup group = new ButtonGroup();
            group.add(jcOneCounterparties);
            group.add(jcManyCounterparties);

            jpUpFooter = new JPanel();
            jpUpFooter.setLayout(new ParagraphLayout());
            jpUpFooter.add(new JLabel(), ParagraphLayout.NEW_PARAGRAPH);
            jpUpFooter.add(jcOneCounterparties);
            jpUpFooter.add(tfKodKontra);
            jpUpFooter.add(new JLabel(), ParagraphLayout.NEW_PARAGRAPH);
            jpUpFooter.add(jcManyCounterparties);

            jpDownFooter = new JPanel(new GridLayout(0, 4, 5, 5));
            jpDownFooter.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
            jpDownFooter.add(new JLabel());
            jpDownFooter.add(btnAdd);
            jpDownFooter.add(btnClose);
            jpDownFooter.add(new JLabel());

            jpFooter = new JPanel(new BorderLayout(1, 1));
            jpFooter.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
            jpFooter.add(jpUpFooter, BorderLayout.CENTER);
            jpFooter.add(jpDownFooter, BorderLayout.SOUTH);

            mainPanel = new JPanel(new BorderLayout(1, 1));
            mainPanel.add(lbHeader, BorderLayout.NORTH);
            mainPanel.add(jpBody, BorderLayout.CENTER);
            mainPanel.add(jpFooter, BorderLayout.SOUTH);

            this.add(mainPanel);
            this.setTitle("Просмотр спеццен");
            this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        } else {
            hm = scdb.getAllDataSpecCena();
            buffer = new HashMap();
            buff = new HashMap();
            buff = hm;
            mainPanel = new JPanel(new BorderLayout(0, 15));

            myTable = new JTable();
            myTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
                public void valueChanged(ListSelectionEvent e) {
                    NRow = myTable.getSelectedRow();
                    NCol = myTable.getSelectedColumn();
                }
            });
            tm = new DefaultTableModel() {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };
            lbHeader = new JLabel("Просмотр актуальных спеццен");

            btnFind = new MyButton("Найти");
            btnFind.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    jfFind = new JDialog();
                    jfFind.setTitle("Поиск");
                    JPanel jpGridCondition = new JPanel(new GridLayout(2, 2));
                    JPanel jpButtonContainer = new JPanel(new FlowLayout());
                    jfFind.setLayout(new GridLayout(2, 1));
                    jlAgent = new JLabel("Код агента");
                    jlMod = new JLabel("Модель");
                    jtfAgent = new JTextField("");
                    jtfMod = new JTextField("");
                    JButton btnCloseFind = new JButton("Отмена");
                    btnCloseFind.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            jfFind.dispose();
                        }
                    });

                    JButton btnOkFind = new JButton("Найти");
                    btnOkFind.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            SpecCenaDB scdb = new SpecCenaDB();
                            buff = scdb.getFind(jtfAgent.getText().trim(), jtfMod.getText().trim());
                            updateTableAllBrowse(buff);
                            jfFind.dispose();
                        }
                    });

                    jpGridCondition.add(jlAgent);
                    jpGridCondition.add(jtfAgent);
                    jpGridCondition.add(jlMod);
                    jpGridCondition.add(jtfMod);

                    jpButtonContainer.add(btnOkFind);
                    jpButtonContainer.add(btnCloseFind);

                    jfFind.add(jpGridCondition);
                    jfFind.add(jpButtonContainer);

                    jfFind.pack();
                    jfFind.setSize(320, 120);
                    jfFind.setLocationRelativeTo(null);
                    jfFind.setVisible(true);
                    jfFind.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
                }
            });

            btnClose = new MyButton("Закрыть");
            btnClose.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    dispose();
                }
            });

            btnPrint = new MyButton("Отчет");
            btnPrint.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    SpecCenaReport scr = new SpecCenaReport((JFrame) jdPar);
                }
            });

            btnEdit = new MyButton("Изменить");
            btnEdit.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    HashMap data = new HashMap();
                    if (myTable.isRowSelected(myTable.getSelectedRow())) {
                        data.put("id", myTable.getValueAt(myTable.getSelectedRow(), 0));
                        data.put("nscena", tfEditorCena.getText());
                        NRow = myTable.getSelectedRow();
                        NCol = myTable.getSelectedColumn();
                        scdb.specCenaEdit(data);
                        buff = new HashMap();
                        buff = scdb.getAllDataSpecCena();
                        updateTableAllBrowse(buff);
                    } else {
                        JOptionPane.showMessageDialog(null, "Выбирите строку которую хотите изменить");
                    }
                }
            });
            btnRemove = new MyButton("Удалить");
            btnRemove.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    if (myTable.isRowSelected(myTable.getSelectedRow())) {
                        buff = new HashMap();
                        buff.put("id", myTable.getValueAt(myTable.getSelectedRow(), 0));
                        NRow = myTable.getSelectedRow();
                        NCol = myTable.getSelectedColumn();
                        scdb.specCenaRemove(buff);
                        buff = new HashMap();
                        buff = scdb.getAllDataSpecCena();
                        updateTableAllBrowse(buff);
                    } else {
                        JOptionPane.showMessageDialog(null, "Выбирите строку которую хотите удалить");
                    }
                }
            });

            myTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
                public void valueChanged(ListSelectionEvent e) {
                    if (myTable.isRowSelected(myTable.getSelectedRow())) {
                        tfEditorKodAgenta.setText(myTable.getValueAt(myTable.getSelectedRow(), 1).toString());
                        tfEditorModel.setText(myTable.getValueAt(myTable.getSelectedRow(), 2).toString());
                        tfEditorArtic.setText(myTable.getValueAt(myTable.getSelectedRow(), 3).toString());
                        tfEditorSort.setText(myTable.getValueAt(myTable.getSelectedRow(), 4).toString());
                        tfEditorSRazm.setText(myTable.getValueAt(myTable.getSelectedRow(), 5).toString());
                        tfEditorERazm.setText(myTable.getValueAt(myTable.getSelectedRow(), 6).toString());
                        tfEditorSRost.setText(myTable.getValueAt(myTable.getSelectedRow(), 7).toString());
                        tfEditorERost.setText(myTable.getValueAt(myTable.getSelectedRow(), 8).toString());
                        tfEditorCena.setText(myTable.getValueAt(myTable.getSelectedRow(), 9).toString());
                        tfEditorDate.setText(myTable.getValueAt(myTable.getSelectedRow(), 10).toString());
                    }
                    tm.isCellEditable(myTable.getSelectedRow(), 1);
                    tm.isCellEditable(myTable.getSelectedRow(), 2);
                    tm.isCellEditable(myTable.getSelectedRow(), 3);
                    tm.isCellEditable(myTable.getSelectedRow(), 4);
                    tm.isCellEditable(myTable.getSelectedRow(), 5);
                    tm.isCellEditable(myTable.getSelectedRow(), 6);
                    tm.isCellEditable(myTable.getSelectedRow(), 7);
                    tm.isCellEditable(myTable.getSelectedRow(), 8);
                    tm.isCellEditable(myTable.getSelectedRow(), 9);
                    tm.isCellEditable(myTable.getSelectedRow(), 10);
                }
            });

            jpBody = new JPanel(new GridLayout(2, 1));
            buff = scdb.getAllDataSpecCena();
            updateTableAllBrowse(buff);
            myTable.getColumnModel().getColumn(1).setPreferredWidth(35);
            myTable.getColumnModel().getColumn(2).setPreferredWidth(65);
            myTable.getColumnModel().getColumn(3).setPreferredWidth(20);
            myTable.getColumnModel().getColumn(4).setPreferredWidth(35);
            myTable.getColumnModel().getColumn(5).setPreferredWidth(35);
            myTable.getColumnModel().getColumn(6).setPreferredWidth(35);
            myTable.getColumnModel().getColumn(7).setPreferredWidth(35);
            myTable.getColumnModel().getColumn(8).setPreferredWidth(30);
            myTable.getColumnModel().getColumn(9).setPreferredWidth(30);
            jsp = new JScrollPane(myTable, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            jpBody.add(jsp);
            jpHeader = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 30));
            jpHeader.add(lbHeader);
            jpUpFooter = new JPanel(new GridLayout(5, 5));

            lbEditorKodAgenta = new JLabel("Код кон-та: ");
            tfEditorKodAgenta = new JTextField();
            lbEditorModel = new JLabel("Модель: ");
            tfEditorModel = new JTextField();
            lbEditorArtic = new JLabel("Артикул: ");
            tfEditorArtic = new JTextField();
            lbEditorSort = new JLabel("Сорт: ");
            tfEditorSort = new JTextField();
            lbEditorSRazm = new JLabel("Разм. от: ");
            tfEditorSRazm = new JTextField();
            lbEditorERazm = new JLabel("Разм. до: ");
            tfEditorERazm = new JTextField();
            lbEditorSRost = new JLabel("Рост от: ");
            tfEditorSRost = new JTextField();
            lbEditorERost = new JLabel("Рост до: ");
            tfEditorERost = new JTextField();
            lbEditorCena = new JLabel("Цена: ");
            tfEditorCena = new JTextField();
            lbEditorDate = new JLabel("Дата");
            tfEditorDate = new JTextField();

            tfEditorKodAgenta.setEditable(false);
            tfEditorModel.setEditable(false);
            tfEditorArtic.setEditable(false);
            tfEditorSort.setEditable(false);
            tfEditorSRazm.setEditable(false);
            tfEditorERazm.setEditable(false);
            tfEditorSRost.setEditable(false);
            tfEditorERost.setEditable(false);
            tfEditorCena.setEditable(true);
            tfEditorDate.setEditable(false);

            tfEditorKodAgenta.setText(myTable.getValueAt(0, 1).toString());
            tfEditorModel.setText(myTable.getValueAt(0, 2).toString());
            tfEditorArtic.setText(myTable.getValueAt(0, 3).toString());
            tfEditorSort.setText(myTable.getValueAt(0, 4).toString());
            tfEditorSRazm.setText(myTable.getValueAt(0, 5).toString());
            tfEditorERazm.setText(myTable.getValueAt(0, 6).toString());
            tfEditorSRost.setText(myTable.getValueAt(0, 7).toString());
            tfEditorERost.setText(myTable.getValueAt(0, 8).toString());
            tfEditorCena.setText(myTable.getValueAt(0, 9).toString());
            tfEditorDate.setText(myTable.getValueAt(0, 10).toString());

            jpUpFooter.add(lbEditorKodAgenta);
            jpUpFooter.add(tfEditorKodAgenta);
            jpUpFooter.add(new JLabel());
            jpUpFooter.add(lbEditorModel);
            jpUpFooter.add(tfEditorModel);

            jpUpFooter.add(lbEditorArtic);
            jpUpFooter.add(tfEditorArtic);
            jpUpFooter.add(new JLabel());
            jpUpFooter.add(lbEditorSort);
            jpUpFooter.add(tfEditorSort);

            jpUpFooter.add(lbEditorSRazm);
            jpUpFooter.add(tfEditorSRazm);
            jpUpFooter.add(new JLabel());
            jpUpFooter.add(lbEditorERazm);
            jpUpFooter.add(tfEditorERazm);

            jpUpFooter.add(lbEditorSRost);
            jpUpFooter.add(tfEditorSRost);
            jpUpFooter.add(new JLabel());
            jpUpFooter.add(lbEditorERost);
            jpUpFooter.add(tfEditorERost);

            jpUpFooter.add(lbEditorCena);
            jpUpFooter.add(tfEditorCena);
            jpUpFooter.add(new JLabel());
            jpUpFooter.add(lbEditorDate);
            jpUpFooter.add(tfEditorDate);

            jpDownFooter = new JPanel(new FlowLayout());
            jpDownFooter.add(btnPrint);
            jpDownFooter.add(btnEdit);
            jpDownFooter.add(btnRemove);
            jpDownFooter.add(btnFind);
            jpDownFooter.add(btnClose);
            jpFooter = new JPanel(new GridLayout(1, 1));

            jpBody.add(jpUpFooter);
            jpFooter.add(jpDownFooter);
            mainPanel.add(jpBody, BorderLayout.CENTER);
            mainPanel.add(lbHeader, BorderLayout.PAGE_START);
            mainPanel.add(jpFooter, BorderLayout.PAGE_END);
            this.add(mainPanel);
            pack();
            this.setSize(800, 350);
            this.setTitle("Просмотр спеццен");
            this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        }
    }

    public void updateTable(Map data) {
        String[][] dataTable = new String[data.size()][8];
        String[] columnName = {"Модель", "Артикул", "Сорт", "Размер от", "Размер до", "Рост от", "Рост до", "Цена"};
        for (int i = 0; i < data.size(); i++) {
            Map hash = new HashMap();
            hash = (HashMap) data.get(i);
            dataTable[i][0] = hash.get("mod").toString();
            dataTable[i][1] = hash.get("art").toString();
            dataTable[i][2] = hash.get("sort").toString();
            dataTable[i][3] = hash.get("rzmmin").toString();
            dataTable[i][4] = hash.get("rzmmax").toString();
            dataTable[i][5] = hash.get("rstmin").toString();
            dataTable[i][6] = hash.get("rstmax").toString();
            dataTable[i][7] = "";
        }
        tm.setDataVector(dataTable, columnName);
        myTable.setModel(tm);
    }

    public void updateTableAllBrowse(Map data) {
        String[][] dataTable = new String[data.size()][11];
        String[] columnName = {"id", "Код контрагента", "Модель", "Артикул", "Сорт", "Размер от", "Размер до", "Рост от", "Рост до", "Цена", "Дата ввода"};
        for (int i = 0; i < data.size(); i++) {
            Map hash = new HashMap();
            hash = (HashMap) data.get(i);
            dataTable[i][0] = hash.get("id").toString();
            dataTable[i][1] = hash.get("kod_k").toString();
            dataTable[i][2] = hash.get("mod").toString();
            dataTable[i][3] = hash.get("art").toString();
            dataTable[i][4] = hash.get("sort").toString();
            dataTable[i][5] = hash.get("rzmmin").toString();
            dataTable[i][6] = hash.get("rzmmax").toString();
            dataTable[i][7] = hash.get("rstmin").toString();
            dataTable[i][8] = hash.get("rstmax").toString();
            dataTable[i][9] = hash.get("cena").toString();
            if (hash.get("date") != null) {
                dataTable[i][10] = hash.get("date").toString();
            } else {
                dataTable[i][10] = " ";
            }
        }
        tm.setDataVector(dataTable, columnName);
        myTable.setModel(tm);
        myTable.getColumnModel().getColumn(0).setPreferredWidth(0);
        myTable.getColumnModel().getColumn(0).setMinWidth(0);
        myTable.getColumnModel().getColumn(0).setMaxWidth(0);
    }

    public void updateTableActualBrowse(HashMap data) {
        String[][] dataTable = new String[data.size()][11];
        String[] columnName = {"id", "Код контрагента", "Модель", "Артикул", "Сорт", "Размер от", "Размер до", "Рост от", "Рост до", "Цена", "Дата ввода"};
        for (int i = 0; i < data.size(); i++) {
            HashMap hash = new HashMap();
            hash = (HashMap) data.get(i);
            dataTable[i][0] = hash.get("id").toString();
            dataTable[i][1] = hash.get("kod_k").toString();
            dataTable[i][2] = hash.get("mod").toString();
            dataTable[i][3] = hash.get("art").toString();
            dataTable[i][4] = hash.get("sort").toString();
            dataTable[i][5] = hash.get("rzmmin").toString();
            dataTable[i][6] = hash.get("rzmmax").toString();
            dataTable[i][7] = hash.get("rstmin").toString();
            dataTable[i][8] = hash.get("rstmax").toString();
            dataTable[i][9] = hash.get("cena").toString();
            dataTable[i][10] = hash.get("date").toString();
        }
        tm.setDataVector(dataTable, columnName);
        myTable.setModel(tm);
        myTable.removeColumn(myTable.getColumnModel().getColumn(0));
    }

    public String[][] getDataFromTable() {
        String[][] data = new String[myTable.getRowCount()][9];
        for (int i = 0; i < myTable.getRowCount(); i++) {
            data[i][0] = tfKodKontra.getText().trim();
            data[i][1] = myTable.getValueAt(i, 0).toString();
            data[i][2] = myTable.getValueAt(i, 1).toString();
            data[i][3] = myTable.getValueAt(i, 2).toString();
            data[i][4] = myTable.getValueAt(i, 3).toString();
            data[i][5] = myTable.getValueAt(i, 4).toString();
            data[i][6] = myTable.getValueAt(i, 5).toString();
            data[i][7] = myTable.getValueAt(i, 6).toString();
            data[i][8] = myTable.getValueAt(i, 7).toString();
        }
        return data;
    }

    public HashMap getDataFromRow() {
        HashMap hm = new HashMap();
        try {
            hm.put("kod_k", myTable.getValueAt(NRow, 0).toString());
            hm.put("mod", myTable.getValueAt(NRow, 1).toString());
            hm.put("art", myTable.getValueAt(NRow, 2).toString());
            hm.put("sort", myTable.getValueAt(NRow, 3).toString());
            hm.put("rzmmin", myTable.getValueAt(NRow, 4).toString());
            hm.put("rzmmax", myTable.getValueAt(NRow, 5).toString());
            hm.put("rstmin", myTable.getValueAt(NRow, 6).toString());
            hm.put("rstmax", myTable.getValueAt(NRow, 7).toString());
            hm.put("cena", myTable.getValueAt(NRow, 8).toString());
        } catch (Exception ex) {
            if (myTable.getRowCount() > 0) {
                JOptionPane.showMessageDialog(null, "Выберите строку с интересующим Вас контрагентом");
            }
            hm.put("id", " ");
            hm.put("kod_k", " ");
            hm.put("mod", " ");
            hm.put("art", " ");
            hm.put("sort", " ");
            hm.put("rzmmin", " ");
            hm.put("rzmmax", " ");
            hm.put("rstmin", " ");
            hm.put("rstmax", " ");
            hm.put("cena", " ");
        }
        return hm;
    }
}
