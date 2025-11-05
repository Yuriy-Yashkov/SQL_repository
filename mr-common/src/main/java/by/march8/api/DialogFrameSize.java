package by.march8.api;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Аннотирует доменный класс справочника и определяет видимые размеры формы по-умолчанию
 */
@Target(TYPE)
@Retention(RUNTIME)
public @interface DialogFrameSize {
    int width() default 800;
    int height() default 600;
}
