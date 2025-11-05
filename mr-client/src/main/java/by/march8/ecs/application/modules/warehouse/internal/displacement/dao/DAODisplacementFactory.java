package by.march8.ecs.application.modules.warehouse.internal.displacement.dao;

/**
 * @author Andy 26.07.2016.
 */
public class DAODisplacementFactory {

    public static DAODisplacementFactory instance = new DAODisplacementFactory();

    private IDisplacementDao displacementDao;


    /**
     * Возвращает DAO фабрику
     */
    public static DAODisplacementFactory getInstance() {
        return DAODisplacementFactory.instance;
    }

    public IDisplacementDao getDisplacementDao() {
        if (displacementDao == null) {
            displacementDao = new DisplacementDao();
        }
        return displacementDao;
    }
}
