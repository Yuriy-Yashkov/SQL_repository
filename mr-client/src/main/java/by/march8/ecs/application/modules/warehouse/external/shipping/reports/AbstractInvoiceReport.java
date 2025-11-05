package by.march8.ecs.application.modules.warehouse.external.shipping.reports;

import by.gomel.freedev.ucframework.uccore.report.ReportManager;
import by.gomel.freedev.ucframework.uccore.report.api.AbstractReport;
import by.gomel.freedev.ucframework.uccore.report.api.openoffice.DocumentType;
import by.gomel.freedev.ucframework.uccore.report.api.openoffice.OpenOfficeConnector;
import by.gomel.freedev.ucframework.uccore.report.interfaces.IReport;
import by.gomel.freedev.ucframework.uccore.report.model.ReportProperties;
import by.gomel.freedev.ucframework.uccore.report.model.ReportTemplate;
import by.gomel.freedev.ucframework.ucswing.uicontrols.ProgressBar;
import by.march8.ecs.MainController;
import by.march8.ecs.application.modules.warehouse.external.shipping.model.SaleDocumentReport;
import by.march8.ecs.framework.common.Settings;
import by.march8.ecs.framework.common.printers.PrinterSettings;
import by.march8.entities.warehouse.SaleDocumentBase;
import by.march8.entities.warehouse.SaleDocumentDetailItemReport;
import com.sun.star.beans.UnknownPropertyException;
import com.sun.star.beans.XPropertySet;
import com.sun.star.frame.XController;
import com.sun.star.frame.XModel;
import com.sun.star.lang.IllegalArgumentException;
import com.sun.star.lang.WrappedTargetException;
import com.sun.star.lang.XComponent;
import com.sun.star.sheet.XSpreadsheet;
import com.sun.star.table.CellHoriJustify;
import com.sun.star.table.TableBorder;
import com.sun.star.table.XCell;
import com.sun.star.uno.AnyConverter;
import com.sun.star.uno.UnoRuntime;

import java.util.HashMap;
import java.util.List;

/**
 * Абстрактный класс приложений Склада готовой продукции
 *
 * @author Andy 19.05.2016.
 */
public abstract class AbstractInvoiceReport extends AbstractReport implements IReport {
    protected final float CHAR_SIZE = 6.5f;
    protected final float CHAR_SIZE_10 = 10.0f;
    protected MainController controller;
    protected SaleDocumentReport documentReport;
    protected SaleDocumentReport documentReportOld;
    protected SaleDocumentBase saleDocumentBase = null;
    protected List<SaleDocumentDetailItemReport> detailList = null;
    protected HashMap<String, Object> detailMap = null;
    protected SaleDocumentBase saleDocumentBaseOld = null;
    protected List<SaleDocumentDetailItemReport> detailListOld = null;
    protected HashMap<String, Object> detailMapOld = null;
    protected XPropertySet xPropSet;
    protected ProgressBar pb;
    private ReportProperties reportProperties;
    private OpenOfficeConnector document = null;


    public AbstractInvoiceReport(SaleDocumentReport report) {
        documentReport = report;
        if (report.getDocument() != null) {
            saleDocumentBase = documentReport.getDocument();
            detailList = documentReport.getDetailList();
            detailMap = documentReport.getDetailMap();
        }
        init();
    }

    public AbstractInvoiceReport(SaleDocumentReport report, SaleDocumentReport reportOld) {
        documentReport = report;
        documentReportOld = reportOld;
        if (report.getDocument() != null) {
            saleDocumentBase = documentReport.getDocument();
            detailList = documentReport.getDetailList();
            detailMap = documentReport.getDetailMap();
            saleDocumentBaseOld = documentReportOld.getDocument();
            detailListOld = documentReportOld.getDetailList();
            detailMapOld = documentReportOld.getDetailMap();
        }
        init();
    }

    public AbstractInvoiceReport() {
        init();
    }

    /**
     * метод отложенного запуска формирования документа
     */
    public void create() {
        reportProperties = prepareProperties(reportProperties);
        System.out.println("Шаблон принят к загрузке: " + reportProperties.getTemplate().getTemplateName());
        processing(false);
    }

    public void createCustom() {
        reportProperties = prepareProperties(reportProperties);
        System.out.println("Шаблон принят к загрузке: " + reportProperties.getTemplate().getTemplateName());
        processing(true);
    }

    private void init() {
        reportProperties = new ReportProperties();
        ReportTemplate template = new ReportTemplate();
        template.setTemplateName("ПриложениеТТН.ots");
        template.setDocumentType(DocumentType.DOCUMENT_ODS);
        reportProperties.setBlankName("annex_");
        reportProperties.setTemplate(template);
        reportProperties.setPrintable(false);
        reportProperties.setStorable(false);
        reportProperties.setOpenable(true);
        //System.out.println("Запрос на установку имени файла - шаблона");
    }

    /**
     * Главный обработчик формирования документа
     */
    private void processing(boolean isCustom) {

        if (!isCustom) {
            if (saleDocumentBase == null || detailList == null || detailMap == null) {
                System.out.println("Нулевое значение набора данных SaleDocumentShippingReport, формирование невозможно.");
                return;
            }
        }

        ReportManager manager = new ReportManager(null, this);
        try {
            // Создаем документ
            manager.createDocument();
            // Диалог на сохранение документа
            //String file = manager.saveDocumentDialog(DocumentTypeEntity.DOCUMENT_ODS);
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


    protected String getDataAsString(String key) {
        if (detailList != null) {
            String result = (String) detailMap.get(key);
            if (result == null) {
                return "";
            } else {
                return result.trim();
            }
        } else {
            return "";
        }
    }

    protected Object getDataAsObject(String key) {
        if (detailList != null) {
            Object result = detailMap.get(key);
            if (result == null) {
                return "";
            } else {
                return result;
            }
        } else {
            return "";
        }
    }

    /**
     * Задает свойства ячейке. xSpreadsheet - Лист документа column - Номер
     * столбца для вставки row - Номер строки для вставки value - Что будем
     * записывать в строковом формате charSize - Размер шрифта aBorder -
     * Рисовать границу (параметры переменной должны быть заданны до передачи в
     * эту функцию). Если NULL - то граница не рисуется justifyHor - Выравнять
     * текст по горизонтали к правой стороне true/false.
     * <p>
     * Скопировано с SkaldOO
     *
     * @param xSpreadsheet лист документа
     * @param column       номер столбца
     * @param row          номер строки
     * @param value        значение
     * @param charSize     размер шрифта
     * @param aBorder      граница
     * @param justifyHor   выравнивание по горизонтали
     */
    public void setPropertyAndValue(XSpreadsheet xSpreadsheet, int column,
                                    int row, String value, float charSize, TableBorder aBorder,
                                    String justifyHor) {
        try {
            XCell xCell;
            com.sun.star.beans.XPropertySet xPropSet;
            xCell = xSpreadsheet.getCellByPosition(column, row);
            xCell.setFormula(value);
            xPropSet = UnoRuntime
                    .queryInterface(XPropertySet.class,
                            xCell);
            xPropSet.setPropertyValue("CharHeight", charSize);

            if (aBorder != null) {
                xPropSet.setPropertyValue("TableBorder", aBorder);
            }
            if (justifyHor.equals("RIGHT")) {
                xPropSet.setPropertyValue("HoriJustify", CellHoriJustify.RIGHT);
            }
            if (justifyHor.equals("CENTER")) {
                xPropSet.setPropertyValue("HoriJustify", CellHoriJustify.CENTER);
            }
            if (justifyHor.equals("LEFT")) {
                xPropSet.setPropertyValue("HoriJustify", CellHoriJustify.LEFT);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    protected void setPageCount(int rowCount) {
        int pageCount = (rowCount - 36) / 29 + 1;
        pageCount += (rowCount - 36) % 29 > 0 ? 1 : 0;
        documentReport.getDetailMap().put("PAGE_COUNT", "Приложение из " + pageCount + " лист(ов)");
    }

    protected void setPageCountVertical(int rowCount) {
        int page_count = (rowCount - 58) / 50 + 1;
        page_count += (rowCount - 58) % 50 > 0 ? 1 : 0;

        documentReport.getDetailMap().put("PAGE_COUNT", "Приложение из " + page_count + " лист(ов)");
    }

    protected void setPageCount() {
        int nPageCount = 0;
        try {
            XModel xModel = UnoRuntime.queryInterface(
                    XModel.class, document.getXComponent());
            XController xController = xModel.getCurrentController();

            XPropertySet xPropertySet = UnoRuntime.queryInterface(
                    XPropertySet.class, xController);
            nPageCount = AnyConverter.toInt(xPropertySet.getPropertyValue("PageCount"));

        } catch (WrappedTargetException e1) {
            // getPropertyValue
            e1.printStackTrace();
        } catch (IllegalArgumentException e2) {
            //
            e2.printStackTrace();
        } catch (UnknownPropertyException e3) {
            // getPropertyValue
            e3.printStackTrace();
        }

        documentReport.getDetailMap().put("PAGE_COUNT", "Приложение из " + nPageCount + " лист(ов)");
    }

    @Override
    public void useToken() {

    }

    @Override
    public void setProgressBar(ProgressBar task) {
        pb = task;
    }

}
