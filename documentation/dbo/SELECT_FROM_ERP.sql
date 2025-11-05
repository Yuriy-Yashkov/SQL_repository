SELECT ABS(CHECKSUM(NEWID())) AS ID,nsi_kld.ngpr AS ITEM_NAME, 
ARTICLE, MODEL, nsi_sd.rzm_print AS SIZE_PRINT, 
NSI_COLOR,  nsi_sd.kod AS KLD_ID, SD_ID, BALANCE
FROM ERP_REMAINS 
INNER JOIN nsi_sd on nsi_sd.kod1 = SD_ID
INNER JOIN nsi_kld on nsi_kld.kod = nsi_sd.kod
WHERE ((ARTICLE like '5С%') or (ARTICLE like '9С%'))
ORDER BY MODEL, ARTICLE, nsi_sd.rzm, nsi_sd.rst, nsi_sd.srt
