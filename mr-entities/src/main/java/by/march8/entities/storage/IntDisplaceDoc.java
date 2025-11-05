package by.march8.entities.storage;

import by.march8.api.RedirectToEntityManager;
import by.march8.api.TableHeader;
import by.march8.api.utils.DateUtils;
import by.march8.api.MarchDataSourceEnum;

import javax.persistence.*;
import java.util.Date;

/**
 * Бин внутреннего перемещения
 * Created by Andy on 16.12.2014.
 */
//@Entity
@Table(name = "vnperem1")
@RedirectToEntityManager(redirectTo = MarchDataSourceEnum.DS_SQLMARCH8)
public class IntDisplaceDoc {
    @Id
    @Column(name = "item_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @TableHeader(name = "ID",sequence = 0, width = -50)
    private int id ;

    @Column(name = "date")
    @Temporal(TemporalType.TIMESTAMP)
    @TableHeader(name = "Документ_Дата",sequence = 2,width = -70)
    private Date date;

    @Column(name = "ndoc")
    @TableHeader(name = "Документ_Номер",sequence = 1,width = -80)
    private String number ;

    @Column(name = "operac")
    @TableHeader(name = "Операция",sequence = 3, width = 150)
    private String operation ;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "kpodot")
    private NsiTeam deptSource;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="kpodto")
    private NsiTeam deptDest ;

    @Column(name="status")
    @TableHeader(name = "Состояние",sequence = 11,width = 0)
    private int status ;

    @Column(name="kola")
    @TableHeader(name = "Количество_Всего",sequence = 8,width = -80)
    private int numberAll ;

    @Column(name="kolk")
    @TableHeader(name = "Количество_Упакова",sequence = 9,width = -80)
    private int numberPack ;
    @Column(name="kolr")
    @TableHeader(name = "Количество_Россыпью",sequence = 10,width = -80)
    private int numberLoose ;

    public int getId() {
        return id;
    }

    @TableHeader(name = "Отправитель_Наименование",sequence = 4)
    public String getDeptSourceName(){
        if (getDeptSource()!=null) {
            return getDeptSource().getName();
        }
        return "" ;
    }

    @TableHeader(name = "Отправитель_Код",sequence = 5,width = -50)
    public String getDeptSourceCode(){
        if (getDeptSource()!=null) {
            return String.valueOf(getDeptSource().getId());
        }
        return "" ;
    }

    @TableHeader(name = "Получатель_Наименование",sequence = 6)
    public String getDeptDestName(){
        if (getDeptDest()!=null) {
            return getDeptDest().getName();
        }
        return "" ;
    }

    @TableHeader(name = "Получатель_Код",sequence = 7,width = -50)
    public String getDeptDestCode(){
        if (getDeptDest()!=null) {
            return String.valueOf(getDeptDest().getId());
        }
        return "" ;
    }

    public void setId(final int id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(final Date date) {
        this.date = date;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(final String number) {
        this.number = number;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(final String operation) {
        this.operation = operation;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(final int status) {
        this.status = status;
    }

    public int getNumberAll() {
        return numberAll;
    }

    public void setNumberAll(final int numberAll) {
        this.numberAll = numberAll;
    }

    public int getNumberPack() {
        return numberPack;
    }

    public void setNumberPack(final int numberPack) {
        this.numberPack = numberPack;
    }

    public int getNumberLoose() {
        return numberLoose;
    }

    public void setNumberLoose(final int numberLoose) {
        this.numberLoose = numberLoose;
    }

    public NsiTeam getDeptSource() {
        return deptSource;
    }

    public void setDeptSource(final NsiTeam reptSource) {
        this.deptSource = reptSource;
    }

    public NsiTeam getDeptDest() {
        return deptDest;
    }

    public void setDeptDest(final NsiTeam deptDest) {
        this.deptDest = deptDest;
    }

    @Override
    public String toString() {
        return number.trim()+" от "+ DateUtils.getNormalDateFormat(getDate());
    }
}
