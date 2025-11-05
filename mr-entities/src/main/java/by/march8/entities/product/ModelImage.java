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
@Table(name = "MOD_PRODUCT_IMAGE")
@RedirectToEntityManager(redirectTo = MarchDataSourceEnum.DS_SQLSERVER)

public class ModelImage extends BaseEntity {
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

    @Column(name = "NAME")
    @TableHeader(name = "Наименование", width = 150, sequence = 2)
    private String name;

    @Column(name = "FULL_IMAGE")
    @TableHeader(name = "ВЛОЖЕНИЕ", width = 200, sequence = 2)
    private byte[] imageFull;

    @Column(name = "SMALL_IMAGE")
    @TableHeader(name = "ВЛОЖЕНИЕ", width = 200, sequence = 2)
    private byte[] imageSmall;

    @Column(name = "NOTE")
    @TableHeader(name = "Примечание", width = 50, sequence = 6)
    private String note;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte[] getImageFull() {
        return imageFull;
    }

    public void setImageFull(byte[] imageFull) {
        this.imageFull = imageFull;
    }

    public byte[] getImageSmall() {
        return imageSmall;
    }

    public void setImageSmall(byte[] imageSmall) {
        this.imageSmall = imageSmall;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}

