package by.march8.ecs.application.modules.references.general;

import by.gomel.freedev.ucframework.ucdao.DaoFactory;
import by.gomel.freedev.ucframework.ucdao.interfaces.IGenericDao;
import by.gomel.freedev.ucframework.ucdao.model.QueryProperty;
import by.march8.api.utils.DateUtils;
import by.march8.entities.readonly.CurrencyEntityMarch8;
import by.march8.entities.unknowns.CurrencyEntity;

import javax.swing.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CurrencyDAO {

    public static List<CurrencyEntityMarch8> getCurrencyTypes() {
        DaoFactory<CurrencyEntityMarch8> factory = DaoFactory.getInstance();
        IGenericDao<CurrencyEntityMarch8> dao = factory.getGenericDao();
        try {
            return dao.getEntityListByNamedQuery(CurrencyEntityMarch8.class, "CurrencyEntityMarch8.findAll", null);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void loadCurrencyTypesToComboBox(JComboBox<CurrencyEntityMarch8> comboBox) {
        List<CurrencyEntityMarch8> list = getCurrencyTypes();
        if (list != null) {
            comboBox.setModel(new DefaultComboBoxModel(list.toArray()));
        } else {
            comboBox.setModel(new DefaultComboBoxModel<>());
            comboBox.setSelectedIndex(-1);
        }
    }

    @SuppressWarnings("all")
    public static float getCurrencyRateForCurrency(CurrencyEntityMarch8 currency, Date date) {
        if (currency.getId() <= 1) {
            return 1.f;
        }

        String currencyTemp;
        int currencyId = 0;

        switch (currency.getId()) {

            case 2: {
                currencyTemp = "RUB";
                currencyId = 17;
                break;
            }
            case 3: {
                currencyTemp = "USD";
                currencyId = 15;
                break;
            }
            case 4: {
                currencyTemp = "EUR";
                currencyId = 19;
                break;
            }
            case 5: {
                currencyTemp = "UAH";
                currencyId = 18;
                break;
            }

            default:
                break;
        }

        DaoFactory<CurrencyEntity> factory = DaoFactory.getInstance();
        IGenericDao<CurrencyEntity> dao = factory.getGenericDao();
        List<QueryProperty> criteria = new ArrayList<>();
        criteria.add(new QueryProperty("currency", currencyId));
        criteria.add(new QueryProperty("date", DateUtils.getDateOnlyByDate(date)));
        try {
            List<CurrencyEntity> list = dao.getEntityListByNamedQuery(CurrencyEntity.class, "CurrencyEntity.findByCurrencyType", criteria);
            if (list.size() > 0) {
                return list.get(0).getRate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
