package by.march8.tasks.maitenance.doubling;

import by.march8.tasks.maitenance.MaintenanceJDBC;
import common.DateUtils;
import dept.MyReportsModule;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class EAN13Doubling {
    private List<EAN13Entity> data;

    public EAN13Doubling() {
        MyReportsModule.confPath = System.getProperty("user.home") + "/.MyReports/";

        // Поиск дублей EAN для разных цветов
        doDoublingByColorService();

        // Поиск дублей EAN для одного кода изделия
        doDoublingByProductIdService();

        // ПОиск изделий использующих один EAN код
        doDoublingByEANCodeService();
    }

    private void doDoublingByColorService() {
        List<EAN13Entity> data = getEan13List();
        if (data == null) {
            return;
        }

        Set<String> setDoubling = new HashSet<>();
        Map<String, List<EAN13Entity>> map = new HashMap<>();

        for (EAN13Entity item : data) {
            String key = item.getEancode();
            List<EAN13Entity> list = map.get(key);
            if (list == null) {
                list = new ArrayList<>();
                map.put(key, list);
            }
            list.add(item);

            if (list.size() > 1) {
                if (!item.getEancode().trim().equals("")) {
                    setDoubling.add(item.getEancode());
                }
            }
        }

        List<String> list = new ArrayList<>();
        Set<Integer> setInt = new HashSet<>();

        for (String s : setDoubling) {
            StringBuilder text = new StringBuilder();

            for (EAN13Entity item : map.get(s)) {
                setInt.add(item.getColorId());
                text.append(item.getEancode())
                        .append("\t")
                        .append(item.getColorId())
                        .append("\t")
                        .append(item.getProductId())
                        .append("\t")
                        .append(DateUtils.getNormalDateFormat(item.getDate()))
                        .append("\t")
                        .append(item.getId())
                        .append("\n");
            }

            if (setInt.size() > 1) {
                list.add(text.toString());
            }

            setInt.clear();
        }

        try {
            FileUtils.writeLines(new File("D://ean-color.txt"), list);
        } catch (Exception e) {

        }
        System.out.println("Найдено дублей : " + setDoubling.size());
    }

    private void doDoublingByProductIdService() {
        List<EAN13Entity> data = getEan13List();
        if (data == null) {
            return;
        }

        Set<Integer> setDoubling = new HashSet<>();
        Map<Integer, List<EAN13Entity>> map = new HashMap<>();

        for (EAN13Entity item : data) {
            Integer key = item.getProductId();

            List<EAN13Entity> list = map.get(key);
            if (list == null) {
                list = new ArrayList<>();
                map.put(key, list);
            }

            list.add(item);
            if (list.size() > 1) {
                if (!item.getEancode().trim().equals("")) {
                    setDoubling.add(item.getProductId());
                }
            }
        }

        List<String> list = new ArrayList<>();
        Set<Integer> setInt = new HashSet<>();

        for (Integer s : setDoubling) {
            StringBuilder text = new StringBuilder();

            for (EAN13Entity item : map.get(s)) {
                setInt.add(item.getColorId());
                text.append(item.getEancode())
                        .append("\t")
                        .append(item.getColorId())
                        .append("\t")
                        .append(item.getProductId())
                        .append("\t")
                        .append(DateUtils.getNormalDateFormat(item.getDate()))
                        .append("\t")
                        .append(item.getId())
                        .append("\n");
            }

            if (setInt.size() > 1) {
                list.add(text.toString());
            }
            setInt.clear();
        }

        try {
            FileUtils.writeLines(new File("D://ean-product.txt"), list);
        } catch (Exception e) {

        }
        System.out.println("Найдено дублей : " + setDoubling.size());
    }

    private void doDoublingByEANCodeService() {
        List<EAN13Entity> data = getEan13List();
        if (data == null) {
            return;
        }

        Set<String> setDoubling = new HashSet<>();
        Map<String, List<EAN13Entity>> map = new HashMap<>();

        for (EAN13Entity item : data) {
            if (item.getArticleId() < 1) {
                continue;
            }

/*            // ТОлько чулочка, у ее не заполнено поле рост
            if (item.getGrowth() > 0) {
                continue;
            }*/

            String key = item.getEancode();

            List<EAN13Entity> list = map.get(key);
            if (list == null) {
                list = new ArrayList<>();
                map.put(key, list);
            }

            list.add(item);
            if (list.size() > 1) {
                if (!item.getEancode().trim().equals("")) {
                    setDoubling.add(item.getEancode());
                }
            }


        }

        List<String> list = new ArrayList<>();
        //Set<Integer> setInt = new HashSet<>();

        for (String s : setDoubling) {
            StringBuilder text = new StringBuilder();

            for (EAN13Entity item : map.get(s)) {
                text.append(item.getEancode())
                        .append("\t")
                        .append(item.getColorId())
                        .append("\t")
                        .append(item.getProductId())
                        .append("\t")
                        .append(item.getArticleId())
                        .append("\t")
                        .append(item.getArticleNumber())
                        .append("\t")
                        .append(item.getGrade())
                        .append("\t")
                        .append(item.getSize())
                        .append("\t")
                        .append(item.getGrowth())
                        .append("\t")
                        .append(DateUtils.getNormalDateFormat(item.getDate()))
                        .append("\t")
                        .append(item.getId())
                        .append("\n");
            }

            list.add(text.toString());
        }

        try {
            FileUtils.writeLines(new File("D://ean-ean.txt"), list);
        } catch (Exception ignored) {

        }
        System.out.println("Найдено дублей : " + setDoubling.size());
    }

    private List<EAN13Entity> getEan13List() {
        if (data == null) {
            MaintenanceJDBC db = new MaintenanceJDBC();
            data = db.getEan13List();
        }
        return data;
    }
}
