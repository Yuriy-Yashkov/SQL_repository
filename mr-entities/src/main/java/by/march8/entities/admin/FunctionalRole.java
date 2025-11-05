package by.march8.entities.admin;

import by.march8.api.BaseEntity;
import by.march8.api.RedirectToEntityManager;
import by.march8.api.TableHeader;
import by.march8.api.MarchDataSourceEnum;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * Created by Andy on 10.09.2014.
 *
 */
@Setter
@Getter
@Entity
@Table(name="ADM_FUNCTIONAL_ROLE")
@RedirectToEntityManager(redirectTo = MarchDataSourceEnum.DS_SQLSERVER)
public class FunctionalRole extends BaseEntity implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    //@TableHeader(name = "ID", sequence = 99, width = 0)
    private int id ;

    @ManyToOne
    @JoinColumn(name = "ADM_ROLE_ID")
    private UserRole role;

    @OneToOne
    @JoinColumn(name="ADM_FUNCTION_MODE_ID")
    private FunctionMode functionMode ;

    @OneToOne
    @JoinColumn(name="ADM_RIGHT_ID")
    private UserRight right ;

    @Column(name = "NOTE")
    private String note ;

    public FunctionalRole() {
    }

    public FunctionalRole(final UserRole role, final FunctionMode functionMode, final UserRight right, final String note) {
        this.role = role;
        this.functionMode = functionMode;
        this.right = right;
        this.note = note;
    }

    public FunctionalRole(final int id, final UserRole role, final FunctionMode functionMode, final UserRight right, final String note) {
        this.id = id ;
        this.role = role;
        this.functionMode = functionMode;
        this.right = right;
        this.note = note;
    }

    public FunctionalRole(final FunctionalRole functionalRole) {
        this.role = functionalRole.getRole();
        this.functionMode = functionalRole.getFunctionMode();
        this.right = functionalRole.getRight();
        this.note = functionalRole.getNote();
    }

    @TableHeader(name = "Функциональный режим", sequence = 2)
    public String getFunctionModeName(){
        return this.getFunctionMode().getNameRus() ;
    }

    @TableHeader(name = "Права", sequence = 60)
    public String getRightsName(){
        return this.getRight().getName() ;
    }

    @Override
    public String toString() {
        return functionMode.getNameRus();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        FunctionalRole that = (FunctionalRole) o;
        return Objects.equals(functionMode, that.functionMode) && Objects.equals(right, that.right);
    }

    @Override
    public int hashCode() {
        return Objects.hash(functionMode, right);
    }
}
