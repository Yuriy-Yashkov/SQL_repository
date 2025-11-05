package by.march8.entities.product;

import by.march8.api.BaseEntity;
import by.march8.api.RedirectToEntityManager;
import by.march8.api.TableHeader;
import by.march8.api.MarchDataSourceEnum;
import by.march8.entities.company.Employee;
import by.march8.entities.standard.Standard;
import by.march8.entities.standard.Unit;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by lidashka.
 */
@Entity
@Table(name = "MOD_PRODUCT")
@RedirectToEntityManager(redirectTo = MarchDataSourceEnum.DS_SQLSERVER)

public class ModelProduct extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToOne//(fetch = FetchType.LAZY)
    @JoinColumn(name = "REF_PRODUCT_RANGE_ID")
    private ProductRange range;
    @TableHeader(name = "Ассортимент", width = 60, sequence = 6)
    public String getRangeName() {
        return (range!=null)?range.getName():null;
    }

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "REF_PRODUCT_GROUP_ID")
    private ProductGroup group;
    @TableHeader(name = "Группа", width = 60, sequence = 5)
    public String getGroupName() {
        return (group!=null)?group.getName():null;
    }

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "REF_PRODUCT_KIND_ID")
    private ProductKind kind;
    @TableHeader(name = "Вид", width = 50, sequence = 0)
    public String getKindName() {
        return (kind!=null)?kind.getName():null;
    }

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "REF_BRAND_ID")
    private ProductBrand brand;
    @TableHeader(name = "Бренд", width = 60, sequence = 3)
    public String getBrandName() {
        return (brand!=null)?brand.getName():null;
    }

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "REF_COLLECTION_ID")
    private ProductCollection collection;
    @TableHeader(name = "Коллекция", width = 60, sequence = 4)
    public String getCollectionName() {
        return (collection!=null)?collection.getName():null;
    }

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "REF_EMPLOYEE_ID_PAINTER")
    private Employee painter;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "REF_EMPLOYEE_ID_CONSTRUCTOR")
    private Employee constructor;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "REF_UNIT_ID")
    private Unit unit;
    @TableHeader(name = "Ед.изм.", width = 60, sequence = 7)
    public String getUnitAbbreviation() {
        return (unit!=null)?unit.getAbbreviation():null;
    }

    @Column(name = "MODEL")
    @TableHeader(name = "Модель", width = 70, sequence = 1)
    private String model;

    @Column(name = "NAME")
    @TableHeader(name = "Наименование", width = 150, sequence = 2)
    private String name;

    @Column(name = "DESCRIPTION")
    @TableHeader(name = "Описание", width = 100, sequence = 8)
    private String description;

    @Column(name = "NOTE")
    @TableHeader(name = "Примечание", width = 100, sequence = 9)
    private String note;

    @Column(name = "VISIBLE")
    private boolean visible;

    @OneToMany()
    @JoinTable(name = "MOD_CLASSIFICATION", joinColumns = { @JoinColumn(name = "MOD_PRODUCT_ID") }, inverseJoinColumns = { @JoinColumn(name = "REF_STANDARD_ID") })
    @OrderBy ("type, name")
    private Set<Standard> standards = new HashSet<>();

    @OneToMany()
    @JoinTable(name = "MOD_COMPOSITION", joinColumns = { @JoinColumn(name = "MOD_PRODUCT_ID") }, inverseJoinColumns = { @JoinColumn(name = "REF_PRODUCT_RANGE_ID") })
    @OrderBy ("name")
    private Set<ProductRange> composition = new HashSet<>();

    @OneToMany(targetEntity = ModelSizeChart.class, mappedBy = "model", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy ("height")
    private Set<ModelSizeChart> modelSize = new HashSet<>();

    @OneToMany(targetEntity = ModelImage.class, mappedBy = "model", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ModelImage> modelImages = new HashSet<>();

    public Set<ModelSizeChart> getModelSize() {
        return modelSize;
    }

    public void setModelSize(Set<ModelSizeChart> modelSize) {
        this.modelSize = modelSize;
    }

    public Set<ModelImage> getModelImages() {
        return modelImages;
    }

    public void setModelImages(Set<ModelImage> modelImages) {
        this.modelImages = modelImages;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ProductRange getRange() {
        return range;
    }

    public void setRange(ProductRange range) {
        this.range = range;
    }

    public ProductGroup getGroup() {
        return group;
    }

    public void setGroup(ProductGroup group) {
        this.group = group;
    }

    public ProductKind getKind() {
        return kind;
    }

    public void setKind(ProductKind kind) {
        this.kind = kind;
    }

    public ProductBrand getBrand() {
        return brand;
    }

    public void setBrand(ProductBrand brand) {
        this.brand = brand;
    }

    public ProductCollection getCollection() {
        return collection;
    }

    public void setCollection(ProductCollection collection) {
        this.collection = collection;
    }

    public Employee getPainter() {
        return painter;
    }

    public void setPainter(Employee painter) {
        this.painter = painter;
    }

    public Employee getConstructor() {
        return constructor;
    }

    public void setConstructor(Employee constructor) {
        this.constructor = constructor;
    }

    public Unit getUnit() {
        return unit;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public Set<Standard> getStandards() {
        return standards;
    }

    public void setStandards(Set<Standard> standards) {
        this.standards = standards;
    }

    public Set<ProductRange> getComposition() {
        return composition;
    }

    public void setComposition(Set<ProductRange> composition) {
        this.composition = composition;
    }

    @Override
    public String toString() {
        return model+" "+name;
    }

    public String getOrderByField(){
        return model;
    }
}
