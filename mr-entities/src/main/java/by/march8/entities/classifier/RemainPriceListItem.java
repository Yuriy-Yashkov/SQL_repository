package by.march8.entities.classifier;

import by.march8.api.BaseEntity;
import by.march8.api.MarchDataSourceEnum;
import by.march8.api.RedirectToEntityManager;
import by.march8.api.TableHeader;
import by.march8.api.utils.DateUtils;

import javax.persistence.*;
import java.util.Date;

/**
 * @author Andy 13.03.2017.
 */

@NamedQueries({
        @NamedQuery(name = "RemainPriceListItem.findAll",
                query = "SELECT item FROM RemainPriceListItem item  " +
                        "order by item.documentDate" +
                        "  "),
})

@Entity
@Table(name = "REMAINS_PRICELIST")
@RedirectToEntityManager(redirectTo = MarchDataSourceEnum.DS_SQLMARCH8)
public class RemainPriceListItem extends BaseEntity {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @TableHeader(name = "Номер", sequence = 0)
    @Column(name = "DOCUMENT_NUMBER")
    private String documentNumber;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "DOCUMENT_DATE")
    @TableHeader(name = "Дата", width = -100, sequence = 1)
    private Date documentDate;

    @Column(name = "TRADE_ALLOWANCE_VALUE")
    @TableHeader(name = "Надбавка", width = -90, sequence = 3)
    private double tradeAllowanceValue;

    @Column(name = "ORDER_NUMBER")
    private String orderNumber;

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(final int id) {
        this.id = id;
    }

    public String getDocumentNumber() {
        if (documentNumber != null) {
            return documentNumber.trim();
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

    public double getTradeAllowanceValue() {
        return tradeAllowanceValue;
    }

    public void setTradeAllowanceValue(final double tradeAllowanceValue) {
        this.tradeAllowanceValue = tradeAllowanceValue;
    }

    @TableHeader(name = "Приказ №", width = -80, sequence = 2)
    public String getOrderNumber() {
        if (orderNumber != null) {
            return orderNumber.trim();
        } else {
            return "";
        }
    }

    public void setOrderNumber(final String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getDocumentInformation() {
        return "Прейскурант уценки №" + getDocumentNumber() + " от "
                + DateUtils.getNormalDateFormat(getDocumentDate())
                + "г. с размером уценки " + getTradeAllowanceValue() + "%";
    }
}
