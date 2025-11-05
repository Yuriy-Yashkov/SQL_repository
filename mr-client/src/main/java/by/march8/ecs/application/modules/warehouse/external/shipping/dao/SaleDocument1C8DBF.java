package by.march8.ecs.application.modules.warehouse.external.shipping.dao;

import by.march8.entities.warehouse.SaleDocumentInventoryItem;
import by.march8.tasks.accounting.AbstractDBF;
import by.march8.tasks.accounting.DBFExporter;
import workDB.JDBField;

/**
 * @author Andy 11.01.2019 - 9:38.
 */
public class SaleDocument1C8DBF extends AbstractDBF implements DBFExporter<SaleDocumentInventoryItem> {

    private int MAX_ITEM = 29;

    /**
     * Конструктор создания DBF файла
     *
     * @param savePath путь к файлу, без завершающего слэша
     * @param fileName имя файла, без расширения
     */
    public SaleDocument1C8DBF(String savePath, String fileName) {
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void addItem(SaleDocumentInventoryItem item) {
        Object[] row = new Object[MAX_ITEM];

        String param;

        row[0] = "ОАО \"8МАРТА\"";
        row[1] = item.getDocumentNumber();
        row[2] = Integer.valueOf(item.getModelNumber());
        row[3] = item.getArticleName();

        if (item.getItemName().length() > 45) {
            row[4] = item.getItemName().substring(0, 45);
        } else {
            row[4] = item.getItemName();
        }
        row[5] = item.getSizePrint();
        row[6] = item.getGradeAsStringPlus();

        param = item.getColor().trim();
        if (param.length() > 25) {
            param = param.substring(0, 25);
        }

        row[7] = param;
        row[8] = 1;

        row[9] = item.getPrice();
        row[10] = 0;
        row[11] = item.getVat();
        row[12] = 0;
        row[13] = 0;

        row[14] = item.getEancode();
        row[15] = "РБ";
        row[16] = "";
        row[17] = "";

        row[18] = Long.valueOf(item.getCodeTNVED());
        row[19] = String.format("%.4f", item.getWeight());
        row[20] = String.format("%.4f", item.getWeight());
        row[21] = "";

        row[22] = item.getPriceList();
        row[23] = item.getArticleCode();


        row[24] = String.format("%.2f", item.getTradeAllowance());

        row[25] = String.format("%.2f", item.getRetailPrice());
        row[26] = String.valueOf(item.getCodePTK());
        row[27] = item.getAccountingPrice();
        row[28] = item.getVatWholesale();

        writeItem(row);
    }

}
