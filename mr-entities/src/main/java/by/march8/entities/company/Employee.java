package by.march8.entities.company;

import by.march8.api.BaseEntity;
import by.march8.api.RedirectToEntityManager;
import by.march8.api.TableHeader;
import by.march8.api.MarchDataSourceEnum;
import by.march8.entities.admin.UserRole;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@NamedQueries({
        @NamedQuery(name = "Employee.findByEmployeeId",
                query = "SELECT employee FROM Employee employee WHERE employee.id = :employee")
})

/**
 * The Class Employe.
 *
 * @author andy-linux
 */
@Entity
@Table(name = "REF_EMPLOYEE")
@RedirectToEntityManager(redirectTo = MarchDataSourceEnum.DS_SQLSERVER)
public class Employee extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "REF_POSITION_ID")
    private CompanyPosition position;
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "REF_COMPANY_DEPARTMENT_ID")
    private CompanyDepartment department;

    @Column(name = "SURNAME")
    @TableHeader(name = "Фамилия", width = 100, sequence = 0)
    private String surname;

    @Column(name = "NAME")
    @TableHeader(name = "Имя", width = 100, sequence = 1)
    private String name;

    @Column(name = "PATRONYMIC")
    @TableHeader(name = "Отчество", width = 100, sequence = 2)
    private String patronymic;

    @Column(name = "REPORT_CARD_NUMBER")
    @TableHeader(name = "Tab №", width = 50, sequence = 3)
    private Integer cardNumber;

    @Column(name = "WORK_PHONE")
    private String workPhone;
    @Column(name = "HOME_PHONE")
    private String homePhone;
    @Column(name = "MOBILE_PHONE")
    private String mobilePhone;
    @Column(name = "EMAIL")
    private String email;
    @Column(name = "NOTE")
    private String note;
    @Column(name = "VISIBLE")
    private boolean visible;


    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "ADM_USER", joinColumns = { @JoinColumn(name = "REF_EMPLOYEE_ID") }, inverseJoinColumns = { @JoinColumn(name = "ADM_ROLE_ID") })
    private Set<UserRole> roles = new HashSet<>();

    @TableHeader(name = "Подразделение", width = 100, sequence = 5)
    public String getDepartmentName() {
        return department.getName();
    }

    @TableHeader(name = "Должность", width = 100, sequence = 6)
    public String getPositionName() {
        return position.getName();
    }

    public Integer getCardNumber() {
        return cardNumber;
    }

    public CompanyDepartment getDepartment() {
        return department;
    }

    public String getEmail() {
        return email;
    }

    public String getHomePhone() {
        return homePhone;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public String getName() {
        return name;
    }

    public String getNote() {
        return note;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public CompanyPosition getPosition() {
        return position;
    }

    public String getSurname() {
        return surname;
    }

    public String getWorkPhone() {
        return workPhone;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setCardNumber(final Integer cardNumber) {
        this.cardNumber = cardNumber;
    }

    public void setDepartment(final CompanyDepartment department) {
        this.department = department;
    }

    public void setEmail(final String email) {
        this.email = email;
    }

    public void setHomePhone(final String homePhone) {
        this.homePhone = homePhone;
    }

    public void setId(final Integer id) {
        this.id = id;
    }

    public void setMobilePhone(final String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public void setNote(final String note) {
        this.note = note;
    }

    public void setPatronymic(final String patronymic) {
        this.patronymic = patronymic;
    }

    public void setPosition(final CompanyPosition position) {
        this.position = position;
    }

    public void setSurname(final String surname) {
        this.surname = surname;
    }

    public void setVisible(final boolean visible) {
        this.visible = visible;
    }

    public void setWorkPhone(final String workPhone) {
        this.workPhone = workPhone;
    }

    @Override
    public String toString() {
        return getSurname() + " " + getName() + " " + getPatronymic();
    }

    public Set<UserRole> getRoles() {
        return roles;
    }

    public void setRoles(final Set<UserRole> roles) {
        this.roles = roles;
    }

    public String getOrderByField(){
        return getSurname() ;
    }
}
