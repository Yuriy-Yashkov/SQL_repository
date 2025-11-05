package by.march8.ecs.application.modules.warehouse.external.shipping.model;

import by.march8.api.BaseEntity;

/**
 * @author Andy 17.06.2016.
 */
public class ProductionItem extends BaseEntity {
    private int id;
    private int itemCode;
    private String itemModel;
    private String articleName;
    private int articleCode;
    private String name;
    private int stockBalance;


    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(final int id) {
        this.id = id;
    }

    public int getItemCode() {
        return itemCode;
    }

    public void setItemCode(final int itemCode) {
        this.itemCode = itemCode;
    }

    public String getItemModel() {
        return itemModel;
    }

    public void setItemModel(final String itemModel) {
        this.itemModel = itemModel;
    }

    public String getArticleName() {
        return articleName;
    }

    public void setArticleName(final String articleName) {
        this.articleName = articleName;
    }

    public int getArticleCode() {
        return articleCode;
    }

    public void setArticleCode(final int articleCode) {
        this.articleCode = articleCode;
    }

    public String getArticleCodeAsString() {
        return String.valueOf(articleCode);
    }

    public int getStockBalance() {
        return stockBalance;
    }

    public void setStockBalance(final int stockBalance) {
        this.stockBalance = stockBalance;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "ProductionItem{" +
                "id=" + id +
                ", itemCode=" + itemCode +
                ", itemModel='" + itemModel + '\'' +
                ", articleName='" + articleName + '\'' +
                ", articleCode=" + articleCode +
                '}';
    }
}
