package by.march8.tasks.factoring;

import by.gomel.freedev.ucframework.uccore.utils.SystemUtils;
import by.march8.api.utils.DateUtils;
import by.march8.ecs.Bootstrap;
import by.march8.ecs.application.modules.warehouse.external.shipping.dao.DAOSaleDocumentFactory;
import by.march8.ecs.application.modules.warehouse.external.shipping.dao.interfaces.ISaleDocumentDao;
import by.march8.entities.warehouse.SaleDocumentEntity;
import by.march8.tasks.factoring.logic.FactoringCreator;

import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @author Andy 27.02.2018.
 */
public class FactoringController {

    /**
     * Загрузчик программы
     */
    private Bootstrap bootstrap;

    /**
     * Каталог для выгрузки документов
     */
    private String exportPath = "\\\\file-server2\\PRG_Exchange\\export\\";

    public FactoringController(final Bootstrap bootstrap) {
        this.bootstrap = bootstrap;

        System.out.println("INVOICE CREATOR MODE ACTIVATE...");

        if (!SystemUtils.isWindows()) {
            exportPath = "/media/xml/factoring/";
        } else {
            exportPath = "y:\\factoring\\";
        }

        File dir = new File(exportPath);
        if (!dir.exists()) {
            System.out.println("Directory [" + exportPath + "] not found, application terminate...");
            System.exit(0);
        }

        if (isNeedClean()) {
            System.out.println("Cleaning directory ..." + exportPath);
            purgeDirectory(dir);
        }

        System.out.println("Create invoice processingIssuance start ..." + exportPath);
        processingIssuance();
        System.out.println("Create invoice processingIssuance stop ...EXIT");

        System.exit(0);
    }

    private void processingIssuance() {

        Calendar c = Calendar.getInstance();

        // Конечная дата периода отбора, как текущая
        Date endDate = new Date();
        c.setTime(endDate);

        // Устанавливаем начальную дату периода отбора
        int daysToDecrement = -935;
        c.add(Calendar.DATE, daysToDecrement);
        Date beginDate = DateUtils.getDateByStringValue("15.01.2018");//c.getTime();

        // Получаем список актуальных документов за прериод
        System.out.println("Processing dates : " + DateUtils.getNormalDateFormat(beginDate) + " - " + DateUtils.getNormalDateFormat(endDate));
        List<SaleDocumentEntity> list = getSaleDocumentListByPeriod(1, beginDate, endDate);
        System.out.println("For a period of " + list.size() + " documents found");

        // Создаем конструктор счетов-фактур
        FactoringCreator issuanceCreator = new FactoringCreator(exportPath);

        // Возвратные ЕСЧФ с ценами в валюте
        for (SaleDocumentEntity entity : list) {
            try {
                //Создаем счет фактуру для документа
                issuanceCreator.createIssuance(entity);
                System.out.println("processing for document [" + entity.getDocumentNumber() + "]");
            } catch (Exception e) {
                System.out.println("error processing for document [" + entity.getDocumentNumber() + "]");
            }
        }

        issuanceCreator.createErrorReport();
    }

    /**
     * Получает список документов для периода
     *
     * @param documentType 0 - все закрытые документы 1 - только закрытые кроме розницы
     * @param dateBegin начало периода отбора
     * @param dateEnd   конец периода отбора
     * @return список актуальных документов за период
     */
    private List<SaleDocumentEntity> getSaleDocumentListByPeriod(int documentType, Date dateBegin, Date dateEnd) {
        List<SaleDocumentEntity> result = null;
        DAOSaleDocumentFactory factory = DAOSaleDocumentFactory.getInstance();
        ISaleDocumentDao dao = factory.getSaleDocumentDao();
        try {
            result = dao.getSaleDocumentByPeriodAndContractor(documentType, dateBegin, dateEnd, 9193);
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
