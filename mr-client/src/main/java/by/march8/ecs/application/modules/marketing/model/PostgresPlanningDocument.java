package by.march8.ecs.application.modules.marketing.model;

import by.march8.api.BaseEntity;
import by.march8.api.TableHeader;

import java.util.Date;

public class PostgresPlanningDocument extends BaseEntity {

    private int id;

    private String number;
    private Date date;
    private int status;

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    @TableHeader(name = "Наименование", sequence = 10)
    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    @TableHeader(name = "Дата", sequence = 20)
    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @TableHeader(name = "Тип", sequence = 10)
    public String getDocumentType() {
        switch (status) {
            case 0:
                return "План";
            default:
                return "Проект";
        }
    }
}
