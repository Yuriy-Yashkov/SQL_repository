package by.march8.ecs.application.modules.terminal.dao;

import by.gomel.freedev.ucframework.ucdao.jdbc.AbstractMSSQLServerJDBC;
import by.march8.ecs.application.modules.terminal.model.BaseTerminalItem;

import javax.xml.transform.Result;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.List;
import java.util.logging.Logger;

/**
 * @author Andy 23.10.2018 - 7:14.
 */
public class TerminalJDBC extends AbstractMSSQLServerJDBC {

    private static final Logger log = Logger.getLogger(TerminalJDBC.class.getName());

    /**
     * Удаление данных терминала для определенного пользователя
     *
     * @param userName имя пользователя
     */
    public void deleteUserScanForUserName(String userName) {
        String sqlUpdate = "DELETE FROM USER_SCAN_EAN WHERE uservrkv=?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sqlUpdate)) {
            conn.setAutoCommit(false);
            ps.setString(1, userName);
            ps.executeUpdate();
            conn.commit();
        } catch (Exception e) {
            log.severe("Ошибка получение всех идентификаторов" + e.getMessage());
        }
    }

    /**
     * Возвращает количество добавлений данных с терминала по документу
     *
     * @param documentId идентификатор документа
     * @return количество добавлений с терминала в документ
     */
    public int getInsertCountByDocumentId(int documentId) {
        String query = "SELECT MAX(part) AS PARTMAX FROM otgruz2 WHERE doc_id=? ";

        try (PreparedStatement ps = getConnection().prepareStatement(query)) {
            ps.setInt(1, documentId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("PARTMAX");
            }
        } catch (Exception ex) {
            log.severe("Ошибка при выполнении функции getInsertCountByDocumentId: " + ex.getMessage());
        }
        return 0;
    }

    public void saveTerminalData(int documentId, List<BaseTerminalItem> itemList, String userName) {

        String sql = "INSERT INTO USER_SCAN_EAN SELECT ?, ?, ?, ?, ?, ?, ?, 0, 0, 0, ?, 0, 0, 0, \'\', 0, 0, \'\'";
        try (Connection conn = getConnection();
        PreparedStatement ps = conn.prepareStatement(sql)) {
            conn.setAutoCommit(false);
            for (BaseTerminalItem terminalItem : itemList) {
                ps.setLong(1, terminalItem.getBarCode());
                ps.setInt(2, terminalItem.getAmount());
                ps.setInt(3, terminalItem.getPack());
                ps.setTimestamp(4, new Timestamp(System.currentTimeMillis()));
                ps.setString(5, userName);
                ps.setString(6, userName);
                ps.setInt(7, documentId);
                ps.setString(8, "otgruz2");
                ps.addBatch();
            }
            ps.executeBatch();
            conn.commit();
        } catch (Exception e) {
            log.severe("Ошибка получение всех идентификаторов: " + e.getMessage());
        }
    }

    public void updateUserScan(String userName) {
        String sql = "{ call UPDATE_USER_SCAN_EAN (?) }";
        try (CallableStatement cs = getConnection().prepareCall(sql)) {
            cs.setString(1, userName);
            cs.execute();
        } catch (Exception e) {
            log.severe("Ошибка вызова процедуры UPDATE_USER_SCAN_EAN" + e.getMessage());
        }
    }

    public void saveUploadedData(String userName, int partCount) {
        String sql = "INSERT INTO otgruz2 SELECT scan, ?, kol, 0, 0, 0, 0, 0, 0, 0, 0, 0, kol_in_upack, tip, time_ins, pc_ins, eancode, "
                + "doc_id, kod_str, \'\', rzm_marh, rst_marh, kod_izd, ncw, srt, 0 FROM USER_SCAN_EAN WHERE USER_SCAN_EAN.uservrkv=? and USER_SCAN_EAN.kod_izd<>0";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            conn.setAutoCommit(false);
            ps.setInt(1, partCount);
            ps.setString(2, userName);
            ps.executeUpdate();
            conn.commit();
        } catch (Exception e) {
            log.severe("Ошибка получение всех идентификаторов " + e.getMessage());
        }
    }

    public void updateDocumentData(int documentId) {
        String sql = "{ call UPDATE_SHIPMENT_DOCUMENT (?) }";
        try (CallableStatement cs = getConnection().prepareCall(sql)) {
            cs.setInt(1, documentId);
            cs.execute();
        } catch (Exception e) {
            log.severe("Ошибка вызова процедуры updateDocumentData " + e.getMessage());
        }
    }
}
