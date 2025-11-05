package by.march8.entities.product;

import by.march8.api.BaseEntity;
import by.march8.api.RedirectToEntityManager;
import by.march8.api.TableHeader;
import by.march8.api.ISimpleReference;
import by.march8.api.MarchDataSourceEnum;
import by.march8.entities.standard.Unit;

import javax.persistence.*;

/**
 * Created by lidashka.
 */


@Entity
@Table(name = "MOD_MATERIAL")
@RedirectToEntityManager(redirectTo = MarchDataSourceEnum.DS_SQLSERVER)

public class ModelMaterial extends BaseEntity implements ISimpleReference {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "REF_UNIT_ID")
    private Unit unit;
    @TableHeader(name = "Ед.изм.", width = 60, sequence = 1)
    public String getUnitAbbreviation() {
        return (unit!=null)?unit.getAbbreviation():null;
    }

    @Column(name = "NAME")
    @TableHeader(name = "Наименование", width = 200, sequence = 0)
    private String name;

    @Column(name = "NOTE")
    @TableHeader(name = "Примечание", width = 200, sequence = 2)
    private String note;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Unit getUnit() {
        return unit;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
