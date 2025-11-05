package dept.marketing.report;

import by.gomel.freedev.ucframework.ucdao.jdbc.AbstractMSSQLServerJDBC;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Vector;
import java.util.logging.Logger;

/**
 *
 * @author lidashka
 */
public class ReportDB extends AbstractMSSQLServerJDBC {

    private static final Logger log = Logger.getLogger(ReportDB.class.getName());

    public Vector getGroupOtgruzRR(Vector data, boolean flagChul) throws Exception {
        Vector rezalt = new Vector();

        String sql = "IF (not object_id('tempdb..#tmp_otgruz_kt') IS NULL) DROP TABLE #tmp_otgruz_kt "
                + "CREATE TABLE #tmp_otgruz_kt ( "
                + "  fas NUMERIC(15),"
                + "  sar NUMERIC(15),"
                + "  t_1 DECIMAL(15),"
                + "  m_1 DECIMAL(15,3),"
                + "  t_2 DECIMAL(15),"
                + "  m_2 DECIMAL(15,3),"
                + "  t_3 DECIMAL(15),"
                + "  m_3 DECIMAL(15,3),"
                + "  t_4 DECIMAL(15),"
                + "  m_4 DECIMAL(15,3),"
                + "  t_5 DECIMAL(15),"
                + "  m_5 DECIMAL(15,3),"
                + "  t_6 DECIMAL(15),"
                + "  m_6 DECIMAL(15,3),"
                + "  t_itog DECIMAL(15),"
                + "  m_itog DECIMAL(15,3));";

        String sql1 = "INSERT INTO "
                + " #tmp_otgruz_kt(fas,sar,t_1,m_1,t_2,m_2,t_3,m_3,t_4,m_4,t_5,m_5,t_6,m_6,t_itog,m_itog)"
                + " VALUES( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";

        String sql2 = "Select nsi_pkd.ptk, nsi_pkd.nptk,"
                + "     sum(t_1),sum(m_1), "
                + "     sum(t_2),sum(m_2), "
                + "     sum(t_3),sum(m_3), "
                + "     sum(t_4),sum(m_4), "
                + "     sum(t_5),sum(m_5), "
                + "     sum(t_6),sum(m_6), "
                + "     sum(t_itog),sum(m_itog) "
                + " From #tmp_otgruz_kt, nsi_pkd, nsi_kld "
                + " Where nsi_kld.fas = #tmp_otgruz_kt.fas and "
                + "       nsi_kld.sar = #tmp_otgruz_kt.sar and "
                + (flagChul ? " #tmp_otgruz_kt.sar like '43______' and "
                : " #tmp_otgruz_kt.sar not like '43______' and ")
                + "       nsi_kld.ptk = nsi_pkd.ptk "
                + " Group by nsi_pkd.nptk, nsi_pkd.ptk "
                + " Order by nsi_pkd.nptk, nsi_pkd.ptk ";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             PreparedStatement ps1 = conn.prepareStatement(sql1);
             PreparedStatement ps2 = conn.prepareStatement(sql2);) {
            conn.setAutoCommit(false);

            ps.executeUpdate();

            for (Object datum : data) {
                Vector element = (Vector) datum;
                ps1.setInt(1, Integer.parseInt(element.get(0).toString()));
                ps1.setInt(2, Integer.parseInt(element.get(1).toString()));
                ps1.setDouble(3, Double.parseDouble(element.get(2).toString()));
                ps1.setDouble(4, Double.parseDouble(element.get(3).toString()));
                ps1.setDouble(5, Double.parseDouble(element.get(4).toString()));
                ps1.setDouble(6, Double.parseDouble(element.get(5).toString()));
                ps1.setDouble(7, Double.parseDouble(element.get(6).toString()));
                ps1.setDouble(8, Double.parseDouble(element.get(7).toString()));
                ps1.setDouble(9, Double.parseDouble(element.get(8).toString()));
                ps1.setDouble(10, Double.parseDouble(element.get(9).toString()));
                ps1.setDouble(11, Double.parseDouble(element.get(10).toString()));
                ps1.setDouble(12, Double.parseDouble(element.get(11).toString()));
                ps1.setDouble(13, Double.parseDouble(element.get(12).toString()));
                ps1.setDouble(14, Double.parseDouble(element.get(13).toString()));
                ps1.setDouble(15, Double.parseDouble(element.get(14).toString()));
                ps1.setDouble(16, Double.parseDouble(element.get(15).toString()));
                ps1.addBatch(sql);
            }
            ps1.executeBatch();

            ResultSet rs = ps2.executeQuery(sql);

            while (rs.next()) {
                Vector tmp = new Vector();
                tmp.add(rs.getInt(1));
                tmp.add(rs.getString(2).trim());
                tmp.add(BigDecimal.valueOf(rs.getDouble(3)).setScale(3, RoundingMode.HALF_UP).doubleValue());
                tmp.add(BigDecimal.valueOf(rs.getDouble(4)).setScale(3, RoundingMode.HALF_UP).doubleValue());
                tmp.add(BigDecimal.valueOf(rs.getDouble(5)).setScale(3, RoundingMode.HALF_UP).doubleValue());
                tmp.add(BigDecimal.valueOf(rs.getDouble(6)).setScale(3, RoundingMode.HALF_UP).doubleValue());
                tmp.add(BigDecimal.valueOf(rs.getDouble(7)).setScale(3, RoundingMode.HALF_UP).doubleValue());
                tmp.add(BigDecimal.valueOf(rs.getDouble(8)).setScale(3, RoundingMode.HALF_UP).doubleValue());
                tmp.add(BigDecimal.valueOf(rs.getDouble(9)).setScale(3, RoundingMode.HALF_UP).doubleValue());
                tmp.add(BigDecimal.valueOf(rs.getDouble(10)).setScale(3, RoundingMode.HALF_UP).doubleValue());
                tmp.add(BigDecimal.valueOf(rs.getDouble(11)).setScale(3, RoundingMode.HALF_UP).doubleValue());
                tmp.add(BigDecimal.valueOf(rs.getDouble(12)).setScale(3, RoundingMode.HALF_UP).doubleValue());
                tmp.add(BigDecimal.valueOf(rs.getDouble(13)).setScale(3, RoundingMode.HALF_UP).doubleValue());
                tmp.add(BigDecimal.valueOf(rs.getDouble(14)).setScale(3, RoundingMode.HALF_UP).doubleValue());
                tmp.add(BigDecimal.valueOf(rs.getDouble(15)).setScale(3, RoundingMode.HALF_UP).doubleValue());
                tmp.add(BigDecimal.valueOf(rs.getDouble(16)).setScale(3, RoundingMode.HALF_UP).doubleValue());
                rezalt.add(tmp);
            }

            conn.commit();
        } catch (Exception e) {
            log.severe("Ошибка getGroupOtgruzRR()" + e.getMessage());
            throw new Exception("Ошибка getGroupOtgruzRR() " + e.getMessage(), e);
        }
        return rezalt;
    }

    public Vector getGroupVolumeOtgruz(Vector data, boolean flagChul) throws Exception {
        Vector rezalt = new Vector();
        String sql = "IF (not object_id('tempdb..#tmp_otgruz_volume') IS NULL) DROP TABLE #tmp_otgruz_volume "
                + "CREATE TABLE #tmp_otgruz_volume ( "
                + "  fas NUMERIC(15),"
                + "  sar NUMERIC(15),"
                + "  volume_rb DECIMAL(15),"
                + "  volume_ex DECIMAL(15),"
                + "  volume_itogo DECIMAL(15),"
                + "  volume_pr DECIMAL(15),"
                + "  sum_rb DECIMAL(15,3),"
                + "  sum_ex DECIMAL(15,3),"
                + "  sum_itogo DECIMAL(15,3),"
                + "  sum_pr DECIMAL(15,3));";

        String sql1 = "INSERT INTO "
                + " #tmp_otgruz_volume(fas, sar, volume_rb, volume_ex, volume_itogo, volume_pr, sum_rb, sum_ex, sum_itogo, sum_pr)"
                + " VALUES( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";

        String sql2 = "Select nsi_pkd.ptk, nsi_pkd.nptk,"
                + "     sum(volume_rb),"
                + "     sum(volume_ex), "
                + "     sum(volume_itogo), "
                + "     sum(volume_pr), "
                + "     sum(sum_rb), "
                + "     sum(sum_ex), "
                + "     sum(sum_itogo), "
                + "     sum(sum_pr) "
                + " From #tmp_otgruz_volume, nsi_pkd, nsi_kld "
                + " Where nsi_kld.fas = #tmp_otgruz_volume.fas and "
                + "       nsi_kld.sar = #tmp_otgruz_volume.sar and "
                + (flagChul ? " #tmp_otgruz_volume.sar like '43______' and "
                : " #tmp_otgruz_volume.sar not like '43______' and ")
                + "       nsi_kld.ptk = nsi_pkd.ptk "
                + " Group by nsi_pkd.nptk, nsi_pkd.ptk "
                + " Order by nsi_pkd.nptk, nsi_pkd.ptk ";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             PreparedStatement ps1 = conn.prepareStatement(sql1);
             PreparedStatement ps2 = conn.prepareStatement(sql2);) {
            conn.setAutoCommit(false);

            ps.executeUpdate();

            for (Object datum : data) {
                Vector element = (Vector) datum;
                ps1.setInt(1, Integer.parseInt(element.get(0).toString()));
                ps1.setInt(2, Integer.parseInt(element.get(1).toString()));
                ps1.setDouble(3, Double.parseDouble(element.get(2).toString()));
                ps1.setDouble(4, Double.parseDouble(element.get(3).toString()));
                ps1.setDouble(5, Double.parseDouble(element.get(4).toString()));
                ps1.setDouble(6, Double.parseDouble(element.get(5).toString()));
                ps1.setDouble(7, Double.parseDouble(element.get(6).toString()));
                ps1.setDouble(8, Double.parseDouble(element.get(7).toString()));
                ps1.setDouble(9, Double.parseDouble(element.get(8).toString()));
                ps1.setDouble(10, Double.parseDouble(element.get(9).toString()));
                ps1.addBatch();
            }
            ps1.executeBatch();

            ResultSet rs = ps2.executeQuery(sql);

            while (rs.next()) {
                Vector tmp = new Vector();
                tmp.add(rs.getInt(1));
                tmp.add(rs.getString(2).trim());
                tmp.add(BigDecimal.valueOf(rs.getDouble(3)).setScale(3, RoundingMode.HALF_UP).doubleValue());
                tmp.add(BigDecimal.valueOf(rs.getDouble(4)).setScale(3, RoundingMode.HALF_UP).doubleValue());
                tmp.add(BigDecimal.valueOf(rs.getDouble(5)).setScale(3, RoundingMode.HALF_UP).doubleValue());
                tmp.add(rs.getDouble(5) > 0 ? BigDecimal.valueOf(((rs.getDouble(4)) * 100) / (rs.getDouble(5))).setScale(3, RoundingMode.HALF_UP).doubleValue() : 0);
                tmp.add(BigDecimal.valueOf(rs.getDouble(7)).setScale(3, RoundingMode.HALF_UP).doubleValue());
                tmp.add(BigDecimal.valueOf(rs.getDouble(8)).setScale(3, RoundingMode.HALF_UP).doubleValue());
                tmp.add(BigDecimal.valueOf(rs.getDouble(9)).setScale(3, RoundingMode.HALF_UP).doubleValue());
                tmp.add(rs.getDouble(9) > 0 ? BigDecimal.valueOf(((rs.getDouble(8)) * 100) / (rs.getDouble(9))).setScale(3, RoundingMode.HALF_UP).doubleValue() : 0);
                rezalt.add(tmp);
            }

            conn.commit();
        } catch (Exception e) {
            log.severe("Ошибка getGroupVolumeOtgruz()" + e.getMessage());
            throw new Exception("Ошибка getGroupVolumeOtgruz() " + e.getMessage(), e);
        }
        return rezalt;
    }

    public Vector getModelsGroupVolumeOtgruz(int idPtk, boolean flagChul) {
        Vector rezalt = new Vector();

        String sql = "Select nsi_kld.fas, nsi_kld.sar, nsi_kld.nar, nsi_kld.ngpr,"
                + "          volume_rb, volume_ex, volume_itogo, volume_pr,"
                + "          sum_rb, sum_ex, sum_itogo, sum_pr "
                + " From #tmp_otgruz_volume, nsi_pkd, nsi_kld "
                + " Where nsi_kld.fas = #tmp_otgruz_volume.fas and "
                + "       nsi_kld.sar = #tmp_otgruz_volume.sar and "
                + (flagChul ? " #tmp_otgruz_volume.sar like '43______' and "
                : " #tmp_otgruz_volume.sar not like '43______' and ")
                + "       nsi_kld.ptk = nsi_pkd.ptk and "
                + "       nsi_pkd.ptk = " + idPtk + " "
                + " Order by nsi_kld.fas, nsi_kld.sar";

        try (PreparedStatement ps = getConnection().prepareStatement(sql);
             ResultSet rs = ps.executeQuery();) {
            while (rs.next()) {
                Vector tmp = new Vector();
                tmp.add(rs.getInt(1));
                tmp.add(rs.getInt(2));
                tmp.add(rs.getString(3).trim());
                tmp.add(rs.getString(4).trim());
                tmp.add(BigDecimal.valueOf(rs.getDouble(5)).setScale(3, RoundingMode.HALF_UP).doubleValue());
                tmp.add(BigDecimal.valueOf(rs.getDouble(6)).setScale(3, RoundingMode.HALF_UP).doubleValue());
                tmp.add(BigDecimal.valueOf(rs.getDouble(7)).setScale(3, RoundingMode.HALF_UP).doubleValue());
                tmp.add(rs.getDouble(7) > 0 ? BigDecimal.valueOf(((rs.getDouble(6)) * 100) / (rs.getDouble(7))).setScale(3, RoundingMode.HALF_UP).doubleValue() : 0);
                tmp.add(BigDecimal.valueOf(rs.getDouble(9)).setScale(3, RoundingMode.HALF_UP).doubleValue());
                tmp.add(BigDecimal.valueOf(rs.getDouble(10)).setScale(3, RoundingMode.HALF_UP).doubleValue());
                tmp.add(BigDecimal.valueOf(rs.getDouble(11)).setScale(3, RoundingMode.HALF_UP).doubleValue());
                tmp.add(rs.getDouble(11) > 0 ? BigDecimal.valueOf(((rs.getDouble(10)) * 100) / (rs.getDouble(11))).setScale(3, RoundingMode.HALF_UP).doubleValue() : 0);
                rezalt.add(tmp);
            }

        } catch (Exception e) {
            log.severe("Ошибка getModelsGroupVolumeOtgruz()" + e.getMessage());
        }
        return rezalt;
    }

    public Vector getModelsGroupAnalizRR(int idPtk, boolean flagChul) {
        Vector rezalt = new Vector();

        String sql = "Select nsi_kld.fas, nsi_kld.sar, nsi_kld.nar, nsi_kld.ngpr,"
                + "          t_1,m_1,t_2,m_2,t_3,m_3,t_4,m_4,t_5,m_5,t_6,m_6,t_itog,m_itog"
                + " From #tmp_otgruz_kt, nsi_pkd, nsi_kld "
                + " Where nsi_kld.fas = #tmp_otgruz_kt.fas and "
                + "       nsi_kld.sar = #tmp_otgruz_kt.sar and "
                + (flagChul ? " #tmp_otgruz_kt.sar like '43______' and "
                : " #tmp_otgruz_kt.sar not like '43______' and ")
                + "       nsi_kld.ptk = nsi_pkd.ptk and "
                + "       nsi_pkd.ptk = " + idPtk + " "
                + " Order by nsi_kld.fas, nsi_kld.sar";

        try (PreparedStatement ps = getConnection().prepareStatement(sql);
             ResultSet rs = ps.executeQuery();) {

            while (rs.next()) {
                Vector tmp = new Vector();
                tmp.add(rs.getInt(1));
                tmp.add(rs.getInt(2));
                tmp.add(rs.getString(3).trim());
                tmp.add(rs.getString(4).trim());
                tmp.add(BigDecimal.valueOf(rs.getDouble(5)).setScale(3, RoundingMode.HALF_UP).doubleValue());
                tmp.add(BigDecimal.valueOf(rs.getDouble(6)).setScale(3, RoundingMode.HALF_UP).doubleValue());
                tmp.add(BigDecimal.valueOf(rs.getDouble(7)).setScale(3, RoundingMode.HALF_UP).doubleValue());
                tmp.add(BigDecimal.valueOf(rs.getDouble(8)).setScale(3, RoundingMode.HALF_UP).doubleValue());
                tmp.add(BigDecimal.valueOf(rs.getDouble(9)).setScale(3, RoundingMode.HALF_UP).doubleValue());
                tmp.add(BigDecimal.valueOf(rs.getDouble(10)).setScale(3, RoundingMode.HALF_UP).doubleValue());
                tmp.add(BigDecimal.valueOf(rs.getDouble(11)).setScale(3, RoundingMode.HALF_UP).doubleValue());
                tmp.add(BigDecimal.valueOf(rs.getDouble(12)).setScale(3, RoundingMode.HALF_UP).doubleValue());
                tmp.add(BigDecimal.valueOf(rs.getDouble(13)).setScale(3, RoundingMode.HALF_UP).doubleValue());
                tmp.add(BigDecimal.valueOf(rs.getDouble(14)).setScale(3, RoundingMode.HALF_UP).doubleValue());
                tmp.add(BigDecimal.valueOf(rs.getDouble(15)).setScale(3, RoundingMode.HALF_UP).doubleValue());
                tmp.add(BigDecimal.valueOf(rs.getDouble(16)).setScale(3, RoundingMode.HALF_UP).doubleValue());
                tmp.add(BigDecimal.valueOf(rs.getDouble(17)).setScale(3, RoundingMode.HALF_UP).doubleValue());
                tmp.add(BigDecimal.valueOf(rs.getDouble(18)).setScale(3, RoundingMode.HALF_UP).doubleValue());
                rezalt.add(tmp);
            }

        } catch (Exception e) {
            log.severe("Ошибка getModelsGroupVolumeOtgruz()" + e.getMessage());
        }
        return rezalt;
    }

    public Vector getGroupOtgruzClient(Vector data) {
        Vector rezalt = new Vector<>();

        String sql = "IF (not object_id('tempdb..#tmp_otgruz_client') IS NULL) DROP TABLE #tmp_otgruz_client "
                + " CREATE TABLE #tmp_otgruz_client ( "
                + "  sar NUMERIC(15),"
                + "  fas NUMERIC(15),"
                + "  volume DECIMAL(15),"
                + "  summ DECIMAL(15,3))";

        String sql1 = "INSERT INTO "
                + " #tmp_otgruz_client(sar, fas, volume, summ)"
                + " VALUES( ?, ?,?, ?);";

        String sql2 = "Select   nsi_pkd.ptk, " +
                "       nsi_pkd.nptk,"
                + "     sum(volume),"
                + "     sum(summ) "
                + " From #tmp_otgruz_client, nsi_pkd, nsi_kld "
                + " Where nsi_kld.fas = #tmp_otgruz_client.fas and "
                + "       nsi_kld.sar = #tmp_otgruz_client.sar and "
                + "       nsi_kld.ptk = nsi_pkd.ptk "
                + " Group by nsi_pkd.nptk, nsi_pkd.ptk "
                + " Order by nsi_pkd.nptk, nsi_pkd.ptk ";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             PreparedStatement ps1 = conn.prepareStatement(sql1);
             PreparedStatement ps2 = conn.prepareStatement(sql2);) {
            conn.setAutoCommit(false);
            ps.execute(sql);

            for (Object datum : data) {
                Vector element = (Vector) datum;
                ps1.setInt(1, Integer.parseInt(element.get(0).toString()));
                ps1.setInt(2, Integer.parseInt(element.get(1).toString()));
                ps1.setDouble(3, Double.parseDouble(element.get(2).toString()));
                ps1.setDouble(4, Double.parseDouble(element.get(3).toString()));
                ps1.addBatch(sql);
            }
            ps1.executeBatch();

            ResultSet rs = ps2.executeQuery();

            while (rs.next()) {
                Vector tmp = new Vector();
                tmp.add(rs.getObject(1));
                tmp.add(rs.getString(2).trim());
                tmp.add(rs.getDouble(3));
                tmp.add(rs.getDouble(4));
                rezalt.add(tmp);
            }

            conn.commit();
        } catch (Exception e) {
            log.severe("Ошибка getGroupOtgruzClient()" + e.getMessage());
        }
        return rezalt;
    }
}
