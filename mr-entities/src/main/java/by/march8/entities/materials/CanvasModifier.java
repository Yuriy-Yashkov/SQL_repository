package by.march8.entities.materials;

import by.march8.api.*;

import javax.persistence.*;

/**
 * @author andy-linux
 */
@Entity
@Table(name = "REF_CANVAS_MODIFIERS")
@RedirectToEntityManager(redirectTo = MarchDataSourceEnum.DS_SQLMARCH8)
@DialogFrameSize(width = 500,height = 500)
public class CanvasModifier extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="ID")
    private int id;

    @Column(name = "ABBREVIATION")
    @TableHeader(name = "Аббревиатура", sequence = 2)
    private String abbreviation;

    @Column(name = "NAME")
    @TableHeader(name = "Наименование", sequence = 3)
    private String name;

    @Column(name = "NOTE")
    @TableHeader(name = "Примечание", sequence = 4, weight = 0)
    private String note;

    @Column(name = "VISIBLE")
    private boolean visible;

    public int getId() {
        return id;
    }

    public void setId(int id) {this.id = id ;}

    public String getAbbreviation() {
        return abbreviation.trim();
    }

    public void setAbbreviation(String abbrev) {
        this.abbreviation = abbrev;
    }

    public String getName() {
        return name.trim();
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNote() {
        return note;
    }

    public void setNote(final String note) {
        this.note = note;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(final boolean visible) {
        this.visible = visible;
    }

    @Override
    public String toString() {
        return name.trim() + " (" + abbreviation.trim() + ")";
    }
}
