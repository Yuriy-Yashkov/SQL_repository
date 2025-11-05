package by.march8.ecs.application.modules.warehouse.external.shipping.reports;

import by.gomel.freedev.ucframework.uccore.report.ReportManager;
import by.gomel.freedev.ucframework.uccore.report.api.openoffice.DocumentType;
import by.gomel.freedev.ucframework.uccore.report.api.openoffice.OpenOfficeConnector;
import by.gomel.freedev.ucframework.uccore.report.interfaces.IReport;
import by.gomel.freedev.ucframework.uccore.report.model.ReportProperties;
import by.gomel.freedev.ucframework.uccore.report.model.ReportTemplate;
import by.gomel.freedev.ucframework.ucdao.DaoFactory;
import by.gomel.freedev.ucframework.ucdao.interfaces.INativeDao;
import by.gomel.freedev.ucframework.ucswing.uicontrols.ProgressBar;
import by.march8.ecs.MainController;
import by.march8.ecs.application.modules.warehouse.external.shipping.model.March8Marker;
import by.march8.ecs.framework.common.Settings;
import by.march8.ecs.framework.common.printers.PrinterSettings;
import by.march8.entities.warehouse.SaleDocumentEntity;
import com.sun.star.beans.XPropertySet;
import com.sun.star.lang.XComponent;
import com.sun.star.sheet.XSpreadsheet;
import com.sun.star.table.CellHoriJustify;
import com.sun.star.table.TableBorder;
import com.sun.star.table.XCell;
import com.sun.star.uno.UnoRuntime;

import java.sql.Connection;
import java.util.List;

/**
 * @author Andy 04.08.2016.
 */
public abstract class AbstractCustomSpreadsheetReport implements IReport {

    protected MainController controller;
    protected ProgressBar pb;
    protected List<SaleDocumentEntity> documentList = null;
    private ReportProperties reportProperties;
    private OpenOfficeConnector document = null;


    public AbstractCustomSpreadsheetReport() {
        init();
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

    public void create() {
        reportProperties = prepareProperties(reportProperties);
        prepareDataSet();
        processing();
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

    /**
     * Формирование и заполнение XLS документа
     *
     * @param component компонент сервера LibOffice
     * @return флаг успешного завершения
     */
    abstract boolean populateData(XComponent component);

    private boolean prepareDataSet() {
        DaoFactory factory = DaoFactory.getInstance();
        INativeDao dao = factory.getNativeDao();
        Class<?> dbMarker = March8Marker.class;
        dao.processing(connection -> {
            try {
                getDocumentList(connection);
            } catch (Exception ex) {
                System.err.println("Ошибка получение списка документов "
                        + ex.getMessage());
            }
            return true;
        }, dbMarker);
        return true;
    }

    /**
     * Первичный отбор документов по определенному критерию (Native SQL запрос), возвращает список документов типа SaleDocumentEntity
     *
     * @param connection ссылкуа на коннект Hibernate
     * @return список документов по критерию
     * @throws Exception
     */
    abstract boolean getDocumentList(Connection connection) throws Exception;

    @Override
    public void setProgressBar(ProgressBar task) {
        pb = task;
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
}
