package by.gomel.freedev.ucframework.uccore.report.api.openoffice;

import java.awt.*;

/**
 * Примитив ссылки на ячейку
 *
 * Created by Andy on 22.10.2014.
 */
public class ReportLabel {
    private String name;
    private Point link;

    public ReportLabel(final String name, final Point link) {
        this.name = name;
        this.link = link;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public Point getLink() {
        return link;
    }

    public void setLink(final Point link) {
        this.link = link;
    }
}
