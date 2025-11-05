package by.march8.entities.storage;

import by.march8.api.BaseEntity;
import by.march8.api.RedirectToEntityManager;
import by.march8.api.MarchDataSourceEnum;

import javax.persistence.*;
import java.util.Date;

/**
 * @author Andy 03.11.2018 - 7:45.
 */

@Entity
@Table(name = "places1")
@RedirectToEntityManager(redirectTo = MarchDataSourceEnum.DS_SQLMARCH8)
public class StorageLocationsItem extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id")
    private int id;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "data")
    private Date documentDate;

    @Column(name = "ndoc")
    private int documentNumber;

    @Column(name = "podr_id")
    private int departmentCode;

    @Column(name = "prim")
    private String note;

    @Column(name = "kola")
    private int amountAll;

    @Column(name = "kolk")
    private int amountPack;

    @Column(name = "kolr")
    private int amountUnPack;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "datevrkv")
    private Date datevrkv;

    @Column(name = "uservrkv")
    private String uservrkv;

    @Column(name = "kpodvrkv")
    private String kpodvrkv;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "datekrkv")
    private Date datekrkv;

    @Column(name = "userkrkv")
    private String userkrkv;

    @Column(name = "kpodkrkv")
    private String kpodkrkv;


    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    public Date getDocumentDate() {
        return documentDate;
    }

    public void setDocumentDate(Date date) {
        this.documentDate = date;
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

    public String getNote() {
        if (note != null) {
            return note;
        } else {
            return "";
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

    public Date getDatevrkv() {
        return datevrkv;
    }

    public void setDatevrkv(Date datevrkv) {
        this.datevrkv = datevrkv;
    }

    public String getUservrkv() {
        return uservrkv;
    }

    public void setUservrkv(String uservrkv) {
        this.uservrkv = uservrkv;
    }

    public String getKpodvrkv() {
        return kpodvrkv;
    }

    public void setKpodvrkv(String kpodvrkv) {
        this.kpodvrkv = kpodvrkv;
    }

    public Date getDatekrkv() {
        return datekrkv;
    }

    public void setDatekrkv(Date datekrkv) {
        this.datekrkv = datekrkv;
    }

    public String getUserkrkv() {
        return userkrkv;
    }

    public void setUserkrkv(String userkrkv) {
        this.userkrkv = userkrkv;
    }

    public String getKpodkrkv() {
        return kpodkrkv;
    }

    public void setKpodkrkv(String kpodkrkv) {
        this.kpodkrkv = kpodkrkv;
    }
}
