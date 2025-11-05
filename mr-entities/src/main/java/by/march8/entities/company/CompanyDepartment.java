package by.march8.entities.company;

import by.march8.api.BaseEntity;
import by.march8.api.RedirectToEntityManager;
import by.march8.api.TableHeader;
import by.march8.api.MarchDataSourceEnum;

import javax.persistence.*;

/**
 * @author andy-linux
 */
@Entity
@Table(name = "REF_COMPANY_DEPARTMENT")
@RedirectToEntityManager(redirectTo = MarchDataSourceEnum.DS_SQLSERVER)
public class CompanyDepartment extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	@Column(name = "PARENT_ID")
	private Integer parentID;

	@Column(name = "NAME")
	@TableHeader(name = "Наименование подразделения", width = 200, sequence = 0)
	private String name;

	@TableHeader(name = "Примечание", width = 200, sequence = 2)
	@Column(name = "NOTE")
	private String note;

	@Column(name = "VISIBLE")
	private boolean visible;

	@Override
	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getNote() {
		return note;
	}

	public Integer getParentID() {
		return parentID;
	}

	public boolean isVisible() {
		return visible;
	}

	@Override
	public void setId(final int id) {
		this.id = id;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public void setNote(final String note) {
		this.note = note;
	}

	public void setParentID(final Integer parentID) {
		this.parentID = parentID;
	}

	public void setVisible(final boolean visible) {
		this.visible = visible;
	}

	@Override
	public String toString() {
		return name;
	}
}
