package by.march8.ecs.framework.common.model;

/**
 * Модель правила округления
 *
 * @author Andy 05.11.2015.
 */
public class RoundingRule {
    /**
     * Нижняя граница округления
     */
    public int lowerLimit;

    /**
     * Верхняя граница округления
     */
    public int upperLimit;

    /**
     * Величина округления
     */
    public int roundValue;

    public RoundingRule(final int lowerLimit, final int upperLimit, final int roundValue) {
        this.lowerLimit = lowerLimit;
        this.upperLimit = upperLimit;
        this.roundValue = roundValue;
    }
}
