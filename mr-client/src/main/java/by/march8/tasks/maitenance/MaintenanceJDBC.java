package by.march8.tasks.maitenance;

import by.gomel.freedev.ucframework.ucdao.jdbc.AbstractMSSQLServerJDBC;
import by.march8.tasks.maitenance.doubling.EAN13Entity;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class MaintenanceJDBC extends AbstractMSSQLServerJDBC {

    private static final Logger log = Logger.getLogger(MaintenanceJDBC.class.getName());

    public List<EAN13Entity> getEan13List() {
        String query = "select ID,ITEM_CODE, " +
                "REF_COLOR_ID, " +
                "NSI_EANCODE.EANCODE, " +
                "INSERT_DATE, " +
                "ARTICLE_ID, " +
                "ARTICLE_NUMBER, " +
                "GRADE, " +
                "SIZE, " +
                "GROWTH  " +
                "FROM NSI_EANCODE " +
                "LEFT JOIN dbo.VIEW_NSI_CLASSIFIER ON dbo.NSI_EANCODE.ITEM_CODE = dbo.VIEW_NSI_CLASSIFIER.ITEM_ID " +
                "ORDER BY NSI_EANCODE.EANCODE, ITEM_CODE ";
        List<EAN13Entity> result = new ArrayList<>();

        try (PreparedStatement ps = getConnection().prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                EAN13Entity item = new EAN13Entity();
                item.setId(rs.getInt("ID"));
                item.setProductId(rs.getInt("ITEM_CODE"));

                item.setColorId(rs.getInt("REF_COLOR_ID"));
                item.setEancode(rs.getString("EANCODE"));
                item.setDate(rs.getDate("INSERT_DATE"));

                item.setArticleId(rs.getInt("ARTICLE_ID"));
                item.setArticleNumber(rs.getString("ARTICLE_NUMBER"));
                item.setGrade(rs.getInt("GRADE"));
                item.setSize(rs.getInt("SIZE"));
                item.setGrowth(rs.getInt("GROWTH"));

                result.add(item);
            }

        } catch (Exception ex) {
            log.severe("Ошибка при выполнении функции getCurrencyPriceByDocumentId :" + ex.getMessage());
        }
        return result;
    }
}

