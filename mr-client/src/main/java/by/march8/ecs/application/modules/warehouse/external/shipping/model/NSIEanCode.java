package by.march8.ecs.application.modules.warehouse.external.shipping.model;

import by.march8.api.BaseEntity;
import by.march8.api.MarchDataSourceEnum;
import by.march8.api.RedirectToEntityManager;
import by.march8.api.TableHeader;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;


@NamedQueries({
        @NamedQuery(name = "NSIEanCode.findByItemCode",
                query = "SELECT item FROM NSIEanCode item WHERE item.itemCode = :itemCode  " +
                        "order by item.colorName" +
                        "  ")
})

/**
 * @author Andy 05.10.2018 - 8:32.
 */
@Entity
@Table(name = "V_NSI_EANCODE")
@RedirectToEntityManager(redirectTo = MarchDataSourceEnum.DS_SQLMARCH8)
public class NSIEanCode extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private int id;

    @Column(name = "ITEM_CODE")
    private int itemCode;

    @Column(name = "EANCODE")

    private String eanCode;

    @Column(name = "REF_COLOR_ID")
    private int colorId;

    @Column(name = "COLOR_NAME")
    private String colorName;


    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    public int getItemCode() {
        return itemCode;
    }

    public void setItemCode(int itemCode) {
        this.itemCode = itemCode;
    }

    @TableHeader(name = "EAN код", sequence = 20)
    public String getEanCode() {
        if (eanCode != null) {
            return eanCode.trim();
        } else {
            return "";
        }
    }

    public void setEanCode(String eanCode) {
        this.eanCode = eanCode;
    }

    public int getColorId() {
        return colorId;
    }

    public void setColorId(int colorId) {
        this.colorId = colorId;
    }

    @TableHeader(name = "Цвет", sequence = 10)
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
}
