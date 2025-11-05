package dbf;

import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Andy 27.04.2018 - 9:23.
 */
public class TestArraySorting {

    private List<ModelItem> sourceList;


    private List<ModelItem> prepareList() {
        sourceList = new ArrayList<>();
        sourceList.add(new ModelItem("Трусы", 2, 10));
        sourceList.add(new ModelItem("Майка", 2, 11));
        sourceList.add(new ModelItem("Рубашка", 2, 10));
        sourceList.add(new ModelItem("Носки", 2, 10));
        sourceList.add(new ModelItem("Носки", 2, 11));
        sourceList.add(new ModelItem("Майка", 1, 19));
        sourceList.add(new ModelItem("Штаны", 2, 10));
        sourceList.add(new ModelItem("Рубашка", 1, 11));
        sourceList.add(new ModelItem("Трусы", 1, 12));
        sourceList.add(new ModelItem("Майка", 1, 10));
        sourceList.add(new ModelItem("Рубашка", 2, 10));
        sourceList.add(new ModelItem("Трусы", 1, 10));
        sourceList.add(new ModelItem("Носки", 1, 10));
        sourceList.add(new ModelItem("Трусы", 1, 9));
        sourceList.add(new ModelItem("Штаны", 2, 10));
        sourceList.add(new ModelItem("Майка", 1, 15));

        int c = 0;
        for (ModelItem item : sourceList) {
            item.setId(c);
            c++;
        }

        return sourceList;
    }

    @Test
    public void testMap() {
        HashMap<String, Integer> map = new HashMap<>();
        System.out.println(map.put("KEY", 2));
        Integer o = map.get("KEY");
        if (o == null) {
            o = 2;
        } else {
            o += 4;
        }
        System.out.println(map.put("KEY", o));
        System.out.println(map.get("KEY"));
    }

    @Test
    public void testManyLevelSorting() {
        List<ModelItem> list = prepareList();
        HashMap<Integer, ModelItem> workMap = new HashMap<>();
        // Подготовка мапы
        for (ModelItem item : list) {
            workMap.put(item.getId(), item);
        }

        Comparator<ModelItem> comparator = Comparator.comparing(person -> person.getName());
        comparator = comparator.thenComparing(Comparator.comparing(person -> person.getGrade()));
        comparator = comparator.thenComparing(Comparator.comparing(person -> person.getSize()));

        // Sort the stream:
        Stream<ModelItem> personStream = list.stream().sorted(comparator);

        // Make sure that the output is as expected:
        List<ModelItem> sortedPeople = personStream.collect(Collectors.toList());

        for (ModelItem item : sortedPeople) {
            System.out.println(item.toString());
        }

 /*
        //list.sort((a, b) -> a.getName().compareTo(b.getName()));
        int iterationCount = 1;

        //
        List<TreeSet<Object>> treeList = new ArrayList<>();

        for (int i = 0; i < iterationCount; i++) {
            treeList.add(new TreeSet<>());
        }


        for (ModelItem item : list) {
            System.out.println(item.toString());
        }

        System.out.println("**********************************************");

        TreeSet<String> tree = new TreeSet<>();
        HashMap<String, ModelItem> map = new HashMap<>();

        for (ModelItem item : list) {
            System.out.println(item.getName() + ":" + tree.add(item.getName() + item.getGrade()));
            System.out.println(map.put(item.getName(), item) + ": Уже есть");
        }

        for (String s : tree) {
            System.out.println("Индексы ИМЯ СОРТ: " + s);
        }
/*

        Iterator it = map.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            System.out.println("0 Iteration:\t" + pair.getValue());
            //it.remove();
        }

        list.sort((a, b) ->
                Integer.compare(a.getGrade(), (b.getGrade())));

        for (ModelItem item : list) {
            System.out.println(item.toString());
        }


        System.out.println("**********************************************");
        it = map.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            System.out.println("+1 Iteration:\t" + pair.getValue());
            //it.remove();
        }*/
    }

    @Test
    public void testOneLevelSorting() {
        List<ModelItem> list = prepareList();

        //list.sort((a, b) -> a.getName().compareTo(b.getName()));
        int iterationCount = 1;

        //
        List<TreeSet<Object>> treeList = new ArrayList<>();

        for (int i = 0; i < iterationCount; i++) {
            treeList.add(new TreeSet<>());
        }


        for (ModelItem item : list) {
            System.out.println(item.toString());
        }

        System.out.println("**********************************************");

        TreeSet<String> tree = new TreeSet<>();
        HashMap<String, ModelItem> map = new HashMap<>();

        for (ModelItem item : list) {
            System.out.println(item.getName() + ":" + tree.add(item.getName() + item.getGrade()));
            System.out.println(map.put(item.getName(), item) + ": Уже есть");
        }

        for (String s : tree) {
            System.out.println("Индексы ИМЯ СОРТ: " + s);
        }


        Iterator it = map.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            System.out.println("0 Iteration:\t" + pair.getValue());
            //it.remove();
        }

        list.sort((a, b) ->
                Integer.compare(a.getGrade(), (b.getGrade())));

        for (ModelItem item : list) {
            System.out.println(item.toString());
        }


        System.out.println("**********************************************");
        it = map.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            System.out.println("+1 Iteration:\t" + pair.getValue());
            //it.remove();
        }
    }

    @Test
    public void testModelNumberParser() {
        System.out.println(parseModelNumberTrimLeft("\\\\file-server\\catalog\\resize\\СУВЕНИРКА\\МУЖСКОЙ АССОРТИМЕНТ\\26960_джемпер.jpg"));
    }

    private String parseModelNumberTrimLeft(String inputString) {
        //\\file-server\catalog\resize\СУВЕНИРКА\МУЖСКОЙ АССОРТИМЕНТ\26960_джемпер.jpg
        for (int i = inputString.length() - 1; i > 0; i--) {
            char s = inputString.charAt(i);
            if (s == File.separatorChar) {
                return parseModelNumberRight(inputString.substring(i + 1));
            }
        }
        return null;
    }

    private String parseModelNumberRight(String inputString) {
        //\\file-server\catalog\resize\СУВЕНИРКА\МУЖСКОЙ АССОРТИМЕНТ\26960_джемпер.jpg
        for (int i = 0; i < inputString.length() - 1; i++) {
            char s = inputString.charAt(i);
            if (s > '9' || s < '0') {
                return inputString.substring(0, i);
            }
        }
        return null;
    }


}
