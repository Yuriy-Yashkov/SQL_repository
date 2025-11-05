package dept.nsi;

import by.march8.ecs.framework.common.LogCrutch;
import workDB.DBF;
import workDB.PDB;

import javax.swing.*;
import java.util.ArrayList;

/**
 *
 * @author vova
 */
public class PlanSstoimost {
    //private static final Logger log = new Log().getLoger(PlanSstoimost.class);
    private static final LogCrutch log = new LogCrutch();
    PDB pdb = null;
    DBF dbf = null;
    ArrayList rows = new ArrayList();

    public PlanSstoimost(String path) {
        dbf = new DBF();
        rows = dbf.readDBFSstoimost(path);
    }

    public void update() {
        try {
            pdb = new PDB();
            pdb.insertPlanSstoimst(rows);
        } catch (Exception e) {
            log.error("Ошибка при добавлении трудозатрат ", e);
            System.out.println("Ошибка при обновлении справочника трудозатрат" + e);
            JOptionPane.showMessageDialog(null, "Ошибка при обновлении справочника трудозатрат: " + e.getMessage(), "Ошибка!!!", javax.swing.JOptionPane.ERROR_MESSAGE);
            return;
        } finally {
            pdb.disConn();
        }
        JOptionPane.showMessageDialog(null, "Справочник успешно обновлён", "Обновлено", javax.swing.JOptionPane.INFORMATION_MESSAGE);
    }
}
