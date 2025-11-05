/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dept.upack;

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
public class TableFormNew extends JDialog {

    JPanel mainPanel;
    JLabel nameTableProduction;

    int x = 10;
    int y = 10;
    int iter = 0;

    DefaultTableModel tm;
    DefaultTableModel dtm2;
    DefaultTableModel tmForPrint;
    JButton closeButton, shapeButton, printButton, sizeProductionButton;
    JFormattedTextField startDateJFormattedTextField, endDateJFormattedTextField;

    Vector users = new Vector();
    Vector userTm2 = new Vector();
    Vector col; //  = new Vector();
    Vector colTm2; //  = new Vector();    
    String sd;
    String ed;
    int ot4ot;
    int n;
    JTable tm1;
    JTable tm2;
    String kol;
    String kolForPrint;
    String sumForPrint;
    String sum;
    String kolOfComplete = "";
    String kolOfCompleteForPrint = "";
    String sumOfComplete = "";

    public TableFormNew(JDialog parent, boolean modal, String sd, String ed, int ot4ot, int n) {
        super(parent, modal);
        initVariables(sd, ed, ot4ot, n);
        initComponents();
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
        iter++;
        mainPanel = new PanelWihtFone();
        startDateJFormattedTextField = new JFormattedTextField(sd);
        endDateJFormattedTextField = new JFormattedTextField(ed);
        closeButton = new JButton("Закрыть");
        shapeButton = new JButton("Сформировать");
        printButton = new JButton("Печать");
        sizeProductionButton = new JButton("Размеры");
        sizeProductionButton.setVisible(false);
        nameTableProduction = new JLabel();
        shapeButton.addActionListener(new ActionShape());
        closeButton.addActionListener(new ActionClose());
        printButton.addActionListener(new ActionPrint());
        sizeProductionButton.addActionListener(new ActionSizeProduction());
        mainPanel.setLayout(new BorderLayout(5, 5));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        //*************************************************************************************
        // Init centerPanel of mainPanel
        //*************************************************************************************        
        tm1 = new JTable(tm);
        tm1.getColumnModel().getColumn(0).setPreferredWidth(10);
        tm1.getColumnModel().getColumn(1).setPreferredWidth(20);
        tm1.getColumnModel().getColumn(2).setPreferredWidth(60);
        tm1.getColumnModel().getColumn(3).setPreferredWidth(150);
        tm1.getColumnModel().getColumn(4).setPreferredWidth(20);
        tm1.getColumnModel().getColumn(5).setPreferredWidth(20);
        tm1.getColumnModel().getColumn(6).setPreferredWidth(20);
        tm1.setDefaultRenderer(Object.class, new FlightTableRenderer());
        JTableHeader header = tm1.getTableHeader();
        header.setResizingAllowed(false);
        //header.setDefaultRenderer(new HeaderRenderer());
        //header.setReorderingAllowed(false);

        tm2 = new JTable(dtm2);
        tm2.getColumnModel().getColumn(0).setPreferredWidth(10);
        tm2.getColumnModel().getColumn(1).setPreferredWidth(20);
        tm2.getColumnModel().getColumn(2).setPreferredWidth(60);
        tm2.getColumnModel().getColumn(3).setPreferredWidth(150);
        tm2.getColumnModel().getColumn(4).setPreferredWidth(20);
        tm2.getColumnModel().getColumn(5).setPreferredWidth(20);
        tm2.getColumnModel().getColumn(6).setPreferredWidth(20);
        tm2.setDefaultRenderer(Object.class, new FlightTableRendererRes());

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));

        JScrollPane spData = new JScrollPane(tm1);
        spData.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        JScrollPane spResult = new JScrollPane(tm2);
        spResult.setPreferredSize(new Dimension(400, 18));
        spResult.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        spResult.setMaximumSize(new Dimension(5000, 18));
        centerPanel.add(nameTableProduction);
        centerPanel.add(Box.createVerticalStrut(15));
        centerPanel.add(spData);
        centerPanel.add(Box.createVerticalStrut(5));
        centerPanel.add(spResult);
        //*************************************************************************************
        // Init southPanel of mainPanel
        //*************************************************************************************             
        JPanel southPanel = new JPanel(new GridLayout(1, 2, 5, 5));
        JPanel southLeftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel southLeftGridPanel = new JPanel(new GridLayout(1, 2, 5, 5));
        southLeftGridPanel.add(printButton);
        southLeftGridPanel.add(sizeProductionButton);
        southLeftPanel.add(southLeftGridPanel);
        southPanel.add(southLeftPanel);
        JPanel southRightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        southRightPanel.add(closeButton);
        southPanel.add(southRightPanel);
        //*************************************************************************************
        // Init mainPanel
        //*************************************************************************************        
        mainPanel.add(southPanel, BorderLayout.SOUTH);
        mainPanel.add(centerPanel);
        if (ot4ot == 1) {
            this.setTitle("Сдача");
            nameTableProduction.setText("Сдано на склад в период с " + sd + " по " + ed);
        } else if (ot4ot == 2) {
            this.setTitle("Приём");
            nameTableProduction.setText("Принято на упаковку в период с " + sd + " по " + ed);
            sizeProductionButton.setVisible(true);
            //northRightGridPanel.setVisible(true);
        } else if (ot4ot == 3) {
            this.setTitle("Остатки");
            nameTableProduction.setText("Текущие остатки");
        }
        if (iter > 0) {
            centerPanel.printComponents(getGraphics());
        }
        add(mainPanel);
        setSize(970, 630);
        mainPanel.setMinimumSize(new Dimension(970, 630));
        //setLocationRelativeTo(parent);
        setResizable(true);
        //setTitle("Сдача на склад");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    @SneakyThrows
    private void initVariables(String sd, String ed, int ot4ot, int n) {
        this.sd = new String(sd);
        this.ed = new String(ed);
        this.ot4ot = ot4ot;
        this.n = n;
        col = new Vector();
        users = new Vector();
        colTm2 = new Vector();
        userTm2 = new Vector();

        DB db = new DB();
        if (ot4ot == 2) {
            users = db.sdachaNaUpack(sd, ed);
            col.add("№");
            col.add("Модель");
            col.add("Шифр артикула");
            col.add("Наименование");
            col.add("Сорт");
            col.add("Кол-во");
            col.add("Кол-во с комплектностью");
            col.add("Цена");
            col.add("Сумма");
            kol = parsStr(Long.toString(db.kol), 3);
            kolForPrint = Long.toString(db.kol);
            sum = parsStr(Long.toString(db.sum), 3);
            sumForPrint = Long.toString(db.sum);
            kolOfComplete = parsStr(Long.toString(db.kolForSdachaNaUpack), 3);
            kolOfCompleteForPrint = Long.toString(db.kolForSdachaNaUpack);
            sumOfComplete = parsStr(Long.toString(db.sumForSdachaNaUpack), 3);
            Vector usersFormate = new Vector();
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
                    if (numElement == 7 || numElement == 8) {
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
            tm = new DefaultTableModel(usersFormate, col);
            tmForPrint = new DefaultTableModel(users, col);
            userTm2.add("");
            userTm2.add("");
            userTm2.add("");
            userTm2.add("");
            userTm2.add("");
            userTm2.add("");
            userTm2.add("");
            userTm2.add("");
            userTm2.add("");
            colTm2.add("Итого");
            colTm2.add("");
            colTm2.add("");
            colTm2.add("");
            colTm2.add("");
            colTm2.add(kol.toString());
            colTm2.add(kolOfComplete.toString());
            colTm2.add("");
            colTm2.add(sum.toString());
            dtm2 = new DefaultTableModel(0, 9);
            dtm2.setColumnIdentifiers(userTm2);
            dtm2.addRow(colTm2);
        }
        if (ot4ot == 3) { // Остатки


        }

    }

    // Action
    private void ActionSizeProductionButton(java.awt.event.ActionEvent evt) {
        int row = -1;
        row = tm1.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(null, "Не выбрана ни одна строка", "Ошибка!!!", javax.swing.JOptionPane.ERROR_MESSAGE);
            return;
        } else {
            new RazmerForm(this, true, sd, ed, tm1.getValueAt(row, 1).toString(), tm1.getValueAt(row, 2).toString(), tm1.getValueAt(row, 4).toString());
        }
    }

    private void ActionShapeButton(java.awt.event.ActionEvent evt) {
        String srtSrart;
        srtSrart = startDateJFormattedTextField.getText().toString().trim();
        String srtEnd;
        srtEnd = endDateJFormattedTextField.getText().toString().trim();
        initVariables(startDateJFormattedTextField.getText().toString().trim(),
                endDateJFormattedTextField.getText().toString().trim(), ot4ot, n);
        initComponents();
        tm1.repaint();

        //new TableFormNew(this, true, srtSrart, srtEnd, ot4ot, n);        
    }

    /*
    class HeaderRenderer extends DefaultTableCellRenderer {
        // метод возвращает компонент для прорисовки
        @Override
        public Component getTableCellRendererComponent(
        JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        // получаем настроенную надпись от базового класса
        JLabel label = (JLabel)super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        // настраиваем особую рамку и цвет фона
        label.setBounds(0, 0, 50, 60);
        label.setBackground(Color.gray);
        label.setBorder(border);
        return label;
        }
        //private Border border = BorderFactory.createMatteBorder(-1, -1, 1, -1, Color.BLACK);
        
        private Border border = BorderFactory.createMatteBorder(16, 16, 16, 16, new ImageIcon("bullet.png"));
    }
    */
    //*********************************************************************
    // Action
    //*********************************************************************
    class ActionShape implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            ActionShapeButton(e);
        }
    }

    // Action closeButton
    class ActionClose implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            dispose();
        }
    }

    // printButton
    class ActionPrint implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            try {
                tmForPrint.addRow(new Object[]{"", "", "", "", "", kolForPrint.toString(), kolOfCompleteForPrint.toString(), "", sumForPrint});
                OpenOffice oo = new OpenOffice(nameTableProduction.getText(), tmForPrint);
                oo.createReport("ПринятоСданоУпаковка.ots");
            } catch (Exception pe) {
                System.err.println("Error printing: " + pe.getMessage());
            }
        }
    }

    // sizeProductionButton
    class ActionSizeProduction implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            ActionSizeProductionButton(e);
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

