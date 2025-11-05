package by.gomel.freedev.ucframework.uccore.viewport.editor;

/**
 * @author Andy 21.11.2018 - 14:52.
 */
public interface EntityEditor<O, I> {

    O getSourceEntity();

    void setSourceEntiy(I entity);
}
