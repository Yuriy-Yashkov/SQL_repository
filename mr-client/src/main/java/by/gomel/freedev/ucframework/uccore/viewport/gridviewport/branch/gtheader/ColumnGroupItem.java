package by.gomel.freedev.ucframework.uccore.viewport.gridviewport.branch.gtheader;

/**
 * @author Andy 29.10.2014.
 */
public class ColumnGroupItem {
    private ColumnGroup item;
    private String name;

    public ColumnGroupItem(final ColumnGroup item, final String name) {
        this.item = item;
        this.name = name;
    }

    public ColumnGroup getItem() {
        return item;
    }

    public void setItem(final ColumnGroup item) {
        this.item = item;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }
}
