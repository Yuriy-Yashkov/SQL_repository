package dept.production.zsh.spec;

import by.march8.api.utils.DatePeriod;
import by.march8.ecs.framework.common.LogCrutch;
import by.march8.entities.seamstress.ViewSeamstressItem;
import workDB.PDB_new;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

/**
 *
 * @author lidashka
 */
@SuppressWarnings("all")
public class SpecPDB extends PDB_new {
    // private static final Logger log = new Log().getLoger(SpecPDB.class);
    private static final LogCrutch log = new LogCrutch();

    /**
     * Возвращает все подразделения
     * @return
     * @throws Exception
     */
    public Vector getAllDept() throws Exception {
        Vector dept = new Vector();
        String sql = "Select id, department from dept Order by department";

        try {
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                Vector tmp = new Vector();
                tmp.add(rs.getInt("id"));
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
     * Добавляет новую спецификацию
     * @param idDept id подразделения
     * @param idModel id модели
     * @param IdEmploye id пользователя
     * @param naimSpec спецификация
     * @param sDate дата ввода
     * @param dataItem данные спецификации
     * @return
     * @throws Exception
     */
    public boolean addSpec(int idDept, int idModel, int IdEmploye, String naimSpec, long sDate, Vector dataItem, Boolean isBasic) throws Exception {
        boolean rezalt = false;
        String sql;
        try {
            setAutoCommit(false);

            sql = "Insert into spec(id_model, id_dept, id_employer, spec, date_start, basic) values( ?, ?, ?, ?, ?, ?) returning id";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, idModel);
            ps.setInt(2, idDept);
            ps.setInt(3, IdEmploye);
            ps.setString(4, naimSpec.trim());
            ps.setDate(5, new java.sql.Date(sDate));
            ps.setBoolean(6, isBasic);
            rs = ps.executeQuery();

            if (rs.next()) {
                sql = "Insert into spec_item(id_spec, num, operac, id_tech, category, enorm, vnorm) values( ?, ?, ?, ?, ?, ?, ?)";
                for (Iterator it = dataItem.iterator(); it.hasNext(); ) {
                    Vector vec = (Vector) it.next();

                    ps = conn.prepareStatement(sql);
                    ps.setInt(1, rs.getInt(1));
                    ps.setInt(2, Integer.valueOf(vec.get(0).toString()));
                    ps.setString(3, vec.get(1).toString().trim());
                    ps.setInt(4, Integer.valueOf(vec.get(2).toString()));
                    ps.setInt(5, Integer.valueOf(vec.get(4).toString()));
                    ps.setInt(6, Integer.valueOf(vec.get(5).toString()));
                    ps.setDouble(7, Double.valueOf(vec.get(6).toString()));
                    ps.execute();
                }
            } else
                throw new Exception();

            commit();
            rezalt = true;
        } catch (Exception e) {
            rezalt = false;
            rollBack();
            System.err.println("Ошибка addSpec() " + e);
            log.error("Ошибка addSpec()", e);
            throw new Exception("Ошибка addSpec() " + e.getMessage(), e);
        } finally {
            setAutoCommit(true);
        }
        return rezalt;
    }

    /**
     * Добавляет новую спецификацию
     * @param idDept id подразделения
     * @param textOper наименование операции
     * @param idTech id оборудования
     * @param IdEmploye id пользователя
     * @param sDate дата ввода
     * @param enorm норма ед.шт.
     * @param vnorm норма времени
     * @param dataItem спецификации
     * @return
     * @throws Exception
     */
    public boolean addSpecMore(int idDept,
                               String textOper,
                               int idTech,
                               int IdEmploye,
                               long sDate,
                               int enorm,
                               double vnorm,
                               Vector dataItem) throws Exception {

        boolean rezalt = false;
        String sql;
        try {
            setAutoCommit(false);

            for (Iterator it = dataItem.iterator(); it.hasNext(); ) {
                Vector vec = (Vector) it.next();

                sql = "Insert into spec(id_model, "
                        + "         id_dept, "
                        + "         id_employer, "
                        + "         spec, "
                        + "         date_start) "
                        + " values( ?, ?, ?, ?, ?) returning id";
                ps = conn.prepareStatement(sql);
                ps.setInt(1, Integer.valueOf(vec.get(0).toString()));
                ps.setInt(2, idDept);
                ps.setInt(3, IdEmploye);
                ps.setString(4, vec.get(1).toString().trim());
                ps.setDate(5, new java.sql.Date(sDate));
                rs = ps.executeQuery();

                if (rs.next()) {
                    sql = "Insert into spec_item(id_spec, "
                            + "                 num, "
                            + "                 operac, "
                            + "                 id_tech, "
                            + "                 category, "
                            + "                 enorm, "
                            + "                 vnorm) values( ?, ?, ?, ?, ?, ?, ?)";

                    ps = conn.prepareStatement(sql);
                    ps.setInt(1, rs.getInt(1));
                    ps.setInt(2, 1);
                    ps.setString(3, textOper);
                    ps.setInt(4, idTech);
                    ps.setInt(5, 5);
                    ps.setInt(6, enorm);
                    ps.setDouble(7, vnorm);
                    ps.execute();

                } else
                    throw new Exception();
            }

            commit();
            rezalt = true;
        } catch (Exception e) {
            rezalt = false;
            rollBack();
            System.err.println("Ошибка addSpecMore() " + e);
            log.error("Ошибка addSpecMore()", e);
            throw new Exception("Ошибка addSpecMore() " + e.getMessage(), e);
        } finally {
            setAutoCommit(true);
        }
        return rezalt;
    }

    /**
     * Обновляет спецификацию
     * @param idSpec id спецификации
     * @param idDept id подразделения
     * @param idModel id модели
     * @param IdEmploye id пользователя
     * @param naimSpec спецификация
     * @param sDate дата ввода
     * @param dataItem данные спецификации
     * @param deleteItem данные спецификации, которые нужно удалить
     * @return
     * @throws Exception
     */
    public boolean updateSpec(int idSpec, int idDept, int idModel, int IdEmploye, String naimSpec, long sDate, Vector dataItem, Vector deleteItem, Boolean isBasic) throws Exception {
        boolean rezalt = false;
        String sql;
        try {
            setAutoCommit(false);

            sql = "Delete from spec_item where id = ?";
            for (Iterator it = deleteItem.iterator(); it.hasNext(); ) {
                ps = conn.prepareStatement(sql);
                ps.setInt(1, Integer.valueOf(it.next().toString()));
                ps.execute();
            }

            sql = "Update spec set id_model = ? , id_dept = ?, id_employer = ?,  spec = ?, date_start = ?, date_ins = now(), basic = ? where id = ?";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, idModel);
            ps.setInt(2, idDept);
            ps.setInt(3, IdEmploye);
            ps.setString(4, naimSpec.trim());
            ps.setDate(5, new java.sql.Date(sDate));
            ps.setInt(7, idSpec);
            ps.setBoolean(6, isBasic);
            ps.execute();

            for (Iterator it = dataItem.iterator(); it.hasNext(); ) {
                Vector vec = (Vector) it.next();
                if (vec.get(7) == null) {
                    sql = "Insert into spec_item(id_spec, num, operac, id_tech, category, enorm, vnorm) values( ?, ?, ?, ?, ?, ?, ?)";

                    ps = conn.prepareStatement(sql);
                    ps.setInt(1, idSpec);
                    ps.setInt(2, Integer.valueOf(vec.get(0).toString()));
                    ps.setString(3, vec.get(1).toString().trim());
                    ps.setInt(4, Integer.valueOf(vec.get(2).toString()));
                    ps.setInt(5, Integer.valueOf(vec.get(4).toString()));
                    ps.setInt(6, Integer.valueOf(vec.get(5).toString()));
                    ps.setDouble(7, Double.valueOf(vec.get(6).toString()));
                    ps.execute();
                } else {
                    sql = "Update spec_item set id_spec = ?, num = ?, operac = ?, id_tech = ?, category = ?, enorm = ?, vnorm = ? where id = ?";

                    ps = conn.prepareStatement(sql);
                    ps.setInt(1, idSpec);
                    ps.setInt(2, Integer.valueOf(vec.get(0).toString()));
                    ps.setString(3, vec.get(1).toString().trim());
                    ps.setInt(4, Integer.valueOf(vec.get(2).toString()));
                    ps.setInt(5, Integer.valueOf(vec.get(4).toString()));
                    ps.setInt(6, Integer.valueOf(vec.get(5).toString()));
                    ps.setDouble(7, Double.valueOf(vec.get(6).toString()));
                    ps.setInt(8, Integer.valueOf(vec.get(7).toString()));
                    ps.execute();
                }
            }
            commit();
            rezalt = true;
        } catch (Exception e) {
            rezalt = false;
            rollBack();
            System.err.println("Ошибка addSpec() " + e);
            log.error("Ошибка addSpec()", e);
            throw new Exception("Ошибка addSpec() " + e.getMessage(), e);
        } finally {
            setAutoCommit(true);
        }
        return rezalt;
    }

    /**
     * Возвращает все операции подразделения
     * @param idDept id подразделения
     * @return
     * @throws Exception
     */
    public Vector getAllOperac(int idDept) throws Exception {
        Vector operac = new Vector();
        String sql = "Select distinct operac from spec_item, spec "
                + " Where spec.id = spec_item.id_spec and id_dept = ? and operac not like '' "
                + " Order by operac";

        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, idDept);
            rs = ps.executeQuery();
            while (rs.next()) {
                Vector tmp = new Vector();
                tmp.add(rs.getString("operac").trim());
                operac.add(tmp);
            }

        } catch (Exception e) {
            operac = new Vector();
            System.err.println("Ошибка getAllOperac() " + e);
            log.error("Ошибка getAllOperac()", e);
            throw new Exception("Ошибка getAllOperac() " + e.getMessage(), e);
        }
        return operac;
    }

    /**
     * Возвращает все спецификации подразделения
     * @param idDept id подразделения
     * @param paramSortModel (true - сортирует по модели; false -сортирует по дате коррект.;)
     * @return
     * @throws Exception
     */
    public Vector getAllSpec(int idDept, boolean paramSortModel) throws Exception {
        Vector dept = new Vector();
        String sql = "Select idModel, depat, kod, spec, date_start, date_ins, sum(vnorm) as vnorm, basic "
                + "   From ( Select  id_model as idModel, dept.department as depat, "
                + "                  spec.id as kod, spec, "
                + "                  date_start, date_ins, basic "
                + "          From spec, dept "
                + "          Where spec.id_dept = dept.id and "
                + "                spec.id_dept = ? ) as t1 "
                + "   left join spec_item on "
                + "           spec_item.id_spec = t1.kod and "
                + "           spec_item.num >= 0 and "
                + "           spec_item.id_tech <> -1 "
                + "   Group by idModel, depat, kod, spec, date_start, date_ins, basic "
                + (paramSortModel ?
                "   Order by idModel, depat, kod, spec, date_start, date_ins " :
                "   Order by date_ins desc, idModel, depat, kod, spec, date_start ");

        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, idDept);
            rs = ps.executeQuery();
            while (rs.next()) {
                Vector tmp = new Vector();
                tmp.add(false);
                tmp.add(rs.getInt("idModel"));
                tmp.add(rs.getString("depat"));
                tmp.add(rs.getInt("kod"));
                tmp.add(rs.getString("spec"));
                tmp.add(UtilSpec.formatNorm(rs.getDouble("vnorm")));
                tmp.add(new SimpleDateFormat("dd-MM-yyyy").format(rs.getDate("date_start")));
                tmp.add(new SimpleDateFormat("dd-MM-yyyy").format(rs.getDate("date_ins")));
                tmp.add(rs.getBoolean("basic"));
                dept.add(tmp);
            }

        } catch (Exception e) {
            System.err.println("Ошибка getAllSpec() " + e);
            log.error("Ошибка getAllSpec()", e);
            throw new Exception("Ошибка getAllSpec() " + e.getMessage(), e);
        }
        return dept;
    }

    /**
     * Возвращает все спецификации
     * @param paramSortModel (true - сортирует по модели; false -сортирует по дате коррект.;)
     * @return
     * @throws Exception
     */

    public Vector getAllSpec(boolean paramSortModel) throws Exception {
        Vector data = new Vector();
        String sql = "Select idModel, depat, kod, spec, date_start, date_ins, sum(vnorm) as vnorm, basic "
                + "   From ( Select  id_model as idModel, dept.department as depat, "
                + "                  spec.id as kod, spec, "
                + "                  date_start, date_ins, basic "
                + "          From spec, dept "
                + "          Where spec.id_dept = dept.id  ) as t1 "
                + "   left join spec_item on "
                + "           spec_item.id_spec = t1.kod and "
                + "           spec_item.num >= 0 and "
                + "           spec_item.id_tech <> -1 "
                + "   Group by idModel, depat, kod, spec, date_start, date_ins, basic "
                + (paramSortModel ?
                "   Order by idModel, depat, kod, spec, date_start, date_ins " :
                "   Order by date_ins desc, idModel, depat, kod, spec, date_start ");

        try {
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                Vector tmp = new Vector();
                tmp.add(false);
                tmp.add(rs.getInt("idModel"));
                tmp.add(rs.getString("depat"));
                tmp.add(rs.getInt("kod"));
                tmp.add(rs.getString("spec"));
                tmp.add(UtilSpec.formatNorm(rs.getDouble("vnorm")));
                tmp.add(new SimpleDateFormat("dd-MM-yyyy").format(rs.getDate("date_start")));
                tmp.add(new SimpleDateFormat("dd-MM-yyyy").format(rs.getDate("date_ins")));
                tmp.add(rs.getBoolean("basic"));

                data.add(tmp);
            }

        } catch (Exception e) {
            System.err.println("Ошибка getAllSpec() " + e);
            log.error("Ошибка getAllSpec()", e);
            throw new Exception("Ошибка getAllSpec() " + e.getMessage(), e);
        }
        return data;
    }

    /**
     * Возвращает данные спецификации
     * @param idSpec id спецификации
     * @return
     * @throws Exception
     */
    public Vector getDataSpec(int idSpec) throws Exception {
        Vector dataSpec = new Vector();
        String sql = "Select spec.id_model as id_model, s_model.naim as naim, spec.id_dept as id_dept, spec, date_start, fio, date_ins, basic "
                + "    From spec, s_model, employees "
                + "    Where spec.id = ? and "
                + "	     spec.id_model = s_model.model and "
                + "	     spec.id_employer = employees.id ";
        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, idSpec);
            rs = ps.executeQuery();
            while (rs.next()) {
                dataSpec.add(idSpec);
                dataSpec.add(rs.getInt("id_model"));
                dataSpec.add(rs.getString("naim"));
                dataSpec.add(rs.getInt("id_dept"));
                dataSpec.add(rs.getString("spec"));
                dataSpec.add(new SimpleDateFormat("dd-MM-yyyy").format(rs.getDate("date_start")));
                dataSpec.add(rs.getString("fio"));
                dataSpec.add(new SimpleDateFormat("dd-MM-yyyy").format(rs.getDate("date_ins")));
                dataSpec.add(rs.getBoolean("basic"));
            }

        } catch (Exception e) {
            dataSpec = new Vector();
            System.err.println("Ошибка getDataSpec() " + e);
            log.error("Ошибка getDataSpec()", e);
            throw new Exception("Ошибка getDataSpec() " + e.getMessage(), e);
        }
        return dataSpec;
    }

    /**
     * Возвращает все элементы спецификации
     * @param idSpec id спецификации
     * @return
     * @throws Exception
     */
    public Vector getDataSpecItem(int idSpec) throws Exception {
        Vector dataSpecItem = new Vector();
        String sql = "Select num , operac , id_tech ,tech, category, enorm , vnorm "
                + "     from spec_item, s_tech "
                + "        where id_spec = ? and spec_item.id_tech = s_tech.id"
                + "   Order by num";
        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, idSpec);
            rs = ps.executeQuery();
            while (rs.next()) {
                Vector tmp = new Vector();
                tmp.add(rs.getInt("num"));
                tmp.add(rs.getString("operac"));
                tmp.add(rs.getInt("id_tech"));
                tmp.add(rs.getString("tech"));
                tmp.add(rs.getInt("category"));
                tmp.add(rs.getInt("enorm"));
                tmp.add(UtilSpec.formatNorm(rs.getDouble("vnorm")));
                dataSpecItem.add(tmp);
            }

        } catch (Exception e) {
            dataSpecItem = new Vector();
            System.err.println("Ошибка getDataSpecItem() " + e);
            log.error("Ошибка getDataSpecItem()", e);
            throw new Exception("Ошибка getDataSpecItem() " + e.getMessage(), e);
        }
        return dataSpecItem;
    }

    /**
     * Возвращает все элементы спецификации для корректировки
     * @param idSpec id спецификации
     * @return
     * @throws Exception
     */
    public Vector getDataSpecItemToEdit(int idSpec) throws Exception {
        Vector dataSpecItem = new Vector();
        String sql = "Select num , operac , id_tech ,tech, category, enorm , vnorm, spec_item.id as idSpecItem "
                + "     from spec_item, s_tech "
                + "        where id_spec = ? and spec_item.id_tech = s_tech.id"
                + "   Order by num";
        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, idSpec);
            rs = ps.executeQuery();
            while (rs.next()) {
                Vector tmp = new Vector();

                tmp.add(rs.getInt("num"));
                tmp.add(rs.getString("operac"));
                tmp.add(rs.getInt("id_tech"));
                tmp.add(rs.getString("tech"));
                tmp.add(rs.getInt("category"));
                tmp.add(rs.getInt("enorm"));
                tmp.add(UtilSpec.formatNorm(rs.getDouble("vnorm")));
                tmp.add(rs.getInt("idSpecItem"));
                dataSpecItem.add(tmp);
            }

        } catch (Exception e) {
            dataSpecItem = new Vector();
            System.err.println("Ошибка getDataSpecItem() " + e);
            log.error("Ошибка getDataSpecItem()", e);
            throw new Exception("Ошибка getDataSpecItem() " + e.getMessage(), e);
        }
        return dataSpecItem;
    }

    /**
     * Возвращает всё оборудование спецификации
     * @param idSpec id спецификации
     * @return
     * @throws Exception
     */
    public Vector getDataSpecTech(int idSpec) throws Exception {
        Vector dataSpecTech = new Vector();
        String sql = "Select id_tech, tech, sum(vnorm) as summ From spec_item, s_tech "
                + " Where id_spec = ? and spec_item.id_tech = s_tech.id and spec_item.num >= 0 and s_tech.id <> -1"
                + " Group by id_tech,tech";
        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, idSpec);
            rs = ps.executeQuery();
            while (rs.next()) {
                Vector tmp = new Vector();
                tmp.add(rs.getInt("id_tech"));
                tmp.add(rs.getString("tech"));
                tmp.add(UtilSpec.formatNorm(rs.getDouble("summ")));
                dataSpecTech.add(tmp);
            }

        } catch (Exception e) {
            dataSpecTech = new Vector();
            System.err.println("Ошибка getDataSpecTech() " + e);
            log.error("Ошибка getDataSpecTech()", e);
            throw new Exception("Ошибка getDataSpecTech() " + e.getMessage(), e);
        }
        return dataSpecTech;
    }

    /**
     * Удаляет спецификацию
     * @param idSpec id спецификации
     * @throws Exception
     */
    public void deleteSpec(int idSpec) throws Exception {
        String sql = "";
        try {
            setAutoCommit(false);

            sql = "Delete from spec_item where id = ?";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, idSpec);
            ps.execute();

            sql = "Delete from spec where id = ?";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, idSpec);
            ps.execute();

            commit();
        } catch (Exception e) {
            rollBack();
            System.err.println("Ошибка deleteSpec() " + e);
            log.error("Ошибка deleteSpec()", e);
            throw new Exception("Ошибка deleteSpec() " + e.getMessage(), e);
        } finally {
            setAutoCommit(true);
        }
    }

    public boolean testDeleteSpec(int idSpec) throws Exception {
        boolean rezalt = false;
        String sql = "Select distinct list.id "
                + " From list, list_item, spec, spec_item "
                + " Where list.id = list_item.id_list and "
                + "      list.id_spec = spec.id and "
                + "      spec_item.id = list_item.id_spec_item and "
                + "      spec.id = spec_item.id_spec and 	       "
                + "      spec.id = ? ";
        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, idSpec);
            rs = ps.executeQuery();

            if (rs.next())
                rezalt = true;

        } catch (Exception e) {
            rezalt = true;
            System.err.println("Ошибка testDeleteSpec() " + e);
            log.error("Ошибка testDeleteSpec()", e);
            throw new Exception("Ошибка testDeleteSpec() " + e.getMessage(), e);
        }
        return rezalt;
    }

    /**
     * Поиск спецификации
     * @param idSpec id спецификации
     * @param idDept id подразделения
     * @param idModel id модели
     * @param modelNaim наименование модели
     * @param checkboxSt checkbox периода ввода
     * @param sStDate период ввода
     * @param eStDate период ввода 
     * @param checkboxIns checkbox периода корректировки
     * @param sInsDate период корректировки
     * @param eInsDate период корректировки
     * @return
     * @throws Exception
     */
    public Vector searchSpec(String idSpec, int idDept, String idModel, String modelNaim, boolean checkboxSt,
                             String sStDate, String eStDate, boolean checkboxIns, String sInsDate, String eInsDate) throws Exception {

        Vector dataSpec = new Vector();
        String sql = "Select idModel, depat, kod, spec, date_start, date_ins, sum(vnorm) as vnorm, basic "
                + "   From ( Select  id_model as idModel, dept.department as depat, "
                + "                  spec.id as kod, spec, "
                + "                  date_start, date_ins, basic "
                + "          From spec, dept, s_model "
                + "          Where spec.id_dept = dept.id  "
                + "	           and spec.id::text like '" + idSpec + "%'  "
                + (idDept != -1 ? " and spec.id_dept = " + idDept + " " : " ")
                + "	           and id_model = s_model.model  "
                + "	           and id_model::text like '" + idModel + "%'  "
                + "	           and s_model.naim::text like '" + modelNaim + "%' "
                + (checkboxSt ? " and date_start between '" + sStDate + "  00:00'  and '" + eStDate + " 23:59' " : "")
                + (checkboxIns ? " and date_ins between   '" + sInsDate + " 00:00' and '" + eInsDate + " 23:59' " : "") + ") as t1 "
                + "   left join spec_item on "
                + "           spec_item.id_spec = t1.kod and "
                + "           spec_item.num >= 0 and "
                + "           spec_item.id_tech <> -1  "
                + "   Group by idModel, depat, kod, spec, date_start, date_ins, basic "
                + "   Order by idModel, depat, kod, spec, date_start, date_ins ";

        try {
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                Vector tmp = new Vector();
                tmp.add(false);
                tmp.add(rs.getInt("idModel"));
                tmp.add(rs.getString("depat"));
                tmp.add(rs.getInt("kod"));
                tmp.add(rs.getString("spec"));
                tmp.add(UtilSpec.formatNorm(rs.getDouble("vnorm")));
                tmp.add(new SimpleDateFormat("dd-MM-yyyy").format(rs.getDate("date_start")));
                tmp.add(new SimpleDateFormat("dd-MM-yyyy").format(rs.getDate("date_ins")));
                tmp.add(rs.getBoolean("basic"));
                dataSpec.add(tmp);
            }

        } catch (Exception e) {
            dataSpec = new Vector();
            System.err.println("Ошибка searchSpec() " + e);
            log.error("Ошибка searchSpec()", e);
            throw new Exception("Ошибка searchSpec() " + e.getMessage(), e);
        }
        return dataSpec;
    }

    /**
     * Поиск спецификации
     * @param idEmployer id пользователя
     * @param sData дата ввода
     * @return
     * @throws Exception
     */
    public Vector searchSpec(int idEmployer, long sData) throws Exception {
        Vector dataSpec = new Vector();
        String sql = "Select id_model, dept.department as depat, spec.id as kod, spec, sum(vnorm) as vnorm, date_start, date_ins, basic "
                + "     From spec, spec_item, dept, s_model, employees"
                + "     Where spec.id_dept = dept.id and "
                + "           spec.id = spec_item.id_spec and  "
                + "           spec_item.num >= 0 and "
                + "           spec_item.id_tech <> -1 and "
                + "           spec.id_model = s_model.model and "
                + "           spec.id_employer = employees.id and"
                + "           employees.id = ? and"
                + "           date_start between ? and now() "
                + "      Group by id_model, depat, kod, spec, date_start, date_ins, basic "
                + "      Order by id_model, depat, kod, spec, date_start, date_ins";
        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, idEmployer);
            ps.setDate(2, new java.sql.Date(sData));
            rs = ps.executeQuery();
            while (rs.next()) {
                Vector tmp = new Vector();
                tmp.add(false);
                tmp.add(rs.getInt("id_model"));
                tmp.add(rs.getString("depat"));
                tmp.add(rs.getInt("kod"));
                tmp.add(rs.getString("spec"));
                tmp.add(UtilSpec.formatNorm(rs.getDouble("vnorm")));
                tmp.add(new SimpleDateFormat("dd-MM-yyyy").format(rs.getDate("date_start")));
                tmp.add(new SimpleDateFormat("dd-MM-yyyy").format(rs.getDate("date_ins")));
                tmp.add(rs.getBoolean("basic"));
                dataSpec.add(tmp);
            }

        } catch (Exception e) {
            dataSpec = new Vector();
            System.err.println("Ошибка searchSpec() " + e);
            log.error("Ошибка searchSpec()", e);
            throw new Exception("Ошибка searchSpec() " + e.getMessage(), e);
        }
        return dataSpec;
    }

    public int searchListItem(int idSpecItem) throws Exception {
        int kol_list = 0;
        String sql = "Select count(distinct list.id) as kolList "
                + " From list, list_item "
                + " Where list.id = list_item.id_list and "
                + "       id_spec_item = ? ";
        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, idSpecItem);
            rs = ps.executeQuery();
            while (rs.next())
                kol_list = rs.getInt("kolList");

        } catch (Exception e) {
            kol_list = 0;
            System.err.println("Ошибка searchListItem() " + e);
            log.error("Ошибка searchListItem()", e);
            throw new Exception("Ошибка searchListItem() " + e.getMessage(), e);
        }
        return kol_list;
    }


    Vector checkSpecModels(int fas, int idDept) throws Exception {
        Vector rez = new Vector();
        String sql = "Select id, id_model, spec  "
                + " From spec "
                + " Where id_model = ? and id_dept = ?"
                + " Order by id ";
        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, fas);
            ps.setInt(2, idDept);
            rs = ps.executeQuery();
            while (rs.next()) {
                Vector tmp = new Vector();
                tmp.add(rs.getInt("id_model"));
                tmp.add(rs.getString("spec").trim());
                rez.add(tmp);
            }


        } catch (Exception e) {
            rez = new Vector();
            System.err.println("Ошибка checkSpecModels() " + e);
            log.error("Ошибка checkSpecModels()", e);
            throw new Exception("Ошибка checkSpecModels() " + e.getMessage(), e);
        }
        return rez;
    }

    String searchTech(int id) throws Exception {
        String rez = "";
        String sql = "Select tech  "
                + " From s_tech "
                + " Where id = ?";
        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, id);

            rs = ps.executeQuery();
            if (rs.next())
                rez = rs.getString("tech");

        } catch (Exception e) {
            rez = "";
            System.err.println("Ошибка searchTech() " + e);
            log.error("Ошибка searchTech()", e);
            throw new Exception("Ошибка searchTech() " + e.getMessage(), e);
        }
        return rez;

    }


    /**
     * Возвращает все спецификации
     * @param paramSortModel (true - сортирует по модели; false -сортирует по дате коррект.;)
     * @return
     * @throws Exception
     */

    public List<SpecificationItem> getAllSpecifications() throws Exception {
        List<SpecificationItem> result = new ArrayList<>();
        String sql = "Select idModel, sum(vnorm) as vnorm "
                + "   From ( Select  id_model as idModel, dept.department as depat, "
                + "                  spec.id as kod, spec, "
                + "                  date_start, date_ins, basic "
                + "          From spec, dept "
                + "          Where spec.id_dept = dept.id and basic=true ) as t1 "
                + "   left join spec_item on "
                + "           spec_item.id_spec = t1.kod and "
                + "           spec_item.num >= 0 and "
                + "           spec_item.id_tech <> -1 "
                + "   Group by idModel, depat, kod, spec, date_start, date_ins, basic ";

        try {
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                SpecificationItem tmp = new SpecificationItem();
                tmp.setModelNumber(rs.getInt("idModel"));
                tmp.setRating(rs.getDouble("vnorm"));
                result.add(tmp);
            }

        } catch (Exception e) {
            System.err.println("Ошибка getAllSpecifications() " + e);
            log.error("Ошибка getAllSpecifications()", e);
            throw new Exception("Ошибка getAllSpecifications() " + e.getMessage(), e);
        }
        return result;
    }

    public List<ViewSeamstressItem> getSeamstressMap(DatePeriod period) throws Exception {
        List<ViewSeamstressItem> result = new ArrayList<>();
        String sql = "SELECT * FROM v_seamsterss_production_map_test WHERE date_vvod between ? and ? and status = 1 " +
                " ORDER BY document_id, operation_type, worker_name ";

        try {
            ps = conn.prepareStatement(sql);
            ps.setDate(1, new java.sql.Date(period.getBegin().getTime()));
            ps.setDate(2, new java.sql.Date(period.getEnd().getTime()));

            rs = ps.executeQuery();
            while (rs.next()) {
                ViewSeamstressItem tmp = new ViewSeamstressItem();
                tmp.setId(rs.getInt("id"));
                tmp.setDocumentId(rs.getInt("document_id"));
                tmp.setDocumentDate(rs.getDate("date_vvod"));
                tmp.setWorkerName(rs.getString("worker_name"));
                tmp.setWorkerNumber(rs.getInt("worker_number"));
                tmp.setBrigadeCode(rs.getInt("kod"));
                tmp.setModelNumber(rs.getInt("id_model"));
                tmp.setOperationType(rs.getInt("operation_type"));
                tmp.setOperationName(rs.getString("operation_name"));
                tmp.setMachineName(rs.getString("tech"));
                tmp.setAmount(rs.getInt("work_amount"));

                tmp.setNormRate(rs.getDouble("vnorm"));
                tmp.setNormRateAmount(rs.getDouble("norm_by_amount"));
                tmp.setBrigadierName(rs.getString("brigadier_name"));
                result.add(tmp);
            }

        } catch (Exception e) {
            System.err.println("Ошибка getSeamstressMap() " + e);
            log.error("Ошибка getSeamstressMap()", e);
            throw new Exception("Ошибка getSeamstressMap() " + e.getMessage(), e);
        }
        return result;
    }

}

