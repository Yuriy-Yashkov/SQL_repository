package by.gomel.freedev.ucframework.uccore.viewport.gridviewport.model;

import java.lang.annotation.ElementType;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Date;

/**
 * Модель столбца
 * Created by Andy on 09.10.2014.
 */
public class ColumnProperty {
    private String name;
    private int width;
    private int sequence;
    private Method method;
    private Field field;
    private ElementType viewPort;
    private Class<?> clazz;
    private boolean footer;


    public ColumnProperty(final String name, final int width, final int sequence, Method method) {
        this.name = name;
        this.width = width;
        this.sequence = sequence;
        this.method = method;
        this.field = null;
        viewPort = ElementType.METHOD;
    }

    public ColumnProperty(final String name, final int width, final int sequence, Field field) {
        this.name = name;
        this.width = width;
        this.sequence = sequence;
        this.method = null;
        this.field = field;
        viewPort = ElementType.FIELD;
    }


    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(final int width) {
        this.width = width;
    }

    public int getSequence() {
        return sequence;
    }

    public void setSequence(final int sequence) {
        this.sequence = sequence;
    }

    public Method getMethod() {
        return method;
    }

    public Field getField() {
        return field;
    }

    public ElementType getViewPort() {
        return viewPort;
    }

    public Class<?> getClazz() {
        return clazz;
    }

    public void setClazz(Class<?> clazz) {
        if (clazz == Date.class) {
            clazz = String.class;
        }
        this.clazz = clazz;
    }

    public boolean isFooter() {
        return footer;
    }

    public void setFooter(boolean footer) {
        this.footer = footer;
    }
}
