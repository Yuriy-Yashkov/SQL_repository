package by.march8.entities.planning;

import by.march8.api.BaseEntity;
import by.march8.api.RedirectToEntityManager;
import by.march8.api.TableHeader;
import by.march8.api.utils.DateUtils;
import by.march8.api.MarchDataSourceEnum;

import javax.persistence.*;
import java.util.Date;

/**
 * @author Andy 27.11.2018 - 7:50.
 */

@NamedQueries({
        @NamedQuery(name = "ProductionPlanningView.findByPeriod",
                query = "SELECT items FROM ProductionPlanningView items WHERE items.documentDate between :dateBegin and :dateEnd " +
                        "order by items.documentDate")
})

@Entity
@Table(name = "MOD_PLANNING_DOCUMENT")
@RedirectToEntityManager(redirectTo = MarchDataSourceEnum.DS_SQLMARCH8)
public class ProductionPlanningView extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    @TableHeader(name = "ID", width = -50, sequence = 10)
    private int id;

    @Column(name = "DOCUMENT_NUMBER")
    private String documentNumber;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "DOCUMENT_DATE")
    private Date documentDate;

    @Column(name = "STATUS")
    private int status;

    @Column(name = "DOCUMENT_TYPE")
    private int documentType;

    @Column(name = "NOTE")
    private String note;


    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    @TableHeader(name = "Документ_Номер", sequence = 20)
    public String getDocumentNumber() {
        if (documentNumber != null) {
            return documentNumber.trim();
        } else {
            return "";
        }
    }

    public void setDocumentNumber(String documentNumber) {
        this.documentNumber = documentNumber;
    }

    @TableHeader(name = "Документ_Дата", sequence = 30)
    public Date getDocumentDate() {
        return documentDate;
    }

    public void setDocumentDate(Date documentDate) {
        this.documentDate = documentDate;
    }

    @TableHeader(name = "Статус", width = -50, sequence = 50)
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @TableHeader(name = "Документ_Тип", width = -50, sequence = 40)
    public int getDocumentType() {
        return documentType;
    }

    public void setDocumentType(int documentType) {
        this.documentType = documentType;
    }

    @TableHeader(name = "Примечание", sequence = 100)
    public String getNote() {
        if (note != null) {
            return note.trim();
        } else {
            return "";
        }
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getDocumentInformation(){
        return "№"+getDocumentNumber()+" от "+ DateUtils.getNormalDateFormat(getDocumentDate())+" г." ;
    }
}
