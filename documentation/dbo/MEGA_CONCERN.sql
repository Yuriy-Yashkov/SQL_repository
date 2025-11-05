select ot1.export AS EXPORT, SUBSTRING(CONVERT(varchar(10), CL.ARTICLE_CODE),1,3) AS CATEGORY,  sum(ot2.kol  * ot2.kol_in_upack) AS AMOUNT , sum(ot2.itogo) AS ITOGO

from otgruz1 as ot1
LEFT JOIN otgruz2 ot2 on ot2.doc_id = ot1.item_id 
LEFT JOIN VIEW_NSI_CLASSIFIER as CL ON CL.ITEM_ID = ot2.kod_izd

WHERE ot1.[date] BETWEEN CONVERT(DATETIME, '2019-01-01', 102) and CONVERT(DATETIME, '2019-08-31', 102) 
and ot1.status = 0 
and (ot1.operac='Отгрузка покупателю' or ot1.operac='Перемещение в розницу' OR operac='Отгрузка материала')

GROUP BY ot1.export , SUBSTRING(CONVERT(varchar(10), CL.ARTICLE_CODE),1,3)
ORDER BY ot1.export, SUBSTRING(CONVERT(varchar(10), CL.ARTICLE_CODE),1,3)
