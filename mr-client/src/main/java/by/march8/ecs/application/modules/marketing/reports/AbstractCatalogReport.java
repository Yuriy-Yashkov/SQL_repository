package by.march8.ecs.application.modules.marketing.reports;

import by.gomel.freedev.ucframework.uccore.report.ReportManager;
import by.gomel.freedev.ucframework.uccore.report.api.AbstractReport;
import by.gomel.freedev.ucframework.uccore.report.api.openoffice.DocumentType;
import by.gomel.freedev.ucframework.uccore.report.api.openoffice.OpenOfficeConnector;
import by.gomel.freedev.ucframework.uccore.report.interfaces.IReport;
import by.gomel.freedev.ucframework.uccore.report.model.ReportProperties;
import by.gomel.freedev.ucframework.uccore.report.model.ReportTemplate;
import by.gomel.freedev.ucframework.ucswing.uicontrols.ProgressBar;
import by.march8.ecs.MainController;
import by.march8.ecs.framework.common.Settings;
import by.march8.ecs.framework.common.printers.PrinterSettings;
import com.sun.star.beans.XPropertySet;
import com.sun.star.lang.XComponent;
import com.sun.star.table.BorderLine;
import com.sun.star.table.TableBorder;


public abstract class AbstractCatalogReport extends AbstractReport implements IReport {
    protected MainController controller;
    protected XPropertySet xPropSet;
    protected ProgressBar pb;
    private ReportProperties reportProperties;
    private OpenOfficeConnector document = null;
    private BorderLine aLine = new BorderLine();
    private TableBorder aBorder = new TableBorder();

    public AbstractCatalogReport() {
        init();
    }

    /**
     * метод отложенного запуска формирования документа
     */
    public void create() {
        reportProperties = prepareProperties(reportProperties);
        System.out.println("Шаблон принят к загрузке: " + reportProperties.getTemplate().getTemplateName());
        processing();
    }

    @Override
    public void setProgressBar(ProgressBar task) {
        pb = task;
    }

    private void init() {
        reportProperties = new ReportProperties();
        ReportTemplate template = new ReportTemplate();
        template.setTemplateName("marketingPriceListAddition.ots");
        template.setDocumentType(DocumentType.DOCUMENT_ODS);
        reportProperties.setBlankName("catalog_");
        reportProperties.setTemplate(template);
        reportProperties.setPrintable(false);
        reportProperties.setStorable(false);
        reportProperties.setOpenable(true);
    }

    /**
     * Главный обработчик формирования документа
     */
    private void processing() {
        ReportManager manager = new ReportManager(null, this);
        try {
            manager.createDocument();
            String file = Settings.TEMPORARY_DIR + reportProperties.getBlankName();
            if (reportProperties.isStorable()) {
                if (reportProperties.getSavePath() != null) {
                    file = reportProperties.getSavePath() + reportProperties.getBlankName();
                    manager.saveDocument(file, reportProperties.getTemplate().getDocumentType());
                } else {
                    file = manager.saveDocumentDialog(reportProperties.getTemplate().getDocumentType());
                }
            } else {
                manager.saveDocument(file, reportProperties.getTemplate().getDocumentType());
            }
            manager.closeDocument();
            if (reportProperties.isOpenable()) {
                manager.openDocument(file + "." + reportProperties.getTemplate().getDocumentType().getExtension());
            }
        } catch (Exception ex) {
            MainController.exception(ex, "Ошибка формирования отчета");
        }
    }

    /**
     * Метод для изменения параметров отчета.
     * Важный параметр имя файла шаблона TemplateName
     * По-умолчанию шаблон не сохраняемый (без диалога сохранения),
     * не печатаемый(без диалога печати)
     *
     * @param properties параметры отчета
     * @return параметры отчета
     */
    abstract ReportProperties prepareProperties(ReportProperties properties);

    @Override
    public ReportProperties getReportProperties() {
        return reportProperties;
    }

    @Override
    public void createReport() throws Exception {
        try {
            document = new OpenOfficeConnector(reportProperties.getTemplate());
/*            final XSpreadsheetDocument xSpreadsheetDocument = UnoRuntime
                    .queryInterface(XSpreadsheetDocument.class, document.getXComponent());*/
            populateData(document.getXComponent());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void saveReport(final ReportTemplate template, final DocumentType documentType) {
        document.saveDocument(template, documentType);
    }

    @Override
    public void printReport(final PrinterSettings settings) {

    }

    @Override
    public void closeReport() {
        document.closeDocument();
    }

    @Override
    public void openReport(final String fileName) {
        document.openDocument(fileName);
    }

    abstract boolean populateData(XComponent component);

    @Override
    public void useToken() {

    }
}
