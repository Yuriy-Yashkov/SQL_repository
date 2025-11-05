package by.march8.ecs.application.modules.warehouse.external.shipping.dao;

import by.gomel.freedev.ucframework.ucdao.jdbc.AbstractMSSQLServerJDBC;
import by.march8.api.utils.DatePeriod;
import by.march8.api.utils.DateUtils;
import by.march8.ecs.application.modules.warehouse.external.shipping.model.BaseLabelInformation;
import by.march8.ecs.application.modules.warehouse.external.shipping.model.ContractorSequencer;
import by.march8.ecs.application.modules.warehouse.external.shipping.model.EanCodeSaleDocumentItem;
import by.march8.ecs.application.modules.warehouse.external.shipping.model.SaleDocumentView;
import by.march8.ecs.application.modules.warehouse.external.shipping.model.SetMonitorModel;
import by.march8.ecs.framework.common.BackgroundTask;
import by.march8.entities.sales.SalesDocumentSimple;
import by.march8.entities.warehouse.SaleDocumentEntity;
import dept.marketing.cena.CenaPDB;
import dept.marketing.cena.ProfitabilityItem;
import dept.ves.model.AnalysisDetailItem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * @author Andy 14.11.2016.
 */
public class SaleDocumentJDBC extends AbstractMSSQLServerJDBC {

    private static final Logger log = Logger.getLogger(SaleDocumentJDBC.class.getName());

    public int getClassifierVatValue(int documentId) {
        String query = "select item_id as id, isnull(vat,0) as vat from otgruz2 \n" +
                "LEFT JOIN (select nds as vat, kod1 from nsi_sd) as sd on sd.kod1 = otgruz2.kod_izd\n" +
                "WHERE doc_id = ? ";

        try (PreparedStatement ps = getConnection().prepareStatement(query)) {
            ps.setInt(1, documentId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                if (rs.getInt("vat") != 0) {
                    return rs.getInt("vat");
                }
            }
        } catch (Exception ex) {
            log.severe("Ошибка при выполнении функции getClassifierVatValue: " + ex.getMessage());
        }
        return 0;
    }

    public boolean eanCodeIsExist(final String value, int amount) {
        String query = "select count(*) as amount from nsi_sd where ean=?";
        try (PreparedStatement ps = getConnection().prepareStatement(query)) {
            ps.setString(1, value);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int size = rs.getInt("amount");
                if (size > 1) {
                    log.info("Двойной штрихкод: " + value);
                    return false;
                }
                return true;
            }
        } catch (Exception e) {
            log.severe("Ошибка получение изделия по EAN коду " + e.getMessage());
        }
        return false;
    }

    public String getTNVDBySar(final String sar) {
        String query = "select narp from nsi_kld where sar=?";
        String result = "";
        try (PreparedStatement ps = getConnection().prepareStatement(query)) {
            ps.setString(1, sar);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                result = rs.getString("narp");
            }
        } catch (Exception e) {
            log.severe("Ошибка получение ТНВД по шифру артукула" + e.getMessage());
        }
        return result;
    }

    public String getArticleCodeByModelNumber(final String modelNumber) {
        String query = "select sar from nsi_kld where fas=?";
        String result = "";
        try (PreparedStatement ps = getConnection().prepareStatement(query)) {
            ps.setString(1, modelNumber);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                result = rs.getString("sar");
            }
        } catch (Exception e) {
            log.severe("Ошибка получение шифра артикула по номеру артикула" + e.getMessage());
        }
        return result;
    }

    public List<String> getAllEanCode() {
        String query = "SELECT EANCODE FROM NSI_EANCODE WHERE LEN(EANCODE)>12";
        List<String> result = new ArrayList<>();
        try (PreparedStatement ps = getConnection().prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                result.add(rs.getString("EANCODE").trim());
            }
        } catch (Exception e) {
            log.severe("Ошибка получение EAN кодов " + e.getMessage());
        }
        return result;
    }

    public List<String> selectAllItems() {
        String query = "SELECT item_id FROM otgruz2";
        List<String> result = new ArrayList<>();
        try (PreparedStatement ps = getConnection().prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                result.add(rs.getString("item_id").trim());
            }
        } catch (Exception e) {
            log.severe("Ошибка получение всех идентификаторов " + e.getMessage());
        }

        return result;
    }

    public void updateEanCode(EanCodeSaleDocumentItem item) {

        String sqlUpdate = "UPDATE otgruz2 SET eancode=? WHERE item_id=?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sqlUpdate)) {

            conn.setAutoCommit(false);

            ps.setString(1, item.getEanCode());
            ps.setInt(2, item.getId());
            ps.executeUpdate();

            conn.commit();
        } catch (Exception e) {
            log.severe("Ошибка получение всех идентификаторов" + e.getMessage());
        }
    }

    public BaseLabelInformation getLabelInformation(EanCodeSaleDocumentItem itemCode) {
        BaseLabelInformation result = null;

        String sqlGetPackInfo = "SELECT label.barcode, label.eancode, label.ncw FROM upack1 " +
                "LEFT JOIN (SELECT barcode, eancode, ncw FROM label_one) as label ON label.barcode = upack1.barcode " +
                "WHERE upack1.kod_str = ?";

        String sql = "SELECT barcode, eancode, ncw FROM label_one " +
                "WHERE barcode = ?";

        if (itemCode.getType() == 2) {
            sql = sqlGetPackInfo;
        }

        try (PreparedStatement ps = getConnection().prepareStatement(sql)) {

            if (itemCode.getBarCodeId().startsWith("4")) {
                ps.setString(1, itemCode.getBarCode());
            } else {
                ps.setString(1, itemCode.getBarCodeId());
            }

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                result = new BaseLabelInformation();
                result.setColor(rs.getString("ncw"));
                result.setBarCode(rs.getString("barcode"));
                result.setEanCode(rs.getString("eancode"));
            }
        } catch (Exception e) {
            log.severe("Ошибка получение всех идентификаторов" + e.getMessage());
        }
        return result;
    }

    public Map<Integer, BaseLabelInformation> getLabelInformationByDocumentIdAsMap(int documentId) {
        Map<Integer, BaseLabelInformation> result = new HashMap<>();

        String sqlGetLabel = "SELECT * from GET_EAN_DIFFERENCE_BY_DOC_ID(?)";

        try (PreparedStatement ps = getConnection().prepareStatement(sqlGetLabel)) {
            ps.setInt(1, documentId);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                BaseLabelInformation item = new BaseLabelInformation();
                int key = rs.getInt("item_id");
                item.setColor(rs.getString("lbl_color"));
                item.setEanCode(rs.getString("lbl_ean"));
                result.put(key, item);
            }
        } catch (Exception e) {
            log.severe("Ошибка получение всех идентификаторов " + e.getMessage());
        }

        return result;
    }

    public List<String> getSameDocumentNumbers(SaleDocumentView selectedItem) {
        String query = "SELECT ndoc FROM otgruz1 WHERE ndoc like '" + selectedItem.getDocumentNumber() + "%' ORDER BY ndoc";
        List<String> result = new ArrayList<>();
        try (PreparedStatement ps = getConnection().prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                result.add(rs.getString("ndoc").trim());
            }
        } catch (Exception e) {
            log.severe("Ошибка метода getSameDocumentNumbers()" + e.getMessage());
        }
        return result;
    }

    public List<SetMonitorModel> getSaleDocumentMonitorForContractorsAndModels(DatePeriod period, String whereContractors, String whereModels) {
        String queryString = "SELECT " +
                "SUM(AMOUNT*AMOUNT_IN_PACK) AS AMOUNT ," +
                "GRADE," +
                "[SIZE], " +
                "GROWTH, " +
                "MODEL_NUMBER, " +
                "UNLOADING_ADDRESS_ID, " +
                "CONTRACTOR_CODE " +
                "FROM VIEW_SALE_DOCUMENT_CONTRACTOR AS GET_ " +
                "WHERE GET_.CONTRACTOR_CODE IN (?CONTRACTORS) " +
                "AND (GET_.DOCUMENT_DATE BETWEEN CONVERT(DATETIME, ?, 102) AND CONVERT(DATETIME, ?, 102)) " +
                "AND (GET_.MODEL_NUMBER IN (?MODELS)) " +
                "GROUP BY GRADE,[SIZE], GROWTH, MODEL_NUMBER, CONTRACTOR_CODE, UNLOADING_ADDRESS_ID " +
                "ORDER BY CONTRACTOR_CODE, UNLOADING_ADDRESS_ID, MODEL_NUMBER, GRADE, SIZE, GROWTH ";

        queryString = queryString.replace("?CONTRACTORS", whereContractors)
                .replace("?MODELS", whereModels);

        List<SetMonitorModel> result = new ArrayList<>();
        try (PreparedStatement ps = getConnection().prepareStatement(queryString)) {
            ps.setDate(1, DateUtils.getDateAsSQLDate(period.getBegin()));
            ps.setDate(2, DateUtils.getDateAsSQLDate(period.getEnd()));
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                SetMonitorModel item = new SetMonitorModel();
                item.setAmount(rs.getInt("AMOUNT"));
                item.setGrade(rs.getInt("GRADE"));
                item.setSize(rs.getInt("SIZE"));
                item.setGrowth(rs.getInt("GROWTH"));
                item.setModelNumber(rs.getInt("MODEL_NUMBER"));
                item.setUnloadingId(rs.getInt("UNLOADING_ADDRESS_ID"));
                item.setContractorCode(rs.getInt("CONTRACTOR_CODE"));
                result.add(item);
            }
        } catch (Exception ex) {
            log.severe("Ошибка при выполнении функции getSaleDocumentMonitorForContractorsAndModels: " + ex.getMessage());
        }

        return result;
    }

    public List<ContractorSequencer> getContractorsByAddressId(String contractors) {
        String queryString = "SELECT " +
                "dbo.s_klient.NAIM AS CONTRACTOR_NAME, " +
                "dbo.s_adres.NAIM AS CONTRACTOR_ADDRESS, " +
                "dbo.s_klient.KOD AS CONTRACTOR_CODE, " +
                "dbo.s_adres.KLIENT_ID AS CONTRACTOR_ID, " +
                "dbo.s_adres.ITEM_ID AS ADDRESS_ID " +
                "FROM " +
                "dbo.s_adres " +
                "INNER JOIN dbo.s_klient ON dbo.s_adres.KLIENT_ID = dbo.s_klient.ITEM_ID " +
                "WHERE " +
                "dbo.s_adres.ITEM_ID IN (?CONTRACTORS) " +
                "ORDER BY s_klient.NAIM";

        queryString = queryString.replace("?CONTRACTORS", contractors);

        List<ContractorSequencer> result = new ArrayList<>();
        try (PreparedStatement ps = getConnection().prepareStatement(queryString);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                ContractorSequencer item = new ContractorSequencer();
                item.setName(rs.getString("CONTRACTOR_NAME"));
                item.setAddress(rs.getString("CONTRACTOR_ADDRESS"));
                item.setCode(rs.getInt("CONTRACTOR_CODE"));
                item.setId(rs.getInt("ADDRESS_ID"));
                result.add(item);
            }
        } catch (Exception ex) {
            log.severe("Ошибка при выполнении функции getSaleDocumentMonitorForContractorsAndModels :" + ex.getMessage());
        }

        return result;

    }

    public int getContractorCodeById(int contractorId) {
        String query = "select KOD from s_klient where ITEM_ID =?";
        try (PreparedStatement ps = getConnection().prepareStatement(query)) {
            ps.setInt(1, contractorId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                if (rs.getInt("KOD") != 0) {
                    return rs.getInt("KOD");
                }
            }
        } catch (Exception ex) {
            log.severe("Ошибка при выполнении функции getClassifierVatValue: " + ex.getMessage());
        }
        return -1;
    }

    public List<SaleDocumentEntity> getSaleDocumentsForContractorsAndPeriod(DatePeriod period, int contractorCode, boolean openedOnly) {
        List<SaleDocumentEntity> result = new ArrayList<>();
        try (PreparedStatement ps = getConnection().prepareStatement(SaleDocumentQueries.sqlSelectSaleDocumentsOVES)) {
            ps.setInt(1, contractorCode);
            ps.setDate(2, DateUtils.getDateAsSQLDate(period.getBegin()));
            ps.setDate(3, DateUtils.getDateAsSQLDate(period.getEnd()));
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                SaleDocumentEntity item = new SaleDocumentEntity();
                item.setId(rs.getInt("ID"));
                item.setDocumentNumber(rs.getString("DOCUMENT_NUMBER"));
                item.setDocumentDate(rs.getDate("DOCUMENT_DATE"));
                item.setDocumentStatus(rs.getInt("STATUS"));
                item.setCalculated(rs.getBoolean("CALC"));
                if (openedOnly) {
                    if ((item.getDocumentStatus() > 0) && (item.isCalculated())) {
                        result.add(item);
                    }
                } else {
                    result.add(item);
                }
            }
        } catch (Exception ex) {
            log.severe("Ошибка при выполнении функции getSaleDocumentsForContractorsAndPeriod :" + ex.getMessage());
        }
        return result;
    }

    public List<SaleDocumentEntity> getClosedSaleDocumentsForContractorsAndPeriod(DatePeriod period, int contractorCode) {
        List<SaleDocumentEntity> result = new ArrayList<>();
        try (PreparedStatement ps = getConnection().prepareStatement(SaleDocumentQueries.sqlSelectClosedSaleDocumentsOVES)) {
            ps.setInt(1, contractorCode);
            ps.setDate(2, DateUtils.getDateAsSQLDate(period.getBegin()));
            ps.setDate(3, DateUtils.getDateAsSQLDate(period.getEnd()));
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                SaleDocumentEntity item = new SaleDocumentEntity();
                item.setId(rs.getInt("ID"));
                item.setDocumentNumber(rs.getString("DOCUMENT_NUMBER"));
                item.setDocumentDate(rs.getDate("DOCUMENT_DATE"));
                item.setDocumentStatus(rs.getInt("STATUS"));
                item.setCalculated(rs.getBoolean("CALC"));
                result.add(item);
            }
        } catch (Exception ex) {
            log.severe("Ошибка при выполнении функции getSaleDocumentsForContractorsAndPeriod :" + ex.getMessage());
        }
        return result;
    }

    public List<SaleDocumentEntity> getRefundDocumentsForContractorsAndPeriod(DatePeriod period, int contractorCode) {
        List<SaleDocumentEntity> result = new ArrayList<>();
        try (PreparedStatement ps = getConnection().prepareStatement(SaleDocumentQueries.sqlSelectSaleDocumentsOVES)) {
            ps.setInt(1, contractorCode);
            ps.setDate(2, DateUtils.getDateAsSQLDate(period.getBegin()));
            ps.setDate(3, DateUtils.getDateAsSQLDate(period.getEnd()));
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                SaleDocumentEntity item = new SaleDocumentEntity();
                item.setId(rs.getInt("ID"));
                item.setDocumentNumber(rs.getString("DOCUMENT_NUMBER"));
                item.setDocumentDate(rs.getDate("DOCUMENT_DATE"));
                item.setDocumentStatus(rs.getInt("STATUS"));
                item.setCalculated(rs.getBoolean("CALC"));
                result.add(item);
            }
        } catch (Exception ex) {
            log.severe("Ошибка при выполнении функции getSaleDocumentsForContractorsAndPeriod :" + ex.getMessage());
        }

        return result;
    }

    public List<AnalysisDetailItem> getAnalysisByPeriod(DatePeriod period) {

        List<AnalysisDetailItem> result = new ArrayList<>();
        try (PreparedStatement ps = getConnection().prepareStatement(SaleDocumentQueries.sqlSelectAnalysisDetailOVES)) {
            ps.setDate(1, DateUtils.getDateAsSQLDate(period.getBegin()));
            ps.setDate(2, DateUtils.getDateAsSQLDate(period.getEnd()));
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                AnalysisDetailItem item = new AnalysisDetailItem();

                item.setDateSale(rs.getDate("SALE_DATE"));
                item.setContractorCode(rs.getInt("CONTRACTOR"));
                item.setExport(rs.getInt("EXPORT"));
                item.setItemName(rs.getString("ITEM_NAME"));
                item.setArticleCode(rs.getInt("ARTICLE_CO"));
                item.setSize(rs.getInt("SIZE"));
                item.setAmount(rs.getInt("AMOUNT"));
                item.setSumCost(rs.getDouble("ITOGO"));
                item.setSumCostAndVat(rs.getDouble("ITOGOVAT"));
                item.setPrimeCost(rs.getDouble("PRIMECOST"));

                result.add(item);

            }
        } catch (Exception ex) {
            log.severe("Ошибка при выполнении функции getAnalysisByPeriod :" + ex.getMessage());
        }
        return result;
    }

    public void updatePrimeCostBatch(BackgroundTask task, List<AnalysisDetailItem> list) {
        CenaPDB cdb = new CenaPDB();
        String sqlUpdate = "UPDATE MEGA_PLAIN_ANALYSIS_NEW SET PRIMECOST=? WHERE ARTICLE_CO=? AND SIZE = ?";
        try (Connection conn = getConnection();
        PreparedStatement ps = conn.prepareStatement(sqlUpdate)) {

            conn.setAutoCommit(false);

            int count = list.size();
            int current = 1;

            for (AnalysisDetailItem item : list) {
                task.setText("Обработка " + current + " из " + count);

                ProfitabilityItem value = cdb.getSstoimostRentabelAdv(item.getArticleCode(), item.getSize());
                if (value != null) {
                    ps.setDouble(1, value.getPrimeCostReference());
                    ps.setInt(2, item.getArticleCode());
                    ps.setInt(3, item.getSize());
                    ps.addBatch();
                } else {
                    System.out.println("EMPTY");
                }
                current++;
            }
            ps.executeBatch();
            conn.commit();
        } catch (Exception e) {
            log.severe("Ошибка метода updatePrimeCost" + e.getMessage());
        }
    }

    public void updatePrimeCost(int articleCode, int size, double primeCost) {
        String sqlUpdate = "UPDATE MEGA_PLAIN_ANALYSIS_NEW SET PRIMECOST=? WHERE ARTICLE_CO=? AND SIZE = ?";
        try (PreparedStatement ps = getConnection().prepareStatement(sqlUpdate)) {

            ps.setDouble(1, primeCost);
            ps.setInt(2, articleCode);
            ps.setInt(3, size);

            ps.executeUpdate();
        } catch (Exception e) {
            log.severe("Ошибка метода updatePrimeCost " + e.getMessage());
        }
    }

    public List<String> getUploadAddressByContractorCode(int code) {
        List<String> result = new ArrayList<>();
        String sql = "SELECT s_adres.NAIM, s_adres.ITEM_ID  FROM s_adres " +
                "LEFT JOIN s_klient AS contractor on contractor.ITEM_ID = s_adres.KLIENT_ID " +
                "WHERE contractor.KOD = ?";
        try (PreparedStatement ps = getConnection().prepareStatement(sql)) {
            ps.setInt(1, code);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                result.add(rs.getString("NAIM").trim() + "_" + rs.getInt("ITEM_ID"));
            }
        } catch (Exception ex) {
            log.severe("Ошибка при выполнении функции getAnalysisByPeriod :" + ex.getMessage());
        }

        return result;
    }

    public Date getSaleDateForDocumentId(int documentId) {
        String sql = "SELECT DOCUMENT_DATE FROM DRIVING_DIRECTION_DOCUMENT WHERE SALE_DOCUMENT_ID = ?";
        Date result = null;
        try (PreparedStatement ps = getConnection().prepareStatement(sql)) {
            ps.setInt(1, documentId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                result = rs.getDate("DOCUMENT_DATE");
            }
        } catch (Exception ex) {
            log.severe("Ошибка при выполнении функции getSaleDateForDocumentId :" + ex.getMessage());
        }

        return result;
    }

    public void updateSaleDateForDocumentId(int documentId, Date saleDate) {
        String sqlUpdate = "UPDATE DRIVING_DIRECTION_DOCUMENT SET DOCUMENT_DATE=? WHERE SALE_DOCUMENT_ID=? ";
        try (PreparedStatement ps = getConnection().prepareStatement(sqlUpdate)) {

            ps.setDate(1, DateUtils.getDateAsSQLDate(saleDate));
            ps.setInt(2, documentId);

            ps.executeUpdate();

        } catch (Exception e) {
            log.severe("Ошибка метода updateSaleDateForDocumentId" + e.getMessage());
        }
    }

    public String getDocumentNumberForDocumentId(int documentId) {
        String sql = "SELECT ndoc FROM otgruz1 WHERE item_id = ?";
        String result = null;
        try (PreparedStatement ps = getConnection().prepareStatement(sql)) {
            ps.setInt(1, documentId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                result = rs.getString("ndoc");
            }
        } catch (Exception ex) {
            log.severe("Ошибка при выполнении функции getDocumentNumberForDocumentId :" + ex.getMessage());
        }

        if (result != null) {
            return result.trim();
        } else {
            return null;
        }
    }

    public void updateDocumentNumberForDocumentId(int documentId, String number) {
        String sqlUpdate = "UPDATE otgruz1 SET ndoc=? WHERE item_id=? ";
        try (PreparedStatement ps = getConnection().prepareStatement(sqlUpdate)) {

            ps.setString(1, number);
            ps.setInt(2, documentId);
            ps.executeUpdate();

        } catch (Exception e) {
            log.severe("Ошибка метода updateDocumentNumberForDocumentId" + e.getMessage());
        }
    }

    public String getArticleName(int kod) {
        String artIcle = "";
        try (PreparedStatement ps = getConnection().prepareStatement(SaleDocumentQueries.sqlGetArticleName)) {
            ps.setInt(1, kod);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                artIcle = rs.getString("nar");
            }
        } catch (Exception ex) {
            log.severe("Ошибка при выполнении функции getArticleName: " + ex.getMessage());
        }
        return artIcle;
    }

    public List<SalesDocumentSimple> getSalesDocumentSimpleForContractor(DatePeriod period, int contractorCode) {
        List<SalesDocumentSimple> result = new ArrayList<>();
        try (PreparedStatement ps = getConnection().prepareStatement(SaleDocumentQueries.sqlSelectRefundSaleDocuments)) {
            ps.setInt(1, contractorCode);
            ps.setDate(2, DateUtils.getDateAsSQLDate(period.getBegin()));
            ps.setDate(3, DateUtils.getDateAsSQLDate(period.getEnd()));
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                SalesDocumentSimple item = new SalesDocumentSimple();
                item.setId(rs.getInt("ID"));
                item.setDocumentNumber(rs.getString("DOCUMENT_NUMBER"));
                item.setDocumentDate(rs.getDate("DOCUMENT_DATE"));

                result.add(item);
            }
        } catch (Exception ex) {
            log.severe("Ошибка при выполнении функции getSalesDocumentSimpleForContractor :" + ex.getMessage());
        }
        return result;
    }

    public Map<String, String> getEanInformationAsMap() {
        Map<String, String> result = new HashMap<>();

        String sqlGetLabel = "SELECT * from V_NSI_EANCODE";

        try (PreparedStatement ps = getConnection().prepareStatement(sqlGetLabel);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                String key = rs.getString("ITEM_CODE") + "_" + rs.getString("COLOR_NAME");
                result.put(key, rs.getString("EANCODE"));
            }
        } catch (Exception e) {
            log.severe("Ошибка получение EAN кодов" + e.getMessage());
        }

        return result;
    }
}
