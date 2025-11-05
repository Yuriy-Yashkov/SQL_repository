package by.gomel.freedev.ucframework.ucdao;

import by.gomel.freedev.ucframework.ucdao.implementations.AdministrationDao;
import by.gomel.freedev.ucframework.ucdao.implementations.CommonDao;
import by.gomel.freedev.ucframework.ucdao.implementations.GenericDao;
import by.gomel.freedev.ucframework.ucdao.implementations.NativeDAO;
import by.gomel.freedev.ucframework.ucdao.implementations.ReferencesDao;
import by.gomel.freedev.ucframework.ucdao.interfaces.IAdministrationDao;
import by.gomel.freedev.ucframework.ucdao.interfaces.ICommonDao;
import by.gomel.freedev.ucframework.ucdao.interfaces.ICommonDaoThread;
import by.gomel.freedev.ucframework.ucdao.interfaces.IGenericDaoGUI;
import by.gomel.freedev.ucframework.ucdao.interfaces.INativeDao;
import by.gomel.freedev.ucframework.ucdao.interfaces.IReferencesDao;

/**
 * Фабрика слоя DAO. Возвращает необходимые интерфейсы для работы с БД
 * @author andy-linux

 */
public class DaoFactory<T> {
    /**
     * Синглтон ссылка на данный объект
     */
    public static DaoFactory instance = new DaoFactory<>();
    /**
     * DAO интерфейс для работы со справочниками
     */
    private IReferencesDao referenceDao;
    /**
     * DAO интерфейс для работы панели управления администратора
     */
    private IAdministrationDao adminDao;
    /**
     * DAO интерфейс стандартных операций с записями в БД
     */
    private ICommonDao commonDao;

    /**
     * DAO интерфейс стандартных операций с записями в БД
     */
    private ICommonDaoThread commonDaoThread;

    private INativeDao nativeDao;

    /**
     * DAO интерфейс операций с дженерик типами в БД
     */
    private IGenericDaoGUI<T> genericDao;


/*
    private IPureCommonDao<T> iPureCommonDao ;
    private IPureCommonDaoThread<T> iPureCommonDaoThread ;
*/

    /**
     * Возвращает DAO фабрику
     */
    public static <T> DaoFactory<T> getInstance() {
        return DaoFactory.instance;
    }

    /**
     * Возвращает интерфейс DAO панели управления администратора
     */
    public IAdministrationDao getAdministrationDao() {
        if (adminDao == null) {
            adminDao = new AdministrationDao();
        }
        return adminDao;
    }

    /**
     * Возвращает интерфейс DAO справочника
     */
    public IReferencesDao getReferencesDao() {
        if (referenceDao == null) {
            referenceDao = new ReferencesDao();
        }
        return referenceDao;
    }

    /**
     * Возвращает интерфейс DAO стандрартных операций с записями
     */
    public ICommonDao getCommonDao() {
        if (commonDao == null) {
            commonDao = new CommonDao();
        }
        return commonDao;
    }

    public ICommonDaoThread getCommonDaoThread() {
        if (commonDaoThread == null) {
            commonDaoThread = new CommonDao();
        }
        return commonDaoThread;
    }

    public IGenericDaoGUI<T> getGenericDao() {
        if (genericDao == null) {
            genericDao = new GenericDao<T>();
        }
        return genericDao;
    }

    public INativeDao getNativeDao() {
        if (nativeDao == null) {
            nativeDao = new NativeDAO();
        }
        return nativeDao;
    }


/*    public IPureCommonDao getPureCommonDao(){
        if(iPureCommonDao==null){
            iPureCommonDao = new PureCommonDao() ;
        }
        return iPureCommonDao;
    }

    public IPureCommonDaoThread getPureCommonDaoThread(){
        if(iPureCommonDaoThread==null){
            iPureCommonDaoThread = new PureCommonDao() ;
        }
        return iPureCommonDaoThread;
    }*/
}
