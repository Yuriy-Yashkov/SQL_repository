package by.gomel.freedev.ucframework.uccore.viewport.gridviewport.model;

/**
 * @author Andy 09.03.2016.
 */
public class TableCell {
    public int row;
    public int column;
    public boolean isLastCell = false;

    public TableCell(final int row, final int column, final boolean isLastCell) {
        this.row = row;
        this.column = column;
        this.isLastCell = isLastCell;
    }
}
