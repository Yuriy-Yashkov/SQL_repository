package by.march8.entities.admin;

import by.march8.api.BaseEntity;
import by.march8.api.TableHeader;

import javax.persistence.*;

@NamedQueries({
        @NamedQuery(name = "VUserRole.findByEmployeeId",
                query = "SELECT role FROM VUserRole role WHERE role.employeeId = :employee ORDER BY role.name")
})

/**
 * @author Andy 22.03.2016.
 */
@Entity
@Table(name = "V_USER_ROLE")
public class VUserRole extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @TableHeader(name = "ID", width = -50, sequence = 0)
    private int id ;

    @Column(name = "REF_EMPLOYEE_ID")
    private int employeeId ;

    @Column(name="ADM_ROLE_ID")
    private int roleId ;

    @Column(name="NAME")
    @TableHeader(name = "Наименование", width = 100, sequence = 0)
    private String name ;

    @Column(name = "NOTE")
    @TableHeader(name = "Примечание", width = 100, sequence = 1)
    private String note ;



    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(final int id) {
        this.id = id;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(final int employeeId) {
        this.employeeId = employeeId;
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(final int roleId) {
        this.roleId = roleId;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

    public String getNote() {
        return note;
    }

    public void setNote(final String note) {
        this.note = note;
    }
}
