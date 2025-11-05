package by.march8.tasks.accounting;

import by.march8.api.utils.DateUtils;
import by.march8.ecs.application.modules.warehouse.external.shipping.manager.SaleDocumentDataProvider;
import by.march8.ecs.application.modules.warehouse.external.shipping.model.SaleDocumentReport;
import by.march8.ecs.framework.common.LogCrutch;
import by.march8.entities.warehouse.InvoiceType;
import by.march8.entities.warehouse.SaleDocumentBase;
import by.march8.entities.warehouse.SaleDocumentDetailItemReport;
import workDB.JDBField;

import java.util.Date;
import java.util.HashMap;

/**
 * @author Andy 30.11.2016.
 */
public class SpecificationDbf extends AbstractDBF {
    private static final LogCrutch log = new LogCrutch();
    private int MAX_ITEM = 32;

    /**
     * Конструктор создания DBF файла
     *
     * @param pathSave путь к файлу, без завершающего слэша
     * @param fileName имя файла, без расширения
     */
    public SpecificationDbf(String fileName, String pathSave) {
        super(pathSave, fileName);
        try {
            fields = new JDBField[MAX_ITEM];
            fields[0] = new JDBField("DOC_NUMB", 'C', 10, 0);
            fields[1] = new JDBField("DOC_DATE", 'D', 8, 0);
            fields[2] = new JDBField("DOC_TYPE", 'C', 50, 0);

            fields[3] = new JDBField("CLNT_C", 'N', 5, 0);
            fields[4] = new JDBField("CLNT_UNN", 'C', 13, 0);

            fields[5] = new JDBField("CONTR_ID", 'N', 8, 0);
            fields[6] = new JDBField("CONTR_NM", 'C', 10, 0);
            fields[7] = new JDBField("CONTR_DT", 'D', 8, 0);

            fields[8] = new JDBField("CURR_CODE", 'C', 3, 0);
            fields[9] = new JDBField("CURR_RATE", 'N', 19, 4);
            fields[10] = new JDBField("PREPAY", 'C', 1, 0);

            fields[11] = new JDBField("ITEM_NAM", 'C', 250, 0);
            fields[12] = new JDBField("ITEM_ART", 'C', 50, 0);
            fields[13] = new JDBField("ITEM_COD", 'C', 50, 0);
            fields[14] = new JDBField("ITEM_TNV", 'C', 50, 0);
            fields[15] = new JDBField("ITEM_MOD", 'C', 8, 0);
            fields[16] = new JDBField("ITEM_GRZ", 'N', 3, 0);
            fields[17] = new JDBField("ITEM_SIZ", 'N', 3, 0);
            fields[18] = new JDBField("ITEM_GRD", 'N', 1, 0);
            fields[19] = new JDBField("ITEM_CLR", 'C', 50, 0);
            fields[20] = new JDBField("IS_CHILD", 'C', 1, 0);

            fields[21] = new JDBField("AMOUNT", 'N', 19, 5);

            fields[22] = new JDBField("PRICE_W", 'N', 19, 5);
            fields[23] = new JDBField("VAT", 'N', 4, 0);

            fields[24] = new JDBField("PRICE_R", 'N', 19, 5);
            fields[25] = new JDBField("SM_R", 'N', 19, 5);
            fields[26] = new JDBField("SM_VAT_R", 'N', 19, 5);
            fields[27] = new JDBField("SM_CST_R", 'N', 19, 5);

            fields[28] = new JDBField("PRICE_C", 'N', 19, 5);
            fields[29] = new JDBField("SM_C", 'N', 19, 5);
            fields[30] = new JDBField("SM_VAT_C", 'N', 19, 5);
            fields[31] = new JDBField("SM_CST_C", 'N', 19, 5);

        } catch (Exception e) {
            log.error("Ошибка при создании списка полей дбф: ", e);
            e.printStackTrace();
        }
    }

    public void addItem(SaleDocumentReport report) {
        final HashMap<String, Object> detailMap = report.getDetailMap();

        // boolean isRefund = SaleDocumentManager.isDocumentRefund(document);
        final SaleDocumentBase documentBase = report.getDocument();

        // Выполняем только для ТД 8Марта Москва
        if (documentBase.getRecipientCode() != 4251) {
            return;
        }


        for (SaleDocumentDetailItemReport item : report.getDetailList()) {
            try {
                Object[] v = new Object[MAX_ITEM];
                v[0] = documentBase.getDocumentNumber();//Номер документа

                Date dateSale;
                try {
                    dateSale = DateUtils.getDateByStringValue((String) detailMap.get("DOCUMENT_DATE"));
                } catch (Exception ed) {
                    dateSale = documentBase.getDocumentSaleDate();
                }

                v[1] = dateSale;//Дата путевого листа
                v[2] = documentBase.getDocumentType(); //Тип документа
                v[3] = documentBase.getRecipientCode();//Код контрагента

                String unp = "";
                // ЕСли накладная возвратная, реквизиты контрагентов меняем местами
                if (documentBase.getDocumentType().equals(InvoiceType.DOCUMENT_REFUND_BUYER) ||
                        documentBase.getDocumentType().equals(InvoiceType.DOCUMENT_REFUND_RETAIL) ||
                        documentBase.getDocumentType().equals(InvoiceType.DOCUMENT_REFUND_MATERIAL)) {

                    unp = (String) detailMap.get("SENDER_UNP");
                } else {
                    unp = (String) detailMap.get("RECIPIENT_UNP");
                }

                v[4] = unp.trim();// УНН контрагента


                v[5] = documentBase.getRecipientContractId();// ID договора

                String contractNumber = (String) detailMap.get("CONTRACT_NUMBER");
                v[6] = contractNumber.trim();// Номер договора
                v[7] = detailMap.get("CONTRACT_DATE_BEGIN_AS_DATE");// Дата договора

                int curr = documentBase.getCurrencyId();
                String currencyType = "BYN";

                switch (curr) {

                    case 2:
                        currencyType = "RUB";
                        break;
                    case 3:
                        currencyType = "USD";
                        break;
                    case 4:
                        currencyType = "EUR";
                        break;
                    case 5:
                        currencyType = "UAH";
                        break;
                }


                v[8] = currencyType;//Код валюты

                v[9] = documentBase.getCurrencyRateSale();            // Курс валюты на момент отгрузки
                v[10] = documentBase.isPrepayment() ? "T" : "F";// Признак предоплаты

                v[11] = item.getItemName();
                v[12] = item.getArticleNumber();
                v[13] = item.getArticleCode();
                v[14] = item.getTnvedCode();
                v[15] = item.getModelNumber();
                v[16] = item.getItemGrowz();
                v[17] = item.getItemSize();
                v[18] = item.getGradeIfItemIsSock();
                v[19] = item.getItemColor();
                v[20] = SaleDocumentDataProvider.isChildItem(item.getArticleCode()) ? "T" : "F";

                v[21] = item.getAmountPrint();

                v[22] = item.getValueAccountingPrice();
                v[23] = item.getValueVat();
                v[24] = item.getValuePrice();
                v[25] = item.getValueSumCost();
                v[26] = item.getValueSumVat();
                v[27] = item.getValueSumCostAndVat();

                v[28] = item.getValuePriceCurrency();
                v[29] = item.getValueSumCostCurrency();
                v[30] = item.getValueSumVatCurrency();
                v[31] = item.getValueSumCostAndVatCurrency();

                writeItem(v);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
