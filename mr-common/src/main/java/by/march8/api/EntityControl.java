package by.march8.api;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Аннтоация предоставляет доступ к инструментам управления Entity
 * <p>Позволяет промаркировать объекты необходимые для работы справочника </p>
 * <p>Created by Andy on 14.04.2015.</p>
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface EntityControl {
    /**
     * Имя класса-контроллера наследника ControlPane.
     * @return имя класса
     */
    Class<?> control() default Class.class;

    /**
     * Имя класса-редактора для сущности
     * @return имя класса
     */
    Class<?> editorClass() default Class.class ;

    /**
     * Имя сущности для отображения в справочнике.
     * Необходим в случаях, когда за отображение в таблице и за объект отображения отвечают разные сущности
     * @return имя сущности
     */
    Class<?> viewEntity() default Class.class;
}
