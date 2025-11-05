package by.march8.entities.warehouse;

import by.march8.api.BaseEntity;
import by.march8.api.MarchDataSourceEnum;
import by.march8.api.RedirectToEntityManager;
import by.march8.entities.readonly.AddressEntity;
import by.march8.entities.readonly.ContractEntity;
import by.march8.entities.readonly.ContractorEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

/**
 * Класс описывающий путевой лист и реквизиты накладной.
 *
 * @author Andy 11.11.2015.
 */
@Entity
@Table(name = "DRIVING_DIRECTION_DOCUMENT")
@RedirectToEntityManager(redirectTo = MarchDataSourceEnum.DS_SQLMARCH8)
@Getter
@Setter
@NoArgsConstructor
public class SaleDocumentDriving extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private int id;

    @Column(name = "SALE_DOCUMENT_ID")
    private int saleDocumentId;

    @Column(name = "DOCUMENT_NUMBER", length = 50)
    private String documentNumber;

    @Column(name = "DOCUMENT_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date documentDate;

    @Column(name = "SHIPPER_ID", nullable = false)
    private int shipperId;

    @Column(name = "CONSIGNEE_ID", nullable = false)
    private int consigneeId;

    @Column(name = "LOADING_ADDRESS_ID", nullable = false)
    private int loadingAddressId;

    @Column(name = "UNLOADING_ADDRESS_ID", nullable = false)
    private int unloadingAddressId;

    @Column(name = "READDRESSING", length = 50)
    private String readdressing;

    @Column(name = "SALE_ALLOWED", length = 100)
    private String saleAllowed;

    @Column(name = "SHIPPER_PASSED", length = 100)
    private String shipperPassed;

    @Column(name = "TRANSPORTATION_RECEIVE", length = 100)
    private String transportationReceive;

    @Column(name = "SEAL_NUMBER_ID", nullable = false)
    private int sealNumberId = 1;

    @Column(name = "LOADING_DOER", nullable = false)
    private int loadingDoer;

    @Column(name = "LOADING_METHOD", nullable = false)
    private int loadingMethod;

    @Column(name = "SUPPORT_DOCUMENT", length = 100)
    private String supportDocument;

    @Column(name = "NOTE")
    private String note;

    @Column(name = "WARRANT_NUMBER", length = 50)
    private String warrantNumber;

    @Column(name = "WARRANT_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date warrantDate;

    @Column(name = "WARRANT_ISSUED", length = 100)
    private String warrantIssued;

    @Column(name = "WARRANT_RECEIVE", length = 100)
    private String warrantReceive;

    @Column(name = "WARRANT_SEAL_NUMBER", length = 50)
    private String warrantSealNumber;

    @Column(name = "CAR_NUMBER", length = 100)
    private String carNumber;

    @Column(name = "CAR_TRAILER_NUMBER", length = 100)
    private String carTrailerNumber;

    @Column(name = "CAR_DRIVER_NAME", length = 50)
    private String carDriverName;

    @Column(name = "CAR_OWNER", length = 100)
    private String carOwner;

    @Column(name = "CAR_PAYER", length = 50)
    private String carPayer;

    @Column(name = "CAR_CUSTOMER", length = 100)
    private String carCustomer;

    @Column(name = "DOCUMENT_TYPE", length = 50)
    private String documentType;

    @Transient
    private SaleDocumentDrivingAdditional additional = null;

    public SaleDocumentDriving(SaleDocumentDriving originalDriving) {
        this.documentDate = originalDriving.getDocumentDate();
        this.documentNumber = originalDriving.getDocumentNumber();
        this.shipperId = originalDriving.getShipperId();
        this.consigneeId = originalDriving.getConsigneeId();
        this.loadingAddressId = originalDriving.getLoadingAddressId();
        this.unloadingAddressId = originalDriving.getUnloadingAddressId();
        this.readdressing = originalDriving.getReaddressing();
        this.saleAllowed = originalDriving.getSaleAllowed();
        this.shipperPassed = originalDriving.getShipperPassed();
        this.transportationReceive = originalDriving.getTransportationReceive();
        this.sealNumberId = originalDriving.getSealNumberId();
        this.loadingDoer = originalDriving.getLoadingDoer();
        this.loadingMethod = originalDriving.getLoadingMethod();
        this.supportDocument = originalDriving.getSupportDocument();
        this.note = originalDriving.getNote();
        this.warrantNumber = originalDriving.getWarrantNumber();
        this.warrantDate = originalDriving.getWarrantDate();
        this.warrantIssued = originalDriving.getWarrantIssued();
        this.warrantReceive = originalDriving.getWarrantReceive();
        this.warrantSealNumber = originalDriving.getWarrantSealNumber();
        this.carNumber = originalDriving.getCarNumber();
        this.carTrailerNumber = originalDriving.getCarTrailerNumber();
        this.carDriverName = originalDriving.getCarDriverName();
        this.carOwner = originalDriving.getCarOwner();
        this.carPayer = originalDriving.getCarPayer();
        this.carCustomer = originalDriving.getCarCustomer();
        this.documentType = originalDriving.getDocumentType();
    }

    public static String getContractorAddressByAddressId(ContractorEntity contractorEntity, int addressId) {
        return contractorEntity.getAddressList().stream()
                .filter(Objects::nonNull)
                .filter(address -> address.getId() == addressId)
                .map(address -> address.getFullName().trim())
                .findFirst().orElse("");
    }

    public static AddressEntity getAddressEntityByAddressId(ContractorEntity contractorEntity, int addressId) {
        return contractorEntity.getAddressList().stream()
                .filter(Objects::nonNull)
                .filter(address -> address.getId() == addressId).findFirst().orElse(null);
    }

    /**
     * Возвращает структуру КОНТРАКТ
     *
     * @param contractorEntity контрагент
     * @param contractId       идентификатор контракта/договора
     * @return контракт/договор
     */
    public static ContractEntity getContract(ContractorEntity contractorEntity, int contractId) {
        return contractorEntity.getContractList().stream()
                .filter(Objects::nonNull)
                .filter(contract -> contract.getId() == contractId)
                .findFirst()
                .orElse(ContractEntity.builder()
                        .id(contractId)
                        .contractor(contractorEntity)
                        .date(new Date())
                        .dateOut(new Date())
                        .name("Нет данных")
                        .number("НЕТ ДАННЫХ")
                        .build());
    }

    public static String getLoadingDoerById(final int loadingDoer) {
        return "OAO \"8 Марта\"";
    }

    public static String getLoadingMethodById(final int loadingMethod) {
        return "ручн";
    }

    public static String getSealNumberById(final int sealNumber) {
        return sealNumber == 0 ? "\"8 Марта\" Склад №6": "8M/3";
    }

    public String getDocumentTypeOrDefault() {
        return Objects.nonNull(this.documentType) ? this.documentType : "По доверенности №";
    }
}
