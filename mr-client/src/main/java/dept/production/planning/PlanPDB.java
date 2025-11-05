package dept.production.planning;

import by.march8.api.utils.DatePeriod;
import by.march8.api.utils.DateUtils;
import by.march8.ecs.application.modules.marketing.model.PostgresPlanningDocument;
import by.march8.ecs.application.modules.marketing.model.PostgresPlanningDocumentItem;
import by.march8.ecs.framework.common.LogCrutch;
import common.UtilFunctions;
import dept.production.planning.ean.UtilEan;
import workDB.PDB_new;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Vector;

/**
 *
 * @author lidashka
 */
@SuppressWarnings("all")
public class PlanPDB extends PDB_new {
    // private static final Logger log = new Log().getLoger(PlanPDB.class);
    private static final LogCrutch log = new LogCrutch();

    /**
     * Возвращает все подразделения
     * @return
     * @throws Exception
     */
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
            System.err.println("Ошибка getAllDept() " + e);
            log.error("Ошибка getAllDept()", e);
            throw new Exception("Ошибка getAllDept() " + e.getMessage(), e);
        }
        return dept;
    }

    /**
     * Добавляет план производства в БД
     * @param namePlan - наименование плана
     * @param idDept - id подразделения
     * @param sDate - дата плана
     * @param status - статус плана
     * @param note - примечание 
     * @param list - элементы плана производства
     * @param idEmploye - id сотрудника 
     * @return true/false
     * @throws Exception
     */
    public int addPlan(String namePlan, int idDept, long sDate, int status, String note, ArrayList list, int idEmploye) throws Exception {
        String sql = "";
        int idPlan = -1;

        try {
            setAutoCommit(false);

            sql = "Insert into plan(id_dept, plan_name, plan_date, id_empl_vvod, id_empl_ins, note, status) "
                    + " values( ?, ?, ?, ?, ?, ?, ?) returning id";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, idDept);
            ps.setString(2, namePlan);
            ps.setDate(3, new java.sql.Date(sDate));
            ps.setInt(4, idEmploye);
            ps.setInt(5, idEmploye);
            ps.setString(6, note);
            ps.setInt(7, status);
            rs = ps.executeQuery();

            if (rs.next()) {
                idPlan = rs.getInt(1);

                sql = "Insert into plan_item(id_plan, cx, sar, fas, rst, rzm, kol_month, kol_day, note_item,  id_empl_ins) "
                        + "values( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

                for (int i = 0; i < list.size(); i++) {
                    Object[] row = (Object[]) list.get(i);

                    if (!row[1].toString().substring(0, 2).equals("43") &&
                            !row[1].toString().substring(0, 2).equals("47") &&
                            !row[1].toString().substring(0, 2).equals("48") &&
                            !row[1].toString().substring(0, 2).equals("49")) {
                        ps = conn.prepareStatement(sql);
                        ps.setInt(1, idPlan);
                        ps.setInt(2, Integer.valueOf(row[0].toString()));
                        ps.setInt(3, Integer.valueOf(row[1].toString()));
                        ps.setInt(4, Integer.valueOf(row[2].toString()));
                        ps.setInt(5, Integer.valueOf(row[3].toString()));
                        ps.setInt(6, Integer.valueOf(row[4].toString()));
                        ps.setDouble(7, Double.valueOf(row[5].toString()));
                        ps.setDouble(8, Double.valueOf(row[6].toString()));
                        ps.setString(9, String.valueOf(row[7]).trim());
                        ps.setInt(10, idEmploye);
                        ps.execute();
                    }
                }
            } else
                throw new Exception();

            commit();
        } catch (Exception e) {
            rollBack();
            idPlan = -1;
            System.err.println("Ошибка addPlan() " + e);
            log.error("Ошибка addPlan()", e);
            throw new Exception("Ошибка addPlan() " + e.getMessage(), e);
        } finally {
            setAutoCommit(true);
        }
        return idPlan;
    }

    /**
     * Добавляет план производства в БД из временной таблицы
     * @param namePlan
     * @param idDept
     * @param sDate
     * @param status
     * @param note
     * @param idEmploye
     * @return
     * @throws Exception
     */
    public int addTempPlanTable(String namePlan, int idDept, long sDate, int status, String note, int idEmploye) throws Exception {
        String sql = "";
        int idPlan = 0;

        try {
            setAutoCommit(false);

            sql = "Insert into plan(id_dept, plan_name, plan_date, id_empl_vvod, id_empl_ins, note, status) "
                    + " values( ?, ?, ?, ?, ?, ?, ?) returning id";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, idDept);
            ps.setString(2, namePlan);
            ps.setDate(3, new java.sql.Date(sDate));
            ps.setInt(4, idEmploye);
            ps.setInt(5, idEmploye);
            ps.setString(6, note);
            ps.setInt(7, status);
            rs = ps.executeQuery();

            if (rs.next()) {
                idPlan = rs.getInt(1);

                sql = "Select cx, sar, fas, rst, rzm, kol_month, kol_day, id_spec, id_empl_ins, date_ins, dekada1, dekada2, dekada3, note_item "
                        + " From tmp_plan_item ";
                ps = conn.prepareStatement(sql);
                rs = ps.executeQuery();

                while (rs.next()) {

                    sql = "Insert into plan_item(id_plan, cx, sar, fas, rst, rzm, kol_month, kol_day, id_spec, id_empl_ins, date_ins, "
                            + "                     dekada1, dekada2, dekada3, note_item) "
                            + "values( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                    ps = conn.prepareStatement(sql);
                    ps.setInt(1, idPlan);
                    ps.setInt(2, rs.getInt("cx"));
                    ps.setInt(3, rs.getInt("sar"));
                    ps.setInt(4, rs.getInt("fas"));
                    ps.setInt(5, rs.getInt("rst"));
                    ps.setInt(6, rs.getInt("rzm"));
                    ps.setDouble(7, rs.getDouble("kol_month"));
                    ps.setDouble(8, rs.getDouble("kol_day"));
                    ps.setInt(9, rs.getInt("id_spec"));
                    ps.setInt(10, rs.getInt("id_empl_ins"));
                    ps.setDate(11, rs.getDate("date_ins"));
                    ps.setDouble(12, rs.getDouble("dekada1"));
                    ps.setDouble(13, rs.getDouble("dekada2"));
                    ps.setDouble(14, rs.getDouble("dekada3"));
                    ps.setString(15, rs.getString("note_item"));
                    ps.execute();
                }
            } else
                throw new Exception();

            commit();

        } catch (Exception e) {
            rollBack();
            System.err.println("Ошибка addTempPlanTable() " + e);
            log.error("Ошибка addTempPlanTable()", e);
            throw new Exception("Ошибка addTempPlanTable() " + e.getMessage(), e);
        } finally {
            setAutoCommit(true);
        }
        return idPlan;
    }

    /**
     * Добавляет проект план производства в БД из временных таблиц
     * @param namePlan
     * @param idDept
     * @param sDate
     * @param status
     * @param note
     * @param idEmploye
     * @return
     * @throws Exception
     */
    public int addTempProjectTable(String namePlan, int idDept, long sDate, int status, String note, int idEmploye, int workDays) throws Exception {
        String sql = "";
        int idPlan = 0;

        try {
            setAutoCommit(false);

            sql = "Insert into plan(id_dept, plan_name, plan_date, id_empl_vvod, id_empl_ins, note, status, workdays) "
                    + " values( ?, ?, ?, ?, ?, ?, ?, ?) returning id";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, idDept);
            ps.setString(2, namePlan);
            ps.setDate(3, new java.sql.Date(sDate));
            ps.setInt(4, idEmploye);
            ps.setInt(5, idEmploye);
            ps.setString(6, note);
            ps.setInt(7, status);
            ps.setInt(8, workDays);
            rs = ps.executeQuery();

            if (rs.next()) {
                idPlan = rs.getInt(1);

                sql = "Select cx, sar, fas, rst, rzm, kol_month, kol_day, id_spec, id_empl_ins, date_ins,"
                        + " dekada1, dekada2, dekada3, note_item, fas_vid, fas_pname, kol_x, nar "
                        + " From tmp_plan_item ";
                ps = conn.prepareStatement(sql);
                rs = ps.executeQuery();

                while (rs.next()) {

                    sql = "Insert into plan_item(id_plan, cx, sar, fas, rst, rzm, kol_month, kol_day, id_spec, id_empl_ins, date_ins, "
                            + "                  dekada1, dekada2, dekada3, note_item, fas_vid, fas_pname, kol_x, nar) "
                            + "values( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

                    ps = conn.prepareStatement(sql);
                    ps.setInt(1, idPlan);
                    ps.setInt(2, rs.getInt("cx"));
                    ps.setInt(3, rs.getInt("sar"));
                    ps.setInt(4, rs.getInt("fas"));
                    ps.setInt(5, rs.getInt("rst"));
                    ps.setInt(6, rs.getInt("rzm"));
                    ps.setDouble(7, rs.getDouble("kol_month"));
                    ps.setDouble(8, rs.getDouble("kol_day"));
                    ps.setInt(9, rs.getInt("id_spec"));
                    ps.setInt(10, rs.getInt("id_empl_ins"));
                    ps.setDate(11, rs.getDate("date_ins"));
                    ps.setDouble(12, rs.getDouble("dekada1"));
                    ps.setDouble(13, rs.getDouble("dekada2"));
                    ps.setDouble(14, rs.getDouble("dekada3"));
                    ps.setString(15, rs.getString("note_item"));
                    ps.setInt(16, rs.getInt("fas_vid"));
                    ps.setString(17, rs.getString("fas_pname"));
                    ps.setInt(18, rs.getInt("kol_x"));
                    ps.setString(19, rs.getString("nar"));
                    ps.execute();
                }

                sql = "Select distinct fas From tmp_plan_fas_new ";
                ps = conn.prepareStatement(sql);
                rs = ps.executeQuery();

                while (rs.next()) {
                    sql = "Insert into plan_fas_new(id_plan, fas) values(?, ?)";

                    ps = conn.prepareStatement(sql);
                    ps.setInt(1, idPlan);
                    ps.setInt(2, rs.getInt("fas"));
                    ps.execute();
                }

                sql = "Select distinct fas, vid, cx, sar, nar From tmp_plan_fas_dekor ";
                ps = conn.prepareStatement(sql);
                rs = ps.executeQuery();

                while (rs.next()) {
                    sql = "Insert into plan_fas_dekor( id_plan, fas, vid, cx, sar, nar) values(?, ?, ?, ?, ?, ?)";

                    ps = conn.prepareStatement(sql);
                    ps.setInt(1, idPlan);
                    ps.setInt(2, rs.getInt("fas"));
                    ps.setInt(3, rs.getInt("vid"));
                    ps.setInt(4, rs.getInt("cx"));
                    ps.setInt(5, rs.getInt("sar"));
                    ps.setString(6, rs.getString("nar"));
                    ps.execute();
                }

                sql = "Select distinct fas, id_polotno, nar_polotno, sostav, vid, rashod, cx, sar, nar "
                        + " From tmp_plan_fas_sostav ";
                ps = conn.prepareStatement(sql);
                rs = ps.executeQuery();

                while (rs.next()) {
                    sql = "Insert into plan_fas_sostav( id_plan, fas, id_polotno, nar_polotno, sostav, vid, rashod, cx, sar, nar)"
                            + " values( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

                    ps = conn.prepareStatement(sql);
                    ps.setInt(1, idPlan);
                    ps.setInt(2, rs.getInt("fas"));
                    ps.setInt(3, rs.getInt("id_polotno"));
                    ps.setString(4, rs.getString("nar_polotno"));
                    ps.setString(5, rs.getString("sostav"));
                    ps.setInt(6, rs.getInt("vid"));
                    ps.setDouble(7, rs.getDouble("rashod"));
                    ps.setInt(8, rs.getInt("cx"));
                    ps.setInt(9, rs.getInt("sar"));
                    ps.setString(10, rs.getString("nar"));
                    ps.execute();
                }

                sql = "Select distinct fas, id_color, ncw, cx, sar, nar From tmp_plan_fas_color ";
                ps = conn.prepareStatement(sql);
                rs = ps.executeQuery();

                while (rs.next()) {
                    sql = "Insert into plan_fas_color(id_plan, fas, id_color, ncw, cx, sar, nar) "
                            + "        values( ?, ?, ?, ?, ?, ?, ?)";

                    ps = conn.prepareStatement(sql);
                    ps.setInt(1, idPlan);
                    ps.setInt(2, rs.getInt("fas"));
                    ps.setInt(3, rs.getInt("id_color"));
                    ps.setString(4, rs.getString("ncw"));
                    ps.setInt(5, rs.getInt("cx"));
                    ps.setInt(6, rs.getInt("sar"));
                    ps.setString(7, rs.getString("nar"));
                    ps.execute();
                }

            } else
                throw new Exception();

            commit();

        } catch (Exception e) {
            rollBack();
            System.err.println("Ошибка addTempProjectTable() " + e);
            log.error("Ошибка addTempProjectTable()", e);
            throw new Exception("Ошибка addTempProjectTable() " + e.getMessage(), e);
        } finally {
            setAutoCommit(true);
        }
        return idPlan;
    }

    /**
     * Возвращает все планы по заданным параметрам
     * @param idDept
     * @param flagVod
     * @param sDate
     * @param eDate
     * @param flagIns
     * @param sInsDate
     * @param eInsDate
     * @param planNum
     * @param status
     * @return
     * @throws Exception
     */
    public Vector getAllPlans(int idDept, boolean flagVod, long sDate, long eDate,
                              boolean flagIns, long sInsDate, long eInsDate, String planNum, String status) throws Exception {

        Vector elements = new Vector();
        String sql = "";

        try {
            sql = "Select department, plan.id as idPlan, plan_date, plan_name, id_empl_vvod,  fio, date_ins, status "
                    + "             From plan, dept, employees "
                    + "             Where plan.id_empl_vvod = employees.id and "
                    + "                   plan.id_dept = dept.id "
                    + (idDept != -1 ? " and dept.id =" + idDept + " " : " ")
                    + "                   and plan.id::text like '" + planNum + "%' "
                    + "                   and plan.status::text like '" + status + "%' "
                    + "                   and plan.status<>3  "
                    + "                   and plan.status<>-2  "
                    + (flagVod ? " and plan.plan_date between '" + new java.sql.Date(sDate) + " 00:00' "
                    + "                                                    and '" + new java.sql.Date(eDate) + " 23:59' " : " ")
                    + (flagIns ? " and plan.date_ins between '" + new java.sql.Date(sInsDate) + " 00:00' "
                    + "                                                    and '" + new java.sql.Date(eInsDate) + " 23:59' " : " ")
                    + "  Order by department, idPlan ";

            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                Vector tmp = new Vector();
                tmp.add(false);
                tmp.add(rs.getString("department"));
                tmp.add(rs.getInt("idPlan"));
                tmp.add(new SimpleDateFormat("dd-MM-yyyy").format(rs.getDate("plan_date")));
                tmp.add(rs.getString("plan_name"));
                tmp.add(rs.getString("fio"));
                tmp.add(new SimpleDateFormat("dd-MM-yyyy").format(rs.getDate("date_ins")));

                switch (rs.getInt("status")) {
                    case 0:
                        tmp.add("главный");
                        break;
                    case 1:
                        tmp.add("проект");
                        break;
                    case 2:
                        tmp.add("копия");
                        break;
                    case -1:
                        tmp.add("удалён");
                        break;
                    default:
                        tmp.add("неизвестно");
                        break;
                }
                tmp.add(rs.getInt("status"));
                elements.add(tmp);
            }
        } catch (Exception e) {
            elements = new Vector();
            System.err.println("Ошибка getAllPlans() " + e);
            log.error("Ошибка getAllPlans()", e);
            throw new Exception("Ошибка getAllPlans() " + e.getMessage(), e);
        }
        return elements;
    }

    public Vector getAllPlans(String nameType, boolean flagVod, long sDate, long eDate,
                              boolean flagIns, long sInsDate, long eInsDate, String planNum, String status) throws Exception {

        Vector elements = new Vector();
        String sql = "";

        try {
            sql = "Select department, plan.id as idPlan, plan_date, plan_name, id_empl_vvod,  fio, date_ins, status "
                    + "             From plan, dept, employees "
                    + "             Where plan.id_empl_vvod = employees.id and "
                    + "                   plan.id_dept = dept.id "
                    + "                   and plan.plan_name like initcap('%" + nameType + "%') "
                    + "                   and plan.id::text like '" + planNum + "%'  "
                    + "                   and plan.status::text like '" + status + "%' "
                    + "                   and plan.status<>3  "
                    + "                   and plan.status<>-2  "
                    + (flagVod ? " and plan.plan_date between '" + new java.sql.Date(sDate) + " 00:00' "
                    + "                                                    and '" + new java.sql.Date(eDate) + " 23:59' " : " ")
                    + (flagIns ? " and plan.date_ins between '" + new java.sql.Date(sInsDate) + " 00:00' "
                    + "                                                    and '" + new java.sql.Date(eInsDate) + " 23:59' " : " ")
                    + "  Order by department, idPlan ";

            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                Vector tmp = new Vector();
                tmp.add(false);
                tmp.add(rs.getString("department"));
                tmp.add(rs.getInt("idPlan"));
                tmp.add(new SimpleDateFormat("dd-MM-yyyy").format(rs.getDate("plan_date")));
                tmp.add(rs.getString("plan_name"));
                tmp.add(rs.getString("fio"));
                tmp.add(new SimpleDateFormat("dd-MM-yyyy").format(rs.getDate("date_ins")));

                switch (rs.getInt("status")) {
                    case 0:
                        tmp.add("главный");
                        break;
                    case 1:
                        tmp.add("проект");
                        break;
                    case 2:
                        tmp.add("копия");
                        break;
                    case -1:
                        tmp.add("удалён");
                        break;
                    default:
                        tmp.add("неизвестно");
                        break;
                }
                tmp.add(rs.getInt("status"));
                elements.add(tmp);
            }
        } catch (Exception e) {
            elements = new Vector();
            System.err.println("Ошибка getAllPlans() " + e);
            log.error("Ошибка getAllPlans()", e);
            throw new Exception("Ошибка getAllPlans() " + e.getMessage(), e);
        }
        return elements;
    }

    /**
     * Возвращает все проекты плана по заданным параметрам
     * @param flagVod
     * @param sDate
     * @param eDate
     * @param flagIns
     * @param sInsDate
     * @param eInsDate
     * @param projNum
     * @param status
     * @return
     * @throws Exception
     */
    public Vector getAllProject(boolean flagVod, long sDate, long eDate, boolean flagIns, long sInsDate, long eInsDate, String projNum, String status) throws Exception {

        Vector elements = new Vector();
        String sql = "";

        try {
            sql = "Select plan.id as idPlan, plan_date, plan_name, id_empl_vvod,  fio, date_ins, status "
                    + "             From plan, employees "
                    + "             Where plan.id_empl_vvod = employees.id "
                    + "                   and plan.id::text like '" + projNum + "%' "
                    + (status.equals("") ? " and plan.status <> 0 "
                    + "                                       and plan.status <> 2 "
                    + "                                       and plan.status <> -1 "
                    : " and plan.status::text like '" + status + "%' ")
                    + (flagVod ? " and plan.plan_date between '" + new java.sql.Date(sDate) + " 00:00' "
                    + "                                                    and '" + new java.sql.Date(eDate) + " 23:59' " : " ")
                    + (flagIns ? " and plan.date_ins between '" + new java.sql.Date(sInsDate) + " 00:00' "
                    + "                                                    and '" + new java.sql.Date(eInsDate) + " 23:59' " : " ")
                    + "  Order by idPlan ";

            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                Vector tmp = new Vector();
                tmp.add(false);
                tmp.add(rs.getInt("idPlan"));
                tmp.add(rs.getString("plan_name"));
                tmp.add(new SimpleDateFormat("dd-MM-yyyy").format(rs.getDate("plan_date")));
                tmp.add(rs.getString("fio"));
                tmp.add(new SimpleDateFormat("dd-MM-yyyy").format(rs.getDate("date_ins")));

                switch (rs.getInt("status")) {
                    case 1:
                        tmp.add("проект");
                        break;
                    case 3:
                        tmp.add("формируется");
                        break;
                    case -2:
                        tmp.add("удалён");
                        break;
                    default:
                        tmp.add("неизвестно");
                        break;
                }
                tmp.add(rs.getInt("status"));
                elements.add(tmp);
            }
        } catch (Exception e) {
            elements = new Vector();
            System.err.println("Ошибка getAllProject() " + e);
            log.error("Ошибка getAllProject()", e);
            throw new Exception("Ошибка getAllProject() " + e.getMessage(), e);
        }
        return elements;
    }

    /**
     *
     * @param idPlan
     * @return
     * @throws Exception
     */
    public Vector getDataPlan(int idPlan) throws Exception {
        Vector elements = new Vector();
        String sql = "Select dept.id as idDept, department, plan.id as idPlan, plan_date, plan_name, id_empl_vvod, "
                + "         EMP1.fio as vvodFio, date_vvod, plan.id_empl_ins as insEmpl, EMP2.fio as insFio, "
                + "         plan.date_ins as insDate, note, status, sum(kol_month) as kolM, sum(kol_day) as kolD, workdays "
                + " From plan, plan_item, dept, employees as EMP1, employees as EMP2 "
                + " Where plan.id_empl_vvod = EMP1.id and "
                + "       plan.id_empl_ins = EMP2.id and "
                + "       plan.id_dept = dept.id and "
                + "       plan.id = plan_item.id_plan and"
                + "       plan.id = ? "
                + " Group by idDept, department, idPlan, plan_date, plan_name, "
                + "          id_empl_vvod, vvodFio, date_vvod, insEmpl, insFio, insDate, note, status, workdays ";

        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, idPlan);
            rs = ps.executeQuery();
            while (rs.next()) {
                elements.add(rs.getInt("idDept"));
                elements.add(rs.getString("department"));
                elements.add(rs.getInt("idPlan"));
                elements.add(new SimpleDateFormat("dd-MM-yyyy").format(rs.getDate("plan_date")));
                elements.add(rs.getString("plan_name"));
                elements.add(rs.getInt("id_empl_vvod"));
                elements.add(rs.getString("vvodFio"));
                elements.add(new SimpleDateFormat("dd-MM-yyyy").format(rs.getDate("date_vvod")));
                elements.add(rs.getInt("insEmpl"));
                elements.add(rs.getString("insFio"));
                elements.add(new SimpleDateFormat("dd-MM-yyyy").format(rs.getDate("insDate")));
                elements.add(rs.getString("note"));

                switch (rs.getInt("status")) {
                    case 0:
                        elements.add("главный");
                        break;
                    case 1:
                        elements.add("копия НСИ");
                        break;
                    case 2:
                        elements.add("копия");
                        break;
                    case -1:
                        elements.add("удалён");
                        break;
                    default:
                        elements.add("неизвестно");
                        break;
                }
                elements.add(rs.getInt("status"));
                elements.add(rs.getDouble("kolM"));
                elements.add(rs.getDouble("kolD"));
                elements.add(rs.getInt("workdays"));
            }
        } catch (Exception e) {
            elements = new Vector();
            System.err.println("Ошибка getDataPlan() " + e);
            log.error("Ошибка getDataPlan()", e);
            throw new Exception("Ошибка getDataPlan() " + e.getMessage(), e);
        }
        return elements;
    }

    /**
     *
     * @param idPlan
     * @return
     * @throws Exception
     */
    public Vector getDataItemPlan(int idPlan) throws Exception {
        Vector elements = new Vector();
        String sql = "Select fas, kolM, idSpec, modelSpec, nameSpec, sum(round(spec_item.vnorm::numeric," + UtilPlan.ROUNDING_NORM + ")) as vnorm "
                + "             From (Select fas, sum(round(kol_month,3)) as kolM, spec.id as idSpec, "
                + "                          spec.id_model as modelSpec, spec.spec as nameSpec "
                + "                     From plan, plan_item, spec "
                + "                     Where plan.id = plan_item.id_plan and "
                + "                             plan.id = ? and "
                + "                             plan_item.id_spec = spec.id "
                + "                     Group by fas, idSpec, modelSpec, nameSpec) as t1, spec_item "
                + "             Where t1.idSpec = spec_item.id_spec and "
                + "                     spec_item.num >= 0 and "
                + "                     spec_item.id_tech <> -1"
                + "             Group by fas, kolM, idSpec, modelSpec, nameSpec "
                + "             Order by fas, modelSpec, nameSpec ";

        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, idPlan);
            rs = ps.executeQuery();
            while (rs.next()) {
                Vector tmp = new Vector();
                tmp.add(false);
                tmp.add(rs.getInt("idSpec"));
                tmp.add(rs.getInt("modelSpec"));
                tmp.add(rs.getString("nameSpec"));
                tmp.add(rs.getInt("fas"));
                tmp.add(Double.valueOf(UtilFunctions.formatNorm(rs.getDouble("kolM"), 0)));
                tmp.add(UtilFunctions.formatNorm(rs.getDouble("vnorm"), UtilPlan.ROUNDING_NORM));
                //   tmp.add(rs.getDouble("vnorm"));
                tmp.add(Double.valueOf(UtilFunctions.formatNorm(Double.valueOf(rs.getDouble("kolM") * rs.getDouble("vnorm")), 3)));
                elements.add(tmp);
            }

        } catch (Exception e) {
            elements = new Vector();
            System.err.println("Ошибка getDataItemPlan() " + e);
            log.error("Ошибка getDataItemPlan()", e);
            throw new Exception("Ошибка getDataItemPlan() " + e.getMessage(), e);
        }
        return elements;
    }

    public Vector getDataItemProjectStok(int idPlan) throws Exception {
        Vector elements = new Vector();
        String sql = "Select min(rst) as minrst, max(rst) as maxrst,  max(rzm) as maxrzm,  min(rzm) as minrzm, t2.fas_vid as fas_vid, t2.fas_pname as fas_pname, "
                + "	t2.fas as fas, t2.kolM as kolM, t2.kol_x as kol_x, t2.note_item as note_item, t2.sar as sar, t2.nar as nar, polotno, color, idNewFas, dekor "
                + " From(Select plan_item.id_plan as idPlan , fas_vid, fas_pname, fas, sum(round(kol_month,3)) as kolM, kol_x, note_item, sar, nar,"
                + "			(SELECT ARRAY_TO_STRING(ARRAY(SELECT nar_polotno FROM plan_fas_sostav Where plan_item.fas = plan_fas_sostav.fas and "
                + "                                                                                                 plan_fas_sostav.id_plan = plan_item.id_plan and "
                + "                                                                                                 plan_fas_sostav.sar = plan_item.sar and "
                + "                                                                                                 plan_fas_sostav.nar = plan_item.nar "
                + "                                                                                            Order by plan_fas_sostav.vid, nar_polotno),', ')) as polotno,"
                + "			(SELECT ARRAY_TO_STRING(ARRAY(SELECT ncw FROM plan_fas_color Where plan_item.fas = plan_fas_color.fas and "
                + "                                                                                        plan_fas_color.id_plan = plan_item.id_plan and "
                + "                                                                                        plan_fas_color.sar = plan_item.sar and "
                + "                                                                                        plan_fas_color.nar = plan_item.nar "
                + "                                                                                   Order by ncw),', ')) as color,"
                + "			(SELECT distinct id FROM plan_fas_new Where plan_item.fas = plan_fas_new.fas and plan_fas_new.id_plan = plan_item.id_plan) as idNewFas,  "
                + "			(SELECT ARRAY_TO_STRING(ARRAY(SELECT vid FROM plan_fas_dekor Where plan_item.fas = plan_fas_dekor.fas and "
                + "                                                                                         plan_fas_dekor.id_plan = plan_item.id_plan and "
                + "                                                                                         plan_fas_dekor.sar = plan_item.sar and "
                + "                                                                                         plan_fas_dekor.nar = plan_item.nar  "
                + "                                                                                  Order by vid),', ')) as dekor   "
                + "		From plan, plan_item "
                + "		Where plan.id = plan_item.id_plan and  "
                + "			plan.id = ? "
                + "		Group by idPlan, fas_vid, fas_pname, fas, kol_x, note_item, sar, nar ) as t2, plan_item "
                + " Where t2.idPlan = plan_item.id_plan and "
                + "     t2.fas_vid = plan_item.fas_vid and "
                + "	t2.fas_pname = plan_item.fas_pname and "
                + "	t2.fas = plan_item.fas and "
                + "	t2.kol_x = plan_item.kol_x and "
                + "	t2.note_item = plan_item.note_item and "
                + "	t2.sar = plan_item.sar and "
                + "	t2.nar = plan_item.nar "
                + " Group by t2.fas_vid, t2.fas_pname, t2.fas, t2.kolM, t2.kol_x, t2.note_item, t2.sar, t2.nar, polotno, color, idNewFas, dekor "
                + " Order by fas_vid, fas_pname, fas, sar, nar ";

        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, idPlan);
            rs = ps.executeQuery();
            while (rs.next()) {
                Vector tmp = new Vector();
                tmp.add(false);
                tmp.add(rs.getInt("fas_vid"));
                tmp.add(rs.getString("fas_pname"));
                tmp.add(rs.getInt("fas"));
                tmp.add(rs.getString("polotno"));
                tmp.add(Double.valueOf(UtilFunctions.formatNorm(rs.getDouble("kolM"), 0)));
                tmp.add(rs.getInt("kol_x"));
                tmp.add(rs.getString("nar"));
                tmp.add(rs.getString("minrst").equals(rs.getString("maxrst")) ? rs.getString("minrst") : rs.getString("minrst") + "--" + rs.getString("maxrst"));
                tmp.add(rs.getString("minrzm").equals(rs.getString("maxrzm")) ? rs.getString("minrzm") : rs.getString("minrzm") + "--" + rs.getString("maxrzm"));
                tmp.add(rs.getString("color"));
                tmp.add(rs.getString("dekor"));
                tmp.add(rs.getString("note_item"));

                if (rs.getObject("idNewFas") != null) tmp.add(UtilPlan.NEW);
                else tmp.add("");

                elements.add(tmp);
            }

        } catch (Exception e) {
            elements = new Vector();
            System.err.println("Ошибка getDataItemProjectStok() " + e);
            log.error("Ошибка getDataItemProjectStok()", e);
            throw new Exception("Ошибка getDataItemProjectStok() " + e.getMessage(), e);
        }
        return elements;
    }

    /**
     *
     * @param idPlan
     * @return
     * @throws Exception
     */
    public Vector getDataItemPlanDetal(int idPlan) throws Exception {
        Vector elements = new Vector();
        String sql = "Select idPlanItem, cx, sar, t2.fas as fas, rst, rzm, kolM, kolD, idSpec, nameSpec, "
                + "     vnorm, idEmployees, fio, insDate, dekada1, dekada2, dekada3, note_item, plan_fas_new.id as idNewFas "
                + " From (Select plan_item.id as idPlanItem, cx, sar, fas, rst, rzm, round(kol_month,3) as kolM, round(kol_day,3) as kolD, "
                + "             spec.id as idSpec, spec.spec as nameSpec, sum(round(spec_item.vnorm::numeric," + UtilPlan.ROUNDING_NORM + ")) as vnorm, "
                + "             employees.id as idEmployees, fio, plan_item.date_ins as insDate, dekada1, dekada2, dekada3, note_item  "
                + "         From plan, plan_item, spec, spec_item, employees "
                + "         Where plan.id = plan_item.id_plan and "
                + "                 spec.id = spec_item.id_spec and "
                + "                 plan_item.id_spec = spec.id and "
                + "                 plan_item.id_empl_ins = employees.id and "
                + "                 plan.id = ? and "
                + "                 spec_item.num >= 0 and "
                + "                 spec_item.id_tech <> -1 "
                + "         Group by idPlanItem, cx, sar, fas, rst, rzm, kolM, kolD, idSpec, nameSpec, idEmployees, fio, insDate, dekada1, dekada2, dekada3, note_item) as t2 "
                + " left join plan_fas_new "
                + " on t2.fas = plan_fas_new.fas and "
                + "     plan_fas_new.id_plan = ? "
                + " Order by fas, sar, rst, rzm, idEmployees, fio, insDate, cx ";

        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, idPlan);
            ps.setInt(2, idPlan);
            rs = ps.executeQuery();
            while (rs.next()) {
                Vector tmp = new Vector();
                tmp.add(false);
                tmp.add(rs.getInt("idPlanItem"));
                tmp.add(rs.getInt("sar"));
                tmp.add(rs.getInt("fas"));
                tmp.add(rs.getInt("rst"));
                tmp.add(rs.getInt("rzm"));
                tmp.add(rs.getDouble("kolM"));
                tmp.add(rs.getDouble("kolD"));
                tmp.add(rs.getInt("idSpec"));
                tmp.add(rs.getString("nameSpec"));
                tmp.add(UtilFunctions.formatNorm(rs.getDouble("vnorm"), UtilPlan.ROUNDING_NORM));
                //tmp.add(rs.getDouble("vnorm"));                
                tmp.add(Double.valueOf(UtilFunctions.formatNorm(Double.valueOf(rs.getDouble("kolM") * rs.getDouble("vnorm")), 3)));
                tmp.add(rs.getInt("idEmployees"));
                tmp.add(rs.getString("fio"));
                tmp.add(new SimpleDateFormat("dd-MM-yyyy").format(rs.getDate("insDate")));
                tmp.add(rs.getInt("cx"));
                tmp.add(UtilFunctions.formatNorm(rs.getDouble("dekada1"), 2));
                tmp.add(UtilFunctions.formatNorm(rs.getDouble("dekada2"), 2));
                tmp.add(UtilFunctions.formatNorm(rs.getDouble("dekada3"), 2));
                tmp.add(rs.getString("note_item"));

                if (rs.getObject("idNewFas") != null) tmp.add(UtilPlan.NEW);
                else tmp.add("");

                elements.add(tmp);
            }

        } catch (Exception e) {
            elements = new Vector();
            System.err.println("Ошибка getDataItemPlanDetal() " + e);
            log.error("Ошибка getDataItemPlanDetal()", e);
            throw new Exception("Ошибка getDataItemPlanDetal() " + e.getMessage(), e);
        }
        return elements;
    }

    public Vector getDataItemProjectDetal(int idPlan) throws Exception {
        Vector elements = new Vector();
        String sql = "Select fas_vid, fas_pname, fas, kolM, kol_x, note_item, cx, idSpec, nameSpec, sar, nar, sum(round(spec_item.vnorm::numeric," + UtilPlan.ROUNDING_NORM + ")) as vnorm,  polotno, color, idNewFas, dekor "
                + "	From (Select plan_item.id_plan as idPlan , fas_vid, fas_pname, fas, sum(round(kol_month,3)) as kolM, kol_x, note_item, cx, spec.id as idSpec, "
                + "			spec.id_model as modelSpec, spec.spec as nameSpec, sar, nar, "
                + "			(SELECT ARRAY_TO_STRING(ARRAY(SELECT nar_polotno FROM plan_fas_sostav Where plan_item.fas = plan_fas_sostav.fas and "
                + "                                                                                                 plan_fas_sostav.id_plan = plan_item.id_plan and "
                + "                                                                                                 plan_fas_sostav.cx = plan_item.cx and "
                + "                                                                                                 plan_fas_sostav.sar = plan_item.sar and "
                + "                                                                                                 plan_fas_sostav.nar = plan_item.nar "
                + "                                                                                           Order by plan_fas_sostav.vid, nar_polotno),', ')) as polotno,"
                + "			(SELECT ARRAY_TO_STRING(ARRAY(SELECT ncw FROM plan_fas_color Where plan_item.fas = plan_fas_color.fas and "
                + "                                                                                        plan_fas_color.id_plan = plan_item.id_plan and "
                + "                                                                                        plan_fas_color.cx = plan_item.cx and "
                + "                                                                                        plan_fas_color.sar = plan_item.sar and "
                + "                                                                                        plan_fas_color.nar = plan_item.nar "
                + "                                                                                  Order by ncw),', ')) as color,"
                + "			(SELECT distinct id FROM plan_fas_new Where plan_item.fas = plan_fas_new.fas and plan_fas_new.id_plan = plan_item.id_plan) as idNewFas,  "
                + "			(SELECT ARRAY_TO_STRING(ARRAY(SELECT vid FROM plan_fas_dekor Where plan_item.fas = plan_fas_dekor.fas and "
                + "                                                                                        plan_fas_dekor.id_plan = plan_item.id_plan and "
                + "                                                                                        plan_fas_dekor.cx = plan_item.cx and "
                + "                                                                                        plan_fas_dekor.sar = plan_item.sar and "
                + "                                                                                        plan_fas_dekor.nar = plan_item.nar "
                + "                                                                                  Order by vid),', ')) as dekor   "
                + "		From plan, plan_item, spec "
                + "		Where plan.id = plan_item.id_plan and "
                + "			plan.id = ? and "
                + "			plan_item.id_spec = spec.id "
                + "		Group by idPlan, fas_vid, fas_pname, fas, kol_x, note_item, cx, idSpec, modelSpec, nameSpec, sar, nar ) as t1, spec_item  "
                + "	Where t1.idSpec = spec_item.id_spec and "
                + "		spec_item.num >= 0 and  "
                + "		spec_item.id_tech <> -1 "
                + "	Group by fas_vid, fas_pname, fas, kolM, kol_x, note_item, cx, idSpec, modelSpec, nameSpec, sar, nar, polotno, color , idNewFas, dekor "
                + "     Order by fas, fas_pname, sar, nar ";
        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, idPlan);
            rs = ps.executeQuery();
            while (rs.next()) {
                Vector tmp = new Vector();
                tmp.add(false);
                tmp.add(rs.getInt("fas_vid"));
                tmp.add(rs.getString("fas_pname"));
                tmp.add(rs.getInt("fas"));
                tmp.add(rs.getString("polotno"));
                tmp.add(Double.valueOf(UtilFunctions.formatNorm(rs.getDouble("kolM"), 0)));
                tmp.add(rs.getInt("kol_x"));
                tmp.add(rs.getInt("idSpec"));
                tmp.add(rs.getString("nameSpec"));
                tmp.add(UtilFunctions.formatNorm(rs.getDouble("vnorm"), UtilPlan.ROUNDING_NORM));
                tmp.add(Double.valueOf(UtilFunctions.formatNorm(Double.valueOf(rs.getDouble("kolM") * rs.getDouble("vnorm")), 3)));
                tmp.add(rs.getString("color"));
                tmp.add(rs.getString("dekor"));
                tmp.add(rs.getInt("cx"));
                tmp.add(rs.getString("note_item"));
                tmp.add(rs.getInt("sar"));
                tmp.add(rs.getString("nar"));
                if (rs.getObject("idNewFas") != null) tmp.add(UtilPlan.NEW);
                else tmp.add("");

                elements.add(tmp);
            }

        } catch (Exception e) {
            elements = new Vector();
            System.err.println("Ошибка getDataItemProjectDetal() " + e);
            log.error("Ошибка getDataItemProjectDetal()", e);
            throw new Exception("Ошибка getDataItemProjectDetal() " + e.getMessage(), e);
        }
        return elements;
    }

    /**
     *
     * @param idPlan
     * @return
     * @throws Exception
     */
    public Vector getDataItemProject(int idPlan) throws Exception {
        Vector elements = new Vector();
        String sql = "Select plan_work_model.note as noteEmpl, minrst, maxrst, maxrzm, minrzm, fas_vid, fas_pname, fas, kolM, kol_x, note_item, sar, nar, cx, polotno, color, idNewFas, dekor"
                + " From (Select min(rst) as minrst, max(rst) as maxrst,  max(rzm) as maxrzm,  min(rzm) as minrzm, t2.fas_vid as fas_vid, t2.fas_pname as fas_pname, "
                + "             t2.fas as fas, t2.kolM as kolM, t2.kol_x as kol_x, t2.note_item as note_item, t2.sar as sar, t2.nar as nar, t2.cx as cx, polotno, color, idNewFas, dekor "
                + "         From(Select plan_item.id_plan as idPlan , fas_vid, fas_pname, fas, sum(round(kol_month,3)) as kolM, kol_x, note_item, sar, nar, cx, "
                + "			(SELECT ARRAY_TO_STRING(ARRAY(SELECT nar_polotno FROM plan_fas_sostav Where plan_item.fas = plan_fas_sostav.fas and "
                + "                                                                                                 plan_fas_sostav.id_plan = plan_item.id_plan and "
                + "                                                                                                 plan_fas_sostav.sar = plan_item.sar and "
                + "                                                                                                 plan_fas_sostav.nar = plan_item.nar "
                + "                                                                                            Order by plan_fas_sostav.vid, nar_polotno),', ')) as polotno,"
                + "			(SELECT ARRAY_TO_STRING(ARRAY(SELECT ncw FROM plan_fas_color Where plan_item.fas = plan_fas_color.fas and "
                + "                                                                                        plan_fas_color.id_plan = plan_item.id_plan and "
                + "                                                                                        plan_fas_color.sar = plan_item.sar and "
                + "                                                                                        plan_fas_color.nar = plan_item.nar "
                + "                                                                                   Order by ncw),', ')) as color,"
                + "			(SELECT distinct id FROM plan_fas_new Where plan_item.fas = plan_fas_new.fas and plan_fas_new.id_plan = plan_item.id_plan) as idNewFas,  "
                + "			(SELECT ARRAY_TO_STRING(ARRAY(SELECT vid FROM plan_fas_dekor Where plan_item.fas = plan_fas_dekor.fas and "
                + "                                                                                         plan_fas_dekor.id_plan = plan_item.id_plan and "
                + "                                                                                         plan_fas_dekor.sar = plan_item.sar and "
                + "                                                                                         plan_fas_dekor.nar = plan_item.nar  "
                + "                                                                                  Order by vid),', ')) as dekor   "
                + "                 From plan, plan_item "
                + "                 Where plan.id = plan_item.id_plan and  "
                + "                         plan.id = ? "
                + "                 Group by idPlan, fas_vid, fas_pname, fas, kol_x, note_item, sar, nar, cx ) as t2, plan_item "
                + "         Where t2.idPlan = plan_item.id_plan and "
                + "                 t2.fas_vid = plan_item.fas_vid and "
                + "                 t2.fas_pname = plan_item.fas_pname and "
                + "                 t2.fas = plan_item.fas and "
                + "                 t2.kol_x = plan_item.kol_x and "
                + "                 t2.note_item = plan_item.note_item and "
                + "                 t2.sar = plan_item.sar and "
                + "                 t2.nar = plan_item.nar "
                + "     Group by t2.fas_vid, t2.fas_pname, t2.fas, t2.kolM, t2.kol_x, t2.note_item, t2.sar, t2.nar, t2.cx, polotno, color, idNewFas, dekor ) as t3 "
                + " left join plan_work_model "
                + " on t3.fas = plan_work_model.id_model "
                + " Order by fas_vid, fas_pname, fas, sar, nar, cx ";

        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, idPlan);
            rs = ps.executeQuery();
            while (rs.next()) {
                Vector tmp = new Vector();
                tmp.add(false);
                tmp.add(rs.getInt("fas_vid"));
                tmp.add((rs.getObject("noteEmpl") != null) ? rs.getString("noteEmpl") : " ");
                tmp.add(rs.getString("fas_pname"));
                tmp.add(rs.getInt("fas"));
                tmp.add(rs.getString("nar"));
                tmp.add("");
                tmp.add(rs.getString("minrst").equals(rs.getString("maxrst")) ? rs.getString("minrst") : rs.getString("minrst") + "--" + rs.getString("maxrst"));
                tmp.add(rs.getString("minrzm").equals(rs.getString("maxrzm")) ? rs.getString("minrzm") : rs.getString("minrzm") + "--" + rs.getString("maxrzm"));
                tmp.add(rs.getString("polotno"));
                tmp.add(rs.getString("dekor"));
                tmp.add(Double.valueOf(UtilFunctions.formatNorm(rs.getDouble("kolM"), 0)));
                tmp.add(rs.getInt("kol_x"));
                tmp.add("");
                tmp.add("");
                tmp.add(rs.getInt("sar"));
                tmp.add(rs.getInt("cx"));
                tmp.add(rs.getString("note_item"));
                tmp.add((rs.getObject("idNewFas") != null) ? tmp.add(UtilPlan.NEW) : "");
                tmp.add(false);

                elements.add(tmp);
            }

        } catch (Exception e) {
            elements = new Vector();
            System.err.println("Ошибка getDataItemProject() " + e);
            log.error("Ошибка getDataItemProject()", e);
            throw new Exception("Ошибка getDataItemProject() " + e.getMessage(), e);
        }
        return elements;
    }

    /**
     *
     * @param idPlan
     * @return
     * @throws Exception
     */
    public Vector getDataItemProjectWork(int idPlan, int idEmpl) throws Exception {
        Vector elements = new Vector();
        String sql = " SELECT plan_work.id as idWork, fas, nar, polotno, note, "
                + "       id_empl_ins, EMP2.fio as insFio, date_ins "
                + " FROM plan_work, employees as EMP2  "
                + " WHERE plan_work.id_plan = ? and "
                + "       plan_work.id_empl_ins = ? and "
                + "       plan_work.id_empl_ins = EMP2.id  "
                + " ORDER BY fas, nar ";

        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, idPlan);
            ps.setInt(2, idEmpl);
            rs = ps.executeQuery();

            while (rs.next()) {
                Vector tmp = new Vector();
                tmp.add(false);
                tmp.add(rs.getInt("idWork"));
                tmp.add(1);
                tmp.add(rs.getString("fas"));
                tmp.add(rs.getString("nar"));
                tmp.add("");
                tmp.add(rs.getString("polotno"));
                tmp.add(rs.getString("note"));
                tmp.add(new SimpleDateFormat("dd-MM-yyyy").format(rs.getDate("date_ins")));
                tmp.add(rs.getString("insFio"));

                elements.add(tmp);
            }

        } catch (Exception e) {
            elements = new Vector();
            System.err.println("Ошибка getDataItemProjectWork() " + e);
            log.error("Ошибка getDataItemProjectWork()", e);
            throw new Exception("Ошибка getDataItemProjectWork() " + e.getMessage(), e);
        }
        return elements;
    }

    /**
     *
     * @param idPlan
     * @return
     * @throws Exception
     */
    public Vector getDataItemPlanConv(int idPlan) throws Exception {
        Vector elements = new Vector();
        String sql = "Select cx, t2.fas as fas, kolM, idSpec, modelSpec, nameSpec, vnorm, plan_fas_new.id as idNewFas "
                + " From (Select cx, fas, kolM, idSpec, modelSpec, nameSpec, sum(round(spec_item.vnorm::numeric," + UtilPlan.ROUNDING_NORM + ")) as vnorm "
                + "         From (Select cx, fas, sum(round(kol_month,3)) as kolM, spec.id as idSpec, "
                + "                         spec.id_model as modelSpec, spec.spec as nameSpec "
                + "                 From plan, plan_item, spec "
                + "                 Where plan.id = plan_item.id_plan and "
                + "                         plan.id = ? and "
                + "                         plan_item.id_spec = spec.id "
                + "                 Group by cx, fas, idSpec, modelSpec, nameSpec) as t1, spec_item "
                + "         Where t1.idSpec = spec_item.id_spec and "
                + "                 spec_item.num >= 0 and "
                + "                 spec_item.id_tech <> -1"
                + "         Group by cx, fas, kolM, idSpec, modelSpec, nameSpec ) as t2 "
                + "     left join plan_fas_new "
                + "     on t2.fas = plan_fas_new.fas and "
                + "         plan_fas_new.id_plan = ? "
                + " Order by fas, cx, modelSpec, nameSpec ";

        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, idPlan);
            ps.setInt(2, idPlan);
            rs = ps.executeQuery();
            while (rs.next()) {
                Vector tmp = new Vector();
                tmp.add(false);
                tmp.add(rs.getInt("cx"));
                tmp.add(rs.getInt("idSpec"));
                tmp.add(rs.getInt("modelSpec"));
                tmp.add(rs.getString("nameSpec"));
                tmp.add(rs.getInt("fas"));
                tmp.add(Double.valueOf(UtilFunctions.formatNorm(rs.getDouble("kolM"), 0)));
                tmp.add(UtilFunctions.formatNorm(rs.getDouble("vnorm"), UtilPlan.ROUNDING_NORM));
                //   tmp.add(rs.getDouble("vnorm"));
                tmp.add(Double.valueOf(UtilFunctions.formatNorm(Double.valueOf(rs.getDouble("kolM") * rs.getDouble("vnorm")), 3)));

                if (rs.getObject("idNewFas") != null) tmp.add(UtilPlan.NEW);
                else tmp.add("");

                elements.add(tmp);
            }

        } catch (Exception e) {
            elements = new Vector();
            System.err.println("Ошибка getDataItemPlanConv() " + e);
            log.error("Ошибка getDataItemPlanConv()", e);
            throw new Exception("Ошибка getDataItemPlanConv() " + e.getMessage(), e);
        }
        return elements;
    }

    public Vector getDataItemProjectVnorm(int idPlan) throws Exception {
        Vector elements = new Vector();
        String sql = "Select fas_vid, fas_pname, fas, kolM, kol_x, note_item, idSpec, modelSpec, nameSpec, sar, nar, sum(round(spec_item.vnorm::numeric," + UtilPlan.ROUNDING_NORM + ")) as vnorm,  polotno, idNewFas  "
                + "	From (Select plan_item.id_plan as idPlan , fas_vid, fas_pname, fas, sum(round(kol_month,3)) as kolM, kol_x, note_item, spec.id as idSpec,   "
                + "			spec.id_model as modelSpec, spec.spec as nameSpec, sar, nar, "
                + "			(SELECT ARRAY_TO_STRING(ARRAY(SELECT nar_polotno FROM plan_fas_sostav Where plan_item.fas = plan_fas_sostav.fas and "
                + "                                                                                                 plan_fas_sostav.id_plan = plan_item.id_plan and "
                + "                                                                                                 plan_fas_sostav.sar = plan_item.sar and "
                + "                                                                                                 plan_fas_sostav.nar = plan_item.nar  "
                + "                                                                                           Order by plan_fas_sostav.vid, nar_polotno),', ')) as polotno,"
                + "			(SELECT distinct id FROM plan_fas_new Where plan_item.fas = plan_fas_new.fas and plan_fas_new.id_plan = plan_item.id_plan) as idNewFas  "
                + "		From plan, plan_item, spec "
                + "		Where plan.id = plan_item.id_plan and  "
                + "			plan.id = ? and   "
                + "			plan_item.id_spec = spec.id "
                + "		Group by idPlan, fas_vid, fas_pname, fas, kol_x, note_item, idSpec, modelSpec, nameSpec, sar, nar ) as t1, spec_item   "
                + "	Where t1.idSpec = spec_item.id_spec and  "
                + "		spec_item.num >= 0 and "
                + "		spec_item.id_tech <> -1 "
                + "	Group by fas_vid, fas_pname, fas, kolM, kol_x, note_item, idSpec, modelSpec, nameSpec, sar, nar, polotno, idNewFas "
                + " Order by fas, sar, nar, modelSpec, nameSpec ";

        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, idPlan);
            rs = ps.executeQuery();
            while (rs.next()) {
                Vector tmp = new Vector();
                tmp.add(false);
                tmp.add(rs.getInt("fas_vid"));
                tmp.add(rs.getString("fas_pname"));
                tmp.add(rs.getInt("fas"));
                tmp.add(rs.getString("polotno"));
                tmp.add(Double.valueOf(UtilFunctions.formatNorm(rs.getDouble("kolM"), 0)));
                tmp.add(rs.getInt("kol_x"));
                tmp.add(rs.getInt("idSpec"));
                tmp.add(rs.getInt("modelSpec"));
                tmp.add(rs.getString("nameSpec"));
                tmp.add(UtilFunctions.formatNorm(rs.getDouble("vnorm"), UtilPlan.ROUNDING_NORM));
                tmp.add(Double.valueOf(UtilFunctions.formatNorm(Double.valueOf(rs.getDouble("kolM") * rs.getDouble("vnorm")), 3)));

                if (rs.getObject("idNewFas") != null) tmp.add(UtilPlan.NEW);
                else tmp.add("");

                elements.add(tmp);
            }

        } catch (Exception e) {
            elements = new Vector();
            System.err.println("Ошибка getDataItemProjectVnorm() " + e);
            log.error("Ошибка getDataItemProjectVnorm()", e);
            throw new Exception("Ошибка getDataItemProjectVnorm() " + e.getMessage(), e);
        }
        return elements;
    }

    /**
     *
     * @param idPlan
     * @return
     * @throws Exception
     */
    public Vector getDataItemPlanDekad(int idPlan) throws Exception {
        Vector elements = new Vector();
        String sql = "Select t2.fas as fas, fas_pname, id_spec, spec, kolM, dekada1, dekada2, dekada3, cx, plan_fas_new.id as idNewFas "
                + "     From (Select fas, fas_pname, id_spec, spec, kolM, dekada1, dekada2, dekada3, cx "
                + "             From (Select cx, fas, fas_pname, id_spec, spec, sum(round(kol_month,3)) as kolM, dekada1, dekada2, dekada3 "
                + "                     From plan, plan_item, spec "
                + "                     Where plan.id = plan_item.id_plan and "
                + "                             plan.id = ? and "
                + "                             plan_item.id_spec = spec.id  "
                + "                     Group by cx, fas, fas_pname, id_spec, spec, dekada1, dekada2, dekada3) as t1 "
                + "             left join s_model "
                + "             on fas = s_model.model 	) as t2 "
                + "     left join plan_fas_new "
                + "     on t2.fas = plan_fas_new.fas and "
                + "         plan_fas_new.id_plan = ? "
                + " Order by fas, fas_pname, cx";

        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, idPlan);
            ps.setInt(2, idPlan);
            rs = ps.executeQuery();
            while (rs.next()) {
                Vector tmp = new Vector();
                tmp.add(false);
                tmp.add(rs.getInt("id_spec"));
                tmp.add(rs.getString("spec"));
                tmp.add((rs.getObject("fas_pname") == null) ? "" : rs.getString("fas_pname"));
                tmp.add(rs.getInt("fas"));
                tmp.add(Double.valueOf(UtilFunctions.formatNorm(rs.getDouble("kolM"), 0)));
                tmp.add(rs.getDouble("dekada1"));
                tmp.add(Double.valueOf(UtilFunctions.formatNorm(Double.valueOf(rs.getDouble("kolM") * (rs.getDouble("dekada1") / 100)), 3)));
                tmp.add(rs.getDouble("dekada2"));
                tmp.add(Double.valueOf(UtilFunctions.formatNorm(Double.valueOf(rs.getDouble("kolM") * (rs.getDouble("dekada2") / 100)), 3)));
                tmp.add(rs.getDouble("dekada3"));
                tmp.add(Double.valueOf(UtilFunctions.formatNorm(Double.valueOf(rs.getDouble("kolM") * (rs.getDouble("dekada3") / 100)), 3)));
                tmp.add(rs.getInt("cx"));

                if (rs.getObject("idNewFas") != null) tmp.add(UtilPlan.NEW);
                else tmp.add("");

                elements.add(tmp);
            }

        } catch (Exception e) {
            elements = new Vector();
            System.err.println("Ошибка getDataItemPlanDekad() " + e);
            log.error("Ошибка getDataItemPlanDekad()", e);
            throw new Exception("Ошибка getDataItemPlanDekad() " + e.getMessage(), e);
        }
        return elements;
    }

    public Vector getDataItemProjectDekad(int idPlan) throws Exception {
        Vector elements = new Vector();
        String sql = "Select plan_item.id_plan as idPlan , fas_vid, fas_pname, fas, sar, nar, sum(round(kol_month,3)) as kolM, kol_x, dekada1, dekada2, dekada3,  "
                + "		(SELECT ARRAY_TO_STRING(ARRAY(SELECT nar_polotno FROM plan_fas_sostav Where plan_item.fas = plan_fas_sostav.fas and "
                + "                                                                                         plan_fas_sostav.id_plan = plan_item.id_plan and "
                + "                                                                                         plan_fas_sostav.sar = plan_item.sar and "
                + "                                                                                         plan_fas_sostav.nar = plan_item.nar  "
                + "                                                                                    Order by plan_fas_sostav.vid, nar_polotno),', ')) as polotno,"
                + "		(SELECT ARRAY_TO_STRING(ARRAY(SELECT ncw FROM plan_fas_color Where plan_item.fas = plan_fas_color.fas and "
                + "                                                                                 plan_fas_color.id_plan = plan_item.id_plan and "
                + "                                                                                 plan_fas_color.sar = plan_item.sar and "
                + "                                                                                 plan_fas_color.nar = plan_item.nar  "
                + "                                                                             Order by ncw),', ')) as color,"
                + "		(SELECT distinct id FROM plan_fas_new Where plan_item.fas = plan_fas_new.fas and plan_fas_new.id_plan = plan_item.id_plan) as idNewFas  "
                + "	From plan, plan_item "
                + "	Where plan.id = plan_item.id_plan and "
                + "		plan.id = ?  "
                + "	Group by idPlan, fas_vid, fas_pname, fas, sar, nar, kol_x, dekada1, dekada2, dekada3, idNewFas  "
                + "     Order by fas, fas_pname, sar, nar ";

        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, idPlan);
            rs = ps.executeQuery();
            while (rs.next()) {
                Vector tmp = new Vector();
                tmp.add(false);
                tmp.add(rs.getInt("fas_vid"));
                tmp.add(rs.getString("fas_pname"));
                tmp.add(rs.getInt("fas"));
                tmp.add(rs.getString("polotno"));
                tmp.add(Double.valueOf(UtilFunctions.formatNorm(rs.getDouble("kolM"), 0)));
                tmp.add(rs.getInt("kol_x"));
                tmp.add(rs.getDouble("dekada1"));
                tmp.add(Double.valueOf(UtilFunctions.formatNorm(Double.valueOf(rs.getDouble("kolM") * (rs.getDouble("dekada1") / 100)), 3)));
                tmp.add(rs.getDouble("dekada2"));
                tmp.add(Double.valueOf(UtilFunctions.formatNorm(Double.valueOf(rs.getDouble("kolM") * (rs.getDouble("dekada2") / 100)), 3)));
                tmp.add(rs.getDouble("dekada3"));
                tmp.add(Double.valueOf(UtilFunctions.formatNorm(Double.valueOf(rs.getDouble("kolM") * (rs.getDouble("dekada3") / 100)), 3)));

                if (rs.getObject("idNewFas") != null) tmp.add(UtilPlan.NEW);
                else tmp.add("");

                elements.add(tmp);
            }

        } catch (Exception e) {
            elements = new Vector();
            System.err.println("Ошибка getDataItemProjectDekad() " + e);
            log.error("Ошибка getDataItemProjectDekad()", e);
            throw new Exception("Ошибка getDataItemProjectDekad() " + e.getMessage(), e);
        }
        return elements;
    }

    /**
     *
     * @param idPlan
     * @return
     * @throws Exception
     */
    public boolean updateSpecInPlanProduction(int idPlan) throws Exception {
        String sql = "";
        try {
            setAutoCommit(false);

            sql = "Update plan_item set id_spec = 1 where id_plan = ?";

            ps = conn.prepareStatement(sql);
            ps.setInt(1, idPlan);
            ps.execute(); 
            
            /*
            sql = "Update plan_item "
                    + " Set id_spec = temp_spec.idSpec "
                    + " From (Select idSpec, currentspec.idDept as idDept, t1.model as model, minvnorm "
                    + "         From (Select idDept as idDept, model, min(vnorm) as minvnorm "
                    + "                 From currentspec	"
                    + "                 Group by idDept, model) as t1, currentspec "
                    + "         Where 	minvnorm = vnorm and "
                    + "                 t1.model = currentspec.model "
                    + "         Order by model) as temp_spec, plan"
                    + " Where plan.id = plan_item.id_plan and "
                    + "         plan.id_dept = temp_spec.idDept and "
                    + "         plan_item.fas = temp_spec.model and "
                    + "         plan.id = ? ";
             */

            sql = "     SET enable_indexscan=false; "
                    + " SET enable_seqscan=false;   "
                    + "                             "
                    + " Update plan_item  "
                    + " Set id_spec = temp_spec.idSpec "
                    + " From (	Select idSpec, currentspec.idDept as idDept, t1.model as model, vnorm "
                    + "	        From (Select idDept as idDept, model, max(date_start) as maxdate "
                    + "	               From (SELECT spec.id_model AS model, "
                    + "		                    spec.id_dept AS iddept, "
                    + "				    spec.id AS idspec, "
                    + "			            spec.date_start"
                    + "			      FROM spec ) as cu "
                    + "		       Group by idDept, model"
                    + "		       Order by model) as t1, currentspec "
                    + "	         Where   maxdate = date_start and "
                    + "		         t1.model = currentspec.model "
                    + "	         Order by model) as temp_spec, plan"
                    + " Where plan_item.id_plan = ? and "
                    + "	      plan.id = plan_item.id_plan and "
                    + "       plan.id_dept = temp_spec.idDept and "
                    + "       plan_item.fas = temp_spec.model ";

            ps = conn.prepareStatement(sql);
            ps.setInt(1, idPlan);
            ps.execute();

            commit();
        } catch (Exception e) {
            rollBack();
            System.err.println("Ошибка updateSpecInPlanProduction() " + e);
            log.error("Ошибка updateSpecInPlanProduction()", e);
            throw new Exception("Ошибка updateSpecInPlanProduction() " + e.getMessage(), e);
        } finally {
            setAutoCommit(true);
        }
        return true;
    }

    /**
     *
     * @param idPlan
     * @param type
     * @param data
     * @return
     * @throws Exception
     */
    public boolean updateNarInPlanProduction(int idPlan, int type, Vector data) throws Exception {
        String sql = "";
        try {
            setAutoCommit(false);

            if (type == 1) {

                sql = "Update plan_item set nar = '' where id_plan = ? ";

                ps = conn.prepareStatement(sql);
                ps.setInt(1, idPlan);
                ps.execute();

                sql = "Update plan_fas_color set nar = '' where id_plan = ? ";

                ps = conn.prepareStatement(sql);
                ps.setInt(1, idPlan);
                ps.execute();

                sql = "Update plan_fas_dekor set nar = '' where id_plan = ? ";

                ps = conn.prepareStatement(sql);
                ps.setInt(1, idPlan);
                ps.execute();

                sql = "Update plan_fas_sostav set nar = '' where id_plan = ? ";

                ps = conn.prepareStatement(sql);
                ps.setInt(1, idPlan);
                ps.execute();

                for (Object data1 : data) {
                    Vector tmp = (Vector) data1;

                    sql = " Update plan_item  "
                            + " Set nar = ? "
                            + " Where id_plan = " + idPlan + " and"
                            + "       fas = ? ";

                    ps = conn.prepareStatement(sql);
                    ps.setString(1, tmp.get(1).toString().trim());
                    ps.setInt(2, Integer.valueOf(tmp.get(0).toString()));
                    ps.execute();

                    sql = " Update plan_fas_color  "
                            + " Set nar = ? "
                            + " Where id_plan = " + idPlan + " and"
                            + "       fas = ? ";

                    ps = conn.prepareStatement(sql);
                    ps.setString(1, tmp.get(1).toString().trim());
                    ps.setInt(2, Integer.valueOf(tmp.get(0).toString()));
                    ps.execute();

                    sql = " Update plan_fas_dekor  "
                            + " Set nar = ? "
                            + " Where id_plan = " + idPlan + " and"
                            + "       fas = ? ";

                    ps = conn.prepareStatement(sql);
                    ps.setString(1, tmp.get(1).toString().trim());
                    ps.setInt(2, Integer.valueOf(tmp.get(0).toString()));
                    ps.execute();

                    sql = " Update plan_fas_sostav  "
                            + " Set nar = ? "
                            + " Where id_plan = " + idPlan + " and"
                            + "       fas = ? ";

                    ps = conn.prepareStatement(sql);
                    ps.setString(1, tmp.get(1).toString().trim());
                    ps.setInt(2, Integer.valueOf(tmp.get(0).toString()));
                    ps.execute();
                }
            }
            commit();
        } catch (Exception e) {
            rollBack();
            System.err.println("Ошибка updateNarInPlanProduction() " + e);
            log.error("Ошибка updateNarInPlanProduction()", e);
            throw new Exception("Ошибка updateNarInPlanProduction() " + e.getMessage(), e);
        } finally {
            setAutoCommit(true);
        }
        return true;
    }

    /**
     *
     * @param data
     * @return
     * @throws Exception
     */
    public boolean createTableLoadTechInPlanProduction(Vector data, int flag) throws Exception {
        boolean rezalt = false;
        String sql = "";
        try {
            setAutoCommit(false);

            //удаляем временную таблицу если она есть
            sql = " drop table if exists tmp_load_tech";
            ps = conn.prepareStatement(sql);
            ps.execute();

            //создаем временную таблицу
            sql = "CREATE temp table tmp_load_tech(idSpec integer NOT NULL, summ numeric NOT NULL DEFAULT 0);";
            ps = conn.prepareStatement(sql);
            ps.execute();

            //заполняем временную таблицу
            sql = "INSERT INTO tmp_load_tech VALUES(?, ?);";
            ps = conn.prepareStatement(sql);

            Iterator it = data.iterator();
            while (it.hasNext()) {
                double kol = 0;
                Vector items = (Vector) it.next();

                if (flag == 0) {
                    kol = Double.valueOf(items.get(6).toString());
                } else if (flag == 1) {
                    kol = Double.valueOf(UtilFunctions.formatNorm(Double.valueOf(Double.valueOf(items.get(6).toString()) * (Double.valueOf(items.get(16).toString()) / 100)), 3));
                } else if (flag == 2) {
                    kol = Double.valueOf(UtilFunctions.formatNorm(Double.valueOf(Double.valueOf(items.get(6).toString()) * (Double.valueOf(items.get(17).toString()) / 100)), 3));
                } else if (flag == 3) {
                    kol = Double.valueOf(UtilFunctions.formatNorm(Double.valueOf(Double.valueOf(items.get(6).toString()) * (Double.valueOf(items.get(18).toString()) / 100)), 3));
                }

                ps.setInt(1, Integer.parseInt(items.get(8).toString()));
                ps.setDouble(2, kol);
                ps.execute();
            }
            commit();

            rezalt = true;
        } catch (Exception e) {
            rollBack();
            rezalt = false;
            System.err.println("Ошибка createTableLoadTechInPlanProduction() " + e);
            log.error("Ошибка createTableLoadTechInPlanProduction()", e);
            throw new Exception("Ошибка createTableLoadTechInPlanProduction() " + e.getMessage(), e);
        } finally {
            setAutoCommit(true);
        }
        return rezalt;
    }

    public boolean createTableLoadTechInProjectPlan(Vector data, int flag) throws Exception {
        boolean rezalt = false;
        String sql = "";
        try {
            setAutoCommit(false);

            //удаляем временную таблицу если она есть
            sql = " drop table if exists tmp_load_tech";
            ps = conn.prepareStatement(sql);
            ps.execute();

            //создаем временную таблицу
            sql = "CREATE temp table tmp_load_tech(idSpec integer NOT NULL, summ numeric NOT NULL DEFAULT 0);";
            ps = conn.prepareStatement(sql);
            ps.execute();

            //заполняем временную таблицу
            sql = "INSERT INTO tmp_load_tech VALUES(?, ?);";
            ps = conn.prepareStatement(sql);

            Iterator it = data.iterator();
            while (it.hasNext()) {
                double kol = 0;
                Vector items = (Vector) it.next();

                if (flag == 0) {
                    kol = Double.valueOf(items.get(5).toString());
                } 
                /*else if(flag == 1){
                    kol = Double.valueOf(UtilFunctions.formatNorm(Double.valueOf(Double.valueOf(items.get(6).toString())*(Double.valueOf(items.get(16).toString())/100)), 3));
                } else if(flag == 2){
                    kol = Double.valueOf(UtilFunctions.formatNorm(Double.valueOf(Double.valueOf(items.get(6).toString())*(Double.valueOf(items.get(17).toString())/100)), 3));
               } else if(flag == 3){
                    kol = Double.valueOf(UtilFunctions.formatNorm(Double.valueOf(Double.valueOf(items.get(6).toString())*(Double.valueOf(items.get(18).toString())/100)), 3));
               }*/

                ps.setInt(1, Integer.parseInt(items.get(7).toString()));
                ps.setDouble(2, kol);
                ps.execute();
            }
            commit();

            rezalt = true;
        } catch (Exception e) {
            rollBack();
            rezalt = false;
            System.err.println("Ошибка createTableLoadTechInProjectPlan() " + e);
            log.error("Ошибка createTableLoadTechInProjectPlan()", e);
            throw new Exception("Ошибка createTableLoadTechInProjectPlan() " + e.getMessage(), e);
        } finally {
            setAutoCommit(true);
        }
        return rezalt;
    }

    public boolean createTableRashodMaterialInProjectPlan(Vector data, int flag) throws Exception {
        boolean rezalt = false;
        String sql = "";
        try {
            setAutoCommit(false);

            //удаляем временную таблицу если она есть
            sql = " drop table if exists tmp_rashod_material";
            ps = conn.prepareStatement(sql);
            ps.execute();

            //создаем временную таблицу
            sql = "CREATE temp table tmp_rashod_material(fas integer NOT NULL, "
                    + "                                 summ numeric NOT NULL DEFAULT 0,"
                    + "                                 cx integer NOT NULL DEFAULT 0, "
                    + "                                 sar integer NOT NULL DEFAULT 0, "
                    + "                                 nar character varying(20) NOT NULL DEFAULT ''::character varying);";
            ps = conn.prepareStatement(sql);
            ps.execute();

            //заполняем временную таблицу
            sql = "INSERT INTO tmp_rashod_material VALUES(?, ?, ?, ?, ?);";
            ps = conn.prepareStatement(sql);

            Iterator it = data.iterator();
            while (it.hasNext()) {
                double kol = 0;
                Vector items = (Vector) it.next();

                if (flag == 0) {
                    kol = Double.valueOf(items.get(1).toString());
                } else if (flag == 1) {
                    kol = Double.valueOf(UtilFunctions.formatNorm(Double.valueOf(Double.valueOf(items.get(1).toString()) * (Double.valueOf(items.get(2).toString()) / 100)), 3));
                } else if (flag == 2) {
                    kol = Double.valueOf(UtilFunctions.formatNorm(Double.valueOf(Double.valueOf(items.get(1).toString()) * (Double.valueOf(items.get(3).toString()) / 100)), 3));
                } else if (flag == 3) {
                    kol = Double.valueOf(UtilFunctions.formatNorm(Double.valueOf(Double.valueOf(items.get(1).toString()) * (Double.valueOf(items.get(4).toString()) / 100)), 3));
                }

                ps.setInt(1, Integer.parseInt(items.get(0).toString()));
                ps.setDouble(2, kol);
                ps.setInt(3, Integer.parseInt(items.get(5).toString()));
                ps.setInt(4, Integer.parseInt(items.get(6).toString()));
                ps.setString(5, String.valueOf(items.get(7).toString()));
                ps.execute();
            }
            commit();

            rezalt = true;
        } catch (Exception e) {
            rollBack();
            rezalt = false;
            System.err.println("Ошибка createTableRashodMaterialInProjectPlan() " + e);
            log.error("Ошибка createTableRashodMaterialInProjectPlan()", e);
            throw new Exception("Ошибка createTableRashodMaterialInProjectPlan() " + e.getMessage(), e);
        } finally {
            setAutoCommit(true);
        }
        return rezalt;
    }

    /**
     *
     * @return
     * @throws Exception
     */
    public Vector getAllModelsLoadTechInPlanProduction() throws Exception {
        Vector elements = new Vector();
        String sql = "";
        try {
            sql = " Select distinct id_model "
                    + " From tmp_load_tech, spec "
                    + " Where idSpec = spec.id and id_model >= 0 "
                    + " Order by id_model";
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                elements.add(rs.getInt("id_model"));
            }

        } catch (Exception e) {
            elements = new Vector();
            System.err.println("Ошибка getAllModelsLoadTechInPlanProduction() " + e);
            log.error("Ошибка getAllModelsLoadTechInPlanProduction()", e);
            throw new Exception("Ошибка getAllModelsLoadTechInPlanProduction() " + e.getMessage(), e);
        }
        return elements;
    }

    /**
     *
     * @return
     * @throws Exception
     */
    public boolean dropTableLoadTechInPlanProduction() throws Exception {
        boolean rezalt = false;
        String sql = "";
        try {
            //удаляем временную таблицу
            sql = " drop table if exists tmp_load_tech";
            ps = conn.prepareStatement(sql);
            ps.execute();

            rezalt = true;
        } catch (Exception e) {
            rezalt = false;
            System.err.println("Ошибка dropTableLoadTechInPlanProduction() " + e);
            log.error("Ошибка dropTableLoadTechInPlanProduction()", e);
            throw new Exception("Ошибка dropTableLoadTechInPlanProduction() " + e.getMessage(), e);
        }
        return rezalt;
    }

    /**
     *
     * @return
     * @throws Exception
     */
    public Vector getLoadTechInPlanProduction() throws Exception {
        Vector elements = new Vector();
        String sql = "";
        try {
            sql = "Select idTech, tech, sum(vnorm*kolPlan) as load"
                    + " From(Select idTech, tech, vnorm, sum(tmp_load_tech.summ) as kolPlan "
                    + "     From(Select spec.id_model as model, spec.id as idSpec, spec, s_tech.id as idTech, "
                    + "             tech, sum(round(spec_item.vnorm::numeric, " + UtilPlan.ROUNDING_NORM + ")) AS vnorm "
                    + "          From spec, spec_item, s_tech "
                    + "          Where spec_item.id_spec = spec.id and "
                    + "             spec_item.num >= 0 and "
                    + "             spec_item.id_tech <> (-1) and "
                    + "             s_tech.id = spec_item.id_tech and "
                    + "             spec.id_model >= 0 and "
                    + "             spec.id in (Select distinct tmp_load_tech.idSpec from tmp_load_tech) "
                    + "          Group by model, spec.id, spec, idTech, tech ) as t1, tmp_load_tech "
                    + "      Where tmp_load_tech.idSpec = t1.idSpec "
                    + "     Group by  idTech, tech, vnorm ) as t2 "
                    + " Group by  idTech, tech "
                    + " Order by tech ";

            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                Vector tmp = new Vector();
                tmp.add(false);
                tmp.add(rs.getInt("idTech"));
                tmp.add(rs.getString("tech"));
                tmp.add(Double.valueOf(UtilFunctions.formatNorm(rs.getDouble("load"), UtilPlan.ROUNDING_NORM)));
                elements.add(tmp);
            }

        } catch (Exception e) {
            System.err.println("Ошибка getLoadTechInPlanProduction() " + e);
            log.error("Ошибка getLoadTechInPlanProduction()", e);
            throw new Exception("Ошибка getLoadTechInPlanProduction() " + e.getMessage(), e);
        }
        return elements;
    }

    public Vector getRashodMaterialProject(int id) throws Exception {
        Vector elements = new Vector();
        String sql = "";
        try {
            sql = "Select id_polotno, nar_polotno, sum(kol*rashod) as rashod "
                    + " From ( Select fas, sum(summ) as kol, cx, sar, nar "
                    + "         From tmp_rashod_material "
                    + "         Group by fas, cx, sar, nar) as t1, plan_fas_sostav "
                    + " Where t1.fas = plan_fas_sostav.fas and "
                    + "         plan_fas_sostav.id_plan = ? and "
                    + "       t1.cx = plan_fas_sostav.cx and "
                    + "       t1.sar = plan_fas_sostav.sar and "
                    + "       t1.nar = plan_fas_sostav.nar "
                    + " Group by id_polotno, nar_polotno "
                    + " Order by nar_polotno ";

            ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();
            while (rs.next()) {
                Vector tmp = new Vector();
                tmp.add(false);
                tmp.add(rs.getInt("id_polotno"));
                tmp.add(rs.getString("nar_polotno"));
                tmp.add(Double.valueOf(UtilFunctions.formatNorm(Double.valueOf(rs.getDouble("rashod")), 0)));
                elements.add(tmp);
            }

        } catch (Exception e) {
            System.err.println("Ошибка getRashodMaterialProject() " + e);
            log.error("Ошибка getRashodMaterialProject()", e);
            throw new Exception("Ошибка getRashodMaterialProject() " + e.getMessage(), e);
        }
        return elements;
    }

    public Vector getRashodMaterialProjectTemp() throws Exception {
        Vector elements = new Vector();
        String sql = "";
        try {

            sql = "Select id_polotno, nar_polotno, sum(kol*rashod) as rashod "
                    + " From ( Select fas, sum(summ) as kol, cx, sar, nar "
                    + "         From tmp_rashod_material "
                    + "         Group by fas, cx, sar, nar) as t1, tmp_plan_fas_sostav "
                    + " Where t1.fas = tmp_plan_fas_sostav.fas and "
                    + "       t1.cx = tmp_plan_fas_sostav.cx and "
                    + "       t1.sar = tmp_plan_fas_sostav.sar and "
                    + "       t1.nar = tmp_plan_fas_sostav.nar "
                    + " Group by id_polotno, nar_polotno "
                    + " Order by nar_polotno";

            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                Vector tmp = new Vector();
                tmp.add(false);
                tmp.add(rs.getInt("id_polotno"));
                tmp.add(rs.getString("nar_polotno"));
                tmp.add(Double.valueOf(UtilFunctions.formatNorm(Double.valueOf(rs.getDouble("rashod")), 0)));
                elements.add(tmp);
            }

        } catch (Exception e) {
            System.err.println("Ошибка getRashodMaterialProjectTemp() " + e);
            log.error("Ошибка getRashodMaterialProjectTemp()", e);
            throw new Exception("Ошибка getRashodMaterialProjectTemp() " + e.getMessage(), e);
        }
        return elements;
    }

    public Vector getReportRashodMaterialProject(int id) throws Exception {
        Vector elements = new Vector();
        String sql = "";
        try {
            sql = "Select id_polotno, nar_polotno, sum(kol*rashod) as rashod "
                    + " From ( Select fas, sum(round(kol_month,3)) as kol, cx, sar, nar "
                    + "           From plan, plan_item "
                    + "           Where plan.id = plan_item.id_plan and "
                    + "			plan.id = ? "
                    + "           Group by fas, cx, sar, nar) as t1, plan_fas_sostav "
                    + " Where t1.fas = plan_fas_sostav.fas and "
                    + "         plan_fas_sostav.id_plan = ? and "
                    + "       t1.cx = plan_fas_sostav.cx and "
                    + "       t1.sar = plan_fas_sostav.sar and "
                    + "       t1.nar = plan_fas_sostav.nar "
                    + " Group by id_polotno, nar_polotno "
                    + " Order by nar_polotno ";

            ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            ps.setInt(2, id);
            rs = ps.executeQuery();
            while (rs.next()) {
                Vector tmp = new Vector();
                tmp.add(rs.getString("nar_polotno"));
                tmp.add(Double.valueOf(UtilFunctions.formatNorm(Double.valueOf(rs.getDouble("rashod")), 2)));
                elements.add(tmp);
            }

        } catch (Exception e) {
            System.err.println("Ошибка getRashodMaterialProject() " + e);
            log.error("Ошибка getRashodMaterialProject()", e);
            throw new Exception("Ошибка getRashodMaterialProject() " + e.getMessage(), e);
        }
        return elements;
    }

    public Vector getReportRashodMaterialProjectTemp() throws Exception {
        Vector elements = new Vector();
        String sql = "";
        try {

            sql = "Select id_polotno, nar_polotno, sum(kol*rashod) as rashod "
                    + " From ( Select fas, sum(round(kol_month,3)) as kol, cx, sar, nar "
                    + "           From tmp_plan_item "
                    + "           Group by fas, cx, sar, nar) as t1, tmp_plan_fas_sostav "
                    + " Where t1.fas = tmp_plan_fas_sostav.fas and "
                    + "       t1.cx = tmp_plan_fas_sostav.cx and "
                    + "       t1.sar = tmp_plan_fas_sostav.sar and "
                    + "       t1.nar = tmp_plan_fas_sostav.nar "
                    + " Group by id_polotno, nar_polotno "
                    + " Order by nar_polotno";

            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                Vector tmp = new Vector();
                tmp.add(rs.getString("nar_polotno"));
                tmp.add(Double.valueOf(UtilFunctions.formatNorm(Double.valueOf(rs.getDouble("rashod")), 2)));
                elements.add(tmp);
            }

        } catch (Exception e) {
            System.err.println("Ошибка getRashodMaterialProjectTemp() " + e);
            log.error("Ошибка getRashodMaterialProjectTemp()", e);
            throw new Exception("Ошибка getRashodMaterialProjectTemp() " + e.getMessage(), e);
        }
        return elements;
    }

    /**
     *
     * @param idTech
     * @return
     * @throws Exception
     */
    public Vector getTechDetalInPlanProduction(int idTech) throws Exception {
        Vector elements = new Vector();
        String sql = "";
        try {
            sql = "Select model, t1.idSpec as idSpec, spec, idTech, tech, vnorm , sum(tmp_load_tech.summ) as kolPlan "
                    + " From(Select spec.id_model as model, spec.id as idSpec, spec, s_tech.id as idTech, "
                    + "         tech, sum(round(spec_item.vnorm::numeric, " + UtilPlan.ROUNDING_NORM + ")) AS vnorm "
                    + "     From spec, spec_item, s_tech "
                    + "     Where spec_item.id_spec = spec.id and "
                    + "		spec_item.num >= 0 and "
                    + "		spec_item.id_tech <> (-1) and "
                    + "         spec.id_model >= 0 and "
                    + "		s_tech.id = spec_item.id_tech and "
                    + "         s_tech.id = ? and "
                    + "		spec.id in (Select distinct tmp_load_tech.idSpec from tmp_load_tech) "
                    + "     Group by model, spec.id, spec, idTech, tech ) as t1, tmp_load_tech "
                    + " Where tmp_load_tech.idSpec = t1.idSpec"
                    + " Group by model, t1.idSpec, spec, idTech, tech, vnorm "
                    + " Order by model, idSpec, spec";

            ps = conn.prepareStatement(sql);
            ps.setInt(1, idTech);
            rs = ps.executeQuery();
            while (rs.next()) {
                Vector tmp = new Vector();
                tmp.add(false);
                tmp.add(rs.getInt("model"));
                tmp.add(rs.getInt("idSpec"));
                tmp.add(rs.getString("spec"));
                tmp.add(UtilFunctions.formatNorm(rs.getDouble("vnorm"), UtilPlan.ROUNDING_NORM));
                tmp.add(rs.getDouble("kolPlan"));
                tmp.add(Double.valueOf(UtilFunctions.formatNorm(Double.valueOf(rs.getDouble("kolPlan") * rs.getDouble("vnorm")), UtilPlan.ROUNDING_NORM)));
                elements.add(tmp);
            }

        } catch (Exception e) {
            System.err.println("Ошибка getTechDetalInPlanProduction() " + e);
            log.error("Ошибка getTechDetalInPlanProduction()", e);
            throw new Exception("Ошибка getTechDetalInPlanProduction() " + e.getMessage(), e);
        }
        return elements;
    }

    public Vector getRashodMaterialDetalInProject(int idPolotno, String narPolotno, int idPlan) throws Exception {
        Vector elements = new Vector();
        String sql = "";
        try {
            sql = "Select t1.fas as model, t1.kol as kol, rashod, sum(kol*rashod) as rashodSum, t1.cx as tcx, t1.sar as tsar, t1.nar as tnar "
                    + " From ( Select fas, sum(summ) as kol, cx, sar, nar "
                    + "         From tmp_rashod_material "
                    + "         Group by fas, cx, sar, nar) as t1, plan_fas_sostav "
                    + " Where t1.fas = plan_fas_sostav.fas and "
                    + "         t1.cx = plan_fas_sostav.cx and "
                    + "         t1.sar = plan_fas_sostav.sar and "
                    + "         t1.nar = plan_fas_sostav.nar and "
                    + "         plan_fas_sostav.id_plan = ? and "
                    + "         plan_fas_sostav.id_polotno = ? and "
                    + "         plan_fas_sostav.nar_polotno = ? "
                    + " Group by model, tcx, tsar, tnar, kol, rashod "
                    + " Order by model, rashod ";

            ps = conn.prepareStatement(sql);
            ps.setInt(1, idPlan);
            ps.setInt(2, idPolotno);
            ps.setString(3, narPolotno);
            rs = ps.executeQuery();
            while (rs.next()) {
                Vector tmp = new Vector();
                tmp.add(false);
                tmp.add(rs.getInt("model"));
                tmp.add(rs.getInt("tcx"));
                tmp.add(rs.getInt("tsar"));
                tmp.add(rs.getString("tnar"));
                tmp.add(rs.getDouble("kol"));
                tmp.add(rs.getDouble("rashod"));
                tmp.add(Double.valueOf(UtilFunctions.formatNorm(Double.valueOf(Double.valueOf(UtilFunctions.formatNorm(rs.getDouble("kol"), 0)) * rs.getDouble("rashod")), 2)));
                elements.add(tmp);
            }

        } catch (Exception e) {
            System.err.println("Ошибка getRashodMaterialDetalInProject() " + e);
            log.error("Ошибка getRashodMaterialDetalInProject()", e);
            throw new Exception("Ошибка getRashodMaterialDetalInProject() " + e.getMessage(), e);
        }
        return elements;
    }

    public Vector getRashodMaterialDetalInProjectTemp(int idPolotno, String narPolotno) throws Exception {
        Vector elements = new Vector();
        String sql = "";
        try {
            sql = "Select t1.fas as model, t1.kol as kol, rashod, sum(kol*rashod) as rashodSum, t1.cx as tcx, t1.sar as tsar, t1.nar as tnar "
                    + " From ( Select fas, sum(summ) as kol, cx, sar, nar "
                    + "         From tmp_rashod_material "
                    + "         Group by fas, cx, sar, nar) as t1, tmp_plan_fas_sostav "
                    + " Where t1.fas = tmp_plan_fas_sostav.fas and "
                    + "         t1.cx = tmp_plan_fas_sostav.cx and "
                    + "         t1.sar = tmp_plan_fas_sostav.sar and "
                    + "         t1.nar = tmp_plan_fas_sostav.nar and "
                    + "         tmp_plan_fas_sostav.id_polotno = ? and "
                    + "         tmp_plan_fas_sostav.nar_polotno = ? "
                    + " Group by model, tcx, tsar, tnar, kol, rashod "
                    + " Order by model, rashod ";

            ps = conn.prepareStatement(sql);
            ps.setInt(1, idPolotno);
            ps.setString(2, narPolotno);
            rs = ps.executeQuery();
            while (rs.next()) {
                Vector tmp = new Vector();
                tmp.add(false);
                tmp.add(rs.getInt("model"));
                tmp.add(rs.getInt("tcx"));
                tmp.add(rs.getInt("tsar"));
                tmp.add(rs.getString("tnar"));
                tmp.add(rs.getDouble("kol"));
                tmp.add(rs.getDouble("rashod"));
                tmp.add(Double.valueOf(UtilFunctions.formatNorm(Double.valueOf(Double.valueOf(UtilFunctions.formatNorm(rs.getDouble("kol"), 0)) * rs.getDouble("rashod")), 2)));
                elements.add(tmp);
            }

        } catch (Exception e) {
            System.err.println("Ошибка getRashodMaterialDetalInProjectTemp() " + e);
            log.error("Ошибка getRashodMaterialDetalInProjectTemp()", e);
            throw new Exception("Ошибка getRashodMaterialDetalInProjectTemp() " + e.getMessage(), e);
        }
        return elements;
    }

    /**
     * Создает временную таблицу tmp_plan_item
     * @param idPlan
     * @return
     * @throws Exception
     */
    public boolean createTempPlanItemTables(int idPlan) throws Exception {
        boolean rezalt = false;
        String sql = "";
        try {
            setAutoCommit(false);

            //--------------------------------------план

            //удаляем временную таблицу если она есть
            sql = " drop table if exists tmp_plan_item";
            ps = conn.prepareStatement(sql);
            ps.execute();

            //создаем временную таблицу
            sql = "CREATE temp table tmp_plan_item("
                    + "id bigserial NOT NULL,"
                    + "  cx integer NOT NULL,"
                    + "  sar integer NOT NULL,"
                    + "  fas integer NOT NULL,"
                    + "  rst integer NOT NULL,"
                    + "  rzm integer NOT NULL,"
                    + "  kol_month numeric NOT NULL,"
                    + "  kol_day numeric NOT NULL,"
                    + "  id_spec integer NOT NULL DEFAULT 1,"
                    + "  id_empl_ins integer NOT NULL,"
                    + "  date_ins timestamp with time zone NOT NULL DEFAULT now(),"
                    + "  dekada1 real NOT NULL DEFAULT 33.3,"
                    + "  dekada2 real NOT NULL DEFAULT 33.3,"
                    + "  dekada3 real NOT NULL DEFAULT 33.4,"
                    + "  note_item character varying(250) NOT NULL DEFAULT ''::character varying,"
                    + "  fas_vid integer NOT NULL DEFAULT 0, "
                    + "  fas_pname character varying(250) NOT NULL DEFAULT ''::character varying,"
                    + "  kol_x integer NOT NULL DEFAULT 1,"
                    + "  nar character varying(20) NOT NULL DEFAULT ''::character varying, "
                    + "  CONSTRAINT tmp_plan_item_pkey PRIMARY KEY (id ))";
            ps = conn.prepareStatement(sql);
            ps.execute();

            //заполняем временную таблицу
            sql = "Select cx, sar, fas, rst, rzm, kol_month, kol_day, id_spec, id_empl_ins, date_ins, "
                    + "     dekada1, dekada2, dekada3, note_item, fas_vid, fas_pname, kol_x, nar "
                    + " From plan_item Where id_plan = ? ";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, idPlan);
            rs = ps.executeQuery();

            while (rs.next()) {
                sql = "Insert into tmp_plan_item(cx, sar, fas, rst, rzm, kol_month, kol_day, id_spec, id_empl_ins, date_ins,"
                        + "                          dekada1, dekada2, dekada3, note_item, fas_vid, fas_pname, kol_x, nar) "
                        + "values( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

                ps = conn.prepareStatement(sql);
                ps.setInt(1, rs.getInt("cx"));
                ps.setInt(2, rs.getInt("sar"));
                ps.setInt(3, rs.getInt("fas"));
                ps.setInt(4, rs.getInt("rst"));
                ps.setInt(5, rs.getInt("rzm"));
                ps.setDouble(6, rs.getDouble("kol_month"));
                ps.setDouble(7, rs.getDouble("kol_day"));
                ps.setInt(8, rs.getInt("id_spec"));
                ps.setInt(9, rs.getInt("id_empl_ins"));
                ps.setDate(10, rs.getDate("date_ins"));
                ps.setDouble(11, rs.getDouble("dekada1"));
                ps.setDouble(12, rs.getDouble("dekada2"));
                ps.setDouble(13, rs.getDouble("dekada3"));
                ps.setString(14, rs.getString("note_item"));
                ps.setInt(15, rs.getInt("fas_vid"));
                ps.setString(16, rs.getString("fas_pname"));
                ps.setInt(17, rs.getInt("kol_x"));
                ps.setString(18, rs.getString("nar"));
                ps.execute();
            }

            commit();

            rezalt = true;
        } catch (Exception e) {
            rollBack();
            rezalt = false;
            System.err.println("Ошибка createTempPlanItemTables() " + e);
            log.error("Ошибка createTempPlanItemTables()", e);
            throw new Exception("Ошибка createTempPlanItemTables() " + e.getMessage(), e);
        } finally {
            setAutoCommit(true);
        }
        return rezalt;
    }

    /**
     * Создает временную таблицу tmp_plan_fas_new
     * @param idPlan
     * @return
     * @throws Exception
     */
    public boolean createTempPlanFasNewTables(int idPlan) throws Exception {
        boolean rezalt = false;
        String sql = "";
        try {
            setAutoCommit(false);

            //удаляем временную таблицу если она есть
            sql = " drop table if exists tmp_plan_fas_new";
            ps = conn.prepareStatement(sql);
            ps.execute();

            //создаем временную таблицу
            sql = "CREATE temp table tmp_plan_fas_new("
                    + "id bigserial NOT NULL,"
                    + "  fas integer NOT NULL,"
                    + "  CONSTRAINT tmp_plan_fas_new_pkey PRIMARY KEY (id ))";
            ps = conn.prepareStatement(sql);
            ps.execute();

            //заполняем временную таблицу
            sql = "Select distinct fas From plan_fas_new Where id_plan = ? ";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, idPlan);
            rs = ps.executeQuery();

            while (rs.next()) {
                sql = "Insert into tmp_plan_fas_new( fas) values( ?)";

                ps = conn.prepareStatement(sql);
                ps.setInt(1, rs.getInt("fas"));
                ps.execute();
            }

            commit();

            rezalt = true;
        } catch (Exception e) {
            rollBack();
            rezalt = false;
            System.err.println("Ошибка createTempPlanFasNewTables() " + e);
            log.error("Ошибка createTempPlanFasNewTables()", e);
            throw new Exception("Ошибка createTempPlanFasNewTables() " + e.getMessage(), e);
        } finally {
            setAutoCommit(true);
        }
        return rezalt;
    }

    /**
     * Создает временную таблицу tmp_plan_fas_dekor
     * @param idPlan
     * @return
     * @throws Exception
     */
    public boolean createTempPlanFasDekorTables(int idPlan) throws Exception {
        boolean rezalt = false;
        String sql = "";
        try {
            setAutoCommit(false);

            //удаляем временную таблицу если она есть
            sql = " drop table if exists tmp_plan_fas_dekor";
            ps = conn.prepareStatement(sql);
            ps.execute();

            //создаем временную таблицу
            sql = "CREATE temp table tmp_plan_fas_dekor("
                    + "  id bigserial NOT NULL,"
                    + "  fas integer NOT NULL, "
                    + "  vid integer NOT NULL, "
                    + "  cx integer NOT NULL DEFAULT 0, "
                    + "  sar integer NOT NULL DEFAULT 0, "
                    + "  nar character varying(20) NOT NULL DEFAULT ''::character varying, "
                    + "  CONSTRAINT tmp_plan_fas_dekor_pkey PRIMARY KEY (id ))";
            ps = conn.prepareStatement(sql);
            ps.execute();

            //заполняем временную таблицу
            sql = "Select distinct fas, vid, cx, sar, nar From plan_fas_dekor Where id_plan = ? ";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, idPlan);
            rs = ps.executeQuery();

            while (rs.next()) {
                sql = "Insert into tmp_plan_fas_dekor( fas, vid, cx, sar, nar) values( ?, ?, ?, ?, ?)";

                ps = conn.prepareStatement(sql);
                ps.setInt(1, rs.getInt("fas"));
                ps.setInt(2, rs.getInt("vid"));
                ps.setInt(3, rs.getInt("cx"));
                ps.setInt(4, rs.getInt("sar"));
                ps.setString(5, rs.getString("nar"));
                ps.execute();
            }

            commit();

            rezalt = true;
        } catch (Exception e) {
            rollBack();
            rezalt = false;
            System.err.println("Ошибка createTempPlanFasDekorTables() " + e);
            log.error("Ошибка createTempPlanFasDekorTables()", e);
            throw new Exception("Ошибка createTempPlanFasDekorTables() " + e.getMessage(), e);
        } finally {
            setAutoCommit(true);
        }
        return rezalt;
    }

    /**
     * Создает временную таблицу tmp_plan_fas_sostav
     * @param idPlan
     * @return
     * @throws Exception
     */
    public boolean createTempPlanFasSostavTables(int idPlan) throws Exception {
        boolean rezalt = false;
        String sql = "";
        try {
            setAutoCommit(false);

            //удаляем временную таблицу если она есть
            sql = " drop table if exists tmp_plan_fas_sostav";
            ps = conn.prepareStatement(sql);
            ps.execute();

            //создаем временную таблицу
            sql = "CREATE temp table tmp_plan_fas_sostav("
                    + "  id bigserial NOT NULL, "
                    + "  fas integer NOT NULL, "
                    + "  id_polotno integer NOT NULL, "
                    + "  nar_polotno character varying(9) NOT NULL, "
                    + "  sostav character varying(250) NOT NULL, "
                    + "  vid integer NOT NULL, "
                    + "  rashod real NOT NULL DEFAULT 0, "
                    + "  cx integer NOT NULL DEFAULT 0, "
                    + "  sar integer NOT NULL DEFAULT 0, "
                    + "  nar character varying(20) NOT NULL DEFAULT ''::character varying, "
                    + "  CONSTRAINT tmp_plan_fas_sostav_pkey PRIMARY KEY (id ))";
            ps = conn.prepareStatement(sql);
            ps.execute();

            //заполняем временную таблицу
            sql = "Select distinct fas, id_polotno, nar_polotno, sostav, vid, rashod, cx, sar, nar  "
                    + " From plan_fas_sostav Where id_plan = ? ";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, idPlan);
            rs = ps.executeQuery();

            while (rs.next()) {
                sql = "Insert into tmp_plan_fas_sostav( fas, id_polotno, nar_polotno, sostav, vid, rashod, cx, sar, nar)"
                        + " values( ?, ?, ?, ?, ?, ?, ?, ?, ?)";

                ps = conn.prepareStatement(sql);
                ps.setInt(1, rs.getInt("fas"));
                ps.setInt(2, rs.getInt("id_polotno"));
                ps.setString(3, rs.getString("nar_polotno"));
                ps.setString(4, rs.getString("sostav"));
                ps.setInt(5, rs.getInt("vid"));
                ps.setDouble(6, rs.getDouble("rashod"));
                ps.setInt(7, rs.getInt("cx"));
                ps.setInt(8, rs.getInt("sar"));
                ps.setString(9, rs.getString("nar"));
                ps.execute();
            }

            commit();

            rezalt = true;
        } catch (Exception e) {
            rollBack();
            rezalt = false;
            System.err.println("Ошибка createTempPlanFasSostavTables() " + e);
            log.error("Ошибка createTempPlanFasSostavTables()", e);
            throw new Exception("Ошибка createTempPlanFasSostavTables() " + e.getMessage(), e);
        } finally {
            setAutoCommit(true);
        }
        return rezalt;
    }

    /**
     * Создает временную таблицу tmp_plan_fas_color
     * @param idPlan
     * @return
     * @throws Exception
     */
    public boolean createTempPlanFasColorTables(int idPlan) throws Exception {
        boolean rezalt = false;
        String sql = "";
        try {
            setAutoCommit(false);

            //удаляем временную таблицу если она есть
            sql = " drop table if exists tmp_plan_fas_color";
            ps = conn.prepareStatement(sql);
            ps.execute();

            //создаем временную таблицу
            sql = "CREATE temp table tmp_plan_fas_color("
                    + "  id bigserial NOT NULL,"
                    + "  fas integer NOT NULL, "
                    + "  id_color integer NOT NULL, "
                    + "  ncw character varying(100) NOT NULL, "
                    + "  cx integer NOT NULL DEFAULT 0, "
                    + "  sar integer NOT NULL DEFAULT 0, "
                    + "  nar character varying(20) NOT NULL DEFAULT ''::character varying, "
                    + "  CONSTRAINT tmp_plan_fas_color_pkey PRIMARY KEY (id ))";
            ps = conn.prepareStatement(sql);
            ps.execute();

            //заполняем временную таблицу
            sql = "Select distinct fas, id_color, ncw, cx, sar, nar From plan_fas_color Where id_plan = ? ";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, idPlan);
            rs = ps.executeQuery();

            while (rs.next()) {
                sql = "Insert into tmp_plan_fas_color( fas, id_color, ncw, cx, sar, nar) values( ?, ?, ?, ?, ?, ?)";

                ps = conn.prepareStatement(sql);
                ps.setInt(1, rs.getInt("fas"));
                ps.setInt(2, rs.getInt("id_color"));
                ps.setString(3, rs.getString("ncw"));
                ps.setInt(4, rs.getInt("cx"));
                ps.setInt(5, rs.getInt("sar"));
                ps.setString(6, rs.getString("nar"));

                ps.execute();
            }

            commit();

            rezalt = true;
        } catch (Exception e) {
            rollBack();
            rezalt = false;
            System.err.println("Ошибка createTempPlanFasColorTables() " + e);
            log.error("Ошибка createTempPlanFasColorTables()", e);
            throw new Exception("Ошибка createTempPlanFasColorTables() " + e.getMessage(), e);
        } finally {
            setAutoCommit(true);
        }
        return rezalt;
    }

    /**
     * Удаляет рост, размер, шифр, конвейер из временной таблицы tmp_plan_item
     * @param idEmpl
     * @return
     * @throws Exception
     */
    public boolean removeSarRstRzmTempPlanTable(boolean flagSar, boolean flagRst, boolean flagRzm, boolean flagConv, int idEmpl) throws Exception {
        boolean rezalt = false;
        String sql = "";
        try {
            setAutoCommit(false);

            sql = " UPDATE tmp_plan_item "
                    + "SET id_empl_ins = ?,"
                    + "      date_ins = now()  "
                    + (flagSar ? " , sar = 0 " : " ")
                    + (flagRst ? " , rst = 0 " : " ")
                    + (flagRzm ? " , rzm = 0 " : " ")
                    + (flagConv ? " , cx  = 0 " : " ");
            ps = conn.prepareStatement(sql);
            ps.setInt(1, idEmpl);
            ps.execute();

            sql = "Select cx, sar, fas, rst, rzm, sum(kol_month) as kolM, sum(kol_day) as kolD, id_spec "
                    + " From tmp_plan_item "
                    + " Group by cx, sar, fas, rst, rzm, id_spec";
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            sql = " Delete From tmp_plan_item ";
            ps = conn.prepareStatement(sql);
            ps.execute();

            while (rs.next()) {
                sql = "Insert into tmp_plan_item(cx, sar, fas, rst, rzm, kol_month, kol_day, id_spec, id_empl_ins) "
                        + "values( ?, ?, ?, ?, ?, ?, ?, ?, ?)";

                ps = conn.prepareStatement(sql);
                ps.setInt(1, rs.getInt("cx"));
                ps.setInt(2, rs.getInt("sar"));
                ps.setInt(3, rs.getInt("fas"));
                ps.setInt(4, rs.getInt("rst"));
                ps.setInt(5, rs.getInt("rzm"));
                ps.setDouble(6, rs.getDouble("kolM"));
                ps.setDouble(7, rs.getDouble("kolD"));
                ps.setInt(8, rs.getInt("id_spec"));
                ps.setInt(9, idEmpl);
                ps.execute();
            }

            commit();

            rezalt = true;
        } catch (Exception e) {
            rollBack();
            rezalt = false;
            System.err.println("Ошибка removeSarRstRzmTempPlanTable() " + e);
            log.error("Ошибка removeSarRstRzmTempPlanTable()", e);
            throw new Exception("Ошибка removeSarRstRzmTempPlanTable() " + e.getMessage(), e);
        } finally {
            setAutoCommit(true);
        }
        return rezalt;
    }


    public Vector getShkalaFasProjectDetalTable(int idPlan, int fas, int cx, int sar, String nar) throws Exception {
        Vector elements = new Vector();
        String sql = "Select plan_item.id as idPlanItem , rst, rzm, kol_month as kolM, "
                + "         dekada1, dekada2, dekada3, sum(round(spec_item.vnorm::numeric," + UtilPlan.ROUNDING_NORM + ")) as vnorm"
                + "	From plan, plan_item, spec, spec_item "
                + "	Where plan.id = plan_item.id_plan and "
                + "		plan.id = ? and "
                + "		plan_item.id_spec = spec.id and "
                + "		spec.id  = spec_item.id_spec and "
                + "		spec_item.num >= 0 and "
                + "		spec_item.id_tech <> -1 and "
                + "             plan_item.fas = ? and "
                + "             plan_item.cx = ? and "
                + "             plan_item.sar = ? and "
                + "             plan_item.nar like ?"
                + "	Group by idPlanItem , rst, rzm, kolM, dekada1, dekada2, dekada3  "
                + "	Order by  rst, rzm, idPlanItem	 ";

        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, idPlan);
            ps.setInt(2, fas);
            ps.setInt(3, cx);
            ps.setInt(4, sar);
            ps.setString(5, nar);
            rs = ps.executeQuery();
            while (rs.next()) {
                Vector tmp = new Vector();
                tmp.add(false);
                tmp.add(rs.getInt("idPlanItem"));
                tmp.add(rs.getInt("rst"));
                tmp.add(rs.getInt("rzm"));
                tmp.add(rs.getDouble("kolM"));
                tmp.add(rs.getDouble("dekada1"));
                tmp.add(Double.valueOf(UtilFunctions.formatNorm(Double.valueOf(rs.getDouble("kolM") * (rs.getDouble("dekada1") / 100)), 3)));
                tmp.add(rs.getDouble("dekada2"));
                tmp.add(Double.valueOf(UtilFunctions.formatNorm(Double.valueOf(rs.getDouble("kolM") * (rs.getDouble("dekada2") / 100)), 3)));
                tmp.add(rs.getDouble("dekada3"));
                tmp.add(Double.valueOf(UtilFunctions.formatNorm(Double.valueOf(rs.getDouble("kolM") * (rs.getDouble("dekada3") / 100)), 3)));
                tmp.add(Double.valueOf(UtilFunctions.formatNorm(Double.valueOf(rs.getDouble("kolM") * rs.getDouble("vnorm")), 3)));
                elements.add(tmp);
            }

        } catch (Exception e) {
            elements = new Vector();
            System.err.println("Ошибка getShkalaFasProjectDetalTable() " + e);
            log.error("Ошибка getShkalaFasProjectDetalTable()", e);
            throw new Exception("Ошибка getShkalaFasProjectDetalTable() " + e.getMessage(), e);
        }
        return elements;
    }

    public Vector getShkalaFasProjectDetalTemp(int fas, int cx, int sar, String nar) throws Exception {
        Vector elements = new Vector();
        String sql = "Select tmp_plan_item.id as idPlanItem , rst, rzm, kol_month as kolM, "
                + "         dekada1, dekada2, dekada3, sum(round(spec_item.vnorm::numeric," + UtilPlan.ROUNDING_NORM + ")) as vnorm"
                + "	From tmp_plan_item, spec, spec_item "
                + "	Where tmp_plan_item.id_spec = spec.id and "
                + "		spec.id  = spec_item.id_spec and "
                + "		spec_item.num >= 0 and "
                + "		spec_item.id_tech <> -1 and "
                + "             tmp_plan_item.fas = ? and "
                + "             tmp_plan_item.cx = ? and "
                + "             tmp_plan_item.sar = ? and "
                + "             tmp_plan_item.nar like ? "
                + "	Group by idPlanItem , rst, rzm, kolM, dekada1, dekada2, dekada3  "
                + "	Order by  rst, rzm, idPlanItem	 ";

        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, fas);
            ps.setInt(2, cx);
            ps.setInt(3, sar);
            ps.setString(4, nar);
            rs = ps.executeQuery();
            while (rs.next()) {
                Vector tmp = new Vector();
                tmp.add(false);
                tmp.add(rs.getInt("idPlanItem"));
                tmp.add(rs.getInt("rst"));
                tmp.add(rs.getInt("rzm"));
                tmp.add(rs.getDouble("kolM"));
                tmp.add(rs.getDouble("dekada1"));
                tmp.add(Double.valueOf(UtilFunctions.formatNorm(Double.valueOf(rs.getDouble("kolM") * (rs.getDouble("dekada1") / 100)), 3)));
                tmp.add(rs.getDouble("dekada2"));
                tmp.add(Double.valueOf(UtilFunctions.formatNorm(Double.valueOf(rs.getDouble("kolM") * (rs.getDouble("dekada2") / 100)), 3)));
                tmp.add(rs.getDouble("dekada3"));
                tmp.add(Double.valueOf(UtilFunctions.formatNorm(Double.valueOf(rs.getDouble("kolM") * (rs.getDouble("dekada3") / 100)), 3)));
                tmp.add(Double.valueOf(UtilFunctions.formatNorm(Double.valueOf(rs.getDouble("kolM") * rs.getDouble("vnorm")), 3)));
                elements.add(tmp);
            }

        } catch (Exception e) {
            elements = new Vector();
            System.err.println("Ошибка getShkalaFasProjectDetalTemp() " + e);
            log.error("Ошибка getShkalaFasProjectDetalTemp()", e);
            throw new Exception("Ошибка getShkalaFasProjectDetalTemp() " + e.getMessage(), e);
        }
        return elements;
    }

    /**
     *
     * @return
     * @throws Exception
     */
    public Vector getDataItemPlanTemp() throws Exception {
        Vector elements = new Vector();
        String sql = "Select fas, kolM, idSpec, modelSpec, nameSpec, sum(round(spec_item.vnorm::numeric," + UtilPlan.ROUNDING_NORM + ")) as vnorm "
                + " From (Select fas, sum(round(kol_month,3)) as kolM, spec.id as idSpec, "
                + "             spec.id_model as modelSpec, spec.spec as nameSpec "
                + "	From tmp_plan_item, spec "
                + "	Where tmp_plan_item.id_spec = spec.id "
                + "	Group by fas, idSpec, modelSpec, nameSpec) as t1, spec_item "
                + " Where t1.idSpec = spec_item.id_spec and "
                + "	  spec_item.num >= 0 and "
                + "       spec_item.id_tech <> -1"
                + " Group by fas, kolM, idSpec, modelSpec, nameSpec "
                + " Order by fas, modelSpec, nameSpec ";

        try {
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                Vector tmp = new Vector();
                tmp.add(false);
                tmp.add(rs.getInt("idSpec"));
                tmp.add(rs.getInt("modelSpec"));
                tmp.add(rs.getString("nameSpec"));
                tmp.add(rs.getInt("fas"));
                tmp.add(rs.getDouble("kolM"));
                tmp.add(UtilFunctions.formatNorm(rs.getDouble("vnorm"), UtilPlan.ROUNDING_NORM));
                //   tmp.add(rs.getDouble("vnorm"));
                tmp.add(Double.valueOf(UtilFunctions.formatNorm(Double.valueOf(rs.getDouble("kolM") * rs.getDouble("vnorm")), 3)));
                elements.add(tmp);
            }

        } catch (Exception e) {
            elements = new Vector();
            System.err.println("Ошибка getDataItemPlanTemp() " + e);
            log.error("Ошибка getDataItemPlanTemp()", e);
            throw new Exception("Ошибка getDataItemPlanTemp() " + e.getMessage(), e);
        }
        return elements;
    }

    public Vector getDataItemProjectStokTemp() throws Exception {
        Vector elements = new Vector();
        String sql = "Select min(rst) as minrst, max(rst) as maxrst,  max(rzm) as maxrzm,  min(rzm) as minrzm, t2.fas_vid as fas_vid, t2.fas_pname as fas_pname, "
                + "	t2.fas as fas, t2.kolM as kolM, t2.kol_x as kol_x, t2.note_item as note_item, t2.sar as sar, t2.nar as nar, polotno, color, idNewFas, dekor "
                + " From(Select fas_vid, fas_pname, fas, sum(round(kol_month,3)) as kolM, kol_x, note_item, sar, nar, "
                + "			(SELECT ARRAY_TO_STRING(ARRAY(SELECT nar_polotno FROM tmp_plan_fas_sostav Where tmp_plan_item.fas = tmp_plan_fas_sostav.fas and "
                + "                                                                                                     tmp_plan_item.sar = tmp_plan_fas_sostav.sar and "
                + "                                                                                                     tmp_plan_item.nar = tmp_plan_fas_sostav.nar  "
                + "                                                                                               Order by tmp_plan_fas_sostav.vid, nar_polotno),', ')) as polotno,"
                + "			(SELECT ARRAY_TO_STRING(ARRAY(SELECT ncw FROM tmp_plan_fas_color Where tmp_plan_item.fas = tmp_plan_fas_color.fas and "
                + "                                                                                             tmp_plan_item.sar = tmp_plan_fas_color.sar and "
                + "                                                                                             tmp_plan_item.nar = tmp_plan_fas_color.nar  "
                + "                                                                                         Order by ncw),', ')) as color,"
                + "			(SELECT distinct id FROM tmp_plan_fas_new Where tmp_plan_item.fas = tmp_plan_fas_new.fas) as idNewFas,  "
                + "			(SELECT ARRAY_TO_STRING(ARRAY(SELECT vid FROM tmp_plan_fas_dekor Where tmp_plan_item.fas = tmp_plan_fas_dekor.fas and "
                + "                                                                                             tmp_plan_item.sar = tmp_plan_fas_dekor.sar and "
                + "                                                                                             tmp_plan_item.nar = tmp_plan_fas_dekor.nar  "
                + "                                                                                      Order by vid),', ')) as dekor   "
                + "		From tmp_plan_item "
                + "		Group by fas_vid, fas_pname, fas, kol_x, note_item, sar, nar ) as t2, tmp_plan_item "
                + " Where t2.fas_vid = tmp_plan_item.fas_vid and "
                + "	t2.fas_pname = tmp_plan_item.fas_pname and "
                + "	t2.fas = tmp_plan_item.fas and "
                + "	t2.sar = tmp_plan_item.sar and "
                + "	t2.nar = tmp_plan_item.nar and "
                + "	t2.kol_x = tmp_plan_item.kol_x and "
                + "	t2.note_item = tmp_plan_item.note_item "
                + " Group by t2.fas_vid, t2.fas_pname, t2.fas, t2.kolM, t2.kol_x, t2.note_item, t2.sar, t2.nar, polotno, color, idNewFas, dekor "
                + " Order by fas_vid, fas_pname, fas, sar, nar";

        try {
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                Vector tmp = new Vector();
                tmp.add(false);
                tmp.add(rs.getInt("fas_vid"));
                tmp.add(rs.getString("fas_pname"));
                tmp.add(rs.getInt("fas"));
                tmp.add(rs.getString("polotno"));
                tmp.add(Double.valueOf(UtilFunctions.formatNorm(rs.getDouble("kolM"), 0)));
                tmp.add(rs.getInt("kol_x"));
                tmp.add(rs.getString("nar"));
                tmp.add(rs.getString("minrst").equals(rs.getString("maxrst")) ? rs.getString("minrst") : rs.getString("minrst") + "--" + rs.getString("maxrst"));
                tmp.add(rs.getString("minrzm").equals(rs.getString("maxrzm")) ? rs.getString("minrzm") : rs.getString("minrzm") + "--" + rs.getString("maxrzm"));
                tmp.add(rs.getString("color"));
                tmp.add(rs.getString("dekor"));
                tmp.add(rs.getString("note_item"));

                if (rs.getObject("idNewFas") != null) tmp.add(UtilPlan.NEW);
                else tmp.add("");

                elements.add(tmp);
            }
        } catch (Exception e) {
            elements = new Vector();
            System.err.println("Ошибка getDataItemProjectStokTemp() " + e);
            log.error("Ошибка getDataItemProjectStokTemp()", e);
            throw new Exception("Ошибка getDataItemProjectStokTemp() " + e.getMessage(), e);
        }
        return elements;
    }

    /**
     *
     * @return
     * @throws Exception
     */
    public Vector getDataItemPlanDetalTemp() throws Exception {
        Vector elements = new Vector();
        String sql = "Select idPlanItem, cx, sar, t2.fas as fas, rst, rzm, kolM, kolD, idSpec, nameSpec, "
                + "         vnorm, idEmployees, fio, insDate, dekada1, dekada2, dekada3, note_item, "
                + "         tmp_plan_fas_new.id as idNewFas "
                + "  From (Select tmp_plan_item.id as idPlanItem, cx, sar, fas, rst, rzm, round(kol_month,3) as kolM, round(kol_day,3) as kolD, "
                + "             spec.id as idSpec, spec.spec as nameSpec, sum(round(spec_item.vnorm::numeric," + UtilPlan.ROUNDING_NORM + ")) as vnorm, "
                + "             employees.id as idEmployees, fio, tmp_plan_item.date_ins as insDate, dekada1, dekada2, dekada3, note_item "
                + "         From tmp_plan_item, spec, spec_item, employees "
                + "         Where spec.id = spec_item.id_spec and "
                + "                 tmp_plan_item.id_spec = spec.id and "
                + "                 tmp_plan_item.id_empl_ins = employees.id and "
                + "                 spec_item.num >= 0 and "
                + "                 spec_item.id_tech <> -1 "
                + "         Group by idPlanItem, cx, sar, fas, rst, rzm, kolM, kolD, idSpec, nameSpec, idEmployees, fio, insDate, dekada1, dekada2, dekada3, note_item ) as t2 "
                + "  left join tmp_plan_fas_new "
                + "  on t2.fas = tmp_plan_fas_new.fas "
                + "  Order by fas, sar, rst, rzm, idEmployees, fio, insDate, cx ";

        try {
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                Vector tmp = new Vector();
                tmp.add(false);
                tmp.add(rs.getInt("idPlanItem"));
                tmp.add(rs.getInt("sar"));
                tmp.add(rs.getInt("fas"));
                tmp.add(rs.getInt("rst"));
                tmp.add(rs.getInt("rzm"));
                tmp.add(rs.getDouble("kolM"));
                tmp.add(rs.getDouble("kolD"));
                tmp.add(rs.getInt("idSpec"));
                tmp.add(rs.getString("nameSpec"));
                tmp.add(UtilFunctions.formatNorm(rs.getDouble("vnorm"), UtilPlan.ROUNDING_NORM));
                //tmp.add(rs.getDouble("vnorm"));                
                tmp.add(Double.valueOf(UtilFunctions.formatNorm(Double.valueOf(rs.getDouble("kolM") * rs.getDouble("vnorm")), 3)));
                tmp.add(rs.getInt("idEmployees"));
                tmp.add(rs.getString("fio"));
                tmp.add(new SimpleDateFormat("dd-MM-yyyy").format(rs.getDate("insDate")));
                tmp.add(rs.getInt("cx"));
                tmp.add(UtilFunctions.formatNorm(rs.getDouble("dekada1"), 2));
                tmp.add(UtilFunctions.formatNorm(rs.getDouble("dekada2"), 2));
                tmp.add(UtilFunctions.formatNorm(rs.getDouble("dekada3"), 2));
                tmp.add(rs.getString("note_item"));

                if (rs.getObject("idNewFas") != null) tmp.add(UtilPlan.NEW);
                else tmp.add("");

                elements.add(tmp);
            }

        } catch (Exception e) {
            elements = new Vector();
            System.err.println("Ошибка getDataItemPlanDetalTemp() " + e);
            log.error("Ошибка getDataItemPlanDetalTemp()", e);
            throw new Exception("Ошибка getDataItemPlanDetalTemp() " + e.getMessage(), e);
        }
        return elements;
    }

    public Vector getDataItemProjectDetalTemp() throws Exception {
        Vector elements = new Vector();
        String sql = "Select fas_vid, fas_pname, fas, kolM, kol_x, note_item, cx, idSpec, nameSpec, sar, nar, sum(round(spec_item.vnorm::numeric," + UtilPlan.ROUNDING_NORM + ")) as vnorm,  polotno, color, idNewFas, dekor "
                + "	From (Select fas_vid, fas_pname, fas, sum(round(kol_month,3)) as kolM, kol_x, note_item, cx, spec.id as idSpec, "
                + "			spec.id_model as modelSpec, spec.spec as nameSpec, sar, nar, "
                + "			(SELECT ARRAY_TO_STRING(ARRAY(SELECT nar_polotno FROM tmp_plan_fas_sostav Where tmp_plan_item.fas = tmp_plan_fas_sostav.fas and "
                + "                                                                                                     tmp_plan_item.cx = tmp_plan_fas_sostav.cx and "
                + "                                                                                                     tmp_plan_item.sar = tmp_plan_fas_sostav.sar and "
                + "                                                                                                     tmp_plan_item.nar = tmp_plan_fas_sostav.nar "
                + "                                                                                                Order by tmp_plan_fas_sostav.vid, nar_polotno),', ')) as polotno,"
                + "			(SELECT ARRAY_TO_STRING(ARRAY(SELECT ncw FROM tmp_plan_fas_color Where tmp_plan_item.fas = tmp_plan_fas_color.fas and "
                + "                                                                                            tmp_plan_item.cx = tmp_plan_fas_color.cx and "
                + "                                                                                            tmp_plan_item.sar = tmp_plan_fas_color.sar and "
                + "                                                                                            tmp_plan_item.nar = tmp_plan_fas_color.nar "
                + "                                                                                      Order by ncw),', ')) as color,"
                + "			(SELECT distinct id FROM tmp_plan_fas_new Where tmp_plan_item.fas = tmp_plan_fas_new.fas) as idNewFas,  "
                + "			(SELECT ARRAY_TO_STRING(ARRAY(SELECT vid FROM tmp_plan_fas_dekor Where tmp_plan_item.fas = tmp_plan_fas_dekor.fas and "
                + "                                                                                            tmp_plan_item.cx = tmp_plan_fas_dekor.cx and "
                + "                                                                                            tmp_plan_item.sar = tmp_plan_fas_dekor.sar and "
                + "                                                                                            tmp_plan_item.nar = tmp_plan_fas_dekor.nar "
                + "                                                                                      Order by vid),', ')) as dekor   "
                + "		From tmp_plan_item, spec "
                + "		Where tmp_plan_item.id_spec = spec.id "
                + "		Group by fas_vid, fas_pname, fas, kol_x, note_item, cx, idSpec, modelSpec, nameSpec, sar, nar ) as t1, spec_item  "
                + "	Where t1.idSpec = spec_item.id_spec and "
                + "		spec_item.num >= 0 and  "
                + "		spec_item.id_tech <> -1 "
                + "	Group by fas_vid, fas_pname, fas, kolM, kol_x, note_item, cx, idSpec, modelSpec, nameSpec, sar, nar, polotno, color , idNewFas, dekor "
                + "     Order by fas, fas_pname, cx, sar, nar";
        try {
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                Vector tmp = new Vector();
                tmp.add(false);
                tmp.add(rs.getInt("fas_vid"));
                tmp.add(rs.getString("fas_pname"));
                tmp.add(rs.getInt("fas"));
                tmp.add(rs.getString("polotno"));
                tmp.add(Double.valueOf(UtilFunctions.formatNorm(rs.getDouble("kolM"), 0)));
                tmp.add(rs.getInt("kol_x"));
                tmp.add(rs.getInt("idSpec"));
                tmp.add(rs.getString("nameSpec"));
                tmp.add(UtilFunctions.formatNorm(rs.getDouble("vnorm"), UtilPlan.ROUNDING_NORM));
                tmp.add(Double.valueOf(UtilFunctions.formatNorm(Double.valueOf(rs.getDouble("kolM") * rs.getDouble("vnorm")), 3)));
                tmp.add(rs.getString("color"));
                tmp.add(rs.getString("dekor"));
                tmp.add(rs.getInt("cx"));
                tmp.add(rs.getString("note_item"));
                tmp.add(rs.getInt("sar"));
                tmp.add(rs.getString("nar"));

                if (rs.getObject("idNewFas") != null) tmp.add(UtilPlan.NEW);
                else tmp.add("");

                elements.add(tmp);
            }

        } catch (Exception e) {
            elements = new Vector();
            System.err.println("Ошибка getDataItemProjectDetalTemp() " + e);
            log.error("Ошибка getDataItemProjectDetalTemp()", e);
            throw new Exception("Ошибка getDataItemProjectDetalTemp() " + e.getMessage(), e);
        }
        return elements;
    }

    /**
     *
     * @return
     * @throws Exception
     */
    public Vector getDataItemPlanConvTemp() throws Exception {
        Vector elements = new Vector();
        String sql = "Select cx, t2.fas as fas, kolM, idSpec, modelSpec, nameSpec, vnorm, tmp_plan_fas_new.id as idNewFas "
                + " From (Select cx, fas, kolM, idSpec, modelSpec, nameSpec, sum(round(spec_item.vnorm::numeric," + UtilPlan.ROUNDING_NORM + ")) as vnorm "
                + "         From (Select cx, fas, sum(round(kol_month,3)) as kolM, spec.id as idSpec, "
                + "                     spec.id_model as modelSpec, spec.spec as nameSpec "
                + "                 From tmp_plan_item, spec "
                + "                 Where tmp_plan_item.id_spec = spec.id "
                + "                 Group by cx, fas, idSpec, modelSpec, nameSpec) as t1, spec_item "
                + "         Where t1.idSpec = spec_item.id_spec and "
                + "                 spec_item.num >= 0 and "
                + "                 spec_item.id_tech <> -1"
                + "         Group by cx, fas, kolM, idSpec, modelSpec, nameSpec ) as t2 "
                + "  left join tmp_plan_fas_new "
                + "     on t2.fas = tmp_plan_fas_new.fas "
                + " Order by fas, cx, modelSpec, nameSpec ";

        try {
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                Vector tmp = new Vector();
                tmp.add(false);
                tmp.add(rs.getInt("cx"));
                tmp.add(rs.getInt("idSpec"));
                tmp.add(rs.getInt("modelSpec"));
                tmp.add(rs.getString("nameSpec"));
                tmp.add(rs.getInt("fas"));
                tmp.add(Double.valueOf(UtilFunctions.formatNorm(rs.getDouble("kolM"), 0)));
                tmp.add(UtilFunctions.formatNorm(rs.getDouble("vnorm"), UtilPlan.ROUNDING_NORM));
                //   tmp.add(rs.getDouble("vnorm"));
                tmp.add(Double.valueOf(UtilFunctions.formatNorm(Double.valueOf(rs.getDouble("kolM") * rs.getDouble("vnorm")), 3)));

                if (rs.getObject("idNewFas") != null) tmp.add(UtilPlan.NEW);
                else tmp.add("");

                elements.add(tmp);
            }

        } catch (Exception e) {
            elements = new Vector();
            System.err.println("Ошибка getDataItemPlanConvTemp() " + e);
            log.error("Ошибка getDataItemPlanConvTemp()", e);
            throw new Exception("Ошибка getDataItemPlanConvTemp() " + e.getMessage(), e);
        }
        return elements;
    }

    public Vector getDataItemProjectVnormTemp() throws Exception {
        Vector elements = new Vector();
        String sql = "Select fas_vid, fas_pname, fas, kolM, kol_x, note_item, idSpec, modelSpec, nameSpec, sar, nar, sum(round(spec_item.vnorm::numeric," + UtilPlan.ROUNDING_NORM + ")) as vnorm,  polotno, idNewFas  "
                + "	From (Select fas_vid, fas_pname, fas, sum(round(kol_month,3)) as kolM, kol_x, note_item, spec.id as idSpec,   "
                + "			spec.id_model as modelSpec, spec.spec as nameSpec, sar, nar, "
                + "			(SELECT ARRAY_TO_STRING(ARRAY(SELECT nar_polotno FROM tmp_plan_fas_sostav Where tmp_plan_item.fas = tmp_plan_fas_sostav.fas and "
                + "                                                                                                     tmp_plan_item.sar = tmp_plan_fas_sostav.sar and "
                + "                                                                                                     tmp_plan_item.nar = tmp_plan_fas_sostav.nar  "
                + "                                                                                                 Order by tmp_plan_fas_sostav.vid, nar_polotno),', ')) as polotno,"
                + "			(SELECT distinct id FROM tmp_plan_fas_new Where tmp_plan_item.fas = tmp_plan_fas_new.fas) as idNewFas  "
                + "		From tmp_plan_item, spec "
                + "		Where tmp_plan_item.id_spec = spec.id "
                + "		Group by fas_vid, fas_pname, fas, kol_x, note_item, idSpec, modelSpec, nameSpec, sar, nar ) as t1, spec_item   "
                + "	Where t1.idSpec = spec_item.id_spec and  "
                + "		spec_item.num >= 0 and "
                + "		spec_item.id_tech <> -1 "
                + "	Group by fas_vid, fas_pname, fas, kolM, kol_x, note_item, idSpec, modelSpec, nameSpec, sar, nar, polotno, idNewFas "
                + " Order by fas, fas_pname, sar, nar, modelSpec, nameSpec ";

        try {
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                Vector tmp = new Vector();
                tmp.add(false);
                tmp.add(rs.getInt("fas_vid"));
                tmp.add(rs.getString("fas_pname"));
                tmp.add(rs.getInt("fas"));
                tmp.add(rs.getString("polotno"));
                tmp.add(Double.valueOf(UtilFunctions.formatNorm(rs.getDouble("kolM"), 0)));
                tmp.add(rs.getInt("kol_x"));
                tmp.add(rs.getInt("idSpec"));
                tmp.add(rs.getInt("modelSpec"));
                tmp.add(rs.getString("nameSpec"));
                tmp.add(UtilFunctions.formatNorm(rs.getDouble("vnorm"), UtilPlan.ROUNDING_NORM));
                tmp.add(Double.valueOf(UtilFunctions.formatNorm(Double.valueOf(rs.getDouble("kolM") * rs.getDouble("vnorm")), 3)));

                if (rs.getObject("idNewFas") != null) tmp.add(UtilPlan.NEW);
                else tmp.add("");

                elements.add(tmp);
            }

        } catch (Exception e) {
            elements = new Vector();
            System.err.println("Ошибка getDataItemProjectVnormTemp() " + e);
            log.error("Ошибка getDataItemProjectVnormTemp()", e);
            throw new Exception("Ошибка getDataItemProjectVnormTemp() " + e.getMessage(), e);
        }
        return elements;
    }

    /**
     *
     * @return
     * @throws Exception
     */
    public Vector getDataItemPlanDekadTemp() throws Exception {
        Vector elements = new Vector();
        String sql = "Select t2.fas as fas, fas_pname, id_spec, spec, kolM, dekada1, dekada2, dekada3, cx, tmp_plan_fas_new.id as idNewFas "
                + "     From (Select fas, fas_pname, id_spec, spec, kolM, dekada1, dekada2, dekada3, cx "
                + "             From (Select cx, fas, fas_pname, id_spec, spec, sum(round(kol_month,3)) as kolM, dekada1, dekada2, dekada3 "
                + "                     From tmp_plan_item, spec "
                + "                     Where tmp_plan_item.id_spec = spec.id  "
                + "                     Group by cx, fas, fas_pname, id_spec, spec, dekada1, dekada2, dekada3) as t1 "
                + "             left join s_model "
                + "             on fas = s_model.model 	) as t2 "
                + "     left join tmp_plan_fas_new "
                + "     on t2.fas = tmp_plan_fas_new.fas "
                + " Order by fas, fas_pname, cx";

        try {
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                Vector tmp = new Vector();
                tmp.add(false);
                tmp.add(rs.getInt("id_spec"));
                tmp.add(rs.getString("spec"));
                tmp.add((rs.getObject("fas_pname") == null) ? "" : rs.getString("fas_pname"));
                tmp.add(rs.getInt("fas"));
                tmp.add(Double.valueOf(UtilFunctions.formatNorm(rs.getDouble("kolM"), 0)));
                tmp.add(rs.getDouble("dekada1"));
                tmp.add(Double.valueOf(UtilFunctions.formatNorm(Double.valueOf(rs.getDouble("kolM") * (rs.getDouble("dekada1") / 100)), 3)));
                tmp.add(rs.getDouble("dekada2"));
                tmp.add(Double.valueOf(UtilFunctions.formatNorm(Double.valueOf(rs.getDouble("kolM") * (rs.getDouble("dekada2") / 100)), 3)));
                tmp.add(rs.getDouble("dekada3"));
                tmp.add(Double.valueOf(UtilFunctions.formatNorm(Double.valueOf(rs.getDouble("kolM") * (rs.getDouble("dekada3") / 100)), 3)));
                tmp.add(rs.getInt("cx"));

                if (rs.getObject("idNewFas") != null) tmp.add(UtilPlan.NEW);
                else tmp.add("");

                elements.add(tmp);
            }

        } catch (Exception e) {
            elements = new Vector();
            System.err.println("Ошибка getDataItemPlanDekadTemp() " + e);
            log.error("Ошибка getDataItemPlanDekadTemp()", e);
            throw new Exception("Ошибка getDataItemPlanDekadTemp() " + e.getMessage(), e);
        }
        return elements;
    }

    public Vector getDataItemProjectDekadTemp() throws Exception {
        Vector elements = new Vector();
        String sql = "Select fas_vid, fas_pname, fas, sum(round(kol_month,3)) as kolM, kol_x, dekada1, dekada2, dekada3, sar, nar, "
                + "		(SELECT ARRAY_TO_STRING(ARRAY(SELECT nar_polotno FROM tmp_plan_fas_sostav Where tmp_plan_item.fas = tmp_plan_fas_sostav.fas and "
                + "                                                                                             tmp_plan_item.sar = tmp_plan_fas_sostav.sar and "
                + "                                                                                             tmp_plan_item.nar = tmp_plan_fas_sostav.nar  "
                + "                                                                                         Order by tmp_plan_fas_sostav.vid, nar_polotno),', ')) as polotno,"
                + "		(SELECT ARRAY_TO_STRING(ARRAY(SELECT ncw FROM tmp_plan_fas_color Where tmp_plan_item.fas = tmp_plan_fas_color.fas and "
                + "                                                                                     tmp_plan_item.sar = tmp_plan_fas_color.sar and "
                + "                                                                                     tmp_plan_item.nar = tmp_plan_fas_color.nar "
                + "                                                                              Order by ncw),', ')) as color,"
                + "		(SELECT distinct id FROM tmp_plan_fas_new Where tmp_plan_item.fas = tmp_plan_fas_new.fas ) as idNewFas  "
                + "	From tmp_plan_item "
                + "	Group by fas_vid, fas_pname, fas, kol_x, dekada1, dekada2, dekada3, sar, nar, idNewFas  "
                + "     Order by fas, fas_pname, sar, nar ";

        try {
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                Vector tmp = new Vector();
                tmp.add(false);
                tmp.add(rs.getInt("fas_vid"));
                tmp.add(rs.getString("fas_pname"));
                tmp.add(rs.getInt("fas"));
                tmp.add(rs.getString("polotno"));
                tmp.add(Double.valueOf(UtilFunctions.formatNorm(rs.getDouble("kolM"), 0)));
                tmp.add(rs.getInt("kol_x"));
                tmp.add(rs.getDouble("dekada1"));
                tmp.add(Double.valueOf(UtilFunctions.formatNorm(Double.valueOf(rs.getDouble("kolM") * (rs.getDouble("dekada1") / 100)), 3)));
                tmp.add(rs.getDouble("dekada2"));
                tmp.add(Double.valueOf(UtilFunctions.formatNorm(Double.valueOf(rs.getDouble("kolM") * (rs.getDouble("dekada2") / 100)), 3)));
                tmp.add(rs.getDouble("dekada3"));
                tmp.add(Double.valueOf(UtilFunctions.formatNorm(Double.valueOf(rs.getDouble("kolM") * (rs.getDouble("dekada3") / 100)), 3)));

                if (rs.getObject("idNewFas") != null) tmp.add(UtilPlan.NEW);
                else tmp.add("");

                elements.add(tmp);
            }

        } catch (Exception e) {
            elements = new Vector();
            System.err.println("Ошибка getDataItemProjectDekadTemp() " + e);
            log.error("Ошибка getDataItemProjectDekadTemp()", e);
            throw new Exception("Ошибка getDataItemProjectDekadTemp() " + e.getMessage(), e);
        }
        return elements;
    }

    /**
     * Возвращает для редактирования краткий план
     * @param vec
     * @return
     * @throws Exception
     */
    public Vector getDataEditPlanTemp(Vector vec) throws Exception {
        Vector elements = new Vector();
        String sql = "Select idPlanItem, sar, t1.fas as fas, rst, rzm, kolM, idSpec, nameSpec, vnorm, cx, dekada1, dekada2, dekada3, note_item, tmp_plan_fas_new.id as idNewFas  "
                + "     From(Select tmp_plan_item.id as idPlanItem, sar, fas, rst, rzm, round(kol_month,3) as kolM, "
                + "                 spec.id as idSpec, spec.spec as nameSpec, "
                + "                 sum(round(spec_item.vnorm::numeric," + UtilPlan.ROUNDING_NORM + ")) as vnorm, cx,"
                + "                 dekada1, dekada2, dekada3, note_item  "
                + "             From tmp_plan_item, spec, spec_item "
                + "             Where spec.id = spec_item.id_spec and "
                + "                     tmp_plan_item.id_spec = spec.id and "
                + "                     tmp_plan_item.id_spec = ? and "
                + "                     tmp_plan_item.fas = ? and "
                + "                     spec_item.num >= 0 and "
                + "                     spec_item.id_tech <> -1 "
                + "             Group by idPlanItem, cx, sar, fas, rst, rzm, kolM, idSpec, nameSpec, dekada1, dekada2, dekada3, note_item ) as t1 "
                + "     left join tmp_plan_fas_new "
                + "     on t1.fas = tmp_plan_fas_new.fas "
                + "     Order by fas, sar, rst, rzm, cx";

        try {
            ps = conn.prepareStatement(sql);
            Iterator it = vec.iterator();
            while (it.hasNext()) {
                Vector items = (Vector) it.next();
                ps.setInt(1, Integer.valueOf(items.get(0).toString()));
                ps.setInt(2, Integer.valueOf(items.get(1).toString()));
                rs = ps.executeQuery();
                while (rs.next()) {
                    Vector tmp = new Vector();
                    tmp.add(false);
                    tmp.add(0);
                    tmp.add(rs.getInt("idPlanItem"));
                    tmp.add(rs.getInt("sar"));
                    tmp.add(rs.getInt("fas"));
                    tmp.add(rs.getInt("rst"));
                    tmp.add(rs.getInt("rzm"));
                    tmp.add(rs.getDouble("kolM"));
                    tmp.add(rs.getInt("idSpec"));
                    tmp.add(rs.getString("nameSpec"));
                    tmp.add(UtilFunctions.formatNorm(rs.getDouble("vnorm"), UtilPlan.ROUNDING_NORM));
                    tmp.add(Double.valueOf(UtilFunctions.formatNorm(Double.valueOf(rs.getDouble("kolM") * rs.getDouble("vnorm")), 3)));
                    tmp.add(rs.getInt("cx"));
                    tmp.add(UtilFunctions.formatNorm(rs.getDouble("dekada1"), 2));
                    tmp.add(UtilFunctions.formatNorm(rs.getDouble("dekada2"), 2));
                    tmp.add(UtilFunctions.formatNorm(rs.getDouble("dekada3"), 2));
                    tmp.add(rs.getString("note_item"));

                    if (rs.getObject("idNewFas") != null) tmp.add(UtilPlan.NEW);
                    else tmp.add("");

                    elements.add(tmp);
                }
            }
        } catch (Exception e) {
            elements = new Vector();
            System.err.println("Ошибка getDataEditPlanTemp() " + e);
            log.error("Ошибка getDataEditPlanTemp()", e);
            throw new Exception("Ошибка getDataEditPlanTemp() " + e.getMessage(), e);
        }
        return elements;
    }

    /**
     * Возвращает для редактирования подробный план
     * @param vec
     * @return
     * @throws Exception
     */
    public Vector getDataEditPlanDetalTemp(Vector vec) throws Exception {
        Vector elements = new Vector();
        String sql = "Select idPlanItem, sar, t1.fas as fas, rst, rzm, kolM, idSpec, nameSpec, vnorm, cx, dekada1, dekada2, dekada3, note_item, tmp_plan_fas_new.id as idNewFas  "
                + "     From(Select tmp_plan_item.id as idPlanItem, sar, fas, rst, rzm, round(kol_month,3) as kolM, "
                + "                 spec.id as idSpec, spec.spec as nameSpec, "
                + "                 sum(round(spec_item.vnorm::numeric," + UtilPlan.ROUNDING_NORM + ")) as vnorm, cx,"
                + "                 dekada1, dekada2, dekada3, note_item  "
                + "             From tmp_plan_item, spec, spec_item "
                + "             Where spec.id = spec_item.id_spec and "
                + "                     tmp_plan_item.id_spec = spec.id and "
                + "                     tmp_plan_item.id = ? and "
                + "                     spec_item.num >= 0 and "
                + "                     spec_item.id_tech <> -1 "
                + "             Group by idPlanItem, cx, sar, fas, rst, rzm, kolM, idSpec, nameSpec, dekada1, dekada2, dekada3, note_item ) as t1 "
                + "     left join tmp_plan_fas_new "
                + "     on t1.fas = tmp_plan_fas_new.fas "
                + "     Order by fas, sar, rst, rzm, cx";

        try {
            ps = conn.prepareStatement(sql);
            Iterator it = vec.iterator();
            while (it.hasNext()) {
                ps.setInt(1, Integer.valueOf((it.next()).toString()));
                rs = ps.executeQuery();
                while (rs.next()) {
                    Vector tmp = new Vector();
                    tmp.add(false);
                    tmp.add(0);
                    tmp.add(rs.getInt("idPlanItem"));
                    tmp.add(rs.getInt("sar"));
                    tmp.add(rs.getInt("fas"));
                    tmp.add(rs.getInt("rst"));
                    tmp.add(rs.getInt("rzm"));
                    tmp.add(rs.getDouble("kolM"));
                    tmp.add(rs.getInt("idSpec"));
                    tmp.add(rs.getString("nameSpec"));
                    tmp.add(UtilFunctions.formatNorm(rs.getDouble("vnorm"), UtilPlan.ROUNDING_NORM));
                    tmp.add(Double.valueOf(UtilFunctions.formatNorm(Double.valueOf(rs.getDouble("kolM") * rs.getDouble("vnorm")), 3)));
                    tmp.add(rs.getInt("cx"));
                    tmp.add(UtilFunctions.formatNorm(rs.getDouble("dekada1"), 2));
                    tmp.add(UtilFunctions.formatNorm(rs.getDouble("dekada2"), 2));
                    tmp.add(UtilFunctions.formatNorm(rs.getDouble("dekada3"), 2));
                    tmp.add(rs.getString("note_item"));

                    if (rs.getObject("idNewFas") != null) tmp.add(UtilPlan.NEW);
                    else tmp.add("");

                    elements.add(tmp);
                }
            }

        } catch (Exception e) {
            elements = new Vector();
            System.err.println("Ошибка getDataEditPlanDetalTemp() " + e);
            log.error("Ошибка getDataEditPlanDetalTemp()", e);
            throw new Exception("Ошибка getDataEditPlanDetalTemp() " + e.getMessage(), e);
        }
        return elements;
    }

    /**
     * Возвращает для редактирования конвейера
     * @param vec
     * @return
     * @throws Exception
     */
    public Vector getDataEditPlanConvTemp(Vector vec) throws Exception {
        Vector elements = new Vector();
        String sql = "Select idPlanItem, sar, t1.fas as fas, rst, rzm, kolM, idSpec, nameSpec, vnorm, cx, dekada1, dekada2, dekada3, note_item, tmp_plan_fas_new.id as idNewFas  "
                + "     From(Select tmp_plan_item.id as idPlanItem, sar, fas, rst, rzm, round(kol_month,3) as kolM, "
                + "                 spec.id as idSpec, spec.spec as nameSpec, "
                + "                 sum(round(spec_item.vnorm::numeric," + UtilPlan.ROUNDING_NORM + ")) as vnorm, cx,"
                + "                 dekada1, dekada2, dekada3, note_item  "
                + "         From tmp_plan_item, spec, spec_item "
                + "         Where spec.id = spec_item.id_spec and "
                + "                 tmp_plan_item.id_spec = spec.id and "
                + "                 tmp_plan_item.id_spec = ? and "
                + "                 tmp_plan_item.fas = ? and "
                + "                 tmp_plan_item.cx = ? and "
                + "                 spec_item.num >= 0 and "
                + "                 spec_item.id_tech <> -1 "
                + "         Group by idPlanItem, cx, sar, fas, rst, rzm, kolM, idSpec, nameSpec, dekada1, dekada2, dekada3, note_item ) as t1 "
                + "     left join tmp_plan_fas_new "
                + "     on t1.fas = tmp_plan_fas_new.fas "
                + "     Order by fas, sar, rst, rzm, cx";

        try {
            ps = conn.prepareStatement(sql);
            Iterator it = vec.iterator();
            while (it.hasNext()) {
                Vector items = (Vector) it.next();
                ps.setInt(1, Integer.valueOf(items.get(0).toString()));
                ps.setInt(2, Integer.valueOf(items.get(1).toString()));
                ps.setInt(3, Integer.valueOf(items.get(2).toString()));
                rs = ps.executeQuery();
                while (rs.next()) {
                    Vector tmp = new Vector();
                    tmp.add(false);
                    tmp.add(0);
                    tmp.add(rs.getInt("idPlanItem"));
                    tmp.add(rs.getInt("sar"));
                    tmp.add(rs.getInt("fas"));
                    tmp.add(rs.getInt("rst"));
                    tmp.add(rs.getInt("rzm"));
                    tmp.add(rs.getDouble("kolM"));
                    tmp.add(rs.getInt("idSpec"));
                    tmp.add(rs.getString("nameSpec"));
                    tmp.add(UtilFunctions.formatNorm(rs.getDouble("vnorm"), UtilPlan.ROUNDING_NORM));
                    tmp.add(Double.valueOf(UtilFunctions.formatNorm(Double.valueOf(rs.getDouble("kolM") * rs.getDouble("vnorm")), 3)));
                    tmp.add(rs.getInt("cx"));
                    tmp.add(UtilFunctions.formatNorm(rs.getDouble("dekada1"), 2));
                    tmp.add(UtilFunctions.formatNorm(rs.getDouble("dekada2"), 2));
                    tmp.add(UtilFunctions.formatNorm(rs.getDouble("dekada3"), 2));
                    tmp.add(rs.getString("note_item"));

                    if (rs.getObject("idNewFas") != null) tmp.add(UtilPlan.NEW);
                    else tmp.add("");

                    elements.add(tmp);
                }
            }

        } catch (Exception e) {
            elements = new Vector();
            System.err.println("Ошибка getDataEditPlanConvTemp() " + e);
            log.error("Ошибка getDataEditPlanConvTemp()", e);
            throw new Exception("Ошибка getDataEditPlanConvTemp() " + e.getMessage(), e);
        }
        return elements;
    }

    /**
     * Возвращает для редактирования декады
     * @param vec
     * @return
     * @throws Exception
     */
    public Vector getDataEditPlanDekadTemp(Vector vec) throws Exception {
        Vector elements = new Vector();
        String sql = "Select idPlanItem, sar, t1.fas as fas, rst, rzm, kolM, idSpec, nameSpec, vnorm, cx, dekada1, dekada2, dekada3, note_item, tmp_plan_fas_new.id as idNewFas  "
                + "     From(Select tmp_plan_item.id as idPlanItem, sar, fas, rst, rzm, round(kol_month,3) as kolM, "
                + "                 spec.id as idSpec, spec.spec as nameSpec, "
                + "                 sum(round(spec_item.vnorm::numeric," + UtilPlan.ROUNDING_NORM + ")) as vnorm, cx, "
                + "                 dekada1, dekada2, dekada3, note_item  "
                + "             From tmp_plan_item, spec, spec_item "
                + "             Where spec.id = spec_item.id_spec and "
                + "                     tmp_plan_item.id_spec = spec.id and "
                + "                     tmp_plan_item.id_spec = ? and "
                + "                     tmp_plan_item.fas = ? and "
                + "                     tmp_plan_item.cx = ? and "
                + "                     spec_item.num >= 0 and "
                + "                     spec_item.id_tech <> -1 "
                + "             Group by idPlanItem, cx, sar, fas, rst, rzm, kolM, idSpec, nameSpec, "
                + "                         dekada1, dekada2, dekada3, note_item ) as t1 "
                + "     left join tmp_plan_fas_new "
                + "     on t1.fas = tmp_plan_fas_new.fas "
                + "     Order by fas, sar, rst, rzm, cx";

        try {
            ps = conn.prepareStatement(sql);
            Iterator it = vec.iterator();
            while (it.hasNext()) {
                Vector items = (Vector) it.next();
                ps.setInt(1, Integer.valueOf(items.get(0).toString()));
                ps.setInt(2, Integer.valueOf(items.get(1).toString()));
                ps.setInt(3, Integer.valueOf(items.get(2).toString()));
                rs = ps.executeQuery();
                while (rs.next()) {
                    Vector tmp = new Vector();
                    tmp.add(false);
                    tmp.add(0);
                    tmp.add(rs.getInt("idPlanItem"));
                    tmp.add(rs.getInt("sar"));
                    tmp.add(rs.getInt("fas"));
                    tmp.add(rs.getInt("rst"));
                    tmp.add(rs.getInt("rzm"));
                    tmp.add(rs.getDouble("kolM"));
                    tmp.add(rs.getInt("idSpec"));
                    tmp.add(rs.getString("nameSpec"));
                    tmp.add(UtilFunctions.formatNorm(rs.getDouble("vnorm"), UtilPlan.ROUNDING_NORM));
                    tmp.add(Double.valueOf(UtilFunctions.formatNorm(Double.valueOf(rs.getDouble("kolM") * rs.getDouble("vnorm")), 3)));
                    tmp.add(rs.getInt("cx"));
                    tmp.add(UtilFunctions.formatNorm(rs.getDouble("dekada1"), 2));
                    tmp.add(UtilFunctions.formatNorm(rs.getDouble("dekada2"), 2));
                    tmp.add(UtilFunctions.formatNorm(rs.getDouble("dekada3"), 2));
                    tmp.add(rs.getString("note_item"));

                    if (rs.getObject("idNewFas") != null) tmp.add(UtilPlan.NEW);
                    else tmp.add("");

                    elements.add(tmp);
                }
            }

        } catch (Exception e) {
            elements = new Vector();
            System.err.println("Ошибка getDataEditPlanDekadTemp() " + e);
            log.error("Ошибка getDataEditPlanDekadTemp()", e);
            throw new Exception("Ошибка getDataEditPlanDekadTemp() " + e.getMessage(), e);
        }
        return elements;
    }

    /**
     * Возвращает для редактирования новые модели 
     * @return
     * @throws Exception
     */
    public Vector getDataEditPlanNewModelsTemp() throws Exception {
        Vector elements = new Vector();
        String sql = "Select model, fas "
                + " From(Select distinct fas as model From tmp_plan_item ) as t1 "
                + " left join tmp_plan_fas_new "
                + " on model = fas "
                + " Order by model";

        try {
            ps = conn.prepareStatement(sql);

            rs = ps.executeQuery();
            while (rs.next()) {
                Vector tmp = new Vector();
                tmp.add(rs.getObject("fas") != null ? true : false);
                tmp.add(rs.getInt("model"));
                elements.add(tmp);
            }

        } catch (Exception e) {
            elements = new Vector();
            System.err.println("Ошибка getDataEditPlanNewModelsTemp() " + e);
            log.error("Ошибка getDataEditPlanNewModelsTemp()", e);
            throw new Exception("Ошибка getDataEditPlanNewModelsTemp() " + e.getMessage(), e);
        }
        return elements;
    }

    /**
     * Редактирует временную таблицу tmp_plan_item
     * @param data
     * @param dayWork
     * @param idEmpl
     * @return
     * @throws Exception
     */
    public boolean saveDataEditPlanTemp(Vector data, int dayWork, int idEmpl) throws Exception {
        boolean rezalt = false;
        String sql = "";
        try {
            setAutoCommit(false);

            Iterator it = data.iterator();
            while (it.hasNext()) {
                Vector items = (Vector) it.next();

                if ((items.get(1).toString()).equals("-1")) {

                    sql = "Delete From tmp_plan_item Where tmp_plan_item.id = ?";

                    ps = conn.prepareStatement(sql);
                    ps.setInt(1, Integer.valueOf(items.get(2).toString()));
                    ps.execute();

                } else if ((items.get(1).toString()).equals("0")) {

                    sql = "UPDATE tmp_plan_item "
                            + "SET sar = ? , "
                            + "     fas = ? , "
                            + "     rst = ? , "
                            + "     rzm = ?, "
                            + "     kol_month = ?, "
                            + "     kol_day = ?, "
                            + "     id_spec = ?, "
                            + "     id_empl_ins = ?, "
                            + "     date_ins = now(), "
                            + "     cx = ?, "
                            + "     dekada1 = ?, "
                            + "     dekada2 = ?, "
                            + "     dekada3 = ?, "
                            + "     note_item = ? "
                            + " WHERE tmp_plan_item.id = ? ";

                    ps = conn.prepareStatement(sql);
                    ps.setInt(1, Integer.valueOf(items.get(3).toString()));
                    ps.setInt(2, Integer.valueOf(items.get(4).toString()));
                    ps.setInt(3, Integer.valueOf(items.get(5).toString()));
                    ps.setInt(4, Integer.valueOf(items.get(6).toString()));
                    ps.setDouble(5, Double.valueOf(items.get(7).toString()));
                    ps.setDouble(6, Double.valueOf(UtilFunctions.formatNorm(Double.valueOf(items.get(7).toString()) / dayWork, 3)));
                    ps.setInt(7, Integer.valueOf(items.get(8).toString()));
                    ps.setInt(8, idEmpl);
                    ps.setInt(9, Integer.valueOf(items.get(12).toString()));
                    ps.setDouble(10, Double.valueOf(items.get(13).toString()));
                    ps.setDouble(11, Double.valueOf(items.get(14).toString()));
                    ps.setDouble(12, Double.valueOf(items.get(15).toString()));
                    ps.setString(13, items.get(16).toString().trim());
                    ps.setInt(14, Integer.valueOf(items.get(2).toString()));
                    ps.execute();

                } else if ((items.get(1).toString()).equals("1")) {

                    sql = "Insert into tmp_plan_item(cx, sar, fas, rst, rzm, kol_month, kol_day, "
                            + "     id_spec, id_empl_ins, dekada1, dekada2, dekada3, note_item) "
                            + " values( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

                    ps = conn.prepareStatement(sql);
                    ps.setInt(1, Integer.valueOf(items.get(12).toString()));
                    ps.setInt(2, Integer.valueOf(items.get(3).toString()));
                    ps.setInt(3, Integer.valueOf(items.get(4).toString()));
                    ps.setInt(4, Integer.valueOf(items.get(5).toString()));
                    ps.setInt(5, Integer.valueOf(items.get(6).toString()));
                    ps.setDouble(6, Double.valueOf(items.get(7).toString()));
                    ps.setDouble(7, Double.valueOf(UtilFunctions.formatNorm(Double.valueOf(items.get(7).toString()) / dayWork, 3)));
                    ps.setInt(8, Integer.valueOf(items.get(8).toString()));
                    ps.setInt(9, idEmpl);
                    ps.setDouble(10, Double.valueOf(items.get(13).toString()));
                    ps.setDouble(11, Double.valueOf(items.get(14).toString()));
                    ps.setDouble(12, Double.valueOf(items.get(15).toString()));
                    ps.setString(13, items.get(16).toString().trim());
                    ps.execute();
                }
            }

            commit();

            rezalt = true;
        } catch (Exception e) {
            rollBack();
            rezalt = false;
            System.err.println("Ошибка saveDataEditPlanTemp() " + e);
            log.error("Ошибка saveDataEditPlanTemp()", e);
            throw new Exception("Ошибка saveDataEditPlanTemp() " + e.getMessage(), e);
        } finally {
            setAutoCommit(true);
        }
        return rezalt;
    }

    public boolean saveDataEditSostavProjectTemp(Vector data, int idEmpl) throws Exception {
        boolean rezalt = false;
        String sql = "";
        try {
            setAutoCommit(false);

            Iterator it = data.iterator();
            while (it.hasNext()) {
                Vector items = (Vector) it.next();

                sql = "UPDATE tmp_plan_fas_sostav "
                        + " SET rashod = ? "
                        + " Where id = ? ";

                ps = conn.prepareStatement(sql);
                ps.setDouble(1, Double.valueOf(items.get(10).toString()));
                ps.setInt(2, Integer.valueOf(items.get(1).toString()));
                ps.execute();
            }

            commit();

            rezalt = true;
        } catch (Exception e) {
            rollBack();
            rezalt = false;
            System.err.println("Ошибка saveDataEditSostavProjectTemp() " + e);
            log.error("Ошибка saveDataEditSostavProjectTemp()", e);
            throw new Exception("Ошибка saveDataEditSostavProjectTemp() " + e.getMessage(), e);
        } finally {
            setAutoCommit(true);
        }
        return rezalt;
    }

    /**
     * Редактирует временную таблицу plan_fas_new
     * @param data
     * @return
     * @throws Exception
     */
    public boolean saveNewModelsEditPlanTemp(Vector data) throws Exception {
        boolean rezalt = false;
        String sql = "";
        try {
            setAutoCommit(false);

            sql = "Delete From tmp_plan_fas_new";
            ps = conn.prepareStatement(sql);
            ps.execute();

            sql = "Select distinct fas From tmp_plan_fas_new";
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            Iterator it = data.iterator();
            while (it.hasNext()) {
                sql = "Insert into tmp_plan_fas_new(fas) values( ?)";
                ps = conn.prepareStatement(sql);
                ps.setInt(1, Integer.valueOf(it.next().toString()));
                ps.execute();
            }

            commit();

            rezalt = true;
        } catch (Exception e) {
            rollBack();
            rezalt = false;
            System.err.println("Ошибка saveNewModelsEditPlanTemp() " + e);
            log.error("Ошибка saveNewModelsEditPlanTemp()", e);
            throw new Exception("Ошибка saveNewModelsEditPlanTemp() " + e.getMessage(), e);
        } finally {
            setAutoCommit(true);
        }
        return rezalt;
    }

    /**
     * Добавляет новую запись в проект плана. 
     * (временные таблицы: tmp_plan_item, tmp_plan_fas_new, tmp_plan_fas_dekor, tmp_plan_fas_sostav, tmp_plan_fas_color)
     * @param fasNum
     * @param fasName
     * @param tModelShkala
     * @param fasVid
     * @param ShPrint
     * @param Vyshivka
     * @param kolvoX2
     * @param fasNew
     * @param specNum
     * @param convText
     * @param tModelSostav
     * @param tModelColor
     * @param noteText
     * @return
     * @throws Exception
     */
    public boolean addItemTempProjectTable(int idEmpl, int fasNum, String fasName, Vector tModelShkala, int fasVid,
                                           boolean shPrint, boolean vyshivka, int kolvoX, boolean fasNew, int specNum,
                                           int convText, int sarText, String narText, Vector tModelSostav, Vector tModelColor, String noteText) throws Exception {

        boolean rezalt = false;
        String sql = "";
        try {
            setAutoCommit(false);
            
            /*
            sql = "Delete From tmp_plan_item Where fas = ? and cx = ? and sar = ? and nar like ? ";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, fasNum);
            ps.setInt(2, convText);   
            ps.setInt(3, sarText); 
            ps.setString(4, narText);   
            ps.execute(); 
            */

            Iterator it = tModelShkala.iterator();
            while (it.hasNext()) {
                Vector items = (Vector) it.next();

                sql = "Insert into tmp_plan_item(cx, sar, fas, rst, rzm, kol_month, kol_day, "
                        + "     id_spec, id_empl_ins, dekada1, dekada2, dekada3, note_item, fas_vid, fas_pname, kol_x, nar) "
                        + " values( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

                ps = conn.prepareStatement(sql);
                ps.setInt(1, convText);
                ps.setInt(2, sarText);
                ps.setInt(3, fasNum);
                ps.setInt(4, Integer.valueOf(items.get(1).toString()));
                ps.setInt(5, Integer.valueOf(items.get(2).toString()));
                ps.setDouble(6, Double.valueOf(items.get(3).toString()));
                ps.setDouble(7, 1);
                ps.setInt(8, specNum);
                ps.setInt(9, idEmpl);
                ps.setDouble(10, Double.valueOf(items.get(4).toString()));
                ps.setDouble(11, Double.valueOf(items.get(5).toString()));
                ps.setDouble(12, Double.valueOf(items.get(6).toString()));
                ps.setString(13, noteText);
                ps.setInt(14, fasVid);
                ps.setString(15, fasName);
                ps.setInt(16, kolvoX);
                ps.setString(17, narText);
                ps.execute();
            }

            sql = "Delete From tmp_plan_fas_new Where fas = ? ";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, fasNum);
            ps.execute();

            if (fasNew) {
                sql = "Insert into tmp_plan_fas_new( fas) values( ?)";

                ps = conn.prepareStatement(sql);
                ps.setInt(1, fasNum);
                ps.execute();
            }
            
            /*
            sql = "Delete From tmp_plan_fas_dekor Where fas = ?";
            ps = conn.prepareStatement(sql);         
            ps.setInt(1, fasNum);
            ps.execute(); 
            */

            if (shPrint) {
                sql = "Insert into tmp_plan_fas_dekor( fas, vid, cx, sar, nar) values( ?, ?, ?, ?, ?)";

                ps = conn.prepareStatement(sql);
                ps.setInt(1, fasNum);
                ps.setInt(2, 1);
                ps.setInt(3, convText);
                ps.setInt(4, sarText);
                ps.setString(5, narText);
                ps.execute();
            }

            if (vyshivka) {
                sql = "Insert into tmp_plan_fas_dekor( fas, vid, cx, sar, nar) values( ?, ?, ?, ?, ?)";

                ps = conn.prepareStatement(sql);
                ps.setInt(1, fasNum);
                ps.setInt(2, 2);
                ps.setInt(3, convText);
                ps.setInt(4, sarText);
                ps.setString(5, narText);
                ps.execute();
            }
            
            /*
            sql = "Delete From tmp_plan_fas_sostav Where fas = ?";
            ps = conn.prepareStatement(sql);         
            ps.setInt(1, fasNum);
            ps.execute(); 
            */

            it = tModelSostav.iterator();
            while (it.hasNext()) {
                Vector items = (Vector) it.next();

                sql = "Insert into tmp_plan_fas_sostav( fas, id_polotno, nar_polotno, sostav, vid, rashod, cx, sar, nar)"
                        + " values( ?, ?, ?, ?, ?, ?, ?, ?, ?)";

                ps = conn.prepareStatement(sql);
                ps.setInt(1, fasNum);
                ps.setInt(2, Integer.valueOf(items.get(1).toString()));
                ps.setString(3, items.get(2).toString());
                ps.setString(4, items.get(3).toString());
                ps.setInt(5, Integer.valueOf(items.get(4).toString()));
                ps.setDouble(6, Double.valueOf(items.get(5).toString()));
                ps.setInt(7, convText);
                ps.setInt(8, sarText);
                ps.setString(9, narText);
                ps.execute();
            }
            
            /*
            sql = "Delete From tmp_plan_fas_color Where fas = ?";
            ps = conn.prepareStatement(sql);         
            ps.setInt(1, fasNum);
            ps.execute(); 
            */

            it = tModelColor.iterator();
            while (it.hasNext()) {
                Vector items = (Vector) it.next();

                sql = "Insert into tmp_plan_fas_color( fas, id_color, ncw, cx, sar, nar) values( ?, ?, ?, ?, ?, ?)";

                ps = conn.prepareStatement(sql);
                ps.setInt(1, fasNum);
                ps.setInt(2, Integer.valueOf(items.get(1).toString()));
                ps.setString(3, items.get(2).toString());
                ps.setInt(4, convText);
                ps.setInt(5, sarText);
                ps.setString(6, narText);
                ps.execute();
            }

            commit();

            rezalt = true;
        } catch (Exception e) {
            rollBack();
            rezalt = false;
            System.err.println("Ошибка addItemTempProjectTable() " + e);
            log.error("Ошибка addItemTempProjectTable()", e);
            throw new Exception("Ошибка addItemTempProjectTable() " + e.getMessage(), e);
        } finally {
            setAutoCommit(true);
        }
        return rezalt;
    }

    public boolean updateItemTempProjectTable(Vector update, int idEmpl, int fasNum, String fasName, Vector tModelShkala, int fasVid,
                                              boolean shPrint, boolean vyshivka, int kolvoX, boolean fasNew, int specNum,
                                              int convText, int sarText, String narText, Vector tModelSostav, Vector tModelColor, String noteText) throws Exception {

        boolean rezalt = false;
        String sql = "";

        try {
            setAutoCommit(false);

            int fas_ = Integer.valueOf(update.get(0).toString());
            int sar_ = Integer.valueOf(update.get(1).toString());
            String nar_ = update.get(2).toString().trim().toUpperCase();

            sql = "Delete From tmp_plan_item "
                    + "       Where fas = ? and "
                    + "		sar = ? and "
                    + "             nar like ? ";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, fas_);
            ps.setInt(2, sar_);
            ps.setString(3, nar_);
            ps.execute();

            sql = "Delete From tmp_plan_item "
                    + "       Where fas = ? and "
                    + "		cx = ? and "
                    + "		sar = ? and "
                    + "             nar like ? ";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, fasNum);
            ps.setInt(2, convText);
            ps.setInt(3, sarText);
            ps.setString(4, narText);
            ps.execute();

            Iterator it = tModelShkala.iterator();
            while (it.hasNext()) {
                Vector items = (Vector) it.next();

                sql = "Insert into tmp_plan_item(cx, sar, fas, rst, rzm, kol_month, kol_day, "
                        + "     id_spec, id_empl_ins, dekada1, dekada2, dekada3, note_item, fas_vid, fas_pname, kol_x, nar) "
                        + " values( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

                ps = conn.prepareStatement(sql);
                ps.setInt(1, convText);
                ps.setInt(2, sarText);
                ps.setInt(3, fasNum);
                ps.setInt(4, Integer.valueOf(items.get(1).toString()));
                ps.setInt(5, Integer.valueOf(items.get(2).toString()));
                ps.setDouble(6, Double.valueOf(items.get(3).toString()));
                ps.setDouble(7, 1);
                ps.setInt(8, specNum);
                ps.setInt(9, idEmpl);
                ps.setDouble(10, Double.valueOf(items.get(4).toString()));
                ps.setDouble(11, Double.valueOf(items.get(5).toString()));
                ps.setDouble(12, Double.valueOf(items.get(6).toString()));
                ps.setString(13, noteText);
                ps.setInt(14, fasVid);
                ps.setString(15, fasName);
                ps.setInt(16, kolvoX);
                ps.setString(17, narText);
                ps.execute();
            }

            sql = "Delete From tmp_plan_fas_new Where fas = ? ";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, fas_);
            ps.execute();

            sql = "Delete From tmp_plan_fas_new Where fas = ? ";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, fasNum);
            ps.execute();

            if (fasNew) {
                sql = "Insert into tmp_plan_fas_new( fas) values( ?)";

                ps = conn.prepareStatement(sql);
                ps.setInt(1, fasNum);
                ps.execute();
            }

            sql = "Delete From tmp_plan_fas_dekor "
                    + " Where fas = ? "
                    + " and sar = ? "
                    + " and nar like ? ";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, fas_);
            ps.setInt(2, sar_);
            ps.setString(3, nar_);
            ps.execute();

            sql = "Delete From tmp_plan_fas_dekor Where fas = ? and cx = ? and sar = ? and nar like ? ";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, fasNum);
            ps.setInt(2, convText);
            ps.setInt(3, sarText);
            ps.setString(4, narText);
            ps.execute();

            if (shPrint) {
                sql = "Insert into tmp_plan_fas_dekor( fas, vid, cx, sar, nar) values( ?, ?, ?, ?, ?)";

                ps = conn.prepareStatement(sql);
                ps.setInt(1, fasNum);
                ps.setInt(2, 1);
                ps.setInt(3, convText);
                ps.setInt(4, sarText);
                ps.setString(5, narText);
                ps.execute();
            }

            if (vyshivka) {
                sql = "Insert into tmp_plan_fas_dekor( fas, vid, cx, sar, nar) values( ?, ?, ?, ?, ?)";

                ps = conn.prepareStatement(sql);
                ps.setInt(1, fasNum);
                ps.setInt(2, 2);
                ps.setInt(3, convText);
                ps.setInt(4, sarText);
                ps.setString(5, narText);
                ps.execute();
            }

            sql = "Delete From tmp_plan_fas_sostav "
                    + " Where fas = ? "
                    + " and sar = ? "
                    + " and nar like ? ";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, fas_);
            ps.setInt(2, sar_);
            ps.setString(3, nar_);
            ps.execute();

            sql = "Delete From tmp_plan_fas_sostav Where fas = ? and cx = ? and sar = ? and nar like ? ";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, fasNum);
            ps.setInt(2, convText);
            ps.setInt(3, sarText);
            ps.setString(4, narText);
            ps.execute();

            it = tModelSostav.iterator();
            while (it.hasNext()) {
                Vector items = (Vector) it.next();

                sql = "Insert into tmp_plan_fas_sostav( fas, id_polotno, nar_polotno, sostav, vid, rashod, cx, sar, nar)"
                        + " values( ?, ?, ?, ?, ?, ?, ?, ?, ?)";

                ps = conn.prepareStatement(sql);
                ps.setInt(1, fasNum);
                ps.setInt(2, Integer.valueOf(items.get(1).toString()));
                ps.setString(3, items.get(2).toString());
                ps.setString(4, items.get(3).toString());
                ps.setInt(5, Integer.valueOf(items.get(4).toString()));
                ps.setDouble(6, Double.valueOf(items.get(5).toString()));
                ps.setInt(7, convText);
                ps.setInt(8, sarText);
                ps.setString(9, narText);
                ps.execute();
            }

            sql = "Delete From tmp_plan_fas_color "
                    + " Where fas = ? "
                    + " and sar = ? "
                    + " and nar like ? ";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, fas_);
            ps.setInt(2, sar_);
            ps.setString(3, nar_);
            ps.execute();

            sql = "Delete From tmp_plan_fas_color Where fas = ? and cx = ? and sar = ? and nar like ? ";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, fasNum);
            ps.setInt(2, convText);
            ps.setInt(3, sarText);
            ps.setString(4, narText);
            ps.execute();

            it = tModelColor.iterator();
            while (it.hasNext()) {
                Vector items = (Vector) it.next();

                sql = "Insert into tmp_plan_fas_color( fas, id_color, ncw, cx, sar, nar) values( ?, ?, ?, ?, ?, ?)";

                ps = conn.prepareStatement(sql);
                ps.setInt(1, fasNum);
                ps.setInt(2, Integer.valueOf(items.get(1).toString()));
                ps.setString(3, items.get(2).toString());
                ps.setInt(4, convText);
                ps.setInt(5, sarText);
                ps.setString(6, narText);
                ps.execute();
            }

            commit();

            rezalt = true;
        } catch (Exception e) {
            rollBack();
            rezalt = false;
            System.err.println("Ошибка updateItemTempProjectTable() " + e);
            log.error("Ошибка updateItemTempProjectTable()", e);
            throw new Exception("Ошибка updateItemTempProjectTable() " + e.getMessage(), e);
        } finally {
            setAutoCommit(true);
        }
        return rezalt;
    }

    /**
     * Обновляет БД данными из временной таблицы tmp_plan_item
     * @param idPlan
     * @param namePlan
     * @param idDept
     * @param sDate
     * @param status
     * @param note
     * @param idEmploye
     * @return
     * @throws Exception
     */
    public boolean updateTempPlanTable(int idPlan, String namePlan, int idDept, long sDate, int status, String note, int idEmploye) throws Exception {
        boolean rezalt = false;
        String sql = "";
        try {
            setAutoCommit(false);

            sql = "Update plan Set id_dept = ?, plan_name = ?, plan_date = ?, "
                    + "     id_empl_ins = ?, note = ?, status = ?, date_ins = now() "
                    + " Where plan.id = ?";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, idDept);
            ps.setString(2, namePlan);
            ps.setDate(3, new java.sql.Date(sDate));
            ps.setInt(4, idEmploye);
            ps.setString(5, note);
            ps.setInt(6, status);
            ps.setInt(7, idPlan);
            ps.execute();

            sql = "Delete From plan_item Where plan_item.id_plan = ?";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, idPlan);
            ps.execute();

            sql = "Select cx, sar, fas, rst, rzm, kol_month, kol_day, id_spec, id_empl_ins, date_ins, "
                    + "     dekada1, dekada2, dekada3, note_item, fas_vid, fas_pname, kol_x  "
                    + " From tmp_plan_item ";
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {

                sql = "Insert into plan_item(id_plan, cx, sar, fas, rst, rzm, kol_month, kol_day, id_spec, id_empl_ins, date_ins,"
                        + "                          dekada1, dekada2, dekada3, note_item, fas_vid, fas_pname, kol_x) "
                        + "values( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

                ps = conn.prepareStatement(sql);
                ps.setInt(1, idPlan);
                ps.setInt(2, rs.getInt("cx"));
                ps.setInt(3, rs.getInt("sar"));
                ps.setInt(4, rs.getInt("fas"));
                ps.setInt(5, rs.getInt("rst"));
                ps.setInt(6, rs.getInt("rzm"));
                ps.setDouble(7, rs.getDouble("kol_month"));
                ps.setDouble(8, rs.getDouble("kol_day"));
                ps.setInt(9, rs.getInt("id_spec"));
                ps.setInt(10, rs.getInt("id_empl_ins"));
                ps.setDate(11, rs.getDate("date_ins"));
                ps.setDouble(12, rs.getDouble("dekada1"));
                ps.setDouble(13, rs.getDouble("dekada2"));
                ps.setDouble(14, rs.getDouble("dekada3"));
                ps.setString(15, rs.getString("note_item"));
                ps.setInt(16, rs.getInt("fas_vid"));
                ps.setString(17, rs.getString("fas_pname"));
                ps.setInt(18, rs.getInt("kol_x"));
                ps.execute();
            }

            commit();

            rezalt = true;
        } catch (Exception e) {
            rollBack();
            rezalt = false;
            System.err.println("Ошибка updateTempPlanTable() " + e);
            log.error("Ошибка updateTempPlanTable()", e);
            throw new Exception("Ошибка updateTempPlanTable() " + e.getMessage(), e);
        } finally {
            setAutoCommit(true);
        }
        return rezalt;
    }

    public int updateTempProjectTable(int idPlan, String namePlan, int idDept, long sDate, int status, String note, int idEmploye, int workDays) throws Exception {
        String sql = "";

        try {
            setAutoCommit(false);

            sql = "Update plan Set id_dept = ?, plan_name = ?, plan_date = ?, "
                    + "     id_empl_ins = ?, note = ?, status = ?, date_ins = now(), workdays = ?  "
                    + " Where plan.id = ?";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, idDept);
            ps.setString(2, namePlan);
            ps.setDate(3, new java.sql.Date(sDate));
            ps.setInt(4, idEmploye);
            ps.setString(5, note);
            ps.setInt(6, status);
            ps.setInt(7, workDays);
            ps.setInt(8, idPlan);
            ps.execute();

            sql = "Delete From plan_item Where plan_item.id_plan = ?";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, idPlan);
            ps.execute();

            sql = "Select cx, sar, fas, rst, rzm, kol_month, kol_day, id_spec, id_empl_ins, date_ins,"
                    + " dekada1, dekada2, dekada3, note_item, fas_vid, fas_pname, kol_x, nar "
                    + " From tmp_plan_item ";
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {

                sql = "Insert into plan_item(id_plan, cx, sar, fas, rst, rzm, kol_month, kol_day, id_spec, id_empl_ins, date_ins, "
                        + "                  dekada1, dekada2, dekada3, note_item, fas_vid, fas_pname, kol_x, nar) "
                        + "values( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

                ps = conn.prepareStatement(sql);
                ps.setInt(1, idPlan);
                ps.setInt(2, rs.getInt("cx"));
                ps.setInt(3, rs.getInt("sar"));
                ps.setInt(4, rs.getInt("fas"));
                ps.setInt(5, rs.getInt("rst"));
                ps.setInt(6, rs.getInt("rzm"));
                ps.setDouble(7, rs.getDouble("kol_month"));
                ps.setDouble(8, rs.getDouble("kol_day"));
                ps.setInt(9, rs.getInt("id_spec"));
                ps.setInt(10, rs.getInt("id_empl_ins"));
                ps.setDate(11, rs.getDate("date_ins"));
                ps.setDouble(12, rs.getDouble("dekada1"));
                ps.setDouble(13, rs.getDouble("dekada2"));
                ps.setDouble(14, rs.getDouble("dekada3"));
                ps.setString(15, rs.getString("note_item"));
                ps.setInt(16, rs.getInt("fas_vid"));
                ps.setString(17, rs.getString("fas_pname"));
                ps.setInt(18, rs.getInt("kol_x"));
                ps.setString(19, rs.getString("nar"));
                ps.execute();
            }

            sql = "Delete From plan_fas_new Where plan_fas_new.id_plan = ?";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, idPlan);
            ps.execute();

            sql = "Select distinct fas From tmp_plan_fas_new ";
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                sql = "Insert into plan_fas_new(id_plan, fas) values(?, ?)";

                ps = conn.prepareStatement(sql);
                ps.setInt(1, idPlan);
                ps.setInt(2, rs.getInt("fas"));
                ps.execute();
            }

            sql = "Delete From plan_fas_dekor Where plan_fas_dekor.id_plan = ?";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, idPlan);
            ps.execute();

            sql = "Select distinct fas, vid, cx, sar, nar From tmp_plan_fas_dekor ";
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                sql = "Insert into plan_fas_dekor( id_plan, fas, vid, cx, sar, nar) values(?, ?, ?, ?, ?, ?)";

                ps = conn.prepareStatement(sql);
                ps.setInt(1, idPlan);
                ps.setInt(2, rs.getInt("fas"));
                ps.setInt(3, rs.getInt("vid"));
                ps.setInt(4, rs.getInt("cx"));
                ps.setInt(5, rs.getInt("sar"));
                ps.setString(6, rs.getString("nar"));
                ps.execute();
            }

            sql = "Delete From plan_fas_sostav Where plan_fas_sostav.id_plan = ?";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, idPlan);
            ps.execute();

            sql = "Select distinct fas, id_polotno, nar_polotno, sostav, vid, rashod, cx, sar, nar "
                    + " From tmp_plan_fas_sostav ";
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                sql = "Insert into plan_fas_sostav( id_plan, fas, id_polotno, nar_polotno, sostav, vid, rashod, cx, sar, nar)"
                        + " values( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

                ps = conn.prepareStatement(sql);
                ps.setInt(1, idPlan);
                ps.setInt(2, rs.getInt("fas"));
                ps.setInt(3, rs.getInt("id_polotno"));
                ps.setString(4, rs.getString("nar_polotno"));
                ps.setString(5, rs.getString("sostav"));
                ps.setInt(6, rs.getInt("vid"));
                ps.setDouble(7, rs.getDouble("rashod"));
                ps.setInt(8, rs.getInt("cx"));
                ps.setInt(9, rs.getInt("sar"));
                ps.setString(10, rs.getString("nar"));
                ps.execute();
            }

            sql = "Delete From plan_fas_color Where plan_fas_color.id_plan = ?";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, idPlan);
            ps.execute();

            sql = "Select distinct fas, id_color, ncw, cx, sar, nar From tmp_plan_fas_color ";
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                sql = "Insert into plan_fas_color(id_plan, fas, id_color, ncw, cx, sar, nar) "
                        + "        values( ?, ?, ?, ?, ?, ?, ?)";

                ps = conn.prepareStatement(sql);
                ps.setInt(1, idPlan);
                ps.setInt(2, rs.getInt("fas"));
                ps.setInt(3, rs.getInt("id_color"));
                ps.setString(4, rs.getString("ncw"));
                ps.setInt(5, rs.getInt("cx"));
                ps.setInt(6, rs.getInt("sar"));
                ps.setString(7, rs.getString("nar"));
                ps.execute();
            }

            commit();

        } catch (Exception e) {
            rollBack();
            System.err.println("Ошибка updateTempProjectTable() " + e);
            log.error("Ошибка updateTempProjectTable()", e);
            throw new Exception("Ошибка updateTempProjectTable() " + e.getMessage(), e);
        } finally {
            setAutoCommit(true);
        }
        return idPlan;
    }

    /**
     * Обновляет БД данными из временной таблицы plan_fas_new
     * @param idPlan
     * @return
     * @throws Exception
     */
    public boolean updateNewModelsTempPlanTable(int idPlan) throws Exception {
        boolean rezalt = false;
        String sql = "";
        try {
            setAutoCommit(false);

            sql = "Delete From plan_fas_new Where plan_fas_new.id_plan = ?";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, idPlan);
            ps.execute();

            sql = "Select fas From tmp_plan_fas_new ";
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {

                sql = "Insert into plan_fas_new(id_plan, fas) values( ?, ?)";

                ps = conn.prepareStatement(sql);
                ps.setInt(1, idPlan);
                ps.setInt(2, rs.getInt("fas"));
                ps.execute();
            }

            commit();

            rezalt = true;
        } catch (Exception e) {
            rollBack();
            rezalt = false;
            System.err.println("Ошибка updateNewModelsTempPlanTable() " + e);
            log.error("Ошибка updateNewModelsTempPlanTable()", e);
            throw new Exception("Ошибка updateNewModelsTempPlanTable() " + e.getMessage(), e);
        } finally {
            setAutoCommit(true);
        }
        return rezalt;
    }

    /**
     * Возвращает все новые модели из временной таблицы tmp_plan_fas_new
     * @return
     * @throws Exception
     */
    public Vector getAllNewModelsPlanTemp() throws Exception {
        Vector elements = new Vector();
        String sql = "Select distinct fas From tmp_plan_fas_new Order by fas";

        try {
            ps = conn.prepareStatement(sql);

            rs = ps.executeQuery();
            while (rs.next()) {
                elements.add(rs.getInt("fas"));
            }

        } catch (Exception e) {
            elements = new Vector();
            System.err.println("Ошибка getAllNewModelsPlanTemp() " + e);
            log.error("Ошибка getAllNewModelsPlanTemp()", e);
            throw new Exception("Ошибка getAllNewModelsPlanTemp() " + e.getMessage(), e);
        }
        return elements;
    }

    /**
     * Уданяет план из БД
     * @param idPlan
     * @throws Exception
     */
    public void deletePlan(int idPlan) throws Exception {
        String sql = "";

        try {
            setAutoCommit(false);

            sql = "Delete From plan_fas_sostav Where plan_fas_sostav.id_plan = ?";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, idPlan);
            ps.execute();

            sql = "Delete From plan_fas_new Where plan_fas_new.id_plan = ?";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, idPlan);
            ps.execute();

            sql = "Delete From plan_fas_dekor Where plan_fas_dekor.id_plan = ?";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, idPlan);
            ps.execute();

            sql = "Delete From plan_fas_color Where plan_fas_color.id_plan = ?";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, idPlan);
            ps.execute();

            sql = "Delete From plan_item Where plan_item.id_plan = ?";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, idPlan);
            ps.execute();

            sql = "Delete From plan Where id = ?";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, idPlan);
            ps.execute();

            commit();
        } catch (Exception e) {
            rollBack();
            System.err.println("Ошибка deletePlan() " + e);
            log.error("Ошибка deletePlan()", e);
            throw new Exception("Ошибка deletePlan() " + e.getMessage(), e);
        } finally {
            setAutoCommit(true);
        }
    }

    public void deleteItemProjectPlanTemp(Vector delete) throws Exception {
        String sql = "Delete From tmp_plan_item Where id = ?";

        try {
            setAutoCommit(false);

            for (Object obj : delete) {
                ps = conn.prepareStatement(sql);
                ps.setInt(1, Integer.valueOf(obj.toString()));
                ps.execute();
            }

            commit();
        } catch (Exception e) {
            rollBack();
            System.err.println("Ошибка deleteItemProjectPlanTemp() " + e);
            log.error("Ошибка deleteItemProjectPlanTemp()", e);
            throw new Exception("Ошибка deleteItemProjectPlanTemp() " + e.getMessage(), e);
        } finally {
            setAutoCommit(true);
        }
    }

    public void deleteItemsProjectPlanTemp(Vector delete) throws Exception {
        String sql = " ";

        try {
            setAutoCommit(false);

            for (Object obj : delete) {

                Vector row = (Vector) obj;

                sql = "Delete From tmp_plan_fas_sostav Where fas = ? and cx = ? and sar = ? and nar like ?";
                ps = conn.prepareStatement(sql);
                ps.setInt(1, Integer.valueOf(row.get(3).toString()));
                ps.setInt(2, Integer.valueOf(row.get(13).toString()));
                ps.setInt(3, Integer.valueOf(row.get(15).toString()));
                ps.setString(4, row.get(16).toString());
                ps.execute();

                sql = "Delete From tmp_plan_fas_new Where fas = ? ";
                ps = conn.prepareStatement(sql);
                ps.setInt(1, Integer.valueOf(row.get(3).toString()));
                ps.execute();

                sql = "Delete From tmp_plan_fas_dekor Where fas = ? and cx = ? and sar = ? and nar like ?";
                ps = conn.prepareStatement(sql);
                ps.setInt(1, Integer.valueOf(row.get(3).toString()));
                ps.setInt(2, Integer.valueOf(row.get(13).toString()));
                ps.setInt(3, Integer.valueOf(row.get(15).toString()));
                ps.setString(4, row.get(16).toString());
                ps.execute();

                sql = "Delete From tmp_plan_fas_color Where fas = ? and cx = ? and sar = ? and nar like ?";
                ps = conn.prepareStatement(sql);
                ps.setInt(1, Integer.valueOf(row.get(3).toString()));
                ps.setInt(2, Integer.valueOf(row.get(13).toString()));
                ps.setInt(3, Integer.valueOf(row.get(15).toString()));
                ps.setString(4, row.get(16).toString());
                ps.execute();

                sql = "Delete From tmp_plan_item Where fas = ? and cx = ? and sar = ? and nar like ?";
                ps = conn.prepareStatement(sql);
                ps.setInt(1, Integer.valueOf(row.get(3).toString()));
                ps.setInt(2, Integer.valueOf(row.get(13).toString()));
                ps.setInt(3, Integer.valueOf(row.get(15).toString()));
                ps.setString(4, row.get(16).toString());
                ps.execute();
            }

            commit();
        } catch (Exception e) {
            rollBack();
            System.err.println("Ошибка deleteItemsProjectPlanTemp() " + e);
            log.error("Ошибка deleteItemsProjectPlanTemp()", e);
            throw new Exception("Ошибка deleteItemsProjectPlanTemp() " + e.getMessage(), e);
        } finally {
            setAutoCommit(true);
        }
    }

    /**
     * Возвращает сравнение двух планов
     * @param compare
     * @return
     * @throws Exception
     */
    public Vector compareSelectedPlanProduction(int idPlan1, int idPlan2) throws Exception {
        Vector elements = new Vector();
        String sql = "Select distinct fas, idPlan1, fasC1, kolMC1, idSpecC1, modelSpecC1, nameSpecC1, idPlan2, fasC2, kolMC2, idSpecC2, modelSpecC2, nameSpecC2 "
                + " From(Select fas, idPlan1, fasC1, kolMC1, idSpecC1, modelSpecC1, nameSpecC1, idPlan2, fasC2, kolMC2, idSpecC2, modelSpecC2, nameSpecC2 "
                + "	 From(Select fas, idPlan1, fasC1, kolMC1, idSpecC1, modelSpecC1, nameSpecC1 "
                + "		From(Select distinct plan_item.fas as fas, spec.id as idSpec, spec.spec as nameSpec "
                + "			From plan, plan_item, spec "
                + "			Where plan.id = plan_item.id_plan and "
                + "				plan_item.id_spec = spec.id and "
                + "				(plan.id = ? or "
                + "				plan.id = ?) "
                + "			Order by fas, nameSpec) as t1 "
                + "		left join (Select plan.id as idPlan1, fas as fasC1, sum(round(kol_month,3)) as kolMC1, spec.id as idSpecC1, "
                + "				  spec.id_model as modelSpecC1, spec.spec as nameSpecC1 "
                + "			    From plan, plan_item, spec "
                + "			    Where plan.id = plan_item.id_plan and "
                + "				  plan.id = ? and "
                + "				  plan_item.id_spec = spec.id "
                + "			    Group by idPlan1, fasC1, idSpecC1, modelSpecC1, nameSpecC1) as t2 "
                + "		on fas = fasC1) as t3 "
                + "	 left join (Select plan.id as idPlan2, fas as fasC2, sum(round(kol_month,3)) as kolMC2, spec.id as idSpecC2, "
                + "				  spec.id_model as modelSpecC2, spec.spec as nameSpecC2 "
                + "		   From plan, plan_item, spec "
                + "		   Where plan.id = plan_item.id_plan and "
                + "			  plan.id = ? and "
                + "			  plan_item.id_spec = spec.id "
                + "		   Group by idPlan2, fasC2, idSpecC2, modelSpecC2, nameSpecC2) as t4 "
                + "	 on fas = fasC2) as t5 "
                + " Where nameSpecC1 is NULL or nameSpecC2 is NULL or kolMC1 <> kolMC2 or idSpecC1 <> idSpecC2 "
                + " Order by fas ";

        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, idPlan1);
            ps.setInt(2, idPlan2);
            ps.setInt(3, idPlan1);
            ps.setInt(4, idPlan2);

            rs = ps.executeQuery();
            while (rs.next()) {
                Vector tmp = new Vector();
                tmp.add(false);
                tmp.add(rs.getInt("fas"));
                tmp.add(rs.getInt("idPlan1"));
                tmp.add(rs.getString("nameSpecC1"));
                tmp.add(rs.getDouble("kolMC1"));
                tmp.add(rs.getInt("idPlan2"));
                tmp.add(rs.getString("nameSpecC2"));
                tmp.add(rs.getDouble("kolMC2"));
                elements.add(tmp);
            }

        } catch (Exception e) {
            elements = new Vector();
            System.err.println("Ошибка compareSelectedPlanProduction() " + e);
            log.error("Ошибка compareSelectedPlanProduction()", e);
            throw new Exception("Ошибка compareSelectedPlanProduction() " + e.getMessage(), e);
        }
        return elements;
    }

    public Vector getDataReportProjectPlanFasNew(int idProj) throws Exception {
        Vector elements = new Vector();
        String sql = "Select min(rst) as minrst, max(rst) as maxrst, min(rzm) as minrzm,  max(rzm) as maxrzm, t2.fas_vid as fas_vid, t2.fas_pname as fas_pname, "
                + "	t2.fas as fas, t2.kolM as kolM, t2.kol_x as kol_x, t2.note_item as note_item,  t2.sar as sar, t2.nar as nar, polotno, color, dekor, new_  "
                + " From(Select plan_item.id_plan as idPlan , fas_vid, fas_pname, fas, sum(round(kol_month,3)) as kolM, kol_x, note_item, sar, nar, "
                + "			(SELECT ARRAY_TO_STRING(ARRAY(SELECT nar_polotno FROM plan_fas_sostav Where plan_item.fas = plan_fas_sostav.fas and "
                + "                                                                                                 plan_fas_sostav.id_plan = plan_item.id_plan and "
                + "                                                                                                 plan_fas_sostav.sar = plan_item.sar and "
                + "                                                                                                 plan_fas_sostav.nar = plan_item.nar  "
                + "                                                                                             Order by plan_fas_sostav.vid, nar_polotno),', ')) as polotno,"
                + "			(SELECT ARRAY_TO_STRING(ARRAY(SELECT ncw FROM plan_fas_color Where plan_item.fas = plan_fas_color.fas and "
                + "                                                                                         plan_fas_color.id_plan = plan_item.id_plan and "
                + "                                                                                         plan_fas_color.sar = plan_item.sar and "
                + "                                                                                         plan_fas_color.nar = plan_item.nar  "
                + "                                                                                   Order by ncw),', ')) as color,"
                + "			(SELECT distinct id FROM plan_fas_new Where plan_item.fas = plan_fas_new.fas and plan_fas_new.id_plan = plan_item.id_plan) as new_,"
                + "			(SELECT ARRAY_TO_STRING(ARRAY(SELECT vid FROM plan_fas_dekor Where plan_item.fas = plan_fas_dekor.fas and "
                + "                                                                                         plan_fas_dekor.id_plan = plan_item.id_plan and "
                + "                                                                                         plan_fas_dekor.sar = plan_item.sar and "
                + "                                                                                         plan_fas_dekor.nar = plan_item.nar "
                + "                                                                                   Order by vid),', ')) as dekor   "
                + "		From plan, plan_item "
                + "		Where plan.id = plan_item.id_plan and "
                + "			plan.id = ? "
                + "		Group by idPlan, fas_vid, fas_pname, fas, kol_x, note_item, sar, nar ) as t2, plan_item, plan_fas_new "
                + " Where t2.idPlan = plan_item.id_plan and "
                + "     t2.fas_vid = plan_item.fas_vid and "
                + "	t2.fas_pname = plan_item.fas_pname and "
                + "	t2.fas = plan_item.fas and "
                + "	t2.sar = plan_item.sar and "
                + "	t2.nar = plan_item.nar and "
                + "	t2.kol_x = plan_item.kol_x and "
                + "	t2.note_item = plan_item.note_item and "
                + "     t2.idPlan = plan_fas_new.id_plan and "
                + "	t2.fas = plan_fas_new.fas "
                + " Group by t2.fas_vid, t2.fas_pname, t2.fas, t2.kolM, t2.kol_x, t2.note_item, t2.sar, t2.nar, polotno, color, dekor, new_ "
                + " Order by fas_vid, fas_pname, fas, sar, nar";
        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, idProj);
            rs = ps.executeQuery();
            while (rs.next()) {
                Vector tmp = new Vector();

                tmp.add(rs.getInt("fas_vid"));

                if (rs.getObject("new_") != null) tmp.add("V");
                else tmp.add("");

                tmp.add(rs.getString("fas_pname").toUpperCase());
                tmp.add(rs.getInt("fas"));
                tmp.add(rs.getString("polotno"));
                tmp.add(Double.valueOf(UtilFunctions.formatNorm(rs.getDouble("kolM"), 0)));
                tmp.add(rs.getInt("kol_x"));
                tmp.add(rs.getString("minrst").equals(rs.getString("maxrst")) ? rs.getString("minrst") : rs.getString("minrst") + "--" + rs.getString("maxrst"));
                tmp.add(rs.getString("minrzm").equals(rs.getString("maxrzm")) ? rs.getString("minrzm") : rs.getString("minrzm") + "--" + rs.getString("maxrzm"));
                tmp.add(rs.getString("color"));
                tmp.add(rs.getString("dekor"));
                tmp.add(rs.getString("note_item"));

                elements.add(tmp);
            }

        } catch (Exception e) {
            elements = new Vector();
            System.err.println("Ошибка getDataReportProjectPlanFasNew() " + e);
            log.error("Ошибка getDataReportProjectPlanFasNew()", e);
            throw new Exception("Ошибка getDataReportProjectPlanFasNew() " + e.getMessage(), e);
        }
        return elements;
    }

    public Vector getDataReportProjectPlanFasNewTemp() throws Exception {
        Vector elements = new Vector();
        String sql = "Select min(rst) as minrst, max(rst) as maxrst, min(rzm) as minrzm,  max(rzm) as maxrzm, t2.fas_vid as fas_vid, t2.fas_pname as fas_pname, "
                + "	t2.fas as fas, t2.kolM as kolM, t2.kol_x as kol_x, t2.note_item as note_item , t2.sar as sar, t2.nar as nar, polotno, color, dekor, new_  "
                + " From(Select fas_vid, fas_pname, fas, sum(round(kol_month,3)) as kolM, kol_x, note_item, sar, nar,"
                + "			(SELECT ARRAY_TO_STRING(ARRAY(SELECT nar_polotno FROM tmp_plan_fas_sostav Where tmp_plan_item.fas = tmp_plan_fas_sostav.fas and "
                + "                                                                                                     tmp_plan_item.sar = tmp_plan_fas_sostav.sar and "
                + "                                                                                                     tmp_plan_item.nar = tmp_plan_fas_sostav.nar "
                + "                                                                                               Order by tmp_plan_fas_sostav.vid, nar_polotno),', ')) as polotno,"
                + "			(SELECT ARRAY_TO_STRING(ARRAY(SELECT ncw FROM tmp_plan_fas_color Where tmp_plan_item.fas = tmp_plan_fas_color.fas and "
                + "                                                                                             tmp_plan_item.sar = tmp_plan_fas_color.sar and "
                + "                                                                                             tmp_plan_item.nar = tmp_plan_fas_color.nar  "
                + "                                                                                      Order by ncw),', ')) as color,"
                + "			(SELECT distinct id FROM tmp_plan_fas_new Where tmp_plan_item.fas = tmp_plan_fas_new.fas) as new_,"
                + "			(SELECT ARRAY_TO_STRING(ARRAY(SELECT vid FROM tmp_plan_fas_dekor Where tmp_plan_item.fas = tmp_plan_fas_dekor.fas and "
                + "                                                                                             tmp_plan_item.sar = tmp_plan_fas_dekor.sar and "
                + "                                                                                             tmp_plan_item.nar = tmp_plan_fas_dekor.nar  "
                + "                                                                                      Order by vid),', ')) as dekor   "
                + "		From tmp_plan_item "
                + "		Group by fas_vid, fas_pname, fas, kol_x, note_item, sar, nar ) as t2, tmp_plan_item, tmp_plan_fas_new "
                + " Where t2.fas_vid = tmp_plan_item.fas_vid and "
                + "	t2.fas_pname = tmp_plan_item.fas_pname and "
                + "	t2.fas = tmp_plan_item.fas and "
                + "	t2.sar = tmp_plan_item.sar and "
                + "	t2.nar = tmp_plan_item.nar and "
                + "	t2.kol_x = tmp_plan_item.kol_x and "
                + "	t2.note_item = tmp_plan_item.note_item and "
                + "	t2.fas = tmp_plan_fas_new.fas "
                + " Group by t2.fas_vid, t2.fas_pname, t2.fas, t2.kolM, t2.kol_x, t2.note_item, t2.sar, t2.nar, polotno, color, dekor, new_ "
                + " Order by fas_vid, fas_pname, fas, sar, nar";
        try {
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                Vector tmp = new Vector();

                tmp.add(rs.getInt("fas_vid"));

                if (rs.getObject("new_") != null) tmp.add("V");
                else tmp.add("");

                tmp.add(rs.getString("fas_pname").toUpperCase());
                tmp.add(rs.getInt("fas"));
                tmp.add(rs.getString("polotno"));
                tmp.add(Double.valueOf(UtilFunctions.formatNorm(rs.getDouble("kolM"), 0)));
                tmp.add(rs.getInt("kol_x"));
                tmp.add(rs.getString("minrst").equals(rs.getString("maxrst")) ? rs.getString("minrst") : rs.getString("minrst") + "--" + rs.getString("maxrst"));
                tmp.add(rs.getString("minrzm").equals(rs.getString("maxrzm")) ? rs.getString("minrzm") : rs.getString("minrzm") + "--" + rs.getString("maxrzm"));
                tmp.add(rs.getString("color"));
                tmp.add(rs.getString("dekor"));
                tmp.add(rs.getString("note_item"));

                elements.add(tmp);
            }

        } catch (Exception e) {
            elements = new Vector();
            System.err.println("Ошибка getDataReportProjectPlanFasNewTemp() " + e);
            log.error("Ошибка getDataReportProjectPlanFasNewTemp()", e);
            throw new Exception("Ошибка getDataReportProjectPlanFasNewTemp() " + e.getMessage(), e);
        }
        return elements;
    }

    public Vector getDataReportProjectPlanDekorPrint(int idProj) throws Exception {
        Vector elements = new Vector();
        String sql = "Select min(rst) as minrst, max(rst) as maxrst, min(rzm) as minrzm,  max(rzm) as maxrzm, t2.fas_vid as fas_vid, t2.fas_pname as fas_pname, "
                + "	t2.fas as fas, t2.kolM as kolM, t2.kol_x as kol_x, t2.note_item as note_item,  t2.sar as sar, t2.nar as nar, polotno, color, dekor, new_  "
                + " From(Select plan_item.id_plan as idPlan , fas_vid, fas_pname, fas, sum(round(kol_month,3)) as kolM, kol_x, note_item, sar, nar, "
                + "			(SELECT ARRAY_TO_STRING(ARRAY(SELECT nar_polotno FROM plan_fas_sostav Where plan_item.fas = plan_fas_sostav.fas and "
                + "                                                                                                 plan_fas_sostav.id_plan = plan_item.id_plan and "
                + "                                                                                                 plan_fas_sostav.sar = plan_item.sar and "
                + "                                                                                                 plan_fas_sostav.nar = plan_item.nar  "
                + "                                                                                             Order by plan_fas_sostav.vid, nar_polotno),', ')) as polotno,"
                + "			(SELECT ARRAY_TO_STRING(ARRAY(SELECT ncw FROM plan_fas_color Where plan_item.fas = plan_fas_color.fas and "
                + "                                                                                         plan_fas_color.id_plan = plan_item.id_plan and "
                + "                                                                                         plan_fas_color.sar = plan_item.sar and "
                + "                                                                                         plan_fas_color.nar = plan_item.nar  "
                + "                                                                                   Order by ncw),', ')) as color,"
                + "			(SELECT distinct id FROM plan_fas_new Where plan_item.fas = plan_fas_new.fas and plan_fas_new.id_plan = plan_item.id_plan) as new_,"
                + "			(SELECT ARRAY_TO_STRING(ARRAY(SELECT vid FROM plan_fas_dekor Where plan_item.fas = plan_fas_dekor.fas and "
                + "                                                                                         plan_fas_dekor.id_plan = plan_item.id_plan and "
                + "                                                                                         plan_fas_dekor.sar = plan_item.sar and "
                + "                                                                                         plan_fas_dekor.nar = plan_item.nar "
                + "                                                                                   Order by vid),', ')) as dekor   "
                + "		From plan, plan_item "
                + "		Where plan.id = plan_item.id_plan and "
                + "			plan.id = ? "
                + "		Group by idPlan, fas_vid, fas_pname, fas, kol_x, note_item, sar, nar ) as t2, plan_item "
                + " Where t2.idPlan = plan_item.id_plan and "
                + "     t2.fas_vid = plan_item.fas_vid and "
                + "	t2.fas_pname = plan_item.fas_pname and "
                + "	t2.fas = plan_item.fas and "
                + "	t2.sar = plan_item.sar and "
                + "	t2.nar = plan_item.nar and "
                + "	t2.kol_x = plan_item.kol_x and "
                + "	t2.note_item = plan_item.note_item and "
                + "     dekor like '%1%' "
                + " Group by t2.fas_vid, t2.fas_pname, t2.fas, t2.kolM, t2.kol_x, t2.note_item, t2.sar, t2.nar, polotno, color, dekor, new_ "
                + " Order by fas_vid, fas_pname, fas, sar, nar";
        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, idProj);
            rs = ps.executeQuery();
            while (rs.next()) {
                Vector tmp = new Vector();

                tmp.add(rs.getInt("fas_vid"));

                if (rs.getObject("new_") != null) tmp.add("V");
                else tmp.add("");

                tmp.add(rs.getString("fas_pname").toUpperCase());
                tmp.add(rs.getInt("fas"));
                tmp.add(rs.getString("polotno"));
                tmp.add(Double.valueOf(UtilFunctions.formatNorm(rs.getDouble("kolM"), 0)));
                tmp.add(rs.getInt("kol_x"));
                tmp.add(rs.getString("minrst").equals(rs.getString("maxrst")) ? rs.getString("minrst") : rs.getString("minrst") + "--" + rs.getString("maxrst"));
                tmp.add(rs.getString("minrzm").equals(rs.getString("maxrzm")) ? rs.getString("minrzm") : rs.getString("minrzm") + "--" + rs.getString("maxrzm"));
                tmp.add(rs.getString("color"));
                tmp.add(rs.getString("dekor"));
                tmp.add(rs.getString("note_item"));

                elements.add(tmp);
            }

        } catch (Exception e) {
            elements = new Vector();
            System.err.println("Ошибка getDataReportProjectPlanDekorPrint() " + e);
            log.error("Ошибка getDataReportProjectPlanDekorPrint()", e);
            throw new Exception("Ошибка getDataReportProjectPlanDekorPrint() " + e.getMessage(), e);
        }
        return elements;
    }

    public Vector getDataReportProjectPlanDekorPrintTemp() throws Exception {
        Vector elements = new Vector();
        String sql = "Select min(rst) as minrst, max(rst) as maxrst, min(rzm) as minrzm,  max(rzm) as maxrzm, t2.fas_vid as fas_vid, t2.fas_pname as fas_pname, "
                + "	t2.fas as fas, t2.kolM as kolM, t2.kol_x as kol_x, t2.note_item as note_item , t2.sar as sar, t2.nar as nar, polotno, color, dekor, new_  "
                + " From(Select fas_vid, fas_pname, fas, sum(round(kol_month,3)) as kolM, kol_x, note_item, sar, nar,"
                + "			(SELECT ARRAY_TO_STRING(ARRAY(SELECT nar_polotno FROM tmp_plan_fas_sostav Where tmp_plan_item.fas = tmp_plan_fas_sostav.fas and "
                + "                                                                                                     tmp_plan_item.sar = tmp_plan_fas_sostav.sar and "
                + "                                                                                                     tmp_plan_item.nar = tmp_plan_fas_sostav.nar "
                + "                                                                                               Order by tmp_plan_fas_sostav.vid, nar_polotno),', ')) as polotno,"
                + "			(SELECT ARRAY_TO_STRING(ARRAY(SELECT ncw FROM tmp_plan_fas_color Where tmp_plan_item.fas = tmp_plan_fas_color.fas and "
                + "                                                                                             tmp_plan_item.sar = tmp_plan_fas_color.sar and "
                + "                                                                                             tmp_plan_item.nar = tmp_plan_fas_color.nar  "
                + "                                                                                      Order by ncw),', ')) as color,"
                + "			(SELECT distinct id FROM tmp_plan_fas_new Where tmp_plan_item.fas = tmp_plan_fas_new.fas) as new_,"
                + "			(SELECT ARRAY_TO_STRING(ARRAY(SELECT vid FROM tmp_plan_fas_dekor Where tmp_plan_item.fas = tmp_plan_fas_dekor.fas and "
                + "                                                                                             tmp_plan_item.sar = tmp_plan_fas_dekor.sar and "
                + "                                                                                             tmp_plan_item.nar = tmp_plan_fas_dekor.nar  "
                + "                                                                                      Order by vid),', ')) as dekor   "
                + "		From tmp_plan_item "
                + "		Group by fas_vid, fas_pname, fas, kol_x, note_item, sar, nar ) as t2, tmp_plan_item "
                + " Where t2.fas_vid = tmp_plan_item.fas_vid and "
                + "	t2.fas_pname = tmp_plan_item.fas_pname and "
                + "	t2.fas = tmp_plan_item.fas and "
                + "	t2.sar = tmp_plan_item.sar and "
                + "	t2.nar = tmp_plan_item.nar and "
                + "	t2.kol_x = tmp_plan_item.kol_x and "
                + "	t2.note_item = tmp_plan_item.note_item and "
                + "     dekor like '%1%' "
                + " Group by t2.fas_vid, t2.fas_pname, t2.fas, t2.kolM, t2.kol_x, t2.note_item, t2.sar, t2.nar, polotno, color, dekor, new_ "
                + " Order by fas_vid, fas_pname, fas, sar, nar";
        try {
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                Vector tmp = new Vector();

                tmp.add(rs.getInt("fas_vid"));

                if (rs.getObject("new_") != null) tmp.add("V");
                else tmp.add("");

                tmp.add(rs.getString("fas_pname").toUpperCase());
                tmp.add(rs.getInt("fas"));
                tmp.add(rs.getString("polotno"));
                tmp.add(Double.valueOf(UtilFunctions.formatNorm(rs.getDouble("kolM"), 0)));
                tmp.add(rs.getInt("kol_x"));
                tmp.add(rs.getString("minrst").equals(rs.getString("maxrst")) ? rs.getString("minrst") : rs.getString("minrst") + "--" + rs.getString("maxrst"));
                tmp.add(rs.getString("minrzm").equals(rs.getString("maxrzm")) ? rs.getString("minrzm") : rs.getString("minrzm") + "--" + rs.getString("maxrzm"));
                tmp.add(rs.getString("color"));
                tmp.add(rs.getString("dekor"));
                tmp.add(rs.getString("note_item"));

                elements.add(tmp);
            }

        } catch (Exception e) {
            elements = new Vector();
            System.err.println("Ошибка getDataReportProjectPlanDekorPrintTemp() " + e);
            log.error("Ошибка getDataReportProjectPlanDekorPrintTemp()", e);
            throw new Exception("Ошибка getDataReportProjectPlanDekorPrintTemp() " + e.getMessage(), e);
        }
        return elements;
    }

    public Vector getDataReportProjectPlanStok(int idProj, int type) throws Exception {
        Vector elements = new Vector();
        String sql = "Select min(rst) as minrst, max(rst) as maxrst, min(rzm) as minrzm,  max(rzm) as maxrzm, t2.fas_vid as fas_vid, t2.fas_pname as fas_pname, "
                + "	t2.fas as fas, t2.kolM as kolM, t2.kol_x as kol_x, t2.note_item as note_item,  t2.sar as sar, t2.nar as nar, polotno, color, dekor, new_  "
                + " From(Select plan_item.id_plan as idPlan , fas_vid, fas_pname, fas, sum(round(kol_month,3)) as kolM, kol_x, note_item, sar, nar, "
                + "			(SELECT ARRAY_TO_STRING(ARRAY(SELECT nar_polotno FROM plan_fas_sostav Where plan_item.fas = plan_fas_sostav.fas and "
                + "                                                                                                 plan_fas_sostav.id_plan = plan_item.id_plan and "
                + "                                                                                                 plan_fas_sostav.sar = plan_item.sar and "
                + "                                                                                                 plan_fas_sostav.nar = plan_item.nar  "
                + "                                                                                             Order by plan_fas_sostav.vid, nar_polotno),', ')) as polotno,"
                + "			(SELECT ARRAY_TO_STRING(ARRAY(SELECT ncw FROM plan_fas_color Where plan_item.fas = plan_fas_color.fas and "
                + "                                                                                         plan_fas_color.id_plan = plan_item.id_plan and "
                + "                                                                                         plan_fas_color.sar = plan_item.sar and "
                + "                                                                                         plan_fas_color.nar = plan_item.nar  "
                + "                                                                                   Order by ncw),', ')) as color,"
                + "			(SELECT distinct id FROM plan_fas_new Where plan_item.fas = plan_fas_new.fas and plan_fas_new.id_plan = plan_item.id_plan) as new_,"
                + "			(SELECT ARRAY_TO_STRING(ARRAY(SELECT vid FROM plan_fas_dekor Where plan_item.fas = plan_fas_dekor.fas and "
                + "                                                                                         plan_fas_dekor.id_plan = plan_item.id_plan and "
                + "                                                                                         plan_fas_dekor.sar = plan_item.sar and "
                + "                                                                                         plan_fas_dekor.nar = plan_item.nar "
                + "                                                                                   Order by vid),', ')) as dekor   "
                + "		From plan, plan_item "
                + "		Where plan.id = plan_item.id_plan and "
                + "			plan.id = ? "
                + "		Group by idPlan, fas_vid, fas_pname, fas, kol_x, note_item, sar, nar ) as t2, plan_item "
                + " Where t2.idPlan = plan_item.id_plan and "
                + "     t2.fas_vid = plan_item.fas_vid and "
                + "	t2.fas_pname = plan_item.fas_pname and "
                + "	t2.fas = plan_item.fas and "
                + "	t2.sar = plan_item.sar and "
                + "	t2.nar = plan_item.nar and "
                + "	t2.kol_x = plan_item.kol_x and "
                + "	t2.note_item = plan_item.note_item "
                + " Group by t2.fas_vid, t2.fas_pname, t2.fas, t2.kolM, t2.kol_x, t2.note_item, t2.sar, t2.nar, polotno, color, dekor, new_ "
                + " Order by fas_vid, " + (type == 1 ? " fas_pname, fas, " : " fas,fas_pname, ") + " sar, nar";
        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, idProj);
            rs = ps.executeQuery();
            while (rs.next()) {
                Vector tmp = new Vector();

                tmp.add(rs.getInt("fas_vid"));

                if (rs.getObject("new_") != null) tmp.add("V");
                else tmp.add("");

                tmp.add(rs.getString("fas_pname").toUpperCase());
                tmp.add(rs.getInt("fas"));
                tmp.add(rs.getString("polotno"));
                tmp.add(Double.valueOf(UtilFunctions.formatNorm(rs.getDouble("kolM"), 0)));
                tmp.add(rs.getInt("kol_x"));
                tmp.add(rs.getString("minrst").equals(rs.getString("maxrst")) ? rs.getString("minrst") : rs.getString("minrst") + "--" + rs.getString("maxrst"));
                tmp.add(rs.getString("minrzm").equals(rs.getString("maxrzm")) ? rs.getString("minrzm") : rs.getString("minrzm") + "--" + rs.getString("maxrzm"));
                tmp.add(rs.getString("color"));
                tmp.add(rs.getString("dekor"));
                tmp.add(rs.getString("note_item"));

                elements.add(tmp);
            }

        } catch (Exception e) {
            elements = new Vector();
            System.err.println("Ошибка getDataReportProjectPlanStok() " + e);
            log.error("Ошибка getdataReportProjectPlanStok()", e);
            throw new Exception("Ошибка getdataReportProjectPlanStok() " + e.getMessage(), e);
        }
        return elements;
    }

    public Vector getDataReportProjectPlanStokTemp(int type) throws Exception {
        Vector elements = new Vector();
        String sql = "Select min(rst) as minrst, max(rst) as maxrst, min(rzm) as minrzm,  max(rzm) as maxrzm, t2.fas_vid as fas_vid, t2.fas_pname as fas_pname, "
                + "	t2.fas as fas, t2.kolM as kolM, t2.kol_x as kol_x, t2.note_item as note_item , t2.sar as sar, t2.nar as nar, polotno, color, dekor, new_  "
                + " From(Select fas_vid, fas_pname, fas, sum(round(kol_month,3)) as kolM, kol_x, note_item, sar, nar,"
                + "			(SELECT ARRAY_TO_STRING(ARRAY(SELECT nar_polotno FROM tmp_plan_fas_sostav Where tmp_plan_item.fas = tmp_plan_fas_sostav.fas and "
                + "                                                                                                     tmp_plan_item.sar = tmp_plan_fas_sostav.sar and "
                + "                                                                                                     tmp_plan_item.nar = tmp_plan_fas_sostav.nar "
                + "                                                                                               Order by tmp_plan_fas_sostav.vid, nar_polotno),', ')) as polotno,"
                + "			(SELECT ARRAY_TO_STRING(ARRAY(SELECT ncw FROM tmp_plan_fas_color Where tmp_plan_item.fas = tmp_plan_fas_color.fas and "
                + "                                                                                             tmp_plan_item.sar = tmp_plan_fas_color.sar and "
                + "                                                                                             tmp_plan_item.nar = tmp_plan_fas_color.nar  "
                + "                                                                                      Order by ncw),', ')) as color,"
                + "			(SELECT distinct id FROM tmp_plan_fas_new Where tmp_plan_item.fas = tmp_plan_fas_new.fas) as new_,"
                + "			(SELECT ARRAY_TO_STRING(ARRAY(SELECT vid FROM tmp_plan_fas_dekor Where tmp_plan_item.fas = tmp_plan_fas_dekor.fas and "
                + "                                                                                             tmp_plan_item.sar = tmp_plan_fas_dekor.sar and "
                + "                                                                                             tmp_plan_item.nar = tmp_plan_fas_dekor.nar  "
                + "                                                                                      Order by vid),', ')) as dekor   "
                + "		From tmp_plan_item "
                + "		Group by fas_vid, fas_pname, fas, kol_x, note_item, sar, nar ) as t2, tmp_plan_item "
                + " Where t2.fas_vid = tmp_plan_item.fas_vid and "
                + "	t2.fas_pname = tmp_plan_item.fas_pname and "
                + "	t2.fas = tmp_plan_item.fas and "
                + "	t2.sar = tmp_plan_item.sar and "
                + "	t2.nar = tmp_plan_item.nar and "
                + "	t2.kol_x = tmp_plan_item.kol_x and "
                + "	t2.note_item = tmp_plan_item.note_item "
                + " Group by t2.fas_vid, t2.fas_pname, t2.fas, t2.kolM, t2.kol_x, t2.note_item, t2.sar, t2.nar, polotno, color, dekor, new_ "
                + " Order by fas_vid, " + (type == 1 ? " fas_pname, fas, " : " fas,fas_pname, ") + " sar, nar";
        try {
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                Vector tmp = new Vector();

                tmp.add(rs.getInt("fas_vid"));

                if (rs.getObject("new_") != null) tmp.add("V");
                else tmp.add("");

                tmp.add(rs.getString("fas_pname").toUpperCase());
                tmp.add(rs.getInt("fas"));
                tmp.add(rs.getString("polotno"));
                tmp.add(Double.valueOf(UtilFunctions.formatNorm(rs.getDouble("kolM"), 0)));
                tmp.add(rs.getInt("kol_x"));
                tmp.add(rs.getString("minrst").equals(rs.getString("maxrst")) ? rs.getString("minrst") : rs.getString("minrst") + "--" + rs.getString("maxrst"));
                tmp.add(rs.getString("minrzm").equals(rs.getString("maxrzm")) ? rs.getString("minrzm") : rs.getString("minrzm") + "--" + rs.getString("maxrzm"));
                tmp.add(rs.getString("color"));
                tmp.add(rs.getString("dekor"));
                tmp.add(rs.getString("note_item"));

                elements.add(tmp);
            }

        } catch (Exception e) {
            elements = new Vector();
            System.err.println("Ошибка getDataReportProjectPlanStokTemp() " + e);
            log.error("Ошибка getdataReportProjectPlanStokTemp()", e);
            throw new Exception("Ошибка getdataReportProjectPlanStokTemp() " + e.getMessage(), e);
        }
        return elements;
    }

    public Vector getDataReportProjectPlanVnorm(int idProj) throws Exception {
        Vector elements = new Vector();
        String sql = "Select fas_vid, fas_pname, fas, kolM, kol_x, note_item, idSpec, modelSpec, nameSpec, sar, nar, sum(round(spec_item.vnorm::numeric," + UtilPlan.ROUNDING_NORM + ")) as vnorm,  polotno, new_ , dekor "
                + "	From (Select plan_item.id_plan as idPlan , fas_vid, fas_pname, fas, sum(round(kol_month,3)) as kolM, kol_x, note_item, spec.id as idSpec, "
                + "			spec.id_model as modelSpec, spec.spec as nameSpec, sar, nar, "
                + "			(SELECT ARRAY_TO_STRING(ARRAY(SELECT nar_polotno FROM plan_fas_sostav Where plan_item.fas = plan_fas_sostav.fas and "
                + "                                                                                                 plan_fas_sostav.id_plan = plan_item.id_plan and "
                + "                                                                                                 plan_fas_sostav.sar = plan_item.sar and "
                + "                                                                                                 plan_fas_sostav.nar = plan_item.nar  "
                + "                                                                                             Order by plan_fas_sostav.vid, nar_polotno),',')) as polotno,"
                + "			(SELECT distinct id FROM plan_fas_new Where plan_item.fas = plan_fas_new.fas and plan_fas_new.id_plan = plan_item.id_plan) as new_,"
                + "			(SELECT ARRAY_TO_STRING(ARRAY(SELECT vid FROM plan_fas_dekor Where plan_item.fas = plan_fas_dekor.fas and "
                + "                                                                                         plan_fas_dekor.id_plan = plan_item.id_plan and "
                + "                                                                                         plan_fas_dekor.sar = plan_item.sar and "
                + "                                                                                         plan_fas_dekor.nar = plan_item.nar "
                + "                                                                                   Order by vid),',')) as dekor "
                + "		From plan, plan_item, spec "
                + "		Where plan.id = plan_item.id_plan and "
                + "			plan.id = ? and "
                + "			plan_item.id_spec = spec.id "
                + "		Group by idPlan, fas_vid, fas_pname, fas, kol_x, note_item, idSpec, modelSpec, nameSpec, sar, nar ) as t1, spec_item "
                + "	Where t1.idSpec = spec_item.id_spec and "
                + "		spec_item.num >= 0 and "
                + "		spec_item.id_tech <> -1 "
                + "	Group by fas_vid, fas_pname, fas, kolM, kol_x, note_item, idSpec, modelSpec, nameSpec, sar, nar, polotno, new_ ,dekor"
                + " Order by fas_vid, fas_pname, fas, sar, nar";
        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, idProj);
            rs = ps.executeQuery();
            while (rs.next()) {
                Vector tmp = new Vector();

                tmp.add(rs.getInt("fas_vid"));

                if (rs.getObject("new_") != null) tmp.add("V");
                else tmp.add("");

                tmp.add(rs.getString("fas_pname").toUpperCase());
                tmp.add(rs.getInt("fas"));
                tmp.add(rs.getString("polotno"));
                tmp.add(Double.valueOf(UtilFunctions.formatNorm(rs.getDouble("kolM"), 0)));
                tmp.add(rs.getInt("kol_x"));
                tmp.add(UtilFunctions.formatNorm(rs.getDouble("vnorm"), UtilPlan.ROUNDING_NORM));
                tmp.add(Double.valueOf(UtilFunctions.formatNorm(Double.valueOf(rs.getDouble("kolM") * rs.getDouble("vnorm")), 3)));
                tmp.add(rs.getString("dekor"));
                tmp.add(rs.getString("note_item"));

                elements.add(tmp);
            }

        } catch (Exception e) {
            elements = new Vector();
            System.err.println("Ошибка getDataReportProjectPlanVnorm() " + e);
            log.error("Ошибка getdataReportProjectPlanVnorm()", e);
            throw new Exception("Ошибка getdataReportProjectPlanVnorm() " + e.getMessage(), e);
        }
        return elements;
    }

    public Vector getDataReportProjectPlanVnormTemp() throws Exception {
        Vector elements = new Vector();
        String sql = "Select fas_vid, fas_pname, fas, kolM, kol_x, note_item, idSpec, modelSpec, nameSpec, sar, nar, sum(round(spec_item.vnorm::numeric," + UtilPlan.ROUNDING_NORM + ")) as vnorm,  polotno, new_ , dekor "
                + "	From (Select fas_vid, fas_pname, fas, sum(round(kol_month,3)) as kolM, kol_x, note_item, spec.id as idSpec, "
                + "			spec.id_model as modelSpec, spec.spec as nameSpec, sar, nar, "
                + "			(SELECT ARRAY_TO_STRING(ARRAY(SELECT nar_polotno FROM tmp_plan_fas_sostav Where tmp_plan_item.fas = tmp_plan_fas_sostav.fas and "
                + "                                                                                                     tmp_plan_item.sar = tmp_plan_fas_sostav.sar and "
                + "                                                                                                     tmp_plan_item.nar = tmp_plan_fas_sostav.nar "
                + "                                                                                                 Order by tmp_plan_fas_sostav.vid, nar_polotno),',')) as polotno,"
                + "			(SELECT distinct id FROM tmp_plan_fas_new Where tmp_plan_item.fas = tmp_plan_fas_new.fas) as new_,"
                + "			(SELECT ARRAY_TO_STRING(ARRAY(SELECT vid FROM tmp_plan_fas_dekor Where tmp_plan_item.fas = tmp_plan_fas_dekor.fas and "
                + "                                                                                             tmp_plan_item.sar = tmp_plan_fas_dekor.sar and "
                + "                                                                                             tmp_plan_item.nar = tmp_plan_fas_dekor.nar "
                + "                                                                                             Order by vid),',')) as dekor "
                + "		From tmp_plan_item, spec "
                + "		Where tmp_plan_item.id_spec = spec.id "
                + "		Group by fas_vid, fas_pname, fas, kol_x, note_item, idSpec, modelSpec, nameSpec, sar, nar ) as t1, spec_item "
                + "	Where t1.idSpec = spec_item.id_spec and "
                + "		spec_item.num >= 0 and "
                + "		spec_item.id_tech <> -1 "
                + "	Group by fas_vid, fas_pname, fas, kolM, kol_x, note_item, idSpec, modelSpec, nameSpec, sar, nar, polotno, new_ ,dekor"
                + " Order by fas_vid, fas_pname, fas, sar, nar";
        try {
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                Vector tmp = new Vector();

                tmp.add(rs.getInt("fas_vid"));

                if (rs.getObject("new_") != null) tmp.add("V");
                else tmp.add("");

                tmp.add(rs.getString("fas_pname").toUpperCase());
                tmp.add(rs.getInt("fas"));
                tmp.add(rs.getString("polotno"));
                tmp.add(Double.valueOf(UtilFunctions.formatNorm(rs.getDouble("kolM"), 0)));
                tmp.add(rs.getInt("kol_x"));
                tmp.add(UtilFunctions.formatNorm(rs.getDouble("vnorm"), UtilPlan.ROUNDING_NORM));
                tmp.add(Double.valueOf(UtilFunctions.formatNorm(Double.valueOf(rs.getDouble("kolM") * rs.getDouble("vnorm")), 3)));
                tmp.add(rs.getString("dekor"));
                tmp.add(rs.getString("note_item"));

                elements.add(tmp);
            }

        } catch (Exception e) {
            elements = new Vector();
            System.err.println("Ошибка getDataReportProjectPlanVnormTemp() " + e);
            log.error("Ошибка getDataReportProjectPlanVnormTemp()", e);
            throw new Exception("Ошибка getDataReportProjectPlanVnormTemp() " + e.getMessage(), e);
        }
        return elements;
    }

    public int checkKolvoPlanTableTemp() throws Exception {
        int kol = 0;
        String sql = "Select count(id) as kol From tmp_plan_item ";
        try {
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) {
                kol = rs.getInt("kol");
            }

        } catch (Exception e) {
            kol = 0;
            System.err.println("Ошибка checkKolvoPlanTableTemp() " + e);
            log.error("Ошибка checkKolvoPlanTableTemp()", e);
            throw new Exception("Ошибка checkKolvoPlanTableTemp() " + e.getMessage(), e);
        }
        return kol;
    }

    public Vector getDataItemProject(int model, int cx, int sar, String nar) throws Exception {
        Vector elements = new Vector();
        String sql = "Select distinct fas, fas_pname, fas_vid, dekor, new_, kol_x, idSpec, nameSpec, "
                + "         sum(round(vnorm::numeric," + UtilPlan.ROUNDING_NORM + ")) as vnorm, cx, note_item, sar, nar"
                + " From (Select distinct fas, fas_pname, fas_vid, "
                + "	(SELECT ARRAY_TO_STRING(ARRAY(SELECT vid FROM tmp_plan_fas_dekor Where tmp_plan_item.fas = tmp_plan_fas_dekor.fas and "
                + "                                                                             tmp_plan_item.cx = tmp_plan_fas_dekor.cx and "
                + "                                                                             tmp_plan_item.sar = tmp_plan_fas_dekor.sar and "
                + "                                                                             tmp_plan_item.nar = tmp_plan_fas_dekor.nar "
                + "                                                                      Order by vid),',')) as dekor,   "
                + "	(SELECT ARRAY_TO_STRING(ARRAY(SELECT distinct id FROM tmp_plan_fas_new Where tmp_plan_item.fas = tmp_plan_fas_new.fas ),','))  as new_,   "
                + "	kol_x, spec.id as idSpec, spec.spec as nameSpec, "
                + "     (round(spec_item.vnorm::numeric," + UtilPlan.ROUNDING_NORM + ")) as vnorm, cx, note_item, sar, nar "
                + "                From tmp_plan_item, spec, spec_item  "
                + "	Where tmp_plan_item.id_spec = spec.id and "
                + "		fas = ? and "
                + "		cx = ? and "
                + "		sar = ? and "
                + "		nar like ? and "
                + "		spec.id = spec_item.id_spec and "
                + "		spec_item.num >= 0 and  "
                + "		spec_item.id_tech <> -1 ) as t1 "
                + " Group by fas, fas_pname, fas_vid, dekor, new_, kol_x, idSpec,  nameSpec, cx, note_item, sar, nar ";
        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, model);
            ps.setInt(2, cx);
            ps.setInt(3, sar);
            ps.setString(4, nar);
            rs = ps.executeQuery();
            if (rs.next()) {
                elements.add(rs.getInt("fas"));
                elements.add(rs.getString("fas_pname"));
                elements.add(rs.getInt("fas_vid"));
                elements.add(rs.getString("dekor"));
                elements.add((!rs.getObject("new_").equals("")) ? true : false);
                elements.add(rs.getInt("kol_x"));
                elements.add(rs.getInt("idSpec"));
                elements.add(rs.getString("nameSpec"));
                elements.add(rs.getDouble("vnorm"));
                elements.add(rs.getInt("cx"));
                elements.add(rs.getString("note_item"));
                elements.add(rs.getInt("sar"));
                elements.add(rs.getString("nar"));
            }

        } catch (Exception e) {
            elements = new Vector();
            System.err.println("Ошибка getDataItemProject() " + e);
            log.error("Ошибка getDataItemProject()", e);
            throw new Exception("Ошибка getDataItemProject() " + e.getMessage(), e);
        }
        return elements;
    }

    public Vector getDataShkalaProject(int model, int cx, int sar, String nar) throws Exception {
        Vector elements = new Vector();
        String sql = "Select rst, rzm, kol_month as kolM, dekada1, dekada2, dekada3 "
                + "	From tmp_plan_item "
                + "	Where 	fas = ? and "
                + "		cx = ? and "
                + "		sar = ? and "
                + "		nar like ? "
                + "	Order by  rst, rzm";
        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, model);
            ps.setInt(2, cx);
            ps.setInt(3, sar);
            ps.setString(4, nar);
            rs = ps.executeQuery();
            while (rs.next()) {
                Vector tmp = new Vector();
                tmp.add(false);
                tmp.add(rs.getInt("rst"));
                tmp.add(rs.getInt("rzm"));
                tmp.add(rs.getDouble("kolM"));
                tmp.add(rs.getDouble("dekada1"));
                tmp.add(rs.getDouble("dekada2"));
                tmp.add(rs.getDouble("dekada3"));
                elements.add(tmp);
            }

        } catch (Exception e) {
            elements = new Vector();
            System.err.println("Ошибка getDataShkalaProject() " + e);
            log.error("Ошибка getDataShkalaProject()", e);
            throw new Exception("Ошибка getDataShkalaProject() " + e.getMessage(), e);
        }
        return elements;
    }

    public Vector getDataSostavProject(int model, int cx, int sar, String nar) throws Exception {
        Vector elements = new Vector();
        String sql = "Select distinct id_polotno, nar_polotno, sostav, vid, rashod "
                + " From tmp_plan_fas_sostav "
                + " Where fas = ? and cx = ? and sar = ? and nar like ?"
                + " Order by nar_polotno, vid";
        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, model);
            ps.setInt(2, cx);
            ps.setInt(3, sar);
            ps.setString(4, nar);
            rs = ps.executeQuery();
            while (rs.next()) {
                Vector tmp = new Vector();
                tmp.add(false);
                tmp.add(rs.getInt("id_polotno"));
                tmp.add(rs.getString("nar_polotno"));
                tmp.add(rs.getString("sostav"));
                tmp.add(rs.getInt("vid"));
                tmp.add(rs.getDouble("rashod"));
                elements.add(tmp);
            }

        } catch (Exception e) {
            elements = new Vector();
            System.err.println("Ошибка getDataSostavProject() " + e);
            log.error("Ошибка getDataSostavProject()", e);
            throw new Exception("Ошибка getDataSostavProject() " + e.getMessage(), e);
        }
        return elements;
    }

    public Vector getDataColorProject(int model, int cx, int sar, String nar) throws Exception {
        Vector elements = new Vector();
        String sql = "Select distinct id_color, ncw "
                + "     From tmp_plan_fas_color "
                + "     Where fas = ? and cx = ? and sar = ? and nar like ?"
                + "     Order by ncw ";
        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, model);
            ps.setInt(2, cx);
            ps.setInt(3, sar);
            ps.setString(4, nar);
            rs = ps.executeQuery();
            while (rs.next()) {
                Vector tmp = new Vector();
                tmp.add(false);
                tmp.add(rs.getInt("id_color"));
                tmp.add(rs.getString("ncw"));
                elements.add(tmp);
            }

        } catch (Exception e) {
            elements = new Vector();
            System.err.println("Ошибка getDataColorProject() " + e);
            log.error("Ошибка getDataColorProject()", e);
            throw new Exception("Ошибка getDataColorProject() " + e.getMessage(), e);
        }
        return elements;
    }

    public Vector getRashodMaterialModels(int id) throws Exception {
        Vector elements = new Vector();
        String sql = "Select id, cx, sar, nar, fas, id_polotno, vid, nar_polotno, sostav, rashod "
                + " From plan_fas_sostav "
                + " Where plan_fas_sostav.id_plan = ? "
                + " Order by  fas, sar, vid, nar_polotno, sostav";
        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();
            while (rs.next()) {
                Vector tmp = new Vector();
                tmp.add(false);
                tmp.add(rs.getInt("id"));
                tmp.add(rs.getInt("cx"));
                tmp.add(rs.getInt("sar"));
                tmp.add(rs.getString("nar"));
                tmp.add(rs.getInt("fas"));
                tmp.add(rs.getInt("id_polotno"));
                tmp.add(rs.getInt("vid"));
                tmp.add(rs.getString("nar_polotno"));
                tmp.add(rs.getString("sostav"));
                tmp.add(Double.valueOf(UtilFunctions.formatNorm(rs.getDouble("rashod"), 2)));
                elements.add(tmp);
            }

        } catch (Exception e) {
            elements = new Vector();
            System.err.println("Ошибка getRashodMaterialModels() " + e);
            log.error("Ошибка getRashodMaterialModels()", e);
            throw new Exception("Ошибка getRashodMaterialModels() " + e.getMessage(), e);
        }
        return elements;
    }

    public Vector getRashodMaterialModelsTemp() throws Exception {
        Vector elements = new Vector();
        String sql = "Select id, cx, sar, nar, fas, id_polotno, vid, nar_polotno, sostav, rashod "
                + " From tmp_plan_fas_sostav "
                + " Order by  fas, sar, vid, nar_polotno, sostav";
        try {
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                Vector tmp = new Vector();
                tmp.add(false);
                tmp.add(rs.getInt("id"));
                tmp.add(rs.getInt("cx"));
                tmp.add(rs.getInt("sar"));
                tmp.add(rs.getString("nar"));
                tmp.add(rs.getInt("fas"));
                tmp.add(rs.getInt("id_polotno"));
                tmp.add(rs.getInt("vid"));
                tmp.add(rs.getString("nar_polotno"));
                tmp.add(rs.getString("sostav"));
                tmp.add(Double.valueOf(UtilFunctions.formatNorm(rs.getDouble("rashod"), 2)));
                elements.add(tmp);
            }

        } catch (Exception e) {
            elements = new Vector();
            System.err.println("Ошибка getRashodMaterialModelsTemp() " + e);
            log.error("Ошибка getRashodMaterialModelsTemp()", e);
            throw new Exception("Ошибка getRashodMaterialModelsTemp() " + e.getMessage(), e);
        }
        return elements;
    }

    public Vector getReportRashodMaterialModelsProject(int id) throws Exception {
        Vector elements = new Vector();
        String sql = "Select fas_pname, kol_x, idSostav, t1.cx as cx, t1.sar as sar, t1.nar as nar, t1.fas as fas,"
                + "          id_polotno, vid, nar_polotno, sostav, rashod, sum(round(kol_month,3)) as kolM, "
                + "          (SELECT distinct id FROM plan_fas_new Where plan_item.fas = plan_fas_new.fas and plan_fas_new.id_plan = plan_item.id_plan) as new_ "
                + " From (Select id as idSostav, cx, sar, nar, fas, id_polotno, vid, nar_polotno, sostav, rashod "
                + "        From plan_fas_sostav  "
                + "        Where plan_fas_sostav.id_plan = ? "
                + "        Order by  fas, sar, vid, nar_polotno, sostav) as t1, plan_item "
                + " Where id_plan = ? and "
                + "	t1.cx = plan_item.cx and "
                + "	t1.sar= plan_item.sar and "
                + "	t1.nar = plan_item.nar and "
                + "	t1.fas = plan_item.fas "
                + " Group by fas_pname, kol_x, idSostav, t1.cx, t1.sar, t1.nar, t1.fas, id_polotno, vid, nar_polotno, sostav, rashod, new_ "
                + " Order by  fas, sar, nar, vid, nar_polotno, sostav ";
        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            ps.setInt(2, id);
            rs = ps.executeQuery();
            while (rs.next()) {
                Vector tmp = new Vector();

                if (rs.getObject("new_") != null) tmp.add("V");
                else tmp.add("");

                tmp.add(rs.getString("fas_pname").toUpperCase());
                tmp.add(rs.getInt("fas"));
                tmp.add(rs.getString("nar_polotno"));
                //tmp.add(rs.getString("sostav"));                   
                tmp.add(Double.valueOf(UtilFunctions.formatNorm(rs.getDouble("kolM"), 0)));
                tmp.add(rs.getInt("kol_x"));
                tmp.add(Double.valueOf(UtilFunctions.formatNorm(rs.getDouble("rashod"), 2)));
                elements.add(tmp);
            }

        } catch (Exception e) {
            elements = new Vector();
            System.err.println("Ошибка getRashodMaterialModels() " + e);
            log.error("Ошибка getRashodMaterialModels()", e);
            throw new Exception("Ошибка getRashodMaterialModels() " + e.getMessage(), e);
        }
        return elements;
    }

    public Vector getReportRashodMaterialModelsProjectTemp() throws Exception {
        Vector elements = new Vector();
        String sql = "Select fas_pname, kol_x, idSostav, t1.cx as cx, t1.sar as sar, t1.nar as nar, t1.fas as fas,"
                + "          id_polotno, vid, nar_polotno, sostav, rashod, sum(round(kol_month,3)) as kolM, "
                + "          (SELECT distinct id FROM tmp_plan_fas_new Where tmp_plan_item.fas = tmp_plan_fas_new.fas ) as new_ "
                + " From (Select id as idSostav, cx, sar, nar, fas, id_polotno, vid, nar_polotno, sostav, rashod "
                + "        From tmp_plan_fas_sostav  "
                + "        Order by  fas, sar, vid, nar_polotno, sostav) as t1, tmp_plan_item "
                + " Where t1.cx = tmp_plan_item.cx and "
                + "	t1.sar= tmp_plan_item.sar and "
                + "	t1.nar = tmp_plan_item.nar and "
                + "	t1.fas = tmp_plan_item.fas "
                + " Group by fas_pname, kol_x, idSostav, t1.cx, t1.sar, t1.nar, t1.fas, id_polotno, vid, nar_polotno, sostav, rashod, new_ "
                + " Order by  fas, sar, nar, vid, nar_polotno, sostav ";
        try {
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                Vector tmp = new Vector();

                if (rs.getObject("new_") != null) tmp.add("V");
                else tmp.add("");

                tmp.add(rs.getString("fas_pname").toUpperCase());
                tmp.add(rs.getInt("fas"));
                tmp.add(rs.getString("nar_polotno"));
                //tmp.add(rs.getString("sostav"));                   
                tmp.add(Double.valueOf(UtilFunctions.formatNorm(rs.getDouble("kolM"), 0)));
                tmp.add(rs.getInt("kol_x"));
                tmp.add(Double.valueOf(UtilFunctions.formatNorm(rs.getDouble("rashod"), 2)));
                elements.add(tmp);
            }

        } catch (Exception e) {
            elements = new Vector();
            System.err.println("Ошибка getRashodMaterialModelsTemp() " + e);
            log.error("Ошибка getRashodMaterialModelsTemp()", e);
            throw new Exception("Ошибка getRashodMaterialModelsTemp() " + e.getMessage(), e);
        }
        return elements;
    }

    boolean searchItemTempProjectTable(int fasNum, int sarText, String narText) throws Exception {
        boolean rezalt = false;

        String sql = "Select distinct id From tmp_plan_item Where fas = ? and sar = ? and nar like ? ";
        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, fasNum);
            ps.setInt(2, sarText);
            ps.setString(3, narText);
            rs = ps.executeQuery();

            if (rs.next()) {
                rezalt = true;
            }

        } catch (Exception e) {
            rezalt = false;
            System.err.println("Ошибка searchItemTempProjectTable() " + e);
            log.error("Ошибка searchItemTempProjectTable()", e);
            throw new Exception("Ошибка searchItemTempProjectTable() " + e.getMessage(), e);
        }
        return rezalt;
    }

    public Vector getDataMaterialTable(int idPlan) throws Exception {
        Vector elements = new Vector();

        String sql = "Select fas, sum(round(kol_month,3)) as kolM, dekada1, dekada2, dekada3, cx, sar, nar "
                + "     From plan_item, plan "
                + "     Where plan.id = plan_item.id_plan and "
                + "           plan.id = ? "
                + "     Group by fas, dekada1, dekada2, dekada3, cx, sar, nar";
        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, idPlan);
            rs = ps.executeQuery();
            while (rs.next()) {
                Vector tmp = new Vector();
                tmp.add(rs.getInt("fas"));
                tmp.add(Double.valueOf(UtilFunctions.formatNorm(rs.getDouble("kolM"), 0)));
                tmp.add(rs.getDouble("dekada1"));
                tmp.add(rs.getDouble("dekada2"));
                tmp.add(rs.getDouble("dekada3"));
                tmp.add(rs.getInt("cx"));
                tmp.add(rs.getInt("sar"));
                tmp.add(rs.getString("nar"));
                elements.add(tmp);
            }

        } catch (Exception e) {
            elements = new Vector();
            System.err.println("Ошибка getDataMaterialTable() " + e);
            log.error("Ошибка getDataMaterialTable()", e);
            throw new Exception("Ошибка getDataMaterialTable() " + e.getMessage(), e);
        }
        return elements;
    }

    public Vector getDataMaterialTableTemp() throws Exception {
        Vector elements = new Vector();
        String sql = "Select fas, sum(round(kol_month,3)) as kolM, dekada1, dekada2, dekada3, cx, sar, nar "
                + "     From tmp_plan_item "
                + "     Group by fas, dekada1, dekada2, dekada3, cx, sar, nar";
        try {
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                Vector tmp = new Vector();
                tmp.add(rs.getInt("fas"));
                tmp.add(Double.valueOf(UtilFunctions.formatNorm(rs.getDouble("kolM"), 0)));
                tmp.add(rs.getDouble("dekada1"));
                tmp.add(rs.getDouble("dekada2"));
                tmp.add(rs.getDouble("dekada3"));
                tmp.add(rs.getInt("cx"));
                tmp.add(rs.getInt("sar"));
                tmp.add(rs.getString("nar"));
                elements.add(tmp);
            }

        } catch (Exception e) {
            elements = new Vector();
            System.err.println("Ошибка getDataMaterialTableTemp() " + e);
            log.error("Ошибка getDataMaterialTableTemp()", e);
            throw new Exception("Ошибка getDataMaterialTableTemp() " + e.getMessage(), e);
        }
        return elements;
    }

    public boolean mixPlan(int idPlan1, int idPlan2, int idEmpl) throws Exception {
        boolean rez = false;

        String sql = "";

        try {
            setAutoCommit(false);

            sql = "Select cx, sar, fas, rst, rzm, kol_month, kol_day, id_empl_ins "
                    + " From plan_item "
                    + " Where id_plan = ? "
                    + " Order by id ";

            ps = conn.prepareStatement(sql);
            ps.setInt(1, idPlan1);
            rs = ps.executeQuery();

            while (rs.next()) {

                sql = "Insert into plan_item(id_plan, cx, sar, fas, rst, rzm, kol_month, kol_day, id_empl_ins) "
                        + "values( ?, ?, ?, ?, ?, ?, ?, ?, ?)";

                ps = conn.prepareStatement(sql);
                ps.setInt(1, idPlan2);
                ps.setInt(2, rs.getInt("cx"));
                ps.setInt(3, rs.getInt("sar"));
                ps.setInt(4, rs.getInt("fas"));
                ps.setInt(5, rs.getInt("rst"));
                ps.setInt(6, rs.getInt("rzm"));
                ps.setDouble(7, rs.getDouble("kol_month"));
                ps.setDouble(8, rs.getDouble("kol_day"));
                ps.setInt(9, idEmpl);
                ps.execute();
            }

            sql = "Update plan Set id_empl_ins = ?, status = ?, date_ins = now() "
                    + " Where plan.id = ?";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, idEmpl);
            ps.setInt(2, -1);
            ps.setInt(3, idPlan1);
            ps.execute();

            commit();

            rez = true;
        } catch (Exception e) {
            rollBack();
            rez = false;
            System.err.println("Ошибка mixPlan() " + e);
            log.error("Ошибка mixPlan()", e);
            throw new Exception("Ошибка mixPlan() " + e.getMessage(), e);
        } finally {
            setAutoCommit(true);
        }

        return rez;
    }

    /**
     *
     * @param idPlan
     * @return
     * @throws Exception
     */
    public Vector getDataAllNormPlan(int idPlan) throws Exception {
        Vector elements = new Vector();
        String sql = "";

        try {
            sql = "Select  fas, kol_month, (Select naim from s_model where s_model.model = fas) as ngpr, "
                    + "	shidSpec, shdept, shvnorm, vnormVTO, "
                    + "	zkidSpec, zkdept, zkvnorm, (Select sum(round(vnorm::numeric," + UtilPlan.ROUNDING_NORM + ")) as vnorm "
                    + "					From spec, spec_item "
                    + "					Where spec.id = zkidSpec and "
                    + "					   spec.id = spec_item.id_spec and  "
                    + "					   spec_item.num < 0 "
                    + "					Group by id_model "
                    + "					Order by id_model ) as vnormMULT "
                    + " From (Select fas, kol_month, shidSpec, shdept, shvnorm, (Select sum(round(vnorm::numeric," + UtilPlan.ROUNDING_NORM + ")) as vnorm "
                    + "								From spec, spec_item "
                    + "								Where spec.id = shidSpec and "
                    + "								   spec.id = spec_item.id_spec and  "
                    + "								   spec_item.num < 0 "
                    + "								Group by id_model "
                    + "								Order by id_model ) as vnormVTO, "
                    + "		idSpec as zkidSpec, idDept as zkdept, maxvnorm as zkvnorm  "
                    + "	From(Select fas, kol_month, idSpec as shidSpec, idDept as shdept, maxvnorm as shvnorm "
                    + "		From(Select fas, sum(round(kol_month,3)) as kol_month "
                    + "			From plan, plan_item "
                    + "			Where plan_item.id_plan = ? and "
                    + "			      plan.id = plan_item.id_plan "
                    + "			Group by fas "
                    + "			Order by fas) as t1 "
                    + "		left join max_vnorm_date_spec "
                    + "		On t1.fas = max_vnorm_date_spec.model and "
                    + "			idDept = 8  ) as t2 "
                    + "	left join max_vnorm_date_spec "
                    + "	On t2.fas = max_vnorm_date_spec.model and "
                    + "		idDept = 9 "
                    + "	) as t_rez "
                    + " Order by ngpr, fas, shdept ";

            ps = conn.prepareStatement(sql);
            ps.setInt(1, idPlan);
            rs = ps.executeQuery();

            while (rs.next()) {
                Vector tmp = new Vector();
                tmp.add(rs.getString("ngpr"));
                tmp.add(rs.getInt("fas"));
                tmp.add(Double.valueOf(UtilFunctions.formatNorm(rs.getDouble("kol_month"), 0)));
                tmp.add(UtilFunctions.formatNorm(rs.getDouble("shvnorm"), UtilPlan.ROUNDING_NORM));
                tmp.add(UtilFunctions.formatNorm(rs.getDouble("vnormVTO"), UtilPlan.ROUNDING_NORM));
                tmp.add(UtilFunctions.formatNorm(rs.getDouble("zkvnorm"), UtilPlan.ROUNDING_NORM));
                tmp.add(UtilFunctions.formatNorm(rs.getDouble("vnormMULT"), UtilPlan.ROUNDING_NORM));
                elements.add(tmp);
            }

        } catch (Exception e) {
            elements = new Vector();
            System.err.println("Ошибка getDataAllNormPlan() " + e);
            log.error("Ошибка getDataAllNormPlan()", e);
            throw new Exception("Ошибка getDataAllNormPlan() " + e.getMessage(), e);
        }
        return elements;
    }

    public Vector getDataAnalysisPlan(int idPlan) throws Exception {
        Vector elements = new Vector();
        String sql = "";

        try {
            sql = "Select fas_vid, fas, fas_pname, sum(kol_month) as kol_month , "
                    + "        (SELECT distinct id "
                    + "          FROM plan_fas_new "
                    + "          Where plan_item.fas = plan_fas_new.fas and "
                    + "                plan_fas_new.id_plan = plan_item.id_plan) as new_ "
                    + " From plan, plan_item "
                    + " Where plan.id = plan_item.id_plan and "
                    + "       plan.id = ? "
                    + " Group by fas_vid, fas_pname, fas, new_ "
                    + " Order by fas ";

            ps = conn.prepareStatement(sql);
            ps.setInt(1, idPlan);
            rs = ps.executeQuery();

            while (rs.next()) {
                Vector tmp = new Vector();
                tmp.add(false);
                tmp.add(UtilPlan.getVid(rs.getInt("fas_vid")));
                tmp.add(rs.getString("fas_pname"));
                tmp.add(rs.getInt("fas"));
                tmp.add(Double.valueOf(UtilFunctions.formatNorm(rs.getDouble("kol_month"), 0)));
                tmp.add(0);
                tmp.add(0);
                if (rs.getObject("new_") != null) tmp.add(UtilEan.NEW);
                else tmp.add("");
                elements.add(tmp);
            }

        } catch (Exception e) {
            elements = new Vector();
            System.err.println("Ошибка getDataAllNormPlan() " + e);
            log.error("Ошибка getDataAllNormPlan()", e);
            throw new Exception("Ошибка getDataAllNormPlan() " + e.getMessage(), e);
        }
        return elements;
    }

    public Vector getDataAnalysisPlans(int idProj, int idPlan, int idVypusk) throws Exception {
        Vector elements = new Vector();
        String sql = "";

        try {
            sql = "Select fasAll, naim, vid, fasProj, kolProj, new_ , fasPlan, kolPlan, fasVypusk, kolVypusk "
                    + "From (Select fasAll, vid, fasProj, kolProj, new_ , fasPlan, kolPlan, fas as fasVypusk, sum(kol_month) as kolVypusk "
                    + "       From(Select fasAll, vid, fasProj, kolProj, new_ , fas as fasPlan, sum(kol_month) as kolPlan "
                    + "               From(Select fasAll, fas_vid as vid, fas as fasProj, sum(kol_month) as kolProj, (SELECT distinct id FROM plan_fas_new Where plan_item.fas = plan_fas_new.fas and plan_fas_new.id_plan = plan_item.id_plan) as new_ "
                    + "                       From (Select distinct fas as fasAll "
                    + "                               From plan, plan_item "
                    + "                               Where plan.id = plan_item.id_plan and"
                    + "                                       (plan.id = " + idProj + " or "
                    + "                                        plan.id = " + idPlan + " or "
                    + "                                        plan.id = " + idVypusk + " )) as t1 left join plan_item on id_plan = " + idProj + " and fasAll = fas "
                    + "                               Group by fasAll, vid, fasProj, new_) as t2 left join plan_item on id_plan = " + idPlan + " and fasAll = fas "
                    + "                       Group by fasAll, vid, fasProj, kolProj, new_, fasPlan) as t3 left join plan_item on id_plan = " + idVypusk + " and fasAll = fas "
                    + "               Group by fasAll, vid, fasProj, kolProj, new_, fasPlan, kolPlan, fasVypusk )as t4 left join s_model on fasAll = model "
                    + "Order by fasProj, fasAll ";

            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                Vector tmp = new Vector();
                tmp.add(false);
                tmp.add(rs.getObject("vid") != null ? UtilPlan.getVid(rs.getInt("vid")) : UtilPlan.getVid(0));
                tmp.add(rs.getObject("naim") != null ? rs.getString("naim") : "");
                tmp.add(rs.getInt("fasAll"));
                tmp.add(Double.valueOf(UtilFunctions.formatNorm(rs.getDouble("kolProj"), 0)));
                tmp.add(Double.valueOf(UtilFunctions.formatNorm(rs.getDouble("kolPlan"), 0)));
                tmp.add(Double.valueOf(UtilFunctions.formatNorm(rs.getDouble("kolVypusk"), 0)));
                if (rs.getObject("new_") != null) tmp.add(UtilEan.NEW);
                else tmp.add("");
                elements.add(tmp);
            }

        } catch (Exception e) {
            elements = new Vector();
            System.err.println("Ошибка getDataAnalysisPlans() " + e);
            log.error("Ошибка getDataAnalysisPlans()", e);
            throw new Exception("Ошибка getDataAnalysisPlans() " + e.getMessage(), e);
        }
        return elements;
    }

    public Vector getDetalFasProject(int idProj, int fas) throws Exception {
        Vector elements = new Vector();
        String sql = "";
        try {
            sql = "Select min(rst) as minrst, max(rst) as maxrst,  max(rzm) as maxrzm,  min(rzm) as minrzm, t2.fas_vid as fas_vid, t2.fas_pname as fas_pname, "
                    + "                	t2.fas as fas, t2.kolM as kolM, t2.kol_x as kol_x, t2.note_item as note_item, t2.sar as sar, t2.nar as nar, polotno, color, idNewFas "
                    + "                 From(Select plan_item.id_plan as idPlan , fas_vid, fas_pname, fas, sum(round(kol_month,3)) as kolM, kol_x, note_item, sar, nar, "
                    + "                			(SELECT ARRAY_TO_STRING(ARRAY(SELECT nar_polotno FROM plan_fas_sostav Where plan_item.fas = plan_fas_sostav.fas and "
                    + "                                                                                                                 plan_fas_sostav.id_plan = plan_item.id_plan and "
                    + "                                                                                                                 plan_fas_sostav.sar = plan_item.sar and "
                    + "                                                                                                                 plan_fas_sostav.nar = plan_item.nar "
                    + "                                                                                                            Order by plan_fas_sostav.vid, nar_polotno),', ')) as polotno, "
                    + "                			(SELECT ARRAY_TO_STRING(ARRAY(SELECT ncw FROM plan_fas_color Where plan_item.fas = plan_fas_color.fas and "
                    + "                                                                                                        plan_fas_color.id_plan = plan_item.id_plan and                "
                    + "                                                                                                        plan_fas_color.sar = plan_item.sar and                 "
                    + "                                                                                                        plan_fas_color.nar = plan_item.nar "
                    + "                                                                                                   Order by ncw),', ')) as color,"
                    + "                			(SELECT distinct id FROM plan_fas_new Where plan_item.fas = plan_fas_new.fas and plan_fas_new.id_plan = plan_item.id_plan) as idNewFas "
                    + "                		From plan, plan_item "
                    + "                		Where plan.id = plan_item.id_plan and  "
                    + "                			plan.id = ? and "
                    + "                			plan_item.fas = ? "
                    + "                		Group by idPlan, fas_vid, fas_pname, fas, kol_x, note_item, sar, nar ) as t2, plan_item "
                    + "                 Where t2.idPlan = plan_item.id_plan and "
                    + "                     t2.fas_vid = plan_item.fas_vid and "
                    + "                     t2.fas_pname = plan_item.fas_pname and "
                    + "                     t2.fas = plan_item.fas and "
                    + "                     t2.kol_x = plan_item.kol_x and "
                    + "                	t2.note_item = plan_item.note_item and "
                    + "                	t2.sar = plan_item.sar and "
                    + "                	t2.nar = plan_item.nar "
                    + "                 Group by t2.fas_vid, t2.fas_pname, t2.fas, t2.kolM, t2.kol_x, t2.note_item, t2.sar, t2.nar, polotno, color, idNewFas "
                    + "                 Order by fas, fas_pname, sar, nar";

            ps = conn.prepareStatement(sql);
            ps.setInt(1, idProj);
            ps.setInt(2, fas);
            rs = ps.executeQuery();

            while (rs.next()) {
                Vector tmp = new Vector();
                tmp.add(rs.getObject("fas_vid") != null ? UtilPlan.getVid(rs.getInt("fas_vid")) : UtilPlan.getVid(0));
                tmp.add(rs.getString("fas_pname"));
                tmp.add(rs.getInt("fas"));
                tmp.add(rs.getString("polotno"));
                tmp.add(rs.getInt("kol_x"));
                tmp.add(rs.getString("color"));
                tmp.add(rs.getString("note_item"));
                if (rs.getObject("idNewFas") != null) tmp.add(UtilPlan.NEW);
                else tmp.add("");

                //  tmp.add(rs.getString("minrst").equals(rs.getString("maxrst")) ? rs.getString("minrst") :  rs.getString("minrst")+"--"+rs.getString("maxrst"));
                tmp.add(rs.getString("minrzm").equals(rs.getString("maxrzm")) ? rs.getString("minrzm") : rs.getString("minrzm") + "--" + rs.getString("maxrzm"));

                tmp.add(Double.valueOf(UtilFunctions.formatNorm(rs.getDouble("kolM"), 0)));

                elements.add(tmp);
            }

        } catch (Exception e) {
            elements = new Vector();
            System.err.println("Ошибка getDetalFasProject() " + e);
            log.error("Ошибка getDetalFasProject()", e);
            throw new Exception("Ошибка getDetalFasProject() " + e.getMessage(), e);
        }
        return elements;
    }

    public Vector getDetalFasPlan(int idPlan, int fas) throws Exception {
        Vector elements = new Vector();
        String sql = "";
        try {
            sql = "Select plan_item.id_plan as idPlan , fas_vid, fas_pname, fas, "
                    + " sum(round(kol_month,3)) as kolM, kol_x, note_item, sar, nar, rst, rzm "
                    + "                		From plan, plan_item "
                    + "                		Where plan.id = plan_item.id_plan and  "
                    + "                			plan.id = ? and "
                    + "                			plan_item.fas = ? "
                    + "                		Group by idPlan, fas_vid, fas_pname, fas, kol_x, note_item, sar, nar, rst, rzm  "
                    + "                 Order by fas, fas_pname, sar, nar, rst, rzm ";

            ps = conn.prepareStatement(sql);
            ps.setInt(1, idPlan);
            ps.setInt(2, fas);
            rs = ps.executeQuery();

            while (rs.next()) {
                Vector tmp = new Vector();
                tmp.add(rs.getInt("sar"));
                tmp.add(rs.getString("fas_pname"));
                tmp.add(rs.getInt("fas"));
                tmp.add(rs.getString("nar"));
                tmp.add("");
                tmp.add(rs.getInt("rst"));
                tmp.add(rs.getInt("rzm"));
                tmp.add(Double.valueOf(UtilFunctions.formatNorm(rs.getDouble("kolM"), 0)));

                elements.add(tmp);
            }

        } catch (Exception e) {
            elements = new Vector();
            System.err.println("Ошибка getDetalFasPlan() " + e);
            log.error("Ошибка getDetalFasPlan()", e);
            throw new Exception("Ошибка getDetalFasPlan() " + e.getMessage(), e);
        }
        return elements;
    }

    public Vector getDetalFasVypusk(int idVypusk, int fas) throws Exception {
        Vector elements = new Vector();
        String sql = "";
        try {
            sql = "Select plan_item.id_plan as idPlan , fas_vid, fas_pname, fas, "
                    + " sum(round(kol_month,3)) as kolM, kol_x, note_item, sar, nar, rst, rzm "
                    + "                		From plan, plan_item "
                    + "                		Where plan.id = plan_item.id_plan and  "
                    + "                			plan.id = ? and "
                    + "                			plan_item.fas = ? "
                    + "                		Group by idPlan, fas_vid, fas_pname, fas, kol_x, note_item, sar, nar, rst, rzm  "
                    + "                 Order by fas, fas_pname, sar, nar, rst, rzm ";

            ps = conn.prepareStatement(sql);
            ps.setInt(1, idVypusk);
            ps.setInt(2, fas);
            rs = ps.executeQuery();

            while (rs.next()) {
                Vector tmp = new Vector();
                tmp.add(rs.getInt("sar"));
                tmp.add(rs.getString("fas_pname"));
                tmp.add(rs.getInt("fas"));
                tmp.add(rs.getString("nar"));
                tmp.add("");
                tmp.add(rs.getInt("rst"));
                tmp.add(rs.getInt("rzm"));
                tmp.add(Double.valueOf(UtilFunctions.formatNorm(rs.getDouble("kolM"), 0)));

                elements.add(tmp);
            }

        } catch (Exception e) {
            elements = new Vector();
            System.err.println("Ошибка getDetalFasVypusk() " + e);
            log.error("Ошибка getDetalFasVypusk()", e);
            throw new Exception("Ошибка getDetalFasVypusk() " + e.getMessage(), e);
        }
        return elements;
    }

    public Vector getFasInPlan(int idPlan, int type) throws Exception {
        Vector elements = new Vector();
        String sql = "";
        try {
            sql = "Select distinct fas "
                    + " From plan, plan_item "
                    + " Where plan.id = plan_item.id_plan and  "
                    + "       plan.id = ? "
                    + (type == -1 ? " and plan_item.nar <> '' " : " ")
                    + " Order by fas ";

            ps = conn.prepareStatement(sql);
            ps.setInt(1, idPlan);
            rs = ps.executeQuery();

            while (rs.next()) {
                elements.add(rs.getInt("fas"));
            }

        } catch (Exception e) {
            elements = new Vector();
            System.err.println("Ошибка getFasInPlan() " + e);
            log.error("Ошибка getFasInPlan()", e);
            throw new Exception("Ошибка getFasInPlan() " + e.getMessage(), e);
        }
        return elements;
    }

    /*
    public Vector getFasNarInPlan(int idPlan) throws Exception{
        Vector elements = new Vector();       
        String sql = "";	
        try {
            sql = "Select distinct fas, nar "
                    + " From plan, plan_item "
                    + " Where plan.id = plan_item.id_plan and  "
                    + "       plan.id = ? and "
                    + "       plan_item.nar not like '' "
                    + " Order by fas, nar ";
            
            ps = conn.prepareStatement(sql);
            ps.setInt(1, idPlan);
            rs = ps.executeQuery();
            
            while(rs.next()){    
                Vector tmp = new Vector();
                tmp.add(rs.getInt("fas"));                
                tmp.add(rs.getInt(""));
                elements.add(rs.getInt("fas"));
            }
            
        } catch (Exception e) {
            elements = new Vector();
            System.err.println("Ошибка getFasInPlan() "+ e);
            log.error("Ошибка getFasInPlan()", e);
            throw new Exception("Ошибка getFasInPlan() " + e.getMessage(), e);
        }
        return elements;
    }
    */

    public Vector updateProjectTable(int idProj, Vector dataProject) throws Exception {
        Vector elements = new Vector();
        String sql = "";
        int fas, sar, cx;
        String nar, narNew;

        try {
            setAutoCommit(false);

            for (Object vec : dataProject) {
                if (Boolean.valueOf(((Vector) vec).get(0).toString())) {

                    nar = ((Vector) vec).get(5).toString();
                    narNew = ((Vector) vec).get(6).toString().trim();
                    sar = Integer.valueOf(((Vector) vec).get(15).toString());
                    fas = Integer.valueOf(((Vector) vec).get(4).toString());
                    cx = Integer.valueOf(((Vector) vec).get(16).toString());

                    sql = "Select distinct fas_vid, fas_pname, fas, nar, sar, cx  "
                            + " From plan_item "
                            + " Where id_plan = ? and "
                            + "       sar = ? and "
                            + "       nar = ? and "
                            + "       fas = ? and "
                            + "       cx = ? ";

                    ps = conn.prepareStatement(sql);
                    ps.setInt(1, idProj);
                    ps.setInt(2, sar);
                    ps.setString(3, nar);
                    ps.setInt(4, fas);
                    ps.setInt(5, cx);
                    rs = ps.executeQuery();

                    if (rs.next()) {
                        sql = "Update plan_fas_color "
                                + " Set nar = ? "
                                + " Where id_plan = ? and "
                                + "       sar = ? and "
                                + "       nar = ? and "
                                + "       fas = ? and "
                                + "       cx = ? ";

                        ps = conn.prepareStatement(sql);
                        ps.setString(1, narNew);
                        ps.setInt(2, idProj);
                        ps.setInt(3, sar);
                        ps.setString(4, nar);
                        ps.setInt(5, fas);
                        ps.setInt(6, cx);
                        ps.execute();

                        sql = "Update plan_fas_dekor "
                                + " Set nar = ? "
                                + " Where id_plan = ? and "
                                + "       sar = ? and "
                                + "       nar = ? and "
                                + "       fas = ? and "
                                + "       cx = ? ";

                        ps = conn.prepareStatement(sql);
                        ps.setString(1, narNew);
                        ps.setInt(2, idProj);
                        ps.setInt(3, sar);
                        ps.setString(4, nar);
                        ps.setInt(5, fas);
                        ps.setInt(6, cx);
                        ps.execute();

                        sql = "Update plan_fas_sostav "
                                + " Set nar = ? "
                                + " Where id_plan = ? and "
                                + "       sar = ? and "
                                + "       nar = ? and "
                                + "       fas = ? and "
                                + "       cx = ? ";

                        ps = conn.prepareStatement(sql);
                        ps.setString(1, narNew);
                        ps.setInt(2, idProj);
                        ps.setInt(3, sar);
                        ps.setString(4, nar);
                        ps.setInt(5, fas);
                        ps.setInt(6, cx);
                        ps.execute();

                        sql = "Update plan_item "
                                + " Set nar = ? "
                                + " Where id_plan = ? and "
                                + "       sar = ? and "
                                + "       nar = ? and "
                                + "       fas = ? and "
                                + "       cx = ? ";

                        ps = conn.prepareStatement(sql);
                        ps.setString(1, narNew);
                        ps.setInt(2, idProj);
                        ps.setInt(3, sar);
                        ps.setString(4, nar);
                        ps.setInt(5, fas);
                        ps.setInt(6, cx);
                        ps.execute();

                    } else {
                        elements.add(vec);
                    }
                }
            }

            commit();

        } catch (Exception e) {
            rollBack();
            elements = new Vector();
            System.err.println("Ошибка updateProjectTable() " + e);
            log.error("Ошибка updateProjectTable()", e);
            throw new Exception("Ошибка updateProjectTable() " + e.getMessage(), e);
        } finally {
            setAutoCommit(true);
        }
        return elements;
    }

    public void updateProjectWorkTable(int idProj, Vector dataProjectWork, int idEmpl) throws Exception {
        String sql = "";
        int flag, idItem;
        String fas, nar, polotno, note;

        try {
            setAutoCommit(false);

            for (Object vec : dataProjectWork) {

                idItem = Integer.valueOf(((Vector) vec).get(1).toString());
                flag = Integer.valueOf(((Vector) vec).get(2).toString());

                fas = ((Vector) vec).get(3).toString();
                nar = ((Vector) vec).get(4).toString();
                polotno = ((Vector) vec).get(6).toString();
                note = ((Vector) vec).get(7).toString();

                if (flag == 0) {

                    sql = " INSERT INTO plan_work(id_plan, nar, polotno, note, id_empl_ins, fas)"
                            + "    VALUES (?, ?, ?, ?, ?, ?); ";

                    ps = conn.prepareStatement(sql);
                    ps.setInt(1, idProj);
                    ps.setString(2, nar);
                    ps.setString(3, polotno);
                    ps.setString(4, note);
                    ps.setInt(5, idEmpl);
                    ps.setString(6, fas);
                    ps.execute();

                } else if (flag == 1) {

                    sql = "UPDATE plan_work "
                            + "   SET nar=?, "
                            + "       polotno=?, "
                            + "       note=?, "
                            + "       id_empl_ins=?, "
                            + "       fas=?"
                            + " WHERE id_plan = ? and "
                            + "       id = ?; ";

                    ps = conn.prepareStatement(sql);
                    ps.setString(1, nar);
                    ps.setString(2, polotno);
                    ps.setString(3, note);
                    ps.setInt(4, idEmpl);
                    ps.setString(5, fas);
                    ps.setInt(6, idProj);
                    ps.setInt(7, idItem);
                    ps.execute();

                } else if (flag == -1 && idItem > 0) {

                    sql = "DELETE FROM plan_work "
                            + " WHERE id_plan = ? and "
                            + "       id = ?; ";

                    ps = conn.prepareStatement(sql);
                    ps.setInt(1, idProj);
                    ps.setInt(2, idItem);
                    ps.execute();
                }
            }

            commit();

        } catch (Exception e) {
            rollBack();
            System.err.println("Ошибка updateProjectTable() " + e);
            log.error("Ошибка updateProjectTable()", e);
            throw new Exception("Ошибка updateProjectTable() " + e.getMessage(), e);
        } finally {
            setAutoCommit(true);
        }
    }

    public Vector getDataWorkModelProject() throws Exception {
        Vector elements = new Vector();
        String sql = "";
        try {
            sql = "Select model, naim, idWork, fas, noteWork "
                    + " from (Select plan_work_model.id as idWork,"
                    + "         plan_work_model.id_model as fas, "
                    + "		plan_work_model.note as noteWork "
                    + "	from plan_work_model ) as t1 "
                    + "     right join s_model "
                    + "     on t1.fas = s_model.model "
                    + " Order by model, naim, noteWork";

            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                Vector tmp = new Vector();
                tmp.add(false);
                tmp.add(rs.getInt("idWork"));
                tmp.add(rs.getInt("model"));
                tmp.add(rs.getString("naim"));
                tmp.add((rs.getObject("noteWork") == null) ? "" : rs.getString("noteWork"));
                elements.add(tmp);
            }

        } catch (Exception e) {
            elements = new Vector();
            System.err.println("Ошибка getDataWorkModelProject() " + e);
            log.error("Ошибка getDataWorkModelProject()", e);
            throw new Exception("Ошибка getDataWorkModelProject() " + e.getMessage(), e);
        }
        return elements;
    }

    public void saveDataWorkModelProject(Vector data) throws Exception {
        String sql = "";
        int fas, idItem;
        String note = "";

        try {
            setAutoCommit(false);

            for (Object vec : data) {

                idItem = Integer.valueOf(((Vector) vec).get(1).toString());
                fas = Integer.valueOf(((Vector) vec).get(2).toString());

                note = ((Vector) vec).get(4).toString();

                if (idItem == 0) {

                    sql = " INSERT INTO plan_work_model(id_model, note) VALUES (?, ?);";

                    ps = conn.prepareStatement(sql);
                    ps.setInt(1, fas);
                    ps.setString(2, note);
                    ps.execute();

                } else if (idItem != 0) {

                    sql = " UPDATE plan_work_model "
                            + "   SET note=? "
                            + "   WHERE id = ?; ";

                    ps = conn.prepareStatement(sql);
                    ps.setString(1, note);
                    ps.setInt(2, idItem);
                    ps.execute();
                }
            }
            commit();

        } catch (Exception e) {
            rollBack();
            System.err.println("Ошибка saveDataWorkModelProject() " + e);
            log.error("Ошибка saveDataWorkModelProject()", e);
            throw new Exception("Ошибка saveDataWorkModelProject() " + e.getMessage(), e);
        } finally {
            setAutoCommit(true);
        }
    }

    public Vector getDataReportProject(int id, boolean flagCx, String textCx) throws Exception {
        Vector elements = new Vector();
        String sql = "";
        try {
            sql = " Select min(rst) as minrst, "
                    + "    max(rst) as maxrst,  "
                    + "    max(rzm) as maxrzm,  "
                    + "    min(rzm) as minrzm, "
                    + "    t2.fas_vid as fas_vid, "
                    + "    t2.fas_pname as fas_pname, "
                    + "    t2.fas as fas, "
                    + "    t2.kolM as kolM, "
                    + "    t2.kol_x as kol_x, "
                    + "    t2.sar as sar, "
                    + "    t2.nar as nar, "
                    + "    t2.cx as cx, "
                    + "    polotno, "
                    + "    color, "
                    + "    idNewFas, "
                    + "    dekor "
                    + " From(Select plan_item.id_plan as idPlan , fas_vid, fas_pname, fas, "
                    + "         sum(round(kol_month,3)) as kolM, kol_x, sar, nar, cx, "
                    + "		(SELECT ARRAY_TO_STRING(ARRAY(SELECT nar_polotno "
                    + "						FROM plan_fas_sostav Where plan_item.fas = plan_fas_sostav.fas and "
                    + "		  				    	                   plan_fas_sostav.id_plan = plan_item.id_plan and "
                    + "									   plan_fas_sostav.sar = plan_item.sar and "
                    + "									   plan_fas_sostav.nar = plan_item.nar "
                    + "						Order by plan_fas_sostav.vid, nar_polotno),', ')) as polotno,	"
                    + "         (SELECT ARRAY_TO_STRING(ARRAY(SELECT ncw "
                    + "                                         FROM plan_fas_color Where plan_item.fas = plan_fas_color.fas and "
                    + "                                                                    plan_fas_color.id_plan = plan_item.id_plan and                "
                    + "                                                                    plan_fas_color.sar = plan_item.sar and                 "
                    + "                                                                    plan_fas_color.nar = plan_item.nar "
                    + "                                         Order by ncw),', ')) as color,"
                    + "		(SELECT distinct id FROM plan_fas_new Where plan_item.fas = plan_fas_new.fas and plan_fas_new.id_plan = plan_item.id_plan) as idNewFas,  "
                    + "		(SELECT ARRAY_TO_STRING(ARRAY(SELECT vid "
                    + "					       FROM plan_fas_dekor Where plan_item.fas = plan_fas_dekor.fas and "
                    + "									 plan_fas_dekor.id_plan = plan_item.id_plan and "
                    + "									 plan_fas_dekor.sar = plan_item.sar and "
                    + "									 plan_fas_dekor.nar = plan_item.nar  "
                    + "								  Order by vid),', ')) as dekor                "
                    + "     From plan, plan_item "
                    + "     Where plan.id = plan_item.id_plan and  "
                    + "           plan.id = ? "
                    + "     Group by idPlan, fas_vid, fas_pname, fas, kol_x, sar, nar, cx ) as t2, plan_item "
                    + " Where t2.idPlan = plan_item.id_plan and "
                    + "       t2.fas_vid = plan_item.fas_vid and "
                    + "       t2.fas_pname = plan_item.fas_pname and "
                    + "       t2.fas = plan_item.fas and "
                    + "       t2.kol_x = plan_item.kol_x and "
                    + "       t2.sar = plan_item.sar and "
                    + "       t2.nar = plan_item.nar and "
                    + "       t2.cx = plan_item.cx "
                    + (flagCx ? " and plan_item.cx::text like '" + textCx + "' " : " ")
                    + " Group by t2.fas_vid, t2.fas_pname, t2.fas, t2.kolM, "
                    + "          t2.kol_x, t2.sar, t2.nar, t2.cx, polotno, color, idNewFas, dekor "
                    + " Order by fas_vid, fas_pname, fas, nar, sar ";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();

            while (rs.next()) {
                Vector tmp = new Vector();

                tmp.add(rs.getInt("fas_vid"));
                tmp.add((rs.getObject("idNewFas") != null) ? "V" : "");
                tmp.add(rs.getString("fas_pname").toUpperCase());
                tmp.add(rs.getInt("fas"));
                tmp.add(rs.getString("nar"));
                tmp.add(rs.getString("minrst"));
                tmp.add(rs.getString("maxrst"));
                tmp.add(rs.getString("minrst").equals(rs.getString("maxrst")) ? rs.getString("minrst") : rs.getString("minrst") + "--" + rs.getString("maxrst"));
                tmp.add(rs.getString("minrzm"));
                tmp.add(rs.getString("maxrzm"));
                tmp.add(rs.getString("minrzm").equals(rs.getString("maxrzm")) ? rs.getString("minrzm") : rs.getString("minrzm") + "--" + rs.getString("maxrzm"));
                tmp.add(rs.getString("polotno"));
                tmp.add(rs.getString("color"));
                tmp.add(Double.valueOf(UtilFunctions.formatNorm(rs.getDouble("kolM"), 0)));
                tmp.add(rs.getInt("kol_x"));
                tmp.add(rs.getString("dekor"));

                elements.add(tmp);
            }

        } catch (Exception e) {
            System.err.println("Ошибка getDataReportProject() " + e);
            log.error("Ошибка getDataReportProject()", e);
            throw new Exception("Ошибка getDataReportProject() " + e.getMessage(), e);
        }
        return elements;
    }

    /**
     * Возвращает планы производства в которых встречается искомое изделие
     * с сумарным количеством выпуска в месяц по шифру артикула, артикулу и номеру модели.
     * @param sar - шифр артикула
     * @param nar - артикул
     * @param fas - модель
     * @return Vector(true / false, шифр артикула, артикул, модель, выпуск ( шт), idPlan, наименование плана, статус, idStatus)
     * @throws Exception
     */
    public Vector getPowerSearch(String sar, String nar, String fas) throws Exception {
        Vector elements = new Vector();
        String sql = "";
        String textNar = "";
        PlanDB db = null;
        try {
            db = new PlanDB();

            sql = "Select distinct plan.id as idPlan, plan_date, plan_name, status, "
                    + "            fas, nar, sar, sum(round(kol_month,3)) as kolM "
                    + "   From plan, plan_item "
                    + "   Where plan.id = plan_item.id_plan "
                    + (sar.trim().equals("") ? " " : " and plan_item.sar::text like '" + sar + "%' ")
                    + (fas.trim().equals("") ? " " : " and plan_item.fas::text like '" + fas + "%' ")
                    + "         and plan.status = 0  "
                    + "    Group by idPlan , fas, nar, sar "
                    + "    Order by idPlan desc, fas, nar, sar  ";

            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                textNar = db.getNar(rs.getInt("fas"), rs.getInt("sar"), nar);

                Vector tmp = new Vector();
                tmp.add(false);
                tmp.add(rs.getInt("sar"));
                tmp.add(textNar);
                tmp.add(rs.getInt("fas"));
                tmp.add(rs.getDouble("kolM"));
                tmp.add(rs.getInt("idPlan"));
                tmp.add(rs.getString("plan_name"));

                switch (rs.getInt("status")) {
                    case 0:
                        tmp.add("главный");
                        break;
                    case 1:
                        tmp.add("проект");
                        break;
                    case 2:
                        tmp.add("копия");
                        break;
                    case -1:
                        tmp.add("удалён");
                        break;
                    default:
                        tmp.add("неизвестно");
                        break;
                }
                tmp.add(rs.getInt("status"));
                tmp.add(rs.getDate("plan_date"));

                if (!nar.trim().equals("")) {
                    if (!textNar.trim().equals(""))
                        elements.add(tmp);
                } else {
                    elements.add(tmp);
                }
            }
        } catch (Exception e) {
            elements = new Vector();
            e.printStackTrace();
            System.err.println("Ошибка getPowerSearch() " + e);
            log.error("Ошибка getPowerSearch()", e);
            throw new Exception("Ошибка getPowerSearch() " + e.getMessage(), e);
        } finally {
            db.disConn();
        }
        return elements;
    }

    public List<PostgresPlanningDocument> getPlanningDocumentsByPeriod(DatePeriod period) {
        String query = "SELECT id, plan_name, plan_date, status FROM plan " +
                "WHERE status = 0 AND plan_date BETWEEN ? AND ?" +
                "ORDER BY plan_date";

        List<PostgresPlanningDocument> result = new ArrayList<>();
        try {
            ps = conn.prepareStatement(query);
            ps.setDate(1, DateUtils.getDateAsSQLDate(period.getBegin()));
            ps.setDate(2, DateUtils.getDateAsSQLDate(period.getEnd()));
            rs = ps.executeQuery();
            while (rs.next()) {
                PostgresPlanningDocument item = new PostgresPlanningDocument();
                item.setId(rs.getInt("id"));
                item.setNumber(rs.getString("plan_name"));
                item.setDate(rs.getDate("plan_date"));
                item.setStatus(rs.getInt("status"));
                result.add(item);
            }
        } catch (Exception ex) {
            System.out.println("Ошибка при выполнении функции getPlanningDocumentsByPeriod :" + ex);
            return null;
        }
        return result;
    }

    public Set<Integer> getArticlesByPlanningDocument(int documentId) {
        String query = "SELECT sar FROM \"VINFO_PLAN_AND_PROJECTPLAN\" " +
                "WHERE id_plan = ? " +
                "GROUP BY sar";

        Set<Integer> result = new HashSet<>();
        try {
            ps = conn.prepareStatement(query);
            ps.setInt(1, documentId);
            rs = ps.executeQuery();
            while (rs.next()) {
                result.add(rs.getInt("sar"));
            }
        } catch (Exception ex) {
            System.out.println("Ошибка при выполнении функции getArticlesByPlanningDocument :" + ex);
            return null;
        }
        return result;
    }

    public List<PostgresPlanningDocumentItem> getPlanningDocumentItemsByDocument(int documentId) {
        String query = "SELECT id_plan_item AS item_id, sar, rzm, rst " +
                "FROM \"VINFO_PLAN_AND_PROJECTPLAN\" " +
                "WHERE id_plan = ?";

        List<PostgresPlanningDocumentItem> result = new ArrayList<>();
        try {
            ps = conn.prepareStatement(query);
            ps.setInt(1, documentId);
            rs = ps.executeQuery();
            while (rs.next()) {
                PostgresPlanningDocumentItem item = new PostgresPlanningDocumentItem();
                item.setId(rs.getInt("item_id"));
                item.setArticleCode(rs.getInt("sar"));
                item.setSize(rs.getInt("rzm"));
                item.setGrowth(rs.getInt("rst"));
                result.add(item);
            }
        } catch (Exception ex) {
            System.out.println("Ошибка при выполнении функции getPlanningDocumentItemsByDocument :" + ex);
            return null;
        }
        return result;
    }

    public List<Integer> getItemsCategoryByDocument(int documentId) {
        String query = "SELECT substring(cast (sar AS text),1,3) AS CATEGORY " +
                "FROM \"VINFO_PLAN_AND_PROJECTPLAN\" WHERE id_plan = ? " +
                "GROUP BY substring(cast (sar as text),1,3) " +
                "ORDER BY substring(cast (sar as text),1,3) ";

        List<Integer> result = new ArrayList<>();
        try {
            ps = conn.prepareStatement(query);
            ps.setInt(1, documentId);
            rs = ps.executeQuery();
            while (rs.next()) {
                result.add(rs.getInt("CATEGORY"));
            }
        } catch (Exception ex) {
            System.out.println("Ошибка при выполнении функции getItemsCategoryByDocument :" + ex);
            return null;
        }
        return result;
    }

}