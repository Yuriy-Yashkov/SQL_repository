package by.march8.entities.warehouse;

import by.march8.api.RedirectToEntityManager;
import by.march8.api.MarchDataSourceEnum;

import javax.persistence.*;
import java.util.Date;

@NamedQueries({
        @NamedQuery(name = "AdjustmentSaleDocument.findByDocumentId",
                query = "SELECT item FROM AdjustmentSaleDocument item WHERE item.currentDocumentId = :document ")
})

/**
 * @author Andy 16.01.2019 - 12:42.
 */
@Entity
@Table(name="OTGRUZ_ADJUSTMENT_DOCUMENT")
@RedirectToEntityManager(redirectTo = MarchDataSourceEnum.DS_SQLMARCH8)
public class AdjustmentSaleDocument {

    @Id
    @Column(name="ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id ;

    @Column(name = "CURRENT_DOCUMENT_NUMBER")
    private int currentDocumentId ;
    @Column(name = "OLD_DOCUMENT_NUMBER")
    private int oldDocumentId ;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "OPERATION_DATE")
    private Date adjustmentDate;

    @Column(name="ADJUSTMENT_CAUSE")
    private String cause ;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCurrentDocumentId() {
        return currentDocumentId;
    }

    public void setCurrentDocumentId(int currentDocumentId) {
        this.currentDocumentId = currentDocumentId;
    }

    public int getOldDocumentId() {
        return oldDocumentId;
    }

    public void setOldDocumentId(int oldDocumentId) {
        this.oldDocumentId = oldDocumentId;
    }

    public Date getAdjustmentDate() {
        return adjustmentDate;
    }

    public void setAdjustmentDate(Date adjustmentDate) {
        this.adjustmentDate = adjustmentDate;
    }

    public String getCause() {
        return cause;
    }

    public void setCause(String cause) {
        this.cause = cause;
    }
}
