package by.march8.ecs.application.modules.warehouse.external.shipping.mode.dao;

import by.gomel.freedev.ucframework.ucdao.jdbc.AbstractMSSQLServerJDBC;
import by.march8.ecs.application.modules.warehouse.external.shipping.mode.model.InventoryReportBean;

import java.io.PipedReader;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

/**
 * @author Developer on 26.08.2020 10:00
 */
public class InventoryDB extends AbstractMSSQLServerJDBC {

    private static final Logger log = Logger.getLogger(InventoryDB.class.getName());

    public List<InventoryReportBean> getInventoryReportBeanList(String str) {

        List<InventoryReportBean> list = new ArrayList<>();
        String query = "select otgruz2.scan as eancode,nsi_sd.nds as nds,otgruz2.cena as cena,otgruz2.kol as kol,nsi_kld.nar as nar,nsi_kld.ngpr as ngpr," +
                "nsi_sd.srt as srt,nsi_sd.rzm_print as rzm_print \n" +
                "from otgruz2\n" +
                "INNER JOIN otgruz1 ON otgruz1.item_id=otgruz2.doc_id\n" +
                "INNER JOIN nsi_sd ON otgruz2.kod_izd=nsi_sd.kod1\n" +
                "INNER JOIN nsi_kld ON nsi_sd.kod=nsi_kld.kod\n" +
                "where otgruz2.doc_id=?";
        Integer docId = getDocIdFromDB(str);
        try (PreparedStatement ps = getConnection().prepareStatement(query)) {
            ps.setInt(1, docId);
            ResultSet rs = ps.executeQuery();
            int id = 1;
            while (rs.next()) {
                InventoryReportBean reportBean = new InventoryReportBean(
                        id++,
                        rs.getString("eancode"),
                        rs.getInt("nds"),
                        rs.getString("nar"),
                        rs.getString("ngpr"),
                        rs.getInt("srt"),
                        rs.getString("rzm_print"),
                        rs.getDouble("cena"),
                        rs.getInt("kol")
                );
                list.add(reportBean);
            }
            return list;
        } catch (NullPointerException ex) {
            log.severe("Нет такой накладной " + ex.getMessage());
        } catch (Exception ex) {
            log.severe("Ошибка при выполнении функции getCourseByOrderStr: " + ex.getMessage());
        }
        return Collections.emptyList();
    }

    private Integer getDocIdFromDB(String str) {
        String query = "select item_id from otgruz1 where ndoc=?";
        try (PreparedStatement ps = getConnection().prepareStatement(query)) {
            ps.setString(1, str);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("item_id");
            }
        } catch (Exception ex) {
            log.severe("Ошибка при выполнении функции getCourseByOrderStr: " + ex.getMessage());
        }
        return null;
    }
}
