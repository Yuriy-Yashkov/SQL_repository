package dept.sprav.product;


/**
 *
 * @author дlidashka
 */
public class UtilProduct {
    public static String COPY = "copy";
    public static String EDIT = "edit";
    public static String OPEN = "open";
    public static String ADD = "add";

    public static String SELECT = "select_default";
    public static String SELECT_TO = "select_to";

    public static int KOD_GOST = 1;
    public static int KOD_TNV = 2;
    public static int KOD_OKRB = 3;
    public static int KOD_SEG = 4;
    public static int KOD_SEM = 5;
    public static int KOD_KL = 6;
    public static int KOD_BR = 7;
    public static int KOD_DESIGNER = 1;
    public static int KOD_MASTER = 1;
    public static int KOD_TO_GENERAL = 0;
    public static int KOD_TO_RST_OFF = 1;
    public static int KOD_TO_RST_ON = 2;

    public static String TYPE_IZD_NAME = "izd_name";
    public static String TYPE_IZD_BRAND = "izd_brand";
    public static String TYPE_IZD_IZM = "izd_izm";
    public static String TYPE_IZD_TIP = "izd_tip";
    public static String TYPE_IZD_VID = "izd_vid";
    public static String TYPE_IZD_GROUP = "izd_group";
    public static String TYPE_IZD_ASSORT = "izd_assort";
    public static String TYPE_IZD_KOD_SEG = "izd_kod_seg";
    public static String TYPE_IZD_KOD_SEM = "izd_kod_sem";
    public static String TYPE_IZD_KOD_KL = "izd_kod_kl";
    public static String TYPE_IZD_KOD_BR = "izd_kod_br";
    public static String TYPE_IZD_KOD_GOST = "izd_kod_gost";
    public static String TYPE_IZD_KOD_TNV = "izd_kod_tnv";
    public static String TYPE_IZD_KOD_OKRB = "izd_kod_okrb";
    public static String TYPE_EMPL_DESIGNER = "empl_designer";
    public static String TYPE_EMPL_MASTER = "empl_master";
    public static String TYPE_TO_GENERAL = "to_general";
    public static String TYPE_TO_RST_ON = "to_rst_on";
    public static String TYPE_TO_RST_OFF = "to_rst_off";

    /**
     * Показывает нажатие кнопки выбора
     */
    static boolean ACTION_BUTT_SELECT_SEARCH = false;

    /** Данные выбранные пользователем в результате выбора/поиска   */
    static ItemSelect ITEM_SELECT_SEARCH = new ItemSelect();

    /** Данные выбранные пользователем в результате выбора/поиска тех. оп.   */
    static ItemSelectTO ITEM_SELECT_SEARCH_TO = new ItemSelectTO();

    static String getNameTipTO(int idTip) {
        String srt = "";

        try {
            switch (idTip) {
                case 0:
                    srt = "основной";
                    break;
                case 1:
                    srt = "не зав. от роста";
                    break;
                case 2:
                    srt = "зав. от роста";
                    break;
                default:
                    srt = "";
                    break;
            }
        } catch (Exception e) {
        }

        return srt;
    }

}
