package by.march8.ecs.application.modules.warehouse.external.shipping.dao.interfaces;

import by.gomel.freedev.ucframework.ucdao.model.QueryProperty;
import by.march8.ecs.application.modules.warehouse.external.shipping.model.SaleDocumentView;
import by.march8.entities.warehouse.ContractorBank;
import by.march8.entities.warehouse.SaleDocumentAnalysisItem;
import by.march8.entities.warehouse.SaleDocumentDetailItemReport;
import by.march8.entities.warehouse.SaleDocumentDriving;
import by.march8.entities.warehouse.SaleDocumentEntity;
import by.march8.entities.warehouse.SaleDocumentItemView;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

/**
 * Интерфейс доступа к данным накладной
 * Created by Andy on 07.08.2015.
 */
public interface ISaleDocumentDao {

    /**
     * Возвращает список накладных за указанный период
     * @param dateBegin начало периода
     * @param dateEnd окончание периода
     * @return список документов
     */
    List<SaleDocumentView> getSaleDocumentsByPeriod(String documentType, Date dateBegin, Date dateEnd, int currency);

    /**
     * Возвращает список накладных за указанный период в отдельном потоке с
     * выводом на экран формы с прогрессом выполнения
     * @param dateBegin начало периода
     * @param dateEnd окончание периода
     * @return список документов
     */
    List<SaleDocumentView> getSaleDocumentsByPeriodThread(String documentType, Date dateBegin, Date dateEnd, int currency);

    SaleDocumentView saveSaleDocument(SaleDocumentView document);

    List<SaleDocumentDetailItemReport> getSaleDocumentsForReport(int id, String documentNumber);

    /**
     * Возвращает структуру БАНК по КОДУ контрагента
     * @param contractorCode код контрагента, не идентификатор
     * @return БАНК
     */
    ContractorBank getContractorBankByContractorCode(int contractorCode);

    SaleDocumentDriving getSaleDocumentDrivingByDocumentId(int documentId);

    List<SaleDocumentEntity> getSaleDocumentByPeriod(int documentId, Date dateBegin, Date dateEnd);

    List<SaleDocumentEntity> getSaleDocumentByPeriodAndContractor(int documentId, Date dateBegin, Date dateEnd, int contractor);

    List<SaleDocumentEntity> getAllSaleDocumentByPeriodAndContractor(int documentId, Date dateBegin, Date dateEnd, int contractor);

    int getSaleDocumentIdByDocumentNumber(String documentNumber);

    List<SaleDocumentItemView> getEntityListByNamedQuery(final Class<SaleDocumentItemView> className, String namedQuery, List<QueryProperty> criteria) throws SQLException;

    int getOpenedSaleDocumentEntityByNumber(String documentNumber);

    List<SaleDocumentAnalysisItem> getSaleDocumentByPeriodExportAnalysis(Date dateBegin, Date dateEnd);
}
