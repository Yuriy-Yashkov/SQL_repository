package by.march8.ecs.application.modules.cut.mapper;

import by.march8.ecs.application.modules.cut.model.SewingRouteRow;

public class SewingRouteRowMapper {

    public SewingRouteRow mapRow(Object[] row) {
        return new SewingRouteRow(
                row[0].toString(),   // Артикул (nar)
                row[1].toString(),   // Наименование (ngpr)
                row[2].toString(),   // Артикул2 (sar)
                row[3].toString(),   // Номер модели (fas)
                row[4].toString(),   // Цвет (ncw)
                row[5].toString(),   // Сорт
                row[6].toString(),   // Размер (rzm)
                row[7].toString(),   // Рост (rst)
                row[8].toString(),   // Кол-во (kol)
                row[9].toString(),   // Бригада (brig_otpr)
                row[10].toString(),   // Бригадир
                row[11].toString(),   // Код листа (kod_marh)
                row[12].toString()   // Дата пошива (CONVERT(...))
        );
    }
}
