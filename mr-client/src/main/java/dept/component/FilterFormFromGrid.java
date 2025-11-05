/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dept.component;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.util.ArrayList;

/**
 *
 * @author user
 */
public class FilterFormFromGrid extends JDialog {

    Object[][] dt;
    Object[] cn;
    private ArrayList<String> nameColumn;
    private JTable jtTableFilter;
    private JComboBox jcbColName;
    private JTable jTab;
    private JScrollPane jsp;
    private DefaultTableModel dtm;
    private TableModel tm;


    public FilterFormFromGrid(Object parent, int filter, Table dat) {

        super();
        jcbColName = new JComboBox();
        nameColumn = new ArrayList<String>();
        for (int i = 0; i < dat.getMyTable().getColumnCount(); i++) {
            jcbColName.addItem(dat.getMyTable().getColumnName(i));
            //nameColumn.add(data.getMyTable().getColumnName(i));
        }

        jTab = new JTable();

        dt = new Object[1][3];
        dt[0][0] = jcbColName;
        dt[0][1] = 1;
        dt[0][2] = true;
        cn = new Object[3];
        cn[0] = "nameCol";
        cn[1] = "nameCo";
        cn[2] = "nameC";


        //dtm = new DefaultTableModel(data, columnNames);
        dtm = new DefaultTableModel(dt, cn) {
            public Class getColumnClass(int row, int column) {
                return dt[0][column].getClass(); //dt[0][column].getClass(); //dataType;
            }
        };


        jcbColName.addItemListener(e -> JOptionPane.showMessageDialog(null, jcbColName.getItemAt(jcbColName.getSelectedIndex())));

        jTab.setModel(dtm);
        //jTab.setValueAt(jcbColName.getItemAt(0).toString(), row, column);
        jTab.getColumnModel().getColumn(0).setCellEditor(new DefaultCellEditor(jcbColName));
        jTab.setValueAt(jcbColName.getItemAt(0), 0, 0);
        //jTab.getValueAt(0, 0);
        jsp = new JScrollPane(jTab);


//        TableColumn gradeColumn = jTab.getColumnModel().getColumn(0);
//        //final JComboBox comboBox = new JComboBox();
//        gradeColumn.setCellEditor(new DefaultCellEditor(jcbColName));
//        
        //jsp.setViewportView(jTab);

        this.add(jsp);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setSize(new Dimension(640, 480));
        setVisible(true);

    }

}
