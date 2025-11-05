package by.march8.ecs.framework.common.comparators;

import java.lang.reflect.Method;
import java.util.Comparator;

/**
 * Created by suvarov on 24.11.14.
 * Компаратор для сортировки данных таблицы
 */
@SuppressWarnings("all")
public class ComporatorForTables implements Comparator<Object> {
    public int compare(Object o1, Object o2) {
        // System.out.println("o1= "+o1+"o2= "+o2);
        if (o1 != null && o2 != null) {
            Class c = o1.getClass();
            String orderBy1 = null;
            String orderBy2 = null;
            try {
                Method method = c.getDeclaredMethod("getOrderByField");
                orderBy1 = (String) method.invoke(o1);
                orderBy2 = (String) method.invoke(o2);
                if (orderBy1 != null && orderBy2 != null) {
                    return orderBy1.compareTo(orderBy2);
                }
                if (orderBy1 == null)
                    return -1;
                else {
                    if (orderBy1 == null && orderBy2 == null) return 0;
                    else return 1;
                }
            } catch (Exception e) {
                //e.printStackTrace();
                return 0;
            }


        }
        return 0;
    }
}