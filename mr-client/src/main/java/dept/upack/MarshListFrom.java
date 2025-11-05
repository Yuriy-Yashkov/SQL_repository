package dept.upack;

import common.ProgressBar;
import workDB.DB;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

/**
 * @author vova
 */
public class MarshListFrom extends JDialog {

    JPanel mainPanel;
    JLabel lHead;
    JTable table;
    JScrollPane scrollPanel;
    JButton bClose;
    TableModel tModel;
    Vector rows;
    Vector columns;
    ProgressBar pb;
    private int x = 10;
    private int y = 10;

    public MarshListFrom(JFrame parent, boolean f) {
        super(parent, f);
        initComponents();
        setTitle("Потерянные маршрутные лиcты");
        add(mainPanel);
        setSize(310 + columns.size() * 50, 400);
        setResizable(false);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(parent);
        setVisible(true);
    }

    private void initComponents() {
        mainPanel = new JPanel(null);

        columns = new Vector();
        columns.add("#");
        columns.add("Номер");
        columns.add("Код");
        columns.add("Кол-во");
        rows = new Vector();

        pb = new ProgressBar(this, false, "Поиск маршрутов");
        SwingWorker sw = new SwingWorker() {
            @Override
            protected Object doInBackground() throws Exception {
                DB db = new DB();
                rows = new Vector(db.getLoseMarshList(""));
                return null;
            }

            @Override
            protected void done() {
                pb.dispose();
            }
        };
        sw.execute();
        pb.setVisible(true);

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
        table = new JTable(tModel);
        final TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(tModel);
        table.setRowSorter(sorter);
        table.getColumnModel().getColumn(0).setPreferredWidth(50);
        table.getColumnModel().getColumn(1).setPreferredWidth(100);
        table.getColumnModel().getColumn(2).setPreferredWidth(100);
        scrollPanel = new JScrollPane(table);
        scrollPanel.setBounds(x, y + 20, 280 + columns.size() * 50, 300);
        mainPanel.add(scrollPanel);

        if (rows.size() > 0) {
            lHead = new JLabel("АЙ-ЯЙ-ЯЙ...");
        } else lHead = new JLabel("Всё хорошо");
        lHead.setBounds(x + 50, y, 150, 20);
        mainPanel.add(lHead);

        bClose = new JButton("Close");
        bClose.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        bClose.setBounds((310 + columns.size() * 50) / 2 - 60, y + 330, 120, 20);
        mainPanel.add(bClose);

    }
}
