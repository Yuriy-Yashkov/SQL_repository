package by.march8.entities.admin;

import by.march8.api.BaseEntity;
import by.march8.api.RedirectToEntityManager;
import by.march8.api.TableHeader;
import by.march8.api.MarchDataSourceEnum;
import by.march8.entities.company.Employee;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**Класс описывающий аккаунт пользователя с его правами, ролями,
 * и доступными ему функциональными режимами
 *
 * Created by Andy on 10.09.2014.
 */

@Entity
@Table(name = "ADM_USER_INFORMATION")
@RedirectToEntityManager(redirectTo = MarchDataSourceEnum.DS_SQLSERVER)
public class UserInformation extends BaseEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @TableHeader(name = "ID", width = -30, sequence = 0)
    private int id;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "REF_EMPLOYEE_ID")
    private Employee employee;

    @Column(name = "USER_LOGIN")
    @TableHeader(name = "Login Name", sequence = 1)
    private String userLogin;

    @Column(name = "USER_PASSWORD")
    @TableHeader(name = "Login Password",  sequence = 2)
    private String userPassword;

    @Column(name = "NOTE")
    private String note;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<UserFormRights> formRights = new ArrayList<>();


    public Employee getEmployee() {
        return employee;
    }
    public int getId() {
        return id;
    }
    public String getNote() {
        return note;
    }
    public String getUserLogin() {
        return userLogin;
    }
    public String getUserPassword() {
        return userPassword;
    }
    public void setEmployee(final Employee employee) {
        this.employee = employee;
    }
    public void setId(final int id) {
        this.id = id;
    }
    public void setNote(final String note) {
        this.note = note;
    }
    public void setUserLogin(final String userLogin) {
        this.userLogin = userLogin;
    }
    public void setUserPassword(final String userPassword) {
        this.userPassword = userPassword;
    }
    @Override
    public String toString() {
        return userLogin;
    }

    public String getOrderByField() {
        return getUserLogin();
    }

    public List<UserFormRights> getFormRights() {
        return formRights;
    }

    @SuppressWarnings("unused")
    public void setFormRights(final List<UserFormRights> formRights) {
        this.formRights = formRights;
    }

    public UserFormRights getFormRightsByFormId(int formId) {
        for (UserFormRights right : getFormRights()) {
            if (right.getForm().getId() == formId) {
                return right;
            }
        }
        return null;
    }

    public UserFormRights getFormRightsByFormClass(String formClass) {
        for (UserFormRights right : getFormRights()) {
            if (right.getForm().getName().trim().equals(formClass)) {
                return right;
            }
        }
        return null;
    }
}
