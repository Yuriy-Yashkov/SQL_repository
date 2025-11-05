package by.march8.ecs.application.modules.warehouse.external.shipping.dao;

import by.gomel.freedev.ucframework.ucdao.jdbc.AbstractMSSQLServerJDBC;
import by.march8.ecs.MainController;
import by.march8.ecs.framework.common.BackgroundTask;
import by.march8.entities.warehouse.SaleDocument;
import by.march8.entities.warehouse.SaleDocumentItem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import java.util.logging.Logger;

/**
 * @author Andy 22.11.2016.
 */
public class DatabaseDocumentSQLUpdater extends AbstractMSSQLServerJDBC implements DocumentUpdater {

    private static final Logger log = Logger.getLogger(DatabaseDocumentSQLUpdater.class.getName());

    private static final String SQL_UPDATE_OTGRUZ_1 = "UPDATE otgruz1 SET " +
            "kola=?, " +                                         //1
            "kolr=?, " +                                         //2
            "kolk=?, " +                                         //3
            "sale_date=?, " +                                    //4
            "kurs=?, " +                                         //5
            "kurs_bank=?, " +                                    //6
            "vat_value=?, " +                                    //7
            "summa=?, " +                                        //8
            "summa_nds=?, " +                                    //9
            "summa_all=? " +                                     //10
            "WHERE item_id=? ";                                  //11

    private static final String SQL_UPDATE_OTGRUZ_2 = "UPDATE otgruz2 SET " +
            "nds=?, " +                                          //1
            "cena=?, " +                                         //2
            "summa=?, " +                                        //3
            "summa_nds=?, " +                                    //4
            "itogo=?, " +                                        //5
            "cenav=?, " +                                        //6
            "summav=?, " +                                       //7
            "summa_ndsv=?, " +                                   //8
            "itogov=?, " +                                       //9
            "cena_uch=? " +                                     //10
            "WHERE item_id=?";                                  //11

    private static final String SQL_UPDATE_ADDITION = "UPDATE _otgruz2_addition SET " +
            "torg_nadb=?, " +
            "rozn_cena=? " +
            "WHERE id_item=?";

    private static final String SQL_INSERT_ADDITION = "INSERT INTO _otgruz2_addition " +
            "(id_item, torg_nadb, rozn_cena) " +
            "VALUES(?, ?, ?)";

    private static final String SQL_DELETE_ADDITION = "DELETE FROM _otgruz2_addition " +
            "WHERE id_item=? ";

    private static final String SQL_SELECT_ADDITION = "SELECT torg_nadb FROM _otgruz2_addition " +
            "WHERE id_item=?";


    private boolean update(final SaleDocument document) {

        List<SaleDocumentItem> detailList = document.getDetailList();
        try (Connection conn = getConnection()) {
            conn.setAutoCommit(false);
            for (SaleDocumentItem item : detailList) {
                PreparedStatement ps = conn.prepareStatement(SQL_SELECT_ADDITION);
                ps.setInt(1, item.getId());
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    if (item.getTradeMarkupItem() != null) {
                        ps = conn.prepareStatement(SQL_UPDATE_ADDITION);
                        ps.setInt(3, item.getId());
                        ps.setInt(1, (int) item.getTradeMarkupItem().getValueTradeMarkup());
                        ps.setDouble(2, item.getTradeMarkupItem().getMarkAndPriceValue());
                        ps.execute();
                    } else {
                        ps = conn.prepareStatement(SQL_DELETE_ADDITION);
                        ps.setInt(1, item.getId());
                        ps.execute();
                    }
                } else {
                    if (item.getTradeMarkupItem() != null) {
                        ps = conn.prepareStatement(SQL_INSERT_ADDITION);
                        ps.setInt(1, item.getId());
                        ps.setInt(2, (int) item.getTradeMarkupItem().getValueTradeMarkup());
                        ps.setDouble(3, item.getTradeMarkupItem().getMarkAndPriceValue());
                        ps.execute();
                    }

                }
            }

            PreparedStatement ps = conn.prepareStatement(SQL_UPDATE_OTGRUZ_2);
            for (SaleDocumentItem item : detailList) {
                ps.setDouble(1, item.getValueVat());
                ps.setDouble(2, item.getValuePrice());
                ps.setDouble(3, item.getValueSumCost());
                ps.setDouble(4, item.getValueSumVat());
                ps.setDouble(5, item.getValueSumCostAndVat());
                ps.setDouble(6, item.getValuePriceCurrency());
                ps.setDouble(7, item.getValueSumCostCurrency());
                ps.setDouble(8, item.getValueSumVatCurrency());
                ps.setDouble(9, item.getValueSumCostAndVatCurrency());
                ps.setDouble(10, item.getValuePriceForAccounting());
                ps.setInt(11, item.getId());
                ps.addBatch();
            }
            ps.executeBatch();

            ps = conn.prepareStatement(SQL_UPDATE_OTGRUZ_1);

            ps.setInt(1, document.getAmountAll());
            ps.setInt(2, document.getAmountPack());
            ps.setInt(3, document.getAmountNotPack());
            ps.setDate(4, new java.sql.Date(document.getDocumentSaleDate().getDate()));
            ps.setDouble(5, document.getCurrencyRateFixed());
            ps.setDouble(6, document.getCurrencyRateSale());
            ps.setInt(7, (int) document.getDocumentVatValue());
            ps.setDouble(8, document.getValueSumCost());
            ps.setDouble(9, document.getValueSumVat());
            ps.setDouble(10, document.getValueSumCostAndVat());
            ps.setInt(11, document.getId());
            ps.execute();

            conn.commit();
        } catch (Exception e) {
          log.severe("Error in function update: " + e.getMessage());
        }

        return false;
    }


    @Override
    public boolean updateDocument(final SaleDocument document) throws Exception {

        class Task extends BackgroundTask {
            public Task(final String messageText) {
                super(messageText);
            }

            @Override
            protected Object doInBackground() throws Exception {
                update(document);
                return true;
            }
        }

        Task task = new Task("Сохранение данных...");

        try {
            task.executeTask();
        } catch (Exception e) {
            MainController.exception(e, "Ошибка сохраниения данных для " + document.getClass());
        }

        return true;
    }
}
