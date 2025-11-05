package dept.upack.ns;

import by.march8.ecs.framework.common.LogCrutch;
import common.UtilFunctions;
import workDB.DB_new;

import javax.swing.*;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Vector;

/**
 *
 * @author vova
 * @date 16.11.2011
 */
public class UpackNSDB extends DB_new {
    // private static final Logger log = new Log().getLoger(UpackNSDB.class);
    private static final LogCrutch log = new LogCrutch();

    /**
     * Помечает МЛ как принятый
     * @param kod -- код маршрутного листа
     * @return true/false
     * @throws Exception
     *
     * @author vova
     * @date 17.11.2011
     */
    public boolean confirmML(int kod) throws Exception {

        String query = "update _poshiv_ns set status = 0 where kod_marh = ? ";
        try {
            ps = conn.prepareStatement(query);
            ps.setInt(1, kod);
            if (ps.executeUpdate() != 0) {
                return true;
            }
        } catch (Exception e) {
            System.err.println("Ошибка confirmML(int kod) " + e);
            throw new Exception("Ошибка при принятии МЛ ", e);
        }
        return false;
    }

    /**
     * Помечает МЛ как отказанный
     * @param kod -- код маршрутного листа
     * @return true/false
     * @throws Exception
     *
     * @author vova
     * @date 17.11.2011
     */
    public boolean refusalML(int kod) throws Exception {

        String query = "update _poshiv_ns set status = 2 where kod_marh = ? ";
        try {
            ps = conn.prepareStatement(query);
            ps.setInt(1, kod);
            if (ps.executeUpdate() != 0) {
                return true;
            }
        } catch (Exception e) {
            System.err.println("Ошибка refusalML(int kod) " + e);
            throw new Exception("Ошибка при отказе от МЛ ", e);
        }
        return false;
    }

    /**
     * Возвращает список маршрутных листов за период вермени
     * @param sd -- начало периода дд.мм.гггг
     * @param ed -- конец периода дд.мм.гггг
     * @return Vector[Vector] (номер, код маршрута, кол-во изделий, бригада, дата, статус)
     * @throws Exception
     * @author vova
     * @date 17.11.2011
     */
    public Vector getMarhList(String sd, String ed) throws Exception {
        long sDate = 0;
        long eDate = 0;
        try {
            sDate = UtilFunctions.convertDateStrToLong(sd);
            eDate = UtilFunctions.convertDateStrToLong(ed);
        } catch (ParseException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
            return (Vector) Collections.emptyList();
        }

        Vector items = new Vector();
        int i = 0;

        String query = "select nomer, p.kod_marh, sdacha, brigada, data, status from " +
                " (select kod_marh, kod_izd, sum(sdacha) as sdacha, brigada, data from poshiv where data between  ? and ?  and pdrto = 888 group by kod_marh, kod_izd, brigada, data)as p " +
                " left join (select kod_marh, status = case status when 1 then 'Новый' when 0 then 'Принят' when 2 then 'Отказ' else '--//--' end from _poshiv_ns group by kod_marh, status) as ns on ns.kod_marh = p.kod_marh " +
                " left join (select nomer, kod from marh_list)as ml on ml.kod = p.kod_marh " +
                " order by data, nomer";
        try {
            ps = conn.prepareStatement(query);
            ps.setDate(1, new java.sql.Date(sDate));
            ps.setDate(2, new java.sql.Date(eDate));
            rs = ps.executeQuery();
            while (rs.next()) {
                Vector user = new Vector();
                user.add(rs.getString(1).trim());
                user.add(rs.getInt(2));
                user.add(rs.getInt(3));
                user.add(rs.getString(4).trim());
                user.add(rs.getString(5).trim().substring(0, 10));
                user.add(rs.getString(6).trim());
                items.add(user);
            }
        } catch (Exception e) {
            System.err.println("Ошибка getMarhList(String sd, String ed) " + e);
            throw new Exception("Ошибка получения списка маршрутных листов ", e);
        }
        return items;
    }

    /**
     * Возвращает изделия маршрута
     * @param kod_marh -- код маршрута
     * @return Vector[Vector] (наименование, модель, артикул, сорт, рост, размер, кол-во)
     * @throws Exception
     * @author vova
     * @date 17.11.2011
     */
    public Vector getMarhListDescr(int kod_marh) throws Exception {
        Vector items = new Vector();

        String query = "select ngpr, fas, nar, srt, rst, rzm, sdacha, skidka, ns.barcode from " +
                " (select barcode, kod_marh, skidka from _poshiv_ns where kod_marh = ? )as ns " +
                " left join (select srt, rst, rzm, kod_izd, barcode, sdacha from poshiv)as p on p.barcode = ns.barcode " +
                " left join (select ngpr, nar, fas, kod from nsi_kld)as kld on kld.kod = p.kod_izd";
        try {
            ps = conn.prepareStatement(query);
            ps.setInt(1, kod_marh);
            rs = ps.executeQuery();
            while (rs.next()) {
                Vector user = new Vector();
                user.add(rs.getString(1).trim());
                user.add(rs.getString(2).trim());
                user.add(rs.getString(3).trim());
                user.add(rs.getString(4).trim());
                user.add(rs.getString(5).trim());
                user.add(rs.getInt(6));
                user.add(rs.getInt(7));
                user.add(rs.getInt(8));
                user.add(rs.getString(9).trim());
                items.add(user);
            }
        } catch (Exception e) {
            System.err.println("getMarhListDescr(int kod_marh) " + e);
            throw new Exception("Ошибка получения деталей маршрутного листа ", e);
        }
        return items;
    }

    /**
     * Возвращает список принятых изделий за период
     * @param sd -- начало периода
     * @param ed -- конец периода
     * @return Vector[Vector] (наименование, модель, артикул, сорт, рост, размер, номер маршрута, код маршрута кол-во, бригада)
     * @throws Exception
     * @author vova
     * @date 17.11.2011
     */
    public Vector getItemList(String sd, String ed) throws Exception {
        long sDate = 0;
        long eDate = 0;
        try {
            sDate = UtilFunctions.convertDateStrToLong(sd);
            eDate = UtilFunctions.convertDateStrToLong(ed);
        } catch (ParseException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
            return (Vector) Collections.emptyList();
        }

        Vector items = new Vector();
        int i = 0;

        String query = "select ngpr, fas, nar, srt, rst, rzm, nomer, kod_marh, sdacha, brigada, skidka from " +
                " (select kod_marh, kod_izd, rzm, rst, srt, sdacha, brigada, barcode from poshiv where data between ? and ? and pdrto = 888)as p " +
                " join (select barcode, status, skidka  from _poshiv_ns where status = 0) as ns on ns.barcode = p.barcode " +
                " left join (select ngpr, nar, fas, kod from nsi_kld)as kld on kld.kod = p.kod_izd " +
                " left join (select nomer, kod from marh_list)as ml on ml.kod = p.kod_marh " +
                " order by nar, srt, rst, rzm ";
        try {
            ps = conn.prepareStatement(query);
            ps.setDate(1, new java.sql.Date(sDate));
            ps.setDate(2, new java.sql.Date(eDate));
            rs = ps.executeQuery();
            while (rs.next()) {
                Vector user = new Vector();
                user.add(rs.getString(1).trim());
                user.add(rs.getString(2).trim());
                user.add(rs.getString(3).trim());
                user.add(rs.getString(4).trim());
                user.add(rs.getString(5).trim());
                user.add(rs.getString(6).trim());
                user.add(rs.getString(7).trim());
                user.add(rs.getString(8).trim());
                user.add(rs.getString(9).trim());
                user.add(rs.getString(10).trim());
                user.add(rs.getInt(11));
                items.add(user);
            }
        } catch (Exception e) {
            System.err.println("Ошибка getItemList(String sd, String ed) " + e);
            throw new Exception("Ошибка получения списка принятых изделий ", e);
        }
        return items;
    }

    /**
     * Изменяет скидку на изделие
     * @param barcode -- баркод изделия
     * @param skidka -- скидка
     * @return true(была изменена) / false(изменений не было, возможно нет такого изделия)
     * @throws Exception
     * @author vova
     * @date 24.11.2011
     */
    public boolean setSkidka(String barcode, int skidka) throws Exception {

        String query = "update _poshiv_ns set skidka = ? where barcode = ?";
        try {
            ps = conn.prepareStatement(query);
            ps.setInt(1, skidka);
            ps.setString(2, barcode);

            if (ps.executeUpdate() == 0) return false;
        } catch (Exception e) {
            System.err.println("setSkidka(String barcode, int skidka) " + e);
            throw new Exception("Ошибка при изменении скидки для изделия ", e);
        }
        return true;
    }

    /**
     * Получении списка различных скидок
     * @return ArrayList{String}
     * @throws Exception
     * @author vova
     * @date 25.11.2011
     */
    public ArrayList<String> getSkidkaList() throws Exception {

        ArrayList<String> skidkaType = new ArrayList<String>();
        skidkaType.add("Все");
        String query = "select distinct skidka from _poshiv_ns ";
        try {
            ps = conn.prepareStatement(query);
            rs = ps.executeQuery();
            while (rs.next()) {
                skidkaType.add(rs.getString(1).trim());
            }
        } catch (Exception e) {
            System.err.println("getSkidkaList() " + e);
            throw new Exception("Ошибка при получении списка различных скидок", e);
        }
        return skidkaType;
    }
}