package dept.sbit;

import by.march8.ecs.framework.common.LogCrutch;
import dept.sbit.model.SubTypeProduct;
import workDB.DB_new;

import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author Andy
 */
public class DataBaseTradeAllowance extends DB_new {

    /*private static final org.apache.log4j.Logger log = new Log()
            .getLoger(DB.class);
*/
    private static final LogCrutch log = new LogCrutch();

    public HashMap<String, String> getNowTradeAllowance() {
        HashMap<String, String> data = new HashMap<String, String>();
        try {
            String query = " SELECT 'vch',tn FROM _torg_nadbavka as tn "
                    + " WHERE klient_id != 0 and klient_id != 1316 and (art = '_31%') AND (sort = 1) "
                    + " union "
                    + " SELECT 'vt',tn FROM _torg_nadbavka as tn "
                    + " WHERE klient_id != 0 and klient_id != 1316 and (art = '__1%') AND (sort = 1) "
                    + " union "
                    + " SELECT 'dch',tn FROM _torg_nadbavka as tn "
                    + " WHERE klient_id != 0 and klient_id != 1316 and (art = '_33%') AND (sort = 1) "
                    + " union "
                    + " SELECT 'dt',tn FROM _torg_nadbavka as tn  "
                    + " WHERE klient_id != 0 and klient_id != 1316 and (art = '__3%') AND (sort = 1)";
            ps = conn.prepareStatement(query);
            rs = ps.executeQuery();
            while (rs.next()) {
                data.put(rs.getString(1), rs.getString(2));
            }
        } catch (Exception e) {
            System.err.println("Ошибка getNowTradeAllowance() " + e);
            log.error("Ошибка в DataBeseTradeAllowance getNowTradeAllowance()",
                    e);
        }
        return data;
    }

    public ArrayList<String> getCompanyStore() {
        ArrayList<String> data = new ArrayList<String>();
        try {
            String query = " SELECT klient_id FROM _torg_nadbavka as tn \n"
                    + " WHERE klient_id != '' and klient_id != 1316 and art in ('__1%','__2%','__3%','__5%','__6%') \n"
                    + " group by klient_id ";
            ps = conn.prepareStatement(query);
            rs = ps.executeQuery();
            while (rs.next()) {
                data.add(rs.getString(1));
            }
        } catch (Exception e) {
            System.err.println("Ошибка getCompanyStore() " + e);
            log.error("Ошибка в DataBeseTradeAllowance getCompanyStore()", e);
        }
        return data;
    }

    public void setAllowanceByClientId(int clientid, SubTypeProduct product) {
        // Запрос для трикотаж взрослый
        String sql = "update _torg_nadbavka set tn = ? where klient_id = ? and (sort=1 or sort=2) and "
                + "(art='_31%' or " + "art='_32%' or " + "art='_35%')";
        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(2, clientid);
            ps.setInt(1, product.getAllowanceKnitwearAdult());
            ps.executeUpdate();
        } catch (Exception e1) {
            System.out
                    .println("Ошибка сохранения торговой надбавки [Трикотаж - взрослый]");
        }
        // Запрос для чулочных взрослых
        sql = "update _torg_nadbavka set tn = ? where klient_id = ? and (sort=1 or sort=2) and "
                + "(art='__1%' or " + "art='__2%' or " + "art='__5%')";
        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(2, clientid);
            ps.setInt(1, product.getAllowanceStockingAdult());
            ps.executeUpdate();
        } catch (Exception e1) {
            System.out
                    .println("Ошибка сохранения торговой надбавки [Носочный - взрослый]");
        }
        // Запрос для трикотаж детский
        sql = "update _torg_nadbavka set tn = ? where klient_id = ? and (sort=1 or sort=2) and "
                + "(art='__3%' or " + "art='__6%')";
        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(2, clientid);
            ps.setInt(1, product.getAllowanceKnitwearBaby());
            ps.executeUpdate();
        } catch (Exception e1) {
            System.out
                    .println("Ошибка сохранения торговой надбавки [Трикотаж - детский]");
        }
        // Запрос для чулочных детский
        sql = "update _torg_nadbavka set tn = ? where klient_id = ? and (sort=1 or sort=2) and "
                + "(art='_33%' or art='_36%')";
        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(2, clientid);
            ps.setInt(1, product.getAllowanceStockingBaby());
            ps.executeUpdate();
        } catch (Exception e1) {
            System.out
                    .println("Ошибка сохранения торговой надбавки [Носочный - детский]");
        }
    }

    public SubTypeProduct getAllowanceByClientID(int clientid) {
        SubTypeProduct stp = new SubTypeProduct();

        // Запрос для трикотаж взрослый
        String sql = " SELECT top 1 tn FROM _torg_nadbavka as tn "
                + " WHERE klient_id = ? and (art = '__1%') AND (sort = 1) ";
        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, clientid);
            rs = ps.executeQuery();
            while (rs.next()) {
                if (!rs.getString(1).equals(""))
                    stp.setAllowanceKnitwearAdult(rs.getInt(1));
                else
                    stp.setAllowanceKnitwearAdult(0);
            }
        } catch (Exception e1) {
            System.out
                    .println("Ошибка получения торговой надбавки [Трикотаж - взрослый]");
        }

        // Запрос для чулочных взрослых
        sql = " SELECT top 1 tn FROM _torg_nadbavka as tn "
                + " WHERE klient_id = ? and (art = '_31%') AND (sort = 1) ";
        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, clientid);
            rs = ps.executeQuery();
            while (rs.next()) {
                if (!rs.getString(1).equals(""))
                    stp.setAllowanceStockingAdult(rs.getInt(1));
                else
                    stp.setAllowanceStockingAdult(0);
            }
        } catch (Exception e1) {
            System.out
                    .println("Ошибка получения торговой надбавки [Носочный - взрослый]");
        }
        // Запрос для трикотаж детский
        sql = " SELECT top 1 tn FROM _torg_nadbavka as tn "
                + " WHERE klient_id = ? and (art = '__3%') AND (sort = 1) ";
        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, clientid);
            rs = ps.executeQuery();
            while (rs.next()) {
                if (!rs.getString(1).equals(""))
                    stp.setAllowanceKnitwearBaby(rs.getInt(1));
                else
                    stp.setAllowanceKnitwearBaby(0);
            }
        } catch (Exception e1) {
            System.out
                    .println("Ошибка получения торговой надбавки [Трикотаж - детский]");
        }

        // Запрос для чулочных детский
        sql = " SELECT top 1 tn FROM _torg_nadbavka as tn "
                + " WHERE klient_id = ? and (art = '_33%') AND (sort = 1) ";
        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, clientid);
            rs = ps.executeQuery();
            while (rs.next()) {
                if (!rs.getString(1).equals(""))
                    stp.setAllowanceStockingBaby(rs.getInt(1));
                else
                    stp.setAllowanceStockingBaby(0);
            }
        } catch (Exception e1) {
            System.out
                    .println("Ошибка получения торговой надбавки [Носочный - детский]");
        }

        return stp;
    }

}
