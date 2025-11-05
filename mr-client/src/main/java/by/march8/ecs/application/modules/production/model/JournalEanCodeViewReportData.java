package by.march8.ecs.application.modules.production.model;

import by.march8.entities.production.EanCodeByColorsView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lidashka on 21.12.2018.
 */
public class JournalEanCodeViewReportData {
    private EanCodeByColorsView document;
    private List<EanCodeByColorsView> data;

    public EanCodeByColorsView getDocument() {
        return document;
    }

    public void setDocument(EanCodeByColorsView document) {
        this.document = document;
    }

    public List<EanCodeByColorsView> getData() {
        return data;
    }

    public void setData(ArrayList<EanCodeByColorsView> data) {
        this.data = data;
    }
}
