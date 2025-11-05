package by.march8.ecs.application.modules.warehouse.external.shipping.dao;

import by.gomel.freedev.ucframework.ucdao.jdbc.AbstractMSSQLServerJDBC;
import by.march8.api.utils.DateUtils;
import by.march8.ecs.MainController;
import by.march8.ecs.framework.common.BackgroundTask;
import by.march8.entities.warehouse.SaleDocument;
import by.march8.entities.warehouse.SaleDocumentItem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.logging.Logger;

/**
 * @author Andy 27.04.2017.
 */
public class NativeDocumentUpdater extends AbstractMSSQLServerJDBC implements DocumentUpdater {

    private static final Logger log = Logger.getLogger(NativeDocumentUpdater.class.getName());

    private static final String SQL_UPDATE_DOCUMENT_ITEM = "UPDATE otgruz1 SET " +
            "[date]=?," +
            "ndoc=?," +
            "kpodot=?," +
            "kpl=?," +
            "summa=?," +
            "summa_nds=?," +
            "summa_all=?," +
            "skidka=?," +
            "kola=?," +
            "kolk=?," +
            "kolr=?," +
            "status=?," +
            "ucenka3s=?," +
            "datevrkv=?," +
            "uservrkv=?," +
            "kpodvrkv=?," +
            "datekrkv=?," +
            "userkrkv=?," +
            "kpodkrkv=?," +
            "klient_id=?," +
            "vid_sert=?," +
            "vid_ggr=?," +
            "operac=?," +
            "export=?," +
            "valuta_id=?," +
            "kurs=?," +
            "skidka_tip=?," +
            "kurs_bank=?," +
            "nalogi=?," +
            "dogovor_id=?," +
            "prepayment=?," +
            "sale_date=?," +
            "trade_mark_type=?," +
            "trade_mark_value=?," +
            "vat_value=?," +
            "nosort=?," +
            "prepayment_date=?," +
            "calculation_factor=?," +
            "saved=?" +
            //"new_price=?" +
            " WHERE item_id = ?";

    private static final String GET_SQL_UPDATE_DOCUMENT_DETAIL_ITEM = "UPDATE otgruz2 SET " +
            "scan=?," +
            "part=?," +
            "kol=?," +
            "cena=?," +
            "summa=?," +
            "nds=?," +
            "summa_nds=?," +
            "itogo=?," +
            "cenav=?," +
            "summav=?," +
            "summa_ndsv=?," +
            "itogov=?," +
            "kol_in_upack=?," +
            "tip=?," +
            "time_ins=?," +
            "pc_ins=?," +
            "eancode=?," +
            "doc_id=?," +
            "kod_str=?," +
            "preiscur=?," +
            "rzm_marh=?," +
            "rst_marh=?," +
            "kod_izd=?," +
            "ncw=?," +
            "srt=?," +
            "cena_uch=?" +
            " WHERE item_id =?";

    private boolean updateSaleDocumentItem(final SaleDocument document) {
        try (PreparedStatement ps = getConnection().prepareStatement(SQL_UPDATE_DOCUMENT_ITEM)) {

            ps.setDate(1, DateUtils.getDateAsSQLDate(document.getDocumentDate()));
            ps.setString(2, document.getDocumentNumber());
            ps.setInt(3, document.getSenderCode());
            ps.setInt(4, document.getRecipientCode());

            ps.setDouble(5, document.getValueSumCost());
            ps.setDouble(6, document.getValueSumVat());
            ps.setDouble(7, document.getValueSumCostAndVat());

            ps.setFloat(8, document.getDiscountValue());

            ps.setInt(9, document.getAmountAll());
            ps.setInt(10, document.getAmountPack());
            ps.setInt(11, document.getAmountNotPack());

            ps.setInt(12, document.getDocumentStatus());

            ps.setFloat(13, document.getPriceReduction3Grade());

            ps.setDate(14, DateUtils.getDateAsSQLDate(document.getCreateRecordDate()));
            ps.setString(15, document.getCreateRecordUser());
            ps.setString(16, document.getCreateRecordCode());

            ps.setDate(17, DateUtils.getDateAsSQLDate(document.getEditRecordDate()));
            ps.setString(18, document.getEditRecordUser());
            ps.setString(19, document.getEditRecordCode());

            ps.setInt(20, document.getRecipientId());

            ps.setInt(21, document.getCertificateId());
            ps.setInt(22, document.getCertificateGGRId());

            ps.setString(23, document.getDocumentType());
            ps.setInt(24, document.getDocumentExport());

            ps.setInt(25, document.getCurrencyId());
            ps.setFloat(26, document.getCurrencyRateFixed());
            ps.setFloat(27, document.getDiscountType());
            ps.setFloat(28, document.getCurrencyRateSale());

            ps.setInt(29, document.getDocumentVATType());

            ps.setInt(30, document.getRecipientContractId());
            ps.setBoolean(31, document.isPrepayment());

            ps.setDate(32, DateUtils.getDateAsSQLDate(document.getDocumentSaleDate()));
            ps.setInt(33, document.getTradeMarkType());
            ps.setFloat(34, document.getValueTradeMarkup());

            ps.setFloat(35, document.getDocumentVatValue());
            ps.setBoolean(36, document.isNotVarietal());
            ps.setDate(37, DateUtils.getDateAsSQLDate(document.getPrepaymentDate()));
            ps.setFloat(38, document.getCalculationFactor());
            ps.setBoolean(39, true);

            ps.setInt(40, document.getId());

            ps.executeUpdate();
            return true;
        } catch (Exception e) {
            log.severe("Ошибка в updateSaleDocumentItem: ");
        }
        return false;
    }

    private boolean updateSaleDocumentDetailItem(final SaleDocument document) {
        List<SaleDocumentItem> list = document.getDetailList();
        if (list == null || list.isEmpty()) {
            return false;
        }

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(GET_SQL_UPDATE_DOCUMENT_DETAIL_ITEM)) {
            conn.setAutoCommit(false);
            for (SaleDocumentItem item : list) {
                ps.setLong(1, item.getItemScanCode());
                ps.setInt(2, item.getPartNumber());
                ps.setInt(3, item.getAmount());

                ps.setDouble(4, item.getValuePrice());
                ps.setDouble(6, item.getValueVAT());

                ps.setDouble(5, item.getValueSumCost());
                ps.setDouble(7, item.getValueSumVat());
                ps.setDouble(8, item.getValueSumCostAndVat());

                ps.setDouble(9, item.getValuePriceCurrency());
                ps.setDouble(10, item.getValueSumCostCurrency());
                ps.setDouble(11, item.getValueSumVatCurrency());
                ps.setDouble(12, item.getValueSumCostAndVatCurrency());

                ps.setInt(13, item.getAmountInPack());
                ps.setInt(14, item.getItemType());

                ps.setDate(15, DateUtils.getDateAsSQLDate(item.getSystemDate()));
                ps.setString(16, item.getDiscount());

                ps.setString(17, item.getItemEanCode());
                ps.setInt(18, item.getDocument().getId());
                ps.setInt(19, item.getItemLabelId());

                ps.setString(20, item.getItemPriceList());

                ps.setInt(21, item.getItemSize());
                ps.setInt(22, item.getItemGrowth());
                ps.setInt(23, item.getItem());

                ps.setString(24, item.getItemColor());
                ps.setInt(25, item.getItemGrade());

                ps.setDouble(26, item.getValuePriceForAccounting());

                ps.setInt(27, item.getId());

                ps.addBatch();
            }
            ps.executeBatch();
            conn.commit();

            return true;
        } catch (Exception e) {
            log.severe("Ошибка в updateSaleDocumentItem: " + e.getMessage());
        }
        return false;
    }

    @Override
    public boolean updateDocument(final SaleDocument document) {
        if (document == null) {
            return false;
        } else {
            if (document.getDetailList() == null) {
                return false;
            }
        }

        class Task extends BackgroundTask {
            public Task(final String messageText) {
                super(messageText);
            }

            @Override
            protected Object doInBackground() throws Exception {
                return updateSaleDocumentItem(document) && updateSaleDocumentDetailItem(document);
            }
        }

        Task task = new Task("Сохранение данных...");
        task.executeTask();
        try {
            return (boolean) task.getResultObject();
        } catch (Exception e) {
            MainController.exception(e, "Ошибка сохраниения документа данных ");
            return false;
        }
    }
}
