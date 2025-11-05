package by.march8.tasks.accounting;

import by.march8.api.utils.DateUtils;
import by.march8.ecs.application.modules.warehouse.external.shipping.manager.SaleDocumentManager;
import by.march8.ecs.application.modules.warehouse.external.shipping.model.SaleDocumentReport;
import by.march8.ecs.framework.common.LogCrutch;
import by.march8.entities.warehouse.InvoiceType;
import by.march8.entities.warehouse.SaleDocumentBase;
import by.march8.entities.warehouse.SaleDocumentDetailItemReport;
import workDB.JDBField;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.regex.Pattern;

/**
 * @author Andy
 */
public class OldLineDBF extends AbstractDBF {

    private static final LogCrutch log = new LogCrutch();

    private Pattern patternLatin;
    private Pattern patternCyrilic;

    public OldLineDBF(String fileName, String pathSave, Date date) {
        super(pathSave, fileName);
        try {
            fields = new JDBField[22];
            fields[0] = new JDBField("DAO", 'N', 4, 0);
            fields[1] = new JDBField("KPL", 'N', 4, 0);
            fields[2] = new JDBField("SK", 'N', 4, 0);
            fields[3] = new JDBField("TTN", 'N', 9, 0);
            fields[4] = new JDBField("NAK", 'N', 10, 0);
            fields[5] = new JDBField("SAR", 'N', 8, 0);
            fields[6] = new JDBField("SOT", 'N', 3, 0);
            fields[7] = new JDBField("FAS", 'N', 6, 0);
            fields[8] = new JDBField("RST", 'N', 3, 0);
            fields[9] = new JDBField("RZM", 'N', 3, 0);
            fields[10] = new JDBField("SRT", 'N', 1, 0);
            fields[11] = new JDBField("PACH", 'N', 4, 0);
            fields[12] = new JDBField("KOL", 'N', 8, 1);
            fields[13] = new JDBField("CNO", 'N', 9, 2);
            fields[14] = new JDBField("CNR", 'N', 8, 2);
            fields[15] = new JDBField("KCV", 'N', 3, 0);
            fields[16] = new JDBField("NPT", 'N', 5, 0);
            fields[17] = new JDBField("VCN", 'C', 2, 0);
            fields[18] = new JDBField("CRR", 'N', 10, 4);
            fields[19] = new JDBField("NDB", 'N', 9, 2);
            fields[20] = new JDBField("NDR", 'N', 8, 2);
            fields[21] = new JDBField("PSK", 'N', 4, 1);

        } catch (Exception e) {
            log.error("Ошибка при создании списка полей дбф: ", e);
            e.printStackTrace();
        }

        if (date == null) {
            date = new Date();
        }

        Calendar calendar = Calendar.getInstance();

        calendar.setTime(date);

        if (calendar.get(Calendar.MONTH) + 1 < 10) {
            fileName = "AR_O0" + String.valueOf(calendar.get(Calendar.MONTH) + 1) + "d.dbf";
        } else {
            fileName = "AR_O" + String.valueOf(calendar.get(Calendar.MONTH) + 1) + "d.dbf";
        }

        savePathFull = savePath + "/" + fileName;

        patternLatin = Pattern.compile("[a-zA-z]{0,2}");
        patternCyrilic = Pattern.compile("[а-яА-Я]{0,2}");
    }

    public void addItem(SaleDocumentReport report) {
        SaleDocumentBase document = report.getDocument();
        final HashMap<String, Object> detailMap = report.getDetailMap();
        String c1 = "", c2 = "";
        c1 = patternCyrilic.matcher(document.getDocumentNumber()).replaceAll("").trim();
        c1 = patternLatin.matcher(c1).replaceAll("").trim();
        //m = p.matcher();

        c2 = c1 + "1";

        Date dateSale = null;
        try {
            dateSale = DateUtils.getDateByStringValue((String) detailMap.get("DOCUMENT_DATE"));
        } catch (Exception ed) {
            dateSale = report.getDocument().getDocumentSaleDate();
        }


        Calendar c = Calendar.getInstance();
        c.setTime(dateSale);
        String day = String.valueOf(c.get(Calendar.DAY_OF_MONTH));
        String month = String.valueOf(c.get(Calendar.MONTH) + 1);
        if (month.length() < 2) {
            month = "0" + month;
        }

        String fullDate = day + month;

        int date = Integer.valueOf(fullDate);

        boolean isRefund = SaleDocumentManager.isDocumentRefund(document);

        final SaleDocumentBase documentBase = report.getDocument();

        int region = 0;

        if (documentBase.getDocumentType().equals(InvoiceType.DOCUMENT_REFUND_BUYER) ||
                documentBase.getDocumentType().equals(InvoiceType.DOCUMENT_REFUND_RETAIL) ||
                documentBase.getDocumentType().equals(InvoiceType.DOCUMENT_REFUND_MATERIAL)) {
            region = (int) detailMap.get("SENDER_REGION");
        } else {
            region = (int) detailMap.get("RECIPIENT_REGION");
        }


        for (SaleDocumentDetailItemReport item : report.getDetailList()) {
            Object[] v = new Object[22];
            v[0] = date;// Дата
            v[1] = document.getRecipientCode();//КОд контрагента
            v[2] = 737;

            v[3] = Long.valueOf(c1.trim());
            v[4] = Long.valueOf(c2.trim());

            v[5] = Integer.valueOf(item.getArticleCode());
            v[6] = 0;
            v[7] = Integer.valueOf(item.getModelNumber());
            v[8] = item.getItemGrowz();
            v[9] = item.getItemSize();

            v[10] = item.getGradeIfItemIsSock();

            v[11] = document.getRecipientCode();

            if (isRefund) {
                v[12] = new BigDecimal(item.getAmountPrint() * (-1)).setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
            } else {
                v[12] = new BigDecimal(item.getAmountPrint()).setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
            }

            v[13] = item.getValuePrice();

            v[14] = new BigDecimal((item.getValuePrice() * item.getValueVat() / 100) + item.getValuePrice())
                    .setScale(2, BigDecimal.ROUND_HALF_UP)
                    .doubleValue();

            v[15] = 552;
            v[16] = region;
            v[17] = "";
            v[18] = new BigDecimal((item.getValuePriceCurrency() * item.getValueVat() / 100) + item.getValuePriceCurrency())
                    .setScale(2, BigDecimal.ROUND_HALF_UP)
                    .doubleValue();

            v[19] = new BigDecimal(item.getValuePrice() * item.getValueVat() / 100)
                    .setScale(2, BigDecimal.ROUND_HALF_UP)
                    .doubleValue();

            v[20] = item.getValuePriceCurrency();
            v[21] = 0;//document.getDiscountValue();

            writeItem(v);
        }
    }

}
