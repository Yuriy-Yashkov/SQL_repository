/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dept.upack;

import common.PanelWihtFone;
import workOO.OpenOffice;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Vector;

/**
 *
 * @author user
 */
public class TableForm2New extends JDialog {
    Vector columns = null;
    Vector rows;
    Vector rowsForPrint;
    String str;
    private JPanel mainPanel;
    private JTable table;
    private JTable summTable;
    private JLabel nameTable;
    private DefaultTableModel tModel;
    private DefaultTableModel tModelSumm;
    private DefaultTableModel tModelForPrint;
    private JScrollPane scrollTable;
    private JScrollPane scrollTableSum;
    private JButton bClose;
    private JButton bPrint;
    private long kol;
    private long kolOfKomplect;
    private long sum;


    public TableForm2New(JDialog parent, boolean f, int type, String date, ArrayList column, ArrayList row, long kol, long kolOfKomplect, long sum) {
        super(parent, f);
        columns = new Vector(column);
        str = new String("Сдано на склад");
        rowsForPrint = new Vector(row);
        if (columns.get(columns.size() - 1).toString() == "Сумма") {
            rows = FormatVectorUsers(new Vector(row), columns.size() - 3, columns.size() - 2, columns.size() - 1);
        } else {
            rows = FormatVectorUsers(new Vector(row), columns.size() - 2, columns.size() - 1);
        }
        this.kol = kol;
        this.kolOfKomplect = kolOfKomplect;
        this.sum = sum;
        //int longColumb = column


        initComponents();
    }

    private void initComponents() {
        mainPanel = new PanelWihtFone();
        nameTable = new JLabel("Перечень продукции сданной на склад");

        tModelForPrint = new DefaultTableModel(rowsForPrint, columns);
        tModel = new DefaultTableModel(rows, columns);


        Vector colTm2 = new Vector();
        Vector usersTModelSumm = new Vector();
        int longColumb = columns.size();
        for (int i = 0; i < columns.size(); i++) {
            usersTModelSumm.add("");
        }
        boolean flagSumma = false;
        int numCol = 3;
        String str = new String(columns.get(columns.size() - 1).toString());
        if (columns.get(columns.size() - 1).toString() == "Сумма") {
            flagSumma = true;
            numCol = numCol + 2;// =+2
        }
        colTm2.add("Итого");
        for (int i = 0; i < columns.size() - numCol; i++) {
            colTm2.add("");
        }
        colTm2.add(parsStr(Long.toString(kol), 3));
        colTm2.add(parsStr(Long.toString(kolOfKomplect), 3));
        if (flagSumma) {
            colTm2.add("");
            colTm2.add(parsStr(Long.toString(sum), 3));
        }
        tModelSumm = new DefaultTableModel(0, 3);
        tModelSumm.setColumnIdentifiers(usersTModelSumm);
        tModelSumm.addRow(colTm2);

        table = new JTable(tModel);
        JTableHeader header = table.getTableHeader();
        header.setResizingAllowed(false);
        final TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(tModel);
        table.setRowSorter(sorter);
        summTable = new JTable(tModelSumm);
        if (columns.get(columns.size() - 1).toString() == "Сумма") {
            table.setDefaultRenderer(Object.class, new FlightTableRenderer());
            summTable.setDefaultRenderer(Object.class, new FlightTableRendererRes());
        } else {
            table.setDefaultRenderer(Object.class, new FlightTableRendererNoSumm());
            summTable.setDefaultRenderer(Object.class, new FlightTableRendererResNoSumm());
        }

        scrollTable = new JScrollPane(table);
        scrollTable.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollTableSum = new JScrollPane(summTable);
        scrollTableSum.setPreferredSize(new Dimension(400, 18));
        scrollTableSum.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollTableSum.setMaximumSize(new Dimension(5000, 18));
        mainPanel.setLayout(new BorderLayout(5, 5));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.add(nameTable);
        centerPanel.add(Box.createVerticalStrut(15));
        centerPanel.add(scrollTable);
        centerPanel.add(Box.createVerticalStrut(5));
        centerPanel.add(scrollTableSum);
        //*************************************************************************************
        // Init southPanel of mainPanel
        //*************************************************************************************        
        bClose = new JButton("Закрыть");
        bPrint = new JButton("Печать");
        bClose.addActionListener(new ActionClose());
        bPrint.addActionListener(new ActionPrint());
        JPanel southPanel = new JPanel(new GridLayout(1, 2, 5, 5));
        JPanel southLeftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel southLeftGridPanel = new JPanel(new GridLayout(1, 2, 5, 5));
        southLeftGridPanel.add(bPrint);
        southLeftPanel.add(southLeftGridPanel);
        southPanel.add(southLeftPanel);
        JPanel southRightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        southRightPanel.add(bClose);
        southPanel.add(southRightPanel);

        mainPanel.add(southPanel, BorderLayout.SOUTH);
        mainPanel.add(centerPanel);
        this.setTitle("Сдано на склад");
        add(mainPanel);
        setSize(970, 630);
        mainPanel.setMinimumSize(new Dimension(970, 630));
        setResizable(true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    public Vector FormatVectorUsers(Vector users, int numberElement1, int numberElement2) {
        Vector usersFormate = new Vector();
        //Vector vtest = new Vector();
        int schotchik = 0;
        while (schotchik < users.size()) {
            String ss = new String();
            ss = users.elementAt(schotchik).toString();
            ss = ss.substring(1, ss.length() - 1);
            boolean flagStop = true;
            String ssResult = new String();
            Vector vectorResult = new Vector();
            int numElement = 0;

            while (flagStop) {
                int numSubStringEnd = 0;
                numSubStringEnd = ss.indexOf(",");
                if (ss.indexOf(",") == -1) {
                    numSubStringEnd = ss.length();
                    flagStop = false;
                }
                ssResult = ss.substring(0, numSubStringEnd);
                if (flagStop) {
                    ss = ss.substring(numSubStringEnd + 2, ss.length());
                }
                if (numElement == numberElement1 || numElement == numberElement2) {
                    String[] resultRasryad = new String[9];
                    int initRes = 0;
                    for (int i = ssResult.length(); i > 0; i = i - 3) {
                        if (i >= 3) {
                            resultRasryad[initRes] = ssResult.substring(i - 3, i);
                        } else {
                            resultRasryad[initRes] = ssResult.substring(0, i);
                        }
                        initRes++;
                    }
                    ssResult = "";
                    while (initRes > 0) {
                        initRes--;
                        if (initRes > 0) {
                            ssResult += resultRasryad[initRes] + " ";
                        } else {
                            ssResult += resultRasryad[initRes];
                        }
                    }
                }
                vectorResult.add(ssResult);
                numElement++;
            }
            usersFormate.add(vectorResult);
            schotchik++;
        }
        return usersFormate;
    }

    public Vector FormatVectorUsers(Vector users, int numberElement1, int numberElement2, int numberElement3) {
        Vector usersFormate = new Vector();
        //Vector vtest = new Vector();
        int schotchik = 0;
        while (schotchik < users.size()) {
            String ss = new String();
            ss = users.elementAt(schotchik).toString();
            ss = ss.substring(1, ss.length() - 1);
            boolean flagStop = true;
            String ssResult = new String();
            Vector vectorResult = new Vector();
            int numElement = 0;

            while (flagStop) {
                int numSubStringEnd = 0;
                numSubStringEnd = ss.indexOf(",");
                if (ss.indexOf(",") == -1) {
                    numSubStringEnd = ss.length();
                    flagStop = false;
                }
                ssResult = ss.substring(0, numSubStringEnd);
                if (flagStop) {
                    ss = ss.substring(numSubStringEnd + 2, ss.length());
                }
                if (numElement == numberElement1 || numElement == numberElement2 || numElement == numberElement3) {
                    String[] resultRasryad = new String[9];
                    int initRes = 0;
                    for (int i = ssResult.length(); i > 0; i = i - 3) {
                        if (i >= 3) {
                            resultRasryad[initRes] = ssResult.substring(i - 3, i);
                        } else {
                            resultRasryad[initRes] = ssResult.substring(0, i);
                        }
                        initRes++;
                    }
                    ssResult = "";
                    while (initRes > 0) {
                        initRes--;
                        if (initRes > 0) {
                            ssResult += resultRasryad[initRes] + " ";
                        } else {
                            ssResult += resultRasryad[initRes];
                        }
                    }
                }
                vectorResult.add(ssResult);
                numElement++;
            }
            usersFormate.add(vectorResult);
            schotchik++;
        }
        return usersFormate;
    }

    public String parsStr(String str, int razr) {
        StringBuffer s = new StringBuffer(str);
        int i = s.length() - razr;
        for (; i > 0; i = i - razr) {
            s.insert(i, " ");
        }
        return s.toString();
    }

    // Action
    class ActionClose implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            dispose();
        }
    }

    // printButton
    class ActionPrint implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            try {
                OpenOffice oo = new OpenOffice(nameTable.getText(), tModelForPrint);
                oo.createReport("Остатки.ots");
            } catch (Exception er) {
                System.err.println("Ошибка при печати " + er);
            }
        }
    }

    public class FlightTableRenderer extends DefaultTableCellRenderer {
        public Component getTableCellRendererComponent(JTable table,
                                                       Object value,
                                                       boolean isSelected,
                                                       boolean hasFocus,
                                                       int row,
                                                       int column) {

            setText(value.toString());
            setFont(new Font("Serif", Font.ROMAN_BASELINE, 12));
            table.getColumnModel().getColumn(0).setPreferredWidth(120);
            table.getColumnModel().getColumn(table.getColumnCount() - 1).setPreferredWidth(120);
            table.getColumnModel().getColumn(table.getColumnCount() - 2).setPreferredWidth(50);
            table.getColumnModel().getColumn(table.getColumnCount() - 3).setPreferredWidth(50);
            if (column == table.getColumnCount() - 3 || column == table.getColumnCount() - 2 || column == table.getColumnCount() - 1) {
                setHorizontalAlignment(SwingConstants.RIGHT);
            } else {
                setHorizontalAlignment(SwingConstants.LEFT);
            }

            return this;

        }
    }

    public class FlightTableRendererNoSumm extends DefaultTableCellRenderer {
        public Component getTableCellRendererComponent(JTable table,
                                                       Object value,
                                                       boolean isSelected,
                                                       boolean hasFocus,
                                                       int row,
                                                       int column) {

            setText(value.toString());
            setFont(new Font("Serif", Font.ROMAN_BASELINE, 12));
            table.getColumnModel().getColumn(0).setPreferredWidth(120);
            table.getColumnModel().getColumn(table.getColumnCount() - 1).setPreferredWidth(50);
            table.getColumnModel().getColumn(table.getColumnCount() - 2).setPreferredWidth(50);
            if (column == table.getColumnCount() - 2 || column == table.getColumnCount() - 1) {
                setHorizontalAlignment(SwingConstants.RIGHT);
            } else {
                setHorizontalAlignment(SwingConstants.LEFT);
            }
            return this;

        }
    }


    public class FlightTableRendererRes extends DefaultTableCellRenderer {
        public Component getTableCellRendererComponent(JTable table,
                                                       Object value,
                                                       boolean isSelected,
                                                       boolean hasFocus,
                                                       int row,
                                                       int column) {
            setText(value.toString());
            setFont(new Font("Serif", Font.BOLD, 15));
            table.getColumnModel().getColumn(0).setPreferredWidth(120);
            table.getColumnModel().getColumn(table.getColumnCount() - 1).setPreferredWidth(120);
            table.getColumnModel().getColumn(table.getColumnCount() - 2).setPreferredWidth(50);
            table.getColumnModel().getColumn(table.getColumnCount() - 3).setPreferredWidth(50);
            if (column == table.getColumnCount() - 3 || column == table.getColumnCount() - 2 || column == table.getColumnCount() - 1) {
                setHorizontalAlignment(SwingConstants.RIGHT);
            } else {
                setHorizontalAlignment(SwingConstants.LEFT);
            }
            return this;
        }
    }

    public class FlightTableRendererResNoSumm extends DefaultTableCellRenderer {
        public Component getTableCellRendererComponent(JTable table,
                                                       Object value,
                                                       boolean isSelected,
                                                       boolean hasFocus,
                                                       int row,
                                                       int column) {
            setText(value.toString());
            setFont(new Font("Serif", Font.BOLD, 15));
            table.getColumnModel().getColumn(0).setPreferredWidth(120);
            table.getColumnModel().getColumn(table.getColumnCount() - 1).setPreferredWidth(50);
            table.getColumnModel().getColumn(table.getColumnCount() - 2).setPreferredWidth(50);
            if (column == table.getColumnCount() - 3 || column == table.getColumnCount() - 2 || column == table.getColumnCount() - 1) {
                setHorizontalAlignment(SwingConstants.RIGHT);
            } else {
                setHorizontalAlignment(SwingConstants.LEFT);
            }
            return this;
        }
    }
}