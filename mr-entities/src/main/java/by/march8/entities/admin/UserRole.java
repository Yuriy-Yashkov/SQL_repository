package by.march8.entities.admin;

import by.march8.api.BaseEntity;
import by.march8.api.RedirectToEntityManager;
import by.march8.api.TableHeader;
import by.march8.api.ISimpleReference;
import by.march8.api.MarchDataSourceEnum;
import by.march8.entities.company.Employee;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Andy on 10.09.2014.
 *
 */

@Setter
@Getter
@Entity
@Table(name = "ADM_ROLE")
@RedirectToEntityManager(redirectTo = MarchDataSourceEnum.DS_SQLSERVER)
public class UserRole extends BaseEntity implements Serializable, ISimpleReference {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(name = "NAME")
    @TableHeader(name = "Наименование", width = 100, sequence = 0)
	private String name;

	@Column(name = "NOTE")
    @TableHeader(name = "Примечание", width = 100, sequence = 1)
	private String note;

    @OneToMany(mappedBy = "role",fetch = FetchType.EAGER,
            cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, orphanRemoval = true)
    private Set<FunctionalRole> functionalRole = new HashSet<FunctionalRole>();

    @ManyToMany(mappedBy = "roles",fetch = FetchType.EAGER)
    private Set<Employee> employees = new HashSet<Employee>();

    @Override
    public String toString() {
        return  name ;
    }


    public String getOrderByField(){
        return getName() ;
    }
}
