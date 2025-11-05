package by.march8.ecs.application.modules.warehouse.external.shipping.reports;

import by.gomel.freedev.ucframework.uccore.report.api.openoffice.DocumentType;
import by.gomel.freedev.ucframework.uccore.report.api.openoffice.OpenOfficeConnector;
import by.gomel.freedev.ucframework.uccore.report.model.ReportTemplate;
import by.march8.ecs.application.modules.warehouse.external.shipping.mode.dao.InventoryDB;
import by.march8.ecs.application.modules.warehouse.external.shipping.mode.model.InventoryReportBean;
import by.march8.ecs.application.modules.warehouse.external.shipping.model.SaleDocumentView;
import com.sun.star.beans.PropertyVetoException;
import com.sun.star.beans.UnknownPropertyException;
import com.sun.star.lang.IllegalArgumentException;
import com.sun.star.lang.IndexOutOfBoundsException;
import com.sun.star.lang.WrappedTargetException;
import com.sun.star.lang.XComponent;
import com.sun.star.sheet.XSpreadsheet;
import com.sun.star.sheet.XSpreadsheetDocument;
import com.sun.star.sheet.XSpreadsheets;
import com.sun.star.table.XCell;
import com.sun.star.uno.UnoRuntime;

import javax.swing.*;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author tmp on 16.11.2021 10:36
 */
public class InventoryReport {


    public void createInventoryDocument(SaleDocumentView selectedItem) {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        String s = JOptionPane.showInputDialog(
                frame,
                "Введите номер накладной");
        System.out.println(s);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        int nadb = Integer.valueOf(JOptionPane.showInputDialog(
                frame,
                "Введите величину торговой надбавки"));
        System.out.println(nadb);
        InventoryDB db = new InventoryDB();
        List<InventoryReportBean> list = new ArrayList<>();
        list = db.getInventoryReportBeanList(s);
        list.forEach(System.out::println);

        ReportTemplate template = new ReportTemplate();
        template.setTemplateName("inventOpis.ots");
        template.setDocumentType(DocumentType.DOCUMENT_ODS);
        template.setSaveName("111");
        template.setSavePath("c:\\");
        template.setVisible(true);
        int count = 0;

        try {
            OpenOfficeConnector connector = new OpenOfficeConnector(template);

            XComponent xComponent = connector.getXComponent();
            XSpreadsheetDocument xSpreadsheetDocument = UnoRuntime
                    .queryInterface(XSpreadsheetDocument.class, xComponent);
            XSpreadsheets xSpreadsheets = xSpreadsheetDocument.getSheets();
            Object sheet = null;
            com.sun.star.beans.XPropertySet xPropSet = null;

            com.sun.star.table.BorderLine aLine = new com.sun.star.table.BorderLine();
            aLine.Color = 0x000000;
            aLine.InnerLineWidth = aLine.LineDistance = 0;
            aLine.OuterLineWidth = 30;
            com.sun.star.table.TableBorder aBorder = new com.sun.star.table.TableBorder();
            aBorder.TopLine = aBorder.BottomLine = aBorder.LeftLine = aBorder.RightLine = aLine;
            aBorder.IsTopLineValid = aBorder.IsBottomLineValid = true;
            aBorder.IsLeftLineValid = aBorder.IsRightLineValid = true;
            int kol;
            int sum;

            try {
                sheet = xSpreadsheets.getByName("Лист1");
                // и столбы и строки начинаются с нуля!!!!
                XSpreadsheet xSpreadsheet = UnoRuntime
                        .queryInterface(XSpreadsheet.class, sheet);
                XCell xCell = null;
                int j = 0;
                int headCell = 53;
                int tinCell = 84;
                int kolCount = 0;
                double sumCount = 0;
                int listCount = list.size();
                if (listCount < 31) {
                    listCount = 1;
                } else if (listCount % 30 != 0) {
                    listCount = listCount / 30 + 1;
                } else {
                    listCount = listCount / 30;
                }
                System.out.println(listCount);
                for (int g = 0; g <= listCount; g++) {
                    xCell = xSpreadsheet.getCellByPosition(0, headCell); // столбы потом строки
                    xCell.setFormula("№ п/п");
                    xPropSet = UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
                    xPropSet.setPropertyValue("TableBorder", aBorder);
                    xCell = xSpreadsheet.getCellByPosition(1, headCell);
                    xCell.setFormula("Штрихкод");
                    xPropSet = UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
                    xPropSet.setPropertyValue("TableBorder", aBorder);
                    xCell = xSpreadsheet.getCellByPosition(2, headCell);
                    xCell.setFormula("Артикул");
                    xPropSet = UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
                    xPropSet.setPropertyValue("TableBorder", aBorder);
                    xCell = xSpreadsheet.getCellByPosition(3, headCell);
                    xCell.setFormula("Наименование товара");
                    xPropSet = UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
                    xPropSet.setPropertyValue("TableBorder", aBorder);
                    xCell = xSpreadsheet.getCellByPosition(4, headCell);
                    xCell.setFormula("Сорт");
                    xPropSet = UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
                    xPropSet.setPropertyValue("TableBorder", aBorder);
                    xCell = xSpreadsheet.getCellByPosition(5, headCell);
                    xCell.setFormula("Размер");
                    xPropSet = UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
                    xPropSet.setPropertyValue("TableBorder", aBorder);
                    xCell = xSpreadsheet.getCellByPosition(6, headCell);
                    xCell.setFormula("Цена");
                    xPropSet = UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
                    xPropSet.setPropertyValue("TableBorder", aBorder);
                    xCell = xSpreadsheet.getCellByPosition(7, headCell);
                    xCell.setFormula("Кол-во");
                    xPropSet = UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
                    xPropSet.setPropertyValue("TableBorder", aBorder);
                    xCell = xSpreadsheet.getCellByPosition(8, headCell);
                    xCell.setFormula("Сумма, руб");
                    xPropSet = UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
                    xPropSet.setPropertyValue("TableBorder", aBorder);
                    int h = 0;
                    for (int i = headCell + 1; i <= tinCell + 1; i++) {
                        if (j == list.size()) {
                            break;
                        }
                        System.out.println(list.get(j).getNds());
                        xCell = xSpreadsheet.getCellByPosition(0, i); // столбы потом строки
                        xCell.setValue(list.get(j).getId());
                        xPropSet = UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
                        xPropSet.setPropertyValue("TableBorder", aBorder);
                        xCell = xSpreadsheet.getCellByPosition(1, i);
                        xCell.setFormula(list.get(j).getEanCode());
                        xPropSet = UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
                        xPropSet.setPropertyValue("TableBorder", aBorder);
                        xCell = xSpreadsheet.getCellByPosition(2, i);
                        xCell.setFormula(list.get(j).getNar().trim());
                        xPropSet = UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
                        xPropSet.setPropertyValue("TableBorder", aBorder);
                        xCell = xSpreadsheet.getCellByPosition(3, i);
                        xCell.setFormula(list.get(j).getNgpr().trim());
                        xPropSet = UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
                        xPropSet.setPropertyValue("TableBorder", aBorder);
                        xCell = xSpreadsheet.getCellByPosition(4, i);
                        xCell.setFormula(list.get(j).getSrt() + " сорт");
                        xPropSet = UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
                        xPropSet.setPropertyValue("TableBorder", aBorder);
                        xCell = xSpreadsheet.getCellByPosition(5, i);
                        xCell.setFormula(list.get(j).getRzmPrint().trim());
                        xPropSet = UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
                        xPropSet.setPropertyValue("TableBorder", aBorder);
                        xCell = xSpreadsheet.getCellByPosition(6, i);
                        xCell.setValue(list.get(j).getCena(nadb));
                        xPropSet = UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
                        xPropSet.setPropertyValue("TableBorder", aBorder);
                        xCell = xSpreadsheet.getCellByPosition(7, i);
                        xCell.setValue(list.get(j).getKol());
                        xPropSet = UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
                        xPropSet.setPropertyValue("TableBorder", aBorder);
                        kolCount = kolCount + list.get(j).getKol();
                        xCell = xSpreadsheet.getCellByPosition(8, i);
                        xCell.setValue(list.get(j).getSum(nadb));
                        xPropSet = UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
                        xPropSet.setPropertyValue("TableBorder", aBorder);
                        sumCount = sumCount + list.get(j).getSum(nadb);
                        j++;
                        h++;
                        count = h;
                    }
                    xCell = xSpreadsheet.getCellByPosition(5, tinCell + 2);
                    xCell.setFormula("Итого на странице:");
                    xCell = xSpreadsheet.getCellByPosition(7, tinCell + 2);
                    xCell.setFormula("=SUM(H" + (headCell + 1) + ":H" + (tinCell + 2) + ")");
                    xCell = xSpreadsheet.getCellByPosition(8, tinCell + 2);
                    xCell.setFormula("=SUM(I" + (headCell + 1) + ":I" + (tinCell + 2) + ")");
                    xCell = xSpreadsheet.getCellByPosition(0, tinCell + 3);
                    xCell.setFormula("Число порядковых номеров, помещенных на данной странице______________________________________");
                    xCell = xSpreadsheet.getCellByPosition(0, tinCell + 4);
                    xCell.setFormula("Общий итог количества в натуральных показателях всех ценностей, записанных на данной странице,______");
                    xCell = xSpreadsheet.getCellByPosition(0, tinCell + 5);
                    xCell.setFormula("________________________________________________________________________________________");
                    xCell = xSpreadsheet.getCellByPosition(0, tinCell + 6);
                    xCell.setFormula("Председатель комиссии____________________________");
                    xCell = xSpreadsheet.getCellByPosition(0, tinCell + 7);
                    xCell.setFormula("Члены Комиссии__________________________________________________________________________");
                    xCell = xSpreadsheet.getCellByPosition(0, tinCell + 8);
                    xCell.setFormula("Материально ответственное лицо(лица)________________________________________________________");

                    headCell = headCell + 40;
                    tinCell = tinCell + 40;
                    if (j == list.size()) {
                        break;
                    }
                }
                headCell = headCell - 40;
                tinCell = tinCell - 40;
                tinCell = tinCell - (31 - count);
                xCell = xSpreadsheet.getCellByPosition(5, tinCell + 1);
                xCell.setFormula("Итого на странице:");
                xCell = xSpreadsheet.getCellByPosition(7, tinCell + 1);
                xCell.setFormula("=SUM(H" + (headCell + 1) + ":H" + (tinCell + 1) + ")");
                xCell = xSpreadsheet.getCellByPosition(8, tinCell + 1);
                xCell.setFormula("=SUM(I" + (headCell + 1) + ":I" + (tinCell + 1) + ")");
                xCell = xSpreadsheet.getCellByPosition(5, tinCell + 2);
                xCell.setFormula("Всего по описи:");
                xCell = xSpreadsheet.getCellByPosition(7, tinCell + 2);
                xCell.setValue(kolCount);
                xCell = xSpreadsheet.getCellByPosition(8, tinCell + 2);
                xCell.setValue(sumCount);
                xCell = xSpreadsheet.getCellByPosition(0, tinCell + 2);
                xCell.setFormula(" ");
                xCell = xSpreadsheet.getCellByPosition(0, tinCell + 3);
                xCell.setFormula("Всего по описи, руб. _____________________________________________________________________ ");
                xCell = xSpreadsheet.getCellByPosition(0, tinCell + 4);
                xCell.setFormula("Число порядковых номеров, помещенных на данной странице______________________________________");
                xCell = xSpreadsheet.getCellByPosition(0, tinCell + 5);
                xCell.setFormula("Число порядковых номеров, всего по описи ______________________________________ ");
                xCell = xSpreadsheet.getCellByPosition(0, tinCell + 6);
                xCell.setFormula("Общий итог количества в натуральных показателях всех ценностей, записанных на данной странице,______ ");
                xCell = xSpreadsheet.getCellByPosition(0, tinCell + 7);
                xCell.setFormula("________________________________________________________________________________________ ");
                xCell = xSpreadsheet.getCellByPosition(0, tinCell + 8);
                xCell.setFormula("Общий итог количества в натуральных показателях всех ценностей, всего по описи,______ ");
                xCell = xSpreadsheet.getCellByPosition(0, tinCell + 9);
                xCell.setFormula("________________________________________________________________________________________ ");
                xCell = xSpreadsheet.getCellByPosition(0, tinCell + 10);
                xCell.setFormula("Председатель комиссии____________________________");
                xCell = xSpreadsheet.getCellByPosition(0, tinCell + 11);
                xCell.setFormula("Члены комиссии:");
                xCell = xSpreadsheet.getCellByPosition(0, tinCell + 12);
                xCell.setFormula("_______________________    _____________________    ________________________");
                xCell = xSpreadsheet.getCellByPosition(0, tinCell + 13);
                xCell.setFormula("_______________________    _____________________    ________________________");
                xCell = xSpreadsheet.getCellByPosition(0, tinCell + 14);
                xCell.setFormula("_______________________    _____________________    ________________________");
                xCell = xSpreadsheet.getCellByPosition(0, tinCell + 15);
                xCell.setFormula("_______________________    _____________________    ________________________");
                xCell = xSpreadsheet.getCellByPosition(0, tinCell + 16);
                xCell.setFormula("_______________________    _____________________    ________________________");
                xCell = xSpreadsheet.getCellByPosition(0, tinCell + 17);
                xCell.setFormula("При проверке фактического наличия оказалось:");
                xCell = xSpreadsheet.getCellByPosition(0, tinCell + 18);
                xCell.setFormula("Наличными деньгами______________________________________________________________");
                xCell = xSpreadsheet.getCellByPosition(0, tinCell + 19);
                xCell.setFormula("Товаров на _________________________________________________ руб. _____________________ коп.");
                xCell = xSpreadsheet.getCellByPosition(0, tinCell + 20);
                xCell.setFormula("Тары на ____________________________________________________ руб. _____________________ коп.");
                xCell = xSpreadsheet.getCellByPosition(0, tinCell + 21);
                xCell.setFormula("Все ценности, поименованные в настоящей инвентаризационной описи с №______ по №______, комиссией проверены");
                xCell = xSpreadsheet.getCellByPosition(0, tinCell + 22);
                xCell.setFormula(" в натуре в моем(нашем) присутствии и внесены в опись, в связи с чем претензий к инвентаризационной комиссии ");
                xCell = xSpreadsheet.getCellByPosition(0, tinCell + 23);
                xCell.setFormula("не имею(не имеем). Ценности,перечисленные в описи, находятся на моем(нашем) ответственном хранении.");
                xCell = xSpreadsheet.getCellByPosition(0, tinCell + 24);
                xCell.setFormula("Материально ответственное лицо(лица)___________________________________________________________");
                xCell = xSpreadsheet.getCellByPosition(5, tinCell + 25);
                xCell.setFormula("");
                xCell = xSpreadsheet.getCellByPosition(7, tinCell + 25);
                xCell.setFormula("");
                xCell = xSpreadsheet.getCellByPosition(8, tinCell + 25);
                xCell.setFormula("");
                xCell = xSpreadsheet.getCellByPosition(0, tinCell + 25);
                xCell.setFormula("\"________\"___________________ 20___г.");
                xCell = xSpreadsheet.getCellByPosition(0, tinCell + 26);
                xCell.setFormula("Цену и таксировку проверил ______________________    ______________________");
                xCell = xSpreadsheet.getCellByPosition(0, tinCell + 27);
                xCell.setFormula("                                                             должность                                  подпись");
                xCell = xSpreadsheet.getCellByPosition(0, tinCell + 28);
                xCell.setFormula("\"________\"___________________ 20___г.");
                xCell = xSpreadsheet.getCellByPosition(0, tinCell + 29);
                xCell.setFormula("");
                xCell = xSpreadsheet.getCellByPosition(0, tinCell + 30);
                xCell.setFormula("");
                xCell = xSpreadsheet.getCellByPosition(0, tinCell + 31);
                xCell.setFormula("");
                for (int i = tinCell + 3; i <= tinCell + 52; i++) {
                    xCell = xSpreadsheet.getCellByPosition(5, i);
                    xCell.setFormula("");
                    xCell = xSpreadsheet.getCellByPosition(7, i);
                    xCell.setFormula("");
                    xCell = xSpreadsheet.getCellByPosition(8, i);
                    xCell.setFormula("");
                }

            } catch (WrappedTargetException e) {
                e.printStackTrace();
            } catch (IndexOutOfBoundsException e) {
                e.printStackTrace();
            } catch (com.sun.star.container.NoSuchElementException e) {
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (PropertyVetoException e) {
                e.printStackTrace();
            } catch (UnknownPropertyException e) {
                e.printStackTrace();
            }

            String path = connector.saveDocument(template, DocumentType.DOCUMENT_ODS);
            connector.openDocument(path);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}
