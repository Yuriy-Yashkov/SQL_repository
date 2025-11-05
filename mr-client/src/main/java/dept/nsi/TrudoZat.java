package dept.nsi;

import by.march8.ecs.framework.common.LogCrutch;
import dept.MyReportsModule;
import workDB.DBF;
import workDB.PDB;

import javax.swing.*;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Properties;


/**
 *
 * @author vova
 */
public class TrudoZat {

    //private static final Logger log = new Log().getLoger(TrudoZat.class);
    private static final LogCrutch log = new LogCrutch();
    PDB pdb = null;
    DBF dbf = null;

    public void update() {
        ArrayList items = new ArrayList();
        Iterator it = null;
        String path = null;
        Properties prop = new Properties();

        try {
            File configfile = new File(MyReportsModule.confPath + "Conf.properties");
            prop.load(new FileInputStream(configfile));
            path = new String(prop.getProperty("dbf.trudozat"));
        } catch (Exception e) {
            System.err.println("Ошибка при чтении настроек из файла с конфигурациями Conf.properties: " + e);
            log.error("Ошибка при чтении настроек из файла с конфигурациями Conf.properties", e);
            JOptionPane.showMessageDialog(null, "Ошибка при чтении настроек из файла с конфигурациями Conf.properties", "Ошибка!!!", JOptionPane.ERROR_MESSAGE);
        }

        try {
            pdb = new PDB();
            dbf = new DBF(path, null);
            items = dbf.readTrudoZat();
            it = items.iterator();
            if (it.hasNext()) {
                pdb.clearTrudoZat();
            }
            while (it.hasNext()) {
                pdb.insertTrudoZat((Integer) it.next(), (Integer) it.next(), (Float) it.next());
            }
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
