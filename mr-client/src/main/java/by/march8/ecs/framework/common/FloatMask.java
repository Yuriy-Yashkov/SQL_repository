package by.march8.ecs.framework.common;

import java.util.regex.Pattern;

/**
 * @author Andy 16.09.2015.
 */
public class FloatMask {
    private boolean preset;
    private int integerPart;
    private int fractionPart;

    public boolean isPreset() {
        return preset;
    }

    public void setPreset(final boolean preset) {
        this.preset = preset;
    }

    public int getIntegerPart() {
        return integerPart;
    }

    public void setIntegerPart(final int integerPart) {
        this.integerPart = integerPart;
    }

    public int getFractionPart() {
        return fractionPart;
    }

    public void setFractionPart(final int fractionPart) {
        this.fractionPart = fractionPart;
    }

    public void parse(final String tempMask) {
        String[] tempSign = tempMask.split(Pattern.quote("."));
        // Если маска соответствует шаблону - парсим
        if (tempSign.length == 2) {
            integerPart = 0;
            try {
                integerPart = Integer.valueOf(tempSign[0]);
            } catch (Exception e) {
                System.err.println("Ошибка форматирования целой части для маски " + tempMask);
            }

            fractionPart = 0;
            try {
                fractionPart = Integer.valueOf(tempSign[1]);
            } catch (Exception e) {
                System.err.println("Ошибка форматирования дробной части для маски " + tempMask);
            }
            // Если маска 0.0  - не обрабатываем и помечаем маску как дэфолтная
            if (integerPart == 0 && fractionPart == 0) {
                setPreset(false);
            } else {
                setPreset(true);
            }
        } else {
            setPreset(false);
        }
        System.out.println("Для маски " + tempMask + " сигнатура " + toString());
    }

    @Override
    public String toString() {
        return "Маска [" +
                "целое=" + integerPart +
                ", дробное=" + fractionPart +
                "], активность=" + preset;
    }
}
