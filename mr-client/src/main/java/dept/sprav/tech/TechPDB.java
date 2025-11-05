package dept.sprav.tech;

import by.march8.ecs.framework.common.LogCrutch;
import workDB.PDB_new;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Vector;

//import org.apache.log4j.Logger;

/**
 *
 * @author lidashka
 */
public class TechPDB extends PDB_new {
    //private static final Logger log = new Log().getLoger(TechPDB.class);
    private static final LogCrutch log = new LogCrutch();

    /**
     * Возвращает все записи справочника оборудования.
     * @return Vector[сокращение, оборудование]
     * @throws Exception
     */
    public Vector getAllTech() throws Exception {
        Vector models = new Vector();
        String sql = "Select id, tech, price from s_tech Order by tech";
        try {
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                Vector tmp = new Vector();
                tmp.add(rs.getInt("id"));
                tmp.add(rs.getString("tech"));
                tmp.add(new BigDecimal(rs.getDouble("price")).setScale(2, RoundingMode.HALF_UP).doubleValue());
                models.add(tmp);
            }
        } catch (Exception e) {
            System.err.println("Ошибка getAllTech() " + e);
            log.error("Ошибка getAllTech()", e);
            throw new Exception("Ошибка getAllTech() " + e.getMessage(), e);
        }
        return models;
    }

    /**
     * Сохраняет изменения оборудования.     
     * @param idTech id оборудования
     * @param tech оборудование
     * @param price стоимость
     * @throws Exception
     */
    public void saveTech(Integer idTech, String tech, double price) throws Exception {
        String sql = "UPDATE s_tech SET tech = ?, price = ? WHERE id = ?";
        try {
            ps = conn.prepareStatement(sql);
            ps.setString(1, tech);
            ps.setDouble(2, new BigDecimal(price).setScale(2, RoundingMode.HALF_UP).doubleValue());
            ps.setInt(3, idTech);
            ps.execute();
        } catch (Exception e) {
            System.err.println("Ошибка saveTech() " + e);
            log.error("Ошибка saveTech()", e);
            throw new Exception("Ошибка saveTech() ", e);
        }
    }

    /**
     * Добавляет новое оборудование в справочник.
     * @param tech оборудование
     * @param price стоимость
     * @throws Exception
     */
    public void addTech(String tech, double price) throws Exception {
        String sql = "INSERT INTO s_tech (tech, price) VALUES (?, ?)";
        try {
            ps = conn.prepareStatement(sql);
            ps.setString(1, tech);
            ps.setDouble(2, new BigDecimal(price).setScale(2, RoundingMode.HALF_UP).doubleValue());
            ps.execute();
        } catch (Exception e) {
            System.err.println("Ошибка addTech() " + e);
            log.error("Ошибка addTech()", e);
            throw new Exception("Ошибка addTech() " + e.getMessage(), e);
        }
    }

    /**
     * Удаляет оборудование.
     * @param idTech модель
     * @throws Exception
     */
    public void deleteTech(Integer idTech) throws Exception {
        String sql = "DELETE FROM s_tech WHERE id = ?";
        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, idTech);
            ps.execute();
        } catch (Exception e) {
            System.err.println("Ошибка deleteTech() " + e);
            log.error("Ошибка deleteTech()", e);
            throw new Exception("Ошибка deleteTech() " + e.getMessage(), e);
        }
    }
}
