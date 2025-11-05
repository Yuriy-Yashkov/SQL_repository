package by.march8.ecs.application.modules.warehouse.external.shipping.reports;

import by.march8.ecs.MainController;
import by.march8.ecs.application.modules.marketing.model.ModelImageSize;
import by.march8.ecs.application.modules.warehouse.external.shipping.model.SaleDocumentReport;
import by.march8.ecs.framework.common.Settings;
import by.march8.ecs.services.images.ImageService;
import by.march8.ecs.services.images.ModelImageService;
import by.march8.entities.warehouse.SaleDocumentBase;
import by.march8.entities.warehouse.SaleDocumentDetailItemReport;
import org.apache.commons.io.FileUtils;
import org.zeroturnaround.zip.ZipUtil;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author Andy 25.01.2018.
 */
public class AdditionHTMLReport {

    private SaleDocumentReport reportData;
    private SaleDocumentBase document;
    private List<SaleDocumentDetailItemReport> list;
    private HashMap<String, Object> map;

    private String separator = File.separator;
    private ImageService modelImageService;

    public AdditionHTMLReport(SaleDocumentReport report) {
        reportData = report;
        if (report != null) {
            document = reportData.getDocument();
            list = reportData.getDetailList();
            map = reportData.getDetailMap();

            prepareImagesPaths();
        }
    }

    private void prepareImagesPaths() {
        modelImageService = ModelImageService.getInstance();
        // Подготовка путей к картинкам
        for (SaleDocumentDetailItemReport item : list) {
            item.setImagePath(modelImageService.getDefaultImageFileByModelNumber(item.getModelNumber(), ModelImageSize.BIG));
        }
    }


    public void create(String outputDir) {
        if (outputDir == null) {
            outputDir = Settings.TEMPORARY_DIR;
        }

        // Создаем каталог номера документа
        outputDir += document.getDocumentNumber() + separator;
        createDirectory(outputDir);


        try {
            FileUtils.cleanDirectory(new File(outputDir));
        } catch (IOException e) {
            e.printStackTrace();
        }


        createDirectory(outputDir + "mail");
        createDirectory(outputDir + "preview");

        // Блок инициализации переменных
        String docNumber = document.getDocumentNumber();
        String titleText = "Приложение к документу №";

        String templatePath = MainController.getRunPath().replace('\\', '/') + "/" + "Templates" + separator + "html" + separator;

        //String path = "file:///" + MainController.getRunPath().replace('\\', '/') + "/" + "Templates/" + "additionPriceList.html";
        //String path = "Templates" + separator + "html" + separator + "additionPriceList.html";
        //String path = "Templates" + separator + "html" + separator + "templateGeneralTable.html";

        File htmlTemplateFile = new File(templatePath + "templateGeneralTable.html");
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

            List<SaleDocumentDetailItemReport> list = reportData.getDetailList();

            //String body = "";
            htmlString = htmlString.replace("$title", titleText + docNumber);
            htmlString = htmlString.replace("$docInfo", docNumber + " от " + document.getDocumentSaleDate());
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
                for (SaleDocumentDetailItemReport item : list) {
                    String fileName = item.getModelNumber() + ".jpg";
                    File outFile = new File(outputDir + "preview" + separator + "catalog" + separator + fileName);
                    if (!outFile.exists()) {
                        //String oldFile = item.getImagePath();
                        copyFile(item.getImagePath(), outputDir + "preview" + separator + "catalog" + separator + fileName);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            htmlString = htmlString.replace("$general_table", createSpecificationTable(list, templateGeneralTable, outputDir + "preview" + separator + "catalog" + separator));

            // сохраняем файл контекста в выбранную дирректорию
            File outputFile = new File(outputDir + "preview" + separator + "index.html");
            FileUtils.writeStringToFile(outputFile, htmlString);

            ZipUtil.pack(new File(outputDir + "preview" + separator), new File(outputDir + "mail" + separator + document.getDocumentNumber() + ".zip"));

            // Запуск браузера для просмотра
            //Desktop.getDesktop().browse(outputFile.toURI());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String createSpecificationTable(List<SaleDocumentDetailItemReport> list, String templateGeneralTable, String outputDir) {

        if (templateGeneralTable.equals("")) {
            return "";
        }

        //String path = "file:///" + MainController.getRunPath().replace('\\', '/') + "/" + "Templates/" + "additionPriceList.html";

        String templateSpecTable = "<tr><td>$color</td><td>$size</td><td>$grade</td><td>$amount</td></tr>";
        String startThumbTag = "<tr><td colspan=\"2\"><hr></td></tr><tr ><td align=\"left\" colspan=\"2\">";
        String endThumbTag = "</td><tr>";
        String templateThumb = "<a href=\"$thumb_path\" class=\"thickbox\" data-lightbox=\"image-1\">\n" +
                "<img class=\"my-image-thumb\" border=\"0\" src=\"$thumb_path\"></a>";

        String finalGeneralTable = "";
        String finalSpecTable;

        String currGeneralTable;
        String currSpecTable;

        String model;

        HashMap<String, List<SaleDocumentDetailItemReport>> map = new HashMap<>();
        List<String> sequence = new ArrayList<>();
        for (SaleDocumentDetailItemReport item : list) {
            model = item.getModelNumber();
            List<SaleDocumentDetailItemReport> aList = map.get(model);
            if (aList == null) {
                aList = new ArrayList<>();
                map.put(model, aList);
                sequence.add(model);
            }
            aList.add(item);
        }

        for (String s : sequence) {
            finalSpecTable = "";
            List<SaleDocumentDetailItemReport> aList = map.get(s);
            if (aList != null) {

                for (SaleDocumentDetailItemReport item : aList) {
                    currSpecTable = templateSpecTable
                            .replace("$color", item.getItemColor())
                            .replace("$size", item.getItemSizePrint())
                            .replace("$grade", String.valueOf(item.getItemGrade()))
                            .replace("$amount", String.valueOf(item.getAmountPrint()));
                    finalSpecTable += currSpecTable;
                }

                SaleDocumentDetailItemReport _item = aList.get(0);
                currGeneralTable = templateGeneralTable
                        .replace("$item_name", _item.getItemName())
                        .replace("$model_number", _item.getModelNumber())
                        .replace("$tnvd_code", _item.getTnvedCode())
                        .replace("$image_path", "catalog/" + _item.getModelNumber() + ".jpg");

                // Получить дополнительные картинки если есть
                String currThumbTable = "";

                int count = 0;

                List<String> imgList = modelImageService.getImageListByModelNumber(s, ModelImageSize.BIG);
                if (imgList != null) {
                    for (String img : imgList) {
                        count++;
                        String fileName = s + "_" + count + ".jpg";
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

    public String createHTMLReport(String outputDir, boolean archiveAfterCreate, boolean openAfterCreate) {
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

}
