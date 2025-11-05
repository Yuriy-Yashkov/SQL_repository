package by.march8.entities.storage;

import by.march8.api.RedirectToEntityManager;
import by.march8.api.MarchDataSourceEnum;

import javax.persistence.*;

/**
 * Бин бригад
 * Created by Andy on 16.12.2014.
 */
//@Entity
@Table(name = "nsi_brig")
@RedirectToEntityManager(redirectTo = MarchDataSourceEnum.DS_SQLMARCH8)
public class NsiTeam {
    @Id
    @Column(name = "kod1")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "naim")
    private String name;

    public int getId() {
        return id;
    }

    public void setId(final int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }
}
