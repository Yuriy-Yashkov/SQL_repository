package by.march8.api;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Аннотация перенаправляет бин на произвольный {@link javax.persistence.EntityManager}.
 * Направление определяется в конечном методе слоя DAO.
 * <p/>Пример - {@link by.gomel.freedev.ucframework.ucdao.implementations.CommonDao#getEntityManagerByEntity(Class)}
 *
 * @see javax.persistence.EntityManager
 * @see by.gomel.freedev.ucframework.ucdao.implementations.CommonDao#getEntityManagerByEntity(Class)
 * @see MarchDataSourceEnum
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface RedirectToEntityManager {
    /**
     * Тип источника данных
     * @return тип
     */
    MarchDataSourceEnum redirectTo() default MarchDataSourceEnum.DS_SQLSERVER;
}
