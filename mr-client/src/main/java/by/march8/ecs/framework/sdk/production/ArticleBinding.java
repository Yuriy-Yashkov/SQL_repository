package by.march8.ecs.framework.sdk.production;

import java.util.Date;

/**
 * The Class ArticleBinding.
 */
@SuppressWarnings("all")
public class ArticleBinding {

    // Перечень 3 символа артикула
    /**
     * The Constant productionTypeModifiers.
     */
    static final String[] productionTypeModifiers = {"Собств", "Давальч"};
    // 3 символ артикула
    /**
     * The Constant productTypeCode.
     */
    static final String[] productTypeCode = {"С", "Д"};
    // Перечень 4 символа артикула
    /**
     * The Constant productOrderTypeModifiers.
     */
    static final String[] productOrderTypeModifiers = {"Заказ", "Норм"};
    // 4 символ артикула
    /**
     * The Constant productOrderTypeCode.
     */
    static final String[] productOrderTypeCode = {"З", "Н"};
    // Перечень 5 символа артикула (Группа продукции)
    /**
     * The Constant productionGroupModifiers.
     */
    static final String[] productionGroupModifiers = {"Бельё",
            "Верхний трикотаж", "Чулки", "Полотно", "Пряжа-протир"};
    // 5 символ артикула (Группа продукции)
    /**
     * The Constant productionGroupCode.
     */
    static final int[] productionGroupCode = {1, 2, 3, 7, 8};

    /**
     * Article binding.
     *
     * @param productType      the product type
     * @param productOrderType the product order type
     * @param productionGroup  the production group
     * @param id               the id
     * @return the string
     */

    // Генерация Артикула
    @SuppressWarnings("deprecation")
    public final String articleBinding(final String productType,
                                       final String productOrderType, final String productionGroup,
                                       final int id) {
        int index;
        final int FIFTH_GROUP = 5;
        final int DATE_CORRECTION = 100;
        final StringBuilder result = new StringBuilder();
        // Получаем системное время
        final Date date = new Date(System.currentTimeMillis());
        // ГОД (Первые 2 цифры артикула)
        result.append(date.getYear() - DATE_CORRECTION); // последние 2 цифры

        // ProductionType
        for (index = 0; index < productionTypeModifiers.length; index++) {
            if (productType.toLowerCase().contains(
                    productionTypeModifiers[index].toLowerCase())) {
                result.append(productTypeCode[index]);
                break;
            }
        }
        // ProductionOrderType
        for (index = 0; index < productOrderTypeModifiers.length; index++) {
            if (productOrderType.toLowerCase().contains(
                    productOrderTypeModifiers[index].toLowerCase())) {
                result.append(productOrderTypeCode[index]);
                break;
            }
        }
        // ProductionGroup
        for (index = 0; index < productionGroupModifiers.length; index++) {
            if (productionGroup.toLowerCase().contains(
                    productionGroupModifiers[index].toLowerCase())) {
                result.append(productionGroupCode[index]);
                break;
            }
            // Если не нашло в перечне нужной группы продукции, методом
            // исключения добавляется символ 5 (Прочие, протир лента, лоскут за
            // м., пакет 0,5кг)
            result.append(FIFTH_GROUP);
            break;
        }

        // ID
        result.append("." + id);
        // Д40
        result.append(".Д40");

        // Готовый артикул
        return result.toString();
    }
    /**
     * Проверка работы метода public static void main(final String[] args) {
     * final ArticleBinding ab = new ArticleBinding();
     * System.out.println(ab.articleBinding("Собств", "Заказ", "Бельё", 100)); }
     */

}
