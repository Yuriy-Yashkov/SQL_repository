package dept.sklad.ostatki;

import common.PanelWihtFone;
import workDB.DB;
import workDB.PDB;
import workOO.OpenOffice;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 *
 * @author vova
 */
public class OstResultForm extends JDialog {

    Vector columns = null;
    Vector rows;
    String str;
    private JPanel mainPanel;
    private JTable table;
    private JLabel lHead;
    private DefaultTableModel tModel;
    private JScrollPane scrollTable;
    private JButton bClose;
    private JButton bPrint;
    private int x = 10;
    private int y = 10;

    public OstResultForm(JDialog parent, boolean f, int type, String date, ArrayList column, List row) {
        super(parent, f);
        columns = new Vector(column);

        PDB pdb = new PDB();
        switch (type) {
            case 0: {
                str = new String("Текущие остатки");
                rows = new Vector(row);//new Vector(db.realOstSklad());
                break;
            }
            case 1: {
                str = new String("Остатки на начало месяца");
                rows = new Vector(row);//new Vector(pdb.monthOstSklad(Integer.parseInt(date)));
                break;
            }
            case 2: {
                str = new String("Остатки на " + date);
                rows = new Vector(row);//new Vector(pdb.dateOstSklad(date));
                break;
            }
        }
        pdb.disConn();
        initComponents();
        lHead.setText(str);
        setTitle("Остатки");
        add(mainPanel);
        setSize(310 + columns.size() * 50, 500);
        setResizable(false);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(parent);
        setVisible(true);
    }

    public void initComponents() {
        mainPanel = new PanelWihtFone();

        float curs = 1;

        //создаём таблицу
        tModel = new DefaultTableModel(rows, columns) {
            @Override
            public Class getColumnClass(int column) {
                Class returnValue;
                if ((column >= 0) && (column < getColumnCount())) {
                    returnValue = getValueAt(0, column).getClass();
                } else {
                    returnValue = Object.class;
                }
                return returnValue;
            }
        };

        String valuta = tModel.getColumnName(tModel.getColumnCount() - 1);
        valuta = valuta.substring(valuta.indexOf("(") + 1, valuta.length() - 1);
        DB db = new DB();
        curs = db.getCurrencyKurs(valuta);

        for (int i = 0; i < tModel.getRowCount(); i++) {
            tModel.setValueAt(Long.parseLong(tModel.getValueAt(i, tModel.getColumnCount() - 1).toString()) / curs, i, tModel.getColumnCount() - 1);
        }
        table = new JTable(tModel);
        final TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(tModel);
        table.setRowSorter(sorter);
        scrollTable = new JScrollPane(table);
        scrollTable.setBounds(x, y + 20, 280 + columns.size() * 50, 400);
        mainPanel.add(scrollTable);

        lHead = new JLabel();
        lHead.setBounds(x + 100, y, 200, 20);
        mainPanel.add(lHead);

        bClose = new JButton("Close");
        bClose.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        bClose.setBounds((310 + columns.size() * 50) / 2 - 60, y + 430, 120, 20);
        mainPanel.add(bClose);

        bPrint = new JButton("Печать");
        bPrint.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    /*MessageFormat headerFormat = new MessageFormat(lHead.getText());
                    MessageFormat footerFormat = new MessageFormat("- {0} -");
                    table.print(JTable.PrintMode.FIT_WIDTH, headerFormat, footerFormat);*/
                    OpenOffice oo = new OpenOffice(lHead.getText(), tModel);
                    oo.createReport("Остатки.ots");
                } catch (Exception er) {
                    System.err.println("Ошибка при печати " + er);
                }
            }
        });
        bPrint.setBounds(20, y + 430, 120, 20);
        mainPanel.add(bPrint);

    }
}
