package by.gomel.freedev.ucframework.ucdao.implementations;

import by.gomel.freedev.ucframework.ucdao.SqlServerDataSources;
import by.gomel.freedev.ucframework.ucdao.interfaces.IAdministrationDao;
import by.march8.ecs.MainController;
import by.march8.ecs.framework.common.BackgroundTask;
import by.march8.entities.admin.UserInformation;
import by.march8.entities.admin.UserRole;
import org.hibernate.Session;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Andy 10.09.2014.
 */
public class AdministrationDao extends BaseDao implements IAdministrationDao {

    @Override
    public UserInformation getUserInformationThread(final String userLogin) {
        class Task extends BackgroundTask {
            UserInformation result = null;

            public Task() {
                super("Авторизация пользователя...");
            }

            @Override
            protected UserInformation doInBackground() throws Exception {
                result = getUserInformation(userLogin);
                return result;
            }

            public UserInformation getResultInformation() {
                return result;
            }
        }

        Task task = new Task();
        task.executeTask();
        try {
            return task.getResultInformation();
        } catch (Exception e) {
            MainController.exception(e, "Ошибка получения данных для пользователя " + userLogin);
        }
        return null;
    }

    private UserInformation getUserInformation(final String userName) {
        UserInformation result = null;
        final EntityManager manager = SqlServerDataSources.getEntityManager();
        final Session session = manager.unwrap(Session.class);
        try {
            session.beginTransaction();
            final Query query = manager
                    .createQuery("SELECT ui from UserInformation ui WHERE ui.userLogin = :userLogin");
            query.setParameter("userLogin", userName);
            final List res = query.getResultList();
            if (!res.isEmpty()) {
                if (res.size() == 1) {
                    result = (UserInformation) res.get(0);
                }
            }
        } catch (final Exception e) {
            MainController.exception(e, "Ошибка получения данных для пользователя " + userName);
        } finally {
            manager.close();
        }
        return result;
    }

    @Override
    public ArrayList<UserRole> getUserRolesByEmployeeId(
            final int employeeId) {
        ArrayList<UserRole> result = null;
        final EntityManager manager = SqlServerDataSources.getEntityManager();
        final Session session = manager.unwrap(Session.class);
        try {
            session.beginTransaction();
            final Query query = manager
                    .createQuery("SELECT ur FROM UserRole ur  WHERE ur.employees = :employeeId");
            query.setParameter("employeeId", employeeId);
            // noinspection unchecked
            result = (ArrayList<UserRole>) query.getResultList();
        } catch (final Exception e) {
            MainController.exception(e, "Ошибка получения ролей сотрудника id=" + employeeId);
        } finally {
            manager.close();
        }
        return result;
    }

    @Override
    public UserInformation hasHaveAccount(final int employeeId) {
        UserInformation result = null;
        final EntityManager manager = SqlServerDataSources.getEntityManager();
        final Session session = manager.unwrap(Session.class);
        try {
            session.beginTransaction();
            final Query query = manager
                    .createQuery("SELECT ui from UserInformation ui WHERE ui.employee.id = :employeeId");
            query.setParameter("employeeId", employeeId);
            final List res = query.getResultList();
            if (!res.isEmpty()) {
                if (res.size() == 1) {
                    result = (UserInformation) res.get(0);
                }
            }
        } catch (final Exception e) {
            MainController.exception(e, "Ошибка проверки наличия аккаунта у сотрудника id=" + employeeId);
        } finally {
            manager.close();
        }
        return result;
    }

}
