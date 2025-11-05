package dept.marketing.cena;

import by.gomel.freedev.ucframework.ucdao.jdbc.AbstractMSSQLServerJDBC;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Vector;
import java.util.logging.Logger;

/**
 * @author lidashka
 */

public class CenaDB extends AbstractMSSQLServerJDBC {

    private static final Logger log = Logger.getLogger(CenaDB.class.getName());

    /**
     * Получение всех прейскурантов заданного периода
     *
     * @param sDate начало периода  дд.мм.гггг
     * @param eDate конец периода  дд.мм.гггг
     * @return Vector[Vector] (true/false, прейскурант)
     * @throws Exception
     */
    public Vector getPreiscur(String sDate, String eDate) {
        Vector pr = new Vector();
        String sql = "Select distinct preiscur from nsi_kld, nsi_sd "
                + "where nsi_sd.kod = nsi_kld.kod "
                + "      and cno <> 0 "
                + "      and (nsi_sd.datevrkv between ? and ?"
                + "           or nsi_sd.datekrkv between ? and ?)"
                + "Order by preiscur";
        try (PreparedStatement ps = getConnection().prepareStatement(sql)) {
            ps.setString(1, sDate);
            ps.setString(2, eDate);
            ps.setString(3, sDate);
            ps.setString(4, eDate);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Vector tmp = new Vector();
                tmp.add(false);
                tmp.add(rs.getString("preiscur").trim());
                pr.add(tmp);
            }
        } catch (Exception e) {
            log.severe("Ошибка getPreiscur(): " + e.getMessage());
        }
        return pr;
    }

    /**
     * Возвращает полную информацию по изделию, которая удовлетворяет параметрам поиска
     *
     * @param strSar      строка, содержащая шифры артикулов
     * @param strNar      строка, содержащая артикула
     * @param strFas      строка, содержащая модели
     * @param strPreiscur строка, содержащая прейскуранты
     * @return Vector[Vector] (модель, шифр. артикул, артикул, наименование, сорт, min рост, max рост, minrst--maxrst,
     * min размер, max размер, minrzm-maxrzm, состав сырья, цена план., ндс, цена розничная, тн, цена реализации, цена в RUB)
     * @throws Exception
     */
    public Vector getFullTableIzd(Object strSar, Object strNar, Object strFas, Object strPreiscur, boolean cenaV, boolean xo, boolean cenaVP, String sDate, String eDate) throws Exception {
        Vector rezalt = new Vector();
        double tn;

        String sql = "Select distinct sar, nar, ngpr, prim = case when prim is null then '' else prim end,"
                + "         min(sd1.rst) as minrst, max(sd1.rst) as maxrst,min(sd1.rzm) as minrzm, max(sd1.rzm) as maxrzm,"
                + "         sd1.cno, sd1.nds, sd1.cnr, sd1.cnp, sd1.srt, fas "
                + " From nsi_kld, nsi_sd as sd1 "
                + (cenaV && cenaVP ? " , _nsi_cnp " : "")
                + " Where sd1.kod = nsi_kld.kod and "
                + "         sd1.srt = (Select min(sd2.srt) from nsi_sd as sd2 "
                + "                	where sd1.kod = sd2.kod ) and  "
                + "         sd1.cno > 0 "
                + (cenaV ? "and sd1.cnp > 0" : "")
                + (cenaV && cenaVP ? " and sd1.kod1 = _nsi_cnp.kod_izd  "
                + " and date_korr between '" + sDate + " 00:00 ' and '" + eDate + " 23:59' " : "")
//                    +  (xo ? "" : "and nar not like '%ОБ%' and nar not like '%ВД%' and nar not like '%ХД%'" )
                + strSar
                + strNar
                + strFas
                + strPreiscur
                + " Group by fas, nar, sar, ngpr, prim, sd1.cno, sd1.nds, sd1.cnr, sd1.cnp, sd1.srt "
                + " Order by fas, nar, sar, ngpr, prim, sd1.cno, sd1.nds, sd1.cnr, sd1.cnp, sd1.srt, minrst, maxrst, minrzm, maxrzm";


        try (PreparedStatement ps = getConnection().prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                if (rs.getString("sar").charAt(2) == '3' ||
                        rs.getString("sar").charAt(2) == '6') tn = 20;
                else tn = 30;

                Vector tmp = new Vector();
                tmp.add(rs.getObject("fas"));                                   // модель
                tmp.add(rs.getObject("sar"));                                   // шифр. артикул    
                tmp.add(rs.getObject("nar"));                                   // артикул
                tmp.add(rs.getString("ngpr").trim());                           // наименование
                tmp.add(rs.getInt("srt"));                                      // сорт
                tmp.add(rs.getInt("minrst"));                                   // min рост
                tmp.add(rs.getInt("maxrst"));                                   // max рост
                tmp.add(rs.getString("minrst").equals(rs.getString("maxrst")) ? rs.getString("minrst") : rs.getString("minrst") + "--" + rs.getString("maxrst"));
                tmp.add(rs.getInt("minrzm"));                                   // min размер
                tmp.add(rs.getInt("maxrzm"));                                   // max размер
                tmp.add(rs.getString("minrzm").equals(rs.getString("maxrzm")) ? rs.getString("minrzm") : rs.getString("minrzm") + "--" + rs.getString("maxrzm"));
                tmp.add(rs.getString("prim").trim());                           // состав сырья
                tmp.add(rs.getDouble("cno"));                                   // цена план.
                tmp.add(rs.getInt("nds"));                                   // ндс
                tmp.add(rs.getDouble("cnr"));                                   // цена реализации
                tmp.add(tn);                                                    // тн
                tmp.add(BigDecimal.valueOf(rs.getDouble("cno") + tn / 100 * rs.getDouble("cno") + rs.getDouble("nds") / 100 * (rs.getDouble("cno") + tn / 100 * rs.getDouble("cno")))
                        .setScale(2, RoundingMode.HALF_UP).doubleValue());     // цена розничная
                tmp.add(rs.getDouble("cnp"));                                   // цена в RUB 
                rezalt.add(tmp);
            }
        } catch (Exception e) {
            log.severe("Ошибка getFullTableAdvanSearch(): " + e.getMessage());
            throw new Exception("Ошибка getFullTableAdvanSearch(): ", e);
        }
        return rezalt;
    }

    /**
     * Возвращает полную информацию по изделию
     *
     * @param sar
     * @param nar
     * @param fas
     * @param srt
     * @param minrst
     * @param maxrst
     * @param minrzm
     * @param maxrzm
     * @return Vector[Vector] (модель, шифр. артикул, артикул, наименование, сорт, min рост, max рост, minrst--maxrst,
     * min размер, max размер, minrzm-maxrzm, состав сырья, цена план., ндс, цена розничная, тн, цена реализации, цена в RUB)
     * @throws Exception
     */
    public Vector getFullTableIzd(Object sar, Object nar, Object fas, Object srt,
                                  Object minrst, Object maxrst, Object minrzm, Object maxrzm) throws Exception {

        Vector rezalt = new Vector();
        double tn;

        String sql = "Select distinct ngpr, prim = case when prim is null then '' else prim end,"
                + "   min(sd1.rst) as minrst, max(sd1.rst) as maxrst,min(sd1.rzm) as minrzm, max(sd1.rzm) as maxrzm,"
                + "   sd1.cno, sd1.nds, sd1.cnr, sd1.cnp as cnp, sd1.srt, fas "
                + "                     from nsi_kld, nsi_sd as sd1 "
                + "                      where sd1.kod = nsi_kld.kod and "
                + " 			   sar= ? and  "
                + "    			   nar= ? and   "
                + "   			   fas= ? and    "
                + " 			  (rst between ? and ?) and  "
                + "    			  (rzm between ? and ?) and "
                + "			   sd1.srt = ? and "
                + "			   sd1.cno > 0  "
                + "                     Group by fas, nar, sar, ngpr, prim, sd1.cno, sd1.nds, sd1.cnr, sd1.cnp, sd1.srt "
                + "                     Order by minrst , maxrst, minrzm, maxrzm ";

        try (PreparedStatement ps = getConnection().prepareStatement(sql)) {
            ps.setObject(1, sar);
            ps.setObject(2, nar);
            ps.setObject(3, fas);
            ps.setObject(4, minrst);
            ps.setObject(5, maxrst);
            ps.setObject(6, minrzm);
            ps.setObject(7, maxrzm);
            ps.setObject(8, srt);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                if (String.valueOf(sar).charAt(2) == '3' ||
                        String.valueOf(sar).charAt(2) == '6')
                    tn = 20;
                else
                    tn = 30;

                Vector tmp = new Vector();
                tmp.add(fas);                                                   // модель
                tmp.add(sar);                                                   // шифр. артикул    
                tmp.add(nar);                                                   // артикул
                tmp.add(rs.getString("ngpr").trim());                           // наименование
                tmp.add(srt);                                                   // сорт
                tmp.add(rs.getInt("minrst"));                                   // min рост
                tmp.add(rs.getInt("maxrst"));                                   // max рост
                tmp.add(rs.getString("minrst").equals(rs.getString("maxrst")) ? rs.getString("minrst") : rs.getString("minrst") + "--" + rs.getString("maxrst"));
                tmp.add(rs.getInt("minrzm"));                                   // min размер
                tmp.add(rs.getInt("maxrzm"));                                   // max размер
                tmp.add(rs.getString("minrzm").equals(rs.getString("maxrzm")) ? rs.getString("minrzm") : rs.getString("minrzm") + "--" + rs.getString("maxrzm"));
                tmp.add(rs.getString("prim").trim());                           // состав сырья
                tmp.add(rs.getDouble("cno"));                                   // цена план.
                tmp.add(rs.getInt("nds"));                                      // ндс
                tmp.add(rs.getDouble("cnr"));                                   // цена реализации
                tmp.add(tn);                                                    // тн
                tmp.add(BigDecimal.valueOf(rs.getDouble("cno") + tn / 100 * rs.getDouble("cno") + rs.getDouble("nds") / 100 * (rs.getDouble("cno") + tn / 100 * rs.getDouble("cno")))
                        .setScale(2, RoundingMode.HALF_UP).doubleValue());     // цена розничная
                tmp.add(rs.getDouble("cnp"));                                   // цена в RUB 
                rezalt.add(tmp);
            }
        } catch (Exception e) {
            log.severe("Ошибка getFullTableAdvanSearch(): " + e);
            throw new Exception("Ошибка getFullTableAdvanSearch(): ", e);
        }
        return rezalt;
    }

    /**
     * Возвращает полную информацию по изделию
     *
     * @param fas     модель
     * @param partSar шифр артикула
     * @param partNar артикул
     * @param nptk    название изделия
     * @return Vector[Vector] (true/false, модель, шифр. артикул, артикул, наименование, сорт, min рост, max рост, minrst--maxrst,
     * min размер, max размер, minrzm-maxrzm, состав сырья, цена план., ндс, цена розничная, тн, цена реализации,
     * себ-сть расчётная, рен-сть из справ., цена в RUB, цена в USD, цена в EUR , себ-сть из справ., рен-сть расчётная)
     * @throws Exception
     */
    public Vector getFullTableIzd(int fas, Object partSar, Object partNar, String nptk) throws Exception {
        Vector rezalt = new Vector();
        CenaPDB pdb = null;
        String sqlNptk = "";
        double tn;
        double ss_sprav;
        double rent_sprav;
        double ss;
        double rent;

        if (!nptk.isEmpty()) sqlNptk = " nsi_kld.ptk = nsi_pkd.ptk AND (nsi_pkd.nptk LIKE '" + nptk + "%') AND";
        if (!partNar.toString().isEmpty()) partNar = "AND nsi_kld.nar " + partNar + " ";

        String sql = "Select t1.fas, t1.sar, t1.nar, t1.ngpr, t1.prim, t1.srt, min(rst) as minrst, max(rst) as maxrst, min(rzm) as minrzm, max(rzm) as maxrzm, cnp, cno, nds, cnr "
                + " From nsi_sd, nsi_kld ,(Select DISTINCT sar, nar, ngpr, fas, prim = case when prim is null then '' else prim end, min(srt) as srt "
                + "				from nsi_sd, nsi_kld, nsi_pkd, _ost_sklad "
                + "				where " + sqlNptk + " nsi_kld.sar LIKE '" + partSar + "%' and "
                + "					 nsi_kld.fas = " + fas + " and "
                + "                                  nsi_sd.kod = nsi_kld.kod "
                //+ "					 nsi_sd.kod1 = _ost_sklad.kod_izd " +
                + partNar
                + "			Group by sar, nar, ngpr, fas,prim		                "
/*                + "			UNION "
                + "			SELECT DISTINCT sar, nar, ngpr, fas, prim = case when prim is null then '' else prim end, min(srt) as srt "
                + "	                       FROM nsi_kld, nsi_pkd, nsi_sd "
                + "                               WHERE "+sqlNptk+" nsi_kld.sar LIKE '"+partSar+"%' and "
                + "                                 nsi_sd.kod = nsi_kld.kod and "
                + "				 	nsi_kld.fas = "+fas+" "
              //  + "					(nar LIKE '"+ MyReportsModule.sYear+"%' OR nar LIKE '"+ MyReportsModule.eYear+"%') "
                    + partNar
                + "			Group by sar, nar, ngpr, fas,prim*/ + ") as t1 "
                + " Where nsi_sd.kod = nsi_kld.kod and "
                + "	 nsi_kld.sar = t1.sar and "
                + "	 nsi_kld.nar = t1.nar and "
                + "	 nsi_kld.fas = t1.fas and "
                + "	 nsi_sd.srt = t1.srt and "
/*              + "	 nsi_kld.nar not like '%ОБ%' and "
                + "	 nsi_kld.nar not like '%ВД%' and "
                + "	 nsi_kld.nar not like '%ХД%' and "
                */
                + "	 cno > 0 "
                + " Group by t1.fas, t1.sar, t1.nar, t1.ngpr, t1.prim, t1.srt, cnp, cno, nds, cnr "
                + " Order by t1.fas, t1.nar, t1.sar, t1.ngpr, t1.prim, t1.srt, cnp, cno, nds, cnr, minrst, maxrst, minrzm, maxrzm";
        try (PreparedStatement ps = getConnection().prepareStatement(sql);) {
            pdb = new CenaPDB();

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                rent = new Double("0");

                if (rs.getString("sar").charAt(2) == '3' ||
                        rs.getString("sar").charAt(2) == '6')
                    tn = 20;
                else
                    tn = 30;


                float[] data = pdb.getSstoimostRentabel(rs.getObject("sar"), rs.getInt("maxrzm"));

                ss_sprav = BigDecimal.valueOf(data[0]).setScale(4, RoundingMode.HALF_UP).doubleValue();
                rent_sprav = BigDecimal.valueOf(data[1]).setScale(2, RoundingMode.HALF_UP).doubleValue();

                if (ss_sprav > 0) {
                    rent = BigDecimal.valueOf(((rs.getDouble("cno") - ss_sprav) / ss_sprav) * 100).setScale(2, RoundingMode.HALF_UP).doubleValue();
                }

                if (rent_sprav > 0) {
                    ss = BigDecimal.valueOf((rs.getDouble("cno") * 100) / (rent_sprav + 100)).setScale(4, RoundingMode.HALF_UP).doubleValue();
                } else {
                    ss = ss_sprav;
                    rent_sprav = rent;
                }

                Vector tmp = new Vector();
                tmp.add(false);                                             // checkbox
                tmp.add(fas);                                               // модель
                tmp.add(rs.getObject("sar"));                               // шифр. артикул    
                tmp.add(rs.getObject("nar"));                               // артикул
                tmp.add(rs.getString("ngpr").trim());                       // наименование
                tmp.add(rs.getInt("srt"));                                  // сорт                    
                tmp.add(rs.getString("minrst"));                           // min рост
                tmp.add(rs.getString("maxrst"));                           // max рост                 
                tmp.add(rs.getString("minrst").equals(rs.getString("maxrst")) ? rs.getString("minrst") : rs.getString("minrst") + "--" + rs.getString("maxrst"));
                tmp.add(rs.getString("minrzm"));                           // min размер
                tmp.add(rs.getString("maxrzm"));                           // max размер                    
                tmp.add(rs.getString("minrzm").equals(rs.getString("maxrzm")) ? rs.getString("minrzm") : rs.getString("minrzm") + "--" + rs.getString("maxrzm"));
                tmp.add(rs.getString("prim").trim());                       // состав сырья
                tmp.add(rs.getDouble("cno"));                               // цена план.
                tmp.add(rs.getInt("nds"));                              // ндс
                tmp.add(rs.getDouble("cnr"));                               // цена реализации                    
                tmp.add(tn);                                                // тн
                tmp.add(BigDecimal.valueOf(rs.getDouble("cno") + tn / 100 * rs.getDouble("cno") + rs.getDouble("nds") / 100 * (rs.getDouble("cno") + tn / 100 * rs.getDouble("cno")))
                        .setScale(2, RoundingMode.HALF_UP).doubleValue()); // цена розничная
                tmp.add(ss);                                                // себ-сть расчётная 
                tmp.add(rent_sprav);                                        // рен-сть из справ.   
                tmp.add(rs.getDouble("cnp"));                              // цена в RUB    
                tmp.add(new Double(0));                                     // цена в USD 
                tmp.add(new Double(0));                                     // цена в EUR 
                tmp.add(ss_sprav);                                          // себ-сть из справ.
                tmp.add(rent);                                              // рен-сть расчётная 
                rezalt.add(tmp);
            }
        } catch (Exception e) {
            log.severe("Ошибка getFullTableIzd(): " + e);
            throw new Exception("Ошибка getFullTableIzd(): ", e);
        }
        return rezalt;
    }

    /**
     * История изменения цены
     *
     * @param sar    шифр артикула
     * @param nar    артикул
     * @param fas    модель
     * @param srt    сорт
     * @param minrst min значение роста
     * @param maxrst max значение роста
     * @param minrzm min значение размера
     * @param maxrzm max значение размера
     * @param tn     торговая надбавка (%)
     * @return Vector[Vector] (дата, цена пл., НДС, цена реализ., ТН, цена рознич., прейскурант)
     * @throws Exception
     */
    public Vector getHistoryPrice(int sar, String nar, int fas, int srt, int minrst, int maxrst, int minrzm, int maxrzm, double nds, float tn) throws Exception {
        Vector rezalt = new Vector();
        String sql = "Select distinct price_data.date_c, price_data.cno, price_data.nds, price_data.cnr, price_data.preiscur "
                + " from nsi_kld, nsi_sd, price_data "
                + " where nsi_kld.kod=nsi_sd.kod and sar=? and nar=? and fas=? and srt=? "
                + "      and (rst between ? and ?) and (rzm between ? and ?) "
                + "      and nsi_sd.kod1=price_data.kod1 and price_data.cno<>0";
        String sql1 = "Select distinct d_preiscur, cno, nds , cnr , preiscur  from nsi_kld, nsi_sd "
                + " where nsi_kld.kod=nsi_sd.kod and sar= ? and nar= ? and fas= ? and srt= ? and nds= ? "
                + "      and (rst between ? and ?) and (rzm between ? and ?) ";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             PreparedStatement ps1 = conn.prepareStatement(sql1);) {
            ps.setInt(1, sar);
            ps.setString(2, nar);
            ps.setInt(3, fas);
            ps.setInt(4, srt);
            ps.setInt(5, minrst);
            ps.setInt(6, maxrst);
            ps.setInt(7, minrzm);
            ps.setInt(8, maxrzm);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Vector tmp = new Vector();
                tmp.add(new SimpleDateFormat("dd-MM-yyyy").format(rs.getDate(1)));
                tmp.add(rs.getDouble(2));
                tmp.add(rs.getFloat(3));
                tmp.add(rs.getInt(4));
                tmp.add(tn);
                tmp.add(BigDecimal.valueOf(rs.getDouble(2) + tn / 100 * rs.getDouble(2) + rs.getFloat(3) / 100 * (rs.getDouble(2) + tn / 100 * rs.getDouble(2))).setScale(2, RoundingMode.HALF_UP).doubleValue());
                tmp.add(rs.getString(5).trim());
                rezalt.add(tmp);
            }

            ps1.setInt(1, sar);
            ps1.setString(2, nar);
            ps1.setInt(3, fas);
            ps1.setInt(4, srt);
            ps1.setDouble(5, nds);
            ps1.setInt(6, minrst);
            ps1.setInt(7, maxrst);
            ps1.setInt(8, minrzm);
            ps1.setInt(9, maxrzm);
            ResultSet rs1 = ps1.executeQuery();

            if (rs1.next()) {
                Vector tmp = new Vector();
                tmp.add(new SimpleDateFormat("dd-MM-yyyy").format(rs.getDate(1)));
                tmp.add(rs.getDouble(2));
                tmp.add(rs.getFloat(3));
                tmp.add(rs.getInt(4));
                tmp.add(tn);
                tmp.add(BigDecimal.valueOf(rs.getDouble(2) + tn / 100 * rs.getDouble(2) + rs.getFloat(3) / 100 * (rs.getDouble(2) + tn / 100 * rs.getDouble(2))).setScale(2, RoundingMode.HALF_UP).doubleValue());
                tmp.add(rs.getString(5).trim());
                if (!rezalt.contains(tmp)) rezalt.add(tmp);
            }
        } catch (Exception e) {
            log.severe("Ошибка getHistoryPrice(): " + e.getMessage());
            throw new Exception("Ошибка getHistoryPrice(): ", e);
        }
        return rezalt;
    }

    /**
     * Обновляет в справочнике цену в валюте;
     *
     * @param sar
     * @param nar
     * @param fas
     * @param srt
     * @param minrst
     * @param maxrst
     * @param minrzm
     * @param maxrzm
     * @param cno
     * @param cnp
     * @param nds
     * @param newcena
     * @throws Exception
     */
    public void setNewCenaV(int sar, String nar, int fas, int srt, int minrst, int maxrst,
                            int minrzm, int maxrzm, double cno, double cnp, int nds, double newcena) throws Exception {

        String sql = "Update nsi_sd "
                + " Set cnp = ? "
                + " Where kod1 in "
                + "     (Select kod1  "
                + "         From nsi_kld, nsi_sd "
                + "         Where nsi_kld.kod=nsi_sd.kod and "
                + "             sar= ? and "
                + "             nar= ? and "
                + "             fas= ? and "
                + "             srt= ? and "
                + "             (rst between ? and ?) and "
                + "             (rzm between ? and ?) and "
                + "             cno= ? and "
                + "             cnp= ? and "
                + "             nds= ? )";
        try (PreparedStatement ps = getConnection().prepareStatement(sql)) {
            ps.setDouble(1, newcena);
            ps.setInt(2, sar);
            ps.setString(3, nar);
            ps.setInt(4, fas);
            ps.setInt(5, srt);
            ps.setInt(6, minrst);
            ps.setInt(7, maxrst);
            ps.setInt(8, minrzm);
            ps.setInt(9, maxrzm);
            ps.setDouble(10, cno);
            ps.setDouble(11, cnp);
            ps.setInt(12, nds);
            ps.execute();
        } catch (Exception e) {
            log.severe("Ошибка setNewCenaV() " + e.getMessage());
            throw new Exception("Ошибка setNewCenaV() ", e);
        }
    }

    /**
     * Возвращает цену в валюте;
     *
     * @param sar
     * @param nar
     * @param fas
     * @param srt
     * @param minrst
     * @param maxrst
     * @param minrzm
     * @param maxrzm
     * @param cno
     * @param nds
     * @return cnp
     * @throws Exception
     */
    public double getCenaV(int sar, String nar, int fas, int srt, int minrst, int maxrst,
                           int minrzm, int maxrzm, double cno, int nds) throws Exception {

        Double cena = new Double(0);
        String sql = "Select cnp "
                + " From nsi_kld, nsi_sd "
                + "         Where nsi_kld.kod=nsi_sd.kod and "
                + "             sar= ? and "
                + "             nar= ? and "
                + "             fas= ? and "
                + "             srt= ? and "
                + "             (rst between ? and ?) and "
                + "             (rzm between ? and ?) and "
                + "             cno= ? and "
                + "             nds= ? ";

        try (PreparedStatement ps = getConnection().prepareStatement(sql)) {
            ps.setInt(1, sar);
            ps.setString(2, nar);
            ps.setInt(3, fas);
            ps.setInt(4, srt);
            ps.setInt(5, minrst);
            ps.setInt(6, maxrst);
            ps.setInt(7, minrzm);
            ps.setInt(8, maxrzm);
            ps.setDouble(9, cno);
            ps.setInt(10, nds);
            ResultSet rs = ps.executeQuery();
            if (rs.next())
                cena = rs.getDouble("cnp");
        } catch (Exception e) {
            log.severe("Ошибка getCenaV() " + e.getMessage());
            throw new Exception("Ошибка getCenaV() ", e);
        }
        return cena;
    }
}