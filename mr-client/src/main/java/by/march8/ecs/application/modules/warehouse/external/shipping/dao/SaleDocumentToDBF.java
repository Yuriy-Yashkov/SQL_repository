package by.march8.ecs.application.modules.warehouse.external.shipping.dao;

import by.march8.ecs.framework.common.LogCrutch;
import by.march8.entities.warehouse.VSaleDocumentReport;
import by.march8.tasks.accounting.AbstractDBF;
import workDB.JDBField;

import java.util.ArrayList;

/**
 * @author tmp on 03.12.2021 10:56
 */
public class SaleDocumentToDBF extends AbstractDBF {

    private static final LogCrutch log = new LogCrutch();
    private final ArrayList<Object> data;
    private int MAX_ITEM = 29;

    /**
     * Конструктор создания DBF файла
     *
     * @param savePath путь к файлу, без завершающего слэша
     * @param fileName имя файла, без расширения
     */
    public SaleDocumentToDBF(ArrayList<Object> data, String savePath, String fileName) {
        super(savePath, fileName);
        this.data = data;
        try {
            fields = new JDBField[MAX_ITEM];
            fields[0] = new JDBField("ID", 'N', 19, 5);
            fields[1] = new JDBField("DATE", 'D', 8, 0);
            fields[2] = new JDBField("NDOC", 'C', 10, 0);
            fields[3] = new JDBField("TYPE", 'C', 50, 0);
            fields[4] = new JDBField("CCODE", 'C', 5, 0);
            fields[5] = new JDBField("CNAME", 'C', 250, 0);
            fields[6] = new JDBField("MODEL", 'N', 10, 0);
            fields[7] = new JDBField("ARTICLE", 'C', 20, 0);
            fields[8] = new JDBField("INAME", 'C', 250, 0);
            fields[9] = new JDBField("ISIZE", 'C', 50, 0);
            fields[10] = new JDBField("IGRADE", 'N', 1, 0);
            fields[11] = new JDBField("ICOLOR", 'C', 50, 0);
            fields[12] = new JDBField("AMOUNT", 'N', 19, 0);
            fields[13] = new JDBField("CUNAME", 'C', 10, 0);
            fields[14] = new JDBField("CUFIXER", 'N', 19, 5);
            fields[15] = new JDBField("CUSALER", 'N', 19, 5);
            fields[16] = new JDBField("VAT", 'N', 19, 5);

            fields[17] = new JDBField("PRICE", 'N', 19, 5);
            fields[18] = new JDBField("COST", 'N', 19, 5);
            fields[19] = new JDBField("SUMVAT", 'N', 19, 5);
            fields[20] = new JDBField("COSTVAT", 'N', 19, 5);

            fields[21] = new JDBField("PRICEC", 'N', 19, 5);
            fields[22] = new JDBField("COSTC", 'N', 19, 5);
            fields[23] = new JDBField("SUMVATC", 'N', 19, 5);
            fields[24] = new JDBField("COSTVATC", 'N', 19, 5);

            fields[25] = new JDBField("RCRITM", 'N', 19, 5);
            fields[26] = new JDBField("RCRAMNT", 'N', 19, 5);
            fields[27] = new JDBField("RVARITM", 'N', 19, 5);
            fields[28] = new JDBField("RVARAMNT", 'N', 19, 5);


        } catch (Exception e) {
            log.error("Ошибка при создании списка полей дбф: ", e);
            e.printStackTrace();
        }


    }

    void create() {
        int counter = 1;
        for (Object obj : data) {
            Object[] v = new Object[MAX_ITEM];
            VSaleDocumentReport item = (VSaleDocumentReport) obj;
/*
            v[0] = counter;
            v[1] = DateUtils.getNormalDateFormat(item.getDocumentDate());
            v[2] = ;
            v[3] = ;
            v[4] = ;
            v[5] = ;
            v[6] = ;
            v[7] = ;
            v[8] = ;
            v[9] = ;
            v[10] = ;
            v[11] = ;
            v[12] = ;
            v[13] = ;
            v[14] = ;
            v[15] = ;
            v[16] = ;
            v[17] = ;
            v[18] = ;
            v[19] = ;
            v[20] = ;
            v[21] = ;
            v[22] = ;
            v[23] = ;
            v[24] = ;
            v[25] = ;
            v[26] = ;
            v[27] = ;
            v[28] = ;
*/
            counter++;
            writeItem(v);
        }
    }
}
