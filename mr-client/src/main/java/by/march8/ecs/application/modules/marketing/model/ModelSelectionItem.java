package by.march8.ecs.application.modules.marketing.model;

import by.march8.api.BaseEntity;
import by.march8.api.TableHeader;

public class ModelSelectionItem extends BaseEntity {

    private int id;
    private int model;
    private String articles;

    private int lineHeight;
    private String articlesToHTML;

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    @TableHeader(name = "Модель", sequence = 10)
    public int getModel() {
        return model;
    }

    public void setModel(int model) {
        this.model = model;
    }


    public String getArticles() {
        return articles;
    }

    public void setArticles(String articles) {
        this.articles = articles;
    }

    public int getLineHeight() {
        return lineHeight;
    }

    public void setLineHeight(int lineHeight) {
        this.lineHeight = lineHeight;
    }

    @TableHeader(name = "Артикула", sequence = 20)
    public String getArticlesToHTML() {
        return articlesToHTML;
    }

    public void setArticlesToHTML(String articlesToHTML) {
        this.articlesToHTML = articlesToHTML;
    }
}
