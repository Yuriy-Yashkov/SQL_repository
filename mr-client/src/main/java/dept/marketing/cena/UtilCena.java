package dept.marketing.cena;

import javax.swing.*;
import javax.swing.table.TableColumn;
import java.util.HashMap;

/**
 *
 * @author lidashka
 */
public class UtilCena {
    public static String KURS_RUB = String.valueOf(new Double(1));
    public static String KURS_USD = String.valueOf(new Double(1));
    public static String KURS_EUR = String.valueOf(new Double(1));

    public static String TABLE_ART = "TableArt";
    public static String TABLE_ANALYSIS = "TableAnalysis";
    public static String TABLE_CHANGE = "TableChange";

    public static String SETTING_ART = "CenaForm.mapSetting";
    public static String SETTING_ANALYSIS = "CenaForm.mapSettingAnalysis";
    public static String SETTING_CHANGE = "CenaForm.mapSettingChange";

    public static String TEK_DATE = "";

    public static String CHANGE_UPDATE = "update";
    public static String CHANGE_MODIFI = "modify";

    public static String DOCUMENT_LIST1 = "list1";
    public static String DOCUMENT_LIST2 = "list2";
    public static String DOCUMENT_LIST3 = "list3";
    public static String DOCUMENT_LIST4 = "list4";
    public static String DOCUMENT_LIST5 = "list5";

    public static void setLoadDataPropFile(HashMap<String, String> map, String[][] arr) {
        try {
            for (int i = 0; i < arr.length; i++)
                map.put(arr[i][0], arr[i][1]);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Ошибка!" + ex.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void setKURS_RUB(String KURS_RUB) {
        try {
            if (Double.parseDouble(KURS_RUB.trim().replace(",", ".")) > 0)
                UtilCena.KURS_RUB = String.valueOf(Double.parseDouble(KURS_RUB.trim().replace(",", ".")));
            else
                throw new Exception();
        } catch (Exception e) {
            UtilCena.KURS_RUB = String.valueOf(new Double(1));
        }
    }

    public static void setKURS_USD(String KURS_USD) {
        try {
            if (Double.parseDouble(KURS_USD.trim().replace(",", ".")) > 0)
                UtilCena.KURS_USD = String.valueOf(Double.parseDouble(KURS_USD.trim().replace(",", ".")));
            else
                throw new Exception();
        } catch (Exception e) {
            UtilCena.KURS_USD = String.valueOf(new Double(1));
        }
    }

    public static void setKURS_EUR(String KURS_EUR) {
        try {
            if (Double.parseDouble(KURS_EUR.trim().replace(",", ".")) > 0)
                UtilCena.KURS_EUR = String.valueOf(Double.parseDouble(KURS_EUR.trim().replace(",", ".")));
            else
                throw new Exception();
        } catch (Exception e) {
            UtilCena.KURS_EUR = String.valueOf(new Double(1));
        }
    }

    public static void initColumTableMap(JTable table, final HashMap<String, String> map) {
        String name = "";
        TableColumn column = null;
        for (int i = 0; i < table.getColumnCount(); i++) {
            column = table.getColumnModel().getColumn(i);
            name = table.getColumnName(i);

            if (Boolean.valueOf(map.get(name))) {
                if (name.equals("")) column.setPreferredWidth(5);
                else column.setPreferredWidth(35);
            } else {
                column.setMinWidth(0);
                column.setMaxWidth(0);
            }
        }
    }
}