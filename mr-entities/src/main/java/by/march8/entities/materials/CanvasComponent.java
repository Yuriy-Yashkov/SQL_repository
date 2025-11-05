package by.march8.entities.materials;

import by.march8.api.BaseEntity;
import by.march8.api.RedirectToEntityManager;
import by.march8.api.TableHeader;
import by.march8.api.MarchDataSourceEnum;

import javax.persistence.*;

/**
 * @author Andy 09.01.2015.
 */
@Entity
@Table(name = "MAN_CANVAS_COMPONENT")
@RedirectToEntityManager(redirectTo = MarchDataSourceEnum.DS_SQLMARCH8)
public class CanvasComponent extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="ID")
    private int id;

    @ManyToOne
    @JoinColumn(name = "MAN_CANVAS_ID")
    private CanvasItem canvas;

    @OneToOne
    @JoinColumn(name = "MAN_YARN_ID")
    private YarnItem yarn;

    @Column(name = "COMPONENT_PERCENT")
    @TableHeader(name = "Содержание", sequence = 1)
    private Float percent;

    @Column(name = "NOTE")
    private String note;

    public CanvasComponent() {
    }

    public CanvasComponent(final CanvasComponent item) {
        yarn = item.getYarn();
        percent = item.getPercent() ;
        note = item.getNote() ;
    }

    @TableHeader(name = "Наименование компонента", sequence = 0)
    public String getYarnName() {
        if (yarn != null) {
            return yarn.getName();
        } else {
            return "";
        }
    }

    public int getId() {
        return id;
    }

    public void setId(final int id) {
        this.id = id;
    }

    public CanvasItem getCanvas() {
        return canvas;
    }

    public void setCanvas(final CanvasItem canvas) {
        this.canvas = canvas;
    }

    public YarnItem getYarn() {
        return yarn;
    }

    public void setYarn(final YarnItem yarn) {
        this.yarn = yarn;
    }

    public Float getPercent() {
        return percent;
    }

    public void setPercent(final Float percent) {
        this.percent = percent;
    }

    public String getNote() {
        return note;
    }

    public void setNote(final String note) {
        this.note = note;
    }

    @Override
    public String toString() {
        return yarn.toString() ;
    }
}
