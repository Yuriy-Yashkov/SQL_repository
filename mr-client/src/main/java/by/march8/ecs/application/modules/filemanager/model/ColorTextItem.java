package by.march8.ecs.application.modules.filemanager.model;

import by.march8.api.BaseEntity;
import by.march8.api.TableHeader;

import java.awt.*;

/**
 * @author Andy 29.12.2018 - 8:05.
 */
public class ColorTextItem extends BaseEntity {
    private int id;
    private String name;
    private Color color;
    private String html;

    public ColorTextItem(int id, String name, Color color, String htmlId) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.html = htmlId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @TableHeader(name = "Наименование цвета", sequence = 0)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public String getHtml() {
        return html;
    }

    public void setHtml(String html) {
        this.html = html;
    }

    @Override
    public String toString() {
        return getName();
    }
}
