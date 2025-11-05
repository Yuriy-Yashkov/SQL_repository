package by.march8.entities.readonly;

import by.march8.api.BaseEntity;
import by.march8.api.MarchDataSourceEnum;
import by.march8.api.RedirectToEntityManager;
import by.march8.api.TableHeader;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Контрагенты программы March8
 *
 * @author Andy on 13.08.2015.
 */
@Entity
@Table(name = "S_KLIENT")
@RedirectToEntityManager(redirectTo = MarchDataSourceEnum.DS_SQLMARCH8)
public class ContractorEntity extends BaseEntity {

    @Id
    @Column(name = "ITEM_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @TableHeader(name = "ID", width = -50, sequence = 0)
    private int id;


    @Column(name = "KOD")
    @TableHeader(name = "Код", width = -50, sequence = 1)
    private int code;

    @Column(name = "NAIM")
    @TableHeader(name = "Наименование", sequence = 2)
    private String name;

    @Column(name = "VID")
    private int type;

    @Column(name = "REZIDENT")
    private int resident;

    @Column(name ="UNN")
    private String codeUNN ;

    @Column(name ="KT")
    private int regionCode ;

    @OneToMany(mappedBy = "contractor", cascade = CascadeType.ALL, orphanRemoval = true,fetch = FetchType.EAGER)
    private List<AddressEntity> addressList = new ArrayList<>() ;

    @OneToOne(fetch = FetchType.EAGER)
   // @PrimaryKeyJoinColumn
    @JoinColumn(name = "POSTADRES")
    private AddressEntity postAddress ;

    @OneToOne(fetch = FetchType.EAGER)
   // @PrimaryKeyJoinColumn
    @JoinColumn(name = "URADRES")
    private AddressEntity legalAddress ;


    @OneToMany(mappedBy = "contractor", orphanRemoval = true,fetch = FetchType.EAGER)
    private List<ContractEntity> contractList = new ArrayList<>() ;

    @OneToOne(fetch = FetchType.EAGER)
    @PrimaryKeyJoinColumn
    @JoinColumn(name = "DOGOVOR", nullable = true)
    private ContractEntity generalContract;

    @Column(name = "SKIDKA")
    private float discountValue;

    @Column(name = "FULLNAIM")
    private String fullName;


    public String getName() {
        if(name!=null) {
            return name.trim();
        }else{
            return  "" ;
        }
    }

    public void setName(final String name) {
        this.name = name;
    }

    public int getCode() {
        return code;
    }

    public void setCode(final int code) {
        this.code = code;
    }

    public int getType() {
        return type;
    }

    public void setType(final int type) {
        this.type = type;
    }

    public int getResident() {
        return resident;
    }

    public void setResident(final int resident) {
        this.resident = resident;
    }

    public String getCodeUNN() {
        return codeUNN;
    }

    public void setCodeUNN(final String codeUNN) {
        this.codeUNN = codeUNN;
    }

    /*
    public List<AddressMarch8> getAddressList() {
        return addressList;
    }

    public void setAddressList(final List<AddressMarch8> addressList) {
        this.addressList = addressList;
    }

    public AddressMarch8 getPostAddress() {
        return postAddress;
    }

    public void setPostAddress(final AddressMarch8 postAddress) {
        this.postAddress = postAddress;
    }

    public AddressMarch8 getLegalAddress() {
        return legalAddress;
    }

    public void setLegalAddress(final AddressMarch8 legalAddress) {
        this.legalAddress = legalAddress;
    }
*/



    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(final int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return name.trim();
    }

    public float getDiscountValue() {
        return discountValue;
    }

    public void setDiscountValue(final float discountValue) {
        this.discountValue = discountValue;
    }

/*    public List<ContractMarch8> getContractList() {
        return contractList;
    }

    public void setContractList(final List<ContractMarch8> contractList) {
        this.contractList = contractList;
    }

    public ContractMarch8 getGeneralContract() {
        return generalContract;
    }

    public void setGeneralContract(final ContractMarch8 generalConract) {
        this.generalContract = generalConract;
    }*/

    public List<AddressEntity> getAddressList() {
        return addressList;
    }

    public void setAddressList(final List<AddressEntity> addressList) {
        this.addressList = addressList;
    }

    public AddressEntity getPostAddress() {

        return postAddress;
    }

    public void setPostAddress(final AddressEntity postAddress) {
        this.postAddress = postAddress;
    }

    public AddressEntity getLegalAddress() {
        return legalAddress;
    }

    public void setLegalAddress(final AddressEntity legalAddress) {
        this.legalAddress = legalAddress;
    }


    public List<ContractEntity> getContractList() {
        return contractList;
    }

    public void setContractList(final List<ContractEntity> contractList) {
        this.contractList = contractList;
    }


    public ContractEntity getGeneralContract() {
        return generalContract;
    }

    public void setGeneralContract(final ContractEntity generalContract) {
        this.generalContract = generalContract;
    }

    public void printAllAddress() {
        for(AddressEntity address: getAddressList()){
            System.out.println(address.toString()) ;
        }
    }


    public void printAllContracts() {
        for(ContractEntity address: getContractList()){
            System.out.println(address.toString()) ;
        }
    }


    public int getRegionCode() {
        return regionCode;
    }

    public void setRegionCode(final int regionCode) {
        this.regionCode = regionCode;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(final String fullName) {
        this.fullName = fullName;
    }
}
