package by.gomel.freedev.ucframework.ucdao;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 * @author Andy 16.12.2014.
 */
public class SqlServerMarch8DS {
    private static final String PERSISTENT_UNIT_NAME = "march8arm-sqlserver-march8";
    private static EntityManagerFactory emf;

    /*    static {
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


            Task task = new Task("Инициализация Microsoft SQL драйвера...");
            try {
                task.executeTask();
                emf = (EntityManagerFactory)task.get() ;
            } catch (Exception e) {
                MainController.exception(e, "Ошибка инициализации SQL драйвера ");
            }
        }*/
    static {
        emf = CustomPersistence
                .createEntityManagerFactory(PERSISTENT_UNIT_NAME);
    }

    public static EntityManager getEntityManager() {
        return emf.createEntityManager();
    }
}
