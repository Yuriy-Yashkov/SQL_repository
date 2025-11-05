package by.march8.entities.product;

import by.march8.api.BaseEntity;
import by.march8.api.RedirectToEntityManager;
import by.march8.api.TableHeader;
import by.march8.api.MarchDataSourceEnum;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by lidashka.
 */
@Entity
@Table(name = "MOD_PROTOCOL")
@RedirectToEntityManager(redirectTo = MarchDataSourceEnum.DS_SQLSERVER)

public class Protocol extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "CODE")
    @TableHeader(name = "№", width = 100, sequence = 0)
    private String code;

    @Column(name = "PROTOCOL_DATE")
    @Temporal(TemporalType.DATE)
    @TableHeader(name = "Дата", width = 100, sequence = 1)
    private Date date;

    @Column(name = "NOTE")
    @TableHeader(name = "Примечание", width = 100, sequence = 2)
    private String note;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    @Override
    public String toString() {
        return "№" + code + " от " + date ;
    }

    public String getOrderByField(){
        return code ;
    }
}
