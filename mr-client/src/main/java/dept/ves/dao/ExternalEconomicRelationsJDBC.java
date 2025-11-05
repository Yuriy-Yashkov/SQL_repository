package dept.ves.dao;

import by.gomel.freedev.ucframework.ucdao.jdbc.AbstractMSSQLServerJDBC;
import by.march8.api.utils.DatePeriod;
import by.march8.api.utils.DateUtils;
import dept.ves.model.DiscountAnalyser;
import dept.ves.model.VolumeOfPurchases;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * @author Developer on 02.03.2020 7:18
 */
public class ExternalEconomicRelationsJDBC extends AbstractMSSQLServerJDBC {

    private static final Logger log = Logger.getLogger(ExternalEconomicRelationsJDBC.class.getName());

    private static void getVolumeOfPurchases(PreparedStatement ps, List<VolumeOfPurchases> result) throws SQLException {
        try (ResultSet rs = ps.executeQuery()) {
            int counter = 1;
            while (rs.next()) {
                VolumeOfPurchases item = new VolumeOfPurchases();
                item.setId(counter);
                item.setContractorName(rs.getString("NAIM"));
                item.setContractorCode(rs.getInt("kpl"));
                item.setSumPurchases(rs.getDouble("TOTAL_SUM"));
                item.setCurrencyId(rs.getInt("valuta_id"));
                item.setDiscountValue(DiscountAnalyser.getDiscount(item.getSumPurchases()));
                result.add(item);
                counter++;
            }
        } catch (SQLException ex) {
            log.severe("Error in method getVolumeOfPurchases :" + ex.getMessage());
        }
    }

    public List<VolumeOfPurchases> getVolumeOfPurchasesByFlexiblePeriod(int periodDecrement) {

        List<VolumeOfPurchases> result = new ArrayList<>();
        String sql = "SELECT s_klient.NAIM, kpl, SUM(summa_all)AS TOTAL_SUM, valuta_id FROM otgruz1 " +
                "LEFT JOIN s_klient ON s_klient.KOD = kpl " +
                "LEFT JOIN DRIVING_DIRECTION_DOCUMENT ON DRIVING_DIRECTION_DOCUMENT.SALE_DOCUMENT_ID = otgruz1.item_id " +
                "WHERE status = 0 and operac='Отгрузка покупателю' and export = 1 AND valuta_id = 2 AND DRIVING_DIRECTION_DOCUMENT.DOCUMENT_DATE BETWEEN GETDATE()-? AND GETDATE() " +
                "GROUP BY s_klient.NAIM,kpl, valuta_id " +
                "ORDER BY  valuta_id, s_klient.NAIM, kpl ";

        try (PreparedStatement ps = getConnection().prepareStatement(sql)) {
            ps.setInt(1, periodDecrement);
            getVolumeOfPurchases(ps, result);
        } catch (Exception ex) {
            log.severe("Ошибка при выполнении функции getVolumeOfPurchasesByFlexiblePeriod :" + ex.getMessage());
        }
        return result;
    }

    public List<VolumeOfPurchases> getVolumeOfPurchasesByPeriod(DatePeriod period) {

        List<VolumeOfPurchases> result = new ArrayList<>();
        String sql = "SELECT s_klient.NAIM, kpl, SUM(summa_all)AS TOTAL_SUM, valuta_id FROM otgruz1 " +
                "LEFT JOIN s_klient ON s_klient.KOD = kpl " +
                "LEFT JOIN DRIVING_DIRECTION_DOCUMENT ON DRIVING_DIRECTION_DOCUMENT.SALE_DOCUMENT_ID = otgruz1.item_id " +
                "WHERE status = 0 and operac='Отгрузка покупателю' and export = 1 AND valuta_id=2 AND DRIVING_DIRECTION_DOCUMENT.DOCUMENT_DATE BETWEEN ? AND ? " +
                "GROUP BY s_klient.NAIM,kpl, valuta_id " +
                "ORDER BY  valuta_id, s_klient.NAIM, kpl ";

        try (PreparedStatement ps = getConnection().prepareStatement(sql)) {
            ps.setDate(1, DateUtils.getDateAsSQLDate(period.getBegin()));
            ps.setDate(2, DateUtils.getDateAsSQLDate(period.getEnd()));
            getVolumeOfPurchases(ps, result);
        } catch (Exception ex) {
            log.severe("Ошибка при выполнении функции getVolumeOfPurchasesByFlexiblePeriod :" + ex.getMessage());
        }
        return result;
    }
}
