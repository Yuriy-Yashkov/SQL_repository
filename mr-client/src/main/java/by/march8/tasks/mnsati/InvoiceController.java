package by.march8.tasks.mnsati;

import by.gomel.freedev.ucframework.uccore.interfaces.ApplicationController;
import by.gomel.freedev.ucframework.uccore.utils.SystemUtils;
import by.march8.api.utils.DateUtils;
import by.march8.ecs.Bootstrap;
import by.march8.ecs.application.modules.warehouse.external.shipping.analysis.AbstractDataAnalysis;
import by.march8.ecs.application.modules.warehouse.external.shipping.model.DocumentUploadPreset;
import by.march8.entities.warehouse.SaleDocumentEntity;
import by.march8.tasks.accounting.AccountingCreator;
import by.march8.tasks.mnsati.logic.IssuanceCreator;

import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Автономный режим для формирования электронных счетов-фактур.
 * <p>
 * Для активации использовать аргументы -invoice -clean
 * при запуске MyReports.jar
 * <p>
 * -invoice переводит MyReports в данный режим
 * <p>
 * -clean чистит директорию перед началом работы
 *
 * @author Andy 21.06.2016.
 */
public class InvoiceController extends AbstractDataAnalysis implements ApplicationController {

    /**
     * Загрузчик программы
     */
    private Bootstrap bootstrap;

    /**
     * Каталог для выгрузки документов
     */
    private String exportPath = "\\\\file-server2\\PRG_Exchange\\export\\";
    private String exportPathCurrency = "\\\\file-server2\\PRG_Exchange\\export_currency\\";

    public InvoiceController(final Bootstrap bootstrap) {
        this.bootstrap = bootstrap;

        System.out.println("INVOICE CREATOR MODE ACTIVATE...");

        if (!SystemUtils.isWindows()) {
            exportPath = "/media/xml/nakl_MyReports/";
            exportPathCurrency = "/media/xml/nakl_curr_MyReports/";
        } else {
            exportPath = "\\\\terminal-srv6\\xml\\nakl_MyReports\\";
            exportPathCurrency = "\\\\terminal-srv6\\xml\\nakl_curr_MyReports\\";
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

        dir = new File(exportPathCurrency);
        if (!dir.exists()) {
            System.out.println("Directory [" + exportPathCurrency + "] not found, application terminate...");
            System.exit(0);
        }

        if (isNeedClean()) {
            System.out.println("Cleaning directory ..." + exportPathCurrency);
            purgeDirectory(dir);
        }

        System.out.println("Create invoice processingIssuance start ..." + exportPath);
        processingIssuance();
        System.out.println("Create invoice processingIssuance stop ...EXIT");

        System.exit(0);
    }

    @SuppressWarnings("unused")
    public InvoiceController(final Bootstrap bootstrap, boolean isExport) {
        this.bootstrap = bootstrap;

        System.out.println("EXPORT DBF CREATOR MODE ACTIVATE...");

        if (!SystemUtils.isWindows()) {
            exportPath = "/media/xml/nakl_MyReports/";
        } else {
            exportPath = "\\\\terminal-srv6\\xml\\nakl_MyReports\\";
        }

        File dir = new File(exportPath);
        if (!dir.exists()) {
            System.out.println("Directory [" + exportPath + "] not found, application terminate...");
            System.exit(0);
        }

/*        if (isNeedClean()) {
            System.out.println("Cleaning directory ..." + exportPath);
            purgeDirectory(dir);
        }*/

        System.out.println("Create dbf processingAccounting start ..." + exportPath);
        processingAccounting(isAccountingMode(), isOldLineMode());
        System.out.println("Create dbf processingAccounting stop ...EXIT");

        System.exit(0);
    }

    /**
     * ФОрмирование DBF структуры типа NAKL.dbf конкретного  контаргента
     * @param bootstrap загрузчик
     * @param contractorCode код контрагента
     */
    public InvoiceController(final Bootstrap bootstrap, int contractorCode) {
        this.bootstrap = bootstrap;

        System.out.println("EXPORT DBF CREATOR MODE ACTIVATE...");

        if (!SystemUtils.isWindows()) {
            exportPath = "/media/xml/nakl_MyReports/";
        } else {
            exportPath = "y:\\nakl_MyReports\\";
        }

        File dir = new File(exportPath);
        if (!dir.exists()) {
            System.out.println("Directory [" + exportPath + "] not found, application terminate...");
        }

/*        if (isNeedClean()) {
            System.out.println("Cleaning directory ..." + exportPath);
            purgeDirectory(dir);
        }*/

        System.out.println("Create MOST.dbf processingAccounting start ..." + exportPath);
        processingAccountingByContractor(contractorCode);
        System.out.println("Create MOST.dbf processingAccounting stop ...EXIT");
    }

    public InvoiceController(DocumentUploadPreset preset) {
        if (preset == null) {
            return;
        }

        this.bootstrap = null;


        System.out.println("EXPORT DBF CREATOR MODE ACTIVATE...");

        if (!SystemUtils.isWindows()) {
            exportPath = "/media/xml/nakl_MyReports/";
        } else {
            exportPath = "y:\\nakl_MyReports\\";
        }

        File dir = new File(exportPath);
        if (!dir.exists()) {
            System.out.println("Directory [" + exportPath + "] not found, application terminate...");
        }

        System.out.println("Create CUSTOM.dbf processingAccounting start ..." + exportPath);
        processingAccountingByContractor(preset);
        System.out.println("Create MOST.dbf processingAccounting stop ...EXIT");
    }

    /**
     * Перевод MyReports в режим формирования экспортных DBF для 1С Бухгалтерии
     *
     * @return флаг перевода в данный режим
     */
    private boolean isAccountingMode() {
        for (String s : bootstrap.getArguments()) {
            if (s.equals("-accounting")) {
                return true;
            }
        }
        return false;
    }

    /**
     * Перевод MyReports в режим формирования экспортных DBF для старой линии
     *
     * @return флаг перевода в данный режим
     */
    private boolean isOldLineMode() {
        for (String s : bootstrap.getArguments()) {
            if (s.equals("-oldline")) {
                return true;
            }
        }
        return false;
    }

    /**
     * Если аргументами коммандной строки передана команда
     * очистить дирректорию перед началом работы
     *
     * @return флаг очистки дирректории
     */
    private boolean isNeedClean() {
        for (String s : bootstrap.getArguments()) {
            if (s.equals("-clean")) {
                return true;
            }
        }
        return false;
    }

    /**
     * Рекурсивная чистка каталога выгрузки, ЧИСТИТ КАТАЛОГ ПОЛНОСТЬЮ
     *
     * @param dir начальная дирректория
     */
    @SuppressWarnings("all")
    void purgeDirectory(File dir) {
        for (File file : dir.listFiles()) {
            if (file.isDirectory()) {
                purgeDirectory(file);
            }
            file.delete();
        }
    }

    /**
     * Главный метод обработки документов
     */
    private void processingIssuance() {

        Calendar c = Calendar.getInstance();

        // Конечная дата периода отбора, как текущая
        //Date endDate = (new GregorianCalendar(2019, 11 , 31)).getTime();
        Date endDate = new Date();
        c.setTime(endDate);
        // Устанавливаем начальную дату периода отбора
        int daysToDecrement = -70;
        c.add(Calendar.DATE, daysToDecrement);
        Date beginDate = c.getTime();

        // Получаем список актуальных документов за прериод
        System.out.println("Processing dates : " + DateUtils.getNormalDateFormat(beginDate) + " - " + DateUtils.getNormalDateFormat(endDate));
        List<SaleDocumentEntity> list = getSaleDocumentListByPeriod(1, beginDate, endDate);
        System.out.println("For a period of " + list.size() + " documents found");

        // Создаем конструктор счетов-фактур
        IssuanceCreator issuanceCreator = new IssuanceCreator(exportPath);
        issuanceCreator.setExportPathCurrency(exportPathCurrency);
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
    }


    @SuppressWarnings("unused")
    private void processingAccounting(boolean isAccounting, boolean isOldLine) {

        Calendar c = Calendar.getInstance();

        // Конечная дата периода отбора, как текущая
        Date endDate = new Date();
        c.setTime(endDate);

        // Устанавливаем начальную дату периода отбора
        int daysToDecrement = -70;
        c.add(Calendar.DATE, daysToDecrement);
        Date beginDate = c.getTime();

        // Получаем список актуальных документов за прериод
        System.out.println("Processing dates : " + DateUtils.getNormalDateFormat(beginDate) + " - " + DateUtils.getNormalDateFormat(endDate));
        List<SaleDocumentEntity> list = getSaleDocumentListByPeriod(0, beginDate, endDate);
        System.out.println("For a period of " + list.size() + " documents found");

        // Создаем конструктор для формирования экспортных DBF файлов
        new AccountingCreator(isAccounting, false, list);
    }

    private void processingAccountingByContractor(DocumentUploadPreset preset) {
        // Получаем список актуальных документов за прериод
        System.out.println("Processing dates : " + DateUtils.getNormalDateFormat(preset.getPeriodBegin()) + " - " + DateUtils.getNormalDateFormat(preset.getPeriodEnd()));
        List<SaleDocumentEntity> list = getSaleDocumentListByPeriodAndContractor(preset.getContractorCode(), preset.getPeriodBegin(), preset.getPeriodEnd());
        System.out.println("For a period of " + list.size() + " documents found");

        // Создаем конструктор для формирования экспортных DBF файлов
        new AccountingCreator(list, preset);
    }

    private void processingAccountingByContractor(int contractorCode) {
        Calendar c = Calendar.getInstance();
        // Конечная дата периода отбора, как текущая
        Date endDate = new Date();
        c.setTime(endDate);

        // Устанавливаем начальную дату периода отбора
        int daysToDecrement = -70;
        c.add(Calendar.DATE, daysToDecrement);
        Date beginDate = c.getTime();

        // Получаем список актуальных документов за прериод
        System.out.println("Processing dates : " + DateUtils.getNormalDateFormat(beginDate) + " - " + DateUtils.getNormalDateFormat(endDate));
        List<SaleDocumentEntity> list = getSaleDocumentListByPeriodAndContractor(contractorCode, beginDate, endDate);
        System.out.println("For a period of " + list.size() + " documents found");

        // Создаем конструктор для формирования экспортных DBF файлов
        new AccountingCreator(list, null);
    }


}
