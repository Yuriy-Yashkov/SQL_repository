-- Накопительная ведомость.
-- Использоать таблицы: nsi_kld, poshiv, nsi_sd.
-- Условие запроса: kod_marh = 602529 и промежуток даты.

SELECT nsi_kld.sar,
       nsi_kld.ngpr                                                          AS 'Наименование',
       nsi_kld.nar                                                           AS 'Артикул',
       nsi_kld.fas                                                           AS 'Модель',
       poshiv.kod_izd,
       price.srt                                                                'Сорт',
       SUM(poshiv.kol_sdano)                                                 AS 'План',
--        COUNT(*)                                                              AS 'Кол-во в группе',
--        price.kod,
       price.cno                                                                'Цена оптовая',
       SUM(poshiv.kol_sdano) * price.cno                                     AS 'Сумма без налогов',
       SUM(poshiv.kol_sdano) * price.cno + SUM(poshiv.kol_sdano) * price.cnv AS 'Сумма с налогами'

FROM Gomel.dbo.nsi_kld AS nsi_kld
         JOIN Gomel.dbo.poshiv AS poshiv ON nsi_kld.kod = poshiv.kod_izd
         LEFT JOIN (SELECT DISTINCT kod, srt, cno, cnv
                    FROM Gomel.dbo.nsi_sd
                    GROUP BY kod, srt, rst, rzm, cno, cnv) AS price
                   ON poshiv.kod_izd = price.kod
                       AND poshiv.srt = price.srt
--                        AND poshiv.rst = price.rst
--                        AND poshiv.rzm = price.rzm
WHERE
--         poshiv.kod_marh = 602529
    poshiv.data >= '01-05-2025'
  AND poshiv.data < '01-06-2025'
  AND price.cno <> 0
--   AND (price.srt = 1 OR price.srt = 2)
GROUP BY nsi_kld.sar, nsi_kld.ngpr,
         nsi_kld.nar,
         nsi_kld.fas,
         poshiv.kod_izd,
         price.srt,
         price.kod,
         price.cno,
         price.cnv
ORDER BY nsi_kld.sar, price.srt