package by.march8.entities.planning;

import by.march8.api.BaseEntity;
import by.march8.api.RedirectToEntityManager;
import by.march8.api.MarchDataSourceEnum;

import javax.persistence.*;
import java.util.Date;

/**
 * @author Andy 06.12.2018 - 9:46.
 */
@Entity
@Table(name = "MOD_PLANNING_DOCUMENT")
@RedirectToEntityManager(redirectTo = MarchDataSourceEnum.DS_SQLMARCH8)
public class ProductionPlanningEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="ID")
    private int id ;

    @Column(name="DOCUMENT_NUMBER")
    private String documentNumber ;

    @Column(name="DOCUMENT_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date documentDate;


    @Column(name = "DOCUMENT_TYPE")
    private int documentType ;

    @Column(name="STATUS")
    private int status ;

    @Column(name = "NOTE")
    private String note ;

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    public String getDocumentNumber() {
        if(documentNumber!=null) {
            return documentNumber.trim();
        }else{
            return "" ;
        }
    }

    public void setDocumentNumber(String documentNumber) {
        this.documentNumber = documentNumber;
    }

    public Date getDocumentDate() {
        return documentDate;
    }

    public void setDocumentDate(Date documentDate) {
        this.documentDate = documentDate;
    }

    public int getDocumentType() {
        return documentType;
    }

    public void setDocumentType(int documentType) {
        this.documentType = documentType;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getNote() {
        if(note!=null) {
            return note.trim();
        }else{
            return "";
        }
    }

    public void setNote(String note) {
        this.note = note;
    }


}
