package dept.tech;

import workDB.DB;
import workDB.DBF;

import java.util.Vector;

/**
 *
 * @author vova
 */
public class EanList {

    static String path;
    DB db = new DB();
    DBF dbf = new DBF();
    Vector v = new Vector();

    public String getPath() {
        return path;
    }

    public void setPath(String str) {
        path = new String(str);
    }

    public String getMonth() {
        return path.substring(path.length() - 6, path.length() - 4);
    }

    public Vector createList() {
        try {
            v = dbf.readDBFPlan(path);
            v = db.getEanList(v);
        } catch (Exception e) {
            System.err.println(e);
        }
        return v;
    }
}
