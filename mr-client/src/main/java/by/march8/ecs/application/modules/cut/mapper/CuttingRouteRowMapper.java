package by.march8.ecs.application.modules.cut.mapper;

import by.march8.ecs.application.modules.cut.model.CuttingRouteRow;

public class CuttingRouteRowMapper {

    public CuttingRouteRow mapRow(Object[] row) {
        return new CuttingRouteRow(
                row[0].toString(),   // Артикул (nar)
                row[1].toString(),   // Наименование (ngpr)
                row[2].toString(),   // Артикул2 (sar)
                row[3].toString(),   // Номер модели (fas)
                row[4].toString(),   // Цвет (ncw)
                row[5].toString(),   // Размер (rzm)
                row[6].toString(),   // Рост (rst)
                row[7].toString(),   // Кол-во (kol)
                row[8].toString(),   // Бригада (brig_otpr)
                row[9].toString(),   // Бригадир
                row[10].toString(),   // Код листа (kod_marh)
                row[11].toString()   // Дата пошива (CONVERT(...))
        );
    }
}
