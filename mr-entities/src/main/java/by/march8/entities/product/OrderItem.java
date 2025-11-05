package by.march8.entities.product;

import by.march8.api.BaseEntity;
import by.march8.api.TableHeader;

public class OrderItem extends BaseEntity {
    private int id ;
    private int articleId;
    private int productId;
    private String articleNumber;
    private int modelNumber;
    private String itemName ;
    private String itemSize ;
    private double itemPrice;
    private String itemColor;
    private int amount ;

    public int getArticleId() {

        return articleId;
    }

    public void setArticleId(int articleId) {
        this.articleId = articleId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }
    @TableHeader(name="Артикул", sequence = 30)
    public String getArticleNumber() {
        if(articleNumber!=null) {
            return articleNumber.trim();
        }
        return null ;
    }

    public void setArticleNumber(String articleNumber) {
        this.articleNumber = articleNumber;
    }

    @TableHeader(name="Модель", sequence = 20)
    public int getModelNumber() {
        return modelNumber;
    }

    public void setModelNumber(int modelNumber) {
        this.modelNumber = modelNumber;
    }
    @TableHeader(name="Наименование", sequence = 10)
    public String getItemName() {
        if(itemName!=null) {
            return itemName.trim();
        }
        return null ;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    @TableHeader(name="Размер", sequence = 40)
    public String getItemSize() {
        if(itemSize!=null) {
            return itemSize.trim();
        }
        return null ;
    }


    public void setItemSize(String itemSize) {
        this.itemSize = itemSize;
    }

    public double getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(double itemPrice) {
        this.itemPrice = itemPrice;
    }
    @TableHeader(name="Цвет", sequence = 50)
    public String getItemColor() {
        if(itemColor!=null) {
            return itemColor.trim();
        }
        return null ;
    }

    public void setItemColor(String itemColor) {
        this.itemColor = itemColor;
    }

    @TableHeader(name="Кол-во", sequence = 60)
    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "ProductionOrder{" +
                "articleId=" + articleId +
                ", productId=" + productId +
                ", articleNumber='" + articleNumber + '\'' +
                ", modelNumber=" + modelNumber +
                ", itemName='" + itemName + '\'' +
                ", itemSize='" + itemSize + '\'' +
                ", itemPrice=" + itemPrice +
                ", itemColor='" + itemColor + '\'' +
                ", amount=" + amount +
                '}';
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }
}
