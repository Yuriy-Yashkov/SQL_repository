package by.march8.ecs.framework.common.money;

import by.march8.ecs.framework.common.model.RoundingRule;

import java.math.BigDecimal;

/**
 * @author Andy 03.11.2017.
 */
public class RoundUtils {

    public static final int ROUND_XXX_XX = 4;
    public static final int ROUND_XXX_XXXX = 5;
    public static final int ROUND_XXX_XXX = 6;
    private static final RoundingRule roundingRule_100 = new RoundingRule(50, 50, 100);
    private static final RoundingRule roundingRule_50 = new RoundingRule(25, 75, 50);

    public static double round(double value, int roundType) {

        switch (roundType) {
            // Без округления
            case 0:
                break;
            // Округлять до целого
            case 1:
                return roundAndGetDouble(value, 0);
            // Округление 100 до запятой
            case 2:
                return roundLongByRoundingRule(roundAndGetDouble(value, -2), roundingRule_100);
            // Дробное 1 знак после запятой
            case 3:
                return roundAndGetDouble(value, 1);
            // Дробное 2 знака после запятой
            case 4:
                return roundAndGetDouble(value, 2);
            case 5:
                return roundAndGetDouble(value, 4);

            case 6:
                return roundAndGetDouble(value, 3);
        }
        return 0;
    }

    /**
     * Округление целого числа типа Long по правилу
     * ПРоверено на округлении до 50 и 100
     *
     * @param value значение для округления
     * @param rule  правило округления
     * @return округленное значение
     */
    public static double roundLongByRoundingRule(double value, RoundingRule rule) {
        int rest = (int) value % 100;
        long x = (long) value;
        x /= 100;
        x *= 100;
        if (rule.lowerLimit == rule.upperLimit) {
            if (rest >= rule.lowerLimit) {
                x += rule.roundValue;
            }
        } else {
            if (rest >= rule.upperLimit) {
                x += (rule.roundValue * 2);
            } else if (rest >= rule.lowerLimit && rest < rule.upperLimit) {
                x += rule.roundValue;
            }
        }
        return x;
    }

    public static double roundAndGetDouble(double value, int roundIndex) {
        return roundBigDecimal(value, roundIndex).doubleValue();
    }

    public static BigDecimal roundBigDecimal(double value, int roundIndex) {
        BigDecimal decimal = new BigDecimal(String.valueOf(value));
        // В цикле округляем с большего (думаю больше 5-ти знаков не имеет смысла вводить) до нужного знака
      /*  if (roundIndex > 5) {
            roundIndex = 5;
        }
        for (int index = 5; index >= roundIndex; index--) {
            decimal = decimal.setScale(index, RoundingMode.HALF_UP);
        }
        */
        return decimal.setScale(10, BigDecimal.ROUND_HALF_UP).setScale(roundIndex, BigDecimal.ROUND_HALF_UP);

    }

}
