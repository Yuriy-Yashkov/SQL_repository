package dept.sklad.ho;

import com.jhlabs.awt.ParagraphLayout;
import common.UtilFunctions;

import javax.swing.*;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Vector;

/**
 *
 * @author lidashka
 */
public class UtilSkladHO {

    public static String SKLADHO_CLAD = "О.В.Романюк";
    public static String SKLADHO_BUH = "Е.И.Кучкина";
    public static String SKLADHO_HACH = "Н.В.Павлова   ";
    //public static String SKLADHO_HACH = "М.И.Громыко   ";

    public static Vector DEPT_MODEL = new Vector();
    public static int DEPT_SELECT_ITEM = -1;
    public static String SETTING_DEPT_SELECT_ITEM = "UtilSkladHO.deptSettingSelectItem";

    public static Vector TIP_MOVE_MODEL = new Vector();
    public static int TIP_MOVE_SELECT_ITEM = -1;
    public static String SETTING_TIP_MOVE_SELECT_ITEM = "UtilSkladHO.tipMoveSettingSelectItem";

    public static String DATE_VVOD = "";
    public static String SETTING_DATE_VVOD = "UtilSkladHO.dateSettingVvod";

    public static double ACTUAL_MOISTURE = 1.03;
    public static String SETTING_MOISTURE = "UtilSkladHO.moisture";

    public static String SETTING_MAP_MOVE_EDIT = "UtilSkladHO.mapSettingMoveEdit";
    public static String SETTING_MAP_PRODUCT_EDIT = "UtilSkladHO.mapSettingProductEdit";
    public static String SETTING_MAP_PRODUCT_TMC = "UtilSkladHO.mapSettingProductTmc";
    public static String SETTING_MAP_PRODUCT_MOVE = "UtilSkladHO.mapSettingProductMove";
    public static String SETTING_MAP_MATERIAL_EDIT = "UtilSkladHO.mapSettingMaterialEdit";
    public static String SETTING_MAP_IZD_EDIT = "UtilSkladHO.mapSettingIzdEdit";
    public static String SETTING_MAP_OST_KL_EDIT = "UtilSkladHO.mapSettingOstKlEdit";
    public static String SETTING_MAP_OST_DR_EDIT = "UtilSkladHO.mapSettingOstDrEdit";
    public static String SETTING_MAP_OST_MODEL_EDIT = "UtilSkladHO.mapSettingOstModelEdit";

    public static String OST_VID_1 = "кладовая";
    public static String OST_VID_2 = "др.подр.";

    public static String SKLAD_VID_1 = "полотно";
    public static String SKLAD_VID_2 = "всп.материалы";

    public static String SKLAD_MADE_1 = "фабрика";
    public static String SKLAD_MADE_2 = "импорт";

    public static String TYPE_MOVE_PR = "приход";
    public static String TYPE_MOVE_RS = "расход";
    public static String TYPE_MOVE_VZ = "возврат";
    public static String TYPE_MOVE_AK = "по акту";
    public static String TYPE_MOVE_BR = "брак";

    public static String KOLVO_MASS = "Масса";
    public static String KOLVO_KOL = "Кол-во";

    public static String TYPE_ADD = "add";
    public static String TYPE_EDIT = "edit";
    public static String TYPE_OPEN = "open";
    public static String TYPE_ADD_RETURN = "add_return";
    public static String TYPE_ADD_BIG_RETURN = "add_big_return";
    public static String TYPE_EDIT_RETURN = "edit_return";
    public static String PRICE_ACTUAL = "max";

    public static String REPORT_POLOTNO_F = "r_polotno_f";
    public static String REPORT_POLOTNO_I = "r_polotno_i";
    public static String REPORT_POLOTNO_I_PO = "r_polotno_i_po";
    public static String REPORT_VMATERIAL = "r_vmaterial";
    public static String REPORT_KRO = "r_kro";
    public static String REPORT_NAK = "r_nak";
    public static String REPORT_INVENTORY = "r_inventory";

    public static String DATA_MOVE = "move";
    public static String DATA_PRODUCT = "product";
    public static String DATA_MATERIAL = "material";
    public static String DATA_IZD = "izd";
    public static String DATA_RETURN = "return";

    public static int SPRAV_GROUPTMC_ID = -1;
    public static String SPRAV_GROUPTMC_NAME = "";

    public static String SEARCH_PRODUCT_SDATE = "";
    public static String SEARCH_PRODUCT_EDATE = "";
    public static String SEARCH_PRODUCT_MODEL = "";

    public static Vector PRODUCT_DATA_VECTOR = new Vector();

    public static int SPRAV_TMC_ID = -1;
    public static String SPRAV_TMC_GROUP = "";
    public static String SPRAV_TMC_NAME = "";
    public static int SPRAV_TMC_SAR = 0;
    public static String SPRAV_TMC_NAR = "";
    public static String SPRAV_TMC_VID = "";
    public static String SPRAV_TMC_MADE = "";
    public static String SPRAV_TMC_EDIZM = "";

    public static boolean BUTT_ACTION_EDIT = false;
    public static boolean BUTT_ACTION_SELECT_SPRAV = false;
    public static boolean BUTT_ACTION_SELECT_GROUP = false;
    public static boolean BUTT_ACTION_SELECT_PRODUCT = false;

    public static String OTCHET_NAK = "SkladHONak.ots";
    public static String OTCHET_MAP = "SkladHOMap.ots";
    public static String OTCHET_MOVE_NP = "SkladHOMoveNP.ots";
    public static String OTCHET_MOVE_IP_G = "SkladHOMoveIPGroup.ots";
    public static String OTCHET_MOVE_IP_P = "SkladHOMoveP.ots";
    public static String OTCHET_MOVE_PO_IP = "SkladHOPOIP.ots";
    public static String OTCHET_MOVE_VM_G = "SkladHOMoveVMGroup.ots";
    public static String OTCHET_MOVE_VM_P = "SkladHOMoveVM.ots";
    public static String OTCHET_INVENTORY = "SkladHOInventory.ots";

    public static int numRowTableAll(JTable table) throws Exception {
        int rez = 0;
        try {
            if (table.getSelectedRow() != -1) {
                int num = (table.getRowCount() == 1) ? Integer.valueOf(table.getValueAt(0, 1).toString()) : 0;
                for (int i = 1; i < table.getRowCount(); i++) {
                    num = Integer.valueOf(table.getValueAt(i - 1, 1).toString()) + 1;
                    table.setValueAt(num, i, 1);
                }
                rez = num + 1;
            } else rez = 0;
        } catch (Exception e) {
            rez = 0;
            throw new Exception("Ошибка numRowTableAll() " + e.getMessage(), e);
        }
        return rez;
    }

    public static Vector getItemTable(JTable table) throws Exception {
        Vector temp = new Vector();

        try {
            for (int i = 0; i < table.getModel().getColumnCount(); i++) {
                temp.add(table.getValueAt(table.getSelectedRow(), i));
            }

        } catch (Exception e) {
            temp = new Vector();
            throw new Exception("Ошибка getItemTable() " + e.getMessage(), e);
        }

        return temp;
    }

    /**
     * Расчитывает кондиционное кол-во по фактическому
     * @param kolvoF - фактическое кол-во
     * @return - кондиционное кол-во
     * @throws Exception
     */
    public static double countKolvoK(double kolvoF) throws Exception {
        double kolvoK = 0;

        try {
            kolvoK = Double.valueOf(UtilFunctions.formatNorm(kolvoF * ACTUAL_MOISTURE, 3));

        } catch (Exception e) {
            kolvoK = 0;
            throw new Exception("Ошибка countKolvoK() " + e.getMessage(), e);
        }

        return kolvoK;
    }

    /**
     * Расчитывает фактическое кол-во по кондиционному
     * @param kolvoK - кондиционное кол-во
     * @return - фактическое кол-во
     * @throws Exception
     */
    public static double countKolvoF(double kolvoK) throws Exception {
        double kolvoF = 0;

        try {
            if (ACTUAL_MOISTURE != 0) {
                kolvoF = Double.valueOf(UtilFunctions.formatNorm(kolvoK / ACTUAL_MOISTURE, 3));
            }

        } catch (Exception e) {
            kolvoF = 0;
            throw new Exception("Ошибка countKolvoF() " + e.getMessage(), e);
        }

        return kolvoF;
    }

    public static void editMoistureMaterial(double history) throws Exception {
        try {
            JPanel panel = new JPanel();
            final JTextField textMoisture = new JTextField(String.valueOf(ACTUAL_MOISTURE));

            panel.setLayout(new ParagraphLayout());

            textMoisture.setPreferredSize(new Dimension(100, 20));

            textMoisture.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

            panel.add(new JLabel("Коэффициент:"), ParagraphLayout.NEW_PARAGRAPH);
            panel.add(textMoisture);

            if (JOptionPane.showOptionDialog(null, panel, "Влажность материалов", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, new Object[]{"Сохранить", "Отмена"}, "Сохранить") == JOptionPane.YES_OPTION) {

                ACTUAL_MOISTURE = Double.valueOf(textMoisture.getText().trim().replace(",", "."));

                try {
                    UtilFunctions.setSettingPropFile(String.valueOf(ACTUAL_MOISTURE), SETTING_MOISTURE);
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "Ошибка! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
                }
            }

        } catch (Exception e) {
            ACTUAL_MOISTURE = history;

            throw new Exception("Ошибка editMoistureMaterial() " + e.getMessage(), e);
        }
    }

    public static void initColumTableMap(JTable table, final HashMap<String, String> map) throws Exception {
        try {
            if (table != null & map != null) {
                String name;
                TableColumn column;

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
        } catch (Exception e) {
            throw new Exception("Ошибка initColumTableMap() " + e.getMessage(), e);
        }
    }

    /**
     * Возвращает кондиционное количество, если оно для данного вида ТМЦ и ед. изм. существует.
     * Если нет, то возвращает фактическое, которое хранится в БД.
     * @param vid - вид ТМЦ (1 - полотно; 2 - всп. материалы)
     * @param made - ТМЦ производства (1 - фабрика; 2 - импорт)
     * @param edIzm - ед. изм.
     * @param kolvo - кол-во
     * @return
     * @throws Exception
     */
    public static double getKolvo(int vid, int made, String edIzm, double kolvo) throws Exception {
        double rezalt;
        try {
            if (vid == 2
                    || (vid == 1 && made == 2)
                    || edIzm.toLowerCase().trim().contains("м")
                    || edIzm.toLowerCase().trim().contains("m"))
                rezalt = new BigDecimal(kolvo).setScale(3, RoundingMode.HALF_UP).doubleValue();
            else
                rezalt = countKolvoK(kolvo);
        } catch (Exception e) {
            rezalt = 0;
            throw new Exception("Ошибка getKolvo() " + e.getMessage(), e);
        }
        return rezalt;
    }
}
