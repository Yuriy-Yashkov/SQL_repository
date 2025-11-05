package by.march8.ecs.application.modules.production.dao;

import by.gomel.freedev.ucframework.ucdao.jdbc.AbstractMSSQLServerJDBC;
import by.march8.ecs.application.modules.production.model.RouteSheetBasic;

import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * @author Andy on 28.02.2020 8:34
 */
public class RouteSheetJDBC extends AbstractMSSQLServerJDBC {

    private static final Logger log = Logger.getLogger(RouteSheetJDBC.class.getName());

    public List<RouteSheetBasic> getRouteSheetsByNumber(double number) {
        List<RouteSheetBasic> result = new ArrayList<>();
        String sql = "SELECT kod, nomer, data, kol, kod_owner FROM marh_list WHERE nomer = ? ORDER BY data";

        try (PreparedStatement ps = getConnection().prepareStatement(sql)) {
            ps.setDouble(1, number);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                RouteSheetBasic item = new RouteSheetBasic();
                item.setId(rs.getInt("kod"));
                item.setDocumentNumber(rs.getString("nomer"));
                item.setDocumentDate(rs.getDate("data"));
                item.setAmount(rs.getInt("kol"));
                item.setDeptOwner(rs.getInt("kod_owner"));
                result.add(item);
            }
        } catch (Exception ex) {
            log.severe("Ошибка при выполнении функции getRouteSheetsByNumber :" + ex.getMessage());
        }
        return result;
    }

    public List<String> getDepartmentCodes() {
        List<String> result = new ArrayList<>();
        String sql = "SELECT kod1 FROM nsi_brig WHERE kod1 >= 100 ORDER BY kod1";

        try (PreparedStatement ps = getConnection().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                result.add(rs.getString(1).trim());
            }
        } catch (Exception ex) {
            log.severe("Ошибка при выполнении функции getDepartmentCodes :" + ex.getMessage());
        }
        return result;
    }

    public void changeDocumentOwner(RouteSheetBasic doc, int code) {
        String sqlUpdate = "UPDATE marh_list SET kod_owner=? WHERE kod=? ";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sqlUpdate)) {
            conn.setAutoCommit(true);
            ps.setInt(1, code);
            ps.setInt(2, doc.getId());
            ps.executeUpdate();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
            log.severe("Ошибка метода changeDocumentOwner " + e.getMessage());
        }
    }
}
