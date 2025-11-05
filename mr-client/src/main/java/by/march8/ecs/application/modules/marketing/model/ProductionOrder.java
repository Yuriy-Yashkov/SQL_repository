package by.march8.ecs.application.modules.marketing.model;

import by.march8.entities.product.OrderItem;

import java.util.Date;
import java.util.List;

public class ProductionOrder {
    private Date date;
    private Catalog catalog;
    private List<OrderItem> orderList;


    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Catalog getCatalog() {
        return catalog;
    }

    public void setCatalog(Catalog catalog) {
        this.catalog = catalog;
    }

    public List<OrderItem> getOrderList() {
        return orderList;
    }

    public void setOrderList(List<OrderItem> orderList) {
        this.orderList = orderList;
    }

}
