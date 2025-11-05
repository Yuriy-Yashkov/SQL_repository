package by.march8.entities.company;

import by.march8.api.BaseEntity;
import by.march8.api.RedirectToEntityManager;
import by.march8.api.TableHeader;
import by.march8.api.MarchDataSourceEnum;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by lidashka.
 */
@Entity
@Table(name = "VIEW_REF_EMPLOYEE_ART")
@RedirectToEntityManager(redirectTo = MarchDataSourceEnum.DS_SQLSERVER)

public class VEmployeeArt extends BaseEntity implements Serializable {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "SURNAME")
    @TableHeader(name = "Фамилия", width = 70, sequence = 1)
    private String surname;

    @Column(name = "employee_name")
    @TableHeader(name = "Имя", width = 70, sequence = 1)
    private String name;

    @Column(name = "PATRONYMIC")
    @TableHeader(name = "Отчество", width = 70, sequence = 1)
    private String patronymic;

    @Column(name = "department_name")
    @TableHeader(name = "Отдел", width = 70, sequence = 1)
    private String department;

    @Column(name = "position_name")
    @TableHeader(name = "Должность", width = 70, sequence = 1)
    private String position;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    @Override
    public String toString() {
        return  surname + " " + name + " " + patronymic;
    }
}

