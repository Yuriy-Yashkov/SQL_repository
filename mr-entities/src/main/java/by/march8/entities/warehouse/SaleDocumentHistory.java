package by.march8.entities.warehouse;

import by.march8.api.BaseEntity;
import by.march8.api.RedirectToEntityManager;
import by.march8.api.MarchDataSourceEnum;

import javax.persistence.*;
import java.util.Date;

/**
 * @author Andy 13.06.2016.
 */
@Entity
@Table(name = "HISTORY_SALE_DOCUMENT")
@RedirectToEntityManager(redirectTo = MarchDataSourceEnum.DS_SQLMARCH8)
public class SaleDocumentHistory extends BaseEntity{

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id ;

    @Column(name = "OPERATION_TYPE")
    private int operationId;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "OPERATION_DATE")
    private Date operationDate ;

    @Column(name = "REF_DOCUMENT")
    private int documentId ;

    @Column(name="NUMBER_DOCUMENT")
    private String documentNumber ;

    @Column(name = "NOTE")
    private String note ;


    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(final int id) {
        this.id = id;
    }

    public int getOperationId() {
        return operationId;
    }

    public void setOperationId(final int operationId) {
        this.operationId = operationId;
    }

    public Date getOperationDate() {
        return operationDate;
    }

    public void setOperationDate(final Date operationDate) {
        this.operationDate = operationDate;
    }

    public int getDocumentId() {
        return documentId;
    }

    public void setDocumentId(final int documentId) {
        this.documentId = documentId;
    }

    public String getDocumentNumber() {
        return documentNumber;
    }

    public void setDocumentNumber(final String documentNumber) {
        this.documentNumber = documentNumber;
    }

    public String getNote() {
        return note;
    }

    public void setNote(final String note) {
        this.note = note;
    }
}
