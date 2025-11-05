package by.march8.ecs.application.modules.planning.report;

import by.gomel.freedev.ucframework.uccore.report.model.ReportProperties;
import by.march8.api.utils.DateUtils;
import by.march8.ecs.application.modules.planning.model.ProductionPlanningBucket;
import by.march8.ecs.application.modules.planning.model.ProductionPlanningReport;
import by.march8.entities.planning.PlanningItemComponent;
import by.march8.entities.planning.ProductionPlanningComposition;
import by.march8.entities.planning.ProductionPlanningItem;
import com.sun.star.container.XNamed;
import com.sun.star.lang.XComponent;
import com.sun.star.sheet.XSpreadsheet;
import com.sun.star.sheet.XSpreadsheetDocument;
import com.sun.star.sheet.XSpreadsheets;
import com.sun.star.table.XCell;
import com.sun.star.uno.UnoRuntime;

import java.util.List;

/**
 * @author Andy 07.12.2018 - 10:01.
 */
public class PlanningDocumentReport extends AbstractPlanningDocumentReport {


    public PlanningDocumentReport(ProductionPlanningReport report) {
        super(report);
        create();
    }

    @Override
    ReportProperties prepareProperties(ReportProperties properties) {
        // Устанавливаем имя файла шаблона
        properties.setBlankName("planning_" +
                planningDocument.getDocumentNumber() + "_" +
                DateUtils.getNormalDateFormat(planningDocument.getDocumentDate()));
        properties.getTemplate().setTemplateName("productionPlanning.ots");
        return properties;
    }

    @Override
    boolean populateData(XComponent xComponent) {
        try {
            XSpreadsheetDocument xSpreadsheetDocument = UnoRuntime
                    .queryInterface(XSpreadsheetDocument.class, xComponent);
            XSpreadsheets xSpreadsheets = xSpreadsheetDocument.getSheets();

            String documentInfo = planningDocument.getDocumentInformation();

            // *********************************************************************************
            // ЛИСТ ПЛАН ПРОИЗВОДСТВА
            // *********************************************************************************

            Object sheet = xSpreadsheets.getByName("План производства");
            XSpreadsheet xSpreadsheet = UnoRuntime
                    .queryInterface(XSpreadsheet.class, sheet);

            XNamed xNamed = UnoRuntime.queryInterface(XNamed.class,
                    xSpreadsheet);

            XCell xCell;

            xCell = xSpreadsheet.getCellByPosition(0, 2);
            xCell.setFormula(documentInfo);

            int row = 5;
            // ПОлучим список бакетов для отчета
            for (List<ProductionPlanningBucket> list : productionData) {

                if (list != null) {

                    // В цикле получим список бакетов для артикула
                    for (ProductionPlanningBucket bucket : list) {
                        // Для каждого бакета в артикуле выводим шапку
                        if (bucket != null) {
                            // Наименование изделия из бакета
                            xCell = xSpreadsheet.getCellByPosition(0, row);
                            xCell.setFormula(bucket.getBucketInformation());
                            setBorderForCellRange(xSpreadsheet, 0, 13, row - 1);
                            setFontBoldForCellRange(xSpreadsheet, 0, 13, row);
                            setBorderForCellRange(xSpreadsheet, 0, 13, row);
                            // Распишем состав для бакета
                            int componentColumn = 6;
                            for (ProductionPlanningComposition component : bucket.getSlots()) {
                                xCell = xSpreadsheet.getCellByPosition(componentColumn, row);
                                xCell.setFormula(component.getMaterialName());
                                componentColumn++;
                            }
                            row++;

                            int startRow = row;
                            int n = 1;
                            // Выведем в отчет спецификацию для артикула в пределах бакета
                            for (ProductionPlanningItem item : bucket.getItems()) {
                                // № п/п
                                xCell = xSpreadsheet.getCellByPosition(1, row);
                                xCell.setValue(n);
                                // Росторазмер
                                xCell = xSpreadsheet.getCellByPosition(2, row);
                                xCell.setFormula(String.valueOf(item.getSizePrint()));
                                // Оборудование
                                xCell = xSpreadsheet.getCellByPosition(3, row);
                                xCell.setFormula(String.valueOf(item.getEquipmentName()));
                                // Процент
                                xCell = xSpreadsheet.getCellByPosition(4, row);
                                xCell.setValue(item.getPercent());
                                // Количество
                                xCell = xSpreadsheet.getCellByPosition(5, row);
                                xCell.setValue(item.getAmount());
                                // Компоненты
                                for (PlanningItemComponent component : item.getComponentList()) {
                                    int col = bucket.getSlotNumberByComponentId(component.getComponent());
                                    xCell = xSpreadsheet.getCellByPosition(col + 6, row);
                                    xCell.setValue(component.getUseValue());
                                }
                                n++;
                                row++;
                            }

                            setFontBoldForCellRange(xSpreadsheet, 0, 13, row);
                            setBorderForCellRange(xSpreadsheet, 0, 13, row - 1);
                            // Подведение итогов для бакета
                            xCell = xSpreadsheet.getCellByPosition(3, row);
                            xCell.setFormula("Итого: ");
                            xCell = xSpreadsheet.getCellByPosition(4, row);
                            xCell.setFormula("= SUM(E" + startRow + ":E" + row + ")");
                            xCell = xSpreadsheet.getCellByPosition(5, row);
                            xCell.setFormula("= SUM(F" + startRow + ":F" + row + ")");
                            xCell = xSpreadsheet.getCellByPosition(6, row);
                            xCell.setFormula("= SUM(G" + startRow + ":G" + row + ")");
                            xCell = xSpreadsheet.getCellByPosition(7, row);
                            xCell.setFormula("= SUM(H" + startRow + ":H" + row + ")");
                            xCell = xSpreadsheet.getCellByPosition(8, row);
                            xCell.setFormula("= SUM(I" + startRow + ":I" + row + ")");
                            xCell = xSpreadsheet.getCellByPosition(9, row);
                            xCell.setFormula("= SUM(J" + startRow + ":J" + row + ")");
                            xCell = xSpreadsheet.getCellByPosition(10, row);
                            xCell.setFormula("= SUM(K" + startRow + ":K" + row + ")");
                            xCell = xSpreadsheet.getCellByPosition(11, row);
                            xCell.setFormula("= SUM(L" + startRow + ":L" + row + ")");
                            xCell = xSpreadsheet.getCellByPosition(12, row);
                            xCell.setFormula("= SUM(M" + startRow + ":M" + row + ")");
                            xCell = xSpreadsheet.getCellByPosition(13, row);
                            xCell.setFormula("= SUM(N" + startRow + ":N" + row + ")");
                            setBorderForCellRange(xSpreadsheet, 0, 13, row);
                            row++;
                        }
                        //
                        row++;
                    }
                }
            }

            // *********************************************************************************
            // ЛИСТ РАСХОДА МАТЕРИАЛА
            // *********************************************************************************

            sheet = xSpreadsheets.getByName("Расход сырья");
            xSpreadsheet = UnoRuntime
                    .queryInterface(XSpreadsheet.class, sheet);

            xCell = xSpreadsheet.getCellByPosition(0, 2);
            xCell.setFormula(documentInfo);

            row = 5;
            for (ProductionPlanningComposition composition : planningDocument.getCompositionList()) {
                xCell = xSpreadsheet.getCellByPosition(0, row);
                xCell.setValue(row - 4);

                xCell = xSpreadsheet.getCellByPosition(1, row);
                xCell.setFormula(composition.getMaterialName());

                xCell = xSpreadsheet.getCellByPosition(2, row);
                xCell.setValue(composition.getUseValue());

                setFullBorderForCellRange(xSpreadsheet, 0, 2, row, row);
                row++;
            }

            // *********************************************************************************
            // ЛИСТ ОБОРУДОВАНИЕ
            // *********************************************************************************

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

}
