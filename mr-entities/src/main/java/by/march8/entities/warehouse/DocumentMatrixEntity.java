package by.march8.entities.warehouse;

import by.march8.api.BaseEntity;
import by.march8.api.MarchDataSourceEnum;
import by.march8.api.RedirectToEntityManager;
import by.march8.api.TableHeader;

import javax.persistence.*;
import java.util.Date;

@NamedQueries({
        @NamedQuery(name = "DocumentMatrixEntity.findAll",
                query = "SELECT item FROM DocumentMatrixEntity item " +
                        "ORDER BY item.date")
})

@Entity
@Table(name="DOCUMENT_MATRIX")
@RedirectToEntityManager(redirectTo = MarchDataSourceEnum.DS_SQLMARCH8)
public class DocumentMatrixEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="ID")
    private int id ;

    @Column(name="NUMBER")
    private String number ;

    @Column(name="DATE")
    @Temporal(value = TemporalType.DATE)
    private Date date ;

    @Column(name="PERIOD_BEGIN")
    @Temporal(value = TemporalType.DATE)
    private Date periodBegin ;

    @Temporal(value = TemporalType.DATE)
    @Column(name="PERIOD_END")
    private Date periodEnd ;

    @Column(name="CONTRACTORS")
    private String contractors ;

    @Column(name="MODELS")
    private String models ;

    @Column(name="NOTE")
    private String note ;


    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    @TableHeader(name = "Номер документа", sequence = 10)
    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @TableHeader(name = "Период отбора данных_Начало", sequence = 20)
    public Date getPeriodBegin() {
        return periodBegin;
    }

    public void setPeriodBegin(Date periodBegin) {
        this.periodBegin = periodBegin;
    }
    @TableHeader(name = "Период отбора данных_Окончание", sequence = 30)
    public Date getPeriodEnd() {
        return periodEnd;
    }

    public void setPeriodEnd(Date periodEnd) {
        this.periodEnd = periodEnd;
    }

   // @TableHeader(name = "Контрагенты", sequence = 100)
    public String getContractors() {
        return contractors;
    }

    public void setContractors(String contractors) {
        this.contractors = contractors;
    }

    //@TableHeader(name = "Модели", sequence = 200)
    public String getModels() {
        return models;
    }

    public void setModels(String models) {
        this.models = models;
    }

    @TableHeader(name = "Примечание", sequence = 300)
    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
