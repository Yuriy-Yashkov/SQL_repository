package dept.sbit.protocol;

import by.march8.ecs.framework.common.LogCrutch;
import workDB.DB_new;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author lidashka
 */
public class ProtocolDB extends DB_new {

    private static final LogCrutch log = new LogCrutch();

    public static double round(double value) {
        BigDecimal decimal = new BigDecimal(String.valueOf(value));
        return decimal.setScale(10, BigDecimal.ROUND_HALF_UP).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    public Vector getInfoModelProduct(Integer fas, String dataNar, boolean checkEan, int discontValue) throws Exception {
        Vector elements = new Vector();
        String sql = "";
        int k = 0;
        boolean addition = true;
        double discountPrice;

        try {
            dataNar = dataNar.replace(" ", "").replace(" ", "").replace(" ", "").replace(".", ",").replace(".", ",").trim();

            for (String nar : dataNar.split(",")) {
                if (!nar.equals("")) {
                    sql = "SELECT DISTINCT " +
                            "    ean, EANCODE, REFERENCE_COLOR.[NAME] AS COLOR, ngpr, nar, fas, rst, rzm, rzm_print, narp, cno, nds, cnr  "
                            + "     FROM nsi_kld "
                            + "            INNER JOIN nsi_sd ON nsi_sd.kod = nsi_kld.kod "
                            + "            INNER JOIN NSI_EANCODE ON dbo.NSI_EANCODE.ITEM_CODE = dbo.nsi_sd.kod1 "
                            + "            INNER JOIN REFERENCE_COLOR ON  dbo.NSI_EANCODE.REF_COLOR_ID = dbo.REFERENCE_COLOR.ID "
                            + "     WHERE nsi_kld.fas = ? AND "
                            + "           nsi_kld.nar like '" + nar.toUpperCase().replace("С", "_").replace("C", "_") + "' AND "
                            + "           nsi_sd.srt = 1 AND "
                            + "           nsi_sd.cno >0 "
                            + "     ORDER BY  ngpr, fas, nar, rst, rzm, rzm_print, COLOR, EANCODE ";

                    ps = conn.prepareStatement(sql);
                    ps.setInt(1, fas);
                    rs = ps.executeQuery();

                    Vector tmp = new Vector();
                    Set<String> eanSet = new HashSet<>();

                    while (rs.next()) {
                        addition = true;
                        String ean = String.valueOf(rs.getObject("EANCODE"));
                        if (!eanSet.contains(ean)) {
                            eanSet.add(ean);
                            tmp = new Vector();
                            tmp.add(rs.getInt("fas"));
                            tmp.add(rs.getString("nar").trim().toUpperCase());
                            tmp.add(++k);
                            tmp.add(rs.getObject("EANCODE") == null ? "" : rs.getObject("EANCODE"));
                            tmp.add(rs.getString("ngpr").trim().toUpperCase() + " " + rs.getString("nar").trim().toUpperCase() + "\n"
                                    + "Модель: " + rs.getInt("fas") + "\n"
                                    + "Размер: " + (rs.getObject("rzm_print") == null ? "" : rs.getObject("rzm_print").toString().trim()) + " "
                                    + rs.getString("COLOR").trim().toUpperCase());
                            tmp.add("ОАО \"8 Марта\"");
                            tmp.add("РБ");
                            tmp.add(rs.getObject("narp"));
                            tmp.add("шт.");
                            tmp.add(rs.getDouble("cno"));
                            tmp.add(discontValue);

                            discountPrice = round(rs.getDouble("cno") - (rs.getDouble("cno") * discontValue / 100));
                            tmp.add(discountPrice);

                            tmp.add(rs.getInt("nds"));
                            tmp.add(round(discountPrice * (1 + rs.getDouble("nds") / 100)));

                            if (checkEan && tmp.get(3).toString().trim().equals(""))
                                addition = false;

                            if (addition)
                                elements.add(tmp);
                        }
                    }

                } else
                    UtilProtocol.ERROR_MESSAGE += "\nмодель " + fas + " артикул -   ";
            }

        } catch (Exception e) {
            elements = new Vector();
            System.err.println("Ошибка getInfoModelProduct() " + e);
            log.error("Ошибка getInfoModelProduct()", e);
            throw new Exception("Ошибка getInfoModelProduct() " + e.getMessage(), e);
        }
        return elements;
    }

    public Vector getDetalInfoModelProduct(Integer fas, String dataNar, boolean checkEan, int discontValue) throws Exception {
        Vector elements = new Vector();
        String sql = "";
        int k = 0;
        boolean addition = true;
        double discountPrice;

        try {
            dataNar = dataNar.replace(" ", "").replace(" ", "").replace(" ", "").replace(".", ",").replace(".", ",").trim();

            for (String nar : dataNar.split(",")) {
                if (!nar.equals("")) {
                    sql = "SELECT DISTINCT " +
                            "    ean, EANCODE, REFERENCE_COLOR.[NAME] AS COLOR, ngpr, nar, fas, rst, rzm, rzm_print, narp, cno, nds, cnr  "
                            + "     FROM nsi_kld "
                            + "            INNER JOIN nsi_sd ON nsi_sd.kod = nsi_kld.kod "
                            + "            INNER JOIN NSI_EANCODE ON dbo.NSI_EANCODE.ITEM_CODE = dbo.nsi_sd.kod1 "
                            + "            INNER JOIN REFERENCE_COLOR ON  dbo.NSI_EANCODE.REF_COLOR_ID = dbo.REFERENCE_COLOR.ID "
                            + "     WHERE nsi_kld.fas = ? AND "
                            + "           nsi_kld.nar like '" + nar.toUpperCase().replace("С", "_").replace("C", "_") + "' AND "
                            + "           nsi_sd.srt = 1 AND "
                            + "           nsi_sd.cno >0 "
                            + "     ORDER BY  ngpr, fas, nar, rst, rzm, rzm_print, COLOR, EANCODE ";

                    ps = conn.prepareStatement(sql);
                    ps.setInt(1, fas);
                    rs = ps.executeQuery();

                    Vector tmp = new Vector();
                    Set<String> eanSet = new HashSet<>();

                    while (rs.next()) {
                        addition = true;
                        String ean = String.valueOf(rs.getObject("EANCODE"));
                        if (!eanSet.contains(ean)) {
                            eanSet.add(ean);

                            tmp = new Vector();
                            tmp.add(rs.getInt("fas"));
                            tmp.add(rs.getString("nar").trim().toUpperCase());
                            tmp.add(++k);
                            tmp.add(rs.getObject("EANCODE") == null ? "" : rs.getObject("EANCODE"));
                            tmp.add(rs.getString("ngpr").trim().toUpperCase());
                            tmp.add(rs.getInt("fas"));
                            tmp.add(rs.getString("nar").trim().toUpperCase());
                            tmp.add(rs.getString("COLOR").trim().toUpperCase());
                            tmp.add(rs.getObject("rzm_print") == null ? "" : rs.getObject("rzm_print").toString().trim());
                            tmp.add("ОАО \"8 Марта\"");
                            tmp.add("РБ");
                            tmp.add(rs.getObject("narp"));
                            tmp.add("шт.");
                            tmp.add(rs.getDouble("cno"));
                            tmp.add(discontValue);

                            discountPrice = round(rs.getDouble("cno") - (rs.getDouble("cno") * discontValue / 100));
                            tmp.add(discountPrice);

                            tmp.add(rs.getInt("nds"));
                            tmp.add(round(discountPrice * (1 + rs.getDouble("nds") / 100)));

                            if (checkEan && tmp.get(3).toString().trim().equals(""))
                                addition = false;

                            if (addition)
                                elements.add(tmp);
                        }
                    }

                } else
                    UtilProtocol.ERROR_MESSAGE += "\nмодель " + fas + " артикул -   ";
            }

        } catch (Exception e) {
            elements = new Vector();
            System.err.println("Ошибка getInfoModelProduct() " + e);
            log.error("Ошибка getInfoModelProduct()", e);
            throw new Exception("Ошибка getInfoModelProduct() " + e.getMessage(), e);
        }
        return elements;
    }

    AtomicInteger rowNumber = new AtomicInteger(16);

    public Vector getGreenDetailInfoModelProduct(Integer fas, String dataNar, boolean checkEan, int discontValue) throws Exception {
        Vector elements = new Vector();

        double discountPrice;


        dataNar = dataNar.replace(" ", "").replace(" ", "").replace(" ", "").replace(".", ",").replace(".", ",").trim();

        for (String nar : dataNar.split(",")) {
            if (!nar.isEmpty()) {
                String sql = "SELECT DISTINCT " +
                        "    ean, EANCODE, REFERENCE_COLOR.[NAME] AS COLOR, ngpr, nar, fas, rst, rzm, rzm_print, narp, cno, nds, cnr, nsi_sd.massa as massa, kkr  "
                        + "     FROM nsi_kld "
                        + "            INNER JOIN nsi_sd ON nsi_sd.kod = nsi_kld.kod "
                        + "            INNER JOIN NSI_EANCODE ON dbo.NSI_EANCODE.ITEM_CODE = dbo.nsi_sd.kod1 "
                        + "            INNER JOIN REFERENCE_COLOR ON  dbo.NSI_EANCODE.REF_COLOR_ID = dbo.REFERENCE_COLOR.ID "
                        + "     WHERE nsi_kld.fas = ? AND "
                        + "           nsi_kld.nar like '" + nar.toUpperCase().replace("С", "_").replace("C", "_") + "' AND "
                        + "           nsi_sd.srt = 1 AND "
                        + "           nsi_sd.cno >0 "
                        + "     ORDER BY  ngpr, fas, nar, COLOR, rst, rzm, EANCODE, rzm_print ";

                try (PreparedStatement ps = getConnection().prepareStatement(sql)) {
                    ps.setInt(1, fas);
                    rs = ps.executeQuery();

                    Map<Integer, Object> tmp;
                    Set<String> eanSet = new HashSet<>();
                    while (rs.next()) {
                        String ean = String.valueOf(rs.getObject("EANCODE"));
                        if (!eanSet.contains(ean)) {
                            eanSet.add(ean);

                            tmp = new LinkedHashMap<>();
                            ;
                            tmp.put(9, rs.getObject("EANCODE") == null ? "" : rs.getObject("EANCODE"));
                            tmp.put(10, String.format("=L%d&\" \"&O%d&\" \"&N%d", rowNumber.get(), rowNumber.get(), rowNumber.get()));
                            tmp.put(11, rs.getString("ngpr").trim().toUpperCase());
                            tmp.put(13, rs.getString("COLOR").trim().toUpperCase());
                            tmp.put(14, rs.getObject("rzm_print") == null ? "" : rs.getObject("rzm_print").toString().trim()
                                    + " " + rs.getString("fas").trim()
                                    + " " + rs.getString("nar").trim().toUpperCase());
                            tmp.put(15, rs.getObject("EANCODE") == null ? "" : rs.getObject("EANCODE"));
                            tmp.put(16, rs.getDouble("massa"));
                            tmp.put(17, "шт.");
                            tmp.put(18, "кг.");
                            tmp.put(19, "ОАО \"8 Марта\"");
                            tmp.put(20, "ОАО \"8 Марта\"");
                            tmp.put(21, "РБ");
                            tmp.put(22, "РБ");
                            tmp.put(24, "ОАО \"8 Марта\"");
                            tmp.put(25, rs.getInt("kkr"));
                            tmp.put(33, rs.getObject("narp"));
                            tmp.put(34, rs.getString("narp").substring(0, 4));
                            tmp.put(37, rs.getInt("nds"));
                            tmp.put(38, rs.getDouble("cno"));
                            tmp.put(39, discontValue);
                            discountPrice = round(rs.getDouble("cno") - (rs.getDouble("cno") * discontValue / 100));
                            tmp.put(40, discountPrice);
                            tmp.put(41, round(discountPrice * (1 + rs.getDouble("nds") / 100)));
                            if (checkEan && !tmp.get(9).toString().trim().isEmpty()) {
                                rowNumber.incrementAndGet();
                                elements.add(tmp);
                            }
                            if (!checkEan) {
                                rowNumber.incrementAndGet();
                                elements.add(tmp);
                            }
                        }
                    }
                } catch (Exception e) {
                    System.err.println("Ошибка getInfoModelProduct() " + e);
                    log.error("Ошибка getInfoModelProduct()", e);
                    throw new Exception("Ошибка getInfoModelProduct() " + e.getMessage(), e);
                }
            } else
                UtilProtocol.ERROR_MESSAGE += "\nмодель " + fas + " артикул -   ";
        }

        return elements;
    }

    public Vector getAllNarModels(String fas, String nar, String articleNar) throws Exception {
        Vector elements = new Vector();
        String sql = "";

        try {
            String expression = getSQLRequest(articleNar);

            sql = "Select distinct nar "
                    + "     From nsi_kld "
                    + "     Where fas like ? and  "
                    + "           fas > 0  "
                    + (expression.trim().length() > 0 ? " and " + expression : " ")
                    + "     Order by nar";

            ps = conn.prepareStatement(sql);
            ps.setString(1, fas + "%");
            rs = ps.executeQuery();
            while (rs.next()) {
                elements.add(rs.getString("nar").trim().toUpperCase());
            }

        } catch (Exception e) {
            System.err.println("Ошибка getAllNarModels() " + e);
            log.error("Ошибка getAllNarModels()", e);
            throw new Exception("Ошибка getAllNarModels() " + e.getMessage(), e);
        }
        return elements;
    }

    public Vector getNarModel(Integer sar, boolean checkEan, String articleNar) throws Exception {
        Vector elements = new Vector();
        String sql = "";

        try {
            String expression = getSQLRequest(articleNar);

            if (checkEan)
                sql = "Select distinct nar "
                        + "     From nsi_kld, nsi_sd "
                        + "     Where nsi_kld.kod = nsi_sd.kod  and " +
                        "             sar = ? and  "
                        + "           fas > 0 and "
                        + "           (nsi_sd.ean NOT LIKE '' or nsi_sd.ean <> NULL) "
                        + (expression.trim().length() > 0 ? " and " + expression : " ")
                        + "     Order by nar ";
            else
                sql = "Select distinct nar "
                        + "     From nsi_kld "
                        + "     Where sar = ? and  "
                        + "           fas > 0  "
                        + (expression.trim().length() > 0 ? " and " + expression : " ")
                        + "     Order by nar ";


            ps = conn.prepareStatement(sql);
            ps.setInt(1, sar);
            rs = ps.executeQuery();
            while (rs.next()) {
                if (!rs.getString("nar").trim().equals(""))
                    elements.add(rs.getString("nar").trim().toUpperCase());
            }

        } catch (Exception e) {
            System.err.println("Ошибка getNarModel() " + e);
            log.error("Ошибка getNarModel()", e);
            throw new Exception("Ошибка getNarModel() " + e.getMessage(), e);
        }
        return elements;
    }

    private String getSQLRequest(String article) {
        if (article != null) {
            String[] array = article.split(";");
            String sql = "";
            for (final String anArray : array) {
                sql += " nsi_kld.nar Like '" + anArray + "%' or ";
            }

            if (sql.length() > 10) {
                sql = sql.substring(0, sql.length() - 3);
                return " (" + sql + ") ";
            } else return "";
        } else {
            return "";
        }
    }
}
