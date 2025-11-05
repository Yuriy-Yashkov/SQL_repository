package by.march8.entities.product;

import by.march8.api.BaseEntity;
import by.march8.api.RedirectToEntityManager;
import by.march8.api.TableHeader;
import by.march8.api.MarchDataSourceEnum;
import by.march8.entities.materials.CanvasItem;

import javax.persistence.*;

/**
 * @author  by lidashka.
 */

@Entity
@Table(name = "MOD_CONFECTION_MAP_CANVAS")
@RedirectToEntityManager(redirectTo = MarchDataSourceEnum.DS_SQLSERVER)
public class ConfectionMapCanvas extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "ORDINAL_NUMBER")
    @TableHeader(name = "Номер", width = 30, sequence = 0)
    private int number;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MOD_CONFECTION_MAP_ID")
    private ConfectionMap confectionMap;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MAN_CANVAS_ID")
    private CanvasItem canvasItem;
    @TableHeader(name = "Полотно", width = 50, sequence = 1)
    public String getCanvasName() {
        return (canvasItem!=null)?canvasItem.getArticle():null;
    }

    @Column(name = "CANVAS_KIND")
    private int kind;
    @TableHeader(name = "Вид", width = 50, sequence = 2)
    public String getKindName() {
        String value = "";

        switch (kind) {
            case 0: value = "основное"; break;
            case 1: value = "отделочное"; break;
            default: value = " "; break;
        }

        return value;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public ConfectionMap getConfectionMap() {
        return confectionMap;
    }

    public void setConfectionMap(ConfectionMap confectionMap) {
        this.confectionMap = confectionMap;
    }

    public CanvasItem getCanvasItem() {
        return canvasItem;
    }

    public void setCanvasItem(CanvasItem canvasItem) {
        this.canvasItem = canvasItem;
    }

    public int getKind() {
        return kind;
    }

    public void setKind(int kind) {
        this.kind = kind;
    }

    @Override
    public String toString() {
        return canvasItem + " ";
    }

    public String getOrderByField(){
        return number + " " + kind + " " +canvasItem;
    }
}
