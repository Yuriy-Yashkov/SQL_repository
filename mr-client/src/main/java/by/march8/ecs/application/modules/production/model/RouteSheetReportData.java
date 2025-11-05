package by.march8.ecs.application.modules.production.model;

import by.march8.entities.production.RouteSheetItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lidashka on 19.10.2018.
 */
public class RouteSheetReportData {
    private RouteSheetItem document;
    private List<RouteSheetItem> data;

    public RouteSheetItem getDocument() {
        return document;
    }

    public void setDocument(RouteSheetItem document) {
        this.document = document;
    }

    public List<RouteSheetItem> getData() {
        return data;
    }

    public void setData(ArrayList<RouteSheetItem> data) {
        this.data = data;
    }
}
