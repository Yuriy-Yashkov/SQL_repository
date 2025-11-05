package by.march8.entities.images;

import by.march8.api.BaseEntity;
import by.march8.api.RedirectToEntityManager;
import by.march8.api.MarchDataSourceEnum;

import javax.persistence.*;

@Entity
@Table(name = "IMAGES_MODEL_IMAGE")
@RedirectToEntityManager(redirectTo = MarchDataSourceEnum.DS_SQLMARCH8)
public class DBImage extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private int id;

    @ManyToOne()
    @JoinColumn(name = "REF_MODEL_ID")
    private DBModelImage model;

    @Column(name="REF_COLOR_ID")
    private int colorId ;

    @Column(name = "IMAGE_FILE_NAME")
    private String imageFileName;

    @Column(name = "SOURCE_FILE_NAME")
    private String sourceFileName;

    @Column(name="WIDTH")
    private int width ;

    @Column(name = "HEIGHT")
    private int height ;

    @Column(name="SIZE")
    private int size ;

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    public DBModelImage getModel() {
        return model;
    }

    public void setModel(DBModelImage model) {
        this.model = model;
    }

    public int getColorId() {
        return colorId;
    }

    public void setColorId(int colorId) {
        this.colorId = colorId;
    }

    public String getImageFileName() {
        if(imageFileName!=null) {
            return imageFileName.trim();
        }
        return null;
    }

    public void setImageFileName(String imageFileName) {
        this.imageFileName = imageFileName;
    }

    public String getSourceFileName() {
        if(sourceFileName!=null) {
            return sourceFileName.trim();
        }
        return null;
    }

    public void setSourceFileName(String sourceFileName) {
        this.sourceFileName = sourceFileName;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

}
