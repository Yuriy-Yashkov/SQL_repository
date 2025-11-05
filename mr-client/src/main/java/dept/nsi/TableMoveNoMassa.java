/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dept.nsi;

import common.PanelWihtFone;
import lombok.SneakyThrows;
import workDB.DB;
import workOO.OpenOffice;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

/**
 *
 * @author user
 */
public class TableMoveNoMassa extends JDialog {
    Vector columns = null;
    Vector rows;
    Vector rowsForPrint;
    String str;
    private JPanel mainPanel;
    private JTable table;
    private JLabel nameTable;
    private DefaultTableModel tModel;
    private JButton bClose;
    private JButton bPrint;
    private Vector rowTableMove = new Vector();
    private Vector columsTableMove = new Vector();
    private String beginDate;
    private String endDate;

    @SneakyThrows
    public TableMoveNoMassa(JDialog parent, boolean f, String bDate, String eDate) {
        super(parent, f);
        this.beginDate = new String(bDate);
        this.endDate = new String(eDate);

        nameTable = new JLabel("Перемещение на склад с массой равной нулю в период с " + beginDate + " по " + endDate);
        DB db = new DB();
        rowTableMove = db.getNaclNoMassa(beginDate, endDate);
        columsTableMove.add("№");
        columsTableMove.add("Модель");
        columsTableMove.add("Шифр артикула");
        columsTableMove.add("Артикул");
        columsTableMove.add("Название");
        tModel = new DefaultTableModel(rowTableMove, columsTableMove);
        table = new JTable(tModel);
        table.getColumnModel().getColumn(0).setPreferredWidth(5);
        table.getColumnModel().getColumn(1).setPreferredWidth(15);
        table.getColumnModel().getColumn(2).setPreferredWidth(60);
        table.getColumnModel().getColumn(3).setPreferredWidth(60);
        table.getColumnModel().getColumn(4).setPreferredWidth(60);
        table.setDefaultRenderer(Object.class, new TableMoveNoMassa.FlightTableRenderer());
        JTableHeader header = table.getTableHeader();
        header.setResizingAllowed(false);

        initComponents();

        this.setTitle("Внутреннее перемещение");
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
        bClose.addActionListener(new TableMoveNoMassa.ActionClose());
        bPrint = new JButton("Печать");
        bPrint.addActionListener(new TableMoveNoMassa.ActionPrint());
        mainPanel.setLayout(new BorderLayout(5, 5));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        //*************************************************************************************
        // Init centerPanel of mainPanel
        //*************************************************************************************                
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        JScrollPane spData = new JScrollPane(table);
        spData.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        centerPanel.add(nameTable);
        centerPanel.add(Box.createVerticalStrut(15));
        centerPanel.add(spData);
        centerPanel.add(Box.createVerticalStrut(5));

        //centerPanel.add(spResult);        //  
        //*************************************************************************************
        // Init southPanel of mainPanel
        //*************************************************************************************             
        JPanel southPanel = new JPanel(new GridLayout(1, 2, 5, 5));
        JPanel southLeftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        southLeftPanel.add(bPrint);
        JPanel southRightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        southRightPanel.add(bClose);
        JPanel southLeftGridPanel = new JPanel(new GridLayout(1, 2, 5, 5));
        southLeftGridPanel.add(southLeftPanel);
        southLeftGridPanel.add(southRightPanel);
        southPanel.add(southLeftGridPanel);
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

    class ActionPrint implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            try {
                OpenOffice oo = new OpenOffice(nameTable.getText(), tModel);
                oo.createReport("ПеремещениеБезМассы.ots");
            } catch (Exception pe) {
                System.err.println("Error printing: " + pe.getMessage());
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
