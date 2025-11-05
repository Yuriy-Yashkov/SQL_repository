package by.march8.ecs.application.modules.references.general;

import by.gomel.freedev.ucframework.ucdao.DaoFactory;
import by.gomel.freedev.ucframework.ucdao.interfaces.IGenericDao;
import by.gomel.freedev.ucframework.ucdao.model.QueryProperty;
import by.march8.entities.readonly.ContractEntity;
import by.march8.entities.readonly.ContractorEntity;
import by.march8.entities.readonly.ContractorEntityView;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ContractorDAO {

    public static List<ContractorEntityView> getAllContractors() {
        DaoFactory<ContractorEntityView> factory = DaoFactory.getInstance();
        IGenericDao<ContractorEntityView> dao = factory.getGenericDao();
        try {
            return dao.getEntityListByNamedQuery(ContractorEntityView.class, "ContractorEntityView.findAll", null);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static ContractorEntity getContractorEntityById(int id) {
        DaoFactory<ContractorEntity> factory = DaoFactory.getInstance();
        IGenericDao<ContractorEntity> dao = factory.getGenericDao();
        try {
            return dao.getEntityById(ContractorEntity.class, id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static ContractorEntityView getContractorViewById(int id) {
        DaoFactory<ContractorEntityView> factory = DaoFactory.getInstance();
        IGenericDao<ContractorEntityView> dao = factory.getGenericDao();
        try {
            return dao.getEntityById(ContractorEntityView.class, id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<ContractEntity> getContractsForContractorByContractorId(int contractorId) {
        DaoFactory<ContractEntity> factory = DaoFactory.getInstance();
        IGenericDao<ContractEntity> dao = factory.getGenericDao();
        List<QueryProperty> criteria = new ArrayList<>();
        criteria.add(new QueryProperty("contractor", contractorId));
        try {
            return dao.getEntityListByNamedQuery(ContractEntity.class, "ContractEntity.findByContractorId", criteria);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
