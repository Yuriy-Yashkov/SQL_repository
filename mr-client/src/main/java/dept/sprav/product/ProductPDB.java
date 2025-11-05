package dept.sprav.product;

import by.march8.ecs.framework.common.LogCrutch;
import workDB.PDB_new;

import java.util.Vector;

//import org.apache.log4j.Logger;

/**
 *
 * @author lidashka
 */
public class ProductPDB extends PDB_new {
    // private static final Logger log = new Log().getLoger(ProductPDB.class);
    private static final LogCrutch log = new LogCrutch();

    Vector getIzdName() throws Exception {
        Vector vec = new Vector();
        String sql = "";
        try {
            sql = " Select distinct name From izd Order by name ";

            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                Vector tmp = new Vector();
                tmp.add(-1);
                tmp.add(rs.getString("nassort"));
                tmp.add("");
                vec.add(tmp);
            }
        } catch (Exception e) {
            System.err.println("Ошибка getIzdAssort() " + e);
            log.error("Ошибка getIzdAssort()", e);
            throw new Exception("Ошибка getIzdAssort() " + e.getMessage(), e);
        }
        return vec;
    }

    Vector getIzdBrand() throws Exception {
        Vector vec = new Vector();
        String sql = "";
        try {
            sql = " Select distinct id, nbrand, note "
                    + " From s_brand "
                    + " Order by nbrand ";

            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                Vector tmp = new Vector();
                tmp.add(rs.getInt("id"));
                tmp.add(rs.getString("nbrand"));
                tmp.add(rs.getString("note"));
                vec.add(tmp);
            }
        } catch (Exception e) {
            System.err.println("Ошибка getIzdBrand() " + e);
            log.error("Ошибка getIzdBrand()", e);
            throw new Exception("Ошибка getIzdBrand() " + e.getMessage(), e);
        }
        return vec;
    }

    Vector getIzdIzm() throws Exception {
        Vector vec = new Vector();
        String sql = "";
        try {
            sql = " Select distinct id, nizm, note "
                    + " From s_izm "
                    + " Order by nizm ";

            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                Vector tmp = new Vector();
                tmp.add(rs.getInt("id"));
                tmp.add(rs.getString("nizm"));
                tmp.add(rs.getString("note"));
                vec.add(tmp);
            }
        } catch (Exception e) {
            System.err.println("Ошибка getIzdIzm() " + e);
            log.error("Ошибка getIzdIzm()", e);
            throw new Exception("Ошибка getIzdIzm() " + e.getMessage(), e);
        }
        return vec;
    }

    Vector getIzdTip() throws Exception {
        Vector vec = new Vector();
        String sql = "";
        try {
            sql = " Select distinct id, ntip, note "
                    + " From s_tip "
                    + " Order by ntip ";

            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                Vector tmp = new Vector();
                tmp.add(rs.getInt("id"));
                tmp.add(rs.getString("ntip"));
                tmp.add(rs.getString("note"));
                vec.add(tmp);
            }
        } catch (Exception e) {
            System.err.println("Ошибка getIzdTip() " + e);
            log.error("Ошибка getIzdTip()", e);
            throw new Exception("Ошибка getIzdTip() " + e.getMessage(), e);
        }
        return vec;
    }

    Vector getIzdVid() throws Exception {
        Vector vec = new Vector();
        String sql = "";
        try {
            sql = " Select distinct id, nvid, note "
                    + " From s_vid "
                    + " Order by nvid ";

            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                Vector tmp = new Vector();
                tmp.add(rs.getInt("id"));
                tmp.add(rs.getString("nvid"));
                tmp.add(rs.getString("note"));
                vec.add(tmp);
            }
        } catch (Exception e) {
            System.err.println("Ошибка getIzdVid() " + e);
            log.error("Ошибка getIzdVid()", e);
            throw new Exception("Ошибка getIzdVid() " + e.getMessage(), e);
        }
        return vec;
    }

    Vector getIzdGroup() throws Exception {
        Vector vec = new Vector();
        String sql = "";
        try {
            sql = " Select distinct id, ngroup, note "
                    + " From s_group "
                    + " Order by ngroup ";

            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                Vector tmp = new Vector();
                tmp.add(rs.getInt("id"));
                tmp.add(rs.getString("ngroup"));
                tmp.add(rs.getString("note"));
                vec.add(tmp);
            }
        } catch (Exception e) {
            System.err.println("Ошибка getIzdGroup() " + e);
            log.error("Ошибка getIzdGroup()", e);
            throw new Exception("Ошибка getIzdGroup() " + e.getMessage(), e);
        }
        return vec;
    }

    Vector getIzdAssort() throws Exception {
        Vector vec = new Vector();
        String sql = "";
        try {
            sql = " Select distinct id, nassort, note "
                    + " From s_assort "
                    + " Order by nassort ";

            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                Vector tmp = new Vector();
                tmp.add(rs.getInt("id"));
                tmp.add(rs.getString("nassort"));
                tmp.add(rs.getString("note"));
                vec.add(tmp);
            }
        } catch (Exception e) {
            System.err.println("Ошибка getIzdAssort() " + e);
            log.error("Ошибка getIzdAssort()", e);
            throw new Exception("Ошибка getIzdAssort() " + e.getMessage(), e);
        }
        return vec;
    }

    /**
     * Возвращает все коды в соответствии с типом
     *
     * @param type
     * @return
     * @throws Exception
     */
    public Vector getIzdKod(int type) throws Exception {
        Vector vec = new Vector();
        String sql = "";
        try {
            sql = " Select distinct id, kod_item, note "
                    + " From s_kod_item "
                    + " Where id_kod = ? "
                    + " Order by kod_item ";

            ps = conn.prepareStatement(sql);
            ps.setInt(1, type);
            rs = ps.executeQuery();

            while (rs.next()) {
                Vector tmp = new Vector();
                tmp.add(rs.getInt("id"));
                tmp.add(rs.getString("kod_item"));
                tmp.add(rs.getString("note"));
                vec.add(tmp);
            }
        } catch (Exception e) {
            System.err.println("Ошибка getIzdKod() " + e);
            log.error("Ошибка getIzdKod()", e);
            throw new Exception("Ошибка getIzdKod() " + e.getMessage(), e);
        }
        return vec;
    }

    Vector getEmpl(int type) throws Exception {
        Vector vec = new Vector();
        String sql = "";
        try {
            sql = " Select distinct id, fio, note_empl "
                    + " From employees "
                    + " Where id_dept = ? "
                    + " Order by fio ";

            ps = conn.prepareStatement(sql);
            ps.setInt(1, type);
            rs = ps.executeQuery();

            while (rs.next()) {
                Vector tmp = new Vector();
                tmp.add(rs.getInt("id"));
                tmp.add(rs.getString("fio"));
                tmp.add(rs.getString("note_empl"));
                vec.add(tmp);
            }
        } catch (Exception e) {
            System.err.println("Ошибка getEmpl() " + e);
            log.error("Ошибка getEmpl()", e);
            throw new Exception("Ошибка getEmpl() " + e.getMessage(), e);
        }
        return vec;
    }

    Vector getTehOp(int type) throws Exception {
        Vector vec = new Vector();
        String sql = "";
        try {
            sql = " Select distinct id, num, tip_tehop, ntehop, val1, val2, note "
                    + " From s_tehop "
                    + " Where tip_tehop = ? "
                    + " Order by ntehop ";

            ps = conn.prepareStatement(sql);
            ps.setInt(1, type);
            rs = ps.executeQuery();

            while (rs.next()) {
                Vector tmp = new Vector();
                tmp.add(rs.getInt("id"));
                tmp.add(rs.getString("num"));
                tmp.add(rs.getInt("tip_tehop"));
                tmp.add(UtilProduct.getNameTipTO(rs.getInt("tip_tehop")));
                tmp.add(rs.getString("ntehop"));
                tmp.add(rs.getDouble("val1"));
                tmp.add(rs.getDouble("val2"));
                tmp.add(rs.getString("note"));
                vec.add(tmp);
            }
        } catch (Exception e) {
            System.err.println("Ошибка getTehOp() " + e);
            log.error("Ошибка getTehOp()", e);
            throw new Exception("Ошибка getTehOp() " + e.getMessage(), e);
        }
        return vec;
    }

    boolean addIzdBrand(String name, String note) throws Exception {
        boolean rezalt = false;
        String sql = "";

        try {
            sql = "INSERT INTO s_brand (nbrand, note) VALUES( ?, ?)";

            ps = conn.prepareStatement(sql);
            ps.setString(1, name.trim());
            ps.setString(2, note.trim().toLowerCase());
            ps.execute();

            rezalt = true;
        } catch (Exception e) {
            rezalt = false;
            System.err.println("Ошибка addIzdBrand() " + e);
            log.error("Ошибка addIzdBrand()", e);
            throw new Exception("Ошибка addIzdBrand(): " + e.getMessage(), e);
        }

        return rezalt;
    }

    boolean editBrand(int id, String name, String note) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    boolean addIzdIzm(String name, String note) throws Exception {
        boolean rezalt = false;
        String sql = "";

        try {
            sql = "INSERT INTO s_izm (nizm, note) VALUES( ?, ?)";

            ps = conn.prepareStatement(sql);
            ps.setString(1, name.trim());
            ps.setString(2, note.trim().toLowerCase());
            ps.execute();

            rezalt = true;
        } catch (Exception e) {
            rezalt = false;
            System.err.println("Ошибка addIzdIzm() " + e);
            log.error("Ошибка addIzdIzm()", e);
            throw new Exception("Ошибка addIzdIzm(): " + e.getMessage(), e);
        }

        return rezalt;
    }

    boolean addIzdTip(String name, String note) throws Exception {
        boolean rezalt = false;
        String sql = "";

        try {
            sql = "INSERT INTO s_tip (ntip, note) VALUES( ?, ?)";

            ps = conn.prepareStatement(sql);
            ps.setString(1, name.trim());
            ps.setString(2, note.trim().toLowerCase());
            ps.execute();

            rezalt = true;
        } catch (Exception e) {
            rezalt = false;
            System.err.println("Ошибка addIzdTip() " + e);
            log.error("Ошибка addIzdTip()", e);
            throw new Exception("Ошибка addIzdTip(): " + e.getMessage(), e);
        }

        return rezalt;
    }

    boolean addIzdVid(String name, String note) throws Exception {
        boolean rezalt = false;
        String sql = "";

        try {
            sql = "INSERT INTO s_vid (nvid, note) VALUES( ?, ?)";

            ps = conn.prepareStatement(sql);
            ps.setString(1, name.trim());
            ps.setString(2, note.trim().toLowerCase());
            ps.execute();

            rezalt = true;
        } catch (Exception e) {
            rezalt = false;
            System.err.println("Ошибка addIzdVid() " + e);
            log.error("Ошибка addIzdVid()", e);
            throw new Exception("Ошибка addIzdVid(): " + e.getMessage(), e);
        }

        return rezalt;
    }

    boolean addIzdGroup(String name, String note) throws Exception {
        boolean rezalt = false;
        String sql = "";

        try {
            sql = "INSERT INTO s_group (ngroup, note) VALUES( ?, ?)";

            ps = conn.prepareStatement(sql);
            ps.setString(1, name.trim());
            ps.setString(2, note.trim().toLowerCase());
            ps.execute();

            rezalt = true;
        } catch (Exception e) {
            rezalt = false;
            System.err.println("Ошибка addIzdGroup() " + e);
            log.error("Ошибка addIzdGroup()", e);
            throw new Exception("Ошибка addIzdGroup(): " + e.getMessage(), e);
        }

        return rezalt;
    }

    boolean addIzdAssort(String name, String note) throws Exception {
        boolean rezalt = false;
        String sql = "";

        try {
            sql = "INSERT INTO s_assort (nassort, note) VALUES( ?, ?)";

            ps = conn.prepareStatement(sql);
            ps.setString(1, name.trim());
            ps.setString(2, note.trim().toLowerCase());
            ps.execute();

            rezalt = true;
        } catch (Exception e) {
            rezalt = false;
            System.err.println("Ошибка addIzdAssort() " + e);
            log.error("Ошибка addIzdAssort()", e);
            throw new Exception("Ошибка addIzdAssort(): " + e.getMessage(), e);
        }

        return rezalt;
    }

    boolean addKod(String name, String note, int kod) throws Exception {
        boolean rezalt = false;
        String sql = "";

        try {
            sql = "INSERT INTO s_kod_item (id_kod, kod_item, note) VALUES( ?, ?, ?)";

            ps = conn.prepareStatement(sql);
            ps.setInt(1, kod);
            ps.setString(2, name.trim());
            ps.setString(3, note.trim().toLowerCase());
            ps.execute();

            rezalt = true;
        } catch (Exception e) {
            rezalt = false;
            System.err.println("Ошибка addKod() " + e);
            log.error("Ошибка addKod()", e);
            throw new Exception("Ошибка addKod(): " + e.getMessage(), e);
        }

        return rezalt;
    }

    boolean addEmpl(String name, String note, int kod) throws Exception {
        boolean rezalt = false;

        String sql = "";

        try {

            sql = " INSERT INTO employees (fio, num, id_dept, id_pro, note_empl) "
                    + "     VALUES(?, ?, ?, ?, ?)";

            ps = conn.prepareStatement(sql);
            ps.setString(1, name);
            ps.setInt(2, 0);
            ps.setInt(3, kod);
            ps.setInt(4, 1);
            ps.setString(5, note);
            ps.execute();

            rezalt = true;
        } catch (Exception e) {
            System.err.println("Ошибка addEmpl() " + e);
            log.error("Ошибка addEmpl()", e);
            throw new Exception("Ошибка addEmpl(): " + e.getMessage(), e);
        }

        return rezalt;
    }

    boolean addTehOp(String num, String name, Double val1, Double val2, String note, int tip) throws Exception {
        boolean rezalt = false;
        String sql = "";

        try {
            sql = "INSERT INTO s_tehop (num, tip_tehop, ntehop, val1, val2, note) "
                    + " VALUES( ?, ?, ?, ?, ?, ?)";

            ps = conn.prepareStatement(sql);
            ps.setString(1, num);
            ps.setInt(2, tip);
            ps.setString(3, name.trim());
            ps.setDouble(4, val1);
            ps.setDouble(5, val2);
            ps.setString(6, note.trim().toLowerCase());
            ps.execute();

            rezalt = true;
        } catch (Exception e) {
            rezalt = false;
            System.err.println("Ошибка addTehOp() " + e);
            log.error("Ошибка addTehOp()", e);
            throw new Exception("Ошибка addTehOp(): " + e.getMessage(), e);
        }

        return rezalt;
    }

    boolean editTehOp(int id, String num, String name, Double val1, Double val2, String note, int tip) throws Exception {
        boolean rezalt = false;
        String sql = "";

        try {
            sql = "Update s_tehop  "
                    + " Set num = ?, "
                    + "     tip_tehop =?, "
                    + "     ntehop = ?,"
                    + "     val1 = ?,"
                    + "     val2 = ?,"
                    + "     note = ?"
                    + " Where id = ? ";

            ps = conn.prepareStatement(sql);
            ps.setString(1, num);
            ps.setInt(2, tip);
            ps.setString(3, name.trim());
            ps.setDouble(4, val1);
            ps.setDouble(5, val2);
            ps.setString(6, note.trim().toLowerCase());
            ps.setInt(7, id);
            ps.execute();

            rezalt = true;
        } catch (Exception e) {
            rezalt = false;
            System.err.println("Ошибка addTehOp() " + e);
            log.error("Ошибка addTehOp()", e);
            throw new Exception("Ошибка addTehOp(): " + e.getMessage(), e);
        }

        return rezalt;
    }

}
