package by.march8.ecs.application.modules.warehouse.external.shipping.dao;

import by.march8.ecs.application.modules.warehouse.external.shipping.dao.implementations.SaleDocumentDao;
import by.march8.ecs.application.modules.warehouse.external.shipping.dao.interfaces.ISaleDocumentDao;

/**
 * DAO фабрика предназначена для работы с данными относящимися к процессам реализации, отгрузки и возврата товара
 * в пределах склада готовой продукции
 *
 */
public class DAOSaleDocumentFactory {

    public static DAOSaleDocumentFactory instance = new DAOSaleDocumentFactory();

    private ISaleDocumentDao saleDocumentDao;


    /**
     * Возвращает DAO фабрику
     */
    public static DAOSaleDocumentFactory getInstance() {
        return DAOSaleDocumentFactory.instance;
    }

    public ISaleDocumentDao getSaleDocumentDao() {
        if (saleDocumentDao == null) {
            saleDocumentDao = new SaleDocumentDao();
        }
        return saleDocumentDao;
    }
}
