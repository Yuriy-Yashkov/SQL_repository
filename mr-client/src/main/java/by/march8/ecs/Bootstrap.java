package by.march8.ecs;

import by.march8.ecs.framework.common.Configuration;
import by.march8.ecs.framework.common.Settings;
import by.march8.tasks.factoring.FactoringController;
import by.march8.tasks.maitenance.MaintenanceController;
import by.march8.tasks.mnsati.InvoiceController;
import by.march8.tasks.remains.RemainsController;
import by.march8.tasks.selections.SelectionsController;

import javax.swing.*;

/**
 * Первичный загрузчик загружает ядро (рабочей среды) новой/старой оболочки MyReports .
 * Взаимодействует с конфигурационным файлом
 * Created by Andy on 03.08.2015.
 */
public class Bootstrap {
    private String runPath;
    private Configuration configuration;
    private String[] arguments;

    /**
     * Главный конструктор загрузчика
     *
     * @param runPath путь запуска приложения
     */
    public Bootstrap(String runPath, String[] args) {
        this.runPath = runPath;
        arguments = args;

        configuration = new Configuration(Settings.CONFIG_FILE_NAME);
        SwingWorker sw = new SwingWorker() {
            @Override
            protected Object doInBackground() {
                java.awt.EventQueue.invokeLater(Bootstrap.this::boot);
                return 0;
            }

            @Override
            protected void done() {
//                ss.dispose();
            }
        };
        sw.execute();
    }

    /**
     * Перевод MyReports в режим формирования электронных счетов фактур
     *
     * @return флаг перевода в данный режим
     */
    private boolean isInvoiceMode() {
        for (String s : arguments) {
            if (s.equals("-invoice")) {
                return true;
            }
        }
        return false;
    }

    private boolean isFactoring() {
        for (String s : arguments) {
            if (s.equals("-factoring")) {
                return true;
            }
        }
        return false;
    }

    private boolean isSelections() {
        for (String s : arguments) {
            if (s.equals("-selections")) {
                return true;
            }
        }
        return false;
    }

    private boolean isMaintenance() {
        for (String s : arguments) {
            if (s.equals("-maintenance")) {
                return true;
            }
        }
        return false;
    }


    private boolean isRemains() {
        for (String s : arguments) {
            if (s.equals("-remains")) {
                return true;
            }
        }
        return false;
    }


    /**
     * Перевод MyReports в режим формирования экспортных DBF
     *
     * @return флаг перевода в данный режим
     */
    private boolean isExportMode() {
        for (String s : arguments) {
            if (s.equals("-dbf")) {
                return true;
            }
        }
        return false;
    }

    private void boot() {
        if (isInvoiceMode()) {
            new InvoiceController(this);
        } else if (isRemains()) {
            new RemainsController(this);
        } else if (isExportMode()) {
            new InvoiceController(this, true);
        } else if (isFactoring()) {
            new FactoringController(this);
        } else if (isSelections()) {
            new SelectionsController(this);
        } else if (isMaintenance()) {
            new MaintenanceController(this);
        } else {
            new MainController(this);
        }
    }

    /**
     * Возвращает путь запуска приложения
     *
     * @return возвращает путь запуска
     */
    public String getRunPath() {
        return runPath;
    }

    /**
     * Метод возвращает ссылку на конфигуратор приложения
     *
     * @return ссылка на конфигуратор
     */
    public Configuration getConfiguration() {
        return configuration;
    }

    public String[] getArguments() {
        return arguments;
    }
}
