package by.march8.ecs.application.modules.planning.report;

import by.gomel.freedev.ucframework.uccore.report.ReportManager;
import by.gomel.freedev.ucframework.uccore.report.api.AbstractReport;
import by.gomel.freedev.ucframework.uccore.report.api.openoffice.DocumentType;
import by.gomel.freedev.ucframework.uccore.report.api.openoffice.OpenOfficeConnector;
import by.gomel.freedev.ucframework.uccore.report.interfaces.IReport;
import by.gomel.freedev.ucframework.uccore.report.model.ReportProperties;
import by.gomel.freedev.ucframework.uccore.report.model.ReportTemplate;
import by.gomel.freedev.ucframework.ucswing.uicontrols.ProgressBar;
import by.march8.ecs.MainController;
import by.march8.ecs.application.modules.planning.model.ProductionPlanningBucket;
import by.march8.ecs.application.modules.planning.model.ProductionPlanningReport;
import by.march8.ecs.framework.common.Settings;
import by.march8.ecs.framework.common.printers.PrinterSettings;
import by.march8.entities.planning.ProductionPlanningDocument;
import com.sun.star.beans.XPropertySet;
import com.sun.star.lang.XComponent;

import java.util.List;

/**
 * @author Andy 07.12.2018 - 9:53.
 */
abstract class AbstractPlanningDocumentReport extends AbstractReport implements IReport {

    protected final float CHAR_SIZE = 6.5f;
    protected final float CHAR_SIZE_10 = 10.0f;
    protected MainController controller;
    protected ProductionPlanningReport documentReport;
    protected ProductionPlanningDocument planningDocument;
    protected List<List<ProductionPlanningBucket>> productionData;
    protected XPropertySet xPropSet;

    protected ProgressBar pb;
    private ReportProperties reportProperties;
    private OpenOfficeConnector document = null;


    public AbstractPlanningDocumentReport(ProductionPlanningReport report) {
        documentReport = report;
        if (report != null) {
            planningDocument = report.getDocument();
            productionData = report.getProductionData();
        }
        init();
    }

    @Override
    public void setProgressBar(ProgressBar task) {
        pb = task;
    }

    /**
     * метод отложенного запуска формирования документа
     */
    public void create() {
        reportProperties = prepareProperties(reportProperties);
        System.out.println("Шаблон принят к загрузке: " + reportProperties.getTemplate().getTemplateName());
        processing();
    }


    private void init() {
        reportProperties = new ReportProperties();
        ReportTemplate template = new ReportTemplate();
        template.setTemplateName("productionPlanning.ots");
        template.setDocumentType(DocumentType.DOCUMENT_ODS);
        reportProperties.setBlankName("planning_");
        reportProperties.setTemplate(template);
        reportProperties.setPrintable(false);
        reportProperties.setStorable(false);
        reportProperties.setOpenable(true);
    }

    abstract ReportProperties prepareProperties(ReportProperties properties);

    private void processing() {
        ReportManager manager = new ReportManager(null, this);
        try {
            // Создаем документ
            manager.createDocument();
            // Диалог на сохранение документа
            //String file = manager.saveDocumentDialog(DocumentTypeEntity.DOCUMENT_ODS);
            String file = Settings.TEMPORARY_DIR + reportProperties.getBlankName();

            if (reportProperties.isStorable()) {
                file = manager.saveDocumentDialog(reportProperties.getTemplate().getDocumentType());
            } else {
                manager.saveDocument(file, reportProperties.getTemplate().getDocumentType());
            }
            // Диалог на печать документа
            // manager.printDocumentDialog();
            // Закрываем документ и отсоединяемся от сервера OO
            manager.closeDocument();

            // Открываем сохраненный документ
            if (reportProperties.isOpenable()) {
                manager.openDocument(file + "." + reportProperties.getTemplate().getDocumentType().getExtension());
            }
        } catch (Exception ex) {
            MainController.exception(ex, "Ошибка формирования отчета");
        }
    }

    abstract boolean populateData(XComponent component);

    @Override
    public void useToken() {

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
}
