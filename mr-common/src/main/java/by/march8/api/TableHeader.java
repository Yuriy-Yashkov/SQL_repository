package by.march8.api;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Аннотация позволяет объявить поле или Getter-метод столбцом в гриде
 * <code><p>
 * &#064;TableHeader(name="Название столбца", width = 50, sequence = 0, weight=1)
 * </p></code>
 * <pre>
 * name -       имя столбца в гриде. По-умолчанию [""]
 * width -      ширина столбца, если отрицательное - устанавливает максимальную ширину,
 *              если положительное - минимальную, значение 0 скрывает колонку. По-умолчанию [50]
 * sequence -   порядковый номер колонки, последовательность(0,1,2....Х) не обязательна
 * weight -     Вес/значимость колонки. Если установлена в 0 то в режиме выбора из списка
 *              (справочника) данная колонка не отображается. По-умолчанию [1]
 *              Используется, если для разных функц.режимов нужно показать/скрыть некоторые колонки
 * </pre>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.FIELD})
public @interface TableHeader {
    /**
     * Название колонки
     */
    String name() default "";

    /**
     * Ширина колонки
     */
    int width() default 50;

    /**
     * Очередность колонки
     */
    int sequence();

    /**
     * Вес/значимость колонки при отображении в таблице
     * @return вес/значимость колонки в таблице
     */
    int weight() default 1;

    boolean footer() default false ;
}
