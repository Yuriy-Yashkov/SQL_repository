package by.march8.ecs.application.modules.cut.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class CuttingRouteRow {
    private final StringProperty articleCanvas = new SimpleStringProperty();        // Артикул полотна (nar)
    private final StringProperty name = new SimpleStringProperty();           // Наименование (ngpr)
    private final StringProperty article = new SimpleStringProperty();       // Артикул (sar)
    private final StringProperty modelNumber = new SimpleStringProperty();    // Номер модели (fas)
    private final StringProperty color = new SimpleStringProperty();          // Цвет (ncw)
    private final StringProperty size = new SimpleStringProperty();           // Размер (rzm)
    private final StringProperty height = new SimpleStringProperty();         // Рост (rst)
    private final StringProperty quantity = new SimpleStringProperty();       // Кол-во (kol)
    private final StringProperty brigade = new SimpleStringProperty();        // Бригада (brig_otpr)
    private final StringProperty foreman = new SimpleStringProperty();        // Бригадир
    private final StringProperty codeList = new SimpleStringProperty();       // Код листа (kod_marh)
    private final StringProperty sewDate = new SimpleStringProperty();        // Дата пошива (converted to varchar)

    public CuttingRouteRow(String articleCanvas, String name, String article, String modelNumber,
                           String color, String size, String height, String quantity,
                           String brigade, String foreman, String codeList, String sewDate) {

        this.articleCanvas.set(articleCanvas);
        this.name.set(name);
        this.article.set(article);
        this.modelNumber.set(modelNumber);
        this.color.set(color);
        this.size.set(size);
        this.height.set(height);
        this.quantity.set(quantity);
        this.brigade.set(brigade);
        this.foreman.set(foreman);
        this.codeList.set(codeList);
        this.sewDate.set(sewDate);
    }

    public StringProperty articleCanvasProperty() { return articleCanvas; }
    public StringProperty nameProperty() { return name; }
    public StringProperty articleProperty() { return article; }
    public StringProperty modelNumberProperty() { return modelNumber; }
    public StringProperty colorProperty() { return color; }
    public StringProperty sizeProperty() { return size; }
    public StringProperty heightProperty() { return height; }
    public StringProperty quantityProperty() { return quantity; }
    public StringProperty brigadeProperty() { return brigade; }
    public StringProperty foremanProperty() { return foreman; }
    public StringProperty codeListProperty() { return codeList; }
    public StringProperty sewDateProperty() { return sewDate; }
    public String getArticleCanvas() { return articleCanvas.get(); }
    public String getName() { return name.get(); }
    public String getArticle() { return article.get(); }
    public String getModelNumber() { return modelNumber.get(); }
    public String getColor() { return color.get(); }
    public String getSize() { return size.get(); }
    public String getHeight() { return height.get(); }
    public String getQuantity() { return quantity.get(); }
    public String getBrigade() { return brigade.get(); }
    public String getForeman() { return foreman.get(); }
    public String getCodeList() { return codeList.get(); }
    public String getSewDate() { return sewDate.get(); }

}
