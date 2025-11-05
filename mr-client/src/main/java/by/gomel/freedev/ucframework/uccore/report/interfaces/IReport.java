package by.gomel.freedev.ucframework.uccore.report.interfaces;


import by.gomel.freedev.ucframework.uccore.report.api.openoffice.DocumentType;
import by.gomel.freedev.ucframework.uccore.report.model.ReportProperties;
import by.gomel.freedev.ucframework.uccore.report.model.ReportTemplate;
import by.gomel.freedev.ucframework.ucswing.uicontrols.ProgressBar;
import by.march8.ecs.framework.common.printers.PrinterSettings;

/**
 * Интерфейс описывает базовый функционал отчета
 * <p/>
 * Created by Andy on 13.10.2014.
 */
public interface IReport {

    /**
     * Метод возвращает сведения о отчете
     */
    ReportProperties getReportProperties();

    /**
     * Метод содержит функции создания отчета, наполнения данными шаблона и т.д.
     */
    void createReport() throws Exception;

    /**
     * Метод содержит функии сохранения отчета из шаблона в файл savePath
     */
    void saveReport(ReportTemplate template, DocumentType documentType) throws Exception;

    /**
     * Метод содержит функционал вывода отчета на печать
     */
    void printReport(PrinterSettings settings);

    /**
     * Метод содержит функционал при закрытии отчета: освобождение ресурсов, отключение коннектов и т.д.
     */
    void closeReport() throws Exception;

    void openReport(String fileName) throws Exception;

    void useToken();

    void setProgressBar(ProgressBar task);

    /**
     * Метод устанавливает свойства отчету загруженные из внешнего источника
     */
}
