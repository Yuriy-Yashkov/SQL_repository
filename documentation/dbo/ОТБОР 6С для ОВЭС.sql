SELECT
sum(otgruz2.itogo),
SUM(otgruz2.kol* otgruz2.kol_in_upack),
--dbo.nsi_kld.ngpr AS ITEM_NAME,
--dbo.nsi_kld.nar AS ARTICLE_NUMBER,
--dbo.nsi_kld.fas AS MODEL_NUMBER,
SUBSTRING(CONVERT(varchar(10), dbo.nsi_kld.sar),1,3) AS CATEGORY

FROM
dbo.otgruz1
LEFT JOIN dbo.otgruz2 ON dbo.otgruz1.item_id = dbo.otgruz2.doc_id
LEFT JOIN dbo.nsi_sd ON dbo.otgruz2.kod_izd = dbo.nsi_sd.kod1
LEFT JOIN dbo.nsi_kld ON dbo.nsi_sd.kod = dbo.nsi_kld.kod

WHERE operac = 'Отгрузка покупателю' and status = 0 and export = 1 and nsi_kld.nar like '7С%' and [date] BETWEEN '2019-05-01' AND '2019-05-30'

GROUP BY SUBSTRING(CONVERT(varchar(10), dbo.nsi_kld.sar),1,3)
ORDER BY SUBSTRING(CONVERT(varchar(10), dbo.nsi_kld.sar),1,3)