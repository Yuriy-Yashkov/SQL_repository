package by.march8.ecs.application.modules.warehouse.external.shipping.dao;

import by.march8.entities.warehouse.SaleDocumentInventoryItem;
import by.march8.tasks.accounting.AbstractDBF;
import by.march8.tasks.accounting.DBFExporter;
import workDB.JDBField;

/**
 * @author Andy 11.01.2019 - 10:48.
 */
public class SaleDocument1C7DBF extends AbstractDBF implements DBFExporter<SaleDocumentInventoryItem> {

    private int MAX_ITEM = 16;

    public SaleDocument1C7DBF(String savePath, String fileName) {
        super(savePath, fileName);
        try {
            fields = new JDBField[MAX_ITEM];
            fields[0] = new JDBField("MODEL", 'F', 6, 0);
            fields[1] = new JDBField("ART", 'C', 15, 0);
            fields[2] = new JDBField("NAIM", 'C', 45, 0);
            fields[3] = new JDBField("RAZMER", 'C', 20, 0);
            fields[4] = new JDBField("SORT", 'C', 8, 0);
            fields[5] = new JDBField("CENA", 'F', 20, 2);
            fields[6] = new JDBField("NDS", 'F', 7, 2);
            fields[7] = new JDBField("EANCODE", 'C', 13, 0);
            fields[8] = new JDBField("COUNTRY", 'C', 2, 0);
            fields[9] = new JDBField("TNVD", 'F', 12, 0);
            fields[10] = new JDBField("MASSA_ED", 'C', 20, 0);
            fields[11] = new JDBField("PREISCUR", 'C', 50, 0);
            fields[12] = new JDBField("SAR", 'N', 8, 0);
            fields[13] = new JDBField("TORG_NADB", 'N', 20, 0);
            fields[14] = new JDBField("ROZN_CENA", 'N', 20, 4);
            fields[15] = new JDBField("KPK", 'C', 15, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void addItem(SaleDocumentInventoryItem item) {
        Object[] row = new Object[MAX_ITEM];
        row[0] = Integer.valueOf(item.getModelNumber());
        row[1] = item.getArticleName();
        if (item.getItemName().length() > 45) {
            row[2] = item.getItemName().substring(0, 45);
        } else {
            row[2] = item.getItemName();
        }
        row[3] = item.getSizePrint();
        row[4] = item.getGradeAsStringPlus();
        row[5] = item.getPrice();
        row[6] = item.getVat();
        row[7] = item.getEancode();
        row[8] = "РБ";
        row[9] = Long.valueOf(item.getCodeTNVED());
        row[10] = String.format("%.4f", item.getWeight());
        row[11] = item.getPriceList();
        row[12] = Integer.valueOf(item.getArticleCode());
        row[13] = item.getTradeAllowance();
        row[14] = item.getRetailPrice();
        row[15] = String.valueOf(item.getCodePTK());

        writeItem(row);
    }
}
