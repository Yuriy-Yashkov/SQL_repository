/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dept.calculationprice;

/**
 * Ассортимент
 * @author user
 */
public class Assortment {

    private int id;
    private int idGroup;
    private int idType;
    private String ptk;
    private String name;

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
     * @return the idType
     */
    public int getIdType() {
        return idType;
    }

    /**
     * @param idType the idType to set
     */
    public void setIdType(int idType) {
        this.idType = idType;
    }

    /**
     * @return the ptk
     */
    public String getPtk() {
        return ptk;
    }

    /**
     * @param ptk the ptk to set
     */
    public void setPtk(String ptk) {
        this.ptk = ptk;
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
