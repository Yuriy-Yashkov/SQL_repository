/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dept.calculationprice;

import javax.swing.*;
import javax.swing.event.ListDataListener;
import java.util.ArrayList;

/**
 * Комбобокс для выбора прейскуранта
 *
 * @author user
 */
public final class ComboboxPrice implements ComboBoxModel {

    private Price selectedItem;
    private ArrayList<Price> priceList;
    private int index;

    public ComboboxPrice(ArrayList<Price> priceList) {
        setPriceList(priceList);
        setSelectedItem(0);
    }

    @Override
    public Object getSelectedItem() {
        return selectedItem.getName();
    }

    @Override
    public void setSelectedItem(Object anItem) {
        selectedItem = getPriceList().get(index);

    }

    @Override
    public void addListDataListener(ListDataListener l) {
    }

    @Override
    public Object getElementAt(int index) {
        this.index = index;
        return getPriceList().get(index).getName();
    }

    @Override
    public int getSize() {
        return getPriceList().size();
    }

    @Override
    public void removeListDataListener(ListDataListener l) {
    }

    public int getId() {
        return selectedItem.getId();
    }

    /**
     * @return the priceList
     */
    public ArrayList<Price> getPriceList() {
        return priceList;
    }

    /**
     * @param priceList the priceList to set
     */
    public void setPriceList(ArrayList<Price> priceList) {
        this.priceList = priceList;
    }

}
