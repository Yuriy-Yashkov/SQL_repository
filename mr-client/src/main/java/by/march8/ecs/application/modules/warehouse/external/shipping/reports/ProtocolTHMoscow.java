package by.march8.ecs.application.modules.warehouse.external.shipping.reports;

import by.gomel.freedev.ucframework.uccore.report.api.openoffice.DocumentType;
import by.gomel.freedev.ucframework.uccore.report.model.ReportProperties;
import by.march8.ecs.application.modules.references.classifier.dao.ClassifierJDBC;
import by.march8.ecs.application.modules.references.classifier.model.ItemNameReplacer;
import by.march8.ecs.application.modules.references.classifier.ui.ClassifierTree;
import by.march8.ecs.application.modules.warehouse.external.shipping.model.SaleDocumentReport;
import by.march8.entities.classifier.ClassifierArticleComposition;
import by.march8.entities.warehouse.SaleDocumentDetailItemReport;
import com.sun.star.container.XNamed;
import com.sun.star.lang.XComponent;
import com.sun.star.sheet.XSpreadsheet;
import com.sun.star.sheet.XSpreadsheetDocument;
import com.sun.star.sheet.XSpreadsheets;
import com.sun.star.table.XCell;
import com.sun.star.uno.UnoRuntime;

/**
 * @author Andy 17.06.2016.
 */
public class ProtocolTHMoscow extends AbstractInvoiceReport {

    private ClassifierJDBC db;

    public ProtocolTHMoscow(final SaleDocumentReport report) {
        super(report);
        documentReport = report;
        create();
    }

    @Override
    ReportProperties prepareProperties(final ReportProperties properties) {
        // Устанавливаем имя файла шаблона
        properties.setBlankName("protocol_" + saleDocumentBase.getDocumentNumber());
        properties.getTemplate().setTemplateName("protocol_trade_house_moscow.ots");
        properties.getTemplate().setDocumentType(DocumentType.DOCUMENT_XLS);
        //properties.setName(ReportEnum.XLSM_REPORT);
        return properties;
    }


    @Override
    boolean populateData(final XComponent component) {
        try {
            XSpreadsheetDocument xSpreadsheetDocument = UnoRuntime
                    .queryInterface(XSpreadsheetDocument.class, component);
            XSpreadsheets xSpreadsheets = xSpreadsheetDocument.getSheets();
            Object sheet = xSpreadsheets.getByName("Ассортимент");
            XSpreadsheet xSpreadsheet = UnoRuntime
                    .queryInterface(XSpreadsheet.class, sheet);

            XNamed xNamed = UnoRuntime.queryInterface(XNamed.class,
                    xSpreadsheet);

            //xNamed.setName("Спецификация к ТТН№ " + saleDocumentBase.getDocumentNumber());

            String COMPANY_NAME = "ОАО \"8 Марта\"";
            String REPUBLIC = "Беларусь";

            //String contract = getDataAsString("CONTRACT_NUMBER") + " от " + getDataAsString("CONTRACT_DATE_BEGIN") + " г.";
            XCell xCell = xSpreadsheet.getCellByPosition(17, 1);
            /*xCell.setFormula(contract);

            xCell = xSpreadsheet.getCellByPosition(7, 3);
            xCell.setFormula("между " + getDataAsString("RECIPIENT_NAME") + " и " + getDataAsString("SENDER_NAME"));

            xCell = xSpreadsheet.getCellByPosition(2, 3);
            xCell.setFormula("к договору №" + contract);*/

            int row = 4;

            for (SaleDocumentDetailItemReport item : detailList) {

                xCell = xSpreadsheet.getCellByPosition(2, row);
                xCell.setFormula(ItemNameReplacer.transform(item.getItemName()));


                try {
                    int code = Integer.valueOf(item.getArticleCode().substring(0, 2));
                    int code_ = Integer.valueOf(item.getArticleCode().substring(0, 3));
                    xCell = xSpreadsheet.getCellByPosition(3, row);
                    xCell.setFormula(ClassifierTree.getNameAssortmentByArticleSegment(code) + " - " +
                            ClassifierTree.getNameAssortmentByArticleSegment(code_));
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

                xCell = xSpreadsheet.getCellByPosition(4, row);
                xCell.setFormula(COMPANY_NAME);

                xCell = xSpreadsheet.getCellByPosition(5, row);
                xCell.setFormula("");

                xCell = xSpreadsheet.getCellByPosition(6, row);
                xCell.setFormula(COMPANY_NAME);

                xCell = xSpreadsheet.getCellByPosition(7, row);
                xCell.setFormula(REPUBLIC);


                xCell = xSpreadsheet.getCellByPosition(8, row);
                xCell.setFormula("'" + item.getEanCode());

                xCell = xSpreadsheet.getCellByPosition(9, row);
                xCell.setFormula(item.getArticleName());

                xCell = xSpreadsheet.getCellByPosition(10, row);
                //xCell.setFormula(item.getProductNameSizeStringSingleLine());
                xCell.setFormula(item.getProductNameSizeStringSingleLine() + "\n" + getMaterialComposition(item.getModelNumber()).getAllCompositions().replace("_", ", "));


                xCell = xSpreadsheet.getCellByPosition(20, row);
                xCell.setValue(item.getValuePriceCurrency());

                row++;

            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    private ClassifierArticleComposition getMaterialComposition(String modelNumber) {
        if (db == null) {
            db = new ClassifierJDBC();
        }

        return db.getMaterialCompositionByModelNumber(Integer.valueOf(modelNumber));
    }
}
