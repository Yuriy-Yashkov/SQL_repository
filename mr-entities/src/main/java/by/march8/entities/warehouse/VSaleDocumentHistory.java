package by.march8.entities.warehouse;

import by.march8.api.RedirectToEntityManager;
import by.march8.api.TableHeader;
import by.march8.api.MarchDataSourceEnum;

import javax.persistence.*;
import java.util.Date;

/**
 * @author Andy 15.11.2017.
 */


@NamedQueries({
        @NamedQuery(name = "VSaleDocumentHistory.findByDocumentId",
                query = "SELECT history FROM VSaleDocumentHistory history WHERE history.documentId = :document " +
                        "order by history.date")
})

@Entity
@Table(name="V_HISTORY_SALE_DOCUMENT")
@RedirectToEntityManager(redirectTo = MarchDataSourceEnum.DS_SQLMARCH8)
public class VSaleDocumentHistory {

    @Id
    @Column(name = "ID")
    private int id ;

    @Temporal(TemporalType.TIMESTAMP)
    @TableHeader(name = "Дата", sequence = 1)
    @Column(name="OPERATION_DATE")
    private Date date ;

    @Column(name = "NAME")
    @TableHeader(name = "Действие над документом", sequence = 3)
    private String operation ;

    @Column(name = "REF_DOCUMENT")
    private int documentId;

    @Column(name = "NOTE")
    @TableHeader(name = "Примечание", sequence = 4)
    private String note ;

    @Column(name = "NUMBER_DOCUMENT")
    @TableHeader(name = "Номер документа", sequence = 2)
    private String documentNumber ;


    public int getId() {
        return id;
    }

    public void setId(final int id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(final Date date) {
        this.date = date;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(final String operation) {
        this.operation = operation;
    }

    public int getDocumentId() {
        return documentId;
    }

    public void setDocumentId(final int documentId) {
        this.documentId = documentId;
    }

    public String getNote() {
        return note;
    }

    public void setNote(final String note) {
        this.note = note;
    }

    public String getDocumentNumber() {
        return documentNumber;
    }

    public void setDocumentNumber(final String documentNumber) {
        this.documentNumber = documentNumber;
    }
}
