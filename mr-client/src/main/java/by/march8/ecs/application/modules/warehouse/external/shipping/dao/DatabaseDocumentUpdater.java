package by.march8.ecs.application.modules.warehouse.external.shipping.dao;

import by.gomel.freedev.ucframework.ucdao.DaoFactory;
import by.gomel.freedev.ucframework.ucdao.interfaces.ICommonDaoThread;
import by.march8.entities.warehouse.SaleDocument;

/**
 * @author Andy 21.10.2016.
 */
public class DatabaseDocumentUpdater implements DocumentUpdater {
    @Override
    public boolean updateDocument(final SaleDocument document) throws Exception {
        // Сохраним изменения в базу после расчета документа
        DaoFactory factory = DaoFactory.getInstance();
        ICommonDaoThread dao = factory.getCommonDaoThread();
        // Обновляем документ в базе
        dao.updateEntityThread(document);
        return true;
    }
}
