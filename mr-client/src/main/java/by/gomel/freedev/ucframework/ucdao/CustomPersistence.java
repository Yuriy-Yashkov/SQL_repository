package by.gomel.freedev.ucframework.ucdao;

import by.march8.ecs.MainController;
import org.hibernate.ejb.HibernatePersistence;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceException;
import javax.persistence.spi.PersistenceProvider;
import javax.persistence.spi.PersistenceProviderResolverHolder;
import java.util.List;
import java.util.Map;

/**Класс для явного указания JPA вызывать провайдера EntityManager-а для Hibernate
 * Тайная магия...*/
@SuppressWarnings({"deprecation", "rawtypes"})
public class CustomPersistence extends Persistence {

    public static EntityManagerFactory createEntityManagerFactory(
            final String persistenceUnitName) {
        return CustomPersistence.createEntityManagerFactory(
                persistenceUnitName, null);
    }

    public static EntityManagerFactory createEntityManagerFactory(
            final String persistenceUnitName, final Map properties) {
        EntityManagerFactory emf = null;
        try {
            final List<PersistenceProvider> providers = getProviders();
            PersistenceProvider defaultProvider = null;
            for (final PersistenceProvider provider : providers) {
                if (provider instanceof HibernatePersistence) {
                    defaultProvider = provider;
                    continue;
                }
                emf = provider.createEntityManagerFactory(persistenceUnitName,
                        properties);
                if (emf != null) {
                    break;
                }
            }
            if ((emf == null) && (defaultProvider != null)) {
                emf = defaultProvider.createEntityManagerFactory(
                        persistenceUnitName, properties);
            }
            if (emf == null) {
                throw new PersistenceException(
                        "No Persistence provider for EntityManager named "
                                + persistenceUnitName);
            }
        } catch (Exception e) {
            MainController.exception(e, "Нет доступа к серверу " + persistenceUnitName);
        }
        return emf;
    }

    protected static List<PersistenceProvider> getProviders() {
        return PersistenceProviderResolverHolder
                .getPersistenceProviderResolver().getPersistenceProviders();
    }

}
