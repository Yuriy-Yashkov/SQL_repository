package by.march8.ecs.framework.common.comparators;

import by.gomel.freedev.ucframework.uccore.viewport.gridviewport.model.ColumnProperty;

import java.util.Comparator;

/**
 * Created by Andy on 09.10.2014.
 * Сортировка столбцов грида по параметру аннотации {@see by.march8.api.TableHeader}
 */
public class SortBySequence implements Comparator<ColumnProperty> {
    @Override
    public int compare(final ColumnProperty o1, final ColumnProperty o2) {
        if (o1.getSequence() < o2.getSequence()) {
            return -1;
        } else if (o1.getSequence() > o2.getSequence()) {
            return 1;
        }
        return 0;
    }
}
