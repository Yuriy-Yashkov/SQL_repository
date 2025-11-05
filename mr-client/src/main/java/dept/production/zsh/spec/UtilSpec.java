package dept.production.zsh.spec;

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
public class UtilSpec {
    public static Object[] DEPT_ITEMS = {""};
    public static Vector DEPT_MODEL = new Vector();
    public static int DEPT_SELECT_ITEM = -1;
    public static String SETTING_DEPT_SELECT_ITEM = "UtilCena.deptSettingSelectItem";
    public static String SETTING_DEPT_SELECT_ITEM_DIALOG = "UtilCena.deptSettingSelectItemDialog";

    public static int ROUNDING_NORM = 4;
    public static String SETTING_ROUNDING_NORM = "UtilCena.roundingSettingNorm";

    public static String OPERAC = "";

    public static String BUTTON_SAVE = "Сохранить";
    public static String BUTTON_ADD = "Добавить";

    public static String FORMULA_NORM = formatNorm(new Double(0));

    public static String FOREMAN_OOTIZ = "Н.А.Синёва";
    public static String FOREMAN_DEPT = "О.А.Кончиц";
    public static String ENGINEER_VED = "Л.И.Колыванова";
    public static String ENGINEER_1 = "И.А.Фарнина";
    public static String ENGINEER_2 = "Л.В.Бычкова";
    public static String ENGINEER_3 = "E.Д.Шатило";

    public static int SPEC_ID = 1;
    public static String SPEC_NAME = "Спецификация отсутствует";
    public static double SPEC_NORM = 0;
    public static int SPEC_MODEL = -1;
    public static Vector SPEC_SELECTED_MODEL = new Vector();

    public static boolean SPEC_BUTT_SELECT_ACTION = false;
    public static boolean SPEC_BUTT_SAVE_ACTION = false;

    public static int ID_DEPT_SEWING = 8;
    public static int ID_DEPT_OTK = 24;
    public static int ID_TECH_OTK = 42;

    public static String getDept(Vector model, int index) {
        String item = "";
        for (Iterator it = model.iterator(); it.hasNext(); ) {
            Item object = (Item) it.next();
            if (object.getId() == index) {
                item = object.getDescription();
                break;
            }
        }
        return item;
    }

    public static String getFormatRoundingNorm() {
        String format = "0.0000";

        if (ROUNDING_NORM > 0) {
            format = "0.0";
            for (int i = 1; i < ROUNDING_NORM; i++)
                format += "0";
        }
        return format;
    }

    public static String formatNorm(double data) {
        DecimalFormatSymbols s = new DecimalFormatSymbols();
        s.setDecimalSeparator('.');
        return (new DecimalFormat(getFormatRoundingNorm(), s).format(new BigDecimal(data).setScale(ROUNDING_NORM, RoundingMode.HALF_UP).doubleValue()));
    }
}
