/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dept.sklad.ostatki;

import by.march8.ecs.framework.common.LogCrutch;

import javax.swing.*;
import java.util.ArrayList;

/**
 *
 * @author user
 */
public class CreateQuery {

    //  private static final Logger log = new Log().getLoger(CreateQuery.class);
    private static final LogCrutch log = new LogCrutch();
    private String query = "";
    private String buffer = "";
    private String nameView = "";
    private ArrayList<String> condition;

    public CreateQuery(String nameView, ArrayList<String> condition) {
        this.nameView = nameView;
        this.condition = condition;
    }

    public String creteQ() {
        try {
            this.query = " select * from " + this.nameView;
            for (int i = 0; i < this.condition.size(); i++) {
                if (this.buffer.equals("") && !this.condition.get(i).equals(null)) {
                    this.buffer += " where " + this.condition.get(i);
                } else {
                    this.buffer += " or " + this.condition.get(i);
                }
            }
        } catch (Exception ex) {
            System.err.println("Error in reportNowRemains in class CreateQuery. \nPlease contact developer" + ex);
            log.error("Error in reportNowRemains in class CreateQuery.", ex);
            JOptionPane.showMessageDialog(null, "Error in method reportNowRemains in class CreateQuery. \nPlease contact developer");
        }
        return query + buffer;
    }
}
