package dept.production.planning.ean;

import dept.ves.model.AnalysisConstants;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

/**
 *
 *
 * @author lidashka
 */
public class UtilEan {
    public static String EIGHT_MARCH = "OAO \"8 Марта\"";
    public static String GLN = "4810995900003";
    public static String FACTORY_NAME = "factory_name";
    public static String FACTORY_GS1 = "factory_gs1";
    public static String FACTORY_TEL = "factory_tel";
    public static String FACTORY_EMPL = "factory_empl";

    public static String MAIL_NAME = "mail_name";
    public static String MAIL_ADDRESS = "mail_address";
    public static String MAIL_TEXT = "mail_text";

    public static String FOLDER_SELECT = "";
    public static String SETTING_FOLDER_SELECT = "UtilEan.folderSettingSelect";

    public static String SETTING_FACTORY_NAME = "UtilEan.SettingFactoryName";
    public static String SETTING_FACTORY_GS1 = "UtilEan.SettingFactoryGs1";
    public static String SETTING_FACTORY_TEL = "UtilEan.SettingFactoryTel";
    public static String SETTING_FACTORY_EMPL = "UtilEan.SettingFactoryEmpl";

    public static String SETTING_MAIL_NAME = "UtilEan.SettingMailName";
    public static String SETTING_MAIL_ADDRESS = "UtilEan.SettingMailAdress";
    public static String SETTING_MAIL_TEXT = "UtilEan.SettingMailText";

    public static String DATA_SMALL = "small";
    public static String DATA_BIG = "big";
    public static String DATA_CLASS = "class";
    public static String DATA_FAS_SIZE_PLAN = "size_plan";
    public static String DATA_FAS_SIZE_MSSQL = "size_mssql";
    public static String DATA_FAS_SIZE_EAN_MSSQL = "size_ean_mssql";
    public static String DATA_FAS_PROJECT = "fas_pr";
    public static String DATA_FAS_PLAN = "fas_pl";
    public static String DATA_FAS_MSSQL = "fas_mssql";
    public static String DATA_FAS_MARSH_LIST = "fas_marsh_list";
    public static String DATA_KOD_GPC_SEG = "gpc_seg";
    public static String DATA_KOD_GPC_SEM = "gpc_sem";
    public static String DATA_KOD_GPC_KL = "gpc_kl";
    public static String DATA_KOD_GPC_BR = "gpc_br";
    public static String DATA_KOD_OKRB = "okrb";
    public static String DATA_KOD_THB = "thb";
    public static String DATA_KOD_GOST = "gost";
    public static String DATA_KOD_DEFAULT = "default";

    public static boolean ACTION_BUTT_PLUS = false;
    public static boolean ACTION_BUTT_ITEM_ADD = false;
    public static boolean ACTION_BUTT_SELECT_FAS = false;
    public static boolean ACTION_BUTT_SELECT = false;
    public static boolean ACTION_BUTT_EDIT_COLOR = false;

    public static String COPY = "copy";
    public static String EDIT = "edit";
    public static String OPEN = "open";
    public static String ADD = "add";
    public static String RETURN = "return";

    public static String NEW = "НОВИНКА";
    public static String CHECK_FAS_NAR = "check_nar";
    public static String SELECT_COLOR = "color";

    public static String CHECK_REPEAT = "дубль:";

    public static String ITEM_ADD_FAS_NUM = "";
    public static String ITEM_ADD_FAS_NAME = "";
    public static String ITEM_ADD_FAS_NAR = "";
    public static String ITEM_ADD_FAS_VID = "0";
    public static String ITEM_ADD_FAS_KOLX = "1";
    public static ArrayList<EanItemListSize> ITEM_ADD_RST_RZM;
    public static String ITEM_ADD_SRT = "1";
    public static String ITEM_ADD_SELECT_ITEM_ID = "";
    public static String ITEM_ADD_SELECT_ITEM = "";
    public static Vector ITEM_ADD_FAS_KOD_DEFAULT = new Vector();
    public static String ITEM_ADD_COLOR_NUM = "";
    public static String ITEM_ADD_COLOR_NAME = "";
    public static String ITEM_ADD_PLAN_NUM = "";
    public static String ITEM_ADD_PLAN_NAME = "";
    public static String ITEM_ADD_PROJECT_NUM = "";
    public static String ITEM_ADD_PROJECT_NAME = "";

    public static String ITEM_SEARCH_COLOR_NAME = "";
    public static String ITEM_SEARCH_DATE_MARSH_LIST = "";
    public static String ITEM_SEARCH_DATE_TEMPLATE = "";

    public static String NOTE_SIZE_PLAN = "Размеры из плана производства. Могут отличаться от размеров из классификатора на данную модель;";
    public static String NOTE_SIZE_PROJECT = "Размеры из классификатора на данную модель по всем артикулам;";
    public static String NOTE_SIZE_MSSQL = "Размеры из классификатора на данную модель. Введены оператором НСИ;";
    public static String NOTE_SIZE_MARSH_LIST = "Размеры из классификатора на данную модель. Введены оператором НСИ;";

    static String PATH_EANLIST = "/Eanlist";
    static String PATH_IMPORT_EANLIST = "/EanlistImport";

    static int COLUM_FAS;
    static int COLUM_NAR;
    static int COLUM_COLOR;
    static int COLUM_SRT;

    static int EAN_PREFIX = 232;

    public static String getSrtIzd(int fasSrt) {
        String srt = "";
        switch (fasSrt) {
            case 1:
                srt = "1";
                break;
            case 2:
                srt = "2";
                break;
            case 3:
                srt = "н/c";
                break;
            case 4:
                srt = "У";
                break;
            default:
                srt = "";
                break;
        }
        return srt;
    }

    public static int setSrtIzd(String srt) {
        int i = 1;

        try {
            if (srt.trim().toLowerCase().equals("1"))
                i = 1;
            else if (srt.trim().toLowerCase().equals("2"))
                i = 2;
            else if (srt.trim().toLowerCase().replace(" ", "").replace(" ", "").equals("н/c"))
                i = 3;
            else if (srt.trim().toLowerCase().equals("у"))
                i = 4;
            else
                i = 1;
        } catch (Exception e) {
        }

        return i;
    }

    public static Map<Integer, String> getGenderMap() {
        Map<Integer, String> map = new HashMap<>();
        map.put(411, "Мужской");
        map.put(421, "Мужской");
        map.put(412, "Женский");
        map.put(422, "Женский");
        return map;
    }

    public static Map<String, Boolean> sendGtinsToDatamark(List<String> eans) {
        Map<String, Boolean> gtinsValidation = new HashMap<>();
        String bearer = getTokenForDatamarkService();
        if (bearer != null && !bearer.isEmpty()) {
            eans.forEach(ean -> {
                try {
                    int code = dataMarkVerification(bearer, ean);
                    if (code != 200) {
                        code = dataMarkVerification(bearer, ean);
                    }
                    if (code != 200) {
                        code = dataMarkVerification(bearer, ean);
                    }
                    if (code == 200) {
                        gtinsValidation.put(ean, true);
                    } else {
                        gtinsValidation.put(ean, false);
                    }
                } catch (Exception e) {
                    gtinsValidation.put(ean, false);
                    e.printStackTrace();
                }
            });
        }
        return gtinsValidation;
    }

    public static int dataMarkVerification(String bearer, String ean) throws Exception {
        String urlParameters = "token=" + bearer;
        byte[] postData = urlParameters.getBytes(StandardCharsets.UTF_8);
        int postDataLength = postData.length;
        //https://api.datamark.by https://v2-sandbox-api.datamark.by
        String request = "https://api.datamark.by/items/addByGtin";
        String jsonInputGtin = "{\"gtin\": \"0" + ean + "\"}";
        byte[] data = jsonInputGtin.getBytes(StandardCharsets.UTF_8);
        URL url = new URL(request);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestProperty("token", bearer);
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("charset", "utf-8");
        conn.setRequestProperty("Content-Length", Integer.toString(postDataLength));
        conn.setUseCaches(false);
        conn.setDoInput(true);
        conn.setDoOutput(true);
        try (DataOutputStream wr = new DataOutputStream(conn.getOutputStream())) {
            wr.write(data);
        }
        return conn.getResponseCode();
    }

    public static String getTokenForDatamarkService() {
        String urlParameters = "username=" + AnalysisConstants.USERNAME + "&password=" + AnalysisConstants.PASSWORD + "&is_rules_agree=true";
        byte[] postData = urlParameters.getBytes(StandardCharsets.UTF_8);
        int postDataLength = postData.length;
        //https://api.datamark.by https://v2-sandbox-api.datamark.by
        String request = "https://api.datamark.by/auth";
        try {
            URL url = new URL(request);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setInstanceFollowRedirects(false);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("charset", "utf-8");
            conn.setRequestProperty("Content-Length", Integer.toString(postDataLength));
            conn.setUseCaches(false);
            try (DataOutputStream wr = new DataOutputStream(conn.getOutputStream())) {
                wr.write(postData);
            }
            System.out.println(conn.getResponseCode());
            System.out.println(conn.getResponseMessage());

            BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
            StringBuilder sb = new StringBuilder();
            String output;
            while ((output = br.readLine()) != null) {
                sb.append(output);
            }
            return sb.substring(10, sb.indexOf("\",\"token_refresh\""));
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Ошибка DataMark аунтификация");
            return "";
        }
    }
}
