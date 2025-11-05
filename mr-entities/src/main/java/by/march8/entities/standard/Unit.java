package by.march8.entities.standard;

import by.march8.api.BaseEntity;
import by.march8.api.RedirectToEntityManager;
import by.march8.api.TableHeader;
import by.march8.api.MarchDataSourceEnum;

import javax.persistence.*;

/**
 * Created by lidashka.
 */

@Entity
@Table(name="REF_UNIT")
@RedirectToEntityManager(redirectTo = MarchDataSourceEnum.DS_SQLSERVER)
public class Unit extends BaseEntity {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private int id ;

    @Column(name="ABBREVIATION")
    @TableHeader(name = "Аббревиатура", width = 30, sequence = 0)
    private String abbreviation ;

    @Column(name="NAME")
    @TableHeader(name = "Наименование", width = 100, sequence = 1)
    private String name ;

    @Column(name="VISIBLE")
    private boolean visible ;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAbbreviation() {
        return abbreviation;
    }

    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    @Override
    public String toString(){
        return abbreviation;
    }
}
