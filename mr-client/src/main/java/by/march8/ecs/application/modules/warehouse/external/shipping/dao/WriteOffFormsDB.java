package by.march8.ecs.application.modules.warehouse.external.shipping.dao;

import by.gomel.freedev.ucframework.ucdao.jdbc.AbstractMSSQLServerJDBC;
import by.march8.api.utils.DateUtils;
import by.march8.ecs.application.modules.warehouse.external.shipping.model.WriteOffFormsItem;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

/**
 * @author Andy 07.04.2017.
 */
public class WriteOffFormsDB extends AbstractMSSQLServerJDBC {

    private static final Logger log = Logger.getLogger(WriteOffFormsDB.class.getName());

    public List<WriteOffFormsItem> getWriteOffFormsList(Date dateBegin, Date dateEnd) {
        log.info("Отбор данных для " + DateUtils.getNormalDateFormat(dateBegin) + " по " + DateUtils.getNormalDateFormat(dateEnd));
        String query = "select " +
                "driving.DOCUMENT_DATE, " +
                "otgruz.ndoc, " +
                "otgruz.item_id, " +
                "otgruz.summa_all," +
                "client.NAIM, " +
                "otgruz.export " +
                "from DRIVING_DIRECTION_DOCUMENT as driving " +
                "JOIN (select item_id, ndoc, status, operac, export, summa_all, kpl from otgruz1 )as otgruz on otgruz.item_id  = driving.SALE_DOCUMENT_ID \n" +
                "LEFT JOIN (select KOD, NAIM from s_klient )as client on otgruz.kpl  = client.KOD           \n" +
                "where ((driving.DOCUMENT_DATE >= CONVERT(DATETIME, ?, 102))and(driving.DOCUMENT_DATE <= CONVERT(DATETIME, ?, 102)))and(otgruz.status = 0)\n" +
                "and (LEN(otgruz.ndoc)>7)\n" +
                "ORDER BY otgruz.ndoc";

        List<WriteOffFormsItem> result = new ArrayList<>();
        try (PreparedStatement ps = getConnection().prepareStatement(query)) {
            ps.setDate(1, DateUtils.getDateAsSQLDate(dateBegin));
            ps.setDate(2, DateUtils.getDateAsSQLDate(dateEnd));
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                WriteOffFormsItem item = new WriteOffFormsItem();
                item.setId(rs.getInt("item_id"));
                item.setFullNumber(rs.getString("ndoc"));
                item.setDate(rs.getDate("DOCUMENT_DATE"));
                item.setSumm(rs.getDouble("summa_all"));

                item.setClientName(rs.getString("NAIM"));
                item.setExport(rs.getInt("export"));

                result.add(item);
            }
            return result;
        } catch (Exception ex) {
            log.severe("Ошибка при выполнении функции getWriteOffFormsList :" + ex.getMessage());
        }
        return null;
    }
}
