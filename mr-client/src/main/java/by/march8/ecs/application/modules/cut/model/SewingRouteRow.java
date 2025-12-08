package by.march8.ecs.application.modules.cut.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class SewingRouteRow {
    private final StringProperty article = new SimpleStringProperty();        // Артикул (nar)
    private final StringProperty name = new SimpleStringProperty();           // Наименование (ngpr)
    private final StringProperty article2 = new SimpleStringProperty();       // Артикул2 (sar)
    private final StringProperty modelNumber = new SimpleStringProperty();    // Номер модели (fas)
    private final StringProperty color = new SimpleStringProperty();          // Цвет (ncw)
    private final StringProperty grade = new SimpleStringProperty();          // Сорт (srt)
    private final StringProperty size = new SimpleStringProperty();           // Размер (rzm)
    private final StringProperty height = new SimpleStringProperty();         // Рост (rst)
    private final StringProperty quantity = new SimpleStringProperty();       // Кол-во (kol_sdano)
    private final StringProperty brigade = new SimpleStringProperty();        // Бригада (brigada)
    private final StringProperty foreman = new SimpleStringProperty();        // Бригадир (brigada)
    private final StringProperty codeList = new SimpleStringProperty();       // Код листа (kod_marh)
    private final StringProperty sewDate = new SimpleStringProperty();        // Дата пошива (CONVERT)

    public SewingRouteRow(String article, String name, String article2, String modelNumber,
                          String color, String grade, String size, String height,
                          String quantity, String brigade, String foreman, String codeList, String sewDate) {

        this.article.set(article);
        this.name.set(name);
        this.article2.set(article2);
        this.modelNumber.set(modelNumber);
        this.color.set(color);
        this.grade.set(grade);
        this.size.set(size);
        this.height.set(height);
        this.quantity.set(quantity);
        this.brigade.set(brigade);
        this.foreman.set(foreman);
        this.codeList.set(codeList);
        this.sewDate.set(sewDate);
    }

    public StringProperty articleProperty() { return article; }
    public StringProperty nameProperty() { return name; }
    public StringProperty article2Property() { return article2; }
    public StringProperty modelNumberProperty() { return modelNumber; }
    public StringProperty colorProperty() { return color; }
    public StringProperty gradeProperty() { return grade; }
    public StringProperty sizeProperty() { return size; }
    public StringProperty heightProperty() { return height; }
    public StringProperty quantityProperty() { return quantity; }
    public StringProperty brigadeProperty() { return brigade; }
    public StringProperty foremanProperty() { return brigade; }
    public StringProperty codeListProperty() { return codeList; }
    public StringProperty sewDateProperty() { return sewDate; }
    public String getArticle() { return article.get(); }
    public String getName() { return name.get(); }
    public String getArticle2() { return article2.get(); }
    public String getModelNumber() { return modelNumber.get(); }
    public String getColor() { return color.get(); }
    public String getGrade() { return grade.get(); }
    public String getSize() { return size.get(); }
    public String getHeight() { return height.get(); }
    public String getQuantity() { return quantity.get(); }
    public String getBrigade() { return brigade.get(); }
    public String getForeman() { return foreman.get(); }
    public String getCodeList() { return codeList.get(); }
    public String getSewDate() { return sewDate.get(); }

}
