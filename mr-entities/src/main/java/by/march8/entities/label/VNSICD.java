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
@Table(name = "VIEW_NSI_CD")
@RedirectToEntityManager(redirectTo = MarchDataSourceEnum.DS_SQLMARCH8)
public class VNSICD extends BaseEntity implements Serializable{
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    @Id
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
    @Column(name = "INSERT_USER")
    @TableHeader(name = "ДОБАВИЛ", width = 200, sequence = 2)
    private String insertUser;

    @Column(name = "INSERT_DEPARTMENT")
    @TableHeader(name = "ДЕПАРТАМЕНТ", width = 200, sequence = 3)
    private int insertDepartment;
    @Column(name = "EDIT_DATE")
    @TableHeader(name = "ДАТА КОРРЕКТИРОВКИ", width = 200, sequence = 4)
    @Temporal(TemporalType.TIMESTAMP)
    private Date editDate;
    @Column(name = "EDIT_USER")
    @TableHeader(name = "ОТКОРРЕКТИРОВАЛ", width = 200, sequence = 5)
    private String editUser;
    @Column(name = "EDIT_DEPARTMENT")
    //@TableHeader(name = "EDIT_DEPARTMENT", width = 200, sequence = 6)
    private Integer editDepartment;
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

    public String getInsertUser() {
        return insertUser;
    }

    public void setInsertUser(String insertUser) {
        this.insertUser = insertUser;
    }

    public int getInsertDepartment() {
        return insertDepartment;
    }

    public void setInsertDepartment(int insertDepartment) {
        this.insertDepartment = insertDepartment;
    }

    public Date getEditDate() {
        if (editDate == null){
            return null;
        }
        else
            return editDate;
    }

    public void setEditDate(Date editDate) {
        this.editDate = editDate;
    }

    public String getEditUser() {
        if ((editUser == null) || editUser.isEmpty()){
            return null;
        }
        else
            return editUser;
    }

    public void setEditUser(String editUser) {
        this.editUser = editUser;
    }

    public Integer getEditDepartment() {
        if (editDepartment== null){
            return null;
        }
        else
            return editDepartment;
    }

    public void setEditDepartment(Integer editDepartment) {
        this.editDepartment = editDepartment;
    }
    @Override
    public String toString() {
        return name;
    }
}
