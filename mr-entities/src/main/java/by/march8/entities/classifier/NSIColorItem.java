package by.march8.entities.classifier;

import by.march8.api.RedirectToEntityManager;
import by.march8.api.MarchDataSourceEnum;

import javax.persistence.*;

/**
 * @author Andy 10.09.2018 - 7:16.
 */

@NamedQueries({
        @NamedQuery(name = "NSIColorItem.findAll",
                query = "SELECT item FROM NSIColorItem item  " +
                        "order by item.parentId, item.name" )
})


@Entity
@Table(name="nsi_cd")
@RedirectToEntityManager(redirectTo = MarchDataSourceEnum.DS_SQLMARCH8)
public class NSIColorItem {

    @Id
    @Column(name="cw")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "ncw")
    private String name ;
    @Column(name="REF_COLOR_ID")
    private int parentId ;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        if(name!=null){
            return name.trim();
        }else {
            return "";
        }
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }
}
