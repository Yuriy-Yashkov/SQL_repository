package workDB;

import by.gomel.freedev.ucframework.ucdao.jdbc.AbstractMSSQLServerJDBC;
import by.march8.entities.warehouse.SaleDocumentDetailItemReport;
import common.UtilFunctions;
import dept.MyReportsModule;
import dept.sbit.reports.InvoicesUtils;
import dept.sbit.reports.model.CustomerInfoBean;
import dept.sbit.reports.model.PropInvoiceBean;
import dept.sklad.model.ResultTTN;

import javax.swing.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Types;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.logging.Logger;

import static common.UtilFunctions.convertDateStrToLong;

public class DB extends AbstractMSSQLServerJDBC {

    private static final Logger log = Logger.getLogger(DB.class.getName());

    public long kol = 0;
    public long kolForSdachNaSklad = 0;
    public long kolForSdachaNaUpack = 0;
    public long mkol = 0;
    public long sum = 0;
    public long sumForSdachaNaUpack = 0;
    public long msum = 0;

/* *****************************************************************************
-----------------------Экономисты запуск выпуск бригат пошива   --------------
******************************************************************************/

    public Vector zapuskPoshivaPoBrigadam(String sd, String ed, int brig) throws ParseException {
        Vector items = new Vector();
        kol = 0;

        long s = convertDateStrToLong(sd);
        long e = convertDateStrToLong(ed);

        String query = "select nomer, fas, ngpr, sum(kol) as kol from (select kod_marh, kod_izd, kol from prix_osn where brig_pol = ? and data >= ? and data < ?)as t1 " +
                " join (select nomer, kod from marh_list)as ml on ml.kod = t1.kod_marh " +
                " join (select fas, ngpr, kod, kkr from nsi_kld) as nsikld on t1.kod_izd = nsikld.kod group by fas, nomer, ngpr order by nomer";

        try (PreparedStatement ps = getConnection().prepareStatement(query)) {
            ps.setLong(1, brig);
            ps.setDate(2, new java.sql.Date(s));
            ps.setDate(3, new java.sql.Date(e + DAY));

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Vector vv = new Vector();
                vv.add(rs.getString(1));
                vv.add(rs.getString(2));
                vv.add(rs.getString(3).trim());
                vv.add(rs.getLong(4));
                kol += rs.getLong(4);
                items.add(vv);
            }
        } catch (Exception er) {
            log.severe("Ошибка создания списка zapuskPoshivaPoBrigadam " + er.getMessage());
        }
        return items;
    }

    public Vector vipuskPoshivaPoBrigadam(String sd, String ed, int brig) throws ParseException {
        Vector items = new Vector();
        kol = 0;

        long s = convertDateStrToLong(sd);
        long e = convertDateStrToLong(ed);

        String query = "select nomer, fas, ngpr, sum(kol*kkr) from (select kod_marh, kod_izd from poshiv where brigada = ? group by kod_marh, kod_izd) as t1 " +
                " join ( select kod_marh, sum(kol)as kol from nakl_sdacha where data >= ? and data < ? group by kod_marh)as t2 on t1.kod_marh = t2.kod_marh " +
                " join (select nomer, kod from marh_list)as ml on ml.kod = t1.kod_marh " +
                " join (select fas, ngpr, kod, kkr from nsi_kld) as nsikld on t1.kod_izd = nsikld.kod group by fas, nomer, ngpr order by nomer";

        try (PreparedStatement ps = getConnection().prepareStatement(query)) {
            ps.setLong(1, brig);
            ps.setDate(2, new java.sql.Date(s));
            ps.setDate(3, new java.sql.Date(e + DAY));

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Vector vv = new Vector();
                vv.add(rs.getString(1));
                vv.add(rs.getString(2));
                vv.add(rs.getString(3).trim());
                vv.add(rs.getLong(4));
                kol += rs.getLong(4);
                items.add(vv);
            }
        } catch (Exception er) {
            log.severe("Ошибка создания списка vipuskPoshivaPoBrigadam " + er.getMessage());
        }
        return items;
    }

/******************************************************************************
 -----------------------Экономисты отчёт по закройношвейному цеху--------------
 ******************************************************************************/
    /**
     * Расчёт закроеных изделий за период с sDate по eDate для бригады brigada
     *
     * @param sDate   Дата начало периода
     * @param eDate   Дата окончания периода (не включается в период)
     * @param brigada Номер бригады закроя
     * @return количество закроеных изделий за период с sDate по eDate бригадой brigada
     */
    public long zakroyBrig(long sDate, long eDate, int brigada) {
        long rez = 0;

        String queryString = "select sum(kol) from prix_osn where brig_pol = ? and data >= ? and data < ?";
        try (PreparedStatement ps = getConnection().prepareStatement(queryString)) {
            ps.setInt(1, brigada);
            ps.setDate(2, new java.sql.Date(sDate));
            ps.setDate(3, new java.sql.Date(eDate + DAY));//new java.sql.Date((int)(eDate.getYear()), (int)eDate.getMonth(), (int)(eDate.getDay())));
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                rez = rs.getLong(1);
            }

        } catch (Exception e) {
            log.severe("Error in zakroyBrig function: " + e.getMessage());
        }
        return rez;
    }

    /**
     * Расчёт пошито изделий за период с sDate по eDate для бригады brigada
     *
     * @param sDate   Дата начало периода
     * @param eDate   Дата окончания периода (не включается в период)
     * @param brigada Номер бригады закроя
     * @return количесво изделий пошитых пригадой за период времени
     */
    public long poshivBrig(long sDate, long eDate, int brigada) {
        long rez = 0;

        String queryString = "select sum(t2.kol*kkr) from (select kod_marh, kod_izd from poshiv where brigada = ? group by kod_marh, kod_izd) as t1 join ( select kod_marh, sum(kol)as kol from nakl_sdacha where data >= ? and data < ? group by kod_marh)as t2 on t1.kod_marh = t2.kod_marh " +
                " join (select kod, kkr from nsi_kld) as nsikld on t1.kod_izd = nsikld.kod";
        try (PreparedStatement ps = getConnection().prepareStatement(queryString)) {
            ps.setInt(1, brigada);
            ps.setDate(2, new java.sql.Date(sDate));
            ps.setDate(3, new java.sql.Date(eDate + DAY));
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                rez = rs.getLong(1);
            }
        } catch (Exception e) {
            log.severe("Error in zakroyDay function: " + e.getMessage());
        }
        return rez;
    }

    /**
     * Расчёт запуска изделий за период с sDate по eDate для бригады brigada
     *
     * @param sDate   Дата начало периода
     * @param eDate   Дата окончания периода (не включается в период)
     * @param brigada Номер бригады закроя
     * @return количесво изделий за период с sDate по eDate для бригады brigada
     */
    public long zapuskBrig(long sDate, long eDate, int brigada) {
        long rez = 0;

        String queryString = "select sum(kol) from prix_osn where brig_pol = ? and data >= ? and data < ?;";
        try (PreparedStatement ps = getConnection().prepareStatement(queryString)) {
            ps.setInt(1, brigada);
            ps.setDate(2, new java.sql.Date(sDate));
            ps.setDate(3, new java.sql.Date(eDate + DAY));
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                rez = rs.getLong(1);
            }
        } catch (Exception e) {
            log.severe("Error in zapuskPoshivBrig function: " + e.getMessage());
        }
        return rez;
    }

    /**
     * Расчёт пошито изделий за период с sDate по eDate для бригады brigada
     *
     * @param Date    Дата на которую необходимо узнать остатки
     * @param brigada Номер бригады закроя
     * @return остатки для бригады brigada
     */
    public long ostatki(long Date, int brigada) {
        long rez = 0;
        long prix = 0;
        long posh = 0;

        String queryString = "select sum(kol) from prix_osn where data >= '01.06.2009' and data < ? and brig_pol = ?;";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(queryString)) {
            ps.setInt(2, brigada);
            ps.setDate(1, new java.sql.Date(Date));
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                prix = rs.getLong(1);
            }
            queryString = "select sum(kol_sdano) from poshiv where data >= '01.06.2009' and data < ? and brigada = ?;";
            PreparedStatement fps = conn.prepareStatement(queryString);
            fps.setInt(2, brigada);
            fps.setDate(1, new java.sql.Date(Date));
            ResultSet frs = fps.executeQuery();
            if (frs.next()) {
                posh = frs.getLong(1);
            }

        } catch (Exception e) {
            log.severe("Error in ostatki function: " + e.getMessage());
        }
        rez = prix - posh;
        return rez;
    }

//    public long ostatkiKroy(long Date, int brigada) {
//        long rez = 0;
//        long prix = 0;
//        long posh = 0;
//
//        String queryString = "select sum(kol) from prix_osn where data >= '01.01.2009' and data < ? and brig_pol = ?;";
//        try (Connection conn = getConnection();
//             PreparedStatement ps = conn.prepareStatement(queryString)) {
//            ps.setInt(2, brigada);
//            ps.setDate(1, new java.sql.Date(Date));
//            ResultSet rs = ps.executeQuery();
//            if (rs.next()) {
//                prix = rs.getLong(1);
//            }
//            queryString = "select sum(kol) from rasx_osn where data >= '01.01.2009' and data < ? and brig_otpr = ?;";
//            PreparedStatement fps = conn.prepareStatement(queryString);
//            fps.setInt(2, brigada);
//            fps.setDate(1, new java.sql.Date(Date));
//            ResultSet frs = fps.executeQuery();
//            if (frs.next()) {
//                posh = frs.getLong(1);
//            }
//
//        } catch (Exception e) {
//            log.severe("Error in ostatki function: " + e.getMessage());
//        }
//        rez = prix - posh;
//        return rez;
//    }

    /**
     * Расчёт пошито изделий за период с sDate по eDate
     *
     * @param sDate Дата начало периода
     * @param eDate Дата окончания периода (не включается в период)
     * @return long количество изделий
     */
    public long poshivVerh(long sDate, long eDate) {
        long rez = 0;
        long kol = 0;
        long sar = 0;

        String queryString = "select (t1.kol*nsikld.kkr), nsikld.sar from (select kol, srt, kod_izd from v_nakl_sdacha1 where srt < 3 and data >= ? and data < ?) as t1 join (select kkr, sar, kod from nsi_kld) as nsikld on t1.kod_izd = nsikld.kod;";
        try (PreparedStatement ps = getConnection().prepareStatement(queryString)) {
            ps.setDate(1, new java.sql.Date(sDate));
            ps.setDate(2, new java.sql.Date(eDate + DAY));
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                kol = rs.getLong(1);
                sar = rs.getLong(2);
                sar /= 1000000;
                sar %= 10;
                if (sar == 2) {
                    rez += kol;
                }
            }

        } catch (Exception e) {
            log.severe("Error in poshivVerh function: " + e.getMessage());
        }
        return rez;
    }

    /**
     * Расчёт запуска изделий за период с sDate по eDate (верх)
     *
     * @param sDate Дата начало периода
     * @param eDate Дата окончания периода (не включается в период)
     * @return long количество изделий
     */
    public long zapuskVerh(long sDate, long eDate) {
        long rez = 0;
        long kol = 0;
        long sar = 0;

        String queryString = "select t1.kol, t2.sar from (select kol, kod_izd from prix_osn where data >= ? and data < ? and (brig_pol between 101 and 108 or brig_pol between 121 and 128))as t1" +
                " join (select sar, kod from nsi_kld)as t2 on t1.kod_izd = t2.kod ";
        try (PreparedStatement ps = getConnection().prepareStatement(queryString)) {
            ps.setDate(1, new java.sql.Date(sDate));
            ps.setDate(2, new java.sql.Date(eDate + DAY));
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                kol = rs.getLong(1);
                sar = rs.getLong(2);
                sar /= 1000000;
                sar %= 10;
                if (sar == 2) {
                    rez += kol;
                }
            }
        } catch (Exception e) {
            log.severe("Error in zapuskVerh function: " + e.getMessage());
        }
        return rez;
    }

//    public long ostatkiVerh(long sDate, long eDate, ArrayList al) {
//        long rez = 0;
//        long kr = 0;
//        long sar = 0;
//        long pr1 = 0;
//
//        Object[] a = al.toArray();
//
//        String queryString = "select kol,kroy from marh_list where kod in (select t1.kod from (select max(data)as mdata, kod from prot_marh group by kod) t1 inner join (select data, kod from prot_marh where pol = ? and block <>1) t2 on t1.kod = t2.kod where t1.mdata = t2.data and mdata < ?);";
//        try (Connection conn = getConnection();
//             PreparedStatement ps = conn.prepareStatement(queryString)) {
//            for (int i = 0; i < 16; i++) {
//                int brigada = Integer.parseInt(a[i].toString());
//                ps.setInt(1, brigada);
//                ps.setDate(2, new java.sql.Date(sDate));
//                ResultSet rs = ps.executeQuery();
//                while (rs.next()) {
//                    pr1 = rs.getLong(1);
//                    kr = rs.getLong(2);
//
//                    PreparedStatement ps2 = conn.prepareStatement("select sar from nsi_kld where fas = (select fas from kroy where kod = ?)");
//                    ps2.setLong(1, kr);
//                    ResultSet rs2 = ps2.executeQuery();
//                    if (rs2.next()) {
//                        sar = rs2.getLong(1);
//                        sar /= 1000000;
//                        sar %= 10;
//                        if (sar == 2) {
//                            rez += pr1;
//                        }
//                    }
//                    ps2.close();
//                    rs2.close();
//                }
//            }
//        } catch (Exception e) {
//            log.severe("Error in ostatkiVerh function: " + e.getMessage());
//        }
//        return rez;
//    }

    public long zakroiVerh(long sDate, long eDate) {
        long rez = 0;
        long kol = 0;
        long sar = 0;

        String queryString = " select t1.kol, t2.sar from (select kol, kod_izd from prix_osn where data >= ? and data < ? and (brig_pol between 152 and 167))as t1 " +
                "join (select sar, kod from nsi_kld)as t2 on t1.kod_izd = t2.kod";

        try (PreparedStatement ps = getConnection().prepareStatement(queryString)) {
            ps.setDate(1, new java.sql.Date(sDate));
            ps.setDate(2, new java.sql.Date(eDate + DAY));
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                kol = rs.getLong(1);
                sar = rs.getLong(2);
                sar /= 1000000;
                sar %= 10;
                if (sar == 2) {
                    rez += kol;
                }
            }

        } catch (Exception e) {
            log.severe("Error in zakroyDay function: " + e.getMessage());
        }
        return rez;
    }

    /**
     * Расчитывает стоимость пошитых изделий за период времение
     *
     * @param sDate начало периода
     * @param eDate конец периода
     * @param f     флаг определяющий учитывать ли сорт изделий
     * @return стоимость
     */
//    public float sumPoshiv(long sDate, long eDate, boolean f) {
//        float rez = 0;
//        float cn = 0;
//        long kol = 0;
//        String queryString = new String();
//
//        if (f) {
//            queryString = "select  round(cno*(1+nds/100),2),(t1.kol_sdano*nsikld.kkr)as kol_sdano from " +
//                    " (select kod_izd,rzm,rst,srt,kol_sdano from v_poshiv where  srt < 3 and (brigada between 101 and 108 or brigada between 121 and 128) and data >= ? and data < ?) as t1 " +
//                    " join nsi_kld as nsikld on t1.kod_izd = nsikld.kod " +
//                    " join (select kod,rzm,rst,srt,cno,nds from nsi_sd)t2 " +
//                    " on t1.kod_izd = t2.kod where t1.rzm = t2.rzm and t1.rst = t2.rst and t1.srt = t2.srt";
//        } else queryString = "select  round(cno*(1+nds/100),2),(t1.kol_sdano*nsikld.kkr)as kol_sdano from " +
//                " (select kod_izd,rzm,rst,srt,kol_sdano from v_poshiv where  srt < 3 and (brigada between 101 and 108 or brigada between 121 and 128) and data >= ? and data < ?) as t1 " +
//                " join nsi_kld as nsikld on t1.kod_izd = nsikld.kod " +
//                " join (select kod,rzm,rst,srt,cno,nds from nsi_sd where srt = 1)t2 " +
//                " on t1.kod_izd = t2.kod where t1.rzm = t2.rzm and t1.rst = t2.rst";
//        try (PreparedStatement ps = getConnection().prepareStatement(queryString)) {
//            ps.setDate(1, new java.sql.Date(sDate));
//            ps.setDate(2, new java.sql.Date(eDate + DAY));
//            ResultSet rs = ps.executeQuery();
//            while (rs.next()) {
//                cn = rs.getFloat(1);
//                kol = rs.getLong(2);
//                rez += (kol * cn);
//            }
//
//        } catch (Exception e) {
//            log.severe("Error in zapuskPoshivBrig function: " + e.getMessage());
//        }
//        //rez /= 1000000;
//        BigDecimal bd = new BigDecimal(String.valueOf(rez));
//        bd.setScale(3, BigDecimal.ROUND_HALF_UP).setScale(2, BigDecimal.ROUND_HALF_UP);
//        return bd.floatValue();
//    }
//
//    // ToDo запрос на перетягивание EAN кодов из таблицы NSI_EANCODE в nsi_sd,
//    //  а так же удаление дубликатов. Надо разделить и причесать.
//    public void eanTransfer() {
//        String sql = new String();
//        Map<String, String> map = new HashMap<String, String>();
//        String id;
//        String ean;
//        sql = "SELECT [ean],[kod1],[ITEM_CODE],[EANCODE]  FROM [dbo].[nsi_sd]" +
//                "JOIN [dbo].[NSI_EANCODE]" +
//                "ON [dbo].[nsi_sd].[kod1] = [dbo].[NSI_EANCODE].[ITEM_CODE]" +
//                "WHERE [ean] LIKE '' and [EANCODE] !=''" +
//                "ORDER BY [kod1]";
//        try {
//            ps = conn.prepareStatement(sql);
//            rs = ps.executeQuery();
//            while (rs.next()) {
//                id = rs.getString(2);
//                ean = rs.getString(4);
//                map.put(id, ean);
//            }
//        } catch (SQLException e) {
//            System.out.println("Error from eanTransfer");
//            throw new RuntimeException(e);
//        }
//
//        System.out.println(map);
//        sql = "UPDATE [dbo].[nsi_sd] SET [ean]=? WHERE [kod1]=?";
//        String del;
//        long counter = 0;
//        for (Map.Entry<String, String> item : map.entrySet()) {
//            System.out.println("id=" + Integer.valueOf(item.getKey()) + "|\nean=" + item.getValue() + "|");
//            try {
//                ps = conn.prepareStatement(sql);
//                ps.setString(1, item.getValue());
//                ps.setInt(2, Integer.valueOf(item.getKey()));
//                ps.executeUpdate();
//                del = "DELETE FROM [dbo].[NSI_EANCODE]" +
//                        "WHERE " +
//                        "[EANCODE] IN (SELECT [EANCODE] FROM [dbo].[NSI_EANCODE] GROUP BY [EANCODE] HAVING COUNT(*) > 1) " +
//                        "AND [EANCODE] = '" + item.getValue() + "'" +
//                        "AND [REF_COLOR_ID] = 2";
//                ps = conn.prepareStatement(del);
//                //ps.setString(1, item.getValue());
//                ps.executeUpdate();
//                commit();
//            } catch (SQLException e) {
//                System.out.println("Error from eanTransfer");
//                throw new RuntimeException(e);
//            }
//            long a = map.size();
//            System.out.println("Сделанно " + (counter + 1) + " из " + map.size() + ".");
//            ++counter;
//        }
//    }

    /**
     * Удаляет дубликаты EAN кодов в таблице, с цветом 2
     */
    public void del_dublecate_EAN() {
        String sql = " DELETE FROM [dbo].[NSI_EANCODE] WHERE [REF_COLOR_ID] = 2 AND EXISTS ( SELECT 1 FROM [dbo].[NSI_EANCODE] ean2 WHERE ean2.[EANCODE] = [NSI_EANCODE].[EANCODE] GROUP BY ean2.[EANCODE] HAVING COUNT(*) > 1 ); ";

        try (PreparedStatement ps = getConnection().prepareStatement(sql)) {
            int rowsAffected = ps.executeUpdate();
            log.info("Удалено {} строк с дубликатами EAN кодов и цветом 2" + rowsAffected);
        } catch (Exception e) {
            log.severe("Ошибка при удалении дубликатов EAN кодов" + e.getMessage());
        }
    }

    public float sumZapusk(long sDate, long eDate) {
        float rez = 0;

        String queryString = "select round(sum(cno*kol/t3.kkr),2) from (select kod_izd,rzm,rst,kol from prix_osn where (brig_pol between 101 and 108 or brig_pol between 121 and 128) and data >= ? and data < ?)t1 " +
                " join (select kod, kkr from nsi_kld)as t3 on t3.kod = t1.kod_izd " +
                " inner join (select kod,rzm,rst,srt,cno,nds from nsi_sd where srt = 1)t2 on t1.kod_izd = t2.kod where t1.rzm = t2.rzm and t1.rst = t2.rst";
        try (PreparedStatement ps = getConnection().prepareStatement(queryString)) {
            ps.setDate(1, new java.sql.Date(sDate));
            ps.setDate(2, new java.sql.Date(eDate + DAY));
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                rez = rs.getFloat(1);
            }

        } catch (Exception e) {
            log.severe("Error in zapuskPoshiv function: " + e.getMessage());
        }
        BigDecimal bd = new BigDecimal(String.valueOf(rez));
        bd.setScale(10, RoundingMode.HALF_UP).setScale(2, RoundingMode.HALF_UP);
        return bd.floatValue();
    }

    public float sumZakr(long sDate, long eDate) {
        float rez = 0;
        String query = "select round(sum(t2.cno * t1.kol/ t3.kkr),2) from (select kod_izd,rzm,rst,kol from prix_osn where data >= ? and data < ? and (brig_pol = 152 or brig_pol = 153 or brig_pol = 154 or brig_pol = 155 or brig_pol = 164 or brig_pol = 166 or brig_pol = 167))t1 " +
                " join (select kod, kkr from nsi_kld)as t3 on t3.kod = t1.kod_izd " +
                " inner join (select kod,rzm,rst,srt,cno, nds from nsi_sd where srt = 1)t2 on t1.kod_izd = t2.kod where t1.rzm = t2.rzm and t1.rst = t2.rst";
        try (PreparedStatement ps = getConnection().prepareStatement(query)) {
            ps.setDate(1, new java.sql.Date(sDate));
            ps.setDate(2, new java.sql.Date(eDate + DAY));
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                rez = rs.getFloat(1);
            }
        } catch (Exception e) {
            log.severe("Ошибка функции sumZakr " + e.getMessage());
        }
        BigDecimal bd = new BigDecimal(String.valueOf(rez));
        bd.setScale(10, RoundingMode.HALF_UP).setScale(2, RoundingMode.HALF_UP);
        return bd.floatValue();
    }

    /**
     * Количество едениц принятых складом от упаковки
     *
     * @param sDate начало периода
     * @param eDate конец периода
     * @param n     номер ячейки с которой надо учитывать продукцию
     * @param sort3 учитывать только продукцию 3-го сорта
     * @return кол-во едениц
     */
    public long upacovSdano(long sDate, long eDate, int n, boolean sort3) {
        long rez = 0;
        String queryString;

        if (sort3) {
            queryString = "select sum(t1.kol*t1.kol_in_upack*t3.kkr) from (select kol,kol_in_upack, kod_izd from vnperem2 where srt = 3 and doc_id in (select item_id from vnperem1 where status = 0 and kpodot = 138 and kpodto = 737 and date >= ? and date < ? and ndoc >= ?))as t1 " +
                    " join (select kod, kod1 from nsi_sd)as t2 on t1.kod_izd = t2.kod1 " +
                    " join (select kkr, kod from nsi_kld)as t3 on t2.kod = t3.kod";
        } else
            queryString = "select sum(t1.kol*t1.kol_in_upack*t3.kkr) from (select kol,kol_in_upack, kod_izd from vnperem2 where srt < 3 and kod_marh <> 0 and doc_id in (select item_id from vnperem1 where status = 0 and kpodot = 138 and kpodto = 737 and date >= ? and date < ? and ndoc >= ?))as t1 " +
                    " join (select kod, kod1 from nsi_sd)as t2 on t1.kod_izd = t2.kod1 " +
                    " join (select kkr, kod from nsi_kld)as t3 on t2.kod = t3.kod";
        try (PreparedStatement ps = getConnection().prepareStatement(queryString)) {
            ps.setDate(1, new java.sql.Date(sDate));
            ps.setDate(2, new java.sql.Date(eDate + DAY));
            ps.setInt(3, n);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                rez += rs.getLong(1);
            }

        } catch (Exception e) {
            log.severe("Error in upacovSdano function: " + e.getMessage());
        }
        return rez;
    }

    public long sdanoNaUpacov(long sDate, long eDate) {
        long rez = 0;

        String queryString = "select kol from v_nakl_sdacha1 where srt < 3 and data >= ? and data < ?";
        try (PreparedStatement ps = getConnection().prepareStatement(queryString)) {
            ps.setDate(1, new java.sql.Date(sDate));
            ps.setDate(2, new java.sql.Date(eDate + DAY));
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                rez += rs.getLong(1);
            }

        } catch (Exception e) {
            log.severe("Error in upacovSdano function: " + e.getMessage());
        }
        return rez;
    }

    /**
     * Стоимость изделий принятых складом от упаковки
     *
     * @param sDate начало периода
     * @param eDate конец периода
     * @param n     номер начальной ячейки
     * @param sort3 булеан -- только третий сорт(тру) или все(фалсе)
     * @return стоимость
     */
    public float sumUpacovSdano(long sDate, long eDate, int n, boolean sort3) {
        float rez = 0;
        String queryString;

        if (sort3) {
            queryString = "select round(sum(t1.kol*t1.cena),2) from (select cena, (kol*kol_in_upack)as kol, srt, kod_izd from vnperem2 where srt = 3 and doc_id in (select item_id from vnperem1 where status = 0 and kpodot = 138 and kpodto = 737 and date >= ? and date < ? and ndoc >= ?))as t1 " +
                    " join (select nds, kod, kod1 from nsi_sd)as t2 on t1.kod_izd = t2.kod1";
        } else
            queryString = "select round(sum(t1.kol*t1.cena),2) from (select cena, (kol*kol_in_upack)as kol, srt, kod_izd from vnperem2 where srt < 3 and kod_marh <> 0 and doc_id in (select item_id from vnperem1 where status = 0 and kpodot = 138 and kpodto = 737 and date >= ? and date < ? and ndoc >= ?))as t1 " +
                    " join (select nds, kod, kod1 from nsi_sd)as t2 on t1.kod_izd = t2.kod1";
        try (PreparedStatement ps = getConnection().prepareStatement(queryString)) {
            ps.setDate(1, new java.sql.Date(sDate));
            ps.setDate(2, new java.sql.Date(eDate + DAY));
            ps.setInt(3, n);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                rez += rs.getFloat(1);
            }

        } catch (Exception e) {
            log.severe("Error in upacovSdano function: " + e.getMessage());
        }
        BigDecimal bd = new BigDecimal(String.valueOf(rez));
        bd.setScale(10, RoundingMode.HALF_UP).setScale(2, RoundingMode.HALF_UP);
        return bd.floatValue();
    }

    public float sumUpacovPrin(long sDate, long eDate, boolean sort) {
        float rez = 0;

        String queryString;
        if (sort) {
            queryString = "select  round(sum(t1.kol * t2.cno),2) from (select kol, fas, nar,  kod_izd, rzm, srt from v_nakl_sdacha1 where srt < 3 and data >= ? and data < ?) as t1 " +
                    " join (select cno, nds, kod, rzm_print, srt from nsi_sd)as t2 on t1.kod_izd = t2.kod where t1.srt = t2.srt and t1.rzm = t2.rzm_print  group by t1.fas, t1.nar, t2.cno,t2.nds order by t1.fas ";
        } else
            queryString = "select  round(sum(t1.kol * t2.cno),2) from (select kol, fas, nar,  kod_izd, rzm, srt from v_nakl_sdacha1 where srt < 3 and data >= ? and data < ?) as t1 " +
                    " join (select cno, nds, kod, rzm_print, srt from nsi_sd where srt = 1)as t2 on t1.kod_izd = t2.kod where t1.rzm = t2.rzm_print  group by t1.fas, t1.nar, t2.cno,t2.nds order by t1.fas ";
        try (PreparedStatement ps = getConnection().prepareStatement(queryString)) {
            ps.setDate(1, new java.sql.Date(sDate));
            ps.setDate(2, new java.sql.Date(eDate + DAY));
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                rez += rs.getFloat(1);
            }

        } catch (Exception e) {
            log.severe("Error in upacovSdano function: " + e.getMessage());
        }
        BigDecimal bd = new BigDecimal(String.valueOf(rez));
        bd.setScale(10, RoundingMode.HALF_UP).setScale(2, RoundingMode.HALF_UP);
        return bd.floatValue();
    }

    //----------------------------СКЛАД------------------------
//    public void setOtgr(int dao, int kpl, int sk, long ttn, long nak, long sar, int sot, long fas, int rst, int rzm, int srt, int pach, float kol, float cno, long cnr, int kcv, int npt, String vcn, float crr, float ndb, float ndr, float psk) {
//        cnr = (long) (cnr * (1 - psk / 100));
//        String queryString = "insert into t_Otgr (DAO, KPL, SK, TTN, NAK, SAR, SOT, FAS, RST, RZM, SRT, PACH, KOL, CNO, CNR, KCV, NPT, VCN, CRR, NDB, NDR, PSK)" +
//                " values(" + dao + "," + kpl + "," + sk + "," + ttn + "," + nak + "," + sar + "," + sot + "," + fas + "," + rst + "," + rzm + "," + srt + "," + pach + "," + kol + "," + cno + "," + cnr + "," + kcv + "," + npt + "," + vcn + "," + crr + "," + ndb + "," + ndr + "," + psk + ")";
//        try (PreparedStatement ps = getConnection().prepareStatement(queryString)) {
//            ps.executeUpdate();
//
//        } catch (Exception e) {
//            log.severe("Ошибка при заполнении t_Otgr: " + e.getMessage());
//        }
//    }
//
//    public void clearOtgr() {
//        String queryString = "delete from t_Otgr";
//        try (PreparedStatement ps = getConnection().prepareStatement(queryString)) {
//            ps.executeUpdate();
//
//        } catch (Exception e) {
//            log.severe("Ошибка при заполнении t_Otgr: " + e.getMessage());
//        }
//    }

    /* ****************************************************************************
    -----------------------Сбыт отправка накладных---------------------------------
    ******************************************************************************/
    public Vector initTableAdresat() {
        Vector klients = new Vector();
        String query = "select KOD, NAIM from s_klient order by KOD";

        try (PreparedStatement ps = getConnection().prepareStatement(query)) {
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Vector klient = new Vector();
                klient.add(rs.getLong(1));
                klient.add(rs.getString(2));
                klients.add(klient);
            }

        } catch (Exception e) {
            log.severe("Ошибка инициализации таблицы адресатов. Фун-ция initTableAdresat() " + e.getMessage());
        }
        return klients;
    }

    /**
     * Получает список накладных с указанной даты до текучего числа
     *
     * @param date дата
     * @param id   идентификатор клиента
     * @return список накладных
     */
    public Vector getNakl(String date, int id) throws ParseException {
        Vector v = new Vector();
        long d = convertDateStrToLong(date);
        String query = "select date, ndoc, status, saved from otgruz1 where date >= ? and kpl = ? and status <> 1 ";
        try (PreparedStatement ps = getConnection().prepareStatement(query)) {
            ps.setDate(1, new java.sql.Date(d));
            ps.setLong(2, id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Vector vv = new Vector();

                vv.add(rs.getDate(1));
                vv.add(rs.getString(2).trim());

                vv.add(rs.getInt(3));
                vv.add(rs.getBoolean(4));

                v.add(vv);
            }
        } catch (Exception e) {
            log.severe("Ошибка создания списка электронных накладных " + e.getMessage());
        }
        return v;
    }

    public Vector getNakl(String date) throws ParseException {
        Vector v = new Vector();

        long d = convertDateStrToLong(date);
        String query = "select date, ndoc, operac, kpl, NAIM, summa, summa_nds, summa_all, status, saved from (select date, ndoc, operac, kpl, summa, summa_nds, summa_all, " +
                "status = case " +
                "when status = 0 then 'Закрыт' " +
                "when status = 1 then 'Удалён' " +
                "when status = 2 then 'Непонятно =)' " +
                "when status = 3 then 'Формируется' " +
                "end, saved " +
                " from otgruz1 where date >= ?) as t1 join (select KOD, NAIM from s_klient)  as t2 on t1.kpl = t2.KOD order by date";

        try (PreparedStatement ps = getConnection().prepareStatement(query);) {
            ps.setDate(1, new java.sql.Date(d));
            ResultSet rs = ps.executeQuery();
            PDB pdb = new PDB();
            while (rs.next()) {
                Vector vv = new Vector();
                vv.add(rs.getDate(1));// Дата
                vv.add(rs.getString(2).trim());//Номер
                vv.add(rs.getString(3).trim());//Тип документа
                vv.add(rs.getLong(4));// Код контрагента
                vv.add(rs.getString(5).trim());// Наименование клиента
                vv.add(String.format("%.2f", rs.getDouble(6)));// Сумма
                vv.add(String.format("%.2f", rs.getDouble(7)));//Сумма НДС
                vv.add(String.format("%.2f", rs.getDouble(8)));//Сумма Итого
                vv.add(rs.getString(9).trim());// Статус документа
                vv.add(pdb.getNDocOtgruz(rs.getString(2).trim()));
                vv.add(rs.getBoolean(10));// Статус расчета документа
                v.add(vv);
            }
            pdb.disConn();
        } catch (Exception e) {
            log.severe("Ошибка создания списка электронных накладных " + e.getMessage());
        }
        return v;
    }

    public Integer[] getNaklDescr(String nomer) {
        Integer count[] = new Integer[3];
        count[0] = 0;
        count[1] = 0;
        count[2] = 0;
        String query = "select kola, kolk, kolr from otgruz1 where ndoc = ?";
        try (PreparedStatement ps = getConnection().prepareStatement(query)) {
            ps.setString(1, nomer);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                count[0] = rs.getInt(1);
                count[1] = rs.getInt(2);
                count[2] = rs.getInt(3);
            }
        } catch (Exception e) {
            log.severe("Ошибка получения кол-ва изделий в накладной " + e.getMessage());
        }
        return count;
    }

//    public boolean haveTTNReport(String nomer) {
//        String query = "select id from _ttn_report where nomer = ?";
//        try (PreparedStatement ps = getConnection().prepareStatement(query);) {
//            ps.setString(1, nomer);
//            ResultSet rs = ps.executeQuery();
//            if (rs.next()) {
//                return true;
//            }
//        } catch (Exception e) {
//            log.severe("Ошибка haveTTNReport(String nomer) " + e.getMessage());
//        }
//        return false;
//    }
//
//    public boolean havePutListReport(String nomer) {
//        String query = "select id from _put_list_report where nomer = ?";
//        try (PreparedStatement ps = getConnection().prepareStatement(query)) {
//            ps.setString(1, nomer);
//            ResultSet rs = ps.executeQuery();
//            if (rs.next()) {
//                return true;
//            }
//        } catch (Exception e) {
//            log.severe("Ошибка havePutListReport(String nomer) " + e.getMessage());
//        }
//        return false;
//    }

    /**
     * Получает информацию о накладной по номеру
     *
     * @param nomer номер документа
     * @param flag  если 1 то получаем дополнительную инфу
     * @return вектор спецификации
     */
    public Vector getNaklAllDescr(String nomer, int flag) {
        Vector v = new Vector();
        String query = "select fas, nar, ngpr, rzm_print = case when rzm_print is null then '' else rzm_print end, srt, ncw, sum(kol), cena, sum(summa), nds, sum(summa_nds), sum(itogo), sar, rzm, kod_izd from " +
                " (select item_id from otgruz1 where ndoc = ?)as t " +
                " left join (select ncw, kol*kol_in_upack as kol, cena, summa, nds, summa_nds, kod_izd, itogo, doc_id from otgruz2 )as t1 on t.item_id = t1.doc_id" +
                " left join (select rzm_print, rst, rzm, srt, kod, kod1 from nsi_sd) as t3 on t1.kod_izd = t3.kod1" +
                " left join (select ngpr, fas, nar, sar, kod from nsi_kld) as t2 on t3.kod = t2.kod" +
                " group by fas, nar, ngpr, rzm_print, srt, ncw,nds,cena, sar, rzm, kod_izd order by fas, nar, ngpr, ncw, rzm_print, srt, nds,cena";
        try (PreparedStatement ps = getConnection().prepareStatement(query)) {
            ps.setString(1, nomer);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Vector vv = new Vector();
                vv.add(rs.getLong(1));
                vv.add(rs.getString(2).trim());
                vv.add(rs.getString(3).trim());
                vv.add(rs.getString(4).trim());
                vv.add(rs.getLong(5));
                vv.add(rs.getString(6).trim());
                vv.add(rs.getLong(7));
                vv.add(rs.getFloat(8));
                vv.add(rs.getFloat(9));
                vv.add(rs.getLong(10));
                vv.add(rs.getFloat(11));
                vv.add(rs.getFloat(12));
                if (flag == 1) {
                    vv.add(rs.getLong(13));
                    vv.add(rs.getLong(14));
                    vv.add(rs.getLong(15));
                }
                v.add(vv);
            }
        } catch (Exception e) {
            log.severe("Ошибка создания списка изделий в накладной " + e.getMessage());
        }
        return v;
    }

    public void setPriceNakl(String nomer, Vector rows) {
        Vector v;
        String query = "update otgruz2 set cena = ?, summa = ? * kol* kol_in_upack, summa_nds = ? * kol* kol_in_upack *nds/100, itogo = ? * kol* kol_in_upack *nds/100 + ? * kol* kol_in_upack where doc_id = (select item_id from otgruz1 where ndoc = ?) and kod_izd = ?";
        String query1 = "select sum(summa), sum(summa_nds), sum(itogo) from otgruz2 where doc_id = (select item_id from otgruz1 where ndoc = ?)";
        String query2 = "update otgruz1 set summa = ?, summa_nds = ?, summa_all = ? where ndoc = ?";
        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(query);
             PreparedStatement ps1 = con.prepareStatement(query1);
             PreparedStatement ps2 = con.prepareStatement(query2);) {
            con.setAutoCommit(false);
            for (Object row : rows) {
                v = (Vector) row;
                ps.setInt(1, (Integer) v.get(8));
                ps.setInt(2, (Integer) v.get(8));
                ps.setInt(3, (Integer) v.get(8));
                ps.setInt(4, (Integer) v.get(8));
                ps.setInt(5, (Integer) v.get(8));
                ps.setString(6, nomer);
                ps.setLong(7, (Long) v.get(9));
                ps.executeUpdate();
            }

            ps1.setString(1, nomer);
            ResultSet rs = ps1.executeQuery();
            float sum = 0;
            float sum_nds = 0;
            float itogo = 0;
            if (rs.next()) {
                sum = rs.getFloat(1);
                sum_nds = rs.getFloat(2);
                itogo = rs.getFloat(3);
            }

            ps2.setFloat(1, sum);
            ps2.setFloat(2, sum_nds);
            ps2.setFloat(3, itogo);
            ps2.setString(4, nomer);
            ps2.executeUpdate();
            con.commit();
        } catch (Exception e) {
            log.severe("Ошибка изменении стоимости изделий в накладной " + e.getMessage());
        }
    }

//    public void createFullNakl(String nn, String pathSave, boolean valuta, boolean createApp) {
//        String query;
//        float kurs = 1;
//        if (!valuta) query = " {? = call _nakladnie (? , 'full') }";
//        else query = " {? = call _nakladniev (? , 'full') }";
//        String dogovor = new String("");
//        String param;
//        DBF dbf = null;
//        if (pathSave.equals("".toString()) || pathSave == null) {
//            dbf = new DBF(1, nn, "");
//        } else dbf = new DBF(1, nn, pathSave);
//        List<Object[]> data = new ArrayList<>();
//
//        long n = -1;
//        ResultSet rs;
//        try {
//            //connect.setAutoCommit(false);
//            if (valuta) {
//                ps = conn.prepareStatement("SELECT kkrr from _put_list where ttn_id = (select top 1 item_id from otgruz1 where ndoc = ?)");
//                ps.setString(1, nn);
//                rs = ps.executeQuery();
//                if (rs.next()) kurs = rs.getFloat(1);
//            }
//            ps = conn.prepareStatement("select item_id from otgruz1 where ndoc = ?");
//            ps.setString(1, nn);
//            rs = ps.executeQuery();
//            rs.next();
//            n = rs.getLong(1);
//
//            ps = conn.prepareStatement("select DOGOVOR from ttn where DOC_ID = ?");
//            ps.setLong(1, n);
//            rs = ps.executeQuery();
//            if (rs.next()) {
//                dogovor = new String(rs.getString(1).trim());
//                if (dogovor.length() > 50) dogovor = dogovor.substring(0, 50);
//            }
//            //else dogovor = new String("");
//            if (dogovor.length() < 1) dogovor = "";
//            cs = conn.prepareCall(query);
//            cs.setLong(2, n);
//            cs.registerOutParameter(1, Types.OTHER);
//            rs = cs.executeQuery();
//
//            dbf.conn();
//
//            while (rs.next()) {
//                Object[] v = new Object[23];
//                v[0] = "ОАО \"8МАРТА\"";
//                v[1] = nn;
//                v[2] = rs.getObject("fas");
//                v[3] = rs.getString("nar").trim();
//                param = rs.getString("ngpr").toString().trim();
//                if (param.length() > 45) param = param.substring(0, 45);
//                v[4] = param;
//
//                v[5] = rs.getObject("rzm").toString().trim();
//                String str = rs.getObject("srt").toString().trim();
//                if (str.length() > 8) str = str.substring(0, 7);
//                v[6] = str;
//
//                param = rs.getString("ncw").toString().trim();
//                if (param.length() > 25) param = param.substring(0, 25);
//                v[7] = param;
//
//                v[8] = rs.getObject("kol_all");
//                v[9] = rs.getFloat("cena");
//                v[10] = rs.getFloat("summa");
//                v[11] = rs.getObject("nds");
//                v[12] = rs.getFloat("summa_nds");
//                v[13] = rs.getFloat("itogo");
//                v[14] = rs.getObject("eancode");
//                v[15] = "РБ";
//
//                param = rs.getString("sertifikat").toString().trim();
//                if (param.length() > 100) param = param.substring(0, 105);
//                v[16] = param;
//                param = rs.getString("ggr").toString().trim();
//                if (param.length() > 80) param = param.substring(0, 80);
//                v[17] = param;
//
//                v[18] = rs.getObject("narp");
//                v[19] = String.valueOf(rs.getObject("massa_ed"));
//                v[20] = String.valueOf(rs.getObject("massa"));
//                v[21] = dogovor;
//                param = rs.getString("preiscur").toString().trim();
//                if (param.length() > 50) param = param.substring(0, 50);
//                v[22] = param;
//                data.add(v);
//                dbf.write(v);
//            }
//            try {
//                if (createApp) {
//                    SaleOO saleOO = new SaleOO(data);
//                    saleOO.createReport("ПриложениеЭлНакл.ots", pathSave);
//                }
//            } catch (Exception exportError) {
//                System.out.println("Ошибка экспорта электронной накладной" + exportError);
//            }
//
//        } catch (Exception e) {
//            System.out.println(e);
//            return;
//        } finally {
//            if (dbf != null) dbf.disconn();
//        }
//    }
//
//    public void createCUMNakl(String nn, String pathSave) {
//        String query = " {? = call _nakladnie (?, 'cum') }";
//        String param;
//        DBF dbf = null;
//        if (pathSave.equals("".toString()) || pathSave == null) {
//            dbf = new DBF(2, nn, "");
//        } else dbf = new DBF(2, nn, pathSave);
//        long n = -1;
//        Date date = null;
//        float skidka = 0;
//
//        try {
//            ps = conn.prepareStatement("select item_id, date, skidka from otgruz1 where ndoc = ?");
//            ps.setString(1, nn);
//            rs = ps.executeQuery();
//            rs.next();
//            n = rs.getLong(1);
//            date = rs.getDate(2);
//            skidka = rs.getFloat(3);
//
//            cs = conn.prepareCall(query);
//            cs.setLong(2, n);
//            cs.registerOutParameter(1, Types.OTHER);
//            rs = cs.executeQuery();
//
//            dbf.conn();
//            while (rs.next()) {
//                Object[] v = new Object[21];
//                v[0] = nn;
//                v[1] = date;
//                v[2] = Long.parseLong(rs.getObject("eancode").toString());
//
//                param = rs.getString("ngpr").toString().trim();
//                if (param.length() > 30) param = param.substring(0, 30);
//                v[3] = param;
//
//                param = rs.getString("nar").toString().trim();
//                if (param.length() > 30) param = param.substring(0, 30);
//                v[4] = param;
//
//                param = rs.getString("fas").toString().trim();
//                if (param.length() > 15) param = param.substring(0, 15);
//                v[5] = param;
//
//                param = rs.getString("rzm").toString().trim();
//                if (param.length() > 15) param = param.substring(0, 15);
//                v[6] = param;
//
//                v[7] = "РБ";
//                v[8] = rs.getObject("srt").toString().trim().substring(0, 1);
//                v[9] = "шт   ";
//                v[10] = rs.getObject("kol_all");
//                v[11] = Float.parseFloat(rs.getObject("cena").toString());
//                v[12] = Float.parseFloat("0");
//                v[13] = Float.parseFloat("0");
//                v[14] = Float.parseFloat(((Float) skidka).toString());
//                v[15] = Float.parseFloat(rs.getObject("nds").toString());
//                v[16] = "";
//                v[17] = "";
//                param = rs.getString("sertifikat").toString().trim();
//                if (param.length() > 253) param = param.substring(0, 253);
//                v[18] = param;
//                v[19] = "";
//                v[20] = "";
//
//                dbf.write(v);
//            }
//
//        } catch (Exception e) {
//            System.out.println("------" + e);
//            return;
//        } finally {
//            if (dbf != null) dbf.disconn();
//        }
//    }

/* *****************************************************************************
-----------------------Плановый ------------------------------------------------
******************************************************************************/

    public Vector getEanList(Vector o) {
        Vector v = new Vector();
        String dropQuery = " DROP TABLE _plan";
        String selectQuery = " select  t2.ngpr, t2.fas, t2.nar, pl.art, pl.rzm, pl.rst, t1.rzm_print, t2.prim from " +
                " (select art, rzm, rst from _plan) as pl " +
                " left join (select srt, rzm, rst, kod, ean, rzm_print from nsi_sd where ean = '') as t1 on pl.rst = t1.rst and pl.rzm = t1.rzm " +
                " left join (select fas, sar, nar, ngpr, kod, prim from nsi_kld)as t2 on t1.kod = t2.kod and pl.art = t2.sar where fas is not null group by  t1.rzm_print, t2.fas, t2.ngpr, pl.art, t2.nar, pl.rzm, pl.rst, t2.prim order by t2.fas, t1.rzm_print";
        String createQuery = " CREATE TABLE _plan(art varchar(20), rzm int, rst int)";
        String insertQuery = "insert into _plan values(?, ?, ?)";
        Iterator it = o.iterator();
        try (PreparedStatement dps = getConnection().prepareStatement(dropQuery);
             PreparedStatement sps = getConnection().prepareStatement(selectQuery);
             PreparedStatement cps = getConnection().prepareStatement(createQuery);
             PreparedStatement ips = getConnection().prepareStatement(insertQuery);) {

            dps.executeUpdate();

            cps.executeUpdate();
            while (it.hasNext()) {
                String s1 = String.valueOf(it.next());
                String s2 = String.valueOf(it.next());
                String s3 = String.valueOf(it.next());

                ips.setString(1, s1);
                ips.setLong(2, Long.parseLong(s2));
                ips.setLong(3, Long.parseLong(s3));
                ips.executeUpdate();
            }

            ResultSet rs = sps.executeQuery();

            while (rs.next()) {
                if (rs.getString(1) != null) v.add(rs.getString(1));
                else v.add("");
                v.add(rs.getLong(2));
                if (rs.getString(3) != null) v.add(rs.getString(3));
                else v.add("");
                v.add(rs.getLong(4));
                //v.add(rs.getInt(5));
                //v.add(rs.getInt(6));
                if (rs.getString(8) != null) v.add(rs.getString(8).trim());
                else v.add("");
                if (rs.getString(7) != null) v.add(rs.getString(7));
                else v.add("");

            }

            dps.executeUpdate();
        } catch (Exception e) {
            log.severe("Ошибка получения списка изделий из дбф плана " + e.getMessage());
        }
        return v;
    }

    /* ****************************************************************************
     * ****************************Упаковка****************************************
     * ***************************************************************************/

    public List getLoseMarshList(String date) {
        List list = new ArrayList();
        String query = "select nomer, kod, kol from marh_list where kod in (select t1.kod_marh from (select kod_marh from vnperem2 where srt< 3 and doc_id in (select item_id from vnperem1 where status = 0 and kpodot = 138 and kpodto = 737 and date >= '01.11.2010') group by kod_marh) as t1 " +
                " left join (select kod_marh, sum(kol) as kol from nakl_sdacha where data >= '01.01.2010' group by kod_marh) as t2 on t1.kod_marh = t2.kod_marh where t2.kod_marh is null) order by nomer ";
        try (PreparedStatement ps = getConnection().prepareStatement(query)) {
            ResultSet rs = ps.executeQuery();
            int count = 1;
            while (rs.next()) {
                Vector m = new Vector();
                m.add(count++);
                m.add(rs.getString(1));
                m.add(rs.getString(2));
                m.add(rs.getInt(3));
                list.add(m);
            }
        } catch (Exception e) {
            log.severe("Ошибка получения невведённых маршрутов getLoseMershList " + e.getMessage());
        }
        return list;
    }

    /**
     * Сдача продукции на склад от упаковки
     *
     * @param sd начало периода
     * @param ed конец периода
     * @param n  номер начальной ячейки
     * @return Вектор элементов
     */
    public Vector sdachaNaSklad(String sd, String ed, int n) throws ParseException {
        long sDate = convertDateStrToLong(sd);
        long eDate = convertDateStrToLong(ed);
        Vector users = new Vector();
        int i = 0;
        kol = 0;
        sum = 0;

        String query = "select t3.fas, t3.nar, sum(t1.kol), t1.cena*(1+t2.nds/100) as cena, t1.srt  from (select cena, (kol*kol_in_upack)as kol, srt, kod_izd from vnperem2 where srt < 3 and kod_marh <> 0 and doc_id in (select item_id from vnperem1 where status = 0 and kpodot = 138 and kpodto = 737 and date >= ? and date < ? and ndoc >= ?))as t1 " +
                " join (select nds, kod, kod1 from nsi_sd)as t2 on t1.kod_izd = t2.kod1 " +
                " join (select nar, fas, kod from nsi_kld)as t3 on t2.kod = t3.kod group by t3.fas, t3.nar, t1.cena, t2.nds, t1.srt order by t3.fas";

        try (PreparedStatement ps = getConnection().prepareStatement(query);) {
            ps.setDate(1, new java.sql.Date(sDate));
            ps.setDate(2, new java.sql.Date(eDate + DAY));
            ps.setInt(3, n);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Vector user = new Vector();
                user.add(++i);
                user.add(rs.getString(1));
                user.add(rs.getString(2));
                user.add(rs.getLong(5));
                user.add(rs.getLong(3));
                user.add(rs.getLong(4));
                user.add(rs.getLong(3) * rs.getLong(4));
                kol += rs.getLong(3);
                sum += (rs.getLong(3) * rs.getLong(4));
                users.add(user);
            }
        } catch (Exception e) {
            log.severe("Ошибка полученя списка cдачи на склад " + e.getMessage());
        }
        return users;
    }

    public Vector sdachaNaSkladPoModel(String sd, String ed, int n) throws ParseException {
        long sDate = convertDateStrToLong(sd);
        long eDate = convertDateStrToLong(ed);
        Vector users = new Vector();
        int i = 0;
        kol = 0;
        sum = 0;

        String query = "select t3.fas, sum(t1.kol) from (select (kol*kol_in_upack)as kol, kod_izd from vnperem2 where srt < 3 and doc_id in (select item_id from vnperem1 where status = 0 and kpodot = 138 and kpodto = 737 and date >= ? and date < ? and ndoc >= ?))as t1 " +
                " join (select kod, kod1 from nsi_sd)as t2 on t1.kod_izd = t2.kod1 " +
                " join (select fas, kod from nsi_kld)as t3 on t2.kod = t3.kod group by t3.fas order by t3.fas";

        try (PreparedStatement ps = getConnection().prepareStatement(query);) {
            ps.setDate(1, new java.sql.Date(sDate));
            ps.setDate(2, new java.sql.Date(eDate + DAY));
            ps.setInt(3, n);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Vector user = new Vector();
                user.add(rs.getString(1));
                user.add(rs.getLong(2));
                users.add(user);
            }
        } catch (Exception e) {
            log.severe("Ошибка полуеня списка cдачи на склад " + e.getMessage());
        }
        return users;
    }

    public ArrayList sdachaNaSklad(String sd, String ed, int n, int model, String sar, int nar, String color, int size, int rst, String srt, HashMap<String, Boolean> flags, boolean f) throws ParseException {
        long sDate = convertDateStrToLong(sd);
        long eDate = convertDateStrToLong(ed);
        ArrayList users = new ArrayList();
        kol = 0;
        sum = 0;
        String chul = "";
        if (f) chul = " or kpodot = 501 ";

        String query = new String("select ngpr");

        if (flags.get("Модель")) {
            query += ",  fas";
        }
        if (flags.get("Артикул")) {
            query += ",  sar";
            query += ",  nar";
        }

        if (flags.get("Размер")) {
            query += ",  rst";
            query += ",  rzm";
        }
        if (flags.get("Сорт")) {
            query += ",  srt";
        }
        //query += ",  kod1";
        if (flags.get("Цвет")) {
            query += ",  color";
        }
        query += ",  sum(kol) as kol";
        query += ",  sum(kol)*kkr as kol1"; // add
        if (flags.get("Цена")) {
            query += ",  cena";
        }
        query += " from (select cena, (kol*kol_in_upack)as kol, srt, kod_izd, ncw as color from vnperem2 where doc_id in (select item_id from vnperem1 where status = 0 and (kpodot = 138 " + chul + ") and kpodto = 737 and date >= ? and date < ? and ndoc >= ?))as t1 " +  //kod_marh <> 0 and
                " join (select nds, kod, kod1, rst, rzm from nsi_sd)as t2 on t1.kod_izd = t2.kod1 " +
                " join (select ngpr, sar, nar, fas, kod, kkr from nsi_kld)as t3 on t2.kod = t3.kod ";

        if (flags.get("Артикул")) {
            if (nar != -1) query += " and sar = " + nar;
            if (!sar.equals("Все")) query += " and nar = '" + sar + "'";
        }
        if (flags.get("Модель") && model != -1) {
            query += " and fas = " + model;
        }
        if (flags.get("Размер")) {
            if (rst != -1) query += " and rst = " + rst;
            if (size != -1) query += "and rzm = " + size;
        }
        if (flags.get("Сорт")) {
            if (!srt.equals("Все")) query += " and srt in (" + srt + ")";
        }
        if (flags.get("Цвет")) {
            if (!color.equals("Все")) query += " and color = '" + color + "'";
        }
        query += " and kol > 0 ";

        //group by
        query += " group by ngpr";
        if (flags.get("Модель")) {
            query += ",  fas";
        }
        if (flags.get("Артикул")) {
            query += ",  sar";
            query += ",  nar";
        }

        if (flags.get("Размер")) {
            query += ",  rst";
            query += ",  rzm";
        }
        if (flags.get("Сорт")) {
            query += ",  srt";
        }
        //query += ",  kod1";
        if (flags.get("Цвет")) {
            query += ",  color";
        }
        if (flags.get("Цена")) {
            query += ",  cena";
        }
        query += ",  kkr";

        try (PreparedStatement ps = getConnection().prepareStatement(query)) {
            ps.setDate(1, new java.sql.Date(sDate));
            ps.setDate(2, new java.sql.Date(eDate + DAY));
            ps.setInt(3, n);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Vector v = new Vector();
                v.add(rs.getString("ngpr").trim());
                if (flags.get("Модель")) {
                    v.add(rs.getInt("fas"));
                }
                if (flags.get("Артикул")) {
                    v.add(rs.getString("nar").trim());
                    //v.add(rs.getInt("sar"));
                }
                if (flags.get("Размер")) {
                    v.add(rs.getInt("rst"));
                    v.add(rs.getInt("rzm"));
                }
                if (flags.get("Цвет")) {
                    v.add(rs.getString("color").trim());
                }
                if (flags.get("Сорт")) {
                    v.add(rs.getString("srt"));
                }
                kol += rs.getInt("kol");
                kolForSdachNaSklad += rs.getInt("kol1");
                v.add(rs.getInt("kol"));
                v.add(rs.getInt("kol1"));// add

                if (flags.get("Цена")) {
                    v.add(rs.getInt("cena"));
                    v.add(rs.getInt("cena") * rs.getInt("kol"));
                    sum += (rs.getInt("cena") * rs.getInt("kol"));
                }
                users.add(v);
            }
        } catch (Exception e) {
            log.severe("Ошибка получения списка cдачи на склад " + e.getMessage());
        }
        return users;
    }

    /**
     * Информация о изделиях принятых на упаковку за период
     *
     * @param sd начало периода
     * @param ed конец периода
     * @return Возвращает вектор описывающий изделия
     */
    public Vector sdachaNaUpack(String sd, String ed) throws ParseException {
        long sDate = convertDateStrToLong(sd);
        long eDate = convertDateStrToLong(ed);
        Vector users = new Vector();
        int i = 0;
        kol = 0;
        sum = 0;

        String query = "SELECT fas, nar, ngpr, SUM(kol) AS kol , SUM(kol) * kkr AS kol1, cno * (1 + nds / 100) AS EXPR1, srt " +
                "FROM (SELECT     t1.kol, t1.fas, t1.nar, t1.kod_izd, t1.rzm, t1.srt, t4.kkr, t4.cno, t4.nds, t4.ngpr " +
                "FROM v_nakl_sdacha1 t1 INNER JOIN " +
                "(SELECT     t2.cno, t2.nds, t2.kod, t2.rzm_print, t2.srt, t3.kkr, t3.ngpr " +
                "FROM          nsi_sd t2, nsi_kld t3 " +
                "WHERE      t2.kod = t3.kod) t4 " +
                "ON t1.kod_izd = t4.kod AND t1.srt = t4.srt AND t1.rzm = t4.rzm_print " +
                "WHERE      (t1.data > ? ) AND (t1.data < ? ) AND (t1.srt < 3)) DERIVEDTBL " +
                "GROUP BY fas, nar, ngpr, srt, cno, nds, kkr " +
                "ORDER BY fas";
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
                user.add(rs.getString(7).trim());
                user.add(rs.getLong(4));
                user.add(rs.getLong(5));
                user.add(rs.getLong(6));
                user.add(rs.getLong(4) * rs.getLong(6));

                kol += rs.getLong(4);
                kolForSdachaNaUpack += rs.getLong(5);
                sum += (rs.getLong(4) * rs.getLong(6));
                users.add(user);
            }
        } catch (Exception e) {
            log.severe("Ошибка получения списка cдачи на склад " + e.getMessage());
        }
        return users;
    }

    /**
     * Информация о изделиях по размерам принятых на упаковку за период
     *
     * @param sd  начало периода
     * @param ed  конец периода
     * @param fas модель
     * @param nar артикул
     * @param srt сорт
     * @return Возвращает вектор описывающий изделия
     */
    public Vector sdachaNaUpackRazm(String sd, String ed, String fas, String nar, String srt) throws ParseException {
        long sDate = convertDateStrToLong(sd);
        long eDate = convertDateStrToLong(ed);
        Vector items = new Vector();
        int i = 0;
        kol = 0;
        sum = 0;

        String query = "select t1.rzm, sum(t1.kol)as kol from (select kol, fas, nar,  kod_izd, rzm, srt from v_nakl_sdacha1 where srt = ? and data >= ? and data < ? and fas = ? and nar = ?) as t1 " +
                "group by t1.fas, t1.nar, t1.srt, t1.rzm order by t1.rzm ";
        try (PreparedStatement ps = getConnection().prepareStatement(query)) {
            ps.setString(1, srt);
            ps.setDate(2, new java.sql.Date(sDate));
            ps.setDate(3, new java.sql.Date(eDate + DAY));
            ps.setString(4, fas);
            ps.setString(5, nar);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Vector item = new Vector();
                item.add(++i);
                item.add(rs.getString(1));
                item.add(rs.getString(2));

                items.add(item);
            }
        } catch (Exception e) {
            log.severe("Ошибка полученя списка cдачи на склад по размерам " + e.getMessage());
        }
        return items;
    }

    public Vector ostUpack() {
        Vector users = new Vector();
        int i = 0;
        long q = 0;
        kol = 0;
        sum = 0;
        mkol = 0;
        msum = 0;

        String query = "select kld.fas, kld.nar, sum(ostup.kol) as kol, (nsisd.cno*(1+nsisd.nds/100))as cena, sum(abs(kol)*(nsisd.cno*(1+nsisd.nds/100))),nsisd.srt from (select kod_izd, sum(kol) as kol from _ost_upack group by kod_izd) as ostup join (select kod, kod1, cno, nds, srt from nsi_sd where srt < 3) as nsisd on ostup.kod_izd = nsisd.kod1 " +
                " join (select nar,fas, kod from nsi_kld) as kld on kld.kod = nsisd.kod group by kld.fas, kld.nar, (nsisd.cno*(1+nsisd.nds/100)), nsisd.srt order by kld.fas, kld.nar,nsisd.srt ";
        try (PreparedStatement ps = getConnection().prepareStatement(query)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                q = rs.getLong(3);
                if (q != 0) {
                    Vector user = new Vector();
                    user.add(++i);
                    user.add(rs.getString(1));
                    user.add(rs.getString(2));
                    user.add(rs.getLong(6));
                    user.add(q);
                    user.add(rs.getLong(4));
                    user.add(rs.getLong(5));
                    if (q > 0) {
                        kol += q;
                        sum += (rs.getLong(5));
                    } else if (q < 0) {
                        mkol += q;
                        msum += (rs.getLong(5));
                    }
                    users.add(user);
                }
            }
        } catch (Exception e) {
            log.severe("Ошибка полученя списка cдачи на склад " + e.getMessage());
        }
        return users;
    }

    //упаковка
    public List realOstUpack() {
        List items = new ArrayList();
        String query = "select kod_izd, color, kol from _ost_upack  where kol <> 0 order by kod_izd, color";
        try (PreparedStatement ps = getConnection().prepareStatement(query)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Vector v = new Vector();
                v.add(rs.getInt(1));
                v.add(rs.getString(2));
                v.add(rs.getInt(3));
                items.add(v);
            }
        } catch (Exception e) {
            log.severe("Ошибка realOstUpack() " + e.getMessage());
        }
        return items;
    }

    /**
     * возврачает изделя принятые упаковкой в период времени
     *
     * @param sDate -- начало периода
     * @param eDate -- конец периода
     * @return набор данных в виде: код изделия -- цвет -- кол-во
     */
    public List sdanoNaUpack(long sDate, long eDate) {
        List items = new ArrayList();
        String queryString = " select t2.kod1, t1.ncw, sum(t1.kol)as kol from (select ncw, kol, kod_izd, rzm, srt from v_nakl_sdacha1 where data >= ? and data < ?) as t1 " +
                " join (select kod1, kod, rzm_print, srt from nsi_sd)as t2 on t1.kod_izd = t2.kod and t1.srt = t2.srt and t1.rzm = t2.rzm_print  group by kod1, ncw order by kod1, ncw ";
        ;

        try (PreparedStatement ps = getConnection().prepareStatement(queryString)) {
            ps.setDate(1, new java.sql.Date(sDate));
            ps.setDate(2, new java.sql.Date(eDate + DAY));
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                items.add(rs.getInt(1));//код изделия
                items.add(rs.getString(2));//цвет изделия
                items.add(rs.getInt(3));//кол-во
            }

        } catch (Exception e) {
            log.severe("Ошибка при выполнении функции sdanoNaUpack" + e.getMessage());
        }
        return items;
    }

    /**
     * возврачает изделя принятые складом от упаковки и чулок  в период времени
     *
     * @param sDate -- начало периода
     * @param eDate -- конец периода
     * @return набор данных в виде: код изделия -- цвет -- кол-во
     */
    public List sdanoUpackSklad(long sDate, long eDate) {
        List items = new ArrayList();
        String queryString = "select t1.kod_izd, t1.ncw, sum(t1.kol)  from (select cena, (kol*kol_in_upack)as kol, srt, kod_izd, ncw from vnperem2 where doc_id in (select item_id from vnperem1 where status = 0 and kpodot = 138 and kpodto = 737 and date >= ? and date < ?))as t1  " +
                " group by t1.kod_izd, t1.ncw";

        try (PreparedStatement ps = getConnection().prepareStatement(queryString)) {
            ps.setDate(1, new java.sql.Date(sDate));
            ps.setDate(2, new java.sql.Date(eDate + DAY));
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                items.add(rs.getInt(1));//код изделия
                items.add(rs.getString(2));//цвет изделия
                items.add(rs.getInt(3));//кол-во
            }
        } catch (Exception e) {
            log.severe("Error in sdanoUpackSklad function: " + e.getMessage());
        }
        return items;
    }

    public Vector getPereuchZSH(int number) {
        Vector list = new Vector();
        String query = "select brig.naim, kld.ngpr, kld.fas, ml.nomer, kld.nar, sum(t1.kol) from (select kod_izd, kod_marh, kol from vnperem2 where kod_marh <> 0 and doc_id = (select item_id from vnperem1 where ndoc = ?)) as t1 " +
                " join (select kod, kod1 from nsi_sd)as sd on sd.kod1 = t1.kod_izd " +
                " join (select ngpr, nar, fas, kod from nsi_kld)as kld on kld.kod = sd.kod " +
                " join (select kpodvrkv, nomer, kod from marh_list)as ml on ml.kod = t1.kod_marh " +
                " join (select naim, kod1 from nsi_brig)as brig on brig.kod1 = ml.kpodvrkv " +
                "   group by brig.naim, kld.ngpr, kld.fas, ml.nomer, kld.nar " +
                "   order by brig.naim, kld.ngpr, kld.fas, ml.nomer, kld.nar ";
        String query1 = "select '', kld.ngpr, kld.fas, '', kld.nar, t1.kol from " +
                " (select kod_izd, kod_marh, kol from vnperem2 where kod_marh = 0 and doc_id = (select item_id from vnperem1 where ndoc = ?)) as t1 " +
                " join (select kod, kod1 from nsi_sd)as sd on sd.kod1 = t1.kod_izd " +
                " join (select ngpr, nar, fas, kod from nsi_kld)as kld on kld.kod = sd.kod ";
        try (PreparedStatement ps = getConnection().prepareStatement(query);
             PreparedStatement ps1 = getConnection().prepareStatement(query1);) {
            ps.setString(1, String.valueOf(number));
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Vector m = new Vector();
                m.add(rs.getString(1).trim());
                m.add(rs.getString(2).trim());
                m.add(rs.getString(3));
                m.add(rs.getString(4));
                m.add(rs.getString(5));
                m.add(rs.getInt(6));
                list.add(m);
            }

            ps1.setString(1, String.valueOf(number));
            ResultSet rs1 = ps1.executeQuery();
            while (rs1.next()) {
                Vector m = new Vector();
                m.add(rs1.getString(1).trim());
                m.add(rs1.getString(2).trim());
                m.add(rs1.getString(3));
                m.add(rs1.getString(4));
                m.add(rs1.getString(5));
                m.add(rs1.getInt(6));
                list.add(m);
            }

        } catch (Exception e) {
            log.severe("Ошибка при создании данных для Переучёта ЗШ  getPereuchZSH(int number) " + e.getMessage());
        }
        return list;
    }

    /* ****************************************************************************
     * ***************************OTK**********************************************
     *************************************************************************** */

    /**
     * Информация о изделиях принятых на упаковку за период
     *
     * @param sd начало периода
     * @param ed конец периода
     * @return Возвращает вектор описывающий изделия
     */
//    public Vector sdachaNaUpackOTK(String sd, String ed) {
//        long sDate = convertDateStrToLong(sd);
//        long eDate = convertDateStrToLong(ed);
//        Vector items = new Vector();
//        int i = 0;
//        kol = 0;
//        sum = 0;
//
//        String query = "select ml.nomer,t1.fas, t1.naim, t1.rzm, t1.nar, t1.srt, sum(t1.kol)as kol from (select naim, kol, fas, nar,  kod_izd, rzm, srt, kod_marh from v_nakl_sdacha1 where srt < 4 and data >= ? and data < ?) as t1 " +
//                " join (select cno, nds, kod, rzm_print, srt from nsi_sd)as t2 on t1.kod_izd = t2.kod " +
//                " join (select nomer, kod, kroy from marh_list) as ml on ml.kod = t1.kod_marh " +
//                " where t1.srt = t2.srt and t1.rzm = t2.rzm_print  group by ml.nomer, t1.naim, t1.nar, t1.srt, t2.cno,t2.nds, t1.rzm, t1.fas order by ml.nomer, t1.fas, t1.naim, t1.rzm, t1.srt ";
//        try (PreparedStatement ps = getConnection().prepareStatement(query);) {
//            ps.setDate(1, new java.sql.Date(sDate));
//            ps.setDate(2, new java.sql.Date(eDate + DAY));
//            ResultSet rs = ps.executeQuery();
//            while (rs.next()) {
//                Vector user = new Vector();
//                user.add(++i);
//                user.add(rs.getString(1));
//                user.add(rs.getString(2));
//                user.add(rs.getString(3));
//                user.add(rs.getString(4));
//                user.add(rs.getString(5));
//                user.add(rs.getLong(6));
//                user.add(rs.getLong(7));
//                items.add(user);
//            }
//        } catch (Exception e) {
//            log.severe("Ошибка полуеня списка cдачи на склад " + e);
//        }
//        return items;
//    }
    /* ***************************************************************************
     * ******************Пошив отчёт по трудозатратам*****************************
     *****************************************************************************/

    /**
     * Расчитывает стоимость пошитых изделий за период времение для бригады
     *
     * @param sDate начало периода
     * @param eDate конец периода
     * @param brig  номер бригады
     * @return стоимость
     */
    public double sumPoshivBrig(long sDate, long eDate, int brig) throws Exception {
        double rez = 0;
        String queryString = "select sum(t2.kol * t3.cno*(1+t3.nds/100)) from (select kod_marh, kod_izd from poshiv where brigada = ? group by kod_marh, kod_izd) as t1 " +
                " join ( select barcode, kod_marh, sum(kol)as kol from nakl_sdacha where data >= ? and data < ? group by barcode, kod_marh)as t2 on t1.kod_marh = t2.kod_marh " +
                " join (select nomer, kod from marh_list)as ml on ml.kod = t1.kod_marh " +
                " join (select barcode, rzm_marh, rst_marh, srt, kod_marh from label_one)as lo on lo.barcode = t2.barcode and lo.kod_marh = t1.kod_marh " +
                " join (select cno, nds, kod, rzm, srt, rst from nsi_sd )as t3 on t1.kod_izd = t3.kod where lo.srt = t3.srt and lo.rzm_marh = t3.rzm and lo.rst_marh = t3.rst";

        try (PreparedStatement ps = getConnection().prepareStatement(queryString);) {

            ps.setInt(1, brig);
            ps.setDate(2, new java.sql.Date(sDate));
            ps.setDate(3, new java.sql.Date(eDate + DAY));


            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                rez += rs.getFloat(1);
            }
        } catch (Exception e) {
            log.severe("Error in sumPoshivBrig function: " + e.getMessage());
        }
        rez /= 1000000;
        return rez;
    }

    public List<Integer> trudoZatBrig(long sDate, long eDate, int brig) throws Exception {
        List<Integer> items = new ArrayList();
        String queryString = "select sar, fas, sum(t2.kol) from (select kod_marh, kod_izd from poshiv where brigada = ? group by kod_marh, kod_izd) as t1 join ( select kod_marh, sum(kol)as kol from nakl_sdacha where data >= ? and data < ? group by kod_marh)as t2 on t1.kod_marh = t2.kod_marh  " +
                " join (select kod, kkr, sar, fas from nsi_kld) as nsikld on t1.kod_izd = nsikld.kod " +
                " group by sar, fas";

        try (PreparedStatement ps = getConnection().prepareStatement(queryString);) {
            ps.setInt(1, brig);
            ps.setDate(2, new java.sql.Date(sDate));
            ps.setDate(3, new java.sql.Date(eDate + DAY));
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                items.add(rs.getInt(1));//артикул
                items.add(rs.getInt(2));//модель
                items.add(rs.getInt(3));//кол-во
            }
        } catch (Exception e) {
            log.severe("Error in trudoZatBrig function: " + e.getMessage());
        }
        return items;
    }

    /* **************************************************************************
     * ****************************Склад****************************************
     * ***************************************************************************/
//склад
    public List realOstSklad() {
        List items = new ArrayList();
        String query = "select kod_izd, color, kol from _ost_sklad  where kol <> 0 order by kod_izd, color";
        try (PreparedStatement ps = getConnection().prepareStatement(query);) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Vector v = new Vector();
                v.add(rs.getInt(1));
                v.add(rs.getString(2));
                v.add(rs.getInt(3));
                items.add(v);
            }
        } catch (Exception e) {
            log.severe("Ошибка realOstSklad() " + e.getMessage());
        }
        return items;
    }

    /**
     * возврачает изделя принятые складом от упаковки(138), чулок(501),  внешние возвраты b (b133) в период времени
     *
     * @param sDate -- начало периода
     * @param eDate -- конец периода
     * @return набор данных в виде: код изделия -- цвет -- кол-во
     */
    public List zdanoNaSklad(long sDate, long eDate) {
        List items = new ArrayList();
        String queryString = "select t1.kod_izd, t1.ncw, sum(t1.kol)  from (select cena, (kol*kol_in_upack)as kol, srt, kod_izd, ncw from vnperem2 where doc_id in (select item_id from vnperem1 where status = 0 and (kpodot = 138 or kpodot = 133 or kpodot = 501) and kpodto = 737 and date >= ? and date < ?))as t1  " +
                " group by t1.kod_izd, t1.ncw";
        String queryString1 = "select kod_izd, ncw, sum(t1.kol*t1.kol_in_upack) from (select kol,kol_in_upack, kod_izd, ncw from otgruz2 where  doc_id in (select item_id from otgruz1 where status = 0 and date >= ? and date < ? and (operac = 'Возврат от покупателя' or operac = 'Возврат из розницы')))as t1 group by t1.kod_izd, t1.ncw";
        try (PreparedStatement ps = getConnection().prepareStatement(queryString);
             PreparedStatement ps1 = getConnection().prepareStatement(queryString1);) {
            ps.setDate(1, new java.sql.Date(sDate));
            ps.setDate(2, new java.sql.Date(eDate + DAY));
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                items.add(rs.getInt(1));//код изделия
                items.add(rs.getString(2));//цвет изделия
                items.add(rs.getInt(3));//кол-во
            }

            ps1.setDate(1, new java.sql.Date(sDate));
            ps1.setDate(2, new java.sql.Date(eDate + DAY));
            ResultSet rs1 = ps1.executeQuery();
            while (rs1.next()) {
                items.add(rs1.getInt(1));//код изделия
                items.add(rs1.getString(2));//цвет изделия
                items.add(rs1.getInt(3));//кол-во
            }
        } catch (Exception e) {
            log.severe("Error in zdanoNaSklad function: " + e.getMessage());
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
    public List otgruzkaSklad(long sDate, long eDate) {
        List items = new ArrayList();
        String queryString = "select t1.kod_izd, t1.ncw, sum(t1.kol*t1.kol_in_upack) from (select kol,kol_in_upack, kod_izd, ncw from otgruz2 where doc_id in (select item_id from otgruz1 where status = 0 and date >= ? and date < ? and (operac <> 'Возврат от покупателя' and operac <> 'Возврат из розницы')))as t1 " +
                " group by t1.kod_izd, t1.ncw";

        try (PreparedStatement ps = getConnection().prepareStatement(queryString);) {
            ps.setDate(1, new java.sql.Date(sDate));
            ps.setDate(2, new java.sql.Date(eDate));
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                items.add(rs.getInt(1));//код изделия
                items.add(rs.getString(2));//цвет изделия
                items.add(rs.getInt(3));//кол-во
            }
        } catch (Exception e) {
            log.severe("Error in otgruzkaSklad function: " + e.getMessage());
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
    public Vector getProtocolSoglas(String nomer) {
        Vector v = new Vector();
        String query = "select ngpr, nar, fas, cno, (cno/kurs) as rr, cenav, cena, kurs, skidka from " +
                " (select item_id, kurs, skidka from otgruz1 where ndoc = ?)as t " +
                " left join (select cena, kod_izd, cenav, doc_id from otgruz2 )as t1 on t.item_id = t1.doc_id" +
                " left join (select kod, kod1, cno from nsi_sd) as t3 on t1.kod_izd = t3.kod1" +
                " left join (select ngpr, fas, nar, kod from nsi_kld) as t2 on t3.kod = t2.kod" +
                " group by ngpr, nar, fas, cno, (cno/kurs), cenav, cena, kurs, skidka order by ngpr, nar, fas, cno";
        try (PreparedStatement ps = getConnection().prepareStatement(query);) {
            ps.setString(1, nomer);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Vector vv = new Vector();
                vv.add(rs.getString(1).trim());
                vv.add(rs.getString(2).trim());
                vv.add(rs.getString(3).trim());
                vv.add(rs.getLong(4));
                vv.add(rs.getFloat(5));
                vv.add(rs.getFloat(6));
                vv.add(rs.getLong(7));
                vv.add(rs.getLong(8));
                vv.add(rs.getFloat(9));
                v.add(vv);
            }
        } catch (Exception e) {
            log.severe("Ошибка в DB getProtocolSoglas(String nomer)" + e.getMessage());
        }
        return v;
    }

    /**
     * добавляет новую ячейку(возврат) в таблицу отгрузки
     *
     * @param dateDoc  дата документа
     * @param nomerTTN номер ТТН
     * @param operac   операция документа
     * @param idKlient код клиента
     * @param list     набор импортируемых данных
     * @return true / false
     */
    public boolean insertOtgruz(String dateDoc, String nomerTTN, String operac, int idKlient, ArrayList list, boolean debug) {
        boolean rezalt = false;
        int idItemKlient;
        int idDoc;
        int num = 0;
        String sql = "SELECT ITEM_ID FROM s_klient where KOD = ?";
        String sql1 = "INSERT INTO otgruz1([date],ndoc,kpodot,kpl,summa,summa_nds,summa_all,"
                + "	skidka,kola,kolk,kolr,status,ucenka3s,datevrkv,	uservrkv,kpodvrkv,datekrkv,userkrkv,"
                + "     kpodkrkv,klient_id,vid_sert,vid_ggr,operac,export,valuta_id,"
                + "	kurs,skidka_tip,kurs_bank,nalogi)"
                + "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);";
        String sql2 = "SELECT @@IDENTITY AS 'Identity'";
        String sql3 = "IF (not object_id('MyTempTable') IS NULL) DROP TABLE MyTempTable "
                + "CREATE TABLE MyTempTable ( COLOR_     CHAR(15) ,"
                + "                         KOL_       NUMERIC(5), "
                + "                         CENA_      DECIMAL(9,2),"
                + "                         SUMMA_     DECIMAL(9,2),"
                + "                         NDS_       DECIMAL(5,2),"
                + "                         SUMMA_NDS_ DECIMAL(9,2),"
                + "                         ITOGO_     DECIMAL(9,2),"
                + "                         EANCODE_   CHAR(13),"
                + "                         PREISCUR_  CHAR(30));";
        String sql4 = "select max(barcode) as barcodemax," +
                "KOL_, CENA_,  SUMMA_, NDS_," +
                "SUMMA_NDS_, ITOGO_, EANCODE_," +
                "PREISCUR_, rst_marh, rzm_marh," +
                "sd.kod1 as kod_izd, COLOR_," +
                "sd.srt as srt" +
                "from MyTempTable as mt " +
                "left join " +
                "(SELECT * FROM NSI_EANCODE " +
                "JOIN nsi_sd ON NSI_EANCODE.ITEM_CODE = nsi_sd.kod1) " +
                "as sd on EANCODE_= sd.EANCODE " +
                "left join label_one as lo on EANCODE_=lo.eancode " +
                "group by KOL_, CENA_,  SUMMA_, NDS_, SUMMA_NDS_, ITOGO_, EANCODE_, PREISCUR_, rst_marh, rzm_marh,  sd.kod1, COLOR_, sd.srt order by EANCODE_";

        if (debug) {
            sql4 = "select KOL_ as barcodemax, KOL_, CENA_,  SUMMA_, NDS_, SUMMA_NDS_, ITOGO_, EANCODE_, PREISCUR_, sd.rst as rst_marh, sd.rzm as rzm_marh, " +
                    "sd.kod1 as kod_izd, COLOR_, sd.srt as srt from MyTempTable as mt " +
                    "INNER JOIN NSI_EANCODE as NEAN on NEAN.EANCODE = EANCODE_ " +
                    "INNER JOIN nsi_sd as sd on NEAN.ITEM_CODE = sd.kod1 " +
                    "order by EANCODE_";
        }

        String sql5 = "INSERT INTO otgruz2(scan, part, kol, cena, summa, nds, summa_nds, "
                + "     itogo,cenav, summav, summa_ndsv, itogov, kol_in_upack, tip, time_ins, pc_ins,"
                + "     eancode, doc_id, kod_str, preiscur, rzm_marh, rst_marh, kod_izd, ncw, srt, cena_uch)"
                + " VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             PreparedStatement ips = conn.prepareStatement(sql1);
             PreparedStatement ps1 = conn.prepareStatement(sql2);
             PreparedStatement ps2 = conn.prepareStatement(sql3);
             PreparedStatement ps3 = conn.prepareStatement(sql4);
             PreparedStatement ps4 = conn.prepareStatement(sql5);
             Statement st = conn.createStatement();) {
            conn.setAutoCommit(false);
            ps.setDouble(1, idKlient);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                idItemKlient = rs.getInt("ITEM_ID");

                ips.setDate(1, java.sql.Date.valueOf(new String(new SimpleDateFormat("yyyy-MM-dd").format(new SimpleDateFormat("dd.MM.yyyy").parse(dateDoc)))));
                ips.setString(2, nomerTTN);                                      //ndoc
                ips.setInt(3, 737);                                           //kpodot
                ips.setInt(4, idKlient);                                      //kpl
                ips.setDouble(5, 0);                                             //summa
                ips.setDouble(6, 0);                                             //summa_nds
                ips.setDouble(7, 0);                                             //summa_all
                ips.setInt(8, 0);                                             //skidka
                ips.setDouble(9, 0);                                             //kola
                ips.setDouble(10, 0);                                            //kolk
                ips.setDouble(11, 0);                                            //kolr
                ips.setInt(12, 3);                                            //status
                ips.setDouble(13, 0);                                            //ucenka3s
                ips.setDate(14, new java.sql.Date(new java.util.Date().getTime())); //datevrkv
                ips.setString(15, "Склад6-карт");                                //uservrkv
                ips.setString(16, "737");                                        //kpodvrkv
                ips.setDate(17, new java.sql.Date(new java.util.Date().getTime())); //datekrkv
                ips.setString(18, "Склад6-карт");                                //userkrkv
                ips.setString(19, "737");                                        //kpodkrkv
                ips.setInt(20, idItemKlient);                                 //klient_id
                ips.setInt(21, 1);                                            //vid_sert
                ips.setInt(22, 1);                                            //vid_ggr
                ips.setString(23, operac);                                       //operac
                ips.setInt(24, 0);                                            //export
                ips.setInt(25, 1);                                            //valuta_id
                ips.setInt(26, 1);                                            //kurs
                ips.setInt(27, 0);                                            //skidka_tip
                ips.setInt(28, 1);                                            //kurs_bank
                ips.setInt(29, 1);                                            //nalogi
                ips.execute();


                ResultSet rs1 = ps1.executeQuery();
                if (rs1.next()) {
                    idDoc = rs1.getInt("Identity");

                    ps2.execute(sql);

                    for (Object o : list) {
                        num = 0;
                        Object[] row = (Object[]) o;
                        st.execute("INSERT INTO MyTempTable(COLOR_ , KOL_ , CENA_ , SUMMA_ , NDS_ , SUMMA_NDS_, ITOGO_ , EANCODE_ , PREISCUR_ )"
                                + " VALUES('" + row[num++] + "'," + row[num++] + "," + row[num++] + "," + row[num++] + "," + row[num++] + "," + row[num++] + "," + row[num++] + ",'" + row[num++] + "','" + row[num++] + "')");
                    }
                    // TODO ?!?!Если решится вопрос с таблицей nsi_sd, и туда можно будет заносить записи
                    //  с одинаковымими складскими кодами, и разными EAN убрать из запроса костыль в виде селекта.

                    ResultSet rs2 = ps3.executeQuery(sql);
                    num = 0;
                    while (rs2.next()) {

                        ps4.setInt(1, rs2.getInt("barcodemax"));               //scan
                        ps4.setInt(2, 1);                                     //part
                        ps4.setInt(3, rs2.getInt("KOL_"));            //kol
                        ps4.setDouble(4, rs2.getDouble("CENA_"));                 //cena
                        ps4.setDouble(5, rs2.getDouble("SUMMA_"));        //summa
                        ps4.setDouble(6, rs2.getDouble("NDS_"));            //nds
                        ps4.setDouble(7, rs2.getDouble("SUMMA_NDS_"));        //summa_nds
                        ps4.setDouble(8, rs2.getDouble("ITOGO_"));        //itogo
                        ps4.setDouble(9, .00);                                   //cenav
                        ps4.setDouble(10, .00);                                  //summav
                        ps4.setDouble(11, .00);                                  //summa_ndsv
                        ps4.setDouble(12, .00);                                  //itogov
                        ps4.setInt(13, 1);                                    //kol_in_upack
                        ps4.setInt(14, 1);                                    //tip
                        ps4.setDate(15, new java.sql.Date(new java.util.Date().getTime()));   //time_ins
                        ps4.setString(16, "SKLAD6 # proiz");                     //pc_ins
                        ps4.setString(17, rs2.getString("EANCODE_"));             //eancode
                        ps4.setInt(18, idDoc);                                //doc_id
                        ps4.setInt(19, rs2.getInt("barcodemax"));                 //kod_str
                        ps4.setString(20, rs2.getString("PREISCUR_"));            //preiscur
                        ps4.setInt(21, rs2.getInt("rzm_marh"));        //rzm_marh
                        ps4.setInt(22, rs2.getInt("rst_marh"));        //rst_marh
                        ps4.setInt(23, rs2.getInt("kod_izd"));                 //kod_izd
                        ps4.setString(24, rs2.getString("COLOR_"));               //ncw
                        ps4.setInt(25, rs2.getInt("srt"));            //srt
                        ps4.setDouble(26, rs2.getDouble("CENA_"));            //cena_uch
                        ps4.execute();
                    }
                    conn.commit();
                    rezalt = true;
                } else
                    conn.rollback();
            }
        } catch (Exception e) {
            rezalt = false;
            log.severe("Ошибка в insertOtgruz()" + e.getMessage());
        }
        return rezalt;
    }

    /* **************************************************************************
     * ****************************Остатки****************************************
     * ***************************************************************************/

    // справочники
    public List getAllColor() {
        List colors = new ArrayList();
        colors.add("Все");

        String query = "select distinct ncw from nsi_cd";
        try (PreparedStatement ps = getConnection().prepareStatement(query)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                colors.add(rs.getString(1).trim());
            }
        } catch (Exception e) {
            log.severe("Error in getAllColor function: " + e.getMessage());
        }
        return colors;
    }

    public List getAllCurrency() {
        List currency = new ArrayList();

        String query = "select naim from valuta";
        try (PreparedStatement ps = getConnection().prepareStatement(query)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                currency.add(rs.getString(1).trim());
            }
        } catch (Exception e) {
            log.severe("Error in getAllCurrency function: " + e.getMessage());
        }
        return currency;
    }

    public float getCurrencyKurs(String name) {
        float kurs = 1;

        String query = "select top 1 kurs, max(data) from valuta1 where valuta_id in (select kod from valuta where naim = ?)group by kurs, data order by data desc";
        try (PreparedStatement ps = getConnection().prepareStatement(query)) {
            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                kurs = rs.getFloat(1);
            }
        } catch (Exception e) {
            log.severe("Error in getAllCurrency function: " + e.getMessage());
        }
        return kurs;
    }

    public List descrOst(int type, String date, int model, String sar, int nar, String color, int size, int rst, String srt, int count, HashMap<String, Boolean> flags, String ngpr) {
        List<Vector> ost = new ArrayList();
        PDB pdb = new PDB();
        switch (type) {
            case 0: {
                ost = new ArrayList(realOstSklad());
                break;
            }
            case 1: {
                ost = pdb.monthOstSklad(Integer.parseInt(date));
                break;
            }
            case 2: {
                ost = pdb.dateOstSklad(date);
                break;
            }
        }
        pdb.disConn();

        List descrOst = new ArrayList();
        String query = getQueryForDescrOst(model, sar, nar, color, size, rst, srt, count, flags, ngpr);

        try (Connection conn = getConnection();
             PreparedStatement cps = conn.prepareStatement("create table __ost_sklad(kod_izd int, color varchar(50), kol int)");
             PreparedStatement ips = conn.prepareStatement("insert into __ost_sklad values(?, ?, ?)");
             PreparedStatement ps = conn.prepareStatement(query);
             PreparedStatement dps = conn.prepareStatement("drop table __ost_sklad;")) {
            conn.setAutoCommit(false);
            cps.executeUpdate();
            for (Vector v : ost) {
                ips.setInt(1, (Integer) v.get(0));
                ips.setString(2, v.get(1).toString());
                ips.setInt(3, (Integer) v.get(2));
                ips.executeUpdate();
            }

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Vector v = new Vector();
                v.add(rs.getString("ngpr").trim());
                if (Boolean.TRUE.equals(flags.get("Модель"))) {
                    v.add(rs.getInt("fas"));
                }
                if (Boolean.TRUE.equals(flags.get("Артикул"))) {
                    v.add(rs.getString("nar").trim());
                    v.add(rs.getInt("sar"));
                }
                if (flags.get("Размер")) {
                    v.add(rs.getInt("rst"));
                    v.add(rs.getInt("rzm"));
                }
                if (flags.get("Цвет")) {
                    v.add(rs.getString("color").trim());
                }
                if (flags.get("Сорт")) {
                    v.add(rs.getString("srt"));
                }
                v.add(rs.getInt("kol"));
                if (flags.get("Цена")) {
                    v.add(rs.getInt("cno"));
                }
                descrOst.add(v);
            }

            dps.executeUpdate();
            conn.commit();
        } catch (Exception e) {
            log.severe("Ошибка при выполнении фун-ции descrOst: " + e.getMessage());
        }

        return descrOst;
    }

    private static String getQueryForDescrOst(int model, String sar, int nar, String color, int size, int rst, String srt, int count, HashMap<String, Boolean> flags, String ngpr) {
        String query = "select ngpr";

        if (flags.get("Модель")) {
            query += ",  fas";
        }
        if (flags.get("Артикул")) {
            query += ",  sar";
            query += ",  nar";
        }

        if (flags.get("Размер")) {
            query += ",  rst";
            query += ",  rzm";
        }
        if (flags.get("Сорт")) {
            query += ",  srt";
        }

        if (flags.get("Цвет")) {
            query += ",  color";
        }
        query += ",  sum(kol) as kol";
        if (flags.get("Цена")) {
            query += ",  cno";
        }
        query += " from (select rst, rzm, srt, kod, kod1, cno from nsi_sd) as t1 " +
                " join (select ngpr, fas, sar ,nar, kod from nsi_kld) as t2 on t1.kod = t2.kod" +
                " join (select kod_izd, color, kol from __ost_sklad where kol <> 0)as t3 on t3.kod_izd = t1.kod1 " +
                " where 1 = 1 ";

        if (flags.get("Артикул")) {
            if (nar != -1) query += " and sar = " + nar;
            if (!sar.equals("Все")) query += " and nar = '" + sar + "'";
        }
        if (flags.get("Модель") && model != -1) {
            query += " and fas = " + model;
        }
        if (flags.get("Размер")) {
            if (rst != -1) query += " and rst = " + rst;
            if (size != -1) query += "and rzm = " + size;
        }
        if (flags.get("Сорт")) {
            if (!srt.equals("Все")) query += " and srt in (" + srt + ")";
        }
        if (flags.get("Цвет")) {
            if (!color.equals("Все")) query += " and color = '" + color + "'";
        }
        query += " and kol > " + count;
        if (flags.get("Название")) {
            if (!ngpr.equals("Все")) query += " and ngpr = '" + ngpr + "'";
        }

        query += " group by ngpr";
        if (flags.get("Модель")) {
            query += ",  fas";
        }
        if (flags.get("Артикул")) {
            query += ",  sar";
            query += ",  nar";
        }

        if (flags.get("Размер")) {
            query += ",  rst";
            query += ",  rzm";
        }
        if (flags.get("Сорт")) {
            query += ",  srt";
        }
        if (flags.get("Цвет")) {
            query += ",  color";
        }
        if (flags.get("Цена")) {
            query += ",  cno";
        }
        return query;
    }

    public List descrOstUpack(int type, String date, int model, String sar, int nar, String color, int size, int rst, String srt, int count, HashMap<String, Boolean> flags) {
        List<Vector> ost = new ArrayList();
        PDB pdb = new PDB();
        switch (type) {
            case 0: {
                ost = new ArrayList(realOstUpack());
                break;
            }
            case 1: {
                ost = pdb.monthOstUpack(Integer.parseInt(date));
                break;
            }
            case 2: {
                ost = pdb.dateOstUpack(date);
                break;
            }
        }
        pdb.disConn();

        List descrOst = new ArrayList();
        String query = getQueryForDescrOst(model, sar, nar, color, size, rst, srt, count, flags, null);
        try (Connection conn = getConnection();
             PreparedStatement cps = conn.prepareStatement("create table __ost_upack(kod_izd int, color varchar(50), kol int)");
             PreparedStatement ips = conn.prepareStatement("insert into __ost_upack values(?, ?, ?)");
             PreparedStatement sps = conn.prepareStatement(query);
             PreparedStatement dps = conn.prepareStatement("drop table __ost_upack;");) {
            conn.setAutoCommit(false);
            cps.executeUpdate();
            for (Vector v : ost) {
                ips.setInt(1, (Integer) v.get(0));
                ips.setString(2, v.get(1).toString());
                ips.setInt(3, (Integer) v.get(2));
                ips.executeUpdate();
            }

            ResultSet rs = sps.executeQuery();

            while (rs.next()) {
                Vector v = new Vector();
                v.add(rs.getString("ngpr").trim());
                if (flags.get("Модель")) {
                    v.add(rs.getInt("fas"));
                }
                if (flags.get("Артикул")) {
                    v.add(rs.getString("nar").trim());
                    v.add(rs.getInt("sar"));
                }
                if (flags.get("Размер")) {
                    v.add(rs.getInt("rst"));
                    v.add(rs.getInt("rzm"));
                }
                if (flags.get("Цвет")) {
                    v.add(rs.getString("color").trim());
                }
                if (flags.get("Сорт")) {
                    v.add(rs.getString("srt"));
                }
                v.add(rs.getInt("kol"));
                if (flags.get("Цена")) {
                    v.add(rs.getInt("cno"));
                }
                descrOst.add(v);
            }

            dps.executeUpdate();
            conn.commit();
        } catch (Exception e) {
            log.severe("Ошибка при выполнении фун-ции descrOstUpack(): " + e.getMessage());
        }

        return descrOst;
    }

//    public void setOstMonth() {
//        try {
//            PDB pd = new PDB();
//            ps = conn.prepareStatement("select * from _ost_sklad");
//            rs = ps.executeQuery();
//            while (rs.next()) {
//                pd.setOst(rs.getInt("kod_izd"), rs.getString("color"), rs.getInt("kol"));
//            }
//            pd.disConn();
//        } catch (Exception e) {
//            log.severe("Ошибка setOstMonth(): ", e.getMessage());
//            JOptionPane.showMessageDialog(null, "Ошибка setOstMonth(): " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
//        }
//    }

/******************************************************************************
 ---------------------------Маркетинг-------------------------------------------
 ******************************************************************************/
    /**
     * возвращает все фасоны выбранного изделия
     *
     * @param sar
     * @param nar
     * @return набор данных
     */
    public Vector getFason(int sar, String nar) {
        Vector fasons = new Vector();
        String query = "SELECT fas from nsi_kld where sar = ? and nar like '?%'";
        try (PreparedStatement ps = getConnection().prepareStatement(query)) {
            ps.setInt(1, sar);
            ps.setString(2, nar);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                fasons.add(rs.getInt(1));
            }
        } catch (Exception e) {
            log.severe("Ошибка getFason(): " + e.getMessage());
            JOptionPane.showMessageDialog(null, "Ошибка getFason(): " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
        return fasons;
    }

    /**
     * возвращает название изделия ngpr
     *
     * @param sar
     * @param nar
     * @return возвращает название изделия ngpr
     */
    public String getNameIzdelie(int sar, String nar) {
        String nameizd = null;
        String query = "Select ngpr from nsi_kld where sar = ? and nar like '?%' ";
        try (PreparedStatement ps = getConnection().prepareStatement(query)) {
            ps.setInt(1, sar);
            ps.setString(2, nar);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                nameizd = rs.getString(1).trim();
            }
        } catch (Exception e) {
            log.severe("Ошибка getNameIzdelie(): " + e.getMessage());
            JOptionPane.showMessageDialog(null, "Ошибка getNameIzdelie(): " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
        return nameizd;
    }

    public Object[] getNameIzdelie(String sarpattern) {
        Object[] nameizd = {""};
        Vector tmpnameizd = new Vector();
        tmpnameizd.add("");
        String sqlsar = "";
        if (!sarpattern.substring(1, 2).equals("3")) sqlsar = " and sar not like '" + "43______" + "' ";
        try (PreparedStatement ps = getConnection().prepareStatement("Select distinct ngpr from nsi_kld, nsi_sd, _ost_sklad " +
                " where nsi_sd.kod = nsi_kld.kod and nsi_kld.kod = nsi_sd.kod and nsi_sd.kod1 = _ost_sklad.kod_izd " +
                "       and sar like '" + sarpattern + "' " + sqlsar + " "
                + " Order by ngpr");) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String t = rs.getString(1).replace(".", " ").trim().replace("  ", " ").replace("  ", " ");
                if (!tmpnameizd.contains(t))
                    tmpnameizd.add(t);
            }
            nameizd = new Object[tmpnameizd.size()];
            tmpnameizd.copyInto(nameizd);
        } catch (Exception e) {
            log.severe("Ошибка getNameIzdelie(): " + e.getMessage());
            JOptionPane.showMessageDialog(null, "Ошибка getNameIzdelie(): " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
        return nameizd;
    }

    public Object[] getAllNameIzdelie() {
        Object[] nameizd = {""};
        Vector tmpnameizd = new Vector();
        tmpnameizd.add("");
        String query = "Select distinct ngpr from nsi_kld, nsi_sd, _ost_sklad " +
                " where nsi_sd.kod = nsi_kld.kod and nsi_kld.kod = nsi_sd.kod and nsi_sd.kod1 = _ost_sklad.kod_izd";
        try (PreparedStatement ps = getConnection().prepareStatement(query)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String t = rs.getString(1).replace(".", " ").trim().replace("  ", " ").replace("  ", " ");
                if (!tmpnameizd.contains(t))
                    tmpnameizd.add(t);
            }
            nameizd = new Object[tmpnameizd.size()];
            tmpnameizd.copyInto(nameizd);
        } catch (Exception e) {
            log.severe("Ошибка getNameIzdelie(): " + e.getMessage());
            JOptionPane.showMessageDialog(null, "Ошибка getNameIzdelie(): " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
        return nameizd;
    }

    public Vector getSort(Object sar, String nar, String nptk, int fas, boolean checkbox) {
        Vector sorts = new Vector();
        String sql = "";
        String sqlsklad = "";
        String fromsklad = "";
        String sqlnptk = "";
        if (checkbox) {
            sqlsklad = " and nsi_sd.kod1 = _ost_sklad.kod_izd and _ost_sklad.kol > 0 ";
            fromsklad = " ,_ost_sklad ";
        }
        if (nptk.equals("Без категории")) sqlnptk = " and nsi_kld.ptk = 0 ";
        else sqlnptk = " and nsi_kld.ptk = nsi_pkd.ptk and nptk like '" + nptk + "%' ";

        if (nptk.equals(""))
            sql = "Select distinct srt From  nsi_sd, nsi_kld " + fromsklad + " " +
                    "      	where nsi_sd.kod = nsi_kld.kod " +
                    "         and sar like '" + sar + "%' and nar like '" + nar + "%' and fas=" + fas + " " + sqlsklad +
                    "       Order by srt ";
        else
            sql = "Select distinct srt From  nsi_sd, nsi_kld, nsi_pkd " + fromsklad + "" +
                    "      	where nsi_sd.kod = nsi_kld.kod " +
                    "         and sar like '" + sar + "%' and nar like '" + nar + "%' and fas=" + fas + " " + sqlnptk + sqlsklad +
                    "       Order by srt ";
        try (PreparedStatement ps = getConnection().prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                sorts.add(rs.getInt(1));
            }
        } catch (Exception e) {
            log.severe("Ошибка getSort(): " + e.getMessage());
            JOptionPane.showMessageDialog(null, "Ошибка getSort(): " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
        return sorts;
    }

//    public Vector getRazmer(int sar, String nar, int fas, int srt) {
//        Vector razmers = new Vector();
//        String sql = "";
//        try {
//            sql = "Select distinct  rst, rzm from nsi_kld, nsi_sd " +
//                    "     where nsi_sd.kod = nsi_kld.kod and sar = " + sar + " and nar like '" + nar + "%' and fas=" + fas + " and srt=" + srt + " " +
//                    "Order by rst, rzm ";
//
//            ps = conn.prepareStatement(sql);
//            rs = ps.executeQuery();
//            while (rs.next()) {
//                Vector tmp = new Vector();
//                tmp.add(rs.getObject("rst"));
//                tmp.add(rs.getObject("rzm"));
//                razmers.add(tmp);
//            }
//        } catch (Exception e) {
//            log.severe("Ошибка getRazmer(): ", e.getMessage());
//            JOptionPane.showMessageDialog(null, "Ошибка getRazmer(): " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
//        }
//        return razmers;
//    }

    public Vector getRazmer(Object sar, String nar, String nptk, int fas, int srt, String color, int idvaluta, int course, boolean checkbox) {
        Vector razmers = new Vector();
        double kurs = 1;
        String sql = "";
        String sqlcolor = "";
        String sqlnptk = "";
        String sqlcno = "";
        if (checkbox)
            sqlcolor = " and nsi_sd.kod1 = _ost_sklad.kod_izd and _ost_sklad.kol > 0 and _ost_sklad.color like '" + color + "%' ";
        if (srt < 3) sqlcno = " and cno <> 0 ";
        if (nptk.equals("Без категории")) sqlnptk = " and nsi_kld.ptk = 0 ";
        else sqlnptk = " and nsi_kld.ptk = nsi_pkd.ptk and nptk like '" + nptk + "%' ";

        if (color.equals("неизвестен")) {
            if (nptk.equals(""))
                sql = "Select distinct  sar, nar, ngpr, rst, rzm, cno from nsi_kld, nsi_sd" +
                        "     where nsi_sd.kod = nsi_kld.kod " + sqlcno + // and cno <> 0
                        "        and sar like '" + sar + "%' and nar like '" + nar + "%' and fas=" + fas + " and srt=" + srt + " " +
                        "    Order by sar, nar, ngpr, rst, rzm, cno ";
            else
                sql = "Select distinct  sar, nar, ngpr, rst, rzm, cno from nsi_kld, nsi_sd, nsi_pkd" +
                        "     where nsi_sd.kod = nsi_kld.kod " + sqlnptk +
                        "        and sar like '" + sar + "%' and nar like '" + nar + "%' and fas=" + fas + " and srt=" + srt + " " +
                        "        and (nar LIKE '" + MyReportsModule.sYear + "%' OR nar LIKE '" + MyReportsModule.eYear + "%') " +
                        "    Order by sar, nar, ngpr, rst, rzm, cno ";

        } else {
            if (Integer.parseInt(sar.toString().replace("%", "")) > 40000 && !sar.toString().contains("%")) {
                sql = "Select distinct  sar, nar, ngpr, rst, rzm, cno from nsi_kld, nsi_sd, _ost_sklad" +
                        "   where nsi_sd.kod = nsi_kld.kod " + sqlcno + // and cno <> 0
                        "	 and sar like '" + sar + "%' and nar like '" + nar + "%' and fas=" + fas + " and srt=" + srt + " " + sqlcolor +
                        "   Order by sar, nar, ngpr, rst, rzm, cno ";
            } else {
                if (nptk.equals(""))
                    sql = "Select distinct  sar, nar, ngpr, rst, rzm, cno from nsi_kld, nsi_sd, _ost_sklad" +
                            "   where nsi_sd.kod = nsi_kld.kod " +
                            "	 and sar like '" + sar + "%' and nar like '" + nar + "%' and fas=" + fas + " and srt=" + srt + " " +
                            "	 and nsi_sd.kod1 = _ost_sklad.kod_izd " + sqlcolor +
                            "   Order by sar, nar, ngpr, rst, rzm, cno ";

                else
                    sql = "Select distinct  sar, nar, ngpr, rst, rzm, cno from nsi_kld, nsi_sd, _ost_sklad, nsi_pkd" +
                            "   where nsi_sd.kod = nsi_kld.kod " + sqlnptk +
                            "	 and sar like '" + sar + "%' and nar like '" + nar + "%' and fas=" + fas + " and srt=" + srt + " " +
                            "	 and nsi_sd.kod1 = _ost_sklad.kod_izd " + sqlcolor +
                            "   Order by sar, nar, ngpr, rst, rzm, cno ";
            }
        }
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement("Select kurs from valuta1 where valuta_id = " + idvaluta + " and data = (Select max(data) from valuta1 where valuta_id = " + idvaluta + ")");
             PreparedStatement sps = conn.prepareStatement(sql)) {
            if (idvaluta != 0) {
                ResultSet rs = ps.executeQuery();
                if (rs.next())
                    kurs = rs.getDouble(1);
            } else
                kurs = course;

            ResultSet rs_ = sps.executeQuery();
            while (rs_.next()) {
                Vector razmer = new Vector();
                razmer.add(false);                                              //checkbox
                razmer.add(rs_.getString("sar"));                               //sar
                razmer.add(rs_.getString("nar"));                               //nar
                razmer.add(rs_.getString("ngpr").trim());                       //ngpr
                razmer.add(rs_.getInt("rst"));                                  //рост
                razmer.add(rs_.getInt("rzm"));                                  //размер
                razmer.add(new BigDecimal(rs_.getDouble("cno") / kurs).setScale(2, RoundingMode.HALF_UP).doubleValue());                                             //цена без НДС
                razmer.add(getOstatokIzd(rs_.getString("sar").trim(), rs_.getString("nar").trim(), fas, srt, rs_.getInt("rst"), rs_.getInt("rzm"), color.trim()));//остаток
                razmer.add(1);
                razmers.add(razmer);
            }
            rs_.close();
        } catch (Exception e) {
            log.severe("Ошибка getRazmer(): " + e.getMessage());
            JOptionPane.showMessageDialog(null, "Ошибка getRazmer(): " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
        return razmers;
    }

    public Vector getAllRazmer(Object sar, String nar, String ngpr, int rzm1, int rzm2, int rst1, int rst2) {
        Vector razmers = new Vector();
        String tmpSar;
        String tmpSql = "";
        tmpSar = String.valueOf(sar).replace("0", "_");
        if (tmpSar.substring(1, 2).equals("_")) {
            tmpSar = new StringBuilder(tmpSar).delete(1, 2).toString();
            tmpSar = new StringBuilder(tmpSar).insert(1, "[12]").toString();
        }

        if (rst1 >= 0 && rst2 >= 0)
            tmpSql = " and  (rst BETWEEN " + rst1 + " AND " + rst2 + ") ";
        if (rzm1 >= 0 && rzm2 >= 0)
            tmpSql += "and (rzm BETWEEN " + rzm1 + " AND " + rzm2 + ") ";

        String query = "Select distinct rst, rzm from nsi_kld, nsi_sd, _ost_sklad" +
                " where nsi_sd.kod = nsi_kld.kod" +
                "     and nsi_sd.kod1 = _ost_sklad.kod_izd " + tmpSql + " and sar like '" + tmpSar + "' " +
                " Order by rst, rzm ";
        try (PreparedStatement ps = getConnection().prepareStatement(query)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Vector razmer = new Vector();
                razmer.add(false);                                              //checkbox
                razmer.add(sar);                                                //sar
                razmer.add(nar);                                                //nar
                razmer.add(ngpr);                                               //ngpr
                razmer.add(rs.getInt(1));                                       //рост
                razmer.add(rs.getInt(2));                                       //размер
                razmer.add(0);                                                  //цена без НДС
                razmer.add(0);                                                  //остаток
                razmer.add(1);                                                  //кол-во
                razmers.add(razmer);
            }
        } catch (Exception e) {
            log.severe("Ошибка getAllRazmer(): " + e.getMessage());
            JOptionPane.showMessageDialog(null, "Ошибка getAllRazmer(): " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
        return razmers;
    }

    public Vector getColor(Object sar, String nar, String nptk, int fas, int srt, boolean checkbox) {
        Vector<String> colors = new Vector();
        String sql = "";
        String sqlsklad = "";
        String sqlnptk = "";
        if (checkbox) sqlsklad = " and _ost_sklad.kol > 0 and srt=" + srt + " ";
        if (nptk.equals("Без категории")) sqlnptk = " and nsi_kld.ptk = 0 ";
        else sqlnptk = " and nsi_kld.ptk = nsi_pkd.ptk and nptk like '" + nptk + "%' ";

        if (nptk.isEmpty())
            sql = "Select distinct color From  nsi_sd, nsi_kld,_ost_sklad" +
                    "      	where nsi_sd.kod1 = _ost_sklad.kod_izd and nsi_sd.kod = nsi_kld.kod " +
                    "         and sar like '" + sar + "%' and nar like '" + nar + "%' and fas=" + fas + " " + sqlsklad +
                    "       Order by color ";
        else
            sql = "Select distinct color From  nsi_sd, nsi_kld,_ost_sklad, nsi_pkd" +
                    "      	where nsi_sd.kod1 = _ost_sklad.kod_izd and nsi_sd.kod = nsi_kld.kod " +
                    "         and sar like '" + sar + "%' and nar like '" + nar + "%' and fas=" + fas + " " + sqlnptk + sqlsklad +
                    "       Order by color ";
        try (PreparedStatement ps = getConnection().prepareStatement(sql)) {

            ResultSet rs = ps.executeQuery();
            while (rs.next())
                colors.add(rs.getString(1).trim());

            if ((!checkbox && Integer.parseInt(sar.toString().replace("%", "")) > 40000 && !sar.toString().contains("%") && !nar.isEmpty()) || colors.isEmpty())
                colors.add("неизвестен");

        } catch (Exception e) {
            log.severe("Ошибка getColor(): " + e.getMessage());
            JOptionPane.showMessageDialog(null, "Ошибка getColor(): " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
        return colors;
    }

    public int getOstatokIzd(Object sar, Object nar, Object fas, Object srt, Object rst, Object rzm, Object color) {
        int ostIzd = 0;
        String query = "Select distinct rst, rzm, cno, kol from nsi_kld, nsi_sd, _ost_sklad" +
                "   where nsi_sd.kod = nsi_kld.kod " +
                "	 and nsi_kld.kod in (Select kod from nsi_kld where sar=" + sar + " and nar like '" + nar + "%' and fas=" + fas + " and srt=" + srt + " and rst=" + rst + " and rzm=" + rzm + ")" +
                "	 and nsi_sd.kod1 = _ost_sklad.kod_izd and _ost_sklad.color like '" + color + "%'" +
                "   Order by rst, rzm ";
        try (PreparedStatement ps = getConnection().prepareStatement(query)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) ostIzd = rs.getInt("kol");
        } catch (Exception e) {
            log.severe("Ошибка getOstatokIzd(): " + e.getMessage());
            JOptionPane.showMessageDialog(null, "Ошибка getOstatokIzd(): " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
        return ostIzd;
    }

    public void createNDocTable() {
        PDB pdb = new PDB();
        Vector ndoc = pdb.getNDocOstatokPostponed();
        pdb.disConn();

        try (Connection conn = getConnection();
             Statement st = conn.createStatement();) {
            st.execute("IF (not object_id('tempdb..#MyTempTable') IS NULL) DROP TABLE #MyTempTable CREATE TABLE #MyTempTable (doc VARCHAR(20) PRIMARY KEY, idorder INT, dateorder VARCHAR(20));");

            for (int i = 0; i < ndoc.size(); i++) {
                Vector row = (Vector) ndoc.elementAt(i);
                st.execute("INSERT INTO #MyTempTable VALUES ('" + row.get(0).toString() + "', " + Integer.parseInt(row.get(1).toString()) + ", '" + row.get(2).toString() + "' );");
            }

        } catch (Exception e) {
            log.severe("Ошибка getRazmer(): " + e.getMessage());
            JOptionPane.showMessageDialog(null, "Ошибка getRazmer(): " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    public int getOstatokPostponed(Object sar, Object nar, Object fas, Object sort, Object rst, Object rzm, Object color) {
        int kolsobsklad = 0;
        try (PreparedStatement ps = getConnection().prepareStatement("select kod1, sar, nar, fas, nsi_sd.srt, ngpr, rst, rzm, otgruz2.ncw, sum(kol*kol_in_upack)" +
                     "  from otgruz1,otgruz2,nsi_sd, nsi_kld, #MyTempTable" +
                     "			  where ndoc COLLATE DATABASE_DEFAULT = doc" +
                     " 				 and otgruz2.doc_id = otgruz1.item_id" +
                     "           			 and nsi_sd.kod1 = otgruz2.kod_izd" +
                     "               			 and nsi_kld.kod = nsi_sd.kod" +
                     "               			 and nsi_sd.kod1 = (Select nsi_sd.kod1 from nsi_kld,nsi_sd where " +
                     "						 sar = " + sar + "" +
                     "						 and nar = '" + nar + "' " +
                     "						 and fas = " + fas + " " +
                     "						 and srt = " + sort + "" +
                     "						 and rst = " + rst + "" +
                     "						 and rzm = " + rzm + "" +
                     "						 and nsi_kld.kod = nsi_sd.kod)" +
                     "                                and otgruz2.ncw = '" + color + "' " +
                     "        Group by kod1, sar, nar, fas, nsi_sd.srt, ngpr, rst,rzm, otgruz2.ncw, nsi_sd.kod1");
             ResultSet rs = ps.executeQuery();) {
            while (rs.next()) {
                kolsobsklad += rs.getInt(10);
            }
        } catch (Exception e) {
            log.severe("Ошибка getOstatokPostponed(): " + e.getMessage());
            JOptionPane.showMessageDialog(null, "Ошибка getOstatokPostponed(): " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
        return kolsobsklad;
    }

    public Vector<String> getClientsPostponed(Object sar, Object nar, Object fas, Object sort, Object color, Object rost, Object razmer) {
        Vector<String> clients = new Vector<String>();
        PDB pdb = new PDB();
        Vector ndoc = pdb.getNDocOstatokPostponed();
        pdb.disConn();
        try (PreparedStatement ps = getConnection().prepareStatement("Select NAIM, idorder, dateorder, sum(kol*kol_in_upack)  from otgruz1,otgruz2,nsi_sd, nsi_kld,s_klient, #MyTempTable" +
                "                         	  where ndoc COLLATE DATABASE_DEFAULT = doc " +
                "                                 	and otgruz2.doc_id = otgruz1.item_id and nsi_sd.kod1 = otgruz2.kod_izd" +
                "                         		and nsi_kld.kod = nsi_sd.kod and nsi_sd.kod1 = (Select nsi_sd.kod1 from nsi_kld,nsi_sd where" +
                "                         						         sar = " + sar + "" +
                "                                						 and nar = '" + nar + "'" +
                "                                						 and fas = " + fas + "" +
                "                                						 and srt = " + sort + "" +
                "                                						 and rst = " + rost + "" +
                "                                						 and rzm = " + razmer + "" +
                "								 and nsi_kld.kod = nsi_sd.kod)" +
                "						and otgruz2.ncw = '" + color + "' " +
                "                                   and ITEM_ID = klient_id and operac NOT like 'Возврат%'" +
                "     Group by NAIM,  idorder, dateorder")) {

            if (!ndoc.isEmpty()) {
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    clients.add("заказ № " + rs.getObject(2) + " - " + rs.getObject(4) + " шт. " + rs.getString(1).trim() + " на " + rs.getObject(3));
                }
            }
        } catch (Exception e) {
            log.severe("Ошибка getClientsPostponed(): " + e.getMessage());
            JOptionPane.showMessageDialog(null, "Ошибка getClientsPostponed(): " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
        return clients;
    }

    /**
     * возвращает ветку дерева по классу продукции
     *
     * @param ppid
     * @param name
     * @param checkbox
     */
    public Vector openNode(int ppid, String name, boolean checkbox, boolean treefas) {
        Vector rows = new Vector();
        if (ppid == 4) {
            Object[] vetkagroup = {"Без категории", "Бельё", "Верхний трикотаж", "Чулки", "нет имени", "Прочие", "нет имени", "Полотно", "Пряжа-протир.", "Мебель"};
            for (int i = 0; i < vetkagroup.length; i++) {
                Vector newRow = new Vector();
                newRow.addElement(40 + i);
                newRow.addElement(4);
                newRow.addElement("1");
                newRow.addElement("+");
                newRow.addElement(vetkagroup[i]);
                rows.addElement(newRow);
            }
        }
        else if (ppid == 40 && treefas) {
            String sql = "Select distinct fas, sar from nsi_kld,_ost_sklad,nsi_sd " +
                    "   where nsi_kld.kod = nsi_sd.kod and nsi_sd.kod1 = _ost_sklad.kod_izd and nsi_kld.ptk = 0" +
                    " UNION " +
                    " Select distinct fas, sar from nsi_kld,nsi_pkd " +
                    "   where nsi_kld.ptk = 0  and (nar LIKE '" + MyReportsModule.sYear + "%' OR nar LIKE '" + MyReportsModule.eYear + "%')" +
                    " Order by fas, sar";
            if (checkbox) {
                sql = "Select distinct fas, sar from nsi_kld,_ost_sklad,nsi_sd " +
                        "  where nsi_kld.kod = nsi_sd.kod and nsi_sd.kod1 = _ost_sklad.kod_izd " +
                        "        and _ost_sklad.kol>0 " +
                        "        and nsi_kld.ptk = 0" +
                        "  Order by fas, sar";

            }
            try (PreparedStatement ps = getConnection().prepareStatement(sql);) {
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    Vector newRow = new Vector();
                    newRow.addElement(0);
                    newRow.addElement(rs.getObject(2));
                    newRow.addElement("");
                    newRow.addElement("");
                    newRow.addElement(rs.getObject(1));
                    rows.addElement(newRow);
                }
            } catch (Exception e) {
                log.severe("Ошибка openNode_() ppid = 40: " + e.getMessage());
            }
        }
        else if (ppid == 40) {
            String sql = "Select distinct nar, sar from nsi_kld,_ost_sklad,nsi_sd " +
                    "   where nsi_kld.kod = nsi_sd.kod and nsi_sd.kod1 = _ost_sklad.kod_izd and nsi_kld.ptk = 0" +
                    " UNION " +
                    " Select distinct nar,sar from nsi_kld,nsi_pkd " +
                    "   where nsi_kld.ptk = 0  and (nar LIKE '" + MyReportsModule.sYear + "%' OR nar LIKE '" + MyReportsModule.eYear + "%')" +
                    " Order by sar";
            if (checkbox) sql = "Select distinct nar, sar from nsi_kld,_ost_sklad,nsi_sd " +
                    "  where nsi_kld.kod = nsi_sd.kod and nsi_sd.kod1 = _ost_sklad.kod_izd " +
                    "        and _ost_sklad.kol>0 " +
                    "        and nsi_kld.ptk = 0" +
                    "  Order by sar";
            try (PreparedStatement ps = getConnection().prepareStatement(sql)) {
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    Vector newRow = new Vector();
                    newRow.addElement(rs.getInt(2));
                    newRow.addElement(ppid);
                    newRow.addElement("");
                    newRow.addElement("");
                    newRow.addElement(rs.getObject(1));
                    rows.addElement(newRow);
                }
            } catch (Exception e) {
                log.severe("Ошибка openNode_() ppid = 40: " + e.getMessage());
            }
        }
        else if (ppid > 40 && ppid < 50) {
            Object[] vetkaizd = {"Мебель", "Изделия мужские", "Изделия женские", "Изделия детские", "нет", "Спорт. взр.", "Спорт. дет.", "Игрушки", "Наборы", "нет имени"};
            for (int j = 0; j < vetkaizd.length; j++) {
                Vector newRow = new Vector();
                newRow.addElement(ppid * 10 + j);
                newRow.addElement(ppid);
                newRow.addElement("1");
                newRow.addElement("+");
                newRow.addElement(vetkaizd[j]);
                rows.addElement(newRow);
            }
        }
        else if (ppid >= 400 && ppid < 500) {
            String sql =  "Select distinct nptk,  SUBSTRING(CAST(sar AS varchar), 1, 4) AS EXPR1 from nsi_kld,nsi_pkd,_ost_sklad,nsi_sd " +
                    "                          where nsi_kld.ptk = nsi_pkd.ptk and sar like '" + ppid + "%' " +
                    "                            and nsi_kld.kod = nsi_sd.kod and nsi_sd.kod1 = _ost_sklad.kod_izd" +
                    "                        UNION " +
                    "                        Select distinct nptk,  SUBSTRING(CAST(sar AS varchar), 1, 4) AS EXPR1 from nsi_kld,nsi_pkd  " +
                    "			   where nsi_kld.ptk = nsi_pkd.ptk  and sar like '" + ppid + "%' " +
                    "			     and (nar LIKE '" + MyReportsModule.sYear + "%' OR nar LIKE '" + MyReportsModule.eYear + "%')   " +
                    "                        Order by nptk, EXPR1";
            if (checkbox)
                sql = "Select distinct nptk,  SUBSTRING(CAST(sar AS varchar), 1, 4) AS EXPR1 from nsi_kld,nsi_pkd,_ost_sklad,nsi_sd " +
                        "                          where nsi_kld.ptk = nsi_pkd.ptk and sar like '" + ppid + "%' " +
                        "                            and nsi_kld.kod = nsi_sd.kod and nsi_sd.kod1 = _ost_sklad.kod_izd" +
                        "                            and _ost_sklad.kol>0 " +
                        "                         Order by nptk, EXPR1";
            try (PreparedStatement ps = getConnection().prepareStatement(sql)) {
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    Vector newRow = new Vector();
                    newRow.addElement(Integer.parseInt(rs.getObject(2).toString()));
                    newRow.addElement(ppid);
                    newRow.addElement("1");
                    newRow.addElement("+");
                    newRow.addElement(rs.getObject(1));
                    rows.addElement(newRow);
                }
            } catch (Exception e) {
                log.severe("Ошибка openNode_() ppid>=400 && ppid<500: " + e.getMessage());
            }
        }
        else if (ppid >= 4000 && ppid < 5000 && treefas) {
            String sql = "Select distinct fas from nsi_kld,nsi_pkd,_ost_sklad,nsi_sd " +
                    "   where nsi_kld.ptk=nsi_pkd.ptk and sar like '" + ppid + "%' and nptk like '" + name + "%' " +
                    "       and nsi_kld.kod = nsi_sd.kod and nsi_sd.kod1 = _ost_sklad.kod_izd" +
                    " UNION " +
                    " Select distinct fas from nsi_kld,nsi_pkd, nsi_sd " +
                    "   where nsi_kld.ptk=nsi_pkd.ptk  and sar like '" + ppid + "%' " +
                    "        and nsi_sd.kod = nsi_kld.kod and  nptk like '" + name + "%'  and (nar LIKE '" + MyReportsModule.sYear + "%' OR nar LIKE '" + MyReportsModule.eYear + "%')" +
                    " Order by fas";
            if (checkbox) sql = "Select distinct fas from nsi_kld,nsi_pkd,_ost_sklad,nsi_sd " +
                    "  where nsi_kld.ptk=nsi_pkd.ptk and sar like '" + ppid + "%' and  nptk like '" + name + "%' " +
                    "       and nsi_kld.kod = nsi_sd.kod and nsi_sd.kod1 = _ost_sklad.kod_izd" +
                    "       and _ost_sklad.kol > 0" +
                    "  Order by fas";
            try (PreparedStatement ps = getConnection().prepareStatement(sql)) {
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    Vector newRow = new Vector();
                    newRow.addElement(0);
                    newRow.addElement(ppid);
                    newRow.addElement("");
                    newRow.addElement("");
                    newRow.addElement(rs.getObject(1));
                    rows.addElement(newRow);
                }
            } catch (Exception e) {
                log.severe("Ошибка openNode_() ppid>=4000 && ppid<5000 && treefas: " + e.getMessage());
            }
        }
        else if (ppid >= 4000 && ppid < 5000) {
            String sql = "Select distinct nar,sar from nsi_kld,nsi_pkd,_ost_sklad,nsi_sd " +
                    "   where nsi_kld.ptk=nsi_pkd.ptk and sar like '" + ppid + "%' and nptk like '" + name + "%' " +
                    "       and nsi_kld.kod = nsi_sd.kod and nsi_sd.kod1 = _ost_sklad.kod_izd" +
                    " UNION " +
                    " Select distinct nar,sar from nsi_kld,nsi_pkd " +
                    "   where nsi_kld.ptk=nsi_pkd.ptk  and sar like '" + ppid + "%'" +
                    "       and  nptk like '" + name + "%'  and (nar LIKE '" + MyReportsModule.sYear + "%' OR nar LIKE '" + MyReportsModule.eYear + "%')" +
                    " Order by sar";
            if (checkbox)
                sql = "Select distinct nar,sar from nsi_kld,nsi_pkd,_ost_sklad,nsi_sd " +
                        "  where nsi_kld.ptk=nsi_pkd.ptk and sar like '" + ppid + "%' and  nptk like '" + name + "%' " +
                        "       and nsi_kld.kod = nsi_sd.kod and nsi_sd.kod1 = _ost_sklad.kod_izd" +
                        "       and _ost_sklad.kol > 0" +
                        "  Order by sar";
            try (PreparedStatement ps = getConnection().prepareStatement(sql)) {
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    Vector newRow = new Vector();
                    newRow.addElement(rs.getInt(2));
                    newRow.addElement(ppid);
                    newRow.addElement("");
                    newRow.addElement("");
                    newRow.addElement(rs.getObject(1));
                    rows.addElement(newRow);
                }
            } catch (Exception e) {
                log.severe("Ошибка openNode_() ppid>=4000 && ppid<5000: " + e.getMessage());
            }
        }
        return rows;
    }

    /**
     * возвращает вектор
     *
     * @param ppid
     * @param criteriaSearch
     * @param textSearch
     * @param checkbox
     * @return
     */
    public Vector openTree(int ppid, String criteriaSearch, Object textSearch, boolean checkbox) {
        Vector rows = new Vector();
        String sql = "";
        System.out.println("openTree");
        if (checkbox) sql = "Select distinct nar,sar from nsi_kld, _ost_sklad, nsi_sd, nsi_pkd " +
                "       	Where " + criteriaSearch + " " + textSearch + " and (nsi_kld.ptk = nsi_pkd.ptk or nsi_kld.ptk = 0) and nsi_kld.kod = nsi_sd.kod " +
                "                and nsi_sd.kod1 = _ost_sklad.kod_izd" +
                "	              and _ost_sklad.kol > 0" +
                "          Order by sar ";
        else sql = "Select distinct nar,sar from nsi_kld, _ost_sklad, nsi_sd, nsi_pkd " +
                "   Where " + criteriaSearch + " " + textSearch + " and nsi_kld.kod = nsi_sd.kod " +
                "         and nsi_sd.kod1 = _ost_sklad.kod_izd and (nsi_kld.ptk = nsi_pkd.ptk or nsi_kld.ptk = 0) " +
                " UNION " +
                " Select distinct nar,sar from nsi_kld, nsi_pkd, nsi_sd " +
                "   Where " + criteriaSearch + " " + textSearch + " and nsi_sd.kod = nsi_kld.kod" +
                "         and (nar LIKE '" + MyReportsModule.sYear + "%' OR nar LIKE '" + MyReportsModule.eYear + "%') and (nsi_kld.ptk = nsi_pkd.ptk or nsi_kld.ptk = 0)" +
                " Order by sar";
        try (PreparedStatement ps = getConnection().prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Vector newRow = new Vector();
                newRow.addElement(rs.getInt(2));
                newRow.addElement(ppid);
                newRow.addElement("");
                newRow.addElement("");
                newRow.addElement(rs.getObject(1));
                rows.addElement(newRow);
            }
        } catch (Exception e) {
            log.severe("Ошибка openTree(): " + e.getMessage());
        }
        return rows;
    }

    public Vector openTreeFasony(int ppid, String criteriaSearch, Object textSearch, boolean checkbox) {
        Vector rows = new Vector();
        String sql = "";
        if (checkbox) sql = "Select distinct fas from nsi_kld,nsi_pkd,_ost_sklad,nsi_sd " +
                " Where " + criteriaSearch + " " + textSearch + " and nsi_kld.ptk=nsi_pkd.ptk and sar like '" + ppid + "%' " +
                "       and nsi_kld.kod = nsi_sd.kod and nsi_sd.kod1 = _ost_sklad.kod_izd  and _ost_sklad.kol > 0" +
                "  Order by fas";
        else sql = "Select distinct fas from nsi_kld,nsi_pkd,_ost_sklad,nsi_sd " +
                "   Where " + criteriaSearch + " " + textSearch + " and nsi_kld.ptk=nsi_pkd.ptk " +
                "       and nsi_kld.kod = nsi_sd.kod and nsi_sd.kod1 = _ost_sklad.kod_izd" +
                " UNION " +
                " Select distinct fas from nsi_kld,nsi_pkd,nsi_sd " +
                "   Where " + criteriaSearch + " " + textSearch + " and nsi_sd.kod = nsi_kld.kod " +
                "         and (nar LIKE '" + MyReportsModule.sYear + "%' OR nar LIKE '" + MyReportsModule.eYear + "%')" +
                " Order by fas";
        try (PreparedStatement ps = getConnection().prepareStatement(sql)) {

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Vector newRow = new Vector();
                newRow.addElement(0);
                newRow.addElement(ppid);
                newRow.addElement("");
                newRow.addElement("");
                newRow.addElement(rs.getObject(1));
                rows.addElement(newRow);
            }
        } catch (Exception e) {
            log.severe("Ошибка openTree(): " + e.getMessage());
        }
        return rows;
    }

    /**
     * поиск детей корневой ветки в БД
     *
     * @param pid
     * @param name
     * @return true/false
     */
    public boolean searchChildren(int pid, String name, String nptk, String criteriaSearch, Object textSearch, boolean checkbox) {
        boolean rezult = false;
        String sql = "";
        String sqlnptk = "";

        if (nptk.equals("Без категории")) sqlnptk = " and nsi_kld.ptk = 0 ";
        else sqlnptk = " and nsi_kld.ptk = nsi_pkd.ptk and nptk like '" + nptk + "%' ";

        if (!criteriaSearch.isEmpty()) {
            if (checkbox) sql = "Select distinct sar, fas from nsi_kld, _ost_sklad, nsi_sd, nsi_pkd " +
                    " 	Where " + criteriaSearch + " " + textSearch + " and sar like '" + pid + "%' and fas like '" + name + "' " + sqlnptk + "  " +
                    "	      and nsi_kld.kod = nsi_sd.kod and nsi_sd.kod1 = _ost_sklad.kod_izd" +
                    "        and _ost_sklad.kol > 0" +
                    "  Order by sar  ";
            else sql = "Select distinct sar, nar from nsi_kld, _ost_sklad, nsi_sd, nsi_pkd " +
                    "    Where " + criteriaSearch + " " + textSearch + " and sar like '" + pid + "%' and fas like '" + name + "' and nsi_kld.kod = nsi_sd.kod " + sqlnptk + " " +
                    "        and nsi_sd.kod1 = _ost_sklad.kod_izd " +
                    "    UNION" +
                    " Select distinct sar, nar from nsi_kld, nsi_pkd, nsi_sd " +
                    "    Where " + criteriaSearch + " " + textSearch + " and sar like '" + pid + "%' and fas like '" + name + "' and nsi_sd.kod = nsi_kld.kod  " + sqlnptk + " " +
                    "        and (nar LIKE '" + MyReportsModule.sYear + "%' OR nar LIKE '" + MyReportsModule.eYear + "%') " +
                    " Order by sar";
        } else {
            if (pid < 4000 || pid > 40000000)
                sql = "Select top 1 sar from nsi_kld where sar like'" + pid + "%'";
            else
                sql = "SELECT top 1 nptk from nsi_kld,nsi_pkd where nsi_kld.ptk=nsi_pkd.ptk and sar in(Select distinct sar From nsi_kld where sar like '" + pid + "%') " +
                        " and nptk in (Select distinct nptk from nsi_pkd where nptk like '" + name + "%')";

            if (pid == 0)
                sql = "Select top 1 fas from nsi_kld where fas like'" + name + "%'";
        }

        try (PreparedStatement ps = getConnection().prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            if (rs.next()) rezult = true;
        } catch (Exception e) {
            log.severe("Ошибка searchChildren(): " + e.getMessage());
        }

        if (pid == 40) rezult = true;

        return rezult;
    }

    public Vector searchSar(String criteriaSearch, Object textSearch, boolean checkbox) {
        Vector sar = new Vector();
        String sql = "Select distinct sar, nar from nsi_kld, _ost_sklad, nsi_sd, nsi_pkd " +
                "   Where " + criteriaSearch + " " + textSearch + " and nsi_kld.kod = nsi_sd.kod " +
                "         and nsi_sd.kod1 = _ost_sklad.kod_izd and (nsi_kld.ptk = nsi_pkd.ptk or nsi_kld.ptk = 0) " +
                " UNION " +
                " Select distinct sar, nar from nsi_kld, nsi_pkd, nsi_sd" +
                "   Where " + criteriaSearch + " " + textSearch + " and nsi_sd.kod = nsi_kld.kod " +
                "         and (nar LIKE '" + MyReportsModule.sYear + "%' OR nar LIKE '" + MyReportsModule.eYear + "%') and (nsi_kld.ptk = nsi_pkd.ptk or nsi_kld.ptk = 0)" +
                " Order by sar";
        if (checkbox) {
            sql = "Select distinct sar, nar from nsi_kld, _ost_sklad, nsi_sd, nsi_pkd " +
                    "       	Where " + criteriaSearch + " " + textSearch + " and (nsi_kld.ptk = nsi_pkd.ptk or nsi_kld.ptk = 0) and nsi_kld.kod = nsi_sd.kod " +
                    "                and nsi_sd.kod1 = _ost_sklad.kod_izd" +
                    "	              and _ost_sklad.kol > 0" +
                    "          Order by sar ";
        }
        try (PreparedStatement ps = getConnection().prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                sar.add(rs.getInt(1));
            }
        } catch (Exception e) {
            log.severe("Ошибка searchSar(): " + e.getMessage());
            JOptionPane.showMessageDialog(null, "Ошибка searchSar(): " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
        return sar;
    }

    public Map<Object, String> searchFas(String criteriaSearch, Object textSearch, boolean checkbox) {
        Map<Object, String> fas = new HashMap<>();
        String sql = "Select distinct sar, fas from nsi_kld, _ost_sklad, nsi_sd, nsi_pkd " +
                "   Where " + criteriaSearch + " " + textSearch + " and nsi_kld.kod = nsi_sd.kod " +
                "         and nsi_sd.kod1 = _ost_sklad.kod_izd and (nsi_kld.ptk = nsi_pkd.ptk or nsi_kld.ptk = 0) " +
                " UNION " +
                " Select distinct sar, fas from nsi_kld, nsi_pkd, nsi_sd" +
                "   Where " + criteriaSearch + " " + textSearch + " and nsi_sd.kod = nsi_kld.kod " +
                "         and (nar LIKE '" + MyReportsModule.sYear + "%' OR nar LIKE '" + MyReportsModule.eYear + "%') and (nsi_kld.ptk = nsi_pkd.ptk or nsi_kld.ptk = 0)" +
                " Order by sar";
        if (checkbox)
            sql = "Select distinct sar, fas from nsi_kld, _ost_sklad, nsi_sd, nsi_pkd " +
                    "       	Where " + criteriaSearch + " " + textSearch + " and (nsi_kld.ptk = nsi_pkd.ptk or nsi_kld.ptk = 0) and nsi_kld.kod = nsi_sd.kod " +
                    "                and nsi_sd.kod1 = _ost_sklad.kod_izd" +
                    "	              and _ost_sklad.kol > 0" +
                    "          Order by sar ";
        try (PreparedStatement ps = getConnection().prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                fas.put(rs.getObject(1), rs.getString(2));
            }
        } catch (Exception e) {
            log.severe("Ошибка searchSar(): " + e.getMessage());
            JOptionPane.showMessageDialog(null, "Ошибка searchSar(): " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
        return fas;
    }

    public String getNameClient(int id) {
        String name_client = "";
        try (PreparedStatement ps = getConnection().prepareStatement("Select NAIM from s_klient where KOD=" + id + "")) {
            ResultSet rs = ps.executeQuery();
            while (rs.next())
                name_client = rs.getString(1).trim();
        } catch (Exception e) {
            log.severe("Ошибка getNameClient(): " + e.getMessage());
            JOptionPane.showMessageDialog(null, "Ошибка getNameClient(): " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
        return name_client;
    }

    public double[] getCena(int sar, String nar, int fason, int sort, int rost, int razmer, int idvaluta, int kurs) {
        double[] cena = new double[2];

        String sql = "Select distinct cno/kurs, nds from nsi_kld, nsi_pkd, nsi_sd, valuta1 " +
                " where nsi_kld.kod=nsi_sd.kod and sar = " + sar + " and nar = '" + nar + "'" +
                "   and fas = " + fason + " and srt = " + sort + " and rst = " + rost + " and rzm = " + razmer + "" +
                "   and valuta_id = " + idvaluta + " and data = (Select max(data) from valuta1 where valuta_id = " + idvaluta + ") ;";

        if (idvaluta == 0)
            sql = "Select distinct (cno/'" + kurs + "'), nds from nsi_kld, nsi_pkd, nsi_sd " +
                    " where nsi_kld.kod=nsi_sd.kod and sar = " + sar + " and nar = '" + nar + "'" +
                    "   and fas = " + fason + " and srt = " + sort + " and rst = " + rost + " and rzm = " + razmer + " ;";

        else if (idvaluta == 1)
            sql = "Select distinct cno, nds from nsi_kld, nsi_pkd, nsi_sd " +
                    " where nsi_kld.kod=nsi_sd.kod and sar = " + sar + " and nar = '" + nar + "'" +
                    "   and fas = " + fason + " and srt = " + sort + " and rst = " + rost + " and rzm = " + razmer + ";";
        try (PreparedStatement ps = getConnection().prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                cena[0] = BigDecimal.valueOf(rs.getFloat(1)).setScale(2, RoundingMode.HALF_UP).doubleValue(); //цена без НДС
                cena[1] = BigDecimal.valueOf(rs.getFloat(2)).setScale(2, RoundingMode.HALF_UP).doubleValue(); //НДС
            }
        } catch (Exception e) {
            log.severe("Ошибка getCena(): " + e.getMessage());
            JOptionPane.showMessageDialog(null, "Ошибка getCena(): " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
        return cena;
    }

    public Vector getValuta() {
        Vector valuta = new Vector();
        Vector tmp = new Vector();
        tmp.add(1);
        tmp.add("Белорусский рубль");
        tmp.add("бел. руб.");
        tmp.add(1);
        valuta.add(tmp);
        String sql = "Select kod, fullnaim, propis2, kurs  from valuta, valuta1 as V1 where kod = valuta_id and data = (Select max(data) from valuta1 as V2 where V1.valuta_id=V2.valuta_id) ";
        try (PreparedStatement ps = getConnection().prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                tmp = new Vector();
                tmp.add(rs.getInt("kod"));
                tmp.add(rs.getString("fullnaim").trim());
                tmp.add(rs.getString("propis2").trim());
                tmp.add(rs.getInt("kurs"));
                valuta.add(tmp);
            }
        } catch (Exception e) {
            log.severe("Ошибка getValuta(): " + e.getMessage());
            JOptionPane.showMessageDialog(null, "Ошибка getValuta(): " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
        return valuta;
    }

    public Vector getDataOtgruz(int idClient, Vector ndocs) {
        Vector rows = new Vector();

        String sql = "Select kod1, sar, nar, fas, nsi_sd.srt, ngpr, rst,rzm, otgruz2.ncw, sum(kol*kol_in_upack) from otgruz1, otgruz2, nsi_sd, nsi_kld" +
                "                         where ndoc COLLATE DATABASE_DEFAULT in (Select doc from #TempNDoc)" +
                "        			and otgruz2.doc_id in (Select item_id from otgruz1 where klient_id=(Select ITEM_ID from s_klient where KOD=" + idClient + ") and (status=0 or status=3))" +
                "              		and otgruz2.doc_id = otgruz1.item_id" +
                "              		and nsi_sd.kod1 = otgruz2.kod_izd" +
                "                    		and nsi_kld.kod = nsi_sd.kod" +
                "                  Group by kod1, sar, nar, fas, nsi_sd.srt, ngpr, rst,rzm, otgruz2.ncw, nsi_sd.kod1";
        try (Connection conn = getConnection();
             Statement st = conn.createStatement()) {
            if (!ndocs.isEmpty()) {
                st.execute("IF (not object_id('tempdb..#TempNDoc') IS NULL) DROP TABLE #TempNDoc CREATE TABLE #TempNDoc (doc VARCHAR(20) PRIMARY KEY);");

                for (int i = 0; i < ndocs.size(); i++) {
                    st.execute("INSERT INTO #TempNDoc VALUES ('" + ndocs.elementAt(i).toString() + "');");
                }

                ResultSet rs = st.executeQuery(sql);

                while (rs.next()) {
                    Vector tmp = new Vector();
                    tmp.add(rs.getInt(1) + "" + rs.getString(9).trim());           // код+цвет
                    tmp.add(rs.getInt(2));                                     // шифр
                    tmp.add(rs.getString(3).trim());                           // артикул
                    tmp.add(rs.getInt(4));                                     // модель
                    tmp.add(rs.getInt(5));                                     // сорт
                    tmp.add(rs.getString(6).trim());                           // название изд.
                    tmp.add(rs.getInt(7));                                     // рост
                    tmp.add(rs.getInt(8));                                     // размер
                    tmp.add(rs.getString(9).trim());                           // цвет
                    tmp.add(rs.getInt(10));                                    // кол-во
                    rows.add(tmp);
                }
            }
        } catch (Exception e) {
            log.severe("Ошибка getDataOtgruz(): " + e.getMessage());
            JOptionPane.showMessageDialog(null, "Ошибка getDataOtgruz(): " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
        return rows;
    }

    public Vector getDataOtgruzDetails(int idClient, Vector ndocs, int sar, String nar, int fason, int sort, String ngpr, int rost, int razmer, String color) {
        Vector rows = new Vector();
        Object _sar = "-", _nar = "-", _fason = "-", _srt = "-", _ngpr = "-", _rost = "-", _razmer = "-", kod = 0, kol_vo = "-", percent = 100;
        String _color = "-";
        String sql = "Select kod1, sar, nar, fas, nsi_sd.srt, ngpr, rst, rzm, otgruz2.ncw, sum(kol*kol_in_upack)" +
                " from otgruz1,otgruz2,nsi_sd, nsi_kld" +
                "   where ndoc COLLATE DATABASE_DEFAULT in (Select doc from #TempNDoc)" +
                "          and otgruz2.doc_id in (Select item_id from otgruz1 where klient_id = (Select ITEM_ID from s_klient where KOD=" + idClient + ") and (status=0 or status=3)) " +
                "		and otgruz2.doc_id = otgruz1.item_id" +
                "		and nsi_sd.kod1 = otgruz2.kod_izd" +
                "		and nsi_kld.kod = nsi_sd.kod" +
                "		and nsi_sd.kod1 in (Select nsi_sd.kod1 from nsi_kld,nsi_sd where sar = " + sar + " and nar = '" + nar + "' and fas = " + fason + " and srt=" + sort + " and rst=" + rost + " and rzm=" + razmer + " and nsi_kld.kod = nsi_sd.kod)" +
                "		and otgruz2.ncw = '" + color + "' " +
                " Group by kod1, sar, nar, fas, nsi_sd.srt, ngpr, rst,rzm, otgruz2.ncw, nsi_sd.kod1";
        try (PreparedStatement ps = getConnection().prepareStatement(sql)) {
            if (!ndocs.isEmpty()) {
                ResultSet rs = ps.executeQuery(sql);

                if (rs.next()) {
                    kod = rs.getInt(1);
                    _sar = sar;
                    _nar = nar;
                    _fason = fason;
                    _srt = sort;
                    _ngpr = ngpr;
                    _rost = rost;
                    _razmer = razmer;
                    _color = rs.getString(9).trim();
                    kol_vo = rs.getInt(10);
                }
            }
            rows.add(percent);
            rows.add(kod + "" + _color);
            rows.add(_sar);
            rows.add(_nar);
            rows.add(_fason);
            rows.add(_srt);
            rows.add(_ngpr);
            rows.add(_rost);
            rows.add(_razmer);
            rows.add(_color);
            rows.add(kol_vo);
        } catch (Exception e) {
            log.severe("Ошибка getDataOtgruzDetails(): " + e.getMessage());
            JOptionPane.showMessageDialog(null, "Ошибка getDataOtgruzDetails(): " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
        return rows;
    }

    public boolean testNDoc(String ndoc, int idClient) {
        boolean rezalt = false;
        String sql = "SELECT date from otgruz1 where ndoc like " + ndoc + " and klient_id = (Select ITEM_ID from s_klient where KOD=" + idClient + ") and (status=0 or status=3)";
        try (PreparedStatement ps = getConnection().prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            if (rs.next()) rezalt = true;
        } catch (Exception e) {
            log.severe("Ошибка testNDoc(): " + e.getMessage());
            JOptionPane.showMessageDialog(null, "Ошибка testNDoc(): " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
        return rezalt;
    }

    public boolean testNDoc(String ndoc) {
        boolean rezalt = false;
        String sql = "SELECT date from otgruz1 where ndoc like '" + ndoc + "' ";
        try (PreparedStatement ps = getConnection().prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            if (rs.next()) rezalt = true;
        } catch (Exception e) {
            log.severe("Ошибка testNDoc(): " + e.getMessage());
            JOptionPane.showMessageDialog(null, "Ошибка testNDoc(): " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
        return rezalt;
    }

    public Vector getNDocs(Object idClient) {
        Vector ndocs = new Vector();
        String sql = "SELECT TOP 50 ndoc,date FROM otgruz1 WHERE klient_id = (SELECT ITEM_ID FROM s_klient WHERE KOD = " + idClient + ") AND (status = 0 OR status = 3)  AND operac NOT like 'Возврат%' Order by date desc, ndoc desc";
        try (PreparedStatement ps = getConnection().prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Vector tmp = new Vector();
                tmp.add(rs.getString(1).trim());
                tmp.add(new SimpleDateFormat("dd-MM-yyyy").format(rs.getDate(2)));
                ndocs.add(tmp);
            }
        } catch (Exception e) {
            log.severe("Ошибка getNDoc(): " + e.getMessage());
            JOptionPane.showMessageDialog(null, "Ошибка getNDoc(): " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
        return ndocs;
    }

    public Vector testFas(int newFas) {
        Vector fas = new Vector();
        String sql = "Select distinct nar,sar from nsi_kld,nsi_pkd,_ost_sklad,nsi_sd " +
                "    where nsi_kld.ptk=nsi_pkd.ptk and fas = " + newFas + " and nsi_kld.kod = nsi_sd.kod and nsi_sd.kod1 = _ost_sklad.kod_izd" +
                " UNION " +
                " Select distinct nar,sar from nsi_kld,nsi_pkd " +
                "    where nsi_kld.ptk=nsi_pkd.ptk  and fas = " + newFas + " and (nar LIKE '" + MyReportsModule.sYear + "%' OR nar LIKE '" + MyReportsModule.eYear + "%')" +
                " Order by sar";
        try (PreparedStatement ps = getConnection().prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                fas.add(rs.getInt("sar") + " " + rs.getString("nar").trim() + ";\n ");
            }
        } catch (Exception e) {
            log.severe("Ошибка testFas(): " + e.getMessage());
            JOptionPane.showMessageDialog(null, "Ошибка testFas(): " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
        return fas;
    }

//    public Vector getStatisticOtgruzClient(int idClient, int kurs, String sDateSk, String eDateSk, String[][] vidIzd,
//                                           Vector otgruz, Vector zakaz, HashMap<String, String> map, HashMap<String, Boolean> flags, boolean f) {
//
//        Vector statis = new Vector();
//        String sqlsar = "";
//        String sdacha = "0";
//        String k = "0";
//        String key = "";
//        Statement st = null;
//        double kursValuta = 1;
//        Vector tmp = new Vector();
//        ArrayList tmplist = new ArrayList();
//        try {
//            st = conn.createStatement();
//            st.execute("IF (not object_id('tempdb..#otgruz_client') IS NULL) "
//                    + "DROP TABLE #otgruz_client CREATE TABLE  #otgruz_client "
//                    + "( sar numeric, fas numeric, kol numeric, cno numeric, cnr numeric);");
//
//            for (int i = 0; i < otgruz.size(); i++) {
//                Vector t = (Vector) otgruz.elementAt(i);
//                st.execute("INSERT INTO  #otgruz_client VALUES ("
//                        + Integer.parseInt(t.get(0).toString()) + " ,"
//                        + Integer.parseInt(t.get(1).toString()) + ","
//                        + (int) Double.parseDouble(t.get(2).toString()) + ","
//                        + (int) Double.parseDouble(t.get(3).toString()) + ","
//                        + (int) Double.parseDouble(t.get(4).toString()) + " );");
//            }
//
//            if (kurs != 0) kursValuta = kurs;
//            for (int i = 0; i < vidIzd.length; i++) {
//                if (i == 0) sqlsar = " and #otgruz_client.sar LIKE '" + vidIzd[0][0] + "'";
//                else
//                    sqlsar = " and #otgruz_client.sar LIKE '" + vidIzd[i][0] + "' and #otgruz_client.sar NOT LIKE '" + vidIzd[0][0] + "'";
//
//                ResultSet rs_ = st.executeQuery("SELECT nsi_kld.ngpr, #otgruz_client.fas, SUM(#otgruz_client.kol) AS kol, " +
//                        " SUM(#otgruz_client.cno * #otgruz_client.kol) AS summa, SUM(#otgruz_client.cnr * #otgruz_client.kol) AS summaall" +
//                        " From  nsi_kld, #otgruz_client " +
//                        "		where nsi_kld.sar =  #otgruz_client.sar " +
//                        "		  and nsi_kld.fas =  #otgruz_client.fas " + sqlsar +
//                        " GROUP BY nsi_kld.ngpr, #otgruz_client.fas" +
//                        " ORDER BY nsi_kld.ngpr, #otgruz_client.fas");
//
//                while (rs_.next()) {
//                    k = "0";
//                    key = map.get(vidIzd[i][0] + rs_.getObject(2).toString());
//                    if (key != null) {
//                        k = key;
//                        map.remove(vidIzd[i][0] + rs_.getObject(2).toString());
//                        map.put("-1", map.get(vidIzd[i][0] + rs_.getObject(2).toString()));
//                    }
//
//                    tmp = new Vector();
//                    tmp.add(vidIzd[i][1].toUpperCase());                        // ассортимент
//                    tmp.add(rs_.getString(1).trim().toLowerCase().replace("  ", " ").replace("  ", " "));  // наименование изд.
//                    tmp.add(rs_.getInt(2));                                      // модель
//                    tmp.add(k);                                                  // кол-во по заявкам
//                    tmp.add(rs_.getInt(3));                                      // кол-во по накладным
//                    tmp.add(new Double(rs_.getLong(4) / kursValuta));              // сумма
//                    tmp.add(new Double(rs_.getLong(5) / kursValuta));              // сумма с ндс
//
//                    if (f) {
//                        sdacha = "0";
//                        tmplist = sdachaNaSklad(sDateSk, eDateSk, 0, rs_.getInt(2), "", 0, "", 0, 0, "", flags, true);
//                        if (!tmplist.isEmpty()) {
//                            Vector t = (Vector) tmplist.get(0);
//                            sdacha = t.elementAt(2).toString();
//                        }
//                        tmp.add(sdacha);                                           // сдано на склад
//                    }
//                    statis.add(tmp);
//                }
//
//                Set<String> keys = map.keySet();
//                Object[] arrayKeys = keys.toArray();
//                for (Object key_ar : arrayKeys) {
//                    if (!key_ar.toString().equals("-1") && key_ar.toString().substring(0, 8).equals(vidIzd[i][0])) {
//                        for (int j = 0; j < zakaz.size(); j++) {
//                            Vector t = (Vector) zakaz.get(j);
//                            if (t.elementAt(4).toString().equals(vidIzd[i][0]) &&
//                                    t.elementAt(2).toString().equals(key_ar.toString().substring(8))) {
//                                tmp = new Vector();
//                                tmp.add(vidIzd[i][1].toUpperCase());                        // ассортимент
//                                tmp.add(t.elementAt(1).toString());                         // наименование изд.
//                                tmp.add(t.elementAt(2).toString());                         // модель
//                                tmp.add(t.elementAt(3));                                    // кол-во по заявкам
//                                tmp.add(0);                                                 // кол-во по накладным
//                                tmp.add(0);                                                 // сумма
//                                tmp.add(0);                                                 // сумма с ндс
//
//                                if (f) {
//                                    sdacha = "0";
//                                    tmplist = sdachaNaSklad(sDateSk, eDateSk, 0,
//                                            Integer.parseInt(t.elementAt(2).toString()), "", 0, "", 0, 0, "", flags, true);
//                                    if (!tmplist.isEmpty()) {
//                                        Vector tt = (Vector) tmplist.get(0);
//                                        sdacha = tt.elementAt(2).toString();
//                                    }
//                                    tmp.add(sdacha);                                          // сдано на склад
//                                }
//                                statis.add(tmp);
//
//                                map.remove(key_ar.toString());
//                                map.put("-1", map.get(key_ar.toString()));
//                            }
//                        }
//                    }
//                }
//            }
//        } catch (Exception e) {
//            log.severe("Ошибка getStatisticOtgruzClient(): " + e.getMessage());
//            JOptionPane.showMessageDialog(null, "Ошибка getStatisticOtgruzClient(): " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
//        }
//        return statis;
//    }

    public Vector getSaleZaPeriod(String ngpr, String sDate, String eDate, boolean checkbox) {
        Vector sale = new Vector();
        String sql = "select fas, ngpr, sum(otgruz2.kol*otgruz2.kol_in_upack) as otgr_kol" +
                " from otgruz1,otgruz2, nsi_sd, nsi_kld" +
                " where ngpr like '?%' " +
                "      and nsi_sd.kod1 = otgruz2.kod_izd" +
                "      and nsi_kld.kod = nsi_sd.kod" +
                "      and otgruz2.doc_id = otgruz1.item_id" +
                "                    and (otgruz1.date BETWEEN ? and ?)" +
                "      and (otgruz1.status = 0 or otgruz1.status = 3) " +
                "      and (otgruz1.operac <> 'Возврат от покупателя' and otgruz1.operac <> 'Возврат из розницы')" +
                " Group by fas, ngpr" +
                " Order by otgr_kol desc, fas";
        String ssql = "select sum(_ost_sklad.kol) as ost_kol from _ost_sklad, nsi_kld, nsi_sd" +
                " where ngpr like '?' " +
                "	and nsi_kld.fas = ? " +
                "	and _ost_sklad.kod_izd = nsi_sd.kod1" +
                "	and nsi_sd.kod = nsi_kld.kod" +
                " Group by fas, ngpr";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, ngpr.replace(" ", "%"));
            ps.setString(2, sDate);
            ps.setString(3, eDate);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Vector tmp = new Vector();
                tmp.add(rs.getString(2).trim());
                tmp.add(rs.getInt(1));
                tmp.add(rs.getInt(3));
                if (checkbox) {
                    PreparedStatement fps = conn.prepareStatement(ssql);
                    fps.setString(1, rs.getString(2).trim());
                    fps.setInt(2, rs.getInt(1));
                    ResultSet frs = ps.executeQuery();
                    if (frs.next())
                        tmp.add(frs.getInt("ost_kol"));
                    else
                        tmp.add("0");
                }
                sale.add(tmp);
            }
        } catch (Exception e) {
            log.severe("Ошибка getSaleZaPeriod()" + e.getMessage());
            JOptionPane.showMessageDialog(null, "Ошибка getSaleZaPeriod(): " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
        return sale;
    }

    /* **************************************************************************
     * ********************************NSI****************************************
     * ***************************************************************************/

    public void updateKTClient(ArrayList<Object[]> rows) {
        long kt;
        long kc;

        String query = "update s_klient set KT = ? where KT = 0 and KOD = ? ";
        try (PreparedStatement ps = getConnection().prepareStatement(query)) {

            for (Object[] o : rows) {
                kt = (Long) o[0];
                kc = (Long) o[1];
                if (kc <= 900) continue;
                ps.setLong(1, kt);
                ps.setLong(2, kc);
                ps.addBatch();
            }
            ps.executeBatch();
        } catch (Exception e) {
            log.severe("Ошибка при обновлении кодов территорий клиентов " + e.getMessage());
        }
    }

    public Vector getNaclForArticle(String article, String beginDate, String endDate) throws ParseException {
        long sDate = convertDateStrToLong(beginDate);
        long eDate = convertDateStrToLong(endDate);
        Vector users = new Vector();
        int i = 0;
        kol = 0;
        kolForSdachaNaUpack = 0;
        final DateFormat df = new java.text.SimpleDateFormat("dd.MM.yyyy");
        String query = "SELECT res2.ndoc, res2.[date], res2.kol, res2.kolkopml, res1.rzm_print, res1.nar, res1.ngpr " +
                " FROM (SELECT nsi_kld.nar, nsi_kld.ngpr, nsi_kld.kod, nsi_sd.kod AS EXPR1, nsi_sd.kod1, nsi_sd.rzm_print " +
                " FROM nsi_kld INNER JOIN nsi_sd ON nsi_kld.kod = nsi_sd.kod " +
                " WHERE (nsi_kld.nar = ?)) AS res1 INNER JOIN " +
                " (SELECT vnperem1.[date], vnperem1.ndoc, vnperem1.item_id, vnperem2.doc_id, vnperem2.kod_izd, " +
                " vnperem2.kol, (vnperem2.kol_in_upack * vnperem2.kol) AS kolkopml " +
                " FROM vnperem1 INNER JOIN vnperem2 ON vnperem1.item_id = vnperem2.doc_id " +
                " WHERE (vnperem1.status = 0) AND (vnperem1.datevrkv >= ?) AND (vnperem1.datevrkv < ?)) AS res2 " +
                " ON res1.kod1 = res2.kod_izd " +
                " ORDER BY res2.ndoc, res2.[date], res2.kol, res2.kolkopml, res1.rzm_print, res1.nar, res1.ngpr";
        try (PreparedStatement ps = getConnection().prepareStatement(query)) {
            ps.setString(1, article);
            ps.setDate(2, new java.sql.Date(sDate));
            ps.setDate(3, new java.sql.Date(eDate + DAY));
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Vector user = new Vector();
                user.add(++i);
                user.add(rs.getString(1).trim());
                user.add(df.format(rs.getDate(2)));
                user.add(rs.getLong(3));
                user.add(rs.getLong(4));
                user.add(rs.getString(5).trim());
                user.add(rs.getString(6).trim());
                user.add(rs.getString(7).trim());
                users.add(user);
                kol += rs.getLong(3);
                kolForSdachaNaUpack += rs.getLong(3) * rs.getLong(4);
            }
        } catch (Exception e) {
            log.severe("Ошибка полученя списка cдачи на склад " + e.getMessage());
        }
        return users;
    }

    public Vector getNaclForArticle(String beginDate, String endDate) throws ParseException {
        long sDate = convertDateStrToLong(beginDate);
        long eDate = convertDateStrToLong(endDate);
        Vector users = new Vector();
        int i = 0;
        kol = 0;
        kolForSdachaNaUpack = 0;
        String query = "SELECT res1.nar, res1.ngpr, SUM(res2.kol) AS kolUp, SUM(res2.kolkopml) AS Summ " +
                "FROM (SELECT nsi_kld.nar, nsi_kld.ngpr, nsi_kld.kod, nsi_sd.kod AS EXPR1, nsi_sd.kod1, nsi_sd.rzm_print " +
                "FROM nsi_kld INNER JOIN " +
                "nsi_sd ON nsi_kld.kod = nsi_sd.kod) res1 INNER JOIN " +
                "(SELECT vnperem1.[date], vnperem1.ndoc, vnperem1.item_id, vnperem2.doc_id, vnperem2.kod_izd, vnperem2.kol, vnperem2.kol_in_upack, " +
                "(vnperem2.kol_in_upack * vnperem2.kol) AS kolkopml " +
                "FROM vnperem1 INNER JOIN " +
                "vnperem2 ON vnperem1.item_id = vnperem2.doc_id " +
                "WHERE (vnperem1.status = 0) AND (vnperem1.datevrkv >= ?) AND (vnperem1.datevrkv < ?)) " +
                "res2 ON res1.kod1 = res2.kod_izd " +
                "GROUP BY res1.nar, res1.ngpr";
        try (PreparedStatement ps = getConnection().prepareStatement(query)) {
            ps.setDate(1, new java.sql.Date(sDate));
            ps.setDate(2, new java.sql.Date(eDate + DAY));
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Vector user = new Vector();
                user.add(++i);
                user.add(rs.getString(1).trim());
                user.add(rs.getString(2).trim());
                user.add(rs.getLong(3));
                user.add(rs.getLong(4));
                users.add(user);
                kol += rs.getLong(3);
                kolForSdachaNaUpack += rs.getLong(4);
            }
        } catch (Exception e) {
            log.severe("Ошибка полученя списка cдачи на склад " + e.getMessage());
        }
        return users;
    }

    public Vector getNaclNoMassa(String beginDate, String endDate) throws ParseException {
        long sDate = convertDateStrToLong(beginDate);
        long eDate = convertDateStrToLong(endDate);
        Vector users = new Vector();
        int i = 0;
        kol = 0;
        kolForSdachaNaUpack = 0;
        String query = "SELECT res1.fas, res1.sar, res1.nar, res1.ngpr " +
                "FROM (SELECT nsi_kld.fas, nsi_kld.sar, nsi_kld.nar, nsi_kld.ngpr, nsi_kld.kod, nsi_sd.kod AS EXPR1, nsi_sd.kod1, nsi_sd.rzm_print, nsi_sd.massa " +
                "FROM nsi_kld INNER JOIN " +
                "nsi_sd ON nsi_kld.kod = nsi_sd.kod " +
                "WHERE nsi_sd.massa = 0) res1 INNER JOIN " +
                "(SELECT vnperem1.[date], vnperem1.ndoc, vnperem1.item_id, vnperem2.doc_id, vnperem2.kod_izd, vnperem2.kol, vnperem2.kol_in_upack, " +
                "(vnperem2.kol_in_upack * vnperem2.kol) AS kolkopml " +
                "FROM vnperem1 INNER JOIN " +
                "vnperem2 ON vnperem1.item_id = vnperem2.doc_id " +
                "WHERE (vnperem1.status = 0) AND (vnperem1.datevrkv >= ?) AND (vnperem1.datevrkv < ?)) " +
                "res2 ON res1.kod1 = res2.kod_izd " +
                "GROUP BY res1.fas, res1.nar, res1.sar, res1.ngpr";
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
                users.add(user);
            }
        } catch (Exception e) {
            log.severe("Ошибка полученя списка cдачи на склад " + e.getMessage());
        }
        return users;
    }

    /**
     * Метод отбора накладных по периоду, типу накладной и экспорту
     *
     * @author Andy
     */
    public Vector getNakl(Date dateBegin, Date dateEnd, int typeTTN, boolean export) {
        Vector v = new Vector();
        String operac = "";

        String query = "select date, ndoc, operac, kpl, NAIM, summa, summa_nds, summa_all, status from (select date, ndoc, operac, kpl, summa, summa_nds, summa_all, "
                + "status = case "
                + "when status = 0 then 'Закрыт' "
                + "when status = 1 then 'Удалён' "
                + "when status = 2 then 'Непонятно =)' "
                + "when status = 3 then 'Формируется' "
                + "end "
                + " from otgruz1 where ((date >= CONVERT(DATETIME, ?, 102))and(date <= CONVERT(DATETIME, ?, 102)))";

        if (typeTTN > 0) {
            query += "and(operac=?)";
            switch (typeTTN) {
                case 1: {
                    operac = "Отгрузка покупателю";
                    break;
                }
                case 2: {
                    operac = "Возврат от покупателя";
                    break;
                }
                case 3: {
                    operac = "Перемещение в розницу";
                    break;
                }
                case 4: {
                    operac = "Возврат из розницы";
                    break;
                }
            }
        }

        if (export) {
            query += "and(export=1)";
        }

        query += ") as t1 join (select KOD, NAIM from s_klient)  as t2 on t1.kpl = t2.KOD order by date";

        try (PreparedStatement ps = getConnection().prepareStatement(query)) {
            ps.setDate(1, new java.sql.Date(dateBegin.getTime()));
            ps.setDate(2, new java.sql.Date(dateEnd.getTime()));
            if (typeTTN > 0) ps.setString(3, operac);
            ResultSet rs = ps.executeQuery();
            PDB pdb = new PDB();
            while (rs.next()) {
                Vector vv = new Vector();
                vv.add(rs.getDate(1));
                vv.add(rs.getString(2).trim());
                vv.add(rs.getString(3).trim());
                vv.add(rs.getLong(4));
                vv.add(rs.getString(5).trim());
                vv.add(String.format("%.2f", rs.getDouble(6)));
                vv.add(String.format("%.2f", rs.getDouble(7)));
                vv.add(String.format("%.2f", rs.getDouble(8)));
                vv.add(rs.getString(9).trim());
                vv.add(pdb.getNDocOtgruz(rs.getString(2).trim()));
                v.add(vv);
            }
        } catch (Exception e) {
            log.severe("Ошибка создания списка электронных накладных "
                    + e.getMessage());
        }
        return v;
    }

    /**
     * Метод возвращает реквизиты стоимостей по накладной
     *
     * @author Andy
     **/
    public ResultTTN getTTNInfo(String ttn) {
        ResultTTN result = new ResultTTN();
        String query = "SELECT SUM(o2.kol*o2.kol_in_upack) as total, SUM(o2.summa)as summa_bel, SUM(o2.summa_nds)as summa_nds_bel, SUM(o2.itogo)as itogo_bel, "
                + "SUM(o2.summav)as summa_valuta, SUM(o2.summa_ndsv)as summa_nds_valuta, SUM(o2.itogov)as itogo_valuta, val.kod, val.naim "
                + "FROM otgruz1 o1 "
                + "left join (select kod, naim from valuta )as val on o1.valuta_id = val.kod "
                + "left join otgruz2 as o2 on o1.item_id = o2.doc_id "
                + "where ndoc= ? GROUP BY val.kod,val.naim ";
        try (PreparedStatement ps = getConnection().prepareStatement(query)) {
            ps.setString(1, ttn);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                result.setCountItems(rs.getInt(1));

                result.setSummaRub(rs.getLong(2));
                result.setSummaRubNDS(rs.getLong(3));
                result.setSummaRubItogo(rs.getLong(4));

                result.setSummaValuta(rs.getDouble(5));
                result.setSummaValutaNDS(rs.getDouble(6));
                result.setSummaValutaItogo(rs.getDouble(7));

                result.setValutaID(rs.getInt(8));
                result.setValutaName(rs.getString(9));
            }
        } catch (Exception ex) {
            log.severe("Ошибка в DB getTTNInfo" + ex.getMessage());
        }
        return result;
    }

    /**
     * Получение детальной информации по накладной (Новая версия)
     *
     * @param nomer номер кодумента
     * @author Andy
     * @version 0.1.02-04-2014
     */
    public Vector getTTNAllDescr(String nomer) {
        Vector v = new Vector();
        String query = "select fas, nar, ngpr, rzm_print = case when rzm_print is null then '' else rzm_print end, srt, ncw, sum(kol), cena, cenav, kod_izd, doc_id,eancode, scan from "
                + " (select item_id from otgruz1 where ndoc = ?)as t "
                + " left join (select ncw, kol*kol_in_upack as kol, cena,cenav, kod_izd, doc_id,eancode,scan from otgruz2 )as t1 on t.item_id = t1.doc_id"
                + " left join (select rzm_print, rst, rzm, srt, kod, kod1 from nsi_sd) as t3 on t1.kod_izd = t3.kod1"
                + " left join (select ngpr, fas, nar, sar, kod from nsi_kld) as t2 on t3.kod = t2.kod"
                + " GROUP BY t2.fas, t2.nar, t2.ngpr, t3.rzm_print, t3.srt, t1.ncw, t1.cena, t1.cenav, t2.sar, t1.kod_izd, t1.doc_id, t1.eancode, t1.scan"
                + " ORDER BY t2.sar,t2.nar, t2.ngpr, t1.ncw, t3.rzm_print, t3.srt";
        try (PreparedStatement ps = getConnection().prepareStatement(query)) {
            ps.setString(1, nomer);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Vector vv = new Vector();
                vv.add(rs.getString(1));
                vv.add(rs.getString(2).trim());
                vv.add(rs.getString(3).trim());
                vv.add(rs.getString(4).trim());
                vv.add(rs.getLong(5));
                vv.add(rs.getString(6).trim());
                vv.add(rs.getLong(7));
                vv.add(rs.getLong(8));
                vv.add(rs.getDouble(9));
                vv.add(rs.getLong(10));
                vv.add(rs.getLong(11));
                vv.add(rs.getString(12));
                vv.add(rs.getLong(13));

                v.add(vv);
            }
        } catch (Exception e) {
            log.severe("Ошибка создания списка изделий в накладной "
                    + e.getMessage());
        }
        return v;
    }

    /**
     * Метод получает код контрагента по номеру накладной
     *
     * @author Andy
     */
    public int getClientID(String nomer) {
        int v = 0;
        String query = "select kpl from otgruz1 where ndoc=?";
        try (PreparedStatement ps = getConnection().prepareStatement(query)) {
            ps.setString(1, nomer);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                v = rs.getInt(1);
            }
        } catch (Exception e) {
            log.severe("Ошибка получение ID клиента "
                    + e.getMessage());
        }
        return v;
    }

    /**
     * Метод возвращает список накладных, в которых содержится товар соответствующий EAN-коду
     *
     * @author Andy
     */
    public Vector getDocListByItem(String eanItem, int kodClient, Date dateBegin, Date dateEnd) {
        Vector v = new Vector();

        String query = "SELECT dbo.otgruz1.ndoc, dbo.otgruz1.[date] AS date_doc, dbo._put_list.[date] AS date_otgr, dbo.s_klient.NAIM, dbo.otgruz2.cena, dbo.otgruz2.cenav "
                + "FROM dbo.otgruz2 INNER JOIN dbo.otgruz1 ON dbo.otgruz2.doc_id = dbo.otgruz1.item_id "
                + "LEFT OUTER JOIN dbo.s_klient ON dbo.otgruz1.kpl = dbo.s_klient.KOD "
                + "LEFT OUTER JOIN dbo._put_list ON dbo.otgruz1.item_id = dbo._put_list.ttn_id "
                + "WHERE (dbo.otgruz2.eancode = ?) AND (dbo.otgruz1.kpl = ?) AND (dbo.otgruz1.operac = 'Отгрузка покупателю')"
                + "AND ((dbo.otgruz1.date >= CONVERT(DATETIME, ?, 102))and(dbo.otgruz1.date <= CONVERT(DATETIME, ?, 102))) "
                + "ORDER BY dbo.otgruz1.[date] DESC";
        try (PreparedStatement ps = getConnection().prepareStatement(query)) {

            ps.setString(1, eanItem);
            ps.setInt(2, kodClient);
            ps.setDate(3, new java.sql.Date(dateBegin.getTime()));
            ps.setDate(4, new java.sql.Date(dateEnd.getTime()));

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Vector vv = new Vector();
                vv.add(rs.getString(1));
                vv.add(rs.getDate(2));
                vv.add(rs.getDate(3));
                vv.add(rs.getString(4).trim());
                vv.add(rs.getLong(5));
                vv.add(rs.getDouble(6));
                v.add(vv);
            }
        } catch (Exception e) {
            log.severe("Ошибка создания списка документов  "
                    + e.getMessage());
        }
        return v;
    }

    /**
     * Метод изменяет цену позиции в накладной и расчитывает по ней итоги
     *
     * @param docid   Идентификатор документа (ТТН)
     * @param eancode Код идентификатор позиции
     * @param value   Значение цены
     * @param valuta  если true, то значение цены в валюте
     */
    public void setPricePositionTTN(Long docid, Long eancode, Object value, boolean valuta) throws Exception {
        String query;
        if (valuta) {
            query = "update otgruz2 set cenav = ?, summav = ? * kol* kol_in_upack, summa_ndsv = ? * kol* kol_in_upack *nds/100, itogov = ? * kol* kol_in_upack *nds/100 + ? * kol* kol_in_upack where doc_id = ? and eancode = ?";
        } else {
            query = "update otgruz2 set cena = ?, summa = ? * kol* kol_in_upack, summa_nds = ? * kol* kol_in_upack *nds/100, itogo = ? * kol* kol_in_upack *nds/100 + ? * kol* kol_in_upack where doc_id = ? and eancode = ?";
        }

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            conn.setAutoCommit(false);
            if (valuta) {
                ps.setDouble(1, (Double) value);
                ps.setDouble(2, (Double) value);
                ps.setDouble(3, (Double) value);
                ps.setDouble(4, (Double) value);
                ps.setDouble(5, (Double) value);
            } else {
                ps.setLong(1, (Long) value);
                ps.setLong(2, (Long) value);
                ps.setLong(3, (Long) value);
                ps.setLong(4, (Long) value);
                ps.setLong(5, (Long) value);
            }

            ps.setLong(6, docid);
            ps.setLong(7, eancode);

            ps.executeUpdate();
            conn.commit();
        } catch (Exception e) {
            log.severe("Ошибка изменении стоимости изделий в накладной "
                    + e.getMessage());
        }
    }

    public int getSarByModelNumber(String model) {
        String query = "SELECT TOP 1 sar FROM dbo.nsi_kld WHERE (fas = ?) ORDER BY datevrkv DESC";
        try (PreparedStatement ps = getConnection().prepareStatement(query)) {
            ps.setString(1, model.trim());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                if (!rs.getString(1).isEmpty()) {
                    return rs.getInt(1);
                }
            }
        } catch (Exception e) {
            log.severe("Ошибка getSarByModelNumber " + e.getMessage());
        }
        return 0;
    }

    private CustomerInfoBean getCustomerByDocumentNumber(String documentNumber) {
        CustomerInfoBean customer = new CustomerInfoBean();
        String query = "SELECT kpl, s_klient.NAIM, s_klient.UNN "
                + "from otgruz1 "
                + "LEFT JOIN s_klient on s_klient.KOD = otgruz1.kpl "
                + "WHERE ndoc = ?";
        try (PreparedStatement ps = getConnection().prepareStatement(query)) {
            ps.setString(1, documentNumber);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                customer.setCode(rs.getInt("KPL"));
                customer.setName(rs.getString("NAIM").trim());
                customer.setUnp(rs.getString("UNN").trim());
            }

        } catch (Exception e) {
            log.severe("Ошибка getCustomerByDocumentNumber для " + documentNumber + " " + e.getMessage());
        }
        return customer;
    }

    private PropInvoiceBean getInvoicesInfo(String documentNumber) {
        PropInvoiceBean invoice = InvoicesUtils.getInvoiceInfo(documentNumber);
        String query = "SELECT otgruz1.item_id as id,otgruz1.ndoc,_put_list.[date] "
                + "from otgruz1 "
                + "LEFT JOIN _put_list on _put_list.ttn_id = otgruz1.item_id "
                + "WHERE otgruz1.ndoc = ?";
        try (PreparedStatement ps = getConnection().prepareStatement(query)) {
            ps.setString(1, documentNumber);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                invoice.setDate(rs.getDate("date"));
            }

        } catch (Exception e) {
            log.severe("Ошибка getInvoicesInfo для " + documentNumber + " " + e.getMessage());
        }
        return invoice;
    }

    public String createENaklBTP(String nn, String pathSave) {
        String query;
        String UNPMain = "400078265";
        query = " {? = call _nakladnie (? , 'full') }";

        PropInvoiceBean invoiceInfo = getInvoicesInfo(nn);
        CustomerInfoBean client = getCustomerByDocumentNumber(nn);

        StringBuilder fName = new StringBuilder();

        fName.append(UNPMain);
        fName.append("_");
        fName.append(client.getUnp());
        fName.append("_");
        fName.append(UtilFunctions.dateToFormatString(invoiceInfo.getDate()));
        fName.append("_");
        fName.append(invoiceInfo.getNumber());

        String dogovor = "";
        String param;
        DBF dbf = null;
        if (pathSave.equals("") || pathSave == null) {
            dbf = new DBF(6, fName.toString(), "");
        } else dbf = new DBF(6, fName.toString(), pathSave);
        ArrayList<Object[]> data = new ArrayList<Object[]>();

        long n = -1;

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement("select item_id from otgruz1 where ndoc = ?");
             PreparedStatement fps = conn.prepareStatement("select DOGOVOR from ttn where DOC_ID = ?");
             CallableStatement cs = conn.prepareCall(query);
        ) {
            ps.setString(1, nn);
            ResultSet rs = ps.executeQuery();
            rs.next();
            n = rs.getLong(1);

            fps.setLong(1, n);
            ResultSet frs = fps.executeQuery();
            if (frs.next()) {
                dogovor = frs.getString(1).trim();
                if (dogovor.length() > 50) dogovor = dogovor.substring(0, 50);
            }
            if (dogovor.isEmpty())
                dogovor = "";
            cs.setLong(2, n);
            cs.registerOutParameter(1, Types.OTHER);
            ResultSet srs = cs.executeQuery();

            dbf.conn();
            while (srs.next()) {
                Object[] v = new Object[25];

                v[0] = invoiceInfo.getNumber();
                String tmpEan = srs.getString("eancode").trim();
                if (tmpEan.isEmpty()) {
                    tmpEan = "0";
                }
                v[1] = invoiceInfo.getDate();
                v[2] = Long.valueOf(tmpEan);
                v[3] = srs.getString("ngpr").trim();
                v[4] = srs.getString("nar").trim();
                v[5] = srs.getString("fas");
                v[6] = srs.getString("rzm").trim();
                v[7] = "РБ";

                String str = srs.getString("srt").trim();
                v[8] = str.substring(0, 3);
                v[9] = "шт.";
                v[10] = srs.getInt("kol_all");
                v[11] = srs.getFloat("cena");
                v[12] = 0;
                v[13] = 0;
                v[14] = 0;
                v[15] = srs.getDouble("nds");
                param = srs.getString("preiscur").trim();
                if (param.length() > 15) param = param.substring(0, 15);
                v[16] = param;
                v[17] = invoiceInfo.getDate();
                param = srs.getString("sertifikat").trim();
                if (param.length() > 100) param = param.substring(0, 105);
                v[18] = param;
                v[19] = "";
                v[20] = "";
                v[21] = UNPMain;
                v[22] = client.getUnp();
                v[23] = invoiceInfo.getSeries();
                v[24] = 0;
                data.add(v);
                dbf.write(v);
            }
        } catch (Exception e) {
            log.severe("error in method createENaklBTP: " + e.getMessage());

        } finally {
            if (dbf != null) dbf.disconn();
        }
        return fName.toString();
    }

    public String createOldEmporium(String nn, String pathSave) {
        String query = " {? = call _nakladnie (? , 'full') }";

        getInvoicesInfo(nn);
        getCustomerByDocumentNumber(nn);

        StringBuilder fName = new StringBuilder();

        fName.append("SPEC");
        fName.append("_");
        fName.append(nn);

        String dogovor = new String("");
        String param;
        DBF dbf = null;
        if (pathSave.isEmpty() || pathSave == null) {
            dbf = new DBF(7, fName.toString(), "");
        } else dbf = new DBF(7, fName.toString(), pathSave);
        List<Object[]> data = new ArrayList<>();

        long n = -1;

        try (Connection conn = getConnection();
             PreparedStatement fps = conn.prepareStatement("select item_id from otgruz1 where ndoc = ?");
             PreparedStatement sps = conn.prepareStatement("select DOGOVOR from ttn where DOC_ID = ?");
             CallableStatement cs = conn.prepareCall(query)
        ) {
            fps.setString(1, nn);
            ResultSet rs = fps.executeQuery();
            rs.next();
            n = rs.getLong(1);

            sps.setLong(1, n);
            ResultSet frs = sps.executeQuery();
            if (frs.next()) {
                dogovor = frs.getString(1).trim();
                if (dogovor.length() > 50)
                    dogovor = dogovor.substring(0, 50);
            }
            if (dogovor.isEmpty()) dogovor = "";
            cs.setLong(2, n);
            cs.registerOutParameter(1, Types.OTHER);
            ResultSet trs = cs.executeQuery();

            dbf.conn();
            while (trs.next()) {
                Object[] v = new Object[16];

                v[0] = nn;
                String tmpEan = trs.getString("eancode").trim();
                if (tmpEan.isEmpty()) {
                    tmpEan = "0";
                }
                v[1] = tmpEan;

                String sb = trs.getString("ngpr").trim() +
                        ", " + rs.getString("ncw").trim() +
                        ", Модель " + trs.getString("fas").trim() +
                        ", Размер " + trs.getString("rzm").trim() +
                        ", " + rs.getString("srt").trim();
                v[2] = sb;
                v[3] = "шт.";
                v[4] = trs.getString("nar").trim();
                v[5] = trs.getFloat("kol_all");
                v[6] = trs.getFloat("cena");
                v[7] = trs.getDouble("nds");
                v[8] = 0;
                v[9] = trs.getFloat("summa_nds");
                v[10] = trs.getFloat("itogo");
                v[11] = 1;
                v[12] = 1;
                v[13] = 0;
                param = trs.getString("sertifikat").trim();
                if (param.length() > 100) param = param.substring(0, 100);
                v[14] = param;
                v[15] = "";
                data.add(v);
                dbf.write(v);
            }
        } catch (Exception e) {
            log.severe("Error in method: " + e.getMessage());
        } finally {
            if (dbf != null) dbf.disconn();
        }
        return fName.toString();
    }

    /**
     * Метод проверяет, есть ли в базе изделие соответствующее ЕАН коду
     *
     * @param value объект EAN-код
     * @return true - если изделие в базе есть
     */
    public boolean eanCodeIsExist(final String value) {
        String query = "select kod from nsi_sd where ean=?";
        try (PreparedStatement ps = getConnection().prepareStatement(query)) {
            ps.setString(1, value);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return true;
            }
        } catch (Exception e) {
            log.severe("Ошибка получение изделия по EAN коду "
                    + e.getMessage());
        }
        return false;
    }

    public boolean eanCodeIsExist(final String value, int amount) {
        String query = "select count(*) as amount from nsi_sd where ean=?";
        try (PreparedStatement ps = getConnection().prepareStatement(query)) {
            ps.setString(1, value);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int size = rs.getInt("amount");
                if (size > 1) {
                    log.info("Двойной штрихкод: " + value);
                    return false;
                }
                return true;
            }
        } catch (Exception e) {
            log.severe("Ошибка получение изделия по EAN коду "
                    + e.getMessage());
        }
        return false;
    }

    public boolean createRefundInvoice(String dateDoc, String documentNumber, String operac, int contractorCode, List<SaleDocumentDetailItemReport> list) {

        if (list == null) {
            return false;
        }

        boolean rezalt = false;
        int idItemClient;
        int idDoc;
        String sql = "SELECT ITEM_ID FROM s_klient where KOD = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            conn.setAutoCommit(false);
            ps.setDouble(1, contractorCode);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                idItemClient = rs.getInt("ITEM_ID");

                sql = "INSERT INTO otgruz1([date],ndoc,kpodot,kpl,summa,summa_nds,summa_all,"
                        + "	skidka,kola,kolk,kolr,status,ucenka3s,datevrkv,	uservrkv,kpodvrkv,datekrkv,userkrkv,"
                        + "     kpodkrkv,klient_id,vid_sert,vid_ggr,operac,export,valuta_id,"
                        + "	kurs,skidka_tip,kurs_bank,nalogi,trade_mark_type)"
                        + "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);";

                PreparedStatement fps = conn.prepareStatement(sql);
                fps.setDate(1, java.sql.Date.valueOf(new String(new SimpleDateFormat("yyyy-MM-dd").format(new SimpleDateFormat("dd.MM.yyyy").parse(dateDoc)))));
                fps.setString(2, documentNumber);                                      //ndoc
                fps.setInt(3, 737);                                           //kpodot
                fps.setInt(4, contractorCode);                                      //kpl
                fps.setDouble(5, 0);                                             //summa
                fps.setDouble(6, 0);                                             //summa_nds
                fps.setDouble(7, 0);                                             //summa_all
                fps.setInt(8, 0);                                             //skidka
                fps.setDouble(9, 0);                                             //kola
                fps.setDouble(10, 0);                                            //kolk
                fps.setDouble(11, 0);                                            //kolr
                fps.setInt(12, 3);                                            //status
                fps.setDouble(13, 0);                                            //ucenka3s
                fps.setDate(14, new java.sql.Date(new java.util.Date().getTime())); //datevrkv
                fps.setString(15, "Склад6-карт");                                //uservrkv
                fps.setString(16, "737");                                        //kpodvrkv
                fps.setDate(17, new java.sql.Date(new java.util.Date().getTime())); //datekrkv
                fps.setString(18, "Склад6-карт");                                //userkrkv
                fps.setString(19, "737");                                        //kpodkrkv
                fps.setInt(20, idItemClient);                                 //klient_id
                fps.setInt(21, 1);                                            //vid_sert
                fps.setInt(22, 1);                                            //vid_ggr
                fps.setString(23, operac);                                       //operac
                fps.setInt(24, 0);                                            //export
                fps.setInt(25, 1);                                            //valuta_id
                fps.setInt(26, 1);                                            //kurs
                fps.setInt(27, 0);                                            //skidka_tip
                fps.setInt(28, 1);                                            //kurs_bank
                fps.setInt(29, 1);                                            //nalogi
                fps.setInt(30, 2);
                fps.execute();

                sql = "SELECT @@IDENTITY AS 'Identity'";

                PreparedStatement sps = conn.prepareStatement(sql);
                ResultSet frs = sps.executeQuery();
                if (frs.next()) {
                    idDoc = frs.getInt("Identity");

                    sql = "IF (not object_id('RefundTempTable') IS NULL) DROP TABLE RefundTempTable "
                            + "CREATE TABLE RefundTempTable ( COLOR_     CHAR(15) ,"
                            + "                         KOL_       NUMERIC(5), "
                            + "                         CENA_      DECIMAL(9,2),"
                            + "                         SUMMA_     DECIMAL(9,2),"
                            + "                         NDS_       DECIMAL(5,2),"
                            + "                         SUMMA_NDS_ DECIMAL(9,2),"
                            + "                         ITOGO_     DECIMAL(9,2),"
                            + "                         EANCODE_   CHAR(13),"
                            + "                         PREISCUR_  CHAR(30),"
                            + "                         TA_        NUMERIC(2));";

                    Statement st = conn.createStatement();
                    st.execute(sql);


                    sql = "INSERT INTO RefundTempTable(COLOR_ , KOL_ , CENA_ , SUMMA_ , NDS_ , SUMMA_NDS_, ITOGO_ , EANCODE_ , PREISCUR_, TA_ )"
                            + " VALUES( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                    PreparedStatement tps = conn.prepareStatement(sql);
                    for (SaleDocumentDetailItemReport refundItem : list) {
                        tps.setString(1, refundItem.getItemColor());                  // COLOR
                        tps.setInt(2, refundItem.getAmountAll());                     // AMOUNT
                        tps.setDouble(3, refundItem.getValuePrice());                 // CENA
                        tps.setDouble(4, refundItem.getValueSumCost());               // SUMMA
                        tps.setDouble(5, refundItem.getValueVat());                   // NDS
                        tps.setDouble(6, refundItem.getValueSumVat());                // SUMMA_NDS
                        tps.setDouble(7, refundItem.getValueSumCostAndVat());         // ITOGO
                        tps.setString(8, refundItem.getEanCode());                    // EAN
                        tps.setString(9, refundItem.getItemPriceList());              // PREISCUR
                        tps.setDouble(10, refundItem.getValueTradeMarkup());          // TN

                        tps.addBatch();
                    }
                    tps.executeBatch();

                    sql = "select KOL_ as barcodemax, KOL_, CENA_,  SUMMA_, NDS_, SUMMA_NDS_, ITOGO_, EANCODE_, PREISCUR_, sd.rst as rst_marh, sd.rzm as rzm_marh, " +
                            " sd.kod1 as kod_izd, COLOR_, sd.srt as srt, TA_ from RefundTempTable as mt " +
                            " INNER JOIN NSI_EANCODE as ean on EANCODE_= ean.EANCODE " +
                            " INNER JOIN nsi_sd as sd on ean.ITEM_CODE = sd.kod1 " +
                            " order by EANCODE_ ";

                    rs = st.executeQuery(sql);
                    while (rs.next()) {

                        sql = "INSERT INTO otgruz2(scan, part, kol, cena, summa, nds, summa_nds, "
                                + "     itogo,cenav, summav, summa_ndsv, itogov, kol_in_upack, tip, time_ins, pc_ins,"
                                + "     eancode, doc_id, kod_str, preiscur, rzm_marh, rst_marh, kod_izd, ncw, srt, cena_uch)"
                                + " VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);";

                        PreparedStatement aps = conn.prepareStatement(sql);

                        int valueTradeAllowance = rs.getInt("TA_");

                        aps.setInt(1, rs.getInt("barcodemax"));               //scan
                        aps.setInt(2, 1);                                     //part
                        aps.setInt(3, rs.getInt("KOL_"));            //kol
                        aps.setDouble(4, rs.getDouble("CENA_"));                 //cena
                        aps.setDouble(5, rs.getDouble("SUMMA_"));        //summa
                        aps.setDouble(6, rs.getDouble("NDS_"));            //nds
                        aps.setDouble(7, rs.getDouble("SUMMA_NDS_"));        //summa_nds
                        aps.setDouble(8, rs.getDouble("ITOGO_"));        //itogo
                        aps.setDouble(9, .00);                                   //cenav
                        aps.setDouble(10, .00);                                  //summav
                        aps.setDouble(11, .00);                                  //summa_ndsv
                        aps.setDouble(12, .00);                                  //itogov
                        aps.setInt(13, 1);                                    //kol_in_upack
                        aps.setInt(14, 1);                                    //tip
                        aps.setDate(15, new java.sql.Date(new java.util.Date().getTime()));   //time_ins
                        aps.setInt(16, valueTradeAllowance);
                        aps.setString(17, rs.getString("EANCODE_"));             //eancode
                        aps.setInt(18, idDoc);                                //doc_id
                        aps.setInt(19, rs.getInt("barcodemax"));                 //kod_str
                        aps.setString(20, rs.getString("PREISCUR_"));            //preiscur
                        aps.setInt(21, rs.getInt("rzm_marh"));        //rzm_marh
                        aps.setInt(22, rs.getInt("rst_marh"));        //rst_marh
                        aps.setInt(23, rs.getInt("kod_izd"));                 //kod_izd
                        aps.setString(24, rs.getString("COLOR_"));               //ncw
                        aps.setInt(25, rs.getInt("srt"));            //srt
                        aps.setDouble(26, rs.getDouble("CENA_"));            //cena_uch
                        aps.execute();

                    }
                    conn.commit();
                    rezalt = true;
                } else conn.rollback();
            }
        } catch (Exception e) {
            log.severe("Ошибка в createRefundInvoice()" + e.getMessage());
        }
        return rezalt;
    }
}