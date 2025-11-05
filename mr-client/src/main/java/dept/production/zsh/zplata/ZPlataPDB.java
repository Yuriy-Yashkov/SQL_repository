package dept.production.zsh.zplata;

import by.march8.ecs.framework.common.LogCrutch;
import common.UtilFunctions;
import dept.production.zsh.zplata.dto.TechDto;
import workDB.PDB_new;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import java.util.logging.Logger;

/**
 *
 * @author lidashka
 */
public class ZPlataPDB extends PDB_new {

    private static final Logger log = Logger.getLogger(ZPlataPDB.class.getName());

    public List<TechDto> getTable(Date sDate, Date eDate, int departmentId, int brigadeId) {

        List<TechDto> table = new ArrayList<>();

        String sql = "select tech.tech, sum( round( list_item.kolvo * round(work.vnorm::numeric, 4), 2)), emp.fio " +
                "from s_tech tech, spec_item work, list_item, employees emp, list " +
                "where tech.id = work.id_tech" +
                "  and work.id = list_item.id_spec_item" +
                "  and list_item.id_employe = emp.id" +
                "  and list_item.id_list = list.id" +
                "  and date_vvod between ? AND ?" +
                "  and emp.id_dept = ?" +
                "  and id_brig = ?" +
                "  and list.status <> -1 " +
                "group by emp.fio, tech " +
                "order by fio;";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDate(1, sDate);
            ps.setDate(2, eDate);
            ps.setInt(3, departmentId);
            ps.setInt(4, brigadeId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                table.add(TechDto.builder()
                        .employeeFio(rs.getString("fio"))
                        .techName(rs.getString("tech"))
                        .workTime(rs.getBigDecimal("sum"))
                        .build());
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return table;
    }

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
            log.severe("Ошибка getAllDept()" + e);
            throw new Exception("Ошибка getAllDept() " + e.getMessage(), e);
        }
        return dept;
    }

    public Vector getBrig() throws Exception {
        Vector brig = new Vector();
        try {
            ps = conn.prepareStatement("Select s_brig.id as idBrig, kod From s_brig Order by kod");
            rs = ps.executeQuery();
            while (rs.next()) {
                Vector tmp = new Vector();
                tmp.add(rs.getInt("idBrig"));
                tmp.add(rs.getInt("kod"));
                brig.add(tmp);
            }
        } catch (Exception e) {
            System.err.println("Ошибка getBrig() " + e);
            log.severe("Ошибка getBrig()" + e);
            throw new Exception("Ошибка getBrig(): " + e.getMessage(), e);
        }
        return brig;
    }

    public Vector getBrigList(int idDept) throws Exception {
        Vector elements = new Vector();
        String sql = "";

        try {
            sql = "Select distinct s_brig.id as idBrig, kod  "
                    + "  From s_brig, list "
                    + "  Where list.id_brig = s_brig.id "
                    + (idDept != -1 ? " and id_dept = ? " : " ")
                    + "  Order by kod";

            ps = conn.prepareStatement(sql);
            if (idDept != -1)
                ps.setInt(1, idDept);
            rs = ps.executeQuery();

            while (rs.next()) {
                Vector tmp = new Vector();
                tmp.add(rs.getInt("idBrig"));
                tmp.add(rs.getInt("kod"));
                elements.add(tmp);
            }

        } catch (Exception e) {
            elements = new Vector();
            log.severe("Ошибка getBrig()" + e);
        }
        return elements;
    }

    public Vector getBrigadir(int idDept) throws Exception {
        Vector brig = new Vector();
        String sql = "";

        try {
            sql = "Select distinct employees.id as idEmploye, fio "
                    + " From employees, list "
                    + " Where list.id_employer = employees.id "
                    + (idDept != -1 ? " and list.id_dept = ? " : " ")
                    + " Order by fio";

            ps = conn.prepareStatement(sql);
            if (idDept != -1)
                ps.setInt(1, idDept);
            rs = ps.executeQuery();
            while (rs.next()) {
                Vector tmp = new Vector();
                tmp.add(rs.getInt("idEmploye"));
                tmp.add(rs.getString("fio"));
                brig.add(tmp);
            }
        } catch (Exception e) {
            log.severe("Ошибка getBrigadir()" + e);
        }
        return brig;
    }

    public Vector getAllSpec(int model, int dept) throws Exception {
        Vector spec = new Vector();
        String sql = "";

        try {
            sql = "Select spec.id as idspec, spec, date_start, department "
                    + " From spec, dept "
                    + " Where spec.id_dept = dept.id and "
                    + "  id_model = ? "
                    + (dept != -1 ? " and id_dept = ? " : " ")
                    + " Order by date_start desc, spec";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, model);
            if (dept != -1) ps.setInt(2, dept);
            rs = ps.executeQuery();
            while (rs.next()) {
                Vector tmp = new Vector();
                tmp.add(rs.getInt("idspec"));
                tmp.add(rs.getString("spec").trim());
                tmp.add(new SimpleDateFormat("dd-MM-yyyy").format(rs.getDate("date_start")));
                tmp.add(rs.getString("department").trim());
                spec.add(tmp);
            }

            if (model != 0 && dept == 10) {
                sql = "Select spec.id as idspec, spec, date_start, department "
                        + " From spec, dept "
                        + " Where spec.id_dept = dept.id and "
                        + "  id_model = ? "
                        + (dept != -1 ? " and id_dept = ? " : " ")
                        + " Order by date_start desc, spec";

                ps = conn.prepareStatement(sql);
                ps.setInt(1, 0);
                if (dept != -1) ps.setInt(2, dept);
                rs = ps.executeQuery();
                while (rs.next()) {
                    Vector tmp = new Vector();
                    tmp.add(rs.getInt("idspec"));
                    tmp.add(rs.getString("spec").trim());
                    tmp.add(new SimpleDateFormat("dd-MM-yyyy").format(rs.getDate("date_start")));
                    tmp.add(rs.getString("department").trim());
                    spec.add(tmp);
                }
            }

        } catch (Exception e) {
            log.severe("Ошибка getAllSpec()" + e);
        }
        return spec;
    }

    public Vector getElementsSpec(Integer idSpec) throws Exception {
        Vector elements = new Vector();
        String sql = " Select spec_item.id as idspec, num, operac, id_tech, tech, category , enorm, vnorm "
                + "     From spec_item, s_tech "
                + "     Where id_spec = ? and spec_item.id_tech = s_tech.id "
                + "     Order by num ";

        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, idSpec);
            rs = ps.executeQuery();
            while (rs.next()) {
                Vector tmp = new Vector();
                tmp.add(false);
                tmp.add(rs.getInt("idspec"));
                tmp.add(rs.getInt("num"));
                tmp.add(rs.getString("operac").trim());
                tmp.add(rs.getInt("id_tech"));
                tmp.add(rs.getString("tech").trim());
                tmp.add(rs.getInt("category"));
                tmp.add(rs.getInt("enorm"));
                tmp.add(UtilZPlata.formatNorm(rs.getDouble("vnorm"), UtilZPlata.ROUNDING_NORM));
                tmp.add(false);
                elements.add(tmp);
            }

        } catch (Exception e) {
            log.severe("Ошибка getElementsSpec()" + e);
        }
        return elements;
    }

    public void addList(int idDept, int idBrig, int idModel, int idSpec, int idEmploye, int kolvo,
                        String marshrut, long vvodData, String note, int status, Vector elementList) throws Exception {
        String sql = "";

        try {
            setAutoCommit(false);

            sql = "Insert into list(id_model, id_dept, id_brig, id_employer, id_spec, kol, marshlist, date_vvod, note, status) "
                    + " values( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) returning id";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, idModel);
            ps.setInt(2, idDept);
            ps.setInt(3, idBrig);
            ps.setInt(4, idEmploye);
            ps.setInt(5, idSpec);
            ps.setInt(6, kolvo);
            ps.setString(7, marshrut.trim());
            ps.setDate(8, new java.sql.Date(vvodData));
            ps.setString(9, note);
            ps.setInt(10, status);
            rs = ps.executeQuery();

            if (rs.next()) {
                sql = "Insert into list_item(id_list, id_spec_item, id_employe, kolvo, addflag, nvflag) "
                        + "values( ?, ?, ?, ?, ?, ?)";
                for (Iterator it = elementList.iterator(); it.hasNext(); ) {
                    Vector vec = (Vector) it.next();
                    if (vec.get(10) != null && vec.get(13) != null) {
                        ps = conn.prepareStatement(sql);
                        ps.setInt(1, rs.getInt(1));
                        ps.setInt(2, Integer.valueOf(vec.get(1).toString()));
                        ps.setInt(3, Integer.valueOf(vec.get(10).toString()));
                        ps.setInt(4, Integer.valueOf(vec.get(13).toString()));
                        ps.setBoolean(5, Boolean.valueOf(vec.get(9).toString()));
                        ps.setBoolean(6, Boolean.valueOf(vec.get(0).toString()));
                        ps.execute();
                    }
                }
            } else
                throw new Exception();

            commit();
        } catch (Exception e) {
            rollBack();
            log.severe("Ошибка addList()" + e);
        } finally {
            setAutoCommit(true);
        }
    }

    public void updateList(int idList, int idDept, int idBrig, int idModel, int idSpec, int idEmploye, int kolvo,
                           String marshrut, long vvodData, String note, int status, Vector elementList) throws Exception {
        String sql = "";

        try {
            setAutoCommit(false);

            sql = "Delete from list_item where id_list = ?";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, idList);
            ps.execute();

            sql = "Update list set id_model = ?, id_dept = ?, id_brig = ?, id_employer = ?, id_spec = ?, kol = ?, "
                    + "     marshlist = ?, date_vvod = ?, note = ?, status= ?, date_ins = now() "
                    + " Where id = ? ";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, idModel);
            ps.setInt(2, idDept);
            ps.setInt(3, idBrig);
            ps.setInt(4, idEmploye);
            ps.setInt(5, idSpec);
            ps.setInt(6, kolvo);
            ps.setString(7, marshrut.trim());
            ps.setDate(8, new java.sql.Date(vvodData));
            ps.setString(9, note);
            ps.setInt(10, status);
            ps.setInt(11, idList);
            ps.execute();


            sql = "Insert into list_item(id_list, id_spec_item, id_employe, kolvo, addflag, nvflag) "
                    + "values( ?, ?, ?, ?, ?, ?)";
            for (Iterator it = elementList.iterator(); it.hasNext(); ) {
                Vector vec = (Vector) it.next();
                if (vec.get(10) != null && vec.get(13) != null) {
                    ps = conn.prepareStatement(sql);
                    ps.setInt(1, idList);
                    ps.setInt(2, Integer.valueOf(vec.get(1).toString()));
                    ps.setInt(3, Integer.valueOf(vec.get(10).toString()));
                    ps.setInt(4, Integer.valueOf(vec.get(13).toString()));
                    ps.setBoolean(5, Boolean.valueOf(vec.get(9).toString()));
                    ps.setBoolean(6, Boolean.valueOf(vec.get(0).toString()));
                    ps.execute();
                }
            }

            commit();
        } catch (Exception e) {
            rollBack();
            log.severe("Ошибка updateList()" + e);
        } finally {
            setAutoCommit(true);
        }
    }

    public void deleteList(int idList, int idEmployer) throws Exception {
        String sql = " Update list set status = -1, date_ins = now(), id_employer = ? Where id = ?";

        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, idEmployer);
            ps.setInt(2, idList);
            ps.executeUpdate();

        } catch (Exception e) {
            log.severe("Ошибка deleteList()" + e);
        }
    }

    public Vector getAllList(int idDept, int idBrig, int idBrigadir, boolean flagVod, long sVodDate, long eVodDate,
                             boolean flagIns, long sInsDate, long eInsDate, String listNum, String modelNum,
                             String marshrut, String status) throws Exception {

        Vector elements = new Vector();
        String sql = "";

        try {
            sql = "Select department, kodBrig, idList, marshlist, model, kol, sum(list_item.kolvo) as nvsum, fio, date_vvod, date_ins, status "
                    + "   From(Select department, s_brig.kod as kodBrig, list.id as idList, marshlist, model, kol,  fio, date_vvod, date_ins, status "
                    + "         From list, dept, employees, s_model, s_brig "
                    + "         Where list.id_model = s_model.model and "
                    + "                 list.id_employer = employees.id and "
                    + "                 list.id_dept = dept.id and "
                    + "                 list.id_brig = s_brig.id "
                    + (idDept != -1 ? " and dept.id =" + idDept + " " : " ")
                    + (idBrig != -1 ? " and s_brig.id =" + idBrig + " " : " ")
                    + (idBrigadir != -1 ? " and employees.id =" + idBrigadir + " " : " ")
                    + "                 and list.id::text like '" + listNum + "%' "
                    + "                 and s_model.model::text like '" + modelNum + "%' "
                    + "                 and marshlist like '" + marshrut + "%' "
                    + "                 and status::text like '" + status + "%' "
                    + (flagVod ? " and date_vvod between '" + new java.sql.Date(sVodDate) + "' "
                    + "                                              and '" + new java.sql.Date(eVodDate) + "' " : " ")
                    + (flagIns ? " and date_ins between '" + new java.sql.Date(sInsDate) + " 00:00' "
                    + "                                              and '" + new java.sql.Date(eInsDate) + " 23:59' " : " ")
                    + "      ) as t1 left join list_item on t1.idList = list_item.id_list and nvflag = true and id_employe <> -1 "
                    + "   Group by department, kodBrig, idList, marshlist, model, kol,  fio, date_vvod, date_ins, status "
                    + "   Order by department, kodBrig, date_vvod, model, idList  ";

            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                Vector tmp = new Vector();
                tmp.add(false);
                tmp.add(rs.getString("department"));
                tmp.add(rs.getInt("kodBrig"));
                tmp.add(rs.getString("fio"));
                tmp.add(rs.getInt("idList"));
                tmp.add(rs.getString("marshlist"));
                tmp.add(rs.getInt("model"));
                tmp.add(rs.getInt("kol"));
                tmp.add(rs.getInt("nvsum"));
                tmp.add(new SimpleDateFormat("dd-MM-yyyy").format(rs.getDate("date_vvod")));
                tmp.add(new SimpleDateFormat("dd-MM-yyyy").format(rs.getDate("date_ins")));

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
                elements.add(tmp);
            }
        } catch (Exception e) {
            elements = new Vector();
            log.severe("Ошибка getAllList()" + e);
        }
        return elements;
    }

    public Vector getDataList(int idList) throws Exception {
        Vector elements = new Vector();
        String sql = "Select dept.id as idDept, department, id_brig as idBrig, s_model.model as idModel,"
                + "          s_model.naim as naimModel, spec.id as idSpec, spec.spec as naimSpec, marshlist, "
                + "          kol, list.date_vvod as date_vvod, list.date_ins as date_ins, status, note, fio "
                + "   From list, dept, employees, s_model, spec "
                + "   Where list.id_model = s_model.model and "
                + "     	list.id_employer = employees.id and "
                + "            	list.id_dept = dept.id  and "
                + "            	id_spec = spec.id and "
                + "            	list.id = ? ";

        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, idList);
            rs = ps.executeQuery();
            while (rs.next()) {
                elements.add(rs.getInt("idDept"));
                elements.add(rs.getString("department"));
                elements.add(rs.getInt("idBrig"));
                elements.add(rs.getString("fio"));
                elements.add(rs.getInt("idModel"));
                elements.add(rs.getString("naimModel"));
                elements.add(rs.getInt("idSpec"));
                elements.add(rs.getString("naimSpec"));
                elements.add(rs.getString("marshlist"));
                elements.add(rs.getInt("kol"));
                elements.add(new SimpleDateFormat("dd-MM-yyyy").format(rs.getDate("date_vvod")));
                elements.add(new SimpleDateFormat("dd-MM-yyyy").format(rs.getDate("date_ins")));
                elements.add(rs.getString("note"));

                switch (rs.getInt("status")) {
                    case 0:
                        elements.add("формируется");
                        break;
                    case 1:
                        elements.add("закрыт");
                        break;
                    case -1:
                        elements.add("удалён");
                        break;
                    default:
                        elements.add("неизвестно");
                        break;
                }
                elements.add(rs.getInt("status"));
            }
        } catch (Exception e) {
            elements = new Vector();
            log.severe("Ошибка getDataList()" + e);
            throw new Exception("Ошибка getDataList() " + e.getMessage(), e);
        }
        return elements;
    }

    public Vector getDataListItem(int idList) throws Exception {
        Vector elements = new Vector();
        String sql = "Select idListItem, addflag, nvflag, idSpecItem, numSpecItem, operac, idTech, tech, categorySpecItem, enorm, vnorm, idEmploye, numEmploye, fio, kolvo, razryad "
                + "   From(Select idListItem, addflag, nvflag, idSpecItem, numSpecItem, operac, idTech, tech, categorySpecItem, enorm, vnorm, idEmploye, numEmploye, fio, kolvo, date "
                + "	From(Select idListItem, addflag, nvflag, idSpecItem, numSpecItem, operac, idTech, tech, categorySpecItem, enorm, vnorm, idEmploye, employees.num as numEmploye, fio, kolvo "
                + "		From(Select list_item.id as idListItem, addflag, nvflag, idSpecItem, numSpecItem, operac, idTech, tech, categorySpecItem, enorm, vnorm, list_item.id_employe as idEmploye, kolvo "
                + "			From (Select spec_item.id as idSpecItem, spec_item.num as numSpecItem, operac, spec_item.id_tech as idTech, "
                + "				     tech, spec_item.category as categorySpecItem, enorm, vnorm "
                + "					From spec, spec_item, list, s_tech "
                + "					Where 	list.id = ? and "
                + "						list.id_spec = spec.id and "
                + "						spec_item.id_spec = spec.id and "
                + "						spec_item.id_tech = s_tech.id "
                + "			      ) as t1 left join list_item on t1.idSpecItem = list_item.id_spec_item and id_list = ? "
                + "		     )"
                + " as t2 left join employees on t2.idEmploye = employees.id "
                + "		) as t3 left join (Select max(date) as date, id_employe "
                + "					From h_razryad "
                + "					Where date < now() "
                + "					Group by id_employe "
                + "				  ) as t4 on t3.idEmploye = t4.id_employe "
                + "        ) as t5 left join h_razryad on t5.date = h_razryad.date and h_razryad.id_employe = t5.idEmploye "
                + "   Order by numSpecItem, idListItem ";

        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, idList);
            ps.setInt(2, idList);
            rs = ps.executeQuery();
            while (rs.next()) {
                Vector tmp = new Vector();
                tmp.add(rs.getObject("nvflag") == null ? false : rs.getBoolean("nvflag"));
                tmp.add(rs.getInt("idSpecItem"));
                tmp.add(rs.getInt("numSpecItem"));
                tmp.add(rs.getString("operac"));
                tmp.add(rs.getInt("idTech"));
                tmp.add(rs.getString("tech"));
                tmp.add(rs.getInt("categorySpecItem"));
                tmp.add(rs.getInt("enorm"));
                tmp.add(UtilZPlata.formatNorm(rs.getDouble("vnorm"), UtilZPlata.ROUNDING_NORM));
                tmp.add(rs.getObject("addflag") == null ? false : rs.getBoolean("addflag"));
                tmp.add(rs.getObject("idEmploye"));
                tmp.add(rs.getObject("numEmploye"));
                tmp.add(rs.getObject("fio"));
                tmp.add(rs.getObject("kolvo"));
                tmp.add(rs.getObject("kolvo") != null ? UtilZPlata.formatNorm(Double.valueOf(rs.getDouble("kolvo") * Double.valueOf(UtilZPlata.formatNorm(rs.getDouble("vnorm"), UtilZPlata.ROUNDING_NORM))), 2) : null);
                tmp.add(rs.getObject("razryad"));
                elements.add(tmp);
            }
        } catch (Exception e) {
            elements = new Vector();
            log.severe("Ошибка getDataListItem()" + e);
        }
        return elements;
    }

    public int getAllKolModel(int idBrig, int idDept, long sVodDate, long eVodDate) throws Exception {
        int sum = 0;
        String sql = "";

        try {
            sql = "Select sum(list.kol) as sum "
                    + " From list, s_brig "
                    + " Where list.status <> -1 and"
                    + "       list.id_brig = s_brig.id    "
                    + (idBrig != -1 ? " and s_brig.id = " + idBrig + " " : " ")
                    + (idDept != -1 ? " and list.id_dept = " + idDept + " " : " ")
                    + "       and date_vvod between '" + new java.sql.Date(sVodDate) + "'"
                    + "                        and '" + new java.sql.Date(eVodDate) + "' ";

            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next())
                sum = rs.getInt("sum");

        } catch (Exception e) {
            sum = 0;
            log.severe("Ошибка getAllKolModel()" + e);
        }
        return sum;
    }

    public Vector getSumTotalByModel(int idBrig, int idDept, int fas, long sVodDate, long eVodDate) throws Exception {
        Vector elements = new Vector();
        String sql = "";

        try {
            sql = "Select "
                    + "     id_model as model, "
                    + "     s_brig.kod as brig, "
                    + "     sum(list_item.kolvo) as nvsum "
                    + " From "
                    + "     list, "
                    + "     list_item, "
                    + "     s_brig "
                    + " Where "
                    + "     list.id = list_item.id_list and "
                    + "     list.status <> -1 and "
                    + "     list.id_brig = s_brig.id  "
                    + (fas != -1 ? " and id_model = " + fas + " " : " ")
                    + (idBrig != -1 ? "  and s_brig.id = " + idBrig + " " : " ")
                    + (idDept != -1 ? " and list.id_dept = " + idDept + " " : " ")
                    + "     and  date_vvod between '" + new java.sql.Date(sVodDate) + "' "
                    + "                       and '" + new java.sql.Date(eVodDate) + "' "
                    + "     and nvflag = true "
                    + " Group by s_brig.kod, id_model "
                    + " Order by id_model, s_brig.kod ";

            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                Vector tmp = new Vector();
                tmp.add(rs.getInt("model"));
                tmp.add(rs.getInt("brig"));
                tmp.add(rs.getInt("nvsum"));
                elements.add(tmp);
            }

        } catch (Exception e) {
            elements = new Vector();
            log.severe("Ошибка getSumTotalByModel()" + e);
        }
        return elements;
    }

    public Vector getSumDefectByModel(int idBrig, int idDept, int fas, long sVodDate, long eVodDate) throws Exception {
        Vector elements = new Vector();
        String sql = "";

        try {
            sql = "Select "
                    + "     id_model as model, "
                    + "     s_brig.kod as brig, "
                    + "     sum(list_item.kolvo) as nvsum "
                    + " From "
                    + "     list, "
                    + "     list_item, "
                    + "     s_brig "
                    + " Where "
                    + "     list.id = list_item.id_list and "
                    + "     list.status <> -1 and "
                    + "     list.id_brig = s_brig.id  "
                    + (fas != -1 ? " and id_model = " + fas + " " : " ")
                    + (idBrig != -1 ? "  and s_brig.id = " + idBrig + " " : " ")
                    + (idDept != -1 ? " and list.id_dept = " + idDept + " " : " ")
                    + "     and date_vvod between '" + new java.sql.Date(sVodDate) + "' "
                    + "                       and '" + new java.sql.Date(eVodDate) + "' "
                    + "     and list_item.id_employe = -1 "
                    + " Group by s_brig.kod, id_model "
                    + " Order by id_model, s_brig.kod ";

            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                Vector tmp = new Vector();
                tmp.add(rs.getInt("model"));
                tmp.add(rs.getInt("brig"));
                tmp.add(rs.getInt("nvsum"));
                elements.add(tmp);
            }

        } catch (Exception e) {
            elements = new Vector();
            log.severe("Ошибка getSumDefectByModel()" + e);
        }
        return elements;
    }


    public Vector getProductionBrigOtchet(int idBrig, int idDept, long sVodDate, long eVodDate) throws Exception {
        Vector elements = new Vector();
        String sql = "";

        try {

            sql = "Select coalesce(t3.idEmploye, t4.idEmploye) as idEmploye, coalesce(t3.numEmploye, t4.numEmploye) as numEmploye, "
                    + "     coalesce(t3.fio, t4.fio) as fio, coalesce(category, t3.razryad) as razryad, summ, summ_nes, summ_dk "
                    + " From (Select coalesce(t2.idDept, t1.idDept) as idDept, coalesce(t2.idEmploye, t1.idEmploye) as idEmploye, "
                    + "             coalesce(t2.numEmploye, t1.numEmploye) as numEmploye, coalesce(t2.fio, t1.fio) as fio, "
                    + "             coalesce(category, razryad) as razryad, summ, summ_nes"
                    + "        From (Select list_nesort.id_dept as idDept, list_nesort.id_brig as idBrig, employees.id as idEmploye, employees.num as numEmploye, "
                    + "			employees.fio as fio, list_nesort.razryad as razryad, sum(round(list_nesort.kol*" + UtilZPlata.NESORT_KOF + ", 2)) as summ_nes 	"
                    + "             From employees, list_nesort, s_brig "
                    + "             Where   list_nesort.id_employer = employees.id "
                    + "			and employees.id <> -1 	"
                    + "			and list_nesort.id_brig = s_brig.id  "
                    + (idBrig != -1 ? " and s_brig.id = " + idBrig + " " : " ")
                    + (idDept != -1 ? " and list_nesort.id_dept = " + idDept + " " : " ")
                    + "                 and date_vvod between '" + new java.sql.Date(sVodDate) + "' "
                    + "                                   and '" + new java.sql.Date(eVodDate) + "' "
                    + "        Group by idDept, idBrig, idEmploye, numEmploye, fio, razryad) as t1  "
                    + "	full join (Select list.id_dept as idDept, list.id_brig as idBrig, employees.id as idEmploye, employees.num as numEmploye, "
                    + "			employees.fio as fio,spec_item.category as category, sum(round(list_item.kolvo*round(spec_item.vnorm::numeric," + UtilZPlata.ROUNDING_NORM + "), 2)) as summ "
                    + "		    From employees, list, list_item, spec, spec_item, s_brig "
                    + "		    Where   list_item.id_employe = employees.id "
                    + "			and list.id = list_item.id_list "
                    + "			and list.id_spec = spec.id  "
                    + "			and spec.id = spec_item.id_spec	"
                    + "			and spec_item.id = list_item.id_spec_item "
                    + "			and list.status <> -1 	"
                    + "			and employees.id <> -1 	"
                    + "			and list.id_brig = s_brig.id  "
                    + (idBrig != -1 ? " and list.id_brig = " + idBrig + " " : " ")
                    + (idDept != -1 ? " and list.id_dept = " + idDept + " " : " ")
                    + (idDept == 8 ? " and spec_item.num > -1 " : " ")
                    + "                 and date_vvod between '" + new java.sql.Date(sVodDate) + "' "
                    + "                                   and '" + new java.sql.Date(eVodDate) + "' "
                    + "		Group by idDept, idBrig, idEmploye, numEmploye, fio, category) as t2 "
                    + "	on t1.idDept = t2.idDept "
                    + "		and t1.idBrig = t2.idBrig "
                    + "		and t1.idEmploye = t2.idEmploye "
                    + "		and t1.razryad = t2.category  ) as t3 "
                    + " full join (Select list.id_dept as idDept, list.id_brig as idBrig, employees.id as idEmploye, employees.num as numEmploye, "
                    + "			employees.fio as fio,spec_item.category as category, sum(round(list_item.kolvo*round(spec_item.vnorm::numeric," + UtilZPlata.ROUNDING_NORM + "), 2)) as summ_dk "
                    + "		    From employees, list, list_item, spec, spec_item, s_brig "
                    + "		    Where   list_item.id_employe = employees.id "
                    + "			and list.id = list_item.id_list "
                    + "			and list.id_spec = spec.id  "
                    + "			and spec.id = spec_item.id_spec	"
                    + "			and spec_item.id = list_item.id_spec_item "
                    + "			and list.status <> -1 	"
                    + "			and employees.id <> -1 	"
                    + "			and list.id_brig = s_brig.id  "
                    + (idBrig != -1 ? " and list.id_brig <> " + idBrig + " " : " ")
                    + "			and employees.id in ( Select distinct id_employe "
                    + "						From list, list_item  "
                    + "						Where list.id = list_item.id_list "
                    + "                                               and list.status <> -1 "
                    + "                                               and list_item.id_employe <> -1 "
                    + (idBrig != -1 ? " and list.id_brig = " + idBrig + " " : " ")
                    + (idDept != -1 ? " and list.id_dept = " + idDept + " " : " ")
                    + "                                               and date_vvod between '" + new java.sql.Date(sVodDate) + "' "
                    + "                                                                  and '" + new java.sql.Date(eVodDate) + "'  )"
                    + (idDept != -1 ? " and list.id_dept = " + idDept + " " : " ")
                    + "                  and date_vvod between '" + new java.sql.Date(sVodDate) + "' "
                    + "                                    and '" + new java.sql.Date(eVodDate) + "' "
                    + "		Group by idDept, idBrig, idEmploye, numEmploye, fio, category ) as t4 "
                    + " on t3.idDept = t4.idDept "
                    + "		and t3.idEmploye = t4.idEmploye "
                    + "		and t3.razryad = t4.category "
                    + " Order by fio, numEmploye, razryad ";

            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                Vector tmp = new Vector();
                tmp.add(rs.getInt("numEmploye"));
                tmp.add(rs.getString("fio").trim());
                tmp.add(rs.getInt("razryad"));
                tmp.add(rs.getDouble("summ"));
                tmp.add(rs.getDouble("summ_nes")); //несортн.
                tmp.add(rs.getDouble("summ_dk"));  // другие конвеера
                elements.add(tmp);
            }

        } catch (Exception e) {
            elements = new Vector();
            log.severe("Ошибка getProductionBrigOtchet()" + e);
        }
        return elements;
    }

    public Vector getProductionBrigOtchetNEW(int idBrig, int idDept, long sVodDate, long eVodDate) throws Exception {
        Vector elements = new Vector();
        String sql = "";

        try {
            sql = "   SELECT "
                    + "     (Select s_brig.kod From h_brig, s_brig "
                    + "        Where h_brig.id_employe = idEmploye and  h_brig.id_brig = s_brig.id and  date <= '" + new java.sql.Date(eVodDate) + "' "
                    + "        Order by date desc limit 1"
                    + "     ) as id_brig, "
                    + "     (Select razryad From h_razryad "
                    + "        Where h_razryad.id_employe = idEmploye and date <= '" + new java.sql.Date(eVodDate) + "' "
                    + "        Order by date desc limit 1"
                    + "     ) as raz,	"
                    + "     idEmploye, "
                    + "     num, "
                    + "     fio, "
                    + "     profession, "
                    + "     razryad, "
                    + "     summ, "
                    + "     summ_nes, "
                    + "     summ_dk  "
                    + " FROM "
                    + "     (Select coalesce(t3.idEmploye, t4.idEmploye) as idEmploye, coalesce(category, t3.razryad) as razryad, summ, summ_nes, summ_dk      "
                    + "	From (Select coalesce(t2.idDept, t1.idDept) as idDept, coalesce(t2.idEmploye, t1.idEmploye) as idEmploye,      "
                    + "		     coalesce(category, razryad) as razryad, summ, summ_nes             "
                    + "		From (Select list_nesort.id_dept as idDept, list_nesort.id_brig as idBrig, list_nesort.id_employer as idEmploye, list_nesort.razryad as razryad, "
                    + "				sum(round(list_nesort.kol*" + UtilZPlata.NESORT_KOF + ", 2)) as summ_nes 	                     "
                    + "			From list_nesort, s_brig                      "
                    + "			Where   list_nesort.id_employer <> -1 	                         "
                    + "				and list_nesort.id_brig = s_brig.id   "
                    + (idBrig != -1 ? " and s_brig.id = " + idBrig + " " : " ")
                    + (idDept != -1 ? " and list_nesort.id_dept = " + idDept + " " : " ")
                    + "                                and date_vvod between '" + new java.sql.Date(sVodDate) + "' "
                    + "                                                  and '" + new java.sql.Date(eVodDate) + "' "
                    + "			Group by idDept, idBrig, idEmploye, razryad) as t1           "
                    + "		full join (Select list.id_dept as idDept, list.id_brig as idBrig, list_item.id_employe as idEmploye, spec_item.category as category, "
                    + "					 sum(round(list_item.kolvo*round(spec_item.vnorm::numeric," + UtilZPlata.ROUNDING_NORM + "), 2)) as summ    "
                    + "	                   From  list, list_item, spec, spec_item, s_brig                      "
                    + "	                   Where   list.id = list_item.id_list                          "
                    + "				and list.id_spec = spec.id                           "
                    + "				and spec.id = spec_item.id_spec	                         "
                    + "				and spec_item.id = list_item.id_spec_item                          "
                    + "				and list.status <> -1 	                         "
                    + "				and list_item.id_employe <> -1 	                         "
                    + "				and list.id_brig = s_brig.id   "
                    + (idBrig != -1 ? " and list.id_brig = " + idBrig + " " : " ")
                    + (idDept != -1 ? " and list.id_dept = " + idDept + " " : " ")
                    + (idDept == 8 ? " and spec_item.num > -1 " : " ")
                    + "                                and date_vvod between '" + new java.sql.Date(sVodDate) + "' "
                    + "                                                 and '" + new java.sql.Date(eVodDate) + "' "
                    + "			   Group by idDept, idBrig, idEmploye, category ) as t2          "
                    + "		on t1.idDept = t2.idDept              "
                    + "			and t1.idBrig = t2.idBrig              "
                    + "			and t1.idEmploye = t2.idEmploye              "
                    + "			and t1.razryad = t2.category  ) as t3      "
                    + "	full join (Select list.id_dept as idDept, list_item.id_employe as idEmploye, spec_item.category as category, "
                    + "			sum(round(list_item.kolvo*round(spec_item.vnorm::numeric," + UtilZPlata.ROUNDING_NORM + "), 2)) as summ_dk                  "
                    + "		    From  list, list_item, spec, spec_item, s_brig                  "
                    + "		    Where   list.id = list_item.id_list                  	"
                    + "			and list.id_spec = spec.id                           "
                    + "			and spec.id = spec_item.id_spec	                         "
                    + "			and spec_item.id = list_item.id_spec_item                          "
                    + "			and list.status <> -1 	                         "
                    + "			and list_item.id_employe <> -1 	                         "
                    + "			and list.id_brig = s_brig.id   "
                    + (idBrig != -1 ? " and list.id_brig <> " + idBrig + " " : " ")
                    + "                         and list_item.id_employe in ( Select distinct id_employe "
                    + "                                        From h_brig, s_brig "
                    + "                                        Where s_brig.id = h_brig.id_brig         "
                    + (idBrig != -1 ? " and id_brig = " + idBrig + " " : " ")
                    + "                                        Group by id_employe "
                    + "                                        Having max(date) <= '" + new java.sql.Date(eVodDate) + "'   ) "
                    + (idDept != -1 ? " and list.id_dept = " + idDept + " " : " ")
                    + (idDept == 8 ? " and spec_item.num > -1 " : " ")
                    + "                         and date_vvod between '" + new java.sql.Date(sVodDate) + "' "
                    + "                                           and '" + new java.sql.Date(eVodDate) + "' "
                    + "		   Group by idDept, idEmploye, category ) as t4    "
                    + "	on t3.idDept = t4.idDept      "
                    + "		and t3.idEmploye = t4.idEmploye 	    "
                    + "		and t3.razryad = t4.category"
                    + "        ) as t5, "
                    + "        employees, "
                    + "        employees_pro "
                    + " WHERE "
                    + "        idEmploye = employees.id and "
                    + "        employees.id_pro = employees_pro.id "
                    + " ORDER BY "
                    + "        id_brig, "
                    + "        fio, "
                    + "        num, "
                    + "        razryad ";


                     /*

                     + "                     Select (Select s_brig.kod "
                     + "        From h_brig, s_brig "
                     + "        Where h_brig.id_employe = idEmploye and "
                     + "            h_brig.id_brig = s_brig.id and "
                     + "            date <= '"+new java.sql.Date(eVodDate)+"' "
                     + "        ProdcutionOrder by date desc limit 1) as id_brig,"
                     + " idEmploye, numEmploye, fio, razryad, summ, summ_nes, summ_dk "
                    + " From(Select coalesce(t3.idEmploye, t4.idEmploye) as idEmploye, coalesce(t3.numEmploye, t4.numEmploye) as numEmploye, "
                    + "         coalesce(t3.fio, t4.fio) as fio, coalesce(category, t3.razryad) as razryad, summ, summ_nes, summ_dk "
                    + "     From (Select coalesce(t2.idDept, t1.idDept) as idDept, coalesce(t2.idEmploye, t1.idEmploye) as idEmploye, "
                    + "                 coalesce(t2.numEmploye, t1.numEmploye) as numEmploye, coalesce(t2.fio, t1.fio) as fio, "
                    + "                 coalesce(category, razryad) as razryad, summ, summ_nes"
                    + "             From (Select list_nesort.id_dept as idDept, list_nesort.id_brig as idBrig, employees.id as idEmploye, employees.num as numEmploye, "
                    + "                 	employees.fio as fio, list_nesort.razryad as razryad, sum(round(list_nesort.kol*"+UtilZPlata.NESORT_KOF+", 2)) as summ_nes 	"
                    + "                     From employees, list_nesort, s_brig "
                    + "                     Where   list_nesort.id_employer = employees.id "
                    + "                         and employees.id <> -1 	"
                    + "                         and list_nesort.id_brig = s_brig.id  "
                    +                           (idBrig != -1 ? " and s_brig.id = "+idBrig+" " : " ")
                    +                           (idDept != -1  ? " and list_nesort.id_dept = "+idDept+" " : " ")
                    + "                         and date_vvod between '"+new java.sql.Date(sVodDate)+"' "
                    + "                                           and '"+new java.sql.Date(eVodDate)+"' "
                    + "             Group by idDept, idBrig, idEmploye, numEmploye, fio, razryad) as t1  "
                    + "         full join (Select list.id_dept as idDept, list.id_brig as idBrig, employees.id as idEmploye, employees.num as numEmploye, "
                    + "                 	employees.fio as fio,spec_item.category as category, "
                     + "                        sum(round(list_item.kolvo*round(spec_item.vnorm::numeric,"+UtilZPlata.ROUNDING_NORM+"), 2)) as summ "
                    + "                     From employees, list, list_item, spec, spec_item, s_brig "
                    + "                     Where   list_item.id_employe = employees.id "
                    + "                         and list.id = list_item.id_list "
                    + "                         and list.id_spec = spec.id  "
                    + "                         and spec.id = spec_item.id_spec	"
                    + "                         and spec_item.id = list_item.id_spec_item "
                    + "                         and list.status <> -1 	"
                    + "                         and employees.id <> -1 	"
                    + "                         and list.id_brig = s_brig.id  "
                    +                           (idBrig != -1 ? " and list.id_brig = "+idBrig+" " : " ")
                    +                           (idDept != -1 ? " and list.id_dept = "+idDept+" " : " ")
                    +                           (idDept == 8 ?  " and spec_item.num > -1 " : " ")
                    + "                        and date_vvod between '"+new java.sql.Date(sVodDate)+"' "
                    + "                                          and '"+new java.sql.Date(eVodDate)+"' "
                    + "                 Group by idDept, idBrig, idEmploye, numEmploye, fio, category) as t2 "
                    + "         on t1.idDept = t2.idDept "
                    + "             and t1.idBrig = t2.idBrig "
                    + "             and t1.idEmploye = t2.idEmploye "
                    + "             and t1.razryad = t2.category  ) as t3 "
                    + "     full join (Select list.id_dept as idDept, employees.id as idEmploye, employees.num as numEmploye, "
                    + "                 	employees.fio as fio,spec_item.category as category, "
                     + "                        sum(round(list_item.kolvo*round(spec_item.vnorm::numeric,"+UtilZPlata.ROUNDING_NORM+"), 2)) as summ_dk "
                    + "                 From employees, list, list_item, spec, spec_item, s_brig "
                    + "                 Where   list_item.id_employe = employees.id "
                    + "                 	and list.id = list_item.id_list "
                    + "                 	and list.id_spec = spec.id  "
                    + "                         and spec.id = spec_item.id_spec	"
                    + "                         and spec_item.id = list_item.id_spec_item "
                    + "                         and list.status <> -1 	"
                    + "                         and employees.id <> -1 	"
                    + "                         and list.id_brig = s_brig.id  "
                    +                          (idBrig != -1 ? " and list.id_brig <> "+idBrig+" " : " ")
                    + "                         and employees.id in ( Select distinct id_employe "
                    + "                                        From h_brig, s_brig "
                    + "                                        Where s_brig.id = h_brig.id_brig         "
                    +                                          (idBrig != -1 ? " and id_brig = "+idBrig+" " : " ")
                    + "                                        Group by id_employe "
                    + "                                        Having max(date) <= '"+new java.sql.Date(eVodDate)+"'   )"
                    +                          (idDept != -1  ? " and list.id_dept = "+idDept+" " : " ")
                    + "                         and date_vvod between '"+new java.sql.Date(sVodDate)+"' "
                    + "                                           and '"+new java.sql.Date(eVodDate)+"' "
                    + "             Group by idDept, idEmploye, numEmploye, fio, category ) as t4 "
                    + "   on t3.idDept = t4.idDept "
                    + "     and t3.idEmploye = t4.idEmploye "
                    + "	    and t3.razryad = t4.category) as t5 "
                    + " ProdcutionOrder by id_brig, fio, numEmploye, razryad ";
                    */

            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                Vector tmp = new Vector();
                tmp.add(rs.getInt("num"));
                tmp.add(rs.getString("fio").trim());
                tmp.add(idDept == 8 ? "швея" : "");
                tmp.add(rs.getInt("razryad"));
                tmp.add(rs.getDouble("summ"));
                tmp.add(rs.getDouble("summ_dk"));  // другие конвеера
                tmp.add(rs.getDouble("summ_nes")); //несортн.
                tmp.add(rs.getInt("id_brig"));
                tmp.add("\n" + rs.getString("profession").trim() + ", " + rs.getInt("raz") + "р.");
                elements.add(tmp);
            }

        } catch (Exception e) {
            elements = new Vector();
            log.severe("Ошибка getProductionBrigOtchetNEW()" + e);
        }
        return elements;
    }

    public Vector getProductionVtoOtchet(int idBrig, int idDept, long sVodDate, long eVodDate) throws Exception {
        Vector elements = new Vector();
        String sql = "";

        try {
            sql = " Select (Select s_brig.kod From h_brig, s_brig "
                    + "        Where h_brig.id_employe = idEmploye and  h_brig.id_brig = s_brig.id and  date <= '" + new java.sql.Date(eVodDate) + "' "
                    + "        Order by date desc limit 1) as id_brig, "
                    + "    (Select razryad From h_razryad "
                    + "        Where h_razryad.id_employe = idEmploye and date <= '" + new java.sql.Date(eVodDate) + "' "
                    + "        Order by date desc limit 1) as raz,	"
                    + "     idEmploye, num, fio, profession, "
                    + "     razryad, summ, summ_nes, summ_dk  "
                    + " From(Select coalesce(t3.idEmploye, t4.idEmploye) as idEmploye, coalesce(category, t3.razryad) as razryad, summ, summ_nes, summ_dk      "
                    + "	From (Select coalesce(t2.idDept, t1.idDept) as idDept, coalesce(t2.idEmploye, t1.idEmploye) as idEmploye,      "
                    + "		     coalesce(category, razryad) as razryad, summ, summ_nes             "
                    + "		From (Select list_nesort.id_dept as idDept, list_nesort.id_brig as idBrig, list_nesort.id_employer as idEmploye, list_nesort.razryad as razryad, "
                    + "				sum(round(list_nesort.kol*" + UtilZPlata.NESORT_KOF + ", 2)) as summ_nes 	                     "
                    + "			From list_nesort, s_brig                      "
                    + "			Where   list_nesort.id_employer <> -1 	                         "
                    + "				and list_nesort.id_brig = s_brig.id   "
                    + (idBrig != -1 ? " and s_brig.id = " + idBrig + " " : " ")
                    + (idDept != -1 ? " and list_nesort.id_dept = " + idDept + " " : " ")
                    + "                                and date_vvod between '" + new java.sql.Date(sVodDate) + "' "
                    + "                                                  and '" + new java.sql.Date(eVodDate) + "' "
                    + "			Group by idDept, idBrig, idEmploye, razryad) as t1           "
                    + "		full join (Select list.id_dept as idDept, list.id_brig as idBrig, list_item.id_employe as idEmploye, spec_item.category as category, "
                    + "					 sum(round(list_item.kolvo*round(spec_item.vnorm::numeric," + UtilZPlata.ROUNDING_NORM + "), 2)) as summ    "
                    + "	                   From  list, list_item, spec, spec_item, s_brig                      "
                    + "	                   Where   list.id = list_item.id_list                          "
                    + "				and list.id_spec = spec.id                           "
                    + "				and spec.id = spec_item.id_spec	                         "
                    + "				and spec_item.id = list_item.id_spec_item                          "
                    + "				and list.status <> -1 	                         "
                    + "				and list_item.id_employe <> -1 	                         "
                    + "				and list.id_brig = s_brig.id   "
                    + (idBrig != -1 ? " and list.id_brig = " + idBrig + " " : " ")
                    + (idDept != -1 ? " and list.id_dept = " + idDept + " " : " ")
                    + (idDept == 8 ? " and spec_item.num < 0 " : " ")
                    + "                                and date_vvod between '" + new java.sql.Date(sVodDate) + "' "
                    + "                                                 and '" + new java.sql.Date(eVodDate) + "' "
                    + "			   Group by idDept, idBrig, idEmploye, category ) as t2          "
                    + "		on t1.idDept = t2.idDept              "
                    + "			and t1.idBrig = t2.idBrig              "
                    + "			and t1.idEmploye = t2.idEmploye              "
                    + "			and t1.razryad = t2.category  ) as t3      "
                    + "	full join (Select list.id_dept as idDept, list_item.id_employe as idEmploye, spec_item.category as category, "
                    + "			sum(round(list_item.kolvo*round(spec_item.vnorm::numeric," + UtilZPlata.ROUNDING_NORM + "), 2)) as summ_dk                  "
                    + "		    From  list, list_item, spec, spec_item, s_brig                  "
                    + "		    Where   list.id = list_item.id_list                  	"
                    + "			and list.id_spec = spec.id                           "
                    + "			and spec.id = spec_item.id_spec	                         "
                    + "			and spec_item.id = list_item.id_spec_item                          "
                    + "			and list.status <> -1 	                         "
                    + "			and list_item.id_employe <> -1 	                         "
                    + "			and list.id_brig = s_brig.id   "
                    + (idBrig != -1 ? " and list.id_brig <> " + idBrig + " " : " ")
                    + "                         and list_item.id_employe in ( Select distinct id_employe "
                    + "                                        From h_brig, s_brig "
                    + "                                        Where s_brig.id = h_brig.id_brig         "
                    + (idBrig != -1 ? " and id_brig = " + idBrig + " " : " ")
                    + "                                        Group by id_employe "
                    + "                                        Having max(date) <= '" + new java.sql.Date(eVodDate) + "'   ) "
                    + (idDept != -1 ? " and list.id_dept = " + idDept + " " : " ")
                    + (idDept == 8 ? " and spec_item.num < 0 " : " ")
                    + "                         and date_vvod between '" + new java.sql.Date(sVodDate) + "' "
                    + "                                           and '" + new java.sql.Date(eVodDate) + "' "
                    + "		   Group by idDept, idEmploye, category ) as t4    "
                    + "	on t3.idDept = t4.idDept      "
                    + "		and t3.idEmploye = t4.idEmploye 	    "
                    + "		and t3.razryad = t4.category) as t5, employees, employees_pro "
                    + "	Where idEmploye = employees.id and "
                    + "	      employees.id_pro = employees_pro.id "
                    + " Order by id_brig, fio, num, razryad ";

                /*    "Select employees.id as idEmploye, employees.num as numEmploye, employees.fio as fio, spec_item.category as razryad, "
                + "           sum(round(list_item.kolvo*round(spec_item.vnorm::numeric,"+UtilZPlata.ROUNDING_NORM+"), 2)) as summ "
                + " From employees, list, list_item, spec, spec_item, s_brig "
                + " Where list_item.id_employe = employees.id "
                + "	and list.id = list_item.id_list "
                + "	and list.id_spec = spec.id  "
                + "	and spec.id = spec_item.id_spec	"
                + "	and spec_item.id = list_item.id_spec_item "
                + "	and list.status <> -1 "
                + "	and employees.id <> -1 "
                + "     and spec_item.num < 0 "
                + "	and list.id_brig = s_brig.id "
                +       (idBrig != -1 ? " and s_brig.id = "+idBrig+" " : " ")
                +       (idDept != -1 ? " and list.id_dept = "+idDept+" " : " ")
                + "     and date_vvod between '"+new java.sql.Date(sVodDate)+"' "
                + "                       and '"+new java.sql.Date(eVodDate)+"' "
                + " Group by idEmploye, numEmploye, fio, category "
                + " ProdcutionOrder by fio, numEmploye, razryad ";
                */

            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                Vector tmp = new Vector();
                tmp.add(rs.getInt("num"));
                tmp.add(rs.getString("fio").trim());
                tmp.add("термоотделочник");
                tmp.add(rs.getInt("razryad"));
                tmp.add(rs.getDouble("summ"));
                tmp.add(rs.getDouble("summ_dk"));  // другие конвеера
                tmp.add(rs.getDouble("summ_nes")); //несортн.
                tmp.add(rs.getInt("id_brig"));
                tmp.add("\n" + rs.getString("profession").trim() + ", " + rs.getInt("raz") + "р.");
                elements.add(tmp);
            }

        } catch (Exception e) {
            elements = new Vector();
            log.severe("Ошибка getProductionVtoOtchet()" + e);
        }
        return elements;
    }

    public double getProductionBrakBrigOtchet(int idBrig, int idDept, long sVodDate, long eVodDate) throws Exception {
        double brak = 0;
        String sql = "";

        try {
            sql = "Select employees.id, sum(round(list_item.kolvo*round(spec_item.vnorm::numeric," + UtilZPlata.ROUNDING_NORM + "), 2)) as summ"
                    + " From employees, list, list_item, spec, spec_item, s_brig "
                    + " Where   list_item.id_employe = employees.id "
                    + "	and list.id = list_item.id_list "
                    + "	and list.id_spec = spec.id  "
                    + "	and spec.id = spec_item.id_spec "
                    + "	and spec_item.id = list_item.id_spec_item "
                    + "     and list.status <> -1 "
                    + "	and list.id_brig = s_brig.id  "
                    + "     and employees.id = -1 "
                    + (idBrig != -1 ? " and s_brig.id = " + idBrig + " " : " ")
                    + (idDept != -1 ? " and list.id_dept = " + idDept + " " : " ")
                    + "     and date_vvod between '" + new java.sql.Date(sVodDate) + "' "
                    + "                       and '" + new java.sql.Date(eVodDate) + "' "
                    + " Group by employees.id ";

            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next())
                brak = rs.getDouble("summ");

        } catch (Exception e) {
            brak = 0;
            log.severe("Ошибка getProductionBrakBrigOtchet()" + e);
        }
        return brak;
    }

    public Vector getProductionOtchetNV(int idBrig, int idDept, long sVodDate, long eVodDate, String type) throws Exception {
        Vector elements = new Vector();
        String sql = "";

        try {
            sql = "Select model, sum(list_item.kolvo) as nvsum, list.date_vvod as date_vvod, enorm, vnorm, operac "
                    + " From list, list_item, spec, spec_item, dept, s_model, s_brig "
                    + " Where list.id = list_item.id_list and "
                    + "       list.id_spec = spec.id and "
                    + "       list_item.id_spec_item = spec_item.id and "
                    + "       spec.id = spec_item.id_spec and "
                    + "       list.id_model = s_model.model and "
                    + "       list.id_dept = dept.id and "
                    + "       list.id_brig = s_brig.id and "
                    + "       list.status <> -1 and "
                    + "       date_vvod between '" + new java.sql.Date(sVodDate) + "' "
                    + "                     and '" + new java.sql.Date(eVodDate) + "' "
                    + (idBrig != -1 ? " and s_brig.id = " + idBrig + " " : " ")
                    + (idDept != -1 ? " and list.id_dept = " + idDept + " " : " ")
                    + (type.equals("1") ? " and list_item.nvflag = true "
                    + " and id_employe <> -1 " : " ")
                    + " Group by model, date_vvod, enorm, vnorm, operac "
                    + " Order by model, enorm, vnorm, date_vvod ";

            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                Vector tmp = new Vector();
                tmp.add(new SimpleDateFormat("dd-MM-yyyy").format(rs.getDate("date_vvod")));
                tmp.add(rs.getInt("model"));
                tmp.add(rs.getInt("nvsum"));
                tmp.add(rs.getInt("enorm"));
                tmp.add(UtilZPlata.formatNorm(rs.getDouble("vnorm"), UtilZPlata.ROUNDING_NORM));
                tmp.add(rs.getString("operac"));
                elements.add(tmp);
            }

        } catch (Exception e) {
            elements = new Vector();
            log.severe("Ошибка getProductionOtchetNV()" + e);
        }
        return elements;
    }

    public Vector getProductionOtchetNVShvei(int idBrig, int idDept, long sVodDate, long eVodDate) throws Exception {
        Vector elements = new Vector();
        String sql = "";

        try {
            sql = "Select model, nvsum,  date_vvod, sum(vnorm) as norm, idSpec, spec_ as op "
                    + "	From (Select distinct model, list.kol as nvsum, list.date_vvod as date_vvod, spec.id as idSpec, spec as spec_ "
                    + "		From list, list_item, spec, spec_item, dept, s_model, s_brig "
                    + "		Where list.id = list_item.id_list and "
                    + "			list.id_spec = spec.id and "
                    + "			list_item.id_spec_item = spec_item.id and "
                    + "			spec.id = spec_item.id_spec and "
                    + "			list.id_model = s_model.model and "
                    + "			list.id_dept = dept.id and "
                    + "			list.id_brig = s_brig.id and "
                    + "			list.status <> -1 and "
                    + "                     list.date_vvod between '" + new java.sql.Date(sVodDate) + "' "
                    + "                                        and '" + new java.sql.Date(eVodDate) + "' "
                    + (idBrig != -1 ? " and s_brig.id = " + idBrig + " " : " ")
                    + (idDept != -1 ? " and list.id_dept = " + idDept + " " : " ")
                    + "            ) as t1, spec, spec_item "
                    + "	Where spec.id = spec_item.id_spec and "
                    + "		spec_item.num >= 0 and   "
                    + "         spec_item.id_tech <> -1 and "
                    + "		spec.id = idSpec"
                    + "	Group by model, nvsum, date_vvod, idSpec, spec_  "
                    + " UNION "
                    + " Select model, sum(list_item.kolvo) as nvsum, list.date_vvod as date_vvod, vnorm as norm, spec.id as idSpec, operac as op "
                    + "	From list, list_item, spec, spec_item, dept, s_model, s_brig "
                    + "	Where list.id = list_item.id_list and "
                    + "		list.id_spec = spec.id and "
                    + "		list_item.id_spec_item = spec_item.id and  "
                    + "		spec.id = spec_item.id_spec and "
                    + "		spec_item.num < 0 and "
                    + "		list.id_model = s_model.model and  "
                    + "		list.id_dept = dept.id and "
                    + "		list.id_brig = s_brig.id and "
                    + "		list.status <> -1 and "
                    + "             list.date_vvod between '" + new java.sql.Date(sVodDate) + "' "
                    + "                           and '" + new java.sql.Date(eVodDate) + "' "
                    + (idBrig != -1 ? " and s_brig.id = " + idBrig + " " : " ")
                    + (idDept != -1 ? " and list.id_dept = " + idDept + " " : " ")
                    + "	Group by model, date_vvod, vnorm, idSpec, operac  "
                    + "Order by model, idSpec, norm desc, date_vvod";
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                Vector tmp = new Vector();
                tmp.add(new SimpleDateFormat("dd-MM-yyyy").format(rs.getDate("date_vvod")));
                tmp.add(rs.getInt("model"));
                tmp.add(rs.getInt("nvsum"));
                tmp.add(0);
                tmp.add(UtilZPlata.formatNorm(rs.getDouble("norm"), UtilZPlata.ROUNDING_NORM));
                tmp.add(rs.getString("op"));
                elements.add(tmp);
            }

        } catch (Exception e) {
            elements = new Vector();
            log.severe("Ошибка getProductionOtchetNVShvei()" + e);
        }
        return elements;
    }

    public Vector getProductionOtchetNVEmplRaz(int idBrig, int idDept, long sVodDate, long eVodDate, int idEmployer) throws Exception {
        Vector elements = new Vector();
        String sql = "";

        try {
            sql = "Select model, sum(list_item.kolvo) as nvsum, employees.id as idEmployer, fio , enorm, vnorm, operac "
                    + " From list, list_item, spec, spec_item, dept, s_model, s_brig, employees "
                    + "	Where list.id = list_item.id_list and "
                    + "		list.id_spec = spec.id and "
                    + " 	list_item.id_spec_item = spec_item.id and "
                    + "         list_item.id_employe = employees.id and	"
                    + "         spec.id = spec_item.id_spec and "
                    + "         list.id_model = s_model.model and "
                    + "         list.id_dept = dept.id and "
                    + "         list.id_brig = s_brig.id and "
                    + "         list.status <> -1 and  "
                    + "         date_vvod between '" + new java.sql.Date(sVodDate) + "' "
                    + "                       and '" + new java.sql.Date(eVodDate) + "' "
                    + (idBrig != -1 ? " and s_brig.id = " + idBrig + " " : " ")
                    + (idDept != -1 ? " and list.id_dept = " + idDept + " " : " ")
                    + (idEmployer != -1 ? " and employees.id = " + idEmployer + " " : " ")
                    + " Group by model, idEmployer, fio, enorm, vnorm, operac"
                    + " Order by model, enorm, vnorm, operac, fio";

            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                Vector tmp = new Vector();
                tmp.add(rs.getString("fio"));
                tmp.add(rs.getInt("model"));
                tmp.add(rs.getInt("nvsum"));
                tmp.add(rs.getInt("enorm"));
                tmp.add(UtilZPlata.formatNorm(rs.getDouble("vnorm"), UtilZPlata.ROUNDING_NORM));
                tmp.add(rs.getString("operac"));
                elements.add(tmp);
            }

        } catch (Exception e) {
            elements = new Vector();
            log.severe("Ошибка getProductionOtchetNVEmplRaz()" + e);
        }
        return elements;
    }

    public Vector getProductionOtchetNVEmplKr(int idBrig, int idDept, long sVodDate, long eVodDate, int idEmployer) throws Exception {
        Vector elements = new Vector();
        String sql = "";

        try {
            sql = "Select t1.model as model, t1.idEmployer as idEmployer, t1.fio as fio, t1.razryad as razryad, "
                    + "     sum(t1.nvsum) as sum_kol, sum(t2.summ) as sum_vyr "
                    + " From(Select model, employees.id as idEmployer, fio, spec_item.category as razryad, "
                    + "		sum(list_item.kolvo) as nvsum, 'Кол-во'::text as operac_t1 "
                    + "      From list, list_item, spec, spec_item, dept, s_model, s_brig, employees "
                    + "      Where list.id = list_item.id_list and 	"
                    + "             list.id_spec = spec.id and "
                    + "             list_item.id_spec_item = spec_item.id and "
                    + "		list_item.id_employe = employees.id and "
                    + "		spec.id = spec_item.id_spec and "
                    + "		list.id_model = s_model.model and "
                    + "		list.id_dept = dept.id and "
                    + "		list.id_brig = s_brig.id and "
                    + "		list.status <> -1 and "
                    + "         date_vvod between '" + new java.sql.Date(sVodDate) + "' "
                    + "                       and '" + new java.sql.Date(eVodDate) + "' "
                    + (idBrig != -1 ? " and s_brig.id = " + idBrig + " " : " ")
                    + (idDept != -1 ? " and list.id_dept = " + idDept + " " : " ")
                    + "     Group by model, idEmployer, fio, razryad "
                    + "     Order by model, fio, razryad) as t1 "
                    + " join(Select model, employees.id as idEmployer, fio, spec_item.category as razryad, "
                    + "		sum(round(list_item.kolvo*round(spec_item.vnorm::numeric, " + UtilZPlata.ROUNDING_NORM + "), 2)) as summ, 'Выраб.'::text as operac_t2 "
                    + "     From list, list_item, spec, spec_item, dept, s_model, s_brig, employees "
                    + "     Where list.id = list_item.id_list and "
                    + "		list.id_spec = spec.id and "
                    + "		list_item.id_spec_item = spec_item.id and "
                    + "		list_item.id_employe = employees.id and "
                    + "		spec.id = spec_item.id_spec and "
                    + "		list.id_model = s_model.model and "
                    + "		list.id_dept = dept.id and "
                    + "		list.id_brig = s_brig.id and "
                    + "		list.status <> -1 and "
                    + "         date_vvod between '" + new java.sql.Date(sVodDate) + "' "
                    + "                       and '" + new java.sql.Date(eVodDate) + "' "
                    + (idBrig != -1 ? " and s_brig.id = " + idBrig + " " : " ")
                    + (idDept != -1 ? " and list.id_dept = " + idDept + " " : " ")
                    + "     Group by model, idEmployer, fio, razryad "
                    + "     Order by model, fio, razryad ) as t2 "
                    + " on t1.model = t2.model and "
                    + "     t1.idEmployer = t2.idEmployer and  "
                    + "     t1.fio = t2.fio and "
                    + "     t1.razryad = t2.razryad "
                    + (idEmployer != -1 ? " and t1.idEmployer = " + idEmployer + " " : " ")
                    + " Group by t1.model, t1.idEmployer, t1.fio, t1.razryad "
                    + " Order by model, fio, razryad";

            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                Vector tmp = new Vector();
                tmp.add(rs.getString("fio") + "  " + rs.getInt("razryad") + " ");
                tmp.add(rs.getInt("model"));
                tmp.add(rs.getInt("sum_kol"));
                tmp.add(rs.getDouble("sum_vyr"));
                elements.add(tmp);
            }

        } catch (Exception e) {
            elements = new Vector();
            log.severe("Ошибка getProductionOtchetNVEmplKrat()" + e);
        }
        return elements;
    }

    public Vector getProductionOtchetNVModel(int idBrig, int idDept, long sVodDate, long eVodDate, int fas) throws Exception {
        Vector elements = new Vector();
        String sql = "";

        try {
            sql = " Select  "
                    + "     model, "
                    + "     spec_item.num as num, "
                    + "     operac, "
                    + "     s_brig.kod as brig, "
                    + "     sum(list_item.kolvo) as nvsum, "
                    + "     enorm, "
                    + "     vnorm "
                    + " From "
                    + "     list, "
                    + "     list_item, "
                    + "     spec, "
                    + "     spec_item, "
                    + "     s_model, "
                    + "     s_brig "
                    + " Where "
                    + "     list.id = list_item.id_list and "
                    + "     list.id_spec = spec.id and "
                    + "     list_item.id_spec_item = spec_item.id and "
                    + "     spec.id = spec_item.id_spec and "
                    + "     list.id_model = s_model.model and "
                    + "     list.id_brig = s_brig.id and "
                    + "     list.status <> -1 and  "
                    + "     date_vvod between '" + new java.sql.Date(sVodDate) + "' "
                    + "                   and '" + new java.sql.Date(eVodDate) + "' "
                    + (fas != -1 ? " and model        = " + fas + " " : " ")
                    + (idBrig != -1 ? " and s_brig.id    = " + idBrig + " " : " ")
                    + (idDept != -1 ? " and list.id_dept = " + idDept + " " : " ")
                    + " Group by model, enorm, vnorm, s_brig.kod, spec_item.num, operac "
                    + " Order by model, spec_item.num,  operac,  brig ";

            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                Vector tmp = new Vector();
                tmp.add(rs.getInt("model"));
                tmp.add(rs.getInt("num"));
                tmp.add(rs.getString("operac"));
                tmp.add(rs.getInt("brig"));
                tmp.add(rs.getInt("nvsum"));
                tmp.add(rs.getInt("enorm"));
                tmp.add(UtilZPlata.formatNorm(rs.getDouble("vnorm"), UtilZPlata.ROUNDING_NORM));
                elements.add(tmp);
            }

        } catch (Exception e) {
            elements = new Vector();
            log.severe("Ошибка getProductionOtchetNVModel()" + e);
        }
        return elements;
    }

    public Vector getProductionOtchetNVBrak(int idBrig, int idDept, long sVodDate, long eVodDate) throws Exception {
        Vector elements = new Vector();
        String sql = "";

        try {
            sql = "Select model, sum(list_item.kolvo) as nvsum "
                    + "	From list, list_item, spec, spec_item, dept, s_model, s_brig "
                    + "	Where list.id = list_item.id_list and "
                    + "		list.id_spec = spec.id and "
                    + "		list_item.id_spec_item = spec_item.id and "
                    + "		spec.id = spec_item.id_spec and "
                    + "		list.id_model = s_model.model and "
                    + "		list.id_dept = dept.id and "
                    + "		list.id_brig = s_brig.id and "
                    + "		list.status <> -1 and "
                    //     + "		list_item.nvflag = true and "
                    + "		id_employe = -1 and "
                    + "         date_vvod between '" + new java.sql.Date(sVodDate) + "' "
                    + "                       and '" + new java.sql.Date(eVodDate) + "' "
                    + (idBrig != -1 ? " and s_brig.id = " + idBrig + " " : " ")
                    + (idDept != -1 ? " and list.id_dept = " + idDept + " " : " ")
                    + "	Group by model";

            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                Vector tmp = new Vector();
                tmp.add(rs.getInt("model"));
                tmp.add(rs.getInt("nvsum"));
                elements.add(tmp);
            }

        } catch (Exception e) {
            elements = new Vector();
            log.severe("Ошибка getProductionOtchetNVBrak()" + e);
        }
        return elements;
    }

    public Vector getProductionEmployeesTable(int idDept, int idBrig, long sVodDate, long eVodDate) throws Exception {
        Vector elements = new Vector();
        String sql = "";

        try {
            sql = "Select dept.id as idDept, department, s_brig.id as idBrig, s_brig.kod as kodBrig, employees.num as numEmploye, employees.fio as fio, employees.id as idEmploye, "
                    + "         spec_item.category as category, sum(round(list_item.kolvo*round(spec_item.vnorm::numeric," + UtilZPlata.ROUNDING_NORM + "), 2)) as summ "
                    + "  From employees, list, list_item, spec, spec_item, s_brig, dept "
                    + "  Where  list_item.id_employe = employees.id "
                    + "     and list.id = list_item.id_list "
                    + "     and list.id_spec = spec.id "
                    + "     and spec.id = spec_item.id_spec "
                    + "     and spec_item.id = list_item.id_spec_item "
                    + "     and list.status <> -1 "
                    + "     and list.id_brig = s_brig.id "
                    + "     and list.id_dept = dept.id "
                    + (idBrig != -1 ? " and list.id_brig = " + idBrig + " " : " ")
                    + (idDept != -1 ? " and list.id_dept = " + idDept + " " : " ")
                    + "     and date_vvod between '" + new java.sql.Date(sVodDate) + "' "
                    + "                        and '" + new java.sql.Date(eVodDate) + "' "
                    + "  Group by idDept, department, idBrig, kodBrig, idEmploye, employees.fio, employees.num, spec_item.category "
                    + "  Order by idDept, department, kodBrig, employees.fio, employees.num, spec_item.category";

            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                Vector tmp = new Vector();
                tmp.add(false);
                tmp.add(rs.getInt("idDept"));
                tmp.add(rs.getString("department"));
                tmp.add(rs.getInt("idBrig"));
                tmp.add(rs.getInt("kodBrig"));
                tmp.add(rs.getInt("idEmploye"));
                tmp.add(rs.getInt("numEmploye"));
                tmp.add(rs.getString("fio").trim());
                tmp.add(rs.getInt("category"));
                tmp.add(new Double(UtilZPlata.formatNorm(rs.getDouble("summ"), 2)));
                elements.add(tmp);
            }

        } catch (Exception e) {
            elements = new Vector();
            log.severe("Ошибка getProductionEmployeesTable()" + e);
        }
        return elements;
    }

    public Vector getProductionBrigTable(int idDept, int idBrig, long sVodDate, long eVodDate) throws Exception {
        Vector elements = new Vector();
        String sql = "";

        try {
            sql = "Select dept.id as idDept, department, s_brig.id as idBrig, s_brig.kod as kodBrig, sum(round(list_item.kolvo*round(spec_item.vnorm::numeric," + UtilZPlata.ROUNDING_NORM + "), 2)) as summ "
                    + "                  From list, list_item, spec, spec_item, s_brig, dept "
                    + "                  Where  list.id = list_item.id_list "
                    + "                     and list.id_spec = spec.id "
                    + "                     and spec.id = spec_item.id_spec "
                    + "                     and spec_item.id = list_item.id_spec_item "
                    + "                     and list.status <> -1 "
                    + "                     and list.id_brig = s_brig.id "
                    + "                     and list.id_dept = dept.id "
                    + (idBrig != -1 ? " and list.id_brig = " + idBrig + " " : " ")
                    + (idDept != -1 ? " and list.id_dept = " + idDept + " " : " ")
                    + "                     and date_vvod between '" + new java.sql.Date(sVodDate) + "' "
                    + "                                       and '" + new java.sql.Date(eVodDate) + "' "
                    + "                  Group by idDept, department, idBrig, kodBrig"
                    + "                  Order by idDept, department, kodBrig";

            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                Vector tmp = new Vector();
                tmp.add(false);
                tmp.add(rs.getInt("idDept"));
                tmp.add(rs.getString("department"));
                tmp.add(rs.getInt("idBrig"));
                tmp.add(rs.getInt("kodBrig"));
                tmp.add(new Double(UtilZPlata.formatNorm(rs.getDouble("summ"), 2)));
                elements.add(tmp);
            }

        } catch (Exception e) {
            elements = new Vector();
            log.severe("Ошибка getProductionBrigTable()" + e);
        }
        return elements;
    }

    public Vector getProductionModelTable(int idDept, int idBrig, long sVodDate, long eVodDate) throws Exception {
        Vector elements = new Vector();
        String sql = "";

        try {
            sql = "Select dept.id as idDept, department, s_brig.id as idBrig, s_brig.kod as kodBrig, list.id_model as model, sum(round(list_item.kolvo*round(spec_item.vnorm::numeric," + UtilZPlata.ROUNDING_NORM + "), 2)) as summ "
                    + "                  From list, list_item, spec, spec_item, s_brig, dept "
                    + "                  Where  list.id = list_item.id_list "
                    + "                     and list.id_spec = spec.id "
                    + "                     and spec.id = spec_item.id_spec "
                    + "                     and spec_item.id = list_item.id_spec_item "
                    + "                     and list.status <> -1 "
                    + "                     and list.id_brig = s_brig.id "
                    + "                     and list.id_dept = dept.id "
                    + (idBrig != -1 ? " and list.id_brig = " + idBrig + " " : " ")
                    + (idDept != -1 ? " and list.id_dept = " + idDept + " " : " ")
                    + "                     and date_vvod between '" + new java.sql.Date(sVodDate) + "' "
                    + "                                       and '" + new java.sql.Date(eVodDate) + "' "
                    + "                  Group by idDept, department, idBrig, kodBrig, model "
                    + "                  Order by idDept, department, kodBrig, model ";

            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                Vector tmp = new Vector();
                tmp.add(false);
                tmp.add(rs.getInt("idDept"));
                tmp.add(rs.getString("department"));
                tmp.add(rs.getInt("idBrig"));
                tmp.add(rs.getInt("kodBrig"));
                tmp.add(rs.getInt("model"));
                tmp.add(new Double(UtilZPlata.formatNorm(rs.getDouble("summ"), 2)));
                elements.add(tmp);
            }

        } catch (Exception e) {
            elements = new Vector();
            log.severe("Ошибка getProductionModelTable()" + e);
        }
        return elements;
    }

    public Vector getProductionMarshrutTable(int idDept, int idBrig, long sVodDate, long eVodDate) throws Exception {
        Vector elements = new Vector();
        String sql = "";

        try {
            sql = "Select dept.id as idDept, department, s_brig.id as idBrig, s_brig.kod as kodBrig, list.id_model as model, marshlist, sum(round(list_item.kolvo*round(spec_item.vnorm::numeric," + UtilZPlata.ROUNDING_NORM + "), 2)) as summ "
                    + "                  From list, list_item, spec, spec_item, s_brig, dept "
                    + "                  Where  list.id = list_item.id_list "
                    + "                     and list.id_spec = spec.id "
                    + "                     and spec.id = spec_item.id_spec "
                    + "                     and spec_item.id = list_item.id_spec_item "
                    + "                     and list.status <> -1 "
                    + "                     and list.id_brig = s_brig.id "
                    + "                     and list.id_dept = dept.id "
                    + (idBrig != -1 ? " and list.id_brig = " + idBrig + " " : " ")
                    + (idDept != -1 ? " and list.id_dept = " + idDept + " " : " ")
                    + "                     and date_vvod between '" + new java.sql.Date(sVodDate) + "' "
                    + "                                       and '" + new java.sql.Date(eVodDate) + "' "
                    + "                  Group by idDept, department, idBrig, kodBrig, model, marshlist "
                    + "                  Order by idDept, department, kodBrig, model, marshlist ";

            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                Vector tmp = new Vector();
                tmp.add(false);
                tmp.add(rs.getInt("idDept"));
                tmp.add(rs.getString("department"));
                tmp.add(rs.getInt("idBrig"));
                tmp.add(rs.getInt("kodBrig"));
                tmp.add(rs.getInt("model"));
                tmp.add(rs.getString("marshlist").trim());
                tmp.add(new Double(UtilZPlata.formatNorm(rs.getDouble("summ"), 2)));
                elements.add(tmp);
            }

        } catch (Exception e) {
            elements = new Vector();
            log.severe("Ошибка getProductionMarshrutTable()" + e);
        }
        return elements;
    }

    public Vector getKolvoModelTable(int idDept, int idBrig, long sVodDate, long eVodDate) throws Exception {
        Vector elements = new Vector();
        String sql = "";

        try {
            sql = "Select t3.idDept as idDept, t3.department as department, t3.idBrig as idBrig, t3.kodBrig as kodBrig, t3.model as model, "
                    + "         t3.kol as kol, t3.kol_ch as kol_ch, t4.kol_br as kol_br "
                    + " From (Select t1.idDept, t1.department, t1.idBrig, t1.kodBrig, t1.model, kol, kol_ch "
                    + "	From(Select dept.id as idDept, department, s_brig.id as idBrig, s_brig.kod as kodBrig, model, sum(kol) as kol "
                    + "			 From list, dept, s_model, s_brig "
                    + "			 Where list.id_model = s_model.model and "
                    + "			       list.id_dept = dept.id and "
                    + "			       list.id_brig = s_brig.id  and "
                    + "                            list.status <> -1 "
                    + (idBrig != -1 ? " and list.id_brig = " + idBrig + " " : " ")
                    + (idDept != -1 ? " and list.id_dept = " + idDept + " " : " ")
                    + "                            and date_vvod between '" + new java.sql.Date(sVodDate) + "' "
                    + "                                              and '" + new java.sql.Date(eVodDate) + "' "
                    + "			 Group by idDept, department, idBrig, kodBrig, model ) as t1 "
                    + "	left join (Select dept.id as idDept, department, s_brig.id as idBrig, s_brig.kod as kodBrig, model, sum(list_item.kolvo) as kol_ch "
                    + "			 From list, list_item, dept, s_model, s_brig "
                    + "			 Where list.id = list_item.id_list and "
                    + "			       list.id_model = s_model.model and "
                    + "			       list.id_dept = dept.id and  "
                    + "			       list.id_brig = s_brig.id  and "
                    + "			       list_item.nvflag = true and"
                    + "			       list_item.id_employe <> -1 and "
                    + "                            list.status <> -1 "
                    + (idBrig != -1 ? " and list.id_brig = " + idBrig + " " : " ")
                    + (idDept != -1 ? " and list.id_dept = " + idDept + " " : " ")
                    + "                            and date_vvod between '" + new java.sql.Date(sVodDate) + "' "
                    + "                                              and '" + new java.sql.Date(eVodDate) + "' "
                    + "			 Group by idDept, department, idBrig, kodBrig, model) as t2 "
                    + "	on t1.idDept = t2.idDept and "
                    + "	   t1.idBrig = t2.idBrig and "
                    + "	   t1.kodBrig = t2.kodBrig and "
                    + "	   t1.model = t2.model "
                    + "	Order by t1.department, t1.idBrig, t1.model ) as t3 "
                    + " left join (Select dept.id as idDept, department, s_brig.id as idBrig, s_brig.kod as kodBrig, model, sum(list_item.kolvo) as kol_br "
                    + "			 From list, list_item, dept, s_model, s_brig "
                    + "			 Where list.id = list_item.id_list and "
                    + "			       list.id_model = s_model.model and "
                    + "			       list.id_dept = dept.id and "
                    + "			       list.id_brig = s_brig.id  and "
                    // + "			       list_item.nvflag = true and "
                    + "			       list_item.id_employe = -1 and "
                    + "                            list.status <> -1 "
                    + (idBrig != -1 ? " and list.id_brig = " + idBrig + " " : " ")
                    + (idDept != -1 ? " and list.id_dept = " + idDept + " " : " ")
                    + "                            and date_vvod between '" + new java.sql.Date(sVodDate) + "' "
                    + "                                              and '" + new java.sql.Date(eVodDate) + "' "
                    + "			 Group by idDept, department, idBrig, kodBrig, model) as t4 "
                    + " on t3.idDept = t4.idDept and "
                    + "   t3.idBrig = t4.idBrig and "
                    + "   t3.kodBrig = t4.kodBrig and "
                    + "   t3.model = t4.model "
                    + " Order by idDept, department, kodBrig, model ";

            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                Vector tmp = new Vector();
                tmp.add(false);
                tmp.add(rs.getInt("idDept"));
                tmp.add(rs.getString("department"));
                tmp.add(rs.getInt("idBrig"));
                tmp.add(rs.getInt("kodBrig"));
                tmp.add(rs.getInt("model"));
                tmp.add(rs.getInt("kol"));
                tmp.add(rs.getInt("kol_ch"));
                tmp.add(rs.getInt("kol_br"));
                elements.add(tmp);
            }

        } catch (Exception e) {
            elements = new Vector();
            log.severe("Ошибка getKolvoModelTable()" + e);
        }
        return elements;
    }

    public Vector getWorkEmployeTable(int idDept, int idBrig, long sVodDate, long eVodDate, boolean flag, int workDay) throws Exception {
        Vector elements = new Vector();
        String sql = "";
        double itogo = 0;

        try {
            if (flag)
                sql = "Select coalesce(t3.idEmploye, t4.idEmploye) as idEmploye, coalesce(t3.numEmploye, t4.numEmploye) as numEmploye, "
                        + "     coalesce(t3.fio, t4.fio) as fio, coalesce(category, t3.razryad) as razryad, summ, summ_nes, summ_dk "
                        + " From (Select coalesce(t2.idDept, t1.idDept) as idDept, coalesce(t2.idEmploye, t1.idEmploye) as idEmploye, "
                        + "             coalesce(t2.numEmploye, t1.numEmploye) as numEmploye, coalesce(t2.fio, t1.fio) as fio, "
                        + "             coalesce(category, razryad) as razryad, summ, summ_nes"
                        + "        From (Select list_nesort.id_dept as idDept, list_nesort.id_brig as idBrig, employees.id as idEmploye, employees.num as numEmploye, "
                        + "			employees.fio as fio, list_nesort.razryad as razryad, sum(round(list_nesort.kol*" + UtilZPlata.NESORT_KOF + ", 2)) as summ_nes 	"
                        + "             From employees, list_nesort, s_brig "
                        + "             Where   list_nesort.id_employer = employees.id "
                        + "			and employees.id <> -1 	"
                        + "			and list_nesort.id_brig = s_brig.id  "
                        + (idBrig != -1 ? " and s_brig.id = " + idBrig + " " : " ")
                        + (idDept != -1 ? " and list_nesort.id_dept = " + idDept + " " : " ")
                        + "                 and date_vvod between '" + new java.sql.Date(sVodDate) + "' "
                        + "                                   and '" + new java.sql.Date(eVodDate) + "' "
                        + "        Group by idDept, idBrig, idEmploye, numEmploye, fio, razryad) as t1  "
                        + "	full join (Select list.id_dept as idDept, list.id_brig as idBrig, employees.id as idEmploye, employees.num as numEmploye, "
                        + "			employees.fio as fio,spec_item.category as category, sum(round(list_item.kolvo*round(spec_item.vnorm::numeric," + UtilZPlata.ROUNDING_NORM + "), 2)) as summ "
                        + "		    From employees, list, list_item, spec, spec_item, s_brig "
                        + "		    Where   list_item.id_employe = employees.id "
                        + "			and list.id = list_item.id_list "
                        + "			and list.id_spec = spec.id  "
                        + "			and spec.id = spec_item.id_spec	"
                        + "			and spec_item.id = list_item.id_spec_item "
                        + "			and list.status <> -1 	"
                        + "			and employees.id <> -1 	"
                        + "			and list.id_brig = s_brig.id  "
                        + (idBrig != -1 ? " and list.id_brig = " + idBrig + " " : " ")
                        + (idDept != -1 ? " and list.id_dept = " + idDept + " " : " ")
                        + "                 and date_vvod between '" + new java.sql.Date(sVodDate) + "' "
                        + "                                   and '" + new java.sql.Date(eVodDate) + "' "
                        + "		Group by idDept, idBrig, idEmploye, numEmploye, fio, category) as t2 "
                        + "	on t1.idDept = t2.idDept "
                        + "		and t1.idBrig = t2.idBrig "
                        + "		and t1.idEmploye = t2.idEmploye "
                        + "		and t1.razryad = t2.category  ) as t3 "
                        + " full join (Select list.id_dept as idDept, employees.id as idEmploye, employees.num as numEmploye, "
                        + "			employees.fio as fio,spec_item.category as category, "
                        + "                 sum(round(list_item.kolvo*round(spec_item.vnorm::numeric," + UtilZPlata.ROUNDING_NORM + "), 2)) as summ_dk "
                        + "		    From employees, list, list_item, spec, spec_item, s_brig "
                        + "		    Where   list_item.id_employe = employees.id "
                        + "			and list.id = list_item.id_list "
                        + "			and list.id_spec = spec.id  "
                        + "			and spec.id = spec_item.id_spec	"
                        + "			and spec_item.id = list_item.id_spec_item "
                        + "			and list.status <> -1 	"
                        + "			and employees.id <> -1 	"
                        + "			and list.id_brig = s_brig.id  "
                        + (idBrig != -1 ? " and list.id_brig <> " + idBrig + " " : " ")
                        + "			and employees.id in ( Select distinct id_employe "
                        + "						From list, list_item 	"
                        + "						Where list.id = list_item.id_list "
                        + "                                               and list.status <> -1 "
                        + "                                               and list_item.id_employe <> -1 "
                        + (idBrig != -1 ? " and list.id_brig = " + idBrig + " " : " ")
                        + (idDept != -1 ? " and list.id_dept = " + idDept + " " : " ")
                        + "                                               and date_vvod between '" + new java.sql.Date(sVodDate) + "' "
                        + "                                                                  and '" + new java.sql.Date(eVodDate) + "'  )"
                        + (idDept != -1 ? " and list.id_dept = " + idDept + " " : " ")
                        + "                  and date_vvod between '" + new java.sql.Date(sVodDate) + "' "
                        + "                                    and '" + new java.sql.Date(eVodDate) + "' "
                        + "		Group by idDept, idEmploye, numEmploye, fio, category ) as t4 "
                        + " on t3.idDept = t4.idDept "
                        + "		and t3.idEmploye = t4.idEmploye "
                        + "		and t3.razryad = t4.category "
                        + " Order by fio, numEmploye, razryad ";
            else
                sql = "Select coalesce(t3.idEmploye, t4.idEmploye) as idEmploye, coalesce(t3.numEmploye, t4.numEmploye) as numEmploye, "
                        + "     coalesce(t3.fio, t4.fio) as fio, 0::numeric as razryad, summ, summ_nes, summ_dk "
                        + " From (Select coalesce(t2.idDept, t1.idDept) as idDept, coalesce(t2.idEmploye, t1.idEmploye) as idEmploye, "
                        + "             coalesce(t2.numEmploye, t1.numEmploye) as numEmploye, coalesce(t2.fio, t1.fio) as fio, summ, summ_nes"
                        + "        From (Select list_nesort.id_dept as idDept, list_nesort.id_brig as idBrig, employees.id as idEmploye, employees.num as numEmploye, "
                        + "			employees.fio as fio, sum(round(list_nesort.kol*" + UtilZPlata.NESORT_KOF + ", 2)) as summ_nes 	"
                        + "             From employees, list_nesort, s_brig "
                        + "             Where   list_nesort.id_employer = employees.id "
                        + "			and employees.id <> -1 	"
                        + "			and list_nesort.id_brig = s_brig.id  "
                        + (idBrig != -1 ? " and s_brig.id = " + idBrig + " " : " ")
                        + (idDept != -1 ? " and list_nesort.id_dept = " + idDept + " " : " ")
                        + "                 and date_vvod between '" + new java.sql.Date(sVodDate) + "' "
                        + "                                   and '" + new java.sql.Date(eVodDate) + "' "
                        + "        Group by idDept, idBrig, idEmploye, numEmploye, fio) as t1  "
                        + "	full join (Select list.id_dept as idDept, list.id_brig as idBrig, employees.id as idEmploye, employees.num as numEmploye, "
                        + "			employees.fio as fio, sum(round(list_item.kolvo*round(spec_item.vnorm::numeric," + UtilZPlata.ROUNDING_NORM + "), 2)) as summ "
                        + "		    From employees, list, list_item, spec, spec_item, s_brig "
                        + "		    Where   list_item.id_employe = employees.id "
                        + "			and list.id = list_item.id_list "
                        + "			and list.id_spec = spec.id  "
                        + "			and spec.id = spec_item.id_spec	"
                        + "			and spec_item.id = list_item.id_spec_item "
                        + "			and list.status <> -1 	"
                        + "			and employees.id <> -1 	"
                        + "			and list.id_brig = s_brig.id  "
                        + (idBrig != -1 ? " and list.id_brig = " + idBrig + " " : " ")
                        + (idDept != -1 ? " and list.id_dept = " + idDept + " " : " ")
                        + "                 and date_vvod between '" + new java.sql.Date(sVodDate) + "' "
                        + "                                   and '" + new java.sql.Date(eVodDate) + "' "
                        + "		Group by idDept, idBrig, idEmploye, numEmploye, fio) as t2 "
                        + "	on t1.idDept = t2.idDept "
                        + "		and t1.idBrig = t2.idBrig "
                        + "		and t1.idEmploye = t2.idEmploye ) as t3 "
                        + " full join (Select list.id_dept as idDept, employees.id as idEmploye, employees.num as numEmploye, "
                        + "			employees.fio as fio, sum(round(list_item.kolvo*round(spec_item.vnorm::numeric," + UtilZPlata.ROUNDING_NORM + "), 2)) as summ_dk "
                        + "		    From employees, list, list_item, spec, spec_item, s_brig "
                        + "		    Where   list_item.id_employe = employees.id "
                        + "			and list.id = list_item.id_list "
                        + "			and list.id_spec = spec.id  "
                        + "			and spec.id = spec_item.id_spec	"
                        + "			and spec_item.id = list_item.id_spec_item "
                        + "			and list.status <> -1 	"
                        + "			and employees.id <> -1 	"
                        + "			and list.id_brig = s_brig.id  "
                        + (idBrig != -1 ? " and list.id_brig <> " + idBrig + " " : " ")
                        + "			and employees.id in ( Select distinct id_employe "
                        + "						From list, list_item	 "
                        + "						Where list.id = list_item.id_list  "
                        + "                                               and list.status <> -1 "
                        + "                                               and list_item.id_employe <> -1 "
                        + (idBrig != -1 ? " and list.id_brig = " + idBrig + " " : " ")
                        + (idDept != -1 ? " and list.id_dept = " + idDept + " " : " ")
                        + "                                               and date_vvod between '" + new java.sql.Date(sVodDate) + "' "
                        + "                                                                 and '" + new java.sql.Date(eVodDate) + "'  )"
                        + (idDept != -1 ? " and list.id_dept = " + idDept + " " : " ")
                        + "                  and date_vvod between '" + new java.sql.Date(sVodDate) + "' "
                        + "                                    and '" + new java.sql.Date(eVodDate) + "' "
                        + "		Group by idDept, idEmploye, numEmploye, fio ) as t4 "
                        + " on t3.idDept = t4.idDept "
                        + "		and t3.idEmploye = t4.idEmploye "
                        + " Order by fio, numEmploye, razryad ";

            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                Vector tmp = new Vector();

                itogo = new Double(UtilZPlata.formatNorm(new Double(UtilZPlata.formatNorm(rs.getDouble("summ"), 2)) +
                        new Double(UtilZPlata.formatNorm(rs.getDouble("summ_nes"), 2)) +
                        new Double(UtilZPlata.formatNorm(rs.getDouble("summ_dk"), 2)), 2));
                tmp.add(false);
                tmp.add(rs.getInt("idEmploye"));
                tmp.add(rs.getInt("numEmploye"));
                tmp.add(rs.getString("fio").trim());


                tmp.add(rs.getInt("razryad"));
                tmp.add(new Double(UtilZPlata.formatNorm(rs.getDouble("summ"), 2)));
                tmp.add(new Double(UtilZPlata.formatNorm(rs.getDouble("summ_nes"), 2)));
                tmp.add(new Double(UtilZPlata.formatNorm(rs.getDouble("summ_dk"), 2)));
                tmp.add(itogo);
                tmp.add(workDay);
                tmp.add(new Double(UtilZPlata.formatNorm((itogo * 100) / workDay, 2)));
                elements.add(tmp);
            }

        } catch (Exception e) {
            elements = new Vector();
            log.severe("Ошибка getWorkEmployeTable()" + e);
        }
        return elements;
    }

    public Vector getWorkEmployeTableByOnlyJobEvaluation(int idDept, int idBrig, long sVodDate, long eVodDate, boolean flag, int workDay) throws Exception {
        Vector elements = new Vector();
        String sql = "";
        double itogo = 0;

        try {
            if (flag)
                sql = " Select numEmploye, " +
                        "       razryad, " +
                        "       sum(round(summ, 2)) as summ, " +
                        "       sum(round(summ_nes, 2)) as summ_nes, " +
                        "       sum(round(summ_dk, 2)) as summ_dk " +
                        " From ( Select coalesce(t3.idEmploye, t4.idEmploye) as idEmploye, coalesce(t3.numEmploye, t4.numEmploye) as numEmploye, "
                        + "             coalesce(t3.fio, t4.fio) as fio, coalesce(category, t3.razryad) as razryad, summ, summ_nes, summ_dk "
                        + "         From (Select coalesce(t2.idDept, t1.idDept) as idDept, coalesce(t2.idEmploye, t1.idEmploye) as idEmploye, "
                        + "                      coalesce(t2.numEmploye, t1.numEmploye) as numEmploye, coalesce(t2.fio, t1.fio) as fio,  "
                        + "                      coalesce(category, razryad) as razryad, summ, summ_nes"
                        + "                 From (Select list_nesort.id_dept as idDept, list_nesort.id_brig as idBrig, employees.id as idEmploye, employees.num as numEmploye, "
                        + "			                    employees.fio as fio, list_nesort.razryad as razryad, sum(round(list_nesort.kol*" + UtilZPlata.NESORT_KOF + ", 2)) as summ_nes 	"
                        + "                         From employees, list_nesort, s_brig "
                        + "                         Where   list_nesort.id_employer = employees.id "
                        + "			                        and employees.id <> -1 	"
                        + "			                        and list_nesort.id_brig = s_brig.id  "
                        + (idBrig != -1 ? " and s_brig.id = " + idBrig + " " : " ")
                        + (idDept != -1 ? " and list_nesort.id_dept = " + idDept + " " : " ")
                        + "                                 and date_vvod between '" + new java.sql.Date(sVodDate) + "' "
                        + "                                                   and '" + new java.sql.Date(eVodDate) + "' "
                        + "                         Group by idDept, idBrig, idEmploye, numEmploye, fio, razryad) as t1  "
                        + "	                        full join (Select list.id_dept as idDept, list.id_brig as idBrig, employees.id as idEmploye, employees.num as numEmploye, "
                        + "			                                    employees.fio as fio, spec_item.category as category, sum(round(list_item.kolvo*round(spec_item.vnorm::numeric," + UtilZPlata.ROUNDING_NORM + "), 2)) as summ "
                        + "		                                From employees, list, list_item, spec, spec_item, s_brig "
                        + "		                                Where   list_item.id_employe = employees.id "
                        + "			                                and list.id = list_item.id_list "
                        + "			                                and list.id_spec = spec.id  "
                        + "			                                and spec.id = spec_item.id_spec	"
                        + "			                                and spec_item.id = list_item.id_spec_item "
                        + "			                                and list.status <> -1 	"
                        + "			                                and employees.id <> -1 	"
                        + "			                                and list.id_brig = s_brig.id  "
                        + (idBrig != -1 ? " and list.id_brig = " + idBrig + " " : " ")
                        + (idDept != -1 ? " and list.id_dept = " + idDept + " " : " ")
                        + "                                         and date_vvod between '" + new java.sql.Date(sVodDate) + "' "
                        + "                                                            and '" + new java.sql.Date(eVodDate) + "' "
                        + "		                                Group by idDept, idBrig, idEmploye, numEmploye, fio, category) as t2 "
                        + "	                        on t1.idDept = t2.idDept "
                        + "		                        and t1.idBrig = t2.idBrig "
                        + "		                        and t1.idEmploye = t2.idEmploye "
                        + "		                        and t1.razryad = t2.category  ) as t3 "
                        + "         full join (Select list.id_dept as idDept, employees.id as idEmploye, employees.num as numEmploye, "
                        + "			                    employees.fio as fio, spec_item.category as category, "
                        + "                             sum(round(list_item.kolvo*round(spec_item.vnorm::numeric," + UtilZPlata.ROUNDING_NORM + "), 2)) as summ_dk "
                        + "		                From employees, list, list_item, spec, spec_item, s_brig "
                        + "		                Where   list_item.id_employe = employees.id "
                        + "			                and list.id = list_item.id_list "
                        + "			                and list.id_spec = spec.id  "
                        + "			                and spec.id = spec_item.id_spec	"
                        + "			                and spec_item.id = list_item.id_spec_item "
                        + "			                and list.status <> -1 	"
                        + "			                and employees.id <> -1 	"
                        + "			                and list.id_brig = s_brig.id  "
                        + (idBrig != -1 ? " and list.id_brig <> " + idBrig + " " : " ")
                        + "			                and employees.id in ( Select distinct id_employe "
                        + "						                            From list, list_item 	"
                        + "						                            Where list.id = list_item.id_list "
                        + "                                                     and list.status <> -1 "
                        + "                                                     and list_item.id_employe <> -1 "
                        + (idBrig != -1 ? " and list.id_brig = " + idBrig + " " : " ")
                        + (idDept != -1 ? " and list.id_dept = " + idDept + " " : " ")
                        + "                                                     and date_vvod between '" + new java.sql.Date(sVodDate) + "' "
                        + "                                                                         and '" + new java.sql.Date(eVodDate) + "'  )"
                        + (idDept != -1 ? " and list.id_dept = " + idDept + " " : " ")
                        + "                         and date_vvod between '" + new java.sql.Date(sVodDate) + "' "
                        + "                                           and '" + new java.sql.Date(eVodDate) + "' "
                        + "		                Group by idDept, idEmploye, numEmploye, fio, category ) as t4 "
                        + "         on      t3.idDept = t4.idDept "
                        + "		        and t3.idEmploye = t4.idEmploye "
                        + "		        and t3.razryad = t4.category ) as t0 "
                        + " Group by numEmploye, razryad "
                        + " Order by numEmploye, razryad ";
            else
                sql = " Select numEmploye, " +
                        "       razryad, " +
                        "       sum(round(summ, 2)) as summ, " +
                        "       sum(round(summ_nes, 2)) as summ_nes, " +
                        "       sum(round(summ_dk, 2)) as summ_dk " +
                        " From ( Select coalesce(t3.idEmploye, t4.idEmploye) as idEmploye, coalesce(t3.numEmploye, t4.numEmploye) as numEmploye, "
                        + "              coalesce(t3.fio, t4.fio) as fio, 0::numeric as razryad, summ, summ_nes, summ_dk "
                        + "         From (Select coalesce(t2.idDept, t1.idDept) as idDept, coalesce(t2.idEmploye, t1.idEmploye) as idEmploye, "
                        + "                     coalesce(t2.numEmploye, t1.numEmploye) as numEmploye, coalesce(t2.fio, t1.fio) as fio, summ, summ_nes "
                        + "               From (Select list_nesort.id_dept as idDept, list_nesort.id_brig as idBrig, employees.id as idEmploye, employees.num as numEmploye, "
                        + "			                    employees.fio as fio, sum(round(list_nesort.kol*" + UtilZPlata.NESORT_KOF + ", 2)) as summ_nes 	"
                        + "                         From employees, list_nesort, s_brig "
                        + "                         Where   list_nesort.id_employer = employees.id "
                        + "			                    and employees.id <> -1 	"
                        + "			                    and list_nesort.id_brig = s_brig.id  "
                        + (idBrig != -1 ? " and s_brig.id = " + idBrig + " " : " ")
                        + (idDept != -1 ? " and list_nesort.id_dept = " + idDept + " " : " ")
                        + "                             and date_vvod between '" + new java.sql.Date(sVodDate) + "' "
                        + "                                               and '" + new java.sql.Date(eVodDate) + "' "
                        + "                         Group by idDept, idBrig, idEmploye, numEmploye, fio ) as t1  "
                        + "	                full join (Select list.id_dept as idDept, list.id_brig as idBrig, employees.id as idEmploye, employees.num as numEmploye, "
                        + "			                        employees.fio as fio, sum(round(list_item.kolvo*round(spec_item.vnorm::numeric," + UtilZPlata.ROUNDING_NORM + "), 2)) as summ "
                        + "		                        From employees, list, list_item, spec, spec_item, s_brig "
                        + "		                        Where   list_item.id_employe = employees.id "
                        + "			                        and list.id = list_item.id_list "
                        + "			                        and list.id_spec = spec.id  "
                        + "			                        and spec.id = spec_item.id_spec	"
                        + "			                        and spec_item.id = list_item.id_spec_item "
                        + "			                        and list.status <> -1 	"
                        + "			                        and employees.id <> -1 	"
                        + "			                        and list.id_brig = s_brig.id  "
                        + (idBrig != -1 ? " and list.id_brig = " + idBrig + " " : " ")
                        + (idDept != -1 ? " and list.id_dept = " + idDept + " " : " ")
                        + "                                 and date_vvod between '" + new java.sql.Date(sVodDate) + "' "
                        + "                                                   and '" + new java.sql.Date(eVodDate) + "' "
                        + "		                        Group by idDept, idBrig, idEmploye, numEmploye, fio) as t2 "
                        + "	                on t1.idDept = t2.idDept "
                        + "		                and t1.idBrig = t2.idBrig "
                        + "		                and t1.idEmploye = t2.idEmploye ) as t3 "
                        + "         full join (Select list.id_dept as idDept, employees.id as idEmploye, employees.num as numEmploye, "
                        + "			                employees.fio as fio, sum(round(list_item.kolvo*round(spec_item.vnorm::numeric," + UtilZPlata.ROUNDING_NORM + "), 2)) as summ_dk "
                        + "		                From employees, list, list_item, spec, spec_item, s_brig "
                        + "		                Where   list_item.id_employe = employees.id "
                        + "			                and list.id = list_item.id_list "
                        + "			                and list.id_spec = spec.id  "
                        + "			                and spec.id = spec_item.id_spec	"
                        + "			                and spec_item.id = list_item.id_spec_item "
                        + "			                and list.status <> -1 	"
                        + "			                and employees.id <> -1 	"
                        + "			                and list.id_brig = s_brig.id  "
                        + (idBrig != -1 ? " and list.id_brig <> " + idBrig + " " : " ")
                        + "			                and employees.id in ( Select distinct id_employe "
                        + "					                    	        From list, list_item	 "
                        + "						                            Where list.id = list_item.id_list  "
                        + "                                                     and list.status <> -1 "
                        + "                                                     and list_item.id_employe <> -1 "
                        + (idBrig != -1 ? " and list.id_brig = " + idBrig + " " : " ")
                        + (idDept != -1 ? " and list.id_dept = " + idDept + " " : " ")
                        + "                                                     and date_vvod between '" + new java.sql.Date(sVodDate) + "' "
                        + "                                                                         and '" + new java.sql.Date(eVodDate) + "'  )"
                        + (idDept != -1 ? " and list.id_dept = " + idDept + " " : " ")
                        + "                         and date_vvod between '" + new java.sql.Date(sVodDate) + "' "
                        + "                                           and '" + new java.sql.Date(eVodDate) + "' "
                        + "		                Group by idDept, idEmploye, numEmploye, fio ) as t4 "
                        + "         on t3.idDept = t4.idDept "
                        + "		        and t3.idEmploye = t4.idEmploye ) as t0 " +
                        "   Group by numEmploye, razryad "
                        + " Order by numEmploye, razryad ";

            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                Vector tmp = new Vector();

                itogo = new Double(UtilZPlata.formatNorm(new Double(UtilZPlata.formatNorm(rs.getDouble("summ"), 2)) +
                        new Double(UtilZPlata.formatNorm(rs.getDouble("summ_nes"), 2)) +
                        new Double(UtilZPlata.formatNorm(rs.getDouble("summ_dk"), 2)), 2));
                tmp.add(false);
                tmp.add(0);
                tmp.add(rs.getInt("numEmploye"));
                tmp.add("таб№ " + rs.getString("numEmploye"));
                tmp.add(rs.getInt("razryad"));
                tmp.add(new Double(UtilZPlata.formatNorm(rs.getDouble("summ"), 2)));
                tmp.add(new Double(UtilZPlata.formatNorm(rs.getDouble("summ_nes"), 2)));
                tmp.add(new Double(UtilZPlata.formatNorm(rs.getDouble("summ_dk"), 2)));
                tmp.add(itogo);
                tmp.add(workDay);
                tmp.add(new Double(UtilZPlata.formatNorm((itogo * 100) / workDay, 2)));
                elements.add(tmp);
            }

        } catch (Exception e) {
            elements = new Vector();
            log.severe("Ошибка getWorkEmployeTable()" + e);
        }
        return elements;
    }

    public Vector getKolvoMarshrutTable(int idDept, int idBrig, long sVodDate, long eVodDate) throws Exception {
        Vector elements = new Vector();
        String sql = "";

        try {
            sql = "Select t3.idDept as idDept, t3.department as department, t3.idBrig as idBrig, t3.kodBrig as kodBrig, t3.marshlist as marshlist, "
                    + "         t3.kol as kol, t3.kol_ch as kol_ch, t4.kol_br as kol_br "
                    + " From (Select t1.idDept, t1.department, t1.idBrig, t1.kodBrig, t1.marshlist, kol, kol_ch "
                    + "	From(Select dept.id as idDept, department, s_brig.id as idBrig, s_brig.kod as kodBrig, marshlist, sum(kol) as kol "
                    + "			 From list, dept, s_brig "
                    + "			 Where list.id_dept = dept.id and "
                    + "			       list.id_brig = s_brig.id  and "
                    + "                            list.status <> -1 "
                    + (idBrig != -1 ? " and list.id_brig = " + idBrig + " " : " ")
                    + (idDept != -1 ? " and list.id_dept = " + idDept + " " : " ")
                    + "                            and date_vvod between '" + new java.sql.Date(sVodDate) + "' "
                    + "                                              and '" + new java.sql.Date(eVodDate) + "' "
                    + "			 Group by idDept, department, idBrig, kodBrig, marshlist ) as t1 "
                    + "	left join (Select dept.id as idDept, department, s_brig.id as idBrig, s_brig.kod as kodBrig, marshlist, sum(list_item.kolvo) as kol_ch "
                    + "			 From list, list_item, dept, s_brig "
                    + "			 Where list.id = list_item.id_list and "
                    + "			       list.id_dept = dept.id and  "
                    + "			       list.id_brig = s_brig.id  and "
                    + "			       list_item.nvflag = true and"
                    + "			       list_item.id_employe <> -1 and "
                    + "                            list.status <> -1 "
                    + (idBrig != -1 ? " and list.id_brig = " + idBrig + " " : " ")
                    + (idDept != -1 ? " and list.id_dept = " + idDept + " " : " ")
                    + "                            and date_vvod between '" + new java.sql.Date(sVodDate) + "' "
                    + "                                              and '" + new java.sql.Date(eVodDate) + "' "
                    + "			 Group by idDept, department, idBrig, kodBrig, marshlist) as t2 "
                    + "	on t1.idDept = t2.idDept and "
                    + "	   t1.idBrig = t2.idBrig and "
                    + "	   t1.kodBrig = t2.kodBrig and "
                    + "	   t1.marshlist = t2.marshlist "
                    + "	Order by t1.department, t1.idBrig, t1.marshlist ) as t3 "
                    + " left join (Select dept.id as idDept, department, s_brig.id as idBrig, s_brig.kod as kodBrig, marshlist, sum(list_item.kolvo) as kol_br "
                    + "			 From list, list_item, dept, s_brig "
                    + "			 Where list.id = list_item.id_list and "
                    + "			       list.id_dept = dept.id and "
                    + "			       list.id_brig = s_brig.id  and "
                    // + "			       list_item.nvflag = true and "
                    + "			       list_item.id_employe = -1 and "
                    + "                            list.status <> -1 "
                    + (idBrig != -1 ? " and list.id_brig = " + idBrig + " " : " ")
                    + (idDept != -1 ? " and list.id_dept = " + idDept + " " : " ")
                    + "                            and date_vvod between '" + new java.sql.Date(sVodDate) + "' "
                    + "                                              and '" + new java.sql.Date(eVodDate) + "' "
                    + "			 Group by idDept, department, idBrig, kodBrig, marshlist) as t4 "
                    + " on t3.idDept = t4.idDept and "
                    + "   t3.idBrig = t4.idBrig and "
                    + "   t3.kodBrig = t4.kodBrig and "
                    + "   t3.marshlist = t4.marshlist "
                    + " Order by idDept, department, kodBrig, marshlist ";

            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                Vector tmp = new Vector();
                tmp.add(false);
                tmp.add(rs.getInt("idDept"));
                tmp.add(rs.getString("department"));
                tmp.add(rs.getInt("idBrig"));
                tmp.add(rs.getInt("kodBrig"));
                tmp.add(rs.getString("marshlist").trim());
                tmp.add(rs.getInt("kol"));
                tmp.add(rs.getInt("kol_ch"));
                tmp.add(rs.getInt("kol_br"));
                elements.add(tmp);
            }

        } catch (Exception e) {
            elements = new Vector();
            log.severe("Ошибка getKolvoMarshrutTable()" + e);
        }
        return elements;
    }

    public Vector searchList(int idBrigadir, long sDate) throws Exception {
        Vector elements = new Vector();
        String sql = "Select department, kodBrig, idList, marshlist, model, kol, sum(list_item.kolvo) as nvsum, fio, date_vvod, date_ins, status "
                + " From( Select department, s_brig.kod as kodBrig, list.id as idList, marshlist, model, kol,  fio, date_vvod, date_ins, status "
                + "                 From list, dept, employees, s_model, s_brig "
                + "                 Where list.id_model = s_model.model and "
                + "                	list.id_employer = employees.id and "
                + "                	list.id_dept = dept.id and "
                + "                	list.id_brig = s_brig.id and "
                + "                     employees.id = ? and "
                + "                     date_vvod between ? and now()  "
                + "     ) as t1 left join list_item on t1.idList = list_item.id_list and nvflag = true and id_employe <> -1 "
                + " Group by department, kodBrig, idList, marshlist, model, kol,  fio, date_vvod, date_ins, status "
                + " Order by department, kodBrig, date_vvod, model, idList  ";

        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, idBrigadir);
            ps.setDate(2, new java.sql.Date(sDate));
            rs = ps.executeQuery();
            while (rs.next()) {
                Vector tmp = new Vector();
                tmp.add(false);
                tmp.add(rs.getString("department"));
                tmp.add(rs.getInt("kodBrig"));
                tmp.add(rs.getString("fio"));
                tmp.add(rs.getInt("idList"));
                tmp.add(rs.getString("marshlist"));
                tmp.add(rs.getInt("model"));
                tmp.add(rs.getInt("kol"));
                tmp.add(rs.getInt("nvsum"));
                tmp.add(new SimpleDateFormat("dd-MM-yyyy").format(rs.getDate("date_vvod")));
                tmp.add(new SimpleDateFormat("dd-MM-yyyy").format(rs.getDate("date_ins")));

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
                elements.add(tmp);
            }
        } catch (Exception e) {
            elements = new Vector();
            log.severe("Ошибка getAllList()" + e);
        }
        return elements;
    }

    public Vector getDetalProductionEmployees(int idDept, int idBrig, int idEmploye, int razryad, long sDate, long eDate) throws Exception {
        Vector elements = new Vector();
        String sql = "";

        try {
            sql = "Select dept.id as idDept, department, s_brig.id as idBrig, s_brig.kod as kodBrig, list.id as idList, model, marshlist, list.date_vvod as _date_vvod, "
                    + "	list.date_ins as _date_ins, sum(round(list_item.kolvo*round(spec_item.vnorm::numeric," + UtilZPlata.ROUNDING_NORM + "), 2)) as summ  "
                    + " From list, list_item, dept, employees, s_model, s_brig , spec_item, spec "
                    + " Where list_item.id_employe = employees.id "
                    + "           and list.id = list_item.id_list "
                    + "           and list.id_spec = spec.id  "
                    + "           and spec.id = spec_item.id_spec "
                    + "           and spec_item.id = list_item.id_spec_item "
                    + "           and list.id_model = s_model.model  "
                    + "           and list.id_dept = dept.id "
                    + "           and list.id_brig = s_brig.id "
                    + "           and list.status <> -1 "
                    + "           and employees.id = ? "
                    + "           and spec_item.category = ? "
                    + (idDept != -1 ? " and dept.id =" + idDept + " " : " ")
                    + (idBrig != -1 ? " and s_brig.id =" + idBrig + " " : " ")
                    + "           and date_vvod between ? and ? "
                    + " Group by idDept, department, idBrig, kodBrig, idList, model, marshlist, _date_vvod, _date_ins "
                    + " Order by _date_vvod, model, idList ";

            ps = conn.prepareStatement(sql);
            ps.setInt(1, idEmploye);
            ps.setInt(2, razryad);
            ps.setDate(3, new java.sql.Date(sDate));
            ps.setDate(4, new java.sql.Date(eDate));
            rs = ps.executeQuery();

            while (rs.next()) {
                Vector tmp = new Vector();
                tmp.add(rs.getInt("idDept"));
                tmp.add(rs.getString("department"));
                tmp.add(rs.getInt("idBrig"));
                tmp.add(rs.getInt("kodBrig"));
                tmp.add(rs.getInt("idList"));
                tmp.add(rs.getInt("model"));
                tmp.add(rs.getString("marshlist"));
                tmp.add(new SimpleDateFormat("dd-MM-yyyy").format(rs.getDate("_date_vvod")));
                tmp.add(new SimpleDateFormat("dd-MM-yyyy").format(rs.getDate("_date_ins")));
                tmp.add(new Double(UtilZPlata.formatNorm(rs.getDouble("summ"), 2)));
                elements.add(tmp);
            }

        } catch (Exception e) {
            elements = new Vector();
            log.severe("Ошибка getDetalProductionEmployees()" + e);
        }
        return elements;
    }

    public Vector getDetalProductionBrig(int idDept, int idBrig, long sDate, long eDate) throws Exception {
        Vector elements = new Vector();
        String sql = "";

        try {
            sql = "Select dept.id as idDept, department, s_brig.id as idBrig, s_brig.kod as kodBrig, list.id as idList, model, marshlist, list.date_vvod as _date_vvod, "
                    + "	list.date_ins as _date_ins, sum(round(list_item.kolvo*round(spec_item.vnorm::numeric," + UtilZPlata.ROUNDING_NORM + "), 2)) as summ  "
                    + " From list, list_item, dept, employees, s_model, s_brig , spec_item, spec "
                    + " Where list_item.id_employe = employees.id "
                    + "           and list.id = list_item.id_list "
                    + "           and list.id_spec = spec.id  "
                    + "           and spec.id = spec_item.id_spec "
                    + "           and spec_item.id = list_item.id_spec_item "
                    + "           and list.id_model = s_model.model  "
                    + "           and list.id_dept = dept.id "
                    + "           and list.id_brig = s_brig.id "
                    + "           and list.status <> -1 "
                    + (idDept != -1 ? " and dept.id =" + idDept + " " : " ")
                    + (idBrig != -1 ? " and s_brig.id =" + idBrig + " " : " ")
                    + "           and date_vvod between ? and ? "
                    + " Group by idDept, department, idBrig, kodBrig, idList, model, marshlist, _date_vvod, _date_ins "
                    + " Order by _date_vvod, model, idList ";

            ps = conn.prepareStatement(sql);
            ps.setDate(1, new java.sql.Date(sDate));
            ps.setDate(2, new java.sql.Date(eDate));
            rs = ps.executeQuery();

            while (rs.next()) {
                Vector tmp = new Vector();
                tmp.add(rs.getInt("idDept"));
                tmp.add(rs.getString("department"));
                tmp.add(rs.getInt("idBrig"));
                tmp.add(rs.getInt("kodBrig"));
                tmp.add(rs.getInt("idList"));
                tmp.add(rs.getInt("model"));
                tmp.add(rs.getString("marshlist"));
                tmp.add(new SimpleDateFormat("dd-MM-yyyy").format(rs.getDate("_date_vvod")));
                tmp.add(new SimpleDateFormat("dd-MM-yyyy").format(rs.getDate("_date_ins")));
                tmp.add(new Double(UtilZPlata.formatNorm(rs.getDouble("summ"), 2)));
                elements.add(tmp);
            }

        } catch (Exception e) {
            elements = new Vector();
            log.severe("Ошибка getDetalProductionBrig()" + e);
        }
        return elements;
    }

    public Vector getDetalProductionModel(int idDept, int idBrig, int model, long sDate, long eDate) throws Exception {
        Vector elements = new Vector();
        String sql = "";

        try {
            sql = "Select dept.id as idDept, department, s_brig.id as idBrig, s_brig.kod as kodBrig, list.id as idList, model, marshlist, list.date_vvod as _date_vvod, "
                    + "	list.date_ins as _date_ins, sum(round(list_item.kolvo*round(spec_item.vnorm::numeric," + UtilZPlata.ROUNDING_NORM + "), 2)) as summ  "
                    + " From list, list_item, dept, employees, s_model, s_brig , spec_item, spec "
                    + " Where list_item.id_employe = employees.id "
                    + "           and list.id = list_item.id_list "
                    + "           and list.id_spec = spec.id  "
                    + "           and spec.id = spec_item.id_spec "
                    + "           and spec_item.id = list_item.id_spec_item "
                    + "           and list.id_model = s_model.model  "
                    + "           and list.id_dept = dept.id "
                    + "           and list.id_brig = s_brig.id "
                    + "           and list.status <> -1 "
                    + "           and s_model.model = ? "
                    + (idDept != -1 ? " and dept.id =" + idDept + " " : " ")
                    + (idBrig != -1 ? " and s_brig.id =" + idBrig + " " : " ")
                    + "           and date_vvod between ? and ? "
                    + " Group by idDept, department, idBrig, kodBrig, idList, model, marshlist, _date_vvod, _date_ins "
                    + " Order by _date_vvod, model, idList ";

            ps = conn.prepareStatement(sql);
            ps.setInt(1, model);
            ps.setDate(2, new java.sql.Date(sDate));
            ps.setDate(3, new java.sql.Date(eDate));
            rs = ps.executeQuery();

            while (rs.next()) {
                Vector tmp = new Vector();
                tmp.add(rs.getInt("idDept"));
                tmp.add(rs.getString("department"));
                tmp.add(rs.getInt("idBrig"));
                tmp.add(rs.getInt("kodBrig"));
                tmp.add(rs.getInt("idList"));
                tmp.add(rs.getInt("model"));
                tmp.add(rs.getString("marshlist"));
                tmp.add(new SimpleDateFormat("dd-MM-yyyy").format(rs.getDate("_date_vvod")));
                tmp.add(new SimpleDateFormat("dd-MM-yyyy").format(rs.getDate("_date_ins")));
                tmp.add(new Double(UtilZPlata.formatNorm(rs.getDouble("summ"), 2)));
                elements.add(tmp);
            }

        } catch (Exception e) {
            elements = new Vector();
            log.severe("Ошибка getDetalProductionModel()" + e);
        }
        return elements;
    }

    public Vector getDetalProductionMarshrut(int idDept, int idBrig, String marshrut, long sDate, long eDate) throws Exception {
        Vector elements = new Vector();
        String sql = "";

        try {
            sql = "Select dept.id as idDept, department, s_brig.id as idBrig, s_brig.kod as kodBrig, list.id as idList, model, marshlist, list.date_vvod as _date_vvod, "
                    + "	list.date_ins as _date_ins, sum(round(list_item.kolvo*round(spec_item.vnorm::numeric," + UtilZPlata.ROUNDING_NORM + "), 2)) as summ  "
                    + " From list, list_item, dept, employees, s_model, s_brig , spec_item, spec "
                    + " Where list_item.id_employe = employees.id "
                    + "           and list.id = list_item.id_list "
                    + "           and list.id_spec = spec.id  "
                    + "           and spec.id = spec_item.id_spec "
                    + "           and spec_item.id = list_item.id_spec_item "
                    + "           and list.id_model = s_model.model  "
                    + "           and list.id_dept = dept.id "
                    + "           and list.id_brig = s_brig.id "
                    + "           and list.status <> -1 "
                    + "           and list.marshlist = ? "
                    + (idDept != -1 ? " and dept.id =" + idDept + " " : " ")
                    + (idBrig != -1 ? " and s_brig.id =" + idBrig + " " : " ")
                    + "           and date_vvod between ? and ? "
                    + " Group by idDept, department, idBrig, kodBrig, idList, model, marshlist, _date_vvod, _date_ins "
                    + " Order by _date_vvod, model, idList ";

            ps = conn.prepareStatement(sql);
            ps.setString(1, marshrut);
            ps.setDate(2, new java.sql.Date(sDate));
            ps.setDate(3, new java.sql.Date(eDate));
            rs = ps.executeQuery();

            while (rs.next()) {
                Vector tmp = new Vector();
                tmp.add(rs.getInt("idDept"));
                tmp.add(rs.getString("department"));
                tmp.add(rs.getInt("idBrig"));
                tmp.add(rs.getInt("kodBrig"));
                tmp.add(rs.getInt("idList"));
                tmp.add(rs.getInt("model"));
                tmp.add(rs.getString("marshlist"));
                tmp.add(new SimpleDateFormat("dd-MM-yyyy").format(rs.getDate("_date_vvod")));
                tmp.add(new SimpleDateFormat("dd-MM-yyyy").format(rs.getDate("_date_ins")));
                tmp.add(new Double(UtilZPlata.formatNorm(rs.getDouble("summ"), 2)));
                elements.add(tmp);
            }

        } catch (Exception e) {
            elements = new Vector();
            log.severe("Ошибка getDetalProductionMarshrut()" + e);
        }
        return elements;
    }

    public Vector getDetalKolvoModel(int idDept, int idBrig, int model, long sVodDate, long eVodDate) throws Exception {
        Vector elements = new Vector();
        String sql = "";

        try {
            sql = "Select t3.idDept as idDept, t3.department as department, t3.idBrig as idBrig, t3.kodBrig as kodBrig, t3.idList as idList, "
                    + "         t3.model as model, t3.marshlist as marshlist, t3.kol as kol, t3.kol_ch as kol_ch, t4.kol_br as kol_br "
                    + " From (Select t1.idDept, t1.department, t1.idBrig, t1.kodBrig, t1.idList, t1.model, t1.marshlist, kol, kol_ch "
                    + "	From(Select dept.id as idDept, department, s_brig.id as idBrig, s_brig.kod as kodBrig, list.id as idList, model, marshlist, sum(kol) as kol "
                    + "			 From list, dept, s_model, s_brig "
                    + "			 Where list.id_model = s_model.model and "
                    + "			       list.id_dept = dept.id and "
                    + "			       list.id_brig = s_brig.id  and "
                    + "                            s_model.model = ? and "
                    + "                            list.status <> -1 "
                    + (idBrig != -1 ? " and list.id_brig = " + idBrig + " " : " ")
                    + (idDept != -1 ? " and list.id_dept = " + idDept + " " : " ")
                    + "                            and date_vvod between '" + new java.sql.Date(sVodDate) + "' "
                    + "                                              and '" + new java.sql.Date(eVodDate) + "' "
                    + "			 Group by idDept, department, idBrig, kodBrig, idList, model, marshlist ) as t1 "
                    + "	left join (Select dept.id as idDept, department, s_brig.id as idBrig, s_brig.kod as kodBrig, list.id as idList, model, marshlist, sum(list_item.kolvo) as kol_ch "
                    + "			 From list, list_item, dept, s_model, s_brig "
                    + "			 Where list.id = list_item.id_list and "
                    + "			       list.id_model = s_model.model and "
                    + "			       list.id_dept = dept.id and  "
                    + "			       list.id_brig = s_brig.id  and "
                    + "			       list_item.nvflag = true and"
                    + "			       list_item.id_employe <> -1 and "
                    + "                            s_model.model = ? and "
                    + "                            list.status <> -1 "
                    + (idBrig != -1 ? " and list.id_brig = " + idBrig + " " : " ")
                    + (idDept != -1 ? " and list.id_dept = " + idDept + " " : " ")
                    + "                            and date_vvod between '" + new java.sql.Date(sVodDate) + "' "
                    + "                                              and '" + new java.sql.Date(eVodDate) + "' "
                    + "			 Group by idDept, department, idBrig, kodBrig, idList, model, marshlist) as t2 "
                    + "	on t1.idDept = t2.idDept and "
                    + "	   t1.idBrig = t2.idBrig and "
                    + "	   t1.kodBrig = t2.kodBrig and "
                    + "	   t1.idList = t2.idList and "
                    + "	   t1.model = t2.model and "
                    + "	   t1.marshlist = t2.marshlist "
                    + "	Order by t1.department, t1.idBrig, t1.model, t1.marshlist ) as t3 "
                    + " left join (Select dept.id as idDept, department, s_brig.id as idBrig, s_brig.kod as kodBrig, list.id as idList, model, marshlist, sum(list_item.kolvo) as kol_br "
                    + "			 From list, list_item, dept, s_model, s_brig "
                    + "			 Where list.id = list_item.id_list and "
                    + "			       list.id_model = s_model.model and "
                    + "			       list.id_dept = dept.id and "
                    + "			       list.id_brig = s_brig.id  and "
                    // + "			       list_item.nvflag = true and "
                    + "			       list_item.id_employe = -1 and "
                    + "                            s_model.model = ? and "
                    + "                            list.status <> -1 "
                    + (idBrig != -1 ? " and list.id_brig = " + idBrig + " " : " ")
                    + (idDept != -1 ? " and list.id_dept = " + idDept + " " : " ")
                    + "                            and date_vvod between '" + new java.sql.Date(sVodDate) + "' "
                    + "                                              and '" + new java.sql.Date(eVodDate) + "' "
                    + "			 Group by idDept, department, idBrig, kodBrig, idList, model, marshlist) as t4 "
                    + " on t3.idDept = t4.idDept and "
                    + "   t3.idBrig = t4.idBrig and "
                    + "   t3.kodBrig = t4.kodBrig and "
                    + "   t3.idList = t4.idList and "
                    + "   t3.model = t4.model and "
                    + "   t3.marshlist = t4.marshlist "
                    + " Order by idDept, department, kodBrig, idList, model, marshlist ";

            ps = conn.prepareStatement(sql);
            ps.setInt(1, model);
            ps.setInt(2, model);
            ps.setInt(3, model);
            rs = ps.executeQuery();

            while (rs.next()) {
                Vector tmp = new Vector();
                tmp.add(rs.getInt("idDept"));
                tmp.add(rs.getString("department"));
                tmp.add(rs.getInt("idBrig"));
                tmp.add(rs.getInt("kodBrig"));
                tmp.add(rs.getInt("idList"));
                tmp.add(rs.getInt("model"));
                tmp.add(rs.getString("marshlist"));
                tmp.add(rs.getInt("kol"));
                tmp.add(rs.getInt("kol_ch"));
                tmp.add(rs.getInt("kol_br"));
                elements.add(tmp);
            }

        } catch (Exception e) {
            elements = new Vector();
            log.severe("Ошибка getDetalKolvoModel()" + e);
        }
        return elements;
    }

    public Vector getDetalKolvoMarshrut(int idDept, int idBrig, String marshrut, long sVodDate, long eVodDate) throws Exception {
        Vector elements = new Vector();
        String sql = "";

        try {
            sql = "Select t3.idDept as idDept, t3.department as department, t3.idBrig as idBrig, t3.kodBrig as kodBrig, t3.idList as idList, "
                    + "         t3.model as model, t3.marshlist as marshlist, t3.kol as kol, t3.kol_ch as kol_ch, t4.kol_br as kol_br "
                    + " From (Select t1.idDept, t1.department, t1.idBrig, t1.kodBrig, t1.idList, t1.model, t1.marshlist, kol, kol_ch "
                    + "	From(Select dept.id as idDept, department, s_brig.id as idBrig, s_brig.kod as kodBrig, list.id as idList, model, marshlist, sum(kol) as kol "
                    + "			 From list, dept, s_model, s_brig "
                    + "			 Where list.id_model = s_model.model and "
                    + "			       list.id_dept = dept.id and "
                    + "			       list.id_brig = s_brig.id  and "
                    + "                            list.marshlist = ? and "
                    + "                            list.status <> -1 "
                    + (idBrig != -1 ? " and list.id_brig = " + idBrig + " " : " ")
                    + (idDept != -1 ? " and list.id_dept = " + idDept + " " : " ")
                    + "                            and date_vvod between '" + new java.sql.Date(sVodDate) + "' "
                    + "                                              and '" + new java.sql.Date(eVodDate) + "' "
                    + "			 Group by idDept, department, idBrig, kodBrig, idList, model, marshlist ) as t1 "
                    + "	left join (Select dept.id as idDept, department, s_brig.id as idBrig, s_brig.kod as kodBrig, list.id as idList, model, marshlist, sum(list_item.kolvo) as kol_ch "
                    + "			 From list, list_item, dept, s_model, s_brig "
                    + "			 Where list.id = list_item.id_list and "
                    + "			       list.id_model = s_model.model and "
                    + "			       list.id_dept = dept.id and  "
                    + "			       list.id_brig = s_brig.id  and "
                    + "			       list_item.nvflag = true and"
                    + "			       list_item.id_employe <> -1 and "
                    + "                            list.marshlist = ? and "
                    + "                            list.status <> -1 "
                    + (idBrig != -1 ? " and list.id_brig = " + idBrig + " " : " ")
                    + (idDept != -1 ? " and list.id_dept = " + idDept + " " : " ")
                    + "                            and date_vvod between '" + new java.sql.Date(sVodDate) + "' "
                    + "                                              and '" + new java.sql.Date(eVodDate) + "' "
                    + "			 Group by idDept, department, idBrig, kodBrig, idList, model, marshlist) as t2 "
                    + "	on t1.idDept = t2.idDept and "
                    + "	   t1.idBrig = t2.idBrig and "
                    + "	   t1.kodBrig = t2.kodBrig and "
                    + "	   t1.idList = t2.idList and "
                    + "	   t1.model = t2.model and "
                    + "	   t1.marshlist = t2.marshlist "
                    + "	Order by t1.department, t1.idBrig, t1.model, t1.marshlist ) as t3 "
                    + " left join (Select dept.id as idDept, department, s_brig.id as idBrig, s_brig.kod as kodBrig, list.id as idList, model, marshlist, sum(list_item.kolvo) as kol_br "
                    + "			 From list, list_item, dept, s_model, s_brig "
                    + "			 Where list.id = list_item.id_list and "
                    + "			       list.id_model = s_model.model and "
                    + "			       list.id_dept = dept.id and "
                    + "			       list.id_brig = s_brig.id  and "
                    // + "			       list_item.nvflag = true and "
                    + "			       list_item.id_employe = -1 and "
                    + "                            list.marshlist = ? and "
                    + "                            list.status <> -1 "
                    + (idBrig != -1 ? " and list.id_brig = " + idBrig + " " : " ")
                    + (idDept != -1 ? " and list.id_dept = " + idDept + " " : " ")
                    + "                            and date_vvod between '" + new java.sql.Date(sVodDate) + "' "
                    + "                                              and '" + new java.sql.Date(eVodDate) + "' "
                    + "			 Group by idDept, department, idBrig, kodBrig, idList, model, marshlist) as t4 "
                    + " on t3.idDept = t4.idDept and "
                    + "   t3.idBrig = t4.idBrig and "
                    + "   t3.kodBrig = t4.kodBrig and "
                    + "   t3.idList = t4.idList and "
                    + "   t3.model = t4.model and "
                    + "   t3.marshlist = t4.marshlist "
                    + " Order by idDept, department, kodBrig, idList, model, marshlist ";

            ps = conn.prepareStatement(sql);
            ps.setString(1, marshrut);
            ps.setString(2, marshrut);
            ps.setString(3, marshrut);
            rs = ps.executeQuery();

            while (rs.next()) {
                Vector tmp = new Vector();
                tmp.add(rs.getInt("idDept"));
                tmp.add(rs.getString("department"));
                tmp.add(rs.getInt("idBrig"));
                tmp.add(rs.getInt("kodBrig"));
                tmp.add(rs.getInt("idList"));
                tmp.add(rs.getInt("model"));
                tmp.add(rs.getString("marshlist"));
                tmp.add(rs.getInt("kol"));
                tmp.add(rs.getInt("kol_ch"));
                tmp.add(rs.getInt("kol_br"));
                elements.add(tmp);
            }

        } catch (Exception e) {
            elements = new Vector();
            log.severe("Ошибка getDetalKolvoMarshrut()" + e);
        }
        return elements;
    }

    public Vector getDetalKolvoErrorList(int idDept, int idBrig, long sVodDate, long eVodDate) throws Exception {
        Vector elements = new Vector();
        String sql = "";

        try {
            sql = "Select idDept, department, idBrig, kodBrig, idList, idSpec, model, marshlist, _date_vvod, _date_ins, summ, summ_ "
                    + " From(Select idDept, department, idBrig, kodBrig, idList, idSpec, model, marshlist, _date_vvod, _date_ins, summ, kolList, sum(round(kolList*round(spec_item.vnorm::numeric," + UtilZPlata.ROUNDING_NORM + "), 2)) as summ_ "
                    + "     From (Select dept.id as idDept, department, s_brig.id as idBrig, s_brig.kod as kodBrig, list.id as idList, list.id_spec as idSpec, model, marshlist, list.date_vvod as _date_vvod, "
                    + "			list.date_ins as _date_ins, sum(round(list_item.kolvo*round(spec_item.vnorm::numeric," + UtilZPlata.ROUNDING_NORM + "), 2)) as summ, list.kol as kolList"
                    + "		    From list, list_item, dept, employees, s_model, s_brig , spec_item, spec  "
                    + "		    Where list_item.id_employe = employees.id "
                    + "			and list.id = list_item.id_list "
                    + "			and list.id_spec = spec.id "
                    + "			and spec.id = spec_item.id_spec "
                    + "			and spec_item.id = list_item.id_spec_item "
                    + "			and list.id_model = s_model.model "
                    + "			and list.id_dept = dept.id "
                    + "			and list.id_brig = s_brig.id "
                    + "			and list.status <> -1    "
                    + (idBrig != -1 ? " and list.id_brig = " + idBrig + " " : " ")
                    + (idDept != -1 ? " and list.id_dept = " + idDept + " " : " ")
                    + "                 and list.date_vvod between '" + new java.sql.Date(sVodDate) + "' "
                    + "                                        and '" + new java.sql.Date(eVodDate) + "' "
                    + "			Group by idDept, department, idBrig, kodBrig, idList, idSpec, model, marshlist, _date_vvod, _date_ins, kolList  "
                    + "			Order by _date_vvod, idSpec, model, idList, kolList) as t1, spec, spec_item"
                    + "     Where spec.id = spec_item.id_spec "
                    + "             and spec.id = idSpec  "
                    + "     Group by idDept, department, idBrig, kodBrig, idList, idSpec, model, marshlist, _date_vvod, _date_ins, summ, kolList) as t2 "
                    + " Where summ - summ_ >0.2 or summ_ - summ >0.2		 ";

            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                Vector tmp = new Vector();
                tmp.add(rs.getInt("idDept"));
                tmp.add(rs.getString("department"));
                tmp.add(rs.getInt("idBrig"));
                tmp.add(rs.getInt("kodBrig"));
                tmp.add(rs.getInt("idList"));
                tmp.add(rs.getInt("model"));
                tmp.add(rs.getString("marshlist"));
                tmp.add(new SimpleDateFormat("dd-MM-yyyy").format(rs.getDate("_date_vvod")));
                tmp.add(new Double(UtilZPlata.formatNorm(rs.getDouble("summ"), 2)));
                tmp.add(new Double(UtilZPlata.formatNorm(rs.getDouble("summ_"), 2)));
                elements.add(tmp);
            }

        } catch (Exception e) {
            elements = new Vector();
            log.severe("Ошибка getDetalKolvoErrorList()" + e);
        }
        return elements;
    }

    public Vector getDetalProductionEmployees(int idList, int idEmployer, int razryad) throws Exception {
        Vector elements = new Vector();
        String sql = "";

        try {
            sql = "Select dept.id as idDept, department, s_brig.id as idBrig, s_brig.kod as kodBrig, list.id as idList, model, marshlist, list.date_vvod as _date_vvod, "
                    + "	list.date_ins as _date_ins, sum(round(list_item.kolvo*round(spec_item.vnorm::numeric," + UtilZPlata.ROUNDING_NORM + "), 2)) as summ  "
                    + " From list, list_item, dept, employees, s_model, s_brig , spec_item, spec "
                    + " Where list_item.id_employe = employees.id "
                    + "           and list.id = ? "
                    + "           and employees.id = ?  "
                    + "           and spec_item.category = ?  "
                    + "           and list.id = list_item.id_list "
                    + "           and list.id_spec = spec.id  "
                    + "           and spec.id = spec_item.id_spec "
                    + "           and spec_item.id = list_item.id_spec_item "
                    + "           and list.id_model = s_model.model  "
                    + "           and list.id_dept = dept.id "
                    + "           and list.id_brig = s_brig.id "
                    + "           and list.status <> -1 "
                    + " Group by idDept, department, idBrig, kodBrig, idList, model, marshlist, _date_vvod, _date_ins "
                    + " Order by _date_vvod, model, idList ";

            ps = conn.prepareStatement(sql);
            ps.setInt(1, idList);
            ps.setInt(2, idEmployer);
            ps.setInt(3, razryad);
            rs = ps.executeQuery();

            while (rs.next()) {
                elements.add(rs.getInt("idDept"));
                elements.add(rs.getString("department"));
                elements.add(rs.getInt("idBrig"));
                elements.add(rs.getInt("kodBrig"));
                elements.add(rs.getInt("idList"));
                elements.add(rs.getInt("model"));
                elements.add(rs.getString("marshlist"));
                elements.add(new SimpleDateFormat("dd-MM-yyyy").format(rs.getDate("_date_vvod")));
                elements.add(new SimpleDateFormat("dd-MM-yyyy").format(rs.getDate("_date_ins")));
                elements.add(new Double(UtilZPlata.formatNorm(rs.getDouble("summ"), 2)));
            }

        } catch (Exception e) {
            elements = new Vector();
            log.severe("Ошибка getDetalProductionEmployees()" + e);
        }
        return elements;
    }

    public Vector getDetalProduction(int idList) throws Exception {
        Vector elements = new Vector();
        String sql = "";

        try {
            sql = "Select dept.id as idDept, department, s_brig.id as idBrig, s_brig.kod as kodBrig, list.id as idList, model, marshlist, list.date_vvod as _date_vvod, "
                    + "	list.date_ins as _date_ins, sum(round(list_item.kolvo*round(spec_item.vnorm::numeric," + UtilZPlata.ROUNDING_NORM + "), 2)) as summ  "
                    + " From list, list_item, dept, employees, s_model, s_brig , spec_item, spec "
                    + " Where list_item.id_employe = employees.id "
                    + "           and list.id = ? "
                    + "           and list.id = list_item.id_list "
                    + "           and list.id_spec = spec.id  "
                    + "           and spec.id = spec_item.id_spec "
                    + "           and spec_item.id = list_item.id_spec_item "
                    + "           and list.id_model = s_model.model  "
                    + "           and list.id_dept = dept.id "
                    + "           and list.id_brig = s_brig.id "
                    + "           and list.status <> -1 "
                    + " Group by idDept, department, idBrig, kodBrig, idList, model, marshlist, _date_vvod, _date_ins "
                    + " Order by _date_vvod, model, idList ";

            ps = conn.prepareStatement(sql);
            ps.setInt(1, idList);
            rs = ps.executeQuery();

            while (rs.next()) {
                elements.add(rs.getInt("idDept"));
                elements.add(rs.getString("department"));
                elements.add(rs.getInt("idBrig"));
                elements.add(rs.getInt("kodBrig"));
                elements.add(rs.getInt("idList"));
                elements.add(rs.getInt("model"));
                elements.add(rs.getString("marshlist"));
                elements.add(new SimpleDateFormat("dd-MM-yyyy").format(rs.getDate("_date_vvod")));
                elements.add(new SimpleDateFormat("dd-MM-yyyy").format(rs.getDate("_date_ins")));
                elements.add(new Double(UtilZPlata.formatNorm(rs.getDouble("summ"), 2)));
            }

        } catch (Exception e) {
            elements = new Vector();
            log.severe("Ошибка getDetalProduction()" + e);
        }
        return elements;
    }

    public Vector getDetalKolvoErrorList(int idList) throws Exception {
        Vector elements = new Vector();
        String sql = "";

        try {
            sql = "Select idDept, department, idBrig, kodBrig, idList, idSpec, model, marshlist, _date_vvod, _date_ins, summ, summ_ "
                    + " From(Select idDept, department, idBrig, kodBrig, idList, idSpec, model, marshlist, _date_vvod, _date_ins, summ, kolList, sum(round(kolList*round(spec_item.vnorm::numeric," + UtilZPlata.ROUNDING_NORM + "), 2)) as summ_ "
                    + "     From (Select dept.id as idDept, department, s_brig.id as idBrig, s_brig.kod as kodBrig, list.id as idList, list.id_spec as idSpec, model, marshlist, list.date_vvod as _date_vvod, "
                    + "			list.date_ins as _date_ins, sum(round(list_item.kolvo*round(spec_item.vnorm::numeric," + UtilZPlata.ROUNDING_NORM + "), 2)) as summ, list.kol as kolList"
                    + "		    From list, list_item, dept, employees, s_model, s_brig , spec_item, spec  "
                    + "		    Where list_item.id_employe = employees.id "
                    + "			and list.id = list_item.id_list "
                    + "			and list.id_spec = spec.id "
                    + "			and spec.id = spec_item.id_spec "
                    + "			and spec_item.id = list_item.id_spec_item "
                    + "			and list.id_model = s_model.model "
                    + "			and list.id_dept = dept.id "
                    + "			and list.id_brig = s_brig.id "
                    + "			and list.status <> -1 "
                    + "                 and list.id = ? "
                    + "			Group by idDept, department, idBrig, kodBrig, idList, idSpec, model, marshlist, _date_vvod, _date_ins, kolList  "
                    + "			Order by _date_vvod, idSpec, model, idList, kolList) as t1, spec, spec_item"
                    + "     Where spec.id = spec_item.id_spec "
                    + "             and spec.id = idSpec  "
                    + "     Group by idDept, department, idBrig, kodBrig, idList, idSpec, model, marshlist, _date_vvod, _date_ins, summ, kolList) as t2 "
                    + " Where summ - summ_ >0.2 or summ_ - summ >0.2		 ";

            ps = conn.prepareStatement(sql);
            ps.setInt(1, idList);
            rs = ps.executeQuery();

            if (rs.next()) {
                elements.add(rs.getInt("idDept"));
                elements.add(rs.getString("department"));
                elements.add(rs.getInt("idBrig"));
                elements.add(rs.getInt("kodBrig"));
                elements.add(rs.getInt("idList"));
                elements.add(rs.getInt("model"));
                elements.add(rs.getString("marshlist"));
                elements.add(new SimpleDateFormat("dd-MM-yyyy").format(rs.getDate("_date_vvod")));
                elements.add(new Double(UtilZPlata.formatNorm(rs.getDouble("summ"), 2)));
                elements.add(new Double(UtilZPlata.formatNorm(rs.getDouble("summ_"), 2)));
            }

        } catch (Exception e) {
            elements = new Vector();
            log.severe("Ошибка getDetalKolvoErrorList()" + e);
        }
        return elements;
    }

    public Vector getDetalKolvo(int idList) throws Exception {
        Vector elements = new Vector();
        String sql = "";

        try {
            sql = "Select t3.idDept as idDept, t3.department as department, t3.idBrig as idBrig, t3.kodBrig as kodBrig, t3.idList as idList, "
                    + "         t3.model as model, t3.marshlist as marshlist, t3.kol as kol, t3.kol_ch as kol_ch, t4.kol_br as kol_br "
                    + " From (Select t1.idDept, t1.department, t1.idBrig, t1.kodBrig, t1.idList, t1.model, t1.marshlist, kol, kol_ch "
                    + "	From(Select dept.id as idDept, department, s_brig.id as idBrig, s_brig.kod as kodBrig, list.id as idList, model, marshlist, sum(kol) as kol "
                    + "			 From list, dept, s_model, s_brig "
                    + "			 Where list.id_model = s_model.model and "
                    + "			       list.id_dept = dept.id and "
                    + "			       list.id_brig = s_brig.id  and "
                    + "                            list.id = ? and "
                    + "                            list.status <> -1 "
                    + "			 Group by idDept, department, idBrig, kodBrig, idList, model, marshlist ) as t1 "
                    + "	left join (Select dept.id as idDept, department, s_brig.id as idBrig, s_brig.kod as kodBrig, list.id as idList, model, marshlist, sum(list_item.kolvo) as kol_ch "
                    + "			 From list, list_item, dept, s_model, s_brig "
                    + "			 Where list.id = list_item.id_list and "
                    + "			       list.id_model = s_model.model and "
                    + "			       list.id_dept = dept.id and  "
                    + "			       list.id_brig = s_brig.id  and "
                    + "			       list_item.nvflag = true and"
                    + "			       list_item.id_employe <> -1 and "
                    + "                            list.id = ? and "
                    + "                            list.status <> -1 "
                    + "			 Group by idDept, department, idBrig, kodBrig, idList, model, marshlist) as t2 "
                    + "	on t1.idDept = t2.idDept and "
                    + "	   t1.idBrig = t2.idBrig and "
                    + "	   t1.kodBrig = t2.kodBrig and "
                    + "	   t1.idList = t2.idList and "
                    + "	   t1.model = t2.model and "
                    + "	   t1.marshlist = t2.marshlist "
                    + "	Order by t1.department, t1.idBrig, t1.model, t1.marshlist ) as t3 "
                    + " left join (Select dept.id as idDept, department, s_brig.id as idBrig, s_brig.kod as kodBrig, list.id as idList, model, marshlist, sum(list_item.kolvo) as kol_br "
                    + "			 From list, list_item, dept, s_model, s_brig "
                    + "			 Where list.id = list_item.id_list and "
                    + "			       list.id_model = s_model.model and "
                    + "			       list.id_dept = dept.id and "
                    + "			       list.id_brig = s_brig.id  and "
                    //  + "			       list_item.nvflag = true and "
                    + "			       list_item.id_employe = -1 and "
                    + "                            list.id = ? and "
                    + "                            list.status <> -1 "
                    + "			 Group by idDept, department, idBrig, kodBrig, idList, model, marshlist) as t4 "
                    + " on t3.idDept = t4.idDept and "
                    + "   t3.idBrig = t4.idBrig and "
                    + "   t3.kodBrig = t4.kodBrig and "
                    + "   t3.idList = t4.idList and "
                    + "   t3.model = t4.model and "
                    + "   t3.marshlist = t4.marshlist "
                    + " Order by idDept, department, kodBrig, idList, model, marshlist ";

            ps = conn.prepareStatement(sql);
            ps.setInt(1, idList);
            ps.setInt(2, idList);
            ps.setInt(3, idList);
            rs = ps.executeQuery();

            while (rs.next()) {
                elements.add(rs.getInt("idDept"));
                elements.add(rs.getString("department"));
                elements.add(rs.getInt("idBrig"));
                elements.add(rs.getInt("kodBrig"));
                elements.add(rs.getInt("idList"));
                elements.add(rs.getInt("model"));
                elements.add(rs.getString("marshlist"));
                elements.add(rs.getInt("kol"));
                elements.add(rs.getInt("kol_ch"));
                elements.add(rs.getInt("kol_br"));
            }

        } catch (Exception e) {
            elements = new Vector();
            log.severe("Ошибка getDetalKolvo()" + e);
        }
        return elements;
    }

    public Vector getNesort(int idDept, int idBrig, long sVodDate, long eVodDate) throws Exception {
        Vector elements = new Vector();
        String sql = "";

        try {
            sql = "Select list_nesort.id as idNesort, list_nesort.id_dept as idDept, department, "
                    + "     list_nesort.id_brig as idBrig, s_brig.kod as kodBrig, list_nesort.id_employer as idEmployer, "
                    + "     fio, list_nesort.razryad as razryad, list_nesort.id_model as model, list_nesort.kol as kol, "
                    + "     list_nesort.date_vvod as date_vvod, list_nesort.date_ins as date_ins "
                    + " From list_nesort, dept, s_brig, employees "
                    + " Where list_nesort.id_dept = dept.id and "
                    + "	list_nesort.id_brig = s_brig.id and "
                    + "	list_nesort.id_employer = employees.id "
                    + (idBrig != -1 ? " and list_nesort.id_brig = " + idBrig + " " : " ")
                    + (idDept != -1 ? " and list_nesort.id_dept = " + idDept + " " : " ")
                    + "     and list_nesort.date_vvod between '" + new java.sql.Date(sVodDate) + "' "
                    + "                       and '" + new java.sql.Date(eVodDate) + "' "
                    + " Order by idDept, idBrig, fio, razryad, model ";

            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                Vector tmp = new Vector();
                tmp.add(false);
                tmp.add(rs.getInt("idNesort"));
                tmp.add(rs.getInt("idDept"));
                tmp.add(rs.getString("department"));
                tmp.add(rs.getInt("idBrig"));
                tmp.add(rs.getInt("kodBrig"));
                tmp.add(rs.getInt("idEmployer"));
                tmp.add(rs.getString("fio"));
                tmp.add(rs.getInt("razryad"));
                tmp.add(rs.getInt("model"));
                tmp.add(rs.getInt("kol"));
                tmp.add(UtilZPlata.formatNorm(Double.valueOf(rs.getInt("kol") * UtilZPlata.NESORT_KOF), 2));
                tmp.add(new SimpleDateFormat("dd-MM-yyyy").format(rs.getDate("date_vvod")));
                tmp.add(new SimpleDateFormat("dd-MM-yyyy").format(rs.getDate("date_ins")));
                elements.add(tmp);
            }

        } catch (Exception e) {
            elements = new Vector();
            log.severe("Ошибка getNesort()" + e);
        }
        return elements;
    }

    void addNesort(int idDept, int idBrig, int idBrigadir, int idEmployer, int razryad, int model, int kol, long sDate) throws Exception {
        String sql = "";

        try {
            sql = " Insert into list_nesort(id_dept, id_brig, id_employer_ins, id_employer, razryad, id_model, kol, date_vvod) "
                    + " values( ?, ?, ?, ?, ?, ?, ?, ?) ";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, idDept);
            ps.setInt(2, idBrig);
            ps.setInt(3, idBrigadir);
            ps.setInt(4, idEmployer);
            ps.setInt(5, razryad);
            ps.setInt(6, model);
            ps.setInt(7, kol);
            ps.setDate(8, new java.sql.Date(sDate));
            ps.execute();

        } catch (Exception e) {
            log.severe("Ошибка addNesort()" + e);
        }
    }

    void updateNesort(int idNesort, int idDept, int idBrig, int idBrigadir, int idEmployer, int razryad, int model, int kol, long sDate) throws Exception {
        String sql = "";

        try {
            sql = " Update list_nesort set id_dept = ?, id_brig = ?, id_employer_ins = ?, id_employer = ?, "
                    + "                    razryad  = ?, id_model  = ?, kol = ?, date_vvod  = ? Where id = ? ";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, idDept);
            ps.setInt(2, idBrig);
            ps.setInt(3, idBrigadir);
            ps.setInt(4, idEmployer);
            ps.setInt(5, razryad);
            ps.setInt(6, model);
            ps.setInt(7, kol);
            ps.setDate(8, new java.sql.Date(sDate));
            ps.setInt(9, idNesort);
            ps.execute();

        } catch (Exception e) {
            log.severe("Ошибка updateNesort()" + e);
        }
    }

    public void deleteNesort(int idNesort) throws Exception {
        String sql = " Delete From list_nesort Where id = ?";

        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, idNesort);
            ps.executeUpdate();

        } catch (Exception e) {
            log.severe("Ошибка deleteList()" + e);
        }
    }

    public boolean addImportReportVedtT4(ItemT4 data) throws Exception {
        String sql = "";
        boolean rezalt = false;
        int id;

        ZPlataDBImport impdb;

        try {
            setAutoCommit(false);

            impdb = new ZPlataDBImport();

            // Удаляем данные из mssql за выбранный период и бригаду
            impdb.deleteItemsDataReport(data.getDate(), data.getBrig());

            // Удаляем данные из postgressql за выбранный период и бригаду
            sql = "DELETE FROM list_report_item "
                    + " WHERE id_list_report in "
                    + "                 ( Select id  "
                    + "                   From list_report "
                    + "                   Where dept = ? and brig = ? and date = ?  )";

            ps = conn.prepareStatement(sql);
            ps.setString(1, data.getDept());
            ps.setString(2, data.getBrig());
            ps.setDate(3, new java.sql.Date(data.getDate()));
            ps.execute();

            sql = "DELETE FROM list_report "
                    + " WHERE dept = ? and brig = ? and date = ? ";

            ps = conn.prepareStatement(sql);
            ps.setString(1, data.getDept());
            ps.setString(2, data.getBrig());
            ps.setDate(3, new java.sql.Date(data.getDate()));
            ps.execute();

            // Добавляем строку с основными параметрами ведомости т4 в postgressql
            sql = "INSERT INTO list_report("
                    + "            dept, brig, period, work_smen, work_clock, work_day, date)"
                    + "    VALUES (?, ?, ?, ?, ?, ?, ?) returning id ";

            ps = conn.prepareStatement(sql);
            ps.setString(1, data.getDept());
            ps.setString(2, data.getBrig());
            ps.setString(3, data.getPeriod());
            ps.setString(4, data.getWorkSmen().toString());
            ps.setString(5, data.getWorkClock().toString());
            ps.setString(6, data.getWorkDay().toString());
            ps.setDate(7, new java.sql.Date(data.getDate()));
            rs = ps.executeQuery();

            if (rs.next()) {

                id = rs.getInt(1);

                sql = "INSERT INTO list_report_item("
                        + "            id_list_report, tabelnum, fio, prof, razryad, datatabel, "
                        + "            vyrbrig, vyritog, procent, stoim, clvech, dnoch, clnoch, dotrab, clotrab, "
                        + "            vyrx2, dvnedr, clvnedr, dtarif, cltarif, dsredn, clsredn, vrednost, "
                        + "            stoimplus, stoimminus, kofuch, vyrdk, nesort, dtarif23, cltarif23, "
                        + "            dotpusk, dblist, dperehod, dsotpusk, ddmateri, dmk, vyrosvoen, prem_percent, percent_minus)"
                        + "    VALUES (?, ?, ?, ?, ?, ?, "
                        + "            ?, ?, ?, ?, ?, ?, ?, ?, "
                        + "            ?, ?, ?, ?, ?, ?, ?, ?, "
                        + "            ?, ?, ?, ?, ?, ?, ?, "
                        + "            ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

                for (Object obj : data.getDataPeople()) {
                    ItemBuh temp = (ItemBuh) obj;

                    // Добавляем строку - содержание ведомости т4 в mssql

                    if (!temp.getFio().startsWith("(")) {

                        // процент выполнения
                        if (temp.getProcent() > 0) {
                            impdb.importItemDataReport(data.getDate(),                  // дата
                                    temp.getTabelNum(),             // табельный номер
                                    -9,                             // код операции из бух.программы
                                    temp.getProcent(),              // arg процент выполнения
                                    0,                              // arg2
                                    0,                              // hours
                                    0,                              // days
                                    0,                              // сумма -
                                    data.getBrig(),
                                    temp.getProf());
                        }

                        // процент премирования
                        if (temp.getPercent() > 0) {
                            impdb.importItemDataReport(data.getDate(),                  // дата
                                    temp.getTabelNum(),             // табельный номер
                                    136,                             // код операции из бух.программы
                                    temp.getPercent(),              // arg процент премирования
                                    0,                              // arg2
                                    0,                              // hours
                                    0,                              // days
                                    0,                              // сумма -
                                    data.getBrig(),
                                    temp.getProf());
                        }

                        // отработанные дни часы
                        if (temp.getClOtrab() > 0 || temp.getdOtrab() > 0) {
                            impdb.importItemDataReport(data.getDate(),                  // дата
                                    temp.getTabelNum(),             // табельный номер
                                    -3,                             // код операции из бух.программы
                                    0,                              // arg
                                    0,                              // arg2
                                    temp.getClOtrab(),              // hours отр. часы
                                    temp.getdOtrab(),               // days отр. дни
                                    0,                              // сумма -
                                    data.getBrig(),
                                    temp.getProf());
                        }

                        // Перевод дни
                        if (temp.getdPerevod() > 0) {
                            impdb.importItemDataReport(data.getDate(),                  // дата
                                    temp.getTabelNum(),             // табельный номер
                                    -1,                             // код операции из бух.программы
                                    0,                              // arg
                                    0,                              // arg2
                                    0,                              // hours
                                    temp.getdPerevod(),             // days перевод дни
                                    0,                              // сумма -
                                    data.getBrig(),
                                    temp.getProf());
                        }

                        // Итоговая выработка
                        if (temp.getVyrItog() > 0) {
                            impdb.importItemDataReport(data.getDate(),                  // дата
                                    temp.getTabelNum(),             // табельный номер
                                    13,                             // код операции из бух.программы
                                    temp.getStoim(),                // arg стоимость
                                    temp.getRazryad(),              // arg2 разряд
                                    temp.getVyrItog(),              // hours выработка
                                    0,                              // days -
                                    0,                              // сумма -
                                    data.getBrig(),
                                    temp.getProf());
                        }

                        // вечернее время дни часы
                        if (temp.getClVech() > 0) {
                            impdb.importItemDataReport(data.getDate(),                  // дата
                                    temp.getTabelNum(),             // табельный номер
                                    31,                             // код операции из бух.программы
                                    0,                              // arg
                                    0,                              // arg2
                                    temp.getClVech(),               // hours веч. часы
                                    0,                              // days
                                    0,                              // сумма -
                                    data.getBrig(),
                                    temp.getProf());
                        }

                        // ноч. дни
                        if (temp.getClNoch() > 0 || temp.getdNoch() > 0) {
                            impdb.importItemDataReport(data.getDate(),                  // дата
                                    temp.getTabelNum(),             // табельный номер
                                    32,                             // код операции из бух.программы
                                    0,                              // arg
                                    0,                              // arg2
                                    temp.getdNoch(),                // hours ноч. дни
                                    0,                              // days
                                    0,                              // сумма -
                                    data.getBrig(),
                                    temp.getProf());
                        }

                        // ноч. часы
                        if (temp.getClNoch() > 0 || temp.getdNoch() > 0) {
                            impdb.importItemDataReport(data.getDate(),                  // дата
                                    temp.getTabelNum(),             // табельный номер
                                    134,                            // код операции из бух.программы
                                    0,                              // arg
                                    0,                              // arg2
                                    temp.getClNoch(),               // hours ноч. часы
                                    0,                              // days
                                    0,                              // сумма -
                                    data.getBrig(),
                                    temp.getProf());
                        }

                        // двойная выработка
                        if (temp.getVyrX2() > 0) {
                            impdb.importItemDataReport(data.getDate(),                  // дата
                                    temp.getTabelNum(),             // табельный номер
                                    12,                             // код операции из бух.программы
                                    0,                              // arg
                                    temp.getRazryad(),              // arg2 разряд
                                    temp.getVyrX2(),                // hours выработка x2
                                    0,                              // days
                                    0,                              // сумма -
                                    data.getBrig(),
                                    temp.getProf());
                        }

                        // внедрение дни часы
                        if (temp.getClVnedr() > 0 || temp.getdVnedr() > 0) {
                            impdb.importItemDataReport(data.getDate(),                  // дата
                                    temp.getTabelNum(),             // табельный номер
                                    18,                             // код операции из бух.программы
                                    0,                              // arg
                                    0,                              // arg2
                                    temp.getClVnedr(),              // hours внедрение часы
                                    temp.getdVnedr(),               // days внедрение дни
                                    0,                              // сумма -
                                    data.getBrig(),
                                    temp.getProf());
                        }

                        // тариф. дни часы
                        if (temp.getClTarif() > 0 || temp.getdTarif() > 0) {
                            impdb.importItemDataReport(data.getDate(),                  // дата
                                    temp.getTabelNum(),             // табельный номер
                                    9,                             // код операции из бух.программы
                                    0,                              // arg
                                    0,                              // arg2
                                    temp.getClTarif(),              // hours тариф часы
                                    temp.getdTarif(),               // days тариф дни
                                    0,                              // сумма -
                                    data.getBrig(),
                                    temp.getProf());
                        }

                        // средний. дни часы
                        if (temp.getClSredn() > 0 || temp.getdSredn() > 0) {
                            impdb.importItemDataReport(data.getDate(),                  // дата
                                    temp.getTabelNum(),             // табельный номер
                                    11,                             // код операции из бух.программы
                                    0,                              // arg
                                    0,                              // arg2
                                    temp.getClSredn(),              // hours сред. часы
                                    temp.getdSredn(),               // days сред. дни
                                    0,                              // сумма -
                                    data.getBrig(),
                                    temp.getProf());
                        }

                        // вредность
                        if (temp.getVrednost() > 0) {
                            impdb.importItemDataReport(data.getDate(),                  // дата
                                    temp.getTabelNum(),             // табельный номер
                                    7,                              // код операции из бух.программы
                                    0,                              // arg
                                    0,                              // arg2
                                    temp.getVrednost(),             // hours вредность часы
                                    0,                              // days
                                    0,                              // сумма -
                                    data.getBrig(),
                                    temp.getProf());
                        }

                        // стоимость +
                        if (temp.getStoimPlus() > 0) {
                            impdb.importItemDataReport(data.getDate(),                  // дата
                                    temp.getTabelNum(),             // табельный номер
                                    38,                             // код операции из бух.программы
                                    0,                              // arg
                                    0,                              // arg2
                                    0,                              // hours
                                    0,                              // days
                                    temp.getStoimPlus(),            // сумма стоимость +
                                    data.getBrig(),
                                    temp.getProf());
                        }

                        // стоимость +
                        if (temp.getStoimMinus() != 0) {
                            impdb.importItemDataReport(data.getDate(),                  // дата
                                    temp.getTabelNum(),             // табельный номер
                                    39,                             // код операции из бух.программы
                                    0,                              // arg
                                    0,                              // arg2
                                    0,                              // hours
                                    0,                              // days
                                    temp.getStoimMinus(),           // сумма стоимость -
                                    data.getBrig(),
                                    temp.getProf());
                        }

                        // 2/3 тариф. дни часы
                        if (temp.getClTarif23() > 0 || temp.getdTarif23() > 0) {
                            impdb.importItemDataReport(data.getDate(),                  // дата
                                    temp.getTabelNum(),             // табельный номер
                                    33,                             // код операции из бух.программы
                                    0,                              // arg
                                    0,                              // arg2
                                    temp.getClTarif23(),            // hours 2/3 тариф часы
                                    temp.getdTarif23(),             // days 2/3 тариф дни
                                    0,                              // сумма -
                                    data.getBrig(),
                                    temp.getProf());
                        }

                        // Отпуск дни
                        if (temp.getdOtpusk() > 0) {
                            impdb.importItemDataReport(data.getDate(),                  // дата
                                    temp.getTabelNum(),             // табельный номер
                                    87,                             // код операции из бух.программы
                                    0,                              // arg
                                    0,                              // arg2
                                    0,                              // hours
                                    temp.getdOtpusk(),              // days отпуск. дни
                                    0,                              // сумма -
                                    data.getBrig(),
                                    temp.getProf());
                        }

                        // Бол. лист дни
                        if (temp.getdBList() > 0) {
                            impdb.importItemDataReport(data.getDate(),                  // дата
                                    temp.getTabelNum(),             // табельный номер
                                    81,                             // код операции из бух.программы
                                    0,                              // arg
                                    0,                              // arg2
                                    0,                              // hours
                                    temp.getdBList(),               // days больн. дни
                                    0,                              // сумма -
                                    data.getBrig(),
                                    temp.getProf());
                        }

                        // Соц. отпуск дни
                        if (temp.getdSOtpusk() > 0) {
                            impdb.importItemDataReport(data.getDate(),                  // дата
                                    temp.getTabelNum(),             // табельный номер
                                    16,                             // код операции из бух.программы
                                    0,                              // arg
                                    0,                              // arg2
                                    0,                              // hours
                                    temp.getdSOtpusk(),             // days соц. отпуск. дни
                                    0,                              // сумма -
                                    data.getBrig(),
                                    temp.getProf());
                        }

                        // День матери дни
                        if (temp.getdDMateri() > 0) {
                            impdb.importItemDataReport(data.getDate(),                  // дата
                                    temp.getTabelNum(),             // табельный номер
                                    59,                             // код операции из бух.программы
                                    0,                              // arg
                                    0,                              // arg2
                                    0,                              // hours
                                    temp.getdDMateri(),             // days День матери дни
                                    0,                              // сумма -
                                    data.getBrig(),
                                    temp.getProf());
                        }

                        // Мед.комиссия дни
                        if (temp.getdMk() > 0) {
                            impdb.importItemDataReport(data.getDate(),                  // дата
                                    temp.getTabelNum(),             // табельный номер
                                    10,                             // код операции из бух.программы
                                    0,                              // arg
                                    0,                              // arg2
                                    0,                              // hours
                                    temp.getdMk(),                  // days Мед.комиссия дни
                                    0,                              // сумма -
                                    data.getBrig(),
                                    temp.getProf());
                        }

                    }

                    // Добавляем строку - содержание ведомости т4 в postgressql
                    ps = conn.prepareStatement(sql);
                    ps.setInt(1, id);
                    ps.setString(2, temp.getTabelNum());
                    ps.setString(3, temp.getFio());
                    ps.setString(4, temp.getProf());
                    ps.setInt(5, temp.getRazryad());
                    ps.setString(6, temp.getDataTabel().toString());
                    ps.setDouble(7, temp.getVyrBrig());
                    ps.setDouble(8, temp.getVyrItog());
                    ps.setDouble(9, temp.getProcent());
                    ps.setDouble(10, temp.getStoim());
                    ps.setDouble(11, temp.getClVech());
                    ps.setInt(12, temp.getdNoch());
                    ps.setDouble(13, temp.getClNoch());
                    ps.setInt(14, temp.getdOtrab());
                    ps.setDouble(15, temp.getClOtrab());
                    ps.setDouble(16, temp.getVyrX2());
                    ps.setInt(17, temp.getdVnedr());
                    ps.setDouble(18, temp.getClVnedr());
                    ps.setInt(19, temp.getdTarif());
                    ps.setDouble(20, temp.getClTarif());
                    ps.setInt(21, temp.getdSredn());
                    ps.setDouble(22, temp.getClSredn());
                    ps.setDouble(23, temp.getVrednost());
                    ps.setDouble(24, temp.getStoimPlus());
                    ps.setDouble(25, temp.getStoimMinus());
                    ps.setDouble(26, temp.getKofUch());
                    ps.setDouble(27, temp.getVyrDK());
                    ps.setDouble(28, temp.getNesort());
                    ps.setInt(29, temp.getdTarif23());
                    ps.setDouble(30, temp.getClTarif23());
                    ps.setInt(31, temp.getdOtpusk());
                    ps.setInt(32, temp.getdBList());
                    ps.setInt(33, temp.getdPerevod());
                    ps.setInt(34, temp.getdSOtpusk());
                    ps.setInt(35, temp.getdDMateri());
                    ps.setInt(36, temp.getdMk());
                    ps.setDouble(37, temp.getVyrOsvoen());
                    ps.setDouble(38, temp.getPercent());
                    ps.setDouble(39, temp.getMinusPercent());
                    ps.execute();
                }

                commit();

                rezalt = true;
            }
        } catch (Exception e) {
            rollBack();
            log.severe("Ошибка addImportVedomostT4()" + e);
        } finally {
            setAutoCommit(true);
        }

        return rezalt;
    }

    public boolean replayImportReportVedtT4(Vector vec) throws Exception {
        String sql = "";
        boolean rezalt = false;

        ZPlataDBImport impdb;

        try {
            setAutoCommit(false);

            impdb = new ZPlataDBImport();

            for (Object element : vec) {

                sql = "Select dept, brig, period, work_smen, work_clock, work_day, date "
                        + " From list_report"
                        + " Where id = ? ";

                ps = conn.prepareStatement(sql);
                ps.setInt(1, Integer.valueOf(element.toString()));
                rs = ps.executeQuery();

                if (rs.next()) {
                    ItemT4 data = new ItemT4();

                    data.setDate(UtilFunctions.convertDateStrToLong(new SimpleDateFormat("dd.MM.yyyy").format(rs.getDate("date"))));
                    data.setBrig(rs.getString("brig"));

                    sql = "Select tabelnum, fio, prof, razryad, datatabel, "
                            + "            vyrbrig, vyritog, procent, stoim, clvech, dnoch, clnoch, dotrab, clotrab, "
                            + "            vyrx2, dvnedr, clvnedr, dtarif, cltarif, dsredn, clsredn, vrednost, "
                            + "            stoimplus, stoimminus, kofuch, vyrdk, nesort, dtarif23, cltarif23, "
                            + "            dotpusk, dblist, dperehod, dsotpusk, ddmateri, dmk , vyrosvoen "
                            + " From list_report_item "
                            + " Where  id_list_report = ? ";

                    ps = conn.prepareStatement(sql);
                    ps.setInt(1, Integer.valueOf(element.toString()));
                    rs = ps.executeQuery();

                    Vector list = new Vector();

                    while (rs.next()) {
                        ItemBuh blist = new ItemBuh();

                        blist.setTabelNum(rs.getString("tabelnum"));
                        blist.setFio(rs.getString("fio"));
                        blist.setProf(rs.getString("prof"));
                        blist.setRazryad(rs.getInt("razryad"));
                        blist.setDataTabel(new Vector());
                        blist.setVyrBrig(rs.getDouble("vyrbrig"));
                        blist.setVyrItog(rs.getDouble("vyritog"));
                        blist.setProcent(rs.getDouble("procent"));
                        blist.setStoim(rs.getDouble("stoim"));
                        blist.setClVech(rs.getDouble("clvech"));
                        blist.setdNoch(rs.getInt("dnoch"));
                        blist.setClNoch(rs.getDouble("clnoch"));
                        blist.setdOtrab(rs.getInt("dotrab"));
                        blist.setClOtrab(rs.getDouble("clotrab"));
                        blist.setVyrX2(rs.getDouble("vyrx2"));
                        blist.setdVnedr(rs.getInt("dvnedr"));
                        blist.setClVnedr(rs.getDouble("clvnedr"));
                        blist.setdTarif(rs.getInt("dtarif"));
                        blist.setClTarif(rs.getDouble("cltarif"));
                        blist.setdSredn(rs.getInt("dsredn"));
                        blist.setClSredn(rs.getDouble("clsredn"));
                        blist.setVrednost(rs.getDouble("vrednost"));
                        blist.setStoimMinus(rs.getDouble("stoimminus") * (-1));
                        blist.setStoimPlus(rs.getDouble("stoimplus"));
                        blist.setKofUch(rs.getDouble("kofuch"));
                        blist.setVyrDK(rs.getDouble("vyrdk"));
                        blist.setNesort(rs.getDouble("nesort"));
                        blist.setdTarif23(rs.getInt("dtarif23"));
                        blist.setClTarif23(rs.getDouble("cltarif23"));
                        blist.setdOtpusk(rs.getInt("dotpusk"));
                        blist.setdBList(rs.getInt("dblist"));
                        blist.setdPerehod(rs.getInt("dperehod"));
                        blist.setdSOtpusk(rs.getInt("dsotpusk"));
                        blist.setdDMateri(rs.getInt("ddmateri"));
                        blist.setdMk(rs.getInt("dmk"));
                        blist.setVyrOsvoen(rs.getDouble("vyrosvoen"));

                        list.add(blist);
                    }
                    data.setDataPeople(list);

                    // Удаляем данные из mssql за выбранный период и бригаду
                    impdb.deleteItemsDataReport(data.getDate(), data.getBrig());

                    // Добавляем содержание ведомости т4 в mssql
                    for (Object obj : data.getDataPeople()) {
                        ItemBuh temp = (ItemBuh) obj;

                        if (!temp.getFio().startsWith("(")) {

                            // процент выполнения
                            if (temp.getProcent() > 0) {
                                impdb.importItemDataReport(data.getDate(),                  // дата
                                        temp.getTabelNum(),             // табельный номер
                                        -9,                             // код операции из бух.программы
                                        temp.getProcent(),              // arg процент выполнения
                                        0,                              // arg2
                                        0,                              // hours
                                        0,                              // days
                                        0,                              // сумма -
                                        data.getBrig(),
                                        temp.getProf());
                            }

                            // отработанные дни часы
                            if (temp.getClOtrab() > 0 || temp.getdOtrab() > 0) {
                                impdb.importItemDataReport(data.getDate(),                  // дата
                                        temp.getTabelNum(),             // табельный номер
                                        -3,                             // код операции из бух.программы
                                        0,                              // arg
                                        0,                              // arg2
                                        temp.getClOtrab(),              // hours отр. часы
                                        temp.getdOtrab(),               // days отр. дни
                                        0,                              // сумма -
                                        data.getBrig(),
                                        temp.getProf());
                            }

                            // Перевод дни
                            if (temp.getdPerevod() > 0) {
                                impdb.importItemDataReport(data.getDate(),                  // дата
                                        temp.getTabelNum(),             // табельный номер
                                        -1,                             // код операции из бух.программы
                                        0,                              // arg
                                        0,                              // arg2
                                        0,                              // hours
                                        temp.getdPerevod(),             // days перевод дни
                                        0,                              // сумма -
                                        data.getBrig(),
                                        temp.getProf());
                            }

                            // Итоговая выработка
                            if (temp.getVyrItog() > 0) {
                                impdb.importItemDataReport(data.getDate(),                  // дата
                                        temp.getTabelNum(),             // табельный номер
                                        13,                             // код операции из бух.программы
                                        temp.getStoim(),                // arg стоимость
                                        temp.getRazryad(),              // arg2 разряд
                                        temp.getVyrItog(),              // hours выработка
                                        0,                              // days -
                                        0,                              // сумма -
                                        data.getBrig(),
                                        temp.getProf());
                            }

                            // вечернее время дни часы
                            if (temp.getClVech() > 0) {
                                impdb.importItemDataReport(data.getDate(),                  // дата
                                        temp.getTabelNum(),             // табельный номер
                                        31,                             // код операции из бух.программы
                                        0,                              // arg
                                        0,                              // arg2
                                        temp.getClVech(),               // hours веч. часы
                                        0,                              // days
                                        0,                              // сумма -
                                        data.getBrig(),
                                        temp.getProf());
                            }

                            // ноч. дни
                            if (temp.getClNoch() > 0 || temp.getdNoch() > 0) {
                                impdb.importItemDataReport(data.getDate(),                  // дата
                                        temp.getTabelNum(),             // табельный номер
                                        32,                             // код операции из бух.программы
                                        0,                              // arg
                                        0,                              // arg2
                                        temp.getdNoch(),                // hours ноч. дни
                                        0,                              // days
                                        0,                              // сумма -
                                        data.getBrig(),
                                        temp.getProf());
                            }

                            // ноч. часы
                            if (temp.getClNoch() > 0 || temp.getdNoch() > 0) {
                                impdb.importItemDataReport(data.getDate(),                  // дата
                                        temp.getTabelNum(),             // табельный номер
                                        134,                            // код операции из бух.программы
                                        0,                              // arg
                                        0,                              // arg2
                                        temp.getClNoch(),               // hours ноч. часы
                                        0,                              // days
                                        0,                              // сумма -
                                        data.getBrig(),
                                        temp.getProf());
                            }

                            // двойная выработка
                            if (temp.getVyrX2() > 0) {
                                impdb.importItemDataReport(data.getDate(),                  // дата
                                        temp.getTabelNum(),             // табельный номер
                                        12,                             // код операции из бух.программы
                                        0,                              // arg
                                        temp.getRazryad(),              // arg2 разряд
                                        temp.getVyrX2(),                // hours выработка x2
                                        0,                              // days
                                        0,                              // сумма -
                                        data.getBrig(),
                                        temp.getProf());
                            }

                            // внедрение дни часы
                            if (temp.getClVnedr() > 0 || temp.getdVnedr() > 0) {
                                impdb.importItemDataReport(data.getDate(),                  // дата
                                        temp.getTabelNum(),             // табельный номер
                                        18,                             // код операции из бух.программы
                                        0,                              // arg
                                        0,                              // arg2
                                        temp.getClVnedr(),              // hours внедрение часы
                                        temp.getdVnedr(),               // days внедрение дни
                                        0,                              // сумма -
                                        data.getBrig(),
                                        temp.getProf());
                            }

                            // тариф. дни часы
                            if (temp.getClTarif() > 0 || temp.getdTarif() > 0) {
                                impdb.importItemDataReport(data.getDate(),                  // дата
                                        temp.getTabelNum(),             // табельный номер
                                        9,                             // код операции из бух.программы
                                        0,                              // arg
                                        0,                              // arg2
                                        temp.getClTarif(),              // hours тариф часы
                                        temp.getdTarif(),               // days тариф дни
                                        0,                              // сумма -
                                        data.getBrig(),
                                        temp.getProf());
                            }

                            // средний. дни часы
                            if (temp.getClSredn() > 0 || temp.getdSredn() > 0) {
                                impdb.importItemDataReport(data.getDate(),                  // дата
                                        temp.getTabelNum(),             // табельный номер
                                        11,                             // код операции из бух.программы
                                        0,                              // arg
                                        0,                              // arg2
                                        temp.getClSredn(),              // hours сред. часы
                                        temp.getdSredn(),               // days сред. дни
                                        0,                              // сумма -
                                        data.getBrig(),
                                        temp.getProf());
                            }

                            // вредность
                            if (temp.getVrednost() > 0) {
                                impdb.importItemDataReport(data.getDate(),                  // дата
                                        temp.getTabelNum(),             // табельный номер
                                        7,                              // код операции из бух.программы
                                        0,                              // arg
                                        0,                              // arg2
                                        temp.getVrednost(),             // hours вредность часы
                                        0,                              // days
                                        0,                              // сумма -
                                        data.getBrig(),
                                        temp.getProf());
                            }

                            // стоимость +
                            if (temp.getStoimPlus() > 0) {
                                impdb.importItemDataReport(data.getDate(),                  // дата
                                        temp.getTabelNum(),             // табельный номер
                                        38,                             // код операции из бух.программы
                                        0,                              // arg
                                        0,                              // arg2
                                        0,                              // hours
                                        0,                              // days
                                        temp.getStoimPlus(),            // сумма стоимость +
                                        data.getBrig(),
                                        temp.getProf());
                            }

                            // стоимость +
                            if (temp.getStoimMinus() != 0) {
                                impdb.importItemDataReport(data.getDate(),                  // дата
                                        temp.getTabelNum(),             // табельный номер
                                        39,                             // код операции из бух.программы
                                        0,                              // arg
                                        0,                              // arg2
                                        0,                              // hours
                                        0,                              // days
                                        temp.getStoimMinus(),           // сумма стоимость -
                                        data.getBrig(),
                                        temp.getProf());
                            }

                            // 2/3 тариф. дни часы
                            if (temp.getClTarif23() > 0 || temp.getdTarif23() > 0) {
                                impdb.importItemDataReport(data.getDate(),                  // дата
                                        temp.getTabelNum(),             // табельный номер
                                        33,                             // код операции из бух.программы
                                        0,                              // arg
                                        0,                              // arg2
                                        temp.getClTarif23(),            // hours 2/3 тариф часы
                                        temp.getdTarif23(),             // days 2/3 тариф дни
                                        0,                              // сумма -
                                        data.getBrig(),
                                        temp.getProf());
                            }

                            // Отпуск дни
                            if (temp.getdOtpusk() > 0) {
                                impdb.importItemDataReport(data.getDate(),                  // дата
                                        temp.getTabelNum(),             // табельный номер
                                        87,                             // код операции из бух.программы
                                        0,                              // arg
                                        0,                              // arg2
                                        0,                              // hours
                                        temp.getdOtpusk(),              // days отпуск. дни
                                        0,                              // сумма -
                                        data.getBrig(),
                                        temp.getProf());
                            }

                            // Бол. лист дни
                            if (temp.getdBList() > 0) {
                                impdb.importItemDataReport(data.getDate(),                  // дата
                                        temp.getTabelNum(),             // табельный номер
                                        81,                             // код операции из бух.программы
                                        0,                              // arg
                                        0,                              // arg2
                                        0,                              // hours
                                        temp.getdBList(),               // days больн. дни
                                        0,                              // сумма -
                                        data.getBrig(),
                                        temp.getProf());
                            }

                            // Соц. отпуск дни
                            if (temp.getdSOtpusk() > 0) {
                                impdb.importItemDataReport(data.getDate(),                  // дата
                                        temp.getTabelNum(),             // табельный номер
                                        16,                             // код операции из бух.программы
                                        0,                              // arg
                                        0,                              // arg2
                                        0,                              // hours
                                        temp.getdSOtpusk(),             // days соц. отпуск. дни
                                        0,                              // сумма -
                                        data.getBrig(),
                                        temp.getProf());
                            }

                            // День матери дни
                            if (temp.getdDMateri() > 0) {
                                impdb.importItemDataReport(data.getDate(),                  // дата
                                        temp.getTabelNum(),             // табельный номер
                                        59,                             // код операции из бух.программы
                                        0,                              // arg
                                        0,                              // arg2
                                        0,                              // hours
                                        temp.getdDMateri(),             // days День матери дни
                                        0,                              // сумма -
                                        data.getBrig(),
                                        temp.getProf());
                            }

                            // Мед.комиссия дни
                            if (temp.getdMk() > 0) {
                                impdb.importItemDataReport(data.getDate(),                  // дата
                                        temp.getTabelNum(),             // табельный номер
                                        10,                             // код операции из бух.программы
                                        0,                              // arg
                                        0,                              // arg2
                                        0,                              // hours
                                        temp.getdMk(),                  // days Мед.комиссия дни
                                        0,                              // сумма -
                                        data.getBrig(),
                                        temp.getProf());
                            }
                        }
                    }
                }
            }

            commit();

            rezalt = true;
        } catch (Exception e) {
            rollBack();
            log.severe("Ошибка replayImportReportVedtT4()" + e);
        } finally {
            setAutoCommit(true);
        }

        return rezalt;
    }

    public Vector getAllImportReports() throws Exception {
        Vector elements = new Vector();
        String sql = "";

        try {
            sql = " Select  id, dept, brig, period, date from list_report Order by date desc, dept, brig  ";

            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                Vector tmp = new Vector();
                tmp.add(false);
                tmp.add(rs.getInt("id"));
                tmp.add(new SimpleDateFormat("dd-MM-yyyy").format(rs.getDate("date")));
                tmp.add(rs.getString("dept"));
                tmp.add(rs.getString("brig"));
                tmp.add(rs.getString("period"));
                elements.add(tmp);
            }

        } catch (Exception e) {
            elements = new Vector();
            log.severe("Ошибка getAllImportReports()" + e);
        }
        return elements;
    }

    public Vector getDataReportList(int idReport) throws Exception {
        Vector elements = new Vector();
        String sql = "";

        try {
            sql = "SELECT tabelnum, fio, prof, razryad, datatabel, "
                    + "        vyrbrig, vyritog, procent, stoim, clvech, dnoch, clnoch, dotrab, clotrab, "
                    + "        vyrx2, dvnedr, clvnedr, dtarif, cltarif, dsredn, clsredn, vrednost, "
                    + "        stoimplus, stoimminus, kofuch, vyrdk, nesort, dtarif23, cltarif23, "
                    + "        dotpusk, dblist, dperehod, dsotpusk, ddmateri, dmk, vyrosvoen,"
                    + "        prem_percent, percent_minus"
                    + "  FROM  list_report_item "
                    + "  WHERE id_list_report = ?"
                    + "  ORDER BY  fio, tabelnum, prof, razryad";

            ps = conn.prepareStatement(sql);
            ps.setInt(1, idReport);
            rs = ps.executeQuery();

            while (rs.next()) {
                Vector tmp = new Vector();
                tmp.add(rs.getString("tabelnum"));
                tmp.add(rs.getString("fio"));
                tmp.add(rs.getString("prof"));
                tmp.add(rs.getInt("razryad"));
                tmp.add(rs.getString("datatabel"));
                tmp.add(rs.getDouble("vyrbrig"));
                tmp.add(rs.getDouble("vyritog"));
                tmp.add(rs.getDouble("procent"));
                tmp.add(rs.getDouble("stoim"));
                tmp.add(rs.getDouble("clvech"));
                tmp.add(rs.getInt("dnoch"));
                tmp.add(rs.getDouble("clnoch"));
                tmp.add(rs.getInt("dotrab"));
                tmp.add(rs.getDouble("clotrab"));
                tmp.add(rs.getDouble("vyrx2"));
                tmp.add(rs.getInt("dvnedr"));
                tmp.add(rs.getDouble("clvnedr"));
                tmp.add(rs.getInt("dtarif"));
                tmp.add(rs.getDouble("cltarif"));
                tmp.add(rs.getInt("dsredn"));
                tmp.add(rs.getDouble("clsredn"));
                tmp.add(rs.getDouble("vrednost"));
                tmp.add(rs.getDouble("stoimplus"));
                tmp.add(rs.getDouble("stoimminus"));
                tmp.add(rs.getDouble("kofuch"));
                tmp.add(rs.getDouble("vyrdk"));
                tmp.add(rs.getDouble("nesort"));
                tmp.add(rs.getInt("dtarif23"));
                tmp.add(rs.getDouble("cltarif23"));
                tmp.add(rs.getInt("dotpusk"));
                tmp.add(rs.getInt("dblist"));
                tmp.add(rs.getInt("dperehod"));
                tmp.add(rs.getInt("dsotpusk"));
                tmp.add(rs.getInt("ddmateri"));
                tmp.add(rs.getInt("dmk"));
                tmp.add(rs.getDouble("vyrosvoen"));
                tmp.add(rs.getDouble(37));
                tmp.add(rs.getDouble(38));
                elements.add(tmp);
            }

        } catch (Exception e) {
            elements = new Vector();
            log.severe("Ошибка getDataReportList()" + e);
        }
        return elements;
    }

    public boolean deleteReportList(int idReportList) throws Exception {
        boolean flag = false;
        String sql = "";

        try {
            setAutoCommit(false);

            sql = "DELETE FROM list_report_item "
                    + " WHERE id_list_report = ? ";

            ps = conn.prepareStatement(sql);
            ps.setInt(1, idReportList);
            ps.execute();

            sql = "DELETE FROM list_report "
                    + " WHERE id = ? ";

            ps = conn.prepareStatement(sql);
            ps.setInt(1, idReportList);
            ps.execute();

            commit();

            flag = true;

        } catch (Exception e) {
            rollBack();
            flag = false;
            log.severe("Ошибка deleteReportList()" + e);
        } finally {
            setAutoCommit(true);
        }

        return flag;
    }

    public Vector getLoadTechBrig(int idDept, int idBrig, long sVodDate, long eVodDate) throws Exception {
        Vector elements = new Vector();
        String sql = "";

        try {
            sql = "Select dept.id as idDept, "
                    + "     department, "
                    + "     s_brig.id as idBrig, "
                    + "     s_brig.kod as kodBrig, "
                    + "     s_tech.id as idTech, "
                    + "     s_tech.tech as tech, "
                    + "     s_tech.price as techPrice, "
                    + "     sum(round(list_item.kolvo*round(spec_item.vnorm::numeric," + UtilZPlata.ROUNDING_NORM + "), 2)) as summ "
                    + " From s_tech, list, list_item, spec, spec_item, s_brig, dept "
                    + " Where    s_tech.id = spec_item.id_tech "
                    + "     and list.id = list_item.id_list "
                    + "     and list.id_spec = spec.id "
                    + "     and spec.id = spec_item.id_spec "
                    + "     and spec_item.id = list_item.id_spec_item "
                    + "     and list.status <> -1 "
                    + "     and list.id_brig = s_brig.id "
                    + "     and list.id_dept = dept.id "
                    + (idBrig != -1 ? " and list.id_brig = " + idBrig + " " : " ")
                    + (idDept != -1 ? " and list.id_dept = " + idDept + " " : " ")
                    + "     and date_vvod between '" + new java.sql.Date(sVodDate) + "' "
                    + "                       and '" + new java.sql.Date(eVodDate) + "' "
                    + " Group by idDept, department, idBrig, kodBrig, idTech, tech, techPrice"
                    + " Order by tech, techPrice";

            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                Vector tmp = new Vector();
                tmp.add(false);
                tmp.add(rs.getInt("idTech"));
                tmp.add(rs.getString("tech").trim());
                tmp.add(new BigDecimal(rs.getDouble("techPrice")).setScale(2, RoundingMode.HALF_UP));
                tmp.add(new Double(UtilZPlata.formatNorm(rs.getDouble("summ"), 2)));
                tmp.add(UtilZPlata.PLAN_WORKING_TIME);
                tmp.add(new BigDecimal(Double.valueOf(tmp.get(4).toString())
                        / UtilZPlata.PLAN_WORKING_TIME).setScale(2, RoundingMode.HALF_UP));
                tmp.add(new BigDecimal(Double.valueOf(tmp.get(6).toString())
                        * Double.valueOf(tmp.get(3).toString())).setScale(2, RoundingMode.HALF_UP));
                elements.add(tmp);
            }

        } catch (Exception e) {
            elements = new Vector();
            System.err.println("Ошибка getLoadTechBrig() " + e);
            log.severe("Ошибка getLoadTechBrig()" + e);
            throw new Exception("Ошибка getLoadTechBrig() " + e.getMessage(), e);
        }
        return elements;
    }

    public Vector getLoadTechAllBrig(int idDept, long sVodDate, long eVodDate) throws Exception {
        Vector elements = new Vector();
        String sql = "Select s_tech.id as idTech,\n" +
                "       s_tech.tech as tech,\n" +
                "       s_tech.price as techPrice,\n" +
                "       sum(round(list_item.kolvo*round(spec_item.vnorm::numeric,4), 2)) as summ\n" +
                "From s_tech, list, list_item, spec, spec_item, s_brig, dept\n" +
                "Where    s_tech.id = spec_item.id_tech\n" +
                "  and list.id = list_item.id_list\n" +
                "  and list.id_spec = spec.id\n" +
                "  and spec.id = spec_item.id_spec\n" +
                "  and spec_item.id = list_item.id_spec_item\n" +
                "  and list.status <> -1\n" +
                "  and list.id_brig = s_brig.id\n" +
                "  and list.id_dept = dept.id\n" +
                "  and list.id_brig in (15,16,24,19,23,22,21,20)\n" +
                "  and list.id_dept = ?\n" +
                "     and date_vvod between ? and ? " +
                "Group by idTech, tech, techPrice\n" +
                "Order by tech, techPrice";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idDept);
            ps.setDate(2, new java.sql.Date(sVodDate));
            ps.setDate(3, new java.sql.Date(eVodDate));
            rs = ps.executeQuery();

            while (rs.next()) {
                Vector tmp = new Vector();
                tmp.add(false);
                tmp.add(rs.getInt("idTech"));
                tmp.add(rs.getString("tech").trim());
                tmp.add(BigDecimal.valueOf(rs.getDouble("techPrice")).setScale(2, RoundingMode.HALF_UP));
                tmp.add(new Double(UtilZPlata.formatNorm(rs.getDouble("summ"), 2)));
                tmp.add(UtilZPlata.PLAN_WORKING_TIME);
                tmp.add(BigDecimal.valueOf(Double.parseDouble(tmp.get(4).toString())
                        / UtilZPlata.PLAN_WORKING_TIME).setScale(2, RoundingMode.HALF_UP));
                tmp.add(BigDecimal.valueOf(Double.parseDouble(tmp.get(6).toString())
                        * Double.parseDouble(tmp.get(3).toString())).setScale(2, RoundingMode.HALF_UP));
                elements.add(tmp);
            }

        } catch (Exception e) {
            elements = new Vector();
            log.severe("Ошибка getLoadTechBrig()" + e);
        }
        return elements;
    }
}
