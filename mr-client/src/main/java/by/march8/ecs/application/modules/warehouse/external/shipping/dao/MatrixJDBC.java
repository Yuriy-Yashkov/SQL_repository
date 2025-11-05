package by.march8.ecs.application.modules.warehouse.external.shipping.dao;

import by.gomel.freedev.ucframework.ucdao.jdbc.AbstractMSSQLServerJDBC;
import by.march8.ecs.application.modules.warehouse.external.shipping.model.MatrixContractor;
import by.march8.ecs.application.modules.warehouse.external.shipping.model.MatrixModel;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class MatrixJDBC extends AbstractMSSQLServerJDBC {

    private static final Logger log = Logger.getLogger(MatrixJDBC.class.getName());

    public List<MatrixContractor> getMatrixContractors(String contractors) {
        String sql = "SELECT KOD, NAIM " +
                "FROM s_klient " +
                "WHERE KOD in (?CONTRACTORS) " +
                "ORDER BY KOD ";

        sql = sql.replace("?CONTRACTORS", contractors);

        List<MatrixContractor> result = new ArrayList<>();
        try (PreparedStatement ps = getConnection().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            int counter = 1;
            while (rs.next()) {
                MatrixContractor item = new MatrixContractor();
                item.setId(counter++);
                item.setCode(rs.getInt("KOD"));
                item.setName(rs.getString("NAIM"));
                item.setOrderByAddress(false);
                result.add(item);
            }
        } catch (Exception ex) {
            log.severe("Ошибка при выполнении функции getMatrixContractors :" + ex.getMessage());
        }
        return result;
    }

    public List<MatrixModel> getMatrixModels(String models) {
        String sql = "SELECT fas AS MODEL, ngpr AS NAME " +
                "FROM nsi_kld " +
                "WHERE fas in (?MODELS) " +
                "GROUP BY ngpr, fas " +
                "ORDER BY ngpr ";

        sql = sql.replace("?MODELS", models);

        List<MatrixModel> result = new ArrayList<>();
        try (PreparedStatement ps = getConnection().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            int counter = 1;
            while (rs.next()) {
                MatrixModel item = new MatrixModel();
                item.setId(counter++);
                item.setModelNumber(rs.getInt("MODEL"));
                item.setName(rs.getString("NAME"));
                result.add(item);
            }
        } catch (Exception ex) {
            log.severe("Ошибка при выполнении функции getMatrixModels :" + ex.getMessage());
        }
        return result;
    }
}
