package by.march8.entities.classifier;

import by.march8.api.BaseEntity;
import by.march8.api.RedirectToEntityManager;
import by.march8.api.MarchDataSourceEnum;

import javax.persistence.*;

/**
 * @author Andy 21.05.2018 - 8:03.
 */

@NamedQueries({
        @NamedQuery(name = "ReferenceColorItem.findAll",
                query = "SELECT model FROM ReferenceColorItem model  " +
                        "order by model.parentId" +
                        "  ")
})

@Entity
@Table(name="REFERENCE_COLOR")
@RedirectToEntityManager(redirectTo = MarchDataSourceEnum.DS_SQLMARCH8)
public class ReferenceColorItem  extends BaseEntity{

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id ;

    @Column(name = "PARENT_ID")
    private int parentId ;

    @Column(name = "NAME")
    private String name ;

    @Transient
    private boolean nsiColor  = false;

    @Transient
    private boolean newColor = false ;


    public ReferenceColorItem() {
    }

    public ReferenceColorItem(final int id, final int parentId, final String name) {
        this.id  = id;
        this.parentId = parentId;
        this.name = name;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(final int id) {
        this.id = id;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(final int parentId) {
        this.parentId = parentId;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public boolean isNsiColor() {
        return nsiColor;
    }

    public void setNsiColor(boolean nsiColor) {
        this.nsiColor = nsiColor;
    }

    public boolean isNewColor() {
        return newColor;
    }

    public void setNewColor(boolean newColor) {
        this.newColor = newColor;
    }

    @Override
    public String toString() {
        return  name ; // +" ["+parentId+"]-["+id+"] - ["+isNsiColor()+"]";
    }
}
