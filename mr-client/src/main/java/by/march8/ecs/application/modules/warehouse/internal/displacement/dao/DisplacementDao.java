package by.march8.ecs.application.modules.warehouse.internal.displacement.dao;

import by.gomel.freedev.ucframework.ucdao.DaoFactory;
import by.gomel.freedev.ucframework.ucdao.interfaces.INativeDao;
import by.march8.ecs.MainController;
import by.march8.ecs.application.modules.warehouse.external.shipping.model.March8Marker;
import by.march8.ecs.framework.common.BackgroundTask;
import by.march8.entities.warehouse.DisplacementInvoiceView;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Andy 26.07.2016.
 */
public class DisplacementDao implements IDisplacementDao {

    private List<DisplacementInvoiceView> getDisplacementDocumentByPeriod(int documentType, final Date dateBegin, final Date dateEnd) {
        ArrayList<DisplacementInvoiceView> data = new ArrayList<>();
        Class<?> dbMarker = March8Marker.class;
        DaoFactory factory = DaoFactory.getInstance();
        INativeDao dao = factory.getNativeDao();
        dao.processing(connection -> {
            try {
                String query = null;
                boolean isComplexQuery = false;
                query = DisplacementQueries.allDisplacementsByPeriod;


                PreparedStatement ps = connection.prepareStatement(query);

                ps.setDate(1, new java.sql.Date(dateBegin.getTime()));
                ps.setDate(2, new java.sql.Date(dateEnd.getTime()));


                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    DisplacementInvoiceView document = new DisplacementInvoiceView();
                    document.setId(rs.getInt("item_id"));
                    document.setDate(rs.getDate("date"));
                    document.setDocumentNumber(rs.getString("ndoc").trim());

                    document.setSenderCode(rs.getInt("kpodot"));
                    document.setSenderName(rs.getString("naim"));

                    document.setRecipientCode(rs.getInt("kpodto"));
                    document.setRecipientName(rs.getString("EXPR1"));

                    document.setAmountAll(rs.getInt("kola"));
                    document.setAmountPack(rs.getInt("kolk"));
                    document.setAmountUnPack(rs.getInt("kolr"));

                    document.setStatusCode(rs.getInt("status"));

                    document.setOperationName(rs.getString("operac"));

                    document.setReduction3Grade(rs.getDouble("ucenka3s"));

                    data.add(document);
                }
            } catch (Exception ex) {
                System.err.println("Ошибка получение списка документов "
                        + ex.getMessage());
            }
            return true;
        }, dbMarker);

        return data;
    }

    @Override
    @SuppressWarnings("all")
    public List<DisplacementInvoiceView> getDisplacementDocumentByPeriodThread(final int documentType, final Date dateBegin, final Date dateEnd) {

        class Task extends BackgroundTask {
            public Task(final String messageText) {
                super(messageText);
            }

            @Override
            protected List<DisplacementInvoiceView> doInBackground() throws Exception {
                return getDisplacementDocumentByPeriod(documentType, dateBegin, dateEnd);
            }
        }

        Task task = new Task("Получение данных...");
        task.executeTask();
        try {
            return (List<DisplacementInvoiceView>) task.get();
        } catch (Exception e) {
            MainController.exception(e, "Ошибка получения данных для метода getDisplacementDocumentByPeriodThread");
            return null;
        }
    }

}
