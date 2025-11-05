package by.march8.ecs.application.modules.filemanager.dao;

import by.gomel.freedev.ucframework.ucdao.jdbc.AbstractMSSQLServerJDBC;
import by.march8.ecs.application.modules.filemanager.model.LabelItem;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

/**
 * @author Andy 10.01.2019 - 13:30.
 */
public class ImageManagerJDBC extends AbstractMSSQLServerJDBC {

    private static final Logger log = Logger.getLogger(ImageManagerJDBC.class.getName());

    public List<LabelItem> getLastLabels() {
        List<LabelItem> result = new ArrayList<>();

        String query = "SELECT fas as MODEL " +
                "FROM label_one " +
                "WHERE DATEPART(m, data) = DATEPART(m, DATEADD(m, -1, getdate())) " +
                "AND DATEPART(yyyy, data) = DATEPART(yyyy, DATEADD(m, -1, getdate())) " +
                "GROUP BY fas " +
                "ORDER BY fas ";
        try (PreparedStatement ps = getConnection().prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                result.add(new LabelItem(rs.getString("MODEL").trim()));
            }
        } catch (Exception ex) {
            log.severe("Error in class getRazrezArt()" + ex);
        }
        return result;
    }

    public List<String> getItemNamesByModelNumber(String model) {
        String query = "SELECT DISTINCT ngpr AS ITEM_NAME FROM nsi_kld WHERE fas = ? ORDER BY ngpr";

        List<String> result = new ArrayList<>();
        try (PreparedStatement ps = getConnection().prepareStatement(query)) {
            ps.setString(1, model);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                result.add(rs.getString("ITEM_NAME"));
            }
            rs.close();
        } catch (Exception ex) {
            log.severe("Ошибка при выполнении функции getItemNamesByModelNumber :" + ex);
        }
        return result;
    }

    public Set<Integer> getModelNumbersHavingImages() {
        String query = "SELECT DISTINCT MODEL_NUMBER  FROM IMAGES_MODEL GROUP BY MODEL_NUMBER";

        Set<Integer> result = new HashSet<>();
        try (PreparedStatement ps = getConnection().prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                result.add(rs.getInt("MODEL_NUMBER"));
            }
        } catch (Exception ex) {
            log.severe("Ошибка при выполнении функции getModelNumbersHavingImages :" + ex);
        }
        return result;
    }

    public Set<String> getImagesSourceFileSet() {

        String query = "SELECT SOURCE_FILE_NAME  FROM IMAGES_MODEL_IMAGE";

        Set<String> result = new HashSet<>();
        try (PreparedStatement ps = getConnection().prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                result.add(rs.getString("SOURCE_FILE_NAME").trim().toLowerCase());
            }
        } catch (Exception ex) {
            log.severe("Ошибка при выполнении функции getImagesSourceFileSet :" + ex);
        }
        return result;
    }
}
