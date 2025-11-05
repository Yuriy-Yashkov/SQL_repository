package by.march8.entities.warehouse;

import by.march8.api.BaseEntity;
import by.march8.api.MarchDataSourceEnum;
import by.march8.api.RedirectToEntityManager;
import by.march8.api.TableHeader;

import javax.persistence.*;
import java.util.Date;

@NamedQueries({
        @NamedQuery(name = "SaleDocumentView.findAllPerPeriodAndContractor",
                query = "SELECT doc FROM SaleDocumentView doc WHERE doc.documentDate BETWEEN :periodBegin AND :periodEnd " +
                        "AND doc.contractorId = :contractor " +
                        "order by " +
                        "doc.documentDate ASC")
})


@Entity
@Table(name = "VIEW_SALE_DOCUMENT_LIST")
@RedirectToEntityManager(redirectTo = MarchDataSourceEnum.DS_SQLMARCH8)
public class SaleDocumentView extends BaseEntity {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "DOCUMENT_NUMBER")
    private String documentNumber;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "DOCUMENT_DATE")
    private Date documentDate;

    @Column(name = "DOCUMENT_TYPE")
    private String documentType;

    @Column(name = "DOCUMENT_STATUS")
    private int documentStatus;

    @Column(name = "EXPORT")
    private int export;

    @Column(name = "CONTRACTOR_CODE")
    private int contractorCode;

    @Column(name = "CONTRACTOR_ID")
    private int contractorId;

    @Column(name = "CONTRACTOR_NAME")
    private String contractorName;

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    @TableHeader(name = "№ документа", sequence = 20)
    public String getDocumentNumber() {
        return documentNumber;
    }

    public void setDocumentNumber(String documentNumber) {
        this.documentNumber = documentNumber;
    }
    @TableHeader(name = "Дата", width = -80, sequence = 10)
    public Date getDocumentDate() {
        return documentDate;
    }

    public void setDocumentDate(Date documentDate) {
        this.documentDate = documentDate;
    }

    @TableHeader(name = "Тип документа", sequence = 30)
    public String getDocumentType() {
        return documentType;
    }

    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }

    @TableHeader(name = "Статус", width = 0 , sequence = 9999)
    public int getDocumentStatus() {
        return documentStatus;
    }

    public void setDocumentStatus(int documentStatus) {
        this.documentStatus = documentStatus;
    }

    //@TableHeader(name = "Контрагент_Код", sequence = 100)
    public int getContractorCode() {
        return contractorCode;
    }

    public void setContractorCode(int contractorCode) {
        this.contractorCode = contractorCode;
    }

    public int getContractorId() {
        return contractorId;
    }

    public void setContractorId(int contractorId) {
        this.contractorId = contractorId;
    }

    //@TableHeader(name = "Контрагент_Наименование", sequence = 120)
    public String getContractorName() {
        return contractorName;
    }

    public void setContractorName(String contractorName) {
        this.contractorName = contractorName;
    }


    public int getExport() {
        return export;
    }

    public void setExport(int export) {
        this.export = export;
    }
}
