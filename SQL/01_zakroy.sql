-- Маршрутный лист: закрой
-- Использоать таблицы: nsi_kld, prix_osn или rasx_osn
-- Условие запроса: kod_izd = 55090 AND kod_marh = 602529

SELECT nsi_kld.nar                                 'Артикул',
       nsi_kld.ngpr                                'Наименование',
       nsi_kld.sar                                 'Артикул',
       nsi_kld.fas                                 'Номер модели',
       poshiv.ncw                                  'Цвет',
       rasx_osn.rzm                                'Размер',
       rasx_osn.rst                                'Рост',
       rasx_osn.kol                                'Кол-во',
       rasx_osn.brig_otpr                          'Бригада',
       rasx_osn.kod_marh                           'Код листа',
       CONVERT(varchar(10), rasx_osn.data, 104) AS 'Дата пошива'
FROM Gomel.dbo.nsi_kld nsi_kld
         JOIN (SELECT Distinct kod_izd, ncw
               FROM Gomel.dbo.poshiv) AS poshiv
              ON nsi_kld.kod = poshiv.kod_izd
         JOIN Gomel.dbo.rasx_osn AS rasx_osn
              ON nsi_kld.kod = rasx_osn.kod_izd
WHERE rasx_osn.kod_marh = 602529
  AND rasx_osn.data >= '19-09-2025' -- именно такой способ
  AND rasx_osn.data < '20-09-2025'  -- функции не работают
ORDER BY rasx_osn.rzm;
