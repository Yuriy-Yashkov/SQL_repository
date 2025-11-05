package by.march8.ecs.application.modules.warehouse.external.shipping.services;

import by.gomel.freedev.ucframework.ucdao.DaoFactory;
import by.gomel.freedev.ucframework.ucdao.interfaces.IGenericDao;
import by.march8.ecs.services.IService;
import by.march8.entities.warehouse.SaleDocumentHistory;

import java.sql.SQLException;
import java.util.Date;

/**
 * Служба ведения истории по документу
 *
 * @author Andy 15.11.2017.
 */
public class SaleDocumentHistoryService implements IService {

    public static final int HISTORY_CREATE_DOCUMENT = 1;
    public static final int HISTORY_EDIT_MAIN_DOCUMENT = 2;
    public static final int HISTORY_EDIT_MAIN_DOCUMENT_NUMBER = 3;
    public static final int HISTORY_EDIT_BSO_DOCUMENT = 4;
    public static final int HISTORY_RECALCULATE_DOCUMENT = 5;
    public static final int HISTORY_CREATE_REPORT = 6;
    public static final int HISTORY_CLOSE_DOCUMENT = 7;
    public static final int HISTORY_OPEN_DOCUMENT = 8;
    public static final int HISTORY_DELETE_DOCUMENT = 9;
    public static final int HISTORY_SEND_EMAIL_DOCUMENT = 10;
    public static final int HISTORY_CREATE_WEB_DOCUMENT = 11;

    private static SaleDocumentHistoryService instance;

    private SaleDocumentHistoryService() {

    }


    /**
     * Возвращает сервис истории документа
     *
     * @return сервис истории документа
     */
    public static SaleDocumentHistoryService getInstance() {
        if (instance == null) {
            instance = new SaleDocumentHistoryService();
        }
        return instance;
    }


    public void historyCreateDocument(int id, String number) {
        SaleDocumentHistory history = getHistoryBlank();
        history.setDocumentId(id);
        history.setDocumentNumber(number);
        history.setOperationId(HISTORY_CREATE_DOCUMENT);
        saveHistory(history);
    }

    public void historyEditDocument(int id, String number) {
        SaleDocumentHistory history = getHistoryBlank();
        history.setDocumentId(id);
        history.setDocumentNumber(number);
        history.setOperationId(HISTORY_EDIT_MAIN_DOCUMENT);
        saveHistory(history);
    }

    public void historyEditNumberDocument(int id, String number, String oldNumber) {
        SaleDocumentHistory history = getHistoryBlank();
        history.setDocumentId(id);
        history.setDocumentNumber(number);
        history.setNote(oldNumber);
        history.setOperationId(HISTORY_EDIT_MAIN_DOCUMENT_NUMBER);
        saveHistory(history);
    }

    public void historyEditBSODocument(int id, String number) {
        SaleDocumentHistory history = getHistoryBlank();
        history.setDocumentId(id);
        history.setDocumentNumber(number);
        history.setOperationId(HISTORY_EDIT_BSO_DOCUMENT);
        saveHistory(history);
    }

    public void historyRecalculateDocument(int id, String number) {
        SaleDocumentHistory history = getHistoryBlank();
        history.setDocumentId(id);
        history.setDocumentNumber(number);
        history.setOperationId(HISTORY_RECALCULATE_DOCUMENT);
        saveHistory(history);
    }

    public void historyCreateReport(int id, String number) {
        SaleDocumentHistory history = getHistoryBlank();
        history.setDocumentId(id);
        history.setDocumentNumber(number);
        history.setOperationId(HISTORY_CREATE_REPORT);
        saveHistory(history);
    }

    public void historyCloseDocument(int id, String number) {
        SaleDocumentHistory history = getHistoryBlank();
        history.setDocumentId(id);
        history.setDocumentNumber(number);
        history.setOperationId(HISTORY_CLOSE_DOCUMENT);
        saveHistory(history);
    }


    public void historyOpenDocument(int id, String number) {
        SaleDocumentHistory history = getHistoryBlank();
        history.setDocumentId(id);
        history.setDocumentNumber(number);
        history.setOperationId(HISTORY_OPEN_DOCUMENT);
        saveHistory(history);
    }

    public void historyDeleteDocument(int id, String number) {
        SaleDocumentHistory history = getHistoryBlank();
        history.setDocumentId(id);
        history.setDocumentNumber(number);
        history.setOperationId(HISTORY_DELETE_DOCUMENT);
        saveHistory(history);
    }

    public void historyCreateWEBDocument(int id, String number) {
        SaleDocumentHistory history = getHistoryBlank();
        history.setDocumentId(id);
        history.setDocumentNumber(number);
        history.setOperationId(HISTORY_CREATE_WEB_DOCUMENT);
        saveHistory(history);
    }

    public void historySendEmail(String number, String eMail) {
        if (eMail != null) {
            SaleDocumentHistory history = getHistoryBlank();
            history.setDocumentId(0);
            history.setDocumentNumber(number.trim());
            history.setNote(eMail.trim());
            history.setOperationId(HISTORY_SEND_EMAIL_DOCUMENT);
            saveHistory(history);
        }
    }

    private SaleDocumentHistory getHistoryBlank() {
        SaleDocumentHistory result = new SaleDocumentHistory();
        result.setOperationDate(new Date());

        return result;
    }


    private void saveHistory(SaleDocumentHistory element) {
        DaoFactory<SaleDocumentHistory> factory = DaoFactory.getInstance();
        IGenericDao<SaleDocumentHistory> dao = factory.getGenericDao();
        try {
            dao.saveEntity(element);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
