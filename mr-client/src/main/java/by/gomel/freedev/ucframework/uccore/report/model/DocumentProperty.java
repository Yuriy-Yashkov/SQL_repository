package by.gomel.freedev.ucframework.uccore.report.model;

/** Структура свойства объекта
 * Created by Andy on 17.10.2014.
 */
public class DocumentProperty {
    private String key;
    private String value;


    public DocumentProperty(final String key, final String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

}
