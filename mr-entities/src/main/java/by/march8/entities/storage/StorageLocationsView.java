package by.march8.entities.storage;

import by.march8.api.BaseEntity;
import by.march8.api.RedirectToEntityManager;
import by.march8.api.TableHeader;
import by.march8.api.MarchDataSourceEnum;

import javax.persistence.*;
import java.util.Date;

/**
 * @author Andy 03.11.2018 - 7:45.
 */

@NamedQueries({
        @NamedQuery(name = "StorageLocationsView.findByPeriod",
                query = "SELECT items FROM StorageLocationsView items WHERE items.date between :dateBegin and :dateEnd " +
                        "order by items.documentNumber")
})

@Entity
@Table(name = "v_places1")
@RedirectToEntityManager(redirectTo = MarchDataSourceEnum.DS_SQLMARCH8)
public class StorageLocationsView extends BaseEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id")
    private int id ;

    @Temporal(TemporalType.TIMESTAMP)
    @TableHeader(name = "Документ_Дата", width = -80, sequence = 10)
    @Column(name="data")
    private Date date ;

    @TableHeader(name = "Документ_Номер", width = -80, sequence = 20)
    @Column(name = "ndoc")
    private int documentNumber;

    @TableHeader(name = "Подразделение_Код", width = 80, sequence = 30)
    @Column(name = "podr_id")
    private int departmentCode;

    @TableHeader(name = "Подразделение_Наименование", sequence = 40)
    @Column(name = "naim")
    private String departmentName;

    @TableHeader(name = "Примечание", sequence = 40)
    @Column(name = "prim")
    private String note;

    @Column(name="kola")
    private int amountAll;

    @Column(name="kolk")
    private int amountPack;

    @Column(name="kolr")
    private int amountUnPack;

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getDocumentNumber() {
        return documentNumber;
    }

    public void setDocumentNumber(int documentNumber) {
        this.documentNumber = documentNumber;
    }

    public int getDepartmentCode() {
        return departmentCode;
    }

    public void setDepartmentCode(int departmentCode) {
        this.departmentCode = departmentCode;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getNote() {
        if(note!=null) {
            return note.trim();
        }else{
            return "" ;
        }
    }

    public void setNote(String note) {
        this.note = note;
    }

    public int getAmountAll() {
        return amountAll;
    }

    public void setAmountAll(int amountAll) {
        this.amountAll = amountAll;
    }

    public int getAmountPack() {
        return amountPack;
    }

    public void setAmountPack(int amountPack) {
        this.amountPack = amountPack;
    }

    public int getAmountUnPack() {
        return amountUnPack;
    }

    public void setAmountUnPack(int amountUnPack) {
        this.amountUnPack = amountUnPack;
    }
}
