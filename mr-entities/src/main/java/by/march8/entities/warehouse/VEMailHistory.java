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
        @NamedQuery(name = "VEMailHistory.findByDocument",
                query = "SELECT history FROM VEMailHistory history WHERE history.type = :type " +
                        "ORDER BY history.operationDate")
})

@Entity
@Table(name="V_EMAIL_HISTORY")
@RedirectToEntityManager(redirectTo = MarchDataSourceEnum.DS_SQLMARCH8)
public class VEMailHistory {

    @Id
    @Column(name="ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name="OPERATION_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    @TableHeader(name = "Дата", sequence = 1)
    private Date operationDate;

    @Column(name="NUMBER_DOCUMENT")
    @TableHeader(name = "Документ", sequence = 2)
    private String documentNumber;

    @Column(name="NOTE")
    @TableHeader(name = "E-Mail адрес отправки", sequence = 3)
    private String eMail ;

    @Column(name = "OPERATION_TYPE")
    private int type ;


    public int getId() {
        return id;
    }

    public void setId(final int id) {
        this.id = id;
    }

    public String getDocumentNumber() {
        return documentNumber;
    }

    public void setDocumentNumber(final String documentNumber) {
        this.documentNumber = documentNumber;
    }

    public String geteMail() {
        return eMail;
    }

    public void seteMail(final String eMail) {
        this.eMail = eMail;
    }

    public Date getOperationDate() {
        return operationDate;
    }

    public void setOperationDate(final Date operationDate) {
        this.operationDate = operationDate;
    }

    public int getType() {
        return type;
    }

    public void setType(final int type) {
        this.type = type;
    }
}
