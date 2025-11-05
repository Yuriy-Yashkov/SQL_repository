package dept.nsi;

import by.gomel.freedev.ucframework.ucdao.jdbc.AbstractMSSQLServerJDBC;
import common.ProgressBar;

import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.logging.Logger;


/**
 *
 * @author vova
 */
public class NsiDB extends AbstractMSSQLServerJDBC {
    private static final Logger log = Logger.getLogger(NsiDB.class.getName());
    PreparedStatement ps2;

    private static String prepareQuery(Map hm) {
        String endQuery = " ) and (sd.datevrkv>? and sd.datevrkv<?) "
                + " group by sar,nar,nds "
                + "order by sar asc ";

        String allCondition = "";

        String startQuery = " select sar,nds,nar from nsi_sd as sd "
                + " left join (select kod,rtrim(ltrim(str(sar))) as sar,nar from nsi_kld) as kl on sd.kod=kl.kod where ( ";

        String BelTrikVz = " ((left(sar,2)='41') and ((substring(sar,3,1)='1') or (substring(sar,3,1)='2') or"
                + " (substring(sar,3,1)='5')) and upper(left(ltrim(rtrim(nar)),1))<>'Б' and nds<>20) ";
        String VerhTrikVz = " ((left(sar,2)='42') and ((substring(sar,3,1)='1') or (substring(sar,3,1)='2') or "
                + " (substring(sar,3,1)='5')) and upper(left(ltrim(rtrim(nar)),1))<>'Б' and nds<>20) ";
        String ChulkiVz = "((left(sar,2)='43') and ((substring(sar,3,1)='1') or (substring(sar,3,1)='2') or (substring(sar,3,1)='5')) "
                + "and upper(left(ltrim(rtrim(nar)),1))<>'Б' and nds<>20) ";

        String BelTrikDet = " ((left(sar,2)='41') and ((substring(sar,3,1)='3') or (substring(sar,3,1)='6')) and upper(left(ltrim(rtrim(nar)),1))<>'Б' and nds<>10) ";
        String VerhTrikDet = " ((left(sar,2)='42') and ((substring(sar,3,1)='3') or (substring(sar,3,1)='6')) and upper(left(ltrim(rtrim(nar)),1))<>'Б' and nds<>10) ";
        String ChulkiDet = " ((left(sar,2)='43') and ((substring(sar,3,1)='3') or (substring(sar,3,1)='6')) and upper(left(ltrim(rtrim(nar)),1))<>'Б' and nds<>10) ";

        String Polotno = " ((substring(sar,1,2)='47' and substring(sar,3,1)<>'0') and upper(left(ltrim(rtrim(nar)),1))<>'Б' and nds<>20) ";
        String Eksport = " (substring(sar,5,2)='99' and nds<>0) ";
        String DavlSir = " ((substring(sar,5,1)='0' and left(sar,2)<>'47' and upper(left(ltrim(rtrim(nar)),1))<>'Б' and nds<>0) or (substring(sar,3,1)='0' and left(sar,2)='47' and upper(left(ltrim(rtrim(nar)),1))<>'Б' and nds<>20)) ";

        if (hm.get("cbBelTrikVz").toString().equals("1")) {
            if (allCondition.isEmpty()) {
                allCondition = allCondition + BelTrikVz;
            } else {
                allCondition = allCondition + " or " + BelTrikVz;
            }
        }
        if (hm.get("cbVerhTrikVz").toString().equals("1")) {
            if (allCondition.isEmpty()) {
                allCondition = allCondition + VerhTrikVz;
            } else {
                allCondition = allCondition + " or " + VerhTrikVz;
            }
        }
        if (hm.get("cbChulkiVz").toString().equals("1")) {
            if (allCondition.isEmpty()) {
                allCondition = allCondition + ChulkiVz;
            } else {
                allCondition = allCondition + " or " + ChulkiVz;
            }
        }

        if (hm.get("cbBelTrikDet").toString().equals("1")) {
            if (allCondition.isEmpty()) {
                allCondition = allCondition + BelTrikDet;
            } else {
                allCondition = allCondition + " or " + BelTrikDet;
            }
        }
        if (hm.get("cbVerhTrikDet").toString().equals("1")) {
            if (allCondition.isEmpty()) {
                allCondition = allCondition + VerhTrikDet;
            } else {
                allCondition = allCondition + " or " + VerhTrikDet;
            }
        }
        if (hm.get("cbChulkiDet").toString().equals("1")) {
            if (allCondition.isEmpty()) {
                allCondition = allCondition + ChulkiDet;
            } else {
                allCondition = allCondition + " or " + ChulkiDet;
            }
        }

        if (hm.get("cbPolotno").toString().equals("1")) {
            if (allCondition.isEmpty()) {
                allCondition = allCondition + Polotno;
            } else {
                allCondition = allCondition + " or " + Polotno;
            }
        }
        if (hm.get("cbEksport").toString().equals("1")) {
            if (allCondition.isEmpty()) {
                allCondition = allCondition + Eksport;
            } else {
                allCondition = allCondition + " or " + Eksport;
            }
        }
        if (hm.get("cbDavlSir").toString().equals("1")) {
            if (allCondition.isEmpty()) {
                allCondition = allCondition + DavlSir;
            } else {
                allCondition = allCondition + " or " + DavlSir;
            }
        }
        return startQuery + allCondition + endQuery;
    }

    public List getNSI_KLD() {
        List items = new ArrayList();
        String query = "select sar, nar, fas, ngpr from nsi_kld order by fas";
        try (PreparedStatement ps = getConnection().prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Vector v = new Vector();
                v.add(false);
                v.add(rs.getString(1).trim());
                v.add(rs.getString(2).trim());
                v.add(rs.getInt(3));
                v.add(rs.getString(4).trim());
                items.add(v);
            }
        } catch (Exception e) {
            log.severe("Ошибка getNSI_KLD() " + e);
        }
        return items;
    }

    public boolean delNSI_KLD(List elem, ProgressBar bp) {
        boolean f = true;
        String query = "select kod from nsi_kld where fas = ? and nar = ? and sar = ?";
        try (Connection conn = getConnection();
            PreparedStatement ps = getConnection().prepareStatement(query)) {
            conn.setAutoCommit(false);

            for (int i = 0; i < elem.size(); i++) {
                bp.setProgress(i + 1);
                ps.setInt(1, Integer.parseInt(((ArrayList) elem.get(i)).get(2).toString()));
                ps.setString(2, ((ArrayList) elem.get(i)).get(1).toString());
                ps.setString(3, ((ArrayList) elem.get(i)).get(0).toString());

                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    Savepoint sp = conn.setSavepoint();
                    try {
                        int kod = rs.getInt(1);

                        executeDelete(conn, "delete from upack where kod_izd = ?", kod);
                        executeDelete(conn, "delete from upack1 where barcode in (select barcode from label_one where kod_izd = ?)", kod);
                        executeDelete(conn, "delete from label_one where kod_izd = ?", kod);
                        executeDelete(conn, "delete from poshiv where kod_izd = ?", kod);
                        executeDelete(conn, "delete from marh_list1 where kod_kroy in (select kod_str from kroy1 where kod_izd = ?)", kod);
                        executeDelete(conn, "delete from kroy1 where kod_izd = ?", kod);
                        executeDelete(conn, "delete from nsi_kld where kod = ?", kod);

                    } catch (Exception e) {
                        conn.rollback(sp);
                        f = false;
                        log.severe("Ошибка при удалении изделия " + ((ArrayList) elem.get(i)).get(2).toString()
                                + ((ArrayList) elem.get(i)).get(1).toString() + e.getMessage());
                        JOptionPane.showMessageDialog(null, "Ошибка при удалении изделия "
                                + ((ArrayList) elem.get(i)).get(2).toString()
                                + ((ArrayList) elem.get(i)).get(1).toString() + e.getMessage(),
                                "Отчёт сформирован", JOptionPane.INFORMATION_MESSAGE);
                    }
                }

            }

            conn.commit();

        } catch (Exception e) {
            log.severe("Ошибка delNSI_KLD() " + e);
        }
        return f;
    }

    private void executeDelete(Connection conn, String sql, int kod) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, kod);
            ps.executeUpdate();
        }
    }

    public List getNSI_WKD() {
        List items = new ArrayList();
        String query = "select kod, sostav, naim from nsi_wkd order by kod";
        try (PreparedStatement ps = getConnection().prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Vector v = new Vector();
                v.add(false);
                v.add(rs.getLong(1));
                v.add(rs.getString(2).trim());
                v.add(rs.getString(3).trim());
                items.add(v);
            }
        } catch (Exception e) {
            log.severe("Ошибка getNSI_WKD() " + e);
        }
        return items;
    }

    public void addNsiWkd(String kod, String sostav, String naim) {
        String query = "INSERT INTO nsi_wkd " +
                "(kod, sostav, naim, datevrkv, uservrkv, kpodvrkv, datekrkv, kpodkrkv) " +
                "VALUES (?, ?, ?, '', '', '', '', '')";
        try (PreparedStatement ps = getConnection().prepareStatement(query)) {
            ps.setLong(1, Long.parseLong(kod));
            ps.setString(2, sostav);
            ps.setString(3, naim);
            ps.executeUpdate();
        } catch (Exception e) {
            log.severe("Ошибка addNsiWkd " + e);
        }
    }

    public boolean getEqualsKod(String kod) {
        boolean flag = false;
        String query = "select kod, sostav, naim from nsi_wkd where kod = ?";
        try (PreparedStatement ps = getConnection().prepareStatement(query)) {
            ps.setLong(1, Long.parseLong(kod));
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                flag = true;
            }
        } catch (Exception e) {
            log.severe("Ошибка getEqualsKod() " + e);
        }
        return flag;
    }

    public void updateNsiWkd(long kodNew, String sostavNew, String naimNew, long kodOld, String sostavOld, String naimOld) {
        String query = "UPDATE nsi_wkd SET kod = ?, sostav = ?, naim = ? WHERE (kod = ?) and (sostav = ?) and (naim = ?)";
        try (PreparedStatement ps = getConnection().prepareStatement(query)) {
            ps.setLong(1, kodNew);
            ps.setString(2, sostavNew);
            ps.setString(3, naimNew);
            ps.setLong(4, kodOld);
            ps.setString(5, sostavOld);
            ps.setString(6, naimOld);
            ps.executeUpdate();
        } catch (Exception e) {
            log.severe("Ошибка getEqualsKod() " + e);
        }
    }

    public boolean checkNDS(Map hm) {
        boolean flag = true;
        List<Map<String, Object>> arrayHash = new ArrayList<>();
        String startQuery = prepareQuery(hm);

        try (PreparedStatement ps = getConnection().prepareStatement(startQuery)) {
            ps.setDate(1, new java.sql.Date(Long.parseLong(hm.get("sDate").toString())));
            ps.setDate(2, new java.sql.Date(Long.parseLong(hm.get("eDate").toString())));
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Map<String, Object> hmm = new HashMap<>();
                hmm.put("sar", rs.getString(1));
                hmm.put("nar", rs.getString(3));
                hmm.put("nds", rs.getString(2));
                arrayHash.add(hmm);
                flag = false;
            }
            if (!flag) {
                ReporpNsi rn = new ReporpNsi(arrayHash);
                rn.checkNdsReport();
            }
        } catch (Exception ex) {
            log.severe("checkNDS(Map hm)" + ex.getMessage());
            flag = false;
        }
        return flag;
    }

}