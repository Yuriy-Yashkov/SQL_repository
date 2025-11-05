package by.march8.entities.warehouse;

import by.march8.api.BaseEntity;
import by.march8.api.TableHeader;

import javax.swing.*;

/**
 * @author Andy 26.04.2018 - 12:53.
 */
public class WarehouseRemainsItem extends BaseEntity {
    private int id;
    @TableHeader(name = "Ассортимент продукции_Наименование", width = 230, sequence = 10)
    private String itemName;
    @TableHeader(name = "Ассортимент продукции_Номер артикула", width = -100, sequence = 40)
    private String articleNumber;
    //@TableHeader(name = "Артикул_Шифр", sequence = 30)
    private String articleCode;
    @TableHeader(name = "Ассортимент продукции_Модель", width = -100, sequence = 30)
    private int modelNumber;
    @TableHeader(name = "Сорт", width = -50, sequence = 50)
    private int grade;
    //@TableHeader(name = "Размер", width = -50,sequence = 60)
    private int size;
    //@TableHeader(name = "Рост",width = -50, sequence = 70)
    private int growth;
    @TableHeader(name = "Количество_Модель", width = -100, sequence = 90)
    private int amountByModel;
    @TableHeader(name = "Количество_Артикул", width = -100, sequence = 91)
    private int amountByArticle;


    private String type;

    private int typeIndex;

    private int amount;

    //@TableHeader(name = "Цена, ед.",width = -50, sequence = 100)
    private double cost;

    private String sizePrint;

    private String growthPrint;
    private String costPrint;

    private String itemNamePrint;

    @TableHeader(name = "Размер", width = -50, sequence = 60)
    public String getSizePrint() {
        return "<html>" + sizePrint + "</html>";
    }

    @TableHeader(name = "Цена, ед.", width = -50, sequence = 100)
    public String getCostPrint() {
        return
                "<html>" + costPrint + "</html>";
    }

    public void setCostPrint(final double cost) {
        if (costPrint == null) {
            costPrint = String.valueOf(cost);
        } else {
            costPrint += "<br>" + String.valueOf(cost);
        }
    }

    public void setSizePrint(int size) {
        if (sizePrint == null) {
            sizePrint = String.valueOf(size);
        } else {
            sizePrint += "<br>" + String.valueOf(size);
        }
    }

    public void setSizePrint(final String sizePrint) {
        this.sizePrint = sizePrint;
    }

    private String amountPrint;


    @TableHeader(name = "Количество_Росто-размер", width = -50, sequence = 92)
    public String getAmountPrint() {
        return "<html>" + amountPrint + "</html>";
    }

    public void setAmountPrint(final int amount) {

      /*  if (amountPrint == null) {
            amountPrint = String.valueOf(amount);
        } else {
            amountPrint += "<br>" + String.valueOf(amount);
        }
        */

        if (amountPrint == null) {
            if (amount < 0) {
                amountPrint = "<font color=\"red\">" + String.valueOf(amount) + "</font>";
            } else {
                amountPrint = String.valueOf(amount);
            }
        } else {
            if (amount < 0) {
                amountPrint += "<br><font color=\"red\">" + String.valueOf(amount) + "</font>";
            } else {
                amountPrint += "<br>" + String.valueOf(amount);
            }
        }
    }

    @TableHeader(name = "Рост", width = -50, sequence = 70)
    public String getGrowthPrint() {
        return "<html>" + growthPrint + "</html>";
    }

    public void setGrowthPrint(final int growth) {
        if (growthPrint == null) {
            growthPrint = String.valueOf(growth);
        } else {
            growthPrint += "<br>" + String.valueOf(growth);
        }
    }

    private JLabel image ;


    @Override
    public int getId() {
        return id;
    }


    @Override
    public void setId(final int id) {
        this.id = id;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(final String itemName) {
        this.itemName = itemName;
    }

    public String getArticleNumber() {
        return articleNumber;
    }

    public void setArticleNumber(final String articleNumber) {
        this.articleNumber = articleNumber;
    }

    public String getArticleCode() {
        if (articleCode == null) {
            articleCode = "0";
        } else {
            if (articleCode.trim().equals("")) {
                articleCode = "0";
            } else {
                articleCode = articleCode.trim();
            }
        }
        return articleCode ;
    }

    public void setArticleCode(final String articleCode) {
        this.articleCode = articleCode;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(final int grade) {
        this.grade = grade;
    }

    public int getSize() {
        return size;
    }

    public void setSize(final int size) {
        this.size = size;
    }

    public int getGrowth() {
        return growth;
    }

    public void setGrowth(final int growth) {
        this.growth = growth;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(final double cost) {
        this.cost = cost;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(final int amount) {
        this.amount = amount;
    }

    public int getModelNumber() {
        return modelNumber;
    }


    public void setModelNumber(final int modelNumber) {
        this.modelNumber = modelNumber;
    }

    public int getAmountByModel() {
        return amountByModel;
    }

    public void setAmountByModel(final int amountByModel) {
        this.amountByModel = amountByModel;
    }

    public int getAmountByArticle() {
        return amountByArticle;
    }

    public void setAmountByArticle(final int amountByArticle) {
        this.amountByArticle = amountByArticle;
    }

    @TableHeader(name = "Изображение", width = 150, sequence = 900)
    public JLabel getImage() {
        return image;
    }

    public void setImage(final JLabel image) {
        this.image = image;
    }

    @TableHeader(name = "Ассортимент продукции_Категория", width=250, sequence = 11)
    public String getType() {
        return type;
    }

    public int getTypeIndex() {
        return typeIndex;
    }

    public void setTypeIndex(final int typeIndex) {
        this.typeIndex = typeIndex;
    }

    public void setType(final String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "WarehouseRemainsItem{" +
                "id=" + id +
                ", itemName='" + itemName + '\'' +
                ", modelNumber=" + modelNumber +
                ", sizePrint='" + sizePrint + '\'' +
                ", growthPrint='" + growthPrint + '\'' +
                ", costPrint='" + costPrint + '\'' +
                '}';
    }

    public String getItemNamePrint() {
        return itemNamePrint;
    }

    public void setItemNamePrint(final String itemNamePrint) {
        this.itemNamePrint = itemNamePrint;
    }
}
