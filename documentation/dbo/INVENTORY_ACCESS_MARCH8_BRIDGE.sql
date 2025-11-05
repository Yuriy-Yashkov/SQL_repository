--select * from [ОБОРОТ_ГОТ] WHERE [Артикул] = 41111110 AND [Месяц] = 8 AND [Сорт]=1 ORDER BY [Размер], [Рост]
--select * from [ОБОРОТ_ГОТ] WHERE [Артикул] = 43347627 AND [Месяц] = 8 AND [Сорт]=1 ORDER BY [Размер], [Рост]
--select count(*) from [ОБОРОТ_ГОТ] WHERE [Месяц] = 8

select 
nsi_kld.ngpr AS ITEM_NAME,
nsi_kld.nar AS ARTICLE_NUMBER,
[Модель] AS MODEL_NUMBER,
[Артикул] AS ARTICLE_CODE,
[Сорт] AS GRADE,
[Размер_н] AS MIN_SIZE,
[Размер_к]AS MAX_SIZE,
[Рост_н] AS MIN_GROWTH,
[Рост_к] AS MAX_GROWTH,
[Цена_ценник] AS PRICE,
sum([Ост_кол]) AS AMOUNT,
sum([Ост_сум_бн])AS COST
from [ОБОРОТ_ГОТ]
LEFT JOIN nsi_kld ON nsi_kld.sar = [Артикул]
WHERE [Месяц]=8 AND [Артикул] = 42519840
GROUP BY nsi_kld.ngpr,nsi_kld.nar,[Модель],[Артикул],[Сорт],[Размер_н],[Размер_к],[Рост_н],[Рост_к], [Цена_ценник]
ORDER BY nsi_kld.ngpr,[Модель],[Артикул],[Сорт],[Размер_н],[Размер_к],[Рост_н],[Рост_к], [Цена_ценник]