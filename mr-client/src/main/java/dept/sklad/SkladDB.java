package dept.sklad;

import dept.MyReportsModule;
import dept.marketing.cena.CenaPDB;
import dept.sklad.model.Dogovor;
import dept.sklad.model.DogovorInfo;
import lombok.Getter;
import lombok.SneakyThrows;
import workDB.DBF;
import workDB.DB_new;

import javax.swing.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import static common.UtilFunctions.convertDateStrToLong;

/**
 * @author vova
 */
public class SkladDB extends DB_new {

    private static final Logger log = Logger.getLogger(SkladDB.class.getName());

    @Getter
    public String izm = "";
    public String type_prod = "";
    public java.util.regex.Matcher m;
    public java.util.regex.Pattern p, p1;

    public static BigDecimal roundBigDecimal(double value, int roundIndex) {
        BigDecimal decimal = new BigDecimal(value);
        if (roundIndex > 5) {
            roundIndex = 5;
        }
        for (int index = 5; index >= roundIndex; index--) {
            decimal = decimal.setScale(index, RoundingMode.HALF_UP);
        }
        return decimal;
    }

    public String getTypeProd() {
        return type_prod;
    }

//    public List realOstSklad() {
//        List items = new ArrayList();
//        String query = "select kod_izd, color, kol from _ost_sklad  where kol <> 0 order by kod_izd, color";
//        try (PreparedStatement ps = conn.prepareStatement(query)) {
//            ResultSet rs = ps.executeQuery();
//            while (rs.next()) {
//                Vector v = new Vector();
//                v.add(rs.getInt(1));
//                v.add(rs.getString(2));
//                v.add(rs.getInt(3));
//                items.add(v);
//            }
//        } catch (Exception e) {
//            log.severe("Ошибка realOstSklad() " + e);
//        }
//        return items;
//    }

    /**
     * возврачает изделя принятые складом от упаковки(138), чулок(501), внешние
     * возвраты b (b133) в период времени
     *
     * @param sDate -- начало периода
     * @param eDate -- конец периода
     * @return набор данных в виде: код изделия -- цвет -- кол-во
     */
//    public ArrayList zdanoNaSklad(long sDate, long eDate) {
//        ArrayList items = new ArrayList();
//        String queryString = new String();
//
//        try {
//            queryString = "select t1.kod_izd, t1.ncw, sum(t1.kol)  from (select cena, (kol*kol_in_upack)as kol, srt, kod_izd, ncw from vnperem2 where doc_id in (select item_id from vnperem1 where status = 0 and (kpodot = 138 or kpodot = 133 or kpodot = 501) and kpodto = 737 and date >= ? and date < ?))as t1  "
//                    + " group by t1.kod_izd, t1.ncw";
//
//            ps = conn.prepareStatement(queryString);
//            ps.setDate(1, new java.sql.Date(sDate));
//            ps.setDate(2, new java.sql.Date(eDate + DAY));
//            rs = ps.executeQuery();
//            while (rs.next()) {
//                items.add(rs.getInt(1));//код изделия
//                items.add(rs.getString(2));//цвет изделия
//                items.add(rs.getInt(3));//кол-во
//            }
//возвраты от покупателей
//            queryString = "select kod_izd, ncw, sum(t1.kol*t1.kol_in_upack) from (select kol,kol_in_upack, kod_izd, ncw from otgruz2 where  doc_id in (select item_id from otgruz1 where status = 0 and date >= ? and date < ? and (operac = 'Возврат от покупателя' or operac = 'Возврат из розницы')))as t1 group by t1.kod_izd, t1.ncw";
//            ps = conn.prepareStatement(queryString);
//            ps.setDate(1, new java.sql.Date(sDate));
//            ps.setDate(2, new java.sql.Date(eDate + DAY));
//            rs = ps.executeQuery();
//            while (rs.next()) {
//                items.add(rs.getInt(1));//код изделия
//                items.add(rs.getString(2));//цвет изделия
//                items.add(rs.getInt(3));//кол-во
//            }
//        } catch (Exception e) {
//            log.error("Ошибка при выполнении функции zdanoNaSklad", e);
//            System.out.println("Error in zdanoNaSklad function: " + e);
//        }
//        return items;
//    }
//
//    /**
//     * возврачает изделя отгруженные складом в период времени
//     *
//     * @param sDate -- начало периода
//     * @param eDate -- конец периода
//     * @return набор данных в виде: код изделия -- цвет -- кол-во
//     */
//    public ArrayList otgruzkaSklad(long sDate, long eDate) {
//        ArrayList items = new ArrayList();
//        String queryString = new String();
//
//        try {
//            queryString = "select t1.kod_izd, t1.ncw, sum(t1.kol*t1.kol_in_upack) " +
//                    "from (select kol,kol_in_upack, kod_izd, ncw from otgruz2 where doc_id in " +
//                    "(select item_id from otgruz1 where status = 0 and date >= ? and date < ? and " +
//                    "(operac <> 'Возврат от покупателя' and operac <> 'Возврат из розницы')))as t1 "
//                    + " group by t1.kod_izd, t1.ncw";
//
//            ps = conn.prepareStatement(queryString);
//            ps.setDate(1, new java.sql.Date(sDate));
//            ps.setDate(2, new java.sql.Date(eDate));
//            rs = ps.executeQuery();
//            while (rs.next()) {
//                items.add(rs.getInt(1));//код изделия
//                items.add(rs.getString(2));//цвет изделия
//                items.add(rs.getInt(3));//кол-во
//            }
//        } catch (Exception e) {
//            log.error("Ошибка при выполнении функции otgruzkaSklad", e);
//            System.out.println("Error in otgruzkaSklad function: " + e);
//        }
//        return items;
//    }

    //WHERE     (doc_id = 89181) AND (ncw = 'РАЗНОЦВЕТ') AND (cenav = 28.16) AND (rst_marh = 98) AND (rzm_marh = 14)
    //sum(kol*kol_in_upack)
    //ДЖИНС

    /**
     * возвращает накладные отгруженные за дату
     *
     * @param sDate -- начало периода
     * @param eDate -- конец периода
     */
    public List otgruzkaSkladNakl(String sDate, String eDate) {
        List items = new ArrayList();
        Map hm;
        String queryString = "{ ? = call _otgruzka_sklad_nakl (?,?) }";
        try (CallableStatement cs = getConnection().prepareCall(queryString)) {
            cs.registerOutParameter(1, Types.OTHER);
            cs.setString(2, sDate);
            cs.setString(3, eDate);
            ResultSet rs = cs.executeQuery();
            while (rs.next()) {
                hm = new HashMap();
                hm.put("name", rs.getString(1));
                hm.put("data", rs.getString(2));
                hm.put("ndoc", rs.getString(3));
                hm.put("summa", rs.getString(4));
                hm.put("summa_nds", rs.getString(5));
                hm.put("summa_all", rs.getString(6));
                hm.put("summav", rs.getString(7));
                hm.put("summa_ndsv", rs.getString(8));
                hm.put("summa_allv", rs.getString(9));
                hm.put("operac", rs.getString(10));
                hm.put("kol", rs.getString(11));
                items.add(hm);
            }
        } catch (Exception e) {
            log.severe("Ошибка при выполнении функции otgruzkaSklad" + e.getMessage());
        }
        return items;
    }

    /**
     * возврачает изделя отгруженные складом в период времени
     *
     * @param sDate -- начало периода
     * @param eDate -- конец периода
     * @return набор данных в виде: код изделия -- цвет -- кол-во
     */
//    public Vector getProtocolSoglas(String nomer) {
//        Vector v = new Vector();
//        String query = "select ngpr, nar, fas, cno, (cno/kurs) as rr, cenav, cena, kurs, skidka from "
//                + " (select item_id, kurs, skidka from otgruz1 where ndoc = ?)as t "
//                + " left join (select cena, kod_izd, cenav, doc_id from otgruz2 )as t1 on t.item_id = t1.doc_id"
//                + " left join (select kod, kod1, cno from nsi_sd) as t3 on t1.kod_izd = t3.kod1"
//                + " left join (select ngpr, fas, nar, kod from nsi_kld) as t2 on t3.kod = t2.kod"
//                + " group by ngpr, nar, fas, cno, (cno/kurs), cenav, cena, kurs, skidka order by ngpr, nar, fas, cno";
//        try {
//            ps = conn.prepareStatement(query);
//            ps.setString(1, nomer);
//            rs = ps.executeQuery();
//            while (rs.next()) {
//                Vector vv = new Vector();
//                vv.add(rs.getString(1).trim());
//                vv.add(rs.getString(2).trim());
//                vv.add(rs.getString(3).trim());
//                vv.add(rs.getLong(4));
//                vv.add(rs.getFloat(5));
//                vv.add(rs.getFloat(6));
//                vv.add(rs.getLong(7));
//                vv.add(rs.getLong(8));
//                vv.add(rs.getFloat(9));
//                v.add(vv);
//            }
//        } catch (Exception e) {
//            log.severe("Ошибка в DB getProtocolSoglas(String nomer) " + e);
//        }
//        return v;
//    }
    public String[] getAdresRazgruz(String ttn) {
        String[] adres = new String[100];
        String[] adresResult = null;
        String result;
        int i = 0;
        String query = " SELECT t1.PINDEX,t1.OBLAST,t1.RAION,t1.GOROD,t1.STREET,t1.DOM FROM s_adres as t1 "
                + " INNER JOIN (SELECT klient_id,kpl FROM otgruz1 WHERE ndoc=?) as t2 ON t1.KLIENT_ID = t2.klient_id ";
        try (PreparedStatement ps = getConnection().prepareStatement(query)) {
            ps.setString(1, ttn);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                result = "";
                if (!rs.getString(1).trim().isEmpty()) {
                    result = result + rs.getString(1).trim() + ", ";
                }
                if (!rs.getString(2).trim().isEmpty()) {
                    result = result + rs.getString(2).trim() + " обл., ";
                }
                if (!rs.getString(3).trim().isEmpty()) {
                    result = result + rs.getString(3).trim() + " район, ";
                }
                if (!rs.getString(4).trim().isEmpty()) {
                    result = result + " г. " + rs.getString(4).trim() + ", ";
                }
                if (!rs.getString(5).trim().isEmpty()) {
                    result = result + " ул. " + rs.getString(5).trim() + ", ";
                }
                if (!rs.getString(6).trim().isEmpty()) {
                    result = result + "д. " + rs.getString(6).trim();
                }
                adres[i] = result;
                i++;
            }
            adresResult = new String[i];
            System.arraycopy(adres, 0, adresResult, 0, i);
        } catch (Exception e) {
            log.severe("Ошибка в DB getAdresRazgruz(String nomer) " + e.getMessage());
        }
        return adresResult;
    }

    public String[] getAdresPoluchatel(String ttn) {
        String[] adres = new String[100];
        int i = 0;
        String[] adresResult = null;
        String result;
        String klient = "";

        String queryAdres = " SELECT t1.PINDEX,t1.OBLAST,t1.RAION,t1.GOROD,t1.STREET,t1.DOM FROM s_adres as t1 "
                + " INNER JOIN (SELECT klient_id,kpl FROM otgruz1 WHERE ndoc=?) as t2 ON t1.KLIENT_ID = t2.klient_id ";
        String queryKlient = "SELECT t1.FULLNAIM,t1.URADRES FROM s_klient t1 INNER JOIN (SELECT klient_id FROM otgruz1 WHERE ndoc = ?) t2 ON t1.ITEM_ID = t2.klient_id";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(queryKlient);
             PreparedStatement ps1 = conn.prepareStatement(queryAdres)) {
            ps.setString(1, ttn);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                klient = rs.getString(1).trim();
            }
            ps1.setString(1, ttn);
            ResultSet rs1 = ps1.executeQuery();
            while (rs1.next()) {
                result = "";
                adres[i] = klient.trim() + ", ";
                if (!rs1.getString(1).trim().isEmpty()) {
                    result = result + rs1.getString(1).trim() + ", ";
                }
                if (!rs1.getString(2).trim().isEmpty()) {
                    result = result + rs1.getString(2).trim() + " обл., ";
                }
                if (!rs1.getString(3).trim().isEmpty()) {
                    result = result + rs1.getString(3).trim() + " район, ";
                }
                if (!rs1.getString(4).trim().isEmpty()) {
                    result = result + " г. " + rs1.getString(4).trim() + ", ";
                }
                if (!rs1.getString(5).trim().isEmpty()) {
                    result = result + " ул. " + rs1.getString(5).trim() + ", ";
                }
                if (!rs1.getString(6).trim().isEmpty()) {
                    result = result + "д. " + rs1.getString(6).trim();
                }
                adres[i] += result;
                i++;
            }
            adresResult = new String[i];
            System.arraycopy(adres, 0, adresResult, 0, i);
        } catch (Exception e) {
            log.severe("Ошибка в DB getAdresRazgruz(String nomer) " + e.getMessage());
        }
        return adresResult;
    }

    public List getOtpravAndPoluch(String ttn) {
        List hm = new ArrayList();
        String query = " select gruzopoluchatel,otpravitel from _put_list where ttn_id = "
                + " ( select item_id from otgruz1 where ndoc=? ) ";
        try (PreparedStatement ps = getConnection().prepareStatement(query)) {
            ps.setString(1, ttn);
            rs = ps.executeQuery();
            while (rs.next()) {
                hm.add(rs.getString("gruzopoluchatel"));
                hm.add(rs.getString("otpravitel"));
            }
        } catch (Exception ex) {
            log.severe("Ошибка в DB getOtpravAndPoluch" + ex.getMessage());
        }
        return hm;
    }

    public String quickSearch(String var) {
        int i = 0;
        String result = "";
        String sql = "{ ? = call _quick_search (?) }";
        try (CallableStatement cs = getConnection().prepareCall(sql)) {
            cs.registerOutParameter(1, Types.OTHER);
            var = var + "%";
            cs.setString(2, var);
            ResultSet rs = cs.executeQuery();
            while (rs.next() && i == 0) {
                result = rs.getString(1).substring(var.length() - 1, rs.getString(1).length());
                i++;
            }

        } catch (Exception ex) {
            log.severe("Exception in method quickSearch(Srting var)" + ex.getMessage());
        }
        return result;
    }

    public int shareNakl(String oldDoc, String newDoc) {
        String sql = " {? = call _share_nakadnie (?, ?) }";
        int result = 0;
        try (CallableStatement cs = getConnection().prepareCall(sql)) {
            cs.registerOutParameter(1, Types.OTHER);
            cs.setString(2, newDoc);
            cs.setString(3, oldDoc);
            ResultSet rs = cs.executeQuery();
            while (rs.next()) {
                result++;
            }
        } catch (Exception ex) {
            log.severe("Exception in method shareNakl " + ex.getMessage());
        }
        return result;
    }

    public HashMap getPutListInfo(String ttn) {
        HashMap<String, Object> hm = new HashMap();
        String query = "";
        query = " select ttn_id,ot1.item_id,ot1.ndoc,ot1.prepayment,ot1.operac, ot1.date as datedoc from _put_list as pl "
                + " left join otgruz1 as ot1 on pl.ttn_id=ot1.item_id "
                + " where ot1.ndoc=? ";
        hm.put("datedoc", "");
        hm.put("date", "");
        hm.put("kol", "");
        hm.put("adres_otprav", "");
        hm.put("adres_poluch", "");
        hm.put("razgruz", "");
        hm.put("pogruz", "");
        hm.put("pereadres", "");
        hm.put("dover", "");
        hm.put("doverDate", "");
        hm.put("doverVidan", "");
        hm.put("doverPrinial", "");
        hm.put("doverPlomba", "");
        hm.put("schot_platelschik", "");
        hm.put("otpustil", "");
        hm.put("sdal", "");
        hm.put("prinial", "");
        hm.put("nplombi", "");
        hm.put("ispolnitel", "");
        hm.put("primechanie", "");
        hm.put("doc", "");
        hm.put("avto", "");
        hm.put("trailer", "");
        hm.put("voditel", "");
        hm.put("vladelets_avto", "");
        hm.put("zakazchik_avto", "");
        hm.put("skidka_client", "1");
        hm.put("skidka_opt", "0");
        hm.put("kurs_nbrb", "1");
        hm.put("valuta", "BYR");
        hm.put("kkrr", -1);
        hm.put("gruz_mest", "");
        hm.put("massa", "");
        hm.put("summa_nds", "");
        hm.put("summa_i_nds", "");
        hm.put("nds", "");
        hm.put("nomer", "");
        hm.put("klient_id", "");
        hm.put("ttn", "");
        hm.put("osnovanie", "");
        hm.put("sposob", "руч.");
        hm.put("dogovor", "");
        hm.put("kod_klienta", "");
        hm.put("vid_ggr", "");
        hm.put("vid_sert", "");
        hm.put("unn_pol", "");
        hm.put("torg_nadb", "");
        hm.put("prepayment", "");
        hm.put("operac", "");
        try {
            ps = conn.prepareStatement(query);
            ps.setString(1, ttn);
            rs = ps.executeQuery();
            if (rs.next()) {
                hm.put("datedoc", rs.getString("datedoc"));
                hm.put("ttn_id", rs.getString("ttn_id"));
                hm.put("prepayment", rs.getBoolean("prepayment"));
                hm.put("operac", rs.getString("operac"));
                query = " select top 1 pl.date, pl.ndoc, avto, pritsep, voditel, vlad_avto, zakaz_avto, pereadres, otpustil, sum(ot2.kol*ot2.kol_in_upack) as kol, "
                        + " sdal, prinial, doc, nplombi, ispol_pogruz, primechanie, skidka_client, skidka_opt, kurs_nbrb, "
                        + " valuta, kkrr, isnull(ndover, '')as ndover, isnull(dover_date, '')as dover_date, isnull(dover_vidan, '')as dover_vidan, "
                        + " isnull(dover_prinial, '') as dover_prinial, isnull(dover_plomba, '')as dover_plomba, isnull(schot_platelschik, 0) "
                        + " as schot_platelschik, gruzopoluchatel, prazgruzki, isnull(osnovanie, '') as osnovanie,ot2.nds as nds, "
                        + " ot1.vid_ggr,ot1.vid_sert,ot1.kpl, kl.UNN,ppogruzki,ot1.operac,pl.torg_nad from _put_list as pl "
                        + " left join otgruz1 as ot1 on pl.ttn_id=ot1.item_id "
                        + " left join s_klient as kl on ot1.kpl=kl.KOD "
                        + " left join otgruz2 as ot2 on ot1.item_id=ot2.doc_id "
                        + " where ttn_id = (select item_id from otgruz1 where ndoc = ?) "
                        + " group by pl.date, pl.ndoc, avto, pritsep, voditel, vlad_avto, zakaz_avto, pereadres, otpustil, "
                        + " sdal, prinial, doc, nplombi, ispol_pogruz, primechanie, skidka_client, skidka_opt, kurs_nbrb, "
                        + " valuta, kkrr, ndover, dover_date, dover_vidan, dover_prinial, dover_plomba, schot_platelschik, gruzopoluchatel, prazgruzki, "
                        + " osnovanie, ot2.nds,ot1.vid_ggr,ot1.vid_sert,ot1.kpl, kl.UNN,ppogruzki,ot1.operac,pl.torg_nad ";
                ps = conn.prepareStatement(query);
                ps.setString(1, ttn);
                rs = ps.executeQuery();
                if (rs.next()) {
                    hm.put("operac", rs.getString("operac"));
                    hm.put("ttn", ttn);
                    hm.put("vid_ggr", rs.getString("vid_ggr"));
                    hm.put("vid_sert", rs.getString("vid_sert"));
                    hm.put("osnovanie", rs.getString("osnovanie").trim());
                    //hm.put("dogovor", hm.get("osnovanie"));

                    String da = "";
                    String[] a = new String[2];
                    a = rs.getString("date").split(" ");
                    if (!a[0].trim().equals("1900-01-01")) {
                        a[0] = a[0].trim();
                        da += a[0].substring(8, 10) + ".";
                        da += a[0].substring(5, 7) + ".";
                        da += a[0].substring(0, 4);
                    }
                    if (!da.trim().equals("01.01.1900") && !da.trim().equals("")) {
                        hm.put("date", da);
                    } else {
                        hm.put("date", "");
                    }
                    hm.put("nomer", rs.getString("ndoc").trim());
                    hm.put("avto", rs.getString("avto").trim());
                    hm.put("trailer", rs.getString("pritsep").trim());
                    hm.put("voditel", rs.getString("voditel").trim());
                    hm.put("vladelets_avto", rs.getString("vlad_avto").trim());
                    hm.put("zakazchik_avto", rs.getString("zakaz_avto").trim());
                    hm.put("pereadres", rs.getString("pereadres").trim());
                    hm.put("otpustil", rs.getString("otpustil").trim());
                    hm.put("sdal", rs.getString("sdal").trim());
                    hm.put("prinial", rs.getString("prinial").trim());
                    hm.put("doc", rs.getString("doc").trim());
                    hm.put("nplombi", rs.getString("nplombi").trim());
                    hm.put("ispolnitel", rs.getString("ispol_pogruz").trim());
                    hm.put("primechanie", rs.getString("primechanie").trim());
                    hm.put("kol", rs.getString("kol"));
                    if (rs.getInt("kpl") == 8889 || rs.getInt("kpl") == 2281 || rs.getInt("kpl") == 3451 || rs.getInt("kpl") == 5270) {
                        hm.put("nds", 0);
                    } else {
                        hm.put("nds", rs.getFloat("nds"));
                    }
                    System.out.println("Получение значения НДС " + hm.get("nds"));
                    hm.put("pogruz", rs.getString("ppogruzki"));

                    hm.put("kod_klienta", rs.getString("kpl"));
                    hm.put("unn_pol", rs.getString("UNN").trim());
                    hm.put("dover", rs.getString("ndover").trim());
                    da = "";
                    a = new String[2];
                    a = rs.getString("dover_date").split(" ");
                    if (!a[0].trim().equals("1900-01-01")) {

                        a[0] = a[0].trim();
                        da += a[0].substring(8, 10) + ".";
                        da += a[0].substring(5, 7) + ".";
                        da += a[0].substring(0, 4);
                    }

                    if (!da.trim().equals("01.01.1900") && !da.trim().equals("")) {
                        hm.put("doverDate", da);
                    } else {
                        hm.put("doverDate", "");
                    }
                    hm.put("doverVidan", rs.getString("dover_vidan").trim());
                    hm.put("doverPrinial", rs.getString("dover_prinial").trim());
                    hm.put("doverPlomba", rs.getString("dover_plomba").trim());
                    hm.put("schot_platelschik", rs.getString("schot_platelschik"));
                    if (!rs.getString("gruzopoluchatel").isEmpty()) {
                        hm.put("gruzopoluchatel", rs.getString("gruzopoluchatel"));
                    }
                    if (!rs.getString("prazgruzki").isEmpty()) {
                        hm.put("razgruz", rs.getString("prazgruzki"));
                    }

                    BigDecimal n = new BigDecimal(Float.parseFloat(rs.getString("skidka_client").trim()));
                    hm.put("skidka_client", round(n, 4));

                    n = new BigDecimal(Float.parseFloat(rs.getString("skidka_opt").trim()));
                    hm.put("skidka_opt", round(n, 2));

                    n = new BigDecimal(Float.parseFloat(rs.getString("kurs_nbrb").trim()));
                    hm.put("kurs_nbrb", round(n, 2));

                    hm.put("valuta", rs.getString("valuta").trim());

                    n = new BigDecimal(Float.parseFloat(rs.getString("kkrr").trim()));
                    hm.put("kkrr", round(n, 2));
                    hm.put("torg_nadb", rs.getFloat("torg_nad"));
                }
            } else {
                query = "select summa, summa_nds, summa_all,(kolk + kolr) as gm, prepayment, otgruz1.operac from otgruz1 where ndoc=? ";
                ps = conn.prepareStatement(query);
                ps.setString(1, ttn);
                rs = ps.executeQuery();
                if (rs.next()) {
                    hm.put("gruz_mest", rs.getInt("gm"));
                    hm.put("summa_nds", rs.getString("summa_nds"));
                    hm.put("summa_i_nds", rs.getString("summa_all"));
                    hm.put("prepayment", rs.getBoolean("prepayment"));
                    hm.put("operac", rs.getString("operac"));
                }
                query = " select isnull(dog.NAIM,'') as NAIM,isnull(dog.DATA,'') as DATA,isnull(dog.NOMER, '') as NOMER,ot1.kpl, (ot1.kola) as kolvo,(ot1.kolk+ot1.kolr) as gruz_mest, "
                        + " ot1.vid_ggr, ot1.vid_sert,ot1.ndoc,ot1.item_id,kl.UNN,ot2.nds,ot1.skidka,ot2.nds,ltrim(rtrim(isnull(kl.NAIM,''))) as naim_org, "
                        + " ltrim(rtrim(isnull(adr.PINDEX,''))) as PINDEX, ltrim(rtrim(isnull(adr.OBLAST,''))) as OBLAST, "
                        + " ltrim(rtrim(isnull(adr.RAION,''))) as RAION, ltrim(rtrim(isnull(adr.GOROD,''))) as GOROD, "
                        + " ltrim(rtrim(isnull(adr.STREET,''))) as STREET, ltrim(rtrim(isnull(adr.DOM,''))) as DOM, ot1.operac,ot1.valuta_id "
                        + " from (select klient_id,item_id,kpl,ndoc,kola,kolk,kolr,summa,summa_nds,summa_all,vid_sert,vid_ggr,skidka,operac,valuta_id from otgruz1 where ndoc=?) as ot1 "
                        + " left join (select kod_izd,doc_id,nds from otgruz2) as ot2 on ot1.item_id=ot2.doc_id "
                        + " left join (select NAIM,UNN, ITEM_ID,POSTADRES,DOGOVOR,KOD from s_klient) as kl on ot1.kpl=kl.KOD "
                        + " left join (select PINDEX,OBLAST,RAION,GOROD,STREET,DOM,ITEM_ID from s_adres) as adr on kl.POSTADRES=adr.ITEM_ID "
                        + " left join (select NAIM,DATA,NOMER,ITEM_ID,KLIENT_ID from s_dogovor) as dog on kl.DOGOVOR=dog.ITEM_ID and kl.ITEM_ID=dog.KLIENT_ID "
                        + " left join (select massa,kod1 from nsi_sd) as sd on ot2.kod_izd=sd.kod1 ";
                ps = conn.prepareStatement(query);
                ps.setString(1, ttn);
                rs = ps.executeQuery();
                if (rs.next()) {
                    if (rs.getInt("valuta_id") == 1) {
                        hm.put("valuta", "BYR");
                    } else {
                        if (rs.getInt("valuta_id") == 2) {
                            hm.put("valuta", "RUB");
                        }
                    }
                    String ss = "";
                    String[] a = new String[2];
                    hm.put("doc", "ТТН № " + ttn);
                    hm.put("kol", rs.getString("kolvo"));
                    if (!rs.getString("NAIM").trim().equals("")) {
                        ss += rs.getString("NAIM").trim();
                    }
                    if (!rs.getString("NOMER").trim().equals("0") && !rs.getString("NOMER").trim().equals("")) {
                        ss += " № " + rs.getString("NOMER").trim();
                    }
                    if (!rs.getString("DATA").trim().equals("")) {
                        a = rs.getString("DATA").split(" ");
                    }
                    if (!a[0].trim().equals("1900-01-01")) {
                        String da = "";
                        a[0] = a[0].trim();
                        da += a[0].substring(8, 10) + ".";
                        da += a[0].substring(5, 7) + ".";
                        da += a[0].substring(0, 4);
                        ss += " от " + da;
                    }
                    hm.put("operac", rs.getString("operac"));
                    hm.put("osnovanie", ss.trim());
                    hm.put("sposob", "руч.");
                    hm.put("ttn", ttn);
                    hm.put("dogovor", hm.get("osnovanie").toString().trim());
                    hm.put("dogovor", "");
                    if (rs.getInt("kpl") == 8889 || rs.getInt("kpl") == 2281 || rs.getInt("kpl") == 3451 || rs.getInt("kpl") == 5270) {
                        hm.put("nds", 0);
                    } else {
                        hm.put("nds", rs.getFloat("nds"));
                    }
                    hm.put("ttn_id", rs.getInt("item_id"));
                    hm.put("kod_klienta", rs.getString("kpl"));
                    hm.put("vid_ggr", rs.getString("vid_ggr").trim());
                    hm.put("vid_sert", rs.getString("vid_sert").trim());
                    hm.put("unn_pol", rs.getString("UNN"));
                    hm.put("skidka_opt", rs.getString("skidka"));
                    hm.put("gruz_mest", rs.getString("gruz_mest"));
                    String adr = "";
                    String adr1 = "";
                    if (!rs.getString("naim_org").toString().trim().equals("")) {
                        adr += rs.getString("naim_org").toString().trim();
                    }
                    if (!rs.getString("PINDEX").toString().trim().equals("")) {
                        adr += ", " + rs.getString("PINDEX").toString().trim();
                    }
                    if (!rs.getString("OBLAST").toString().trim().equals("")) {
                        adr += ", обл, " + rs.getString("OBLAST").toString().trim();
                    }
                    if (!rs.getString("RAION").toString().trim().equals("")) {
                        adr += ", район " + rs.getString("RAION").toString().trim();
                    }
                    if (!rs.getString("GOROD").toString().trim().equals("")) {
                        adr += ", г. " + rs.getString("GOROD").toString().trim();
                    }
                    if (!rs.getString("STREET").toString().trim().equals("")) {
                        adr += ", ул. " + rs.getString("STREET").toString().trim();
                    }
                    if (!rs.getString("DOM").toString().trim().equals("")) {
                        adr += ", д. " + rs.getString("DOM").toString().trim();
                    }
                    hm.put("adres_poluch", adr);
                }
            }


            query = "select PINDEX,OBLAST,RAION,GOROD,STREET,DOM from s_klient "
                    + " left join (select PINDEX,OBLAST,RAION,GOROD,STREET,DOM,ITEM_ID as item from s_adres) as adr on s_klient.URADRES=adr.item "
                    + " where s_klient.KOD=-1 ";
            ps = conn.prepareStatement(query);
            rs = ps.executeQuery();
            if (rs.next()) {
                String adr = "";
                if (!rs.getString("PINDEX").toString().trim().equals("")) {
                    adr += rs.getString("PINDEX").toString().trim();
                }
                if (!rs.getString("OBLAST").toString().trim().equals("")) {
                    adr += ", обл, " + rs.getString("OBLAST").toString().trim();
                }
                if (!rs.getString("RAION").toString().trim().equals("")) {
                    adr += ", район " + rs.getString("RAION").toString().trim();
                }
                if (!rs.getString("GOROD").toString().trim().equals("")) {
                    adr += ", г. " + rs.getString("GOROD").toString().trim();
                }
                if (!rs.getString("STREET").toString().trim().equals("")) {
                    adr += ", ул. " + rs.getString("STREET").toString().trim();
                }
                if (!rs.getString("DOM").toString().trim().equals("")) {
                    adr += ", д. " + rs.getString("DOM").toString().trim();
                }
                hm.put("pogruz", adr);
            }
            query = " select isnull(dog.NAIM,'') as NAIM,isnull(dog.DATA,'') as DATA,isnull(dog.NOMER, '') as NOMER,ot1.kpl, (ot1.kola) as kolvo,(ot1.kolk+ot1.kolr) as gruz_mest, "
                    + " ot1.vid_ggr, ot1.vid_sert,ot1.ndoc,ot1.item_id,kl.UNN,ot2.nds,ot1.skidka,ot2.nds,ltrim(rtrim(isnull(kl.NAIM,''))) as naim_org, "
                    + " ltrim(rtrim(isnull(adr.PINDEX,''))) as PINDEX, ltrim(rtrim(isnull(adr.OBLAST,''))) as OBLAST, "
                    + " ltrim(rtrim(isnull(adr.RAION,''))) as RAION, ltrim(rtrim(isnull(adr.GOROD,''))) as GOROD, "
                    + " ltrim(rtrim(isnull(adr.STREET,''))) as STREET, ltrim(rtrim(isnull(adr.DOM,''))) as DOM, ot1.operac,ot1.valuta_id "
                    + " from (select klient_id,item_id,kpl,ndoc,kola,kolk,kolr,summa,summa_nds,summa_all,vid_sert,vid_ggr,skidka,operac,valuta_id from otgruz1 where ndoc=?) as ot1 "
                    + " left join (select kod_izd,doc_id,nds from otgruz2) as ot2 on ot1.item_id=ot2.doc_id "
                    + " left join (select NAIM,UNN, ITEM_ID,POSTADRES,DOGOVOR,KOD from s_klient) as kl on ot1.kpl=kl.KOD "
                    + " left join (select PINDEX,OBLAST,RAION,GOROD,STREET,DOM,ITEM_ID from s_adres) as adr on kl.POSTADRES=adr.ITEM_ID "
                    + " left join (select NAIM,DATA,NOMER,ITEM_ID,KLIENT_ID from s_dogovor) as dog on kl.DOGOVOR=dog.ITEM_ID and kl.ITEM_ID=dog.KLIENT_ID "
                    + " left join (select massa,kod1 from nsi_sd) as sd on ot2.kod_izd=sd.kod1 ";
            ps = conn.prepareStatement(query);
            ps.setString(1, ttn);
            rs = ps.executeQuery();
            if (rs.next()) {
                String ss = "";
                String[] a = new String[2];
                if (!rs.getString("NAIM").trim().equals("")) {
                    ss += rs.getString("NAIM").trim();
                }
                if (!rs.getString("NOMER").trim().equals("0") && !rs.getString("NOMER").trim().equals("")) {
                    ss += " № " + rs.getString("NOMER").trim();
                }
                if (!rs.getString("DATA").trim().equals("")) {
                    a = rs.getString("DATA").split(" ");
                }
                if (!a[0].trim().equals("1900-01-01")) {
                    String da = "";
                    a[0] = a[0].trim();
                    da += a[0].substring(8, 10) + ".";
                    da += a[0].substring(5, 7) + ".";
                    da += a[0].substring(0, 4);
                    ss += " от " + da;
                }

                if (hm.get("osnovanie").toString().trim().equals("")) {
                    hm.put("osnovanie", ss.trim());
                }
            }
        } catch (Exception e) {
            log.severe("Ошибка в SkladDB getPutListInfo(HashMap hm) " + e.getMessage());
            System.err.println("Ошибка в SkladDB getPutListInfo(HashMap hm) " + e.getMessage());
        }

        try {
            query = "SELECT date as date_document from otgruz1 where ndoc = ?";
            ps = conn.prepareStatement(query);
            ps.setString(1, ttn);
            rs = ps.executeQuery();
            if (rs.next()) {
                hm.put("document_date", rs.getDate("date_document"));
            }
        } catch (Exception ex) {
            System.err.println("Ошибка получения даты документа " + ex.getMessage());
        }


        return hm;
    }

    private double round(BigDecimal b, int range) {
        b = b.setScale(range, RoundingMode.HALF_UP);
        return b.doubleValue();
    }

    public void writePutList(Map hm, Map hmm) {
        String query = "INSERT INTO _put_list (ndoc, ttn_id, date, avto, pritsep, voditel, vlad_avto, zakaz_avto, "
                + " pereadres, otpustil, sdal,prinial, nplombi, ispol_pogruz, primechanie,doc, skidka_client, skidka_opt, "
                + " kurs_nbrb, valuta, kkrr, ndover, dover_date, dover_vidan, dover_prinial, dover_plomba, "
                + " schot_platelschik, gruzopoluchatel, prazgruzki,otpravitel,osnovanie,sposob,ppogruzki,nds,userName,torg_nad,cmr) "
                + " values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?,?,?,?,?,?,?,?) ";
        String query1 = "select id from _put_list where ttn_id = ? ";
        String query2 = "update _put_list set ndoc = ?, ttn_id = ?, date = ?, avto = ?, pritsep = ?, voditel = ?,"
                + " vlad_avto = ?, zakaz_avto = ?, pereadres = ?, otpustil = ?, sdal = ?,prinial = ?, nplombi = ?,"
                + " ispol_pogruz = ?, primechanie = ?,doc = ?, skidka_client = ?, skidka_opt = ?, kurs_nbrb = ?, "
                + " valuta = ?, kkrr = ?, ndover = ?, dover_date = ?, dover_vidan = ?, dover_prinial = ?, "
                + " dover_plomba = ?, schot_platelschik = ?, gruzopoluchatel = ?, prazgruzki = ?,date_ins=getdate(),"
                + " otpravitel=?,osnovanie=?,sposob=?,ppogruzki=?,nds=?,userName=?,torg_nad=?,cmr=?  where ttn_id = ? ";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(query);
             PreparedStatement ps1 = conn.prepareStatement(query1);
             PreparedStatement ps2 = conn.prepareStatement(query2);) {
            ps.setInt(1, Integer.parseInt(hm.get("ttn_id").toString()));
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) {
                java.sql.Date date_in = new java.sql.Date(convertDateStrToLong(hm.get("date").toString()));
                ps1.setString(1, hm.get("nomer").toString().trim());
                ps1.setInt(2, Integer.parseInt(hm.get("ttn_id").toString().trim()));
                ps1.setDate(3, date_in);
                ps1.setString(4, hm.get("avto").toString().trim());
                ps1.setString(5, hm.get("trailer").toString().trim());
                ps1.setString(6, hm.get("voditel").toString().trim());
                ps1.setString(7, hm.get("vladelets_avto").toString().trim());
                ps1.setString(8, hm.get("zakazchik_avto").toString().trim());
                ps1.setString(9, hm.get("pereadres").toString().trim());
                ps1.setString(10, hm.get("otpustil").toString().trim());
                ps1.setString(11, hm.get("sdal").toString().trim());
                ps1.setString(12, hm.get("prinial").toString().trim());
                ps1.setString(13, hm.get("nplombi").toString().trim());
                ps1.setString(14, hm.get("ispolnitel").toString().trim());
                ps1.setString(15, hm.get("primechanie").toString().trim());
                ps1.setString(16, hm.get("doc").toString().trim());
                ps1.setFloat(17, Float.parseFloat(hmm.get("koef").toString().trim()));
                ps1.setFloat(18, Float.parseFloat(hmm.get("skidka").toString().trim()));
                ps1.setFloat(19, Float.parseFloat(hmm.get("kurs_bel").toString().trim()));
                ps1.setString(20, (hmm.get("valuta").toString().trim()));
                ps1.setFloat(21, Float.parseFloat(hmm.get("kurs").toString().trim()));
                ps1.setString(22, hm.get("dover").toString().trim());
                date_in = new java.sql.Date(convertDateStrToLong(hm.get("doverDate").toString().trim()));
                ps1.setDate(23, date_in);
                ps1.setString(24, hm.get("doverVidan").toString().trim());
                ps1.setString(25, hm.get("doverPrinial").toString().trim());
                ps1.setString(26, hm.get("doverPlomba").toString().trim());
                ps1.setString(27, hm.get("schot_platelschik").toString().trim());
                ps1.setString(28, hm.get("adres_poluch").toString().trim());
                ps1.setString(29, hm.get("razgruz").toString().trim());
                ps1.setString(30, hm.get("adres_otprav").toString().trim());
                ps1.setString(31, hm.get("osnovanie").toString().trim());
                ps1.setString(32, hm.get("sposob").toString().trim());
                ps1.setString(33, hm.get("ppogruzki").toString().trim());
                ps1.setFloat(34, Float.parseFloat(hm.get("nds").toString()));
                ps1.setString(35, MyReportsModule.UserName);
                ps1.setInt(36, Integer.parseInt(hm.get("torg_nadb").toString()));
                ps1.setString(37, hm.get("cmr").toString().trim());
                ps1.executeUpdate();
            } else {
                java.sql.Date date_in = new java.sql.Date(convertDateStrToLong(hm.get("date").toString()));
                ps2.setString(1, hm.get("nomer").toString());
                ps2.setInt(2, Integer.parseInt(hm.get("ttn_id").toString().trim()));
                ps2.setDate(3, date_in);
                ps2.setString(4, hm.get("avto").toString().trim());
                ps2.setString(5, hm.get("trailer").toString().trim());
                ps2.setString(6, hm.get("voditel").toString().trim());
                ps2.setString(7, hm.get("vladelets_avto").toString().trim());
                ps2.setString(8, hm.get("zakazchik_avto").toString().trim());
                ps2.setString(9, hm.get("pereadres").toString().trim());
                ps2.setString(10, hm.get("otpustil").toString().trim());
                ps2.setString(11, hm.get("sdal").toString().trim());
                ps2.setString(12, hm.get("prinial").toString().trim());
                ps2.setString(13, hm.get("nplombi").toString().trim());
                ps2.setString(14, hm.get("ispolnitel").toString().trim());
                ps2.setString(15, hm.get("primechanie").toString().trim());
                ps2.setString(16, hm.get("doc").toString().trim());
                ps2.setFloat(17, Float.parseFloat(hmm.get("koef").toString().trim()));
                ps2.setFloat(18, Float.parseFloat(hmm.get("skidka").toString().trim()));
                ps2.setFloat(19, Float.parseFloat(hmm.get("kurs_bel").toString().trim()));
                ps2.setString(20, (hmm.get("valuta").toString().trim()));
                ps2.setFloat(21, Float.parseFloat(hmm.get("kurs").toString().trim()));
                ps2.setString(22, hm.get("dover").toString());
                date_in = new java.sql.Date(convertDateStrToLong(hm.get("doverDate").toString().trim()));
                ps2.setDate(23, date_in);
                ps2.setString(24, hm.get("doverVidan").toString().trim());
                ps2.setString(25, hm.get("doverPrinial").toString().trim());
                ps2.setString(26, hm.get("doverPlomba").toString().trim());
                ps2.setString(27, hm.get("schot_platelschik").toString().trim());
                ps2.setString(28, hm.get("adres_poluch").toString().trim());
                ps2.setString(29, hm.get("razgruz").toString().trim());
                ps2.setString(30, hm.get("adres_otprav").toString().trim());
                ps2.setString(31, hm.get("osnovanie").toString().trim());
                ps2.setString(32, hm.get("sposob").toString().trim());
                ps2.setString(33, hm.get("ppogruzki").toString().trim());
                ps2.setFloat(34, Float.parseFloat(hm.get("nds").toString().trim()));
                ps2.setInt(38, Integer.parseInt(hm.get("ttn_id").toString().trim()));
                ps2.setString(35, MyReportsModule.UserName);
                ps2.setFloat(36, Float.parseFloat(hm.get("torg_nadb").toString()));
                ps2.setString(37, hm.get("cmr").toString());
                ps2.executeUpdate();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ошибка записи в put_list!");
            log.severe("Ошибка в SkladDB writePutList(HashMap hm) " + e.getMessage());
        }
    }

//    public ArrayList getPrilojenieReturn(String nomer) {
//        ArrayList<HashMap> v = new ArrayList();
//        boolean bol = true;
//        BigDecimal n;
//        PreparedStatement ps2;
//        ResultSet rs2;
//        PreparedStatement ps3;
//        ResultSet rs3;
//        int kid = 0;
//        String polotnoQuery = " select ncw as cvet, kolvo_upack as gruzm,   (massa_surov) as massa from _otgruz_polotno as pol "
//                + " left join (select ncw,item_id,doc_id from otgruz2) as ot2 on pol.id_str_from_otgruz2=ot2.item_id "
//                + /*" left join (select cw,ncw from nsi_cd) as cd on cd.cw=ot2.rzm_marh "+*/ " where doc_id=(select item_id from otgruz1 where ndoc=?)";
//        String pryazhaQuery = " select cd.ncw as cvet, kolvo_upack as gruzm, massa_surov as massa from _otgruz_polotno as pol "
//                + " left join (select item_id,doc_id,ncw,kod_izd from otgruz2) as ot2 on pol.id_str_from_otgruz2=ot2.item_id "
//                + " left join (select rzm_print,kod1 from nsi_sd) as sd on sd.kod1=ot2.kod_izd "
//                + " left join (select cw,ncw from nsi_cd) as cd on cd.cw=sd.rzm_print "
//                + " where doc_id=(select item_id from otgruz1 where ndoc=?) ";
//        String query = " select sar, nar, fas, ngpr, narp, ncw, rst, rzm, srt, ean, sum(kol), cena, sum(summa), nds, sum(summa_nds), "
//                + "sum(itogo),sum(kkr), sum(mas*kol) as massa,preiscur,kpl,rzm_print from (select item_id,kpl from otgruz1 where ndoc = ?)as ot1 "
//                + " left join (select kod_izd, doc_id, (kol*kol_in_upack) as kol, kol as kkr, cena, summa, summa_nds, itogo, eancode, preiscur, ncw from otgruz2)as ot2 on ot2.doc_id = ot1.item_id "
//                + " left join (select kod1, kod, rzm_print, nds, ean, massa as mas, srt, rst, rzm from nsi_sd)as sd on sd.kod1 = ot2.kod_izd "
//                + " left join (select kod, sar, nar, fas, ngpr, narp from nsi_kld)as kld on sd.kod = kld.kod " + //,kkr
//                " group by kpl,sar, nar, fas, ngpr, narp, ncw, rst, rzm, srt, ean, cena, nds,preiscur,rzm_print "
//                + " order by sar, nar, fas, rst, rzm, ncw ";
//        try {
//            updateSumTTN(nomer);
//            ps = conn.prepareStatement(query);
//            ps.setString(1, nomer);
//            rs = ps.executeQuery();
//            ps2 = conn.prepareStatement(polotnoQuery);
//            ps2.setString(1, nomer);
//            rs2 = ps2.executeQuery();
//            ps3 = conn.prepareStatement(pryazhaQuery);
//            ps3.setString(1, nomer);
//            rs3 = ps3.executeQuery();
//
//            while (rs.next()) {
//                rs2.next();
//                rs3.next();
//                HashMap<String, Object> m = new HashMap();
//                m.put("sar", rs.getInt(1));
//                m.put("nar", rs.getString(2).trim());
//                if (m.get("sar").toString().trim().equals("43141201") && rs.getInt(20) == 9904) {
//                    m.put("ngpr", rs.getString(4).trim() + " Х/Б");
//                } else if (m.get("sar").toString().trim().equals("41141341") && rs.getInt(20) == 9904) {
//                    m.put("ngpr", rs.getString(4).trim() + " (ТЕЛЬНЯШКА)");
//                } else {
//                    m.put("ngpr", rs.getString(4).trim());
//                }
//                m.put("narp", rs.getLong(5));
//                m.put("rst", rs.getInt(7));
//                m.put("cena", rs.getFloat(12));
//                if (m.get("sar").toString().startsWith("47")) {
//                    m.put("type_prod", "Полотно");
//                    m.put("massa", rs2.getDouble("massa"));
//                    m.put("rzm_print", "0");
//                    m.put("kol", rs2.getDouble("massa"));
//                    m.put("ncw", rs2.getString("cvet"));
//                    m.put("fas", " ");
//                    m.put("kkr", rs2.getDouble("gruzm"));
//                }
//                if (m.get("sar").toString().startsWith("470")) {
//                    m.put("type_prod", "Пряжа");
//                    m.put("massa", rs3.getFloat("massa"));
//                    m.put("rzm", "0");
//                    m.put("kol", rs3.getString("massa"));
//                    m.put("ncw", rs3.getString("cvet"));
//                    m.put("fas", " ");
//                    m.put("kkr", rs3.getString("gruzm"));
//                }
//                if (m.get("sar").toString().startsWith("48")) {
//                    m.put("type_prod", "Отходы трикотажного производства");
//                    m.put("rzm", rs.getInt(8));
//                    m.put("kol", rs.getInt(11));
//                    m.put("massa", rs.getInt(18));
//                    m.put("ncw", rs.getString(6).trim());
//                    m.put("fas", rs.getInt(3));
//                    m.put("kkr", rs.getInt(17));
//                }
//                if (!m.get("sar").toString().startsWith("47") && !m.get("sar").toString().startsWith("48")) {
//                    m.put("type_prod", "Трикотажные,чулочно-носочные изделия");
//                    m.put("rzm", rs.getInt(8));
//                    m.put("kol", rs.getInt(11));
//                    m.put("massa", rs.getFloat(18));
//                    m.put("ncw", rs.getString(6).trim());
//                    m.put("fas", rs.getInt(3));
//                    m.put("kkr", rs.getInt(17));
//                }
//                m.put("srt", rs.getString(9).trim());
//                m.put("ean", rs.getString(10).trim());
//                m.put("summa", rs.getDouble(13));
//                m.put("nds", rs.getFloat(14));
//                m.put("summa_nds", rs.getDouble(15));
//                m.put("itogo", rs.getDouble(16));
//                m.put("preiscur", rs.getString(19).trim());
//                if (rs.getString("rzm_print") != null && !m.get("sar").toString().startsWith("47")) {
//                    m.put("rzm_print", rs.getString("rzm_print").trim());
//                }
//                getIzm(m);
//                v.add(m);
//            }
//        } catch (Exception e) {
//            log.error("Ошибка в SkladDB getPrilojenieTTN(String nomer) ", e);
//            System.err.println("Ошибка в SkladDB getPrilojenieTTN(String nomer)" + e.getMessage());
//        }
//        return v;
//    }

    public ArrayList getPrilojenieTTN(String nomer, float nds) {
        ArrayList<HashMap> v = new ArrayList();
        boolean bol = true;
        BigDecimal n;
        PreparedStatement ps2;
        ResultSet rs2;
        PreparedStatement ps3;
        ResultSet rs3;
        int kid = 0;
        String polotnoQuery = " select cd.ncw as cvet, kolvo_upack as gruzm, (massa_surov) as massa from _otgruz_polotno as pol "
                + " left join (select rzm_marh,item_id,doc_id from otgruz2) as ot2 on pol.id_str_from_otgruz2=ot2.item_id "
                + " left join (select cw,ncw from nsi_cd) as cd on cd.cw=ot2.rzm_marh  "
                + " where doc_id=(select item_id from otgruz1 where ndoc=?) ";

        String pryazhaQuery = " select cd.ncw as cvet,kolvo_upack as gruzm, massa_surov as massa from _otgruz_polotno as pol "
                + " left join (select rzm_marh,item_id,doc_id,kod_izd from otgruz2) as ot2 on pol.id_str_from_otgruz2=ot2.item_id "
                + " left join (select rzm_print,kod1 from nsi_sd) as sd on sd.kod1=ot2.kod_izd "
                + " left join (select cw,ncw from nsi_cd) as cd on cd.cw=ot2.rzm_marh "
                + " where doc_id=(select item_id from otgruz1 where ndoc=?) ";

        String query = " select sar, nar, fas, ngpr, narp, ncw, rst, rzm, srt, ean, sum(kol), cena, sum(summa), nds, sum(summa_nds), "
                + "sum(itogo),sum(kkr), sum(mas*kol) as massa,preiscur,kpl,rzm_print from (select item_id,kpl from otgruz1 where ndoc = ?)as ot1 "
                + " left join (select kod_izd, doc_id, (kol*kol_in_upack) as kol, kol as kkr, cena, summa, summa_nds, itogo, eancode, preiscur, ncw from otgruz2)as ot2 on ot2.doc_id = ot1.item_id "
                + " left join (select kod1, kod, rzm_print, nds, ean, massa as mas, srt, rst, rzm from nsi_sd)as sd on sd.kod1 = ot2.kod_izd "
                + " left join (select kod, sar, nar, fas, ngpr, narp from nsi_kld)as kld on sd.kod = kld.kod " + //,kkr
                " group by kpl,sar, nar, fas, ngpr, narp, ncw, rst, rzm, srt, ean, cena, nds,preiscur,rzm_print "
                + " order by sar, nar, fas, rst, rzm, ncw ";
        try {
            updateSumTTN(nomer);
            ps = conn.prepareStatement(query);
            ps.setString(1, nomer);
            rs = ps.executeQuery();
            ps2 = conn.prepareStatement(polotnoQuery);
            ps2.setString(1, nomer);
            rs2 = ps2.executeQuery();
            ps3 = conn.prepareStatement(pryazhaQuery);
            ps3.setString(1, nomer);
            rs3 = ps3.executeQuery();
            int cr = 0, cr1 = 0;
            boolean fl1 = false, fl2 = false;
            while (rs.next()) {

                while (rs2.next() && cr == 0 && !fl1) {
                    cr++;
                }
                while (rs3.next() && cr1 == 0 && !fl2) {
                    cr1++;
                }
                if (cr != 0) {
                    rs2 = ps2.executeQuery();
                    rs2.next();
                    cr--;
                    fl1 = true;
                }
                if (cr1 != 0) {
                    rs3 = ps3.executeQuery();
                    rs3.next();
                    cr1--;
                    fl2 = true;
                }

                HashMap<String, Object> m = new HashMap();
                m.put("sar", rs.getInt(1));
                m.put("nar", rs.getString(2).trim());
                if (m.get("sar").toString().trim().equals("43141201") && rs.getInt(20) == 9904) {
                    m.put("ngpr", rs.getString(4).trim() + " Х/Б");
                } else if (m.get("sar").toString().trim().equals("41141341") && rs.getInt(20) == 9904) {
                    m.put("ngpr", rs.getString(4).trim() + " (ТЕЛЬНЯШКА)");
                } else {
                    m.put("ngpr", rs.getString(4).trim());
                }
                m.put("narp", rs.getLong(5));
                m.put("rst", rs.getInt(7));
                m.put("cena", rs.getFloat(12));
                if (m.get("sar").toString().startsWith("47")) {
                    m.put("type_prod", "Полотно");
                    m.put("massa", rs2.getDouble("massa"));
                    m.put("rzm_print", "0");
                    m.put("kol", rs2.getDouble("massa"));
                    m.put("ncw", rs2.getString("cvet"));
                    m.put("fas", " ");
                    m.put("kkr", rs2.getDouble("gruzm"));
                }
                if (m.get("sar").toString().startsWith("470")) {
                    m.put("type_prod", "Пряжа");
                    m.put("massa", rs3.getFloat("massa"));
                    m.put("rzm", "0");
                    m.put("kol", rs3.getString("massa"));
                    m.put("ncw", rs3.getString("cvet"));
                    m.put("fas", " ");
                    m.put("kkr", rs3.getString("gruzm"));
                }
                if (m.get("sar").toString().startsWith("48")) {
                    m.put("type_prod", "Набор лоскута");
                    m.put("rzm_print", rs.getString("rzm_print"));
                    m.put("kol", rs.getInt(11));
                    m.put("massa", rs.getInt(18));
                    m.put("ncw", rs.getString(6).trim());
                    m.put("fas", rs.getInt(3));
                    m.put("kkr", rs.getInt(17));
                }
                if (!m.get("sar").toString().startsWith("47") && !m.get("sar").toString().startsWith("48")) {
                    m.put("type_prod", "Трикотажные,чулочно-носочные изделия");
                    m.put("rzm_print", rs.getString("rzm_print"));
                    m.put("kol", rs.getInt(11));
                    m.put("massa", rs.getFloat(18));
                    m.put("ncw", rs.getString(6).trim());
                    m.put("fas", rs.getInt(3));
                    m.put("kkr", rs.getInt(17));
                }
                m.put("srt", rs.getString(9).trim());
                m.put("ean", rs.getString(10).trim());
                m.put("summa", rs.getDouble(13));
                m.put("nds", rs.getFloat(14));
                m.put("summa_nds", rs.getDouble(15));
                m.put("itogo", rs.getDouble(16));
                m.put("preiscur", rs.getString(19).trim());
                if (rs.getString("rzm_print") != null && !m.get("sar").toString().startsWith("47")) {
                    m.put("rzm_print", rs.getString("rzm_print").trim());
                }
                getIzm(m);
                v.add(m);
            }
        } catch (Exception e) {
            log.severe("Ошибка в SkladDB getPrilojenieTTN(String nomer) " + e.getMessage());
        }
        return v;
    }

    public ArrayList getPrilojenieTTN2(String nomer, float nds, float torgNadbavka) {
        ArrayList<HashMap> v = new ArrayList();
        int kid = 0;
        PreparedStatement ps3, ps4;
        ResultSet rs3, rs4;
        String polotnoQuery = " select cd.ncw as cvet, kolvo_upack as gruzm, (massa_surov) as massa from _otgruz_polotno as pol "
                + " left join (select rzm_marh,item_id,doc_id from otgruz2) as ot2 on pol.id_str_from_otgruz2=ot2.item_id "
                + " left join (select cw,ncw from nsi_cd) as cd on cd.cw=ot2.rzm_marh  "
                + " where doc_id=(select item_id from otgruz1 where ndoc=?) "
                + " order by pol.id_str_from_otgruz2 ";
        String pryazhaQuery = " select cd.ncw as cvet,kolvo_upack as gruzm, massa_surov as massa from _otgruz_polotno as pol "
                + " left join (select rzm_marh,item_id,doc_id,kod_izd from otgruz2) as ot2 on pol.id_str_from_otgruz2=ot2.item_id "
                + " left join (select rzm_print,kod1 from nsi_sd) as sd on sd.kod1=ot2.kod_izd "
                + " left join (select cw,ncw from nsi_cd) as cd on cd.cw=ot2.rzm_marh "
                + " where doc_id=(select item_id from otgruz1 where ndoc=?) "
                + " order by pol.id_str_from_otgruz2 ";
        String query = " select sar, nar, fas, ngpr, narp, ncw, rst, rzm, srt, ean, sum(kol), cena, sum(summa), nds, sum(summa_nds), "
                + " sum(itogo),sum(kkr), sum(mas) as massa,preiscur, klient_id, date, sum(summav), sum(summa_ndsv), sum(itogov), cenav,ot1.kpl,"
                + " rzm_print from (select item_id, klient_id, kpl, date from otgruz1 where ndoc = ?)as ot1 "
                + " left join (select kod_izd, doc_id, (kol*kol_in_upack) as kol, kol as kkr, cena, summa, summa_nds, itogo, eancode, preiscur, ncw, summav, summa_ndsv, itogov, cenav, nds from otgruz2)as ot2 on ot2.doc_id = ot1.item_id "
                + " left join (select kod1, kod, rzm_print, ean, massa as mas, srt, rst, rzm from nsi_sd)as sd on sd.kod1 = ot2.kod_izd "
                + " left join (select kod, sar, nar, fas, ngpr, narp from nsi_kld)as kld on sd.kod = kld.kod "
                + /* " where nar!='' "+*/ " group by kpl,sar, nar, fas, ngpr, narp, ncw, rst,rzm_print, rzm, srt, ean, cena, nds,preiscur, klient_id, date, cenav "
                + " order by sar, nar, fas, rst, rzm, ncw";

        try {
            //updateSumTTN(nomer);
            //changePriceToBel(nomer);
            ps = conn.prepareStatement(query);
            ps.setString(1, nomer);
            rs = ps.executeQuery();

            ps3 = conn.prepareStatement(polotnoQuery);
            ps3.setString(1, nomer);
            rs3 = ps3.executeQuery();
            ps4 = conn.prepareStatement(pryazhaQuery);
            ps4.setString(1, nomer);
            rs4 = ps4.executeQuery();
            int cr = 0, cr1 = 0;
            boolean fl1 = false, fl2 = false;
            while (rs.next()) {

                while (rs3.next() && cr == 0 && !fl1) {
                    cr++;
                }
                while (rs4.next() && cr1 == 0 && !fl2) {
                    cr1++;
                }
                if (cr != 0) {
                    rs3 = ps3.executeQuery();
                    rs3.next();
                    cr--;
                    fl1 = true;
                }
                if (cr1 != 0) {
                    rs4 = ps4.executeQuery();
                    rs4.next();
                    cr1--;
                    fl2 = true;
                }

                HashMap<String, Object> m = new HashMap();
                m.put("sar", rs.getInt("sar"));
                if (rs.getString("nar") != null) {
                    m.put("nar", rs.getString("nar").trim());
                } else {
                    m.put("nar", 0);
                }
                m.put("fas", rs.getInt(3));
                if (rs.getString("ngpr") != null) {
                    m.put("ngpr", rs.getString("ngpr").trim());
                } else {
                    m.put("ngpr", 0);
                }
                //m.put("ngpr", rs.getString(4).trim());


                if (m.get("sar").toString().startsWith("47")) {
                    m.put("type_prod", "Полотно");
                    m.put("massa", rs3.getDouble("massa"));
                    m.put("rzm_print", "0");
                    m.put("kol", rs3.getDouble("massa"));
                    m.put("ncw", rs3.getString("cvet"));
                    m.put("fas", " ");
                    m.put("kkr", rs3.getDouble("gruzm"));
                }
                if (m.get("sar").toString().startsWith("470")) {
                    m.put("type_prod", "Пряжа");
                    m.put("massa", rs4.getFloat("massa"));
                    m.put("rzm_print", "0");
                    m.put("kol", rs4.getString("massa"));
                    m.put("ncw", rs4.getString("cvet"));
                    m.put("fas", " ");
                    m.put("kkr", rs4.getString("gruzm"));
                }
                if (m.get("sar").toString().startsWith("48")) {
                    m.put("type_prod", "Набор лоскута");
                    m.put("rzm_print", rs.getString("rzm_print"));
                    m.put("kol", rs.getInt(11));
                    m.put("massa", rs.getInt(18));
                    m.put("ncw", rs.getString(6).trim());
                    m.put("fas", rs.getInt(3));
                    m.put("kkr", rs.getInt(17));
                }
                if (!m.get("sar").toString().startsWith("47") && !m.get("sar").toString().startsWith("48")) {
                    m.put("type_prod", "Трикотажные,чулочно-носочные изделия");
                    m.put("rzm_print", rs.getString("rzm_print"));
                    m.put("kol", rs.getInt(11));
                    m.put("massa", rs.getFloat(18));
                    m.put("ncw", rs.getString(6).trim());
                    m.put("fas", rs.getInt(3));
                    m.put("kkr", rs.getInt(17));
                }

                //rs.getString("rzm_print")


                m.put("narp", rs.getLong(5));
                //m.put("ncw", rs.getString(6).trim());
                m.put("rst", rs.getInt(7));
                m.put("rzm", rs.getInt(8));
                m.put("srt", rs.getString(9).trim());
                m.put("ean", rs.getString(10).trim());
                //m.put("kol", rs.getInt(11));
                m.put("cena", rs.getFloat(12));
                m.put("summa", rs.getFloat(13));
                m.put("nds", rs.getFloat(14));
                m.put("summa_nds", rs.getFloat(15));
                m.put("itogo", rs.getFloat(16));
                //m.put("kkr", rs.getInt(17));
                //m.put("massa", rs.getFloat(18)*rs.getInt(11));
                m.put("preiscur", rs.getString(19).trim());
                m.put("klient_id", rs.getString(20).trim());
                m.put("cena_rus", rs.getFloat(25));
                m.put("summa_rus", rs.getFloat(22));
                m.put("summa_nds_rus", rs.getFloat(23));
                m.put("summa_i_nds_rus", rs.getFloat(24));
                //m.put("rzm_print", rs.getString("rzm_print").trim());
                //if(rs.getString("rzm_print")!=null&& !m.get("sar").toString().startsWith("47"))
                //  m.put("rzm_print", rs.getString("rzm_print").trim());
//                if(m.get("sar").toString().startsWith("47")){
//                    m.put("type_prod", "Полотно");
//                }else{
//                    m.put("type_prod", "Трикотажные,чулочно-носочные изделия");
//                }
                if (torgNadbavka == -1) {
                    String query2;
                    PreparedStatement ps2;
                    ResultSet rs2;
                    int ya = Integer.parseInt(rs.getString("nar").trim().substring(0, 1));
                    Date d = new Date(System.currentTimeMillis());
                    int y = d.getYear();
                    y = y % 10;
                    if (ya <= y) {
                        ya += 10;
                    }
                    if (y < 2) {
                        y += 10;
                    }
                    y = y - 2;
                    //проверяем старый ли артикул
                    if (ya < y) {
                        query2 = "select top 1 tn, max(date) from _torg_nadbavka where art = 'старый' "
                                + "and date < ? group by tn order by max(date)";
                        ps2 = conn.prepareStatement(query2);
                        ps2.setDate(1, rs.getDate(21));
                        rs2 = ps2.executeQuery();
                        if (rs2.next()) {
                            m.put("torg_nadbavka", rs2.getInt(1));

                        }
                    } else {
                        //проверяем для всех клиентов с конкретными полями (артикулб, модель, сорт)
                        query2 = "select top 1 tn, max(date) from _torg_nadbavka where klient_id is null and art = ? "
                                + "and model = ? and sort = ? and date < ? group by tn order by max(date)";

                        ps2 = conn.prepareStatement(query2);
                        ps2.setString(1, rs.getString("sar").trim());
                        ps2.setInt(2, rs.getInt(3));
                        ps2.setString(3, rs.getString(9).trim());
                        ps2.setDate(4, rs.getDate(21));
                        rs2 = ps2.executeQuery();
                        if (rs2.next()) {
                            m.put("torg_nadbavka", rs2.getInt(1));
                        } else {
                            //проверяем ТН для конкретного клиента с
                            query2 = "select top 1 tn, max(date) from _torg_nadbavka where klient_id = ? and ? like art "
                                    + "and (model = ? or model is null) and sort = ? and date < ? group by art, tn order by art, max(date)";
                            ps2 = conn.prepareStatement(query2);
                            ps2.setInt(1, rs.getInt("klient_id"));
                            ps2.setString(2, rs.getString("sar").trim());
                            ps2.setInt(3, rs.getInt(3));
                            ps2.setString(4, rs.getString(9).trim());
                            ps2.setDate(5, rs.getDate(21));
                            rs2 = ps2.executeQuery();
                            if (rs2.next()) {
                                m.put("torg_nadbavka", rs2.getInt(1));
                            } else {
                                int q = Integer.parseInt(rs.getString("sar").trim().substring(2, 3));
                                if (q == 3 || q == 6) {
                                    query2 = "select top 1 tn, max(date) from _torg_nadbavka "
                                            + "where art = 'новый д' and date < ? group by tn order by max(date)";
                                } else {
                                    query2 = "select top 1 tn, max(date) from _torg_nadbavka where art = 'новый в' "
                                            + "and date < ? group by tn order by max(date)";
                                }
                                ps2 = conn.prepareStatement(query2);
                                ps2.setDate(1, rs.getDate(21));
                                rs2 = ps2.executeQuery();
                                if (rs2.next()) {
                                    m.put("torg_nadbavka", rs2.getInt(1));
                                } else {
                                    System.out.println(rs.getString("klient_id").trim() + " -- " + rs.getString("sar").trim() + " -- " + rs.getInt("fas") + " -- " + rs.getString("srt").trim() + " -- " + rs.getDate("date"));
                                    m.put("torg_nadbavka", 0);
                                }
                            }
                        }
                    }
                } else {
                    m.put("torg_nadbavka", torgNadbavka);
                }
                getIzm(m);
                v.add(m);
            }
        } catch (Exception e) {
            log.severe("Ошибка в SkladDB getPrilojenieTTN2(String nomer) " + e);
            System.err.println("Ошибка в SkladDB getPrilojenieTTN2(String nomer)" + e.getMessage());
        }
        return v;
    }

    public ArrayList getPrilojenieTTNRus(String nomer, float nds, HashMap money) {
        ArrayList<HashMap> v = new ArrayList();
        PreparedStatement ps2;
        ResultSet rs2;
        PreparedStatement ps3, ps4;
        ResultSet rs3, rs4;
        String polotnoQuery = " select cd.ncw as cvet, kolvo_upack as gruzm, (massa_surov) as massa from _otgruz_polotno as pol "
                + " left join (select rzm_marh,item_id,doc_id from otgruz2) as ot2 on pol.id_str_from_otgruz2=ot2.item_id "
                + " left join (select cw,ncw from nsi_cd) as cd on cd.cw=ot2.rzm_marh  "
                + " where doc_id=(select item_id from otgruz1 where ndoc=?) ";
        /*+ " order by pol.id_str_from_otgruz2 ";*/
        String pryazhaQuery = " select cd.ncw as cvet,kolvo_upack as gruzm, massa_surov as massa from _otgruz_polotno as pol "
                + " left join (select rzm_marh,item_id,doc_id,kod_izd from otgruz2) as ot2 on pol.id_str_from_otgruz2=ot2.item_id "
                + " left join (select rzm_print,kod1 from nsi_sd) as sd on sd.kod1=ot2.kod_izd "
                + " left join (select cw,ncw from nsi_cd) as cd on cd.cw=ot2.rzm_marh "
                + " where doc_id=(select item_id from otgruz1 where ndoc=?) ";
        /*+ " order by pol.id_str_from_otgruz2 ";*/

        String query = " select sar, nar, fas, ngpr, narp, ncw, rst, rzm, srt, ean, sum(kol) as kol, cena, nds, "
                + "sum(kgm) as kgm, sum(mas*kol) as massa, preiscur, klient_id, date, cena,sum(summa) as summa,"
                + "sum(summa_nds) as summa_nds,sum(itogo) as itogo,cenav, sum(summav) as summav,"
                + "sum(summa_ndsv) as summa_ndsv,sum(itogov) as itogov,rzm_print "
                + "from (select item_id, klient_id, date from otgruz1 where ndoc = ?)as ot1 "
                + "left join (select kod_izd, doc_id, kol as kgm, (kol*kol_in_upack) as kol, "
                + "kol_in_upack, cena, eancode, preiscur, ncw, cenav, nds,summav,summa_ndsv,itogov,summa,summa_nds,itogo from otgruz2) "
                + "as ot2 on ot2.doc_id = ot1.item_id left join (select kod1, kod, rzm_print, ean, massa as mas, srt, rst, rzm, cnp from nsi_sd) "
                + "as sd on sd.kod1 = ot2.kod_izd left join (select kod, sar, nar, fas, ngpr, kkr, narp from nsi_kld)as kld on sd.kod = kld.kod "
                + "group by sar, nar, fas, ngpr, narp, ncw, rst, rzm, srt, ean, cena, cnp, nds,preiscur, klient_id, "
                + "date, cenav, kol_in_upack, cena,summa,summa_nds,itogo,cenav, summav,summa_ndsv,itogov,rzm_print "
                + "order by sar, nar, fas, rst, rzm, ncw ";
        try {
            ps = conn.prepareStatement(query);
            ps.setString(1, nomer);
            rs = ps.executeQuery();

            int x = 0; /*qweqw//wqewe/*
             int y =1; //qwe*/ //qweeeq*/


            ps3 = conn.prepareStatement(polotnoQuery);
            ps3.setString(1, nomer);
            rs3 = ps3.executeQuery();
            ps4 = conn.prepareStatement(pryazhaQuery);
            ps4.setString(1, nomer);
            rs4 = ps4.executeQuery();
            int cr = 0, cr1 = 0;
            boolean fl1 = false, fl2 = false;
            while (rs.next()) {

                while (rs3.next() && cr == 0 && !fl1) {
                    cr++;
                }
                while (rs4.next() && cr1 == 0 && !fl2) {
                    cr1++;
                }
                if (cr != 0) {
                    rs3 = ps3.executeQuery();
                    rs3.next();
                    cr--;
                    fl1 = true;
                }
                if (cr1 != 0) {
                    rs4 = ps4.executeQuery();
                    rs4.next();
                    cr1--;
                    fl2 = true;
                }

                HashMap<String, Object> m = new HashMap();
                m.put("sar", rs.getInt("sar"));
                m.put("nar", rs.getString("nar").trim());
                //m.put("fas", rs.getInt("fas"));
                m.put("ngpr", rs.getString("ngpr").trim());
                m.put("narp", rs.getLong("narp"));
                //m.put("ncw", rs.getString("ncw").trim());
                m.put("rst", rs.getInt("rst"));
                m.put("rzm", rs.getInt("rzm"));
                m.put("srt", rs.getString("srt").trim());
                m.put("ean", rs.getString("ean").trim());
                //m.put("kol", rs.getInt("kol"));
                m.put("nds", rs.getInt("nds"));


                if (m.get("sar").toString().startsWith("47")) {
                    m.put("type_prod", "Полотно");
                    m.put("massa", rs3.getDouble("massa"));
                    m.put("rzm_print", "0");
                    m.put("kol", rs3.getDouble("massa"));
                    m.put("ncw", rs3.getString("cvet"));
                    m.put("fas", " ");
                    m.put("kkr", rs3.getDouble("gruzm"));
                }
                if (m.get("sar").toString().startsWith("470")) {
                    m.put("type_prod", "Пряжа");
                    m.put("massa", rs4.getFloat("massa"));
                    m.put("rzm_print", "0");
                    m.put("kol", rs4.getString("massa"));
                    m.put("ncw", rs4.getString("cvet"));
                    m.put("fas", " ");
                    m.put("kkr", rs4.getString("gruzm"));
                }
                if (m.get("sar").toString().startsWith("48")) {
                    m.put("type_prod", "Набор лоскута");
                    m.put("rzm_print", rs.getString("rzm_print"));
                    m.put("kol", rs.getInt(11));
                    m.put("massa", rs.getInt(18));
                    m.put("ncw", rs.getString(6).trim());
                    m.put("fas", rs.getInt(3));
                    m.put("kkr", rs.getInt(17));
                }
                if (!m.get("sar").toString().startsWith("47") && !m.get("sar").toString().startsWith("48")) {
                    m.put("type_prod", "Трикотажные,чулочно-носочные изделия");
                    m.put("rzm_print", rs.getString("rzm_print"));
                    m.put("kol", rs.getString(11));
                    m.put("massa", rs.getString("massa"));
                    m.put("ncw", rs.getString(6).trim());
                    m.put("fas", rs.getInt(3));
                    m.put("kkr", rs.getInt("kgm"));
                }


                float kurs = Float.parseFloat(money.get("kurs").toString());
                BigDecimal n = new BigDecimal((rs.getFloat("cenav"))); /* Float.parseFloat(money.get("koef").toString()) * (100 - Float.parseFloat(money.get("skidka").toString())) / 100)*kurs);*/
                n = n.setScale(2, BigDecimal.ROUND_HALF_UP);
                m.put("cena_rus", rs.getFloat("cenav"));

                float cena = rs.getFloat("cenav");//
                float kolvo = rs.getFloat("kol");//


                //float sum = cena*kolvo;
                n = new BigDecimal(rs.getFloat("summav"));
                n = n.setScale(5, BigDecimal.ROUND_HALF_UP);
                n = n.setScale(4, BigDecimal.ROUND_HALF_UP);
                n = n.setScale(3, BigDecimal.ROUND_HALF_UP);
                n = n.setScale(2, BigDecimal.ROUND_HALF_UP);
                //m.put("summa_rus", n);
                m.put("summa_rus", rs.getFloat("summav"));
                //m.put("summa_nds_rus", rs.getFloat(23));
                //n = new BigDecimal(Float.parseFloat(m.get("summa_rus").toString())*Integer.parseInt(m.get("nds").toString())/100);
                n = new BigDecimal(rs.getFloat("summa_ndsv"));
                n = n.setScale(4, BigDecimal.ROUND_HALF_UP);
                n = n.setScale(3, BigDecimal.ROUND_HALF_UP);
                n = n.setScale(2, BigDecimal.ROUND_HALF_UP);
                //m.put("summa_nds_rus", n.floatValue());
                m.put("summa_nds_rus", rs.getFloat("summa_ndsv"));
                //m.put("summa_i_nds_rus", rs.getFloat(24));
                //n = new BigDecimal(Float.parseFloat(m.get("summa_rus").toString())+Float.parseFloat(m.get("summa_nds_rus").toString()));
                n = new BigDecimal(rs.getFloat("itogov"));
                n = n.setScale(2, BigDecimal.ROUND_HALF_UP);

                //m.put("summa_i_nds_rus", n);
                m.put("summa_i_nds_rus", rs.getFloat("itogov"));
//                n = new BigDecimal(Float.parseFloat(m.get("cena_rus").toString()) * Float.parseFloat(money.get("kurs_bel").toString()));
//
//                n = n.setScale(0, BigDecimal.ROUND_HALF_UP);
                m.put("cena", rs.getFloat("cena"));
                if (m.get("cena").equals("0")) {
                    n = new BigDecimal(Float.parseFloat(m.get("cena_rus").toString()) * kurs);
                    n = n.setScale(2, BigDecimal.ROUND_HALF_UP);
                    n = n.setScale(1, BigDecimal.ROUND_HALF_UP);
                    n = n.setScale(0, BigDecimal.ROUND_HALF_UP);
                    m.put("cena", n);
                }


                //n = new BigDecimal(rs.getFloat("kol") * rs.getFloat("cena"));
                //n = n.setScale(0, BigDecimal.ROUND_HALF_UP);
                m.put("summa", rs.getFloat("summa"));

                //m.put("summa_nds", rs.getFloat(15));
                //n = new BigDecimal(Float.parseFloat(m.get("summa").toString()) * Float.parseFloat(m.get("nds").toString()) / 100);
//                n = n.setScale(3, BigDecimal.ROUND_HALF_UP);
                //n = n.setScale(2, BigDecimal.ROUND_HALF_UP);
                //n = n.setScale(1, BigDecimal.ROUND_HALF_UP);
                // n = n.setScale(0, BigDecimal.ROUND_HALF_UP);
                m.put("summa_nds", rs.getFloat("summa_nds"));
                //m.put("itogo", rs.getFloat(16));
                //n = new BigDecimal(Float.parseFloat(m.get("summa").toString()) + Float.parseFloat(m.get("summa_nds").toString()));
                //n = n.setScale(0, BigDecimal.ROUND_HALF_UP);
                m.put("itogo", rs.getFloat("itogo"));

                //m.put("kkr", rs.getInt("kgm"));
                //n = new BigDecimal(rs.getFloat("massa"));
                //n = n.setScale(2, BigDecimal.ROUND_HALF_UP);
                //m.put("massa", n);
                m.put("preiscur", rs.getString("preiscur").trim());
                m.put("klient_id", rs.getString("klient_id").trim());
                //m.put("rzm_print", rs.getString("rzm_print").trim());
                String query2;

                int ya = Integer.parseInt(rs.getString(2).trim().substring(0, 1));
                Date d = new Date(System.currentTimeMillis());
                int y = d.getYear();
                y = y % 10;
                if (ya <= y) {
                    ya += 10;
                }
                if (y < 2) {
                    y += 10;
                }
                y = y - 2;

                //проверяем старый ли артикул
                if (ya < y) {
                    query2 = "select top 1 tn, max(date) from _torg_nadbavka where art = 'старый' and date < ? group by tn order by max(date)";
                    ps2 = conn.prepareStatement(query2);
                    ps2.setDate(1, rs.getDate("date"));
                    rs2 = ps2.executeQuery();
                    if (rs2.next()) {
                        m.put("torg_nadbavka", rs2.getInt(1));
                    }
                } else {
                    query2 = "select top 1 tn, max(date) from _torg_nadbavka where klient_id is null and art = ? and model = ? and sort = ? and date < ? group by tn order by max(date)";

                    ps2 = conn.prepareStatement(query2);
                    ps2.setString(1, rs.getString("sar").trim());
                    ps2.setInt(2, rs.getInt("fas"));
                    ps2.setString(3, rs.getString("srt").trim());
                    ps2.setDate(4, rs.getDate("date"));

                    rs2 = ps2.executeQuery();
                    if (rs2.next()) {
                        m.put("torg_nadbavka", rs2.getInt(1));
                    } else {
                        query2 = "select top 1 tn, max(date) from _torg_nadbavka where klient_id = ? and ? like art and (model = ? or model is null) and sort = ? and date < ? group by tn order by max(date)";
                        //query2 = "select top 1 tn, max(date) from _torg_nadbavka where klient_id = " + rs.getString(20).trim()+ " and '" +rs.getString(2).trim()+"' like art and (model = "+rs.getInt(3)+" or model is null) and sort = "+rs.getString(9).trim()+" and date < "+rs.getDate(21)+" group by tn order by max(date)";
                        ps2 = conn.prepareStatement(query2);
                        ps2.setString(1, rs.getString("klient_id").trim());
                        ps2.setString(2, rs.getString("sar").trim());
                        ps2.setInt(3, rs.getInt("fas"));
                        ps2.setString(4, rs.getString("srt").trim());
                        ps2.setDate(5, rs.getDate("date"));
                        rs2 = ps2.executeQuery();
                        //System.out.println(query2);
                        if (rs2.next()) {
                            m.put("torg_nadbavka", rs2.getInt(1));
                        } else {
                            int q = Integer.parseInt(rs.getString("sar").trim().substring(2, 3));
                            if (q == 3 || q == 6) {
                                query2 = "select top 1 tn, max(date) from _torg_nadbavka where art = 'новый д' and date < ? group by tn order by max(date)";
                            } else {
                                query2 = "select top 1 tn, max(date) from _torg_nadbavka where art = 'новый в' and date < ? group by tn order by max(date)";
                            }
                            ps2 = conn.prepareStatement(query2);
                            ps2.setDate(1, rs.getDate("date"));
                            rs2 = ps2.executeQuery();
                            if (rs2.next()) {
                                m.put("torg_nadbavka", rs2.getInt(1));
                            } else {

                                System.out.println(rs.getString("klient_id").trim() + " -- " + rs.getString("sar").trim() + " -- " + rs.getInt("fas") + " -- " + rs.getString("srt").trim() + " -- " + rs.getDate("date"));
                                m.put("torg_nadbavka", 0);
                            }
                            // JOptionPane.showMessageDialog(null, "Не определена Торг Надб для nar=" + rs.getString(2).trim() + ": ", "Ошибка!!!", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
                if (m.get("sar").toString().startsWith("48")) {
                    m.put("izm", "кг.");
                }
                if (m.get("sar").toString().startsWith("43")) {
                    m.put("izm", "пар");
                }
                if (!m.get("sar").toString().startsWith("43") && !m.get("sar").toString().startsWith("48")) {
                    m.put("izm", "шт.");
                }

                //    izm=m.get("izm").toString();
                v.add(m);
                getIzm(m);
            }
        } catch (Exception e) {
            log.severe("Ошибка в SkladDB getPrilojenieTTNRus(String nomer) " + e);
            System.err.println("Ошибка в SkladDB getPrilojenieTTNRus(String nomer)" + e.getMessage());
        }
        return v;
    }

//    public String getGGR(String sar, int vid) {
//        String ggr = "";
//
//        try {
//            String query = " select znach from nsi_prop where vid = ? and sar like ? and sgpr = 0 and tip = 'Удостоверение ГГР'";
//            ps = conn.prepareStatement(query);
//
//            while (sar.length() > 1) {
//                ps.setInt(1, vid);
//                ps.setString(2, sar + "%");
//                rs = ps.executeQuery();
//                if (rs.next()) {
//                    ggr = rs.getString(1).trim();
//                    break;
//                }
//                sar = sar.substring(0, sar.length() - 2);
//            }
//        } catch (Exception e) {
//            log.severe("Ошибка при выполнении функции getGGR" + e);
//            System.out.println("Ошибка при выполнении функции getGGR: " + e);
//        }
//
//        return ggr;
//    }

    public String getSertifikat(String sar, int vid) {
        String sertifikat = "";

        String query = " select znach from nsi_prop where vid = ? and sar like ? and sgpr = 0 and tip = 'Сертификат'";
        String query2 = " select znach from nsi_prop where vid = ? and sar like ? and sgpr = 4 and tip = 'Сертификат'";

        try {
            ps = conn.prepareStatement(query);
            PreparedStatement ps2 = conn.prepareStatement(query2);
            ResultSet rs2;
            while (sar.length() > 1) {
                ps.setInt(1, vid);
                ps.setString(2, sar + "%");
                rs = ps.executeQuery();
                if (rs.next()) {
                    sertifikat = rs.getString(1).trim();
                    break;
                }
                ps2.setInt(1, vid);
                ps2.setString(2, sar + '%');
                rs2 = ps2.executeQuery();
                if (rs2.next()) {
                    sertifikat = rs2.getString(1).trim();
                    break;
                }
                sar = sar.substring(0, sar.length() - 1);
                System.out.println(sar);
            }
        } catch (Exception e) {
            log.severe("Ошибка при выполнении функции getSertifikat" + e.getMessage());
        }
        return sertifikat;
    }

//    public void createSkladNakl(String nn, String pathSave, boolean valuta, boolean diskount) {
//        String query = "";
//        //setOtgruzAddition(nn);
//        if (!valuta && !diskount) {
//            query = " {? = call _nakladniev_firm_mag (?) }";
//        }
//        } else {
//            query = " {? = call _nakladniev_firm_mag (?) }";
//        }
//        // Скидка !!!!!!!!!!
//        if (!valuta && diskount) {
//            query = " {? = call _nakladniev_firm_mag_skid (?) }";
//        }
//        else {
//            query = " {? = call _nakladniev_firm_mag_skid (?) }";
//        }
//        String dogovor = new String("");
//        String param;
//        DBF dbf = null;
//        if (pathSave.equals("".toString()) || pathSave == null) {
//            dbf = new DBF(3, nn, "");
//        } else {
//            dbf = new DBF(3, nn, pathSave);
//        }
//        long n = -1;
//        try {
//            //connect.setAutoCommit(false);
//            ps = conn.prepareStatement("select item_id from otgruz1 where ndoc = ?");
//            ps.setString(1, nn);
//            ResultSet rs = ps.executeQuery();
//            rs.next();
//            n = rs.getLong(1);
//
//            ps = conn.prepareStatement("select DOGOVOR from ttn where DOC_ID = ?");
//            ps.setLong(1, n);
//
//            rs = ps.executeQuery();
//            if (rs.next()) {
//                dogovor = new String(rs.getString(1).trim());
//                if (dogovor.length() > 50) {
//                    dogovor = dogovor.substring(0, 50);
//                }
//            }
//            //else dogovor = new String("");
//            if (dogovor.length() < 1) {
//                dogovor = "";
//            }
//            cs = conn.prepareCall(query);
//            cs.setLong(2, n);
//            cs.registerOutParameter(1, Types.OTHER);
//            rs = cs.executeQuery();
//
//            dbf.conn();
//            while (rs.next()) {
//                System.out.println("Позиция");
//                Object[] v = new Object[29];
//                v[0] = "ОАО \"8МАРТА\"";
//                v[1] = nn;
//                v[2] = rs.getObject("fas");
//                v[3] = rs.getString("nar").trim();
//                param = rs.getString("ngpr").toString().trim();
//                if (param.length() > 45) {
//                    param = param.substring(0, 45);
//                }
//                v[4] = param;
//
//                v[5] = rs.getObject("rzm").toString().trim();
//                String str = rs.getObject("srt").toString().trim();
//                if (str.length() > 8) {
//                    str = str.substring(0, 7);
//                }
//                v[6] = str;
//
//                param = rs.getString("ncw").toString().trim();
//                if (param.length() > 25) {
//                    param = param.substring(0, 25);
//                }
//                v[7] = param;
//
//                v[8] = rs.getObject("kol_all");
//                v[9] = rs.getFloat("cena");
//                v[10] = rs.getObject("summa");
//                v[11] = rs.getObject("nds");
//                v[12] = rs.getObject("summa_nds");
//                v[13] = rs.getObject("itogo");
//                v[14] = rs.getObject("eancode");
//                v[15] = "РБ";
//
//                param = rs.getString("sertifikat").toString().trim();
//                if (param.length() > 80) {
//                    param = param.substring(0, 80);
//                }
//                v[16] = param;
//                param = rs.getString("ggr").toString().trim();
//                if (param.length() > 80) {
//                    param = param.substring(0, 80);
//                }
//                v[17] = param;
//
//                v[18] = rs.getObject("narp");
//                v[19] = String.valueOf(rs.getObject("massa_ed"));
//                v[20] = String.valueOf(rs.getObject("massa"));
//                v[21] = dogovor;
//                param = rs.getString("preiscur").toString().trim();
//                if (param.length() > 50) {
//                    param = param.substring(0, 50);
//                }
//                v[22] = param;
//                v[23] = rs.getString("sar").trim();
//                String tn = rs.getObject("torg_nadb") != null ? rs.getString("torg_nadb").trim() : "0";
//                String rc = rs.getObject("rozn_cena") != null ? rs.getString("rozn_cena").trim() : "0";
//                v[24] = tn;
//                v[25] = rc;
//                v[26] = rs.getObject("ptk") != null ? rs.getString("ptk").trim() : "0";
//                v[27] = rs.getObject("cena_uch") != null ? rs.getFloat("cena_uch") : 0;
//                // Получаем ставку НДС для изделия
//                float nds = 0.0f;
//
//                try {
//                    nds = getActualNDS(rs.getString("sar"), rs.getString("nar"), 0, nn);
//                } catch (Exception e) {
//                    System.out.println("Ошибка получения значенияч НДС");
//                }
//
//                v[28] = nds;
//
//                dbf.write(v);
//            }
//
//        } catch (Exception e) {
//            System.err.println(e);
//            return;
//        } finally {
//            if (dbf != null) {
//                dbf.disconn();
//            }
//        }
//    }
//
//    /**
//     * Пересчёт бел.цены от цены в валюте
//     *
//     * @param ks     -- коэф. скидка
//     * @param skidka -- оптовая скидка
//     * @param kurs   -- курс НБРБ
//     */
//    void changePrice(String ttn, float ks, float skidka, float kurs, float kursnb, float nds, String val) {
//        int kod_izd, kpl = 0;
//        ArrayList listIzdel = new ArrayList();
//        ArrayList listSpecCen = new ArrayList();
//        HashMap hmm = null;
//        float nds2;
//        double cnp = -1, t = 0;
//        String kp = "1.002", km = "1.01";
//        try {
//            setAutoCommit(false);
//            String query = "select sd.srt,kld.nar,kld.fas,kpl, ot2.item_id, kod_izd, kol, kol_in_upack, nds,rzm_marh, rst_marh,doc_id,ncw,cenav from otgruz2 as ot2 "
//                    + "left join (select kpl,item_id from otgruz1 where ndoc=?) as ot1 on ot2.doc_id=ot1.item_id "
//                    + "left join (SELECT kod, kod1,srt FROM nsi_sd) AS sd ON sd.kod1=ot2.kod_izd "
//                    + "left join (SELECT kod, fas, nar FROM nsi_kld) AS kld ON sd.kod=kld.kod "
//                    + "where doc_id=ot1.item_id ";
//            ps = conn.prepareStatement(query);
//            ps.setString(1, ttn);
//            rs = ps.executeQuery();
//            while (rs.next()) {
//                hmm = new HashMap();
//                hmm.put("kpl", rs.getInt("kpl"));
//                hmm.put("item_id", rs.getInt("item_id"));
//                hmm.put("kod_izd", rs.getInt("kod_izd"));
//                hmm.put("kol", rs.getInt("kol"));
//                hmm.put("kol_in_upack", rs.getInt("kol_in_upack"));
//                hmm.put("nds", rs.getInt("nds"));
//                hmm.put("rzm_marh", rs.getInt("rzm_marh"));
//                hmm.put("rst_marh", rs.getInt("rst_marh"));
//                hmm.put("doc_id", rs.getInt("doc_id"));
//                hmm.put("ncw", rs.getString("ncw"));
//                hmm.put("cenav", rs.getDouble("cenav"));
//                hmm.put("fas", rs.getInt("fas"));
//                hmm.put("nar", rs.getString("nar"));
//                hmm.put("srt", rs.getInt("srt"));
//                listIzdel.add(hmm);
//            }
//            rs.close();
//            query = "SELECT kpl FROM otgruz1 WHERE ndoc=?";
//            ps = conn.prepareStatement(query);
//            ps.setString(1, ttn);
//            rs = ps.executeQuery();
//            if (rs.next()) {
//                kpl = rs.getInt(1);
//            }
//            query = " SELECT skod_kontragenta, smodel,sart,ssort, srazmer,srazmer_end, srost,srost_end, scena,date_insert_record "
//                    + "FROM _speccena AS ot2 WHERE date_insert_record in (SELECT TOP 1 date_insert_record FROM _speccena AS ot1 "
//                    + "WHERE ot2.smodel=ot1.smodel and ot2.srazmer=ot1.srazmer and ot2.srost=ot1.srost and ot1.sart=ot2.sart "
//                    + "and ot2.skod_kontragenta=ot1.skod_kontragenta "
//                    + "ORDER BY date_insert_record DESC) and skod_kontragenta=?"
//                    + " order by skod_kontragenta, smodel,sart,srazmer,srazmer_end, srost,srost_end, scena,date_insert_record ";
//            ps = conn.prepareStatement(query);
//            ps.setInt(1, kpl);
//            rs = ps.executeQuery();
//            while (rs.next()) {
//                hmm = new HashMap();
//                hmm.put("skod_kontragenta", rs.getInt("skod_kontragenta"));
//                hmm.put("smodel", rs.getInt("smodel"));
//                hmm.put("sart", rs.getString("sart"));
//                hmm.put("ssort", rs.getInt("ssort"));
//                hmm.put("srazmer", rs.getInt("srazmer"));
//                hmm.put("srazmer_end", rs.getInt("srazmer_end"));
//                hmm.put("srost", rs.getInt("srost"));
//                hmm.put("srost_end", rs.getInt("srost_end"));
//                hmm.put("scena", rs.getDouble("scena"));
//                hmm.put("date_insert_record", rs.getDate("date_insert_record"));
//                listSpecCen.add(hmm);
//            }
//            rs.close();
//
//            for (int i = 0; i < listIzdel.size(); i++) {
//                cnp = -1;
//                hmm = new HashMap();
//                hmm = (HashMap) listIzdel.get(i);
//                kod_izd = Integer.valueOf(hmm.get("kod_izd").toString());
//                if (nds == -1) {
//                    nds2 = getActualNDS(kod_izd);
//                } else {
//                    nds2 = nds;
//                }
//
//                for (int j = 0; j < listSpecCen.size(); j++) {
//                    HashMap x = new HashMap();
//                    x = (HashMap) listSpecCen.get(j);
//                    if ((hmm.get("fas").toString().trim().equals(x.get("smodel").toString().trim()))
//                            && (hmm.get("srt").toString().trim().equals(x.get("ssort").toString().trim()))
//                            && (hmm.get("nar").toString().trim().equals(x.get("sart").toString().trim()))
//                            && (Integer.valueOf(hmm.get("rzm_marh").toString().trim()) <= Integer.valueOf(x.get("srazmer_end").toString().trim())
//                            && Integer.valueOf(hmm.get("rzm_marh").toString().trim()) >= Integer.valueOf(x.get("srazmer").toString().trim()))
//                            && (Integer.valueOf(hmm.get("rst_marh").toString().trim()) <= Integer.valueOf(x.get("srost_end").toString().trim())
//                            && Integer.valueOf(hmm.get("rst_marh").toString().trim()) >= Integer.valueOf(x.get("srost").toString().trim()))) {
//                        cnp = Double.valueOf(x.get("scena").toString().trim());
//                        if (kpl == 1527 && cnp != -1) {
//                            cnp = cnp * (double) ks;
//                        }
//                    }
//                }
//                if (cnp == -1) {
//                    query = "select cnp from nsi_sd where kod1 = ?";
//                    ps = conn.prepareStatement(query);
//                    ps.setInt(1, kod_izd);
//                    rs = ps.executeQuery();
//                    if (rs.next()) {
//                        cnp = rs.getDouble("cnp");
//                    }
//                    cnp = cnp * ks * (100 - skidka) / 100;
//                }
//
//                BigDecimal n = new BigDecimal(cnp);
//                n = n.setScale(2, BigDecimal.ROUND_HALF_UP);
//                cnp = n.doubleValue();
//
//                query = "update otgruz2 set cena = ?, summa = ?, nds = ?, summa_nds = ?, itogo = ?, "
//                        + " cenav = ?, summav = ?, summa_ndsv = ?, itogov = ? where item_id = ?";
//                ps = conn.prepareStatement(query);
//
//                if (!val.trim().equals("RUB")) {
//                    n = new BigDecimal(cnp / kurs);
//                    n = n.setScale(2, BigDecimal.ROUND_HALF_UP);
//                    ps.setDouble(6, n.doubleValue());
//                } else {
//                    ps.setDouble(6, cnp);
//                }
//
//                cnp = n.doubleValue();
//                double cena = cnp;
//                double kol = Integer.valueOf(hmm.get("kol").toString()) * Integer.valueOf(hmm.get("kol_in_upack").toString());
//                n = new BigDecimal(cena * kol);
//                //n = n.setScale(5, BigDecimal.ROUND_HALF_UP);
//                n = n.setScale(4, BigDecimal.ROUND_HALF_UP);
//                n = n.setScale(3, BigDecimal.ROUND_HALF_UP);
//                n = n.setScale(2, BigDecimal.ROUND_HALF_UP);
//                ps.setDouble(7, n.doubleValue());
//                t = n.doubleValue();
//
//                n = new BigDecimal(t * (nds2 / 100));
//                //n = new BigDecimal(n.doubleValue()* Integer.valueOf(hmm.get("kol").toString())*Integer.valueOf(hmm.get("kol_in_upack").toString()));
//                n = n.setScale(3, BigDecimal.ROUND_HALF_UP);
//                n = n.setScale(2, BigDecimal.ROUND_HALF_UP);
//                ps.setDouble(8, n.doubleValue());
//
//                ps.setDouble(9, t + n.doubleValue());
//
//                ps.setInt(10, Integer.valueOf(hmm.get("item_id").toString()));
//
//
//                n = new BigDecimal(cnp * kursnb);
//                n = n.setScale(0, BigDecimal.ROUND_HALF_UP);
//                ps.setDouble(1, n.doubleValue());
//                t = n.doubleValue();
//
//                n = new BigDecimal(t * Integer.valueOf(hmm.get("kol").toString()) * Integer.valueOf(hmm.get("kol_in_upack").toString()));
//                n = n.setScale(0, BigDecimal.ROUND_HALF_UP);
//                ps.setDouble(2, n.doubleValue());
//                t = n.intValue();
//
//                ps.setDouble(3, nds2);
//
//                n = new BigDecimal(t * nds2 / 100);
//                n = n.setScale(3, BigDecimal.ROUND_HALF_UP);
//                n = n.setScale(2, BigDecimal.ROUND_HALF_UP);
//                n = n.setScale(1, BigDecimal.ROUND_HALF_UP);
//                ps.setDouble(4, n.doubleValue());
//
//                BigDecimal bg = new BigDecimal(t + n.floatValue());
//                bg = bg.setScale(1, BigDecimal.ROUND_HALF_UP);
//                ps.setDouble(5, bg.doubleValue());
//
//                ps.execute();
//            }
//            commit();
//            query = "update otgruz1 set summa=(select sum(summav) from otgruz2 where doc_id=(select item_id from otgruz1 where ndoc=?)), "
//                    + " summa_nds=(select sum(summa_ndsv) from otgruz2 where doc_id=(select item_id from otgruz1 where ndoc=?)), "
//                    + " summa_all=(select sum(itogov) from otgruz2 where doc_id=(select item_id from otgruz1 where ndoc=?)) "
//                    + " where ndoc=?";
//            ps = conn.prepareStatement(query);
//            ps.setString(1, ttn);
//            ps.setString(2, ttn);
//            ps.setString(3, ttn);
//            ps.setString(4, ttn);
//            ps.executeUpdate();
//
//            if (ps != null) {
//                try {
//                    ps.close();
//                } catch (Exception e) {
//                    System.out.println("Error close preparedstatment" + e.getMessage());
//                }
//            }
//            if (rs != null) {
//                try {
//                    rs.close();
//                } catch (Exception e) {
//                    System.out.println("Error close preparedstatment" + e.getMessage());
//                }
//            }
//        } catch (Exception e) {
//            rollback();
//            System.out.println(e);
//        } finally {
//            setAutoCommit(true);
//            if (ps != null) {
//                try {
//                    ps.close();
//                } catch (Exception e) {
//                    System.out.println("Error close preparedstatment" + e.getMessage());
//                }
//            }
//            if (rs != null) {
//                try {
//                    rs.close();
//                } catch (Exception e) {
//                    System.out.println("Error close preparedstatment" + e.getMessage());
//                }
//            }
//        }
//    }

    void changePriceToTN2NDS(String ttn, float nds) {
        int kid = 0;
        String query = "";
        float cno = 0, t = 0, summa = 0, summa_nds = 0, itogo = 0;
        PreparedStatement ps2 = null;
        ResultSet rs2 = null;
        try {
            setAutoCommit(false);
            query = " select ot2.item_id, kod_izd, kol, kol_in_upack, nds,kol,kol_in_upack,kpl from otgruz2 as ot2 "
                    + " left join otgruz1 as ot1 on ot2.doc_id=ot1.item_id "
                    + " where doc_id = (select item_id from otgruz1 where ndoc = ?) ";
            ps = conn.prepareStatement(query);
            ps.setString(1, ttn);
            rs = ps.executeQuery();

            while (rs.next()) {
                //kod_izd = ;

                String query2 = " select cno, sar,nar from nsi_sd as sd "
                        + " left join (select sar,kod,nar from nsi_kld) as kld on kld.kod=sd.kod "
                        + " where kod1 = ? ";
                ps2 = conn.prepareStatement(query2);
                ps2.setInt(1, rs.getInt("kod_izd"));
                rs2 = ps2.executeQuery();
                if (rs2.next()) {
                    cno = rs2.getFloat("cno");
                }
                BigDecimal n = new BigDecimal(cno);
                n = n.setScale(2, BigDecimal.ROUND_HALF_UP);
                cno = n.floatValue();


                summa = cno * rs.getFloat("kol") * rs.getFloat("kol_in_upack");
                n = new BigDecimal(summa);
                n = n.setScale(2, BigDecimal.ROUND_HALF_UP);
                summa = n.floatValue();

                if (nds == -1) {
                    nds = getActualNDS(rs2.getString("sar"), rs2.getString("nar"), rs.getInt("kpl"), ttn);
                }
                summa_nds = summa * (nds / 100);
                n = new BigDecimal(summa_nds);
                n = n.setScale(2, BigDecimal.ROUND_HALF_UP);
                summa_nds = n.floatValue();

                itogo = summa + summa_nds;
                n = new BigDecimal(itogo);
                n = n.setScale(2, BigDecimal.ROUND_HALF_UP);
                itogo = n.floatValue();

                query2 = "update otgruz2 set cena = ?, summa = ?, summa_nds=?, itogo=?,nds=? where item_id = ?";
                ps2 = conn.prepareStatement(query2);
                ps2.setFloat(1, cno);
                ps2.setFloat(2, summa);
                ps2.setFloat(3, summa_nds);
                ps2.setFloat(4, itogo);
                ps2.setFloat(5, nds);
                ps2.setInt(6, rs.getInt("item_id"));
                ps2.executeUpdate();


            }
            /*String query2 = "update otgruz2 set nds=? where doc_id=(select item_id from otgruz1 where ndoc=?)";
             ps2 = conn.prepareStatement(query2);
             ps2.setFloat(1, nds);
             ps2.setString(2, ttn);
             ps2.executeUpdate();*/
            commit();
        } catch (Exception e) {
            rollback();
            System.out.println(e);
        } finally {
            setAutoCommit(true);
            if (ps2 != null) {
                try {
                    ps2.close();
                } catch (Exception e) {
                    System.out.println("Error close preparedstatment" + e.getMessage());
                }
            }
            if (rs2 != null) {
                try {
                    rs2.close();
                } catch (Exception e) {
                    System.out.println("Error close preparedstatment" + e.getMessage());
                }
            }
        }
    }

    void changePriceFirmMag(String ttn, float nds) {
        int kid = 0;
        String query = "";
        float cno = 0, t = 0, summa = 0, summa_nds = 0, itogo = 0;
        PreparedStatement ps2 = null;
        ResultSet rs2 = null;
        try {
            setAutoCommit(false);
//            query="select kpl from otgruz1 where ndoc=?";
//            ps = conn.prepareStatement(query);
//            ps.setString(1, ttn);
//            rs = ps.executeQuery();
//            if(rs.next())
//            if(rs.getInt(1) ==2281 || rs.getInt(1)==8889){
//                nds=0;
//            }
            query = " select ot2.item_id, kod_izd, kol, kol_in_upack, nds,kol,kol_in_upack,kpl from otgruz2 as ot2 "
                    + " left join otgruz1 as ot1 on ot2.doc_id=ot1.item_id "
                    + " where doc_id = (select item_id from otgruz1 where ndoc = ?) ";
            ps = conn.prepareStatement(query);
            ps.setString(1, ttn);
            rs = ps.executeQuery();

            while (rs.next()) {
                //kod_izd = ;

                String query2 = " select cno, sar,nar from nsi_sd as sd "
                        + " left join (select sar,kod,nar from nsi_kld) as kld on kld.kod=sd.kod "
                        + " where kod1 = ? ";
                ps2 = conn.prepareStatement(query2);
                ps2.setInt(1, rs.getInt("kod_izd"));
                rs2 = ps2.executeQuery();
                if (rs2.next()) {
                    cno = rs2.getFloat("cno");
                }
                BigDecimal n = new BigDecimal(cno);
                n = n.setScale(2, BigDecimal.ROUND_HALF_UP);
                //n = n.setScale(1, BigDecimal.ROUND_HALF_UP);
                //n = n.setScale(0, BigDecimal.ROUND_HALF_UP);
                cno = n.floatValue();


                summa = cno * rs.getFloat("kol") * rs.getFloat("kol_in_upack");
                n = new BigDecimal(summa);
                n = n.setScale(2, BigDecimal.ROUND_HALF_UP);
                //n = n.setScale(1, BigDecimal.ROUND_HALF_UP);
                //n = n.setScale(0, BigDecimal.ROUND_HALF_UP);
                summa = n.floatValue();

                if (nds == -1) {
                    nds = getActualNDS(rs2.getString("sar"), rs2.getString("nar"), rs.getInt("kpl"), ttn);
                }
                summa_nds = summa * (nds / 100);
                n = new BigDecimal(summa_nds);
                n = n.setScale(2, BigDecimal.ROUND_HALF_UP);
                //n = n.setScale(1, BigDecimal.ROUND_HALF_UP);
                //n = n.setScale(0, BigDecimal.ROUND_HALF_UP);
                summa_nds = n.floatValue();

                itogo = summa + summa_nds;
                n = new BigDecimal(itogo);
                n = n.setScale(2, BigDecimal.ROUND_HALF_UP);
                //n = n.setScale(1, BigDecimal.ROUND_HALF_UP);
                //n = n.setScale(0, BigDecimal.ROUND_HALF_UP);
                itogo = n.floatValue();

                query2 = "update otgruz2 set cena = ?, summa = ?, summa_nds=?, itogo=?,nds=? where item_id = ?";
                ps2 = conn.prepareStatement(query2);
                ps2.setFloat(1, cno);
                ps2.setFloat(2, summa);
                ps2.setFloat(3, summa_nds);
                ps2.setFloat(4, itogo);
                ps2.setFloat(5, nds);
                ps2.setInt(6, rs.getInt("item_id"));
                ps2.executeUpdate();


            }
            /*String query2 = "update otgruz2 set nds=? where doc_id=(select item_id from otgruz1 where ndoc=?)";
             ps2 = conn.prepareStatement(query2);
             ps2.setFloat(1, nds);
             ps2.setString(2, ttn);
             ps2.executeUpdate();*/
            commit();
        } catch (Exception e) {
            rollback();
            System.out.println(e);
        } finally {
            setAutoCommit(true);
            if (ps2 != null) {
                try {
                    ps2.close();
                } catch (Exception e) {
                    System.out.println("Error close preparedstatment" + e.getMessage());
                }
            }
            if (rs2 != null) {
                try {
                    rs2.close();
                } catch (Exception e) {
                    System.out.println("Error close preparedstatment" + e.getMessage());
                }
            }
        }
    }

    void changePriceToSebestoim(String ttn, float nds) {
        int kid = 0;
        String query = "";
        //nds=0;
        float sebStoi, cno = 0, t = 0, summa = 0, summa_nds = 0, itogo = 0, rent;
        float[] ss = new float[2];
        PreparedStatement ps2 = null;
        ResultSet rs2 = null;
        try {
            setAutoCommit(false);
//            String query="select kpl from otgruz1 where ndoc=?";
//            ps = conn.prepareStatement(query);
//            ps.setString(1, ttn);
//            rs = ps.executeQuery();
//            if(rs.next())
//            if(rs.getInt(1) ==2281 || rs.getInt(1)==8889){
//                nds=0;
//            }
            query = " select item_id, kod_izd, kol, kol_in_upack, nds,kol,kol_in_upack from otgruz2 where doc_id = (select item_id from otgruz1 where ndoc = ?)";
            ps = conn.prepareStatement(query);
            ps.setString(1, ttn);
            rs = ps.executeQuery();

            while (rs.next()) {
                //kod_izd = ;

                String query2 = " select cno,sar,rzm_marh from nsi_sd as sd "
                        + " left join (select sar,kod,nar from nsi_kld) as kld on kld.kod=sd.kod "
                        + " left join (select rzm_marh,item_id,kod_izd from otgruz2) as ot2 on sd.kod1=ot2.kod_izd "
                        + " where ot2.item_id=? ";
                ps2 = conn.prepareStatement(query2);
                ps2.setInt(1, rs.getInt("item_id"));
                //ps2.setInt(2, rs.getInt("kod_izd"));
                rs2 = ps2.executeQuery();
                dept.marketing.cena.CenaPDB cpdb = new CenaPDB();
                if (rs2.next()) {
                    ss = cpdb.getSstoimostRentabel(rs2.getLong("sar"), rs2.getInt("rzm_marh"));
                }
                if (ss[1] > 0) {
                    cno = new BigDecimal((rs2.getInt("cno") * 100) / (ss[1] + 100)).setScale(1, RoundingMode.HALF_UP).floatValue();
                } else {
                    cno = ss[0];
                }

                BigDecimal n = new BigDecimal(cno);
                n = n.setScale(-1, BigDecimal.ROUND_HALF_UP);
                //n = n.setScale(1, BigDecimal.ROUND_HALF_UP);
                //n = n.setScale(0, BigDecimal.ROUND_HALF_UP);
                cno = n.floatValue();


                summa = cno * rs.getFloat("kol") * rs.getFloat("kol_in_upack");
                n = new BigDecimal(summa);
                n = n.setScale(-1, BigDecimal.ROUND_HALF_UP);
                //n = n.setScale(1, BigDecimal.ROUND_HALF_UP);
                //n = n.setScale(0, BigDecimal.ROUND_HALF_UP);
                summa = n.floatValue();

                if (nds == -1) {
                    nds = getActualNDS(rs2.getString("sar"), rs2.getString("nar"), 0, ttn);
                }
                summa_nds = summa * (nds / 100);
                n = new BigDecimal(summa_nds);
                n = n.setScale(-1, BigDecimal.ROUND_HALF_UP);
                //n = n.setScale(1, BigDecimal.ROUND_HALF_UP);
                //n = n.setScale(0, BigDecimal.ROUND_HALF_UP);
                summa_nds = n.floatValue();

                itogo = summa + summa_nds;
                n = new BigDecimal(itogo);
                n = n.setScale(-1, BigDecimal.ROUND_HALF_UP);
                //n = n.setScale(1, BigDecimal.ROUND_HALF_UP);
                //n = n.setScale(0, BigDecimal.ROUND_HALF_UP);
                itogo = n.floatValue();

                query2 = "update otgruz2 set cena = ?, summa = ?, summa_nds=?, itogo=?,nds=? where item_id = ?";
                ps2 = conn.prepareStatement(query2);
                ps2.setFloat(1, cno);
                ps2.setFloat(2, summa);
                ps2.setFloat(3, summa_nds);
                ps2.setFloat(4, itogo);
                ps2.setFloat(5, nds);
                ps2.setInt(6, rs.getInt("item_id"));
                ps2.executeUpdate();
            }
            /*String query2 = "update otgruz2 set nds=? where doc_id=(select item_id from otgruz1 where ndoc=?)";
             ps2 = conn.prepareStatement(query2);
             ps2.setFloat(1, nds);
             ps2.setString(2, ttn);
             ps2.executeUpdate();*/
            commit();
        } catch (Exception e) {
            rollback();
            System.out.println(e);
        } finally {
            setAutoCommit(true);
            if (ps2 != null) {
                try {
                    ps2.close();
                } catch (Exception e) {
                    System.out.println("Error close preparedstatment" + e.getMessage());
                }
            }
            if (rs2 != null) {
                try {
                    rs2.close();
                } catch (Exception e) {
                    System.out.println("Error close preparedstatment" + e.getMessage());
                }
            }
        }
    }

    void changePriceToBelCenaIzd(String ttn, float skidka) {
        int kod_izd, kid = 0;
        String query = "";
        float cno = 0, t = 0, summa = 0, summa_nds = 0, itogo = 0, nds = -1;
        PreparedStatement ps2 = null;
        ResultSet rs2 = null;
        try {
            setAutoCommit(false);
//            String query="select kpl from otgruz1 where ndoc=?";
//            ps = conn.prepareStatement(query);
//            ps.setString(1, ttn);
//            rs = ps.executeQuery();
//            if(rs.next())
//            if(rs.getInt(1) ==2281 || rs.getInt(1)==8889){
//                nds=0;
//            }

            query = " select ot2.item_id, kod_izd, kol, kol_in_upack, nds,kpl from otgruz2 as ot2"
                    + " left join otgruz1 as ot1 on ot2.doc_id=ot1.item_id "
                    + " where doc_id = (select item_id from otgruz1 where ndoc = ?)";
            ps = conn.prepareStatement(query);
            ps.setString(1, ttn);
            rs = ps.executeQuery();

            while (rs.next()) {
                kod_izd = rs.getInt("kod_izd");

                String query2 = " select cno, sar,nar from nsi_sd as sd "
                        + " left join (select sar,kod,nar from nsi_kld) as kld on kld.kod=sd.kod "
                        + " where kod1 = ? ";
                ps2 = conn.prepareStatement(query2);
                ps2.setInt(1, kod_izd);
                rs2 = ps2.executeQuery();
                if (rs2.next()) {
                    cno = rs2.getFloat("cno") * (1 - (skidka / 100));
                }

                BigDecimal n = new BigDecimal(cno);
                n = n.setScale(2, BigDecimal.ROUND_HALF_UP);
                n = n.setScale(1, BigDecimal.ROUND_HALF_UP);
                n = n.setScale(0, BigDecimal.ROUND_HALF_UP);
                cno = n.intValue();


                summa = cno * rs.getFloat("kol") * rs.getFloat("kol_in_upack");
                n = new BigDecimal(summa);
                n = n.setScale(2, BigDecimal.ROUND_HALF_UP);
                n = n.setScale(1, BigDecimal.ROUND_HALF_UP);
                n = n.setScale(0, BigDecimal.ROUND_HALF_UP);
                summa = n.intValue();

                if (nds == -1) {
                    nds = getActualNDS(rs2.getString("sar"), rs2.getString("nar"), rs.getInt("kpl"), ttn);
                }
                summa_nds = summa * (nds / 100);
                n = new BigDecimal(summa_nds);
                n = n.setScale(2, BigDecimal.ROUND_HALF_UP);
                n = n.setScale(1, BigDecimal.ROUND_HALF_UP);
                n = n.setScale(0, BigDecimal.ROUND_HALF_UP);
                summa_nds = n.intValue();

                itogo = summa + summa_nds;
                n = new BigDecimal(itogo);
                n = n.setScale(2, BigDecimal.ROUND_HALF_UP);
                n = n.setScale(1, BigDecimal.ROUND_HALF_UP);
                n = n.setScale(0, BigDecimal.ROUND_HALF_UP);
                itogo = n.intValue();

                query2 = "update otgruz2 set cena = ?, summa = ?, summa_nds=?, itogo=?,nds=? where item_id = ?";
                ps2 = conn.prepareStatement(query2);
                ps2.setFloat(1, cno);
                ps2.setFloat(2, summa);
                ps2.setFloat(3, summa_nds);
                ps2.setFloat(4, itogo);
                ps2.setFloat(5, nds);
                ps2.setInt(6, rs.getInt("item_id"));
                ps2.executeUpdate();


            }
            String query2 = "update otgruz1 set skidka=? where ndoc = ?";
            ps2 = conn.prepareStatement(query2);
            ps2.setFloat(1, skidka);
            ps2.setString(2, ttn);
            ps2.executeUpdate();
            commit();
        } catch (Exception e) {
            rollback();
            System.out.println(e);
        } finally {
            setAutoCommit(true);
            if (ps2 != null) {
                try {
                    ps2.close();
                } catch (Exception e) {
                    System.out.println("Error close preparedstatment" + e.getMessage());
                }
            }
            if (rs2 != null) {
                try {
                    rs2.close();
                } catch (Exception e) {
                    System.out.println("Error close preparedstatment" + e.getMessage());
                }
            }
        }
    }

    void changePriceToBelSumDoc(String ttn, float skidka) {
        int kod_izd, kpl = 0;
        String query = "";
        float cno = 0, t = 0, summa = 0, summa_nds = 0, itogo = 0, nds = -1;
        ;
        BigDecimal n = null;
        PreparedStatement ps = null, ps2 = null;
        ResultSet rs = null, rs2 = null;
        try {
            setAutoCommit(false);
//            String query="select kpl from otgruz1 where ndoc=?";
//            ps = conn.prepareStatement(query);
//            ps.setString(1, ttn);
//            rs = ps.executeQuery();
//            if(rs.next())
//                kpl=rs.getInt(1);
            query = " select ot2.item_id, kod_izd, kol, kol_in_upack, nds,kol,kol_in_upack,kpl from otgruz2 as ot2 "
                    + "right join (select kpl,item_id,ndoc from otgruz1) as ot1 on ot1.item_id=ot2.doc_id where ot1.ndoc = ?";
            ps = conn.prepareStatement(query);
            ps.setString(1, ttn);
            rs = ps.executeQuery();
            while (rs.next()) {
                kod_izd = rs.getInt("kod_izd");
                String query2 = " select cno, sar,nar from nsi_sd as sd "
                        + " left join (select sar,kod,nar from nsi_kld) as kld on kld.kod=sd.kod "
                        + " where kod1 = ? ";
                ps2 = conn.prepareStatement(query2);
                ps2.setInt(1, kod_izd);
                rs2 = ps2.executeQuery();
                if (rs2.next()) {
                    n = new BigDecimal(rs2.getFloat("cno") - (rs2.getFloat("cno") * (skidka / 100)));
                }
                n = n.setScale(2, BigDecimal.ROUND_UP);
                n = n.setScale(1, BigDecimal.ROUND_DOWN);
                n = n.setScale(0, BigDecimal.ROUND_HALF_DOWN);
                cno = n.floatValue();

                summa = cno * rs.getFloat("kol") * rs.getFloat("kol_in_upack");
                n = new BigDecimal(summa);
                n = n.setScale(2, BigDecimal.ROUND_UP);
                n = n.setScale(1, BigDecimal.ROUND_DOWN);
                n = n.setScale(0, BigDecimal.ROUND_HALF_DOWN);
                summa = n.floatValue();

                if (nds == -1) {
                    nds = getActualNDS(rs2.getString("sar"), rs2.getString("nar"), rs.getInt("kpl"), ttn);
                }

                summa_nds = summa * (nds / 100);
                n = new BigDecimal(summa_nds);
                n = n.setScale(2, BigDecimal.ROUND_UP);
                n = n.setScale(1, BigDecimal.ROUND_DOWN);
                n = n.setScale(0, BigDecimal.ROUND_HALF_DOWN);
                summa_nds = n.floatValue();

                itogo = summa + summa_nds;
                n = new BigDecimal(itogo);
                n = n.setScale(2, BigDecimal.ROUND_UP);
                n = n.setScale(1, BigDecimal.ROUND_DOWN);
                n = n.setScale(0, BigDecimal.ROUND_HALF_DOWN);
                itogo = n.floatValue();

                query2 = "update otgruz2 set cena = ?, summa = ?, summa_nds=?, itogo=?,nds=? where item_id = ?";
                ps2 = conn.prepareStatement(query2);
                ps2.setFloat(1, cno);
                ps2.setFloat(2, summa);
                ps2.setFloat(3, summa_nds);
                ps2.setFloat(4, itogo);
                ps2.setFloat(5, nds);
                ps2.setInt(6, rs.getInt("item_id"));
                ps2.executeUpdate();
            }
            String query2 = "update otgruz1 set skidka=? where ndoc = ?";
            ps2 = conn.prepareStatement(query2);
            ps2.setFloat(1, skidka);
            ps2.setString(2, ttn);
            ps2.executeUpdate();
            commit();
        } catch (Exception e) {
            rollback();
            System.out.println(e);
        } finally {
            setAutoCommit(true);
            if (ps2 != null) {
                try {
                    ps2.close();
                } catch (Exception e) {
                    System.out.println("Error close preparedstatment" + e.getMessage());
                }
            }
            if (rs != null) {
                try {
                    rs.close();
                } catch (Exception e) {
                    System.out.println("Error close preparedstatment" + e.getMessage());
                }
            }
        }
    }

    public void setOtgruzAddition(int roundValue, String nomer) {
        String query = "select ot2.item_id as item_id, sar, fas, srt, cena, nds, klient_id, date, nar from (select item_id, klient_id, date from otgruz1 where ndoc = ?)as ot1 "
                + " left join (select kod_izd, doc_id, cena, nds, item_id from otgruz2)as ot2 on ot2.doc_id = ot1.item_id "
                + " left join (select kod1, kod, srt from nsi_sd)as sd on sd.kod1 = ot2.kod_izd "
                + " left join (select kod, sar, fas, nar from nsi_kld)as kld on sd.kod = kld.kod"
                + " where srt!=0 "; //and (srt=1 or srt=2)";
        try {
            ps = conn.prepareStatement(query);
            ps.setString(1, nomer);
            rs = ps.executeQuery();
            while (rs.next()) {
                int torg_nadb = 0;
                String query2;
                PreparedStatement ps2;
                ResultSet rs2;
                int ya;
                if (rs.getString("nar").trim().substring(0, 1).equals("1")
                        || rs.getString("nar").trim().substring(0, 1).equals("2")
                        || rs.getString("nar").trim().substring(0, 1).equals("3")
                        || rs.getString("nar").trim().substring(0, 1).equals("4")
                        || rs.getString("nar").trim().substring(0, 1).equals("5")
                        || rs.getString("nar").trim().substring(0, 1).equals("6")
                        || rs.getString("nar").trim().substring(0, 1).equals("7")
                        || rs.getString("nar").trim().substring(0, 1).equals("8")
                        || rs.getString("nar").trim().substring(0, 1).equals("9")
                        || rs.getString("nar").trim().substring(0, 1).equals("0")) {
                    ya = Integer.parseInt(rs.getString("nar").trim().substring(0, 1));
                } else {
                    ya = 0;
                }
                Date d = new Date(System.currentTimeMillis());
                int y = d.getYear();
                y = y % 10;
                if (ya <= y) {
                    ya += 10;
                }
                if (y < 2) {
                    y += 10;
                }
                y = y - 2;
                //проверяем старый ли артикул
                if (ya < y) {
                    query2 = "select top 1 tn, max(date) from _torg_nadbavka where art = 'старый' and date < ? group by tn order by max(date)";
                    ps2 = conn.prepareStatement(query2);
                    ps2.setDate(1, rs.getDate("date"));
                    rs2 = ps2.executeQuery();
                    if (rs2.next()) {
                        torg_nadb = rs2.getInt(1);
                    }
                } else {
                    query2 = "select top 1 tn, max(date) from _torg_nadbavka where klient_id is null and art = ? and model = ? and sort = ? and date < ? group by tn order by max(date)";
                    ps2 = conn.prepareStatement(query2);
                    ps2.setString(1, rs.getString("sar").trim());
                    ps2.setInt(2, rs.getInt("fas"));
                    ps2.setString(3, rs.getString("srt").trim());
                    ps2.setDate(4, rs.getDate("date"));
                    rs2 = ps2.executeQuery();
                    if (rs2.next()) {
                        torg_nadb = rs2.getInt(1);
                    } else {
                        query2 = "select top 1 tn, max(date) from _torg_nadbavka where klient_id = ? and ? like art and (model = ? or model is null) and sort = ? and date < ? group by art, tn order by art, max(date)";
                        ps2 = conn.prepareStatement(query2);
                        ps2.setInt(1, rs.getInt("klient_id"));
                        ps2.setString(2, rs.getString("sar").trim());
                        ps2.setInt(3, rs.getInt("fas"));
                        ps2.setString(4, rs.getString("srt").trim());
                        ps2.setDate(5, rs.getDate("date"));
                        rs2 = ps2.executeQuery();
                        if (rs2.next()) {
                            torg_nadb = rs2.getInt(1);
                        } else {
                            int q = Integer.parseInt(rs.getString("sar").trim().substring(2, 3));
                            if (q == 3 || q == 6) {
                                query2 = "select top 1 tn, max(date) from _torg_nadbavka where art = 'новый д' and date < ? group by tn order by max(date)";
                            } else {
                                query2 = "select top 1 tn, max(date) from _torg_nadbavka where art = 'новый в' and date < ? group by tn order by max(date)";
                            }
                            ps2 = conn.prepareStatement(query2);
                            ps2.setDate(1, rs.getDate("date"));
                            rs2 = ps2.executeQuery();
                            if (rs2.next()) {
                                torg_nadb = rs2.getInt(1);
                            } else {
                                System.out.println(rs.getString("klient_id").trim() + " -- " + rs.getString("sar").trim() + " -- " + rs.getInt("fas") + " -- " + rs.getString("srt").trim() + " -- " + rs.getDate("date"));
                                torg_nadb = 0;
                            }
                        }
                    }
                }
                query2 = "select id_item from _otgruz2_addition where id_item = ?";
                ps2 = conn.prepareStatement(query2);
                ps2.setInt(1, rs.getInt("item_id"));
                rs2 = ps2.executeQuery();
                long rc = 0;
                int cena = rs.getInt("cena");
                float ctn = (float) (((float) (cena * torg_nadb)) / 100.);
                int nds = rs.getInt("nds");
                rc = (long) (cena + ctn + ((float) ((cena + ctn) * nds) / 100.));
                int ost = (int) (rc % 100);
                rc /= 100;
                rc *= 100;
                /*               switch (roundValue) {

                    case 50:
                        if (ost >= 25 && ost < 75) {
                            rc += 50;
                        } else if (ost >= 75) {
                            rc += 100;
                            break;
                        }
                    case 100:
                        if (ost >= 25 && ost < 51) {
                            rc += 0;
                        } else if (ost >= 51) {
                            rc += 100;
                            break;
                        }
                }*/

                switch (roundValue) {
                    case 50:
                        if (ost >= 25 && ost < 75) {
                            rc += 50;
                        } else if (ost >= 75) {
                            rc += 100;
                        }
                        break;
                    case 100:
                        if (ost >= 25 && ost < 50) {
                            rc += 0;
                        } else if (ost >= 50) {
                            rc += 100;
                        }
                        break;
                }


                if (rs2.next()) {
                    String q = "update _otgruz2_addition set torg_nadb = ?, rozn_cena = ? where id_item = ?";
                    ps2 = conn.prepareStatement(q);
                    ps2.setInt(1, torg_nadb);
                    ps2.setLong(2, rc);
                    ps2.setInt(3, rs.getInt("item_id"));
                    ps2.executeUpdate();
                } else {
                    String q = "insert into _otgruz2_addition (id_item,torg_nadb, rozn_cena ) values(?, ?, ?)";
                    ps2 = conn.prepareStatement(q);
                    ps2.setInt(1, rs.getInt("item_id"));
                    ps2.setInt(2, torg_nadb);
                    ps2.setLong(3, rc);
                    ps2.executeUpdate();
                }
            }
        } catch (Exception e) {
            log.severe("Ошибка в SkladDB setOtgruzAddition(String nomer) " + e);
            System.err.println("Ошибка в setOtgruzAddition(String nomer)" + e);
        }
    }

    public void setOtgruzAddition(int roundValue, String nomer, int tn) {
        String query = "select ot2.item_id as item_id, sar, fas, srt, cena, nds, klient_id, date, nar from (select item_id, klient_id, date from otgruz1 where ndoc = ?)as ot1 "
                + " left join (select kod_izd, doc_id, cena, nds, item_id from otgruz2)as ot2 on ot2.doc_id = ot1.item_id "
                + " left join (select kod1, kod, srt from nsi_sd)as sd on sd.kod1 = ot2.kod_izd "
                + " left join (select kod, sar, fas, nar from nsi_kld)as kld on sd.kod = kld.kod";
        try {
            ps = conn.prepareStatement(query);
            ps.setString(1, nomer);
            rs = ps.executeQuery();
            while (rs.next()) {

                int torg_nadb = tn;
                String query2;
                PreparedStatement ps2;
                ResultSet rs2;


                query2 = "select id_item from _otgruz2_addition where id_item = ?";
                ps2 = conn.prepareStatement(query2);
                ps2.setInt(1, rs.getInt("item_id"));
                rs2 = ps2.executeQuery();
                long rc = 0;
                int cena = rs.getInt("cena");
                float ctn = (float) (((float) (cena * torg_nadb)) / 100.);
                int nds = rs.getInt("nds");
                rc = (long) (cena + ctn + ((float) ((cena + ctn) * nds) / 100.));
                int ost = (int) (rc % 100);
                rc /= 100;
                rc *= 100;


                switch (roundValue) {
                    case 50:
                        if (ost >= 25 && ost < 75) {
                            rc += 50;
                        } else if (ost >= 75) {
                            rc += 100;
                        }
                        break;
                    case 100:
                        if (ost >= 25 && ost < 50) {
                            rc += 0;
                        } else if (ost >= 50) {
                            rc += 100;
                        }
                        break;
                }


                if (rs2.next()) {
                    String q = "update _otgruz2_addition set torg_nadb = ?, rozn_cena = ? where id_item = ?";
                    ps2 = conn.prepareStatement(q);
                    ps2.setInt(1, torg_nadb);
                    ps2.setLong(2, rc);
                    ps2.setInt(3, rs.getInt("item_id"));
                    ps2.executeUpdate();
                } else {
                    String q = "insert into _otgruz2_addition (id_item,torg_nadb, rozn_cena ) values(?, ?, ?)";
                    ps2 = conn.prepareStatement(q);
                    ps2.setInt(1, rs.getInt("item_id"));
                    ps2.setInt(2, torg_nadb);
                    ps2.setLong(3, rc);
                    ps2.executeUpdate();
                }
            }
        } catch (Exception e) {
            log.severe("Ошибка в SkladDB setOtgruzAddition(String nomer) " + e);
            System.err.println("Ошибка в setOtgruzAddition(String nomer)" + e);
        }
    }

    /**
     * Обновляет цены на изделие в ТТН
     *
     * @param ndoc    -- номер ТТН
     * @param kod_izd -- код изделия
     * @param price   -- новая цена
     */
    public void setNewPrice(String ndoc, int kod_izd, float price) {
        try {
            String query = "update otgruz2 set cena = ?, summa = (kol* kol_in_upack * ?), summa_nds = ((kol* kol_in_upack * ?)* nds / 100), itogo = (kol* kol_in_upack * ?) +  ((kol* kol_in_upack * ?)* nds / 100) where kod_izd = ? and doc_id = (select item_id from otgruz1 where ndoc = ?)";
            ps = conn.prepareStatement(query);
            ps.setFloat(1, price);
            ps.setFloat(2, price);
            ps.setFloat(3, price);
            ps.setFloat(4, price);
            ps.setFloat(5, price);
            ps.setInt(6, kod_izd);
            ps.setString(7, ndoc);
            ps.executeUpdate();
        } catch (Exception e) {
            log.severe("Ошибка при выполнении функции setNewPrice(String ndoc, int kod_izd, float price)" + e.getMessage());
        }
    }

    public List<Map<String, Object>> svodnayaVedomostPoFirmMag(Map<String, Object> parameters) {
        Map<String, Object> hm;
        List<Map<String, Object>> allResult = new ArrayList<>();
        String query = "select ndoc,put_list,kol,summa,summa_nds,sum_torg_nad,nds_tn,all_nds, "
                + " 		 (case when nds=10 "
                + "				then rozn_sum "
                + "				else 0 end ) as rozn_sum_kid, "
                + "              (case when nds=20 "
                + "				then rozn_sum "
                + "				else 0 end ) as rozn_sum_adult, date_doc, nds  "
                + "	from (select  ndoc,put_list,sum(kol) as kol,sum(summa) as summa, sum (summa_nds) as summa_nds,sum(sum_torg_nad) as sum_torg_nad, "
                + "		sum(nds_tn) as nds_tn,sum(all_nds) as all_nds,sum(kol*t1.rozn_cena) as rozn_sum,nds, date_doc "
                + "	  from (select  ot1.ndoc,pl.put_list,ot1.kpl,ot1.status,ot1.operac,sum(ot2.kol) as kol,sum(ot2.summa) as summa, "
                + "		  sum(ot2.summa_nds) as summa_nds,ot2.nds, oa.torg_nadb,round(sum(ot2.summa*oa.torg_nadb/100),0) as sum_torg_nad, "
                + "		  round(sum((ot2.summa*oa.torg_nadb/100)*nds/100),0) as nds_tn, "
                + "	  	  round(sum(ot2.summa_nds+((ot2.summa*oa.torg_nadb/100)*nds/100)),0) as all_nds,rozn_cena,kld.sar,sd.srt,pl.date_doc from otgruz1 as ot1 "
                + "		  	left join (select item_id,doc_id,kol*kol_in_upack as kol,summa,summa_nds,nds,kod_izd from otgruz2) as ot2 on ot1.item_id=ot2.doc_id "
                + "			left join (select ndoc as put_list,ttn_id,date,date as date_doc from _put_list ) as pl on ot1.item_id=pl.ttn_id  "
                + "			left join (select id_item,torg_nadb,rozn_cena from _otgruz2_addition) as oa on ot2.item_id=oa.id_item "
                + "			left join (select kod1,kod,srt from nsi_sd) as sd on sd.kod1=ot2.kod_izd "
                + "			left join (select sar,kod from nsi_kld) as kld on kld.kod=sd.kod "
                + "		  where ot1.status=0 and kpl=? and pl.date between " + parameters.get("date").toString() + " and " + parameters.get("type").toString()
                + "		  group by ot1.ndoc,pl.put_list,ot1.kpl,ot1.status,ot1.operac, "
                + "		  ot2.nds, oa.torg_nadb,round(ot2.summa*oa.torg_nadb/100,0),rozn_cena,kld.sar,sd.srt, pl.date_doc"
                + "	  )as t1 "
                + "	  where  " + parameters.get("operac").toString()
                + "	  group by ndoc,put_list,nds, t1.date_doc )as t2 order by ndoc";

        try (PreparedStatement ps = getConnection().prepareStatement(query)) {
            ps.setInt(1, Integer.parseInt(parameters.get("kpl").toString()));
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                hm = new HashMap<>();
                hm.put("ndoc", rs.getString(1));
                hm.put("put_list", rs.getString(2));
                hm.put("kol", rs.getInt(3));
                hm.put("summa", rs.getInt(4));
                hm.put("summa_nds", rs.getInt(5));
                hm.put("sum_torg_nad", rs.getInt(6));
                hm.put("nds_tn", rs.getInt(7));
                hm.put("all_nds", rs.getInt(8));
                hm.put("rozn_sum_kid", rs.getInt(9));
                hm.put("rozn_sum_adult", rs.getInt(10));
                hm.put("date_doc", rs.getDate(11));
                hm.put("nds_value", rs.getInt(12));
                allResult.add(hm);
            }
        } catch (Exception ex) {
            log.severe("Ошибка при выполнении функции svodnayaVedomostPoFirmMag " + ex.getMessage());
        }
        return allResult;
    }

    public List<Map<String, Object>> getFirmShop() {
        List<Map<String, Object>> listShop = new ArrayList<>();
        Map<String, Object> buffer;
        String query = "select '('+rtrim(ltrim(str(KOD)))+')'+rtrim(ltrim(NAIM)) from s_klient  "
                + "where UNN = (select UNN from s_klient where KOD=-1) and VID = 5 ";
        try (PreparedStatement ps = getConnection().prepareStatement(query)) {
            rs = ps.executeQuery();
            while (rs.next()) {
                buffer = new HashMap<>();

                buffer.put("name", rs.getString(1));
                listShop.add(buffer);
            }
        } catch (Exception ex) {
            log.severe("Ошибка при выполнении функции getFirmShop " + ex.getMessage());
        }
        return listShop;
    }

    /**
     * @autor vova Сохраняет отчёт "Путевой лист в базу данных"
     * @param path -- путь к файлу
     */
    /*    public void savePutListDB(String path, int id_ttn, int nomer){
     try{
     String query = " update _put_list_report set nomer = ?, date = getdate(), report = ? where id = ? ";
     ps = conn.prepareStatement(query);
     ps.setInt(1, nomer);
     File report = new File(path);
     FileInputStream   fis = new FileInputStream(report);
     ps.setBinaryStream(2, fis, (int) report.length());
     ps.setInt(3, id_ttn);
     int row = ps.executeUpdate();
     fis.close();
     if (row == 0){
     query = " insert into _put_list_report (nomer, report, id) values(?, ?, ?)";
     ps = conn.prepareStatement(query);
     ps.setInt(1, nomer);
     report = new File(path);
     fis = new FileInputStream(report);
     ps.setBinaryStream(2, fis, (int) report.length());
     ps.setInt(3, id_ttn);
     ps.executeUpdate();
     fis.close();}
     }catch(Exception e){
     log.error("Ошибка при выполнении функции savePutListDB(String path, int id_ttn, int nomer)", e);
     System.out.println ("Ошибка при выполнении функции savePutListDB(String path, int id_ttn, int nomer): " + e);
     }
     }*/
    public void updateSumTTN(String ndoc) {
        String query = "update otgruz1 set summa = (select sum(summa) from otgruz2 where doc_id = (select item_id from otgruz1 where ndoc = ?)), "
                + " summa_nds = (select sum(summa_nds) from otgruz2 where doc_id = (select item_id from otgruz1 where ndoc = ?)), "
                + " summa_all = (select sum(itogo) from otgruz2 where doc_id = (select item_id from otgruz1 where ndoc = ?)) "
                + " where ndoc = ? ";
        try (PreparedStatement ps = getConnection().prepareStatement(query)) {
            ps.setString(1, ndoc);
            ps.setString(2, ndoc);
            ps.setString(3, ndoc);
            ps.setString(4, ndoc);
            ps.executeUpdate();
        } catch (Exception e) {
            log.severe("Ошибка при выполнении функции getSertifikat" + e.getMessage());
        }
    }

    /**
     * @param nomer -- номер ТТН
     * @return путь к файлу с отчётом
     * @autor vova Получение отчёта "приложение ТТН"
     */
    public String getPutListReport(int nomer) {
        String savePath = MyReportsModule.confPath + "Reports/" + nomer + ".odt";
        String query = "select report from _put_list_report where nomer = ? ";
        try (PreparedStatement ps = getConnection().prepareStatement(query)) {
            ps.setInt(1, nomer);
            rs = ps.executeQuery();
            if (rs.next()) {
                File image = new File(savePath);
                FileOutputStream fos = new FileOutputStream(image);
                byte[] buffer = new byte[1];
                InputStream is = rs.getBinaryStream("report");
                while (is.read(buffer) > 0) {
                    fos.write(buffer);
                }
                fos.close();
            }
        } catch (Exception e) {
            log.severe("Ошибка при выполнении функции getPutListReport(int nomer)" + e.getMessage());
        }
        return savePath;
    }

    /**
     * @param path -- путь к файлу
     * @autor vova Сохраняет отчёт "Приложение ТТН в базу данных"
     */
//    public void saveTTNDB(String path, int id_ttn, String nomer) {
//        try {
//            String query = " update _ttn_report set nomer = ?, date = getdate(), report = ? where id = ? ";
//            ps = conn.prepareStatement(query);
//            ps.setString(1, nomer);
//            File report = new File(path);
//            FileInputStream fis = new FileInputStream(report);
//            ps.setBinaryStream(2, fis, (int) report.length());
//            ps.setInt(3, id_ttn);
//            int row = ps.executeUpdate();
//            fis.close();
//            if (row == 0) {
//                query = " insert into _ttn_report (nomer,report, id) values(?, ?, ?)";
//                ps = conn.prepareStatement(query);
//                ps.setString(1, nomer);
//                report = new File(path);
//                fis = new FileInputStream(report);
//                ps.setBinaryStream(2, fis, (int) report.length());
//                ps.setInt(3, id_ttn);
//                ps.executeUpdate();
//                fis.close();
//            }
//        } catch (Exception e) {
//            log.severe("Ошибка при выполнении функции saveTTNDB(String path, int id_ttn, int nomer)" + e);
//            System.out.println("Ошибка при выполнении функции saveTTNDB(String path, int id_ttn, int nomer): " + e);
//        }
//    }

    /**
     * @param nomer -- номер ТТН
     * @return путь к файлу с отчётом
     * @autor vova Получение отчёта "приложение ТТН"
     */
    public String getTTNReport(int nomer) {
        String savePath = MyReportsModule.confPath + "Reports/" + nomer + ".ods";
        String query = "select report from _ttn_report where nomer = ? ";
        try (PreparedStatement ps = getConnection().prepareStatement(query)) {
            ps.setInt(1, nomer);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                File image = new File(savePath);
                FileOutputStream fos = new FileOutputStream(image);
                byte[] buffer = new byte[1];
                InputStream is = rs.getBinaryStream("report");
                while (is.read(buffer) > 0) {
                    fos.write(buffer);
                }
                fos.close();
            }
        } catch (Exception e) {
            log.severe("Ошибка при выполнении функции saveTTNDB(String path, int id_ttn, int nomer)" + e.getMessage());
        }
        return savePath;
    }

    public List<Map<String, Object>> getPrilojenieTTNSkidkaBelSummaDoc(String nomer, Map money) {
        List<Map<String, Object>> v = new ArrayList();
        BigDecimal n;
        String query = "select sar, nar, fas, ngpr, narp, ncw, rst, rzm, srt, ean, sum(kol), cena, sum(summa), nds, sum(summa_nds), sum(itogo),sum(kkr), sum(mas*kol) as massa,preiscur,cno,rzm_print from (select item_id from otgruz1 where ndoc = ?)as ot1 "
                + " left join (select kod_izd, doc_id, (kol*kol_in_upack) as kol, kol as kkr, cena,nds, summa, summa_nds, itogo, eancode, preiscur, ncw from otgruz2)as ot2 on ot2.doc_id = ot1.item_id "
                + " left join (select kod1, kod, rzm_print, ean, massa as mas, srt, rst, rzm,cno from nsi_sd)as sd on sd.kod1 = ot2.kod_izd "
                + " left join (select kod, sar, nar, fas, ngpr, narp from nsi_kld)as kld on sd.kod = kld.kod " + //,kkr
                " group by sar, nar, fas, ngpr, narp, ncw, rst, rzm, srt, ean, cena, nds,preiscur,cno,rzm_print "
                + " order by sar, nar, fas, rst, rzm, ncw,cno ";
        try (PreparedStatement ps = getConnection().prepareStatement(query)) {
            ps.setString(1, nomer);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Map<String, Object> m = new HashMap<>();
                m.put("rzm_print", rs.getString("rzm_print"));
                m.put("sar", rs.getInt(1));
                m.put("nar", rs.getString(2).trim());
                m.put("fas", rs.getInt(3));
                m.put("ngpr", rs.getString(4).trim());
                m.put("narp", rs.getLong(5));
                m.put("ncw", rs.getString(6).trim());
                m.put("rst", rs.getInt(7));
                m.put("rzm", rs.getInt(8));
                m.put("srt", rs.getString(9).trim());
                m.put("ean", rs.getString(10).trim());
                m.put("kol", rs.getInt(11));
                n = BigDecimal.valueOf(rs.getFloat(12));
                n = n.setScale(2, RoundingMode.HALF_UP);
                m.put("cena", n);

                n = BigDecimal.valueOf(rs.getFloat(20));
                n = n.setScale(2, RoundingMode.HALF_UP);
                m.put("cenaBS", n);

                n = BigDecimal.valueOf(rs.getFloat(13));
                n = n.setScale(2, RoundingMode.HALF_UP);
                m.put("summa", n);


                n = BigDecimal.valueOf(Float.parseFloat(m.get("cenaBS").toString()) * Float.parseFloat(m.get("kol").toString()));
                n = n.setScale(2, RoundingMode.HALF_UP);
                m.put("summaBS", n);

                m.put("nds", rs.getInt(14));

                n = BigDecimal.valueOf(rs.getFloat(15));
                n = n.setScale(2, RoundingMode.HALF_UP);
                m.put("summa_nds", n);


                n = BigDecimal.valueOf(Float.parseFloat(m.get("summaBS").toString()) * (Float.parseFloat(m.get("nds").toString()) / 100));
                n = n.setScale(2, RoundingMode.HALF_UP);
                m.put("summa_ndsBS", n);

                n = BigDecimal.valueOf(Float.parseFloat(m.get("summa").toString()) + Float.parseFloat(m.get("summa_nds").toString()));
                n = n.setScale(2, BigDecimal.ROUND_HALF_UP);
                m.put("itogo", n);


                n = BigDecimal.valueOf(Float.parseFloat(m.get("summaBS").toString()) + Float.parseFloat(m.get("summa_ndsBS").toString()));
                n = n.setScale(0, RoundingMode.HALF_UP);
                m.put("itogoBS", n);

                m.put("kkr", rs.getInt(17));
                n = BigDecimal.valueOf(rs.getFloat(18));
                n = n.setScale(2, RoundingMode.HALF_UP);
                m.put("massa", n);
                m.put("preiscur", rs.getString(19).trim());
                m.put("skidka", money.get("skidka"));

                if (m.get("sar").toString().startsWith("48")) {
                    m.put("izm", "кг.");
                }

                if (m.get("sar").toString().startsWith("43")) {
                    m.put("izm", "пар");
                }

                if (m.get("sar").toString().startsWith("4700")) {
                    m.put("izm", "кг.");
                }

                if (m.get("sar").toString().startsWith("4701")) {
                    m.put("izm", "м.");
                }

                if (!m.get("sar").toString().startsWith("4701") && !m.get("sar").toString().startsWith("4700")) {
                    m.put("izm", "кг.");
                }

                if (!m.get("sar").toString().startsWith("43") && !m.get("sar").toString().startsWith("48")) {
                    m.put("izm", "шт.");
                }

                if (getIzm().isEmpty()) {
                    izm = m.get("izm").toString();
                }

                v.add(m);
            }
        } catch (Exception e) {
            log.severe("Ошибка в SkladDB getPrilojenieTTN(String nomer) " + e.getMessage());
        }
        return v;
    }

    public List<Map<String, Object>> getPrilojenieTTNSkidkaBelCenaIzd(String nomer, Map money) {
        List<Map<String, Object>> v = new ArrayList();
        BigDecimal n;
        String query = "select sar, nar, fas, ngpr, narp, ncw, rst, rzm, srt, ean, sum(kol), cena, sum(summa), nds, sum(summa_nds), sum(itogo),sum(kkr), sum(mas) as massa,preiscur,cno,rzm_print from (select item_id from otgruz1 where ndoc = ?)as ot1 "
                + " left join (select kod_izd, doc_id, (kol*kol_in_upack) as kol, kol as kkr, cena,nds, summa, summa_nds, itogo, eancode, preiscur, ncw from otgruz2)as ot2 on ot2.doc_id = ot1.item_id "
                + " left join (select kod1, kod, rzm_print, ean, massa as mas, srt, rst, rzm,cno from nsi_sd)as sd on sd.kod1 = ot2.kod_izd "
                + " left join (select kod, sar, nar, fas, ngpr, narp from nsi_kld)as kld on sd.kod = kld.kod " + //,kkr
                " group by sar, nar, fas, ngpr, narp, ncw, rst, rzm, srt, ean, cena, nds,preiscur,cno,rzm_print "
                + " order by sar, nar, fas, rzm,srt,ncw,cno";
        try (PreparedStatement ps = getConnection().prepareStatement(query)){
            ps.setString(1, nomer);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Map<String, Object> m = new HashMap();
                m.put("rzm_print", rs.getString("rzm_print"));
                m.put("sar", rs.getInt(1));
                m.put("nar", rs.getString(2).trim());
                m.put("fas", rs.getInt(3));
                m.put("ngpr", rs.getString(4).trim());
                m.put("narp", rs.getLong(5));
                m.put("ncw", rs.getString(6).trim());
                m.put("rst", rs.getInt(7));
                m.put("rzm", rs.getInt(8));
                m.put("srt", rs.getString(9).trim());
                m.put("ean", rs.getString(10).trim());
                m.put("kol", rs.getInt(11));
                n = BigDecimal.valueOf(rs.getFloat(12));
                n = n.setScale(2, RoundingMode.HALF_UP);
                m.put("cena", rounding(n));

                n = BigDecimal.valueOf(rs.getFloat(20));
                n = n.setScale(0, RoundingMode.HALF_UP);
                m.put("cenaBS", n.intValue());

                n = BigDecimal.valueOf(Float.parseFloat(m.get("cena").toString()) * Float.parseFloat(m.get("kol").toString()));
                n = n.setScale(2, RoundingMode.HALF_UP);
                m.put("summa", n.intValue());

                n = BigDecimal.valueOf(Float.parseFloat(m.get("cenaBS").toString()) * Float.parseFloat(m.get("kol").toString()));
                n = n.setScale(0, RoundingMode.HALF_UP);
                m.put("summaBS", n.intValue());

                m.put("nds", rs.getInt(14));

                n = BigDecimal.valueOf(rs.getFloat(15));
                n = n.setScale(2, RoundingMode.HALF_UP);
                m.put("summa_nds", rounding(n));

                n = BigDecimal.valueOf(Float.parseFloat(m.get("summaBS").toString()) * (Float.parseFloat(m.get("nds").toString()) / 100));
                n = n.setScale(0, RoundingMode.HALF_UP);
                m.put("summa_ndsBS", n);

                n = BigDecimal.valueOf(Float.parseFloat(m.get("summa").toString()) + Float.parseFloat(m.get("summa_nds").toString()));
                n = n.setScale(2, RoundingMode.HALF_UP);
                m.put("itogo", rounding(n));

                n = BigDecimal.valueOf(Float.parseFloat(m.get("summaBS").toString()) + Float.parseFloat(m.get("summa_ndsBS").toString()));
                n = n.setScale(0, RoundingMode.HALF_UP);
                m.put("itogoBS", n);

                m.put("kkr", rs.getInt(17));
                n = BigDecimal.valueOf(rs.getFloat(18));
                n = n.setScale(2, RoundingMode.HALF_UP);
                m.put("massa", n);
                m.put("preiscur", rs.getString(19).trim());
                m.put("skidka", money.get("skidka"));
                if (m.get("sar").toString().startsWith("48")) {
                    m.put("izm", "кг.");
                }
                if (m.get("sar").toString().startsWith("43")) {
                    m.put("izm", "пар");
                }
                if (!m.get("sar").toString().startsWith("43") && !m.get("sar").toString().startsWith("48")) {
                    m.put("izm", "шт.");
                }
                if (getIzm().isEmpty()) {
                    izm = m.get("izm").toString();
                }
                v.add(m);
            }
        } catch (Exception e) {
            log.severe("Ошибка в SkladDB getPrilojenieTTN(String nomer) " + e.getMessage());
        }
        return v;
    }

//    public boolean getDateClosing(String nttn) {
//
//        boolean workVariable = false;
//        int ttnid = 0;
//        String query1 = "select date_closing from _put_list where ttn_id=?";
//        String query = "select item_id from otgruz1 where ndoc=?";
//        try {
//            ps = conn.prepareStatement(query);
//            ps.setString(1, nttn);
//            rs = ps.executeQuery();
//            if (rs.next()) {
//                ttnid = rs.getInt(1);
//            }
//            ps = conn.prepareStatement(query1);
//            ps.setInt(1, ttnid);
//            rs = ps.executeQuery();
//            if (rs.next()) {
//                if (rs.getDate(1) != null && rs.getDate(1).toString().equals("")) {
//                    workVariable = true;
//                }
//            }
//
//        } catch (Exception e) {
//            log.error("Ошибка в SkladDB getDateConversionPrice(String nttn)", e);
//            System.err.println("Ошибка в SkladDB getDateConversionPrice(String nttn)" + e.getMessage());
//        }
//        return workVariable;
//    }

    @SneakyThrows
    public void setDateClosing(String nttn, String d) {
        Date dat = new Date(convertDateStrToLong(d));
        String query = "update _put_list set date=? where ttn_id=(select item_id from otgruz1 where ndoc=?)";
        try {
            ps = conn.prepareStatement(query);
            ps.setDate(1, dat);
            ps.setString(2, nttn);
            ps.executeUpdate();
        } catch (Exception e) {
            log.severe("Ошибка в SkladDB setDateClosing" + e.getMessage());
        }
    }

//    public int getTipSkidki(String ttn) {
//        String query = "select skidka_tip from otgruz1 where ndoc=?";
//        int result = 0;
//        try {
//            ps = conn.prepareStatement(query);
//            ps.setString(1, ttn);
//            rs = ps.executeQuery();
//            while (rs.next()) {
//                result = rs.getInt(1);
//            }
//        } catch (Exception e) {
//            log.severe("Ошибка в SkladDB getTipSkidki(String ttn)" + e);
//            System.err.println("Ошибка в SkladDB getTipSkidki(String ttn)" + e.getMessage());
//        }
//        return result;
//    }

    public int getStatusTTN(String ttn) {
        int stat = 0;
        String query = "select status from otgruz1 where ndoc=?";
        try (PreparedStatement ps = getConnection().prepareStatement(query)) {
            ps.setString(1, ttn);
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (Exception e) {
            log.severe("Ошибка в SkladDB statusTTN (String ttn)" + e.getMessage());
        }
        return stat;
    }

    public void setStatusCloseTTN(String ttn) {
        String query = "update otgruz1 set status=0 where ndoc=?";
        try (PreparedStatement ps = getConnection().prepareStatement(query)) {
            ps.setString(1, ttn);
            ps.executeUpdate();
        } catch (Exception e) {
            log.severe("Ошибка в SkladDB setStatusCloseTTN(String ttn)" + e.getMessage());
            JOptionPane.showMessageDialog(null, e);
        }
    }

    public int[] getRadioButtonPosition(String ttn) {
        int[] a = new int[7];
        int itemid = 0;
        String query = "select rbttn,rbeks,rbconversion,rbskidka,rbnds,rbtorgnadb, pressButt from _put_list where ttn_id=?";
        String query1 = "select item_id,ndoc from otgruz1 where ndoc=?";
        try (Connection conn = getConnection();
        PreparedStatement ps = conn.prepareStatement(query);
        PreparedStatement ps1 = conn.prepareStatement(query1);) {
            ps.setString(1, ttn);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                itemid = rs.getInt(1);
            }

            ps1.setInt(1, itemid);
            ResultSet rs1 = ps1.executeQuery();
            while (rs1.next()) {
                a[0] = rs1.getInt(1);
                a[1] = rs1.getInt(2);
                a[2] = rs1.getInt(3);
                a[3] = rs1.getInt(4);
                a[4] = rs1.getInt(5);
                a[5] = rs1.getInt(6);
                a[6] = rs1.getInt(7);
            }
        } catch (Exception e) {
            log.severe("Ошибка в SkladDB getRadioButtonPosition(String ttn)" + e.getMessage());
        }
        return a;
    }

    public void setRadioButtonPosition(String ttn, int[] arrayPos) {
        int[] a = new int[6];
        int itemid = 0;
        String query = "update _put_list set rbttn=?, rbeks=?, rbconversion=?, rbskidka=?, rbnds=?, rbtorgnadb=?,pressButt=? where ttn_id=?";
        String query1 = "select item_id,ndoc from otgruz1 where ndoc=?";
        try {
            ps = conn.prepareStatement(query1);
            ps.setString(1, ttn);
            rs = ps.executeQuery();
            while (rs.next()) {
                itemid = rs.getInt(1);
            }
            ps = conn.prepareStatement(query);
            ps.setInt(1, arrayPos[0]);
            ps.setInt(2, arrayPos[1]);
            ps.setInt(3, arrayPos[2]);
            ps.setInt(4, arrayPos[3]);
            ps.setInt(5, arrayPos[4]);
            ps.setInt(6, arrayPos[5]);
            ps.setInt(7, arrayPos[6]);
            ps.setInt(8, itemid);
            ps.executeUpdate();
        } catch (Exception e) {
            log.severe("Ошибка в SkladDB setRadioButtonPosition(String ttn, int[] arrayPos)" + e.getMessage());
        }
    }

    public double rounding(BigDecimal r) {
        int buf, buf1 = 0;
        buf = r.intValue() % 10;
        if (buf < 5) {
            buf1 = r.intValue() - buf;
        }
        if (buf >= 5) {
            buf1 = r.intValue() + (10 - buf);
        }
        return r.doubleValue();
    }

    public boolean thereIsARecordInPutList(String ttn) {
        boolean workVariable = false;
        int itemid = 0;
        String query = "select ttn_id from _put_list where ttn_id=?";
        String query1 = "select item_id,ndoc from otgruz1 where ndoc=?";
        try {
            ps = conn.prepareStatement(query1);
            ps.setString(1, ttn);
            rs = ps.executeQuery();
            if (rs.next()) {
                itemid = rs.getInt(1);
            }
            ps = conn.prepareStatement(query);
            ps.setInt(1, itemid);
            rs = ps.executeQuery();
            if (rs.next()) {
                workVariable = true;
            }

        } catch (Exception e) {
            log.severe("Ошибка в SkladDB thereIsARecordInPutList(String ttn)" + e);
            System.err.println("Ошибка в SkladDB thereIsARecordInPutList(String ttn)" + e.getMessage());
        }
        return workVariable;
    }

    /* public void chekSpecCena(String ttn){
     SpecCenaDB scdb = new SpecCenaDB();
     scdb.setSpecCena(ttn);
     }*/
    public String[] getAdresOtprav() {
        String[] adres = new String[2];
        String query = " select ltrim(rtrim(kl.NAIM)),ltrim(rtrim(adr.NAIM)) from ( select URADRES,NAIM,ITEM_ID from s_klient where KOD=-1 ) as kl "
                + " left join (select NAIM,KLIENT_ID from s_adres ) as adr on adr.KLIENT_ID=kl.ITEM_ID ";
        try {
            ps = conn.prepareStatement(query);
            rs = ps.executeQuery();
            if (rs.next()) {
                adres[0] = rs.getString(1) + ", " + rs.getString(2);
                adres[1] = rs.getString(2);
            }
        } catch (Exception e) {
            log.severe("Ошибка в SkladDB getAdresOtprav()" + e);
            System.err.println("Ошибка в SkladDB getAdresOtprav()" + e.getMessage());
        }
        return adres;
    }

    public boolean presenceSpecialPrice(String ttn) {
        boolean presence = false;
        String query = " SELECT kpl,fas,ncw ,nar,scena "
                + " FROM (SELECT kpl,item_id FROM otgruz1 WHERE ndoc=?) AS ot1 "
                + " left join (SELECT kod_izd,rzm_marh, rst_marh,doc_id,ncw,cenav FROM otgruz2) AS ot2 ON ot1.item_id=ot2.doc_id "
                + " left join (SELECT kod, kod1 FROM nsi_sd) AS sd ON sd.kod1=ot2.kod_izd "
                + " left join (SELECT kod, fas,nar FROM nsi_kld) AS kld ON sd.kod=kld.kod "
                + " join (SELECT skod_kontragenta, smodel, srazmer,srazmer_end, srost,srost_end, scena, svaluta,prikaz_in, prim "
                + " FROM _speccena AS ot2 WHERE prikaz_in in (SELECT TOP 1 prikaz_in FROM _speccena AS ot1 "
                + " WHERE ot2.smodel=ot1.smodel and ot2.srazmer=ot1.srazmer and ot2.srost=ot1.srost "
                + " and ot2.skod_kontragenta=ot1.skod_kontragenta /* and active_cena=1*/ "
                + " ORDER BY prikaz_in DESC)) AS sc ON (ot1.kpl=sc.skod_kontragenta or sc.skod_kontragenta=-1) and sc.smodel = kld.fas "
                + " and (sc.srazmer <= ot2.rzm_marh or sc.srazmer=-1 or ot2.rzm_marh >=sc.srazmer_end ) "
                + " and (sc.srost <= ot2.rst_marh or sc.srost=-1 or ot2.rst_marh>=sc.srost_end) /*and sd.kod1=418683*/ "
                + " GROUP BY kpl,fas, scena,sc.skod_kontragenta,nar,ncw/*,rzm_marh*/ ";
        try {
            ps = conn.prepareStatement(query);
            ps.setString(1, ttn);
            rs = ps.executeQuery();
            if (rs.next() && !presence) {
                presence = true;
            }
        } catch (Exception e) {
            log.severe("Ошибка в SkladDB presenceSpecialPrice(String ttn)" + e);
            System.err.println("Ошибка в SkladDB presenceSpecialPrice(String ttn)" + e.getMessage());
        }
        return presence;
    }

    void nullNDS(String nomer) {
        String query = "update otgruz2 set summa_nds=0, nds=0 where doc_id = (select item_id from otgruz1 where ndoc=?)";
        int kid = 0;
        float nds;
        try {
            ps = conn.prepareStatement(query);
            ps.setString(1, nomer);
            ps.executeUpdate();
            updateSumTTN(nomer);
        } catch (Exception e) {
            log.severe("Ошибка в SkladDB nullNDS(String ttn)" + e);
            System.err.println("Ошибка в SkladDB nullNDS(String ttn)" + e.getMessage());
        }
    }

    public boolean OpenCloseTTN(String ttn) {
        boolean workVariable = false;
        String query = "update otgruz1 set status=8 where ndoc=?";
        try {
            ps = conn.prepareStatement(query);
            ps.setString(1, ttn);
            int in = ps.executeUpdate();
            if (in != 1) {
                JOptionPane.showMessageDialog(null, "Проблемы с открытием. Свяжитесь с разработчиком.", "Внимание!!", JOptionPane.ERROR_MESSAGE);
                workVariable = false;
            } else {
                workVariable = true;
            }
        } catch (Exception e) {
            log.severe("Ошибка в SkladDB OpenCloseTTN(String ttn)" + e);
            System.err.println("Ошибка в SkladDB OpenCloseTTN(String ttn)" + e.getMessage());
            JOptionPane.showMessageDialog(null, "Ошибка при открытии накладной. " + e);
            workVariable = false;
        } finally {
            return workVariable;
        }
    }

    public Object[][] getRowsFromPolotno(String nttn) {
        Object[][] vRows = null;
        int i = 0;
        String query = "select id_str_from_otgruz2 from _otgruz_polotno as op "
                + "join otgruz2 as o2 on op.id_str_from_otgruz2= o2.item_id "
                + "join otgruz1 as o1 on o2.doc_id=o1.item_id "
                + "where o1.ndoc=?";
        try {
            ps = conn.prepareStatement(query);
            ps.setString(1, nttn);
            rs = ps.executeQuery();
            if (rs.next()) {
                query = " select ot2.item_id,nk.sar,nk.nar,cd.ncw,nk.ngpr,op.kolvo_upack,op.massa_upack,op.massa_condit from otgruz2 as ot2 "
                        + " left join nsi_sd as ns on kod_izd=ns.kod1 "
                        + " left join nsi_kld as nk on nk.kod=ns.kod "
                        + " left join _otgruz_polotno as op on ot2.item_id=op.id_str_from_otgruz2 "
                        + " left join nsi_cd as cd on cd.cw=ot2.rzm_marh "
                        + " where doc_id = (select item_id from otgruz1 as ot1 where ndoc=?) ";
                /* + "  order by nk.sar  ";*/
                try {
                    ps = conn.prepareStatement(query);
                    ps.setString(1, nttn);
                    rs = ps.executeQuery();
                    i = 0;
                    while (rs.next()) {
                        i++;
                    }
                    rs = ps.executeQuery();
                    vRows = new Object[i][8];
                    i = 0;
                    while (rs.next()) {
                        if (rs.getString(2).toString().trim().startsWith("470")) {
                            vRows[i][0] = rs.getString(1).toString().trim();
                            vRows[i][1] = rs.getString(2).toString().trim();
                            vRows[i][2] = rs.getString(3).toString().trim();
                            vRows[i][3] = rs.getString(4).toString().trim();
                            vRows[i][4] = rs.getString(5).toString().trim();
                            vRows[i][5] = rs.getString(6).toString().trim();
                            //vRows[i][7] = rs.getString(7).toString().trim();
                            //vRows[i][6] = rs.getString(8).toString().trim();
                        } else {
                            vRows[i][0] = rs.getString(1).toString().trim();
                            vRows[i][1] = rs.getString(2).toString().trim();
                            vRows[i][2] = rs.getString(3).toString().trim();
                            vRows[i][3] = rs.getString(4).toString().trim();
                            vRows[i][4] = rs.getString(5).toString().trim();
                            vRows[i][5] = rs.getDouble(6);
                            vRows[i][6] = rs.getString(7).toString().trim();
                            vRows[i][7] = rs.getString(8).toString().trim();
                        }
                        i = i + 1;
                    }
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, e);
                }
            } else {

                query = " select ot2.item_id,nk.sar,nk.nar,cd.ncw,nk.ngpr from otgruz2 as ot2  "
                        + " left join nsi_sd as ns on kod_izd=ns.kod1 "
                        + " left join nsi_kld as nk on nk.kod=ns.kod "
                        + " left join nsi_cd as cd on cd.cw=ot2.rzm_marh          "
                        + "where doc_id = (select item_id from otgruz1 as ot1 where ndoc=?)";
                /*+ " order by nk.sar ";*/
                try {
                    ps = conn.prepareStatement(query);
                    ps.setString(1, nttn);
                    rs = ps.executeQuery();
                    i = 0;
                    while (rs.next()) {
                        i++;
                    }
                    rs = ps.executeQuery();
                    vRows = new Object[i][5];
                    i = 0;
                    while (rs.next()) {
                        vRows[i][0] = rs.getString(1).toString().trim();
                        vRows[i][1] = rs.getString(2).toString().trim();
                        vRows[i][2] = rs.getString(3).toString().trim();
                        vRows[i][3] = rs.getString(4).toString().trim();
                        vRows[i][4] = rs.getString(5).toString().trim();
                        i = i + 1;
                    }
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "Error this getRowsFromPolotno" + e);
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
        return vRows;
    }

    public void saveValuesInTabPolotno(Object[][] data) {
        String insert = "INSERT INTO _otgruz_polotno "
                + " values (?,round(?,2),round(?,2),'',0) ";
        String update = "update _otgruz_polotno set kolvo_upack=round(?,2),massa_surov=round(?,2), massa_condit=0 "
                + " where id_str_from_otgruz2=? ";
        String query = "select id from _otgruz_polotno where id_str_from_otgruz2=?";
        try {
            ps = conn.prepareStatement(query);
            ps.setString(1, data[0][0].toString());
            rs = ps.executeQuery();
            if (rs.next()) {
                for (int i = 0; i < (data.length); i++) {
                    //data.i
                    String number = data[i][0].toString();
                    float upack = Float.valueOf(data[i][5].toString());
                    float mass = Float.valueOf(data[i][6].toString());
                    ps = conn.prepareStatement(update);
                    ps.setString(3, number);
                    ps.setFloat(1, upack);
                    ps.setFloat(2, mass);
                    ps.executeUpdate();
                }
            } else {
                for (int i = 0; i < (data.length); i++) {
                    //data.i
                    String number = data[i][0].toString();
                    float upack = Float.valueOf(data[i][5].toString());
                    float mass = Float.valueOf(data[i][6].toString());
                    ps = conn.prepareStatement(insert);
                    ps.setString(1, number);
                    ps.setFloat(2, upack);
                    ps.setFloat(3, mass);
                    ps.executeUpdate();
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error in class SkladDB.java method saveValuesInTabPolotno: " + e);
            System.err.println("Error in class SkladDB.java method saveValuesInTabPolotno: " + e);
        }
    }

    public void saveValuesInTabPryazha(Object[][] data) {
        String insert = "INSERT INTO _otgruz_polotno "
                + " values (?,round(?,2),0,round(?,2),'',?) ";
        String update = "update _otgruz_polotno set kolvo_upack=round(?,2),massa_condit=round(?,2),massa_surov=round(?,2) "
                + " where id_str_from_otgruz2=? ";
        String query = "select id from _otgruz_polotno where id_str_from_otgruz2=? ";
        try {
            ps = conn.prepareStatement(query);
            ps.setString(1, data[0][0].toString());
            rs = ps.executeQuery();
            if (rs.next()) {
                for (int i = 0; i < (data.length); i++) {
                    //data.i
                    String number = data[i][0].toString();
                    float upack = Float.valueOf(data[i][5].toString());
                    float mass = Float.valueOf(data[i][6].toString());
                    float massa = Float.valueOf(data[i][7].toString());
                    //String ed_izm = data[i][8].toString();
                    ps = conn.prepareStatement(update);
                    ps.setString(4, number);
                    ps.setFloat(1, upack);
                    ps.setFloat(2, mass);
                    //ps.setString(3, ed_izm);
                    ps.setFloat(3, massa);
                    ps.executeUpdate();
                }
            } else {
                for (int i = 0; i < (data.length); i++) {
                    //data.i
                    String number = data[i][0].toString();
                    float upack = Float.valueOf(data[i][5].toString());
                    float mass = Float.valueOf(data[i][6].toString());
                    float massa = Float.valueOf(data[i][7].toString());
                    //String ed_izm = data[i][8].toString();
                    ps = conn.prepareStatement(insert);
                    ps.setString(1, number);
                    ps.setFloat(2, upack);
                    ps.setFloat(3, mass);
                    //ps.setString(4, ed_izm);
                    ps.setFloat(4, massa);
                    ps.execute();
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error in class SkladDB.java method saveValuesInTabPryazha: \n" + e);
            System.err.println("Error in class SkladDB.java method saveValuesInTabPryazha: \n" + e);
        }
    }

    public void updateSummForPolotnoDefaultNDS(String nn, float nds, float kurs) {
        String cena_kod_str = " select cena,item_id from otgruz2 where doc_id=(select item_id from otgruz1 where ndoc=?)";
        String cenav_kod_str = " select cnp from nsi_sd where kod1 in (select kod_izd from otgruz2 where item_id=?) ";
        String kol_kod_str = "select massa_surov from _otgruz_polotno where id_str_from_otgruz2=?";
        String updateSum = "update otgruz2 set summa=?, summa_nds=?, itogo=?,cenav=?,summav=?,summa_ndsv=?, itogov=?,nds=? where item_id=?";
        PreparedStatement ps2, ps3, ps4;
        ResultSet rs2, rs4;
        BigDecimal sum, sum_nds, itog, n, sumv, sum_ndsv, itogv;
        double cena, summa, summa_nds, summa_all, kol, cenav, summav, summa_ndsv, itogov;
        double ndss = 0.2;
        try {
            ps = conn.prepareCall(cena_kod_str);
            ps.setString(1, nn);
            rs = ps.executeQuery();
            while (rs.next()) {
                try {
                    ps2 = conn.prepareStatement(kol_kod_str);
                    ps2.setInt(1, rs.getInt(2));
                    rs2 = ps2.executeQuery();
                    ps4 = conn.prepareCall(cenav_kod_str);
                    ps4.setInt(1, rs.getInt("item_id"));
                    rs4 = ps4.executeQuery();
                    rs4.next();
                    if (rs2.next()) {
                        try {
                            cenav = Double.valueOf(rs4.getDouble("cnp"));
                            cena = rs.getDouble(1);
                            kol = rs2.getDouble(1);
                            summa = cena * kol;
                            summav = cenav * kol;
                            if (nds == (-1)) {
                                sum_nds = new BigDecimal(summa * ndss);
                                sum_nds = sum_nds.setScale(1, BigDecimal.ROUND_HALF_UP);
                                summa_nds = sum_nds.doubleValue();
                                sum_ndsv = new BigDecimal(summav * ndss);
                                sum_ndsv = sum_ndsv.setScale(2, BigDecimal.ROUND_HALF_UP);
                                summa_ndsv = sum_ndsv.doubleValue();
                            } else {
                                sum_nds = new BigDecimal(summa * nds / 100);
                                sum_nds = sum_nds.setScale(1, BigDecimal.ROUND_HALF_UP);
                                summa_nds = sum_nds.doubleValue();
                                sum_ndsv = new BigDecimal(summav * nds / 100);
                                sum_ndsv = sum_ndsv.setScale(2, BigDecimal.ROUND_HALF_UP);
                                summa_ndsv = sum_ndsv.doubleValue();
                            }
                            summa_all = summa + summa_nds;
                            itogov = summav + summa_ndsv;
                            ps3 = conn.prepareStatement(updateSum);
                            ps3.setDouble(1, summa);
                            ps3.setDouble(2, summa_nds);
                            ps3.setDouble(3, summa_all);
                            ps3.setDouble(4, cenav);
                            ps3.setDouble(5, summav);
                            ps3.setDouble(6, summa_ndsv);
                            ps3.setDouble(7, itogov);
                            if (nds != (-1)) {
                                ps3.setDouble(8, nds);
                            } else {
                                ps3.setDouble(8, ndss);
                            }
                            ps3.setDouble(9, rs.getInt("item_id"));
                            ps3.executeUpdate();
                        } catch (Exception ex) {
                            JOptionPane.showMessageDialog(null, "Ошибка записи в БД цен на полотно");
                        }
                    }
                } catch (Exception ex) {
                    System.err.println("ERRORKA!!!  " + ex);
                }
            }
        } catch (Exception e) {
            System.err.println("ERRORKA in updateSummForPolotnoDefaultNDS!!!" + e);
        }
    }

    public void updateSummForPryazhaDefaultNDS(String nn, int nds) {
        String cena_kod_str = " select cena,item_id from otgruz2 where doc_id=(select item_id from otgruz1 where ndoc=?)";
        String kol_kod_str = "select massa_surov from _otgruz_polotno where id_str_from_otgruz2=?";
        String updateSum = "update otgruz2 set summa=?, summa_nds=?, itogo=? where item_id=?";
        PreparedStatement ps2, ps3;
        ResultSet rs2, rs3;
        double cena, summa, summa_nds, summa_all;
        //nn="сэ745704";
        //nds=-1;
        try {
            ps = conn.prepareCall(cena_kod_str);
            ps.setString(1, nn);
            //cs.setInt(1, nds);
            rs = ps.executeQuery();
            while (rs.next()) {
                try {
                    ps2 = conn.prepareStatement(kol_kod_str);
                    ps2.setInt(1, rs.getInt(2));
                    rs2 = ps2.executeQuery();
                    while (rs2.next()) {
                        cena = rs.getFloat(1);
                        summa = cena * rs2.getFloat(1);
                        if (nds == (-1)) {
                            summa_nds = summa * 0.2;
                        } else {
                            summa_nds = summa * nds;
                        }
                        summa_all = summa + summa_nds;
                        try {
                            ps3 = conn.prepareStatement(updateSum);
                            ps3.setDouble(1, summa);
                            ps3.setDouble(2, summa_nds);
                            ps3.setDouble(3, summa_all);
                            ps3.setInt(4, rs.getInt(2));
                            ps3.executeUpdate();
                        } catch (Exception ex) {
                        }
                    }
                } catch (Exception ex) {
                }

            }

        } catch (Exception e) {
            System.err.println("ERRORKA!!!" + e);
        }
    }

    public float getActualNDS(String sar, String nar, int kpl, String ndoc) {
        float nds = 0;

        if (sar.substring(4, 6).equals("99")) {
            nds = 0;
        }
        if (((sar.substring(4, 5).equals("0") && !sar.startsWith("47")) && (!nar.trim().toUpperCase().startsWith("Б"))) || ((sar.substring(2, 3).equals("0") && sar.startsWith("47")) && (!nar.trim().toUpperCase().startsWith("Б")))) {
            nds = 0;
        }

        if ((sar.startsWith("41")) && (sar.substring(2, 3).equals("1") || sar.substring(2, 3).equals("2") || sar.substring(2, 3).equals("5")) && (!nar.trim().toUpperCase().startsWith("Б"))) {
            nds = 20;
        }
        if ((sar.startsWith("42")) && (sar.substring(2, 3).equals("1") || sar.substring(2, 3).equals("2") || sar.substring(2, 3).equals("5")) && (!nar.trim().toUpperCase().startsWith("Б"))) {
            nds = 20;
        }
        if ((sar.startsWith("43")) && (sar.substring(2, 3).equals("1") || sar.substring(2, 3).equals("2") || sar.substring(2, 3).equals("5")) && (!nar.trim().toUpperCase().startsWith("Б"))) {
            nds = 20;
        }

        if ((sar.startsWith("41")) && (sar.substring(2, 3).equals("3") || sar.substring(2, 3).equals("6")) && (!nar.trim().toUpperCase().startsWith("Б"))) {
            nds = 10;
        }
        if ((sar.startsWith("42")) && (sar.substring(2, 3).equals("3") || sar.substring(2, 3).equals("6")) && (!nar.trim().toUpperCase().startsWith("Б"))) {
            nds = 10;
        }
        if ((sar.startsWith("43")) && (sar.substring(2, 3).equals("3") || sar.substring(2, 3).equals("6")) && (!nar.trim().toUpperCase().startsWith("Б"))) {
            nds = 10;
        }

        if ((sar.startsWith("47") && !sar.substring(2, 3).equals("0")) && (!nar.trim().toUpperCase().startsWith("Б"))) {
            nds = 20;
        }


        // Пляски с бубном вокруг товаров артикулы которых не входят в республиканский перечень
        if (sar.equals("42321107") || sar.equals("41347900") || sar.equals("42321107") || sar.equals("41347900") || sar.equals("41347901") || sar.equals("41347902")) {
            // ||sar.equals("410176006") ||sar.equals("410176010") ) {
            String operac = getTypeDocument(ndoc);
            if (operac != null) {
                if (operac.trim().equals("Возврат от покупателя")) {
                    nds = 20;
                } else {
                    nds = 20;
                }
            }
        }
/*

        // Пляски с бубном вокруг товаров артикулы которых не входят в республиканский перечень
        if (sar.equals("410176006") ||sar.equals("410176010") ) {
            String operac = getTypeDocument(ndoc);
            if (operac != null) {
                    nds = 50;
            }
        }
*/


        if (kpl == 2281 || kpl == 8889 || kpl == 3451 || kpl == 5270) {
            nds = 0;
        }


        return nds;
    }

    public float getActualNDS(int kod_izd) {
        float nds = 0;
        PreparedStatement ps2 = null;
        ResultSet rs2 = null;
        String query = "select sar,nar from nsi_sd as sd "
                + " left join (select sar,nar,kod from nsi_kld) as kld on kld.kod=sd.kod "
                + " where sd.kod1=? ";
        try {
            ps2 = conn.prepareStatement(query);
            ps2.setInt(1, kod_izd);
            rs2 = ps2.executeQuery();
            if (rs2.next()) {
                nds = getActualNDS(rs2.getString(1), rs2.getString(2), 0, "1000001");
            }
        } catch (Exception ex) {
            System.err.println("Error in metod getActualNDS(int kod_izd)" + ex);
        }
        return nds;
    }

    public void getIzm(HashMap m) {
        m.put("izm", "шт.");
        if (m.get("sar").toString().startsWith("47")) {
            m.put("izm", "кг.");
        }

        if (m.get("sar").toString().startsWith("4700")) {
            m.put("izm", "кг.");
        }

        if (m.get("sar").toString().startsWith("4701")) {
            m.put("izm", "м.");
        }

        if (m.get("sar").toString().startsWith("48")) {
            m.put("izm", "кг.");
        }

        if (m.get("sar").toString().startsWith("43")) {
            m.put("izm", "пар");
        }

        if (getIzm().equals("")) {
            izm = m.get("izm").toString();
        }

        if (type_prod.equals("")) {
            type_prod = m.get("type_prod").toString();
        }
    }

    public HashMap getRazrezArt(String ttn) {
        HashMap result = new HashMap();
        HashMap buffer = new HashMap();
        int i = 0;
        String query = "select sar,narp,ngpr, sum(kol) as pach,sum(kol_in_upack*kol) as kol,sum(itogo) as itogo,sum(massa * kol_in_upack*kol) as massa from otgruz1 as ot1 "
                + "left join otgruz2 as ot2 on ot1.item_id=ot2.doc_id "
                + "left join nsi_sd as sd on ot2.kod_izd=sd.kod1 "
                + "left join nsi_kld as kld on sd.kod=kld.kod "
                + "where ndoc=? "
                + "group by sar,narp,ngpr,prim ";
        try {
            ps = conn.prepareStatement(query);
            ps.setString(1, ttn);
            rs = ps.executeQuery();
            while (rs.next()) {
                buffer = new HashMap();
                buffer.put("sar", rs.getString("sar").trim());
                buffer.put("narp", rs.getString("narp").trim());
                buffer.put("ngpr", rs.getString("ngpr").trim());
                buffer.put("pach", rs.getString("pach").trim());
                buffer.put("kol", rs.getString("kol").trim());
                buffer.put("itogo", rs.getString("itogo").trim());
                buffer.put("massa", rs.getString("massa").trim());
                result.put(i, buffer);
                i++;
            }
        } catch (Exception ex) {
            System.err.println("Error in class getRazrezArt()" + ex);
        }
        return result;
    }

    public HashMap getRazrezTNVED(String ttn) {
        HashMap result = new HashMap();
        HashMap buffer = new HashMap();
        int i = 0;
        String query = " select narp,ltrim(rtrim(replace(replace(replace(ngpr,'  ',' '),'   ',' '),'    ',' '))) as ngpr, "
                + " sum(kol) as pach,sum(kol_in_upack*kol) as kol,sum(itogov) as itogo, "
                + " sum(round((massa* kol_in_upack*kol),2)) as massa from otgruz1 as ot1 "
                + " left join otgruz2 as ot2 on ot1.item_id=ot2.doc_id "
                + " left join nsi_sd as sd on ot2.kod_izd=sd.kod1 "
                + " left join nsi_kld as kld on sd.kod=kld.kod "
                + " where ndoc =? "
                + " group by narp,ltrim(rtrim(replace(replace(replace(ngpr,'  ',' '),'   ',' '),'    ',' '))) "
                + " order by narp ";
        try {
            ps = conn.prepareStatement(query);
            ps.setString(1, ttn);
            rs = ps.executeQuery();
            while (rs.next()) {
                buffer = new HashMap();
                //buffer.put("sar", rs.getString("sar").trim());
                buffer.put("narp", rs.getString("narp").trim());
                buffer.put("ngpr", rs.getString("ngpr").trim());
                buffer.put("pach", rs.getString("pach").trim());
                buffer.put("kol", rs.getString("kol").trim());
                buffer.put("itogo", rs.getString("itogo").trim());
//                    BigDecimal n = new BigDecimal(rs.getFloat("massa"));
//                    n = n.setScale(2, BigDecimal.ROUND_HALF_DOWN);
                buffer.put("massa", rs.getFloat("massa"));
                result.put(i, buffer);
                i++;
            }
        } catch (Exception ex) {
            System.err.println("Error in class getRazrezArt()" + ex);
        }
        return result;
    }

    public HashMap getRazrezTNVEDPolotno(String ttn) {
        HashMap result = new HashMap();
        HashMap buffer = new HashMap();
        int i = 0;
        String query = " select narp,ltrim(rtrim(replace(replace(replace(ngpr,'  ',' '),'   ',' '),'    ',' '))) as ngpr, "
                + " sum(kolvo_upack) as pach,sum(massa_surov) as kol,sum(itogov) as itogo, "
                + " sum(round((massa_surov),2)) as massa from otgruz1 as ot1 "
                + " left join otgruz2 as ot2 on ot1.item_id=ot2.doc_id "
                + " left join nsi_sd as sd on ot2.kod_izd=sd.kod1 "
                + " left join nsi_kld as kld on sd.kod=kld.kod "
                + " left join  _otgruz_polotno as op on op.id_str_from_otgruz2=ot2.item_id "
                + " where ndoc =? "
                + " group by narp,ltrim(rtrim(replace(replace(replace(ngpr,'  ',' '),'   ',' '),'    ',' '))) "
                + " order by narp ";
        try {
            ps = conn.prepareStatement(query);
            ps.setString(1, ttn);
            rs = ps.executeQuery();
            while (rs.next()) {
                buffer = new HashMap();
                //buffer.put("sar", rs.getString("sar").trim());
                buffer.put("narp", rs.getString("narp").trim());
                buffer.put("ngpr", rs.getString("ngpr").trim());
                buffer.put("pach", rs.getString("pach").trim());
                buffer.put("kol", rs.getString("kol").trim());
                buffer.put("itogo", rs.getString("itogo").trim());
//                    BigDecimal n = new BigDecimal(rs.getFloat("massa"));
//                    n = n.setScale(2, BigDecimal.ROUND_HALF_DOWN);
                buffer.put("massa", rs.getFloat("massa"));
                result.put(i, buffer);
                i++;
            }
        } catch (Exception ex) {
            System.err.println("Error in class getRazrezArt()" + ex);
        }
        return result;
    }

    public HashMap getRazrezArtALL(String ttn) {
        HashMap result = new HashMap();
        HashMap buffer = new HashMap();
        int i = 0;
        String query = " select sar,narp,ngpr,fas, sum(kol) as pach,sum(round((massa* kol_in_upack*kol),2)) as massa,sum(kol_in_upack*kol) "
                + " as kol,sum(ot2.summav) as summa,sum(ot2.summa_ndsv) as summa_nds from otgruz1 as ot1 "
                + " left join otgruz2 as ot2 on ot1.item_id=ot2.doc_id "
                + " left join nsi_sd as sd on ot2.kod_izd=sd.kod1 "
                + " left join nsi_kld as kld on sd.kod=kld.kod "
                + " where ndoc=?"
                + " group by sar,narp,ngpr,fas "
                + " order by sar";
        try {
            ps = conn.prepareStatement(query);
            ps.setString(1, ttn);
            rs = ps.executeQuery();
            while (rs.next()) {
                buffer = new HashMap();
                buffer.put("ngpr", rs.getString("ngpr").trim());
                buffer.put("fas", rs.getString("fas").trim());
                buffer.put("pach", rs.getString("pach").trim());
                buffer.put("massa", rs.getString("massa").trim());
                buffer.put("kol", rs.getString("kol").trim());
                buffer.put("summa", rs.getString("summa").trim());
                buffer.put("summa_nds", rs.getString("summa_nds").trim());
                result.put(i, buffer);
                i++;
            }
        } catch (Exception ex) {
            System.err.println("Error in class getRazrezArt()" + ex);
        }
        return result;
    }

    public HashMap getRazrezArtALLPolotno(String ttn) {
        HashMap result = new HashMap();
        HashMap buffer = new HashMap();
        int i = 0;
        String query = " select sar,narp,ngpr,fas, sum(kolvo_upack) as pach,sum(round((massa_surov),2)) as massa,sum(massa_surov) as kol, "
                + " sum(ot2.summav) as summa,sum(ot2.summa_ndsv) as summa_nds from otgruz1 as ot1 "
                + " left join otgruz2 as ot2 on ot1.item_id=ot2.doc_id "
                + " left join nsi_sd as sd on ot2.kod_izd=sd.kod1 "
                + " left join nsi_kld as kld on sd.kod=kld.kod "
                + " left join _otgruz_polotno as op on op.id_str_from_otgruz2=ot2.item_id "
                + " where ndoc = ? "
                + " group by sar,narp,ngpr,fas "
                + " order by sar ";
        try {
            ps = conn.prepareStatement(query);
            ps.setString(1, ttn);
            rs = ps.executeQuery();
            while (rs.next()) {
                buffer = new HashMap();
                buffer.put("ngpr", rs.getString("ngpr").trim());
                buffer.put("fas", rs.getString("fas").trim());
                buffer.put("pach", rs.getString("pach").trim());
                buffer.put("massa", rs.getString("massa").trim());
                buffer.put("kol", rs.getString("kol").trim());
                buffer.put("summa", rs.getString("summa").trim());
                buffer.put("summa_nds", rs.getString("summa_nds").trim());
                result.put(i, buffer);
                i++;
            }
        } catch (Exception ex) {
            System.err.println("Error in class getRazrezArt()" + ex);
        }
        return result;
    }

    public HashMap returnSchetFaktura(String kka) {
        HashMap hmn = new HashMap();
        HashMap hmPoluch = new HashMap();
        HashMap hmOtprav = new HashMap();
        String sOtprav = " select isnull(kl.NAIM,'') as NAIM,isnull(TELEFON,'') as TELEFON,isnull(rs.NAIM,'') as NAIMRS,isnull(rs.NOMER,'') as NOMER, "
                + " isnull(bank.NAIM,'') as NAIMBANK, isnull(bank.PADRES,'') as PADRES,isnull(bank.MFO,'') as MFO,isnull(bank.KORSCHET,'') as KORSCHET, "
                + " isnull(bank.KORUNN,'') as KORUNN, isnull(kl.OKPO,'') as OKPO from s_klient as kl "
                + " left join s_adres as ad on ad.ITEM_ID=kl.POSTADRES "
                + " left join s_rschet as rs on rs.KLIENT_ID=kl.ITEM_ID "
                + " left join s_bank as bank on bank.ITEM_ID=rs.BANK_ID "
                + " where KOD=1317 and rs.NAIM not like '%Осн%' ";
        String sPoluch = " select isnull(kl.NAIM,'') as NAIM,isnull(TELEFON,'') as TELEFON,isnull(rs.NAIM,'') as NAIMRS,isnull(rs.NOMER,'') as NOMER, "
                + " isnull(bank.NAIM,'') as NAIMBANK, isnull(bank.PADRES,'') as PADRES,isnull(bank.MFO,'') as MFO,isnull(bank.KORSCHET,'') as KORSCHET, "
                + " isnull(bank.KORUNN,'') as KORUNN, isnull(kl.OKPO,'') as OKPO from s_klient as kl "
                + " left join s_adres as ad on ad.ITEM_ID=kl.POSTADRES "
                + " left join s_rschet as rs on rs.KLIENT_ID=kl.ITEM_ID "
                + " left join s_bank as bank on bank.ITEM_ID=rs.BANK_ID "
                + " where KOD=? ";
        try {
            ps = conn.prepareStatement(sOtprav);
            rs = ps.executeQuery();

            if (rs.next()) {
                hmOtprav.put("post", rs.getString("NAIM").trim());
                hmOtprav.put("adres", rs.getString("PADRES").trim() + ", Телефоны: " + rs.getString("TELEFON").trim());
                // hmOtprav.put("rs", rs.getString("NAIMRS")+": "+rs.getString("NOMER"));
                hmOtprav.put("banknaim", rs.getString("NAIMBANK").trim());
                hmOtprav.put("bankadr", rs.getString("PADRES").trim() + ", МФО: " + rs.getString("MFO").trim());
                hmOtprav.put("ks", "K/C: " + rs.getString("KORSCHET").trim());
                hmOtprav.put("inn", " ИНН: " + rs.getString("KORUNN").trim());
                hmOtprav.put("okpo", rs.getString("OKPO").trim());
            }
            String rsb = "Расч./Счет: " + rs.getString("NAIMRS").trim() + ": " + rs.getString("NOMER").trim() + "; ";
            while (rs.next()) {
                rsb = rsb + rs.getString("NAIMRS").trim() + ": " + rs.getString("NOMER").trim() + "; ";
            }
            hmOtprav.put("rs", rsb);
            hmn.put("otprav", hmOtprav);


            ps = conn.prepareStatement(sPoluch);
            ps.setString(1, kka);
            rs = ps.executeQuery();
            if (rs.next()) {
                hmPoluch.put("post", rs.getString("NAIM").trim());
                hmPoluch.put("adres", rs.getString("PADRES").trim() + " Телефоны: " + rs.getString("TELEFON").trim());
                // hmOtprav.put("rs", rs.getString("NAIMRS")+": "+rs.getString("NOMER"));
                hmPoluch.put("banknaim", rs.getString("NAIMBANK").trim());
                hmPoluch.put("bankadr", rs.getString("PADRES").trim() + ", МФО:  " + rs.getString("MFO").trim());
                hmPoluch.put("ks", rs.getString("KORSCHET").trim());
                hmPoluch.put("inn", " ИНН: " + rs.getString("KORUNN").trim());
                hmPoluch.put("okpo", rs.getString("OKPO").trim());
            }
            rsb = "";
            rsb = "Расч./Счет: " + rs.getString("NAIMRS").trim() + ": " + rs.getString("NOMER").trim() + "; ";
            while (rs.next()) {
                rsb = rsb + rs.getString("NAIMRS").trim() + ": " + rs.getString("NOMER").trim() + "; ";
            }
            hmPoluch.put("rs", rsb);
            hmn.put("poluch", hmPoluch);

        } catch (Exception ex) {
            System.err.println("Error in public HashMap returnSchetFaktura(): " + ex);
        }
        return hmn;
    }

    public HashMap getDataForFactura(String ttn) {
        HashMap result = new HashMap();
        HashMap buffer = new HashMap();
        int i = 0;
        String query = " select kld.sar,ngpr,nar, sum(kol) as pach,sum(kol_in_upack*kol) as kol,ot2.cenav ,sum(ot2.summav)as summa, "
                + " ot2.nds,sum(summa_ndsv) as summa_nds,sum(itogov) as summa_all,narp from otgruz1 as ot1 "
                + " left join otgruz2 as ot2 on ot1.item_id=ot2.doc_id "
                + " left join nsi_sd as sd on ot2.kod_izd=sd.kod1 "
                + " left join nsi_kld as kld on sd.kod=kld.kod "
                + " where ndoc=? "
                + " group by ngpr,prim,cenav,kld.sar,ot2.nds,narp,nar "
                + " order by kld.sar,nar ";
        try {
            ps = conn.prepareStatement(query);
            ps.setString(1, ttn);
            rs = ps.executeQuery();
            while (rs.next()) {
                buffer = new HashMap();
                buffer.put("sar", rs.getString("sar").trim());
                buffer.put("ngpr", rs.getString("ngpr").trim());
                buffer.put("nar", rs.getString("nar").trim());
                buffer.put("pach", rs.getString("pach").trim());
                buffer.put("kol", rs.getString("kol").trim());
                buffer.put("cenav", rs.getString("cenav").trim());
                buffer.put("summa", rs.getString("summa").trim());
                buffer.put("nds", rs.getString("nds").trim());
                buffer.put("summa_nds", rs.getString("summa_nds").trim());
                buffer.put("summa_all", rs.getString("summa_all").trim());
                buffer.put("narp", rs.getString("narp").trim());
                result.put(i, buffer);
                i++;
            }
        } catch (Exception ex) {
            System.err.println("Error in class getRazrezArt()" + ex);
        }
        return result;
    }

    public HashMap getDataForFacturaPolotno(String ttn) {
        HashMap result = new HashMap();
        HashMap buffer = new HashMap();
        int i = 0;
        String query = " select kld.sar,ngpr,nar, sum(kolvo_upack) as pach,sum(massa_surov) as kol,ot2.cenav ,sum(ot2.summav)as summa, "
                + " ot2.nds,sum(summa_ndsv) as summa_nds,sum(itogov) as summa_all,narp from otgruz1 as ot1 "
                + " left join otgruz2 as ot2 on ot1.item_id=ot2.doc_id "
                + " left join nsi_sd as sd on ot2.kod_izd=sd.kod1 "
                + " left join nsi_kld as kld on sd.kod=kld.kod "
                + " left join _otgruz_polotno as op on op.id_str_from_otgruz2=ot2.item_id "
                + " where ndoc =? "
                + " group by ngpr,prim,cenav,kld.sar,ot2.nds,narp,nar "
                + " order by kld.sar,nar ";
        try {
            ps = conn.prepareStatement(query);
            ps.setString(1, ttn);
            rs = ps.executeQuery();
            while (rs.next()) {
                buffer = new HashMap();
                buffer.put("sar", rs.getString("sar").trim());
                buffer.put("ngpr", rs.getString("ngpr").trim());
                buffer.put("nar", rs.getString("nar").trim());
                buffer.put("pach", rs.getString("pach").trim());
                buffer.put("kol", rs.getString("kol").trim());
                buffer.put("cenav", rs.getString("cenav").trim());
                buffer.put("summa", rs.getString("summa").trim());
                buffer.put("nds", rs.getString("nds").trim());
                buffer.put("summa_nds", rs.getString("summa_nds").trim());
                buffer.put("summa_all", rs.getString("summa_all").trim());
                buffer.put("narp", rs.getString("narp").trim());
                result.put(i, buffer);
                i++;
            }
        } catch (Exception ex) {
            System.err.println("Error in class getRazrezArt()" + ex);
        }
        return result;
    }

    public HashMap getInfoNakl(String tn) {
        HashMap hm = new HashMap();
        String query = " select data,sum(s) as SUMMA, sum(sn) as SUMMA_NDS,sum(sa) as SUMMA_ALL,pl.userName from otgruz1 as ot1 "
                + " left join (select sum(summa)as s, sum(summa_nds) as sn, sum(itogo) as sa, doc_id from otgruz2 group by doc_id) as ot2 on ot2.doc_id=ot1.item_id "
                + " left join (select [date] as data,ttn_id,userName from _put_list ) as pl on pl.ttn_id = ot2.doc_id "
                + " where ot1.ndoc = ? and status=0 "
                + " group by data,operac,ot1.ndoc,pl.userName "
                + " order by data ";
        try {
            ps = conn.prepareStatement(query);
            ps.setString(1, tn);
            rs = ps.executeQuery();
            if (rs.next()) {
                hm.put("summa", rs.getString("SUMMA"));
                hm.put("summaNds", rs.getString("SUMMA_NDS"));
                hm.put("summaAll", rs.getString("SUMMA_ALL"));
                hm.put("DateClosing", rs.getString("data"));
                hm.put("User", rs.getString("userName"));
            } else {
                hm.put("summa", "null");
            }
        } catch (Exception ex) {
            System.err.println("Error! (Class SkladDB in method getInfoNakl(String tn)): " + ex);
        }
        return hm;
    }

    public boolean transfNakl(String tnOld, String tnNew) {
        boolean flag = true;
        int id = 0;
        String clear_ot1 = " delete ot1 ";
        String clear_ot2 = " delete ot2 ";
        String copyToOt1 = " insert into ot1 (date,ndoc,kpodot,kpl,summa,summa_nds,summa_all,skidka,kola,kolk,kolr," +
                " status,ucenka3s,datevrkv,uservrkv,kpodvrkv,datekrkv,userkrkv, kpodkrkv,klient_id,vid_sert,vid_ggr," +
                " operac,export,valuta_id,kurs,skidka_tip,kurs_bank,nalogi) "
                + " select date,ndoc,kpodot,kpl,summa,summa_nds,summa_all,skidka,kola,kolk,kolr,status,ucenka3s," +
                " datevrkv,uservrkv,kpodvrkv,datekrkv,userkrkv, "
                + " kpodkrkv,klient_id,vid_sert,vid_ggr,operac,export,valuta_id,kurs,skidka_tip,kurs_bank," +
                "nalogi from otgruz1 where ndoc=? /*Номер возврата*/ ";
        String copyToOt2 = " insert into ot2 (scan,part,kol,cena,summa,nds,summa_nds,itogo,cenav,summav,summa_ndsv," +
                "itogov,kol_in_upack,tip,time_ins,pc_ins,eancode,doc_id,kod_str,preiscur,rzm_marh,rst_marh," +
                "kod_izd,ncw,srt,cena_uch) "
                + " select scan,part,kol,cena,summa,nds,summa_nds,itogo,cenav,summav,summa_ndsv,itogov,kol_in_upack,tip,time_ins,pc_ins,eancode,doc_id,kod_str, "
                + " preiscur,rzm_marh,rst_marh,kod_izd,ncw,srt,cena_uch from otgruz2 " +
                "where doc_id=(select item_id from otgruz1 where ndoc=?) /*Номер возврата*/ ";
        String copyToOtgruz2 = " insert into otgruz2 " +
                "(scan,part,kol,cena,summa,nds,summa_nds,itogo,cenav,summav,summa_ndsv,itogov,kol_in_upack,tip," +
                "time_ins,pc_ins,eancode,doc_id,kod_str, preiscur,rzm_marh,rst_marh,kod_izd,ncw,srt,cena_uch) "
                + " select scan,part,kol,cena,summa,nds,summa_nds,itogo,cenav,summav,summa_ndsv,itogov,kol_in_upack," +
                "tip,time_ins,pc_ins,eancode,? /*id накладной В которую копируем*/,kod_str,preiscur,rzm_marh," +
                "rst_marh,kod_izd,ncw,srt,cena_uch from ot2 " +
                "where doc_id=(select item_id from otgruz1 where ndoc=?) /*Номер возврата*/ ";
        String updateSumm = " update otgruz1 set summa=(select sum(summa) from otgruz2 where doc_id=? /*id накладной В которую копируем*/) " +
                "where item_id=? /*id накладной В которую копируем*/ ";
        String updateSummNds = " update otgruz1 set summa_nds=(select sum(summa_nds) from otgruz2 where doc_id=? /*id накладной В которую копируем*/) " +
                "where item_id=? /*id накладной В которую копируем*/ ";
        String updateSummAll = " update otgruz1 set summa_all=(select sum(itogo) from otgruz2 where doc_id=? /*id накладной В которую копируем*/) " +
                "where item_id=? /*id накладной В которую копируем*/ ";
        String getId = " select item_id from otgruz1 where ndoc = ?";

        if (flag) {
            try {
                ps = conn.prepareStatement(getId);
                ps.setString(1, tnNew);
                rs = ps.executeQuery();
                if (rs.next()) {
                    id = rs.getInt("item_id");
                }
            } catch (Exception ex) {
                System.err.println("Not get item_id from otgruz1. Check number doc.");
                flag = false;
            }
        }

        if (flag) {
            try {
                ps = conn.prepareStatement(clear_ot1);
                ps.execute();
                ps = conn.prepareStatement(clear_ot2);
                ps.execute();
            } catch (Exception ex) {
                System.err.println("Error! (Class SkladDB in method transfNakl(String tn)): Clear table 'ot1' and 'ot2' " + ex);
                flag = false;
            }
        }

        if (flag) {
            try {
                ps = conn.prepareStatement(copyToOt1);
                ps.setString(1, tnOld);
                ps.execute();
            } catch (Exception ex) {
                System.err.println("Error! (Class SkladDB in method transfNakl(String tn)): 'copyToOt1' " + ex);
                flag = false;
            }
        }

        if (flag) {
            try {
                ps = conn.prepareStatement(copyToOt2);
                ps.setString(1, tnOld);
                ps.execute();
            } catch (Exception ex) {
                System.err.println("Error! (Class SkladDB in method transfNakl(String tn)): 'copyToOt2' " + ex);
                flag = false;
            }
        }

        if (flag) {
            try {
                ps = conn.prepareStatement(copyToOtgruz2);
                ps.setInt(1, id);
                ps.setString(2, tnOld);
                ps.execute();
            } catch (Exception ex) {
                System.err.println("Error! (Class SkladDB in method transfNakl(String tn)): 'copyToOtgruz2' " + ex);
                flag = false;
            }
        }

        if (flag) {
            try {
                ps = conn.prepareStatement(updateSumm);
                ps.setInt(1, id);
                ps.setInt(2, id);
                ps.execute();
            } catch (Exception ex) {
                System.err.println("Error! (Class SkladDB in method transfNakl(String tn)): 'updateSumm' " + ex);
                flag = false;
            }
        }

        if (flag) {
            try {
                ps = conn.prepareStatement(updateSummNds);
                ps.setInt(1, id);
                ps.setInt(2, id);
                ps.execute();
            } catch (Exception ex) {
                System.err.println("Error! (Class SkladDB in method transfNakl(String tn)): 'updateSummNds' " + ex);
                flag = false;
            }
        }

        if (flag) {
            try {
                ps = conn.prepareStatement(updateSummAll);
                ps.setInt(1, id);
                ps.setInt(2, id);
                ps.execute();
            } catch (Exception ex) {
                System.err.println("Error! (Class SkladDB in method transfNakl(String tn)): 'updateSummAll' " + ex);
                flag = false;
            }
        }
        return flag;
    }

    public Map getProtSogl(String ndoc, float kurs, float nds) {
        Map hm = new HashMap();
        int i = 0;
        String query = " select sar,ngpr, nar, fas, cenav from "
                + " (select item_id, kurs, skidka from otgruz1 where ndoc = ?)as t "
                + " left join (select cena, kod_izd, cenav, doc_id from otgruz2 )as t1 on t.item_id = t1.doc_id "
                + " left join (select kod, kod1, cno from nsi_sd) as t3 on t1.kod_izd = t3.kod1 "
                + " left join (select sar,ngpr, fas, nar, kod from nsi_kld) as t2 on t3.kod = t2.kod "
                + " group by sar,ngpr, nar, fas, cno, cenav, cena, kurs, skidka order by sar,ngpr, nar, fas, cno ";
        try {
            ps = conn.prepareStatement(query);
            ps.setString(1, ndoc);
            rs = ps.executeQuery();
            while (rs.next()) {
                HashMap hash = new HashMap();
                hash.put("ngpr", rs.getString("ngpr").trim());
                hash.put("nar", rs.getString("nar").trim());
                hash.put("fas", rs.getString("fas").trim());
                BigDecimal n = new BigDecimal(rs.getFloat("cenav") * kurs * ((nds / 100) + 1));
                n = n.setScale(0, BigDecimal.ROUND_HALF_UP);
                hash.put("bc", n);
                n = new BigDecimal(rs.getFloat("cenav") * ((100 + nds) / 100));
                n = n.setScale(2, BigDecimal.ROUND_HALF_UP);
                hash.put("rc", n);
                hm.put(i, hash);
                i++;
            }
        } catch (Exception e) {
            System.err.println("Ошибка полученя списка " + e);
        }
        return hm;
    }

    public HashMap getAllPryazha() {

        HashMap hm = new HashMap();
        int i = 0;
        String query = " select sar,ngpr from nsi_kld where sar like '4700_torg' ";
        try {
            ps = conn.prepareStatement(query);
            rs = ps.executeQuery();
            while (rs.next()) {
                HashMap hash = new HashMap();
                hash.put("sar", rs.getString("sar"));
                hash.put("ngpr", rs.getString("ngpr"));
                hm.put(i, hash);
                i++;
            }
        } catch (Exception e) {
            System.err.println("Ошибка полученя списка " + e);
        }
        return hm;
    }

    public HashMap getAllNakl() {
        HashMap hm = new HashMap();
        int i = 0;
        String query = " select ndoc, date from otgruz1 where date>=dateadd(day,-30,getdate()) ";
        try {
            ps = conn.prepareStatement(query);
            rs = ps.executeQuery();
            while (rs.next()) {
                HashMap hash = new HashMap();
                hash.put("ndoc", rs.getString("ndoc"));
                hash.put("date", rs.getString("date"));
                hm.put(i, hash);
                i++;
            }
        } catch (Exception e) {
            System.err.println("Ошибка полученя списка " + e);
        }
        return hm;
    }

    public void createDbfFromShipping(String sd, String ed) {
        System.out.println(sd + " - " + ed);
        Calendar cal = Calendar.getInstance();
        String fileName;
        String query = " {? = call _dbf_from_shipping (?,?) }";
        //String myPath = "/home/user/";
        String myPath = "c:\\";
        if (cal.get(Calendar.MONTH) + 1 < 10) {
            fileName = "AR_O0" + String.valueOf(cal.get(Calendar.MONTH) + 1) + "d";
        } else {
            fileName = "AR_O" + String.valueOf(cal.get(Calendar.MONTH) + 1) + "d";
        }
        int i = 0;
        DBF dbf = null;
        String c1 = "", c2 = "";
        dbf = new DBF(4, fileName, myPath);
        try {
            cs = conn.prepareCall(query);
            cs.setString(2, sd);
            cs.setString(3, ed);
            cs.registerOutParameter(1, Types.OTHER);
            rs = cs.executeQuery();
            dbf.conn();
            while (rs.next()) {
                Object[] v = new Object[22];
                v[0] = rs.getInt(1);
                v[1] = rs.getInt(2);
                v[2] = rs.getInt(3);

                p1 = Pattern.compile("[a-zA-z]{0,2}");
                p = Pattern.compile("[а-яА-Я]{0,2}");
                c1 = p1.matcher(rs.getString(4).toString()).replaceAll("").trim();
                c1 = p.matcher(c1).replaceAll("").trim();
                //m = p.matcher();

                c2 = c1 + "1";
                v[3] = Long.valueOf(c1.trim());
                v[4] = Long.valueOf(c2.trim());

                v[5] = rs.getInt(6);
                v[6] = 0;
                v[7] = rs.getInt(8);
                v[8] = rs.getInt(9);
                v[9] = rs.getInt(10);
                v[10] = rs.getInt(11);
                v[11] = rs.getInt(12);

                if (rs.getString(23).trim().startsWith("Возв")) {
                    v[12] = rs.getInt(13) * (-1);
                } else {
                    v[12] = rs.getInt(13);
                }

                v[13] = rs.getFloat(14);
                v[14] = rs.getFloat(15);
                v[15] = rs.getFloat(16);
                v[16] = rs.getInt(17);
                v[17] = "";
                v[18] = rs.getFloat(19);
                v[19] = rs.getFloat(20);
                v[20] = rs.getFloat(21);
                v[21] = rs.getInt(22);
                i++;
                System.out.println("Запись");
                dbf.write(v);
            }

        } catch (Exception e) {
            System.err.println(e + String.valueOf(i));
            return;
        } finally {
            if (dbf != null) {
                dbf.disconn();
            }
        }
    }

    public boolean createDbfInventarization(String ndoc) {
        String fileName = "ostf";
        String query = " {? = call _dbf_inventarization (?) }";
        String myPath = "/home/user/";
        int i = 0;
        boolean fl = true;
        DBF dbf = null;
        dbf = new DBF(5, fileName, myPath);
        try {
            cs = conn.prepareCall(query);
            cs.setString(2, ndoc.trim());
            cs.registerOutParameter(1, Types.OTHER);
            rs = cs.executeQuery();
            dbf.conn();
            while (rs.next()) {
                Object[] v = new Object[19];
                v[0] = rs.getString(1);
                v[1] = rs.getFloat(2);
                v[2] = rs.getFloat(3);
                v[3] = rs.getFloat(4);
                v[4] = rs.getInt(5);
                v[5] = rs.getInt(6);
                v[6] = rs.getFloat(7);
                v[7] = rs.getInt(8);
                v[8] = rs.getInt(9);
                v[9] = rs.getInt(10);
                v[10] = rs.getInt(11);
                v[11] = rs.getFloat(12);
                v[12] = rs.getFloat(13);
                v[13] = rs.getFloat(14);
                v[14] = rs.getString(15).trim();
                v[15] = rs.getString(16);
                v[16] = rs.getInt(17);
                v[17] = rs.getString(18).trim();
                if (rs.getString(19).trim().length() <= 14) {
                    v[18] = rs.getString(19).trim();
                } else {
                    v[18] = rs.getString(19).trim().substring(0, 14);
                }
                i++;
                dbf.write(v);
            }
            return fl;
        } catch (Exception e) {
            System.err.println(e + String.valueOf(i));
            fl = false;
            return fl;
        } finally {
            if (dbf != null) {
                dbf.disconn();
                return fl;
            }
        }
    }

    public ArrayList getActualSpecCena(String ds, String de) {
        ArrayList<HashMap> v = new ArrayList();
        HashMap hm = new HashMap();

        String query = " SELECT skod_kontragenta, smodel,sart,ssort, srazmer,srazmer_end, srost,srost_end, scena,date_insert_record "
                + "FROM _speccena AS ot2 WHERE date_insert_record in (SELECT TOP 1 date_insert_record FROM _speccena AS ot1 "
                + "WHERE ot2.smodel=ot1.smodel and ot2.srazmer=ot1.srazmer and ot2.srost=ot1.srost and ot1.sart=ot2.sart "
                + "and ot2.skod_kontragenta=ot1.skod_kontragenta "
                + "ORDER BY date_insert_record DESC) and date_insert_record>=? "
                + " and date_insert_record<=? "
                + " order by skod_kontragenta, smodel,sart,srazmer,srazmer_end, srost,srost_end, scena,date_insert_record ";
        try {
            ps = conn.prepareStatement(query);
            ps.setString(1, ds);
            ps.setString(2, de);
            rs = ps.executeQuery();
            while (rs.next()) {
                HashMap h = new HashMap();
                h.put("skod_kontragenta", rs.getString("skod_kontragenta"));
                h.put("smodel", rs.getString("smodel"));
                h.put("sart", rs.getString("sart"));
                h.put("ssort", rs.getString("ssort"));
                h.put("srazmer", rs.getString("srazmer"));
                h.put("srazmer_end", rs.getString("srazmer_end"));
                h.put("srost", rs.getString("srost"));
                h.put("srost_end", rs.getString("srost_end"));
                h.put("scena", rs.getString("scena"));
                h.put("date_insert_record", rs.getString("date_insert_record"));
                v.add(h);
            }
        } catch (Exception ex) {
            System.err.println("Error! (Class SkladDB in method getActualSpecCena(String tn)): " + ex);
        }
        return v;
    }

    /**
     * метод возвращает набор договоров по контрагенту структуры Dogovor
     */

    public ArrayList<Dogovor> getClientDogovorList(String tn) {
        ArrayList<Dogovor> v = new ArrayList<Dogovor>();

        String query = "SELECT dbo.s_dogovor.ITEM_ID, dbo.s_dogovor.NAIM, dbo.s_dogovor.NOMER, dbo.s_dogovor.DATA "
                + "FROM dbo.s_dogovor INNER JOIN dbo.s_klient ON dbo.s_dogovor.KLIENT_ID = dbo.s_klient.ITEM_ID "
                + "INNER JOIN dbo.otgruz1 ON dbo.s_klient.KOD = dbo.otgruz1.kpl "
                + "WHERE (dbo.otgruz1.ndoc = ?)";
        try {
            ps = conn.prepareStatement(query);
            ps.setString(1, tn);
            rs = ps.executeQuery();
            while (rs.next()) {
                String ss = "";
                String[] a = new String[2];

                if (!rs.getString("NAIM").trim().equals("")) {
                    ss += rs.getString("NAIM").trim();
                }
                if (!rs.getString("NOMER").trim().equals("0") && !rs.getString("NOMER").trim().equals("")) {
                    ss += " № " + rs.getString("NOMER").trim();
                }
                if (!rs.getString("DATA").trim().equals("")) {
                    a = rs.getString("DATA").split(" ");
                }
                if (!a[0].trim().equals("1900-01-01")) {
                    String da = "";
                    a[0] = a[0].trim();
                    da += a[0].substring(8, 10) + ".";
                    da += a[0].substring(5, 7) + ".";
                    da += a[0].substring(0, 4);
                    ss += " от " + da;
                }
                v.add(new Dogovor(rs.getInt(1), ss));
            }
        } catch (Exception ex) {
            log.severe("Ошибка при выполнении функции getClientDogovorList(tn) " + ex);
            System.out.println("Ошибка при выполнении функции getClientDogovorList(tn) :" + ex);
        }
        return v;
    }

    /**
     * Метод устанавливает id договора контрагента для накладной при выборе оператором из выпадающего
     * списка в форме PrintTTN
     */
    public void setDogovorIDTTN(String ttn, int id) {
        String query = "update otgruz1 set dogovor_id=? where ndoc=?";
        try {
            ps = conn.prepareStatement(query);
            ps.setInt(1, id);
            ps.setString(2, ttn);
            ps.executeUpdate();
        } catch (Exception ex) {
            log.severe("Ошибка при выполнении функции setDogovorIDTTN(ttn, id) " + ex);
            System.out.println("Ошибка при выполнении функции setDogovorIDTTN(ttn, id) :" + ex);
        }
    }

    public void setPrePaymentTTN(String ttn, boolean prepayment) {
        String query = "update otgruz1 set prepayment=? where ndoc=?";
        try {
            ps = conn.prepareStatement(query);
            ps.setBoolean(1, prepayment);
            ps.setString(2, ttn);
            ps.executeUpdate();
        } catch (Exception ex) {
            log.severe("Ошибка при выполнении функции setPrePaymentTTN(ttn, b) " + ex);
            System.out.println("Ошибка при выполнении функции setPrePaymentTTN(ttn, b) :" + ex);
        }
    }

    /**
     * Метод обновляет ТТН с учетом валюты расчета, если расчет ведется в валюте, то
     * в поля summa, summa_nds, summa_all пишет значения сумм полей summav, summa_ndsv, itogov из otgruz2
     */
    public void updateSumTTN(String ndoc, boolean valuta) {
        try {
            // Запрос для сохранения суммы в бел. рублях
            String q1 = "update otgruz1 set summa = (select sum(summa) from otgruz2 where doc_id = (select item_id from otgruz1 where ndoc = ?)), "
                    + " summa_nds = (select sum(summa_nds) from otgruz2 where doc_id = (select item_id from otgruz1 where ndoc = ?)), "
                    + " summa_all = (select sum(itogo) from otgruz2 where doc_id = (select item_id from otgruz1 where ndoc = ?)) "
                    + " where ndoc = ? ";
            //Запрос для сохранения суммы в валюте
            String q2 = "update otgruz1 set summa = (select sum(summav) from otgruz2 where doc_id = (select item_id from otgruz1 where ndoc = ?)), "
                    + " summa_nds = (select sum(summa_ndsv) from otgruz2 where doc_id = (select item_id from otgruz1 where ndoc = ?)), "
                    + " summa_all = (select sum(itogov) from otgruz2 where doc_id = (select item_id from otgruz1 where ndoc = ?)) "
                    + " where ndoc = ? ";
            String query;

            // Проверка если расчет не в бел. рублях
            if (valuta) query = q2;
            else query = q1;
            ps = conn.prepareStatement(query);
            ps.setString(1, ndoc);
            ps.setString(2, ndoc);
            ps.setString(3, ndoc);
            ps.setString(4, ndoc);
            ps.executeUpdate();
        } catch (Exception e) {
            log.severe("Ошибка при выполнении функции updateSumTTN(ndoc, valuta)" + e);
            System.out.println("Ошибка при выполнении функции updateSumTTN(ndoc, valuta) :" + e);
        }
    }

    public void setNewPriceNakl(String nomer, Vector rows) throws Exception {
        Vector v = new Vector();
        String query = "update otgruz2 set " +
                " cena = ?, summa = ? * kol* kol_in_upack, " +
                " summa_nds = ? * kol* kol_in_upack *nds/100, itogo = ? * kol* kol_in_upack *nds/100 + ? * kol* kol_in_upack, " +
                " cenav = ?, summav = ? * kol* kol_in_upack, " +
                " summa_ndsv = ? * kol* kol_in_upack *nds/100, itogov = ? * kol* kol_in_upack *nds/100 + ? * kol* kol_in_upack" +
                " where doc_id = (select item_id from otgruz1 where ndoc = ?) and (kod_izd = ?) and (scan = ?)";
        //  and (scan = ?)
        try {
            setAutoCommit(false);
            ps = conn.prepareStatement(query);
            Iterator it = rows.iterator();
            while (it.hasNext()) {
                v = (Vector) it.next();
                ps.setLong(1, (Long) v.get(7));
                ps.setLong(2, (Long) v.get(7));
                ps.setLong(3, (Long) v.get(7));
                ps.setLong(4, (Long) v.get(7));
                ps.setLong(5, (Long) v.get(7));

                ps.setDouble(6, (Double) v.get(8));
                ps.setDouble(7, (Double) v.get(8));
                ps.setDouble(8, (Double) v.get(8));
                ps.setDouble(9, (Double) v.get(8));
                ps.setDouble(10, (Double) v.get(8));

                ps.setString(11, nomer);
                ps.setLong(12, (Long) v.get(9));
                // System.out.println(v.get(12));
                ps.setLong(13, (Long) v.get(12));
                ps.executeUpdate();
            }
            commit();
        } catch (Exception e) {
            log.severe("Ошибка изменении стоимости изделий в накладной " + e);
            System.err.println("Ошибка изменении стоимости изделий в накладной " + e.getMessage());
            rollback();
            throw e;
        } finally {
            setAutoCommit(true);
        }
    }

    public int getDogovorIDTTN(String ttn) {
        String query = "select dogovor_id from otgruz1 where ndoc=?";
        try {
            ps = conn.prepareStatement(query);
            ps.setString(1, ttn);
            rs = ps.executeQuery();
            while (rs.next()) {
                System.out.println("ID ДОГОВОРА :" + rs.getInt(1));
                return rs.getInt(1);
            }
            return 0;
        } catch (Exception ex) {
            log.severe("Ошибка при выполнении функции setDogovorIDTTN(ttn, id) " + ex);
            System.out.println("Ошибка при выполнении функции setDogovorIDTTN(ttn, id) :" + ex);
        }

        return 0;
    }

    public DogovorInfo getClientDogovor(int id) {
        DogovorInfo result = null;
        System.out.println("Вход в метод getClientDogovor с атрибутом " + id);

        String query = "SELECT dbo.s_dogovor.ITEM_ID, dbo.s_dogovor.NAIM, dbo.s_dogovor.NOMER, "
                + "dbo.s_dogovor.DATA, dbo.s_dogovor.DATA_END "
                + "FROM dbo.s_dogovor where dbo.s_dogovor.ITEM_ID = ? ";
        try {
            ps = conn.prepareStatement(query);
            ps.setInt(1, id);
            rs = ps.executeQuery();
            while (rs.next()) {
                System.out.println("Выполняется метод getClientDogovor");
                result = new DogovorInfo();

                result.setId(rs.getInt(1));
                result.setNaim(rs.getString(2).trim());
                result.setNumber(rs.getString(3));
                result.setDataBegin(rs.getDate(4));
                result.setDataEnd(rs.getDate(5));
                return result;
            }
        } catch (Exception ex) {
            log.severe("Ошибка при выполнении функции getClientDogovorList(tn) " + ex);
            System.out.println("Ошибка при выполнении функции getClientDogovorList(tn) :" + ex);
        }
        return null;
    }

    public String getClientNameByCode(String ttn) {
        String query = "select kpl from otgruz1 where ndoc=?";
        int kpl = 0;
        try {
            ps = conn.prepareStatement(query);
            ps.setString(1, ttn);
            rs = ps.executeQuery();
            while (rs.next()) {

                kpl = rs.getInt(1);
            }

        } catch (Exception ex) {
            log.severe("Ошибка при выполнении функции getClientNameByCode(ttn, id) " + ex);
            System.out.println("Ошибка при выполнении функции getClientNameByCode(ttn, id) :" + ex);
        }

        String result = "";
        query = "Select NAIM from s_klient where KOD = ?";
        try {
            ps = conn.prepareStatement(query);
            ps.setInt(1, kpl);
            rs = ps.executeQuery();
            while (rs.next()) {
                System.out.println("Выполняется метод getClientDogovor");
                result = rs.getString(1);
                return result;
            }
        } catch (Exception ex) {
            log.severe("Ошибка при выполнении функции getClientNaimByCode(code) " + ex);
            System.out.println("Ошибка при выполнении функции getClientNaimByCode(code) :" + ex);
        }
        return null;
    }

    void changePriceFork(String ttn, float ks, float skidka, float fixedRate, float saleRate, float nds, String val) {
        int kod_izd, kpl = 0;
        ArrayList listIzdel = new ArrayList();
        ArrayList listSpecCen = new ArrayList();
        HashMap hmm = null;
        float nds2;
        double cnp = -1, t = 0;
        String kp = "1.002", km = "1.01";
        // Получаем признак несортного товара
        boolean documentIsNoSort = getNoSortFlag(ttn);
        PreparedStatement ps2 = null;
        //System.out.println("Признак несорта " + documentIsNoSort);
        try {

            setAutoCommit(false);
            String query = "select sd.srt,kld.nar,kld.fas,kpl, ot2.item_id, kod_izd, kol, kol_in_upack, nds,rzm_marh, rst_marh,doc_id,ncw,cenav,cena_uch from otgruz2 as ot2 "
                    + "left join (select kpl,item_id from otgruz1 where ndoc=?) as ot1 on ot2.doc_id=ot1.item_id "
                    + "left join (SELECT kod, kod1,srt FROM nsi_sd) AS sd ON sd.kod1=ot2.kod_izd "
                    + "left join (SELECT kod, fas, nar FROM nsi_kld) AS kld ON sd.kod=kld.kod "
                    + "where doc_id=ot1.item_id ";

            ps = conn.prepareStatement(query);
            ps.setString(1, ttn);
            rs = ps.executeQuery();
            while (rs.next()) {
                hmm = new HashMap();
                hmm.put("kpl", rs.getInt("kpl"));
                hmm.put("item_id", rs.getInt("item_id"));
                hmm.put("kod_izd", rs.getInt("kod_izd"));
                hmm.put("kol", rs.getInt("kol"));
                hmm.put("kol_in_upack", rs.getInt("kol_in_upack"));
                hmm.put("nds", rs.getInt("nds"));
                hmm.put("rzm_marh", rs.getInt("rzm_marh"));
                hmm.put("rst_marh", rs.getInt("rst_marh"));
                hmm.put("doc_id", rs.getInt("doc_id"));
                hmm.put("ncw", rs.getString("ncw"));
                hmm.put("cenav", rs.getDouble("cenav"));
                hmm.put("fas", rs.getInt("fas"));
                hmm.put("nar", rs.getString("nar"));
                hmm.put("srt", rs.getInt("srt"));
                hmm.put("cena_uch", rs.getDouble("cena_uch"));
                listIzdel.add(hmm);
            }

            rs.close();
            query = "SELECT kpl FROM otgruz1 WHERE ndoc=?";
            ps = conn.prepareStatement(query);
            ps.setString(1, ttn);
            rs = ps.executeQuery();
            if (rs.next()) {
                kpl = rs.getInt(1);
            }
            query = " SELECT skod_kontragenta, smodel,sart,ssort, srazmer,srazmer_end, srost,srost_end, scena,date_insert_record "
                    + "FROM _speccena AS ot2 WHERE date_insert_record in (SELECT TOP 1 date_insert_record FROM _speccena AS ot1 "
                    + "WHERE ot2.smodel=ot1.smodel and ot2.srazmer=ot1.srazmer and ot2.srost=ot1.srost and ot1.sart=ot2.sart "
                    + "and ot2.skod_kontragenta=ot1.skod_kontragenta "
                    + "ORDER BY date_insert_record DESC) and skod_kontragenta=?"
                    + " order by skod_kontragenta, smodel,sart,srazmer,srazmer_end, srost,srost_end, scena,date_insert_record ";
            ps = conn.prepareStatement(query);
            ps.setInt(1, kpl);
            rs = ps.executeQuery();
            while (rs.next()) {
                hmm = new HashMap();
                hmm.put("skod_kontragenta", rs.getInt("skod_kontragenta"));
                hmm.put("smodel", rs.getInt("smodel"));
                hmm.put("sart", rs.getString("sart"));
                hmm.put("ssort", rs.getInt("ssort"));
                hmm.put("srazmer", rs.getInt("srazmer"));
                hmm.put("srazmer_end", rs.getInt("srazmer_end"));
                hmm.put("srost", rs.getInt("srost"));
                hmm.put("srost_end", rs.getInt("srost_end"));
                hmm.put("scena", rs.getDouble("scena"));
                hmm.put("date_insert_record", rs.getDate("date_insert_record"));
                listSpecCen.add(hmm);
            }
            rs.close();


            //setAutoCommit(false) ;
            for (int i = 0; i < listIzdel.size(); i++) {
                cnp = -1;
                hmm = new HashMap();
                hmm = (HashMap) listIzdel.get(i);
                kod_izd = Integer.valueOf(hmm.get("kod_izd").toString());

                double cnp_ = Double.valueOf(hmm.get("cena_uch").toString());

                if (nds == -1) {
                    nds2 = getActualNDS(kod_izd);
                } else {
                    nds2 = nds;
                }

                for (int j = 0; j < listSpecCen.size(); j++) {
                    HashMap x = new HashMap();
                    x = (HashMap) listSpecCen.get(j);
                    if ((hmm.get("fas").toString().trim().equals(x.get("smodel").toString().trim()))
                            && (hmm.get("srt").toString().trim().equals(x.get("ssort").toString().trim()))
                            && (hmm.get("nar").toString().trim().equals(x.get("sart").toString().trim()))
                            && (Integer.valueOf(hmm.get("rzm_marh").toString().trim()) <= Integer.valueOf(x.get("srazmer_end").toString().trim())
                            && Integer.valueOf(hmm.get("rzm_marh").toString().trim()) >= Integer.valueOf(x.get("srazmer").toString().trim()))
                            && (Integer.valueOf(hmm.get("rst_marh").toString().trim()) <= Integer.valueOf(x.get("srost_end").toString().trim())
                            && Integer.valueOf(hmm.get("rst_marh").toString().trim()) >= Integer.valueOf(x.get("srost").toString().trim()))) {
                        cnp = Double.valueOf(x.get("scena").toString().trim());
                        if (kpl == 1527 && cnp != -1) {
                            cnp = cnp * (double) ks;
                        }
                    }
                }


                // Признак наличия спеццены
                boolean spetialCost = false;
                if (!documentIsNoSort) {
                    if (cnp == -1) {
                        cnp = cnp_;
                        /*
                        query = "select cno from nsi_sd where kod1 = ?";
                        ps = conn.prepareStatement(query);
                        ps.setInt(1, kod_izd);
                        rs = ps.executeQuery();
                        if (rs.next()) {
                            cnp = rs.getDouble("cno");
                        }*/

                        cnp = cnp * ks * (100 - skidka) / 100;
                    } else {
                        spetialCost = true;
                    }
                } else {
                    // Документ для несортных изделий, цену берем из НСИ
                    query = "select cnp from nsi_sd where kod1 = ?";
                    ps = conn.prepareStatement(query);
                    ps.setInt(1, kod_izd);
                    rs = ps.executeQuery();
                    if (rs.next()) {
                        cnp = rs.getDouble("cnp");
                    }
                    cnp = cnp * ks * (100 - skidka) / 100;
                }

                BigDecimal n = new BigDecimal(cnp);
                n = n.setScale(2, BigDecimal.ROUND_HALF_UP);
                cnp = n.doubleValue();

                // Расчет документа и сохранение в таблице
                query = "update otgruz2 set cena = ?, summa = ?, nds = ?, summa_nds = ?, itogo = ?, "
                        + " cenav = ?, summav = ?, summa_ndsv = ?, itogov = ? where item_id = ?";
                ps2 = conn.prepareStatement(query);

               /* if (!val.trim().equals("RUB")) {
                    n = new BigDecimal(cnp / kurs);
                    n = n.setScale(2, BigDecimal.ROUND_HALF_UP);
                    ps.setDouble(6, n.doubleValue());
                } else {
                    ps.setDouble(6, cnp);
                }*/
                BigDecimal cena;
                Double rusCost;
                if (!documentIsNoSort) {
                    // Если у товара есть спец цена
                    if (spetialCost) {
                        rusCost = cnp;
                        cena = new BigDecimal(cnp);
                        // Если у товара нет спец цены
                    } else {
                        rusCost = cnp / fixedRate;
                        cena = new BigDecimal(cnp / fixedRate);
                    }
                } else {
                    rusCost = cnp;
                    cena = new BigDecimal(cnp);
                }
                //System.out.print(rusCost+" - ");
                n = new BigDecimal(rusCost);
                /*n = n.setScale(5, BigDecimal.ROUND_HALF_UP);
                n = n.setScale(4, BigDecimal.ROUND_HALF_UP);
                */
                //n = n.setScale(2, BigDecimal.ROUND_CEILING);
                n = n.setScale(2, BigDecimal.ROUND_HALF_UP);
                //Double rusCost_ = Math.floor((n.doubleValue() * 1e2) / 1e2);
                rusCost = n.doubleValue();
                //System.out.println(rusCost );

                cena = cena.setScale(2, BigDecimal.ROUND_HALF_UP);
/*                Double rusCost = cnp / kurs;
                double cena = cnp / kurs;*/

                ps2.setDouble(6, rusCost);
                cnp = n.doubleValue();

                int kol = Integer.valueOf(hmm.get("kol").toString()) * Integer.valueOf(hmm.get("kol_in_upack").toString());
                //System.out.println("Цена "+cena +" количество "+kol);
                n = new BigDecimal(cena.doubleValue() * kol);
                // n = n.setScale(5, BigDecimal.ROUND_HALF_UP);
                // n = n.setScale(4, BigDecimal.ROUND_HALF_UP);
                // n = n.setScale(3, BigDecimal.ROUND_HALF_UP);
                n = n.setScale(2, BigDecimal.ROUND_HALF_UP);
                // n = n.round(new MathContext(3, RoundingMode.HALF_UP));
                // n = new BigDecimal(n, new MathContext(2));
                //System.out.println(n.doubleValue());
                ps2.setDouble(7, n.doubleValue());
                t = n.doubleValue();
//
                n = new BigDecimal(t * (nds2 / 100));
                //n = new BigDecimal(n.doubleValue()* Integer.valueOf(hmm.get("kol").toString())*Integer.valueOf(hmm.get("kol_in_upack").toString()));
                n = n.setScale(3, BigDecimal.ROUND_HALF_UP);
                n = n.setScale(2, BigDecimal.ROUND_HALF_UP);
                ps2.setDouble(8, n.doubleValue());
                ps2.setDouble(9, t + n.doubleValue());
                ps2.setInt(10, Integer.valueOf(hmm.get("item_id").toString()));


                // Расчет цены в BYR отностительно курса валюты
                n = new BigDecimal(rusCost * saleRate);
                // n = n.setScale(3, BigDecimal.ROUND_HALF_UP);
                // n = n.setScale(2, BigDecimal.ROUND_HALF_UP);
                n = n.setScale(1, BigDecimal.ROUND_HALF_UP);
                n = n.setScale(0, BigDecimal.ROUND_HALF_DOWN);

                //System.out.println(String.format("%s - %s - %s", rusCost, saleRate, n.doubleValue()));

                ps2.setDouble(1, n.doubleValue());
                t = n.doubleValue();

                n = new BigDecimal(t * Integer.valueOf(hmm.get("kol").toString()) * Integer.valueOf(hmm.get("kol_in_upack").toString()));
                //n = n.setScale(3, BigDecimal.ROUND_HALF_UP);
                //n = n.setScale(2, BigDecimal.ROUND_HALF_UP);
                n = n.setScale(1, BigDecimal.ROUND_HALF_UP);
                n = n.setScale(0, BigDecimal.ROUND_HALF_UP);
                ps2.setDouble(2, n.doubleValue());
                t = n.intValue();

                ps2.setDouble(3, nds2);

                n = new BigDecimal(t * nds2 / 100);
                //n = n.setScale(3, BigDecimal.ROUND_HALF_UP);
                // n = n.setScale(2, BigDecimal.ROUND_HALF_UP);
                n = n.setScale(1, BigDecimal.ROUND_HALF_UP);
                n = n.setScale(0, BigDecimal.ROUND_HALF_UP);
                ps2.setDouble(4, n.doubleValue());

                BigDecimal bg = new BigDecimal(t + n.doubleValue());
                //n = n.setScale(3, BigDecimal.ROUND_HALF_UP);
                // n = n.setScale(2, BigDecimal.ROUND_HALF_UP);
                bg = bg.setScale(1, BigDecimal.ROUND_HALF_UP);
                n = n.setScale(0, BigDecimal.ROUND_HALF_UP);
                ps2.setDouble(5, bg.doubleValue());
                ps2.executeUpdate();
            }

            //commit();


            query = "update otgruz1 set summa=(select sum(summav) from otgruz2 where doc_id=(select item_id from otgruz1 where ndoc=?)), "
                    + " summa_nds=(select sum(summa_ndsv) from otgruz2 where doc_id=(select item_id from otgruz1 where ndoc=?)), "
                    + " summa_all=(select sum(itogov) from otgruz2 where doc_id=(select item_id from otgruz1 where ndoc=?)) "
                    + " where ndoc=?";
            ps = conn.prepareStatement(query);
            ps.setString(1, ttn);
            ps.setString(2, ttn);
            ps.setString(3, ttn);
            ps.setString(4, ttn);
            ps.executeUpdate();

            commit();
            if (ps != null) {
                try {
                    ps.close();
                } catch (Exception e) {
                    System.out.println("Error close preparedstatment" + e.getMessage());
                }
            }
            if (rs != null) {
                try {
                    rs.close();
                } catch (Exception e) {
                    System.out.println("Error close preparedstatment" + e.getMessage());
                }
            }
        } catch (Exception e) {
            rollback();
            System.out.println(e);
        } finally {
            setAutoCommit(true);
            if (ps != null) {
                try {
                    ps.close();
                } catch (Exception e) {
                    System.out.println("Error close preparedstatment" + e.getMessage());
                }
            }
            if (rs != null) {
                try {
                    rs.close();
                } catch (Exception e) {
                    System.out.println("Error close preparedstatment" + e.getMessage());
                }
            }
        }
    }

    public double roundAndGetDouble(double value, int roundIndex) {
        return roundBigDecimal(value, roundIndex).doubleValue();
    }

    /**
     * Метод проверяет отсутсвует ли данный артикул в республиканском перечне товаров для выбора НДС
     *
     * @param nar артикульный номер
     * @return
     */
    public boolean isNotPresentInTheList(String nar) {
        String query = "select listrb from nsi_kld where nar=?";
        try {
            ps = conn.prepareStatement(query);
            ps.setString(1, nar);
            rs = ps.executeQuery();
            while (rs.next()) {
                //System.out.println("ID ДОГОВОРА :" + rs.getInt(1));
                int result = rs.getInt(1);
                if (result == 1) {
                    System.out.println("Артикула нет в республиканском перечне");
                    return true;
                } else {
                    System.out.println("Артикул есть в республиканском перечне");
                    return false;
                }
            }
            return false;
        } catch (Exception ex) {
            log.severe("Ошибка при выполнении функции setDogovorIDTTN(ttn, id) " + ex);
            System.out.println("Ошибка при выполнении функции setDogovorIDTTN(ttn, id) :" + ex);
        }
        return false;
    }

    /**
     * Метод возвращает тип документа (отгрузка/возврат и т.д.)
     *
     * @param number номер документа
     */
    public String getTypeDocument(String number) {
        String query = "select operac from otgruz1 WHERE ndoc = ?";
        try {
            ps = conn.prepareStatement(query);
            ps.setString(1, number);
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString(1);
            }
        } catch (Exception ex) {
            log.severe("Ошибка при выполнении функции getTypeDocument " + ex);
        }
        return "";
    }

    /**
     * Метод изменяет признак документа на несортный РФ
     *
     * @param ttn      номер документа
     * @param selected флаг признака
     */
    public void setNoSortFlag(final String ttn, final boolean selected) {
        String query = "update otgruz1 set nosort=? where ndoc=?";
        try (PreparedStatement ps = getConnection().prepareStatement(query)) {
            ps.setBoolean(1, selected);
            ps.setString(2, ttn);
            ps.executeUpdate();
        } catch (Exception ex) {
            log.severe("Ошибка при выполнении функции setNoSortFlag(ttn, b) " + ex);
        }
    }

    public boolean getNoSortFlag(final String tn) {
        String query = "select nosort from otgruz1 WHERE ndoc = ?";
        try (PreparedStatement ps = getConnection().prepareStatement(query)) {
            ps.setString(1, tn);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getBoolean(1);
            }
        } catch (Exception ex) {
            log.severe("Ошибка при выполнении функции getNoSortFlag " + ex);
        }
        return false;
    }

    public boolean documentIsCalculated(final String docNumber) {
        String query = "select saved from otgruz1 WHERE ndoc = ?";
        try (PreparedStatement ps = getConnection().prepareStatement(query)) {
            ps.setString(1, docNumber);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getBoolean(1);
            }
        } catch (Exception ex) {
            log.severe("Ошибка при выполнении функции getNoSortFlag " + ex);
        }
        return false;
    }

    public boolean documentIsClosed(final String docNumber) {
        String query = "select status from otgruz1 WHERE ndoc = ?";
        try (PreparedStatement ps = getConnection().prepareStatement(query)) {
            ps.setString(1, docNumber);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int status = rs.getInt(1);
                if (status == 0) {
                    return true;
                }
            }
        } catch (Exception ex) {
            log.severe("Ошибка при выполнении функции documentIsClosed " + ex);
        }
        return false;
    }
}
