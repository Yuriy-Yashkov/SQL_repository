/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dept.nsi;

import by.march8.ecs.framework.common.LogCrutch;
import workDB.DB;
import workDB.DBF;

import javax.swing.*;
import java.util.ArrayList;


/**
 *
 * @author vova
 */
public class ClientKT {
    //private static final Logger log = new Log().getLoger(ClientKT.class);
    private static final LogCrutch log = new LogCrutch();
    DB db = null;
    DBF dbf = null;
    ArrayList rows = new ArrayList();

    public ClientKT(String path) {
        dbf = new DBF();
        rows = dbf.readDBFClient(path);
    }

    public void update() {
        try {
            db = new DB();
            db.updateKTClient(rows);
        } catch (Exception e) {
            log.error("Ошибка при обновлении кодов территорий клиентов ", e);
            System.out.println("Ошибка при обновлении кодов территорий клиентов" + e);
            JOptionPane.showMessageDialog(null, "Ошибка при обновлении кодов территорий клиентов: " + e.getMessage(), "Ошибка!!!", javax.swing.JOptionPane.ERROR_MESSAGE);
            return;
        }
        JOptionPane.showMessageDialog(null, "Справочник успешно обновлён", "Обновлено", javax.swing.JOptionPane.INFORMATION_MESSAGE);
    }
}
