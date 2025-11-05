package by.march8.ecs.application.modules.warehouse.external.shipping.analysis;

import by.march8.ecs.application.modules.warehouse.external.shipping.dao.DAOSaleDocumentFactory;
import by.march8.ecs.application.modules.warehouse.external.shipping.dao.interfaces.ISaleDocumentDao;
import by.march8.entities.warehouse.SaleDocumentAnalysisItem;
import by.march8.entities.warehouse.SaleDocumentEntity;

import java.util.Date;
import java.util.List;

public abstract class AbstractDataAnalysis {

    /**
     * Получает список документов для периода
     *
     * @param documentType 0 - все закрытые документы 1 - только закрытые кроме розницы
     * @param dateBegin    начало периода отбора
     * @param dateEnd      конец периода отбора
     * @return список актуальных документов за период
     */
    public List<SaleDocumentEntity> getSaleDocumentListByPeriod(int documentType, Date dateBegin, Date dateEnd) {
        List<SaleDocumentEntity> result = null;
        DAOSaleDocumentFactory factory = DAOSaleDocumentFactory.getInstance();
        ISaleDocumentDao dao = factory.getSaleDocumentDao();
        try {
            result = dao.getSaleDocumentByPeriod(documentType, dateBegin, dateEnd);
        } catch (final Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public List<SaleDocumentAnalysisItem> getExportSaleDocumentListByPeriod(Date dateBegin, Date dateEnd) {
        List<SaleDocumentAnalysisItem> result = null;
        DAOSaleDocumentFactory factory = DAOSaleDocumentFactory.getInstance();
        ISaleDocumentDao dao = factory.getSaleDocumentDao();
        try {
            result = dao.getSaleDocumentByPeriodExportAnalysis(dateBegin, dateEnd);
        } catch (final Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public List<SaleDocumentEntity> getSaleDocumentListByPeriodAndContractor(int contractorCode, Date dateBegin, Date dateEnd) {
        List<SaleDocumentEntity> result = null;
        DAOSaleDocumentFactory factory = DAOSaleDocumentFactory.getInstance();
        ISaleDocumentDao dao = factory.getSaleDocumentDao();
        try {
            result = dao.getSaleDocumentByPeriodAndContractor(0, dateBegin, dateEnd, contractorCode);
        } catch (final Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
