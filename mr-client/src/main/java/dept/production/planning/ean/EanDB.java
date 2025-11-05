package dept.production.planning.ean;

import workDB.DB_new;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.logging.Logger;

public class EanDB extends DB_new {

    private static final Logger log = Logger.getLogger(EanDB.class.getName());

    public Vector getKodThb() {
        Vector vec = new Vector();
        String sql = "Select distinct kod from nsi_wkd Order by kod";
        try (PreparedStatement ps = getConnection().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                String kod = rs.getString("kod");
                vec.add(kod);
            }
        } catch (Exception e) {
            log.severe("Ошибка getKodThb()" + e);
        }
        return vec;
    }

    public Couple<String, Integer> getTnvedByItemModel(String fas) {
        HashMap<Date, Couple<String, Integer>> map = new HashMap<>();
        String sql = "SELECT DISTINCT narp, sar, datekrkv "
                + "     FROM nsi_kld "
                + "     WHERE nsi_kld.fas = ? "
                + "     ORDER BY datekrkv desc";
        try (ResultSet rs = execute(sql, mapParameters(Collections.singletonList(fas)))) {
            while (rs.next()) {
                map.put(rs.getDate("datekrkv"), new Couple<>(rs.getString("narp"), rs.getInt("sar")));
            }
            return map.isEmpty()
                    ? new Couple<>("", 0)
                    : map.get(map.entrySet().stream()
                    .map(el -> el.getKey())
                    .max(Date::compareTo).get());
        } catch (SQLException e) {
            log.severe("Ошибка получения tnved" + e);
            return new Couple<>("", 0);
        }
    }

    private Map<Integer, Couple> mapParameters(final List parameters) {
        Map<Integer, Couple> map = new HashMap<>();
        parameters.stream().forEach(el -> map.put(parameters.indexOf(el) + 1, new Couple(el.getClass(), el)));
        return map;
    }

    private ResultSet execute(String sql, Map<Integer, Couple> parameters) {
        try (PreparedStatement ps = getConnection().prepareStatement(sql)) {
            parameters.forEach((key, value) -> {
                if (value.getV1().equals(Integer.class)) {
                    try {
                        ps.setInt(key, (Integer) value.getV2());
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }
                if (value.getV1().equals(String.class)) {
                    try {
                        ps.setString(key, (String) value.getV2());
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
            return ps.executeQuery();
        } catch (Exception e) {
            return null;
        }
    }

    public Integer getSarByFasAndNAr(Integer fas, String nar) {
        int sar = 0;
        String sql = "SELECT DISTINCT sar, datekrkv "
                + "     FROM nsi_kld "
                + "     WHERE nsi_kld.fas = ? and nsi_kld.nar = ? "
                + "     ORDER BY datekrkv desc";
        try (PreparedStatement ps = getConnection().prepareStatement(sql)) {
            ps.setInt(1, fas);
            ps.setString(2, nar);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                sar = rs.getInt("sar");
            }
            return sar;
        } catch (Exception e) {
            return 0;
        }
    }

    public Vector openRstRzm(int fas) {
        Vector elements = new Vector();
        String fsql = "SELECT DISTINCT rst, rzm "
                + "     FROM nsi_sd INNER JOIN "
                + "          nsi_kld ON nsi_sd.kod = nsi_kld.kod "
                + "     WHERE nsi_kld.fas = ? and rst <> 0 "
                + "     ORDER BY rst, rzm ";
        String ssql = "SELECT DISTINCT rzm "
                + "     FROM nsi_sd INNER JOIN "
                + "          nsi_kld ON nsi_sd.kod = nsi_kld.kod "
                + "     WHERE nsi_kld.fas = ? "
                + "     ORDER BY rzm ";

        try (PreparedStatement fps = getConnection().prepareStatement(fsql);
             PreparedStatement sps = getConnection().prepareStatement(ssql);) {
            fps.setInt(1, fas);
            ResultSet frs = fps.executeQuery();
            while (frs.next()) {
                Vector tmp = new Vector();
                tmp.add(false);
                tmp.add(frs.getInt("rst"));
                tmp.add(frs.getInt("rzm"));
                tmp.add("");
                tmp.add("");
                tmp.add("");
                tmp.add("");
                tmp.add("");
                elements.add(tmp);
            }

            sps.setInt(1, fas);
            ResultSet srs = sps.executeQuery();
            while (srs.next()) {
                Vector tmp = new Vector();
                tmp.add(false);
                tmp.add(0);
                tmp.add(srs.getInt("rzm"));
                tmp.add("");
                tmp.add("");
                elements.add(tmp);
            }

        } catch (Exception e) {
            log.severe("Ошибка openRstRzm()" + e);
        }
        return elements;
    }

    public Vector openRstRzm(int fas, int sar, String nar, int srt, int idColor) {
        Vector elements = new Vector();
        Vector tmp;
        String sql = "SELECT DISTINCT rst, rzm, rzm_print, nsi_sd.kod1 as kod1 "
                + "     FROM nsi_sd INNER JOIN "
                + "          nsi_kld ON nsi_sd.kod = nsi_kld.kod "
                + "     WHERE nsi_kld.fas = ? and "
                + "           nsi_kld.sar = ? AND "
                + "           nsi_kld.nar like ? AND "
                + "           nsi_sd.srt = ? "
                + "     ORDER BY rst, rzm ";

        String sqlWithoutColor = "SELECT DISTINCT "
                + "     EANCODE, "
                + "     REFERENCE_COLOR.[NAME] as color "
                + " FROM  "
                + "     NSI_EANCODE, "
                + "     REFERENCE_COLOR"
                + " WHERE"
                + "     NSI_EANCODE.REF_COLOR_ID = REFERENCE_COLOR.ID AND "
                + "     NSI_EANCODE.ITEM_CODE = ? AND "
                + "     NSI_EANCODE.REF_COLOR_ID = ? "
                + " ORDER BY color";

        String sqlWithColor = "SELECT DISTINCT "
                + "     EANCODE, "
                + "     REFERENCE_COLOR.[NAME] as color "
                + " FROM  "
                + "     NSI_EANCODE, "
                + "     REFERENCE_COLOR"
                + " WHERE"
                + "     NSI_EANCODE.REF_COLOR_ID = REFERENCE_COLOR.ID AND "
                + "     NSI_EANCODE.ITEM_CODE = ? AND "
                + "     NSI_EANCODE.REF_COLOR_ID = ? "
                + " ORDER BY color";

        try (PreparedStatement ps = getConnection().prepareStatement(sql);
             PreparedStatement psWithoutColor = getConnection().prepareStatement(sqlWithoutColor);
             PreparedStatement psWithColor = getConnection().prepareStatement(sqlWithColor);) {

            ps.setInt(1, fas);
            ps.setInt(2, sar);
            ps.setString(3, nar.toUpperCase() + "%");
            ps.setInt(4, srt);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                StringBuilder ean13 = new StringBuilder("");

                psWithoutColor.setInt(1, rs.getInt("kod1"));
                psWithoutColor.setInt(2, 2);
                ResultSet rsWithoutColor = psWithoutColor.executeQuery();

                while (rsWithoutColor.next())
                    ean13.append(rsWithoutColor.getString("EANCODE").trim()).append(" ").append(rsWithoutColor.getString("color"));

                if (idColor > 2) {
                    psWithColor.setInt(1, rs.getInt("kod1"));
                    psWithColor.setInt(2, idColor);
                    ResultSet rsWithColor = psWithColor.executeQuery();

                    while (rsWithColor.next())
                        ean13.append(rsWithColor.getString("EANCODE").trim()).append(" ").append(rsWithColor.getString("color"));
                }

                tmp = new Vector();
                tmp.add(false);
                tmp.add(rs.getInt("rst"));
                tmp.add(rs.getInt("rzm"));
                tmp.add(rs.getObject("rzm_print") == null ? "" : rs.getObject("rzm_print").toString().trim());
                tmp.add(ean13);
                tmp.add("");
                tmp.add("");
                tmp.add("");
                elements.add(tmp);
            }

        } catch (Exception e) {
            log.severe("Ошибка openRstRzm()" + e);
        }
        return elements;
    }

    public Vector getSARFromKLD(String modelNumber, String nameArticle) {
        Vector elements = new Vector();
        String sql = "Select distinct fas, ngpr, nar, sar, prim = CASE WHEN prim IS NULL THEN '' ELSE prim END  "
                + "     From nsi_kld "
                + "     Where fas like ? and  "
                + "           fas >= 0 "
                + "           and nar like ? "
                + "     Order by fas, nar, sar, ngpr";

        try (PreparedStatement ps = getConnection().prepareStatement(sql)) {

            ps.setString(1, modelNumber.trim() + "%");
            ps.setString(2, nameArticle.trim() + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Vector tmp = new Vector();
                tmp.add(rs.getInt("fas"));                                      // модель
                tmp.add(rs.getInt("sar"));                                      // шифр артикула
                tmp.add(rs.getString("nar").trim().toUpperCase());              // артикул
                tmp.add(rs.getString("ngpr").trim().toLowerCase());             // название
                tmp.add(rs.getString("prim").trim());                           // состав сырья
                elements.add(tmp);
            }

        } catch (Exception e) {
            log.severe("Ошибка getSARFromKLD()" + e);
        }
        return elements;
    }

    public Vector getAllModels(String fasNum) throws SQLException {
        Vector elements = new Vector();
        String sql = "Select distinct fas, ngpr, nar, sar, prim = CASE WHEN prim IS NULL THEN '' ELSE prim END  "
                + "     From nsi_kld "
                + "     Where fas like ? and  "
                + "           fas >= 0 "
                + "     Order by fas, nar, sar, ngpr";

        try (PreparedStatement ps = getConnection().prepareStatement(sql)) {
            ps.setString(1, fasNum + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Vector tmp = new Vector();
                tmp.add(rs.getInt("fas"));                                      // модель
                tmp.add(rs.getInt("sar"));                                      // шифр артикула
                tmp.add(rs.getString("nar").trim().toUpperCase());              // артикул
                tmp.add(rs.getString("ngpr").trim().toLowerCase());             // название
                tmp.add(rs.getString("prim").trim());                           // состав сырья
                elements.add(tmp);
            }

        } catch (Exception e) {
            log.severe("Ошибка getAllModels()" + e);
        }
        return elements;
    }

    public Vector getDetalFas(int fas, int sar) {
        Vector elements = new Vector();
        String sql = "SELECT TOP 1 fas, ngpr, nar, sar, prim = CASE WHEN prim IS NULL THEN '' ELSE prim END "
                + " FROM  nsi_kld INNER JOIN"
                + "        nsi_sd ON nsi_sd.kod = nsi_kld.kod "
                + " WHERE nsi_kld.fas = ? AND "
                + "       nsi_kld.sar = ? AND "
                + "       nsi_kld.fas > 0 ";
        try (PreparedStatement ps = getConnection().prepareStatement(sql)) {
            ps.setInt(1, fas);
            ps.setInt(2, sar);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                elements.add(rs.getInt("sar"));
                elements.add(rs.getString("nar").trim().toUpperCase());
                elements.add(rs.getInt("fas"));
                elements.add(rs.getString("ngpr").trim().toUpperCase());
                elements.add(rs.getString("prim").trim());
            }

        } catch (Exception e) {
            log.severe("Ошибка getDetalFas()" + e);
        }
        return elements;
    }

    public Vector getEanFas(int fas, int sar, String nar, int srt, int rst, int rzm) {
        Vector elements = new Vector();
        String sql = "SELECT rzm_print, nsi_sd.kod1 as id_ "
                + " FROM  nsi_kld INNER JOIN"
                + "        nsi_sd ON nsi_sd.kod = nsi_kld.kod "
                + " WHERE nsi_kld.fas = ? AND "
                + "       nsi_kld.sar = ? AND "
                + "       nsi_kld.nar like ? AND "
                + "       nsi_sd.srt = ? AND "
                + "       nsi_sd.rst = ? AND "
                + "       nsi_sd.rzm = ? AND "
                + "       nsi_kld.fas > 0 ";
        String fsql = "SELECT DISTINCT "
                + "     EANCODE, "
                + "     REFERENCE_COLOR.[NAME] as color "
                + " FROM  "
                + "     NSI_EANCODE, "
                + "     REFERENCE_COLOR"
                + " WHERE"
                + "     NSI_EANCODE.REF_COLOR_ID = REFERENCE_COLOR.ID AND "
                + "     NSI_EANCODE.ITEM_CODE = ? "
                + " ORDER BY color";
        StringBuilder ean13;

        try (PreparedStatement ps = getConnection().prepareStatement(sql)) {

            ps.setInt(1, fas);
            ps.setInt(2, sar);
            ps.setString(3, nar.toUpperCase() + "%");
            ps.setInt(4, srt);
            ps.setInt(5, rst);
            ps.setInt(6, rzm);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                elements.add(rs.getObject("rzm_print") == null
                        ? ""
                        : rs.getObject("rzm_print").toString().trim());
                try(PreparedStatement fps = getConnection().prepareStatement(fsql)) {
                    fps.setInt(1, rs.getInt("id_"));
                    ResultSet rs_ = fps.executeQuery();

                    ean13 = new StringBuilder();
                    while (rs_.next()) {
                        ean13.append(rs_.getObject("EANCODE")).append(" ").append(rs_.getString("color")).append("\n<br> ");
                    }

                    elements.add("<html>" + ean13 + "</html>");
                }
            }

        } catch (Exception e) {
            log.severe("Ошибка getEanFas()" + e);
        }
        return elements;
    }

    Vector getCheckFasNar(String nar) {
        Vector elements = new Vector();
        String sql = "SELECT distinct ngpr, fas, sar, nar "
                + " FROM  nsi_kld "
                + " WHERE nsi_kld.nar like ?  ";

        try (PreparedStatement ps = getConnection().prepareStatement(sql)) {

            ps.setString(1, "%" + nar.toUpperCase().replace("C", "С") + "%");

            executeQuery(elements, ps);

            ps.setString(1, "%" + nar.toUpperCase().replace("С", "C") + "%");

            executeQuery(elements, ps);

        } catch (Exception e) {
            log.severe("Ошибка getCheckFasNar()" + e);
        }
        return elements;
    }

    private void executeQuery(Vector elements, PreparedStatement ps) throws SQLException {
        try(ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Vector tmp = new Vector();
                tmp.add(rs.getString("ngpr").trim().toUpperCase());
                tmp.add(rs.getInt("fas"));
                tmp.add(rs.getString("nar").trim().toUpperCase());
                tmp.add(rs.getInt("sar"));
                elements.add(tmp);
            }
        }
    }

    public String importEancodeWithColor(EanItem item, int rst, int rzm) throws SQLException {
        String message = "НЕУДАЧА";
        String sql;

        try {
            setAutoCommit(false);

            if (item.getColorNum() <= 0)
                message = "Цвет не корректно задан";
            else { // Цвет задан
                if (!item.getColorName().trim().isEmpty()) { // Цвет не пусто
                    for (EanItemListSize size : item.getDataSize()) {
                        if (size.getRst() == rst && size.getRzm() == rzm) {

                            if (size.getEan13().contains(UtilEan.CHECK_REPEAT))
                                size.setEan13(size.getEan13().replace(UtilEan.CHECK_REPEAT, "").trim());

                            if (size.getEan13().length() != 13)
                                message = "EAN не 13 символов";

                            else { // EAN 13 символов
                                // Поиск модель в таблице NSI_EANCODE
                                sql = " SELECT ITEM_ID, ITEM_PRINT_SIZE, COLOR_ID, COLOR_NAME, EAN_CODE "
                                        + " FROM "
                                        + "         V_PRODUCTION_COLORS "
                                        + " WHERE "
                                        + "         ARTICLE_NUMBER like upper('" + item.getFasNar().toUpperCase().replace("C", "_").replace("С", "_") + "') and "
                                        + "         MODEL_NUMBER = " + item.getFasNum() + " and "
                                        + "         ITEM_GROWTH = " + size.getRst() + " and "
                                        + "         ITEM_SIZE = " + size.getRzm() + " and "
                                        + "         ITEM_GRADE = " + item.getFasSrt() + " and "
                                        + "         COLOR_NAME like upper('" + item.getColorName() + "') and "
                                        + "         COLOR_ID = " + item.getColorNum() + " ";
                                ps = conn.prepareStatement(sql);
                                rs = ps.executeQuery();

                                if (rs.next()) { // Запись найдена в таблице NSI_EANCODE
                                    if (!size.getSizePrint().trim().equalsIgnoreCase(rs.getString("ITEM_PRINT_SIZE").trim()))
                                        message = "Этикетка не совпадает " + rs.getString("ITEM_PRINT_SIZE").trim().toLowerCase();

                                    else {  // Размер на этикетке совпадает
                                        if (rs.getString("EAN_CODE").trim().isEmpty()
                                                || rs.getString("EAN_CODE").trim().startsWith("232")) { // поле EANCODE пусто

                                            // Импорт ean13. Обновление записи;
                                            sql = "Update NSI_EANCODE "
                                                    + " Set EANCODE = ? "
                                                    + " Where ITEM_CODE = ? ";
                                            ps = conn.prepareStatement(sql);
                                            ps.setString(1, size.getEan13());
                                            ps.setInt(2, rs.getInt("ITEM_ID"));
                                            ps.execute();

                                            message = "ИМПОРТ с цветом. Обновление записи.";

                                        } else {  // поле EANCODE не пусто
                                            if (size.getEan13().trim().equals(rs.getString("EAN_CODE").trim()))
                                                message = "ИМПОРТ, записан корректно";
                                            else
                                                message = "в БД, не совпадает EAN";
                                        }
                                    }
                                } else { // Запись модели не найдена в таблице NSI_EANCODE
                                    // Поиск запись в таблице nsi_sd
                                    sql = "Select nsi_sd.kod1 as kodSize, "
                                            + "   nsi_sd.rzm_print as printSize, "
                                            + "   nsi_sd.ean as eanSize "
                                            + "  From nsi_sd, nsi_kld "
                                            + "  Where nsi_kld.kod = nsi_sd.kod and "
                                            + "        nar like upper('" + item.getFasNar().toUpperCase().replace("C", "_").replace("С", "_") + "') and "
                                            + "        fas = " + item.getFasNum() + " and "
                                            + "        rst = " + size.getRst() + " and "
                                            + "        rzm = " + size.getRzm() + " and "
                                            + "        srt = " + item.getFasSrt() + " ";
                                    ps = conn.prepareStatement(sql);
                                    rs = ps.executeQuery();

                                    if (rs.next()) { // Запись найдена в таблице nsi_sd
                                        if (!size.getSizePrint().trim().equalsIgnoreCase(rs.getString("printSize").trim()))
                                            message = "Этикетка не совпадает " + rs.getString("printSize").trim().toLowerCase();

                                        else { // Размер на этикетке совпадает
                                            // Импорт ean13. Добавление записи;
                                            sql = "Insert into NSI_EANCODE (ITEM_CODE, REF_COLOR_ID, EANCODE) values (?,?,?) ";
                                            ps = conn.prepareStatement(sql);
                                            ps.setInt(1, rs.getInt("kodSize"));
                                            ps.setInt(2, item.getColorNum());
                                            ps.setString(3, size.getEan13());
                                            ps.execute();

                                            message = "ИМПОРТ с цветом";
                                        }
                                    } else  // Запись не найдена в nsi_sd
                                        message = "в БД не найдена запись ";
                                }
                            }
                        }
                    }
                }
            }
            commit();

        } catch (SQLException e) {
            rollback();
            log.severe("Ошибка importEANwithColor()" + e);
            throw new SQLException("Ошибка importEANwithColor() " + e.getMessage(), e);
        } finally {
            setAutoCommit(true);
        }
        return message;
    }

    public String importEancodeWithColor(int fas, String nar, int srt, int rst, int rzm, String ean, int idColor) throws SQLException {
        String message = "НЕУДАЧА";
        String sql;

        try {
            if (idColor <= 0) message = "Цвет не корректно задан";
            else {
                if (ean.contains(UtilEan.CHECK_REPEAT))
                    ean = ean.replace(UtilEan.CHECK_REPEAT, "").trim();

                if (ean.length() != 13) message = "EAN не 13 символов";
                else {
                    // EAN 13 символов
                    // Ищем модель в таблице NSI_EANCODE
                    sql = " SELECT ITEM_ID, ITEM_PRINT_SIZE, COLOR_ID, COLOR_NAME, EAN_CODE "
                            + " FROM "
                            + "         V_PRODUCTION_COLORS "
                            + " WHERE "
                            + "         ARTICLE_NUMBER like upper('" + nar.toUpperCase().replace("C", "_").replace("С", "_") + "') and "
                            + "         MODEL_NUMBER = ? and "
                            + "         ITEM_GRADE = ? and "
                            + "         ITEM_GROWTH = ? and "
                            + "         ITEM_SIZE = ? and "
                            + "         COLOR_ID = ? ";

                    ps = conn.prepareStatement(sql);
                    ps.setInt(1, fas);
                    ps.setInt(2, srt);
                    ps.setInt(3, rst);
                    ps.setInt(4, rzm);
                    ps.setInt(5, idColor);
                    rs = ps.executeQuery();

                    if (rs.next()) {
                        // поле EANCODE пусто
                        if (rs.getString("EAN_CODE").trim().isEmpty()
                                || rs.getString("EAN_CODE").trim().startsWith("232")) {
                            // Импорт ean13. Обновление записи;
                            sql = "Update NSI_EANCODE "
                                    + " Set EANCODE = ? "
                                    + " Where ITEM_CODE = ? ";

                            ps = conn.prepareStatement(sql);
                            ps.setString(1, ean);
                            ps.setInt(2, rs.getInt("ITEM_ID"));
                            ps.execute();

                            message = "ИМПОРТ с цветом. Обновление записи.";
                        } else {
                            // поле EANCODE не пусто
                            if (ean.trim().equals(rs.getString("EAN_CODE").trim()))
                                message = "ИМПОРТ, записан корректно";
                            else
                                message = "в БД, не совпадает EAN";
                        }

                    } else {
                        // Запись модели не найдена в таблице NSI_EANCODE
                        // Ищем модель в таблице nsi_sd
                        sql = " Select nsi_sd.kod1 as kodSize, "
                                + "     nsi_sd.rzm_print as printSize, "
                                + "     nsi_sd.ean as eanSize "
                                + " From "
                                + "     nsi_sd, nsi_kld "
                                + " Where nsi_kld.kod = nsi_sd.kod and "
                                + "        nar like upper('" + nar.toUpperCase().replace("C", "_").replace("С", "_") + "') and "
                                + "        fas = ? and "
                                + "        srt = ? and "
                                + "        rst = ? and "
                                + "        rzm = ?  ";

                        ps = conn.prepareStatement(sql);
                        ps.setInt(1, fas);
                        ps.setInt(2, srt);
                        ps.setInt(3, rst);
                        ps.setInt(4, rzm);
                        rs = ps.executeQuery();

                        if (rs.next()) {
                            // Запись модели найдена в таблице nsi_sd
                            // Импорт ean13. Добавление записи;
                            sql = "Insert into NSI_EANCODE (ITEM_CODE, REF_COLOR_ID, EANCODE) values (?,?,?) ";

                            ps = conn.prepareStatement(sql);
                            ps.setInt(1, rs.getInt("kodSize"));
                            ps.setInt(2, idColor);
                            ps.setString(3, ean);
                            ps.execute();

                            message = "ИМПОРТ с цветом";
                        } else
                            // Запись не найдена в nsi_sd
                            message = "в БД не найдена запись ";
                    }
                }
            }
        } catch (SQLException e) {
            log.severe("Ошибка importEancodeWithColor()" + e);
            throw new SQLException("Ошибка importEancodeWithColor() " + e.getMessage(), e);
        }
        return message;
    }

    public String importEancode(EanItem item, int rst, int rzm) throws SQLException {
        String message = "НЕУДАЧА";
        String sql;

        try {
            setAutoCommit(false);

            if (item.getColorName().trim().isEmpty()) {
                for (EanItemListSize size : item.getDataSize()) {
                    if (size.getRst() == rst && size.getRzm() == rzm) {

                        if (size.getEan13().contains(UtilEan.CHECK_REPEAT))
                            size.setEan13(size.getEan13().replace(UtilEan.CHECK_REPEAT, "").trim());

                        if (size.getEan13().length() == 13) {
                            sql = "Select nsi_sd.kod1 as kodSize, "
                                    + "   nsi_sd.rzm_print as printSize, "
                                    + "   nsi_sd.ean as eanSize "
                                    + "  From nsi_sd, nsi_kld "
                                    + "  Where nsi_kld.kod = nsi_sd.kod and "
                                    + "        nar like upper('" + item.getFasNar().toUpperCase().replace("C", "_").replace("С", "_") + "') and "
                                    + "        fas = " + item.getFasNum() + " and "
                                    + "        rst = " + size.getRst() + " and "
                                    + "        rzm = " + size.getRzm() + " and "
                                    + "        srt = " + item.getFasSrt() + " ";

                            ps = conn.prepareStatement(sql);
                            rs = ps.executeQuery();

                            if (rs.next()) {
                                if (size.getSizePrint().trim().equalsIgnoreCase(rs.getString("printSize").trim())) {

                                    sql = "Update nsi_sd "
                                            + " Set ean = ? "
                                            + " Where nsi_sd.kod1 = ? ";

                                    ps = conn.prepareStatement(sql);
                                    ps.setString(1, size.getEan13());
                                    ps.setInt(2, rs.getInt("kodSize"));
                                    ps.execute();

                                    message = "ИМПОРТ";

                                } else
                                    message = "этикетка не совпадает " + rs.getString("printSize").trim().toLowerCase();
                            } else
                                message = "в БД не найдена запись";
                        } else
                            message = "EAN не 13 символов";
                    }
                }
            }
            commit();

        } catch (SQLException e) {
            rollback();
            log.severe("Ошибка importEAN()" + e);
            throw new SQLException("Ошибка importEAN() " + e.getMessage(), e);
        } finally {
            setAutoCommit(true);
        }
        return message;
    }

    public Vector getColor() throws SQLException {
        Vector elements = new Vector();
        String sql = "SELECT "
                + "     ID, NAME "
                + " FROM  "
                + "     REFERENCE_COLOR "
                + " WHERE "
                + "     PARENT_ID = 1 and "
                + "     NAME NOT LIKE 'РЕЗЕРВ%'"
                + " ORDER BY "
                + "     NAME";
        Vector tmp;

        try (PreparedStatement ps = getConnection().prepareStatement(sql);
             ResultSet rs = ps.executeQuery();) {
            while (rs.next()) {
                tmp = new Vector();
                tmp.add(rs.getInt("ID"));
                tmp.add(rs.getString("NAME").trim().toUpperCase());
                elements.add(tmp);
            }

        } catch (Exception e) {
            log.severe("Ошибка getColor()" + e);
        }
        return elements;
    }

    public int getColorId(String name) throws SQLException {
        String sql = " SELECT "
                + "     ID "
                + " FROM  "
                + "     REFERENCE_COLOR "
                + " WHERE "
                + "     NAME LIKE ?";

        try (PreparedStatement ps= getConnection().prepareStatement(sql)) {

            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();

            if (rs.next())
                return rs.getInt("ID");

        } catch (Exception e) {
            log.severe("Ошибка getColorId()" + e);
        }
        return -1;
    }

    public Vector getAllModelsInMarshList(String fasNum, String date) {
        Vector elements = new Vector();
        String sql =
                "SELECT nsi_kld.ngpr as ngpr, nsi_kld.fas as fas, nsi_kld.sar as sar, "
                        + " nsi_kld.nar as nar, marh_list1.cw as cw, nsi_cd.ncw as ncw,"
                        + " sum(marh_list1.kol) as kol, REFERENCE_COLOR.ID as idColor,"
                        + " REFERENCE_COLOR.[NAME] as color "
                        + "FROM marh_list, "
                        + "      marh_list1, "
                        + "      nsi_cd, "
                        + "      kroy1, "
                        + "      nsi_kld, "
                        + "      REFERENCE_COLOR "
                        + "WHERE "
                        + "      marh_list.kod = marh_list1.kod_marh and "
                        + "      marh_list1.cw = nsi_cd.cw and "
                        + "      kroy1.kod_str = marh_list1.kod_kroy and     "
                        + "      nsi_cd.REF_COLOR_ID = REFERENCE_COLOR.ID and "
                        + "      nsi_kld.fas like ? and "
                        + "      marh_list.data > ? and "
                        + "      kroy1.kod_izd = nsi_kld.kod "
                        + "GROUP BY "
                        + "      nsi_kld.ngpr, "
                        + "      nsi_kld.fas, "
                        + "      nsi_kld.sar, "
                        + "      nsi_kld.nar, "
                        + "      marh_list1.cw, "
                        + "      nsi_cd.ncw, "
                        + "      REFERENCE_COLOR.ID, "
                        + "      REFERENCE_COLOR.[NAME]  "
                        + "ORDER BY "
                        + "      nsi_kld.ngpr, "
                        + "      nsi_kld.fas, "
                        + "      nsi_kld.sar, "
                        + "      nsi_kld.nar, "
                        + "      marh_list1.cw, "
                        + "      nsi_cd.ncw, "
                        + "      REFERENCE_COLOR.ID, "
                        + "      REFERENCE_COLOR.[NAME]  ";

        try (PreparedStatement ps = getConnection().prepareStatement(sql)) {
            ps.setString(1, fasNum + "%");
            ps.setString(2, date);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Vector tmp = new Vector();
                tmp.add(rs.getInt("fas"));                                      // 0 - модель
                tmp.add(rs.getInt("sar"));                                      // 1 - шифр
                tmp.add(rs.getString("nar").trim().toUpperCase());              // 2 - артикул
                tmp.add(rs.getString("ngpr").trim());                           // 3 - название
                tmp.add(rs.getInt("cw"));                                       // 4 - ID цвет маршрута
                tmp.add(rs.getString("ncw").trim().toUpperCase());              // 5 - цвет маршрута
                tmp.add(rs.getInt("idColor"));                                  // 6 - ID цвет EAN
                tmp.add(rs.getString("color").trim().toUpperCase());            // 7 - цвет EAN
                tmp.add(rs.getInt("kol"));                                      // 8 - кол-во
                tmp.add("");                                                    // 9 - заявки с цветом
                tmp.add(0);                                                     // 10 - совпадение
                tmp.add(new Vector());                                          // 11 - массив id заявок
                elements.add(tmp);
            }

        } catch (Exception e) {
            log.severe("Ошибка getAllModelsInMarshList()" + e);
        }
        return elements;
    }

    public Vector getRstRzmModelsInMarshList(int fas,
                                             int sar,
                                             String nar,
                                             int idColor,
                                             String date) {
        Vector elements = new Vector();
        String sql = " SELECT DISTINCT "
                + "     kroy1.rst as rst ,"
                + "     kroy1.rzm as rzm  "
                + " FROM "
                + "     marh_list, "
                + "     marh_list1, "
                + "     nsi_cd, kroy1, "
                + "     nsi_kld "
                + " WHERE "
                + "     marh_list.kod = marh_list1.kod_marh and "
                + "     marh_list1.cw = nsi_cd.cw and "
                + "     marh_list1.cw = ? and "
                + "     marh_list.data > ? and "
                + "     nsi_kld.fas = ? and "
                + "     nsi_kld.nar like ? and "
                + "     kroy1.kod_str = marh_list1.kod_kroy and "
                + "     kroy1.kod_izd = nsi_kld.kod ";

        try (PreparedStatement ps = getConnection().prepareStatement(sql)) {
            ps.setInt(1, idColor);
            ps.setString(2, date);
            ps.setInt(3, fas);
            ps.setString(4, nar + "%");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Vector tmp = new Vector();
                tmp.add(rs.getInt("rst"));                                      // рост
                tmp.add(rs.getInt("rzm"));                                      // размер
                elements.add(tmp);
            }

        } catch (Exception e) {
            log.severe("Ошибка getRstRzmModelsInMarshList()" + e);
        }
        return elements;
    }

    public boolean updateColor(int id, int idOld) {
        String sql = " UPDATE "
                + "     nsi_cd "
                + " SET "
                + "     REF_COLOR_ID = ? "
                + " WHERE "
                + "     cw = ? ";

        try (PreparedStatement ps = getConnection().prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.setInt(2, idOld);
            ps.execute();
            return true;

        } catch (Exception e) {
            log.severe("Ошибка setEanImport()" + e);
            return false;
        }
    }

    public Vector getDetalMarshList(int fas, int sar, String nar, int idColor, String date) {
        Vector elements = new Vector();
        String sql =
                "SELECT DISTINCT marh_list.nomer as nomer, marh_list.data as date, "
                        + " brigadir, sar, nsi_kld.fas as fas, ngpr, nar, marh_list1.cw as cw, "
                        + " ncw, rst, rzm, marh_list.kol as kol_all, marh_list1.kol as kol,"
                        + " kroy.shifrp as shifrp "
                        + "FROM marh_list, marh_list1, nsi_cd, kroy1, kroy, nsi_kld "
                        + "WHERE "
                        + " marh_list.kod = marh_list1.kod_marh and "
                        + " marh_list1.cw = nsi_cd.cw and "
                        + " kroy1.kod_str = marh_list1.kod_kroy and "
                        + " nsi_kld.fas = ? and "
                        + " nsi_kld.sar = ? and "
                        + " nsi_kld.nar like ? and "
                        + " marh_list1.cw = ? and "
                        + " marh_list.data > ? and "
                        + " kroy1.kod_izd = nsi_kld.kod and "
                        + " kroy.kod = dbo.kroy1.kod "
                        + "ORDER BY marh_list.nomer, rst, rzm ";

        try (PreparedStatement ps = getConnection().prepareStatement(sql)) {
            ps.setInt(1, fas);
            ps.setInt(2, sar);
            ps.setString(3, nar + "%");
            ps.setInt(4, idColor);
            ps.setString(5, date);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Vector tmp = new Vector();
                tmp.add(false);
                tmp.add(rs.getObject("nomer"));                                 // 1
                tmp.add(rs.getObject("date"));                                  // 2
                tmp.add(rs.getString("brigadir").trim());                       // 3
                tmp.add(rs.getInt("fas"));                                      // 4 модель
                tmp.add(rs.getString("nar").trim().toUpperCase());              // 5 артикул
                tmp.add(rs.getString("ncw").trim().toUpperCase());              // 6 цвет маршрута
                tmp.add(rs.getInt("kol_all"));                                  // 9
                tmp.add(rs.getInt("rst"));                                      // 7 рост
                tmp.add(rs.getInt("rzm"));                                      // 8
                tmp.add(rs.getInt("kol"));                                      // 10 кол-во
                tmp.add(rs.getString("shifrp").trim().toUpperCase());           // 11
                elements.add(tmp);
            }
            rs.close();

        } catch (Exception e) {
            log.severe("Ошибка getDetalMarshList()" + e);
        }
        return elements;
    }

    public Vector getEanByDataMrshList(long date) {
        Vector elements = new Vector();
        String sql = "    SELECT  "
                + "      ngpr, "
                + "      fas, "
                + "      nar, "
                + "      sort, "
                + "      ncw, "
                + "      color, "
                + "      kolvo, "
                + "      rst, "
                + "      rzm, "
                + "      rzm_print, "
                + "      ean,  "
                + "      NSI_EANCODE.EANCODE as EANCODE, "
                + "      sar, "
                + "      cw, "
                + "      idColor "
                + " FROM( "
                + "        SELECT "
                + "            nsi_kld.ngpr as ngpr, "
                + "            nsi_kld.fas as fas, "
                + "            nsi_kld.sar as sar, "
                + "            nsi_kld.nar as nar, "
                + "            marh_list1.cw as cw, "
                + "            nsi_cd.ncw as ncw, "
                + "            sum(marh_list1.kol) as kolvo, "
                + "            REFERENCE_COLOR.ID as idColor, "
                + "            REFERENCE_COLOR.[NAME] as color, "
                + "            kroy1.kod_izd as kod_izd, "
                + "            kroy1.rst as rst, "
                + "            kroy1.rzm as rzm, "
                + "            nsi_sd.kod1 as kod_rzm, "
                + "            nsi_sd.srt as sort, "
                + "            nsi_sd.rzm_print as rzm_print, "
                + "            nsi_sd.ean as ean "
                + "        FROM "
                + "            marh_list, "
                + "            marh_list1, "
                + "            nsi_cd, "
                + "            kroy1, "
                + "            nsi_kld, "
                + "            nsi_sd, "
                + "            REFERENCE_COLOR "
                + "        WHERE "
                + "            marh_list.data > ? and "
                + "            marh_list.kod = marh_list1.kod_marh and "
                + "            marh_list1.cw = nsi_cd.cw and "
                + "            marh_list1.kod_kroy = kroy1.kod_str and   "
                + "            nsi_cd.REF_COLOR_ID = REFERENCE_COLOR.ID and  "
                + "            kroy1.kod_izd = nsi_kld.kod and "
                + "            nsi_sd.kod = nsi_kld.kod and "
                + "            nsi_sd.rst = kroy1.rst and "
                + "            nsi_sd.rzm = kroy1.rzm "
                + "        GROUP BY "
                + "            nsi_kld.ngpr, "
                + "            nsi_kld.fas, "
                + "            nsi_kld.sar, "
                + "            nsi_kld.nar, "
                + "            marh_list1.cw,   "
                + "            nsi_cd.ncw, "
                + "            REFERENCE_COLOR.ID, "
                + "            REFERENCE_COLOR.[NAME], "
                + "            kroy1.kod_izd, "
                + "            kroy1.rst, "
                + "            kroy1.rzm, "
                + "            nsi_sd.kod1, "
                + "            nsi_sd.srt,"
                + "            nsi_sd.rzm_print, "
                + "            nsi_sd.ean ) as t1 "
                + " LEFT JOIN NSI_EANCODE on dbo.NSI_EANCODE.ITEM_CODE = t1.kod_rzm and "
                + "                          dbo.NSI_EANCODE.REF_COLOR_ID <> 2 and "
                + "                          dbo.NSI_EANCODE.REF_COLOR_ID = t1.idColor "
                + " ORDER BY "
                + "      ngpr, "
                + "      fas, "
                + "      sar, "
                + "      nar, "
                + "      cw, "
                + "      ncw, "
                + "      idColor, "
                + "      color, "
                + "      kod_izd, "
                + "      sort, "
                + "      rst, "
                + "      rzm, "
                + "      rzm_print ";

        try (PreparedStatement ps = getConnection().prepareStatement(sql)) {
            ps.setDate(1, new java.sql.Date(date));
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Vector tmp = new Vector();
                tmp.add(false);
                tmp.add(rs.getString("ngpr").trim().toUpperCase());             // 1 наименование
                tmp.add(rs.getInt("fas"));                                      // 2 модель
                tmp.add(rs.getString("nar").trim().toUpperCase());              // 3 артикул
                tmp.add(rs.getInt("sort"));                                     // 4 сорт
                tmp.add(rs.getString("ncw").trim().toUpperCase());              // 5 цвет из м/л
                tmp.add(rs.getString("color").trim().toUpperCase());            // 6 цвет группы
                tmp.add(rs.getInt("sort") != 1 ? "" : rs.getInt("kolvo"));            // 7 кол-во
                tmp.add(rs.getInt("rst"));                                      // 8 рост
                tmp.add(rs.getInt("rzm"));                                      // 9 размер
                tmp.add(rs.getObject("rzm_print") == null ? "" : rs.getObject("rzm_print").toString().trim()); // 10 размер для печати
                tmp.add(rs.getObject("ean") == null ? "" : rs.getObject("ean").toString().trim());             // 11 EAN из классификатора без цвета
                tmp.add(rs.getObject("EANCODE") == null ? "" : rs.getObject("EANCODE").toString().trim());     // 12 EAN с цветом
                tmp.add("");                                                    // 13 EAN из заявки
                tmp.add(rs.getInt("sar"));                                      // 14 шифр
                tmp.add(rs.getInt("cw"));                                       // 15 id цвета из м/л
                tmp.add(rs.getInt("idColor"));                                  // 16 id цвета группы
                tmp.add("");                                                    // 17 id заявки на получение EAN
                elements.add(tmp);
            }

        } catch (Exception e) {
            log.severe("Ошибка getEanByDataMrshList()" + e);
        }
        return elements;
    }

    public String getEncodeWithColor(int fas, String nar,
                                     int srt, int rst,
                                     int rzm, int idColor) {
        String eancode = null;
        String sql = "  Select "
                + "     NSI_EANCODE.EANCODE as EAN "
                + " From "
                + "     nsi_sd, nsi_kld, NSI_EANCODE "
                + "  Where "
                + "     nsi_kld.kod = nsi_sd.kod and "
                + "     nsi_sd.kod1 = NSI_EANCODE.ITEM_CODE and "
                + "     nar like upper(?) and "
                + "     fas = ? and "
                + "     srt = ? and "
                + "     rst = ? and "
                + "     rzm = ?  and "
                + "     REF_COLOR_ID = ? ";
        try (PreparedStatement ps = getConnection().prepareStatement(sql)) {
            ps.setString(1, nar.toUpperCase().replace("C", "_")
                    .replace("С", "_"));
            ps.setInt(2, fas);
            ps.setInt(3, srt);
            ps.setInt(4, rst);
            ps.setInt(5, rzm);
            ps.setInt(6, idColor);
            ResultSet rs = ps.executeQuery();

            if (rs.next())
                return rs.getString("EAN").trim();

            rs.close();
        } catch (Exception e) {
            log.severe("Ошибка getEancodeWithColor() " + e);
        }
        return eancode;
    }

    public EanItemListSize getWeight(EanItem eanItem,
                                     EanItemListSize eanItemListSize) {
        String sql = "Select DISTINCT dbo.nsi_sd.massa"
                + "   FROM [Gomel].[dbo].[nsi_kld], [Gomel].[dbo].[nsi_sd] "
                + "   where dbo.nsi_kld.kod = dbo.nsi_sd.kod" +
                " and [Gomel].[dbo].[nsi_kld].[fas] = " + eanItem.getFasNum() +
                " AND [Gomel].[dbo].[nsi_kld].[nar] like " + "'" + eanItem.getFasNar() + "%'" +
                " and [dbo].[nsi_sd].[rst] = " + eanItemListSize.getRst() +
                " AND [dbo].[nsi_sd].[rzm] = " + eanItemListSize.getRzm();
        try (PreparedStatement ps = getConnection().prepareStatement(sql);
             ResultSet rs = ps.executeQuery();) {
            if (rs.next())
                eanItemListSize.setWeight(rs.getString("massa").trim());

        } catch (Exception e) {
            log.severe("Ошибка getWeight() " + e);
        }
        return eanItemListSize;
    }
}
