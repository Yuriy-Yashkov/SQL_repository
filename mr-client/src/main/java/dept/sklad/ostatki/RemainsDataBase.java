/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dept.sklad.ostatki;

import by.march8.ecs.framework.common.LogCrutch;
import workDB.DB_new;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author user
 */
@SuppressWarnings({"serial"})
public class RemainsDataBase extends DB_new {

    // private static final Logger log = new Log().getLoger(RemainsDataBase.class);
    private static final LogCrutch log = new LogCrutch();
    private ResultSetMetaData rsmd;
    private int columnCount;
    private ArrayList<Object> columnName;

    /**
     *
     * @param nameView
     * @return
     */
    public ArrayList<ArrayList<Object>> getDataView(String nameView) {
        ArrayList<ArrayList<Object>> data = new ArrayList<ArrayList<Object>>();
        try {
            ps = conn.prepareStatement("select * from " + nameView);
            rs = ps.executeQuery();
            rsmd = rs.getMetaData();
            columnCount = rsmd.getColumnCount();
            setHeaderLabel();
            data.add(columnName);
            while (rs.next()) {
                ArrayList<Object> arBuffer = new ArrayList<Object>();
                int i = 1;
                while (i <= columnCount) {
                    arBuffer.add(rs.getObject(i));
                    i++;
                }
                data.add(arBuffer);
            }
        } catch (SQLException ex) {
            System.out.println(ex + this.getClass().getName().toString());
        }
        return data;
    }

    /**
     *
     * @param query
     * @return
     */
    public ArrayList<ArrayList<Object>> getDataQuery(String query) {
        ArrayList<ArrayList<Object>> data = new ArrayList<ArrayList<Object>>();
        try {
            ps = conn.prepareStatement(query);
            rs = ps.executeQuery();
            rsmd = rs.getMetaData();
            columnCount = rsmd.getColumnCount();
            while (rs.next()) {
                ArrayList<Object> arBuffer = new ArrayList<Object>();
                int i = 1;
                while (i <= columnCount) {
                    arBuffer.add(rs.getObject(i));
                    i++;
                }
                data.add(arBuffer);
            }
        } catch (SQLException ex) {
            System.out.println(ex + this.getClass().getName().toString());
        }
        return data;
    }

    /**
     *
     * @param query
     * @return
     */
    public ArrayList<ArrayList<Object>> getDataWithCondition(String query) {
        ArrayList<ArrayList<Object>> data = new ArrayList<ArrayList<Object>>();
        try {
            ps = conn.prepareStatement(query);
            rs = ps.executeQuery();
            rsmd = rs.getMetaData();
            columnCount = rsmd.getColumnCount();
            while (rs.next()) {
                ArrayList<Object> arBuffer = new ArrayList<Object>();
                int i = 1;
                while (i <= columnCount) {
                    arBuffer.add(rs.getObject(i));
                    i++;
                }
                data.add(arBuffer);
            }
        } catch (SQLException ex) {
            System.out.println(ex + this.getClass().getName().toString());
        }
        return data;
    }

    public void setHeaderLabel() {
        columnName = new ArrayList<Object>();
        try {
            for (int i = 1; i <= columnCount; i++) {
                columnName.add(rsmd.getColumnName(i));
            }
        } catch (SQLException ex) {
        }

    }
}
