package by.march8.entities.classifier;

import by.march8.api.BaseEntity;
import by.march8.api.RedirectToEntityManager;
import by.march8.api.TableHeader;
import by.march8.api.MarchDataSourceEnum;

import javax.persistence.*;

/**
 * @author Andy 21.05.2018 - 11:57.
 */

@NamedQueries({
        @NamedQuery(name = "RecipesColorItem.findByColorId",
                query = "SELECT item FROM RecipesColorItem item  " +
                        "WHERE item.colorId =:colorId " +
                        "order by item.name" )
})

@Entity
@Table(name = "REFERENCE_COLOR_RECIPES")
@RedirectToEntityManager(redirectTo = MarchDataSourceEnum.DS_SQLMARCH8)
public class RecipesColorItem extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private int id;
    @Column(name = "REF_COLOR_ID")
    private int colorId;

    @Column(name = "NAME")
    private String name;

    @Column(name = "CODE")
    private String code;

    public RecipesColorItem() {
    }

    public RecipesColorItem(final int id, final int colorId, final String name) {
        this.id = id;
        this.colorId = colorId;
        this.name = name;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(final int id) {
        this.id = id;
    }

    public int getColorId() {
        return colorId;
    }

    public void setColorId(final int colorId) {
        this.colorId = colorId;
    }

    @TableHeader(name = "Рецептура_Наименование", sequence = 10)
    public String getName() {
        if(name!=null) {
            return name.trim();
        }else{
            return "";
        }
    }

    public void setName(final String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return getName()+" ["+getCode()+"]";
    }

    @TableHeader(name = "Рецептура_Код", sequence = 1, width = -100)
    public String getCode() {
        if(code!=null) {
            return code.trim();
        }else{
            return "";
        }
    }

    public void setCode(String code) {
        this.code = code;
    }
}


