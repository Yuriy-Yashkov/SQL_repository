package by.march8.entities.warehouse;

import by.march8.api.BaseEntity;
import by.march8.api.RedirectToEntityManager;
import by.march8.api.MarchDataSourceEnum;

import javax.persistence.*;

/**
 * @author Andy 08.08.2018 - 7:05.
 */
@Entity
@Table(name = "otgruz1")
@RedirectToEntityManager(redirectTo = MarchDataSourceEnum.DS_SQLMARCH8)
public class SaleDocumentContractInformation extends BaseEntity {

    @Id
    @Column(name = "item_id")
    private int id ;

    @Column(name="ndoc")
    private String documentNumber;

    @Transient
    private int contractorId;

    @Column(name="dogovor_id")
    private int contractId;

    @Column(name="saved")
    private boolean isCalculate ;

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    public String getDocumentNumber() {
        return documentNumber;
    }

    public void setDocumentNumber(String documentNumber) {
        this.documentNumber = documentNumber;
    }

    public int getContractorId() {
        return contractorId;
    }

    public void setContractorId(int contractorId) {
        this.contractorId = contractorId;
    }

    public int getContractId() {
        return contractId;
    }

    public void setContractId(int contractId) {
        this.contractId = contractId;
    }

    public boolean isCalculate() {
        return isCalculate;
    }

    public void setIsCalculate(boolean isCalculate) {
        this.isCalculate = isCalculate;
    }
}
