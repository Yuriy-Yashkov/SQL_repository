/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dept.calculationprice;

import javax.swing.tree.DefaultMutableTreeNode;

/**
 * @author user
 */
@SuppressWarnings("all")
public final class NodeGroup extends DefaultMutableTreeNode {

    private int id;
    private int idTypeProd;
    private String name;
    private int typeCalculation;

    public NodeGroup(Group group) {

        setId(group.getId());
        setIdTypeProd(group.getIdTypeProd());
        setName(group.getName());
        setTypeCalculation(group.getTypeCalculation());

        setUserObject(getName());
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

    /**
     * @return the idTypeProd
     */
    public int getIdTypeProd() {
        return idTypeProd;
    }

    /**
     * @param idTypeProd the idTypeProd to set
     */
    public void setIdTypeProd(int idTypeProd) {
        this.idTypeProd = idTypeProd;
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

    /**
     * @return the typeCalculation
     */
    public int getTypeCalculation() {
        return typeCalculation;
    }

    /**
     * @param typeCalculation the typeCalculation to set
     */
    public void setTypeCalculation(int typeCalculation) {
        this.typeCalculation = typeCalculation;
    }


}
