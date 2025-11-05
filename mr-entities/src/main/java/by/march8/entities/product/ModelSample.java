package by.march8.entities.product;

import by.march8.api.BaseEntity;
import by.march8.api.RedirectToEntityManager;
import by.march8.api.TableHeader;
import by.march8.api.MarchDataSourceEnum;
import by.march8.entities.company.Employee;
import by.march8.entities.standard.Standard;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by lidashka.
 */
@Entity
@Table(name = "MOD_SAMPLE")
@RedirectToEntityManager(redirectTo = MarchDataSourceEnum.DS_SQLSERVER)

public class ModelSample extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "REF_EMPLOYEE_ID_PAINTER")
    private Employee painter;
    @TableHeader(name = "Художник", width = 60, sequence = 4)
    public String getPainterName() {
        return (painter!=null)?painter.toString():null;
    }

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "REF_EMPLOYEE_ID_CONSTUCTOR")
    private Employee constructor;
    @TableHeader(name = "Конструктор", width = 60, sequence = 5)
    public String getConstructorName() {
        return (constructor!=null)?constructor.toString():null;
    }

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "REF_PRODUCT_KIND_ID")
    private ProductKind kind;
    @TableHeader(name = "Вид", width = 5, sequence = 0)
    public String getKindName() {
        return (kind!=null)?kind.getName():null;
    }

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "REF_STANDARD_ID")
    private Standard standard;
    //@TableHeader(name = "ГОСТ", width = 40, sequence = 0)
    public String getStandardName() {
        return (standard!=null)?standard.getName():null;
    }

    @Column(name = "MODEL")
    @TableHeader(name = "Модель", width = 40, sequence = 1)
    private String model;

    @Column(name = "NAME")
    @TableHeader(name = "Наименование", width = 100, sequence = 2)
    private String name;

    @Column(name = "SIZE")
    //@TableHeader(name = "Размеры", width = 60, sequence = 0)
    private String size;

    @Column(name = "CANVAS")
    //@TableHeader(name = "Полотно", width = 100, sequence = 0)
    private String canvas;

    @Column(name = "APPLIED_MATERIALS")
    //@TableHeader(name = "Прикладные", width = 100, sequence = 0)
    private String applied;

    @Column(name = "SAMPLE_DESCRIPTION")
    //@TableHeader(name = "Описание", width = 100, sequence = 0)
    private String sampleDescription;

    @Column(name = "CREATION_DATE")
    @Temporal(TemporalType.DATE)
    @TableHeader(name = "Дата создания", width = 40, sequence = 6)
    private Date date;

    @Column(name = "SAMPLE_IMAGE")
    //@TableHeader(name = "Эскиз", width = 200, sequence = 2)
    private byte[] imageSample;

    @Column(name = "NOTE")
    @TableHeader(name = "Примечание", width = 100, sequence = 7)
    private String note;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Employee getPainter() {
        return painter;
    }

    public void setPainter(Employee painter) {
        this.painter = painter;
    }

    public void setPainter(int id) {
        painter = new Employee();

        this.painter.setId(id);
    }

    public Employee getConstructor() {
        return constructor;
    }

    public void setConstructor(Employee constructor) {
        this.constructor = constructor;
    }

    public void setConstructor(int id) {
        constructor = new Employee();

        this.constructor.setId(id);
    }

    public ProductKind getKind() {
        return kind;
    }

    public void setKind(ProductKind kind) {
        this.kind = kind;
    }

    public Standard getStandard() {
        return standard;
    }

    public void setStandard(Standard standard) {
        this.standard = standard;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getCanvas() {
        return canvas;
    }

    public void setCanvas(String canvas) {
        this.canvas = canvas;
    }

    public String getApplied() {
        return applied;
    }

    public void setApplied(String applied) {
        this.applied = applied;
    }

    public String getSampleDescription() {
        return sampleDescription;
    }

    public void setSampleDescription(String sampleDescription) {
        this.sampleDescription = sampleDescription;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public byte[] getImageSample() {
        return imageSample;
    }

    public void setImageSample(byte[] imageSample) {
        this.imageSample = imageSample;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    @Override
    public String toString() {
        return model+" "+name;
    }
}
