package by.march8.entities.standard;

import by.march8.api.BaseEntity;
import by.march8.api.RedirectToEntityManager;
import by.march8.api.TableHeader;
import by.march8.api.MarchDataSourceEnum;

import javax.persistence.*;

@Entity
@Table(name="REF_STANDARD_TYPE")
@RedirectToEntityManager(redirectTo = MarchDataSourceEnum.DS_SQLSERVER)
public class StandardType extends BaseEntity {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id ;	

	@Column(name="SHORT_NAME")
    @TableHeader(name = "Наименование", width = 100, sequence = 0)
	private String shortName ;

    @Column(name="FULL_NAME")
    @TableHeader(name = "Наименование полное", width = 100, sequence = 1)
    private String fullName ;

	@Column(name="note")
    @TableHeader(name = "Примечание", width = 100, sequence = 2)
	private String note ;

    @Column(name="VISIBLE")
    private boolean visible ;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    @Override
	public String toString(){
		return shortName;
	}

}
