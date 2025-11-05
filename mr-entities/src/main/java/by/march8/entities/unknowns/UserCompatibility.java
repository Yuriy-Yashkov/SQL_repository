package by.march8.entities.unknowns;

import by.march8.api.RedirectToEntityManager;
import by.march8.api.MarchDataSourceEnum;

/**
 * Класс для совместимости версий ядра программы, замена классу-синглтону User
 * Created by Andy on 23.09.2015.
 */

@RedirectToEntityManager(redirectTo = MarchDataSourceEnum.DS_POSTGRESQL)
public class UserCompatibility {
    private int userId;
    private String userLogin;
    private String userPassword;
    private int employeeId;
    private String employeeName;
    private int departmentId;
    private String departmentName;


    public int getUserId() {
        return userId;
    }

    public void setUserId(final int userId) {
        this.userId = userId;
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

    public int getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(final int departmentId) {
        this.departmentId = departmentId;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(final String departmentName) {
        this.departmentName = departmentName;
    }

    @Override
    public String toString() {
        return "UserCompatibility{" +
                "userId=" + userId +
                ", userLogin='" + userLogin + '\'' +
                ", userPassword='" + userPassword + '\'' +
                ", employeeId=" + employeeId +
                ", employeeName='" + employeeName + '\'' +
                ", departmentId=" + departmentId +
                ", departmentName='" + departmentName + '\'' +
                '}';
    }
}
