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
import by.march8.ecs.application.modules.marketing.model.MarketingRemainsItem;
import by.march8.ecs.application.modules.marketing.model.MarketingRemainsReportData;
import by.march8.ecs.framework.common.Settings;
import by.march8.ecs.framework.common.printers.PrinterSettings;
import by.march8.entities.warehouse.WarehouseRemainsItem;
import com.sun.star.beans.XPropertySet;
import com.sun.star.lang.XComponent;
import com.sun.star.sheet.XSpreadsheet;
import com.sun.star.table.BorderLine;
import com.sun.star.table.TableBorder;
import com.sun.star.table.XCell;
import com.sun.star.uno.UnoRuntime;

import java.util.List;

/**
 * @author Andy 15.05.2018 - 11:04.
 */
public abstract class AbstractMarketingRemainsReport extends AbstractReport implements IReport {
    protected MainController controller;
    protected MarketingRemainsReportData reportData;
    protected MarketingRemainsItem priceList;
    protected List<WarehouseRemainsItem> detailList;
    protected XPropertySet xPropSet;
    protected ProgressBar pb;
    private ReportProperties reportProperties;
    private OpenOfficeConnector document = null;
    private BorderLine aLine = new BorderLine();
    private TableBorder aBorder = new TableBorder();

    public AbstractMarketingRemainsReport(MarketingRemainsReportData report) {
        reportData = report;
        if (report != null) {
            priceList = report.getDocument();
            detailList = report.getData();
        }
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

    private void init() {
        reportProperties = new ReportProperties();
        ReportTemplate template = new ReportTemplate();
        template.setTemplateName("marketingRemains.ots");
        template.setDocumentType(DocumentType.DOCUMENT_ODS);
        reportProperties.setBlankName("marketing_remains_");
        reportProperties.setTemplate(template);
        reportProperties.setPrintable(false);
        reportProperties.setStorable(false);
        reportProperties.setOpenable(true);

        aLine.Color = 0x000000;
        aLine.InnerLineWidth = 0;
        aLine.LineDistance = 0;
        aLine.OuterLineWidth = 25;

        aBorder.BottomLine = aLine;
        aBorder.IsBottomLineValid = true;
    }

    /**
     * Главный обработчик формирования документа
     */
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

    protected void setBorderForRangeCell(XSpreadsheet xSpreadsheet, int column, int row, int size) {
        for (int p = column; p < size; p++) {
            try {
                XCell xCell = xSpreadsheet.getCellByPosition(p, row - 1);
                xPropSet = UnoRuntime
                        .queryInterface(
                                XPropertySet.class,
                                xCell);
                xPropSet.setPropertyValue("TableBorder", aBorder);
            } catch (Exception e) {
                System.err.println("Ошибка установки параметра [TableBorder] для диапазона");
                e.printStackTrace();
            }
        }
    }

    @Override
    public void useToken() {

    }

    @Override
    public void setProgressBar(ProgressBar task) {
        pb = task;
    }
}
