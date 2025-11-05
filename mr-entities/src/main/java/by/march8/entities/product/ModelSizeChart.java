package by.march8.entities.product;

import by.march8.api.BaseEntity;
import by.march8.api.RedirectToEntityManager;
import by.march8.api.TableHeader;
import by.march8.api.MarchDataSourceEnum;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by lidashka.
 */

@Entity
@Table(name = "MOD_SIZE_CHART")
@RedirectToEntityManager(redirectTo = MarchDataSourceEnum.DS_SQLSERVER)

public class ModelSizeChart extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MOD_PRODUCT_ID")
    @OrderBy ("model")
    private ModelProduct model;

    @TableHeader(name = "Модель", width = 30, sequence = 0)
    public String getModelProduct() {
        return (model!=null)?model.getModel():null;
    }

    @TableHeader(name = "Наименование", width = 150, sequence = 1)
    public String getNameProduct() {
        return (model!=null)?model.getName():null;
    }

    @Column(name = "HEIGHT")
    @TableHeader(name = "Рост", width = 10, sequence = 2)
    private Float height;

    @Column(name = "SIZE")
    @TableHeader(name = "Размер", width = 10, sequence = 3)
    private Float size;

    //@Column(name = "PRINTSIZE")
    //@TableHeader(name = "Размер для печати", width = 50, sequence = 4)
   // private String printsize;

    @Column(name = "NOTE")
    //@TableHeader(name = "Примечание", width = 50, sequence = 6)
    private String note;

    @OneToMany(targetEntity = ModelSizeValue.class, mappedBy = "modelSize", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy ("number")
    private Set<ModelSizeValue> modelSizeValues = new HashSet<>();

    @TableHeader(name = "Обмеры", width = 50, sequence = 4)
    @OrderBy ("number")
    public String getModelMeasurementValue() {
        return (modelSizeValues!=null)?
                    modelSizeValues.toString().replace("[-","").replace(", -","-").replace("[","").replace("]",""):
                    null;
    }

    public ModelSizeChart() {
    }

    public ModelSizeChart(ModelProduct model, Float height, Float size, String note, Set<ModelSizeValue> modelSizeValues) {
        this.model = model;
        this.height = height;
        this.size = size;
        this.note = note;
        this.modelSizeValues = modelSizeValues;
    }

    public ModelSizeChart(final ModelSizeChart modelSizeChart) {
        this.model = modelSizeChart.model;
        this.height = modelSizeChart.height;
        this.size = modelSizeChart.size;
        this.note = modelSizeChart.note;
        this.modelSizeValues = modelSizeChart.modelSizeValues;
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

    public Float getHeight() {
        return height;
    }

    public void setHeight(Float height) {
        this.height = height;
    }

    public Float getSize() {
        return size;
    }

    public void setSize(Float size) {
        this.size = size;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Set<ModelSizeValue> getModelSizeValues() {
        return modelSizeValues;
    }

    public void setModelSizeValues(Set<ModelSizeValue> modelSizeValues) {
        this.modelSizeValues = modelSizeValues;
    }

    @Override
    public String toString() {
        return model+" "+height +" "+size;
    }

    public String getOrderByField(){
        return model + " " + height + " " +size;
    }
}
