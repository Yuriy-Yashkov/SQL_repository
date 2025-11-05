package by.march8.ecs.application.modules.marketing.dao;

import by.gomel.freedev.ucframework.ucdao.jdbc.AbstractMSSQLServerJDBC;
import by.march8.ecs.application.modules.references.classifier.model.ClassifierNode;
import by.march8.ecs.application.modules.references.classifier.model.ClassifierNodeType;
import by.march8.ecs.application.modules.references.classifier.ui.ClassifierTree;
import by.march8.ecs.application.modules.references.classifier.ui.ClassifierTreeDataSource;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class ERPRemainsTreeJDBC extends AbstractMSSQLServerJDBC implements ClassifierTreeDataSource {

    private static final Logger log = Logger.getLogger(ERPRemainsTreeJDBC.class.getName());

    @Override
    public List<ClassifierNode> getGroupNodes() {
        String query = "SELECT SUBSTRING(CONVERT(varchar(10), CATEGORY),1,2) as CODE " +
                "FROM VIEW_ERP_REMAINS_TREE " +
                "GROUP BY SUBSTRING(CONVERT(varchar(10), CATEGORY),1,2) " +
                "ORDER BY SUBSTRING(CONVERT(varchar(10), CATEGORY),1,2)";

        List<ClassifierNode> result = new ArrayList<>();
        try (PreparedStatement ps = getConnection().prepareStatement(query);
             ResultSet rs = ps.executeQuery();) {
            while (rs.next()) {
                ClassifierNode node = new ClassifierNode();
                node.setType(ClassifierNodeType.GROUP);
                node.setCode(rs.getInt("CODE"));
                node.setName(ClassifierTree.getNameAssortmentByArticleSegment(rs.getInt("CODE")));
                result.add(node);
            }
        } catch (Exception ex) {
            log.severe("Ошибка при выполнении функции getGroupNodes" + ex.getMessage());
        }

        return result;
    }

    @Override
    public List<ClassifierNode> getCategoryNodesByGroupId(ClassifierNode node) {
        String query = "SELECT CATEGORY as CODE " +
                "FROM VIEW_ERP_REMAINS_TREE " +
                "WHERE CATEGORY LIKE '" + node.getCode() + "%' " +
                "GROUP BY CATEGORY " +
                "ORDER BY CATEGORY";

        List<ClassifierNode> result = new ArrayList<>();
        try (PreparedStatement ps = getConnection().prepareStatement(query);
             ResultSet rs = ps.executeQuery();) {
            while (rs.next()) {
                ClassifierNode node_ = new ClassifierNode();
                node_.setType(ClassifierNodeType.CATEGORY);
                node_.setCode(rs.getInt("CODE"));
                node_.setName(ClassifierTree.getNameAssortmentByArticleSegment(rs.getInt("CODE")));
                result.add(node_);
            }
        } catch (Exception ex) {
            log.severe("Ошибка при выполнении функции getCategoryNodesByGroupId" + ex.getMessage());
        }

        return result;
    }

    @Override
    public List<ClassifierNode> getAssortmentNodesByCategoryId(ClassifierNode node) {
        String query = "SELECT ITEM_NAME AS ASSORTMENT " +
                "FROM VIEW_ERP_REMAINS_TREE " +
                "WHERE CATEGORY LIKE '" + node.getCode() + "%' " +
                "GROUP BY ITEM_NAME ORDER BY ITEM_NAME";

        List<ClassifierNode> result = new ArrayList<>();
        try (PreparedStatement ps = getConnection().prepareStatement(query);
             ResultSet rs = ps.executeQuery();) {
            while (rs.next()) {
                ClassifierNode node_ = new ClassifierNode();
                node_.setType(ClassifierNodeType.ASSORTMENT);
                node_.setCode(node.getCode());
                node_.setName(rs.getString("ASSORTMENT"));
                result.add(node_);
            }
        } catch (Exception ex) {
            log.severe("Ошибка при выполнении функции getAssortmentNodesByCategoryId" + ex.getMessage());
        }
        return result;
    }

    @Override
    public List<ClassifierNode> getModelNodesByAssortmentName(ClassifierNode node) {
        String query = "select MODEL_NUMBER AS MODEL " +
                "from VIEW_ERP_REMAINS_TREE " +
                "WHERE ITEM_NAME = ? " +
                "GROUP BY MODEL_NUMBER " +
                "ORDER BY MODEL_NUMBER";

        List<ClassifierNode> result = new ArrayList<>();
        try (PreparedStatement ps = getConnection().prepareStatement(query);) {
            ps.setString(1, node.getName());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ClassifierNode node_ = new ClassifierNode();
                node_.setType(ClassifierNodeType.MODEL);
                node_.setCode(node.getCode());
                node_.setName(rs.getString("MODEL"));
                result.add(node_);
            }
            rs.close();
        } catch (Exception ex) {
            log.severe("Ошибка при выполнении функции getModelNodesByAssortmentName" + ex.getMessage());
        }

        return result;
    }

    @Override
    public List<ClassifierNode> getArticleNodesByProductNumber(ClassifierNode node) {
        return null;
    }

    @Override
    public List<ClassifierNode> searchModel(String text) {
        String query = "SELECT CATEGORY as CODE, MODEL_NUMBER AS MODEL, ITEM_NAME AS ASSORTMENT " +
                "FROM VIEW_ERP_REMAINS_TREE " +
                "WHERE MODEL_NUMBER = ? " +
                "GROUP BY CATEGORY, MODEL_NUMBER, ITEM_NAME " +
                "ORDER BY MODEL_NUMBER";

        List<ClassifierNode> result = new ArrayList<>();
        try (PreparedStatement ps = getConnection().prepareStatement(query);) {
            ps.setString(1, text);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ClassifierNode node_ = new ClassifierNode();
                node_.setType(ClassifierNodeType.MODEL);
                node_.setCode(rs.getInt("CODE"));
                node_.setName(rs.getString("MODEL"));
                node_.setAssortmentName(rs.getString("ASSORTMENT"));
                result.add(node_);
            }
            rs.close();
        } catch (Exception ex) {
            log.severe("Ошибка при выполнении функции searchModel" + ex.getMessage());
        }

        return result;
    }

    @Override
    public List<ClassifierNode> searchArticle(String text) {
        return null;
    }

    public List<String> getModelListByNode(ClassifierNode node) {
        String query = "SELECT MODEL " +
                "FROM ERP_REMAINS " +
                "WHERE CATEGORY LIKE '" + node.getCode() + "%'" + " and MODEL > 0 " +
                "GROUP BY MODEL ORDER BY MODEL";

        List<String> result = new ArrayList<>();
        try (PreparedStatement ps = getConnection().prepareStatement(query);
             ResultSet rs = ps.executeQuery();) {
            while (rs.next()) {
                result.add(rs.getString("MODEL"));
            }
        } catch (Exception ex) {
            log.severe("Ошибка при выполнении функции getModelListByNode" + ex.getMessage());
        }

        return result;
    }

    public List<String> getArticleListByNode(int model) {
        String query = "SELECT ARTICLE " +
                "FROM ERP_REMAINS " +
                "WHERE MODEL = ? " +
                "GROUP BY ARTICLE ORDER BY ARTICLE ";

        List<String> result = new ArrayList<>();
        try (PreparedStatement ps = getConnection().prepareStatement(query);) {
            ps.setInt(1, model);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                result.add(rs.getString("ARTICLE"));
            }
            rs.close();
        } catch (Exception ex) {
            log.severe("Ошибка при выполнении функции getArticleListByNode" + ex.getMessage());
        }

        return result;
    }
}
