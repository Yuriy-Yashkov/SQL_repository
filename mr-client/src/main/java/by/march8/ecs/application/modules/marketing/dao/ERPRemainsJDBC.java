package by.march8.ecs.application.modules.marketing.dao;

import by.gomel.freedev.ucframework.ucdao.jdbc.AbstractMSSQLServerJDBC;
import by.march8.ecs.application.modules.marketing.model.ERPFilter;
import by.march8.ecs.application.modules.marketing.model.ModelSelectionItem;
import by.march8.entities.warehouse.ERPRemainsEntity;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

public class ERPRemainsJDBC extends AbstractMSSQLServerJDBC {

    private static final Logger log = Logger.getLogger(ERPRemainsJDBC.class.getName());

    private String sqlSelectByCategory = "SELECT ABS(CHECKSUM(NEWID())) AS ID,nsi_kld.ngpr AS ITEM_NAME, " +
            "ARTICLE, MODEL, nsi_sd.rzm_print AS SIZE_PRINT, CATEGORY, " +
            "NSI_COLOR,  nsi_sd.kod AS KLD_ID, SD_ID, BALANCE, nsi_sd.rzm AS SIZE, nsi_sd.rst AS GROWTH, nsi_sd.srt AS GRADE, nsi_sd.cno AS PRICE " +
            "FROM ERP_REMAINS " +
            "INNER JOIN nsi_sd on nsi_sd.kod1 = SD_ID " +
            "INNER JOIN nsi_kld on nsi_kld.kod = nsi_sd.kod " +
            "WHERE CATEGORY LIKE '" + ":NODE" + "%'" + " and MODEL > 0 " +
            "ORDER BY MODEL, ARTICLE, nsi_sd.rzm, nsi_sd.rst, nsi_sd.srt";

    private String sqlSelectByCategoryArticle = "SELECT ABS(CHECKSUM(NEWID())) AS ID,nsi_kld.ngpr AS ITEM_NAME, " +
            "ARTICLE, MODEL, nsi_sd.rzm_print AS SIZE_PRINT, CATEGORY, " +
            "NSI_COLOR,  nsi_sd.kod AS KLD_ID, SD_ID, BALANCE, nsi_sd.rzm AS SIZE, nsi_sd.rst AS GROWTH, nsi_sd.srt AS GRADE, nsi_sd.cno AS PRICE " +
            "FROM ERP_REMAINS " +
            "INNER JOIN nsi_sd on nsi_sd.kod1 = SD_ID " +
            "INNER JOIN nsi_kld on nsi_kld.kod = nsi_sd.kod " +
            "WHERE (CATEGORY LIKE '" + ":NODE" + "%'" + " and MODEL > 0 ) :ARTICLE " +
            "ORDER BY MODEL, ARTICLE, nsi_sd.rzm, nsi_sd.rst, nsi_sd.srt";

    private String sqlSelectByModel = "SELECT ABS(CHECKSUM(NEWID())) AS ID,nsi_kld.ngpr AS ITEM_NAME, " +
            "ARTICLE, MODEL, nsi_sd.rzm_print AS SIZE_PRINT, CATEGORY, " +
            "NSI_COLOR,  nsi_sd.kod AS KLD_ID, SD_ID, BALANCE, nsi_sd.rzm AS SIZE, nsi_sd.rst AS GROWTH, nsi_sd.srt AS GRADE, nsi_sd.cno AS PRICE " +
            "FROM ERP_REMAINS " +
            "INNER JOIN nsi_sd on nsi_sd.kod1 = SD_ID " +
            "INNER JOIN nsi_kld on nsi_kld.kod = nsi_sd.kod " +
            "WHERE :MODEL " +
            "ORDER BY MODEL, ARTICLE, nsi_sd.rzm, nsi_sd.rst, nsi_sd.srt";

    public List<ERPRemainsEntity> getRemainsByCriteria(ERPFilter filter) {
        String sql = "";
        // Если указаны артикула
        if (filter.getArticles() != null) {
            String[] articles = filter.getArticles().split(";");
            StringBuilder sb = new StringBuilder();
            for (String a : articles) {
                sb.append("(ARTICLE LIKE '").append(a).append("%') OR ");
            }

            sqlSelectByCategoryArticle = sqlSelectByCategoryArticle
                    .replace(":ARTICLE", " AND (" + sb.substring(0, sb.length() - 3) + ") ");
            //System.out.println(sqlSelectByCategoryArticle.replace(":NODE", String.valueOf(filter.getNode().getCode())));
            sql = sqlSelectByCategoryArticle.replace(":NODE", String.valueOf(filter.getNode().getCode()));
            log.info("-----------" + 1 + " : " + sql);

        } else {
            // Если список моделей не пуст
            if (filter.getModels() != null && !filter.getModels().isEmpty()) {
                // Фильтр по моделям с учетом артикулов у модели, иначе все артикула модели
                // Формируем запрос для каждой позиции
                StringBuilder sb = new StringBuilder();
                for (ModelSelectionItem item : filter.getModels()) {
                    // ФОрмируем список артикулов, если есть
                    String articles = item.getArticles();
                    String articleList = "";
                    if (articles != null && !articles.isEmpty()) {
                        articleList = "AND (ARTICLE IN ('" + articles.replace(",", "','") + "'))";
                    }
                    sb.append("((MODEL = ").append(item.getModel()).append(")").append(articleList).append(") OR ");
                }

                sql = sqlSelectByModel
                        .replace(":MODEL", sb.substring(0, sb.length() - 3));
                log.info("-----------" + 2 + " : " + sqlSelectByModel);

            } else {
                // Фильтр только по категории
                sql = sqlSelectByCategory.replace(":NODE", String.valueOf(filter.getNode().getCode()));
                log.info("-----------" + 3 + " : " + sqlSelectByCategory);
            }
        }
        if(!sql.isEmpty()) {
            try (PreparedStatement ps = getConnection().prepareStatement(sql)) {
                List<ERPRemainsEntity> result = new ArrayList<>();
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    ERPRemainsEntity item = new ERPRemainsEntity();
                    item.setId(rs.getInt("ID"));
                    item.setItemName(rs.getString("ITEM_NAME"));
                    item.setModelNumber(rs.getInt("MODEL"));
                    item.setCategory(rs.getString("CATEGORY"));

                    item.setArticleNumber(rs.getString("ARTICLE"));
                    item.setArticleId(rs.getInt("KLD_ID"));

                    item.setProductId(rs.getInt("SD_ID"));
                    item.setNsiColorName(rs.getString("NSI_COLOR"));

                    item.setAmount(rs.getInt("BALANCE"));
                    item.setSize(rs.getInt("SIZE"));
                    item.setGrowth(rs.getInt("GROWTH"));
                    item.setGrade(rs.getInt("GRADE"));

                    item.setPrice(rs.getFloat("PRICE"));

                    if (filter.isGradeOnly()) {
                        if (item.getGrade() == 1) {
                            result.add(item);
                        }
                    } else {
                        result.add(item);
                    }
                }

                return result;
            } catch (Exception e) {
                log.severe(e.getMessage());
            }
        }

        return Collections.emptyList();
    }
}
