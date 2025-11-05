package by.march8.ecs.application.modules.marketing.dao;

import by.gomel.freedev.ucframework.ucdao.jdbc.AbstractMSSQLServerJDBC;
import by.march8.ecs.application.modules.references.classifier.model.ClassifierNode;
import by.march8.ecs.application.modules.references.classifier.model.ClassifierNodeType;
import by.march8.ecs.application.modules.references.classifier.ui.ClassifierTree;
import by.march8.ecs.application.modules.references.classifier.ui.ClassifierTreeDataSource;
import by.march8.entities.product.ProductionCatalog;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class ProductionCatalogTreeJDBC extends AbstractMSSQLServerJDBC implements ClassifierTreeDataSource {

    private static final Logger log = Logger.getLogger(ProductionCatalogTreeJDBC.class.getName());
    private ProductionCatalog document;

    public ProductionCatalogTreeJDBC(ProductionCatalog document) {
        this.document = document;
    }

    @Override
    public List<ClassifierNode> getGroupNodes() {
        String query = "SELECT SUBSTRING(CONVERT(varchar(10), CATEGORY),1,2) as CODE " +
                "FROM VIEW_PRODUCTION_CATALOG_TREE " +
                "WHERE DOCUMENT = ? " +
                "GROUP BY SUBSTRING(CONVERT(varchar(10), CATEGORY),1,2) " +
                "ORDER BY SUBSTRING(CONVERT(varchar(10), CATEGORY),1,2)";

        List<ClassifierNode> result = new ArrayList<>();
        try (PreparedStatement ps = getConnection().prepareStatement(query)) {
            ps.setInt(1, document.getId());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ClassifierNode node = new ClassifierNode();
                node.setType(ClassifierNodeType.GROUP);
                node.setCode(rs.getInt("CODE"));
                node.setName(ClassifierTree.getNameAssortmentByArticleSegment(rs.getInt("CODE")));
                result.add(node);
            }
        } catch (Exception ex) {
            log.severe("Ошибка при выполнении функции getGroupNodes " + ex.getMessage());
        }

        return result;
    }

    @Override
    public List<ClassifierNode> getCategoryNodesByGroupId(ClassifierNode node) {
        String query = "SELECT SUBSTRING(CONVERT(varchar(10), CATEGORY),1,3) as CODE " +
                "FROM VIEW_PRODUCTION_CATALOG_TREE " +
                "WHERE DOCUMENT = ? AND " +
                "ARTICLE_CODE LIKE '" + node.getCode() + "%' " +
                "GROUP BY SUBSTRING(CONVERT(varchar(10), CATEGORY),1,3) " +
                "ORDER BY SUBSTRING(CONVERT(varchar(10), CATEGORY),1,3)";

        List<ClassifierNode> result = new ArrayList<>();
        try (PreparedStatement ps = getConnection().prepareStatement(query)) {
            ps.setInt(1, document.getId());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ClassifierNode nodes = new ClassifierNode();
                nodes.setType(ClassifierNodeType.CATEGORY);
                nodes.setCode(rs.getInt("CODE"));
                nodes.setName(ClassifierTree.getNameAssortmentByArticleSegment(rs.getInt("CODE")));
                result.add(nodes);
            }
        } catch (Exception ex) {
            log.severe("Ошибка при выполнении функции getCategoryNodesByGroupId " + ex.getMessage());
        }

        return result;
    }

    @Override
    public List<ClassifierNode> getAssortmentNodesByCategoryId(ClassifierNode node) {
        String query = "SELECT ITEM_NAME AS ASSORTMENT " +
                "FROM VIEW_PRODUCTION_CATALOG_TREE " +
                "WHERE DOCUMENT = ? AND " +
                "ARTICLE_CODE LIKE '" + node.getCode() + "%' " +
                "GROUP BY ITEM_NAME ORDER BY ITEM_NAME";

        List<ClassifierNode> result = new ArrayList<>();
        try (PreparedStatement ps = getConnection().prepareStatement(query)) {
            ps.setInt(1, document.getId());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ClassifierNode node_ = new ClassifierNode();
                node_.setType(ClassifierNodeType.ASSORTMENT);
                node_.setCode(node.getCode());
                node_.setName(rs.getString("ASSORTMENT"));
                result.add(node_);
            }
            rs.close();
        } catch (Exception ex) {
            log.severe("Ошибка при выполнении функции getAssortmentNodesByCategoryId " + ex.getMessage());
        }
        return result;
    }

    @Override
    public List<ClassifierNode> getModelNodesByAssortmentName(ClassifierNode node) {
        String query = "select MODEL_NUMBER AS MODEL " +
                "from VIEW_PRODUCTION_CATALOG_TREE " +
                "WHERE DOCUMENT = ? AND " +
                "ITEM_NAME = ? " +
                "GROUP BY MODEL_NUMBER " +
                "ORDER BY MODEL_NUMBER";

        List<ClassifierNode> result = new ArrayList<>();
        try (PreparedStatement ps = getConnection().prepareStatement(query)) {
            ps.setInt(1, document.getId());
            ps.setString(2, node.getName());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ClassifierNode node_ = new ClassifierNode();
                node_.setType(ClassifierNodeType.MODEL);
                node_.setCode(node.getCode());
                node_.setName(rs.getString("MODEL"));
                result.add(node_);
            }
        } catch (Exception ex) {
            log.severe("Ошибка при выполнении функции getModelNodesByAssortmentName " + ex.getMessage());
        }

        return result;
    }

    @Override
    public List<ClassifierNode> getArticleNodesByProductNumber(ClassifierNode node) {
        return null;
    }

    @Override
    public List<ClassifierNode> searchModel(String text) {
        String query = "SELECT SUBSTRING(CONVERT(varchar(10), CATEGORY),1,3) as CODE, MODEL_NUMBER AS MODEL, ITEM_NAME AS ASSORTMENT " +
                "FROM VIEW_PRODUCTION_CATALOG_TREE " +
                "WHERE DOCUMENT = ? AND " +
                "MODEL_NUMBER = ? " +
                "GROUP BY SUBSTRING(CONVERT(varchar(10), CATEGORY),1,3), MODEL_NUMBER, ITEM_NAME " +
                "ORDER BY MODEL_NUMBER";

        List<ClassifierNode> result = new ArrayList<>();
        try (PreparedStatement ps = getConnection().prepareStatement(query)) {
            ps.setInt(1, document.getId());
            ps.setString(2, text);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ClassifierNode node_ = new ClassifierNode();
                node_.setType(ClassifierNodeType.MODEL);
                node_.setCode(rs.getInt("CODE"));
                node_.setName(rs.getString("MODEL"));
                node_.setAssortmentName(rs.getString("ASSORTMENT"));
                result.add(node_);
            }
        } catch (Exception ex) {
            log.severe("Ошибка при выполнении функции searchModel " + ex.getMessage());
        }

        return result;
    }

    @Override
    public List<ClassifierNode> searchArticle(String text) {
        return null;
    }
}
