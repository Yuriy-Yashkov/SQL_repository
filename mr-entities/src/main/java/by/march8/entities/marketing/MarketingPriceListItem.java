package by.march8.entities.marketing;

import by.march8.api.BaseEntity;
import by.march8.api.RedirectToEntityManager;
import by.march8.api.TableHeader;
import by.march8.api.MarchDataSourceEnum;

import javax.persistence.*;
import java.util.Date;


@NamedQueries({
        @NamedQuery(name = "MarketingPriceListItem.findAll",
                query = "SELECT item FROM MarketingPriceListItem item  " +
                        "order by item.documentDate" +
                        "  "),
})
/**
 * @author Andy 12.10.2017.
 */
@Entity
@Table(name = "MARKETING_PRICE_LIST")
@RedirectToEntityManager(redirectTo = MarchDataSourceEnum.DS_SQLMARCH8)
public class MarketingPriceListItem extends BaseEntity {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "DOCUMENT_NUMBER")
    private String documentNumber;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "DOCUMENT_DATE")
    @TableHeader(name = "Дата", width = -80, sequence = 3)
    private Date documentDate;

    @Column(name = "DOCUMENT_TYPE")
    private int documentType;

    @Column(name = "DOCUMENT_VALUE")
    @TableHeader(name = "Ставка", width = -40, sequence = 5)
    private double documentValue;

    @Column(name = "STATUS")
    @TableHeader(name = "", width = 0, sequence = 1)
    private int status;

    @Column(name="TO_PRIME_COST")
    private boolean toPrimeCost ;

    @Column(name = "ORDER_NUMBER")
    private String orderNumber;

    @Column(name = "ORDER_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date orderDate;

    @Column(name = "ORDER_ALLOWANCE_VALUE")
    private double allowanceValue;

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(final int id) {
        this.id = id;
    }

    @TableHeader(name = "Номер", sequence = 2)
    public String getDocumentNumber() {
        if (documentNumber != null) {
            return documentNumber.trim();
        } else {
            return "";
        }
    }

    @TableHeader(name = "Тип документа", width = -100, sequence = 4)
    public String getDocumentTypeString(){
        switch (documentType){
            case 1:return "Изменение ТН";
            case 2:return "Уценка";
        }
        return "";
    }

    public void setDocumentNumber(final String documentNumber) {
        this.documentNumber = documentNumber;
    }

    public Date getDocumentDate() {
        return documentDate;
    }

    public void setDocumentDate(final Date documentDate) {
        this.documentDate = documentDate;
    }

    public int getDocumentType() {
        return documentType;
    }

    public void setDocumentType(final int documentType) {
        this.documentType = documentType;
    }

    public double getDocumentValue() {
        return documentValue;
    }

    public void setDocumentValue(final double documentValue) {
        this.documentValue = documentValue;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(final int status) {
        this.status = status;
    }

    public boolean isToPrimeCost() {
        return toPrimeCost;
    }

    public void setToPrimeCost(final boolean toPrimeCost) {
        this.toPrimeCost = toPrimeCost;
    }

    public String getOrderNumber() {
        if(orderNumber!=null) {
            return orderNumber.trim();
        }else{
            return "";
        }
    }

    public void setOrderNumber(final String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(final Date orderDate) {
        this.orderDate = orderDate;
    }

    public double getAllowanceValue() {
        return allowanceValue;
    }

    public void setAllowanceValue(final double allowanceValue) {
        this.allowanceValue = allowanceValue;
    }
}
