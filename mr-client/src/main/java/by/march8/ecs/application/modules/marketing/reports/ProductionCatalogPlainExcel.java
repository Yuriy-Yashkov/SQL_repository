package by.march8.ecs.application.modules.marketing.reports;

import by.gomel.freedev.ucframework.uccore.report.enums.FontStyle;
import by.gomel.freedev.ucframework.uccore.report.model.ReportProperties;
import by.march8.ecs.application.modules.filemanager.model.ImageItem;
import by.march8.ecs.application.modules.marketing.model.ProductCatalogArticleReport;
import by.march8.ecs.application.modules.marketing.model.ProductCatalogReport;
import by.march8.ecs.application.modules.marketing.model.ProductionCatalogCategoryReport;
import by.march8.ecs.application.modules.references.classifier.model.ItemNameReplacer;
import by.march8.ecs.services.images.ColorImageService;
import by.march8.ecs.services.images.ModelImageServiceDB;
import com.sun.star.container.XNamed;
import com.sun.star.lang.XComponent;
import com.sun.star.sheet.XSpreadsheet;
import com.sun.star.sheet.XSpreadsheetDocument;
import com.sun.star.sheet.XSpreadsheets;
import com.sun.star.table.XCell;
import com.sun.star.table.XCellRange;
import com.sun.star.uno.UnoRuntime;

public class ProductionCatalogPlainExcel extends AbstractCatalogReport {
    private ProductCatalogReport reportData;

    private ColorImageService sImage;
    private String savePath;

    public ProductionCatalogPlainExcel(ProductCatalogReport report, String path) {
        reportData = report;
        savePath = path;
        sImage = ModelImageServiceDB.getInstance();
        create();
    }


    @Override
    ReportProperties prepareProperties(ReportProperties properties) {
        // Устанавливаем имя файла шаблона
        properties.setBlankName("catalog_" + reportData.getDocument().getDocumentNumber().replace("/", "-"));
        properties.getTemplate().setTemplateName("catalogPlainTable.ots");
        properties.setStorable(true);
        properties.setSavePath(savePath);
        return properties;
    }

    @Override
    boolean populateData(XComponent component) {

        try {
            XSpreadsheetDocument xSpreadsheetDocument = UnoRuntime
                    .queryInterface(XSpreadsheetDocument.class, component);
            XSpreadsheets xSpreadsheets = xSpreadsheetDocument.getSheets();
            Object sheet = xSpreadsheets.getByName("Лист1");
            XSpreadsheet xSpreadsheet = UnoRuntime
                    .queryInterface(XSpreadsheet.class, sheet);

            XNamed xNamed = UnoRuntime.queryInterface(XNamed.class,
                    xSpreadsheet);

            xNamed.setName("Каталог продукции");

            XCell xCell;
            int row = 4;
            int startRow = row;
            int index = 1;

            xCell = xSpreadsheet.getCellByPosition(0, 1);
            xCell.setFormula(reportData.getDocument().getDocumentInformation() + "(" + reportData.getPreset().getCurrencyType().getName() + ")");

            for (ProductionCatalogCategoryReport category : reportData.getCategoryList()) {
                mergeCellRange(xSpreadsheet, 0, row, 8, row);
                xCell = xSpreadsheet.getCellByPosition(0, row);
                xCell.setFormula(category.getCategoryAsString().replace("_", ", "));
                setCharHeightForCell(xCell, 12, FontStyle.Bold);
                row++;
                for (ProductCatalogArticleReport article : category.getData()) {
                    xCell = xSpreadsheet.getCellByPosition(0, row);
                    xCell.setFormula(String.valueOf(index));

                    xCell = xSpreadsheet.getCellByPosition(1, row);
                    xCell.setFormula(ItemNameReplacer.transform(article.getItemName()));

                    xCell = xSpreadsheet.getCellByPosition(2, row);
                    xCell.setFormula(String.valueOf(article.getModelNumber()));

                    xCell = xSpreadsheet.getCellByPosition(3, row);
                    xCell.setFormula(String.valueOf(article.getArticleNumber()));

                    xCell = xSpreadsheet.getCellByPosition(4, row);
                    if (article.getMaterialComposition() != null) {
                        xCell.setFormula(article.getMaterialComposition().replace(",", "\n").trim());
                    }

                    xCell = xSpreadsheet.getCellByPosition(5, row);
                    xCell.setFormula(article.getSizeRange().replace("_", "\n").trim());

                    xCell = xSpreadsheet.getCellByPosition(6, row);
                    xCell.setFormula(article.getPriceRange().replace("_", "\n").trim());

                    xCell = xSpreadsheet.getCellByPosition(7, row);
                    if (article.getColorsArticle() != null) {
                        xCell.setFormula(article.getColorsArticle().replace(",", "\n").trim());
                    }

                    xCell = xSpreadsheet.getCellByPosition(8, row);

                    ImageItem defaultImage = sImage.getImageByModelNumberAndImageName(String.valueOf(article.getModelNumber()),
                            article.getImage());
                    if (defaultImage != null) {

                        XCellRange xCellRange = xSpreadsheet
                                .getCellRangeByPosition(13, row, 13,
                                        row);

                        insertImageToDocAndResize(
                                xSpreadsheetDocument,
                                xSpreadsheet,
                                xCell,
                                String.valueOf(article.getModelNumber()),
                                String.valueOf(defaultImage.getPreviewImageFile()),
                                xCellRange, 1400);
                    }

                    xCell = xSpreadsheet.getCellByPosition(9, row);
                    xCell.setFormula(article.getBrandName());

                    xCell = xSpreadsheet.getCellByPosition(10, row);
                    xCell.setFormula(String.valueOf(article.getAmount()));

/*
                    // ОБРАБОТКА КАРТИНОК
                    ImageItem defaultImage = imageService.getDefaultImageByModelNumber(String.valueOf(article.getModelNumber()));
                    if (defaultImage != null) {
//                        String fileName = copyImage(article.getModelNumber() + "_DEFAULT", defaultImage.getImageFile(), pathToSave);
                    }
*/
                    index++;
                    row++;
                }
            }

            setFullBorderForCellRange(xSpreadsheet, 0, 10, startRow, row - 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
