package by.gomel.freedev.ucframework.ucdao.implementations;

import by.gomel.freedev.ucframework.ucdao.PostgresDataSource;
import by.gomel.freedev.ucframework.ucdao.SqlServerDataSources;
import by.gomel.freedev.ucframework.ucdao.SqlServerMarch8DS;
import by.march8.api.MarchDataSourceEnum;
import by.march8.api.RedirectToEntityManager;

import javax.persistence.EntityManager;

/**
 * @author Andy 29.03.2016.
 */
public class BaseDao {
    /**
     * Возвращает по аннотации RedirectToEntityManager() соответствующий ей EntityManager
     *
     * @see RedirectToEntityManager
     */
    protected EntityManager getEntityManagerByEntity(final Class<?> entity) {
        if (entity.isAnnotationPresent(RedirectToEntityManager.class)) {
            final RedirectToEntityManager redirect = entity
                    .getAnnotation(RedirectToEntityManager.class);

            if (redirect.redirectTo() == MarchDataSourceEnum.DS_SQLSERVER) {
                return SqlServerDataSources.getEntityManager();
            } else if (redirect.redirectTo() == MarchDataSourceEnum.DS_SQLMARCH8) {
                return SqlServerMarch8DS.getEntityManager();
            } else {
                return PostgresDataSource.getEntityManager();
            }
        } else {
            return SqlServerDataSources.getEntityManager();
        }
    }
}
