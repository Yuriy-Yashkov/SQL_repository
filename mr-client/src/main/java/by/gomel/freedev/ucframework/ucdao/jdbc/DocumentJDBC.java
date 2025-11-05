package by.gomel.freedev.ucframework.ucdao.jdbc;

import by.march8.ecs.application.modules.warehouse.external.shipping.model.ContractorChecker;
import by.march8.ecs.application.modules.warehouse.external.shipping.model.Reduction3Grade;
import by.march8.ecs.application.modules.warehouse.internal.displacement.ReductionPriceNSI;
import by.march8.ecs.framework.common.BackgroundTask;
import by.march8.entities.warehouse.SaleDocumentDetailItemReport;
import by.march8.entities.warehouse.SaleDocumentItemView;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class DocumentJDBC extends AbstractMSSQLServerJDBC {

    private static final Logger log = Logger.getLogger(DocumentJDBC.class.getName());

    public int documentIsExist(final String docNumber) {
        String query = "select item_id from otgruz1 WHERE ndoc = ?";
        try (PreparedStatement ps = getConnection().prepareStatement(query)) {
            ps.setString(1, docNumber);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int result = rs.getInt(1);
                if (result > 0) {
                    return result;
                }
            }
        } catch (Exception ex) {
            log.severe("Ошибка при выполнении функции getNoSortFlag :" + ex.getMessage());
        }
        return -1;
    }

    public List<SaleDocumentItemView> getSaleDocumentDetailViewList(final String number) {

        String query = "SELECT " +
                "dbo.otgruz2.ncw, " +
                "dbo.otgruz2.srt, " +
                "dbo.otgruz2.kol, " +
                "dbo.otgruz2.cena, " +
                "dbo.otgruz2.summa, " +
                "dbo.otgruz2.nds, " +
                "dbo.otgruz2.summa_nds, " +
                "dbo.otgruz2.itogo, " +
                "dbo.otgruz2.cenav, " +
                "dbo.otgruz2.summav, " +
                "dbo.otgruz2.summa_ndsv, " +
                "dbo.otgruz2.itogov, " +
                "dbo.otgruz2.kol_in_upack, " +
                "dbo.nsi_sd.cno, " +
                "dbo.nsi_kld.sar, " +
                "dbo.nsi_kld.nar, " +
                "dbo.nsi_kld.ngpr, " +
                "dbo.otgruz2.doc_id, " +
                "dbo.otgruz1.ndoc, " +
                "dbo.otgruz2.item_id, " +
                "dbo.otgruz2.cena_uch, " +
                "dbo.otgruz2.kod_izd, " +
                "dbo.otgruz2.eancode, " +
                "dbo.otgruz2.scan, " +
                "dbo.nsi_kld.fas, " +
                "case " +
                "    when dbo._otgruz2_addition.torg_nadb is NULL then 0 " +
                "    else dbo._otgruz2_addition.torg_nadb " +
                "    end as torg_nadb, " +
                "case " +
                "    when dbo._otgruz2_addition.rozn_cena is NULL then 0 " +
                "    else dbo._otgruz2_addition.rozn_cena " +
                "    end as rozn_cena " +
                "FROM " +
                "dbo.otgruz2 " +
                "INNER JOIN dbo.nsi_sd ON dbo.nsi_sd.kod1 = dbo.otgruz2.kod_izd " +
                "INNER JOIN dbo.nsi_kld ON dbo.nsi_kld.kod = dbo.nsi_sd.kod " +
                "INNER JOIN dbo.otgruz1 ON dbo.otgruz1.item_id = dbo.otgruz2.doc_id " +
                "LEFT OUTER JOIN dbo._otgruz2_addition ON dbo._otgruz2_addition.id_item = dbo.otgruz2.item_id " +
                "WHERE dbo.otgruz1.ndoc = ?";
        List<SaleDocumentItemView> result = new ArrayList<>();

        try (PreparedStatement ps = getConnection().prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                SaleDocumentItemView item = new SaleDocumentItemView();
/*

                item.setId(rs.getInt("item_id"));
                @Column(name = "doc_id")
                private int documentId;

                @Column(name = "ndoc")
                private String documentNumber;

                @Column(name = "kod_izd")
                private int itemId;
                @Column(name = "kol")
                private int amount;
                @Column(name = "kol_in_upack")
                private int amountInPack;
                @Column(name = "eancode")
                private String itemEanCode;
                @Column(name = "scan")
                private String itemScanCode;
                @Column(name = "ncw")
                private String itemColor;
                @Column(name = "srt")
                private int itemGrade;
                @Column(name = "cena_uch")
                private double valuePriceForAccounting;
                @Column(name = "nds")
                private float valueVAT;
                @Column(name = "cena")
                private double valuePrice;
                @Column(name = "summa")
                private double valueSumCost;
                @Column(name = "summa_nds")
                private double valueSumVat;
                @Column(name = "itogo")
                private double valueSumCostAndVat;
                @Column(name = "cenav")
                private double valuePriceCurrency;
                @Column(name = "summav")
                private double valueSumCostCurrency;
                @Column(name = "summa_ndsv")
                private double valueSumVatCurrency;
                @Column(name = "itogov")
                private double valueSumCostAndVatCurrency;
                @Column(name = "cno")
                private float priceWholesale;
                @Column(name = "ngpr")
                private String productName;
                @Column(name = "fas")
                private int modelNumber;
                @Column(name = "nar")
                private String articleName;
                @Column(name = "sar")
                private int articleCode;
                @Column(name = "torg_nadb")
                private float tradeMarkValue;
                @Column(name = "rozn_cena")
                private float retailPriceValue;
                */

            }
        } catch (Exception ex) {
            log.severe("Ошибка при выполнении функции getNoSortFlag :" + ex.getMessage());
        }

        return result;
    }

    public List<Reduction3Grade> getWholesalePriceByDocumentId(int documentId) {
        String query = "select item_id , cno  from otgruz2 o " +
                "INNER JOIN (select kod, kod1, cno, rzm, rst, srt as sort from nsi_sd ) " +
                "as n on n.kod = (SELECT kod from nsi_sd s where s.kod1 = o.kod_izd) " +
                "and n.rst = o.rst_marh " +
                "and n.rzm = o.rzm_marh " +
                "and sort = 1 " +
                "WHERE doc_id = ? ORDER BY cno";

        List<Reduction3Grade> result = new ArrayList<>();

        try (PreparedStatement ps = getConnection().prepareStatement(query)) {
            ps.setInt(1, documentId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Reduction3Grade item = new Reduction3Grade();
                item.setId(rs.getInt("item_id"));
                item.setPrice(rs.getDouble("cno"));
                item.setPriceCurrency(0.0);
                result.add(item);
            }
        } catch (Exception ex) {
            log.severe("Ошибка при выполнении функции getWholesalePriceByDocumentId :" + ex.getMessage());
        }
        return result;
    }

    public List<Reduction3Grade> getCurrencyPriceByDocumentId(int documentId) {
        String query = "select item_id , cnp, cno, kod_izd  from otgruz2 o " +
                "INNER JOIN (select kod, kod1, cno, cnp from nsi_sd) " +
                "             as n on n.kod1 = o.kod_izd " +
                "                WHERE doc_id = ? ORDER BY cno";

        List<Reduction3Grade> result = new ArrayList<>();

        try (PreparedStatement ps = getConnection().prepareStatement(query)) {
            ps.setInt(1, documentId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Reduction3Grade item = new Reduction3Grade();
                item.setId(rs.getInt("item_id"));
                item.setPrice(rs.getDouble("cno"));
                item.setPriceCurrency(rs.getDouble("cnp"));
                result.add(item);
            }

        } catch (Exception ex) {
            log.severe("Ошибка при выполнении функции getCurrencyPriceByDocumentId :" + ex.getMessage());
        }
        return result;
    }

    public List<ReductionPriceNSI> getNSIPriceByDocumentId(final int documentId) {

        String query = "select item_id , cno, o.cena, o.kol*o.kol_in_upack as amount, srt  from vnperem2 o \n" +
                "                INNER JOIN (select kod, kod1, cno, rzm, rst, srt as sort from nsi_sd ) \n" +
                "                as n on n.kod = (SELECT kod from nsi_sd s where s.kod1 = o.kod_izd) \n" +
                "                and n.rst = (SELECT rst from nsi_sd s2 where s2.kod1 = o.kod_izd)\n" +
                "                and n.rzm = (SELECT rzm from nsi_sd s3 where s3.kod1 = o.kod_izd)\n" +
                "                and sort = 1 \n" +
                "                WHERE doc_id = ? ORDER BY cno";

        List<ReductionPriceNSI> result = new ArrayList<>();

        try (PreparedStatement ps = getConnection().prepareStatement(query)) {
            ps.setInt(1, documentId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ReductionPriceNSI item = new ReductionPriceNSI();
                item.setItemId(rs.getInt("item_id"));
                item.setPrice1Grade(rs.getDouble("cno"));
                item.setAmount(rs.getInt("amount"));
                item.setGrade(rs.getInt("srt"));
                result.add(item);
            }

        } catch (Exception ex) {
            log.severe("Ошибка при выполнении функции getNSIPriceByDocumentId :" + ex.getMessage());
        }
        return result;

    }

    public void saveItemPrice3GradeByDocument(final List<ReductionPriceNSI> price) {

        class Task extends BackgroundTask {
            public Task(final String messageText) {
                super(messageText);
            }

            @Override
            protected Boolean doInBackground() throws Exception {
                String query = "update vnperem2 set cena=? where item_id=?";

                try (Connection conn = getConnection();
                     PreparedStatement ps = conn.prepareStatement(query);) {
                    conn.setAutoCommit(false);

                    for (ReductionPriceNSI item : price) {
                        ps.setDouble(1, item.getPrice3Grade());
                        ps.setInt(2, item.getItemId());
                        ps.addBatch();

                        // Очистка пакета каждые 100 элементов
                        if ((price.indexOf(item) + 1) % 100 == 0) {
                            ps.executeBatch();
                            ps.clearBatch();
                        }
                    }

                    // Выполнение оставшихся элементов
                    if (ps.getUpdateCount() > 0) {
                        ps.executeBatch();
                    }

                    conn.commit();
                    return true;

                } catch (Exception ex) {
                    log.severe("Ошибка при выполнении функции saveItemPrice3GradeByDocument: " +
                            ex.getMessage());
                }
                return false;
            }
        }

        Task task = new Task("Обновление цены...");
        task.executeTask();
    }

    public List<SaleDocumentDetailItemReport> getDataForFactura(String ttn) {
        List<SaleDocumentDetailItemReport> result = new ArrayList<>();

        String query = " select kld.sar,ngpr,nar, sum(kol) as pach,sum(kol_in_upack*kol) as kol,ot2.cenav ,sum(ot2.summav)as summa, "
                + " ot2.nds,sum(summa_ndsv) as summa_nds,sum(itogov) as summa_all,narp from otgruz1 as ot1 "
                + " left join otgruz2 as ot2 on ot1.item_id=ot2.doc_id "
                + " left join nsi_sd as sd on ot2.kod_izd=sd.kod1 "
                + " left join nsi_kld as kld on sd.kod=kld.kod "
                + " where ndoc=? "
                + " group by ngpr,prim,cenav,kld.sar,ot2.nds,narp,nar "
                + " order by kld.sar,nar ";
        try (PreparedStatement ps = getConnection().prepareStatement(query)) {
            ps.setString(1, ttn);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                SaleDocumentDetailItemReport newItem = new SaleDocumentDetailItemReport();

                newItem.setArticleCode(rs.getString("sar").trim());
                newItem.setItemName(rs.getString("ngpr").trim());
                newItem.setArticleNumber(rs.getString("nar").trim());
                result.add(newItem);
            }
        } catch (Exception ex) {
            log.severe("Error in method getDataForFactura()" + ex.getMessage());
        }
        return result;
    }

    public ContractorChecker getContractorCheckerByContractorId(final int contractorId) {
        ContractorChecker result = null;
        String query = "select KOD as CODE,NAIM as NAME, POSTADRES, URADRES, R_SCHET, DOGOVOR from s_klient WHERE ITEM_ID = ?";
        try (PreparedStatement ps = getConnection().prepareStatement(query)) {
            ps.setInt(1, contractorId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                result = new ContractorChecker();

                result.setContractorCode(rs.getInt("CODE"));
                result.setName(rs.getString("NAME"));
                result.setPostAddressId(rs.getInt("POSTADRES"));
                result.setLegalAddressId(rs.getInt("URADRES"));
                result.setContractId(rs.getInt("DOGOVOR"));
            }
        } catch (Exception ex) {
            log.severe("Ошибка при выполнении функции getContractorCheckerByContractorId :" + ex.getMessage());
        }
        return result;
    }
}
