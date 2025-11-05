package by.gomel.freedev.ucframework.ucswing.uicontrols.filelist.model;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

/**
 * @author Andy 19.12.2018 - 13:37.
 */
public class ListItemTransferable implements Transferable {

    public static final DataFlavor DND_ITEM = new DataFlavor(String.class, "java/ListItem");
    private String listItem;

    public ListItemTransferable(String listItem) {
        this.listItem = listItem;
    }

    @Override
    public DataFlavor[] getTransferDataFlavors() {
        return new DataFlavor[]{DND_ITEM};
    }

    @Override
    public boolean isDataFlavorSupported(DataFlavor flavor) {
        return flavor.equals(DND_ITEM);
    }

    @Override
    public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
        return listItem;
    }
}