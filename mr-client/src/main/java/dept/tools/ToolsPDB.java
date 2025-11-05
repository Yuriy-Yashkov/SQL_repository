package dept.tools;

import by.march8.ecs.framework.common.LogCrutch;
import workDB.PDB_new;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author vova
 * @date 13.08.2012
 */
public class ToolsPDB extends PDB_new {
    // private static final Logger log = new Log().getLoger(ToolsPDB.class);
    private static final LogCrutch log = new LogCrutch();

    /**
     * Возвращает формы приложения для которых заданы права меню
     * @return
     * @throws Exception
     */
    public Object[][] getForms() {
        ArrayList<Object[]> arows = new ArrayList();
        String query = "select id, name_form, about from forms";

        try {
            ps = conn.prepareStatement(query);
            rs = ps.executeQuery();
            while (rs.next()) {
                arows.add(new Object[]{rs.getInt("id"), rs.getString("name_form").trim(), rs.getString("about").trim()});
            }
        } catch (SQLException e) {
            System.err.println("Ошибка ToolsPDB.getForms() " + e);
        }
        Object[][] r = new Object[arows.size()][3];
        int i = 0;
        for (Object[] o : arows) {
            r[i] = o;
            i++;
        }
        return r;
    }

    public String getRights(String login, int idForm) {
        String r = new String();
        String query = "select rights from menu_right where id_form = ? and id_user = (select id from users where login = ?)";

        try {
            ps = conn.prepareStatement(query);
            ps.setInt(1, idForm);
            ps.setString(2, login);
            rs = ps.executeQuery();
            if (rs.next()) {
                r = rs.getString("rights");
            }
        } catch (SQLException e) {
            System.err.println("Ошибка ToolsPDB.getRights(String login, int idForm) " + e);
        }
        return r;
    }

    public void setRights(String login, int idForm, String rights) {
        String query = "select id from users where login = ?";
        int id_user = 0;

        try {
            ps = conn.prepareStatement(query);
            ps.setString(1, login);
            rs = ps.executeQuery();
            if (rs.next()) {
                id_user = rs.getInt("id");
            }
        } catch (SQLException e) {
            System.err.println("Ошибка ToolsPDB.setRights(String login, int idForm, String rights) " + e);
        }

        query = "update menu_right set rights = ? where id_user = ? and id_form = ?";
        int row = 0;
        try {
            ps = conn.prepareStatement(query);
            ps.setString(1, rights);
            ps.setInt(2, id_user);
            ps.setInt(3, idForm);
            row = ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Ошибка ToolsPDB.setRights(String login, int idForm, String rights) " + e);
        }
        if (row == 0) {

            query = "insert into menu_right (rights, id_user, id_form) values(?, ?, ?)";

            try {
                ps = conn.prepareStatement(query);
                ps.setString(1, rights);
                ps.setInt(2, id_user);
                ps.setInt(3, idForm);
                row = ps.executeUpdate();
            } catch (SQLException e) {
                System.err.println("Ошибка ToolsPDB.setRights(String login, int idForm, String rights) " + e);
            }
        }
    }

    public String getClassName(int idForm) {
        String r = new String();
        String query = "select name_form from forms where id = ?";

        try {
            ps = conn.prepareStatement(query);
            ps.setInt(1, idForm);
            rs = ps.executeQuery();
            if (rs.next()) {
                r = rs.getString("name_form");
            }
        } catch (SQLException e) {
            System.err.println("Ошибка ToolsPDB.getClassName(int idForm) " + e);
        }
        return r;
    }
}
