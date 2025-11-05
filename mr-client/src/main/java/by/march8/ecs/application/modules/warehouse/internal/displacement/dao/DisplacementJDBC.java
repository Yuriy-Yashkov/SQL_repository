package by.march8.ecs.application.modules.warehouse.internal.displacement.dao;

import by.gomel.freedev.ucframework.ucdao.jdbc.AbstractMSSQLServerJDBC;
import by.march8.api.utils.DateUtils;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;
import java.util.logging.Logger;

/**
 * @author Developer on 28.02.2020 7:26
 */
public class DisplacementJDBC extends AbstractMSSQLServerJDBC {

    private static final Logger log = Logger.getLogger(DisplacementJDBC.class.getName());

    public Date getSaleDateForDocumentId(int documentId) {
        String sql = "SELECT date FROM vnperem1 WHERE item_id = ?";
        Date result = null;
        try (PreparedStatement ps = getConnection().prepareStatement(sql)) {
            ps.setInt(1, documentId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                result = rs.getDate("date");
            }
        } catch (Exception ex) {
            log.severe("Ошибка при выполнении функции getSaleDateForDocumentId :" + ex.getMessage());
        }

        return result;
    }

    public void updateSaleDateForDocumentId(int documentId, Date saleDate) {
        String sqlUpdate = "UPDATE vnperem1 SET date=? WHERE item_id=? ";
        try (PreparedStatement ps = getConnection().prepareStatement(sqlUpdate)) {
            ps.setDate(1, DateUtils.getDateAsSQLDate(saleDate));
            ps.setInt(2, documentId);
            ps.executeUpdate();
        } catch (Exception e) {
            log.severe("Ошибка метода updateSaleDateForDocumentId " + e.getMessage());
        }
    }
}

