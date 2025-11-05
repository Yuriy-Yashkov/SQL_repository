package dept.otk;

import by.gomel.freedev.ucframework.ucdao.jdbc.AbstractMSSQLServerJDBC;
import lombok.SneakyThrows;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import java.util.logging.Logger;

import static common.UtilFunctions.convertDateStrToLong;

/**
 *
 * @author vova
 */
public class OtkDB extends AbstractMSSQLServerJDBC {
    private static final Logger log = Logger.getLogger(OtkDB.class.getName());

    @SneakyThrows
    public Vector getMarhList(String sd, String ed) {
        long sDate = convertDateStrToLong(sd);
        long eDate = convertDateStrToLong(ed);
        Vector items = new Vector();
        int i = 0;

        String query = "select nomer, t1.naim, shifrp, fas, t2.naim, kol, data, t1.kod_marh from (select nomer, kod_marh, kol,data, naim from v_nakl_sdacha where data >= ? and data < ?)as t1 " +
                " join (select naim, fas, kod_marh from v_nakl_sdacha1 group by naim, fas, kod_marh)as t2 on t2.kod_marh = t1.kod_marh " +
                " join (select kroy, kod from marh_list)as t3 on t3.kod = t1.kod_marh " +
                " join (select shifrp, kod, brigada from kroy)as t4 on t4.kod = t3.kroy";
        try (PreparedStatement ps = getConnection().prepareStatement(query)) {
            ps.setDate(1, new java.sql.Date(sDate));
            ps.setDate(2, new java.sql.Date(eDate + DAY));
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Vector user = new Vector();
                user.add(++i);
                user.add(rs.getString(1).trim());
                user.add(rs.getString(2).trim());
                user.add(rs.getString(3).trim());
                user.add(rs.getString(4).trim());
                user.add(rs.getString(5).trim());
                user.add(rs.getLong(6));
                user.add(rs.getString(7).trim());
                user.add(rs.getString(8).trim());
                items.add(user);
            }
        } catch (Exception e) {
            log.severe("Ошибка getMarhList(String sd, String ed) " + e);
        }
        return items;
    }

    @SneakyThrows
    public Vector getMarhListDetails(int kod_marh, String sd, String ed) {
        Vector items = new Vector();
        int i = 0;
        long sDate = convertDateStrToLong(sd);
        long eDate = convertDateStrToLong(ed);

        String query = "select fas, nar, naim, ncw, rzm, srt, sum(kol) from v_nakl_sdacha1 where data >= ? and data < ? and kod_marh = ? " +
                " group by fas, nar, naim, ncw, rzm, srt";

        String query1 = "select fas, nar, ngpr, ncw, rzm, srt, sum(kol) from (select kod_izd, (kol* kol_in_upack)as kol, ncw, srt from vnperem2 where kod_marh = ? and srt > 2) as vnp2 " +
                " join (select rzm_print as rzm, kod, kod1 from nsi_sd)as sd on sd.kod1 = vnp2.kod_izd " +
                " join (select nar, kod, fas, ngpr from nsi_kld) as kld on kld.kod = sd.kod " +
                " group by fas, nar, ngpr, ncw, rzm, srt";

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(query);
             PreparedStatement ps1 = con.prepareStatement(query1);) {
            ps.setDate(1, new java.sql.Date(sDate));
            ps.setDate(2, new java.sql.Date(eDate + DAY));
            ps.setInt(3, kod_marh);
            getEntities(ps, i, items);

            ps1.setInt(1, kod_marh);
            getEntities(ps1, i, items);
        } catch (Exception e) {
            log.severe("Ошибка getMarhListDetails(int kod_marh, String sd, String ed) " + e.getMessage());
        }
        return items;
    }

    private static void getEntities(PreparedStatement ps, int i, Vector items) throws SQLException {
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            Vector user = new Vector();
            user.add(++i);
            user.add(rs.getString(1).trim());
            user.add(rs.getString(2).trim());
            user.add(rs.getString(3).trim());
            user.add(rs.getString(4).trim());
            user.add(rs.getString(5).trim());
            user.add(rs.getInt(6));
            user.add(rs.getLong(7));
            items.add(user);
        }
    }

    public Vector getMarhListDetailsPlan(int kod_marh) {
        Vector items = new Vector();
        int i = 0;

        String query = "select fas, ngpr, rzm_print, kol from (select kod_kroy, kol from marh_list1 where kod_marh = ?) as ml1 " +
                " join (select kod_izd, kod_str, rst, rzm from kroy1) as k on k.kod_str = ml1.kod_kroy " +
                " join (select srt, rzm_print, rst, rzm, kod from nsi_sd) as sd on sd.kod = k.kod_izd and sd.rzm = k.rzm and sd.rst = k.rst and srt = 1 " +
                " join (select fas, ngpr, kod, nar from nsi_kld) as kld on kld.kod = k.kod_izd ";
        try (PreparedStatement ps = getConnection().prepareStatement(query)) {
            ps.setInt(1, kod_marh);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Vector user = new Vector();
                user.add(++i);
                user.add(rs.getString(1).trim());
                user.add(rs.getString(2).trim());
                user.add(rs.getString(3).trim());
                user.add(rs.getInt(4));
                items.add(user);
            }
        } catch (Exception e) {
            log.severe("Ошибка getMarhListDetailsPlan(int kod_marh) " + e.getMessage());
        }
        return items;
    }
}
