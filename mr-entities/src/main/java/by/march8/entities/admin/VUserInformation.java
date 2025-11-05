package by.march8.entities.admin;

import by.march8.api.BaseEntity;
import by.march8.api.TableHeader;

import javax.persistence.*;

@NamedQueries({
        @NamedQuery(name = "VUserInformation.findAll",
                query = "SELECT information FROM VUserInformation information ORDER BY information.userLogin")
})

/**
 * @author Andy 22.03.2016.
 */
@Entity
@Table(name = "V_USER_INFORMATION")
public class VUserInformation extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @TableHeader(name = "ID", width = -30, sequence = 0)
    private int id;

    @Column(name = "USER_LOGIN")
    @TableHeader(name = "Login Name", sequence = 1)
    private String userLogin;

    @Column(name = "USER_PASSWORD")
    @TableHeader(name = "Login Password",  sequence = 2)
    private String userPassword;

    @Column(name = "EMPLOYEE_ID")
    private int employeeId;

    @Column(name = "EMPLOYEE_NAME")
    private String employeeName;

    @Column(name = "EMPLOYEE_SURNAME")
    private String employeeSurname;

    @Column(name = "EMPLOYEE_PATRONYMIC")
    private String employeePatronymic;

    @Column(name = "POSITION_NAME")
    private String positionName;

    @Column(name = "DEPARTMENT_NAME")
    private String departmentName;


    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(final int id) {
        this.id = id;
    }

    public String getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(final String userLogin) {
        this.userLogin = userLogin;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(final String userPassword) {
        this.userPassword = userPassword;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(final int employeeId) {
        this.employeeId = employeeId;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(final String employeeName) {
        this.employeeName = employeeName;
    }

    public String getEmployeeSurname() {
        return employeeSurname;
    }

    public void setEmployeeSurname(final String employeeSurname) {
        this.employeeSurname = employeeSurname;
    }

    public String getEmployeePatronymic() {
        return employeePatronymic;
    }

    public void setEmployeePatronymic(final String employeePatronymic) {
        this.employeePatronymic = employeePatronymic;
    }

    public String getPositionName() {
        return positionName;
    }

    public void setPositionName(final String positionName) {
        this.positionName = positionName;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(final String departmentName) {
        this.departmentName = departmentName;
    }

    public String getFullEmployeeName() {

        String resultString = "";
        if (employeeSurname != null) {
            resultString = employeeSurname.trim() + resultString + " ";
        }

        if (employeeName != null) {
            resultString = resultString +" "+employeeName.trim();
        }

        if(employeePatronymic!=null){
            resultString = resultString+" " + employeePatronymic.trim();
        }

        return resultString.trim() ;
    }

    @Override
    public String toString() {
        return  userLogin ;
    }
}
