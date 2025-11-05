/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dept.calculationprice;

import javax.swing.*;
import javax.swing.event.ListDataListener;
import java.util.ArrayList;

/**
 * Комбобокс для выбора группы
 *
 * @author user
 */
public final class ComboBoxGroup implements ComboBoxModel {

    private Group selectedItem;
    private ArrayList<Group> groupList;
    private int index;

    /**
     */
    public ComboBoxGroup(ArrayList<Group> groupList) {
        this.groupList = groupList;
        setSelectedItem(0);
    }

    @Override
    public Object getSelectedItem() {
        return selectedItem.getName();
    }

    @Override
    public void setSelectedItem(Object anItem) {
        selectedItem = groupList.get(index);

    }

    @Override
    public void addListDataListener(ListDataListener l) {
    }

    @Override
    public Object getElementAt(int index) {
        this.index = index;
        return groupList.get(index).getName();
    }

    @Override
    public int getSize() {
        return groupList.size();
    }

    @Override
    public void removeListDataListener(ListDataListener l) {
    }

    public int getId() {
        return selectedItem.getId();
    }

}
