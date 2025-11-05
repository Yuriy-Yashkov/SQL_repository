package dept.production.planning;

import javax.swing.*;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Vector;

/**
 *
 * @author lidashka
 */
public class UtilPlan {
    public static boolean ACTION_BUTT_PLAN_SELECT = false;
    public static int PLAN_SELECT_NUM = -1;
    public static String PLAN_SELECT_NAME = "";

    public static Vector DEPT_MODEL = new Vector();
    public static int DEPT_SELECT_ITEM = -1;

    public static String SETTING_DEPT_SELECT_ITEM = "UtilPlan.deptSettingSelectItem";
    public static String SETTING_MAP_EDIT = "UtilPlan.mapSettingEdit";

    public static int ROUNDING_NORM = 4;
    public static String SETTING_ROUNDING_NORM = "UtilPlan.roundingSettingNorm";

    public static int WORK_ARTICLE = 0;
    public static String SETTING_WORK_ARTICLE = "UtilPlan.workArticleSetting";

    public static String DBF_MODELS16 = "";
    public static String SETTING_DBF_MODELS16 = "UtilPlan.fileDbfModels16";
    public static String DBF_RASHOD_DETAL = "";
    public static String SETTING_DBF_RASKLAD_DETAL = "UtilPlan.fileDbfRaskladDetal";
    public static String DBF_RASHOD = "";
    public static String SETTING_DBF_RASKLAD = "UtilPlan.fileDbfRasklad";

    public static String TEXT_SEARCH = "";

    public static int[] COL_PLAN = {5, 5, 7, 7};
    public static int[] COL_DETAL = {6, 7, 11, 11};
    public static int[] COL_CONV = {6, 6, 8, 8};
    public static int[] COL_DEKAD = {5, 7, 9, 11};
    public static int[] COL_TECH = {3, 3, 3, 3};
    public static int[] COL_MATERIAL = {3, 3, 3, 3};
    public static int[] COL_TECH_SMALL = {5, 5, 6, 6};
    public static int[] COL_STOK = {5, 5, 5, 5};
    public static int[] COL_VNORM = {5, 5, 11, 11};
    public static int[] COL_PJDEKAD = {5, 8, 10, 12};
    public static int[] COL_PJDETAL1 = {5, 5, 5, 5};
    public static int[] COL_PJDETAL2 = {4, 6, 8, 10};
    public static int[] COL_ANALYSIS = {4, 5, 6, 6};
    public static int[] COL_ANALYSIS_PROJ = {9, 9, 9, 9};
    public static int[] COL_ANALYSIS_PLAN = {7, 7, 7, 7};
    public static int[] COL_ANALYSIS_VYPUSK = {7, 7, 7, 7};

    public static String PLAN_COPY = "copy_plan";
    public static String PLAN_EDIT = "edit_plan";
    public static String PLAN_OPEN = "open_plan";

    public static String PROJECT_COPY = "copy_project";
    public static String PROJECT_EDIT = "edit_project";
    public static String PROJECT_OPEN = "open_project";
    public static String PROJECT_ADD = "add_project";

    public static String DATA_KR = "plan";
    public static String DATA_STOK = "stok";
    public static String DATA_DETAL = "detal";
    public static String DATA_CONV = "conv";
    public static String DATA_VNORM = "vnorm";
    public static String DATA_DEKAD = "dekad";

    public static double EDIT_PLAN_KOL = 0;
    public static int EDIT_PLAN_SPEC_ID = 1;
    public static String EDIT_PLAN_SPEC_NAME = "Спецификация отсутствует";
    public static double EDIT_PLAN_SPEC_NORM = 0;
    public static int EDIT_PLAN_CONV = 0;
    public static double EDIT_PLAN_DEKAD1 = 0;
    public static double EDIT_PLAN_DEKAD2 = 0;
    public static double EDIT_PLAN_DEKAD3 = 0;
    public static String EDIT_PLAN_NOTE = "";

    public static boolean EDIT_PROJECT_ARTICL_BUTT_ACTION = false;
    public static Vector EDIT_PROJECT_ARTICL_ITEM = new Vector();

    public static boolean EDIT_BUTT_ACTION = false;
    public static boolean EDIT_PROJECT_BUTT_ACTION = false;
    public static boolean EDIT_NEW_MODELS_BUTT_ACTION = false;
    public static int EDIT_ADD_ITEM_PLAN_SPEC_ID = 1;
    public static String EDIT_ADD_ITEM_PLAN_SPEC_NAME = "Спецификация отсутствует";
    public static double EDIT_ADD_ITEM_PLAN_SPEC_NORM = 0;
    public static int EDIT_ADD_ITEM_PLAN_SAR = 0;
    public static int EDIT_ADD_ITEM_PLAN_FAS = 0;
    public static int EDIT_ADD_ITEM_PLAN_RST = 0;
    public static int EDIT_ADD_ITEM_PLAN_RZM = 0;
    public static double EDIT_ADD_ITEM_PLAN_KOL = 0;
    public static int EDIT_ADD_ITEM_PLAN_CONV = 0;
    public static double EDIT_ADD_ITEM_PLAN_DEKAD1 = 0;
    public static double EDIT_ADD_ITEM_PLAN_DEKAD2 = 0;
    public static double EDIT_ADD_ITEM_PLAN_DEKAD3 = 0;
    public static String EDIT_ADD_ITEM_PLAN_NOTE = "";

    public static boolean EDIT_ADD_PROJ_BUTT_ACTION = false;
    public static String EDIT_ADD_PROJ_FAS_NUM = "";
    public static String EDIT_ADD_PROJ_FAS_NAME = "";
    public static Vector EDIT_ADD_PROJ_FAS_RST_RZM = new Vector();
    public static Vector EDIT_ADD_PROJ_FAS_COLOR = new Vector();
    public static Vector EDIT_ADD_PROJ_FAS_SOSTAV = new Vector();

    public static String EDIT_PROJ_TYPE_FAS = "fas";
    public static String EDIT_PROJ_TYPE_COLOR = "color";
    public static String EDIT_PROJ_TYPE_SOSTAV = "sostav";
    public static String EDIT_PROJ_TYPE_MODEL16 = "model16";
    public static String EDIT_PROJ_TYPE_RASHOD = "rasklad";

    public static String NEW = "НОВИНКА";
    public static String PLAN = "план";
    public static String PROJECT = "проект";
    public static String VYPUSK = "выпуск";
    public static String VYPUSK_TL = "выпуск_ТЛ";

    public static int YEARS = -1;

    public static int PLAN_SELECT_NUM_SIZES = -1;
    public static String PLAN_SELECT_NAME_SIZES = "";

    public static double[] countSumm(JTable table, int[] col) {
        double[] summ = new double[4];
        try {
            for (int i = 0; i < table.getRowCount(); i++) {
                summ[0] += Double.valueOf(table.getValueAt(i, col[0]).toString());
                summ[1] += Double.valueOf(table.getValueAt(i, col[1]).toString());
                summ[2] += Double.valueOf(table.getValueAt(i, col[2]).toString());
                summ[3] += Double.valueOf(table.getValueAt(i, col[3]).toString());
            }
        } catch (Exception e) {
            summ = new double[4];
            JOptionPane.showMessageDialog(null, e.getMessage(), "Ошибка ", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
        return summ;
    }

    public static void addRowSorterListener(JTable table, JPanel filterPanel, int[] col) {
        UtilPlan.setFooterTable(table, filterPanel, col, UtilPlan.countSumm(table, col));
    }

    public static void setFooterTable(JTable table, JPanel filterPanel, int[] col, double[] summ) {
        try {
            filterPanel.removeAll();
            for (int i = 0; i < table.getColumnModel().getColumnCount(); i++) {
                if (i == col[0]) {
                    filterPanel.add(new JTextField(String.valueOf(new DecimalFormat("###,###.##").format(summ[0]))));
                } else if (i == col[1]) {
                    filterPanel.add(new JTextField(String.valueOf(new DecimalFormat("###,###.##").format(summ[1]))));
                } else if (i == col[2]) {
                    filterPanel.add(new JTextField(String.valueOf(new DecimalFormat("###,###.##").format(summ[2]))));
                } else if (i == col[3]) {
                    filterPanel.add(new JTextField(String.valueOf(new DecimalFormat("###,###.##").format(summ[3]))));
                } else {
                    filterPanel.add(new JTextField());
                }
            }

            TableColumnModel tcm = table.getColumnModel();
            if (filterPanel.getComponents().length > 0) {
                for (int i = 0; i < tcm.getColumnCount(); i++) {
                    JTextField textField = (JTextField) filterPanel.getComponent(i);
                    Dimension d = textField.getPreferredSize();
                    d.width = tcm.getColumn(i).getWidth();
                    textField.setPreferredSize(d);
                }
            }
            filterPanel.revalidate();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Ошибка ", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void initColumTableMap(JTable table, final HashMap<String, String> map) {
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
    }

    static Object getVid(int vid) {
        String srt = "";
        switch (vid) {
            case 1:
                srt = "муж.";
                break;
            case 2:
                srt = "жен.";
                break;
            case 3:
                srt = "дет.";
                break;
            default:
                srt = "нет";
                break;
        }
        return srt;
    }
}
