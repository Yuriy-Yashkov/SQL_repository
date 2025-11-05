package dept.sprav.employe;

import by.march8.ecs.framework.common.LogCrutch;
import workDB.PDB_new;

import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.Vector;

/**
 *
 * @author lidashka
 */
public class EmployePDB extends PDB_new {
    // static final Logger log = new Log().getLoger(EmployePDB.class);
    private static final LogCrutch log = new LogCrutch();

    public Vector getDept() throws Exception {
        Vector dept = new Vector();
        try {
            ps = conn.prepareStatement("Select * from dept Order by department");
            rs = ps.executeQuery();
            while (rs.next()) {
                Vector tmp = new Vector();
                tmp.add(rs.getInt(1));
                tmp.add(rs.getString(2));
                dept.add(tmp);
            }
        } catch (Exception e) {
            System.err.println("Ошибка getDept() " + e);
            log.error("Ошибка getDept()", e);
            throw new Exception("Ошибка getDept(): " + e.getMessage(), e);
        }
        return dept;
    }

    public Vector getBrig() throws Exception {
        Vector brig = new Vector();
        try {
            ps = conn.prepareStatement("Select id, kod, brig from s_brig Order by kod");
            rs = ps.executeQuery();
            while (rs.next()) {
                Vector tmp = new Vector();
                tmp.add(rs.getInt("id"));
                tmp.add(rs.getInt("kod"));
                tmp.add(rs.getString("brig"));
                brig.add(tmp);
            }
        } catch (Exception e) {
            System.err.println("Ошибка getBrig() " + e);
            log.error("Ошибка getBrig()", e);
            throw new Exception("Ошибка getBrig(): " + e.getMessage(), e);
        }
        return brig;
    }

    public Vector getProfession() throws Exception {
        Vector dept = new Vector();
        try {
            ps = conn.prepareStatement("Select * from employees_pro Order by profession");
            rs = ps.executeQuery();
            while (rs.next()) {
                Vector tmp = new Vector();
                tmp.add(rs.getInt(1));
                tmp.add(rs.getString(2));
                dept.add(tmp);
            }
        } catch (Exception e) {
            System.err.println("Ошибка getProfession() " + e);
            log.error("Ошибка getProfession()", e);
            throw new Exception("Ошибка getProfession(): " + e.getMessage(), e);
        }
        return dept;
    }

    public Vector getAllEmployees() throws Exception {
        Vector employees = new Vector();

        String sql = "Select idEmploye, fio, department, num, id_users, razryad, kodBrig, profession, note_empl "
                + " From infoemployees "
                + " Order by department, kodBrig, fio ";

        try {
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                Vector employe = new Vector();
                employe.add(rs.getInt("idEmploye"));
                employe.add(rs.getString("fio"));
                employe.add(rs.getString("department"));
                employe.add(rs.getInt("kodBrig"));
                employe.add(rs.getInt("num"));
                employe.add(rs.getInt("razryad"));

                if (rs.getInt("id_users") > 0) employe.add("да");
                else employe.add("нет");

                employe.add(rs.getString("profession"));
                employe.add(rs.getString("note_empl"));

                employees.add(employe);
            }
        } catch (Exception e) {
            System.err.println("Ошибка getAllEmployees() " + e);
            log.error("Ошибка getAllEmployees()", e);
            throw new Exception("Ошибка getAllEmployees(): " + e.getMessage(), e);
        }
        return employees;
    }

    public Vector getAllEmployees(Vector brigs) throws Exception {
        Vector employees = new Vector();
        String sql = "";

        try {
            // Брак
            sql = " SELECT t9.idemploye, t9.fio, t9.iddept, t9.department, t9.num, t9.id_pro,  employees_pro.profession, t9.note_empl, t9.id_users, t9.login, t9.password, "
                    + "    t9.idh_razryad, t9.dater, t9.razryad, t9.idh_brig, t9.dateb, t9.idbrig,  t9.kodbrig, t9.brig"
                    + "   FROM ( SELECT t8.idemploye, t8.fio, t8.iddept, t8.department, t8.num, t8.id_pro, t8.note_empl, t8.id_users, t8.login, t8.password, t8.idh_razryad, t8.dater, t8.razryad, t8.idh_brig, t8.dateb, s_brig.id AS idbrig, s_brig.kod AS kodbrig, s_brig.brig"
                    + "           FROM ( SELECT t7.idemploye, t7.fio, t7.iddept, t7.department, t7.num, t7.id_users, t7.id_pro, t7.note_empl, t7.login, t7.password, t7.idh_razryad, t7.dater, t7.razryad, h_brig.id AS idh_brig, t7.dateb, h_brig.id_brig"
                    + "                   FROM ( SELECT t6.idemploye, t6.fio, t6.iddept, t6.department, t6.num, t6.id_users, t6.id_pro, t6.note_empl, t6.login, t6.password, h_razryad.id AS idh_razryad, t6.dater, h_razryad.razryad, t6.dateb"
                    + "                           FROM ( SELECT t4.idemploye, t4.fio, t4.iddept, t4.department, t4.num, t4.id_users, t4.id_pro, t4.note_empl, t4.login, t4.password, t4.dater, t5.dateb"
                    + "                                   FROM ( SELECT t2.idemploye, t2.fio, t2.iddept, t2.department, t2.num,  t2.id_users, t2.id_pro,  t2.note_empl, t2.login, t2.password,  t1.dater"
                    + "                                           FROM ( SELECT t3.idemploye, t3.fio, t3.num, t3.iddept, t3.department, t3.id_users, t3.id_pro, t3.note_empl, users.login, users.password"
                    + "                                                   FROM ( SELECT employees.id AS idemploye, employees.fio, employees.id_dept AS iddept, dept.department, employees.num,  employees.id_users, employees.id_pro, employees.note_empl "
                    + "                                                          FROM employees, dept "
                    + "                                                          WHERE dept.id = employees.id_dept AND employees.id = -1 ) t3"
                    + "                                              LEFT JOIN users ON users.id = t3.id_users) t2"
                    + "                                      LEFT JOIN ( SELECT max(h_razryad.date) AS dater, "
                    + "                                                    h_razryad.id_employe"
                    + "                                                   FROM h_razryad"
                    + "                                                  WHERE h_razryad.date < now()"
                    + "                                                  GROUP BY h_razryad.id_employe) t1 ON t1.id_employe = t2.idemploye) t4"
                    + "                              LEFT JOIN ( SELECT max(h_brig.date) AS dateb, "
                    + "                                            h_brig.id_employe"
                    + "                                           FROM h_brig"
                    + "                                          WHERE h_brig.date < now()"
                    + "                                          GROUP BY h_brig.id_employe) t5 ON t5.id_employe = t4.idemploye) t6"
                    + "                      LEFT JOIN h_razryad ON t6.dater = h_razryad.date AND t6.idemploye = h_razryad.id_employe) t7"
                    + "              LEFT JOIN h_brig ON t7.dateb = h_brig.date AND t7.idemploye = h_brig.id_employe) t8"
                    + "      LEFT JOIN s_brig ON t8.id_brig = s_brig.id) t9"
                    + "   LEFT JOIN employees_pro ON t9.id_pro = employees_pro.id ";

            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                Vector employe = new Vector();
                employe.add(false);
                employe.add(rs.getInt("idEmploye"));
                employe.add(rs.getString("fio"));
                employe.add(rs.getString("department"));
                employe.add(rs.getInt("kodBrig"));
                employe.add(rs.getInt("num"));
                employe.add(rs.getInt("razryad"));
                employe.add(rs.getInt("id_users") > 0 ? "да" : "нет");
                employe.add(rs.getString("profession"));
                employe.add(rs.getString("note_empl"));
                employees.add(employe);
            }

            // Сотрудники             
            if (!brigs.isEmpty()) {
                for (Iterator it = brigs.iterator(); it.hasNext(); ) {
                    int idBrig = Integer.valueOf(it.next().toString());

                    sql = "Select idEmploye, fio, idDept, department, num, id_users, razryad, kodBrig, profession, note_empl "
                            + "From infoemployees "
                            + (idBrig != -1 ? " Where idbrig = " + idBrig + " " : " ")
                            + "Order by department, kodBrig, fio ";

                    ps = conn.prepareStatement(sql);
                    rs = ps.executeQuery();
                    while (rs.next()) {
                        Vector employe = new Vector();
                        employe.add(false);
                        employe.add(rs.getInt("idEmploye"));
                        employe.add(rs.getString("fio"));
                        employe.add(rs.getString("department"));
                        employe.add(rs.getInt("kodBrig"));
                        employe.add(rs.getInt("num"));
                        employe.add(rs.getInt("razryad"));
                        employe.add(rs.getInt("id_users") > 0 ? "да" : "нет");
                        employe.add(rs.getString("profession"));
                        employe.add(rs.getString("note_empl"));
                        employees.add(employe);
                    }
                }
            } else {
                sql = "Select idEmploye, fio, idDept, department, num, id_users, razryad, kodBrig, profession, note_empl "
                        + "From infoemployees "
                        + "Order by department, kodBrig, fio ";

                ps = conn.prepareStatement(sql);
                rs = ps.executeQuery();
                while (rs.next()) {
                    Vector employe = new Vector();
                    employe.add(false);
                    employe.add(rs.getInt("idEmploye"));
                    employe.add(rs.getString("fio"));
                    employe.add(rs.getString("department"));
                    employe.add(rs.getInt("kodBrig"));
                    employe.add(rs.getInt("num"));
                    employe.add(rs.getInt("razryad"));
                    employe.add(rs.getInt("id_users") > 0 ? "да" : "нет");
                    employe.add(rs.getString("profession"));
                    employe.add(rs.getString("note_empl"));
                    employees.add(employe);
                }
            }

        } catch (Exception e) {
            System.err.println("Ошибка getAllEmployees() " + e);
            log.error("Ошибка getAllEmployees()", e);
            throw new Exception("Ошибка getAllEmployees(): " + e.getMessage(), e);
        }
        return employees;
    }

    public int getIdEmploye(int idUser) throws Exception {
        int idEmploye = -1;
        try {
            ps = conn.prepareStatement("Select id from employees where id_users = '" + idUser + "' ");
            rs = ps.executeQuery();
            if (rs.next())
                idEmploye = rs.getInt(1);
        } catch (Exception e) {
            System.err.println("Ошибка getIdEmploye() " + e);
            log.error("Ошибка getIdEmploye()", e);
            throw new Exception("Ошибка getIdEmploye(): " + e.getMessage(), e);
        }
        return idEmploye;
    }

    public Vector getEmploye(int id) throws Exception {
        Vector employe = new Vector();

        String sql = "Select idEmploye, fio, idDept, department, num, "
                + "          id_users, login, password, razryad, "
                + "          id_pro, profession, note_empl, idBrig, dateR, dateB "
                + " From (Select idEmploye, fio, idDept, department, num, "
                + "          id_users, login, password, razryad, "
                + "          id_pro, note_empl, idBrig, dateR, dateB "
                + "         From infoemployees "
                + "         Where idEmploye = ? ) as t1"
                + " left join employees_pro on "
                + " t1.id_pro = employees_pro.id ";

        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();
            if (rs.next()) {
                employe.add(rs.getInt("idEmploye"));
                employe.add(rs.getString("fio"));
                employe.add(rs.getInt("num"));
                employe.add(rs.getString("department"));
                employe.add(rs.getInt("idBrig"));
                employe.add(rs.getString("dateB") == null ? null : new SimpleDateFormat("dd-MM-yyyy").format(rs.getDate("dateB")));
                employe.add(rs.getInt("razryad"));
                employe.add(rs.getString("dateR") == null ? null : new SimpleDateFormat("dd-MM-yyyy").format(rs.getDate("dateR")));
                employe.add(rs.getInt("id_users"));
                employe.add(rs.getString("login"));
                employe.add(rs.getString("password"));
                employe.add(rs.getString("profession"));
                employe.add(rs.getString("note_empl").trim());
            }
        } catch (Exception e) {
            System.err.println("Ошибка getEmploye() " + e);
            log.error("Ошибка getEmploye()", e);
            throw new Exception("Ошибка getEmploye(): " + e.getMessage(), e);
        }
        return employe;
    }

    public boolean deleteEmploye(int id) throws Exception {
        boolean rezalt = false;
        try {
            ps = conn.prepareStatement("Delete from employees where employees.id = '" + id + "'");
            ps.executeUpdate();
        } catch (Exception e) {
            System.err.println("Ошибка deleteEmploye() " + e);
            log.error("Ошибка deleteEmploye()", e);
            throw new Exception("Ошибка deleteEmploye(): " + e.getMessage(), e);
        }
        return rezalt;
    }

    public boolean saveNewEmploye(String fio, int idDept, int num, int idUser, int razryad, int idBrig,
                                  long sDateR, long sDateB, int idPro, String note) throws Exception {
        boolean rezalt = false;
        int id = 0;

        String sql = "";

        try {
            setAutoCommit(false);

            sql = "INSERT INTO employees (fio, num, id_dept, id_pro, note_empl) "
                    + " VALUES(?, ?, ?, ?, ?) RETURNING id";
            ps = conn.prepareStatement(sql);
            ps.setString(1, fio);
            ps.setInt(2, num);
            ps.setInt(3, idDept);
            ps.setInt(4, idPro);
            ps.setString(5, note);
            rs = ps.executeQuery();

            if (rs.next()) id = rs.getInt(1);

            if (idUser > 0) {
                ps = conn.prepareStatement("UPDATE employees SET id_users = " + idUser + "  where id = " + id + "");
                ps.executeUpdate();
            }

            sql = "Select * from h_razryad where razryad = ? and date = ? and id_employe = ? ";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, razryad);
            ps.setDate(2, new java.sql.Date(sDateR));
            ps.setInt(3, id);
            rs = ps.executeQuery();

            if (!rs.next()) {
                sql = "INSERT INTO h_razryad (id_employe,razryad,date) VALUES(?,?,?) ";
                ps = conn.prepareStatement(sql);
                ps.setInt(1, id);
                ps.setInt(2, razryad);
                ps.setDate(3, new java.sql.Date(sDateR));
                ps.executeUpdate();
            }

            sql = "Select * from h_razryad where razryad = ? and date = ? and id_employe = ? ";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, razryad);
            ps.setDate(2, new java.sql.Date(sDateR));
            ps.setInt(3, id);
            rs = ps.executeQuery();

            if (!rs.next()) {
                sql = "INSERT INTO h_razryad (id_employe,razryad,date) VALUES(?,?,?) ";
                ps = conn.prepareStatement(sql);
                ps.setInt(1, id);
                ps.setInt(2, razryad);
                ps.setDate(3, new java.sql.Date(sDateR));
                ps.executeUpdate();
            }

            sql = "Select * from h_brig where id_brig = ? and date = ? and id_employe = ? ";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, idBrig);
            ps.setDate(2, new java.sql.Date(sDateB));
            ps.setInt(3, id);
            rs = ps.executeQuery();

            if (!rs.next()) {
                sql = "INSERT INTO h_brig (id_employe,id_brig,date) VALUES(?,?,?) ";
                ps = conn.prepareStatement(sql);
                ps.setInt(1, id);
                ps.setInt(2, idBrig);
                ps.setDate(3, new java.sql.Date(sDateB));
                ps.executeUpdate();
            }

            rezalt = true;
            commit();
        } catch (Exception e) {
            rollBack();
            System.err.println("Ошибка saveNewEmploye() " + e);
            log.error("Ошибка saveNewEmploye()", e);
            throw new Exception("Ошибка saveNewEmploye(): " + e.getMessage(), e);
        } finally {
            setAutoCommit(true);
        }
        return rezalt;
    }

    public void saveEditEmploye(int idEmploye, String fio, int idDept, int num, int idUser,
                                int razryad, int idBrig, long sDateR, long sDateB, int idPro, String note) throws Exception {
        String sql;

        try {
            setAutoCommit(false);

            sql = " UPDATE employees "
                    + " SET fio = ?, num = ?, id_dept = ?, id_pro = ?, note_empl = ? "
                    + " Where id = ? ";
            ps = conn.prepareStatement(sql);
            ps.setString(1, fio);
            ps.setInt(2, num);
            ps.setInt(3, idDept);
            ps.setInt(4, idPro);
            ps.setString(5, note);
            ps.setInt(6, idEmploye);
            ps.executeUpdate();

            sql = "Select id from h_razryad where date = ? and id_employe = ? ";
            ps = conn.prepareStatement(sql);
            ps.setDate(1, new java.sql.Date(sDateR));
            ps.setInt(2, idEmploye);
            rs = ps.executeQuery();

            if (!rs.next()) {
                sql = "Insert into h_razryad (id_employe,razryad,date) values (?,?,?) ";
                ps = conn.prepareStatement(sql);
                ps.setInt(1, idEmploye);
                ps.setInt(2, razryad);
                ps.setDate(3, new java.sql.Date(sDateR));
                ps.executeUpdate();
            } else {
                sql = "UPDATE h_razryad set razryad = ?, date = ? where id = ?  ";
                ps = conn.prepareStatement(sql);
                ps.setInt(1, razryad);
                ps.setDate(2, new java.sql.Date(sDateR));
                ps.setInt(3, rs.getInt(1));
                ps.executeUpdate();
            }

            sql = "Select id from h_brig where date = ? and id_employe = ? ";
            ps = conn.prepareStatement(sql);
            ps.setDate(1, new java.sql.Date(sDateB));
            ps.setInt(2, idEmploye);
            rs = ps.executeQuery();

            if (!rs.next()) {
                sql = "Insert into h_brig (id_employe,id_brig,date) values (?,?,?) ";
                ps = conn.prepareStatement(sql);
                ps.setInt(1, idEmploye);
                ps.setInt(2, idBrig);
                ps.setDate(3, new java.sql.Date(sDateB));
                ps.executeUpdate();
            } else {
                sql = "UPDATE h_brig set id_brig = ?, date = ? where id = ? ";
                ps = conn.prepareStatement(sql);
                ps.setInt(1, idBrig);
                ps.setDate(2, new java.sql.Date(sDateB));
                ps.setInt(3, rs.getInt(1));
                ps.executeUpdate();
            }

            if (idUser > 0) {
                sql = "UPDATE employees SET id_users = ? where id = ? ";
                ps = conn.prepareStatement(sql);
                ps.setInt(1, idUser);
                ps.setInt(2, idEmploye);
                ps.executeUpdate();

            } else if (idUser == 0) {
                sql = "UPDATE employees SET id_users = ? where id = ? ";
                ps = conn.prepareStatement(sql);
                ps.setObject(1, null);
                ps.setInt(2, idEmploye);
                ps.executeUpdate();
            }

            commit();
        } catch (Exception e) {
            rollBack();
            System.err.println("Ошибка saveEditEmploye() " + e);
            log.error("Ошибка saveEditEmploye()", e);
            throw new Exception("Ошибка saveEditEmploye(): " + e.getMessage(), e);
        } finally {
            setAutoCommit(true);
        }
    }

    public int testLoginPassUser(String login, String pass) throws Exception {
        int rezalt = -1;
        try {
            ps = conn.prepareStatement("Select id from users where login = '" + login + "' and password = '" + pass + "'");
            rs = ps.executeQuery();
            if (rs.next())
                rezalt = rs.getInt(1);
        } catch (Exception e) {
            System.err.println("Ошибка testLoginPassUser() " + e);
            log.error("Ошибка testLoginPassUser()", e);
            throw new Exception("Ошибка testLoginPassUser(): " + e.getMessage(), e);
        }
        return rezalt;
    }

    public Vector getHistoryRazryad(int idEmp) throws Exception {
        Vector rezalt = new Vector();
        String sql = "Select razryad, date "
                + " From h_razryad "
                + " Where id_employe = ? "
                + " Order by date,razryad  ";
        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, idEmp);
            rs = ps.executeQuery();

            while (rs.next()) {
                Vector tmp = new Vector();
                tmp.add(rs.getInt("razryad"));
                tmp.add(rs.getString("date") == null ? "" : new SimpleDateFormat("dd-MM-yyyy").format(rs.getDate("date")));

                rezalt.add(tmp);
            }

        } catch (Exception e) {
            rezalt = new Vector();
            System.err.println("Ошибка getAllRazryad() " + e);
            log.error("Ошибка getAllRazryad()", e);
            throw new Exception("Ошибка getAllRazryad(): " + e.getMessage(), e);
        }
        return rezalt;
    }

    public Vector getHistoryBrig(int idEmp) throws Exception {
        Vector rezalt = new Vector();
        String sql = "Select s_brig.kod as kodBrig, brig, h_brig.date "
                + "   From s_brig, h_brig "
                + "   Where s_brig.id = h_brig.id_brig and "
                + "         id_employe = ? "
                + "   Order by h_brig.date ";
        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, idEmp);
            rs = ps.executeQuery();

            while (rs.next()) {
                Vector tmp = new Vector();
                tmp.add(rs.getInt("kodBrig"));
                tmp.add(rs.getString("brig"));
                tmp.add(rs.getString("date") == null ? "" : new SimpleDateFormat("dd-MM-yyyy").format(rs.getDate("date")));

                rezalt.add(tmp);
            }

        } catch (Exception e) {
            rezalt = new Vector();
            System.err.println("Ошибка getHistoryBrig() " + e);
            log.error("Ошибка getHistoryBrig()", e);
            throw new Exception("Ошибка getHistoryBrig(): " + e.getMessage(), e);
        }
        return rezalt;
    }

    public Vector getAllBrig(Vector brigs) throws Exception {
        Vector rezalt = new Vector();
        String sql = "Select distinct s_brig.id as idBrig, s_brig.kod as kodBrig "
                + "   From s_brig, h_brig "
                + "   Where s_brig.id = h_brig.id_brig "
                + "   Order by  kodBrig, idBrig ";
        try {
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                Vector tmp = new Vector();
                if (!brigs.isEmpty())
                    tmp.add(brigs.contains(rs.getString("idBrig")) ? true : false);
                else
                    tmp.add(true);
                tmp.add(rs.getInt("kodBrig"));
                tmp.add(rs.getInt("idBrig"));
                rezalt.add(tmp);
            }

        } catch (Exception e) {
            rezalt = new Vector();
            System.err.println("Ошибка getHistoryBrig() " + e);
            log.error("Ошибка getHistoryBrig()", e);
            throw new Exception("Ошибка getHistoryBrig(): " + e.getMessage(), e);
        }
        return rezalt;
    }
}

