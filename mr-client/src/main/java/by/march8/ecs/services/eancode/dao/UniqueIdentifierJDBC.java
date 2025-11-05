package by.march8.ecs.services.eancode.dao;

import by.gomel.freedev.ucframework.ucdao.jdbc.AbstractMSSQLServerJDBC;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.logging.Logger;

/**
 * @author Andy 03.11.2018 - 11:23.
 */
public class UniqueIdentifierJDBC extends AbstractMSSQLServerJDBC {

    private static final Logger log = Logger.getLogger(UniqueIdentifierJDBC.class.getName());

    public long getNextUniqueIdentifier(String identifier) {
        String sql = "{call punictabl (?, ?) }";
        String sqlSelect = "SELECT NOMER from unictabl WHERE NAME= ? ";
        long result = 0;
        try (Connection conn = getConnection();
             CallableStatement cs = conn.prepareCall(sql);
             PreparedStatement ps = conn.prepareStatement(sqlSelect);) {
            conn.setAutoCommit(false);
            cs.setString(1, identifier);
            cs.setInt(2, 1);
            cs.execute();

            ps.setString(1, identifier);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                result = rs.getLong(1);
            }

            conn.commit();
        } catch (Exception e) {
            log.severe("Ошибка метода \"getNextUniqueIdentifier()\" для таблицы " + identifier);
        }

        return result;
    }

}
