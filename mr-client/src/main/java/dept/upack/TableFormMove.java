/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dept.upack;

import common.PanelWihtFone;
import lombok.SneakyThrows;
import workDB.DB;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Vector;

/**
 *
 * @author user
 */
public class TableFormMove extends JDialog {
    Vector columns = null;
    Vector rows;
    Vector rowsForPrint;
    String str;
    private JPanel mainPanel;
    private JTable table;
    private JTable summTable;
    private JLabel nameTable;
    private JTextField tfSearch;
    private DefaultTableModel tModel;
    private DefaultTableModel tModelSumm;
    private JScrollPane scrollTable;
    private JScrollPane scrollTableSum;
    private JButton bClose;
    private Vector rowTableMove = new Vector();
    private Vector columsTableMove = new Vector();
    private Vector rowTableSum = new Vector();
    private Vector columsTableSum = new Vector();
    private String article;
    private String beginDate;
    private String endDate;
    private boolean flagTableSumm = false;
    private String kol;
    private String kolOfComplete;
    private long sum;

    @SneakyThrows
    public TableFormMove(JDialog parent, boolean f, String article, String bDate, String eDate) {
        super(parent, f);
        this.article = new String(article);
        this.beginDate = new String(bDate);
        this.endDate = new String(eDate);
        flagTableSumm = true;
        nameTable = new JLabel("Перемещение по артиклу " + article + " в период с " + beginDate + " по " + endDate);
        DB db = new DB();
        rowTableMove = db.getNaclForArticle(article, beginDate, endDate);
        columsTableMove.add("№");
        columsTableMove.add("№ документа");
        columsTableMove.add("Дата");
        columsTableMove.add("Кол-во");
        columsTableMove.add("Кол-во с ком.");
        columsTableMove.add("Размер");
        columsTableMove.add("Артикул");
        columsTableMove.add("Наименование");
        kol = parsStr(Long.toString(db.kol), 3);
        kolOfComplete = parsStr(Long.toString(db.kolForSdachaNaUpack), 3);

        rowTableSum.add("");
        rowTableSum.add("");
        rowTableSum.add("");
        rowTableSum.add("");
        rowTableSum.add("");
        rowTableSum.add("");
        rowTableSum.add("");
        rowTableSum.add("");

        columsTableSum.add("Итого");
        columsTableSum.add("");
        columsTableSum.add("");
        columsTableSum.add(kol);
        columsTableSum.add(kolOfComplete);
        columsTableSum.add("");
        columsTableSum.add("");
        columsTableSum.add("");

        tModel = new DefaultTableModel(rowTableMove, columsTableMove);
        tModelSumm = new DefaultTableModel(0, 9);
        tModelSumm.setColumnIdentifiers(rowTableSum);
        tModelSumm.addRow(columsTableSum);

        table = new JTable(tModel);
        table.getColumnModel().getColumn(0).setPreferredWidth(5);
        table.getColumnModel().getColumn(1).setPreferredWidth(60);
        table.getColumnModel().getColumn(2).setPreferredWidth(60);
        table.getColumnModel().getColumn(3).setPreferredWidth(30);
        table.getColumnModel().getColumn(4).setPreferredWidth(30);
        table.getColumnModel().getColumn(5).setPreferredWidth(40);
        table.getColumnModel().getColumn(6).setPreferredWidth(40);
        table.setDefaultRenderer(Object.class, new TableFormMove.FlightTableRenderer());
        JTableHeader header = table.getTableHeader();
        header.setResizingAllowed(false);

        summTable = new JTable(tModelSumm);
        summTable.getColumnModel().getColumn(0).setPreferredWidth(5);
        summTable.getColumnModel().getColumn(1).setPreferredWidth(60);
        summTable.getColumnModel().getColumn(2).setPreferredWidth(60);
        summTable.getColumnModel().getColumn(3).setPreferredWidth(30);
        summTable.getColumnModel().getColumn(4).setPreferredWidth(30);
        summTable.getColumnModel().getColumn(5).setPreferredWidth(40);
        summTable.getColumnModel().getColumn(6).setPreferredWidth(40);
        summTable.setDefaultRenderer(Object.class, new TableFormMove.FlightTableRendererRes());

        initComponents();

        this.setTitle("Внутреннее перемещение по артиклу");
        add(mainPanel);
        setSize(970, 630);
        mainPanel.setMinimumSize(new Dimension(970, 630));
        setResizable(true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    @SneakyThrows
    public TableFormMove(JDialog parent, boolean f, String bDate, String eDate) {
        super(parent, f);
        this.beginDate = new String(bDate);
        this.endDate = new String(eDate);
        nameTable = new JLabel("Перемещение с упаковки на склад в период с " + beginDate + " по " + endDate);
        DB db = new DB();
        rowTableMove = db.getNaclForArticle(beginDate, endDate);
        columsTableMove.add("№");
        columsTableMove.add("Артикул");
        columsTableMove.add("Наименование");
        columsTableMove.add("Кол-во упак.");
        columsTableMove.add("Кол-во.");
        kol = parsStr(Long.toString(db.kol), 3);
        kolOfComplete = parsStr(Long.toString(db.kolForSdachaNaUpack), 3);
        rowTableSum.add("");
        rowTableSum.add("");
        rowTableSum.add("");
        rowTableSum.add("");
        rowTableSum.add("");
        columsTableSum.add("Итого");
        columsTableSum.add("");
        columsTableSum.add("");
        columsTableSum.add(kol);
        columsTableSum.add(kolOfComplete);

        tModel = new DefaultTableModel(rowTableMove, columsTableMove);
        tModelSumm = new DefaultTableModel(0, 9);
        tModelSumm.setColumnIdentifiers(rowTableSum);
        tModelSumm.addRow(columsTableSum);

        table = new JTable(tModel);
        final TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(tModel);
        table.setRowSorter(sorter);
        table.getColumnModel().getColumn(0).setPreferredWidth(5);
        table.getColumnModel().getColumn(1).setPreferredWidth(60);
        table.getColumnModel().getColumn(2).setPreferredWidth(60);
        table.getColumnModel().getColumn(3).setPreferredWidth(30);
        table.getColumnModel().getColumn(4).setPreferredWidth(30);

        table.setDefaultRenderer(Object.class, new TableFormMove.FlightTableRenderer());
        JTableHeader header = table.getTableHeader();
        header.setResizingAllowed(false);

        summTable = new JTable(tModelSumm);
        summTable.getColumnModel().getColumn(0).setPreferredWidth(5);
        summTable.getColumnModel().getColumn(1).setPreferredWidth(60);
        summTable.getColumnModel().getColumn(2).setPreferredWidth(60);
        summTable.getColumnModel().getColumn(3).setPreferredWidth(30);
        summTable.getColumnModel().getColumn(4).setPreferredWidth(30);

        summTable.setDefaultRenderer(Object.class, new TableFormMove.FlightTableRendererRes());
        tfSearch = new JTextField(15);
        tfSearch.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(java.awt.event.KeyEvent evt) {
                if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
                    sorter.setRowFilter(RowFilter.regexFilter(tfSearch.getText(), 1));
                } else if (evt.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    tfSearch.setText("");
                    sorter.setRowFilter(null);
                }
            }
        });


        initComponents();

        this.setTitle("Внутреннее перемещение по артиклу");
        add(mainPanel);
        setSize(970, 630);
        mainPanel.setMinimumSize(new Dimension(970, 630));
        setResizable(true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setVisible(true);


    }

    public String parsStr(String str, int razr) {
        StringBuffer s = new StringBuffer(str);
        int i = s.length() - razr;
        for (; i > 0; i = i - razr) {
            s.insert(i, " ");
        }
        return s.toString();
    }

    private void initComponents() {
        mainPanel = new PanelWihtFone();
        bClose = new JButton("Закрыть");
        bClose.addActionListener(new TableFormMove.ActionClose());


        mainPanel.setLayout(new BorderLayout(5, 5));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        //*************************************************************************************
        // Init centerPanel of mainPanel
        //*************************************************************************************        

        //header.setDefaultRenderer(new HeaderRenderer());
        //header.setReorderingAllowed(false);


        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));

        JScrollPane spData = new JScrollPane(table);
        spData.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        JScrollPane spResult = new JScrollPane(summTable);
        spResult.setPreferredSize(new Dimension(400, 18));
        spResult.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        spResult.setMaximumSize(new Dimension(5000, 18));
        centerPanel.add(nameTable);
        centerPanel.add(Box.createVerticalStrut(15));
        centerPanel.add(spData);
        centerPanel.add(Box.createVerticalStrut(5));
        if (flagTableSumm) {
            centerPanel.add(spResult);
        }
        //centerPanel.add(spResult);        //  
        //*************************************************************************************
        // Init southPanel of mainPanel
        //*************************************************************************************             
        JPanel southPanel = new JPanel(new GridLayout(1, 2, 5, 5));
        JPanel southLeftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel southLeftGridPanel = new JPanel(new GridLayout(1, 1, 5, 5));


        if (!flagTableSumm) {
            southLeftPanel.add(tfSearch);
            southLeftGridPanel.add(southLeftPanel);
            southPanel.add(southLeftGridPanel);
        }
        JPanel southRightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        southRightPanel.add(bClose);
        southPanel.add(southRightPanel);
        //*************************************************************************************
        // Init mainPanel
        //*************************************************************************************        
        mainPanel.add(southPanel, BorderLayout.SOUTH);
        mainPanel.add(centerPanel);


    }

    class ActionClose implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            dispose();
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

            if (isSelected) {
                setBackground(Color.GREEN);
                //setForeground(Color.BLACK);
            } else {
                setBackground(Color.WHITE);
                //setForeground(Color.BLACK);
            }

            if (column == 4 || column == 5 || column == 6 || column == 7 || column == 8) {
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
            if (column == 4 || column == 5 || column == 6 || column == 7 || column == 8) {
                setHorizontalAlignment(SwingConstants.RIGHT);
            } else {
                setHorizontalAlignment(SwingConstants.LEFT);
            }

            return this;

        }
    }

}