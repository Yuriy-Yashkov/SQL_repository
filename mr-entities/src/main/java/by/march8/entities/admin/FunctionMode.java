package by.march8.entities.admin;

import by.march8.api.BaseEntity;
import by.march8.api.RedirectToEntityManager;
import by.march8.api.TableHeader;
import by.march8.api.MarchDataSourceEnum;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by Andy on 10.09.2014.
 *
 */
@Entity
@Table(name = "ADM_FUNCTION_MODE")
@RedirectToEntityManager(redirectTo = MarchDataSourceEnum.DS_SQLSERVER)
public class FunctionMode extends BaseEntity implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@Column(name = "NAME_ENG")
    @TableHeader(name = "Name", width = 200, sequence = 1)
	private String nameEng;
	@Column(name = "NAME_RU")
    @TableHeader(name = "Наименование", width = 200, sequence = 0)
	private String nameRus;
	@Column(name = "NOTE")
    @TableHeader(name = "Примечание", width = 200, sequence = 3)
	private String note;

	public int getId() {
		return id;
	}

	public String getNameEng() {
		return nameEng;
	}

	public String getNameRus() {
		return nameRus;
	}

	public String getNote() {
		return note;
	}

	public void setId(final int id) {
		this.id = id;
	}

	public void setNameEng(final String nameEng) {
		this.nameEng = nameEng;
	}

	public void setNameRus(final String nameRus) {
		this.nameRus = nameRus;
	}

	public void setNote(final String note) {
		this.note = note;
	}

	@Override
	public String toString() {
		return nameRus;
	}

    public String getOrderByField(){
        return getNameRus() ;
    }
}
