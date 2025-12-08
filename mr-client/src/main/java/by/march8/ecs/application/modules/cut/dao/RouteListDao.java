package by.march8.ecs.application.modules.cut.dao;

import by.gomel.freedev.ucframework.ucdao.SqlServerMarch8DS;
import by.gomel.freedev.ucframework.ucdao.implementations.BaseDao;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.sql.Date;
import java.util.List;

public class RouteListDao extends BaseDao {

    private static final long DEFAULT_MAX_CODE_LIST = 999_999_999L;

    private static final String FIND_CODE_LIST =
            "SELECT DISTINCT " +
                    "   CONVERT(varchar(10), rasx_osn.data, 104) AS data_poshiva, " +
                    "   rasx_osn.kod_marh AS kod_lista " +
                    "FROM Gomel.dbo.rasx_osn rasx_osn " +
                    "JOIN Gomel.dbo.poshiv poshiv ON rasx_osn.kod_marh = poshiv.kod_marh " +
                    "WHERE rasx_osn.data >= :dateFrom " +
                    "  AND rasx_osn.data < :dateTo " +
                    " AND rasx_osn.kod_marh >= :codeListMin " +
                    " AND rasx_osn.kod_marh <= :codeListMax";

    private static final String FIND_ROUTE_BY_CODE_LIST = "SELECT nsi_kld.nar 'Артикул1'," +
            "        nsi_kld.ngpr                                'Наименование'," +
            "        nsi_kld.sar                                 'Артикул2'," +
            "        nsi_kld.fas                                 'Номер модели'," +
            "        poshiv.ncw                                  'Цвет'," +
            "        rasx_osn.rzm                                'Размер'," +
            "        rasx_osn.rst                                'Рост'," +
            "        rasx_osn.kol                                'Кол-во'," +
            "        rasx_osn.brig_otpr                          'Бригада'," +
            "        marh_list.brigadir                          'Бригадир'," +
            "        rasx_osn.kod_marh                           'Код листа'," +
            "        CONVERT(varchar(10), rasx_osn.data, 104)    'Дата пошива'" +
            " FROM Gomel.dbo.nsi_kld nsi_kld\n" +
            "          JOIN (SELECT Distinct kod_izd, ncw " +
            "                FROM Gomel.dbo.poshiv) AS poshiv " +
            "               ON nsi_kld.kod = poshiv.kod_izd " +
            "          JOIN Gomel.dbo.rasx_osn AS rasx_osn " +
            "               ON nsi_kld.kod = rasx_osn.kod_izd " +
            " JOIN Gomel.dbo.marh_list marh_list ON rasx_osn.kod_marh = marh_list.kod " +
            " WHERE rasx_osn.kod_marh = :codList " +
            "   AND rasx_osn.data >= :dateFrom " +
            "   AND rasx_osn.data < :dateTo " +
            " ORDER BY rasx_osn.rzm; ";
        private static final String FIND_SEWING_BY_CODE_LIST = "SELECT nsi_kld.nar 'Артикул'," +
                "       nsi_kld.ngpr                              'Наименование'," +
                "       nsi_kld.sar                               'Артикул2'," +
                "       nsi_kld.fas                               'Номер модели'," +
                "       poshiv.ncw                                'Цвет'," +
                "       poshiv.srt                                'Сорт'," +
                "       poshiv.rzm                                'Размер'," +
                "       poshiv.rst                                'Рост'," +
                "       poshiv.kol_sdano                          'Кол-во'," +
                "       poshiv.brigada                            'Бригада'," +
                "       poshiv.brigadir                           'Бригадир'," +
                "       poshiv.kod_marh                           'Код листа'," +
                "       CONVERT(varchar(10), poshiv.data, 104)    'Дата пошива'" +
                "FROM Gomel.dbo.nsi_kld nsi_kld " +
                "         JOIN Gomel.dbo.poshiv AS poshiv " +
                "              ON nsi_kld.kod = poshiv.kod_izd " +
                "WHERE     poshiv.kod_marh = :codeList " +
                "  AND poshiv.kol_sdano > 0 " +
                "ORDER BY poshiv.rzm;";

    public List<Object[]> findCodeList(Date dateFrom, Date dateTo, long codeList) {
        long codeListMin = 0;
        long codeListMax;

        EntityManager entityManager = SqlServerMarch8DS.getEntityManager();

        if (codeList == 0) {
            codeListMax = DEFAULT_MAX_CODE_LIST;
        } else {
            codeListMin = codeList;
            codeListMax = codeList;
        }

        try {
            Query query = entityManager.createNativeQuery(FIND_CODE_LIST);

            query.setParameter("dateFrom", dateFrom);
            query.setParameter("dateTo", dateTo);
            query.setParameter("codeListMin", codeListMin);
            query.setParameter("codeListMax", codeListMax);

            return query.getResultList();

        } finally {
            entityManager.close();
        }
    }

    public List<Object[]> findRouteList(Date dateFrom, Date dateTo, long codeList) {
        EntityManager entityManager = SqlServerMarch8DS.getEntityManager();

        try {
            Query query = entityManager.createNativeQuery(FIND_ROUTE_BY_CODE_LIST);

            query.setParameter("codList", codeList);
            query.setParameter("dateFrom", dateFrom);
            query.setParameter("dateTo", dateTo);

            return query.getResultList();

        } finally {
            entityManager.close();
        }
    }

    public List<Object[]> findSewingList(long codeList) {
        EntityManager entityManager = SqlServerMarch8DS.getEntityManager();

        try {
            Query query = entityManager.createNativeQuery(FIND_SEWING_BY_CODE_LIST);

            query.setParameter("codeList", codeList);

            return query.getResultList();

        } finally {
            entityManager.close();
        }
    }
}
