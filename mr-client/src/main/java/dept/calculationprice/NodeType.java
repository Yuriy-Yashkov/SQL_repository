/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dept.calculationprice;

import javax.swing.tree.DefaultMutableTreeNode;

/**
 *
 * @author user
 */
@SuppressWarnings("all")
public final class NodeType extends DefaultMutableTreeNode {
    private static final long serialVersionUID = 1L;

    private int id;
    private int idGroup;
    private String name;
    private int ageType;

    public NodeType(Type type) {
        setId(type.getId());
        setIdGroup(type.getIdGroup());
        setName(type.getName());
        setAgeType(type.getAgeType());

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
     * @return the idGroup
     */
    public int getIdGroup() {
        return idGroup;
    }

    /**
     * @param idGroup the idGroup to set
     */
    public void setIdGroup(int idGroup) {
        this.idGroup = idGroup;
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
     * @return the ageType
     */
    public int getAgeType() {
        return ageType;
    }

    /**
     * @param ageType the ageType to set
     */
    public void setAgeType(int ageType) {
        this.ageType = ageType;
    }

}
