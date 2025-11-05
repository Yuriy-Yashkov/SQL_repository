package dept.mylog;

import common.PanelWihtFone;
import workDB.PDB;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

/**
 *
 * @author vova
 */
public class LoginResultForm extends JDialog {

    PanelWihtFone mainPanel;
    JLabel lHead;
    JButton bClose;
    JButton bDelete;
    JTable table;
    DefaultTableModel tableModel;
    int x = 10;
    int y = 10;
    Vector columns = new Vector();
    Vector rows = new Vector();
    JScrollPane tableScroll;
    private String name;


    public LoginResultForm(JDialog frame, boolean f, String name, String sDate, String eDate) {
        super(frame, f);
        this.name = name;
        setTitle("Результаты за период с " + sDate + " по " + eDate);
        columns.add("IP");
        columns.add("Пользователь");
        columns.add("Дата");
        columns.add("Действие");
        PDB pdb = new PDB();
        rows = pdb.getUserLogin(name, sDate, eDate);
        pdb.disConn();
        initComponents();
        add(mainPanel);
        setSize(450, 500);
        setResizable(false);
        setLocationRelativeTo(frame);
        setVisible(true);
    }

    private void initComponents() {
        mainPanel = new PanelWihtFone();

        lHead = new JLabel("Данные для пользователя " + name);
        lHead.setBounds(x + 20, y, 370, 20);
        mainPanel.add(lHead);

        tableModel = new DefaultTableModel(rows, columns);
        table = new JTable(tableModel);
        table.getColumnModel().getColumn(0).setPreferredWidth(120);
        table.getColumnModel().getColumn(1).setPreferredWidth(120);
        table.getColumnModel().getColumn(2).setPreferredWidth(175);
        table.getColumnModel().getColumn(3).setPreferredWidth(80);

        table.setAutoscrolls(true);
        tableScroll = new JScrollPane(table);
        tableScroll.setBounds(x, y + 30, 430, 380);
        mainPanel.add(tableScroll);

        bClose = new JButton("Закрыть");
        bClose.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        bClose.setBounds(x + 175, y + 420, 100, 20);
        mainPanel.add(bClose);

    }
}
