package by.march8.ecs.application.modules.art.dbf;

import by.gomel.freedev.ucframework.uccore.utils.SystemUtils;
import by.march8.ecs.MainController;
import by.march8.ecs.application.modules.art.model.DosNsiItem;
import by.march8.ecs.application.modules.art.model.ProductionReportData;
import by.march8.ecs.application.modules.art.model.ProductionReportItem;
import by.march8.ecs.application.modules.art.model.ProductionReportTotal;
import com.linuxense.javadbf.DBFReader;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author Andy 21.01.2017.
 */
@SuppressWarnings("all")
public class ProductionReportDBF {

    private static final String DBF_ZAKR = "ZAKR/zakr";
    private static final String DBF_ZAOS = "ZAKROST/zaos";
    private static final String DBF_OSTV = "ZAKROST/ostv";
    private static final String DBF_PR = "DVI/PR/pr_";
    private static final String DBF_NSI = "DVI/PR/nsi_kld.dbf";

    private static final String DBF_PATH_WINDOWS = "//ser01/D$/";
    private static final String DBF_PATH_UNIX = "/nfs/ser01/";

    private HashMap<String, ProductionReportItem> data = new HashMap<>();

    public boolean check() {
        if (!getDBFPath().equals("")) {
            return true;
        } else {
            return false;
        }
    }


    public HashMap<String, ProductionReportItem> getData() {
        return data;
    }

    public String getEnvironmentFiles() {
        String defaultPath;
        if (SystemUtils.isWindows()) {
            defaultPath = DBF_PATH_WINDOWS;
        } else {
            defaultPath = DBF_PATH_UNIX;
        }
        return DBF_ZAKR + "XX.dbf\n" + DBF_ZAOS + "XX.dbf\n" +
                DBF_PR + "XXd.dbf\n" + DBF_NSI + "\n" + "Путь по-умолчанию [" + defaultPath + "] не доступен. \nУкажите путь к файлам в параметре [DBF_PATH] конфигурационного файла программы.";
    }

    /**
     * TODO:Вернет НЗП и коэфиценты.
     */
    public ProductionReportData getNZPItemList(int monthId) {
        // Открыть файлы DBF
        String monthIdString = "0";
        if (monthId > 9) {
            monthIdString = String.valueOf(monthId);
        } else {
            monthIdString += String.valueOf(monthId);
        }
        String fileNameZaos = DBF_ZAOS + monthIdString + ".dbf";
        String fileNameOstv = "ZAKROST/ostv" + monthIdString + ".dbf";
        String fileNameNZP = "ZAKROST/nzp" + monthIdString + ".dbf";

        // Очистка карты изделий
        data.clear();

        // Получение статистики по остаткам
        getDataFromZAOSDBF(data, getDBFPath(), fileNameZaos);
        getDataFromOSTVDBF(data, getDBFPath(), fileNameOstv);
        return null;

    }

    /**
     * Возвращает коллекцию записей о продукции для отдела УМиРА
     *
     * @param monthId номер месяца
     * @return коллекция записей
     */
    public ProductionReportData getProductReportItemList(int monthId) {
        // Открыть файл DBF
        String monthIdString = "0";
        if (monthId > 9) {
            monthIdString = String.valueOf(monthId);
        } else {
            monthIdString += String.valueOf(monthId);
        }
        String fileNameZakr = DBF_ZAKR + monthIdString + ".dbf";
        String fileNameZaos = DBF_ZAOS + monthIdString + ".dbf";
        String fileNamePr = DBF_PR + monthIdString + "d.dbf";

        // Очистка карты изделий
        data.clear();
        // Загрузка карты из файла закроя (ZAKRXXX.DBF)


        // Получение статистики по закрою
        getDataFromZAKRDBF(data, getDBFPath(), fileNameZakr);

        // Получение статистики по остаткам
        getDataFromZAOSDBF(data, getDBFPath(), fileNameZaos);

        // Получение статистики по приходу на склад
        getDataFromPRDBF(data, getDBFPath(), fileNamePr);

        List<ProductionReportItem> result = new ArrayList<>();

        Iterator it = data.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            ProductionReportItem item = (ProductionReportItem) pair.getValue();
            result.add(item);
            it.remove();
        }

        // Заполнение Данными NSI
        getDataFromNSIDBF(result, getDBFPath(), DBF_NSI);

        // СОртировка списка по шифру артикула
        Collections.sort(result, new Comparator<ProductionReportItem>() {
            @Override
            public int compare(ProductionReportItem item2, ProductionReportItem item1) {
                return item2.getArticleCode().compareTo(item1.getArticleCode());
            }
        });

        ProductionReportTotal total = new ProductionReportTotal();
        for (ProductionReportItem item : result) {
            // Расчет колонки 8 ( 3 + 4 + 5 - 6 - 7)
            item.setRemainsEndPeriod(
                    item.getRemainsBeginPeriod()
                            + item.getInComingCuttingDept()
                            //+ item.getInComing708Dept()
                            - item.getOutComingWarehouse()
                            - item.getOutComingCuttingDept());
            // Колонка 3
            total.setRemainsBeginPeriod(total.getRemainsBeginPeriod() + item.getRemainsBeginPeriod());
            // Колонка 4
            total.setInComingCuttingDept(total.getInComingCuttingDept() + item.getInComingCuttingDept());
            // Колонка 5
            //total.setInComing708Dept(total.getInComing708Dept() + item.getInComing708Dept());
            // Колонка 6
            total.setOutComingWarehouse(total.getOutComingWarehouse() + item.getOutComingWarehouse());
            // Колонка 7
            total.setOutComingCuttingDept(total.getOutComingCuttingDept() + item.getOutComingCuttingDept());
            // Колонка 8
            total.setRemainsEndPeriod(total.getRemainsEndPeriod() + item.getRemainsEndPeriod());
        }


        ProductionReportData reportData = new ProductionReportData();
        reportData.setData(result);
        reportData.setTotal(total);

        return reportData;
    }

    private String getDBFPath() {
        if (SystemUtils.isWindows()) {

            File f = new File(DBF_PATH_WINDOWS);
            if (f.exists()) {
                return DBF_PATH_WINDOWS;
            } else {
                String newDBFPath = MainController.getConfiguration().getProperty("DBF_PATH");
                System.out.println("newDBFPath");
                f = new File(newDBFPath);
                if (f.exists()) {
                    return newDBFPath;
                } else {
                    return "";
                }
            }
        } else {
            File f = new File(DBF_PATH_UNIX);
            if (f.exists()) {
                return DBF_PATH_UNIX;
            } else {
                String newDBFPath = MainController.getConfiguration().getProperty("DBF_PATH");
                f = new File(newDBFPath);
                if (f.exists()) {
                    return newDBFPath;
                } else {
                    return "";
                }
            }

        }
    }

    private String getStringFromFloatString(String value) {
        return String.valueOf(Double.valueOf(value).intValue());
    }

    private HashMap<String, ProductionReportItem> getDataFromZAKRDBF(HashMap<String, ProductionReportItem> map, String path, String fileName) {

        InputStream inputStream = null;

        try {
            inputStream = new FileInputStream(path + fileName);
            DBFReader reader = new DBFReader(inputStream);
            reader.setCharactersetName("cp866");


            int columnCount = reader.getFieldCount();
            if (columnCount != 14) {
                System.out.println("Не верный формат файла");
            } else {
                Object[] dbfRow = null;
                System.out.println("Всего записей в файле [" + fileName + "]: " + reader.getRecordCount());
                while ((dbfRow = reader.nextRecord()) != null) {
                    // К обработке принимаем данные по определеннным критериям
                    // Поле OTPR только 923
                    // Поле POL только 724
                    try {
                        int tokenOTPR = (int) Math.round(Float.valueOf(dbfRow[0].toString()));
                        int tokenPOL = (int) Math.round(Float.valueOf(dbfRow[3].toString()));

/*                        // Колонка 5 ПРИХОД ИЗДЕЛИЙ ИЗ ЦЕХА 708
                        if (tokenPOL == 724 && tokenOTPR == 708) {
                            // В качестве ключа принимаем шифр артикула изделия
                            String articleCode = getStringFromFloatString(dbfRow[5].toString());
                            ProductionReportItem item = map.get(articleCode);

                            // Изделие в карте отсутствует, создаем новое
                            if (item == null) {
                                item = new ProductionReportItem();
                                item.setCategory(articleCode);

                                int[] oldDate = getDateOldFormat(dbfRow[1].toString());
                                item.setDayNumber(oldDate[0]);
                                item.setMonthNumber(oldDate[1]);

                                item.setModelNumber(getStringWithoutPoint(dbfRow[7].toString()));
                                item.setInComing708Dept((int) Math.round(Float.valueOf(dbfRow[12].toString())));
                                map.put(articleCode, item);
                            } else {
                                item.setInComing708Dept(item.getInComing708Dept() + ((int) Math.round(Float.valueOf(dbfRow[12].toString()))));
                            }

                        }*/

                        //***********************************************************
                        // Колонка 4 ПРИХОД ИЗДЕЛИЙ ИЗ ЦЕХА
                        if (tokenPOL == 724 && tokenOTPR == 923) {
                            // В качестве ключа принимаем шифр артикула изделия
                            String articleCode = getStringFromFloatString(dbfRow[5].toString());
                            ProductionReportItem item = map.get(articleCode);
                            // Изделие в картеотсутствует, создаем новое
                            if (item == null) {

                                item = new ProductionReportItem();
                                item.setArticleCode(articleCode);

                                int[] oldDate = getDateOldFormat(dbfRow[1].toString());
                                item.setDayNumber(oldDate[0]);
                                item.setMonthNumber(oldDate[1]);

                                item.setModelNumber(getStringWithoutPoint(dbfRow[7].toString()));
                                item.setInComingCuttingDept((int) Math.round(Float.valueOf(dbfRow[12].toString())));
                                map.put(articleCode, item);
                            } else {
                                item.setInComingCuttingDept(item.getInComingCuttingDept() + ((int) Math.round(Float.valueOf(dbfRow[12].toString()))));
                            }
                        }

                        //***********************************************************
                        // Колонка 7 СДАНО В ЦЕХ
                        if (tokenPOL == 708 && tokenOTPR == 724) {
                            // В качестве ключа принимаем шифр артикула изделия
                            String articleCode = getStringFromFloatString(dbfRow[5].toString());
                            ProductionReportItem item = map.get(articleCode);
                            // Изделие в картеотсутствует, создаем новое
                            if (item == null) {
                                item = new ProductionReportItem();
                                item.setArticleCode(articleCode);

                                int[] oldDate = getDateOldFormat(dbfRow[1].toString());
                                item.setDayNumber(oldDate[0]);
                                item.setMonthNumber(oldDate[1]);

                                item.setModelNumber(getStringWithoutPoint(dbfRow[7].toString()));
                                item.setOutComingCuttingDept((int) Math.round(Float.valueOf(dbfRow[12].toString())));
                                map.put(articleCode, item);
                            } else {
                                item.setOutComingCuttingDept(item.getOutComingCuttingDept() + ((int) Math.round(Float.valueOf(dbfRow[12].toString()))));
                            }
                        }

                    } catch (Exception ex) {
                        System.err.println("Ошибка преобразования типа в DBF: " + fileName);
                        ex.printStackTrace();
                        break;
                    }
                }
            }

            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return map;
    }

    private HashMap<String, ProductionReportItem> getDataFromOSTVDBF(HashMap<String, ProductionReportItem> map, String path, String fileName) {

        InputStream inputStream = null;

        try {
            inputStream = new FileInputStream(path + fileName);
            DBFReader reader = new DBFReader(inputStream);
            reader.setCharactersetName("cp866");

            int columnCount = reader.getFieldCount();
            if (columnCount != 6) {
                System.out.println("Не верный формат файла");
            } else {
                Object[] dbfRow = null;
                System.out.println("Всего записей в файле [" + fileName + "]: " + reader.getRecordCount());
                while ((dbfRow = reader.nextRecord()) != null) {
                    // К обработке принимаем данные по определеннным критериям
                    // Поле SHBR только 724
                    try {
                        //int tokenSHBR = Integer.valueOf(getStringWithoutZero(dbfRow[0].toString()));

                        // В качестве ключа принимаем шифр артикула изделия
                        String articleCode = dbfRow[3].toString();
                        ProductionReportItem item = map.get(articleCode);

                        // Изделие в карте отсутствует, создаем новое
                        if (item == null) {
                            item = new ProductionReportItem();
                            item.setArticleCode(articleCode);

                            /** TODO: Что сюда передовать, числа то нет.*/
                            item.setDayNumber(Integer.valueOf(getStringWithoutZero("01")));
                            item.setMonthNumber(Integer.valueOf(getStringWithoutZero(dbfRow[2].toString())));

                            item.setModelNumber(getStringWithoutZero(dbfRow[4].toString().trim()));

                            item.setRemainsBeginPeriod((int) Math.round(Float.valueOf(dbfRow[5].toString().trim())));
                            map.put(articleCode, item);
                        } else {
                            item.setRemainsBeginPeriod(item.getRemainsBeginPeriod() + ((int) Math.round(Float.valueOf(dbfRow[5].toString()))));
                        }

                    } catch (Exception ex) {
                        System.err.println("Ошибка преобразования типа в DBF: " + fileName);
                        ex.printStackTrace();
                        break;
                    }
                }
            }

            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return map;
    }

    private HashMap<String, ProductionReportItem> getDataFromZAOSDBF(HashMap<String, ProductionReportItem> map, String path, String fileName) {

        InputStream inputStream = null;

        try {
            inputStream = new FileInputStream(path + fileName);
            DBFReader reader = new DBFReader(inputStream);
            reader.setCharactersetName("cp866");

            int columnCount = reader.getFieldCount();
            if (columnCount != 10) {
                System.out.println("Не верный формат файла");
            } else {
                Object[] dbfRow = null;
                System.out.println("Всего записей в файле [" + fileName + "]: " + reader.getRecordCount());
                while ((dbfRow = reader.nextRecord()) != null) {
                    // К обработке принимаем данные по определеннным критериям
                    // Поле SHBR только 724
                    try {
                        int tokenSHBR = Integer.valueOf(getStringWithoutZero(dbfRow[0].toString()));

                        // Колонка 3 ОСТАТОК НА НАЧАЛО ПЕРИОДА
                        if (tokenSHBR == 724) {
                            // В качестве ключа принимаем шифр артикула изделия
                            String articleCode = dbfRow[7].toString();
                            ProductionReportItem item = map.get(articleCode);

                            // Изделие в карте отсутствует, создаем новое
                            if (item == null) {
                                item = new ProductionReportItem();
                                item.setArticleCode(articleCode);

                                item.setDayNumber(Integer.valueOf(getStringWithoutZero(dbfRow[1].toString())));
                                item.setMonthNumber(Integer.valueOf(getStringWithoutZero(dbfRow[2].toString())));

                                item.setModelNumber(getStringWithoutZero(dbfRow[8].toString()));

                                item.setRemainsBeginPeriod((int) Math.round(Float.valueOf(dbfRow[9].toString())));
                                map.put(articleCode, item);
                            } else {
                                item.setRemainsBeginPeriod(item.getRemainsBeginPeriod() + ((int) Math.round(Float.valueOf(dbfRow[9].toString()))));
                            }

                        }

                    } catch (Exception ex) {
                        System.err.println("Ошибка преобразования типа в DBF: " + fileName);
                        ex.printStackTrace();
                        break;
                    }
                }
            }

            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return map;
    }

    private HashMap<String, ProductionReportItem> getDataFromPRDBF(HashMap<String, ProductionReportItem> map, String path, String fileName) {

        InputStream inputStream = null;

        try {
            inputStream = new FileInputStream(path + fileName);
            DBFReader reader = new DBFReader(inputStream);
            reader.setCharactersetName("cp866");

            int columnCount = reader.getFieldCount();
            if (columnCount != 32) {
                System.out.println("Не верный формат файла");
            } else {
                Object[] dbfRow = null;
                System.out.println("Всего записей в файле [" + fileName + "]: " + reader.getRecordCount());
                while ((dbfRow = reader.nextRecord()) != null) {
                    // К обработке принимаем данные по определеннным критериям
                    // Поле OTPP только 724
                    try {
                        int tokenOTPP = 0;
                        //System.err.println("/"+dbfRow[9]+"/");
                        if (dbfRow[9] != null) {
                            tokenOTPP = (int) Math.round(Float.valueOf(dbfRow[9].toString()));
                        }

                        int tokenPOLNF = 0;

                        if (dbfRow[11] != null) {
                            tokenPOLNF = (int) Math.round(Float.valueOf(dbfRow[11].toString()));
                        }

/*                        if(tokenOTPP==724){
                            System.out.println(tokenOTPP+" / "+tokenPOLNF);
                        }*/
                        // Колонка 6 ПРИХОД ИЗДЕЛИЙ ИЗ ЦЕХА 708
                        if (tokenOTPP == 724 && tokenPOLNF == 737) {
                            if (dbfRow[15] != null) {
                                // В качестве ключа принимаем шифр артикула изделия
                                String articleCode = getStringFromFloatString(dbfRow[15].toString());
                                ProductionReportItem item = map.get(articleCode);

                                // Изделие в карте отсутствует, создаем новое
                                if (item == null) {
                                    item = new ProductionReportItem();
                                    item.setArticleCode(articleCode);

                                    int[] oldDate = getDateOldFormat(dbfRow[0].toString());
                                    item.setDayNumber(oldDate[0]);
                                    item.setMonthNumber(oldDate[1]);

                                    item.setModelNumber(getStringWithoutPoint(dbfRow[17].toString()));
                                    item.setOutComingWarehouse((int) Math.round(Float.valueOf(dbfRow[22].toString())));
                                    map.put(articleCode, item);
                                } else {
                                    item.setOutComingWarehouse(item.getOutComingWarehouse() + ((int) Math.round(Float.valueOf(dbfRow[22].toString()))));
                                }
                            }

                        }

                    } catch (Exception ex) {
                        System.err.println("Ошибка преобразования типа в DBF: " + fileName);
                        ex.printStackTrace();
                        break;
                    }
                }
            }

            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return map;
    }

    /**
     * Читаем классификатор и заполняем мапу
     *
     * @param list     мапа изделий
     * @param path     путь к DBF файлу
     * @param fileName имя DBF файла
     * @return мапа после обработки
     */
    private List<ProductionReportItem> getDataFromNSIDBF(List<ProductionReportItem> list, String path, String fileName) {

        HashMap<String, DosNsiItem> nsiMap = new HashMap<>();
        //List<DosNsiItem> listNsi = new ArrayList<>();
        InputStream inputStream = null;

        try {
            inputStream = new FileInputStream(path + fileName);
            DBFReader reader = new DBFReader(inputStream);
            reader.setCharactersetName("cp866");

            int columnCount = reader.getFieldCount();
            if (columnCount != 9) {
                System.out.println("Не верный формат файла");
            } else {
                Object[] dbfRow = null;
                System.out.println("Всего записей в файле [" + fileName + "]: " + reader.getRecordCount());
                //System.out.println(reader.getField(3).getName() + " - " + reader.getField(3).getDataType()+","+reader.getField(3).getFieldLength()+","+reader.getField(3).getDecimalCount());
                while ((dbfRow = reader.nextRecord()) != null) {

                    try {
                        String articleCode = getStringFromFloatString(dbfRow[3].toString());
                        String prefix = articleCode.substring(0, 2);
                        if (prefix.equals("41") || prefix.equals("42") || prefix.equals("43")) {
                            DosNsiItem newItem = new DosNsiItem();

//System.out.println(Double.valueOf(dbfRow[3].toString()).intValue()+" - "+getStringFromFloatString(dbfRow[3].toString())+" - "+ Double.valueOf(dbfRow[3].toString()));
                            newItem.setArticleCode(articleCode);
                            newItem.setName(dbfRow[0].toString().trim());
                            newItem.setArticleName(dbfRow[1].toString().trim());
                            nsiMap.put(articleCode, newItem);
                        }
                    } catch (Exception ex) {
                        System.err.println("Ошибка преобразования типа в DBF: " + fileName);
                        ex.printStackTrace();
                        break;
                    }
                }
            }

            System.out.println("В МАПЕ ДЛЯ ОБРАБОТКИ ВСЕГО " + nsiMap.size());

            // Через итератор пробежимся по мапе изделий и заполним недостающими данными из НСИ
            // Пробегаемся по мапе и заполняем новый список из мапы
            int find = 0;
            for (ProductionReportItem itm : list) {
                if (itm != null) {
                    DosNsiItem nsiItem = nsiMap.get(itm.getArticleCode());
                    if (nsiItem != null) {
                        itm.setItemName(nsiItem.getName());
                        itm.setArticleName(nsiItem.getArticleName());
                        find++;
                    }
                }
            }

            inputStream.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * Метод удаляет превые символы "0"  в строке
     *
     * @param value исходная строка
     * @return итоговая строка без стартовых нулей
     */
    private String getStringWithoutZero(String value) {
        int index = 0;
        while (value.substring(index, index + 1).equals("0")) {

            index++;
        }
        return value.substring(index, value.length());
    }

    /**
     * Метод принимает строку определенного формата ("302" или "1311")
     * и возвращает целочисленный массив из двух элементов,
     * в первой ячейке  номер дня (3 или 13)
     * во второй ячейке номер месяца (2 или 11)
     *
     * @param oldDate исходная строка даты
     * @return итоговый массив даты
     */
    private int[] getDateOldFormat(String oldDate) {
        int[] result = {0, 0};

        if (oldDate.length() > 3) {
            result[0] = Integer.valueOf(getStringWithoutZero(oldDate.substring(0, 2)));
            result[1] = Integer.valueOf(getStringWithoutZero(oldDate.substring(2, 4).trim()));
        } else {
            result[0] = Integer.valueOf(getStringWithoutZero(oldDate.substring(0, 1)));
            result[1] = Integer.valueOf(getStringWithoutZero(oldDate.substring(1, 3).trim()));
        }
        return result;
    }

    /**
     * Метод возвращает строку без "хвоста" полсе точки
     *
     * @param value исходная строка
     * @return итоговая строка
     */
    private String getStringWithoutPoint(String value) {
        int pointIndex = value.indexOf(".");
        if (pointIndex >= 0) {
            return value.substring(0, pointIndex);
        } else {
            return value;
        }
    }

}
