package by.march8.entities.materials;

import by.march8.api.BaseEntity;
import by.march8.api.RedirectToEntityManager;
import by.march8.api.TableHeader;
import by.march8.api.MarchDataSourceEnum;

import javax.persistence.*;

/**
 * Created by Andy on 06.04.2015.
 * Класс описывает единицу списка маркировочного состава полотна
 */
@Entity
@Table(name = "MAN_CANVAS_MARKING_COMPOSIT")
@RedirectToEntityManager(redirectTo = MarchDataSourceEnum.DS_SQLMARCH8)
public class CanvasMarkingComposition extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="ID")
    private int id ;

    @ManyToOne
    @JoinColumn(name = "MAN_CANVAS_ID")
    private CanvasItem canvas;

    @OneToOne
    @JoinColumn(name = "REF_COMPONENT_YARNS_TYPE_ID")
    private YarnComponentType component ;

    @Column(name="PERCENT_VALUE")
    private float percent ;

    public CanvasMarkingComposition() {
    }

    public CanvasMarkingComposition(final CanvasMarkingComposition item) {
        component = item.getComponent();
        percent = item.getPercent();
    }

    public int getId() {
        return id;
    }

    public void setId(final int id) {
        this.id = id;
    }

    @TableHeader(name = "Наименование компонента", sequence = 0)
    public String getComponentName(){
        if(component!=null){
            return component.getName() ;
        }
        return "" ;
    }

    public YarnComponentType getComponent() {
        return component;
    }

    public void setComponent(final YarnComponentType component) {
        this.component = component;
    }

    @TableHeader(name = "Содержание", sequence = 1)
    public float getPercent() {
        return percent;
    }

    public void setPercent(final float percent) {
        this.percent = percent;
    }

    public CanvasItem getCanvas() {
        return canvas;
    }

    public void setCanvas(final CanvasItem canvas) {
        this.canvas = canvas;
    }

    @Override
    public String toString() {
        return component.toString() ;
    }
}
