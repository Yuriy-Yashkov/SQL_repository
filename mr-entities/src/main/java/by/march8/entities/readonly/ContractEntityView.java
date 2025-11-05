package by.march8.entities.readonly;

import by.march8.api.BaseEntity;
import by.march8.api.RedirectToEntityManager;
import by.march8.api.TableHeader;
import by.march8.api.utils.DateUtils;
import by.march8.api.MarchDataSourceEnum;

import javax.persistence.*;
import java.util.Date;



/**
 * @author Andy 11.12.2015.
 */
@Entity
@Table(name = "s_dogovor")
@RedirectToEntityManager(redirectTo = MarchDataSourceEnum.DS_SQLMARCH8)
public class ContractEntityView extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @TableHeader(name="Код",width = -80, sequence = 0)
    @Column(name = "ITEM_ID")
    private int id;

    @Column(name = "NAIM")
    @TableHeader(name="Наименование договора_Тип документа", sequence = 1)
    private String name;

    @Column(name = "NOMER")
    @TableHeader(name="Наименование договора_Номер документа", sequence = 2)
    private String number;

    @Column(name = "DATA")
    @TableHeader(name="Период действия договора_Начало",width = -150, sequence = 3)
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;

    @Column(name = "DATA_END")
    @TableHeader(name="Период действия договора_Окончание",width = -150, sequence = 4)
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateOut;


    @Column(name = "KLIENT_ID")
    private int contractorId;

    @Override
    public String toString() {
        if (name!=null){
        return name.trim() + " №" + number.trim() + " от " + DateUtils.getNormalDateFormat(date) + "г.";}else{
            return "";
        }
    }

    public int getId() {
        return id;
    }

    public void setId(final int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(final String number) {
        this.number = number;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(final Date date) {
        this.date = date;
    }

    public Date getDateOut() {
        return dateOut;
    }

    public void setDateOut(final Date dateOut) {
        this.dateOut = dateOut;
    }

    public int getContractorId() {
        return contractorId;
    }

    public void setContractorId(final int contractorId) {
        this.contractorId = contractorId;
    }
}
