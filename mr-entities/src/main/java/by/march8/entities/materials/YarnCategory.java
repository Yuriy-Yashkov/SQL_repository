package by.march8.entities.materials;

import by.march8.api.BaseEntity;
import by.march8.api.RedirectToEntityManager;
import by.march8.api.TableHeader;
import by.march8.api.MarchDataSourceEnum;

import javax.persistence.*;

/**
 * @author Andy 26.09.2014.
 */
@Entity
@Table(name = "REF_YARN_CATEGORY")
@RedirectToEntityManager(redirectTo = MarchDataSourceEnum.DS_SQLMARCH8)
public class YarnCategory extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="ID")
    private int id;
    @Column(name = "NAME")
    @TableHeader(name = "Наименование", width = 100, sequence = 0)
    private String name;
    @TableHeader(name = "DTEX", width = 50, sequence = 2)
    @Column(name = "DTEX")
    private float standardDTex;
    @TableHeader(name = "TEX", width = 50, sequence = 1)
    @Column(name = "TEX")
    private float standardTex;
    @TableHeader(name = "NM", width = 50, sequence = 6)
    @Column(name = "NM")
    private float standardNm;
    @TableHeader(name = "NEB", width = 50, sequence = 4)
    @Column(name = "NEB")
    private float standardNeb;
    @TableHeader(name = "NEW", width = 50, sequence = 5)
    @Column(name = "NEV")
    private float standardNew;
    @TableHeader(name = "DEN", width = 50, sequence = 3)
    @Column(name = "DEN")
    private float standardDen;
    @Column(name = "VISIBLE")
    private boolean visible;

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

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(final boolean visible) {
        this.visible = visible;
    }

    public float getStandardDTex() {
        return standardDTex;
    }

    public void setStandardDTex(float standardDTex) {
        this.standardDTex = standardDTex;
    }

    public float getStandardTex() {
        return standardTex;
    }

    public void setStandardTex(float standardTex) {
        this.standardTex = standardTex;
    }

    public float getStandardNm() {
        return standardNm;
    }

    public void setStandardNm(float standardNm) {
        this.standardNm = standardNm;
    }

    public float getStandardNeb() {
        return standardNeb;
    }

    public void setStandardNeb(float standardNeb) {
        this.standardNeb = standardNeb;
    }

    public float getStandardNew() {
        return standardNew;
    }

    public void setStandardNew(float standardNew) {
        this.standardNew = standardNew;
    }

    public float getStandardDen() {
        return standardDen;
    }

    public void setStandardDen(float standardDen) {
        this.standardDen = standardDen;
    }

    @Override
    public String toString() {
        return name;
    }

    public String getCategoryParams() {
        return "DTEX:" + getStandardDTex() + ";" +
                "DEN:" + getStandardDen() + ";" +
                "NM:" + getStandardNm() + ";" +
                "NEB:" + getStandardNeb() + ";" +
                "NEW:" + getStandardNew();
    }
}
