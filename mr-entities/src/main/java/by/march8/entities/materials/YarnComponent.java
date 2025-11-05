package by.march8.entities.materials;

import by.march8.api.BaseEntity;
import by.march8.api.RedirectToEntityManager;
import by.march8.api.TableHeader;
import by.march8.api.MarchDataSourceEnum;

import javax.persistence.*;

/**
 * @author andy-linux
 */
@Entity
@Table(name = "REF_YARN_COMPONENT")
@RedirectToEntityManager(redirectTo = MarchDataSourceEnum.DS_SQLMARCH8)
public class YarnComponent extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="ID")
    private int id;

    @ManyToOne
    @JoinColumn(name = "MAN_YARN_ID")
    private YarnItem yarn;

    @OneToOne
    @JoinColumn(name = "REF_COMPONENT_YARNS_TYPE_ID")
    private YarnComponentType yarnType;
    @Column(name = "NAME")
    private String name;
    @Column(name = "COMPONENT_PERCENT")
    @TableHeader(name = "Содержание",  sequence = 2)
    private float componentPercent;
    @Column(name = "NOTE")
    //@TableHeader(name = "Примечание",  sequence = 3)
    private String note;
    @Column(name = "VISIBLE")
    private boolean visible;

    @TableHeader(name = "Компонент",  sequence = 1)
    public String getComponentName() {
        return yarnType.getName();
    }

    @TableHeader(name = "Код",width = -50, sequence = 0)
    public String getComponentAbbrev() {
        return yarnType.getAbbreviation();
    }

    public YarnItem getYarn() {
        return yarn;
    }

    public void setYarn(final YarnItem yarn) {
        this.yarn = yarn;
    }

    public int getId() {
        return id;
    }

    public void setId(final int id) {
        this.id = id;
    }

    public YarnComponentType getYarnType() {
        return yarnType;
    }

    public void setYarnType(final YarnComponentType yarnType) {
        this.yarnType = yarnType;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public float getComponentPercent() {
        return componentPercent;
    }

    public void setComponentPercent(final float componentPercent) {
        this.componentPercent = componentPercent;
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
        return "";
    }

}
