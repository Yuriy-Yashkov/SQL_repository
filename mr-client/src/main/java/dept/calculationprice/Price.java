/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dept.calculationprice;

/**
 * @author user
 */
public class Price {
    private int id;
    private int id_type_calculation;
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
     * @return the id_type_calculation
     */
    @SuppressWarnings("unused")
    public int getId_type_calculation() {
        return id_type_calculation;
    }

    /**
     * @param id_type_calculation the id_type_calculation to set
     */
    public void setId_type_calculation(int id_type_calculation) {
        this.id_type_calculation = id_type_calculation;
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
