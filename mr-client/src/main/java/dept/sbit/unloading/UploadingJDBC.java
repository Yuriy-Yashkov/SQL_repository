package dept.sbit.unloading;

import by.gomel.freedev.ucframework.ucdao.jdbc.AbstractMSSQLServerJDBC;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.logging.Logger;

/**
 * @author Andy 05.07.2017.
 */
public class UploadingJDBC extends AbstractMSSQLServerJDBC {

    private static final Logger log = Logger.getLogger(UploadingJDBC.class.getName());

    public NSIItem getTNVDCode(String ean) {
        String query = "SELECT sd.cno,sd.nds,sd.massa,kld.narp FROM nsi_sd AS sd " +
                "LEFT JOIN (SELECT kod, narp FROM nsi_kld) AS kld ON sd.kod= kld.kod  " +
                " WHERE sd.ean = ?";

        try (PreparedStatement ps = getConnection().prepareStatement(query)) {
            ps.setString(1, ean);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {

                NSIItem item = new NSIItem();

                if (rs.getString("narp") != null) {
                    item.setTnvdCode(rs.getString("narp"));
                    item.setPrice(rs.getDouble("cno") / 100);
                    item.setVat(rs.getDouble("nds"));
                    item.setWeight(rs.getDouble("massa"));
                } else {
                    item.setTnvdCode("");
                }

                return item;
            }

        } catch (Exception ex) {
            log.severe("Ошибка при выполнении функции getClassifierVatValue :" + ex.getMessage());
        }
        return null;
    }
}
