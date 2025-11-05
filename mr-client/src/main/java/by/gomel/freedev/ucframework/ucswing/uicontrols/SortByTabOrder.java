package by.gomel.freedev.ucframework.ucswing.uicontrols;

import by.gomel.freedev.ucframework.ucswing.uicontrols.interfaces.IUCComponent;

import java.util.Comparator;

/**
 * @author Andy 23.03.2015.
 */
public class SortByTabOrder implements Comparator<IUCComponent> {
    @Override
    public int compare(final IUCComponent o1, final IUCComponent o2) {
        if (o1.getTabOrder() < o2.getTabOrder()) {
            return -1;
        } else if (o1.getTabOrder() > o2.getTabOrder()) {
            return 1;
        }
        return 0;
    }
}
