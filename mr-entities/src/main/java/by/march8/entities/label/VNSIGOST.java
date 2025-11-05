package by.march8.entities.label;

import by.march8.api.BaseEntity;
import by.march8.api.RedirectToEntityManager;
import by.march8.api.TableHeader;
import by.march8.api.MarchDataSourceEnum;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by suvarov on 17.12.14.
 */
@Entity
@Table(name = "VIEW_NSI_GOST")
@RedirectToEntityManager(redirectTo = MarchDataSourceEnum.DS_SQLMARCH8)
public class VNSIGOST extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    @javax.persistence.Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @TableHeader(name = "НАИМЕНОВАНИЕ", width = 200, sequence = 0)
    @Column(name = "NAME")
    private String name;
    @TableHeader(name = "ДАТА ВВОДА", width = 200, sequence = 1)
    @Column(name = "INSERT_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date insertDate;
    @Column(name = "NOTE")
    @TableHeader(name = "ПРИМЕЧАНИЕ", width = 200, sequence = 2)
    private String note;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getInsertDate() {
        return insertDate;
    }

    public void setInsertDate(Date insertDate) {
        this.insertDate = insertDate;
    }

    public String getNote() {
        if (((note == null) || note.isEmpty())) {
            return "";
        } else
            return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String toString() {
        return name;
    }
}
