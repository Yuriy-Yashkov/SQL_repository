package by.march8.entities.readonly;

import by.march8.api.BaseEntity;
import by.march8.api.MarchDataSourceEnum;
import by.march8.api.RedirectToEntityManager;
import by.march8.api.TableHeader;

import javax.persistence.*;


@NamedQueries({
        @NamedQuery(name = "ContractorEntityView.findAll",
                query = "SELECT contractor FROM ContractorEntityView contractor  " +
                        "WHERE contractor.contractId > 0 "+
                        "order by contractor.code "),

        @NamedQuery(name = "ContractorEntityView.findAllOrderBy",
                query = "SELECT contractor FROM ContractorEntityView contractor  " +
                        "order by contractor.name ")
})

/**
 * Контрагенты программы March8
 *
 * @author Andy on 13.08.2015.
 */
@Entity
@Table(name = "S_KLIENT")
@RedirectToEntityManager(redirectTo = MarchDataSourceEnum.DS_SQLMARCH8)
public class ContractorEntityView extends BaseEntity {

    @Id
    @Column(name = "ITEM_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    //@TableHeader(name = "ID", width = -50, sequence = 11)
    private int id;


    @Column(name = "KOD")
    @TableHeader(name = "Код", width = -70, sequence = 1)
    private int code;

    @Column(name = "NAIM")
    private String name;

    @Column(name = "REZIDENT")
    private int resident;

    @Column(name = "SKIDKA")
    private float discountValue;

    @Column(name = "DOGOVOR")
    private Integer contractId ;

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(final int id) {
        this.id = id;
    }

    public int getCode() {
        return code;
    }

    public void setCode(final int code) {
        this.code = code;
    }

    @TableHeader(name = "Наименование", sequence = 2)
    public String getName() {
        if(name!=null) {
            return name.trim();
        }
        return null ;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public int getResident() {
        return resident;
    }

    public void setResident(final int resident) {
        this.resident = resident;
    }

    public float getDiscountValue() {
        return discountValue;
    }

    public void setDiscountValue(final float discountValue) {
        this.discountValue = discountValue;
    }


    @Override
    public String toString() {
        return name.trim();
    }

    public Integer getContractId() {
        if(contractId==null){
            contractId = 0 ;
        }
        return contractId;
    }

    public void setContractId(final Integer contractId) {
        this.contractId = contractId;
    }
}
