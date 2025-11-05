package by.march8.tasks.accounting;

/**
 * @author Andy 11.01.2019 - 10:51.
 */
public interface DBFExporter<T> {

    void addItem(T t);

    void connect();

    void disconnect();
}
