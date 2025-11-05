package by.march8.tasks.factoring.logic;

import by.gomel.freedev.ucframework.ucdao.DaoFactory;
import by.gomel.freedev.ucframework.ucdao.interfaces.IGenericDao;
import by.gomel.freedev.ucframework.ucdao.model.QueryProperty;
import by.march8.ecs.framework.common.Settings;
import by.march8.entities.warehouse.SaleDocumentEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Andy 27.02.2018.
 */
public class FactoringFactory {

    private static FactoringFactory instance = null;

    private FactoringFactory() {

    }

    public static FactoringFactory getInstance() {
        if (instance == null) {
            instance = new FactoringFactory();
        }
        return instance;
    }


    public void createIssuanceByDocumentNumber(String documentNumber) {
        SaleDocumentEntity entity = getDocumentEntityByDocumentNumber(documentNumber);
        if (entity != null) {
            FactoringCreator issuanceCreator = new FactoringCreator(Settings.FACTORING_DIR);
            try {
                issuanceCreator.createIssuance(entity);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Ошибка получения данных для документа [" + documentNumber.trim() + "]");
        }
    }

    private SaleDocumentEntity getDocumentEntityByDocumentNumber(String documentNumber) {
        List<QueryProperty> criteria = new ArrayList<QueryProperty>();
        criteria.add(new QueryProperty("number", documentNumber));

        DaoFactory<SaleDocumentEntity> factoryMarch = DaoFactory.getInstance();
        IGenericDao<SaleDocumentEntity> daoMarch = factoryMarch.getGenericDao();
        try {
            return daoMarch.getEntityByNamedQuery(SaleDocumentEntity.class, "SaleDocumentEntity.findByNumber", criteria);

        } catch (final Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
