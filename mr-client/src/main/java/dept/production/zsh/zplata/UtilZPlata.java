package dept.production.zsh.zplata;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Iterator;
import java.util.Vector;

/**
 *
 * @author lidashka
 */
public class UtilZPlata {
    public static String COPY = "copy";
    public static String EDIT = "edit";
    public static String OPEN = "open";
    public static String ADD = "add";

    public static int[] COL_TECH = {4, 6, 7, 7};
    public static String FOLDER_SELECT = "";
    public static String SETTING_FOLDER_SELECT = "UtilZPlata.folderSettingSelect";
    public static Vector DEPT_MODEL = new Vector();
    public static int DEPT_SELECT_ITEM = -1;
    public static String SETTING_DEPT_SELECT_ITEM = "UtilZPlata.deptSettingSelectItem";
    public static Vector BRIG_MODEL = new Vector();
    public static int BRIG_SELECT_ITEM = -1;
    public static String SETTING_BRIG_SELECT_ITEM = "UtilZPlata.brigSettingSelectItem";
    public static Vector BRIGADIR_MODEL = new Vector();
    public static int BRIGADIR_SELECT_ITEM = -1;
    public static String SETTING_BRIGADIR_SELECT_ITEM = "UtilZPlata.brigadirSettingSelectItem";
    public static int ROUNDING_NORM = 4;
    public static String SETTING_ROUNDING_NORM = "UtilZPlata.roundingSettingNorm";
    public static int PLAN_WORKING_TIME = 169;
    public static String SETTING_PLAN_WORKING_TIME = "UtilZPlata.planSettingWorkingTime";
    public static double RATE_1ST_CATEGORY = 130;
    public static String SETTING_RATE_1ST_CATEGORY = "UtilZPlata.rateSetting1stCategory";
    public static String PROF_STAFF_1 = "Нач. швейного цеха:";
    public static String STAFF_1 = "О.А.Кончиц:";
    public static String SETTING_STAFF_1 = "UtilZPlata.staff_1Setting1stCategory";
    public static String PROF_STAFF_2 = "Нач. раскройного цеха:";
    public static String STAFF_2 = "Т.Н. Зых";
    public static String SETTING_STAFF_2 = "UtilZPlata.staff_2Setting1stCategory";
    public static String PROF_STAFF_3 = "Пом. мастера:";
    public static String STAFF_3 = "";
    public static String SETTING_STAFF_3 = "UtilZPlata.staff_3Setting1stCategory";
    public static String PROF_STAFF_4 = "Мастер смены:";
    public static String STAFF_4 = "В.И. Солошенко";
    public static String SETTING_STAFF_4 = "UtilZPlata.staff_4Setting1stCategory";
    public static String PROF_STAFF_5 = "Экономист:";
    public static String STAFF_5 = "";
    public static String SETTING_STAFF_5 = "UtilZPlata.staff_5Setting1stCategory";
    public static String DATE_VVOD = "";
    public static String SETTING_DATE_VVOD = "UtilZPlata.dateSettingVvod";
    public static String BUTTON_SAVE = "Сохранить";
    public static String BUTTON_ADD = "Добавить";
    public static String OTCHET_T4_NEW = "ZPlata(T4_NEW).ots";
    public static String OTCHET_T4_NEW_SEWING_WORKSHOP = "ZPlata(T4_NEW_FOR_SEWING_WORKSHOP).ots";
    public static String OTCHET_T4_PL_NEW = "ZPlata(T4+_NEW).ots";
    public static String OTCHET_T4_PL_NEW_SEWING_WORKSHOP = "ZPlata(T4+_NEW_FOR_SEWING_WORKSHOP).ots";
    public static String OTCHET_T4_NEW_BY_PERSONAL_NUMBER = "ZPlata(T4_NEW_BY_PN).ots";
    public static String OTCHET_T4_NEW_BY_PERSONAL_NUMBER_SEWING_WORKSHOP = "ZPlata(T4_NEW_BY_PN_FOR_SEWING_WORKSHOP).ots";
    public static String OTCHET_T4_PL_NEW_BY_PERSONAL_NUMBER = "ZPlata(T4+_NEW_BY_PN).ots";
    public static String OTCHET_T4_PL_NEW_BY_PERSONAL_NUMBER_SEWING_WORKSHOP = "ZPlata(T4+_NEW_BY_PN_FOR_SEWING_WORKSHOP).ots";
    public static final String NV_BRIG_REP = "ZPlata(NV_BRIG_REP).ots";
    public static String OTCHET_NV = "ZPlata(NV).ots";
    public static String OTCHET_NV_EMPL = "ZPlata(NV_EMPL).ots";
    public static String OTCHET_NV_EMPL_KR = "ZPlata(NV_EMPL_KR).ots";
    public static String OTCHET_NV_MODEL = "ZPlata(NV_MODEL).ots";
    public static String OTCHET_PROF_OSVOENIE = "освоение профессии";
    public static String KOLVO_MODEL = "model";
    public static String KOLVO_MARSHRUT = "marshrut";
    public static String KOLVO_ERROR = "error";
    public static boolean BRIGADIR_EDIT = false;
    public static double NESORT_KOF = 0.016;
    static boolean ACTION_BUTT_SELECT_SEARCH = false;
    static String ITEM_SELECT_SEARCH_ID = "";
    static String ITEM_SELECT_SEARCH_DATE = "";
    static String ITEM_SELECT_SEARCH_DEPT = "";
    static String ITEM_SELECT_SEARCH_BRIG = "";
    static String ITEM_SELECT_SEARCH_PERIOD = "";

    public static Item getIndexModel(Vector model, int index) throws Exception {
        Item item = null;
        try {
            for (Iterator it = model.iterator(); it.hasNext(); ) {
                Item object = (Item) it.next();
                if (object.getId() == index) {
                    item = object;
                    break;
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new Exception("Ошибка: " + e.getMessage(), e);
        }
        return item;
    }

    public static int getItemsModel(Vector model, String item) throws Exception {
        int index = -1;
        try {
            for (Iterator it = model.iterator(); it.hasNext(); ) {
                Item object = (Item) it.next();
                if (object.getDescription().equals(item)) {
                    index = object.getId();
                    break;
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new Exception("Ошибка: " + e.getMessage(), e);
        }
        return index;
    }

    public static void fullModel(Vector model, Vector data) throws Exception {
        try {
            model.add(new Item(-1, "Все...", ""));
            for (int j = 0; j < data.size(); j++)
                model.add(new Item(Integer.parseInt(((Vector) data.elementAt(j)).get(0).toString()), ((Vector) data.elementAt(j)).get(1).toString(), ""));

        } catch (Exception e) {
            model = new Vector();
            System.out.println(e.getMessage());
            throw new Exception("Ошибка: " + e.getMessage(), e);
        }
    }

    public static String getFormatRoundingNorm(int rounding) {
        String format = "0.0000";

        if (rounding > 0) {
            format = "0.0";
            for (int i = 1; i < rounding; i++)
                format += "0";
        }
        return format;
    }

    public static String formatNorm(double value, int rounding) {
        DecimalFormatSymbols s = new DecimalFormatSymbols();
        s.setDecimalSeparator('.');
        return (new DecimalFormat(getFormatRoundingNorm(rounding), s).format(new BigDecimal(value).setScale(rounding, RoundingMode.HALF_UP).doubleValue()));
    }
}
