/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dept.calculationprice;

import javax.swing.*;
import javax.swing.event.ListDataListener;
import java.util.ArrayList;

/**
 * Комбобокс для выбора Типа Продукции
 *
 * @author user
 */
public final class ComboBoxTypeProduction implements ComboBoxModel {
    private TypeProduction selectedItem;
    private ArrayList<TypeProduction> typeProductionList;
    private int index;


    public ComboBoxTypeProduction(ArrayList<TypeProduction> typeProductionList) {
        this.typeProductionList = typeProductionList;
        setSelectedItem(0);
    }


    @Override
    public Object getSelectedItem() {

        return selectedItem.getName();
    }

    @Override
    public void setSelectedItem(Object anItem) {
        selectedItem = typeProductionList.get(index);

    }

    @Override
    public void addListDataListener(ListDataListener l) {
    }

    @Override
    public Object getElementAt(int index) {
        this.index = index;
        return typeProductionList.get(index).getName();
    }

    @Override
    public int getSize() {
        return typeProductionList.size();
    }

    @Override
    public void removeListDataListener(ListDataListener l) {
    }

    public String toString() {

        return "9999";
    }

    public int getId() {
        return selectedItem.getId();
    }


}
