package by.march8.entities.standard;

import by.march8.api.BaseEntity;
import by.march8.api.RedirectToEntityManager;
import by.march8.api.TableHeader;
import by.march8.api.MarchDataSourceEnum;
import by.march8.entities.product.ModelProduct;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;


@Entity
@Table(name = "REF_STANDARD")
@RedirectToEntityManager(redirectTo = MarchDataSourceEnum.DS_SQLSERVER)
public class Standard extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @TableHeader(name = "Наименование", width = 150, sequence = 1)
    @Column(name = "NAME")
    private String name;

    @Temporal(TemporalType.DATE)
    @Column(name = "APPROVAL_DATE")
    @TableHeader(name = "Дата", width = 70, sequence = 2)
    private Date date;

    @Column(name = "NOTE")
    @TableHeader(name = "Примечание", width = 200, sequence = 4)
    private String note;

    @OneToOne
    @JoinColumn(name = "REF_STANDARD_TYPE_ID")
    private StandardType type;
    @TableHeader(name = "Тип", width = 70, sequence = 0)
    public String getStandardTypeName() {
        return type.getShortName();
    }

    @Column(name = "VISIBLE")
    private boolean visible;

    public Standard() {
    }

    public Standard(String name, Date date, String note, StandardType type, boolean visible, Set<ModelProduct> modelProducts) {
        this.name = name;
        this.date = date;
        this.note = note;
        this.type = type;
        this.visible = visible;
    }

    public Standard(final Standard standard) {
        this.name = standard.name;
        this.date = standard.date;
        this.note = standard.note;
        this.type = standard.type;
        this.visible = standard.visible;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public StandardType getType() {
        return type;
    }

    public void setType(StandardType code) {
        this.type = code;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    @Override
    public String toString() {
        return  name ;
    }
}
