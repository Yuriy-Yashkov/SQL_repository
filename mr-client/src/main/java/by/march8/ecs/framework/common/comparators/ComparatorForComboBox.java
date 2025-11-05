package by.march8.ecs.framework.common.comparators;

import java.util.Comparator;

/**
 * @author suvarov on 24.11.14.
 * Строковый компаратор для айтемов комбобокса
 */

public class ComparatorForComboBox implements Comparator<Object> {
    public int compare(Object o1, Object o2) {
        //System.out.println("o1= "+o1+"o2= "+o2);
        if ((o1 != null) && (o2 != null)) {
            String str1 = o1.toString();
            String str2 = o2.toString();
            //Дополнительная проверка на тот случай,
            //если toString() всё же вернул null
            if (str1 != null && str2 != null) {
                return str1.compareTo(str2);
            }
        }
        return 0;
    }
}
