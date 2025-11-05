package by.march8.entities.sales;

import by.march8.api.BaseEntity;
import by.march8.api.TableHeader;

import java.util.Date;

/**
 * @author tmp on 16.11.2021 11:22
 */
public class SalesDocumentSimple extends BaseEntity {
    private int id ;
    private Date documentDate ;
    private String documentNumber ;

    @Override
    public int getId() {
        return id;
    }
    @Override
    public void setId(int id) {
        this.id = id;
    }

    @TableHeader(name = "Дата", sequence = 10)
    public Date getDocumentDate() {
        return documentDate;
    }

    public void setDocumentDate(Date documentDate) {
        this.documentDate = documentDate;
    }

    @TableHeader(name = "№ документа", sequence = 20)
    public String getDocumentNumber() {
        return documentNumber;
    }

    public void setDocumentNumber(String documentNumber) {
        this.documentNumber = documentNumber;
    }
}
