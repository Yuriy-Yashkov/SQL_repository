package by.march8.ecs.application.modules.warehouse.external.shipping.enums;

/**
 * Статус документа накладной
 * Created by Andy on 15.10.2015.
 */
public class SaleDocumentStatus {

    /**
     * Документ закрыт
     */
    public static final int CLOSED = 0;

    /**
     * Документ готовится к открытию, только запись
     */
    public static final int PRE_FORMED = 8;

    /**
     * Документ открыт и формируется, только чтение
     */
    public static final int FORMED = 3;

    /**
     * Документ помечен на удаление
     */
    public static final int DELETED = 1;

}
