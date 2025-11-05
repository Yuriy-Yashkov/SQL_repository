package by.march8.ecs.application.modules.warehouse.external.shipping.dao;

import by.gomel.freedev.ucframework.ucdao.jdbc.AbstractMSSQLServerJDBC;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.logging.Logger;

/**
 * @author on 11.05.2020 10:25 Класс для того, что бы подхватывать курс установленный по
 * приказу на квартал, работает с таблицей course_by_order. В которой есть только одна запись.
 */
public class CourseDateByOrderDB extends AbstractMSSQLServerJDBC {

    private static final Logger log = Logger.getLogger(CourseDateByOrderDB.class.getName());

    public String getCourseByOrderStr() {
        String query = "select string_date from course_by_order where id=?";
        try (PreparedStatement ps = getConnection().prepareStatement(query)) {
            ps.setInt(1, 1);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("string_date");
            }
        } catch (Exception ex) {
            log.severe("Ошибка при выполнении функции getCourseByOrderStr:" + ex.getMessage());
        }
        return "";
    }

    public void updateCourseByOrder(String date) {
        String query = "update course_by_order set string_date = ? where id=?";
        try (PreparedStatement ps = getConnection().prepareStatement(query)) {
            ps.setString(1, date);
            ps.setInt(2, 1);
            ps.executeUpdate();
        } catch (Exception ex) {
            log.severe("Ошибка при выполнении функции updateCourseByOrder :" + ex.getMessage());
        }
    }
}
