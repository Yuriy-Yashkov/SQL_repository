package by.march8.entities.product;

import by.march8.api.BaseEntity;
import by.march8.api.RedirectToEntityManager;
import by.march8.api.TableHeader;
import by.march8.api.ISimpleReference;
import by.march8.api.MarchDataSourceEnum;

import javax.persistence.*;

/**
 * @author andy-linux
 */
@Entity
@Table(name = "REF_PRODUCT_TYPE")
@RedirectToEntityManager(redirectTo = MarchDataSourceEnum.DS_SQLSERVER)
public class ProductType extends BaseEntity implements ISimpleReference {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "NAME")
    @TableHeader(name = "Наименование", width = 200, sequence = 0)
    private String name;
    @Column(name = "NOTE")
    @TableHeader(name = "Примечание", width = 200, sequence = 1)
    private String note;
    @Column(name = "VISIBLE")
    private boolean visible ;

    public int getId() {
        return id;
    }

    public void setId(final int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getNote() {
        return note;
    }

    public void setNote(final String note) {
        this.note = note;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(final boolean visible) {
        this.visible = visible;
    }

    @Override
    public String toString() {
        return getName();
    }
}
