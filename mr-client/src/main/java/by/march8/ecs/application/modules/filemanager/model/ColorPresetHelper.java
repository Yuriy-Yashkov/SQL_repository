package by.march8.ecs.application.modules.filemanager.model;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author Andy 29.12.2018 - 8:03.
 */
public class ColorPresetHelper {

    //3
    public static final Color CLR_WHITE = Color.white;
    //4
    public static final Color CLR_PINK = new Color(255, 192, 203);
    //5
    public static final Color CLR_GREEN = new Color(0, 80, 0);
    //2
    private static final Color CLR_ALL = new Color(100, 100, 100);
    //6
    private static final Color CLR_BEIGE = new Color(245, 245, 220);
    //7
    private static final Color CLR_AQUA = new Color(60, 158, 255);
    //8
    private static final Color CLR_VIOLET = new Color(148, 0, 211);
    //9
    private static final Color CLR_BLACK = Color.black;
    //10
    private static final Color CLR_BLUE = Color.blue;
    //11
    private static final Color CLR_YELLOW = new Color(255, 255, 0);
    //12
    private static final Color CLR_PERU = new Color(205, 133, 63);
    //13
    private static final Color CLR_TURQUOISE = new Color(64, 224, 208);
    //14
    private static final Color CLR_PEACHPUFF = new Color(255, 218, 185);
    //15
    private static final Color CLR_CORNSILK = new Color(220, 20, 60);
    //16
    private static final Color CLR_ORANGE = new Color(255, 165, 0);
    //17
    private static final Color CLR_GRAY = new Color(128, 128, 128);
    //18
    private static final Color CLR_JEANS = new Color(52, 63, 81);
    //19
    private static final Color CLR_RED = Color.RED;
    //20
    private static final Color CLR_CORNFLOWERBLUE = new Color(100, 149, 237);
    //21
    private static final Color CLR_SALAD = new Color(127, 255, 0);
    //22
    private static final Color CLR_OLIVE = new Color(128, 128, 0);
    //23
    private static final Color CLR_NUTS = new Color(77, 58, 32);

    public static ArrayList<ColorTextItem> array;

    private static HashMap<String, ColorTextItem> colorMap;
    private static HashMap<Integer, ColorTextItem> colorIdMap;


    public static void fillingColorList(JComboBox<ColorTextItem> comboBox) {
        if (array == null) {
            prepareArray();
        }
        // array.add(new PriceAgreementProtocol(11, new int[]{3093}, "ГринРозница-Акция", "протоколГринРозницаАкция.ots", ProtocolGreenRetailDiscountReport.class));
        comboBox.setModel(new DefaultComboBoxModel(array.toArray()));
        comboBox.setSelectedIndex(0);
    }

    public static void fillingColorListAsStringList(JComboBox<String> comboBox) {
        if (array == null) {
            prepareArray();
        }
        List<String> list = new ArrayList<>();
        for (ColorTextItem item : array) {
            list.add(item.getName());
        }
        // array.add(new PriceAgreementProtocol(11, new int[]{3093}, "ГринРозница-Акция", "протоколГринРозницаАкция.ots", ProtocolGreenRetailDiscountReport.class));
        comboBox.setModel(new DefaultComboBoxModel(list.toArray()));
        comboBox.setSelectedIndex(0);
    }

    public static void fillingColorList(JList<ColorTextItem> list) {
        if (array == null) {
            prepareArray();
        }

        DefaultListModel<ColorTextItem> listModel = new DefaultListModel<>();
        for (ColorTextItem item : array) {
            listModel.addElement(item);
        }

        list.setModel(listModel);
        list.setSelectedIndex(0);
    }

    private static void prepareArray() {
        array = new ArrayList<>();
        array.add(new ColorTextItem(2, "РАЗНОЦВЕТ", CLR_ALL, "CLR_ALL"));
        array.add(new ColorTextItem(3, "БЕЛЫЙ", CLR_WHITE, "CLR_WHITE"));
        array.add(new ColorTextItem(4, "РОЗОВЫЙ", CLR_PINK, "CLR_PINK"));
        array.add(new ColorTextItem(5, "ЗЕЛЕНЫЙ", CLR_GREEN, "CLR_GREEN"));
        array.add(new ColorTextItem(6, "БЕЖЕВЫЙ", CLR_BEIGE, "CLR_BEIGE"));
        array.add(new ColorTextItem(7, "ГОЛУБОЙ", CLR_AQUA, "CLR_AQUA"));
        array.add(new ColorTextItem(8, "ФИОЛЕТОВЫЙ", CLR_VIOLET, "CLR_VIOLET"));
        array.add(new ColorTextItem(9, "ЧЕРНЫЙ", CLR_BLACK, "CLR_BLACK"));
        array.add(new ColorTextItem(10, "СИНИЙ", CLR_BLUE, "CLR_BLUE"));
        array.add(new ColorTextItem(11, "ЖЕЛТЫЙ", CLR_YELLOW, "CLR_YELLOW"));
        array.add(new ColorTextItem(12, "КОРИЧНЕВЫЙ", CLR_PERU, "CLR_PERU"));
        array.add(new ColorTextItem(13, "БИРЮЗОВЫЙ", CLR_TURQUOISE, "CLR_TURQUOISE"));
        array.add(new ColorTextItem(14, "ПЕРСИК", CLR_PEACHPUFF, "CLR_PEACHPUFF"));
        array.add(new ColorTextItem(15, "МАЛИНА", CLR_CORNSILK, "CLR_CORNSILK"));
        array.add(new ColorTextItem(16, "ОРАНЖЕВЫЙ", CLR_ORANGE, "CLR_ORANGE"));
        array.add(new ColorTextItem(17, "СЕРЫЙ", CLR_GRAY, "CLR_GRAY"));
        array.add(new ColorTextItem(18, "ДЖИНС", CLR_JEANS, "CLR_JEANS"));
        array.add(new ColorTextItem(19, "КРАСНЫЙ", CLR_RED, "CLR_RED"));
        array.add(new ColorTextItem(20, "ВАСИЛЕК", CLR_CORNFLOWERBLUE, "CLR_CORNFLOWERBLUE"));
        array.add(new ColorTextItem(21, "САЛАТ", CLR_SALAD, "CLR_SALAD"));
        array.add(new ColorTextItem(22, "ОЛИВКОВЫЙ", CLR_OLIVE, "CLR_OLIVE"));
        array.add(new ColorTextItem(24, "НАБИВКА", CLR_WHITE, "CLR_WHITE"));
        array.add(new ColorTextItem(27, "ПОЛОСКА", CLR_BLACK, "CLR_BLACK"));
        array.add(new ColorTextItem(28, "ГОРОХ", CLR_NUTS, "CLR_NUTS"));

        colorMap = new HashMap<>();
        colorIdMap = new HashMap<>();
        for (ColorTextItem item : array) {
            colorMap.put(item.getName(), item);
            colorIdMap.put(item.getId(), item);
        }
    }

    public static ColorTextItem getColorByName(String colorName) {
        if (array == null) {
            prepareArray();
        }
        ColorTextItem result = colorMap.get(colorName.toUpperCase());
        if (result != null) {
            return result;
        }
        return new ColorTextItem(2, "РАЗНОЦВЕТ", CLR_ALL, "CLR_ALL");
    }

    public static ColorTextItem getColorById(Integer colorId) {
        if (array == null) {
            prepareArray();
        }
        ColorTextItem result = colorIdMap.get(colorId);
        if (result != null) {
            return result;
        }
        return new ColorTextItem(2, "РАЗНОЦВЕТ", CLR_ALL, "CLR_ALL");
    }


    public static List<ColorTextItem> getColorList() {
        prepareArray();
        return array;
    }

    public static String getHTMLColorString(Color color) {
        String red = Integer.toHexString(color.getRed());
        String green = Integer.toHexString(color.getGreen());
        String blue = Integer.toHexString(color.getBlue());

        return "#" +
                (red.length() == 1 ? "0" + red : red) +
                (green.length() == 1 ? "0" + green : green) +
                (blue.length() == 1 ? "0" + blue : blue);
    }

    public static List<ColorTextItem> getColorsAsList(String colors) {
        if (colors != null) {
            List<ColorTextItem> result = new ArrayList<>();
            String[] colors_ = colors.split(",");
            for (String color : colors_) {
                result.add(ColorPresetHelper.getColorByName(color));
            }
            return result;
        }
        return null;
    }


    public static String colorToHTMLIds(String color) {
        if (array == null) {
            prepareArray();
        }
        ColorTextItem result = colorMap.get(color.toUpperCase());
        if (result != null) {
            return result.getHtml().replace("_", "-").toLowerCase();
        }
        return "CLR_ALL";
    }

}
