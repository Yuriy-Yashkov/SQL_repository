package by.march8.ecs.application.modules.sales.dao;

import by.march8.ecs.application.modules.warehouse.external.shipping.model.SaleDocumentReport;
import by.march8.entities.warehouse.SaleDocumentBase;
import by.march8.entities.warehouse.SaleDocumentDetailItemReport;
import by.march8.tasks.accounting.AbstractDBF;
import workDB.JDBField;

public class CommonFormatDBF extends AbstractDBF {
    /**
     * Конструктор создания DBF файла
     *
     * @param savePath путь к файлу, без завершающего слэша
     * @param fileName имя файла, без расширения
     */
    public CommonFormatDBF(String savePath, String fileName) {
        super(savePath, fileName);
        try {
            fields = new JDBField[24];
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
            fields[16] = new JDBField("SERTIFIKAT", 'C', 105, 0);
            fields[17] = new JDBField("GGR", 'C', 80, 0);
            fields[18] = new JDBField("TNVD", 'F', 12, 0);
            fields[19] = new JDBField("MASSA_ED", 'C', 20, 0);
            fields[20] = new JDBField("MASSA", 'C', 18, 0);
            fields[21] = new JDBField("DOGOVOR", 'C', 50, 0);
            fields[22] = new JDBField("PREISCUR", 'C', 50, 0);
            fields[23] = new JDBField("CENA_UCH", 'F', 20, 2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void addItem(SaleDocumentReport report) {
        String param;
        try {
            SaleDocumentBase documentBase = report.getDocument();
            final java.util.List<SaleDocumentDetailItemReport> detailList = report.getDetailList();

            for (SaleDocumentDetailItemReport item : detailList) {
                Object[] v = new Object[24];
                v[0] = "ОАО \"8МАРТА\"";
                v[1] = documentBase.getDocumentNumber();
                v[2] = Integer.valueOf(item.getModelNumber());//rs.getObject("fas");
                v[3] = item.getArticleNumber();//rs.getString("nar").trim();
                param = item.getItemName();
                if (param.length() > 45) param = param.substring(0, 45);
                v[4] = param;

                v[5] = item.getItemSizePrint().replace("--", "-");
                v[6] = item.getGradeAsStringPlus();

                param = item.getItemColor();
                if (param.length() > 25) param = param.substring(0, 25);
                v[7] = param;

                v[8] = item.getAmountPrint();

                if (documentBase.getDocumentExport() > 0) {
                    v[9] = item.getValuePriceCurrency();//rs.getFloat("cena");
                    v[10] = item.getValueSumCostCurrency();//rs.getFloat("summa");
                    v[11] = item.getValueVAT(); //rs.getObject("nds");
                    v[12] = item.getValueSumVatCurrency();//rs.getFloat("summa_nds");
                    v[13] = item.getValueSumCostAndVatCurrency(); //rs.getFloat("itogo");
                } else {
                    v[9] = item.getValuePrice();//rs.getFloat("cena");
                    v[10] = item.getValueSumCost();//rs.getFloat("summa");
                    v[11] = item.getValueVAT(); //rs.getObject("nds");
                    v[12] = item.getValueSumVat();//rs.getFloat("summa_nds");
                    v[13] = item.getValueSumCostAndVat(); //rs.getFloat("itogo");
                }

                v[14] = item.getEanCode();//rs.getObject("eancode");
                v[15] = "РБ";

                param = item.getCertificateName();
                if (param.length() > 100) param = param.substring(0, 105);

                v[16] = param;

                param = item.getLicenseName();
                if (param.length() > 80) param = param.substring(0, 80);
                v[17] = param;

                v[18] = Long.valueOf(item.getTnvedCode());//rs.getObject("narp");

                v[19] = String.format("%.6f", item.getWeight() / item.getAmountAll());//
                v[20] = String.format("%.6f", item.getWeight());//String.valueOf(rs.getObject("massa"));

                param = SaleDocumentReport.getContractDescription(report);
                if (param.length() > 50) param = param.substring(0, 49);
                v[21] = param;

                param = item.getItemPriceList();
                if (param.length() > 50) param = param.substring(0, 50);
                v[22] = param;
                v[23] = item.getAccountingPrice();//rs.getFloat("cena");

                writeItem(v);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
