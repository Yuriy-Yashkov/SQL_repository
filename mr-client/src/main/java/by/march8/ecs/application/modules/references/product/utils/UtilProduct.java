package by.march8.ecs.application.modules.references.product.utils;

import by.march8.entities.product.ModelSizeChart;
import by.march8.entities.product.ModelSizeValue;

import java.text.DecimalFormat;
import java.util.Set;

/**
 * Created by lidashka.
 */
public class UtilProduct {

    public static boolean ACTION_BUTT_CREATE_PRINTSIZE = false;
    public static String ITEM_PRINTSIZE = "";

    public static String getValue(Float value) {
        try {
            DecimalFormat df = new DecimalFormat("#.####");

            return df.format(value);

        } catch (Exception e) {
            return String.valueOf(value);
        }
    }

    public static ModelSizeChart createCopySize(ModelSizeChart selectedSize, String text) {
        ModelSizeChart newSize = new ModelSizeChart();

        newSize.setModel(selectedSize.getModel());
        newSize.setHeight(selectedSize.getHeight());
        newSize.setSize(selectedSize.getSize());
        newSize.setNote(text + " " + selectedSize.getNote());

        Set<ModelSizeValue> sizeValue = selectedSize.getModelSizeValues();
        for (ModelSizeValue itemSizeValue : sizeValue) {
            ModelSizeValue newSizeValue = new ModelSizeValue(itemSizeValue);
            newSizeValue.setModelSize(newSize);
            newSize.getModelSizeValues().add(newSizeValue);
        }

        return newSize;
    }
}
