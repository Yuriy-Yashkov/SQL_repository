/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dept.calculationprice;

import javax.swing.*;
import javax.swing.event.ListDataListener;
import java.util.ArrayList;

/**
 * @author user
 */
public final class ListModelPrice implements ListModel {

    private ArrayList<Price> list;
    private Price price;
    private int id;
    private String name;

    public ListModelPrice(ArrayList<Price> list) {
        setList(list);
    }

    public int getIdPrice() {
        return getId();
    }


    /**
     * @return the list
     */
    public ArrayList<Price> getList() {
        return list;
    }

    /**
     * @param list the list to set
     */
    public void setList(ArrayList<Price> list) {
        this.list = list;
    }

    /**
     * @return the price
     */
    public Price getPrice() {
        return price;
    }

    /**
     * @param price the price to set
     */
    public void setPrice(Price price) {
        this.price = price;
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    public int getSize() {
        return getList().size();
    }

    public void addListDataListener(ListDataListener ll) {

    }

    public void removeListDataListener(ListDataListener ll) {

    }


    public Object getElementAt(int i) {
        setId(getList().get(i).getId());
        setName(getList().get(i).getName());
        return getList().get(i).getName();
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }


}
