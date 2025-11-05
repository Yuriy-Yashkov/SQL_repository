package by.march8.ecs.application.modules.marketing.dao;

import by.gomel.freedev.ucframework.ucdao.jdbc.AbstractMSSQLServerJDBC;
import by.march8.api.utils.DateUtils;
import by.march8.ecs.application.modules.marketing.model.ChartItem;
import by.march8.ecs.application.modules.marketing.model.ScaleItem;
import by.march8.ecs.application.modules.marketing.model.ScaleItemPreliminary;
import by.march8.entities.classifier.ProductionItemBase;
import by.march8.entities.classifier.RemainPriceListDetailItem;
import by.march8.entities.classifier.RemainPriceListItem;
import by.march8.entities.marketing.MarketingPriceListItem;
import by.march8.entities.marketing.ViewMarketingPriceListDetailItem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * @author Andy 26.10.2017.
 */
public class MarketingPriceListMarchJDBC extends AbstractMSSQLServerJDBC {

    private static final Logger log = Logger.getLogger(MarketingPriceListMarchJDBC.class.getName());

    public List<ScaleItemPreliminary> getGetScaleItemListByItemId(int itemId, int itemGrade) {

        String query = "SELECT DISTINCT rzm AS ITEM_SIZE, cno AS ITEM_PRICE FROM nsi_sd WHERE srt=? AND  kod = ? ORDER BY rzm";
        ArrayList<ScaleItemPreliminary> result = new ArrayList<>();
        try (PreparedStatement ps = getConnection().prepareStatement(query)) {
            ps.setInt(1, itemGrade);
            ps.setInt(2, itemId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                result.add(new ScaleItemPreliminary(rs.getInt("ITEM_SIZE"), rs.getDouble("ITEM_PRICE")));
            }
            rs.close();
        } catch (Exception ex) {
            log.severe("Ошибка при выполнении функции getGetScaleItemListByItemId :" + ex.getMessage());
        }
        return result;
    }

    public boolean saveScaleItem(final MarketingPriceListItem item, final List<ScaleItem> list) {
        String queryString = "INSERT INTO MARKETING_PRICE_LIST_DETAIL " +
                "(REF_DOCUMENT_ID, ITEM_ID, SIZE_RANGE_VALUE, PRIME_COST_VALUE, PROFITABILITY_VALUE, EFFECTIVE_PRICE_VALUE) " +
                " VALUES(?, ?, ?, ?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(queryString)) {
            conn.setAutoCommit(false);

            for (ScaleItem scaleItem : list) {
                if (scaleItem.isSaveable()) {
                    ps.setInt(1, item.getId());
                    ps.setInt(2, scaleItem.getId());
                    ps.setString(3, scaleItem.getSizeScale());
                    ps.setDouble(4, scaleItem.getPrimeCost());
                    ps.setDouble(5, scaleItem.getProfitability());
                    ps.setDouble(6, scaleItem.getPrice());
                    ps.addBatch();
                }
            }
            ps.executeBatch();
            conn.commit();
        } catch (Exception e) {
            log.severe("Ошибка при заполнении saveScaleItem" + e.getMessage());
            return false;
        }
        return true;
    }

    public boolean updatePriceList(final List<Object> data) {
        String queryString = "UPDATE MARKETING_PRICE_LIST_DETAIL SET " +
                "PRIME_COST_VALUE =?,PROFITABILITY_VALUE=?, EFFECTIVE_PRICE_VALUE=?, SUGGESTED_PRICE_VALUE=?, CHANGE_PERCENT_VALUE=? " +
                "WHERE ID=? ";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(queryString)) {
            conn.setAutoCommit(false);

            for (Object o : data) {
                ViewMarketingPriceListDetailItem item = (ViewMarketingPriceListDetailItem) o;
                if (item != null) {
                    if (item.isChanged()) {
                        ps.setDouble(1, item.getPrimeCostValue());
                        ps.setDouble(2, item.getProfitabilityValue());
                        ps.setDouble(3, item.getEffectivePriceValue());

                        ps.setDouble(4, item.getSuggestedPriceValue());
                        ps.setDouble(5, item.getChangePercentValue());
                        ps.setInt(6, item.getId());

                        ps.addBatch();
                    }
                }
            }

            ps.executeBatch();
            conn.commit();
        } catch (Exception e) {
            log.severe("Ошибка при заполнении updatePriceList" + e.getMessage());
            return false;
        }
        return true;
    }

    public boolean deletePriceListDetailItemById(int itemId) {
        String query = "DELETE FROM MARKETING_PRICE_LIST_DETAIL WHERE ID=?";

        try (PreparedStatement ps = getConnection().prepareStatement(query)) {
            ps.setInt(1, itemId);
            ps.executeUpdate();
            return true;
        } catch (Exception ex) {
            log.severe("Ошибка при выполнении функции deletePriceListDetailItemById :" + ex.getMessage());
            return false;
        }
    }

    public void priceListDetailProcessing(final RemainPriceListItem priceList, final List<RemainPriceListDetailItem> detailList) {
        String queryPriceList = "insert into REMAINS_PRICELIST_DETAIL (REF_REMAINS_PRICELIST, " +
                "ITEM_ARTICLE_NUMBER," +
                "ITEM_SIZE," +
                "PRICE_1ST_GRADE," +
                "PRICE_2ND_GRADE," +
                "VAT_VALUE" +
                ")" +
                " values(?,?,?,?,?,?)";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(queryPriceList);) {
            conn.setAutoCommit(false);
            for (RemainPriceListDetailItem item : detailList) {
                ps.setInt(1, priceList.getId());
                ps.setString(2, item.getArticleNumber());
                ps.setString(3, item.getItemSize());
                ps.setDouble(4, item.getPrice1stGrade());
                ps.setDouble(5, item.getPrice2ndGrade());
                ps.setDouble(6, item.getVatValue());
                ps.addBatch();
            }

            ps.executeBatch();
            conn.commit();
        } catch (Exception e) {
            log.severe("Ошибка при заполнении priceListDetailProcessing" + e.getMessage());
        }
    }

    public ProductionItemBase getPriceItemByArticleNumberAndSizeAndGrade(String articleNumber, int itemSize, int itemGrade) {
        String query = "SELECT dbo.nsi_kld.fas,dbo.nsi_kld.sar,dbo.nsi_kld.nar, dbo.nsi_sd.cno,dbo.nsi_sd.nds " +
                "FROM dbo.nsi_sd " +
                "INNER JOIN dbo.nsi_kld ON dbo.nsi_sd.kod = dbo.nsi_kld.kod " +
                "WHERE dbo.nsi_kld.nar = ? and dbo.nsi_sd.rzm = ? and dbo.nsi_sd.srt=? ";
        try (PreparedStatement ps = getConnection().prepareStatement(query)) {
            ps.setString(1, articleNumber);
            ps.setInt(3, itemGrade);
            ps.setInt(2, itemSize);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                ProductionItemBase item = new ProductionItemBase();

                item.setModelNumber(rs.getString("fas"));
                item.setArticleNumber(rs.getString("nar"));
                item.setArticleCode(rs.getString("sar"));

                item.setWholesalePrice(rs.getDouble("cno"));
                item.setVat(rs.getDouble("nds"));

                return item;
            }
        } catch (Exception ex) {
            log.severe("Ошибка при выполнении функции getGetScaleItemListByItemId :" + ex);
        }
        return null;
    }

    public List<ChartItem> getSaleListByItemId(int itemId) {
        List<ChartItem> result = new ArrayList<>();
        String sqlDoc = "SELECT " +
                "sum(dbo.otgruz2.kol * dbo.otgruz2.kol_in_upack)as amount, " +
                "dateadd(DAY,0, datediff(day,0, otgruz2.time_ins))as date " +
                "FROM dbo.otgruz2 " +
                "INNER JOIN dbo.nsi_sd ON dbo.otgruz2.kod_izd = dbo.nsi_sd.kod1 " +
                "INNER JOIN dbo.nsi_kld ON dbo.nsi_sd.kod = dbo.nsi_kld.kod " +
                "INNER JOIN dbo.otgruz1 ON dbo.otgruz2.doc_id = otgruz1.item_id " +
                "where nsi_kld.sar = ? and(time_ins>='2016-01-01') and otgruz1.status = 0 and (operac = 'Отгрузка покупателю' or operac='Перемещение в розницу')" +
                "GROUP BY dateadd(DAY,0, datediff(day,0, otgruz2.time_ins)) " +
                "ORDER BY date";
        try (PreparedStatement ps = getConnection().prepareStatement(sqlDoc)) {
            ps.setInt(1, itemId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ChartItem item = new ChartItem();
                item.setAmount(rs.getInt("amount"));
                item.setDate(rs.getString("date"));
                result.add(item);
            }
            rs.close();
        } catch (Exception ex) {
            log.severe("Ошибка в методе getSaleListByItemId:" + ex.getMessage());
        }

        return result;
    }

    public List<ChartItem> getIncomeListByItemId(final int itemId) {
        List<ChartItem> result = new ArrayList<>();

        String sqlDoc = "SELECT " +
                "sum(dbo.vnperem2.kol * dbo.vnperem2.kol_in_upack)as amount, " +
                "dateadd(DAY,0, datediff(day,0, vnperem2.time_ins))as date " +
                "FROM dbo.vnperem2 " +
                "INNER JOIN dbo.nsi_sd ON dbo.vnperem2.kod_izd = dbo.nsi_sd.kod1 " +
                "INNER JOIN dbo.nsi_kld ON dbo.nsi_sd.kod = dbo.nsi_kld.kod " +
                "INNER JOIN dbo.vnperem1 ON dbo.vnperem2.doc_id = vnperem1.item_id " +
                "where nsi_kld.sar = ? and(time_ins>='2016-01-01') and vnperem1.kpodot = 138 and kpodto = 737 and status < 3 " +
                "GROUP BY dateadd(DAY,0, datediff(day,0, vnperem2.time_ins)) " +
                "ORDER BY date";

        try (PreparedStatement ps = getConnection().prepareStatement(sqlDoc)) {
            ps.setInt(1, itemId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ChartItem item = new ChartItem();
                item.setAmount(rs.getInt("amount"));
                item.setDate(rs.getString("date"));
                result.add(item);
            }
            rs.close();
        } catch (Exception ex) {
            log.severe("Ошибка при выполнении метода getIncomeListByItemId:" + ex.getMessage());
        }

        return result;
    }

    public void insertPriceList(RemainPriceListItem priceList) {
        String queryPriceList = "insert into REMAINS_PRICELIST (DOCUMENT_NUMBER, DOCUMENT_DATE,TRADE_ALLOWANCE_VALUE)" +
                " values(?,?,?)";

        try (PreparedStatement ps = getConnection().prepareStatement(queryPriceList)) {
            ps.setString(1, priceList.getDocumentNumber());
            ps.setDate(2, DateUtils.getDateAsSQLDate(priceList.getDocumentDate()));
            ps.setDouble(3, priceList.getTradeAllowanceValue());
            ps.executeUpdate();
        } catch (Exception e) {
            log.severe("Ошибка при выполнении метода insertPriceList" + e.getMessage());
        }

    }

    public void updatePriceList(RemainPriceListItem priceList) {
        String queryPriceList = "update REMAINS_PRICELIST SET DOCUMENT_NUMBER = ?, " +
                "DOCUMENT_DATE = ?, " +
                "TRADE_ALLOWANCE_VALUE = ? " +
                "WHERE ID = ? ";

        try (PreparedStatement ps = getConnection().prepareStatement(queryPriceList)) {
            ps.setString(1, priceList.getDocumentNumber());
            ps.setDate(2, DateUtils.getDateAsSQLDate(priceList.getDocumentDate()));
            ps.setDouble(3, priceList.getTradeAllowanceValue());
            ps.setInt(4, priceList.getId());
            ps.executeUpdate();
        } catch (Exception e) {
            log.severe("Ошибка при выполнении метода updatePriceList" + e.getMessage());
        }

    }

    public void deletePriceList(RemainPriceListItem item) {
        String query = "DELETE FROM REMAINS_PRICELIST WHERE ID=?";

        try (PreparedStatement ps = getConnection().prepareStatement(query)) {
            ps.setInt(1, item.getId());
            ps.executeUpdate();
        } catch (Exception ex) {
            log.severe("Ошибка при выполнении функции deletePriceList :" + ex.getMessage());
        }
    }
}

