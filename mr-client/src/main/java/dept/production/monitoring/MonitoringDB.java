package dept.production.monitoring;

import by.gomel.freedev.ucframework.ucdao.jdbc.AbstractMSSQLServerJDBC;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class MonitoringDB extends AbstractMSSQLServerJDBC {

    private static final Logger log = Logger.getLogger(MonitoringDB.class.getName());

    public Object[][] getClient(long date) {
        List<Object[]> items = new ArrayList<>();
        int i = 0;
        date = date - DAY * 45;
        String query = "select KOD, NAIM from s_klient where KOD in (select distinct(kod_pol) from kroy where kod_pol <> 0 and (data >= ? or kod in (select kroy from marh_list where kod_owner <> 555 and kpodkrkv <> 138))) order by KOD";
        try (PreparedStatement ps = getConnection().prepareStatement(query)) {
            ps.setDate(1, new java.sql.Date(date));
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                items.add(new Object[]{rs.getInt(1), rs.getString(2).trim()});
            }
        } catch (Exception e) {
            log.severe("Ошибка getClient() " + e);
        }
        Object[][] r = new Object[items.size()][2];
        for (Object[] o : items) {
            r[i] = o;
            i++;
        }
        return r;
    }

    public Object[][] getModelClient(int kod_client, long sDate, long eDate) {
        List<Object[]> items = new ArrayList<>();
        int i = 0;

        String query = "SELECT fas, nar, ngpr, sum(v_marh_list1.kol-ISNULL(poshiv.kol_sdano,0)) AS kol, nsi_brig.kod1 as kod_owner, nsi_brig.naim as naim FROM " +
                " (select fas, nar, ngpr,kol,kod_marh, item_id from v_marh_list1 where kod_pol = ?) as v_marh_list1 " +
                " INNER JOIN marh_list ON v_marh_list1.kod_marh = marh_list.kod  " +
                " INNER JOIN nsi_brig ON kod_owner = nsi_brig.kod1 LEFT OUTER JOIN (SELECT TOP 100 PERCENT  " +
                " marh1_id,SUM(kol_sdano+sdacha) AS kol_sdano FROM (SELECT marh1_id,kol_sdano,case when  " +
                " pdrto=888 then sdacha else 0 end as sdacha from poshiv) A GROUP BY marh1_id ORDER BY marh1_id) poshiv  " +
                " ON v_marh_list1.item_id=poshiv.marh1_id WHERE kod_owner<500  GROUP BY fas,nar,ngpr,nsi_brig.kod1,nsi_brig.naim " +
                " HAVING SUM(v_marh_list1.kol-ISNULL(poshiv.kol_sdano,0)) <> 0 ORDER BY fas";
        try (PreparedStatement ps = getConnection().prepareStatement(query)) {
            ps.setInt(1, kod_client);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                items.add(new Object[]{rs.getInt(1), rs.getString(2).trim(), rs.getString(3).trim(), rs.getInt(4), rs.getInt(5), rs.getString(6).trim()});
            }
        } catch (Exception e) {
            log.severe("Ошибка getModelClient(int kod_client) при расчёте бригад" + e);
        }

        String secondQuery = "select fas, nar, ngpr, sum(skol), 737, 'СКЛАД' from (select (kol*kol_in_upack)as skol, srt, kod_izd, ncw as color, kod_marh from vnperem2 where kod_marh <> 0 and doc_id in (select item_id from vnperem1 where status = 0 and kpodot = 138 and kpodto = 737 and date >= ? and date < ?))as t1   " +
                " left join (select kod, kod1 from nsi_sd)as t2 on t1.kod_izd = t2.kod1  " +
                " join (select ngpr, sar, nar, fas, kod from nsi_kld)as t3 on t2.kod = t3.kod  " +
                " join (select kod, kroy from marh_list)as ml on ml.kod = t1.kod_marh " +
                " join (select kod, kod_pol from kroy where kod_pol <> 0)as kroy on kroy.kod = ml.kroy and kroy.kod_pol = ? " +
                " group by fas, nar, ngpr ";
        try (PreparedStatement ps = getConnection().prepareStatement(secondQuery)) {
            ps.setDate(1, new java.sql.Date(sDate));
            ps.setDate(2, new java.sql.Date(eDate + DAY));
            ps.setInt(3, kod_client);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                items.add(new Object[]{rs.getInt(1), rs.getString(2).trim(), rs.getString(3).trim(), rs.getInt(4), rs.getInt(5), rs.getString(6).trim()});
            }
        } catch (Exception e) {
            log.severe("Ошибка getModelClient(int kod_client) расчёте склада" + e);
        }
        Object[][] r = new Object[items.size()][6];
        for (Object[] o : items) {
            r[i] = o;
            i++;
        }
        return r;
    }

    public Object[][] getDescrModelClient(int kod_client, int fas, int kod_brig, long sDate, long eDate) {
        ArrayList<Object[]> items = new ArrayList();
        int i = 0;

        if (kod_brig == 737) {
            String query = "select fas, nar, ngpr, rst, rzm, ncw, sum(skol)  from (select (kol*kol_in_upack)as skol," +
                    " srt, kod_izd, ncw, kod_marh from vnperem2 where kod_marh <> 0 and doc_id in (select item_id " +
                    "from vnperem1 " +
                    "where status = 0 and kpodot = 138 and kpodto = 737 and date >= ? and date < ?))as t1   " +
                    " join (select kod, kod1, rst, rzm from nsi_sd)as t2 on t1.kod_izd = t2.kod1  " +
                    " join (select ngpr, sar, nar, fas, kod from nsi_kld where fas = ?)as t3 on t2.kod = t3.kod  " +
                    " join (select kod, kroy from marh_list)as ml on ml.kod = t1.kod_marh " +
                    " join (select kod, kod_pol from kroy where kod_pol <> 0)as kroy on kroy.kod = ml.kroy and kroy.kod_pol = ? " +
                    " group by fas, nar, ngpr, rst, rzm, ncw";
            try (PreparedStatement ps = getConnection().prepareStatement(query)) {
                ps.setDate(1, new java.sql.Date(sDate));
                ps.setDate(2, new java.sql.Date(eDate + DAY));
                ps.setInt(3, fas);
                ps.setInt(4, kod_client);

                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    items.add(new Object[]{rs.getInt(1), rs.getString(2).trim(), rs.getString(3).trim(), rs.getInt(4), rs.getInt(5), rs.getString(6).trim(), rs.getInt(7)});
                }
            } catch (Exception e) {
                log.severe("Ошибка getDiscrModelClient(int kod_client, int fas, int kod_brig) where kod_brig = 737 " + e);
            }
        } else if (kod_brig == 138) {
            String query = "select ttt1.fas, ttt1.nar, ttt1.naim, ttt1.rst_marh,  ttt1.rzm_marh, ttt1.ncw, case when ttt2.ewq is Null then ttt1.qwe else ttt1.qwe - ttt2.ewq end from ( " +
                    " select fas, nar, naim, rst_marh, rzm_marh, ncw, sum(t1.kol) as qwe from (select barcode, naim, kol, fas, nar,  kod_izd, rzm, kod_marh, ncw from v_nakl_sdacha1 where  data >= ? and data < ? and fas = ?) as t1   " +
                    "                         join (select kod, kroy from marh_list)as ml on ml.kod = t1.kod_marh  " +
                    "                         join (select kod, kod_pol from kroy where kod_pol <> 0)as kroy on kroy.kod = ml.kroy and kroy.kod_pol = ?  " +
                    "                         join (select barcode, rst_marh, rzm_marh from label_one)as lo on lo.barcode = t1.barcode  " +
                    " group by fas, nar, naim, rst_marh, rzm_marh, ncw   " +
                    " ) as ttt1  " +
                    " left join   " +
                    " (  " +
                    " select fas, nar, ngpr, rst, rzm, ncw, sum(skol) as ewq from (select (kol*kol_in_upack)as skol, srt, kod_izd, ncw, kod_marh from vnperem2 where kod_marh <> 0 and doc_id in (select item_id from vnperem1 where status = 0 and kpodot = 138 and kpodto = 737 and date >= ? and date < ?))as t1     " +
                    "                                          join (select kod, kod1, rst, rzm from nsi_sd)as t2 on t1.kod_izd = t2.kod1    " +
                    "                                          join (select ngpr, sar, nar, fas, kod from nsi_kld where fas = ?)as t3 on t2.kod = t3.kod    " +
                    "                                          join (select kod, kroy from marh_list)as ml on ml.kod = t1.kod_marh   " +
                    "                                          join (select kod, kod_pol from kroy where kod_pol <> 0)as kroy on kroy.kod = ml.kroy and kroy.kod_pol = ?  " +
                    "                        group by fas, nar, ngpr, rst, rzm, ncw   " +
                    " )as ttt2 on ttt1.naim = ttt2.ngpr and ttt1.fas= ttt2.fas and ttt1.nar = ttt2.nar and ttt1.rst_marh = ttt2.rst and ttt1.rzm_marh = ttt2.rzm and ttt1.ncw = ttt2.ncw";
            try (PreparedStatement ps = getConnection().prepareStatement(query)) {
                ps.setDate(1, new java.sql.Date(sDate));
                ps.setDate(2, new java.sql.Date(eDate + DAY));
                ps.setInt(3, fas);
                ps.setInt(4, kod_client);
                ps.setDate(5, new java.sql.Date(sDate));
                ps.setDate(6, new java.sql.Date(eDate + DAY));
                ps.setInt(7, fas);
                ps.setInt(8, kod_client);

                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    if (rs.getInt(7) != 0)
                        items.add(new Object[]{rs.getInt(1), rs.getString(2).trim(), rs.getString(3).trim(), rs.getInt(4), rs.getInt(5), rs.getString(6).trim(), rs.getInt(7)});
                }
            } catch (Exception e) {
                log.severe("Ошибка getDiscrModelClient(int kod_client, int fas, int kod_brig) where kod_brig = 138  " + e);
            }
        } else {
            String query = "select fas, nar, ngpr, rst, rzm, ncw, sum(skol) from (select fas, nar, ngpr, kod_marh," +
                    " sum(kol)as skol, rst, rzm, ncw from v_marh_list1 where kod_pol = ? and fas = ? group by fas," +
                    " nar, ngpr, kod_marh, rst, rzm, ncw) as t1 " +
                    " join (select kod_owner, kod from marh_list where kod_owner = ?) as t2 on t1.kod_marh = t2.kod " +
                    " left join (select naim, kod1 from nsi_brig)as brig on brig.kod1 = t2.kod_owner " +
                    " group by fas, nar, ngpr, rst, rzm, ncw order by fas ";
            try (PreparedStatement ps = getConnection().prepareStatement(query)) {
                ps.setInt(1, kod_client);
                ps.setInt(2, fas);
                ps.setInt(3, kod_brig);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    items.add(new Object[]{rs.getInt(1), rs.getString(2).trim(), rs.getString(3).trim(), rs.getInt(4), rs.getInt(5), rs.getString(6).trim(), rs.getInt(7)});
                }
            } catch (Exception e) {
                log.severe("Ошибка getDiscrModelClient(int kod_client, int fas, int kod_brig) " + e);
            }

        }

        Object[][] r = new Object[items.size()][7];
        for (Object[] o : items) {
            r[i] = o;
            i++;
        }
        return r;
    }
}
