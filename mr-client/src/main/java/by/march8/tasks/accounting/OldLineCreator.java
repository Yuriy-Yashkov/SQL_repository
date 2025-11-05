package by.march8.tasks.accounting;

import by.march8.ecs.application.modules.warehouse.external.shipping.manager.SaleDocumentDataProvider;
import by.march8.ecs.application.modules.warehouse.external.shipping.model.SaleDocumentReport;
import by.march8.entities.warehouse.SaleDocumentEntity;
import common.ProgressBar;

import java.util.Date;
import java.util.List;

/**
 * @author Andy 09.08.2016.
 */
public class OldLineCreator {

    private SaleDocumentDataProvider provider;
    private List<SaleDocumentEntity> list;
    private OldLineDBF oldLineDbf = null;

    private String exportPath = "";

    private ProgressBar pb = null;

    public OldLineCreator(String savePath, List<SaleDocumentEntity> list, Date date, ProgressBar pb) {
        this.pb = pb;
        exportPath = savePath;
        provider = new SaleDocumentDataProvider();
        this.list = list;
        oldLineDbf = new OldLineDBF("", exportPath, date);
        oldLineDbf.connect();
        processing();
    }

    public void processing() {

        int step = 1;

        for (SaleDocumentEntity entity : list) {
            try {
                System.out.println("processing for document [" + entity.getDocumentNumber() + "]");

                SaleDocumentReport item = provider.prepareDocument(entity.getId(), false);

                if (item != null) {
                    if (oldLineDbf != null) {
                        oldLineDbf.addItem(item);
                    }
                }

                if (pb != null) {
                    pb.updateValue(step);
                    step++;
                }

            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("error processing for document [" + entity.getDocumentNumber() + "]");
            }
        }

        if (oldLineDbf != null) {
            oldLineDbf.disconnect();
        }
    }

}
