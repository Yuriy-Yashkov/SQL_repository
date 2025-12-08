package by.march8.ecs.application.modules.cut.model;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class CodeListRow {

    private final ObjectProperty<LocalDate> date = new SimpleObjectProperty<>();
    private final StringProperty codeList = new SimpleStringProperty();

    public CodeListRow(String date, String codeList) {
        // конвертируем строку "dd.MM.yyyy" → LocalDate
        DateTimeFormatter f = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        this.date.set(LocalDate.parse(date, f));
        this.codeList.set(codeList);
    }

    public ObjectProperty<LocalDate> dateProperty() {
        return date;
    }

    public LocalDate getDate() {
        return date.get();
    }

    public void setDate(LocalDate d) {
        date.set(d);
    }

    public StringProperty codeListProperty() {
        return codeList;
    }

    public String getCodeList() {
        return codeList.get();
    }

    public void setCodeList(String n) {
        codeList.set(n);
    }
}
