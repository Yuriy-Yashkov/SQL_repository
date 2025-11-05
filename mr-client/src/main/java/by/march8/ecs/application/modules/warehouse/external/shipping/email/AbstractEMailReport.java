package by.march8.ecs.application.modules.warehouse.external.shipping.email;

import by.gomel.freedev.ucframework.uccore.report.ReportManager;
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
import com.sun.star.lang.WrappedTargetException;
import com.sun.star.lang.XComponent;
import com.sun.star.sheet.XSpreadsheet;
import com.sun.star.table.BorderLine;
import com.sun.star.table.CellHoriJustify;
import com.sun.star.table.TableBorder;
import com.sun.star.table.XCell;
import com.sun.star.table.XCellRange;
import com.sun.star.table.XColumnRowRange;
import com.sun.star.uno.AnyConverter;
import com.sun.star.uno.UnoRuntime;

import java.util.HashMap;
import java.util.List;

/**
 * @author Andy 29.11.2017.
 */
public abstract class AbstractEMailReport implements IReport {
    protected MainController controller;
    protected SaleDocumentReport documentReport;
    protected SaleDocumentBase saleDocumentBase = null;
    protected List<SaleDocumentDetailItemReport> detailList = null;
    protected HashMap<String, Object> detailMap = null;
    protected float charSize = (float) 6.5;
    protected XPropertySet xPropSet;
    protected ProgressBar pb;
    private ReportProperties reportProperties;
    private OpenOfficeConnector document = null;
    private BorderLine aLine = new BorderLine();
    private TableBorder aBorder = new TableBorder();


    public AbstractEMailReport(SaleDocumentReport report) {
        documentReport = report;
        if (report.getDocument() != null) {
            saleDocumentBase = documentReport.getDocument();
            detailList = documentReport.getDetailList();
            detailMap = documentReport.getDetailMap();
        }

        init();
    }

    public AbstractEMailReport() {
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

    private void init() {
        reportProperties = new ReportProperties();
        ReportTemplate template = new ReportTemplate();
        template.setTemplateName("email_blank.ots");
        template.setDocumentType(DocumentType.DOCUMENT_XLS);
        reportProperties.setBlankName("");
        reportProperties.setTemplate(template);
        reportProperties.setPrintable(false);
        reportProperties.setStorable(false);
        reportProperties.setOpenable(true);

        //System.out.println("Запрос на установку имени файла - шаблона");

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
                file = manager.saveDocumentDialog(reportProperties.getTemplate().getDocumentType());
            } else {
                manager.saveDocument(file, reportProperties.getTemplate().getDocumentType());
            }
            // Диалог на печать документа
            // manager.printDocumentDialog();
            // Закрываем документ и отсоединяемся от сервера OO
            manager.closeDocument();

/*            // Открываем сохраненный документ
            if (reportProperties.isOpenable()) {
                manager.openDocument(file + "." + reportProperties.getTemplate().getDocumentTypeEntity().getExtension());
            }*/
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

    abstract boolean populateData(XComponent component);

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

    protected void setCharHeightToCell(XCell cell) {
        xPropSet = UnoRuntime
                .queryInterface(com.sun.star.beans.XPropertySet.class,
                        cell);
        try {
            xPropSet.setPropertyValue("CharHeight", charSize);
        } catch (Exception e) {
            System.err.println("Ошибка установки параметра [CharHeight] для ячейки: " + cell);
            e.printStackTrace();
        }
    }

    protected void setCharHeightToCell16(XCell cell) {
        xPropSet = UnoRuntime
                .queryInterface(com.sun.star.beans.XPropertySet.class,
                        cell);
        try {
            xPropSet.setPropertyValue("CharHeight", 10);
            xPropSet.setPropertyValue("CharWeight", com.sun.star.awt.FontWeight.BOLD);


        } catch (Exception e) {
            System.err.println("Ошибка установки параметра [CharHeight] для ячейки: " + cell);
            e.printStackTrace();
        }
    }

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

    protected void setCharHeightForRangeCell(XSpreadsheet xSpreadsheet, int column, int row, int size) {
        for (int p = column; p < size; p++) {
            try {
                XCell xCell = xSpreadsheet.getCellByPosition(p, row);
                xPropSet = UnoRuntime
                        .queryInterface(
                                XPropertySet.class,
                                xCell);
                xPropSet.setPropertyValue("CharHeight", charSize);
            } catch (Exception e) {
                System.err.println("Ошибка установки параметра [TableBorder] для диапазона");
                e.printStackTrace();
            }
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

    protected void rowFormat(XSpreadsheet sheet, int row, int size) {
        float charSize = (float) 6.5;
        com.sun.star.table.BorderLine aLine = new com.sun.star.table.BorderLine();
        aLine.Color = 0x000000;
        aLine.InnerLineWidth = (short) 0;
        aLine.LineDistance = (short) 0;
        aLine.OuterLineWidth = (short) 5;

        com.sun.star.table.TableBorder aBorder = new com.sun.star.table.TableBorder();
        aBorder.BottomLine = aLine;
        aBorder.LeftLine = aLine;
        aBorder.RightLine = aLine;
        aBorder.IsBottomLineValid = true;
        aBorder.IsLeftLineValid = true;
        aBorder.IsRightLineValid = true;
        try {
            for (int z = 0; z < size; z++) {
                XCell xCell = sheet.getCellByPosition(z, row);
                XPropertySet xPropSet = UnoRuntime
                        .queryInterface(XPropertySet.class,
                                xCell);
                xPropSet.setPropertyValue("CharHeight", charSize);
                xPropSet.setPropertyValue("TableBorder", aBorder);
                // xPropSet.setPropertyValue("HoriJustify", com.sun.star.table.CellHoriJustify.CENTER);
                xPropSet.setPropertyValue("VertJustify", com.sun.star.table.CellVertJustify.TOP);
            }
        } catch (Exception e) {
            System.out.println("Ошибка форматирования строки");
        }
    }

    protected void rowFormat(XSpreadsheet sheet, int row, int size, int fontSize) {
        float charSize = (float) fontSize;
        com.sun.star.table.BorderLine aLine = new com.sun.star.table.BorderLine();
        aLine.Color = 0x000000;
        aLine.InnerLineWidth = (short) 0;
        aLine.LineDistance = (short) 0;
        aLine.OuterLineWidth = (short) 5;

        com.sun.star.table.TableBorder aBorder = new com.sun.star.table.TableBorder();
        aBorder.BottomLine = aLine;
        aBorder.LeftLine = aLine;
        aBorder.RightLine = aLine;
        aBorder.IsBottomLineValid = true;
        aBorder.IsLeftLineValid = true;
        aBorder.IsRightLineValid = true;
        try {
            for (int z = 0; z < size; z++) {
                XCell xCell = sheet.getCellByPosition(z, row);
                XPropertySet xPropSet = UnoRuntime
                        .queryInterface(XPropertySet.class,
                                xCell);
                xPropSet.setPropertyValue("CharHeight", charSize);
                xPropSet.setPropertyValue("TableBorder", aBorder);
                // xPropSet.setPropertyValue("HoriJustify", com.sun.star.table.CellHoriJustify.CENTER);
                xPropSet.setPropertyValue("VertJustify", com.sun.star.table.CellVertJustify.TOP);
            }
        } catch (Exception e) {
            System.out.println("Ошибка форматирования строки");
        }
    }

    protected void rowFontSizeForRow(XSpreadsheet sheet, int startRow, int stopRow, int size, int fontSize) {
        float charSize = (float) fontSize;
        try {
            XCellRange xCellRange = sheet.getCellRangeByPosition(0, startRow, size,
                    stopRow);

            XPropertySet xPropSet = UnoRuntime
                    .queryInterface(XPropertySet.class,
                            xCellRange);
            xPropSet.setPropertyValue("CharHeight", charSize);
        } catch (Exception e) {
            System.out.println("Ошибка форматирования строки");
        }
    }

    protected void rowFormat7(XSpreadsheet sheet, int row, int size, int fontSize) {
        float charSize = (float) fontSize;
        com.sun.star.table.BorderLine aLine = new com.sun.star.table.BorderLine();
        aLine.Color = 0x000000;
        aLine.InnerLineWidth = (short) 0;
        aLine.LineDistance = (short) 0;
        aLine.OuterLineWidth = (short) 5;

        com.sun.star.table.TableBorder aBorder = new com.sun.star.table.TableBorder();
        aBorder.BottomLine = aLine;
        aBorder.LeftLine = aLine;
        aBorder.RightLine = aLine;
        aBorder.IsBottomLineValid = true;
        aBorder.IsLeftLineValid = true;
        aBorder.IsRightLineValid = true;
        try {
            for (int z = 0; z < size; z++) {
                XCell xCell = sheet.getCellByPosition(z, row);
                XPropertySet xPropSet = UnoRuntime
                        .queryInterface(XPropertySet.class,
                                xCell);
                xPropSet.setPropertyValue("CharHeight", charSize);
                xPropSet.setPropertyValue("TableBorder", aBorder);
                // xPropSet.setPropertyValue("HoriJustify", com.sun.star.table.CellHoriJustify.CENTER);
                xPropSet.setPropertyValue("VertJustify", com.sun.star.table.CellVertJustify.TOP);
            }
        } catch (Exception e) {
            System.out.println("Ошибка форматирования строки");
        }
    }


    protected void setCustomHeight(XCellRange range, int width) {
        com.sun.star.table.XColumnRowRange xColRowRange = UnoRuntime.queryInterface(XColumnRowRange.class, range);
        com.sun.star.table.XTableRows xRows = xColRowRange.getRows();
        try {
            Object aRowObj = xRows.getByIndex(0);
            XPropertySet xPropSet = UnoRuntime.queryInterface(
                    XPropertySet.class, aRowObj);
            xPropSet.setPropertyValue("Height", width);
        } catch (Exception e) {
            System.out.println("Ошибка");
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

    protected void mergeCellRange(XSpreadsheet xSpreadsheet, int columnStart, int rowStart, int columnEnd, int rowEnd) {
        XCellRange xCellRange = null;
        try {
            xCellRange = xSpreadsheet
                    .getCellRangeByPosition(columnStart, rowStart, columnEnd, rowEnd);
            com.sun.star.util.XMergeable xMerge = UnoRuntime
                    .queryInterface(com.sun.star.util.XMergeable.class,
                            xCellRange);
            xMerge.merge(true);
        } catch (com.sun.star.lang.IndexOutOfBoundsException e) {
            e.printStackTrace();
        }

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
        } catch (com.sun.star.lang.IllegalArgumentException e2) {
            //
            e2.printStackTrace();
        } catch (UnknownPropertyException e3) {
            // getPropertyValue
            e3.printStackTrace();
        }

        documentReport.getDetailMap().put("PAGE_COUNT", "Приложение из " + nPageCount + " лист(ов)");
    }

    @Override
    public void setProgressBar(ProgressBar task) {
        pb = task;
    }
}
