package by.march8.ecs.application.modules.references.classifier.dao;

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

/**
 * @author Andy 08.01.2019 - 10:40.
 */
public class ClassifierTreeJDBC extends AbstractMSSQLServerJDBC implements ClassifierTreeDataSource {

    private static final Logger log = Logger.getLogger(ClassifierTreeJDBC.class.getName());

    public List<ClassifierNode> getGroupNodes() {
        String query = "SELECT SUBSTRING(CONVERT(varchar(10), sar),1,2) as CODE " +
                "FROM nsi_kld " +
                "GROUP BY SUBSTRING(CONVERT(varchar(10), sar),1,2) " +
                "ORDER BY SUBSTRING(CONVERT(varchar(10), sar),1,2)";

        List<ClassifierNode> result = new ArrayList<>();
        try (PreparedStatement ps = getConnection().prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
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

    public List<ClassifierNode> getCategoryNodesByGroupId(ClassifierNode node) {
        String query = "SELECT SUBSTRING(CONVERT(varchar(10), sar),1,3) as CODE " +
                "FROM nsi_kld " +
                "WHERE sar LIKE '" + node.getCode() + "%' " +
                "GROUP BY SUBSTRING(CONVERT(varchar(10), sar),1,3) " +
                "ORDER BY SUBSTRING(CONVERT(varchar(10), sar),1,3)";
        List<ClassifierNode> result = new ArrayList<>();
        try (PreparedStatement ps = getConnection().prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                ClassifierNode node_ = new ClassifierNode();
                node_.setType(ClassifierNodeType.CATEGORY);
                node_.setCode(rs.getInt("CODE"));
                node_.setName(ClassifierTree.getNameAssortmentByArticleSegment(rs.getInt("CODE")));
                result.add(node_);
            }
        } catch (Exception ex) {
            log.severe("Ошибка при выполнении функции getCategoryNodesByGroupId: " + ex.getMessage());
        }
        return result;
    }

    public List<ClassifierNode> getAssortmentNodesByCategoryId(ClassifierNode node) {
        String query = "SELECT ngpr AS ASSORTMENT " +
                "FROM nsi_kld " +
                "WHERE sar LIKE '" + node.getCode() + "%' " +
                "GROUP BY ngpr ORDER BY ngpr";

        List<ClassifierNode> result = new ArrayList<>();
        try (PreparedStatement ps = getConnection().prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                ClassifierNode node_ = new ClassifierNode();
                node_.setType(ClassifierNodeType.ASSORTMENT);
                node_.setCode(node.getCode());
                node_.setName(rs.getString("ASSORTMENT"));
                result.add(node_);
            }
        } catch (Exception ex) {
            log.severe("Ошибка при выполнении функции getAssortmentNodesByCategoryId: " + ex.getMessage());
        }

        return result;
    }

    public List<ClassifierNode> getModelNodesByAssortmentName(ClassifierNode node) {
        String query = "select fas AS MODEL " +
                "from nsi_kld " +
                "where ngpr=? " +
                "GROUP BY fas " +
                "ORDER BY fas";

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
        } catch (Exception ex) {
            log.severe("Ошибка при выполнении функции getModelNodesByAssortmentName: " + ex.getMessage());
        }

        return result;
    }

    public List<ClassifierNode> getArticleNodesByProductNumber(ClassifierNode node) {
        String query = "select nar AS ARTICLE " +
                "from nsi_kld " +
                "where fas=? " +
                "GROUP BY nar " +
                "ORDER BY nar";

        List<ClassifierNode> result = new ArrayList<>();
        try (PreparedStatement ps = getConnection().prepareStatement(query);) {
            ps.setString(1, node.getName());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ClassifierNode node_ = new ClassifierNode();
                node_.setType(ClassifierNodeType.ARTICLE);
                node_.setCode(node.getCode());
                node_.setName(rs.getString("ARTICLE"));
                result.add(node_);
            }
        } catch (Exception ex) {
            log.severe("Ошибка при выполнении функции getArticleNodesByProductNumber: " + ex.getMessage());
        }

        return result;
    }

    public List<ClassifierNode> searchModel(String text) {
        String query = "SELECT SUBSTRING(CONVERT(varchar(10), sar),1,3) as CODE, fas AS MODEL, ngpr AS ASSORTMENT " +
                "FROM nsi_kld " +
                "WHERE fas = ? GROUP BY SUBSTRING(CONVERT(varchar(10), sar),1,3), fas, ngpr ORDER BY fas";

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
        } catch (Exception ex) {
            log.severe("Ошибка при выполнении функции searchModel: " + ex.getMessage());
        }

        return result;
    }

    public List<ClassifierNode> searchArticle(String text) {
        String query = "SELECT SUBSTRING(CONVERT(varchar(10), sar),1,3) as CODE, fas AS MODEL, ngpr AS [NAME] " +
                "FROM nsi_kld " +
                "WHERE nar LIKE '" + text + "%'GROUP BY SUBSTRING(CONVERT(varchar(10), sar),1,3), fas, ngpr ORDER BY fas";

        List<ClassifierNode> result = new ArrayList<>();
        try (PreparedStatement ps = getConnection().prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                ClassifierNode node_ = new ClassifierNode();
                node_.setType(ClassifierNodeType.MODEL);
                node_.setCode(rs.getInt("CODE"));
                node_.setName(rs.getString("MODEL") + "_" + rs.getString("NAME"));
                result.add(node_);
            }
        } catch (Exception ex) {
            log.severe("Ошибка при выполнении функции searchModel");
        }

        return result;
    }
}
