package dept.calculationprice.model;

/**
 * @ author Andy 20.10.2015.
 */

import dept.calculationprice.MyBean;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;

/**
 * Модель таблицы для прейскурантов ЦЕНЫ
 */
public class MyTableModelPriceCen extends AbstractTableModel {

    //    private Set<TableModelListener> listeners = new HashSet<TableModelListener>();
    private ArrayList<MyBean> dataModel;

    /**
     * @param beans
     */
    public MyTableModelPriceCen(ArrayList<MyBean> beans) {
        this.dataModel = beans;
    }

/*    public void addTableModelListener(TableModelListener listener) {
        listeners.add(listener);
    }*/

    public Class<?> getColumnClass(int columnIndex) {
        if (columnIndex == 8) {
            return Boolean.class;
        }
        return String.class;
    }

    public int getColumnCount() {
        return 9;
    }


    public String getColumnName(int columnIndex) {
        switch (columnIndex) {
            case 0:
                return "Наименование изделия";
            case 1:
                return "Название  артикула";
            case 2:
                return "Модель";
            case 3:
                return "Размер";
            case 4:
                return "Цена без НДС";
            case 5:
                return "Ставка НДС %";
            case 6:
                return "Цена с НДС";
            case 7:
                return "Примечание";
            case 8:
                return "Выбор";
        }

        return "";
    }

    public int getRowCount() {
        return dataModel.size();
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        MyBean bean = dataModel.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return bean.getName();
            case 1:
                return bean.getArticle();
            case 2:
                return bean.getModel();
            case 3:
                return bean.getSize();
            case 4:
                return String.format("%.2f", Float.valueOf(bean.getCostWithoutNds().replace(",", ".")));
            case 5:
                return bean.getBetNds();
            case 6:
                return String.format("%.2f", Float.valueOf(bean.getCostWithNds().replace(",", ".")));
            case 7:
                return bean.getPrim();
            case 8:
                return Boolean.valueOf(bean.isCheck());
            case 9:
                return bean.getId();

        }
        return "";
    }

    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return columnIndex == 8;
    }

/*    public void removeTableModelListener(TableModelListener listener) {
        listeners.remove(listener);
    }*/

    public void setValueAt(Object value, int rowIndex, int columnIndex) {
        System.out.println(value);
        MyBean bean = dataModel.get(rowIndex);
        switch (columnIndex) {
            case 8:
                bean.setCheck(Boolean.valueOf(value.toString()));

                break;

        }

    }

/*    public void updateModel(final ArrayList<MyBean> beans) {
        dataModel.clear();
        dataModel.addAll(beans);
        fireTableDataChanged();
    }*/
}