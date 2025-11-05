package by.march8.ecs.application.modules.plan.report;

import by.gomel.freedev.ucframework.uccore.enums.MarchPlanType;
import by.gomel.freedev.ucframework.uccore.report.api.openoffice.DocumentType;
import by.gomel.freedev.ucframework.uccore.report.api.openoffice.OpenOfficeConnector;
import by.gomel.freedev.ucframework.uccore.report.interfaces.IReport;
import by.gomel.freedev.ucframework.uccore.report.model.ReportProperties;
import by.gomel.freedev.ucframework.uccore.report.model.ReportTemplate;
import by.gomel.freedev.ucframework.ucswing.uicontrols.ProgressBar;
import by.march8.ecs.MainController;
import by.march8.ecs.framework.common.printers.PrinterSettings;
import by.march8.entities.plan.PlanItem;
import com.sun.star.beans.PropertyVetoException;
import com.sun.star.beans.UnknownPropertyException;
import com.sun.star.beans.XPropertySet;
import com.sun.star.lang.IllegalArgumentException;
import com.sun.star.lang.WrappedTargetException;
import com.sun.star.sheet.XSpreadsheet;
import com.sun.star.sheet.XSpreadsheetDocument;
import com.sun.star.table.XCell;
import com.sun.star.table.XCellRange;
import com.sun.star.uno.UnoRuntime;

import java.io.File;
import java.util.ArrayList;

/**
 *
 * Created by dpliushchai on 20.11.2014.
 */
@SuppressWarnings("all")
public class PlanReport<T> implements IReport {
    private final MainController controller;
    com.sun.star.table.TableBorder aBorder = null;
    XPropertySet xPropSet = null;
    com.sun.star.table.BorderLine aLine = null;
    XSpreadsheet sheet = null;
    XSpreadsheetDocument xSpreadsheetDocument;
    XCell rCell = null;
    int sumIssuedTinctorial;
    int sumIssuedClose;
    int sumShippedContractors;
    int sumClose;
    int sumIssuedTailoring;
    int sumRentedWarehouse;
    int sumRentedWarehouseContractors;
    int sumPlan;
    int sumDeviation;
    int sumCarryovers;
    int sumCarryoversContractors;
    int sumUpack;
    private ReportProperties reportProperties;
    private OpenOfficeConnector document = null;
    private ArrayList<Object> listObject;
    private MarchPlanType planReport;
    public PlanReport(final MainController controller,
                      final MarchPlanType planReport, ArrayList<Object> listObj) {
        this.controller = controller;
        this.planReport = planReport;
        this.listObject = listObj;

    }

    @Override
    public void setProgressBar(ProgressBar task) {

    }

    @Override
    public ReportProperties getReportProperties() {
        reportProperties = new ReportProperties();
        ReportTemplate template = new ReportTemplate();
        switch (planReport) {
            case PLAN_REPORT:
                template.setTemplateName("rep.ots");
                break;
            case PLAN_REPORT_MARKETING:
                template.setTemplateName("repM.ots");
                break;
            default:
                break;
        }
        // template.setTemplateName("rep.ots");
        reportProperties.setTemplate(template);
        reportProperties.setPrintable(false);
        reportProperties.setStorable(false);
        return reportProperties;
    }

    @Override
    public void createReport() throws Exception {
        switch (planReport) {
            case PLAN_REPORT:
                planReport();
                break;

            case PLAN_REPORT_MARKETING:
                planReportMarketing();
                break;
        }
    }

    @Override
    public void saveReport(ReportTemplate template, final DocumentType documentType) throws Exception {
        document.saveDocument(template, documentType);
    }

    @Override
    public void printReport(PrinterSettings settings) {
        document.printDocument(settings);
    }

    @Override
    public void closeReport() throws Exception {
        document.closeDocument();
    }

    @Override
    public void openReport(final String fileName) throws Exception {
        document.openDocument(fileName);
    }

    public String getLink(XCell cell) {
        String result = "";
        String cellValue = cell.getFormula();
        if (cellValue.length() > 2) {
            String firstSymbol = cellValue.substring(0, 1);
            if (firstSymbol.equals("$")) {
                result = cellValue.substring(1, cellValue.length());
            }
        }
        return result;
    }

    public MainController getController() {
        return controller;
    }

    public MarchPlanType getPlanReport() {
        return planReport;
    }


    public void planReport() {
        try {
            document = new OpenOfficeConnector(reportProperties.getTemplate());
            xSpreadsheetDocument = (XSpreadsheetDocument) UnoRuntime
                    .queryInterface(XSpreadsheetDocument.class, document.getXComponent());

            // ArrayList<Object> listObject = dao.loadDataAll(MarchPlanType.PLAN_REPORT,planId);
            ArrayList<PlanItem> list = new ArrayList<PlanItem>();
            for (int i = 0; i < listObject.size(); i++) {

                list.add((PlanItem) listObject.get(i));
            }


            aLine = new com.sun.star.table.BorderLine();
            aLine.Color = 0x000000;
            aLine.InnerLineWidth = aLine.LineDistance = 0;
            aLine.OuterLineWidth = 1;
            aBorder = new com.sun.star.table.TableBorder();
            aBorder.TopLine = aBorder.BottomLine = aBorder.LeftLine = aBorder.RightLine = aLine;
            aBorder.IsTopLineValid = aBorder.IsBottomLineValid = true;
            aBorder.IsLeftLineValid = aBorder.IsRightLineValid = true;

            sheet = document.getSheetByName("Лист1");

            /**
             * Заполняем шапку
             */
            int x, y; // Координаты ( столбец , строка)

            x = 1;
            y = 3;

            rCell = sheet.getCellByPosition(x, y);
            xPropSet = UnoRuntime.queryInterface(XPropertySet.class, rCell);
            xPropSet.setPropertyValue("TableBorder", aBorder);
            rCell.setFormula("Ассортимент");
            x++;

            rCell = sheet.getCellByPosition(x, y);
            xPropSet = UnoRuntime.queryInterface(XPropertySet.class, rCell);
            xPropSet.setPropertyValue("TableBorder", aBorder);
            rCell.setFormula("Модель");
            x++;

            rCell = sheet.getCellByPosition(x, y);
            xPropSet = UnoRuntime.queryInterface(XPropertySet.class, rCell);
            xPropSet.setPropertyValue("TableBorder", aBorder);
            rCell.setFormula("Размерная шкала");
            x++;

            rCell = sheet.getCellByPosition(x, y);
            xPropSet = UnoRuntime.queryInterface(XPropertySet.class, rCell);
            xPropSet.setPropertyValue("TableBorder", aBorder);
            rCell.setFormula("Цена");
            x++;

            rCell = sheet.getCellByPosition(x, y);
            xPropSet = UnoRuntime.queryInterface(XPropertySet.class, rCell);
            xPropSet.setPropertyValue("TableBorder", aBorder);
            rCell.setFormula("Новинка");
            x++;

            rCell = sheet.getCellByPosition(x, y);
            xPropSet = UnoRuntime.queryInterface(XPropertySet.class, rCell);
            xPropSet.setPropertyValue("TableBorder", aBorder);
            rCell.setFormula("Картинка");


            x++;

            rCell = sheet.getCellByPosition(x, y);
            xPropSet = UnoRuntime.queryInterface(XPropertySet.class, rCell);
            xPropSet.setPropertyValue("TableBorder", aBorder);
            rCell.setFormula("Артикул");
            x++;

            rCell = sheet.getCellByPosition(x, y);
            xPropSet = UnoRuntime.queryInterface(XPropertySet.class, rCell);
            xPropSet.setPropertyValue("TableBorder", aBorder);
            rCell.setFormula("Артикул полотна");
            x++;

            rCell = sheet.getCellByPosition(x, y);
            xPropSet = UnoRuntime.queryInterface(XPropertySet.class, rCell);
            xPropSet.setPropertyValue("TableBorder", aBorder);
            rCell.setFormula("Состав");
            x++;

            rCell = sheet.getCellByPosition(x, y);
            xPropSet = UnoRuntime.queryInterface(XPropertySet.class, rCell);
            xPropSet.setPropertyValue("TableBorder", aBorder);
            rCell.setFormula("Цвет ");
            x++;

            rCell = sheet.getCellByPosition(x, y);
            xPropSet = UnoRuntime.queryInterface(XPropertySet.class, rCell);
            xPropSet.setPropertyValue("TableBorder", aBorder);
            rCell.setFormula("Переходящие остатки");
            x++;

            rCell = sheet.getCellByPosition(x, y);
            xPropSet = UnoRuntime.queryInterface(XPropertySet.class, rCell);
            xPropSet.setPropertyValue("TableBorder", aBorder);
            rCell.setFormula("Выдано в красильный цех ");
            x++;

            rCell = sheet.getCellByPosition(x, y);
            xPropSet = UnoRuntime.queryInterface(XPropertySet.class, rCell);
            xPropSet.setPropertyValue("TableBorder", aBorder);
            rCell.setFormula("Вып .Выдано в красильный цех , %");
            x++;


            rCell = sheet.getCellByPosition(x, y);
            xPropSet = UnoRuntime.queryInterface(XPropertySet.class, rCell);
            xPropSet.setPropertyValue("TableBorder", aBorder);
            rCell.setFormula("Выдано на закрой ,шт");
            x++;

            rCell = sheet.getCellByPosition(x, y);
            xPropSet = UnoRuntime.queryInterface(XPropertySet.class, rCell);
            xPropSet.setPropertyValue("TableBorder", aBorder);
            rCell.setFormula("Вып .Выдано на закрой  ,%");
            x++;

            rCell = sheet.getCellByPosition(x, y);
            xPropSet = UnoRuntime.queryInterface(XPropertySet.class, rCell);
            xPropSet.setPropertyValue("TableBorder", aBorder);
            rCell.setFormula("Отгружено подрядчикам ,шт");
            x++;

            rCell = sheet.getCellByPosition(x, y);
            xPropSet = UnoRuntime.queryInterface(XPropertySet.class, rCell);
            xPropSet.setPropertyValue("TableBorder", aBorder);
            rCell.setFormula("Закроено ,шт");
            x++;

            rCell = sheet.getCellByPosition(x, y);
            xPropSet = UnoRuntime.queryInterface(XPropertySet.class, rCell);
            xPropSet.setPropertyValue("TableBorder", aBorder);
            rCell.setFormula("Выполнено закроено + пер. ост ,% ");
            x++;

            rCell = sheet.getCellByPosition(x, y);
            xPropSet = UnoRuntime.queryInterface(XPropertySet.class, rCell);
            xPropSet.setPropertyValue("TableBorder", aBorder);
            rCell.setFormula("Выдано на пошив ,шт");
            x++;

            rCell = sheet.getCellByPosition(x, y);
            xPropSet = UnoRuntime.queryInterface(XPropertySet.class, rCell);
            xPropSet.setPropertyValue("TableBorder", aBorder);
            rCell.setFormula("Выдано  на упаковку , шт");
            x++;

            rCell = sheet.getCellByPosition(x, y);
            xPropSet = UnoRuntime.queryInterface(XPropertySet.class, rCell);
            xPropSet.setPropertyValue("TableBorder", aBorder);
            xPropSet.setPropertyValue("CharWeight", com.sun.star.awt.FontWeight.BOLD);
            rCell.setFormula("План , шт");
            x++;

            rCell = sheet.getCellByPosition(x, y);
            xPropSet = UnoRuntime.queryInterface(XPropertySet.class, rCell);
            xPropSet.setPropertyValue("TableBorder", aBorder);
            rCell.setFormula("Сдано на склад , шт");
            x++;

            rCell = sheet.getCellByPosition(x, y);
            xPropSet = UnoRuntime.queryInterface(XPropertySet.class, rCell);
            xPropSet.setPropertyValue("TableBorder", aBorder);
            rCell.setFormula("Сдано на склад подрядчики ,шт");
            x++;


            rCell = sheet.getCellByPosition(x, y);
            xPropSet = UnoRuntime.queryInterface(XPropertySet.class, rCell);
            xPropSet.setPropertyValue("TableBorder", aBorder);
            rCell.setFormula("Выполнение плана ,%");
            x++;

            rCell = sheet.getCellByPosition(x, y);
            xPropSet = UnoRuntime.queryInterface(XPropertySet.class, rCell);
            xPropSet.setPropertyValue("TableBorder", aBorder);
            rCell.setFormula("Примечание");
            x++;


            y++;

            int[] xy = new int[2];


            xy = repType(list, x, y, "Мужской ассортимент", 1);
            System.out.println(xy[0] + "   " + xy[1]);
            xy = repType(list, xy[0], xy[1], "Женский ассортимент", 2);
            xy = repType(list, xy[0], xy[1], "Детский ассортимент", 3);
            repResult(xy[0], xy[1]);


            String[] names = document.getElementNames();

//            XCell cBN = document.getCellByName(sheet, "range_col3");
            // cBN.setFormula("Ячейка интервала по имени");
            // cBN = document.getCellByName(sheet, "range_col4");
            // cBN.setFormula("Ячейка 4");


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public int[] repType(ArrayList<PlanItem> planList, int x, int y, String type, int type_id) throws com.sun.star.lang.IndexOutOfBoundsException, UnknownPropertyException, PropertyVetoException, WrappedTargetException, IllegalArgumentException {

        int sumIssuedTinctorialForType = 0;
        int sumIssuedCloseForType = 0;
        int sumShippedContractorsForType = 0;
        int sumCloseForType = 0;
        int sumIssuedTailoringForType = 0;
        int sumRentedWarehouseForType = 0;
        int sumRentedWarehouseContractorsForType = 0;
        int sumPlanForType = 0;
        int sumDeviationForType = 0;
        int sumCarryoversType = 0;
        int sumCarryoversContractorsType = 0;
        int sumUpackType = 0;
        int realizationPlan = 0;
        int realizationZakr = 0;

        XCellRange range = sheet.getCellRangeByName("B" + ++y + ":" + "Z" + y);
        xPropSet = UnoRuntime.queryInterface(XPropertySet.class, range);

        xPropSet.setPropertyValue("TableBorder", aBorder);
        rCell = range.getCellByPosition(0, 0);
        rCell.setFormula(type);


        x = 1;
        y++;

        for (int i = 0; i < planList.size(); i++) {
            if (planList.get(i).getType().getId() == type_id) {

                //  ArrayList<PlanItem> planListType = new ArrayList<>();
                // pl

                PlanItem plan = new PlanItem();
                plan = planList.get(i);

                rCell = sheet.getCellByPosition(x, y);
                xPropSet = UnoRuntime.queryInterface(XPropertySet.class, rCell);
                xPropSet.setPropertyValue("TableBorder", aBorder);
                rCell.setFormula(plan.getAssortmentName());
                x++;

                rCell = sheet.getCellByPosition(x, y);
                xPropSet = UnoRuntime.queryInterface(XPropertySet.class, rCell);
                xPropSet.setPropertyValue("TableBorder", aBorder);
                rCell.setFormula(plan.getModel() + "");

                x++;

                rCell = sheet.getCellByPosition(x, y);
                xPropSet = UnoRuntime.queryInterface(XPropertySet.class, rCell);
                xPropSet.setPropertyValue("TableBorder", aBorder);

                String size[] = plan.getSize().split(",");
                for (int j = 0; j < size.length; j++) {
                    if (j == 0) {
                        rCell.setFormula(size[j].trim());
                    } else {
                        rCell.setFormula(rCell.getFormula().trim() + "\n\n" + size[j].trim());
                    }

                }


                x++;

                rCell = sheet.getCellByPosition(x, y);
                xPropSet = UnoRuntime.queryInterface(XPropertySet.class, rCell);
                xPropSet.setPropertyValue("TableBorder", aBorder);
                String cost[] = plan.getCost().split(",");
                for (int j = 0; j < cost.length; j++) {
                    if (j == 0) {
                        rCell.setFormula(cost[j].trim());
                    } else {
                        rCell.setFormula(rCell.getFormula().trim() + "\n\n" + cost[j].trim());
                    }
                }
                x++;

                rCell = sheet.getCellByPosition(x, y);
                xPropSet = UnoRuntime.queryInterface(XPropertySet.class, rCell);
                xPropSet.setPropertyValue("TableBorder", aBorder);
                rCell.setFormula(plan.getNovetly());
                x++;

                rCell = sheet.getCellByPosition(x, y);
                rCell.setFormula("");
                xPropSet = UnoRuntime.queryInterface(XPropertySet.class, rCell);
                xPropSet.setPropertyValue("TableBorder", aBorder);

                if (plan.getImage() != null) {
                    //System.out.println(plan.getImage().replace('\\', '/').replace("$", "").replaceAll("//file-server.local/Public" , "/nfs/Public"));
                    //
                    File fC = new File("c:\\windows\\explorer.exe");
                    if (!fC.exists()) {
                        rCell.setFormula(plan.getImage().replace('\\', '/').replace("$", "").replaceAll("//file-server.local/Public", "/nfs/Public"));
                    } else {
                        //System.out.println(plan.getImage().replace('\\', '/').replaceAll("/nfs/Public/catalog" , "888"));
                        rCell.setFormula(plan.getImage().replace('\\', '/').replaceAll("/nfs/Public/catalog", "//file-server.local/Public'\\$\\'/catalog").replace("'", ""));
                        // rCell.setFormula(plan.getImage().replace('\\', '/'));
                    }

                } else {
                    rCell.setFormula("нет");
                }

                x++;

                rCell = sheet.getCellByPosition(x, y);
                xPropSet = UnoRuntime.queryInterface(XPropertySet.class, rCell);
                xPropSet.setPropertyValue("TableBorder", aBorder);
                //  xPropSet.setPropertyValue("GraphicURL" , "D:\\2.jpg");
                rCell.setFormula(plan.getArticle());
                x++;

                rCell = sheet.getCellByPosition(x, y);
                xPropSet = UnoRuntime.queryInterface(XPropertySet.class, rCell);
                xPropSet.setPropertyValue("TableBorder", aBorder);
                rCell.setFormula(plan.getArticle_canvas());
                x++;

                rCell = sheet.getCellByPosition(x, y);
                xPropSet = UnoRuntime.queryInterface(XPropertySet.class, rCell);
                xPropSet.setPropertyValue("TableBorder", aBorder);
                rCell.setFormula(plan.getCompositionCanvas());
                x++;

                rCell = sheet.getCellByPosition(x, y);
                xPropSet = UnoRuntime.queryInterface(XPropertySet.class, rCell);
                xPropSet.setPropertyValue("TableBorder", aBorder);

                rCell.setFormula(plan.getColorCanvas() + "");
                x++;

                rCell = sheet.getCellByPosition(x, y);
                xPropSet = UnoRuntime.queryInterface(XPropertySet.class, rCell);
                xPropSet.setPropertyValue("TableBorder", aBorder);
                rCell.setFormula(plan.getCarryovers() + "(" + plan.getFactor() + ")");
                sumCarryovers += plan.getCarryovers();
                x++;

                rCell = sheet.getCellByPosition(x, y);
                xPropSet = UnoRuntime.queryInterface(XPropertySet.class, rCell);
                xPropSet.setPropertyValue("TableBorder", aBorder);
                rCell.setFormula(plan.getIssuedTinctorial() + "(" + plan.getFactor() + ")");
                sumIssuedTinctorialForType += plan.getIssuedTinctorial();
                x++;


                rCell = sheet.getCellByPosition(x, y);
                xPropSet = UnoRuntime.queryInterface(XPropertySet.class, rCell);
                xPropSet.setPropertyValue("TableBorder", aBorder);
                if (plan.getPlan() != 0) {

                    rCell.setFormula("" + (int) (100 * ((plan.getIssuedTinctorial() * 1.0 / plan.getPlan()))));
                } else {

                    rCell.setFormula(0 + "");
                }
                x++;


                rCell = sheet.getCellByPosition(x, y);
                xPropSet = UnoRuntime.queryInterface(XPropertySet.class, rCell);
                xPropSet.setPropertyValue("TableBorder", aBorder);
                rCell.setFormula(plan.getIssuedClose() + "(" + plan.getFactor() + ")");
                sumIssuedCloseForType += plan.getIssuedClose();
                x++;


                rCell = sheet.getCellByPosition(x, y);
                xPropSet = UnoRuntime.queryInterface(XPropertySet.class, rCell);
                xPropSet.setPropertyValue("TableBorder", aBorder);
                if (plan.getPlan() != 0) {

                    rCell.setFormula("" + (int) (100 * (((plan.getIssuedClose()) * 1.0 / plan.getPlan()))));
                } else {

                    rCell.setFormula(0 + "");
                }
                x++;


                rCell = sheet.getCellByPosition(x, y);
                xPropSet = UnoRuntime.queryInterface(XPropertySet.class, rCell);
                xPropSet.setPropertyValue("TableBorder", aBorder);
                rCell.setFormula(plan.getShippedContractors() + "(" + plan.getFactor() + ")");
                sumShippedContractorsForType += plan.getShippedContractors();
                x++;

                rCell = sheet.getCellByPosition(x, y);
                xPropSet = UnoRuntime.queryInterface(XPropertySet.class, rCell);
                xPropSet.setPropertyValue("TableBorder", aBorder);
                rCell.setFormula(plan.getClose() + "(" + plan.getFactor() + ")");
                sumCloseForType += plan.getClose();
                x++;

                rCell = sheet.getCellByPosition(x, y);
                xPropSet = UnoRuntime.queryInterface(XPropertySet.class, rCell);
                xPropSet.setPropertyValue("TableBorder", aBorder);
                if (plan.getPlan() != 0) {

                    rCell.setFormula("" + (int) (100 * (((plan.getClose() + plan.getCarryovers()) * 1.0 / plan.getPlan()))));
                } else {

                    rCell.setFormula(0 + "");
                }
                x++;

                rCell = sheet.getCellByPosition(x, y);
                xPropSet = UnoRuntime.queryInterface(XPropertySet.class, rCell);
                xPropSet.setPropertyValue("TableBorder", aBorder);
                rCell.setFormula(plan.getIssuedTailoring() + "(" + plan.getFactor() + ")");
                sumIssuedTailoringForType += plan.getIssuedTailoring();
                x++;


                rCell = sheet.getCellByPosition(x, y);
                xPropSet = UnoRuntime.queryInterface(XPropertySet.class, rCell);
                xPropSet.setPropertyValue("TableBorder", aBorder);
                rCell.setFormula(plan.getUpack_sum() + "(" + plan.getFactor() + ")");
                sumUpackType += plan.getUpack_sum();
                x++;


                rCell = sheet.getCellByPosition(x, y);
                xPropSet = UnoRuntime.queryInterface(XPropertySet.class, rCell);
                xPropSet.setPropertyValue("TableBorder", aBorder);
                rCell.setFormula(plan.getPlan() + "(" + plan.getFactor() + ")");
                sumPlanForType += plan.getPlan();
                x++;

                rCell = sheet.getCellByPosition(x, y);
                xPropSet = UnoRuntime.queryInterface(XPropertySet.class, rCell);
                xPropSet.setPropertyValue("TableBorder", aBorder);
                rCell.setFormula(plan.getRented_warehouse() + "(" + plan.getFactor() + ")");
                sumRentedWarehouseForType += plan.getRented_warehouse();
                x++;

                rCell = sheet.getCellByPosition(x, y);
                xPropSet = UnoRuntime.queryInterface(XPropertySet.class, rCell);
                xPropSet.setPropertyValue("TableBorder", aBorder);
                rCell.setFormula(plan.getRented_warehouse_contractors() + "(" + plan.getFactor() + ")");
                sumRentedWarehouseContractorsForType += plan.getRented_warehouse_contractors();
                x++;

                rCell = sheet.getCellByPosition(x, y);
                xPropSet = UnoRuntime.queryInterface(XPropertySet.class, rCell);
                xPropSet.setPropertyValue("TableBorder", aBorder);
                rCell.setFormula(plan.getDeviation_percent() + "");

                x++;

                rCell = sheet.getCellByPosition(x, y);
                xPropSet = UnoRuntime.queryInterface(XPropertySet.class, rCell);
                xPropSet.setPropertyValue("TableBorder", aBorder);
                rCell.setFormula(plan.getNote() + "");

                x++;


                x = 1;
                y++;
            }

        }

        rCell = sheet.getCellByPosition(x, y);
        xPropSet = UnoRuntime.queryInterface(XPropertySet.class, rCell);
        xPropSet.setPropertyValue("TableBorder", aBorder);
        rCell.setFormula("Итого");
        x++;

        rCell = sheet.getCellByPosition(x, y);
        xPropSet = UnoRuntime.queryInterface(XPropertySet.class, rCell);
        xPropSet.setPropertyValue("TableBorder", aBorder);
        rCell.setFormula("");
        x++;

        rCell = sheet.getCellByPosition(x, y);
        xPropSet = UnoRuntime.queryInterface(XPropertySet.class, rCell);
        xPropSet.setPropertyValue("TableBorder", aBorder);
        rCell.setFormula("");
        x++;

        rCell = sheet.getCellByPosition(x, y);
        xPropSet = UnoRuntime.queryInterface(XPropertySet.class, rCell);
        xPropSet.setPropertyValue("TableBorder", aBorder);
        rCell.setFormula("");
        x++;

        rCell = sheet.getCellByPosition(x, y);
        xPropSet = UnoRuntime.queryInterface(XPropertySet.class, rCell);
        xPropSet.setPropertyValue("TableBorder", aBorder);
        rCell.setFormula("");
        x++;

        rCell = sheet.getCellByPosition(x, y);
        xPropSet = UnoRuntime.queryInterface(XPropertySet.class, rCell);
        xPropSet.setPropertyValue("TableBorder", aBorder);
        rCell.setFormula("");
        x++;

        rCell = sheet.getCellByPosition(x, y);
        xPropSet = UnoRuntime.queryInterface(XPropertySet.class, rCell);
        xPropSet.setPropertyValue("TableBorder", aBorder);
        rCell.setFormula("");
        x++;

        rCell = sheet.getCellByPosition(x, y);
        xPropSet = UnoRuntime.queryInterface(XPropertySet.class, rCell);
        xPropSet.setPropertyValue("TableBorder", aBorder);
        rCell.setFormula("");
        x++;

        rCell = sheet.getCellByPosition(x, y);
        xPropSet = UnoRuntime.queryInterface(XPropertySet.class, rCell);
        xPropSet.setPropertyValue("TableBorder", aBorder);
        rCell.setFormula("");
        x++;

        rCell = sheet.getCellByPosition(x, y);
        xPropSet = UnoRuntime.queryInterface(XPropertySet.class, rCell);
        xPropSet.setPropertyValue("TableBorder", aBorder);
        rCell.setFormula("");
        x++;

        rCell = sheet.getCellByPosition(x, y);
        xPropSet = UnoRuntime.queryInterface(XPropertySet.class, rCell);
        xPropSet.setPropertyValue("TableBorder", aBorder);
        rCell.setFormula(sumCarryovers + "");

        x++;

        rCell = sheet.getCellByPosition(x, y);
        xPropSet = UnoRuntime.queryInterface(XPropertySet.class, rCell);
        xPropSet.setPropertyValue("TableBorder", aBorder);
        rCell.setFormula(sumIssuedTinctorialForType + "");

        x++;

        rCell = sheet.getCellByPosition(x, y);
        xPropSet = UnoRuntime.queryInterface(XPropertySet.class, rCell);
        xPropSet.setPropertyValue("TableBorder", aBorder);
        rCell.setFormula(100 * sumIssuedTinctorialForType / sumPlanForType + "");

        x++;

        rCell = sheet.getCellByPosition(x, y);
        xPropSet = UnoRuntime.queryInterface(XPropertySet.class, rCell);
        xPropSet.setPropertyValue("TableBorder", aBorder);
        rCell.setFormula(sumIssuedCloseForType + "");

        x++;

        rCell = sheet.getCellByPosition(x, y);
        xPropSet = UnoRuntime.queryInterface(XPropertySet.class, rCell);
        xPropSet.setPropertyValue("TableBorder", aBorder);
        rCell.setFormula(100 * sumIssuedCloseForType / sumPlanForType + "");

        x++;

        rCell = sheet.getCellByPosition(x, y);
        xPropSet = UnoRuntime.queryInterface(XPropertySet.class, rCell);
        xPropSet.setPropertyValue("TableBorder", aBorder);
        rCell.setFormula(sumShippedContractorsForType + "");

        x++;

        rCell = sheet.getCellByPosition(x, y);
        xPropSet = UnoRuntime.queryInterface(XPropertySet.class, rCell);
        xPropSet.setPropertyValue("TableBorder", aBorder);
        rCell.setFormula(sumCloseForType + "");

        x++;

        rCell = sheet.getCellByPosition(x, y);
        xPropSet = UnoRuntime.queryInterface(XPropertySet.class, rCell);
        xPropSet.setPropertyValue("TableBorder", aBorder);
        rCell.setValue(100 * (sumCloseForType + sumCarryoversType) / sumPlanForType);

        x++;

        rCell = sheet.getCellByPosition(x, y);
        xPropSet = UnoRuntime.queryInterface(XPropertySet.class, rCell);
        xPropSet.setPropertyValue("TableBorder", aBorder);
        rCell.setFormula(sumIssuedTailoringForType + "");

        x++;


        rCell = sheet.getCellByPosition(x, y);
        xPropSet = UnoRuntime.queryInterface(XPropertySet.class, rCell);
        xPropSet.setPropertyValue("TableBorder", aBorder);
        rCell.setFormula(sumUpackType + "");

        x++;

        rCell = sheet.getCellByPosition(x, y);
        xPropSet = UnoRuntime.queryInterface(XPropertySet.class, rCell);
        xPropSet.setPropertyValue("TableBorder", aBorder);
        rCell.setFormula(sumPlanForType + "");

        x++;

        rCell = sheet.getCellByPosition(x, y);
        xPropSet = UnoRuntime.queryInterface(XPropertySet.class, rCell);
        xPropSet.setPropertyValue("TableBorder", aBorder);
        rCell.setFormula(sumRentedWarehouseForType + "");

        x++;

        rCell = sheet.getCellByPosition(x, y);
        xPropSet = UnoRuntime.queryInterface(XPropertySet.class, rCell);
        xPropSet.setPropertyValue("TableBorder", aBorder);
        rCell.setFormula(sumRentedWarehouseContractorsForType + "");

        x++;

        rCell = sheet.getCellByPosition(x, y);
        xPropSet = UnoRuntime.queryInterface(XPropertySet.class, rCell);
        xPropSet.setPropertyValue("TableBorder", aBorder);
        rCell.setValue(100 * (sumRentedWarehouseContractorsForType + sumRentedWarehouseForType) / sumPlanForType);
        x++;


        y += 2;
        // System.out.println(y);


        sumIssuedTinctorial += sumIssuedTinctorialForType;
        sumIssuedClose += sumIssuedCloseForType;
        sumShippedContractors += sumShippedContractorsForType;
        sumClose = sumCloseForType;
        sumIssuedTailoring += sumIssuedTailoringForType;
        sumRentedWarehouse += sumRentedWarehouseForType;
        sumRentedWarehouseContractors += sumRentedWarehouseContractorsForType;
        sumPlan += sumPlanForType;
        sumDeviation += sumDeviationForType;
        sumCarryovers += sumCarryoversType;
        sumCarryoversContractors += sumCarryoversContractorsType;

        int[] xy = new int[2];
        xy[0] = x;
        xy[1] = y;

        return xy;
    }

    public void repResult(int x, int y) throws com.sun.star.lang.IndexOutOfBoundsException, UnknownPropertyException, PropertyVetoException, WrappedTargetException, IllegalArgumentException {


        x = 1;
        y++;


        rCell = sheet.getCellByPosition(x, y);
        xPropSet = UnoRuntime.queryInterface(XPropertySet.class, rCell);
        xPropSet.setPropertyValue("TableBorder", aBorder);
        rCell.setFormula("Всего");
        x++;

        rCell = sheet.getCellByPosition(x, y);
        xPropSet = UnoRuntime.queryInterface(XPropertySet.class, rCell);
        xPropSet.setPropertyValue("TableBorder", aBorder);
        rCell.setFormula("");
        x++;

        rCell = sheet.getCellByPosition(x, y);
        xPropSet = UnoRuntime.queryInterface(XPropertySet.class, rCell);
        xPropSet.setPropertyValue("TableBorder", aBorder);
        rCell.setFormula("");
        x++;

        rCell = sheet.getCellByPosition(x, y);
        xPropSet = UnoRuntime.queryInterface(XPropertySet.class, rCell);
        xPropSet.setPropertyValue("TableBorder", aBorder);
        rCell.setFormula("");
        x++;

        rCell = sheet.getCellByPosition(x, y);
        xPropSet = UnoRuntime.queryInterface(XPropertySet.class, rCell);
        xPropSet.setPropertyValue("TableBorder", aBorder);
        rCell.setFormula("");
        x++;

        rCell = sheet.getCellByPosition(x, y);
        xPropSet = UnoRuntime.queryInterface(XPropertySet.class, rCell);
        xPropSet.setPropertyValue("TableBorder", aBorder);
        rCell.setFormula("");
        x++;

        rCell = sheet.getCellByPosition(x, y);
        xPropSet = UnoRuntime.queryInterface(XPropertySet.class, rCell);
        xPropSet.setPropertyValue("TableBorder", aBorder);
        rCell.setFormula("");
        x++;

        rCell = sheet.getCellByPosition(x, y);
        xPropSet = UnoRuntime.queryInterface(XPropertySet.class, rCell);
        xPropSet.setPropertyValue("TableBorder", aBorder);
        rCell.setFormula("");
        x++;

        rCell = sheet.getCellByPosition(x, y);
        xPropSet = UnoRuntime.queryInterface(XPropertySet.class, rCell);
        xPropSet.setPropertyValue("TableBorder", aBorder);
        rCell.setFormula("");
        x++;

        rCell = sheet.getCellByPosition(x, y);
        xPropSet = UnoRuntime.queryInterface(XPropertySet.class, rCell);
        xPropSet.setPropertyValue("TableBorder", aBorder);
        rCell.setFormula("");
        x++;

        rCell = sheet.getCellByPosition(x, y);
        xPropSet = UnoRuntime.queryInterface(XPropertySet.class, rCell);
        xPropSet.setPropertyValue("TableBorder", aBorder);
        rCell.setFormula(sumCarryovers + "");

        x++;

        rCell = sheet.getCellByPosition(x, y);
        xPropSet = UnoRuntime.queryInterface(XPropertySet.class, rCell);
        xPropSet.setPropertyValue("TableBorder", aBorder);
        rCell.setFormula(sumIssuedTinctorial + "");

        x++;

        rCell = sheet.getCellByPosition(x, y);
        xPropSet = UnoRuntime.queryInterface(XPropertySet.class, rCell);
        xPropSet.setPropertyValue("TableBorder", aBorder);
        rCell.setFormula(100 * sumIssuedTinctorial / sumPlan + "");

        x++;

        rCell = sheet.getCellByPosition(x, y);
        xPropSet = UnoRuntime.queryInterface(XPropertySet.class, rCell);
        xPropSet.setPropertyValue("TableBorder", aBorder);
        rCell.setFormula(sumIssuedClose + "");

        x++;

        rCell = sheet.getCellByPosition(x, y);
        xPropSet = UnoRuntime.queryInterface(XPropertySet.class, rCell);
        xPropSet.setPropertyValue("TableBorder", aBorder);
        rCell.setFormula(100 * (sumIssuedClose) / sumPlan + "");

        x++;

        rCell = sheet.getCellByPosition(x, y);
        xPropSet = UnoRuntime.queryInterface(XPropertySet.class, rCell);
        xPropSet.setPropertyValue("TableBorder", aBorder);
        rCell.setFormula(sumShippedContractors + "");

        x++;

        rCell = sheet.getCellByPosition(x, y);
        xPropSet = UnoRuntime.queryInterface(XPropertySet.class, rCell);
        xPropSet.setPropertyValue("TableBorder", aBorder);
        rCell.setFormula(sumClose + "");

        x++;

        rCell = sheet.getCellByPosition(x, y);
        xPropSet = UnoRuntime.queryInterface(XPropertySet.class, rCell);
        xPropSet.setPropertyValue("TableBorder", aBorder);
        rCell.setValue((sumClose + sumCarryovers) * 100 / sumPlan);

        x++;

        rCell = sheet.getCellByPosition(x, y);
        xPropSet = UnoRuntime.queryInterface(XPropertySet.class, rCell);
        xPropSet.setPropertyValue("TableBorder", aBorder);
        rCell.setFormula(sumIssuedTailoring + "");

        x++;

        rCell = sheet.getCellByPosition(x, y);
        xPropSet = UnoRuntime.queryInterface(XPropertySet.class, rCell);
        xPropSet.setPropertyValue("TableBorder", aBorder);
        rCell.setFormula(sumUpack + "");

        x++;

        rCell = sheet.getCellByPosition(x, y);
        xPropSet = UnoRuntime.queryInterface(XPropertySet.class, rCell);
        xPropSet.setPropertyValue("TableBorder", aBorder);
        rCell.setFormula(sumPlan + "");
        x++;

        rCell = sheet.getCellByPosition(x, y);
        xPropSet = UnoRuntime.queryInterface(XPropertySet.class, rCell);
        xPropSet.setPropertyValue("TableBorder", aBorder);
        rCell.setFormula(sumRentedWarehouse + "");

        x++;

        rCell = sheet.getCellByPosition(x, y);
        xPropSet = UnoRuntime.queryInterface(XPropertySet.class, rCell);
        xPropSet.setPropertyValue("TableBorder", aBorder);
        rCell.setFormula(sumRentedWarehouseContractors + "");

        x++;

        rCell = sheet.getCellByPosition(x, y);
        xPropSet = UnoRuntime.queryInterface(XPropertySet.class, rCell);
        xPropSet.setPropertyValue("TableBorder", aBorder);
        rCell.setValue(100 * (sumRentedWarehouseContractors + sumRentedWarehouseContractors) / sumPlan);
        x++;


        x++;
        y += 2;
        System.out.println(y);


    }

    public void planReportMarketing() {

        try {
            document = new OpenOfficeConnector(reportProperties.getTemplate());
            xSpreadsheetDocument = (XSpreadsheetDocument) UnoRuntime
                    .queryInterface(XSpreadsheetDocument.class, document.getXComponent());

            // ArrayList<Object> listObject = dao.loadDataAll(MarchPlanType.PLAN_REPORT,planId);
            ArrayList<PlanItem> list = new ArrayList<PlanItem>();
            for (int i = 0; i < listObject.size(); i++) {

                list.add((PlanItem) listObject.get(i));
            }


            aLine = new com.sun.star.table.BorderLine();
            aLine.Color = 0x000000;
            aLine.InnerLineWidth = aLine.LineDistance = 0;
            aLine.OuterLineWidth = 1;
            aBorder = new com.sun.star.table.TableBorder();
            aBorder.TopLine = aBorder.BottomLine = aBorder.LeftLine = aBorder.RightLine = aLine;
            aBorder.IsTopLineValid = aBorder.IsBottomLineValid = true;
            aBorder.IsLeftLineValid = aBorder.IsRightLineValid = true;

            sheet = document.getSheetByName("Лист1");

            /**
             * Заполняем шапку
             */
            int x, y; // Координаты ( столбец , строка)

            x = 1;
            y = 3;

            rCell = sheet.getCellByPosition(x, y);
            xPropSet = UnoRuntime.queryInterface(XPropertySet.class, rCell);
            xPropSet.setPropertyValue("TableBorder", aBorder);
            rCell.setFormula("Ассортимент");
            x++;

            rCell = sheet.getCellByPosition(x, y);
            xPropSet = UnoRuntime.queryInterface(XPropertySet.class, rCell);
            xPropSet.setPropertyValue("TableBorder", aBorder);
            rCell.setFormula("Модель");
            x++;

            rCell = sheet.getCellByPosition(x, y);
            xPropSet = UnoRuntime.queryInterface(XPropertySet.class, rCell);
            xPropSet.setPropertyValue("TableBorder", aBorder);
            rCell.setFormula("Размерная шкала");
            x++;

            rCell = sheet.getCellByPosition(x, y);
            xPropSet = UnoRuntime.queryInterface(XPropertySet.class, rCell);
            xPropSet.setPropertyValue("TableBorder", aBorder);
            rCell.setFormula("Состав полотна");


            x++;

            rCell = sheet.getCellByPosition(x, y);
            xPropSet = UnoRuntime.queryInterface(XPropertySet.class, rCell);
            xPropSet.setPropertyValue("TableBorder", aBorder);
            rCell.setFormula("Цена");
            x++;

            rCell = sheet.getCellByPosition(x, y);
            xPropSet = UnoRuntime.queryInterface(XPropertySet.class, rCell);
            xPropSet.setPropertyValue("TableBorder", aBorder);
            rCell.setFormula("Примечание");
            x++;

            rCell = sheet.getCellByPosition(x, y);
            xPropSet = UnoRuntime.queryInterface(XPropertySet.class, rCell);
            xPropSet.setPropertyValue("TableBorder", aBorder);
            rCell.setFormula("Эскиз модели");
            x++;

            rCell = sheet.getCellByPosition(x, y);
            xPropSet = UnoRuntime.queryInterface(XPropertySet.class, rCell);
            xPropSet.setPropertyValue("TableBorder", aBorder);
            rCell.setFormula("Выпуск штук ");
            x++;


            y++;

            int[] xy = new int[2];


            xy = repTypeM(list, x, y, "Мужской ассортимент", 1);
            System.out.println(xy[0] + "   " + xy[1]);
            xy = repTypeM(list, xy[0], xy[1], "Женский ассортимент", 2);
            xy = repTypeM(list, xy[0], xy[1], "Детский ассортимент", 3);
            //  repResult(xy[0], xy[1]);


            String[] names = document.getElementNames();

//            XCell cBN = document.getCellByName(sheet, "range_col3");
            // cBN.setFormula("Ячейка интервала по имени");
            // cBN = document.getCellByName(sheet, "range_col4");
            // cBN.setFormula("Ячейка 4");


        } catch (
                Exception e
        ) {
            e.printStackTrace();
        }
    }

    public int[] repTypeM(ArrayList<PlanItem> planList, int x, int y, String type, int type_id) throws com.sun.star.lang.IndexOutOfBoundsException, UnknownPropertyException, PropertyVetoException, WrappedTargetException, IllegalArgumentException {


        XCellRange range = sheet.getCellRangeByName("B" + ++y + ":" + "I" + y);
        xPropSet = UnoRuntime.queryInterface(XPropertySet.class, range);

        xPropSet.setPropertyValue("TableBorder", aBorder);
        rCell = range.getCellByPosition(0, 0);
        rCell.setFormula(type);


        x = 1;
        y++;

        for (int i = 0; i < planList.size(); i++) {
            if (planList.get(i).getType().getId() == type_id) {

                //  ArrayList<PlanItem> planListType = new ArrayList<>();
                // pl

                PlanItem plan = new PlanItem();
                plan = planList.get(i);

                rCell = sheet.getCellByPosition(x, y);
                xPropSet = UnoRuntime.queryInterface(XPropertySet.class, rCell);
                xPropSet.setPropertyValue("TableBorder", aBorder);
                rCell.setFormula(plan.getAssortmentName());
                x++;

                rCell = sheet.getCellByPosition(x, y);
                xPropSet = UnoRuntime.queryInterface(XPropertySet.class, rCell);
                xPropSet.setPropertyValue("TableBorder", aBorder);
                rCell.setFormula(plan.getModel() + "");

                x++;

                rCell = sheet.getCellByPosition(x, y);
                xPropSet = UnoRuntime.queryInterface(XPropertySet.class, rCell);
                xPropSet.setPropertyValue("TableBorder", aBorder);
                rCell.setFormula("");
                x++;

                rCell = sheet.getCellByPosition(x, y);
                xPropSet = UnoRuntime.queryInterface(XPropertySet.class, rCell);
                xPropSet.setPropertyValue("TableBorder", aBorder);
                rCell.setFormula("");
                x++;

                rCell = sheet.getCellByPosition(x, y);
                xPropSet = UnoRuntime.queryInterface(XPropertySet.class, rCell);
                xPropSet.setPropertyValue("TableBorder", aBorder);
                rCell.setFormula("");
                x++;

                rCell = sheet.getCellByPosition(x, y);
                xPropSet = UnoRuntime.queryInterface(XPropertySet.class, rCell);
                xPropSet.setPropertyValue("TableBorder", aBorder);
                rCell.setFormula(plan.getNote());
                x++;

                rCell = sheet.getCellByPosition(x, y);
                rCell.setFormula("");
                xPropSet = UnoRuntime.queryInterface(XPropertySet.class, rCell);
                xPropSet.setPropertyValue("TableBorder", aBorder);

                if (plan.getImage() != null) {
                    //System.out.println(plan.getImage().replace('\\', '/').replace("$", "").replaceAll("//file-server.local/Public" , "/nfs/Public"));
                    //
                    File fC = new File("c:\\windows\\explorer.exe");
                    if (!fC.exists()) {
                        rCell.setFormula(plan.getImage().replace('\\', '/').replace("$", "").replaceAll("//file-server.local/Public", "/nfs/Public"));
                    } else {
                        //System.out.println(plan.getImage().replace('\\', '/').replaceAll("/nfs/Public/catalog" , "888"));
                        rCell.setFormula(plan.getImage().replace('\\', '/').replaceAll("/nfs/Public/catalog", "//file-server.local/Public'\\$\\'/catalog").replace("'", ""));
                        // rCell.setFormula(plan.getImage().replace('\\', '/'));
                    }

                } else {
                    rCell.setFormula("нет");
                }

                x++;

                rCell = sheet.getCellByPosition(x, y);
                xPropSet = UnoRuntime.queryInterface(XPropertySet.class, rCell);
                xPropSet.setPropertyValue("TableBorder", aBorder);
                //  xPropSet.setPropertyValue("GraphicURL" , "D:\\2.jpg");
                rCell.setFormula("");
                x++;


                x = 1;
                y++;
            }

        }


        int[] xy = new int[2];
        xy[0] = x;
        xy[1] = y;

        return xy;
    }


    @Override
    public void useToken() {

    }
}


