package by.gomel.freedev.ucframework.ucdao.model;

/**
 * @author Andy 20.08.2015.
 */
public class QueryProperty {

    private String key;
    private Object value;

    public QueryProperty(final String key, final Object value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(final String key) {
        this.key = key;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(final Object value) {
        this.value = value;
    }
}
