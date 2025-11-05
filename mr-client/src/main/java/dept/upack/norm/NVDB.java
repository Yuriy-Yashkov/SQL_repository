package dept.upack.norm;

import by.gomel.freedev.ucframework.ucdao.jdbc.AbstractMSSQLServerJDBC;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.logging.Logger;

public class NVDB extends AbstractMSSQLServerJDBC {

    private static final Logger log = Logger.getLogger(NVDB.class.getName());

    public ResultSet getItemNV(String sd, String ed) {
        String query =
                "select ngpr, fas, razrad, norma, sum(kol)as kol, (sum(kol) * norma) as normTime  "
                        + "from (SELECT razrad, norma, kod_izd, kol  from nakl_sdacha where status = 1 and data >= ? and data < ?) as nakl "
                        + " left join (select fas, ngpr, kod from nsi_kld)as kld on kld.kod = nakl.kod_izd "
                        + " group by razrad, norma, fas, ngpr "
                        + " order by razrad, norma";
        try (PreparedStatement ps = getConnection().prepareStatement(query)) {
            ps.setString(1, sd);
            ps.setString(2, ed);
            return  ps.executeQuery();

        } catch (Exception e) {
            log.severe("Ошибка getItemNV(String sd, String ed) " + e);
        }
        return null;
    }

}
