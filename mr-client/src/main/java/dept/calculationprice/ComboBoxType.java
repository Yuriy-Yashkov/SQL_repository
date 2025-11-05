/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dept.calculationprice;

import javax.swing.*;
import javax.swing.event.ListDataListener;
import java.util.ArrayList;

/**
 * Комбобокс для выбора Типа
 * @author user
 */
public final class ComboBoxType implements ComboBoxModel {

    private Type selectedItem;
    private ArrayList<Type> typeList;
    private int index;

    public ComboBoxType(ArrayList<Type> typeList) {
        this.typeList = typeList;
        setSelectedItem(0);
    }

    @Override
    public Object getSelectedItem() {
        return selectedItem.getName();
    }

    @Override
    public void setSelectedItem(Object anItem) {
        selectedItem = typeList.get(index);

    }

    @Override
    public void addListDataListener(ListDataListener l) {
    }

    @Override
    public Object getElementAt(int index) {
        this.index = index;
        return typeList.get(index).getName();
    }

    @Override
    public int getSize() {
        return typeList.size();
    }

    @Override
    public void removeListDataListener(ListDataListener l) {
    }

    public int getId() {
        return selectedItem.getId();
    }

}
