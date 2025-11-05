package dept.marketing.report;

import by.march8.ecs.framework.common.LogCrutch;
import workDB.PDB_new;

import javax.swing.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Vector;

/**
 *
 * @author lidashka
 */
public class ReportPDB extends PDB_new {
    private static final LogCrutch log = new LogCrutch();

    public String[] getPeriodOtgruz() throws Exception {
        String[] period = new String[2];
        String sql;
        try {
            sql = "Select dao from nsi_otgruz Order by dao limit 1";
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            if (rs.next()) period[0] = new SimpleDateFormat("dd.MM.yyyy").format(rs.getDate("dao"));
            else period[0] = "-";

            sql = "Select dao from nsi_otgruz Order by dao desc limit 1";
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            if (rs.next()) period[1] = new SimpleDateFormat("dd.MM.yyyy").format(rs.getDate("dao"));
            else period[1] = "-";

        } catch (Exception e) {
            System.err.println("Ошибка getPeriodOtgruz() " + e);
            log.error("Ошибка getPeriodOtgruz()", e);
            throw new Exception("Ошибка getPeriodOtgruz() " + e.getMessage(), e);
        }
        return period;
    }

    public Vector getOtgruz(ArrayList<Integer> idClients, String sDate, String eDate, String vid) {
        Vector otgruz = new Vector();
        String kpl = "";
        String sql;
        int k = 0;

        try {
            if (idClients.size() > 0) {
                for (Integer id : idClients) {
                    if (k < 1) {
                        k++;
                    } else {
                        kpl += " or ";
                    }

                    kpl += " kpl = " + id + " ";
                }

                if (kpl.trim().length() > 0)
                    kpl = " and (" + kpl + ")";

                sql = " Select " +
                        "          sar, " +
                        "          fas, " +
                        "          sum(kol) as kolvo, " +
                        "          sum(kol*cno) as summ " +
                        "  From     " +
                        "          nsi_otgruz " +
                        "  Where    " +
                        "          dao between '" + sDate + "' and '" + eDate + "' and " +
                        "          kol " + vid + " 0 " +
                        kpl +
                        " Group by " +
                        "          sar, " +
                        "          fas " +
                        " Order by " +
                        "          fas ";

                ps = conn.prepareStatement(sql);
                rs = ps.executeQuery();

                while (rs.next()) {
                    Vector tmp = new Vector();
                    tmp.add(rs.getLong(1));
                    tmp.add(rs.getInt(2));
                    tmp.add(rs.getDouble(3));
                    tmp.add(rs.getDouble(4));
                    otgruz.add(tmp);
                }
            }
        } catch (Exception e) {
            otgruz = new Vector();
            log.error("Ошибка getOtgruz(): ", e);
            JOptionPane.showMessageDialog(
                    null,
                    "Ошибка getOtgruz(): " + e.getMessage(),
                    "Ошибка",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
        }
        return otgruz;
    }

    public Vector getZakazClient(int idClient, String sdate, String edate, String[][] vidIzd) {
        Vector zakaz = new Vector();
        String sql = "";
        String sqlsar = "";
        try {
            for (int i = 0; i < vidIzd.length; i++) {
                if (i == 0) sqlsar = " and sh_artikul::text like '" + vidIzd[0][0] + "'";
                else
                    sqlsar = " and sh_artikul::text like '" + vidIzd[i][0] + "' and sh_artikul::text not like '" + vidIzd[0][0] + "'";


                sql = "Select upper(name_izdelie), fason, sum(kol_vo) as kol from orders, list_orders" +
                        "	where list_orders.id_order = orders.id_order and kod_klient = " + idClient + " and state > 0 " +
                        "				and dateorder BETWEEN '" + sdate + "' AND '" + edate + "' " + sqlsar +
                        "	Group by upper(name_izdelie), fason" +
                        "	Order by upper(name_izdelie), fason";

                ps = conn.prepareStatement(sql);
                rs = ps.executeQuery();
                while (rs.next()) {
                    Vector tmp = new Vector();
                    tmp.add(vidIzd[i][1].toUpperCase());                        // ассортимент
                    tmp.add(rs.getString(1).trim().toLowerCase().replace("  ", " ").replace("  ", " "));  // наименование изд.
                    tmp.add(rs.getInt(2));                                      // модель
                    tmp.add(rs.getInt(3));                                      // кол-во по заявкам
                    tmp.add(vidIzd[i][0]);                                      // sar
                    zakaz.add(tmp);
                }
            }
        } catch (Exception e) {
            log.error("Ошибка getZakazClient(): ", e);
            JOptionPane.showMessageDialog(null, "Ошибка getZakazClient(): " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
        return zakaz;
    }

    public Vector getOtgruzRR(long sDate, long eDate, Vector kt, boolean flagChul) throws Exception {
        Vector rezalt = new Vector();
        String sql = "";

        try {
            setAutoCommit(false);

            sql = " Drop table if exists tmp_otgruz_kt";
            ps = conn.prepareStatement(sql);
            ps.executeUpdate();

            sql = " Create temp table tmp_otgruz_kt("
                    + "  fas integer NOT NULL,"
                    + "  sar integer NOT NULL,"
                    + "  t_1 double precision NOT NULL DEFAULT 0,"
                    + "  m_1 double precision NOT NULL DEFAULT 0,"
                    + "  t_2 double precision NOT NULL DEFAULT 0,"
                    + "  m_2 double precision NOT NULL DEFAULT 0,"
                    + "  t_3 double precision NOT NULL DEFAULT 0,"
                    + "  m_3 double precision NOT NULL DEFAULT 0,"
                    + "  t_4 double precision NOT NULL DEFAULT 0,"
                    + "  m_4 double precision NOT NULL DEFAULT 0,"
                    + "  t_5 double precision NOT NULL DEFAULT 0,"
                    + "  m_5 double precision NOT NULL DEFAULT 0,"
                    + "  t_6 double precision NOT NULL DEFAULT 0,"
                    + "  m_6 double precision NOT NULL DEFAULT 0,"
                    + "  t_itog double precision NOT NULL DEFAULT 0,"
                    + "  m_itog double precision NOT NULL DEFAULT 0,"
                    + "  CONSTRAINT tmp_otgruz_kt_pkey PRIMARY KEY (fas,sar)) ";
            ps = conn.prepareStatement(sql);
            ps.executeUpdate();

            String sql_kt = "";
            for (int i = 0; i < kt.size(); i++) {
                String element = (String) kt.get(i);
                String[] elements = element.split(",");
                for (int j = 0; j < elements.length; j++) {
                    sql_kt = sql_kt +
                            (sql_kt.equals("") ? " " : " or ") +
                            " nsi_kt.kt = " + Integer.valueOf(elements[j]) + " ";
                }
            }

            sql = "Select distinct fas, sar "
                    + " From nsi_otgruz, nsi_kt "
                    + " Where nsi_otgruz.npt = nsi_kt.kt and "
                    + "       sar::text not like '45______' and "
                    + "       sar::text not like '47______' and "
                    + "       sar::text not like '48______' and "
                    + (flagChul ? " sar::text like '43______' and "
                    : " sar::text not like '43______' and ")
                    + "       dao between ? and ? "
                    + (sql_kt.equals("") ? " " : " and ( " + sql_kt + " ) ")
                    + " Order by fas, sar ";
            ps = conn.prepareStatement(sql);
            ps.setDate(1, new java.sql.Date(sDate));
            ps.setDate(2, new java.sql.Date(eDate));
            rs = ps.executeQuery();

            sql = "INSERT INTO tmp_otgruz_kt(fas, sar) VALUES(?,?);";
            while (rs.next()) {
                ps = conn.prepareStatement(sql);
                ps.setInt(1, rs.getInt("fas"));
                ps.setInt(2, rs.getInt("sar"));
                ps.executeUpdate();
            }

            // Заполняем во временной таблице t_itog, m_itog
            sql = "Select distinct fas, sar, sum(kol) as st, sum(kol*cno) as sm "
                    + " From nsi_otgruz, nsi_kt "
                    + " Where nsi_otgruz.npt = nsi_kt.kt and "
                    + "       dao between ? and ? "
                    + (sql_kt.equals("") ? " " : " and ( " + sql_kt + " ) ")
                    + " Group by fas, sar "
                    + " Order by fas, sar ";
            ps = conn.prepareStatement(sql);
            ps.setDate(1, new java.sql.Date(sDate));
            ps.setDate(2, new java.sql.Date(eDate));
            rs = ps.executeQuery();

            sql = "UPDATE tmp_otgruz_kt "
                    + " SET t_itog = ?, m_itog = ?  "
                    + " WHERE fas = ? and sar = ? ";
            while (rs.next()) {
                ps = conn.prepareStatement(sql);
                ps.setDouble(1, rs.getDouble("st"));
                ps.setDouble(2, rs.getDouble("sm"));
                ps.setDouble(3, rs.getInt("fas"));
                ps.setDouble(4, rs.getInt("sar"));
                ps.executeUpdate();
            }

            // Заполняем по регион. рынкам t_X, m_X
            for (int i = 0; i < kt.size(); i++) {

                String element = (String) kt.get(i);
                String[] elements = element.split(",");
                int num = i + 1;
                sql_kt = "";

                for (int j = 0; j < elements.length; j++) {
                    sql_kt = sql_kt +
                            (sql_kt.equals("") ? " " : " or ") +
                            " nsi_kt.kt = " + Integer.valueOf(elements[j]) + " ";
                }

                sql = "Select distinct fas, sar, sum(kol) as st, sum(kol*cno) as sm "
                        + " From nsi_otgruz, nsi_kt "
                        + " Where nsi_otgruz.npt = nsi_kt.kt and "
                        + "       dao between ? and ? "
                        + (sql_kt.equals("") ? " " : " and ( " + sql_kt + " ) ")
                        + " Group by fas, sar "
                        + " Order by fas, sar ";
                ps = conn.prepareStatement(sql);
                ps.setDate(1, new java.sql.Date(sDate));
                ps.setDate(2, new java.sql.Date(eDate));
                rs = ps.executeQuery();

                sql = "UPDATE tmp_otgruz_kt "
                        + " SET t_" + num + " = ?, m_" + num + " = ? "
                        + " WHERE fas = ? and sar = ? ";
                while (rs.next()) {
                    ps = conn.prepareStatement(sql);
                    ps.setDouble(1, rs.getDouble("st"));
                    ps.setDouble(2, rs.getDouble("sm"));
                    ps.setDouble(3, rs.getInt("fas"));
                    ps.setDouble(4, rs.getInt("sar"));
                    ps.executeUpdate();
                }
            }

            sql = "Select fas, "
                    + "   sum(t_1),sum(m_1), "
                    + "   sum(t_2),sum(m_2), "
                    + "   sum(t_3),sum(m_3), "
                    + "   sum(t_4),sum(m_4), "
                    + "   sum(t_5),sum(m_5), "
                    + "   sum(t_6),sum(m_6), "
                    + "   sum(t_itog),sum(m_itog) "
                    + " From tmp_otgruz_kt "
                    + (flagChul ? " Where sar::text like '43______' "
                    : " Where sar::text not like '43______' ")
                    + " Group by fas"
                    + " Order by fas";
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                Vector tmp = new Vector();
                tmp.add(0);
                tmp.add(rs.getInt(1));
                tmp.add(new BigDecimal(rs.getDouble(2)).setScale(3, RoundingMode.HALF_UP).doubleValue());
                tmp.add(new BigDecimal(rs.getDouble(3)).setScale(3, RoundingMode.HALF_UP).doubleValue());
                tmp.add(new BigDecimal(rs.getDouble(4)).setScale(3, RoundingMode.HALF_UP).doubleValue());
                tmp.add(new BigDecimal(rs.getDouble(5)).setScale(3, RoundingMode.HALF_UP).doubleValue());
                tmp.add(new BigDecimal(rs.getDouble(6)).setScale(3, RoundingMode.HALF_UP).doubleValue());
                tmp.add(new BigDecimal(rs.getDouble(7)).setScale(3, RoundingMode.HALF_UP).doubleValue());
                tmp.add(new BigDecimal(rs.getDouble(8)).setScale(3, RoundingMode.HALF_UP).doubleValue());
                tmp.add(new BigDecimal(rs.getDouble(9)).setScale(3, RoundingMode.HALF_UP).doubleValue());
                tmp.add(new BigDecimal(rs.getDouble(10)).setScale(3, RoundingMode.HALF_UP).doubleValue());
                tmp.add(new BigDecimal(rs.getDouble(11)).setScale(3, RoundingMode.HALF_UP).doubleValue());
                tmp.add(new BigDecimal(rs.getDouble(12)).setScale(3, RoundingMode.HALF_UP).doubleValue());
                tmp.add(new BigDecimal(rs.getDouble(13)).setScale(3, RoundingMode.HALF_UP).doubleValue());
                tmp.add(new BigDecimal(rs.getDouble(14)).setScale(3, RoundingMode.HALF_UP).doubleValue());
                tmp.add(new BigDecimal(rs.getDouble(15)).setScale(3, RoundingMode.HALF_UP).doubleValue());
                rezalt.add(tmp);
            }

            commit();
        } catch (Exception e) {
            rezalt = new Vector();
            rollBack();
            System.err.println("Ошибка getOtgruzRR() " + e);
            log.error("Ошибка getOtgruzRR()", e);
            throw new Exception("Ошибка getOtgruzRR() " + e.getMessage(), e);
        } finally {
            setAutoCommit(true);
        }
        return rezalt;
    }

    public Vector initTableKT() throws Exception {
        Vector all = new Vector();
        String sql = "Select * from nsi_kt Order by kt";

        try {
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                Vector tmp = new Vector();
                tmp.add(rs.getInt(1));
                tmp.add(rs.getString(2).trim());
                all.add(tmp);
            }

        } catch (Exception e) {
            all = new Vector();
            System.err.println("Ошибка initTableKT() " + e);
            log.error("Ошибка initTableKT()", e);
            throw new Exception("Ошибка initTableKT() " + e.getMessage(), e);
        }
        return all;
    }

    public String getNameKT(int idKt) throws Exception {
        String sql = "Select no from nsi_kt where kt = ? ";

        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, idKt);
            rs = ps.executeQuery();
            if (rs.next())
                return rs.getString(1);
            else
                return "";

        } catch (Exception e) {
            System.err.println("Ошибка initTableKT() " + e);
            log.error("Ошибка initTableKT()", e);
            throw new Exception("Ошибка initTableKT() " + e.getMessage(), e);
        }

    }

    public Vector getTempAnalizRRTable() throws Exception {
        Vector all = new Vector();
        String sql = "Select * from tmp_otgruz_kt Order by fas, sar";

        try {
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                Vector tmp = new Vector();
                tmp.add(rs.getInt(1));
                tmp.add(rs.getInt(2));
                tmp.add(rs.getDouble(3));
                tmp.add(rs.getDouble(4));
                tmp.add(rs.getDouble(5));
                tmp.add(rs.getDouble(6));
                tmp.add(rs.getDouble(7));
                tmp.add(rs.getDouble(8));
                tmp.add(rs.getDouble(9));
                tmp.add(rs.getDouble(10));
                tmp.add(rs.getDouble(11));
                tmp.add(rs.getDouble(12));
                tmp.add(rs.getDouble(13));
                tmp.add(rs.getDouble(14));
                tmp.add(rs.getDouble(15));
                tmp.add(rs.getDouble(16));
                all.add(tmp);
            }

        } catch (Exception e) {
            all = new Vector();
            System.err.println("Ошибка getTempAnalizRRTable() " + e);
            log.error("Ошибка getTempAnalizRRTable()", e);
            throw new Exception("Ошибка getTempAnalizRRTable() " + e.getMessage(), e);
        }
        return all;
    }

    public Vector getVolumeOtgruz(long sDate, long eDate, Vector kt, boolean flagChul) throws Exception {
        Vector rezalt = new Vector();
        String sql = "";

        try {
            setAutoCommit(false);

            sql = " Drop table if exists tmp_otgruz_volume";
            ps = conn.prepareStatement(sql);
            ps.executeUpdate();

            sql = " Create temp table tmp_otgruz_volume("
                    + "  fas integer NOT NULL,"
                    + "  sar integer NOT NULL,"
                    + "  volume_rb double precision NOT NULL DEFAULT 0,"
                    + "  volume_ex double precision NOT NULL DEFAULT 0,"
                    + "  volume_itogo double precision NOT NULL DEFAULT 0,"
                    + "  volume_pr double precision NOT NULL DEFAULT 0,"
                    + "  sum_rb double precision NOT NULL DEFAULT 0,"
                    + "  sum_ex double precision NOT NULL DEFAULT 0,"
                    + "  sum_itogo double precision NOT NULL DEFAULT 0,"
                    + "  sum_pr double precision NOT NULL DEFAULT 0,"
                    + "  CONSTRAINT tmp_otgruz_volume_kt_pkey PRIMARY KEY (fas,sar)) ";
            ps = conn.prepareStatement(sql);
            ps.executeUpdate();

            String sql_kt = "";
            for (int i = 0; i < kt.size(); i++) {
                String element = (String) kt.get(i);
                String[] elements = element.split(",");
                for (int j = 0; j < elements.length; j++) {
                    sql_kt = sql_kt +
                            (sql_kt.equals("") ? " " : " or ") +
                            " nsi_kt.kt = " + Integer.valueOf(elements[j]) + " ";
                }
            }

            sql = "Select distinct fas, sar "
                    + " From nsi_otgruz, nsi_kt "
                    + " Where nsi_otgruz.npt = nsi_kt.kt and "
                    + "       sar::text not like '45______' and "
                    + "       sar::text not like '47______' and "
                    + "       sar::text not like '48______' and "
                    + (flagChul ? " sar::text like '43______' and "
                    : " sar::text not like '43______' and ")
                    + "       dao between ? and ? "
                    + " Order by fas, sar ";
            ps = conn.prepareStatement(sql);
            ps.setDate(1, new java.sql.Date(sDate));
            ps.setDate(2, new java.sql.Date(eDate));
            rs = ps.executeQuery();

            sql = "INSERT INTO tmp_otgruz_volume (fas, sar) VALUES(?,?);";
            while (rs.next()) {
                ps = conn.prepareStatement(sql);
                ps.setInt(1, rs.getInt("fas"));
                ps.setInt(2, rs.getInt("sar"));
                ps.executeUpdate();
            }

            // Заполняем во временной таблице volume_rb, sum_rb
            sql = "Select distinct fas, sar, sum(kol) as st, sum(kol*cno) as sm "
                    + " From nsi_otgruz, nsi_kt "
                    + " Where nsi_otgruz.npt = nsi_kt.kt and "
                    + "	dao between ? and ? "
                    + (sql_kt.equals("") ? " " : " and ( " + sql_kt + " ) ")
                    + " Group by fas, sar "
                    + " Order by fas, sar ";
            ps = conn.prepareStatement(sql);
            ps.setDate(1, new java.sql.Date(sDate));
            ps.setDate(2, new java.sql.Date(eDate));
            rs = ps.executeQuery();


            sql = "UPDATE tmp_otgruz_volume "
                    + " SET volume_rb = ?, sum_rb = ?  "
                    + " WHERE fas = ? and sar = ?  ";
            while (rs.next()) {
                ps = conn.prepareStatement(sql);
                ps.setDouble(1, rs.getDouble("st"));
                ps.setDouble(2, rs.getDouble("sm"));
                ps.setDouble(3, rs.getInt("fas"));
                ps.setDouble(4, rs.getInt("sar"));
                ps.executeUpdate();
            }

            // Заполняем во временной таблице volume_itogo, sum_itogo
            sql = "Select distinct fas, sar, sum(kol) as st, sum(kol*cno) as sm "
                    + " From nsi_otgruz "
                    + " Where dao between ? and ? "
                    + " Group by fas, sar "
                    + " Order by fas, sar ";
            ps = conn.prepareStatement(sql);
            ps.setDate(1, new java.sql.Date(sDate));
            ps.setDate(2, new java.sql.Date(eDate));
            rs = ps.executeQuery();


            sql = "UPDATE tmp_otgruz_volume "
                    + " SET volume_itogo = ?, sum_itogo = ?  "
                    + " WHERE fas = ? and sar = ? ";
            while (rs.next()) {
                ps = conn.prepareStatement(sql);
                ps.setDouble(1, rs.getDouble("st"));
                ps.setDouble(2, rs.getDouble("sm"));
                ps.setDouble(3, rs.getInt("fas"));
                ps.setDouble(4, rs.getInt("sar"));
                ps.executeUpdate();
            }

            sql_kt = "";
            for (int i = 0; i < kt.size(); i++) {
                String element = (String) kt.get(i);
                String[] elements = element.split(",");
                for (int j = 0; j < elements.length; j++) {
                    sql_kt = sql_kt +
                            (sql_kt.equals("") ? " " : " and ") +
                            " nsi_kt.kt <> " + Integer.valueOf(elements[j]) + " ";
                }
            }

            // Заполняем во временной таблице volume_ex, sum_ex
            sql = "Select distinct fas, sar, sum(kol) as st, sum(kol*cno) as sm "
                    + " From nsi_otgruz, nsi_kt "
                    + " Where nsi_otgruz.npt = nsi_kt.kt and "
                    + "	dao between ? and ? "
                    + (sql_kt.equals("") ? " " : " and ( " + sql_kt + " ) ")
                    + " Group by fas, sar "
                    + " Order by fas, sar ";
            ps = conn.prepareStatement(sql);
            ps.setDate(1, new java.sql.Date(sDate));
            ps.setDate(2, new java.sql.Date(eDate));
            rs = ps.executeQuery();

            sql = "UPDATE tmp_otgruz_volume "
                    + " SET volume_ex = ?, sum_ex = ?  "
                    + " WHERE fas = ? and sar = ? ";
            while (rs.next()) {
                ps = conn.prepareStatement(sql);
                ps.setDouble(1, rs.getDouble("st"));
                ps.setDouble(2, rs.getDouble("sm"));
                ps.setDouble(3, rs.getInt("fas"));
                ps.setDouble(4, rs.getInt("sar"));
                ps.executeUpdate();
            }

            sql = "Select fas, "
                    + "     sum(volume_rb),"
                    + "     sum(volume_ex), "
                    + "     sum(volume_itogo), "
                    + "     sum(volume_pr), "
                    + "     sum(sum_rb), "
                    + "     sum(sum_ex), "
                    + "     sum(sum_itogo), "
                    + "     sum(sum_pr) "
                    + " From tmp_otgruz_volume "
                    + (flagChul ? " Where sar::text like '43______' " : " Where sar::text not like '43______' ")
                    + " Group by fas"
                    + " Order by fas";
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                Vector tmp = new Vector();
                tmp.add(0);
                tmp.add(rs.getInt(1));
                tmp.add(new BigDecimal(rs.getDouble(2)).setScale(3, RoundingMode.HALF_UP).doubleValue());
                tmp.add(new BigDecimal(rs.getDouble(3)).setScale(3, RoundingMode.HALF_UP).doubleValue());
                tmp.add(new BigDecimal(rs.getDouble(4)).setScale(3, RoundingMode.HALF_UP).doubleValue());
                tmp.add(rs.getDouble(4) > 0 ? new BigDecimal(((rs.getDouble(3)) * 100) / (rs.getDouble(4))).setScale(3, RoundingMode.HALF_UP).doubleValue() : 0);
                tmp.add(new BigDecimal(rs.getDouble(6)).setScale(3, RoundingMode.HALF_UP).doubleValue());
                tmp.add(new BigDecimal(rs.getDouble(7)).setScale(3, RoundingMode.HALF_UP).doubleValue());
                tmp.add(new BigDecimal(rs.getDouble(8)).setScale(3, RoundingMode.HALF_UP).doubleValue());
                tmp.add(rs.getDouble(8) > 0 ? new BigDecimal(((rs.getDouble(7)) * 100) / (rs.getDouble(8))).setScale(3, RoundingMode.HALF_UP).doubleValue() : 0);
                rezalt.add(tmp);
            }

            commit();
        } catch (Exception e) {
            rezalt = new Vector();
            rollBack();
            System.err.println("Ошибка getVolumeOtgruz() " + e);
            log.error("Ошибка getVolumeOtgruz()", e);
            throw new Exception("Ошибка getVolumeOtgruz() " + e.getMessage(), e);
        } finally {
            setAutoCommit(true);
        }
        return rezalt;
    }

    public Vector getTempAnalizVolumeOtgruzTable() throws Exception {
        Vector all = new Vector();
        String sql = "Select * from tmp_otgruz_volume Order by fas, sar";

        try {
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                Vector tmp = new Vector();
                tmp.add(rs.getInt(1));
                tmp.add(rs.getInt(2));
                tmp.add(rs.getDouble(3));
                tmp.add(rs.getDouble(4));
                tmp.add(rs.getDouble(5));
                tmp.add(rs.getDouble(6));
                tmp.add(rs.getDouble(7));
                tmp.add(rs.getDouble(8));
                tmp.add(rs.getDouble(9));
                tmp.add(rs.getDouble(10));
                all.add(tmp);
            }

        } catch (Exception e) {
            all = new Vector();
            System.err.println("Ошибка getTempAnalizVolumeOtgruzTable() " + e);
            log.error("Ошибка getTempAnalizVolumeOtgruzTable()", e);
            throw new Exception("Ошибка getTempAnalizVolumeOtgruzTable() " + e.getMessage(), e);
        }
        return all;
    }

    Vector getSumItogoVolumeOtgruz(boolean flagChul) throws Exception {
        Vector all = new Vector();
        String sql = "Select sum(volume_rb),"
                + "     sum(volume_ex), "
                + "     sum(volume_itogo), "
                + "     sum(volume_pr), "
                + "     sum(sum_rb), "
                + "     sum(sum_ex), "
                + "     sum(sum_itogo), "
                + "     sum(sum_pr) "
                + " From tmp_otgruz_volume "
                + (flagChul ? "Where sar::text like '43______' "
                : "Where sar::text not like '43______' ");

        try {
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                Vector tmp = new Vector();
                tmp.add(0);
                tmp.add("ИТОГО");
                tmp.add(new BigDecimal(rs.getDouble(1)).setScale(3, RoundingMode.HALF_UP).doubleValue());
                tmp.add(new BigDecimal(rs.getDouble(2)).setScale(3, RoundingMode.HALF_UP).doubleValue());
                tmp.add(new BigDecimal(rs.getDouble(3)).setScale(3, RoundingMode.HALF_UP).doubleValue());
                tmp.add(rs.getDouble(3) > 0 ? new BigDecimal(((rs.getDouble(2)) * 100) / (rs.getDouble(3))).setScale(3, RoundingMode.HALF_UP).doubleValue() : 0);
                tmp.add(new BigDecimal(rs.getDouble(5)).setScale(3, RoundingMode.HALF_UP).doubleValue());
                tmp.add(new BigDecimal(rs.getDouble(6)).setScale(3, RoundingMode.HALF_UP).doubleValue());
                tmp.add(new BigDecimal(rs.getDouble(7)).setScale(3, RoundingMode.HALF_UP).doubleValue());
                tmp.add(rs.getDouble(7) > 0 ? new BigDecimal(((rs.getDouble(6)) * 100) / (rs.getDouble(7))).setScale(3, RoundingMode.HALF_UP).doubleValue() : 0);
                all.add(tmp);
            }

        } catch (Exception e) {
            all = new Vector();
            System.err.println("Ошибка getSumItogoVolumeOtgruz() " + e);
            log.error("Ошибка getSumItogoVolumeOtgruz()", e);
            throw new Exception("Ошибка getSumItogoVolumeOtgruz() " + e.getMessage(), e);
        }
        return all;
    }

    Vector getSumItogoAnalizRR(boolean flagChul) throws Exception {
        Vector all = new Vector();
        String sql = "Select  sum(t_1),sum(m_1), "
                + "   sum(t_2),sum(m_2), "
                + "   sum(t_3),sum(m_3), "
                + "   sum(t_4),sum(m_4), "
                + "   sum(t_5),sum(m_5), "
                + "   sum(t_6),sum(m_6), "
                + "   sum(t_itog),sum(m_itog) "
                + " From tmp_otgruz_kt "
                + (flagChul ? "Where sar::text like '43______' "
                : "Where sar::text not like '43______' ");

        try {
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                Vector tmp = new Vector();
                tmp.add(0);
                tmp.add("ИТОГО");
                tmp.add(new BigDecimal(rs.getDouble(1)).setScale(3, RoundingMode.HALF_UP).doubleValue());
                tmp.add(new BigDecimal(rs.getDouble(2)).setScale(3, RoundingMode.HALF_UP).doubleValue());
                tmp.add(new BigDecimal(rs.getDouble(3)).setScale(3, RoundingMode.HALF_UP).doubleValue());
                tmp.add(new BigDecimal(rs.getDouble(4)).setScale(3, RoundingMode.HALF_UP).doubleValue());
                tmp.add(new BigDecimal(rs.getDouble(5)).setScale(3, RoundingMode.HALF_UP).doubleValue());
                tmp.add(new BigDecimal(rs.getDouble(6)).setScale(3, RoundingMode.HALF_UP).doubleValue());
                tmp.add(new BigDecimal(rs.getDouble(7)).setScale(3, RoundingMode.HALF_UP).doubleValue());
                tmp.add(new BigDecimal(rs.getDouble(8)).setScale(3, RoundingMode.HALF_UP).doubleValue());
                tmp.add(new BigDecimal(rs.getDouble(9)).setScale(3, RoundingMode.HALF_UP).doubleValue());
                tmp.add(new BigDecimal(rs.getDouble(10)).setScale(3, RoundingMode.HALF_UP).doubleValue());
                tmp.add(new BigDecimal(rs.getDouble(11)).setScale(3, RoundingMode.HALF_UP).doubleValue());
                tmp.add(new BigDecimal(rs.getDouble(12)).setScale(3, RoundingMode.HALF_UP).doubleValue());
                tmp.add(new BigDecimal(rs.getDouble(13)).setScale(3, RoundingMode.HALF_UP).doubleValue());
                tmp.add(new BigDecimal(rs.getDouble(14)).setScale(3, RoundingMode.HALF_UP).doubleValue());
                all.add(tmp);
            }

        } catch (Exception e) {
            all = new Vector();
            System.err.println("Ошибка getSumItogoAnalizRR() " + e);
            log.error("Ошибка getSumItogoAnalizRR()", e);
            throw new Exception("Ошибка getSumItogoAnalizRR() " + e.getMessage(), e);
        }
        return all;
    }
}
