package by.march8.entities.admin;

import by.march8.api.BaseEntity;
import by.march8.api.RedirectToEntityManager;
import by.march8.api.TableHeader;
import by.march8.api.ISimpleReference;
import by.march8.api.MarchDataSourceEnum;

import javax.persistence.*;

/**
 * @author Andy 21.09.2015.
 */
@Entity
@Table(name = "ADM_FORMS")
@RedirectToEntityManager(redirectTo = MarchDataSourceEnum.DS_SQLSERVER)
public class ApplicationForm extends BaseEntity implements ISimpleReference {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @TableHeader(name = "ID", width = -30, sequence = 0)
    private int id ;
    @TableHeader(name = "Наименование формы", sequence = 1)
    @Column(name = "NAME")
    private String name ;
    @TableHeader(name = "Примечание", sequence = 2)
    @Column(name = "NOTE")
    private String note ;

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(final int id) {
        this.id = id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(final String name) {
        this.name = name;
    }

    @Override
    public String getNote() {
        return note;
    }

    @Override
    public void setNote(final String note) {
        this.note = note;
    }

    @Override
    public String toString() {
        return "ApplicationForm{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", note='" + note + '\'' +
                '}';
    }
}
