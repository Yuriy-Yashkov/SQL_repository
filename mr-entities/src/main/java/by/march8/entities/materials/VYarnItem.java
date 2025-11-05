package by.march8.entities.materials;

import by.march8.api.BaseEntity;
import by.march8.api.RedirectToEntityManager;
import by.march8.api.TableHeader;
import by.march8.api.MarchDataSourceEnum;

import javax.persistence.*;

/**
 * @author Andy 13.03.2015.
 */
@Entity
@Table(name = "V_MAN_YARN")
@RedirectToEntityManager(redirectTo = MarchDataSourceEnum.DS_SQLSERVER)
public class VYarnItem extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @TableHeader(name = "", sequence = 0)
    @Column(name = "CODE")
    private int code;
    @TableHeader(name = "", sequence = 1)
    @Column(name = "NAME")
    private String name;
    @TableHeader(name = "Категория", sequence = 2)
    @Column(name = "YARN_CATEGORY")
    private String categoryName;
    @TableHeader(name = "DTEX", sequence = 3)
    @Column(name = "DTEX")
    private float dtex;
    @TableHeader(name = "TEX", sequence = 4)
    @Column(name = "TEX")
    private float tex;
    @TableHeader(name = "NM", sequence = 5)
    @Column(name = "NM")
    private float nm;
    @TableHeader(name = "NEB", sequence = 6)
    @Column(name = "NEB")
    private float neb;
    @TableHeader(name = "NEW", sequence = 7)
    @Column(name = "NEV")
    private float nev;
    @TableHeader(name = "DEN", sequence = 8)
    @Column(name = "DEN")
    private float den;



    public int getId() {
        return id;
    }

    public void setId(final int id) {
        this.id = id;
    }

    public int getCode() {
        return code;
    }

    public void setCode(final int code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(final String categoryName) {
        this.categoryName = categoryName;
    }

    public float getDtex() {
        return dtex;
    }

    public void setDtex(final float dtex) {
        this.dtex = dtex;
    }

    public float getTex() {
        return tex;
    }

    public void setTex(final float tex) {
        this.tex = tex;
    }

    public float getNm() {
        return nm;
    }

    public void setNm(final float nm) {
        this.nm = nm;
    }

    public float getNeb() {
        return neb;
    }

    public void setNeb(final float neb) {
        this.neb = neb;
    }

    public float getNev() {
        return nev;
    }

    public void setNev(final float nev) {
        this.nev = nev;
    }

    public float getDen() {
        return den;
    }

    public void setDen(final float den) {
        this.den = den;
    }
}
