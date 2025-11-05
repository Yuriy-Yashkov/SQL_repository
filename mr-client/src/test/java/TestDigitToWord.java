import by.march8.ecs.framework.helpers.digits.DigitToWords;
import by.march8.ecs.framework.helpers.digits.MeasureUnit;
import org.junit.Ignore;
import org.junit.Test;

import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

/**
 * @author Andy 21.06.2016.
 */
public class TestDigitToWord {

    public static double round(double num, double multipleOf) {
        return Math.floor((num + multipleOf / 2) / multipleOf) * multipleOf;
    }

    @Test
    public void testRound() {
        System.out.println(moneyRound(35.90));
        //System.out.println(moneyRound(3.95999));
        //System.out.println(moneyRound(3.7));
    }

    public double moneyRound(double value) {

        double value_ = new BigDecimal(value)
                .setScale(3, RoundingMode.HALF_UP).setScale(2, RoundingMode.HALF_UP).doubleValue();
        int intPart = (int) value_;
        double x = new BigDecimal(value_ - intPart)
                .setScale(3, RoundingMode.HALF_UP).setScale(2, RoundingMode.HALF_UP).doubleValue();
        int floatPart = (int) ((x) * 100);

        String digit = String.valueOf(floatPart);
        int iFirst, iSecond;

        if (digit.length() > 0) {

            if (digit.length() == 1) {
                iFirst = 0;
                iSecond = Integer.valueOf(digit.substring(0, 1));
            } else {
                iFirst = Integer.valueOf(digit.substring(0, 1));
                iSecond = Integer.valueOf(digit.substring(1, 2));
            }

            if (iSecond > 0 && iSecond <= 5) {
                iSecond = 5;
            }

            if (iSecond > 5) {
                iSecond = 0;
                iFirst += 1;
                if (iFirst > 9) {
                    iFirst = 0;
                    intPart += 1;
                }
            }

        } else {
            iFirst = 0;
            iSecond = 0;
        }

        String sFinalValue = intPart + "." + iFirst + "" + iSecond;

        return Double.valueOf(sFinalValue);
    }

    @Test
    @Ignore
    public void testDigitToWord() {
        double money = 24234.234;
        DigitToWords dtw = new DigitToWords(money, MeasureUnit.WEIGHT);
        System.out.printf(dtw.num2str(true));
    }

    @Test
    @Ignore
    public void testCurrency() {

        // System.out.println(SaleDocumentCalculator.getValueCurrencyWithScale(2, 3.1212f));


        double x = 2.8;
        double percent = 25;
        BigDecimal p = new BigDecimal(x * percent / 100).setScale(10, BigDecimal.ROUND_HALF_DOWN).setScale(2, BigDecimal.ROUND_HALF_DOWN);
        BigDecimal y = new BigDecimal(x - p.doubleValue()).setScale(3, BigDecimal.ROUND_HALF_DOWN).setScale(2, BigDecimal.ROUND_HALF_DOWN);

        double x1 = 234.9;
        double percent1 = 25;
        BigDecimal p1 = new BigDecimal(x1 * percent1 / 100);
        BigDecimal y1 = new BigDecimal(x1 - p1.doubleValue());


        // BigDecimal w = new BigDecimal(Double.valueOf(getValue(5.3, 0.95))).setScale(2, BigDecimal.ROUND_HALF_UP);
        DecimalFormat nf = new DecimalFormat("0.##");
        System.out.println(y + "(2.10) " + y1 + "(2.17) " + getHalfSearch(2.125));
    }

    private double getCutDecimal(double d) {
        String s = String.valueOf(d);
        String outString = "";
        int decimalIndex = s.indexOf(".");
        if (decimalIndex + 3 > s.length()) {
            outString = s;
        } else {
            outString = s.substring(0, decimalIndex + 3);
        }
        return Double.valueOf(outString);
    }

    private double getHalfSearch(double d) {
        String s = String.valueOf(d);

        int decimalIndex = s.indexOf(".");
        System.out.println("Индекс " + decimalIndex);
        if (decimalIndex + 3 < s.length()) {

            if (s.charAt(decimalIndex + 3) == '5') {
                System.out.println("Режем хвост");
                return getCutDecimal(d);
            } else {
                BigDecimal bd = new BigDecimal(d).setScale(3, BigDecimal.ROUND_HALF_UP).setScale(2, BigDecimal.ROUND_HALF_UP);
                System.out.println("Округляем");
                return bd.doubleValue();
            }

        } else {
            System.out.println("Игнор");
            return d;
        }

    }

    @Test
    @Ignore
    public void testASC2Text() {
        System.out.println(getDeliveryNoteID("СТ2345234"));
    }

    public String getASCIIByString(String s) {
        String tmp = s.toUpperCase();
        char ch = s.charAt(0);
        switch (ch) {
            case 'А':
                return "192";
            case 'Б':
                return "193";
            case 'В':
                return "194";
            case 'Г':
                return "195";
            case 'Д':
                return "196";
            case 'Е':
                return "197";
            case 'Ж':
                return "198";
            case 'З':
                return "199";
            case 'И':
                return "200";
            case 'Й':
                return "201";
            case 'К':
                return "202";
            case 'Л':
                return "203";
            case 'М':
                return "204";
            case 'Н':
                return "205";
            case 'О':
                return "206";
            case 'П':
                return "207";
            case 'Р':
                return "208";
            case 'С':
                return "209";
            case 'Т':
                return "210";
            case 'У':
                return "211";
            case 'Ф':
                return "212";
            case 'Х':
                return "213";
            case 'Ц':
                return "214";
            case 'Ч':
                return "215";
            case 'Ш':
                return "216";
            case 'Щ':
                return "217";
            case 'Ъ':
                return "218";
            case 'Ы':
                return "219";
            case 'Ь':
                return "220";
            case 'Э':
                return "221";
            case 'Ю':
                return "222";
            case 'Я':
                return "223";
        }
        return "000";
    }

    private String getDeliveryNoteID(String number) {
        String uid = "4813705900003";
        String s1 = number.substring(0, 1);
        String s2 = number.substring(1, 2);

        String num = number.substring(2, number.length());
        //System.out.println(s1+"-"+s2+"-"+num);
        return "001-" + uid + "-" + getASCIIByString(s1) + getASCIIByString(s2) + "0" + num;
    }

    @Test
    @Ignore
    public void testChar() {
        System.out.println(String.valueOf((char) 54));
    }

    private double getValue(double x, double y) {
        return x * y;
    }

    @Test
    @Ignore
    public void testChild() {
        //System.out.println(SaleDocumentDataProvider.isChildItem("47277250"));
        //System.out.println(SaleDocumentDataProvider.isChildItem("47210040"));
        //System.out.println(SaleDocumentDataProvider.isChildItem("42370980"));

        String destDir = "C:/output";


        // returns pathnames for files and directory
        File f = new File("C:/Texts/BlaBla/BlaBlaBla/asdas/asdaeerg/temp.txt");
        System.out.println(f.getParent());

        //destDir+=f.getParent().replace("C:\\Texts","");

        //f = new File(destDir);
        //System.out.println(destDir);
        // create
        //boolean bool = f.mkdirs();

        // print
        // System.out.print("Directory created? "+bool);
    }
}
