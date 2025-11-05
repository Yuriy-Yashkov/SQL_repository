package by.march8.ecs.application.modules.warehouse.external.shipping.reports;

import by.gomel.freedev.ucframework.uccore.report.model.ReportProperties;
import by.march8.ecs.application.modules.filemanager.model.ImageItem;
import by.march8.ecs.application.modules.references.classifier.model.ItemNameReplacer;
import by.march8.ecs.application.modules.references.classifier.ui.ClassifierTree;
import by.march8.ecs.application.modules.warehouse.external.shipping.manager.SaleDocumentDataProvider;
import by.march8.ecs.application.modules.warehouse.external.shipping.model.SaleDocumentReport;
import by.march8.ecs.services.images.ColorImageService;
import by.march8.ecs.services.images.ModelImageServiceDB;
import by.march8.entities.warehouse.SaleDocumentDetailItemReport;
import com.sun.star.container.XNamed;
import com.sun.star.lang.XComponent;
import com.sun.star.sheet.XSpreadsheet;
import com.sun.star.sheet.XSpreadsheetDocument;
import com.sun.star.sheet.XSpreadsheets;
import com.sun.star.table.XCell;
import com.sun.star.uno.UnoRuntime;
import org.apache.commons.io.FileUtils;
import org.zeroturnaround.zip.ZipUtil;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class InternetMarketExportReport extends AbstractInvoiceReport {
    private String savePath;
    private ColorImageService imageService_;

    public InternetMarketExportReport(SaleDocumentReport report, String s) {
        super(report);
        savePath = s;
        documentReport = report;
        imageService_ = ModelImageServiceDB.getInstance();

        // Иногда данный документ формируется до основных расчетов на складе
        // Поэтому ЕАН может отсутствовать в базе, так что принудительно дернем
        SaleDocumentDataProvider provider = new SaleDocumentDataProvider();
        provider.updateEanCodeAndColor(report);

        create();
    }

    @Override
    ReportProperties prepareProperties(final ReportProperties properties) {
        // Устанавливаем имя файла шаблона
        properties.setBlankName("export_information_" + saleDocumentBase.getDocumentNumber());
        properties.getTemplate().setTemplateName("internetMarktTemplate.ots");
        properties.setStorable(true);
        properties.setSavePath(savePath);
        return properties;
    }

    @Override
    boolean populateData(final XComponent component) {
        String documentNumber = saleDocumentBase.getDocumentNumber();
        String imageDir = savePath + documentNumber + "_IMAGES";
        // Создадим каталог НОМЕР ДОКУМЕНТА_IMAGES
        createDirectory(imageDir);
        purgeDirectory(new File(imageDir));

        try {
            XSpreadsheetDocument xSpreadsheetDocument = UnoRuntime
                    .queryInterface(XSpreadsheetDocument.class, component);
            XSpreadsheets xSpreadsheets = xSpreadsheetDocument.getSheets();
            Object sheet = xSpreadsheets.getByName("Лист1");
            XSpreadsheet xSpreadsheet = UnoRuntime
                    .queryInterface(XSpreadsheet.class, sheet);

            XNamed xNamed = UnoRuntime.queryInterface(XNamed.class,
                    xSpreadsheet);

            String articleCode = "";
            int lineStart;

            xNamed.setName("Экспорт данных ТТН №" + documentNumber);

            // ----------------Заполнение шапки документа
            XCell xCell = xSpreadsheet.getCellByPosition(0, 0);

            xCell = xSpreadsheet.getCellByPosition(0, 2);
            xCell.setFormula(SaleDocumentReport.getContractDescription(documentReport));
            setCharHeightForCell(xCell, CHAR_SIZE);

            // ----------------Заполнение тела документа
            int row = 2;
            lineStart = 2;
            HashMap<String, String> imageMap = new HashMap<>();

            for (SaleDocumentDetailItemReport item : detailList) {

                List<ImageItem> images = imageService_.getColorImageListByModel(item.getModelNumber());
                if (images != null) {
                    for (ImageItem item_ : images) {
                        String fileName = imageMap.get(item_.getImageFile());
                        if (fileName == null) {
                            fileName = copyImage(item.getModelNumber() + "_" + item_.getColor(), item_.getImageFile(), imageDir + File.separator);
                            imageMap.put(item_.getImageFile(), fileName);
                        }
                    }
                }

                xCell = xSpreadsheet.getCellByPosition(0, row);
                xCell.setFormula("");

                xCell = xSpreadsheet.getCellByPosition(1, row);
                xCell.setFormula("EAN13");

                xCell = xSpreadsheet.getCellByPosition(2, row);
                xCell.setFormula(item.getEanCode());

                xCell = xSpreadsheet.getCellByPosition(3, row);
                xCell.setFormula(ItemNameReplacer.transform(item.getItemName()));

                xCell = xSpreadsheet.getCellByPosition(4, row);
                xCell.setFormula(ClassifierTree.getNameAssortmentByArticleSegment(Integer.valueOf(item.getArticleCode().substring(0, 3))));

                xCell = xSpreadsheet.getCellByPosition(5, row);
                xCell.setFormula(ClassifierTree.getNameAssortmentByArticleSegment(Integer.valueOf(item.getArticleCode().substring(0, 2))));

                xCell = xSpreadsheet.getCellByPosition(6, row);
                String[] nm = ItemNameReplacer.transform(item.getItemName()).split(" ");
                if (nm.length > 0) {
                    xCell.setFormula(nm[0]);
                } else {
                    xCell.setFormula("");
                }

                xCell = xSpreadsheet.getCellByPosition(7, row);
                xCell.setFormula("");

                xCell = xSpreadsheet.getCellByPosition(8, row);
                xCell.setFormula("");

                xCell = xSpreadsheet.getCellByPosition(9, row);
                xCell.setFormula("");

                xCell = xSpreadsheet.getCellByPosition(10, row);
                xCell.setFormula(item.getArticleNumber() + " м." + item.getModelNumber());

                xCell = xSpreadsheet.getCellByPosition(11, row);
                xCell.setFormula("РБ");

                xCell = xSpreadsheet.getCellByPosition(12, row);
                xCell.setFormula("ОАО \"8 Марта\"");

                xCell = xSpreadsheet.getCellByPosition(13, row);
                xCell.setFormula("");

                xCell = xSpreadsheet.getCellByPosition(14, row);
                xCell.setFormula("");
                // Состав
                xCell = xSpreadsheet.getCellByPosition(15, row);
                xCell.setFormula(item.getLabelComposition());
                // Уход
                xCell = xSpreadsheet.getCellByPosition(16, row);
                xCell.setFormula("");
                // Состав 1
                xCell = xSpreadsheet.getCellByPosition(17, row);
                xCell.setFormula(item.getLabelComposition());
                // Состав 2
                xCell = xSpreadsheet.getCellByPosition(18, row);
                xCell.setFormula("Без подкладки");

                xCell = xSpreadsheet.getCellByPosition(19, row);
                xCell.setFormula("");
                xCell = xSpreadsheet.getCellByPosition(20, row);
                xCell.setFormula("");
                //Размер
                xCell = xSpreadsheet.getCellByPosition(21, row);
                xCell.setValue(item.getItemSize());
                //Рост
                xCell = xSpreadsheet.getCellByPosition(22, row);
                xCell.setValue(getMinGrowth(item.getItemSizePrint(), item.getItemGrowz()));

                xCell = xSpreadsheet.getCellByPosition(23, row);
                xCell.setFormula("");

                xCell = xSpreadsheet.getCellByPosition(24, row);
                xCell.setFormula("");
                // Цвет
                xCell = xSpreadsheet.getCellByPosition(25, row);
                xCell.setFormula(item.getItemColorNew());

                xCell = xSpreadsheet.getCellByPosition(26, row);
                xCell.setFormula(item.getItemColorNew());

                xCell = xSpreadsheet.getCellByPosition(27, row);
                xCell.setFormula("Всесезон");

                // ПРинадлежность полу, эксмпериментально
                xCell = xSpreadsheet.getCellByPosition(28, row);
                xCell.setFormula(item.getBelongAgeCategory());

                // ПРинадлежность полу, эксмпериментально
                xCell = xSpreadsheet.getCellByPosition(29, row);
                xCell.setFormula(item.getYearsOld());

                xCell = xSpreadsheet.getCellByPosition(30, row);
                xCell.setFormula(item.getCertificateDescription().replace("Сертификат:", ""));

                xCell = xSpreadsheet.getCellByPosition(33, row);
                xCell.setFormula("ОАО \"8 Марта 246022, г. Гомель, ул. Советская, д.41");
                row++;
            }

        } catch (Exception e) {
            System.out.println("Ошибка формирования отчета ");
            e.printStackTrace();
        }

        ZipUtil.pack(new File(imageDir), new File(savePath + documentNumber + "_IMAGES.zip"));

        return true;
    }

    private int getMinGrowth(String values, int value) {

        String[] params = values.split(",");
        if (params.length > 1) {
            String[] growthMin = params[0].split("-");
            if (growthMin.length > 1) {
                try {
                    return Integer.valueOf(growthMin[1]);
                } catch (Exception e) {
                    return value;
                }
            }
        }
        return value;
    }

    private boolean createDirectory(String path) {
        File file = new File(path);
        try {
            return file.mkdirs();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private void purgeDirectory(File dir) {
        for (File file : dir.listFiles()) {
            if (file.isDirectory()) {
                purgeDirectory(file);
            }
            file.delete();
        }
    }

    private String copyImage(String fileName_, String source, String destinationDir) {
        // Формируем имя файла
        int index = 1;
        String fileName = fileName_ + ".jpg";
        while (existImage(destinationDir, fileName)) {
            fileName = fileName_ + "_" + index + ".jpg";
            index++;
        }
        copyFile(source, destinationDir + fileName);
        return fileName;
    }

    private boolean existImage(String path, String fileName) {
        File file = new File(path + fileName);
        return file.exists();
    }

    private void copyFile(String sourceFile, String destinationFile) {
        if (sourceFile == null) {
            return;
        }
        File source = new File(sourceFile);
        File destination = new File(destinationFile);
        try {

            FileUtils.copyFile(source, destination);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
