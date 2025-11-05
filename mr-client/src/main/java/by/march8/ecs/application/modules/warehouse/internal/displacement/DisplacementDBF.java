package by.march8.ecs.application.modules.warehouse.internal.displacement;

import by.march8.ecs.application.modules.warehouse.external.shipping.manager.CertificateService;
import by.march8.ecs.framework.common.LogCrutch;
import by.march8.entities.warehouse.Certificate;
import by.march8.entities.warehouse.DisplacementInvoiceView;
import by.march8.entities.warehouse.VInternalInvoiceItem;
import by.march8.tasks.accounting.AbstractDBF;
import workDB.JDBField;

import java.util.List;

/**
 * @author Andy 19.03.2018.
 */
public class DisplacementDBF extends AbstractDBF {

    private static final LogCrutch log = new LogCrutch();
    // Инициализация менеджера сертификатов
    CertificateService certificateService = CertificateService.getInstance();
    private int MAX_ITEM = 30;

    /**
     * Конструктор создания DBF файла
     *
     * @param savePath путь к файлу, без завершающего слэша
     * @param fileName имя файла, без расширения
     */
    public DisplacementDBF(final String savePath, final String fileName) {
        super(savePath, fileName);

        try {
            fields = new JDBField[MAX_ITEM];

            fields[0] = new JDBField("FIRMA", 'C', 12, 0);
            fields[1] = new JDBField("TTN_NOMER", 'C', 10, 0);
            fields[2] = new JDBField("MODEL", 'F', 6, 0);
            fields[3] = new JDBField("ART", 'C', 15, 0);
            fields[4] = new JDBField("NAIM", 'C', 45, 0);
            fields[5] = new JDBField("RAZMER", 'C', 20, 0);
            fields[6] = new JDBField("SORT", 'C', 8, 0);
            fields[7] = new JDBField("COLOR", 'C', 25, 0);
            fields[8] = new JDBField("KOL", 'F', 16, 0);
            fields[9] = new JDBField("CENA", 'F', 20, 2);
            fields[10] = new JDBField("SUMMA", 'F', 20, 2);
            fields[11] = new JDBField("NDS", 'F', 7, 2);
            fields[12] = new JDBField("SUMMA_NDS", 'F', 20, 2);
            fields[13] = new JDBField("ITOGO", 'F', 20, 2);
            fields[14] = new JDBField("EANCODE", 'C', 13, 0);
            fields[15] = new JDBField("COUNTRY", 'C', 2, 0);
            fields[16] = new JDBField("SERTIFIKAT", 'C', 80, 0);
            fields[17] = new JDBField("GGR", 'C', 80, 0);
            fields[18] = new JDBField("TNVD", 'F', 12, 0);
            fields[19] = new JDBField("MASSA_ED", 'C', 20, 0);
            fields[20] = new JDBField("MASSA", 'C', 18, 0);
            fields[21] = new JDBField("DOGOVOR", 'C', 50, 0);
            fields[22] = new JDBField("PREISCUR", 'C', 50, 0);
            fields[23] = new JDBField("SAR", 'C', 15, 0);
            fields[24] = new JDBField("TORG_NADB", 'C', 15, 0);
            fields[25] = new JDBField("ROZN_CENA", 'C', 15, 0);
            fields[26] = new JDBField("KPK", 'C', 15, 0);
            fields[27] = new JDBField("CENA_UCH", 'F', 20, 2);
            fields[28] = new JDBField("NDS_VALUE", 'F', 7, 2);
            fields[29] = new JDBField("SCAN", 'C', 13, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean processing(DisplacementInvoiceReport report) {
        if (report == null) {
            return false;
        }

        if (report.getDocument() == null) {
            return false;
        }

        if (report.getList() == null) {
            return false;
        }

        DisplacementInvoiceView document = report.getDocument();
        List<VInternalInvoiceItem> data = report.getList();


        for (VInternalInvoiceItem item : data) {
            if (!insetItem(document, item)) {
                return false;
            }
        }
        return true;
    }


    private boolean insetItem(DisplacementInvoiceView document, VInternalInvoiceItem item) {

        Object[] v = new Object[MAX_ITEM];
        try {
            v[0] = "ОАО \"8МАРТА\"";
            v[1] = document.getDocumentNumber();
            v[2] = Integer.valueOf(item.getModel());
            v[3] = item.getArticleNumber();

            if (item.getName().length() > 45) {
                v[4] = item.getName().substring(0, 45);
            } else {
                v[4] = item.getName();
            }


            v[5] = item.getSize();
            v[6] = item.getSort();

            String param = item.getColor().trim();
            if (param.length() > 25) {
                param = param.substring(0, 25);
            }

            v[7] = param;
            v[8] = item.getQuantityAll();

            v[9] = 0; //item.getValuePrice();
            v[10] = 0;//item.getValueSumCost();//rs.getObject("summa");
            v[11] = 0;//item.getValueVAT(); //rs.getObject("nds");
            v[12] = 0;//item.getValueSumVat();//rs.getObject("summa_nds");
            v[13] = 0;//item.getValueSumCostAndVat(); //rs.getObject("itogo");

            v[14] = item.getEanCode();// rs.getObject("eancode");
            v[15] = "РБ";

            Certificate certificate = certificateService.getCertificateByArticleRecursively(item.getArticleCode().trim(), 1);
            param = "";
            if (certificate != null) {
                param = certificate.getValue();
                if (param.length() > 80) {
                    param = param.substring(0, 80);
                }
            }

            v[16] = param;
            v[17] = "";
            v[18] = Long.valueOf(item.getTnvedCode());//rs.getObject("narp");
            v[19] = String.format("%.4f", item.getWeight());//String.valueOf(rs.getObject("massa_ed"));
            v[20] = String.format("%.4f", item.getWeight() * item.getQuantityAll());//String.valueOf(rs.getObject("massa"));
            v[21] = "";//dogovor;
            v[22] = "";
            v[23] = item.getArticleCode();//rs.getString("sar").trim();
            v[24] = "0";//String.format("%.2f", documentBase.getTradeMarkValue());
            v[25] = "0";//String.format("%.2f", retail.getValueCostRetail());
            v[26] = String.valueOf(item.getPtkCode()); //"0";//rs.getObject("ptk") != null ? rs.getString("ptk").trim() : "0";
            v[27] = item.getAccountingPrice(); //rs.getObject("cena_uch") != null ? rs.getFloat("cena_uch") : 0;
            v[28] = item.getAccountingVat();
            v[29] = String.valueOf(item.getScanCode());

            writeItem(v);
        } catch (Exception ex) {
            System.err.println(ex);
            return false;
        }
        return true;
    }

}
