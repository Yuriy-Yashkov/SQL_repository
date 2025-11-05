SELECT s_klient.NAIM, kpl, SUM(summa_all)AS TOTAL_SUM, valuta_id FROM otgruz1 
LEFT JOIN s_klient ON s_klient.KOD = kpl
LEFT JOIN DRIVING_DIRECTION_DOCUMENT ON DRIVING_DIRECTION_DOCUMENT.SALE_DOCUMENT_ID = otgruz1.item_id
WHERE status = 0 and operac='Отгрузка покупателю' and export = 1 AND DRIVING_DIRECTION_DOCUMENT.DOCUMENT_DATE BETWEEN GETDATE()-31 AND GETDATE()
GROUP BY s_klient.NAIM,kpl, valuta_id
ORDER BY  valuta_id, s_klient.NAIM, kpl