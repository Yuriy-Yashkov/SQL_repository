SELECT o1.[date] FROM otgruz1 o1
WHERE ((o1.operac = 'Отгрузка покупателю' OR o1.operac = 'Отгрузка материала') AND o1.export = 1 and status = 0 and adjustment_type<2) and (o1.[date]>'2017-01-01' and o1.[date]<='2020-01-01')
GROUP BY o1.[date]
ORDER BY o1.[date]
