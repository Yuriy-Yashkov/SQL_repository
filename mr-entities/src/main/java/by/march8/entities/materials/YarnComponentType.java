package by.march8.entities.materials;

import by.march8.api.BaseEntity;
import by.march8.api.RedirectToEntityManager;
import by.march8.api.TableHeader;
import by.march8.api.MarchDataSourceEnum;

import javax.persistence.*;

/**
 * @author andy-linux
 * 
 */
@Entity
@Table(name = "REF_COMPONENT_YARNS_TYPE")
@RedirectToEntityManager(redirectTo = MarchDataSourceEnum.DS_SQLMARCH8)
public class YarnComponentType extends BaseEntity {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="ID")
	private int id;
	@Column(name="ABBREVIATION")
    @TableHeader(name = "Код", width = -100, sequence = 0)
	private String abbreviation;
	@Column(name="NAME")
    @TableHeader(name = "Наименование", sequence = 1)
	private String name;
    @Column(name="PRIORITY")
    private boolean priority ;

    @Column(name = "VISIBLE")
    private boolean visible ;

    public int getId() {
        return id;
    }

    public void setId(final int id) {
        this.id = id;
    }

    public String getAbbreviation() {
        return abbreviation;
    }

    public void setAbbreviation(final String abbreviation) {
        this.abbreviation = abbreviation;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(final boolean visible) {
        this.visible = visible;
    }

    public boolean isPriority() {
        return priority;
    }

    public void setPriority(boolean priority) {
        this.priority = priority;
    }

    @Override
	public String toString() {
		return getName() + " (" + getAbbreviation() + ")";
	}
}
