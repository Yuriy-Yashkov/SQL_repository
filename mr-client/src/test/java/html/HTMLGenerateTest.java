package html;

import by.march8.ecs.application.modules.warehouse.external.shipping.model.SaleDocumentReport;
import by.march8.entities.warehouse.SaleDocumentBase;
import by.march8.entities.warehouse.SaleDocumentDetailItemReport;
import org.apache.commons.io.FileUtils;
import org.junit.Ignore;
import org.junit.Test;
import org.zeroturnaround.zip.ZipUtil;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * @author Andy 19.12.2017.
 */

public class HTMLGenerateTest {

    String separator = File.separator;


    @Test
    @Ignore
    public void testWorkWithTemplate() {
        // Блок инициализации переменных
        String outputDir = "path/";
        String docNumber = "ЭИ12345678";
        String titleText = "Приложение к документу №";

        String path = "Templates" + separator + "html" + separator;

        //String path = "file:///" + MainController.runPath.replace('\\', '/') + "/" + "Templates/" + "additionPriceList.html";
        //String path = "Templates" + separator + "html" + separator + "additionPriceList.html";
        //String path = "Templates" + separator + "html" + separator + "templateGeneralTable.html";

        try {
            FileUtils.cleanDirectory(new File(outputDir));
        } catch (IOException e) {
            e.printStackTrace();
        }

        File htmlTemplateFile = new File(path + "templateGeneralTable.html");
        String templateGeneralTable = "";
        try {
            templateGeneralTable = FileUtils.readFileToString(htmlTemplateFile);
        } catch (Exception e) {
            e.printStackTrace();
        }

        htmlTemplateFile = new File(path + "additionPriceList.html");
        String htmlString;
        try {
            htmlString = FileUtils.readFileToString(htmlTemplateFile);

            SaleDocumentReport report = getReportData();
            List<SaleDocumentDetailItemReport> list = report.getDetailList();

            //String body = "";
            htmlString = htmlString.replace("$title", titleText + docNumber);
            htmlString = htmlString.replace("$docInfo", docNumber + " от 10.12.2018г.");
            //htmlString = htmlString.replace("$body", body);


            //Блок копирования зависимостей в указанную дирректорию
            // Копируем таблицу стилей [style.css]
            //   copyFile("Templates"+separator+"html"+separator+"css"+separator+"style.css",outputDir+"css"+separator+"style.css");
            //    copyFile("Templates"+separator+"html"+separator+"bg.jpg",outputDir+"css"+separator+"bg.jpg");
            copyDir("Templates" + separator + "html" + separator + "css", outputDir + "css");

            // Копируем изображения в выходную дирректорию
            File catalogDir = new File(outputDir + "catalog");
            try {
                catalogDir.mkdir();
                // Скопируем изображения в выходную дирректорию
                for (SaleDocumentDetailItemReport item : list) {
                    String fileName = item.getModelNumber() + ".jpg";
                    File outFile = new File(outputDir + "catalog" + separator + fileName);
                    if (!outFile.exists()) {
                        //String oldFile = item.getImagePath();
                        copyFile(item.getImagePath(), outputDir + "catalog" + separator + fileName);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            htmlString = htmlString.replace("$general_table", createSpecificationTable(list, templateGeneralTable, outputDir + "catalog" + separator));

            // сохраняем файл контекста в выбранную дирректорию
            File outputFile = new File(outputDir + "index.html");
            FileUtils.writeStringToFile(outputFile, htmlString);

            ZipUtil.pack(new File(outputDir), new File("D:\\january.zip"));

            // Запуск браузера для просмотра
            Desktop.getDesktop().browse(outputFile.toURI());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String createSpecificationTable(List<SaleDocumentDetailItemReport> list, String templateGeneralTable, String outputDir) {

        if (templateGeneralTable.equals("")) {
            return "";
        }

        //String path = "file:///" + MainController.runPath.replace('\\', '/') + "/" + "Templates/" + "additionPriceList.html";

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
                for (String img : getImagesByModelNumber(s)) {
                    count++;
                    String fileName = s + "_" + count + ".jpg";
                    currThumbTable += templateThumb.replace("$thumb_path", "catalog/" + fileName);
                    copyFile(img, outputDir + fileName);
                }

                if (count > 0) {
                    currGeneralTable = currGeneralTable.replace("$thumb_image", startThumbTag + currThumbTable + endThumbTag);
                }

                finalGeneralTable += currGeneralTable.replace("$spec_table", finalSpecTable) + "<br>";
            }
        }

        return finalGeneralTable;
    }

    private void copyFile(String sourceFile, String destFile) {
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

    // Демонстрационная структура документа
    private SaleDocumentReport getReportData() {
        SaleDocumentReport result = new SaleDocumentReport();

        SaleDocumentBase documentBase = new SaleDocumentBase();
        documentBase.setDocumentNumber("EA128500");
        documentBase.setDocumentDate(new Date());
        result.setDocument(documentBase);

        List<SaleDocumentDetailItemReport> list = new ArrayList<>();

        //list.add(new SaleDocumentDetailItemReport("","","","",1,"",3,""));
        list.add(new SaleDocumentDetailItemReport("Блузка", "105050", "9090909090", "Красный", 1, "12-23-12", 3, "\\\\file-server\\catalog\\resize\\Мужской ассортимент\\ГС\\Джемпера, фуфайки короткий рукав\\00610_1.JPG"));
        list.add(new SaleDocumentDetailItemReport("Брюки женские", "203040", "1234123421", "Пурпурный", 9, "12-23-12", 3, "\\\\file-server\\catalog\\resize\\Мужской ассортимент\\КОРОТКИЙ РУКАВ\\фуфайки\\7679.JPG"));
        list.add(new SaleDocumentDetailItemReport("Брюки женские", "203040", "1234123421", "Синий", 9, "12-23-12", 3, "\\\\file-server\\catalog\\resize\\Мужской ассортимент\\КОРОТКИЙ РУКАВ\\фуфайки\\7679.JPG"));
        list.add(new SaleDocumentDetailItemReport("Брюки женские ЛАТЕКС", "203040", "1234123421", "Голубой", 9, "13-23-12", 3, "\\\\file-server\\catalog\\resize\\Мужской ассортимент\\КОРОТКИЙ РУКАВ\\фуфайки\\7679.JPG"));
        list.add(new SaleDocumentDetailItemReport("Брюки женские ЛЮКС", "203040", "1234123421", "Черный", 9, "14-23-12", 3, "\\\\file-server\\catalog\\resize\\Мужской ассортимент\\КОРОТКИЙ РУКАВ\\фуфайки\\7679.JPG"));
        list.add(new SaleDocumentDetailItemReport("Джемпер мальч", "506070", "3456345636", "Зеленый", 1, "12-23-12", 3, "\\\\file-server\\catalog\\resize\\Детский ассортимент\\МАЛЬЧИКИ\\ТМ VOSMAE\\ВЕРХ\\ДЛИННЫЙ РУКАВ\\джемпера\\10903.JPG"));
        list.add(new SaleDocumentDetailItemReport("Джемпер мальч", "506070", "3456345636", "Зеленый", 1, "12-23-12", 3, "\\\\file-server\\catalog\\resize\\Детский ассортимент\\МАЛЬЧИКИ\\ТМ VOSMAE\\ВЕРХ\\ДЛИННЫЙ РУКАВ\\джемпера\\10903.JPG"));
        list.add(new SaleDocumentDetailItemReport("Джемпер мальч", "506070", "3456345636", "Зеленый", 1, "12-23-12", 3, "\\\\file-server\\catalog\\resize\\Детский ассортимент\\МАЛЬЧИКИ\\ТМ VOSMAE\\ВЕРХ\\ДЛИННЫЙ РУКАВ\\джемпера\\10903.JPG"));
        list.add(new SaleDocumentDetailItemReport("Джемпер мальч", "506070", "3456345636", "Зеленый", 1, "12-23-12", 3, "\\\\file-server\\catalog\\resize\\Детский ассортимент\\МАЛЬЧИКИ\\ТМ VOSMAE\\ВЕРХ\\ДЛИННЫЙ РУКАВ\\джемпера\\10903.JPG"));
        list.add(new SaleDocumentDetailItemReport("Джемпер мальч", "506070", "3456345636", "Зеленый", 1, "12-23-12", 3, "\\\\file-server\\catalog\\resize\\Детский ассортимент\\МАЛЬЧИКИ\\ТМ VOSMAE\\ВЕРХ\\ДЛИННЫЙ РУКАВ\\джемпера\\10903.JPG"));
        list.add(new SaleDocumentDetailItemReport("Джемпер мальч", "506070", "3456345636", "Зеленый", 1, "12-23-12", 3, "\\\\file-server\\catalog\\resize\\Детский ассортимент\\МАЛЬЧИКИ\\ТМ VOSMAE\\ВЕРХ\\ДЛИННЫЙ РУКАВ\\джемпера\\10903.JPG"));
        list.add(new SaleDocumentDetailItemReport("Джемпер мальч", "506070", "3456345636", "Зеленый", 1, "12-23-12", 3, "\\\\file-server\\catalog\\resize\\Детский ассортимент\\МАЛЬЧИКИ\\ТМ VOSMAE\\ВЕРХ\\ДЛИННЫЙ РУКАВ\\джемпера\\10903.JPG"));
        list.add(new SaleDocumentDetailItemReport("Джемпер мальч", "506070", "3456345636", "Зеленый", 1, "12-23-12", 3, "\\\\file-server\\catalog\\resize\\Детский ассортимент\\МАЛЬЧИКИ\\ТМ VOSMAE\\ВЕРХ\\ДЛИННЫЙ РУКАВ\\джемпера\\10903.JPG"));

        result.setDetailList(list);


        return result;
    }

    private List<String> getImagesByModelNumber(String modelNumber) {
        List<String> result = new ArrayList<>();
        switch (modelNumber) {
            case "105050":
                result.add("\\\\file-server\\catalog\\resize\\Мужской ассортимент\\КОРОТКИЙ РУКАВ\\майки верх\\6981_2.jpg");
                result.add("\\\\file-server\\catalog\\resize\\Мужской ассортимент\\КОРОТКИЙ РУКАВ\\майки верх\\3888.JPG");
                result.add("\\\\file-server\\catalog\\resize\\Мужской ассортимент\\БЕЛЬЕ (трусы, майки,  комплекты, фуфайки)\\фуфайки\\3681.JPG");
                break;
            case "203040":
                result.add("\\\\file-server\\catalog\\resize\\Чулочно-носочный ассортимент\\LONATI\\женский\\Плюш\\333 (1).png");
                result.add("\\\\file-server\\catalog\\resize\\Чулочно-носочный ассортимент\\LONATI\\женский\\544 рис2.jpg");
                result.add("\\\\file-server\\catalog\\resize\\Чулочно-носочный ассортимент\\LONATI\\детский\\540 (капсулы)\\МАЛЬЧИК\\Рэгби\\540,1.png");
                result.add("\\\\file-server\\catalog\\resize\\Детский ассортимент\\ДЕВОЧКИ\\КОЛЛЕКЦИИ\\БЕЛЬЕ\\КОЛЛЕКЦИЯ ПОНЧИКИ тм VOSMAE\\10999.JPG");
                break;
            case "506070":
                result.add("\\\\file-server\\catalog\\resize\\Детский ассортимент\\ДЕВОЧКИ\\КОЛЛЕКЦИИ\\ВЕРХ\\КОЛЛЕКЦИЯ СОВУШКА тм VOSMAE\\10855_комплект.jpg");
                result.add("\\\\file-server\\catalog\\resize\\Купальный ассортимент\\мужской ассортимент\\плавки\\2516_1.JPG");
                break;
        }
        return result;
    }
}
