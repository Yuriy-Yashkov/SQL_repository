package by.march8.tasks.selections;

import by.gomel.freedev.ucframework.uccore.utils.SystemUtils;
import by.march8.api.utils.DateUtils;
import by.march8.ecs.Bootstrap;
import by.march8.ecs.application.modules.warehouse.external.shipping.dao.DAOSaleDocumentFactory;
import by.march8.ecs.application.modules.warehouse.external.shipping.dao.interfaces.ISaleDocumentDao;
import by.march8.entities.warehouse.SaleDocumentEntity;
import by.march8.tasks.selections.logic.SelectionsCreator;

import java.io.File;
import java.util.Date;
import java.util.List;

/**
 * @author Andy 12.04.2018 - 9:36.
 */
public class SelectionsController {
    /**
     * Загрузчик программы
     */
    private Bootstrap bootstrap;

    /**
     * Каталог для выгрузки документов
     */
    private String exportPath = "\\\\file-server2\\PRG_Exchange\\export\\";

    public SelectionsController(final Bootstrap bootstrap) {
        this.bootstrap = bootstrap;

        System.out.println("INVOICE CREATOR MODE ACTIVATE...");

        if (!SystemUtils.isWindows()) {
            exportPath = "/media/xml/factoring/";
        } else {
            exportPath = "C:\\selections\\";
        }

        File dir = new File(exportPath);
        if (!dir.exists()) {
            System.out.println("Directory [" + exportPath + "] not found, application terminate...");
            System.exit(0);
        }

        if (isNeedClean()) {
            System.out.println("Cleaning directory ..." + exportPath);
            //purgeDirectory(dir);
        }

        System.out.println("Create invoice processingIssuance start ..." + exportPath);
        processingIssuance();
        System.out.println("Create invoice processingIssuance stop ...EXIT");

        System.exit(0);
    }

    private void getMaterialList() {
        Date endDate = new Date();
        //endDate = DateUtils.getDateByStringValue("12.07.2017") ;
        Date beginDate = DateUtils.getDateByStringValue("01.07.2018");

        // Получаем список актуальных документов за прериод
        System.out.println("Processing dates : " + DateUtils.getNormalDateFormat(beginDate) + " - " + DateUtils.getNormalDateFormat(endDate));
        List<SaleDocumentEntity> list = getMaterialByPeriod(2, beginDate, endDate);

    }


    private void processingIssuance() {
        // Конечная дата периода отбора, как текущая
        Date endDate = new Date();
        //endDate = DateUtils.getDateByStringValue("12.07.2017") ;
        Date beginDate = DateUtils.getDateByStringValue("01.07.2017");

        // Получаем список актуальных документов за прериод
/*        System.out.println("Processing dates : " + DateUtils.getNormalDateFormat(beginDate) + " - " + DateUtils.getNormalDateFormat(endDate));
        List<SaleDocumentEntity> list = getSaleDocumentListByPeriod(1, beginDate, endDate);
        System.out.println("For a period of " + list.size() + " documents found");
*/
        // Создаем конструктор счетов-фактур
        SelectionsCreator creator = new SelectionsCreator(exportPath);
        creator.processing();
        //
/*        for (SaleDocumentEntity entity : list) {
            try {
                //Создаем счет фактуру для документа
                creator.createIssuance(entity);
                System.out.println("processing for document [" + entity.getDocumentNumber() + "]");
            } catch (Exception e) {
                System.out.println("error processing for document [" + entity.getDocumentNumber() + "]");
            }
        }*/
    }


    private List<SaleDocumentEntity> getMaterialByPeriod(int documentType, Date dateBegin, Date dateEnd) {
        List<SaleDocumentEntity> result = null;
        DAOSaleDocumentFactory factory = DAOSaleDocumentFactory.getInstance();
        ISaleDocumentDao dao = factory.getSaleDocumentDao();

        try {
            //5307
            result = dao.getSaleDocumentByPeriod(documentType, dateBegin, dateEnd);
        } catch (final Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * Получает список документов для периода
     *
     * @param documentType 0 - все закрытые документы 1 - только закрытые кроме розницы
     * @param dateBegin    начало периода отбора
     * @param dateEnd      конец периода отбора
     * @return список актуальных документов за период
     */
    private List<SaleDocumentEntity> getSaleDocumentListByPeriod(int documentType, Date dateBegin, Date dateEnd) {
        List<SaleDocumentEntity> result = null;
        DAOSaleDocumentFactory factory = DAOSaleDocumentFactory.getInstance();
        ISaleDocumentDao dao = factory.getSaleDocumentDao();
        try {
            //5307
            result = dao.getSaleDocumentByPeriodAndContractor(documentType, dateBegin, dateEnd, 3783);
        } catch (final Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    private boolean isNeedClean() {
        for (String s : bootstrap.getArguments()) {
            if (s.equals("-clean")) {
                return true;
            }
        }
        return false;
    }

    void purgeDirectory(File dir) {
        for (File file : dir.listFiles()) {
            if (file.isDirectory()) {
                purgeDirectory(file);
            }
            file.delete();
        }
    }

}
