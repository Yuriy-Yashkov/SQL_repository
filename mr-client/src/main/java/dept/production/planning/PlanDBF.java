package dept.production.planning;

import by.march8.ecs.framework.common.LogCrutch;
import com.svcon.jdbf.DBFReader;
import workDB.DBF_new;

import javax.swing.*;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

//import org.apache.log4j.Logger;

/**
 *
 * @author lidashka
 */
public class PlanDBF extends DBF_new {

    //private static final Logger log = new Log().getLoger(PlanDBF.class);
    private static final LogCrutch log = new LogCrutch();
    private PlanDB db;

    /**
     * Считывает план производства из dbf файла 
     * @param path - путь к dbf
     * @return - ArrayList(Object[]) (признак, шифр артикула, модель, рост, размер, кол-во в месяц, кол-во в день)
     * @throws Exception
     */
    public ArrayList readPlan(String path) throws Exception {
        ArrayList items = new ArrayList();
        DBFReader dbfr_copy = null;

        Object historySar = 0;
        Object hisoryFas = 0;
        try {
            dbfr = new DBFReader(path);
            dbfr_copy = new DBFReader(path);

            Object obj[] = new Object[dbfr.getFieldCount()];
            Object obj_copy[] = new Object[dbfr_copy.getFieldCount()];

            while (dbfr.hasNextRecord()) {
                obj = dbfr.nextRecord();
                Object[] item = new Object[8];

                if (Integer.parseInt(obj[4].toString().trim()) != 0) {
                    if (obj[1].toString().trim().equals("737") &&
                            obj[9].toString().trim().equals("1")) {

                        if (Integer.parseInt(obj[7].toString().trim()) != 0) {
                            item[2] = obj[7];
                        } else {
                            if (!obj[3].equals(historySar)) {
                                historySar = obj[3];
                                while (dbfr_copy.hasNextRecord()) {
                                    obj_copy = dbfr_copy.nextRecord();
                                    if (Integer.parseInt(obj_copy[7].toString().trim()) != 0 && obj_copy[3].equals(historySar)) {
                                        hisoryFas = obj_copy[7];
                                        break;
                                    }
                                }
                            }
                            item[2] = hisoryFas;
                        }
                        item[0] = 0;                                            // конвейер
                        item[1] = obj[3];
                        item[3] = (obj[5] != null && !obj[5].toString().trim().equals("".toString())) ? obj[5] : 0;
                        item[4] = obj[4];
                        item[5] = obj[12];
                        item[6] = obj[13];
                        item[7] = "";
                        items.add(item);
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Ошибка чтения дбф плана: " + e.getMessage());
            throw e;
        } finally {
            try {
                if (dbfr != null) dbfr.close();
            } catch (Exception e) {
                System.err.println("Ошибка закрытия дбф плана: " + e.getMessage());
            }
        }
        return items;
    }

    public ArrayList readVypusk(String path) throws Exception {
        ArrayList items = new ArrayList();
        try {
            dbfr = new DBFReader(path);

            Object obj[] = new Object[dbfr.getFieldCount()];

            while (dbfr.hasNextRecord()) {
                obj = dbfr.nextRecord();
                Object[] item = new Object[8];

                // 737 - склад; 739 - подрядчики; 
                // Если только obj[10] - данные + несорт.                

                if (Integer.parseInt(obj[17].toString().trim()) != 0) {
                    if (obj[10].toString().trim().equals("737") &&
                            obj[11].toString().trim().equals("737") &&
                            obj[29].toString().trim().equals("3")) {

                        item[0] = 0;                                            // конвейер
                        item[2] = obj[17];                                      // модель
                        item[1] = obj[15];                                      // шифр артикула
                        item[3] = (obj[21] != null && !obj[21].toString().trim().equals("".toString())) ? obj[21] : 0; // рост
                        item[4] = obj[20];                                      // размер                        
                        item[5] = obj[22];                                      // кол-во 
                        if ((obj[4].toString().trim()).equals("30"))
                            item[6] = obj[22];                                      // кол-во 
                        else if ((obj[4].toString().trim()).equals("38"))
                            item[6] = "-" + obj[22];                                      // кол-во
                        item[7] = "";                                           // название
                        items.add(item);
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Ошибка чтения дбф выпуска: " + e.getMessage());
            throw e;
        } finally {
            try {
                if (dbfr != null) dbfr.close();
            } catch (Exception e) {
                System.err.println("Ошибка закрытия дбф выпуска: " + e.getMessage());
            }
        }
        return items;
    }

    public ArrayList readVypuskTL(String path) throws Exception {
        ArrayList items = new ArrayList();
        try {
            dbfr = new DBFReader(path);

            Object obj[] = new Object[dbfr.getFieldCount()];

            while (dbfr.hasNextRecord()) {
                obj = dbfr.nextRecord();
                Object[] item = new Object[8];

                if (Integer.parseInt(obj[3].toString().trim()) != 0) {
                    item[0] = 0;                                            // конвейер
                    item[1] = obj[2];                                       // шифр артикула
                    item[2] = obj[3];                                       // модель
                    item[3] = 0;                                            // рост
                    item[4] = 0;                                            // размер                        
                    item[5] = obj[4];                                       // кол-во                        
                    item[6] = obj[4];                                       // кол-во 
                    item[7] = "";                                           // название    
                    items.add(item);
                }
            }
        } catch (Exception e) {
            System.err.println("Ошибка чтения дбф выпуска: " + e.getMessage());
            throw e;
        } finally {
            try {
                if (dbfr != null) dbfr.close();
            } catch (Exception e) {
                System.err.println("Ошибка закрытия дбф выпуска: " + e.getMessage());
            }
        }
        return items;
    }

    public Vector readDBFModels16(String path) throws Exception {
        Vector items = new Vector();
        Object[][] colname = {{"KOD", ""}, {"MODEL", ""}, {"POL", ""}, {"PR", ""}};

        com.linuxense.javadbf.DBFField field = null;
        try {
            InputStream inputStream = new FileInputStream(path);
            com.linuxense.javadbf.DBFReader reader = new com.linuxense.javadbf.DBFReader(inputStream);
            reader.setCharactersetName("cp866");

            for (int i = 0; i < colname.length; i++) {
                for (int j = 0; j < reader.getFieldCount(); j++) {
                    field = reader.getField(j);
                    if (field.getName().trim().toUpperCase().equals(colname[i][0]))
                        colname[i][1] = j;
                }
                if (colname[i][1].equals(""))
                    JOptionPane.showMessageDialog(null, "Ошибка: в dbf-файле не найден столбец " + colname[i][0], "Ошибка!!!", javax.swing.JOptionPane.ERROR_MESSAGE);
            }

            Object obj[] = new Object[reader.getFieldCount()];

            for (int i = 0; i < reader.getRecordCount(); i++) {
                obj = reader.nextRecord();
                Vector tmp = new Vector();
                for (int j = 0; j < colname.length; j++) {
                    switch (j) {
                        case 0:
                            tmp.add(Integer.parseInt(obj[Integer.parseInt(colname[j][1].toString())].toString().trim().replace(".0", "")));
                            break;
                        case 1:
                        case 2:
                            tmp.add(obj[Integer.parseInt(colname[j][1].toString())].toString().trim());
                            break;
                        case 3:
                            tmp.add(Integer.parseInt(obj[Integer.parseInt(colname[j][1].toString())].toString().trim().replace(".0", "")));

                            if (Integer.parseInt(obj[Integer.parseInt(colname[j][1].toString())].toString().trim().replace(".0", "")) == 0)
                                tmp.add("основ.");
                            else if (Integer.parseInt(obj[Integer.parseInt(colname[j][1].toString())].toString().trim().replace(".0", "")) == -1)
                                tmp.add("вспом.");

                            break;
                        default:
                            break;
                    }
                }
                items.add(tmp);
            }
            inputStream.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ошибка чтения дбф: " + e.getMessage(), "Ошибка!!!", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
        return items;
    }

    public Vector readDBFRasklad(String path, int model) throws Exception {
        Vector items = new Vector();
        Object[][] colname = {{"FASON", ""}, {"AI", ""}, {"SHAI", ""}, {"NAI", ""}, {"ROST", ""}, {"RAZM", ""},
                {"PRIZN", ""}, {"SHAP", ""}, {"AP", ""}, {"SHRASKL", ""}, {"RASHSYR", ""}};

        com.linuxense.javadbf.DBFField field = null;
        try {
            InputStream inputStream = new FileInputStream(path);
            com.linuxense.javadbf.DBFReader reader = new com.linuxense.javadbf.DBFReader(inputStream);
            reader.setCharactersetName("cp866");

            for (int i = 0; i < colname.length; i++) {
                for (int j = 0; j < reader.getFieldCount(); j++) {
                    field = reader.getField(j);
                    if (field.getName().trim().toUpperCase().equals(colname[i][0]))
                        colname[i][1] = j;
                }
                if (colname[i][1].equals(""))
                    JOptionPane.showMessageDialog(null, "Ошибка: в dbf-файле не найден столбец " + colname[i][0], "Ошибка!!!", javax.swing.JOptionPane.ERROR_MESSAGE);
            }

            Object obj[] = new Object[reader.getFieldCount()];

            for (int i = 0; i < reader.getRecordCount(); i++) {
                obj = reader.nextRecord();
                if (obj != null) {
                    if (Integer.parseInt(obj[Integer.parseInt(colname[0][1].toString())].toString().trim().replace(".0", "")) == model) {
                        Vector tmp = new Vector();
                        tmp.add(false);
                        for (int j = 0; j < colname.length; j++) {
                            tmp.add(obj[Integer.parseInt(colname[j][1].toString())].toString().trim());
                        }
                        if (!items.contains(tmp))
                            items.add(tmp);
                    }
                } else {
                    i = reader.getRecordCount() + 1;
                }
            }
            inputStream.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ошибка чтения дбф: " + e.getMessage(), "Ошибка!!!", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
        return items;
    }

    public Vector readDBFRasklad2(String path, int model) throws Exception {
        Vector items = new Vector();
        Vector history = new Vector();
        Vector t = new Vector();
        double rashod = 0;
        int k = 0;
        Object[][] colname = {{"FASON", ""}, {"AI", ""}, {"SHAI", ""}, {"NAI", ""},
                {"PRIZN", ""}, {"SHAP", ""}, {"AP", ""}, {"SHRASKL", ""}, {"RASHSYR", ""}};

        com.linuxense.javadbf.DBFField field = null;
        try {
            InputStream inputStream = new FileInputStream(path);
            com.linuxense.javadbf.DBFReader reader = new com.linuxense.javadbf.DBFReader(inputStream);
            reader.setCharactersetName("cp866");

            for (int i = 0; i < colname.length; i++) {
                for (int j = 0; j < reader.getFieldCount(); j++) {
                    field = reader.getField(j);
                    if (field.getName().trim().toUpperCase().equals(colname[i][0]))
                        colname[i][1] = j;
                }
                if (colname[i][1].equals(""))
                    JOptionPane.showMessageDialog(null, "Ошибка: в dbf-файле не найден столбец " + colname[i][0], "Ошибка!!!", javax.swing.JOptionPane.ERROR_MESSAGE);
            }

            Object obj[] = new Object[reader.getFieldCount()];

            for (int i = 0; i < reader.getRecordCount(); i++) {
                obj = reader.nextRecord();
                if (obj != null) {
                    if (Integer.parseInt(obj[Integer.parseInt(colname[0][1].toString())].toString().trim().replace(".0", "")) == model) {
                        Vector tmp = new Vector();
                        tmp.add(false);

                        for (int j = 0; j < colname.length - 1; j++) {
                            tmp.add(obj[Integer.parseInt(colname[j][1].toString())].toString().trim()); 
                            /*
                        switch(j){
                            case 0: 
                                tmp.add(Integer.parseInt(obj[Integer.parseInt(colname[j][1].toString())].toString().trim().replace(".0", "")));    
                                break;
                            case 1: case 2: 
                                tmp.add(obj[Integer.parseInt(colname[j][1].toString())].toString().trim());    
                                break;
                            case 3:
                                tmp.add(Integer.parseInt(obj[Integer.parseInt(colname[j][1].toString())].toString().trim().replace(".0", "")));   

                                if(Integer.parseInt(obj[Integer.parseInt(colname[j][1].toString())].toString().trim().replace(".0", "")) == 0)
                                    tmp.add("основ.");
                                else if(Integer.parseInt(obj[Integer.parseInt(colname[j][1].toString())].toString().trim().replace(".0", "")) == -1)
                                    tmp.add("вспом.");

                                break;
                            default: break;  
                            * }
                            */
                        }

                        if (!history.isEmpty()) {
                            if (!history.contains(tmp)) {
                                t.add(k > 0 ? rashod / k : rashod);
                                items.add(t);

                                k = 0;
                                rashod = 0;
                                t = tmp;

                                history.add(tmp);
                                rashod += Double.parseDouble(obj[Integer.parseInt(colname[colname.length - 1][1].toString())].toString().trim());
                                k++;
                            } else {
                                t = tmp;
                                rashod += Double.parseDouble(obj[Integer.parseInt(colname[colname.length - 1][1].toString())].toString().trim());
                                k++;
                            }
                        } else {
                            t = tmp;
                            rashod += Double.parseDouble(obj[Integer.parseInt(colname[colname.length - 1][1].toString())].toString().trim());
                            k++;
                            history.add(tmp);
                        }
                    }
                } else {
                    i = reader.getRecordCount() + 1;
                }
            }
            inputStream.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ошибка чтения дбф: " + e.getMessage(), "Ошибка!!!", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
        return items;
    }

    public Vector readDBFRasklad3(String path, int model) throws Exception {
        Vector items = new Vector();
        HashMap map = new HashMap<Vector, Vector>();
        Vector vmap = new Vector();
        int k = 0;
        double r = 0;
        Object[][] colname = {{"FASON", ""}, {"SHAI", ""}, {"AI", ""}, {"NAI", ""},
                {"PRIZN", ""}, {"SHAP", ""}, {"AP", ""}, /*{"SHRASKL",""}, {"NARASKL",""}, */{"RASHSYR", ""}};

        com.linuxense.javadbf.DBFField field = null;
        try {
            InputStream inputStream = new FileInputStream(path);
            com.linuxense.javadbf.DBFReader reader = new com.linuxense.javadbf.DBFReader(inputStream);
            reader.setCharactersetName("cp866");

            for (int i = 0; i < colname.length; i++) {
                for (int j = 0; j < reader.getFieldCount(); j++) {
                    field = reader.getField(j);
                    if (field.getName().trim().toUpperCase().equals(colname[i][0]))
                        colname[i][1] = j;
                }
                if (colname[i][1].equals(""))
                    JOptionPane.showMessageDialog(null, "Ошибка: в dbf-файле не найден столбец " + colname[i][0], "Ошибка!!!", javax.swing.JOptionPane.ERROR_MESSAGE);
            }

            Object obj[] = new Object[reader.getFieldCount()];

            for (int i = 0; i < reader.getRecordCount(); i++) {
                obj = reader.nextRecord();
                if (obj != null) {
                    if (Integer.parseInt(obj[Integer.parseInt(colname[0][1].toString())].toString().trim().replace(".0", "")) == model) {
                        Vector tmp = new Vector();

                        tmp.add(false);
                        for (int j = 0; j < colname.length - 1; j++) {
                            if (colname[j][0].toString().equals("PRIZN")) {
                                tmp.add(Integer.parseInt(obj[Integer.parseInt(colname[j][1].toString())].toString().trim().replace(".0", "")));
                            } else {
                                tmp.add(obj[Integer.parseInt(colname[j][1].toString())].toString().trim().replace(".0", ""));
                            }
                        }

                        if (map.containsKey(tmp)) {
                            k = Integer.valueOf(((Vector) map.get(tmp)).elementAt(0).toString());
                            r = Double.valueOf(((Vector) map.get(tmp)).elementAt(1).toString());

                            vmap = new Vector();
                            vmap.add(k + 1);
                            vmap.add(r + Double.parseDouble(obj[Integer.parseInt(colname[colname.length - 1][1].toString())].toString().trim()));
                            map.put(tmp, vmap);
                        } else {
                            vmap = new Vector();
                            vmap.add(1);
                            vmap.add(Double.parseDouble(obj[Integer.parseInt(colname[colname.length - 1][1].toString())].toString().trim()));
                            map.put(tmp, vmap);
                        }
                    }
                } else {
                    i = reader.getRecordCount() + 1;
                }
            }

            Set<Map.Entry<Vector, Vector>> set = map.entrySet();
            for (Map.Entry<Vector, Vector> me : set) {
                Vector temp1 = me.getKey();
                Vector temp2 = me.getValue();

                temp1.add(Double.valueOf(temp2.elementAt(1).toString()) / Integer.valueOf(temp2.elementAt(0).toString()));
                items.add(temp1);
            }
            inputStream.close();
        } catch (Exception e) {
            items = new Vector();
            JOptionPane.showMessageDialog(null, "Ошибка чтения дбф: " + e.getMessage(), "Ошибка!!!", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
        return items;
    }

    public Vector readDBFRaskladDetal(String path, Vector data) throws Exception {
        Vector items = new Vector();
        HashMap map = new HashMap<Vector, Vector>();
        Vector vmap = new Vector();
        int k = 0;
        double r = 0;
        boolean flag = false;
        Object[][] colname = {{"FASON", ""}, {"SHAI", ""}, {"AI", ""}, {"NAI", ""},
                {"PRIZN", ""}, {"SHAP", ""}, {"AP", ""}/*, {"SHRASKL",""}, {"NARASKL",""}*/};
        Object[][] colname_ = {{"ROST", ""}, {"RAZM", ""}, {"RASHSYR", ""}};

        com.linuxense.javadbf.DBFField field = null;
        try {
            InputStream inputStream = new FileInputStream(path);
            com.linuxense.javadbf.DBFReader reader = new com.linuxense.javadbf.DBFReader(inputStream);
            reader.setCharactersetName("cp866");

            for (int i = 0; i < colname.length; i++) {
                for (int j = 0; j < reader.getFieldCount(); j++) {
                    field = reader.getField(j);
                    if (field.getName().trim().toUpperCase().equals(colname[i][0]))
                        colname[i][1] = j;
                }
                if (colname[i][1].equals(""))
                    JOptionPane.showMessageDialog(null, "Ошибка: в dbf-файле не найден столбец " + colname[i][0], "Ошибка!!!", javax.swing.JOptionPane.ERROR_MESSAGE);
            }

            for (int i = 0; i < colname_.length; i++) {
                for (int j = 0; j < reader.getFieldCount(); j++) {
                    field = reader.getField(j);
                    if (field.getName().trim().toUpperCase().equals(colname_[i][0]))
                        colname_[i][1] = j;
                }
                if (colname_[i][1].equals(""))
                    JOptionPane.showMessageDialog(null, "Ошибка: в dbf-файле не найден столбец " + colname_[i][0], "Ошибка!!!", javax.swing.JOptionPane.ERROR_MESSAGE);
            }

            Object obj[] = new Object[reader.getFieldCount()];

            for (int i = 0; i < reader.getRecordCount(); i++) {
                obj = reader.nextRecord();
                if (obj != null) {
                    flag = true;

                    for (int j = 0; j < colname.length; j++) {
                        if (!obj[Integer.parseInt(colname[j][1].toString())].toString().trim().replace(".0", "").equals(data.get(j + 1).toString())) {
                            flag = false;
                            break;
                        }
                    }

                    if (flag) {
                        Vector tmp = new Vector();

                        tmp.add(false);
                        for (int j = 0; j < colname.length; j++) {
                            tmp.add(obj[Integer.parseInt(colname[j][1].toString())].toString().trim().replace(".0", ""));
                        }
                        for (int j = 0; j < colname_.length; j++) {
                            tmp.add(obj[Integer.parseInt(colname_[j][1].toString())].toString().trim().replace(".0", ""));
                        }
                        items.add(tmp);
                    }
                } else {
                    i = reader.getRecordCount() + 1;
                }
            }

            inputStream.close();
        } catch (Exception e) {
            items = new Vector();
            JOptionPane.showMessageDialog(null, "Ошибка чтения дбф: " + e.getMessage(), "Ошибка!!!", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
        return items;
    }
}
