package dept.production.planning.ean;

import by.march8.ecs.framework.common.LogCrutch;
import workDB.PDB_new;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Vector;

/**
 * Класс для работы с БД
 *
 * @author lidashka
 */
public class EanPDB extends PDB_new {
    private static final LogCrutch log = new LogCrutch();
    EanDB edb = new EanDB();

    /**
     * Возвращает все заявки удовлетворяющие условиям.
     * @param flagVod
     * @param sDate
     * @param eDate
     * @param flagIns
     * @param sInsDate
     * @param eInsDate
     * @param status
     * @return
     * @throws Exception
     */
    public Vector getAllEanList(String fas,
                                String nar,
                                boolean flagVod,
                                long sDate,
                                long eDate,
                                boolean flagIns,
                                long sInsDate,
                                long eInsDate,
                                String status) throws Exception {

        Vector elements = new Vector();
        String sql = "";

        try {
            sql = "Select distinct eanlist.id as idEan, ean_date, ean_num, ean_name, eanlist.id_empl_vvod as id_empl_vvod, fio, eanlist.date_ins as date_ins, status, datamark_export "
                    + "             From eanlist, eanlist_item, employees "
                    + "             Where eanlist.id = eanlist_item.id_eanlist and "
                    + "                   eanlist.id_empl_vvod = employees.id and "
                    + "                   eanlist_item.fas::text like ? and "
                    + "                   eanlist_item.nar::text like ? and "
                    + "                   eanlist.status::text like ? "
                    + (flagVod ? " and eanlist.ean_date between '" + new java.sql.Date(sDate) + " 00:00' "
                    + "                                                    and '" + new java.sql.Date(eDate) + " 23:59' " : " ")
                    + (flagIns ? " and eanlist.date_ins between '" + new java.sql.Date(sInsDate) + " 00:00' "
                    + "                                                    and '" + new java.sql.Date(eInsDate) + " 23:59' " : " ")
                    + "  Order by idEan ";


            ps = conn.prepareStatement(sql);
            ps.setString(1, fas + "%");
            ps.setString(2, nar + "%");
            ps.setString(3, status + "%");
            rs = ps.executeQuery();
            while (rs.next()) {
                Vector tmp = new Vector();
                tmp.add(false);
                tmp.add(rs.getInt("idEan"));
                tmp.add(rs.getInt("ean_num"));
                tmp.add(rs.getString("ean_name"));
                tmp.add(new SimpleDateFormat("dd-MM-yyyy").format(rs.getDate("ean_date")));
                tmp.add(rs.getString("fio"));
                tmp.add(new SimpleDateFormat("dd-MM-yyyy").format(rs.getDate("date_ins")));

                switch (rs.getInt("status")) {
                    case 0:
                        tmp.add("отправлен");
                        break;
                    case 1:
                        tmp.add("формируется");
                        break;
                    case 2:
                        tmp.add("ответ");
                        break;
                    case 3:
                        tmp.add("импорт");
                        break;
                    case 4:
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
                tmp.add(rs.getString("datamark_export"));
                elements.add(tmp);
            }
        } catch (SQLException e) {
            elements = new Vector();
            System.err.println("Ошибка getAllEanList() " + e);
            log.error("Ошибка getAllEanList()", e);
            throw new Exception("Ошибка getAllEanList() " + e.getMessage(), e);
        }
        return elements;
    }

    /**
     * Возвращает по заданным условиям модели из проекта плана производства.
     * @param fasNum - модель
     * @param planNum - номер проекта плана производства
     * @param fasNew - новинка
     * @return
     * @throws Exception
     */
    public Vector getAllModelsProject(String fasNum,
                                      int planNum,
                                      boolean fasNew) throws Exception {
        Vector elements = new Vector();
        String sql = "Select idPlan, fasNum, fas_pname, fas_vid, kol_x, idNewFas "
                + " From (Select idPlan, fasNum, fas_pname, fas_vid, kol_x, plan_fas_new.id as idNewFas "
                + "	From (Select distinct plan.id as idPlan, plan_item.fas as fasNum, fas_pname, fas_vid, kol_x "
                + "		From plan, plan_item "
                + "		Where plan.id = plan_item.id_plan and "
                + "		      (plan.status = 1 or plan.status = 3) and  "
                + "		      plan.id = ? and "
                + "		      plan_item.fas::text like ?  ) as t1 "
                + "	left join plan_fas_new  "
                + "	on idPlan = plan_fas_new.id_plan and "
                + "	   fasNum = plan_fas_new.fas) as t2 "
                + (fasNew ? " Where idNewFas > 0 " : " ")
                + " Order by idPlan, fasNum, fas_pname";

        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, planNum);
            ps.setString(2, fasNum + "%");
            rs = ps.executeQuery();
            while (rs.next()) {
                Vector tmp = new Vector();
                tmp.add(rs.getInt("idPlan"));
                tmp.add(rs.getInt("fasNum"));
                tmp.add(rs.getString("fas_pname").trim().toLowerCase());
                tmp.add(rs.getInt("fas_vid"));
                tmp.add(rs.getInt("kol_x"));
                if (rs.getObject("idNewFas") != null) tmp.add(UtilEan.NEW);
                else tmp.add("");
                elements.add(tmp);
            }

        } catch (SQLException e) {
            System.err.println("Ошибка getAllModels() " + e);
            log.error("Ошибка getAllModels()", e);
            throw new Exception("Ошибка getAllModels() " + e.getMessage(), e);
        }
        return elements;
    }

    /**
     * Возвращает по заданным условиям модели из плана производства.
     * По шифрам информация о модели берется из классификатора (nsi_kld, nsi_sd).
     * @param fasNum
     * @param planNum
     * @return
     * @throws Exception
     */
    public Vector getAllModelsPlan(String fasNum,
                                   int planNum) throws Exception {

        Vector elements = new Vector();
        EanDB db = null;
        String sql = "Select distinct plan.id as idPlan, plan_item.fas as fasNum, fas_pname, kol_x, sar, nar  "
                + "		From plan, plan_item "
                + "		Where plan.id = plan_item.id_plan and "
                + "		      plan.id = ? and "
                + "		      plan_item.fas::text like ? "
                + " Order by fasNum, fas_pname";

        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, planNum);
            ps.setString(2, fasNum + "%");
            rs = ps.executeQuery();

            db = new EanDB();

            while (rs.next()) {
                Vector tmpFas = db.getDetalFas(rs.getInt("fasNum"), rs.getInt("sar"));

                Vector tmp = new Vector();
                tmp.add(rs.getInt("fasNum"));
                tmp.add(rs.getInt("sar"));
                tmp.add(tmpFas.size() > 0 ? tmpFas.elementAt(1).toString().trim().toUpperCase() : "");
                tmp.add(tmpFas.size() > 0 ? tmpFas.elementAt(3).toString().trim().toUpperCase() : "");
                tmp.add(tmpFas.size() > 0 ? tmpFas.elementAt(4).toString().trim() : "");
                elements.add(tmp);
            }

        } catch (Exception e) {
            System.err.println("Ошибка getAllModelsPlan() " + e);
            log.error("Ошибка getAllModelsPlan()", e);
            throw new Exception("Ошибка getAllModelsPlan() " + e.getMessage(), e);
        } finally {
            db.disConn();
        }
        return elements;
    }

    /**
     * Возвращает все коды GPC сегмента
     * @return
     * @throws Exception
     */
    public Vector getGPCSeg() throws Exception {
        Vector vec = new Vector();
        String sql = "";
        try {
            sql = "Select distinct gpc_seg From eanlist_item Order by gpc_seg";

            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                Vector tmp = new Vector();
                tmp.add(rs.getString("gpc_seg"));
                vec.add(tmp);
            }
        } catch (SQLException e) {
            System.err.println("Ошибка getGPCSeg() " + e);
            log.error("Ошибка getGPCSeg()", e);
            throw new Exception("Ошибка getGPCSeg() " + e.getMessage(), e);
        }
        return vec;
    }

    /**
     * Возвращает все коды GPC семейства
     * @return
     * @throws Exception
     */
    public Vector getGPCSem() throws Exception {
        Vector vec = new Vector();
        String sql = "";
        try {
            sql = "Select distinct gpc_sem from eanlist_item Order by gpc_sem";

            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                Vector tmp = new Vector();
                tmp.add(rs.getString("gpc_sem"));
                vec.add(tmp);
            }
        } catch (SQLException e) {
            System.err.println("Ошибка getGPCSem() " + e);
            log.error("Ошибка getGPCSem()", e);
            throw new Exception("Ошибка getGPCSem() " + e.getMessage(), e);
        }
        return vec;
    }

    /**
     * Возвращает все коды GPC класса
     * @return
     * @throws Exception
     */
    public Vector getGPCKl() throws Exception {
        Vector vec = new Vector();
        String sql = "";
        try {
            sql = "Select distinct gpc_kl from eanlist_item Order by gpc_kl";

            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                Vector tmp = new Vector();
                tmp.add(rs.getString("gpc_kl"));
                vec.add(tmp);
            }
        } catch (SQLException e) {
            System.err.println("Ошибка getGPCKl() " + e);
            log.error("Ошибка getGPCKl()", e);
            throw new Exception("Ошибка getGPCKl() " + e.getMessage(), e);
        }
        return vec;
    }

    /**
     * Возвращает все коды GPC брика
     * @return
     * @throws Exception
     */
    public Vector getGPCBr() throws Exception {
        Vector vec = new Vector();
        String sql = "";
        try {
            sql = "Select distinct gpc_br from eanlist_item Order by gpc_br";

            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                Vector tmp = new Vector();
                tmp.add(rs.getString("gpc_br"));
                vec.add(tmp);
            }
        } catch (SQLException e) {
            System.err.println("Ошибка getGPCBr() " + e);
            log.error("Ошибка getGPCBr()", e);
            throw new Exception("Ошибка getGPCBr() " + e.getMessage(), e);
        }
        return vec;
    }

    /**
     * Возвращает все коды ОКРБ-007
     * @return
     * @throws Exception
     */
    public Vector getKodOkrb() throws Exception {
        Vector vec = new Vector();
        String sql = "";
        try {
            sql = "Select distinct okrb from eanlist_item Order by okrb";

            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                Vector tmp = new Vector();
                tmp.add(rs.getString("okrb"));
                vec.add(tmp);
            }
        } catch (SQLException e) {
            System.err.println("Ошибка getKodOkrb() " + e);
            log.error("Ошибка getKodOkrb()", e);
            throw new Exception("Ошибка getKodOkrb() " + e.getMessage(), e);
        }
        return vec;
    }

    /**
     * Возвращает все коды THB
     * @return
     * @throws Exception
     */
    @Deprecated
    public Vector getKodThb() throws Exception {
        Vector vec = new Vector();
        String sql = "";
        try {
            sql = "Select distinct tnb from eanlist_item Order by tnb";

            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                Vector tmp = new Vector();
                tmp.add(rs.getString("tnb"));
                vec.add(tmp);
            }
        } catch (SQLException e) {
            System.err.println("Ошибка getKodThb() " + e);
            log.error("Ошибка getKodThb()", e);
            throw new Exception("Ошибка getKodThb() " + e.getMessage(), e);
        }
        return vec;
    }

    /**
     * Возвращает все ГОСТы
     * @return
     * @throws Exception
     */
    public Vector getKodGost() throws Exception {
        Vector vec = new Vector();
        String sql = "";
        try {
            sql = "Select distinct gost from eanlist_item Order by gost";

            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                Vector tmp = new Vector();
                tmp.add(rs.getString("gost"));
                vec.add(tmp);
            }
        } catch (SQLException e) {
            System.err.println("Ошибка getKodGost() " + e);
            log.error("Ошибка getKodGost()", e);
            throw new Exception("Ошибка getKodGost() " + e.getMessage(), e);
        }
        return vec;
    }


    /**
     * Возвращает все шаблоны кодов классификации для заданный модели.
     * @param fas
     * @param pname
     * @return
     * @throws Exception
     */
    public Vector getKodTemplates(String fas, String pname, long date) throws Exception {
        Vector vec = new Vector();
        String sql = "";
        try {
            sql = "Select distinct fas, "
                    + "             pname, "
                    + "             gpc_seg, "
                    + "             gpc_sem, "
                    + "             gpc_kl,"
                    + "             gpc_br, "
                    + "             okrb, "
                    + "             tnb, "
                    + "             gost "
                    + " From eanlist_item "
                    + " Where fas::text like ?  and "
                    + "       pname::text like ? and "
                    + "       eanlist_item.date_vvod > ?  "
                    + " Order by fas, pname, gpc_seg, gpc_sem, gpc_kl, gpc_br, okrb, tnb, gost ";

            ps = conn.prepareStatement(sql);
            ps.setString(1, fas + "%");
            ps.setString(2, "%" + pname.toUpperCase().trim() + "%");
            ps.setDate(3, new java.sql.Date(date));
            rs = ps.executeQuery();

            while (rs.next()) {
                Vector tmp = new Vector();
                tmp.add(rs.getInt("fas"));
                tmp.add(rs.getString("pname"));
                tmp.add(rs.getString("gpc_seg"));
                tmp.add(rs.getString("gpc_sem"));
                tmp.add(rs.getString("gpc_kl"));
                tmp.add(rs.getString("gpc_br"));
                tmp.add(rs.getString("okrb"));
                tmp.add(rs.getString("tnb"));
                tmp.add(rs.getString("gost"));
                vec.add(tmp);
            }
        } catch (Exception e) {
            System.err.println("Ошибка getKodDefault() " + e);
            log.error("Ошибка getKodDefault()", e);
            throw new Exception("Ошибка getKodDefault() " + e.getMessage(), e);
        }
        return vec;
    }

    /**
     * Добавляет заявку на получение ean-кодов в бд
     * @param numEanlist
     * @param nameEanlist
     * @param dataEanlist
     * @param status
     * @param noteEanlist
     * @param idEmpl
     * @param data
     * @return
     * @throws Exception
     */
    public boolean addEanlist(int numEanlist,
                              String nameEanlist,
                              long dataEanlist,
                              int status,
                              String noteEanlist,
                              int idEmpl,
                              ArrayList<EanItem> data) throws Exception {

        boolean rez = false;
        String sql;

        try {
            setAutoCommit(false);

            sql = "Insert into eanlist( ean_num, ean_name, ean_date, note, status, id_empl_vvod, id_empl_ins) "
                    + " values( ?, ?, ?, ?, ?, ?, ?) returning id";

            ps = conn.prepareStatement(sql);
            ps.setInt(1, numEanlist);
            ps.setString(2, nameEanlist);
            ps.setDate(3, new java.sql.Date(dataEanlist));
            ps.setString(4, noteEanlist);
            ps.setInt(5, status);
            ps.setInt(6, idEmpl);
            ps.setInt(7, idEmpl);
            rs = ps.executeQuery();

            if (rs.next()) {
                int eanId = rs.getInt(1);

                for (EanItem eanItem : data) {
                    if (eanItem.getFlag() != -1) {
                        sql = "Insert into eanlist_item(id_eanlist, sar, nar, fas, pname, srt,"
                                + " fas_vid, kol_x, gpc_seg, gpc_sem, gpc_kl, gpc_br, "
                                + " okrb, tnb, gost, id_empl_vvod, id_empl_ins, upac, note, id_color, color) "
                                + " values( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) returning id ";
                        EanDB eanDB = new EanDB();
                        Integer sar = eanDB.getSarByFasAndNAr(eanItem.getFasNum(), String.valueOf(eanItem.getFasNar()));
                        ps = conn.prepareStatement(sql);
                        ps.setInt(1, eanId);
                        ps.setInt(2, Objects.nonNull(sar) ? sar : 0);
                        ps.setString(3, eanItem.getFasNar());
                        ps.setInt(4, eanItem.getFasNum());
                        ps.setString(5, eanItem.getFasName());
                        ps.setInt(6, eanItem.getFasSrt());
                        ps.setInt(7, eanItem.getFasVid());
                        ps.setInt(8, eanItem.getKolX());
                        ps.setInt(9, eanItem.getTextGpcSeg());
                        ps.setInt(10, eanItem.getTextGpcSem());
                        ps.setInt(11, eanItem.getTextGpcKl());
                        ps.setInt(12, eanItem.getTextGpcBr());
                        ps.setString(13, eanItem.getTextOKRB());
                        ps.setString(14, eanItem.getTextTHB());
                        ps.setString(15, eanItem.getTextGOST());
                        ps.setInt(16, idEmpl);
                        ps.setInt(17, idEmpl);
                        ps.setString(18, eanItem.getUpacText());
                        ps.setString(19, eanItem.getNoteText());
                        ps.setInt(20, eanItem.getColorNum());
                        ps.setString(21, eanItem.getColorName());
                        rs = ps.executeQuery();

                        if (rs.next()) {
                            sql = "Insert into eanlist_size (id_eanlist_item, rst, rzm, size_print, ean13)"
                                    + " values( ?, ?, ?, ?, ?)";

                            for (EanItemListSize size : eanItem.getDataSize()) {
                                ps = conn.prepareStatement(sql);
                                ps.setInt(1, rs.getInt(1));
                                ps.setInt(2, size.getRst());
                                ps.setInt(3, size.getRzm());
                                ps.setString(4, size.getSizePrint());
                                ps.setString(5, size.getEan13() == null ? "" : size.getEan13());
                                ps.execute();
                            }
                        }
                    }
                }

                commit();
                rez = true;
            }
        } catch (SQLException e) {
            rez = false;
            rollBack();
            System.err.println("Ошибка addEanlist() " + e);
            log.error("Ошибка addEanlist()", e);
            throw new Exception("Ошибка addEanlist() " + e.getMessage(), e);
        } finally {
            setAutoCommit(true);
        }
        return rez;
    }

    /**
     * Редактирует заявку на получение ean-кодов в бд
     * @param idEanlist
     * @param numEanlist
     * @param nameEanlist
     * @param dataEanlist
     * @param status
     * @param noteEanlist
     * @param idEmpl
     * @param data
     * @return
     * @throws Exception
     */
    public boolean editEanlist(int idEanlist, int numEanlist,
                               String nameEanlist,
                               long dataEanlist,
                               int status,
                               String noteEanlist,
                               int idEmpl,
                               ArrayList<EanItem> data) throws Exception {

        boolean rez = false;
        String sql;

        try {
            setAutoCommit(false);

            sql = "Update eanlist set ean_num = ?, "
                    + "             ean_name = ?, "
                    + "             ean_date = ?, "
                    + "             note = ?, "
                    + "             status = ?,"
                    + "             id_empl_ins = ?, "
                    + "             date_ins = now() "
                    + " Where eanlist.id = ? ";

            ps = conn.prepareStatement(sql);
            ps.setInt(1, numEanlist);
            ps.setString(2, nameEanlist);
            ps.setDate(3, new java.sql.Date(dataEanlist));
            ps.setString(4, noteEanlist);
            ps.setInt(5, status);
            ps.setInt(6, idEmpl);
            ps.setInt(7, idEanlist);
            ps.execute();

            for (EanItem eanItem : data) {
                if (eanItem.getFlag() == 0) {
                    sql = "Insert into eanlist_item(id_eanlist, sar, nar, fas, pname, srt,"
                            + " fas_vid, kol_x, gpc_seg, gpc_sem, gpc_kl, gpc_br, "
                            + " okrb, tnb, gost, id_empl_vvod, id_empl_ins, upac, note, id_color, color) "
                            + " values( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) returning id ";

                    Vector sar = edb.getAllModels(String.valueOf(eanItem.getFasNum()));
                    int shifrArt = 0;
                    if (sar.size() > 0) {
                        shifrArt = ((Integer) ((Vector) sar.get(0)).get(1)).intValue();
                    } else {
                        shifrArt = 0;
                    }

                    ps = conn.prepareStatement(sql);
                    ps.setInt(1, idEanlist);
                    ps.setInt(2, shifrArt);
                    ps.setString(3, eanItem.getFasNar());
                    ps.setInt(4, eanItem.getFasNum());
                    ps.setString(5, eanItem.getFasName());
                    ps.setInt(6, eanItem.getFasSrt());
                    ps.setInt(7, eanItem.getFasVid());
                    ps.setInt(8, eanItem.getKolX());
                    ps.setInt(9, eanItem.getTextGpcSeg());
                    ps.setInt(10, eanItem.getTextGpcSem());
                    ps.setInt(11, eanItem.getTextGpcKl());
                    ps.setInt(12, eanItem.getTextGpcBr());
                    ps.setString(13, eanItem.getTextOKRB());
                    ps.setString(14, eanItem.getTextTHB());
                    ps.setString(15, eanItem.getTextGOST());
                    ps.setInt(16, idEmpl);
                    ps.setInt(17, idEmpl);
                    ps.setString(18, eanItem.getUpacText());
                    ps.setString(19, eanItem.getNoteText());
                    ps.setInt(20, eanItem.getColorNum());
                    ps.setString(21, eanItem.getColorName());
                    rs = ps.executeQuery();

                    if (rs.next()) {
                        sql = "Insert into eanlist_size (id_eanlist_item, rst, rzm, size_print, ean13)"
                                + " values( ?, ?, ?, ?, ?)";

                        for (EanItemListSize size : eanItem.getDataSize()) {
                            ps = conn.prepareStatement(sql);
                            ps.setInt(1, rs.getInt(1));
                            ps.setInt(2, size.getRst());
                            ps.setInt(3, size.getRzm());
                            ps.setString(4, size.getSizePrint());
                            ps.setString(5, size.getEan13() == null ? "" : size.getEan13());
                            ps.execute();
                        }
                    }

                } else if (eanItem.getFlag() == 1) {

                    sql = " Update eanlist_item set "
                            + "                 sar = ?, "
                            + "                 nar = ?, "
                            + "                 fas = ?, "
                            + "                 pname = ?, "
                            + "                 srt = ?, "
                            + "                 fas_vid = ?, "
                            + "                 kol_x = ?, "
                            + "                 gpc_seg = ?, "
                            + "                 gpc_sem = ?, "
                            + "                 gpc_kl = ?, "
                            + "                 gpc_br = ?, "
                            + "                 okrb = ?, "
                            + "                 tnb = ?, "
                            + "                 gost = ?, "
                            + "                 id_empl_ins = ?, "
                            + "                 date_ins = now(), "
                            + "                 upac = ?, "
                            + "                 note = ?,"
                            + "                 id_color = ?,"
                            + "                 color = ? "
                            + " Where eanlist_item.id_eanlist = ? and "
                            + "       eanlist_item.id = ?   ";

                    ps = conn.prepareStatement(sql);
                    EanDB eanDB = new EanDB();
                    Integer sar = eanDB.getSarByFasAndNAr(eanItem.getFasNum(), String.valueOf(eanItem.getFasNar()));
                    ps.setInt(1, sar);
                    ps.setString(2, eanItem.getFasNar());
                    ps.setInt(3, eanItem.getFasNum());
                    ps.setString(4, eanItem.getFasName());
                    ps.setInt(5, eanItem.getFasSrt());
                    ps.setInt(6, eanItem.getFasVid());
                    ps.setInt(7, eanItem.getKolX());
                    ps.setInt(8, eanItem.getTextGpcSeg());
                    ps.setInt(9, eanItem.getTextGpcSem());
                    ps.setInt(10, eanItem.getTextGpcKl());
                    ps.setInt(11, eanItem.getTextGpcBr());
                    ps.setString(12, eanItem.getTextOKRB());
                    ps.setString(13, eanItem.getTextTHB());
                    ps.setString(14, eanItem.getTextGOST());
                    ps.setInt(15, idEmpl);
                    ps.setString(16, eanItem.getUpacText());
                    ps.setString(17, eanItem.getNoteText());
                    ps.setInt(18, eanItem.getColorNum());
                    ps.setString(19, eanItem.getColorName());
                    ps.setInt(20, idEanlist);
                    ps.setInt(21, eanItem.getId());
                    ps.execute();

                    sql = "Delete From eanlist_size Where eanlist_size.id_eanlist_item = ?";
                    ps = conn.prepareStatement(sql);
                    ps.setInt(1, eanItem.getId());
                    ps.execute();

                    sql = "Insert into eanlist_size (id_eanlist_item, rst, rzm, size_print, ean13)"
                            + " values( ?, ?, ?, ?, ?)";

                    for (EanItemListSize size : eanItem.getDataSize()) {
                        ps = conn.prepareStatement(sql);
                        ps.setInt(1, eanItem.getId());
                        ps.setInt(2, size.getRst());
                        ps.setInt(3, size.getRzm());
                        ps.setString(4, size.getSizePrint());
                        ps.setString(5, size.getEan13() == null ? "" : size.getEan13());
                        ps.execute();
                    }

                } else if (eanItem.getFlag() == -1 && eanItem.getId() > 0) {

                    sql = "Delete From eanlist_size "
                            + " Where eanlist_size.id in (Select eanlist_size.id "
                            + "				From eanlist_size, eanlist_item "
                            + "				Where eanlist_item.id_eanlist = ? and "
                            + "                               eanlist_item.id = ? and "
                            + "				      eanlist_item.id = eanlist_size.id_eanlist_item)";

                    ps = conn.prepareStatement(sql);
                    ps.setInt(1, idEanlist);
                    ps.setInt(2, eanItem.getId());
                    ps.execute();

                    sql = "Delete From eanlist_item "
                            + " Where eanlist_item.id_eanlist = ? and "
                            + "       eanlist_item.id = ? ";

                    ps = conn.prepareStatement(sql);
                    ps.setInt(1, idEanlist);
                    ps.setInt(2, eanItem.getId());
                    ps.execute();
                }
            }
            commit();

            rez = true;
        } catch (SQLException e) {
            rez = false;
            rollBack();
            System.err.println("Ошибка addEanlist() " + e);
            log.error("Ошибка addEanlist()", e);
            throw new Exception("Ошибка addEanlist() " + e.getMessage(), e);
        } finally {
            setAutoCommit(true);
        }
        return rez;
    }

    /**
     * Возвращает все данные заявки по id
     * @param idEanlist
     * @return
     * @throws Exception
     */
    public EanList getDataEanlist(int idEanlist) throws Exception {
        EanList elements = new EanList();
        ArrayList<EanItem> items = new ArrayList<>();
        EanItem eanItem;
        ArrayList<EanItemListSize> itemsListSizes;
        EanItemListSize eanItemListSize;

        String sql = " "
                + " SELECT "
                + "         eanlist.id as idEanlist, "
                + "         ean_num, "
                + "         ean_name, "
                + "         ean_date, "
                + "         note, "
                + "         status, "
                + "         id_empl_vvod, "
                + "         date_vvod, "
                + "         id_empl_ins, "
                + "         date_ins, "
                + "         EMP1.fio as vvodFio, "
                + "         EMP2.fio as insFio, "
                + "         datamark_export "
                + " FROM "
                + "         eanlist, "
                + "         employees as EMP1, "
                + "         employees as EMP2 "
                + " WHERE "
                + "         eanlist.id_empl_vvod = EMP1.id and "
                + "         eanlist.id_empl_ins = EMP2.id and "
                + "         eanlist.id = ?  ";

        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, idEanlist);
            rs = ps.executeQuery();
            if (rs.next()) {
                elements.setIdEanlist(rs.getInt("idEanlist"));
                elements.setEanNum(rs.getInt("ean_num"));
                elements.setEanName(rs.getString("ean_name"));
                elements.setEanDate(new SimpleDateFormat("dd-MM-yyyy").format(rs.getDate("ean_date")));
                elements.setIdEmplVvod(rs.getInt("id_empl_vvod"));
                elements.setVvodFio(rs.getString("vvodFio"));
                elements.setDateVvod(new SimpleDateFormat("dd-MM-yyyy").format(rs.getDate("date_vvod")));
                elements.setIdEmplIns(rs.getInt("id_empl_ins"));
                elements.setInsFio(rs.getString("insFio"));
                elements.setInsDate(new SimpleDateFormat("dd-MM-yyyy").format(rs.getDate("date_ins")));
                elements.setNote(rs.getString("note"));

                switch (rs.getInt("status")) {
                    case 0:
                        elements.setStatus("отправлен");
                        break;
                    case 1:
                        elements.setStatus("формируется");
                        break;
                    case 2:
                        elements.setStatus("ответ");
                        break;
                    case 3:
                        elements.setStatus("импорт");
                        break;
                    case 4:
                        elements.setStatus("закрыт");
                        break;
                    case -1:
                        elements.setStatus("удалён");
                        break;
                    default:
                        elements.setStatus("неизвестно");
                        break;
                }
                elements.setIdStatus(rs.getInt("status"));

                sql = " "
                        + " SELECT  "
                        + "         eanlist_item.id as idItem, "
                        + "         sar, "
                        + "         nar, "
                        + "         fas, "
                        + "         pname, "
                        + "         srt, "
                        + "         fas_vid, "
                        + "         kol_x, "
                        + "         gpc_seg, "
                        + "         gpc_sem, "
                        + "         gpc_kl, "
                        + "         gpc_br, "
                        + "         okrb, "
                        + "         tnb, "
                        + "         gost, "
                        + "         upac, "
                        + "         eanlist_item.note as note,"
                        + "         id_color,"
                        + "         color "
                        + " FROM "
                        + "         eanlist, eanlist_item"
                        + " WHERE "
                        + "         eanlist.id = eanlist_item.id_eanlist and "
                        + "         eanlist.id = ?  "
                        + " ORDER BY "
                        + "         fas, sar, nar, color, srt, upac ";

                ps = conn.prepareStatement(sql);
                ps.setInt(1, idEanlist);
                rs = ps.executeQuery();

                while (rs.next()) {
                    eanItem = new EanItem();
                    eanItem.setIdFlag(1);
                    eanItem.setFasSar(rs.getInt("sar"));
                    eanItem.setId(rs.getInt("idItem"));
                    eanItem.setFasNum(rs.getInt("fas"));
                    eanItem.setFasName(rs.getString("pname"));
                    eanItem.setFasVid(rs.getInt("fas_vid"));
                    eanItem.setFasSrt(rs.getInt("srt"));
                    eanItem.setKolX(rs.getInt("kol_x"));
                    eanItem.setFasNar(rs.getString("nar"));
                    eanItem.setNoteText(rs.getString("note"));
                    eanItem.setUpacText(rs.getString("upac"));
                    eanItem.setTextGpcSeg(rs.getInt("gpc_seg"));
                    eanItem.setTextGpcSem(rs.getInt("gpc_sem"));
                    eanItem.setTextGpcKl(rs.getInt("gpc_kl"));
                    eanItem.setTextGpcBr(rs.getInt("gpc_br"));
                    eanItem.setTextOKRB(rs.getString("okrb"));
                    eanItem.setTextTHB(rs.getString("tnb"));
                    eanItem.setTextGOST(rs.getString("gost"));
                    eanItem.setColorNum(rs.getInt("id_color"));
                    eanItem.setColorName(rs.getString("color"));

                    itemsListSizes = new ArrayList<>();

                    sql = " "
                            + " SELECT "
                            + "     eanlist_size.id as idSize, "
                            + "     id_eanlist_item, "
                            + "     rst, "
                            + "     rzm, "
                            + "     size_print, "
                            + "     ean13 "
                            + " FROM "
                            + "     eanlist_item, "
                            + "     eanlist_size "
                            + " WHERE "
                            + "     eanlist_size.id_eanlist_item = eanlist_item.id and "
                            + "     eanlist_size.id_eanlist_item = ?  "
                            + "ORDER BY "
                            + "     rst, rzm, size_print  ";

                    ps = conn.prepareStatement(sql);
                    ps.setInt(1, rs.getInt("idItem"));
                    ResultSet rs_ = ps.executeQuery();
                    EanDB eanDB = new EanDB();

                    while (rs_.next()) {
                        eanItemListSize = new EanItemListSize();
                        eanItemListSize.setIdEanItem(idEanlist);
                        eanItemListSize.setId(rs_.getInt("idSize"));
                        eanItemListSize.setRst(rs_.getInt("rst"));
                        eanItemListSize.setRzm(rs_.getInt("rzm"));
                        eanItemListSize.setSizePrint(rs_.getString("size_print"));
                        eanItemListSize.setEan13(rs_.getObject("ean13") != null ? rs_.getString("ean13") : "");

                        eanItemListSize = eanDB.getWeight(eanItem, eanItemListSize);
                        //System.out.println(eanItemListSize.getWeight());

                        itemsListSizes.add(eanItemListSize);
                    }

                    eanItem.setDataSize(itemsListSizes);
                    items.add(eanItem);
                }
                elements.setEanItems(items);
            }
        } catch (SQLException e) {
            elements = new EanList();
            System.err.println("Ошибка getDataEanlist() " + e);
            log.error("Ошибка getDataEanlist()", e);
            throw new Exception("Ошибка getDataEanlist() " + e.getMessage(), e);
        }
        return elements;
    }

    public int addEan13ReportZayavka(int id, ArrayList<EanItem> eanItems) throws Exception {
        int flag = -1;
        String sql;

        try {
            setAutoCommit(false);

            for (EanItem item : eanItems) {
                for (EanItemListSize size : item.getDataSize()) {

                    sql = " SELECT "
                            + "     eanlist_size.id as idItem "
                            + " FROM "
                            + "     eanlist, "
                            + "     eanlist_item, "
                            + "     eanlist_size  "
                            + " WHERE "
                            + "     eanlist.id = eanlist_item.id_eanlist and "
                            + "     eanlist_item.id = eanlist_size.id_eanlist_item and "
                            + "     eanlist.id = ? and "
                            + "     nar like upper(?) and "
                            + "     fas = ? and "
                            + "     pname like upper(?) and "
                            + "     srt = ? and "
                            + "     size_print like ? and "
                            + "     color like upper(?) ";

                    ps = conn.prepareStatement(sql);
                    ps.setInt(1, id);
                    ps.setString(2, item.getFasNar());
                    ps.setInt(3, item.getFasNum());
                    ps.setString(4, item.getFasName());
                    ps.setInt(5, item.getFasSrt());
                    ps.setString(6, size.getSizePrint());
                    ps.setString(7, item.getColorName());
                    rs = ps.executeQuery();

                    if (rs.next()) {

                        sql = "Update eanlist_size "
                                + " Set ean13 = ? "
                                + " Where eanlist_size.id = ? ";

                        ps = conn.prepareStatement(sql);
                        ps.setString(1, size.getEan13());
                        ps.setInt(2, rs.getInt("idItem"));
                        ps.execute();

                    } else {
                        flag = 0;
                    }
                }
            }

            sql = "Update eanlist "
                    + " Set status = 2 "
                    + " Where eanlist.id = ? ";

            ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            ps.execute();

            if (flag != 0) {

                flag = 1;
            }

            commit();

        } catch (Exception e) {
            flag = -1;
            rollBack();
            System.err.println("Ошибка addEan13ReportZayavka() " + e);
            log.error("Ошибка addEan13ReportZayavka()", e);
            throw new Exception("Ошибка addEan13ReportZayavka() " + e.getMessage(), e);
        } finally {
            setAutoCommit(true);
        }

        return flag;
    }

    /**
     * Возвращает размерную сетку модели по номеру и шифру
     * из заданного плана производства.
     * @param idPlan
     * @param fas
     * @param sar
     * @return
     * @throws Exception
     */
    public Vector openRstRzm(int idPlan, int fas, int sar) throws Exception {
        Vector elements = new Vector();
        String sql;

        try {

            sql = "Select distinct rst, rzm "
                    + " From plan, plan_item "
                    + " Where plan.id = plan_item.id_plan and "
                    + "     plan.id = ? and "
                    + "     fas = ? and "
                    + "     sar = ? "
                    + "Order by rst, rzm ";

            ps = conn.prepareStatement(sql);
            ps.setInt(1, idPlan);
            ps.setInt(2, fas);
            ps.setInt(3, sar);
            rs = ps.executeQuery();

            while (rs.next()) {
                Vector tmp = new Vector();
                tmp.add(false);
                tmp.add(rs.getInt("rst"));
                tmp.add(rs.getInt("rzm"));
                tmp.add("");
                tmp.add("");
                tmp.add("");
                tmp.add("");
                tmp.add("");
                elements.add(tmp);
            }

        } catch (SQLException e) {
            System.err.println("Ошибка openRstRzm() " + e);
            log.error("Ошибка openRstRzm()", e);
            throw new Exception("Ошибка openRstRzm() " + e.getMessage(), e);
        }
        return elements;
    }

    /**
     * Возвращает размерную сетку модели по номеру и шифру
     * из заданного плана производства.
     * @param idPlan
     * @param fas
     * @param sar
     * @return
     * @throws Exception
     */
    public Vector openRstRzmInPlan(int idPlan, int fas, int sar) throws Exception {
        Vector elements = new Vector();
        String sql;

        try {

            sql = "Select distinct rst, rzm "
                    + " From plan, plan_item "
                    + " Where plan.id = plan_item.id_plan and "
                    + "     plan.id = ? and "
                    + "     fas = ? and "
                    + "     sar = ? "
                    + "Order by rst, rzm ";

            ps = conn.prepareStatement(sql);
            ps.setInt(1, idPlan);
            ps.setInt(2, fas);
            ps.setInt(3, sar);
            rs = ps.executeQuery();

            while (rs.next()) {
                Vector tmp = new Vector();
                tmp.add(rs.getInt("rst"));
                tmp.add(rs.getInt("rzm"));
                elements.add(tmp);
            }

        } catch (SQLException e) {
            System.err.println("Ошибка openRstRzm() " + e);
            log.error("Ошибка openRstRzm()", e);
            throw new Exception("Ошибка openRstRzm() " + e.getMessage(), e);
        }
        return elements;
    }

    /**
     * Возвращает размерную сетку модели, которая была ранее заказана по номеру, шифру и цвету.
     * @param idColor
     * @param nar
     * @param fas
     * @param srt
     * @return
     * @throws Exception
     */
    public Vector openRstRzmInOrder(int idColor, String nar, int fas, int srt) throws Exception {
        Vector elements = new Vector();
        String sql;

        try {

            sql = "    Select " +
                    "       rst, " +
                    "       rzm " +
                    "  From " +
                    "       eanlist, " +
                    "       eanlist_item, " +
                    "       eanlist_size " +
                    "  Where " +
                    "       eanlist.id = eanlist_item.id_eanlist and " +
                    "       eanlist.status <> -1 and  " +
                    "       eanlist_item.id = eanlist_size.id_eanlist_item and " +
                    "       eanlist_item.nar = ? and " +
                    "       eanlist_item.fas = ? and " +
                    "       eanlist_item.srt = ? and " +
                    "       eanlist_item.id_color = ? ";

            ps = conn.prepareStatement(sql);
            ps.setString(1, nar);
            ps.setInt(2, fas);
            ps.setInt(3, srt);
            ps.setInt(4, idColor);
            rs = ps.executeQuery();

            while (rs.next()) {
                Vector tmp = new Vector();
                tmp.add(rs.getInt("rst"));
                tmp.add(rs.getInt("rzm"));
                elements.add(tmp);
            }

        } catch (SQLException e) {
            System.err.println("Ошибка openRstRzmInOrder() " + e);
            log.error("Ошибка openRstRzmInOrder()", e);
            throw new Exception("Ошибка openRstRzmInOrder() " + e.getMessage(), e);
        }
        return elements;
    }

    public boolean setStatus(int idEanlist, int status) throws Exception {
        boolean rez = false;
        String sql;

        try {

            sql = "Update eanlist set status = ? Where eanlist.id = ? ";

            ps = conn.prepareStatement(sql);
            ps.setInt(1, status);
            ps.setInt(2, idEanlist);
            ps.execute();

            rez = true;
        } catch (SQLException e) {
            rez = false;
            System.err.println("Ошибка setStatus() " + e);
            log.error("Ошибка setStatus()", e);
            throw new Exception("Ошибка setStatus() " + e.getMessage(), e);
        }
        return rez;
    }

    public Vector getCheckEanlist(
            boolean flagFas,
            boolean flagNar) throws Exception {

        Vector elements = new Vector();
        String sql = "";

        try {
            sql = "Select distinct eanl_1.id as id1, eanl_1.ean_num as num1, eani_1.pname as pname1, "
                    + "     eani_1.fas as fas1, eani_1.nar as nar1,  "
                    + "	    eanl_2.id as id2, eanl_2.ean_num as num2, eani_2.pname as pname2, "
                    + "     eani_2.fas as fas2, eani_2.nar as nar2 "
                    + " From eanlist as eanl_1, eanlist_item as eani_1, "
                    + "      eanlist as eanl_2, eanlist_item as eani_2 "
                    + " Where eanl_1.id = eani_1.id_eanlist and "
                    + "       eanl_2.id = eani_2.id_eanlist and "
                    + "       eanl_1.status <> -1 and "
                    + "       eanl_2.status <> -1 and "
                    + "       eanl_1.id <> eanl_2.id and "
                    + "       eani_1.id <> eani_2.id  "
                    + (flagFas ? "  and eani_1.fas = eani_2.fas " : " ")
                    + (flagNar ? "  and eani_1.nar = eani_2.nar " : " ")
                    + " Order by fas1, nar1, id1";

            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                Vector tmp = new Vector();
                tmp.add(rs.getInt("id1"));
                tmp.add(rs.getInt("num1"));
                tmp.add(rs.getString("pname1"));
                tmp.add(rs.getInt("fas1"));
                tmp.add(rs.getString("nar1"));
                tmp.add("-");
                tmp.add("-");
                tmp.add("-");
                if (!elements.contains(tmp))
                    elements.add(tmp);
            }

        } catch (SQLException e) {
            System.err.println("Ошибка getCheckEanlist() " + e);
            log.error("Ошибка getCheckEanlist()", e);
            throw new Exception("Ошибка getCheckEanlist() " + e.getMessage(), e);
        }
        return elements;
    }

    public Vector getCheckEanlistFasSrt(
            boolean flagFas,
            boolean flagNar) throws Exception {

        Vector elements = new Vector();
        String sql;

        try {
            sql = "Select distinct eanl_1.id as id1, eanl_1.ean_num as num1, eani_1.pname as pname1, "
                    + "     eani_1.fas as fas1, eani_1.nar as nar1, eani_1.srt as srt1, "
                    + "	    eanl_2.id as id2, eanl_2.ean_num as num2, eani_2.pname as pname2, "
                    + "     eani_2.fas as fas2, eani_2.nar as nar2, eani_2.srt as srt2 "
                    + " From eanlist as eanl_1, eanlist_item as eani_1, "
                    + "      eanlist as eanl_2, eanlist_item as eani_2 "
                    + " Where eanl_1.id = eani_1.id_eanlist and "
                    + "       eanl_2.id = eani_2.id_eanlist and "
                    + "       eanl_1.status <> -1 and "
                    + "       eanl_2.status <> -1 and "
                    + "       eanl_1.id <> eanl_2.id and "
                    + "       eani_1.id <> eani_2.id  and "
                    + "       eani_1.srt = eani_2.srt "
                    + (flagFas ? "  and eani_1.fas = eani_2.fas " : " ")
                    + (flagNar ? "  and eani_1.nar = eani_2.nar " : " ")
                    + " Order by fas1, nar1, srt1, id1";

            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                Vector tmp = new Vector();
                tmp.add(rs.getInt("id1"));
                tmp.add(rs.getInt("num1"));
                tmp.add(rs.getString("pname1"));
                tmp.add(rs.getInt("fas1"));
                tmp.add(rs.getString("nar1"));
                tmp.add(rs.getInt("srt1"));
                tmp.add("-");
                tmp.add("-");
                if (!elements.contains(tmp))
                    elements.add(tmp);
            }

        } catch (SQLException e) {
            System.err.println("Ошибка getCheckEanlistFasNarSrt() " + e);
            log.error("Ошибка getCheckEanlistFasNarSrt()", e);
            throw new Exception("Ошибка getCheckEanlistFasNarSrt() " + e.getMessage(), e);
        }
        return elements;
    }

    public Vector getCheckEanlistFasRzm(
            boolean flagFas,
            boolean flagNar) throws Exception {

        Vector elements = new Vector();
        String sql;

        try {
            sql = "Select distinct eanl_1.id as id1, eanl_1.ean_num as num1, eani_1.pname as pname1, "
                    + "     eani_1.fas as fas1, eani_1.nar as nar1, eani_1.srt as srt1, eans_1.rst as rst1, eans_1.rzm as rzm1, "
                    + "	    eanl_2.id as id2, eanl_2.ean_num as num2, eani_2.pname as pname2, "
                    + "     eani_2.fas as fas2, eani_2.nar as nar2, eani_2.srt as srt2, eans_2.rst as rst2, eans_2.rzm as rzm2 "
                    + " From eanlist as eanl_1, eanlist_item as eani_1, eanlist_size as eans_1,"
                    + "      eanlist as eanl_2, eanlist_item as eani_2, eanlist_size as eans_2 "
                    + " Where eanl_1.id = eani_1.id_eanlist and "
                    + "       eanl_2.id = eani_2.id_eanlist and "
                    + "       eani_1.id = eans_1.id_eanlist_item and "
                    + "       eani_2.id = eans_2.id_eanlist_item and "
                    + "       eanl_1.status <> -1 and "
                    + "       eanl_2.status <> -1 and "
                    + "       eanl_1.id <> eanl_2.id and "
                    + "       eani_1.id <> eani_2.id  and "
                    + "       eani_1.srt = eani_2.srt and "
                    + "       eans_1.rst = eans_2.rst and "
                    + "       eans_1.rzm = eans_2.rzm  "
                    + (flagFas ? "  and eani_1.fas = eani_2.fas " : " ")
                    + (flagNar ? "  and eani_1.nar = eani_2.nar " : " ")
                    + " Order by fas1, nar1, srt1, rst1, rzm1, id1 ";

            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                Vector tmp = new Vector();
                tmp.add(rs.getInt("id1"));
                tmp.add(rs.getInt("num1"));
                tmp.add(rs.getString("pname1"));
                tmp.add(rs.getInt("fas1"));
                tmp.add(rs.getString("nar1"));
                tmp.add(rs.getInt("srt1"));
                tmp.add(rs.getInt("rst1"));
                tmp.add(rs.getInt("rzm1"));
                if (!elements.contains(tmp))
                    elements.add(tmp);
            }

        } catch (SQLException e) {
            System.err.println("Ошибка getCheckEanlistFasRzm() " + e);
            log.error("Ошибка getCheckEanlistFasRzm()", e);
            throw new Exception("Ошибка getCheckEanlistFasRzm() " + e.getMessage(), e);
        }
        return elements;
    }

    /**
     * Вектор данных полученных из getAllModelsInMarshList() дополняет сведениями
     * из заявок на получение EAN-кодов.
     * @param rows
     * @return
     * @throws Exception
     */
    public Vector getOrderEANAndColor(Vector rows) throws Exception {
        Vector elements = new Vector();
        Vector model;
        Vector tmp;
        Vector tmp1;
        String sql;
        String str;

        try {
            sql = "     SELECT DISTINCT "
                    + "     eanlist.id as id, "
                    + "     ean_num, "
                    + "     color, "
                    + "     fas,"
                    + "     nar  "
                    + " FROM "
                    + "     eanlist_item, eanlist "
                    + " WHERE "
                    + "     eanlist.id = eanlist_item.id_eanlist and "
                    + "     eanlist.status <> -1 and "
                    + "     fas = ? and "
                    + "     nar = ? "
                    + " Order by id ";

            for (Object row : rows) {
                model = (Vector) row;
                str = "";
                tmp = new Vector();

                ps = conn.prepareStatement(sql);
                ps.setInt(1, Integer.valueOf(model.get(0).toString()));         //fas
                ps.setString(2, model.get(2).toString().trim().toUpperCase());  //nar
                rs = ps.executeQuery();

                while (rs.next()) {

                    if (rs.getString("color").trim().toUpperCase().equals(model.get(7).toString().trim().toUpperCase())
                            || rs.getString("color").trim().equals(""))

                        str = str + rs.getInt("ean_num") + " " + rs.getString("color") + "\n<br> ";

                    tmp1 = new Vector();
                    tmp1.add(rs.getInt("id"));
                    tmp1.add(rs.getInt("fas"));
                    tmp1.add(rs.getString("nar").trim());

                    if (!tmp.contains(tmp1)) tmp.add(tmp1);
                }

                str = "<html>" + str + "</html>";
                model.set(9, str);                                              // заявки с цветом
                model.set(10, 0);                                               // совпадение
                model.set(11, tmp);                                             // Массив id заявок

                elements.add(model);
            }

        } catch (SQLException e) {
            elements = rows;
            System.err.println("Ошибка getOrderEANAndColor() " + e);
            log.error("Ошибка getOrderEANAndColor()", e);
            throw new Exception("Ошибка getOrderEANAndColor() " + e.getMessage(), e);
        }
        return elements;
    }

    public Vector getDetalModelAndSize(Vector rows, int idColor) throws Exception {
        Vector rezalt = new Vector();
        Vector model;
        Vector tmp;
        String sql;
        String str1;
        String str2;
        String message;

        try {
            for (Object row : rows) {
                model = (Vector) row;

                sql = "     SELECT DISTINCT "
                        + "     eanlist.id as idEanList, "
                        + "     ean_num, "
                        + "     status , "
                        + "     ean_date,"
                        + "     fas,"
                        + "     nar,"
                        + "     srt, "
                        + "     color,"
                        + "     eanlist_item.id as idEanListItem  "
                        + " FROM "
                        + "     eanlist_item, eanlist "
                        + " WHERE "
                        + "     eanlist.id = eanlist_item.id_eanlist and "
                        + "     eanlist.id = ? and "
                        + "     eanlist_item.fas = ? and "
                        + "     eanlist_item.nar like ? and " +
                        "       ( eanlist_item.id_color = ? or eanlist_item.id_color = 2 )"
                        + " Order by ean_num, fas, nar, color, srt  ";

                ps = conn.prepareStatement(sql);
                ps.setInt(1, Integer.valueOf(model.get(0).toString()));         //id eanlist
                ps.setInt(2, Integer.valueOf(model.get(1).toString()));         //fas
                ps.setString(3, model.get(2).toString().trim()
                        .toUpperCase().replace('C', '_').replace('С', '_'));    //nar
                ps.setInt(4, idColor);                                          //idColor
                rs = ps.executeQuery();

                while (rs.next()) {
                    tmp = new Vector();
                    tmp.add(false);
                    tmp.add(rs.getInt("idEanList"));

                    switch (rs.getInt("status")) {
                        case 0:
                            message = "отправлен";
                            break;
                        case 1:
                            message = "формируется";
                            break;
                        case 2:
                            message = "ответ";
                            break;
                        case 3:
                            message = "импорт";
                            break;
                        case 4:
                            message = "закрыт";
                            break;
                        case -1:
                            message = "удалён";
                            break;
                        default:
                            message = "неизвестно";
                            break;
                    }

                    tmp.add(rs.getInt("ean_num") + "   " + message);
                    tmp.add(new SimpleDateFormat("dd-MM-yyyy").format(rs.getDate("ean_date")));
                    tmp.add(rs.getInt("fas"));
                    tmp.add(rs.getString("nar"));
                    tmp.add(rs.getInt("srt"));
                    tmp.add(rs.getString("color"));

                    sql = "SELECT DISTINCT "
                            + "    size_print, "
                            + "    ean13, "
                            + "    rst, "
                            + "    rzm,"
                            + "    ean_import   "
                            + " FROM "
                            + "     eanlist_size "
                            + " WHERE "
                            + "    id_eanlist_item = ? "
                            + " ORDER BY rst, rzm, size_print ";

                    ps = conn.prepareStatement(sql);
                    ps.setInt(1, rs.getInt("idEanListItem"));
                    ResultSet rs_ = ps.executeQuery();

                    str1 = "";
                    str2 = "";
                    while (rs_.next()) {
                        str1 = str1 + rs_.getString("size_print") + "\n<br> ";
                        str2 = str2 + rs_.getObject("ean13") + "    " + (rs_.getBoolean("ean_import") ? " ИМПОРТ" : " НЕТ") + "\n<br> ";
                    }
                    str1 = "<html>" + str1 + "</html>";
                    str2 = "<html>" + str2 + "</html>";

                    tmp.add(str1);
                    tmp.add(str2);
                    rezalt.add(tmp);
                }
            }

        } catch (SQLException e) {
            rezalt = new Vector();
            System.err.println("Ошибка getDetalModelAndSize() " + e);
            log.error("Ошибка getDetalModelAndSize()", e);
            throw new Exception("Ошибка getDetalModelAndSize() " + e.getMessage(), e);
        }
        return rezalt;
    }

    /**
     * Возвращает все модели из заявок начиная с выбранной даты, EAN-коды которых ещё не были импортированы в БД.
     * @param date
     * @return
     * @throws Exception
     */
    public ArrayList<EanList> getDataEanlistNotImport(long date) throws Exception {
        ArrayList<EanList> eanLists;
        ArrayList<EanItem> items;
        ArrayList<EanItemListSize> itemsListSizes;
        EanList elements;
        EanItem eanItem;
        EanItemListSize eanItemListSize;

        String sql = "  "
                + "     SELECT DISTINCT  "
                + "                 eanlist.id as idEanlist, "
                + "                 ean_num, "
                + "                 ean_name, "
                + "                 ean_date, "
                + "                 eanlist.note as note, "
                + "                 status, "
                + "                 eanlist.id_empl_vvod as id_empl_vvod, "
                + "                 eanlist.date_vvod as date_vvod, "
                + "                 eanlist.id_empl_ins as id_empl_ins, "
                + "                 eanlist.date_ins as date_ins "
                + "     FROM "
                + "                 eanlist, "
                + "                 eanlist_item, "
                + "                 eanlist_size "
                + "     WHERE "
                + "                 eanlist.id = eanlist_item.id_eanlist and "
                + "                 eanlist_item.id = eanlist_size.id_eanlist_item and "
                + "                 ean_import = 'FALSE' and "
                + "                 eanlist.date_vvod > ? and "
                + "                 eanlist.status <> -1 "
                + "     ORDER BY "
                + "                 idEanlist";

        try {
            ps = conn.prepareStatement(sql);
            ps.setDate(1, new java.sql.Date(date));
            rs = ps.executeQuery();

            eanLists = new ArrayList<>();

            while (rs.next()) {
                elements = new EanList();

                elements.setIdEanlist(rs.getInt("idEanlist"));
                elements.setEanNum(rs.getInt("ean_num"));
                elements.setEanName(rs.getString("ean_name"));
                elements.setEanDate(new SimpleDateFormat("dd-MM-yyyy").format(rs.getDate("ean_date")));
                elements.setIdEmplVvod(rs.getInt("id_empl_vvod"));
                elements.setVvodFio("");
                elements.setDateVvod(new SimpleDateFormat("dd-MM-yyyy").format(rs.getDate("date_vvod")));
                elements.setIdEmplIns(rs.getInt("id_empl_ins"));
                elements.setInsFio("");
                elements.setInsDate(new SimpleDateFormat("dd-MM-yyyy").format(rs.getDate("date_ins")));
                elements.setNote(rs.getString("note"));

                switch (rs.getInt("status")) {
                    case 0:
                        elements.setStatus("отправлен");
                        break;
                    case 1:
                        elements.setStatus("формируется");
                        break;
                    case 2:
                        elements.setStatus("ответ");
                        break;
                    case 3:
                        elements.setStatus("импорт");
                        break;
                    case 4:
                        elements.setStatus("закрыт");
                        break;
                    case -1:
                        elements.setStatus("удалён");
                        break;
                    default:
                        elements.setStatus("неизвестно");
                        break;
                }
                elements.setIdStatus(rs.getInt("status"));

                sql = " "
                        + " SELECT  DISTINCT "
                        + "         eanlist_item.id as idItem, "
                        + "         sar, "
                        + "         nar, "
                        + "         fas, "
                        + "         pname, "
                        + "         srt, "
                        + "         fas_vid, "
                        + "         kol_x, "
                        + "         gpc_seg, "
                        + "         gpc_sem, "
                        + "         gpc_kl, "
                        + "         gpc_br, "
                        + "         okrb, "
                        + "         tnb, "
                        + "         gost, "
                        + "         upac, "
                        + "         eanlist_item.note as note,"
                        + "         id_color,"
                        + "         color "
                        + " FROM "
                        + "         eanlist_item , "
                        + "         eanlist_size "
                        + " WHERE "
                        + "         eanlist_item.id_eanlist = ? and "
                        + "         eanlist_item.id = eanlist_size.id_eanlist_item and "
                        + "         ean_import = 'FALSE' "
                        + " ORDER BY "
                        + "         fas, sar, nar, color, srt, upac ";

                ps = conn.prepareStatement(sql);
                ps.setInt(1, rs.getInt("idEanlist"));
                ResultSet rs_ = ps.executeQuery();

                items = new ArrayList<>();

                while (rs_.next()) {
                    eanItem = new EanItem();

                    eanItem.setIdFlag(1);
                    eanItem.setId(rs_.getInt("idItem"));
                    eanItem.setFasNum(rs_.getInt("fas"));
                    eanItem.setFasName(rs_.getString("pname"));
                    eanItem.setFasVid(rs_.getInt("fas_vid"));
                    eanItem.setFasSrt(rs_.getInt("srt"));
                    eanItem.setKolX(rs_.getInt("kol_x"));
                    eanItem.setFasNar(rs_.getString("nar"));
                    eanItem.setNoteText(rs_.getString("note"));
                    eanItem.setUpacText(rs_.getString("upac"));
                    eanItem.setTextGpcSeg(rs_.getInt("gpc_seg"));
                    eanItem.setTextGpcSem(rs_.getInt("gpc_sem"));
                    eanItem.setTextGpcKl(rs_.getInt("gpc_kl"));
                    eanItem.setTextGpcBr(rs_.getInt("gpc_br"));
                    eanItem.setTextOKRB(rs_.getString("okrb"));
                    eanItem.setTextTHB(rs_.getString("tnb"));
                    eanItem.setTextGOST(rs_.getString("gost"));
                    eanItem.setColorNum(rs_.getInt("id_color"));
                    eanItem.setColorName(rs_.getString("color"));

                    sql = " "
                            + " SELECT DISTINCT "
                            + "     eanlist_size.id as idSize, "
                            + "     id_eanlist_item, "
                            + "     rst, "
                            + "     rzm, "
                            + "     size_print, "
                            + "     ean13, "
                            + "     ean_import "
                            + " FROM "
                            + "     eanlist_size "
                            + " WHERE "
                            + "     eanlist_size.id_eanlist_item = ? and "
                            + "     ean_import = 'FALSE' "
                            + " ORDER BY "
                            + "     rst, rzm, size_print  ";

                    ps = conn.prepareStatement(sql);
                    ps.setInt(1, rs_.getInt("idItem"));
                    ResultSet rs__ = ps.executeQuery();

                    itemsListSizes = new ArrayList<>();

                    while (rs__.next()) {
                        eanItemListSize = new EanItemListSize();

                        eanItemListSize.setIdEanItem(rs.getInt("idEanlist"));
                        eanItemListSize.setId(rs__.getInt("idSize"));
                        eanItemListSize.setRst(rs__.getInt("rst"));
                        eanItemListSize.setRzm(rs__.getInt("rzm"));
                        eanItemListSize.setSizePrint(rs__.getString("size_print"));
                        eanItemListSize.setEan13(rs__.getObject("ean13") != null ? rs__.getString("ean13") : "");
                        eanItemListSize.setEanImport(rs__.getBoolean("ean_import"));

                        itemsListSizes.add(eanItemListSize);
                    }

                    eanItem.setDataSize(itemsListSizes);
                    items.add(eanItem);
                }
                elements.setEanItems(items);

                eanLists.add(elements);
            }
        } catch (SQLException e) {
            eanLists = new ArrayList<>();
            System.err.println("Ошибка getDataEanlistNotImport() " + e);
            log.error("Ошибка getDataEanlistNotImport()", e);
            throw new Exception("Ошибка getDataEanlistNotImport() " + e.getMessage(), e);
        }
        return eanLists;
    }

    /**
     * Возвращает все модели из заявок удовлетворяющие условию.
     * @param fas
     * @param nar
     * @return
     * @throws Exception
     */
    public ArrayList<EanList> getDataEanlistImport(String fas, String nar) throws Exception {
        ArrayList<EanList> eanLists;
        ArrayList<EanItem> items;
        ArrayList<EanItemListSize> itemsListSizes;
        EanList elements;
        EanItem eanItem;
        EanItemListSize eanItemListSize;

        String sql = "  "
                + "     SELECT DISTINCT  "
                + "                 eanlist.id as idEanlist, "
                + "                 ean_num, "
                + "                 ean_name, "
                + "                 ean_date, "
                + "                 eanlist.note as note, "
                + "                 status, "
                + "                 eanlist.id_empl_vvod as id_empl_vvod, "
                + "                 eanlist.date_vvod as date_vvod, "
                + "                 eanlist.id_empl_ins as id_empl_ins, "
                + "                 eanlist.date_ins as date_ins "
                + "     FROM "
                + "                 eanlist, "
                + "                 eanlist_item, "
                + "                 eanlist_size "
                + "     WHERE "
                + "                 eanlist.id = eanlist_item.id_eanlist and "
                + "                 eanlist_item.id = eanlist_size.id_eanlist_item and "
                + "                 eanlist_item.fas::text like ? and "
                + "                 eanlist_item.nar::text like ? and "
                + "                 eanlist.status <> -1 "
                + "     ORDER BY "
                + "                 idEanlist";

        try {
            ps = conn.prepareStatement(sql);
            ps.setString(1, fas + "%");
            ps.setString(2, nar + "%");
            rs = ps.executeQuery();

            eanLists = new ArrayList<>();

            while (rs.next()) {
                elements = new EanList();

                elements.setIdEanlist(rs.getInt("idEanlist"));
                elements.setEanNum(rs.getInt("ean_num"));
                elements.setEanName(rs.getString("ean_name"));
                elements.setEanDate(new SimpleDateFormat("dd-MM-yyyy").format(rs.getDate("ean_date")));
                elements.setIdEmplVvod(rs.getInt("id_empl_vvod"));
                elements.setVvodFio("");
                elements.setDateVvod(new SimpleDateFormat("dd-MM-yyyy").format(rs.getDate("date_vvod")));
                elements.setIdEmplIns(rs.getInt("id_empl_ins"));
                elements.setInsFio("");
                elements.setInsDate(new SimpleDateFormat("dd-MM-yyyy").format(rs.getDate("date_ins")));
                elements.setNote(rs.getString("note"));

                switch (rs.getInt("status")) {
                    case 0:
                        elements.setStatus("отправлен");
                        break;
                    case 1:
                        elements.setStatus("формируется");
                        break;
                    case 2:
                        elements.setStatus("ответ");
                        break;
                    case 3:
                        elements.setStatus("импорт");
                        break;
                    case 4:
                        elements.setStatus("закрыт");
                        break;
                    case -1:
                        elements.setStatus("удалён");
                        break;
                    default:
                        elements.setStatus("неизвестно");
                        break;
                }
                elements.setIdStatus(rs.getInt("status"));

                sql = " "
                        + " SELECT  DISTINCT "
                        + "         eanlist_item.id as idItem, "
                        + "         sar, "
                        + "         nar, "
                        + "         fas, "
                        + "         pname, "
                        + "         srt, "
                        + "         fas_vid, "
                        + "         kol_x, "
                        + "         gpc_seg, "
                        + "         gpc_sem, "
                        + "         gpc_kl, "
                        + "         gpc_br, "
                        + "         okrb, "
                        + "         tnb, "
                        + "         gost, "
                        + "         upac, "
                        + "         eanlist_item.note as note,"
                        + "         id_color,"
                        + "         color "
                        + " FROM "
                        + "         eanlist_item , "
                        + "         eanlist_size "
                        + " WHERE "
                        + "         eanlist_item.id_eanlist = ? and "
                        + "         eanlist_item.id = eanlist_size.id_eanlist_item and "
                        + "         eanlist_item.fas::text like ? and "
                        + "         eanlist_item.nar::text like ? "
                        + " ORDER BY "
                        + "         fas, sar, nar, color, srt, upac ";

                ps = conn.prepareStatement(sql);
                ps.setInt(1, rs.getInt("idEanlist"));
                ps.setString(2, fas + "%");
                ps.setString(3, nar + "%");
                ResultSet rs_ = ps.executeQuery();

                items = new ArrayList<>();

                while (rs_.next()) {
                    eanItem = new EanItem();

                    eanItem.setIdFlag(1);
                    eanItem.setId(rs_.getInt("idItem"));
                    eanItem.setFasNum(rs_.getInt("fas"));
                    eanItem.setFasName(rs_.getString("pname"));
                    eanItem.setFasVid(rs_.getInt("fas_vid"));
                    eanItem.setFasSrt(rs_.getInt("srt"));
                    eanItem.setKolX(rs_.getInt("kol_x"));
                    eanItem.setFasNar(rs_.getString("nar"));
                    eanItem.setNoteText(rs_.getString("note"));
                    eanItem.setUpacText(rs_.getString("upac"));
                    eanItem.setTextGpcSeg(rs_.getInt("gpc_seg"));
                    eanItem.setTextGpcSem(rs_.getInt("gpc_sem"));
                    eanItem.setTextGpcKl(rs_.getInt("gpc_kl"));
                    eanItem.setTextGpcBr(rs_.getInt("gpc_br"));
                    eanItem.setTextOKRB(rs_.getString("okrb"));
                    eanItem.setTextTHB(rs_.getString("tnb"));
                    eanItem.setTextGOST(rs_.getString("gost"));
                    eanItem.setColorNum(rs_.getInt("id_color"));
                    eanItem.setColorName(rs_.getString("color"));

                    sql = " "
                            + " SELECT DISTINCT "
                            + "     eanlist_size.id as idSize, "
                            + "     id_eanlist_item, "
                            + "     rst, "
                            + "     rzm, "
                            + "     size_print, "
                            + "     ean13, "
                            + "     ean_import "
                            + " FROM "
                            + "     eanlist_size "
                            + " WHERE "
                            + "     eanlist_size.id_eanlist_item = ? "
                            + " ORDER BY "
                            + "     rst, rzm, size_print  ";

                    ps = conn.prepareStatement(sql);
                    ps.setInt(1, rs_.getInt("idItem"));
                    ResultSet rs__ = ps.executeQuery();

                    itemsListSizes = new ArrayList<>();

                    while (rs__.next()) {
                        eanItemListSize = new EanItemListSize();

                        eanItemListSize.setIdEanItem(rs.getInt("idEanlist"));
                        eanItemListSize.setId(rs__.getInt("idSize"));
                        eanItemListSize.setRst(rs__.getInt("rst"));
                        eanItemListSize.setRzm(rs__.getInt("rzm"));
                        eanItemListSize.setSizePrint(rs__.getString("size_print"));
                        eanItemListSize.setEan13(rs__.getObject("ean13") != null ? rs__.getString("ean13") : "");
                        eanItemListSize.setEanImport(rs__.getBoolean("ean_import"));

                        itemsListSizes.add(eanItemListSize);
                    }

                    eanItem.setDataSize(itemsListSizes);
                    // TODO Тут добавление массы.
                    items.add(eanItem);
                }
                elements.setEanItems(items);

                eanLists.add(elements);
            }
        } catch (SQLException e) {
            eanLists = new ArrayList<>();
            System.err.println("Ошибка getDataEanlistImport() " + e);
            log.error("Ошибка getDataEanlistImport()", e);
            throw new Exception("Ошибка getDataEanlistImport() " + e.getMessage(), e);
        }
        return eanLists;
    }

    /**
     * Возвращает все модели из заявок удовлетворяющие условию.
     * @param idEanList
     * @return
     * @throws Exception
     */
    public ArrayList<EanList> getDataEanlistImport(int idEanList) throws Exception {
        ArrayList<EanList> eanLists;
        ArrayList<EanItem> items;
        ArrayList<EanItemListSize> itemsListSizes;
        EanList elements;
        EanItem eanItem;
        EanItemListSize eanItemListSize;

        String sql = "  "
                + "     SELECT DISTINCT  "
                + "                 eanlist.id as idEanlist, "
                + "                 ean_num, "
                + "                 ean_name, "
                + "                 ean_date, "
                + "                 eanlist.note as note, "
                + "                 status, "
                + "                 eanlist.id_empl_vvod as id_empl_vvod, "
                + "                 eanlist.date_vvod as date_vvod, "
                + "                 eanlist.id_empl_ins as id_empl_ins, "
                + "                 eanlist.date_ins as date_ins "
                + "     FROM "
                + "                 eanlist, "
                + "                 eanlist_item, "
                + "                 eanlist_size "
                + "     WHERE "
                + "                 eanlist.id = eanlist_item.id_eanlist and "
                + "                 eanlist_item.id = eanlist_size.id_eanlist_item and "
                + "                 eanlist.id = ? and "
                + "                 eanlist.status <> -1 "
                + "     ORDER BY "
                + "                 idEanlist";

        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, idEanList);
            rs = ps.executeQuery();

            eanLists = new ArrayList<>();

            while (rs.next()) {
                elements = new EanList();

                elements.setIdEanlist(rs.getInt("idEanlist"));
                elements.setEanNum(rs.getInt("ean_num"));
                elements.setEanName(rs.getString("ean_name"));
                elements.setEanDate(new SimpleDateFormat("dd-MM-yyyy").format(rs.getDate("ean_date")));
                elements.setIdEmplVvod(rs.getInt("id_empl_vvod"));
                elements.setVvodFio("");
                elements.setDateVvod(new SimpleDateFormat("dd-MM-yyyy").format(rs.getDate("date_vvod")));
                elements.setIdEmplIns(rs.getInt("id_empl_ins"));
                elements.setInsFio("");
                elements.setInsDate(new SimpleDateFormat("dd-MM-yyyy").format(rs.getDate("date_ins")));
                elements.setNote(rs.getString("note"));

                switch (rs.getInt("status")) {
                    case 0:
                        elements.setStatus("отправлен");
                        break;
                    case 1:
                        elements.setStatus("формируется");
                        break;
                    case 2:
                        elements.setStatus("ответ");
                        break;
                    case 3:
                        elements.setStatus("импорт");
                        break;
                    case 4:
                        elements.setStatus("закрыт");
                        break;
                    case -1:
                        elements.setStatus("удалён");
                        break;
                    default:
                        elements.setStatus("неизвестно");
                        break;
                }
                elements.setIdStatus(rs.getInt("status"));

                sql = " "
                        + " SELECT  DISTINCT "
                        + "         eanlist_item.id as idItem, "
                        + "         sar, "
                        + "         nar, "
                        + "         fas, "
                        + "         pname, "
                        + "         srt, "
                        + "         fas_vid, "
                        + "         kol_x, "
                        + "         gpc_seg, "
                        + "         gpc_sem, "
                        + "         gpc_kl, "
                        + "         gpc_br, "
                        + "         okrb, "
                        + "         tnb, "
                        + "         gost, "
                        + "         upac, "
                        + "         eanlist_item.note as note,"
                        + "         id_color,"
                        + "         color "
                        + " FROM "
                        + "         eanlist_item , "
                        + "         eanlist_size "
                        + " WHERE "
                        + "         eanlist_item.id_eanlist = ? and "
                        + "         eanlist_item.id = eanlist_size.id_eanlist_item "
                        + " ORDER BY "
                        + "         fas, sar, nar, color, srt, upac ";

                ps = conn.prepareStatement(sql);
                ps.setInt(1, idEanList);
                ResultSet rs_ = ps.executeQuery();

                items = new ArrayList<>();

                while (rs_.next()) {
                    eanItem = new EanItem();

                    eanItem.setIdFlag(1);
                    eanItem.setId(rs_.getInt("idItem"));
                    eanItem.setFasNum(rs_.getInt("fas"));
                    eanItem.setFasName(rs_.getString("pname"));
                    eanItem.setFasVid(rs_.getInt("fas_vid"));
                    eanItem.setFasSrt(rs_.getInt("srt"));
                    eanItem.setKolX(rs_.getInt("kol_x"));
                    eanItem.setFasNar(rs_.getString("nar"));
                    eanItem.setNoteText(rs_.getString("note"));
                    eanItem.setUpacText(rs_.getString("upac"));
                    eanItem.setTextGpcSeg(rs_.getInt("gpc_seg"));
                    eanItem.setTextGpcSem(rs_.getInt("gpc_sem"));
                    eanItem.setTextGpcKl(rs_.getInt("gpc_kl"));
                    eanItem.setTextGpcBr(rs_.getInt("gpc_br"));
                    eanItem.setTextOKRB(rs_.getString("okrb"));
                    eanItem.setTextTHB(rs_.getString("tnb"));
                    eanItem.setTextGOST(rs_.getString("gost"));
                    eanItem.setColorNum(rs_.getInt("id_color"));
                    eanItem.setColorName(rs_.getString("color"));

                    sql = " "
                            + " SELECT DISTINCT "
                            + "     eanlist_size.id as idSize, "
                            + "     id_eanlist_item, "
                            + "     rst, "
                            + "     rzm, "
                            + "     size_print, "
                            + "     ean13, "
                            + "     ean_import "
                            + " FROM "
                            + "     eanlist_size "
                            + " WHERE "
                            + "     eanlist_size.id_eanlist_item = ? "
                            + " ORDER BY "
                            + "     rst, rzm, size_print  ";

                    ps = conn.prepareStatement(sql);
                    ps.setInt(1, rs_.getInt("idItem"));
                    ResultSet rs__ = ps.executeQuery();

                    itemsListSizes = new ArrayList<>();

                    while (rs__.next()) {
                        eanItemListSize = new EanItemListSize();

                        eanItemListSize.setIdEanItem(rs.getInt("idEanlist"));
                        eanItemListSize.setId(rs__.getInt("idSize"));
                        eanItemListSize.setRst(rs__.getInt("rst"));
                        eanItemListSize.setRzm(rs__.getInt("rzm"));
                        eanItemListSize.setSizePrint(rs__.getString("size_print"));
                        eanItemListSize.setEan13(rs__.getObject("ean13") != null ? rs__.getString("ean13") : "");
                        eanItemListSize.setEanImport(rs__.getBoolean("ean_import"));

                        itemsListSizes.add(eanItemListSize);
                    }

                    eanItem.setDataSize(itemsListSizes);
                    items.add(eanItem);
                }
                elements.setEanItems(items);

                eanLists.add(elements);
            }
        } catch (SQLException e) {
            eanLists = new ArrayList<>();
            System.err.println("Ошибка getDataEanlistImport() " + e);
            log.error("Ошибка getDataEanlistImport()", e);
            throw new Exception("Ошибка getDataEanlistImport() " + e.getMessage(), e);
        }
        return eanLists;
    }

    public boolean setEanImport(ArrayList<EanItemListSize> listSizes) throws Exception {
        boolean rez = true;
        String sql;

        try {
            sql = ""
                    + " Update "
                    + "     eanlist_size "
                    + " Set "
                    + "     ean_import = 'true' "
                    + " Where "
                    + "     eanlist_size.id = ? ";

            for (EanItemListSize eanItemListSize : listSizes) {
                ps = conn.prepareStatement(sql);
                ps.setInt(1, eanItemListSize.getId());
                ps.execute();
            }
        } catch (SQLException e) {
            rez = false;
            System.err.println("Ошибка setEanImport() " + e);
            log.error("Ошибка setEanImport()", e);
            throw new Exception("Ошибка setEanImport() " + e.getMessage(), e);
        }
        return rez;
    }

    /**
     * Возвращает все записи из заявок начиная с выбранной даты, EAN-коды для которых существуют
     * @param date
     * @return
     * @throws Exception
     */
    public ArrayList<EanItem> getDataEanItemWithExistingEanByDate(long date) throws Exception {
        ArrayList<EanItem> items;
        ArrayList<EanItemListSize> itemsListSizes;
        EanItem eanItem;
        EanItemListSize eanItemListSize;

        String sql = "";

        try {

            sql = " "
                    + " SELECT  DISTINCT "
                    + "         eanlist_item.id as idItem, "
                    + "         sar, "
                    + "         nar, "
                    + "         fas, "
                    + "         pname, "
                    + "         srt, "
                    + "         fas_vid, "
                    + "         kol_x, "
                    + "         gpc_seg, "
                    + "         gpc_sem, "
                    + "         gpc_kl, "
                    + "         gpc_br, "
                    + "         okrb, "
                    + "         tnb, "
                    + "         gost, "
                    + "         upac, "
                    + "         eanlist_item.note as note,"
                    + "         id_color,"
                    + "         color "
                    + " FROM "
                    + "         eanlist, "
                    + "         eanlist_item , "
                    + "         eanlist_size "
                    + " WHERE "
                    + "         eanlist.id = eanlist_item.id_eanlist and "
                    + "         eanlist.date_vvod > ? and "
                    + "         eanlist_item.id = eanlist_size.id_eanlist_item and "
                    + "         ean13 not like '' "
                    + " ORDER BY "
                    + "         fas, sar, nar, color, srt, upac ";

            ps = conn.prepareStatement(sql);
            ps.setDate(1, new java.sql.Date(date));
            rs = ps.executeQuery();

            items = new ArrayList<EanItem>();

            while (rs.next()) {
                eanItem = new EanItem();

                eanItem.setIdFlag(1);
                eanItem.setId(rs.getInt("idItem"));
                eanItem.setFasNum(rs.getInt("fas"));
                eanItem.setFasName(rs.getString("pname"));
                eanItem.setFasVid(rs.getInt("fas_vid"));
                eanItem.setFasSrt(rs.getInt("srt"));
                eanItem.setKolX(rs.getInt("kol_x"));
                eanItem.setFasNar(rs.getString("nar"));
                eanItem.setNoteText(rs.getString("note"));
                eanItem.setUpacText(rs.getString("upac"));
                eanItem.setTextGpcSeg(rs.getInt("gpc_seg"));
                eanItem.setTextGpcSem(rs.getInt("gpc_sem"));
                eanItem.setTextGpcKl(rs.getInt("gpc_kl"));
                eanItem.setTextGpcBr(rs.getInt("gpc_br"));
                eanItem.setTextOKRB(rs.getString("okrb"));
                eanItem.setTextTHB(rs.getString("tnb"));
                eanItem.setTextGOST(rs.getString("gost"));
                eanItem.setColorNum(rs.getInt("id_color"));
                eanItem.setColorName(rs.getString("color"));

                sql = " "
                        + " SELECT DISTINCT "
                        + "     eanlist_size.id as idSize, "
                        + "     id_eanlist_item, "
                        + "     rst, "
                        + "     rzm, "
                        + "     size_print, "
                        + "     ean13, "
                        + "     ean_import "
                        + " FROM "
                        + "     eanlist_size "
                        + " WHERE "
                        + "     eanlist_size.id_eanlist_item = ? and "
                        + "     ean13 not like '' "
                        + " ORDER BY "
                        + "     rst, rzm, size_print  ";

                ps = conn.prepareStatement(sql);
                ps.setInt(1, rs.getInt("idItem"));
                ResultSet rs_ = ps.executeQuery();

                itemsListSizes = new ArrayList<EanItemListSize>();

                while (rs_.next()) {
                    eanItemListSize = new EanItemListSize();

                    eanItemListSize.setIdEanItem(rs.getInt("idItem"));
                    eanItemListSize.setId(rs_.getInt("idSize"));
                    eanItemListSize.setRst(rs_.getInt("rst"));
                    eanItemListSize.setRzm(rs_.getInt("rzm"));
                    eanItemListSize.setSizePrint(rs_.getString("size_print"));
                    eanItemListSize.setEan13(rs_.getObject("ean13") != null ? rs_.getString("ean13") : "");
                    eanItemListSize.setEanImport(rs_.getBoolean("ean_import"));

                    itemsListSizes.add(eanItemListSize);
                }

                eanItem.setDataSize(itemsListSizes);

                if (!itemsListSizes.isEmpty())
                    items.add(eanItem);
            }
        } catch (SQLException e) {
            items = new ArrayList<EanItem>();
            System.err.println("Ошибка getDataEanItemNotImport() " + e);
            log.error("Ошибка getDataEanItemNotImport()", e);
            throw new Exception("Ошибка getDataEanItemNotImport() " + e.getMessage(), e);
        }
        return items;
    }

    /**
     * Возвращает id заявки на получение EAN-кодов ГС1 по (IdEanItem) номеру записи из неё.
     * @param idEanItem
     * @return
     * @throws Exception
     */
    int getIdEanListByIdEanItem(int idEanItem) throws Exception {
        int idEanList = 0;
        String sql;

        try {
            sql = "SELECT "
                    + "     eanlist.id as id "
                    + " FROM "
                    + "     eanlist, "
                    + "     eanlist_item "
                    + " WHERE "
                    + "     eanlist.id = eanlist_item.id_eanlist and "
                    + "     eanlist_item.id = ? ";

            ps = conn.prepareStatement(sql);
            ps.setInt(1, idEanItem);
            rs = ps.executeQuery();

            if (rs.next()) {
                idEanList = rs.getInt("id");
            }

        } catch (SQLException e) {
            idEanList = 0;
            System.err.println("Ошибка getIdEanListByIdEanItem() " + e);
            log.error("Ошибка getIdEanListByIdEanItem()", e);
            throw new Exception("Ошибка getIdEanListByIdEanItem() " + e.getMessage(), e);
        }
        return idEanList;
    }

    public ArrayList<EanItem> getDataEanItemWithNotExistingEanByDate(long date) throws Exception {
        ArrayList<EanItem> items;
        ArrayList<EanItemListSize> itemsListSizes;
        EanItem eanItem;
        EanItemListSize eanItemListSize;

        String sql = "";

        try {

            sql = " "
                    + " SELECT  DISTINCT "
                    + "         eanlist_item.id as idItem, "
                    + "         sar, "
                    + "         nar, "
                    + "         fas, "
                    + "         pname, "
                    + "         srt, "
                    + "         fas_vid, "
                    + "         kol_x, "
                    + "         gpc_seg, "
                    + "         gpc_sem, "
                    + "         gpc_kl, "
                    + "         gpc_br, "
                    + "         okrb, "
                    + "         tnb, "
                    + "         gost, "
                    + "         upac, "
                    + "         eanlist_item.note as note,"
                    + "         id_color,"
                    + "         color "
                    + " FROM "
                    + "         eanlist, "
                    + "         eanlist_item , "
                    + "         eanlist_size "
                    + " WHERE "
                    + "         eanlist.id = eanlist_item.id_eanlist and "
                    + "         eanlist.date_vvod > ? and "
                    + "         eanlist_item.id = eanlist_size.id_eanlist_item and "
                    + "         ean13 like '' "
                    + " ORDER BY "
                    + "         fas, sar, nar, color, srt, upac ";

            ps = conn.prepareStatement(sql);
            ps.setDate(1, new java.sql.Date(date));
            rs = ps.executeQuery();

            items = new ArrayList<EanItem>();

            while (rs.next()) {
                eanItem = new EanItem();

                eanItem.setIdFlag(1);
                eanItem.setId(rs.getInt("idItem"));
                eanItem.setFasNum(rs.getInt("fas"));
                eanItem.setFasName(rs.getString("pname"));
                eanItem.setFasVid(rs.getInt("fas_vid"));
                eanItem.setFasSrt(rs.getInt("srt"));
                eanItem.setKolX(rs.getInt("kol_x"));
                eanItem.setFasNar(rs.getString("nar"));
                eanItem.setNoteText(rs.getString("note"));
                eanItem.setUpacText(rs.getString("upac"));
                eanItem.setTextGpcSeg(rs.getInt("gpc_seg"));
                eanItem.setTextGpcSem(rs.getInt("gpc_sem"));
                eanItem.setTextGpcKl(rs.getInt("gpc_kl"));
                eanItem.setTextGpcBr(rs.getInt("gpc_br"));
                eanItem.setTextOKRB(rs.getString("okrb"));
                eanItem.setTextTHB(rs.getString("tnb"));
                eanItem.setTextGOST(rs.getString("gost"));
                eanItem.setColorNum(rs.getInt("id_color"));
                eanItem.setColorName(rs.getString("color"));

                sql = " "
                        + " SELECT DISTINCT "
                        + "     eanlist_size.id as idSize, "
                        + "     id_eanlist_item, "
                        + "     rst, "
                        + "     rzm, "
                        + "     size_print, "
                        + "     ean13, "
                        + "     ean_import "
                        + " FROM "
                        + "     eanlist_size "
                        + " WHERE "
                        + "     eanlist_size.id_eanlist_item = ? and "
                        + "     ean13 like '' "
                        + " ORDER BY "
                        + "     rst, rzm, size_print  ";

                ps = conn.prepareStatement(sql);
                ps.setInt(1, rs.getInt("idItem"));
                ResultSet rs_ = ps.executeQuery();

                itemsListSizes = new ArrayList<EanItemListSize>();

                while (rs_.next()) {
                    eanItemListSize = new EanItemListSize();

                    eanItemListSize.setIdEanItem(rs.getInt("idItem"));
                    eanItemListSize.setId(rs_.getInt("idSize"));
                    eanItemListSize.setRst(rs_.getInt("rst"));
                    eanItemListSize.setRzm(rs_.getInt("rzm"));
                    eanItemListSize.setSizePrint(rs_.getString("size_print"));
                    eanItemListSize.setEan13("XXX");
                    eanItemListSize.setEanImport(rs_.getBoolean("ean_import"));

                    itemsListSizes.add(eanItemListSize);
                }

                eanItem.setDataSize(itemsListSizes);

                if (!itemsListSizes.isEmpty())
                    items.add(eanItem);
            }
        } catch (SQLException e) {
            items = new ArrayList<EanItem>();
            System.err.println("Ошибка getDataEanItemNotImport() " + e);
            log.error("Ошибка getDataEanItemNotImport()", e);
            throw new Exception("Ошибка getDataEanItemNotImport() " + e.getMessage(), e);
        }
        return items;
    }

    public void eanVerified(String ean) {
        try {
            setAutoCommit(false);
            String sql = "Update eanlist_size set datamark_verification = true "
                    + " Where eanlist_size.ean13 like '%" + ean + "%'";
            ps = conn.prepareStatement(sql);
            ps.execute();
            commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void changeEanRequestDatamarkExportStatusById(String requestNumberId, String status) {
        try {
            setAutoCommit(false);
            String sql = "Update eanlist set datamark_export = '" + status + "'"
                    + " where eanlist.id = " + requestNumberId;
            ps = conn.prepareStatement(sql);
            ps.execute();
            commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Map<String, String> getEansByRequestNumberId(String requestNumberId) {
        Map<String, String> eanTnbMap = new HashMap<>();
        try {
            String sql = "Select ean13, tnb " +
                    "from eanlist e " +
                    "inner join eanlist_item ei on e.id = ei.id_eanlist " +
                    "inner join eanlist_size es on ei.id = es.id_eanlist_item " +
                    "where e.id =" + requestNumberId;
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                eanTnbMap.put(rs.getString("ean13"), rs.getString("tnb"));
            }
            return eanTnbMap;
        } catch (Exception e) {
            e.printStackTrace();
            return eanTnbMap;
        }
    }
}
