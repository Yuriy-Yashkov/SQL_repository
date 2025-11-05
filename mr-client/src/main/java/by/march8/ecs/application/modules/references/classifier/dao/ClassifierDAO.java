package by.march8.ecs.application.modules.references.classifier.dao;

import by.gomel.freedev.ucframework.ucdao.DaoFactory;
import by.gomel.freedev.ucframework.ucdao.interfaces.IGenericDao;
import by.gomel.freedev.ucframework.ucdao.model.QueryProperty;
import by.march8.entities.classifier.ClassifierArticleComposition;
import by.march8.entities.classifier.ClassifierModelParams;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * @author Andy 21.01.2019 - 9:47.
 */
public class ClassifierDAO {
    public static ClassifierModelParams getClassifierItem(int id) {
        DaoFactory<ClassifierModelParams> factory = DaoFactory.getInstance();
        IGenericDao<ClassifierModelParams> dao = factory.getGenericDao();
        ClassifierModelParams result = null;
        try {
            result = dao.getEntityById(ClassifierModelParams.class, id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static ClassifierModelParams getClassifierItemByArticleCode(int article) {
        DaoFactory<ClassifierModelParams> factory = DaoFactory.getInstance();
        IGenericDao<ClassifierModelParams> dao = factory.getGenericDao();
        List<QueryProperty> criteria = new ArrayList<>();
        criteria.add(new QueryProperty("article", article));
        try {
            List<ClassifierModelParams> list_;
            list_ = dao.getEntityListByNamedQuery(ClassifierModelParams.class, "ClassifierModelParams.findByArticleCode", criteria);

            if (list_ != null) {
                if (!list_.isEmpty()) {
                    return list_.get(0);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<ClassifierArticleComposition> getClassifierArticleCompositionItem(int modelNumber) {
        DaoFactory<ClassifierArticleComposition> factory = DaoFactory.getInstance();
        IGenericDao<ClassifierArticleComposition> dao = factory.getGenericDao();
        List<QueryProperty> criteria = new ArrayList<>();
        criteria.add(new QueryProperty("modelNumber", modelNumber));
        try {
            return dao.getEntityListByNamedQuery(ClassifierArticleComposition.class, "ClassifierArticleComposition.findByModelNumber", criteria);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static HashMap<Integer, ClassifierArticleComposition> getClassifierArticleCompositionAsMap(Set<Integer> set_) {
        DaoFactory<ClassifierArticleComposition> factory = DaoFactory.getInstance();
        IGenericDao<ClassifierArticleComposition> dao = factory.getGenericDao();
        HashMap<Integer, ClassifierArticleComposition> result = new HashMap<>();
        List<QueryProperty> criteria = new ArrayList<>();
        criteria.add(new QueryProperty("models", set_));
        try {
            List<ClassifierArticleComposition> list = dao.getEntityListByNamedQuery(ClassifierArticleComposition.class, "ClassifierArticleComposition.findFromList", criteria);
            for (ClassifierArticleComposition composition : list) {
                result.put(composition.getModelNumber(), composition);
            }
            return result;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}
