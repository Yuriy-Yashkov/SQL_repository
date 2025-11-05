package dept.calculationprice.model;

import dept.calculationprice.MyBean;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;

/**
 * @author Andy 20.10.2015.
 */
public class CalculationBeanTableModel extends AbstractTableModel {
    /**
     * Модель  главной таблицы для отображения калькуляции ЦЕНЫ
     */
    /*  private Set<TableModelListener> listeners = new HashSet<TableModelListener>();*/
    private ArrayList<MyBean> dataModel;

    /**
     * @param beans
     */
    public CalculationBeanTableModel(ArrayList<MyBean> beans) {
        dataModel = beans;
    }

     /*   public void addTableModelListener(TableModelListener listener) {
            listeners.add(listener);
        }*/

    public Class<?> getColumnClass(int columnIndex) {
        switch (columnIndex) {
            case 0:
                return Boolean.class;
            case 1:
                return String.class;
            case 2:
                return String.class;
            case 3:
                return String.class;
            case 4:
                return String.class;
            case 5:
                return String.class;
            case 6:
                return String.class;
            //return bean.getBetNds();
            case 7:
                return String.class;
            case 8:
                return String.class;
            case 9:
                return String.class;

            case 10:
                return String.class;
            case 11:
                return Integer.class;
            default:
                return String.class;
        }
    }

    public int getColumnCount() {
        return 12;
    }

    public String getColumnName(int columnIndex) {
        switch (columnIndex) {
            case 0:
                return "+";
            case 1:
                return "Наименование изделия";
            case 2:
                return "Название  артикула";
            case 3:
                return "Модель";
            case 4:
                return "Размер";
            case 5:
                return "Цена без НДС";
            case 6:
                return "";
            //return "Ставка НДС %";
            case 7:
                return "Себестоимость";
            //return "Цена с НДС";
            case 8:
                return "Примечание";
            case 9:
                return "Подрядчик";
            case 10:
                return "Полотно";
            case 11:
                return "ID";
        }

        return "";
    }

    public int getRowCount() {
        return dataModel.size();
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        /*    if (rowIndex>dataModel.size()){
                rowIndex = 0 ;
            }*/
        // int index_ = jTableListCalculation.convertRowIndexToView(rowIndex);
        MyBean bean = dataModel.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return bean.isCheck();
            case 1:
                return bean.getName();
            case 2:
                return bean.getArticle();
            case 3:
                return bean.getModel();
            case 4:
                return bean.getSize();
            case 5:
                return String.format("%.2f", Float.valueOf(bean.getCostWithoutNds().replace(",", ".")));
            case 6:
                return "";
            //return bean.getBetNds();
            case 7:
                return String.format("%.4f", bean.getCc());
            case 8:
                return bean.getPrim();
            case 9:
                return bean.getNIZ1();

            case 10:
                return bean.getPOL();
            case 11:
                return bean.getId();

        }
        return "";
    }

    @Override
    public void setValueAt(final Object aValue, final int rowIndex, final int columnIndex) {

        if (columnIndex == 0) {
            MyBean bean = dataModel.get(rowIndex);
            bean.setCheck((boolean) aValue);
        }
    }

    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return columnIndex == 0;
    }

  /*      public void removeTableModelListener(TableModelListener listener) {
            listeners.remove(listener);
        }*/

/*        public void updateModel(final ArrayList<MyBean> beans) {
            dataModel.clear();
            dataModel.addAll(beans);
            fireTableDataChanged();
            //System.out.println("Размер " + beans.size());
            fireTableRowsInserted(beans.size(), beans.size());
            *//*this.fireTableDataChanged();
            super.fireTableDataChanged();*//*
        }*/

    public int getIndexById(final int id_new) {
        for (int i = 0; i < dataModel.size(); i++) {
            if (dataModel.get(i).getId() == id_new) {
                return i;
            }
        }
        return -1;
    }
}
