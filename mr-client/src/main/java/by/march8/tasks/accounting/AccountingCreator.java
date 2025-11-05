package by.march8.tasks.accounting;

import by.gomel.freedev.ucframework.uccore.utils.SystemUtils;
import by.march8.ecs.application.modules.warehouse.external.shipping.manager.SaleDocumentDataProvider;
import by.march8.ecs.application.modules.warehouse.external.shipping.model.DocumentUploadPreset;
import by.march8.ecs.application.modules.warehouse.external.shipping.model.SaleDocumentReport;
import by.march8.entities.warehouse.SaleDocumentEntity;
import common.ProgressBar;

import java.util.Date;
import java.util.List;

/**
 * @author Andy 08.07.2016.
 */
public class AccountingCreator {

    private SaleDocumentDataProvider provider;
    private List<SaleDocumentEntity> list;
    private AccountingDbf accountingDbf = null;
    private SpecificationDbf specificationDbf = null;
    private OldLineDBF oldLineDbf = null;

    private String exportPath = "";

    private ProgressBar pb = null;

    public AccountingCreator(boolean isAccounting, boolean isOldLine, List<SaleDocumentEntity> list) {
        if (!SystemUtils.isWindows()) {
            exportPath = "/home/user/xml/dbf";
        } else {
            exportPath = "\\\\terminal-srv6\\xml\\dbf\\";
        }

        provider = new SaleDocumentDataProvider();
        this.list = list;

        if (isAccounting) {
            accountingDbf = new AccountingDbf("NAKL", exportPath);
            accountingDbf.connect();

            specificationDbf = new SpecificationDbf("SPEC", exportPath);
            specificationDbf.connect();
        }

        if (isOldLine) {
            oldLineDbf = new OldLineDBF("", exportPath, null);
            oldLineDbf.connect();
        }

        processing();
    }

    public AccountingCreator(List<SaleDocumentEntity> list, DocumentUploadPreset preset) {
        if (!SystemUtils.isWindows()) {
            exportPath = "/home/user/xml/dbf";
        } else {
            exportPath = "\\\\terminal-srv6\\xml\\dbf\\";
        }

        provider = new SaleDocumentDataProvider();
        this.list = list;

        String fileName = "MOST";

        if (preset != null) {
            fileName = "ACCOUNTING_DOCUMENT";
        }

        accountingDbf = new AccountingDbf(fileName, exportPath);
        accountingDbf.connect();

        processing();
    }

    public AccountingCreator(boolean isAccounting, boolean isOldLine, List<SaleDocumentEntity> list, Date date, ProgressBar pb) {
        this.pb = pb;

        if (!SystemUtils.isWindows()) {
            exportPath = "/home/user/xml/dbf";
        } else {
            exportPath = "\\\\terminal-srv6\\xml\\dbf\\";
        }

        provider = new SaleDocumentDataProvider();
        this.list = list;

        if (isAccounting) {
            accountingDbf = new AccountingDbf("NAKL", exportPath);
            accountingDbf.connect();
        }

        if (isOldLine) {
            oldLineDbf = new OldLineDBF("", exportPath, date);
            oldLineDbf.connect();
        }

        processing();

    }

    public void processing() {

        int step = 1;

        for (SaleDocumentEntity entity : list) {
            try {
                System.out.println("processing for document [" + entity.getDocumentNumber() + "]");

                SaleDocumentReport item = provider.prepareDocument(entity.getId(), false);
                /**
                 * TODO: Как разбремся с накладной 1438831, убрать эту каку.
                 */
                if (entity.getDocumentNumber().contains("1438831") || entity.getDocumentNumber().contains("215459")
                        || entity.getDocumentNumber().contains("бш1438831")) {
                    System.out.println(item.getDocument().toString());
                    System.out.println(item.getDocument().printDocumentInConsole());
                }
                // Вот до сель.
                if (item != null) {

                    if (accountingDbf != null) {
                        accountingDbf.addItem(item);
                    }

                    if (specificationDbf != null) {
                        specificationDbf.addItem(item);
                    }

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

        if (accountingDbf != null) {
            accountingDbf.disconnect();
        }

        if (specificationDbf != null) {
            specificationDbf.disconnect();
        }

        if (oldLineDbf != null) {
            oldLineDbf.disconnect();
        }
    }


}
