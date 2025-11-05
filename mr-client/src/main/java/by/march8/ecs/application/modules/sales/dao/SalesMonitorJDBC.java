package by.march8.ecs.application.modules.sales.dao;

import by.gomel.freedev.ucframework.ucdao.jdbc.AbstractMSSQLServerJDBC;
import by.march8.api.utils.DatePeriod;
import by.march8.api.utils.DateUtils;
import by.march8.entities.sales.SalesDocumentSimple;
import by.march8.entities.sales.SalesMonitorItemEntity;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

public class SalesMonitorJDBC extends AbstractMSSQLServerJDBC {

    private static final Logger log = Logger.getLogger(SalesMonitorJDBC.class.getName());

    public List<SalesMonitorItemEntity> getSalesByPeriodAndContractorAndModel(DatePeriod period, int contractor, int modelNumber) {

        String query = "SELECT " +
                "ABS(CHECKSUM(NEWID())) AS ID, " +
                "dbo.otgruz2.rzm_marh AS [SIZE], " +
                "dbo.otgruz2.rst_marh AS GROWTH, " +
                "sum(dbo.otgruz2.kol * dbo.otgruz2.kol_in_upack) AS AMOUNT " +
                "FROM " +
                "dbo.otgruz1 " +
                "INNER JOIN dbo.otgruz2 ON dbo.otgruz1.item_id = dbo.otgruz2.doc_id " +
                "INNER JOIN dbo.nsi_sd ON dbo.otgruz2.kod_izd = dbo.nsi_sd.kod1 " +
                "INNER JOIN dbo.nsi_kld ON dbo.nsi_sd.kod = dbo.nsi_kld.kod " +
                "INNER JOIN dbo.DRIVING_DIRECTION_DOCUMENT ON dbo.otgruz1.item_id = dbo.DRIVING_DIRECTION_DOCUMENT.SALE_DOCUMENT_ID " +
                "WHERE " +
                "dbo.otgruz1.kpl = ? AND " +
                "dbo.nsi_kld.fas = ? AND " +
                "dbo.otgruz1.status = 0 AND " +
                "(dbo.otgruz1.operac = 'Перемещение в розницу' OR " +
                "dbo.otgruz1.operac = 'Отгрузка покупателю') and (DOCUMENT_DATE BETWEEN ? and ?) " +
                "GROUP BY " +
                "dbo.otgruz2.rzm_marh, " +
                "dbo.otgruz2.rst_marh " +
                "ORDER BY " +
                "dbo.otgruz2.rzm_marh, " +
                "dbo.otgruz2.rst_marh ";

        List<SalesMonitorItemEntity> result = new ArrayList<>();
        try (PreparedStatement ps = getConnection().prepareStatement(query)) {
            ps.setInt(1, contractor);
            ps.setInt(2, modelNumber);
            ps.setDate(3, DateUtils.getDateAsSQLDate(period.getBegin()));
            ps.setDate(4, DateUtils.getDateAsSQLDate(period.getEnd()));
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                SalesMonitorItemEntity item = new SalesMonitorItemEntity();
                item.setId(rs.getInt("ID"));
                item.setSize(rs.getInt("SIZE"));
                item.setGrowth(rs.getInt("GROWTH"));
                item.setAmount(rs.getInt("AMOUNT"));
                result.add(item);
            }
            return result;
        } catch (Exception ex) {
            log.severe("Ошибка при выполнении функции getSalesByPeriodAndContractorAndModel: " + ex.getMessage());
        }
        return Collections.emptyList();
    }

    public List<SalesDocumentSimple> getSalesDocumentSimpleForContractor(int code) {
        return null;
    }
}
