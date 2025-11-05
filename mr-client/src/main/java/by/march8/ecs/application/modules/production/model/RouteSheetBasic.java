package by.march8.ecs.application.modules.production.model;

import by.march8.api.BaseEntity;
import by.march8.api.TableHeader;

import java.util.Date;

/**
 * @author Andy on 28.02.2020 8:35
 */
public class RouteSheetBasic extends BaseEntity {
    private int id;
    private String documentNumber;
    private Date documentDate;

    private int amount;
    private int deptOwner;

    @Override
    @TableHeader(name = "КОД", sequence = 0)
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    @TableHeader(name = "№", sequence = 10)
    public String getDocumentNumber() {
        if (documentNumber != null) {
            return documentNumber.trim();
        }
        return "";
    }

    public void setDocumentNumber(String documentNumber) {
        this.documentNumber = documentNumber;
    }

    @TableHeader(name = "Дата", sequence = 20)
    public Date getDocumentDate() {
        return documentDate;
    }

    public void setDocumentDate(Date documentDate) {
        this.documentDate = documentDate;
    }

    @TableHeader(name = "Кол-во", sequence = 30)
    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    @TableHeader(name = "Владелец", sequence = 40)
    public int getDeptOwner() {
        return deptOwner;
    }

    public void setDeptOwner(int deptOwner) {
        this.deptOwner = deptOwner;
    }
}
