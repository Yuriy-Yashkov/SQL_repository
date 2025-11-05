package by.march8.entities.service;

import by.march8.api.BaseEntity;
import by.march8.api.MarchDataSourceEnum;
import by.march8.api.RedirectToEntityManager;
import by.march8.api.TableHeader;

import javax.persistence.*;

@Entity
@Table(name = "SERVICE_PRICE")
@RedirectToEntityManager(redirectTo = MarchDataSourceEnum.DS_SQLMARCH8)
public class ServicePriceEntity extends BaseEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int ID;
    @Column(name = "ARTICLE")
    @TableHeader(name = "Артикул", width = 200, sequence = 0)
    private String article;
    @Column(name = "PRICE")
    @TableHeader(name = "Цена", width = 30, sequence = 1)
    private double price;

    public String getArticle() {
        return article;
    }

    public void setArticle(String article) {
        this.article = article;
    }

    public double getPrice() {
        return price;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public void setPrice(double price) {
        this.price = price;
    }
    @Override
    public String toString() {
        return "SERVICE_PRICE = {"+
                " "+Double.toString(getPrice())+
                " "+getArticle()+"}";

    }
}