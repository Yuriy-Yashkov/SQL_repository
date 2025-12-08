package by.march8.ecs.application.modules.cut.service;

import by.march8.ecs.application.modules.cut.dao.RouteListDao;
import by.march8.ecs.application.modules.cut.mapper.CuttingRouteRowMapper;
import by.march8.ecs.application.modules.cut.mapper.SewingRouteRowMapper;
import by.march8.ecs.application.modules.cut.model.CodeListRow;
import by.march8.ecs.application.modules.cut.model.CuttingRouteRow;
import by.march8.ecs.application.modules.cut.model.SewingRouteRow;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.sql.Date;

@Slf4j
public class RouteListService {

    private final RouteListDao routeListDao = new RouteListDao();
    private final CuttingRouteRowMapper cuttingRouteRowMapper = new CuttingRouteRowMapper();
    private final SewingRouteRowMapper sewingRouteRowMapper = new SewingRouteRowMapper();

    public List<CodeListRow> findCodeList(LocalDate dateFrom, LocalDate dateTo, long codeList) {
        Date sqlDateFrom = Date.valueOf(dateFrom);
        Date sqlDateTo = Date.valueOf(dateTo);
        List<CodeListRow> result = new ArrayList<>();

        List<Object[]> raw = routeListDao.findCodeList(sqlDateFrom, sqlDateTo, codeList);

        log.info("Входящие данные: Дата от: {}, Дата до: {}, Код: {}", new Object[]{dateFrom, dateTo, codeList});

        for (Object[] row : raw) {
            log.info("Результат запроса главной таблицы: {},  {}", row[0].toString(), row[1].toString());
            String dateFromOut = row[0].toString();
            String dateToOut = row[1].toString();

            result.add(new CodeListRow(dateFromOut, dateToOut));
        }

        return result;
    }

    public List<CuttingRouteRow> findRouteList(LocalDate dateFrom, LocalDate dateTo, long codeList) {
        Date sqlDateFrom = Date.valueOf(dateFrom);
        Date sqlDateTo = Date.valueOf(dateTo);
        List<CuttingRouteRow> result = new ArrayList<>();

        List<Object[]> raw = routeListDao.findRouteList(sqlDateFrom, sqlDateTo, codeList);

        log.info("Входящие данные: Дата от: {}, Дата до: {}, Код: {}", new Object[]{dateFrom, dateTo, codeList});

        for (Object[] row : raw) {
            log.info("Результат запроса CuttingRouteRow таблицы: {},  {}", row[0].toString(), row[1].toString());

            result.add(cuttingRouteRowMapper.mapRow(row));
        }

        return result;
    }

    public List<SewingRouteRow> findSewingList(long codeList) {
        List<SewingRouteRow> result = new ArrayList<>();

        List<Object[]> raw = routeListDao.findSewingList(codeList);

        log.info("Входящие данные: Код: {}", codeList);

        for (Object[] row : raw) {
            log.info("Результат запроса CuttingRouteRow таблицы: {},  {}", row[0].toString(), row[1].toString());

            result.add(sewingRouteRowMapper.mapRow(row));
        }

        return result;
    }
}
