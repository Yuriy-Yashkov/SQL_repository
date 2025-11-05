package common;

import dept.MyReportsModule;
import lombok.SneakyThrows;

import javax.swing.*;
import javax.swing.text.MaskFormatter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Properties;
import java.util.Vector;

public class UtilFunctions {

    /**
     * Создаёт иконку
     * @param path путь
     * @return ImageIcon иконка
     */
    public static ImageIcon createIcon(String path) {
        String imgURL = MyReportsModule.progPath + path;
        return new ImageIcon(imgURL);
    }

    public static String dateToFormatString(Date date) {
        SimpleDateFormat ft = new SimpleDateFormat("ddMMyyyy");
        return ft.format(date);
    }

    /**
     * Проверяет корректность даты
     * @param chDate -- дата в формате дд.мм.гггг
     * @return true/false
     */
    public static boolean checkDate(String chDate) throws Exception {
        Calendar cal = new GregorianCalendar();
        cal.setLenient(false);
        cal.clear();
        try {
            int d = Integer.parseInt(chDate.substring(0, 2));
            int m = Integer.parseInt(chDate.substring(3, 5));
            int y = Integer.parseInt(chDate.substring(6, 10));
            cal.set(y, m - 1, d);
            cal.getTime();
            return true;
        } catch (Exception e) {
            throw new Exception("Ошибка преобразования даты: " + chDate + " " + e.getMessage());
        }
    }

    /**
     * Преобразует строку в формате "дд.мм.гггг" в миллисекунды тип long
     * @param date строка в формате "дд.мм.гггг"
     * @return long количество миллисекунд
     * <br>

     */
    public static long convertDateStrToLong(String date) throws ParseException {
        Date d;
        DateFormat formatter = new java.text.SimpleDateFormat("dd.MM.yyyy");

        try {
            d = formatter.parse(date);
        } catch (ParseException e) {
            throw new ParseException("Ошибка преобразования даты в миллисекунды ", 0);
        }
        return d.getTime();
    }

    /**
     * Возвращает маску вывода даты
     * @return MaskFormatter формат дд.мм.гггг
     */
    public static MaskFormatter maskFormatterDate() {
        MaskFormatter mask = null;
        try {
            mask = new MaskFormatter("##.##.####");
            mask.setPlaceholderCharacter('0');
        } catch (Exception e) {
            System.out.println("Ошибка: " + e);
        }
        return mask;
    }

    /**
     * Чтение файла настроек
     * @throws Exception
     */
    public static int readPropFile(String setting) throws Exception {
        Properties prop = new Properties();
        int item = -1;
        try {
            if (MyReportsModule.confDesignPath.exists()) {
                prop.load(new FileInputStream(MyReportsModule.confDesignPath));
                if (prop.getProperty(setting) != null)
                    item = Integer.parseInt(prop.getProperty(setting));
            }
        } catch (Exception e) {
            throw new Exception("Ошибка чтения файла настроек! " + e.getMessage());
        }
        return item;
    }

    /**
     * Чтение файла настроек
     * @throws Exception
     */
    public static String readPropFileString(String setting) throws Exception {
        Properties prop = new Properties();
        String item = "";
        try {
            if (MyReportsModule.confDesignPath.exists()) {
                prop.load(new FileInputStream(MyReportsModule.confDesignPath));
                if (prop.getProperty(setting) != null)
                    item = prop.getProperty(setting);
            }
        } catch (Exception e) {

            throw new Exception("Ошибка чтения файла настроек! " + e.getMessage());
        }
        return item;
    }

    /**
     * Чтение файла настроек
     * @throws Exception
     */
    public static String[] readPropFileStringArray(String setting) throws Exception {
        Properties prop = new Properties();
        String[] arr = new String[]{};
        try {
            if (MyReportsModule.confDesignPath.exists()) {
                prop.load(new FileInputStream(MyReportsModule.confDesignPath));
                try {
                    if (prop.getProperty(setting) != null)
                        arr = prop.getProperty(setting).split(",");
                } catch (Exception e) {
                    arr = new String[]{};
                }
            }
        } catch (Exception e) {
            throw new Exception("Ошибка чтения файла настроек! " + e.getMessage());
        }
        return arr;
    }

    /**
     * Чтение файла настроек
     * @throws Exception
     */
    public static String[][] readPropFileStringsArray(String setting) throws Exception {
        Properties prop = new Properties();
        String[][] arr = new String[][]{};
        try {
            if (MyReportsModule.confDesignPath.exists()) {
                prop.load(new FileInputStream(MyReportsModule.confDesignPath));
                try {
                    if (prop.getProperty(setting) != null) {
                        String[] a = prop.getProperty(setting).split(";");
                        arr = new String[a.length][2];
                        for (int i = 0; i < a.length; i++)
                            arr[i] = a[i].split(",");
                    }
                } catch (Exception e) {
                    arr = new String[][]{};
                }
            }
        } catch (Exception e) {
            throw new Exception("Ошибка чтения файла настроек! " + e.getMessage());
        }
        return arr;
    }

    /**
     * Запись настроек в файл
     * @throws Exception
     */
    public static void setSettingPropFile(String rezaltPrint, String propName) throws Exception {
        Properties prop = new Properties();
        try {
            if (MyReportsModule.confDesignPath.exists()) {
                prop.load(new FileInputStream(MyReportsModule.confDesignPath));
                prop.setProperty(propName, rezaltPrint);
                prop.store(new FileOutputStream(MyReportsModule.confDesignPath), null);
            }
        } catch (Exception ex) {
            throw new Exception("Не удалось сохранить текущие настройки в файл! " + ex.getMessage());
        }
    }

    /**
     * Возвращает рабочие дни месяца
     * @param dd1 начало периода
     * @param dd2 конеч периода
     * @param mm месяц
     * @param yy год
     * @param k кол-во рабочих дней в неделю (кроме суббота и воскресенье)
     * @param saturday суббота
     * @param sunday воскресенье
     * @return рабочие дни месяца
     * @throws Exception
     */
    public static Vector getWorkingDays(int dd1, int dd2, int mm, int yy, int k, boolean saturday, boolean sunday) throws Exception {
        Vector period = new Vector();
        Vector holiday = new Vector(Arrays.asList("1.01", "7.01", "8.03", "1.05", "9.05", "3.07", "7.11", "25.12"));
        int a = 0, y = 0, m = 0, dd = 0;

        try {
            if (k > 1 && k < 7) {
                a = (14 - mm) / 12;
                y = yy - a;
                m = mm + 12 * a - 2;

                for (int i = dd1; i <= dd2; i++) {
                    dd = (7000 + (i + y + y / 4 - y / 100 + y / 400 + (31 * m) / 12)) % 7;

                    if (!holiday.contains(i + "." + (mm < 10 ? "0" + mm : mm))) {
                        if ((dd > 0 && dd <= k) || (saturday && dd == 6) || (sunday && dd == 0))
                            period.add(i);
                    }
                }
            } else
                throw new Exception("\nНекорректное значение: " + k + " рабочих дня в неделю!");
        } catch (Exception e) {
            period = new Vector();
            throw new Exception("Не удалось получить рабочие дни месяца! " + e.getMessage());
        }
        return period;
    }

    /**
     * Возвращает запись из модели данных по индексу
     * @throws Exception
     */
    public static Item getItemsModel(Vector model, int index) throws Exception {
        Item item = null;
        try {
            for (final Object aModel : model) {
                Item object = (Item) aModel;
                if (object.getId() == index) {
                    item = object;
                    break;
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new Exception("Ошибка: " + e.getMessage(), e);
        }
        return item;
    }

    /**
     * Возвращает индекс из модели данных по записи
     * @throws Exception
     */
    public static int getIndexModel(Vector model, String item) throws Exception {
        int index = -1;
        try {
            for (final Object aModel : model) {
                Item object = (Item) aModel;
                if (object.getDescription().equals(item)) {
                    index = object.getId();
                    break;
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new Exception("Ошибка: " + e.getMessage(), e);
        }
        return index;
    }

    /**
     * Заполняет модель данных 
     * @param model - модель
     * @param data - данные 
     * @throws Exception
     */
    public static void fullModel(Vector model, Vector data) throws Exception {
        try {
            model.add(new Item(-1, "Все...", ""));
            for (int j = 0; j < data.size(); j++)
                model.add(new Item(Integer.parseInt(((Vector) data.elementAt(j)).get(0).toString()), ((Vector) data.elementAt(j)).get(1).toString(), ""));

        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new Exception("Ошибка: " + e.getMessage(), e);
        }
    }

    /**
     * Возвращает маску для округления до заданного знака
     */
    public static String getFormatRoundingNorm(int rounding) {
        String format = "0.0000";

        if (rounding > 0) {
            format = "0.0";
            for (int i = 1; i < rounding; i++)
                format += "0";
        }
        return format;
    }

    /**
     * Округляет до заданного знака
     */
    public static String formatNorm(double value, int rounding) {
        DecimalFormatSymbols s = new DecimalFormatSymbols();
        s.setDecimalSeparator('.');
        return (new DecimalFormat(getFormatRoundingNorm(rounding), s).format(BigDecimal.valueOf(value).setScale(rounding, RoundingMode.HALF_UP).doubleValue()));
    }

    public static boolean isWindows() {
        String os = System.getProperty("os.name").toLowerCase();
        return os.contains("win");
    }

    public static String getCatalogImageDir() {
        if (isWindows()) {
            return "//file-server/catalog";
        } else {
            return "/nfs/Programs/MyReports/catalog";
        }
    }
}
