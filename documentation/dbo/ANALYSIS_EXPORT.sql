SELECT o1.[date], (o2.kol*kol_in_upack) as amount ,itogo, itogov, CL.ARTICLE_CODE FROM otgruz2 o2
LEFT JOIN otgruz1 as o1 on o1.item_id = o2.doc_id
LEFT JOIN VIEW_NSI_CLASSIFIER as CL ON CL.ITEM_ID = o2.kod_izd
WHERE ((o1.operac = 'Отгрузка покупателю' OR o1.operac = 'Отгрузка материала') AND o1.export = 1 and status = 0 and adjustment_type<2) and (o1.[date]>'2017-01-01' and o1.[date]<='2020-01-01')
