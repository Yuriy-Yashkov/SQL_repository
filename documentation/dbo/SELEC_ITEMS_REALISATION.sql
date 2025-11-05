select SUM(kol*kol_in_upack)  from otgruz2
LEFT JOIN VIEW_NSI_CLASSIFIER AS NSI ON NSI.ITEM_ID = otgruz2.kod_izd
LEFT JOIN otgruz1 AS o1 ON o1.item_id = otgruz2.doc_id

WHERE ARTICLE_NUMBER = '8С564Д40' and [date] > '2019-01-01' AND (operac = 'Отгрузка покупателю' OR operac = 'Перемещение в розницу') AND (status = 0)