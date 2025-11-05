/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dept.component;

import dept.sklad.ostatki.RemainsDataBase;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.util.ArrayList;

/**
 *
 * @author user
 */
@SuppressWarnings({"serial"})
public class Table extends JScrollPane {

    boolean tableModelListenerIsChanging = false;
    int minSelectedRow = -1;
    int maxSelectedRow = -1;
    private JTable myTable;
    private DefaultTableModel dtm;
    private Object[][] dataTable;
    private Object[] columnName;
    private RemainsDataBase rdb;
    private ArrayList<ArrayList<Object>> data;

    public Table() {
    }

    /**
     * Создает таблицу
     *
     * @param view - массив данных (c названиями колонок)
     */
    public Table(String view) {
        try {
            rdb = new RemainsDataBase();
            data = rdb.getDataView(view);

            columnName = new Object[data.get(0).size()];
            for (int j = 0; j < data.get(0).size(); j++) {
                columnName[j] = data.get(0).get(j);
            }
            data.remove(0);//удаляем названия столбцов
            data.trimToSize();//ужимаем массив (после удаления остается пустое место, эта функция его удаляет)

            if (data.size() > 0) {
                dataTable = new Object[data.size()][data.get(0).size()];
            } else {
                dataTable = new Object[0][columnName.length];
            }

            for (int i = 0; i < data.size(); i++) {
                ArrayList<Object> temp = data.get(i);
                for (int j = 0; j < columnName.length; j++) {
                    dataTable[i][j] = temp.get(j);
                }
            }
            dtm = new DefaultTableModel(dataTable, columnName) {
                public Class getColumnClass(int column) {
                    return dataTable[0][column].getClass(); //dataType;
                }
            };
            myTable = new JTable();
            myTable.setModel(dtm);
            TableRowSorter<TableModel> sorter = new TableRowSorter(dtm);
            myTable.setRowSorter(sorter);
            this.setViewportView(myTable);
        } catch (Exception ex) {
            System.out.println(ex + this.getClass().getName().toString());
        }
    }

    /**
     *
     * @param query
     */
    public void updateTable(ArrayList<ArrayList<Object>> query) {
        data = query;
        try {
            dataTable = new Object[data.size()][columnName.length];
            for (int i = 0; i < data.size(); i++) {
                ArrayList<Object> temp = data.get(i);
                for (int j = 0; j < columnName.length; j++) {
                    dataTable[i][j] = temp.get(j);
                }
            }
            DefaultTableModel dtmBuf = new DefaultTableModel(dataTable, columnName);
            this.myTable = new JTable(dtmBuf);
        } catch (Exception ex) {
            System.out.println(ex + this.getClass().getName().toString());
        }

    }

    public ArrayList<ArrayList<Object>> getData() {
        return data;
    }

    public void setView(boolean param) {
        this.setViewportView(myTable);
    }

    public JTable getMyTable() {
        return myTable;
    }
}
