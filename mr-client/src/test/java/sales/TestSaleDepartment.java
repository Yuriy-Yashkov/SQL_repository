package sales;

import by.gomel.freedev.ucframework.ucdao.DaoFactory;
import by.gomel.freedev.ucframework.ucdao.interfaces.IGenericDao;
import by.gomel.freedev.ucframework.ucdao.model.QueryProperty;
import by.march8.api.utils.DatePeriod;
import by.march8.api.utils.DateUtils;
import by.march8.ecs.MainController;
import by.march8.ecs.application.modules.references.classifier.model.ItemNameReplacer;
import by.march8.ecs.application.modules.sales.model.PreOrderSaleDocumentReport;
import by.march8.ecs.application.modules.sales.report.PreOrderSalesHTMLReport;
import by.march8.ecs.application.modules.warehouse.external.shipping.model.SaleDocumentSet;
import by.march8.entities.sales.PreOrderProductItem;
import by.march8.entities.warehouse.SaleDocumentBase;
import org.junit.Ignore;
import org.junit.Test;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Andy 14.01.2019 - 7:52.
 */
public class TestSaleDepartment {

    private List<PreOrderProductItem> getProductList(int articleId) {
        DaoFactory<PreOrderProductItem> factory = DaoFactory.getInstance();
        IGenericDao<PreOrderProductItem> dao = factory.getGenericDao();
        List<QueryProperty> criteria = new ArrayList<>();
        criteria.add(new QueryProperty("article", articleId));
        List<PreOrderProductItem> list = null;
        try {
            list = dao.getEntityListByNamedQuery(PreOrderProductItem.class, "PreOrderProductItem.find1stGradeByArticleId", criteria);
            for (PreOrderProductItem item : list) {
                item.setName(ItemNameReplacer.transform(item.getName()));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    @Test
    @Ignore
    public void testPrepareWebExport() {
        MainController.setRunPath("d:/SVN/MyReports/MyReports20/");
        List<PreOrderProductItem> list = getProductList(45454);
        list.addAll(getProductList(47510));
        list.addAll(getProductList(24651));
        PreOrderSaleDocumentReport reportData = new PreOrderSaleDocumentReport();
        reportData.setSpecification(list);
        PreOrderSalesHTMLReport report = new PreOrderSalesHTMLReport();
        report.create("c:/html/", reportData);
    }

    @Test
    @Ignore
    public void testGetSaleDocumentByContractor() {
        int contractorCode = 4955;
        DatePeriod period = new DatePeriod();
        period.setBegin(DateUtils.getDateByStringValue("01.01.2018"));
        period.setEnd(DateUtils.getDateByStringValue("31.01.2018"));
        System.out.println(DateUtils.getPeriodForDate(new Date()).printPeriod());
        List<SaleDocumentBase> list = getSaleDocumentByContractorCodeAndPeriod(4955, period);

        List<List<SaleDocumentSet>> listsOfPeriods = new ArrayList<>();


        listsOfPeriods.add(getSaleDocumentPerCOntractorAndYear(4955, 2017));
        listsOfPeriods.add(getSaleDocumentPerCOntractorAndYear(4955, 2018));

        for (List<SaleDocumentSet> periodList : listsOfPeriods) {
            for (SaleDocumentSet set_ : periodList) {
                System.out.println(set_.printInformation());
            }
            System.out.println("**************************************************");
        }

    }

    private List<SaleDocumentSet> getSaleDocumentPerCOntractorAndYear(int contractor, int year) {
        List<DatePeriod> periodList = DateUtils.getPeriodListForYear(year);
        List<SaleDocumentSet> documentSet = new ArrayList<>();
        for (DatePeriod period : periodList) {
            SaleDocumentSet set_ = new SaleDocumentSet();
            set_.setPeriod(period);
            //set_.setContractor(contractor);
            set_.setDocuments(getSaleDocumentByContractorCodeAndPeriod(contractor, period));
            documentSet.add(set_);
        }
        return documentSet;
    }

    private List<SaleDocumentBase> getSaleDocumentByContractorCodeAndPeriod(int contractorId, DatePeriod period) {
        DaoFactory<SaleDocumentBase> factory = DaoFactory.getInstance();
        IGenericDao<SaleDocumentBase> dao = factory.getGenericDao();
        List<QueryProperty> criteria = new ArrayList<>();
        criteria.add(new QueryProperty("periodBegin", period.getBegin()));
        criteria.add(new QueryProperty("periodEnd", period.getEnd()));
        criteria.add(new QueryProperty("contractor", contractorId));
        List<SaleDocumentBase> list = null;
        try {
            list = dao.getEntityListByNamedQuery(SaleDocumentBase.class, "SaleDocumentBase.findByPeriodAndContractorCode", criteria);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }
}
