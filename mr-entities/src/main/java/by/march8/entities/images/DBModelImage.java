package by.march8.entities.images;

import by.march8.api.BaseEntity;
import by.march8.api.RedirectToEntityManager;
import by.march8.api.MarchDataSourceEnum;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@NamedQueries({ @NamedQuery(name = "DBModelImage.getItemByModelNumber",
        query = "SELECT item FROM DBModelImage item WHERE item.modelNumber = :model")})

@Entity
@Table(name = "IMAGES_MODEL")
@RedirectToEntityManager(redirectTo = MarchDataSourceEnum.DS_SQLMARCH8)
public class DBModelImage extends BaseEntity {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "MODEL_NUMBER")
    private int modelNumber;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "DEFAULT_IMAGE_ID")
    private DBImage defaultImage;

    @OneToMany(mappedBy = "model", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<DBImage> imageList ;

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    public int getModelNumber() {
        return modelNumber;
    }

    public void setModelNumber(int modelNumber) {
        this.modelNumber = modelNumber;
    }

    public DBImage getDefaultImage() {
        return defaultImage;
    }

    public void setDefaultImage(DBImage defaultImage) {
        this.defaultImage = defaultImage;
    }

    public List<DBImage> getImageList() {
        if(imageList==null){
            imageList = new ArrayList<>() ;
        }
        return imageList;
    }

    public void setImageList(List<DBImage> imageList) {
        this.imageList = imageList;
    }
}
