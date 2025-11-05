package dept.sklad.ho;

import by.march8.ecs.framework.common.LogCrutch;
import workDB.PDB_new;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.Vector;

/**
 *
 * @author lidashka
 */
@SuppressWarnings("all")
public class SkladHOPDB extends PDB_new {
    //private static final Logger log = new Log().getLoger(SkladHOPDB.class);
    private static final LogCrutch log = new LogCrutch();

    public Vector getDept() throws Exception {
        Vector dept = new Vector();
        String sql = "Select dept.id as idDept, department From dept  Order by department";

        try {
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                Vector tmp = new Vector();
                tmp.add(rs.getInt("idDept"));
                tmp.add(rs.getString("department").trim());
                dept.add(tmp);
            }

        } catch (Exception e) {
            dept = new Vector();
            System.err.println("Ошибка getAllDept() " + e);
            log.error("Ошибка getAllDept()", e);
            throw new Exception("Ошибка getAllDept() " + e.getMessage(), e);
        }
        return dept;
    }

    public Vector getAllTMC(long datePrice) throws Exception {
        Vector tmc = new Vector();
        String sql = "Select idTmc, tmcname, sartmc, nartmc, vid, made, ed_izm,"
                + "          note, id_group, tmc_group, evvod, dvvod, eins, dins,"
                + "          (Select price from sklad_hud_price where sklad_hud_price.id_tmc = idTmc and date_st <= ? Order by date_st desc limit 1 ) as priceTmc "
                + "     From (Select sklad_hud_tmc.id as idTmc, tmcname, sartmc, nartmc, vid, made, ed_izm, note, id_group, "
                + "              empl1.fio as evvod, date_vvod as dvvod, empl2.fio as eins, date_ins as dins "
                + "          From sklad_hud_tmc, employees as empl1, employees as empl2 "
                + "          Where empl1.id = sklad_hud_tmc.id_empl_vvod and "
                + "                  empl2.id = sklad_hud_tmc.id_empl_ins) as t1 "
                + "     left join sklad_hud_tmc_group "
                + "     on sklad_hud_tmc_group.id = id_group "
                + "     Order by tmc_group, tmcname, vid, made,  nartmc, sartmc, idTmc ";

        try {
            ps = conn.prepareStatement(sql);
            ps.setDate(1, new java.sql.Date(datePrice));
            rs = ps.executeQuery();
            while (rs.next()) {
                Vector tmp = new Vector();
                tmp.add(false);
                tmp.add(rs.getInt("idTmc"));
                tmp.add(rs.getInt("id_group"));
                tmp.add(rs.getObject("tmc_group") == null ? "" : rs.getString("tmc_group").trim());
                tmp.add(rs.getString("tmcname").trim());
                tmp.add(rs.getString("nartmc").trim());
                tmp.add(rs.getInt("sartmc"));
                tmp.add(rs.getInt("vid") == 1 ? UtilSkladHO.SKLAD_VID_1 : UtilSkladHO.SKLAD_VID_2);
                tmp.add(rs.getInt("made") == 1 ? UtilSkladHO.SKLAD_MADE_1 : UtilSkladHO.SKLAD_MADE_2);
                tmp.add(rs.getString("ed_izm").trim());
                tmp.add(new BigDecimal(rs.getDouble("priceTmc")).setScale(4, RoundingMode.HALF_UP).doubleValue());
                tmp.add(rs.getString("note").trim());
                tmp.add(rs.getString("evvod"));
                tmp.add(new SimpleDateFormat("dd-MM-yyyy").format(rs.getDate("dvvod")));
                tmp.add(rs.getString("eins"));
                tmp.add(new SimpleDateFormat("dd-MM-yyyy").format(rs.getDate("dins")));
                tmc.add(tmp);
            }

        } catch (Exception e) {
            System.err.println("Ошибка getAllTMC() " + e);
            log.error("Ошибка getAllTMC()", e);
            throw new Exception("Ошибка getAllTMC() " + e.getMessage(), e);
        }
        return tmc;
    }

    public Vector getAllGroupTMC() throws Exception {
        Vector data = new Vector();
        String sql = "Select id, tmc_group  "
                + "     From sklad_hud_tmc_group "
                + "     Order by tmc_group ";

        try {
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                Vector tmp = new Vector();
                tmp.add(false);
                tmp.add(rs.getInt("id"));
                tmp.add(rs.getString("tmc_group").trim());
                data.add(tmp);
            }
        } catch (Exception e) {
            System.err.println("Ошибка getAllGroupTMC() " + e);
            log.error("Ошибка getAllGroupTMC()", e);
            throw new Exception("Ошибка getAllGroupTMC() " + e.getMessage(), e);
        }
        return data;
    }

    public boolean addGroupTmcSkladHO(String groupTmc, int idEmpl) throws Exception {
        boolean rezalt = false;
        String sql = "";

        try {
            sql = "INSERT INTO sklad_hud_tmc_group (tmc_group, id_empl_vvod, id_empl_ins) "
                    + "   VALUES( ?, ?, ?)";

            ps = conn.prepareStatement(sql);
            ps.setString(1, groupTmc);
            ps.setInt(2, idEmpl);
            ps.setInt(3, idEmpl);
            ps.execute();

            rezalt = true;
        } catch (Exception e) {
            rezalt = false;
            System.err.println("Ошибка addGroupTmcSkladHO() " + e);
            log.error("Ошибка addGroupTmcSkladHO()", e);
            throw new Exception("Ошибка addGroupTmcSkladHO(): " + e.getMessage(), e);
        }

        return rezalt;
    }

    public boolean editGroupTmcSkladHO(int idGroup, String groupTmc, int idEmpl) throws Exception {
        boolean rezalt = false;
        String sql = "";

        try {
            sql = " UPDATE sklad_hud_tmc_group "
                    + "   Set tmc_group = ?, id_empl_ins = ? "
                    + "   Where id = ? ";

            ps = conn.prepareStatement(sql);
            ps.setString(1, groupTmc);
            ps.setInt(2, idEmpl);
            ps.setInt(3, idGroup);
            ps.execute();

            rezalt = true;
        } catch (Exception e) {
            rezalt = false;
            System.err.println("Ошибка editGroupTmcSkladHO() " + e);
            log.error("Ошибка editGroupTmcSkladHO()", e);
            throw new Exception("Ошибка editGroupTmcSkladHO(): " + e.getMessage(), e);
        }

        return rezalt;
    }

    public boolean deleteGroupTMC(int id) throws Exception {
        boolean rezalt = false;
        String sql = "";

        try {
            setAutoCommit(false);

            sql = "Update sklad_hud_tmc Set id_group = -1 Where id_group = ?";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            ps.execute();

            sql = "Delete From sklad_hud_tmc_group Where id = ?";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            ps.execute();

            commit();

            rezalt = true;
        } catch (Exception e) {
            rollBack();
            rezalt = false;
            System.err.println("Ошибка deleteGroupTMC() " + e);
            log.error("Ошибка deleteGroupTMC()", e);
            throw new Exception("Ошибка deleteGroupTMC() " + e.getMessage(), e);
        } finally {
            setAutoCommit(true);
        }

        return rezalt;
    }

    public boolean addTmcSkladHO(int vid, int made, int sar, String nar, String tmcName,
                                 String edIzm, String note, int group, double price, long datePrice, int idEmpl) throws Exception {
        boolean rezalt = false;
        String sql = "";
        ResultSet rs_ = null;

        try {
            setAutoCommit(false);

            sql = "INSERT INTO sklad_hud_tmc (vid, made, sartmc, nartmc, tmcname,"
                    + "          ed_izm, id_empl_vvod, id_empl_ins, note, id_group) "
                    + "   VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?) returning id ";

            ps = conn.prepareStatement(sql);
            ps.setInt(1, vid);
            ps.setInt(2, made);
            ps.setInt(3, sar);
            ps.setString(4, nar);
            ps.setString(5, tmcName);
            ps.setString(6, edIzm);
            ps.setInt(7, idEmpl);
            ps.setInt(8, idEmpl);
            ps.setString(9, note);
            ps.setInt(10, group);
            rs = ps.executeQuery();

            if (price > 0) {
                if (rs.next()) {
                    sql = "Select id From sklad_hud_price Where id_tmc = ? and date_st = ?";
                    ps = conn.prepareStatement(sql);
                    ps.setInt(1, rs.getInt(1));
                    ps.setDate(2, new java.sql.Date(datePrice));
                    rs_ = ps.executeQuery();

                    if (rs_.next()) {
                        sql = "Delete From sklad_hud_price Where id = ?";
                        ps = conn.prepareStatement(sql);
                        ps.setInt(1, rs_.getInt("id"));
                        ps.execute();
                    }

                    sql = "Insert into sklad_hud_price(id_tmc, date_st, price, id_empl_vvod, id_empl_ins) "
                            + "values( ?, ?, ?, ?, ?)";
                    ps = conn.prepareStatement(sql);
                    ps.setInt(1, rs.getInt(1));
                    ps.setDate(2, new java.sql.Date(datePrice));
                    ps.setDouble(3, price);
                    ps.setInt(4, idEmpl);
                    ps.setInt(5, idEmpl);
                    ps.execute();
                }
            }

            commit();
            rezalt = true;
        } catch (Exception e) {
            rollBack();
            rezalt = false;
            System.err.println("Ошибка addTmcSkladHO() " + e);
            log.error("Ошибка addTmcSkladHO()", e);
            throw new Exception("Ошибка addTmcSkladHO(): " + e.getMessage(), e);
        } finally {
            setAutoCommit(true);
        }

        return rezalt;
    }

    public boolean editTmcSkladHO(int idTmc, int vid, int made, int sar, String nar, String tmcName,
                                  String edIzm, String note, int group, int idEmpl) throws Exception {
        boolean rezalt = false;
        String sql = "";

        try {

            sql = "UPDATE sklad_hud_tmc SET vid = ?, made = ?, sartmc = ?, "
                    + "                     nartmc = ?, tmcname = ?, ed_izm = ?, "
                    + "                     id_empl_ins = ?, date_ins = now(), note = ?, id_group = ? "
                    + " Where id = ?";

            ps = conn.prepareStatement(sql);
            ps.setInt(1, vid);
            ps.setInt(2, made);
            ps.setInt(3, sar);
            ps.setString(4, nar);
            ps.setString(5, tmcName);
            ps.setString(6, edIzm);
            ps.setInt(7, idEmpl);
            ps.setString(8, note);
            ps.setInt(9, group);
            ps.setInt(10, idTmc);
            ps.execute();

            rezalt = true;
        } catch (Exception e) {
            rezalt = false;
            System.err.println("Ошибка editTmcSkladHO() " + e);
            log.error("Ошибка editTmcSkladHO()", e);
            throw new Exception("Ошибка editTmcSkladHO(): " + e.getMessage(), e);
        }

        return rezalt;
    }

    public boolean deleteTMC(int id) throws Exception {
        boolean rezalt = false;
        String sql = "";

        try {
            setAutoCommit(false);

            sql = "Delete From sklad_hud_price Where id_tmc = ? ";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            ps.execute();

            sql = "Delete From sklad_hud_tmc Where id = ?";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            ps.execute();

            commit();

            rezalt = true;
        } catch (Exception e) {
            rollBack();
            rezalt = false;
            System.err.println("Ошибка deleteTMC() " + e);
            log.error("Ошибка deleteTMC()", e);
            throw new Exception("Ошибка deleteTMC() " + e.getMessage(), e);
        } finally {
            setAutoCommit(true);
        }

        return rezalt;
    }

    public boolean deleteMoveTMC(int id) throws Exception {
        boolean rezalt = false;
        String sql = "";

        try {
            sql = "Delete From sklad_hud_move Where id = ?";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            ps.execute();

            rezalt = true;
        } catch (Exception e) {
            rezalt = false;
            System.err.println("Ошибка deleteMoveTMC() " + e);
            log.error("Ошибка deleteMoveTMC()", e);
            throw new Exception("Ошибка deleteMoveTMC() " + e.getMessage(), e);
        }

        return rezalt;
    }

    public boolean deleteProd(int id) throws Exception {
        boolean rezalt = false;
        String sql = "";

        try {
            setAutoCommit(false);

            sql = "Delete From sklad_hud_move Where id_prod = ?";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            ps.execute();

            sql = "Delete From sklad_hud_storage_item Where id_prod = ?";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            ps.execute();

            sql = "Delete From sklad_hud_prod_item Where id_prod = ?";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            ps.execute();

            sql = "Delete From sklad_hud_prod Where id = ?";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            ps.execute();

            commit();

            rezalt = true;

        } catch (Exception e) {
            rollBack();
            rezalt = false;
            System.err.println("Ошибка deleteProd() " + e);
            log.error("Ошибка deleteProd()", e);
            throw new Exception("Ошибка deleteProd() " + e.getMessage(), e);
        } finally {
            setAutoCommit(true);
        }

        return rezalt;
    }

    public boolean deleteIzd(int id) throws Exception {
        boolean rezalt = false;
        String sql = "";

        try {
            setAutoCommit(false);

            sql = "Delete From sklad_hud_storage_item Where id_storage = ?";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            ps.execute();

            sql = "Delete From sklad_hud_storage Where id = ?";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            ps.execute();

            commit();

            rezalt = true;

        } catch (Exception e) {
            rollBack();
            rezalt = false;
            System.err.println("Ошибка deleteIzd() " + e);
            log.error("Ошибка deleteIzd()", e);
            throw new Exception("Ошибка deleteIzd() " + e.getMessage(), e);
        } finally {
            setAutoCommit(true);
        }

        return rezalt;
    }

    public boolean deleteProdItem(int id) throws Exception {
        boolean rezalt = false;
        String sql = "";

        try {
            sql = "Delete From sklad_hud_prod_item Where id = ?";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            ps.execute();

            rezalt = true;
        } catch (Exception e) {
            rezalt = false;
            System.err.println("Ошибка deleteProdItem() " + e);
            log.error("Ошибка deleteProdItem()", e);
            throw new Exception("Ошибка deleteProdItem() " + e.getMessage(), e);
        }

        return rezalt;
    }

    public boolean deleteMoveTMCTemp(int id) throws Exception {
        boolean rezalt = false;
        String sql = "";

        try {
            sql = "Delete From tmp_sklad_hud_move Where id = ?";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            ps.execute();

            rezalt = true;
        } catch (Exception e) {
            rezalt = false;
            System.err.println("Ошибка deleteMoveTMCTemp() " + e);
            log.error("Ошибка deleteMoveTMCTemp()", e);
            throw new Exception("Ошибка deleteMoveTMCTemp() " + e.getMessage(), e);
        }

        return rezalt;
    }

    public boolean deletePriceTMC(int id) throws Exception {
        boolean rezalt = false;
        String sql = "";

        try {
            sql = "Delete From sklad_hud_price Where id = ?";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            ps.execute();

            rezalt = true;
        } catch (Exception e) {
            rezalt = false;
            System.err.println("Ошибка deletePriceTMC() " + e);
            log.error("Ошибка deletePriceTMC()", e);
            throw new Exception("Ошибка deletePriceTMC() " + e.getMessage(), e);
        }

        return rezalt;
    }

    public Vector getPrice(boolean flagVod, long sDate, long eDate, boolean flagIns, long sInsDate, long eInsDate) throws Exception {
        Vector data = new Vector();
        String sql = "";

        try {
            sql = "Select sklad_hud_price.id as idPrice, sklad_hud_tmc.id as idTmc, vid, made, sartmc, tmcname, nartmc, "
                    + "         price, date_st, fio, sklad_hud_price.date_ins as dateIns "
                    + "     From sklad_hud_tmc, sklad_hud_price, employees "
                    + "     Where employees.id = sklad_hud_price.id_empl_ins and "
                    + "           sklad_hud_tmc.id = sklad_hud_price.id_tmc "
                    + (flagVod ? " and sklad_hud_price.date_st between '" + new java.sql.Date(sDate) + " 00:00' "
                    + "                                                     and '" + new java.sql.Date(eDate) + " 23:59' " : " ")
                    + (flagIns ? " and sklad_hud_price.date_ins between '" + new java.sql.Date(sInsDate) + " 00:00' "
                    + "                                                       and '" + new java.sql.Date(eInsDate) + " 23:59' " : " ")
                    + "     Order by tmcname, vid, made, sartmc, nartmc, date_st ";

            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                Vector tmp = new Vector();

                tmp.add(false);
                tmp.add(rs.getInt("idPrice"));
                tmp.add(rs.getInt("idTmc"));
                tmp.add(rs.getInt("vid") == 1 ? UtilSkladHO.SKLAD_VID_1 : UtilSkladHO.SKLAD_VID_2);
                tmp.add(rs.getInt("made") == 1 ? UtilSkladHO.SKLAD_MADE_1 : UtilSkladHO.SKLAD_MADE_2);
                tmp.add(rs.getInt("sartmc"));
                tmp.add(rs.getString("tmcname").trim());
                tmp.add(rs.getString("nartmc").trim());
                tmp.add(new BigDecimal(rs.getDouble("price")).setScale(4, RoundingMode.HALF_UP).doubleValue());
                tmp.add(new SimpleDateFormat("dd-MM-yyyy").format(rs.getDate("date_st")));
                tmp.add(rs.getString("fio"));
                tmp.add(new SimpleDateFormat("dd-MM-yyyy").format(rs.getDate("dateIns")));
                data.add(tmp);
            }

        } catch (Exception e) {
            data = new Vector();
            System.err.println("Ошибка getPrice() " + e);
            log.error("Ошибка getPrice()", e);
            throw new Exception("Ошибка getPrice() " + e.getMessage(), e);
        }
        return data;
    }

    public boolean addPrice(int idTmc, long sDate, double price, int idEmpl) throws Exception {
        boolean rezalt = false;
        String sql = "";

        try {
            setAutoCommit(false);

            sql = "Select id From sklad_hud_price Where id_tmc = ? and date_st = ?";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, idTmc);
            ps.setDate(2, new java.sql.Date(sDate));
            rs = ps.executeQuery();

            if (rs.next()) {
                sql = "Delete From sklad_hud_price Where id = ?";
                ps = conn.prepareStatement(sql);
                ps.setInt(1, rs.getInt("id"));
                ps.execute();

            }

            sql = "Insert into sklad_hud_price(id_tmc, date_st, price, id_empl_vvod, id_empl_ins) "
                    + "values( ?, ?, ?, ?, ?)";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, idTmc);
            ps.setDate(2, new java.sql.Date(sDate));
            ps.setDouble(3, price);
            ps.setInt(4, idEmpl);
            ps.setInt(5, idEmpl);
            ps.execute();

            commit();

            rezalt = true;

        } catch (Exception e) {
            rollBack();
            rezalt = false;
            System.err.println("Ошибка addPrice() " + e);
            log.error("Ошибка addPrice()", e);
            throw new Exception("Ошибка addPrice() " + e.getMessage(), e);
        } finally {
            setAutoCommit(true);
        }

        return rezalt;
    }

    public Vector getHistoryPrice(boolean flagVod, long sDate, long eDate,
                                  boolean flagIns, long sInsDate, long eInsDate, int idTmc) throws Exception {
        Vector tmc = new Vector();
        String sql = "";

        try {

            sql = "Select sklad_hud_price.id as idPrice, sklad_hud_tmc.id as idTmc, vid, made, sartmc, tmcname, nartmc, "
                    + "         price, date_st, fio, sklad_hud_price.date_ins as dateIns "
                    + "     From sklad_hud_tmc, sklad_hud_price, employees "
                    + "     Where employees.id = sklad_hud_price.id_empl_ins and "
                    + "           sklad_hud_tmc.id = sklad_hud_price.id_tmc and "
                    + "           sklad_hud_tmc.id = ? "
                    + (flagVod ? " and sklad_hud_price.date_st between '" + new java.sql.Date(sDate) + " 00:00' "
                    + "                                                      and '" + new java.sql.Date(eDate) + " 23:59' " : " ")
                    + (flagIns ? " and sklad_hud_price.date_ins between '" + new java.sql.Date(sInsDate) + " 00:00' "
                    + "                                                       and '" + new java.sql.Date(eInsDate) + " 23:59' " : " ")
                    + "     Order by tmcname, vid, made, sartmc, nartmc, date_st, dateIns ";

            ps = conn.prepareStatement(sql);
            ps.setInt(1, idTmc);
            rs = ps.executeQuery();
            while (rs.next()) {
                Vector tmp = new Vector();

                tmp.add(false);
                tmp.add(rs.getInt("idPrice"));
                tmp.add("'" + new SimpleDateFormat("dd-MM-yyyy").format(rs.getDate("date_st")));
                tmp.add(new BigDecimal(rs.getDouble("price")).setScale(4, RoundingMode.HALF_UP).doubleValue());
                tmp.add("'" + new SimpleDateFormat("dd-MM-yyyy").format(rs.getDate("dateIns")));
                tmp.add(rs.getString("fio"));
                tmc.add(tmp);
            }

        } catch (Exception e) {
            tmc = new Vector();
            System.err.println("Ошибка getHistoryPrice() " + e);
            log.error("Ошибка getHistoryPrice()", e);
            throw new Exception("Ошибка getHistoryPrice() " + e.getMessage(), e);
        }
        return tmc;
    }

    public Vector getPriceForDate(long date) throws Exception {
        Vector tmc = new Vector();
        String sql = "";

        try {
            sql = "Select t1.idTmc as idTmc, t1.vid as vid, t1.made as made, t1.sartmc as sartmc, "
                    + "     t1.tmcname as tmcname, t1.nartmc as nartmc, t1.date_st as date_st, price "
                    + " From (Select sklad_hud_tmc.id as idTmc,vid, made, sartmc, tmcname, nartmc, max(date_st) as date_st  "
                    + " 	From  sklad_hud_tmc, sklad_hud_price  "
                    + " 	Where sklad_hud_tmc.id = sklad_hud_price.id_tmc   and "
                    + "               sklad_hud_price.date_st < '" + new java.sql.Date(date) + " 00:00' "
                    + " 	Group by idTmc, vid, made, sartmc, tmcname, nartmc) as t1 "
                    + " left join sklad_hud_price "
                    + " on t1.date_st = sklad_hud_price.date_st and "
                    + "    t1.idTmc = sklad_hud_price.id_tmc "
                    + " Order by tmcname, vid, made, sartmc, nartmc, date_st ";

            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                Vector tmp = new Vector();

                tmp.add(false);
                tmp.add(rs.getInt("idTmc"));
                tmp.add(rs.getInt("vid") == 1 ? UtilSkladHO.SKLAD_VID_1 : UtilSkladHO.SKLAD_VID_2);
                tmp.add(rs.getInt("made") == 1 ? UtilSkladHO.SKLAD_MADE_1 : UtilSkladHO.SKLAD_MADE_2);
                tmp.add(rs.getInt("sartmc"));
                tmp.add(rs.getString("tmcname").trim());
                tmp.add(rs.getString("nartmc").trim());
                tmp.add(new BigDecimal(rs.getDouble("price")).setScale(4, RoundingMode.HALF_UP).doubleValue());
                tmp.add("'" + new SimpleDateFormat("dd-MM-yyyy").format(rs.getDate("date_st")));
                tmc.add(tmp);
            }

        } catch (Exception e) {
            tmc = new Vector();
            System.err.println("Ошибка getPriceForDate() " + e);
            log.error("Ошибка getPriceForDate()", e);
            throw new Exception("Ошибка getPriceForDate() " + e.getMessage(), e);
        }
        return tmc;
    }

    public boolean addMove(long stDate, int tip, int idTmc, Double kolvo, int part, String doc, String note,
                           Object idDept, Object idEmpl, int idUser, int status) throws Exception {

        boolean rezalt = false;
        String sql = "";

        try {

            sql = "Insert into sklad_hud_move(tip_move, date_move, id_tmc, id_dept, id_empl, doc, kolvo, "
                    + "                         kol_part, note, id_empl_vvod, id_empl_ins, status) "
                    + " values( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, tip);
            ps.setDate(2, new java.sql.Date(stDate));
            ps.setInt(3, idTmc);
            ps.setObject(4, idDept);
            ps.setObject(5, idEmpl);
            ps.setString(6, doc);
            ps.setDouble(7, kolvo);
            ps.setInt(8, part);
            ps.setString(9, note);
            ps.setInt(10, idUser);
            ps.setInt(11, idUser);
            ps.setInt(12, status);
            ps.execute();

            rezalt = true;

        } catch (Exception e) {
            rezalt = false;
            System.err.println("Ошибка addMove() " + e);
            log.error("Ошибка addMove()", e);
            throw new Exception("Ошибка addMove() " + e.getMessage(), e);
        }

        return rezalt;
    }

    public boolean addMoveReturnTemp(long stDate, int tip, int idTmc, Double kolvo, int part, String doc, String note,
                                     Object idDept, Object idEmpl, int idUser, int status) throws Exception {

        boolean rezalt = false;
        String sql = "";

        try {

            sql = "Insert into tmp_sklad_hud_move(tip_move, date_move, id_tmc, id_dept, id_empl, doc, kolvo, "
                    + "                         kol_part, note, id_empl_vvod, id_empl_ins, status) "
                    + " values( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, tip);
            ps.setDate(2, new java.sql.Date(stDate));
            ps.setInt(3, idTmc);
            ps.setObject(4, idDept);
            ps.setObject(5, idEmpl);
            ps.setString(6, doc);
            ps.setDouble(7, kolvo);
            ps.setInt(8, part);
            ps.setString(9, note);
            ps.setInt(10, idUser);
            ps.setInt(11, idUser);
            ps.setInt(12, status);
            ps.execute();

            rezalt = true;

        } catch (Exception e) {
            rezalt = false;
            System.err.println("Ошибка addMoveReturnTemp() " + e);
            log.error("Ошибка addMoveReturnTemp()", e);
            throw new Exception("Ошибка addMoveReturnTemp() " + e.getMessage(), e);
        }

        return rezalt;
    }

    public boolean addMoveTMCTemp(long stDate, int tip, String doc, String note,
                                  Object idDept, Object idEmpl, int idUser, int status, Vector dataTMC) throws Exception {

        boolean rezalt = false;
        String sql = "";

        try {
            setAutoCommit(false);

            sql = "Insert into tmp_sklad_hud_move(tip_move, date_move, id_tmc, id_dept, id_empl, doc, kolvo, "
                    + "                         kol_part, note, id_empl_vvod, id_empl_ins, status) "
                    + " values( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

            for (Iterator it = dataTMC.iterator(); it.hasNext(); ) {

                ItemTMC item = (ItemTMC) it.next();

                if (item.getId() > -1) {

                    ps = conn.prepareStatement(sql);
                    ps.setInt(1, tip);
                    ps.setDate(2, new java.sql.Date(stDate));
                    ps.setInt(3, item.getId());
                    ps.setObject(4, idDept);
                    ps.setObject(5, idEmpl);
                    ps.setString(6, doc);
                    ps.setDouble(7, item.getKolvo());
                    ps.setInt(8, item.getPart());
                    ps.setString(9, note);
                    ps.setInt(10, idUser);
                    ps.setInt(11, idUser);
                    ps.setInt(12, status);
                    ps.execute();
                }
            }

            commit();

            rezalt = true;

        } catch (Exception e) {
            rollBack();
            rezalt = false;
            System.err.println("Ошибка addMoveTMCTemp() " + e);
            log.error("Ошибка addMoveTMCTemp()", e);
            throw new Exception("Ошибка addMoveTMCTemp() " + e.getMessage(), e);
        } finally {
            setAutoCommit(true);
        }

        return rezalt;
    }

    public boolean editMove(int id, long stDate, int tip, int idTmc, Double kolvo, int part, String doc, String note,
                            Object idDept, Object idEmpl, int idUser, int status) throws Exception {

        boolean rezalt = false;
        String sql = "";

        try {

            sql = "UPDATE sklad_hud_move SET tip_move = ?, date_move = ?, id_tmc = ?, id_dept = ?, id_empl = ?, "
                    + "   doc = ?, kolvo = ?, kol_part = ?, note = ?, id_empl_ins = ?, date_ins = now(), status = ? "
                    + " Where id = ?";

            ps = conn.prepareStatement(sql);
            ps.setInt(1, tip);
            ps.setDate(2, new java.sql.Date(stDate));
            ps.setInt(3, idTmc);
            ps.setObject(4, idDept);
            ps.setObject(5, idEmpl);
            ps.setString(6, doc);
            ps.setDouble(7, kolvo);
            ps.setInt(8, part);
            ps.setString(9, note);
            ps.setInt(10, idUser);
            ps.setInt(11, status);
            ps.setInt(12, id);
            ps.execute();

            rezalt = true;

        } catch (Exception e) {
            rezalt = false;
            System.err.println("Ошибка editMove() " + e);
            log.error("Ошибка editMove()", e);
            throw new Exception("Ошибка editMove() " + e.getMessage(), e);
        }

        return rezalt;
    }

    public boolean editMoveReturnTemp(int id, long stDate, int tip, int idTmc, Double kolvo, int part, String doc, String note,
                                      Object idDept, Object idEmpl, int idUser, int status) throws Exception {

        boolean rezalt = false;
        String sql = "";

        try {

            sql = "UPDATE tmp_sklad_hud_move SET tip_move = ?, date_move = ?, id_tmc = ?, id_dept = ?, id_empl = ?, "
                    + "   doc = ?, kolvo = ?, kol_part = ?, note = ?, id_empl_ins = ?, date_ins = now(), status = ? "
                    + " Where id = ?";

            ps = conn.prepareStatement(sql);
            ps.setInt(1, tip);
            ps.setDate(2, new java.sql.Date(stDate));
            ps.setInt(3, idTmc);
            ps.setObject(4, idDept);
            ps.setObject(5, idEmpl);
            ps.setString(6, doc);
            ps.setDouble(7, kolvo);
            ps.setInt(8, part);
            ps.setString(9, note);
            ps.setInt(10, idUser);
            ps.setInt(11, status);
            ps.setInt(12, id);
            ps.execute();

            rezalt = true;

        } catch (Exception e) {
            rezalt = false;
            System.err.println("Ошибка editMoveReturnTemp() " + e);
            log.error("Ошибка editMoveReturnTemp()", e);
            throw new Exception("Ошибка editMoveReturnTemp() " + e.getMessage(), e);
        }

        return rezalt;
    }

    public Vector getDataMoveSklad(boolean flagVod, long sDate, long eDate, boolean flagIns,
                                   long sInsDate, long eInsDate, String status) throws Exception {
        Vector data = new Vector();
        String sql = "";

        try {
            sql = "Select idMove, tip_move, vid, made, idTmc, nartmc, sartmc, tmcname, date_move, kolvo, price, date_st_ , date_ins_, kol_part, "
                    + "		doc, moveNote, idDept, department, idEmpl, nameEmpl, ed_izm, status, dV, eV, eIns, idProd "
                    + " From(Select idMove, tip_move, vid, made, idTmc, nartmc, sartmc, tmcname, date_move, kolvo, price, date_st_ , date_ins_, kol_part, "
                    + "		doc, moveNote, idDept, idEmpl, employees.fio as nameEmpl, ed_izm, status, dV, eV, eIns, idProd  "
                    + "     From (Select idMove, tip_move, vid, made, idTmc, nartmc, sartmc, tmcname, date_move, kolvo, price, date_st_ , date_ins_, kol_part, "
                    + "			doc, moveNote, idDept, idEmpl, ed_izm, status, dV, eV, eIns, idProd "
                    + "             From (Select idMove, tip_move, vid, made, idTmc, nartmc, sartmc, tmcname, date_move, kolvo, max(date_st) as date_st_ ,  "
                    + "				date_ins_, kol_part , doc, moveNote, idDept, idEmpl, ed_izm, status, dV, eV, eIns, idProd  "
                    + "                	From (Select distinct sklad_hud_move.id as idMove, tip_move, sklad_hud_tmc.id as idTmc, vid, made, nartmc, sartmc, tmcname, "
                    + "					date_move, kolvo, kol_part, sklad_hud_move.date_ins as date_ins_, doc, "
                    + "					sklad_hud_move.note as moveNote, sklad_hud_move.id_dept as idDept, "
                    + "					sklad_hud_move.id_empl as idEmpl, ed_izm, status, "
                    + "					sklad_hud_move.date_vvod as dV, empl1.fio as eV, empl2.fio as eIns, sklad_hud_move.id_prod as idProd "
                    + "				From  sklad_hud_move, sklad_hud_tmc, employees as empl1, employees as empl2 "//, employees as empl3 "
                    + "				Where sklad_hud_move.id_tmc = sklad_hud_tmc.id and "
                    + "					sklad_hud_move.status::text like ? and "
                    + "					empl1.id = sklad_hud_move.id_empl_vvod and "
                    + "					empl2.id = sklad_hud_move.id_empl_ins "
                    + (flagVod ? " and sklad_hud_move.date_move between '" + new java.sql.Date(sDate) + " 00:00' "
                    + "                                                                            and '" + new java.sql.Date(eDate) + " 23:59' " : " ")
                    + (flagIns ? " and sklad_hud_move.date_ins between '" + new java.sql.Date(sInsDate) + " 00:00' "
                    + "                                                                           and '" + new java.sql.Date(eInsDate) + " 23:59'  " : " ") + " ) as t1 "
                    + "			left join sklad_hud_price on t1.idTmc = sklad_hud_price.id_tmc "
                    + "			Group by idMove, tip_move, idTmc, vid, made, nartmc, sartmc, tmcname, date_move, kolvo, "
                    + "			kol_part, date_ins_, doc, moveNote, idDept, idEmpl, ed_izm, status, dV, eV, eIns, idProd) as t2 "
                    + "             left join sklad_hud_price on t2.idTmc = sklad_hud_price.id_tmc and t2.date_st_ = sklad_hud_price.date_st) as t3  "
                    + " 	left join employees on t3.idEmpl = employees.id) as t4 "
                    + " left join dept on t4.idDept = dept.id  	 "
                    + " Order by date_move, idMove ";

            ps = conn.prepareStatement(sql);
            ps.setString(1, status + "%");
            rs = ps.executeQuery();

            while (rs.next()) {
                Vector tmp = new Vector();
                tmp.add(false);
                tmp.add(rs.getInt("idMove"));
                tmp.add(new SimpleDateFormat("dd-MM-yyyy").format(rs.getDate("date_move")));
                tmp.add(rs.getInt("tip_move"));
                switch (rs.getInt("tip_move")) {
                    case 1:
                        tmp.add(UtilSkladHO.TYPE_MOVE_PR);
                        break;
                    case 2:
                        tmp.add(UtilSkladHO.TYPE_MOVE_RS);
                        break;
                    case 3:
                        tmp.add(UtilSkladHO.TYPE_MOVE_VZ);
                        break;
                    case 4:
                        tmp.add(UtilSkladHO.TYPE_MOVE_AK);
                        break;
                    case 5:
                        tmp.add(UtilSkladHO.TYPE_MOVE_BR);
                        break;
                    default:
                        tmp.add("ошибка");
                        break;
                }

                tmp.add(rs.getInt("idTmc"));
                tmp.add(rs.getString("tmcname").trim());
                tmp.add(rs.getString("nartmc").trim());
                tmp.add(rs.getInt("sartmc"));
                tmp.add(rs.getInt("vid") == 1 ? UtilSkladHO.SKLAD_VID_1 : UtilSkladHO.SKLAD_VID_2);
                tmp.add(rs.getInt("made") == 1 ? UtilSkladHO.SKLAD_MADE_1 : UtilSkladHO.SKLAD_MADE_2);
                tmp.add(rs.getString("ed_izm").trim());
                tmp.add(rs.getDouble("kolvo"));
                tmp.add(UtilSkladHO.getKolvo(rs.getInt("vid"), rs.getInt("made"), rs.getString("ed_izm"), rs.getDouble("kolvo")));
                tmp.add(rs.getInt("kol_part"));
                tmp.add(rs.getDate("date_st_") == null ? "" : new SimpleDateFormat("dd-MM-yyyy").format(rs.getDate("date_st_")));
                tmp.add(new BigDecimal(rs.getDouble("price")).setScale(4, RoundingMode.HALF_UP).doubleValue());
                tmp.add(new BigDecimal(new BigDecimal(rs.getDouble("price")).setScale(4, RoundingMode.HALF_UP).doubleValue() * rs.getDouble("kolvo")).setScale(2, RoundingMode.HALF_UP).doubleValue());
                tmp.add(rs.getObject("idDept") == null ? "-1" : rs.getInt("idDept"));
                tmp.add(rs.getString("department") == null ? "-" : rs.getString("department"));
                tmp.add(rs.getObject("idEmpl") == null ? "" : rs.getInt("idEmpl"));
                tmp.add(rs.getString("nameEmpl") == null ? "" : rs.getString("nameEmpl"));
                tmp.add(rs.getString("doc").trim());
                tmp.add(rs.getString("moveNote").trim());
                tmp.add(new SimpleDateFormat("dd-MM-yyyy").format(rs.getDate("dV")));
                tmp.add(rs.getString("eV").trim());
                tmp.add(new SimpleDateFormat("dd-MM-yyyy").format(rs.getDate("date_ins_")));
                tmp.add(rs.getString("eIns").trim());
                tmp.add(rs.getInt("idProd") == -1 ? "" : rs.getObject("idProd"));

                switch (rs.getInt("status")) {
                    case 0:
                        tmp.add("формируется");
                        break;
                    case 1:
                        tmp.add("закрыт");
                        break;
                    case -1:
                        tmp.add("удалён");
                        break;
                    default:
                        tmp.add("неизвестно");
                        break;
                }
                tmp.add(rs.getInt("status"));
                data.add(tmp);
            }

        } catch (Exception e) {
            data = new Vector();
            System.err.println("Ошибка getDataMoveSklad() " + e);
            log.error("Ошибка getDataMoveSklad()", e);
            throw new Exception("Ошибка getDataMoveSklad() " + e.getMessage(), e);
        }
        return data;
    }

    public Vector getDataProductSklad(boolean flagVod, long sDate, long eDate, boolean flagIns,
                                      long sInsDate, long eInsDate, String status) throws Exception {
        Vector data = new Vector();
        String sql = "";

        try {
            sql = "Select sklad_hud_prod.id as idProduct, date_st, sklad_hud_prod.id_dept as idDept, department, id_empl,  "
                    + "     empl3.fio as empl,  fas,  sar,  nar, vfas, pname,  kolvo,  "
                    + "     shkala,  status,  note, empl1.fio as eVvod,  sklad_hud_prod.date_vvod as dVvod,  "
                    + "     empl2.fio as eIns,  sklad_hud_prod.date_ins as dIns "
                    + "	From sklad_hud_prod, employees as empl1, employees as empl2, employees as empl3, dept "
                    + "	Where empl1.id = sklad_hud_prod.id_empl_vvod and  "
                    + "	      empl2.id = sklad_hud_prod.id_empl_ins and "
                    + "	      empl3.id = sklad_hud_prod.id_empl and "
                    + "	      dept.id = sklad_hud_prod.id_dept and "
                    + "       sklad_hud_prod.status::text like ? "
                    + (flagVod ? " and date_st between '" + new java.sql.Date(sDate) + " 00:00' "
                    + "                                 and '" + new java.sql.Date(eDate) + " 23:59' " : " ")
                    + (flagIns ? "  and sklad_hud_prod.date_ins between '" + new java.sql.Date(sInsDate) + " 00:00' "
                    + "                                                  and '" + new java.sql.Date(eInsDate) + " 23:59'  " : " ")
                    + "	Order by date_st, idProduct ";

            ps = conn.prepareStatement(sql);
            ps.setString(1, status + "%");
            rs = ps.executeQuery();
            while (rs.next()) {
                Vector tmp = new Vector();

                tmp.add(false);
                tmp.add(rs.getInt("idProduct"));
                tmp.add(new SimpleDateFormat("dd-MM-yyyy").format(rs.getDate("date_st")));
                tmp.add(rs.getInt("fas"));
                tmp.add(rs.getString("pname").trim());
                tmp.add(rs.getString("nar").trim());
                tmp.add(rs.getInt("vfas"));
                switch (rs.getInt("vfas")) {
                    case 0:
                        tmp.add("нет");
                        break;
                    case 1:
                        tmp.add("муж.");
                        break;
                    case 2:
                        tmp.add("жен.");
                        break;
                    case 3:
                        tmp.add("дет.");
                        break;
                    default:
                        tmp.add("нет");
                        break;
                }
                tmp.add(rs.getInt("kolvo"));
                tmp.add(rs.getString("shkala").trim());
                tmp.add(rs.getString("note").trim());
                tmp.add(rs.getInt("idDept"));
                tmp.add(rs.getString("department").trim());
                tmp.add(rs.getInt("id_empl"));
                tmp.add(rs.getString("empl").trim());
                tmp.add(new SimpleDateFormat("dd-MM-yyyy").format(rs.getDate("dVvod")));
                tmp.add(rs.getString("eVvod").trim());
                tmp.add(new SimpleDateFormat("dd-MM-yyyy").format(rs.getDate("dIns")));
                tmp.add(rs.getString("eIns").trim());

                switch (rs.getInt("status")) {
                    case 0:
                        tmp.add("формируется");
                        break;
                    case 1:
                        tmp.add("закрыт");
                        break;
                    case -1:
                        tmp.add("удалён");
                        break;
                    default:
                        tmp.add("неизвестно");
                        break;
                }
                tmp.add(rs.getInt("status"));
                data.add(tmp);
            }

        } catch (Exception e) {
            data = new Vector();
            System.err.println("Ошибка getDataProductSklad() " + e);
            log.error("Ошибка getDataProductSklad()", e);
            throw new Exception("Ошибка getDataProductSklad() " + e.getMessage(), e);
        }
        return data;
    }

    public Vector getDataProductSklad(boolean flagVod, long sDate, long eDate, String model, int idStor) throws Exception {
        Vector data = new Vector();
        String sql = "";

        try {
            sql = "Select sklad_hud_prod.id as idProduct, date_st, sklad_hud_prod.id_dept as idDept, department, id_empl,  "
                    + "     empl3.fio as empl,  fas,  sar,  nar, vfas, pname,  kolvo,  "
                    + "     shkala,  status,  note, empl1.fio as eVvod,  sklad_hud_prod.date_vvod as dVvod,  "
                    + "     empl2.fio as eIns,  sklad_hud_prod.date_ins as dIns "
                    + "	From sklad_hud_prod, employees as empl1, employees as empl2, employees as empl3, dept "
                    + "	Where empl1.id = sklad_hud_prod.id_empl_vvod and  "
                    + "	      empl2.id = sklad_hud_prod.id_empl_ins and "
                    + "	      empl3.id = sklad_hud_prod.id_empl and "
                    + "	      dept.id = sklad_hud_prod.id_dept and "
                    + "       sklad_hud_prod.status = 1 and "
                    + "       sklad_hud_prod.fas::text like ? and "
                    + "       sklad_hud_prod.id not in (Select id_prod from sklad_hud_storage_item "
                    + (idStor > 0 ? " Where id_storage <>  " + idStor + " " : " ") + " )  "
                    + (flagVod ? " and date_st between '" + new java.sql.Date(sDate) + " 00:00' "
                    + "                                 and '" + new java.sql.Date(eDate) + " 23:59' " : " ")
                    + "	Order by date_st, idProduct ";

            ps = conn.prepareStatement(sql);
            ps.setString(1, model + "%");
            rs = ps.executeQuery();
            while (rs.next()) {
                Vector tmp = new Vector();

                tmp.add(false);
                tmp.add(rs.getInt("idProduct"));
                tmp.add(new SimpleDateFormat("dd-MM-yyyy").format(rs.getDate("date_st")));
                tmp.add(rs.getInt("fas"));
                tmp.add(rs.getString("pname").trim());
                tmp.add(rs.getString("nar").trim());
                tmp.add(rs.getInt("vfas"));
                switch (rs.getInt("vfas")) {
                    case 0:
                        tmp.add("нет");
                        break;
                    case 1:
                        tmp.add("муж.");
                        break;
                    case 2:
                        tmp.add("жен.");
                        break;
                    case 3:
                        tmp.add("дет.");
                        break;
                    default:
                        tmp.add("нет");
                        break;
                }
                tmp.add(rs.getInt("kolvo"));
                tmp.add(rs.getString("shkala").trim());
                tmp.add(rs.getString("note").trim());
                tmp.add(rs.getInt("idDept"));
                tmp.add(rs.getString("department").trim());
                tmp.add(rs.getInt("id_empl"));
                tmp.add(rs.getString("empl").trim());
                tmp.add(new SimpleDateFormat("dd-MM-yyyy").format(rs.getDate("dVvod")));
                tmp.add(rs.getString("eVvod").trim());
                tmp.add(new SimpleDateFormat("dd-MM-yyyy").format(rs.getDate("dIns")));
                tmp.add(rs.getString("eIns").trim());

                switch (rs.getInt("status")) {
                    case 0:
                        tmp.add("формируется");
                        break;
                    case 1:
                        tmp.add("закрыт");
                        break;
                    case -1:
                        tmp.add("удалён");
                        break;
                    default:
                        tmp.add("неизвестно");
                        break;
                }
                tmp.add(rs.getInt("status"));
                data.add(tmp);
            }

        } catch (Exception e) {
            data = new Vector();
            System.err.println("Ошибка getDataProductSklad() " + e);
            log.error("Ошибка getDataProductSklad()", e);
            throw new Exception("Ошибка getDataProductSklad() " + e.getMessage(), e);
        }
        return data;
    }

    public Vector getDataProductSklad(int idProd) throws Exception {
        Vector tmp = new Vector();
        String sql = "";

        try {
            sql = "Select sklad_hud_prod.id as idProduct, date_st, sklad_hud_prod.id_dept as idDept, department, id_empl,  "
                    + "     empl3.fio as empl,  fas,  sar,  nar, vfas, pname,  kolvo,  "
                    + "     shkala,  status,  note, empl1.fio as eVvod,  sklad_hud_prod.date_vvod as dVvod,  "
                    + "     empl2.fio as eIns,  sklad_hud_prod.date_ins as dIns "
                    + "	From sklad_hud_prod, employees as empl1, employees as empl2, employees as empl3, dept "
                    + "	Where empl1.id = sklad_hud_prod.id_empl_vvod and  "
                    + "	      empl2.id = sklad_hud_prod.id_empl_ins and "
                    + "	      empl3.id = sklad_hud_prod.id_empl and "
                    + "	      dept.id = sklad_hud_prod.id_dept and "
                    + "       sklad_hud_prod.id = ? "
                    + "	Order by date_st, idProduct ";

            ps = conn.prepareStatement(sql);
            ps.setInt(1, idProd);
            rs = ps.executeQuery();

            while (rs.next()) {

                tmp.add(false);
                tmp.add(rs.getInt("idProduct"));
                tmp.add(new SimpleDateFormat("dd-MM-yyyy").format(rs.getDate("date_st")));
                tmp.add(rs.getInt("fas"));
                tmp.add(rs.getString("pname").trim());
                tmp.add(rs.getString("nar").trim());
                tmp.add(rs.getInt("vfas"));
                switch (rs.getInt("vfas")) {
                    case 0:
                        tmp.add("нет");
                        break;
                    case 1:
                        tmp.add("муж.");
                        break;
                    case 2:
                        tmp.add("жен.");
                        break;
                    case 3:
                        tmp.add("дет.");
                        break;
                    default:
                        tmp.add("нет");
                        break;
                }
                tmp.add(rs.getInt("kolvo"));
                tmp.add(rs.getString("shkala").trim());
                tmp.add(rs.getString("note").trim());
                tmp.add(rs.getInt("idDept"));
                tmp.add(rs.getString("department").trim());
                tmp.add(rs.getInt("id_empl"));
                tmp.add(rs.getString("empl").trim());
                tmp.add(new SimpleDateFormat("dd-MM-yyyy").format(rs.getDate("dVvod")));
                tmp.add(rs.getString("eVvod").trim());
                tmp.add(new SimpleDateFormat("dd-MM-yyyy").format(rs.getDate("dIns")));
                tmp.add(rs.getString("eIns").trim());

                switch (rs.getInt("status")) {
                    case 0:
                        tmp.add("формируется");
                        break;
                    case 1:
                        tmp.add("закрыт");
                        break;
                    case -1:
                        tmp.add("удалён");
                        break;
                    default:
                        tmp.add("неизвестно");
                        break;
                }
                tmp.add(rs.getInt("status"));
            }

        } catch (Exception e) {
            tmp = new Vector();
            System.err.println("Ошибка getDataProductSklad() " + e);
            log.error("Ошибка getDataProductSklad()", e);
            throw new Exception("Ошибка getDataProductSklad() " + e.getMessage(), e);
        }
        return tmp;
    }

    public Vector getDataMaterialSklad(boolean flagVod, long sDate, long eDate, boolean flagIns,
                                       long sInsDate, long eInsDate, String status) throws Exception {
        Vector data = new Vector();
        String sql = "";

        try {
            sql = "Select idMaterial, id_prod, dateProd, fas, idTmc, vid, made, sartmc, nartmc, tmcname, ed_izm, "
                    + "     idDept, department, idEmpl, fioEmpl, kol_vo, kol_part, kol_oth, dIns, date_st_, price "
                    + " From(Select idMaterial, id_prod, dateProd, fas, idTmc, vid, made, sartmc, nartmc, tmcname, ed_izm, idDept, department, idEmpl, "
                    + "             fioEmpl, kol_vo, kol_part, kol_oth, dIns, max(sklad_hud_price.date_st) as date_st_ "
                    + "         From(Select sklad_hud_prod_item.id as idMaterial, id_prod, sklad_hud_prod.date_st as dateProd, fas, "
                    + "                     sklad_hud_prod_item.id_tmc as idTmc, sklad_hud_prod.id_dept as idDept, department, "
                    + "                     vid, made,sartmc, nartmc, tmcname, ed_izm, sklad_hud_prod.id_empl as idEmpl, "
                    + "                     empl1.fio as fioEmpl, kol_vo, kol_part, kol_oth, sklad_hud_prod_item.date_ins as dIns "
                    + "               From sklad_hud_prod_item, sklad_hud_prod, sklad_hud_tmc, employees as empl1, dept "
                    + "               Where sklad_hud_tmc.id = sklad_hud_prod_item.id_tmc and "
                    + "                     sklad_hud_prod.id = sklad_hud_prod_item.id_prod and  "
                    + "                     empl1.id = sklad_hud_prod.id_empl and "
                    + "                     sklad_hud_prod.id_dept = dept.id and "
                    + "                     sklad_hud_prod.status::text like ? "
                    + (flagVod ? " and sklad_hud_prod.date_st between '" + new java.sql.Date(sDate) + " 00:00' "
                    + "                                                               and '" + new java.sql.Date(eDate) + " 23:59' " : " ")
                    + (flagIns ? "  and sklad_hud_prod.date_ins between '" + new java.sql.Date(sInsDate) + " 00:00' "
                    + "                                                                 and '" + new java.sql.Date(eInsDate) + " 23:59'  " : " ") + ") as t1"
                    + "         left join sklad_hud_price on t1.idTmc = sklad_hud_price.id_tmc	"
                    + "         Group by idMaterial, id_prod, dateProd, fas, idTmc, vid, made, sartmc, nartmc, tmcname, ed_izm, idDept, department, idEmpl, "
                    + "                  fioEmpl, kol_vo, kol_part, kol_oth, dIns) as t2 "
                    + " left join sklad_hud_price on t2.idTmc = sklad_hud_price.id_tmc and t2.date_st_ = sklad_hud_price.date_st "
                    + " Order by dateProd, id_prod, idMaterial";

            ps = conn.prepareStatement(sql);
            ps.setString(1, status + "%");
            rs = ps.executeQuery();
            while (rs.next()) {
                Vector tmp = new Vector();

                tmp.add(false);
                tmp.add(rs.getInt("idMaterial"));
                tmp.add(rs.getInt("id_prod"));
                tmp.add(new SimpleDateFormat("dd-MM-yyyy").format(rs.getDate("dateProd")));
                tmp.add(rs.getInt("fas"));
                tmp.add(rs.getInt("idTmc"));
                tmp.add(rs.getString("tmcname").trim());
                tmp.add(rs.getString("nartmc").trim());
                tmp.add(rs.getInt("sartmc"));
                tmp.add(rs.getInt("vid") == 1 ? UtilSkladHO.SKLAD_VID_1 : UtilSkladHO.SKLAD_VID_2);
                tmp.add(rs.getInt("made") == 1 ? UtilSkladHO.SKLAD_MADE_1 : UtilSkladHO.SKLAD_MADE_2);
                tmp.add(rs.getString("ed_izm").trim());
                tmp.add(rs.getDouble("kol_vo"));
                tmp.add(rs.getInt("kol_part"));
                tmp.add(rs.getDouble("kol_oth"));
                tmp.add(rs.getDate("date_st_") == null ? "" : new SimpleDateFormat("dd-MM-yyyy").format(rs.getDate("date_st_")));
                tmp.add(new BigDecimal(rs.getDouble("price")).setScale(4, RoundingMode.HALF_UP).doubleValue());
                tmp.add(new BigDecimal(new BigDecimal(rs.getDouble("price")).setScale(4, RoundingMode.HALF_UP).doubleValue() * rs.getDouble("kol_vo")).setScale(2, RoundingMode.HALF_UP).doubleValue());
                tmp.add(rs.getInt("idDept"));
                tmp.add(rs.getString("department").trim());
                tmp.add(rs.getInt("idEmpl"));
                tmp.add(rs.getString("fioEmpl"));
                tmp.add(rs.getString("dIns").trim());
                data.add(tmp);
            }

        } catch (Exception e) {
            data = new Vector();
            System.err.println("Ошибка getDataMaterialSklad() " + e);
            log.error("Ошибка getDataMaterialSklad()", e);
            throw new Exception("Ошибка getDataMaterialSklad() " + e.getMessage(), e);
        }
        return data;
    }


    public Vector getDataIzdSklad(boolean flagVod, long sDate, long eDate, boolean flagIns,
                                  long sInsDate, long eInsDate, String status) throws Exception {

        Vector data = new Vector();
        String sql = "";

        try {
            sql = "Select (Select department from dept where dept.id = idDeptStor) as deptStor, "
                    + "   (Select fio from employees where employees.id = idEmplStor) as emplStor,"
                    + "   (Select fio from employees where employees.id = idVvodStor) as eVvodStor, "
                    + "   (Select fio from employees where employees.id = idInsStor) as eInsStor, "
                    + "    idStor, dateStor, idDeptStor, idEmplStor, docStor, noteStor, idVvodStor, dVvodStor, "
                    + "    idInsStor, dInsStor, statusStor, idProd, fas, sar, nar,  vfas, pname, kolvo, shkala "
                    + " From (Select sklad_hud_storage.id as idStor,  "
                    + "		sklad_hud_storage.date_st as dateStor, "
                    + "		sklad_hud_storage.id_dept as  idDeptStor, "
                    + "		sklad_hud_storage.id_empl as idEmplStor, "
                    + "		sklad_hud_storage.doc as docStor, "
                    + "		sklad_hud_storage.note as noteStor, "
                    + "		sklad_hud_storage.id_empl_vvod as idVvodStor, "
                    + "		sklad_hud_storage.date_vvod as dVvodStor,  "
                    + "		sklad_hud_storage.id_empl_ins as idInsStor,   "
                    + "		sklad_hud_storage.date_ins as dInsStor, "
                    + "		sklad_hud_storage.status as statusStor,"
                    + "		sklad_hud_prod.id as idProd,"
                    + "		sklad_hud_prod.fas as fas ,"
                    + "		sklad_hud_prod.sar as sar,"
                    + "		sklad_hud_prod.nar as nar,"
                    + "		sklad_hud_prod.vfas as vfas,"
                    + "		sklad_hud_prod.pname as pname,"
                    + "		sklad_hud_prod.kolvo as kolvo,"
                    + "		sklad_hud_prod.shkala as shkala"
                    + "     From sklad_hud_storage, sklad_hud_storage_item, sklad_hud_prod "
                    + "     Where sklad_hud_storage.id = sklad_hud_storage_item.id_storage and"
                    + "           sklad_hud_prod.id = sklad_hud_storage_item.id_prod "
                    + (flagVod ? " and sklad_hud_storage.date_st between '" + new java.sql.Date(sDate) + " 00:00' "
                    + "                                                       and '" + new java.sql.Date(eDate) + " 23:59' " : " ")
                    + (flagIns ? "  and sklad_hud_storage.date_ins between '" + new java.sql.Date(sInsDate) + " 00:00' "
                    + "                                                         and '" + new java.sql.Date(eInsDate) + " 23:59'  " : " ") + " ) as t1 "
                    + " Order by dateStor, idStor, fas, nar";

            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                Vector tmp = new Vector();
                tmp.add(false);
                tmp.add(rs.getInt("idStor"));
                tmp.add(new SimpleDateFormat("dd-MM-yyyy").format(rs.getDate("dateStor")));
                tmp.add(rs.getString("docStor").trim());
                tmp.add(rs.getObject("idDeptStor") == null ? "-1" : rs.getInt("idDeptStor"));
                tmp.add(rs.getString("deptStor") == null ? "-" : rs.getString("deptStor"));
                tmp.add(rs.getObject("idEmplStor") == null ? "" : rs.getInt("idEmplStor"));
                tmp.add(rs.getString("emplStor") == null ? "" : rs.getString("emplStor"));
                tmp.add(rs.getInt("idProd"));
                tmp.add(rs.getInt("fas"));
                tmp.add(rs.getString("pname").trim());
                tmp.add(rs.getString("nar").trim());
                tmp.add(rs.getInt("vfas"));
                switch (rs.getInt("vfas")) {
                    case 0:
                        tmp.add("нет");
                        break;
                    case 1:
                        tmp.add("муж.");
                        break;
                    case 2:
                        tmp.add("жен.");
                        break;
                    case 3:
                        tmp.add("дет.");
                        break;
                    default:
                        tmp.add("нет");
                        break;
                }
                tmp.add(rs.getInt("kolvo"));
                tmp.add(rs.getString("shkala").trim());
                tmp.add(rs.getString("noteStor").trim());
                tmp.add(new SimpleDateFormat("dd-MM-yyyy").format(rs.getDate("dVvodStor")));
                tmp.add(rs.getString("eVvodStor").trim());
                tmp.add(new SimpleDateFormat("dd-MM-yyyy").format(rs.getDate("dInsStor")));
                tmp.add(rs.getString("eInsStor").trim());

                switch (rs.getInt("statusStor")) {
                    case 0:
                        tmp.add("формируется");
                        break;
                    case 1:
                        tmp.add("закрыт");
                        break;
                    case -1:
                        tmp.add("удалён");
                        break;
                    default:
                        tmp.add("неизвестно");
                        break;
                }
                tmp.add(rs.getInt("statusStor"));
                data.add(tmp);
            }

        } catch (Exception e) {
            data = new Vector();
            System.err.println("Ошибка getDataIzdSklad() " + e);
            log.error("Ошибка getDataIzdSklad()", e);
            throw new Exception("Ошибка getDataIzdSklad() " + e.getMessage(), e);
        }
        return data;
    }

    public boolean createTempSkladMoveTable(int idProduct) throws Exception {
        boolean rezalt = false;
        String sql = "";
        try {
            setAutoCommit(false);

            //--------------------------------------план

            //удаляем временную таблицу если она есть
            sql = " drop table if exists tmp_sklad_hud_move";
            ps = conn.prepareStatement(sql);
            ps.execute();

            //создаем временную таблицу
            sql = "CREATE temp table tmp_sklad_hud_move("
                    + "  id serial NOT NULL, "
                    + "  tip_move integer NOT NULL DEFAULT (3), "
                    + "  date_move date NOT NULL, "
                    + "  id_tmc integer NOT NULL, "
                    + "  id_dept integer, "
                    + "  id_empl integer, "
                    + "  doc character varying(100) NOT NULL, "
                    + "  kolvo double precision NOT NULL, "
                    + "  kol_part integer NOT NULL, "
                    + "  note character varying(250) NOT NULL, "
                    + "  id_empl_vvod integer NOT NULL, "
                    + "  date_vvod timestamp without time zone NOT NULL DEFAULT now(), "
                    + "  id_empl_ins integer, "
                    + "  date_ins timestamp without time zone NOT NULL DEFAULT now(), "
                    + "  status integer NOT NULL, "
                    + "  id_prod integer NOT NULL DEFAULT (-1), "
                    + "  CONSTRAINT sklad_hud_move_pkey PRIMARY KEY (id)) ";

            ps = conn.prepareStatement(sql);
            ps.execute();

            //заполняем временную таблицу
            if (idProduct != -1) {
                sql = "Select tip_move, date_move, id_tmc, id_dept, id_empl, doc, kolvo, kol_part, note,"
                        + " id_empl_vvod, date_vvod, id_empl_ins,date_ins, status, id_prod "
                        + " From sklad_hud_move Where id_prod = ? ";
                ps = conn.prepareStatement(sql);
                ps.setInt(1, idProduct);
                rs = ps.executeQuery();

                while (rs.next()) {
                    sql = "Insert into tmp_sklad_hud_move(tip_move, date_move, id_tmc, id_dept, id_empl, doc, kolvo, "
                            + "         kol_part, note, id_empl_vvod, date_vvod, id_empl_ins, date_ins, status, id_prod) "
                            + " values( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

                    ps = conn.prepareStatement(sql);
                    ps.setInt(1, rs.getInt("tip_move"));
                    ps.setDate(2, rs.getDate("date_move"));
                    ps.setInt(3, rs.getInt("id_tmc"));
                    ps.setObject(4, rs.getObject("id_dept"));
                    ps.setObject(5, rs.getObject("id_empl"));
                    ps.setString(6, rs.getString("doc"));
                    ps.setDouble(7, rs.getDouble("kolvo"));
                    ps.setInt(8, rs.getInt("kol_part"));
                    ps.setString(9, rs.getString("note"));
                    ps.setInt(10, rs.getInt("id_empl_vvod"));
                    ps.setDate(11, rs.getDate("date_vvod"));
                    ps.setInt(12, rs.getInt("id_empl_ins"));
                    ps.setDate(13, rs.getDate("date_ins"));
                    ps.setInt(14, rs.getInt("status"));
                    ps.setInt(15, rs.getInt("id_prod"));
                    ps.execute();
                }
            }
            commit();

            rezalt = true;
        } catch (Exception e) {
            rollBack();
            rezalt = false;
            System.err.println("Ошибка createTempSkladMoveTable() " + e);
            log.error("Ошибка createTempSkladMoveTable()", e);
            throw new Exception("Ошибка createTempSkladMoveTable() " + e.getMessage(), e);
        } finally {
            setAutoCommit(true);
        }
        return rezalt;
    }

    public Vector getDataProductReturnTable(int idProduct) throws Exception {
        Vector element = new Vector();
        String sql = "";

        try {

            sql = "Select idMove, tip_move, vid, made, idTmc, nartmc, sartmc, tmcname, date_move, kolvo, date_ins_, kol_part, "
                    + "     doc, moveNote, idDept, department, idEmpl, nameEmpl, ed_izm, status, dV, eV, eIns, idProd "
                    + " From( Select idMove, tip_move, vid, made, idTmc, nartmc, sartmc, tmcname, date_move, kolvo, date_ins_, kol_part, "
                    + "              doc, moveNote, idDept, idEmpl, employees.fio as nameEmpl, ed_izm, status, dV, eV, eIns, idProd "
                    + "      From (Select distinct sklad_hud_move.id as idMove, tip_move, sklad_hud_tmc.id as idTmc, vid, made, nartmc, "
                    + "                        sartmc, tmcname, date_move, kolvo, kol_part, sklad_hud_move.date_ins as date_ins_, doc, "
                    + "                        sklad_hud_move.note as moveNote, sklad_hud_move.id_dept as idDept, "
                    + "                        sklad_hud_move.id_empl as idEmpl, ed_izm, status, sklad_hud_move.date_vvod as dV, "
                    + "                        empl1.fio as eV, empl2.fio as eIns, sklad_hud_move.id_prod as idProd "
                    + "                  From  sklad_hud_move, sklad_hud_tmc, employees as empl1, employees as empl2, employees as empl3  "
                    + "                  Where sklad_hud_move.id_tmc = sklad_hud_tmc.id and "
                    // + "                        sklad_hud_move.tip_move = 3 and "
                    + "                        sklad_hud_move.id_prod = ? and "
                    + "                        empl1.id = sklad_hud_move.id_empl_vvod and "
                    + "                        empl2.id = sklad_hud_move.id_empl_ins  ) as t1 "
                    + "     left join employees on t1.idEmpl = employees.id) as t2 "
                    + " left join dept on t2.idDept = dept.id"
                    + " Order by date_move, idMove ";

            ps = conn.prepareStatement(sql);
            ps.setInt(1, idProduct);
            rs = ps.executeQuery();

            while (rs.next()) {
                Vector tmp = new Vector();
                tmp.add(false);
                tmp.add(rs.getInt("idMove"));
                tmp.add(new SimpleDateFormat("dd-MM-yyyy").format(rs.getDate("date_move")));
                tmp.add(rs.getInt("tip_move"));
                switch (rs.getInt("tip_move")) {
                    case 1:
                        tmp.add(UtilSkladHO.TYPE_MOVE_PR);
                        break;
                    case 2:
                        tmp.add(UtilSkladHO.TYPE_MOVE_RS);
                        break;
                    case 3:
                        tmp.add(UtilSkladHO.TYPE_MOVE_VZ);
                        break;
                    case 4:
                        tmp.add(UtilSkladHO.TYPE_MOVE_AK);
                        break;
                    case 5:
                        tmp.add(UtilSkladHO.TYPE_MOVE_BR);
                        break;
                    default:
                        tmp.add("ошибка");
                        break;
                }

                tmp.add(rs.getInt("idTmc"));
                tmp.add(rs.getString("tmcname").trim());
                tmp.add(rs.getString("nartmc").trim());
                tmp.add(rs.getInt("sartmc"));
                tmp.add(rs.getInt("vid") == 1 ? UtilSkladHO.SKLAD_VID_1 : UtilSkladHO.SKLAD_VID_2);
                tmp.add(rs.getInt("made") == 1 ? UtilSkladHO.SKLAD_MADE_1 : UtilSkladHO.SKLAD_MADE_2);
                tmp.add(rs.getString("ed_izm").trim());
                tmp.add(rs.getDouble("kolvo"));
                tmp.add(UtilSkladHO.getKolvo(rs.getInt("vid"), rs.getInt("made"), rs.getString("ed_izm").trim(), rs.getDouble("kolvo")));
                tmp.add(rs.getInt("kol_part"));
                tmp.add("-");
                tmp.add(new Double(0));
                tmp.add(0);
                tmp.add(rs.getObject("idDept") == null ? "-1" : rs.getInt("idDept"));
                tmp.add(rs.getString("department") == null ? "-" : rs.getString("department"));
                tmp.add(rs.getObject("idEmpl") == null ? "" : rs.getInt("idEmpl"));
                tmp.add(rs.getString("nameEmpl") == null ? "" : rs.getString("nameEmpl"));
                tmp.add(rs.getString("doc").trim());
                tmp.add(rs.getString("moveNote").trim());
                tmp.add(new SimpleDateFormat("dd-MM-yyyy").format(rs.getDate("dV")));
                tmp.add(rs.getString("eV").trim());
                tmp.add(new SimpleDateFormat("dd-MM-yyyy").format(rs.getDate("date_ins_")));
                tmp.add(rs.getString("eIns").trim());
                tmp.add(rs.getInt("idProd") == -1 ? "" : rs.getObject("idProd"));

                switch (rs.getInt("status")) {
                    case 0:
                        tmp.add("формируется");
                        break;
                    case 1:
                        tmp.add("закрыт");
                        break;
                    case -1:
                        tmp.add("удалён");
                        break;
                    default:
                        tmp.add("неизвестно");
                        break;
                }
                tmp.add(rs.getInt("status"));
                element.add(tmp);
            }

        } catch (Exception e) {
            System.err.println("Ошибка getDataProductReturnTable() " + e);
            log.error("Ошибка getDataProductReturnTable()", e);
            throw new Exception("Ошибка getDataProductReturnTable() " + e.getMessage(), e);
        }
        return element;
    }

    public Vector getDataProductReturnTableTemp() throws Exception {
        Vector element = new Vector();
        String sql = "";

        try {

            sql = "Select idMove, tip_move, vid, made, idTmc, nartmc, sartmc, tmcname, date_move, kolvo, date_ins_, kol_part, "
                    + "     doc, moveNote, idDept, department, idEmpl, nameEmpl, ed_izm, status, dV, eV, eIns, idProd "
                    + " From( Select idMove, tip_move, vid, made, idTmc, nartmc, sartmc, tmcname, date_move, kolvo, date_ins_, kol_part, "
                    + "              doc, moveNote, idDept, idEmpl, employees.fio as nameEmpl, ed_izm, status, dV, eV, eIns, idProd "
                    + "      From (Select distinct tmp_sklad_hud_move.id as idMove, tip_move, sklad_hud_tmc.id as idTmc, vid, made, nartmc, "
                    + "                        sartmc, tmcname, date_move, kolvo, kol_part, tmp_sklad_hud_move.date_ins as date_ins_, doc, "
                    + "                        tmp_sklad_hud_move.note as moveNote, tmp_sklad_hud_move.id_dept as idDept, "
                    + "                        tmp_sklad_hud_move.id_empl as idEmpl, ed_izm, status,"
                    + "                        tmp_sklad_hud_move.date_vvod as dV, empl1.fio as eV, "
                    + "                        empl2.fio as eIns, tmp_sklad_hud_move.id_prod as idProd "
                    + "                  From  tmp_sklad_hud_move, sklad_hud_tmc, employees as empl1, employees as empl2, employees as empl3  "
                    + "                  Where tmp_sklad_hud_move.id_tmc = sklad_hud_tmc.id and "
                    //  + "                        tmp_sklad_hud_move.tip_move = 3 and "
                    + "                        empl1.id = tmp_sklad_hud_move.id_empl_vvod and "
                    + "                        empl2.id = tmp_sklad_hud_move.id_empl_ins  ) as t1 "
                    + "     left join employees on t1.idEmpl = employees.id) as t2 "
                    + " left join dept on t2.idDept = dept.id"
                    + " Order by date_move, idMove ";

            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                Vector tmp = new Vector();
                tmp.add(false);
                tmp.add(rs.getInt("idMove"));
                tmp.add(new SimpleDateFormat("dd-MM-yyyy").format(rs.getDate("date_move")));
                tmp.add(rs.getInt("tip_move"));
                switch (rs.getInt("tip_move")) {
                    case 1:
                        tmp.add(UtilSkladHO.TYPE_MOVE_PR);
                        break;
                    case 2:
                        tmp.add(UtilSkladHO.TYPE_MOVE_RS);
                        break;
                    case 3:
                        tmp.add(UtilSkladHO.TYPE_MOVE_VZ);
                        break;
                    case 4:
                        tmp.add(UtilSkladHO.TYPE_MOVE_AK);
                        break;
                    case 5:
                        tmp.add(UtilSkladHO.TYPE_MOVE_BR);
                        break;
                    default:
                        tmp.add("ошибка");
                        break;
                }

                tmp.add(rs.getInt("idTmc"));
                tmp.add(rs.getString("tmcname").trim());
                tmp.add(rs.getString("nartmc").trim());
                tmp.add(rs.getInt("sartmc"));
                tmp.add(rs.getInt("vid") == 1 ? UtilSkladHO.SKLAD_VID_1 : UtilSkladHO.SKLAD_VID_2);
                tmp.add(rs.getInt("made") == 1 ? UtilSkladHO.SKLAD_MADE_1 : UtilSkladHO.SKLAD_MADE_2);
                tmp.add(rs.getString("ed_izm").trim());
                tmp.add(rs.getDouble("kolvo"));
                tmp.add(UtilSkladHO.getKolvo(rs.getInt("vid"), rs.getInt("made"), rs.getString("ed_izm").trim(), rs.getDouble("kolvo")));
                tmp.add(rs.getInt("kol_part"));
                tmp.add("-");
                tmp.add(new Double(0));
                tmp.add(0);
                tmp.add(rs.getObject("idDept") == null ? "-1" : rs.getInt("idDept"));
                tmp.add(rs.getString("department") == null ? "-" : rs.getString("department"));
                tmp.add(rs.getObject("idEmpl") == null ? "" : rs.getInt("idEmpl"));
                tmp.add(rs.getString("nameEmpl") == null ? "" : rs.getString("nameEmpl"));
                tmp.add(rs.getString("doc").trim());
                tmp.add(rs.getString("moveNote").trim());
                tmp.add(new SimpleDateFormat("dd-MM-yyyy").format(rs.getDate("dV")));
                tmp.add(rs.getString("eV").trim());
                tmp.add(new SimpleDateFormat("dd-MM-yyyy").format(rs.getDate("date_ins_")));
                tmp.add(rs.getString("eIns").trim());
                tmp.add(rs.getInt("idProd") == -1 ? "" : rs.getObject("idProd"));

                switch (rs.getInt("status")) {
                    case 0:
                        tmp.add("формируется");
                        break;
                    case 1:
                        tmp.add("закрыт");
                        break;
                    case -1:
                        tmp.add("удалён");
                        break;
                    default:
                        tmp.add("неизвестно");
                        break;
                }
                tmp.add(rs.getInt("status"));
                element.add(tmp);
            }

        } catch (Exception e) {
            System.err.println("Ошибка getDataProductReturnTableTemp() " + e);
            log.error("Ошибка getDataProductReturnTableTemp()", e);
            throw new Exception("Ошибка getDataProductReturnTableTemp() " + e.getMessage(), e);
        }
        return element;
    }

    public Vector getDataProductMaterialTable(int idProduct) throws Exception {
        Vector element = new Vector();
        String sql = "";

        try {
            sql = "Select sklad_hud_prod_item.id as idItem, num, id_tmc, tmcname, sartmc, nartmc, "
                    + "   vid, made, ed_izm, kol_vo, kol_part, kol_oth, doc, sklad_hud_prod_item.note as note "
                    + " From sklad_hud_prod_item, sklad_hud_tmc"
                    + " Where sklad_hud_prod_item.id_tmc = sklad_hud_tmc.id and "
                    + "       sklad_hud_prod_item.id_prod = ? "
                    + " Order by num, tmcname, idItem ";

            ps = conn.prepareStatement(sql);
            ps.setInt(1, idProduct);
            rs = ps.executeQuery();

            while (rs.next()) {
                Vector tmp = new Vector();
                tmp.add(false);
                tmp.add(rs.getInt("num"));
                tmp.add(rs.getInt("id_tmc"));
                tmp.add(rs.getString("tmcname").trim());
                tmp.add(rs.getString("nartmc").trim());
                tmp.add(rs.getInt("sartmc"));
                tmp.add(rs.getInt("vid") == 1 ? UtilSkladHO.SKLAD_VID_1 : UtilSkladHO.SKLAD_VID_2);
                tmp.add(rs.getInt("made") == 1 ? UtilSkladHO.SKLAD_MADE_1 : UtilSkladHO.SKLAD_MADE_2);
                tmp.add(rs.getString("ed_izm").trim());
                tmp.add(rs.getDouble("kol_vo"));
                tmp.add(UtilSkladHO.getKolvo(rs.getInt("vid"), rs.getInt("made"), rs.getString("ed_izm").trim(), rs.getDouble("kol_vo")));
                tmp.add(rs.getInt("kol_part"));
                tmp.add(rs.getDouble("kol_oth"));
                tmp.add(rs.getInt("idItem"));
                tmp.add(rs.getString("doc").trim());
                tmp.add(rs.getString("note").trim());
                element.add(tmp);
            }

        } catch (Exception e) {
            System.err.println("Ошибка getDataProductMaterialTable() " + e);
            log.error("Ошибка getDataProductMaterialTable()", e);
            throw new Exception("Ошибка getDataProductMaterialTable() " + e.getMessage(), e);
        }
        return element;
    }

    public Vector getDataStorageModelTable(int idStorage) throws Exception {
        Vector data = new Vector();
        String sql = "";

        try {
            sql = "Select sklad_hud_prod.id as idProduct, sklad_hud_prod.date_st as date_st, "
                    + "     sklad_hud_prod.id_dept as idDept, department, sklad_hud_prod.id_empl as id_empl,  "
                    + "     empl3.fio as empl, fas, sar, nar, vfas, pname, kolvo,  "
                    + "     shkala, sklad_hud_prod.status as status, sklad_hud_prod.note as note, "
                    + "     empl1.fio as eVvod, sklad_hud_prod.date_vvod as dVvod,  "
                    + "     empl2.fio as eIns, sklad_hud_prod.date_ins as dIns, fas_s, pname_s "
                    + "	From sklad_hud_prod, employees as empl1, employees as empl2, employees as empl3, dept, "
                    + "       sklad_hud_storage_item, sklad_hud_storage "
                    + "	Where empl1.id = sklad_hud_prod.id_empl_vvod and  "
                    + "	      empl2.id = sklad_hud_prod.id_empl_ins and "
                    + "	      empl3.id = sklad_hud_prod.id_empl and "
                    + "	      dept.id = sklad_hud_prod.id_dept and "
                    + "       sklad_hud_storage_item.id_prod = sklad_hud_prod.id and "
                    + "       sklad_hud_storage.id = sklad_hud_storage_item.id_storage and "
                    + "       sklad_hud_storage.id = ? "
                    + "	Order by fas, sar, date_st  ";

            ps = conn.prepareStatement(sql);
            ps.setInt(1, idStorage);
            rs = ps.executeQuery();
            while (rs.next()) {

                Vector tmp = new Vector();
                tmp.add(false);
                tmp.add(rs.getInt("idProduct"));
                tmp.add(new SimpleDateFormat("dd-MM-yyyy").format(rs.getDate("date_st")));
                tmp.add(rs.getInt("fas"));
                tmp.add(rs.getString("pname").trim());
                tmp.add(rs.getString("nar").trim());
                tmp.add(rs.getInt("vfas"));
                switch (rs.getInt("vfas")) {
                    case 0:
                        tmp.add("нет");
                        break;
                    case 1:
                        tmp.add("муж.");
                        break;
                    case 2:
                        tmp.add("жен.");
                        break;
                    case 3:
                        tmp.add("дет.");
                        break;
                    default:
                        tmp.add("нет");
                        break;
                }
                tmp.add(rs.getInt("kolvo"));
                tmp.add(rs.getString("shkala").trim());
                tmp.add(rs.getString("note").trim());
                tmp.add(rs.getInt("idDept"));
                tmp.add(rs.getString("department").trim());
                tmp.add(rs.getInt("id_empl"));
                tmp.add(rs.getString("empl").trim());
                tmp.add(new SimpleDateFormat("dd-MM-yyyy").format(rs.getDate("dVvod")));
                tmp.add(rs.getString("eVvod").trim());
                tmp.add(new SimpleDateFormat("dd-MM-yyyy").format(rs.getDate("dIns")));
                tmp.add(rs.getString("eIns").trim());

                switch (rs.getInt("status")) {
                    case 0:
                        tmp.add("формируется");
                        break;
                    case 1:
                        tmp.add("закрыт");
                        break;
                    case -1:
                        tmp.add("удалён");
                        break;
                    default:
                        tmp.add("неизвестно");
                        break;
                }
                tmp.add(rs.getInt("status"));
                tmp.add(rs.getInt("fas_s") == -1 ? "" : rs.getInt("fas_s"));
                tmp.add(rs.getInt("fas_s") == -1 ? "" : rs.getString("pname_s").trim());
                data.add(tmp);
            }

        } catch (Exception e) {
            System.err.println("Ошибка getDataStorageModelTable() " + e);
            log.error("Ошибка getDataStorageModelTable()", e);
            throw new Exception("Ошибка getDataStorageModelTable() " + e.getMessage(), e);
        }
        return data;
    }

    public boolean addSkladProduct(long stDate, int status, int idDept, int numEmpl, int numFas,
                                   String nameFas, String narFas, int vidFas, int kolvo, String shkala, String note,
                                   Vector dataItem, int idUser) throws Exception {

        boolean rezalt = false;
        String sql = "";
        ResultSet rs_1 = null;

        try {
            setAutoCommit(false);

            sql = "Insert into sklad_hud_prod(date_st, id_dept, id_empl, fas, sar, nar, vfas, pname, kolvo, shkala, "
                    + "                         status, note, id_empl_vvod, id_empl_ins) "
                    + "values( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) returning id";
            ps = conn.prepareStatement(sql);
            ps.setDate(1, new java.sql.Date(stDate));
            ps.setInt(2, idDept);
            ps.setInt(3, numEmpl);
            ps.setInt(4, numFas);
            ps.setInt(5, 0);
            ps.setString(6, narFas.trim().toUpperCase());
            ps.setInt(7, vidFas);
            ps.setString(8, nameFas.trim().toUpperCase());
            ps.setInt(9, kolvo);
            ps.setString(10, shkala.trim().toLowerCase());
            ps.setInt(11, status);
            ps.setString(12, note.trim());
            ps.setInt(13, idUser);
            ps.setInt(14, idUser);
            rs = ps.executeQuery();

            if (rs.next()) {
                sql = "Insert into sklad_hud_prod_item (id_prod, num, id_tmc, kol_vo ,kol_part, "
                        + "                             kol_oth, note, doc, id_empl_vvod, id_empl_ins) "
                        + " values( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                for (Iterator it = dataItem.iterator(); it.hasNext(); ) {
                    Vector vec = (Vector) it.next();

                    ps = conn.prepareStatement(sql);
                    ps.setInt(1, rs.getInt(1));
                    ps.setInt(2, Integer.valueOf(vec.get(1).toString()));
                    ps.setInt(3, Integer.valueOf(vec.get(2).toString()));
                    ps.setDouble(4, Double.valueOf(vec.get(9).toString()));
                    ps.setInt(5, Integer.valueOf(vec.get(11).toString()));
                    ps.setDouble(6, Double.valueOf(vec.get(12).toString()));
                    ps.setString(7, vec.get(14).toString());
                    ps.setString(8, vec.get(15).toString());
                    ps.setInt(9, idUser);
                    ps.setInt(10, idUser);
                    ps.execute();
                }

                sql = "Select id, tip_move, date_move, id_tmc, id_dept, id_empl, doc, kolvo, kol_part, note,"
                        + " id_empl_vvod, date_vvod, id_empl_ins,date_ins, status "
                        + " From tmp_sklad_hud_move ";
                ps = conn.prepareStatement(sql);
                rs_1 = ps.executeQuery();

                while (rs_1.next()) {
                    sql = "Insert into sklad_hud_move(tip_move, date_move, id_tmc, id_dept, id_empl, doc, kolvo, "
                            + "         kol_part, note, id_empl_vvod, date_vvod, id_empl_ins, date_ins, status, id_prod) "
                            + " values( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

                    ps = conn.prepareStatement(sql);

                    ps.setInt(1, rs_1.getInt("tip_move"));
                    ps.setDate(2, rs_1.getDate("date_move"));
                    ps.setInt(3, rs_1.getInt("id_tmc"));
                    ps.setObject(4, idDept);
                    ps.setObject(5, numEmpl);
                    ps.setString(6, rs_1.getString("doc"));
                    ps.setDouble(7, rs_1.getDouble("kolvo"));
                    ps.setInt(8, rs_1.getInt("kol_part"));
                    ps.setString(9, rs_1.getString("note"));
                    ps.setInt(10, rs_1.getInt("id_empl_vvod"));
                    ps.setDate(11, rs_1.getDate("date_vvod"));
                    ps.setInt(12, rs_1.getInt("id_empl_ins"));
                    ps.setDate(13, rs_1.getDate("date_ins"));
                    ps.setInt(14, status);
                    ps.setInt(15, rs.getInt(1));
                    ps.execute();
                }

            } else
                throw new Exception();


            commit();

            rezalt = true;
        } catch (Exception e) {
            rezalt = false;
            rollBack();
            System.err.println("Ошибка addSkladProduct() " + e);
            log.error("Ошибка addSkladProduct()", e);
            throw new Exception("Ошибка addSkladProduct() " + e.getMessage(), e);
        } finally {
            setAutoCommit(true);
        }
        return rezalt;
    }

    boolean editSkladProduct(int id, long stDate, int status, int idDept, int numEmpl, int numFas,
                             String nameFas, String narFas, int vidFas, int kolvo, String shkala, String note,
                             Vector dataItem, int idUser) throws Exception {

        boolean rezalt = false;
        String sql = "";

        try {
            setAutoCommit(false);

            sql = "UPDATE sklad_hud_prod SET date_st = ?, id_dept = ?, id_empl = ?, fas = ?,"
                    + "     sar = ?, nar = ?, vfas = ?, pname = ?, kolvo = ?, shkala = ?, "
                    + "     note = ?,  id_empl_ins = ?, date_ins = now(), status = ? "
                    + " Where id = ?";
            ps = conn.prepareStatement(sql);
            ps.setDate(1, new java.sql.Date(stDate));
            ps.setInt(2, idDept);
            ps.setInt(3, numEmpl);
            ps.setInt(4, numFas);
            ps.setInt(5, 0);
            ps.setString(6, narFas.trim().toUpperCase());
            ps.setInt(7, vidFas);
            ps.setString(8, nameFas.trim().toUpperCase());
            ps.setInt(9, kolvo);
            ps.setString(10, shkala.trim().toLowerCase());
            ps.setString(11, note.trim());
            ps.setInt(12, idUser);
            ps.setInt(13, status);
            ps.setInt(14, id);
            ps.execute();

            sql = "Delete From sklad_hud_prod_item Where id_prod = ?";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            ps.execute();

            sql = "Insert into sklad_hud_prod_item (id_prod, num, id_tmc, kol_vo ,kol_part, "
                    + "                             kol_oth, note, doc, id_empl_vvod, id_empl_ins) "
                    + " values( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            for (Iterator it = dataItem.iterator(); it.hasNext(); ) {
                Vector vec = (Vector) it.next();

                ps = conn.prepareStatement(sql);
                ps.setInt(1, id);
                ps.setInt(2, Integer.valueOf(vec.get(1).toString()));
                ps.setInt(3, Integer.valueOf(vec.get(2).toString()));
                ps.setDouble(4, Double.valueOf(vec.get(9).toString()));
                ps.setInt(5, Integer.valueOf(vec.get(11).toString()));
                ps.setDouble(6, Double.valueOf(vec.get(12).toString()));
                ps.setString(7, vec.get(14).toString());
                ps.setString(8, vec.get(15).toString());
                ps.setInt(9, idUser);
                ps.setInt(10, idUser);
                ps.execute();
            }

            sql = "Delete From sklad_hud_move Where id_prod = ?";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            ps.execute();

            sql = "Select tip_move, date_move, id_tmc, id_dept, id_empl, doc, kolvo, kol_part, note,"
                    + " id_empl_vvod, date_vvod, id_empl_ins,date_ins, status, id_prod "
                    + " From tmp_sklad_hud_move ";
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                sql = "Insert into sklad_hud_move(tip_move, date_move, id_tmc, id_dept, id_empl, doc, kolvo, "
                        + "         kol_part, note, id_empl_vvod, date_vvod, id_empl_ins, date_ins, status, id_prod) "
                        + " values( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

                ps = conn.prepareStatement(sql);

                ps.setInt(1, rs.getInt("tip_move"));
                ps.setDate(2, rs.getDate("date_move"));
                ps.setInt(3, rs.getInt("id_tmc"));
                ps.setObject(4, idDept);
                ps.setObject(5, numEmpl);
                ps.setString(6, rs.getString("doc"));
                ps.setDouble(7, rs.getDouble("kolvo"));
                ps.setInt(8, rs.getInt("kol_part"));
                ps.setString(9, rs.getString("note"));
                ps.setInt(10, rs.getInt("id_empl_vvod"));
                ps.setDate(11, rs.getDate("date_vvod"));
                ps.setInt(12, rs.getInt("id_empl_ins"));
                ps.setDate(13, rs.getDate("date_ins"));
                ps.setInt(14, status);
                ps.setInt(15, id);
                ps.execute();
            }

            commit();

            rezalt = true;
        } catch (Exception e) {
            rezalt = false;
            rollBack();
            System.err.println("Ошибка editSkladProduct() " + e);
            log.error("Ошибка editSkladProduct()", e);
            throw new Exception("Ошибка editSkladProduct() " + e.getMessage(), e);
        } finally {
            setAutoCommit(true);
        }
        return rezalt;
    }

    /**
     * Возвращает остатки на нач. периода.
     * @param sDate - месяц
     * @param flag - паказывать/не показывать нулевые остатки
     * @return
     * @throws Exception
     */
    public Vector getOst1DateStart(long sDate, boolean flag) throws Exception {
        Vector data = new Vector();
        String sql = "";

        try {
            sql = "Select  sklad_hud_tmc.id as idTmc, id_group, "
                    + "     (Select tmc_group from sklad_hud_tmc_group where sklad_hud_tmc_group.id = id_group) as tmc_group, "
                    + "     tmcname, nartmc, sartmc, vid, made, ed_izm, ost, ost_part "
                    + " From sklad_hud_ost, sklad_hud_tmc "
                    + " Where sklad_hud_tmc.id = sklad_hud_ost.id_tmc and "
                    + "       sklad_hud_ost.date_f = ? and "
                    + "       sklad_hud_ost.tip_ost = 1 "
                    + (flag ? " " : " and sklad_hud_ost.ost <> 0 ")
                    + " Order by tmc_group, tmcname, nartmc, id_group  ";

            ps = conn.prepareStatement(sql);
            ps.setDate(1, new java.sql.Date(sDate));
            rs = ps.executeQuery();

            while (rs.next()) {
                Vector tmp = new Vector();
                tmp.add(false);
                tmp.add(rs.getObject("tmc_group") == null ? "" : rs.getString("tmc_group").trim());
                tmp.add(rs.getInt("idTmc"));
                tmp.add(rs.getString("tmcname").trim());
                tmp.add(rs.getString("nartmc").trim());
                tmp.add(rs.getInt("sartmc"));
                tmp.add(rs.getInt("vid") == 1 ? UtilSkladHO.SKLAD_VID_1 : UtilSkladHO.SKLAD_VID_2);
                tmp.add(rs.getInt("made") == 1 ? UtilSkladHO.SKLAD_MADE_1 : UtilSkladHO.SKLAD_MADE_2);
                tmp.add(rs.getString("ed_izm").trim());
                tmp.add(new BigDecimal(rs.getDouble("ost")).setScale(3, RoundingMode.HALF_UP).doubleValue());
                tmp.add(new Double(0));
                tmp.add(new Double(0));
                tmp.add(new BigDecimal(rs.getDouble("ost")).setScale(3, RoundingMode.HALF_UP).doubleValue());
                tmp.add(UtilSkladHO.getKolvo(rs.getInt("vid"), rs.getInt("made"), rs.getString("ed_izm"), rs.getDouble("ost")));
                tmp.add(rs.getDouble("ost_part"));
                data.add(tmp);
            }

        } catch (Exception e) {
            data = new Vector();
            System.err.println("Ошибка getOst1DateStart() " + e);
            log.error("Ошибка getOst1DateStart()", e);
            throw new Exception("Ошибка getOst1DateStart() " + e.getMessage(), e);
        }
        return data;
    }

    /**
     * Возвращает остатки на нач. периода. с ценами
     * @param sDate - месяц
     * @param flag - паказывать/не показывать нулевые остатки
     * @return
     * @throws Exception
     */
    public Vector getOst1DateStartPrice(long sDate, boolean flag) throws Exception {
        Vector data = new Vector();
        String sql = "";

        try {
            sql = "Select idTmc, "
                    + "     id_group, "
                    + "     tmc_group, "
                    + "     tmcname, "
                    + "     nartmc, "
                    + "     sartmc, "
                    + "     vid, "
                    + "     made, "
                    + "     ed_izm, "
                    + "     ost, "
                    + "     ost_part, "
                    + "     (Select price from sklad_hud_price where sklad_hud_price.id_tmc = idTmc and date_st <= ? Order by date_st desc limit 1 ) as priceTmc "
                    + " From( Select sklad_hud_tmc.id as idTmc, id_group, "
                    + "              (Select tmc_group from sklad_hud_tmc_group where sklad_hud_tmc_group.id = id_group) as tmc_group, "
                    + "               tmcname, nartmc, sartmc, vid, made, ed_izm, ost, ost_part   "
                    + "       From sklad_hud_ost, sklad_hud_tmc "
                    + "       Where sklad_hud_tmc.id = sklad_hud_ost.id_tmc and "
                    + "             sklad_hud_ost.date_f = ? and "
                    + "             sklad_hud_ost.tip_ost = 1 "
                    + (flag ? " " : " and sklad_hud_ost.ost <> 0 ") + " ) as t1"
                    + " Order by tmc_group, tmcname, nartmc, id_group ";


            ps = conn.prepareStatement(sql);
            ps.setDate(1, new java.sql.Date(sDate));
            ps.setDate(2, new java.sql.Date(sDate));
            rs = ps.executeQuery();

            while (rs.next()) {
                Vector tmp = new Vector();
                tmp.add(false);
                tmp.add(rs.getObject("tmc_group") == null ? "" : rs.getString("tmc_group").trim());
                tmp.add(rs.getInt("idTmc"));
                tmp.add(rs.getString("tmcname").trim());
                tmp.add(rs.getString("nartmc").trim());
                tmp.add(rs.getInt("sartmc"));
                tmp.add(rs.getInt("vid") == 1 ? UtilSkladHO.SKLAD_VID_1 : UtilSkladHO.SKLAD_VID_2);
                tmp.add(rs.getInt("made") == 1 ? UtilSkladHO.SKLAD_MADE_1 : UtilSkladHO.SKLAD_MADE_2);
                tmp.add(rs.getString("ed_izm").trim());
                tmp.add(new BigDecimal(rs.getDouble("ost")).setScale(3, RoundingMode.HALF_UP).doubleValue());
                tmp.add(new Double(0));
                tmp.add(new Double(0));
                tmp.add(new BigDecimal(rs.getDouble("ost")).setScale(3, RoundingMode.HALF_UP).doubleValue());
                tmp.add(UtilSkladHO.getKolvo(rs.getInt("vid"), rs.getInt("made"), rs.getString("ed_izm"), rs.getDouble("ost")));
                tmp.add(rs.getDouble("ost_part"));
                tmp.add(new BigDecimal(rs.getDouble("priceTmc")).setScale(4, RoundingMode.HALF_UP).doubleValue());
                data.add(tmp);
            }

        } catch (Exception e) {
            data = new Vector();
            System.err.println("Ошибка getOst1DateStart() " + e);
            log.error("Ошибка getOst1DateStart()", e);
            throw new Exception("Ошибка getOst1DateStart() " + e.getMessage(), e);
        }
        return data;
    }

    public Vector getOst1Date(long stDate, long sDate, long eDate, boolean flag) throws Exception {
        Vector data = new Vector();
        String sql = "";

        try {

            sql = "Select id_group, "
                    + "     (Select tmc_group from sklad_hud_tmc_group where sklad_hud_tmc_group.id = id_group) as tmc_group, "
                    + "     idTmc, tmcname, nartmc, sartmc, vid, made, ed_izm, ostN, ostpN, ostP, ostpP, ostR, ostpR, ostSum, ostpSum   "
                    + " From(Select id_group, idTmc, tmcname, nartmc, sartmc, vid, made, ed_izm,"
                    + "              ostN, ostpN, ostP, ostpP, ostR, ostpR, (ostN + ostP - ostR) as ostSum, (ostpN + ostpP - ostpR) as ostpSum "
                    + "     From(Select idTmc, ostN, ostpN, ostP, ostpP, COALESCE(sum(kolvo),0) as ostR, COALESCE(sum(kol_part),0) as ostpR "
                    + "             From( Select idTmc, ostN, ostpN, COALESCE(sum(kolvo),0) as ostP, COALESCE(sum(kol_part),0) as ostpP "
                    + "                   	From( Select idTmc, COALESCE(ost,0) as ostN, COALESCE(ost_part,0) as ostpN  "
                    + "                         	From (Select id as idTmc from sklad_hud_tmc) as t1 "
                    + "                         	Left join sklad_hud_ost "
                    + "                                 On sklad_hud_ost.id_tmc = idTmc and "
                    + "                                    sklad_hud_ost.tip_ost = 1 and "
                    + "                                    sklad_hud_ost.date_f = ?) as t2 "
                    + "                         Left join sklad_hud_move "
                    + "                         On sklad_hud_move.id_tmc = idTmc and "
                    + "                             sklad_hud_move.tip_move <> 2 and "
                    + "                             sklad_hud_move.status = 1 and "
                    + "                             sklad_hud_move.date_move BETWEEN ? and ? "
                    + "                         Group by idTmc, ostN, ostpN) as t3 "
                    + "                 Left join sklad_hud_move "
                    + "                 On sklad_hud_move.id_tmc = idTmc and "
                    + "                     sklad_hud_move.tip_move = 2 and "
                    + "                     sklad_hud_move.status = 1 and "
                    + "                     sklad_hud_move.date_move BETWEEN ? and ? "
                    + "                 Group by idTmc, ostN, ostpN, ostP, ostpP) as t4	"
                    + "	Join sklad_hud_tmc "
                    + "	On sklad_hud_tmc.id = idTmc) as t5 "
                    + (flag ? " " : " Where ostSum <> 0 ")
                    + " Order by tmc_group, id_group, tmcname, nartmc, sartmc, idtmc ";

            ps = conn.prepareStatement(sql);
            ps.setDate(1, new java.sql.Date(stDate));
            ps.setDate(2, new java.sql.Date(sDate));
            ps.setDate(3, new java.sql.Date(eDate));
            ps.setDate(4, new java.sql.Date(sDate));
            ps.setDate(5, new java.sql.Date(eDate));
            rs = ps.executeQuery();

            while (rs.next()) {
                Vector tmp = new Vector();
                tmp.add(false);
                tmp.add(rs.getObject("tmc_group") == null ? "" : rs.getString("tmc_group").trim());
                tmp.add(rs.getInt("idTmc"));
                tmp.add(rs.getString("tmcname").trim());
                tmp.add(rs.getString("nartmc").trim());
                tmp.add(rs.getInt("sartmc"));
                tmp.add(rs.getInt("vid") == 1 ? UtilSkladHO.SKLAD_VID_1 : UtilSkladHO.SKLAD_VID_2);
                tmp.add(rs.getInt("made") == 1 ? UtilSkladHO.SKLAD_MADE_1 : UtilSkladHO.SKLAD_MADE_2);
                tmp.add(rs.getString("ed_izm").trim());
                tmp.add(new BigDecimal(rs.getDouble("ostN")).setScale(3, RoundingMode.HALF_UP).doubleValue());
                tmp.add(new BigDecimal(rs.getDouble("ostP")).setScale(3, RoundingMode.HALF_UP).doubleValue());
                tmp.add(new BigDecimal(rs.getDouble("ostR")).setScale(3, RoundingMode.HALF_UP).doubleValue());
                tmp.add(new BigDecimal(rs.getDouble("ostSum")).setScale(3, RoundingMode.HALF_UP).doubleValue());
                tmp.add(UtilSkladHO.getKolvo(rs.getInt("vid"), rs.getInt("made"), rs.getString("ed_izm").trim(), rs.getDouble("ostSum")));
                tmp.add(rs.getInt("ostpSum"));
                data.add(tmp);
            }

        } catch (Exception e) {
            data = new Vector();
            System.err.println("Ошибка getOst1Date() " + e);
            log.error("Ошибка getOst1Date()", e);
            throw new Exception("Ошибка getOst1Date() " + e.getMessage(), e);
        }

        return data;
    }

    public Vector getOst1Date(long stDate, long sDate, long eDate, boolean flag, Vector vecTmc) throws Exception {
        Vector data = new Vector();
        String sql = "";

        try {

            sql = "Select id_group, "
                    + "     (Select tmc_group from sklad_hud_tmc_group where sklad_hud_tmc_group.id = id_group) as tmc_group, "
                    + "     idTmc, tmcname, nartmc, sartmc, vid, made, ed_izm, ostN, ostpN, ostP, ostpP, ostR, ostpR, ostSum, ostpSum   "
                    + " From(Select id_group, idTmc, tmcname, nartmc, sartmc, vid, made, ed_izm,"
                    + "              ostN, ostpN, ostP, ostpP, ostR, ostpR, (ostN + ostP - ostR) as ostSum, (ostpN + ostpP - ostpR) as ostpSum "
                    + "     From(Select idTmc, ostN, ostpN, ostP, ostpP, COALESCE(sum(kolvo),0) as ostR, COALESCE(sum(kol_part),0) as ostpR "
                    + "             From( Select idTmc, ostN, ostpN, COALESCE(sum(kolvo),0) as ostP, COALESCE(sum(kol_part),0) as ostpP "
                    + "                   	From( Select idTmc, COALESCE(ost,0) as ostN, COALESCE(ost_part,0) as ostpN  "
                    + "                         	From (Select id as idTmc from sklad_hud_tmc) as t1 "
                    + "                         	Left join sklad_hud_ost "
                    + "                                 On sklad_hud_ost.id_tmc = idTmc and "
                    + "                                    sklad_hud_ost.tip_ost = 1 and "
                    + "                                    sklad_hud_ost.date_f = ?) as t2 "
                    + "                         Left join sklad_hud_move "
                    + "                         On sklad_hud_move.id_tmc = idTmc and "
                    + "                             sklad_hud_move.tip_move <> 2 and "
                    + "                             sklad_hud_move.status = 1 and "
                    + "                             sklad_hud_move.date_move BETWEEN ? and ? "
                    + "                         Group by idTmc, ostN, ostpN) as t3 "
                    + "                 Left join sklad_hud_move "
                    + "                 On sklad_hud_move.id_tmc = idTmc and "
                    + "                     sklad_hud_move.tip_move = 2 and "
                    + "                     sklad_hud_move.status = 1 and "
                    + "                     sklad_hud_move.date_move BETWEEN ? and ? "
                    + "                 Group by idTmc, ostN, ostpN, ostP, ostpP) as t4	"
                    + "	Join sklad_hud_tmc "
                    + "	On sklad_hud_tmc.id = idTmc) as t5 "
                    + (flag ? " " : " Where ostSum <> 0 ")
                    + " Order by tmc_group, id_group, tmcname, nartmc, sartmc, idtmc ";

            ps = conn.prepareStatement(sql);
            ps.setDate(1, new java.sql.Date(stDate));
            ps.setDate(2, new java.sql.Date(sDate));
            ps.setDate(3, new java.sql.Date(eDate));
            ps.setDate(4, new java.sql.Date(sDate));
            ps.setDate(5, new java.sql.Date(eDate));
            rs = ps.executeQuery();

            while (rs.next()) {
                if (vecTmc.contains(rs.getInt("idTmc"))) {
                    Vector tmp = new Vector();
                    tmp.add(false);
                    tmp.add(rs.getObject("tmc_group") == null ? "" : rs.getString("tmc_group").trim());
                    tmp.add(rs.getInt("idTmc"));
                    tmp.add(rs.getString("tmcname").trim());
                    tmp.add(rs.getString("nartmc").trim());
                    tmp.add(rs.getInt("sartmc"));
                    tmp.add(rs.getInt("vid") == 1 ? UtilSkladHO.SKLAD_VID_1 : UtilSkladHO.SKLAD_VID_2);
                    tmp.add(rs.getInt("made") == 1 ? UtilSkladHO.SKLAD_MADE_1 : UtilSkladHO.SKLAD_MADE_2);
                    tmp.add(rs.getString("ed_izm").trim());
                    tmp.add(new BigDecimal(rs.getDouble("ostN")).setScale(3, RoundingMode.HALF_UP).doubleValue());
                    tmp.add(new BigDecimal(rs.getDouble("ostP")).setScale(3, RoundingMode.HALF_UP).doubleValue());
                    tmp.add(new BigDecimal(rs.getDouble("ostR")).setScale(3, RoundingMode.HALF_UP).doubleValue());
                    tmp.add(new BigDecimal(rs.getDouble("ostSum")).setScale(3, RoundingMode.HALF_UP).doubleValue());
                    tmp.add(UtilSkladHO.getKolvo(rs.getInt("vid"), rs.getInt("made"), rs.getString("ed_izm").trim(), rs.getDouble("ostSum")));
                    tmp.add(rs.getInt("ostpSum"));
                    data.add(tmp);
                }
            }

        } catch (Exception e) {
            data = new Vector();
            System.err.println("Ошибка getOst1Date() " + e);
            log.error("Ошибка getOst1Date()", e);
            throw new Exception("Ошибка getOst1Date() " + e.getMessage(), e);
        }

        return data;
    }

    public Vector getOst1DateCurrent(long sDate, boolean flag) throws Exception {
        Vector data = new Vector();
        String sql = "";

        try {
            sql = "Select id_group, "
                    + "     (Select tmc_group from sklad_hud_tmc_group where sklad_hud_tmc_group.id = id_group) as tmc_group, "
                    + "     idTmc, tmcname, nartmc, sartmc, vid, made, ed_izm, ostN, ostpN, ostP, ostpP, ostR, ostpR, ostSum, ostpSum   "
                    + " From(Select id_group, idTmc, tmcname, nartmc, sartmc, vid, made, ed_izm,"
                    + "              ostN, ostpN, ostP, ostpP, ostR, ostpR, (ostN + ostP - ostR) as ostSum, (ostpN + ostpP - ostpR) as ostpSum "
                    + "     From(Select idTmc, ostN, ostpN, ostP, ostpP, COALESCE(sum(kolvo),0) as ostR, COALESCE(sum(kol_part),0) as ostpR "
                    + "             From( Select idTmc, ostN, ostpN, COALESCE(sum(kolvo),0) as ostP, COALESCE(sum(kol_part),0) as ostpP "
                    + "                   	From( Select idTmc, COALESCE(ost,0) as ostN, COALESCE(ost_part,0) as ostpN  "
                    + "                         	From (Select id as idTmc from sklad_hud_tmc) as t1 "
                    + "                         	Left join sklad_hud_ost "
                    + "                                 On sklad_hud_ost.id_tmc = idTmc and "
                    + "                                    sklad_hud_ost.tip_ost = 1 and "
                    + "                                    sklad_hud_ost.date_f = ?) as t2 "
                    + "                         Left join sklad_hud_move "
                    + "                         On sklad_hud_move.id_tmc = idTmc and "
                    + "                             sklad_hud_move.tip_move <> 2 and "
                    + "                             sklad_hud_move.status = 1 and "
                    + "                             sklad_hud_move.date_move BETWEEN ? and now() "
                    + "                         Group by idTmc, ostN, ostpN) as t3 "
                    + "                 Left join sklad_hud_move "
                    + "                 On sklad_hud_move.id_tmc = idTmc and "
                    + "                     sklad_hud_move.tip_move = 2 and "
                    + "                     sklad_hud_move.status = 1 and "
                    + "                     sklad_hud_move.date_move BETWEEN ? and now() "
                    + "                 Group by idTmc, ostN, ostpN, ostP, ostpP) as t4	"
                    + "	Join sklad_hud_tmc "
                    + "	On sklad_hud_tmc.id = idTmc) as t5 "
                    + (flag ? " " : " Where ostSum <> 0 ")
                    + " Order by tmc_group, id_group, tmcname, nartmc, sartmc, idtmc ";

            ps = conn.prepareStatement(sql);
            ps.setDate(1, new java.sql.Date(sDate));
            ps.setDate(2, new java.sql.Date(sDate));
            ps.setDate(3, new java.sql.Date(sDate));
            rs = ps.executeQuery();

            while (rs.next()) {
                Vector tmp = new Vector();
                tmp.add(false);
                tmp.add(rs.getObject("tmc_group") == null ? "" : rs.getString("tmc_group").trim());
                tmp.add(rs.getInt("idTmc"));
                tmp.add(rs.getString("tmcname").trim());
                tmp.add(rs.getString("nartmc").trim());
                tmp.add(rs.getInt("sartmc"));
                tmp.add(rs.getInt("vid") == 1 ? UtilSkladHO.SKLAD_VID_1 : UtilSkladHO.SKLAD_VID_2);
                tmp.add(rs.getInt("made") == 1 ? UtilSkladHO.SKLAD_MADE_1 : UtilSkladHO.SKLAD_MADE_2);
                tmp.add(rs.getString("ed_izm").trim());
                tmp.add(new BigDecimal(rs.getDouble("ostN")).setScale(3, RoundingMode.HALF_UP).doubleValue());
                tmp.add(new BigDecimal(rs.getDouble("ostP")).setScale(3, RoundingMode.HALF_UP).doubleValue());
                tmp.add(new BigDecimal(rs.getDouble("ostR")).setScale(3, RoundingMode.HALF_UP).doubleValue());
                tmp.add(new BigDecimal(rs.getDouble("ostSum")).setScale(3, RoundingMode.HALF_UP).doubleValue());
                tmp.add(UtilSkladHO.getKolvo(rs.getInt("vid"), rs.getInt("made"), rs.getString("ed_izm").trim(), rs.getDouble("ostSum")));
                tmp.add(rs.getInt("ostpSum"));
                data.add(tmp);
            }

        } catch (Exception e) {
            data = new Vector();
            System.err.println("Ошибка getOst1DateCurrent() " + e);
            log.error("Ошибка getOst1DateCurrent()", e);
            throw new Exception("Ошибка getOst1DateCurrent() " + e.getMessage(), e);
        }

        return data;
    }

    public Vector getOst2DateStart(long sDate, boolean flag) throws Exception {
        Vector data = new Vector();
        String sql = "";

        try {
            sql = "Select  sklad_hud_tmc.id as idTmc, id_group, "
                    + "     (Select tmc_group from sklad_hud_tmc_group where sklad_hud_tmc_group.id = id_group) as tmc_group, "
                    + "     tmcname, nartmc, sartmc, vid, made, ed_izm, ost, ost_part, "
                    + "     id_dept, (Select department from dept where dept.id = id_dept) as dept, "
                    + "     id_empl, (Select fio from employees where employees.id = id_empl) as empl "
                    + " From sklad_hud_ost, sklad_hud_tmc "
                    + " Where sklad_hud_tmc.id = sklad_hud_ost.id_tmc and "
                    + "       sklad_hud_ost.date_f = ? and "
                    + "       sklad_hud_ost.tip_ost = 2 "
                    + (flag ? " " : " and sklad_hud_ost.ost <> 0 ")
                    + " Order by tmc_group, tmcname, nartmc, id_group  ";

            ps = conn.prepareStatement(sql);
            ps.setDate(1, new java.sql.Date(sDate));
            rs = ps.executeQuery();

            while (rs.next()) {
                Vector tmp = new Vector();
                tmp.add(false);
                tmp.add(rs.getObject("tmc_group") == null ? "" : rs.getString("tmc_group").trim());
                tmp.add(rs.getInt("idTmc"));
                tmp.add(rs.getString("tmcname").trim());
                tmp.add(rs.getString("nartmc").trim());
                tmp.add(rs.getInt("sartmc"));
                tmp.add(rs.getInt("vid") == 1 ? UtilSkladHO.SKLAD_VID_1 : UtilSkladHO.SKLAD_VID_2);
                tmp.add(rs.getInt("made") == 1 ? UtilSkladHO.SKLAD_MADE_1 : UtilSkladHO.SKLAD_MADE_2);
                tmp.add(rs.getString("ed_izm").trim());
                tmp.add(new BigDecimal(rs.getDouble("ost")).setScale(3, RoundingMode.HALF_UP).doubleValue());
                tmp.add(new Double(0));
                tmp.add(new Double(0));
                tmp.add(new Double(0));
                tmp.add(new Double(0));
                tmp.add(new BigDecimal(rs.getDouble("ost")).setScale(3, RoundingMode.HALF_UP).doubleValue());
                tmp.add(UtilSkladHO.getKolvo(rs.getInt("vid"), rs.getInt("made"), rs.getString("ed_izm").trim(), rs.getDouble("ost")));
                tmp.add(rs.getDouble("ost_part"));
                tmp.add(rs.getInt("id_dept"));
                tmp.add(rs.getString("dept"));
                tmp.add(rs.getObject("id_empl") == null ? -1 : rs.getInt("id_empl"));
                tmp.add(rs.getObject("empl") == null ? "" : rs.getString("empl").trim());
                data.add(tmp);
            }

        } catch (Exception e) {
            data = new Vector();
            System.err.println("Ошибка getOst2DateStart() " + e);
            log.error("Ошибка getOst2DateStart()", e);
            throw new Exception("Ошибка getOst2DateStart() " + e.getMessage(), e);
        }

        return data;
    }

    public Vector getOst2Date(long stDate, long sDate, long eDate, boolean flag) throws Exception {
        Vector data = new Vector();
        String sql = "";

        try {
            sql = " Select id_group, tmc_group, idTmc, tmcname, nartmc, sartmc, vid, made, ed_izm, "
                    + "        idDept, dept, idEmpl, empl, "
                    + "        ostN, ostpN, ostV, ostpV, ostVz, ostpVz, ostOt, ostpOt, ostoOt, "
                    + "        ostSum, ostpSum "
                    + " From( Select id_group, tmc_group, idTmc, tmcname, nartmc, sartmc, vid, made, ed_izm, "
                    + "            idDept, dept, idEmpl, empl, "
                    + "            ostN, ostpN, ostV, ostpV, ostVz, ostpVz, ostOt, ostpOt, ostoOt, "
                    + "           (round((ostN + ostV)::numeric, 3) - round((ostVz + ostOt + ostoOt)::numeric, 3)) as ostSum, "
                    + "           (round((ostpN + ostpV)::numeric,3) - round((ostpVz + ostpOt)::numeric,3)) as ostpSum "
                    + "     From(Select id_group, (Select tmc_group from sklad_hud_tmc_group where sklad_hud_tmc_group.id = id_group) as tmc_group, "
                    + "               idTmc, tmcname, nartmc, sartmc, vid, made, ed_izm, "
                    + "             idDept, (Select department from dept where dept.id = idDept) as dept, "
                    + "             idEmpl, (Select fio from employees where employees.id = idEmpl) as empl, "
                    + "             COALESCE(sum(ostN),0) as ostN, COALESCE(sum(ostpN),0) as ostpN, "
                    + "             COALESCE(sum(ostV),0) as ostV, COALESCE(sum(ostpV),0) as ostpV, "
                    + "             COALESCE(sum(ostVz),0) as ostVz, COALESCE(sum(ostpVz),0) as ostpVz, "
                    + "             COALESCE(sum(ostOt),0) as ostOt, COALESCE(sum(ostpOt),0) as ostpOt, COALESCE(sum(ostoOt),0) as ostoOt    "
                    + "        From (Select COALESCE(t5.idTmc, t6.idTmc) as idTmc, COALESCE(t5.idDept, t6.idDept) as idDept, COALESCE(t5.idEmpl, t6.idEmpl) as idEmpl, "
                    + "                 	ostN, ostpN, ostV, ostpV, ostVz, ostpVz, ostOt, ostpOt,	ostoOt  "
                    + "             From ( Select COALESCE(t3.idTmc, t4.idTmc) as idTmc, COALESCE(t3.idDept, t4.idDept) as idDept, COALESCE(t3.idEmpl, t4.idEmpl) as idEmpl,"
                    + "                 	ostN, ostpN, ostV, ostpV, ostVz, ostpVz "
                    + "                  From (Select COALESCE(t1.idTmc, t2.idTmc) as idTmc, COALESCE(t1.idDept, t2.idDept) as idDept, COALESCE(t1.idEmpl, t2.idEmpl) as idEmpl, "
                    + "                         	ostN, ostpN, ostV, ostpV "
                    + "                       From ( Select id_tmc as idTmc, id_dept as idDept, id_empl as idEmpl, COALESCE(ost,0) as ostN, COALESCE(ost_part,0) as ostpN  "
                    + "                         	From  sklad_hud_ost "
                    + "                         	Where sklad_hud_ost.tip_ost = 2 and "
                    + "                                 	sklad_hud_ost.date_f = ?) as t1"
                    + "                       FULL OUTER JOIN ( "
                    + "                             Select id_tmc as idTmc, id_dept as idDept, id_empl as idEmpl, COALESCE(sum(kolvo),0) as ostV, COALESCE(sum(kol_part),0) as ostpV  "
                    + "                             From sklad_hud_move "
                    + "                             Where (sklad_hud_move.id_dept = 20 or "
                    + "                                  sklad_hud_move.id_dept = 21) and "
                    + "                                  sklad_hud_move.tip_move = 2 and  "
                    + "                                  sklad_hud_move.status = 1 and "
                    + "                                  sklad_hud_move.date_move BETWEEN ? and ? "
                    + "                             Group by idTmc, idDept, idEmpl) as t2 "
                    + "                        ON t1.idTmc = t2.idTmc and "
                    + "                            t1.idDept = t2.idDept and "
                    + "                           t1.idEmpl = t2.idEmpl ) as t3 "
                    + "               FULL OUTER JOIN ( "
                    + "                     Select id_tmc as idTmc, id_dept as idDept, id_empl as idEmpl, COALESCE(sum(kolvo),0) as ostVz, COALESCE(sum(kol_part),0) as ostpVz "
                    + "                     From sklad_hud_move "
                    + "                     Where (sklad_hud_move.id_dept = 20 or "
                    + "                          sklad_hud_move.id_dept = 21) and "
                    + "                          sklad_hud_move.tip_move = 3 and "
                    + "                          sklad_hud_move.status = 1 and "
                    + "                          sklad_hud_move.date_move BETWEEN ? and ? "
                    + "                     Group by idTmc, idDept, idEmpl) as t4 "
                    + "               ON t3.idTmc = t4.idTmc and "
                    + "                 t3.idDept = t4.idDept and "
                    + "                 t3.idEmpl = t4.idEmpl) as t5 "
                    + "       FULL OUTER JOIN ("
                    + "             Select id_tmc as idTmc, id_dept as idDept, id_empl as idEmpl, COALESCE(sum(kol_vo),0) as ostOt, "
                    + "                 	COALESCE(sum(kol_part),0) as ostpOt, COALESCE(sum(kol_oth),0) as ostoOt  "
                    + "             From sklad_hud_prod, sklad_hud_prod_item "
                    + "             Where sklad_hud_prod.id = sklad_hud_prod_item.id_prod and "
                    + "                 (sklad_hud_prod.id_dept = 20 or "
                    + "                  sklad_hud_prod.id_dept = 21) and "
                    + "                  sklad_hud_prod.status = 1 and "
                    + "                  sklad_hud_prod.date_st BETWEEN ? and ? "
                    + "             Group by idTmc, idDept, idEmpl ) as t6 "
                    + "       ON t5.idTmc = t6.idTmc and  "
                    + "          t5.idDept = t6.idDept and "
                    + "          t5.idEmpl = t6.idEmpl ) "
                    + "          as t7, sklad_hud_tmc "
                    + "      Where sklad_hud_tmc.id = idTmc "
                    + "      Group by id_group, tmc_group, idTmc, tmcname, nartmc, sartmc, vid, made, ed_izm, idDept, dept, idEmpl, empl) as t7 "
                    + "    Group by id_group, tmc_group, idTmc, tmcname, nartmc, sartmc, vid, made, ed_izm, "
                    + "        idDept, dept, idEmpl, empl, "
                    + "        ostN, ostpN, ostV, ostpV, ostVz, ostpVz, ostOt, ostpOt, ostoOt ) as t8 "
                    + (flag ? " " : " Where ostSum <> 0 ")
                    + " Order by idDept, idEmpl, tmc_group, id_group, tmcname, nartmc, sartmc, idtmc ";

            ps = conn.prepareStatement(sql);
            ps.setDate(1, new java.sql.Date(stDate));
            ps.setDate(2, new java.sql.Date(sDate));
            ps.setDate(3, new java.sql.Date(eDate));
            ps.setDate(4, new java.sql.Date(sDate));
            ps.setDate(5, new java.sql.Date(eDate));
            ps.setDate(6, new java.sql.Date(sDate));
            ps.setDate(7, new java.sql.Date(eDate));
            rs = ps.executeQuery();

            while (rs.next()) {
                Vector tmp = new Vector();
                tmp.add(false);
                tmp.add(rs.getObject("tmc_group") == null ? "" : rs.getString("tmc_group").trim());
                tmp.add(rs.getInt("idTmc"));
                tmp.add(rs.getString("tmcname").trim());
                tmp.add(rs.getString("nartmc").trim());
                tmp.add(rs.getInt("sartmc"));
                tmp.add(rs.getInt("vid") == 1 ? UtilSkladHO.SKLAD_VID_1 : UtilSkladHO.SKLAD_VID_2);
                tmp.add(rs.getInt("made") == 1 ? UtilSkladHO.SKLAD_MADE_1 : UtilSkladHO.SKLAD_MADE_2);
                tmp.add(rs.getString("ed_izm").trim());
                tmp.add(new BigDecimal(rs.getDouble("ostN")).setScale(3, RoundingMode.HALF_UP).doubleValue());
                tmp.add(new BigDecimal(rs.getDouble("ostV")).setScale(3, RoundingMode.HALF_UP).doubleValue());
                tmp.add(new BigDecimal(rs.getDouble("ostOt")).setScale(3, RoundingMode.HALF_UP).doubleValue());
                tmp.add(new BigDecimal(rs.getDouble("ostoOt")).setScale(3, RoundingMode.HALF_UP).doubleValue());
                tmp.add(new BigDecimal(rs.getDouble("ostVz")).setScale(3, RoundingMode.HALF_UP).doubleValue());
                tmp.add(new BigDecimal(rs.getDouble("ostSum")).setScale(3, RoundingMode.HALF_UP).doubleValue());
                tmp.add(UtilSkladHO.getKolvo(rs.getInt("vid"), rs.getInt("made"), rs.getString("ed_izm").trim(), rs.getDouble("ostSum")));
                tmp.add(rs.getInt("ostpSum"));
                tmp.add(rs.getInt("idDept"));
                tmp.add(rs.getString("dept"));
                tmp.add(rs.getObject("idEmpl") == null ? -1 : rs.getInt("idEmpl"));
                tmp.add(rs.getObject("empl") == null ? "" : rs.getString("empl").trim());
                data.add(tmp);
            }

        } catch (Exception e) {
            data = new Vector();
            System.err.println("Ошибка getOst2Date() " + e);
            log.error("Ошибка getOst2Date()", e);
            throw new Exception("Ошибка getOst2Date() " + e.getMessage(), e);
        }

        return data;
    }

    public Vector getOst2DateCurrent(long sDate, boolean flag) throws Exception {
        Vector data = new Vector();
        String sql = "";

        try {
            sql = " Select id_group, tmc_group, idTmc, tmcname, nartmc, sartmc, vid, made, ed_izm, "
                    + "        idDept, dept, idEmpl, empl, "
                    + "        ostN, ostpN, ostV, ostpV, ostVz, ostpVz, ostOt, ostpOt, ostoOt, "
                    + "        ostSum, ostpSum "
                    + " From( Select id_group, tmc_group, idTmc, tmcname, nartmc, sartmc, vid, made, ed_izm, "
                    + "            idDept, dept, idEmpl, empl, "
                    + "            ostN, ostpN, ostV, ostpV, ostVz, ostpVz, ostOt, ostpOt, ostoOt, "
                    + "           (round((ostN + ostV)::numeric, 3) - round((ostVz + ostOt + ostoOt)::numeric, 3)) as ostSum, "
                    + "           (round((ostpN + ostpV)::numeric,3) - round((ostpVz + ostpOt)::numeric,3)) as ostpSum "
                    + "     From(Select id_group, (Select tmc_group from sklad_hud_tmc_group where sklad_hud_tmc_group.id = id_group) as tmc_group, "
                    + "               idTmc, tmcname, nartmc, sartmc, vid, made, ed_izm, "
                    + "             idDept, (Select department from dept where dept.id = idDept) as dept, "
                    + "             idEmpl, (Select fio from employees where employees.id = idEmpl) as empl, "
                    + "             COALESCE(sum(ostN),0) as ostN, COALESCE(sum(ostpN),0) as ostpN, "
                    + "             COALESCE(sum(ostV),0) as ostV, COALESCE(sum(ostpV),0) as ostpV, "
                    + "             COALESCE(sum(ostVz),0) as ostVz, COALESCE(sum(ostpVz),0) as ostpVz, "
                    + "             COALESCE(sum(ostOt),0) as ostOt, COALESCE(sum(ostpOt),0) as ostpOt, COALESCE(sum(ostoOt),0) as ostoOt    "
                    + "        From (Select COALESCE(t5.idTmc, t6.idTmc) as idTmc, COALESCE(t5.idDept, t6.idDept) as idDept, COALESCE(t5.idEmpl, t6.idEmpl) as idEmpl, "
                    + "                 	ostN, ostpN, ostV, ostpV, ostVz, ostpVz, ostOt, ostpOt,	ostoOt  "
                    + "             From ( Select COALESCE(t3.idTmc, t4.idTmc) as idTmc, COALESCE(t3.idDept, t4.idDept) as idDept, COALESCE(t3.idEmpl, t4.idEmpl) as idEmpl,"
                    + "                 	ostN, ostpN, ostV, ostpV, ostVz, ostpVz "
                    + "                  From (Select COALESCE(t1.idTmc, t2.idTmc) as idTmc, COALESCE(t1.idDept, t2.idDept) as idDept, COALESCE(t1.idEmpl, t2.idEmpl) as idEmpl, "
                    + "                         	ostN, ostpN, ostV, ostpV "
                    + "                       From ( Select id_tmc as idTmc, id_dept as idDept, id_empl as idEmpl, COALESCE(ost,0) as ostN, COALESCE(ost_part,0) as ostpN  "
                    + "                         	From  sklad_hud_ost "
                    + "                         	Where sklad_hud_ost.tip_ost = 2 and "
                    + "                                 	sklad_hud_ost.date_f = ?) as t1"
                    + "                       FULL OUTER JOIN ( "
                    + "                             Select id_tmc as idTmc, id_dept as idDept, id_empl as idEmpl, COALESCE(sum(kolvo),0) as ostV, COALESCE(sum(kol_part),0) as ostpV  "
                    + "                             From sklad_hud_move "
                    + "                             Where (sklad_hud_move.id_dept = 20 or "
                    + "                                  sklad_hud_move.id_dept = 21) and "
                    + "                                  sklad_hud_move.tip_move = 2 and  "
                    + "                                  sklad_hud_move.status = 1 and "
                    + "                                  sklad_hud_move.date_move BETWEEN ? and now() "
                    + "                             Group by idTmc, idDept, idEmpl) as t2 "
                    + "                        ON t1.idTmc = t2.idTmc and "
                    + "                            t1.idDept = t2.idDept and "
                    + "                           t1.idEmpl = t2.idEmpl ) as t3 "
                    + "               FULL OUTER JOIN ( "
                    + "                     Select id_tmc as idTmc, id_dept as idDept, id_empl as idEmpl, COALESCE(sum(kolvo),0) as ostVz, COALESCE(sum(kol_part),0) as ostpVz "
                    + "                     From sklad_hud_move "
                    + "                     Where (sklad_hud_move.id_dept = 20 or "
                    + "                          sklad_hud_move.id_dept = 21) and "
                    + "                          sklad_hud_move.tip_move = 3 and "
                    + "                          sklad_hud_move.status = 1 and "
                    + "                          sklad_hud_move.date_move BETWEEN ? and now() "
                    + "                     Group by idTmc, idDept, idEmpl) as t4 "
                    + "               ON t3.idTmc = t4.idTmc and "
                    + "                 t3.idDept = t4.idDept and "
                    + "                 t3.idEmpl = t4.idEmpl) as t5 "
                    + "       FULL OUTER JOIN ("
                    + "             Select id_tmc as idTmc, id_dept as idDept, id_empl as idEmpl, COALESCE(sum(kol_vo),0) as ostOt, "
                    + "                 	COALESCE(sum(kol_part),0) as ostpOt, COALESCE(sum(kol_oth),0) as ostoOt  "
                    + "             From sklad_hud_prod, sklad_hud_prod_item "
                    + "             Where sklad_hud_prod.id = sklad_hud_prod_item.id_prod and "
                    + "                 (sklad_hud_prod.id_dept = 20 or "
                    + "                  sklad_hud_prod.id_dept = 21) and "
                    + "                  sklad_hud_prod.status = 1 and "
                    + "                  sklad_hud_prod.date_st BETWEEN ? and now() "
                    + "             Group by idTmc, idDept, idEmpl ) as t6 "
                    + "       ON t5.idTmc = t6.idTmc and  "
                    + "          t5.idDept = t6.idDept and "
                    + "          t5.idEmpl = t6.idEmpl ) "
                    + "          as t7, sklad_hud_tmc "
                    + "      Where sklad_hud_tmc.id = idTmc "
                    + "      Group by id_group, tmc_group, idTmc, tmcname, nartmc, sartmc, vid, made, ed_izm, idDept, dept, idEmpl, empl) as t7 "
                    + "    Group by id_group, tmc_group, idTmc, tmcname, nartmc, sartmc, vid, made, ed_izm, "
                    + "        idDept, dept, idEmpl, empl, "
                    + "        ostN, ostpN, ostV, ostpV, ostVz, ostpVz, ostOt, ostpOt, ostoOt ) as t8 "
                    + (flag ? " " : " Where ostSum <> 0 ")
                    + " Order by idDept, idEmpl, tmc_group, id_group, tmcname, nartmc, sartmc, idtmc ";

            ps = conn.prepareStatement(sql);
            ps.setDate(1, new java.sql.Date(sDate));
            ps.setDate(2, new java.sql.Date(sDate));
            ps.setDate(3, new java.sql.Date(sDate));
            ps.setDate(4, new java.sql.Date(sDate));
            rs = ps.executeQuery();

            while (rs.next()) {
                Vector tmp = new Vector();
                tmp.add(false);
                tmp.add(rs.getObject("tmc_group") == null ? "" : rs.getString("tmc_group").trim());
                tmp.add(rs.getInt("idTmc"));
                tmp.add(rs.getString("tmcname").trim());
                tmp.add(rs.getString("nartmc").trim());
                tmp.add(rs.getInt("sartmc"));
                tmp.add(rs.getInt("vid") == 1 ? UtilSkladHO.SKLAD_VID_1 : UtilSkladHO.SKLAD_VID_2);
                tmp.add(rs.getInt("made") == 1 ? UtilSkladHO.SKLAD_MADE_1 : UtilSkladHO.SKLAD_MADE_2);
                tmp.add(rs.getString("ed_izm").trim());
                tmp.add(new BigDecimal(rs.getDouble("ostN")).setScale(3, RoundingMode.HALF_UP).doubleValue());
                tmp.add(new BigDecimal(rs.getDouble("ostV")).setScale(3, RoundingMode.HALF_UP).doubleValue());
                tmp.add(new BigDecimal(rs.getDouble("ostOt")).setScale(3, RoundingMode.HALF_UP).doubleValue());
                tmp.add(new BigDecimal(rs.getDouble("ostoOt")).setScale(3, RoundingMode.HALF_UP).doubleValue());
                tmp.add(new BigDecimal(rs.getDouble("ostVz")).setScale(3, RoundingMode.HALF_UP).doubleValue());
                tmp.add(new BigDecimal(rs.getDouble("ostSum")).setScale(3, RoundingMode.HALF_UP).doubleValue());
                tmp.add(UtilSkladHO.getKolvo(rs.getInt("vid"), rs.getInt("made"), rs.getString("ed_izm").trim(), rs.getDouble("ostSum")));
                tmp.add(rs.getInt("ostpSum"));
                tmp.add(rs.getInt("idDept"));
                tmp.add(rs.getString("dept"));
                tmp.add(rs.getObject("idEmpl") == null ? -1 : rs.getInt("idEmpl"));
                tmp.add(rs.getObject("empl") == null ? "" : rs.getString("empl").trim());
                data.add(tmp);
            }

        } catch (Exception e) {
            data = new Vector();
            System.err.println("Ошибка getOst2DateCurrent() " + e);
            log.error("Ошибка getOst2DateCurrent()", e);
            throw new Exception("Ошибка getOst2DateCurrent() " + e.getMessage(), e);
        }

        return data;
    }

    public Vector getOst3DateStart(long sDate, boolean flag) throws Exception {
        Vector data = new Vector();
        String sql = "";

        try {
            sql = "Select fas, pname, vfas, COALESCE(ost,0) as ostN  "
                    + "	From sklad_hud_ost_prod "
                    + "	Where sklad_hud_ost_prod.tip_ost = 1 and "
                    + "	      sklad_hud_ost_prod.date_f = ?"
                    + (flag ? " " : " and sklad_hud_ost_prod.ost <> 0 ")
                    + " Order by fas, vfas, pname  ";

            ps = conn.prepareStatement(sql);
            ps.setDate(1, new java.sql.Date(sDate));
            rs = ps.executeQuery();

            while (rs.next()) {
                Vector tmp = new Vector();
                tmp.add(false);
                tmp.add(rs.getInt("fas"));
                tmp.add(rs.getString("pname").trim());
                tmp.add(rs.getInt("vfas"));
                switch (rs.getInt("vfas")) {
                    case 0:
                        tmp.add("нет");
                        break;
                    case 1:
                        tmp.add("муж.");
                        break;
                    case 2:
                        tmp.add("жен.");
                        break;
                    case 3:
                        tmp.add("дет.");
                        break;
                    default:
                        tmp.add("нет");
                        break;
                }
                tmp.add(new BigDecimal(rs.getInt("ostN")).intValue());
                tmp.add(0);
                tmp.add(0);
                tmp.add(new BigDecimal(rs.getInt("ostN")).intValue());
                data.add(tmp);
            }

        } catch (Exception e) {
            data = new Vector();
            System.err.println("Ошибка getOst3DateStart() " + e);
            log.error("Ошибка getOst3DateStart()", e);
            throw new Exception("Ошибка getOst3DateStart() " + e.getMessage(), e);
        }

        return data;
    }

    public Vector getOst3Date(long stDate, long sDate, long eDate, boolean flag) throws Exception {
        Vector data = new Vector();
        String sql = "";

        try {
            sql = "Select fas, pname, vfas, ostN, ostP, ostS, ostSum "
                    + " From(Select fas, pname, vfas, ostN, ostP, ostS, (ostN + ostP - ostS) as ostSum "
                    + "     From (Select fas, pname, vfas, "
                    + "		 COALESCE(sum(ostN),0) as ostN, "
                    + "		 COALESCE(sum(ostP),0) as ostP, "
                    + "		 COALESCE(sum(ostS),0) as ostS "
                    + "		From (Select COALESCE(t3.fas, t4.fas) as fas, COALESCE(t3.pname, t4.pname) as pname, "
                    + "					COALESCE(t3.vfas, t4.vfas) as vfas, ostN,  ostP, ostS "
                    + "			From(Select COALESCE(t1.fas, t2.fas) as fas, COALESCE(t1.pname, t2.pname) as pname, "
                    + "					COALESCE(t1.vfas, t2.vfas) as vfas, ostN, ostP "
                    + "				From (Select fas, pname, vfas, COALESCE(ost,0) as ostN  "
                    + "					From sklad_hud_ost_prod "
                    + "					Where sklad_hud_ost_prod.tip_ost = 1 and "
                    + "					      sklad_hud_ost_prod.date_f = ? ) as t1 "
                    + "				FULL OUTER JOIN ( "
                    + "					Select fas, pname, vfas, COALESCE(sum(kolvo),0) as ostP "
                    + "					  From sklad_hud_prod "
                    + "					  Where sklad_hud_prod.status = 1 and "
                    + "						sklad_hud_prod.date_st BETWEEN ? and ? "
                    + "					Group by fas, pname, vfas) as t2 	      "
                    + "				ON t1.fas = t2.fas and "
                    + "				   t1.pname = t2.pname and "
                    + "				   t1.vfas = t2.vfas ) as t3 "
                    + "			FULL OUTER JOIN ( "
                    + "				Select fas, pname, vfas, COALESCE(sum(kolvo),0) as ostS "
                    + "				  From sklad_hud_storage, sklad_hud_storage_item, sklad_hud_prod "
                    + "				  Where sklad_hud_storage.id = sklad_hud_storage_item.id_storage and "
                    + "					sklad_hud_storage_item.id_prod = sklad_hud_prod.id and "
                    + "					sklad_hud_storage.status = 1 and "
                    + "					sklad_hud_storage.date_st BETWEEN ? and ? "
                    + "				Group by fas, pname, vfas) as t4 "
                    + "			ON t3.fas = t4.fas and "
                    + "			   t3.pname = t4.pname and "
                    + "			   t3.vfas = t4.vfas) as t5 "
                    + "		Group by fas, pname, vfas) as t6 "
                    + "     Group by fas, pname, vfas, ostN, ostP, ostS ) as t7 "
                    //   +  ( flag?" ":" Where ostSum <> 0 ")
                    + " Order by fas, vfas, pname	   ";

            ps = conn.prepareStatement(sql);
            ps.setDate(1, new java.sql.Date(stDate));
            ps.setDate(2, new java.sql.Date(sDate));
            ps.setDate(3, new java.sql.Date(eDate));
            ps.setDate(4, new java.sql.Date(sDate));
            ps.setDate(5, new java.sql.Date(eDate));
            rs = ps.executeQuery();

            while (rs.next()) {
                Vector tmp = new Vector();
                tmp.add(false);
                tmp.add(rs.getInt("fas"));
                tmp.add(rs.getString("pname").trim());
                tmp.add(rs.getInt("vfas"));
                switch (rs.getInt("vfas")) {
                    case 0:
                        tmp.add("нет");
                        break;
                    case 1:
                        tmp.add("муж.");
                        break;
                    case 2:
                        tmp.add("жен.");
                        break;
                    case 3:
                        tmp.add("дет.");
                        break;
                    default:
                        tmp.add("нет");
                        break;
                }
                tmp.add(new BigDecimal(rs.getInt("ostN")).intValue());
                tmp.add(new BigDecimal(rs.getInt("ostP")).intValue());
                tmp.add(new BigDecimal(rs.getInt("ostS")).intValue());
                tmp.add(new BigDecimal(rs.getInt("ostSum")).intValue());
                data.add(tmp);
            }

        } catch (Exception e) {
            data = new Vector();
            System.err.println("Ошибка getOst3Date() " + e);
            log.error("Ошибка getOst3Date()", e);
            throw new Exception("Ошибка getOst3Date() " + e.getMessage(), e);
        }

        return data;
    }

    public Vector getOst3DateCurrent(long sDate, boolean flag) throws Exception {
        Vector data = new Vector();
        String sql = "";

        try {
            sql = "Select fas, pname, vfas, ostN, ostP, ostS, ostSum "
                    + " From(Select fas, pname, vfas, ostN, ostP, ostS, (ostN + ostP - ostS) as ostSum "
                    + "     From (Select fas, pname, vfas, "
                    + "		 COALESCE(sum(ostN),0) as ostN, "
                    + "		 COALESCE(sum(ostP),0) as ostP, "
                    + "		 COALESCE(sum(ostS),0) as ostS "
                    + "		From (Select COALESCE(t3.fas, t4.fas) as fas, COALESCE(t3.pname, t4.pname) as pname, "
                    + "					COALESCE(t3.vfas, t4.vfas) as vfas, ostN,  ostP, ostS "
                    + "			From(Select COALESCE(t1.fas, t2.fas) as fas, COALESCE(t1.pname, t2.pname) as pname, "
                    + "					COALESCE(t1.vfas, t2.vfas) as vfas, ostN, ostP "
                    + "				From (Select fas, pname, vfas, COALESCE(ost,0) as ostN  "
                    + "					From sklad_hud_ost_prod "
                    + "					Where sklad_hud_ost_prod.tip_ost = 1 and "
                    + "					      sklad_hud_ost_prod.date_f = ? ) as t1 "
                    + "				FULL OUTER JOIN ( "
                    + "					Select fas, pname, vfas, COALESCE(sum(kolvo),0) as ostP "
                    + "					  From sklad_hud_prod "
                    + "					  Where sklad_hud_prod.status = 1 and "
                    + "						sklad_hud_prod.date_st BETWEEN ? and now() "
                    + "					Group by fas, pname, vfas) as t2 	      "
                    + "				ON t1.fas = t2.fas and "
                    + "				   t1.pname = t2.pname and "
                    + "				   t1.vfas = t2.vfas ) as t3 "
                    + "			FULL OUTER JOIN ( "
                    + "				Select fas, pname, vfas, COALESCE(sum(kolvo),0) as ostS "
                    + "				  From sklad_hud_storage, sklad_hud_storage_item, sklad_hud_prod "
                    + "				  Where sklad_hud_storage.id = sklad_hud_storage_item.id_storage and "
                    + "					sklad_hud_storage_item.id_prod = sklad_hud_prod.id and "
                    + "					sklad_hud_storage.status = 1 and "
                    + "					sklad_hud_storage.date_st BETWEEN ? and now() "
                    + "				Group by fas, pname, vfas) as t4 "
                    + "			ON t3.fas = t4.fas and "
                    + "			   t3.pname = t4.pname and "
                    + "			   t3.vfas = t4.vfas) as t5 "
                    + "		Group by fas, pname, vfas) as t6 "
                    + "     Group by fas, pname, vfas, ostN, ostP, ostS ) as t7 "
                    //  +  ( flag?" ":" Where ostSum <> 0 ")
                    + " Order by fas, vfas, pname	   ";

            ps = conn.prepareStatement(sql);
            ps.setDate(1, new java.sql.Date(sDate));
            ps.setDate(2, new java.sql.Date(sDate));
            ps.setDate(3, new java.sql.Date(sDate));
            rs = ps.executeQuery();

            while (rs.next()) {
                Vector tmp = new Vector();
                tmp.add(false);
                tmp.add(rs.getInt("fas"));
                tmp.add(rs.getString("pname").trim());
                tmp.add(rs.getInt("vfas"));
                switch (rs.getInt("vfas")) {
                    case 0:
                        tmp.add("нет");
                        break;
                    case 1:
                        tmp.add("муж.");
                        break;
                    case 2:
                        tmp.add("жен.");
                        break;
                    case 3:
                        tmp.add("дет.");
                        break;
                    default:
                        tmp.add("нет");
                        break;
                }
                tmp.add(new BigDecimal(rs.getInt("ostN")).intValue());
                tmp.add(new BigDecimal(rs.getInt("ostP")).intValue());
                tmp.add(new BigDecimal(rs.getInt("ostS")).intValue());
                tmp.add(new BigDecimal(rs.getInt("ostSum")).intValue());
                data.add(tmp);
            }

        } catch (Exception e) {
            data = new Vector();
            System.err.println("Ошибка getOst3DateCurrent() " + e);
            log.error("Ошибка getOst3DateCurrent()", e);
            throw new Exception("Ошибка getOst3DateCurrent() " + e.getMessage(), e);
        }

        return data;
    }

    public Vector getOstTMCDetalStart(int id, int tip, long sDate) throws Exception {
        Vector data = new Vector();
        String sql = "";

        try {
            sql = "Select  sklad_hud_tmc.id as idTmc, id_group, "
                    + "     (Select tmc_group from sklad_hud_tmc_group where sklad_hud_tmc_group.id = id_group) as tmc_group, "
                    + "     tmcname, nartmc, sartmc, vid, made, ed_izm, ost, ost_part "
                    + " From sklad_hud_ost, sklad_hud_tmc "
                    + " Where sklad_hud_tmc.id = sklad_hud_ost.id_tmc and "
                    + "       sklad_hud_ost.date_f = ? and "
                    + "       sklad_hud_ost.tip_ost = ? and "
                    + "       sklad_hud_tmc.id = ? "
                    + " Order by tmc_group, tmcname, nartmc, id_group  ";

            ps = conn.prepareStatement(sql);
            ps.setDate(1, new java.sql.Date(sDate));
            ps.setInt(2, tip);
            ps.setInt(3, id);
            rs = ps.executeQuery();

            while (rs.next()) {
                Vector tmp = new Vector();
                tmp.add(false);
                tmp.add("н/п");
                tmp.add("");
                tmp.add(new SimpleDateFormat("dd-MM-yyyy").format(sDate));
                tmp.add(rs.getObject("tmc_group") == null ? "" : rs.getString("tmc_group").trim());
                tmp.add(rs.getInt("idTmc"));
                tmp.add(rs.getString("tmcname").trim());
                tmp.add(rs.getString("nartmc").trim());
                tmp.add(rs.getInt("sartmc"));
                tmp.add(rs.getInt("vid") == 1 ? UtilSkladHO.SKLAD_VID_1 : UtilSkladHO.SKLAD_VID_2);
                tmp.add(rs.getInt("made") == 1 ? UtilSkladHO.SKLAD_MADE_1 : UtilSkladHO.SKLAD_MADE_2);
                tmp.add(rs.getString("ed_izm").trim());
                tmp.add(rs.getDouble("ost"));
                tmp.add(UtilSkladHO.getKolvo(rs.getInt("vid"), rs.getInt("made"), rs.getString("ed_izm").trim(), rs.getDouble("ost")));
                tmp.add(rs.getDouble("ost_part"));
                data.add(tmp);
            }

        } catch (Exception e) {
            data = new Vector();
            System.err.println("Ошибка getOstTMCDetalStart() " + e);
            log.error("Ошибка getOstTMCDetalStart()", e);
            throw new Exception("Ошибка getOstTMCDetalStart() " + e.getMessage(), e);
        }

        return data;
    }

    public Vector getOstTMCDetalEmplStart(int id, int tip, int idDept, int idEmpl, long sDate) throws Exception {
        Vector data = new Vector();
        String sql = "";

        try {
            sql = "Select  sklad_hud_tmc.id as idTmc, id_group, "
                    + "     (Select tmc_group from sklad_hud_tmc_group where sklad_hud_tmc_group.id = id_group) as tmc_group, "
                    + "     tmcname, nartmc, sartmc, vid, made, ed_izm, ost, ost_part "
                    + " From sklad_hud_ost, sklad_hud_tmc "
                    + " Where sklad_hud_tmc.id = sklad_hud_ost.id_tmc and "
                    + "       sklad_hud_ost.date_f = ? and "
                    + "       sklad_hud_ost.tip_ost = ? and "
                    + "       sklad_hud_tmc.id = ? and "
                    + "       sklad_hud_ost.id_dept = ? and "
                    + "       sklad_hud_ost.id_empl = ? "
                    + " Order by tmc_group, tmcname, nartmc, id_group  ";

            ps = conn.prepareStatement(sql);
            ps.setDate(1, new java.sql.Date(sDate));
            ps.setInt(2, tip);
            ps.setInt(3, id);
            ps.setInt(4, idDept);
            ps.setInt(5, idEmpl);
            rs = ps.executeQuery();

            while (rs.next()) {
                Vector tmp = new Vector();
                tmp.add(false);
                tmp.add("н/п");
                tmp.add("");
                tmp.add(new SimpleDateFormat("dd-MM-yyyy").format(sDate));
                tmp.add(rs.getObject("tmc_group") == null ? "" : rs.getString("tmc_group").trim());
                tmp.add(rs.getInt("idTmc"));
                tmp.add(rs.getString("tmcname").trim());
                tmp.add(rs.getString("nartmc").trim());
                tmp.add(rs.getInt("sartmc"));
                tmp.add(rs.getInt("vid") == 1 ? UtilSkladHO.SKLAD_VID_1 : UtilSkladHO.SKLAD_VID_2);
                tmp.add(rs.getInt("made") == 1 ? UtilSkladHO.SKLAD_MADE_1 : UtilSkladHO.SKLAD_MADE_2);
                tmp.add(rs.getString("ed_izm").trim());
                tmp.add(rs.getDouble("ost"));
                tmp.add(UtilSkladHO.getKolvo(rs.getInt("vid"), rs.getInt("made"), rs.getString("ed_izm").trim(), rs.getDouble("ost")));
                tmp.add(rs.getDouble("ost_part"));
                data.add(tmp);
            }

        } catch (Exception e) {
            data = new Vector();
            System.err.println("Ошибка getOstTMCDetalEmplStart() " + e);
            log.error("Ошибка getOstTMCDetalEmplStart()", e);
            throw new Exception("Ошибка getOstTMCDetalEmplStart() " + e.getMessage(), e);
        }

        return data;
    }

    public Vector getOstTMCDetalMove(int id, long stDate, long eDate) throws Exception {
        Vector data = new Vector();
        String sql = "";

        try {
            sql = "Select distinct sklad_hud_move.id as idMove, tip_move, sklad_hud_tmc.id as idTmc, "
                    + "     id_group, vid, made, nartmc, sartmc, tmcname, "
                    + "     (Select tmc_group from sklad_hud_tmc_group where sklad_hud_tmc_group.id = id_group) as tmc_group, "
                    + "     date_move, kolvo, kol_part, ed_izm "
                    + "	From  sklad_hud_move, sklad_hud_tmc "
                    + "	Where sklad_hud_move.id_tmc = sklad_hud_tmc.id and "
                    + "		sklad_hud_move.status = 1 and "
                    + "         sklad_hud_move.date_move between ? and ? and "
                    + "         sklad_hud_tmc.id = ? "
                    + " Order by date_move, idMove ";

            ps = conn.prepareStatement(sql);
            ps.setDate(1, new java.sql.Date(stDate));
            ps.setDate(2, new java.sql.Date(eDate));
            ps.setInt(3, id);
            rs = ps.executeQuery();

            while (rs.next()) {
                Vector tmp = new Vector();
                tmp.add(false);
                switch (rs.getInt("tip_move")) {
                    case 1:
                        tmp.add(UtilSkladHO.TYPE_MOVE_PR);
                        break;
                    case 2:
                        tmp.add(UtilSkladHO.TYPE_MOVE_RS);
                        break;
                    case 3:
                        tmp.add(UtilSkladHO.TYPE_MOVE_VZ);
                        break;
                    case 4:
                        tmp.add(UtilSkladHO.TYPE_MOVE_AK);
                        break;
                    case 5:
                        tmp.add(UtilSkladHO.TYPE_MOVE_BR);
                        break;
                    default:
                        tmp.add("ошибка");
                        break;
                }
                tmp.add(rs.getInt("idMove"));
                tmp.add(new SimpleDateFormat("dd-MM-yyyy").format(rs.getDate("date_move")));
                tmp.add(rs.getObject("tmc_group") == null ? "" : rs.getString("tmc_group").trim());
                tmp.add(rs.getInt("idTmc"));
                tmp.add(rs.getString("tmcname").trim());
                tmp.add(rs.getString("nartmc").trim());
                tmp.add(rs.getInt("sartmc"));
                tmp.add(rs.getInt("vid") == 1 ? UtilSkladHO.SKLAD_VID_1 : UtilSkladHO.SKLAD_VID_2);
                tmp.add(rs.getInt("made") == 1 ? UtilSkladHO.SKLAD_MADE_1 : UtilSkladHO.SKLAD_MADE_2);
                tmp.add(rs.getString("ed_izm").trim());
                tmp.add(rs.getDouble("kolvo"));
                tmp.add(UtilSkladHO.getKolvo(rs.getInt("vid"), rs.getInt("made"), rs.getString("ed_izm").trim(), rs.getDouble("kolvo")));
                tmp.add(rs.getInt("kol_part"));
                data.add(tmp);
            }

        } catch (Exception e) {
            data = new Vector();
            System.err.println("Ошибка getOst1DetalMove() " + e);
            log.error("Ошибка getOst1DetalMove()", e);
            throw new Exception("Ошибка getOst1DetalMove() " + e.getMessage(), e);
        }

        return data;
    }

    public Vector getOstTMCDetalDeptMove(int idTmc, int idDept, int idEmpl, long stDate, long eDate) throws Exception {
        Vector data = new Vector();
        String sql = "";

        try {
            sql = "Select distinct sklad_hud_move.id as idMove, tip_move, sklad_hud_tmc.id as idTmc, "
                    + "     id_group, vid, made, nartmc, sartmc, tmcname, "
                    + "     (Select tmc_group from sklad_hud_tmc_group where sklad_hud_tmc_group.id = id_group) as tmc_group, "
                    + "     date_move, kolvo, kol_part, ed_izm "
                    + "	From  sklad_hud_move, sklad_hud_tmc "
                    + "	Where sklad_hud_move.id_tmc = sklad_hud_tmc.id and "
                    + "		sklad_hud_move.status = 1 and "
                    + "         sklad_hud_move.date_move between ? and ? and "
                    + "         sklad_hud_tmc.id = ? "
                    + (idDept > 0 ? " and  sklad_hud_move.id_dept = " + idDept + " " : " ")
                    + (idEmpl > 0 ? " and sklad_hud_move.id_empl = " + idEmpl + " " : " ")
                    + " Order by date_move, idMove ";

            ps = conn.prepareStatement(sql);
            ps.setDate(1, new java.sql.Date(stDate));
            ps.setDate(2, new java.sql.Date(eDate));
            ps.setInt(3, idTmc);
            rs = ps.executeQuery();

            while (rs.next()) {
                Vector tmp = new Vector();
                tmp.add(false);
                switch (rs.getInt("tip_move")) {
                    case 1:
                        tmp.add(UtilSkladHO.TYPE_MOVE_PR);
                        break;
                    case 2:
                        tmp.add(UtilSkladHO.TYPE_MOVE_RS);
                        break;
                    case 3:
                        tmp.add(UtilSkladHO.TYPE_MOVE_VZ);
                        break;
                    case 4:
                        tmp.add(UtilSkladHO.TYPE_MOVE_AK);
                        break;
                    case 5:
                        tmp.add(UtilSkladHO.TYPE_MOVE_BR);
                        break;
                    default:
                        tmp.add("ошибка");
                        break;
                }
                tmp.add(rs.getInt("idMove"));
                tmp.add(new SimpleDateFormat("dd-MM-yyyy").format(rs.getDate("date_move")));
                tmp.add(rs.getObject("tmc_group") == null ? "" : rs.getString("tmc_group").trim());
                tmp.add(rs.getInt("idTmc"));
                tmp.add(rs.getString("tmcname").trim());
                tmp.add(rs.getString("nartmc").trim());
                tmp.add(rs.getInt("sartmc"));
                tmp.add(rs.getInt("vid") == 1 ? UtilSkladHO.SKLAD_VID_1 : UtilSkladHO.SKLAD_VID_2);
                tmp.add(rs.getInt("made") == 1 ? UtilSkladHO.SKLAD_MADE_1 : UtilSkladHO.SKLAD_MADE_2);
                tmp.add(rs.getString("ed_izm").trim());
                tmp.add(rs.getDouble("kolvo"));
                tmp.add(UtilSkladHO.getKolvo(rs.getInt("vid"), rs.getInt("made"), rs.getString("ed_izm").trim(), rs.getDouble("kolvo")));
                tmp.add(rs.getInt("kol_part"));
                data.add(tmp);
            }

        } catch (Exception e) {
            data = new Vector();
            System.err.println("Ошибка getOstTMCDetalDeptMove() " + e);
            log.error("Ошибка getOstTMCDetalDeptMove()", e);
            throw new Exception("Ошибка getOstTMCDetalDeptMove() " + e.getMessage(), e);
        }

        return data;
    }

    public Vector getOstDetalProd(int idTmc, int idDept, int idEmpl, long stDate, long eDate) throws Exception {
        Vector data = new Vector();
        String sql = "";

        try {

            sql = "Select sklad_hud_prod_item.id as idMaterial, id_prod, sklad_hud_prod.date_st as dateProd, fas, "
                    + "   sklad_hud_prod_item.id_tmc as idTmc, id_group, sklad_hud_prod.id_dept as idDept, department, "
                    + "   (Select tmc_group from sklad_hud_tmc_group where sklad_hud_tmc_group.id = id_group) as tmc_group, "
                    + "   vid, made, sartmc, nartmc, tmcname, ed_izm, sklad_hud_prod.id_empl as idEmpl, "
                    + "   empl1.fio as fioEmpl, kol_vo, kol_part, kol_oth, sklad_hud_prod_item.date_ins as dIns "
                    + " From sklad_hud_prod_item, sklad_hud_prod, sklad_hud_tmc, employees as empl1, dept "
                    + " Where sklad_hud_tmc.id = sklad_hud_prod_item.id_tmc and "
                    + "         sklad_hud_prod.id = sklad_hud_prod_item.id_prod and  "
                    + "         empl1.id = sklad_hud_prod.id_empl and "
                    + "         sklad_hud_prod.id_dept = dept.id and "
                    + "         sklad_hud_prod.status = 1 and "
                    + "         sklad_hud_prod.date_st between ? and ? and "
                    + "         sklad_hud_tmc.id = ? "
                    + (idDept > 0 ? " and  sklad_hud_prod.id_dept = " + idDept + " " : " ")
                    + (idEmpl > 0 ? " and  sklad_hud_prod.id_empl = " + idEmpl + " " : " ")
                    + " Order by dateProd, id_prod ";

            ps = conn.prepareStatement(sql);
            ps.setDate(1, new java.sql.Date(stDate));
            ps.setDate(2, new java.sql.Date(eDate));
            ps.setInt(3, idTmc);
            rs = ps.executeQuery();

            while (rs.next()) {
                Vector tmp = new Vector();
                tmp.add(false);
                tmp.add("списано");
                tmp.add(rs.getInt("id_prod"));
                tmp.add(new SimpleDateFormat("dd-MM-yyyy").format(rs.getDate("dateProd")));
                tmp.add(rs.getObject("tmc_group") == null ? "" : rs.getString("tmc_group").trim());
                tmp.add(rs.getInt("idTmc"));
                tmp.add(rs.getString("tmcname").trim());
                tmp.add(rs.getString("nartmc").trim());
                tmp.add(rs.getInt("sartmc"));
                tmp.add(rs.getInt("vid") == 1 ? UtilSkladHO.SKLAD_VID_1 : UtilSkladHO.SKLAD_VID_2);
                tmp.add(rs.getInt("made") == 1 ? UtilSkladHO.SKLAD_MADE_1 : UtilSkladHO.SKLAD_MADE_2);
                tmp.add(rs.getString("ed_izm").trim());
                tmp.add(rs.getDouble("kol_vo") + rs.getDouble("kol_oth"));
                tmp.add(rs.getInt("vid") == 2 ? rs.getDouble("kol_vo") + rs.getDouble("kol_oth") : UtilSkladHO.countKolvoK(rs.getDouble("kol_vo") + rs.getDouble("kol_oth")));
                tmp.add(rs.getInt("kol_part"));
                data.add(tmp);
            }

        } catch (Exception e) {
            data = new Vector();
            System.err.println("Ошибка getOstDetalProd() " + e);
            log.error("Ошибка getOstDetalProd()", e);
            throw new Exception("Ошибка getOstDetalProd() " + e.getMessage(), e);
        }

        return data;
    }

    public Vector getOstModelDetalStart(int model, long sDate, boolean flag) throws Exception {
        Vector data = new Vector();
        String sql = "";

        try {
            sql = "Select fas, pname, vfas, COALESCE(ost,0) as ostN  "
                    + "	From sklad_hud_ost_prod "
                    + "	Where sklad_hud_ost_prod.tip_ost = 1 and "
                    + "	      sklad_hud_ost_prod.date_f = ? and "
                    + "       sklad_hud_ost_prod.fas = ? "
                    + (flag ? " " : " and sklad_hud_ost_prod.ost <> 0 ")
                    + " Order by fas, vfas, pname  ";

            ps = conn.prepareStatement(sql);
            ps.setDate(1, new java.sql.Date(sDate));
            ps.setInt(2, model);
            rs = ps.executeQuery();

            while (rs.next()) {
                Vector tmp = new Vector();
                tmp.add(false);
                tmp.add("н/п");
                tmp.add("");
                tmp.add(new SimpleDateFormat("dd-MM-yyyy").format(sDate));
                tmp.add(rs.getInt("fas"));
                tmp.add(rs.getString("pname").trim());
                tmp.add(rs.getInt("vfas"));
                switch (rs.getInt("vfas")) {
                    case 0:
                        tmp.add("нет");
                        break;
                    case 1:
                        tmp.add("муж.");
                        break;
                    case 2:
                        tmp.add("жен.");
                        break;
                    case 3:
                        tmp.add("дет.");
                        break;
                    default:
                        tmp.add("нет");
                        break;
                }
                tmp.add(new BigDecimal(rs.getInt("ostN")).intValue());
                data.add(tmp);
            }

        } catch (Exception e) {
            data = new Vector();
            System.err.println("Ошибка getOstModelDetalStart() " + e);
            log.error("Ошибка getOstModelDetalStart()", e);
            throw new Exception("Ошибка getOstModelDetalStart() " + e.getMessage(), e);
        }

        return data;
    }

    public Vector getOstModelDetalMove(int id, long stDate, long sDate, long eDate, boolean flag) throws Exception {
        Vector data = new Vector();
        String sql = "";

        try {
            sql = "Select fas, pname, vfas, ostN, ostP, ostS, ostSum "
                    + " From(Select fas, pname, vfas, ostN, ostP, ostS, (ostN + ostP - ostS) as ostSum "
                    + "     From (Select fas, pname, vfas, "
                    + "		 COALESCE(sum(ostN),0) as ostN, "
                    + "		 COALESCE(sum(ostP),0) as ostP, "
                    + "		 COALESCE(sum(ostS),0) as ostS "
                    + "		From (Select COALESCE(t3.fas, t4.fas) as fas, COALESCE(t3.pname, t4.pname) as pname, "
                    + "					COALESCE(t3.vfas, t4.vfas) as vfas, ostN,  ostP, ostS "
                    + "			From(Select COALESCE(t1.fas, t2.fas) as fas, COALESCE(t1.pname, t2.pname) as pname, "
                    + "					COALESCE(t1.vfas, t2.vfas) as vfas, ostN, ostP "
                    + "				From (Select fas, pname, vfas, COALESCE(ost,0) as ostN  "
                    + "					From sklad_hud_ost_prod "
                    + "					Where sklad_hud_ost_prod.tip_ost = 1 and "
                    + "					      sklad_hud_ost_prod.date_f = ? ) as t1 "
                    + "				FULL OUTER JOIN ( "
                    + "					Select fas, pname, vfas, COALESCE(sum(kolvo),0) as ostP "
                    + "					  From sklad_hud_prod "
                    + "					  Where sklad_hud_prod.status = 1 and "
                    + "						sklad_hud_prod.date_st BETWEEN ? and ? "
                    + "					Group by fas, pname, vfas) as t2 	      "
                    + "				ON t1.fas = t2.fas and "
                    + "				   t1.pname = t2.pname and "
                    + "				   t1.vfas = t2.vfas ) as t3 "
                    + "			FULL OUTER JOIN ( "
                    + "				Select fas, pname, vfas, COALESCE(sum(kolvo),0) as ostS "
                    + "				  From sklad_hud_storage, sklad_hud_storage_item, sklad_hud_prod "
                    + "				  Where sklad_hud_storage.id = sklad_hud_storage_item.id_storage and "
                    + "					sklad_hud_storage_item.id_prod = sklad_hud_prod.id and "
                    + "					sklad_hud_storage.status = 1 and "
                    + "					sklad_hud_storage.date_st BETWEEN ? and ? "
                    + "				Group by fas, pname, vfas) as t4 "
                    + "			ON t3.fas = t4.fas and "
                    + "			   t3.pname = t4.pname and "
                    + "			   t3.vfas = t4.vfas) as t5 "
                    + "		Group by fas, pname, vfas) as t6 "
                    + "     Group by fas, pname, vfas, ostN, ostP, ostS ) as t7 "
                    //   +  ( flag?" ":" Where ostSum <> 0 ")
                    + " Order by fas, vfas, pname	   ";

            ps = conn.prepareStatement(sql);
            ps.setDate(1, new java.sql.Date(stDate));
            ps.setDate(2, new java.sql.Date(sDate));
            ps.setDate(3, new java.sql.Date(eDate));
            ps.setDate(4, new java.sql.Date(sDate));
            ps.setDate(5, new java.sql.Date(eDate));
            rs = ps.executeQuery();

            while (rs.next()) {
                Vector tmp = new Vector();
                tmp.add(false);
                tmp.add(rs.getInt("fas"));
                tmp.add(rs.getString("pname").trim());
                tmp.add(rs.getInt("vfas"));
                switch (rs.getInt("vfas")) {
                    case 0:
                        tmp.add("нет");
                        break;
                    case 1:
                        tmp.add("муж.");
                        break;
                    case 2:
                        tmp.add("жен.");
                        break;
                    case 3:
                        tmp.add("дет.");
                        break;
                    default:
                        tmp.add("нет");
                        break;
                }
                tmp.add(new BigDecimal(rs.getInt("ostN")).intValue());
                tmp.add(new BigDecimal(rs.getInt("ostP")).intValue());
                tmp.add(new BigDecimal(rs.getInt("ostS")).intValue());
                tmp.add(new BigDecimal(rs.getInt("ostSum")).intValue());
                data.add(tmp);
            }

        } catch (Exception e) {
            data = new Vector();
            System.err.println("Ошибка getOstModelDetalMove() " + e);
            log.error("Ошибка getOstModelDetalMove()", e);
            throw new Exception("Ошибка getOstModelDetalMove() " + e.getMessage(), e);
        }

        return data;
    }

    public Vector getEmplList(int idDept) throws Exception {
        Vector elements = new Vector();
        String sql = "";

        try {
            sql = "Select distinct sklad_hud_prod.id_empl as idEmpl, fio  "
                    + "  From sklad_hud_prod, employees "
                    + "  Where sklad_hud_prod.id_empl = employees.id "
                    + (idDept != -1 ? " and sklad_hud_prod.id_dept = ? " : " ")
                    + "  Order by fio";

            ps = conn.prepareStatement(sql);
            if (idDept != -1)
                ps.setInt(1, idDept);
            rs = ps.executeQuery();

            while (rs.next()) {
                Vector tmp = new Vector();
                tmp.add(rs.getInt("idEmpl"));
                tmp.add(rs.getString("fio"));
                elements.add(tmp);
            }

        } catch (Exception e) {
            elements = new Vector();
            System.err.println("Ошибка getEmplList() " + e);
            log.error("Ошибка getEmplList()", e);
            throw new Exception("Ошибка getEmplList() " + e.getMessage(), e);
        }
        return elements;
    }

    public Vector getDataReportAllMap(int idEmpl, int idDept, long sDate, long eDate) throws Exception {
        Vector elements = new Vector();
        String sql = "";

        try {
            sql = "";


        } catch (Exception e) {
            elements = new Vector();
            System.err.println("Ошибка getAllMap() " + e);
            log.error("Ошибка getAllMap()", e);
            throw new Exception("Ошибка getAllMap() " + e.getMessage(), e);
        }
        return elements;
    }

    public Vector getDataReportPOIP(long stDate, long sDate, long eDate, String flagIzm) throws Exception {
        Vector elements = new Vector();
        String sql = "";

        try {
            sql = " Select idTmc, tmcname, nartmc, sartmc, vid, made, ed_izm, id_group, tmc_group, "
                    + "	   ostN, ostP, ostR, dateP, "
                    + "	  COALESCE((Select price from sklad_hud_price Where sklad_hud_price.date_st = dateP and sklad_hud_price.id_tmc = idTmc limit 1),0) as priceP "
                    + " From(Select idTmc, tmcname, nartmc, sartmc, vid, made, ed_izm, id_group, "
                    + "             (Select tmc_group from sklad_hud_tmc_group where sklad_hud_tmc_group.id = id_group) as tmc_group, "
                    + "             ostN, ostP, ostR, "
                    + "		   (Select max(date_st) from sklad_hud_price Where sklad_hud_price.id_tmc = idTmc and date_st <= ?  limit 1) as dateP "
                    + "     From (Select idTmc, ostN, ostP,  COALESCE(sum(kolvo),0) as ostR "
                    + "		  From (Select idTmc, ostN, COALESCE(sum(kolvo),0) as ostP "
                    + "                  From(Select idTmc, COALESCE(ost,0) as ostN "
                    + "                         From(Select id as idTmc from sklad_hud_tmc where vid = 1 and made = 2 ) as t1 "
                    + "				Left join sklad_hud_ost "
                    + "				On sklad_hud_ost.id_tmc = idTmc and "
                    + "				   sklad_hud_ost.tip_ost = 1 and "
                    + "				   sklad_hud_ost.date_f = ? ) as t2 "
                    + "			 Left join sklad_hud_move "
                    + "			 On sklad_hud_move.id_tmc = idTmc and "
                    + "			    sklad_hud_move.tip_move <> 2 and "
                    + "			    sklad_hud_move.status = 1 and "
                    + "			    sklad_hud_move.date_move BETWEEN   ? and ? "
                    + "			Group by idTmc, ostN) as t3 "
                    + "		 Left join sklad_hud_move "
                    + "		 On sklad_hud_move.id_tmc = idTmc and  "
                    + "		    sklad_hud_move.tip_move = 2 and "
                    + "		    sklad_hud_move.status = 1 and "
                    + "		    sklad_hud_move.date_move BETWEEN ? and ? "
                    + "		Group by idTmc, ostN, ostP)  as t4 "
                    + "	 Join sklad_hud_tmc "
                    + "	 On sklad_hud_tmc.id = idTmc and "
                    + "	   (ostN <> 0 or ostP <> 0 or ostR <> 0 )) as t5 "
                    + " Where ed_izm like ? "
                    + " Order by tmc_group, tmcname, nartmc, id_group ";

            ps = conn.prepareStatement(sql);
            ps.setDate(1, new java.sql.Date(stDate));
            ps.setDate(2, new java.sql.Date(stDate));
            ps.setDate(3, new java.sql.Date(sDate));
            ps.setDate(4, new java.sql.Date(eDate));
            ps.setDate(5, new java.sql.Date(sDate));
            ps.setDate(6, new java.sql.Date(eDate));
            ps.setString(7, "%" + flagIzm + "%");
            rs = ps.executeQuery();

            while (rs.next()) {
                Vector tmp = new Vector();
                tmp.add(rs.getObject("tmc_group") == null ? "" : rs.getString("tmc_group").trim());
                tmp.add(rs.getString("tmcname").trim());
                tmp.add(rs.getString("nartmc").trim());
                tmp.add(rs.getString("ed_izm").trim());
                tmp.add(new BigDecimal(rs.getDouble("priceP")).setScale(4, RoundingMode.HALF_UP).doubleValue());
                tmp.add(UtilSkladHO.getKolvo(rs.getInt("vid"), rs.getInt("made"), rs.getString("ed_izm"), rs.getDouble("ostN")));
                tmp.add(UtilSkladHO.getKolvo(rs.getInt("vid"), rs.getInt("made"), rs.getString("ed_izm"), rs.getDouble("ostP")));
                tmp.add(UtilSkladHO.getKolvo(rs.getInt("vid"), rs.getInt("made"), rs.getString("ed_izm"), rs.getDouble("ostR")));
                elements.add(tmp);
            }

        } catch (Exception e) {
            elements = new Vector();
            System.err.println("Ошибка getDataReportPOIP() " + e);
            log.error("Ошибка getDataReportPOIP()", e);
            throw new Exception("Ошибка getDataReportPOIP() " + e.getMessage(), e);
        }
        return elements;
    }

    public Vector getDataReportMove(long stDate, long sDate, long eDate, int vid) throws Exception {
        Vector elements = new Vector();
        String sql = "";

        try {
            sql = " Select idTmc, tmcname, nartmc, sartmc, vid, made, ed_izm, id_group, tmc_group, "
                    + "	ostN, ostpN,	ostP, ostpP,	ostPD, ostpPD,	ostR, ostpR,	ostRH, ostpRH,	ostRV, ostpRV,	ostRD, ostpRD, dateN, dateP, "
                    + "	COALESCE((Select price from sklad_hud_price Where sklad_hud_price.date_st = dateN and sklad_hud_price.id_tmc = idTmc limit 1),0) as priceN, "
                    + "	COALESCE((Select price from sklad_hud_price Where sklad_hud_price.date_st = dateP and sklad_hud_price.id_tmc = idTmc limit 1),0) as priceP "
                    + "From(Select idTmc, tmcname, nartmc, sartmc, vid, made, ed_izm, id_group, "
                    + "             (Select tmc_group from sklad_hud_tmc_group where sklad_hud_tmc_group.id = id_group) as tmc_group, "
                    + "             ostN, ostpN, ostP, ostpP, ostPD, ostpPD, ostR, ostpR, "
                    + "             ostRH, ostpRH, ostRV, ostpRV, ostRD, ostpRD,  "
                    + "		(Select max(date_st) from sklad_hud_price Where sklad_hud_price.id_tmc = idTmc and date_st <= ? limit 1) as dateN, "
                    + "		(Select max(date_st) from sklad_hud_price Where sklad_hud_price.id_tmc = idTmc and date_st <= ?  limit 1) as dateP "
                    + "	 From (Select idTmc, ostN, ostpN, ostP, ostpP, ostPD, ostpPD, ostR, ostpR, ostRH, ostpRH, ostRV, ostpRV, "
                    + "		COALESCE(sum(kolvo),0) as ostRD, COALESCE(sum(kol_part),0) as ostpRD "
                    + "	From (Select idTmc, ostN, ostpN, ostP, ostpP, ostPD, ostpPD, ostR, ostpR, ostRH, ostpRH, "
                    + "			COALESCE(sum(kolvo),0) as ostRV, COALESCE(sum(kol_part),0) as ostpRV "
                    + "		From (Select idTmc, ostN, ostpN, ostP, ostpP, ostPD, ostpPD, ostR, ostpR, "
                    + "				COALESCE(sum(kolvo),0) as ostRH, COALESCE(sum(kol_part),0) as ostpRH "
                    + "			From (Select idTmc, ostN, ostpN, ostP, ostpP, ostPD, ostpPD, "
                    + "					COALESCE(sum(kolvo),0) as ostR, COALESCE(sum(kol_part),0) as ostpR "
                    + "				From (Select idTmc, ostN, ostpN, ostP, ostpP, COALESCE(sum(kolvo),0) as ostPD,  "
                    + "						COALESCE(sum(kol_part),0) as ostpPD "
                    + "					From (Select idTmc, ostN, ostpN, COALESCE(sum(kolvo),0) as ostP, COALESCE(sum(kol_part),0) as ostpP "
                    + "						From(Select idTmc, COALESCE(ost,0) as ostN, COALESCE(ost_part,0) as ostpN "
                    + "							From(Select id as idTmc from sklad_hud_tmc where vid = ? "
                    + "                                                                                                 " + (vid == 1 ? "and made = 2" : " ") + " ) as t1 "
                    + "							Left join sklad_hud_ost "
                    + "							On sklad_hud_ost.id_tmc = idTmc and "
                    + "							   sklad_hud_ost.tip_ost = 1 and "
                    + "							   sklad_hud_ost.date_f = ? ) as t3 "
                    + "					Left join sklad_hud_move "
                    + "						On sklad_hud_move.id_tmc = idTmc and "
                    + "							sklad_hud_move.tip_move <> 2 and "
                    + "							sklad_hud_move.status = 1 and "
                    + "							sklad_hud_move.id_dept = 4 and  "
                    + "							sklad_hud_move.date_move BETWEEN   ? and ? "
                    + "						Group by idTmc, ostN, ostpN) as t4 "
                    + "					Left join sklad_hud_move "
                    + "					On sklad_hud_move.id_tmc = idTmc and "
                    + "						sklad_hud_move.tip_move <> 2 and "
                    + "						sklad_hud_move.status = 1 and "
                    + "						sklad_hud_move.id_dept <> -1 and  "
                    + "						sklad_hud_move.id_dept <> 4 and "
                    + "						sklad_hud_move.date_move BETWEEN ? and ? "
                    + "					Group by idTmc, ostN, ostpN, ostP, ostpP) as t5 "
                    + "				Left join sklad_hud_move "
                    + "				On sklad_hud_move.id_tmc = idTmc and  "
                    + "					sklad_hud_move.tip_move = 2 and "
                    + "					sklad_hud_move.status = 1 and "
                    + "					sklad_hud_move.date_move BETWEEN ? and ? "
                    + "				Group by idTmc, ostN, ostpN, ostP, ostpP, ostPD, ostpPD) as t6 "
                    + "			Left join sklad_hud_move "
                    + "			On sklad_hud_move.id_tmc = idTmc and  "
                    + "				sklad_hud_move.tip_move = 2 and "
                    + "				sklad_hud_move.status = 1 and "
                    + "				sklad_hud_move.id_dept = 20 and  "
                    + "				sklad_hud_move.date_move BETWEEN  ? and ? "
                    + "			Group by idTmc, ostN, ostpN, ostP, ostpP, ostPD, ostpPD, ostR, ostpR) as t7  "
                    + "		Left join sklad_hud_move "
                    + "		On sklad_hud_move.id_tmc = idTmc and "
                    + "			sklad_hud_move.tip_move = 2 and "
                    + " 			sklad_hud_move.status = 1 and "
                    + "			sklad_hud_move.id_dept = 21 and "
                    + "			sklad_hud_move.date_move BETWEEN ? and ? "
                    + "		Group by idTmc, ostN, ostpN, ostP, ostpP, ostPD, ostpPD, ostR, ostpR, ostRH, ostpRH) as t8 "
                    + "	 Left join sklad_hud_move "
                    + "	 On sklad_hud_move.id_tmc = idTmc and "
                    + "		sklad_hud_move.tip_move = 2 and "
                    + "		sklad_hud_move.status = 1 and "
                    + "		sklad_hud_move.id_dept <> 20 and  "
                    + "		sklad_hud_move.id_dept <> 21 and  "
                    + "		sklad_hud_move.date_move BETWEEN ? and ? "
                    + "	 Group by idTmc, ostN, ostpN, ostP, ostpP, ostPD, ostpPD, ostR, ostpR, "
                    + "		ostRH, ostpRH, ostRV, ostpRV) as t9 "
                    + "	 Join sklad_hud_tmc "
                    + "	 On sklad_hud_tmc.id = idTmc and "
                    + "	   (ostN <> 0 or ostpN <> 0 or "
                    + "		ostP <> 0 or ostpP <> 0 or "
                    + "		ostPD <> 0 or ostpPD <> 0 or "
                    + "		ostR <> 0 or ostpR <> 0 or "
                    + "		ostRH <> 0 or ostpRH <> 0 or "
                    + "		ostRV <> 0 or ostpRV <> 0 or "
                    + "		ostRD <> 0 or ostpRD <> 0) ) as t10	 "
                    + " Order by tmc_group, tmcname, nartmc, id_group ";

            ps = conn.prepareStatement(sql);
            ps.setDate(1, new java.sql.Date(stDate));
            ps.setDate(2, new java.sql.Date(eDate));
            ps.setInt(3, vid);
            ps.setDate(4, new java.sql.Date(stDate));
            ps.setDate(5, new java.sql.Date(sDate));
            ps.setDate(6, new java.sql.Date(eDate));
            ps.setDate(7, new java.sql.Date(sDate));
            ps.setDate(8, new java.sql.Date(eDate));
            ps.setDate(9, new java.sql.Date(sDate));
            ps.setDate(10, new java.sql.Date(eDate));
            ps.setDate(11, new java.sql.Date(sDate));
            ps.setDate(12, new java.sql.Date(eDate));
            ps.setDate(13, new java.sql.Date(sDate));
            ps.setDate(14, new java.sql.Date(eDate));
            ps.setDate(15, new java.sql.Date(sDate));
            ps.setDate(16, new java.sql.Date(eDate));
            rs = ps.executeQuery();

            while (rs.next()) {
                Vector tmp = new Vector();
                tmp.add(rs.getObject("tmc_group") == null ? "" : rs.getString("tmc_group").trim());
                tmp.add(rs.getString("tmcname").trim());
                tmp.add(rs.getString("nartmc").trim());
                tmp.add(new BigDecimal(rs.getDouble("priceN")).setScale(4, RoundingMode.HALF_UP).doubleValue());
                tmp.add(new BigDecimal(rs.getDouble("priceP")).setScale(4, RoundingMode.HALF_UP).doubleValue());
                tmp.add(rs.getString("ed_izm").trim());
                tmp.add(UtilSkladHO.getKolvo(rs.getInt("vid"), rs.getInt("made"), rs.getString("ed_izm"), rs.getDouble("ostN")));
                tmp.add(UtilSkladHO.getKolvo(rs.getInt("vid"), rs.getInt("made"), rs.getString("ed_izm"), rs.getDouble("ostP")));
                tmp.add(UtilSkladHO.getKolvo(rs.getInt("vid"), rs.getInt("made"), rs.getString("ed_izm"), rs.getDouble("ostPD")));
                tmp.add(UtilSkladHO.getKolvo(rs.getInt("vid"), rs.getInt("made"), rs.getString("ed_izm"), rs.getDouble("ostR")));
                tmp.add(UtilSkladHO.getKolvo(rs.getInt("vid"), rs.getInt("made"), rs.getString("ed_izm"), rs.getDouble("ostRH")));
                tmp.add(UtilSkladHO.getKolvo(rs.getInt("vid"), rs.getInt("made"), rs.getString("ed_izm"), rs.getDouble("ostRV")));
                tmp.add(UtilSkladHO.getKolvo(rs.getInt("vid"), rs.getInt("made"), rs.getString("ed_izm"), rs.getDouble("ostRD")));
                elements.add(tmp);
            }

        } catch (Exception e) {
            elements = new Vector();
            System.err.println("Ошибка getDataReportMove() " + e);
            log.error("Ошибка getDataReportMove()", e);
            throw new Exception("Ошибка getDataReportMove() " + e.getMessage(), e);
        }
        return elements;
    }

    /*
    public Vector getDataReportMoveIP(long stDate, long sDate, long eDate) throws Exception{
        Vector elements = new Vector();      
        String sql = "";		      
    
        try {
            sql = "Select nartmc, ed_izm, id_group, tmc_group, sum(ostpN) as ostpN, sum(ostN) as ostN, sum(sostN) as sostN, "
                    + " sum(ostpP) as ostpP, sum(ostP) as ostP, sum(sostP) as sostP, sum(ostpPD) as ostpPD, sum(ostPD) as ostPD, sum(sostPD) as sostPD, "
                    + "	sum(ostpR) as ostpR, sum(ostR) as ostR, sum(sostR) as sostR, sum(ostpRH) as ostpRH, sum(ostRH) as ostRH, sum(sostRH) as sostRH, "
                    + "	sum(ostpRV) as ostpRV, sum(ostRV) as ostRV, sum(sostRV) as sostRV, sum(ostpRD) as ostpRD, sum(ostRD) as ostRD, sum(sostRD) as sostRD "
                    + " From( Select nartmc, ed_izm, id_group, "
                    + "	(Select tmc_group from sklad_hud_tmc_group where sklad_hud_tmc_group.id = id_group) as tmc_group, "
                    + "	ostpN, ostN, sum(ostN*priceN) as sostN, ostpP, ostP, sum(ostP*priceP) as sostP, ostpPD, ostPD, sum(ostPD*pricePD) as sostPD, "
                    + "	ostpR, ostR, sum(ostR*priceR) as sostR, ostpRH, ostRH, sum(ostRH*priceRH) as sostRH, ostpRV, ostRV, sum(ostRV*priceRV) as sostRV, "
                    + "	ostpRD, ostRD, sum(ostRD*priceRD) as sostRD "
                    + " From (Select idTmc, ostN, ostpN, priceN, ostP, ostpP, priceP, ostPD, ostpPD, pricePD, "
                    + "		ostR, ostpR, priceR, ostRH, ostpRH, priceRH, ostRV, ostpRV, priceRV, date_move as dateRD, "
                    + "		COALESCE(sum(kolvo),0) as ostRD, COALESCE(sum(kol_part),0) as ostpRD, "
                    + "		COALESCE((Select price from sklad_hud_price where sklad_hud_price.id_tmc = idTmc and date_st <= date_move Order by date_st desc limit 1) ,0) as priceRD									"
                    + "	From (Select idTmc, ostN, ostpN, priceN, ostP, ostpP, priceP, ostPD, ostpPD, pricePD, ostR, ostpR, priceR, ostRH, ostpRH, priceRH, date_move as dateRV, "
                    + "			COALESCE(sum(kolvo),0) as ostRV, COALESCE(sum(kol_part),0) as ostpRV, "
                    + "			COALESCE((Select price from sklad_hud_price where sklad_hud_price.id_tmc = idTmc and date_st <= date_move Order by date_st desc limit 1) ,0) as priceRV															"
                    + "		From (Select idTmc, ostN, ostpN, priceN, ostP, ostpP, priceP, ostPD, ostpPD, pricePD, ostR, ostpR, priceR, date_move as dateRH, "
                    + "				COALESCE(sum(kolvo),0) as ostRH, COALESCE(sum(kol_part),0) as ostpRH, "
                    + "				COALESCE((Select price from sklad_hud_price where sklad_hud_price.id_tmc = idTmc and date_st <= date_move Order by date_st desc limit 1) ,0) as priceRH																		"
                    + "			From (Select idTmc, ostN, ostpN, priceN, ostP, ostpP, priceP, ostPD, ostpPD, pricePD, date_move as dateR, "
                    + "					COALESCE(sum(kolvo),0) as ostR, COALESCE(sum(kol_part),0) as ostpR, "
                    + "					COALESCE((Select price from sklad_hud_price where sklad_hud_price.id_tmc = idTmc and date_st <= date_move Order by date_st desc limit 1) ,0) as priceR															"
                    + "				From (Select idTmc, ostN, ostpN, priceN, ostP, ostpP, priceP, date_move as datePD, "
                    + "					COALESCE(sum(kolvo),0) as ostPD, COALESCE(sum(kol_part),0) as ostpPD, "
                    + "					COALESCE((Select price from sklad_hud_price where sklad_hud_price.id_tmc = idTmc and date_st <= date_move Order by date_st desc limit 1) ,0) as pricePD											"
                    + "					From (Select idTmc, ostN, ostpN, priceN, date_move as dateP, "
                    + "						COALESCE(sum(kolvo),0) as ostP, COALESCE(sum(kol_part),0) as ostpP, "
                    + "						COALESCE((Select price from sklad_hud_price where sklad_hud_price.id_tmc = idTmc and date_st <= date_move Order by date_st desc limit 1) ,0) as priceP						"
                    + "						From(Select idTmc, COALESCE(ost,0) as ostN, COALESCE(ost_part,0) as ostpN, "
                    + "							COALESCE((Select price from sklad_hud_price where sklad_hud_price.id_tmc = idTmc and date_st <= ? Order by date_st desc limit 1) ,0) as priceN "
                    + "							From(Select id as idTmc from sklad_hud_tmc where vid = 1 and made = 2) as t1							"
                    + "							Left join sklad_hud_ost "
                    + "							On sklad_hud_ost.id_tmc = idTmc and "
                    + "							   sklad_hud_ost.tip_ost = 1 and "
                    + "							   sklad_hud_ost.date_f = ?) as t3 "
                    + "					Left join sklad_hud_move "
                    + "						On sklad_hud_move.id_tmc = idTmc and "
                    + "							sklad_hud_move.tip_move <> 2 and "
                    + "							sklad_hud_move.status = 1 and "
                    + "							sklad_hud_move.id_dept = 4 and "
                    + "							sklad_hud_move.date_move BETWEEN ? and ? "
                    + "						Group by idTmc, ostN, ostpN, priceN, dateP) as t4 "
                    + "					Left join sklad_hud_move "
                    + "					On sklad_hud_move.id_tmc = idTmc and"
                    + "						sklad_hud_move.tip_move <> 2 and"
                    + "						sklad_hud_move.status = 1 and"
                    + "						sklad_hud_move.id_dept <> 4 and"
                    + "						sklad_hud_move.date_move BETWEEN ? and ? "
                    + "					Group by idTmc, ostN, ostpN, priceN, ostP, ostpP, priceP, datePD) as t5 "
                    + "				Left join sklad_hud_move "
                    + "				On sklad_hud_move.id_tmc = idTmc and "
                    + "					sklad_hud_move.tip_move = 2 and "
                    + "					sklad_hud_move.status = 1 and "
                    + "					sklad_hud_move.date_move BETWEEN ? and ? "
                    + "				Group by idTmc, ostN, ostpN, priceN, ostP, ostpP, priceP, ostPD, ostpPD, pricePD, dateR) as t6 "
                    + "			Left join sklad_hud_move "
                    + "			On sklad_hud_move.id_tmc = idTmc and "
                    + "				sklad_hud_move.tip_move = 2 and "
                    + "				sklad_hud_move.status = 1 and "
                    + "				sklad_hud_move.id_dept = 20 and "
                    + "				sklad_hud_move.date_move BETWEEN ? and ? "
                    + "			Group by idTmc, ostN, ostpN, priceN, ostP, ostpP, priceP, ostPD, ostpPD, pricePD, ostR, ostpR, priceR, dateRH) as t7 "
                    + "		Left join sklad_hud_move "
                    + "		On sklad_hud_move.id_tmc = idTmc and "
                    + "			sklad_hud_move.tip_move = 2 and "
                    + "			sklad_hud_move.status = 1 and "
                    + "			sklad_hud_move.id_dept = 21 and "
                    + "			sklad_hud_move.date_move BETWEEN ? and ? "
                    + "		Group by idTmc, ostN, ostpN, priceN, ostP, ostpP, priceP, ostPD, ostpPD, pricePD, "
                    + "			 ostR, ostpR, priceR, ostRH, ostpRH, priceRH, dateRV) as t8 "
                    + "	 Left join sklad_hud_move "
                    + "	 On sklad_hud_move.id_tmc = idTmc and "
                    + "		sklad_hud_move.tip_move = 2 and "
                    + "		sklad_hud_move.status = 1 and"
                    + "		sklad_hud_move.id_dept <> 20 and "
                    + "		sklad_hud_move.id_dept <> 21 and "
                    + "		sklad_hud_move.date_move BETWEEN ? and ? "
                    + "	 Group by idTmc, ostN, ostpN, priceN, ostP, ostpP, priceP, ostPD, ostpPD, pricePD, "
                    + "		  ostR, ostpR, priceR, ostRH, ostpRH, priceRH, ostRV, ostpRV, priceRV, dateRD) as t9 "
                    + " Join sklad_hud_tmc "
                    + " On sklad_hud_tmc.id = idTmc and "
                    + "   (ostN <> 0 or ostpN <> 0 or "
                    + "	ostP <> 0 or ostpP <> 0 or "
                    + "	ostPD <> 0 or ostpPD <> 0 or "
                    + "	ostR <> 0 or ostpR <> 0 or "
                    + "	ostRH <> 0 or ostpRH <> 0 or "
                    + "	ostRV <> 0 or ostpRV <> 0 or "
                    + "	ostRD <> 0 or ostpRD <> 0) "
                    + " Group by id_group, tmc_group, nartmc, ed_izm, "
                    + "     ostpN, ostN, ostpP, ostP, ostpPD, ostPD, ostpR, ostR, ostpRH, ostRH, ostpRV, ostRV, ostpRD, ostRD) as t10 "
                    + " Group by nartmc, ed_izm, id_group, tmc_group " 
                    + " Order by tmc_group, nartmc, id_group ";
            
            ps = conn.prepareStatement(sql);   
            ps.setDate(1, new java.sql.Date(stDate));            
            ps.setDate(2, new java.sql.Date(stDate));
            ps.setDate(3, new java.sql.Date(sDate));
            ps.setDate(4, new java.sql.Date(eDate));
            ps.setDate(5, new java.sql.Date(sDate));
            ps.setDate(6, new java.sql.Date(eDate));
            ps.setDate(7, new java.sql.Date(sDate));
            ps.setDate(8, new java.sql.Date(eDate));
            ps.setDate(9, new java.sql.Date(sDate));
            ps.setDate(10, new java.sql.Date(eDate));
            ps.setDate(11, new java.sql.Date(sDate));
            ps.setDate(12, new java.sql.Date(eDate));
            ps.setDate(13, new java.sql.Date(sDate));
            ps.setDate(14, new java.sql.Date(eDate));
            rs = ps.executeQuery();
                        
            while(rs.next()){
                Vector tmp = new Vector();
                tmp.add(rs.getObject("tmc_group")== null?"":rs.getString("tmc_group").trim());
                tmp.add(rs.getString("nartmc").trim());
                tmp.add(rs.getString("ed_izm").trim());
                tmp.add(UtilSkladHO.getKolvo(rs.getInt("vid"), rs.getString("ed_izm"), rs.getDouble("ostN")));
                tmp.add(rs.getDouble("sostN"));
                tmp.add(UtilSkladHO.getKolvo(rs.getInt("vid"), rs.getString("ed_izm"), rs.getDouble("ostP")));
                tmp.add(rs.getDouble("sostP"));
                tmp.add(UtilSkladHO.getKolvo(rs.getInt("vid"), rs.getString("ed_izm"), rs.getDouble("ostPD")));
                tmp.add(rs.getDouble("sostPD"));
                tmp.add(UtilSkladHO.getKolvo(rs.getInt("vid"), rs.getString("ed_izm"), rs.getDouble("ostR")));
                tmp.add(rs.getDouble("sostR"));
                tmp.add(UtilSkladHO.getKolvo(rs.getInt("vid"), rs.getString("ed_izm"), rs.getDouble("ostRH")));
                tmp.add(rs.getDouble("sostRH"));
                tmp.add(UtilSkladHO.getKolvo(rs.getInt("vid"), rs.getString("ed_izm"), rs.getDouble("ostRV")));
                tmp.add(rs.getDouble("sostRV"));
                tmp.add(UtilSkladHO.getKolvo(rs.getInt("vid"), rs.getString("ed_izm"), rs.getDouble("ostRD")));
                tmp.add(rs.getDouble("sostRD"));
                elements.add(tmp);
            }
            
        } catch (Exception e) {
            elements = new Vector();
            System.err.println("Ошибка getDataReportMoveIP() "+ e);
            log.error("Ошибка getDataReportMoveIP()", e);
            throw new Exception("Ошибка getDataReportMoveIP() " + e.getMessage(), e);
        }
        return elements; 
    }
    
    public Vector getDataReportMoveVM(long stDate, long sDate, long eDate) throws Exception{
        Vector elements = new Vector();      
        String sql = "";		      
    
        try {
            sql = "Select ed_izm, id_group, tmc_group, sum(ostpN) as ostpN, sum(ostN) as ostN, sum(sostN) as sostN, "
                    + " sum(ostpP) as ostpP, sum(ostP) as ostP, sum(sostP) as sostP, sum(ostpPD) as ostpPD, sum(ostPD) as ostPD, sum(sostPD) as sostPD, "
                    + "	sum(ostpR) as ostpR, sum(ostR) as ostR, sum(sostR) as sostR, sum(ostpRH) as ostpRH, sum(ostRH) as ostRH, sum(sostRH) as sostRH, "
                    + "	sum(ostpRV) as ostpRV, sum(ostRV) as ostRV, sum(sostRV) as sostRV, sum(ostpRD) as ostpRD, sum(ostRD) as ostRD, sum(sostRD) as sostRD "
                    + " From( Select ed_izm, id_group, "
                    + "	(Select tmc_group from sklad_hud_tmc_group where sklad_hud_tmc_group.id = id_group) as tmc_group, "
                    + "	ostpN, ostN, sum(ostN*priceN) as sostN, ostpP, ostP, sum(ostP*priceP) as sostP, ostpPD, ostPD, sum(ostPD*pricePD) as sostPD, "
                    + "	ostpR, ostR, sum(ostR*priceR) as sostR, ostpRH, ostRH, sum(ostRH*priceRH) as sostRH, ostpRV, ostRV, sum(ostRV*priceRV) as sostRV, "
                    + "	ostpRD, ostRD, sum(ostRD*priceRD) as sostRD "
                    + " From (Select idTmc, ostN, ostpN, priceN, ostP, ostpP, priceP, ostPD, ostpPD, pricePD, "
                    + "		ostR, ostpR, priceR, ostRH, ostpRH, priceRH, ostRV, ostpRV, priceRV, date_move as dateRD, "
                    + "		COALESCE(sum(kolvo),0) as ostRD, COALESCE(sum(kol_part),0) as ostpRD, "
                    + "		COALESCE((Select price from sklad_hud_price where sklad_hud_price.id_tmc = idTmc and date_st <= date_move Order by date_st desc limit 1) ,0) as priceRD									"
                    + "	From (Select idTmc, ostN, ostpN, priceN, ostP, ostpP, priceP, ostPD, ostpPD, pricePD, ostR, ostpR, priceR, ostRH, ostpRH, priceRH, date_move as dateRV, "
                    + "			COALESCE(sum(kolvo),0) as ostRV, COALESCE(sum(kol_part),0) as ostpRV, "
                    + "			COALESCE((Select price from sklad_hud_price where sklad_hud_price.id_tmc = idTmc and date_st <= date_move Order by date_st desc limit 1) ,0) as priceRV															"
                    + "		From (Select idTmc, ostN, ostpN, priceN, ostP, ostpP, priceP, ostPD, ostpPD, pricePD, ostR, ostpR, priceR, date_move as dateRH, "
                    + "				COALESCE(sum(kolvo),0) as ostRH, COALESCE(sum(kol_part),0) as ostpRH, "
                    + "				COALESCE((Select price from sklad_hud_price where sklad_hud_price.id_tmc = idTmc and date_st <= date_move Order by date_st desc limit 1) ,0) as priceRH																		"
                    + "			From (Select idTmc, ostN, ostpN, priceN, ostP, ostpP, priceP, ostPD, ostpPD, pricePD, date_move as dateR, "
                    + "					COALESCE(sum(kolvo),0) as ostR, COALESCE(sum(kol_part),0) as ostpR, "
                    + "					COALESCE((Select price from sklad_hud_price where sklad_hud_price.id_tmc = idTmc and date_st <= date_move Order by date_st desc limit 1) ,0) as priceR															"
                    + "				From (Select idTmc, ostN, ostpN, priceN, ostP, ostpP, priceP, date_move as datePD, "
                    + "					COALESCE(sum(kolvo),0) as ostPD, COALESCE(sum(kol_part),0) as ostpPD, "
                    + "					COALESCE((Select price from sklad_hud_price where sklad_hud_price.id_tmc = idTmc and date_st <= date_move Order by date_st desc limit 1) ,0) as pricePD											"
                    + "					From (Select idTmc, ostN, ostpN, priceN, date_move as dateP, "
                    + "						COALESCE(sum(kolvo),0) as ostP, COALESCE(sum(kol_part),0) as ostpP, "
                    + "						COALESCE((Select price from sklad_hud_price where sklad_hud_price.id_tmc = idTmc and date_st <= date_move Order by date_st desc limit 1) ,0) as priceP						"
                    + "						From(Select idTmc, COALESCE(ost,0) as ostN, COALESCE(ost_part,0) as ostpN, "
                    + "							COALESCE((Select price from sklad_hud_price where sklad_hud_price.id_tmc = idTmc and date_st <= ? Order by date_st desc limit 1) ,0) as priceN "
                    + "							From(Select id as idTmc from sklad_hud_tmc where vid = 2 ) as t1							"
                    + "							Left join sklad_hud_ost "
                    + "							On sklad_hud_ost.id_tmc = idTmc and "
                    + "							   sklad_hud_ost.tip_ost = 1 and "
                    + "							   sklad_hud_ost.date_f = ?) as t3 "
                    + "					Left join sklad_hud_move "
                    + "						On sklad_hud_move.id_tmc = idTmc and "
                    + "							sklad_hud_move.tip_move <> 2 and "
                    + "							sklad_hud_move.status = 1 and "
                    + "							sklad_hud_move.id_dept = 4 and "
                    + "							sklad_hud_move.date_move BETWEEN ? and ? "
                    + "						Group by idTmc, ostN, ostpN, priceN, dateP) as t4 "
                    + "					Left join sklad_hud_move "
                    + "					On sklad_hud_move.id_tmc = idTmc and"
                    + "						sklad_hud_move.tip_move <> 2 and"
                    + "						sklad_hud_move.status = 1 and"
                    + "						sklad_hud_move.id_dept <> 4 and"
                    + "						sklad_hud_move.date_move BETWEEN ? and ? "
                    + "					Group by idTmc, ostN, ostpN, priceN, ostP, ostpP, priceP, datePD) as t5 "
                    + "				Left join sklad_hud_move "
                    + "				On sklad_hud_move.id_tmc = idTmc and "
                    + "					sklad_hud_move.tip_move = 2 and "
                    + "					sklad_hud_move.status = 1 and "
                    + "					sklad_hud_move.date_move BETWEEN ? and ? "
                    + "				Group by idTmc, ostN, ostpN, priceN, ostP, ostpP, priceP, ostPD, ostpPD, pricePD, dateR) as t6 "
                    + "			Left join sklad_hud_move "
                    + "			On sklad_hud_move.id_tmc = idTmc and "
                    + "				sklad_hud_move.tip_move = 2 and "
                    + "				sklad_hud_move.status = 1 and "
                    + "				sklad_hud_move.id_dept = 20 and "
                    + "				sklad_hud_move.date_move BETWEEN ? and ? "
                    + "			Group by idTmc, ostN, ostpN, priceN, ostP, ostpP, priceP, ostPD, ostpPD, pricePD, ostR, ostpR, priceR, dateRH) as t7 "
                    + "		Left join sklad_hud_move "
                    + "		On sklad_hud_move.id_tmc = idTmc and "
                    + "			sklad_hud_move.tip_move = 2 and "
                    + "			sklad_hud_move.status = 1 and "
                    + "			sklad_hud_move.id_dept = 21 and "
                    + "			sklad_hud_move.date_move BETWEEN ? and ? "
                    + "		Group by idTmc, ostN, ostpN, priceN, ostP, ostpP, priceP, ostPD, ostpPD, pricePD, "
                    + "			 ostR, ostpR, priceR, ostRH, ostpRH, priceRH, dateRV) as t8 "
                    + "	 Left join sklad_hud_move "
                    + "	 On sklad_hud_move.id_tmc = idTmc and "
                    + "		sklad_hud_move.tip_move = 2 and "
                    + "		sklad_hud_move.status = 1 and"
                    + "		sklad_hud_move.id_dept <> 20 and "
                    + "		sklad_hud_move.id_dept <> 21 and "
                    + "		sklad_hud_move.date_move BETWEEN ? and ? "
                    + "	 Group by idTmc, ostN, ostpN, priceN, ostP, ostpP, priceP, ostPD, ostpPD, pricePD, "
                    + "		  ostR, ostpR, priceR, ostRH, ostpRH, priceRH, ostRV, ostpRV, priceRV, dateRD) as t9 "
                    + " Join sklad_hud_tmc "
                    + " On sklad_hud_tmc.id = idTmc and "
                    + "   (ostN <> 0 or ostpN <> 0 or "
                    + "	ostP <> 0 or ostpP <> 0 or "
                    + "	ostPD <> 0 or ostpPD <> 0 or "
                    + "	ostR <> 0 or ostpR <> 0 or "
                    + "	ostRH <> 0 or ostpRH <> 0 or "
                    + "	ostRV <> 0 or ostpRV <> 0 or "
                    + "	ostRD <> 0 or ostpRD <> 0) "
                    + " Group by id_group, tmc_group, ed_izm,"
                    + "     ostpN, ostN, ostpP, ostP, ostpPD, ostPD, ostpR, ostR, ostpRH, ostRH, ostpRV, ostRV, ostpRD, ostRD ) as t10 "
                    + " Group by ed_izm, id_group, tmc_group " 
                    + " Order by tmc_group, id_group ";
            
            ps = conn.prepareStatement(sql);   
            ps.setDate(1, new java.sql.Date(stDate));            
            ps.setDate(2, new java.sql.Date(stDate));
            ps.setDate(3, new java.sql.Date(sDate));
            ps.setDate(4, new java.sql.Date(eDate));
            ps.setDate(5, new java.sql.Date(sDate));
            ps.setDate(6, new java.sql.Date(eDate));
            ps.setDate(7, new java.sql.Date(sDate));
            ps.setDate(8, new java.sql.Date(eDate));
            ps.setDate(9, new java.sql.Date(sDate));
            ps.setDate(10, new java.sql.Date(eDate));
            ps.setDate(11, new java.sql.Date(sDate));
            ps.setDate(12, new java.sql.Date(eDate));
            ps.setDate(13, new java.sql.Date(sDate));
            ps.setDate(14, new java.sql.Date(eDate));
            rs = ps.executeQuery();
                        
            while(rs.next()){
                Vector tmp = new Vector();
                tmp.add(rs.getObject("tmc_group")== null?"":rs.getString("tmc_group").trim());
                tmp.add(rs.getString("ed_izm").trim());
                //tmp.add(rs.getDouble("ostpN"));                 
                tmp.add(rs.getDouble("ostN")); 
                tmp.add(rs.getDouble("sostN"));  
                //tmp.add(rs.getDouble("ostpP"));                 
                tmp.add(rs.getDouble("ostP")); 
                tmp.add(rs.getDouble("sostP"));                
                //tmp.add(rs.getDouble("ostpPD"));                 
                tmp.add(rs.getDouble("ostPD"));   
                tmp.add(rs.getDouble("sostPD"));                              
                //tmp.add(rs.getDouble("ostpR"));                 
                tmp.add(rs.getDouble("ostR"));  
                tmp.add(rs.getDouble("sostR"));                               
                //tmp.add(rs.getDouble("ostpRH"));                 
                tmp.add(rs.getDouble("ostRH"));  
                tmp.add(rs.getDouble("sostRH"));                               
                //tmp.add(rs.getDouble("ostpRV"));                 
                tmp.add(rs.getDouble("ostRV")); 
                tmp.add(rs.getDouble("sostRV"));                
                //tmp.add(rs.getDouble("ostpRD"));                 
                tmp.add(rs.getDouble("ostRD")); 
                tmp.add(rs.getDouble("sostRD"));                
                elements.add(tmp);
            }
            
        } catch (Exception e) {
            elements = new Vector();
            System.err.println("Ошибка getDataReportMoveVM() "+ e);
            log.error("Ошибка getDataReportMoveVM()", e);
            throw new Exception("Ошибка getDataReportMoveVM() " + e.getMessage(), e);
        }
        return elements; 
    }
    */
    public Vector getDataReportMoveNP(long stDate, long sDate, long eDate) throws Exception {
        Vector elements = new Vector();
        String sql = "";

        try {
            sql = " Select idTmc, tmcname, nartmc, sartmc, vid, made, ed_izm, id_group, "
                    + " (Select tmc_group from sklad_hud_tmc_group where sklad_hud_tmc_group.id = id_group) as tmc_group,  "
                    + " ostN, ostpN, ostP, ostpP, ostPD, ostpPD, ostR, ostpR, "
                    + "	ostRH, ostpRH, ostRV, ostpRV, ostRD, ostpRD "
                    + "	 From (Select idTmc, ostN, ostpN, ostP, ostpP, ostPD, ostpPD, ostR, ostpR, ostRH, ostpRH, ostRV, ostpRV, "
                    + "		COALESCE(sum(kolvo),0) as ostRD, COALESCE(sum(kol_part),0) as ostpRD "
                    + "	From (Select idTmc, ostN, ostpN, ostP, ostpP, ostPD, ostpPD, ostR, ostpR, ostRH, ostpRH, "
                    + "			COALESCE(sum(kolvo),0) as ostRV, COALESCE(sum(kol_part),0) as ostpRV "
                    + "		From (Select idTmc, ostN, ostpN, ostP, ostpP, ostPD, ostpPD, ostR, ostpR, "
                    + "				COALESCE(sum(kolvo),0) as ostRH, COALESCE(sum(kol_part),0) as ostpRH "
                    + "			From (Select idTmc, ostN, ostpN, ostP, ostpP, ostPD, ostpPD, "
                    + "					COALESCE(sum(kolvo),0) as ostR, COALESCE(sum(kol_part),0) as ostpR "
                    + "				From (Select idTmc, ostN, ostpN, ostP, ostpP, COALESCE(sum(kolvo),0) as ostPD,  "
                    + "						COALESCE(sum(kol_part),0) as ostpPD "
                    + "					From (Select idTmc, ostN, ostpN, COALESCE(sum(kolvo),0) as ostP, COALESCE(sum(kol_part),0) as ostpP "
                    + "						From(Select idTmc, COALESCE(ost,0) as ostN, COALESCE(ost_part,0) as ostpN "
                    + "							From(Select id as idTmc from sklad_hud_tmc where vid = 1 and made = 1 ) as t1 "
                    + "							Left join sklad_hud_ost "
                    + "							On sklad_hud_ost.id_tmc = idTmc and "
                    + "							   sklad_hud_ost.tip_ost = 1 and "
                    + "							   sklad_hud_ost.date_f = ? ) as t3 "
                    + "					Left join sklad_hud_move "
                    + "						On sklad_hud_move.id_tmc = idTmc and "
                    + "							sklad_hud_move.tip_move <> 2 and "
                    + "							sklad_hud_move.status = 1 and "
                    + "							sklad_hud_move.id_dept = 4 and  "
                    + "							sklad_hud_move.date_move BETWEEN   ? and ? "
                    + "						Group by idTmc, ostN, ostpN) as t4 "
                    + "					Left join sklad_hud_move "
                    + "					On sklad_hud_move.id_tmc = idTmc and "
                    + "						sklad_hud_move.tip_move <> 2 and "
                    + "						sklad_hud_move.status = 1 and "
                    + "						sklad_hud_move.id_dept <> 4 and "
                    + "						sklad_hud_move.date_move BETWEEN ? and ? "
                    + "					Group by idTmc, ostN, ostpN, ostP, ostpP) as t5 "
                    + "				Left join sklad_hud_move "
                    + "				On sklad_hud_move.id_tmc = idTmc and  "
                    + "					sklad_hud_move.tip_move = 2 and "
                    + "					sklad_hud_move.status = 1 and "
                    + "					sklad_hud_move.date_move BETWEEN ? and ? "
                    + "				Group by idTmc, ostN, ostpN, ostP, ostpP, ostPD, ostpPD) as t6 "
                    + "			Left join sklad_hud_move "
                    + "			On sklad_hud_move.id_tmc = idTmc and  "
                    + "				sklad_hud_move.tip_move = 2 and "
                    + "				sklad_hud_move.status = 1 and "
                    + "				sklad_hud_move.id_dept = 20 and  "
                    + "				sklad_hud_move.date_move BETWEEN  ? and ? "
                    + "			Group by idTmc, ostN, ostpN, ostP, ostpP, ostPD, ostpPD, ostR, ostpR) as t7  "
                    + "		Left join sklad_hud_move "
                    + "		On sklad_hud_move.id_tmc = idTmc and "
                    + "			sklad_hud_move.tip_move = 2 and "
                    + " 		sklad_hud_move.status = 1 and "
                    + "			sklad_hud_move.id_dept = 21 and "
                    + "			sklad_hud_move.date_move BETWEEN ? and ? "
                    + "		Group by idTmc, ostN, ostpN, ostP, ostpP, ostPD, ostpPD, ostR, ostpR, ostRH, ostpRH) as t8 "
                    + "	 Left join sklad_hud_move "
                    + "	 On sklad_hud_move.id_tmc = idTmc and "
                    + "		sklad_hud_move.tip_move = 2 and "
                    + "		sklad_hud_move.status = 1 and "
                    + "		sklad_hud_move.id_dept <> 20 and  "
                    + "		sklad_hud_move.id_dept <> 21 and  "
                    + "		sklad_hud_move.date_move BETWEEN  ? and ? "
                    + "	 Group by idTmc, ostN, ostpN, ostP, ostpP, ostPD, ostpPD, ostR, ostpR, "
                    + "		ostRH, ostpRH, ostRV, ostpRV) as t9 "
                    + "	 Join sklad_hud_tmc "
                    + "	 On sklad_hud_tmc.id = idTmc and "
                    + "	   (ostN <> 0 or ostpN <> 0 or "
                    + "		ostP <> 0 or ostpP <> 0 or "
                    + "		ostPD <> 0 or ostpPD <> 0 or "
                    + "		ostR <> 0 or ostpR <> 0 or "
                    + "		ostRH <> 0 or ostpRH <> 0 or "
                    + "		ostRV <> 0 or ostpRV <> 0 or "
                    + "		ostRD <> 0 or ostpRD <> 0)  "
                    + " Order by tmc_group, tmcname, nartmc ";

            ps = conn.prepareStatement(sql);
            ps.setDate(1, new java.sql.Date(stDate));
            ps.setDate(2, new java.sql.Date(sDate));
            ps.setDate(3, new java.sql.Date(eDate));
            ps.setDate(4, new java.sql.Date(sDate));
            ps.setDate(5, new java.sql.Date(eDate));
            ps.setDate(6, new java.sql.Date(sDate));
            ps.setDate(7, new java.sql.Date(eDate));
            ps.setDate(8, new java.sql.Date(sDate));
            ps.setDate(9, new java.sql.Date(eDate));
            ps.setDate(10, new java.sql.Date(sDate));
            ps.setDate(11, new java.sql.Date(eDate));
            ps.setDate(12, new java.sql.Date(sDate));
            ps.setDate(13, new java.sql.Date(eDate));
            rs = ps.executeQuery();

            while (rs.next()) {
                Vector tmp = new Vector();
                tmp.add(rs.getObject("tmc_group") == null ? "" : rs.getString("tmc_group").trim());
                tmp.add(rs.getString("tmcname").trim());
                tmp.add(rs.getString("nartmc").trim());
                tmp.add(rs.getString("ed_izm").trim());
                tmp.add(UtilSkladHO.getKolvo(rs.getInt("vid"), rs.getInt("made"), rs.getString("ed_izm"), rs.getDouble("ostN")));
                tmp.add(UtilSkladHO.getKolvo(rs.getInt("vid"), rs.getInt("made"), rs.getString("ed_izm"), rs.getDouble("ostP")));
                tmp.add(UtilSkladHO.getKolvo(rs.getInt("vid"), rs.getInt("made"), rs.getString("ed_izm"), rs.getDouble("ostPD")));
                tmp.add(UtilSkladHO.getKolvo(rs.getInt("vid"), rs.getInt("made"), rs.getString("ed_izm"), rs.getDouble("ostR")));
                tmp.add(UtilSkladHO.getKolvo(rs.getInt("vid"), rs.getInt("made"), rs.getString("ed_izm"), rs.getDouble("ostRH")));
                tmp.add(UtilSkladHO.getKolvo(rs.getInt("vid"), rs.getInt("made"), rs.getString("ed_izm"), rs.getDouble("ostRV")));
                tmp.add(UtilSkladHO.getKolvo(rs.getInt("vid"), rs.getInt("made"), rs.getString("ed_izm"), rs.getDouble("ostRD")));
                elements.add(tmp);
            }

        } catch (Exception e) {
            elements = new Vector();
            System.err.println("Ошибка getDataReportMoveNP() " + e);
            log.error("Ошибка getDataReportMoveNP()", e);
            throw new Exception("Ошибка getDataReportMoveNP() " + e.getMessage(), e);
        }
        return elements;
    }

    public boolean executeOstSkladHO(long stDate, long sNDate, long sDate, long eDate, int idUser) throws Exception {
        boolean rezalt = false;
        Vector data = new Vector();
        String sql = "";

        try {
            setAutoCommit(false);

            // Удаляем старые данные за этот период 

            sql = "Delete From sklad_hud_ost Where date_f = ? ";
            ps = conn.prepareStatement(sql);
            ps.setDate(1, new java.sql.Date(stDate));
            ps.execute();

            sql = "Delete From sklad_hud_ost_prod Where date_f = ? ";
            ps = conn.prepareStatement(sql);
            ps.setDate(1, new java.sql.Date(stDate));
            ps.execute();

            // Поиск актуальных остатков и заполнение таблиц остатков 

            // Остатки ТМЦ в кладовой
            try {
                data = getOst1Date(sNDate, sDate, eDate, false);
            } catch (Exception e) {
                data = new Vector();
            }

            sql = "Insert into sklad_hud_ost(tip_ost, date_f, id_tmc, ost, ost_part, id_empl_vvod) "
                    + "values( ?, ?, ?, ?, ?, ?) returning id";

            for (Iterator it = data.iterator(); it.hasNext(); ) {
                Vector vec = (Vector) it.next();

                ps = conn.prepareStatement(sql);
                ps.setInt(1, 1);
                ps.setDate(2, new java.sql.Date(stDate));
                ps.setInt(3, Integer.valueOf(vec.get(2).toString()));
                ps.setDouble(4, Double.valueOf(vec.get(12).toString()));
                ps.setInt(5, Integer.valueOf(vec.get(14).toString()));
                ps.setInt(6, idUser);
                ps.execute();
            }

            // Остатки ТМЦ в ХЭО/Внедр.

            try {
                data = getOst2Date(stDate, sDate, eDate, false);
            } catch (Exception e) {
                data = new Vector();
            }

            sql = "Insert into sklad_hud_ost(tip_ost, date_f, id_tmc, ost, ost_part, id_dept, id_empl, id_empl_vvod) "
                    + "values( ?, ?, ?, ?, ?, ?, ?, ?) returning id";

            for (Iterator it = data.iterator(); it.hasNext(); ) {
                Vector vec = (Vector) it.next();

                ps = conn.prepareStatement(sql);
                ps.setInt(1, 2);
                ps.setDate(2, new java.sql.Date(stDate));
                ps.setInt(3, Integer.valueOf(vec.get(2).toString()));
                ps.setDouble(4, Double.valueOf(vec.get(14).toString()));
                ps.setInt(5, Integer.valueOf(vec.get(16).toString()));
                ps.setInt(6, Integer.valueOf(vec.get(17).toString()));
                ps.setInt(7, Integer.valueOf(vec.get(19).toString()));
                ps.setInt(8, idUser);
                ps.execute();
            }

            // Остатки моделей в кладовой

            try {
                data = getOst3Date(stDate, sDate, eDate, false);
            } catch (Exception e) {
                data = new Vector();
            }

            sql = "Insert into sklad_hud_ost_prod "
                    + " (tip_ost, date_f, fas, vfas, pname, ost, id_empl_vvod) "
                    + "  values( ?, ?, ?, ?, ?, ?, ?) returning id ";

            for (Iterator it = data.iterator(); it.hasNext(); ) {
                Vector vec = (Vector) it.next();

                ps = conn.prepareStatement(sql);
                ps.setInt(1, 1);
                ps.setDate(2, new java.sql.Date(stDate));
                ps.setInt(3, Integer.valueOf(vec.get(1).toString()));
                ps.setDouble(4, Double.valueOf(vec.get(3).toString()));
                ps.setString(5, vec.get(2).toString().trim().toUpperCase());
                ps.setInt(6, Integer.valueOf(vec.get(8).toString()));
                ps.setInt(7, idUser);
                ps.execute();
            }

            commit();

            rezalt = true;
        } catch (Exception e) {
            rezalt = false;
            rollBack();
            System.err.println("Ошибка executeOstSkladHO() " + e);
            log.error("Ошибка executeOstSkladHO()", e);
            throw new Exception("Ошибка executeOstSkladHO() " + e.getMessage(), e);
        } finally {
            setAutoCommit(true);
        }
        return rezalt;
    }

    public Vector getAllPeriodOstSkladHO() throws Exception {
        Vector elements = new Vector();
        String sql = "";

        try {
            sql = "Select distinct date_f, fio, date_vvod "
                    + " From sklad_hud_ost, employees "
                    + " Where sklad_hud_ost.id_empl_vvod = employees.id "
                    + " Order by date_f, date_vvod ";
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                Vector tmp = new Vector();
                tmp.add(false);
                tmp.add(new SimpleDateFormat("dd-MM-yyyy").format(rs.getDate("date_f")));
                tmp.add(rs.getString("fio"));
                tmp.add(new SimpleDateFormat("dd-MM-yyyy").format(rs.getDate("date_vvod")));
                elements.add(tmp);
            }

        } catch (Exception e) {
            elements = new Vector();
            System.err.println("Ошибка getAllPeriodOstSkladHO() " + e);
            log.error("Ошибка getAllPeriodOstSkladHO()", e);
            throw new Exception("Ошибка getAllPeriodOstSkladHO() " + e.getMessage(), e);
        }
        return elements;
    }

    public boolean deletePeriodOstSkladHO(long sDate) throws Exception {
        boolean rezalt = false;
        String sql = "";

        try {
            sql = "Delete From sklad_hud_ost Where date_f = ?";
            ps = conn.prepareStatement(sql);
            ps.setDate(1, new java.sql.Date(sDate));
            ps.execute();

            rezalt = true;
        } catch (Exception e) {
            rezalt = false;
            System.err.println("Ошибка deletePeriodOstSkladHO() " + e);
            log.error("Ошибка deletePeriodOstSkladHO()", e);
            throw new Exception("Ошибка deletePeriodOstSkladHO() " + e.getMessage(), e);
        }

        return rezalt;
    }

    public boolean addSkladStorage(long stDate, String doc, String note,
                                   Object idDept, Object idEmpl, int idUser, int status, Vector dataItem) throws Exception {
        boolean rezalt = false;
        String sql = "";

        try {
            setAutoCommit(false);

            sql = "Insert into sklad_hud_storage(date_st,  id_dept,  id_empl,  doc,  note, id_empl_vvod, id_empl_ins,  status) "
                    + "values( ?, ?, ?, ?, ?, ?, ?, ?) returning id";
            ps = conn.prepareStatement(sql);
            ps.setDate(1, new java.sql.Date(stDate));
            ps.setObject(2, idDept);
            ps.setObject(3, idEmpl);
            ps.setString(4, doc.trim());
            ps.setString(5, note.trim());
            ps.setInt(6, idUser);
            ps.setInt(7, idUser);
            ps.setInt(8, status);
            rs = ps.executeQuery();

            if (rs.next()) {
                sql = "Insert into sklad_hud_storage_item (id_storage, id_prod, fas_s, pname_s, id_empl_vvod, id_empl_ins) "
                        + " values( ?, ?, ?, ?, ?, ?)";
                for (Iterator it = dataItem.iterator(); it.hasNext(); ) {
                    Vector vec = (Vector) it.next();

                    ps = conn.prepareStatement(sql);
                    ps.setInt(1, rs.getInt(1));
                    ps.setInt(2, Integer.valueOf(vec.get(1).toString()));
                    ps.setInt(3, vec.get(21).toString().trim().equals("") ? -1 : Integer.valueOf(vec.get(21).toString()));
                    ps.setString(4, vec.get(21).toString().trim().equals("") ? "" : vec.get(22).toString().trim().toUpperCase());
                    ps.setInt(5, idUser);
                    ps.setInt(6, idUser);
                    ps.execute();
                }

            } else
                throw new Exception();


            commit();

            rezalt = true;
        } catch (Exception e) {
            rezalt = false;
            rollBack();
            System.err.println("Ошибка addSkladStorage() " + e);
            log.error("Ошибка addSkladStorage()", e);
            throw new Exception("Ошибка addSkladStorage() " + e.getMessage(), e);
        } finally {
            setAutoCommit(true);
        }
        return rezalt;
    }

    public boolean editSkladStorage(int idSt, long stDate, String doc, String note,
                                    Object idDept, Object idEmpl, int idUser, int status, Vector dataItem) throws Exception {
        boolean rezalt = false;
        String sql = "";

        try {
            setAutoCommit(false);

            sql = "UPDATE sklad_hud_storage SET "
                    + "     date_st = ?,  id_dept = ?,  id_empl = ?,  doc= ?,  "
                    + "     note = ?, id_empl_ins = ?,  status = ?  "
                    + " Where id = ?";
            ps = conn.prepareStatement(sql);
            ps.setDate(1, new java.sql.Date(stDate));
            ps.setObject(2, idDept);
            ps.setObject(3, idEmpl);
            ps.setString(4, doc.trim());
            ps.setString(5, note.trim());
            ps.setInt(6, idUser);
            ps.setInt(7, status);
            ps.setInt(8, idSt);
            ps.execute();

            sql = "Delete From sklad_hud_storage_item Where id_storage = ?";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, idSt);
            ps.execute();

            sql = "Insert into sklad_hud_storage_item (id_storage, id_prod, fas_s, pname_s, id_empl_vvod, id_empl_ins) "
                    + " values( ?, ?, ?, ?, ?, ?)";
            for (Iterator it = dataItem.iterator(); it.hasNext(); ) {
                Vector vec = (Vector) it.next();

                ps = conn.prepareStatement(sql);
                ps.setInt(1, idSt);
                ps.setInt(2, Integer.valueOf(vec.get(1).toString()));
                ps.setInt(3, vec.get(21).toString().trim().equals("") ? -1 : Integer.valueOf(vec.get(21).toString()));
                ps.setString(4, vec.get(21).toString().trim().equals("") ? "" : vec.get(22).toString().trim().toUpperCase());
                ps.setInt(5, idUser);
                ps.setInt(6, idUser);
                ps.execute();
            }

            commit();

            rezalt = true;
        } catch (Exception e) {
            rezalt = false;
            rollBack();
            System.err.println("Ошибка editSkladStorage() " + e);
            log.error("Ошибка editSkladStorage()", e);
            throw new Exception("Ошибка editSkladStorage() " + e.getMessage(), e);
        } finally {
            setAutoCommit(true);
        }
        return rezalt;
    }
}
