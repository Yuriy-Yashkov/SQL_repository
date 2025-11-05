package by.march8.ecs.application.modules.references.classifier.dao;

import by.gomel.freedev.ucframework.ucdao.jdbc.AbstractMSSQLServerJDBC;
import by.march8.ecs.application.modules.planning.model.ProductionEquipment;
import by.march8.ecs.application.modules.references.classifier.model.ConsumptionRateItem;
import by.march8.ecs.application.modules.references.classifier.model.ProductCategoryItem;
import by.march8.entities.classifier.ClassifierArticleComposition;
import by.march8.entities.classifier.ClassifierItem;
import by.march8.entities.classifier.ClassifierModelParams;
import by.march8.entities.classifier.CompositionMaterialView;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * @author Andy 20.11.2018 - 10:02.
 */
public class ClassifierJDBC extends AbstractMSSQLServerJDBC {

    private static final Logger log = Logger.getLogger(ClassifierJDBC.class.getName());

    public void insertRate(ConsumptionRateItem item) {
        String queryString = "INSERT INTO NSI_COMPOSITION_MATERIAL_RATES " +
                "(REF_NSI_SD_ID, REF_NSI_COMPOSITION_MATERIAL_ID, RATE) " +
                " VALUES(?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(queryString)) {
            ps.setInt(1, item.getProductId());
            ps.setInt(2, item.getMaterialId());
            ps.setDouble(3, item.getRate());
            ps.executeUpdate();
        } catch (Exception e) {
            log.severe("Ошибка при заполнении insertRate" + e.getMessage());
        }
    }

    public void updateRate(ConsumptionRateItem item) {
        String queryString = "UPDATE NSI_COMPOSITION_MATERIAL_RATES SET " +
                "RATE =? " +
                "WHERE ID=? ";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(queryString)) {
            ps.setDouble(1, item.getRate());
            ps.setInt(2, item.getRateId());
            ps.executeUpdate();
        } catch (Exception e) {
            log.severe("Ошибка при заполнении updateRate" + e.getMessage());
        }
    }

    public void deleteRate(ConsumptionRateItem item) {
        String queryString = "DELETE FROM NSI_COMPOSITION_MATERIAL_RATES " +
                "WHERE REF_NSI_SD_ID = ? AND REF_NSI_COMPOSITION_MATERIAL_ID = ? ";
        try (PreparedStatement ps = getConnection().prepareStatement(queryString)) {
            ps.setInt(1, item.getProductId());
            ps.setInt(2, item.getMaterialId());
            ps.executeUpdate();
        } catch (Exception e) {
            log.severe("Ошибка при заполнении deleteRate" + e.getMessage());
        }
    }

    public Map<Integer, ConsumptionRateItem> getConsumptionRatesBySDCode(int sdCode) {
        String query = "select * from NSI_COMPOSITION_MATERIAL_RATES nsi  WHERE REF_NSI_SD_ID = ?";
        HashMap<Integer, ConsumptionRateItem> result = new HashMap<>();
        try (PreparedStatement ps = getConnection().prepareStatement(query)) {
            ps.setInt(1, sdCode);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ConsumptionRateItem item = new ConsumptionRateItem();
                item.setId(rs.getInt("ID"));
                item.setProductId(rs.getInt("REF_NSI_SD_ID"));
                item.setRate(rs.getDouble("RATE"));
                item.setMaterialId(rs.getInt("REF_NSI_COMPOSITION_MATERIAL_ID"));
                item.setRateId(rs.getInt("ID"));
                result.put(rs.getInt("REF_NSI_COMPOSITION_MATERIAL_ID"), item);
            }
        } catch (Exception ex) {
            log.severe("Ошибка при выполнении функции getConsumptionRatesBySDCode для (" + sdCode + ") :" + ex.getMessage());
        }

        return result;
    }

    public void deleteRatesForModel(CompositionMaterialView item, ClassifierModelParams controlItem) {
        String queryString = "DELETE FROM NSI_COMPOSITION_MATERIAL_RATES " +
                "WHERE REF_NSI_SD_ID in (?1) AND REF_NSI_COMPOSITION_MATERIAL_ID = ?2";
        String aList = "";
        for (ClassifierItem classifierItem : controlItem.getAssortmentList()) {
            if (classifierItem.getItemGrade() == 1) {
                aList += classifierItem.getId() + ",";
            }
        }
        aList = aList.substring(0, aList.length() - 1);
        queryString = queryString.replace("?1", aList).replace("?2", String.valueOf(item.getId()));

        try (PreparedStatement ps = getConnection().prepareStatement(queryString)) {
            ps.executeUpdate();
        } catch (Exception e) {
            log.severe("Ошибка при заполнении deleteRatesForModel" + e.getMessage());
        }
    }

    public void applyComponentsForAll(ClassifierModelParams item, List<CompositionMaterialView> list) {
        String queryString = "INSERT INTO NSI_COMPOSITION_MATERIAL_RATES " +
                "(REF_NSI_SD_ID, REF_NSI_COMPOSITION_MATERIAL_ID, RATE) " +
                " VALUES(?, ?, ?)";
        Map<String, ConsumptionRateItem> map = getConsumptionRatesByModel(item);
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(queryString)) {

            conn.setAutoCommit(false);

            for (ClassifierItem classifierItem : item.getAssortmentList()) {
                for (CompositionMaterialView materialItem : list) {
                    if (classifierItem.getItemGrade() == 1) {
                        ConsumptionRateItem item_ = map.get(classifierItem.getId() + "_" + materialItem.getId());
                        if (item_ == null) {
                            ps.setInt(1, classifierItem.getId());
                            ps.setInt(2, materialItem.getId());
                            ps.setDouble(3, 0);
                            ps.addBatch();
                        }
                    }
                }
            }

            ps.executeBatch();
            conn.commit();
        } catch (Exception e) {
            log.severe("Ошибка при заполнении applyComponentsForAll" + e.getMessage());
        }
    }

    public Map<String, ConsumptionRateItem> getConsumptionRatesByModel(ClassifierModelParams controlItem) {
        String queryString = "SELECT * FROM NSI_COMPOSITION_MATERIAL_RATES " +
                "WHERE REF_NSI_SD_ID in (?1)";

        String aList = "";
        for (ClassifierItem classifierItem : controlItem.getAssortmentList()) {
            if (classifierItem.getItemGrade() == 1) {
                aList += classifierItem.getId() + ",";
            }
        }
        aList = aList.substring(0, aList.length() - 1);
        queryString = queryString.replace("?1", aList);

        Map<String, ConsumptionRateItem> result = new HashMap<>();
        try (PreparedStatement ps = getConnection().prepareStatement(queryString);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                ConsumptionRateItem item = new ConsumptionRateItem();
                item.setId(rs.getInt("ID"));
                item.setProductId(rs.getInt("REF_NSI_SD_ID"));
                item.setRate(rs.getDouble("RATE"));
                item.setMaterialId(rs.getInt("REF_NSI_COMPOSITION_MATERIAL_ID"));
                item.setRateId(rs.getInt("ID"));
                result.put(rs.getInt("REF_NSI_SD_ID") + "_" + rs.getInt("REF_NSI_COMPOSITION_MATERIAL_ID"), item);
            }
        } catch (Exception ex) {
            log.severe("Ошибка при выполнении функции getConsumptionRatesByModel для (" + controlItem.getId() + ") :" + ex.getMessage());
        }

        return result;
    }

    public ProductionEquipment getEquipmentBySDCode(int sdCode) {
        String query = "SELECT N.ID, N.REF_NSI_SD_ID, N.REF_EQUIPMENT_ID, EQUIPMENT.SHORT_NAME, N.PERFORMANCE_VALUE " +
                "FROM NSI_PRODUCTION_EQUIPMENT N " +
                "LEFT JOIN (SELECT ID, SHORT_NAME FROM dbo.REF_EQUIPMENT) EQUIPMENT on EQUIPMENT.ID = N.REF_EQUIPMENT_ID " +
                "WHERE REF_NSI_SD_ID = ?";

        ProductionEquipment result = null;
        try (PreparedStatement ps = getConnection().prepareStatement(query)) {
            ps.setInt(1, sdCode);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                result = new ProductionEquipment();
                result.setId(rs.getInt("ID"));
                result.setProductId(rs.getInt("REF_NSI_SD_ID"));
                result.setEquipmentId(rs.getInt("REF_EQUIPMENT_ID"));
                result.setEquipmentName(rs.getString("SHORT_NAME"));
                result.setPerformance(rs.getDouble("PERFORMANCE_VALUE"));
            }
        } catch (Exception ex) {
            log.severe("Ошибка при выполнении функции getConsumptionRatesBySDCode для (" + sdCode + ") :" + ex.getMessage());
        }

        return result;
    }

    public void insertEquipment(ProductionEquipment item) {
        String queryString = "INSERT INTO NSI_PRODUCTION_EQUIPMENT " +
                "(REF_NSI_SD_ID, REF_EQUIPMENT_ID, PERFORMANCE_VALUE) " +
                " VALUES(?, ?, ?)";
        try (PreparedStatement ps = getConnection().prepareStatement(queryString)) {
            ps.setInt(1, item.getProductId());
            ps.setInt(2, item.getEquipmentId());
            ps.setDouble(3, item.getPerformance());
            ps.executeUpdate();
        } catch (Exception e) {
            log.severe("Ошибка при заполнении insertEquipment" + e.getMessage());
        }
    }

    public void updateEquipment(ProductionEquipment item) {
        String queryString = "UPDATE NSI_PRODUCTION_EQUIPMENT SET " +
                "REF_EQUIPMENT_ID =?, " +
                "PERFORMANCE_VALUE =? " +
                "WHERE ID=? ";
        try (PreparedStatement ps = getConnection().prepareStatement(queryString)) {
            ps.setInt(1, item.getEquipmentId());
            ps.setDouble(2, item.getPerformance());
            ps.setInt(3, item.getId());
            ps.executeUpdate();
        } catch (Exception e) {
            log.severe("Ошибка при заполнении updateRate" + e.getMessage());
        }
    }

    public ClassifierArticleComposition getMaterialCompositionByArticleId(int articleId) {
        String query = "select TOP 1 sostav1, sostav2,sostav3,sostav4, data from label_one where kod_izd = ? ORDER BY data DESC";

        ClassifierArticleComposition result = null;
        try (PreparedStatement ps = getConnection().prepareStatement(query)) {
            ps.setInt(1, articleId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                result = new ClassifierArticleComposition();
                result.setComposition_1(rs.getString("sostav1"));
                result.setComposition_2(rs.getString("sostav2"));
                result.setComposition_3(rs.getString("sostav3"));
                result.setComposition_4(rs.getString("sostav4"));
            }
        } catch (Exception ex) {
            log.severe("Ошибка при выполнении функции getMaterialCompositionByArticleId для (" + articleId + ") :" + ex.getMessage());
        }

        return result;
    }

    public ClassifierArticleComposition getMaterialCompositionByModelNumber(int modelNumber) {
        String query = "select TOP 1 sostav1, sostav2,sostav3,sostav4, data from label_one " +
                "LEFT JOIN nsi_kld on nsi_kld.kod = label_one.kod_izd " +
                "WHERE nsi_kld.fas = ? ORDER BY data DESC ";

        ClassifierArticleComposition result = null;
        try (PreparedStatement ps = getConnection().prepareStatement(query)) {
            ps.setInt(1, modelNumber);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                result = new ClassifierArticleComposition();
                result.setComposition_1(rs.getString("sostav1"));
                result.setComposition_2(rs.getString("sostav2"));
                result.setComposition_3(rs.getString("sostav3"));
                result.setComposition_4(rs.getString("sostav4"));
            }
        } catch (Exception ex) {
            log.severe("Ошибка при выполнении функции getMaterialCompositionByModelNumber для (" + modelNumber + ") :" + ex.getMessage());
        }
        return result;
    }

    public List<ProductCategoryItem> getAssortmentTemp() {
        String query = "select ngpr as ITEM_NAME, SUBSTRING(CONVERT(varchar(10), dbo.nsi_kld.sar), 1, 3) AS CATEGORY from nsi_kld " +
                "where sar <= 43999999 " +
                "GROUP BY ngpr, SUBSTRING(CONVERT(varchar(10), dbo.nsi_kld.sar), 1, 3) " +
                "ORDER BY ngpr ";

        List<ProductCategoryItem> result = new ArrayList<>();
        try (PreparedStatement ps = getConnection().prepareStatement(query)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ProductCategoryItem item = new ProductCategoryItem();
                item.setRawName(rs.getString("ITEM_NAME").trim());
                item.setCode(rs.getInt("CATEGORY"));
                result.add(item);
            }
        } catch (Exception ex) {
            log.severe("Error in method getAssortmentTemp:" + ex.getMessage());
        }
        return result;
    }
}
