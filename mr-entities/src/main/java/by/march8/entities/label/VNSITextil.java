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
@Table(name = "VIEW_NSI_TEXTIL")
@RedirectToEntityManager(redirectTo = MarchDataSourceEnum.DS_SQLMARCH8)
public class VNSITextil extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    @javax.persistence.Id
    @Column(name="ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "NAME")
    @TableHeader(name = "НАИМЕНОВАНИЕ", width = 200, sequence = 0)
    private String name;
    @Column(name = "INSERT_DATE")
    @TableHeader(name = "ДАТА ВВОДА", width = 200, sequence = 1)
    @Temporal(TemporalType.TIMESTAMP)
    private Date insertDate;

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
    public String toString(){
        return name;
    }
    public String getOrderByField(){
        return getName();
    }
}
