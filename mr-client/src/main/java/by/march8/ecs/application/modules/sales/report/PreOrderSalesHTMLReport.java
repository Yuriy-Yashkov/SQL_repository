package by.march8.ecs.application.modules.sales.report;

import by.march8.api.utils.DateUtils;
import by.march8.ecs.MainController;
import by.march8.ecs.application.modules.marketing.model.ModelImageSize;
import by.march8.ecs.application.modules.sales.model.PreOrderSaleDocumentReport;
import by.march8.ecs.framework.common.Settings;
import by.march8.ecs.services.images.ImageService;
import by.march8.ecs.services.images.ModelImageServiceDB;
import by.march8.entities.sales.PreOrderProductItem;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * @author Andy 14.01.2019 - 8:10.
 */
public class PreOrderSalesHTMLReport {

    private String separator = File.separator;
    private ImageService modelImageService;
    private List<PreOrderProductItem> productList;
    private boolean debug = true;

    private void prepareImagesPaths() {
        modelImageService = ModelImageServiceDB.getInstance();

    }

    public void create(String outputDir, PreOrderSaleDocumentReport reportData) {
        if (outputDir == null) {
            outputDir = Settings.TEMPORARY_DIR;
        }

        prepareImagesPaths();

        // Создаем каталог номера документа
        outputDir += DateUtils.getNormalDateTimeFormatPlus(new Date()) + separator;
        createDirectory(outputDir);

        try {
            FileUtils.cleanDirectory(new File(outputDir));
        } catch (IOException e) {
            e.printStackTrace();
        }


        createDirectory(outputDir + "mail");
        createDirectory(outputDir + "preview");

        // Блок инициализации переменных
        String docNumber = "TEST_NUMBER";
        String titleText = "Ознакомительный каталог продукции ОАО \"8 Марта\"";

        String templatePath = "";
        if (debug) {
            templatePath = "Templates" + separator + "html" + separator;
        } else {
            templatePath = MainController.getRunPath().replace('\\', '/') + "/" + "Templates" + separator + "html" + separator;
        }

        //String path = "file:///" + MainController.getRunPath().replace('\\', '/') + "/" + "Templates/" + "additionPriceList.html";
        //String path = "Templates" + separator + "html" + separator + "additionPriceList.html";
        //String path = "Templates" + separator + "html" + separator + "templateGeneralTable.html";

        File htmlTemplateFile = new File(templatePath + "templateCatalogTable.html");
        String templateGeneralTable = "";
        try {
            templateGeneralTable = FileUtils.readFileToString(htmlTemplateFile);
        } catch (Exception e) {
            e.printStackTrace();
        }

        htmlTemplateFile = new File(templatePath + "additionPriceList.html");
        String htmlString;
        try {
            htmlString = FileUtils.readFileToString(htmlTemplateFile);

            List<PreOrderProductItem> productList = reportData.getSpecification();

            //String body = "";
            htmlString = htmlString.replace("$title", titleText + docNumber);
            htmlString = htmlString.replace("$docInfo", docNumber + " от " + DateUtils.getNormalDateFormat(new Date()));
            //htmlString = htmlString.replace("$body", body);


            //Блок копирования зависимостей в указанную дирректорию
            // Копируем таблицу стилей [style.css]
            //   copyFile("Templates"+separator+"html"+separator+"css"+separator+"style.css",outputDir+"css"+separator+"style.css");
            //    copyFile("Templates"+separator+"html"+separator+"bg.jpg",outputDir+"css"+separator+"bg.jpg");
            copyDir(MainController.getRunPath().replace('\\', '/') + "/" + "Templates" + separator + "html" + separator + "css", outputDir + "preview" + separator + "css");

            // Копируем изображения в выходную дирректорию
            File catalogDir = new File(outputDir + "preview" + separator + "catalog");
            try {
                catalogDir.mkdir();
                // Скопируем изображения в выходную дирректорию
                /*for (SaleDocumentDetailItemReport item : list) {
                    String fileName = item.getModelNumber() + ".jpg";
                    File outFile = new File(outputDir + "preview" + separator + "catalog" + separator + fileName);
                    if (!outFile.exists()) {
                        //String oldFile = item.getImagePath();
                        copyFile(item.getImagePath(), outputDir + "preview" + separator + "catalog" + separator + fileName);
                    }
                }
                */
            } catch (Exception e) {
                e.printStackTrace();
            }

            htmlString = htmlString.replace("$general_table", createSpecificationTable(productList, templateGeneralTable, outputDir + "preview" + separator + "catalog" + separator));

            // сохраняем файл контекста в выбранную дирректорию
            File outputFile = new File(outputDir + "preview" + separator + "index.html");
            FileUtils.writeStringToFile(outputFile, htmlString);

            //ZipUtil.pack(new File(outputDir + "preview" + separator), new File(outputDir + "mail" + separator + document.getDocumentNumber() + ".zip"));

            // Запуск браузера для просмотра
            //Desktop.getDesktop().browse(outputFile.toURI());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String createSpecificationTable(List<PreOrderProductItem> list, String templateGeneralTable, String outputDir) {

        if (templateGeneralTable.equals("")) {
            return "";
        }

        //String path = "file:///" + MainController.getRunPath().replace('\\', '/') + "/" + "Templates/" + "additionPriceList.html";

        String templateSpecTable = "<tr id=\"$row_id\">" +
                "<td><a class=\"price_label\" id=\"$size_id\" value=\"$article_id\">$print_size</a></td>" +
                "<td><a class=\"price_label\" id=\"$cost_id\">$price</a></td>" +
                "<td>$check_box</td></tr>";

        String startThumbTag = "<tr><td colspan=\"2\"><hr></td></tr><tr ><td align=\"left\" colspan=\"2\">";
        String endThumbTag = "</td><tr>";
        String templateThumb = "<a href=\"$thumb_path\" class=\"thickbox\" data-lightbox=\"image-1\">" +
                "<img class=\"my-image-thumb\" border=\"0\" src=\"$thumb_path\"></a>";

        String templateCheckBox = "<div>" +
                "<button id=\"$ato_btn_id\" onclick=\"add_to_order_form(this)\">+</button>" +
                "</div>";


        String finalGeneralTable = "";
        String finalSpecTable;

        String currGeneralTable;
        String currSpecTable;

        int model;

        HashMap<Integer, List<PreOrderProductItem>> map = new HashMap<>();
        List<Integer> sequence = new ArrayList<>();
        for (PreOrderProductItem item : list) {
            model = item.getModelNumber();
            List<PreOrderProductItem> aList = map.get(model);
            if (aList == null) {
                aList = new ArrayList<>();
                map.put(model, aList);
                sequence.add(model);
            }
            aList.add(item);
        }

        for (Integer index : sequence) {
            finalSpecTable = "";
            List<PreOrderProductItem> aList = map.get(index);
            if (aList != null) {

                PreOrderProductItem _item = aList.get(0);

                for (PreOrderProductItem item : aList) {
                    currSpecTable = templateSpecTable
                            .replace("$row_id", "row_" + item.getId())
                            .replace("$article_id", String.valueOf(_item.getArticleId()))
                            .replace("$size_id", "size_" + item.getId())
                            .replace("$cost_id", "cost_" + item.getId())
                            .replace("$print_size", item.getPrintSize())
                            .replace("$price", String.format("%.2f", item.getPrice()).replace(",", "."))
                            .replace("$check_box", templateCheckBox.replace("$ato_btn_id", "ato_btn_" + item.getId())
                            );
                    finalSpecTable += currSpecTable;
                }

                currGeneralTable = templateGeneralTable
                        .replace("$navigation_path", "<a class=\"price_label\" href=\"#42\">Верхний трикотаж</a>" +
                                "<a class=\"price_label\">-</a>" +
                                "<a class=\"price_label\" href=\"#423\">Изделия детские</a>")
                        .replace("$item_name_id", "item_name_" + _item.getArticleId())
                        .replace("$item_model_id", "item_model_" + _item.getArticleId())
                        .replace("$item_name", _item.getName())
                        .replace("$model_number", String.valueOf(_item.getModelNumber()))
                        .replace("$tnvd_code", _item.getCodeTNVED())
                        .replace("$image_path", "catalog/" + _item.getModelNumber() + ".jpg");

                // Получить дополнительные картинки если есть
                String currThumbTable = "";

                //Обработка изображения по-умолчанию
                String defaultImage = modelImageService.getDefaultImageFileByModelNumber(String.valueOf(index), ModelImageSize.BIG);
                if (defaultImage != null) {
                    String fileName = index + ".jpg";
                    copyFile(defaultImage, outputDir + fileName);
                }

                int count = 0;

                List<String> imgList = modelImageService.getImageListByModelNumber(String.valueOf(index), ModelImageSize.BIG);
                if (imgList != null) {
                    for (String img : imgList) {
                        count++;
                        String fileName = index + "_" + count + ".jpg";
                        currThumbTable += templateThumb.replace("$thumb_path", "catalog/" + fileName);
                        copyFile(img, outputDir + fileName);
                    }
                }

                currGeneralTable = currGeneralTable.replace("$thumb_image", startThumbTag + currThumbTable + endThumbTag);
                finalGeneralTable += currGeneralTable.replace("$spec_table", finalSpecTable) + "<br>";
            }
        }

        return finalGeneralTable;
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

    private void copyFile(String sourceFile, String destFile) {
        if (sourceFile == null) {
            return;
        }
        File source = new File(sourceFile);
        File dest = new File(destFile);
        try {

            FileUtils.copyFile(source, dest);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void copyDir(String sourceFile, String destFile) {
        File source = new File(sourceFile);
        File dest = new File(destFile);
        try {
            FileUtils.copyDirectory(source, dest);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
