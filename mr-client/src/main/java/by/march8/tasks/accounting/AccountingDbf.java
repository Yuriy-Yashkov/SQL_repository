package by.march8.tasks.accounting;

import by.march8.api.utils.DateUtils;
import by.march8.ecs.application.modules.warehouse.external.shipping.model.SaleDocumentReport;
import by.march8.ecs.application.modules.warehouse.external.shipping.model.TotalSummingUp;
import by.march8.ecs.application.modules.warehouse.external.shipping.services.ResponsiblePersonsService;
import by.march8.ecs.framework.common.LogCrutch;
import by.march8.entities.warehouse.InvoiceType;
import by.march8.entities.warehouse.SaleDocumentBase;
import by.march8.entities.warehouse.SaleDocumentDetailItemReport;
import workDB.JDBField;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * @author Andy 08.07.2016.
 */

public class AccountingDbf extends AbstractDBF {

    private static final LogCrutch log = new LogCrutch();
    private int MAX_ITEM = 33;

    public AccountingDbf(String fileName, String pathSave) {
        super(pathSave, fileName);
        try {
            fields = new JDBField[MAX_ITEM];
            fields[0] = new JDBField("ID", 'N', 19, 5);
            fields[1] = new JDBField("NDOC", 'C', 10, 0);
            fields[2] = new JDBField("DATE", 'D', 8, 0);
            fields[3] = new JDBField("UNN", 'C', 13, 0);
            fields[4] = new JDBField("NAIM", 'C', 250, 0);
            fields[5] = new JDBField("KOL", 'N', 19, 5);
            fields[6] = new JDBField("S", 'N', 19, 5);
            fields[7] = new JDBField("SN", 'N', 19, 5);
            fields[8] = new JDBField("SV", 'N', 19, 5);
            fields[9] = new JDBField("SVN", 'N', 19, 5);
            fields[10] = new JDBField("VALUTA", 'C', 5, 0);
            fields[11] = new JDBField("NOMER", 'C', 15, 0);
            fields[12] = new JDBField("DATA", 'D', 8, 0);
            fields[13] = new JDBField("ITEM_ID", 'N', 19, 5);
            fields[14] = new JDBField("DET", 'N', 19, 5);
            fields[15] = new JDBField("TYPE", 'C', 50, 0);
            fields[16] = new JDBField("NDS", 'N', 19, 5);
            fields[17] = new JDBField("KOD", 'N', 19, 5);
            fields[18] = new JDBField("USL", 'N', 19, 5);
            fields[19] = new JDBField("PREPAY", 'C', 1, 0);
            fields[20] = new JDBField("S_ALLOW", 'N', 19, 2);
            fields[21] = new JDBField("S_VAT", 'N', 19, 2);
            fields[22] = new JDBField("L_ADDR", 'C', 250, 0);
            fields[23] = new JDBField("S_RATE", 'N', 19, 4);
            fields[24] = new JDBField("NO_GRADE", 'C', 1, 0);
            fields[25] = new JDBField("GIVE", 'C', 1, 0);

            fields[26] = new JDBField("SDOC", 'C', 10, 0);
            fields[27] = new JDBField("SDATE", 'C', 8, 0);
            fields[28] = new JDBField("RDOC", 'C', 10, 0);
            fields[29] = new JDBField("RDATE", 'C', 8, 0);
            fields[30] = new JDBField("REGION", 'N', 4, 0);
            fields[31] = new JDBField("MANAGER", 'C', 50, 0);
            fields[32] = new JDBField("TABEL_NUM", 'N', 4, 0);
        } catch (Exception e) {
            log.error("Ошибка при создании списка полей дбф: ", e);
            e.printStackTrace();
        }
    }

    public void addItem(SaleDocumentReport report) {
        final SaleDocumentBase documentBase = report.getDocument();
        final List<SaleDocumentDetailItemReport> detailList = report.getDetailList();
        final HashMap<String, Object> detailMap = report.getDetailMap();
        final TotalSummingUp summingUp = report.getSummingUp();
        ResponsiblePersonsService responsiblePersonsService = ResponsiblePersonsService.getInstance();

        Object[] v = new Object[MAX_ITEM];

        v[0] = documentBase.getId();//rs.getInt(1);// Идентификатор документа
        v[1] = documentBase.getDocumentNumber();//Номер документа

        Date dateSale;
        try {
            dateSale = DateUtils.getDateByStringValue((String) detailMap.get("DOCUMENT_DATE"));
        } catch (Exception ed) {
            dateSale = documentBase.getDocumentSaleDate();
        }

        v[2] = dateSale;//Дата путевого листа

        String unp = "";
        String senderName = "";

        // ЕСли накладная возвратная, реквизиты контрагентов меняем местами
        if (documentBase.getDocumentType().equals(InvoiceType.DOCUMENT_REFUND_BUYER) ||
                documentBase.getDocumentType().equals(InvoiceType.DOCUMENT_REFUND_RETAIL) ||
                documentBase.getDocumentType().equals(InvoiceType.DOCUMENT_REFUND_MATERIAL)) {

            unp = (String) detailMap.get("SENDER_UNP");
            senderName = (String) detailMap.get("SENDER_NAME");
            // Юридический адрес контрагента
            v[22] = detailMap.get("SENDER_ADDRESS");
            v[30] = detailMap.get("SENDER_REGION");
        } else {
            unp = (String) detailMap.get("RECIPIENT_UNP");
            senderName = (String) detailMap.get("RECIPIENT_NAME");
            // Юридический адрес контрагента
            v[22] = detailMap.get("RECIPIENT_ADDRESS");
            v[30] = detailMap.get("RECIPIENT_REGION");
        }

        v[31] = detailMap.get("SALE_ALLOWED");


        //detailMap.get("SALE_MANAGER")
        v[32] = responsiblePersonsService.getTabelNumber(detailMap.get("SALE_ALLOWED").toString());


        v[3] = unp.trim();// УНН контрагента
        v[4] = senderName.trim();//Наименование контрагента

        v[5] = summingUp.getAmount(); //Количество


        if (documentBase.getRecipientCode() == 4037) {
            v[6] = documentBase.getValueSumCost();//Сумма
            v[7] = documentBase.getValueSumVat();// Сумма НДС
        } else {
            v[6] = summingUp.getValueSumCost();//Сумма
            v[7] = summingUp.getValueSumVat();// Сумма НДС
        }

        v[8] = summingUp.getValueSumCostCurrency();// Сумма Валюта
        v[9] = summingUp.getValueSumVatCurrency();// Сумма валюта НДС

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


        v[10] = currencyType;//Код валюты
        String contractNumber = (String) detailMap.get("CONTRACT_NUMBER");

        v[11] = contractNumber.trim();// Номер договора

        v[12] = detailMap.get("CONTRACT_DATE_BEGIN_AS_DATE");//rs.getInt(13) * (-1);// Дата договора

        v[13] = documentBase.getRecipientContractId(); // ИД Договора
        v[14] = detailMap.get("CHILD_COUNT");
        v[15] = documentBase.getDocumentType(); //Тип документа
        v[16] = documentBase.getDocumentVatValue();   //Ставка НДС
        v[17] = documentBase.getRecipientCode();//Код контрагента

        v[18] = documentBase.isAsService() ? 1 : 0;//Код услуги
        v[19] = documentBase.isPrepayment() ? "T" : "F";// Признак предоплаты


        // Сумма торговых надбавок
        v[20] = summingUp.getValueSumAllowance();
        // Сумма НДС т/н
        v[21] = summingUp.getValueSumVatRetail();
        if (documentBase.getDocumentNumber().trim().toLowerCase().equals("ре5993608")) {
            v[20] = summingUp.getValueSumAllowance() / 100;
            // Сумма НДС т/н
            v[21] = summingUp.getValueSumVatRetail() / 100;
        }
        // Курс валюты на момент отгрузки
        v[23] = documentBase.getCurrencyRateSale();

        if (documentBase.isNotVarietal()) {
            v[24] = "1";
        } else {
            v[24] = "0";
        }

        if (documentBase.isGiveTake()) {
            //    if(documentBase.getServiceType()>1) {
            v[25] = String.valueOf(documentBase.getServiceType());
            //  }else {
            //    v[25] = "1";
            // }
        } else {
            v[25] = "0";
        }

        String s = documentBase.getSourceDocNumber();
        if (s != null) {
            if (!s.trim().equals("")) {
                v[26] = s;//Номер исходного документа

                if (documentBase.getSourceDocDate() == null) {
                    v[27] = "";
                } else {
                    v[27] = DateUtils.getShortNormalDateFormat(documentBase.getSourceDocDate());//Дата исходного документа
                }
            } else {
                v[26] = "";
                v[27] = "";
            }
        } else {
            v[26] = "";
            v[27] = "";
        }

        s = documentBase.getRefundDocNumber();
        if (s != null) {
            if (!s.trim().equals("")) {
                v[28] = s;//documentBase.getRefundDocNumber();//Номер возвратного документа

                if (documentBase.getSourceDocDate() == null) {
                    v[29] = "";
                } else {
                    v[29] = DateUtils.getShortNormalDateFormat(documentBase.getRefundDocDate());//Дата возвратного документа
                }
            } else {
                v[28] = "";
                v[29] = "";
            }
        } else {
            v[28] = "";
            v[29] = "";
        }

        writeItem(v);
        /**/
    }

}
