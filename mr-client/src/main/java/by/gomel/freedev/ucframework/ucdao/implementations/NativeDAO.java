package by.gomel.freedev.ucframework.ucdao.implementations;

import by.gomel.freedev.ucframework.ucdao.interfaces.INativeDao;
import by.gomel.freedev.ucframework.ucdao.interfaces.ISqlProcessing;
import org.hibernate.Session;

import javax.persistence.EntityManager;

/**
 * @author Andy 13.04.2015.
 */
public class NativeDAO extends BaseDao implements INativeDao {

    @Override
    public void processing(final ISqlProcessing sqlProcessing, Class<?> c) {
        EntityManager manager = getEntityManagerByEntity(c);
        Session session = manager.unwrap(Session.class);
        session.getTransaction().begin();
        session.doWork(sqlProcessing::processing);
        session.getTransaction().commit();
        session.close();
    }
}
