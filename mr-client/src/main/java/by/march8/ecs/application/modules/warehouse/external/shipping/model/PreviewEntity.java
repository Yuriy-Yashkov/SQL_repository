package by.march8.ecs.application.modules.warehouse.external.shipping.model;

/**
 * Позволяет представлять класс как колонтитул грида
 * В коллекции является тем же классом , но имеет некторорые признаки, определяющие его как колонтитул
 *
 * @author Andy 03.11.2015.
 */
public abstract class PreviewEntity {
    // Тип нижнего колонтитула обычный
    public static final int FOOTER_COMMON = 0;
    // Тип нижнего колонтитула итоговый, по всему документу
    public static final int FOOTER_FINAL = 1;

    /**
     * Возвращает значение по которому будет контролироваться начало нового блока в коллекции
     *
     * @return значение-маркер
     */
    public abstract String getMarkerFieldValue();

    /**
     * Генерирует объект данного класса, но промаркированного как колонтитул для блока
     *
     * @param footerType тип колонтитула
     * @return объект класса как колонтитул
     */
    public abstract PreviewEntity generateFooter(int footerType);

    /**
     * метод расчитывает необходимые поля у класса при каждой итеррации про создании колонтитула
     *
     * @param item          объект-колонтитул
     * @param previewEntity текущий объект коллекции, из которого нужно брать данные
     * @return объект колонтитул
     */
    public abstract PreviewEntity calculateFooter(final PreviewEntity item, final PreviewEntity previewEntity);

    /**
     * Проверка на то, является ли данный объект колонтитулом
     *
     * @return true если объект является колонтитулом
     */
    public abstract boolean isFooter();
}
