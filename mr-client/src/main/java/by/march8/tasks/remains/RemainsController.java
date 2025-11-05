package by.march8.tasks.remains;

import by.gomel.freedev.ucframework.uccore.utils.SystemUtils;
import by.march8.ecs.Bootstrap;
import by.march8.tasks.remains.dao.RemainsDBFReader;
import by.march8.tasks.remains.dao.RemainsJDBC;
import by.march8.tasks.remains.logic.RemainsDBFItem;
import common.DateUtils;
import dept.MyReportsModule;

import java.io.File;
import java.util.Date;
import java.util.List;

public class RemainsController {

    /**
     * Загрузчик программы
     */
    private Bootstrap bootstrap;

    /**
     * Каталог для выгрузки документов
     */
    private String exportPath = "\\\\1c8-terminal\\datamatrix\\";

    public RemainsController(Bootstrap bootstrap) {
        this.bootstrap = bootstrap;

        System.out.println("REMAINS CREATOR MODE ACTIVATE...");

        if (!SystemUtils.isWindows()) {
            exportPath = null;
        }

        if (exportPath == null) {
            return;
        }

        File dir = new File(exportPath);

        if (!dir.exists()) {
            System.out.println("Directory [" + exportPath + "] not found, application terminate...");
            System.exit(0);
        }

        System.out.println("Method processingRemainsCreator start ..." + exportPath);
        processingRemainsCreator();
        System.out.println("Method processingRemainsCreator stop ...EXIT");

        System.exit(0);
    }


    private void processingRemainsCreator() {
        String path = System.getProperty("user.home");
        path += "/.MyReports/";
        MyReportsModule.confPath = path;

        RemainsJDBC db = new RemainsJDBC();

        // Очищаем таблицу остатков
        db.dropERPRemainsTable();


        // Получаем файл остатков
        RemainsDBFReader dbf = new RemainsDBFReader();
        List<RemainsDBFItem> remainsDBFItems = dbf.readRemainsDBF(exportPath + DateUtils.getFileNameByDate(new Date()) + ".dbf");
        System.out.println(remainsDBFItems.size());

        // Импорт данных в SQL Server
        db.remainsImport(remainsDBFItems);

        // Обработка данных

        db.prepareRemainsSQL();
    }
}
