package dept.production.zsh.zplata;

/**
 *
 * @author lidashka
 */
public class Item {
    private int id;
    private String description;
    private String formula;

    public Item(int id, String description, String formula) {
        this.id = id;
        this.description = description;
        this.formula = formula;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public String getFormula() {
        return formula;
    }

    public String toString() {
        return getDescription();
    }
}
