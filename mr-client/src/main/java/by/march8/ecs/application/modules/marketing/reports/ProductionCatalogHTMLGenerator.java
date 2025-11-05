package by.march8.ecs.application.modules.marketing.reports;

import by.march8.api.utils.DateUtils;
import by.march8.ecs.MainController;
import by.march8.ecs.application.modules.filemanager.model.ColorPresetHelper;
import by.march8.ecs.application.modules.filemanager.model.ImageItem;
import by.march8.ecs.application.modules.marketing.model.ProductCatalogArticleReport;
import by.march8.ecs.application.modules.marketing.model.ProductCatalogReport;
import by.march8.ecs.application.modules.marketing.model.ProductionCatalogCategoryReport;
import by.march8.ecs.application.modules.references.classifier.model.ItemNameReplacer;
import by.march8.ecs.application.modules.references.classifier.ui.ClassifierTree;
import by.march8.ecs.framework.common.Settings;
import by.march8.ecs.services.images.ColorImageService;
import by.march8.ecs.services.images.ModelImageServiceDB;
import by.march8.entities.product.ProductionCatalogProductReport;
import org.apache.commons.io.FileUtils;
import org.zeroturnaround.zip.ZipUtil;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by andy on 27.01.19.
 */
@SuppressWarnings("all")
public class ProductionCatalogHTMLGenerator {
    public final static String BLOCK_CONTENT_TOP = "BLOCK_CONTENT_TOP";
    public final static String BLOCK_CONTENT_CENTER = "BLOCK_CONTENT_CENTER";
    public final static String BLOCK_CONTENT_BOTTOM = "BLOCK_CONTENT_BOTTOM";

    public final static String BLOCK_ANCHOR = "BLOCK_ANCHOR";
    public final static String BLOCK_ANCHOR_CONTENT = "BLOCK_ANCHOR_CONTENT";
    public final static String BLOCK_ANCHOR_LINK = "BLOCK_ANCHOR_LINK";

    public final static String BLOCK_PRODUCT_INFORMATION = "BLOCK_PRODUCT_INFORMATION";
    public final static String BLOCK_PRODUCT_COLORS = "BLOCK_PRODUCT_COLORS";
    public final static String BLOCK_PRODUCT_OLD_PRICE = "BLOCK_PRODUCT_OLD_PRICE";
    public final static String BLOCK_PRODUCT_PRICE = "BLOCK_PRODUCT_PRICE";
    public final static String BLOCK_PRODUCT_PHOTOS = "BLOCK_PRODUCT_PHOTOS";


    public final static String BLOCK_PRODUCT_SIZES = "BLOCK_PRODUCT_SIZES";

    public final static String VAR_CATALOG_INFORMATION = "$CATALOG_INFORMATION";
    public final static String VAR_CATALOG_ID = "$CATALOG_ID";
    public final static String VAR_CATALOG_NUMBER = "$CATALOG_NUMBER";
    public final static String VAR_CATALOG_DATE = "$CATALOG_DATE";
    public final static String VAR_CURRENCY_ID = "$CURRENCY_ID";
    public final static String VAR_CURRENCY_DATE = "$CURRENCY_DATE";
    public final static String VAR_CURRENCY_RATE = "$CURRENCY_RATE";


    public final static String VAR_TITLE_TEXT = "$TITLE_TEXT";
    public final static String VAR_ANCHOR_ID = "$ANCHOR_ID";

    public final static String VAR_ANCHOR_TEXT = "$ANCHOR_TEXT";

    public final static String VAR_ANCHOR_LINK_ID = "$ANCHOR_LINK_ID";
    public final static String VAR_ANCHOR_LINK_TEXT = "$ANCHOR_LINK_TEXT";

    public final static String VAR_FORM_HEADER_TEXT = "$FORM_HEADER_TEXT";
    public final static String VAR_PRODUCT_ID = "$PRODUCT_ID";
    public final static String VAR_PRODUCT_MAIN_PHOTO = "$PRODUCT_MAIN_PHOTO";
    public final static String VAR_PRODUCT_INFORMATION = "$PRODUCT_INFORMATION";
    public final static String VAR_PRODUCT_COLOR_ID = "$PRODUCT_COLOR_ID";
    public final static String VAR_PRODUCT_COLOR_TEXT = "$PRODUCT_COLOR_TEXT";
    public final static String VAR_PRODUCT_SIZE_ID = "$PRODUCT_SIZE_ID";
    public final static String VAR_PRODUCT_SIZE_TEXT = "$PRODUCT_SIZE_TEXT";
    public final static String VAR_OLD_PRICE_VALUE = "$OLD_PRICE_VALUE";
    public final static String VAR_PRICE_VALUE = "$PRICE_VALUE";
    public final static String VAR_PRODUCT_COLORS = "$PRODUCT_COLORS";

    public final static String VAR_PRODUCT_MODEL_ID = "$PRODUCT_MODEL_ID";
    public final static String VAR_PRODUCT_PHOTO = "$PRODUCT_PHOTO";
    public final static String VAR_PRODUCT_PHOTO_TEXT = "$PRODUCT_IMAGE_TEXT";
    public final static String VAR_PAGE_CONTENT = "$PAGE_CONTENT";

    public final static String VAR_MODEL_NAME = "$MODEL_NAME";
    public final static String VAR_ARTICLE_NAME = "$ARTICLE_NAME";

    private ColorImageService imageService;
    private ProductCatalogReport data;

    public ProductionCatalogHTMLGenerator(ProductCatalogReport data_) {
        imageService = ModelImageServiceDB.getInstance();
        data = data_;
    }

    public void createPage(String pathToSave) {
        String templatePath = MainController.getRunPath() + "/Templates/htmlTemplates/catalogTemplate/";
        System.out.println("Путь к файлу шаблона : " + templatePath);
        System.out.println("Путь к сохраненному файлу : " + pathToSave);
        String templateName = "index.html";

        File htmlTemplateFile = new File(templatePath + templateName);
        String htmlTemplate;
        try {
            System.out.println("Чтение шаблона :" + htmlTemplateFile.getAbsolutePath() + " : " + htmlTemplateFile.getName());
            htmlTemplate = FileUtils.readFileToString(htmlTemplateFile, "utf-8");
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        //***************************************************************************
        // Восстановление каталога назначения
        if (pathToSave == null) {
            pathToSave = Settings.TEMPORARY_DIR + "html_" + DateUtils.getNormalDateFormatDelimiter(new Date());
        }


        System.out.println("Создается каталог для сохранения: " + pathToSave);
        // Создаем каталог на вссякий случАй
        // Создаем каталог на вссякий случАй
        createDirectory(pathToSave + "mail/");
        createDirectory(pathToSave + "preview/");
        // Воссоздадим каталоги для каталога))
        System.out.println("Создается каталог для сохранения: css");
        createDirectory(pathToSave + "preview/css/");
        System.out.println("Создается каталог для сохранения: images");
        createDirectory(pathToSave + "preview/images/");
        // Почистим каталог скартинками, потому как остальное сохраняется с заменой
        // и копии картинок по механике(_x+1) плодиться не будут
        purgeDirectory(new File(pathToSave + "preview/images/"));
        purgeDirectory(new File(pathToSave + "mail/"));
        // Скопируем содержимое шаблона в конечную папку
        System.out.println("Копируется содержимое WEB страницы: CSS");
        copyDir(templatePath + "css/", pathToSave + "preview/css/");
        // Дирректории воссозданы...вы великолепны
        //***************************************************************************

        // ***************************************************************************************
        // ПОДГОТОВКА ШАБЛОНА К ОБРАБОТКЕ
        System.out.println("Подготовка шаблона...");
        //Вырежем Якорь
        String blockAnchorMarker = getContentForBlock(htmlTemplate, BLOCK_ANCHOR);
        String itemAnchorMarker = getContentForBlock(blockAnchorMarker, BLOCK_ANCHOR_CONTENT);
        blockAnchorMarker = setMarkerForBlock(blockAnchorMarker, BLOCK_ANCHOR_CONTENT, "$" + BLOCK_ANCHOR_CONTENT);
        //Обновим шаблон вырезанием
        htmlTemplate = setMarkerForBlock(htmlTemplate, BLOCK_ANCHOR, VAR_PAGE_CONTENT);

        // ВЫрезаем из шаблона блок описания модели
        String blockProduct = getContentForBlock(htmlTemplate, BLOCK_CONTENT_CENTER);
        htmlTemplate = setMarkerForBlock(htmlTemplate, BLOCK_CONTENT_CENTER, "");

        String blockProductInformation = getContentForBlock(blockProduct, BLOCK_PRODUCT_INFORMATION);
        blockProduct = setMarkerForBlock(blockProduct, BLOCK_PRODUCT_INFORMATION, "$" + BLOCK_PRODUCT_INFORMATION);

        // Вырезаем из шаблона блок цвета
        String blockColors = getContentForBlock(blockProduct, BLOCK_PRODUCT_COLORS);
        blockProduct = setMarkerForBlock(blockProduct, BLOCK_PRODUCT_COLORS, "$" + BLOCK_PRODUCT_COLORS);

        // Вырезаем из шаблона блок размеров
        String blockSizes = getContentForBlock(blockProduct, BLOCK_PRODUCT_SIZES);
        blockProduct = setMarkerForBlock(blockProduct, BLOCK_PRODUCT_SIZES, "$" + BLOCK_PRODUCT_SIZES);
        // Вырезаем из блок размеров старую цену
        String blockOldPrice = getContentForBlock(blockSizes, BLOCK_PRODUCT_OLD_PRICE);
        blockSizes = setMarkerForBlock(blockSizes, BLOCK_PRODUCT_OLD_PRICE, "$" + BLOCK_PRODUCT_OLD_PRICE);
        // Вырезаем из блок размеров старую цену
        String blockPrice = getContentForBlock(blockSizes, BLOCK_PRODUCT_PRICE);
        blockSizes = setMarkerForBlock(blockSizes, BLOCK_PRODUCT_PRICE, "$" + BLOCK_PRODUCT_PRICE);
        // Вырезаем блок перехода по маркерам
        String blockAnchorLink = getContentForBlock(blockProduct, BLOCK_ANCHOR_LINK);
        blockProduct = setMarkerForBlock(blockProduct, BLOCK_ANCHOR_LINK, "$" + BLOCK_ANCHOR_LINK);

        // Вырезаем блок изображений для модели
        String blockImages = getContentForBlock(blockProduct, BLOCK_PRODUCT_PHOTOS);
        blockProduct = setMarkerForBlock(blockProduct, BLOCK_PRODUCT_PHOTOS, "$" + BLOCK_PRODUCT_PHOTOS);
        // Шаблон подготовлен, теперь самое важное - не напутать м магией ниже
        //********************************************************************************************

        //Формируем страницу
        String doc = "Каталог продукции " + data.getDocument().getDocumentInformation() + "г." +
                "(" + data.getPreset().getCurrencyType().getName() + ")";

        htmlTemplate = htmlTemplate.replace(VAR_CATALOG_INFORMATION, doc)
                .replace(VAR_CATALOG_ID, String.valueOf(data.getDocument().getId()))
                .replace(VAR_CATALOG_NUMBER, data.getDocument().getDocumentNumber())
                .replace(VAR_CATALOG_DATE, DateUtils.dateToSQLTimestampShort(data.getDocument().getDocumentDate()))
                .replace(VAR_CURRENCY_ID, String.valueOf(data.getPreset().getCurrencyType().getId()))
                .replace(VAR_CURRENCY_RATE, String.valueOf(data.getPreset().getCurrencyRate()))
                .replace(VAR_CURRENCY_DATE, DateUtils.dateToSQLTimestampShort(data.getPreset().getCurrencyDate()))
                .replace(VAR_CATALOG_DATE, DateUtils.dateToSQLTimestampShort(data.getDocument().getDocumentDate()))
                .replace(VAR_TITLE_TEXT, doc + " : ОАО 8 Марта ");

        System.out.println("Заполнение шаблона");
        String pageContent = "";
        String topCategory = "";
        String topMarker = null;

        HashMap<String, String> imageMap = new HashMap<>();

        for (ProductionCatalogCategoryReport category : data.getCategoryList()) {
            String newAnchor = "";
            String marker = "";

            if (topMarker == null) {
                marker += itemAnchorMarker.replace(VAR_ANCHOR_ID, getTopCategory(category.getCategoryId()))
                        .replace(VAR_ANCHOR_TEXT, "Продукция")
                        + "&#8226;";
                topMarker = "top";
            }

            if (!topCategory.equals(getTopCategory(category.getCategoryId()))) {
                marker += itemAnchorMarker.replace(VAR_ANCHOR_ID, getTopCategory(category.getCategoryId()))
                        .replace(VAR_ANCHOR_TEXT, getTopCategoryName(category.getCategoryId()))
                        + "&#8226;";
            }

            marker += itemAnchorMarker.replace(VAR_ANCHOR_ID, getLastCategory(category.getCategoryId()))
                    .replace(VAR_ANCHOR_TEXT, getLastCategoryName(category.getCategoryId()));
            topCategory = getTopCategory(category.getCategoryId());

            newAnchor += blockAnchorMarker
                    .replace("$BLOCK_ANCHOR_CONTENT", marker);

            pageContent += newAnchor;
            // Раскидываем модели в категории
            for (ProductCatalogArticleReport article : category.getData()) {

                // Наименование изделия
                String newProduct = blockProduct.replace(VAR_FORM_HEADER_TEXT, ItemNameReplacer.transform(article.getItemName()))
                        .replace(VAR_PRODUCT_ID, "product_" + article.getArticleCode())
                        .replace(VAR_MODEL_NAME, String.valueOf(article.getModelNumber()))
                        .replace(VAR_ARTICLE_NAME, String.valueOf(article.getArticleNumber()));

                //***********************************************************************************
                // ОБРАБОТКА КАРТИНОК
                ImageItem defaultImage = imageService.getDefaultImageByModelNumber(String.valueOf(article.getModelNumber()));
                if (defaultImage != null) {
                    String fileName = imageMap.get(imageService.getImageLargeName(article.getImage()));
                    if (fileName == null) {
                        fileName = copyImage(article.getModelNumber() + "_DEFAULT", imageService.getImageLargeName(article.getImage()), pathToSave);
                        imageMap.put(imageService.getImageLargeName(article.getImage()), fileName);
                    }
                    newProduct = newProduct.replace(VAR_PRODUCT_MAIN_PHOTO, "images/" + fileName);
                } else {
                    newProduct = newProduct.replace(VAR_PRODUCT_MAIN_PHOTO, "");
                }
                //***********************************************************************************

                //Модель
                String productInformation = blockProductInformation
                        .replace(VAR_PRODUCT_INFORMATION, "Модель: " + article.getModelNumber());
                // Артикул
                productInformation += blockProductInformation
                        .replace(VAR_PRODUCT_INFORMATION, "Артикул: " + article.getArticleNumber());
                // Состав
                productInformation += blockProductInformation
                        .replace(VAR_PRODUCT_INFORMATION, "Состав: " + article.getMaterialComposition());
                // Брэнд
                productInformation += blockProductInformation
                        .replace(VAR_PRODUCT_INFORMATION, "Бренд: " + article.getBrandName());
                newProduct = newProduct.replace("$" + BLOCK_PRODUCT_INFORMATION, productInformation);
                String colors = "";
                // Цвета


                for (String color : getColors(article.getColorsArticle())) {
                    if (color != null) {


                        colors += blockColors.replace(VAR_PRODUCT_COLOR_ID, ColorPresetHelper.colorToHTMLIds(color))
                                .replace(VAR_PRODUCT_COLOR_TEXT, color);
                    }
                }
                //Размеры
                newProduct = newProduct.replace("$" + BLOCK_PRODUCT_COLORS, colors);
                String sizes = "";
                for (ProductionCatalogProductReport product : article.getData()) {

                    String newSize = blockSizes.replace(VAR_PRODUCT_SIZE_ID, String.valueOf(product.getProductCode()))
                            .replace(VAR_PRODUCT_MODEL_ID, String.valueOf(product.getArticleCode()))
                            .replace(VAR_PRODUCT_COLORS, product.getColorsProduct())
                            .replace(VAR_PRODUCT_SIZE_TEXT, product.getSizePrint());
                    String oldPrice = blockOldPrice.replace(VAR_OLD_PRICE_VALUE, String.valueOf(product.getPriceVale()));
                    String newPrice = blockPrice.replace(VAR_PRICE_VALUE, String.valueOf(product.getPriceDiscountValue()));
                    /*if (product.getDiscountValue() > 0) {
                        newSize = newSize.replace("$" + BLOCK_PRODUCT_OLD_PRICE, oldPrice);
                        newSize = newSize.replace("$" + BLOCK_PRODUCT_PRICE, newPrice);
                    } else {*/
                    newSize = newSize.replace("$" + BLOCK_PRODUCT_OLD_PRICE, "");
                    newSize = newSize.replace("$" + BLOCK_PRODUCT_PRICE, newPrice);
                    //}
                    sizes += newSize;
                }
                newProduct = newProduct.replace("$" + BLOCK_PRODUCT_SIZES, sizes);

                //**************************************************************************************
                // Изображения

                String image = "";

                List<ImageItem> images = imageService.getColorImageListByModel(String.valueOf(article.getModelNumber()));
                if (images != null) {
                    for (ImageItem item : images) {
                        String fileName = imageMap.get(item.getImageFile());
                        if (fileName == null) {
                            fileName = copyImage(article.getModelNumber() + "_" + item.getColor(), item.getImageFile(), pathToSave);
                            imageMap.put(item.getImageFile(), fileName);
                        }
                        image += blockImages.replace(VAR_PRODUCT_PHOTO, "images/" + fileName)
                                .replace(VAR_PRODUCT_PHOTO_TEXT, item.getColor())
                                .replace(VAR_PRODUCT_ID, "product_" + article.getArticleCode());
                    }
                }


                newProduct = newProduct.replace("$" + BLOCK_PRODUCT_PHOTOS, image);
                //**************************************************************************************

                // Переход по якорям
                String anchorLink = blockAnchorLink.replace(VAR_ANCHOR_LINK_ID, topMarker)
                        .replace(VAR_ANCHOR_LINK_TEXT, "Продукция") + "&#8226;";
                anchorLink += blockAnchorLink.replace(VAR_ANCHOR_LINK_ID, getTopCategory(category.getCategoryId()))
                        .replace(VAR_ANCHOR_LINK_TEXT, getTopCategoryName(category.getCategoryId())) + "&#8226;";
                anchorLink += blockAnchorLink.replace(VAR_ANCHOR_LINK_ID, getLastCategory(category.getCategoryId()))
                        .replace(VAR_ANCHOR_LINK_TEXT, getLastCategoryName(category.getCategoryId()));


                newProduct = newProduct.replace("$" + BLOCK_ANCHOR_LINK, anchorLink);
                pageContent += newProduct;
            }
        }

        htmlTemplate = putDataToTemplate(htmlTemplate, VAR_PAGE_CONTENT, pageContent);
        //.replace("\n","")
        //.replace("\r","")
        //.replace("  ","");

        File outputFile = new File(pathToSave + "preview/index.html");
        System.out.println("Сохраняем созданный документ: " + outputFile.getAbsolutePath() + ":" + outputFile.getName());
        try {
            // Сохраняем HTML
            FileUtils.writeStringToFile(outputFile, htmlTemplate, Charset.forName("utf-8"));
            //Создаем архив каталога
            ZipUtil.pack(new File(pathToSave + "preview/"), new File(pathToSave + "mail/catalog_" + data.getDocument().getDocumentNumber() + ".zip"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String putDataToTemplate(String template, String key, String value) {
        return template.replace(key, value);
    }

    private String getContentForBlock(String template, String blockId) {
        String openKey = getOpenKey(blockId);
        String closeKey = getCloseKey(blockId);

        int startId = template.indexOf(openKey);
        int stopId = template.indexOf(closeKey);
        return template.substring(startId + openKey.length(), stopId);
    }

    private String setMarkerForBlock(String template, String blockId, String marker) {
        String openKey = getOpenKey(blockId);
        String closeKey = getCloseKey(blockId);

        int startId = template.indexOf(openKey);
        int stopId = template.indexOf(closeKey);
        return template.substring(0, startId) + marker + template.substring(stopId + closeKey.length(), template.length());
    }

    private String parseTemplate(String template, String open, String close) {
        int startId = template.indexOf(open);
        int stopId = template.indexOf(close);
        return template.substring(startId, stopId + close.length());
    }

    public String getOpenKey(String variable) {
        return "<!--" + variable.trim() + "-->";
    }

    public String getCloseKey(String variable) {
        return "<!--/" + variable.trim() + "-->";
    }

    public String[] getColors(String colors) {
        if (colors == null) {
            colors = "РАЗНОЦВЕТ";
        }
        return colors.split(",");
    }

    public String getTopCategory(String category) {
        String[] categories = category.split("_");
        return categories[0];
    }

    public String getLastCategory(String category) {
        String[] categories = category.split("_");
        if (categories.length > 1) {
            return categories[1];
        }
        return null;
    }

    public String getTopCategoryName(String category) {
        String[] categories = category.split("_");
        return ClassifierTree.getNameAssortmentByArticleSegment(Integer.valueOf(categories[0]));
    }

    public String getLastCategoryName(String category) {
        String[] categories = category.split("_");
        if (categories.length > 1) {
            return ClassifierTree.getNameAssortmentByArticleSegment(Integer.valueOf(categories[1]));
        }
        return null;
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

    private void copyDir(String sourceFile, String destinationFile) {
        File source = new File(sourceFile);
        File destination = new File(destinationFile);
        try {
            FileUtils.copyDirectory(source, destination);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void purgeDirectory(File dir) {
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
        while (existImage(destinationDir + "preview/images/", fileName)) {
            fileName = fileName_ + "_" + index + ".jpg";
            index++;
        }
        copyFile(source, destinationDir + "preview/images/" + fileName);
        return fileName;
    }

    private boolean existImage(String path, String fileName) {
        File file = new File(path + fileName);
        return file.exists();
    }

}
