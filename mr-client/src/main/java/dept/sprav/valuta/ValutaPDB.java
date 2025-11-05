package dept.sprav.valuta;

import by.march8.ecs.framework.common.LogCrutch;
import workDB.PDB_new;

import java.util.HashMap;
import java.util.Vector;

//import org.apache.log4j.Logger;

/**
 *
 * @author vova
 */
public class ValutaPDB extends PDB_new {
    // private static final Logger log = new Log().getLoger(ValutaPDB.class);
    private static final LogCrutch log = new LogCrutch();

    public Vector getValutaList() {
        Vector items = new Vector();
        int i = 0;

        String query = "select id, name, full_name, symbol, about from valuta";
        try {
            ps = conn.prepareStatement(query);

            rs = ps.executeQuery();
            while (rs.next()) {
                Vector user = new Vector();
                user.add(++i);
                user.add(rs.getString(2).trim());
                user.add(rs.getString(3).trim());
                user.add(rs.getString(4) == null ? "" : rs.getString(4).trim());
                user.add(rs.getString(5) == null ? "" : rs.getString(5).trim());
                user.add(rs.getLong(1));
                items.add(user);
            }
        } catch (Exception e) {
            System.err.println("Ошибка getValutaList() " + e);
        }
        return items;
    }

    public HashMap getValuta(int id) {
        HashMap valuta = new HashMap();
        valuta.put("id", 0);
        valuta.put("name", "");
        valuta.put("full_name", "");
        valuta.put("symbol", "");
        valuta.put("about", "");
        int i = 0;

        String query = "select id, name, full_name, symbol, about from valuta where id = ?";
        try {
            ps = conn.prepareStatement(query);
            ps.setInt(1, id);
            rs = ps.executeQuery();
            while (rs.next()) {
                Vector user = new Vector();
                valuta.put("id", rs.getInt(1));
                valuta.put("name", rs.getString(2).trim());
                valuta.put("full_name", rs.getString(3).trim());
                valuta.put("symbol", rs.getString(4) == null ? "" : rs.getString(4).trim());
                valuta.put("about", rs.getString(5) == null ? "" : rs.getString(5).trim());
            }
        } catch (Exception e) {
            System.err.println("Ошибка getValutaList() " + e);
        }
        return valuta;
    }

    public HashMap getValuta(String name) {
        HashMap valuta = new HashMap();
        valuta.put("id", 0);
        valuta.put("name", "");
        valuta.put("full_name", "");
        valuta.put("symbol", "");
        valuta.put("about", "");
        int i = 0;

        String query = "select id, name, full_name, symbol, about from valuta where name = ?";
        try {
            ps = conn.prepareStatement(query);
            ps.setString(1, name);
            rs = ps.executeQuery();
            while (rs.next()) {
                Vector user = new Vector();
                valuta.put("id", rs.getInt(1));
                valuta.put("name", rs.getString(2).trim());
                valuta.put("full_name", rs.getString(3).trim());
                valuta.put("symbol", rs.getString(4) == null ? "" : rs.getString(4).trim());
                valuta.put("about", rs.getString(5) == null ? "" : rs.getString(5).trim());
            }
        } catch (Exception e) {
            System.err.println("Ошибка getValuta(String name) " + e);
        }
        return valuta;
    }

    public void setValuta(HashMap v) {
        String query;
        int id = Integer.parseInt(v.get("id").toString());
        if (id == 0) query = "insert into valuta(name, full_name, symbol, about) values(?, ? , ?, ?)";
        else query = "update valuta set name = ?, full_name = ?, symbol = ?, about = ? where id = ?";
        try {
            ps = conn.prepareStatement(query);
            ps.setString(1, v.get("name").toString());
            ps.setString(2, v.get("full_name").toString());
            ps.setString(3, v.get("symbol").toString());
            ps.setString(4, v.get("about").toString());
            if (id > 0) ps.setInt(5, id);
            ps.executeUpdate();
        } catch (Exception e) {
            System.err.println("Ошибка setValuta(HashMap v)" + e);
        }

    }

    public void delValuta(int id) {
        String query = "delete from valuta where id = ?";
        try {
            ps = conn.prepareStatement(query);
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (Exception e) {
            System.err.println("Ошибка delValuta(HashMap v)" + e);
        }

    }

    public void delKursValuta(Long id) {
        String query = "delete from valuta_kurs where id = ?";
        try {
            ps = conn.prepareStatement(query);
            ps.setLong(1, id);
            ps.executeUpdate();
        } catch (Exception e) {
            System.err.println("Ошибка delKursValuta(Long id)" + e);
        }

    }

    /**
     * Возвращает список курсов 
     * @param type -- тип курса<br>
     * 0 -- текущий<br>
     * 1 -- прошлый<br>
     * 2 -- будущий<br>
     * @return Vector(Vectors) Вектор векторов<br> (Валюта основная -- Валюта дополнительная -- Курс -- Дата начала действия -- Код курса)
     */

    public Vector getKursList(int type) {
        Vector items = new Vector();
        StringBuilder query = new StringBuilder();

        if (type == 0)
            query.append("select v.name, v2.name, t3.kurs, t2.date_start, t2.id from (select id_valuta1, id_valuta2, max(date_start) as ds from valuta_kurs ")
                    .append(" group by id_valuta1, id_valuta2 having max(date_start) <= now()) as t1 ")
                    .append(" left join (select max(id)as id, date_start,id_valuta1, id_valuta2  from valuta_kurs group by id_valuta1, id_valuta2 ,date_start) as t2  ")
                    .append("     on t2.date_start = t1.ds and t2.id_valuta1 = t1.id_valuta1 and t2.id_valuta2 = t1.id_valuta2  ")
                    .append(" left join (select id, id_valuta1, id_valuta2, kurs, date_start from valuta_kurs) as t3 on t2.id = t3.id ")
                    .append(" left join (select id, name from valuta) as v on v.id = t2.id_valuta1 ")
                    .append(" left join (select id, name from valuta) as v2 on v2.id = t2.id_valuta2 ")
                    .append(" order by v.name, v2.name, t2.date_start,t2.id ");

        else if (type == 1)
            query.append("select v.name, v2.name, t2.kurs, t2.date_start, t2.id from (select id, id_valuta1, id_valuta2, kurs, date_start from valuta_kurs where date_start <= now()) as t2 ")
                    .append(" left join (select id, name from valuta) as v on v.id = t2.id_valuta1 ")
                    .append(" left join (select id, name from valuta) as v2 on v2.id = t2.id_valuta2 ")
                    .append(" order by v.name, v2.name, t2.date_start,t2.id ");

        else if (type == 2)
            query.append("select v.name, v2.name, t2.kurs, t2.date_start, t2.id from (select id, id_valuta1, id_valuta2, kurs, date_start from valuta_kurs where date_start > now()) as t2 ")
                    .append(" left join (select id, name from valuta) as v on v.id = t2.id_valuta1 ")
                    .append(" left join (select id, name from valuta) as v2 on v2.id = t2.id_valuta2 ")
                    .append(" order by v.name, v2.name, t2.date_start,t2.id ");

        try {
            ps = conn.prepareStatement(query.toString());
            rs = ps.executeQuery();
            while (rs.next()) {
                Vector user = new Vector();
                user.add(rs.getString(1).trim());
                user.add(rs.getString(2).trim());
                user.add(rs.getFloat(3));
                user.add(rs.getString(4));
                user.add(rs.getLong(5));
                items.add(user);
            }
        } catch (Exception e) {
            System.err.println("Ошибка getValutaList() " + e);
        }
        return items;
    }

    /**
     * Возвращает список курсов
     * @return Vector(Vectors) Вектор векторов<br> (Дата начала действия -- Валюта основная -- Валюта дополнительная -- Курс -- Дата корректировки)
     */

    public Vector getKurs(String val1, String val2) {
        Vector items = new Vector();
        StringBuilder query = new StringBuilder();

        query.append("select distinct v1.name as name1, v2.name as name2, kurs, date_start, date_ins ")
                .append("from valuta as v1, valuta as v2, valuta_kurs ")
                .append("where v1.id <> v2.id and id_valuta1 = v1.id and id_valuta2 = v2.id and v1.name = ? and v2.name = ? ")
                .append("Order by name1, name2, date_start, date_ins ");

        try {
            ps = conn.prepareStatement(query.toString());
            ps.setString(1, val1);
            ps.setString(2, val2);
            rs = ps.executeQuery();
            while (rs.next()) {
                Vector tmp = new Vector();
                tmp.add(rs.getString("date_start"));
                tmp.add(rs.getString("name1").trim());
                tmp.add(rs.getString("name2").trim());
                tmp.add(rs.getFloat("kurs"));
                tmp.add(rs.getString("date_ins"));
                items.add(tmp);
            }
        } catch (Exception e) {
            log.error("Ошибка getKurs(): ", e);
            System.err.println("Ошибка getKurs() " + e);
        }
        return items;
    }

    public void addKurs(Integer idval1, Integer idval2, float kurs, String name, String date) {

        long d = convertDateStrToLong(date);

        String query = "insert into valuta_kurs(id_valuta1, id_valuta2, kurs, \"user\", date_start) values(?, ? , ?, ?, ?)";
        try {
            ps = conn.prepareStatement(query);
            ps.setInt(1, idval1);
            ps.setInt(2, idval2);
            ps.setFloat(3, kurs);
            ps.setString(4, name);
            ps.setDate(5, new java.sql.Date(d));
            ps.executeUpdate();
        } catch (Exception e) {
            System.err.println("Ошибка addKurs()" + e);
        }
    }
}
