package dept.sprav.model;

import by.march8.ecs.framework.common.LogCrutch;
import workDB.PDB_new;

import java.util.Vector;

/**
 *
 * @author lidashka
 */
public class ModelPDB extends PDB_new {
    //private static final Logger log = new Log().getLoger(ModelPDB.class);
    private static final LogCrutch log = new LogCrutch();

    /**
     * Возвращает все записи справочника моделей.
     * @return Vector[модель, наименование]
     * @throws Exception
     */
    public Vector getAllModels() throws Exception {
        Vector models = new Vector();
        String sql = "Select model, naim from s_model where model >= 0 Order by model, naim";
        try {
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                Vector tmp = new Vector();
                tmp.add(rs.getInt("model"));
                tmp.add(rs.getString("naim"));
                models.add(tmp);
            }
        } catch (Exception e) {
            System.err.println("Ошибка getAllModels() " + e);
            log.error("Ошибка getAllModels()", e);
            throw new Exception("Ошибка getAllModels() " + e.getMessage(), e);
        }
        return models;
    }

    /**
     * Сохраняет изменения модели.
     * @param model модель
     * @param naim наименование
     * @throws Exception
     */
    public void saveModel(Integer model, String naim) throws Exception {
        String sql = "UPDATE s_model SET naim = ? WHERE model = ?";
        try {
            ps = conn.prepareStatement(sql);
            ps.setString(1, naim);
            ps.setInt(2, model);
            ps.execute();
        } catch (Exception e) {
            System.err.println("Ошибка saveModel() " + e);
            log.error("Ошибка saveModel()", e);
            throw new Exception("Ошибка saveModel() ", e);
        }
    }

    /**
     * Добавляет новую модель в справочник.
     * @param model модель
     * @param naim наименование
     * @throws Exception
     */
    public void addModel(Integer model, String naim) throws Exception {
        String sql = "INSERT INTO s_model (model, naim) VALUES (?, ?)";
        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, model);
            ps.setString(2, naim);
            ps.execute();
        } catch (Exception e) {
            System.err.println("Ошибка addModel() " + e);
            log.error("Ошибка addModel()", e);
            throw new Exception("Ошибка addModel() " + e.getMessage(), e);
        }
    }

    /**
     * Удаляет модель.
     * @param model модель
     * @throws Exception
     */
    public void deleteModel(Integer model) throws Exception {
        String sql = "DELETE FROM s_model WHERE model = ?";
        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, model);
            ps.execute();
        } catch (Exception e) {
            System.err.println("Ошибка deleteModel() " + e);
            log.error("Ошибка deleteModel()", e);
            throw new Exception("Ошибка deleteModel() " + e.getMessage(), e);
        }
    }
}
