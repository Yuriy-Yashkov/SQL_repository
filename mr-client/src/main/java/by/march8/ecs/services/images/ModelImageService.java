package by.march8.ecs.services.images;

import by.gomel.freedev.ucframework.uccore.utils.SystemUtils;
import by.march8.ecs.MainController;
import by.march8.ecs.application.modules.marketing.model.ModelImageSize;
import by.march8.ecs.framework.common.Settings;
import by.march8.ecs.services.IService;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author Andy 10.01.2018.
 */
public class ModelImageService implements IService, ImageService {
    private static ModelImageService instance = null;
    private List<String> sourceList = null;
    private HashMap<String, List<String>> map = null;
    private String catalogTreeFile = "";
    private String catalogDirectory = "";

    private ModelImageService() {
        reload();
    }

    public static ImageService getInstance() {
        if (instance == null) {
            instance = new ModelImageService();
        }
        return instance;
    }

    public static String parseModelNumberTrimLeft(String inputString) {
        for (int i = inputString.length() - 1; i > 0; i--) {
            char s = inputString.charAt(i);
            if (s == File.separatorChar) {
                return parseModelNumber(inputString.substring(i + 1));
            }
        }
        return null;
    }

    public static String parseModelNumber(String inputString) {
        for (int i = 0; i < inputString.length() - 1; i++) {
            char s = inputString.charAt(i);
            if (s > '9' || s < '0') {
                return inputString.substring(0, i);
            }
        }
        return null;
    }

    private void reload() {

        if (SystemUtils.isWindows()) {
            catalogTreeFile = Settings.CATALOG_TREE_FILE + ".windows";
            catalogDirectory = Settings.WINDOWS_CATALOG_PATH;
        } else {
            catalogTreeFile = Settings.CATALOG_TREE_FILE + ".linux";
            catalogDirectory = Settings.UNIX_CATALOG_PATH;
        }

        String file = MainController.getConfiguration().getProperty(Settings.PROPERTY_CATALOG_PATH, catalogDirectory + catalogTreeFile);
        sourceList = new ArrayList<>();

        File collectionFile = new File(catalogDirectory + catalogTreeFile);
        if (collectionFile.exists()) {
            try {
                MainController.getConfiguration().setProperty(Settings.PROPERTY_CATALOG_PATH, file);
                sourceList = FileUtils.readLines(collectionFile, "utf-8");
                map = prepareMap(sourceList);

                System.out.println("*******************************************************************************");
                System.out.println("Служба изображений запущена, загружено [" + sourceList.size() + "] записей");
                System.out.println("Файл коллекции [" + collectionFile.getAbsolutePath() + "]");
                System.out.println("*******************************************************************************");

            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            System.out.println("*******************************************************************************");
            System.out.println("Нет доступа к файлу [" + collectionFile.getAbsolutePath() + "]");
            System.out.println("*******************************************************************************");
        }
    }

    @Override
    public boolean haveImage(String model) {
        return map.get(model) != null;
    }

    private HashMap<String, List<String>> prepareMap(List<String> inputList) {
        HashMap<String, List<String>> result = new HashMap<>();
        for (String s : inputList) {
            String key = parseModelNumberTrimLeft(s);
            if (key != null) {
                List<String> itemList = result.get(key);
                if (itemList == null) {
                    List<String> newList = new ArrayList<>();
                    newList.add(s);
                    result.put(key, newList);
                } else {
                    itemList.add(s);
                }
            }
        }
        return result;
    }

    //getDefaultImageFileByModelNumber

    /**
     * Возвращает список найденных файлов содержащих номер модели в имени файла изображения
     *
     * @param modelNumber номер модели
     * @return список имен файлов
     */

    /**
     * Возвращает имя файла изображения с полным совпадением имени с номером модели [XXXX.jpg]
     *
     * @param modelNumber номер модели XXXX
     * @return имя файла
     */
    //@Override
    private String getImageFileByModelNumber(String modelNumber, ModelImageSize imageSize) {
        if (map == null) {
            return null;
        }

        String searchString = File.separator + modelNumber + ".jpg";
        List<String> arrayList = map.get(modelNumber);
        if (arrayList == null) {
            return null;
        }
//        System.out.println("ПОиск изделия " + searchString);
        try {
            for (String s : arrayList) {
                if (s.toLowerCase().endsWith(searchString)) {
                    if (imageSize == ModelImageSize.SMALL) {
                        return s.replace("resize", "resize_120");
                    }
                    return s;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Возвращает имя файла изображения по-умолчанию с полным совпадением имени с номером модели [XXXX.jpg]
     *
     * @param modelNumber номер модели XXXX
     * @return имя файла
     */
    @Override
    public String getDefaultImageFileByModelNumber(String modelNumber, ModelImageSize imageSize) {
        String img = getImageFileByModelNumber(modelNumber, imageSize);
        if (img == null) {
            List<String> list = getImageListByModelNumber(modelNumber, imageSize);
            if (list == null) {
                return null;
            }

            if (list.size() > 0) {
                return list.get(0);
            } else {
                return null;
            }
        } else {
            return img;
        }
    }

    /**
     * Возвращает список найденных файлов содержащих номер модели в имени файла изображения
     *
     * @param modelNumber номер модели
     * @return список имен файлов
     */
    @Override
    public List<String> getImageListByModelNumber(String modelNumber, ModelImageSize imageSize) {

        if (map == null) {
            return null;
        }

        java.util.List<String> result = new ArrayList<>();

        if (modelNumber.length() < 3) {
            return result;
        }

        List<String> arrayList = map.get(modelNumber);
        if (arrayList == null) {
            return null;
        }

        try {
            for (String s : arrayList) {
                if (s.contains(File.separator + modelNumber)) {
                    if (s.toLowerCase().endsWith(".jpg")) {
                        if (imageSize == ModelImageSize.SMALL) {
                            result.add(s.replace("resize", "resize_120"));
                        } else {
                            result.add(s);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return imageGrouping(result);
        //return result;
    }

    @Override
    public String getNoImageFile() {
        if (SystemUtils.isWindows()) {
            return catalogDirectory + "noimage.jpg";
        } else {
            return "/" + catalogDirectory + File.separator + "noimage.jpg";
        }
    }

    private List<String> imageGrouping(List<String> inList) {
        List<String> outList = new ArrayList<>();
        HashMap<String, String> map = new HashMap<>();
        String key;
        String[] parsArray;
        String separator = File.separator;

        if (SystemUtils.isWindows()) {
            separator += File.separator;
        }

        if (inList.size() == 0) {
            return inList;
        }

        for (String inString : inList) {
            parsArray = inString.split(separator);
            key = parsArray[parsArray.length - 1];
            map.put(key, inString);
        }

        Iterator it = map.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            String item = (String) pair.getValue();
            outList.add(item);
            it.remove();
        }

        return outList;
    }
}
