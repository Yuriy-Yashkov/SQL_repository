package by.march8.entities.classifier;

import by.march8.api.BaseEntity;
import by.march8.api.RedirectToEntityManager;
import by.march8.api.TableHeader;
import by.march8.api.MarchDataSourceEnum;

import javax.persistence.*;

/**
 * @author Andy 03.10.2018 - 7:12.
 */
@NamedQueries({
        @NamedQuery(name = "ColorRecipesSearchItem.findByCode",
                query = "SELECT item FROM ColorRecipesSearchItem item  " +
                        " where lower(item.code) like :recipe " +
                        "order by item.code" +
                        "  ")
})

@Entity
@Table(name="V_COLOR_RECIPES_SEARCH")
@RedirectToEntityManager(redirectTo = MarchDataSourceEnum.DS_SQLMARCH8)
public class ColorRecipesSearchItem extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private int id;

    @Column(name="NAME")
    private String name ;

    @Column(name="CODE")
    private String code;

    @Column(name="COLOR_ID")
    private int colorId ;

    @Column(name = "COLOR_NAME")
    private String ColorName ;


    @Override
    //@TableHeader(name = "ID", width = -50, sequence = 1)
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }
    @TableHeader(name = "Рецептура_Наименование", sequence = 100)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @TableHeader(name = "Рецептура_Код", sequence = 20)
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getColorId() {
        return colorId;
    }

    public void setColorId(int colorId) {
        this.colorId = colorId;
    }

    @TableHeader(name = "Группа", sequence = 10)
    public String getColorName() {
        return ColorName;
    }

    public void setColorName(String colorName) {
        ColorName = colorName;
    }

    @Override
    public String toString() {
        return "ColorRecipesSearchItem{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", code='" + code + '\'' +
                ", colorId=" + colorId +
                ", ColorName='" + ColorName + '\'' +
                '}';
    }
}
