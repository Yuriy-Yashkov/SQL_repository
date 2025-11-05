package by.march8.entities.product;

import by.march8.api.BaseEntity;
import by.march8.api.RedirectToEntityManager;
import by.march8.api.TableHeader;
import by.march8.api.MarchDataSourceEnum;

import javax.persistence.*;

/**
 * Created by lidashka.
 */

@Entity
@Table(name = "MOD_VALUE_SIZE")
@RedirectToEntityManager(redirectTo = MarchDataSourceEnum.DS_SQLSERVER)
public class ModelSizeValue extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MOD_SIZE_CHART_ID")
    private ModelSizeChart modelSize;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "REF_MEASUREMENT_TYPE_ID")
    private MeasurementType measumentType;

    @TableHeader(name = "Обмер", width = 50, sequence = 1)
    public String getMeasurement() {
        return (measumentType!=null)?measumentType.getName():null;
    }

    @Column(name = "ORDINAL_NUMBER")
    @TableHeader(name = "№", width = 10, sequence = 0)
    private int number;

    @Column(name = "VALUE_SIZE")
    @TableHeader(name = "Значение", width = 50, sequence = 2)
    private String value;

    @Column(name = "NOTE")
    @TableHeader(name = "Примечание", width = 50, sequence = 3)
    private String note;

    public ModelSizeValue() {
    }

    public ModelSizeValue(ModelSizeChart modelSize, MeasurementType measumentType, int number, String value, String note) {
        this.modelSize = modelSize;
        this.measumentType = measumentType;
        this.number = number;
        this.value = value;
        this.note = note;
    }

    public ModelSizeValue(final ModelSizeValue modelSizeValue) {
        this.modelSize = modelSizeValue.modelSize;
        this.measumentType = modelSizeValue.measumentType;
        this.number = modelSizeValue.number;
        this.value = modelSizeValue.value;
        this.note = modelSizeValue.note;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ModelSizeChart getModelSize() {
        return modelSize;
    }

    public void setModelSize(ModelSizeChart modelSize) {
        this.modelSize = modelSize;
    }

    public MeasurementType getMeasumentType() {
        return measumentType;
    }

    public void setMeasumentType(MeasurementType measumentType) {
        this.measumentType = measumentType;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    @Override
    public String toString() {
        return "-" + value;
    }

    public int getOrderByField(){
        return number;
    }
}
