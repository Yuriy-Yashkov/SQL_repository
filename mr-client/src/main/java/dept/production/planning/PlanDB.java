package dept.production.planning;

import workDB.DB_new;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Vector;

/**
 *
 * @author lidashka
 */
@SuppressWarnings("all")
public class PlanDB extends DB_new {
    //private static final Logger log = new Log().getLoger(PlanDB.class);

    public Vector getAllModels(String str) throws Exception {
        Vector elements = new Vector();
        String sql = "Select distinct fas, ngpr "
                + "     From nsi_kld "
                + "     Where fas like '" + str + "%' and "
                + "             fas > 0 "
                + "     Order by fas, ngpr";

        try {
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                Vector tmp = new Vector();
                tmp.add(rs.getInt("fas"));
                tmp.add(rs.getString("ngpr").trim());
                elements.add(tmp);
            }

        } catch (Exception e) {
            System.err.println("Ошибка getAllModels() " + e);
            // log.error("Ошибка getAllModels()", e);
            throw new Exception("Ошибка getAllModels() " + e.getMessage(), e);
        }
        return elements;
    }

    public String getCompositionsByParameters(final Integer fas, final Integer sar, final String nar) throws Exception {
        try {
            String sql = "Select sostav1, sostav2, sostav3 from nsi_kld kld " +
                    "inner join kroy1 kroy1 on kroy1.kod_izd = kld.kod " +
                    "inner join kroy kroy on kroy.kod = kroy1.kod " +
                    "inner join marh_list1 list1 on list1.kod_kroy = kroy1.kod_str " +
                    "inner join marh_list list on list.kod = list1.kod_marh " +
                    "inner join nsi_polotno polotno on polotno.narpol = kroy.shifrp " +
                    "WHERE " +
                    "        kld.fas = " + fas +
                    "        and kld.sar = " + sar +
                    "        and kld.nar like '%" + nar + "'" +
                    "ORDER BY list.data DESC ";
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) {
                StringBuilder composition = new StringBuilder();
                String sostav1 = rs.getString("sostav1").trim();
                String sostav2 = rs.getString("sostav2").trim();
                String sostav3 = rs.getString("sostav3").trim();
                if (sostav1 != null && !sostav1.equals("")) {
                    composition.append(sostav1);
                }
                if (sostav2 != null && !sostav2.equals("")) {
                    composition.append(", " + sostav2);
                }
                if (sostav3 != null && !sostav3.equals("")) {
                    composition.append(", " + sostav3);
                }
                return composition.toString();
            }
        } catch (Exception e) {
            System.out.println("Ошибка PlanDB -> getAllCompositionsByListModels");
            e.printStackTrace();
            throw new Exception("Ошибка getAllCompositionsByListModels() " + e.getMessage(), e);
        }
        return "";
    }

    public Vector getDetalModel(int sar) throws Exception {
        Vector element = new Vector();
        String sql = "Select distinct ngpr, nar, prim "
                + "     From nsi_kld "
                + "     Where sar = ? ";

        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, sar);
            rs = ps.executeQuery();
            if (rs.next()) {
                element.add(rs.getString("ngpr").trim());
                element.add(rs.getString("nar").trim());
                element.add(rs.getString("prim").trim());
            }

        } catch (Exception e) {
            System.err.println("Ошибка getDetalModel() " + e);
            //  log.error("Ошибка getDetalModel()", e);
            throw new Exception("Ошибка getDetalModel() " + e.getMessage(), e);
        }
        return element;
    }

    public Vector getAllColors() throws Exception {
        Vector elements = new Vector();
        String sql = "Select distinct cw, ncw "
                + "     From nsi_cd "
                + "     Order by ncw";

        try {
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                Vector tmp = new Vector();
                tmp.add(false);
                tmp.add(rs.getInt("cw"));
                tmp.add(rs.getString("ncw").trim());
                elements.add(tmp);
            }

        } catch (Exception e) {
            System.err.println("Ошибка getAllColors() " + e);
            // log.error("Ошибка getAllColors()", e);
            throw new Exception("Ошибка getAllColors() " + e.getMessage(), e);
        }
        return elements;
    }

    public Vector getAllPolotno() throws Exception {
        Vector elements = new Vector();
        String sql = "Select distinct kod, narpol, sostav1, sostav2"
                + "     From nsi_polotno "
                + "     Order by narpol";

        try {
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                Vector tmp = new Vector();
                tmp.add(false);
                tmp.add(rs.getInt("kod"));
                tmp.add(rs.getString("narpol").trim());
                tmp.add(rs.getString("sostav1").trim() + " " + rs.getString("sostav2").trim());
                elements.add(tmp);
            }

        } catch (Exception e) {
            System.err.println("Ошибка getAllPolotno() " + e);
            // log.error("Ошибка getAllPolotno()", e);
            throw new Exception("Ошибка getAllPolotno() " + e.getMessage(), e);
        }
        return elements;
    }

    public Vector openRstRzm(int fas) throws Exception {
        Vector elements = new Vector();
        String sql = "";

        try {

            sql = "SELECT DISTINCT rzm "
                    + "     FROM nsi_sd INNER JOIN "
                    + "          nsi_kld ON nsi_sd.kod = nsi_kld.kod "
                    + "     WHERE nsi_kld.fas = ? "
                    + "     ORDER BY rzm ";

            ps = conn.prepareStatement(sql);
            ps.setInt(1, fas);
            rs = ps.executeQuery();
            while (rs.next()) {
                Vector tmp = new Vector();
                tmp.add(false);
                tmp.add(0);
                tmp.add(rs.getInt("rzm"));
                elements.add(tmp);
            }

            sql = "SELECT DISTINCT rst, rzm "
                    + "     FROM nsi_sd INNER JOIN "
                    + "          nsi_kld ON nsi_sd.kod = nsi_kld.kod "
                    + "     WHERE nsi_kld.fas = ? and rst <> 0 "
                    + "     ORDER BY rst, rzm ";

            ps = conn.prepareStatement(sql);
            ps.setInt(1, fas);
            rs = ps.executeQuery();
            while (rs.next()) {
                Vector tmp = new Vector();
                tmp.add(false);
                tmp.add(rs.getInt("rst"));
                tmp.add(rs.getInt("rzm"));
                elements.add(tmp);
            }

        } catch (Exception e) {
            System.err.println("Ошибка getAllModels() " + e);
            //log.error("Ошибка getAllModels()", e);
            throw new Exception("Ошибка getAllModels() " + e.getMessage(), e);
        }
        return elements;
    }

    public Vector openPolotno(String npol) throws Exception {
        Vector elements = new Vector();
        String sql = "Select distinct kod, narpol, sostav1, sostav2"
                + "     From nsi_polotno "
                + "     Where upper(replace(narpol,' ','')) like upper(replace('" + npol + "%',' ',''))"
                + "     Order by narpol";

        try {
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                Vector tmp = new Vector();
                tmp.add(false);
                tmp.add(rs.getInt("kod"));
                tmp.add(rs.getString("narpol").trim());
                tmp.add(rs.getString("sostav1").trim() + " " + rs.getString("sostav2").trim());
                elements.add(tmp);
            }

        } catch (Exception e) {
            System.err.println("Ошибка openPolotno() " + e);
            //log.error("Ошибка openPolotno()", e);
            throw new Exception("Ошибка openPolotno() " + e.getMessage(), e);
        }
        return elements;
    }

    public Vector searchNar(Vector data, int year) throws Exception {
        Vector elements = new Vector();
        String sql = "";
        try {

            for (Object element : data) {
                int fas = Integer.valueOf(element.toString());

                sql = "Select top 1 nar "
                        + " From (Select nar, SUBSTRING(nar, 1, 1) as endYear "
                        + "      From nsi_kld "
                        + "      Where nsi_kld.nar not like '%ОБ%' and "
                        + "            nsi_kld.nar not like '%ВД%' and  "
                        + "            nsi_kld.nar not like '%ХД%' and "
                        + "            nsi_kld.nar not like 'Б%' and "
                        + "            nsi_kld.nar not like 'б%' and "
                        + "            nsi_kld.nar not like '%Б%' and "
                        + "            nsi_kld.nar not like '%б%' and "
                        + "            nsi_kld.nar not like '' and "
                        //      заказные артикула
                        //      + "            nsi_kld.nar not like '__7___Д40' and "
                        + "            nsi_kld.fas = ?) as t1 "
                        + " Where t1.endYear = ? "
                        + " Order by t1.endYear desc ";

                ps = conn.prepareStatement(sql);
                ps.setInt(1, fas);
                ps.setInt(2, year);
                rs = ps.executeQuery();

                while (rs.next()) {
                    Vector tmp = new Vector();
                    tmp.add(fas);
                    tmp.add(rs.getString("nar"));

                    if (!elements.contains(tmp))
                        elements.add(tmp);
                }
            }

        } catch (Exception e) {
            elements = new Vector();
            System.err.println("Ошибка searchNar() " + e);
            //log.error("Ошибка searchNar()", e);
            throw new Exception("Ошибка searchNar() " + e.getMessage(), e);
        }
        return elements;
    }

    public Vector searchNar(int fas, int stDate, int endDate) throws Exception {
        Vector elements = new Vector();
        String sql = "";
        try {

            sql = " SELECT DISTINCT sar, "
                    + "                nar, "
                    + "                ngpr, "
                    + "                prim = CASE WHEN prim IS NULL THEN '' ELSE prim END, "
                    + "                min (sd1.rst) AS minrst, "
                    + "                max (sd1.rst) AS maxrst, "
                    + "                min (sd1.rzm) AS minrzm, "
                    + "                max (sd1.rzm) AS maxrzm, "
                    + "                sd1.cno, "
                    + "                sd1.cnp, "
                    + "                fas, "
                    + "                nsi_kld.datekrkv AS data "
                    + "  FROM nsi_kld, nsi_sd AS sd1 "
                    + " WHERE     sd1.kod = nsi_kld.kod "
                    + "       AND sd1.srt = 1 "
                    + "       AND sd1.cno > 0 "
                    + "       AND fas = ? "
                    + "       AND nar IN (SELECT nar "
                    + "                     FROM (SELECT nar, SUBSTRING (nar, 1, 1) AS endYear "
                    + "                             FROM nsi_kld "
                    + "                             WHERE    nsi_kld.nar NOT LIKE '%ОБ%' "
                    + "                                  AND nsi_kld.nar NOT LIKE '%ВД%' "
                    + "                                  AND nsi_kld.nar NOT LIKE '%ХД%' "
                    + "                                  AND nsi_kld.nar NOT LIKE 'Б%' "
                    + "                                  AND nsi_kld.nar NOT LIKE 'б%' "
                    + "                                  AND nsi_kld.nar NOT LIKE '%Б%' "
                    + "                                  AND nsi_kld.nar NOT LIKE '%б%' "
                    + "                                  AND nsi_kld.nar NOT LIKE '' "
                    // заказные артикула
                    // + "                                  AND nsi_kld.nar NOT LIKE '__7___Д40' "
                    + "                                  AND nsi_kld.fas = ? ) AS t1 "
                    + "                    WHERE t1.endYear BETWEEN ? AND ? ) "
                    + "GROUP BY fas, "
                    + "         nar, "
                    + "         sar, "
                    + "         ngpr, "
                    + "         prim, "
                    + "         sd1.cno, "
                    + "         sd1.nds, "
                    + "         sd1.cnr, "
                    + "         sd1.cnp, "
                    + "         sd1.srt, "
                    + "         nsi_kld.datekrkv "
                    + "ORDER BY data DESC; ";

            ps = conn.prepareStatement(sql);
            ps.setInt(1, fas);
            ps.setInt(2, fas);
            ps.setInt(3, stDate);
            ps.setInt(4, endDate);
            rs = ps.executeQuery();

            // System.out.println("Поиск ");
            while (rs.next()) {
                Vector tmp = new Vector();
                tmp.add(rs.getObject("sar"));                                   // шифр. артикул
                tmp.add(rs.getString("ngpr").trim());                           // наименование
                tmp.add(rs.getString("fas"));                                   // модель
                tmp.add(rs.getObject("nar"));                                   // артикул
                tmp.add(rs.getString("minrst").equals(rs.getString("maxrst")) ? rs.getString("minrst") : rs.getString("minrst") + "--" + rs.getString("maxrst"));
                tmp.add(rs.getString("minrzm").equals(rs.getString("maxrzm")) ? rs.getString("minrzm") : rs.getString("minrzm") + "--" + rs.getString("maxrzm"));
                tmp.add(rs.getString("prim").trim());                           // состав сырья
                tmp.add(rs.getDouble("cno"));                                   // цена план.
                tmp.add(rs.getDouble("cnp"));                                   // цена в RUB
                elements.add(tmp);
                //System.out.println("Найдено ");
            }

        } catch (Exception e) {
            elements = new Vector();
            System.err.println("Ошибка searchNar() " + e);
            // log.error("Ошибка searchNar()", e);
            throw new Exception("Ошибка searchNar() " + e.getMessage(), e);
        }
        return elements;
    }

    public Vector getDataItemProjectStokCena(Vector data) throws Exception {
        Vector elements = new Vector();
        String sql = "";
        try {

            for (Object obj : data) {
                Vector vec = (Vector) obj;

                int fas = Integer.valueOf(vec.get(3).toString());
                String nar = vec.get(7).toString();

                String[] sizes = vec.get(9).toString().split("--");
                String rzm = "";

                if (sizes.length == 2) {
                    rzm = " and rzm between " + sizes[0] + " and " + sizes[1] + " ";
                } else if (sizes.length == 1) {
                    rzm = " and rzm = " + sizes[0] + " ";
                }

                Vector tmp = new Vector();

                if (!nar.trim().equals("")) {

                    nar = nar + '%';

                    sql = "Select distinct cno "
                            + " From nsi_sd INNER JOIN "
                            + "         nsi_kld ON nsi_sd.kod = nsi_kld.kod "
                            + " Where fas = ?  "
                            + "       and nar like ? "
                            + rzm
                            + "       and cno > 0  "
                            + "       and srt = 1 "
                            + " Order by cno ";

                    ps = conn.prepareStatement(sql);
                    ps.setInt(1, fas);
                    ps.setString(2, nar);
                    rs = ps.executeQuery();

                    while (rs.next()) {
                        tmp.add(rs.getDouble("cno"));
                    }

                    vec.add(tmp);
                } else {
                    vec.add(tmp);
                }

                elements.add(vec);
            }

        } catch (Exception e) {
            elements = new Vector();
            System.err.println("Ошибка getDataItemProjectStokCena() " + e);
            //log.error("Ошибка getDataItemProjectStokCena()", e);
            throw new Exception("Ошибка getDataItemProjectStokCena() " + e.getMessage(), e);
        }
        return elements;
    }

    public Vector getCena(int fas, String nar, String size, int type) throws Exception {
        Vector elements = new Vector();
        String sql = "";

        try {

            if (!nar.trim().equals("")) {
                nar = nar + '%';

                String[] sizes = size.split("--");
                String rzm = "";

                if (sizes.length == 2) {
                    rzm = " and rzm between " + sizes[0] + " and " + sizes[1] + " ";
                } else if (sizes.length == 1) {
                    rzm = " and rzm = " + sizes[0] + " ";
                }

                if (type == 1) {

                    sql = "Select distinct cno "
                            + " From nsi_sd INNER JOIN "
                            + "         nsi_kld ON nsi_sd.kod = nsi_kld.kod "
                            + " Where fas = ?  "
                            + "       and nar like ? "
                            + rzm
                            + "       and cno > 0  "
                            + "       and srt = 1 "
                            + " Order by cno ";

                    ps = conn.prepareStatement(sql);
                    ps.setInt(1, fas);
                    ps.setString(2, nar);
                    rs = ps.executeQuery();

                    while (rs.next()) {
                        elements.add(new BigDecimal(rs.getDouble("cno")).setScale(2, RoundingMode.HALF_UP));
                    }

                } else if (type == 2) {

                    sql = "Select distinct cnp "
                            + " From nsi_sd INNER JOIN "
                            + "         nsi_kld ON nsi_sd.kod = nsi_kld.kod "
                            + " Where fas = ?  "
                            + "       and nar like ? "
                            + rzm
                            + "       and cnp > 0  "
                            + "       and srt = 1 "
                            + " Order by cnp ";

                    ps = conn.prepareStatement(sql);
                    ps.setInt(1, fas);
                    ps.setString(2, nar);
                    rs = ps.executeQuery();

                    while (rs.next()) {
                        elements.add(new BigDecimal(rs.getDouble("cnp")).setScale(1, RoundingMode.HALF_UP));
                    }
                }
            }

        } catch (Exception e) {
            elements = new Vector();
            System.err.println("Ошибка getCena() " + e);
            //log.error("Ошибка getCena()", e);
            throw new Exception("Ошибка getCena() " + e.getMessage(), e);
        }
        return elements;
    }

    public Vector getDataReportCenaProjectPlan(Vector data,
                                               boolean flagKurs, double kursRUB, double kursUSD, double kursEUR) throws Exception {

        Vector elements = new Vector();
        String sql = "";
        Vector tmp = new Vector();
        Vector cena = new Vector();

        try {

            for (Object obj : data) {
                Vector vec = (Vector) obj;

                int fas = Integer.valueOf(vec.get(3).toString());
                String nar = vec.get(4).toString();
                String rzm1 = vec.get(8).toString().trim();
                String rzm2 = vec.get(9).toString().trim();

                tmp = new Vector();

                if (!nar.trim().equals("")) {

                    nar = nar + '%';

                    sql = "Select distinct cno, fas, nar, max(rzm) as maxrzm, min(rzm) as minrzm "
                            + " From nsi_sd INNER JOIN "
                            + "         nsi_kld ON nsi_sd.kod = nsi_kld.kod "
                            + " Where fas = ?  "
                            + "       and nar like ? "
                            + "       and rzm between " + rzm1 + " and " + rzm2 + " "
                            + "       and cno > 0 "
                            + "       and srt = 1 "
                            + " Group by fas, nar, cno "
                            + " Order by fas, nar, cno, minrzm, maxrzm  ";

                    ps = conn.prepareStatement(sql);
                    ps.setInt(1, fas);
                    ps.setString(2, nar);
                    rs = ps.executeQuery();

                    while (rs.next()) {
                        tmp.add(new BigDecimal(rs.getDouble("cno")).setScale(2, RoundingMode.HALF_UP)
                                + " ("
                                + (rs.getString("minrzm").equals(rs.getString("maxrzm")) ?
                                rs.getString("minrzm") :
                                rs.getString("minrzm") + "--" + rs.getString("maxrzm"))
                                + ") "
                        );
                    }

                    vec.add(tmp);

                } else {
                    vec.add(tmp);
                }

                tmp = new Vector();
                cena = new Vector();

                if (!nar.trim().equals("")) {

                    sql = "Select distinct cnp, fas, nar, max(rzm) as maxrzm, min(rzm) as minrzm "
                            + " From nsi_sd INNER JOIN "
                            + "         nsi_kld ON nsi_sd.kod = nsi_kld.kod "
                            + " Where fas = ?  "
                            + "       and nar like ? "
                            + "       and rzm between " + rzm1 + " and " + rzm2 + " "
                            + "       and cnp > 0 "
                            + "       and srt = 1 "
                            + " Group by fas, nar, cnp "
                            + " Order by fas, nar, cnp, minrzm, maxrzm  ";

                    ps = conn.prepareStatement(sql);
                    ps.setInt(1, fas);
                    ps.setString(2, nar);
                    rs = ps.executeQuery();

                    while (rs.next()) {
                        tmp.add(new BigDecimal(rs.getDouble("cnp") * kursRUB).setScale(2, RoundingMode.HALF_UP)
                                + " ("
                                + (rs.getString("minrzm").equals(rs.getString("maxrzm")) ?
                                rs.getString("minrzm") :
                                rs.getString("minrzm") + "--" + rs.getString("maxrzm"))
                                + ") "
                        );

                        Vector tmpCena = new Vector();
                        tmpCena.add(new BigDecimal(rs.getDouble("cnp") * kursRUB).setScale(2, RoundingMode.HALF_UP));
                        tmpCena.add(" ("
                                + (rs.getString("minrzm").equals(rs.getString("maxrzm")) ?
                                rs.getString("minrzm") :
                                rs.getString("minrzm") + "--" + rs.getString("maxrzm"))
                                + ") ");
                        cena.add(tmpCena);
                    }

                    vec.add(tmp);

                    if (flagKurs) {

                        tmp = new Vector();
                        for (Object obj_ : cena) {
                            Vector item = (Vector) obj_;

                            tmp.add(new BigDecimal(Double.valueOf(item.get(0).toString()) / kursUSD).setScale(2, RoundingMode.HALF_UP)
                                    + item.get(1).toString());

                        }
                        vec.add(tmp);

                        tmp = new Vector();
                        for (Object obj_ : cena) {
                            Vector item = (Vector) obj_;

                            tmp.add(new BigDecimal(Double.valueOf(item.get(0).toString()) / kursEUR).setScale(2, RoundingMode.HALF_UP)
                                    + item.get(1).toString());

                        }
                        vec.add(tmp);
                    }

                } else {
                    vec.add(tmp);

                    if (flagKurs) {
                        vec.add(tmp);
                        vec.add(tmp);
                    }
                }

                elements.add(vec);
            }

        } catch (Exception e) {
            elements = new Vector();
            System.err.println("Ошибка getDataReportCenaProjectPlan() " + e);
            // log.error("Ошибка getDataReportCenaProjectPlan()", e);
            throw new Exception("Ошибка getDataReportCenaProjectPlan() " + e.getMessage(), e);
        }
        return elements;
    }


    public Vector getDataReportCenaProjectPlanFork(Vector data,
                                                   boolean flagKurs, double kursRUB, double kursUSD, double kursEUR) throws Exception {

        Vector elements = new Vector();
        String sql = "";
        Vector tmp = new Vector();
        Vector cena = new Vector();

        try {

            for (Object obj : data) {
                Vector vec = (Vector) obj;

                int fas = Integer.valueOf(vec.get(3).toString());
                String nar = vec.get(4).toString();
                String rzm1 = vec.get(8).toString().trim();
                String rzm2 = vec.get(9).toString().trim();

                tmp = new Vector();

                if (!nar.trim().equals("")) {

                    nar = nar + '%';

                    sql = "Select distinct cno, fas, nar, max(rzm) as maxrzm, min(rzm) as minrzm "
                            + " From nsi_sd INNER JOIN "
                            + "         nsi_kld ON nsi_sd.kod = nsi_kld.kod "
                            + " Where fas = ?  "
                            + "       and nar like ? "
                            + "       and rzm between " + rzm1 + " and " + rzm2 + " "
                            + "       and cno > 0 "
                            + "       and srt = 1 "
                            + " Group by fas, nar, cno "
                            + " Order by fas, nar, cno, minrzm, maxrzm  ";

                    ps = conn.prepareStatement(sql);
                    ps.setInt(1, fas);
                    ps.setString(2, nar);
                    rs = ps.executeQuery();

                    while (rs.next()) {
                        tmp.add(new BigDecimal(rs.getDouble("cno")).setScale(2, RoundingMode.HALF_UP)
                                + " ("
                                + (rs.getString("minrzm").equals(rs.getString("maxrzm")) ?
                                rs.getString("minrzm") :
                                rs.getString("minrzm") + "--" + rs.getString("maxrzm"))
                                + ") "
                        );
                    }

                    vec.add(tmp);

                } else {
                    vec.add(tmp);
                }

                tmp = new Vector();
                cena = new Vector();

                if (!nar.trim().equals("")) {

                    sql = "Select distinct cno, fas, nar, max(rzm) as maxrzm, min(rzm) as minrzm "
                            + " From nsi_sd INNER JOIN "
                            + "         nsi_kld ON nsi_sd.kod = nsi_kld.kod "
                            + " Where fas = ?  "
                            + "       and nar like ? "
                            + "       and rzm between " + rzm1 + " and " + rzm2 + " "
                            + "       and cno > 0 "
                            + "       and srt = 1 "
                            + " Group by fas, nar, cno "
                            + " Order by fas, nar, cno, minrzm, maxrzm  ";

                    ps = conn.prepareStatement(sql);
                    ps.setInt(1, fas);
                    ps.setString(2, nar);
                    rs = ps.executeQuery();

                    while (rs.next()) {
                        // БелРуб
                        tmp.add(new BigDecimal(rs.getDouble("cno") / kursRUB * 100).setScale(2, RoundingMode.HALF_UP)
                                + " ("
                                + (rs.getString("minrzm").equals(rs.getString("maxrzm")) ?
                                rs.getString("minrzm") :
                                rs.getString("minrzm") + "--" + rs.getString("maxrzm"))
                                + ") "
                        );

                        Vector tmpCena = new Vector();
                        tmpCena.add(new BigDecimal(rs.getDouble("cno") / kursRUB * 100).setScale(2, RoundingMode.HALF_UP));
                        tmpCena.add(" ("
                                + (rs.getString("minrzm").equals(rs.getString("maxrzm")) ?
                                rs.getString("minrzm") :
                                rs.getString("minrzm") + "--" + rs.getString("maxrzm"))
                                + ") ");

                        tmpCena.add(new BigDecimal(rs.getDouble("cno")));
                        cena.add(tmpCena);
                    }

                    vec.add(tmp);

                    if (flagKurs) {

                        tmp = new Vector();
                        for (Object obj_ : cena) {
                            Vector item = (Vector) obj_;

                            tmp.add(new BigDecimal(Double.valueOf(item.get(2).toString()) / kursUSD).setScale(2, RoundingMode.HALF_UP)
                                    + item.get(1).toString());

                        }
                        vec.add(tmp);

                        tmp = new Vector();
                        for (Object obj_ : cena) {
                            Vector item = (Vector) obj_;

                            tmp.add(new BigDecimal(Double.valueOf(item.get(2).toString()) / kursEUR).setScale(2, RoundingMode.HALF_UP)
                                    + item.get(1).toString());

                        }
                        vec.add(tmp);
                    }

                } else {
                    vec.add(tmp);

                    if (flagKurs) {
                        vec.add(tmp);
                        vec.add(tmp);
                    }
                }

                elements.add(vec);
            }

        } catch (Exception e) {
            elements = new Vector();
            System.err.println("Ошибка getDataReportCenaProjectPlan() " + e);
            // log.error("Ошибка getDataReportCenaProjectPlan()", e);
            throw new Exception("Ошибка getDataReportCenaProjectPlan() " + e.getMessage(), e);
        }
        return elements;
    }

    public Vector searchNarWorkArticle(Vector data) throws Exception {
        Vector elements = new Vector();
        String sql = "";
        try {

            for (Object obj : data) {
                Vector vec = (Vector) obj;

                int fas = Integer.valueOf(vec.get(3).toString());
                String nar = vec.get(4).toString().toUpperCase().replace("C", "_").replace("С", "_").trim();

                if (!nar.trim().equals("")) {

                    sql = "Select distinct nar "
                            + " From  nsi_kld "
                            + " Where fas = ?  "
                            + "       and nar like '" + nar + "' ";

                    ps = conn.prepareStatement(sql);
                    ps.setInt(1, fas);
                    rs = ps.executeQuery();

                    if (rs.next()) {
                        vec.setElementAt(rs.getString("nar"), 5);
                    }
                }

                elements.add(vec);

            }

        } catch (Exception e) {
            elements = new Vector();
            System.err.println("Ошибка searchNarWorkArticle() " + e);
            //log.error("Ошибка searchNarWorkArticle()", e);
            throw new Exception("Ошибка searchNarWorkArticle() " + e.getMessage(), e);
        }
        return elements;
    }

    /**
     * Информация о модели по номеру и шифру.
     * @param fas
     * @param sar
     * @param nar
     * @return первая строка sar, nar, fas, ngpr, ean
     * @throws Exception
     */
    public String getNar(int fas, int sar, String nar) throws Exception {
        String element = "";
        String sql = "";

        try {
            sql = "SELECT TOP 1 nar "
                    + " FROM  nsi_kld INNER JOIN"
                    + "        nsi_sd ON nsi_sd.kod = nsi_kld.kod "
                    + " WHERE nsi_kld.fas = ? AND "
                    + "       nsi_kld.sar = ? AND "
                    + "       nsi_kld.fas > 0 "
                    + (nar.trim().equals("") ? " " : " and  nsi_kld.nar like '" + nar + "%' ")
                    + " ORDER BY  ean  ";

            ps = conn.prepareStatement(sql);
            ps.setInt(1, fas);
            ps.setInt(2, sar);
            rs = ps.executeQuery();

            if (rs.next()) {
                element = rs.getString("nar").trim().toLowerCase();
            }

        } catch (Exception e) {
            element = "";
            System.err.println("Ошибка getNar() " + e);
            //log.error("Ошибка getNar()", e);
            throw new Exception("Ошибка getNar() " + e.getMessage(), e);
        }
        return element;
    }

}