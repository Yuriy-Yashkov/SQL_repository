package by.gomel.freedev.ucframework.ucdao;

import by.march8.ecs.MainController;
import by.march8.ecs.framework.common.BackgroundTask;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 * Класс возвращает EntityManager для работы с PostgreSQL СУБД
 */
public class PostgresDataSource {

    private static final String PERSISTENT_UNIT_NAME = "march8arm-postgresql";
    private static EntityManagerFactory emf;

    static {
        class Task extends BackgroundTask {

            public Task(final String messageText) {
                super(messageText, true);
            }

            @Override
            protected Object doInBackground() throws Exception {
                try {
                    return CustomPersistence
                            .createEntityManagerFactory(PERSISTENT_UNIT_NAME);
                } catch (final Throwable ex) {
                    MainController.exception(ex, "Нет доступа к серверу " + PERSISTENT_UNIT_NAME);
                    throw new ExceptionInInitializerError(ex);
                }

            }
        }


        Task task = new Task("Инициализация PostgreSQL драйвера...");
        try {
            task.executeTask();
            emf = (EntityManagerFactory) task.get();
        } catch (Exception e) {
            MainController.exception(e, "Ошибка инициализации SQL драйвера ");
        }
    }

    public static EntityManager getEntityManager() {
        return emf.createEntityManager();
    }
}
