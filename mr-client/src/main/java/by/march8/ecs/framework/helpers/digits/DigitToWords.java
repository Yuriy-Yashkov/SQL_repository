package by.march8.ecs.framework.helpers.digits;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Класс для работы с деньгами
 *
 * @author runcore
 */

public class DigitToWords {
    private static final String[][] kind = {
            {"", "один", "два", "три", "четыре", "пять", "шесть", "семь", "восемь", "девять"},
            {"", "одна", "две", "три", "четыре", "пять", "шесть", "семь", "восемь", "девять"},
    };
    private static final String[] numberX00 = {"", "сто", "двести", "триста", "четыреста", "пятьсот", "шестьсот", "семьсот", "восемьсот", "девятьсот"};
    private static final String[] number1X = {"", "десять", "одиннадцать", "двенадцать", "тринадцать", "четырнадцать", "пятнадцать", "шестнадцать", "семнадцать", "восемнадцать", "девятнадцать", "двадцать"};
    private static final String[] numberX = {"", "десять", "двадцать", "тридцать", "сорок", "пятьдесят", "шестьдесят", "семьдесят", "восемьдесят", "девяносто"};
    private static final String[][] common = {
            {"тысяча", "тысячи", "тысяч", "1"},
            {"миллион", "миллиона", "миллионов", "0"},
            {"миллиард", "миллиарда", "миллиардов", "0"},
            {"триллион", "триллиона", "триллионов", "0"}};


    /**
     * Обрабатываемое значение
     */
    private BigDecimal amount;
    /**
     * Наименование валюты
     */
    private CurrencyType typeMoney;

    /**
     * Единица измерения
     */
    private MeasureUnit measureUnit;

    /**
     * Если число отрицательное
     */
    private boolean negativeNumber;

    /**
     * Конструктор из Double
     */
    public DigitToWords(double value, CurrencyType typeMoney) {
        if (typeMoney == CurrencyType.BYR) {
            measureUnit = MeasureUnit.INTEGER;
        } else {
            measureUnit = MeasureUnit.FLOAT;
        }
        this.typeMoney = typeMoney;
        if (value < 0) {
            negativeNumber = true;
            value = value * -1;
        } else {
            negativeNumber = false;
        }
        initValue(value);
    }

    public DigitToWords(double value, MeasureUnit measureUnit) {
        this.measureUnit = measureUnit;
        this.typeMoney = null;
        initValue(value);
    }

    private void initValue(double value) {
        amount = new BigDecimal(value);
    }

    /**
     * Вернуть сумму как строку
     */
    public String asString() {
        return amount.toString();
    }

    /**
     * Вернуть сумму прописью, с точностью до копеек
     */
    public String num2str() {
        return num2str(false);
    }

    /**
     * Выводим сумму прописью
     *
     * @param stripkop boolean флаг - показывать копейки или нет
     * @return String Сумма прописью
     */
    public String num2str(boolean stripkop) {

        String[][] forms = {
                {"копейка", "копейки", "копеек", "1"},
                {"рубль", "рубля", "рублей", "0"},
                {"тысяча", "тысячи", "тысяч", "1"},
                {"миллион", "миллиона", "миллионов", "0"},
                {"миллиард", "миллиарда", "миллиардов", "0"},
                {"триллион", "триллиона", "триллионов", "0"},
                // можно добавлять дальше секстиллионы и т.д.
        };

        if (typeMoney != null) {
            switch (typeMoney) {
                case BYR:
                    forms = new String[][]{
                            {"копейка", "копейки", "копеек", "1"},
                            {"рубль", "рубля", "рублей", "0"},
                            // можно добавлять дальше секстиллионы и т.д.
                    };
                    break;
                case BYN:
                    forms = new String[][]{
                            {"копейка", "копейки", "копеек", "1"},
                            {"рубль", "рубля", "рублей", "0"},
                            // можно добавлять дальше секстиллионы и т.д.
                    };
                    break;
                case RUB:
                    forms = new String[][]{
                            {"копейка", "копейки", "копеек", "1"},
                            {"рубль", "рубля", "рублей", "0"},
                            // можно добавлять дальше секстиллионы и т.д.
                    };
                    break;
                case USD:
                    forms = new String[][]{
                            {"цент", "цента", "центов", "1"},
                            {"доллар", "доллара", "долларов", "0"},
                            // можно добавлять дальше секстиллионы и т.д.
                    };
                    break;
                case EUR:
                    forms = new String[][]{
                            {"цент", "цента", "центов", "1"},
                            {"евро", "евро", "евро", "0"},
                            // можно добавлять дальше секстиллионы и т.д.
                    };
                    break;
                case UAH:
                    forms = new String[][]{
                            {"копейка", "копейки", "копеек", "1"},
                            {"гривна", "гривны", "гривен", "0"},
                            // можно добавлять дальше секстиллионы и т.д.
                    };
                    break;
            }
        } else {
            switch (measureUnit) {
                case INTEGER:
                    forms = new String[][]{
                            {"", "", "", "1"},
                            {"", "", "", "0"},
                            // можно добавлять дальше секстиллионы и т.д.
                    };
                    break;
                case FLOAT:
                    break;
                case WEIGHT:
                    forms = new String[][]{
                            {"грамм", "грамма", "грамм", "1"},
                            {"килограмм", "килограмма", "килограмм", "0"},
                            // можно добавлять дальше секстиллионы и т.д.
                    };
                    break;
                case AMOUNT:
                    forms = new String[][]{
                            {"шт.", "шт.", "шт.", "1"},
                            {"шт.", "шт.", "шт.", "0"},
                            // можно добавлять дальше секстиллионы и т.д.
                    };
                    break;
            }
        }

        // Избавляемся от экспоненты в больших числах
        //NumberFormat f = NumberFormat.getInstance();
        //f.setGroupingUsed(false);

        DecimalFormat df = new DecimalFormat("#.0######");
        df.setDecimalSeparatorAlwaysShown(false);
        //System.out.println(df.format(amount.doubleValue()));


        String sdf = df.format(amount.doubleValue());
        int zeroIndex = sdf.indexOf(",");


        if (zeroIndex == 0) {
            sdf = "0" + sdf;
        }

        String[] parts = sdf.split(",");

        long integerPart = Long.parseLong(parts[0]);
        long decimalPart = Long.parseLong(parts[1]);

        String kops = parts[1];//String.valueOf(decimalPart);

        if (measureUnit == MeasureUnit.WEIGHT) {
            if (kops.length() == 1) {
                kops = kops + "00";
            }
            if (kops.length() == 2) {
                kops = kops + "0";
            }

            if (kops.substring(0, 1).equals("0")) {
                kops = kops.substring(1);
                decimalPart = Long.parseLong(kops);

            } else {
                decimalPart = Long.parseLong(kops);
            }
        } else {
            if (kops.length() == 1) {
                if (parts[1].substring(0, 1).equals("0")) {
                    kops = "0" + kops;
                } else {
                    kops = kops + "0";
                }
            }

            if (kops.length() > 2) {
                kops = kops.substring(0, 2);
                decimalPart = Long.parseLong(kops);
            }
        }


        // Парсинг числа до запятой
        String o = getWordsForDigit(integerPart, forms);

        if (integerPart == 0) {// если Ноль
            o = "ноль " + morph(0, forms[1][0], forms[1][1], forms[1][2]);
            if (stripkop) {
                return o;
            } else {
                return o + " " + decimalPart + " " + morph(decimalPart, forms[0][0], forms[0][1], forms[0][2]);
            }
        }

        if (typeMoney == CurrencyType.BYR) {
            stripkop = true;
        }

        if (measureUnit == MeasureUnit.AMOUNT) {
            stripkop = true;
        }

        if (measureUnit == MeasureUnit.INTEGER) {
            stripkop = true;
        }

        // Копейки в цифровом виде
        if (measureUnit == MeasureUnit.WEIGHT) {
            System.out.println(kops);
            //o = o + "" + kops + " " + morph(Long.valueOf(kops), forms[0][0], forms[0][1], forms[0][2]);
            o = o + "" + kops + " " + morph(Long.valueOf(kops), forms[0][0], forms[0][1], forms[0][2]);
            o = o.replaceAll(" {3,}", " ");
        } else {
            if (stripkop) {
                o = o.replaceAll(" {2,}", " ");
            } else {
                o = o + "" + kops + " " + morph(Long.valueOf(kops), forms[0][0], forms[0][1], forms[0][2]);
                o = o.replaceAll(" {2,}", " ");
            }
        }
        if (negativeNumber)
            o = "минус " + o;
        return o;

    }

    private String getWordsForDigit(long value, String[][] unit) {
        String result = "";

        // Парсинг значения на триады
        ArrayList<Long> triad = parseTriad(value);

        // Больше нуля
        int triadIndex = triad.size();
        for (Long aTriad : triad) {// перебираем сегменты

            // Индекс получения рода для морфологии
            int kindIndex;

            // Если индекс триады больше 1 (число больше тысячи), род единицы измерения берем из общего массива
            if (triadIndex > 1) {
                kindIndex = Integer.valueOf(common[triadIndex - 2][3]);// определяем род
                // Иначе единица измерения конкретная bp case
            } else {
                kindIndex = Integer.valueOf(unit[triadIndex][3]);// определяем род
            }

            int currentTriad = Integer.valueOf(aTriad.toString());// текущий сегмент

            if (currentTriad == 0 && triadIndex > 1) {
                triadIndex--;
                continue;
            }

            // доведение до триады
            String rs = String.valueOf(currentTriad); // число в строку
            while (rs.length() < 3) {
                rs = "0" + rs;
            }

            // получаем циферки для анализа
            int r1 = Integer.valueOf(rs.substring(0, 1)); //первая цифра
            int r2 = Integer.valueOf(rs.substring(1, 2)); //вторая
            int r3 = Integer.valueOf(rs.substring(2, 3)); //третья
            int r23 = Integer.valueOf(rs.substring(1, 3)); //вторая и третья

            // Супер-нано-анализатор циферок
            if (currentTriad > 99) result += numberX00[r1] + " "; // Сотни
            if (r23 > 20) {// >20
                result += numberX[r2] + " ";
                result += kind[kindIndex][r3] + " ";
            } else { // <=20
                if (r23 > 9) result += number1X[r23 - 9] + " "; // 10-20
                else result += kind[kindIndex][r3] + " "; // 0-9
            }

            if (triadIndex > 1) {
                result += morph(currentTriad, common[triadIndex - 2][0], common[triadIndex - 2][1], common[triadIndex - 2][2]) + " ";
            } else {
                result += morph(currentTriad, unit[triadIndex][0], unit[triadIndex][1], unit[triadIndex][2]) + " ";
            }
            triadIndex--;
        }
        return result;
    }

    /**
     * Разбор числа на триады
     *
     * @param value число
     * @return список триад
     */
    private ArrayList<Long> parseTriad(final long value) {
        long tempInteger = value;
        // Парсинг значения на триады
        ArrayList<Long> result = new ArrayList<Long>();
        // За триаду принимаем группу из 3-х символов
        while (tempInteger > 999) {
            // Повторяем деления-отнимания
            long seg = tempInteger / 1000;
            result.add(tempInteger - (seg * 1000));
            tempInteger = seg;
        }
        // Последнюю триаду добавим в список
        result.add(tempInteger);
        // Реверсируем список
        Collections.reverse(result);
        return result;
    }

    /**
     * Склоняем словоформу
     *
     * @param n  Long количество объектов
     * @param f1 String вариант словоформы для одного объекта
     * @param f2 String вариант словоформы для двух объектов
     * @param f5 String вариант словоформы для пяти объектов
     * @return String правильный вариант словоформы для указанного количества объектов
     */
    private String morph(long n, String f1, String f2, String f5) {
        n = Math.abs(n) % 100;
        long n1 = n % 10;
        if (n >= 10 && n <= 20) return f5;
        if (n1 > 1 && n1 < 5) return f2;
        if (n1 == 1) return f1;
        return f5;
    }
}