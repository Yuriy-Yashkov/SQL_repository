package by.march8.tasks.remains.dao;

import by.gomel.freedev.ucframework.ucdao.jdbc.AbstractMSSQLServerJDBC;
import by.march8.tasks.remains.logic.RemainsDBFItem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.logging.Logger;

/**
 * @author Andy 14.11.2016.
 */
public class RemainsJDBC extends AbstractMSSQLServerJDBC {

    private static final Logger log = Logger.getLogger(RemainsJDBC.class.getName());

    String sqlStep1 = "UPDATE ERP_REMAINS " +
            "SET SD_ID = (" +
            "SELECT TOP 1 ITEM_CODE " +
            "FROM NSI_EANCODE " +
            "WHERE NSI_EANCODE.EANCODE = ERP_REMAINS.EANCOD)";

    String sqlStep2 = "UPDATE ERP_REMAINS " +
            "SET CATEGORY = ( " +
            "SELECT TOP 1 SUBSTRING(CONVERT(varchar(10), dbo.nsi_kld.sar),1,3) AS CATEGORY " +
            "FROM nsi_sd " +
            "INNER JOIN nsi_kld ON nsi_kld.kod = nsi_sd.kod " +
            "WHERE nsi_sd.kod1 = ERP_REMAINS.SD_ID )";

    String sqlStep3 = "UPDATE ERP_REMAINS " +
            "SET NSI_COLOR = ( " +
            "SELECT TOP 1 dbo.REFERENCE_COLOR.NAME " +
            "FROM NSI_EANCODE " +
            "INNER JOIN dbo.REFERENCE_COLOR ON dbo.REFERENCE_COLOR.ID = dbo.NSI_EANCODE.REF_COLOR_ID " +
            "WHERE NSI_EANCODE.EANCODE = ERP_REMAINS.EANCOD )";

    String sqlDeleteAll = "DELETE FROM ERP_REMAINS";

    public boolean prepareRemainsSQL() {
        executeSQL(sqlStep1);
        executeSQL(sqlStep2);
        executeSQL(sqlStep3);
        return true;
    }

    public void dropERPRemainsTable() {
        executeSQL(sqlDeleteAll);
    }

    private void executeSQL(String varSQL) {
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(varSQL)) {
            conn.setAutoCommit(false);
            ps.executeUpdate();
            conn.commit();
        } catch (Exception e) {
            log.severe("Ошибка выполнения запроса " + varSQL);
        }
    }

    public void remainsImport(List<RemainsDBFItem> remainsDBFItems) {
        String insertInto = "INSERT INTO ERP_REMAINS (MODEL, ARTICLE, EANCOD, BALANCE ) values(?, ?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(insertInto)) {
            conn.setAutoCommit(false);
            for (RemainsDBFItem item : remainsDBFItems) {
                ps.setInt(1, item.getModelNumber());
                ps.setString(2, item.getArticleNumber());
                ps.setString(3, item.getEanCode());
                ps.setInt(4, item.getAmount());
                ps.addBatch();
            }
            ps.executeBatch();
            conn.commit();
        } catch (Exception e) {
            log.severe("Ошибка выполнения запроса " + insertInto);
        }
    }
}
