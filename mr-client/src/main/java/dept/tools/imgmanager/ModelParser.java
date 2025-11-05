package dept.tools.imgmanager;

import java.util.ArrayList;

/**
 * Класс реализует парсинг имени файла изображения модели для извлечения из его
 * имени информации, описанной в требованиях к загружаемым фотографиям
 */
public class ModelParser {
    private ArrayList<ItemModel> arrayModel = new ArrayList<ItemModel>();

    private ItemModel getAttributesModel(ItemModel itemModel, String sign) {
        String signs[] = new String[3];
        signs = sign.split("_");

        for (int i = 0; i < signs.length; i++) {
            if (!signs[i].equals("")) {
                // Получаем номер подмодели
                if (signs[i].startsWith("M")) {
                    itemModel.setSubmodel(Integer.valueOf(signs[i].substring(1,
                            signs[i].length())));
                    // Получение номера эскиза
                } else if (signs[i].startsWith("S")) {
                    itemModel.setSketch(Integer.valueOf(signs[i].substring(1,
                            signs[i].length())));
                } else {
                    // Получение номера фотографии в текущей модели
                    itemModel.setImageNumber(Integer.valueOf(signs[i]));
                }
            }
        }

        return itemModel;
    }

    /**Метод получает номер модели, разделено с получением дополнительных атрибутов*/
    private ItemModel getModelInfo(ItemModel itemModel) {
        String name = "";
        String fullName = itemModel.getFullName();
        int index = fullName.indexOf("_");
        if (index < 0) {
            name = fullName;
        } else {
            name = fullName.substring(0, index);
            getAttributesModel(itemModel,
                    fullName.substring(index + 1, fullName.length()));
        }
        itemModel.setName(name.trim());

        return itemModel;
    }

    public ArrayList<ItemModel> getModelList(String model) {
        arrayModel.clear();
        // извлекаем из имени model расширение jpg если есть
        model = model.substring(0, model.indexOf("."));
        String[] models = new String[4];
        models = model.split("=");

        for (int i = 0; i < models.length; i++) {
            if (!models[i].equals("")) {
                arrayModel.add(new ItemModel(models[i]));
            }
        }
        for (int i = 0; i < arrayModel.size(); i++) {
            getModelInfo(arrayModel.get(i));
        }
        return arrayModel;
    }

}
