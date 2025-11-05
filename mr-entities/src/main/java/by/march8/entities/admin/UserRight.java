package by.march8.entities.admin;

import by.march8.api.BaseEntity;
import by.march8.api.RedirectToEntityManager;
import by.march8.api.TableHeader;
import by.march8.api.ISimpleReference;
import by.march8.api.MarchDataSourceEnum;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by Andy on 10.09.2014.
 *
 */
@Entity
@Table(name = "ADM_RIGHT")
@RedirectToEntityManager(redirectTo = MarchDataSourceEnum.DS_SQLSERVER)
public class UserRight extends BaseEntity implements Serializable, ISimpleReference {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    @TableHeader(name = "ID", width = -50, sequence = 0)
	private int id;

	@Column(name = "NAME")
    @TableHeader(name = "Наименование", width = 100, sequence = 0)
    private String name;
	@Column(name = "NOTE")
    @TableHeader(name = "Примечание", width = 100, sequence = 0)
	private String note;

    public UserRight(final int id ,final String name, final String note) {
        this.id = id ;
        this.name = name;
        this.note = note;
    }

    public UserRight() {
    }

    public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getNote() {
		return note;
	}

	public void setId(final int id) {
		this.id = id;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public void setNote(final String note) {
		this.note = note;
	}

	@Override
	public String toString() {
		return name;
	}
}
