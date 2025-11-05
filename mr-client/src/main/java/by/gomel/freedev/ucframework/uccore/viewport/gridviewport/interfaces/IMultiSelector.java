package by.gomel.freedev.ucframework.uccore.viewport.gridviewport.interfaces;

import java.util.ArrayList;
import java.util.Set;

/**
 * @author Andy 24.12.2014.
 */
public interface IMultiSelector<T> {
    void preset(ArrayList<T> presetCollection);

    Set<T> getSelectedItems();
}
