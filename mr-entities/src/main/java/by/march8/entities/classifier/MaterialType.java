package by.march8.entities.classifier;

import by.march8.api.BaseEntity;
import by.march8.api.RedirectToEntityManager;
import by.march8.api.MarchDataSourceEnum;

import javax.persistence.*;

/**
 * @author Andy 21.11.2018 - 7:33.
 */
@Entity
@Table(name="REF_MATERIAL_TYPE")
@RedirectToEntityManager(redirectTo = MarchDataSourceEnum.DS_SQLMARCH8)
public class MaterialType extends BaseEntity {


    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name="NAME")
    private String name ;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        if(name!=null) {
            return name.trim();
        }else{
            return "";
        }
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return getName() ;
    }
}
