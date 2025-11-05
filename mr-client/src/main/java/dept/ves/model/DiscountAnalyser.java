package dept.ves.model;

/**
 * @author Andy on 02.03.2020 8:50
 */
public class DiscountAnalyser {

    public static int getDiscount(double value) {
        if (value >= 200000.0 && value <= 399999.0) {
            return 2;
        }

        if (value >= 400000.0 && value <= 599999.0) {
            return 4;
        }

        if (value >= 600000.0 && value <= 799999.0) {
            return 6;
        }

        if (value >= 800000.0 && value <= 999999.0) {
            return 8;
        }

        if (value >= 1000000.0 && value <= 1499999.0) {
            return 10;
        }

        if (value >= 1500000.0 && value <= 1999999.0) {
            return 15;
        }

        if (value >= 2000000.0) {
            return 20;
        }

        return 0;
    }

}
