package by.march8.ecs.application.modules.warehouse.external.shipping.dao;

/**
 * Константный класс SQL запросов для работы с данными накладных
 * Created by Andy on 07.08.2015.
 */
public final class SaleDocumentQueries {

    /**
     * Получение всех документов за период без типа
     */
    public static final String allSaleDocumentsByPeriod =
            "select date, ndoc, operac, kpl, NAIM, summa, summa_nds, summa_all, status,item_id, saved, klient_id, dogovor_id, adjustment_type, adjustment_ndoc, sale_date  from (select date, ndoc, operac, kpl, summa, summa_nds, summa_all, "
                    + "status = case "
                    + "when status = 0 then 'Закрыт' "
                    + "when status = 1 then 'Удалён' "
                    + "when status = 8 then 'Непонятно =)' "
                    + "when status = 3 then 'Формируется' "
                    + " end, item_id, saved, klient_id, dogovor_id, adjustment_type, adjustment_ndoc, sale_date  from otgruz1 where ((date >= CONVERT(DATETIME, ?, 102))and(date <= CONVERT(DATETIME, ?, 102)))and(adjustment_type<>2)"
                    + ") as t1 join (select KOD, NAIM from s_klient)  as t2 on t1.kpl = t2.KOD order by date";

    public static final String allSaleDocumentsByPeriodAndCurr =
            "select date, ndoc, operac, kpl, NAIM, summa, summa_nds, summa_all, status,item_id, saved, klient_id, dogovor_id, adjustment_type, adjustment_ndoc, sale_date  from (select date, ndoc, operac, kpl, summa, summa_nds, summa_all, "
                    + "status = case "
                    + "when status = 0 then 'Закрыт' "
                    + "when status = 1 then 'Удалён' "
                    + "when status = 8 then 'Непонятно =)' "
                    + "when status = 3 then 'Формируется' "
                    + " end, item_id, saved, klient_id, dogovor_id, adjustment_type, adjustment_ndoc, sale_date  from otgruz1 where ((date >= CONVERT(DATETIME, ?, 102))and(date <= CONVERT(DATETIME, ?, 102)))and(export=?)and(adjustment_type<>2)"
                    + ") as t1 join (select KOD, NAIM from s_klient)  as t2 on t1.kpl = t2.KOD order by date";

    /**
     * Получение всех документов за период и определенного типа
     */
    public static final String allSaleDocumentsByPeriodAndType =
            "select date, ndoc, operac, kpl, NAIM, summa, summa_nds, summa_all, status,item_id, saved, klient_id, dogovor_id, adjustment_type, adjustment_ndoc, sale_date  from (select date, ndoc, operac, kpl, summa, summa_nds, summa_all, "
                    + "status = case "
                    + "when status = 0 then 'Закрыт' "
                    + "when status = 1 then 'Удалён' "
                    + "when status = 8 then 'Непонятно =)' "
                    + "when status = 3 then 'Формируется' "
                    + " end, item_id, saved, klient_id, dogovor_id, adjustment_type, adjustment_ndoc, sale_date  from otgruz1 where ((date >= CONVERT(DATETIME, ?, 102))and(date <= CONVERT(DATETIME, ?, 102)))and(operac=?)and(adjustment_type<>2)"
                    + ") as t1 join (select KOD, NAIM from s_klient)  as t2 on t1.kpl = t2.KOD order by date";

    public static final String allSaleDocumentsByPeriodAndTypeAndCurr =
            "select date, ndoc, operac, kpl, NAIM, summa, summa_nds, summa_all, status,item_id, saved, klient_id, dogovor_id, adjustment_type, adjustment_ndoc, sale_date  from (select date, ndoc, operac, kpl, summa, summa_nds, summa_all, "
                    + "status = case "
                    + "when status = 0 then 'Закрыт' "
                    + "when status = 1 then 'Удалён' "
                    + "when status = 8 then 'Непонятно =)' "
                    + "when status = 3 then 'Формируется' "
                    + " end, item_id, saved, klient_id, dogovor_id, adjustment_type, adjustment_ndoc, sale_date  from otgruz1 where ((date >= CONVERT(DATETIME, ?, 102))and(date <= CONVERT(DATETIME, ?, 102)))and(operac=?)and(export=?)and(adjustment_type<>2)"
                    + ") as t1 join (select KOD, NAIM from s_klient)  as t2 on t1.kpl = t2.KOD order by date";


    /**
     * Получение спецификации документа , основное
     */

    public static final String detailForReports = "SELECT sar AS article_code, " +
            "nar AS article_number, " +
            "fas AS model_number, " +
            "ngpr AS item_name, " +
            "narp AS tnvd_code," +
            "eancode AS ean_code, " +
            "ncw AS item_color, " +
            "rst AS item_growth, " +
            "rzm AS item_size, " +
            "rzm_print AS item_size_print, " +
            "srt AS item_grade,  " +
            "sum(amount) AS amount, " +
            "sum(amount_all) AS amount_all, " +
            "cena_uch AS accounting_price, " +
            "nds AS vat, " +
            "cena AS price, " +
            "sum(summa) AS sum_cost, " +
            "sum(summa_nds)AS sum_vat, " +
            "sum(itogo) AS sum_cost_vat," +
            "cenav AS price_currency, " +
            "sum(summav) AS sum_cost_currency, " +
            "sum(summa_ndsv) AS sum_vat_currency, " +
            "sum(itogov) AS sum_cost_vat_currency," +
            "CASE " +
            "    WHEN torg_nadb IS NULL THEN 0" +
            "    ELSE torg_nadb " +
            "    END AS trade_markup," +
            "CASE " +
            "    WHEN rozn_cena IS NULL THEN 0" +
            "    ELSE rozn_cena" +
            "    END AS retail_price, " +
            "sum(mas) AS weight, " +
            "preiscur AS price_list," +
            "ndoc, id ,0 as cd_id, ptk, komplekt AS composition, vzr, prim  " +
            "FROM " +
            "(SELECT item_id, ndoc FROM otgruz1 WHERE ndoc = ? ) AS ot1 " +
            "LEFT JOIN (SELECT item_id as id,kod_izd, doc_id, (kol*kol_in_upack) AS amount_all, kol as amount, cena, summa, summa_nds, itogo, eancode, preiscur, ncw, summav, summa_ndsv, itogov, cenav, nds, cena_uch FROM otgruz2) AS ot2 ON ot2.doc_id= ot1.item_id " +
            "LEFT JOIN (SELECT kod1, kod, rzm_print, massa AS mas, srt, rst, rzm, vzr FROM nsi_sd) AS sd ON sd.kod1= ot2.kod_izd " +
            "LEFT JOIN (SELECT kod, sar, nar, fas, ngpr, narp, ptk, komplekt, prim FROM nsi_kld) AS kld ON sd.kod= kld.kod " +
            "LEFT OUTER JOIN (SELECT torg_nadb, rozn_cena, id_item FROM _otgruz2_addition) AS trade ON trade.id_item= ot2.id " +
            //"left join (select cw as cd_id, ncw as cname from nsi_cd) as cd on ltrim(rtrim(cd.cname))=ltrim(rtrim(ot2.ncw)) " +

            "GROUP BY sar, nar, fas, ngpr, narp, ncw, rst,rzm_print, rzm, srt, eancode, cena_uch, cena, nds,preiscur, cenav,torg_nadb, rozn_cena, ndoc, kod_izd, id , ptk, komplekt, vzr, prim " +
            "ORDER BY sar, nar, fas, rst, rzm ";

    /**
     * ПОлучение спецификации с группировкой, для бригадира, скорее всего будет убрано
     */
    public static final String detailForReportsPack = "SELECT sar AS article_code, " +
            "nar AS article_number, " +
            "fas AS model_number, " +
            "ngpr AS item_name, " +
            "narp AS tnvd_code," +
            "eancode AS ean_code, " +
            "ncw AS item_color, " +
            "rst AS item_growth, " +
            "rzm AS item_size, " +
            "rzm_print AS item_size_print, " +
            "srt AS item_grade,  " +
            "sum(amount) AS amount, " +
            "sum(amount_all) AS amount_all, " +
            "cena_uch AS accounting_price, " +
            "nds AS vat, " +
            "cena AS price, " +
            "sum(summa) AS sum_cost, " +
            "sum(summa_nds)AS sum_vat, " +
            "sum(itogo) AS sum_cost_vat," +
            "cenav AS price_currency, " +
            "sum(summav) AS sum_cost_currency, " +
            "sum(summa_ndsv) AS sum_vat_currency, " +
            "sum(itogov) AS sum_cost_vat_currency," +
            "CASE " +
            "    WHEN torg_nadb IS NULL THEN 0" +
            "    ELSE torg_nadb " +
            "    END AS trade_markup," +
            "CASE " +
            "    WHEN rozn_cena IS NULL THEN 0" +
            "    ELSE rozn_cena" +
            "    END AS retail_price," +
            "sum(mas) AS weight, " +
            "preiscur AS price_list," +
            " ndoc, id ,0 as cd_id, ptk , komplekt AS composition , vzr, prim, " +
            //"ndoc, id ,0 as cd_id, ptk , komplekt AS composition , vzr, prim, pc_ins as disc "+
            " CASE ISNUMERIC(pc_ins) WHEN 1 THEN cast(ltrim(rtrim(pc_ins)) as numeric(18,2)) WHEN 0 THEN 0 END as disc  " +
            " FROM " +
            "(SELECT item_id, ndoc FROM otgruz1 WHERE ndoc = ? ) AS ot1 " +
            "LEFT JOIN (SELECT item_id as id,kod_izd, doc_id, (kol*kol_in_upack) AS amount_all, kol as amount, cena, summa, summa_nds, itogo, eancode, preiscur, ncw, summav, summa_ndsv, itogov, cenav, nds, cena_uch, pc_ins FROM otgruz2) AS ot2 ON ot2.doc_id= ot1.item_id " +
            "LEFT JOIN (SELECT kod1, kod, rzm_print, massa AS mas, srt, rst, rzm, vzr FROM nsi_sd) AS sd ON sd.kod1= ot2.kod_izd " +
            "LEFT JOIN (SELECT kod, sar, nar, fas, ngpr, narp, ptk, komplekt, prim FROM nsi_kld) AS kld ON sd.kod= kld.kod " +
            "LEFT OUTER JOIN (SELECT torg_nadb, rozn_cena, id_item FROM _otgruz2_addition) AS trade ON trade.id_item= ot2.id " +
            // "left join (select cw as cd_id, ncw as cname from nsi_cd) as cd on ltrim(rtrim(cd.cname))=ltrim(rtrim(ot2.ncw)) " +

            "GROUP BY sar, nar, fas, ngpr, narp, ncw, rst,rzm_print, rzm, srt, eancode, cena_uch, cena, nds,preiscur, cenav,torg_nadb, rozn_cena, ndoc, id, ptk, komplekt, vzr, prim, pc_ins " +
            "ORDER BY sar, nar, fas, rst, rzm";

    /**
     * Получение информации о банке контрагента по коду(не ID) контрагента
     */
    public static final String getBankInformationByContractorCode = "select top 1 isnull(kl.NAIM,'') as NAIM,isnull(TELEFON,'') as TELEFON,isnull(rs.NAIM,'') as NAIMRS,isnull(rs.NOMER,'') as NOMER, " +
            "isnull(bankId.NAIM,'') as NAIMBANK, isnull(bankId.PADRES,'') as PADRES,isnull(bankId.MFO,'') as MFO,isnull(bankId.KORSCHET,'') as KORSCHET, " +
            "isnull(bankId.KORUNN,'') as KORUNN, isnull(kl.OKPO,'') as OKPO from s_klient as kl " +
            "left join s_adres as ad on ad.ITEM_ID=kl.POSTADRES " +
            "left join s_rschet as rs on rs.KLIENT_ID=kl.ITEM_ID " +
            " left join s_bank as bankId on bankId.ITEM_ID=rs.BANK_ID " +
            "where KOD=?";

    /**
     * ПОлучение информации о накладной из старой ветки MyReports, будет убрано
     */
    public static final String querySaleDocumentDrivingOld = "select " +
            "ndoc as document_number," +
            "date as document_date," +
            "ppogruzki as loading_address," +
            "prazgruzki as unloading_address," +
            "pereadres as readdressing," +
            "otpustil as sale_allowed," +
            "otpustil as sale_manager," +
            "sdal as shipper_passed," +
            "prinial as transportation_receive," +
            "nplombi as seal_number," +
            "ispol_pogruz as loading_doer," +
            "sposob as loading_method," +
            "doc as support_document," +
            "primechanie as note," +
            "ndover as warrant_number," +
            "dover_date as warrant_date," +
            "dover_vidan as warrant_issued," +
            "dover_prinial as warrant_receive," +
            "dover_plomba as warrant_seal_number," +
            "avto as car_number, " +
            "pritsep as car_trailer_number," +
            "voditel as car_driver_name," +
            "vlad_avto as car_owner," +
            "schot_platelschik as car_payer," +
            "zakaz_avto as car_customer, " +
            "nds as vat " +
            "from _put_list where ttn_id = ?";

    /**
     * Запрос на получение цен из классификатора для изделий в документе
     */
    public static final String queryGetClassifierPriceByDocumentNumber = "select item_id as id, isnull(vat,0) as vat, isnull(price,0) as price, isnull(price_currency,0)as price_currency, isnull(price_retail,0)as price_retail from otgruz2 " +
            "LEFT JOIN (select nds as vat,cno as price, cnp as price_currency, cnr as price_retail,  kod1 from nsi_sd) as sd on sd.kod1 = otgruz2.kod_izd " +
            "WHERE doc_id = ?";


    public static final String queryGetProductionItemByDocument = "select item_id as id, kod_izd as item_code, model, article_name, article_code  from otgruz2 " +
            "LEFT JOIN (select kod1, kod from nsi_sd) as sd on sd.kod1 = otgruz2.kod_izd " +
            "LEFT JOIN (select kod as item, fas as model, nar as article_name, sar as article_code from nsi_kld) as kld on kld.item = sd.kod " +
            "WHERE doc_id = ? ";


    /**
     * Получение номеров закрытых документов без розницы и материалов
     */
    public static final String queryGetDocsForInvoices = "select driving.DOCUMENT_NUMBER, otgruz.ndoc, otgruz.item_id, otgruz.status, otgruz.operac, otgruz.export, adjustment_type, adjustment_ndoc from DRIVING_DIRECTION_DOCUMENT as driving " +
            "JOIN (select item_id, ndoc, status, operac, export, adjustment_type, adjustment_ndoc from otgruz1 )as otgruz on otgruz.item_id  = driving.SALE_DOCUMENT_ID " +
            "where ((driving.DOCUMENT_DATE >= CONVERT(DATETIME, ?, 102))and(driving.DOCUMENT_DATE <= CONVERT(DATETIME, ?, 102)))and(otgruz.status = 0)" +
            "and (operac='Отгрузка покупателю' or operac='Возврат от покупателя' or operac='Отгрузка материала' or operac='Возврат материала')and(adjustment_type<>2)";

    public static final String queryGetDocsForInvoicesByContractor = "select driving.DOCUMENT_NUMBER, otgruz.ndoc, otgruz.item_id, otgruz.status, otgruz.operac, otgruz.export, adjustment_type, adjustment_ndoc from DRIVING_DIRECTION_DOCUMENT as driving " +
            "JOIN (select item_id, ndoc, status, operac, export,kpl, adjustment_type, adjustment_ndoc from otgruz1 )as otgruz on otgruz.item_id  = driving.SALE_DOCUMENT_ID " +
            "where ((driving.DOCUMENT_DATE >= CONVERT(DATETIME, ?, 102))and(driving.DOCUMENT_DATE <= CONVERT(DATETIME, ?, 102)))and(otgruz.status = 0)and(adjustment_type<>2)" +
            "and (operac='Отгрузка покупателю') and (kpl=?)";

    public static final String queryGetAllDocsForInvoicesByContractor = "select driving.DOCUMENT_NUMBER, otgruz.ndoc, otgruz.item_id, otgruz.status, otgruz.operac, otgruz.export, adjustment_type, adjustment_ndoc from DRIVING_DIRECTION_DOCUMENT as driving " +
            "JOIN (select item_id, ndoc, status, operac, export,kpl, adjustment_type, adjustment_ndoc from otgruz1 )as otgruz on otgruz.item_id  = driving.SALE_DOCUMENT_ID " +
            "where ((driving.DOCUMENT_DATE >= CONVERT(DATETIME, ?, 102))and(driving.DOCUMENT_DATE <= CONVERT(DATETIME, ?, 102)))and(otgruz.status <> 1)and(adjustment_type<>2)" +
            "and (operac='Отгрузка покупателю' or operac='Перемещение в розницу' ) and (kpl=?) ORDER BY driving.DOCUMENT_DATE ASC ";

    /**
     * Получение номеров всех закрытых документов
     */
    public static final String queryGetDocsForInvoicesAll = "select driving.DOCUMENT_NUMBER, otgruz.ndoc, otgruz.item_id, otgruz.status, otgruz.operac, otgruz.export, adjustment_type, adjustment_ndoc from DRIVING_DIRECTION_DOCUMENT as driving " +
            "JOIN (select item_id, ndoc, status, operac, export, adjustment_type, adjustment_ndoc from otgruz1 )as otgruz on otgruz.item_id  = driving.SALE_DOCUMENT_ID " +
            "where ((driving.DOCUMENT_DATE >= CONVERT(DATETIME, ?, 102))and(driving.DOCUMENT_DATE <= CONVERT(DATETIME, ?, 102)))and(otgruz.status = 0)and(adjustment_type<>2)";


    /**
     * Получение номеров всех закрытых документов
     */
    public static final String queryGetMaterialDocsForInvoicesAll = "select driving.DOCUMENT_NUMBER, otgruz.ndoc, otgruz.item_id, otgruz.status, otgruz.operac, otgruz.export, adjustment_type, adjustment_ndoc from DRIVING_DIRECTION_DOCUMENT as driving " +
            "JOIN (select item_id, ndoc, status, operac, export, adjustment_type, adjustment_ndoc from otgruz1 )as otgruz on otgruz.item_id  = driving.SALE_DOCUMENT_ID " +
            "where ((driving.DOCUMENT_DATE >= CONVERT(DATETIME, ?, 102))and(driving.DOCUMENT_DATE <= CONVERT(DATETIME, ?, 102)))and(otgruz.status = 0) and (operac='Отгрузка материала')and(adjustment_type<>2)";

    /**
     * Получение ID документа по номеру
     */
    public static final String queryGetDocumentIdByDocumentNumber = "select item_id from otgruz1 where ndoc = ?";

    public static final String queryGetOpenedDocumentIdByDocumentNumber = "select item_id from otgruz1 where ndoc = ? and status = 3";

    /**
     * Получение всех сгруппированных грузомест по ID документа
     */
    public static final String queryGetCargoSpaceByDocumentNumber = "select item_id as id, doc_id as document_id, space_load_count as cargo_space from otgruz2_space_load where doc_id = ?";


    public static final String sqlGetItemByEanCode = "select sd.kod1, kld.ngpr,kld.fas,kld.nar,kld.sar,kld.narp,sd.cno,sd.nds,sd.rzm_print, sd.srt,sd.massa, " +
            "sd.preiscur,kld.ptk, sd.rst, sd.rzm, sd.vzr from nsi_sd sd " +
            "left join (select ngpr,narp, fas, nar, sar, kod, ptk from nsi_kld) as kld on sd.kod = kld.kod " +
            "where sd.ean = ?";

    public static final String sqlRetailInventoryByPeriodAndContractor = "SELECT driving.DOCUMENT_DATE, otgruz.ndoc AS DOCUMENT_NUMBER, otgruz.item_id AS DOCUMENT_ID " +
            "FROM DRIVING_DIRECTION_DOCUMENT as driving " +
            "JOIN (select item_id, ndoc, status, operac, export, kpl from otgruz1 )as otgruz on otgruz.item_id  = driving.SALE_DOCUMENT_ID " +
            "WHERE ((driving.DOCUMENT_DATE >= CONVERT(DATETIME, ?, 102))and(driving.DOCUMENT_DATE <= CONVERT(DATETIME, ?, 102)))and(otgruz.status = 0) and (kpl=?) and (operac = 'Перемещение в розницу') " +
            "ORDER BY driving.DOCUMENT_DATE ";

    public static final String sqlInventoryByPeriodAndContractorAndUAddress = "SELECT driving.DOCUMENT_DATE, otgruz.ndoc AS DOCUMENT_NUMBER, otgruz.item_id AS DOCUMENT_ID " +
            "FROM DRIVING_DIRECTION_DOCUMENT as driving " +
            "JOIN (select item_id, ndoc, status, operac, export, kpl from otgruz1 )as otgruz on otgruz.item_id  = driving.SALE_DOCUMENT_ID " +
            "WHERE ((driving.DOCUMENT_DATE >= CONVERT(DATETIME, ?, 102))and(driving.DOCUMENT_DATE <= CONVERT(DATETIME, ?, 102)))and(otgruz.status = 0) and (kpl=?) and (operac = 'Отгрузка покупателю') " +
            "and (driving.UNLOADING_ADDRESS_ID = ?) " +
            "ORDER BY driving.DOCUMENT_DATE ";


    public static final String sqlPreOrderSaleDocument = "SELECT sar AS article_code, " +
            "            nar AS article_number, " +
            "            fas AS model_number, " +
            "            ngpr AS item_name, " +
            "            narp AS tnvd_code, " +
            "            VIEW_COLOR_EANCODE.EANCODE AS ean_code, " +
            "            COLOR AS item_color, " +
            "            rst AS item_growth, " +
            "            rzm AS item_size, " +
            "            rzm_print AS item_size_print, " +
            "            srt AS item_grade,  " +
            "            sum(AMOUNT) AS amount, " +
            "            sum(AMOUNT) AS amount_all, " +
            "            ACCOUNTING_PRICE_VALUE AS accounting_price, " +
            "            VAT_VALUE AS vat, " +
            "            COST AS price, " +
            "            sum(SUM_COST) AS sum_cost, " +
            "            sum(SUM_COST_VAT)AS sum_vat, " +
            "            sum(SUM_COST_VAT) AS sum_cost_vat," +
            "            COST_CURRENCY AS price_currency, " +
            "            sum(SUM_COST_CURRENCY) AS sum_cost_currency, " +
            "            sum(SUM_VAT_CURRENCY) AS sum_vat_currency, " +
            "            sum(SUM_COST_VAT_CURRENCY) AS sum_cost_vat_currency," +
            "            0 AS trade_markup," +
            "            0 AS retail_price, " +
            "            sum(mas) AS weight, " +
            "            '' AS price_list, " +
            "            'ndoc' as ndoc, ID , 0 as cd_id, ptk, komplekt AS composition, vzr, prim, DISCOUNT_VALUE  " +
            "             FROM PRE_ORDER_SALE_DOCUMENT_ITEM as ITEM " +
            "            LEFT JOIN (SELECT kod1, kod, rzm_print, massa AS mas, srt, rst, rzm, vzr FROM nsi_sd) AS sd ON sd.kod1= ITEM.PRODUCT_ID " +
            "            LEFT JOIN (SELECT kod, sar, nar, fas, ngpr, narp, ptk, komplekt, prim FROM nsi_kld) AS kld ON sd.kod= kld.kod " +
            "            LEFT JOIN VIEW_COLOR_EANCODE ON VIEW_COLOR_EANCODE.ITEM_CODE = ITEM.PRODUCT_ID " +
            "            WHERE REF_PRE_ORDER_SALE_DOCUMENT_ID=? AND VIEW_COLOR_EANCODE.COLOR_NAME = COLOR " +

            "            GROUP BY sar, nar, fas, ngpr, narp, VIEW_COLOR_EANCODE.EANCODE, COLOR, rst,rzm_print, rzm, srt, ACCOUNTING_PRICE_VALUE,COST, VAT_VALUE,COST_CURRENCY,PRODUCT_ID, ID , ptk, komplekt, vzr, prim, DISCOUNT_VALUE " +
            "            ORDER BY sar, nar, fas, rst, rzm";

    public static final String sqlSelectSaleDocumentsOVES =
            "SELECT item_id AS ID, date as DOCUMENT_DATE, ndoc AS DOCUMENT_NUMBER, status as STATUS, saved as CALC " +
                    "FROM otgruz1 " +
                    //"LEFT JOIN DRIVING_DIRECTION_DOCUMENT ON DRIVING_DIRECTION_DOCUMENT.SALE_DOCUMENT_ID = otgruz1.item_id " +
                    "WHERE kpl = ? " +
                    "AND (date BETWEEN CONVERT(DATETIME, ?, 102) AND CONVERT(DATETIME, ?, 102)) " +
                    "AND (operac = 'Отгрузка покупателю' OR operac = 'Отгрузка материала') " +
                    "AND status<>1 " +
                    "AND LEN(ndoc)>5 " +
                    "ORDER BY date, ndoc ";

    public static final String sqlSelectClosedSaleDocumentsOVES =
            "SELECT item_id AS ID, date as DOCUMENT_DATE, ndoc AS DOCUMENT_NUMBER, status as STATUS, saved as CALC " +
                    "FROM otgruz1 " +
                    //"LEFT JOIN DRIVING_DIRECTION_DOCUMENT ON DRIVING_DIRECTION_DOCUMENT.SALE_DOCUMENT_ID = otgruz1.item_id " +
                    "WHERE kpl = ? " +
                    "AND (date BETWEEN CONVERT(DATETIME, ?, 102) AND CONVERT(DATETIME, ?, 102)) " +
                    "AND operac = 'Отгрузка покупателю' " +
                    "AND status=0 " +
                    "AND LEN(ndoc)>5 " +
                    "ORDER BY date, ndoc ";

    public static final String sqlSelectRefundSaleDocumentsOVES =
            "SELECT item_id AS ID, date as DOCUMENT_DATE, ndoc AS DOCUMENT_NUMBER, status as STATUS, saved as CALC " +
                    "FROM otgruz1 " +
                    //"LEFT JOIN DRIVING_DIRECTION_DOCUMENT ON DRIVING_DIRECTION_DOCUMENT.SALE_DOCUMENT_ID = otgruz1.item_id " +
                    "WHERE kpl = ? " +
                    "AND (date BETWEEN CONVERT(DATETIME, ?, 102) AND CONVERT(DATETIME, ?, 102)) " +
                    "AND operac = 'Возврат от покупателя' " +
                    "AND status<>1 " +
                    "AND LEN(ndoc)>=5 " +
                    "ORDER BY date, ndoc ";

    public static final String sqlSelectRefundSaleDocuments =
            "SELECT item_id AS ID, date as DOCUMENT_DATE, ndoc AS DOCUMENT_NUMBER " +
                    "FROM otgruz1 " +
                    //"LEFT JOIN DRIVING_DIRECTION_DOCUMENT ON DRIVING_DIRECTION_DOCUMENT.SALE_DOCUMENT_ID = otgruz1.item_id " +
                    "WHERE kpl = ? " +
                    "AND (date BETWEEN CONVERT(DATETIME, ?, 102) AND CONVERT(DATETIME, ?, 102)) " +
                    "AND operac = 'Возврат от покупателя' " +
                    "AND status<>1 " +
                    "AND LEN(ndoc)>=5 " +
                    "ORDER BY date DESC, ndoc ";

    public static final String sqlSelectAnalysisDetailOVES = "SELECT * FROM MEGA_PLAIN_ANALYSIS_NEW " +
            "WHERE SALE_DATE BETWEEN CONVERT(DATETIME, ?, 102) and CONVERT(DATETIME, ?, 102) AND ARTICLE_CO NOT LIKE '48%'";

    public static final String sqlSelectAnalysisDetailOVESTemp = "select ARTICLE_CO, SIZE " +
            "FROM MEGA_PLAIN_ANALYSIS_NEW " +
            "GROUP BY ARTICLE_CO, [SIZE] ";
    public static final String sqlGetArticleName = "select nar from nsi_kld where kod = " +
            "(select kod from nsi_sd where kod1 = ?) ";
}
