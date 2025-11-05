package dept.marketing.cena;

import by.march8.ecs.application.modules.marketing.model.PriceParamsItem;
import by.march8.ecs.framework.common.LogCrutch;
import workDB.PDB_new;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.Vector;

//import org.apache.log4j.Logger;

/**
 *
 * @author lidashka
 */
public class CenaPDB extends PDB_new {
    //private static final Logger log = new Log().getLoger(CenaDB.class);
    private static final LogCrutch log = new LogCrutch();

    /**
     * Возвращает актуальную рентабельность.
     * @param sar шифр артикула
     * @param size размер
     * @return рентабельность
     */
    public float getRentabel(Long sar, Long size) throws Exception {
        float rent = 0;
        String sql = "select prc from nsi_plan_sstoimost where art = ? and razm <= ? order by begin_date desc, dt_korr desc, razm desc limit 1 ";

        try {
            ps = conn.prepareStatement(sql);
            ps.setString(1, sar.toString());
            ps.setLong(2, size);
            rs = ps.executeQuery();

            if (rs.next()) {
                rent = rs.getFloat(1);
            } else {
                sql = "select prc from nsi_plan_sstoimost where art = ? and razm = 0  order by begin_date desc, dt_korr desc, razm desc limit 1 ";

                ps = conn.prepareStatement(sql);
                ps.setString(1, sar.toString());
                rs = ps.executeQuery();

                if (rs.next()) {
                    rent = rs.getFloat(1);
                }
            }
        } catch (Exception e) {
            System.err.println("Ошибка getRentabel() " + e);
            log.error("Ошибка getRentabel()", e);
            throw new Exception("Ошибка getRentabel() ", e);
        }
        return rent;
    }

    /**
     * Возвращает актуальную себестоимость.
     * @param sar шифр артикула
     * @param size размер
     * @return себестоимость
     */
    public float getSstoimost(Long sar, Long size) throws Exception {
        float zatr = 0;
        String sql = "select  (syrje + toplivo + vsp_mat + amort + dor_fond + soc_strax + fndzan_ozd + chrez_nal + prochie) "
                + " from nsi_plan_sstoimost where art = ? and razm <= ? order by begin_date desc, dt_korr desc, razm desc, id desc limit 1 ";

        try {
            ps = conn.prepareStatement(sql);
            ps.setString(1, sar.toString());
            ps.setLong(2, size);
            rs = ps.executeQuery();

            if (rs.next()) {
                zatr = rs.getFloat(1);
            } else {
                sql = "select (syrje + toplivo + vsp_mat + amort + dor_fond + soc_strax + fndzan_ozd + chrez_nal + prochie) "
                        + " from nsi_plan_sstoimost where art = ? and razm = 0  order by begin_date desc, dt_korr desc, razm desc, id desc limit 1 ";

                ps = conn.prepareStatement(sql);
                ps.setString(1, sar.toString());
                rs = ps.executeQuery();

                if (rs.next()) {
                    zatr = rs.getFloat(1);
                }
            }
        } catch (Exception e) {
            System.err.println("Ошибка getSstoimost() " + e);
            log.error("Ошибка getSstoimost()", e);
            throw new Exception("Ошибка getSstoimost() ", e);
        }
        return zatr;
    }

    /**
     * Возвращает актуальную себестоимость и рентабельность.
     * @param sar шифр артикула
     * @param size размер
     * @return float[себестоимость, рентабельность]
     */
    public float[] getSstoimostRentabel(Object sar, int size) throws Exception {
        float[] rez = {0, 0};
        String sql = "select (syrje + toplivo + vsp_mat + amort + dor_fond + soc_strax + fndzan_ozd + chrez_nal + prochie), "
                + "prc from nsi_plan_sstoimost where art = ? and razm <= ? order by begin_date desc, dt_korr desc, razm desc, id desc limit 1 ";

        try {
            ps = conn.prepareStatement(sql);
            ps.setString(1, sar.toString());
            ps.setLong(2, size);
            rs = ps.executeQuery();

            if (rs.next()) {
                rez[0] = rs.getFloat(1);
                rez[1] = rs.getFloat(2);
            } else {
                sql = "select (syrje + toplivo + vsp_mat + amort + dor_fond + soc_strax + fndzan_ozd + chrez_nal + prochie), "
                        + "prc from nsi_plan_sstoimost where art = ? and razm = 0  order by begin_date desc, dt_korr desc, razm desc, id desc limit 1 ";

                ps = conn.prepareStatement(sql);
                ps.setString(1, sar.toString());
                rs = ps.executeQuery();

                if (rs.next()) {
                    rez[0] = rs.getFloat(1);
                    rez[1] = rs.getFloat(2);
                }
            }
        } catch (Exception e) {
            System.err.println("Ошибка getSstoimostRentabel() " + e);
            log.error("Ошибка getSstoimostRentabel()", e);
            throw new Exception("Ошибка getSstoimostRentabel() ", e);
        }
        return rez;
    }

    public ProfitabilityItem getSstoimostRentabelAdv(Object sar, int size) {
        float[] rez = {0, 0};
        String sql = "select (syrje + toplivo + vsp_mat + amort + dor_fond + soc_strax + fndzan_ozd + chrez_nal + prochie), "
                + "prc from nsi_plan_sstoimost where art = ? and razm <= ? order by begin_date desc, dt_korr desc, razm desc, id desc limit 1 ";
        ProfitabilityItem result = new ProfitabilityItem();

        try {
            ps = conn.prepareStatement(sql);
            ps.setString(1, sar.toString());
            ps.setLong(2, size);
            rs = ps.executeQuery();

            if (rs.next()) {
                result.setPrimeCostReference(rs.getFloat(1));
                result.setProfitabilityReference(rs.getFloat(2));
            } else {
                sql = "select (syrje + toplivo + vsp_mat + amort + dor_fond + soc_strax + fndzan_ozd + chrez_nal + prochie), "
                        + "prc from nsi_plan_sstoimost where art = ? and razm = 0  order by begin_date desc, dt_korr desc, razm desc, id desc limit 1 ";

                ps = conn.prepareStatement(sql);
                ps.setString(1, sar.toString());
                rs = ps.executeQuery();

                if (rs.next()) {
                    result.setPrimeCostReference(rs.getFloat(1));
                    result.setProfitabilityReference(rs.getFloat(2));
                }
            }
        } catch (Exception e) {
            System.err.println("Ошибка getSstoimostRentabel() " + e);
            log.error("Ошибка getSstoimostRentabel()", e);
        }
        return result;
    }

    /**
     * Возвращает полную информацию по изделию, которая удовлетворяет параметрам поиска
     * @return Vector[Vector] (true/false, модель, шифр. артикул, артикул, наименование, сорт, min рост, max рост, minrst--maxrst,
     *                  min размер, max размер, minrzm-maxrzm, состав сырья, цена план., ндс, цена розничная, тн, цена реализации, 
     *                  себ-сть расчётная, рен-сть из справ., цена в RUB, цена в USD, цена в EUR , себ-сть из справ., рен-сть расчётная) 
     * @throws Exception
     */
    public Vector getFullTableAdvanSearch(Vector fullTableAdvanSearch) throws Exception {
        Vector rez = new Vector();
        ResultSet rs_ = null;
        String sql = "";

        double ss_sprav;
        double rent_sprav;
        double ss;
        double rent;
        int i;

        try {
            sql = "Drop table if exists tmpcarts;";
            ps = conn.prepareStatement(sql);
            ps.executeUpdate();

            sql = "Create TEMPORARY table advansearch ("
                    + "			  fas integer NOT NULL,"
                    + "			  sar integer NOT NULL,"
                    + "			  nar character varying(20) NOT NULL,"
                    + "			  ngpr character varying(50) NOT NULL,"
                    + "			  srt integer NOT NULL,"
                    + "			  minrst integer NOT NULL,"
                    + "			  maxrst integer NOT NULL,"
                    + "			  rst character varying(10) NOT NULL,"
                    + "			  minrzm integer NOT NULL,"
                    + "			  maxrzm integer NOT NULL,"
                    + "			  rzm character varying(10) NOT NULL,"
                    + "			  sostav character varying(50) NOT NULL,"
                    + "			  cno double precision NOT NULL,"
                    + "			  nds integer NOT NULL,"
                    + "			  cnr double precision NOT NULL,"
                    + "			  tn double precision NOT NULL,"
                    + "			  cenr double precision NOT NULL,"
                    + "			  cnp double precision NOT NULL);";
            stmt = conn.createStatement();
            stmt.executeUpdate(sql);

            sql = "Insert into advansearch(fas, sar, nar, ngpr , srt, minrst , maxrst , rst , minrzm, maxrzm,  rzm, sostav , cno,  nds , cnr, tn, cenr , cnp)"
                    + "		values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);";
            ps = conn.prepareStatement(sql);

            for (Iterator it = fullTableAdvanSearch.iterator(); it.hasNext(); ) {
                i = 0;
                Vector items = (Vector) it.next();
                for (Iterator it1 = items.iterator(); it1.hasNext(); ) {
                    Object item = it1.next();
                    ps.setObject(++i, item);
                }
                ps.executeUpdate();
            }

            sql = "Select fas,advansearch.sar,nar,ngpr,srt,minrst,maxrst,rst,minrzm,maxrzm,rzm,sostav,cno,nds,cnr,tn,cenr,cnp, t3.seb, t3.prc "
                    + "             From advansearch "
                    + "               left join ( "
                    + "			select t1.art as sart, t1.begin_date as ds,t1.dt_korr as dk, t1.razm, seb, t1.prc as prc "
                    + "			from (	select  id, begin_date, dt_korr, art, razm, (syrje + toplivo + vsp_mat + amort + dor_fond + soc_strax + fndzan_ozd + chrez_nal + prochie) as seb, prc "
                    + "				from nsi_plan_sstoimost) as t1 "
                    + "                        join (	select max(begin_date) as d, max(dt_korr) as k, max(id) as i, art, razm "
                    + "				from nsi_plan_sstoimost "
                    + "				where art in (select text(sar) from advansearch) "
                    + "				group by art,razm) as t2 "
                    + "			on t1.dt_korr = t2.k and t1.art = t2.art and t1.id = t2.i and  t1.razm = t2.razm"
                    + "			where t1.art in (select text(sar) from advansearch) "
                    + "			order by t1.art) as t3  "
                    + "		on text(advansearch.sar) = t3.sart and( (t3.razm between minrzm and maxrzm) or t3.razm = 0) "
                    + "             Order by fas,nar,advansearch.sar,ngpr,srt,minrst,maxrst,rst,minrzm,maxrzm,rzm  ";

            ps = conn.prepareStatement(sql);
            rs_ = ps.executeQuery();

            while (rs_.next()) {
                ss = new Double("0");
                rent = new Double("0");

                ss_sprav = new BigDecimal(rs_.getDouble("seb")).setScale(2, RoundingMode.HALF_UP).doubleValue();
                rent_sprav = new BigDecimal(rs_.getDouble("prc")).setScale(2, RoundingMode.HALF_UP).doubleValue();

                if (ss_sprav == 0 && rent_sprav == 0 && rs_.getString("nar").indexOf("ОБ") == -1 &&
                        rs_.getString("nar").indexOf("ХД") == -1 && rs_.getString("nar").indexOf("ВД") == -1) {
                    float[] data = getSstoimostRentabel(rs_.getObject("sar"), rs_.getInt("maxrzm"));
                    ss_sprav = new BigDecimal(data[0]).setScale(4, RoundingMode.HALF_UP).doubleValue();
                    rent_sprav = new BigDecimal(data[1]).setScale(2, RoundingMode.HALF_UP).doubleValue();
                }

                if (ss_sprav > 0)
                    rent = new BigDecimal(((rs_.getDouble("cno") - ss_sprav) / ss_sprav) * 100).setScale(2, RoundingMode.HALF_UP).doubleValue();

                if (rent_sprav > 0)
                    ss = new BigDecimal((rs_.getDouble("cno") * 100) / (rent_sprav + 100)).setScale(4, RoundingMode.HALF_UP).doubleValue();
                else {
                    ss = ss_sprav;
                    rent_sprav = rent;
                }

                Vector tmp = new Vector();
                tmp.add(false);                                             // checkbox
                tmp.add(rs_.getObject("fas"));                              // модель
                tmp.add(rs_.getObject("sar"));                              // шифр. артикул    
                tmp.add(rs_.getObject("nar"));                              // артикул
                tmp.add(rs_.getString("ngpr").trim());                      // наименование
                tmp.add(rs_.getObject("srt"));                              // сорт                    
                tmp.add(rs_.getString("minrst"));                           // min рост
                tmp.add(rs_.getString("maxrst"));                           // max рост                 
                tmp.add(rs_.getString("rst"));
                tmp.add(rs_.getString("minrzm"));                           // min размер
                tmp.add(rs_.getString("maxrzm"));                           // max размер                    
                tmp.add(rs_.getString("rzm"));
                tmp.add(rs_.getString("sostav").trim());                    // состав сырья
                tmp.add(rs_.getDouble("cno"));                              // цена план.
                tmp.add(rs_.getObject("nds"));                              // ндс
                tmp.add(rs_.getDouble("cnr"));                              // цена реализации                    
                tmp.add(rs_.getDouble("tn"));                               // тн
                tmp.add(rs_.getDouble("cenr"));                                // цена розничная                    
                tmp.add(ss);                                                // себ-сть расчётная 
                tmp.add(rent_sprav);                                        // рен-сть из справ.   
                tmp.add(rs_.getDouble("cnp"));                              // цена в RUB    
                tmp.add(new Double(0));                                     // цена в USD 
                tmp.add(new Double(0));                                     // цена в EUR 
                tmp.add(ss_sprav);                                          // себ-сть из справ.
                tmp.add(rent);                                              // рен-сть расчётная 
                if (!rez.contains(tmp))
                    rez.add(tmp);
            }

        } catch (Exception e) {
            System.err.println("Ошибка getFullTableAdvanSearch() " + e);
            log.error("Ошибка getFullTableAdvanSearch()", e);
            throw new Exception("Ошибка getFullTableAdvanSearch() ", e);
        }
        return rez;
    }

    /**
     * Возвращает подробный перечень затрат 
     * @param sar шифр артикула
     * @param maxrzm размер
     * @return Vector[затраты, ...] 
     * @throws Exception
     */
    public Vector getHistoryZatraty(int sar, int maxrzm) throws Exception {
        Vector rezalt = new Vector();
        try {
            ps = conn.prepareStatement("Select begin_date, razm, syrje, vsp_mat, toplivo, zarplata, soc_strax, fndzan_ozd, chrez_nal, prochie,"
                    + "       (syrje + toplivo + vsp_mat + amort + dor_fond + soc_strax + fndzan_ozd + chrez_nal + prochie) as ss, prc, dt_korr "
                    + "   from nsi_plan_sstoimost where art='" + sar + "' and razm = (Select max(razm) from nsi_plan_sstoimost  "
                    + "where razm<=" + maxrzm + " and art='" + sar + "') "
                    + " Order by begin_date, dt_korr ");
            rs = ps.executeQuery();
            while (rs.next()) {
                Vector tmp = new Vector();
                tmp.add(new SimpleDateFormat("dd-MM-yyyy").format(rs.getDate("begin_date")));
                tmp.add(rs.getInt("razm"));
                tmp.add(rs.getFloat("syrje"));
                tmp.add(rs.getFloat("vsp_mat"));
                tmp.add(rs.getFloat("toplivo"));
                tmp.add(rs.getFloat("zarplata"));
                tmp.add(rs.getFloat("soc_strax"));
                tmp.add(rs.getFloat("fndzan_ozd"));
                tmp.add(rs.getFloat("chrez_nal"));
                tmp.add(rs.getFloat("prochie"));
                tmp.add(new BigDecimal(rs.getDouble("ss")).setScale(4, RoundingMode.HALF_UP).doubleValue());
                tmp.add(rs.getFloat("prc"));
                tmp.add(new SimpleDateFormat("dd-MM-yyyy").format(rs.getDate("dt_korr")));
                rezalt.add(tmp);
            }
        } catch (Exception e) {
            System.err.println("Ошибка getHistoryZatraty() " + e);
            log.error("Ошибка getHistoryZatraty()", e);
            throw new Exception("Ошибка getHistoryZatraty() ", e);
        }
        return rezalt;
    }

    /**
     * Возвращает история изменения себестоимости изделия
     * @param sar шифр артикула
     * @param rzm размер
     * @return Vector[дата, шифр артикула, размер, себестоимость, рентабельность]
     * @throws Exception
     */
    public Vector getHistorySStoim(int sar, int rzm) throws Exception {
        Vector rezalt = new Vector();
        try {
            ps = conn.prepareStatement("Select min(begin_date) as begin_date, art, razm, "
                    + "         (syrje + toplivo + vsp_mat + amort + dor_fond + soc_strax + fndzan_ozd + chrez_nal + prochie) as ss, "
                    + "         prc, min(dt_korr) as dt_korr"
                    + " from nsi_plan_sstoimost "
                    + " where art = '" + sar + "' and razm = (Select max(razm) from nsi_plan_sstoimost  where razm<=" + rzm + " and art = '" + sar + "') "
                    + " Group by art, razm, ss, prc "
                    + " Order by begin_date, dt_korr ;");
            rs = ps.executeQuery();
            while (rs.next()) {
                Vector tmp = new Vector();
                tmp.add(new SimpleDateFormat("dd-MM-yyyy").format(rs.getDate("begin_date")));
                tmp.add(rs.getString("art"));
                tmp.add(rs.getInt("razm"));
                tmp.add(new BigDecimal(rs.getDouble("ss")).setScale(4, RoundingMode.HALF_UP).doubleValue());
                tmp.add(rs.getFloat("prc"));
                rezalt.add(tmp);
            }
        } catch (Exception e) {
            System.err.println("Ошибка getHistorySStoim() " + e);
            log.error("Ошибка getHistorySStoim()", e);
            throw new Exception("Ошибка getHistorySStoim() ", e);
        }
        return rezalt;
    }

    /**
     * Возвращает история изменения себестоимости изделия
     * @param item изделие
     * @return Изделие
     */
    public PriceParamsItem getPrimeCostValueByItem(PriceParamsItem item) {

        try {
            ps = conn.prepareStatement("Select min(begin_date) as begin_date, art, razm, "
                    + "         (syrje + toplivo + vsp_mat + amort + dor_fond + soc_strax + fndzan_ozd + chrez_nal + prochie) as ss, "
                    + "         prc, min(dt_korr) as dt_korr"
                    + " from nsi_plan_sstoimost "
                    // + " where art = '"+sar+"' and razm = (Select max(razm) from nsi_plan_sstoimost  where razm<="+rzm+" and art = '"+sar+"') "
                    + " Group by art, razm, ss, prc "
                    + " Order by begin_date, dt_korr ;");
            rs = ps.executeQuery();
            while (rs.next()) {
                Vector tmp = new Vector();
                tmp.add(new SimpleDateFormat("dd-MM-yyyy").format(rs.getDate("begin_date")));
                tmp.add(rs.getString("art"));
                tmp.add(rs.getInt("razm"));
                tmp.add(new BigDecimal(rs.getDouble("ss")).setScale(4, RoundingMode.HALF_UP).doubleValue());
                tmp.add(rs.getFloat("prc"));
                //rezalt.add(tmp);
            }
        } catch (Exception e) {
            System.err.println("Ошибка getPrimeCostValueByItem() " + e);
        }

        return item;
    }
}