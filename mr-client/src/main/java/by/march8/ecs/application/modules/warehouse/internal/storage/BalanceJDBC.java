package by.march8.ecs.application.modules.warehouse.internal.storage;

import by.gomel.freedev.ucframework.ucdao.jdbc.AbstractMSSQLServerJDBC;
import by.march8.entities.storage.BalanceItem;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class BalanceJDBC extends AbstractMSSQLServerJDBC {

    private static final Logger log = Logger.getLogger(BalanceJDBC.class.getName());

    public List<BalanceItem> getBalanceListByStorageDocumentId(int documentId) {

        String sql = "SELECT " +
                "ARTICLE_ID, " +
                "ITEM_NAME, " +
                "MODEL, " +
                "ARTICLE_NUMBER, " +
                "ARTICLE_CODE, " +
                "GRADE, " +
                "GROWTH, " +
                "SIZE, " +
                "PRICE, " +
                "PRICE_GRADE, " +
                "SUM(AMOUNT) AS AMOUNT " +
                "FROM " +
                "VIEW_PLACES_DOCUMENT WHERE DOCUMENT_ID = ? " +
                "GROUP BY ARTICLE_ID, ITEM_NAME, MODEL, ARTICLE_NUMBER, ARTICLE_CODE, GRADE, GROWTH, SIZE, PRICE, PRICE_GRADE " +
                "ORDER BY MODEL, ARTICLE_NUMBER, GRADE, [SIZE], GROWTH ";


        List<BalanceItem> result = new ArrayList<>();

        try (PreparedStatement ps = getConnection().prepareStatement(sql)) {
            ps.setInt(1, documentId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                BalanceItem item = new BalanceItem();
                item.setAccessTypeData(false);

                item.setArticleId(rs.getInt("ARTICLE_ID"));
                item.setItemName(rs.getString("ITEM_NAME"));
                item.setModelNumber(rs.getInt("MODEL"));
                item.setArticleNumber(rs.getString("ARTICLE_NUMBER"));
                item.setArticleCode(rs.getInt("ARTICLE_CODE"));

                item.setGrade(rs.getInt("GRADE"));
                item.setSize(rs.getInt("SIZE"));
                item.setGrowth(rs.getInt("GROWTH"));

                item.setPrice(rs.getDouble("PRICE"));
                item.setPriceGrade(rs.getDouble("PRICE_GRADE"));
                item.setAmount(rs.getInt("AMOUNT"));

                result.add(item);
            }
        } catch (Exception ex) {
            log.severe("Ошибка при выполнении функции getBalanceListByStorageDocumentId :" + ex.getMessage());
        }
        return result;
    }

    public List<BalanceItem> getBalanceAccessByMonth(int month) {
        String sql = "SELECT " +
                "nsi_kld.ngpr AS ITEM_NAME, " +
                "nsi_kld.nar AS ARTICLE_NUMBER, " +
                "[Модель] AS MODEL_NUMBER, " +
                "[Артикул] AS ARTICLE_CODE, " +
                "[Сорт] AS GRADE, " +
                "[Размер_н] AS MIN_SIZE, " +
                "[Размер_к]AS MAX_SIZE, " +
                "[Рост_н] AS MIN_GROWTH, " +
                "[Рост_к] AS MAX_GROWTH, " +
                "[Цена_ценник] AS PRICE, " +
                "SUM([Ост_кол]) AS AMOUNT, " +
                "SUM([Ост_сум_бн])AS COST " +
                "FROM [ОБОРОТ_ГОТ] " +
                "LEFT JOIN nsi_kld ON nsi_kld.sar = [Артикул] " +
                "WHERE [Месяц]=? AND [Артикул] < 47000000 " +
                "GROUP BY nsi_kld.ngpr,nsi_kld.nar,[Модель],[Артикул],[Сорт],[Размер_н],[Размер_к],[Рост_н],[Рост_к], [Цена_ценник] " +
                "ORDER BY nsi_kld.ngpr,nsi_kld.nar,[Модель],[Артикул],[Сорт],[Размер_н],[Размер_к],[Рост_н],[Рост_к], [Цена_ценник] ";


        List<BalanceItem> result = new ArrayList<>();

        try (PreparedStatement ps = getConnection().prepareStatement(sql)) {
            ps.setInt(1, month);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                BalanceItem item = new BalanceItem();
                item.setAccessTypeData(true);
                item.setItemName(rs.getString("ITEM_NAME"));
                item.setArticleNumber(rs.getString("ARTICLE_NUMBER"));
                item.setArticleCode(rs.getInt("ARTICLE_CODE"));
                item.setModelNumber(rs.getInt("MODEL_NUMBER"));

                item.setGrade(rs.getInt("GRADE"));
                item.setMinSize(rs.getInt("MIN_SIZE"));
                item.setMaxSize(rs.getInt("MAX_SIZE"));
                item.setMinGrowth(rs.getInt("MIN_GROWTH"));
                item.setMaxGrowth(rs.getInt("MAX_GROWTH"));

                item.setPrice(rs.getDouble("PRICE"));
                item.setCost(rs.getDouble("COST"));
                item.setAmount(rs.getInt("AMOUNT"));

                result.add(item);
            }
        } catch (Exception ex) {
            log.severe("Ошибка при выполнении функции getBalanceAccessByMonth :" + ex.getMessage());
        }
        return result;
    }
}
