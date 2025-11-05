package by.march8.entities.materials;


import by.march8.api.BaseEntity;
import by.march8.api.RedirectToEntityManager;
import by.march8.api.TableHeader;
import by.march8.api.MarchDataSourceEnum;

import javax.persistence.*;

/**
 * @author andy-linux
 */
@Entity
@Table(name = "REF_CANVAS_WEAVE")
@RedirectToEntityManager(redirectTo = MarchDataSourceEnum.DS_SQLMARCH8)
public class CanvasWeave extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="ID")
    private int id;
    @TableHeader(name = "Шифр",sequence = 0, width = -50)
    @Column(name = "CODE")
    private int code;
    @TableHeader(name="Наименование", width = 200 ,sequence = 1)
    @Column(name = "NAME")
    private String name;

    @TableHeader(name = "Примечание", width = 200,sequence = 2)
    @Column(name = "NOTE")
    private String note;
    @Column(name = "VISIBLE")
    private boolean visible;

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

    public String getNote() {
        return note;
    }

    public void setNote(final String note) {
        this.note = note;
    }

    public void setVisible(final boolean visible) {
        this.visible = visible;
    }

    public boolean isVisible() {
        return visible;
    }

    @Override
    public String toString(){
        return name ;
    }
}
