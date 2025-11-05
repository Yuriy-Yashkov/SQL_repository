package dept.sklad.specCena;

import by.march8.ecs.framework.common.LogCrutch;
import workDB.DB_new;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author DzmitryB
 * Class для работы с таблицей _speccena db.Gomel
 */
public class SpecCenaDB extends DB_new {
    //private static final Logger log = new Log().getLoger(SkladDB.class);
    private static final LogCrutch log = new LogCrutch();

    public void specCenaAdd(String[][] data, boolean selectedGeneralPrice) throws Exception {
        String query;

        try {
            setAutoCommit(false);

            if (selectedGeneralPrice) {

                query = "insert into _speccena values (?,?,?,?,?,?,?,?,round(?,2),getdate(),?)";

                for (int i = 0; i < data.length; i++) {
                    ps = conn.prepareStatement(query);
                    ps.setString(1, "0");
                    ps.setString(2, data[i][1].trim());
                    ps.setString(3, data[i][2].trim());
                    ps.setString(4, data[i][3].trim());
                    ps.setString(5, data[i][4].trim());
                    ps.setString(6, data[i][5].trim());
                    ps.setString(7, data[i][6].trim());
                    ps.setString(8, data[i][7].trim());
                    ps.setFloat(9, Float.valueOf(data[i][8].trim().replace(",", ".")));
                    ps.setInt(10, 1);
                    ps.execute();
                }

            } else {
                query = "insert into _speccena values (?,?,?,?,?,?,?,?,round(?,2),getdate(),?)";

                for (int i = 0; i < data.length; i++) {
                    ps = conn.prepareStatement(query);
                    ps.setString(1, data[i][0].trim());
                    ps.setString(2, data[i][1].trim());
                    ps.setString(3, data[i][2].trim());
                    ps.setString(4, data[i][3].trim());
                    ps.setString(5, data[i][4].trim());
                    ps.setString(6, data[i][5].trim());
                    ps.setString(7, data[i][6].trim());
                    ps.setString(8, data[i][7].trim());
                    ps.setFloat(9, Float.valueOf(data[i][8].trim().replace(",", ".")));
                    ps.setInt(10, 0);
                    ps.execute();
                }
            }

            commit();
        } catch (Exception e) {
            rollback();
            log.error("Ошибка в specCenaAdd() ", e);
            System.err.println("Ошибка в specCenaAdd() " + e.getMessage());
            throw new Exception("Ошибка specCenaAdd(): " + e.getMessage(), e);
        } finally {
            setAutoCommit(true);
        }
    }

    public void specCenaEdit(HashMap hm) {
        String query = "update _speccena set scena=? where id=?";
        try {
            ps = conn.prepareStatement(query);
            ps.setString(1, hm.get("nscena").toString());
            ps.setString(2, hm.get("id").toString());
            ps.executeUpdate();
        } catch (Exception e) {
            log.error("Ошибка в specCenaEdit(String[][] data) ", e);
            System.err.println("Ошибка в specCenaEdit(String[][] data) " + e.getMessage());
        }
    }

    public void specCenaRemove(Map hm) {
        String query = "delete _speccena where id=?";
        try {
            ps = conn.prepareStatement(query);
            ps.setString(1, hm.get("id").toString());
            ps.executeUpdate();
        } catch (Exception e) {
            log.error("Ошибка в specCenaEdit(String[][] data) ", e);
            System.err.println("Ошибка в specCenaEdit(String[][] data) " + e.getMessage());
        }
    }

    public String[] getValuta() {
        String[] val = new String[15];
        String[] mas = null;
        int i = 0;
        String query = "select naim from valuta";
        try {
            ps = conn.prepareStatement(query);
            rs = ps.executeQuery();
            while (rs.next()) {
                val[i] = rs.getString(1);
                i++;
            }
            mas = new String[i];
            System.arraycopy(val, 0, mas, 0, i);
        } catch (Exception e) {
            log.error("Ошибка в getValuta() ", e);
            System.err.println("Ошибка в getValuta() " + e.getMessage());
        }
        return mas;
    }

    public String getValutaName(int cifra) {
        String stroka = "";
        if (cifra == 0)
            stroka = "Бел.руб.";
        if (cifra == 1)
            stroka = "Рос.руб.";
        if (cifra == 2)
            stroka = "Доллар США";
        if (cifra == 3)
            stroka = "Евро";
        if (cifra == 4)
            stroka = "Гривна";
        return stroka;
    }

    public HashMap getAllDataSpecCena() {
        HashMap data = new HashMap();
        int i = 0;
        String query = " SELECT id,skod_kontragenta,smodel,sart,ssort,srazmer,srazmer_end,srost,srost_end,scena,date_insert_record FROM _speccena " +
                " group by id,skod_kontragenta,smodel,sart,ssort,srazmer,srazmer_end,srost,srost_end,scena,date_insert_record " +
                " order by date_insert_record desc ";
        try {
            ps = conn.prepareStatement(query);
            rs = ps.executeQuery();
            while (rs.next()) {
                HashMap buffer = new HashMap();
                buffer.put("id", rs.getLong("id"));
                buffer.put("kod_k", rs.getString("skod_kontragenta"));
                buffer.put("mod", rs.getString("smodel"));
                buffer.put("art", rs.getString("sart"));
                buffer.put("sort", rs.getString("ssort"));
                buffer.put("rzmmin", rs.getString("srazmer"));
                buffer.put("rzmmax", rs.getString("srazmer_end"));
                buffer.put("rstmin", rs.getString("srost"));
                buffer.put("rstmax", rs.getString("srost_end"));
                buffer.put("cena", rs.getString("scena"));
                buffer.put("date", rs.getString("date_insert_record"));
                data.put(i, buffer);
                i++;
            }
        } catch (Exception ex) {
            System.err.println("Error in class SpecCenaDB in method getAllDataSpecCena(): " + ex);
            HashMap buffer = new HashMap();
            buffer.put("id", " ");
            buffer.put("kod_k", " ");
            buffer.put("mod", " ");
            buffer.put("art", " ");
            buffer.put("sort", " ");
            buffer.put("rzmmin", " ");
            buffer.put("rzmmax", " ");
            buffer.put("rstmin", " ");
            buffer.put("rstmax", " ");
            buffer.put("cena", " ");
            buffer.put("date", " ");
            data.put(i, buffer);
            i++;
        }
        return data;
    }

    public HashMap getActualSpecCena() {
        HashMap data = new HashMap();
        HashMap data1 = new HashMap();
        int i = 0;
        int j = 0;
        String query = " SELECT skod_kontragenta,smodel,sart,ssort,srazmer,srazmer_end,srost,srost_end,max(date_insert_record) as date_insert_record " +
                " FROM _speccena group by skod_kontragenta,smodel,sart,ssort,srazmer,srazmer_end,srost,srost_end";
        try {
            ps = conn.prepareStatement(query);
            rs = ps.executeQuery();
            while (rs.next()) {
                HashMap buffer = new HashMap();
                //buffer.put("id", rs.getLong("id"));
                buffer.put("kod_k", rs.getString("skod_kontragenta"));
                buffer.put("mod", rs.getString("smodel"));
                buffer.put("art", rs.getString("sart"));
                buffer.put("sort", rs.getString("ssort"));
                buffer.put("rzmmin", rs.getString("srazmer"));
                buffer.put("rzmmax", rs.getString("srazmer_end"));
                buffer.put("rstmin", rs.getString("srost"));
                buffer.put("rstmax", rs.getString("srost_end"));
                //buffer.put("cena", rs.getString("scena"));
                //buffer.put("date", rs.getString("date_insert_record"));
                data1.put(i, buffer);
                i++;
            }
            i = 0;
            while (i < data1.size()) {
                query = " select top 1 id,skod_kontragenta,smodel,sart,ssort,srazmer,srazmer_end,srost,srost_end,scena,date_insert_record from _speccena " +
                        " where skod_kontragenta=? and smodel=? and sart=? and ssort=? and srazmer=? and srazmer_end=? " +
                        " and srost=? and srost_end=? " +
                        "order by date_insert_record desc";
                HashMap buffer1 = (HashMap) data1.get(i);
                ps = conn.prepareStatement(query);
                ps.setString(1, buffer1.get("kod_k").toString());
                ps.setString(2, buffer1.get("mod").toString());
                ps.setString(3, buffer1.get("art").toString());
                ps.setString(4, buffer1.get("sort").toString());
                ps.setString(5, buffer1.get("rzmmin").toString());
                ps.setString(6, buffer1.get("rzmmax").toString());
                ps.setString(7, buffer1.get("rstmin").toString());
                ps.setString(8, buffer1.get("rstmax").toString());
                rs = ps.executeQuery();
                if (rs.next()) {
                    HashMap buffer = new HashMap();
                    buffer.put("id", rs.getLong("id"));
                    buffer.put("kod_k", rs.getString("skod_kontragenta"));
                    buffer.put("mod", rs.getString("smodel"));
                    buffer.put("art", rs.getString("sart"));
                    buffer.put("sort", rs.getString("ssort"));
                    buffer.put("rzmmin", rs.getString("srazmer"));
                    buffer.put("rzmmax", rs.getString("srazmer_end"));
                    buffer.put("rstmin", rs.getString("srost"));
                    buffer.put("rstmax", rs.getString("srost_end"));
                    buffer.put("cena", rs.getString("scena"));
                    buffer.put("date", rs.getString("date_insert_record"));
                    data.put(j, buffer);
                    j++;
                }
                i++;
            }
        } catch (Exception ex) {
            System.err.println("Error in class SpecCenaDB in method getActualSpecCena(): " + query + ex);
        }
        return data;
    }

    public HashMap getAllKontrSpecCena(HashMap hm) {
        HashMap data = new HashMap();
        int i = 0;
        String query = " select id,skod_kontragenta,smodel,sart,ssort,srazmer,srazmer_end,srost,srost_end,scena,date_insert_record from _speccena " +
                " where skod_kontragenta=? " +
                " order by date_insert_record desc";
        try {
            ps = conn.prepareStatement(query);
            if (hm.get("kod_k").toString().trim().equals(""))
                ps.setString(1, "0");
            else
                ps.setString(1, hm.get("kod_k").toString());
            rs = ps.executeQuery();
            while (rs.next()) {
                HashMap buffer = new HashMap();
                buffer.put("id", rs.getLong("id"));
                buffer.put("kod_k", rs.getString("skod_kontragenta"));
                buffer.put("mod", rs.getString("smodel"));
                buffer.put("art", rs.getString("sart"));
                buffer.put("sort", rs.getString("ssort"));
                buffer.put("rzmmin", rs.getString("srazmer"));
                buffer.put("rzmmax", rs.getString("srazmer_end"));
                buffer.put("rstmin", rs.getString("srost"));
                buffer.put("rstmax", rs.getString("srost_end"));
                buffer.put("cena", rs.getString("scena"));
                buffer.put("date", rs.getString("date_insert_record"));
                data.put(i, buffer);
                i++;
            }
        } catch (Exception ex) {
            System.err.println("Error in class SpecCenaDB in method getAllDataSpecCena(): " + ex);
        }
        return data;
    }

    public HashMap getFind(String agent, String mod) {
        HashMap data = new HashMap();
        int i = 0;
        String query = "";
        try {
            if (!agent.trim().equals("") && !mod.trim().equals("")) {
                query = " select id,skod_kontragenta,smodel,sart,ssort,srazmer,srazmer_end,srost,srost_end,scena,date_insert_record from _speccena " +
                        " where skod_kontragenta=? and smodel=? " +
                        " order by date_insert_record desc";
                ps = conn.prepareStatement(query);
                ps.setInt(1, Integer.valueOf(agent));
                ps.setInt(2, Integer.valueOf(mod));
            }
            if (!agent.trim().equals("") && mod.trim().equals("")) {
                query = " select id,skod_kontragenta,smodel,sart,ssort,srazmer,srazmer_end,srost,srost_end,scena,date_insert_record from _speccena " +
                        " where skod_kontragenta=?" +
                        " order by date_insert_record desc";
                ps = conn.prepareStatement(query);
                ps.setInt(1, Integer.valueOf(agent));
            }
            if (agent.trim().equals("") && !mod.trim().equals("")) {
                query = " select id,skod_kontragenta,smodel,sart,ssort,srazmer,srazmer_end,srost,srost_end,scena,date_insert_record from _speccena " +
                        " where smodel=?" +
                        " order by date_insert_record desc";
                ps = conn.prepareStatement(query);
                ps.setInt(1, Integer.valueOf(mod));
            }
            if (agent.trim().equals("") && mod.trim().equals("")) {
                query = " select id,skod_kontragenta,smodel,sart,ssort,srazmer,srazmer_end,srost,srost_end,scena,date_insert_record from _speccena " +
                        " order by date_insert_record desc";
                ps = conn.prepareStatement(query);
            }
            rs = ps.executeQuery();
            while (rs.next()) {
                HashMap buffer = new HashMap();
                buffer.put("id", rs.getLong("id"));
                buffer.put("kod_k", rs.getString("skod_kontragenta"));
                buffer.put("mod", rs.getString("smodel"));
                buffer.put("art", rs.getString("sart"));
                buffer.put("sort", rs.getString("ssort"));
                buffer.put("rzmmin", rs.getString("srazmer"));
                buffer.put("rzmmax", rs.getString("srazmer_end"));
                buffer.put("rstmin", rs.getString("srost"));
                buffer.put("rstmax", rs.getString("srost_end"));
                buffer.put("cena", rs.getString("scena"));
                buffer.put("date", rs.getString("date_insert_record"));
                data.put(i, buffer);
                i++;
            }
        } catch (Exception ex) {
            System.err.println("Error in class SpecCenaDB in method getAllDataSpecCena(): " + ex);
        }
        return data;
    }
}
