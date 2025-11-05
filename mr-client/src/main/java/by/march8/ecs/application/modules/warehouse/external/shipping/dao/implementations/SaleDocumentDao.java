package by.march8.ecs.application.modules.warehouse.external.shipping.dao.implementations;

import by.gomel.freedev.ucframework.ucdao.DaoFactory;
import by.gomel.freedev.ucframework.ucdao.interfaces.INativeDao;
import by.gomel.freedev.ucframework.ucdao.jdbc.DocumentJDBC;
import by.gomel.freedev.ucframework.ucdao.model.QueryProperty;
import by.march8.ecs.MainController;
import by.march8.ecs.application.modules.warehouse.external.shipping.dao.SaleDocumentQueries;
import by.march8.ecs.application.modules.warehouse.external.shipping.dao.interfaces.ISaleDocumentDao;
import by.march8.ecs.application.modules.warehouse.external.shipping.model.March8Marker;
import by.march8.ecs.application.modules.warehouse.external.shipping.model.SaleDocumentView;
import by.march8.ecs.framework.common.BackgroundTask;
import by.march8.entities.warehouse.ContractorBank;
import by.march8.entities.warehouse.SaleDocumentAnalysisItem;
import by.march8.entities.warehouse.SaleDocumentDetailItemReport;
import by.march8.entities.warehouse.SaleDocumentDriving;
import by.march8.entities.warehouse.SaleDocumentDrivingAdditional;
import by.march8.entities.warehouse.SaleDocumentEntity;
import by.march8.entities.warehouse.SaleDocumentItemView;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * DAO реализация доступа к данным накладной
 * Created by Andy on 07.08.2015.
 */
public class SaleDocumentDao implements ISaleDocumentDao {

    @Override
    public List<SaleDocumentView> getSaleDocumentsByPeriod(String documentType, final Date dateBegin, final Date dateEnd, int currency) {
        ArrayList<SaleDocumentView> data = new ArrayList<>();
        Class<?> dbMarker = March8Marker.class;
        DaoFactory factory = DaoFactory.getInstance();
        INativeDao dao = factory.getNativeDao();
        dao.processing(connection -> {
            try {
                String query = null;
                boolean isComplexQuery = false;
                if (!documentType.toLowerCase().equals("показать все")) {
                    isComplexQuery = true;
                    if (currency < 0) {
                        query = SaleDocumentQueries.allSaleDocumentsByPeriodAndType;
                    } else {
                        query = SaleDocumentQueries.allSaleDocumentsByPeriodAndTypeAndCurr;
                    }
                } else {
                    if (currency < 0) {
                        query = SaleDocumentQueries.allSaleDocumentsByPeriod;
                    } else {
                        query = SaleDocumentQueries.allSaleDocumentsByPeriodAndCurr;
                    }
                }

                PreparedStatement ps = connection.prepareStatement(query);

                ps.setDate(1, new java.sql.Date(dateBegin.getTime()));
                ps.setDate(2, new java.sql.Date(dateEnd.getTime()));

                if (isComplexQuery) {
                    ps.setString(3, documentType);
                    if (currency >= 0) {
                        ps.setInt(4, currency);
                    }
                } else {
                    if (currency >= 0) {
                        ps.setInt(3, currency);
                    }
                }

                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    SaleDocumentView document = new SaleDocumentView();
                    document.setId(rs.getInt(10));
                    document.setDate(rs.getDate(1));
                    document.setDocumentNumber(rs.getString(2).trim());
                    document.setOperation(rs.getString(3).trim());
                    document.setContractorCode(rs.getInt(4));
                    document.setContractorName(rs.getString(5).trim());
                    document.setValueSum(rs.getDouble(6));
                    document.setValueVat(rs.getDouble(7));
                    document.setValueSumAndVat(rs.getDouble(8));
                    document.setStatusText(rs.getString(9));
                    document.setBidNumber(0);
                    document.setContractorId(rs.getInt("klient_id"));
                    document.setContractId(rs.getInt("dogovor_id"));
                    document.setAdjustmentType(rs.getInt("adjustment_type"));
                    document.setAdjustmentDocumentNumber(rs.getString("adjustment_ndoc"));
                    document.setSaleDate(rs.getDate("sale_date"));

                    Boolean saved = rs.getBoolean("saved");
                    if (saved) {
                        document.setIsSaved(true);
                    } else {
                        document.setIsSaved(false);
                    }

                    data.add(document);
                }
            } catch (Exception ex) {
                System.err.println("Ошибка получение списка документов "
                        + ex.getMessage());
            }
            return true;
        }, dbMarker);
        return data;
    }

    @Override
    public List<SaleDocumentEntity> getSaleDocumentByPeriodAndContractor(final int documentId, final Date dateBegin, final Date dateEnd, int contractorId) {
        List<SaleDocumentEntity> data = new ArrayList<>();
        Class<?> dbMarker = March8Marker.class;
        DaoFactory factory = DaoFactory.getInstance();
        INativeDao dao = factory.getNativeDao();
        dao.processing(connection -> {
            try {
                String query = SaleDocumentQueries.queryGetDocsForInvoicesByContractor;

                if (documentId == 0) {
                    query = SaleDocumentQueries.queryGetDocsForInvoicesByContractor;
                }

                PreparedStatement ps = connection.prepareStatement(query);

                ps.setDate(1, new java.sql.Date(dateBegin.getTime()));
                ps.setDate(2, new java.sql.Date(dateEnd.getTime()));
                ps.setInt(3, contractorId);

                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    SaleDocumentEntity item = new SaleDocumentEntity();
                    item.setId(rs.getInt("item_id"));
                    item.setDocumentDate(new Date());
                    item.setDocumentNumber(rs.getString("ndoc"));
                    item.setDocumentStatus(rs.getInt("status"));
                    item.setIsExport(rs.getInt("export"));
                    item.setAdjustmentType(rs.getInt("adjustment_type"));
                    item.setAdjustmentDocumentId(rs.getInt("adjustment_ndoc"));
                    data.add(item);
                }
            } catch (Exception ex) {
                System.err.println("Ошибка получение списка документов "
                        + ex.getMessage());
            }
            return true;
        }, dbMarker);

        return data;
    }

    @Override
    public List<SaleDocumentEntity> getAllSaleDocumentByPeriodAndContractor(final int documentId, final Date dateBegin, final Date dateEnd, int contractorId) {
        List<SaleDocumentEntity> data = new ArrayList<>();
        Class<?> dbMarker = March8Marker.class;
        DaoFactory factory = DaoFactory.getInstance();
        INativeDao dao = factory.getNativeDao();
        dao.processing(connection -> {
            try {
                String query = SaleDocumentQueries.queryGetDocsForInvoicesByContractor;

                if (documentId == 0) {
                    query = SaleDocumentQueries.queryGetAllDocsForInvoicesByContractor;
                }

                PreparedStatement ps = connection.prepareStatement(query);

                ps.setDate(1, new java.sql.Date(dateBegin.getTime()));
                ps.setDate(2, new java.sql.Date(dateEnd.getTime()));
                ps.setInt(3, contractorId);

                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    SaleDocumentEntity item = new SaleDocumentEntity();
                    item.setId(rs.getInt("item_id"));
                    item.setDocumentDate(new Date());
                    item.setDocumentNumber(rs.getString("ndoc"));
                    item.setDocumentStatus(rs.getInt("status"));
                    item.setIsExport(rs.getInt("export"));
                    item.setAdjustmentType(rs.getInt("adjustment_type"));
                    item.setAdjustmentDocumentId(rs.getInt("adjustment_ndoc"));
                    data.add(item);
                }
            } catch (Exception ex) {
                System.err.println("Ошибка получение списка документов "
                        + ex.getMessage());
            }
            return true;
        }, dbMarker);

        return data;
    }


    @Override
    public List<SaleDocumentDetailItemReport> getSaleDocumentsForReport(int id, String documentNumber) {
        ArrayList<SaleDocumentDetailItemReport> data = new ArrayList<>();
        Class<?> dbMarker = March8Marker.class;
        DaoFactory factory = DaoFactory.getInstance();
        INativeDao dao = factory.getNativeDao();
        dao.processing(connection -> {
            try {
                String query = SaleDocumentQueries.detailForReports;

                if (id == 1) {
                    query = SaleDocumentQueries.detailForReportsPack;
                }

                if (id == 9) {
                    query = SaleDocumentQueries.sqlPreOrderSaleDocument;
                }

                PreparedStatement ps = connection.prepareStatement(query);
                if (id != 9) {
                    ps.setString(1, documentNumber);
                } else {
                    ps.setInt(1, Integer.valueOf(documentNumber));
                }

                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    SaleDocumentDetailItemReport item = new SaleDocumentDetailItemReport();
                    item.setId(rs.getInt("id"));
                    item.setDocument(rs.getString("ndoc"));

                    item.setArticleCode(rs.getString("article_code"));
                    item.setArticleNumber(rs.getString("article_number"));
                    item.setModelNumber(rs.getString("model_number"));
                    item.setItemName(rs.getString("item_name"));

                    item.setComposition(rs.getString("composition"));
                    item.setCanvasComposition(rs.getString("prim"));

                    item.setTnvedCode(rs.getString("tnvd_code"));
                    item.setEanCode(rs.getString("ean_code"));
                    item.setItemColor(rs.getString("item_color"));
                    item.setItemGrowz(rs.getInt("item_growth"));
                    item.setItemSize(rs.getInt("item_size"));
                    item.setItemSizePrint(rs.getString("item_size_print"));
                    item.setItemGrade(rs.getInt("item_grade"));
                    item.setAmount(rs.getInt("amount"));
                    item.setAmountAll(rs.getInt("amount_all"));

                    item.setValueAccountingPrice(rs.getDouble("accounting_price"));
                    item.setValueVAT(rs.getFloat("vat"));

                    item.setValuePrice(rs.getDouble("price"));
                    item.setValueSumCost(rs.getDouble("sum_cost"));
                    item.setValueSumVat(rs.getDouble("sum_vat"));
                    item.setValueSumCostAndVat(rs.getDouble("sum_cost_vat"));

                    item.setValuePriceCurrency(rs.getDouble("price_currency"));
                    item.setValueSumCostCurrency(rs.getDouble("sum_cost_currency"));
                    item.setValueSumVatCurrency(rs.getDouble("sum_vat_currency"));
                    item.setValueSumCostAndVatCurrency(rs.getDouble("sum_cost_vat_currency"));
                    item.setTradeMarkup(rs.getDouble("trade_markup"));

                    item.setValueTradeMarkup(0.0);
                    item.setValueRetailPrice(0.0);

                    item.setWeight(rs.getDouble("weight"));

                    item.setItemPriceList(rs.getString("price_list"));

                    item.setColorCode(rs.getInt("cd_id"));

                    item.setPtkCode(rs.getInt("ptk"));

                    item.setItemAgeGroup(rs.getInt("vzr"));

                    if (id == 9) {
                        item.setDiscountValue(rs.getFloat("DISCOUNT_VALUE"));
                    }

                    if (id == 1) {
                        //item.setDiscountValue(Float.parseFloat(rs.getString("disc")));
                        item.setDiscountValue(rs.getFloat("disc"));
                    }

                    data.add(item);
                }
            } catch (Exception ex) {
                System.err.println("Ошибка получение списка документов ");
                ex.printStackTrace();
            }
            return true;
        }, dbMarker);


        return data;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<SaleDocumentView> getSaleDocumentsByPeriodThread(final String documentType, final Date dateBegin, final Date dateEnd, final int currency) {
        class Task extends BackgroundTask {
            public Task(final String messageText) {
                super(messageText);
            }

            @Override
            protected List<SaleDocumentView> doInBackground() throws Exception {
                return getSaleDocumentsByPeriod(documentType, dateBegin, dateEnd, currency);
            }
        }

        Task task = new Task("Получение данных...");
        task.executeTask();
        try {
            return (List<SaleDocumentView>) task.get();
        } catch (Exception e) {
            MainController.exception(e, "Ошибка получения данных для метода getSaleDocumentsByPeriodThread");
            return null;
        }
    }

    @Override
    public ContractorBank getContractorBankByContractorCode(final int contractorCode) {
        ContractorBank result = new ContractorBank();
        Class<?> dbMarker = March8Marker.class;
        DaoFactory factory = DaoFactory.getInstance();
        INativeDao dao = factory.getNativeDao();
        dao.processing(connection -> {
            try {
                String query = SaleDocumentQueries.getBankInformationByContractorCode;

                PreparedStatement ps = connection.prepareStatement(query);
                ps.setInt(1, contractorCode);

                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    result.setName(rs.getString("NAIMBANK"));
                    result.setAddress(rs.getString("PADRES"));
                    result.setAccountType(rs.getString("NAIMRS"));
                    result.setAccountNumber(rs.getString("NOMER"));

                    result.setCodeMFO(rs.getString("MFO"));
                    result.setCodeOKPO(rs.getString("OKPO"));
                    result.setCodeUNN(rs.getString("KORUNN"));
                    result.setAccountCorrespondentNumber(rs.getString("KORSCHET"));
                }
            } catch (Exception ex) {
                System.err.println("Ошибка получение списка документов "
                        + ex.getMessage());
            }
            return true;
        }, dbMarker);
        return result;
    }

    @Override
    public SaleDocumentView saveSaleDocument(final SaleDocumentView document) {
        ArrayList<SaleDocumentView> data = new ArrayList<>();
        Class<?> dbMarker = March8Marker.class;
        DaoFactory factory = DaoFactory.getInstance();
        INativeDao dao = factory.getNativeDao();
        dao.processing(connection -> {
            try {


            } catch (Exception ex) {
                System.err.println("Ошибка получение ID клиента "
                        + ex.getMessage());
            }
            return true;
        }, dbMarker);
        return document;
    }

    @Override
    public SaleDocumentDriving getSaleDocumentDrivingByDocumentId(final int documentId) {
        SaleDocumentDriving result = new SaleDocumentDriving();
        Class<?> dbMarker = March8Marker.class;
        DaoFactory factory = DaoFactory.getInstance();
        INativeDao dao = factory.getNativeDao();
        dao.processing(connection -> {
            try {
                String query = SaleDocumentQueries.querySaleDocumentDrivingOld;

                PreparedStatement ps = connection.prepareStatement(query);
                ps.setInt(1, documentId);

                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    result.setDocumentNumber(rs.getString("document_number"));
                    result.setDocumentDate(rs.getDate("document_date"));
                    result.setReaddressing(rs.getString("readdressing"));
                    result.setSaleAllowed(rs.getString("sale_allowed"));
                    result.setShipperPassed(rs.getString("shipper_passed"));
                    result.setTransportationReceive(rs.getString("transportation_receive"));
                    result.setSupportDocument(rs.getString("support_document"));
                    result.setNote(rs.getString("note"));
                    result.setDocumentType(rs.getString("document_type"));
                    result.setWarrantNumber(rs.getString("warrant_number"));
                    result.setWarrantDate(rs.getDate("warrant_date"));
                    result.setWarrantIssued(rs.getString("warrant_issued"));
                    result.setWarrantReceive(rs.getString("warrant_receive"));
                    result.setWarrantSealNumber(rs.getString("warrant_seal_number"));
                    result.setCarNumber(rs.getString("car_number"));
                    result.setCarTrailerNumber(rs.getString("car_trailer_number"));
                    result.setCarDriverName(rs.getString("car_driver_name"));
                    result.setCarOwner(rs.getString("car_owner"));
                    result.setCarPayer(rs.getString("car_payer"));
                    result.setCarCustomer(rs.getString("car_customer"));

                    SaleDocumentDrivingAdditional additional = new SaleDocumentDrivingAdditional();
                    additional.setLoadingAddress(rs.getString("loading_address"));
                    additional.setUnloadingAddress(rs.getString("unloading_address"));
                    additional.setSealNumber(rs.getString("seal_number"));
                    additional.setLoadingDoer(rs.getString("loading_doer"));
                    additional.setLoadingMethod(rs.getString("loading_method"));
                    additional.setValueVat(rs.getInt("vat"));

                    result.setAdditional(additional);
                }
            } catch (Exception ex) {
                System.err.println("Ошибка получение деталей путевого листа "
                        + ex.getMessage());
            }
            return true;
        }, dbMarker);

        return result;
    }

    @Override
    public List<SaleDocumentEntity> getSaleDocumentByPeriod(final int documentId, final Date dateBegin, final Date dateEnd) {
        List<SaleDocumentEntity> data = new ArrayList<>();
        Class<?> dbMarker = March8Marker.class;
        DaoFactory factory = DaoFactory.getInstance();
        INativeDao dao = factory.getNativeDao();
        dao.processing(connection -> {
            try {
                String query = SaleDocumentQueries.queryGetDocsForInvoices;

                if (documentId == 0) {
                    query = SaleDocumentQueries.queryGetDocsForInvoicesAll;
                }

                if (documentId == 2) {
                    query = SaleDocumentQueries.queryGetMaterialDocsForInvoicesAll;
                }

                PreparedStatement ps = connection.prepareStatement(query);

                ps.setDate(1, new java.sql.Date(dateBegin.getTime()));
                ps.setDate(2, new java.sql.Date(dateEnd.getTime()));

                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    SaleDocumentEntity item = new SaleDocumentEntity();
                    item.setId(rs.getInt("item_id"));
                    item.setDocumentDate(new Date());
                    item.setDocumentNumber(rs.getString("ndoc"));
                    item.setDocumentStatus(rs.getInt("status"));
                    item.setIsExport(rs.getInt("export"));
                    item.setAdjustmentType(rs.getInt("adjustment_type"));
                    item.setAdjustmentDocumentId(rs.getInt("adjustment_ndoc"));
                    data.add(item);
                }
            } catch (Exception ex) {
                System.err.println("Ошибка получение списка документов "
                        + ex.getMessage());
            }
            return true;
        }, dbMarker);

        return data;
    }

    @Override
    public int getSaleDocumentIdByDocumentNumber(final String documentNumber) {
        Class<?> dbMarker = March8Marker.class;
        DaoFactory factory = DaoFactory.getInstance();
        INativeDao dao = factory.getNativeDao();
        final int result = 0;
        dao.processing(connection -> {
            try {
                String query = SaleDocumentQueries.queryGetDocumentIdByDocumentNumber;
                PreparedStatement ps = connection.prepareStatement(query);
                ps.setString(1, documentNumber);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    int res = rs.getInt("item_id");
                    if (res != 0) {
                        // result = res;
                        break;
                    }
                }
            } catch (Exception ex) {
                System.err.println("Ошибка получение списка документов "
                        + ex.getMessage());
            }
            return true;
        }, dbMarker);

        return result;
    }


    @Override
    public List<SaleDocumentItemView> getEntityListByNamedQuery(final Class<SaleDocumentItemView> className, final String namedQuery, final List<QueryProperty> criteria) {

        DocumentJDBC db = new DocumentJDBC();

        String number = null;
        for (QueryProperty property : criteria) {
            String key = property.getKey();
            if (key.equals("document")) {
                number = (String) property.getValue();
            }
        }

        if (number != null) {
            return db.getSaleDocumentDetailViewList(number);
        } else {
            return new ArrayList<>();
        }
    }

    @Override
    public int getOpenedSaleDocumentEntityByNumber(String documentNumber) {
        Class<?> dbMarker = March8Marker.class;
        DaoFactory factory = DaoFactory.getInstance();
        INativeDao dao = factory.getNativeDao();
        final int[] result = {0};
        dao.processing(connection -> {
            try {
                String query = SaleDocumentQueries.queryGetOpenedDocumentIdByDocumentNumber;
                PreparedStatement ps = connection.prepareStatement(query);
                ps.setString(1, documentNumber);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    int res = rs.getInt("item_id");
                    if (res != 0) {
                        // result = res;
                        result[0] = res;
                        break;
                    }
                }
            } catch (Exception ex) {
                System.err.println("Ошибка получение документа по номеру [" + documentNumber + "]"
                        + ex.getMessage());
            }
            return true;
        }, dbMarker);

        return result[0];
    }

    public List<SaleDocumentAnalysisItem> getSaleDocumentByPeriodExportAnalysis(Date dateBegin, Date dateEnd) {
        String query = "SELECT o1.[date], (o2.kol*kol_in_upack) as amount ,itogo, itogov, CL.ARTICLE_CODE FROM otgruz2 o2 " +
                "LEFT JOIN otgruz1 as o1 on o1.item_id = o2.doc_id " +
                "LEFT JOIN VIEW_NSI_CLASSIFIER as CL ON CL.ITEM_ID = o2.kod_izd " +
                "WHERE ((o1.operac = 'Отгрузка покупателю' OR o1.operac = 'Отгрузка материала') " +
                "AND o1.export = 1 " +
                "AND status = 0 " +
                "AND adjustment_type<2) " +
                "AND ((o1.[date] >= CONVERT(DATETIME, ?, 102))and(o1.[date] <= CONVERT(DATETIME, ?, 102)))";

        List<SaleDocumentAnalysisItem> data = new ArrayList<>();
        Class<?> dbMarker = March8Marker.class;
        DaoFactory factory = DaoFactory.getInstance();
        INativeDao dao = factory.getNativeDao();
        dao.processing(connection -> {
            try {

                PreparedStatement ps = connection.prepareStatement(query);

                ps.setDate(1, new java.sql.Date(dateBegin.getTime()));
                ps.setDate(2, new java.sql.Date(dateEnd.getTime()));

                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    SaleDocumentAnalysisItem item = new SaleDocumentAnalysisItem();
                    item.setDate(rs.getDate("date"));
                    item.setAmount(rs.getDouble("amount"));

                    item.setCost(rs.getDouble("itogo"));
                    item.setCostCurrency(rs.getDouble("itogov"));
                    item.setArticleCode(rs.getString("ARTICLE_CODE"));
                    data.add(item);
                }
            } catch (Exception ex) {
                System.err.println("Ошибка получение списка документов "
                        + ex.getMessage());
            }
            return true;
        }, dbMarker);

        return data;
    }
}
