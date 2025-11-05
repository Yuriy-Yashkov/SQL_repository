package by.march8.entities.warehouse;

import by.march8.api.BaseEntity;
import by.march8.api.MarchDataSourceEnum;
import by.march8.api.RedirectToEntityManager;
import by.march8.api.TableHeader;

import javax.persistence.*;

/**
 * @author Andy 13.12.2016.
 */
@NamedQueries({
        @NamedQuery(name = "ResponsiblePersons.findByType",
                query = "SELECT persons FROM ResponsiblePersons persons WHERE persons.type = :type ORDER BY persons.name")
})

@Entity
@Table(name="RESPONSIBLE_PERSONS")
@RedirectToEntityManager(redirectTo = MarchDataSourceEnum.DS_SQLMARCH8)
public class ResponsiblePersons extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="ID")
    private int id;

    @Column(name = "TYPE")
    private int type ;

    @Column(name="EXPORT_JOB")
    private boolean isExport ;

    @Column(name = "OFFICIAL_POSITION")
    @TableHeader(name = "Должность", sequence = 1)
    private String officialPosition ;

    @Column(name = "NAME")
    @TableHeader(name = "Наименование", sequence = 6)
    private String name ;

    @Column(name = "tabelNumber")
    @TableHeader(name = "Табельный номер", sequence = 6)
    private int tabelNumber ;

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(final int id) {
        this.id = id;
    }

    public int getTabelNumber() {
        return tabelNumber;
    }

    public void setTabelNumber(int tabelNumber) {
        this.tabelNumber = tabelNumber;
    }

    public int getType() {
        return type;
    }

    public void setType(final int type) {
        this.type = type;
    }

    public String getOfficialPosition() {
        if(officialPosition==null){
            return "" ;
        }else {
            return officialPosition.trim();
        }
    }

    public void setOfficialPosition(final String officialPosition) {
        this.officialPosition = officialPosition;
    }

    public String getName() {
        if(name==null){
            return "" ;
        }else {
            return name.trim();
        }
    }

    public void setName(final String name) {
        this.name = name;
    }

    public boolean isExport() {
        return isExport;
    }

    public void setIsExport(final boolean isExport) {
        this.isExport = isExport;
    }
}
