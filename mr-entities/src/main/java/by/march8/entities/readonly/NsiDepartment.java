package by.march8.entities.readonly;

import by.march8.api.MarchDataSourceEnum;
import by.march8.api.RedirectToEntityManager;

import javax.persistence.*;

/**
 * @author Andy on 19.11.2021 11:42
 */
@Entity
@Table(name = "V_DEPARTMENTS")
@RedirectToEntityManager(redirectTo = MarchDataSourceEnum.DS_SQLMARCH8)
public class NsiDepartment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id ;
    private String departmentName;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDepartmentName() {
        if(departmentName!=null){
            return departmentName.trim();
        }
        return "";
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    @Override
    public String toString() {
        return getDepartmentName() ;
    }
}
