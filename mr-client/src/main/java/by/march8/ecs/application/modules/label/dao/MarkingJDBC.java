package by.march8.ecs.application.modules.label.dao;

import by.gomel.freedev.ucframework.ucdao.jdbc.AbstractMSSQLServerJDBC;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.logging.Logger;

/**
 * @author Developer on 13.01.2020 10:57
 */
public class MarkingJDBC extends AbstractMSSQLServerJDBC {

    private static final Logger log = Logger.getLogger(MarkingJDBC.class.getName());

    public String getBarCode128IfExist(int model, int size) {
        String query = "select * from BARCODE_CUSTOM WHERE MODEL=? AND SIZE = ? ";
        try (PreparedStatement ps = getConnection().prepareStatement(query)) {
            ps.setInt(1, model);
            ps.setInt(2, size);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("CODE_SEGMENT1")
                        + rs.getString("CODE_SEGMENT2")
                        + rs.getString("CODE_SEGMENT3");
            }
        } catch (Exception ex) {
            log.severe("Ошибка при выполнении функции getBarCode128IfExist :" + ex.getMessage());
        }
        return "0";
    }
}
