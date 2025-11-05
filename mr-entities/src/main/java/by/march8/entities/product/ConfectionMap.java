package by.march8.entities.product;

import by.march8.api.BaseEntity;
import by.march8.api.RedirectToEntityManager;
import by.march8.api.TableHeader;
import by.march8.api.MarchDataSourceEnum;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * @author  by lidashka.
 */
@Entity
@Table(name = "MOD_CONFECTION_MAP")
@RedirectToEntityManager(redirectTo = MarchDataSourceEnum.DS_SQLSERVER)
public class ConfectionMap extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MOD_PRODUCT_ID")
    private ModelProduct model;

    @TableHeader(name = "Модель", width = 30, sequence = 0)
    public String getModelProduct() {
        return (model!=null)?model.getModel():null;
    }

    @TableHeader(name = "Наименование", width = 150, sequence = 1)
    public String getNameProduct() {
        return (model!=null)?model.getName():null;
    }

    @Column(name = "ORDINAL_NUMBER")
    @TableHeader(name = "Номер карты", width = 30, sequence = 2)
    private int number;

    @Column(name = "CREATION_DATE")
    @Temporal(TemporalType.DATE)
    @TableHeader(name = "Дата создания", width = 40, sequence = 4)
    private Date date;

    @Column(name = "NOTE")
    @TableHeader(name = "Примечание", width = 200, sequence = 5)
    private String note;

    @OneToMany()
    @JoinTable(name = "MOD_CONFECTION_MAP_ITEM", joinColumns = { @JoinColumn(name = "MOD_CONFECTION_MAP_ID") }, inverseJoinColumns = { @JoinColumn(name = "MOD_MATERIAL_ID") })
    private Set<ModelMaterial> modelMaterials = new HashSet<>();

    @OneToMany(targetEntity = ConfectionMapCanvas.class, mappedBy = "confectionMap", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy ("number, kind, canvasItem")
    private Set<ConfectionMapCanvas> confectionMapCanvases = new HashSet<>();

    @TableHeader(name = "Состав полотен", width = 150, sequence = 3)
    @OrderBy ("number, kind, canvasItem")
    public String getCanvasesName() {
        return (confectionMapCanvases!=null)?
                confectionMapCanvases.toString().replace("[-","").replace(", -","-").replace("[","").replace("]",""):
                null;
    }


    public Set<ModelMaterial> getModelMaterials() {
        return modelMaterials;
    }

    public void setModelMaterials(Set<ModelMaterial> modelMaterials) {
        this.modelMaterials = modelMaterials;
    }

    public Set<ConfectionMapCanvas> getConfectionMapCanvases() {
        return confectionMapCanvases;
    }

    public void setConfectionMapCanvases(Set<ConfectionMapCanvas> confectionMapCanvases) {
        this.confectionMapCanvases = confectionMapCanvases;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ModelProduct getModel() {
        return model;
    }

    public void setModel(ModelProduct model) {
        this.model = model;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    @Override
    public String toString() {
        return " № "+number + ". Состав " + getCanvasesName();
    }

    public String getOrderByField(){
        return model +" " + number;
    }
}
