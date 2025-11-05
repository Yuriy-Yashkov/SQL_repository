package by.march8.entities.warehouse;

import by.march8.api.BaseEntity;
import by.march8.api.RedirectToEntityManager;
import by.march8.api.MarchDataSourceEnum;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * @author Andy 13.10.2017.
 */
@Entity
@Table(name = "otgruz1")
@RedirectToEntityManager(redirectTo = MarchDataSourceEnum.DS_SQLMARCH8)
public class SaleDocumentRefundInformation extends BaseEntity{

    @Id
    @Column(name = "item_id")
    private int id ;

    @Column(name = "source_doc_number")
    private String sourceDocNumber ;
    @Column(name = "source_doc_date")
    private Date sourceDocDate ;

    @Column(name = "refund_doc_number")
    private String refundDocNumber ;
    @Column(name = "refund_doc_date")
    private Date refundDocDate ;

    @Column(name="saved")
    private boolean isCalculate ;

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(final int id) {
        this.id = id;
    }

    public String getSourceDocNumber() {
        return sourceDocNumber;
    }

    public void setSourceDocNumber(final String sourceDocNumber) {
        this.sourceDocNumber = sourceDocNumber;
    }

    public Date getSourceDocDate() {
        return sourceDocDate;
    }

    public void setSourceDocDate(final Date sourceDocDate) {
        this.sourceDocDate = sourceDocDate;
    }

    public String getRefundDocNumber() {
        return refundDocNumber;
    }

    public void setRefundDocNumber(final String refundDocNumber) {
        this.refundDocNumber = refundDocNumber;
    }

    public Date getRefundDocDate() {
        return refundDocDate;
    }

    public void setRefundDocDate(final Date refundDocDate) {
        this.refundDocDate = refundDocDate;
    }

    public boolean isCalculate() {
        return isCalculate;
    }

    public void setIsCalculate(final boolean isCalculate) {
        this.isCalculate = isCalculate;
    }
}
