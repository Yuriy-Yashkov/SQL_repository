-- Маршрутный лист: пошив
-- Использоать таблицы: nsi_kld и poshiv
-- Условие запроса: kod_marh = 602529

SELECT nsi_kld.nar                               'Артикул',
       nsi_kld.ngpr                              'Наименование',
       nsi_kld.sar                               'Артикул',
       nsi_kld.fas                               'Номер модели',
       poshiv.ncw                                'Цвет',
       poshiv.srt                                'Сорт',
       poshiv.rzm                                'Размер',
       poshiv.rst                                'Рост',
       poshiv.kol_sdano                          'Кол-во',
       poshiv.brigada                            'Бригада',
       poshiv.kod_marh                           'Код листа',
       CONVERT(varchar(10), poshiv.data, 104) AS 'Дата пошива'
FROM Gomel.dbo.nsi_kld nsi_kld
         JOIN Gomel.dbo.poshiv AS poshiv
              ON nsi_kld.kod = poshiv.kod_izd
WHERE     poshiv.kod_marh = 602529
  AND poshiv.kol_sdano > 0
ORDER BY poshiv.rzm;
