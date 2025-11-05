/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dept.calculationprice;

import javax.swing.*;
import javax.swing.event.ListDataListener;
import java.util.ArrayList;

/**
 * Комбобокс для выбора ассортимента
 *
 * @author user
 */
public final class ComboBoxAssortment implements ComboBoxModel {
    private Assortment selectedItem;
    private ArrayList<Assortment> assortmentList;
    private int index;


    public ComboBoxAssortment(ArrayList<Assortment> assortment) {
        this.assortmentList = assortment;
        setSelectedItem(0);
    }

    @Override
    public Object getSelectedItem() {
        return selectedItem.getName();
    }

    @Override
    public void setSelectedItem(Object anItem) {
        selectedItem = assortmentList.get(index);

    }

    @Override
    public void addListDataListener(ListDataListener l) {
    }

    @Override
    public Object getElementAt(int index) {
        this.index = index;
        return assortmentList.get(index).getName();
    }

    @Override
    public int getSize() {
        return assortmentList.size();
    }

    @Override
    public void removeListDataListener(ListDataListener l) {
    }

    public String getPtk() {
        return selectedItem.getPtk();
    }
}
