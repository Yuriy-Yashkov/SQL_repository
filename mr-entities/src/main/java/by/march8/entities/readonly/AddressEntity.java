package by.march8.entities.readonly;

import by.march8.api.BaseEntity;

import javax.persistence.*;

/**
 * @ author Andy 23.10.2015.
 */
@Entity
@Table(name = "s_adres")
public class AddressEntity extends BaseEntity {

    @Id
    @Column(name = "ITEM_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "NAIM")
    private String fullName;
    @Column(name = "OBLAST")
    private String region;
    @Column(name = "RAION")
    private String district;
    @Column(name = "PINDEX")
    private String postIndex;
    @Column(name = "GOROD")
    private String city;
    @Column(name = "STREET")
    private String street;
    @Column(name = "DOM")
    private String houseNumber;
    @ManyToOne
    @JoinColumn(name = "KLIENT_ID")
    private ContractorEntity contractor;

    @Transient
    private String contractorName ;

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(final int id) {
        this.id = id;
    }

    public String getFullName() {
        if(fullName!=null) {
            return fullName.trim();
        }else{
            return "" ;
        }
    }

    public void setFullName(final String fullName) {
        this.fullName = fullName;
    }

    public String getRegion() {
        if(region!=null) {
            return region.trim();
        }
        return "";
    }

    public void setRegion(final String region) {
        this.region = region;
    }

    public String getDistrict() {
        if(district!=null) {
            return district.trim();
        }
        return "";
    }

    public void setDistrict(final String district) {
        this.district = district;
    }

    public String getPostIndex() {
        if(postIndex!=null) {
            return postIndex.trim();
        }
        return "";
    }

    public void setPostIndex(final String postIndex) {
        this.postIndex = postIndex;
    }

    public String getCity() {
        if(city!=null) {
            return city.trim();
        }
        return null;
    }

    public void setCity(final String city) {
        this.city = city;
    }

    public String getStreet() {
        if(street!=null) {
            return street.trim();
        }
        return "";
    }

    public void setStreet(final String street) {
        this.street = street;
    }

    public String getHouseNumber() {
        if(houseNumber!=null) {
            return houseNumber.trim();
        }
        return null;
    }

    public void setHouseNumber(final String houseNumber) {
        this.houseNumber = houseNumber;
    }

    public ContractorEntity getContractor() {
        return contractor;
    }

    public void setContractor(final ContractorEntity contractor) {
        this.contractor = contractor;
    }

    @Override
    public String toString() {
        if(contractor!=null) {
            if(contractorName==null) {
                return fullName.trim();
            }else{
                return getContractorName() + fullName.trim();
            }
        }else{
            return getContractorName() + fullName.trim();
        }
    }

    public String getContractorName() {
        if(contractorName==null){
            return "";
        }else {
            return contractorName.trim()+",";
        }
    }

    public void setContractorName(final String contractorName) {
        this.contractorName = contractorName;
    }
}
