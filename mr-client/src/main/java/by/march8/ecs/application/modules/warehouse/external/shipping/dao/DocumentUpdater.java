package by.march8.ecs.application.modules.warehouse.external.shipping.dao;

import by.march8.entities.warehouse.SaleDocument;

/**
 * @author Andy 21.10.2016.
 */
public interface DocumentUpdater {
    boolean updateDocument(SaleDocument document) throws Exception;
}
