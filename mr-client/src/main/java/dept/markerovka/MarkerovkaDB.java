package dept.markerovka;

import by.gomel.freedev.ucframework.ucdao.jdbc.AbstractMSSQLServerJDBC;
import lombok.SneakyThrows;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import static common.UtilFunctions.convertDateStrToLong;

public class MarkerovkaDB extends AbstractMSSQLServerJDBC {

    private static final Logger log = Logger.getLogger(MarkerovkaDB.class.getName());

    public Map getAboutLabel(long barcode) {

        Map item = new HashMap();
        String query = "select naim, eancode, nar, fas, rzm, srt, ncw, gost, gost1, sostav1, sostav2, sostav3, sostav4, textil, data, rzm_marh, rst_marh, brend, komplekt, kod_izd, rzm_naim1,rzm_naim2, rzm_naim3 "
                + " from (select naim, eancode, nar, fas, rzm, srt, ncw, gost, gost1, sostav1, sostav2, sostav3, sostav4, "
                + " textil, data, rzm_marh, rst_marh, brend, komplekt, kod_izd from label_one where barcode = ?)as t1 "
                + " join (select rzm_naim1,rzm_naim2, rzm_naim3, kod from nsi_kld)as t2 on t1.kod_izd = t2.kod ";
        try (PreparedStatement ps = getConnection().prepareStatement(query)) {
            ps.setLong(1, barcode);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                item.put("naim", (rs.getString("naim") == null) ? "" : rs.getString("naim").trim().toUpperCase());
                item.put("ean", (rs.getString("eancode") == null) ? "" : rs.getString("eancode").trim());
                item.put("nar", (rs.getString("nar") == null) ? "" : rs.getString("nar").trim());
                item.put("fas", (rs.getString("fas") == null) ? "" : rs.getString("fas").trim());
                String rzm = (rs.getString("rzm") == null) ? "" : rs.getString("rzm").trim();
                item.put("rzm", rzm);
                //rzm += "-";
                String[] r = rzm.split("-");
                item.put("rzm1", "");
                item.put("rzm2", "");
                item.put("rzm3", "");
                if (r.length >= 1) item.put("rzm1", r[0]);
                if (r.length >= 2) item.put("rzm2", r[1]);
                if (r.length >= 3) item.put("rzm3", r[2]);
                item.put("srt", (rs.getString("srt") == null) ? "" : rs.getString("srt").trim());
                item.put("ncw", (rs.getString("ncw") == null) ? "" : rs.getString("ncw").trim());
                item.put("gost", (rs.getString("gost") == null) ? "" : rs.getString("gost").trim());
                item.put("gost1", (rs.getString("gost1") == null) ? "" : rs.getString("gost1").trim());
                item.put("sostav1", (rs.getString("sostav1") == null) ? "" : rs.getString("sostav1").trim());
                item.put("sostav2", (rs.getString("sostav2") == null) ? "" : rs.getString("sostav2").trim());
                item.put("sostav3", (rs.getString("sostav3") == null) ? "" : rs.getString("sostav3").trim());
                item.put("sostav4", (rs.getString("sostav4") == null) ? "" : rs.getString("sostav4").trim());
                item.put("textil", (rs.getString("textil") == null) ? "" : rs.getString("textil").trim());
                item.put("data", (rs.getString("data") == null) ? "" : rs.getString("data").trim());
                item.put("rst_marh", (rs.getString("rst_marh") == null) ? "" : rs.getString("rst_marh").trim());
                item.put("rzm_marh", (rs.getString("rzm_marh") == null) ? "" : rs.getString("rzm_marh").trim());
                item.put("Brend", (rs.getString("brend") == null) ? "" : rs.getString("brend").trim().toLowerCase());
                item.put("komplekt", (rs.getString("komplekt") == null) ? "" : rs.getString("komplekt").trim());
                item.put("LngRzm1", (rs.getString("rzm_naim1") == null) ? "" : rs.getString("rzm_naim1").trim());
                item.put("LngRzm2", (rs.getString("rzm_naim2") == null) ? "" : rs.getString("rzm_naim2").trim());
                item.put("LngRzm3", (rs.getString("rzm_naim3") == null) ? "" : rs.getString("rzm_naim3").trim());
                item.put("kod_izd", rs.getString("kod_izd").trim());
            }

        } catch (Exception e) {
            log.severe("Ошибка realOstSklad() " + e.getMessage());
        }
        return item;
    }

    /**
     * Возвращает список язков
     * @return ArrayList[Language]
     */
    public List<Language> getLaguages() {
        List<Language> lngs = new ArrayList<>();
        String query = "select id, code, name from _language";
        try (PreparedStatement ps = getConnection().prepareStatement(query)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Language lng = new Language(rs.getInt("id"), rs.getString("code"), rs.getString("name"));
                lngs.add(lng);
            }

        } catch (Exception e) {
            log.severe("Ошибка MarkerovkaDB.getLaguages()" + e.getMessage());
        }
        return lngs;
    }

    /**
     * Возвращает уникальный код изделия
     * @param kod -- код изделия в справочнике nsi_kld
     * @param srt -- сорт
     * @param rzm -- размер
     * @param rst -- рост
     * @return код изделия или -1 если код не найден
     */
    public int getItemId(int kod, int srt, int rzm, int rst) {
        String query = "select kod1 from nsi_sd where kod = ? and srt = ? and rzm= ? and rst = ?";
        try (PreparedStatement ps = getConnection().prepareStatement(query)) {
            ps.setInt(1, kod);
            ps.setInt(2, srt);
            ps.setInt(3, rzm);
            ps.setInt(4, rst);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("kod1");
            }
        } catch (Exception e) {
            log.severe("Ошибка MarkerovkaDB.getProductId(int kod, int srt, int rzm, int rst)" + e.getMessage());
        }
        return -1;
    }

    public Map getAboutLabelLng(Label l) throws Exception {

        Map item = new HashMap();
        String query = " select ngpr from _trans_item where kod_izd = ? and lang_id = ?";
        String query1 = " select var_name, var_lng from _trans_const where lang_id = ?";
        try (Connection con = getConnection();
        PreparedStatement ps = con.prepareStatement(query);
        PreparedStatement ps1 = con.prepareStatement(query1);) {
            ps.setInt(1, l.getItem_id());
            ps.setInt(2, l.getLng_id());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                item.put("naim", (rs.getString("ngpr") == null) ? "" : rs.getString("ngpr").trim());
            } else {
                throw new Exception("Не найден перевод для изделия c баркодом " + l.getBarcode());
            }

            ps1.setInt(1, l.getLng_id());
            ResultSet rs1 = ps1.executeQuery();
            while (rs.next()) {
                item.put(rs1.getString("var_name"), rs1.getString("var_lng"));
            }
        } catch (SQLException e) {
            log.severe("Ошибка MarkerovkaDB.getAboutLabelLng(Label l) " + e.getMessage());
        }
        return item;
    }

    /**
     * Возвращает список этикеток
     * @return ArrayList[LabelPath]
     */
    public List<LabelPath> getLabelsPath() {
        List<LabelPath> lps = new ArrayList<>();
        String query = "select id, path, name from _label_path";
        try (PreparedStatement ps = getConnection().prepareStatement(query)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                LabelPath lp = new LabelPath(rs.getInt("id"), rs.getString("path"), rs.getString("name"));
                lps.add(lp);
            }

        } catch (Exception e) {
            log.severe("Ошибка MarkerovkaDB.getLabelsPath()" + e.getMessage());
        }
        return lps;
    }

    public void addLabel(LabelPath lp) {
        String query = "insert into _label_path(path, name) values(?, ?)";
        try (PreparedStatement ps = getConnection().prepareStatement(query)) {
            ps.setString(1, lp.getPath());
            ps.setString(2, lp.getName());
            ps.executeUpdate();
        } catch (Exception e) {
            log.severe("Ошибка MarkerovkaDB.addLabel(LabelPath lp)" + e.getMessage());
        }
    }

    public void addLabel(String path, String name) {
        String query = "insert into _label_path(path, name) values(?, ?)";
        try (PreparedStatement ps = getConnection().prepareStatement(query)) {
            ps.setString(1, path);
            ps.setString(2, name);
            ps.executeUpdate();
        } catch (Exception e) {
            log.severe("Ошибка MarkerovkaDB.addLabel(LabelPath lp)" + e.getMessage());
        }
    }

    /**
     * Возвращает список наименований по заданному артикулу
     * @param nar -- String артикул
     * @return ArrayList[String]
     */
    public List<String> getNames(String nar) {
        List<String> lngs = new ArrayList<>();
        String query = "select ngpr from nsi_kld where nar = ?";
        try (PreparedStatement ps = getConnection().prepareStatement(query)) {
            ps.setString(1, nar);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                lngs.add(rs.getString("ngpr").trim().toUpperCase());
            }

        } catch (Exception e) {
            log.severe("Ошибка MarkerovkaDB.getNames(String nar)" + e.getMessage());
        }
        return lngs;
    }

    /**
     * Возвращает перевод наименования для изделия
     * @param nar -- String артикул
     * @param lngId -- код языка
     * @param ngpr -- наименование на русском языке
     * @return String
     */
    public String getTransName(String nar, int lngId, String ngpr) {
        String tName = "";
        String query = "select ngpr from _trans_item where lang_id = ? and kod_izd in (select kod1 from nsi_sd where kod in (select kod from nsi_kld where nar = ? and ngpr = ?))";
        try (PreparedStatement ps = getConnection().prepareStatement(query)) {
            ps.setInt(1, lngId);
            ps.setString(2, nar);
            ps.setString(3, ngpr);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                tName = rs.getString("ngpr").trim().toUpperCase();
            }

        } catch (Exception e) {
            log.severe("Ошибка MarkerovkaDB.getTransNames(String nar, int lngId, String ngpr)" + e.getMessage());
        }
        return tName.toUpperCase();
    }

    /**
     * Задаёт перевод для изделия
     * @param nar -- String артикул
     * @param lngId -- код языка
     * @param ngpr -- наименование на русском языке
     */
    public void setTransName(String nar, int lngId, String ngpr, String tNgpr) {
        String query = "select kod1 from nsi_sd where kod in (select kod from nsi_kld where nar = ? and ngpr = ?)";
        String query1 = "insert into _trans_item(lang_id, ngpr, kod_izd) values (?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(query);
             PreparedStatement ps1 = conn.prepareStatement(query1);
        ) {
            ps.setString(1, nar);
            ps.setString(2, ngpr);
            ResultSet rs = ps.executeQuery();
            ps1.setInt(1, lngId);
            ps1.setString(2, tNgpr);
            while (rs.next()) {
                ps1.setLong(3, rs.getLong("kod1"));
                ps1.addBatch();
            }
            ps1.executeBatch();

        } catch (Exception e) {
            log.severe("Ошибка MarkerovkaDB.setTransNames(String nar, int lngId, String ngpr, String tNgpr)" + e.getMessage());
        }
    }

    /**
     * Изменяет перевод для изделия
     * @param nar -- String артикул
     * @param lngId -- код языка
     * @param ngpr -- наименование на русском языке
     */
    public void apdTransName(String nar, int lngId, String ngpr, String tNgpr) {

        String query = "update _trans_item set ngpr = ? where lang_id = ? and kod_izd in (select kod1 from nsi_sd where kod in (select kod from nsi_kld where nar = ? and ngpr = ?))";
        try (PreparedStatement ps = getConnection().prepareStatement(query)) {
            ps.setString(1, tNgpr);
            ps.setInt(2, lngId);
            ps.setString(3, nar);
            ps.setString(4, ngpr.toUpperCase());
            ps.executeUpdate();
        } catch (Exception e) {
            log.severe("Ошибка MarkerovkaDB.apdTransName(String nar, int lngId, String ngpr, String tNgpr)" + e.getMessage());
        }
    }

    public Map<String, String> getTransSostav(Language l) {
        Map<String, String> sost = new HashMap();
        String query = "select name_rus, name_lng from _trans_composition where lang_id = ?";
        try (PreparedStatement ps = getConnection().prepareStatement(query)) {
            ps.setInt(1, l.getId());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                sost.put(rs.getString("name_rus"), rs.getString("name_lng"));
            }
        } catch (Exception e) {
            log.severe("Ошибка MarkerovkaDB.getTransSostav(Language l)" + e.getMessage());
        }
        return sost;
    }

    public Map getTransDictionary(Language l) {
        Map words = new HashMap();
        String query = "select name_rus, name_lng from _trans_dictionary where lang_id = ?";
        try (PreparedStatement ps = getConnection().prepareStatement(query)) {
            ps.setInt(1, l.getId());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                words.put(rs.getString("name_rus"), rs.getString("name_lng"));
            }
        } catch (Exception e) {
            log.severe("Ошибка MarkerovkaDB.getTransDictionary(Language l)" + e.getMessage());
        }
        return words;
    }

    @SneakyThrows
    public Object[][] getNakl(String date) {
        List<Object[]> arows = new ArrayList<>();
        long d = convertDateStrToLong(date);
        String query = "select date, ndoc, operac, kpl, NAIM, item_id  "
                + " from (select date, ndoc, operac, kpl, status, item_id from otgruz1 where date >= ? and status = 0 and operac = 'Отгрузка покупателю') as t1 "
                + " join (select KOD, NAIM from s_klient)  as t2 on t1.kpl = t2.KOD order by date";

        try (PreparedStatement ps = getConnection().prepareStatement(query)) {
            ps.setDate(1, new java.sql.Date(d));
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                arows.add(new Object[]{rs.getDate("date"), rs.getInt("ndoc"), rs.getString("operac").trim(), rs.getString("NAIM").trim(), rs.getInt("item_id")});
            }
        } catch (Exception e) {
            log.severe("Ошибка MarkerovkaDB.getTransDictionary(Language l)" + e.getMessage());
        }
        Object[][] r = new Object[arows.size()][5];
        int i = 0;
        for (Object[] o : arows) {
            r[i] = o;
            i++;
        }
        return r;
    }

    public Object[][] getNaklDescr(int doc_id) {
        List<Object[]> arows = new ArrayList<>();

        String query = "select fas, sar, ngpr, rzm_print, label.srt as srt, label.ncw as ncw, kol, barcode from (select sum(kol*kol_in_upack)as kol, eancode, rzm_marh, rst_marh, kod_izd, ncw, srt from otgruz2 where doc_id = ? " +
                " group by eancode, rzm_marh, rst_marh, kod_izd, ncw, srt) as otgr " +
                " join (select kod1, kod, srt, rzm, rst from nsi_sd)as sd on  sd.kod1 = otgr.kod_izd and sd.srt = otgr.srt and sd.rzm = otgr.rzm_marh and sd.rst = otgr.rst_marh " +
                " join (select fas, sar, kod, ngpr from nsi_kld)as kld on kld.kod = sd.kod " +
                " join (select max(barcode)as barcode, srt, rst_marh, rzm_marh, kod_izd, ncw, rzm as rzm_print from label_one group by srt, rst_marh, rzm_marh, kod_izd, ncw, rzm) as label on  label.kod_izd = sd.kod and label.srt = otgr.srt and label.rzm_marh = otgr.rzm_marh and label.rst_marh = otgr.rst_marh and label.ncw = otgr.ncw " +
                " order by  fas, sar, ngpr, rzm_print, label.srt, label.ncw";

        try (PreparedStatement ps = getConnection().prepareStatement(query)) {
            ps.setInt(1, doc_id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                arows.add(new Object[]{rs.getString("fas"), rs.getInt("sar"), rs.getString("ngpr").trim(), rs.getString("rzm_print").trim(), rs.getInt("srt"), rs.getString("ncw").trim(), rs.getInt("kol"), rs.getLong("barcode")});
            }
        } catch (Exception e) {
            log.severe("Ошибка MarkerovkaDBgetNaklDescr(int doc_id)" + e.getMessage());
        }
        Object[][] r = new Object[arows.size()][8];
        int i = 0;
        for (Object[] o : arows) {
            r[i] = o;
            i++;
        }
        return r;
    }

    public boolean chekTransName(String nar, int lngId, String ngpr) {
        boolean flag = false;
        String query = "select ngpr from _trans_item where lang_id = ? and kod_izd in (select kod1 from nsi_sd where kod in (select kod from nsi_kld where nar = ? and ngpr = ?))";

        try (PreparedStatement ps = getConnection().prepareStatement(query);) {
            ps.setInt(1, lngId);
            ps.setString(2, nar);
            ps.setString(3, ngpr);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                flag = true;
            }
        } catch (Exception e) {
            log.severe("Ошибка MarkerovkaDB.getTransNames(String nar, int lngId, String ngpr)" + e.getMessage());
        }
        return flag;
    }
}
