package by.march8.entities.classifier;

import by.march8.api.BaseEntity;
import by.march8.api.RedirectToEntityManager;
import by.march8.api.TableHeader;
import by.march8.api.MarchDataSourceEnum;

import javax.persistence.*;

/**
 * @author Andy 18.10.2018 - 13:07.
 */
@NamedQueries({
        @NamedQuery(name = "ColorSearchItem.findByName",
                query = "SELECT item FROM ColorSearchItem item  " +
                        " where lower(item.colorName) like lower(:colorName) " +
                        "order by item.colorParentName" +
                        "  ")
})

@Entity
@Table(name = "V_COLOR_NSI_SEARCH")
@RedirectToEntityManager(redirectTo = MarchDataSourceEnum.DS_SQLMARCH8)
public class ColorSearchItem extends BaseEntity {

    @Id
    @Column(name = "COLOR_CODE")
    @TableHeader(name = "КОД", sequence = 40)
    private int code;
    @Column(name = "COLOR_ROOT_NAME")
    private String colorRootName;

    @Column(name = "COLOR_PARENT_NAME")
    private String colorParentName;

    @Column(name = "COLOR_NAME")
    private String colorName;

    @Column(name = "REF_COLOR_ID")
    private int parentId;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getColorRootName() {
        return colorRootName;
    }

    public void setColorRootName(String colorRootName) {
        this.colorRootName = colorRootName;
    }

    @TableHeader(name = "Группа", sequence = 10)
    public String getColorParentName() {
        if (colorParentName != null) {
            return colorParentName.trim();
        } else {
            return "";
        }
    }

    public void setColorParentName(String colorParentName) {
        this.colorParentName = colorParentName;
    }

    @TableHeader(name = "Цвет", sequence = 20)
    public String getColorName() {
        if (colorName != null) {
            return colorName.trim();
        } else {
            return "";
        }
    }

    public void setColorName(String colorName) {
        this.colorName = colorName;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }
}
