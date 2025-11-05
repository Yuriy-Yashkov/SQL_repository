package by.gomel.freedev.ucframework.uccore.report;


import by.gomel.freedev.ucframework.uccore.report.api.openoffice.DocumentType;
import by.gomel.freedev.ucframework.uccore.report.interfaces.IReport;
import by.gomel.freedev.ucframework.uccore.report.model.ReportProperties;
import by.gomel.freedev.ucframework.uccore.report.model.ReportTemplate;
import by.gomel.freedev.ucframework.uccore.utils.FileUtils;
import by.march8.ecs.MainController;
import by.march8.ecs.framework.common.BackgroundTask;
import by.march8.ecs.framework.common.printers.PrinterSettings;

import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.print.PrinterJob;
import java.io.File;

/**
 * Менеджер отчетов, не окончательный, будет допиливаться ооочень долго.
 * Created by Andy on 15.10.2014.
 */
@Deprecated
public class ReportManager {

    //private final MainController controller;
    private IReport report;

    public ReportManager(MainController mainController) {
        //controller = mainController;
    }

    public ReportManager(MainController mainController, IReport iReport) {
        //controller = mainController;
        report = iReport;
        initialReport(report);
    }

    private IReport initialReport(IReport iReport) {
        // Получаем у отчета информацию для поиска его в БД
        // Получаем информацию о отчете для дальнейшего использования
        ReportProperties info = iReport.getReportProperties();

        // Возвращает отчет на верх для последующей работы
        return iReport;
    }

    /**
     * Метод создания документа: Открытие шаблона, создание пустого документа по шаблону,
     * заполнение документа данными
     */
    public boolean createDocument() {
        if (report == null) {
            return false;
        }

        class Task extends BackgroundTask {
            public Task(final String messageText) {
                super(messageText);
            }

            @Override
            protected Object doInBackground() throws Exception {
                report.setProgressBar(this.getProgressBar());
                report.createReport();
                return 0;
            }
        }

        Task task = new Task("Создание документа");
        task.executeTask();
        return true;
    }

    /**
     * Метод закрывает заполненный данными документ, без сохранения
     */
    public boolean closeDocument() {
        if (report == null) {
            return false;
        }

        class Task extends BackgroundTask {
            public Task(final String messageText) {
                super(messageText);
            }

            @Override
            protected Object doInBackground() throws Exception {
                try {
                    report.closeReport();
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("Ошибка фонового потока closeDocument ()");
                }
                return 0;
            }
        }

        Task task = new Task("Закрытие документа");
        task.executeTask();

        return true;
    }

    /**
     * Метод производит сохранение документа по fileName с указанием типа документа DocumentTypeEntity
     */
    public boolean saveDocument(final String fileName, final DocumentType type) {
        if (report == null) {
            return false;
        }

        class Task extends BackgroundTask {
            public Task(final String messageText) {
                super(messageText);
            }

            @Override
            protected Object doInBackground() throws Exception {
                try {
                    File file = new File(fileName);
                    ReportTemplate template = report.getReportProperties().getTemplate();
                    // Задаем имя для файла
                    template.setSaveName(file.getName());
                    // Задаем каталог для сохранения файла
                    template.setSavePath(FileUtils.extractPath(fileName));
                    report.saveReport(template, type);
                } catch (Exception e) {
                    System.out.println("Ошибка фонового потока saveDocument(final String " + fileName + ")");
                    e.printStackTrace();
                }
                return 0;
            }
        }

        Task task = new Task("Сохранение документа");
        task.executeTask();

        return true;
    }

    /**
     * Метод вызывает диалог сохранить файл c последующим сохранением  в выбранный тип DocumentTypeEntity
     */
    public String saveDocumentDialog(DocumentType documentType) {
        FileNameExtensionFilter filter = new FileNameExtensionFilter(documentType.getDescription(), documentType.getExtension());
        JFileChooser dialog = new JFileChooser();
        dialog.setFileFilter(filter);
        dialog.setDialogTitle("Сохранить документ как " + documentType.getExtension() + " файл");

        int ret = dialog.showSaveDialog(null);
        if (ret == JFileChooser.APPROVE_OPTION) {
            File saveFile = dialog.getSelectedFile();
            try {
                String file = saveFile.getAbsolutePath() + "." + documentType.getExtension();
                saveDocument(saveFile.getAbsolutePath(), documentType);
                return file;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        return null;
    }

    public boolean printDocument() {
        class Task extends BackgroundTask {
            public Task(final String messageText) {
                super(messageText);
            }

            @Override
            protected Object doInBackground() throws Exception {
                try {
                    report.printReport(null);
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("Ошибка фонового потока printDocument ");
                }
                return 0;
            }
        }

        Task task = new Task("Печать документа");
        task.executeTask();
        return true;
    }

    /**
     * Метод вызывает диалог выбора принтера перед выводом на принтер
     */
    public void printDocumentDialog() {
        PrinterJob printerJob = PrinterJob.getPrinterJob();
        PrintRequestAttributeSet aset = new HashPrintRequestAttributeSet();
        //PageFormat pf = job.pageDialog(aset);
        //job.setPrintable(new PrintDialogExample(), pf);
        if (printerJob.printDialog(aset)) {
            PrinterSettings settings = new PrinterSettings();
            settings.setPrinterProperties(printerJob.getPrintService());
            report.printReport(settings);
        }
    }

    /**
     * Метод открывает документ
     */
    public void openDocument(String fileName) {
        try {
            report.openReport(fileName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
