package by.march8.ecs.services.eancode;

import by.gomel.freedev.ucframework.ucdao.DaoFactory;
import by.gomel.freedev.ucframework.ucdao.interfaces.IGenericDaoGUI;
import by.gomel.freedev.ucframework.ucdao.model.QueryProperty;
import by.march8.ecs.services.IService;
import by.march8.ecs.services.eancode.dao.UniqueIdentifierJDBC;
import by.march8.entities.storage.UniqueIdentifiers;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Andy 03.11.2018 - 11:10.
 */
public class UniqueIdentifierService implements IService {

    private static UniqueIdentifierService instance = null;

    private UniqueIdentifierService() {
    }

    public static UniqueIdentifierService getInstance() {
        if (instance == null) {
            instance = new UniqueIdentifierService();
        }


        return instance;
    }

    private long getNextUniqueIdentifier(String ident) {
        DaoFactory<UniqueIdentifiers> factory = DaoFactory.getInstance();
        IGenericDaoGUI<UniqueIdentifiers> dao = factory.getGenericDao();
        java.util.List<QueryProperty> criteria = new ArrayList<>();
        criteria.add(new QueryProperty("key", ident));
        List<UniqueIdentifiers> data = new ArrayList<>();
        try {
            data.addAll(dao.getEntityListByNamedQueryGUI(UniqueIdentifiers.class, "UniqueIdentifiers.findByKey", criteria));
        } catch (final Exception e) {
            e.printStackTrace();
        }

        if (data.size() > 0) {
            return data.get(0).getCurrentNumber();
        }

        return 0;
    }

    public long getNextUniqueIdentifierAndSave(String ident) {
        UniqueIdentifierJDBC db = new UniqueIdentifierJDBC();
        return db.getNextUniqueIdentifier(ident);
    }
}
